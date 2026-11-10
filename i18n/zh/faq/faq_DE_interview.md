<!-- 41eb5cfbbd7f -->
# 資料工程面試 FAQ

> **範圍** — 資料工程師面試的高層次地圖與考前檢查清單：每個主題各列出你必須能說出口的幾個重點，並連到 `de/` 底下的深入解析，那裡才有機制、程式碼與追問題。
> **另見**：[`de/spark.md`](./de/spark.md)、[`de/kafka.md`](./de/kafka.md)、[`de/hadoop.md`](./de/hadoop.md)、[`de/data_modeling.md`](./de/data_modeling.md)、[`de/sql_for_data.md`](./de/sql_for_data.md)、[`de/batch_pipeline.md`](./de/batch_pipeline.md)、[`de/schema_design.md`](./de/schema_design.md)、[`de/stream_pipeline.md`](./de/stream_pipeline.md)、[`de/flink.md`](./de/flink.md)、[`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md)、[`de/aws_data.md`](./de/aws_data.md)、[`de/data_performance.md`](./de/data_performance.md) 與 [`de/analytics.md`](./de/analytics.md) —— 每個主題一份深入解析。

面試前一晚讀這份。底下任何一行如果你沒辦法在兩分鐘內講清楚，就打開那個主題的深入解析。

---

<!-- 0a0a5028bec5 -->
## 1) 主題地圖 ⭐⭐⭐⭐⭐

| 主題 | 權重 | 面試官真正在探的 | 深入解析 |
|---|---|---|---|
| Apache Spark | ⭐⭐⭐⭐⭐ | 你知不知道時間花在哪裡 —— shuffle、傾斜、記憶體 | [`de/spark.md`](./de/spark.md) |
| Kafka | ⭐⭐⭐⭐⭐ | 順序、持久性與投遞保證，以及它們的代價 | [`de/kafka.md`](./de/kafka.md) |
| Hadoop 生態系 | ⭐⭐⭐ | 基礎，以及每個元件被什麼取代 | [`de/hadoop.md`](./de/hadoop.md) |
| 資料建模 | ⭐⭐⭐⭐⭐ | 粒度、星型模型與歷史（SCD） | [`de/data_modeling.md`](./de/data_modeling.md) |
| 資料用 SQL | ⭐⭐⭐⭐⭐ | 現場寫出 window function 與經典題型 | [`de/sql_for_data.md`](./de/sql_for_data.md) |
| 批次管線 | ⭐⭐⭐⭐ | 冪等性、回補、資料品質 | [`de/batch_pipeline.md`](./de/batch_pipeline.md) |
| Schema 設計 | ⭐⭐⭐⭐ | 從存取模式設計資料表，並安全地演進 | [`de/schema_design.md`](./de/schema_design.md) |
| 串流管線 | ⭐⭐⭐⭐ | 事件時間、watermark、狀態與 exactly-once | [`de/stream_pipeline.md`](./de/stream_pipeline.md) |
| Apache Flink | ⭐⭐⭐⭐ | 大多數串流答案底下的那個引擎 —— state backend、checkpoint、搭配 Kafka 的 exactly-once | [`de/flink.md`](./de/flink.md) |
| SQL vs NoSQL | ⭐⭐⭐⭐ | 選一個儲存並為取捨辯護 | [`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md) |
| AWS 資料服務 | ⭐⭐⭐⭐ | Glue vs EMR vs Athena、S3 上的 Iceberg、如何把資料從 RDS 取出、S3 → SQS → Lambda 資料攝取、CDK 與監控 | [`de/aws_data.md`](./de/aws_data.md) |
| 儲存與處理效能 | ⭐⭐⭐⭐⭐ | 少讀、少搬、避免 Spark / 資料庫 / Trino 的 OOM、排序與壓實資料、讀與寫的取捨、SQL 改寫 | [`de/data_performance.md`](./de/data_performance.md) |
| 分析 | ⭐⭐⭐⭐ | 精確的指標定義、不可加總的指標、A/B 測試的資料、找出指標變動的根因 | [`de/analytics.md`](./de/analytics.md) |

---

<!-- f7419bc070eb -->
## 2) Apache Spark ⭐⭐⭐⭐⭐

- **架構** —— 一個 driver 負責規劃與排程；executor 每個 partition 跑一個 task；cluster manager（YARN、Kubernetes）分配資源。
- **Lazy evaluation** —— transformation 只建立計畫；action 才把它當成一個 job 執行，並在每次 shuffle 處切成 stage。
- **窄依賴 vs 寬依賴** —— `map` / `filter` 留在同一個 stage；`groupBy` / `join` / `distinct` 會透過網路 shuffle 資料，而時間就花在 shuffle。
- **用 DataFrame 而不是 RDD** —— schema 讓 Catalyst 能最佳化計畫、Tungsten 能緊湊地存放資料列；Python UDF 會讓兩者都失效。
- **Join** —— 一邊很小時用 broadcast hash join，否則用 sort-merge join。
- **傾斜** —— 某個 task 跑得比其他久很多；用 AQE skew join、broadcast、把熱點 key 拆出來處理，或 salting 來修。
- **Partition** —— `repartition` 會 shuffle，`coalesce` 只合併；每個 partition 約 100–200 MB，避免寫出成千上萬個小檔案。
- **故障** —— driver OOM 通常是 `collect()`；executor OOM 通常是傾斜或 shuffle partition 太少；遺失的 partition 由 lineage 重新計算。

深入解析：[`de/spark.md`](./de/spark.md)。

---

<!-- 902429248bd7 -->
## 3) Apache Kafka ⭐⭐⭐⭐⭐

- **模型** —— 分區、有副本、只能附加的 log；consumer 自己追蹤 offset，資料保留到 retention 到期為止。
- **Partition** —— 平行度與順序的單位；只有 partition 內保證順序，所以用「順序重要的那個實體」當 key。
- **持久性** —— `acks=all` 搭配 `min.insync.replicas=2` 與 replication factor 3，撐得過單一 broker 故障；`acks=1` 在 leader 切換時可能遺失寫入。
- **Consumer group** —— 每個 group 中一個 partition 只給一個 consumer，所以 partition 數是平行度上限；rebalance 會重新分配 partition。
- **投遞** —— 處理後才 commit 是 at-least-once；exactly-once 需要冪等 producer、transaction 與 `read_committed`，Kafka 之外還要冪等或交易式的 sink。
- **Retention vs compaction** —— 依時間 / 大小刪除舊 segment，或只保留每個 key 的最新值當作 changelog。
- **Lag** —— 最新 offset 減去已提交 offset：第一個該設告警的指標。

深入解析：[`de/kafka.md`](./de/kafka.md)。

---

<!-- df74bfd6af0b -->
## 4) Hadoop 生態系 ⭐⭐⭐

- **HDFS** —— 大 block（預設 128 MB）、三份副本並具機架感知、所有中介資料都在 NameNode 記憶體裡 —— 這就是小檔案很傷的原因。
- **MapReduce** —— map → 依 key shuffle 與排序 → reduce，每個 job 之間都寫磁碟；Spark 透過在 DAG 中把資料留在記憶體而取代了它。
- **YARN** —— ResourceManager、NodeManager，以及每個應用程式一個 ApplicationMaster；讓 Spark、Hive 等共用同一個叢集。
- **Hive** —— 在檔案上跑 SQL，加上 Metastore；Metastore 比 Hive 的引擎活得更久，至今仍是許多引擎讀取的 catalog。
- **HBase** —— 跑在 HDFS 上、Bigtable 風格的寬欄式儲存；row key 的設計決定效能。
- **被什麼取代** —— 物件儲存取代 HDFS、Spark / Trino 取代 MapReduce / Hive、Kafka 取代 Flume、Airflow 取代 Oozie，表格格式（Iceberg、Delta、Hudi）為 data lake 帶來 ACID。

深入解析：[`de/hadoop.md`](./de/hadoop.md)。

---

<!-- cf6101c5ddef -->
## 5) 資料建模 ⭐⭐⭐⭐⭐

- **OLTP vs OLAP** —— 為了寫入正確而正規化，為了讀取快速而反正規化。
- **先定粒度** —— 在說出任何一個欄位之前，先講一筆事實列代表什麼。
- **星型模型** —— 一張由度量值與外鍵組成的事實表，周圍是反正規化的維度；雪花型則把維度再正規化。
- **事實表類型** —— 交易型、週期快照、累積快照、無事實。
- **可加性** —— 可加、半可加（餘額：不能跨時間加總）、不可加（比率：改存組成它的部分）。
- **SCD Type 2** —— 關閉舊列並插入帶有效日期的新版本；事實表存代理鍵，把每筆事實釘在當時為真的那個版本上。
- **做法** —— Kimball（一致化的星型）、Inmon（先建 3NF 資料倉儲）、Data Vault（hub、link、satellite）、one big table。

深入解析：[`de/data_modeling.md`](./de/data_modeling.md)。

---

<!-- 009c4a42f252 -->
## 6) 資料用 SQL ⭐⭐⭐⭐⭐

- **執行順序** —— `FROM` → `WHERE` → `GROUP BY` → `HAVING` → window → `SELECT` → `ORDER BY` → `LIMIT`；所以 window 的結果不能在 `WHERE` 裡過濾。
- **Window function** —— `ROW_NUMBER` / `RANK` / `DENSE_RANK`、`LAG` / `LEAD`、累計 `SUM`；要知道有 `ORDER BY` 時的預設 frame。
- **題型** —— 每組 top-N、去重、gaps and islands、累計總和、留存、漏斗、sessionisation、中位數。
- **NULL 陷阱** —— `NULL` 不等於任何東西；`NOT IN` 遇到 `NULL` 會回傳零列；在右表上寫 `WHERE` 會把 `LEFT JOIN` 變成 inner join。
- **Fan-out** —— join 到每個 key 有多列的表會重複計算；先彙總，檢查粒度。
- **資料倉儲成本** —— 掃描的 bytes：不要 `SELECT *`、在分區欄位上過濾、讓 clustering 跳過區塊。

深入解析：[`de/sql_for_data.md`](./de/sql_for_data.md)。

---

<!-- 41027b832d0a -->
## 7) 批次資料管線 ⭐⭐⭐⭐

- **ELT** —— 先落地原始資料，再在資料倉儲內轉換；保留原始層，有 bug 時重跑就能修好。
- **分層** —— bronze（原始）→ silver（清理、一致化）→ gold（業務 mart）。
- **冪等性** —— 每次執行覆寫其邏輯日期的 partition，所以重試與回補都安全；邏輯裡永遠不要用 `now()`。
- **增量載入** —— 時間戳 watermark 抓不到硬刪除；基於 log 的 CDC 抓得到。
- **編排** —— Airflow 負責依賴、排程、重試與回補，而不是轉換邏輯。
- **資料品質** —— 在每一層邊界做 schema、not-null、唯一性、參照完整性與新鮮度檢查。
- **格式** —— 分析用 Parquet，擷取用 Avro；Iceberg / Delta / Hudi 加上 ACID、`MERGE` 與 time travel。

深入解析：[`de/batch_pipeline.md`](./de/batch_pipeline.md)。

---

<!-- 29a462dafd7f -->
## 8) Schema 設計 ⭐⭐⭐⭐

- **方法** —— 需求 → 存取模式 → 粒度 → 鍵 → 實體配置 → 它會怎麼變。
- **鍵** —— 代理鍵當主鍵，保留自然鍵供查找；分散式系統用依時間排序的 id（UUIDv7、ULID）。
- **配置** —— 依幾乎每個查詢都會過濾的欄位分區（通常是日期），依次常過濾的欄位 cluster 或排序，依 join key 做 bucket。
- **演進** —— 新增可為 null 的欄位是安全的；改名、刪除與縮小型別會打壞讀取者；用 schema registry 強制相容性。
- **型別** —— 金額用 `DECIMAL`、時間戳用 UTC、鍵加 `NOT NULL`、每張表都有稽核欄位。

深入解析：[`de/schema_design.md`](./de/schema_design.md)。

---

<!-- d767c5e5eaaa -->
## 9) 串流資料管線 ⭐⭐⭐⭐

- **何時用串流** —— 只有在延遲值得一個永遠在線、有狀態的系統時；否則用批次。
- **Lambda vs Kappa** —— 兩套程式碼（批次 + 速度層）vs 一套串流程式碼、靠重播 log 來重新處理。
- **事件時間** —— 依事情發生的時間彙總；watermark 估計資料最晚會遲到多久，並決定視窗何時關閉。
- **視窗** —— tumbling、sliding、session；遲到資料可以丟掉、送到 side output，或用來更新結果。
- **端到端 exactly-once** —— 可重播的來源、和來源 offset 一起做 checkpoint 的狀態，以及交易式或冪等的 sink。
- **引擎** —— 低延遲與大狀態選 Flink，已經在用 Spark 的團隊選 Spark Structured Streaming，服務內嵌選 Kafka Streams。
- **維運** —— backpressure、dead-letter queue、consumer lag、checkpoint 健康度。

深入解析：[`de/stream_pipeline.md`](./de/stream_pipeline.md)；Flink 引擎本身 —— runtime、state backend、checkpoint 設定、Kafka sink —— 在 [`de/flink.md`](./de/flink.md)。

---

<!-- e7b26a44b995 -->
## 10) SQL vs NoSQL ⭐⭐⭐⭐

- **取捨** —— SQL 給你 schema、join 與 ACID；NoSQL 拿其中一部分換水平擴展，以及圍繞單一存取模式設計的模型。
- **ACID vs BASE** —— 強交易保證 vs 基本可用、最終一致。
- **CAP / PACELC** —— 發生分區時在一致性與可用性間選一個；沒有分區時，在延遲與一致性間選一個。
- **家族** —— key-value（Redis、DynamoDB）、文件（MongoDB）、寬欄（Cassandra）、圖（Neo4j）、搜尋（Elasticsearch）。
- **查詢優先建模** —— 在 NoSQL 裡，每種存取模式一張表，partition key 要能分散負載、避免熱點分區。
- **預設答案** —— 除非某個明確的需求排除它，否則先用 PostgreSQL，再說出能滿足那個需求的儲存。

深入解析：[`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md)。

---

<!-- d73492cc82ad -->
## 11) 回答架構題 ⭐⭐⭐⭐

大多數 DE 面試都有一題「設計一條管線」。同樣的六個步驟適用於每一題：

1. **釐清** —— 來源、資料量、延遲需求、使用者、正確性需求。
2. **批次或串流** —— 依延遲需求決定，並說明理由。
3. **畫出流程** —— 來源 → 擷取 → 儲存分層 → 處理 → 服務。
4. **建模** —— 粒度、事實表與維度表、分區方式。
5. **讓它可靠** —— 冪等性、回補、遲到資料、exactly-once 或 at-least-once 加去重。
6. **維運** —— 資料品質檢查、監控、告警、成本。

---

<!-- 847d46a3f7da -->
## 12) 考前檢查清單

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
- **AWS** — 一個 Glue catalog 給所有引擎、Glue vs EMR、Iceberg commit = 交換指標、Athena 按掃描位元組計費、從 RDS 做 CDC + replication slot 陷阱
- **Performance** — 先量測；剪枝 + 下推；先聚合再 join；不把無上限的東西集中到一個節點；排序 / Z-order + 壓實；`MERGE` 的來源先去重；每個索引都是寫入成本
- **Analytics** — 定義分子 / 分母 / 時間窗 / 篩選條件；比率要用加總後相除，不是比率的平均；distinct count 不能相加；SRM 檢查；先問「資料對不對？」再講任何故事
