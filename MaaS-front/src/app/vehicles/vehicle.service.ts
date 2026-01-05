import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Vehicle } from './vehicle.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private base = 'http://localhost:8080/api/vehicles';

  constructor(private http: HttpClient) {}

  create(v: Vehicle) {
    return this.http.post<Vehicle>(this.base, v);
  }

  getAllVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.base}/all`);
  }

  searchVehicles(params: { [key: string]: any }): Observable<Vehicle[]> {
    let httpParams = new HttpParams();

    for (const key in params) {
      const value = params[key];
      if (value !== null && value !== undefined && value !== '') {
        httpParams = httpParams.append(key, value);
      }
    }

    return this.http.get<Vehicle[]>(`${this.base}/search`, { params: httpParams });
  }

  getVehicleById(id: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.base}/${id}`);
  }

  getMaintenanceHistory(id: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/${id}/maintenance`);
  }

  getTowingHistory(id: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/${id}/towing`);
  }

  addMaintenanceRecord(id: string, maintenance: any): Observable<any> {
    return this.http.post<any>(`${this.base}/${id}/maintenance`, maintenance);
  }

  addTowingRecord(id: string, towing: any): Observable<any> {
    return this.http.post<any>(`${this.base}/${id}/towing`, towing);
  }

  updateMaintenanceRecord(vehicleId: string, maintenanceId: string | number, maintenance: any): Observable<any> {
    return this.http.put<any>(`${this.base}/${vehicleId}/maintenance/${maintenanceId}`, maintenance);
  }

  deleteMaintenanceRecord(vehicleId: string, maintenanceId: string | number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${vehicleId}/maintenance/${maintenanceId}`);
  }

  updateTowingRecord(vehicleId: string, towingId: string | number, towing: any): Observable<any> {
    return this.http.put<any>(`${this.base}/${vehicleId}/towing/${towingId}`, towing);
  }

  deleteTowingRecord(vehicleId: string, towingId: string | number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${vehicleId}/towing/${towingId}`);
  }

  getRentalHistory(id: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/${id}/rentals`);
  }

  addRentalRecord(id: string, rental: any): Observable<any> {
    return this.http.post<any>(`${this.base}/${id}/rentals`, rental);
  }

  updateRentalRecord(vehicleId: string, rentalId: string | number, rental: any): Observable<any> {
    return this.http.put<any>(`${this.base}/${vehicleId}/rentals/${rentalId}`, rental);
  }

  deleteRentalRecord(vehicleId: string, rentalId: string | number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${vehicleId}/rentals/${rentalId}`);
  }

  // Update a vehicle by id (admin)
  updateVehicle(id: string | number, vehicle: Partial<Vehicle>): Observable<any> {
    // include Authorization header if token exists (backend likely protects this endpoint)
    const token = localStorage.getItem('token');
    let options: { headers?: HttpHeaders } = {};
    if (token) {
      options.headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    }
    // backend exposes admin update at /api/admin/vehicle/{id}
    return this.http.put<any>(`http://localhost:8080/api/admin/vehicle/${id}`, vehicle, options);
  }

  deleteDecommissionedVehicles(): Observable<any> {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.delete('http://localhost:8080/api/admin/deleteDecommissionedVehicles', { headers, responseType: 'text' });
  }
}
