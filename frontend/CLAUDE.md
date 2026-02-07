# Frontend - Angular + Ionic Application

## Architecture

- **Framework**: Angular 20.3.0
- **UI Framework**: Ionic 8.4.1
- **Language**: TypeScript 5.9.2
- **Build Tool**: Angular CLI 20.3.15 with application builder
- **Testing**: Karma 6.4.0 with Jasmine 5.9.0
- **Package Manager**: npm 11.7.0
- **Status**: Ionic 8 integrated with standalone components
- **Note**: Capacitor not yet added (web-only for now)

## Project Structure

```
frontend/
├── Dockerfile            # Multi-stage Docker build
├── nginx.conf            # Production nginx configuration
├── .dockerignore         # Frontend Docker exclusions
├── angular.json          # Angular CLI configuration
├── package.json          # npm dependencies and scripts
├── tsconfig.json         # TypeScript configuration
├── tsconfig.app.json     # App-specific TypeScript config
├── tsconfig.spec.json    # Test-specific TypeScript config
├── public/               # Static assets
│   └── favicon.ico
└── src/
    ├── main.ts           # Application entry point
    ├── index.html        # HTML template
    ├── styles.css        # Global styles (Ionic CSS imports)
    └── app/
        ├── app.ts        # Root component (standalone)
        ├── app.html      # Root component template (ion-app)
        ├── app.css       # Root component styles
        ├── app.config.ts # Application configuration (Ionic providers)
        ├── app.routes.ts # Routing configuration
        ├── app.spec.ts   # Root component tests
        └── pages/        # Ionic pages
            └── home/     # Home page
                ├── home.page.ts   # Home page component
                ├── home.page.html # Home page template
                └── home.page.css  # Home page styles
```

## Key Dependencies

- Angular 20.3.0 (standalone components, signals, vite-based build)
- Ionic 8.4.1 (mobile-first UI framework)
- TypeScript 5.9.2
- Karma/Jasmine for testing

See `package.json` for complete dependency list.

## What's Implemented

- Angular 20.3.0 standalone application initialized
- Ionic 8.4.1 integrated with standalone components
- TypeScript 5.9.2 configuration
- Karma/Jasmine testing framework configured
- Home page with Ionic UI components
- Routing infrastructure with lazy loading
- Prettier configured for code formatting (100 char line width)
- Application builder with development/production configs
- Modern @angular/build system (vite-based)
- Ionic global styles and theming configured

## What's Not Yet Implemented

1. **Mobile Native**: Capacitor not yet added (web-only for now)
2. **State Management**: NgRx or advanced state management not configured
3. **PWA**: PWA capabilities not yet enabled
4. **Ionic Features**: Many Ionic components not yet utilized
5. **E2E Testing**: Playwright not yet configured

## Development Commands

```bash
# Install dependencies
npm install

# Start dev server with Ionic CLI (http://localhost:8100)
npm start
# or
ionic serve

# Build for production
npm run build
# or
ionic build

# Run tests
npm test

# Build and watch for changes
npm run watch

# Format code with Prettier
npm run format

# Run Ionic CLI commands directly
npx ionic --help
```

## Key Decisions & Context

1. **Angular Version**: Using Angular 20.3.0 (latest stable)
2. **Ionic Version**: Using Ionic 8.4.1 (latest stable) with Ionic CLI 7.2.0
3. **Architecture**: Standalone components (no NgModules) - default in Angular 20
4. **Ionic Integration**: Standalone Ionic components with provideIonicAngular
5. **Development CLI**: Using Ionic CLI for serve/build commands (wraps Angular CLI)
6. **Project Name**: Angular project named "app" (Ionic CLI default expectation)
7. **Testing**: Karma/Jasmine (Angular default)
8. **TypeScript**: Version 5.9.2
9. **Code Formatting**: Prettier configured with 100 char line width, single quotes
10. **Build System**: Modern @angular/build (vite-based) instead of webpack
11. **Mobile-First**: Ionic 8 provides mobile-first UI components
12. **Theming**: Use Ionic CSS variables, shadow parts, and color system for all styling
13. **CSS Import Order**: All @import rules (including Google Fonts) must come before CSS rules

## Notes for AI Assistants

- **Location**: All frontend code is in `frontend/`
- **Architecture**: Use standalone components (default in Angular 20), not NgModules
- **Ionic Components**: Import Ionic components from `@ionic/angular/standalone`
- **Testing**: Karma/Jasmine configured, use for unit tests
- **Test Configuration**: Angular tests using `IonRouterOutlet` require `provideRouter([])` in test configuration
- **Formatting**: Prettier configured - run on save or via npm scripts
- **Angular Features**: Using Angular 20 with modern features (signals, control flow, etc.)
- **Ionic Version**: Using Ionic 8 with web components and mobile-first design
- **Build Commands**: Use `npm start` or `ionic serve` for dev server, `npm run build` or `ionic build` for production
- **Code Style**: Follow Angular/TypeScript conventions
- **CI Environment**: Karma needs `--no-watch --no-progress --browsers=ChromeHeadless`
- **MCP Servers**:
  - `angular-cli` - Angular-specific documentation and best practices
  - `playwright` - Browser automation and E2E testing (not yet configured)
- **Playwright Usage**: **ALWAYS** use the Playwright MCP server to verify frontend changes after implementation. Start the dev server, navigate to the page, take screenshots, and interact with the UI to confirm the changes work as expected. This is critical for catching visual bugs and interaction issues early.
- **Port**: Development server runs on :8100

## Common Issues & Solutions

### npm Install Issues
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Test Failures
- Ensure all Ionic components have proper test configuration
- Use `provideRouter([])` for components using IonRouterOutlet
- Run tests in headless mode for CI: `npm test -- --no-watch --browsers=ChromeHeadless`

## Future Additions

When implementing new frontend features, consider:

1. Adding Capacitor for native mobile capabilities (iOS/Android)
2. Implementing Signal-based state management or NgRx
3. Adding PWA capabilities with @angular/pwa
4. Creating more Ionic UI components and pages
5. Setting up E2E tests with Playwright
6. Adding ESLint for code quality
7. Adding native device features (camera, geolocation, etc.) via Capacitor
