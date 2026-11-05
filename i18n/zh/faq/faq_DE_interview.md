<!-- a042bae39fca -->
# 資料工程面試 FAQ

> **範圍** — 資料工程師面試會探的核心知識，一個主題一節：Spark、Kafka、Hadoop 生態系、資料建模、分析用 SQL、批次與串流管線、schema 設計，以及 SQL vs NoSQL —— 每一題都先給你會說出口的那句答案，再講機制，最後講架構。
> **另見**：[`spark/faq_spark_hadoop.md`](./spark/faq_spark_hadoop.md)、[`kafka/faq_kafka.md`](./kafka/faq_kafka.md)、[`flink/faq_flink.md`](./flink/faq_flink.md)、[`sql/faq_sql.md`](./sql/faq_sql.md)、[`faq_data_model.md`](./faq_data_model.md) 與 [`db/faq_DB_DW.md`](./db/faq_DB_DW.md) —— 各工具更長的筆記；這份檔案是橫跨它們的面試地圖。

每一節都先給一句話的答案，再講機制，然後是面試官下一步會追問的取捨。
星號代表這個主題在 DE 面試中出現的頻率。

---

<!-- 5aed11828baf -->
## 1) Apache Spark ⭐⭐⭐⭐⭐

<!-- 899a92993dd4 -->
### 1-1) 架構：driver、cluster manager、executor

**Spark 是分散式運算引擎：一個 driver 負責規劃 job，許多 executor 平行執行，
cluster manager 負責分配機器。**

| 元件 | 角色 |
|---|---|
| **Driver** | 執行你的 `main()`，持有 `SparkSession`，建立 DAG、排程 task、收集結果 |
| **Cluster manager** | 分配資源 —— YARN、Kubernetes、Standalone（Mesos 已棄用） |
| **Executor** | worker 節點上的一個 JVM；執行 task、快取資料、寫 shuffle 檔 |
| **Task** | 最小工作單位：一個 task 處理**一個 partition** |

<!--CODE-->

- **部署模式**：`client`（driver 在提交的那台機器上 —— 適合 notebook）vs
  `cluster`（driver 在叢集內 —— 正式環境的 job 用這個）。
- 平行度 = partition 數；同時可執行的 task 槽位 = executors × 每個 executor 的 cores。
  一個 200 個 partition 的 stage 跑在 50 個槽位上（例如 10 個 executor × 5 個 core），
  要分 4 波（wave）執行 —— 實際吞吐量仍取決於工作負載與 I/O。

<!-- c3deac918de4 -->
### 1-2) Lazy evaluation、DAG、stage 與 shuffle

**Transformation 是 lazy 的，只會建立執行計畫；action 才會觸發執行，而計畫在每一次
shuffle 處被切成 stage。**

| | Transformation | Action |
|---|---|---|
| 例子 | `select`、`filter`、`map`、`join`、`groupBy` | `count`、`collect`、`show`、`write`、`take` |
| 回傳 | 新的 DataFrame / RDD | 回到 driver 的值，或寫到儲存的資料 |
| 會執行嗎？ | 不會 —— 只是加進計畫 | 會 —— 提交一個 **job** |

- **Narrow dependency（窄依賴）** —— 每個輸出 partition 只依賴一個輸入 partition
  （`map`、`filter`、`union`）。資料不搬動；Spark 把它們串成同一個 stage。
- **Wide dependency（寬依賴）** —— 一個輸出 partition 需要很多輸入的資料（`groupBy`、
  `join`、`distinct`、`repartition`）。這就是 **shuffle**：寫到本機磁碟、透過網路傳輸、
  再讀回來。它會結束一個 stage。

<!--CODE-->

為什麼 lazy evaluation 重要：Catalyst 在執行前就看到整個計畫，所以能把 filter 下推到
讀取端、裁掉用不到的欄位，並挑選 join 策略。

<!-- d53dfc5bc146 -->
### 1-3) RDD vs DataFrame vs Dataset、Catalyst 與 Tungsten

**用 DataFrame：它帶有 schema，所以 Catalyst 能最佳化它、Tungsten 能把它存得很緊湊；
RDD 是 Spark 看不進去的不透明函式。**

| | RDD | DataFrame | Dataset |
|---|---|---|---|
| Schema | 沒有 | 有（由欄位組成的列） | 有，且有型別（`Dataset[T]`） |
| 最佳化器 | 沒有 | Catalyst | Catalyst |
| 語言 | 全部 | 全部 | 只有 Scala / Java |
| 何時用 | 需要底層控制、非結構化資料 | 幾乎所有情況 | 在 Scala 要編譯期型別檢查 |

- **Catalyst** —— 查詢最佳化器：解析後的計畫 → 分析 → 最佳化的邏輯計畫
  （predicate pushdown、常數折疊、column pruning）→ 多個實體計畫，依成本選一個。
- **Tungsten** —— 執行引擎：off-heap 二進位列格式、whole-stage code generation
  （把多個運算子融合成一個生成的 Java 函式）。
- Python UDF 會同時破壞這兩者：每一列都要序列化送到 Python worker 再送回來。優先用
  內建函式；真的需要 UDF 時，用向量化的 `pandas_udf`（Arrow 批次）。

<!--CODE-->

<!-- dea76fe5e641 -->
### 1-4) 分區：`repartition` vs `coalesce`，以及小檔案問題

**`repartition(n)` 會做完整 shuffle，可增可減；`coalesce(n)` 只合併既有的 partition、
不做 shuffle，所以只能減少。**

| | `repartition(n)` / `repartition(col)` | `coalesce(n)` |
|---|---|---|
| Shuffle | 有 | 沒有 |
| 結果 | 大小平均的 partition | 可能不平均 |
| 用途 | 提高平行度、寫入前按 key 聚在一起 | 在最後減少輸出檔案數 |

- `spark.sql.shuffle.partitions`（預設 **200**）決定 shuffle 之後的 partition 數 ——
  TB 級 job 設太低會出現巨大且 spill 的 task，小 job 設太高會產生一堆超小 task。
  **AQE**（`spark.sql.adaptive.enabled`，3.2 起預設開啟）會在執行期自動合併它們。
- **經驗法則**：每個 partition 約 100–200 MB，每個 core 2–4 個 task。
- **小檔案** —— 寫出 10,000 個小檔會拖慢之後每一個讀取者（每個檔案一個 task，加上
  HDFS 中介資料 / S3 列目錄的開銷）。解法：寫入前 `coalesce` / `repartition`，
  或跑壓實（compaction）job（Delta `OPTIMIZE`、Iceberg `rewrite_data_files`）。
- 寫入時的 `partitionBy("dt")` 是給 pruning 用的**目錄結構**，和記憶體裡的 partition
  不是同一回事 —— 而用高基數欄位做 partitionBy 會讓檔案數爆炸。

<!-- e309c06fd026 -->
### 1-5) Join 與資料傾斜 ⭐⭐⭐⭐⭐

**一邊很小時 Spark 選 broadcast hash join，否則選 sort-merge join；資料傾斜（skew）是指
少數幾個 key 握有大部分的列，於是一個 task 跑一小時，其他的一分鐘就結束。**

| 策略 | 何時 | Shuffle |
|---|---|---|
| **Broadcast hash join** | 一邊小於 `spark.sql.autoBroadcastJoinThreshold`（預設 10 MB）或有 hint | 沒有 —— 小的那邊複製到每個 executor |
| **Sort-merge join** | 兩邊都大、等值 join（預設） | 兩邊都要 |
| **Shuffle hash join** | 一邊小很多但大到不能 broadcast | 兩邊都要 |
| **Broadcast nested loop** | 非等值 join | —— 而且很慢；避免 |

怎麼看出傾斜：在 Spark UI 中，某個 stage 的最大 task 時間是中位數的 10 倍，而且它的
shuffle read 量遠大於其他 task。

怎麼修，依你嘗試的順序：

1. **AQE skew join** —— `spark.sql.adaptive.skewJoin.enabled=true` 會自動切開過大的 partition。
2. **Broadcast** 小的那邊，讓大的那邊根本不用 shuffle。
3. **把熱點 key 過濾掉或另外處理** —— 它常常是 `NULL` 或測試帳號；其餘正常 join 後再 `union`。
4. **Salting（加鹽）** —— 在大表的傾斜 key 上加一個隨機後綴，小表則按每個後綴複製一份。

<!--CODE-->

<!-- 2798b3b79d3f -->
### 1-6) 記憶體、快取與常見的失敗

**Executor 記憶體分成 execution（shuffle、join、sort）與 storage（快取），兩者可以互相
借用；多數 OOM 不是 driver 上的 `collect()`，就是 executor 上某個傾斜的 partition。**

- 對 DataFrame 來說 `cache()` = `persist(MEMORY_AND_DISK)`。只快取會被**多個 action
  重複使用**的東西，用完 `unpersist()`。快取本身也是 lazy 的。
- **Driver OOM**：對大結果做 `collect()` / `toPandas()`，或 broadcast 了太大的東西。
  改成寫到儲存，或用 `take(n)`。
- **Executor OOM / spill**：partition 傾斜、shuffle partition 太少、broadcast 太大。
  調高 `spark.sql.shuffle.partitions`、修掉傾斜、加大 `executor.memoryOverhead`
  （Python worker 與 off-heap 都住在那裡）。
- **Checkpointing** 把資料寫到可靠儲存，截斷過長的 lineage（反覆迭代的 job）。
- 容錯來自 **lineage**：遺失的 partition 由它的上游重新計算，而不是從副本還原。

---

<!-- 235d2a9ee24a -->
## 2) Apache Kafka ⭐⭐⭐⭐⭐

<!-- e45b4371fac8 -->
### 2-1) 架構：broker、topic、partition、replica

**Kafka 是一個分散式、有副本、只能附加的 commit log：producer 往 partition 附加資料，
consumer 用各自的 offset 讀取，而資料會保留到 retention 期限為止，不論有沒有人讀過。**

| 名詞 | 意義 |
|---|---|
| **Broker** | 一台 Kafka 伺服器；叢集由多台組成 |
| **Topic** | 一個有名字的紀錄串流 |
| **Partition** | 有序、不可變的 log；是平行度的單位，**也是**順序的單位 |
| **Offset** | 一筆紀錄在其 partition 內的位置 |
| **Replica** | partition 在另一台 broker 上的副本；一個是 **leader**，其他是 **follower** |
| **ISR** | In-sync replicas —— 跟上 leader 進度的 follower |
| **Controller** | 管理 leader 選舉與中介資料 —— 3.x 起用 **KRaft**（Kafka 內建的 Raft quorum）；4.0 移除了 ZooKeeper |

<!--CODE-->

為什麼快：順序寫磁碟、OS page cache、用零拷貝 `sendfile` 送給 consumer，以及端到端的
批次 + 壓縮。

<!-- c3a54f7f4027 -->
### 2-2) Producer：key、`acks`、冪等性

**Key 決定 partition，所以相同的 key 永遠落在同一個 partition、保持順序；
`acks=all` 搭配 `min.insync.replicas=2` 才讓一次寫入撐得過 broker 故障。**

| `acks` | 等待 | 風險 |
|---|---|---|
| `0` | 什麼都不等 | 任何故障都會遺失 |
| `1` | 只等 leader | leader 在 follower 複製前掛掉就遺失 |
| `all`（`-1`） | 目前所有 ISR 成員 | 只有所有同步副本同時失效才會遺失 —— 前提是 replication factor ≥ 3、`min.insync.replicas = 2`，且 `unclean.leader.election.enable=false`（預設值） |

- Partition = `hash(key) % numPartitions`；沒有 key → 跨批次的 sticky partitioning。
  **事後增加 partition 會重新對應 key**，打破每個 key 的順序。
- `enable.idempotence=true`（3.0 起為預設）給每個 producer 一個 id 與每個 partition 的
  序號，所以重試不會寫出重複資料。
- 吞吐量的旋鈕：`batch.size`、`linger.ms`、`compression.type`（`lz4` / `zstd`）。

<!-- a18054cae202 -->
### 2-3) Consumer group、offset 與 rebalance

**一個 group 裡，每個 partition 只會被一個 consumer 讀，所以 partition 數就是 group
平行度的上限；consumer 提交（commit）offset，才知道從哪裡繼續。**

<!--CODE-->

- Offset 存在內部 topic `__consumer_offsets`。
- **處理完才 commit** → at-least-once（當機會重播）。**先 commit 再處理** →
  at-most-once（當機會遺失）。Auto-commit 是一個計時器，所以兩種都可能發生。
- **Rebalance** —— consumer 加入、離開或超過 `max.poll.interval.ms` 時，partition 會被
  重新分配。Eager 協定會讓整個 group 停下來；cooperative-sticky 分配（以及 4.0 的
  KIP-848 協定）只搬必須搬的部分。
- **Consumer lag** = 最新 offset − 已提交 offset；第一個該設告警的指標。

<!-- fc21c57bc411 -->
### 2-4) 投遞語意與 exactly-once ⭐⭐⭐⭐⭐

**At-least-once 是你設計時的預設；Kafka 內部的 exactly-once 需要冪等 producer 加上
transaction，而要 exactly-once 寫到外部系統，則需要冪等或支援交易的 sink。**

| 語意 | 怎麼做 | 故障時的結果 |
|---|---|---|
| At-most-once | 先 commit 再處理 | 訊息遺失 |
| At-least-once | 先處理再 commit | 訊息重複 |
| Exactly-once | 冪等 producer + transaction + `isolation.level=read_committed` | 兩者皆無 |

- Kafka transaction 讓「從 A 讀 → 寫到 B → 提交 offset」成為原子操作。Kafka Streams 的
  `processing.guarantee=exactly_once_v2` 和 Flink 的 Kafka sink 用的就是它。
- 寫入資料庫：讓寫入**冪等** —— 以自然鍵做 upsert，或把 offset 和資料存在同一個 DB
  transaction 裡。

<!-- 2ce5eab57a9a -->
### 2-5) 保留、log compaction 與順序

**Delete 保留策略依時間或大小丟掉舊 segment；compaction 只保留每個 key 的最新值 ——
一份可以用來重建表格的 changelog。**

- `cleanup.policy=delete` —— `retention.ms`（預設 7 天）/ `retention.bytes`。
- `cleanup.policy=compact` —— 保留每個 key 的最後一筆；值為 `null`（tombstone）會刪除該
  key。用在 CDC 表、`__consumer_offsets`、Kafka Streams 狀態 changelog。
- **順序**只在**單一 partition 內**有保證。需要全域順序 → 只用一個 partition（沒有平行度）。
  需要每個實體的順序 → 以實體 id 當 key。
- 有重試時，`max.in.flight.requests.per.connection ≤ 5` 加上冪等性就能維持順序。

<!-- 465505662027 -->
### 2-6) Kafka vs 傳統訊息佇列

**佇列在訊息被消費後就刪掉；Kafka 保留整個 log，所以多個獨立的 consumer 都能讀，
而且任何一個都能重播。**

| | Kafka | RabbitMQ / SQS |
|---|---|---|
| 模型 | 分區的 log，pull | 佇列，push（Rabbit）/ pull（SQS） |
| 消費之後 | 保留到 retention 期限 | 刪除 |
| 重播 | 可以 —— 重設 offset | 不行 |
| 順序 | 每個 partition 內 | 每個佇列內（FIFO 變體） |
| 強項 | 高吞吐事件流、fan-out、CDC | 任務佇列、逐訊息路由、低流量 |

---

<!-- 1408f185ae29 -->
## 3) Hadoop 生態系 ⭐⭐⭐

<!-- 199470295d94 -->
### 3-1) HDFS：NameNode、DataNode、block

**HDFS 把檔案切成大 block（預設 128 MB），每個 block 在不同 DataNode 上存三份副本，
而所有中介資料都放在一台 NameNode 的記憶體裡。**

| 元件 | 存放 |
|---|---|
| **NameNode** | 命名空間與 block → DataNode 的對應，放在記憶體；edit log 與 fsimage 在磁碟上 |
| **DataNode** | block 本身；送出心跳與 block report |
| **Standby NameNode** | HA 容錯切換，透過 JournalNode 保持同步 |

- **Rack awareness（機架感知）**：副本 1 在本機，副本 2、3 在另一個機架 —— 撐得過整個機架故障。
- 一次寫入、多次讀取：可以 append，不能隨機更新。
- **小檔案很傷**，是因為每個檔案都要佔 NameNode 記憶體（每個物件約 150 bytes），
  而不是因為磁碟空間。
- **Data locality（資料在地性）**：把運算排到持有該 block 的節點上，搬程式碼而不是搬資料。

<!-- c147ad9fea06 -->
### 3-2) MapReduce：map、shuffle、reduce

**Map 把每個輸入 split 轉成 key-value 對，shuffle 透過網路依 key 分組，reduce 對每個
key 做彙總 —— 每一步都要寫磁碟，這就是 Spark 取代它的原因。**

<!--CODE-->

- **Combiner** = map 端的 reduce；減少 shuffle 量（只對 sum 這類可結合的運算有效）。
- **Spark vs MapReduce**：Spark 跨 stage 把中間資料留在記憶體，並建立完整的 DAG；
  MapReduce 每個 job 之間都寫回 HDFS。在反覆迭代的工作上，Spark 通常快 10–100 倍。

<!-- 55ea24666f07 -->
### 3-3) YARN：資源管理器

**YARN 把資源管理和運算分開，所以 MapReduce、Spark、Flink、Hive 可以共用同一個叢集。**

| 元件 | 角色 |
|---|---|
| **ResourceManager** | 全叢集的排程器（Capacity / Fair scheduler、佇列） |
| **NodeManager** | 每個節點一個；啟動並監控 container |
| **ApplicationMaster** | 每個應用程式一個；協商 container 並追蹤自己的 task |
| **Container** | 一個節點上的一份 CPU + 記憶體 |

<!-- 6395ff5cee41 -->
### 3-4) 生態系地圖，以及取代它們的東西

**知道每個工具做什麼、現代技術棧改用什麼 —— 面試官兩個都會問。**

| 工具 | 它是什麼 | 現代對應 |
|---|---|---|
| **Hive** | 在 HDFS 上跑 SQL，編譯成 MR / Tez / Spark；以及 **Hive Metastore** | Spark SQL、Trino、雲端資料倉儲；Metastore 仍然存在（或 Glue / Unity Catalog） |
| **HBase** | 寬欄式、在 HDFS 上隨機讀寫（Bigtable 模型） | Bigtable、Cassandra、DynamoDB |
| **Pig** | 跑在 MR 上的資料流腳本 | Spark |
| **Sqoop** | RDBMS ↔ HDFS 的大量複製 | Spark JDBC、Debezium CDC、託管的 connector |
| **Flume** | 把 log 收集進 HDFS | Kafka + Kafka Connect |
| **Oozie** | 工作流排程器 | Airflow、Dagster |
| **ZooKeeper** | 協調、leader 選舉 | 仍在使用；Kafka 已改用 KRaft |
| **Impala / Presto** | 在 HDFS 上的 MPP SQL | Trino、Starburst |
| **HDFS** | 分散式儲存 | 物件儲存（S3、GCS、ADLS）—— 運算與儲存分離 |

最大的轉變：**儲存與運算現在是分開的**（物件儲存 + 彈性運算），而開放表格格式
（Iceberg、Delta、Hudi）補上了 HDFS 目錄從來沒有的 ACID 表。

---

<!-- b940b53106fe -->
## 4) 資料建模 ⭐⭐⭐⭐⭐

<!-- 843c1dfce73a -->
### 4-1) OLTP vs OLAP、正規化 vs 反正規化

**OLTP 模型是正規化的，讓每個事實只寫一次、更新保持一致；OLAP 模型是反正規化的，
讓讀取只掃很少的表、做很少的 join。**

| | OLTP | OLAP |
|---|---|---|
| 工作負載 | 大量依 key 的小讀寫 | 少量大型掃描與彙總 |
| 模型 | 3NF，正規化 | star schema，反正規化 |
| 儲存 | 列式（row-oriented） | 欄式（column-oriented） |
| 例子 | MySQL、PostgreSQL | BigQuery、Snowflake、Redshift、ClickHouse |
| 最佳化目標 | 寫入正確性、併發 | 讀取吞吐量 |

正規化形式，各一句話：

- **1NF** —— 值是原子的，沒有重複群組。
- **2NF** —— 符合 1NF，且沒有非鍵欄位只依賴複合鍵的一部分。
- **3NF** —— 符合 2NF，且沒有非鍵欄位依賴另一個非鍵欄位（沒有遞移依賴）。

<!-- 1cd098058515 -->
### 4-2) 維度建模：事實、維度、粒度 ⭐⭐⭐⭐⭐

**先宣告粒度（grain）—— 一筆事實列代表什麼 —— 再決定描述它的維度與它承載的度量值。**
Kimball 四步驟：選業務流程 → 宣告粒度 → 找出維度 → 找出事實。

- **事實表（fact table）** —— 業務事件的度量（`amount`、`quantity`），加上指向維度的外鍵；
  非常高、非常窄。
- **維度表（dimension table）** —— 描述性的脈絡（誰、什麼、哪裡、何時）；寬而短。
- **Star schema（星型）** —— 事實表直接 join 反正規化的維度。更快、更簡單；預設選擇。
- **Snowflake schema（雪花型）** —— 維度再正規化成子維度。冗餘較少，join 較多。

<!--CODE-->

度量值的可加性：**可加**（銷售金額 —— 可沿任何維度加總）、
**半可加**（帳戶餘額 —— 可跨帳戶加總，不能跨時間加總）、
**不可加**（比率 —— 改存分子和分母）。

<!-- 5f4a8fd2f004 -->
### 4-3) 事實表類型

| 類型 | 每一列代表 | 例子 |
|---|---|---|
| **Transaction（交易型）** | 一個事件 | 一筆訂單明細 |
| **Periodic snapshot（週期快照）** | 每個實體每個週期 | 每日帳戶餘額 |
| **Accumulating snapshot（累積快照）** | 一個流程實例，隨流程推進而更新 | 訂單：下單 → 付款 → 出貨 → 送達的日期 |
| **Factless（無事實）** | 沒有度量值的事件或涵蓋關係 | 學生出席了課程 |

<!-- 99260c826eda -->
### 4-4) 緩慢變化維度（SCD）⭐⭐⭐⭐⭐

**SCD Type 2 用「關閉舊列、插入新列」保留歷史，讓每筆事實 join 到它發生當時
為真的那個維度版本。**

| 類型 | 行為 | 歷史 |
|---|---|---|
| 0 | 永不改變 | 只有原始值 |
| 1 | 覆寫 | 沒有 |
| 2 | 新增一列 + `valid_from` / `valid_to` / `is_current` | 完整 |
| 3 | 新增 `previous_value` 欄位 | 一步 |
| 4 | 現況表 + 另一張歷史表 | 完整，分開存 |
| 6 | 1 + 2 + 3 合併 | 完整 + 每一列都帶現值 |

<!--CODE-->

事實表存的是維度的**代理鍵**（`customer_sk`），而不是 `customer_id`，
這正是把每筆事實釘在某一個版本上的東西。

<!-- cd12ad320952 -->
### 4-5) Kimball vs Inmon vs Data Vault vs One Big Table

| 做法 | 概念 | 優點 | 缺點 |
|---|---|---|---|
| **Kimball** | 由下而上：每個流程一個一致化（conformed）的星型模型 | 交付快、對業務友善 | 一致化需要紀律 |
| **Inmon** | 由上而下：一個 3NF 企業資料倉儲，再從它建 mart | 單一整合的事實來源 | 建置慢 |
| **Data Vault** | hub（鍵）、link（關係）、satellite（隨時間變化的屬性） | 可稽核、吸收來源變動 | join 很多；上面還需要一層 mart |
| **One Big Table** | 完全攤平的寬表 | 在欄式引擎上查詢最快 | 資料重複、難以修改 |

常見的現代答案：raw / 整合層用 Data Vault 或 staging，mart 層用 Kimball 星型，
某一個熱門 dashboard 再用一張寬表。

---

<!-- 792d92ee23d8 -->
## 5) 資料用 SQL ⭐⭐⭐⭐⭐

<!-- b293a3ca9d08 -->
### 5-1) 邏輯執行順序

**SQL 先跑 `FROM`，`SELECT` 幾乎最後才跑 —— 所以你不能在 `WHERE` 裡過濾 window
function 的結果，也不能在 `WHERE` 裡用 `SELECT` 的別名。**

<!--CODE-->

- `WHERE` 在分組前過濾列；`HAVING` 在分組後過濾群組。
- 要過濾 window 結果，把它包進 CTE / 子查詢（或在 Snowflake、BigQuery、Databricks
  用 `QUALIFY`）。

<!-- 3b797033c091 -->
### 5-2) Window function ⭐⭐⭐⭐⭐

**Window function 在一組相關的列上計算，但不會把它們合併 —— `GROUP BY` 每個群組回傳
一列，window 則保留每一列。**

| 函式 | 用途 |
|---|---|
| `ROW_NUMBER()` | 唯一的 1、2、3 —— 去重、每組取第 1 名 |
| `RANK()` | 同分同名次，之後跳號（1, 1, 3） |
| `DENSE_RANK()` | 同分同名次，不跳號（1, 1, 2） |
| `LAG()` / `LEAD()` | 上一列 / 下一列的值 —— 日對日變化 |
| `SUM() OVER (... ROWS BETWEEN ...)` | 累計總和、移動平均 |
| `FIRST_VALUE()` / `LAST_VALUE()` | frame 中的第一個 / 最後一個（注意預設 frame） |
| `NTILE(n)` | 分成 n 個桶 —— 百分位 |

<!--CODE-->

**陷阱**：有 `ORDER BY` 卻沒寫 frame 時，預設是 `RANGE BETWEEN UNBOUNDED PRECEDING AND
CURRENT ROW` —— 相同 `order_date` 的列會被一起加總，而 `LAST_VALUE` 會回傳目前這一列，
而不是最後一列。

<!-- 7a71fdad85c1 -->
### 5-3) 常被考的題型

**每組 Top-N** 與**去重** —— 在 partition 內排名，保留第 1 名：

<!--CODE-->

**Gaps and islands（連續區間）** —— 連續登入天數：在同一段連續區間內，日期減去列號是常數。

<!--CODE-->

**留存 / 漏斗** —— 條件彙總：

<!--CODE-->

還要準備：中位數 / 百分位（`PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY x)`）、用
`SUM(CASE WHEN ...)` 做樞紐轉置、sessionisation（`LAG` + 30 分鐘間隔旗標 + 累計 `SUM`）。

<!-- 558806bc797d -->
### 5-4) Join 陷阱：NULL、fan-out、anti-join

- **Fan-out（列數放大）** —— 事實表 join 一張每個 key 有多列的表，列數會倍增，`SUM`
  就會重複計算。先彙總再 join；檢查兩邊的粒度。
- **`NULL` 鍵在等值 join 中永遠不會匹配**（`NULL = NULL` 的結果是 unknown）。
- **子查詢裡有 `NULL` 時，`NOT IN`** 會回傳**零列**。改用 `NOT EXISTS` 或
  `LEFT JOIN ... WHERE b.id IS NULL`。
- **在右表上寫 `WHERE` 會把 `LEFT JOIN` 變成 inner join** —— 把條件放進 `ON`。
- `COUNT(*)` 數列數；`COUNT(col)` 跳過 `NULL`；`COUNT(DISTINCT col)` 在大規模下很貴 ——
  能接受 ±2% 誤差時用 `APPROX_COUNT_DISTINCT`（HyperLogLog）。

<!--CODE-->

<!-- 3595f8f3a55d -->
### 5-5) 資料倉儲上的查詢效能

**在欄式資料倉儲上，成本就是掃描的 bytes：只選需要的欄位、在分區欄位上過濾，
讓引擎跳過檔案。**

- **Partition pruning** —— 直接在分區欄位上過濾（`dt = '2026-11-01'`）；用函式包住它
  （`DATE(ts) = ...`）可能讓 pruning 失效。
- **Clustering / sort key** 讓引擎用 min/max 統計（zone map）跳過區塊。
- 在欄式儲存上 `SELECT *` 會讀每一個欄位 —— 最昂貴的習慣。
- 讀 `EXPLAIN`：找全表掃描、join 策略，以及輸出列數最多的那個 stage。
- 把熱門查詢預先彙總成摘要表或 materialised view。

---

<!-- 04fe2c5357c4 -->
## 6) 批次資料管線 ⭐⭐⭐⭐

<!-- d54a908285e8 -->
### 6-1) ETL vs ELT，以及分層架構

**ETL 在載入前轉換；ELT 先把原始資料載進資料倉儲，再在裡面用 SQL 轉換 —— 這是現代的
預設，因為倉儲運算便宜，而保留原始資料讓你可以重新處理。**

| | ETL | ELT |
|---|---|---|
| 在哪轉換 | 另一個引擎（Spark、Informatica） | 資料倉儲 / lakehouse（SQL、dbt） |
| 保留原始資料 | 常常沒有 | 有 |
| 適合 | 大量非 SQL 邏輯、載入前清除 PII | 今天大多數的分析場景 |

<!--CODE-->

Medallion（bronze / silver / gold）分層和 staging → integration → presentation 是同一個
概念：保留原始那一份，所以轉換有 bug 時，修好重跑就好。

<!-- 146faaa63725 -->
### 6-2) 冪等性與回補（backfill）⭐⭐⭐⭐⭐

**當同一份輸入跑兩次得到相同結果時，管線就是冪等的 —— 這個性質讓重試與回補變得安全。**

- **覆寫 partition，不要 append**：每次執行擁有 `dt = {{ ds }}` 並取代它
  （`INSERT OVERWRITE`、Spark dynamic partition overwrite、`MERGE`）。
- **用邏輯日期當參數**，永遠不要用 `now()` —— 回補三月就必須讀三月的資料。
- 沒有按日期分區的表，用自然鍵做 `MERGE` / upsert。
- 把副作用（寄信、呼叫 API）拆成獨立的 task，並加上 key，讓它不會觸發兩次。

<!--CODE-->

<!-- 6bba38379484 -->
### 6-3) 增量載入與 CDC

| 方法 | 怎麼做 | 抓得到刪除嗎？ | 成本 |
|---|---|---|---|
| **全量重載** | 複製整張表 | 可以 | 高；小表沒問題 |
| **依時間戳增量** | `WHERE updated_at > last_watermark` | 不行 | 需要可靠的 `updated_at` |
| **CDC（基於 log）** | 讀資料庫的 binlog / WAL（Debezium → Kafka） | 可以 | 對來源負擔最低，基礎設施較多 |

- 以 `updated_at` 當 watermark 並留一小段重疊窗口，之後再去重 —— 時鐘偏差與長交易會讓
  資料列「在過去」才提交。
- 硬刪除對時間戳查詢是看不見的；用 CDC 或軟刪除旗標。

<!-- e5352c6cef0a -->
### 6-4) 編排（Orchestration）

**編排器負責依賴、排程、重試與回補 —— 不負責轉換邏輯。** 標準答案通常是 Airflow：
由 task 組成的 DAG，依邏輯日期排程。

- 讓 task **原子化且冪等**；資料透過儲存傳遞，不要透過 XCom。
- 用 **sensor**（或 dataset / asset 驅動的排程）等上游資料，而不是猜一個 cron 時間。
- 帶退避的重試、SLA / 告警、用 `catchup` 做回補、用 pool 限制共用資源的併發。
- 其他選擇：Dagster（以資產為中心）、Prefect、雲端原生（Step Functions；Composer 是託管
  的 Airflow）。見 [`faq_Airflow.md`](./faq_Airflow.md)。

<!-- 7636a8c275c4 -->
### 6-5) 資料品質

**像測試程式碼一樣測試資料：在每一層的邊界檢查，在壞資料進到 dashboard 之前就讓管線停下來。**

| 檢查 | 例子 |
|---|---|
| Schema | 預期的欄位與型別 |
| 完整性 | 鍵不可為 null；列數在上週的 ±X% 以內 |
| 唯一性 | 主鍵沒有重複 |
| 有效性 | `amount >= 0`、status 在允許的集合內 |
| 參照完整性 | 每個 `customer_sk` 都存在於 `dim_customer` |
| 新鮮度 | 最新的 partition 在 SLA 內到達 |

工具：dbt tests、Great Expectations、Soda、Deequ。把壞資料隔離（quarantine）而不是
默默丟掉，並和產出資料的團隊訂下**資料契約（data contract）**。

<!-- 30d3e2164021 -->
### 6-6) 檔案格式與表格格式 ⭐⭐⭐⭐

**Parquet 是分析的預設：欄式、壓縮、每個 row group 都有統計資訊讓查詢跳過資料。
Iceberg 或 Delta 這類表格格式在那些檔案之上加入 ACID 交易與 time travel。**

| 格式 | 結構 | 強項 |
|---|---|---|
| CSV / JSON | 列式、文字 | 人看得懂；慢、沒有型別 |
| **Avro** | 列式、二進位、內嵌 schema | 串流與資料擷取；schema 演進最好 |
| **Parquet** | 欄式 | 分析掃描、壓縮、predicate pushdown |
| **ORC** | 欄式 | Hive 生態系，和 Parquet 類似 |

表格格式（**Iceberg、Delta Lake、Hudi**）在 Parquet 檔案之上維護一份交易 log / 中介資料樹，
帶來：原子提交、`MERGE` / `UPDATE` / `DELETE`、schema 演進、time travel、隱藏分區
（Iceberg），以及小檔案壓實。這就是把 data lake 變成 **lakehouse** 的東西。

---

<!-- 50f16a2d1c70 -->
## 7) Schema 設計 ⭐⭐⭐⭐

<!-- 42d13b234949 -->
### 7-1) 從查詢與粒度出發

**寫任何 DDL 之前，schema 設計要先回答三個問題：一列代表什麼、資料會怎麼被查、
它會怎麼變。**

針對「為叫車 app 的分析設計資料表」的一個完整回答：

1. **流程** —— 叫車請求、行程完成、付款。
2. **粒度** —— `fact_trip`：每一趟完成的行程一列。
3. **維度** —— 乘客、司機（SCD2：評分、換城市）、日期、地點（區域）。
4. **度量值** —— 車資、距離、時長、加成倍率。
5. **存取模式** —— 「每城市每天的行程與營收」→ 依 `trip_date` 分區、依 `pickup_zone_sk` 做 cluster（區域可以彙總成城市）。

<!--CODE-->

<!-- c3d350ead43a -->
### 7-2) 鍵：自然鍵 vs 代理鍵

| | 自然鍵 | 代理鍵 |
|---|---|---|
| 是什麼 | 業務識別碼（`email`、`order_no`） | 沒有意義的生成 id（`customer_sk`） |
| 優點 | 有意義，不用查表 | 穩定、小、支援 SCD2 版本、能跨來源 join |
| 缺點 | 可能改變，跨來源系統可能衝突 | 需要查表 / 生成的步驟 |

在資料倉儲的維度表裡兩個都留：代理鍵當主鍵，自然鍵用來查找與去重。在分散式系統裡，
不靠中央計數器生成 id —— UUIDv7 / ULID（依時間排序）或自然鍵的雜湊值。

<!-- 99c258cad677 -->
### 7-3) 分區、clustering 與 bucketing

**依幾乎每個查詢都會過濾的欄位分區，且基數要夠低，讓每個分區都夠大 —— 通常是日期。**

| 技術 | 做什麼 | 選什麼 |
|---|---|---|
| **分區（Partitioning）** | 每個值一個目錄 / 區段；pruning 直接跳過 | 日期；每個分區目標 ≥ ~1 GB |
| **Clustering / sort key** | 在檔案內排序資料；min/max 統計可跳過區塊 | 次常被過濾的欄位（`user_id`、`country`） |
| **Bucketing** | 依 key 雜湊到 N 個檔案 | join key，讓 join 省掉 shuffle |
| **Z-order / liquid clustering** | 多欄位的局部性 | 同時有多個過濾欄位 |

陷阱：依 `user_id` 分區（數百萬個小分區）；UTC 資料卻用當地時間的日期分區；
用一個沒有任何查詢會過濾的欄位分區。

<!-- 51b9e91f1fe4 -->
### 7-4) Schema 演進

**增加式的變更是安全的；改名、縮小型別、移除必要欄位都會打壞下游某個人 —— 所以每次
變更都要依相容性規則檢查。**

| 變更 | 通常安全嗎？ |
|---|---|
| 新增可為 null 的欄位 / 有預設值的欄位 | 安全 |
| 放寬型別（`INT` → `BIGINT`） | 通常安全 |
| 欄位改名 | 不安全 —— 新增新欄位、回補、淘汰舊欄位 |
| 刪除欄位 | 會打壞用到它的讀取者 |
| 縮小型別 / 改變意義 | 不安全 |

- **Schema Registry**（Avro / Protobuf / JSON Schema）在每個 topic 上強制 `BACKWARD`
  （新的讀取者能讀舊資料 —— 預設）、`FORWARD` 或 `FULL` 相容性。
- Iceberg 以 id 追蹤欄位，所以改名與調整順序只動中介資料。Delta 要先開啟 column mapping
  （`delta.columnMapping.mode = 'name'`，會升級表格的 protocol）才有同樣的行為。
- 透過**資料契約**協議變更，破壞性的變更要做版本化（`orders_v2`）。

<!-- 7a2786683140 -->
### 7-5) 資料型別與慣例

- 金額 → `DECIMAL(p, s)`，永遠不要用 `FLOAT`（捨入誤差）。
- 時間戳 → UTC，用 `TIMESTAMP WITH TIME ZONE` 或明確的 `_utc` 後綴；當地時間的日期只當
  衍生欄位保留。
- 鍵與必要欄位加 `NOT NULL` —— 它表達意圖，也能抓到壞的載入。
- 列舉值 → 一張查找維度表或受限制的字串，不要用魔術數字。
- 每張表都有稽核欄位：`created_at`、`updated_at`、`_ingested_at`、`_source`、`_batch_id`。
- 一致的命名：`snake_case`、`fact_` / `dim_` 前綴，自然鍵用 `_id`，代理鍵用 `_sk`。
- **半結構化**資料（JSON）→ 先原樣落進 `VARIANT` / `JSON` / `STRUCT` 欄位，
  再把查詢真正會用到的欄位提升成有型別的欄位。

---

<!-- c38b14ba7baa -->
## 8) 串流資料管線 ⭐⭐⭐⭐

<!-- f722e7988b28 -->
### 8-1) 批次 vs 串流、Lambda vs Kappa

**批次依排程處理有界的資料集；串流持續處理無界的資料。只有在延遲值得那份維運成本時
才選串流。**

| | 批次 | 串流 |
|---|---|---|
| 資料 | 有界 | 無界 |
| 延遲 | 分鐘–小時 | 毫秒–秒 |
| 正確性 | 簡單 —— 資料都到齊了 | 遲到與亂序的資料 |
| 重新處理 | 重跑該 partition | 從 log 重播 |
| 執行成本 | 較低 | 永遠在線、有狀態 |

- **Lambda 架構** —— 一個批次層（準確）加上一個速度層（快速），在服務時合併。
  兩套程式碼算同一件事。
- **Kappa 架構** —— 只有串流；重新處理時，把 Kafka log 重播進新版本的 job。
  一套程式碼；仰賴長的 retention。
- 今天，能用同一份程式碼跑批次與串流的引擎（Spark、Flink、Beam）加上 lakehouse 表，
  讓大多數新設計都偏向 Kappa。

<!--CODE-->

<!-- 9e740634041b -->
### 8-2) 事件時間、watermark 與視窗 ⭐⭐⭐⭐⭐

**依事件時間（事情發生的時間）彙總，而不是處理時間；watermark 是引擎估計「不會再有比
*T* 更舊的事件進來」，它決定一個視窗何時能關閉。**

| 視窗 | 形狀 | 例子 |
|---|---|---|
| **Tumbling（滾動）** | 固定大小、不重疊 | 每分鐘點擊數 |
| **Sliding / hopping（滑動）** | 固定大小、會重疊 | 每 1 分鐘算一次 10 分鐘平均 |
| **Session（會話）** | 依間隔、每個 key 各自 | 使用者的活動，直到閒置 30 分鐘 |

- Watermark = 目前看到的最大事件時間 − 允許的遲到時間。落在 watermark 之後的紀錄是
  **遲到資料**：丟掉、送到 side output，或更新已經輸出的結果。
- 允許的遲到時間越長 → 結果越完整、狀態越多、延遲越高。

<!--CODE-->

<!-- f0f45c046356 -->
### 8-3) 狀態、checkpoint 與端到端 exactly-once ⭐⭐⭐⭐

**端到端 exactly-once 需要三個部分：可重播的來源、和來源 offset 一起做 checkpoint 的
引擎狀態，以及冪等或支援交易的 sink。**

- **狀態** —— 累計彙總、join 緩衝、去重集合；存在 state backend（RocksDB），並用 TTL
  或 watermark 限制大小，否則會無限成長。
- **Checkpoint** —— 狀態 + 來源 offset 的一致性快照（Flink：Chandy-Lamport barrier）。
  故障時還原快照，並從那些 offset 重播。
- **Sink** —— Kafka transaction、兩階段提交（Flink `TwoPhaseCommitSinkFunction`）、
  交易式表格提交（Delta / Iceberg），或以確定性 id 做 upsert。
- 做不到這些時，誠實的答案是 **at-least-once + 冪等寫入**，這也是大多數正式環境
  管線實際在跑的方式。

<!-- 667f295abacc -->
### 8-4) Spark Structured Streaming vs Flink vs Kafka Streams

| | Spark Structured Streaming | Apache Flink | Kafka Streams |
|---|---|---|---|
| 模型 | micro-batch（continuous mode 仍是實驗性） | 真正的逐筆處理 | 逐筆處理的函式庫 |
| 延遲 | ~100 ms–秒 | 毫秒 | 毫秒 |
| 狀態 | state store（可用 RocksDB） | 一等公民、可非常大、有 savepoint | 本地 RocksDB + changelog topic |
| 部署 | 一個 Spark 叢集 | 一個 Flink 叢集 | 在你的應用程式裡 —— 不需要叢集 |
| 何時選 | 團隊已經在跑 Spark；批次 + 串流統一 | 低延遲、複雜的事件時間、大狀態 | Kafka 進、Kafka 出的微服務 |

<!-- fd3e310a82a9 -->
### 8-5) Backpressure、遲到資料與故障處理

- **Backpressure（背壓）** —— 慢的 sink 讓上游運算子跟著變慢，而不是一路緩衝到 OOM
  （Flink 透過網路緩衝區傳遞背壓；Spark 用 `maxOffsetsPerTrigger` 限制每批讀取量）。
  突發流量由 Kafka 以 lag 的形式吸收。
- **毒訊息（poison message）** —— 攔下反序列化與驗證錯誤，連同錯誤訊息送到
  **dead-letter queue**，讓管線繼續跑。
- **重新處理** —— 保留原始串流（長 retention 或封存到物件儲存），讓修好的 job 可以重播。
- **監控**：consumer lag、端到端延遲、checkpoint 時間與失敗、watermark 延遲、
  因遲到而被丟棄的紀錄數。

---

<!-- 3af33230f306 -->
## 9) SQL vs NoSQL ⭐⭐⭐⭐

<!-- 75b0ef9a0da3 -->
### 9-1) 比較

**SQL 資料庫給你固定的 schema、join 與 ACID 交易，設計上多半是單節點或緊密協調的；
NoSQL 資料庫拿其中一部分去換水平擴展、彈性 schema，以及圍繞單一存取模式設計的資料模型。**

| | SQL（關聯式） | NoSQL |
|---|---|---|
| 資料模型 | 表、列、關聯 | key-value、文件、寬欄、圖 |
| Schema | schema-on-write，強制執行 | 彈性 / schema-on-read |
| 查詢 | SQL、即席 join | 每種儲存各自的 API；join 通常在應用程式裡做 |
| 交易 | ACID、多列 | 常常只限單一項目 / 單一分區；有些提供更多 |
| 擴展 | 先垂直擴展；read replica，分片很難 | 依 partition key 水平擴展 |
| 一致性 | 強一致 | 可調，常是最終一致 |
| 例子 | PostgreSQL、MySQL、Oracle | Redis、DynamoDB、MongoDB、Cassandra、Neo4j |

**NewSQL**（Spanner、CockroachDB、TiDB）兼具 SQL、ACID 與水平擴展，
代價是跨區域寫入的延遲。

<!-- 490bcce4a99f -->
### 9-2) ACID vs BASE、CAP 與 PACELC

- **ACID** —— **A**tomicity 原子性（全有或全無）、**C**onsistency 一致性（約束成立）、
  **I**solation 隔離性（併發交易互不干擾）、**D**urability 持久性（已提交的撐得過當機）。
- **BASE** —— **B**asically **A**vailable 基本可用、**S**oft state 軟狀態、**E**ventually consistent 最終一致。
- **CAP** —— 發生網路**分區（P）**時，分散式儲存必須在**一致性（C）**（拒絕部分請求）和
  **可用性（A）**（照樣回應，可能是舊資料）之間二選一。分區一定會發生，所以真正的選擇是 CP 或 AP。
- **PACELC** —— 而**否則（E）**，沒有分區時，它在**延遲（L）**與**一致性（C）**之間取捨。
  DynamoDB / Cassandra：PA/EL。Spanner：PC/EC。

| 儲存 | CAP 傾向 | 備註 |
|---|---|---|
| Cassandra | AP | 每個查詢可調：讀寫都用 `QUORUM`（R + W > N）就得到強一致讀 |
| DynamoDB | AP | 預設是最終一致讀；`ConsistentRead=true` 可在主表或 LSI 上做強一致讀，GSI 永遠不行 |
| MongoDB、HBase | CP | 每個分片 / 區域只有一個 primary |
| Spanner | CP | TrueTime；外部一致性 |

<!-- e3d717aa8f8e -->
### 9-3) NoSQL 家族

| 家族 | 模型 | 擅長 | 例子 |
|---|---|---|---|
| **Key-value** | `key → blob` | 快取、session、計數器、O(1) 查找 | Redis、DynamoDB、Memcached |
| **文件（Document）** | `key → JSON 文件` | 巢狀、結構多變、整份讀取的實體 | MongoDB、Couchbase、Firestore |
| **寬欄（Wide-column）** | partition key → 排序好的欄位列 | 巨量寫入吞吐、時間序列 | Cassandra、HBase、Bigtable、ScyllaDB |
| **圖（Graph）** | 節點 + 邊 | 多跳的關係查詢 | Neo4j、Neptune |
| **搜尋（Search）** | 倒排索引 | 全文、分面搜尋、log | Elasticsearch、OpenSearch |
| **時間序列（Time-series）** | 依時間分區的指標 | 監控指標、IoT | InfluxDB、TimescaleDB、Prometheus |

<!-- e7cabf54f01f -->
### 9-4) NoSQL 建模：查詢優先

**關聯式 schema 從實體出發設計；NoSQL schema 從查詢出發設計 —— 每種存取模式一張表、
反正規化，並選好 partition key，讓一個查詢只打到一個分區。**

<!--CODE-->

- **Partition key** —— 分散負載；少數值特別大的 key 會造成**熱點分區**。
- **Clustering key** —— 分區內的排序，讓範圍掃描成為可能。
- 第二種查詢模式（「依使用者查訊息」）就建**第二張表**，同時寫入。
- DynamoDB 的 single-table design 遵循同樣的規則，用 `PK` / `SK` 和 GSI。

<!-- ba8b2e1edade -->
### 9-5) 怎麼選

| 需求 | 選擇 |
|---|---|
| 金流、庫存、跨多列的不變量 | SQL（PostgreSQL / MySQL） |
| 即席分析與 join | SQL 資料倉儲（BigQuery、Snowflake） |
| 極高寫入吞吐、查詢已知、多區域 | 寬欄（Cassandra）/ DynamoDB |
| 低延遲快取或計數器 | Redis |
| 彈性的巢狀文件 | MongoDB / Firestore |
| 關係走訪 | 圖資料庫 |
| 全球規模**且**要 SQL + ACID | Spanner / CockroachDB |

面試答案：**「除非某個需求排除它，否則先用 PostgreSQL」** —— 然後說出那個需求
（寫入量、延遲、schema 多變、全球分佈）以及能滿足它的儲存。大多數真實系統都是
**多語持久化（polyglot）**：一個 OLTP 資料庫、一個快取、一個搜尋索引、一個資料倉儲，
透過 Kafka 上的 CDC 串在一起。

---

<!-- 9c70498b939a -->
## 10) 考前檢查清單

每個主題一行 —— 如果每一行你都能說出口，那一節就穩了。

- **Spark** — driver/executor、窄依賴 vs 寬依賴、為什麼 shuffle 會結束一個 stage、broadcast vs sort-merge、怎麼修資料傾斜
- **Kafka** — partition = 順序 + 平行度、acks/ISR、consumer group、at-least-once vs exactly-once
- **Hadoop** — HDFS block + NameNode、MapReduce shuffle、YARN、每個工具被什麼取代
- **Modeling** — 先定粒度、星型 vs 雪花型、事實表類型、SCD2 搭配代理鍵
- **SQL** — 執行順序、ROW_NUMBER/RANK/LAG、每組 top-N、gaps and islands、NOT IN + NULL
- **Batch** — ELT + medallion、冪等的分區覆寫、CDC、編排、資料品質檢查
- **Schema** — 查詢 + 粒度、代理鍵、依日期分區、相容的演進、DECIMAL + UTC
- **Stream** — 事件時間 + watermark、視窗類型、checkpoint、端到端 exactly-once
- **SQL vs NoSQL** — ACID vs BASE、CAP/PACELC、各家族、查詢優先建模、「先用 Postgres」
