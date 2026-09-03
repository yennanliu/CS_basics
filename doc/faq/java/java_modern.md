# Modern Java FAQ (Java 9 – 21)

> **Scope** — what changed after Java 8: `var`, records, sealed types, pattern matching, text blocks, the module system, and virtual threads.
> **See also**: [`java_functional.md`](./java_functional.md) — the Java 8 lambda/stream
> toolkit; [`java_multi_thread.md`](./java_multi_thread.md) — platform threads and pools.

Most teams that say "we're on Java 8" are really on 11 or 17 by now, and interviewers ask
what you would use if you were not. This is the short list that actually changes how code
is written.

---

## 1) Release Map ⭐⭐⭐

| Version | Status | Headline features |
|---------|--------|-------------------|
| **8** (2014) | LTS, still everywhere | Lambdas, streams, `Optional`, new date/time API |
| **9** | — | Module system (JPMS), `List.of`, `Stream.takeWhile` |
| **10** | — | `var` for locals |
| **11** (2018) | **LTS** | `String.strip/repeat/lines`, `HttpClient`, single-file source launch |
| **14–16** | — | `switch` expressions, records, text blocks, `instanceof` patterns |
| **17** (2021) | **LTS** | Sealed classes, the above finalised |
| **21** (2023) | **LTS** | **Virtual threads**, pattern matching for `switch`, record patterns, sequenced collections |

Java 9 also switched to a six-month release train, so a version number says much less
about capability than it used to; what matters is which LTS you target.

---

## 2) `var` — Local Variable Type Inference ⭐⭐⭐

```java
// java
var users = new HashMap<String, List<Order>>();   // no repeated type on the left
for (var entry : users.entrySet()) { ... }
```

- Locals, `for` variables and `try`-with-resources only — never fields, parameters or
  return types.
- The type is still **static and fixed**; this is inference, not dynamic typing.
- Use it when the right-hand side already names the type. Avoid it when it hides the
  type that a reader needs (`var result = service.process(x);`).

---

## 3) Records ⭐⭐⭐⭐⭐

A record is a transparent carrier for immutable data — the class you used to generate with
an IDE.

```java
// java
public record Money(long cents, String currency) {

    public Money {                                   // compact constructor: validate
        Objects.requireNonNull(currency);
        if (cents < 0) throw new IllegalArgumentException("negative");
    }

    public Money plus(Money other) {                 // ordinary methods are fine
        return new Money(cents + other.cents, currency);
    }
}
```

The compiler generates: `private final` fields, a canonical constructor, accessors named
`cents()` / `currency()` (no `get` prefix), plus `equals`, `hashCode` and `toString`.

| Property | Consequence |
|----------|-------------|
| Implicitly `final`, cannot extend a class | Not a replacement for a class hierarchy |
| Fields are `final` | Value semantics, safe to share across threads |
| `equals`/`hashCode` from all components | Usable as a map key straight away |
| Components are shallowly immutable | A `record Team(List<Player> players)` can still be mutated through the list — copy in the compact constructor |

Use for DTOs, API request/response bodies, value objects, and multi-value returns.
Don't use where identity or mutability matters (JPA entities, for example).

---

## 4) Sealed Types & Pattern Matching ⭐⭐⭐⭐

`sealed` fixes the set of permitted subtypes — a **closed hierarchy** the compiler can
reason about.

```java
// java
public sealed interface Shape permits Circle, Square, Rectangle {}

public record Circle(double r) implements Shape {}
public record Square(double side) implements Shape {}
public record Rectangle(double w, double h) implements Shape {}
```

Combined with `switch` pattern matching (21), the compiler proves the switch is
**exhaustive**, so adding a new subtype turns "silent wrong behaviour at runtime" into a
compile error:

```java
// java
double area = switch (shape) {                 // no default needed — sealed + exhaustive
    case Circle c        -> Math.PI * c.r() * c.r();
    case Square s        -> s.side() * s.side();
    case Rectangle(double w, double h) -> w * h;    // record pattern: destructures
};
```

Smaller pieces of the same feature:

```java
// java
if (obj instanceof String s && !s.isBlank()) {  // pattern: binds `s`, no cast
    System.out.println(s.length());
}

String label = switch (day) {                   // switch EXPRESSION: yields a value,
    case SAT, SUN -> "weekend";                 // arrow form, no fall-through, no break
    default       -> "weekday";
};
```

This is the *algebraic data type* style: sealed interface + records for the variants +
`switch` over them. It is how you model "one of N cases" without a visitor.

---

## 5) Text Blocks & String Additions ⭐⭐

```java
// java
String query = """
        SELECT id, name
        FROM   users
        WHERE  age > ?
        """;                       // incidental indentation stripped

"  hi  ".strip();                  // Unicode-aware trim (11)
"ab".repeat(3);                    // "ababab" (11)
text.lines().filter(...)           // Stream<String> (11)
String.join(", ", parts);
"%s is %d".formatted(name, age);   // (15)
```

---

## 6) Collections & API Additions ⭐⭐⭐

```java
// java
List<String> fixed = List.of("a", "b");            // immutable, rejects null (9)
Map<String, Integer> m = Map.of("a", 1, "b", 2);
List<String> copy = List.copyOf(other);

var first = list.getFirst();                       // sequenced collections (21)
var reversed = list.reversed();

Files.readString(path);                            // (11)
HttpClient.newHttpClient().send(request, ofString());   // built-in HTTP/2 client (11)
```

`List.of` returns a **truly immutable** list (add/remove/set all throw), unlike
`Arrays.asList` (fixed size but `set`-able) and `Collections.unmodifiableList` (a view of
a list someone else can still change).

---

## 7) The Module System (JPMS) ⭐⭐

```java
// java — module-info.java
module com.acme.orders {
    requires com.acme.common;
    exports  com.acme.orders.api;      // only this package is visible outside
}
```

Strong encapsulation at the package level plus explicit dependencies; `jlink` can then
build a runtime image with only the modules you use. In practice most applications stay
on the classpath and only feel JPMS through "illegal reflective access" warnings and
`--add-opens` flags. Know what it is and why the JDK itself was modularised.

---

## 8) Virtual Threads (Project Loom, 21) ⭐⭐⭐⭐⭐

A **virtual thread** is a `Thread` scheduled by the JVM onto a small pool of OS carrier
threads. When it blocks on I/O it is *unmounted*, freeing the carrier — so blocking code
scales like async code without being rewritten.

```java
// java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var url : urls) {
        executor.submit(() -> fetch(url));     // a million of these is fine
    }
}   // close() waits for all tasks
```

| | Platform thread | Virtual thread |
|---|-----------------|----------------|
| Backed by | One OS thread | Heap-allocated stack on a carrier thread |
| Cost | ~1 MB stack, expensive to create | ~KB, cheap — create per task |
| Good count | Hundreds | Millions |
| Blocking I/O | Wastes the OS thread | Unmounts, carrier does other work |
| Pooling | Necessary | **Anti-pattern** — create one per task |

What changes in practice:

- **Thread pools stop being the way to limit concurrency.** Use a `Semaphore` to bound
  access to a database or downstream service.
- **`synchronized` used to pin** a virtual thread to its carrier while blocking inside it
  (much improved in 24); `ReentrantLock` never did — prefer it around blocking calls.
- `ThreadLocal` still works but loses its "expensive, so cache per thread" rationale;
  scoped values are the intended replacement.
- CPU-bound work gains nothing — Loom is about **concurrency** (many waiting tasks), not
  parallelism.

**Structured concurrency** (preview) makes a task's children a scope that fails and
cancels as a unit — the same idea as Python's `TaskGroup`:

```java
// java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var user  = scope.fork(() -> fetchUser(id));
    var order = scope.fork(() -> fetchOrders(id));
    scope.join().throwIfFailed();
    return new Page(user.get(), order.get());
}
```

---

## 9) Common Interview Q&A

**Q: Why would you upgrade from 8 to 17/21?**
Records and sealed types remove boilerplate and make illegal states unrepresentable;
`switch` expressions and pattern matching remove a class of fall-through bugs; G1/ZGC and
JIT improvements cut pause times; 21 adds virtual threads. Plus 8 is out of free public
support for most vendors.

**Q: Record vs Lombok `@Value`?**
Records are a language feature — no annotation processor, no IDE plugin, and the compiler
knows about them (patterns, exhaustiveness). Lombok still wins if you need mutability or
builders on a class you cannot make a record.

**Q: Sealed vs `final` vs package-private constructors?**
`final` blocks all extension; a package-private constructor limits extension to one
package but tells the compiler nothing. `sealed` names the exact permitted subtypes, which
is what enables exhaustiveness checking.

**Q: Do virtual threads replace reactive frameworks?**
For most request-per-task services, yes — they give the same scalability with ordinary
blocking, debuggable, stack-traceable code. Reactive still wins where you need streaming
backpressure semantics.

**Q: Is `var` bad for readability?**
Only when the type is not obvious from the right-hand side. `var list = new ArrayList<String>()`
is clear; `var x = compute()` is not.

---

## 10) Recap Checklist

```text
[ ] LTS versions: 8, 11, 17, 21 — and what each added
[ ] var: locals only, still statically typed
[ ] Records: what is generated, and where they do NOT fit
[ ] Sealed + records + switch = exhaustive modelling of "one of N"
[ ] instanceof pattern, switch expression vs statement
[ ] List.of vs Arrays.asList vs unmodifiableList
[ ] Virtual threads: unmounting, no pooling, Semaphore for limits, pinning
[ ] Structured concurrency as scoped fork/join
```

---

## References

- [JDK 21 release notes](https://openjdk.org/projects/jdk/21/)
- [JEP 444 — Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 395 — Records](https://openjdk.org/jeps/395)
- [`java_functional.md`](./java_functional.md) — lambdas, streams, `Optional`
