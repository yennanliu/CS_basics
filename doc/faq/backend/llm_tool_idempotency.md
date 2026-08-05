# LLM Tool Call 冪等性設計（Idempotency for AI Agent Tools）

> 適用場景：AI Agent / LLM Orchestrator 呼叫具有副作用的外部工具（退款、發信、建立訂單、扣點數）時，如何防止重複執行。

## 目錄

1. [什麼是冪等性](#1-什麼是冪等性)
2. [為什麼 LLM 工具特別需要冪等性](#2-為什麼-llm-工具特別需要冪等性)
3. [基礎實作：Check-Act-Record](#3-基礎實作check-act-record)
4. [產生 idempotency_key 的 5 種策略](#4-產生-idempotency_key-的-5-種策略)
5. [策略比較總結](#5-策略比較總結)
6. [失敗處理：Two-Phase Lock](#6-失敗處理two-phase-lock)
7. [如何「刻意」重新執行](#7-如何刻意重新執行)
8. [常見陷阱](#8-常見陷阱)
9. [實務檢查清單](#9-實務檢查清單)
10. [References](#10-references)

---

## 1. 什麼是冪等性

**冪等性（Idempotency）確保「無論同一個操作被執行一次還是多次，其最終的系統狀態與結果都是相同的」。**

```text
f(x) = f(f(x)) = f(f(f(x)))
```

| HTTP Method | 天然冪等？ | 說明 |
|-------------|-----------|------|
| `GET` | ✅ | 純讀取 |
| `PUT` | ✅ | 覆蓋寫入，寫幾次結果一樣 |
| `DELETE` | ✅ | 刪除後再刪除，狀態仍是「已刪除」 |
| `POST` | ❌ | **每次都建立新資源 → 需要 idempotency_key 保護** |

LLM Tool Call 大多數帶副作用的工具（`create_order`、`request_refund`、`send_email`）本質上就是 `POST`，**必須自行實作冪等性保護**。

---

## 2. 為什麼 LLM 工具特別需要冪等性

傳統 API 的重複請求來源大多是「網路重試」，但在 LLM Agent 架構中，重複來源多了好幾層：

```text
┌──────────┐   ①使用者狂點送出   ┌──────────────┐
│ Frontend │ ──────────────────▶ │ AI           │
└──────────┘                     │ Orchestrator │
                                 └──────┬───────┘
                    ②Orchestrator       │  ③LLM 幻覺 / 重複 tool_call
                      網路超時重試       │     (同一輪吐兩次相同請求)
                                        ▼
                                 ┌──────────────┐
                                 │  Tool / API  │  ← 副作用發生處
                                 └──────────────┘
```

1. **網路超時與重試**：Orchestrator 呼叫工具時遇到網路波動，沒收到回應而觸發自動重試。
2. **LLM 幻覺或邏輯錯誤**：模型在同一次生成中，因為理解錯誤，連續輸出兩次相同的 Tool Call。
3. **使用者重複點擊**：前端使用者覺得 AI 回應太慢，重複按下送出，導致整個對話流程被觸發兩次。
4. **Agent Loop 重跑**：ReAct / Plan-Execute 類型的 Agent 在下一輪思考時，誤判上一步「沒成功」而重跑。
5. **多 Agent 併發**：Supervisor 同時派發任務給多個 sub-agent，剛好都呼叫到同一個工具。

> 如果沒有這層保護，當執行的是「退款」、「寄信」或「扣除點數」這類帶副作用（Side Effects）的操作時，就會發生**重複退款**或**狂發垃圾信**的災難。

---

## 3. 基礎實作：Check-Act-Record

最直觀的做法是用 Redis 做「檢查 → 執行 → 紀錄」三步驟。

`idempotency_key` 由 AI Orchestrator 生成（通常為 `{session_id}:{tool_name}:{call_index}`），確保同一次 LLM 請求中的工具呼叫具有唯一識別。

```python
# python
# IDEA: Check-Act-Record —— 先查快取，沒命中才執行，執行完寫回快取
async def request_refund(order_id: str, amount: float, idempotency_key: str):
    # 1. 查詢 idempotency_key 是否已執行過
    cached = await redis.get(f"idem:{idempotency_key}")
    if cached:
        return json.loads(cached)  # 回傳上次結果，不重複執行

    # 2. 執行退款
    result = await payment_api.refund(order_id, amount)

    # 3. 儲存結果，TTL 24 小時（覆蓋 LLM 可能重試的時間窗）
    await redis.setex(f"idem:{idempotency_key}", 86400, json.dumps(result))
    return result
```

### 這版本的兩個破口

| 問題 | 說明 |
|------|------|
| **無法防併發** | 兩個請求在 100ms 內同時進來，都會在步驟 1 miss，然後**都執行退款** |
| **失敗會被吃掉語意** | `payment_api.refund` 拋 Exception 時不寫快取（行為正確），但沒有鎖，重試仍可能併發 |

→ 正式環境請用 [第 6 節的 Two-Phase Lock](#6-失敗處理two-phase-lock)。

---

## 4. 產生 idempotency_key 的 5 種策略

### 4-1) 對話索引（原始範例）

* **格式**：`{session_id}:{tool_name}:{call_index}`
* **優點**：與 Agent 執行步驟一一對應，方便追蹤/稽核 trace。
* **缺點**：需要自行維護 `call_index` 計數器；若 Agent 重跑整輪，`call_index` 歸零會誤擋。

---

### 4-2) 使用 LLM 供應商原生的 Tool Call ID ⭐ 強烈推薦

OpenAI、Anthropic 等主流模型在回傳工具呼叫請求時，都會自帶一個唯一的 `tool_call_id`（例如：`call_abc123xyz`、`toolu_01A09q90qw`）。

* **格式**：直接使用該 ID，例如 `idem:call_abc123xyz`
* **優點**：最簡單、最直觀。同一次 LLM 生成中的重複執行，這個 ID 會是固定的；天然與模型綁定，不需要自己維護 `call_index`。
* **缺點**：如果重試發生在「**重新請 LLM 生成回答**」的階段，模型會產生一個全新的 `tool_call_id`，此機制就會失效。

```python
# python
# Anthropic Messages API：從 tool_use block 取出原生 id
for block in response.content:
    if block.type == "tool_use":
        idem_key = f"idem:{block.id}"          # e.g. idem:toolu_01A09q90qw
        result = await dispatch(block.name, block.input, idem_key)
```

---

### 4-3) 業務邏輯唯一鍵（Business Logic Key）

不依賴 AI 的狀態，而是根據「業務本身」的規則來定義唯一性。

* **格式**：`{tool_name}:{業務唯一識別碼}`，例如 `refund:{order_id}`
* **優點**：**極度安全**。一筆訂單在業務邏輯上通常只能「全額退款一次」。無論 LLM 怎麼發瘋，只要這筆訂單退過款，Redis 就能擋下第二次。
* **缺點**：**缺乏彈性**。如果業務允許「多次部分退款」，這個 Key 會誤擋正常的第二次退款請求 → 改用 [4-6 交易單號](#7-如何刻意重新執行)。

---

### 4-4) 參數雜湊（Payload Hashing）

將 LLM 傳入的所有參數（Arguments）進行 Hash 運算，並結合 Session ID。

* **格式**：`{session_id}:{tool_name}:{sha256(canonical_json(kwargs))}`
* **優點**：精準判斷「完全相同的操作」。同樣的退款金額 + 訂單號 → Hash 相同被攔截；LLM 第二次決定退不同金額 → Hash 改變，放行。
* **缺點**：參數若含時間戳記（Timestamp）或隨機字串，Hash 每次都變，**防護完全失效**。

```python
# python
import hashlib, json

def payload_key(session_id: str, tool_name: str, kwargs: dict) -> str:
    # sort_keys 確保 dict 順序不影響 hash；剔除易變欄位
    volatile = {"timestamp", "request_time", "nonce", "trace_id"}
    stable = {k: v for k, v in kwargs.items() if k not in volatile}
    digest = hashlib.sha256(
        json.dumps(stable, sort_keys=True, ensure_ascii=False).encode()
    ).hexdigest()[:16]
    return f"idem:{session_id}:{tool_name}:{digest}"
```

> ⚠️ 一定要 `sort_keys=True`，否則 `{"a":1,"b":2}` 與 `{"b":2,"a":1}` 會算出不同 hash。

---

### 4-5) 前端請求 ID（Client Request ID）

將生成 Key 的責任推到最源頭（前端 Web / App）。使用者送出訊息時，前端生成一個 UUID 並一路傳遞到 Tool Call 中。

* **格式**：`{request_uuid}:{tool_name}`
* **優點**：能完美解決「使用者狂按送出按鈕」導致的重複執行問題。
* **缺點**：需要前端配合修改 API 規格，**架構侵入性較高**（跨團隊溝通成本）。

---

## 5. 策略比較總結

| 策略 | 適用場景 | 防禦範圍 | 實作難度 |
| --- | --- | --- | --- |
| **對話索引（原始範例）** | 適合需要嚴格記錄 Agent 執行步驟的系統 | 攔截 LLM 內部重試 | 中 |
| **原生 Tool Call ID** | 使用 OpenAI / Anthropic 等標準工具呼叫 API | 攔截 Orchestrator 網路重試 | 低 |
| **業務邏輯唯一鍵** | 絕對不能重複執行的關鍵財務操作（如：註銷帳號） | 攔截任何形式的重複 | 低 |
| **參數雜湊（Hash）** | 同一對話中，允許對不同參數執行多次相同工具 | 攔截相同參數的幻覺生成 | 高 |
| **前端請求 ID** | 網頁 / App 使用者可能因網路延遲而重複點擊 | 攔截使用者端的重複觸發 | 高（需跨團隊） |

### 實務建議：分層防禦（Defense in Depth）

單一策略都有破口，正式系統通常**疊兩層**：

```text
第一層（廣義去重）：原生 tool_call_id  → 擋掉 Orchestrator 網路重試
第二層（業務兜底）：refund:{order_id}   → 擋掉「LLM 重新生成」造成的新 tool_call_id
```

---

## 6. 失敗處理：Two-Phase Lock

### ❌ 錯誤做法：把時間戳記加進 Key

**不建議將時間戳記直接加在 Key 後面。**

如果改用 `{tool_name}:{業務唯一識別碼}-{timestamp}`，因為每次呼叫（包含網路波動造成的 100ms 自動重試）的時間戳記都不一樣，Redis 的快取**永遠不會命中** → **冪等性保護完全失效**。

### ✅ 正確做法：分散式鎖 + 失敗釋放

如果 API 執行失敗（網路斷線、第三方金流 500），**絕對不能把失敗結果當成功快取起來**。

只有成功的結果才存入 24 小時快取；一旦拋出 Exception，就立即刪除鎖，允許下一次重試。

```python
# python
# IDEA: Two-Phase Lock —— 成功才寫冪等快取，失敗釋放鎖允許重試
async def request_refund(order_id: str, amount: float):
    idem_key = f"idem:refund:{order_id}"
    lock_key = f"lock:refund:{order_id}"

    # 1. 檢查是否已有「成功執行」的結果
    cached = await redis.get(idem_key)
    if cached:
        return json.loads(cached)

    # 2. 併發防護：搶占執行鎖（防止同一秒內多個請求同時進來）
    acquired = await redis.set(lock_key, "IN_PROGRESS", nx=True, ex=30)
    if not acquired:
        raise Exception("操作正在處理中，請勿重複送出")

    try:
        # 3. 執行業務邏輯
        result = await payment_api.refund(order_id, amount)

        # 4. 成功：存入 24HR 冪等快取
        await redis.setex(idem_key, 86400, json.dumps(result))
        return result

    except Exception as e:
        # 5. 失敗：不做冪等快取，交由 finally 釋放鎖，允許後續重試
        raise e
    finally:
        await redis.delete(lock_key)
```

### 狀態流轉圖

```text
        ┌─────────────┐
        │  請求進入    │
        └──────┬──────┘
               ▼
     ┌───────────────────┐   HIT    ┌──────────────────┐
     │ GET idem_key      │─────────▶│ 回傳上次成功結果  │
     └─────────┬─────────┘          └──────────────────┘
               │ MISS
               ▼
     ┌───────────────────┐   FAIL   ┌──────────────────┐
     │ SET lock NX EX 30 │─────────▶│ 429 處理中，請稍候│
     └─────────┬─────────┘          └──────────────────┘
               │ OK
               ▼
     ┌───────────────────┐
     │ 呼叫 payment_api  │
     └────┬─────────┬────┘
   成功    │         │  失敗
          ▼         ▼
  ┌──────────────┐ ┌──────────────────┐
  │SETEX idem 24H│ │ 不寫快取，拋例外  │
  └──────┬───────┘ └────────┬─────────┘
         └────────┬─────────┘
                  ▼
          ┌──────────────┐
          │ DEL lock_key │  ← finally 一定執行
          └──────────────┘
```

### 三個關鍵設計點

| 設計點 | 原因 |
|--------|------|
| `nx=True` | Redis `SET NX` 是**原子操作**，等同 CAS，才能真正防併發 |
| `ex=30`（鎖 TTL） | 服務中途 crash 時，鎖會自動過期，不會永久卡死 |
| 冪等快取 TTL = 86400 | 必須**大於**所有可能的重試時間窗（Orchestrator retry + 使用者重送） |

> ⚠️ **鎖 TTL 必須大於業務執行時間**。若退款 API 平均 5s、P99 25s，`ex=30` 才安全；否則鎖提前過期 → 第二個請求進來 → 併發雙扣。

---

## 7. 如何「刻意」重新執行

如果業務邏輯改變，或前一次失敗後確定要人工干預重新觸發，有 3 種標準解法：

### 7-1) 改用「嘗試次數（Attempt / Version）」而非時間戳記

由 Orchestrator 或人工明確控制版本號，而不是自動帶入隨機時間。

* **Key 格式**：`idem:refund:{order_id}:v2` 或 `idem:refund:{order_id}:attempt_2`
* **優勢**：在 `attempt_2` 的生命週期內依然享有自動重試防護，同時又能實現第二次執行。

### 7-2) 快取主動失效（Cache Eviction / Force Retry）

在管理後台（或 Orchestrator 邏輯）提供「清除冪等鎖」的選項，或呼叫時傳入 `force_retry=True`：

```python
# python
if force_retry:
    await redis.delete(f"idem:refund:{order_id}")
```

> 🔒 `force_retry` **絕不可以開放給 LLM 自行決定**。它只能來自人工後台或明確的系統流程 —— 否則模型「覺得剛剛沒成功」就自己帶 `force_retry=True`，冪等性形同虛設。

### 7-3) 綁定「業務交易單號」而非「主實體 ID」

如果這是一筆**新的退款意圖**，應該為這筆「退款請求」生成 unique 的 `refund_request_id`，而不是拿 `order_id` 當 Key。

* **Key 格式**：`idem:refund:{refund_request_id}`
* **效果**：同一個 `order_id` 可產生多次不同的 `refund_request_id`（適合多次部分退款），而每一次的 `refund_request_id` 自身依然具備冪等性。

```text
order_1001
 ├── refund_request_A (退 $300) → idem:refund:req_A   ← 各自冪等
 ├── refund_request_B (退 $200) → idem:refund:req_B
 └── refund_request_C (退 $100) → idem:refund:req_C
```

---

## 8. 常見陷阱

| 陷阱 | 症狀 | 解法 |
|------|------|------|
| **Key 帶時間戳記** | 快取永不命中，等於沒做冪等 | 用固定業務鍵或 attempt 版本號 |
| **只有 GET/SET，沒有鎖** | 同一秒併發雙扣 | `SET NX EX` 分散式鎖 |
| **把失敗結果也快取** | 暫時性錯誤被鎖死 24 小時，永遠無法重試 | 只快取成功結果 |
| **TTL 太短** | 使用者 5 分鐘後重送 → 重複執行 | TTL > 最大重試窗（建議 24H） |
| **鎖 TTL < 業務耗時** | 鎖提前過期造成併發 | 鎖 TTL > P99 latency，或用 watchdog 續期 |
| **Redis 單點掛掉** | 冪等層全失效，副作用直接穿透 | DB unique index 兜底（見下） |
| **只擋不回結果** | 第二次呼叫回 `duplicate` error，LLM 誤判失敗又重試 | **回傳與第一次相同的成功結果**，讓 LLM 認為完成 |
| **參數不同卻共用 Key** | 退 $300 被當成退 $100 的重複 | Key 納入 payload hash，或比對參數不同時報錯 |

### 最後一道防線：資料庫 Unique Index

Redis 是「效能層」，不是「正確性層」。**真正的正確性應由 DB 保證**：

```sql
-- 用 unique constraint 讓 DB 幫你擋掉重複寫入
CREATE TABLE refunds (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id          VARCHAR(64) NOT NULL,
    idempotency_key   VARCHAR(255) NOT NULL,
    amount            DECIMAL(10,2) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    result_payload    JSON,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_idem (idempotency_key)
);
```

```python
# python
# Redis miss 時仍可能併發 → 靠 DB unique index 收尾
try:
    await db.insert_refund(idempotency_key=key, order_id=order_id, amount=amount)
except UniqueViolation:
    return await db.get_refund_result(key)   # 已存在 → 回傳既有結果
```

> 對照「[Overbooking Prevention](./overbooking_prevention.md)」中的策略：Redis 原子操作擋流量、DB constraint 保正確性，是同一套分層思路。

---

## 9. 實務檢查清單

設計一個帶副作用的 LLM Tool 時，逐項確認：

- [ ] 這個工具有副作用嗎？（會改變外部狀態 / 花錢 / 發通知）→ 有就必須做冪等
- [ ] `idempotency_key` 由誰生成？（Orchestrator / 前端 / 模型原生 ID）
- [ ] Key 裡**沒有**時間戳記或隨機值
- [ ] 有用 `SET NX EX` 做併發鎖
- [ ] 鎖 TTL > 業務 P99 執行時間
- [ ] **只快取成功結果**，失敗時釋放鎖
- [ ] 快取 TTL ≥ 24H（涵蓋所有重試窗）
- [ ] 重複呼叫時**回傳原本的成功結果**，而不是丟 error
- [ ] DB 有 unique index 當最後防線
- [ ] `force_retry` 只允許人工 / 系統觸發，**不暴露給 LLM**
- [ ] Tool schema 的 description 有寫明「此操作不可逆」，降低模型亂呼叫的機率
- [ ] 有記錄 audit log（誰在哪個 session 觸發了哪個副作用）

---

## 10. References

- [AI Eng from Scratch — Phase 13 Part 1: MCP & APIs](https://yennj12.js.org/yennj12_blog_V4/posts/ai-eng-from-scratch-phase13-part1-mcp-apis-zh/)
- [Stripe — Idempotent Requests](https://docs.stripe.com/api/idempotent_requests)
- [Anthropic — Tool use with Claude](https://docs.claude.com/en/docs/agents-and-tools/tool-use/overview)
- [Redis — Distributed Locks with Redlock](https://redis.io/docs/latest/develop/use-cases/patterns/distributed-locks/)
- 相關文件：[Overbooking Prevention](./overbooking_prevention.md)、[Redis in Backend](../redis/redis_backend.md)
