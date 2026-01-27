# Git Best Practices Guidelines for AI Assistants

## Commit Message Format
Use Conventional Commits: `type(scope): description`

### Required Types
- `feat:` - new feature or functionality
- `fix:` - bug fix or error correction
- `docs:` - documentation only changes
- `style:` - formatting, whitespace, semicolons (no logic change)
- `refactor:` - code restructuring without behavior change
- `test:` - adding or updating tests
- `chore:` - maintenance, dependencies, build configuration

### Commit Message Rules
1. Use imperative mood: "add" not "added" or "adds"
2. Keep description under 72 characters
3. Lowercase after the colon
4. No period at the end
5. Scope is optional but recommended: `feat(auth):` or `fix(api):`

### Examples
````
feat(auth): add JWT authentication
fix(database): resolve connection timeout
docs(readme): update installation steps
refactor(utils): simplify date formatting logic
test(api): add integration tests for user endpoints
chore(deps): update dependencies to latest versions
````

## Branch Protection Policy

**CRITICAL RULE: Direct commits to the main branch are STRICTLY PROHIBITED.**

### Policy
- All changes MUST be developed on a feature branch
- Feature branches MUST be integrated into main via GitHub Pull Request
- NO direct commits, pushes, or merges to main are allowed
- This applies to all contributors, including AI assistants

### Rationale
- Ensures code review process for all changes
- Maintains clean, auditable commit history
- Prevents accidental breaking changes
- Enables CI/CD validation before merge
- Facilitates team collaboration and knowledge sharing

### Enforcement
If you accidentally commit to main:
````bash
# Undo the commit but keep changes
git reset --soft HEAD~1

# Create proper feature branch
git checkout -b feat/your-feature-name

# Commit to feature branch
git commit -m "feat(scope): your changes"

# Push feature branch
git push origin feat/your-feature-name
````

## Branch Naming Format
Use same convention: `type/description-in-kebab-case`

### Examples
````
feat/user-authentication
fix/login-timeout-error
docs/api-documentation
refactor/database-queries
test/payment-integration
chore/update-dependencies
````

## Test-Driven Development (TDD) - Required Workflow

**TDD is mandatory for all code changes.** Tests must be written BEFORE implementation.

### TDD Cycle

```
┌─────────────────────────────────────────────────────┐
│  1. RED    →  Write failing test                    │
│  2. GREEN  →  Write minimal code to pass            │
│  3. REFACTOR → Improve code, keep tests passing     │
│  4. REPEAT →  Next test case                        │
└─────────────────────────────────────────────────────┘
```

### TDD Rules for AI Assistants

1. **NEVER write implementation code without a failing test first**
2. **Show the test failure** before implementing
3. **Show the test passing** after implementing
4. **Commit tests and implementation together** (same commit)

### Test-First Commit Messages

````
test(user): add tests for user creation endpoint
feat(user): implement user creation endpoint

# Combined approach (preferred):
feat(user): add user creation endpoint with tests
````

### TDD in the Development Process

```bash
# 1. Create feature branch
git checkout -b feat/feature-name

# 2. Write failing test
# ... create test file, run tests, see RED

# 3. Implement minimal code
# ... write code, run tests, see GREEN

# 4. Refactor if needed
# ... improve code, run tests, still GREEN

# 5. Commit test + implementation together
git add src/test/... src/main/...
git commit -m "feat(scope): add feature with tests"

# 6. Repeat for next test case
```

### Test Coverage Expectations

| Change Type | Test Requirement |
|-------------|------------------|
| New feature | Unit tests for all public methods |
| Bug fix | Regression test that reproduces the bug |
| Refactor | All existing tests must pass |
| API endpoint | Integration test with request/response |

### PR Checklist for TDD

Add to PR description:
````markdown
## TDD Compliance
- [ ] Tests written before implementation
- [ ] All new code has test coverage
- [ ] All tests pass locally
- [ ] No test skips or pending tests added
````

## Complete Workflow

### 1. Before Starting Work
````bash
# Ensure you're on main and it's up to date
git checkout main
git pull origin main

# Create feature branch
git checkout -b feat/feature-name
````

### 2. Development Process
````bash
# Make changes to files

# Stage specific files only
git add path/to/file

# Commit with conventional format
git commit -m "feat(scope): description"

# Continue making commits as needed
# Push branch regularly
git push origin feat/feature-name
````

### 3. Before Creating PR
````bash
# Ensure branch is up to date with main
git checkout main
git pull origin main
git checkout feat/feature-name
git rebase main

# Or use merge if rebasing isn't preferred
git merge main

# Push final changes
git push origin feat/feature-name --force-with-lease  # if rebased
````

## Pull Request Creation

### PR Title Format
Use conventional commits format: `type(scope): Description`

Example: `feat(auth): Add JWT authentication system`

### PR Description Template
````markdown
## Description
[Clear explanation of what this PR does and why]

## Type of Change
- [ ] feat: New feature
- [ ] fix: Bug fix
- [ ] docs: Documentation update
- [ ] style: Code style/formatting
- [ ] refactor: Code refactoring
- [ ] test: Adding tests
- [ ] chore: Maintenance

## Changes Made
- [Bullet point list of specific changes]
- [Include file names and what was modified]
- [Mention any new dependencies or configuration]

## Testing
- [ ] Code has been tested locally
- [ ] All existing tests pass
- [ ] New tests added (if applicable)

## Screenshots/Examples
[If UI changes, include before/after screenshots]
[If API changes, include example requests/responses]

## Breaking Changes
- [ ] Yes (explain below)
- [ ] No

[If yes, explain what breaks and migration steps]

## Checklist
- [ ] Code follows project conventions
- [ ] No credentials or secrets committed
- [ ] No debug code or console.logs remain
- [ ] Documentation updated (if needed)
- [ ] Ready for review

## Additional Notes
[Any other context, concerns, or discussion points]
````

### Complete PR Description Example
````markdown
## Description
Implements JWT-based authentication system to replace the current session-based auth. This improves security and enables stateless authentication for our API.

## Type of Change
- [x] feat: New feature

## Changes Made
- Added JWT token generation and validation in `src/auth/jwt.service.ts`
- Created authentication middleware in `src/middleware/auth.middleware.ts`
- Updated login endpoint to return JWT tokens in `src/controllers/auth.controller.ts`
- Added JWT configuration to environment variables
- Updated user model to include refresh token storage
- Added token refresh endpoint

## Testing
- [x] Code has been tested locally
- [x] All existing tests pass
- [x] New tests added for JWT service and auth middleware

Test coverage: 95% on new files

## Screenshots/Examples
**Login Request:**
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "expiresIn": 3600
}
```

## Breaking Changes
- [x] Yes

**BREAKING CHANGE:** The `/api/auth/login` endpoint now returns JWT tokens instead of setting session cookies. Clients must:
1. Store the `accessToken` from response
2. Include `Authorization: Bearer <token>` header in subsequent requests
3. Use `/api/auth/refresh` endpoint to get new tokens

Migration guide added to `docs/MIGRATION.md`

## Checklist
- [x] Code follows project conventions
- [x] No credentials or secrets committed
- [x] No debug code or console.logs remain
- [x] Documentation updated
- [x] Ready for review

## Additional Notes
- JWT_SECRET must be added to environment variables before deployment
- Token expiration is set to 1 hour (configurable via JWT_EXPIRATION)
- Refresh tokens are valid for 7 days
````

## Pre-Commit Checklist
- [ ] Review all staged changes
- [ ] Ensure no credentials, API keys, or secrets included
- [ ] Verify no debug code or console.logs remain
- [ ] Check `.gitignore` excludes build artifacts and dependencies
- [ ] Confirm commit contains only related changes
- [ ] All tests pass locally

## AI Execution Rules

### Must Do
1. **NEVER commit directly to main** - ALL changes must go through feature branches and GitHub PRs
2. **Always work on feature branches** - create a new branch for every change
3. **Pull before starting** - ensure branch is up to date
4. **Create comprehensive PRs** with all information needed for human review
5. **Include testing information** - what was tested and results
6. **Document breaking changes** clearly with migration steps
7. **List all modified files** and what changed
8. **One logical feature** per branch/PR

### Never Do
1. **Never commit**: passwords, API keys, tokens, private keys, credentials
2. **Never merge PRs** - humans must approve and merge
3. **Never force push** without `--force-with-lease`
4. **Never skip tests** if test suite exists
5. **Never mix unrelated changes** in same branch

### PR Size Guidelines
- **Small PR**: 1-3 files, <200 lines changed (ideal)
- **Medium PR**: 4-10 files, 200-500 lines changed (acceptable)
- **Large PR**: >10 files or >500 lines (break into smaller PRs if possible)

## Breaking Changes Format
For breaking changes:
````bash
git commit -m "feat(api)!: change authentication method

BREAKING CHANGE: JWT tokens now required for all endpoints.
Session-based auth has been removed. Clients must update to
include Authorization header with Bearer token."
````

## Common Scopes by Project Type
- **Web**: auth, ui, api, database, router, middleware, components
- **Backend**: api, database, auth, services, models, config
- **General**: config, utils, core, models, services, tests, docs

## GitHub PR Commands
````bash
# Using GitHub CLI (gh)
gh pr create --title "feat(auth): Add JWT authentication" --body-file pr-template.md

# Or push and create PR via web interface
git push origin feat/feature-name
# Then navigate to GitHub and click "Create Pull Request"
````