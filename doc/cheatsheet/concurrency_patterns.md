# Concurrency Patterns (Java)

## Overview
Google sometimes asks concurrency/threading problems at L4+. These test understanding of synchronization, thread safety, and concurrent data structure design.

### When to Use
- Problem explicitly mentions threads, parallel execution, or producer/consumer
- Design problems requiring thread-safe data structures

## Pattern 1: Print in Order / Sequence Control

### Using CountDownLatch
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

### Using Semaphore
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
        oSem.acquire();  // wait for 2 H first — actually, simpler approach below
        releaseOxygen.run();
        try { barrier.await(); } catch (BrokenBarrierException e) {}
    }
}
```

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
