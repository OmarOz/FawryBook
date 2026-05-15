import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';

import { Post } from '../models/post.model';
import { CommentRequest } from '../models/comment.model';
import { InteractionRequest } from '../models/Interaction.model';

@Injectable({
  providedIn: 'root',
})
export class PostDetailsService {
  private apiUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) {}

  getPostById(id: string): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`);
  }

  createPost(data: any) {

    return this.http.post(
      `${this.apiUrl}/create`,
      data
    );
  }

    updatePost(id: string, data: any) {

    return this.http.put(
      `${this.apiUrl}/${id}`,
      data
    );
  }

  deletePost(id: string) {

    return this.http.delete(
      `${this.apiUrl}/${id}`
    );
  }

  addComment(data: CommentRequest) {
    return this.http.post(
      `${environment.apiUrl}/posts/comment`,
      data
    );
  }

  setInteraction(data: InteractionRequest) {
    return this.http.post(
      `${environment.apiUrl}/posts/interaction`,
      data
    );
  }

}
