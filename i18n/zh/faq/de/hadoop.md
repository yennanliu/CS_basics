<!-- c784f9b0c947 -->
# Hadoop 生態系面試深入解析

> **範圍** — 資料工程師面試會探的 Hadoop 生態系：HDFS 內部機制、MapReduce 從頭到尾、YARN、Hive、HBase、ZooKeeper、周邊工具、檔案格式，以及整個技術棧如何移往物件儲存與 lakehouse。Spark 本身不在本篇範圍內。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) — 總覽與考前檢查清單；[`spark.md`](./spark.md) — 取代 MapReduce 的引擎；[`batch_pipeline.md`](./batch_pipeline.md) — 管線中的檔案格式與表格格式；[`sql_vs_nosql.md`](./sql_vs_nosql.md) — HBase 在 NoSQL 儲存中的位置；較舊的筆記 [`../spark/faq_hadoop.md`](../spark/faq_hadoop.md) 與 [`../spark/faq_mapreduce.md`](../spark/faq_mapreduce.md)。

答案以 **Apache Hadoop 3.x** 與 **Hive 3/4** 為準。每一節都先給你會說出口的那句話，
再講機制，然後是面試官下一步會追問的取捨。

---

<!-- 5a195ed7e88a -->
## 1) 全貌 ⭐⭐⭐

<!-- 8bfcc45f69e9 -->
### 1-1) Hadoop 是什麼，以及它的三層

**Hadoop 是在一般商用機器組成的叢集上儲存與處理超大資料集的框架：HDFS 存資料，
YARN 分配叢集資源，MapReduce（或 Tez、Spark）在上面運算。**

| 層 | 元件 | 工作 |
|---|---|---|
| 儲存 | **HDFS** | 分散式、有副本的檔案系統 |
| 資源管理 | **YARN** | 在各應用程式之間排程 CPU 與記憶體 |
| 運算 | **MapReduce**（以及跑在 YARN 上的 Tez、Spark、Flink） | 執行處理 |
| 共用 | **Hadoop Common** | 共用函式庫、RPC、設定、安全 |

<!--CODE-->

<!-- c9b8c6b4b95c -->
### 1-2) 背後的設計原則

**Hadoop 假設硬體隨時會壞，所以它複製資料、重試工作，並且把運算搬到資料旁邊，
而不是把資料搬到運算旁邊。**

- **故障是常態** —— 磁碟或節點故障靠副本與 task 重試處理，而不是靠昂貴的硬體。
- **Data locality（資料在地性）** —— 排程器偏好把 task 跑在持有該 block 的節點上
  （至少同一個機架），因為網路才是稀缺資源。
- **一次寫入、多次讀取** —— 檔案只能附加，不能原地更新，這讓複製與一致性都很單純。
- **吞吐量優先於延遲** —— 為大型循序掃描而生，不是為毫秒級查找（那是 HBase 的工作）。
- **水平擴展** —— 加節點就同時加了儲存與運算。

---

<!-- 19ce6c10a8ac -->
## 2) HDFS ⭐⭐⭐⭐⭐

<!-- e6028b805788 -->
### 2-1) 架構：NameNode、DataNode、block

**HDFS 把每個檔案切成大 block（預設 128 MB），每個 block 存在多個 DataNode 上
（預設三份副本），並把整個命名空間放在一台 NameNode 的記憶體裡。**

| 元件 | 存放 / 負責 |
|---|---|
| **NameNode** | 命名空間（目錄、檔案、權限）與檔案 → block 的對應，放在記憶體；以 fsimage + edit log 持久化命名空間 |
| **DataNode** | 以本機檔案存放 block；送出心跳（預設每 3 秒）與定期的 block report |
| **Secondary NameNode** | 在非 HA 叢集中把 edit log 合併進 fsimage（checkpoint）—— **不是**熱備援 |
| **Standby NameNode** | 在 HA 叢集中，命名空間的即時副本，可以接手，並負責做 checkpoint |
| **JournalNode** | 在 HA 中存放共享的 edit log（通常是 3 或 5 台組成的 quorum） |

- NameNode **不會**持久化 block 的位置；啟動時它從 block report 重建 block → DataNode 的
  對應。在回報的 block 足夠之前，它停在 **safe mode**（唯讀）。
- Client 只為了中介資料才找 NameNode；資料本身直接在 client 與 DataNode 之間流動，
  所以 NameNode 不是資料瓶頸。
- 一個 DataNode 約 10 分鐘沒有心跳就會被判定死亡，它的 block 會從存活的副本重新複製。

<!-- d97b57fcf561 -->
### 2-2) fsimage、edit log 與 checkpoint

**fsimage 是命名空間的快照，edit log 記錄之後的每一次變更；checkpoint 把兩者合併成新的
fsimage，讓重啟時不必重播好幾天的 edit。**

<!--CODE-->

- Checkpoint 依時間（`dfs.namenode.checkpoint.period`，預設 1 小時）或依尚未 checkpoint 的
  交易數（`dfs.namenode.checkpoint.txns`）觸發。
- 沒有 checkpoint 的話，edit log 會無限成長，NameNode 重啟可能要花好幾個小時。

<!-- d78a89b5aba5 -->
### 2-3) 高可用：JournalNode 與 ZKFC

**HDFS HA 執行一個 active 和一個 standby NameNode，它們透過一組 JournalNode quorum 共享
edit log；每台 NameNode 上的 ZooKeeper Failover Controller 偵測故障並把 standby 升上來。**

| 元件 | 角色 |
|---|---|
| **Active NameNode** | 服務所有 client 請求，把 edit 寫到 JournalNode |
| **Standby NameNode** | 從 JournalNode 追讀 edit，保持命名空間最新，也接收 block report |
| **JournalNodes（QJM）** | 多數節點確認後 edit 才算提交；2f + 1 台可容忍 f 台故障 |
| **ZKFC** | 對本機 NameNode 做健康檢查，在 ZooKeeper 持有一把 ephemeral 鎖，觸發 failover |
| **Fencing（隔離）** | 確保舊的 active 不能繼續寫 —— QJM 只接受最新 epoch 的寫入 |

<!--CODE-->

- **Split-brain（腦裂）**風險 —— 兩台 NameNode 都以為自己是 active —— 正是 fencing 與
  QJM 的 epoch 編號要防止的。
- Hadoop 3 支援多於一台 standby NameNode。

<!-- 1ddaff26ad5d -->
### 2-4) Federation 與 router-based federation

**Federation 讓命名空間水平擴展：好幾台獨立的 NameNode 各自擁有命名空間的一部分，
而所有 DataNode 都替它們全部存放 block。**

- 每台 NameNode 管理一個 **namespace volume** 和它自己的 **block pool**；DataNode 向每一台
  NameNode 註冊。
- HA 與 federation 是正交的：HA 移除單點故障，federation 移除單一 NameNode 的記憶體上限。
  大型叢集兩個都用。
- Client 透過 **ViewFs**（client 端的掛載表）或 **Router-based Federation**（server 端的
  router 層，Hadoop 2.9+）看到同一棵樹。

<!-- ce81ef08b4ad -->
### 2-5) 寫入管線

**Client 先問 NameNode 要寫到哪，然後把 block 串流經過一條 DataNode 管線 —— 每一台
存下資料並轉給下一台 —— 確認（ack）再沿著鏈往回傳。**

<!--CODE-->

- 資料以**封包**（預設 64 KB）連同 checksum 傳送；client 維護一個 data queue 和一個
  ack queue，一個封包要等整條管線都 ack 後才會丟棄。
- **寫到一半有 DataNode 故障時**，管線會關閉，未 ack 的封包放回佇列前端，移除故障節點，
  繼續寫到剩下的節點。NameNode 發現該 block 副本不足，之後再補複製。
- 正在寫入的檔案，對讀取者只可見到最後一個完成的 block，除非寫入者呼叫
  `hflush()` / `hsync()`。

<!-- 3203fbf177fc -->
### 2-6) 讀取路徑

**Client 從 NameNode 取得依網路距離排序的 block 位置，然後直接從最近的 DataNode 讀取
每個 block，一邊讀一邊驗證 checksum。**

- 在地性順序：同一節點 → 同一機架 → 其他機架。
- 遇到 checksum 錯誤或 DataNode 掛掉時，client 改讀下一份副本並回報損壞的 block，
  NameNode 接著從好的副本重新複製。
- **Short-circuit local reads（短路本機讀取）**讓同一台機器上的 client 直接讀 block 檔，
  繞過 DataNode 的 TCP 路徑。

<!-- 85601cd2f7e7 -->
### 2-7) 副本、機架感知與 erasure coding

**預設的放置策略是：第一份副本在寫入者的節點，第二份在另一個機架的節點，第三份在第二個
機架的另一個節點 —— 這樣一整個機架壞掉也不會遺失資料，而只有一份副本需要跨機架。**

| | 3 份副本 | Erasure coding（RS-6-3） |
|---|---|---|
| 儲存額外開銷 | 200%（原始的 3 倍） | 50%（原始的 1.5 倍） |
| 可容忍 | 遺失 2 份 | 9 個 cell 中遺失 3 個 |
| 寫入 / 讀取成本 | 便宜 | 編碼要 CPU；重建要從 6 個節點讀 |
| 資料在地性 | 有 | 大多沒有 —— 一個 block 被條帶化分散到多個節點 |
| 用於 | 熱資料、小檔案 | 冷資料 / 封存資料 |

<!--CODE-->

- 機架感知需要一個拓撲腳本（`net.topology.script.file.name`）；沒有的話，每個節點都在
  `/default-rack`。
- 新增節點後，用 **balancer**（`hdfs balancer`）讓各 DataNode 的磁碟用量平均。

<!-- 3b48870f356e -->
### 2-8) 小檔案問題 ⭐⭐⭐⭐

**每個檔案、目錄和 block 都是 NameNode 記憶體裡的一個物件（一般估計每個約 150 bytes），
所以數百萬個小檔會在磁碟滿之前就先耗盡 NameNode —— 而且每個小檔還要多花一個 map task。**

| 解法 | 怎麼做 |
|---|---|
| 寫大一點的檔案 | 在上游批次化或緩衝；在 Spark 寫入前先 coalesce |
| 壓實（compaction）job | 定期把一個分區的小檔重寫成少數幾個大檔 |
| 容器格式 | 把紀錄打包進 SequenceFile / Avro / Parquet / ORC |
| **HAR**（Hadoop Archive） | `hadoop archive` 把檔案打包成一個封存檔；減少 NameNode 物件，但讀取較慢且封存檔不可變 |
| `CombineFileInputFormat` | 讓一個 map task 讀很多小檔（改善運算，不改善 NameNode 記憶體） |
| 表格格式 | Iceberg / Delta / Hudi 的壓實（`rewrite_data_files`、`OPTIMIZE`） |

<!-- 05be83e00986 -->
### 2-9) 值得記住的指令

<!--CODE-->

---

<!-- 23f8a2b6fc7a -->
## 3) MapReduce ⭐⭐⭐⭐

<!-- b0a282a8112e -->
### 3-1) 從頭到尾的流程

**MapReduce 讀取 input split，把每筆紀錄 map 成 key-value 對，經過排序與 shuffle 讓同一個
key 的所有值都送到同一個 reducer，再把每個 key reduce 成輸出 —— 各階段之間的中間資料都
寫到本機磁碟。**

<!--CODE-->

<!-- a68ab0ba365d -->
### 3-2) Input split vs HDFS block

**Block 是 HDFS 實體存放資料的方式；input split 是 MapReduce 在邏輯上把輸入分給各 map task
的方式 —— 通常一個 split 對應一個 block，但兩者不是同一件事。**

- Split 大小 = `max(minSize, min(maxSize, blockSize))`，所以預設一個 split 等於一個 block。
- 跨越 block 邊界的紀錄仍會被完整讀到：文字檔的 `RecordReader` 會跳過第一行不完整的部分，
  並讀過自己 split 的結尾，把最後一行讀完。
- Map task 數 = split 數。Reduce task 數是設定出來的（`mapreduce.job.reduces`，預設 1）。
- 不可切分的輸入（單一個 gzip 檔）不論多大都只有**一個** map task。

<!-- ac808fe4ee4e -->
### 3-3) Map 端：緩衝區、partitioner、排序、spill、combiner

**Map 的輸出先進入一個環狀記憶體緩衝區；填到超過門檻時，依 reducer 分區、依 key 排序、
可選擇性地 combine，然後 spill 到本機磁碟，最後把多個 spill 合併成每個 map task 一個
排序好的檔案。**

| 步驟 | 細節 | 設定 |
|---|---|---|
| 緩衝區 | 每個 map task 一個環狀緩衝區 | `mapreduce.task.io.sort.mb`（預設 100 MB） |
| Spill 門檻 | 緩衝區滿到這個比例時，背景執行緒開始 spill | `mapreduce.map.sort.spill.percent`（0.80） |
| 分區 | 每個 key 給哪個 reducer —— 預設 `HashPartitioner` | 自訂 `Partitioner` 類別 |
| 排序 | 在每個分區內依 key 排序 | `SortComparator` |
| Combiner | 對每次 spill 做 map 端 reduce，減少 shuffle 量 | `job.setCombinerClass(...)` |
| 合併 | 把多個 spill 合併成一個分區且排序好的輸出檔 | `mapreduce.task.io.sort.factor` |

- 預設 partitioner 是 `(key.hashCode() & Integer.MAX_VALUE) % numReduceTasks`；傾斜的 key
  會把它所有的值都送到同一個 reducer。
- **Combiner** 必須在跑零次、一次或多次時都正確 —— sum、count、max 沒問題；average 就錯了
  （改成 combine `(sum, count)` 對）。

<!-- f3bd7abe578b -->
### 3-4) Shuffle 與 reduce 端

**每個 reducer 從每個已完成的 map task 複製屬於自己的分區，合併排序好的片段，然後對每個
key 呼叫一次 `reduce()`，傳入該 key 所有值的迭代器。**

- **複製階段** —— map 一完成 reducer 就開始抓（由
  `mapreduce.job.reduce.slowstart.completedmaps` 控制），透過 HTTP 從 NodeManager 的
  shuffle handler 取得。
- **合併階段** —— 抓來的片段在記憶體與磁碟上合併成一條排序好的串流。
- **Reduce 階段** —— 同一個 key 的值一起到達；輸出經由 `OutputFormat` 寫到 HDFS，
  每個 reducer 一個檔案。
- **Secondary sort（二次排序）** —— 想讓值也排序，就用複合 key、一個 partitioner，
  再加上依自然 key 的 grouping comparator。

<!-- b47cea35987d -->
### 3-5) 用 Java 寫 word count

<!--CODE-->

<!--CODE-->

<!-- 77dbf3926b7a -->
### 3-6) 容錯、speculative execution 與 counter

**失敗的 task 會在其他節點重試，慢的 task 可以被一個推測性副本拿來賽跑，只有當某個 task
用完所有嘗試次數時，整個 job 才會失敗。**

- **Task 失敗** —— 最多重試 `mapreduce.map.maxattempts` / `mapreduce.reduce.maxattempts` 次
  （預設 4）。Map 輸出存在本機磁碟，所以遺失一個節點代表它已完成的 map task 也要重跑。
- **ApplicationMaster 失敗** —— YARN 會重啟 AM（`mapreduce.am.max-attempts`），它可以從
  job history 復原已完成的 task。
- **Speculative execution（推測執行）** —— 當某個 task 比同儕慢很多時，在別處啟動一個
  重複嘗試；先完成的勝出，另一個被殺掉。它對壞掉的節點有幫助，**對資料傾斜沒用**
  （重複的那份有一樣過大的輸入），而且有副作用的 task 應該關掉它。
- **Counter（計數器）** —— 內建的（輸入/輸出紀錄數、spill 紀錄數、讀取 bytes）和自訂的
  （`context.getCounter("quality", "bad_rows").increment(1)`）；job 慢或結果錯時第一個該看的地方。

<!-- 90dffba1066b -->
### 3-7) MapReduce 的 join 模式

| 模式 | 怎麼做 | 何時 |
|---|---|---|
| **Reduce-side join** | mapper 給每筆紀錄標上來源；reducer 依 key 做 join | 兩邊都很大；一定可行；完整 shuffle |
| **Map-side（replicated）join** | 小表經由 distributed cache（`job.addCacheFile`）送到每個 mapper，載入成 hash map | 一邊放得進記憶體；沒有 shuffle |
| **Sorted merge join** | 兩邊輸入事先以相同方式排序與分區，在 map 中合併 | 輸入早已用同樣方式 bucket 好 |

這些就是 Spark 所稱的 sort-merge join 與 broadcast hash join —— 見
[`spark.md`](./spark.md)。

---

<!-- aecf5068bd2d -->
## 4) YARN ⭐⭐⭐⭐

<!-- cb4d98fbd587 -->
### 4-1) 元件

**YARN 把資源管理和應用程式邏輯分開：一個 ResourceManager 仲裁整個叢集，每個節點上的
NodeManager 執行 container，而每個應用程式自帶一個 ApplicationMaster。**

| 元件 | 角色 |
|---|---|
| **ResourceManager** | 叢集的權威：一個 **Scheduler**（分配資源）和一個 **ApplicationsManager**（接收 job、啟動與重啟 AM） |
| **NodeManager** | 每個節點一個；啟動 container、強制其資源上限、回報節點健康與用量 |
| **ApplicationMaster** | 每個應用程式一個；向 RM 協商 container，並告訴 NM 要跑什麼 |
| **Container** | 某個節點上的一份記憶體與 vcore 配額 |
| **Timeline / History server** | 應用程式結束後保留 log 與指標 |

- 在 Hadoop 1，**JobTracker** 同時負責排程與追蹤 job，而且只能跑 MapReduce；YARN
  （Hadoop 2）移除了這個瓶頸，讓 Spark、Tez、Flink 能共用叢集。
- RM 可以跑 HA（透過 ZooKeeper 做 active / standby）。

<!-- 34b8ec3218f5 -->
### 4-2) 一個應用程式的生命週期

<!--CODE-->

- Container 請求帶有**在地性**偏好（節點 → 機架 → 任意）；排程器可能稍微延遲，
  以取得本機的 container。
- 關鍵大小：`yarn.nodemanager.resource.memory-mb` / `cpu-vcores`（一個節點提供多少）、
  `yarn.scheduler.minimum-allocation-mb` / `maximum-allocation-mb`（container 的上下限）。
- 超過記憶體上限的 container 會被 NodeManager 殺掉 —— 就是 Spark 中經典的
  「Container killed by YARN for exceeding memory limits」錯誤，解法是調高 executor 的
  memory overhead。

<!-- 7ff009a50ff2 -->
### 4-3) 排程器

| 排程器 | 模型 | 用途 |
|---|---|---|
| **FIFO** | 一個佇列，先到先服務 | 只用於測試 |
| **Capacity**（Apache Hadoop 的預設） | 階層式佇列，每個佇列有保證的份額；閒置容量可以借用；preemption 會收回 | 有 SLA 的多團隊叢集 |
| **Fair** | 應用程式隨時間平均分享資源；佇列可加權 | 互動式的混合工作負載 |

- **Node labels（節點標籤）**把佇列綁到某一群節點（GPU 節點、大記憶體節點）。
- **Preemption（搶占）**殺掉超額佇列裡的 container，讓另一個佇列拿到它的保證份額。

---

<!-- 40b140e6b7dc -->
## 5) Hive ⭐⭐⭐⭐

<!-- fa30a938c008 -->
### 5-1) 架構與 Metastore

**Hive 在 HDFS 或物件儲存的檔案之上提供 SQL：它把 HiveQL 編譯成 Tez（或 MapReduce）job，
而它的 Metastore 存放的表格定義，生態系其他工具也都會讀。**

| 元件 | 角色 |
|---|---|
| **HiveServer2** | JDBC / ODBC 端點；client 用 `beeline` 連線 |
| **Driver / 編譯器** | 解析 HiveQL、規劃、最佳化（Calcite CBO），產生 DAG |
| **執行引擎** | **Tez**（Hive 2/3 起為預設）；MapReduce 已棄用；Hive-on-Spark 在 Hive 4 被移除 |
| **Metastore（HMS）** | 表格 schema、分區、位置、SerDe —— 存在 RDBMS（MySQL / PostgreSQL），透過 Thrift 提供服務 |
| **LLAP** | 可選的常駐 daemon，帶快取，用於互動式查詢 |

- **Schema-on-read** —— Hive 載入時不驗證資料；查詢時由 SerDe 解讀檔案。
- **Metastore 比作為查詢引擎的 Hive 活得更久**：Spark SQL、Trino、Impala、Flink 都把它當
  catalog，雲端 catalog（AWS Glue）也講它的 API。

<!-- bc5ede6ab6ec -->
### 5-2) Managed table vs external table

**刪除 managed table 會刪掉資料；刪除 external table 只刪中介資料 —— 其他工具也擁有的資料，
用 external table。**

| | Managed（內部表） | External（外部表） |
|---|---|---|
| 資料位置 | Hive warehouse 目錄 | 你在 `LOCATION` 指定的任何路徑 |
| `DROP TABLE` | 刪除中介資料**和**資料 | 只刪中介資料 |
| ACID | 在啟用 ACID 的 Hive 3 中，managed ORC 表是交易式的 | 不是交易式的 |
| 用於 | Hive 完全擁有的表 | 原始落地區、和 Spark / 其他 job 共用的資料 |

<!-- 554589c930bf -->
### 5-3) 分區與 bucket

**分區依某個欄位的值把表切成不同目錄，讓查詢可以跳過；bucket 依某個欄位把列雜湊到固定數量
的檔案，讓 join 與抽樣可以利用這個配置。**

| | 分區（Partitioning） | Bucketing |
|---|---|---|
| 配置 | 每個值一個目錄：`.../dt=2026-11-01/` | 每個分區 N 個檔案：`hash(col) % N` |
| 選擇 | 低基數、永遠會被過濾的欄位（日期） | 高基數的 join key（`user_id`） |
| 好處 | partition pruning | bucket map join、sort-merge bucket join、抽樣 |
| 陷阱 | 分區太多 → 小檔案 + Metastore 負擔 | 資料寫入後 bucket 數就固定了 |

- **靜態分區** —— 值寫在語句裡。**動態分區** —— 取自 `SELECT` 最後幾個欄位
  （`hive.exec.dynamic.partition.mode=nonstrict`）。
- 其他工具寫進分區目錄的檔案，在分區註冊之前是看不見的：`MSCK REPAIR TABLE t` 或
  `ALTER TABLE t ADD PARTITION`。

<!-- abffef49f546 -->
### 5-4) HiveQL 範例

<!--CODE-->

<!-- 0768ebef8220 -->
### 5-5) 讓 Hive 查詢變快

- **Partition pruning** —— 直接在分區欄位上過濾。
- **欄式格式** —— ORC / Parquet，搭配 predicate pushdown 與 column pruning。
- **Map join** —— `hive.auto.convert.join=true` 把小表 broadcast 出去，而不是 shuffle。
- **向量化** —— `hive.vectorized.execution.enabled=true` 以批次處理列。
- **CBO** —— `ANALYZE TABLE t COMPUTE STATISTICS FOR COLUMNS`，讓 Calcite 最佳化器能挑選
  join 順序。
- **傾斜** —— `hive.optimize.skewjoin=true`，或把熱點 key 另外處理。
- **Tez + LLAP** 取得互動式的延遲。

---

<!-- 50f4cfb0c3df -->
## 6) HBase ⭐⭐⭐

<!-- 4660164e3f3c -->
### 6-1) 資料模型

**HBase 是一個分散式、排序過的 map —— row key → column family → column qualifier →
timestamp → value —— 建在 HDFS 上，用來依 key 做隨機、即時的讀寫。**

<!--CODE-->

- 列**依 row key 排序**，所以依前綴做範圍掃描很便宜。
- Column family 要事先宣告並分開存放；qualifier 可以自由命名，空的 cell 不佔空間（稀疏）。
- 每個 cell 依 timestamp 保留多個**版本**。
- **單列原子性**；沒有多列交易、沒有次要索引、沒有 join、沒有 SQL
  （Apache Phoenix 加上一層 SQL）。

<!-- 1832f16ae86b -->
### 6-2) 架構

| 元件 | 角色 |
|---|---|
| **Region** | 一張表中一段連續的 row key 範圍；長太大就分裂 |
| **RegionServer** | 服務一組 region：讀、寫、flush、compaction |
| **HMaster** | 把 region 分派給 RegionServer，處理 DDL 與負載平衡（不在資料路徑上） |
| **ZooKeeper** | 追蹤存活的伺服器與 active master |
| **`hbase:meta`** | 把 row key 範圍對應到 region 與伺服器的表 |

Client 透過 ZooKeeper 找到 `hbase:meta`，查出自己 key 所在的 region，快取那個位置，
之後就直接和 RegionServer 溝通。

<!-- 5366ccb232a0 -->
### 6-3) 寫入與讀取路徑

**寫入先到 write-ahead log，再到記憶體中的 MemStore，MemStore 會 flush 成 HDFS 上不可變的
HFile；讀取則合併 MemStore、block cache 與 HFile。**

<!--CODE-->

- 這是 **LSM-tree** 設計：快速的循序寫入，讀取可能要碰好幾個檔案 —— 和 Cassandra、RocksDB
  同一家族。
- 刪除會寫入一個 **tombstone**；資料在 major compaction 時才被移除。

<!-- 0c66bd414fad -->
### 6-4) Row key 設計 ⭐⭐⭐⭐

**Row key 是唯一的索引，所以要從存取模式來設計 —— 而且絕對不要讓它單調遞增，否則每一筆
寫入都會落在同一個 region。**

| 問題 | 解法 |
|---|---|
| 以時間戳或序號開頭 → 一個熱點 region | **加鹽**（`hash(id) % N` 前綴）或把 key **雜湊** |
| 需要最新的排前面 | 附加反轉的時間戳：`Long.MAX_VALUE - ts` |
| 需要依使用者掃描 | 把 `user_id` 放前面：`<salt>#<user_id>#<reversed_ts>` |
| Key 太長浪費空間 | 每個 cell 都會存一份 key —— 保持簡短 |
| 建表時就有 region 熱點 | 依預期的 key 範圍**預先分裂（pre-split）**表格 |

加鹽能分散寫入，但會把一次前綴掃描變成 N 個平行掃描 —— 這就是要說出來的取捨。

---

<!-- 1f431a83cc0d -->
## 7) ZooKeeper 與生態系的其他成員 ⭐⭐⭐

<!-- b33661b9d894 -->
### 7-1) ZooKeeper

**ZooKeeper 是一個小型、強一致、有副本的儲存，用來放協調用的資料 —— leader 選舉、鎖、
設定與成員資格 —— 不是用來放應用程式資料。**

- 2f + 1 台伺服器組成的 **ensemble** 可容忍 f 台故障；寫入經過 leader，並以 **ZAB** 協定
  由多數節點提交。
- 資料存在 **znode**：persistent（持久）、**ephemeral**（client 的 session 結束時就刪除 ——
  故障偵測的基礎），以及 **sequential**（帶計數器後綴 —— 鎖與 leader 選舉的基礎）。
- **Watch** 會在 znode 變更時通知 client 一次。
- 被 HDFS HA（ZKFC）、YARN RM HA、HBase、Hive 的鎖，以及 KRaft 之前的 Kafka 使用。

<!-- a3a600be060b -->
### 7-2) 資料擷取、工作流與查詢工具

| 工具 | 做什麼 | 現況 / 現代對應 |
|---|---|---|
| **Sqoop** | 以 map-only job 在 RDBMS 與 HDFS / Hive 之間大量匯入/匯出 | 2021 年移入 Apache Attic 退役 → Spark JDBC、Debezium CDC、託管的 connector |
| **Flume** | 以 agent 收集 log 進 HDFS（source → channel → sink） | Kafka + Kafka Connect、Fluent Bit |
| **Oozie** | XML 工作流（DAG）與 coordinator（時間 / 資料觸發）排程器 | Airflow、Dagster |
| **Pig** | 編譯成 MR / Tez 的 Pig Latin 資料流腳本 | Spark |
| **Impala** | 有常駐 daemon 的 MPP SQL 引擎，讀 Hive Metastore | Trino、Spark SQL、雲端資料倉儲 |
| **Presto / Trino** | 跨多種來源（包括 Hive 表）的分散式 SQL | 仍是現役 |
| **Ranger / Atlas** | 細粒度授權 / 中介資料血緣 | 雲端 IAM + catalog（Unity Catalog、Lake Formation） |
| **Kerberos** | 叢集身分驗證 | 安全的 Hadoop 叢集至今仍以它驗證身分 |

---

<!-- 284681d8631c -->
## 8) 檔案格式與壓縮 ⭐⭐⭐⭐

**分析用欄式格式（ORC 或 Parquet），擷取與訊息用列式格式（Avro），選壓縮時要清楚檔案是否
還能切分。**

| 格式 | 結構 | Schema | 可切分 | 最適合 |
|---|---|---|---|---|
| Text / CSV | 列式 | 沒有 | 可以（未壓縮時） | 資料交換、除錯 |
| **SequenceFile** | 列式、二進位 key-value | Writable 型別 | 可以 | MapReduce 中間資料、打包小檔 |
| **Avro** | 列式、二進位 | 內嵌 JSON schema | 可以 | 擷取、Kafka、schema 演進 |
| **ORC** | 欄式、stripe | 內嵌 | 可以 | Hive、ACID 表 |
| **Parquet** | 欄式、row group | 內嵌 | 可以 | Spark、Trino、大多數引擎 |

| 壓縮格式 | 本身可切分 | 備註 |
|---|---|---|
| gzip | 不行 | 壓縮率好；一個大 `.gz` 檔就是一個 map task |
| bzip2 | 可以 | 壓縮率最好，很慢 |
| Snappy / LZ4 | 不行 | 很快；放在容器格式**內部**（Parquet / ORC / Avro / SequenceFile）就沒問題，它們仍可切分 |
| LZO | 有索引才行 | 需要另外的建索引步驟 |
| Zstandard | 不行（裸檔） | 速度不錯且壓縮率高；常用在 Parquet / ORC 內部 |

欄式格式在分析上勝出，是因為查詢只讀它選的欄位、相近的值放在一起壓縮效果好，而且每個
row group 或 stripe 的 min/max 統計讓讀取端可以整段跳過資料。

---

<!-- 85418a7a939f -->
## 9) 從 Hadoop 到雲端與 Lakehouse ⭐⭐⭐⭐

<!-- c7f225e03ab2 -->
### 9-1) 儲存與運算分離

**現代技術棧用物件儲存取代 HDFS，用彈性運算取代常駐叢集，讓儲存與運算各自擴展、各自付費。**

| | Hadoop 叢集上的 HDFS | 物件儲存（S3、GCS、ADLS） |
|---|---|---|
| 擴展 | 加節點 = 儲存 + 運算一起加 | 儲存無上限；運算分開 |
| 成本 | 常駐節點、3 份副本 | 依儲存 GB 數付費；耐久性由供應商管理 |
| 在地性 | 資料在本機的 task | 沒有 —— 資料永遠經過網路 |
| 改名（rename） | 原子的中介資料操作 | S3：複製 + 刪除，不是原子的（GCS / ADLS Gen2 不同） |
| 列目錄 | 很快，來自 NameNode 記憶體 | 較慢，分頁的 API 呼叫 |

- 因為 S3 的 rename 不是原子的，經典的「先寫到暫存目錄、提交時 rename」output committer
  在那裡又慢又不安全；Hadoop 的 **S3A committer**（magic / staging）與表格格式解決了這個問題。
- **託管 Hadoop**（EMR、Dataproc、HDInsight）在物件儲存之上按需執行 YARN、Spark 與 Hive。
- **開放表格格式** —— Iceberg、Delta Lake、Hudi —— 在 Parquet 檔案上加一份交易 log，提供
  ACID 提交、`MERGE`、schema 演進、time travel 與壓實。再配上一個 catalog（Hive Metastore、
  Glue、Unity、Polaris），就是 **lakehouse**。

<!-- b45a12943e5e -->
### 9-2) Hadoop MapReduce vs Spark

**Spark 取代 MapReduce 成為運算引擎，是因為它在整個 DAG 中把中間資料留在記憶體，而不是
每一步之間都寫磁碟 —— 但 Spark 仍常常跑在 YARN 上、從 HDFS 讀資料。**

| | MapReduce | Spark |
|---|---|---|
| 模型 | 每個 job 兩個固定階段；要更多就串接 job | 任意由 stage 組成的 DAG |
| 中間資料 | map 與 reduce 之間寫本機磁碟，job 之間寫 HDFS | 盡量放記憶體，spill / shuffle 時才寫磁碟 |
| 迭代工作（ML、圖） | 每次迭代一個 job，重新讀輸入 | 跨迭代快取資料集 |
| API | Java map / reduce | SQL、DataFrame、Python、Scala、串流、MLlib |
| 容錯 | 重跑失敗的 task；輸出在磁碟上 | 依 lineage 重新計算 |
| 仍然適合 | 在既有叢集上非常大、簡單、受磁碟限制的批次 job | 幾乎其他所有情況 |

---

<!-- b101ba94c439 -->
## 10) 面試問答 ⭐⭐⭐⭐

- **為什麼 HDFS 的 block 這麼大？** 讓尋道時間相對於傳輸時間變小，並讓 block 的數量 ——
  也就是 NameNode 記憶體 —— 保持低。
- **NameNode 掛掉會怎樣？** 沒有 HA 時，HDFS 無法使用，直到它從 fsimage + edits 重啟。
  有 HA 時，ZKFC 會 failover 到 standby，而 standby 早已有命名空間與 block 位置。
- **Secondary NameNode 是備援嗎？** 不是。它只負責把 edit log checkpoint 進 fsimage；
  它不能服務 client。
- **HDFS 怎麼偵測損壞的資料？** 寫入與讀取時每個 chunk 都有 checksum，加上 DataNode 定期的
  block scanner；損壞的副本會用好的副本取代。
- **Block vs input split？** 實體儲存單位 vs 一個 map task 的邏輯單位；通常對齊，但不是同一件事。
- **為什麼 combiner 不能直接算平均？** 它可能跑任意次；平均的平均是錯的。輸出 `(sum, count)`，
  在 reducer 裡再相除。
- **MapReduce 裡怎麼處理傾斜的 key？** 自訂 partitioner、在第一個 job 對 key 加鹽並在第二個
  job 合併，或者另一邊很小時用 map-side join。Speculative execution 沒有幫助。
- **Managed vs external Hive 表？** Drop 時刪資料 vs 只刪中介資料。
- **為什麼小檔案不好？** 每個物件都佔 NameNode 記憶體，加上每個檔案一個 map task。
- **什麼時候用 HBase 而不是 Hive？** 依 key 的低延遲隨機讀寫（使用者檔案、計數器、依實體的
  時間序列）vs 大型分析掃描。
- **YARN 比 Hadoop 1 多了什麼？** 它把 JobTracker 拆成一個 ResourceManager 和每個應用程式
  各自的 ApplicationMaster，所以叢集能擴展得更大，也能跑 MapReduce 以外的引擎。
- **Hadoop 死了嗎？** HDFS 叢集與 MapReduce 在萎縮，但它的概念與零件無所不在：YARN 仍在跑
  Spark，Hive Metastore 仍是共通的 catalog，Parquet 與 ORC 出自那個生態系，而物件儲存加上
  表格格式就是它的繼承者。
