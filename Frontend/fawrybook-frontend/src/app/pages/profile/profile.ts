import { Component, OnInit, signal ,ChangeDetectorRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService, UserProfile } from '../../services/UserService';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent implements OnInit {

  // state
  profile = signal<UserProfile | null>(null);
  loading = signal(true);
  error = signal('');
  successMessage = signal('');
  isEditing = signal(false);
  saving = signal(false);

  profileForm: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading.set(true);
    this.userService.getProfile().subscribe({
      next: (profile) => {
        this.profile.set(profile);
        this.loading.set(false);
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        this.error.set('Failed to load profile.');
        this.loading.set(false);
      }
    });
  }

  startEditing(): void {
    this.isEditing.set(true);
    this.successMessage.set('');
    this.error.set('');
    this.profileForm.patchValue({ username: this.profile()?.username });
    this.cdr.detectChanges();
  }

  cancelEditing(): void {
    this.isEditing.set(false);
    this.error.set('');
    this.profileForm.patchValue({ username: this.profile()?.username });
    this.cdr.detectChanges();
  }

  saveProfile(): void {
    if (this.profileForm.invalid) return;

    this.saving.set(true);
    this.error.set('');

    const username = this.profileForm.value.username;

    this.userService.updateProfile({ username }).subscribe({
      next: (updatedProfile) => {
        this.profile.set(updatedProfile);
        this.isEditing.set(false);
        this.saving.set(false);
        this.successMessage.set('Profile updated successfully!');

        // clear success message after 3 seconds
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: () => {
        this.error.set('Failed to update profile. Please try again.');
        this.saving.set(false);
      }
    });
  }

  // helper to access form fields easily in HTML
  get usernameControl() {
    return this.profileForm.get('username');
  }
}