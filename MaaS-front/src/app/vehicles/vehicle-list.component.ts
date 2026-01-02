import { Component, OnInit, inject } from '@angular/core';
import { VehicleService } from './vehicle.service';
import { Vehicle } from './vehicle.model';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, DecimalPipe]
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  isLoading = true;
  error: any = null;
  authService = inject(AuthService);

  search = {
    brand: '',
    model: '',
    licenseCategory: '',
    status: '',
    year: null as number | null
  };

  constructor(private vehicleService: VehicleService) { }

  ngOnInit(): void {
    this.executeSearch();
  }

  executeSearch(): void {
    this.isLoading = true;
    this.error = null;

    this.vehicleService.searchVehicles(this.search).subscribe({
      next: (data) => {
        this.vehicles = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to fetch vehicles:', err);
        this.error = 'Could not load vehicle data.';
        this.isLoading = false;
      }
    });
  }

  resetSearch(): void {
    this.search = {
      brand: '',
      model: '',
      licenseCategory: '',
      status: '',
      year: null
    };

    this.executeSearch();
  }

  getVehicleImage(vehicle: Vehicle): string {
    // Generate a deterministic image based on ID or brand
    // For demo purposes, we return a random high-quality car image from Unsplash
    // In a real app, this would come from the backend or an image service
    const keywords = `${vehicle.brand} ${vehicle.model} car`;
    return `https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&q=80&w=400`; // Placeholder for now to avoid broken dynamic links
    // Ideally: return `https://source.unsplash.com/400x300/?car,${encodeURIComponent(vehicle.brand)}`;
    // But source.unsplash is deprecated/unreliable for static consistency without ID.
    // Let's use a clear placeholder or rotate a few images.
  }

  getRandomImage(id: number): string {
    const images = [
      'https://images.unsplash.com/photo-1550355291-bbee04a92027?auto=format&fit=crop&q=80&w=800',
      'https://images.unsplash.com/photo-1542282088-fe8426682b8f?auto=format&fit=crop&q=80&w=800', // Ferrari
      'https://images.unsplash.com/photo-1503376763036-066120622c74?auto=format&fit=crop&q=80&w=800',
      'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&q=80&w=800', // Ferrari red
      'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&q=80&w=800' // Tesla
    ];
    return images[id % images.length];
  }

  deleteDecommissionedVehicles(): void {
    if (!confirm('Are you sure you want to delete all decommissioned vehicles? This action cannot be undone.')) {
      return;
    }

    this.vehicleService.deleteDecommissionedVehicles().subscribe({
      next: (response) => {
        alert('Decommissioned vehicles deleted successfully');
        this.executeSearch(); // Refresh the list
      },
      error: (err) => {
        console.error('Failed to delete decommissioned vehicles:', err);
        alert('Failed to delete decommissioned vehicles');
      }
    });
  }
}
