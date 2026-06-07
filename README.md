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
| API Integration | GitHub REST API for repos, details, languages, and releases |
| Pagination | Paging 3 |
| Dependency Injection | Hilt |
| CI/CD | GitHub Actions + Firebase App Distribution |
| Current Build | Debug build verified |
| Current Test Status | Focused unit coverage added for paging, repo detail, mappers, and formatters |

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
* API-backed repository detail screen
* Repository stats, topics, language breakdown, and release list
* Readable count, percentage, and relative time formatting
* Section-level retry handling for detail, languages, and releases
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
| Repository detail UI | Implemented |
| Repository detail API | Implemented |
| Languages API | Implemented |
| Releases API | Implemented |
| Navigation and repository detail deep links | Implemented |
| Shared UI formatting | Implemented |
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
StateFlow + PagingData + sectioned UI state
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

Current app runtime is wired to the real `GithubRepositoryImpl`. Debug and test fakes remain available for previews, deterministic test data, and isolated ViewModel coverage.

---

# Module Structure

## app

Application entry point, Compose UI, navigation, ViewModels, Hilt bindings, formatting helpers, and build-variant behavior.

## domain

Business models, repository contracts, repo list/detail use cases, release and language use cases, pagination constants, relative time models, and domain error types.

## data

Retrofit service, repository implementation, DTO mapping, repo detail/language/release endpoints, network error parsing, and PagingSource implementation.

## core

Shared Android and dependency injection utilities.

## core-testing

Shared test factories, fake repos, fake language/release data, and coroutine test helpers.

---

# API Flow

Repository list flow:

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

Repository detail flow:

```text
User selects a repository or opens a deep link
        |
        v
AppNavHost
        |
        v
RepoDetailRoute
        |
        v
RepoDetailViewModel
        |
        v
GetRepoDetailUseCase + GetLanguageUseCase + GetReleasesUseCase
        |
        v
GithubRepository
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
* Parallel loading for repository detail dependencies after the base repo succeeds
* Structured error handling for API and IO failures
* UI state modeled separately for repo detail, languages, and releases
* Preview and test data factories for repeatable UI and ViewModel scenarios
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
| Real repository binding in app runtime | Keeps production behavior aligned with the GitHub API while retaining fakes for tests and previews |
| Sectioned repository detail state | Allows repo metadata, languages, and releases to load, fail, and retry independently |
| Relative time domain model | Keeps formatting testable and localization-friendly |
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
* Repository detail ViewModel loading, success, error, and retry states
* Formatter and mapper behavior
* Readable number formatting and relative time boundaries

Run locally with:

```bash
./gradlew test
```

---

# Screenshots

> Screenshots will be added soon.

Recommended screenshots:

* Repository search screen
* Repository list with pagination
* Loading state
* Error state
* Repository detail screen
* Repository language breakdown
* Repository releases list
* Firebase beta feedback state

---

# Roadmap

* Add Room database cache
* Add WorkManager based background sync
* Add CI coverage reports
* Add screenshots and demo APK link
* Add richer release asset interactions from the detail screen

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
