import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserDto {
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/showUsers';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(this.apiUrl);
  }
}