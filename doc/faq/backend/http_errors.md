# 常見 HTTP 錯誤碼與處理方式（4xx / 5xx）

> **Scope** — 常見 4xx / 5xx 錯誤碼各代表什麼、常見成因、client 端與 server 端該怎麼處理與排查（含哪些可以重試）；不談設計 API 時該回哪個狀態碼與錯誤格式。
> **See also**: [`api_design.md`](./api_design.md) — 設計 API 時的狀態碼選擇與錯誤格式（RFC 9457）；
> [`authentication.md`](./authentication.md) — 401 背後的 Session / JWT；
> [`webhook_integration.md`](./webhook_integration.md) — 退避重試與 DLQ 的完整設計。

## 目錄

1. [先講結論：4xx vs 5xx](#1-先講結論4xx-vs-5xx)
2. [400 Bad Request](#2-400-bad-request)
3. [常見 4xx 狀態碼](#3-常見-4xx-狀態碼)
4. [常見 5xx 狀態碼](#4-常見-5xx-狀態碼)
5. [Client 端怎麼處理：重試還是放棄](#5-client-端怎麼處理重試還是放棄)
6. [Server 端怎麼排查](#6-server-端怎麼排查)
7. [常見追問](#7-常見追問)

---

## 1. 先講結論：4xx vs 5xx

**4xx 是「你（client）送錯了」，5xx 是「我（server）壞了」。** 面試時最重要的推論是：
**4xx 原封不動重送不會成功，要先改 request；5xx 通常是暫時的，可以退避重試。**

| 類別 | 誰的問題 | 原樣重試有用嗎？ | 例外 |
|---|---|---|---|
| **4xx** | Client：格式、認證、權限、資源不存在 | ❌ 沒用，要先修正 request | `408`、`429` 可以等一下再試 |
| **5xx** | Server 或它的上游：bug、過載、逾時 | ✅ 通常可以（退避重試） | `501` 重試也沒用；非冪等的請求要小心 |

---

## 2. 400 Bad Request

**HTTP 400** 表示**用戶端送給伺服器的請求有語法錯誤或無效資料**，伺服器無法理解或拒絕處理。

通俗來說：「**你填的表格或送的資料格式不對，伺服器看不懂，所以退件。**」

### 常見原因

1. **語法或格式錯誤**：JSON、XML 格式出錯（少了引號、括號不對稱）
2. **參數缺失或型別不符**：必要欄位（Required Field）沒填，或型別不對（數字欄位傳了字串）
3. **網址（URL）無效**：URL 含有未經編碼（Encode）的非法字元或特殊符號
4. **Cookie 或 Header 過大 / 損壞**：Cookie 損壞或 Header 超出伺服器上限（規範上是 `431`，但很多伺服器直接回 400）
5. **上傳檔案過大**：超過伺服器的上傳上限（規範上是 `413`，同樣常被回成 400）

### 如何解決

**一般使用者：**

1. 檢查網址（URL）是否打錯或含有異常字元
2. 清除該網站的 **Cookie 與快取（Cache）** 後重新載入
3. 上傳檔案時，確認檔案大小與格式是否符合限制

**開發者 / 前端工程師：**

1. **檢查 Payload 格式**：用 Postman 或瀏覽器 Network 面板確認送出的 JSON 符合語法
2. **對照 API 文件**：query 參數與 body 欄位的名稱、型別（String、Number、Array）是否完全相符
3. **檢查 URL Encode**：網址含中文或特殊符號時，要經過 `encodeURIComponent()` 處理
4. **讀 response body**：設計良好的 API 會在錯誤 body 裡指出是哪個欄位出錯

**後端工程師（回 400 的那一方）：** 回傳**欄位層級的錯誤訊息**，讓 client 知道要改什麼，而不是只丟一句 `Bad Request`：

```json
{
  "status": 400,
  "title": "Validation failed",
  "errors": [
    { "field": "age", "message": "must be a number" },
    { "field": "email", "message": "is required" }
  ]
}
```

---

## 3. 常見 4xx 狀態碼

| 狀態碼 | 名稱 | 核心問題 | 舉例 | 怎麼處理 |
|---|---|---|---|---|
| **400** | Bad Request | **資料格式或參數錯誤** | JSON 少了結尾括號，伺服器解析失敗 | 修正 request 內容 |
| **401** | Unauthorized | **未認證身分** | 沒帶 Token，或 Token 過期 | 重新登入 / 用 Refresh Token 換新 Access Token |
| **403** | Forbidden | **已認證但無權限** | 已登入，但非管理員無法進後台 | 申請權限；重新登入**沒用** |
| **404** | Not Found | **找不到資源** | 網址打錯、資源已被刪除 | 檢查 URL 與資源 ID |
| **405** | Method Not Allowed | **HTTP method 不支援** | 對只接受 `GET` 的路徑送 `POST` | 換正確的 method（看 `Allow` header） |
| **408** | Request Timeout | **client 送太慢** | 上傳到一半連線停住 | 可以重試 |
| **409** | Conflict | **與目前狀態衝突** | 重複建立、樂觀鎖版本不符 | 重新讀取最新狀態再決定 |
| **413** | Content Too Large | **body 太大** | 上傳檔案超過限制 | 壓縮、分段上傳 |
| **415** | Unsupported Media Type | **`Content-Type` 不對** | 送 JSON 卻沒設 `Content-Type: application/json` | 設定正確的 header |
| **422** | Unprocessable Content | **語法正確，語意不合法** | 結束日期早於開始日期 | 修正資料內容 |
| **429** | Too Many Requests | **超過限流** | 一秒內呼叫太多次 | 照 `Retry-After` 等待後再試 |

### 401 vs 403 ⭐⭐⭐⭐⭐

最常被問的一組：

- **401 = 「你是誰？」**：沒有認證，或認證失效 —— **重新登入可能就好了**
- **403 = 「我知道你是誰，但你不能做這件事」**：已認證但沒權限 —— **重新登入也沒用**

```text
沒帶 token / token 過期  → 401 → client 導向登入頁或刷新 token
普通使用者存取 /admin    → 403 → client 顯示「沒有權限」
```

### 404 也可以用來隱藏 403

對於使用者**不該知道存在**的資源（例如別人的私人訂單），回 `404` 而不是 `403`，
因為 `403` 等於告訴對方「這個 ID 存在，只是你看不到」，會洩漏資訊、方便列舉攻擊。

### 400 vs 422

`400` 是「**看不懂**」（JSON 壞掉、型別錯），`422` 是「**看得懂，但內容不合規則**」（欄位都對，但業務規則不允許）。
很多 API 統一用 400，兩者擇一並保持一致即可。

---

## 4. 常見 5xx 狀態碼

| 狀態碼 | 名稱 | 核心問題 | 常見原因 | 怎麼處理 |
|---|---|---|---|---|
| **500** | Internal Server Error | **伺服器程式出錯** | 未捕捉的例外、NullPointerException、DB 查詢錯誤 | 看 server log 與 stack trace |
| **501** | Not Implemented | **伺服器不支援這個功能** | 不認得的 HTTP method | 重試沒用 |
| **502** | Bad Gateway | **上游回了無效的回應** | 後端服務掛了 / crash、port 錯、上游連線被重置 | 檢查上游服務是否活著 |
| **503** | Service Unavailable | **暫時無法服務** | 過載、維護中、沒有健康的實例、熔斷器打開 | 照 `Retry-After` 退避重試、擴容 |
| **504** | Gateway Timeout | **上游太慢、逾時** | 慢查詢、下游 API 卡住、proxy timeout 設太短 | 找慢的那一段；調整 timeout |

### 502 vs 503 vs 504 ⭐⭐⭐⭐

這三個都是 **gateway / proxy（Nginx、Load Balancer、API Gateway）** 在回報後端的狀況，差別在後端「怎麼了」：

```text
Client ──> Nginx / LB ──> App Server ──> DB / 下游 API

502 Bad Gateway      Nginx 連上 App，但拿到的是壞回應（或連線被重置、App 掛了）
503 Unavailable      Nginx 沒有可用的 App 實例，或 App 自己說「我太忙」
504 Gateway Timeout  Nginx 等 App 等到逾時（App 可能還在等 DB）
```

排查口訣：**502 看「活著嗎」，503 看「夠不夠」，504 看「哪裡慢」。**

### 500 是 bug，不是使用者的錯

使用者輸入錯誤**不應該**變成 500 —— 那代表 server 沒做輸入驗證，例外一路往上拋。
正確做法是驗證輸入並回 `400` / `422`，讓 `500` 只代表「真的需要工程師處理的 bug」，監控告警才有意義。

---

## 5. Client 端怎麼處理：重試還是放棄

**Key Idea**：只重試「**暫時性**」錯誤，而且只重試「**冪等**」的請求。

| 狀態碼 | 重試？ | 做法 |
|---|---|---|
| `400`、`404`、`405`、`415`、`422` | ❌ | 修正 request；顯示錯誤給使用者 |
| `401` | 🔁 一次 | 刷新 token 後重送一次；仍 401 就導向登入 |
| `403` | ❌ | 顯示沒有權限 |
| `408`、`429` | ✅ | 照 `Retry-After` 等待 |
| `500` | ⚠️ 有限次 | 可能是暫時性問題，最多重試幾次 |
| `502`、`503`、`504` | ✅ | 指數退避 + jitter |
| `501` | ❌ | 功能不存在 |

```python
import random
import time

import requests

RETRYABLE = {408, 429, 500, 502, 503, 504}
MAX_ATTEMPTS = 5


def get_with_retry(url):
    for attempt in range(MAX_ATTEMPTS):
        response = requests.get(url, timeout=10)
        if response.status_code not in RETRYABLE:
            return response                       # 成功，或重試也沒用的 4xx

        if attempt == MAX_ATTEMPTS - 1:
            break                                 # 最後一次，不用再等

        retry_after = response.headers.get("Retry-After")
        if retry_after is not None and retry_after.isdigit():
            delay = int(retry_after)              # server 指定的等待秒數優先
        else:
            delay = 2 ** attempt                  # 1, 2, 4, 8 秒…
        delay += random.uniform(0, 1)             # jitter：避免所有 client 同時重試
        time.sleep(delay)

    return response
```

**非冪等的請求要小心重試。** `POST /payments` 回了 `504`，不代表扣款沒成功 —— 可能 server 已經處理完，只是回應來不及送回。
重試前要帶 **idempotency key**，讓 server 認得這是同一筆請求（見 [`api_design.md`](./api_design.md)）。

---

## 6. Server 端怎麼排查

| 症狀 | 先看哪裡 |
|---|---|
| 突然大量 `500` | 應用程式 log 的 stack trace；最近一次部署 |
| `502` | 上游 process 是否還活著（crash、OOM kill）；proxy 設定的 port / upstream |
| `503` | 健康檢查、實例數、CPU / 記憶體、連線池是否耗盡、熔斷器狀態 |
| `504` | 慢查詢 log、下游 API latency；proxy timeout 是否比 app 的處理時間短 |
| 大量 `4xx` | 新版 client 是否送錯格式；是否有人在掃描或爆破（大量 `401` / `404`） |

讓排查變快的三件事：

1. **每個 response 帶 trace id**，同一個 id 也寫進 log —— 使用者回報錯誤時，一次查詢就能找到完整呼叫鏈
2. **錯誤率分 4xx / 5xx 監控**：5xx 上升要告警（是我們的問題），4xx 上升通常是 client 或攻擊
3. **不要回 `200 {"error": ...}`**：這會讓 proxy、監控與 client 的重試邏輯全部看不到錯誤

---

## 7. 常見追問

**Q：為什麼叫 401 Unauthorized，意思卻是「未認證」？**
這是 HTTP 規範的歷史命名問題。實際語意是 **unauthenticated**；真正的「未授權」是 `403`。

**Q：500 和 503 都是 server 錯，有什麼差別？**
`500` 是「**出了 bug**」，重試大概也一樣失敗；`503` 是「**暫時忙不過來**」，等一下通常就好，
而且 server 可以用 `Retry-After` 告訴 client 要等多久。

**Q：瀏覽器看到 CORS 錯誤，是 4xx 嗎？**
不一定。CORS 是**瀏覽器**擋下回應，server 可能其實回了 200。要看 Network 面板裡 preflight（`OPTIONS`）
的狀態碼與 `Access-Control-Allow-*` headers。

**Q：API Gateway 回 504，但 app log 顯示請求成功了？**
App 處理完的時間超過了 gateway 的 timeout。Client 看到失敗、server 卻已經成功 —— 這正是
非冪等請求需要 idempotency key 的原因。
