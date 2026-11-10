<!-- a56a24bb52fd -->
# AWS 資料服務：Glue、EMR、Iceberg、Athena 與 RDS

> **範圍** —— 資料工程面試所考的 AWS 資料技術棧：S3、Glue Data Catalog、Glue ETL、EMR、Athena 與 RDS 如何拼在一起、如何在各種運算服務之間做選擇、Glue job 與 bookmark、EMR 叢集組成與 EMR Serverless、在 S3 上搭配 Glue catalog 使用 Apache Iceberg、Athena 的計費模型、分區與 Iceberg DML、如何把資料從 RDS 取出（JDBC、snapshot export、DMS CDC、zero-ETL）、安全性，以及一題從 RDS 到 lakehouse 的完整設計題。Iceberg、Delta 與 Hudi 的引擎中立比較歸 [batch_pipeline.md](./batch_pipeline.md)；Spark 內部機制與調校歸 [spark.md](./spark.md)；Redshift 另有較舊的筆記。
> **另見**：[`./batch_pipeline.md`](./batch_pipeline.md) —— lake vs warehouse vs lakehouse、表格格式、冪等性與回補；[`./spark.md`](./spark.md) —— Glue 與 EMR 底下的引擎；[`./kafka.md`](./kafka.md) —— 用 Debezium 與 Kafka Connect 做 CDC；[`../db/faq_redshift.md`](../db/faq_redshift.md) —— Redshift 筆記；[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單。

大多數 AWS 資料平台都是同樣五塊：**S3 裡的檔案**、**說明哪些檔案構成一張表的 catalog**
（Glue Data Catalog）、**讓這些檔案具備交易性的表格格式**（Iceberg）、**讀寫它們的引擎**
（Glue、EMR、Athena），以及**資料來源的營運資料庫**（RDS / Aurora）。面試很少要你背誦某個服務；
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

<!-- 8b0ec178c456 -->
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

<!-- 80af20a30939 -->
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
省下的成本時才用 EMR on EC2，只有當 BI 工作負載重到、重複到值得養一座資料倉儲時才用 Redshift。」**

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

<!-- d1971f56f87d -->
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
snapshot 中；以及表格以外的複本中。所以要：`DELETE`，接著重寫受影響的檔案（`rewrite_data_files` /
`OPTIMIZE`），讓目前的 snapshot 不再含有該列，再讓仍含有它的 snapshot 過期，最後清除其他複本 ——
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

<!-- a2668b72b23b -->
## 7) 安全性與治理 ⭐⭐⭐⭐

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

<!-- dfce09ab06ec -->
## 8) 完整設計題：從 RDS 訂單到 S3 lakehouse ⭐⭐⭐⭐⭐

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

<!-- 9f777376d202 -->
## 9) 面試工具箱 ⭐⭐⭐⭐⭐

<!-- 0e874ab4999b -->
### 9-1) 快問快答

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

<!-- 0e58ccdc423f -->
### 9-2) 「我們的 Athena 查詢又慢、帳單又翻倍。你會怎麼做？」

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

<!-- 727e2f5aa4d8 -->
### 9-3) 服務速查表

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
