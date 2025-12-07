import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VehicleService } from './vehicle.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vehicle-detail',
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.css'],
  imports: [CommonModule, RouterModule, FormsModule],
  standalone: true
})
export class VehicleDetailComponent implements OnInit {
  vehicle: any;
  maintenanceHistory: any[] = [];
  towingHistory: any[] = [];
  isLoading = true;
  error: string | null = null;

  // Form visibility and data
  showMaintenanceForm = false;
  showTowingForm = false;
  maintenanceFormSubmitting = false;
  towingFormSubmitting = false;

  // Edit state
  isEditingMaintenance = false;
  editingMaintenanceId: number | null = null;

  isEditingTowing = false;
  editingTowingId: number | null = null;

  maintenanceForm = {
    date: '',
    type: '',
    description: '',
    cost: null as number | null
  };

  towingForm = {
    date: '',
    location: '',
    reason: '',
    duration: null as number | null
  };

  constructor(
    private route: ActivatedRoute,
    private vehicleService: VehicleService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.loadVehicleDetails(id);
    });
  }

  loadVehicleDetails(id: string): void {
    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle = data;
        this.loadMaintenanceHistory(id);
        this.loadTowingHistory(id);
      },
      error: (err) => {
        this.error = 'Failed to load vehicle details';
        this.isLoading = false;
      }
    });
  }

  loadMaintenanceHistory(id: string): void {
    this.vehicleService.getMaintenanceHistory(id).subscribe({
      next: (data) => {
        this.maintenanceHistory = data;
      },
      error: (err) => console.error('Failed to load maintenance history', err)
    });
  }

  loadTowingHistory(id: string): void {
    this.vehicleService.getTowingHistory(id).subscribe({
      next: (data) => {
        this.towingHistory = data;
        this.isLoading = false;
      },
      error: (err) => console.error('Failed to load towing history', err)
    });
  }

  /* Maintenance */
  beginEditMaintenance(record: any): void {
    this.isEditingMaintenance = true;
    this.editingMaintenanceId = record.id ?? record.maintenanceId ?? null;
    this.showMaintenanceForm = true;
    this.maintenanceForm = {
      date: record.date ? record.date.split('T')[0] : '',
      type: record.type || '',
      description: record.description || '',
      cost: record.cost ?? null
    };
  }

  submitMaintenanceForm(): void {
    if (!this.maintenanceForm.date || !this.maintenanceForm.type) {
      alert('Please fill in required fields (date and type)');
      return;
    }

    this.maintenanceFormSubmitting = true;
    const payload = {
      date: this.maintenanceForm.date,
      type: this.maintenanceForm.type,
      description: this.maintenanceForm.description,
      cost: this.maintenanceForm.cost,
      vehicleId: this.vehicle.id
    };

    if (this.isEditingMaintenance && this.editingMaintenanceId != null) {
      this.vehicleService.updateMaintenanceRecord(String(this.vehicle.id), String(this.editingMaintenanceId), payload).subscribe({
        next: (data) => {
          const idx = this.maintenanceHistory.findIndex((r: any) => (r.id ?? r.maintenanceId) === this.editingMaintenanceId);
          if (idx !== -1) this.maintenanceHistory[idx] = data;
          this.afterMaintenanceSaved();
        },
        error: (err) => {
          console.error('Failed to update maintenance record', err);
          alert('Failed to update maintenance record');
          this.maintenanceFormSubmitting = false;
        }
      });
    } else {
      this.vehicleService.addMaintenanceRecord(String(this.vehicle.id), payload).subscribe({
        next: (data) => {
          this.maintenanceHistory.push(data);
          this.afterMaintenanceSaved();
        },
        error: (err) => {
          console.error('Failed to add maintenance record', err);
          alert('Failed to add maintenance record');
          this.maintenanceFormSubmitting = false;
        }
      });
    }
  }

  afterMaintenanceSaved(): void {
    this.resetMaintenanceForm();
    this.maintenanceFormSubmitting = false;
    this.showMaintenanceForm = false;
    this.isEditingMaintenance = false;
    this.editingMaintenanceId = null;
  }

  cancelMaintenanceEdit(): void {
    this.isEditingMaintenance = false;
    this.editingMaintenanceId = null;
    this.toggleMaintenanceForm();
  }

  deleteMaintenance(record: any): void {
    const id = record.id ?? record.maintenanceId;
    if (!id) return alert('Cannot determine maintenance id');
    if (!confirm('Delete this maintenance record?')) return;
    this.vehicleService.deleteMaintenanceRecord(String(this.vehicle.id), String(id)).subscribe({
      next: () => {
        this.maintenanceHistory = this.maintenanceHistory.filter((r: any) => (r.id ?? r.maintenanceId) !== id);
      },
      error: (err) => {
        console.error('Failed to delete maintenance record', err);
        alert('Failed to delete maintenance record');
      }
    });
  }

  /* Towing */
  beginEditTowing(record: any): void {
    this.isEditingTowing = true;
    this.editingTowingId = record.id ?? record.towingId ?? null;
    this.showTowingForm = true;
    this.towingForm = {
      date: record.date ? record.date.split('T')[0] : '',
      location: record.location || '',
      reason: record.reason || '',
      duration: record.duration ?? null
    };
  }

  submitTowingForm(): void {
    if (!this.towingForm.date || !this.towingForm.location) {
      alert('Please fill in required fields (date and location)');
      return;
    }

    this.towingFormSubmitting = true;
    const payload = {
      date: this.towingForm.date,
      location: this.towingForm.location,
      reason: this.towingForm.reason,
      duration: this.towingForm.duration,
      vehicleId: this.vehicle.id
    };

    if (this.isEditingTowing && this.editingTowingId != null) {
      this.vehicleService.updateTowingRecord(String(this.vehicle.id), String(this.editingTowingId), payload).subscribe({
        next: (data) => {
          const idx = this.towingHistory.findIndex((r: any) => (r.id ?? r.towingId) === this.editingTowingId);
          if (idx !== -1) this.towingHistory[idx] = data;
          this.afterTowingSaved();
        },
        error: (err) => {
          console.error('Failed to update towing record', err);
          alert('Failed to update towing record');
          this.towingFormSubmitting = false;
        }
      });
    } else {
      this.vehicleService.addTowingRecord(String(this.vehicle.id), payload).subscribe({
        next: (data) => {
          this.towingHistory.push(data);
          this.afterTowingSaved();
        },
        error: (err) => {
          console.error('Failed to add towing record', err);
          alert('Failed to add towing record');
          this.towingFormSubmitting = false;
        }
      });
    }
  }

  afterTowingSaved(): void {
    this.resetTowingForm();
    this.towingFormSubmitting = false;
    this.showTowingForm = false;
    this.isEditingTowing = false;
    this.editingTowingId = null;
  }

  cancelTowingEdit(): void {
    this.isEditingTowing = false;
    this.editingTowingId = null;
    this.toggleTowingForm();
  }

  deleteTowing(record: any): void {
    const id = record.id ?? record.towingId;
    if (!id) return alert('Cannot determine towing id');
    if (!confirm('Delete this towing record?')) return;
    this.vehicleService.deleteTowingRecord(String(this.vehicle.id), String(id)).subscribe({
      next: () => {
        this.towingHistory = this.towingHistory.filter((r: any) => (r.id ?? r.towingId) !== id);
      },
      error: (err) => {
        console.error('Failed to delete towing record', err);
        alert('Failed to delete towing record');
      }
    });
  }

  resetMaintenanceForm(): void {
    this.maintenanceForm = {
      date: '',
      type: '',
      description: '',
      cost: null
    };
  }

  resetTowingForm(): void {
    this.towingForm = {
      date: '',
      location: '',
      reason: '',
      duration: null
    };
  }

  toggleMaintenanceForm(): void {
    this.showMaintenanceForm = !this.showMaintenanceForm;
    if (!this.showMaintenanceForm) {
      this.resetMaintenanceForm();
      this.isEditingMaintenance = false;
      this.editingMaintenanceId = null;
    }
  }

  toggleTowingForm(): void {
    this.showTowingForm = !this.showTowingForm;
    if (!this.showTowingForm) {
      this.resetTowingForm();
      this.isEditingTowing = false;
      this.editingTowingId = null;
    }
  }
}
