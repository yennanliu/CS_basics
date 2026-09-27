<!-- 2423575ac46a -->
# 並行處理模式（Java）

> **範圍** — L4 以上會出現的少數幾題 Java 並行題：依序列印、生產者／消費者、讀寫協調與死結避免，以及它們會用到的同步原語。
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

<!-- 1a15f8bebd0e -->
## 模式 4：死結 — 打破四個條件之一

死結需要**四個條件**同時成立，所以只要阻止其中任何一個就夠了：

| 條件 | 意義 | 怎麼消除 |
|---|---|---|
| 互斥（Mutual exclusion） | 一把鎖同一時間只被一個執行緒持有 | 幾乎無法消除 —— 這正是鎖的用途 |
| 持有並等待（Hold and wait） | 執行緒握著一把鎖，同時又要求另一把 | 一開始就取得全部，或在要求前先釋放 |
| 不可搶占（No preemption） | 鎖無法被奪走 | `tryLock(timeout)` —— 退讓後重試，而不是一直等 |
| **循環等待（Circular wait）** | 「誰在等誰」形成一個環 | **全域的加鎖順序**，或限制競爭者的數量 |

面試時要攻擊的是循環等待：它最便宜就能強制執行，也最容易論證正確性。

<!-- 6fa9250b2fc2 -->
### 標準解法 — 全域加鎖順序

給每把鎖一個固定的等級（id、位址，任何全序且穩定的東西），並讓每個執行緒都依等級遞增的順序取得。
要形成環，必須有人握著高等級的鎖去等低等級的鎖，而這條規則禁止這麼做 —— 所以環不可能形成。

<!--CODE-->

CtCI 15.4 問的是*預防*版本：讓執行緒事先宣告它會需要哪些鎖，建立「先取得於」（acquires-before）圖，
並拒絕任何會讓圖閉合成環的宣告。同樣的想法，只是在註冊時檢查，而不是靠慣例強制。

<!-- c3b6095b904b -->
### 哲學家就餐 — LC 1226

五位哲學家、五支叉子，每人需要身旁的兩支。每個人都先拿左手邊的叉子，正好就是循環等待。
兩個一行就能搞定的解法：

<!--CODE-->

另一種效果相同的做法：先拿 `forks[min(left, right)]` —— 也就是上面的全域順序解法。
第三個選項是讓奇數號的哲學家先拿右邊，打破造成環的對稱性。

<!-- 8471a75a2151 -->
## 模式 5：接力棒傳遞 — 誰做完就喚醒正確的下一位

當超過兩個角色交錯執行時，不要讓每個執行緒去輪詢共享計數器。給每個角色各自的 semaphore，
讓剛印完的執行緒**只釋放擁有下一個值的那一個** —— 沒有任何執行緒會去檢查別人的狀態。

<!--CODE-->

每個迴圈只走訪自己角色擁有的值，而任一時刻恰好只有一張許可在流通 —— 所以列印順序在結構上就是正確的，
沒有需要保護的共享可變計數器。LC 1114 和 LC 1116 是同一根接力棒，只是角色比較少。

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

<!-- stale: fd101c348a5c -->
# 並行處理模式（Java）

> **範圍** — L4 以上會出現的少數幾題 Java 並行題：依序列印、生產者／消費者、讀寫協調，以及它們會用到的同步原語。
> **另見**：[python_gotchas.md](./python_gotchas.md) — GIL 與 Python 的並行故事；[design.md](./design.md) — 執行緒安全的結構設計；[java_trick.md](./java_trick.md) — Java 容器慣用手法。
