import { Component,ChangeDetectorRef } from '@angular/core';

import { Router } from '@angular/router';

import { PostFormComponent }
from '../../shared/components/post-form/post-form';

import { PostDetailsService }
from '../../services/post-details.service';

@Component({
  selector: 'app-create-post',

  standalone: true,

  imports: [PostFormComponent],

  templateUrl: './create-post.html'
})
export class CreatePostComponent {

  constructor(
    private postDetailsService: PostDetailsService,
    private router: Router
  ) {}

  onSubmit(data: any) {

    this.postDetailsService
      .createPost(data)
      .subscribe({

        next: () => {

          this.router.navigate(['/']);
        }
      });
  }
}