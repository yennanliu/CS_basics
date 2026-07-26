# Java TDD FAQ

- https://tech.youzan.com/dddclue/

## Overview

**Test-Driven Development (TDD)** is writing a failing test *before* the code that
makes it pass. Tests drive the design, not just verify it.

## 1) The Red-Green-Refactor Cycle

```
   ┌─────────────────────────────────────┐
   │                                     │
   ▼                                     │
 RED  →  write a small failing test      │
   │                                     │
   ▼                                     │
GREEN →  write the minimum code to pass  │
   │                                     │
   ▼                                     │
REFACTOR → clean up code + tests, ───────┘
           keep everything green
```

- **Red**: the test fails (proves it actually tests something).
- **Green**: do the simplest thing to pass — even hardcoding, then generalize.
- **Refactor**: improve structure with the safety net of passing tests.
- Keep cycles small (minutes). Commit on green.

**Benefits**: high coverage by construction, faster feedback, better design
(testable = decoupled), living documentation, confidence to refactor.

---

## 2) JUnit 5 Basics

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calc;

    @BeforeEach          // runs before each test
    void setUp() { calc = new Calculator(); }

    @Test
    void addsTwoNumbers() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void throwsOnDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6})
    void detectsEven(int n) {
        assertTrue(calc.isEven(n));
    }
}
```

Common annotations & assertions:

| Annotation | Meaning |
|------------|---------|
| `@Test` | Marks a test method |
| `@BeforeEach` / `@AfterEach` | Run before/after each test |
| `@BeforeAll` / `@AfterAll` | Run once (static) |
| `@DisplayName` | Human-readable test name |
| `@ParameterizedTest` | Run with multiple inputs |
| `@Disabled` | Skip a test |

Key assertions: `assertEquals`, `assertTrue`/`assertFalse`, `assertNull`,
`assertThrows`, `assertAll` (group), `assertTimeout`.

---

## 3) Test Structure: AAA (Arrange-Act-Assert)

```java
@Test
void appliesDiscount() {
    // Arrange — set up inputs & state
    Cart cart = new Cart();
    cart.add(new Item("book", 100));

    // Act — invoke the behavior under test
    double total = cart.totalWith(new PercentDiscount(10));

    // Assert — verify the outcome
    assertEquals(90.0, total);
}
```

One logical assertion/behavior per test. Name tests after behavior
(`throwsWhenCartEmpty`), not implementation.

---

## 4) Mocking

Use test doubles to isolate the unit from slow/external collaborators
(DB, network, time). With Mockito:

```java
@Test
void sendsEmailOnSignup() {
    EmailService email = mock(EmailService.class);       // create double
    when(email.isUp()).thenReturn(true);                 // stub a return

    SignupService service = new SignupService(email);    // inject the mock
    service.register("a@b.com");

    verify(email).send(eq("a@b.com"), anyString());      // verify interaction
}
```

Terminology:
- **Stub**: returns canned answers.
- **Mock**: stub + records/verifies interactions.
- **Fake**: lightweight working implementation (e.g. in-memory repo).

Mock **collaborators you own the boundary to**; avoid mocking value objects or
the class under test.

---

## 5) What to Test

- **Do**: business logic, edge cases, boundaries (0, 1, max, empty, null),
  error paths, and each branch/decision.
- **Don't** obsess over: trivial getters/setters, framework code, or mocking so
  heavily the test just re-states the implementation.
- **FIRST** principles: Fast, Independent, Repeatable, Self-validating, Timely.
- Aim for tests that fail for exactly one reason and read as a spec of behavior.
