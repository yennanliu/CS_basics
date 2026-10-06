# 可靠的 Webhook 整合設計（Reliable Webhook Integration）

> **Scope** — 設計「接收端」的 webhook 整合：簽章驗證、去重、快速 ACK、非同步處理、重試與 DLQ、亂序事件與可觀測性；不談一般 HTTP API 的設計，也不談長連線推播。
> **See also**: [`api_design.md`](./api_design.md) — 發送 webhook 的一方該守的規矩（簽章、event id、退避重試）；
> [`llm_tool_idempotency.md`](./llm_tool_idempotency.md) — idempotency key 的產生策略與鎖；
> [`web_long_connections.md`](./web_long_connections.md) — 不用 webhook 時的推播方式（SSE、WebSocket）。

## 目錄

1. [面試開場：先講結論](#1-面試開場先講結論)
2. [整體架構](#2-整體架構)
3. [六個一定要講到的 Concepts](#3-六個一定要講到的-concepts)
4. [常見追問](#4-常見追問)
5. [實務檢查清單](#5-實務檢查清單)

---

## 1. 面試開場：先講結論

**核心假設：webhook 本身是不可靠的。** 它可能 **delay、duplicate、out-of-order，甚至 delivery failure**，
所以不能假設每個 event 只會收到一次 —— 要設計成 **at-least-once delivery + idempotent processing**。

可以直接這樣講：

> 我會把 webhook endpoint 設計成一個**輕量的 ingestion layer**。收到 event 後，先做 authentication（驗證
> webhook signature，確認來源可信），再驗證 payload，並用 event ID 或 idempotency key 做 deduplication。
> 確認是新 event 後，先把它 persist 到 database 或 durable queue，然後**盡快回傳 HTTP 2xx**。
>
> 比較重的 business logic 放到 **asynchronous processing**，避免 request 因為處理太久而 timeout。處理失敗就用
> **exponential backoff** 重試；一直失敗的 event 放進 **dead-letter queue（DLQ）**，之後人工或自動處理。
>
> 最後加上 **observability**：監控 success rate、latency、retry count、duplicate events 和 processing
> failures。這樣 partner 說「某個 webhook 沒收到」時，我們能用 event ID 和 timestamp 追蹤整個 lifecycle。

---

## 2. 整體架構

```text
             Provider（e.g. Uber）
                   |
                   | Webhook Event
                   v
        +----------------------+
        | Partner Webhook API  |
        +----------------------+
                   |
                   v
          Verify Signature ── invalid ──> 401, reject
                   |
                   v
            Validate Event ── malformed ──> 400
                   |
                   v
          Check Event ID
          /              \
      Duplicate          New
         |                 |
         |                 v
         |          Persist Event
         |                 |
         |                 v
         |        Return 2xx（Fast ACK）
         |                 |
         |                 v
         |              Queue
         |                 |
         |                 v
         |          Async Processing
         |                 |
         |          +------+------+
         |          |             |
         |       Success        Failure
         |          |             |
         |          |     Retry（backoff）
         |          |             |
         |          |      DLQ if exhausted
         v          v
   Return 2xx     Done
   & ignore
```

**重複的 event 也要回 2xx。** 回 4xx/5xx 只會讓 provider 繼續重送同一個 event。

---

## 3. 六個一定要講到的 Concepts

### 3-1) Idempotency ⭐⭐⭐⭐⭐

Webhook 可能收到兩次：

```text
event_id = 12345

第一次 → process
第二次 → duplicate → ignore
```

所以不能假設 **one webhook = one delivery**，而要設計成 **at-least-once delivery + idempotent processing**。

**Key Idea**：「先查有沒有處理過，再寫入」有 race condition —— 兩個重送同時到達，兩邊都查到「沒有」。
正確做法是讓資料庫的 **unique constraint** 來判定，一次 insert 就決定誰是第一個：

```python
# webhook_events.event_id 上有 UNIQUE 約束
def ingest(event):
    inserted = db.execute(
        "INSERT INTO webhook_events (event_id, type, payload, status, received_at) "
        "VALUES (%s, %s, %s, 'received', now()) "
        "ON CONFLICT (event_id) DO NOTHING",
        (event["id"], event["type"], json.dumps(event)),
    ).rowcount
    if inserted == 0:
        return "duplicate"      # 已經收過：照樣回 2xx，但不再排入佇列
    queue.publish(event["id"])
    return "accepted"
```

Queue 本身通常也是 at-least-once，所以 **consumer 端同樣要冪等**：處理前檢查 `status`，
或讓 business side effect 本身帶著 `event_id` 做唯一鍵。

### 3-2) Retry ⭐⭐⭐⭐

如果下游暫時掛掉：

```text
Webhook → 500 → Retry → 500 → Retry → 200
```

不要一直立即 retry，而是用 **Exponential Backoff**，最好再加上 **jitter**（隨機抖動）：

```text
1s → 2s → 4s → 8s → 16s → ...（每次再加一點隨機延遲，上限例如 1h）
```

避免下游恢復的瞬間，又被大量同步到達的 retry traffic 壓垮（thundering herd）。
超過重試上限的 event 放進 **DLQ**，並提供 **replay** 工具，修好 bug 後能重新處理。

重試有**兩層**，面試時分開講：

| 層 | 誰重試 | 觸發條件 |
|---|---|---|
| Delivery | Provider | 我們的 endpoint 沒回 2xx 或 timeout |
| Processing | 我們自己的 worker | 已 ACK 的 event 在非同步處理時失敗 |

### 3-3) Fast ACK ⭐⭐⭐⭐⭐

Webhook endpoint **不要收到 event 後才做完所有 business logic**。比較好的方式：

```text
Receive webhook → Validate → Persist / Queue → Return 200 → Async processing
```

因為如果同步處理：

```text
Webhook request → Database → External API → Complex business logic → 20 seconds
```

很容易超過 provider 的 timeout（常見是 5～30 秒），provider 判定失敗又 retry，最後造成 **duplicate processing**。

**關鍵順序**：一定要**先 persist 成功，才回 2xx**。先回 2xx 再寫入的話，寫入失敗時 event 就永遠遺失了，
因為 provider 已經認為送達。

### 3-4) Security ⭐⭐⭐⭐⭐

最重要的是 **Webhook signature verification**，避免任何人直接呼叫 endpoint 偽造 event：

```text
Provider ── payload + signature ──> Partner ── verify signature ──> Accept / Reject
```

```python
import hashlib
import hmac
import time

TOLERANCE_SECONDS = 300

def verify(raw_body: bytes, signature: str, timestamp: str, secret: bytes) -> bool:
    # 拒絕太舊的 request，防止 replay attack
    if abs(time.time() - int(timestamp)) > TOLERANCE_SECONDS:
        return False
    # 簽的是「原始 bytes」，不能用 parse 後再序列化的 JSON
    signed = timestamp.encode() + b"." + raw_body
    expected = hmac.new(secret, signed, hashlib.sha256).hexdigest()
    # 固定時間比較，避免 timing attack
    return hmac.compare_digest(expected, signature)
```

另外可以提：

- **HTTPS/TLS**：傳輸加密
- **Timestamp validation + replay protection**：上面的時間窗口，加上 event ID 去重
- **Secret rotation**：輪替期間同時接受新舊兩把 secret
- **其他驗證**：IP allowlist、mTLS（若 provider 支援）

### 3-5) Out-of-order events ⭐⭐⭐（加分項）

例如：

```text
Event A: ride.created
Event B: ride.completed
```

理想上收到 `A → B`，但實際可能收到 `B → A`。如果照到達順序處理，`ride.created` 會把已完成的行程覆寫回「剛建立」。

常見解法：

| 做法 | 怎麼做 | 適合 |
|---|---|---|
| **版本號 / 時間戳比較** | 每筆資源記住最後套用的 `sequence` 或 `occurred_at`，較舊的 event 直接略過 | provider 有提供單調遞增的版本號 |
| **狀態機只往前走** | 定義合法轉移（`created → accepted → completed`），不允許倒退 | 有明確生命週期的資源 |
| **Thin event + 回查** | event 只當「有變更」的通知，收到後呼叫 provider API 取**最新狀態** | 順序很重要、provider 有查詢 API |
| **依 key 分區** | queue 用 resource ID 當 partition key，同一資源的 event 依序處理 | 只需要同一資源內有序 |

```sql
-- 版本號比較：只有比較新的 event 才會更新
UPDATE rides SET status = :status, version = :version
WHERE ride_id = :ride_id AND version < :version
```

### 3-6) Observability ⭐⭐⭐⭐

要監控的指標：

- webhook **success rate**、**latency**（ACK 時間與處理時間分開看）
- **retry count**、**duplicate events** 比例
- **processing failures**、**DLQ 深度**、queue lag

每個 event 的 lifecycle（`received → queued → processing → done / failed / dlq`）都記錄 **event ID + timestamp**。
partner 回報「某個 webhook 沒收到」時，用 event ID 查一次就知道它是**沒送到、被當成重複、還是處理失敗**。
再搭配 **reconciliation job**：定期呼叫 provider 的 list API 對帳，補回真的漏掉的 event。

---

## 4. 常見追問

**Q：為什麼不直接同步處理，處理完再回 200？**
處理時間不可控，超時會觸發 provider 重送，反而製造更多重複；而且下游一慢，endpoint 的連線就被佔滿。

**Q：去重紀錄要保留多久？**
至少要涵蓋 provider 的**最長重試期間**（常見 3 天左右）。實務上 event 表本身就是稽核紀錄，保留更久，
或用 Redis `SET NX` + TTL 做熱路徑，再由 DB unique index 當最後一道防線。

**Q：provider 的 event 沒有 ID 怎麼辦？**
用 payload 中的業務欄位組成 key（例如 `resource_id + type + occurred_at`），或對原始 body 做 hash。

**Q：endpoint 掛了一小時，事件會不會掉？**
provider 會在它的重試期間內重送；超過的部分靠 **reconciliation job** 對帳補回。

**Q：Exactly-once 做得到嗎？**
網路上的 delivery 做不到；能做到的是 **at-least-once delivery + idempotent processing = effectively-once 的結果**。

---

## 5. 實務檢查清單

```text
[ ] 用 raw body 驗證 HMAC signature，並檢查 timestamp 時間窗口
[ ] payload schema 驗證，壞資料回 400
[ ] event_id 上的 UNIQUE 約束做去重；重複的 event 仍回 2xx
[ ] 先 persist 再回 2xx；endpoint 本身不做重的 business logic
[ ] 非同步 worker 冪等；exponential backoff + jitter；上限後進 DLQ
[ ] DLQ 有 replay 工具
[ ] 亂序處理：版本號比較 / 狀態機 / 回查最新狀態
[ ] 指標：success rate、latency、retry、duplicate、DLQ 深度、queue lag
[ ] 每個 event 的 lifecycle 可用 event ID 追蹤；定期 reconciliation
[ ] Secret rotation 期間同時接受新舊 secret
```
