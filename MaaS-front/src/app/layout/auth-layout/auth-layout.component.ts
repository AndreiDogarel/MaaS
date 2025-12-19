import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-auth-layout',
    standalone: true,
    imports: [RouterOutlet],
    template: `
    <div class="min-h-screen flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-gray-50 dark:bg-gray-900 transition-colors duration-300 bg-[url('https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?auto=format&fit=crop&q=80')] bg-cover bg-center">
      <div class="absolute inset-0 bg-white/30 dark:bg-black/40 backdrop-blur-sm"></div>
      
      <div class="relative z-10 sm:mx-auto sm:w-full sm:max-w-md">
        <h2 class="mt-6 text-center text-3xl font-extrabold text-white tracking-tight drop-shadow-md">
          <span class="bg-clip-text">MaaS Rental</span>
        </h2>
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
