import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="min-h-screen flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-gray-50 dark:bg-gray-900 transition-colors duration-300 bg-[url('https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?auto=format&fit=crop&q=80')] bg-cover bg-center">
      <div class="absolute inset-0 bg-white/30 dark:bg-black/40 backdrop-blur-sm"></div>
      
      <div class="flex items-center justify-center z-10 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="flex-shrink-0 flex items-center">
          <a routerLink="/" class="text-5xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent hover:scale-[1.02] transition-transform">
            MaaS
          </a>
        </div>
      </div>

      <div class="relative z-10 mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="bg-white/90 dark:bg-gray-800/90 py-8 px-4 shadow-2xl sm:rounded-2xl sm:px-10 border border-white/20 backdrop-blur-md">
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `
})
export class AuthLayoutComponent { }
