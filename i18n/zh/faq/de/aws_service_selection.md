<!-- 31e8999776bd -->
# AWS 資料服務：如何在它們之間做選擇

> **範圍** —— 資深資料工程面試中 AWS 的「服務選型」題：每個服務負責哪一層，以及如何在功能相近的服務之間選出一個並為它辯護 —— S3 vs EBS vs EFS、RDS vs DynamoDB vs Redshift、Kinesis Data Streams vs Firehose vs MSK、SQS vs SNS vs EventBridge vs Kinesis、Glue vs EMR vs Lambda、Athena vs Redshift、Step Functions vs MWAA vs EventBridge、Glue Data Catalog vs Lake Formation 以及決定資料存取權的各層、每一層要監控什麼與成本由什麼驅動、三個經典架構情境，以及回答「你為什麼選 X？」的結構。Glue、EMR、Iceberg、Athena、從 RDS 取資料、S3 + SQS + Lambda 資料攝取與 S3 檔案布局的深入內容歸 [aws_data.md](./aws_data.md)；Kafka 內部機制歸 [kafka.md](./kafka.md)；串流語意歸 [stream_pipeline.md](./stream_pipeline.md)。
> **另見**：[`./aws_data.md`](./aws_data.md) —— 這裡每個 Glue、EMR、Athena、Iceberg、RDS、SQS 與 Lambda 答案背後的機制；[`./kafka.md`](./kafka.md) —— MSK 跑的是什麼；[`./stream_pipeline.md`](./stream_pipeline.md) —— event time、watermark、exactly-once；[`./sql_vs_nosql.md`](./sql_vs_nosql.md) —— 與引擎無關的資料庫選型；[`../db/faq_redshift.md`](../db/faq_redshift.md) —— Redshift 筆記；[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單。

AWS 幾乎每件工作都有兩三個服務能做，而面試題從來不是「Kinesis 是什麼？」—— 而是
**「為什麼用 Kinesis，不用 Firehose 或 MSK？」** 所以這份文件依「決策」而不是依「服務」組織：
每一節是一組功能相近的服務、區分它們的表格、決定選哪一個的線索，以及大家對它們常說錯的話。
服務名稱最後再背；先學會每個服務所做的**取捨**。

---

<!-- bf991f0facee -->
## 0) 一句話回答 ⭐⭐⭐⭐⭐

**「S3 就是 data lake；其他一切都是在選擇：資料怎麼進來、怎麼轉換、怎麼查詢、怎麼排程執行。
我依需求來挑 —— 延遲、資料量、誰要消費它、團隊需要多少控制權、維運成本多少 —— 並說明
如果需求改變，我會改用什麼。」**

被問到 *「你為什麼選服務 X？」* 時，用六個步驟回答：

1. **需求（Requirement）** —— 限制住這個決定的資料量、延遲、吞吐量與 SLA。
2. **選擇（Choice）** —— X 做到了哪件需求所要的事。
3. **替代方案（Alternative）** —— 功能相近的服務，以及排除它的那個線索。
4. **取捨（Trade-offs）** —— 成本、維運、效能、可靠性。
5. **失敗處理（Failure handling）** —— 重試、重複資料、資料遺失、重播（replay）。
6. **監控（Monitoring）** —— 哪個指標能說明管線是健康的。

> 「對於標準的批次 ETL 工作負載，我會選 AWS Glue，因為它提供代管的 Spark 執行環境，並且與 S3
> 和 Glue Data Catalog 整合；和 EMR 相比，它省去了叢集管理。如果我需要掌控 Spark runtime、
> 相依套件或叢集設定 —— 或是在規模大到 Spot 價格有意義時 —— 我會把它搬到 EMR。我會用一天
> 具代表性的資料對兩者做 benchmark，比較執行時間、資源用量與總成本，而不是假設哪個比較快。」

---

<!-- 8040857d273a -->
## 1) 地圖：每個服務負責哪一層 ⭐⭐⭐⭐⭐

<!--CODE-->

下面每一節都是這張圖中「不只一個候選服務」的那一列。只有一個答案的那一層 ——
**S3 作為 data lake** —— 是最該先說的，也是 [aws_data.md §8](./aws_data.md#8-laying-out-data-in-s3-)
深入討論的部分。

---

<!-- 4532946aac4a -->
## 2) 儲存與資料庫 ⭐⭐⭐⭐⭐

<!-- c3acf2ce8ebb -->
### 2-1) S3 vs EBS vs EFS vs FSx ⭐⭐⭐⭐

**「S3 是給 data lake 用的物件儲存；EBS 是一台機器的磁碟；EFS 是共享檔案系統；FSx 是我因為
特定理由需要的特定檔案系統。」**

| 服務 | 類型 | 範圍 | 適合 | 不適合 |
|---|---|---|---|---|
| **S3** | 物件儲存，HTTP API | 區域級（regional），11 個 9 的耐久性 | data lake、原始檔案、Spark 的輸入與輸出、備份、資料交換 | POSIX 檔案系統、就地修改、資料庫 |
| **EBS** | 區塊儲存 | 單一 Availability Zone，掛載在一台 instance 上 | EC2 / EMR 節點磁碟、資料庫的 volume、shuffle 與 spill 空間 | 多台 instance 共享、長期的 lake 儲存 |
| **EFS** | NFS 檔案系統 | 區域級，可同時供多台 instance 使用 | EC2 / 容器 / Lambda 之間共享檔案 | 大量分析資料（S3 便宜得多） |
| **FSx** | 代管的 Lustre、Windows、NetApp ONTAP、OpenZFS | 每個檔案系統 | HPC / ML 暫存空間（FSx for Lustre 可連結 S3 bucket）、把既有檔案系統原樣搬上雲 | 純 S3 已經能做的任何事 |

S3 是什麼、不是什麼：

- **物件不可變（immutable）。** 你只能取代一個物件，不能編輯它 —— 這就是為什麼 Iceberg 這類表格格式
  會寫新檔案再切換指標（[aws_data.md §4-2](./aws_data.md#4-2-the-metadata-tree-and-the-commit-)）。
- 寫入後的讀取與列舉（list）都是**強一致**（自 2020 年 12 月起），所以「S3 是最終一致」這個
  老說法已經過時。
- **小檔案是經典的失敗模式。** 每個檔案都是一次要列舉、開啟、讀取的請求，所以一張由一百萬個
  10 KB 檔案組成的表，比同樣資料放在一百個大型 Parquet 檔案裡更貴也更慢；而且請求速率是以
  prefix 計（每個 prefix 每秒數千次讀寫），所以單一熱門 prefix 會被限流（[aws_data.md §8-2](./aws_data.md#8-2-cardinality-file-size-and-the-small-file-problem-)）。
- **儲存層級與 Lifecycle** 會把冷資料移到 Infrequent Access 與 Glacier 層；存取模式未知時，
  **Intelligent-Tiering** 會逐一物件自動處理。

<!-- a699be27394d -->
### 2-2) OLTP vs NoSQL vs OLAP ⭐⭐⭐⭐⭐

**先說出工作負載的類型；服務自然跟著決定。**

| 服務 | 類型 | 適合 | 限制 |
|---|---|---|---|
| **RDS** | 代管關聯式資料庫（PostgreSQL、MySQL…） | 交易、關聯式資料、管線的資料來源 | 單一寫入節點；不是為大量掃描而設計 |
| **Aurora** | 雲原生關聯式資料庫，儲存跨 AZ 共享 | 高可用 OLTP、大量 read replica、zero-ETL 到 Redshift | 成本；本質上仍是 OLTP |
| **DynamoDB** | key-value / document，serverless | 高併發、依已知 key 做毫秒級查詢 | 存取模式必須事先設計；沒有 JOIN；scan 很貴 |
| **Redshift** | MPP 欄式資料倉儲 | BI、大型 fact 對 dimension 的 JOIN、重複的重度分析 | 不是 OLTP 儲存；逐列寫入很慢 |
| **ElastiCache** | 記憶體內（Redis / Valkey、Memcached） | 快取、session、計數器、排行榜 | 記憶體成本；它是快取，不是資料的權威來源 |

<!-- 03cef39abc47 -->
### 2-3) RDS vs DynamoDB ⭐⭐⭐⭐⭐

| | RDS / Aurora | DynamoDB |
|---|---|---|
| 資料模型 | 資料表、外鍵、SQL | 以 partition key（+ sort key）定位的 item |
| Schema | 固定，需做 migration | 每個 item 各自不同；只有 key 是固定的 |
| 查詢 | SQL 能表達的任何東西，包括 JOIN | 依 key 做 get / query，加上次要索引；沒有 JOIN |
| 擴展 | 垂直擴展，加上 read replica | 水平擴展，代管，on-demand 或 provisioned |
| 交易 | 完整 ACID | 在有限數量的 item 之間支援 ACID 交易 |
| 變更串流 | binlog / WAL，由 DMS 或 Debezium 讀取 | DynamoDB Streams，或 Kinesis Data Streams for DynamoDB |
| 進入 data lake | 用 DMS 做 CDC、snapshot export | 匯出到 S3（需開啟 point-in-time recovery），或用串流 |

**設計題的答案是依存取模式切分資料**，而不是把所有東西都放進擴展性最好的那個儲存：

- 帳號與付款 —— **RDS / Aurora**，因為需要交易與 JOIN；
- 依 user id 查 session 與個人資料 —— **DynamoDB**，因為 key 已知且延遲很重要；
- 完整的事件歷史 —— **S3**，因為資料量大且只會附加；
- 跨上述全部的分析 —— **Athena** 或 **Redshift**。

> **陷阱。**「DynamoDB 擴展性好，所以全部放進去。」一個你沒有為它設計 key 或索引的查詢會變成
> 整表 scan，一個臨時問題會變成一個匯出作業。決定一切的是資料模型與存取模式，而不是擴展性。

---

<!-- c7037cee889a -->
## 3) 串流資料攝取：Kinesis Data Streams、Firehose 還是 MSK ⭐⭐⭐⭐⭐

<!-- dfdd45ded2a9 -->
### 3-1) 各自是什麼

- **Kinesis Data Streams（KDS）** —— 一個持久、有序的**串流**。Producer 帶著 partition key 寫入
  record，record 在每個 **shard** 內有序，consumer 依自己的步調讀取，並可在保留期限內**重新讀取**
  （預設 24 小時，可延長到 365 天）。
- **Amazon Data Firehose** —— 一個代管的**投遞（delivery）**服務。它緩衝收到的 record，再寫到目的地
  —— S3、Redshift、OpenSearch、Splunk、HTTP endpoint、Iceberg 表 —— 途中可選擇用 Lambda 轉換，
  或轉成 Parquet / ORC。你不用寫 consumer，也無法倒轉重讀。
- **Amazon MSK** —— 代管的 **Apache Kafka**（provisioned broker 或 MSK Serverless）。有 topic、
  partition、consumer group、Kafka Connect（MSK Connect），以及整個 Kafka client 生態系
  （[kafka.md](./kafka.md)）。

<!-- 47127e6567d9 -->
### 3-2) 比較 ⭐⭐⭐⭐⭐

| | Kinesis Data Streams | Data Firehose | MSK |
|---|---|---|---|
| 定位 | 串流平台 | 投遞到 sink | 代管 Kafka |
| 擴展單位 | shard：寫入 1 MB/s 或 1,000 records/s，讀出 2 MB/s | 無須管理 | broker 規格與數量、partition |
| 容量模式 | provisioned shard 或 on-demand | 自動 | provisioned 或 serverless |
| Consumer | 多個；每個 shard 共用 2 MB/s，或用 enhanced fan-out 各自 2 MB/s | 每個 stream 一個目的地 | 多個 consumer group |
| Replay | 可以，在保留期限內 | 不行 —— 送出就忘 | 可以，在 topic retention 內 |
| 順序 | 每個 shard 內（partition key） | 不是可依賴的保證 | 每個 partition 內 |
| 轉換 | 在你的 consumer 裡（Lambda、Flink、Spark） | 每批一次 Lambda、格式轉換、dynamic partitioning | 在你的 consumer、Kafka Streams、Flink 裡 |
| 維運 | 低到中（provisioned 模式要規劃 shard） | 最低 | 中到高（broker、partition、升級） |
| 最適場景 | 在 AWS 上做即時處理且有多個 consumer | 把 log 與事件落地到 S3 / Redshift / OpenSearch | 既有的 Kafka 環境或需要 Kafka 生態系 |

<!-- e0804602add2 -->
### 3-3) 怎麼選 ⭐⭐⭐⭐⭐

- **只是要把資料落地到 S3（或 Redshift、OpenSearch）？** → **Firehose。** 它會批次寫入、可以寫成
  Parquet、可依 record 裡的欄位分區，而且不需要寫任何程式。
- **多個 consumer、自己的處理邏輯、需要 replay？** → **Kinesis Data Streams**，由 Flink
  （Amazon Managed Service for Apache Flink）、Spark Structured Streaming 或 Lambda 讀取。如果你也想把
  原始串流存到 S3，就再加一個 Firehose 當作另一個 consumer。
- **已經在用 Kafka，或需要 Kafka Connect、Kafka 的語意，或能搬離 AWS 的可攜性？** → **MSK。**

延遲的問題：KDS 的 consumer 在遠低於一秒內就能看到 record（共享吞吐量的輪詢約 200 ms，
enhanced fan-out 更低）。Firehose 的延遲來自它的**緩衝**：當緩衝達到一定大小（1–128 MB）或
時間間隔（0–900 秒）時才寫出 —— 寫到 S3 的預設是 5 MB 或 300 秒 —— 所以 S3 物件是以秒到分鐘
為單位抵達，而不是毫秒。

**陷阱：**

- *「Firehose 是比較便宜的 Kafka。」* 它是一條投遞管線，不是事件平台：沒有 consumer group、沒有
  offset、沒有 replay。目的地失敗時它會重試，然後把失敗的 record 寫到 S3 的錯誤 prefix。
- *「Kinesis 和 Kafka 是同一種東西。」* 兩者都是有多個 consumer、可 replay 的分區 log，但擴展單位
  不同（shard 的吞吐量是固定的；Kafka partition 受限於它所在的 broker），保留期限、consumer API
  與生態系也不同。
- **熱點 shard（hot shard）。** 低基數的 partition key（國家、常數）會把大部分 record 放到同一個
  shard，它被限流（`WriteProvisionedThroughputExceeded`），其他 shard 卻閒著 —— 這是串流版的資料傾斜。
- **重複資料。** 三者都會因 producer 重試與 consumer 重啟而重複投遞；sink 必須是冪等的
  （[aws_data.md §7-5](./aws_data.md#7-5-duplicate-messages-and-idempotency-)）。

---

<!-- 6c53602c39d2 -->
## 4) 佇列與事件：SQS、SNS、EventBridge 還是 Kinesis ⭐⭐⭐⭐

<!-- c3c4d660f2bf -->
### 4-1) 四種形狀 ⭐⭐⭐⭐⭐

**佇列（queue）把每則訊息交給一個 worker；主題（topic）把它複製給每個訂閱者；事件匯流排（event bus）
依內容路由它；串流（stream）則依序保留它，讓任何人都能讀。**

| 需求 | 服務 | 原因 |
|---|---|---|
| 背景工作、緩衝、讓 producer 與慢速 consumer 解耦 | **SQS** | 每則訊息給一個 consumer、重試、DLQ |
| fan-out：一則訊息送給多個訂閱者 | **SNS**（常見做法是 SNS → 多個 SQS queue） | 推送到多個 endpoint |
| 在服務與 SaaS 之間依內容路由事件、排程 | **EventBridge** | rule 依欄位比對後送到 target |
| 大量有序事件、多個讀者、replay | **Kinesis Data Streams / MSK** | 有保留期限、分區的 log |

判斷線索：*「每個工作由 N 個 worker 之一處理一次」* 是 SQS；*「每個事件下個月新的 consumer 也能
重讀」* 是串流；*「S3 物件落地或 Glue job 失敗時，啟動 X」* 是 EventBridge。

<!-- fa1e17d21b5d -->
### 4-2) 面試官會追問的 SQS 細節 ⭐⭐⭐⭐

- **Standard queue** —— 吞吐量幾乎無上限、**至少一次（at-least-once）**、盡力而為的順序：
  會發生重複與亂序。
- **FIFO queue** —— **依 message group** 保證順序、5 分鐘內去重，吞吐量上限較低。不同 group
  之間不保證順序；一個卡住的 group 不會擋住其他 group。
- **Visibility timeout** —— consumer 收到訊息後，訊息會被隱藏這麼久；若沒有及時刪除，它會重新出現
  並再被處理一次。依處理時間來設定它（[aws_data.md §7-4](./aws_data.md#7-4-consuming-sqs-with-lambda-)）。
- **DLQ** —— 被接收超過 `maxReceiveCount` 次後，訊息會移到 dead-letter queue，讓一則壞訊息不再
  卡住其他訊息；修好後再 redrive 回去（[aws_data.md §7-6](./aws_data.md#7-6-dead-letter-queues-and-replay-)）。
- **保留期限** —— 最長 14 天；queue 是緩衝區，不是封存庫。

<!-- 9c27383dd5ae -->
### 4-3) 其他資料攝取服務 ⭐⭐⭐

| 服務 | 做什麼 | 典型用途 |
|---|---|---|
| **AWS DMS** | 從資料庫做 full load + CDC | RDS → S3、資料庫 → 資料庫的遷移（[aws_data.md §6-3](./aws_data.md#6-3-getting-data-out-of-rds-)） |
| **Amazon AppFlow** | 代管的 SaaS 連接器 | Salesforce、Zendesk、Google Analytics → S3 / Redshift |
| **AWS Transfer Family** | 代管的 SFTP / FTPS / FTP，寫入 S3 | 以檔案交付資料的合作夥伴 |
| **AWS DataSync** | 在儲存系統之間大量複製 | 地端 NFS / HDFS → S3 |
| **API Gateway** | 代管的 HTTP 入口 | 外部 client 送來的事件，通常再進 Kinesis、SQS 或 Lambda |
| **Snowball** | 實體傳輸裝置 | 網路無法及時搬完的 PB 級資料 |

---

<!-- f06929e0be7a -->
## 5) 資料處理：Glue、EMR 還是 Lambda ⭐⭐⭐⭐⭐

<!-- 6514046c34a2 -->
### 5-1) 三者一張表

**「Glue 用來在沒有叢集的情況下跑 Spark，EMR 用來掌控叢集，Lambda 用來回應事件 —— 而不是用來
處理資料本身。」**

| | Glue ETL | EMR（EC2、EKS 或 Serverless） | Lambda |
|---|---|---|---|
| 定位 | serverless Spark / Python ETL | 代管的大數據框架：Spark、Hive、Trino、Flink、HBase | 事件驅動的函式 |
| 控制權 | worker 類型與數量、Spark 設定 | 版本、instance 類型、設定、bootstrap action | 記憶體、timeout |
| 大量資料 | 可以 | 可以 | 不行：15 分鐘、10 GB 記憶體、沒有 shuffle |
| 維運 | 低 | EC2 上中到高；Serverless 低 | 最低 |
| 長時間或常駐的工作 | 每個 job 在其 timeout 內 | 可以 —— 長時間運行的叢集、串流 | 不行 |
| 計費 | DPU-hour，按秒計 | EC2 + EMR 加價（可用 Spot），或 vCPU / GB-秒 | 請求數 + GB-秒 |

包含 Athena 與 Redshift 的完整比較矩陣在 [aws_data.md §1-2](./aws_data.md#1-2-choosing-the-compute-)；
Glue 還是 EMR 的決策在 [§3-4](./aws_data.md#3-4-glue-or-emr-)；為什麼是 Lambda 啟動 Spark job、
而不是取代它，見 [§7-1](./aws_data.md#7-1-why-lambda-and-spark-not-one-of-them-)。

<!-- 5f0f13bea0a0 -->
### 5-2) 怎麼選 ⭐⭐⭐⭐⭐

| 情境 | 選擇 |
|---|---|
| 標準的每日 Spark ETL | Glue，或 EMR Serverless |
| TB 級 Spark job | 兩者皆可 —— 用一天具代表性的資料做 benchmark |
| 客製化的 Spark 版本、非 Spark 引擎、特殊函式庫 | EMR |
| 不想管理叢集，且需要整合 Data Catalog | Glue |
| 長時間運行的叢集、串流，或互動式 Trino / Hive | EC2 上的 EMR |
| 啟動時間 SLA 寬鬆的短 ETL | Glue Flex（使用閒置容量，較便宜，可能較晚開始） |
| 逐檔驗證、路由、啟動 job | Lambda |

**陷阱：把「Glue 比較慢」或「EMR 比較便宜」當成事實說出來。** 速度與成本取決於資料量、worker 或
instance 類型、job 的形態、啟動時間，以及 job 調校得多好。一般而言成立的是：EMR 提供更多旋鈕與
Spot 價格，在持續的大規模下*如果*有人調校並維運它就會勝出；Glue 每 DPU 的價格買到的就是省下這份
維運工作。說出你會量測什麼，而不是哪一個會贏。

<!-- 7217aeed347f -->
### 5-3) Spark 的追問 ⭐⭐⭐⭐

選了 Glue 或 EMR，話題就會轉到底下的引擎。要準備好：減少 shuffle、處理傾斜、設定 executor 大小、
避免小檔案、選擇 partition 數量、從 Spark UI 找出慢的 stage，以及讓失敗的 job 能安全重跑。
這些都歸 [spark.md](./spark.md) 與 [data_performance.md](./data_performance.md)；重跑歸
[batch_pipeline.md](./batch_pipeline.md)。

---

<!-- e0b0adb11f17 -->
## 6) 查詢：Athena 還是 Redshift ⭐⭐⭐⭐⭐

<!-- 109e6ed55bb1 -->
### 6-1) 比較

**「Athena 直接在資料所在的 lake 上查詢，依掃描量計費；Redshift 是我把資料載入進去的倉儲，所以
重複的重度查詢快且可預期。」**

| | Athena | Redshift |
|---|---|---|
| 儲存 | S3 上的檔案，透過 Glue Data Catalog | 代管的欄式儲存（RA3），加上透過 Spectrum 讀 S3 |
| 基礎設施 | serverless | provisioned 叢集或 Redshift Serverless |
| 計費 | 每 TB 掃描量（或 provisioned capacity） | node-hour，Serverless 則是 RPU-hour |
| 效能來自 | 檔案格式、分區、檔案大小、選取的欄位 | distribution、sort key、materialized view、workload management |
| 併發 | 每個帳號的查詢上限 | WLM 佇列、concurrency scaling |
| 最適場景 | 臨時問題、探索 lake、輕量的 CTAS 轉換 | dashboard、重複的大型 fact 對 dimension JOIN、穩定的 SLA |

**選 Athena**：資料已經是 S3 上的 Parquet、查詢是探索性或偶發的、沒人想維運倉儲。**選 Redshift**：
BI 工作負載整天跑同樣的重度 JOIN、需要在併發下維持可預期的延遲，或需要 workload management。
好的答案常常兩者並用：**Athena 查 lake，Redshift 放 dashboard 會打的 gold 表** —— 再用
Redshift Spectrum 把兩邊 JOIN 起來。

<!-- c1826047a2a0 -->
### 6-2) 各自怎麼調校 ⭐⭐⭐⭐

**Athena** —— 帳單與延遲的原因相同，都是掃描的位元組數
（[aws_data.md §5-2](./aws_data.md#5-2-the-cost-model-is-the-performance-model-)）：

- 欄式、壓縮的檔案（Parquet / ORC）；
- 依查詢會篩選的欄位分區，讓分區能被剪除（pruning）；
- 明確列出欄位 —— 不要 `SELECT *`；
- 合併小檔案；表格會被更新時用 Iceberg；
- 每個團隊一個 workgroup，並設定單次查詢的掃描上限。

**Redshift** —— 資料布局決定有多少資料要在節點之間搬動（[faq_redshift.md](../db/faq_redshift.md)）：

- **distribution style**：兩張大表的 JOIN 欄位用 `KEY`，讓相符的列落在同一個 slice；小型 dimension
  用 `ALL`；沒有好的 key 時用 `EVEN`；`AUTO` 讓 Redshift 自己選。傾斜的 `KEY` 會把工作堆到一個 slice 上；
- **sort key** 設在用範圍篩選的欄位（通常是日期），讓資料區塊能被跳過；
- **materialized view** 給 dashboard 反覆計算的彙總；
- **workload management** 佇列，讓分析師的臨時查詢不會餓死 dashboard；
- 用 `EXPLAIN` 與系統 view 找出重新分配或廣播資料的那一步。

<!-- 955d3c4fbc37 -->
### 6-3) 分析工具架上的其他服務 ⭐⭐⭐

| 服務 | 做什麼 | 適合 |
|---|---|---|
| **Redshift Spectrum** | 用 Redshift SQL 查 catalog 裡的 S3 表 | 把倉儲表和 lake 資料 JOIN 起來 |
| **OpenSearch** | 搜尋索引與 log 分析 | 全文檢索、log 探索、近即時 dashboard |
| **QuickSight** | BI 與 dashboard | 建立在 Athena / Redshift 上、給業務看的報表 |
| **EMR 上的 Trino** | 你自己維運的分散式 SQL | 非常大量的互動式 SQL、客製化 connector |

---

<!-- 18a04effc897 -->
## 7) 工作流編排：Step Functions、MWAA 還是 EventBridge ⭐⭐⭐⭐

<!-- 2dcd5fd1c0e9 -->
### 7-1) 比較

**「由 AWS 服務呼叫組成的工作流用 Step Functions；有排程、相依關係與回補（backfill）的資料管線用
MWAA；某件事發生時要啟動東西，用 EventBridge。」**

| | Step Functions | MWAA（代管 Airflow） | EventBridge |
|---|---|---|---|
| 模型 | state machine（JSON / 視覺化） | Python DAG | 事件 rule 與排程 |
| 擅長 | 呼叫 AWS 服務、分支、帶 backoff 的重試、parallel 與 map 步驟 | 大量任務之間的相依、排程、`catchup` 與回補、sensor、龐大的 operator 函式庫 | 回應事件、cron 式排程 |
| 重試 | 每個 state，宣告式 | 每個 task，在 operator 裡 | 每個 target，搭配 DLQ |
| 等待 job 完成 | 內建（Glue、EMR、Athena、ECS 的 `.sync` 整合） | operator 與 sensor | 不會 —— 只負責啟動 target |
| 維運 | 無，依狀態轉換次數付費 | 永遠開著的環境、Airflow 升級 | 無 |
| 適合 | 由 AWS 服務組成的 serverless 管線 | 已經在用 Airflow、有數百個 DAG 的團隊 | 放在兩者前面當觸發器 |

<!-- 432f6b77cde6 -->
### 7-2) 怎麼選 ⭐⭐⭐⭐

- **幾個 AWS 步驟，加上分支與重試**（啟動 Glue job、等待、跑 Athena、通知）→ **Step Functions。**
  Standard workflow 最長可跑一年，每個步驟恰好執行一次；Express workflow 用於短時間、大量的事件處理。
- **許多互相依賴的管線、排程、回補過去的日期、跨系統的任務** → **MWAA。** 回補一段日期範圍正是
  Airflow 的核心設計；代價是一個全天候運行、全天候計費的環境。
- **「檔案落地時執行」或「每天 02:00 執行」** → 用 **EventBridge**（rule 或 Scheduler）啟動
  state machine、Glue job 或 Lambda。它不追蹤多步驟的工作流。

Glue 自己的 **workflow 與 trigger** 也能串起 Glue job 與 crawler；每一步都是 Glue 時它們夠用，
只要有一步不是，就會處處受限。

---

<!-- 8e2b1ae60621 -->
## 8) Catalog、治理與存取權 ⭐⭐⭐⭐⭐

<!-- f4d2c9b16d4c -->
### 8-1) 相關服務

| 服務 | 負責 | 價值 |
|---|---|---|
| **Glue Data Catalog** | metadata：database、table、schema、partition | Athena、EMR、Glue 與 Redshift Spectrum 共用同一份表定義 |
| **Glue Crawler** | 從檔案推斷 schema | 探索用；正式環境請宣告 schema（[aws_data.md §2-3](./aws_data.md#2-3-crawlers-and-when-not-to-use-them-)） |
| **Lake Formation** | catalog 資源上的權限 | 對 database、table、欄、列與 cell 授權；以 tag 為基礎的存取（LF-tags） |
| **IAM** | 身分與 policy | 哪個使用者、role 或服務可以呼叫哪個 API |
| **KMS** | 加密金鑰 | 靜態加密，以及誰可以解密 |
| **Secrets Manager** | 憑證 | 把資料庫密碼與 API key 移出程式碼，並支援輪替 |

**一句話說明 Catalog vs Lake Formation：** Data Catalog 說明**資料是什麼**；Lake Formation 決定
**誰可以看到其中哪一部分** —— 對於與它整合的引擎（Athena、Redshift Spectrum、EMR、Glue）。

<!-- 3e5e2d557ea6 -->
### 8-2) 為什麼「我給了它 IAM 權限」還不夠 ⭐⭐⭐⭐⭐

從 lake 讀取資料，只有在它經過的**每一層**都允許時才會成功：

<!--CODE-->

任何一處的明確拒絕（explicit deny）都會勝出。最常見的除錯盲點是 **KMS**：role 可以列舉並
get 物件，讀取卻仍然以存取錯誤失敗，因為它沒有解密的權限。

<!-- c14b7fdd57d6 -->
### 8-3) 「你怎麼保護 S3 data lake？」 ⭐⭐⭐⭐⭐

1. 每個 job 與服務各自一個 **IAM role**，最小權限，範圍限定在它們的 prefix 與表格。
2. **Bucket policy** 只允許預期的 principal、要求 TLS，並可要求必須經由 VPC endpoint 存取。
3. 在帳號層級開啟 **Block Public Access**。
4. 敏感資料用 **SSE-KMS**，並在 key policy 中指名誰可以解密。
5. 用 **Lake Formation** 做表、欄、列層級的存取控制，規模大時用 LF-tags。
6. 用 **CloudTrail**（加上 S3 data events）稽核誰做了什麼。
7. PII 在進入廣泛開放存取的層之前先做**遮罩（masking）或 tokenization**。

**加密不等於存取控制。** 加密在儲存外洩時保護位元組；IAM、bucket policy 與 Lake Formation
決定誰可以讀取它們。面試官會聽你是否把這兩件事分清楚。

---

<!-- 1935f38d1f54 -->
## 9) 監控與可靠性 ⭐⭐⭐⭐

<!-- 639e99465e7b -->
### 9-1) 相關服務

| 服務 | 做什麼 | 資料工程的用途 |
|---|---|---|
| **CloudWatch** | 指標、log、告警、dashboard | job 執行時間、佇列訊息年齡、iterator age、SLA 告警 |
| **CloudTrail** | API 稽核 log | 誰修改了資源或讀取了資料集 |
| **X-Ray** | 分散式追蹤 | 追蹤一個請求穿過 Lambda 與 API Gateway |
| **SNS** | 通知 | job 失敗或告警觸發時發通知 |
| **SQS DLQ** | 失敗訊息的存放處 | 隔離毒訊息（poison message）以便檢查與 redrive |
| **AWS Config** | 資源設定的歷史 | 合規規則、偵測到 bucket 被設為公開 |

<!-- 4e24eedd75b8 -->
### 9-2) 每一層要看的指標 ⭐⭐⭐⭐⭐

| 層 | 監看 | 它告訴你什麼 |
|---|---|---|
| Kinesis | `GetRecords.IteratorAgeMilliseconds`、`WriteProvisionedThroughputExceeded` | consumer 跟不上；producer 被限流（熱點 shard） |
| Firehose | `DeliveryToS3.DataFreshness`、投遞成功率 | 尚未投遞的最舊 record 的年齡 |
| SQS | `ApproximateAgeOfOldestMessage`、`ApproximateNumberOfMessagesVisible`、DLQ 深度 | 積壓正在形成；訊息永久失敗 |
| Spark（Glue / EMR） | job 執行時間、stage 與 task 時間、shuffle、spill、GC | 傾斜、partition 太小、記憶體壓力 |
| S3 | 物件數量、大小、請求錯誤（4xx / 5xx、`503 Slow Down`） | 小檔案、被限流的 prefix |
| Redshift | 查詢時間、佇列等待時間、磁碟使用量 | WLM 飽和、distribution key 選得不好 |
| 管線整體 | 成功率、**資料新鮮度（freshness）**、列數是否符合預期 | SLA 是否真的達成 |

**依 SLA 設告警，而不只是依錯誤**：一條默默什麼都沒處理的管線不會丟出任何錯誤。新鮮度與資料量
檢查能抓到它。較完整的版本見 [aws_data.md §9-2](./aws_data.md#9-2-monitoring-pipeline-health-)。

---

<!-- 2aa2df8bba41 -->
## 10) 每一層的成本 ⭐⭐⭐⭐

| 層 | 帳單跟著什麼走 | 槓桿 |
|---|---|---|
| S3 | 儲存的 GB、請求數、傳輸量 | Lifecycle 移到較冷的層級、Intelligent-Tiering、壓縮、檔案更少更大 |
| Athena | 掃描的位元組數 | Parquet、分區剪除、不要 `SELECT *`、workgroup 掃描上限 |
| Glue | DPU-hour | 合理配置 worker、不急的 job 用 Flex、避免不必要的執行、增量載入 |
| EMR | instance-hour | task node 用 Spot、managed scaling、用短命叢集或 Serverless 取代閒置叢集 |
| Redshift | node-hour 或 RPU-hour | 合理配置容量、尖峰型使用就暫停或改用 Serverless、WLM、materialized view |
| Kinesis | shard-hour（provisioned）或資料量（on-demand） | 依真實尖峰規劃 shard；流量難以預測時用 on-demand |
| Lambda | 請求數 + GB-秒 | 每次呼叫批次處理多筆 record、合理設定記憶體、Graviton |
| 管線整體 | 重複計算 | 用增量處理取代整批重載 |

**「用 Spot instance」的資深版本。** 只說這句話不是答案；後續要回答的是：哪些工作可以被中斷、
checkpoint 存在哪裡、失去的 task 如何重試、被收回的容量會不會讓執行時間超過 SLA，以及省下的錢
值不值得這個風險。成本、可靠性與效能總是一起取捨，從來不是一次只考慮一個。更多槓桿見
[aws_data.md §9-3](./aws_data.md#9-3-watching-the-cost-)。

---

<!-- 2cc2a316b0c6 -->
## 11) 三個架構情境 ⭐⭐⭐⭐⭐

<!-- 8adfcec40e30 -->
### 11-1) 每日 1 TB 的批次處理 ⭐⭐⭐⭐⭐

<!--CODE-->

**為什麼：** S3 便宜地存放原始與處理後的資料，並保留原始資料以便重新處理；Spark 負責分散式
轉換；Parquet 讓查詢只讀需要的欄位；分區讓它只讀需要的日期；Redshift 給 dashboard 穩定的 SLA；
Athena 讓其他人都能直接查 lake。

**資深追問 —— 怎麼讓它更快、更安全？**

- 用**增量**處理（CDC、bookmark、watermark）取代每次全部重掃；
- 依查詢會篩選的欄位分區，不要過度分區；
- 合併小檔案；
- 調校 Spark job 的 shuffle、JOIN 與 partition 數量；
- 讓重跑是冪等的 —— 依 key 做 `MERGE`，或覆寫整個分區 —— 讓重試永遠不會產生重複列；
- 發布前做資料品質檢查、設定重試策略，並準備好回補的路徑。

完整的實作版本 —— 把 RDS 的訂單資料送進 Iceberg lakehouse —— 見
[aws_data.md §11](./aws_data.md#11-worked-design-rds-orders-to-an-s3-lakehouse-)。

<!-- c28a5725df58 -->
### 11-2) 即時應用程式 log ⭐⭐⭐⭐

<!--CODE-->

**為什麼選 KDS：** 多個 consumer 讀同一批 record、串流可在保留期限內 replay、處理是即時的。
**如果唯一的需求是「把 log 送進 S3」，單用 Firehose 就更簡單** —— 不需要 stream，也不需要 Spark。

**「你怎麼處理重複事件？」**

- 在來源端給每個事件一個穩定的 `event_id`；
- 讓處理是冪等的，必要時在有限的時間窗內依 `event_id` 去重；
- 對 consumer 的讀取位置做 checkpoint，讓重啟時是接續而不是從頭開始；
- 記住兩個 sink 之間沒有共同的分散式交易 —— 寫入 S3 與寫入 OpenSearch 可能一個成功一個失敗，
  所以每個 sink 都必須能承受 replay（[stream_pipeline.md](./stream_pipeline.md)）。

**「那遲到的資料呢？」** —— 串流 job 裡用帶 watermark 的 event-time window；批次層則用 lookback，
重新處理最近幾個分區。

<!-- 0b2af3d6d86e -->
### 11-3) 把 RDS 同步進 data lake ⭐⭐⭐⭐⭐

<!--CODE-->

**為什麼選 DMS：** 它先做初始的 full load，再從資料庫 log 串流變更，所以來源永遠不必被重新掃描。
以下追問在 [aws_data.md §6](./aws_data.md#6-amazon-rds-and-aurora-) 都有回答：

- **初始 snapshot** 以及 CDC 開始的時間點，讓中間不漏掉任何資料；
- **刪除** —— DMS 用 `Op = 'D'` 標記；`MERGE` 必須套用它們；
- **順序** —— 依來源的變更序號套用變更，而不是用可能相同的時間戳；
- **重複** —— 依主鍵做 merge，讓重複投遞的變更無害；
- **schema 演進** —— 來源新增欄位時不能讓 job 壞掉；
- **複寫延遲（replication lag）** —— 對它設告警；在 PostgreSQL 上，停住的 consumer 還會讓 WAL
  塞滿磁碟（[aws_data.md §6-4](./aws_data.md#6-4-cdc-prerequisites-and-the-replication-slot-trap-)）。

---

<!-- 493399db1ea7 -->
## 12) 面試工具箱 ⭐⭐⭐⭐⭐

<!-- 61d19b29ab7e -->
### 12-1) 服務選型表

| 需求 | 優先選擇 | 替代方案與時機 |
|---|---|---|
| data lake | S3 | — |
| 批次 ETL | Glue / EMR Serverless | 小型的逐檔工作用 Lambda |
| 大型或高度調校的 Spark | EMR | Glue |
| 即時事件串流 | Kinesis Data Streams | 已在用 Kafka 時用 MSK |
| log → S3 | Firehose | 同時也要處理 log 時，用 KDS + consumer |
| 資料庫 CDC | DMS | 在 MSK Connect 上跑 Debezium |
| 訊息佇列 | SQS | — |
| fan-out 給多個訂閱者 | SNS | 要依內容路由時用 EventBridge |
| 事件路由 / 排程 | EventBridge | — |
| 在 lake 上跑 serverless SQL | Athena | Redshift Spectrum |
| 資料倉儲 | Redshift | — |
| 工作流編排 | Step Functions / MWAA | 用 EventBridge 觸發 |
| metadata catalog | Glue Data Catalog | — |
| lake 治理 | Lake Formation | IAM + bucket policy，粒度較粗 |
| 低延遲 key 查詢 | DynamoDB | 前面加 ElastiCache |
| BI dashboard | QuickSight | — |
| 搜尋 / log 分析 | OpenSearch | — |
| 監控 | CloudWatch | — |
| 加密金鑰 / 機密 | KMS / Secrets Manager | — |

<!-- 4dd2d5039506 -->
### 12-2) 要演練的十五題

| # | 問題 | 要講的重點 |
|---|---|---|
| 1 | S3 還是 Redshift？ | lake vs 倉儲：便宜、開放的檔案 vs 快速、代管的查詢 —— 通常兩者並用（[§6-1](#6-1-the-comparison)） |
| 2 | Glue 還是 EMR？ | serverless vs 控制權；做 benchmark，不要直接斷言（[§5-2](#5-2-choosing-)） |
| 3 | Kinesis 還是 Firehose？ | 你自己處理的串流 vs 你設定好的投遞（[§3-3](#3-3-how-to-choose-)） |
| 4 | Kinesis 還是 MSK？ | AWS 原生的 shard vs Kafka 生態系（[§3-2](#3-2-the-comparison-)） |
| 5 | SQS 還是 Kinesis？ | 給一個 worker 的訊息 vs 給多個讀者、有保留期限的 log（[§4-1](#4-1-the-four-shapes-)） |
| 6 | Athena 還是 Redshift？ | 在 S3 上臨時查詢、依掃描量計費 vs 給重複重度查詢用的倉儲（[§6-1](#6-1-the-comparison)） |
| 7 | 設計 TB 級批次管線 | S3 分層、Spark、Parquet、分區、冪等重跑（[§11-1](#11-1-a-1-tb-daily-batch-)） |
| 8 | 設計即時管線 | KDS、串流處理器、checkpoint、兩個 sink（[§11-2](#11-2-real-time-application-logs-)） |
| 9 | 重複事件？ | 穩定的 id、冪等的 sink、去重時間窗（[aws_data.md §7-5](./aws_data.md#7-5-duplicate-messages-and-idempotency-)） |
| 10 | 遲到的資料？ | 串流用 watermark，批次用 lookback（[stream_pipeline.md](./stream_pipeline.md)） |
| 11 | 失敗復原？ | 帶 backoff 的重試、checkpoint、DLQ、冪等重跑 |
| 12 | 降低管線成本？ | 運算、儲存與掃描，各有各的槓桿（[§10](#10-cost-per-layer-)） |
| 13 | 監控 Spark job？ | 用 Spark UI 找慢的 stage，用 CloudWatch 看執行時間與新鮮度（[§9-2](#9-2-the-metric-per-layer-)） |
| 14 | 保護 lake？ | IAM、bucket policy、KMS、Lake Formation、稽核（[§8-3](#8-3-how-do-you-secure-an-s3-data-lake-)） |
| 15 | 增量 ETL？ | 進來用 CDC 或 watermark，出去用 `MERGE`（[batch_pipeline.md](./batch_pipeline.md)） |

<!-- 6099dde0ae73 -->
### 12-3) 大家常說錯的話

| 常聽到的說法 | 正確說法 |
|---|---|
| 「S3 是最終一致的」 | 自 2020 年 12 月起，讀取與列舉都是強一致 |
| 「Firehose 每 5 MB 或 15 分鐘寫一次」 | 緩衝大小 1–128 MB 與間隔 0–900 秒都可設定；寫到 S3 的預設是 5 MB 或 300 秒 |
| 「Firehose 可以取代 Kafka」 | 它是投遞到 sink；沒有 consumer group、offset 或 replay |
| 「Glue 一定比 EMR 貴 / 慢」 | 取決於資料量、資源配置與調校；兩者都量測看看 |
| 「DynamoDB 擴展性好，全部都用它」 | 只適用於已經設計進 key 的存取模式 |
| 「IAM 允許了，所以 job 讀得到」 | bucket policy、KMS、Lake Formation 與 SCP 都必須同意 |
| 「有加密，所以很安全」 | 加密保護位元組；存取控制決定誰能讀 |
| 「用 Spot 省錢」 | 只適用於可中斷、有 checkpoint、SLA 有餘裕的工作 |

<!-- 3e192d22debc -->
### 12-4) 考前最後複習清單

**儲存與資料庫**

- [ ] S3 vs EBS vs EFS
- [ ] S3 分區與小檔案問題
- [ ] RDS vs DynamoDB vs Redshift

**資料攝取**

- [ ] Kinesis Data Streams vs Firehose vs MSK
- [ ] SQS vs SNS vs EventBridge vs Kinesis
- [ ] DMS full load vs CDC

**資料處理**

- [ ] Glue vs EMR vs Lambda
- [ ] Spark 的 driver、executor、shuffle 與傾斜
- [ ] Parquet、壓縮與分區

**分析**

- [ ] Athena vs Redshift
- [ ] Redshift 的 distribution key 與 sort key
- [ ] `EXPLAIN` 與查詢調校

**架構與維運**

- [ ] Step Functions vs MWAA
- [ ] Glue Data Catalog vs Lake Formation
- [ ] IAM、KMS 與 Secrets Manager
- [ ] CloudWatch、重試、DLQ 與冪等性
- [ ] 每一層的成本

**複習順序：** 先掌握 S3、Glue、EMR、Spark、Kinesis、Redshift 與 Athena —— 它們撐起了 AWS
系統設計答案的大部分 —— 再深入 MWAA、Step Functions、DMS 與 Lake Formation。
