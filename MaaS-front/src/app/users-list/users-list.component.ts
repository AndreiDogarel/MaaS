import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService, UserDto } from '../services/user.service';

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users-list.component.html'
})
export class UsersListComponent implements OnInit {
  users: UserDto[] = [];
  isLoading = true;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching users', err);
        this.isLoading = false;
      }
    });
  }
}