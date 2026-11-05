<!-- 658eeb21de0d -->
# 資料工程師的 Schema 設計

> **範圍** — 資料系統的實體 schema 設計：怎麼回答 schema 設計題、OLTP 資料表、約束與索引、鍵與 ID 生成、資料倉儲的分區 / clustering / 分佈、資料型別、NULL 與命名慣例、軟刪除與多租戶、schema 演進、事件 schema，以及 PostgreSQL、BigQuery 與 Spark / Delta 的完整 DDL 範例。不涵蓋邏輯層的維度建模（star schema、SCD）與 NoSQL 的查詢優先建模。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) — 總覽與考前檢查清單；[`data_modeling.md`](./data_modeling.md) — 事實、維度、粒度與 SCD；[`sql_vs_nosql.md`](./sql_vs_nosql.md) — 查詢優先的 NoSQL 建模；[`sql_for_data.md`](./sql_for_data.md)、[`batch_pipeline.md`](./batch_pipeline.md)、[`stream_pipeline.md`](./stream_pipeline.md)、[`spark.md`](./spark.md)、[`kafka.md`](./kafka.md)、[`hadoop.md`](./hadoop.md) — 其他深入專題。

每個 DDL 區塊都在第一行註解標明方言。各引擎在語法上的差異比 SQL 其他任何地方都大，
把 `CLUSTER BY` 寫成 `CLUSTERED BY` 這種事，面試官一定會注意到。

---

<!-- 3b4dd236901b -->
## 1) 怎麼回答 Schema 設計題 ⭐⭐⭐⭐⭐

<!-- 849e0dd64e03 -->
### 1-1) 方法

**Schema 設計要從查詢往回推：一列代表什麼、資料怎麼被讀寫、之後會怎麼變 —— DDL 最後才寫。**

<!--CODE-->

把這些步驟說出口。面試官評分時看順序和看資料表一樣重：一個還沒問資料怎麼被查就先寫
`CREATE TABLE` 的候選人，等於什麼都沒有設計。

<!-- 58fad88f9daa -->
### 1-2) 寫 DDL 之前要問的問題

| 要問 | 為什麼會改變設計 |
|---|---|
| OLTP 還是分析？ | 正規化 + 索引 vs 反正規化 + 分區 |
| 讀寫比例與資料量 | 索引數量、分區方式、要不要分片 |
| 最熱門的查詢與它的過濾條件 | 主鍵、分區欄位與 clustering 欄位 |
| 新鮮度 | 批次表 vs 串流 upsert vs 只附加的事件 |
| 歷史重要嗎？ | 覆寫 vs 歷史表 vs SCD2（見 [`data_modeling.md`](./data_modeling.md)） |
| 還有誰會讀？ | 演進規則要多嚴格 |
| 保留期限與隱私 | 分區過期、PII 欄位、刪除請求 |

---

<!-- a943700f16aa -->
## 2) OLTP Schema 設計 ⭐⭐⭐⭐⭐

<!-- 0469c708b596 -->
### 2-1) 實務上的正規化

**先正規化到第三正規化，讓每個事實只存一次；再針對實際量測過的讀取路徑刻意地反正規化。**

- **1NF** —— 欄位是原子的，沒有重複群組（`phone1, phone2, phone3` → 一張 `phones` 表）。
- **2NF** —— 沒有非鍵欄位只依賴複合鍵的一部分。
- **3NF** —— 沒有非鍵欄位依賴另一個非鍵欄位（`zip → city`）。
- **刻意的反正規化** —— 當某個值必須在寫入當下凍結時就複製它（`order_line.unit_price`，
  因為商品價格之後會變），或是熱門讀取路徑每次請求都要付出一次 join 的代價時
  （快取的 `orders.item_count`）。

凍結的那份複本不是冗餘：訂單成交的價格，和商品目前的價格是不同的事實。

<!-- 2d2cd274697f -->
### 2-2) 約束

**約束是 schema 可執行的文件：資料庫當場拒絕一筆壞資料，而不是等管線一週後才發現。**

| 約束 | 保證 | 備註 |
|---|---|---|
| `PRIMARY KEY` | 唯一 + 非 null，每張表一個 | 會建立唯一索引 |
| `FOREIGN KEY` | 被參照的那一列存在 | PostgreSQL **不會**幫你替參照端的欄位建索引 |
| `UNIQUE` | 沒有重複值 | PostgreSQL 允許多個 `NULL`，除非用 `NULLS NOT DISTINCT`（15+） |
| `NOT NULL` | 一定有值 | 最便宜的約束；預設就該用 |
| `CHECK` | 列層級的條件成立 | `amount >= 0`、`status IN (...)` |
| `DEFAULT` | 沒給值時的值 | `created_at DEFAULT now()` |

資料倉儲不一樣：BigQuery、Snowflake、Redshift 接受 `PRIMARY KEY` / `FOREIGN KEY` 的宣告，
但**不會強制執行**（它們只是給最佳化器的提示），所以在那裡唯一性是一項資料品質測試，
不是保證。

<!-- 7b53a79821fa -->
### 2-3) 索引 ⭐⭐⭐⭐⭐

**索引是部分欄位排序後的複本，加上指向原列的指標；它把全表掃描變成樹狀查找，而每一次寫入
都要付出維護它的代價。**

| 索引 | 用途 |
|---|---|
| **B-tree**（預設） | 等值、範圍、`ORDER BY`、前綴 `LIKE 'abc%'` |
| **複合索引** `(a, b, c)` | 過濾條件是最左前綴的查詢：`a`、`a + b`、`a + b + c` |
| **覆蓋索引**（`INCLUDE`） | 只靠索引就能回答查詢（index-only scan） |
| **部分索引**（`WHERE ...`） | 只索引熱門查詢會碰到的那些列 |
| **Hash** | 只有等值 |
| **GIN** | `jsonb` 包含查詢、陣列、全文搜尋 |
| **BRIN** | 非常大且天然有序的表（只附加的時間序列） |

**複合索引的欄位順序** —— 最左前綴規則：`(customer_id, created_at)` 上的索引能服務
`WHERE customer_id = ?` 和 `WHERE customer_id = ? AND created_at > ?`，但不能單獨服務
`WHERE created_at > ?`。等值欄位放前面，範圍或排序欄位放最後。

<!--CODE-->

用 `EXPLAIN (ANALYZE, BUFFERS)` 確認索引有被用到；如果一個選擇性高的過濾條件在計畫裡
顯示 `Seq Scan`，代表索引不存在、無法使用（欄位外面包了函式），或統計資訊過期了。

<!-- 431f277465af -->
### 2-4) 索引什麼時候會傷人

- **每次寫入都要維護每一個索引** —— 對一張有八個索引的表插入一列，等於寫九次。
  寫入密集的表只留少數索引。
- **選擇性低** —— 布林值或只有三種值的 `status` 上的索引很少有幫助，除非它是只涵蓋稀有值的
  部分索引。
- **函式會讓索引失效** —— `WHERE lower(email) = ?` 需要 `lower(email)` 的表達式索引；
  `WHERE created_at::date = ?` 應該改寫成範圍條件。
- **重複的前綴** —— 有了 `(a, b)`，`(a)` 就是多餘的。
- **在線上的表建索引** —— 一般的 `CREATE INDEX` 會擋住寫入；PostgreSQL 要用
  `CREATE INDEX CONCURRENTLY`。
- **大量載入** —— 先刪除次要索引、載入、再重建；通常比逐列維護它們快。

---

<!-- 02930b88e6c3 -->
## 3) 鍵與 ID 生成 ⭐⭐⭐⭐

<!-- dd8d290be142 -->
### 3-1) 自然鍵 vs 代理鍵

**用代理鍵當主鍵，自然鍵保留成一個唯一欄位 —— 代理鍵永遠不變，自然鍵是人們拿來查找的東西。**

| | 自然鍵 | 代理鍵 |
|---|---|---|
| 是什麼 | 業務識別碼（`email`、`order_no`、`isbn`） | 沒有意義的生成 id |
| 優點 | 有意義、不用查表、天然可去重 | 穩定、精簡、撐得過業務變動 |
| 缺點 | 可能改變（`email`）、跨來源系統可能衝突 | 需要生成步驟；防重複要另外加唯一約束 |
| 適合 | 小型參照表（`country_code`、`currency_code`） | 其他幾乎所有情況 |

在資料倉儲的維度表裡，代理鍵還替 SCD2 的每個版本編號，所以事實表存的是 `customer_sk`
而不是 `customer_id` —— 見 [`data_modeling.md`](./data_modeling.md)。

<!-- 97798d9a4960 -->
### 3-2) ID 生成策略

**連續 id 精簡且對索引友善，但需要一個協調者；隨機 UUID 不需要協調者，卻把插入打散到整個
索引；依時間排序的 id 兩者兼得。**

| 策略 | 大小 | 有序 | 協調 | 要注意 |
|---|---|---|---|---|
| Auto-increment / `IDENTITY` | 8 bytes（`BIGINT`） | 是 | 資料庫 | 只有一個寫入者；id 洩漏業務量；跨分片合併會衝突 |
| **UUIDv4** | 16 bytes | 否 | 不需要 | 隨機插入讓 B-tree 碎片化、降低快取局部性 |
| **UUIDv7**（RFC 9562） | 16 bytes | 依時間（毫秒） | 不需要 | 洩漏建立時間 |
| **ULID** | 16 bytes | 依時間（毫秒） | 不需要 | 多數資料庫沒有原生型別 |
| **Snowflake id** | 8 bytes | 依時間 | 每個生成器要有唯一的 worker id | 時鐘倒退；worker id 的分配 |
| **自然鍵的雜湊** | 8–32 bytes | 否 | 不需要 | 位數短時會碰撞；自然鍵變了它也會變 |

- Snowflake id 把毫秒時間戳、worker id 與每毫秒的序號塞進一個 64 位元整數
  （Twitter 的配置：41 / 10 / 12 位元，再加一個符號位元）。
- **確定性 id** —— 自然鍵的雜湊（`md5(source || ':' || order_no)`）讓重跑產生相同的鍵，
  使管線保持冪等。
- PostgreSQL 從 13 起內建 `gen_random_uuid()`（v4），18 起有 `uuidv7()`；兩者都存在原生的
  `uuid` 型別裡，永遠不要存成 `text`。

---

<!-- 85983605d49d -->
## 4) 資料倉儲的實體設計 ⭐⭐⭐⭐⭐

<!-- 917ac2855b32 -->
### 4-1) 分區

**依幾乎每個查詢都會過濾的欄位分區 —— 通常是事件日期 —— 讓查詢只讀它需要的分區，
舊資料也能一個分區一個分區地刪掉。**

- **選擇**：低基數、永遠會被過濾的欄位：`event_date`，有時是 `region`。
- **大小**：分區要維持夠大。幾千個小分區在中介資料與檔案列舉上的成本，比 pruning 省下的還多。
- **Pruning 需要直接的過濾條件** —— `WHERE event_date = '2026-11-01'` 會 prune；
  在依 `event_date` 分區的表上寫 `WHERE DATE(event_ts) = ...` 則不會。
- **生命週期** —— 以整個分區為單位過期或封存（BigQuery 的 `partition_expiration_days`、
  PostgreSQL / Hive 的 `DROP PARTITION`）。
- **防呆** —— BigQuery 的 `require_partition_filter = TRUE` 會拒絕掃描所有分區的查詢。
- **Iceberg 隱藏分區** —— `PARTITIONED BY (days(event_ts))` 從時間戳推導出分區，
  寫入者與讀取者都不用處理另外一個日期欄位。

<!-- a553bf77a7ea -->
### 4-2) Clustering 與 sort key

**Clustering 在每個分區內排序資料，讓引擎能用 min/max 統計跳過區塊；挑在分區欄位之後，
查詢最常過濾或 join 的欄位。**

| 引擎 | 功能 | 限制 / 備註 |
|---|---|---|
| BigQuery | `CLUSTER BY` | 最多 4 個欄位；順序有影響（第一個最重要） |
| Snowflake | 自動 micro-partition + 選用的 clustering key | 重新 cluster 會花 credit |
| Redshift | `SORTKEY`（compound 或 interleaved） | compound 偏重它的第一個欄位 |
| Delta Lake | `OPTIMIZE ... ZORDER BY`，或 liquid clustering `CLUSTER BY` | 該表用了 liquid clustering 就取代 `PARTITIONED BY` |
| Iceberg | 排序順序（`WRITE ORDERED BY`） | 在資料寫入或重寫時套用 |

經驗法則：依時間分區，再依接下來一到三個最常被過濾的欄位 cluster（`customer_id`、
`country`），價值最高的過濾欄位放第一。

<!-- 4c2fcd291861 -->
### 4-3) Bucketing、Z-order 與 liquid clustering

- **Bucketing**（Hive / Spark）—— 依 key 把列雜湊到固定數量的檔案
  （`CLUSTERED BY (user_id) INTO 64 BUCKETS`）。兩張以相同方式依 join key 做 bucketing 的表，
  join 時不需要 shuffle。Bucket 數在建表時就固定了。
- **Z-order** —— 交錯多個欄位的位元，讓在這些欄位上都相近的列落在同一批檔案；
  適合查詢會過濾不同欄位的情況。
- **Liquid clustering**（Delta）—— 增量式的 clustering，之後可以更換 clustering key 而不必
  重寫整張表；同一張表不能和 `PARTITIONED BY` 並用。

注意名稱撞車：Hive / Spark 的 `CLUSTERED BY ... INTO n BUCKETS` 是 bucketing；
BigQuery 或 Delta 的 `CLUSTER BY` 是基於排序的 clustering。

<!-- 2b0be910b184 -->
### 4-4) Redshift 的分佈方式

**在 MPP 資料倉儲裡，分佈鍵決定每一列放在哪個節點；把兩張大表依它們的 join key 分佈，
join 就在本地發生，不必走網路。**

| `DISTSTYLE` | 列會放到 | 用於 |
|---|---|---|
| `AUTO`（預設） | 由 Redshift 決定並調整 | 起點 |
| `KEY` | 依 `DISTKEY` 雜湊選出的節點 | 依該 key join 的大事實表 + 大維度表 |
| `ALL` | 每個節點一份完整複本 | 小而變動慢的維度表 |
| `EVEN` | 輪流分配 | 沒有主要 join 的表 |

傾斜的 `DISTKEY`（大部分列共用同一個值）會把大部分工作壓在一個節點上 —— 和 Spark 的資料
傾斜是同一種失敗。

---

<!-- 86e62a8012c1 -->
## 5) 資料型別 ⭐⭐⭐⭐

<!-- 33ada1152e95 -->
### 5-1) 金額與數字

**金額存成精確小數或整數最小單位 —— 永遠不要用二進位浮點數，它無法精確表示 0.10。**

| 選擇 | 例子 | 備註 |
|---|---|---|
| `NUMERIC(12, 2)` / `DECIMAL` | `19.99` | 精確；標準答案 |
| 整數最小單位 | `BIGINT` 存 `1999` 分 | 快又精確；各幣別的最小單位不同（JPY 沒有小數，BHD 有 3 位） |
| `FLOAT` / `DOUBLE` | — | 用在量測值與比率，永遠不用在金額 |
| PostgreSQL `money` | — | 格式依 locale 而定；避免 |

金額旁邊一定要存幣別（`currency_code CHAR(3)`，ISO 4217）。

<!-- b242b90ac16b -->
### 5-2) 時間戳與時區 ⭐⭐⭐⭐⭐

**時間點以 UTC 存在有時區意識的型別裡，報表用的當地日期再推導出來；沒有時區的時間戳只是
牆上時鐘的讀數，不是一個時間點。**

| 引擎 | 時間點（絕對） | 牆上時鐘（無時區） |
|---|---|---|
| PostgreSQL | `timestamptz` —— 以 UTC 儲存，依 session 時區顯示 | `timestamp` |
| BigQuery | `TIMESTAMP` | `DATETIME` |
| Spark 3.4+ | `TIMESTAMP`（= `TIMESTAMP_LTZ`，session 時區） | `TIMESTAMP_NTZ` |
| Snowflake | `TIMESTAMP_TZ` / `TIMESTAMP_LTZ` | `TIMESTAMP_NTZ` |

- **事件時間**（發生的時間）與**擷取時間**（到達的時間）分成兩個欄位保留。
- 「每日」報表需要一個時區：BigQuery 的 `DATE(event_ts, 'America/New_York')`。
  如果報表是依使用者而定，就存使用者的時區。
- 未來的預約（「明年三月當地 9:00」）是牆上時鐘時間加上時區名稱，因為在它發生之前
  時區規則可能會改。

<!-- 771825b0b8f6 -->
### 5-3) 字串、布林與列舉

- **字串** —— PostgreSQL 的 `text` 和 `varchar(n)` 效能相同；`varchar(n)` 只是長度檢查。
  長度限制是真正的規則時才用它。
- **布林** —— 用真正的 `BOOLEAN`，名稱寫成一個陳述（`is_active`、`has_paid`），不要用
  `'Y'/'N'` 字串。三態旗標不是布林；把它做成列舉。
- **列舉** —— 用 `CHECK (status IN (...))` 或一張查找表。PostgreSQL 原生的 `ENUM` 可以加值，
  但不重建型別就無法刪值；魔術數字（`status = 3`）什麼都沒說明。
- **來自其他系統的識別碼** —— 即使看起來像數字也存成字串（`'00123'` 存成整數會掉零）。

<!-- b5ab241efeec -->
### 5-4) 半結構化與巢狀資料

**JSON 先原樣落進半結構化欄位，再把查詢真正會用到的欄位提升成有型別的欄位。**

| 引擎 | 型別 |
|---|---|
| PostgreSQL | `jsonb`（二進位、可用 GIN 索引）—— 優先於 `json` |
| BigQuery | `JSON`，或有型別的 `STRUCT` / `ARRAY`（巢狀、重複欄位） |
| Snowflake | `VARIANT` |
| Spark / Delta | `STRUCT`、`ARRAY`、`MAP`；Spark 4.0 有 `VARIANT` |

BigQuery 的巢狀與重複欄位把一張訂單和它的明細存在同一列，所以常見的讀取不需要 join；
真的需要時再用 `UNNEST` 攤平。

<!--CODE-->

這裡的 `PARTITION BY DATE(order_ts)` 是 BigQuery 在 `TIMESTAMP` 欄位上的時間單位分區，
所以 `DATE(order_ts)` 的過濾條件確實會 prune。

---

<!-- 750747a88081 -->
## 6) NULL、命名慣例與資料表模式 ⭐⭐⭐

<!-- 08d2ad49b67a -->
### 6-1) NULL 處理與預設值

**`NULL` 代表未知，而且會傳染：`NULL = NULL` 是 unknown、`NULL + 1` 是 `NULL`，對一個含有
`NULL` 的清單做 `NOT IN` 什麼都匹配不到。**

- 預設 `NOT NULL`；只有在「未知」是真實存在的狀態時才允許 `NULL`。
- 不要在有型別的欄位裡用魔術值表示未知（`'1900-01-01'`、`-1`）—— 彙總會默默把它算進去。
  例外是維度表裡明確的「未知」成員列，它讓事實表的外鍵能維持 `NOT NULL`。
- 當變更偵測必須看見 `NULL → 值` 時，用 null 安全的比較：`IS DISTINCT FROM`。
- 在 PostgreSQL 11+，`ADD COLUMN ... DEFAULT <常數>` 只改中介資料；
  volatile 的預設值（`random()`）仍會重寫整張表。

<!-- 0a67774b261a -->
### 6-2) 命名與稽核欄位

| 慣例 | 例子 |
|---|---|
| `snake_case`，單數或複數 —— 選一種 | `order_line`、`customer` |
| 資料倉儲裡的層級 / 角色前綴 | `stg_`、`fact_`、`dim_`、`agg_` |
| 鍵的後綴 | `_id` 自然鍵 / 來源鍵、`_sk` 代理鍵 |
| 時間的後綴 | 時間戳用 `_at` / `_ts`，日期用 `_date` |
| 布林寫成陳述 | `is_deleted`、`has_discount` |
| 單位寫在名稱裡 | `duration_ms`、`distance_km`、`amount_usd` |

每張表都有稽核欄位：`created_at`、`updated_at`；在管線裡再加上 `_ingested_at`、
`_source`、`_batch_id`（或 `_run_id`），讓任何一列都能追溯到寫入它的那次載入。

<!-- b2c884c56ef3 -->
### 6-3) 軟刪除與歷史表

**軟刪除是把一列標記為已刪除而不是真的移除 —— 保留下來供稽核與復原，代價是每個查詢都
得把它排除。**

- `deleted_at TIMESTAMPTZ NULL` 比 `is_deleted BOOLEAN` 好：它記錄了何時刪除。
- 唯一性必須忽略已刪除的列 —— 用**部分唯一索引**。
- 一個 view（`active_customer`）替讀取者藏起這個過濾條件。
- 隱私刪除（「刪除我的資料」）需要**硬**刪除或匿名化；軟刪除不符合要求。
- **歷史表** —— 由應用程式或 trigger 寫入的只附加 `customer_history` 保留每個版本，
  又不會弄亂主表；把 CDC 送進 data lake 則是在下游做同一件事。

<!--CODE-->

<!-- 8ab63bfa744e -->
### 6-4) 多租戶 schema

| 模型 | 隔離 | 成本 / 維運 | 適合 |
|---|---|---|---|
| **共用資料表 + `tenant_id`** | 邏輯隔離 —— 每個查詢都要過濾 | 最便宜，一次 migration | 大量小租戶（SaaS 預設） |
| **每個租戶一個 schema** | 命名空間 | migration 要跑 N 次 | 數十到數百個租戶 |
| **每個租戶一個資料庫** | 強 | 最貴 | 少數大型或受監管的租戶 |

- 用共用資料表時，每個索引與主鍵都以 `tenant_id` 開頭，並用 PostgreSQL 的
  **row-level security** 在資料庫裡強制過濾，而不是相信每一個查詢。
- 小心**吵鬧的鄰居**：一個超大租戶可能主宰整張共用表 —— 這就是大租戶常被搬到自己資料庫的原因。

<!--CODE-->

---

<!-- fd10de45ceff -->
## 7) Schema 演進 ⭐⭐⭐⭐⭐

<!-- adafdc46857c -->
### 7-1) 哪些變更是安全的

**增加式的變更是安全的；改名、縮小型別、刪掉有人在讀的欄位都是破壞性的 —— 所以每次變更
上線前都要先分類。**

| 變更 | 通常安全嗎？ | 怎麼做 |
|---|---|---|
| 新增可為 null 的欄位 / 有預設值的欄位 | 安全 | 直接加 |
| 放寬型別（`INT` → `BIGINT`） | 通常安全 | 逐一檢查每個引擎與序列化格式 |
| 欄位改名 | 不安全 | expand-and-contract（7-5） |
| 刪除欄位 | 會打壞它的讀取者 | 先標記淘汰、確認沒有讀取者，再刪除 |
| 縮小型別 / 改變單位或意義 | 不安全 | 新欄位或新的版本化資料表 |
| 把可為 null 的欄位改成 `NOT NULL` | 會打壞送 `NULL` 的寫入者 | 先回補，再加約束 |

<!-- 799fd64d4500 -->
### 7-2) Avro 與 Protobuf 的規則

**Avro 依欄位名稱把寫入者的 schema 與讀取者的 schema 對起來，缺少的欄位用預設值補上；
Protobuf 以欄位編號識別欄位，所以一個編號就是永遠。**

| | Avro | Protobuf（proto3） |
|---|---|---|
| 欄位識別 | 名稱（改名用 aliases） | 欄位編號 |
| 新增欄位 | 給它預設值 | 用任何新編號；不存在的欄位讀成該型別的預設值 |
| 移除欄位 | 對有該欄位預設值的讀取者是安全的 | 用 `reserved` 保留它的編號與名稱 |
| 改名 | 透過 `aliases` | 線路上安全（二進位只帶編號），但會打壞 JSON 對應 |
| 改型別 | 只有列出的提升（`int` → `long`、`float` → `double`……） | 只有線路相容的組合（`int32` ↔ `int64`……）；否則就開新欄位 |

<!--CODE-->

<!-- ffb0d26eba69 -->
### 7-3) Schema Registry 相容性模式

**Registry 會把每個新 schema 版本和舊版本比對，拒絕不相容的版本，所以一次錯誤的 producer
部署會在註冊時失敗，而不是打壞每一個 consumer。**

| 模式 | 保證 | 允許的變更（Avro） | 先升級誰 |
|---|---|---|---|
| `BACKWARD`（預設） | 新 schema 能讀用前一版寫入的資料 | 刪欄位、新增有預設值的欄位 | consumer |
| `FORWARD` | 前一版 schema 能讀用新版寫入的資料 | 新增欄位、刪除原本有預設值的欄位 | producer |
| `FULL` | 兩者皆是 | 只能新增 / 刪除有預設值的欄位 | 任一方 |
| `*_TRANSITIVE` | 同上，但比對**所有**更早的版本，而不只是上一版 | — | — |
| `NONE` | 什麼都不檢查 | 任何變更 | — |

對 consumer 會從頭重播的 Kafka topic 來說，transitive 模式才是安全的選擇：consumer 可能
讀到任何一個舊版本寫入的紀錄。

<!-- 15fcc194bacc -->
### 7-4) 表格格式的演進

| 格式 | 新增 | 改名 / 調整順序 | 刪除 | 備註 |
|---|---|---|---|---|
| 純 Parquet 檔案 | 可以（讀取者合併 schema） | 會壞（依名稱） | 讀取者看到 `NULL` | 根本沒有表層級的 schema |
| Hive Metastore 表 | `ADD COLUMNS` | 依格式而定，依位置或依名稱 | 有限 | 中介資料說一套，檔案是另一套 |
| **Iceberg** | 可以 | 可以，只改中介資料（以 id 追蹤欄位） | 可以 | 還支援不重寫資料的分區演進 |
| **Delta Lake** | 可以（寫入時 `mergeSchema`） | **只有開啟 column mapping** 才是只改中介資料 | 需要 column mapping | column mapping 會升級表格 protocol |

<!--CODE-->

升級 protocol 代表舊版的 Delta 讀取者再也讀不了這張表 —— 先確認每個讀取者的版本。

<!-- 82854adb3479 -->
### 7-5) Expand and contract

**不停機地改名或改型別，分三次發佈：加上新形狀、讓所有人搬過去、再移除舊的。**

<!--CODE-->

當變更大到不是一個欄位能處理時，同樣的模式可以用來版本化整張表或整個 topic（`orders_v2`），
並搭配**資料契約**，寫明誰擁有這個 schema，以及破壞性變更要提前多久通知。

---

<!-- 737d9101c6c2 -->
## 8) 事件與 Log 的 Schema ⭐⭐⭐⭐

<!-- e5f2a1147f12 -->
### 8-1) 事件信封（envelope）

**每個事件都帶著同樣的信封 —— 唯一 id、兩個時間戳、類型與 schema 版本 —— 包住依類型而
不同的 payload。**

| 欄位 | 為什麼 |
|---|---|
| `event_id` | 每個事件唯一（UUIDv7）—— consumer 用它去重 |
| `event_type` | `order_placed`、`page_viewed` —— 用來路由與過濾 |
| `schema_version` | 要用哪一版 payload schema 解析 |
| `event_time` | 發生的時間（producer 的時鐘）—— 視窗與分區都用它 |
| `ingest_time` | 管線收到它的時間 —— 用來量測遲到程度、驅動增量載入 |
| `producer` / `source` | 哪個服務發出的 |
| `entity_id` | 串流據以分區與排序的 key（`order_id`、`user_id`） |
| `payload` | 依類型而定的內容 |

<!--CODE-->

`ingest_time` 通常是管線加上的，不是 producer。

<!-- e5e02ab55b69 -->
### 8-2) 事件設計規則

- **是事實，不是命令** —— 用過去式命名事件（`order_placed`），描述已經發生的事，
  讓任意數量的 consumer 都能各自反應。
- **足夠自足** —— 放進 consumer 需要的欄位，避免處理時還要查表，但不要完整複製每一個相關實體。
- **不可變** —— 更正是一個新事件（`order_amount_corrected`），而不是去改舊的。
- **每個實體或領域一個 topic，而不是每個 consumer 一個** —— 由 consumer 自己過濾。
- **Partition key = 順序重要的那個實體** —— 一張訂單的所有事件都進同一個 partition，
  因此會依序被讀到（見 [`kafka.md`](./kafka.md)）。
- **不放用不到的 PII** —— 事件會被複製到各處，而且保留很久。

---

<!-- 334ab076797e -->
## 9) 完整案例 ⭐⭐⭐⭐⭐

<!-- 3aeb7fa47b08 -->
### 9-1) 電商 OLTP（PostgreSQL）

**需求**：顧客下包含多個商品的訂單；顯示顧客最近的訂單；絕不超賣；保留成交價格。

<!--CODE-->

`stock_qty >= 0` 這個檢查讓資料庫拒絕超賣：
`UPDATE product SET stock_qty = stock_qty - 2 WHERE product_id = ?` 會失敗，而不是變成負數。
它用到 6-3 的 `customer` 表。

<!-- dce10a54b8cd -->
### 9-2) 叫車服務分析（BigQuery）

**需求**：每城市每天的行程與營收；司機表現；司機所在城市與評分的歷史。

1. **流程** —— 行程完成。
2. **粒度** —— `fact_trip`：每一趟完成的行程一列。
3. **維度** —— 乘客、司機（SCD2）、日期、區域（區域可以彙總成城市）。
4. **度量值** —— 車資、距離、時長、加成倍率。
5. **配置** —— 依 `trip_date` 分區，依 `pickup_zone_sk`、`driver_sk` 做 cluster。

<!--CODE-->

`trip_date` 是行程在其城市的**當地**日期，於載入時計算 —— 所以「每城市每天」指的是該城市
自己的那一天，而不是 UTC 的那一天。要說清楚你選的是哪一個。

<!-- 2207106e7a0c -->
### 9-3) 點擊流事件表（Spark SQL 搭配 Delta Lake）

**需求**：每天數十億筆事件；依日期與依使用者查詢；從 Kafka 重播時不能重複。

<!--CODE-->

把 `event_date` 放進 `MERGE` 的條件，會把比對限制在這批資料涉及的分區，而不是整張表 ——
前提是重播的事件保留它原本的 `event_time`。

---

<!-- 3fbaff456b98 -->
## 10) 反模式

| 反模式 | 為什麼傷人 | 改用 |
|---|---|---|
| Entity-attribute-value 表（`entity, key, value`） | 沒有型別、沒有約束，每個查詢都是一次 pivot | 真正的欄位，或只把真正多變的部分放進 `jsonb` 欄位 |
| 用 `FLOAT` 存金額 | `0.1 + 0.2 ≠ 0.3` | `NUMERIC` 或整數最小單位 |
| 時間點用沒有時區的 `TIMESTAMP` | 跨時區與日光節約時會有歧義 | `timestamptz` / UTC |
| 欄位裡存逗號分隔的清單 | 無法索引、join 或加約束 | 子表或 `ARRAY` 型別 |
| 依高基數欄位分區 | 數百萬個小分區 | 依日期分區，依 id 做 cluster |
| 一張表帶 `type` 加 40 個大多為 null 的欄位 | 稀疏、意義不清 | 每種類型一張表，或共用核心表 + 擴充表 |
| 到處都沒有 `NOT NULL` | schema 什麼都沒記錄 | 預設 `NOT NULL` |
| 把欄位拿來裝新的意義 | 舊資料被默默重新解讀 | 新欄位 |
| 大型子表上的外鍵沒有索引 | join 慢、刪除父列也慢 | 替參照端欄位建索引 |

---

<!-- 40493e9f05f1 -->
## 11) 快問快答

**問：主鍵用 UUID 還是 auto-increment？**
答：單一資料庫且 id 不對外曝光時，用 auto-increment `BIGINT`；id 在很多地方生成或會出現在
URL 裡時，用 UUIDv7 —— 它依時間排序，所以不會像 UUIDv4 那樣讓索引碎片化。

**問：怎麼選分區欄位？**
答：幾乎每個查詢都會過濾、而且基數夠低讓分區維持夠大的欄位 —— 通常是事件日期。
接著再依下一批最常被過濾的欄位做 cluster。

**問：一張有 30 個 job 在讀的表，某個欄位要改名。怎麼做？**
答：Expand and contract：加新欄位、雙寫、回補、讓讀取者搬過去，再刪掉舊欄位 ——
或者在 Iceberg 或開了 column mapping 的 Delta 上，等每個讀取者都支援後做只改中介資料的改名。

**問：資料倉儲裡要放外鍵嗎？**
答：為了文件與最佳化器可以宣告，但多數資料倉儲不會強制執行，所以參照完整性要靠管線裡的
資料品質測試來檢查。

**問：JSON 該放哪裡？**
答：在 raw 層放進半結構化欄位；會被過濾、join 或彙總的欄位，到清理層再提升成有型別的欄位。

**問：在 schema 層面，什麼會打壞 Kafka consumer？**
答：移除它在讀的欄位或改它的型別，或是 producer 註冊了不相容的 schema —— 開在 `BACKWARD`
或 `FULL` 模式的 Schema Registry 會在註冊時就拒絕它。
