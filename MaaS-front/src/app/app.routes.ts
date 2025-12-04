import { Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { VehicleFormComponent } from './vehicles/vehicle-form.component';
import { VehicleListComponent } from './vehicles/vehicle-list.component';
import { VehicleDetailComponent } from './vehicles/vehicle-detail.component';

export const routes: Routes = [
    { path: 'users', component: UsersListComponent },
    { path: 'vehicles', component: VehicleListComponent },
    { path: 'vehicles/new', component: VehicleFormComponent },
    { path: 'vehicles/:id', component: VehicleDetailComponent }
];
