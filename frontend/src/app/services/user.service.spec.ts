import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { UserService } from './user.service';
import { User, CreateUserRequest, UpdateUserRequest, PageResponse } from '../models/user.model';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/api/users';

  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    firstName: 'Test',
    lastName: 'User',
    active: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z'
  };

  const mockPageResponse: PageResponse<User> = {
    content: [mockUser],
    pageable: {
      pageNumber: 0,
      pageSize: 20
    },
    totalElements: 1,
    totalPages: 1,
    last: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideHttpClient(),
        provideHttpClientTesting(),
        UserService
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getUsers', () => {
    it('should fetch users and update signals', () => {
      service.getUsers(0, 20);

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);

      expect(service.users()).toEqual([mockUser]);
      expect(service.loading()).toBe(false);
      expect(service.hasMore()).toBe(false);
      expect(service.totalElements()).toBe(1);
    });

    it('should append users when append is true', () => {
      const existingUser: User = { ...mockUser, id: 2 };
      const newPageResponse: PageResponse<User> = {
        ...mockPageResponse,
        content: [existingUser],
        pageable: { pageNumber: 1, pageSize: 20 }
      };

      // First load
      service.getUsers(0, 20);
      const req1 = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      req1.flush(mockPageResponse);

      // Append second page
      service.getUsers(1, 20, true);
      const req2 = httpMock.expectOne(`${baseUrl}?page=1&size=20`);
      req2.flush(newPageResponse);

      expect(service.users().length).toBe(2);
      expect(service.users()).toContain(mockUser);
      expect(service.users()).toContain(existingUser);
    });

    it('should set error signal on HTTP error', () => {
      service.getUsers(0, 20);

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      req.flush('Error', { status: 500, statusText: 'Server Error' });

      expect(service.error()).toBe('Server error. Please try again later.');
      expect(service.loading()).toBe(false);
    });
  });

  describe('searchUsers', () => {
    it('should search users with query', () => {
      service.searchUsers('test');

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=100&search=test`);
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);

      expect(service.users()).toEqual([mockUser]);
    });

    it('should filter by active status', () => {
      service.searchUsers('', 'active');

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=100&active=true`);
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should filter by inactive status', () => {
      service.searchUsers('', 'inactive');

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=100&active=false`);
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should combine query and filter', () => {
      service.searchUsers('john', 'active');

      const req = httpMock.expectOne(`${baseUrl}?page=0&size=100&search=john&active=true`);
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });
  });

  describe('getUserById', () => {
    it('should fetch a single user', (done) => {
      service.getUserById(1).subscribe(user => {
        expect(user).toEqual(mockUser);
        done();
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);
    });

    it('should handle 404 error', (done) => {
      service.getUserById(999).subscribe({
        error: () => {
          expect(service.error()).toBe('User not found.');
          done();
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/999`);
      req.flush('Not Found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createUser', () => {
    it('should create a new user', (done) => {
      const createRequest: CreateUserRequest = {
        email: 'new@example.com',
        firstName: 'New',
        lastName: 'User'
      };

      service.createUser(createRequest).subscribe(user => {
        expect(user).toEqual(mockUser);
        done();
      });

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(createRequest);
      req.flush(mockUser);
    });

    it('should handle duplicate email error', (done) => {
      const createRequest: CreateUserRequest = {
        email: 'existing@example.com',
        firstName: 'Test',
        lastName: 'User'
      };

      service.createUser(createRequest).subscribe({
        error: () => {
          expect(service.error()).toBe('A user with this email already exists.');
          done();
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Conflict', { status: 409, statusText: 'Conflict' });
    });
  });

  describe('updateUser', () => {
    it('should update an existing user', (done) => {
      const updateRequest: UpdateUserRequest = {
        firstName: 'Updated',
        lastName: 'Name'
      };

      const updatedUser = { ...mockUser, ...updateRequest };

      service.updateUser(1, updateRequest).subscribe(user => {
        expect(user).toEqual(updatedUser);
        done();
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updateRequest);
      req.flush(updatedUser);
    });

    it('should handle validation error', (done) => {
      const updateRequest: UpdateUserRequest = {
        email: 'invalid-email'
      };

      service.updateUser(1, updateRequest).subscribe({
        error: () => {
          expect(service.error()).toBe('Email format is invalid');
          done();
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      req.flush({ message: 'Email format is invalid' }, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('deleteUser', () => {
    it('should delete a user', (done) => {
      service.deleteUser(1).subscribe(() => {
        expect(service.loading()).toBe(false);
        done();
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('loadNextPage', () => {
    it('should load the next page and append results', () => {
      // First page
      service.getUsers(0, 20);
      const req1 = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      req1.flush({
        ...mockPageResponse,
        last: false
      });

      // Load next page
      service.loadNextPage();
      const req2 = httpMock.expectOne(`${baseUrl}?page=1&size=20`);
      req2.flush({
        ...mockPageResponse,
        content: [{ ...mockUser, id: 2 }],
        pageable: { pageNumber: 1, pageSize: 20 },
        last: true
      });

      expect(service.users().length).toBe(2);
    });
  });

  describe('reset', () => {
    it('should reset all signals to initial state', () => {
      service.getUsers(0, 20);
      const req = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      req.flush(mockPageResponse);

      service.reset();

      expect(service.users()).toEqual([]);
      expect(service.hasMore()).toBe(true);
      expect(service.error()).toBeNull();
      expect(service.totalElements()).toBe(0);
    });
  });

  describe('activeUsers computed signal', () => {
    it('should return only active users', () => {
      const users: User[] = [
        { ...mockUser, id: 1, active: true },
        { ...mockUser, id: 2, active: false },
        { ...mockUser, id: 3, active: true }
      ];

      service.getUsers(0, 20);
      const req = httpMock.expectOne(`${baseUrl}?page=0&size=20`);
      req.flush({
        ...mockPageResponse,
        content: users,
        totalElements: 3
      });

      expect(service.activeUsers().length).toBe(2);
      expect(service.activeUsers().every(u => u.active)).toBe(true);
    });
  });

  describe('error handling', () => {
    it('should handle network errors', (done) => {
      service.getUserById(1).subscribe({
        error: () => {
          expect(service.error()).toBe('Unable to connect to server. Please check your connection.');
          done();
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      req.error(new ProgressEvent('error'));
    });

    it('should handle 500 server error', (done) => {
      service.getUserById(1).subscribe({
        error: () => {
          expect(service.error()).toBe('Server error. Please try again later.');
          done();
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/1`);
      req.flush('Server Error', { status: 500, statusText: 'Internal Server Error' });
    });
  });
});
