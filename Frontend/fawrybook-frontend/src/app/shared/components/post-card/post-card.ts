import { Component, signal } from '@angular/core';

import { Router, RouterLink  } from '@angular/router';
import {DatePipe} from '@angular/common';

import {PostsService} from '../../../services/posts.sevice';
import { Post } from '../../../models/post.model';
import { catchError } from 'rxjs/internal/operators/catchError';

@Component({
  selector: 'app-post-card',
  imports: [DatePipe, RouterLink],
  templateUrl: './post-card.html',
  styleUrl: './post-card.css',
})
export class PostCard {
  constructor(private postsService: PostsService) {}

  posts = signal(Array<Post>());

  ngOnInit() {
    this.postsService.getPosts()
    .pipe(
      catchError(err => {
        console.error('Error fetching posts:', err);
        throw err;
      })
    )
    .subscribe((posts) => {
      this.posts.set(posts);
    });
  }
}
