import { Component,ChangeDetectorRef } from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  FormGroup
} from '@angular/forms';

import { Router,RouterLink } from '@angular/router';

import { AuthService }
from '../../../services/auth.service';

@Component({
  selector: 'app-login',

  standalone: true,

  imports: [ReactiveFormsModule, RouterLink],

  templateUrl: './login.html',

  styleUrl: './login.css'
})
export class LoginComponent {

  loginForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {

    this.loginForm = this.fb.group({

      email: [
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

    if (this.loginForm.invalid) return;

    this.authService.login(
      this.loginForm.value as any
    ).subscribe({

      next: () => {
        this.router.navigate(['/']);
        this.cdr.detectChanges();
      },

      error: err => {
        if (err.status === 401) {
          this.errorMessage = 'Wrong email or password. Please try again.';
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
        this.cdr.detectChanges();
        console.log(err);
      }
    });
  }
}