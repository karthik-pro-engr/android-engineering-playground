# Architecture

## Overview

The project follows Clean Architecture principles with clear separation between Presentation, Domain, and Data layers.

The application adopts an Offline-First approach where Room acts as the Single Source of Truth (SSOT).

---

```mermaid
flowchart TB

subgraph Presentation["Presentation Layer"]
    UI["Jetpack Compose UI"]
    VM["ViewModel"]
    UI --> VM
end

subgraph Domain["Domain Layer"]
    UC["Use Cases"]
end

subgraph Data["Data Layer"]

    REPO["Repository"]

    subgraph Local["Local Data Source"]
        ROOM["Room Database (SSOT)"]
        PAGING["PagingSource"]
        ROOM --> PAGING
    end

    subgraph Paging["Paging 3"]
        RM["RemoteMediator"]
    end

    subgraph Remote["Remote Data Source"]
        RETROFIT["Retrofit"]
    end

    subgraph Worker["Background Processing"]
        WM["WorkManager"]
    end
end

API["GitHub REST API"]

VM --> UC
UC --> REPO

REPO --> ROOM
REPO --> RM

PAGING --> RM

RM --> RETROFIT
RETROFIT --> API

WM --> RETROFIT
WM --> ROOM

classDef ssot fill:#d5f5d5
class ROOM ssot
```

## Layers

### Presentation Layer

- Jetpack Compose
- ViewModel

Responsibilities:

- UI rendering
- State management
- User interaction handling

---

### Domain Layer

- Use Cases

Responsibilities:

- Business logic
- Application rules

---

### Data Layer

- Repository
- Room
- Retrofit
- RemoteMediator

Responsibilities:

- Data synchronization
- Caching
- Pagination

---

## Offline First Data Flow

GitHub API
→ RemoteMediator
→ Room Database (SSOT)
→ PagingSource
→ Repository
→ UseCase
→ ViewModel
→ Compose UI

---

## Background Sync

WorkManager is responsible for:

- Periodic synchronization
- Retry handling
- Exponential backoff
- Network constraints

---

## Why Room as SSOT?

Benefits:

- Offline support
- Testability
- Predictable state
- Consistent UI data source