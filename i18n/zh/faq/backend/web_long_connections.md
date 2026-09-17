<!-- fec870912086 -->
# Web 長連線

> 網頁應用要怎麼維持持久／長壽命的連線

<!-- e262eb2e8ab3 -->
## 目錄

1. [HTTP Keep-Alive（持久連線）](#1-http-keep-alive-persistent-connection)
2. [HTTP Pipelining](#2-http-pipelining)
3. [Long Polling 長輪詢](#3-long-polling)
4. [串流（分塊傳輸）](#4-streaming-chunked-transfer)
5. [Server-Sent Events（SSE）](#5-server-sent-events-sse)
6. [WebSocket](#6-websocket-upgrade-from-http)
7. [HTTP/2 多工](#7-http2-multiplexing)
8. [總結](#summary)

---

在 HTTP 的世界裡，「長連線」通常指的是讓一條 TCP 連線在多個請求之間保持開啟，而不是每個請求結束就關掉（也常稱為**持久連線**或 **keep-alive**）。依你想達成的目的不同，這個概念在實務上有好幾種形式。

---

<!-- 1f75bf74ad3d -->
## 1. HTTP Keep-Alive（持久連線）

* HTTP/1.1 的預設行為。
* 用戶端與伺服器在多個請求／回應之間重複使用同一條 TCP 連線。
* 省掉反覆開關連線的延遲與開銷。

**Header 範例：**
<!--CODE-->

**流程：**
<!--CODE-->

---

<!-- 381cfb4b2f73 -->
## 2. HTTP Pipelining

* 多個 HTTP 請求一次送出，不必等前一個回應。
* 但回應仍必須按順序回來。
* 因為**隊頭阻塞（head-of-line blocking）**問題，今天幾乎不用了。

**流程：**
<!--CODE-->

---

<!-- 9e66ec4f7001 -->
## 3. Long Polling 長輪詢

* 用戶端送出一個請求。
* 伺服器把這個請求**壓著不回**，等到有新資料才回。
* 資料一送出，連線就關閉，用戶端立刻再連一次。
* 常用來做接近即時的更新（比較舊的技術）。

**流程：**
<!--CODE-->

**適用場景：**聊天室、通知系統（舊做法）。

---

<!-- 8fc91fedc749 -->
## 4. 串流（分塊傳輸 Chunked Transfer）

* 伺服器保持連線開著，持續以 chunk 的形式把資料送出去。
* 適合 log、即時資料流、進度更新。

**Header：**
<!--CODE-->

**範例（Node.js）：**
<!--CODE-->

---

<!-- d256483bddce -->
## 5. Server-Sent Events（SSE）

* 單向通訊：只有**伺服器 → 用戶端**。
* 瀏覽器保持一條長壽命的 HTTP 連線。
* 做即時更新（推播通知、儀表板）比 WebSocket 簡單。
* 斷線會自動重連。

**Header：**
<!--CODE-->

**伺服器端範例（Node.js）：**
<!--CODE-->

**用戶端範例：**
<!--CODE-->

---

<!-- d1638c3f1f1a -->
## 6. WebSocket（從 HTTP 升級而來）

* 一開始是 HTTP，接著升級成**持久的全雙工**連線。
* 支援即時的雙向通訊。
* 最適合聊天、遊戲、即時協作。

**升級用的 header：**
<!--CODE-->

**伺服器端範例（Node.js 搭配 `ws`）：**
<!--CODE-->

**用戶端範例：**
<!--CODE-->

---

<!-- 0653ec146af9 -->
## 7. HTTP/2 多工

* 多條 stream 共用**同一條長壽命連線**。
* 解決了 HTTP 層的隊頭阻塞（HTTP/1.1 的 pipelining 沒解決）。
* 更有效率：header 會壓縮（HPACK），各條 stream 互相獨立。

**和 HTTP/1.1 的主要差異：**

| 特性 | HTTP/1.1 | HTTP/2 |
|---|---|---|
| 每個 host 的連線數 | 多條 | 一條（多工） |
| 隊頭阻塞 | 有 | 沒有（HTTP 層） |
| Header 壓縮 | 沒有 | 有（HPACK） |
| Server push | 沒有 | 有 |

---

<!-- 043e242f1d7b -->
## 總結

| 方法 | 方向 | 連線 | 適用場景 |
|---|---|---|---|
| **Keep-Alive** | 請求／回應 | 重用 TCP | 一般 HTTP 效率 |
| **Pipelining** | 請求／回應 | 重用 TCP | 批次請求（幾乎不用了） |
| **Long Polling** | 伺服器 → 用戶端 | 每次都重開 | 舊式的即時更新 |
| **分塊串流** | 伺服器 → 用戶端 | 長壽命 | log、即時資料流 |
| **SSE** | 伺服器 → 用戶端 | 長壽命 | 儀表板、通知 |
| **WebSocket** | 全雙工 | 長壽命 | 聊天、遊戲、協作 |
| **HTTP/2** | 請求／回應 | 多工 | 現代 API、降低延遲 |

**快速決策指南：**
- **要重用連線：**Keep-Alive、HTTP/2
- **用戶端在等資料：**Long Polling
- **伺服器要持續送資料：**串流、SSE
- **要全雙工（雙向）通訊：**WebSocket

---

<!-- 5a7478b819cb -->
## 優點、缺點與適用場景比較

| 方法 | 優點 | 缺點 | 最適合 |
|---|---|---|---|
| **HTTP Keep-Alive** | • 省掉 TCP 三向交握的開銷<br>• HTTP/1.1 預設，支援度極廣<br>• 簡單 —— 不用多寫程式 | • 依然只是請求／回應<br>• 伺服器要抱著閒置連線<br>• 同一條連線內仍有隊頭阻塞 | • REST API<br>• 靜態資源<br>• 任何一般 HTTP 流量 |
| **HTTP Pipelining** | • 不必等就能連送多個請求<br>• 減少來回延遲 | • 回應必須按序抵達（隊頭阻塞）<br>• proxy／伺服器支援很差<br>• 基本上已被淘汰 | • 幾乎不用；已被 HTTP/2 取代 |
| **Long Polling** | • 任何 HTTP 環境都能用<br>• 對防火牆／proxy 友善<br>• 不需要特殊的瀏覽器 API | • 伺服器資源吃很兇（連線被抱著）<br>• 每次回應後重連都有延遲<br>• 不是真的即時 | • 舊的聊天系統<br>• 舊環境裡的通知輪詢<br>• 沒有 WebSocket／SSE 可用時 |
| **分塊串流** | • 伺服器端實作簡單<br>• 純 HTTP 就能跑<br>• 適合很大或長度未知的回應 | • 沒有內建重連<br>• 用戶端要自己處理不完整的 chunk<br>• 結構性不如 SSE | • 檔案下載<br>• 追 log<br>• 回報進度 |
| **Server-Sent Events（SSE）** | • 內建自動重連<br>• 瀏覽器原生 `EventSource` API<br>• 輕量 —— 純 HTTP，不需要升級<br>• 用標準負載平衡器就好擴展 | • 只能伺服器 → 用戶端（單向）<br>• 只能傳文字<br>• 某些 proxy 會緩衝而造成延遲 | • 即時儀表板<br>• 新聞／賽事比分<br>• 推播通知<br>• 動態時報 |
| **WebSocket** | • 全雙工（雙向）<br>• 交握完成後延遲很低<br>• 支援二進位與文字<br>• 瀏覽器支援廣泛 | • 需要協定升級<br>• 較難擴展（要 sticky session 或 pub/sub）<br>• 有些防火牆／proxy 會擋 WS<br>• 錯誤處理較複雜 | • 聊天應用<br>• 線上遊戲<br>• 即時協作（例如 Google Docs）<br>• 金融交易報價 |
| **HTTP/2 多工** | • 一條連線上跑多條 stream<br>• 沒有隊頭阻塞（HTTP 層）<br>• Header 壓縮（HPACK）<br>• 可以 server push | • TCP 層的隊頭阻塞仍在（由 HTTP/3 解決）<br>• 多數瀏覽器要求 TLS<br>• 比較難除錯 | • 現代 REST/gRPC API<br>• 資源很多的網頁<br>• 微服務之間的通訊 |

<!-- cc84e0f59abe -->
### 可擴展性的考量

| 方法 | 伺服器資源 | 水平擴展 |
|---|---|---|
| Keep-Alive | 低（短暫閒置） | 容易 |
| Long Polling | 高（每個用戶端抱著一條執行緒） | 中等 |
| 分塊串流 | 中（開著的回應流） | 中等 |
| SSE | 中（開著的回應流） | 設計成無狀態就很容易 |
| WebSocket | 中偏高（持久連線） | 需要 sticky session 或訊息中介（Redis pub/sub） |
| HTTP/2 | 低到中（一條連線、多條 stream） | 容易 |

<!-- 3fb4c41eec3f -->
### 參考資料

- https://systemdesignschool.io/problems/google-doc/solution
