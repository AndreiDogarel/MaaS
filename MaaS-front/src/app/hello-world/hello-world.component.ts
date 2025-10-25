import { Component } from '@angular/core';
import { HelloService } from '../services/hello.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hello-world',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hello-world.component.html'
})
export class HelloWorldComponent {
  message: string = '';

  constructor(private helloService: HelloService) {}

  getMessage() {
    this.helloService.getHello().subscribe({
      next: (data) => this.message = data,
      error: (err) => console.error(err)
    });
  }
}