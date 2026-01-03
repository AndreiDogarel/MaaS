import { Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { VehicleFormComponent } from './vehicles/vehicle-form.component';
import { VehicleListComponent } from './vehicles/vehicle-list.component';
import { VehicleDetailComponent } from './vehicles/vehicle-detail.component';
import { UploadDocumentComponent } from './documents/upload-document.component';
import { ManageCustomersComponent } from './customers/manage-customers.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth/auth.guard';
import {MonthlyReportsComponent} from "./reports/monthly-reports.component";

export const routes: Routes = [
    {
        path: 'auth',
        component: AuthLayoutComponent,
        children: [
            { path: 'login', component: LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: '', redirectTo: 'login', pathMatch: 'full' }
        ]
    },
    {
        path: '',
        component: MainLayoutComponent,
        children: [
            { path: '', component: HomeComponent },
            { path: 'vehicles', component: VehicleListComponent },
            { path: 'vehicles/new', component: VehicleFormComponent, canActivate: [authGuard] },
            { path: 'vehicles/:id', component: VehicleDetailComponent },
            { path: 'users', component: UsersListComponent, canActivate: [authGuard] },
            { path: 'documents/upload', component: UploadDocumentComponent, canActivate: [authGuard] },
            { path: 'customers', component: ManageCustomersComponent, canActivate: [authGuard] },
            { path: 'reports/monthly', component: MonthlyReportsComponent, canActivate: [authGuard] }
        ]
    },
    { path: '**', redirectTo: '' }
];
