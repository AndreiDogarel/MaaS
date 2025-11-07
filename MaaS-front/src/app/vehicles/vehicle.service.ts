import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Vehicle } from './vehicle.model';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private base = '/api/vehicles';
  constructor(private http: HttpClient) {}
  create(v: Vehicle) { return this.http.post<Vehicle>(this.base, v); }
}
