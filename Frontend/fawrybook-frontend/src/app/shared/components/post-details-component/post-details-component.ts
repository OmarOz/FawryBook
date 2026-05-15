import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostDetailsService } from '../../../services/post-details.service';
import { Post } from '../../../models/post.model';
import {DatePipe, CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup
} from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router,RouterLink } from '@angular/router';


@Component({
  selector: 'app-post-details-component',
  imports: [DatePipe, CommonModule,ReactiveFormsModule,RouterLink],
  templateUrl: './post-details-component.html',
  styleUrl: './post-details-component.css',
})
export class PostDetailsComponent {
  post: Post | null = null;
  loading = true;
  error = false;
  isAuthor = false;
  commentForm!: FormGroup;
 constructor(
    private route: ActivatedRoute,          
    private postDetailsService: PostDetailsService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.commentForm = this.fb.group({
      comment: ['', [Validators.required]]
    });
  }

   addComment() {

  if (
    this.commentForm.invalid ||
    !this.post
  ) return;

  const request = {
    id: '',
    postId: this.post.id,

    content:
      this.commentForm.value.comment!,
      authorUsername: 'current_user', 
      createdAt: new Date().toISOString()
  };

  this.postDetailsService
    .addComment(request)
    .subscribe({

      next: (newComment: any) => {

        this.commentForm.reset();
        this.loadPostDetails(this.post!.id);
      },

      error: err => {

        console.log(err);
      }
    });
}


setReaction(type: string) {

  if (!this.post) return;

  const request = {

    postId: this.post.id,

    interactionType: type
  };

  this.postDetailsService
    .setInteraction(request)
    .subscribe({

      next: () => {
        console.log('Interaction set successfully', request);



        this.loadPostDetails(this.post!.id);
      },

      error: err => {

        console.log(err);
      }
    });
}

  private loadPostDetails(id: string): void {
    this.loading = true;
    this.postDetailsService.getPostById(id).subscribe({
      next: (post) => {
        console.log('Fetched post details:', post);
        this.post = post;
        this.loading = false;
        this.isAuthor = this.post?.author.email === this.authService.getCurrentUserEmail();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = true;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deletePost() {

  if (!this.post) return;

  this.postDetailsService
    .deletePost(this.post.id)
    .subscribe({

      next: () => {

        this.router.navigate(['/']);
      }
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.loadPostDetails(id);
    }
  }

}
