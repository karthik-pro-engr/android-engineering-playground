# GitHub API Playground

A production-oriented Android API integration playground built with modern Android development practices.

This project demonstrates scalable Android engineering concepts including multi-module architecture, Clean Architecture, Jetpack Compose UI, dependency injection, pagination, resilient networking, Firebase release workflows, and testable state management.

The application integrates with the GitHub API and is designed as a real-world Android engineering portfolio project.

---

# Project Snapshot

| Area | Status |
|---|---|
| Platform | Android |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Modules | app, domain, data, core, core-testing |
| API Integration | GitHub REST API |
| Pagination | Paging 3 |
| Dependency Injection | Hilt |
| CI/CD | GitHub Actions + Firebase App Distribution |
| Current Build | Debug build verified |
| Current Test Status | Unit test suite needs minor test-signature fix |

---

# Tech Stack

## Language

* Kotlin

## UI

* Jetpack Compose
* Material 3
* Navigation Compose

## Architecture

* MVVM
* Clean Architecture
* Repository Pattern
* Multi-module Architecture

## Asynchronous Programming

* Kotlin Coroutines
* Kotlin Flow
* StateFlow

## Dependency Injection

* Hilt

## Networking

* Retrofit
* OkHttp
* Gson Converter
* GitHub REST API

## Pagination

* Paging 3
* Custom GitHub Link Header parsing

## Firebase

* Firebase Crashlytics
* Firebase Analytics
* Firebase App Distribution

## Testing

* JUnit
* MockK
* Turbine
* Truth
* MockWebServer
* Compose UI Testing

## Tooling

* Gradle Kotlin DSL
* Version Catalog
* GitHub Actions
* Husky
* Commitlint

---

# Features

* GitHub repository listing by username
* Paginated repository loading
* Modern Compose UI
* Loading, empty, and error states
* Retry-ready paging UI structure
* Repository detail screen structure
* Deep link support for repository detail route
* Multi-module project structure
* Firebase beta distribution workflow
* Release signing configuration
* CI workflow for build, test, and lint
* Shared testing utilities

---

# Current Implementation Status

| Capability | Status |
|---|---|
| Repository list screen | Implemented |
| Username search | Implemented |
| Paging support | Implemented |
| GitHub repo list API path | Implemented and tested |
| Repository detail UI | Implemented with demo data |
| Repository detail API | Planned |
| Languages API | Planned |
| Releases API | Planned |
| Room local cache | Planned |
| WorkManager sync | Future improvement |

---

# Architecture

The project follows Clean Architecture with clear separation of concerns.

```text
UI Layer
Jetpack Compose Screens
        |
        v
ViewModel Layer
StateFlow + PagingData
        |
        v
Domain Layer
UseCases + Repository Contracts
        |
        v
Data Layer
Repository Implementation
        |
        v
Remote Layer
Retrofit + OkHttp + GitHub API
```

Current app runtime uses a fake repository for deterministic UI development, while the real GitHub paging implementation exists in the data layer and is covered by tests.

---

# Module Structure

## app

Application entry point, Compose UI, navigation, ViewModels, Hilt bindings, and build-variant behavior.

## domain

Business models, repository contracts, use cases, pagination constants, and domain error types.

## data

Retrofit service, repository implementation, DTO mapping, network error parsing, and PagingSource implementation.

## core

Shared Android and dependency injection utilities.

## core-testing

Shared test factories, fake data, and coroutine test helpers.

---

# API Flow

```text
User enters GitHub username
        |
        v
RepoListScreen
        |
        v
GithubReposListViewModel
        |
        v
GetUserReposUseCase
        |
        v
GithubRepository
        |
        v
GithubPagingSource
        |
        v
GithubService
        |
        v
GitHub REST API
```

---

# Engineering Practices

* Modular Android architecture
* Clean separation between UI, domain, and data layers
* Dependency injection with Hilt
* Reactive UI state using Flow and StateFlow
* Paging 3 based list loading
* GitHub pagination using Link header parsing
* Structured error handling for API and IO failures
* Firebase App Distribution pipeline
* Release signing automation
* Conventional commit validation
* Unit and UI test coverage for important flows

---

# Key Architecture Decisions

| Decision | Reason |
|---|---|
| Multi-module setup | Keeps UI, domain, data, and testing responsibilities separate |
| Repository contract in domain | Makes the app easier to test and swap implementations |
| Paging 3 | Handles large repo lists, load states, retry, and pagination lifecycle |
| Fake repository during UI iteration | Keeps UI development deterministic while real API work evolves |
| Firebase beta workflow | Supports production-style release and feedback loops |

---

# Build And Run

```bash
git clone https://github.com/karthik-pro-engr/github-api-playground.git
cd github-api-playground
npm install
./gradlew :app:assembleDebug
```

Useful commands:

```bash
./gradlew test
./gradlew lint
./gradlew :app:assembleBeta
./gradlew :app:appDistributionUploadBeta
```

---

# Configuration

| Variable | Purpose |
|---|---|
| KEYSTORE_BASE64 | Base64 encoded release keystore |
| RELEASE_STORE_PASSWORD | Release keystore password |
| RELEASE_KEY_ALIAS | Release signing key alias |
| RELEASE_KEY_PASSWORD | Release signing key password |
| FIREBASE_SERVICE_ACCOUNT_JSON | Firebase service account JSON |
| GOOGLE_SERVICES_JSON | Firebase Android config for CI |

---

# Testing Status

The project includes tests for:

* PagingSource success and failure scenarios
* GitHub pagination behavior
* API error mapping
* Repository paging emission
* ViewModel query handling
* Compose paging UI states
* Formatter and mapper behavior

Current note:

* Debug build is verified.
* Full unit test command currently needs a small fix in `RepoDetailViewModelTest` because the test constructor call is behind the ViewModel signature.

---

# Screenshots

> Screenshots will be added soon.

Recommended screenshots:

* Repository search screen
* Repository list with pagination
* Loading state
* Error state
* Repository detail screen
* Firebase beta feedback state

---

# Roadmap

* Wire real GitHub repository implementation into beta/release runtime
* Implement repository detail API
* Implement languages API
* Implement releases API
* Add Room database cache
* Add WorkManager based background sync
* Fix current test drift and make full test suite green
* Add CI coverage reports
* Add screenshots and demo APK link

---

# Author

**Karthikkumar T**  
Android Engineer

Focused on modern Android development, scalable architecture, API integration, and production-grade mobile engineering.

* GitHub: [https://github.com/karthik-pro-engr](https://github.com/karthik-pro-engr)
* LinkedIn: [https://www.linkedin.com/in/karthikkumar-thangavel-a2a5b5229/](https://www.linkedin.com/in/karthikkumar-thangavel-a2a5b5229/)
* Portfolio: [https://github.com/karthik-pro-engr/github-api-playground#readme](https://github.com/karthik-pro-engr/github-api-playground#readme)

---

# License

Licensed under the [Apache-2.0 License](./LICENSE).