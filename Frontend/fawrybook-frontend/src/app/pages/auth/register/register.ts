import { Component,ChangeDetectorRef } from '@angular/core';
import {FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService }
from '../../../services/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],

  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {
  registerationForm!:FormGroup;
  errorMessage: string | null = null;

   constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {

    this.registerationForm = this.fb.group({

      email: [
        '',
        [Validators.required]
      ],
      username: [
        '',
        [Validators.required]
      ],
      password: [
        '',
        [Validators.required]
      ]
    });
    
  }
  onSubmit() {

    if (this.registerationForm.invalid) return;

    this.authService.register(
      this.registerationForm.value as any
    ).subscribe({

      next: () => {
        this.errorMessage = null;
        this.router.navigate(['/']);
        this.cdr.detectChanges();
      },

      error: err => {
        if (err.status === 409) {
            this.errorMessage = 'This email or username is already taken.';
          } else if (err.status === 400) {
            this.errorMessage = 'Please check your information and try again.';
          } else {
            this.errorMessage = 'Something went wrong. Please try again later.';
          }
        this.cdr.detectChanges();
        console.log(err);
      }
    });
  }
}
