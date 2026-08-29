<!-- fd101c348a5c -->
# 並行處理模式（Java）

> **範圍** — L4 以上會出現的少數幾題 Java 並行題：依序列印、生產者／消費者、讀寫協調，以及它們會用到的同步原語。
> **另見**：[python_gotchas.md](./python_gotchas.md) — GIL 與 Python 的並行故事；[design.md](./design.md) — 執行緒安全的結構設計；[java_trick.md](./java_trick.md) — Java 容器慣用手法。

<!-- fa160d1433d1 -->
## LeetCode 題目清單

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

<!-- 91a68817d222 -->
## 總覽
Google 在 L4 以上偶爾會考並行／多執行緒題。這類題目在檢驗你對同步機制、執行緒安全，以及並行資料結構設計的理解。

<!-- 82bfa9b5d085 -->
### 什麼時候用
- 題目明講了執行緒、平行執行，或生產者／消費者
- 要求設計執行緒安全資料結構的設計題

<!-- 3299a9b32256 -->
## 模式 1：依序列印／順序控制

<!-- 7050009915ae -->
### 用 CountDownLatch — LC 1114
<!--CODE-->

<!-- e6e9e2300c31 -->
### 用 Semaphore — LC 1115
<!--CODE-->

<!-- e3cfe2e71d59 -->
## 模式 2：生產者－消費者／有界緩衝區

<!--CODE-->

<!-- 8009413e8c9f -->
## 模式 3：讀寫鎖／H2O 問題

<!--CODE-->

<!-- cea052688fad -->
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

<!-- df1083b2f332 -->
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
