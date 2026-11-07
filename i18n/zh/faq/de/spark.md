<!-- 6e8fe00f5691 -->
# Apache Spark — 資料工程面試深入解析

> **範圍** — 資料工程面試會探的 Apache Spark 全部內容：執行模型、API 與最佳化器、shuffle、分區、join 與資料傾斜、快取與記憶體、PySpark 細節、寫入、Structured Streaming 入門、調校、除錯與常見故障 —— 以 Spark 3.5 為準，並標出 Spark 4.0 的差異。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) — 橫跨所有 DE 主題的總覽與考前檢查清單；[`./stream_pipeline.md`](./stream_pipeline.md) — 串流的深入內容；[`./batch_pipeline.md`](./batch_pipeline.md) — Spark job 在管線中的位置；[`./hadoop.md`](./hadoop.md) — HDFS、YARN 與 MapReduce；[`../spark/faq_spark_hadoop.md`](../spark/faq_spark_hadoop.md) — 題庫：可以直接說出口的簡短答案、排錯演練、管線設計題與讀書計畫。

每一題都先給你會說出口的那句話，再講機制，然後是面試官下一步會追問的取捨。
除非該節另有說明，設定名稱與預設值都以 Spark 3.5 為準。

---

<!-- ea426508cc32 -->
## 1) Spark 是什麼、用在哪裡 ⭐⭐⭐⭐

<!-- 9f500f6cc260 -->
### 1-1) 一段話的答案

**Spark 是分散式、以記憶體優先的運算引擎：你描述對資料集的運算，Spark 把它轉成由平行
task 組成的計畫，在叢集上執行，並從 lineage 重新計算遺失的部分。**

- **統一引擎** —— 批次（Spark SQL / DataFrame）、串流（Structured Streaming）、
  機器學習（MLlib）與圖運算共用同一個引擎與同一套 API。
- **不綁定儲存** —— 讀寫 HDFS、S3 / GCS / ADLS、JDBC、Kafka、Delta、Iceberg、
  Hudi、Parquet、ORC、Avro、JSON、CSV。
- **語言** —— Scala、Java、Python（PySpark）、SQL 與 R；Spark Connect（3.4+）新增一個
  輕量的 client，透過 gRPC 與遠端的 driver 溝通。

<!-- b5ac2d55b27f -->
### 1-2) Spark vs MapReduce

**Spark 把中間結果留在記憶體，並規劃整個由 stage 組成的 DAG；MapReduce 每個 job 之間
都寫回 HDFS，所以多步驟與反覆迭代的工作在它上面慢很多。**

| | MapReduce | Spark |
|---|---|---|
| 中間資料 | job 之間寫到 HDFS | stage 之間放在記憶體 / 本機磁碟 |
| 計畫 | 每個 job 一個 map + 一個 reduce | 由多個 stage 組成的 DAG，整體最佳化 |
| API | 低階的 map / reduce | DataFrame、SQL、RDD |
| 反覆迭代的 job（ML、圖） | 每次迭代都重讀輸入 | 快取一次，在記憶體中迭代 |
| 容錯 | 從 HDFS 輸入重跑失敗的 task | 從 lineage 重新計算 |
| 串流 | 沒有 | Structured Streaming |

Spark 的 shuffle 仍然經過本機磁碟，所以「in-memory」不代表「從不碰磁碟」。

<!-- 3a29f074b236 -->
### 1-3) Spark vs 其他選擇

| 引擎 | 何時選它 |
|---|---|
| **Spark** | 大型批次 ETL、SQL 與程式碼混用、ML 管線、批次與 micro-batch 串流共用一個引擎 |
| **Flink** | 低延遲、逐筆處理且狀態很大的串流 |
| **Trino / Presto** | 在 data lake 上做互動式 SQL，沒有長時間執行的 ETL |
| **雲端資料倉儲（BigQuery、Snowflake）** | 只用 SQL 轉換，不需要自己維運叢集 |
| **DuckDB / Polars / pandas** | 單機放得下的資料 —— 通常更快也簡單得多 |

誠實的面試說法：**「單機放得下，就不要用 Spark。」**

---

<!-- 012b962007b1 -->
## 2) 架構與執行模型 ⭐⭐⭐⭐⭐

<!-- f1e83b4dc05d -->
### 2-1) Driver、cluster manager、executor

**一個 driver 規劃 job，許多 executor 平行執行，cluster manager 負責分配機器。**

| 元件 | 角色 |
|---|---|
| **Driver** | 執行你的 `main()`，持有 `SparkSession`，建立 DAG、排程 task、收集結果 |
| **Cluster manager** | 分配資源 —— YARN、Kubernetes、Standalone（Mesos 已棄用） |
| **Executor** | worker 節點上的一個 JVM；以執行緒執行 task、快取資料、寫 shuffle 檔 |
| **Task** | 最小工作單位：一個 task 處理一個 stage 的**一個 partition** |

<!--CODE-->

- 同時可執行的 task 槽位 = executors × `spark.executor.cores`。一個 200 個 partition 的
  stage 跑在 50 個槽位上（例如 10 個 executor × 5 個 core），要分 4 波執行；實際吞吐量
  仍取決於工作負載與 I/O。
- Driver 是應用程式的單點故障：它掛了，job 就掛了（YARN 可以用
  `spark.yarn.maxAppAttempts` 重試整個應用程式）。

<!-- e08bc1f5bb30 -->
### 2-2) 部署模式與 `spark-submit`

**`client` 模式下 driver 跑在你提交的那台機器；`cluster` 模式下它跑在叢集內 —— 正式環境
的 job 用 `cluster`，這樣筆電斷線也殺不掉它們。**

<!--CODE-->

| 模式 | Driver 跑在 | 用於 |
|---|---|---|
| `client` | 提交的那台機器 | notebook、互動式 shell、除錯 |
| `cluster` | 叢集內的某個節點 | 排程的正式環境 job |
| `local[*]` | 一個 JVM，每個 core 一個執行緒 | 單元測試、小實驗 |

<!-- a36198746f66 -->
### 2-3) Job → stage → task，以及兩個排程器

**一個 action 產生一個 job；DAGScheduler 在 shuffle 邊界把 job 切成 stage；
TaskScheduler 為每個 stage 的每個 partition 送一個 task 給 executor。**

<!--CODE-->

- **DAGScheduler** —— 建立 stage、追蹤哪些 shuffle 輸出存在，shuffle 檔遺失時重新提交
  產生它的 stage。
- **TaskScheduler** —— 安排 task 的位置，偏好 **data locality**（`PROCESS_LOCAL` →
  `NODE_LOCAL` → `RACK_LOCAL` → `ANY`，每一級最多等 `spark.locality.wait`，預設 3 秒），
  並把失敗的 task 重試最多 `spark.task.maxFailures` 次（預設 4）。
- 彼此沒有依賴的 stage 可以同時執行。

<!-- 1bec63d6ab57 -->
### 2-4) 容錯：lineage、重試與 speculation

**Spark 不複製算好的資料；它記得每個 partition 是怎麼建出來的，遺失時就從上游重新計算。**

- **Lineage** —— 每個 RDD / DataFrame 都知道它的上游與套用的函式。失去一個 executor，
  它快取的 partition 會從 lineage 重算；失去它的 shuffle 檔，產生它們的 map stage 會重跑
  （除非外部 shuffle service 仍在提供這些檔案）。
- **Task 重試** —— 失敗的 task 會在另一個 executor 上重試；失敗超過
  `spark.task.maxFailures` 次，stage 與 job 就會失敗。
- **Speculation**（`spark.speculation=true`）—— 對異常慢的 task 啟動一個複本，取先完成的
  那個。它對壞掉的節點有幫助；對資料傾斜沒用，因為複本拿到的是同一個過大的 partition。
  它也代表 task 裡的副作用可能執行兩次。
- **Checkpointing**（`df.checkpoint()`，搭配 `spark.sparkContext.setCheckpointDir(...)`）
  把資料寫到可靠儲存並截斷 lineage —— 適合計畫很長的反覆迭代 job。

<!-- b8a847df4a4b -->
### 2-5) Dynamic allocation

**Dynamic allocation 在 task 排隊時增加 executor、閒置時釋放，讓一個 job 不必在整個執行
期間佔著固定數量的機器。**

- `spark.dynamicAllocation.enabled=true`，以 `minExecutors` / `maxExecutors` 為上下限。
- 釋放 executor 會丟掉它的 shuffle 檔，所以需要**外部 shuffle service**（YARN）或
  `spark.dynamicAllocation.shuffleTracking.enabled=true`（保留仍持有有效 shuffle 資料的
  executor，在 Kubernetes 上通常這樣做）。
- 取捨：叢集利用率更好，換來較慢的爬升速度，以及 stage 之間 executor 的反覆增減。

---

<!-- 0158a67c3f6c -->
## 3) API：RDD、DataFrame、Dataset ⭐⭐⭐⭐

<!-- ad9b051b0d21 -->
### 3-1) 比較

**用 DataFrame：它帶有 schema，所以 Catalyst 能最佳化它、Tungsten 能把它存得很緊湊；
RDD 是一堆不透明的物件，經過 Spark 看不進去的函式處理。**

| | RDD | DataFrame | Dataset |
|---|---|---|---|
| Schema | 沒有 | 有（具名、有型別的欄位所組成的列） | 有，型別化的物件（`Dataset[T]`） |
| 最佳化器 | 沒有 | Catalyst | Catalyst（lambda 對它不透明） |
| 記憶體格式 | JVM 物件 | Tungsten 二進位列 | Tungsten 二進位列 + encoder |
| 語言 | 全部 | 全部 | 只有 Scala / Java |
| 型別錯誤何時發現 | 執行期 | 執行期（欄位名稱） | 編譯期 |
| 何時用 | 需要底層控制、非結構化資料 | 幾乎所有情況 | 想要型別化物件的 Scala 程式碼 |

在 Python 與 SQL 裡只有 DataFrame；在 Scala 裡，`DataFrame` 字面上就是 `Dataset[Row]`。

<!-- 86b312e88ab3 -->
### 3-2) Transformation vs action，以及 lazy evaluation

**Transformation 是 lazy 的，只是加進計畫；action 才執行計畫 —— 這正是 Spark 能在做任何
工作之前最佳化整個計畫的原因。**

| | Transformation | Action |
|---|---|---|
| 例子 | `select`、`filter`、`withColumn`、`join`、`groupBy().agg`、`repartition` | `count`、`collect`、`show`、`take`、`write.save`、`foreach` |
| 回傳 | 新的 DataFrame | 回到 driver 的值，或寫出去的資料 |
| 會執行嗎？ | 不會 | 會 —— 提交一個或多個 job |

<!--CODE-->

常見的坑：對同一個沒快取的 DataFrame 呼叫兩個 action，它會從來源被計算**兩次**。

<!-- 53b485f87fe0 -->
### 3-3) 窄依賴 vs 寬依賴

**窄 transformation 的每個輸出 partition 只需要一個輸入 partition，留在同一個 stage；
寬 transformation 需要來自很多 partition 的資料，所以會強制 shuffle 並開始新的 stage。**

| 窄（不 shuffle） | 寬（shuffle） |
|---|---|
| `select`、`filter`、`withColumn`、`map` | `groupBy`、`distinct`、`orderBy` |
| `union` | `join`（除非 broadcast 或已經同分區） |
| `coalesce`（合併 partition） | `repartition` |
| broadcast hash join（大表那邊） | 帶 `partitionBy` 的 window function |

<!-- 0f0289debab4 -->
### 3-4) RDD 時代至今仍會被問的題目

**`reduceByKey` 在 shuffle 前先在 map 端合併；`groupByKey` 把每個值都送過網路再分組 ——
所以做彙總時該用 `reduceByKey`（或 DataFrame 的 `groupBy().agg`）。**

<!--CODE-->

- `map` vs `flatMap` —— 每個輸入一個輸出 vs 每個輸入零到多個輸出。
- `mapPartitions` —— 每個 partition 執行一次；每個 partition 開一條 DB 連線，而不是每列一條。
- **Broadcast variable** —— 唯讀的值，每個 executor 只送一次（例如一個查找用的 dict）。
- **Accumulator** —— 只寫的計數器，在 driver 上彙總。在 **action** 裡的更新保證只套用
  一次；在 **transformation** 裡的更新，當 task 或 stage 重跑時可能被套用不只一次，
  所以不要在那裡用它做精確計數。

---

<!-- f15b47a83388 -->
## 4) Catalyst、Tungsten 與 AQE ⭐⭐⭐⭐⭐

<!-- 3b4306a34152 -->
### 4-1) Catalyst：查詢怎麼變成計畫

**Catalyst 把你的 DataFrame 或 SQL 轉成邏輯計畫，用規則改寫它，挑一個實體計畫，
再為它生成程式碼。**

<!--CODE-->

- **Predicate pushdown** —— `filter` 被往下推到讀取端，讓 Parquet / ORC 依 min / max
  統計跳過 row group，JDBC 來源則把它推進 `WHERE`。
- **Column pruning** —— 欄式檔案只讀被引用到的欄位。
- **Partition pruning** —— 在分區欄位上的過濾會跳過整個目錄；
  **dynamic partition pruning**（3.0+）在執行期從 join 的另一邊做到這件事。
- **CBO**（`spark.sql.cbo.enabled`）用 `ANALYZE TABLE ... COMPUTE STATISTICS` 產生的表格
  統計來決定 join 順序；沒有統計時，Spark 從檔案大小估計。

<!-- 804318da50e3 -->
### 4-2) Tungsten：執行引擎

**Tungsten 用緊湊的二進位格式存放列、自己管理記憶體，並把多個運算子融合成生成的 Java
程式碼，讓一個 stage 跑起來像一個手寫的迴圈。**

- **UnsafeRow** —— 二進位列格式，脫離 JVM 物件模型：記憶體更少，幾乎沒有 GC 壓力。
- **Whole-stage code generation** —— `filter → project → aggregate` 變成一個生成的函式；
  在 `explain()` 輸出中，這些運算子會標上 `*(n)`。
- 為彙總與 join 設計的 **cache-aware 排序與雜湊**。
- 會讓它失效的：Python UDF、Dataset 上的 Scala lambda，以及非常寬的 schema
  （超過 `spark.sql.codegen.maxFields` 就不做 code generation）。

<!-- f2153adc1d6f -->
### 4-3) Adaptive Query Execution（AQE）

**AQE 在 stage 之間用 shuffle 輸出的真實大小重新最佳化計畫，而不是相信 job 開始前所做的
估計。Spark 3.2 起預設開啟。**

| AQE 功能 | 做什麼 | 關鍵設定 |
|---|---|---|
| **合併 shuffle partition** | 把 shuffle 後許多小 partition 合併成較少、達目標大小的 partition | `spark.sql.adaptive.coalescePartitions.enabled`、`advisoryPartitionSizeInBytes`（64 MB） |
| **切換 join 策略** | 某一邊實際上很小時，把 sort-merge join 改成 broadcast join | `spark.sql.adaptive.autoBroadcastJoinThreshold` |
| **Skew join** | 把過大的 partition 切成多個 task，並複製另一邊對應的 partition | `spark.sql.adaptive.skewJoin.enabled`、`skewedPartitionFactor`、`skewedPartitionThresholdInBytes` |

- AQE 只在 shuffle 邊界起作用 —— 沒有 exchange 的計畫它修不了。
- 預設情況下，一個 partition 要同時大於 `skewedPartitionFactor` × partition 大小中位數
  與 `skewedPartitionThresholdInBytes`（256 MB），才算傾斜。

<!-- 83b23d5b101d -->
### 4-4) 讀懂實體計畫

**`df.explain("formatted")` 顯示實體計畫：由下往上讀，找讀取端的過濾條件、每一個
`Exchange`（一次 shuffle），以及 join 運算子。**

<!--CODE-->

| 計畫節點 | 意義 |
|---|---|
| `FileScan parquet ... PushedFilters: [...]`、`PartitionFilters: [...]` | pushdown 與 partition pruning 有生效 |
| `Exchange hashpartitioning(key, 200)` | 依 `key` shuffle 成 200 個 partition |
| `BroadcastExchange` + `BroadcastHashJoin` | 小的那邊被 broadcast；大的那邊不 shuffle |
| `SortMergeJoin`，兩邊下面各有 `Sort` 與 `Exchange` | 兩邊都 shuffle 並排序 |
| `HashAggregate`（partial）→ `Exchange` → `HashAggregate`（final） | 帶 map 端合併的兩階段彙總 |
| `AdaptiveSparkPlan isFinalPlan=false` | AQE 會在執行期修改這個計畫 —— 到 SQL 分頁看最終版本 |
| `*(2)` 前綴 | 這個運算子在第 2 個 whole-stage codegen stage 內 |

`explain` 模式：`simple`、`extended`（四種計畫全部）、`codegen`、`cost`、`formatted`。

---

<!-- 29c396ef806a -->
## 5) Shuffle ⭐⭐⭐⭐⭐

<!-- cdbbdeebdfc3 -->
### 5-1) Shuffle 實際上做了什麼

**Shuffle 重新分配資料列，讓相同 key 的列都落在同一個 partition：每個 map task 把輸出依
目標 partition 排序寫到本機磁碟，每個 reduce task 再透過網路從每個 map task 抓取自己那一份。**

<!--CODE-->

- Spark 使用 **sort-based shuffle**：每個 map task 一個資料檔與一個索引檔。
- 成本：寫磁碟、序列化、網路傳輸、讀磁碟 —— 通常是 job 中最昂貴的部分，也是大多數
  spill 與 OOM 發生的地方。
- **外部 shuffle service** 在寫出 shuffle 檔的 executor 消失後，仍繼續提供這些檔案
  （在 YARN 上做 dynamic allocation 時需要）。

<!-- b6b912dbff45 -->
### 5-2) 怎麼避免或縮小 shuffle

- **儘早過濾與投影**，讓進到 exchange 的資料更少（Catalyst 會做很多，但中間擋了一個
  UDF 就做不了）。
- **Broadcast** join 中小的那一邊。
- **先彙總** —— `groupBy().agg` 本來就會在 map 端合併；避免先 `collect_list` 再彙總。
- 對經常以同一個 key join 的表做 **bucketing**（見 6-4 節）。
- **不要「以防萬一」就 repartition** —— 每一次 `repartition` 都是完整的 shuffle。
- 壓縮 shuffle 資料（`spark.shuffle.compress`，預設開啟），RDD 工作負載用 Kryo 序列化。

<!-- eb35365d8ab1 -->
### 5-3) `spark.sql.shuffle.partitions`

**它決定每次 DataFrame shuffle 產生多少 partition —— 預設 200，對很小和很大的 job 都不對。**

| 症狀 | 可能原因 | 解法 |
|---|---|---|
| 數千個 task 都在幾毫秒內結束 | 相對於資料量 partition 太多 | 調低，或讓 AQE 合併 |
| 少數幾個 task 往磁碟 spill 好幾 GB | partition 太少 | 調高，讓每個約 100–200 MB |
| 某一個 task 遠慢於其他 | 是傾斜，不是數量問題 | 見 7-3 節 |

經驗法則：shuffle 總大小 ÷ ~128 MB，且至少是 core 數的 2–3 倍。AQE 開啟時可以設得寬鬆
一點，讓合併機制把小 partition 併起來。

---

<!-- ec2505ec9840 -->
## 6) 分區與檔案佈局 ⭐⭐⭐⭐⭐

<!-- 0a76e6ad51fd -->
### 6-1) 記憶體中的 partition

**Partition 是一個 task 處理的那一塊資料；partition 的數量就是一個 stage 的平行度。**

- **讀取時**，檔案來源把輸入切成最大 `spark.sql.files.maxPartitionBytes`（預設 128 MB）
  的 partition；不可切分的檔案（gzip 壓縮的 CSV）不管多大都只有一個 partition。
- **Shuffle 之後**，數量是 `spark.sql.shuffle.partitions`（或 AQE 合併後的數量）。
- `df.rdd.getNumPartitions()` 告訴你目前的數量。

<!-- f46fe53271af -->
### 6-2) `repartition` vs `coalesce`

**`repartition(n)` 會做完整 shuffle，可增可減；`coalesce(n)` 只合併既有的 partition、
不做 shuffle，所以只能減少。**

| | `repartition(n)` / `repartition(n, col)` | `coalesce(n)` |
|---|---|---|
| Shuffle | 有 | 沒有 |
| 結果 | 大小平均的 partition（round-robin 或依 `col` 雜湊） | 可能不平均 |
| 用途 | 提高平行度；分區寫入前把同一個 key 聚在一起 | 在 job 最後減少輸出檔數 |
| 陷阱 | 要付一次完整 shuffle 的成本 | `coalesce(1)` 會把整個上游 stage 壓到一個 task 上 |

`repartitionByRange(n, col)` 透過抽樣做範圍分區 —— 適合在寫出排序好的輸出之前使用。

<!-- 9ddb2fd07d47 -->
### 6-3) 寫入時的 `partitionBy`，以及小檔案問題

**`df.write.partitionBy("dt")` 是讀取時給 pruning 用的目錄佈局，和記憶體中的 partition
不是同一回事 —— 而且每個記憶體 partition 都可能在它持有的每個目錄值下各寫一個檔案。**

<!--CODE-->

- 沒有那個 `repartition`，200 個 task × 30 個日期可能寫出 6,000 個小檔。
- 小檔案拖累每一個讀取者：每個檔案一個 task、讀檔尾、物件儲存列目錄很慢、在 HDFS 上
  佔 NameNode 記憶體。
- 解法：寫入前 `coalesce` / `repartition`、AQE 合併、壓實 job（Delta `OPTIMIZE`、
  Iceberg `rewrite_data_files`）。`maxRecordsPerFile` 只限制單一檔案的大小；它從來不會合併檔案。
- 用查詢會過濾的**低基數**欄位分區（日期）。用 `user_id` 分區會產生數百萬個目錄。

<!-- 3a032beede2f -->
### 6-4) Bucketing

**Bucketing 在寫入時依某個 key 把資料列雜湊到固定數量的檔案，讓之後以該 key 做的 join 或
彙總可以省掉 shuffle。**

<!--CODE-->

- 兩邊需要相同的 bucket 欄位與相容的 bucket 數，才能省掉 shuffle。
- Spark 的 bucketing 與 Hive 的 bucketing 佈局不相容。
- Lakehouse 格式提供其他選擇：Delta 的 Z-order / liquid clustering、Iceberg 的 sort order
  與 hidden partitioning。

---

<!-- 635c321207b8 -->
## 7) Join 與資料傾斜 ⭐⭐⭐⭐⭐

<!-- 5cc88a56f923 -->
### 7-1) Join 策略

**一邊夠小時 Spark 選 broadcast hash join，否則選 sort-merge join；其他都是特殊情況。**

| 策略 | 何時 | Shuffle | 備註 |
|---|---|---|---|
| **Broadcast hash join** | 一邊小於 `spark.sql.autoBroadcastJoinThreshold`（10 MB）或有 hint | 大的那邊不用 | 小的那邊收集到 driver，再送到每個 executor |
| **Shuffle hash join** | 一邊每個 partition 小很多，但大到不能 broadcast | 兩邊都要 | 每個 partition 建一個雜湊表；可能 OOM |
| **Sort-merge join** | 兩邊都大、等值 join —— 預設 | 兩邊都要 | 兩邊各自排序；能優雅地 spill |
| **Broadcast nested loop join** | 非等值 join 且有一邊很小 | 不用 | O(n × m) |
| **Cartesian product** | 非等值 join 且兩邊都大 | 兩邊都要 | 幾乎一定是 bug |

<!--CODE-->

<!--CODE-->

Broadcast 太大的東西，只是把 OOM 從 executor 搬到 driver —— 門檻值的存在是有原因的。

<!-- 67c4abc71fd0 -->
### 7-2) Join 類型與它們的陷阱

| Join | 回傳 | 陷阱 |
|---|---|---|
| `inner` | 匹配的列 | 兩邊都有重複 key 時列數會相乘 |
| `left` / `right` / `full` | 一邊 / 兩邊的所有列 | 之後在可為 null 的那邊過濾，會把它變回 inner join |
| `left_semi` | 有匹配的左表列（只有左表欄位） | 有效率的 `EXISTS` |
| `left_anti` | 沒有匹配的左表列 | 有效率且 `NULL` 安全的 `NOT EXISTS` |
| `cross` | 每一對組合 | 列數爆炸 |

- `NULL` key 在等值 join 中永遠不會匹配；如果必須匹配，用 `eqNullSafe`（SQL 中的 `<=>`）。
- 用欄位名稱清單做 join（`join(df2, "id")`），可以避免 `df1.id == df2.id` 產生的重複、
  有歧義的 `id` 欄位。

<!-- e18238da425d -->
### 7-3) 資料傾斜：發現與修正

**傾斜是少數幾個 key 握有大部分的列，於是一個 task 跑一小時，其他的一分鐘就結束 ——
整個 job 的時間由最慢的那個 task 決定。**

怎麼看出來：在 Spark UI 的 stage 頁面，最大的 task 時間與 shuffle read 量是中位數的好幾倍，
而且其他 task 都結束很久了，還有一個在跑。

怎麼修，依你嘗試的順序：

1. **AQE skew join** —— `spark.sql.adaptive.skewJoin.enabled=true`（AQE 開啟時的預設）
   會自動切開 sort-merge join 中過大的 partition。
2. **Broadcast** 小的那一邊，讓大的那一邊根本不用依 key shuffle。
3. **另外處理熱點 key** —— 它常常是 `NULL`、`-1`、`"unknown"` 或測試帳號：把它過濾掉、
   其餘正常 join，熱點 key 用 broadcast 處理，再 `union`。
4. **Salting（加鹽）** —— 在大表的 key 上加一個隨機後綴，小表則按每個後綴複製一份。
5. 傾斜的 `groupBy` 用**兩階段彙總** —— 先依 `(key, salt)` 彙總，再依 `key` 彙總。

<!--CODE-->

<!--CODE-->

Salting 只適用於可以拆開再合併的彙總（sum、count、max）—— 精確的 `countDistinct`
不額外處理就不行。

---

<!-- bcaa7216b30d -->
## 8) 快取與持久化 ⭐⭐⭐⭐

<!-- 79fde19aa0e5 -->
### 8-1) 何時快取

**只有當一個 DataFrame 被多個 action 重複使用、而且重算很昂貴時才快取；否則快取只花記憶體，
什麼也換不到。**

- 適合：被多個彙總使用的清理後資料集；反覆迭代的 ML 迴圈；在同一個 job 裡被 join 很多次的維度表。
- 不適合：只用一次的 DataFrame；會把其他東西都擠出去的巨大資料集；在單一次寫入前
  「為了加速」而快取。
- `cache()` 是 lazy 的 —— 資料在下一個 action 時才實體化；常用 `count()` 來強制觸發。
  用完一定要 `unpersist()`。

<!-- 5432867a13d2 -->
### 8-2) 儲存等級

| 等級 | 存成 | 不夠時 |
|---|---|---|
| `MEMORY_ONLY` | 記憶體中反序列化的物件 | 重新計算缺少的 partition |
| `MEMORY_AND_DISK` | 記憶體中，溢出到本機磁碟 | 磁碟 |
| `MEMORY_ONLY_SER` / `MEMORY_AND_DISK_SER`（Scala / Java） | 序列化的 bytes —— 較小、較耗 CPU | 重算 / 磁碟 |
| `DISK_ONLY` | 本機磁碟 | — |
| `OFF_HEAP` | off-heap 記憶體 | 需要 `spark.memory.offHeap.enabled` |
| `..._2` 變體 | 同上，但在兩個 executor 上各存一份 | 復原更快，記憶體用量加倍 |

- `RDD.cache()` = `MEMORY_ONLY`；`DataFrame.cache()` = `MEMORY_AND_DISK`（以 Tungsten 的
  記憶體內欄式格式存放）。
- 快取的資料住在 executor 上 —— 失去一個 executor，就從 lineage 重算它的 partition。

<!-- 027746c22342 -->
### 8-3) 快取 vs checkpoint vs 寫到儲存

| | `cache` / `persist` | `checkpoint` | 寫到一張表 |
|---|---|---|---|
| 保留 lineage | 是 | 截斷 | 截斷（變成新的來源） |
| 應用程式結束後還在 | 否 | 檔案還在，但只供這個應用程式使用 | 是 |
| 存在哪 | executor 記憶體 / 本機磁碟 | 可靠儲存（HDFS / S3） | lake / 資料倉儲 |
| 用於 | 同一個 job 內重複使用 | 反覆迭代 job 中非常長的 lineage | 把資料交給另一個 job |

---

<!-- de157d3a6d28 -->
## 9) 記憶體模型與資源配置 ⭐⭐⭐⭐

<!-- fa85414c4f81 -->
### 9-1) 統一記憶體管理

**Executor heap 分成保留區、user memory，以及一塊由 execution（shuffle、join、sort、彙總）
與 storage（快取、broadcast）共用的統一區域 —— execution 可以把快取區塊擠掉，storage
不能擠掉 execution。**

<!--CODE-->

- Execution 記憶體不夠時，運算子會 **spill** 到磁碟（慢但撐得住）；OOM 發生在某個東西無法
  spill 時 —— 一筆巨大的資料列、shuffle hash join 的 build side、Python worker，或 overhead 用完。
- 當 heap + overhead 超過容器限制時，容器會被 YARN / Kubernetes 殺掉 ——
  「Container killed for exceeding memory limits」指的是 **overhead**，不是 heap。

<!-- b1d8e519dc7b -->
### 9-2) Executor 的大小配置

**用中等大小的 executor —— 大約 4–5 個 core，記憶體足以容納每個 core 處理一個 partition
所需 —— 而不是每個節點一個巨大 executor，也不是每個 executor 一個 core。**

| 選擇 | 問題 |
|---|---|
| 非常大的 executor（全部 core、100+ GB heap） | GC 停頓很長、每個 JVM 的 HDFS client 吞吐量有上限 |
| 很小的 executor（1 個 core） | 無法共用 broadcast / 快取，每個 core 都要付 JVM 的額外成本 |
| ~4–5 個 core，記憶體 ≈ cores × （partition 工作集）+ overhead | 常見的起點 |

- 在每個節點上替 OS 與 NodeManager / kubelet 留空間。
- PySpark：Python worker 在 heap 之外，所以要調高 `spark.executor.memoryOverhead`，
  或明確設定 `spark.executor.pyspark.memory`。
- Driver 記憶體只跟回到它身上的東西有關：`collect`、`toPandas`、broadcast，以及非常大的
  計畫 / 檔案清單。

<!-- c57a96527068 -->
### 9-3) 序列化

**Spark 在 shuffle、快取與傳送 closure 時都要序列化資料；對 RDD 來說，Kryo 比 Java 序列化
更快更緊湊，而 DataFrame 已經使用 Tungsten 自己的 encoder。**

- 設定 `spark.serializer=org.apache.spark.serializer.KryoSerializer` 並註冊類別，效果最好
  （RDD / Scala 工作負載）。
- **Closure 也會被序列化**：引用了不可序列化物件（DB 連線、logger、外層類別）的 lambda
  會以 `Task not serializable` 失敗。改在 `mapPartitions` / `foreachPartition` 裡建立該物件。

---

<!-- 59c610b1ec0a -->
## 10) PySpark 細節 ⭐⭐⭐⭐

<!-- 09e7e8b286ed -->
### 10-1) PySpark 怎麼執行

**PySpark 是架在 JVM 引擎上的 Python API：DataFrame 操作在 JVM 中規劃與執行，只有
Python UDF 與 RDD lambda 真的會執行 Python 程式碼 —— 在每個 executor 上獨立的 Python
worker 行程裡。**

<!--CODE-->

所以一條只用內建函式寫成的管線，從 Python 呼叫也能以 JVM 的速度執行。

<!-- c948a19b053a -->
### 10-2) UDF vs pandas UDF vs 內建函式

**優先用內建函式；需要自訂邏輯時用向量化的 `pandas_udf`；一般的 Python UDF 最慢，因為它
一次只搬一列。**

| | 內建（`F.*`） | Python UDF（`@udf`） | Pandas UDF（`@pandas_udf`） |
|---|---|---|---|
| 在哪執行 | JVM，code generation | Python worker，逐列 | Python worker，Arrow 批次 |
| 序列化 | 沒有 | 每列 pickle | Arrow 欄式批次 |
| 最佳化器看得見嗎 | 完全看得見 | 看不見（黑盒子） | 看不見（黑盒子） |
| 典型用途 | 它能表達的一切 | 快速原型 | 大規模的自訂數值 / 字串邏輯 |

<!--CODE-->

- UDF 也會擋住穿過它的 predicate pushdown。
- 分組邏輯：`groupBy(...).applyInPandas(func, schema)` 把每一組當成一個 pandas DataFrame
  交給 `func` —— 很大的一組必須放得進單一 worker 的記憶體。
- Spark 3.5 也有 Arrow 最佳化的 Python UDF（`useArrow=True`），以批次方式傳輸。

<!-- 27a6b1fd1a50 -->
### 10-3) Arrow、`toPandas` 與 `collect`

- `spark.sql.execution.arrow.pyspark.enabled=true` 讓 `toPandas()` 與
  `createDataFrame(pandas_df)` 以欄式批次傳輸，而不是 pickle 過的列。
- `toPandas()` 與 `collect()` 會把**全部**資料拉到 driver —— 摘要沒問題，整張表就是 OOM。
  用 `limit()`、先彙總，或寫到儲存。
- `pyspark.pandas`（前身是 Koalas）提供類似 pandas 的 API，在 Spark 上執行。

---

<!-- fbd02800e709 -->
## 11) Spark SQL、寫入與表格格式 ⭐⭐⭐⭐

<!-- 230d901d4ccc -->
### 11-1) Spark SQL 與 catalog

**Spark SQL 與 DataFrame API 編譯成同樣的計畫；依可讀性選擇，不是依速度。**

<!--CODE-->

- Temp view 只存在一個 session；global temp view 在同一個應用程式的 `global_temp` 中跨
  session 存在；表格則存在 catalog（Hive Metastore、AWS Glue、Unity Catalog、Iceberg REST catalog）。
- Spark 4.0 預設開啟 ANSI SQL 模式（`spark.sql.ansi.enabled=true`）：無效的轉型與整數溢位
  會拋出錯誤，而不是回傳 `NULL` —— 常見的升級驚喜。

<!-- 04bf3b15598d -->
### 11-2) Save mode 與 dynamic partition overwrite

**對分區表做 `overwrite` 預設會取代整張表；設定 `partitionOverwriteMode=dynamic`，就只會
取代你寫入的資料中出現的那些分區 —— 這個設定讓每日 job 可以安全地重跑。**

| Save mode | 目標已存在時 |
|---|---|
| `errorifexists` / `error`（預設） | 失敗 |
| `append` | 新增資料列 —— 重跑會重複 |
| `overwrite` | 取代（整張表，或 dynamic 模式下只取代寫入的分區） |
| `ignore` | 什麼都不做 |

<!--CODE-->

- 對 Hive 格式的表，SQL 的等價寫法是 `INSERT OVERWRITE TABLE t PARTITION (dt = '2026-11-01') SELECT ...`。
- 單純的 Parquet 目錄沒有交易：失敗的 overwrite 可能留下寫到一半的分區，讀取者也可能在
  寫入途中看到它。表格格式解決了這個問題。

<!-- e93fecf0033b -->
### 11-3) 在 Spark 上用 Delta Lake / Iceberg / Hudi

**表格格式在 Parquet 檔案之上加一份交易 log，讓 Spark 在物件儲存上得到原子提交、`MERGE`、
time travel 與 schema 演進。**

<!--CODE-->

| 能力 | 單純 Parquet | Delta / Iceberg / Hudi |
|---|---|---|
| 原子的 overwrite / append | 沒有 | 有 |
| `UPDATE` / `DELETE` / `MERGE` | 自己手動改寫 | 有 |
| 多個寫入者同時寫 | 不安全 | 樂觀併發控制 |
| Time travel | 沒有 | `VERSION AS OF` / snapshot |
| 小檔案壓實 | 自己寫 job | `OPTIMIZE` / `rewrite_data_files` / Hudi compaction |

它們如何融入管線見 [`./batch_pipeline.md`](./batch_pipeline.md)，佈局與演進見
[`./schema_design.md`](./schema_design.md)。

<!-- e99c24931bcb -->
### 11-4) 把資料讀好

- 讀 JSON / CSV 時提供 **schema**，不要用 `inferSchema` —— 推斷要多掃一遍，還可能猜錯型別。
- 格式錯誤資料的 `mode`：`PERMISSIVE`（預設，把壞資料放進 `_corrupt_record`）、
  `DROPMALFORMED`、`FAILFAST`。
- JDBC 讀取是單執行緒的，除非你設定 `partitionColumn`、`lowerBound`、`upperBound` 與
  `numPartitions` —— 而那些上下界只決定切分的步距，不會過濾資料列。
- 讀 Parquet 時的 `mergeSchema` 會調和不同檔案的 schema，但有代價；不要當成預設。

---

<!-- 6f8c8d94d00e -->
## 12) Structured Streaming 簡介 ⭐⭐⭐

<!-- 76796e10350a -->
### 12-1) 模型

**Structured Streaming 把串流當成一張無界的表，增量地在上面執行你的 DataFrame 查詢，預設是
一連串的 micro-batch，進度記錄在 checkpoint 目錄中。**

<!--CODE-->

| 概念 | 選項 |
|---|---|
| 輸出模式 | `append`（只有最終的列）、`update`（變動的列）、`complete`（每批輸出整個結果） |
| Trigger | 預設（上一批結束就跑下一批）、`processingTime="1 minute"`、`availableNow=True`（處理現有的資料後停止 —— 3.3+） |
| 容錯 | offset 與狀態存在 `checkpointLocation`；搭配可重播的來源與冪等 / 交易式的 sink，結果就是 exactly-once |

- 不是每個 sink 都接受每種輸出模式 —— Delta sink 接受 `append` 與 `complete`；
  需要 update 語意時，用 `foreachBatch` 搭配 `MERGE`。
- 修改查詢中有狀態的邏輯，可能讓既有的 checkpoint 無法再使用。

事件時間、watermark、狀態，以及與 Flink、Kafka Streams 的引擎比較，見
[`./stream_pipeline.md`](./stream_pipeline.md)。

---

<!-- ec774e0814bb -->
## 13) 調校檢查清單 ⭐⭐⭐⭐⭐

<!-- fd74dee14a3f -->
### 13-1) 依檢查的順序

**先找出慢的 stage，再問它是讀太多、shuffle 太多，還是一個 task 做了所有工作 —— 最後才調設定。**

1. **少讀一點** —— 在過濾欄位上做 partition pruning、column pruning（不要 `SELECT *`）、
   pushdown 沒被 UDF 擋住、欄式格式。
2. **少 shuffle 一點** —— broadcast 小的維度表、拿掉不必要的 `repartition` / `distinct`、
   join 前先彙總。
3. **修正傾斜** —— AQE skew join、隔離熱點 key、加鹽。
4. **調整 partition 大小** —— 每個 ~100–200 MB，task 數足以讓每個 core 都有事做；開 AQE。
5. **熱路徑上避開 Python** —— 內建函式優於 UDF，`pandas_udf` 優於 `udf`。
6. **快取會被重複使用的東西**，用完 unpersist。
7. **修正檔案佈局** —— 壓實小檔案、合理的 `partitionBy`、對重複的 join 做 bucketing / clustering。
8. **最後才調資源** —— executor 的 core / 記憶體 / overhead、dynamic allocation。

<!-- 33fe08d27f8f -->
### 13-2) 值得記住名字的設定

| 設定 | 預設值（3.5） | 控制什麼 |
|---|---|---|
| `spark.sql.shuffle.partitions` | 200 | DataFrame shuffle 之後的 partition 數 |
| `spark.sql.adaptive.enabled` | true | AQE |
| `spark.sql.autoBroadcastJoinThreshold` | 10 MB | join 中可被 broadcast 那一邊的最大大小（`-1` 表示停用） |
| `spark.sql.files.maxPartitionBytes` | 128 MB | 讀檔時輸入切分的大小 |
| `spark.sql.sources.partitionOverwriteMode` | static | `dynamic` 只覆寫寫入的分區 |
| `spark.executor.memory` / `.cores` | 1g / 依 cluster manager 而定 | executor heap 與 task 槽位 |
| `spark.executor.memoryOverhead` | max(384 MB, 10%) | off-heap 的預留空間（Python、NIO、metaspace） |
| `spark.memory.fraction` | 0.6 | heap 中 execution + storage 統一區域的比例 |
| `spark.dynamicAllocation.enabled` | false | 依需求增減 executor |
| `spark.speculation` | false | 重新啟動慢的 task |
| `spark.serializer` | Java 序列化 | RDD 工作負載設成 Kryo |

---

<!-- edab5693142e -->
## 14) 用 Spark UI 除錯 ⭐⭐⭐⭐

<!-- e299b7ee031b -->
### 14-1) 該看哪裡

**Jobs 分頁 → 慢的 job；Stages 分頁 → 慢的 stage 與它的 task 時間分佈；SQL 分頁 → 帶真實
列數的實體計畫；Executors 分頁 → GC 時間、spill 與失敗。**

| 分頁 | 告訴你 |
|---|---|
| **Jobs** | 哪個 action 慢、它有幾個 stage |
| **Stages** | task 時間的 min / 中位數 / max、shuffle read / write、spill（記憶體與磁碟）、輸入大小 |
| **SQL / DataFrame** | 實際執行的計畫與每個運算子輸出的列數、broadcast 還是 sort-merge、AQE 的最終計畫 |
| **Storage** | 快取了什麼、多少在記憶體 vs 在磁碟 |
| **Executors** | GC 時間、失敗的 task、shuffle 總量、遺失的 executor |
| **Environment** | 實際生效的設定 |

Spark History Server 為已結束的應用程式顯示同樣的 UI。

<!-- 88e913fb9506 -->
### 14-2) 判讀症狀

| 你看到 | 通常代表 |
|---|---|
| 最大 task 時間 ≫ 中位數，某個 task 的 shuffle read ≫ 其他 | 傾斜 |
| 很多 task 都有大量「Spill (Disk)」 | partition 對 execution 記憶體來說太大 |
| GC 時間很高（> 約 task 時間的 10%） | 每個 executor JVM 的資料太多，或快取了反序列化的物件 |
| 某個 stage 只有一個 task | `coalesce(1)`、沒有 `partitionBy` 的 window、不可切分的檔案、全域排序結果 |
| 某個 stage 因「FetchFailed」重跑 | 失去 executor / shuffle 檔 —— 記憶體被殺、被搶占、壞節點 |
| 很多非常短的 task | partition 太多，或很多小檔案 |
| 第一個 task 之前有很長的空檔 | driver 端的工作：列檔案、推斷 schema、規劃巨大的計畫 |

---

<!-- 37b77dd675fa -->
## 15) 常見故障與解法 ⭐⭐⭐⭐⭐

<!-- 26708ecaf10f -->
### 15-1) 記憶體不足（OOM）

| 錯誤 | 在哪 | 常見原因 | 解法 |
|---|---|---|---|
| driver 上的 `java.lang.OutOfMemoryError` | driver | `collect()` / `toPandas()`、過大的 broadcast、涵蓋數百萬個檔案的計畫 | 改成寫出去；`limit`；調低 broadcast 門檻；壓實檔案 |
| executor 上的 `OutOfMemoryError` / heap space | executor | 傾斜的 partition、shuffle hash join 的 build side、巨大的一列（`collect_list`） | 修正傾斜；更多 shuffle partition；改用 sort-merge |
| 「Container killed by YARN for exceeding memory limits」 | 容器 | off-heap 用量：Python worker、NIO buffer | 調高 `spark.executor.memoryOverhead` |
| `ExecutorLostFailure` / `FetchFailedException` | shuffle | 持有 shuffle 檔的 executor 掛了 | 找出它為什麼掛（通常是上面其中之一） |

<!-- fa730e0ecd2d -->
### 15-2) 其他值得認得的錯誤

- **`Task not serializable`** —— closure 抓到了不可序列化的物件；改在 `mapPartitions` /
  `foreachPartition` 裡建立它，或把它做成 broadcast。
- **`AnalysisException: cannot resolve column`** —— 拼錯字、欄位在上游被丟掉，或 self-join
  後欄位有歧義（兩邊都加別名）。
- **Job 卡在 199/200 個 task** —— 最後那個 task 就是傾斜的那個。
- **Broadcast 逾時**（`spark.sql.broadcastTimeout`，300 秒）—— 「小」的那邊其實不小，或算得很慢；
  先把它實體化，或不要 broadcast 它。
- **重跑得到不同結果** —— 非確定性的程式碼（沒有 seed 的 `rand()`、
  `monotonically_increasing_id`、沒有排序的 `first()`）在故障後被重新計算。
- **磁碟空間不足** —— shuffle 與 spill 檔塞滿本機磁碟；給 executor 更多本機儲存，或少 shuffle 一點。

<!-- 2ee851207f08 -->
### 15-3) 批次也要你自己保證 exactly-once

**Spark 批次 job 會在 task、stage 與應用程式層級被重試，所以它的寫入必須是冪等的 ——
重跑必須是取代，而不是新增。**

- 寫入：用 dynamic partition overwrite 或以自然鍵做 `MERGE`，不要 `append`。
- Task 裡的副作用（HTTP 呼叫、寄信、在 `foreach` 裡寫外部 DB）在重試或 speculation 時可能
  執行不只一次 —— 讓它們冪等，或移出 job。
- Output committer：在物件儲存上，用 S3A committer 或表格格式，而不是依賴 rename 的提交，
  因為 rename 在 S3 上不是原子的。

---

<!-- 5836420b361f -->
## 16) 常見問題 ⭐⭐⭐⭐⭐

<!-- f25f752d31de -->
### 16-1) 快問快答

- **什麼會造成 stage 邊界？** 寬依賴 —— 一次 shuffle。
- **一個 stage 有幾個 task？** 該 stage 的每個 partition 一個。
- **`cache()` vs `persist()`？** `cache()` 就是用預設等級的 `persist()`
  （DataFrame 是 `MEMORY_AND_DISK`，RDD 是 `MEMORY_ONLY`）。
- **`repartition` vs `coalesce`？** 完整 shuffle、任意數量 vs 不 shuffle 的合併、只能變少。
- **`groupByKey` vs `reduceByKey`？** `reduceByKey` 先在 map 端合併；優先用它。
- **為什麼一個 stage 很快跑完之後 job 就變慢？** 通常是傾斜，或某次 shuffle 相對於它的
  partition 數太大。
- **AQE 做什麼？** 在 shuffle 邊界重新規劃：合併 partition、切換成 broadcast join、切開傾斜的 partition。
- **什麼是 predicate pushdown？** 把過濾條件移進讀取端，讓來源跳過資料。
- **Broadcast join 的上限？** `spark.sql.autoBroadcastJoinThreshold`，預設 10 MB；小的那邊
  必須放得進 driver 與 executor 的記憶體。
- **Spark 怎麼復原遺失的 partition？** 從 lineage 重新計算。
- **窄 vs 寬？** 窄：每個子 partition 只有一個父 partition。寬：很多個，所以要 shuffle。
- **為什麼避免 Python UDF？** 逐列序列化到 Python 行程，而且最佳化器看不進去。
- **`client` vs `cluster` 部署模式？** Driver 跑在哪：你的機器 vs 叢集內。
- **Spark 裡的 DAG 是什麼？** RDD / DataFrame 依賴關係的圖，由排程器切成 stage。
- **Accumulator 有什麼要注意的？** 在 transformation 裡的更新，重試時可能被算不只一次。
- **Static vs dynamic partition overwrite？** Static 取代整張表；dynamic 只取代寫入的資料中
  出現的分區。
- **什麼是 shuffle spill？** Execution 記憶體滿了，運算子把排序好的片段寫到本機磁碟，之後再合併。
- **Window function 沒有 `partitionBy`？** 所有資料列都搬到一個 partition —— 一個 task 做全部的事。
- **Partition 數怎麼選？** 總資料量 ÷ ~128 MB，至少是 core 數的 2–3 倍，然後讓 AQE 合併。
- **Spark on Kubernetes vs YARN？** 同一個引擎；K8s 把每個 executor 跑成一個 pod，預設沒有
  外部 shuffle service，所以 dynamic allocation 用 shuffle tracking。

<!-- 5a7dc01d2fb4 -->
### 16-2) 設計題：「處理每天 10 TB 的點擊流」

**先說出答案的骨架：只讀當天的分區、清理與去重、用 broadcast join 小維度表、彙總，再用可
重跑的 overwrite 寫出分區且壓實過的輸出。**

1. **輸入** —— 點擊資料以 Parquet 落在 `s3://lake/raw/clicks/dt=YYYY-MM-DD/`（或在第一步
   用明確的 schema 把 JSON 轉成 Parquet）。
2. **讀取** —— 以 `dt = run_date` 過濾，只選需要的欄位。
3. **清理** —— 解析、把格式錯誤的資料丟到隔離路徑、依 `event_id` 去重
   （當天內 `dropDuplicates(["event_id"])`）。
4. **補充資訊** —— broadcast join 小維度表（頁面、活動）；使用者表很大時用 sort-merge join。
5. **彙總** —— 用 `groupBy` 與以 `user_id` 分區的 window function 算 session 與每日指標。
6. **寫出** —— Delta / Iceberg（或 Parquet 搭配 dynamic partition overwrite），以 `dt` 分區，
   依 `dt`（加上一個 clustering key）`repartition` 來控制檔案大小。
7. **維運** —— 開 AQE、依 ~10 TB 設定 shuffle partition 數（約數千個）、發佈前做資料品質
   檢查、以邏輯日期編排以便回補。
