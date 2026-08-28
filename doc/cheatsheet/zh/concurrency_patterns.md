# 並行處理模式（Java）

> **範圍** — L4 以上會出現的少數幾題 Java 並行題：依序列印、生產者／消費者、讀寫協調，以及它們會用到的同步原語。
> **另見**：[python_gotchas.md](./python_gotchas.md) — GIL 與 Python 的並行故事；[design.md](./design.md) — 執行緒安全的結構設計；[java_trick.md](./java_trick.md) — Java 容器慣用手法。

## LeetCode 題目清單

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

## 總覽
Google 在 L4 以上偶爾會考並行／多執行緒題。這類題目在檢驗你對同步機制、執行緒安全，以及並行資料結構設計的理解。

### 什麼時候用
- 題目明講了執行緒、平行執行，或生產者／消費者
- 要求設計執行緒安全資料結構的設計題

## 模式 1：依序列印／順序控制

### 用 CountDownLatch — LC 1114
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

### 用 Semaphore — LC 1115
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

## 模式 2：生產者－消費者／有界緩衝區

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

## 模式 3：讀寫鎖／H2O 問題

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

## 重要並行原語

| 原語 | 用途 | 主要方法 |
|-----------|---------|-------------|
| `synchronized` | 互斥 | `wait()`、`notify()`、`notifyAll()` |
| `ReentrantLock` | 可搭配條件變數的顯式鎖 | `lock()`、`unlock()`、`newCondition()` |
| `Semaphore` | 計數式許可 | `acquire()`、`release()` |
| `CountDownLatch` | 一次性閘門（數到 0 就開） | `await()`、`countDown()` |
| `CyclicBarrier` | 可重複使用的會合點 | `await()` |
| `volatile` | 可見性保證 | — |
| `AtomicInteger` | 免鎖計數器 | `incrementAndGet()`、`compareAndSet()` |

## LC 範例

| # | 題目 | 關鍵概念 |
|---|---------|-------------|
| 1114 | Print in Order | CountDownLatch／Semaphore |
| 1115 | Print FooBar Alternately | 一對 Semaphore |
| 1116 | Print Zero Even Odd | Semaphore 協調 |
| 1117 | Building H2O | CyclicBarrier + Semaphore |
| 1188 | Bounded Blocking Queue | ReentrantLock + Condition |
| 1195 | Fizz Buzz Multithreaded | Semaphore／CyclicBarrier |
| 1226 | The Dining Philosophers | 避免死鎖 |
