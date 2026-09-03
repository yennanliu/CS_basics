# Java Generics FAQ

> **Scope** — type parameters, type erasure and everything that follows from it: bounded types, wildcards and PECS, generic methods, and why generic arrays are illegal.
> **See also**: [`java_collection.md`](./java_collection.md) — the generic APIs you use
> daily; [`faq_OOP.md`](./faq_OOP.md) — variance and Liskov substitution.

Generics move type errors from runtime (`ClassCastException`) to compile time, and let one
implementation serve many types without casting.

---

## 1) The Basics ⭐⭐⭐⭐

```java
// java
public class Box<T> {                 // T = type parameter
    private T value;
    public void set(T value) { this.value = value; }
    public T get()           { return value; }     // no cast at the call site
}

Box<String> box = new Box<>();        // diamond: the compiler infers <String>
String s = box.get();                 // typed, no cast, no ClassCastException
```

Conventional names: `T` type, `E` element, `K`/`V` key/value, `R` result, `N` number.

### Generic methods

The parameter list goes **before** the return type, and can be independent of the class:

```java
// java
public static <T extends Comparable<T>> T max(List<T> list) {
    T best = list.get(0);
    for (T item : list) if (item.compareTo(best) > 0) best = item;
    return best;
}
```

### Bounded type parameters

```java
// java
<T extends Number>                       // upper bound: T is a Number (or subtype)
<T extends Comparable<? super T>>        // the idiom for "sortable by itself or a supertype"
<T extends Serializable & Comparable<T>> // multiple bounds: class first, then interfaces
```

`extends` here means "is a subtype of" for both classes and interfaces. There is **no
`super` bound on a type parameter** — only on wildcards.

---

## 2) Type Erasure ⭐⭐⭐⭐⭐

Generics are a **compile-time** feature. The compiler checks types, inserts casts, and
then **erases** the type arguments: `List<String>` and `List<Integer>` are both `List` at
runtime. Erasure was chosen for backward compatibility with pre-5 bytecode.

```java
// java
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
a.getClass() == b.getClass();          // true — same runtime class
```

Everything below is a consequence of erasure:

| You cannot | Because | Do instead |
|-----------|---------|-----------|
| `new T()` | The type argument is gone at runtime | Pass a `Supplier<T>` or `Class<T>` and call `newInstance` |
| `new T[10]` | Array creation needs a reifiable type | `(T[]) new Object[10]` with `@SuppressWarnings`, or use a `List<T>` |
| `x instanceof List<String>` | Only the raw type survives | `x instanceof List<?>` |
| `static T field;` | Static members are per-class, not per-instantiation | Make the method generic |
| Overload on `List<String>` and `List<Integer>` | Same erased signature | Rename the methods |
| `catch (MyException<T> e)` | Exceptions must be reifiable | Non-generic exception types |

**Bridge methods**: to keep polymorphism working after erasure the compiler synthesises
extra methods — e.g. a class implementing `Comparable<Person>` gets both
`compareTo(Person)` and a synthetic `compareTo(Object)` that casts and delegates. This is
why an unchecked cast can blow up in a method you never wrote.

**Heap pollution / unchecked warnings** happen when erasure lets a value of the wrong
type reach a generic variable — typically through a raw type or a varargs array.
`@SafeVarargs` says "I promise this generic varargs method only reads its array".

---

## 3) Wildcards & PECS ⭐⭐⭐⭐⭐

**Generics are invariant**: `List<String>` is *not* a `List<Object>`, even though `String`
is an `Object`. If it were, you could put a `Integer` into a list of strings through the
wider reference.

Wildcards restore the flexibility safely:

| Form | Means | You can |
|------|-------|---------|
| `List<?>` | Unknown element type | Read as `Object`; add only `null` |
| `List<? extends Number>` | Some subtype of `Number` (a **producer**) | **Read** `Number`; cannot add (which subtype?) |
| `List<? super Integer>` | Some supertype of `Integer` (a **consumer**) | **Add** `Integer`; reads come back as `Object` |

**PECS — Producer `extends`, Consumer `super`:**

```java
// java
// src PRODUCES elements to copy -> extends
// dst CONSUMES the copied elements -> super
public static <T> void copy(List<? extends T> src, List<? super T> dst) {
    for (T item : src) dst.add(item);
}

copy(List.of(1, 2, 3), new ArrayList<Number>());   // List<Integer> -> List<Number>: legal
```

The JDK is full of this: `Collections.max(Collection<? extends T>)`,
`Stream.map(Function<? super T, ? extends R>)`, `forEach(Consumer<? super T>)`. Rule of
thumb: **wildcards on parameters, never on return types** — a wildcard return forces
wildcards on every caller.

### Wildcard vs type parameter

Use a type parameter `<T>` when the same type appears more than once (so the relationship
matters); use `?` when the method does not care what the type is:

```java
// java
void printAll(Collection<?> items)             // doesn't care
<T> void swap(List<T> list, int i, int j)      // both positions must be the SAME T
```

---

## 4) Raw Types & Legacy Interop ⭐⭐⭐

A **raw type** is a generic class used with no type argument (`List list = ...`). It
exists only for pre-Java-5 compatibility: it disables all generic checking, produces
unchecked warnings, and defeats the purpose of the type parameter.

```java
// java
List raw = new ArrayList<String>();
raw.add(42);                       // compiles, unchecked warning
String s = ((List<String>) raw).get(0);   // ClassCastException at runtime
```

Use `List<?>` when you genuinely don't know the element type — it keeps type safety.

---

## 5) Generics and Arrays ⭐⭐⭐

Arrays are **covariant and reified**; generics are **invariant and erased**. The two
models don't mix.

```java
// java
Object[] objects = new String[1];   // legal — arrays are covariant
objects[0] = 42;                    // compiles, throws ArrayStoreException at runtime

List<Object> list = new ArrayList<String>();   // does NOT compile — caught at build time
```

Because arrays check element types at runtime and generics don't, `new T[n]` and
`new List<String>[10]` are illegal. Prefer collections; when you must return an array from
generic code, use the `T[] toArray(T[] a)` shape the JDK uses.

---

## 6) Common Interview Q&A

**Q: What problem do generics solve?**
Compile-time type safety and the removal of casts — a `ClassCastException` becomes a
compile error, and the type is documented in the signature.

**Q: What is type erasure and why does Java use it?**
Type arguments are checked then discarded, so generic code compiles to the same bytecode
as pre-generics code. It was the price of binary backward compatibility in Java 5.

**Q: `List<Object>` vs `List<?>` vs raw `List`?**
`List<Object>` accepts any element but only matches a list declared as `List<Object>`.
`List<?>` matches a list of *some* unknown type and is read-only (bar `null`). Raw `List`
turns checking off entirely — never write it in new code.

**Q: Explain PECS.**
See §3 — read from `? extends`, write to `? super`.

**Q: Can you overload `void f(List<String>)` and `void f(List<Integer>)`?**
No — both erase to `f(List)`, so it is a duplicate method.

**Q: How do you get a `Class<T>` at runtime?**
Pass it explicitly (`Class<T> type` — the "type token" pattern, as in
`EnumMap`/Jackson), or capture it from a subclass's generic superclass
(`TypeReference`, `ParameterizedType`), since that *is* retained in the class file.

**Q: Why can't a generic class extend `Throwable`?**
`catch` matching needs the exact runtime type, which erasure destroys.

---

## 7) Recap Checklist

```text
[ ] Generic class, generic method, bounded parameter syntax
[ ] Erasure: what disappears, and the five things it makes illegal
[ ] Bridge methods and unchecked warnings
[ ] Invariance: why List<String> is not List<Object>
[ ] PECS with the copy() example, and the JDK signatures that use it
[ ] Wildcard vs type parameter — when the same T must appear twice
[ ] Raw types are legacy-only
[ ] Arrays are covariant + reified; generics are invariant + erased
```

---

## References

- [Java Tutorials — Generics](https://docs.oracle.com/javase/tutorial/java/generics/)
- [`java_collection.md`](./java_collection.md) — the generic collection APIs
- [`faq_OOP.md`](./faq_OOP.md) — substitution and variance from the OOP side
