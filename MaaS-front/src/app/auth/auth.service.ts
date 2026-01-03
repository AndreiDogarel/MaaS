import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
    isLoggedIn$ = this.isLoggedInSubject.asObservable();

    private isAdminSubject = new BehaviorSubject<boolean>(false);
    isAdmin$ = this.isAdminSubject.asObservable();

    private isCustomerSubject = new BehaviorSubject<boolean>(false);
    isCustomer$ = this.isCustomerSubject.asObservable();

    private http = inject(HttpClient);
    private router = inject(Router);
    private apiUrl = 'http://localhost:8080/api/auth';

    constructor() {
        if (this.hasToken()) {
            this.validateToken();
        }
    }

    private hasToken(): boolean {
        return !!localStorage.getItem('token');
    }

    isAuthenticated(): boolean {
        return this.isLoggedInSubject.value;
    }

    login(token: string): void {
        localStorage.setItem('token', token);
        this.isLoggedInSubject.next(true);
        this.validateToken();
    }

    loginApi(credentials: any): Observable<string> {
        return this.http.post(this.apiUrl + '/login', credentials, { responseType: 'text' });
    }

    registerApio(credentials: any): Observable<string> {
        return this.http.post(this.apiUrl + '/register', credentials, { responseType: 'text' });
    }    

    logout(): void {
        localStorage.removeItem('token');
        this.isLoggedInSubject.next(false);
        this.isAdminSubject.next(false);
        this.router.navigate(['/auth/login']);
    }

    validateToken(): void {
        const token = localStorage.getItem('token');
        if (!token) return;

        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

        this.http.get<any>(`${this.apiUrl}/validate`, { headers }).pipe(
            tap(response => {
                if (response && response.role === 'ADMIN') {
                    this.isAdminSubject.next(true);
                } else if (response && response.role === 'CUSTOMER') {
                    this.isCustomerSubject.next(true);
                }
                else {
                    this.isAdminSubject.next(false);
                }
            }),
            catchError(error => {
                console.error('Token validation failed', error);
                this.logout();
                return of(null);
            })
        ).subscribe();
    }
}
