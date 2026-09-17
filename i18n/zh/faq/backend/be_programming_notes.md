<!-- f6886ea69516 -->
# 後端工程師的後端程式筆記

> 重點：高流量、並發、鎖的模式，以及實際怎麼寫

<!-- 847d5897091d -->
## 目錄

1. [高流量模式](#high-traffic-patterns)
   - [幕等的請求處理](#1-idempotent-request-processing)
   - [限流](#2-rate-limiting)
   - [斷路器](#3-circuit-breaker)
   - [用分散式鎖做請求去重](#4-request-deduplication-with-distributed-lock)
2. [並發模式](#concurrency-patterns)
   - [ConcurrentHashMap 的操作](#concurrenthashmap-operations)
   - [原子操作](#atomic-operations)
   - [鎖的策略](#lock-strategies)
3. [API 設計模式](#api-design-patterns)
4. [後端常用的 Java 設計模式](#java-design-patterns-for-backend)

---

<!-- ce89aa447f4b -->
## 高流量模式

<!-- 8b5ce0f97991 -->
### 1. 幕等的請求處理

**問題**：設計一個機制，在可設定的時間窗內，避免同一個 `requestId` 的請求被重複處理。

<!-- 99b0a8962e7e -->
#### 最初的設計（V0 — 面試白板版）

<!--CODE-->

**V0 的問題：**
1. `HashMap` 不是執行緒安全的
2. 先檢查再動作（check-then-act）不是原子的（有競態）
3. 過期的項目沒有清理機制
4. 鎖的逾時沒有實作

---

<!-- 2816d76b541f -->
#### 解法 V1：ConcurrentHashMap 加上原子檢查

<!--CODE-->

**有改善，但問題還在：**
- `get()` 之後再 `put()` 不是原子的 —— 競態還在！

---

<!-- 44ea800bd476 -->
#### 解法 V2：用 `compute()` 拿到真正的原子性

<!--CODE-->

---

<!-- bd195239a754 -->
#### 解法 V3：可上線的版本，附清理機制

<!--CODE-->

---

<!-- 4c4fa94ed4f5 -->
#### 解法 V4：用 Redis 的分散式版本

<!--CODE-->

---

<!-- e55ce2107220 -->
### 幕等處理的重點整理

| 面向 | 單一節點 | 分散式 |
|--------|-----------------|-------------|
| 儲存 | `ConcurrentHashMap` | Redis / 資料庫 |
| 原子性 | `compute()` / `computeIfAbsent()` | 帶 TTL 的 `SETNX` |
| 清理 | `ScheduledExecutorService` | Redis TTL 自動過期 |
| 失敗處理 | 出錯時從 map 移除 | 出錯時刪掉該 key |

---

<!-- e96afe8483bc -->
### 2. 限流

<!-- 6a6008df5bc1 -->
#### Token Bucket 實作

<!--CODE-->

<!-- 053219dc40b1 -->
#### 滑動視窗限流器

<!--CODE-->

---

<!-- e4d8f3675bb1 -->
### 3. 斷路器（Circuit Breaker）

<!--CODE-->

---

<!-- 0000d75d384c -->
### 4. 用分散式鎖做請求去重

<!--CODE-->

---

<!-- 27b5fc1dcd65 -->
## 並發模式

<!-- dcf78b505b75 -->
### ConcurrentHashMap 的操作

| 方法 | 行為 | 適用場景 |
|--------|----------|----------|
| `putIfAbsent(k, v)` | key 不存在才插入 | 單純去重 |
| `computeIfAbsent(k, func)` | 不存在時才延遲計算值 | 延遲初始化 |
| `compute(k, func)` | 一律計算（原子的讀-改-寫） | 有條件的更新 |
| `merge(k, v, func)` | 和既有值合併 | 計數、彙總 |

<!--CODE-->

---

<!-- b932d703859c -->
### 原子操作

<!--CODE-->

---

<!-- c92489356b75 -->
### 鎖的策略

<!-- 9b5e2ce112e4 -->
#### 1. 帶逾時的 ReentrantLock

<!--CODE-->

<!-- fcdd658df495 -->
#### 2. 讀多寫少時用 ReadWriteLock

<!--CODE-->

<!-- f638ee35381e -->
#### 3. 用 StampedLock 做樂觀讀

<!--CODE-->

---

<!-- 69b4c3f5e5da -->
## API 設計模式

<!-- 91f0fbf1ec80 -->
### 幕等鍵（Idempotency Key）模式

<!--CODE-->

<!-- eeefb4169ca1 -->
### 指數退避重試

<!--CODE-->

---

<!-- 64d924300bcf -->
## 後端常用的 Java 設計模式

<!-- fcf66ebddb3a -->
### 1. Singleton（執行緒安全）

<!--CODE-->

<!-- 2caf0cc16411 -->
### 2. 用 BlockingQueue 做生產者-消費者

<!--CODE-->

<!-- 4b2821c9eb41 -->
### 3. 物件池（Object Pool）模式

<!--CODE-->

---

<!-- 1062b62a84d4 -->
## 面試常見問題

<!-- 757e38a5c547 -->
### Q1：為什麼要用 `ConcurrentHashMap.compute()`，而不是 get 加 put？

**答**：`get()` 之後接 `put()` 不是原子的。在這兩個操作之間，另一條執行緒就可能改動這個 map。`compute()` 把整個讀-改-寫當成一個原子操作完成。

<!-- 6ecfad37c0db -->
### Q2：`synchronized` 和 `ReentrantLock` 差在哪？

| 特性 | synchronized | ReentrantLock |
|---------|--------------|---------------|
| 鎖可以逾時 | 不行 | 可以（`tryLock`） |
| 可被中斷 | 不行 | 可以 |
| 公平性 | 沒有 | 可設定 |
| 多個條件變數 | 不行 | 可以 |
| 試著拿鎖 | 不行 | 可以 |

<!-- 5da331454f94 -->
### Q3：什麼時候該用 `volatile`？

用 `volatile` 的時機：
- 變數會被多條執行緒存取
- 只有單純的讀或寫（沒有讀-改-寫）
- 不需要複合操作

不夠用的情況：`counter++`（讀-改-寫要用 AtomicInteger）

<!-- 318095eab5de -->
### Q4：怎麼避免死鎖？

1. **鎖的順序**：永遠用同樣的順序取鎖
2. **鎖的逾時**：用帶 timeout 的 `tryLock()`
3. **避免嵌套鎖**：把鎖的範圍縮到最小
4. **用更高階的元件**：`ConcurrentHashMap`、`BlockingQueue`

---

<!-- 3789b5d86abf -->
## 總結：怎麼挑對工具

| 情境 | 解法 |
|----------|----------|
| 單純的計數器 | `AtomicLong` 或 `LongAdder` |
| 有條件的 map 更新 | `ConcurrentHashMap.compute()` |
| 讀多的快取 | `ReadWriteLock` 或 `StampedLock` |
| 請求去重（單節點） | `ConcurrentHashMap` + TTL 清理 |
| 請求去重（分散式） | 帶 TTL 的 Redis `SETNX` |
| 限流 | Token Bucket／滑動視窗 |
| 容錯 | 斷路器 |
| 生產者-消費者 | `BlockingQueue` |

---

<!-- eb2a7fd5a0c7 -->
## 參考資料

- [Java Concurrency Guide](https://javaguide.cn/java/concurrent/)
- [Redis Distributed Locks](https://redis.io/topics/distlock)
- [Martin Fowler - Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)
