import { Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { VehicleFormComponent } from './vehicles/vehicle-form.component';

export const routes: Routes = [
    { path: 'users', component: UsersListComponent },
    { path: 'vehicles/new', component: VehicleFormComponent }
];
