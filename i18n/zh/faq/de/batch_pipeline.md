<!-- fb74e83190d9 -->
# 批次資料管線

> **範圍** —— 資料工程面試關於批次管線會問的一切：參考架構、ETL vs ELT、lake vs warehouse vs lakehouse、資料擷取模式、冪等性與回補、遲到資料、`MERGE`、用 Airflow 做編排、dbt、資料品質、檔案與表格格式、成本、故障處理、PII，以及一題完整的設計題。表格與分區設計在 schema 設計文件；串流在串流管線文件。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單；[`./schema_design.md`](./schema_design.md)、[`./data_modeling.md`](./data_modeling.md)、[`./spark.md`](./spark.md)、[`./stream_pipeline.md`](./stream_pipeline.md)、[`./sql_for_data.md`](./sql_for_data.md)；[`./aws_data.md`](./aws_data.md) —— 同一套架構在 AWS 上的做法（Glue、EMR、Athena、S3 上的 Iceberg）；以及較舊的 [`../faq_Airflow.md`](../faq_Airflow.md) 筆記。

每個答案都先給你會說出口的那句話，再講機制，然後是面試官下一步會追問的取捨。

---

<!-- 50b04599671e -->
## 1) 參考架構 ⭐⭐⭐⭐⭐

<!-- 1665d0a1273d -->
### 1-1) 批次管線的五個階段

**批次管線依排程把有界的資料塊送過五個階段：從來源擷取、落地原始副本、清理並一致化、
為使用者建模、對外提供 —— 編排、品質檢查與中介資料橫跨全部五個階段。**

<!--CODE-->

| 階段 | 負責 | 常見工具 |
|---|---|---|
| **擷取（Ingest）** | 把資料從來源拿出來，且不傷到來源 | Fivetran / Airbyte、Debezium、Spark JDBC、自寫 Python |
| **落地（Raw）** | 收到時原封不動的不可變副本 | 物件儲存（S3、GCS、ADLS）、資料倉儲裡的 raw schema |
| **轉換（Transform）** | 清理、去重、定型別、一致化、業務邏輯 | SQL + dbt、Spark |
| **建模（Model）** | 給使用者的事實、維度、彙總 | dbt、資料倉儲 SQL |
| **提供（Serve）** | dashboard、匯出、特徵表、reverse ETL | BI 工具、feature store、Census / Hightouch |

面試重點：**保留原始副本**。之後每一層都能從它重建，
這讓轉換的 bug 變成一次重跑，而不是一次資料遺失。

<!-- 253d58e185d6 -->
### 1-2) ETL vs ELT

**ETL 在載入前用獨立的引擎轉換；ELT 先載入原始資料，再在資料倉儲裡轉換 ——
這是現代的預設，因為倉儲運算可隨需擴展，而原始資料讓你能重新處理。**

| | ETL | ELT |
|---|---|---|
| 轉換在哪跑 | 專用引擎（Spark、Informatica、Python） | 資料倉儲 / lakehouse（SQL、dbt） |
| 目標端有原始資料 | 通常沒有 | 有 |
| 重新處理 | 從來源重新擷取 | 在 raw 層上重跑 SQL |
| 強項 | 大量非 SQL 邏輯；落地前清除 PII | 改得快、分析師自己寫 SQL、lineage |
| 弱項 | 邏輯在倉儲之外，改起來慢 | 原始敏感資料落進倉儲；運算成本 |

大多數真實的技術棧是混合的：大部分分析用 ELT，資料必須轉換後才准落地的地方
（PII、超大的非結構化輸入）才用 ETL。

<!-- 91398e3349fa -->
### 1-3) Data lake vs warehouse vs lakehouse

**Warehouse 是受管理、schema-on-write 的 SQL 儲存；lake 是物件儲存裡便宜的檔案，
以 schema-on-read 讀取；lakehouse 是在 lake 上面加一層表格格式，帶來 ACID 交易、
schema 強制與快速 SQL。**

| | 資料倉儲 | 資料湖 | Lakehouse |
|---|---|---|---|
| 儲存 | 專有、受管理 | 物件儲存裡的開放檔案 | 開放檔案 + 表格格式中介資料 |
| Schema | 寫入時 | 讀取時 | 寫入時，可演進 |
| 交易 | 有 | 沒有（只是一個裝檔案的目錄） | 有（Iceberg、Delta、Hudi） |
| 資料型態 | 結構化、半結構化 | 任何 | 任何 |
| 引擎 | 倉儲自己的 | 很多（Spark、Trino、Presto） | 很多，讀同一批表 |
| 例子 | Snowflake、BigQuery、Redshift | S3 + Hive 表 | Databricks Delta、S3 上的 Iceberg 搭配 Trino / Spark |

單純資料湖的失敗模式是 **data swamp（資料沼澤）**：沒有交易，所以寫到一半的 job
會被讀取者看到；沒有 schema，所以沒人信任那些檔案。

<!-- d1fb748edb77 -->
### 1-4) Medallion 分層，以及各層負責什麼

**Bronze 是原始副本，silver 是清理並一致化後的事實，gold 為某一種用途而塑形 ——
每一層有不同的負責人，對「什麼可以改」也有不同的規則。**

| 層 | 內容 | 規則 |
|---|---|---|
| **Bronze / raw** | 收到時的來源紀錄，加上擷取中介資料（`_ingested_at`、`_source`、`_batch_id`） | 只能附加；永不修改；保留時間要長到足以重建 silver |
| **Silver / cleaned** | 去重、定型別、驗證過、一致化的實體與事件 | 每個實體版本或事件一列；有品質關卡；PII 已處理 |
| **Gold / marts** | 事實、維度、彙總、特徵表 | 依使用者建模；業務定義住在這裡 |

同一個概念、比較舊的名字：staging → integration → presentation。名字不重要，
重要的是約定：**每一層都能從下一層重建**。

---

<!-- 4882f5edb723 -->
## 2) 資料擷取模式 ⭐⭐⭐⭐⭐

<!-- 1719013c1809 -->
### 2-1) 全量、增量，還是 CDC

**小表用全量重載；來源有可靠的 `updated_at` 時用 watermark 欄位做增量；需要抓刪除、
降低來源負擔或接近即時時，用基於 log 的 CDC。**

| 模式 | 怎麼做 | 抓得到刪除 | 來源負擔 | 要注意 |
|---|---|---|---|---|
| **全量快照** | 每次 `SELECT *`，取代目標 | 可以（從缺席判斷） | 高 | 只適合小表或變動慢的表 |
| **增量（watermark）** | `WHERE updated_at > :last_watermark` | 不行 | 低–中 | 需要有索引且可靠的 `updated_at` |
| **只附加** | `WHERE id > :last_id` | 不適用 | 低 | 只適合不可變的列（log、事件） |
| **基於 log 的 CDC** | 讀 binlog / WAL（Debezium、DMS、Datastream） | 可以 | 最低 | 基礎設施較多；schema 變更會一路流過來 |
| **快照比對** | 比較今天與昨天的全量擷取 | 可以 | 高 | 昂貴；其他都不行時的備案 |

<!-- ef954badec81 -->
### 2-2) 把增量載入做對

**用 watermark 加上重疊窗口拉資料，再依主鍵去重 —— 因為資料列會「在過去」才提交，
剛好卡在 watermark 邊界的會被漏掉。**

- 把 watermark（實際載入的最大 `updated_at`）存在狀態表裡，而不是存在重跑就歸零的變數裡。
- **重疊**：查詢 `updated_at > watermark - overlap`。在上次執行前開始、之後才提交的交易，
  它的 `updated_at` 會比 watermark 還舊。
- 在 silver **去重**：依 `updated_at` 取每個鍵的最新版本（再加一個 tiebreaker，
  例如序號或 CDC 的 log 位置）。
- 應用程式伺服器之間的時鐘偏差讓 `updated_at` 只是近似值；重疊窗口會吸收它。
- 硬刪除永遠不會出現在 `updated_at` 查詢裡。要嘛請來源團隊提供軟刪除旗標，要嘛用 CDC。

<!--CODE-->

固定的上界很重要：沒有它，較晚的重跑會比原本那次讀到更多列，
job 就不再是確定性的。

<!-- d9d1295abf42 -->
### 2-3) 用 Debezium 做 change data capture

**Debezium 讀資料庫的複寫 log，每一次資料列變更 —— insert、update、delete —— 都發出
一個事件到 Kafka，讓資料倉儲不用查來源的表就能重播來源的歷史。**

<!--CODE-->

- 每個事件都帶有 `before`、`after`、`op`（`c`、`u`、`d`，快照讀取為 `r`），以及來源
  中介資料，例如 log 位置與交易 id。
- **初始快照**先載入既有的列，接著從快照記錄的 log 位置繼續串流，所以中間不會漏掉任何東西。
- 用主鍵當 topic 的 key，讓同一列的所有變更依序待在同一個 partition。
- 刪除以 `d` 事件抵達，接著是給 compacted topic 用的 tombstone（`null` 值）。
- 取捨：需要 log 存取權限（`ROW` 格式的 binlog、Postgres replication slot），而被遺棄的
  replication slot 會讓 Postgres 一直保留 WAL，直到磁碟塞滿。

<!-- 328280dee03d -->
### 2-4) 檔案與 API

**檔案投遞需要落地約定 —— 命名、完整性訊號、schema —— 而 API 拉取需要分頁、
限流處理與 checkpoint，讓失敗時能接續而不是從頭來。**

檔案（SFTP、合作夥伴的 S3 bucket）：

- 和對方約定含日期與序號的命名規則，以及**完整性訊號** —— `_SUCCESS` / manifest 檔，
  或結尾列的筆數 —— 這樣你永遠不會載入上傳到一半的檔案。
- 立刻複製到你自己的 raw 區，再從那裡處理；合作夥伴的 bucket 不是你的封存庫。
- 記錄每個載入過的檔案（名稱、checksum、筆數），才能偵測到重送的檔案。

API（SaaS 來源）：

- **分頁**：優先用 cursor-based（`next_cursor`）而不是 offset-based，因為拉取期間資料
  變動時，offset 會跳過或重複紀錄。
- **限流**：遵守 `429` 與 `Retry-After`，以指數退避加上 jitter 重試。
- **增量**：API 有提供時，用 `updated_since` 參數過濾。
- **Checkpoint** 最後的 cursor，讓失敗時從那裡接續。

<!--CODE-->

---

<!-- e2309dd826f6 -->
## 3) 冪等性、確定性與回補 ⭐⭐⭐⭐⭐

<!-- 6956ddbdea51 -->
### 3-1) 對管線來說，冪等是什麼意思

**一個 task 對同一個邏輯日期跑一次或跑十次，目標端最後的狀態都一樣，它就是冪等的 ——
這個性質讓重試、重跑與回補變得安全。**

三個要素：

1. **確定性的輸入** —— task 讀的是由它的邏輯日期 / data interval 定義的固定切片，
   永遠不是「`now()` 以來的所有東西」。
2. **取代，不要附加** —— task 擁有它的輸出切片並覆寫它
   （分區覆寫、`MERGE`、在同一個交易裡先刪後插）。
3. **沒有隱藏的副作用** —— 寄信、呼叫 API、計數器都是獨立的 task，有各自的去重鍵。

| 寫入模式 | 冪等嗎？ | 為什麼 |
|---|---|---|
| `INSERT INTO ... SELECT` | 不是 | 重跑會讓資料列重複 |
| `INSERT OVERWRITE ... PARTITION (dt = ...)` | 是 | 該分區被取代 |
| `DELETE WHERE dt = ...` + `INSERT`，同一個交易 | 是 | 效果相同，給沒有 overwrite 的引擎用 |
| 以自然鍵 `MERGE` | 是 | 匹配的列更新，其他的插入 |
| `CREATE OR REPLACE TABLE ... AS SELECT` | 是 | 整張表重建 |

<!--CODE-->

在 Spark 裡，用 DataFrame 搭配 `partitionBy` 做 `mode("overwrite")` 寫入前，要先設定
`spark.sql.sources.partitionOverwriteMode=dynamic`，否則它會取代表的**每一個**分區，
而不只是 job 寫到的那些。

<!-- c113b946c443 -->
### 3-2) 確定性的陷阱

**任何讀取牆上時鐘、亂數，或輸入的「最新」版本的東西，都會讓重跑得到不同的答案。**

- 轉換裡用 `CURRENT_DATE` / `now()` → 改用這次執行的邏輯日期參數。
- 回補去年資料時讀「現在」的維度 → 依事件發生時有效的版本 join（SCD2），
  或每次執行時對維度做快照。
- 沒有全序的 `ROW_NUMBER()`（`updated_at` 同值）→ 加上 tiebreaker 欄位。
- 沒有排序的 `LIMIT`、沒有 seed 的抽樣、在轉換裡生成 UUID 當鍵 →
  從資料推導鍵（自然鍵的雜湊值）。
- 來源資料事後會變 → 重新處理時讀不可變的 bronze 副本，而不是線上的來源。

<!-- 73e5a5ba01a1 -->
### 3-3) 回補與重新處理

**回補是對過去的邏輯日期重跑管線；只有在每個 task 都冪等且以日期為參數時才安全，
而且要限流，免得餓死每日的例行執行。**

- 在 Airflow 2.x：`airflow dags backfill -s 2026-01-01 -e 2026-03-31 my_dag`（Airflow 3 改成
  `airflow backfill create`），或清除過去的 task instance 讓排程器重跑；DAG 第一次部署時，
  `catchup=True` 會自動回補漏掉的 interval。
- 限制併發（`max_active_runs`、pool），讓 90 天的回補不會把資料倉儲塞滿。
- 後面的分區依賴前面的分區時（累計總和、SCD2），從最舊的開始跑。
- **Schema 變更 + 回補**：部署新程式碼，回補到新表或新分區，拿筆數與彙總值和舊輸出比對，
  然後切換。
- 全表重建 vs 增量回補：當邏輯變更影響每一列（新的業務定義）時，從 bronze 重建整張表
  常常比修補分區更簡單也更安全。

<!-- cd3d5561223d -->
### 3-4) 遲到的資料與遲到的維度

**遲到的事實會落進已經跑過的分區，所以要嘛每天重新處理最近一段窗口，要嘛依抵達日期
分區；遲到的維度是指事實比它的維度列先到，所以先指向一個佔位列，等維度到了再修正。**

遲到的事實：

| 策略 | 怎麼做 | 取捨 |
|---|---|---|
| **滾動重處理窗口** | 每次執行都重建最近 N 天的事件日期分區 | 簡單，N 天內正確；運算成本是 N 倍 |
| **依擷取日期分區** | 依抵達時間載入；使用者依事件日期過濾 | 永遠不重寫；依事件日期查詢要掃更多 |
| **增量 `MERGE`** | 把遲到的列 upsert 進正確的事件日期分區 | 有效率；需要鍵，以及支援 `MERGE` 的表格格式 |

遲到的維度（「早到的事實」）：

- 事實參照了 `customer_id = 42`，但 `dim_customer` 還沒有 42 這一列。
- 插入一個 **inferred member（推定成員）** —— 只有自然鍵、屬性為 `unknown` 的佔位維度列 ——
  並讓事實指向它的代理鍵。
- 真正的維度列到了之後，就地更新那個推定列（對佔位列做 Type 1），
  已經指向它的事實就會拿到正確的屬性。
- 另一種做法 —— 單一的 `-1 Unknown` 列 —— 會失去連結，之後還得把事實重新對應鍵。

<!-- aa6045ac79c5 -->
### 3-5) `MERGE` / upsert 模式

**`MERGE` 以某個鍵把進來的列對應到目標，並逐列決定要更新、插入還是刪除 ——
這是冪等地套用一批增量資料或一批 CDC 資料的標準做法。**

<!--CODE-->

- **先對來源去重**：當多筆來源列匹配到同一筆目標列時，`MERGE` 會失敗（或結果不確定）。
- 可以的話，在 `ON` 子句裡加上分區條件來限制目標掃描範圍 ——
  沒有限制的 `MERGE` 會重寫整張表的檔案。
- Snowflake 與 BigQuery 的 `MERGE` 形狀相同，但細節不同（BigQuery 的
  `SET` 只接受不帶前綴的欄位名）；要確認方言。
- 對 SCD2 來說，單靠 `MERGE` 無法用一筆來源列同時關閉舊列又插入新版本；用 staged-union
  技巧，或在同一個交易裡寫兩條語句（見 [`./data_modeling.md`](./data_modeling.md)）。

---

<!-- e20c4400b21a -->
## 4) 編排 ⭐⭐⭐⭐⭐

<!-- 927deaf2125b -->
### 4-1) 編排器負責什麼

**編排器負責 task 何時跑、以什麼順序、帶什麼重試、針對哪個邏輯日期 ——
它不該擁有轉換邏輯，也不該自己搬資料。**

| 負責 | 不負責 |
|---|---|
| task 之間與管線之間的依賴 | SQL / Spark 程式碼（呼叫它，不要內嵌它） |
| 排程與 data interval | task 之間傳遞大量資料 |
| 重試、逾時、告警 | 業務定義 |
| 回補與重跑 | 儲存 |
| 共用資源的併發限制 | |

<!-- 2c87e8d847d6 -->
### 4-2) 面試官會問的 Airflow 概念

**Airflow DAG 是宣告 task 及其依賴的 Python 程式碼；排程器為每個 data interval 建立一個
DAG run，並在上游 task 成功時執行每個 task。**

| 概念 | 是什麼 |
|---|---|
| **DAG** | task 的圖，帶有排程、開始日期與預設參數 |
| **Operator** | task 範本 —— `BashOperator`、`PythonOperator`、`SQLExecuteQueryOperator`、provider 提供的 operator |
| **TaskFlow** | 以 `@task` 裝飾的 Python 函式；回傳值自動透過 XCom 傳遞 |
| **Sensor** | 等待某個條件（檔案、分區、另一個 DAG）的 task；用 `mode="reschedule"` 或 deferrable sensor，才不會佔住 worker slot |
| **XCom** | task 之間傳遞的小型中介資料 —— id、路徑、筆數；**不是**資料集 |
| **邏輯日期 / data interval** | 這次執行*針對*的區間；2026-11-01 的每日執行在該區間結束後才開始 |
| **Catchup** | 為 `True` 時，排程器會為 `start_date` 以來每個漏掉的 interval 建立執行 |
| **重試** | 每個 task 的 `retries`、`retry_delay`、`retry_exponential_backoff` |
| **Pool** | 跨 DAG 共用、有名字的 slot 上限，用來限制對某個資料庫的負載 |
| **SLA** | Airflow 2.x 中每個 task 的 `sla=` 會觸發 miss callback；Airflow 3.0 移除了它，deadline alert 是後繼者 |
| **Dataset / Asset** | 資料感知排程：上游 task 更新某個 dataset 時，DAG 就執行（Airflow 2.4+；3.0 改名為 Asset） |
| **Dynamic task mapping** | `.expand()` 在執行期為 list 的每個元素建立一個 task instance（Airflow 2.3+） |

經典陷阱：每日 DAG 在邏輯日期 `2026-11-01` 的那次執行，涵蓋區間
`[2026-11-01, 2026-11-02)`，並在區間結束**之後**，也就是 2026-11-02 才開始。查詢應該用
`data_interval_start` / `data_interval_end`，而不是牆上時鐘。

<!-- 7d8453787022 -->
### 4-3) 一個小型的 Airflow 2.x DAG

<!--CODE-->

它像正式環境的地方：每個 mapped `extract` 讀固定的 data interval（確定性），寫到以日期
分區的路徑（重跑時冪等），透過 XCom 只傳遞路徑，而來源由 pool 保護。

<!-- 2858f1f7346c -->
### 4-4) Airflow vs Dagster vs Prefect

| | Airflow | Dagster | Prefect |
|---|---|---|---|
| 單位 | DAG 裡的 task | **software-defined asset**（它產出的那張表） | flow 與 task（一般 Python） |
| 排程 | 依時間，加上 dataset / asset | 以 asset 驅動，分區是一等公民 | 依時間 / 事件，執行期動態決定 |
| 強項 | 生態系、provider，多數公司的預設 | 內建 lineage 與分區、可在本機測試 | 儀式感低、動態工作流 |
| 弱項 | DAG 解析開銷；資料感知是後來才加的 | 生態系較小 | 對重型資料平台來說生態系較小 |
| 託管 | MWAA、Cloud Composer、Astronomer | Dagster+ | Prefect Cloud |

好的面試答案會說出取捨：團隊已經在跑 Airflow、工作是 task 形狀的，就用 Airflow；
想讓編排器理解表與分區時，用 Dagster。

---

<!-- 51d484fd47f8 -->
## 5) dbt ⭐⭐⭐⭐

<!-- f3fd75ff3436 -->
### 5-1) dbt 做什麼

**dbt 把 SQL `SELECT` 語句依依賴順序變成資料倉儲裡的表與 view，並附上測試與文件 ——
它是 ELT 的「T」，不負責把資料搬進搬出。**

- 一個 **model** 是一個包含 `SELECT` 的 `.sql` 檔；`{{ ref('stg_orders') }}` 參照另一個
  model，並建立依賴圖。
- `{{ source('app', 'orders') }}` 宣告原始表，才能對它們檢查新鮮度。
- `dbt build` 依 DAG 順序執行 model、test、seed 與 snapshot。
- Lineage 與文件來自那張圖：`dbt docs generate`。

<!-- 7d38c22d7577 -->
### 5-2) Materialization

| Materialization | 產出 | 用於 |
|---|---|---|
| `view` | 一個 view | 輕量的 staging model，永遠是最新的 |
| `table` | 每次執行都重建的表 | 中小型 mart |
| `incremental` | 只 insert / merge 新的列 | 大型事實表 |
| `ephemeral` | 內嵌進下游 model 的 CTE | 可重用的邏輯，不產生表 |
| `materialized_view` | 資料倉儲的 materialized view（依 adapter 而定） | 低延遲的彙總 |

<!--CODE-->

`INTERVAL '1 day'` 是 PostgreSQL / Snowflake 語法；在 BigQuery 要寫
`TIMESTAMP_SUB(MAX(updated_at), INTERVAL 1 DAY)`。incremental model 的 schema 變更
需要 `on_schema_change` 或 `--full-refresh`。

<!-- b51b0f2c5023 -->
### 5-3) 測試、snapshot 與 contract

- YAML 裡的 **generic test**：`unique`、`not_null`、`accepted_values`、`relationships`。
- **Singular test**：回傳失敗列的 SQL 檔；零列代表通過。
- `dbt_utils`、`dbt_expectations` 這類套件提供筆數、新近度與範圍測試。
- **Snapshot** 在來源表上實作 SCD Type 2（`timestamp` 或 `check` 策略），
  加上 `dbt_valid_from` / `dbt_valid_to`。
- **Model contract**（`contract: {enforced: true}`）讓 model 的欄位或型別偏離宣告的
  schema 時，build 直接失敗。
- `dbt source freshness` 會在來源的 `loaded_at_field` 超過門檻時告警。

<!--CODE-->

---

<!-- 4317cd5ea78b -->
## 6) 資料品質與可觀測性 ⭐⭐⭐⭐⭐

<!-- 45cb67e7ea14 -->
### 6-1) 檢查項目，以及在哪裡跑

**在每一層的邊界檢查資料，在壞資料到達使用者之前讓管線停下來 ——
讓一次載入停下來的失敗檢查，比董事會簡報上的錯誤數字便宜得多。**

| 面向 | 檢查 | 例子 |
|---|---|---|
| **Schema** | 欄位與型別符合預期 | 來源把 `amount` 改名成 `total` |
| **完整性** | 鍵不可為 null；筆數和歷史比較 | 今天的訂單比過去 4 週同一星期幾的平均少了 40% |
| **唯一性** | 主鍵沒有重複 | 一次重試的擷取讓某個分區翻倍 |
| **有效性** | 範圍、列舉值、格式 | `amount < 0`、未知的 `status` |
| **一致性** | 參照完整性、跨表總和 | 事實指向不存在的維度鍵 |
| **新鮮度** | 最新分區 / `max(updated_at)` 在 SLA 內 | 上游的匯出沒有落地 |
| **準確性** | 和事實來源對帳 | 每日營收 vs 金流系統 |

在哪裡跑：

- **擷取**：schema、筆數、檔案完整性 —— 盡早失敗。
- **Silver**：唯一性、null、有效性 —— 隔離壞的列。
- **Gold**：業務規則、對帳、異常檢查 —— 阻擋發布。

<!-- 12688ba78047 -->
### 6-2) 失敗、警告，還是隔離

| 嚴重度 | 動作 | 用於 |
|---|---|---|
| **錯誤** | 讓 task 失敗，阻擋下游 | 壞掉的鍵、schema 漂移、空的載入 |
| **警告** | 告警，繼續 | 小幅的量下滑、少數無效列 |
| **隔離** | 把壞的列寫到旁邊的表，其餘繼續 | 列層級的有效性失敗 |

**Write-audit-publish（WAP）**：把新資料寫到 staging 分支或表，跑檢查，通過後才發布
（交換表、fast-forward 分支 —— Iceberg branch、Nessie，或用 `CREATE OR REPLACE` 交換）。
使用者永遠看不到沒檢查過的資料。

<!-- 93cdaa6b50ef -->
### 6-3) 資料契約與可觀測性

- **資料契約（data contract）** 是和產出資料的團隊之間的協議：schema、語意、SLA、
  負責人，以及變更如何公告。在產出端的 CI 強制執行（schema registry、dbt model contract），
  就能把「來源變了」從一次事故變成一個失敗的 pull request。
- **可觀測性** = 監控資料，而不只是監控 job：每張表的新鮮度、量、schema 與分佈指標，
  並在它們的歷史上做異常偵測（Monte Carlo、Elementary、Soda，或自建的指標表）。
- **Lineage**（OpenLineage、dbt docs、DataHub 或 Unity Catalog 這類 catalog）回答事故中的
  兩個問題：*是什麼弄壞了這張表*，以及*誰會受影響*。

---

<!-- faa6367e0c1a -->
## 7) 檔案格式與表格格式 ⭐⭐⭐⭐⭐

<!-- 206b94ca0cab -->
### 7-1) 列式 vs 欄式格式

**擷取與訊息用 Avro（列式、內嵌 schema），分析用 Parquet（欄式、壓縮、帶統計資訊），
過了 raw 層就永遠不要用 CSV 或 JSON。**

| 格式 | 結構 | Schema | 可切分 | 最適合 |
|---|---|---|---|---|
| CSV | 列式、文字 | 沒有 | 未壓縮時可以；gzip 後不行 | 和人類與舊系統交換資料 |
| JSON lines | 列式、文字 | 沒有 | 未壓縮時可以 | API、半結構化的原始資料 |
| **Avro** | 列式、二進位 | 內嵌，有演進規則 | 可以 | 擷取、Kafka 訊息、寫入密集 |
| **Parquet** | 欄式 | 內嵌 | 可以 | 分析掃描 |
| **ORC** | 欄式 | 內嵌 | 可以 | Hive 生態系 |

欄式格式在分析上勝出，是因為查詢只讀它碰到的欄位，同一欄的值放在一起壓縮效果好，
而每個區塊的統計資訊讓讀取端能跳過資料。

<!-- 0ab78385a9fa -->
### 7-2) Parquet 內部結構

**一個 Parquet 檔切成多個 row group，每個 row group 為每一欄存一個 column chunk，
每個 chunk 再切成 page，而 footer 存放 schema 以及讓讀取端能跳過 row group 的 min/max 統計。**

<!--CODE-->

- **編碼**：低基數欄位用 dictionary encoding（`country` 變成小整數 id），上面再疊
  run-length 與 bit-packing，最後套一個 codec（Snappy、Zstd、Gzip）。
- **Predicate pushdown**：`WHERE amount > 1000` 會跳過 `amount` 最大值 ≤ 1000 的所有
  row group。只有資料依該欄**排序或 cluster** 過才有用 —— 在隨機資料上，每個 row group
  的 min/max 都涵蓋全部範圍。
- **Column pruning**：只讀被選取欄位的 chunk。
- **巢狀資料**（struct、list）以 repetition level 與 definition level 儲存（Dremel
  編碼），所以巢狀欄位也是欄式的。

<!-- 6d01291f1ebc -->
### 7-3) 表格格式：Iceberg、Delta Lake、Hudi

**表格格式在 Parquet 檔案之上維護一層中介資料，精確記錄表的每個版本由哪些檔案組成 ——
這就是讓 lake 擁有原子提交、`MERGE`、schema 演進與 time travel 的東西。**

每種表格格式相對於「一個裝 Parquet 檔的目錄」多出來的東西：

- **原子提交** —— 讀取者看到的是舊版本或新版本，永遠不會是寫到一半的 job。
- **列層級的 `UPDATE` / `DELETE` / `MERGE`**。
- **Schema 演進**，透過欄位 id 或 mapping，讓改名不會打壞檔案。
- **Time travel** —— 查詢或回滾到先前的快照。
- **中介資料裡的檔案層級統計** —— 規劃查詢時不用列目錄就能跳過檔案。
- **Compaction** 與快照過期，作為維護操作。

| | Apache Iceberg | Delta Lake | Apache Hudi |
|---|---|---|---|
| 中介資料 | metadata 檔 → manifest list → manifest | `_delta_log` JSON 提交 + Parquet checkpoint | `.hoodie` 裡的提交時間軸 |
| 分區 | 以轉換函式做**隱藏分區**（`days(ts)`、`bucket(16, id)`）；分區可演進 | Hive 風格的欄位；liquid clustering 是另一個選項 | Hive 風格 |
| 列層級變更 | copy-on-write 或 merge-on-read（delete file） | copy-on-write；deletion vector | copy-on-write 或 merge-on-read，為 upsert 而生 |
| 引擎廣度 | 非常廣（Spark、Flink、Trino、Snowflake、BigQuery、Athena） | 在 Spark / Databricks 上最完整；其他靠 connector | Spark 與 Flink 優先 |
| 常見選擇 | 多引擎的 lakehouse | Databricks 使用者 | CDC 密集的 upsert 工作負載 |

**Copy-on-write vs merge-on-read**：copy-on-write 每次更新都重寫受影響的檔案
（讀快、寫慢）；merge-on-read 寫入小的 delete / log 檔，在讀取時合併
（寫快，在 compaction 之前讀會變慢）。

<!-- a07b2a17413a -->
### 7-4) 小檔案問題與 compaction

**大量小檔案會拖慢每一個讀取者 —— 每個檔案都要一次開檔、一次中介資料查詢，常常還要
一個 task —— 所以批次 job 應該寫出幾百 MB 的檔案，而 compaction 應該合併串流或頻繁
小批次留下的檔案。**

成因：

- 輸出分區太多 × task 太多（每個 task 對它碰到的每個分區各寫一個檔案）。
- 頻繁的 micro-batch 或串流 sink。
- 依高基數欄位過度分區。

解法：

- 寫入前減少寫入者：`coalesce(n)`，或依分區欄位 `repartition`，
  讓每個分區只由少數 task 寫入。
- Spark AQE 會自動合併 shuffle partition（`spark.sql.adaptive.enabled`）。
- 表格維護：Delta `OPTIMIZE`（搭配 `ZORDER BY` 或 liquid clustering）、Iceberg
  `rewrite_data_files` procedure、Hudi clustering / compaction。
- 讓舊快照過期（`VACUUM`、`expire_snapshots`），儲存與中介資料才不會無限成長。

---

<!-- a3587b76bfe0 -->
## 8) 分區、成本與效能 ⭐⭐⭐⭐

<!-- 9dc551f1975d -->
### 8-1) 為讀取者分區

**依幾乎每個查詢都會過濾的欄位分區 —— 通常是事件日期或載入日期 —— 並讓每個分區夠大；
完整規則在 [`./schema_design.md`](./schema_design.md)。**

- 依日期分區同時給讀取者 pruning，也給寫入者一個天然的冪等覆寫單位。
- 在分區內依次常被過濾的欄位做 cluster / 排序，讓 min/max 統計發揮作用。
- 過度分區（依 `user_id`、在小表上依小時）會產生小檔案並讓查詢規劃變慢。

<!-- b9c3dd47867b -->
### 8-2) 批次的成本花在哪裡

| 成本來源 | 槓桿 |
|---|---|
| 掃描的 bytes（BigQuery on-demand、Athena） | column pruning、分區過濾、clustering、預先彙總 |
| 運算時間（Snowflake warehouse、Databricks cluster） | 調整大小並自動暫停；用增量取代全表重建 |
| 重建沒有變的資料 | incremental model，只 `MERGE` 有變動的分區 |
| Spark 裡的 shuffle 與傾斜 | broadcast join、修正傾斜（見 [`./spark.md`](./spark.md)） |
| 舊快照與重複資料的儲存 | 快照過期、bronze 的保留政策 |
| 跨區流量（egress） | 讓運算和儲存在同一個區域 |

面試答案裡常見的高影響改動：把大型事實表每晚的全表重建，改成只對有變動的分區做增量 `MERGE`。

<!-- 0f1321b6aafe -->
### 8-3) 效能檢查清單

- 把過濾與投影下推到來源擷取；不要從正式資料庫 `SELECT *`。
- 從 **read replica** 或 CDC 串流擷取，而不是 primary。
- 模型允許時，用整數代理鍵 join，而不是長字串。
- 在 gold 為 dashboard 預先彙總；不要讓 BI 工具掃 silver。
- 調校前先量測：查詢 profile / `EXPLAIN`、Spark UI 的 stage 時間，以及 DAG 關鍵路徑上
  最慢的 task。

---

<!-- 17fc7ac4968f -->
## 9) 故障處理與重跑 ⭐⭐⭐⭐

<!-- e3d44a4c1483 -->
### 9-1) 為失敗而設計

**假設每個 task 都會在某個時候跑到一半失敗；讓每個 task 都原子化且冪等，
修復方式就永遠是「重跑那個 task」。**

- **原子化的輸出**：先寫到暫存位置或 staging 表，再交換或提交
  （表格格式的提交、`ALTER TABLE ... EXCHANGE PARTITION`、`CREATE OR REPLACE`）。
  寫到一半的結果就永遠不會被看到。
- 對暫時性錯誤（網路、限流）以指數退避**重試**；對確定性錯誤不重試
  （schema 不符每次都會以同樣方式失敗）。
- 每個 task 都設**逾時**，讓卡住的查詢不會永遠擋住整次執行。
- 長時間的擷取要有 **checkpoint**（最後的 cursor、最後的檔案），讓重試能接續。
- **依賴資料，而不是時間**：用 sensor 或 dataset 觸發，讓遲到的上游延後這次執行，
  而不是產出一個空分區。

<!-- f785414b5901 -->
### 9-2) 事故處理手冊

1. **偵測** —— task 失敗、品質檢查失敗，或新鮮度告警。
2. **控制** —— 停止下游發布；對使用者標記受影響的表為過期。
3. **診斷** —— lineage 告訴你哪個上游變了；把失敗的分區和最後一個正常的分區比較
   （筆數、schema、相異鍵數）。
4. **修復** —— 改程式碼、請來源重送，或改設定。
5. **重跑 / 回補** 受影響的 interval，從最舊的開始；冪等性讓這件事是安全的。
6. **驗證** 用同一套檢查，然後發布。
7. **預防** —— 加上本來能更早抓到它的那個檢查。

<!-- 10da94c48559 -->
### 9-3) 常見的故障原因

| 原因 | 症狀 | 預防 |
|---|---|---|
| 上游 schema 變更 | 載入或型別轉換錯誤 | 擷取時的 schema 檢查、資料契約 |
| 上游資料遲到 | 空的或不完整的分區 | sensor、完整性檢查、筆數門檻 |
| 重複擷取 | 指標虛胖 | 冪等寫入、唯一性測試 |
| 資料傾斜或量成長 | job 記憶體不足或錯過 SLA | 量的監控、傾斜處理、自動擴展 |
| 憑證 / 權限變更 | 驗證失敗 | secrets manager、對驗證錯誤告警 |
| 非確定性邏輯 | 重跑時數字改變 | 邏輯日期參數、全序排序 |

---

<!-- 3205103cfda3 -->
## 10) 安全、PII 與刪除 ⭐⭐⭐

<!-- 8dc1a193017c -->
### 10-1) 處理 PII

**在擷取時就分類 PII，讓它待在盡可能少的表裡，而且大多數使用者只能拿到遮罩或
token 化後的版本。**

- 在 catalog 裡用標籤**分類**欄位（email、電話、地址、身分證號）。
- **最小化**：用例不需要的 PII，在進 silver 之前就丟掉。
- **假名化**：用帶金鑰的雜湊（帶 secret 的 HMAC）或 vault 發的 token 取代識別碼，
  join 仍然可行，但原始值不會暴露。沒有加鹽的 email 雜湊可以用字典攻擊還原。
- 在查詢時用欄位層級政策**遮罩**（Snowflake masking policy、BigQuery policy tag、
  Unity Catalog column mask），加上列層級安全。
- 靜態與傳輸中都加密；raw 層的存取只限管線的 service account。

<!-- 33766c17c94f -->
### 10-2) 被遺忘權的刪除

**GDPR / CCPA 的刪除請求必須觸及每一份副本 —— raw、silver、gold、快照與備份 ——
這就是為什麼不可變的 raw 層需要一開始就設計好刪除策略，而不是事後再加。**

- 維護一張**刪除請求表**；排程 job 依當事人 id，在每張表刪除他的列
  （在 Iceberg / Delta / Hudi 或資料倉儲上用 `DELETE` / `MERGE`）。
- 表格格式會為了 time travel 保留舊檔案：之後要跑快照過期與 `VACUUM`，
  否則被刪除的列仍然存在儲存裡。
- **Crypto-shredding**：用每個當事人專屬的金鑰加密他的 PII，然後刪掉金鑰 ——
  在重寫不可變資料或備份不切實際時很有用。
- 盡量不要讓 PII 進入難以重寫的只附加 raw 檔；從一開始就落進表格格式。

---

<!-- 29a85d5e4c14 -->
## 11) 完整的設計題 ⭐⭐⭐⭐⭐

<!-- 51009f651e3e -->
### 11-1) 「設計一條從 OLTP 資料庫到報表資料倉儲的每日管線」

**先釐清需求，再從左到右走過各個階段，並把最多時間花在正確性上：增量擷取、
冪等載入、歷史，以及品質關卡。**

要問的釐清問題：

- 哪些表、多大、變動多快？資料列會被硬刪除嗎？
- 新鮮度需求 —— 每天 06:00 前，還是每小時？
- 報表需要歷史嗎（客戶去年三月的等級是什麼）？
- 我們可以讀 primary、replica，還是 binlog / WAL？
- 誰使用輸出 —— BI dashboard、財務對帳、ML？

針對「orders、customers、products；約 5,000 萬筆訂單；每天 06:00 前；有硬刪除；
customers 需要歷史」的一個設計：

<!--CODE-->

每個選擇的理由：

| 決策 | 理由 |
|---|---|
| 從 replica 做 CDC，而不是在 primary 上 `SELECT` | 抓得到硬刪除；不對正式資料庫造成負擔 |
| Bronze 只附加，依載入日期分區 | 不可變的重播來源；每個載入日期都冪等 |
| Silver 用 Iceberg / Delta | 在 lake 上做 `MERGE` 與刪除；用 time travel 除錯 |
| customers 用 SCD2 | 歷史需求 |
| 事實表 3 天的重處理窗口 | 遲到的更新（退款、狀態變更）落進正確的事件日期分區 |
| 測試阻擋發布 | 使用者永遠看不到壞掉的一天 |
| 每個 task 都用固定的 data interval | 回補與重跑得到相同結果 |

要準備的追問：*如果不允許 CDC*（依 `updated_at` 加重疊窗口做增量，再加每週一次全量
快照比對來抓刪除）；*如果量成長 100 倍*（分割擷取、重型轉換交給 Spark、clustering、
全部增量化）；*如果業務需要每小時*（同樣的設計改成每小時 interval，或改成串流 ——
見 [`./stream_pipeline.md`](./stream_pipeline.md)）。

---

<!-- 8021e1d91fea -->
## 12) 快問快答 ⭐⭐⭐⭐

<!-- e02776d6c9a2 -->
### 12-1) 面試現場會出現的問題

- **問：怎麼讓管線冪等？** —— 每個 task 都以邏輯日期為參數，讀固定的切片，
  並覆寫或 `MERGE` 它的輸出，而不是附加。
- **問：怎麼處理遲到的資料？** —— 重新處理最近一段事件日期分區的窗口，或依抵達日期分區，
  或把遲到的列 `MERGE` 到正確位置；依資料會遲到多久、重寫要花多少成本來選。
- **問：ETL 還是 ELT？** —— 分析預設用 ELT；資料必須先轉換才能落地（PII）或轉換不是
  SQL 形狀時，用 ETL。
- **問：增量載入怎麼偵測刪除？** —— 用 `updated_at` 做不到；用 CDC、軟刪除旗標，
  或定期的全鍵快照比對。
- **問：為什麼用 Parquet？** —— 欄式：只讀需要的欄位、壓縮效果好，而 min/max
  統計讓讀取端能跳過 row group。
- **問：表格格式多了什麼？** —— 在 Parquet 檔案之上加入原子提交、列層級變更、
  schema 演進、time travel 與檔案層級統計。
- **問：XCom 裡傳什麼？** —— 路徑、筆數這類小型中介資料；資料透過儲存傳遞。
- **問：某個 job 很慢，你先看哪裡？** —— DAG 的關鍵路徑，接著是最慢的 stage 或查詢
  profile：傾斜、本該 pruning 卻全表掃描，或本可增量卻全表重建。
- **問：怎麼安全地回補一整年？** —— 冪等的 task、限制併發、從最舊的開始、寫到新表或
  新分區、和舊輸出比對驗證，然後切換。
- **問：怎麼阻止壞資料進到 dashboard？** —— 每一層都有品質檢查、錯誤時阻擋，
  並用 write-audit-publish，只發布檢查過的資料。
