import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule],
  template: `
    <div class="min-h-screen flex flex-col bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 font-sans transition-colors duration-300">
      <!-- Navbar -->
      <nav class="sticky top-0 z-50 backdrop-blur-lg bg-white/70 dark:bg-gray-900/70 border-b border-gray-200 dark:border-gray-800 shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16">
            <div class="flex">
              <div class="flex-shrink-0 flex items-center">
                <a routerLink="/" class="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent hover:scale-[1.02] transition-transform">
                  MaaS
                </a>
              </div>
              <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
                <a routerLink="/vehicles" routerLinkActive="border-blue-500 text-gray-900 dark:text-white" [routerLinkActiveOptions]="{exact: true}" class="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 dark:text-gray-300 hover:border-gray-300 hover:text-gray-700 dark:hover:text-gray-100 transition-colors">
                  Vehicles
                </a>
                <a *ngIf="authService.isAdmin$ | async" routerLink="/vehicles/new" routerLinkActive="border-blue-500 text-gray-900 dark:text-white" class="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 dark:text-gray-300 hover:border-gray-300 hover:text-gray-700 dark:hover:text-gray-100 transition-colors">
                  Add Vehicle
                </a>
                <a *ngIf="authService.isAdmin$ | async" routerLink="/customers" routerLinkActive="border-blue-500 text-gray-900 dark:text-white" [routerLinkActiveOptions]="{exact: true}" class="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 dark:text-gray-300 hover:border-gray-300 hover:text-gray-700 dark:hover:text-gray-100 transition-colors">
                  Manage Customers
                </a>
                <a *ngIf="authService.isAdmin$ | async"
                   routerLink="/reports/monthly"
                   routerLinkActive="border-blue-500 text-gray-900 dark:text-white"
                   class="inline-flex items-center ...">
                  Monthly Reports
                </a>
                <a *ngIf="authService.isCustomer$ | async" routerLink="/documents/upload" routerLinkActive="border-blue-500 text-gray-900 dark:text-white" class="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 dark:text-gray-300 hover:border-gray-300 hover:text-gray-700 dark:hover:text-gray-100 transition-colors">
                  Upload Documents
                </a>
                <a *ngIf="authService.isEmployee$ | async" routerLink="/createRentalContract" routerLinkActive="border-blue-500 text-gray-900 dark:text-white" class="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 dark:text-gray-300 hover:border-gray-300 hover:text-gray-700 dark:hover:text-gray-100 transition-colors">
                  Create Rental Contract
                </a>
              </div>
            </div>
            <div class="flex items-center">
              <ng-container *ngIf="authService.isLoggedIn$ | async; else loginButton">
                <button (click)="logout()" class="ml-8 inline-flex items-center justify-center px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-full shadow-sm text-sm font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all hover:shadow-md cursor-pointer">
                  Sign out
                </button>
              </ng-container>
              <ng-template #loginButton>
                <a routerLink="/auth/login" class="ml-8 inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-full shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all hover:shadow-md">
                    Sign in
                </a>
              </ng-template>
            </div>
          </div>
        </div>
      </nav>

      <!-- Main Content -->
      <main class="flex-grow w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
        <router-outlet></router-outlet>
      </main>

      <!-- Footer -->
      <footer class="bg-white dark:bg-gray-900 border-t border-gray-200 dark:border-gray-800">
        <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          <p class="text-center text-sm text-gray-500 dark:text-gray-400">
            &copy; 2025 MaaS Rental System. All rights reserved.
          </p>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(10px); }
      to { opacity: 1; transform: translateY(0); }
    }
    .animate-fade-in {
      animation: fadeIn 0.5s ease-out forwards;
    }
  `]
})
export class MainLayoutComponent {
  authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
