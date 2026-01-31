import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { ModalController, AlertController, ToastController } from '@ionic/angular/standalone';
import { UsersPage } from './users.page';
import { UserService } from '../../services/user.service';

describe('UsersPage', () => {
  let component: UsersPage;
  let fixture: ComponentFixture<UsersPage>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    const modalControllerMock = jasmine.createSpyObj('ModalController', ['create']);
    const alertControllerMock = jasmine.createSpyObj('AlertController', ['create']);
    const toastControllerMock = jasmine.createSpyObj('ToastController', ['create']);

    await TestBed.configureTestingModule({
      imports: [UsersPage],
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ModalController, useValue: modalControllerMock },
        { provide: AlertController, useValue: alertControllerMock },
        { provide: ToastController, useValue: toastControllerMock },
        UserService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UsersPage);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Flush only UsersPage HTTP requests (from constructor)
    const pending = httpMock.match(req => req.url.includes('/api/users'));
    pending.forEach(req => {
      req.flush({
        content: [],
        pageable: { pageNumber: 0, pageSize: 20 },
        totalElements: 0,
        totalPages: 0,
        last: true
      });
    });
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on initialization', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/api/users?page=0&size=20');
    expect(req.request.method).toBe('GET');
    req.flush({
      content: [],
      pageable: { pageNumber: 0, pageSize: 20 },
      totalElements: 0,
      totalPages: 0,
      last: true
    });
  });
});
