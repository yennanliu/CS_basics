<!-- b72951692068 -->
# API 設計 FAQ（REST，以及什麼時候不要用它）

> **範圍** — 設計一支 HTTP API：資源建模、狀態碼、冪等、分頁、版本管理、錯誤格式、認證授權，以及 REST 和 GraphQL、gRPC 的比較。
> **另見**：[`authentication.md`](./authentication.md) — session 與 JWT；
> [`be_programming_notes.md`](./be_programming_notes.md) — 限流、重試、
> 程式裡的冪等鍵；[`web_long_connections.md`](./web_long_connections.md) —
> 串流與推播。

「幫 X 設計一支 API」是多數後端面試的暖身題。真正被評分的是：你有沒有在想這份
**契約** —— 它怎麼演進、它會怎麼壞，以及客戶端要做什麼才能正確地用它。

---

<!-- 40bcc48cd258 -->
## 1) 資源，不是動詞 ⭐⭐⭐⭐⭐

REST 的資源是一個帶著穩定 URI 的**名詞**；動詞交給 HTTP method。

<!--CODE-->

在 review 裡站得住腳的規則：名詞用複數、小寫加連字號、最多只巢狀一層
（`/orders/1042/items`，再深就改成給連結）、路徑裡不要有動詞
（`/getOrders` ❌）—— 除非那是真正的動作、而不是對資源的修改
（`/cancel`、`/search`、`/reindex`），這種大家都接受寫成 `POST` 的子資源。

<!-- 36b906ef6184 -->
### Method 的語意 ⭐⭐⭐⭐⭐

| Method | 安全（沒有副作用） | 冪等（呼叫 N 次 == 1 次） | 可快取 |
|--------|----------------------|-------------------------------|-----------|
| `GET` | ✅ | ✅ | ✅ |
| `HEAD` | ✅ | ✅ | ✅ |
| `PUT` | ❌ | ✅ | ❌ |
| `DELETE` | ❌ | ✅（第二次呼叫回 404/204） | ❌ |
| `PATCH` | ❌ | ⚠️ 只有你刻意這樣設計才是 | ❌ |
| `POST` | ❌ | ❌ | 很少 |

**冪等就是那個讓重試變安全的性質**，而網路讓重試無可避免。既然 `POST` 不是冪等的，
就給它一個**冪等鍵**：

<!--CODE-->

伺服器把這個 key 連同第一次的回應存起來，之後任何重複請求都重播那份回應 ——
這個模式在 [`be_programming_notes.md`](./be_programming_notes.md) 裡寫得很完整，
AI 工具呼叫的版本則在 [`llm_tool_idempotency.md`](./llm_tool_idempotency.md)。

---

<!-- e8d68153f98a -->
## 2) 狀態碼 ⭐⭐⭐⭐

| 代碼 | 用在 |
|------|-----------|
| **200** OK | 成功，有回應內容 |
| **201** Created | 建立了資源 —— 回一個 `Location` header |
| **202** Accepted | 非同步：已接受待處理，還沒完成 |
| **204** No Content | 成功但沒有內容（`DELETE`，或不回東西的 `PUT`） |
| **400** Bad Request | 語法錯誤／驗證失敗 |
| **401** Unauthorized | **沒有通過認證**（憑證錯誤或缺少） |
| **403** Forbidden | 認證過了，但**沒有權限** |
| **404** Not Found | 沒有這個資源 —— 當 403 會洩漏「它存在」時，這也是比較有禮貌的答案 |
| **409** Conflict | 版本衝突、重複、狀態機被違反 |
| **422** Unprocessable | 語法沒問題但語意不合法（當你想和 400 區分開時） |
| **429** Too Many Requests | 被限流 —— 要附上 `Retry-After` |
| **500** Internal Server Error | 你這邊沒處理到的 bug |
| **502 / 503 / 504** | 上游壞掉／服務不可用（附 `Retry-After`）／上游逾時 |

面試官特別在聽的兩個錯誤：失敗時回 `200 {"error": ...}`（這會弄壞每一個讀狀態列的
客戶端、代理與監控），以及把客戶端輸入錯誤回成 `500`。

---

<!-- 0cd7a068af06 -->
## 3) 錯誤的格式 ⭐⭐⭐⭐

所有錯誤共用一種格式，而且機器先讀得懂：

<!--CODE-->

這就是 RFC 9457（`application/problem+json`），Spring 的 `ProblemDetail` 可以直接
產出它。不管你選哪種格式：

- 要有**穩定的錯誤代碼**讓客戶端分流 —— 不要逼它們去解析人話；
- 要有一個**trace id**，而且同樣出現在你的 log 裡，這樣使用者回報就變成一次查詢；
- 驗證錯誤要附欄位層級的細節；
- 回應裡**絕對不要**出現 stack trace、SQL 片段或內部主機名稱。

---

<!-- 17b68abe672c -->
## 4) 分頁、過濾與排序 ⭐⭐⭐⭐

| 做法 | 請求 | 好處 | 壞處 |
|-------|---------|------|-----|
| **Offset** | `?page=3&size=50` | 可以跳到任一頁、能顯示總數 | `OFFSET 100000` 會變慢；資料變動時項目會在頁與頁之間跑掉 |
| **Cursor／keyset** | `?limit=50&cursor=eyJpZCI6MTA0Mn0` | 有寫入時仍然穩定；不管翻多深，成本都是一次索引定位加上該頁（`O(log n + limit)`） | 沒有頁碼、沒有總數 |

動態時報、log，以及任何量大或會一直變的資料，優先用 **cursor** 分頁；小的後台表格用
offset 沒問題。伺服器端永遠要把 `limit` 設上限（客戶端要 1,000,000 筆就是一次
阻斷服務攻擊）、永遠要有預設值，並把下一個 cursor 放在回應主體或 `Link` header 裡。

<!--CODE-->

過濾與排序放在 query string（`?status=open&sort=-createdAt`），而且可排序的欄位要用
**白名單** —— 對沒有索引的欄位排序就是全表掃描，而把欄位名字串接進 SQL 就是注入。

---

<!-- 6ec216527a8c -->
## 5) 版本管理與演進 ⭐⭐⭐⭐

| 做法 | 例子 | 備註 |
|----------|---------|-------|
| **URI 路徑** | `/v1/orders` | 最醜、最清楚，對快取與 log 都友善。最常見的選擇 |
| Header／媒體型別 | `Accept: application/vnd.acme.v2+json` | 比較「純」，但比較難測也難快取 |
| 查詢參數 | `?version=2` | 容易加，也容易掉 |

更划算的技能是**根本不需要新版本**。向後相容的改動：加一個選填欄位、加一個端點、
加一個列舉值（*前提是你有告訴客戶端要忽略不認得的值*）。破壞性改動：移除或改名欄位、
把驗證變嚴、改型別或改某個值的意義、改預設值。

所以：客戶端必須忽略不認得的欄位、伺服器不能把既有欄位挪作他用，而移除要走
**標記淘汰 → 公告日落時間（`Deprecation`/`Sunset` header）→ 觀察剩餘流量 → 移除**。

---

<!-- 60cc9bf93ff9 -->
## 6) 並發與快取 ⭐⭐⭐

**更新遺失**：兩個客戶端都讀到版本 1，都寫回去，第二個就默默把第一個蓋掉。
用樂觀並發的權杖來修：

<!--CODE-->

值得記住的快取 header：`Cache-Control: public, max-age=60`（個人化的東西用
`no-store`）、`ETag` + `If-None-Match` → `304 Not Modified` 做便宜的再驗證，
以及回應會因 header 而異時要加的 `Vary`（`Accept-Encoding`、`Authorization`）。

---

<!-- 5b1851f9f7bc -->
## 7) 安全檢查表 ⭐⭐⭐⭐⭐

- **只走 HTTPS**，開 HSTS。憑證與 token 不要放在 URL 裡 —— URL 會落進 log、
  代理與 referrer。
- **先認證再授權**，而且授權要**針對每個物件**，不是只看端點。
  「物件層級授權失效」（`GET /orders/1043` 回傳別人的訂單）在實務上是 API 漏洞第一名。
- 伺服器端**驗證每一個輸入**，照 schema 來 —— 長度、型別、範圍、列舉。客戶端驗證
  是使用者體驗，不是安全性。
- **絕對不要信任客戶端送來的身分、價格或角色。**價格請自己重算。
- 針對每個主體與每個 IP **限流**；被擋時回 `429` 並附 `Retry-After`。
- 錯誤**不要洩漏資訊**：沒有 stack trace、登入時不要區分「查無此人」與「密碼錯誤」、
  在意的話也不要用猜得到的內部 id（用 UUID/ULID）。
- 設定**請求大小上限**與逾時；解析 JSON 時限制深度。
- CORS 要明確白名單；有認證的 API 上不要出現 `Access-Control-Allow-Origin: *`。
- 記錄誰做了什麼，但不要把祕密或個資內容寫進 log。

Token 的機制 —— session 與 JWT、refresh token、撤銷 —— 在
[`authentication.md`](./authentication.md)。

---

<!-- e27151a5cb95 -->
## 8) 長時間與批次作業 ⭐⭐⭐

任何慢到不該讓請求卡著等的事，就回一個 job：

<!--CODE-->

然後讓客戶端輪詢，或用 SSE／webhook 推給它
（[`web_long_connections.md`](./web_long_connections.md)）。你送出去的 webhook 也要
守你要求別人守的規矩：payload 要簽章、至少一次投遞並附事件 id 讓接收端去重，
以及帶退避的重試。

---

<!-- e71761f2173a -->
## 9) REST、GraphQL 與 gRPC ⭐⭐⭐

| | REST/JSON | GraphQL | gRPC |
|---|-----------|---------|------|
| 形狀 | HTTP 上的資源 | 單一端點，由客戶端指定查詢 | HTTP/2 + protobuf 上的型別化 RPC |
| 取太多／取不夠 | 常發生 | 設計上就解掉了 | 各方法自己解 |
| 快取 | HTTP 快取免費送 | 很難（POST、每個查詢都不同） | 要自己做 |
| Schema | OpenAPI（選用） | 必備，而且可內省 | 必備的 `.proto` |
| 串流 | SSE/WebSocket 外掛上去 | Subscription | 原生雙向 |
| 瀏覽器支援 | 原生 | 原生 | 需要 grpc-web |
| 痛點 | 端點爆炸 | 查詢成本／深度限制、resolver 的 N+1 | 難除錯、payload 是二進位 |

經驗法則：對外與偏 CRUD 的 API 用 **REST**；在意延遲與型別契約的內部服務之間用
**gRPC**；當很多不同客戶端需要同一張圖的很多種投影時，用 **GraphQL**。

---

<!-- b365b4bbd887 -->
## 10) 面試常見問答

**Q：PUT、PATCH、POST 差在哪？**
`PUT` 取代整個資源，是冪等的；`PATCH` 做部分更新（只有你刻意設計才是冪等的 ——
「把狀態設成 shipped」是，「數量加 1」不是）；`POST` 建立或觸發，不是冪等的。

**Q：付款 API 要怎麼做才能安全重試？**
請求帶冪等鍵，並把結果和它一起存起來；同一個 key 回傳原本的回應，而不是再扣一次款。
再搭配資料庫的唯一性約束當最後一道防線。

**Q：401 和 403 差在哪？**
401 ＝我們不知道你是誰（或你的憑證過期了）。403 ＝我們知道你是誰，但你不可以。

**Q：一個一直在被寫入的動態時報要怎麼分頁？**
用不可變、單調有序的 key 做 cursor／keyset 分頁 —— 用 offset 的話，捲到一半有新資料
插進來就會重複或漏掉項目。

**Q：要改一個欄位的型別，怎麼不弄壞客戶端？**
在舊欄位旁邊加新欄位、兩邊都寫、讓客戶端逐步遷移、觀察用量，最後在新版本裡帶著
日落期把舊欄位拿掉。

**Q：限流要放在哪裡做？**
邊緣（gateway/CDN）做粗粒度的 IP 限制，服務裡做每使用者／每方案的限制，因為那需要
身分。演算法 —— token bucket、滑動視窗 —— 在
[`be_programming_notes.md`](./be_programming_notes.md)。

**Q：文件怎麼寫？**
用 OpenAPI，從程式碼產生或拿它來驗證程式碼，讓兩者不會走鐘，並且每個端點、
每種錯誤都附範例。

---

<!-- ad3e1cc7f87b -->
## 11) 重點檢查表

<!--CODE-->

---

<!-- 0972da56c763 -->
## 參考資料

- [RFC 9110 — HTTP semantics](https://www.rfc-editor.org/rfc/rfc9110.html)
- [RFC 9457 — Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc9457.html)
- [OWASP API Security Top 10](https://owasp.org/API-Security/editions/2023/en/0x11-t10/)
- [`authentication.md`](./authentication.md) · [`be_programming_notes.md`](./be_programming_notes.md) · [`web_long_connections.md`](./web_long_connections.md)
