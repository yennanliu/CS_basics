<!-- be1b1028e076 -->
# Apache Flink — 資料工程面試深入解析

> **範圍** — 資料工程面試會探的 Flink 引擎本身：runtime（JobManager、TaskManager、slot、chaining）、部署、DataStream 與 Table / SQL API、程式碼裡的 watermark 與 timer、state backend、checkpoint 與 savepoint 設定、搭配 Kafka 的 exactly-once、Flink SQL 的 join 與 CDC、背壓、記憶體、維運與常見故障 —— 以 Flink 1.20 為準，並標出 Flink 2.x 的差異。與引擎無關的串流理論（為什麼要串流、視窗種類、遲到資料策略、引擎比較）屬於 [`stream_pipeline.md`](./stream_pipeline.md)。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) — 橫跨所有 DE 主題的總覽與考前檢查清單；[`./stream_pipeline.md`](./stream_pipeline.md) — 把時間語意、視窗、狀態與 exactly-once 當成概念來講；[`./kafka.md`](./kafka.md) — Flink 通常讀取與寫入的那份 log；[`./spark.md`](./spark.md) — 最常拿來和它比較的批次引擎；[`../flink/faq_flink.md`](../flink/faq_flink.md) — 較早、附圖的 Flink 筆記。

每一題都先給你會說出口的那句話，再講機制，然後是面試官下一步會追問的取捨。設定名稱以
Flink 1.20 為準；1.20 改過名的 key，舊名稱在 1.x 仍然有效，並會註明。

---

<!-- 781475e0c1de -->
## 1) Flink 是什麼、用在哪裡 ⭐⭐⭐⭐

<!-- b313f09249fe -->
### 1-1) 一段話的答案

**Flink 是分散式串流處理器：它執行一個由 operator 組成的資料流，一次處理一個事件，把每個
key 的狀態放在 operator 旁邊，並用週期性的分散式快照（稱為 checkpoint）讓狀態具備容錯能力。**

- **串流優先** —— 批次 job 就是一個*有界*的串流；同一個 runtime 兩者都跑。
- **有狀態** —— 狀態是 job 中一等公民、分區、會做 checkpoint 的一部分，在 RocksDB
  backend 上可以長到 TB 級。
- **事件時間** —— watermark、事件時間 timer 與遲到資料處理都是內建的，所以結果取決於事件
  何時發生，而不是何時抵達。
- **狀態 exactly-once** —— 故障之後，每個 operator 的狀態都還原到同一個一致的 checkpoint，
  來源則倒回對應的 offset。

<!-- 8ea61dd946a6 -->
### 1-2) Flink vs Spark

**Flink 逐筆處理，延遲是毫秒級，並管理大量狀態；Spark Structured Streaming 每次 trigger
跑一個 micro-batch，當團隊本來就用 Spark 做批次時比較合適。**

| | Flink | Spark Structured Streaming |
|---|---|---|
| 執行方式 | 長時間運行的 operator，逐筆處理 | 每次 trigger 跑一個小批次 job |
| 典型延遲 | 毫秒 | 約 100 ms 到數秒（continuous mode 仍是實驗性） |
| 狀態 | 放在 heap 或 RocksDB 的 keyed state，可重新擴縮，有 savepoint | 每個 micro-batch 的 state store（可用 RocksDB provider） |
| 事件時間 | 每個來源分區各自的 watermark、timer、side output | 聚合與 join 有 watermark；除了 `flatMapGroupsWithState` / `transformWithState` 之外沒有使用者 timer |
| 自訂邏輯 | 帶狀態與 timer 的 `KeyedProcessFunction` | 任意的有狀態 operator，但比較受限 |
| 批次 | 支援，但 Spark 是更強的批次引擎 | 兩者中最強的批次引擎 |
| 帶著狀態升級 | savepoint → 新 jar → 還原 | checkpoint 相容性取決於查詢改了什麼 |

面試時說的那句話：**「延遲、事件時間或狀態大小是難點時用 Flink；難點是批次量、串流只是旁邊
幾個聚合時用 Spark。」** 四個引擎的比較表（含 Kafka Streams 與 Beam）在
[`stream_pipeline.md` §9-1](./stream_pipeline.md#9-1-comparison)。

<!-- b79f150af543 -->
### 1-3) 什麼時候不要用 Flink

- **分鐘級的新鮮度就夠了** —— 排程批次 job 或 micro-batch 執行起來、除錯起來都更便宜。
- **Kafka 進、Kafka 出，狀態很小** —— Kafka Streams 跑在服務裡面，沒有叢集要維運。
- **沒有人能維運它** —— Flink job 是長時間運行的分散式系統：checkpoint、savepoint、升級與
  on-call。改用託管服務（Amazon Managed Service for Apache Flink、Confluent Cloud for
  Apache Flink、Ververica）或更簡單的工具。

---

<!-- e4d4aafd2629 -->
## 2) 架構與 Runtime ⭐⭐⭐⭐⭐

<!-- d6a8a98c2e4a -->
### 2-1) JobManager、TaskManager 與 slot

**JobManager 規劃並協調 job，TaskManager 執行它，每個 TaskManager 提供固定數量的 task
slot —— slot 是 job 的一個平行切片被排進去的單位。**

| 元件 | 角色 |
|---|---|
| **Client** | 把你的 `main()` 編譯成 JobGraph 並送出（application mode 下這件事發生在 JobManager 上） |
| **JobManager** | 每個 job 或叢集一個：*Dispatcher*（接收 job、提供 Web UI）、*ResourceManager*（向 YARN / Kubernetes 要 TaskManager），以及每個 job 一個 *JobMaster*（排程 task、觸發 checkpoint、處理 failover） |
| **TaskManager** | 一個 JVM worker；以執行緒執行 subtask、保存它們的狀態、透過網路和其他 TaskManager 交換資料 |
| **Task slot** | TaskManager managed memory 的固定一份；CPU 由各 slot 共用 |
| **Subtask** | 一個（chain 後的）operator 的一個平行實例；在一個執行緒裡執行 |

<!--CODE-->

- `taskmanager.numberOfTaskSlots`（預設 1）設定每個 TaskManager 的 slot 數；常見做法是每個
  CPU 核心一個 slot。
- JobManager 是唯一的協調者；正式環境會開 **high availability**（ZooKeeper 或 Kubernetes
  HA 服務），讓新的 JobManager 接手，並從最近一次完成的 checkpoint 還原 job。

<!-- 0233009814f2 -->
### 2-2) 從程式到執行中的 task

**你的程式碼先變成 StreamGraph，chain operator 之後最佳化成 JobGraph，JobManager 再把它
展開成 ExecutionGraph，每個平行 subtask 一個頂點。**

<!--CODE-->

**Operator chaining** 會在連線是一對一（`forward`）且平行度相同時，把相鄰的 operator 合併成
一個 task —— `source → map → filter` 變成一個執行緒，中間沒有序列化也沒有網路跳轉。`keyBy`、
`rebalance` 或平行度改變會打斷 chain。`disableChaining()` / `startNewChain()` 用來把一個很重的
operator 拆出去，讓 Web UI 看得出是哪一個慢。

<!-- 3e006491e607 -->
### 2-3) 平行度、slot 與 slot sharing

**預設情況下，一個 slot 可以放 job 中*每一個* operator 各一個 subtask，所以 job 需要的 slot
數等於它最高的 operator 平行度 —— 不是總和。**

<!--CODE-->

- 平行度可以每個 operator 設（`.setParallelism(n)`）、每個 job 設（`-p`、`parallelism.default`），
  或完全不設（預設 1）。每個 operator 的設定優先。
- **讓來源對齊 Kafka 分區數**：來源平行度高於分區數時，會有 subtask 閒置 —— 而閒置的 subtask
  會拖住 watermark（§4-2）。
- **Max parallelism**（`pipeline.max-parallelism`）是 *key group* 的數量 —— 也就是你將來能擴到
  的上限。在第一次正式部署前就設定好（§5-4）。

<!-- 783f1dae5c19 -->
### 2-4) 部署模式

**正式環境的 job 跑在 application mode —— 每個 job 一個叢集、`main()` 在 JobManager 上執行
—— 通常透過 Flink Kubernetes Operator 跑在 Kubernetes 上。**

| 模式 | 叢集生命週期 | 隔離 | 用途 |
|---|---|---|---|
| **Application** | 每個 application 一個叢集，結束時拆掉 | 完全隔離 | 正式環境的 job |
| **Session** | 長時間運行；多個 job 共用它的 TaskManager | 無 —— 一個壞 job 可能拖垮其他 job | SQL gateway、notebook、很多小 job |
| **Per-job** | 每個 job 一個叢集，`main()` 在 client 上 | 完全隔離 | 1.15 棄用、2.0 移除 —— 改用 application mode |

資源提供者：Kubernetes（原生或 Operator）、YARN 或 standalone。用 Operator 時，一個 job
就是一個 Kubernetes 資源：

<!--CODE-->

---

<!-- f4dfeafe79f0 -->
## 3) API ⭐⭐⭐⭐

<!-- d8119371fb35 -->
### 3-1) 比較

**預設用 SQL / Table API；需要自訂狀態、timer 或 planner 表達不了的控制時，才降到
DataStream API。**

| API | 層級 | 適用時機 |
|---|---|---|
| **Flink SQL** | 宣告式 | 過濾、join、視窗與累計聚合、CDC 管線；分析師也能自己維護 |
| **Table API** | 宣告式，用 Java / Python 寫 | 和 SQL 一樣的計畫，用程式碼組出來 |
| **DataStream API** | operator、狀態、timer | 每個 key 的自訂邏輯：詐欺規則、有特殊規則的 session 化、非同步補資料 |
| **ProcessFunction** | DataStream 最底層 | 直接存取狀態、事件時間 timer 與 side output |
| **DataSet API** | 只做批次 | 舊 API：1.18 棄用、**2.0 移除** —— 批次改用 DataStream 或批次模式的 SQL |

- **PyFlink** 包裝了 Table API 與 DataStream API；Python UDF 在另一個 Python 行程裡執行，
  所以熱路徑上優先用內建函式，和 PySpark 一樣。
- **Scala**：Scala API 在 2.0 移除；Scala 使用者改呼叫 Java API。
- API 可以混用：`Table` 能轉成 `DataStream` 再轉回來（`toChangelogStream`、
  `fromChangelogStream`）。

<!-- 143552681225 -->
### 3-2) 一個最小的 DataStream job

**一個 job 就是：environment → 帶 watermark 策略的來源 → 轉換 → sink → `execute()`。每個
有狀態的 operator 都要給 `uid`。**

<!--CODE-->

- `aggregate` / `reduce` 每個視窗只保留一個 accumulator；視窗上的 `process` 會把每個元素都
  暫存到視窗觸發為止 —— 只有邏輯真的需要全部元素時才用。
- 以 `Duration` 為參數的視窗與 lateness 方法是 1.19+；更早的版本用 `Time.minutes(1)`。

<!-- 6a1d864c114d -->
### 3-3) `KeyedProcessFunction` 與 timer

**`KeyedProcessFunction` 會帶著某個 key 的狀態看到該 key 的每個事件，並能設定 timer，在某個
事件時間或處理時間回呼它 —— 「如果 N 分鐘內 X 沒有發生」就是這樣表達的。**

<!--CODE-->

- **事件時間** timer 在 watermark 越過它時觸發，所以從 checkpoint 或回補重播時也正確；
  **處理時間** timer 依牆上時鐘觸發。
- Timer 以 key 區分、同一個 key 與時間戳只會有一個，並存放在 checkpoint 裡。
- 陷阱：`PAID` 比 `CREATED` 先到（亂序）會留下一個本來不該觸發的警示 —— 提早到的 `PAID`
  也要存進狀態；而一個永遠等不到結束事件的 key，若沒有 timer 或 TTL 清掉，狀態會永遠留著。

<!-- b7068bff5ad9 -->
### 3-4) Async I/O 與 side output

**向外部服務補資料要用 async I/O，絕不要在 `map` 裡做阻塞呼叫；不屬於主輸出的資料送到
side output，而不是丟掉。**

<!--CODE-->

- 在 `map` 裡做阻塞呼叫，整個 subtask 都得等網路 —— 吞吐量崩掉，背壓往上游擴散。
- `unorderedWait` 依完成順序輸出結果（只在 watermark 之間保持順序）；`orderedWait` 保持輸入
  順序，代價是延遲。
- Async I/O 的狀態會進 checkpoint：還原後，進行中的請求會重送，所以查詢必須可以安全重複。

---

<!-- 7cee51683891 -->
## 4) Flink 裡的 Watermark 與遲到資料 ⭐⭐⭐⭐⭐

概念 —— 事件時間 vs 處理時間、watermark 承諾了什麼、遲到資料的策略 —— 在
[`stream_pipeline.md` §3](./stream_pipeline.md#3-time-semantics-and-watermarks-)
與 [§4-3](./stream_pipeline.md#4-3-late-data-strategies)。這一節講 Flink 怎麼實作它們。

<!-- 9f5dc725f954 -->
### 4-1) Watermark 從哪裡來

**Watermark 在來源產生，每個來源 split（每個 Kafka 分區）各自一個，而 operator 的 watermark
是它所有輸入 watermark 的*最小值*。**

- `forBoundedOutOfOrderness(d)` 送出 `看過的最大事件時間 − d − 1 ms`；
  `forMonotonousTimestamps()` 是 `d = 0` 的特例。
- 它們是週期性送出的，每 `pipeline.auto-watermark-interval`（預設 200 ms）一次，不是每筆都送。
- **在來源裡**產生 watermark（`fromSource(..., strategy, ...)`），不要在來源之後用
  `assignTimestampsAndWatermarks`：只有在來源內部才可能做到每個分區各自的 watermark，後面的
  assigner 看到的分區已經交錯在一起了。
- 在 SQL 裡，同一件事是一個欄位宣告：
  `WATERMARK FOR order_time AS order_time - INTERVAL '10' SECOND`。

<!-- 933231f84958 -->
### 4-2) 閒置分區與 watermark 對齊

**一個沒有資料的分區會拖住所有下游的 watermark，一個遠遠跑在前面的分區會讓狀態變大 ——
`withIdleness` 解決前者，watermark 對齊解決後者。**

| 問題 | 症狀 | 解法 |
|---|---|---|
| 空的分區，或來源平行度高於分區數 | 視窗永遠不觸發；Web UI 裡 watermark 卡在 `Long.MIN_VALUE` | `.withIdleness(Duration.ofMinutes(1))`；SQL 用 `table.exec.source.idle-timeout` |
| 一個分區在重播積壓資料，其他分區是即時的 | 視窗狀態變大，追趕時 job OOM | `.withWatermarkAlignment("orders", Duration.ofSeconds(20))` 會暫停跑太前面的 split |

Idleness 有代價：閒置分區醒來時，它最早的那些事件可能已經算遲到了。

<!-- 4d18083d91d6 -->
### 4-3) DataStream API 裡的遲到事件

**視窗在 watermark 越過它的結束時間時觸發；`allowedLateness` 讓它繼續開著接受更新，再更晚的
仍然送到 side output。**

| 事件抵達的時間… | 會發生什麼 |
|---|---|
| watermark 越過視窗結束之前 | 正常計入 |
| 之後，但在 `allowedLateness` 之內 | 視窗帶著更新後的結果**再**觸發一次 —— sink 必須接受更新 |
| 超過 `allowedLateness` | 丟掉，除非 `sideOutputLateData` 把它導出去（§3-4） |

`allowedLateness` 會讓每個視窗的狀態多留那麼久，所以記憶體成本和 key 的數量成正比。

---

<!-- 244977fe85a0 -->
## 5) 狀態與 State Backend ⭐⭐⭐⭐⭐

<!-- a963c759287b -->
### 5-1) 狀態的種類

**Keyed state 在 `keyBy` 之後以目前的 key 為範圍，幾乎所有 job 都用它；operator state 屬於
一個平行 subtask，broadcast state 則是每個 subtask 上一模一樣的一份。**

| 狀態 | 範圍 | 典型用途 |
|---|---|---|
| `ValueState<T>` | key | 最後一個值、一個旗標、一個截止時間 |
| `ListState<T>` | key | 暫存的事件 |
| `MapState<K, V>` | key | 每個 key 的查表；在 RocksDB 上每個 entry 是獨立的一列，所以比放在 `ValueState` 裡的 map 更好擴展 |
| `ReducingState` / `AggregatingState` | key | 累計聚合 |
| Operator `ListState` | subtask | 來源 offset、sink 暫存的資料 |
| Broadcast state | 每個 subtask，內容相同 | 推送給所有實例的規則或設定 |

Keyed vs operator state 與 broadcast 模式的深入內容在
[`stream_pipeline.md` §5-1](./stream_pipeline.md#5-1-keyed-state-vs-operator-state) 與
[§7-4](./stream_pipeline.md#7-4-broadcast-state)。

<!-- cf341939c923 -->
### 5-2) HashMap vs RocksDB

**HashMap 把狀態當成 Java 物件放在 heap 上 —— 最快，但受記憶體限制；RocksDB 把它序列化放在
本機磁碟 —— 每次存取比較慢，但大小由磁碟決定，也是唯一能做 incremental checkpoint 的選擇。**

| | `hashmap`（預設） | `rocksdb` |
|---|---|---|
| 狀態放在哪裡 | JVM heap，以物件形式 | 本機磁碟（RocksDB），用 managed memory 當快取 |
| 存取成本 | 一次物件查找 | 每次讀寫都要序列化 + native 呼叫 |
| 大小上限 | heap；heap 很大就會有很長的 GC 停頓 | 本機磁碟 —— TB 級 |
| Incremental checkpoint | 沒有 —— 每次都是完整快照 | 有：只上傳新的 SST 檔 |
| 適用時機 | 每個 TaskManager 幾 GB 的狀態 | 大狀態，或 checkpoint 花太久 |

- 用 `state.backend.type: hashmap | rocksdb` 設定。Checkpoint 存到哪裡是另一件事：
  `execution.checkpointing.dir`（1.20 之前是 `state.checkpoints.dir`），正式環境一定是耐久儲存
  （S3、GCS、HDFS）。
- 在 RocksDB 上，狀態的**值**要小，並用 `MapState` 取代一個很大的 `ValueState<Map>` —— 每次
  存取都會反序列化整個值。
- Flink 2.0 新增 **ForSt**，一個實驗性的分離式 backend，把狀態放在遠端儲存，讓 TaskManager 的
  本機磁碟不再是上限。

<!-- 7f0be2852d3c -->
### 5-3) 讓狀態有界：TTL

**每一份 keyed state 都需要一條死路 —— 一個會清掉它的 timer，或一個 TTL —— 否則 job 的狀態
會隨著它看過的每一個 key 一直長。**

<!--CODE-->

- TTL 只用**處理時間**，所以回補時狀態不會像即時流量那樣過期。
- 過期的 entry 是延遲清除的：在存取時、在完整快照時，或由 RocksDB compaction 清掉 —— 記憶體
  不會在 TTL 一到就降下來。
- 在 SQL 裡，`table.exec.state.ttl`（或 1.18+ 每個 operator 的 `STATE_TTL` hint）限制 join 與
  聚合的狀態；沒有它，regular join 會永遠保留兩邊（§8-3）。

<!-- 9e9d921cd9ce -->
### 5-4) Key group 與重新擴縮

**Keyed state 被切成固定數量的 key group —— 也就是 max parallelism —— 重新擴縮時是把整個
key group 在 subtask 之間搬動；這就是為什麼 max parallelism 一改就會失去狀態。**

<!--CODE-->

- 預設的 max parallelism 由第一次部署的平行度推出來（至少 128、最多 32768），所以用不同平行度
  第一次部署的 job 可能得到不同的值。明確設定 `pipeline.max-parallelism`。
- 平行度不可能超過 max parallelism；max parallelism 遠大於平行度只會讓每個 key group 多一點
  額外成本。常見做法是取一個 2 的冪次，至少是預期尖峰的好幾倍。

---

<!-- 0f05b0e30e22 -->
## 6) Checkpoint、Savepoint 與復原 ⭐⭐⭐⭐⭐

<!-- fc42a3efea8b -->
### 6-1) Checkpoint 是什麼

**Checkpoint 是每個 operator 的狀態加上來源 offset 的一致快照，而且不需要停下 job：
JobManager 在來源注入 barrier，每個 operator 在 barrier 通過時對自己的狀態做快照。**

故障時，Flink 重啟受影響的 task，從最近一次**完成**的 checkpoint 還原狀態，並把來源倒回它的
offset —— 所以狀態對每一筆資料恰好反映一次，即使有些資料被*處理*了兩次。Barrier 機制、
aligned vs unaligned checkpoint，以及 checkpoint vs savepoint 在
[`stream_pipeline.md` §6-1 到 §6-3](./stream_pipeline.md#6-1-checkpoints-distributed-snapshots)。

對 Kafka 來源而言，**重要的 offset 是 checkpoint 裡的那些**。Flink 只在 checkpoint 完成時把
offset commit 回 Kafka，而且只是為了讓 lag 儀表板能運作；復原時從來不讀它們。

<!-- 7adfc83460bc -->
### 6-2) 值得記住名字的 checkpoint 設定

| Key（1.20） | 預設 | 該設什麼、為什麼 |
|---|---|---|
| `execution.checkpointing.interval` | 未設定 —— checkpoint **關閉** | 30 秒到 5 分鐘；它限制了故障時要重播多少，搭配交易式 sink 時也決定了輸出延遲 |
| `execution.checkpointing.mode` | `EXACTLY_ONCE` | `AT_LEAST_ONCE` 跳過 barrier 對齊 —— 延遲較低，故障後狀態會有重複 |
| `execution.checkpointing.timeout` | 10 分鐘 | 狀態大時調高；逾時的 checkpoint 算失敗 |
| `execution.checkpointing.min-pause` | 0 | 每次 checkpoint 後休息一下，讓 job 不會一直在做 checkpoint |
| `execution.checkpointing.tolerable-failed-checkpoints` | 未設定（0） | 容許幾次失敗，免得一次慢上傳就重啟 job |
| `execution.checkpointing.unaligned.enabled` | false | 背壓讓 aligned checkpoint 逾時時打開 |
| `execution.checkpointing.incremental` | false | 搭配 RocksDB 時打開（1.20 之前是 `state.backend.incremental`） |
| `execution.checkpointing.externalized-checkpoint-retention` | 取消時刪除 | `RETAIN_ON_CANCELLATION`，讓取消的 job 仍能還原 |

<!-- 08fbfd76d4c8 -->
### 6-3) Savepoint 與升級 job

**Savepoint 是你觸發、由你擁有的 checkpoint：用它停掉 job、部署新的 jar、從 savepoint 啟動
—— 只要每個有狀態的 operator 都保持同一個 `uid`，狀態就會帶過去。**

<!--CODE-->

| 改動 | 能還原嗎？ |
|---|---|
| 新的平行度（≤ max parallelism） | 可以 —— key group 會重新分配 |
| 新增 operator | 可以 —— 它從空狀態開始 |
| 移除有狀態的 operator | 只有加上 `--allowNonRestoredState`（`-n`）才行，它會丟掉那份狀態 |
| 沒有明確 `uid` 的 operator，圖有任何改動之後 | **不行** —— 它自動產生的 id 變了，狀態就成了孤兒 |
| POJO / Avro 狀態型別新增一個欄位 | 可以 —— 兩者都支援 schema 演進 |
| 用 Kryo 序列化的狀態型別改了 | 通常不行 —— 狀態裡避免用泛型型別 |
| 改了 max parallelism | 不行 |

要說出口的規則：**「每個有狀態的 operator 都要有 `uid()`，從第一次部署開始。」**
Savepoint 可以在不同 backend 之間移植（canonical 格式）；checkpoint 不保證可以。

<!-- bc9c81ee5e97 -->
### 6-4) 重啟、failover region 與 HA

**Task 失敗時，Flink 只重啟透過管線化資料和它相連的那些 task —— 也就是 failover region ——
並從最近的 checkpoint 還原它們，依 restart strategy 重試。**

| 設定 | 預設 | 備註 |
|---|---|---|
| `restart-strategy.type` | 開了 checkpoint 時是 `exponential-delay`（1.19 起）；沒開時是 `none` | 也有 `fixed-delay` / `failure-rate`；`none` 會在第一個錯誤時讓 job 失敗 |
| `jobmanager.execution.failover-strategy` | `region` | 處處都有 `keyBy` 的串流 job 通常只有一個 region，所以實務上是整個 job 重啟 |
| `high-availability.type` | 無 | 正式環境用 `kubernetes` 或 `zookeeper`，並把 `high-availability.storageDir` 放在耐久儲存上 |

重啟迴圈 —— job 還原、在同一筆資料上失敗、再還原 —— 會表現為一直上升的 `numRestarts` 指標。
這需要修程式（或加一個 dead-letter 輸出），而不是更多重試。

---

<!-- 9dfeb08c99bc -->
## 7) 搭配 Kafka 的端到端 Exactly-Once ⭐⭐⭐⭐⭐

<!-- a3d8da9a2d9a -->
### 7-1) Flink 保證什麼、sink 補上什麼

**Flink 的 checkpoint 讓*狀態* exactly-once；輸出要 exactly-once，sink 必須是冪等的，或在
checkpoint 時以交易方式 commit。**

| Sink 行為 | 故障後的結果 | 例子 |
|---|---|---|
| 單純 append | 重複 —— 上次 checkpoint 之後的資料會再寫一次 | `AT_LEAST_ONCE` 的 Kafka sink、單純的 JDBC insert |
| 冪等（依 key upsert） | 效果上只有一次 —— 重播會覆寫同樣的列 | JDBC upsert、帶 document id 的 Elasticsearch、Cassandra |
| 交易式、兩階段提交 | 恰好一次 —— 輸出只在 checkpoint 完成時 commit | `EXACTLY_ONCE` 的 Kafka sink、file sink |

兩階段提交如何和 checkpoint 對齊在
[`stream_pipeline.md` §6-5](./stream_pipeline.md#6-5-two-phase-commit-sinks)。

<!-- 88b75eb3a212 -->
### 7-2) 設定成 exactly-once 的 Kafka sink

<!--CODE-->

以下每個陷阱面試官都會問：

- **交易逾時。** Sink 預設的 `transaction.timeout.ms` 是 1 小時；broker 的
  `transaction.max.timeout.ms` 預設是 15 分鐘，所以在改掉其中一個之前，job 一啟動就失敗。這個
  逾時也必須大於預期最長的中斷時間加上 checkpoint 間隔，否則 broker 會中止一個 Flink 還打算
  commit 的交易 —— 資料遺失。
- **讀取端必須用 `isolation.level=read_committed`。** 預設（`read_uncommitted`）的 consumer
  會看到已中止交易的資料 —— 正是你花了成本想避免的重複。
- **延遲等於 checkpoint 間隔。** 下游讀取端只在交易 commit 時才看到它的資料，也就是在一次
  checkpoint 結束時；間隔 1 分鐘，輸出就以 1 分鐘為一步抵達。
- **每個 job 的 `transactionalIdPrefix` 要唯一。** 兩個 job 共用同一個 prefix，會互相 fence
  掉對方的 producer。

<!-- 3ce0c3bb0bed -->
### 7-3) 其他 sink

- **File sink（`FileSink`）** —— 寫入 in-progress 的 part 檔，在 checkpoint 完成時把它們轉成
  finished；搭配 bulk 格式（Parquet）時每次 checkpoint 都會滾一個新檔，所以間隔短就會產生很多
  小檔。
- **Lakehouse 資料表** —— Iceberg、Paimon、Delta 與 Hudi 的 Flink sink 每次 checkpoint commit
  一個資料表 snapshot；新鮮度和檔案數之間是同樣的取捨。
- **JDBC** —— 用以主鍵為 key 的 upsert 語句，讓重播是覆寫而不是重複；JDBC sink 也有 XA
  exactly-once 模式，但很少值得它的維運成本。

---

<!-- 1df23eb5baee -->
## 8) Flink SQL 與 Table API ⭐⭐⭐⭐

<!-- 9fe885e0d0ce -->
### 8-1) 一段話講 dynamic table

**對串流下的 Flink SQL 查詢，是對一張一直在變的表做的持續查詢；它的結果是另一張一直在變的
表，以 changelog 的形式送出。**

只有附加的結果（過濾、視窗聚合）可以送到任何 sink；會更新的結果（沒有視窗的 `GROUP BY`、
regular join）會送出更新與撤回，需要一個有主鍵的 sink。Changelog 的列種類（`+I`、`-U`、`+U`、
`-D`）在 [`stream_pipeline.md` §9-2](./stream_pipeline.md#9-2-flink-datastream-and-table--sql)，
SQL 的視窗聚合在 [§10-1](./stream_pipeline.md#10-1-a-windowed-aggregation-in-flink-sql)。

<!-- 0bce609de491 -->
### 8-2) 累計總額，從 Kafka 到 upsert-Kafka

<!--CODE-->

- 這個聚合每個顧客保留一列狀態，永遠保留，除非設定 `table.exec.state.ttl` —— 而有了 TTL，
  閒置超過它的顧客會從零重新開始。
- `upsert-kafka` 把 key 寫成 Kafka 訊息的 key，刪除寫成 tombstone，所以 compacted topic 會
  保留每個顧客最新的總額。
- Planner 會在 `EXPLAIN CHANGELOG_MODE SELECT …` 裡顯示每個 operator 的 changelog 模式。

<!-- 20899862916b -->
### 8-3) 四種 join

**依每一邊要保留多久來選 join：regular join 永遠保留兩邊；其他三種都限制了狀態。**

| Join | 語法 | 保留的狀態 | 用途 |
|---|---|---|---|
| **Regular** | `a JOIN b ON a.k = b.k` | 兩邊，永遠（除非有 TTL） | 小的、有界的輸入；否則設 TTL |
| **Interval** | `… AND b.ts BETWEEN a.ts - INTERVAL '1' HOUR AND a.ts` | 只有那段時間範圍；由 watermark 清掉 | 兩個時間上很接近的事件串流：曝光 → 點擊 |
| **Temporal** | `JOIN rates FOR SYSTEM_TIME AS OF o.order_time` | 表中仍需要的那些版本 | 用事件*當時*的值補資料：匯率、價格 |
| **Lookup** | `JOIN customers FOR SYSTEM_TIME AS OF o.proc_time` | Flink 裡沒有（可選快取） | 每筆資料都去外部資料庫補資料；資料庫必須扛得住 |

串流 join 的概念在 [`stream_pipeline.md` §7](./stream_pipeline.md#7-stream-joins-and-enrichment-)。

<!-- c82e97ac4422 -->
### 8-4) Flink CDC

**Flink CDC 直接把資料庫的變更 log（MySQL binlog、PostgreSQL logical replication）當成
changelog 串流讀進來 —— 先 snapshot、再讀 log —— 中間不需要 Debezium，也不需要 Kafka。**

- 來源以平行的 chunk 做一致的初始 snapshot，不鎖資料表，然後在 snapshot 結束的那個確切位置
  切換到 log。
- CDC 表是會更新的表，所以它能像其他表一樣 join 與聚合，並寫進 upsert sink —— 常見模式是
  MySQL → Flink CDC → Paimon / Iceberg / Hudi。
- Flink CDC 3.x 新增 YAML pipeline，可以同步整個資料庫，包含 schema 變更。
- 如果中間改放 Kafka（Debezium → Kafka → Flink），用 `debezium-json` 格式讀 topic ——
  [`kafka.md`](./kafka.md) 講的是那一側。

<!-- 9955375ba777 -->
### 8-5) 調校 SQL 聚合

| 設定 | 效果 |
|---|---|
| `table.exec.mini-batch.enabled` + `allow-latency` + `size` | 暫存輸入，每批只更新一次狀態 —— 在 RocksDB 上狀態存取次數大幅減少 |
| `table.optimizer.agg-phase-strategy: TWO_PHASE` | shuffle 前先做本地預聚合；需要 mini-batch；解決 `GROUP BY` 的熱 key |
| `table.optimizer.distinct-agg.split.enabled` | 把 `COUNT(DISTINCT …)` 拆成兩層，分散傾斜的 distinct key |
| `table.exec.state.ttl` | 限制聚合與 join 的狀態 |

---

<!-- c5467798d0dd -->
## 9) 背壓與效能 ⭐⭐⭐⭐

<!-- 002a809c1281 -->
### 9-1) 資料怎麼流動，以及為什麼背壓是安全的

**Subtask 之間用網路 buffer 交換資料，採用 credit-based 流量控制：傳送端只送接收端宣告還有
空間的那麼多 buffer，所以慢的 operator 會讓它的上游變慢，而不是把記憶體用光。**

- Flink 的背壓是症狀，不是故障：最慢的 operator 決定整條管線的吞吐量，Kafka lag 隨之增加。
- 它會傷害 checkpoint：barrier 排在暫存資料後面，所以 **aligned** checkpoint 會變慢或逾時。
  Unaligned checkpoint（barrier 超車越過 buffer）與 **buffer debloating**
  （`taskmanager.network.memory.buffer-debloat.enabled`，把傳輸中的資料縮到大約一秒的量）是
  兩個解法。

<!-- 7360c488afb8 -->
### 9-2) 找出瓶頸

**在 Web UI 裡，找出第一個 *busy*、而它上游全都 *backpressured* 的 operator —— 那個
operator 就是瓶頸。**

| 指標（每個 subtask，每秒多少毫秒） | 意義 |
|---|---|
| `busyTimeMsPerSecond` 接近 1000 | 這個 operator 就是慢的那個 |
| `backPressuredTimeMsPerSecond` 很高 | 它在等下游的某個東西 |
| `idleTimeMsPerSecond` 很高 | 它沒事做 —— 上游餵不飽，或有閒置分區 |

接著問它為什麼 busy：只有一個 subtask busy、其他閒置，是**傾斜**（熱 key ——
[`stream_pipeline.md` §8-3](./stream_pipeline.md#8-3-hot-keys)）；所有 subtask 都 busy，是平行度
太低或程式碼太貴 —— Web UI 的 **flame graph**（`rest.flamegraph.enabled`）會顯示是哪個方法；
sink busy 則是外部系統的極限。

<!-- 412fd3c1b614 -->
### 9-3) 調校，依你會檢查的順序

1. **是傾斜嗎？** 比較每個 subtask 的資料筆數。用預聚合（two-phase、mini-batch）或加鹽的 key
   再做第二次聚合來修。
2. **是阻塞呼叫嗎？** 把同步查詢換成 async I/O（§3-4）或有快取的 lookup join。
3. **是狀態存取嗎？** 在 RocksDB 上，SQL 打開 mini-batch、用 `MapState`、值保持小、給 RocksDB
   更多 managed memory。
4. **是序列化嗎？** 泛型型別退回 Kryo 很慢 —— 改用 POJO、Avro 或註冊過的型別；
   `pipeline.generic-types: false` 會讓退回 Kryo 的情況在啟動時就失敗。
5. **是平行度嗎？** 調高 —— 來源最多到 Kafka 分區數，keyed operator 最多到 max parallelism。
6. **是 checkpoint 嗎？** Checkpoint 很久或一直失敗：RocksDB incremental、unaligned
   checkpoint、拉長間隔、`min-pause`。

---

<!-- bca5212daf72 -->
## 10) 記憶體模型與容量規劃 ⭐⭐⭐

<!-- ba9713712b7e -->
### 10-1) TaskManager 記憶體

**設定總量（`taskmanager.memory.process.size`），讓 Flink 自己分配；值得知道的是 heap、
managed memory（RocksDB 的快取）與網路 buffer。**

| 元件 | 大小由什麼決定 | 用途 |
|---|---|---|
| JVM heap（framework + task） | 剩下的部分 | 使用者程式碼的物件、`hashmap` 狀態 |
| **Managed memory** | `taskmanager.memory.managed.fraction`（0.4） | RocksDB 的 block cache 與 write buffer、批次的排序與 join |
| **Network memory** | `taskmanager.memory.network.fraction`（0.1，介於 64 MB 與 1 GB 之間） | subtask 之間的 buffer |
| JVM metaspace | `taskmanager.memory.jvm-metaspace.size`（256 MB） | 載入的類別 |
| JVM overhead | `taskmanager.memory.jvm-overhead.fraction`（0.1） | 執行緒、native 函式庫 |

- 用 `hashmap` 狀態時，managed memory 大多用不到 —— 調低比例，把空間讓給 heap。
- 在 Kubernetes 上，container 被殺掉卻沒有 Java `OutOfMemoryError`，通常代表 RocksDB 或其他
  native 配置超過了上限：調高 JVM overhead，或確認 `state.backend.rocksdb.memory.managed`
  （預設 true）仍然開著。

<!-- f03051c4a6ae -->
### 10-2) 從工作負載推算規模

<!--CODE-->

---

<!-- d05ca9004db1 -->
## 11) 維運 ⭐⭐⭐⭐

<!-- 2e31271479b8 -->
### 11-1) 要監控什麼

| 指標 | 為什麼 |
|---|---|
| Kafka consumer lag（`records-lag-max`，或由已 commit 的 offset 算出的 lag） | job 跟不上的第一個徵兆 |
| `lastCheckpointDuration`、`lastCheckpointSize`、`numberOfFailedCheckpoints` | 越來越大或一直失敗的 checkpoint，預告了一次又慢又痛的復原 |
| `numRestarts` / uptime | 重啟迴圈 |
| `currentOutputWatermark` vs 牆上時鐘 | 事件時間落後；watermark 凍結代表來源閒置或卡住 |
| 每個 operator 的 busy / backpressured 時間 | 瓶頸在哪裡（§9-2） |
| heap、GC 時間、container 記憶體 | 在 OOM 發生之前看到它 |

<!-- 58eaeb5793f7 -->
### 11-2) 擴縮執行中的 job

- **手動** —— 用 savepoint 停掉，再以新的平行度（≤ max parallelism）從它啟動。
- **Adaptive scheduler**（`jobmanager.scheduler: adaptive`）—— job 使用當下可用的 slot；在
  standalone application 叢集搭配 **reactive mode** 時，加 TaskManager 就會讓 job 擴大。
- Kubernetes Operator 的 **Autoscaler**（`job.autoscaler.enabled: "true"`）—— 量測每個 operator
  的 busy 時間與目標速率，再逐個 operator 重新擴縮；Flink 1.18+ 可以就地套用新的平行度，不必
  完整重新部署。
- 來源的擴縮無法超過 Kafka 分區數。

<!-- f86b4a4bd56e -->
### 11-3) 重新處理

修完 bug 要重播歷史資料時：啟動一個**新**的 job，從最早的 offset（或某個時間戳）開始，寫到
新的輸出，讓它追上，把讀取端切過去，再停掉舊的。用修好的程式碼還原舊的 savepoint，只會修到
它之後的事件。這個模式在
[`stream_pipeline.md` §12-3](./stream_pipeline.md#12-3-deploying-a-new-version-and-reprocessing)。

---

<!-- bd16af8cc85f -->
## 12) 常見故障與解法 ⭐⭐⭐⭐⭐

| 症狀 | 可能原因 | 解法 |
|---|---|---|
| 視窗永遠不觸發，輸出是空的 | watermark 沒有前進：有閒置分區、來源平行度高於分區數，或時間戳用秒而不是毫秒 | `withIdleness`；來源平行度對齊分區數；檢查 timestamp assigner |
| 負載高時 checkpoint 逾時 | 背壓拖慢 barrier 對齊；或狀態太大，完整快照做不完 | unaligned checkpoint、buffer debloating、RocksDB incremental、修掉瓶頸 |
| 狀態無限增長 | 沒有 TTL 的 SQL regular join 或 `GROUP BY`；keyed state 從不清除；key 的基數無上限 | `table.exec.state.ttl`、timer 或 `StateTtlConfig`、interval / temporal join |
| job 一啟動就因 Kafka `InvalidTxnTimeoutException` 失敗 | sink 的 1 小時交易逾時超過 broker 的 15 分鐘上限 | 把 `transaction.timeout.ms` 設成 ≤ broker 的 `transaction.max.timeout.ms` |
| 明明是 `EXACTLY_ONCE`，下游卻有重複 | consumer 用 `read_uncommitted` 讀；或 sink 不是交易式的 | `isolation.level=read_committed`；冪等的 upsert sink |
| Savepoint 還原失敗：「cannot map operator state」 | 某個 operator 沒有 `uid`，自動產生的 id 變了 | 設定 `uid`；如果能接受失去那份狀態，用 `--allowNonRestoredState` 還原一次 |
| 一筆壞資料造成重啟迴圈 | 毒訊息上的反序列化或程式例外 | 捕捉例外並導到 dead-letter side output；壞輸入時回傳 null 的 deserializer |
| TaskManager 被 Kubernetes 殺掉，log 裡沒有 `OutOfMemoryError` | native 記憶體（RocksDB）超過 container 上限 | 調高 JVM overhead / process size；讓 RocksDB 繼續用 managed memory |
| 一個 subtask 100% busy，其他閒置 | 熱 key | two-phase 聚合、加鹽、mini-batch |
| 執行時 `ClassNotFoundException` / `NoSuchMethodError` | connector 或相依套件版本和 Flink 的 classpath 衝突 | shade jar；connector 版本對齊 Flink 版本 |

---

<!-- b8d5a4ee40a2 -->
## 13) 常見問題 ⭐⭐⭐⭐⭐

<!-- a9900f2faa4f -->
### 13-1) 快問快答

| 問題 | 要說出口的答案 |
|---|---|
| 什麼讓 Flink 是「真正的串流」？ | operator 持續運行，每筆資料一處理完就往下傳；沒有 micro-batch |
| Flink 怎麼做到 exactly-once？ | checkpoint 一致地快照所有狀態加上來源 offset；故障時一切倒回那個快照；交易式或冪等的 sink 把它延伸到輸出 |
| Checkpoint vs savepoint？ | checkpoint 是自動的、由 Flink 擁有、用於復原；savepoint 由你觸發、由你擁有、用於升級、擴縮與遷移 |
| Task slot 是什麼？ | TaskManager managed memory 的固定一份，job 的一個平行切片在裡面執行；有 slot sharing 時，job 需要的 slot 數等於最高的平行度 |
| Operator chaining 是什麼？ | 把一對一、平行度相同的 operator 合併到一個執行緒，省掉它們之間的序列化 |
| HashMap 還是 RocksDB？ | 狀態小、要低延遲用 HashMap；狀態大、要 incremental checkpoint 用 RocksDB |
| 為什麼每個 operator 都要設 `uid`？ | savepoint 的狀態依 id 對應到 operator；自動產生的 id 在圖改變時會變，狀態就成了孤兒 |
| Max parallelism 是什麼？ | key group 的數量 —— 重新擴縮的上限，在狀態的整個生命週期內固定不變 |
| 為什麼一個閒置的 Kafka 分區會讓我的視窗停住？ | operator 的 watermark 是它輸入的最小值；空的分區永遠不前進，最小值也就不前進 |
| 遲到資料怎麼處理？ | watermark 裡的有界亂序、用 `allowedLateness` 接受更新、其餘的送 side output |
| 背壓是什麼？怎麼找出原因？ | 慢的 operator 透過 credit-based 流量控制讓上游變慢；找出緊接在被背壓的 operator 下游、那個 busy 的 operator |
| Regular vs interval vs temporal vs lookup join？ | regular 永遠保留兩邊；interval 保留一段時間範圍；temporal 依事件時間對版本化的表做 join；lookup 每筆資料查一次外部儲存 |
| 為什麼我的 Kafka 輸出延遲了一分鐘？ | exactly-once 的 sink 每個 checkpoint commit 一次；輸出延遲等於 checkpoint 間隔 |
| TaskManager 掛掉時會發生什麼？ | JobManager 在其他 slot 上重啟受影響的 failover region，從最近完成的 checkpoint 還原；來源倒回它的 offset |
| Flink 2.0 改了什麼？ | 移除 DataSet API、Scala API、per-job mode 與舊的 `SourceFunction` / `SinkFunction`；`config.yaml` 取代 `flink-conf.yaml`；推出 ForSt 分離式狀態 |
| Flink 還是 Spark Structured Streaming？ | 毫秒級延遲、複雜的事件時間邏輯與大狀態用 Flink；團隊已經在用 Spark、秒級延遲可以接受時用 Spark |

<!-- d624ac71c54f -->
### 13-2) 設計題：「即時偵測信用卡詐欺」

**題目** —— 刷卡交易以每秒 20,000 筆的速度進入 Kafka；若一張卡在 1 分鐘內刷超過 5 次，或在
10 分鐘內有兩筆交易相距超過 500 公里，就標記它；警示必須在幾秒內送到下游服務。

<!--CODE-->

要講的重點，大致依面試官追問的順序：

- **為什麼用 `KeyedProcessFunction` 而不是視窗** —— 規則 2 要拿每筆交易和前一筆比，固定視窗
  表達不了；10 分鐘的 timer 讓狀態保持有界。
- **延遲 vs exactly-once** —— 警示 topic 只看得到已 commit 的資料，所以 checkpoint 間隔（10 秒）
  就是延遲的下限。如果幾秒太慢，改用 `AT_LEAST_ONCE`，在下游依交易 id 去重。
- **狀態大小** —— 活躍卡片數 × 幾筆最近的交易：每個 key 很小，但 key 很多 → RocksDB 加上
  incremental checkpoint。
- **熱 key** —— 卡號分布很平均；以商家為單位的規則就不是，需要預聚合。
- **改規則** —— 用 broadcast state，讓門檻不需要 savepoint 與重新部署就能改；改*邏輯*則是一次
  savepoint 升級（§6-3）。
- **故障** —— TaskManager 掛掉時從最近的 checkpoint 還原，最多重播 10 秒的輸入；冪等的警示
  id 讓任何重複都無害。
