# Concurrency Patterns (Java)

> **Scope** — The handful of Java concurrency problems that appear at L4+ — ordered printing, producer/consumer, read-write coordination and deadlock avoidance — plus the primitives they need.
> **See also**: [python_gotchas.md](./python_gotchas.md) — the GIL and Python's concurrency story; [design.md](./design.md) — thread-safe structure design; [java_trick.md](./java_trick.md) — Java collection idioms.

## LeetCode Problem Lists

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

## Overview
Google sometimes asks concurrency/threading problems at L4+. These test understanding of synchronization, thread safety, and concurrent data structure design.

### When to Use
- Problem explicitly mentions threads, parallel execution, or producer/consumer
- Design problems requiring thread-safe data structures

## Pattern 1: Print in Order / Sequence Control

### Using CountDownLatch — LC 1114
```java
// LC 1114 — Print in Order
class Foo {
    private CountDownLatch latch1 = new CountDownLatch(1);
    private CountDownLatch latch2 = new CountDownLatch(1);

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        latch1.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        latch1.await();
        printSecond.run();
        latch2.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        latch2.await();
        printThird.run();
    }
}
```

### Using Semaphore — LC 1115
```java
// LC 1115 — Print FooBar Alternately
class FooBar {
    private int n;
    private Semaphore fooSem = new Semaphore(1);
    private Semaphore barSem = new Semaphore(0);

    public FooBar(int n) { this.n = n; }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooSem.acquire();
            printFoo.run();
            barSem.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barSem.acquire();
            printBar.run();
            fooSem.release();
        }
    }
}
```

## Pattern 2: Producer-Consumer / Bounded Buffer

```java
// LC 1188 — Design Bounded Blocking Queue
class BoundedBlockingQueue {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public BoundedBlockingQueue(int capacity) { this.capacity = capacity; }

    public void enqueue(int element) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) notFull.await();
            queue.offer(element);
            notEmpty.signal();
        } finally { lock.unlock(); }
    }

    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) notEmpty.await();
            int val = queue.poll();
            notFull.signal();
            return val;
        } finally { lock.unlock(); }
    }

    public int size() { lock.lock(); try { return queue.size(); } finally { lock.unlock(); } }
}
```

## Pattern 3: Read-Write Lock / H2O Problem

```java
// LC 1117 — Building H2O
class H2O {
    private Semaphore hSem = new Semaphore(2);
    private Semaphore oSem = new Semaphore(0);
    private CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        hSem.release(2);
    });

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hSem.acquire();
        releaseHydrogen.run();
        try { barrier.await(); } catch (BrokenBarrierException e) {}
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        // NOTE: sketch only — oSem is initialized to 0 permits and never released,
        // so this acquire() blocks forever. A correct H2O gates O with its own
        // Semaphore(1) (limit one O in flight) and lets the CyclicBarrier(3)
        // rendezvous 2 H + 1 O; the barrier action then release(2)s hSem.
        oSem.acquire();
        releaseOxygen.run();
        try { barrier.await(); } catch (BrokenBarrierException e) {}
    }
}
```

## Pattern 4: Deadlock — Break One of the Four Conditions

A deadlock needs **all four** of these at once, so preventing any single one is enough:

| Condition | What it means | How you remove it |
|---|---|---|
| Mutual exclusion | A lock is held by one thread at a time | Rarely removable — it is the point of a lock |
| Hold and wait | A thread holds one lock while asking for another | Acquire everything up front, or release before asking |
| No preemption | A lock cannot be taken away | `tryLock(timeout)` — back off and retry instead of waiting |
| **Circular wait** | A cycle in "who waits for whom" | **A global lock order**, or cap the number of contenders |

Circular wait is the one to attack in an interview: it is the cheapest to enforce and the
easiest to argue correct.

### The standard fix — a global lock order

Give every lock a fixed rank (an id, an address, anything total and stable) and make every
thread acquire in increasing rank. A cycle needs someone to hold a high lock while waiting
for a low one, which the rule forbids — so no cycle can form.

```java
// java
// Transfer between two accounts without deadlocking against the reverse transfer
// IDEA: order the two locks by a stable id, so t1 (A->B) and t2 (B->A) both take
//       the lower id first and one of them simply waits
void transfer(Account from, Account to, long amount) {
    Account first  = from.id < to.id ? from : to;     // total order over the locks
    Account second = from.id < to.id ? to   : from;
    synchronized (first) {
        synchronized (second) {
            from.debit(amount);
            to.credit(amount);
        }
    }
}
```

CtCI 15.4 asks for the *prevention* variant: make threads declare the locks they will need
up front, build the "acquires-before" graph, and refuse any declaration that would close a
cycle. Same idea, checked at registration time instead of enforced by convention.

### Dining Philosophers — LC 1226

Five philosophers, five forks, each needs the two beside them. Everyone grabbing their left
fork first is exactly a circular wait. Two one-line fixes:

```java
// java
// LC 1226 — The Dining Philosophers
// IDEA: a Semaphore(4) means at most 4 philosophers ever hold a fork, so at least one
//       always finds both free — the cycle can never close
// time = O(1) per meal, space = O(n)
class DiningPhilosophers {
    private final ReentrantLock[] forks = new ReentrantLock[5];
    private final Semaphore room = new Semaphore(4);   // n - 1 diners

    public DiningPhilosophers() {
        for (int i = 0; i < 5; i++) forks[i] = new ReentrantLock();
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,  Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,   Runnable putRightFork)
            throws InterruptedException {
        int left = philosopher, right = (philosopher + 1) % 5;

        room.acquire();
        forks[left].lock();
        forks[right].lock();

        pickLeftFork.run(); pickRightFork.run(); eat.run();
        putLeftFork.run();  putRightFork.run();

        forks[right].unlock();
        forks[left].unlock();
        room.release();
    }
}
```

The alternative, same effect: take `forks[min(left, right)]` first — the global-order fix
above. A third option is having odd-numbered philosophers reach right-first, which breaks
the symmetry that creates the cycle.

## Pattern 5: Baton Passing — Whoever Finishes Wakes the Right Successor

When more than two roles interleave, do not have each thread poll a shared counter. Give
each role its own semaphore and let the thread that just printed **release exactly the one
that owns the next value** — no thread ever inspects another's state.

```java
// java
// LC 1195 — Fizz Buzz Multithreaded
// IDEA: one semaphore per role, all starting at 0 except number(). After printing i,
//       hand the permit to whoever owns i+1, so the order is enforced by construction.
// time = O(n) total, space = O(1)
class FizzBuzz {
    private final int n;
    private final Semaphore num      = new Semaphore(1);   // 1 owns the first turn
    private final Semaphore fizz     = new Semaphore(0);
    private final Semaphore buzz     = new Semaphore(0);
    private final Semaphore fizzbuzz = new Semaphore(0);

    public FizzBuzz(int n) { this.n = n; }

    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 3; i <= n; i += 3) {
            if (i % 5 == 0) continue;                      // 15s belong to fizzbuzz()
            fizz.acquire();
            printFizz.run();
            handOff(i + 1);
        }
    }

    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 5; i <= n; i += 5) {
            if (i % 3 == 0) continue;
            buzz.acquire();
            printBuzz.run();
            handOff(i + 1);
        }
    }

    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 15; i <= n; i += 15) {
            fizzbuzz.acquire();
            printFizzBuzz.run();
            handOff(i + 1);
        }
    }

    public void number(Runnable printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 || i % 5 == 0) continue;
            num.acquire();
            printNumber.run();
            handOff(i + 1);
        }
    }

    /** Wake the single thread that owns value i. */
    private void handOff(int i) {
        if (i > n) {                                       // past the end: wake everyone
            num.release(); fizz.release();                 // so nobody is left blocked
            buzz.release(); fizzbuzz.release();
        } else if (i % 15 == 0) fizzbuzz.release();
        else if (i % 3 == 0)    fizz.release();
        else if (i % 5 == 0)    buzz.release();
        else                    num.release();
    }
}
```

Each loop only visits the values that role owns, and exactly one permit is in flight at any
moment — so the printing order is correct by construction, with no shared mutable counter to
guard. LC 1114 and LC 1116 are the same baton with fewer roles.

## Key Concurrency Primitives

| Primitive | Purpose | Key Methods |
|-----------|---------|-------------|
| `synchronized` | Mutual exclusion | `wait()`, `notify()`, `notifyAll()` |
| `ReentrantLock` | Explicit lock with conditions | `lock()`, `unlock()`, `newCondition()` |
| `Semaphore` | Counting permits | `acquire()`, `release()` |
| `CountDownLatch` | One-time gate (count to 0) | `await()`, `countDown()` |
| `CyclicBarrier` | Reusable rendezvous point | `await()` |
| `volatile` | Visibility guarantee | — |
| `AtomicInteger` | Lock-free counter | `incrementAndGet()`, `compareAndSet()` |

## LC Example

| # | Problem | Key Concept |
|---|---------|-------------|
| 1114 | Print in Order | CountDownLatch / Semaphore |
| 1115 | Print FooBar Alternately | Semaphore pair |
| 1116 | Print Zero Even Odd | Semaphore coordination |
| 1117 | Building H2O | CyclicBarrier + Semaphore |
| 1188 | Bounded Blocking Queue | ReentrantLock + Condition |
| 1195 | Fizz Buzz Multithreaded | Semaphore / CyclicBarrier |
| 1226 | The Dining Philosophers | Deadlock avoidance |
