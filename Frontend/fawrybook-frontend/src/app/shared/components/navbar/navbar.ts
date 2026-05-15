import { Component, OnDestroy, OnInit,ChangeDetectorRef } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [  CommonModule,
  RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit, OnDestroy{
  isLoggedIn = false;
  private subscription!: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // subscribe to login state changes
    // whenever user logs in or out, isLoggedIn updates automatically
    this.subscription = this.authService.isLoggedIn$.subscribe(
      status => this.isLoggedIn = status
    );
  }

  ngOnDestroy(): void {
    // always unsubscribe to prevent memory leaks
    this.subscription.unsubscribe();
  }

  onLogout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/']);
        this.cdr.detectChanges();
      },
      error: () => {
        this.authService.clearToken();
        this.router.navigate(['/']);
        this.cdr.detectChanges();
      }
    });
  }

}
