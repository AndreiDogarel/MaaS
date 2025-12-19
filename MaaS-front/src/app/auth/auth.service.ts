import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
    isLoggedIn$ = this.isLoggedInSubject.asObservable();

    constructor() { }

    private hasToken(): boolean {
        return !!localStorage.getItem('token');
    }

    isAuthenticated(): boolean {
        return this.isLoggedInSubject.value;
    }

    login(token: string): void {
        localStorage.setItem('token', token);
        this.isLoggedInSubject.next(true);
    }

    logout(): void {
        localStorage.removeItem('token');
        this.isLoggedInSubject.next(false);
    }
}
