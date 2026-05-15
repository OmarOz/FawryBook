import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import { CommonModule }
from '@angular/common';

import { PostFormComponent }
from '../../shared/components/post-form/post-form';

import { PostDetailsService }
from '../../services/post-details.service';

import { timeout } from 'rxjs/operators';

@Component({
  selector: 'app-edit-post',

  standalone: true,

  imports: [
    CommonModule,
    PostFormComponent
  ],

  templateUrl: './edit-post.html'
})
export class EditPostComponent
implements OnInit {

  post: any = null;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private postDetailsService: PostDetailsService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    console.log('Edit post ID:', id);

    if (id) {

      this.postDetailsService
        .getPostById(id)
        .pipe(
          timeout(10000)
        )
        .subscribe({

          next: post => {

            console.log('Post loaded:', post);
            this.post = post;
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (err) => {

            console.error('Error loading post:', err);
            this.error = true;
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
    } else {

      console.error('No ID in route');
      this.error = true;
      this.loading = false;
    }
  }

  onSubmit(data: any) {

    this.postDetailsService
      .updatePost(this.post.id, {
        ...data,
        id: this.post.id
      })
      .subscribe({

        next: () => {

          this.router.navigate([
            '/posts',
            this.post.id
          ]);
        }
      });
  }
}