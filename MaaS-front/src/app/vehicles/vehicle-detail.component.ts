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

    this.vehicleService.addMaintenanceRecord(this.vehicle.id, payload).subscribe({
      next: (data) => {
        this.maintenanceHistory.push(data);
        this.resetMaintenanceForm();
        this.maintenanceFormSubmitting = false;
        this.showMaintenanceForm = false;
      },
      error: (err) => {
        console.error('Failed to add maintenance record', err);
        alert('Failed to add maintenance record');
        this.maintenanceFormSubmitting = false;
      }
    });
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

    this.vehicleService.addTowingRecord(this.vehicle.id, payload).subscribe({
      next: (data) => {
        this.towingHistory.push(data);
        this.resetTowingForm();
        this.towingFormSubmitting = false;
        this.showTowingForm = false;
      },
      error: (err) => {
        console.error('Failed to add towing record', err);
        alert('Failed to add towing record');
        this.towingFormSubmitting = false;
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
    }
  }

  toggleTowingForm(): void {
    this.showTowingForm = !this.showTowingForm;
    if (!this.showTowingForm) {
      this.resetTowingForm();
    }
  }
}
