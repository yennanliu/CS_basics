<!-- 9c3d842636d1 -->
# 後端工程師的後端程式筆記（第三部）

> 重點：進階並發、鎖的機制、高流量模式

<!-- 437673e8fdee -->
## 目錄

1. [進階並發](#advanced-concurrency)
   - [Fork/Join 框架](#1-forkjoin-framework)
   - [無鎖程式設計](#2-lock-free-programming)
   - [CAS 與 ABA 問題](#3-cas-and-aba-problem)
   - [記憶體屏障與 happens-before](#4-memory-barriers--happens-before)
2. [進階鎖](#advanced-locking)
   - [鎖分段（lock striping）](#1-lock-striping)
   - [分散式鎖演算法](#2-distributed-lock-algorithms)
   - [資料庫層的鎖](#3-database-locking)
   - [自旋鎖與 mutex](#4-spin-lock-vs-mutex)
3. [高流量模式](#high-traffic-patterns)
   - [請求合併](#1-request-coalescing)
   - [過載卸流（load shedding）](#2-load-shedding)
   - [艙壁模式（bulkhead）](#3-bulkhead-pattern)
   - [熱點 key 問題](#4-hot-key-problem)
   - [連線池調校](#5-connection-pool-tuning)
   - [一致性哈希](#6-consistent-hashing)
4. [實際情境](#real-world-scenarios)
5. [面試常見問題](#common-interview-questions)

---

<!-- a5521d02de08 -->
## 進階並發

<!-- 1b3e7083ba48 -->
### 1. Fork/Join 框架

<!--CODE-->

<!-- 0ed57af16b2b -->
### 2. 無鎖程式設計

<!--CODE-->

<!-- 1e7d5cda7cf1 -->
### 3. CAS 與 ABA 問題

<!--CODE-->

<!-- c7fecb79ede3 -->
### 4. 記憶體屏障與 happens-before

<!--CODE-->

---

<!-- 7b42cfc3d887 -->
## 進階鎖

<!-- 43ad75ae1e3b -->
### 1. 鎖分段（lock striping）

<!--CODE-->

<!-- 0109d218d87f -->
### 2. 分散式鎖演算法

<!-- ea62fc248c8e -->
#### Redlock（Redis）

<!--CODE-->

<!-- 9e2f7feed9a4 -->
#### ZooKeeper 鎖

<!--CODE-->

<!-- 3330c9104d0c -->
### 3. 資料庫層的鎖

<!--CODE-->

<!-- b33ded2159cf -->
### 4. 自旋鎖與 mutex

<!--CODE-->

**比較：**

| 面向 | 自旋鎖 Spin Lock | Mutex |
|--------|-----------|-------|
| 等待方式 | 忙等（CPU 一直轉） | 阻塞（由 OS 做 context switch） |
| 最適合 | 臨界區非常短 | 臨界區較長 |
| CPU 用量 | 競爭時很高 | 等待時很低 |
| 延遲 | 低（不用 context switch） | 較高（有 context switch） |
| 公平性 | 通常不公平 | 可以做到公平 |
| 使用場合 | 底層、即時系統 | 一般用途 |

---

<!-- ce89aa447f4b -->
## 高流量模式

<!-- 8cb636db1b6a -->
### 1. 請求合併

<!--CODE-->

<!-- e8dfb9bd5f23 -->
### 2. 過載卸流（load shedding）

<!--CODE-->

<!-- e00872192dc2 -->
### 3. 艙壁模式（bulkhead）

<!--CODE-->

<!-- 74847c69bd57 -->
### 4. 熱點 key 問題

<!--CODE-->

<!-- 412536e98aaf -->
### 5. 連線池調校

<!--CODE-->

<!-- 0ab095d3404c -->
### 6. 一致性哈希

<!--CODE-->

---

<!-- cd2d1e1782ef -->
## 實際情境

<!-- 3221d00d6172 -->
### 情境 1：高併發計數器（像 YouTube 的觀看次數）

<!--CODE-->

<!-- 355b9296224e -->
### 情境 2：分散式限流器

<!--CODE-->

<!-- b444568c3ac0 -->
### 情境 3：最終一致性與衝突解決

<!--CODE-->

---

<!-- 1062b62a84d4 -->
## 面試常見問題

<!-- e248a49b8ae0 -->
### Q1：執行緒安全的 LRU 快取你會怎麼實作？

<!--CODE-->

<!-- 23845241d7b0 -->
### Q2：說明 deadlock、livelock 與 starvation 的差別。

| 條件 | 死鎖 Deadlock | 活鎖 Livelock | 飢餓 Starvation |
|-----------|----------|----------|------------|
| 定義 | 執行緒互相等待、全部卡住 | 執行緒都在動，但沒有人前進 | 某條執行緒始終拿不到資源 |
| 狀態 | 阻塞 | 執行中 | 等待 |
| 資源使用 | 抓著資源不放 | 一直釋放又重拿 | 始終拿不到 |
| 恢復方式 | 需要外力介入 | 有可能自己解開 | 加上公平性可能解決 |
| 例子 | A 等 B、B 等 A | 兩邊同時退讓又同時重試 | 低優先序的執行緒永遠排不到 |

<!--CODE-->

<!-- 2dda8d06e86e -->
### Q3：怎麼避免資料庫連線池被耗盡？

1. **池的大小要合理**：`connections = (core_count * 2) + effective_spindle_count`
2. **連線逾時**：快速失敗，不要無限等下去
3. **洩漏偵測**：連線被持有太久就記錄下來
4. **statement 逾時**：砍掉跑太久的查詢
5. **斷路器**：資料庫已經撐不住時就別再打了
6. **讀取複本**：把讀取負載分散出去
7. **快取**：減少打資料庫的次數

<!-- 8d884d5737c3 -->
### Q4：悲觀鎖與樂觀鎖該用哪個？

| 情境 | 用悲觀鎖 | 用樂觀鎖 |
|----------|-----------------|----------------|
| 衝突機率 | 高 | 低 |
| 讀寫比 | 寫多 | 讀多 |
| 交易長度 | 短 | 可以很長 |
| 重試成本 | 高 | 低 |
| 例子 | 銀行轉帳 | 更新個人資料 |

<!-- eaee77a4aaad -->
### Q5：ConcurrentHashMap 是怎麼在不鎖整個 map 的情況下做到執行緒安全的？

- **分段鎖**（Java 7）：把 map 切成多個 segment，每段有自己的鎖
- **CAS + synchronized**（Java 8 以後）：讀取無鎖，寫入用細粒度的鎖
- **節點層級的鎖**：只鎖正在被修改的那個 bucket
- **volatile 讀取**：讀取不用鎖也能看到最新的寫入

---

<!-- 0247ba6d4b8d -->
## 總結：快速對照

| 問題 | 解法 |
|---------|----------|
| CPU 密集的平行工作 | Fork/Join 框架 |
| 無鎖資料結構 | 用 AtomicReference 做 CAS |
| ABA 問題 | AtomicStampedReference |
| 降低鎖競爭 | 鎖分段（lock striping） |
| 分散式協調 | ZooKeeper / Redlock |
| 驚群效應 | 請求合併 |
| 系統過載 | 過載卸流 |
| 連鎖失敗 | 艙壁模式 |
| 熱點 key | 本地快取 + key 打散 |
| 節點故障 | 一致性哈希 |
| 並發修改 | 樂觀鎖（version 欄位） |
| 獨占存取 | 悲觀鎖（SELECT FOR UPDATE） |

---

<!-- 10a26c4576da -->
## 參考資料

- [Java Concurrency in Practice - Brian Goetz](https://jcip.net/)
- [The Art of Multiprocessor Programming](https://www.amazon.com/Art-Multiprocessor-Programming-Revised-Reprint/dp/0123973376)
- [Martin Kleppmann - Designing Data-Intensive Applications](https://dataintensive.net/)
- [Redis Distributed Locks (Redlock)](https://redis.io/topics/distlock)
- [HikariCP - Connection Pool](https://github.com/brettwooldridge/HikariCP)
