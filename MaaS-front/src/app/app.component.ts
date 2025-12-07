import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HelloWorldComponent } from './hello-world/hello-world.component';
import { UsersListComponent } from './users-list/users-list.component';
import { VehicleListComponent } from "./vehicles/vehicle-list.component";
import { VehicleDetailComponent } from './vehicles/vehicle-detail.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, FormsModule, ReactiveFormsModule, HelloWorldComponent, UsersListComponent, VehicleListComponent, VehicleDetailComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
}
