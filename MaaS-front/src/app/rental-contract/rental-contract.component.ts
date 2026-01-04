import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RentalContractService, RentalContractDto } from '../rental-contract.service';
import {
  RentalContractCreateRequest
} from '../rental-contract.service';

@Component({
  selector: 'app-rental-contract',
  styleUrls: ['./rental-contract.component.css'],
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rental-contract.component.html'
})
export class RentalContractComponent implements OnInit {

  rentals: RentalContractDto[] = [];
  loading = false;
  error = '';

  constructor(private rentalService: RentalContractService) {}

  ngOnInit(): void {
    this.loadPendingRentals();
  }

  loadPendingRentals(): void {
    this.loading = true;
    this.error = '';

    this.rentalService.getPendingRentals().subscribe({
      next: (data) => {
        this.rentals = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load pending rentals';
        this.loading = false;
      }
    });
  }
  createContract(rental: RentalContractDto): void {
    const request: RentalContractCreateRequest = {
      registrationNumber: rental.registrationPlate,
      startDate: rental.startDate,
      endDate: rental.endDate
    };

    this.rentalService.createContract(request).subscribe({
      next: () => {
        // refresh table după creare
        this.loadPendingRentals();
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to create contract';
      }
    });
  }
}
