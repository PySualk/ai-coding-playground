
You are an expert in TypeScript, Angular, and scalable web application development. You write functional, maintainable, performant, and accessible code following Angular and TypeScript best practices.

## File Organization

```
src/app/
├── pages/           # Ionic pages (routable views)
├── components/      # Reusable UI components
├── services/        # Business logic and API calls
├── models/          # TypeScript interfaces and types
├── guards/          # Route guards
└── interceptors/    # HTTP interceptors
```

## TypeScript Best Practices

- Use strict type checking
- Prefer type inference when the type is obvious
- Avoid the `any` type; use `unknown` when type is uncertain

## Angular Best Practices

- Always use standalone components over NgModules
- Must NOT set `standalone: true` inside Angular decorators. It's the default in Angular v20+.
- Use signals for state management
- Implement lazy loading for feature routes
- Do NOT use the `@HostBinding` and `@HostListener` decorators. Put host bindings inside the `host` object of the `@Component` or `@Directive` decorator instead
- Use `NgOptimizedImage` for all static images.
  - `NgOptimizedImage` does not work for inline base64 images.

## Ionic 8 Best Practices

- Import Ionic components from `@ionic/angular/standalone`
- Use `IonRouterOutlet` instead of Angular's `router-outlet` for page transitions
- Leverage Ionic lifecycle hooks: `ionViewWillEnter`, `ionViewDidEnter`, `ionViewWillLeave`, `ionViewDidLeave`
- Use Ionic's built-in mobile patterns: modal, popover, action-sheet, toast, alert
- Prefer Ionic components over native HTML for mobile-optimized UX
- Use `ion-content` as the root element in page templates (provides scroll, pull-to-refresh, infinite-scroll)

### Ionic Component Example
```typescript
import { IonHeader, IonToolbar, IonTitle, IonContent, IonButton } from '@ionic/angular/standalone';

@Component({
  selector: 'app-example',
  imports: [IonHeader, IonToolbar, IonTitle, IonContent, IonButton],
  template: `
    <ion-header>
      <ion-toolbar>
        <ion-title>Example Page</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <ion-button (click)="handleClick()">Click Me</ion-button>
    </ion-content>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ExamplePage {
  handleClick() {
    // Handle click
  }
}
```

## Accessibility Requirements

- It MUST pass all AXE checks.
- It MUST follow all WCAG AA minimums, including focus management, color contrast, and ARIA attributes.

### Components

- Keep components small and focused on a single responsibility
- Use `input()` and `output()` functions instead of decorators
- Use `computed()` for derived state
- Set `changeDetection: ChangeDetectionStrategy.OnPush` in `@Component` decorator
- Prefer inline templates for small components
- Prefer Reactive forms instead of Template-driven ones
- Do NOT use `ngClass`, use `class` bindings instead
- Do NOT use `ngStyle`, use `style` bindings instead
- When using external templates/styles, use paths relative to the component TS file.

### Component Example with Signals
```typescript
import { Component, ChangeDetectionStrategy, signal, computed, input, output } from '@angular/core';
import { IonCard, IonCardHeader, IonCardTitle, IonCardContent } from '@ionic/angular/standalone';

@Component({
  selector: 'app-user-card',
  imports: [IonCard, IonCardHeader, IonCardTitle, IonCardContent],
  template: `
    <ion-card>
      <ion-card-header>
        <ion-card-title>{{ displayName() }}</ion-card-title>
      </ion-card-header>
      <ion-card-content>
        <p>{{ user().email }}</p>
        <button (click)="onDelete()">Delete</button>
      </ion-card-content>
    </ion-card>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserCardComponent {
  // Inputs using new signal-based API
  user = input.required<User>();

  // Outputs
  delete = output<number>();

  // Computed values (derived state)
  displayName = computed(() =>
    this.user().name || 'Unknown User'
  );

  onDelete() {
    this.delete.emit(this.user().id);
  }
}
```

## State Management

- Use signals for local component state
- Use `computed()` for derived state
- Keep state transformations pure and predictable
- Do NOT use `mutate` on signals, use `update` or `set` instead

## Templates

- Keep templates simple and avoid complex logic
- Use native control flow (`@if`, `@for`, `@switch`) instead of `*ngIf`, `*ngFor`, `*ngSwitch`
- Use the async pipe to handle observables
- Do not assume globals like (`new Date()`) are available.
- Do not write arrow functions in templates (they are not supported).

## Services

- Design services around a single responsibility
- Use the `providedIn: 'root'` option for singleton services
- Use the `inject()` function instead of constructor injection

### Service Example with Signals
```typescript
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private users = signal<User[]>([]);

  // Derived state
  activeUsers = computed(() =>
    this.users().filter(u => !u.deletedAt)
  );

  loadUsers() {
    this.http.get<User[]>('/api/users')
      .subscribe(users => this.users.set(users));
  }
}
```

## Routing Patterns

- Use lazy loading for all feature routes
- Define routes in `app.routes.ts` with `loadComponent`
- Use route guards for authentication/authorization

### Routing Example
```typescript
// app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.page').then(m => m.HomePage)
  },
  {
    path: 'users',
    loadComponent: () => import('./pages/users/users.page').then(m => m.UsersPage),
    // canActivate: [AuthGuard]  // Add when auth is implemented
  }
];
```

## Testing Patterns

### Component Testing with Ionic
Tests using `IonRouterOutlet` require `provideRouter([])` in test configuration.

```typescript
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { HomePage } from './home.page';

describe('HomePage', () => {
  let component: HomePage;
  let fixture: ComponentFixture<HomePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomePage],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(HomePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
```

### Running Tests
```bash
# Unit tests (Karma/Jasmine)
npm test

# CI environment (headless Chrome)
npm test -- --no-watch --no-progress --browsers=ChromeHeadless

# With coverage
npm test -- --code-coverage
```
