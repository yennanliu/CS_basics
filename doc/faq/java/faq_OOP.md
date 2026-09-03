# OOP FAQ

> **Scope** — OOP fundamentals for Java interviews: the four pillars, interface vs abstract class, composition over inheritance, SOLID and the everyday principles, and the object contracts.
> **See also**: [`../../cheatsheet/ood_design.md`](../../cheatsheet/ood_design.md) — class-modeling / low-level design prompts (Parking Lot, Elevator, Vending Machine); [`java_design_pattern.md`](./java_design_pattern.md) — the patterns these principles produce.

---

## 1) The Four Pillars of OOP

| Pillar | One-liner | Java mechanism |
|--------|-----------|----------------|
| **Encapsulation** | Bundle data + behavior, hide internal state | `private` fields + getters/setters |
| **Inheritance** | Reuse & specialize a base type | `extends`, `implements` |
| **Polymorphism** | One interface, many concrete behaviors | overriding + dynamic dispatch |
| **Abstraction** | Expose *what*, hide *how* | `interface`, `abstract class` |

### 1-1) Encapsulation

Keep fields `private` and expose controlled access. This protects invariants and lets you change internals without breaking callers.

```java
// java
public class BankAccount {
    private double balance;                 // hidden state

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("must be positive");
        balance += amount;                  // invariant enforced here
    }
    public double getBalance() { return balance; }   // read-only access
}
```

**Why it matters**: callers cannot set `balance` to a negative value directly. All mutation goes through validated methods.

### 1-2) Inheritance (is-a)

A subclass **is-a** specialized version of its superclass and inherits its members.

```java
// java
class Animal {
    void eat() { System.out.println("eating"); }
}
class Dog extends Animal {                  // Dog IS-A Animal
    void bark() { System.out.println("woof"); }
}
```

**Caution**: inheritance is tight coupling. Overusing it produces rigid, fragile hierarchies. Prefer composition when you only want to reuse code (see section 3).

### 1-3) Polymorphism

**Runtime (dynamic) polymorphism** — method **overriding** + dynamic dispatch: the actual object type decides which method runs.

```java
// java
Animal a = new Dog();     // reference type Animal, actual type Dog
a.eat();                  // resolved at RUNTIME -> Dog's version if overridden
```

**Compile-time (static) polymorphism** — method **overloading**: same name, different parameter lists, resolved at compile time.

```java
// java
int add(int a, int b)         { return a + b; }
double add(double a, double b){ return a + b; }   // overloaded
```

| | Overriding | Overloading |
|---|-----------|-------------|
| Resolved | Runtime | Compile time |
| Signature | Same | Different params |
| Where | Subclass redefines parent method | Same class (usually) |
| Return type | Same/covariant | Can differ |

### 1-4) Abstraction

Hide implementation behind a contract. Callers depend on the abstraction, not the concrete class.

```java
// java
interface Shape { double area(); }          // WHAT, not HOW

class Circle implements Shape {
    private final double r;
    Circle(double r){ this.r = r; }
    public double area(){ return Math.PI * r * r; }   // the HOW
}
```

---

## 2) Interface vs Abstract Class ⭐⭐⭐⭐⭐

One of the most common OOP interview questions.

| Aspect | `interface` | `abstract class` |
|--------|-------------|------------------|
| Multiple inheritance | Yes — a class can implement many | No — single superclass only |
| State (fields) | Only `public static final` constants | Can have instance fields |
| Constructors | No | Yes |
| Method bodies | `default`/`static` methods (Java 8+); rest abstract | Mix of abstract + concrete |
| Access modifiers | Members implicitly `public` | Any (`private`, `protected`, ...) |
| Relationship | "**can-do**" capability (`Comparable`, `Runnable`) | "**is-a**" shared base with common code |

**Rule of thumb:**
- Use an **interface** to define a capability many unrelated classes can have, or when you need multiple inheritance of type.
- Use an **abstract class** when subclasses share common state/implementation and form a true is-a family.

```java
// java
interface Drawable { void draw(); }         // capability

abstract class Widget {                      // shared base + state
    protected int x, y;                      // common state
    Widget(int x, int y){ this.x = x; this.y = y; }
    void moveTo(int x, int y){ this.x = x; this.y = y; }  // shared behavior
    abstract void render();                  // subclass must define
}

class Button extends Widget implements Drawable {
    Button(int x, int y){ super(x, y); }
    void render(){ /* ... */ }
    public void draw(){ /* ... */ }
}
```

---

## 3) Composition vs Inheritance ⭐⭐⭐⭐

**"Favor composition over inheritance."**

- **Inheritance (is-a)**: `Car extends Vehicle`. Compile-time, rigid, one parent, exposes parent internals to child.
- **Composition (has-a)**: `Car` holds an `Engine`. Runtime-flexible, swappable, keeps classes small.

```java
// java — composition: Car HAS-A Engine, delegates to it
interface Engine { void start(); }
class ElectricEngine implements Engine { public void start(){ /* ... */ } }

class Car {
    private Engine engine;                   // composed part
    Car(Engine engine){ this.engine = engine; }         // injected -> swappable
    void start(){ engine.start(); }          // delegation
}
// Swap ElectricEngine -> PetrolEngine WITHOUT changing Car.
```

**When to use which:**
- Use **inheritance** only for a genuine is-a relationship where the subtype is substitutable (Liskov).
- Use **composition** to reuse behavior, combine capabilities, or vary behavior at runtime.

---

## 4) SOLID Principles ⭐⭐⭐⭐⭐

| Principle | Meaning | Smell it fixes |
|-----------|---------|----------------|
| **S** — Single Responsibility | One class, one reason to change | God class doing many jobs |
| **O** — Open/Closed | Open to extension, closed to modification | Editing a big `switch` per new type |
| **L** — Liskov Substitution | Subtypes substitutable for their base | `Square extends Rectangle` breaking `setWidth` |
| **I** — Interface Segregation | Prefer small focused interfaces | Fat interface forcing empty/unsupported methods |
| **D** — Dependency Inversion | Depend on abstractions, not concretions | High-level class hard-wired to a concrete DB |

```java
// java — Open/Closed with polymorphism instead of a switch
interface Discount { double apply(double price); }
class NoDiscount      implements Discount { public double apply(double p){ return p; } }
class PercentDiscount implements Discount {
    private final double pct;
    PercentDiscount(double pct){ this.pct = pct; }
    public double apply(double p){ return p * (1 - pct); }
}
// Add a new discount -> new class. Existing code is untouched (closed to modification).
```

---

## 5) Beyond SOLID: Everyday Principles ⭐⭐⭐⭐

| Principle | Says | Failure it prevents |
|-----------|------|---------------------|
| **DRY** — don't repeat yourself | Every piece of knowledge has one authoritative home | The same rule fixed in three places, and missed in a fourth |
| **KISS** — keep it simple | Prefer the boring solution | A framework where a method would do |
| **YAGNI** — you aren't gonna need it | Build for today's requirement | Configurability nobody ever uses, but everyone must read |
| **Law of Demeter** | Talk only to your immediate collaborators | `order.getCustomer().getAddress().getCity()` — a train wreck that couples you to three classes' internals |
| **Tell, don't ask** | Send the object a command instead of pulling out its state to decide for it | An anaemic model with all the logic in "service" classes |
| **Program to an interface** | Depend on the capability, not the implementation | `ArrayList x = …` where `List x = …` would do |
| **Composition over inheritance** | See §3 | Fragile base classes |

DRY has a limit worth stating in an interview: **duplication is cheaper than the wrong
abstraction.** Two pieces of code that merely *look* alike but change for different reasons
should stay apart.

---

## 6) Object Contracts ⭐⭐⭐⭐

Beyond `equals`/`hashCode` (§7), two contracts come up in almost every design:

### `Comparable` vs `Comparator`

| | `Comparable<T>` | `Comparator<T>` |
|---|-----------------|-----------------|
| Where | Implemented **by** the class | A separate object |
| Method | `int compareTo(T other)` | `int compare(T a, T b)` |
| Means | The type's **natural order** (one only) | Any number of alternative orders |
| Use when | There is an obvious ordering (`String`, `Integer`, a version) | Sorting by different fields, or ordering a class you don't own |

```java
// java
class Version implements Comparable<Version> {           // natural order
    private final int build;
    Version(int build) { this.build = build; }
    @Override public int compareTo(Version o) { return Integer.compare(build, o.build); }
}

// alternative orders, composed and reusable
Comparator<Employee> byPay = Comparator.comparingLong(Employee::salary)
                                       .reversed()
                                       .thenComparing(Employee::name);
```

The contract: `compareTo` must be transitive and antisymmetric, and **should be consistent
with `equals`** — when it is not, a `TreeSet` (which dedupes by `compareTo == 0`) and a
`HashSet` (which dedupes by `equals`) disagree about what a duplicate is. Never implement
it as `a.value - b.value`: that overflows for large values.

### Copying: `clone` vs a copy constructor

`Object.clone()` is shallow, requires `Cloneable`, and bypasses constructors. A **copy
constructor** (`new Order(other)`) or a static factory is clearer and preserves
invariants. Best of all is immutability — nothing to copy if nothing can change.

---

## 7) Common Interview Q&A

**Q: What is the difference between `==` and `.equals()`?**
`==` compares references (same object in memory) for objects, or values for primitives. `.equals()` compares logical equality as defined by the class. Always override `equals()` **and** `hashCode()` together.

**Q: Why override `hashCode()` when you override `equals()`?**
The contract: equal objects must have equal hash codes. Hash-based collections (`HashMap`, `HashSet`) use `hashCode()` to find buckets, then `equals()` within a bucket. Breaking the contract means objects "vanish" from these collections.

```java
// java
@Override public boolean equals(Object o){
    if (this == o) return true;
    if (!(o instanceof Point)) return false;
    Point p = (Point) o;
    return x == p.x && y == p.y;
}
@Override public int hashCode(){ return Objects.hash(x, y); }
```

**Q: Can Java have multiple inheritance?**
Not for classes (single `extends`), to avoid the "diamond problem." But a class can `implements` multiple interfaces. Since Java 8, interfaces can carry `default` methods; if two interfaces provide the same default, the implementing class must override to resolve the conflict.

**Q: What is method hiding vs overriding?**
`static` methods are **hidden**, not overridden — resolved by the reference type at compile time. Instance methods are **overridden** — resolved by the actual object type at runtime.

**Q: `final`, `finally`, `finalize`?**
- `final`: keyword — a constant variable / non-overridable method / non-extendable class.
- `finally`: a block that always runs after `try/catch` (cleanup).
- `finalize()`: deprecated Object method the GC once called before reclaiming an object; don't rely on it.

**Q: What is an abstract method?**
A method declared without a body (`abstract void foo();`). Its class must be `abstract`, and concrete subclasses must implement it.

**Q: What is dynamic dispatch / late binding?**
The JVM picks the overridden method based on the runtime type of the object, not the declared reference type. This is the mechanism behind runtime polymorphism.

**Q: Static vs instance members?**
`static` members belong to the class (one shared copy, accessed via the class). Instance members belong to each object. Static methods cannot access instance state directly and cannot be overridden (only hidden).

**Q: What is the `super` keyword?**
Refers to the immediate parent: call a parent constructor (`super(...)`), or invoke a parent method/field the child has overridden/hidden (`super.method()`).

**Q: Cohesion vs coupling?**
- **High cohesion** (good): a class's members strongly relate to one purpose.
- **Loose coupling** (good): classes depend on abstractions, minimizing knowledge of each other's internals.
Aim for high cohesion + loose coupling.

**Q: What is an immutable class and why use one?**
State cannot change after construction (e.g. `String`, `Integer`). Make fields `private final`, provide no setters, and defensively copy mutable inputs. Benefits: thread-safety, safe as map keys, easier reasoning.

```java
// java
public final class Money {
    private final long cents;
    private final String currency;
    public Money(long cents, String currency){ this.cents = cents; this.currency = currency; }
    public long getCents(){ return cents; }              // no setters
}
```

**Q: What is the difference between aggregation and composition?**
Both are has-a. **Composition** = strong ownership; the part cannot outlive the whole (a `House` and its `Room`s). **Aggregation** = weak; the part can exist independently (a `Team` and its `Player`s).

---

## 8) Quick Recap Checklist

```text
[ ] Can explain the 4 pillars with a one-line example each
[ ] Interface vs abstract class: multiple inheritance, state, when to use
[ ] Composition over inheritance: why + a delegation example
[ ] All 5 SOLID principles + the smell each fixes
[ ] DRY / KISS / YAGNI / Law of Demeter — and where DRY stops
[ ] equals()/hashCode() contract and why both matter
[ ] Comparable vs Comparator, and consistency with equals
[ ] Overriding vs overloading (runtime vs compile-time)
[ ] Dynamic dispatch = runtime polymorphism mechanism
[ ] Immutability: how to build one and why it helps
```

---

## References
- [`../../cheatsheet/ood_design.md`](../../cheatsheet/ood_design.md) — Object-Oriented Design / Low-Level Design patterns
- [`../../cheatsheet/design.md`](../../cheatsheet/design.md) — data structure & system coding design
- [`java_design_pattern.md`](./java_design_pattern.md) — the GoF patterns these principles produce
- [`java_basic.md`](./java_basic.md) — the language mechanics (`static`, `final`, `Object`'s methods)
- [`java_generics.md`](./java_generics.md) — variance, wildcards and substitution in the type system
