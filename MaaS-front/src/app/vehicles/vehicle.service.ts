import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Vehicle } from './vehicle.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private base = 'http://localhost:8080/api/vehicles';
  constructor(private http: HttpClient) {}
  create(v: Vehicle) { return this.http.post<Vehicle>(this.base, v); }

  getAllVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.base}/all`);
  }
}
