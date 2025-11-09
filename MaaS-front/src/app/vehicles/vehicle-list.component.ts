import { Component, OnInit } from '@angular/core';
import { VehicleService } from './vehicle.service';
import { Vehicle } from './vehicle.model';
import { CommonModule, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
    imports: [CommonModule, DecimalPipe]
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  isLoading: boolean = true;
  error: any = null;

  constructor(private vehicleService: VehicleService) { }

  ngOnInit(): void {
    this.fetchVehicles();
  }

  fetchVehicles(): void {
    this.isLoading = true;
    this.vehicleService.getAllVehicles().subscribe({
      next: (data) => {
        console.log('Fetched vehicles:', data);
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
}