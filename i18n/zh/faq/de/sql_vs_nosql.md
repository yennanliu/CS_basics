<!-- 81e9500a2fff -->
# SQL vs NoSQL — 資料工程面試深入解析

> **範圍** — 把關聯式資料庫與各個 NoSQL 家族放在一起比較：交易與隔離、儲存引擎、複製與分片、CAP 與一致性模型、每個家族一個代表性的儲存、查詢優先建模、NewSQL，以及在設計題中怎麼選儲存。資料倉儲建模屬於資料建模那份深入解析，不在這裡。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單；[`./data_modeling.md`](./data_modeling.md)、[`./schema_design.md`](./schema_design.md)、[`./sql_for_data.md`](./sql_for_data.md)、[`./kafka.md`](./kafka.md)、[`./stream_pipeline.md`](./stream_pipeline.md)、[`./batch_pipeline.md`](./batch_pipeline.md)、[`./spark.md`](./spark.md)、[`./hadoop.md`](./hadoop.md) —— 其他深入解析；[`../db/faq_DB_DW.md`](../db/faq_DB_DW.md)、[`../redis/redis_info.md`](../redis/redis_info.md) 與 [`../sql/faq_mysql.md`](../sql/faq_mysql.md) —— 關於特定引擎的較早筆記。

每一題都先給你會說出口的那句話，再講機制，最後是面試官下一步會追問的取捨。

---

<!-- 675f3072b25e -->
## 1) 一分鐘答案 ⭐⭐⭐⭐⭐

<!-- 59701e06aa59 -->
### 1-1) 比較表

**SQL 資料庫在單一 primary 或緊密協調的設計上，給你固定的 schema、join 與跨多列的
ACID 交易；NoSQL 資料庫拿其中一部分去換水平擴展、彈性 schema，以及圍繞已知存取模式
設計的資料模型。**

| | SQL（關聯式） | NoSQL |
|---|---|---|
| 資料模型 | 表、列、關聯 | key-value、文件、寬欄、圖、搜尋 |
| Schema | schema-on-write，由資料庫強制 | 彈性；由應用程式強制（或選用的驗證器） |
| 查詢 | SQL、即席 join 與彙總 | 每種儲存各自的 API 或查詢語言；join 通常在應用程式裡做 |
| 交易 | 跨多列、多表的 ACID | 常常只限單一項目或單一分區；有幾個現在提供多項目交易 |
| 擴展 | 先垂直擴展；read replica；分片要手動或外掛 | 依 partition key 水平擴展，內建 |
| 一致性 | 在 primary 上強一致 | 可調，預設常是最終一致 |
| 設計起點 | 實體與它們的關聯 | 應用程式會跑的查詢 |
| 例子 | PostgreSQL、MySQL、Oracle、SQL Server | Redis、DynamoDB、MongoDB、Cassandra、Neo4j、Elasticsearch |

**NewSQL**（Spanner、CockroachDB、TiDB）保留 SQL 與 ACID 並加上水平擴展，
代價是每次寫入都有共識（consensus）延遲。

<!-- 798acbf9a38b -->
### 1-2) 面試官真正在問什麼

「SQL 還是 NoSQL？」很少是在考定義。它在檢查你能不能**說出決定它的那個需求**：

- **跨列的不變量**（餘額不能為負、座位只能賣一次）→ 關聯式交易。
- **未知或即席的查詢**（分析師、報表）→ SQL。
- **寫入量或資料量超過一台機器**，且**查詢是已知的** → 分區的 NoSQL 儲存。
- **邊緣延遲 / 全球分佈** → 多區域的儲存，並說出它的一致性取捨。
- **資料的形狀** —— 圖走訪、全文搜尋、時間序列 —— → 為它而生的儲存。

好的答案結尾會說「而我放棄的是這個」。

---

<!-- 97990849fdda -->
## 2) 關聯式基礎 ⭐⭐⭐⭐⭐

<!-- bd64611c1676 -->
### 2-1) 深入 ACID

**ACID 是交易給你的契約：全有或全無、約束成立、併發交易看不到彼此做到一半的工作，
以及已提交的東西撐得過當機。**

| 字母 | 保證 | 引擎怎麼做到 |
|---|---|---|
| **Atomicity 原子性** | 交易裡的每個陳述句要嘛全部生效，要嘛全不生效 | undo 資訊 / 回滾；log 裡的 commit 紀錄就是開關 |
| **Consistency 一致性** | 宣告的約束（PK、FK、CHECK、UNIQUE）在提交後成立 | 約束檢查；沒宣告的業務規則仍由*應用程式*負責 |
| **Isolation 隔離性** | 併發交易的行為像是以某種順序執行，程度依所選等級 | 鎖和/或 MVCC 快照 |
| **Durability 持久性** | 已提交的交易不會因當機而遺失 | 在 commit 回傳前先把 write-ahead log 刷到磁碟（`fsync`） |

「C」是最弱的一個字母：它的意思是*資料庫不會打破你宣告的規則*，
而不是你的資料是正確的。

<!-- 2bbd128b08d0 -->
### 2-2) 隔離等級與異常 ⭐⭐⭐⭐⭐

**隔離等級是用「允許哪些異常」來定義的；等級越強，異常越少，阻塞或中止越多。**

| 異常 | 發生什麼事 |
|---|---|
| **Dirty read（髒讀）** | 讀到另一個交易尚未提交的寫入 |
| **Non-repeatable read（不可重複讀）** | 同一列讀兩次回傳不同的值 |
| **Phantom read（幻讀）** | 同一個範圍查詢回傳不同的列集合 |
| **Lost update（遺失更新）** | 兩個「讀-改-寫」循環互相覆蓋 |
| **Write skew（寫入偏斜）** | 兩個交易讀到重疊的資料、寫入不相交的列，合起來打破一個不變量 |

| 等級 | 髒讀 | 不可重複讀 | 幻讀 | 備註 |
|---|---|---|---|---|
| Read Uncommitted | 可能 | 可能 | 可能 | 很少用；PostgreSQL 把它當成 Read Committed |
| **Read Committed** | 不會 | 可能 | 可能 | PostgreSQL 與 Oracle 的預設 |
| **Repeatable Read** | 不會 | 不會 | 依 SQL 標準可能 | MySQL InnoDB 的預設；InnoDB 的 next-key lock 會為 locking read 擋住幻讀，PostgreSQL 的快照則防止幻讀 |
| **Serializable** | 不會 | 不會 | 不會 | 也防止 write skew；PostgreSQL 用 SSI（衝突時中止） |

Write skew 的經典例子：兩位值班醫生各自檢查「還有別人在值班嗎？」，都看到有，
於是兩人都下班了。快照隔離允許這種情況；Serializable，或對決策讀到的那些列明確做
`SELECT ... FOR UPDATE`，就能防止它。

<!--CODE-->

<!-- 52442ab2bbcf -->
### 2-3) MVCC 與鎖

**MVCC 為一列保留多個版本，讓讀取者不阻塞寫入者就能看到一致的快照；鎖仍然存在，
讓兩個寫入者不能同時改同一列。**

- **PostgreSQL** —— 每個列版本都帶有建立與刪除它的交易 id（`xmin` / `xmax`）。
  `UPDATE` 會寫出新版本；舊版本由 `VACUUM` 回收。長時間執行的交易會讓舊版本一直活著，
  造成表格膨脹（bloat）。
- **MySQL InnoDB** —— 原地更新，並把舊版本留在 **undo log**；讀取者從它重建快照。
  長交易會讓 undo 歷史一直增長。
- **讀取者不阻塞寫入者，寫入者不阻塞讀取者。** 改同一列的寫入者之間仍會互相阻塞（列鎖）。
- **悲觀鎖** —— `SELECT ... FOR UPDATE`；衝突頻繁時適用。
- **樂觀鎖** —— 寫入時檢查 `version` 欄位
  （`UPDATE ... WHERE id = ? AND version = ?`）；衝突很少時適用。同樣的模式在 NoSQL 中
  以條件式寫入（conditional write）出現。
- **死結** —— 引擎偵測到循環並中止其中一個交易；應用程式必須重試。以一致的順序取得鎖
  可以避免大部分死結。

<!-- 38afd0daf11b -->
### 2-4) Write-ahead log

**Write-ahead log（WAL，在 MySQL 是 redo log）在資料頁寫入之前記錄每個變更，所以當機時
重播 log 就能復原 —— 而同一份 log 也餵給複製與 change data capture。**

<!--CODE-->

- 順序附加 log 很便宜；隨機的頁面寫入被延後並批次處理。
- **複製**就是傳送 log：PostgreSQL streaming replication 傳送 WAL；MySQL 傳送它的
  **binlog**（另一份邏輯 log）。
- **CDC** 工具（Debezium）讀的是同一份 log —— PostgreSQL logical decoding、MySQL
  binlog —— 不用查詢表格就能把列的變更發佈到 Kafka。

---

<!-- a009b9bdcfdd -->
## 3) 儲存引擎：B-tree vs LSM-tree ⭐⭐⭐⭐

<!-- 255c2fee6160 -->
### 3-1) 兩種設計

**B-tree 原地更新頁面，為讀取最佳化；LSM-tree 先把寫入緩衝在記憶體，再刷出排序好的
不可變檔案，讓寫入很便宜，把成本推給讀取與背景的 compaction。**

| | B-tree | LSM-tree |
|---|---|---|
| 寫入路徑 | 找到頁面、原地修改（加上 WAL） | 附加到 WAL + 記憶體中的 **memtable**；刷成排序好的 **SSTable** |
| 讀取路徑 | 一次從根到葉的走訪 | 查 memtable，再從新到舊查 SSTable；**Bloom filter** 跳過檔案 |
| 背景工作 | 頁面分裂、vacuum / purge | **compaction** 合併 SSTable，丟掉被覆寫 / 刪除的 key |
| 擅長 | 點查、範圍讀取、混合 OLTP | 高寫入吞吐、順序 I/O |
| 使用者 | PostgreSQL、MySQL InnoDB、SQL Server、Oracle | Cassandra、RocksDB、LevelDB、HBase、ScyllaDB；DynamoDB 的內部實作沒有公開 |

<!--CODE-->

<!-- f77fa45a4a77 -->
### 3-2) 讀取、寫入與空間放大

**每種儲存引擎都在三種成本之間取捨，而能說出它們的答案聽起來就像有實戰經驗。**

| 放大 | 意義 | B-tree | LSM-tree |
|---|---|---|---|
| **寫入** | 每 byte 使用者資料寫到磁碟的 bytes | 一個小變更就寫整個頁面 | 同一列在每一層 compaction 都被重寫 |
| **讀取** | 每次查找的磁碟讀取次數 | ~ 樹高 | 每次查找要看好幾個 SSTable，靠 Bloom filter 與快取減少 |
| **空間** | 使用的磁碟 vs 有效資料 | 碎片、半滿的頁面 | compaction 前的過時版本與 tombstone |

Compaction 策略是 LSM 的調校旋鈕：**size-tiered**（寫入放大較低、空間較多）vs
**leveled**（讀取與空間放大較低、寫入放大較高）。Cassandra 兩種都提供，
另外還有給時間序列用的 time-window compaction。

---

<!-- fd9b47c2ece2 -->
## 4) 擴展關聯式資料庫 ⭐⭐⭐⭐⭐

<!-- 801a6f2389fd -->
### 4-1) 複製：同步、非同步、read replica

**複製把 primary 的 log 複製到 replica：同步複製在確認提交前等待某個 replica（不遺失、
延遲較高），非同步則不等（快，但 failover 時可能遺失最後幾筆提交）。**

| 模式 | 提交要等 | primary 故障時 |
|---|---|---|
| **非同步** | 只有 primary | 被升級的 replica 上可能少了最近的提交 |
| **半同步**（MySQL） | 至少一個 replica 收到變更 | 幾乎不遺失；逾時就退回非同步 |
| **同步**（PostgreSQL `synchronous_commit` + `synchronous_standby_names`） | 指定的 replica 已確認 | 不遺失；慢的 replica 會拖慢每次提交 |

- **Read replica** 擴展的是讀取，不是寫入 —— 每次寫入仍然進同一個 primary。
- **複製延遲（replication lag）** 會打破 read-your-writes：使用者更新個人資料，下一頁讀
  replica 卻顯示舊值。解法：自己最近的寫入從 primary 讀、把 session 釘在 primary 幾秒，
  或等 replica 追到該次寫入的 log 位置。
- **Failover** 升級一個 replica（Patroni、Orchestrator，或 RDS Multi-AZ 這類託管服務）；
  客戶端透過穩定的 endpoint 重新連線。

<!-- 878f86a9447f -->
### 4-2) 分區 vs 分片

**分區（partitioning）是在一個資料庫內把一張表切成幾塊；分片（sharding）是把資料分散到
不同的資料庫伺服器。分區有助於 pruning 與維護；分片才是擴展寫入的方式。**

| | 分區 | 分片 |
|---|---|---|
| 在哪 | 一台伺服器 | 多台伺服器 |
| 對查詢透明 | 是 —— planner 會做 partition pruning | 否 —— 必須有東西路由每個查詢 |
| 跨片段的交易 | 正常 | 需要分散式交易，或乾脆避免 |
| 例子 | PostgreSQL `PARTITION BY RANGE (created_at)` | MySQL 的 Vitess、PostgreSQL 的 Citus、應用層路由 |

<!--CODE-->

<!-- d510319ee479 -->
### 4-3) 分片策略與它們的陷阱

| 策略 | 怎麼做 | 優點 | 陷阱 |
|---|---|---|---|
| **Range（範圍）** | 每個 shard 一段 key 範圍（`A–F`、`G–M`） | 範圍掃描留在一個 shard | 遞增的 key（時間戳、自增 id）會造成熱點 |
| **Hash（雜湊）** | `hash(key) mod N` | 分佈平均 | 範圍掃描要打到每個 shard；改變 N 會搬動大部分的 key |
| **Consistent hashing（一致性雜湊）** | key 與節點都放在環上，搭配虛擬節點 | 新增一個節點只搬動約 1/N 的 key | 仍然沒有範圍的局部性 |
| **Directory / lookup（目錄）** | 一張表對應 key → shard | 彈性，支援搬移租戶 | 目錄本身是需要保持可用的依賴 |
| **Geo / tenant（地區 / 租戶）** | 依區域或客戶分片 | 資料落地、租戶隔離 | 一個大租戶就變成熱點 shard |

Shard key 決定一切：挑一個每個熱門查詢都會帶到、能分散負載，而且能把同一個交易的列
放在一起的 key（例如 `tenant_id`）。**跨 shard 的 join 與交易就是代價** —— 設計時讓
常見路徑永遠不需要它們。

<!-- b6f6f4fbdfc9 -->
### 4-4) 連線池

**資料庫連線很昂貴 —— 一條 PostgreSQL 連線就是一整個 backend 行程 —— 所以應用程式
共用一小池連線，而不是每個請求開一條。**

- 應用端的連線池（HikariCP、SQLAlchemy 的 pool）在一個服務內重用連線。
- 代理型連線池（PgBouncer、RDS Proxy）在許多應用實例之間共用連線 ——
  在 serverless 或大量 pod 的情況下不可或缺。
- PgBouncer 的 **transaction pooling** 每個交易結束就歸還連線，所以 session 狀態
  （session 層級的 `SET`、advisory lock，以及 —— 在較舊版本中 —— prepared statement）
  不會延續下去。
- 連線越多不等於吞吐越高：超過 core 數之後，競爭只會增加。

---

<!-- 3babee2f7dd5 -->
## 5) 分散式系統理論 ⭐⭐⭐⭐⭐

<!-- e8603927fb69 -->
### 5-1) CAP 與 PACELC

**CAP 說的是：發生網路分區時，分散式儲存必須在一致性（拒絕部分請求）與可用性
（照樣回應，可能是舊資料）之間選擇；PACELC 補充說，即使沒有分區，它也要在延遲與
一致性之間取捨。**

- CAP 的 **C** 指的是 **linearizability（線性一致）**，不是 ACID 的 C。
- 在真實網路中分區不是可選的，所以分區發生時的選擇是 **CP 或 AP**。
- **PACELC** —— 若發生**分區（P）**，選 **A** 或 **C**；**否則（E）**，選**延遲（L）**或**一致性（C）**。

| 儲存 | PACELC | 原因 |
|---|---|---|
| Cassandra | PA/EL（可調） | 任何 replica 都能回應；每個查詢可選更強的等級 |
| DynamoDB | 預設 PA/EL | 除非 `ConsistentRead=true`，否則是最終一致讀 |
| HBase | PC/EC | 每個 region 由一台 RegionServer 服務 |
| Spanner、CockroachDB | PC/EC | 每次寫入都做共識 |

| 儲存 | CAP 傾向 | 備註 |
|---|---|---|
| Cassandra | AP | 每個查詢可調：讀寫都用 `QUORUM`（R + W > N）就得到強一致讀 |
| DynamoDB | AP | 預設是最終一致讀；`ConsistentRead=true` 可在主表或 LSI 上做強一致讀，GSI 永遠不行 |
| MongoDB、HBase | CP | 每個分片 / 區域只有一個 primary |
| Spanner | CP | TrueTime；外部一致性 |

<!-- 0d3efa1b7cc3 -->
### 5-2) 一致性模型

**一致性模型是一道階梯：模型越強，系統的行為越像單一份資料，需要的協調 —— 也就是
延遲 —— 也越多。**

| 模型 | 保證 | 例子 |
|---|---|---|
| **Linearizable（線性一致，強一致）** | 每次讀取都看到最新完成的寫入，依真實時間順序 | Spanner、經由 leader 讀取的 etcd / ZooKeeper |
| **Sequential（順序一致）** | 所有客戶端看到相同的操作順序，但不一定是真實時間順序 | —— 多半只是理論上的一階 |
| **Causal（因果一致）** | 有因果關係的操作依序被看到；併發的操作可能不同 | MongoDB 的 causally consistent session |
| **Read-your-writes（讀己之寫）** | 客戶端一定看得到自己的寫入 | session 黏著、從 primary 讀 |
| **Monotonic reads（單調讀）** | 客戶端永遠不會看到時間倒退 | 把客戶端釘在同一個 replica |
| **Eventual（最終一致）** | 寫入停止後，replica 會收斂 | DynamoDB 預設讀取、Cassandra `ONE` |

面試小技巧：大多數產品需求用 **read-your-writes + monotonic reads** 就能滿足，
成本遠低於 linearizability。

<!-- f5c63331e311 -->
### 5-3) Quorum：R + W > N

**在有 N 個 replica 的 Dynamo 式儲存中，寫入等 W 個確認、讀取問 R 個 replica；若
R + W > N，每次讀取的集合都和最新寫入的集合重疊，所以讀取可以回傳最新的值。**

<!--CODE-->

- 重疊不等於完整的 linearizability：併發寫入、sloppy quorum 與 hinted handoff 仍可能帶來
  意外。衝突用 **last-write-wins** 時間戳解決（Cassandra），或用 version vector /
  應用層合併（原始的 Dynamo 論文）。
- **Read repair** 與 **anti-entropy**（Merkle tree 修復、`nodetool repair`）讓落後的
  replica 跟上。
- `LOCAL_QUORUM` 把 quorum 限在一個資料中心內，所以不用每個請求都付跨區域延遲。

<!-- 3c820acfb24e -->
### 5-4) 一口氣講完共識：Raft 與 Paxos

**共識讓一群節點即使有部分故障，也能對同一份有序 log 達成一致：leader 提出每一筆
紀錄，一旦多數節點存下它就算提交。**

- **Raft** —— 以 term + 多數票選出 leader；leader 複製 log 紀錄；多數確認後該紀錄
  即提交。3 個節點的群組撐得過 1 個故障，5 個節點撐得過 2 個。
- **Paxos / Multi-Paxos** —— 較早、等價的家族；Spanner 每個 split 用一組 Paxos。
- 使用者：etcd 與 Consul（Raft）、ZooKeeper（ZAB，類似的協定）、Kafka KRaft、
  CockroachDB 與 TiKV（每個 range / region 一組 Raft）。
- 代價：每次寫入都要一次多數的來回，所以跨區域部署的群組每次提交都要付跨區域延遲。

---

<!-- cdaf0bc39671 -->
## 6) 深入 NoSQL 家族 ⭐⭐⭐⭐⭐

<!-- c9992e93b698 -->
### 6-1) 總覽

| 家族 | 模型 | 擅長 | 例子 |
|---|---|---|---|
| **Key-value** | `key → value` | 快取、session、計數器、O(1) 查找 | Redis、DynamoDB、Memcached |
| **文件（Document）** | `key → 類 JSON 文件` | 整份讀寫的巢狀實體 | MongoDB、Couchbase、Firestore |
| **寬欄（Wide-column）** | partition key → 排序好的欄位列 | 巨量寫入吞吐、依時間排序的資料 | Cassandra、HBase、Bigtable、ScyllaDB |
| **圖（Graph）** | 節點 + 關係 | 多跳的關係查詢 | Neo4j、Amazon Neptune |
| **搜尋（Search）** | 倒排索引 | 全文、分面搜尋、log 分析 | Elasticsearch、OpenSearch |
| **時間序列（Time-series）** | 依時間分區的量測值 | 監控指標、IoT | InfluxDB、TimescaleDB、Prometheus |

<!-- 0647c2f63c8b -->
### 6-2) Key-value：Redis

**Redis 是記憶體內的資料結構伺服器：資料放在 RAM、命令在單一執行緒上一個接一個執行，
所以每個命令都在毫秒以下。**

| 結構 | 用途 | 命令 |
|---|---|---|
| String | 快取、計數器、鎖 | `GET`、`SET key val EX 60 NX`、`INCR` |
| Hash | 一個物件的欄位 | `HSET`、`HGET`、`HINCRBY` |
| List | 佇列、最近的項目 | `LPUSH`、`RPOP`、`LRANGE` |
| Set | 唯一成員、標籤 | `SADD`、`SISMEMBER`、`SINTER` |
| Sorted set | 排行榜、優先順序、滑動視窗限流 | `ZADD`、`ZRANGE ... REV`、`ZRANGEBYSCORE` |
| Stream | 帶 consumer group 的只附加 log | `XADD`、`XREADGROUP`、`XACK` |
| HyperLogLog | 近似的不重複計數 | `PFADD`、`PFCOUNT` |

- **持久化**：**RDB** 快照（緊湊，可能遺失上次快照之後的寫入）與 **AOF** 只附加檔案
  （`appendfsync everysec` 最多遺失約一秒）。兩者可以同時開。
- 達到 `maxmemory` 時的**淘汰策略**：`allkeys-lru`、`allkeys-lfu`、`volatile-ttl`、`noeviction`……
- **擴展**：replica + **Sentinel** 做 failover；**Redis Cluster** 以 16,384 個 hash slot 分片。
- **交易**：`MULTI` / `EXEC` 把命令排隊並不被插隊地執行，但沒有回滾；Lua script 是原子
  執行的。複製是非同步的，所以已確認的寫入可能在 failover 時遺失。
- 不要把它當成不能遺失之資料的權威儲存 —— 當快取或存放衍生狀態。

<!--CODE-->

更多 Redis 細節見 [`../redis/redis_info.md`](../redis/redis_info.md)。

<!-- b047c2d9aebc -->
### 6-3) Key-value / 文件：DynamoDB ⭐⭐⭐⭐⭐

**DynamoDB 是託管、分區的 key-value 與文件儲存：你選一個 partition key（可再加一個
sort key），所有有效率的存取都要經過這些 key 或某個次要索引。**

- **主鍵** —— 只有 partition key，或 partition key + sort key。partition key 相同的項目
  組成一個 **item collection**，依 sort key 排序，可做範圍查詢（`begins_with`、`between`）。
- **Query vs Scan** —— `Query` 讀一個 partition key 的項目；`Scan` 讀整張表，
  是熱門路徑上要避免的東西。
- **次要索引**：

| | Local secondary index（LSI） | Global secondary index（GSI） |
|---|---|---|
| Key | 相同的 partition key、不同的 sort key | 任意 partition / sort key |
| 建立時機 | 只能在建表時 | 任何時候 |
| 一致性 | 最終一致或強一致（`ConsistentRead=true`） | 只有最終一致 |
| 容量 | 共用主表的 | 自己的 |
| 限制 | 每個 partition key 值的 item collection ≤ 10 GB | —— |

- **一致性** —— 讀取預設是最終一致；`ConsistentRead=true` 在主表或 LSI 上提供強一致讀，
  讀取容量成本是兩倍。
- **容量模式** —— **on-demand**（依請求付費）或 **provisioned**（RCU / WCU，可搭配
  auto scaling）。被節流時會出現 `ProvisionedThroughputExceededException`。
- **熱點分區** —— 單一 partition key 值有吞吐上限；把熱門 key 分散（例如加上後綴），
  並避免低基數的 partition key。
- **交易** —— `TransactWriteItems` / `TransactGetItems` 讓最多 100 個動作跨項目、跨表
  原子化。
- **條件式寫入**（`ConditionExpression`）提供樂觀鎖與「不存在才建立」。
- **TTL** 讓項目過期；**DynamoDB Streams** 發佈項目變更，用於 CDC。

<!-- e647d9c6b6a2 -->
### 6-4) DynamoDB single-table design

**Single-table design 把多種實體類型存在同一張表，用通用的 `PK` / `SK` 屬性，讓一次
`Query` 就把一個實體和它相關的項目一起取回。**

<!--CODE-->

- 先列出每一種存取模式；每一種都必須對應到主表或某個 GSI 上的 key 條件。
- 取捨：新的存取模式可能需要新的 GSI 或回補，而即席分析應該放在由 DynamoDB Streams
  或匯出到 S3 餵養的資料倉儲裡。

<!-- cc4838f6f581 -->
### 6-5) 文件：MongoDB

**MongoDB 把 BSON 文件存在 collection 裡；一起讀取的資料就嵌入（embed），
被共用或無限增長的資料就參照（reference）。**

<!--CODE-->

| 何時嵌入 | 何時參照 |
|---|---|
| 一起讀取、由父文件擁有（訂單 → 明細） | 被很多父文件共用（商品、客戶） |
| 大小有上限 | 無限增長（留言、事件） |
| 一起更新 | 各自獨立且經常更新 |

- **文件上限是 16 MB** —— 無限增長的嵌入陣列是經典錯誤。
- **Replica set** —— 一個 primary，secondary 複製 oplog；故障時自動選舉。
  `writeConcern: "majority"` 與 `readConcern: "majority"` 提供持久性，並避免讀到被回滾的
  寫入；`readPreference` 決定 secondary 是否服務讀取。
- **分片** —— shard key（範圍或雜湊）把一個 collection 切成 chunk 分散到各 shard，
  由 `mongos` 路由。單調遞增的 shard key（像 `createdAt`）會讓每次插入都進同一個 shard。
- **多文件 ACID 交易**是存在的（replica set 自 4.0、sharded cluster 自 4.2），但成本較高；
  一個到處都需要它的 schema，大概本來就該用關聯式資料庫。
- **Aggregation pipeline**（`$match`、`$group`、`$lookup`）涵蓋分析類查詢；
  `$lookup` 就是 join，但不適合在熱門路徑上大規模依賴。
- 可以用 `$jsonSchema` 驗證器做 schema 驗證。

<!-- 27592ab2215c -->
### 6-6) 寬欄：Cassandra ⭐⭐⭐⭐

**Cassandra 是無主（masterless）、分區、有副本的寬欄儲存，為高寫入吞吐而生：partition
key 決定節點，clustering key 決定分區內列的排序，而每個查詢都必須指定 partition key。**

- **環（Ring）** —— 一致性雜湊（Murmur3 token、虛擬節點）；每個節點地位相同，沒有單一
  primary。Replication factor（RF）決定每個分區由幾個節點持有；
  `NetworkTopologyStrategy` 依資料中心放置副本。
- **寫入路徑** —— commit log + memtable → 刷成 SSTable → compaction（一個 LSM-tree，見 3-1）。
  寫入快又便宜；更新只是一次較新的寫入。
- 每個查詢可調的**一致性**：`ONE`、`QUORUM`、`LOCAL_QUORUM`、`ALL`（見 5-3）。
- **Tombstone** —— 刪除會寫入一個標記，存活 `gc_grace_seconds`（預設 10 天），
  讓修復能傳播它。像佇列那樣「插入再刪除」的模式會堆積 tombstone，讓讀取變慢甚至失敗。
- **Lightweight transaction** —— `INSERT ... IF NOT EXISTS`、`UPDATE ... IF col = ?` 使用 Paxos；
  它們很慢，所以別放在熱門路徑上。
- **沒有 join、沒有即席過濾** —— 對非 key 欄位下 `WHERE` 需要 `ALLOW FILTERING`
  （一次掃描）或次要索引；正常的答案是再建一張表。
- **分區大小** —— 讓分區有上限（在 key 中加入時間桶），否則一個 key 會變成又熱又大的分區。

<!-- 8beaf2ea3c29 -->
### 6-7) 圖：Neo4j

**圖資料庫把關係存成一等公民的紀錄，所以多跳走訪是跟著指標走，而不是每一跳跑一次 join。**

<!--CODE-->

- 擅長：推薦、詐騙集團偵測、網路 / 依賴分析、知識圖譜。
- 不擅長：對所有節點做大量彙總，以及必須分片的超大圖。
- SQL 的遞迴 CTE 可以做 2–3 跳；當深度不固定或很深時，圖資料庫才划算。

<!-- 577980222c49 -->
### 6-8) 搜尋：Elasticsearch

**Elasticsearch 是建立在 Lucene 上的分散式搜尋引擎：倒排索引把每個詞對應到含有它的文件，
所以帶相關性評分的全文查詢很快。**

<!--CODE-->

- 一個 **index** 切成數個 **primary shard**（建立時固定），並帶有 **replica**。
- **近即時（near-real-time）**：文件在 refresh 之後才可被搜尋（預設 1 秒）。
- Mapping 型別很重要：`text`（經過分析、全文）vs `keyword`（精確、可彙總）。
- 不是主要資料庫：沒有多文件交易，而改 mapping 通常意味著要重建索引（reindex）。
  透過 CDC 從權威系統餵資料給它。

---

<!-- 1ecdd6fbccc3 -->
## 7) 查詢優先建模 ⭐⭐⭐⭐⭐

<!-- 2c89d6fe6dcf -->
### 7-1) 實體優先 vs 查詢優先

**關聯式 schema 從實體出發並正規化；NoSQL schema 從查詢出發並反正規化，每種存取模式
一張表（或一個索引），並選好 partition key，讓每個查詢只打到一個分區。**

| 步驟 | 關聯式 | NoSQL（Cassandra / DynamoDB） |
|---|---|---|
| 1 | 列出實體與關係 | 列出每一個查詢，以及它的過濾條件與排序 |
| 2 | 正規化到 3NF | 為每個查詢挑一個能分散負載的 partition key |
| 3 | 為慢的查詢加索引 | 為排序與範圍挑 clustering / sort key |
| 4 | 任何新查詢都只是 SQL | 新查詢可能需要新表或新索引 |
| 寫入 | 每個事實只寫一處 | 同一個事實寫進好幾張表 |

<!-- 84b881397fc8 -->
### 7-2) 一個完整的 Cassandra 例子

**題目：聊天 app 需要「某聊天室的最新訊息」與「某使用者發出的訊息」。**

<!--CODE-->

- **Partition key** `(room_id, bucket)` —— 分散負載，並以月為單位限制每個分區的大小。
- **Clustering key** `sent_at DESC` —— 最新的在前，所以「最新 50 則」是一次順序讀取。
- 寫一則訊息就要寫兩張表（logged `BATCH` 能讓它們保持一致，但有成本）——
  反正規化就是單分區讀取的代價。

---

<!-- 02d7db1f1405 -->
## 8) NewSQL 與分析型儲存 ⭐⭐⭐⭐

<!-- 299bbe4985c4 -->
### 8-1) NewSQL：Spanner、CockroachDB、TiDB

**NewSQL 資料庫把資料切成 range，每個 range 用共識做複製，再在上面跑分散式交易 ——
兼具 SQL、ACID 與水平擴展。**

| | Google Spanner | CockroachDB | TiDB |
|---|---|---|---|
| 協定 / 方言 | GoogleSQL 與 PostgreSQL 方言 | 相容 PostgreSQL | 相容 MySQL |
| 複製 | 每個 split 一組 Paxos | 每個 range 一組 Raft | 每個 region 一組 Raft（TiKV） |
| 時間 / 排序 | TrueTime（GPS + 原子鐘，有界的不確定性） | hybrid logical clock | 中央的 timestamp oracle（PD） |
| 預設隔離 | 外部一致性（strict serializability） | Serializable | 快照隔離（稱為 Repeatable Read） |
| 分析 | —— | —— | TiFlash 欄式副本（HTAP） |

- 代價：每次寫入都是一輪共識，所以多區域寫入要付跨區域延遲。
- 當你需要 SQL + ACID **而且**已經超出單一 primary 的能力，或需要強一致的多區域寫入時，
  才選它。

<!-- 0dd42e5b915b -->
### 8-2) OLTP 儲存 vs OLAP 儲存

**OLTP 儲存用列式儲存服務大量依 key 的小讀寫；OLAP 儲存用欄式儲存服務大型掃描與彙總 ——
資料工程師通常就是把資料從前者搬到後者。**

| | OLTP（PostgreSQL、MySQL、DynamoDB） | OLAP（BigQuery、Snowflake、Redshift、ClickHouse） |
|---|---|---|
| 儲存 | 列式（row-oriented） | 欄式（column-oriented），高度壓縮 |
| 查詢 | 依 key 點查、小範圍 | 掃描數十億列中的少數欄位 |
| 併發 | 數千個短交易 | 數十到數百個重量級查詢 |
| 更新 | 持續、列層級 | 批次載入、merge、大多是附加 |
| 索引 | key 上的 B-tree | partition pruning、sort key、zone map |

在 OLTP primary 上跑分析會搶走它的 I/O，還可能鎖住表或讓表膨脹；改為複製到資料倉儲
（見 [`../db/faq_DB_DW.md`](../db/faq_DB_DW.md)）。

<!-- 9ad7e7c625ce -->
### 8-3) 多語持久化與儲存之間的 CDC

**大多數真實系統都用好幾種儲存，各用在它最擅長的地方，並透過串流權威系統的變更來保持
同步 —— 而不是讓應用程式寫進每一個儲存。**

<!--CODE-->

- **雙寫（dual write）**（應用程式先寫 DB 再寫搜尋索引）在任何部分失敗時都會漂移。
  優先用 CDC，或 **transactional outbox**：在同一個 DB 交易中把事件寫進 `outbox` 表，
  再串流那張表。
- 下游儲存與來源之間是最終一致的；設計能容忍延遲的讀取。
- 只有一個儲存是**事實來源（source of truth）**；其他的都可以從它重建。

---

<!-- 4406b23bcabd -->
## 9) 選擇儲存 ⭐⭐⭐⭐⭐

<!-- e02155cc6d45 -->
### 9-1) 決策表

| 需求 | 選擇 |
|---|---|
| 金流、庫存、跨多列的不變量 | SQL（PostgreSQL / MySQL） |
| 即席分析與 join | SQL 資料倉儲（BigQuery、Snowflake） |
| 極高寫入吞吐、查詢已知、多區域 | 寬欄（Cassandra）/ DynamoDB |
| 低延遲快取、計數器、限流 | Redis |
| 彈性的巢狀文件 | MongoDB / Firestore |
| 關係走訪 | 圖資料庫 |
| 全文與分面搜尋 | Elasticsearch / OpenSearch |
| 監控指標與時間序列 | 時間序列資料庫，或搭配時間桶的 Cassandra |
| 全球規模**且**要 SQL + ACID | Spanner / CockroachDB |

預設答案：**「除非某個需求排除它，否則先用 PostgreSQL」** —— 然後說出那個需求
（寫入量、延遲、schema 多變、全球分佈）以及能滿足它的儲存。

<!-- 58e95c6298d1 -->
### 9-2) 選擇之前要問的問題

1. **查詢是什麼？** 已知且少 → NoSQL 可行。即席 → SQL。
2. **有哪些跨列的不變量？** 只要有 → 需要交易；優先用關聯式或 NewSQL。
3. **讀寫速率多少？一年後資料量多大？** 一個 primary（加上 replica）撐得住嗎？
   撐得住的話，關聯式比較簡單。
4. **要多低的延遲？從哪裡存取？** 單一區域 vs 全球使用者。
5. **每次讀取需要什麼一致性？** 金流要強一致，個人資料要 read-your-writes，
   動態牆可以最終一致。
6. **誰來維運？** 託管（DynamoDB、Cloud SQL、Atlas）vs 自架（Cassandra）常常是決定性的成本。

---

<!-- fe1d37760549 -->
## 10) 設計題實戰 ⭐⭐⭐⭐

<!-- a57b5e8d3028 -->
### 10-1) 聊天訊息

**Cassandra（或 Bigtable / ScyllaDB 這類寬欄儲存），依對話與時間桶分區。**

- 寫入量巨大、幾乎只附加、讀法是「某對話最新 N 則」—— 一次 clustering key 的範圍讀取。
- 分區 `(conversation_id, bucket)`，依 `message_id`（以時間為基礎的 id）遞減排序。
- 未讀數與在線狀態放 Redis；訊息搜尋透過 CDC 放進 Elasticsearch。
- 取捨：沒有即席查詢；每種新的存取模式都是一張新表。

<!-- 1103ac339d6d -->
### 10-2) 購物車與結帳

**購物車可以放在 key-value 儲存；結帳必須是關聯式的。**

- **購物車**：以 `user_id` 為 key 的 DynamoDB 或 Redis —— 單一 key 的讀寫，高可用比嚴格
  一致更重要，而且購物車遺失只是麻煩，不是災難。
- **訂單 + 付款 + 庫存**：在 PostgreSQL / MySQL 的同一個交易裡完成，用唯一約束或條件式
  更新，讓庫存不會變負、訂單不會被重複扣款。
- 交接：結帳時讀取購物車，再帶著 **idempotency key** 執行關聯式交易，
  讓重試的請求不會建立第二筆訂單。

<!-- 1b5c3a5e5f0d -->
### 10-3) 使用者個人資料

**先用 PostgreSQL；如果每個使用者的資料形狀差異很大，再考慮文件儲存。**

- 讀多寫少、以 `user_id` 為 key、大小適中 —— 一張關聯式表加上 read replica 與前面的
  快取，就足以服務非常大的使用者量。
- PostgreSQL 的 `JSONB` 能處理選填、多變的屬性，不用離開 SQL。
- Read-your-writes 很重要（使用者會注意到過時的個人資料）：更新後從 primary 讀，
  或寫入時讓快取失效。
- 全球規模且需要多區域寫入時：DynamoDB global tables 或 Spanner —— 並說出各自提供的一致性。

<!-- 234eec1c6f6a -->
### 10-4) 時間序列指標

**時間序列資料庫（監控用 Prometheus，應用指標用 InfluxDB / TimescaleDB），
規模非常大時用依時間桶分區的 Cassandra / Bigtable。**

- 只附加、依時間順序寫入，讀法是範圍讀取與降採樣的彙總。
- 依 `(metric, series, time bucket)` 分區；原始資料保留幾天，較長的保留期則彙總成
  分鐘 / 小時粒度。
- 長期分析放到欄式檔案（Parquet）或 ClickHouse。
- 陷阱：高基數的 label（每個指標都帶 `user_id` label）會讓 series 數量爆炸。

---

<!-- 5d992553004f -->
## 11) 常見誤解 ⭐⭐⭐⭐

<!-- 2b4f93b67dc1 -->
### 11-1) 「NoSQL 沒有 schema」

**NoSQL 沒有*由資料庫強制的* schema；schema 依然存在 —— 存在於讀取資料的應用程式碼裡 ——
所以它是 schema-on-read，而不是沒有 schema。**

- 每個讀取者都必須處理曾經寫入過的每一種文件版本。
- Schema 變更變成應用程式的遷移：兩種形狀都寫、回補，然後停止讀舊的。
- MongoDB 的 `$jsonSchema` 驗證器與 Cassandra 有型別的 CQL 表確實會強制結構。

<!-- 9d29641c7207 -->
### 11-2) 「NoSQL 不能做交易」

**許多 NoSQL 儲存現在都支援交易 —— 只是範圍比關聯式資料庫窄，或成本比較高。**

| 儲存 | 交易 |
|---|---|
| MongoDB | 多文件 ACID 交易（replica set 4.0+、sharded cluster 4.2+） |
| DynamoDB | `TransactWriteItems` / `TransactGetItems`，最多 100 個項目、可跨表 |
| Cassandra | lightweight transaction（單一分區上的 compare-and-set，透過 Paxos） |
| Redis | `MULTI` / `EXEC`（沒有回滾）與原子的 Lua script |

<!-- a19a916041a5 -->
### 11-3) 其他值得糾正的誤解

- **「SQL 無法擴展。」** 關聯式資料庫用 replica 擴展讀取，用分片（Vitess、Citus）或
  NewSQL 擴展寫入；大多數產品永遠不會超出一台調校良好的 primary。
- **「NoSQL 一定比較快。」** 它只對當初建模的存取模式快；沒規劃到的查詢可能就是全表掃描。
- **「最終一致代表資料是錯的。」** 它代表 replica 可能短暫落後；用對的讀取方式
  （quorum、`ConsistentRead`、從 primary 讀），在重要的地方就能得到強一致讀。
- **「CAP 就是三選二。」** 分區容忍不是可選的；要選的是分區*發生時*怎麼做。

---

<!-- 4d2161a2a4ae -->
## 12) 快問快答

<!-- f80ee1657513 -->
### 12-1) 快速問答

- **Q：為什麼不能靠加 read replica 來擴展寫入？** —— 每次寫入仍然進單一 primary；
  replica 只增加讀取容量。寫入要靠分片擴展。
- **Q：怎樣才是好的 partition key？** —— 高基數、存取平均，並出現在每個熱門查詢中；
  它應該讓一個請求的資料落在一個分區。
- **Q：為什麼 Cassandra 的刪除很貴？** —— 刪除是一次 tombstone 寫入，讀取必須跳過它，
  直到 `gc_grace_seconds` 之後的 compaction 才清掉。
- **Q：GSI 讀取什麼時候會是舊的？** —— 隨時都可能：GSI 是非同步更新的，
  只支援最終一致讀。
- **Q：怎麼防止兩個使用者訂到同一個座位？** —— 關聯式資料庫用唯一約束或
  `UPDATE ... WHERE status = 'free'`；DynamoDB 用條件式寫入（`attribute_not_exists`）。
- **Q：寫入很重的事件 log 要用 B-tree 還是 LSM？** —— LSM：順序寫入、compaction 在背景
  進行；代價在讀取。
- **Q：什麼是複製延遲？怎麼對使用者隱藏它？** —— replica 較晚才套用 primary 的 log；
  使用者剛寫完之後，把他的讀取導到 primary。
- **Q：怎麼讓搜尋索引和資料庫保持同步？** —— 從資料庫 log 做 CDC（Debezium → Kafka →
  indexer），不要雙寫。
- **Q：`R + W > N` 換到了什麼？** —— 讀寫集合會重疊，所以讀取看得到最新已確認的寫入；
  它本身並不提供 linearizability。
- **Q：NewSQL 資料庫在哪裡為它的保證付出代價？** —— 每次寫入的共識延遲，跨區域時最嚴重。
