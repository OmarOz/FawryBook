import { Injectable } from '@angular/core';

import { HttpClient }
from '@angular/common/http';

import { Observable }
from 'rxjs';

import { environment }
from '../../environments/environment';

import { Tag }
from '../models/tag.model';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private apiUrl =
    `${environment.apiUrl}/tags`;

  constructor(private http: HttpClient) {}

  getTags():
  Observable<Tag[]> {

    return this.http.get<Tag[]>(
      this.apiUrl
    );
  }

  createTags(names: string[]): Observable<Tag[]> {
  return this.http.post<Tag[]>(this.apiUrl, { names });
}
}