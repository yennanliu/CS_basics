<!-- 7cae1d80c413 -->
# 串流資料管線 — 面試深入解析

> **範圍** — 資料工程師面試會問到的串流處理全貌：何時該用串流、參考架構、時間語意與 watermark、視窗與遲到資料、狀態與 checkpoint、端到端 exactly-once、串流 join、背壓與擴展、各引擎（Flink、Spark Structured Streaming、Kafka Streams、Beam）、即時服務與維運。Kafka broker 內部機制不在這裡 —— 它們屬於 `kafka.md`。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單；[`./kafka.md`](./kafka.md) —— 大多數串流底下的那份 log；[`./batch_pipeline.md`](./batch_pipeline.md) —— 同一條管線的批次那一側；[`./spark.md`](./spark.md) —— Spark 內部機制；[`./flink.md`](./flink.md) —— Flink 的 runtime、API 與設定的深入內容；[`../flink/faq_flink.md`](../flink/faq_flink.md) 與 [`../stream/faq_stream.md`](../stream/faq_stream.md) —— 較早的筆記。

每一節都先給你會說出口的那句話，再講機制，然後是面試官下一步會追問的取捨。
程式碼以 **Flink 1.19+ / 2.x**、**Spark 3.5+** 與 **Kafka Streams 3.x** 為準；版本有差的地方會特別註明。

---

<!-- c9d1637bf94c -->
## 1) 批次 vs 串流 ⭐⭐⭐⭐

<!-- 42b417426107 -->
### 1-1) 什麼時候值得用串流

**只有當有人會根據結果採取行動、而且需要比批次更快拿到時，才用串流 —— 串流買到的是
延遲，付出的代價是永遠在線的基礎設施與更難的正確性。**

| | 批次 | 串流 |
|---|---|---|
| 輸入 | 有界（一天、一小時） | 無界 |
| 延遲 | 分鐘–小時 | 毫秒–秒 |
| 完整性 | job 執行時所有資料都在 | 資料遲到、亂序到達 |
| 狀態 | 每次執行重建 | 在 job 的整個生命週期內保留 |
| 重新處理 | 重跑該 partition | 把 log 重播進新的 job |
| 故障處理 | 重試 task | checkpoint、重啟、處理重複資料 |
| 成本 | 按次付費 | 為永遠在線、有狀態的 worker 付費 |

好的串流理由：詐欺與濫用偵測、告警與監控、即時營運 dashboard、個人化特徵、
系統之間的 CDC 複製。
薄弱的理由：「日報應該要即時」，但根本沒人一天看超過一次 —— 每小時一次的批次
更便宜也更簡單。

**面試訊號**：選擇之前先問「誰會讀這個結果，它可以多舊？」

<!-- 75deb2317bf3 -->
### 1-2) Micro-batch vs 逐筆處理

**Micro-batch 引擎把一小批紀錄收集起來，對它跑一個迷你批次 job；逐筆（record-at-a-time）
引擎則在每筆紀錄到達時就把它推過各個運算子。**

| | Micro-batch | 逐筆處理 |
|---|---|---|
| 例子 | Spark Structured Streaming（預設 trigger） | Flink、Kafka Streams、Dataflow |
| 典型延遲 | 約 100 ms 到數秒，下限由 trigger 決定 | 毫秒 |
| 吞吐量 | 高 —— 批次開銷被攤提 | 高 |
| 容錯 | 重播失敗的 micro-batch | 分散式快照（checkpoint） |
| 心智模型 | 反覆執行的批次查詢 | 永不停止的資料流圖 |

Spark 也有 **continuous processing** 模式，但它仍標示為實驗性，而且只支援類似 map
的運算 —— 不要把它當成低延遲的答案。

<!-- ac5aa0ba49e4 -->
### 1-3) Lambda vs Kappa 架構

**Lambda 用一個批次層負責準確、一個速度層負責新鮮度，再把兩者合併；Kappa 只跑串流，
重新處理時就重播 log。**

<!--CODE-->

| | Lambda | Kappa |
|---|---|---|
| 程式碼 | 兩套（批次 + 串流），必須一致 | 一套 |
| 重新處理 | 重跑批次層 | 把 log 重播進新版本的 job，再切換過去 |
| 需要 | 批次儲存 + 串流基礎設施 | 長的 log retention（或分層儲存 / 封存） |
| 弱點 | 兩條路徑之間的邏輯漂移 | 用串流 job 重播好幾個月的資料很慢 |

現代做法偏向 Kappa：一個能用同一份邏輯跑批次與串流的引擎（Flink、Spark、Beam），
原始串流封存在物件儲存或 lakehouse 表裡，批次回補用同一份程式碼讀那份封存。

---

<!-- d8288aa288b5 -->
## 2) 參考架構 ⭐⭐⭐⭐

<!-- ea25b9804af4 -->
### 2-1) 標準的管線

**來源把事件寫進一份持久的 log，有狀態的處理器把它們轉成結果，sink 再把結果提供給
dashboard、服務或 lakehouse。**

<!--CODE-->

各個元件以及它們存在的理由：

- **持久 log**（Kafka、Kinesis、Pub/Sub）—— 讓 producer 與 consumer 解耦，並讓來源
  **可以重播**，每一個復原與重新處理的方案都依賴這一點。
- **Schema Registry** —— producer 與 consumer 對事件的形狀達成一致；見 `schema_design.md`。
- **串流處理器** —— 過濾、補充（enrichment）、彙總、join；持有狀態。
- **衍生 topic** —— 其他團隊消費的結果，所以一個 job 的輸出就是另一個 job 的輸入。
- **Sink** —— 依存取模式選擇：依 key 查找 → key-value 儲存，任意切片的彙總 → 即時
  OLAP，歷史與分析 → lakehouse 表。

<!-- bc3257207449 -->
### 2-2) 以 CDC 作為來源

**變更資料擷取（CDC）讀取資料庫自己的變更 log（MySQL binlog、PostgreSQL WAL），把每一次
insert、update、delete 轉成事件，而不必查詢資料表。**

- 常見答案是跑在 Kafka Connect 上的 **Debezium**；每張表變成一個以主鍵為 key 的 topic，
  帶有 `before` / `after` 映像與一個 `op` 欄位（`c`、`u`、`d`，快照則是 `r`）。
- 它先做一次初始**快照**，再從它記錄下的 log 位置開始串流變更。
- 刪除會先送出一個 delete 事件，再跟著一個 **tombstone**（值為 null），讓 compacted
  topic 可以丟掉那個 key。
- **Outbox 模式** —— 服務在同一個本地交易中寫入業務資料列和一筆 `outbox` 資料列；
  CDC 負責發佈 outbox。它避免了雙寫問題（DB 提交成功但 Kafka 發佈失敗，或反過來）。

<!-- e80524d83686 -->
### 2-3) 選擇 sink

| 需求 | Sink | 為什麼 |
|---|---|---|
| 依 key 查找（feature store、使用者檔案） | Redis、Cassandra、DynamoDB | 依 key 低延遲讀取；以 upsert 寫入結果 |
| 次秒級、任意切片的 dashboard | Pinot、Druid、ClickHouse | 欄式，直接從 Kafka 擷取 |
| 歷史、分析、ML 訓練 | Iceberg / Delta / Hudi 表 | 便宜的儲存、SQL 引擎、time travel |
| 搜尋事件 | Elasticsearch / OpenSearch | 倒排索引 |
| 其他串流 job | 一個 Kafka topic | 可重播、fan-out |

Sink 決定你能提供的投遞保證（第 6 節）：依 key upsert 的 sink 能讓 at-least-once
看起來像 exactly-once；只能 append 的 sink 則不行。

---

<!-- ff6e9054272c -->
## 3) 時間語意與 Watermark ⭐⭐⭐⭐⭐

<!-- 1173c91634c4 -->
### 3-1) 事件時間、擷取時間、處理時間

**事件時間是事情發生的時間；處理時間是引擎看到它的時間。正確的彙總要用事件時間，
因為處理時間每次重播都會變。**

| 時間 | 由誰設定 | 重播時是否確定？ | 用途 |
|---|---|---|---|
| **事件時間（Event time）** | producer，寫在紀錄裡 | 是 | 業務指標、視窗、join |
| **擷取時間（Ingestion time）** | 到達時由 broker / 來源設定 | 否 | 事件本身不帶時間戳時 |
| **處理時間（Processing time）** | 運算子的牆鐘時間 | 否 | 逾時、監控、「最近 N 秒的流量」 |

事件時間與處理時間之間的差距（**skew**）從來不是常數：行動裝置會離線、某個 partition
會落後、producer 會重試。這正是 watermark 存在的全部理由。

<!-- b00ace911770 -->
### 3-2) Watermark

**Watermark 是串流中的一個標記，意思是「不會再有時間戳 ≤ T 的事件了」；當 watermark
越過事件時間視窗與計時器時，視窗才會關閉、計時器才會觸發。**

常見策略是 **bounded out-of-orderness（有界亂序）**：watermark = 目前看到的最大事件時間 −
一個固定的上限。每個平行的來源實例各自送出 watermark，而有多個輸入的運算子取它們的
**最小值**。

<!--CODE-->

| 上限設太小 | 上限設太大 |
|---|---|
| 更多事件落在 watermark 之後，被算成遲到 | 視窗更晚關閉，所以結果延遲 |
| 延遲較低、狀態較少 | 結果較完整、保留的狀態較多 |

Watermark 是一種**啟發式估計**，不是保證：它用完整性換延遲，而落在它之後的一切
都需要一個遲到資料策略（第 4-3 節）。

<!-- e991b274e021 -->
### 3-3) 閒置來源與 watermark 對齊

**因為運算子取輸入 watermark 的最小值，一個停止送資料的 partition 就會凍結整個 job 的
watermark —— 所有視窗都不再關閉。**

- **Idleness（閒置）** —— `withIdleness(...)` 在安靜一段時間後把該 split 標成閒置，
  讓它在資料再次到達前不參與最小值計算。
- **Watermark alignment（對齊）**（Flink 1.15+，`withWatermarkAlignment`）—— 處理相反的
  問題：某個來源遠遠*領先*其他來源，讓下游運算子必須把它的資料緩衝在狀態裡。對齊會暫停
  那些領先太多的來源。
- 一個 partition 數多於活躍 key 數的 topic，或一個讀某個 partition 比另一個快的回補，
  都會同時觸發這兩個問題；在 Flink UI 檢查每個 subtask 的 watermark。

---

<!-- 8a89beaab102 -->
## 4) 視窗、Trigger 與遲到資料 ⭐⭐⭐⭐⭐

<!-- cb4a079a5601 -->
### 4-1) 視窗類型

**視窗把無限的串流切成可以彙總的有限區塊；類型決定這些區塊是固定的、重疊的，
還是由活動的間隔來定義。**

| 視窗 | 定義 | 每個事件落在 | 例子 |
|---|---|---|---|
| **Tumbling（滾動）** | 固定大小、不重疊 | 剛好一個視窗 | 每城市每分鐘的行程數 |
| **Sliding / hopping（滑動）** | 固定大小，以小於大小的 slide 前進 | size ÷ slide 個視窗 | 每分鐘計算一次 10 分鐘移動計數 |
| **Session（會話）** | 每個 key 在一段閒置間隔後關閉 | 一個 session（session 會合併） | 使用者的造訪，直到閒置 30 分鐘 |
| **Global（全域）** | 所有時間共用一個視窗 | 那唯一的視窗 | 需要自訂 trigger 才會輸出 |
| **Cumulative（累積）**（Flink SQL `CUMULATE`） | 在最大大小內分步成長 | 直到最大值的每一步 | 今天到目前為止的營收，每分鐘更新 |

<!--CODE-->

優先用增量的 `aggregate` / `reduce`，而不是會緩衝每個事件的 `process` 視窗函式：
每個視窗的狀態就從「所有事件」降為「一個累加器」。

<!-- 432d047004ec -->
### 4-2) Trigger：視窗何時輸出

**Trigger 決定視窗的結果何時輸出；預設的事件時間 trigger 只觸發一次，就在 watermark
越過視窗結束時間的時候。**

- **提早觸發（early firing）** —— 在視窗關閉前輸出推測性的結果（每 10 秒處理時間一次），
  讓 dashboard 不會在一小時長的視窗裡整段空白。
- **延後觸發（late firing）** —— 在允許的遲到時間內，每個遲到事件都輸出一次更新後的結果。
- 第一次之後的每次觸發都是同一個視窗的**修正（refinement）**，所以 sink 必須能接受它：
  依 `(key, window_start)` 覆寫，或消費 retraction 串流。
- Beam 把這幾個問題講得很明白：計算*什麼*（轉換）、在事件時間的*哪裡*（視窗）、
  在處理時間的*何時*（trigger），以及修正之間*如何*關聯（accumulating vs discarding panes）。

<!-- 2ae8cbb8e1d7 -->
### 4-3) 遲到資料策略

**遲到事件是指在 watermark 已經越過其視窗之後才到達的事件；你可以丟掉它、把它導到
旁邊，或更新已經輸出的結果。**

| 策略 | 怎麼做 | 成本 |
|---|---|---|
| **丟棄** | 視窗關閉後的預設行為 | 默默少算；至少要計算丟了多少 |
| **Allowed lateness（允許遲到）** | 視窗狀態多保留一段時間，每個遲到事件都重新觸發 | 更多狀態；下游會看到更新 |
| **Side output（側輸出）** | 把遲到事件送到另一個串流 / 表 | 之後由批次 job 對帳 |
| **下游 upsert / retract** | 以視窗為 key 輸出結果；後來的結果覆寫前面的 | sink 必須支援 upsert |
| **用批次修正** | 串流給快速估計，對封存資料跑的批次 job 給最終數字 | 兩條路徑，就像 Lambda |

<!--CODE-->

---

<!-- f1763e583750 -->
## 5) 狀態 ⭐⭐⭐⭐

<!-- eb71bff33c40 -->
### 5-1) Keyed state vs operator state

**Keyed state 依串流的 key 分區，只有帶著那個 key 的紀錄看得到；operator state 屬於
一個平行實例 —— 來源已讀到的 Kafka offset 就是經典例子。**

| | Keyed state | Operator state |
|---|---|---|
| 範圍 | `keyBy` 之後，每個 key 一個值 | 每個平行 subtask 一個值 |
| 類型（Flink） | `ValueState`、`ListState`、`MapState`、`ReducingState`、`AggregatingState` | `ListState`、union list state、broadcast state |
| 擴縮時如何重新分配 | 依 key group（第 8-2 節） | 切分 / 合併那些 list |
| 例子 | 每個使用者的累計總額、每台裝置最後出現的時間 | 來源 offset、緩衝中的 sink 交易 |

<!--CODE-->

<!-- 19bc3e0d602b -->
### 5-2) State backend

**State backend 決定即時狀態放在哪裡、怎麼做快照：放在 JVM heap 求快，或放在本機磁碟上
的嵌入式 RocksDB 以容納比記憶體更大的狀態。**

| Backend | 存放於 | 適合 | 注意 |
|---|---|---|---|
| Flink `HashMapStateBackend` | JVM heap，以物件形式 | 小狀態、最低延遲 | GC 停頓，狀態受限於 heap |
| Flink `EmbeddedRocksDBStateBackend` | 本機磁碟上的 RocksDB | 大狀態、**增量** checkpoint | 每次存取都要序列化；要調 RocksDB 記憶體 |
| Flink 2.0 ForSt | 分離式、遠端儲存 | 非常大的狀態、更快的擴縮 | 較新 —— 確認你版本的成熟度 |
| Spark 預設 state store | executor 記憶體，版本化檔案存在 checkpoint 目錄 | 小到中等的狀態 | key 很多時有記憶體壓力 |
| Spark RocksDB state store（3.2+） | 本機磁碟上的 RocksDB | 大狀態 | 設定 `spark.sql.streaming.stateStore.providerClass` |
| Kafka Streams | 本機 RocksDB + 一個 changelog topic | 每個應用程式實例的狀態 | 還原時間 = 重播 changelog |

不論即時狀態放在哪個 backend，checkpoint 本身都寫到持久儲存（S3、HDFS、GCS）。

<!-- 62c302891231 -->
### 5-3) 狀態 TTL 與狀態大小

**除非有東西把它移除，串流狀態會無限成長：視窗關閉、watermark 越過，或明確的 TTL。**

- **無界的 key** —— 沒有視窗的 `GROUP BY user_id` 會為每個出現過的使用者保留一筆。
  設定 TTL（`StateTtlConfig`、Flink SQL `table.exec.state.ttl`）或把彙總加上視窗。
- 沒有時間上限的**串流-串流 join** 會永遠保留兩邊（第 7-1 節）。
- **去重集合**需要一個時間範圍：「丟掉 24 小時內看過的重複」，而不是「永遠」。
- 狀態成長的徵兆：checkpoint 大小與時間持續上升、RocksDB 磁碟逐漸塞滿、
  每週復原都變得更慢。
- TTL 是正確性的決定，不只是維運的決定：過期的 key 表現得像新的一樣，所以超過 TTL
  後回來的使用者會被當成新使用者。

---

<!-- 930632e8ada1 -->
## 6) 容錯與 Exactly-Once ⭐⭐⭐⭐⭐

<!-- cc6152822738 -->
### 6-1) Checkpoint：分散式快照

**Checkpoint 是每個運算子狀態加上其對應來源 offset 的一致性快照；故障時，job 還原最後
一個 checkpoint，並從那些 offset 重播來源。**

Flink 實作的是 **Chandy-Lamport** 演算法的一種變體（asynchronous barrier snapshotting，
非同步屏障快照）：

<!--CODE-->

Barrier n 之前的一切都在快照裡；之後的一切都會被重播。這就是狀態一致的原因：沒有任何
事件在狀態中被算兩次，也沒有任何事件從狀態中遺失。

<!--CODE-->

<!-- 3676ad8f7d3a -->
### 6-2) 對齊 vs 非對齊 checkpoint

**使用對齊（aligned）checkpoint 時，有多個輸入的運算子要等每個輸入都收到 barrier 才做
快照；在背壓下這段等待可能讓 checkpoint 卡很久，非對齊（unaligned）checkpoint 就是為了
避免這點。**

| | 對齊 | 非對齊（Flink 1.11+） |
|---|---|---|
| 機制 | 已送來 barrier 的輸入先被擋住，直到所有輸入都送來 | barrier 超車緩衝中的資料；傳輸中的緩衝一起放進 checkpoint |
| 背壓下 | checkpoint 時間隨積壓量成長 | checkpoint 很快完成 |
| Checkpoint 大小 | 只有狀態 | 狀態 + 傳輸中的資料 |
| 模式 | exactly-once 與 at-least-once | 只有 exactly-once |

At-least-once checkpoint 完全跳過對齊：在其輸入的 barrier 之後才到的紀錄可能被處理、
然後又被重播，所以可能被算兩次。

<!-- 3b735f07ab73 -->
### 6-3) Checkpoint vs savepoint

**Checkpoint 是自動的，用來從故障中復原；savepoint 由你觸發，用來在不遺失狀態的情況下
停止、升級、擴縮或遷移 job。**

| | Checkpoint | Savepoint |
|---|---|---|
| 由誰觸發 | Flink，週期性 | 維運者（`flink savepoint`、`flink stop --savepointPath`） |
| 用途 | 故障復原 | 計畫性升級、擴縮、Flink 版本升級、job 版本 A/B |
| 格式 | 可能是增量的、依 backend 而定 | canonical（可跨 backend 移植）或 native |
| 生命週期 | 由 Flink 管理，舊的會被清掉 | 由你管理，保留到你刪除為止 |

給每個有狀態的運算子一個穩定的 `.uid("...")`：savepoint 的狀態是依 id 對應到運算子的，
而自動產生的 id 在 job 圖改變時就會變。

<!-- 38b52e9b8212 -->
### 6-4) 端到端 exactly-once ⭐⭐⭐⭐⭐

**串流引擎裡的「exactly-once」指的是每個事件只影響*狀態*一次；要在*輸出*也做到，需要
三樣東西：可重播的來源、一致的 checkpoint，以及支援交易或冪等的 sink。**

| 元件 | 要求 | 例子 |
|---|---|---|
| 來源 | 能倒回到記錄下的位置 | Kafka offset、Kinesis sequence number、檔案 |
| 引擎 | 狀態 + offset 原子地一起快照 | Flink checkpoint、Spark checkpoint + offset 的 WAL |
| Sink | 重播的輸出不會重複 | Kafka transaction、兩階段提交、每批一次的 Delta / Iceberg commit、依 key upsert |

- **交易式 sink** —— 輸出先暫存，只有在 checkpoint 提交時才變得可見。下游讀取者必須
  只讀已提交的資料（Kafka `isolation.level=read_committed`）。
  代價：輸出每個 checkpoint 才可見一次，所以延遲 ≥ checkpoint 間隔。
- **冪等 sink** —— 重播時以相同的值寫入相同的 key（依 `(key, window_start)` upsert、
  確定性的 id）。延遲較低，但讀取者可能短暫看到一個之後會被以相同值重寫的結果。
- Sink 以外的副作用（寄信、呼叫 API）永遠不在保證範圍內 —— 用去重 key 讓它們冪等。
- 沒有交易式或冪等的 sink 時，誠實的描述是 **at-least-once**。

<!-- f6d42f5bf531 -->
### 6-5) 兩階段提交 sink

**兩階段提交 sink 每個 checkpoint 開一個交易，barrier 到達時預提交（pre-commit），
只有在整個 checkpoint 成功後才正式提交。**

<!--CODE-->

<!--CODE-->

陷阱：producer 的 `transaction.timeout.ms` 必須涵蓋最長的 checkpoint 加上重啟時間，否則
broker 會中止一個已預提交的交易，其資料就遺失了；而且它不能超過 broker 的
`transaction.max.timeout.ms`（預設 15 分鐘）。

---

<!-- 21650adafb01 -->
## 7) 串流 Join 與資料補充 ⭐⭐⭐⭐

<!-- d8f61a48b29d -->
### 7-1) 串流-串流 join

**Join 兩個無界串流需要一個時間上限 ——「付款在訂單後 15 分鐘內到達」—— 引擎才知道
什麼時候可以忘掉它正在緩衝的資料列。**

| Join | 條件 | 保留的狀態 |
|---|---|---|
| **Interval join（區間 join）** | `b.ts BETWEEN a.ts - x AND a.ts + y` | 每一邊保留該區間，由 watermark 清除 |
| **Window join（視窗 join）** | 兩個事件在同一個視窗 | 每個視窗 |
| **一般（無界）join** | 只有 key 相等 | 兩邊永遠保留 —— 需要 TTL |

<!--CODE-->

Outer join 多了一個問題：什麼時候輸出「沒有付款的訂單」？只有在 watermark 越過區間結束
之後，所以 outer join 中未匹配的資料列會延遲「區間長度 + watermark 上限」。基於同樣的理由，
Spark 的 outer 串流-串流 join 要求兩邊都有 watermark 並帶時間條件。

<!-- f4342de35472 -->
### 7-2) 串流-表格資料補充

**資料補充（enrichment）是為每個事件加上維度屬性（使用者等級、商家類別）；你可以在外部
儲存查找，或把那張表當成 job 內的狀態保存。**

| 做法 | 怎麼做 | 取捨 |
|---|---|---|
| **每個事件查找一次** | 每筆紀錄查詢一次 DB / 快取（Flink async I/O、lookup join） | 簡單；有外部延遲與負載，要用 async I/O 加快取 |
| **表格當狀態** | 把該表的 CDC 串流消費進 keyed state，在本地 join | 快、沒有外部呼叫；狀態裝著整張表 |
| **Broadcast** | 小表送到每個平行實例（7-4） | 最快；只適合小表 |
| **Kafka Streams KStream-KTable join** | 從 compacted topic 物化出來的 KTable | 和表格當狀態一樣，依 key 分區 |

Lookup join 看到的是**處理時間當下**的表，所以重播時會用今天的屬性去補充舊事件；
temporal join（7-3）解決這個問題。

<!-- 9fcb9d4843a7 -->
### 7-3) Temporal join

**Temporal join 把每個事件 join 到在事件時間當下有效的那個表格版本 —— 等同於串流版的
SCD Type 2 查找。**

<!--CODE-->

重播會得到相同的答案，因為查找是以事件時間為準，而不是 join 執行的那一刻。

<!-- b71af7a38dc1 -->
### 7-4) Broadcast state

**Broadcast state 把一個小而變化緩慢的串流 —— 規則、門檻、功能開關 —— 送到每個平行實例，
讓每一個都能把它套用到自己那一份主串流上。**

- 典型用法：詐欺 job 的規則在執行期更新，不需要重新部署。
- 每個實例都持有完整一份，所以只適合小資料。
- Broadcast 那一側在每個實例上必須以完全相同的方式處理；每個 key 的邏輯留在 keyed 那一側。

<!-- 32c993800393 -->
### 7-5) 去重

**Producer 會重試、來源會重播，所以重複資料是常態；在一個有界的時間範圍內，依唯一的
事件 id 去重。**

<!--CODE-->

<!--CODE-->

- id 必須**在 producer 端**指定 —— 擷取之後才產生的 id 每次重播都不一樣。
- 限制狀態大小：狀態 TTL（Flink SQL）或 watermark（Spark）決定 id 被記住多久。
  在那個範圍之後才到的重複資料會直接通過。

---

<!-- 5d0123fa7306 -->
## 8) 背壓、擴展與熱點 Key ⭐⭐⭐

<!-- 9eee59c27220 -->
### 8-1) 背壓

**背壓（backpressure）是一個慢的運算子或 sink 讓它上游的一切都跟著變慢，而不是一路緩衝
到記憶體耗盡。**

- **Flink** —— 有界的網路緩衝區：當下游 task 的緩衝區滿了，上游 task 就送不出去而變慢，
  一路回推到來源。Web UI 會顯示每個 task 的背壓與忙碌時間。
- **Spark** —— 每個 micro-batch 只取有限的量：`maxOffsetsPerTrigger`（Kafka）或
  `maxFilesPerTrigger`（檔案）。
- **Kafka Streams** —— consumer 單純地 poll 得比較慢。
- 不論哪一種，積壓最後都會變成 Kafka 裡的 **consumer lag**，而那正是它該在的地方：
  由 log 吸收突發流量。

怎麼診斷：找出**第一個**忙碌、而它下一個卻不忙的運算子 —— 那就是瓶頸。常見原因：慢的
外部查找（用 async I/O）、傾斜的 key、partition 太少的 sink，或 RocksDB 裡狀態很大但
記憶體不夠。

<!-- dce25ab24725 -->
### 8-2) 擴展與重新擴縮

**平行度的上限是來源的 partition 數；keyed state 被切成 key group，重新擴縮時就是把整個
key group 重新分配給新的一組實例。**

- Kafka 來源每個 partition 最多一個活躍讀取者，所以平行度超過 partition 數時，多出來的
  來源實例會閒置（下游運算子仍可以更寬）。
- Flink 把每個 key 雜湊到 `maxParallelism` 個 **key group** 之一；每個 subtask 擁有一段
  連續的範圍。重新擴縮 = 做一個 savepoint、以新的平行度重啟，每個 subtask 載入它的新範圍。
- `maxParallelism` 固定了 key group 的數量，除非改寫狀態（State Processor API），否則
  一個 job 的 keyed state 無法改變它。在第一次正式部署之前就刻意設定好。
- 自動擴縮：Flink Kubernetes Operator 的 autoscaler、Dataflow 的自動擴縮，Kafka Streams
  則是增加實例（partition 會被重新分配）。

<!-- 499527f0fc2e -->
### 8-3) 熱點 key

**一個承載大部分流量的 key 會讓一個 subtask 卡在 100%，其他的卻閒著，再多的平行度都沒用。**

| 修法 | 怎麼做 | 何時 |
|---|---|---|
| **兩階段彙總** | 在 key 上加隨機 salt，先依 `(key, salt)` 預彙總，再依 key 彙總 | 可結合的彙總（count、sum、min、max） |
| **本地預彙總** | 在 shuffle 前於每個 subtask 內先合併（Flink SQL `table.exec.mini-batch.*` + two-phase agg） | 高流量的 group-by |
| **把熱點 key 拆出去** | 已知的熱點 key 走專用的路徑 | 少數已知的 key（測試帳號、大租戶） |
| **換 key** | 用更細的粒度當 key（`city, zone`），再在下游彙總 | 粒度允許時 |

---

<!-- 70ba31f0e577 -->
## 9) 各引擎 ⭐⭐⭐⭐

<!-- 13545803d367 -->
### 9-1) 比較

| | Apache Flink | Spark Structured Streaming | Kafka Streams | Apache Beam / Dataflow |
|---|---|---|---|---|
| 模型 | 逐筆處理的資料流 | micro-batch（增量查詢） | 逐筆處理的函式庫 | 統一模型；由 runner 執行 |
| 部署 | Flink 叢集（K8s operator、YARN、託管服務） | Spark 叢集 / Databricks | 你 Java 應用程式裡的一個函式庫 | Dataflow，或 Flink / Spark runner |
| 狀態 | 一等公民、非常大、有 savepoint | 每個 micro-batch 的 state store | 本地 RocksDB + changelog topic | 由 runner 管理 |
| 事件時間 | 完整：watermark、計時器、遲到資料 | 彙總與 join 的 watermark | grace period、stream time | 完整：視窗、trigger、累積模式 |
| 來源 / sink | 大量 connector | Spark 資料來源 | Kafka 進、Kafka 出 | 大量 IO |
| 何時選 | 低延遲、複雜的事件時間、大狀態 | 團隊在跑 Spark；批次 + 串流一個引擎 | 以 Kafka 為中心的微服務，不用維護叢集 | GCP，或要跨 runner 的可移植性 |

<!-- 38cbc74d3023 -->
### 9-2) Flink：DataStream 與 Table / SQL

**Flink 在同一個 runtime 上有兩套主要 API：完全掌控狀態與計時器的 DataStream API，以及
由 planner 最佳化的宣告式 Table API / SQL。**

- **DataStream** —— `keyBy`、視窗、帶狀態與事件時間計時器的 `KeyedProcessFunction`、
  side output、async I/O。用在自訂邏輯（詐欺規則、複雜事件處理）。
- **Table API / SQL** —— 動態表：串流就是一張不斷變化的表，對它的查詢會產生一份 changelog
  （`+I` 插入、`-U` / `+U` 更新前/後、`-D` 刪除）。
- **只會 append** 的結果（視窗彙總、過濾）可以寫到任何 sink；**會更新**的結果（沒有視窗的
  `GROUP BY`）需要帶主鍵的 upsert sink（`upsert-kafka`、帶 key 的 JDBC、Paimon / Hudi）。
- **CEP 函式庫** —— 在事件序列上做模式比對（SQL 裡的 `MATCH_RECOGNIZE`）。

<!-- 32df3b53bfd9 -->
### 9-3) Spark Structured Streaming

**Structured Streaming 把串流當成一張無界的表，對每個 trigger 一個 micro-batch，增量地
執行你的批次 DataFrame 查詢。**

| 輸出模式 | 輸出 | 適用於 |
|---|---|---|
| **Append** | 只有永遠不會再變的資料列 | 不做彙總的查詢；**有** watermark 的彙總（視窗關閉時輸出） |
| **Update** | 自上次 trigger 以來有變的資料列 | 彙總（在一般查詢上等同 append）；不是每個 sink 都支援 |
| **Complete** | 每次 trigger 輸出整張結果表 | 只有彙總；結果必須放得進記憶體 |

| Sink | 輸出模式 | 備註 |
|---|---|---|
| 檔案（Parquet 等） | append | 透過 sink 的 metadata log 做到 exactly-once |
| Delta Lake | append、complete | **不支援** update；要更新就用 `foreachBatch` + `MERGE` |
| Kafka | append、update、complete | at-least-once |
| `foreachBatch` | 任何模式 | 每個 micro-batch 跑你的程式碼；用 batch id 讓它冪等 |
| Console / memory | 任何模式 | 只用於除錯 |

<!--CODE-->

<!--CODE-->

- **Checkpoint 位置**存放 offset、commit log 與狀態；刪掉它，或以不相容的方式改變查詢
  （加入有狀態運算子、改變分組 key），就代表要從頭開始。
- `trigger(availableNow=True)`（3.3+）處理目前所有可用的資料後就停止 —— 把串流程式碼
  當成增量批次 job 來跑。

<!-- 8ea4e35c7d7f -->
### 9-4) Kafka Streams：KStream 與 KTable

**Kafka Streams 是函式庫，不是叢集：你的應用程式從 Kafka 讀取，把狀態放在以 changelog
topic 為後盾的本地 RocksDB，再寫回 Kafka。**

| 抽象 | 是什麼 | 例子 |
|---|---|---|
| **KStream** | 事件串流 —— 每筆紀錄都是一個新事實 | 點擊、付款 |
| **KTable** | changelog —— 每個 key 的最新值 | 使用者檔案、帳戶餘額 |
| **GlobalKTable** | 完整複製到每個實例的 KTable | 小型參考資料；不需 co-partitioning 就能 join |

<!--CODE-->

- Join 需要 **co-partitioning**：兩個 topic 有相同的 key 與相同的 partition 數；重新指定
  key（`groupBy`、`selectKey`）會建立一個內部的 repartition topic。
- `processing.guarantee=exactly_once_v2` 用 Kafka transaction 做 read-process-write。
- 多啟動幾個應用程式實例就能擴展；partition 會在它們之間重新平衡，被搬走的 task 從
  changelog 還原狀態（standby replica 能縮短這段時間）。

<!-- 644a5034f4ee -->
### 9-5) Apache Beam 與 Dataflow

**Beam 是一套程式設計模型 —— PCollection、視窗、trigger、watermark —— 跑在你選的 runner
上；大家說的通常是託管 runner Google Cloud Dataflow。**

- 批次與串流共用一份 pipeline 定義；由 runner（Dataflow、Flink、Spark）執行。
- 它的模型把 what / where / when / how 這幾個問題講得最清楚（第 4-2 節）。
- Dataflow 加上自動擴縮、動態工作重新平衡，以及託管的 shuffle / streaming engine。
- 取捨：可移植性與託管服務，換來較少的調整旋鈕與各 runner 特有的缺口。

---

<!-- d2ed0cdee980 -->
## 10) 串流 SQL ⭐⭐⭐

<!-- f63e4a06681f -->
### 10-1) Flink SQL 的視窗彙總

**串流 SQL 先宣告帶有 watermark 的來源，再跑一個看起來很普通的查詢，其視窗來自
table-valued function。**

<!--CODE-->

- `TUMBLE`、`HOP`、`CUMULATE` 與 `SESSION`（1.19+ 起為 TVF）就是視窗函式。
- 視窗彙總是**只會 append** 的，所以一般的 `kafka` sink 就行；同一個查詢若沒有視窗，
  就會產生更新，需要帶主鍵的 `upsert-kafka`。

---

<!-- e34a334ff6c8 -->
## 11) 提供即時結果 ⭐⭐⭐

<!-- bc798cd1b4de -->
### 11-1) 即時 OLAP 儲存

**即時 OLAP 儲存直接從 Kafka 擷取，並在次秒級時間內回答彙總查詢，這正是建立在原始事件上
的即時 dashboard 所需要的。**

| 儲存 | 強項 | 備註 |
|---|---|---|
| **Apache Druid** | 依時間分區的 segment、擷取時 rollup | 原生 Kafka / Kinesis 擷取；擅長時間序列的切片分析 |
| **Apache Pinot** | 低延遲、面向使用者的分析、upsert | star-tree 索引；LinkedIn 打造、Uber 使用 |
| **ClickHouse** | `MergeTree` 表上非常快的掃描 | Kafka table engine + materialised view；也是通用分析資料庫 |
| **StarRocks / Doris** | 支援即時擷取與 join 的 MPP SQL | 相容 MySQL 協定 |

查詢模式固定時（一個 dashboard 方塊）在串流 job 裡先預彙總；使用者會依任意維度切片時，
把原始事件擷取進 OLAP 儲存。

<!-- 5b513c9ce4b5 -->
### 11-2) 串流寫入 lakehouse 表

**串流寫進 Iceberg、Delta 或 Hudi，讓串流資料有一個持久、可查詢、具 ACID 提交的家 ——
代價是分鐘級的新鮮度與小檔案問題。**

- 每次提交（一個 Flink checkpoint、一個 Spark micro-batch）都會新增檔案；提交越頻繁小檔案
  越多，所以要排程**壓實（compaction）**（Delta `OPTIMIZE`、Iceberg `rewrite_data_files`）
  與 snapshot 過期清理。
- **Upsert / CDC** —— Hudi merge-on-read、Iceberg v2 列級刪除、Delta `MERGE`，以及
  Apache Paimon（為 Flink 串流 upsert 而生）依 key 套用變更。
- 提交間隔決定新鮮度：一分鐘一次的 checkpoint 代表資料大約在到達一分鐘後可以查詢。
- 這讓 Kappa 式的系統不需要另一份批次副本就能回答歷史問題。

---

<!-- cc55ef57fdf8 -->
## 12) 維運 ⭐⭐⭐⭐

<!-- f95621578b4a -->
### 12-1) 要監控什麼

| 指標 | 告訴你 | 何時告警 |
|---|---|---|
| **Consumer lag**（筆數 / 時間） | job 落後它的來源 | lag 持續成長 |
| **端到端延遲**（事件時間 → sink） | 使用者感受到的新鮮度 | 超過 SLA |
| **Watermark 延遲**（現在 − watermark） | 事件時間落後多少 | 持續成長，或凍結（閒置來源） |
| **Checkpoint 時間 / 大小 / 失敗** | 容錯機制的健康度 | 有失敗，或時間接近間隔 |
| 每個運算子的**背壓 / 忙碌時間** | 瓶頸在哪 | 持續偏高 |
| **被丟棄的遲到紀錄** | 因 watermark 而損失的完整性 | 高於預期比例 |
| **重啟次數** | 當機循環 | 任何重啟風暴 |
| **狀態大小** | 無界成長 | 流量穩定但持續成長 |

<!-- 396a7dccd6d0 -->
### 12-2) 故障情境

| 故障 | 會發生什麼 | 緩解 |
|---|---|---|
| Task / 節點掛掉 | 從最後一個 checkpoint 重啟，重播那之後的資料 | checkpoint；備用 slot |
| 毒訊息（schema 錯誤、null） | job 在它上面當機、重啟、再當機 | 攔截錯誤、送到 **dead-letter queue**、繼續執行 |
| Sink 掛掉 | 背壓、lag 成長，checkpoint 可能逾時 | 帶退避的重試；Kafka 吸收積壓；對 lag 告警 |
| 停機期間來源 retention 過期 | 你從沒讀過的資料被刪除 | retention 比最糟的停機更長；封存到物件儲存 |
| Checkpoint 儲存無法使用 | checkpoint 失敗；之後的故障要重播更多 | 容忍 N 次 checkpoint 失敗，並告警 |
| 錯誤的部署寫出錯誤結果 | 下游拿到錯誤資料 | 部署前做 savepoint；從它或更早的 offset 重播到新的輸出 |
| Producer 時鐘偏差 | 未來的事件時間把 watermark 往前推，其他一切都變成遲到 | 夾住或拒絕超前太多的時間戳 |

<!-- 87f20397db30 -->
### 12-3) 部署新版本與重新處理

- **相容的變更** —— 用 savepoint 停止、部署、從 savepoint 還原。穩定的運算子 uid 讓這件事
  可行。
- **不相容的變更**（新的彙總 key、新的狀態 schema）—— 讓新 job 和舊 job 並行，從較早的
  offset 寫到一個**新的輸出**表 / topic，比對之後切換讀取者並淘汰舊 job。這就是 Kappa 的
  重新處理手法。
- **回補** —— retention 涵蓋時從 Kafka 重播；否則把同一份邏輯當批次 job 跑在封存的原始資料上
  （Flink 與 Spark 都能用批次模式跑這份程式碼）。

---

<!-- 209d8286661b -->
## 13) 實戰設計：每城市即時行程數 ⭐⭐⭐⭐

**題目**：「做一個 dashboard，顯示每個城市開始的行程數與營收，一分鐘內更新，
涵蓋過去 24 小時。」

**先釐清**：事件量、新鮮度 SLA、數字是否可以因遲到事件而修正、使用者會往回看多遠、
以及是否會依其他維度過濾。

<!--CODE-->

要說出口的決策：

1. **事件時間**搭配 watermark，因為 app 事件會因為連線差而遲到。
2. **結果以 `(city, window_start)` 為 key** 做 upsert，讓遲到事件修正某一分鐘，而不是產生
   重複的資料列；dashboard 永遠讀到最新值。
3. 寫到 Pinot 用 **at-least-once + 冪等 upsert**，而不是付出 exactly-once Kafka 輸出的交易延遲；
   重播造成的重複會覆寫同一個 key。
4. **依 producer 指定的 `event_id` 去重**，因為行動客戶端會重試。
5. **原始事件寫進 Iceberg**，作為歷史資料，並用每晚的批次對帳修正那些超過允許遲到時間才到的資料。
6. **擴展**：Kafka partition 依尖峰吞吐量規劃；視窗以城市為 key，如果某個城市佔大宗，就改用
   `(city, zone)` 為 key，在查詢時再彙總。
7. **維運**：對 consumer lag、watermark 延遲與 checkpoint 失敗告警；格式錯誤的事件送到 dead-letter。

---

<!-- 91f1adafd8ae -->
## 14) 快問快答 ⭐⭐⭐

**Q：Kafka 是串流處理器嗎？**
Kafka 是 log；Kafka Streams 與 ksqlDB 才是跑在它上面的處理器。Broker 本身只負責儲存與
提供紀錄。

**Q：為什麼不用處理時間 —— 它比較簡單？**
因為結果取決於 job 什麼時候跑：重播或落後的 partition 都會改變計數。處理時間適合營運訊號
（「最近一分鐘流量裡的錯誤」），不適合業務指標。

**Q：如果某個 Kafka partition 沒有收到資料，視窗會怎樣？**
它的 watermark 不會前進，運算子的最小 watermark 就卡住，視窗不再關閉 —— 除非那個來源
被標成閒置。

**Q：Exactly-once 是指每個事件只被處理一次嗎？**
不是。故障後事件可能被處理不只一次；exactly-once 指的是狀態與已提交的輸出對每個事件只
反映一次。

**Q：要怎麼改變一個正在跑的有狀態 job 的彙總邏輯？**
狀態相容就用 savepoint 再還原。不相容就讓新版本從較早的 offset 寫到新的輸出，驗證後再把
讀取者切過去。

**Q：2 秒的 SLA，用 micro-batch 還是 continuous？**
兩者都能達到；短 trigger 的 micro-batch 往往就夠了。SLA 是次秒級、或邏輯需要逐事件的計時器時，
才選逐筆處理。

**Q：怎麼測試串流 job？**
用測試 harness 對函式做單元測試（Flink `KeyedOneInputStreamOperatorTestHarness`、
Kafka Streams `TopologyTestDriver`），刻意餵入亂序與遲到的事件，再對嵌入式或容器化的
Kafka 跑整合測試。
