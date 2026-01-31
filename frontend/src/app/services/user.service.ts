import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import {
  User,
  CreateUserRequest,
  UpdateUserRequest,
  PageResponse
} from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api/users';

  // Private signals for internal state
  private usersSignal = signal<User[]>([]);
  private loadingSignal = signal<boolean>(false);
  private errorSignal = signal<string | null>(null);
  private currentPageSignal = signal<number>(0);
  private hasMoreSignal = signal<boolean>(true);
  private totalElementsSignal = signal<number>(0);

  // Public read-only signals
  users = this.usersSignal.asReadonly();
  loading = this.loadingSignal.asReadonly();
  error = this.errorSignal.asReadonly();
  hasMore = this.hasMoreSignal.asReadonly();
  totalElements = this.totalElementsSignal.asReadonly();

  // Computed signals
  activeUsers = computed(() =>
    this.usersSignal().filter(user => user.active)
  );

  getUsers(page = 0, size = 20, append = false) {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    this.http
      .get<PageResponse<User>>(this.baseUrl, { params })
      .pipe(
        tap(response => {
          if (append) {
            this.usersSignal.update(users => [...users, ...response.content]);
          } else {
            this.usersSignal.set(response.content);
          }
          this.currentPageSignal.set(response.pageable.pageNumber);
          this.hasMoreSignal.set(!response.last);
          this.totalElementsSignal.set(response.totalElements);
          this.loadingSignal.set(false);
        }),
        catchError(error => this.handleError(error))
      )
      .subscribe();
  }

  searchUsers(query: string, activeFilter?: 'all' | 'active' | 'inactive') {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    let params = new HttpParams()
      .set('page', '0')
      .set('size', '100'); // Larger size for search results

    if (query) {
      params = params.set('search', query);
    }

    if (activeFilter === 'active') {
      params = params.set('active', 'true');
    } else if (activeFilter === 'inactive') {
      params = params.set('active', 'false');
    }

    this.http
      .get<PageResponse<User>>(this.baseUrl, { params })
      .pipe(
        tap(response => {
          this.usersSignal.set(response.content);
          this.currentPageSignal.set(0);
          this.hasMoreSignal.set(!response.last);
          this.totalElementsSignal.set(response.totalElements);
          this.loadingSignal.set(false);
        }),
        catchError(error => this.handleError(error))
      )
      .subscribe();
  }

  getUserById(id: number) {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    return this.http.get<User>(`${this.baseUrl}/${id}`).pipe(
      tap(() => this.loadingSignal.set(false)),
      catchError(error => this.handleError(error))
    );
  }

  createUser(request: CreateUserRequest) {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    return this.http.post<User>(this.baseUrl, request).pipe(
      tap(() => this.loadingSignal.set(false)),
      catchError(error => this.handleError(error))
    );
  }

  updateUser(id: number, request: UpdateUserRequest) {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    return this.http.put<User>(`${this.baseUrl}/${id}`, request).pipe(
      tap(() => this.loadingSignal.set(false)),
      catchError(error => this.handleError(error))
    );
  }

  deleteUser(id: number) {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => this.loadingSignal.set(false)),
      catchError(error => this.handleError(error))
    );
  }

  loadNextPage() {
    const nextPage = this.currentPageSignal() + 1;
    this.getUsers(nextPage, 20, true);
  }

  reset() {
    this.usersSignal.set([]);
    this.currentPageSignal.set(0);
    this.hasMoreSignal.set(true);
    this.errorSignal.set(null);
    this.totalElementsSignal.set(0);
  }

  private handleError(error: HttpErrorResponse) {
    this.loadingSignal.set(false);

    let errorMessage = 'An unexpected error occurred. Please try again.';

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = 'Unable to connect to server. Please check your connection.';
    } else {
      // Backend returned an unsuccessful response code
      switch (error.status) {
        case 404:
          errorMessage = 'User not found.';
          break;
        case 400:
          errorMessage = error.error?.message || 'Invalid user data. Please check your input.';
          break;
        case 409:
          errorMessage = 'A user with this email already exists.';
          break;
        case 500:
          errorMessage = 'Server error. Please try again later.';
          break;
        case 0:
          errorMessage = 'Unable to connect to server. Please check your connection.';
          break;
      }
    }

    this.errorSignal.set(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
