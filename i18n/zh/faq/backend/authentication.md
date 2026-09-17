<!-- 1d519f9f3425 -->
# 認證機制：Session 與 JWT

<!-- e363639c6b95 -->
## 目錄

1. [Session（傳統方式）](#1-session傳統方式)
2. [JWT（JSON Web Token）](#2-jwtjson-web-token)
3. [Session vs JWT 比較](#3-session-vs-jwt-比較)
4. [如何選擇？](#4-如何選擇)

---

<!-- 5918ec9495fa -->
## 1. Session（傳統方式）

<!-- 016daaa3e2c1 -->
### 運作流程

1. **登入驗證**：使用者提交帳密，伺服器驗證正確
2. **建立 Session**：
   - 伺服器在後端生成一個唯一的 **Session ID**（隨機不可預測的字串）
   - 將使用者資訊（如 `user_id`、`role`）存入儲存媒介，並與 ID 綁定
3. **發送 Cookie**：伺服器透過 `Set-Cookie` Header 將 Session ID 傳給瀏覽器
4. **後續請求**：瀏覽器自動帶上 Cookie，伺服器取 ID 查後端確認身份

<!-- 637fa40a0c9d -->
### Session 儲存位置

| 儲存媒介 | 說明 |
|---------|------|
| **Memory（記憶體）** | 最簡單，但重啟後資料消失，無法跨伺服器 |
| **Redis（推薦）** | 速度快，支援 TTL，適合分散式系統 |
| **Database** | 最穩定，但頻繁讀寫會造成壓力 |

<!-- 6f298cc5d3ba -->
### Cookie 安全設定

| 設定參數 | 說明 | 防禦攻擊 |
|---------|------|---------|
| **HttpOnly** | JavaScript 無法讀取 Cookie | XSS 攻擊 |
| **Secure** | 只在 HTTPS 下傳輸 | 中間人攻擊 |
| **SameSite** | 設為 `Lax` 或 `Strict` | CSRF 攻擊 |
| **Max-Age / Expires** | 設定有效期 | 降低裝置遺失風險 |

<!-- 2c9626c2f912 -->
### 程式碼範例（Node.js + Express + Redis）

<!--CODE-->

<!-- 76018018eab3 -->
### Session 的優缺點

**優點：**
- 掌控權高：可即時從後端刪除 Session，強制登出
- 資料安全：敏感資料留在伺服器，前端只拿 ID

**缺點：**
- 伺服器壓力：使用者增多時，記憶體消耗增加
- 擴展麻煩：分散式架構需額外的 Redis 來共享 Session

---

<!-- 41e091bc5e03 -->
## 2. JWT（JSON Web Token）

<!-- 53603cd3aed7 -->
### 結構

JWT 由三部分組成，以 `.` 分隔：`xxxxx.yyyyy.zzzzz`

1. **Header（標頭）**：標註 token 類型（JWT）與加密演算法（如 HS256）
2. **Payload（負載）**：存放非敏感使用者資料（如 `userId`、`role`、`exp` 過期時間）
3. **Signature（簽章）**：伺服器用私鑰對前兩部分進行雜湊運算

<!--CODE-->

> Payload 只是 Base64 編碼，**任何人都能解碼**。絕對不要存密碼等敏感資料！

<!-- 75e99caf8201 -->
### 標準流程

1. **登入**：使用者送出帳密
2. **簽發 Token**：伺服器驗證成功後，生成 JWT 回傳給前端
3. **怎麼帶 token**：這裡有**兩條互斥的路**，不要混在一起講 ——

| | `HttpOnly Cookie` 流程 | `Authorization: Bearer` 流程 |
|---|---|---|
| Token 放哪 | 伺服器用 `Set-Cookie` 寫入，JavaScript **讀不到** | 前端自己保管（記憶體 / `localStorage`） |
| 怎麼送出 | **瀏覽器自動附帶**，前端不用也不能組 header | 前端每次請求自己加 `Authorization: Bearer <token>` |
| XSS | 較安全：腳本偷不走 token | 較危險：腳本讀得到就偷得走 |
| CSRF | **會有**，要靠 `SameSite=Lax/Strict` + CSRF token 防 | 天生免疫（攻擊者的頁面沒辦法叫你的瀏覽器加這個 header） |
| 適合 | 同站的網頁應用 | 行動 App、跨網域 API、第三方客戶端 |

> 常見的錯誤就是「存在 `HttpOnly Cookie`，然後前端在 header 裡加 Bearer」—— `HttpOnly` 的重點正是讓
> JavaScript 讀不到那個值，所以第 4 步組不出來。要嘛讓瀏覽器自動送 cookie，要嘛把 token 交給前端保管。

4. **伺服器校驗**：伺服器用 Secret Key 重新計算簽章，若一致且未過期，則視為合法

<!-- 8ecc7bd729de -->
### 雙 Token 機制（推薦實作）

單一 JWT 的致命傷：**發出去就收不回**。實務上通常使用雙 Token：

- **Access Token（短效）**：有效期約 15 分鐘，用來請求 API
- **Refresh Token（長效）**：有效期約 7～30 天，存於資料庫或 Redis
- 當 Access Token 過期，前端拿 Refresh Token 換新的 Access Token
- **優點**：想登出時只需從資料庫刪除 Refresh Token，帳號在 15 分鐘後徹底失效

<!-- 5a09144ff6b2 -->
### 程式碼範例（Node.js）

<!--CODE-->

<!-- 978bb784855c -->
### JWT 的優缺點

**優點：**
- 效能好：伺服器不需查資料庫，直接解析 Token 即可驗證
- 跨平台/微服務：不同服務間不需共享 Session 也能驗證身份

**缺點：**
- 無法即時撤銷：除非做黑名單機制，否則在過期前難以主動撤銷
- 體積較大：每次請求都要傳送整串 Token

<!-- 89bfaccd89a3 -->
### 安全注意事項

1. **別存敏感資料**：Payload 是 Base64，任何人都能解碼
2. **指定演算法**：驗證時明確指定（如 `algorithms: ['HS256']`），防止 `none` 攻擊
3. **防 XSS**：建議存放在 `HttpOnly Cookie`，而非 `localStorage`
4. **檢查過期 `exp`**：務必驗證 Payload 中的 `exp` 欄位

---

<!-- 340ffbe9ba98 -->
## 3. Session vs JWT 比較

| 特性 | Session | JWT |
|------|---------|-----|
| **儲存位置** | 伺服器端（Redis、DB） | 客戶端（Token 自帶資訊） |
| **狀態性** | Stateful（有狀態） | Stateless（無狀態） |
| **擴充性** | 較難（多台伺服器需同步） | 極佳（適合微服務） |
| **安全控制** | 高（可即時撤銷） | 較低（過期前難以撤銷） |
| **頻寬消耗** | 小（只傳 Session ID） | 較大（含使用者資料與簽名） |
| **主要攻擊** | Session Hijacking | XSS、Token 外洩 |

---

<!-- 4061dd7ee309 -->
## 4. 如何選擇？

**選擇 Session，如果：**
- 開發傳統 Web 單體應用
- 需要對登入狀態有**絕對控制權**（如銀行、高安全性後台）
- 不想處理複雜的 Token 刷新機制

**選擇 JWT，如果：**
- 架構是 **SPA（React/Vue）** 或**行動裝置 App**
- 有**微服務**需求，多個系統需共用登入狀態
- 希望水平擴張伺服器，不想處理 Session 同步問題
