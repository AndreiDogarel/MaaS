import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { VehicleService } from './vehicle.service';


@Component({
  selector: 'app-vehicle-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-form.component.html'
})

export class VehicleFormComponent implements OnInit {
  form!: FormGroup;
  submitting = false;
  errorMsg = '';

  constructor(
    private fb: FormBuilder,
    private api: VehicleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      registrationNumber: ['', [Validators.required, Validators.pattern(/^[A-Z0-9-]{3,32}$/)]],
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: [new Date().getFullYear(), [Validators.required, Validators.min(1980), Validators.max(2100)]],
      mileage: [0, [Validators.required, Validators.min(0)]],
      licenseCategory: ['B', Validators.required],
      status: ['AVAILABLE', Validators.required],
      pricePerDay: [0, [Validators.required, Validators.min(0)]],
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.api.create(this.form.value).subscribe({
      next: () => {
        this.submitting = false;
        this.router.navigate(['/vehicles']);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMsg = err?.error?.error ?? 'Submit failed';
      }
    });
  }
}
