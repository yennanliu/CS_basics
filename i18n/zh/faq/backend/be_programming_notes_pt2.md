<!-- c1401aca65e4 -->
# 後端工程師的後端程式筆記（第二部）

> 重點：執行緒池、非同步程式設計、快取、訊息佇列、分散式交易

<!-- b018ba0be47e -->
## 目錄

1. [執行緒池與 Executor 框架](#thread-pool--executor-framework)
2. [CompletableFuture 與非同步程式設計](#completablefuture--async-programming)
3. [快取策略](#caching-strategies)
4. [訊息佇列模式](#message-queue-patterns)
5. [分散式交易](#distributed-transactions)
6. [協調用的工具類](#coordination-utilities)
7. [優雅關閉](#graceful-shutdown)
8. [背壓處理](#backpressure-handling)
9. [面試常見問題](#common-interview-questions)

---

<!-- 366c46e8023b -->
## 執行緒池與 Executor 框架

<!-- fc1d8de5e000 -->
### 1. ThreadPoolExecutor 細看

<!--CODE-->

<!-- dcfddc1dbff9 -->
### 2. 拒絕策略比較

| 策略 | 行為 | 適用場景 |
|--------|----------|----------|
| `AbortPolicy` | 丟出 `RejectedExecutionException` | 快速失敗，讓監控告警 |
| `CallerRunsPolicy` | 由呼叫端的執行緒自己執行該任務 | 背壓，把生產端拖慢 |
| `DiscardPolicy` | 安靜地丟掉任務 | 不關鍵的任務 |
| `DiscardOldestPolicy` | 丟掉佇列中最舊的任務 | 寧可要最新的資料 |

<!--CODE-->

<!-- 6ede6b863ea3 -->
### 3. 佇列型別怎麼選

| 佇列型別 | 行為 | 適用場景 |
|------------|----------|----------|
| `LinkedBlockingQueue` | 無界或有界的 FIFO | 通用 |
| `ArrayBlockingQueue` | 有界，底層是陣列 | 記憶體有限 |
| `SynchronousQueue` | 沒有容量，直接交棒 | 優先把執行緒開滿 |
| `PriorityBlockingQueue` | 依優先序排列 | 任務有優先順序 |

<!--CODE-->

<!-- b108a1f8fa14 -->
### 4. 監控執行緒池的健康狀況

<!--CODE-->

---

<!-- 04379393cb2a -->
## CompletableFuture 與非同步程式設計

<!-- 06c205d32f8c -->
### 1. CompletableFuture 的基本用法

<!--CODE-->

<!-- 213562ab755a -->
### 2. 錯誤處理

<!--CODE-->

<!-- 48f1f98013e2 -->
### 3. 平行執行的幾種寫法

<!--CODE-->

---

<!-- b5645795441d -->
## 快取策略

<!-- fa59f66b7a58 -->
### 1. Cache-Aside 模式（延遲載入）

<!--CODE-->

<!-- a5c11ec5e718 -->
### 2. Write-Through 模式

<!--CODE-->

<!-- 6128997f59c0 -->
### 3. Write-Behind（Write-Back）模式

<!--CODE-->

<!-- f95c4dfe8c0d -->
### 4. 快取加上分散式鎖（避免快取擊穿）

<!--CODE-->

<!-- f4f03f2379df -->
### 5. Refresh-Ahead 模式

<!--CODE-->

---

<!-- 12c89c842437 -->
## 訊息佇列模式

<!-- 40cea190ec26 -->
### 1. At-Least-Once 投遞加上冪等

<!--CODE-->

<!-- 68fe801ceb2f -->
### 2. Transactional Outbox 模式

<!--CODE-->

<!-- 45af35e018b1 -->
### 3. 死信佇列（Dead Letter Queue）處理器

<!--CODE-->

<!-- 475cf6c30735 -->
### 4. Consumer group 與分區分配

<!--CODE-->

---

<!-- 9b5fb201d45b -->
## 分散式交易

<!-- afe948d96d34 -->
### 1. Saga 模式（Choreography 編舞式）

<!--CODE-->

<!-- 66ae9874aac0 -->
### 2. Saga 模式（Orchestrator 編排式）

<!--CODE-->

<!-- b744fefd2175 -->
### 3. 兩階段提交（2PC）模擬

<!--CODE-->

---

<!-- cb9704e46b61 -->
## 協調用的工具類

<!-- 36dea87dbf2c -->
### 1. CountDownLatch — 等 N 件事發生

<!--CODE-->

<!-- 74de49226c78 -->
### 2. CyclicBarrier — 同步會合點

<!--CODE-->

<!-- 2d260567bda6 -->
### 3. Semaphore — 限制同時存取數

<!--CODE-->

<!-- cdf44d4e4e31 -->
### 4. Phaser — 有彈性的 barrier

<!--CODE-->

---

<!-- d7fc8855e022 -->
## 優雅關閉

<!-- d15c36ca4f0e -->
### 1. ExecutorService 的優雅關閉

<!--CODE-->

<!-- ea31e16fb739 -->
### 2. Spring Boot 的優雅關閉

<!--CODE-->

---

<!-- 50c15f71b072 -->
## 背壓處理

<!-- 3e2728ad50dc -->
### 1. 有界佇列加上阻塞

<!--CODE-->

<!-- 5554c3c7c533 -->
### 2. 以速率為基礎的背壓

<!--CODE-->

<!-- 6671cf045c5f -->
### 3. Reactive 的背壓（觀念）

<!--CODE-->

---

<!-- 1062b62a84d4 -->
## 面試常見問題

<!-- 065753ead11a -->
### Q1：驚群（thundering herd）問題你會怎麼處理？

**答：**
1. **避免快取擊穿**：用分散式鎖或 `computeIfAbsent`
2. **把快取過期時間錯開**：TTL 加上隨機抖動
3. **請求合併**：多個相同請求共用一次後端呼叫
4. **斷路器**：避免連鎖失敗

<!--CODE-->

<!-- e0ff8fecc91b -->
### Q2：Saga 的 Choreography 和 Orchestration 差在哪？

| 面向 | Choreography 編舞式 | Orchestration 編排式 |
|--------|--------------|---------------|
| 協調方式 | 去中心化（靠事件） | 集中（由 orchestrator 主導） |
| 耦合 | 鬆 | 對 orchestrator 較緊 |
| 複雜度 | 事件流會變複雜 | 流程控制較單純 |
| 除錯 | 較難（分散各處） | 較容易（只有一個點） |
| 單點故障 | 沒有 | 有（orchestrator） |

<!-- 1b9e86aefc42 -->
### Q3：怎麼做到 exactly-once 處理？

**答：**真正的 exactly-once 很難。改用冪等：
1. **冪等操作**：把操作設計成可以安全重複執行
2. **冪等鍵**：記下已處理過的 request ID
3. **Transactional outbox**：原子地同時更新資料庫與 outbox

<!--CODE-->

<!-- 2313b00233fc -->
### Q4：CountDownLatch 和 CyclicBarrier 該用哪個？

| 特性 | CountDownLatch | CyclicBarrier |
|---------|----------------|---------------|
| 可重複使用 | 不行（一次性） | 可以（能 reset） |
| 用途 | 等 N 件事發生 | 反覆同步 N 條執行緒 |
| 動作 | 倒數到 0 | 在 barrier 前等待 |
| 例子 | 等各服務啟動完成 | 平行演算法的各個階段 |

<!-- e306c93bda7f -->
### Q5：執行緒池要怎麼避免記憶體洩漏？

1. **清掉 ThreadLocal**：用完一定要 remove
2. **有界佇列**：work queue 要設上限
3. **正確關閉**：呼叫 `shutdown()` 並 `awaitTermination()`
4. **拒絕策略**：被拒絕的任務要好好處理
5. **監控佇列長度**：佇列一直長就要告警

<!--CODE-->

---

<!-- a617dbbc5524 -->
## 總結：模式選擇指南

| 問題 | 模式 |
|---------|---------|
| 多個互不相干的任務要平行跑 | `CompletableFuture.allOf()` |
| 誰先回來就用誰 | `CompletableFuture.anyOf()` |
| 限制並發數 | `Semaphore` |
| 等事件發生 | `CountDownLatch` |
| 分階段同步 | `CyclicBarrier` |
| 動態協調 | `Phaser` |
| 分散式交易 | Saga（Orchestrator） |
| 事件驅動的交易 | Saga（Choreography） |
| 快取沒中時怎麼辦 | Cache-Aside + 鎖 |
| 寫入效能 | Write-Behind |
| 訊息可靠性 | At-least-once + 冪等 |
| 失敗訊息怎麼辦 | 死信佇列 |

---

<!-- 4789984e712a -->
## 參考資料

- [Java Concurrency in Practice](https://jcip.net/)
- [Martin Fowler - CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Microservices Patterns - Chris Richardson](https://microservices.io/patterns/)
- [AWS: Saga Pattern](https://docs.aws.amazon.com/prescriptive-guidance/latest/modernization-data-persistence/saga-pattern.html)
