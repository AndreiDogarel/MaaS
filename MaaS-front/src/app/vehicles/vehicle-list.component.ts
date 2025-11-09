import { Component, OnInit } from '@angular/core';
import { VehicleService } from './vehicle.service';
import { Vehicle } from './vehicle.model';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
    imports: [CommonModule, DecimalPipe, FormsModule]
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  isLoading: boolean = true;
  error: any = null;

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
}