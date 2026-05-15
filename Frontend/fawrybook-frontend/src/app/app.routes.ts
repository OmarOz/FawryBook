import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [

  {
    path: '',
    loadComponent: () =>
      import('./pages/posts/posts')
      .then(m => m.PostsComponent)
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./pages/auth/login/login')
      .then(m => m.LoginComponent)
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./pages/auth/register/register')
      .then(m => m.RegisterComponent)
  },


{
    path: 'posts/:id/edit',
    loadComponent: () =>
      import('./pages/edit-post/edit-post')
      .then(m => m.EditPostComponent),
    canActivate: [AuthGuard]   
  },

  {
    path: 'posts/create',
    loadComponent: () =>
      import('./pages/create-post/create-post')
      .then(m => m.CreatePostComponent),
      canActivate: [AuthGuard]  
  },

  {
    path: 'posts/:id',
    loadComponent: () =>
      import('./pages/post-details/post-details')
      .then(m => m.PostDetailComponent)
  },


  {
    path: 'profile',
    loadComponent: () =>
      import('./pages/profile/profile')
      .then(m => m.ProfileComponent),
    canActivate: [AuthGuard]   
  }
];