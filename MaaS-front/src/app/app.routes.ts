import { Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { VehicleFormComponent } from './vehicles/vehicle-form.component';
import { VehicleListComponent } from './vehicles/vehicle-list.component';
import { VehicleDetailComponent } from './vehicles/vehicle-detail.component';
import { UploadDocumentComponent } from './documents/upload-document.component';

export const routes: Routes = [
    { path: '', redirectTo: 'vehicles', pathMatch: 'full' },
    { path: 'users', component: UsersListComponent },
    { path: 'vehicles', component: VehicleListComponent },
    { path: 'vehicles/new', component: VehicleFormComponent },
    { path: 'vehicles/:id', component: VehicleDetailComponent },
    { path: 'documents/upload', component: UploadDocumentComponent }
];
