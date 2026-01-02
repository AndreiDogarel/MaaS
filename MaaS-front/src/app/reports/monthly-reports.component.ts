import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-monthly-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './monthly-reports.component.html'
})
export class MonthlyReportsComponent implements OnInit {
  year = '';
  month = '';
  token = '';
  status = '';
  report: any = null;
  error: string | null = null;
  currentYear = '';
  currentMonth = '';

  listsConfig = [
    { key: 'vehiclesByType', title: 'Vehicles by Type' },
    { key: 'serviceByType', title: 'Service Cost by Type' },
    { key: 'topServiceVehicles', title: 'Top Service Vehicles' }
  ];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const now = new Date();
    this.currentYear = String(now.getFullYear());
    this.currentMonth = String(now.getMonth() + 1);
  }

  private buildUrl(): string {
    const params = new URLSearchParams();
    if (this.year) params.set('year', this.year);
    if (this.month) params.set('month', this.month);
    return '/api/reports/monthly-reports' + (params.toString() ? '?' + params.toString() : '');
  }

  loadReport(): void {
    this.status = 'Loading...';
    this.report = null;
    this.error = null;

    const url = this.buildUrl();
    let headers = new HttpHeaders({ Accept: 'application/json' });
    if (this.token && this.token.trim()) {
      headers = headers.set('Authorization', 'Bearer ' + this.token.trim());
    }

    this.http.get<any>(url, { headers }).subscribe({
      next: data => {
        this.report = data;
        this.status = 'OK';
      },
      error: err => {
        this.status = `Error: ${err.status || 'fetch'}`;
        this.error = (err.error && typeof err.error === 'string') ? err.error : JSON.stringify(err, null, 2);
      }
    });
  }

  toNumber(v: any): number {
    const n = Number(v);
    return Number.isFinite(n) ? n : 0;
  }
}
