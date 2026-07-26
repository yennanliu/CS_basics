# Java Design Pattern

Concise reference to the most interview-relevant patterns with minimal Java snippets.

## 0) Categories

| Category | Purpose | Examples |
|----------|---------|----------|
| Creational | How objects are created | Singleton, Factory, Builder |
| Structural | How objects are composed | Decorator, Adapter, Proxy |
| Behavioral | How objects interact | Strategy, Observer, Template Method |

---

## 1) Singleton (Creational)

One instance, global access. Thread-safe, lazy, and efficient version:

```java
// Bill Pugh holder idiom — lazy, thread-safe, no locking
public class Config {
    private Config() {}
    private static class Holder { static final Config INSTANCE = new Config(); }
    public static Config getInstance() { return Holder.INSTANCE; }
}
```

`enum` singletons are also safe against reflection & serialization:
```java
public enum Config { INSTANCE; }
```
**Watch out**: singletons are global state — hard to test, can hide dependencies.

---

## 2) Factory Method (Creational)

Defer instantiation to a method so callers depend on an interface, not a concrete class.

```java
interface Shape { void draw(); }
class Circle implements Shape { public void draw() {} }
class Square implements Shape { public void draw() {} }

class ShapeFactory {
    static Shape create(String type) {
        return switch (type) {
            case "circle" -> new Circle();
            case "square" -> new Square();
            default -> throw new IllegalArgumentException(type);
        };
    }
}
```

---

## 3) Builder (Creational)

Construct complex/optional-heavy objects step by step; avoids telescoping constructors.

```java
class User {
    private final String name;   // required
    private final int age;       // optional
    private User(Builder b) { this.name = b.name; this.age = b.age; }

    static class Builder {
        private final String name;
        private int age;
        Builder(String name) { this.name = name; }
        Builder age(int age) { this.age = age; return this; }
        User build() { return new User(this); }
    }
}
// usage: new User.Builder("Sam").age(30).build();
```

---

## 4) Strategy (Behavioral)

Encapsulate interchangeable algorithms; swap behavior at runtime.

```java
interface SortStrategy { void sort(int[] a); }

class Sorter {
    private SortStrategy strategy;
    Sorter(SortStrategy s) { this.strategy = s; }
    void setStrategy(SortStrategy s) { this.strategy = s; }
    void run(int[] a) { strategy.sort(a); }
}
// A lambda IS a strategy: new Sorter(a -> Arrays.sort(a));
```

---

## 5) Observer (Behavioral)

One-to-many: subject notifies subscribers on state change (pub/sub, event listeners).

```java
interface Observer { void update(String event); }

class Subject {
    private final List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o) { observers.add(o); }
    void notifyAll(String event) { observers.forEach(o -> o.update(event)); }
}
```

---

## 6) Decorator (Structural)

Add responsibilities dynamically by wrapping — an alternative to subclassing.

```java
interface Coffee { double cost(); }
class Espresso implements Coffee { public double cost() { return 2.0; } }

class MilkDecorator implements Coffee {
    private final Coffee inner;
    MilkDecorator(Coffee c) { this.inner = c; }
    public double cost() { return inner.cost() + 0.5; }
}
// usage: new MilkDecorator(new Espresso())  → 2.5
```
Java I/O (`BufferedReader(new FileReader(...))`) is a real-world decorator chain.

---

## 7) When to Use — Quick Map

| Need | Pattern |
|------|---------|
| Exactly one shared instance | Singleton |
| Decide concrete type at runtime | Factory |
| Many optional constructor params | Builder |
| Swap an algorithm at runtime | Strategy |
| Notify many on a change | Observer |
| Add behavior without subclassing | Decorator |

> **DI (Dependency Injection)** — supply a class's collaborators from outside
> instead of constructing them internally; improves testability and decoupling.
> - https://github.com/ChaoLiou/Blog/issues/74
> - https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/
