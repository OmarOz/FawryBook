import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';

import { Post } from '../models/post.model';

@Injectable({
  providedIn: 'root',
})
export class PostsService {
    posts : Post[] = [];

    private apiUrl = `${environment.apiUrl}/posts/all`;

    constructor(private http: HttpClient) {}

    getPosts(): Observable<Post[]> {
        return this.http.get<Post[]>(this.apiUrl).pipe(
            tap(posts => this.posts = posts)
        );
    }
}
