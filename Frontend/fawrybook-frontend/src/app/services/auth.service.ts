import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';

import { jwtDecode } from 'jwt-decode';

import {
  LoginRequest,
  RegisterRequest,
  AuthResponse
} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl =
    `${environment.apiUrl}/auth`;

  private tokenKey = 'token';

  private isLoggedInSubject =
    new BehaviorSubject<boolean>(this.hasToken());

  isLoggedIn$ =
    this.isLoggedInSubject.asObservable();

  constructor(private http: HttpClient) {}

  register(
    data: RegisterRequest
  ): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/register`,
      data
    ).pipe(
      tap(response =>
        this.saveToken(response.token)
      )
    );
  }

  login(
    data: LoginRequest
  ): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      data
    ).pipe(
      tap(response =>
        this.saveToken(response.token)
      )
    );
  }

  logout():  Observable<AuthResponse>{


    return this.http.post<AuthResponse>(
      `${this.apiUrl}/logout`,
      {}
    ).pipe(
      tap(() => this.clearToken())
    );
  }

  getToken(): string | null {

    return localStorage.getItem(this.tokenKey);
  }

  clearToken(): void {

    localStorage.removeItem(this.tokenKey);
    this.isLoggedInSubject.next(false);  
  }

  private saveToken(token: string): void {

    localStorage.setItem(this.tokenKey, token);

    this.isLoggedInSubject.next(true);
  }

  private hasToken(): boolean {

    return !!localStorage.getItem(this.tokenKey);
  }

  getCurrentUserEmail(): string | null {

    const token = this.getToken();

    if (!token) return null;

    const decoded: any =
      jwtDecode(token);

    return decoded.sub;
  }

}