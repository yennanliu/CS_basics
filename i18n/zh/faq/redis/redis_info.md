<!-- 4c87611dad45 -->
# 後端工程師的 Redis

Redis 是一個強大的開源記憶體資料結構儲存系統，在現代後端系統裡被廣泛使用。
它常被用來做快取、session 儲存、即時分析與訊息系統等等。
對後端工程師來說，懂 Redis 是優化效能、建立可擴展系統的關鍵。

以下是後端工程師該掌握的 Redis 知識大綱：

---

<!-- 41699e1aec8b -->
### 1. **Redis 是什麼？**

* **記憶體資料庫**：Redis 把所有資料放在 RAM 裡，讀寫都極快。
* **key-value 儲存**：Redis 基本上是 key-value 儲存，但也支援更複雜的資料結構。
* **資料結構**：支援字串、list、set、sorted set、hash、bitmap、hyperloglog 與地理空間索引。
* **持久化選項**：可以設定成用 RDB 快照與 AOF 日誌做持久化，也可以純粹當記憶體快取用。
* **單執行緒**：Redis 用單一執行緒處理請求，這讓它在許多場景下又快又有效率。

---

<!-- f483c134c35b -->
### 2. **常見使用場景**

* **快取**：把常被存取的資料放進 Redis，可以降低資料庫負載、縮短回應時間（例如快取網頁或查詢結果）。
* **Session 管理**：常用來存使用者 session（例如 JWT token、登入資料）。
* **即時分析**：很適合即時資料處理，像是計數器、排行榜等等。
* **Pub/Sub 訊息**：提供簡單的發布／訂閱機制，適合事件驅動架構。
* **佇列**：常被拿來做任務佇列，讓工作非同步處理。
* **限流**：可以用來實作 token bucket 或 leaky bucket 之類的限流演算法。

---

<!-- e25375633dd0 -->
### 3. **核心 Redis 指令**

* **字串（Strings）**：Redis 最基本的資料型別。

  * `SET key value`
  * `GET key`
  * `INCR key`（把 key 存的數值加一）
* **List**：有序的元素集合，常拿來當佇列或堆疊。

  * `LPUSH key value`（從左邊推入）
  * `RPUSH key value`（從右邊推入）
  * `LPOP key`（從左邊彈出）
  * `RPOP key`（從右邊彈出）
  * `LRANGE key start stop`（取出某個範圍的元素）
* **Set**：不重複元素的無序集合。

  * `SADD key member`（加入成員）
  * `SREM key member`（移除成員）
  * `SMEMBERS key`（取出所有成員）
* **Sorted Set**：每個元素都配一個分數的集合，做排行榜很好用。

  * `ZADD key score member`（以分數加入成員）
  * `ZRANGE key start stop`（取出某個分數範圍的成員）
* **Hash**：欄位對值的映射，類似 Python 的 dict 或 JSON 物件。

  * `HSET key field value`（設定 hash 裡的欄位）
  * `HGET key field`（取出某欄位的值）
  * `HGETALL key`（取出所有欄位與值）
* **Bitmap**：存位元，很適合追蹤像是使用者旗標這類二元資料。

  * `SETBIT key offset value`（設定某個位移上的位元）
  * `GETBIT key offset`（取出某個位移上的位元）
* **HyperLogLog**：機率型資料結構，用來估算不重複元素的數量。

  * `PFADD key element`（加入一個元素）
  * `PFCOUNT key`（取得不重複元素的近似個數）
* **地理空間（Geospatial）**：Redis 支援存放地理位置資料。

  * `GEOADD key longitude latitude member`
  * `GEODIST key member1 member2`（計算兩個成員之間的距離）
  * `GEORADIUS key longitude latitude radius`（取出某半徑內的成員）

---

<!-- 66d5a4f85c3d -->
### 4. **Redis 的進階功能**

* **持久化**：

  * **RDB（Redis Database）**：以快照為基礎的持久化（每隔一段時間存一次）。
  * **AOF（Append-Only File）**：記錄 Redis 收到的每一個寫入操作（耐久性好，但可能比較慢）。
  * **混合做法**：RDB 與 AOF 併用，兼顧耐久性與效能。
* **複製（Replication）**：支援主從複製，達成資料冗餘與水平擴展。

  * Master 節點負責寫入。
  * Slave 節點複製資料並承接讀取請求。
* **分片（Sharding）**：支援把資料切分到多個 Redis 實例上，達成水平擴展。
* **Sentinel（哨兵）**：提供高可用與自動故障轉移。Redis Sentinel 會監控你的 Redis 伺服器，在 master 掛掉時處理 failover。
* **Cluster（叢集）**：Redis Cluster 讓 Redis 分散式運作，自動分片，並在多個節點之間做容錯與複製。

---

<!-- 80edfdd3397d -->
### 5. **Redis 的過期與淘汰策略**

* **TTL（存活時間）**：可以用 `SETEX`、`EXPIRE` 之類的指令為 key 設定過期時間。

  * `SETEX key seconds value`（設定 key 並帶過期時間）
  * `EXPIRE key seconds`（為既有的 key 設定過期時間）
  * `TTL key`（查詢 key 還剩多久）
* **淘汰策略**：記憶體用完時，Redis 有幾種淘汰策略：

  * `noeviction`：不淘汰，記憶體滿了寫入就回傳錯誤。
  * `allkeys-lru`：淘汰最近最少使用（LRU）的 key。
  * `volatile-lru`：只在有設過期時間的 key 裡淘汰最近最少使用的。
  * `allkeys-random`：隨機淘汰 key。
  * `volatile-random`：只在有設過期時間的 key 裡隨機淘汰。

---

<!-- 7eaa7321e694 -->
### 6. **Redis 的效能考量**

* **記憶體儲存**：Redis 快是因為資料都在記憶體裡。但也因此要確保你的 Redis 實例有足夠記憶體撐住預期的工作負載。
* **Pipelining**：大量操作時可以用 pipelining 提升效能 —— 多個指令一次送給伺服器，不必每送一個就等回應。
* **連線池**：Redis 客戶端都支援連線池，在高負載環境下特別有助於管理大量連線。
* **非同步操作**：Redis 支援不阻塞客戶端的非同步指令，資源利用更有效率。

---

<!-- 36cb839e5eeb -->
### 7. **Redis 客戶端與整合**

* Redis 可以透過各式官方與社群維護的客戶端和大多數後端技術整合（例如 Java 的 **Jedis** 與 **Lettuce**、Python 的 **redis-py**、Node.js 的 **node-redis**）。
* **連線池**：多數客戶端都支援連線池，以有效率地管理對 Redis 的多條連線。
* **Cluster 客戶端**：要搭配 Redis Cluster 時，有些客戶端支援自動重導到正確的節點。

---

<!-- cc8a5d963ca7 -->
### 8. **Redis 的最佳實務**

* **資料建模**：依使用場景選對資料結構（string、list、set、sorted set 等等）。
* **過期／TTL**：只在一段時間內有意義的 key（例如 session 資料、快取）要設過期時間。
* **避開阻塞指令**：`BLPOP`、`BRPOP`、`BRPOPLPUSH` 這類指令會阻塞客戶端，在高併發情境下可能造成效能問題。
* **備份與持久化**：想清楚持久化（RDB 與 AOF）與效能之間的取捨。
* **複製與高可用**：設定複製或 Redis Sentinel，以達成容錯、並在 failover 期間維持可用。
* **監控與日誌**：用 `MONITOR`、`INFO` 等 Redis 監控工具，以及外部工具（例如 RedisInsight、Prometheus）追蹤效能與記憶體用量。
* **把 Redis 當快取，不要當主資料庫**：Redis 很適合做快取或存放短暫資料，但不該拿來當關鍵資料的最終真相來源（除非持久性本來就是硬性需求）。

---

<!-- 0c9888b626a1 -->
### 9. **安全性考量**

* **認證**：用密碼認證限制對 Redis 的存取（Redis 設定檔裡的 `requirepass`）。
* **網路安全**：用防火牆與虛擬私有網路（VPN）把 Redis 的存取限制在信任的 IP。
* **SSL/TLS 加密**：Redis 支援 SSL/TLS，讓客戶端與伺服器之間的通訊加密，在雲端環境尤其重要。

---

<!-- 9617f2691190 -->
### 結語

對後端工程師來說，懂 Redis 與它的各項功能是必要的，尤其是在處理效能優化、快取、即時分析或分散式系統的時候。Redis 是一個用途廣泛的工具，懂得善用它的各種資料結構與持久化選項，能大幅改善系統的效能與擴展性。
