# Java Functional Programming FAQ (Lambdas, Streams, Optional)

> **Scope** — the Java 8 functional toolkit: lambdas, method references, the built-in functional interfaces, the Stream API, collectors, and `Optional`.
> **See also**: [`java_modern.md`](./java_modern.md) — language features from Java 9–21;
> [`java_collection.md`](./java_collection.md) — the collections streams run over.

Streams are the most-used Java 8 feature and the one interviewers probe hardest, because
misuse (side effects, `parallelStream()` on the wrong workload, streams where a `for` loop
was clearer) is easy to spot.

---

## 1) Lambdas & Functional Interfaces ⭐⭐⭐⭐⭐

A **functional interface** is an interface with exactly one abstract method (SAM). A
lambda is an implementation of one.

```java
// java
Runnable r = () -> System.out.println("hi");              // no args
Comparator<String> byLen = (a, b) -> a.length() - b.length();
Function<String, Integer> parse = s -> Integer.parseInt(s);

@FunctionalInterface                    // compiler enforces "exactly one abstract method"
interface Validator<T> { boolean test(T value); }
```

### The built-in interfaces to know

| Interface | Signature | Typical use |
|-----------|-----------|-------------|
| `Function<T,R>` | `R apply(T)` | `map` |
| `BiFunction<T,U,R>` | `R apply(T,U)` | `merge`, `reduce` |
| `Predicate<T>` | `boolean test(T)` | `filter`, `removeIf` |
| `Consumer<T>` | `void accept(T)` | `forEach` |
| `Supplier<T>` | `T get()` | lazy defaults, `orElseGet` |
| `UnaryOperator<T>` | `T apply(T)` | `replaceAll` |
| `BinaryOperator<T>` | `T apply(T,T)` | `reduce`, `Collectors.toMap` merge |

Primitive variants (`IntPredicate`, `ToIntFunction`, `IntUnaryOperator`, …) exist purely
to avoid boxing — use them in hot paths.

### Method references

Four shapes, all sugar for a lambda:

```java
// java
String::toUpperCase        // unbound instance method  -> s -> s.toUpperCase()
System.out::println        // bound instance method     -> x -> System.out.println(x)
Integer::parseInt          // static method             -> s -> Integer.parseInt(s)
ArrayList::new             // constructor               -> () -> new ArrayList<>()
```

### Lambdas vs anonymous classes

| | Lambda | Anonymous class |
|---|--------|-----------------|
| `this` | The **enclosing** instance | The anonymous instance |
| Compiled to | `invokedynamic` (no extra class file per instance) | A real `Outer$1.class` |
| Can declare fields | No — though a capturing lambda carries the values it captured, and can mutate the objects they point to | Yes |
| Target | Functional interfaces only | Any interface / abstract class |

Both capture only **effectively final** locals — a local a lambda uses must never be
reassigned, because the value is captured, not the variable.

---

## 2) Streams: the Pipeline ⭐⭐⭐⭐⭐

A stream is **not** a data structure. It is a one-shot pipeline over a source, made of
**lazy intermediate** operations and one **eager terminal** operation.

```java
// java
List<String> names = people.stream()          // source
        .filter(p -> p.getAge() >= 18)        // intermediate (lazy)
        .map(Person::getName)                 // intermediate (lazy)
        .sorted()                             // stateful intermediate
        .limit(10)                            // short-circuiting
        .collect(Collectors.toList());        // terminal — NOW it runs
```

**Nothing executes until the terminal operation**, and elements flow through the whole
pipeline one at a time (fusion), so `filter → map → findFirst` may touch only one element.

| Kind | Operations |
|------|------------|
| Intermediate (stateless) | `filter`, `map`, `flatMap`, `peek`, `mapToInt` |
| Intermediate (stateful) | `sorted`, `distinct`, `limit`, `skip` — may buffer the whole stream |
| Terminal | `collect`, `forEach`, `reduce`, `count`, `min/max`, `anyMatch`, `findFirst`, `toArray` |

- A stream can be consumed **once**: reusing it throws `IllegalStateException`.
- `map` transforms one element into one; **`flatMap` flattens one element into many**:

```java
// java
List<String> allTags = posts.stream()
        .flatMap(post -> post.getTags().stream())   // Stream<List<String>> -> Stream<String>
        .distinct()
        .toList();                                  // Java 16+, immutable
```

### Reduce

```java
// java
int total = nums.stream().reduce(0, Integer::sum);       // identity + accumulator
Optional<Integer> max = nums.stream().reduce(Integer::max);   // no identity -> Optional
```

The accumulator must be **associative and side-effect free**, or parallel results differ
from sequential ones. For primitives prefer `IntStream.sum()` / `average()` /
`summaryStatistics()` — no boxing, and `average()` returns `OptionalDouble` because an
empty stream has no mean.

---

## 3) Collectors ⭐⭐⭐⭐

```java
// java
import static java.util.stream.Collectors.*;

Map<Dept, List<Employee>> byDept   = staff.stream().collect(groupingBy(Employee::dept));
Map<Dept, Long>           headcount = staff.stream().collect(groupingBy(Employee::dept, counting()));
Map<Dept, Double>         avgPay    = staff.stream().collect(groupingBy(Employee::dept, averagingDouble(Employee::salary)));
Map<Boolean, List<Employee>> split  = staff.stream().collect(partitioningBy(e -> e.salary() > 100_000));
String                    csv       = staff.stream().map(Employee::name).collect(joining(", ", "[", "]"));
Map<String, Employee>     byId      = staff.stream().collect(toMap(Employee::id, e -> e));
```

Two traps that come up constantly:

- **`toMap` throws `IllegalStateException` on a duplicate key.** Pass a merge function:
  `toMap(Employee::id, e -> e, (a, b) -> a)`.
- **`toMap` throws `NullPointerException` on a null value** (unlike `HashMap.put`).
  Filter nulls first, or collect into a `HashMap` yourself.
- `Collectors.toList()` promises **nothing** about the list's type or mutability (today it
  is an `ArrayList`, but don't rely on it). Use `toCollection(ArrayList::new)` when you
  need a mutable list, and `stream.toList()` (16+) when you want an explicitly
  **unmodifiable** one.

`teeing` (12+) runs two collectors over one pass; `mapping` / `filtering` /
`flatMapping` compose as downstream collectors inside `groupingBy`.

---

## 4) Parallel Streams ⭐⭐⭐⭐

`list.parallelStream()` splits the source across the **common ForkJoinPool**
(`cores − 1` workers, shared by the whole JVM).

Use it only when **all** of these hold:

1. The work per element is genuinely CPU-heavy (thousands of ns), and there are many elements.
2. The source splits cheaply — `ArrayList`, arrays, `IntStream.range`. A `LinkedList`
   or `Iterator` source splits badly.
3. The lambdas are **stateless, side-effect free and associative**.
4. There is no blocking I/O in the pipeline — blocking starves the shared pool and can
   stall unrelated code across the application.

```java
// java
long primes = IntStream.rangeClosed(2, 5_000_000)
        .parallel()
        .filter(MathUtil::isPrime)      // pure, CPU-bound -> a good fit
        .count();
```

Anti-patterns: `parallelStream().forEach(list::add)` (unsynchronised mutation),
ordering-sensitive pipelines (`forEachOrdered` re-serialises and gives back the win),
and parallelising anything that hits a database.

---

## 5) Optional ⭐⭐⭐⭐

`Optional<T>` documents "this may legitimately be absent" in a **return type**. It is not
a general-purpose null wrapper.

```java
// java
Optional<User> found = repo.findById(id);

String name = found.map(User::name)
                   .filter(n -> !n.isBlank())
                   .orElse("anonymous");

found.ifPresentOrElse(this::render, this::render404);       // 9+
User user = found.orElseThrow(() -> new NotFoundException(id));
```

| Do | Don't |
|----|-------|
| Return `Optional` from a lookup that can miss | Use it for fields, parameters or collections (return an empty collection instead) |
| `orElseGet(this::expensive)` for a costly default | `orElse(expensive())` — the argument is evaluated **even when a value is present** |
| `orElseThrow(...)` | `get()` without `isPresent()` — the same NPE with extra steps |
| `map` / `flatMap` chains | `if (o.isPresent()) { o.get() … }`, which is just a null check in disguise |

`Optional` is not `Serializable`, and boxing it in a hot loop costs an allocation per
call — that is why entity fields stay plain.

---

## 6) Streams vs Loops — When Not to Stream

Reach for a loop when the body mutates external state, needs `break` with side effects,
uses checked exceptions (lambdas cannot throw them), or when indices matter. A stream
earns its place when the pipeline reads as **what** you want rather than **how** to get
it, and especially for `groupBy`-shaped aggregation.

```java
// java
// Fine as a loop; a stream here is noise.
for (Order o : orders) {
    if (!o.isValid()) { log.warn("skipping {}", o.id()); continue; }
    process(o);
}
```

A lambda may throw whatever its **target type** declares — `Callable.call()` allows any
checked exception. The problem is that the interfaces the Stream API uses (`Function`,
`Predicate`, `Consumer`, …) declare none, so inside a stream a checked exception must be
wrapped:
`.map(f -> { try { return parse(f); } catch (IOException e) { throw new UncheckedIOException(e); } })`.

---

## 7) Common Interview Q&A

**Q: Are streams faster than loops?**
Usually no for simple work — a stream adds pipeline overhead. They win in readability, and
in parallel form for CPU-heavy work on splittable sources. Measure, don't assume.

**Q: What does lazy evaluation buy you?**
Short-circuiting (`findFirst`, `anyMatch`, `limit`) and single-pass fusion: no
intermediate collections between stages.

**Q: Difference between `map` and `flatMap`?**
`map` is 1→1; `flatMap` is 1→many, then flattened into a single stream.

**Q: Can a lambda modify a local variable?**
No — captured locals must be effectively final. Use an `AtomicInteger`, an array, or a
proper reduction instead of a mutable counter.

**Q: What is `invokedynamic` doing here?**
The compiler emits a call site the JVM links at runtime to a generated lambda
implementation, instead of emitting an anonymous class per lambda — fewer class files and
better inlining.

**Q: `Stream.iterate` on an infinite stream?**
Legal as long as something short-circuits it:
`Stream.iterate(1, x -> x * 2).limit(10)`.

**Q: Is a stream reusable?**
No. Build it from the source again, or collect once and reuse the collection.

---

## 8) Recap Checklist

```text
[ ] Functional interface = one abstract method; name the six built-ins
[ ] Four method-reference shapes
[ ] Lambda vs anonymous class: `this`, capture, effectively final
[ ] Lazy intermediates vs eager terminal; a stream is single-use
[ ] map vs flatMap; reduce needs associativity
[ ] groupingBy with a downstream collector; toMap's duplicate-key trap
[ ] When parallelStream helps — and the shared ForkJoinPool it borrows
[ ] Optional as a return type; orElse vs orElseGet
```

---

## References

- [Java SE — the Stream package summary](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)
- [`java_modern.md`](./java_modern.md) — records, sealed types, virtual threads
- [`java_collection.md`](./java_collection.md) — the collections behind the streams
