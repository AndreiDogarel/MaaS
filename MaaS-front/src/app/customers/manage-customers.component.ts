import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, UserDto } from '../services/user.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-manage-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-customers.component.html',
  styleUrls: ['./manage-customers.component.css']
})
export class ManageCustomersComponent implements OnInit {
  private userService = inject(UserService);
  public authService = inject(AuthService);

  customers: UserDto[] = [];
  isLoading = true;
  error: string | null = null;
  showEditForm = false;
  editingCustomerId: number | null = null;

  editForm = {
    id: null as number | null,
    username: '',
    role: ''
  };

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.isLoading = true;
    this.error = null;
    console.log('Loading customers from:', 'http://localhost:8080/api/admin/users');
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        console.log('Customers loaded successfully:', data);
        this.customers = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load customers:', err);
        console.error('Error details:', err.message, err.status, err.error);
        this.error = `Failed to load customers: ${err.message || 'Unknown error'}`;
        this.isLoading = false;
      }
    });
  }

  beginEdit(customer: UserDto): void {
    this.editingCustomerId = customer.id ?? null;
    this.editForm = {
      id: customer.id ?? null,
      username: customer.username || '',
      role: customer.role || ''
    };
    this.showEditForm = true;
  }

  submitEdit(): void {
    if (!this.editForm.id || !this.editForm.username || !this.editForm.role) {
      alert('Please fill in all required fields (username, role)');
      return;
    }

    const updatePayload: Partial<UserDto> = {
      username: this.editForm.username,
      role: this.editForm.role
    };

    this.userService.updateUser(this.editForm.id, updatePayload).subscribe({
      next: (updatedUser) => {
        const idx = this.customers.findIndex(c => c.id === this.editForm.id);
        if (idx !== -1) {
          this.customers[idx] = updatedUser;
        }
        this.cancelEdit();
      },
      error: (err) => {
        console.error('Failed to update customer', err);
        alert('Failed to update customer');
      }
    });
  }

  cancelEdit(): void {
    this.showEditForm = false;
    this.editingCustomerId = null;
    this.editForm = {
      id: null,
      username: '',
      role: ''
    };
  }

  deleteCustomer(customer: UserDto): void {
    const id = customer.id;
    if (!id) return alert('Cannot determine customer id');
    if (!confirm(`Are you sure you want to delete ${customer.username}?`)) return;

    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.customers = this.customers.filter(c => c.id !== id);
      },
      error: (err) => {
        console.error('Failed to delete customer', err);
        alert('Failed to delete customer');
      }
    });
  }
}
