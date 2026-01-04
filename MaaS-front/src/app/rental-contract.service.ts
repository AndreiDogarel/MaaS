// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';



// export interface RentalContractCreateRequest {
//   clientId: number;
//   vehicleId: number;
//   startDate: string; // yyyy-MM-dd
//   endDate: string;   // yyyy-MM-dd
//   operatorId: number;
// }

// export interface RentalContractDto {
//   startDate: string;
//   endDate: string;
//   registrationPlate: string;
//   username: string;
// }


// @Injectable({
//   providedIn: 'root'
// })
// export class RentalContractService {

//   private apiUrl = 'http://localhost:8080/api/vehicles';

//   constructor(private http: HttpClient) {}

//   /**
//    * POST /api/rental-contracts
//    * Creează un contract nou
//    */
//   createContract(
//     request: RentalContractCreateRequest
//   ): Observable<RentalContractCreateRequest> {
//     return this.http.post<RentalContractCreateRequest>(
//       this.apiUrl,
//       request
//     );
//   }

//   /**
//    * GET /api/rentals/getPending
//    * Returnează contractele pending
//    */
//   getPendingRentals(): Observable<RentalContractDto[]> {
//     return this.http.get<RentalContractDto[]>(
//       `${this.apiUrl}/rentals/getPending`
//     );
//   }
// }

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RentalContractCreateRequest {
  registrationNumber: string;
  startDate: string; // yyyy-MM-dd
  endDate: string;   // yyyy-MM-dd
}

export interface RentalContractDto {
  startDate: string;
  endDate: string;
  registrationPlate: string;
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class RentalContractService {

  private apiUrl = 'http://localhost:8080/api/vehicles';
  private postUrl = 'http://localhost:8080/api/contracts';

  constructor(private http: HttpClient) {}

  /** POST /api/vehicles */
  createContract(
    request: RentalContractCreateRequest
  ): Observable<any> {
    return this.http.post(
      this.postUrl,
      request
    );
  }

  /** GET /api/vehicles/rentals/getPending */
  getPendingRentals(): Observable<RentalContractDto[]> {
    return this.http.get<RentalContractDto[]>(
      `${this.apiUrl}/rentals/getPending`
    );
  }
}