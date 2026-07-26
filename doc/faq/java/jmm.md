# JMM FAQ (Java Memory Model)

- https://javaguide.cn/java/jvm/memory-area.html#%E5%A0%86
- https://pdai.tech/md/java/jvm/java-jvm-jmm.html
- https://javaguide.cn/java/concurrent/jmm.html#happens-before-%E5%8E%9F%E5%88%99%E6%98%AF%E4%BB%80%E4%B9%88

## Overview

The **Java Memory Model (JMM)** defines the rules for how threads interact through
memory — specifically *when* a write by one thread becomes visible to a read by
another, and *what* reorderings the compiler/CPU may perform. It is an abstract
contract, not a description of physical RAM.

Why it exists: compilers and CPUs reorder instructions and cache values in
registers/CPU caches for speed. Without rules, multithreaded code would be
unpredictable across hardware.

---

## 1) The Three Guarantees

| Property | Question it answers | Broken by |
|----------|---------------------|-----------|
| **Atomicity** | Does an op happen all-or-nothing? | `count++` (read-modify-write is 3 steps) |
| **Visibility** | Does thread B see thread A's write? | Values cached per-thread |
| **Ordering** | Do ops appear in program order? | Compiler/CPU reordering |

- `long`/`double` reads/writes are not guaranteed atomic on all platforms unless
  `volatile` (64-bit values may be split into two 32-bit ops).

---

## 2) happens-before

The central JMM concept. If action A **happens-before** action B, then A's effects
(memory writes) are **visible and ordered** before B. If two actions are not ordered
by happens-before, the JVM is free to reorder them.

Key happens-before rules:
- **Program order**: within one thread, each action happens-before the next.
- **Monitor lock**: unlocking a monitor happens-before a later lock of the same monitor.
- **volatile**: a write to a volatile field happens-before every later read of it.
- **Thread start**: `Thread.start()` happens-before any action in the started thread.
- **Thread join**: actions in a thread happen-before another thread returns from
  `join()` on it.
- **Transitivity**: if A hb B and B hb C, then A hb C.

> happens-before is about **visibility + ordering guarantees**, not about wall-clock
> time. It does not necessarily mean A physically executes before B.

---

## 3) volatile

```java
private volatile boolean running = true;   // flag another thread can stop
```

Guarantees:
- **Visibility**: writes are flushed to main memory; reads always fetch the latest.
- **Ordering**: inserts memory barriers preventing reordering across the access.

Does **NOT** provide:
- **Atomicity of compound actions** — `volatile int x; x++;` is still racy.
  Use `synchronized` or `AtomicInteger` for read-modify-write.

Classic use: the **double-checked locking** singleton needs `volatile` so a
partially-constructed object is never seen:
```java
private static volatile Singleton instance;
```

---

## 4) synchronized

- Provides **mutual exclusion** (atomicity for the critical section) **and**
  visibility (release/acquire semantics of the monitor).
- Entering a `synchronized` block acquires the monitor; exiting releases it, and
  release happens-before a subsequent acquire of the same monitor.
- So variables written inside a synchronized block are visible to the next thread
  that enters a block on the same lock — no `volatile` needed for those.

---

## 5) final Field Semantics

- A properly constructed object's `final` fields are guaranteed visible to other
  threads **without synchronization**, provided the `this` reference does not
  escape during construction.
- This is what makes immutable objects (all fields `final`, no leaking `this`)
  safe to share freely across threads.

---

## Summary

| Tool | Atomicity | Visibility | Ordering |
|------|-----------|-----------|----------|
| `volatile` | ✗ (single read/write only) | ✓ | ✓ |
| `synchronized` | ✓ | ✓ | ✓ |
| `final` (immutable) | ✓ (never changes) | ✓ (after safe construction) | ✓ |
| Atomic classes | ✓ (CAS) | ✓ | ✓ |
