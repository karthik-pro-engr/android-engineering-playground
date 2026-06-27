# Engineering Challenges & Solutions

## Challenge 1 — Offline Support

### Problem

Users lost access to data when internet connectivity was unavailable.

### Solution

Implemented Room as Single Source of Truth.

All UI screens read from Room instead of directly consuming network responses.

### Outcome

- Offline support
- Better UX
- Reduced network dependency

---

## Challenge 2 — Direct Network Pagination

### Problem

PagingSource directly consumed API responses.

This prevented local caching and offline functionality.

### Solution

Migrated to Paging 3 + RemoteMediator.

RemoteMediator synchronizes data between GitHub API and Room.

### Outcome

- Offline pagination
- Better architecture
- Improved testability

---

## Challenge 3 — Background Synchronization

### Problem

Cached data could become stale.

### Solution

Integrated WorkManager with:

- Network constraints
- Retry policies
- Exponential backoff
- Unique work scheduling

### Outcome

- Reliable synchronization
- Fresh data
- Better user experience

---

## Challenge 4 — Error Handling

### Problem

API and network failures produced inconsistent UI states.

### Solution

Implemented centralized error parsing and domain-level error mapping.

### Outcome

- Consistent error handling
- Better maintainability
- Cleaner UI states

---

## Challenge 5 — Scalability

### Problem

Future features would increase coupling.

### Solution

Applied:

- Clean Architecture
- Repository abstraction
- Hilt Dependency Injection
- Use Cases

### Outcome

- Easier maintenance
- Improved scalability
- Better testability