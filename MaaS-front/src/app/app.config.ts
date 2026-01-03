// // import { ApplicationConfig, provideZoneChangeDetection,importProvidersFrom} from '@angular/core';
// // import { provideRouter } from '@angular/router';
// // import { HttpClientModule } from '@angular/common/http';
// // import { routes } from './app.routes';
// // import { provideClientHydration, withEventReplay } from '@angular/platform-browser';


// // export const appConfig: ApplicationConfig = {
// //   providers: [
// //     provideZoneChangeDetection({ eventCoalescing: true }), 
// //     provideRouter(routes), 
// //     importProvidersFrom(HttpClientModule),
// //     provideClientHydration(withEventReplay())]
    
// // };

// import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
// import { provideRouter } from '@angular/router';
// import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
// import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

// import { routes } from './app.routes';
// import { AuthInterceptor } from './auth.interceptor';

// export const appConfig: ApplicationConfig = {
//   providers: [
//     provideZoneChangeDetection({ eventCoalescing: true }),

//     provideRouter(routes),

//     provideHttpClient(
//       withInterceptorsFromDi()
//     ),

//     provideClientHydration(withEventReplay()),

//     AuthInterceptor
//   ]
// };

import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { routes } from './app.routes';
import { AuthInterceptor } from './auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    provideClientHydration(withEventReplay()),

    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
};