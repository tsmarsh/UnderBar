# UnderBar - Claude Code Guide

## Project Overview

UnderBar is a functional utility library for Java 11+ that promotes type-safe, lambda-based programming. Inspired by Underscore.js and Clojure, it provides static functions for functional programming patterns in Java.

## Build & Test Commands

```bash
# Build all modules
mvn clean install

# Run tests
mvn test

# Run tests for a specific module
mvn test -pl underbar.ocho
mvn test -pl stash
mvn test -pl underbar.dates
mvn test -pl underbar.io
mvn test -pl underbar.crypto

# Skip tests during build
mvn install -DskipTests
```

## Project Structure

```
UnderBar/
├── underbar.ocho/     # Core functional utilities (no dependencies)
├── stash/             # Dynamic Map<String, Object> with type-safe accessors
├── underbar.dates/    # Date/time utilities (ISO 8601)
├── underbar.io/       # I/O utilities (slurp, resource loading)
└── underbar.crypto/   # AES, BCrypt, RSA encryption utilities
```

## Module Dependencies

- `underbar.ocho` - Core foundation, no external dependencies
- `underbar.dates` - No external dependencies
- `stash` - Depends on: underbar.ocho, underbar.dates, gson
- `underbar.io` - Depends on: underbar.ocho, stash
- `underbar.crypto` - Depends on: underbar.ocho

## Key Design Patterns

### Static Methods in Interfaces
All utilities are static methods in interfaces, enabling clean static imports:
```java
import static com.tailoredshapes.underbar.ocho.UnderBar.*;
List<Integer> nums = list(1, 2, 3);
```

### Heap<T> for Lambda Mutability
Workaround for Java's "effectively final" restriction:
```java
Heap<Integer> counter = heap(0);
forEach(items, item -> counter.value++);
```

### Fail-Fast with Die
Use `Die` class for assertions and early failure:
```java
dieIf(condition, () -> "error message");
dieUnless(condition, () -> "error message");
dieIfNull(value, () -> "error message");
```

## Key Classes

| Class | Location | Purpose |
|-------|----------|---------|
| `UnderBar` | underbar.ocho | Core collection/functional operations |
| `UnderString` | underbar.ocho | String manipulation utilities |
| `Die` | underbar.ocho | Error handling and assertions |
| `Maths` | underbar.ocho | Mathematical operations |
| `Stash` | stash | Dynamic string-keyed map with type coercion |
| `Dates` | underbar.dates | ISO 8601 date formatting/parsing |
| `IO` | underbar.io | File and stream utilities |
| `AES` | underbar.crypto | AES-256 encryption |
| `BCrypt` | underbar.crypto | Password hashing |

## Code Style

- Use static imports for UnderBar utilities
- Prefer functional operations (`map`, `filter`, `reduce`) over loops
- Use `Supplier<String>` for lazy error messages
- Favor immutability; use `assoc()` for immutable updates, `update()` for mutable

## Testing

Tests use JUnit 4. Each module has corresponding test classes:
- `UnderBarTest`, `DieTest`, `MathsTest`, `UnderStringTest`, `UnderRegTest`
- `StashTest`
- `DatesTest`
- `IOTest`, `RequestsTest`
- `AESTest`, `BCryptTest`, `RSATest`

## Common Gotchas

1. `list(T... ts)` returns a fixed-size list (Arrays.asList); use `modifiableList()` if mutation needed
2. `lazy()` uses a global cache - call `clearLazyCache()` if memory is a concern
3. `Stash.grab()` throws on missing keys; use `maybe()` or `optional()` for safe access
4. Crypto uses AES-256/CBC/PKCS5Padding - ensure JCE unlimited strength policy if needed
