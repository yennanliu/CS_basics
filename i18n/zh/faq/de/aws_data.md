<!-- fea729d77c44 -->
# AWS 資料服務：Glue、EMR、Iceberg、Athena、RDS、Lambda 與 SQS

> **範圍** —— 資料工程面試所考的 AWS 資料技術棧：S3、Glue Data Catalog、Glue ETL、EMR、Athena 與 RDS 如何拼在一起、如何在各種運算服務之間做選擇、Glue job 與 bookmark、EMR 叢集組成與 EMR Serverless、在 S3 上搭配 Glue catalog 使用 Apache Iceberg、Athena 的計費模型、分區與 Iceberg DML、如何把資料從 RDS 取出（JDBC、snapshot export、DMS CDC、zero-ETL）、用 S3、SQS 與 Lambda 做事件驅動的資料攝取（Lambda vs Spark、timeout、visibility timeout、重複訊息與冪等性、DLQ、消費者 concurrency）、S3 分區與檔案大小、AWS CDK、管線監控與成本、安全性，以及一題從 RDS 到 lakehouse 的完整設計題。Iceberg、Delta 與 Hudi 的引擎中立比較歸 [batch_pipeline.md](./batch_pipeline.md)；Spark 內部機制與調校歸 [spark.md](./spark.md)；Redshift 另有較舊的筆記。
> **另見**：[`./batch_pipeline.md`](./batch_pipeline.md) —— lake vs warehouse vs lakehouse、表格格式、冪等性與回補；[`./spark.md`](./spark.md) —— Glue 與 EMR 底下的引擎；[`./kafka.md`](./kafka.md) —— 用 Debezium 與 Kafka Connect 做 CDC；[`../db/faq_redshift.md`](../db/faq_redshift.md) —— Redshift 筆記；[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單。

大多數 AWS 資料平台都是同樣五塊：**S3 裡的檔案**、**說明哪些檔案構成一張表的 catalog**
（Glue Data Catalog）、**讓這些檔案具備交易性的表格格式**（Iceberg）、**讀寫它們的引擎**
（Glue、EMR、Athena），以及**資料來源的營運資料庫**（RDS / Aurora）。圍繞著它們的是事件驅動的黏合層 —— **S3 事件、SQS
與 Lambda** —— 以及部署並監看這一切的程式碼（**CDK、CloudWatch**）。面試很少要你背誦某個服務；
它要你挑一個，並為取捨辯護。

---

<!-- 228e3e12e5e0 -->
## 0) 一句話回答 ⭐⭐⭐⭐⭐

**「儲存用 S3，Glue Data Catalog 是共用的 metastore，表格用 Iceberg，這樣我在 S3 上就有
原子 commit、`MERGE` 與 time travel。來源資料用 DMS 的 CDC 從 RDS 取出（或用 zero-ETL 直接進
Redshift）。中小型的 serverless Spark job 用 Glue 做轉換；需要掌控版本、調校，或在大規模下
壓低成本時用 EMR —— 通常是 EMR Serverless。分析師用 Athena 查詢，它按掃描的位元組計費，
所以表格是 Parquet、依篩選欄位分區，並且定期壓實（compact）。」**

<!--CODE-->

---

<!-- d5bfd2d918ae -->
## 1) AWS 資料服務地圖 ⭐⭐⭐⭐⭐

<!-- 8a76a6f838cf -->
### 1-1) 每個服務負責什麼

**每個服務負責一層；讓它們能共用表格的是 Glue Data Catalog。**

| 層 | 服務 | 一句話角色 |
|---|---|---|
| 儲存 | **S3** | 耐久、便宜的物件儲存；lake 本身 |
| Metastore | **Glue Data Catalog** | 相容 Hive metastore 的表格定義，由 Athena、EMR、Glue、Redshift Spectrum 共用 |
| 表格格式 | **Apache Iceberg**（或 Delta / Hudi） | 在 S3 的 Parquet 之上提供 snapshot、原子 commit 與列層級變更 |
| Serverless ETL | **AWS Glue jobs** | 託管的 Spark（以及 Python shell）job，按 DPU-hour 計費 |
| 大數據叢集 | **Amazon EMR** | 在 EC2、EKS 或 Serverless 上跑 Spark、Hive、Trino、Flink、HBase |
| 互動式 SQL | **Amazon Athena** | 架在 catalog 上、以 Trino 為基礎的 serverless SQL，按掃描資料量計費 |
| 營運資料庫 | **Amazon RDS / Aurora** | 託管的 PostgreSQL、MySQL 等 —— 是資料來源，不是資料倉儲 |
| 遷移 / CDC | **AWS DMS** | 從 RDS 做全量載入 + change data capture，寫到 S3、Kinesis 或 MSK |
| 治理 | **Lake Formation** | 對 catalog 表格做表、欄、列與儲存格層級的權限控管 |
| 資料倉儲 | **Redshift** | MPP 欄式資料倉儲，適合繁重且重複的 BI 工作負載 |
| 訊息佇列 | **Amazon SQS** | 把生產者與消費者解耦的託管佇列；至少一次交付 |
| 事件運算 | **AWS Lambda** | 短時間、事件驅動的函式 —— 驗證、路由、觸發 —— 每次最多 15 分鐘 |
| 基礎設施即程式碼 | **AWS CDK** | 用 TypeScript / Python 合成 CloudFormation |
| 可觀測性 | **Amazon CloudWatch** | 上面所有服務的指標、日誌與告警 |

<!-- 41cc7ff59f33 -->
### 1-2) 選擇運算服務 ⭐⭐⭐⭐⭐

**問三個問題：是 SQL 還是程式碼、是臨時查詢還是排程作業、需要多少掌控權（以及資料有多大）？**

| | Glue job | EMR on EC2 | EMR Serverless | Athena | Redshift |
|---|---|---|---|---|---|
| 介面 | PySpark / Scala Spark、Python shell | 任何 Hadoop 生態系應用 | Spark、Hive | SQL | SQL |
| 需管理的基礎設施 | 無 | 叢集、節點、AMI | 無（一個 application） | 無 | 叢集或 serverless workgroup |
| 啟動時間 | 數十秒 | 新叢集要數分鐘 | 有預先初始化容量時數秒 | 無 | 無 |
| 調校掌控度 | 有限（worker 類型、數量、Spark conf） | 完整 | 大部分 Spark conf | 很少 | sort / dist key、WLM |
| 計費 | DPU-hour，按秒計 | EC2 + EMR 加價；可用 Spot | 按秒計 vCPU 與 GB | 掃描資料量，或預置容量 | 節點小時或 RPU-hour |
| 最適場景 | 中小型的排程 ETL、crawler、catalog 作業 | 大型、長時間或高度調校的 job；非 Spark 應用；搭配 Spot 在大規模下最便宜 | 負載起伏大、不想維運叢集的 Spark 批次 | 臨時探索、輕量轉換（CTAS） | 儀表板與重複的大型 join |

面試官喜歡的答案：**「探索用 Athena，管線用 Glue 或 EMR Serverless，只有需要掌控權或 Spot
省下的成本時才用 EMR on EC2，只有當 BI 工作負載重到、重複到值得養一座資料倉儲時才用 Redshift。」** Lambda 不在這張表裡，
因為它不是跑轉換的地方：它對事件做出反應，並啟動真正做事的 job
（[§7-1](#7-1-why-lambda-and-spark-not-one-of-them-)）。

---

<!-- 96bbb13e0ae2 -->
## 2) AWS Glue ⭐⭐⭐⭐⭐

<!-- 809e621e7d05 -->
### 2-1) Glue 是什麼

**Glue 是同一個名字下的三個產品：Data Catalog（metastore）、crawler（schema 推斷），以及
serverless Spark ETL job —— 外加圍繞它們的 Data Quality 規則與 workflow。**

| 組成 | 做什麼 |
|---|---|
| **Data Catalog** | database、表格、分區、schema；每個帳號每個 region 一份 |
| **Crawlers** | 掃描 S3 / JDBC，推斷 schema 與分區，寫進 catalog |
| **ETL jobs** | Spark（Glue 5.0 跑 Spark 3.5）、串流 Spark，或純 Python shell |
| **Job bookmarks** | 記住 job 已經處理過什麼，用於增量執行 |
| **Data Quality** | DQDL 規則（`IsComplete "id"`、`RowCount > 0`），在 job 中或針對表格評估 |
| **Workflows / triggers** | 簡單的編排；多數團隊改用 Step Functions 或 MWAA（Airflow） |

<!-- 567b2ae09618 -->
### 2-2) Glue Data Catalog ⭐⭐⭐⭐⭐

**Catalog 是託管的 Hive metastore：一張表就是一個名稱、一個 schema、一種格式和一個 S3 位置。
每個 AWS 引擎都讀同一份定義，這就是它成為整個技術棧中心的原因。**

- **Hive 式表格**：catalog 存 schema 以及**每一個分區**（`dt=2026-11-09` → 一個 S3 prefix）。
  規劃查詢時先從 catalog 列出分區，再從 S3 列出檔案。
- **Iceberg 表格**：catalog 只存一個**指標**（`metadata_location`），指向該表目前的 Iceberg
  metadata 檔。分區與檔案都在 Iceberg 自己的 metadata 裡，所以 catalog 不會隨之膨脹。
- **權限**：IAM 決定誰能呼叫 catalog API；**Lake Formation** 在其上加上表、欄、列與儲存格層級的
  授權，由 Athena、EMR、Glue 與 Redshift 強制執行。
- **共用**：每個 region 每個帳號一份 catalog；跨帳號存取透過 Lake Formation 或 resource policy。

<!-- d2eb21a4a498 -->
### 2-3) Crawler，以及何時不該用 ⭐⭐⭐⭐

**Crawler 從檔案推斷 schema；拿來探索很方便，用在正式管線上則有風險 —— 正式環境的 schema
應該是宣告出來的，而不是猜出來的。**

| 問題 | 怎麼咬到你 | 解法 |
|---|---|---|
| **Schema 漂移** | 一個壞檔案就讓某欄的型別翻轉或多出一欄，讀取端隨之壞掉 | 用 DDL / IaC 宣告表格；在 job 中驗證 |
| **表格爆炸** | 目錄結構不一致，crawler 為每個資料夾各建一張表 | 一致的 prefix；crawler 的 grouping 設定；或乾脆不用 crawler |
| **分區延遲** | 新分區要等 crawler 再跑一次才看得到 | 在 job 中註冊分區、用 partition projection，或用 Iceberg |
| **成本與時間** | 每次都重新掃描一大片 prefix | 增量爬取（S3 event 模式），或不爬 |

註冊新 Hive 分區的方式，越後面越省：

1. 跑 crawler。
2. `MSCK REPAIR TABLE t` —— 列出整個 prefix；大表很慢。
3. 由寫入的 job 執行 `ALTER TABLE t ADD PARTITION (dt='2026-11-09')`。
4. Athena 的 **partition projection** —— 完全不儲存分區（見 [§5-3](#5-3-partitions-repair-add-or-project-)）。
5. **Iceberg** —— commit 本身就記錄了新檔案；沒有東西需要註冊。

<!-- 62f6c349d6b4 -->
### 2-4) 一個 Glue ETL job ⭐⭐⭐⭐

**Glue job 就是外面包了一層 Glue context 的 Spark 應用程式：`GlueContext` 負責讀寫 catalog，
`DynamicFrame` 處理凌亂的 schema，`Job` 負責 bookmark。**

<!--CODE-->

這個 job 用 job 參數 `--datalake-formats iceberg` 加上 [§4-6](#4-6-wiring-spark-to-iceberg-and-the-glue-catalog)
的 catalog 設定來啟用 Iceberg，並用 `--job-bookmark-option job-bookmark-enable` 啟用 bookmark。

- **CDC 事件的排序**：依來源的變更序號排序（DMS 的 `AR_H_CHANGE_SEQ`、Debezium 的 LSN），
  絕不依 `updated_at` —— 兩個事件可能有相同的時間戳，而刪除事件可能根本沒有。把 `change_seq`
  存在目標列上，`MERGE` 就能忽略稍後才被重播的舊事件。硬刪除會把這個版本一起刪掉，所以如果
  已刪除 key 的舊更新仍可能在之後的批次出現，就改留 tombstone（軟刪除）。

- **`DynamicFrame` vs `DataFrame`**：`DynamicFrame` 能容忍同一欄有多種型別（*choice* 型別，
  用 `resolveChoice` 解決），並能接上 bookmark 與 catalog sink。凡是像 SQL 的處理就用 `toDF()`
  轉換；DataFrame API 更快、也更多人熟悉。
- **規模**：一個 **DPU** 是 4 vCPU 與 16 GB。Worker 類型有 `G.1X`（1 DPU）、`G.2X`（2 DPU）
  與更大的；`G.025X` 給小型串流 job。Auto Scaling 會釋放閒置的 worker。
- **Flex execution** 用閒置容量以較低費率跑不急的 job，代價是啟動時間較難預測。
- **Python shell jobs** 用一小部分 DPU 跑純 Python —— 適合呼叫 API 或處理小檔案，不適合
  任何需要 Spark 的工作。

<!-- ebab3cbffa18 -->
### 2-5) Job bookmark 與冪等性 ⭐⭐⭐⭐⭐

**Bookmark 記錄上一次成功執行處理了什麼 —— S3 物件依修改時間，JDBC 列依單調遞增的 key ——
讓下一次執行只讀新的輸入。**

- 它需要 job 參數 `--job-bookmark-option job-bookmark-enable`（預設是停用），再加上
  `job.init`、每個來源上的 `transformation_ctx`，以及最後的 `job.commit()`。
  少了這四個中的任何一個，job 都會悄悄退回全量重載。
- 它是 **at-least-once**：一次寫入後、在 `commit()` 之前失敗的執行，會重新處理同一批輸入。
  所以寫入必須冪等 —— 依 key 做 `MERGE`，或覆寫該次執行邏輯日期的分區 —— 絕不能盲目 append。
- 對 JDBC，預設以主鍵為 key（用 `jobBookmarkKeys` 指定其他欄位）；不單調的 key、或原地更新的列，
  都會被漏掉。這是偏好 CDC 的理由之一。
- **回補**：重設或暫停 bookmark，或另外跑一個以日期為參數的 job。
  Bookmark 回答的是「有什麼是新的？」，不是「重建整個十一月」。

<!-- 452c67e19191 -->
### 2-6) Glue 常見陷阱 ⭐⭐⭐⭐

| 症狀 | 常見原因 | 解法 |
|---|---|---|
| job 很慢，大部分 worker 閒著 | 一個超大或不可切分的檔案（`.gz` CSV） | 可切分的格式；讀入後 repartition |
| 輸出成千上萬個小檔案 | 分區數 × task 數太多 | 寫入前依分區欄位 repartition；壓實 Iceberg |
| driver 發生 `OutOfMemory` | `collect()`，或列出數百萬個 S3 物件 | 保持分散式處理；大量小輸入用 `groupFiles` / `groupSize` |
| JDBC 讀取在單一 task 上跑好幾小時 | 單一連線讀取 | `hashfield` / `hashpartitions`（或 Spark 的 `partitionColumn` / `numPartitions`），從 replica 讀 |
| 重跑造成資料重複 | append + bookmark，寫入卻不冪等 | `MERGE` 或分區覆寫 |

---

<!-- d2e0a0f3b22c -->
## 3) Amazon EMR ⭐⭐⭐⭐

<!-- 4849bea7e9ec -->
### 3-1) EMR 是什麼，以及三種部署選項

**EMR 是託管的開源大數據 —— Spark、Hive、Trino、Flink、HBase、Hudi、Iceberg —— 附上 AWS
調校過的版本與 S3 整合。跑在哪裡由你選。**

| 選項 | 你要管理的 | 適合 |
|---|---|---|
| **EMR on EC2** | 叢集：instance 類型、節點群組、擴縮、Spot | 長時間執行或高度調校的叢集、非 Spark 應用（HBase、Flink）、大規模下的最低成本 |
| **EMR on EKS** | 一個 EKS 叢集，EMR 把 Spark job 提交上去 | 已經統一用 Kubernetes 的團隊 |
| **EMR Serverless** | 一個 *application*（release + 類型）；容量由 AWS 管理 | 負載起伏大、沒有維運團隊的 Spark 或 Hive 批次 |

<!-- 522632977e69 -->
### 3-2) EC2 上的叢集組成 ⭐⭐⭐⭐⭐

**Primary 節點負責協調；core 節點跑 task *而且*存放 HDFS；task 節點只跑 task。
所以 Spot 應該放在 task 節點，資料應該放在 S3。**

| 節點 | 執行 | 失去一台時 |
|---|---|---|
| **Primary**（master） | YARN ResourceManager、HDFS NameNode、Spark history | 叢集失敗（除非是 multi-primary） |
| **Core** | YARN NodeManager **以及** HDFS DataNode | HDFS 區塊與執行中的 task 都會遺失 —— core 用 On-Demand |
| **Task** | 只有 YARN NodeManager | 只會重試它上面執行中的 task —— 最適合 **Spot** |

- **暫時性叢集**：每個 job 建一個、跑完 step、`--auto-terminate`。叢集結束後除了 S3 什麼都不留，
  而這正是重點。
- **長時間執行的叢集**：用於互動式工作（Trino、notebook）或 HBase；搭配 managed scaling 與
  閒置自動終止。
- **Instance fleets** 讓 EMR 從多種 instance 類型中挑選，並混用 On-Demand 與 Spot，
  比只用一種類型時 Spot 被中斷的機率低得多。
- **EMRFS** 是 EMR 讀寫 `s3://` 的方式；**EMRFS S3-optimised committer** 避開了在 S3 上
  又慢又不安全的 rename 式 commit。

<!-- 69cd006c972b -->
### 3-3) 提交工作 ⭐⭐⭐

<!--CODE-->

在正式環境中，這些呼叫來自編排工具 —— Airflow（MWAA）的 operator 或 Step Functions 的 EMR
整合 —— 重試與回補也由它負責。

<!-- 632bc3416b76 -->
### 3-4) Glue 還是 EMR？ ⭐⭐⭐⭐⭐

**當 job 是一般的 Spark、而你重視零維運時用 Glue；當你需要特定版本、非 Spark 引擎、深度調校，
或在大量且穩定的資料量上用 Spot 計價時用 EMR。**

| 問題 | 傾向 Glue | 傾向 EMR |
|---|---|---|
| 維運能力 | 沒有餘力 | 有平台團隊 |
| 引擎 | Spark / Python | Spark 加上 Hive、Trino、Flink、HBase |
| 版本掌控 | Glue 的 Spark 版本就夠了 | 需要特定的 Spark / Iceberg / 函式庫版本 |
| 調校 | 預設值就夠 | 自訂 executor、shuffle service、節點類型 |
| 資料量與成本 | 中小型、不規律 | 大量且穩定 —— Spot task 節點勝出 |
| 啟動 | 數秒到數十秒 | 有預先初始化容量的 Serverless，或保持暖機的叢集 |

<!-- ab5d9080bf03 -->
### 3-5) EMR 常見陷阱 ⭐⭐⭐⭐

- **Core 節點用 Spot** → HDFS 區塊隨節點消失；job 以令人困惑的方式失敗。Spot 只放 task 節點，
  輸出留在 S3。
- **暫時性叢集把資料放在 HDFS** → 叢集終止就沒了。HDFS 只是暫存空間。
- **閒置的長時間叢集** → EMR 帳單上最大的一項。設定閒置自動終止。
- **Executor 太大** → GC 停頓很長；太小 → overhead 太高。從 `maximizeResourceAllocation`
  或明確的每個 executor 4–5 core 起步，再依 Spark UI 調整。
- **S3 節流**（`503 Slow Down`），數千個 task 打同一個 prefix 時發生 → 把 key 分散開，
  寫更少、更大的檔案。

---

<!-- 50d2e9e53391 -->
## 4) AWS 上的 Apache Iceberg ⭐⭐⭐⭐⭐

<!-- fe8b0573f663 -->
### 4-1) 為什麼要在 S3 上用 Iceberg

**S3 上的 Hive 表格只是一個檔案目錄加上 catalog 中的分區清單 —— 沒有原子 commit、列出檔案很慢，
分區清單無止盡地成長。Iceberg 用一棵 metadata 檔案樹取代目錄，並以交換一個指標來 commit。**

| | S3 上的 Hive 式表格 | S3 上的 Iceberg 表格 |
|---|---|---|
| 哪些檔案構成這張表 | 當下 prefix 底下有什麼就是什麼 | 恰好是目前 snapshot 列出的那些檔案 |
| Commit | 檔案一個個出現；讀取端可能看到寫到一半的 job | 原子地交換 `metadata_location` |
| 查詢規劃 | 從 catalog 列出分區，再列出 S3 | 讀 manifest；用檔案統計資訊剪枝，不必列目錄 |
| 列層級變更 | 手動重寫整個分區 | `MERGE`、`UPDATE`、`DELETE` |
| Schema 變更 | 依位置或名稱；改名會讓舊檔案壞掉 | 以欄位 id 為準；可安全新增、刪除、改名、重排 |
| 分區 | 每個查詢都必須篩選的明確分區欄 | **隱藏式** transform；分區演進 |
| 歷史 | 無 | snapshot、time travel、rollback |

與 Delta Lake 和 Hudi 的引擎中立比較在
[batch_pipeline.md §7-3](./batch_pipeline.md#7-3-table-formats-iceberg-delta-lake-hudi)。

<!-- 52d979d3e778 -->
### 4-2) Metadata 樹與 commit ⭐⭐⭐⭐⭐

**每次寫入都產生新的資料檔、新的 manifest 和一個新的 metadata 檔；commit 就是把 catalog 的指標
從舊 metadata 檔 compare-and-swap 到新的那一個。**

<!--CODE-->

- **樂觀並行控制**：寫入者讀到版本 N，寫好自己的檔案，再請 catalog 把指標從 N 移到 N+1。
  如果別的寫入者搶先一步，commit 就失敗，寫入者針對新版本重試 —— 並重新驗證自己的變更仍然適用。
- **會咬人的衝突**：兩個 job 對同一批分區做 `MERGE`，或 compaction 重寫了串流寫入者正在刪除的檔案。
  把 compaction 排在重度寫入之外，或限定在冷分區。
- **讀取端**不鎖任何東西：一個查詢在整個執行期間都固定在同一個 snapshot。

<!-- 38cc6bde91c9 -->
### 4-3) 隱藏式分區與分區演進 ⭐⭐⭐⭐

**Iceberg 依欄位的 transform 分區，所以查詢篩選真正的欄位（`order_ts`）也能剪枝；之後改變分區方式
不必重寫舊資料。**

<!--CODE-->

<!-- 16029140f472 -->
### 4-4) Copy-on-write vs merge-on-read ⭐⭐⭐⭐

**Copy-on-write 會重寫變更碰到的每個資料檔 —— 寫入慢、讀取快。Merge-on-read 寫入小的 delete 檔，
在讀取時再合併 —— 寫入快，但在 compaction 之前讀取會變慢。**

| 工作負載 | 模式 |
|---|---|
| 夜間批次、以讀為主的儀表板 | copy-on-write（預設） |
| 頻繁的 CDC upsert、串流 `MERGE` | merge-on-read，加上排程 compaction |

列層級刪除需要 format version 2；Athena 能讀寫 Iceberg v2。

<!-- 7428d5729211 -->
### 4-5) 表格維護 ⭐⭐⭐⭐⭐

**從不維護的 Iceberg 表格每天都變得更慢、更貴：小檔案、堆積的 delete 檔、數千個 snapshot，
以及失敗寫入留下的孤兒檔案。**

| 工作 | Spark procedure | Athena | 為什麼 |
|---|---|---|---|
| 壓實小檔案、套用刪除 | `rewrite_data_files` | `OPTIMIZE t REWRITE DATA USING BIN_PACK` | 開檔次數變少，讀取更快 |
| 讓舊 snapshot 過期 | `expire_snapshots` | `VACUUM t`（搭配 `vacuum_max_snapshot_age_seconds`） | metadata 與儲存不再增長 |
| 刪除孤兒檔案 | `remove_orphan_files` | `VACUUM t` | 失敗寫入留下的檔案會永遠花錢 |
| 壓實 manifest | `rewrite_manifests` | — | 查詢規劃更快 |

<!--CODE-->

託管的替代方案：**Glue Data Catalog table optimizers** 會自動為 Iceberg 表格執行 compaction、
snapshot 保留與孤兒檔案刪除；**S3 Tables**（table bucket）儲存 Iceberg 表格，維護內建在 bucket 裡。
兩者都是用掌控權換取不必自己管排程。

**GDPR 刪除**光靠 `DELETE` 不夠。該列會變得看不見，但它的位元組還留在三個地方：如果刪除是
merge-on-read，它仍在*目前*的資料檔中（只是被 delete 檔遮住）；在可透過 time travel 讀到的舊
snapshot 中；以及表格以外的複本中。所以要：`DELETE`，接著**強制**重寫受影響的檔案，讓目前的 snapshot 不再含有該列 ——
預設值會跳過 delete 檔太少的資料檔，而這正是只刪一列的情況。在 Spark 中，對 `rewrite_data_files`
傳入 `options => map('delete-file-threshold', '1')`；在 Athena 中，`OPTIMIZE` 之前先把表格屬性
`optimize_rewrite_delete_file_threshold` 設為 `1`。繼續之前先**確認**受影響的分區中已沒有任何
delete 檔（`delete_files` metadata 表）。接著讓仍含有該列的 snapshot 過期，最後清除其他複本 ——
raw CDC 落地區，以及若 bucket 開了版本控制，S3 的非目前版本物件。

<!-- 3abb803c8237 -->
### 4-6) 把 Spark 接上 Iceberg 與 Glue catalog

<!--CODE-->

這些 extension 啟用 `MERGE` 與 `CALL` procedure；`GlueCatalog` 讓 Glue Data Catalog 成為存放
指標的地方，所以 Spark 寫的表就是 Athena 讀的表。

---

<!-- 068a06cc8bcb -->
## 5) Amazon Athena ⭐⭐⭐⭐⭐

<!-- 1007b6cb46e4 -->
### 5-1) Athena 是什麼

**Athena 是架在 S3 上的 serverless SQL：一個以 Trino 為基礎的引擎（engine version 3），從 Glue
Data Catalog 讀取表格定義。你為每個查詢掃描的位元組付費，所以每個效能手段同時也是成本手段。**

- **計價**：按掃描的 TB 計費（每個查詢最低 10 MB），或為穩定、可預測的負載使用**預置容量**
  （保留的 DPU）。
- **Workgroups** 區隔不同團隊：結果存放位置、加密、引擎版本，以及會取消失控查詢的
  **每查詢掃描資料量上限**。
- **格式**：Parquet、ORC、Avro、JSON、CSV；Iceberg 表格支援完整 DML；Hudi 與 Delta 支援讀取。
- **Federated query** 透過 Lambda connector 存取 RDS、DynamoDB 等 —— 拿來查個對照值可以，
  拿來大量匯出太慢。
- **Athena for Apache Spark** 在同一個服務上跑 notebook；與 SQL 引擎是分開的。

<!-- 83f2e56fc95a -->
### 5-2) 計費模型就是效能模型 ⭐⭐⭐⭐⭐

**減少掃描的位元組：欄式格式、壓縮、分區剪枝、更少的欄位，以及大小適中的檔案。**

| 手段 | 為什麼能減少掃描 |
|---|---|
| 用 **Parquet / ORC** 取代 CSV / JSON | 只讀選取的欄位；min/max 統計資訊跳過 row group |
| **壓縮**（Snappy、ZSTD） | 磁碟上的位元組少了，計費的位元組就少了 |
| **依篩選欄位分區** | `WHERE dt = ...` 只讀一個 prefix，而不是整張表 |
| **只選需要的欄位** | 在 Parquet 上 `SELECT *` 會對每一欄計費 |
| **檔案約 128 MB–1 GB** | 數千個小檔案會產生每檔 overhead 與 S3 請求 |
| **依下一個篩選欄位排序 / 叢集** | row group 統計資訊就能跳過每個檔案的大部分 |
| **用 CTAS 轉換** | 一句 `CREATE TABLE AS ... WITH (format = 'PARQUET')` 就把原始 JSON 變成便宜的表 |

<!--CODE-->

<!-- dba0b6a94969 -->
### 5-3) 分區：repair、add 或 project ⭐⭐⭐⭐

**對 Hive 式表格，partition projection 依規則算出分區而不是儲存它們，所以沒有東西需要註冊，
也沒有東西會忘記註冊。**

<!--CODE-->

| 方法 | 成本 | 陷阱 |
|---|---|---|
| `MSCK REPAIR TABLE` | 列出整個 prefix | 大表很慢、可能逾時；只找得到 `key=value` 形式的資料夾 |
| `ALTER TABLE ADD PARTITION` | 一次 catalog 呼叫 | 寫入端必須記得執行 |
| **partition projection** | 無 | **沒有**對 `dt` 篩選的查詢會列舉整個範圍 |
| **Iceberg** | 無 | —— 分區存在表格 metadata 中 |

<!-- 220fd1ed518b -->
### 5-4) Athena 中的 Iceberg ⭐⭐⭐⭐⭐

**Athena 能建立、讀取並寫入 Iceberg 表格，支援 `MERGE`、`UPDATE`、`DELETE`、time travel 與
維護指令 —— 光用 SQL 就足以經營一個小型 lakehouse。**

<!--CODE-->

<!-- 8ced810136f0 -->
### 5-5) Athena、Redshift 還是 EMR Trino？ ⭐⭐⭐⭐

| 需求 | 選擇 | 為什麼 |
|---|---|---|
| 對 lake 資料的臨時提問 | **Athena** | 不需要基礎設施；按查詢付費 |
| 儀表板整天打同樣的 join | **Redshift**（或物化的 gold 表） | 快取、sort key、concurrency scaling；按掃描計費會累積起來 |
| 需要自訂 connector 或調校的重度互動 SQL | **EMR 上的 Trino** | 完全掌控叢集與外掛 |
| 資料倉儲與 lake 資料做 join | **Redshift Spectrum** | 透過同一個 catalog 從資料倉儲查詢 S3 表格 |

<!-- 2cc49985acb7 -->
### 5-6) Athena 常見陷阱 ⭐⭐⭐⭐

| 錯誤或症狀 | 原因 | 解法 |
|---|---|---|
| 一個「小」查詢讓帳單暴增 | `SELECT *`、沒有分區篩選，或是 CSV | 選欄位、加篩選、改 Parquet；設 workgroup 掃描上限 |
| `HIVE_PARTITION_SCHEMA_MISMATCH` | 某個分區的 schema 與表格不同 | 修正或重建該分區；宣告 schema，不要靠爬取 |
| 在大量小檔案上查詢很慢 | 串流或頻繁的小量寫入 | compaction（`OPTIMIZE`、Spark `rewrite_data_files`） |
| 查詢在 30 分鐘後失敗 | DML 逾時配額 | 把工作拆小，或搬到 Glue / EMR |
| `HIVE_BAD_DATA` / 欄位為 null | 檔案型別與宣告的型別不同 | 發布前驗證；把壞檔案隔離 |
| `503 Slow Down` | 對同一個 S3 prefix 請求太多 | 更少、更大的檔案；分散 prefix |

---

<!-- 06ecbfe711dd -->
## 6) Amazon RDS 與 Aurora ⭐⭐⭐⭐

<!-- 4d3ee161bb26 -->
### 6-1) 從資料工程師的角度看 RDS

**RDS 是託管的 OLTP —— PostgreSQL、MySQL、MariaDB、Oracle、SQL Server、Db2 —— 備份、修補與
failover 都替你處理好。Aurora 是 AWS 自己的引擎（相容 MySQL 與 PostgreSQL），儲存跨三個 AZ
複製六份。對資料工程師來說，它是要從中取出資料的真實來源，不是跑分析的地方。**

- 在 primary 上做分析掃描會和應用程式的交易搶資源 —— 改從 **read replica**、snapshot export
  或變更日誌取出。
- 備份是自動的（保留期內可 point-in-time recovery），另加手動 snapshot；
  **snapshot export to S3** 會把表格寫成 Parquet。

<!-- 6086e3344d62 -->
### 6-2) Multi-AZ vs read replica ⭐⭐⭐⭐⭐

**Multi-AZ 是為了可用性；read replica 是為了讀取擴展。面試官愛問這題，因為很多人會搞混。**

| | Multi-AZ（instance） | Read replica | Aurora replica |
|---|---|---|---|
| 目的 | 撐過 AZ 或 instance 故障 | 分擔讀取、報表、資料擷取 | 兩者皆是：讀取，也是 failover 目標 |
| 複製方式 | 同步複製到 standby | 非同步 —— 可能落後 | 共用叢集儲存，通常延遲很低 |
| 可讀 | standby **不可**讀（Multi-AZ DB *cluster* 有兩個可讀的 standby） | 可 | 可 |
| Failover | 自動，DNS endpoint 切換 | 手動 promote | 自動 |
| 跨 region | 否 | 可 | Aurora Global Database |

<!-- 0147f060fbc2 -->
### 6-3) 把資料從 RDS 取出 ⭐⭐⭐⭐⭐

**依新鮮度、以及是否需要刪除事件來選：小表用 JDBC 拉取，一次性或夜間複本用 snapshot export，
大表或必須看到刪除的就用基於日誌的 CDC，目的地是 Redshift 時用 zero-ETL。**

| 方法 | 新鮮度 | 對來源的負載 | 能捕捉刪除 | 備註 |
|---|---|---|---|---|
| **Glue / Spark JDBC 拉取**（全量或 `updated_at` watermark） | 批次 | 查詢資料庫 —— 用 replica | 否（只有軟刪除） | 用分區欄位平行化；`updated_at` 沒被維護的列會被 watermark 漏掉 |
| **Snapshot export to S3** | 每個 snapshot 一次 | 無 | 以 snapshot 當下為準 | 輸出 Parquet；適合初始載入或夜間全量複本 |
| **AWS DMS 全量載入 + CDC** | 數秒到數分鐘 | 讀 binlog / WAL | **是** | 寫到 S3（帶 `Op` 欄的 Parquet / CSV）、Kinesis 或 MSK |
| **MSK Connect 上的 Debezium** | 數秒 | 讀 binlog / WAL | **是** | Kafka 原生；見 [kafka.md](./kafka.md) |
| **Zero-ETL 整合** | 近即時 | 託管 | **是** | Aurora / RDS → Redshift，不必自己跑管線；只限 Redshift |

<!--CODE-->

<!-- 817ec908215b -->
### 6-4) CDC 前置條件與 replication slot 陷阱 ⭐⭐⭐⭐⭐

**CDC 讀的是資料庫自己的變更日誌，所以日誌必須開啟、格式正確、保留得夠久 —— 而 PostgreSQL 上
一個停滯的消費者可能把磁碟塞滿。**

- **MySQL / Aurora MySQL**：開啟自動備份（這會啟用 binlog）、`binlog_format = ROW`，
  並保留足夠的 binlog 讓消費者在中斷後追得上：
  `CALL mysql.rds_set_configuration('binlog retention hours', 24);`
- **PostgreSQL**：在 parameter group 設 `rds.logical_replication = 1`（需重開機），
  接著由 CDC 工具建立一個 **logical replication slot**。
- **陷阱**：replication slot 會保留消費者尚未確認的每個 WAL 區段。如果 DMS task 或 Debezium
  connector 停了，WAL 會一直堆到儲存空間滿，資料庫停止接受寫入。為它設告警，並刪掉屬於已死消費者的 slot。

<!--CODE-->

CloudWatch 的 `TransactionLogsDiskUsage` 與 `OldestReplicationSlotLag` 是該設告警的指標。

<!-- d8fe59b87ff0 -->
### 6-5) 把 RDS 當作來源來維運 ⭐⭐⭐

- **連線數**：數百個 Spark executor 或 Lambda 呼叫可能耗盡 `max_connections`。
  限制 JDBC 平行度；在 Lambda 前面放 **RDS Proxy**。
- **憑證**：Secrets Manager（含輪替）或 IAM 資料庫驗證 —— 絕不把密碼放在 job 參數裡。
- **網路**：RDS 位於私有子網路；Glue job 需要同一個 VPC 中的 **connection**，從那裡存取 S3
  需要 VPC endpoint 或 NAT。
- **擴展**：垂直擴展（instance class）、儲存自動擴展、read replica；Aurora Serverless v2
  以細粒度調整容量，適合負載起伏大的情境。

---

<!-- 6424a4d4d56f -->
## 7) 事件驅動的資料攝取：S3、SQS 與 Lambda ⭐⭐⭐⭐⭐

**AWS 上處理日誌與檔案攝取的常見形狀：物件落地到 S3，每個物件產生一個事件送進 SQS，由 Lambda
消費者驗證後把重度工作交給 Spark，結果再寫回 S3。面試官會追問邊界：為什麼這樣切分、Lambda
做不到什麼，以及當訊息重複抵達、永遠失敗，或一次湧入一萬則時會發生什麼事。**

<!--CODE-->

<!-- 770330293372 -->
### 7-1) 為什麼是 Lambda *加上* Spark，而不是二選一 ⭐⭐⭐⭐⭐

**「它們不是替代品；它們解決的是不同形狀的工作。Lambda 跑短時間、事件驅動的任務 —— 驗證訊息、
讀取 metadata、路由、啟動下一個階段。Spark 跑大型分散式工作 —— 在許多 executor 之間解析、join、
shuffle 與彙總 1 TB 的資料。讓任何一方做另一方的工作，不是浪費就是做不到。」**

| 工作負載 | 適合 | 原因 |
|---|---|---|
| 對單一物件落地做出反應；檢查它的 metadata 與 schema | **Lambda** | 只在事件抵達時執行；隨佇列擴展；沒有閒置資源要付費 |
| 路由、補充或拒絕一筆小紀錄 | **Lambda** | 幾秒鐘的工作，不需要啟動叢集 |
| 解析與轉換 1 TB 的日誌 | **Spark** | partition 分散到各個 executor；沒有 15 分鐘的上限 |
| 對一整天的資料做 join、`groupBy`、window function | **Spark** | 需要 shuffle —— 在機器之間搬移資料 —— 而 Lambda 沒有這個概念 |
| 每日彙總與報表資料表 | **Spark**（若只是單純 SQL，可用 Athena CTAS） | 對整個 partition 掃描一次 |

- **全部用 Spark**：每個小事件都要一個叢集（或一次 Serverless application 的啟動），增加延遲與
  閒置成本；而為每日尖峰配置的常駐叢集，大部分時間是空的。
- **全部用 Lambda**：15 分鐘上限、10 GB 記憶體上限，加上無狀態、彼此不共享的 invocation，
  讓分散式 join 或彙總變得不可能 —— 除非你手工重做一個更差的 Spark。

**資深加分點：** 這個切分同時也是*故障邊界*。一則壞訊息只會讓一次 Lambda invocation 失敗並進入
DLQ；它不會讓一個兩小時的 Spark job 失敗。

<!-- faff1578de32 -->
### 7-2) Lambda vs 長時間執行的運算 ⭐⭐⭐⭐

**從執行時間、資源、計費模型與維運四個面向比較 —— 然後依工作負載各自配對，而不是為了
「serverless」本身而偏好它。**

| | AWS Lambda | 長時間執行的運算（ECS / Fargate、EC2、EMR） |
|---|---|---|
| 執行時間 | 每次 invocation 最多 **15 分鐘** | 數小時或數天 |
| 資源 | 128 MB – 10,240 MB 記憶體；CPU 隨記憶體成比例增加（最多 6 vCPU）；`/tmp` 最多 10 GB | 任意 instance 大小；本機磁碟；多個節點 |
| 擴展 | 依事件自動擴展，直到 concurrency 上限 | auto scaling group、叢集擴展 —— 較慢，但由你掌控 |
| 狀態 | invocation 之間沒有狀態 | 記憶體內狀態、快取、shuffle |
| 計費模型 | 依請求數 + 執行的 GB-秒；閒置時為零 | 執行期間依 instance-秒計費，不論忙或閒 |
| 持續高負載下的成本 | 可能高於一台充分利用的 instance | 保持忙碌時每單位工作更便宜；還可以加上 Spot |
| 維運 | 不用管伺服器、修補或容量規劃 | AMI 或映像檔、擴展策略、叢集生命週期 |
| 冷啟動 | 有 —— 數毫秒到數秒 | 暖機後就沒有 |

| 選項 | 適用於 |
|---|---|
| **Lambda** | 短時間、突發、事件驅動的工作 |
| **ECS / Fargate** | 執行超過 15 分鐘或需要維持連線的容器，且不想管叢集 |
| **EMR（EC2 或 Serverless）/ Glue** | 分散式 Spark —— 見 [§1-2](#1-2-choosing-the-compute-) 與 [§3-4](#3-4-glue-or-emr-) |
| **AWS Batch** | 大量彼此獨立、容器大小的 job |

**資深加分點：** Lambda 擴展得快不代表一定是好事。一次湧入一萬則訊息，可能意味著一千個同時執行的
invocation 一起打資料庫、API 或 Spark job 提交端 —— 見 [§7-7](#7-7-controlling-consumer-concurrency-)。

<!-- fafadb22f308 -->
### 7-3) Lambda 的 timeout 與記憶體限制 ⭐⭐⭐⭐

**「我讓工作去配合限制，而不是一直調高限制直到它不再失敗：讓每次 invocation 保持小、用串流而不是
整個載入，任何大的或長的工作都交給 Spark。」**

**Timeout（最多 900 秒）：**

- **依時間決定 batch 大小。** Event source mapping 的 batch size 與 batching window 決定一次
  invocation 拿到多少；把它們調到一個 batch 在一兩分鐘內完成，遠低於 timeout。
- **把工作拆成階段。** Lambda 驗證並記錄這個 batch，然後啟動 Spark job 就返回；它從不等待 1 TB
  被處理完。需要等待的多步驟流程屬於 **Step Functions** 或排程器，而不是一個在迴圈裡輪詢的 Lambda。
- **知道 timeout 對訊息造成什麼。** 逾時的 invocation 就是失敗：該 batch 會重新變成可見，並計入
  `maxReceiveCount`，所以一個每次都逾時的 job 最後會進到 DLQ。

**記憶體（128 MB – 10,240 MB）：**

- **記憶體買到的是 CPU。** Lambda 依記憶體成比例分配 CPU —— 1,769 MB 等於一個完整的 vCPU ——
  所以調高記憶體常常讓 CPU 密集的函式更快，*而且*每次執行更便宜。要量測而不是猜：該比較的數字是
  每次 invocation 的成本，或每處理 1 GB 的成本。
- **用串流，不要整個載入。** 用 streaming body 逐行讀取 S3 物件，而不是把 2 GB 的檔案 `read()`
  進記憶體。
- **`/tmp` 可設定到最多 10 GB** 作為溢寫空間，但需要它的函式，通常代表這份工作該交給 Spark。
- **在 invocation 之間重用 client** —— 在 handler 外面建立 `boto3` client 與連線，這樣暖機的
  執行環境不必重建它們。

**觀察：** CloudWatch 中的 `Duration`（對照 timeout）、`Errors`、`Throttles` 與
`ConcurrentExecutions`，以及每次 invocation `REPORT` 日誌行中的 *Max Memory Used*（或 Lambda
Insights）。一個 duration 隨資料量成長而逐漸逼近 timeout 的函式，是在告訴你它已經長大到不適合 Lambda 了。

<!-- 9a0d2dd43a79 -->
### 7-4) 用 Lambda 消費 SQS ⭐⭐⭐⭐⭐

**SQS 把生產者與處理端解耦。Lambda 的 event source mapping 替你輪詢佇列、以一個 batch 呼叫函式，
成功後刪除該 batch。四個設定決定它能不能正常運作：訊息形狀、visibility timeout、batch，以及
部分 batch 失敗回報。**

**1. 訊息形狀 —— 傳指標，不傳資料本身。** 一則訊息上限是 1 MiB，所以它攜帶的是 S3 位置與工作的
識別資訊，而不是日誌內容：

<!--CODE-->

S3 event notification 有自己的形狀（一個 `Records` 陣列，含 `bucket`、`key`、`versionId` 與
`sequencer`）；不論哪一種，消費者都從 S3 讀取物件。

**2. Visibility timeout。** 被接收的訊息只是對其他消費者隱藏，並沒有被刪除。函式成功時，mapping
會刪除它；失敗時，它會在 visibility timeout 結束後重新出現並被重試。把它設為**至少是函式 timeout
的六倍**（AWS 的建議），這樣一個慢的或被節流的 batch，不會在第一次嘗試還在執行時就重新出現、
被執行兩次。

**3. Batch size 與 window。** 一次 invocation 可以拿多則訊息：

| 設定 | 效果 | 取捨 |
|---|---|---|
| `BatchSize` | standard queue 最多 10,000（FIFO 為 10） | batch 越大 → invocation 越少，但失敗時要重做的越多，每次執行也越久 |
| `MaximumBatchingWindowInSeconds` | 最多等待 N 秒（上限 300）來湊滿一個 batch | 以延遲換取更少、更滿的 invocation |
| invocation payload | 每次 invocation 6 MB | 不論 `BatchSize` 設多大，都會限制 batch |

**4. 部分 batch 失敗。** 沒有它時，一則壞訊息會讓整個 batch 失敗並全部重試 —— 連已成功的訊息也
重跑一次。開啟 `ReportBatchItemFailures` 後，函式只回傳失敗訊息的 id：

<!--CODE-->

**觀察：** `ApproximateNumberOfMessagesVisible`（積壓量）、`ApproximateAgeOfOldestMessage`
（消費者落後多少 —— 該設告警的就是它）、`NumberOfMessagesReceived` vs
`NumberOfMessagesDeleted`，以及函式的 `Errors` 與 `Duration`。age 持續增加，代表消費者或它下游的
某個東西跟不上了。

<!-- 12c9cbf6886c -->
### 7-5) 重複訊息與冪等性 ⭐⭐⭐⭐⭐

**「Standard queue 是至少一次交付，S3 event notification 也可能重複，所以我假設每則訊息都可能抵達
兩次。交付不是 exactly-once；我讓*處理*具備冪等性 —— 執行兩次與執行一次的結果相同。」**

重複來自佇列本身、用完的 visibility timeout、被重試的 batch、重試發送的生產者，以及 DLQ 重播。
用三層來處理：

**1. 穩定的業務鍵。** 用生產者的 `event_id`（S3 事件則是 bucket + key + `versionId` /
`sequencer`）去重 —— **不是** SQS 的 `messageId`，因為生產者每重送一次同樣的事件，它都會是新的。

**2. 用條件寫入的認領帳本。** 「先檢查、再處理」有競態：兩個消費者可能都看到「尚未完成」。
DynamoDB 的條件寫入只讓其中一個認領到該事件，而租約（lease）讓重試能接手一個在執行中途死掉的消費者：

<!--CODE-->

（Powertools for AWS Lambda 的 `idempotency` 工具就是這個模式。）

**3. 冪等的寫入端。** 帳本能減少重複，但無法消除重複，因為工作可能已經成功，而行程在寫入
`COMPLETED` 之前就死了。所以輸出必須能安全地寫兩次：

| 寫入端 | 冪等寫入 |
|---|---|
| S3 檔案 | 每個 batch 一個確定性路徑（`.../date=2026-11-10/batch_id=b-001/`），整批覆寫 —— 而且讀取端絕不能看到寫一半的結果 |
| Iceberg / Delta 表格 | 依業務鍵 `MERGE`，或在一次 commit 中替換該 batch 的 partition（[§4-2](#4-2-the-metadata-tree-and-the-commit-)） |
| 資料庫 | 依唯一鍵 upsert |

**FIFO queue 本身不是解法。** 它在 **5 分鐘**的窗口內依 `MessageDeduplicationId` 去重，並在同一個
`MessageGroupId` 內保持順序，代價是較低的吞吐量。一小時後從 DLQ 重播的訊息早已在窗口之外 ——
業務層級的冪等性仍然需要。

**資深加分點：** 把*交付*和*效果*分開來看。佇列決定訊息抵達幾次；管線確保不論它抵達幾次，產出的
資料都一樣。

<!-- bfcd3a32f14d -->
### 7-6) Dead-letter queue 與重播 ⭐⭐⭐⭐

**DLQ 把一直失敗的訊息移出主佇列，讓一則 poison message 不會永遠消耗重試次數與處理容量。
它隔離失敗，但不修好失敗 —— 告警、分類與重播是另一半。**

<!--CODE-->

- 主佇列上的 **Redrive policy**：DLQ 的 ARN 與 `maxReceiveCount`。AWS 建議 Lambda 消費者設
  **至少 5**，因為被節流或逾時的 batch 也算一次接收 —— 設太低，健康的訊息會在流量尖峰時落進 DLQ。
- **保留期限**：在 standard queue 中，訊息移到 DLQ 時保留的是*原本*的入佇列時間，所以 DLQ 的保留期限
  要比來源**更長**（最多 14 天）—— 否則訊息在還沒有人看過之前就在 DLQ 裡過期了。
- 對 DLQ 的 `ApproximateNumberOfMessagesVisible > 0` 設**告警**。沒人看的 DLQ，只是比較慢地把資料丟掉。
- **依原因分類：**

| 原因 | 處理 |
|---|---|
| 暫時性 —— 網路抖動、下游中斷、節流 | 依賴恢復健康後 redrive |
| 格式錯誤的訊息、缺少欄位 | 修正生產者；記錄原因後 redrive 或捨棄 |
| 缺少或延遲的資料 | 資料品質調查 |
| 消費者的 bug | 修正、部署，再 redrive |

- **重播**：SQS 的 DLQ redrive（`StartMessageMoveTask`，或主控台的 *Start DLQ redrive*）以受控的
  速率把訊息搬回來源佇列。重播本質上就是重複 —— 這正是 [§7-5](#7-5-duplicate-messages-and-idempotency-)
  要先做的原因。

<!-- aaefd78e237a -->
### 7-7) 控制消費者的 concurrency ⭐⭐⭐⭐

**「我依下游最慢的那一環來決定 concurrency，而不是依 Lambda 能做到多少。如果 Spark 叢集每分鐘只能
接受二十次 job 提交，五百個同時執行的消費者只是把佇列的問題丟給別人。」**

不設上限的消費者會耗盡資料庫連線、觸發 API rate limit、提交過多 Spark job，並且快速燒錢。
由佇列往外的控制手段：

| 控制 | 限制的對象 | 說明 |
|---|---|---|
| SQS event source mapping 上的 **Maximum concurrency** | 由*這個佇列*驅動的 invocation | SQS 的正確旋鈕：mapping 在達到上限時停止輪詢，多出來的訊息就只是在佇列裡等待 |
| 函式上的 **Reserved concurrency** | 這個函式來自任何來源的*所有* invocation | 同時保證函式在帳號內的容量；但若單獨搭配 SQS 使用，poller 仍會取訊息、invocation 被節流，而被節流的訊息會計入 `maxReceiveCount` —— 健康的訊息會漂進 DLQ |
| **Batch size / window** | 每次 invocation 的訊息數 | concurrency × batch size 就是處理中的訊息數 —— 這裡是 50 × 10 = 500，實際吞吐量再由 duration 與到達速率決定 |
| **下游** | 共用資源本身 | 連線用 RDS Proxy、job 提交佇列或 semaphore、每個 batch 或時間窗口一個 Spark job 而不是每則訊息一個、搭配 exponential backoff 與 jitter 的重試 |

如果兩種上限都設，讓 mapping 的 maximum concurrency **低於**函式的 reserved concurrency，
這樣守住防線的是 mapping，而不是節流。

**觀察：** 函式的 `ConcurrentExecutions` 與 `Throttles`、佇列深度與最舊訊息的 age，以及下游自己的
延遲與提交速率。佇列 age 上升而 concurrency 持平，代表上限正在發揮作用 —— 只有在下游還有餘裕時才調高它。

<!-- 1fad0ec5f199 -->
## 8) 在 S3 中組織資料 ⭐⭐⭐⭐

<!-- ec901ecaf8c9 -->
### 8-1) Hive 風格分區與 partition pruning ⭐⭐⭐⭐⭐

**分區是為讀取端設計的：路徑中的 key 讓 Spark、Athena 與 Glue 跳過所有不符合篩選條件的物件。
依將要執行的查詢、照它們篩選的順序來選 key。**

<!--CODE-->

<!--CODE-->

- **日期優先**，因為幾乎每個日誌查詢都有時間範圍。單一的 `dt=yyyy-MM-dd` key 比
  `year=/month=/day=` 好篩選（跨月的範圍只是一次比較，而不是三次）。
- **只有在查詢會篩選它、且基數低時才加第二個 key** —— 只有數十個值的 `cluster_id` 可以；
  `request_id` 或 `user_id` 絕對不行。
- **小時**分區只在資料量大到一小時仍能產生大檔案、且查詢會以小時為單位時才用。
- 用 **partition projection** 註冊分區，或使用 **Iceberg** —— 見
  [§5-3](#5-3-partitions-repair-add-or-project-) 與
  [§4-3](#4-3-hidden-partitioning-and-partition-evolution-)。

**資深加分點：** S3 是物件儲存，不是 HDFS —— 一個 partition 只是一個 key prefix。Pruning 之所以有幫助，
是因為*引擎*從不列出或開啟被跳過的物件。Prefix 對 S3 本身的意義只在請求速率（每個 prefix 每秒約
3,500 次寫入與 5,500 次讀取），那是分散 key 的另一個理由。

<!-- c8fc6042942e -->
### 8-2) 基數、檔案大小與小檔案問題 ⭐⭐⭐⭐

**每多一個分區 key，目錄數就成倍增加；太多的話，每個目錄只裝著幾個小檔案。小檔案在每次讀取時都有
成本：同樣的位元組，要更多次 listing、更多 GET 請求、更多 Spark task，以及開更多 Parquet footer。**

| 手段 | 做法 |
|---|---|
| 格式 | **Parquet**（或 ORC），以 Snappy 或 ZSTD 壓縮 —— 欄位裁剪、依 row-group 統計做 predicate pushdown、更小的掃描量 |
| 目標檔案大小 | 每個檔案約 **128 MB – 1 GB** |
| 分區 | 只用低基數的 key；檢查每個分區的檔案數與每個檔案的大小 |
| 從 Spark 寫入 | 在 `partitionBy` 之前依分區欄位 `repartition`，讓每個 task 對每個分區只寫一個檔案，而不是每個分區*每個 task* 一個；用 `maxRecordsPerFile` 當上限 |
| 已經是小檔案 | 排程的 compaction job —— 或 Iceberg 的 `rewrite_data_files`（[§4-5](#4-5-table-maintenance-)） |
| 傾斜 | 一個巨大的分區（某個吵雜的叢集）加上許多小分區 → 對熱點 key 加鹽或拆分，並檢查分區大小的分布 |
| 舊資料 | S3 Lifecycle：raw 在 30 天後轉到 Standard-IA，之後轉 Glacier 等級，並讓保留政策允許的資料過期 |

<!--CODE-->

搭配 `spark.sql.sources.partitionOverwriteMode=dynamic` 時，那個 `overwrite` 只替換 `logs` 中出現的
分區，所以重跑某一天是冪等的，且不會動到其他天。在一般的 S3 表上，這個替換仍然不是原子的 ——
讀取端可能看到重寫到一半的分區；Iceberg 讓它成為一次 commit。

<!-- c32ee5c96929 -->
## 9) 管線維運：CDK、監控與成本 ⭐⭐⭐⭐

<!-- 27ab1948f393 -->
### 9-1) 用 AWS CDK 做基礎設施即程式碼 ⭐⭐⭐⭐

**「每個 bucket、queue、函式、role 與告警都是 repo 裡的 TypeScript，像程式碼一樣被 review，並透過
同一條管線部署到每個環境。沒有任何東西是在主控台上點出來的，所以 dev、staging 與 prod 的差別只在我們
選的參數。」**

- **CDK** 會合成（synthesise）**CloudFormation**：`cdk synth` 產出 template，`cdk diff` 顯示一個變更
  會對已部署的 stack 做什麼，`cdk deploy` 套用它。
- **Construct** 是重用的單位 —— 一個 `QueueWithDlq` 或 `IngestPipeline` construct，把佇列、DLQ、
  消費者、它的權限與告警包在一起，讓下一條管線不可能忘記 DLQ。
- **環境**是 stack 參數 —— 名稱、concurrency 上限、告警門檻、資源大小 —— 而不是程式碼的複本。

<!--CODE-->

**部署流程：** pull request → 單元測試（`aws-cdk-lib/assertions` 可以斷言，例如每個佇列都有
redrive policy）→ 把 `cdk synth` 與 `cdk diff` 貼在 review 上 → 部署到 staging → 驗證 → 部署到 prod。

**資深加分點：**

- **基礎設施部署可能摧毀資料。** 改變資源的 logical id 或某個只能替換的屬性，會讓 CloudFormation 建立
  一個新的並刪除舊的 —— bucket 或資料表也跟著一起走。對有狀態的資源設 `RemovalPolicy.RETAIN`，並在核准前
  讀 `cdk diff` 中的 `[-]` 與 *replace*。
- **Rollback 還原的是 template，不是資料**，而一個卡在 `UPDATE_ROLLBACK_FAILED` 的 stack 本身就是一起事故。
- 用 `grant*` 方法，**最小權限**幾乎不費力；**程式碼裡沒有秘密資訊** —— 用 Secrets Manager 或 SSM
  Parameter Store；每個資源都打上**標籤**（`Tags.of(app).add(...)`），成本才能歸屬。

<!-- 0c265b1dfa5a -->
### 9-2) 監控管線健康度 ⭐⭐⭐⭐⭐

**「AWS 指標全綠，不代表資料是對的。我會看基礎設施，但我對資料設告警：它有沒有準時到、完不完整、
看起來對不對。」**

| 層 | 觀察 | 它告訴你 |
|---|---|---|
| 攝取 | 每小時、每個來源落地的物件數與位元組 | 某個來源安靜了，或翻倍了 |
| 佇列 | `ApproximateAgeOfOldestMessage`、可見訊息數、DLQ 深度 | 消費者在落後，或訊息在失敗 |
| Lambda | `Errors`、`Throttles`、`Duration` 對照 timeout、`ConcurrentExecutions` | 消費者在失敗、被限流，或已經長大到不適合 Lambda |
| Spark | job 時長、失敗的 stage 與 executor、shuffle read/write、spill、task 時間的傾斜 | job 在變慢，以及原因 |
| 新鮮度 | 最新的完整分區、最後一次成功執行的時間 | 資料遲到了 |
| 完整性 | 對照來源或昨天的列數、每個預期的來源都在 | 資料不完整 |
| 品質 | 關鍵欄位的 null 比例、重複的 key、解析失敗率、schema 漂移 | 資料是錯的 |

- **對 SLA 設告警，而不只是對錯誤。**「昨天的日誌在 08:00 前可查詢」才是契約。如果 job 在 08:45
  完成而每個服務都健康，管線仍然辜負了它的使用者 —— 所以要在 08:00 的分區還不完整時告警，而不是在
  job 出錯時才告警。
- **端到端延遲**：在紀錄上蓋 `ingested_at` 與 `processed_at`，並追蹤兩者差距的 p95 / p99。
- **更少、更好的告警。** 對 SLA 風險與 DLQ 發 page；其他的放到儀表板。一個沒人處理的告警，會訓練大家
  忽略真正重要的那一個。
- 用資料品質檢查（Glue Data Quality、Deequ、Great Expectations）**把關發布**，讓糟糕的一天被擋下來，
  而不是發布出去之後再解釋。

<!-- 217aefe8aae6 -->
### 9-3) 盯緊成本 ⭐⭐⭐⭐

**「在一天 1 TB 的規模下，帳單是設計的一部分。我替所有東西打標籤以便歸屬、追蹤每處理 1 GB 的成本，
並且知道每一項優化動的是哪一行費用。」**

| 服務 | 帳單的驅動因素 | 手段 |
|---|---|---|
| S3 | 儲存的 GB、請求數、傳輸量 | Parquet + 壓縮；更少、更大的檔案；用 Lifecycle 轉到 IA / Glacier；讓可以過期的資料過期 |
| Lambda | 請求數 + GB-秒 | batching，減少 invocation；調整記憶體大小；arm64（Graviton） |
| SQS | API 請求（每 64 KB 算一次） | 批次接收與刪除 —— event source mapping 會替你做 |
| EMR / Glue 上的 Spark | instance-小時或 DPU-小時 | 調整 executor 大小、修正傾斜與 shuffle、EMR task 節點用 Spot、用臨時叢集或 Serverless 取代閒置叢集 |
| CloudWatch | 日誌攝取的 GB、保留期、自訂指標 | prod 的日誌等級、每個 log group 都設保留期、不要每筆紀錄都打日誌 |
| Athena | 掃描的位元組 | [§5-2](#5-2-the-cost-model-is-the-performance-model-) |

- **成本歸屬：** 從 CDK app 對每個資源打上 **cost allocation tag**（要在 Billing 中啟用）；用
  **Cost Explorer** 依標籤檢視；設 **AWS Budgets** 告警；用 **Cost Anomaly Detection** 抓沒人預料到的
  失控迴圈或請求風暴。
- **單位成本** —— 每攝取 1 GB、每處理一天的成本 —— 是資料量成長後仍然有意義的數字。總額因為資料量
  翻倍而翻倍沒問題；每 GB 成本翻倍就有問題。

<!-- 971f45bc1e3a -->
## 10) 安全性與治理 ⭐⭐⭐⭐

**每個 job 一個最小權限的 IAM role、到處都用 KMS 加密、私有網路，以及用 Lake Formation 決定
誰能看到哪一欄。**

| 關注點 | AWS 的答案 |
|---|---|
| Job 身分 | 每個 Glue job / EMR Serverless application 一個 IAM role，只授權它的 prefix 與表格 |
| 細粒度存取 | Lake Formation 對 database、表、欄、列（data filter）授權；用 LF-tag 擴大規模 |
| 靜態加密 | S3 用 SSE-KMS；RDS、Glue catalog 與 job bookmark 用 KMS；Athena 結果加密 |
| 傳輸加密 | 連 RDS 用 TLS；S3 走 HTTPS |
| 網路 | S3、Glue、Athena 的 VPC endpoint；RDS 放在私有子網路 |
| 秘密資訊 | Secrets Manager，由 Glue connection 引用 |
| 稽核 | API 呼叫用 CloudTrail；資料存取用 Lake Formation 與 S3 存取日誌 |
| PII 刪除 | Iceberg `DELETE`、重寫受影響的檔案，再讓 snapshot 過期；清除 raw 複本與 S3 舊版本（[§4-5](#4-5-table-maintenance-)） |

---

<!-- 5dcd9ad79685 -->
## 11) 完整設計題：從 RDS 訂單到 S3 lakehouse ⭐⭐⭐⭐⭐

**題目**：*「訂單存在 Aurora PostgreSQL 資料庫中。分析師每天早上要在 Athena 看到昨天各國的營收，
財務要求更正與取消都要反映出來。請在 AWS 上設計。」*

<!--CODE-->

| 決策 | 理由 |
|---|---|
| **DMS CDC**，而不是依 `updated_at` 做 JDBC 拉取 | 看得到硬刪除與取消；不對 primary 造成負載 |
| **`IncludeOpForFullLoad=true` + 變更序號欄位** | 全量載入的列也會像 CDC 列一樣帶 `Op`（否則是 NULL，`MERGE` 必須把它當成新增）；序號決定每個 key 的事件順序 |
| **原始資料照落地的樣子保留** | 可重播：silver 的 bug 從 raw 重跑就能修好 |
| **Iceberg silver 搭配 `MERGE`** | 列層級更正；原子發布；用 time travel 回答「昨天的報表說了什麼？」 |
| **Merge-on-read + 夜間 compaction** | 每小時的 upsert 保持便宜；讀取保持快速 |
| **Glue job**，而不是 EMR | 資料量不大、沒有平台團隊；若成本或調校需要再搬到 EMR Serverless |
| **以 `order_id` 為 key 的冪等 `MERGE`** | 重試的執行產生同一張表 |
| **Gold 依日期分區** | 早上的查詢只讀一天 |
| **Workgroup 掃描上限** | 一個不小心的 `SELECT *` 不會變成整個月的帳單 |

值得主動提出的故障模式：

- **DMS task 停止** → PostgreSQL 的 slot 保留 WAL（[§6-4](#6-4-cdc-prerequisites-and-the-replication-slot-trap-)）；對延遲與磁碟設告警。
- **變更亂序或被重播** → 依來源變更序號 / LSN 去重，而不是依 `updated_at` 或到達時間，
  並跳過比該列已存序號更舊的事件。
- **上游 schema 變更** → DMS 會帶上新增的欄位；`MERGE` 明確列出欄位，Iceberg 依欄位 id 演進表格。
- **Compaction 與 `MERGE` 衝突** → compaction 只限定在今天以前的分區。

---

<!-- 493399db1ea7 -->
## 12) 面試工具箱 ⭐⭐⭐⭐⭐

<!-- 8a5e5dfc9622 -->
### 12-1) 快問快答

- **Q：Glue Data Catalog 是什麼？** —— 一個由 Athena、EMR、Glue 與 Redshift Spectrum 共用的
  託管 Hive metastore；對 Iceberg 表格，它只存指向目前 metadata 檔的指標。
- **Q：用 crawler 還是 DDL？** —— 探索用 crawler；正式環境用宣告的 schema（DDL / IaC），
  因為猜出來的 schema 會漂移。
- **Q：Glue bookmark 怎麼運作，會出什麼錯？** —— 它記錄上次成功執行處理了什麼；需要
  `transformation_ctx` 與 `job.commit()`，而且是 at-least-once，所以寫入必須冪等。
- **Q：Glue 還是 EMR？** —— 不想維運的一般 Spark 用 Glue；需要版本掌控、非 Spark 引擎、深度調校，
  或大規模下的 Spot 省錢時用 EMR。
- **Q：EMR 上的 Spot instance 放哪裡？** —— Task 節點。Core 節點存放 HDFS。
- **Q：Iceberg 的 commit 在 S3 上怎麼保持原子性？** —— 新檔案、新 metadata，然後對 catalog 指標
  做 compare-and-swap；輸的寫入者重試。
- **Q：Copy-on-write 還是 merge-on-read？** —— 讀取為主的批次用 CoW；頻繁 upsert 用 MoR，
  並搭配 compaction。
- **Q：怎麼降低 Athena 帳單？** —— Parquet、壓縮、依篩選欄位分區、選更少的欄位、壓實小檔案、
  設定 workgroup 掃描上限。
- **Q：Multi-AZ vs read replica？** —— 為 failover 的同步 standby vs 為讀取的非同步複本。
- **Q：怎麼從 RDS 取出資料又不傷到它？** —— 從日誌做 CDC（DMS / Debezium），或從 replica /
  snapshot export 讀；絕不對 primary 做全表掃描。
- **Q：replication slot 陷阱是什麼？** —— 停掉的 CDC 消費者會讓 PostgreSQL 一直保留 WAL，
  直到儲存空間滿。
- **Q：為什麼不全部用 Lambda？** —— 15 分鐘、10 GB、沒有共享狀態、沒有 shuffle。Lambda 對事件做出
  反應並啟動 job；分散式的工作由 Spark 做。
- **Q：SQS 的 visibility timeout 該設多長？** —— 至少是函式 timeout 的六倍，這樣慢的 batch 不會
  重新出現、被執行兩次。
- **Q：怎麼處理重複的 SQS 訊息？** —— 假設它一定會發生：穩定的業務鍵、用 DynamoDB 條件寫入認領，
  以及冪等的寫入端（`MERGE`、確定性路徑）。
- **Q：`ReportBatchItemFailures` 做什麼？** —— 只重試 batch 中失敗的訊息，而不是整個 batch。
- **Q：Lambda 消費者的 `maxReceiveCount` 設多少？** —— 至少 5，因為節流與逾時都算一次接收；
  而 DLQ 的保留期限要比來源佇列長。
- **Q：Reserved concurrency 還是 maximum concurrency？** —— SQS 觸發用 event source mapping 的
  maximum concurrency：多出來的訊息在佇列中等待，而不是被節流進 DLQ。
- **Q：日誌在 S3 怎麼分區？** —— 日期優先，再加一個查詢會篩選的低基數 key；Parquet 檔案
  128 MB – 1 GB；把小檔案壓實。

<!-- 1a6725c4ee9a -->
### 12-2) 「我們的 Athena 查詢又慢、帳單又翻倍。你會怎麼做？」

> 「我會從 Athena 計費的依據 —— 掃描的位元組 —— 開始，因為成本和延遲出自同一個原因。
>
> 首先，我看查詢歷史中掃描資料量最大的查詢，檢查它們的形狀：
> `SELECT *`、缺少分區篩選，或篩選的欄位並不是表格的分區欄位。
>
> 接著看底下的表格：它們是不是 CSV 或 JSON 而不是 Parquet、沒有壓縮，或由串流寫入者
> 產生的數千個小檔案組成？
>
> 解法依效益排序：用 CTAS 或 ETL job 把熱門表格轉成分區、壓縮的 Parquet；如果會有更新就搬到
> Iceberg；排程壓實；並讓重度儀表板改讀預先彙總好的 gold 表。
>
> 然後加上護欄：每個團隊一個 workgroup 並設每查詢掃描上限，再對掃描位元組設告警。如果某個儀表板
> 仍然整天跑同一個重度 join，那個工作負載就該放到 Redshift。」

<!-- c45eeed175bf -->
### 12-3) 「管線在一天 1 TB 時要兩小時；到 2 TB 卻要五小時。為什麼？」

> 「兩倍的資料大約應該花兩倍的時間，所以多出來的那一小時才是重點 —— 有某個東西不再線性擴展了。
> 我會依序走過整條管線，找出時間花到哪裡去了。
>
> 首先看輸入：資料量是真的均勻地翻倍，還是某個來源或某個叢集變大了 —— 那會指向傾斜？接著看佇列：
> 最舊訊息的 age 有沒有在爬升，消費者有沒有碰到 concurrency 上限或被節流？
>
> 然後在 Spark UI 裡看 Spark job：哪個 stage 變長了。一個成長幅度超過輸入的 shuffle stage、
> 少數幾個 task 跑得比中位數久很多，或溢寫到磁碟，都在說傾斜或分區太小；數千個很小的輸入檔案說的是
> 上游的小檔案問題；job 開始前的長時間等待，說的是叢集沒有容量。
>
> 再來看依賴：下游變慢的資料庫或 API，會把它後面的一切都卡住。
>
> 解法跟著原因走 —— 傾斜就加分區或對 key 加鹽，小檔案就 compaction，只有下游還有空間時才調高
> concurrency 上限，工作真的能水平擴展時才加 executor。我還會補上那個本來可以更早抓到它的指標：
> 每 GB 的時長，並對它設告警，而不是只看總時長。」

<!-- d5803c041987 -->
### 12-4) 用你自己的管線回答「你是怎麼……的？」

這些問題問的是*你的*系統，面試官會追問你說的每一句話。五條原則：

1. **先講為什麼，再講怎麼做。** 開頭說出決策以及驅動它的限制，而不是列一串服務。
2. **講出取捨。**「Lambda 運行更便宜、維運更簡單，但它做不了彙總，所以彙總交給 Spark。」
3. **誠實地使用真實數字。** 1 TB/day 是每日平均 —— 平均攤開約 12 MB/s —— 不是尖峰；要知道你引用的是
   哪一個，以及尖峰是多少。
4. **主動展現生產環境思維：** 重試、冪等性、DLQ、監控、復原、成本。
5. **區分你做過的與你知道的。** 被問「你是怎麼實作的？」時，只描述你實際做過的部分。替代方案放在
   *「如果今天重新設計，我會……」*之下 —— 這樣能展現判斷力，又不會宣稱你沒做過的工作。

如果只有時間準備其中三題，就練習 **為什麼用 Lambda 而不是 Spark**（[§7-1](#7-1-why-lambda-and-spark-not-one-of-them-)）、
**重複訊息**（[§7-5](#7-5-duplicate-messages-and-idempotency-)）與 **S3 分區**
（[§8-1](#8-1-hive-style-partitioning-and-partition-pruning-)）：每一題都能延伸成系統設計討論。
把每個答案用英文大聲說出來，控制在一到兩分鐘，並準備好它底下的追問。

<!-- a6b531ba3167 -->
### 12-5) 服務速查表

| 這樣說 | 當被問到 |
|---|---|
| 「一個 catalog，多個引擎」 | 為什麼 Glue Data Catalog 很重要 |
| 「用宣告的，不用爬的」 | 正式環境的 schema |
| 「bookmark 是 at-least-once」 | Glue 增量載入 |
| 「Spot 放 task 節點，資料放 S3」 | EMR 的成本與可靠性 |
| 「commit = 交換指標」 | Iceberg 在 S3 上的原子性 |
| 「不維護就會爛掉」 | Iceberg compaction 與 snapshot 過期 |
| 「掃描的位元組就是帳單」 | Athena 的效能與成本 |
| 「可用性 vs 讀取擴展」 | Multi-AZ vs read replica |
| 「從日誌做 CDC，不要用查詢」 | 從 RDS 擷取資料 |
| 「Lambda 觸發，Spark 計算」 | Lambda vs Spark |
| 「進來是至少一次，出去是冪等」 | 重複的 SQS 訊息 |
| 「visibility ≥ 6 × 函式 timeout」 | SQS + Lambda 的重試 |
| 「依最慢的下游決定 concurrency」 | 消費者 concurrency |
| 「DLQ 隔離問題，不修好問題」 | Dead-letter queue |
| 「日期優先、低基數、大檔案」 | S3 分區 |
| 「對 SLA 告警，不只是對錯誤」 | 管線監控 |
