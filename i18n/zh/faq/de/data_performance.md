<!-- affb2a867e4e -->
# 資料儲存與處理效能 FAQ

> **範圍** —— 資料工程面試跨引擎而非針對單一引擎所問的效能題：回答任何「讓它變快」問題的方法、最佳化資料的儲存方式、最佳化資料的處理方式、在 Spark、關聯式資料庫、MPP 查詢引擎與單機工具中避免記憶體不足（OOM）、資料的重新排序與合併（sorting、clustering、Z-order、compaction、`MERGE`、external merge sort、LSM compaction）、儲存系統的讀取與寫入效能，以及讓查詢變快的 SQL 改寫。各引擎的內部機制留在各自的深入解析；本文件是跨引擎的視角，並連到那些文件。
> **另見**：[`./spark.md`](./spark.md) —— shuffle、資料傾斜、記憶體，以及 Spark 自己的故障對照表；[`./sql_explain.md`](./sql_explain.md) —— 讀懂告訴你時間花在哪裡的執行計畫；[`./sql_for_data.md`](./sql_for_data.md) —— §10 的索引與資料倉儲調校手段；[`./batch_pipeline.md`](./batch_pipeline.md) —— 管線中的檔案格式、table format 與 compaction；[`./sql_vs_nosql.md`](./sql_vs_nosql.md) —— B-tree 與 LSM 儲存引擎；[`../faq_performance_tune.md`](../faq_performance_tune.md) —— 應用程式層級的 profiling。

幾乎每一道資料效能題，底下都是同一個答案：**少讀、少搬、少佔記憶體，並以讀取端需要的形狀寫入。**
以下各節就是把這句話套用到儲存、處理、記憶體、排序與讀寫路徑上。

---

<!-- 9a7d6ec0423f -->
## 0) 一句話回答 ⭐⭐⭐⭐⭐

**「我會先量測時間花在哪裡 —— 看查詢計畫或 Spark UI，而不是用猜的。接著依這個順序減少工作量：
少讀資料（columnar 格式、partition pruning、透過 clustering 讓 min/max 統計資訊跳過檔案）、
少搬資料（join 之前先過濾與聚合、broadcast 小的那一側、修正資料傾斜）、少佔記憶體（用串流取代
collect、足夠的 partition 數、讓引擎 spill），以及少寫（用增量 `MERGE` 取代整批重建、每個檔案
幾百 MB）。做完這些才加硬體。」**

| 面試官的問法 | 一句話答案 | 章節 |
|---|---|---|
| 「你怎麼最佳化資料儲存？」 | columnar + 壓縮、大小適中的檔案、依最常用的過濾條件分區、在分區內排序 | [§2](#2-optimising-storage-) |
| 「你怎麼最佳化處理？」 | pruning 與 pushdown、少做 shuffle、增量計算 | [§3](#3-optimising-processing-) |
| 「在查詢引擎中怎麼避免 OOM？」 | 絕不把無上限的東西實體化到單一地方 —— 一個 key、一個節點、一個 client | [§4](#4-avoiding-out-of-memory-failures-) |
| 「你怎麼重新排序或合併資料？」 | 寫入時 sort / cluster、compact 小檔案、對已去重的來源做 `MERGE` | [§5](#5-re-ordering-and-merging-data-) |
| 「怎麼加快資料庫的讀取或寫入？」 | 每一項讀取最佳化都是寫入成本 —— 先說出取捨，再批次寫入、為讀取建索引 | [§6](#6-read-and-write-performance-on-storage-systems-) |
| 「怎麼讓這段 SQL 變快？」 | sargable 的過濾條件、先聚合再 join、`EXISTS`、keyset 分頁 | [§7](#7-sql-rewrites-that-make-queries-faster-) |

---

<!-- 8fa2d768f7d2 -->
## 1) 回答任何效能題的方法 ⭐⭐⭐⭐⭐

<!-- 31706552c6d4 -->
### 1-1) 時間花在的四個地方

**一個資料 job 的時間花在四個地方 —— 讀取、搬移、運算與寫入 —— 而修正方式取決於哪一個佔大宗，
所以第一步永遠是先查清楚。**

| 階段 | 症狀 | 在哪裡看得到 |
|---|---|---|
| **讀取**（I/O） | scan 時間佔大宗；讀到的位元組 ≫ 需要的位元組 | PostgreSQL 的 `BUFFERS`、BigQuery / Athena 的「bytes scanned」、Spark UI 每個 stage 的 input size |
| **搬移**（shuffle、網路） | 某個 stage 的 shuffle read/write 極大；某個 task 特別慢 | Spark UI 的 stage 表、計畫中的 `Exchange` 節點 |
| **運算** | CPU-bound 的 task；Python UDF；sort 或 hash 在 spill | task 時間 vs GC 時間、`Sort Method: external merge` |
| **寫入** | 大量小檔案；commit 很慢；每一列都要維護索引 | 每個 partition 的檔案數、寫入 stage 的時間、WAL 量 |

調校之前先問：

1. **目標是什麼？**「每日 job 必須在 06:00 前跑完」或「p95 低於 2 秒」—— 要一個數字。
2. **什麼改變了？** 資料量、新的 join、新出現的傾斜 key、某個設定、引擎升級。
3. **哪個階段佔大宗？** 讀計畫或 UI；最慢的 stage 或節點就是要處理的工作。
4. **是一個 task 慢還是全部都慢？** 一個 task 慢是資料傾斜；全部都慢是資料量或資源問題。

<!-- 577855f84ebd -->
### 1-2) 依效益排序的調校手段

**便宜的結構性改變勝過昂貴的調校：移除工作比把同樣的工作做得更快更有價值，而加硬體擺在最後，
因為它掩蓋問題，而且每天都在花錢。**

| 順序 | 手段 | 典型效益 | 例子 |
|---|---|---|---|
| 1 | **根本不做這份工作** | 10–1000× | 用增量取代整批重建；從抽取中拿掉沒用到的欄位 |
| 2 | **少讀** | 10–100× | partition pruning、column pruning、為 data skipping 做 clustering |
| 3 | **少搬** | 2–20× | broadcast 維度表、先聚合再 join、修正資料傾斜 |
| 4 | **算得更聰明** | 2–10× | 用內建函式取代 Python UDF、近似的 distinct 計數 |
| 5 | **寫得更好** | 對下一個讀取者 2–10× | 大小適中的檔案、排序過的輸出、compaction |
| 6 | **調校引擎** | 1.2–3× | shuffle partition 數、記憶體比例、`work_mem` |
| 7 | **水平或垂直擴充** | 最多線性 | 更多 executor、更大的 warehouse |

---

<!-- b25bb54f5720 -->
## 2) 最佳化儲存 ⭐⭐⭐⭐⭐

<!-- 25e5ac7803ff -->
### 2-1) 格式、壓縮與編碼

**分析用途就存成 columnar（Parquet 或 ORC）並搭配快速的 codec：查詢只讀它指名的欄位，
同一欄中相似的值壓縮效果好，而每個 chunk 的統計資訊讓讀取端不必打開就能跳過資料。**

| 選擇 | 用在 | 原因 |
|---|---|---|
| **Parquet / ORC**（columnar） | 分析用的資料表、data lake | column pruning、統計資訊、編碼 |
| **Avro**（列式、帶 schema） | Kafka 訊息、落地原始紀錄 | 整筆紀錄寫入、schema 演進 |
| **CSV / JSON** | 只用在邊界的資料交換 | 沒有型別、沒有統計資訊、解析慢 —— 一抵達就轉換 |

| Codec | 取捨 |
|---|---|
| **Snappy** / **LZ4** | 解壓快、壓縮率中等 —— 常見的預設 |
| **ZSTD** | 壓縮率比 Snappy 好、解碼速度相近 —— 冷資料與溫資料的好預設 |
| **GZIP** | 壓縮率好但慢，而且 gzip 的 **CSV** 無法切分 —— 一個檔案一個 task |

在 Parquet 的 column chunk 內，writer 在壓縮之前還會先**編碼**：低基數字串用 **dictionary**
編碼、重複值用 **run-length** 編碼、排序過的整數與時間戳用 **delta** 編碼。把資料排序（見 [§5](#5-re-ordering-and-merging-data-)）
能讓這三種都更有效，這就是為什麼排序過的檔案常常小 2–5 倍。

<!-- 1af385d2270d -->
### 2-2) 檔案與分區的大小

**目標是每個檔案大約 128 MB 到 1 GB，每個分區至少裝得下幾個檔案的資料量；太多小檔案在開檔與
metadata 上的成本會超過資料本身。**

| 單位 | 目標 | 太小代表 | 太大代表 |
|---|---|---|---|
| **檔案** | 128 MB – 1 GB | 每個小檔案一個 task、一次開檔；列目錄與規劃都很慢 | 檔案內部的平行度很低 |
| **Parquet row group** | 約 128 MB | 統計資訊弱、seek 次數多 | 跳過的粒度很粗 |
| **資料表分區** | ≥ 1 GB，總共幾百到幾千個分區 | 小檔案與龐大的 metadata | 幾乎沒有 pruning |

- **依幾乎每個查詢都會過濾的低基數欄位分區** —— 通常是日期。絕不要依 `user_id` 或其他高基數
  key 分區。
- 在分區內，依次常被過濾的欄位**排序或 cluster**，讓 min/max 統計資訊能跳過檔案
  （見 [`./schema_design.md`](./schema_design.md#4-3-bucketing-z-order-and-liquid-clustering)）。
- Table format（Iceberg、Delta）把檔案清單存在 metadata 裡，所以規劃時不必列目錄 —— 但它無法
  讓小檔案讀起來不慢。

<!-- 5e2f5bed4203 -->
### 2-3) 資料型別與寬度

**一列中的每個位元組都會被讀取、shuffle、快取，並在每一份副本中再存一次，所以用最窄的正確
型別是免費的加速。**

- 整數 surrogate key 比長字串 key join 得更快、壓縮得更好。
- 日期與時間戳存成 `DATE` / `TIMESTAMP`，不要存成字串 —— 它們更小、比較正確，而且能做
  partition pruning。
- 金額用 `DECIMAL(p, s)`；只有在可接受捨入誤差的地方才用 `DOUBLE`。
- 把查詢真正會用到的 JSON 欄位抽成有型別的欄位；裝著 JSON 的 `STRING` 每次查詢都要重新解析，
  而且沒有統計資訊。
- 從寬的原始資料表升級到 silver / gold 時，拿掉沒人讀的欄位。

<!-- 52c7dcb61d3a -->
### 2-4) 生命週期：熱、溫、冷

**不是所有資料都值得同樣的儲存：讓近期、常被讀的資料保持快速，其餘的移到較便宜的層級，
沒人查詢的舊原始明細就用聚合結果取代。**

| 層級 | 存放 | 儲存方式 |
|---|---|---|
| 熱 | 最近幾天或幾週；儀表板會打到 | 資料倉儲原生儲存、SSD、快取 |
| 溫 | 幾個月；臨時分析 | 物件儲存上的 Parquet，標準儲存類別 |
| 冷 | 幾年；稽核與少見的回補 | 不常存取或封存儲存類別 |

- 讓舊的資料表 snapshot 過期（Delta 的 `VACUUM`、Iceberg 的 `expire_snapshots`）—— 否則
  每次改寫都會永遠留著舊檔案。
- 一旦 silver 可信且可重播，就對 bronze / 原始落地資料設 TTL。
- 舊明細不再被查詢時，把它彙總成每日聚合。

---

<!-- efb7904da6f6 -->
## 3) 最佳化處理 ⭐⭐⭐⭐⭐

<!-- 7cbae502c856 -->
### 3-1) 少讀：pruning 與 pushdown

**最快的位元組是從未被讀取的那一個：讓引擎在任何資料抵達 task 之前，就跳過分區、檔案、
row group 與欄位。**

| 技巧 | 跳過什麼 | 需要 |
|---|---|---|
| **Projection pushdown**（column pruning） | 沒被引用的欄位 | columnar 格式；不要 `SELECT *` |
| **Partition pruning** | 整個分區 | 直接對分區欄位本身下過濾條件 |
| **Predicate pushdown** | min/max 排除該值的 row group / 檔案 | 統計資訊；資料依該欄排序或 cluster |
| **Bloom filter** | 不可能包含某個等值的檔案 | 為該欄寫好的 bloom filter |
| **Dynamic partition pruning** | 沒有對應維度列的事實表分區 | 以分區欄位 join 一個已過濾的維度表 |

確認 pushdown 真的發生，而不是假設它發生了：

<!--CODE-->

以下情況會讓 pushdown 失效：欄位外面包了函式（在依 `ts` 的日期分區的欄位上寫
`WHERE to_date(ts) = …`）、改變型別的 cast，以及過濾條件中的 Python UDF。

<!-- 69e11e3acacc -->
### 3-2) 少搬：shuffle 與 join

**Shuffle 會把每一列寫到磁碟並透過網路送出，所以最便宜的 join 就是 shuffle 位元組最少的那個。**

- **在 join 之前先過濾與聚合**，而不是之後 —— 見 [§7-2](#7-2-worked-rewrite-aggregate-before-the-join) 的改寫。
- **Broadcast 小的那一側**，讓大的那一側完全不必移動（Spark 的
  `spark.sql.autoBroadcastJoinThreshold`，預設 10 MB；Trino 的 broadcast distribution）。
- **Co-locate** 總是以同一個 key join 的資料表 —— Spark / Hive 的 bucketing、Redshift 的
  distribution key —— 讓 join 不需要 exchange。
- **修正資料傾斜** —— 一個熱門 key 會讓一個 task 做掉大部分的工作；見
  [`./spark.md`](./spark.md#7-3-data-skew-spotting-it-and-fixing-it)。
- 資料本來就唯一時，避免用 `DISTINCT` 與（會去重的）`UNION` —— 兩者都是隱藏的 shuffle。

<!-- 2540122b899c -->
### 3-3) 少算：增量與預先計算

**為了加一天的資料而重算一整年的歷史，是批次平台最常見的浪費；只處理有變動的部分，並預先
計算常被讀取的結果。**

| 技巧 | 取代 | 注意事項 |
|---|---|---|
| **增量載入 + `MERGE`** | 每晚整批重建 | 需要可靠的變更標記或 CDC |
| 只對有變動的日期做 **partition overwrite** | 重建整張資料表 | job 必須對每個分區具冪等性 |
| **Materialised view / 彙總表** | 掃描明細的儀表板 | 刷新成本與資料過時 |
| **近似聚合**（`approx_count_distinct`、HLL sketch） | 精確的 `COUNT(DISTINCT)` | 約 1–2% 的誤差，事先說明 |
| **內建函式** | Python UDF | UDF 會擋住 code generation，而且每一列都有序列化成本 |

<!-- bbd38aa79010 -->
### 3-4) 少寫

- 只改寫有變動的分區或檔案；table format 的 `MERGE` 只會碰到含有符合列的檔案。
- 頻繁的小量更新偏好 **merge-on-read（讀時合併）**，讀取量大、以大批次更新的資料表偏好
  **copy-on-write（寫時複製）**（見 [§5-4](#5-4-merging-datasets-merge-copy-on-write-and-merge-on-read)）。
- 把許多小寫入合併成較少、較大的寫入 —— 對資料倉儲或 data lake 逐列 insert，每一筆都會產生
  一個檔案或一次 commit。

---

<!-- bba0fe77f074 -->
## 4) 避免記憶體不足（OOM） ⭐⭐⭐⭐⭐

<!-- 0d5d1face7f2 -->
### 4-1) 引擎為什麼會記憶體不足

**當某個無上限的東西被實體化到一個有上限的地方，引擎就會記憶體不足：一個 key 的所有列擠在
一個 task、整個結果放在一個節點、用錯的一側建 hash table，或是太多查詢同時各自拿走自己那一份。**

五種型態，每個引擎都有：

| 型態 | 例子 |
|---|---|
| **全部送到一個地方** | 對 Spark driver `collect()`；client 抓 5,000 萬列；最終結果經過 coordinator |
| **單一 key 太大** | 資料傾斜 —— 佔 30% 列數的 `NULL` 或測試帳號 key 落在同一個 task |
| **Build side 選錯** | hash join 用大的輸入建它的記憶體內資料表 |
| **列數爆炸** | 放大的 join、`explode`、`collect_list` 產生一筆巨大的列 |
| **並行度太高** | 200 條連線每條都被允許使用好幾個 `work_mem` buffer |

答案的另一半，是 buffer 滿了時引擎會怎麼做：

| 引擎 | sort / hash / 聚合的 buffer 滿了時 |
|---|---|
| **Spark** | 把 sort、聚合與 shuffle buffer spill 到本機磁碟；遇到單一過大的 partition、巨大的 broadcast、driver `collect` 或 off-heap 成長時仍會失敗 |
| **PostgreSQL / MySQL** | 把 sort 與 hash spill 到暫存檔（較慢，但會完成）；若每個查詢的記憶體 × 連線數超過 RAM，整台伺服器仍可能被作業系統砍掉 |
| **Trino / Presto / Athena** | 把 operator 留在記憶體中，達到記憶體上限就**讓查詢失敗**，除非有設定 spill —— 控制 join 的大小是你的責任 |
| **pandas** | 不會 spill：整個 DataFrame 必須放得進 RAM，通常是檔案大小的好幾倍 |
| **DuckDB / Polars（串流）** | 對超過記憶體的 sort、join 與聚合做 spill 或串流處理 |

<!-- d2912275bbe3 -->
### 4-2) Spark

**Driver OOM 幾乎都是有東西被拉到 driver；executor OOM 幾乎都是某個 partition 太大 —— 資料傾斜、
shuffle partition 太少，或是列數爆炸。**

預防措施，依檢查順序：

1. **絕不對大結果 `collect()` 或 `toPandas()`** —— 把它寫出去；driver 必須看到資料列時，用
   `limit` 或 `toLocalIterator()`。
2. **足夠的 shuffle partition**，讓每個約 100–200 MB；開啟 AQE，讓它能合併與切分 partition。
3. **先修正資料傾斜再加記憶體** —— AQE skew join、隔離熱門 key 或對它加 salt。
4. **讓 broadcast 名副其實** —— 門檻是以估計值衡量的；實際上有 2 GB 的「小」表會把 driver 與
   每個 executor 一起拖垮。
5. **限制列數成長** —— 檢查 join 的放大倍數，避免對無上限的群組做 `collect_list` / `collect_set`。
6. **Off-heap** —— 「Container killed … exceeding memory limits」代表 Python worker、Arrow 或原生
   buffer 超出了 `spark.executor.memoryOverhead`，而不是 heap。

<!--CODE-->

完整的錯誤 → 原因 → 修正對照表在 [`./spark.md` §15-1](./spark.md#15-1-out-of-memory)，
背後的記憶體區域見 [§9-1](./spark.md#9-1-unified-memory-management)。

<!-- ff103d80aca7 -->
### 4-3) 關聯式資料庫（PostgreSQL、MySQL）

**資料庫很少死於單一查詢；它死於每個操作的記憶體乘上並行度 —— 而抓整個結果集的 client
往往先死。**

伺服器端：

- PostgreSQL 的 **`work_mem`**（預設 4 MB）是每個 sort 或 hash **節點**、每個查詢、每條連線
  各自一份 —— hash 節點可能使用 `work_mem × hash_mem_multiplier`（預設 2.0）。一個有五個
  sort 與 hash 的查詢跑在 200 條連線上，要的記憶體可能遠超過 `work_mem × 200`。
  針對沉重的報表**以 session 或 role 為單位**調高，不要全域調高。
- 在上限之下，sort 或 hash 會 **spill**（`Sort Method: external merge`、`Batches: 8`）——
  變慢，但不致命。修正 spill 見 [`./sql_explain.md` §6-3](./sql_explain.md#6-3-fixing-a-spill)。
- 使用 **connection pooler**（PgBouncer），讓並行度受 pool 限制，而不是受應用程式執行緒數限制。
- MySQL 的對應設定是 `sort_buffer_size` 與 `join_buffer_size`（每個 session），以及
  `tmp_table_size` / `max_heap_table_size`（超過的記憶體內暫存表會改寫到磁碟）。

Client 端 —— 資料管線中常見的那種：

<!--CODE-->

- 連 PostgreSQL 的 JDBC 只有在 **autocommit 關閉**且 result set 為 forward-only 時才會遵守
  `setFetchSize(n)`；否則 driver 會把每一列都緩存起來。
- MySQL Connector/J 用 `setFetchSize(Integer.MIN_VALUE)` 串流，或用 `useCursorFetch=true` 搭配
  正的 fetch size。
- 避免在應用程式裡組出 100 000 個項目的 `IN (...)` 清單 —— 把 key 載入暫存表再 join。

<!-- b956901e26ab -->
### 4-4) MPP 查詢引擎（Trino、Presto、Athena）

**Trino 把 hash join 的 build side 放在記憶體中，達到上限就讓查詢失敗，所以答案是把 build side
變小：把較小的表放在右邊、給 optimizer 統計資訊，並在 join 之前先縮減資料。**

- **Build side** —— Trino 用 join 的**右**側建 hash table。有資料表統計資訊時，cost-based
  optimizer 會重排 join 順序；沒有的話就保留你寫的順序，所以把大的事實表寫在左邊。
- **Distribution** —— **broadcast** join 把 build side 複製到每個 worker（快，但每個 worker
  都要裝下全部）；**partitioned** join 對兩側都做 hash（每個 worker 只裝一部分）。大的 build side
  必須用 partitioned。
- **高基數的 `GROUP BY`** 與**多欄的 `COUNT(DISTINCT)`** 會佔用大型 hash table —— 預先聚合，
  或用 `approx_distinct`。
- 不要把巨大的結果拉到 client：`CREATE TABLE AS SELECT`（CTAS）或 `INSERT INTO` 會從各個 worker
  平行寫出。
- Athena 的 **「Query exhausted resources at this scale factor」** 是同一種故障；修正方式相同，
  再加上分區，讓需要讀的資料本來就更少。

<!-- 01d19617270f -->
### 4-5) 單機工具（pandas、DuckDB、Polars）

**pandas 需要把整個資料集放進 RAM，通常是檔案大小的好幾倍；只讀你需要的欄與列、分塊讀取，
或換成會串流處理的引擎。**

<!--CODE-->

- **DuckDB** 透過 spill 到暫存目錄來執行超過記憶體的 sort、join 與聚合：
  `SET memory_limit = '4GB'; SET temp_directory = '/tmp/duckdb';`，而且它能直接就地查詢
  Parquet，不必先載入。
- **Polars** 的 lazy API（`scan_parquet` … `collect(engine="streaming")`）以批次處理。
- 分塊模式只對**可分解**的聚合（sum、count、min、max）有效；中位數或 distinct 計數需要不同的
  做法（sketch，或真正的引擎）。

<!-- 1f959ed5bb87 -->
### 4-6) 預防檢查清單

- [ ] 沒有任何無上限的東西送到單一地方 —— driver、client 或 coordinator。
- [ ] Partition 有刻意調整大小（Spark 中約 100–200 MB），而不是留著預設值。
- [ ] 知道最熱門 key 所佔的比例；先修正資料傾斜，再加記憶體。
- [ ] 每個 join 的 build / broadcast side 確實很小。
- [ ] 檢查過 join 的放大倍數（前後各做一次 `COUNT(*)`）。
- [ ] 每個查詢的記憶體 × 並行度放得進機器（`work_mem`、Trino 查詢上限、pool 大小）。
- [ ] Client 用 fetch size 或 server-side cursor 串流讀取。
- [ ] 加記憶體是最後的修正手段，而且有記錄*為什麼*需要它。

---

<!-- bf648f1e0b93 -->
## 5) 資料的重新排序與合併 ⭐⭐⭐⭐⭐

<!-- 206bb128176d -->
### 5-1) 為什麼磁碟上的順序很重要

**依查詢會過濾的欄位排序資料，能讓每個檔案的 min/max 範圍變窄，讀取端就能跳過大部分檔案；
它也能改善壓縮，並讓 join 或聚合省掉自己的排序。**

<!--CODE-->

排序過的資料的好處：

- **Data skipping** —— min/max、zone map 與 bloom filter 能 prune 掉更多資料。
- **壓縮** —— 連續相同的值有利於 run-length 與 dictionary 編碼。
- **免排序的 operator** —— merge join 或串流聚合可以直接利用既有的順序。

代價是寫入時的一次排序，也就是一次 shuffle —— 由 writer 付一次，而不是每個讀取者每次讀取都付。

<!-- 94a0977374fa -->
### 5-2) Sort、cluster 還是 Z-order

**線性排序對單一開頭欄位很有效；Z-order 與 Hilbert 曲線（以及 liquid clustering）把多個欄位
交錯排列，讓對其中任何一欄的過濾都能跳過一部分資料 —— 代價是對第一欄能跳過的資料比純排序少。**

| 佈局 | 適合 | 弱點 |
|---|---|---|
| **線性排序** `(a, b)` | 對 `a`、或同時對 `a` 與 `b` 的過濾 | 只對 `b` 的過濾 |
| **Z-order / Hilbert** `(a, b)` | 對 `a` **或** `b` 的過濾 | 欄位非常多時 —— 超過 3–4 欄效益就會消退 |
| **Liquid clustering**（Delta）/ **sort order**（Iceberg） | 不必改寫整張表就能變更的佈局 | 仍需要定期維護 |
| **Bucketing** | 以 bucket key 做的 join 與聚合 | 對其他欄位的跳過 |

<!--CODE-->

<!--CODE-->

`orderBy` 是**全域**排序（range-partitioned shuffle）；`sortWithinPartitions` 在每個 partition
內部各自排序，而 data skipping 需要的就只有這樣。

<!-- ec3404df789e -->
### 5-3) Compaction：合併小檔案

**Compaction 把許多小檔案改寫成較少的大檔案；bin-packing 便宜，只修正檔案數，而 sort
compaction 還會恢復讓跳過機制有效的佈局。**

| 策略 | 做什麼 | 何時 |
|---|---|---|
| **Bin-pack** | 把小檔案串接到目標大小 | 串流或 micro-batch 的 sink；頻繁執行 |
| **Sort / Z-order** | 改寫並重新排序 | 讀取量大的資料表；較少執行，針對近期分區 |

- 依排程 compact **近期分區**；舊分區早就已經 compact 過了。
- Compaction 也是一次 commit：在樂觀並行控制下，它可能和碰到同一批檔案的 writer 衝突 ——
  把它排在大量寫入的時段之外，或只 compact writer 已經寫完的分區。
- 之後要讓 snapshot 過期，否則小檔案仍然存著。

小檔案及其成因在管線中的視角見
[`./batch_pipeline.md` §7-4](./batch_pipeline.md#7-4-the-small-files-problem-and-compaction)。

<!-- 2690fa0becd8 -->
### 5-4) 合併資料集：`MERGE`、copy-on-write 與 merge-on-read

**`MERGE` 把一批變更 upsert 進資料表；在 lakehouse 中，copy-on-write（寫時複製）會改寫變更碰到的
每個檔案（寫入慢、讀取快），而 merge-on-read（讀時合併）寫入小的 delete 或 delta 檔，由讀取端
即時套用（寫入快，在 compact 之前讀取較慢）。**

| | Copy-on-write | Merge-on-read |
|---|---|---|
| 寫入成本 | 改寫每個被碰到的檔案 | 附加 delete / delta 檔 |
| 讀取成本 | 沒有額外成本 | 讀取時套用 delete |
| 適合 | 批次更新、讀取量大的資料表 | 頻繁的小量更新、CDC 串流 |
| 維護 | — | 定期 compaction，把 delta 併回去 |

陷阱：**來源中每個 key 最多只能有一列。** 如果兩筆來源列對到同一筆目標列，`MERGE` 會報錯
（PostgreSQL：*"MERGE command cannot affect row a second time"*；Delta 與 Spark：
「multiple source rows matched」錯誤）。先去重：

<!--CODE-->

- `s.updated_at > t.updated_at` 這個保護條件讓重播或亂序的批次變得無害。
- 可以的話，把**分區欄位加進 `ON` 子句**（`AND t.event_date = s.event_date`），讓引擎 prune
  目標檔案，而不是掃描整張表。

<!-- d914c3cd6e85 -->
### 5-5) 大規模合併排序資料：external merge sort

**資料比記憶體大時，引擎會分成放得進記憶體的區塊排序，把每個排序好的 run 寫到磁碟，再用
min-heap 對這些 run 做串流式的 k-way merge —— sort spill、sort-merge join 與 LSM compaction
背後都是同一個演算法。**

<!--CODE-->

<!--CODE-->

- 成本：N 列、k 個 run 時為 **O(N log k)** 次比較，而每一列大約讀寫兩次 —— 這就是為什麼 spill
  比較慢但仍會完成。
- 如果 run 的數量多於 buffer 能容納的數量，引擎會分好幾輪合併；記憶體越多，run 越少越長，
  合併輪數也越少。

<!-- 476e32ddeb31 -->
### 5-6) LSM compaction：在儲存引擎內部合併

**LSM-tree 儲存（RocksDB、Cassandra、HBase）把寫入變成循序附加，並在背景合併排序好的檔案；
compaction 策略決定你要付哪一種成本。**

| 策略 | 寫入放大 | 讀取與空間放大 | 適合 |
|---|---|---|---|
| **Size-tiered**（Cassandra 的預設） | 低 | 較高 —— 一個 key 可能散在很多檔案中 | 寫入量大、以附加為主 |
| **Leveled**（RocksDB 的預設） | 較高 | 低 —— 每一層大約只需檢查一個檔案 | 讀取量大、更新量大 |
| **Time-window** | 低 | 對帶 TTL 的時間序資料很低 | 時間序列、指標 |

B-tree 與 LSM 的背景知識，以及三種放大，見
[`./sql_vs_nosql.md` §3](./sql_vs_nosql.md#3-2-read-write-and-space-amplification)。

---

<!-- 657b8797c4e2 -->
## 6) 儲存系統的讀取與寫入效能 ⭐⭐⭐⭐⭐

<!-- ebc722752042 -->
### 6-1) 永遠要說出的取捨

**每一個讓讀取變快的結構 —— 索引、排序順序、實體化的副本 —— 都必須在每次寫入時維護，所以
答案的第一句話，是這個工作負載需要的是哪一邊。**

| 讀取最佳化 | 它讓寫入付出什麼 |
|---|---|
| 多一個索引 | 每次 insert / update 多更新一個結構；更多 WAL |
| 排序或 cluster 過的佈局 | 寫入時排序，或之後重新 cluster |
| 反正規化 / 實體化的副本 | 每次變更寫兩次；刷新延遲 |
| Leveled compaction | 更多背景改寫 |
| Read replica | 複寫延遲；primary 仍要承接每一筆寫入 |

這就是 **RUM** 取捨 —— 讀取開銷、更新開銷與記憶體 / 空間開銷：你能降低其中任兩個，
無法三個都降。

<!-- ea96e3ba5edc -->
### 6-2) 更快的寫入

**送出較少但較大的批次、每一列維護較少的結構，以及循序而非隨機寫入，寫入就會變快。**

| 技巧 | 為什麼有幫助 |
|---|---|
| 用**大量載入**（`COPY`、`LOAD DATA`）取代逐列 `INSERT` | 一個 statement、一次解析、最少的來回 |
| **批次** insert，每 N 列 commit 一次 | 每次 commit 一次 fsync，而不是每列一次 |
| 大型回補期間**移除或延後建立次要索引**，之後再重建 | 一次建好索引比每列維護便宜 |
| 寫入量大的資料表**少建索引** | 每個索引都是每列一次寫入 |
| 在 B-tree 中用**循序 key**（`BIGINT` identity、UUIDv7）取代隨機的 UUIDv4 | insert 附加到最右邊的頁面，而不是分裂隨機的頁面 |
| 對大資料表做**分區** | 索引更小；舊分區直接 drop，而不是 `DELETE` |
| 寫入率非常高時用**以 LSM 為基礎的儲存** | 寫入是循序附加 |
| 在可以接受遺失最後片刻資料的地方用**非同步 commit**（`synchronous_commit = off`） | 不必等待 WAL flush |

<!--CODE-->

<!-- b8cbe6011c2c -->
### 6-3) 更快的讀取

**當查詢需要的資料不必掃描就能找到、已經在記憶體中，或已預先計算成查詢要的形狀時，讀取就會變快。**

| 技巧 | OLTP | 分析 |
|---|---|---|
| **索引** —— 複合、covering、partial | ✅ 主要手段 | 很少用（大多數資料倉儲沒有 B-tree） |
| **為 data skipping 排序 / cluster** | — | ✅ 主要手段 |
| **快取** —— buffer pool、Redis、BI 快取 | ✅ | ✅ 用於儀表板 |
| **Read replica** | ✅ 擴充讀取、分擔抽取工作 | — |
| **Materialised view / 彙總表** | 有時 | ✅ |
| **分區** | pruning + 更小的索引 | pruning |
| **Columnar 儲存** | — | ✅ |

索引規則見 [`./sql_for_data.md` §10-1](./sql_for_data.md#10-1-indexes-in-an-oltp-database)；
如何判讀索引有沒有被用到見 [`./sql_explain.md`](./sql_explain.md)。

<!-- 2fc143a015c5 -->
### 6-4) 物件儲存與 data lake

**在 S3 類的儲存上，成本是按請求與按檔案計，而不是按 seek，所以效能來自較少、較大的檔案、
能避免列目錄的 metadata，以及分散在多個 prefix 的 key。**

- S3 每個 prefix 至少能承受**每秒 3,500 次寫入與 5,500 次讀取**；單一熱門 prefix 會被節流
  （`503 Slow Down`）—— 把 key 分散到多個 prefix（分區本來就會這樣）。
- **列目錄很慢而且要分頁**；table format（Iceberg、Delta）改從 metadata 讀取檔案清單。
- 在物件儲存上 **rename 就是複製** —— 靠 rename 暫存目錄來 commit 的協定既慢又不安全；
  table format 改以寫入一個 metadata 指標來 commit。
- Parquet reader 會**先抓 footer**，再用 ranged GET 只抓它需要的 column chunk —— 所以只讀
  少數欄位的寬檔案很便宜，小檔案則不然。

<!-- 6ca6aadaaddb -->
### 6-5) 熱點與競爭

**系統的速度取決於它最忙的那個分區、鎖或 key，所以要讓寫入平均分散，避免把每一筆更新都送到
同一個地方。**

- **分散式儲存中的單調遞增 key**（以時間戳作為 HBase / Bigtable row key 的前綴、循序的 DynamoDB
  partition key）會把每一筆新寫入都送到同一個節點 —— 在前面加上 hash 或 bucket 編號。
- **熱門計數器**（`UPDATE stats SET views = views + 1 WHERE id = 1`）會在同一列的鎖上排隊 ——
  把計數器切成 N 列、讀取時加總，或先在串流中聚合。
- PostgreSQL 中的**長交易**會擋住 vacuum，於是 dead row 累積、讀取變慢 —— 讓批次交易保持
  簡短並分塊進行（見 [§7-4](#7-4-large-updates-and-deletes-in-chunks)）。

---

<!-- 5db0ad8c9d61 -->
## 7) 讓查詢變快的 SQL 改寫 ⭐⭐⭐⭐⭐

<!-- 12520baa443c -->
### 7-1) 改寫對照表

**大多數慢 SQL 的修正方式，是給 optimizer 一個它用得上的 predicate、在昂貴的步驟之前先減少
列數，或是要求比查詢目前要求的更少的東西。**

| 慢的寫法 | 較快的寫法 | 原因 |
|---|---|---|
| `WHERE DATE(created_at) = '2026-11-01'` | `WHERE created_at >= '2026-11-01' AND created_at < '2026-11-02'` | sargable：能用上索引或 partition pruning |
| 回傳大量重複的 `WHERE id IN (SELECT …)`，或先 `JOIN` 再 `DISTINCT` | `WHERE EXISTS (SELECT 1 …)` | semi-join 在第一個符合就停，而且從不放大列數 |
| `WHERE id NOT IN (SELECT col …)` | `WHERE NOT EXISTS (…)` | `col` 含 `NULL` 時仍然正確，而且會規劃成 anti-join |
| 先 join 明細，再 `GROUP BY` | 先對明細 `GROUP BY`，再 join | 抵達 join 的列數更少 |
| `UNION` | 各部分不可能重疊時用 `UNION ALL` | 沒有去重用的 sort 或 hash |
| `SELECT *` | 你會用到的欄位 | columnar 引擎只讀那些欄；covering index 成為可能 |
| `ORDER BY … OFFSET 100000 LIMIT 50` | keyset：`WHERE (created_at, id) < (?, ?) ORDER BY … LIMIT 50` | `OFFSET` 會讀取並丟掉每一筆被跳過的列 |
| 跨兩個有索引欄位的 `WHERE a = ? OR b = ?` | 兩個有索引的查詢以 `UNION ALL` 合併（排除重疊） | 每個分支都用自己的索引 |
| 每列一次的 correlated subquery | join 或 window function | 一次掃描，而不是每列一次查找 |
| 為了「前一列」做 self-join | `LAG()` | 一次排序，而不是一次 join |
| 儀表板上精確的 `COUNT(DISTINCT user_id)` | `APPROX_COUNT_DISTINCT` | 一個小 sketch，而不是完整的 distinct 集合 |

接著**用 `EXPLAIN` 確認** —— optimizer 已經幫你做過的改寫不會帶來任何好處，而 PostgreSQL 12+
已經會把只被引用一次的非遞迴 CTE inline（除非標成 `MATERIALIZED`）。

<!-- 065fcf8ff82d -->
### 7-2) 改寫實例：先聚合再 join

**在十億筆訂單上算「每個國家的營收」，不需要讓十億列抵達 join —— 只需要每個使用者一列。**

<!--CODE-->

它回傳相同的答案，因為 `SUM` 是可分解的 —— 每個使用者小計的總和就是總計。它**不**適用於跨 join
的 `COUNT(DISTINCT …)`，也不適用於以「平均的平均」計算的平均值（見 [`./analytics.md` §2-3](./analytics.md#2-3-ratio-metrics-and-the-averaging-trap)）。

<!-- 3ce96480e5ec -->
### 7-3) 改寫實例：keyset 分頁

<!--CODE-->

Tie-breaker（`order_id`）讓順序成為全序，所以兩列時間戳相同時也不會有列被跳過或重複。
代價：不能「跳到第 2001 頁」—— 只有下一頁 / 上一頁。

<!-- 50715e2d9b64 -->
### 7-4) 分塊進行大量更新與刪除

**一次刪除兩億列的 `DELETE` 會在整段期間持有鎖、一口氣寫出所有 WAL，並留下 vacuum 積壓；分成
有上限的區塊刪除，能讓每個交易都保持簡短。**

<!--CODE-->

- 在分區資料表上更好的做法：**`DROP` 或 detach 舊分區** —— 只動 metadata，不必逐列刪除。
- 在 lakehouse 中，`DELETE` 會改寫檔案（copy-on-write）或寫入 delete 檔（merge-on-read）；
  用分區過濾條件限定範圍，之後再 compact。

---

<!-- a2011fd13548 -->
## 8) 快問快答 ⭐⭐⭐⭐

| 問題 | 答案 |
|---|---|
| Data lake 用 Parquet 還是 Avro？ | 分析用資料表用 Parquet；訊息與原始逐列落地用 Avro |
| 為什麼很多小檔案會慢？ | 每個都要一次開檔、一筆 metadata，而且常常一個 task；讀 10 000 個 1 MB 的檔案遠比讀 10 個 1 GB 的慢 |
| 寫入前用 `repartition` 還是 `coalesce`？ | 用 `coalesce` 便宜地減少檔案數；當每個分區的檔案必須少且平均時，依分區欄位 `repartition` |
| Spark driver OOM —— 第一個要找什麼？ | `collect()` / `toPandas()`、過大的 broadcast，或是對數百萬個檔案做規劃 |
| Executor OOM —— 第一件事？ | 該 stage 最大 task 的輸入量 vs 中位數：資料傾斜，或 shuffle partition 太少 |
| PostgreSQL 查詢很慢，計畫寫著 `external merge` | sort spill 了：為該 session 調高 `work_mem`，或排序更少的列 / 使用本身已有順序的索引 |
| Trino 查詢因記憶體上限失敗 | 把 build（右）側變小、收集統計資訊、用 partitioned join、預先聚合 |
| 怎麼加快大量載入？ | `COPY`、大批次、移除再重建次要索引、循序 key |
| 索引讓讀取變快 —— 為什麼不全部建索引？ | 每個索引都會拖慢每一次寫入並佔用空間；為你實際執行的查詢建索引 |
| Z-order 還是 sort？ | 單一欄位主導過濾條件時用 sort；兩三個欄位使用頻率差不多時用 Z-order |
| Copy-on-write 還是 merge-on-read？ | 讀取量大、批次更新的資料表用 CoW；頻繁小量更新用 MoR，之後再 compact |
| 為什麼 `MERGE` 會以「affect row a second time」失敗？ | 來源中一個 key 有兩列 —— 先用 `ROW_NUMBER()` 去重 |
| `OFFSET` 分頁在深頁很慢 | 改用以有索引、唯一的排序 key 做的 keyset 分頁 |
| 資料庫怎麼排序比記憶體大的資料？ | external merge sort：磁碟上的排序 run，再做 k-way heap merge |
