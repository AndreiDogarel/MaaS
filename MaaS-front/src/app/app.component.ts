import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HelloWorldComponent } from './hello-world/hello-world.component';
import { UsersListComponent } from './users-list/users-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, FormsModule, ReactiveFormsModule, HelloWorldComponent, UsersListComponent
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {
}
