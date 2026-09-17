<!-- f65cf1ed2f00 -->
# LLM Tool Call 冪等性設計（Idempotency for AI Agent Tools）

> 適用場景：AI Agent / LLM Orchestrator 呼叫具有副作用的外部工具（退款、發信、建立訂單、扣點數）時，如何防止重複執行。

<!-- 5e214b1785d8 -->
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

<!-- 1f96f955b925 -->
## 1. 什麼是冪等性

**冪等性（Idempotency）確保「無論同一個操作被執行一次還是多次，其最終的系統狀態與結果都是相同的」。**

<!--CODE-->

| HTTP Method | 天然冪等？ | 說明 |
|-------------|-----------|------|
| `GET` | ✅ | 純讀取 |
| `PUT` | ✅ | 覆蓋寫入，寫幾次結果一樣 |
| `DELETE` | ✅ | 刪除後再刪除，狀態仍是「已刪除」 |
| `POST` | ❌ | **每次都建立新資源 → 需要 idempotency_key 保護** |

LLM Tool Call 大多數帶副作用的工具（`create_order`、`request_refund`、`send_email`）本質上就是 `POST`，**必須自行實作冪等性保護**。

---

<!-- 262021846505 -->
## 2. 為什麼 LLM 工具特別需要冪等性

傳統 API 的重複請求來源大多是「網路重試」，但在 LLM Agent 架構中，重複來源多了好幾層：

<!--CODE-->

1. **網路超時與重試**：Orchestrator 呼叫工具時遇到網路波動，沒收到回應而觸發自動重試。
2. **LLM 幻覺或邏輯錯誤**：模型在同一次生成中，因為理解錯誤，連續輸出兩次相同的 Tool Call。
3. **使用者重複點擊**：前端使用者覺得 AI 回應太慢，重複按下送出，導致整個對話流程被觸發兩次。
4. **Agent Loop 重跑**：ReAct / Plan-Execute 類型的 Agent 在下一輪思考時，誤判上一步「沒成功」而重跑。
5. **多 Agent 併發**：Supervisor 同時派發任務給多個 sub-agent，剛好都呼叫到同一個工具。

> 如果沒有這層保護，當執行的是「退款」、「寄信」或「扣除點數」這類帶副作用（Side Effects）的操作時，就會發生**重複退款**或**狂發垃圾信**的災難。

---

<!-- bf0a46e8f3c4 -->
## 3. 基礎實作：Check-Act-Record

最直觀的做法是用 Redis 做「檢查 → 執行 → 紀錄」三步驟。

`idempotency_key` 由 AI Orchestrator 生成（通常為 `{session_id}:{tool_name}:{call_index}`），確保同一次 LLM 請求中的工具呼叫具有唯一識別。

<!--CODE-->

<!-- 94382022b4f8 -->
### 這版本的兩個破口

| 問題 | 說明 |
|------|------|
| **無法防併發** | 兩個請求在 100ms 內同時進來，都會在步驟 1 miss，然後**都執行退款** |
| **失敗會被吃掉語意** | `payment_api.refund` 拋 Exception 時不寫快取（行為正確），但沒有鎖，重試仍可能併發 |

→ 正式環境請用 [第 6 節的 Two-Phase Lock](#6-失敗處理two-phase-lock)。

---

<!-- 716c7aa6b8df -->
## 4. 產生 idempotency_key 的 5 種策略

<!-- d3e5a5d45d7f -->
### 4-1) 對話索引（原始範例）

* **格式**：`{session_id}:{tool_name}:{call_index}`
* **優點**：與 Agent 執行步驟一一對應，方便追蹤/稽核 trace。
* **缺點**：需要自行維護 `call_index` 計數器；若 Agent 重跑整輪，`call_index` 歸零會誤擋。

---

<!-- 2d5392a4afad -->
### 4-2) 使用 LLM 供應商原生的 Tool Call ID ⭐ 強烈推薦

OpenAI、Anthropic 等主流模型在回傳工具呼叫請求時，都會自帶一個唯一的 `tool_call_id`（例如：`call_abc123xyz`、`toolu_01A09q90qw`）。

* **格式**：直接使用該 ID，例如 `idem:call_abc123xyz`
* **優點**：最簡單、最直觀。同一次 LLM 生成中的重複執行，這個 ID 會是固定的；天然與模型綁定，不需要自己維護 `call_index`。
* **缺點**：如果重試發生在「**重新請 LLM 生成回答**」的階段，模型會產生一個全新的 `tool_call_id`，此機制就會失效。

<!--CODE-->

---

<!-- 0d1fdb03667d -->
### 4-3) 業務邏輯唯一鍵（Business Logic Key）

不依賴 AI 的狀態，而是根據「業務本身」的規則來定義唯一性。

* **格式**：`{tool_name}:{業務唯一識別碼}`，例如 `refund:{order_id}`
* **優點**：**極度安全**。一筆訂單在業務邏輯上通常只能「全額退款一次」。無論 LLM 怎麼發瘋，只要這筆訂單退過款，Redis 就能擋下第二次。
* **缺點**：**缺乏彈性**。如果業務允許「多次部分退款」，這個 Key 會誤擋正常的第二次退款請求 → 改用 [4-6 交易單號](#7-如何刻意重新執行)。

---

<!-- c72ef1f3d82a -->
### 4-4) 參數雜湊（Payload Hashing）

將 LLM 傳入的所有參數（Arguments）進行 Hash 運算，並結合 Session ID。

* **格式**：`{session_id}:{tool_name}:{sha256(canonical_json(kwargs))}`
* **優點**：精準判斷「完全相同的操作」。同樣的退款金額 + 訂單號 → Hash 相同被攔截；LLM 第二次決定退不同金額 → Hash 改變，放行。
* **缺點**：參數若含時間戳記（Timestamp）或隨機字串，Hash 每次都變，**防護完全失效**。

<!--CODE-->

> ⚠️ 一定要 `sort_keys=True`，否則 `{"a":1,"b":2}` 與 `{"b":2,"a":1}` 會算出不同 hash。

---

<!-- 9c6d41eab1ef -->
### 4-5) 前端請求 ID（Client Request ID）

將生成 Key 的責任推到最源頭（前端 Web / App）。使用者送出訊息時，前端生成一個 UUID 並一路傳遞到 Tool Call 中。

* **格式**：`{request_uuid}:{tool_name}`
* **優點**：能完美解決「使用者狂按送出按鈕」導致的重複執行問題。
* **缺點**：需要前端配合修改 API 規格，**架構侵入性較高**（跨團隊溝通成本）。

---

<!-- 1d1cd1aad5b5 -->
## 5. 策略比較總結

| 策略 | 適用場景 | 防禦範圍 | 實作難度 |
| --- | --- | --- | --- |
| **對話索引（原始範例）** | 適合需要嚴格記錄 Agent 執行步驟的系統 | 攔截 LLM 內部重試 | 中 |
| **原生 Tool Call ID** | 使用 OpenAI / Anthropic 等標準工具呼叫 API | 攔截 Orchestrator 網路重試 | 低 |
| **業務邏輯唯一鍵** | 絕對不能重複執行的關鍵財務操作（如：註銷帳號） | 攔截任何形式的重複 | 低 |
| **參數雜湊（Hash）** | 同一對話中，允許對不同參數執行多次相同工具 | 攔截相同參數的幻覺生成 | 高 |
| **前端請求 ID** | 網頁 / App 使用者可能因網路延遲而重複點擊 | 攔截使用者端的重複觸發 | 高（需跨團隊） |

<!-- a9abd656e792 -->
### 實務建議：分層防禦（Defense in Depth）

單一策略都有破口，正式系統通常**疊兩層**：

<!--CODE-->

---

<!-- df477e01f611 -->
## 6. 失敗處理：Two-Phase Lock

<!-- 2800396b3852 -->
### ❌ 錯誤做法：把時間戳記加進 Key

**不建議將時間戳記直接加在 Key 後面。**

如果改用 `{tool_name}:{業務唯一識別碼}-{timestamp}`，因為每次呼叫（包含網路波動造成的 100ms 自動重試）的時間戳記都不一樣，Redis 的快取**永遠不會命中** → **冪等性保護完全失效**。

<!-- 60a4b496af4c -->
### ✅ 正確做法：分散式鎖 + 失敗釋放

如果 API 執行失敗（網路斷線、第三方金流 500），**絕對不能把失敗結果當成功快取起來**。

只有成功的結果才存入 24 小時快取。但**失敗要分成兩種**，不能一律釋放鎖：

- **確定失敗**（金流明確回 4xx／業務拒絕）：沒有副作用發生，釋放鎖讓重試進來是對的。
- **結果未定**（timeout、連線中斷、5xx）：對方**可能已經扣款成功**，只是回應沒回來。這時把鎖刪掉，下一次重試就會變成**第二次退款**。正確做法是留下 `IN_PROGRESS` 的持久狀態、帶著**同一個下游 idempotency key** 重送（讓金流端自己去重），或先去對帳查詢真實結果，確認之後才放行。

<!--CODE-->

<!-- b2bc6da2adc4 -->
### 狀態流轉圖

<!--CODE-->

<!-- c60f987a6ef4 -->
### 三個關鍵設計點

| 設計點 | 原因 |
|--------|------|
| `nx=True` | Redis `SET NX` 是**原子操作**，等同 CAS，才能真正防併發 |
| `ex=30`（鎖 TTL） | 服務中途 crash 時，鎖會自動過期，不會永久卡死 |
| 冪等快取 TTL = 86400 | 必須**大於**所有可能的重試時間窗（Orchestrator retry + 使用者重送） |

> ⚠️ **鎖 TTL 必須大於業務執行時間，但 P99 並不是上界**。平均 5s、P99 25s 只代表百分之一的請求會跑得比 25s *更久* —— 所以 `ex=30` 並不安全：尾端那些請求還在跑，鎖就過期了，第二個請求進來就併發雙扣。要靠三件事一起，而不是把 TTL 調大：
> 1. 給業務呼叫一個**明確的逾時上限**，而且這個上限要**小於**鎖的租期（例如 timeout 10s、TTL 30s）；
> 2. 需要跑更久就用 **watchdog 續期**，而且續期前要**檢查持有者**（比對 lock value 裡的 owner token，別把別人的鎖續掉）；
> 3. 最終還是要有**下游的幕等保證**（同一個 idempotency key 送給金流），鎖只是減少併發，不是正確性的唯一依據。

---

<!-- 306ed5df541c -->
## 7. 如何「刻意」重新執行

如果業務邏輯改變，或前一次失敗後確定要人工干預重新觸發，有 3 種標準解法：

<!-- 454a4be15a21 -->
### 7-1) 改用「嘗試次數（Attempt / Version）」而非時間戳記

由 Orchestrator 或人工明確控制版本號，而不是自動帶入隨機時間。

* **Key 格式**：`idem:refund:{order_id}:v2` 或 `idem:refund:{order_id}:attempt_2`
* **優勢**：在 `attempt_2` 的生命週期內依然享有自動重試防護，同時又能實現第二次執行。

<!-- e793e5e51c62 -->
### 7-2) 快取主動失效（Cache Eviction / Force Retry）

在管理後台（或 Orchestrator 邏輯）提供「清除冪等鎖」的選項，或呼叫時傳入 `force_retry=True`：

<!--CODE-->

> 🔒 `force_retry` **絕不可以開放給 LLM 自行決定**。它只能來自人工後台或明確的系統流程 —— 否則模型「覺得剛剛沒成功」就自己帶 `force_retry=True`，冪等性形同虛設。

<!-- a28cb667d7a7 -->
### 7-3) 綁定「業務交易單號」而非「主實體 ID」

如果這是一筆**新的退款意圖**，應該為這筆「退款請求」生成 unique 的 `refund_request_id`，而不是拿 `order_id` 當 Key。

* **Key 格式**：`idem:refund:{refund_request_id}`
* **效果**：同一個 `order_id` 可產生多次不同的 `refund_request_id`（適合多次部分退款），而每一次的 `refund_request_id` 自身依然具備冪等性。

<!--CODE-->

---

<!-- 288284e2b0d4 -->
## 8. 常見陷阱

| 陷阱 | 症狀 | 解法 |
|------|------|------|
| **Key 帶時間戳記** | 快取永不命中，等於沒做冪等 | 用固定業務鍵或 attempt 版本號 |
| **只有 GET/SET，沒有鎖** | 同一秒併發雙扣 | `SET NX EX` 分散式鎖 |
| **把失敗結果也快取** | 暫時性錯誤被鎖死 24 小時，永遠無法重試 | 只快取成功結果 |
| **TTL 太短** | 使用者 5 分鐘後重送 → 重複執行 | TTL > 最大重試窗（建議 24H） |
| **鎖 TTL < 業務耗時** | 鎖提前過期造成併發 | 業務逾時上限 < 鎖租期，尾端用 owner-checked watchdog 續期，並保留下游幕等 |
| **Redis 單點掛掉** | 冪等層全失效，副作用直接穿透 | DB unique index 兜底（見下） |
| **只擋不回結果** | 第二次呼叫回 `duplicate` error，LLM 誤判失敗又重試 | **回傳與第一次相同的成功結果**，讓 LLM 認為完成 |
| **參數不同卻共用 Key** | 退 $300 被當成退 $100 的重複 | Key 納入 payload hash，或比對參數不同時報錯 |

<!-- 64881eaedc5a -->
### 最後一道防線：資料庫 Unique Index

Redis 是「效能層」，不是「正確性層」。**真正的正確性應由 DB 保證**：

<!--CODE-->

<!--CODE-->

> 對照「[訂位系統如何避免超賣](./overbooking_prevention.md)」中的策略：Redis 原子操作擋流量、DB constraint 保正確性，是同一套分層思路。

---

<!-- d2bb9d4493f1 -->
## 9. 實務檢查清單

設計一個帶副作用的 LLM Tool 時，逐項確認：

- [ ] 這個工具有副作用嗎？（會改變外部狀態 / 花錢 / 發通知）→ 有就必須做冪等
- [ ] `idempotency_key` 由誰生成？（Orchestrator / 前端 / 模型原生 ID）
- [ ] Key 裡**沒有**時間戳記或隨機值
- [ ] 有用 `SET NX EX` 做併發鎖
- [ ] 業務呼叫有明確逾時上限，且該上限 < 鎖租期（不要只看 P99）
- [ ] **只快取成功結果**，失敗時釋放鎖
- [ ] 快取 TTL ≥ 24H（涵蓋所有重試窗）
- [ ] 重複呼叫時**回傳原本的成功結果**，而不是丟 error
- [ ] DB 有 unique index 當最後防線
- [ ] `force_retry` 只允許人工 / 系統觸發，**不暴露給 LLM**
- [ ] Tool schema 的 description 有寫明「此操作不可逆」，降低模型亂呼叫的機率
- [ ] 有記錄 audit log（誰在哪個 session 觸發了哪個副作用）

---

<!-- 49b183caa9ba -->
## 10. 參考資料

- [AI Eng from Scratch — Phase 13 Part 1: MCP & APIs](https://yennj12.js.org/yennj12_blog_V4/posts/ai-eng-from-scratch-phase13-part1-mcp-apis-zh/)
- [Stripe — Idempotent Requests](https://docs.stripe.com/api/idempotent_requests)
- [Anthropic — Tool use with Claude](https://docs.claude.com/en/docs/agents-and-tools/tool-use/overview)
- [Redis — Distributed Locks with Redlock](https://redis.io/docs/latest/develop/use-cases/patterns/distributed-locks/)
- 相關文件：[訂位系統如何避免超賣](./overbooking_prevention.md)、[後端的 Redis](../redis/redis_backend.md)
