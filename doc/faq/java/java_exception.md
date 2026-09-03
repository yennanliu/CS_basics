# Java Exceptions FAQ

> **Scope** — the `Throwable` hierarchy, checked vs unchecked, `try`/`catch`/`finally` mechanics, try-with-resources, and the design rules for exceptions in application code.
> **See also**: [`java_basic.md`](./java_basic.md) — language fundamentals;
> [`java_tdd.md`](./java_tdd.md) — asserting on exceptions in tests.

---

## 1) The Hierarchy ⭐⭐⭐⭐⭐

```text
Throwable
├── Error                       ← JVM-level, do NOT catch
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── NoClassDefFoundError
└── Exception
    ├── RuntimeException        ← UNCHECKED (programming errors)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException / IllegalStateException
    │   ├── IndexOutOfBoundsException
    │   ├── ClassCastException
    │   └── ArithmeticException
    └── everything else         ← CHECKED (recoverable, must be declared)
        ├── IOException
        ├── SQLException
        └── InterruptedException
```

| | Checked | Unchecked (`RuntimeException`) | `Error` |
|---|---------|-------------------------------|---------|
| Compiler enforces handling | Yes — catch or declare | No | No |
| Signals | An expected external failure the caller can react to | A bug in the code | The JVM is in trouble |
| Example | File missing, network down | Null dereference, bad argument | Out of memory |
| You should | Handle or wrap it | Fix the code | Let it kill the process |

`Error` vs `Exception` is the single most-asked exception question: **an `Error` is not
meant to be caught**, because there is usually nothing sane to do — an
`OutOfMemoryError` handler typically needs to allocate.

---

## 2) `throw` vs `throws` ⭐⭐⭐

```java
// java
public String getUserName(int userId) throws SQLException {   // declares: callers beware
    String name = userDao.getUserName(userId);
    if (name == null) {
        throw new SQLException("no user " + userId);          // raises it here
    }
    return name;
}
```

- `throws` is part of the method **declaration** — an API contract. (It is not part of the *signature*: you cannot overload on it.)
- `throw` is a **statement** that raises an instance.
- An overriding method may throw **fewer or narrower** checked exceptions than the method
  it overrides, never more — otherwise callers of the base type could be surprised.

---

## 3) `try` / `catch` / `finally` Mechanics ⭐⭐⭐⭐

```java
// java
try {
    risky();
} catch (FileNotFoundException e) {     // most specific FIRST
    ...
} catch (IOException | SQLException e) { // multi-catch: `e` is effectively final
    ...
} finally {
    cleanup();                          // runs on normal exit, exception, AND return
}
```

Rules that get tested:

- **`finally` always runs** — including after a `return` in the `try` block, which is
  evaluated first, then `finally`, then the value is returned.
- **A `return` (or `throw`) inside `finally` discards the pending exception or return
  value.** Never do it; static analysers flag it.
- The Java-level ways to skip `finally` are `System.exit()`, `Runtime.getRuntime().halt()`, or never leaving the `try` (an infinite loop, a deadlock). Outside the JVM's control: a crash, `kill -9`, or the machine losing power.
- Catching a supertype before a subtype is a **compile error** (unreachable catch).
- Catching `Exception` also catches every `RuntimeException` — including the NPEs you
  wanted to see fail loudly.

```java
// java
int broken() {
    try { return 1; }
    finally { return 2; }    // returns 2, swallows the 1 — and any exception
}
```

---

## 4) try-with-resources ⭐⭐⭐⭐⭐

Any `AutoCloseable` declared in the header is closed automatically, in **reverse order**,
whether the block exits normally or not.

```java
// java
try (var in  = Files.newInputStream(src);
     var out = Files.newOutputStream(dst)) {      // out closes first
    in.transferTo(out);
}                                                 // no finally, no null checks
```

Why it beats `try/finally`: with a manual `finally`, an exception thrown by `close()`
**replaces** the original exception and you lose the real cause. try-with-resources keeps
the original and attaches the close failure as a **suppressed** exception
(`e.getSuppressed()`).

---

## 5) Designing with Exceptions ⭐⭐⭐⭐

### Wrap, don't leak

Let each layer throw its own abstraction, chaining the cause so the stack trace survives:

```java
// java
try {
    return jdbc.query(sql);
} catch (SQLException e) {
    throw new RepositoryException("loading user " + id, e);   // `e` is the cause
}
```

Pass the cause **object**, not its text: `throw new X("context", e)` keeps the original
stack trace, while `throw new X(e.getMessage())` throws it away — the most common way a
root cause disappears from a log.

### Rules of thumb

- **Fail fast on programming errors**: `Objects.requireNonNull(x, "x")`,
  `IllegalArgumentException` for bad input, `IllegalStateException` for a bad call order.
- **Don't use exceptions for control flow.** A lookup that routinely misses should return
  `Optional`, not throw.
- **Never swallow**: an empty `catch` block, or `catch (Exception e) { e.printStackTrace(); }`,
  hides real failures. Log **with** the exception object (`log.error("...", e)`) — passing
  `e.getMessage()` alone throws away the stack trace.
- **Preserve the interrupt flag.** `catch (InterruptedException e)` must either rethrow or
  call `Thread.currentThread().interrupt()`; otherwise the cancellation signal is lost.
- **Prefer unchecked for unrecoverable failures.** Modern APIs (Spring, JPA) wrap checked
  exceptions in runtime ones so callers are not forced into empty `catch` blocks.
- **Messages carry context**: identifiers and values, not "error occurred".
- Custom exceptions: one base per module (`OrderException`), subclasses for cases callers
  branch on. Don't invent a class per message.

### Cost

Creating an exception captures the stack trace, which is the expensive part — thousands
per second in a hot path show up in a profile. Override `fillInStackTrace()` (or use a
shared, stackless instance) only for genuinely hot, control-flow-like signals.

---

## 6) Common Interview Q&A

**Q: Checked vs unchecked — which do you prefer, and why?**
Unchecked for programming errors and for failures the caller cannot meaningfully handle
(the majority); checked where a caller genuinely has an alternative path. Checked
exceptions leak through every layer and encourage empty catches, which is why most modern
frameworks avoid them.

**Q: `final`, `finally`, `finalize`?**
`final` is a keyword (constant / non-overridable / non-extendable); `finally` is the
always-run block; `finalize()` is the deprecated `Object` hook the GC once called —
never rely on it, use try-with-resources or `Cleaner`.

**Q: Can `finally` be skipped?**
Yes: `System.exit()` / `Runtime.halt()`, a thread that never leaves the `try` block, or
anything that kills the process outright (a JVM crash, `kill -9`).

**Q: What is a suppressed exception?**
An exception thrown while closing a try-with-resources resource, attached to the primary
exception instead of replacing it.

**Q: Why is `catch (Exception e)` at the top level still fine?**
At a **boundary** (a request handler, a thread's `run`, `main`) catching broadly to log
and return a 500 is correct — the point is that nothing above you will do it. Inside
business logic it is a smell.

**Q: What happens to an exception thrown in a thread?**
It terminates that thread only, and goes to the thread's `UncaughtExceptionHandler`
(default: print to `stderr`). In an `ExecutorService` it depends how you handed the task
over: `submit()` captures the exception in the returned `Future`, so it is **silent until
you call `Future.get()`**; `execute()` has no `Future`, so it reaches the worker thread's
`UncaughtExceptionHandler` like any other thread.

**Q: `NoClassDefFoundError` vs `ClassNotFoundException`?**
`ClassNotFoundException` is checked and comes from an explicit
`Class.forName`/`loadClass`. `NoClassDefFoundError` means the class was present at compile
time but is missing (or failed to initialise) at runtime — usually a packaging problem.

---

## 7) Recap Checklist

```text
[ ] Throwable tree: Error vs Exception vs RuntimeException
[ ] Checked vs unchecked: who must handle, and what each signals
[ ] throw vs throws; narrowing rule when overriding
[ ] finally always runs; return-in-finally swallows
[ ] try-with-resources: reverse close order, suppressed exceptions
[ ] Wrap with the cause; never log without the exception object
[ ] Restore the interrupt flag on InterruptedException
[ ] Exceptions are not control flow; stack-trace capture costs
```

---

## References

- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [`java_basic.md`](./java_basic.md) — language fundamentals
- [`java_tdd.md`](./java_tdd.md) — testing that the right exception is thrown
