import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { VehicleService } from './vehicle.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { RentalStatus } from '../rentals/rental-status';
import { Vehicle } from './vehicle.model';

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
  rentalHistory: any[] = [];
  isLoading = true;
  error: string | null = null;

  // Admin edit state for vehicle overview
  isEditingVehicle = false;
  vehicleEditModel: Partial<Vehicle> = {};
  vehicleEditSubmitting = false;

  showMaintenanceForm = false;
  showTowingForm = false;
  showRentalForm = false;
  maintenanceFormSubmitting = false;
  towingFormSubmitting = false;
  rentalFormSubmitting = false;

  isEditingMaintenance = false;
  editingMaintenanceId: number | null = null;

  isEditingTowing = false;
  editingTowingId: number | null = null;

  isEditingRental = false;
  editingRentalId: number | null = null;

  rentalStatusValues = Object.values(RentalStatus);

  maintenanceForm = {
    date: '',
    type: '',
    description: '',
    cost: null as number | null,
    inspection: false,
    nextInspectionDate: '',
    mileage: null as number | null
  };

  towingForm = {
    date: '',
    location: '',
    reason: '',
    duration: null as number | null
  };

  rentalForm = {
    startDate: '',
    endDate: '',
    status: '',
    odometerStart: null as number | null,
    odometerEnd: null as number | null,
    totalPrice: null as number | null
  };

  // include 'admin' in allowed tab values
  activeTab: 'overview' | 'maintenance' | 'towing' | 'rentals' = 'overview';

  constructor(
    private route: ActivatedRoute,
    private vehicleService: VehicleService,
    public authService: AuthService
  ) { }

  beginEditVehicle(): void {
    if (!this.vehicle) return;
    this.isEditingVehicle = true;
    // shallow clone so template edits don't immediately change the displayed vehicle until saved
    this.vehicleEditModel = { ...this.vehicle };
  }

  cancelEditVehicle(): void {
    this.isEditingVehicle = false;
    this.vehicleEditModel = {};
  }

  saveVehicle(): void {
    if (!this.vehicle || !this.vehicle.id) return alert('No vehicle loaded');
    // Basic validation: only require fields that the admin update endpoint supports
    if (this.vehicleEditModel.mileage == null || !this.vehicleEditModel.status) {
      return alert('Please provide mileage and status');
    }

    this.vehicleEditSubmitting = true;

    // Only send the fields the backend's VehicleUpdateDto expects / updates
    const payload: any = {
      mileage: this.vehicleEditModel.mileage,
      status: this.vehicleEditModel.status,
      licenseCategory: this.vehicleEditModel.licenseCategory ?? null
    };

    // Include optional fields if they were provided in the edit model
    if (this.vehicleEditModel.registrationNumber !== undefined) payload.registrationNumber = this.vehicleEditModel.registrationNumber;
    if (this.vehicleEditModel.brand !== undefined) payload.brand = this.vehicleEditModel.brand;
    if (this.vehicleEditModel.model !== undefined) payload.model = this.vehicleEditModel.model;
    if (this.vehicleEditModel.year !== undefined) payload.year = this.vehicleEditModel.year;
    if (this.vehicleEditModel.pricePerDay !== undefined) payload.pricePerDay = this.vehicleEditModel.pricePerDay;

    // Debug: log payload and token presence
    try {
      const token = localStorage.getItem('token');
      // eslint-disable-next-line no-console
      console.log('Updating vehicle', { id: this.vehicle?.id, payload, tokenPresent: !!token });
    } catch (e) {
      // ignore in non-browser environments
    }

    this.vehicleService.updateVehicle(String(this.vehicle.id), payload).subscribe({
      next: (updated: any) => {
        // backend returns the DTO we sent — merge into local vehicle for immediate UI update
        this.vehicle = { ...this.vehicle, ...payload };
        this.vehicleEditSubmitting = false;
        this.cancelEditVehicle();
        alert('Vehicle updated successfully');
      },
      error: (err: any) => {
        console.error('Failed to update vehicle', err);
        this.vehicleEditSubmitting = false;
        if (err && err.status === 403) {
          alert('Failed to update vehicle: you are not authorized. Please login as an admin.');
        } else {
          alert('Failed to save vehicle changes');
        }
      }
    });
  }

  setActiveTab(tab: 'overview' | 'maintenance' | 'towing' | 'rentals'): void {
    this.activeTab = tab;
  }

  getVehicleImage(vehicle: any): string {
    return `https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&q=80&w=1200`;
  }

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
        this.loadRentalHistory(id);
      },
      error: () => {
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

  loadRentalHistory(id: string): void {
    this.vehicleService.getRentalHistory(id).subscribe({
      next: (data) => {
        this.rentalHistory = data;
      },
      error: (err) => console.error('Failed to load rental history', err)
    });
  }

  beginEditMaintenance(record: any): void {
    this.isEditingMaintenance = true;
    this.editingMaintenanceId = record.id ?? record.maintenanceId ?? null;
    this.showMaintenanceForm = true;
    this.maintenanceForm = {
      date: record.date ? record.date.split('T')[0] : '',
      type: record.type || '',
      description: record.description || '',
      cost: record.cost ?? null,
      inspection: record.inspection ?? false,
      nextInspectionDate: record.nextInspection ? record.nextInspection.split('T')[0] : '',
      mileage: record.mileage ?? null
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
      inspection: this.maintenanceForm.inspection,
      nextInspectionDate: this.maintenanceForm.nextInspectionDate || null,
      mileage: this.maintenanceForm.mileage,
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

  beginEditRental(record: any): void {
    this.isEditingRental = true;
    this.editingRentalId = record.id ?? record.rentalId ?? null;
    this.showRentalForm = true;
    this.rentalForm = {
      startDate: record.startDate ? record.startDate.split('T')[0] : '',
      endDate: record.endDate ? record.endDate.split('T')[0] : '',
      status: record.status || '',
      odometerStart: record.odometerStart ?? null,
      odometerEnd: record.odometerEnd ?? null,
      totalPrice: record.totalPrice ?? null
    };
  }

  submitRentalForm(): void {
    if (!this.rentalForm.startDate || !this.rentalForm.endDate) {
      alert('Please fill in required fields (start date and end date)');
      return;
    }

    this.rentalFormSubmitting = true;

    const payload: any = {
      startDate: this.rentalForm.startDate,
      endDate: this.rentalForm.endDate,
      vehicleId: this.vehicle.id
    };

    if (this.isEditingRental && this.editingRentalId != null) {
      const updatePayload: any = {
        startDate: this.rentalForm.startDate,
        endDate: this.rentalForm.endDate || null,
        status: this.rentalForm.status || null,
        odometerStart: this.rentalForm.odometerStart,
        odometerEnd: this.rentalForm.odometerEnd,
        totalPrice: this.rentalForm.totalPrice
      };

      this.vehicleService.updateRentalRecord(String(this.vehicle.id), String(this.editingRentalId), updatePayload).subscribe({
        next: (data) => {
          const idx = this.rentalHistory.findIndex((r: any) => (r.id ?? r.rentalId) === this.editingRentalId);
          if (idx !== -1) this.rentalHistory[idx] = data;
          this.afterRentalSaved();
        },
        error: (err) => {
          console.error('Failed to update rental record', err);
          alert('Failed to update rental record');
          this.rentalFormSubmitting = false;
        }
      });
    } else {
      this.vehicleService.addRentalRecord(String(this.vehicle.id), payload).subscribe({
        next: (data) => {
          this.rentalHistory.push(data);
          this.afterRentalSaved();
        },
        error: (err) => {
          console.error('Failed to add rental record', err);
          alert('Failed to add rental record');
          this.rentalFormSubmitting = false;
        }
      });
    }
  }

  afterRentalSaved(): void {
    this.resetRentalForm();
    this.rentalFormSubmitting = false;
    this.showRentalForm = false;
    this.isEditingRental = false;
    this.editingRentalId = null;
  }

  cancelRentalEdit(): void {
    this.isEditingRental = false;
    this.editingRentalId = null;
    this.toggleRentalForm();
  }

  deleteRental(record: any): void {
    const id = record.id ?? record.rentalId;
    if (!id) return alert('Cannot determine rental id');
    if (!confirm('Delete this rental record?')) return;
    this.vehicleService.deleteRentalRecord(String(this.vehicle.id), String(id)).subscribe({
      next: () => {
        this.rentalHistory = this.rentalHistory.filter((r: any) => (r.id ?? r.rentalId) !== id);
      },
      error: (err) => {
        console.error('Failed to delete rental record', err);
        alert('Failed to delete rental record');
      }
    });
  }

  resetMaintenanceForm(): void {
    this.maintenanceForm = {
      date: '',
      type: '',
      description: '',
      cost: null,
      inspection: false,
      nextInspectionDate: '',
      mileage: null
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

  resetRentalForm(): void {
    this.rentalForm = {
      startDate: '',
      endDate: '',
      status: '',
      odometerStart: null,
      odometerEnd: null,
      totalPrice: null
    };
  }

  toggleMaintenanceForm(): void {
    console.log('Toggling maintenance form');
    this.showMaintenanceForm = !this.showMaintenanceForm;
    if (!this.showMaintenanceForm) {
      this.resetMaintenanceForm();
      this.isEditingMaintenance = false;
      this.editingMaintenanceId = null;
    }
  }

  toggleTowingForm(): void {
    console.log('Toggling towing form');
    this.showTowingForm = !this.showTowingForm;
    if (!this.showTowingForm) {
      this.resetTowingForm();
      this.isEditingTowing = false;
      this.editingTowingId = null;
    }
  }

  toggleRentalForm(): void {
    console.log('Toggling rental form');
    this.showRentalForm = !this.showRentalForm;
    if (!this.showRentalForm) {
      this.resetRentalForm();
      this.isEditingRental = false;
      this.editingRentalId = null;
    }
  }
}
