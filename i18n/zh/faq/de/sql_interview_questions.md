<!-- f2b6fc9f8f8e -->
# 資深資料工程師 SQL 面試題

> **範圍** —— 資深資料工程面試 SQL 關卡的題庫：每一題都照面試官的問法提出，然後回答 —— 要說出口的那句話、SQL、每一個追問、大規模下的成本，以及 production 上的取捨。內容涵蓋大規模 join、去重、window function、分析型聚合、gaps and islands、session、遞迴 CTE、用 `MERGE` 做增量載入、SCD Type 2、查詢優化，以及有幾個 task 特別慢的 Spark job。答案背後的機制在它連結的各篇深入解析裡；這份檔案是練習題。
> **另見**：[`./sql_for_data.md`](./sql_for_data.md) —— 從基本原理講 join、NULL、window 與面試題型；[`./sql_explain.md`](./sql_explain.md) —— 回答「為什麼慢？」時要先讀的執行計畫；[`./data_performance.md`](./data_performance.md) —— 跨引擎的儲存配置、pruning 與 SQL 改寫；[`./spark.md`](./spark.md) —— shuffle、AQE 與 skew；[`./data_modeling.md`](./data_modeling.md) —— fact、dimension 與 SCD；[`./batch_pipeline.md`](./batch_pipeline.md) —— 冪等性、回補與遲到資料。

資深的 SQL 關卡不是語法測驗。每一題都有後半段 —— *「如果表有 1 TB 呢？」*、*「為什麼慢？」*、
*「你怎麼知道結果是對的？」*、*「job 重跑會怎樣？」* —— 而區分職級的正是這後半段。因此下面每個
答案的順序都是 **口頭回答 → SQL → 追問 → 成本 → production 取捨**。

除非 fence 第一行標明其他方言，否則每個查詢都是 **PostgreSQL**，而且也能在 DuckDB 上執行。

---

<!-- 3d86a9328302 -->
## 0) 範例資料表

下面每個查詢都跑在這幾張表上。需要其他表的小節會在自己的程式區塊裡建立。

<!--CODE-->

---

<!-- 4e481313055f -->
## 1) 如何回答一道 SQL 題 ⭐⭐⭐⭐⭐

**「我先說出結果的粒度，問清楚 tie、NULL 與遲到資料，把查詢寫成具名的 CTE，用一個小例子大聲
驗證一遍，然後說明它在完整規模下的成本，以及重跑時會發生什麼事。」**

| 步驟 | 你要說的 | 為什麼加分 |
|---|---|---|
| 1. 粒度 | 「每個客戶每天一列。」 | 大多數錯誤答案都是粒度錯了 —— join 發生 fan-out、在錯的層級計數。 |
| 2. 邊界 | 「兩列的 `updated_at` 會相同嗎？`customer_id` 會是 NULL 嗎？一天是指 UTC 嗎？」 | 顯示你看過 production 資料，而不只是教科書上的表。 |
| 3. CTE | 每個 CTE 一個步驟，各自以它所存的內容命名。 | 面試官跟得上，你也能一步一步除錯。 |
| 4. 小例子驗證 | 兩個 key、一個 tie、一個缺漏的日期 —— 手動走一遍。 | 在面試官之前抓到 off-by-one。 |
| 5. 成本 | 「這會依 `event_id` shuffle 10 億列；window 會排序每個 partition。」 | 答案中屬於資深的那一半。 |
| 6. 重跑 | 「這是依 key 做的 `MERGE`，所以重跑只會改寫同樣的列。」 | Production job 正是以冪等性來評斷的。 |

**每一題**練習都要演練這四個問題，因為面試官至少會問其中一個：

1. **規模** —— 表變成 1 TB，或熱門 key 佔了 10 % 的資料時，有什麼改變？
2. **速度** —— 時間花在哪裡？你要怎麼證明？（`EXPLAIN`、Spark UI。）
3. **正確性** —— 你怎麼知道答案是對的？（列數、加總、唯一性檢查。）
4. **重跑** —— 執行兩次會產生重複嗎？

在這些問題背後，面試官評的是五件事，資深的答案不等人問就會逐一碰到：

- **正確性** —— 結果符合業務定義嗎？包括 tie 與 NULL。
- **效能** —— 在完整規模下，會不會造成大量 scan、shuffle 或 spill？
- **可擴展性** —— 到了 TB 級、資料持續成長時還行得通嗎？
- **可靠性** —— 重試、重複與遲到資料有沒有處理？
- **取捨** —— 這個優化在記憶體、金錢或維護上要付出什麼？

---

<!-- cd8768713c7a -->
## 2) 主題地圖 ⭐⭐⭐⭐

| 主題 | 必備知識 | 資深答案多出來的部分 | 本文 | 深入解析 |
|---|---|---|---|---|
| 查詢優化 | `EXPLAIN`、執行計畫 | 找出瓶頸節點，而不是用猜的 | §9 | [`sql_explain.md`](./sql_explain.md) |
| Join | hash、sort-merge、broadcast | 大表對大表的 join、skew、fan-out | §3 | [`sql_for_data.md`](./sql_for_data.md) §2 |
| Window function | `ROW_NUMBER`、`RANK`、`LAG`、`LEAD` | 去重、累計指標、session | §4、§5、§7 | [`sql_for_data.md`](./sql_for_data.md) §5 |
| 聚合 | `GROUP BY`、`HAVING`、條件聚合 | 先聚合再 join、不可加總的指標 | §6 | [`sql_for_data.md`](./sql_for_data.md) §4 |
| CTE 與子查詢 | CTE、相關子查詢、遞迴 | 可讀性 vs optimizer 實際怎麼處理它 | §8、§9 | [`sql_for_data.md`](./sql_for_data.md) §6 |
| 資料品質 | NULL、重複、缺漏紀錄 | 把對帳寫成會讓 job 失敗的斷言 | §7、§10 | [`batch_pipeline.md`](./batch_pipeline.md) |
| 增量處理 | watermark、CDC、`MERGE` | 冪等性、遲到資料、每個 key 多個版本 | §10 | [`batch_pipeline.md`](./batch_pipeline.md) |
| 資料建模 | fact / dimension、SCD 1 / 2 | 重跑也破壞不了的歷史 | §10 | [`data_modeling.md`](./data_modeling.md) |

---

<!-- 60c26a892432 -->
## 3) 大規模 Join ⭐⭐⭐⭐⭐

<!-- 69797103bd28 -->
### 3-1) Q1 —— 把 10 億筆訂單 join 到 1 億個客戶，算出每個客戶的總額

> `orders` 有 10 億列，`customers` 有 1 億列。計算每個客戶的訂單總金額。
> （面試官會接著追問設計、優化、skew、broadcast 與列數暴增。）

**「先把 `orders` 聚合成每個客戶一列，再 join —— 這樣 join 兩邊最多各 1 億列，而不是其中一邊
10 億列。如果報表只需要 `customer_id`，我根本不 join。」**

<!--CODE-->

為什麼比較快：`SUM` 是**可分解的**，所以每個 worker 在 shuffle 之前先把自己那一份做部分聚合
（partial aggregate），跨網路傳送的只有每個 worker 每個客戶一列。先 join 則會把全部 10 億筆
訂單送到它們客戶所在的地方。

**追問 —— 你會怎麼設計？** 先重述粒度（每個客戶一列），決定沒有訂單的客戶要不要出現（要，
金額為 `0` —— 所以從 `customers` 做 `LEFT JOIN` 並用 `COALESCE`），決定訪客訂單怎麼處理
（`customer_id IS NULL` —— 它們不屬於任何人，所以過濾掉並另外回報一個總額），以及哪些狀態
要算進去（`refunded` 大概不該算；要問）。

**追問 —— join 很慢，你怎麼辦？**

1. 改任何東西之前先讀執行計畫或 Spark UI（§9-1）：時間花在 scan、shuffle、sort，還是某一個 task？
2. **少讀一點**：依日期 partition 過濾、只選需要的欄位 —— `orders` 是 columnar，
   `customer_id, amount` 的位元組只是 `SELECT *` 的一小部分。
3. **少搬一點**：先聚合再 join（如上）；若有一邊夠小就 broadcast 它。
4. **處理 skew**：如果某個 task 比同伴跑得久得多（見下一個追問）。
5. **完全避免 shuffle**：對每天都跑的 join，把兩張表都依 `customer_id` 以相同的 bucket 數做
   bucketing 或 clustering，讓相同的 key 本來就放在一起。

**追問 —— 怎麼判斷有沒有 data skew？** 兩個地方的證據一致時就是 skew：

<!--CODE-->

而在 Spark UI 中，該 stage 的 task 摘要會顯示 **最大執行時間與 shuffle read 遠高於中位數**
（例如 20 分鐘對 2 分鐘）。清單最上面出現 `NULL` 或 `-1`、`'unknown'` 這類哨兵值是典型原因：
所有訪客訂單都被 hash 到同一個 partition。

**追問 —— 有一張表放得進記憶體，會改變什麼？** 那就 **broadcast** 它：每個 executor 拿到一份
完整副本，大表就地 join，大的那一邊完全不用 shuffle。低於 `spark.sql.autoBroadcastJoinThreshold`
（預設 10 MB）時 Spark 會自動這麼做，而 AQE 會在執行期發現過濾後的一邊很小時切換過去；你也能用
hint 強制。1 億個客戶放不下 —— 但 `customers WHERE country = 'TW'` 也許放得下。

| Join 策略 | 引擎何時使用 |
|---|---|
| Broadcast hash join | 一邊小到可以複製到每個 executor |
| Sort-merge join | 兩邊都很大；Spark 對兩張大表做等值 join 的預設 |
| Shuffle hash join | 兩邊都 shuffle，且小的一邊每個 partition 都能放進記憶體建成 hash table |

這裡兩張表都很大，所以預設是 sort-merge join；在過濾後的大小證明放得下之前，不要假設 broadcast 可行。

<!--CODE-->

**追問 —— 怎麼避免 join 讓列數暴增？** 當右邊**每個 key 有好幾列**時，join 回傳的列數就會多於
左邊 —— 也就是 fan-out。dimension 上常見的元兇是歷史：SCD Type 2 的 `customers` 表每個*版本*
一列，所以每筆訂單會 join 到它客戶的每一個版本。檢查你假設的唯一性，並在 join 前後對帳：

<!--CODE-->

對有版本的 dimension，要 join 到目前那一列（`AND c.is_current`），或 join 到訂單當時有效的版本
（`AND o.order_date >= c.valid_from AND (o.order_date < c.valid_to OR
c.valid_to IS NULL)`）。

<!-- c565aaf24657 -->
### 3-2) Q3 —— 從未下過訂單的客戶

**「這是 anti-join：我預設用 `NOT EXISTS`，因為不論 NULL 如何它都是對的，而且每個主流
optimizer 都把它當 anti-join 執行。`LEFT JOIN … IS NULL` 等價。要避開的是 `NOT IN`。」**

<!--CODE-->

前兩個回傳客戶 3 與 4；第三個什麼都不回傳。

**追問 —— `LEFT JOIN … IS NULL` vs `NOT EXISTS`？** 在 PostgreSQL、Spark、BigQuery 與 Snowflake
上答案相同、計畫也相同（anti-join）。實務上有兩個差別：`NOT EXISTS` 找到第一筆符合就停，也不可能
fan-out；而 `LEFT JOIN` 的寫法只有在你檢查的是右邊**不可為 NULL 的欄位**時才成立 —— 檢查一個
可為 NULL 的欄位，真正的配對看起來就會像沒配到。

**追問 —— 為什麼 `NOT IN` 遇到 NULL 就壞？** `x NOT IN (a, b, NULL)` 的意思是
`x <> a AND x <> b AND x <> NULL`，而 `x <> NULL` 是 `UNKNOWN`，所以整個條件永遠不會是
`TRUE`，沒有任何一列通過。它也比較慢：因為這條 NULL 規則，PostgreSQL 無法把 `NOT IN` 轉成
anti-join，只能拿它去比對一個 hash 過的 subplan 來過濾。

**追問 —— 在你的資料庫上哪個比較快？怎麼驗證？** 別憑記憶爭論；對兩者都跑 `EXPLAIN ANALYZE`，
比較計畫形狀與實際時間。在 PostgreSQL 中兩種好寫法都顯示 `Hash Anti Join`；`NOT IN` 則顯示
`Filter: (NOT (hashed SubPlan 1))`。在 Spark 中，`EXPLAIN FORMATTED` 對兩種好寫法都顯示
`LeftAnti`，而對可為 NULL 欄位的 `NOT IN` 則顯示 `BroadcastNestedLoopJoin` —— Spark 最慢的 join。

<!-- b9409942996d -->
### 3-3) 爆炸的 join 與笛卡兒積

**「join key 漏掉或不唯一，列數就會相乘；在叢集上這就是一個永遠跑不完的 shuffle，或一個記憶體
耗盡的 executor。我會在 join 之前讓右邊在 join key 上唯一，並在 join 之後檢查列數。」**

| 原因 | 會發生什麼 | 修法 |
|---|---|---|
| 忘了寫 join 條件 | 每一列 × 每一列（笛卡兒積） | 明確寫出 `ON`；除非特別要求，Spark 會拒絕隱式的 cross join |
| 多對多的 key | 列數乘上兩邊的重複數 | 先把其中一邊聚合或 `DISTINCT` 到 join key |
| 有版本的 dimension | 每筆 fact 配到每個版本 | 依 `is_current` 或有效區間 join |
| 用低基數欄位 join | 例如 `country` —— 同國家的每一對 | 用真正的 key join；先聚合再 join |

<!--CODE-->

---

<!-- 4049738b77e0 -->
## 4) 去重 ⭐⭐⭐⭐⭐

<!-- b682cce53fe4 -->
### 4-1) Q2 —— 保留每個事件的最新版本

> 每個事件有 `event_id`、`user_id`、`event_time` 與 `updated_at`。同一個 `event_id` 可能出現
> 不只一次；保留最新的版本。

**「把每個 `event_id` 的列依新到舊編號，保留第 1 號 —— 並在 `ORDER BY` 裡加一個 tie-breaker，
讓選擇是確定的（deterministic）。」**

<!--CODE-->

BigQuery、Snowflake、DuckDB 與 Spark 4.2+ 可以直接用 `QUALIFY rn = 1` 對 window 過濾，省掉 CTE；
Spark 4.1 以前仍需要 CTE 或子查詢。

**追問 —— 兩列的 `updated_at` 相同，結果要怎麼確定？** 沒有 tie-breaker 時 `ROW_NUMBER` 會任選
一列，換一種 partitioning 重跑可能選到另一列 —— 同樣的輸入產出兩張不同的表。加一個真正能排序
版本的欄位：來源的序號、CDC 日誌位置（LSN、binlog offset）、Kafka offset，或像這裡的 ingestion
序號。最後手段是用整列的 hash，結果可重現，但沒有意義。

**追問 —— `ROW_NUMBER()` vs `DISTINCT`？** `DISTINCT` 只移除**每一個**欄位都相同的列。event 1
的兩個版本 `updated_at` 不同，所以 `DISTINCT` 兩列都會保留。`ROW_NUMBER` 是**依 key** 去重，
並讓你選擇哪一列勝出。`DISTINCT` 只適用於完全相同的重複 —— 同一筆紀錄被送了兩次。

**追問 —— 10 億列。會有什麼效能問題？** window 需要同一個 `event_id` 的所有列在一起，所以它會
**依 `event_id` shuffle 整張表**，並**排序**每個 partition —— 這是計畫中最貴的兩步，排序還可能
spill 到磁碟。要點出的問題與修法：

- **Skew**：熱門或為 NULL 的 `event_id` —— 一個 task 拿到幾百萬列。把 NULL 過濾掉或另外處理。
- **Spill**：增加 shuffle partition 數，讓每個排序放得進記憶體。
- **更便宜的運算子**：聚合不需要排序。Spark、DuckDB 與 BigQuery 都有「arg max」聚合，回傳排序值
  最大的那一列：

<!--CODE-->

- **不要每次都對整段歷史去重** —— 見下一個追問。

**追問 —— 資料持續進來，要怎麼增量去重？** 只對新的批次去重，再把它 `MERGE` 進一個本來就唯一
的目標表，只有進來的版本比較新時才取代：

<!--CODE-->

批次必須在 `MERGE` **之前**去重：兩筆來源列對到同一筆目標列，在 PostgreSQL、Spark / Delta 與
Snowflake 上都是錯誤（*"cannot affect row a second time"*）。要讓大目標表上的 `MERGE` 便宜，
可以把批次的事件日期範圍加進 `ON` 子句，只讀被碰到的 partition —— **但前提是有「事件日期永不改變」
的約定**，讓批次中 key 的既有列一定落在這個範圍內。沒有這個約定時，把事件改到另一天的更正在被修剪
過的範圍裡找不到對應，`MERGE` 就會為同一個 `event_id` 插入第二列。日期可能改變時，只用 `event_id`
比對，改從目標表的配置取得速度 —— 依 `event_id` 做 clustering 或 Z-order，讓 key 查找能跳過檔案。
watermark 在 §10-1。

<!-- 9925900276ea -->
### 4-2) 依狀態優先序去重

> 重試會讓每個 `order_id` 留下好幾列。保留最*正確*的那一列：`SUCCESS` 優先於 `PENDING`，
> `PENDING` 優先於 `FAILED`，同狀態內取最新。

**「同樣用 `ROW_NUMBER`，把業務優先序寫成 `ORDER BY` 裡排在時間戳之前的 `CASE`。」**

<!--CODE-->

雖然有一筆較新的 `FAILED`，訂單 1 仍保留 `SUCCESS`；訂單 2 保留 `PENDING`。要說出這條規則存在的
理由 —— 付款成功過一次就是已付款，不管之後的重試回報了什麼 —— 因為面試官在檢查你會不會去問
業務規則，而不是預設「最新的贏」。當不只一個查詢需要這個優先序時，把它放在一張小對照表裡，
而不是寫成 `CASE`。

---

<!-- 38c24f5568fb -->
## 5) Window Function ⭐⭐⭐⭐⭐

<!-- 915aca6a70e0 -->
### 5-1) Q4 —— 每個部門第二高的薪水

**「在部門內依薪水由高到低做 `DENSE_RANK`，保留第 2 名 —— 但要先問清楚『第二高』指的是第二個
*不同的薪水值*，還是排第二的*人*。」**

<!--CODE-->

部門 1（薪水 300、200、200、150、120）回傳 Lin **和** Max，都是 200；部門 2 回傳 Quy，90。

**追問 —— `ROW_NUMBER` vs `RANK` vs `DENSE_RANK`？** 它們只在 tie 時不同。

| 薪水 | `ROW_NUMBER` | `RANK` | `DENSE_RANK` |
|---|---|---|---|
| 300 | 1 | 1 | 1 |
| 200 | 2 | 2 | 2 |
| 200 | 3 | 2 | 2 |
| 150 | 4 | 4 | 3 |

`ROW_NUMBER` 給唯一的編號、任意打破 tie（用來剛好挑一列）；`RANK` 讓 tie 同名次，然後**跳號**
（奧運式排名）；`DENSE_RANK` 讓 tie 同名次且**不跳號**（用來找「第 n 個不同的值」）。

**追問 —— 兩個人並列最高薪，第二高是多少？** 取決於問題本身，所以要問：

- **第二個不同的薪水值** —— `DENSE_RANK = 2`。兩個人 300、一個人 200 時，答案是 200。
- **排第二的人** —— `ROW_NUMBER = 2` 回傳兩個 300 的其中一個，由 tie-breaker 決定（在 `ORDER BY`
  加上 `employee_id` 讓選擇穩定）；而 `RANK = 2` 什麼都不回傳，因為兩人都是第 1 名，下一名是第 3。

也要說明只有一種薪水的部門會怎樣：沒有任何一列。如果報表需要一列 `NULL`（像 LeetCode 176
那樣），就從部門表出發、`LEFT JOIN` 第 2 名那一列。

**追問 —— 每個部門薪水前三名？** 用 `DENSE_RANK() <= 3`，邊界上的 tie 全部納入（LeetCode 185）。
要說清楚這代表什麼：前三個**薪資級距**，人數可能超過三個：

<!--CODE-->

如果報表每個部門必須**剛好**三列，改用 `ROW_NUMBER() <= 3`，並說明由哪個 tie-breaker 決定誰被
切掉。

<!-- 60dc07c7cd96 -->
### 5-2) Q5 —— 營收的 7 日移動平均

**「用一個涵蓋最近七天的 window frame —— 但 `ROWS BETWEEN 6 PRECEDING` 數的是列不是天，所以
日期有缺時，它平均的範圍會超過一週。我會先用日曆補齊日期，或在日期上用 `RANGE` frame。」**

<!--CODE-->

在 11-08 那一列，這個 frame 涵蓋 11-01 到 11-08 —— **八**個日曆天，因為 11-04 與 11-07 沒有資料。

**追問 —— `ROWS` vs `RANGE`？** `ROWS` 數的是目前這列前後的實體列。`RANGE` 取的是**排序值**與
目前列相差在 offset 內的所有列，所以 `RANGE BETWEEN INTERVAL '6 days' PRECEDING AND CURRENT ROW`
不論有幾列，都剛好是最近七個日曆天。`RANGE` 也把 **peer** —— 排序值相同的列 —— 當成一個單位，
這就是為什麼預設 frame（`RANGE … CURRENT ROW`）會讓並列的列有相同的累計值。Spark SQL 的 `RANGE`
需要數值型的排序欄位，所以在那裡要依天數編號排序。

**追問 —— 日期有缺，結果還是 7 日平均嗎？** 不是 —— 有兩個原因。`ROWS` frame 跨越超過七天；
而就算是正確的 `RANGE` frame，也只平均**存在**的那幾天，缺的那天被跳過，而不是當成零。哪個才對
是業務問題：營收表缺一天，通常不是當天零銷售，*就是* pipeline 失敗，平均之前你必須知道是哪一種
（§7-3）。

**追問 —— 先補日期，再算平均。** 如果資料一開始是 `orders`，先把它聚合成**每個日期一列**
（`SUM(amount) … GROUP BY order_date`）。然後產生一條日期序列（date spine），把每日的列
`LEFT JOIN` 上去，並決定缺的那天值多少 —— 只有缺的那天真的代表沒有營收時才用零：

<!--CODE-->

現在 `ROWS` 與天數一致了。前六列平均的仍少於七天；如果部分 window 會誤導，就把它們回報成 `NULL`
（`CASE WHEN days_in_window = 7 THEN … END`）。不用日期序列的話，等價的寫法是 `RANGE` frame：

<!--CODE-->

—— 它平均的是存在的天數而不是七天，所以只有在缺的那天代表「未知」而非「零」時才正確。

---

<!-- ccc0e0c690cf -->
## 6) 聚合與分析型 SQL ⭐⭐⭐⭐

<!-- 6dfc1cd2f110 -->
### 6-1) Q6 —— 每日訪客數、購買人數與轉換率

**「一次掃表就用條件式 distinct count 算出來，除法用 `NULLIF`，讓沒有訪客的那天回傳 `NULL` 而不
是報錯 —— 然後我會確認定義，因為『某天的購買者』和『有轉換的訪客』不是同一件事。」**

<!--CODE-->

沒有 `ELSE` 的 `CASE` 對其他事件類型會產生 `NULL`，而 `COUNT` 會略過 NULL —— 這就是一次掃描能
產出兩個計數的原因。

**追問 —— 為什麼要 `NULLIF`？** `NULLIF(x, 0)` 在 `x` 為 0 時回傳 `NULL`，而任何數除以 `NULL`
都是 `NULL`。沒有它，某天沒有任何造訪就會丟出 *division by zero*，整個查詢掛掉。`1.0 *` 也很重要：
在 PostgreSQL、Spark 的 `div` 與 SQL Server 中，整數除以整數會截斷，所以 1 / 2 會是 0。

**追問 —— `COUNT(*)` vs `COUNT(column)` vs `COUNT(DISTINCT column)`？**

| 運算式 | 計算的是 | 對 `(10, 10, NULL)` |
|---|---|---|
| `COUNT(*)` | 列數 | 3 |
| `COUNT(user_id)` | 非 NULL 的值 | 2 |
| `COUNT(DISTINCT user_id)` | 不同的非 NULL 值 | 1 |

**追問 —— 使用者一天造訪、另一天購買，這個指標還符合嗎？** 不符合。11-02 使用者 12 當天沒有造訪
就購買了，所以 11-02 的轉換率把一個不在分母裡的購買者算進分子。若一個使用者只購買、另一個只造訪，
查詢會回報 1 / 1 = 100 %，但其實沒有人轉換 —— 單日轉換率甚至可能超過 100 %。先把定義談好，再寫成
分子是分母子集的形式。若定義是*「同一天既造訪又購買」*，先聚合成每個使用者每天一列並帶旗標：

<!--CODE-->

若定義是*「在 d 日造訪的使用者中，24 小時內購買的比例」* —— 這也會算進剛過午夜的購買 —— 就以第一次
造訪為錨點：

<!--CODE-->

也要問「一天」是哪個時區；對 UTC 時間戳做 `CAST(event_time AS DATE)` 得到的是 UTC 的一天。

**追問 —— 大表上的 `COUNT(DISTINCT)` 很貴，怎麼降成本？**

1. **用近似值**：`approx_count_distinct`（Spark；BigQuery 的 `APPROX_COUNT_DISTINCT`；Snowflake 的
   `APPROX_COUNT_DISTINCT`）用的是 HyperLogLog —— 每個群組只需幾 KB 狀態，誤差約 1–2 %。用在
   dashboard 可以，用在計費不行。
2. **先預聚合到「使用者 × 日」的粒度**，只做一次 —— 每個 `(date, user_id)` 一列，帶 `visited` /
   `purchased` 旗標 —— 然後從那張表數列數。distinct 的工作在 pipeline 裡做一次，而不是每個
   dashboard 查詢都做。
3. **每天存 sketch**（HLL），讓週與月的不重複數變成 sketch 的聯集，而不是重新掃描。distinct count
   **不可加總**：七天的 DAU 加起來不等於 WAU。
4. **一個查詢只做一個 distinct** —— 在 Spark 中，一個查詢裡對不同欄位做好幾個 `COUNT(DISTINCT …)`，
   會讓資料每個欄位展開一次（`Expand` 運算子）。把它們拆開，或先預聚合。

<!-- 575f28a532a5 -->
### 6-2) Cohort 留存

> 在 d 日註冊的使用者中，有多少比例在 d + 1、d + 7 與 d + 30 日是活躍的？

**「建立 cohort（每個使用者的第一天），建立不重複的活躍日，依使用者 join，依 cohort 與天數差計算
活躍人數 —— 再除以另外算出的 cohort 大小，因為 cohort 的大小不能取決於誰回來了。」**

<!--CODE-->

11-01 這個 cohort 有三個使用者（10、11、13）；第 1 天只有使用者 11 活躍，所以第 1 天留存率是 33.3 %。
`eligible` 這一步讓第 7 天沒有人回來的成熟 cohort 顯示 **0 %**，而不是整列消失 —— `retained` 只有
有人活躍的 cohort-天數組合 —— 同時排除 cohort 還太年輕、尚未到達的天數差。

**這個版本避開的 bug。** 常見的寫法在 `LEFT JOIN` 加上 `WHERE a.activity_date >= c.cohort_date`
之後，在同一個 `GROUP BY cohort_date, day_number` 裡計算 `COUNT(DISTINCT c.user_id)` 與
`COUNT(DISTINCT a.user_id)`。這會出兩個錯：右表上的 `WHERE` 把 `LEFT JOIN` 變成 inner join，從沒
回來的使用者就消失了；而在每個 `day_number` 群組裡，「cohort 大小」其實就是當天活躍的使用者 ——
所以每一列的留存率都是 100 %。分母要每個 cohort 另外算一次。

Production 注意事項：註冊日要來自 `users` 表，而不是 `MIN(event_time)`，因為舊事件回補時後者會
移動；而「截至」指的是活動資料完整的最後一天，不是今天。

<!-- 2c924f817316 -->
### 6-3) DAU、WAU 與 MAU

**「DAU 是每天的不重複使用者。WAU 與 MAU 是往回一段 window 內的不重複使用者，不等於 DAU 的加總 ——
所以我在一段日期範圍上算 distinct，或把 sketch 取聯集。」**

<!--CODE-->

這個範圍 join 會讓每個「使用者 × 日」出現在最多 30 個輸出日裡，所以在大規模下會增量計算 —— 從一張
存好的「使用者 × 日」表每天一個 partition —— 或用每日的 HLL sketch，`MAU(d)` 就是最近 30 個 sketch
的聯集。`DAU / MAU` 就是「黏著度」（stickiness）比率。

<!-- a31d8d058203 -->
### 6-4) 每個使用者第一次與最近一次購買

**「依使用者分組取購買日期的 `MIN` 與 `MAX`。如果題目要的是第一筆的整列 —— 金額、商品 —— 它就變成
`ROW_NUMBER` 或 `arg_min` 的題目。」**

<!--CODE-->

要點出兩個邊界：第一筆訂單被退款（依狀態過濾，或不過濾 —— 要問），以及需要整列時第一天出現 tie
（用 `order_id` 打破）。

<!-- 5b6d67c82a23 -->
### 6-5) 把每日資料轉成每月報表

**「條件聚合：每個輸出欄位一個 `SUM(CASE …)`。這是可攜的 pivot，而且只掃一次。」**

<!--CODE-->

`FILTER (WHERE …)` 是標準寫法（PostgreSQL、DuckDB、SQLite）；`SUM(CASE …)` 到處都能用。Spark、
Snowflake 與 BigQuery 也有 `PIVOT` 子句，但它的欄位清單必須在寫查詢時就知道 —— 不論哪種寫法，
新增一種狀態都需要改程式。

---

<!-- f702880edfb9 -->
## 7) Gaps、Islands 與 Session ⭐⭐⭐⭐⭐

<!-- d883bcf6ebb4 -->
### 7-1) 連續登入三天以上的使用者

**「Gaps and islands：在每個使用者內，用日期減去 row number。連續的日期減去連續的編號會得到同一個
值，所以這個差值就是 island 的 id。要先對日期去重，否則同一天登入第二次會把連續段打斷。」**

<!--CODE-->

使用者 10 有一段 11-01 → 11-03；使用者 11 有 11-03 → 11-06。

<!--CODE-->

- **為什麼要去重**：使用者 10 在 11-02 的兩次登入會拿到 row number 2 與 3，差值因此分裂，三天的
  連續段被回報成兩段短的。用 `DENSE_RANK` 取代 `ROW_NUMBER` 也能修好。
- **方言**：日期減整數是 PostgreSQL / DuckDB 的寫法；MySQL 要用 `DATE_SUB(login_date,
  INTERVAL rn DAY)`，Spark 用 `date_sub(login_date, rn)`。
- **`LAG` 的替代寫法** —— 當某列不是前一列的隔天時標記為新 island，然後旗標的累計 `SUM` 就是
  island id。它能推廣到任意大小的間隔（「兩天內都算連續」），row number 的技巧則不行。
- LeetCode 601（*Human Traffic of Stadium*）就是把日期換成 id 的同一題。

<!-- 79540f2afd55 -->
### 7-2) Sessionization：閒置 30 分鐘後就是新 session

**「`LAG` 給每個事件前一個事件的時間；間隔超過 30 分鐘（或沒有前一個事件）就標記為新 session；
旗標的累計 `SUM` 就是 session 編號。」**

<!--CODE-->

使用者 10 在 09:00、09:00、09:20 的事件是 session 1；11:00 是 session 2。

- **先去重** —— 重複的 event 1 在這裡兩次都進了 session 1；在 production 中，sessionize 之前先跑
  §4-1 的去重。
- **`event_time` 的 tie** —— 在兩個 `ORDER BY` 都加上 tie-breaker，否則同一時刻的兩個事件在兩次
  執行中可能編號不同。
- **全域的 session id** —— `user_id || '-' || session_no`，或使用者與該 session 第一個事件時間的
  hash，後者在之後的事件到達時仍保持穩定。
- **大規模時**這會依 `user_id` shuffle 並排序每個使用者的事件；有幾百萬個事件的機器人就是 skew 的
  key。**跨批次邊界時** session 可能跨過午夜，所以要嘛重新處理一段回看 window，要嘛把每個使用者
  最後的事件時間存在 state 裡往下帶 —— 這正是 streaming session window 做的事。
- **方言** —— PostgreSQL 與 DuckDB 用 `INTERVAL '30 minutes'`；Spark 接受 `INTERVAL 30 MINUTES`，
  BigQuery 用 `TIMESTAMP_DIFF(event_time, prev_time, MINUTE) > 30`。

<!-- f2b8edfe45d7 -->
### 7-3) 每日資料表缺了哪些日期？

**「為預期範圍產生一份日曆，拿資料表對它做 anti-join。然後真正的問題是：缺的日期是零營收，還是
pipeline 沒有跑？」**

<!--CODE-->

回傳 11-04 與 11-07。

**追問 —— 日曆從哪裡來？** PostgreSQL 與 DuckDB 用 `generate_series`，Spark 用 `sequence()` +
`explode()`，BigQuery 用 `GENERATE_DATE_ARRAY` + `UNNEST`，Snowflake 用 `GENERATOR`。大多數資料倉儲
會維護一張永久的 `dim_date` 表 —— 數十年每天一列，帶有會計期間與假日 —— join 它比產生更簡單。

**追問 —— 來源是分區的 Parquet，怎麼避免全表掃描？** 根本不要讀資料：問題是哪些 **partition**
存在，而那是 metadata。Spark / Hive 用 `SHOW PARTITIONS sales`，或讀 table format 的 metadata
（Iceberg 的 `sales.partitions`、Delta log），或列出物件儲存上的 `dt=…/` prefix。拿這份清單和
日曆比對。如果非讀資料不可，只選 partition 欄位，讓引擎從檔案 metadata 回答。

**追問 —— 是零營收還是 pipeline 失敗？** 資料表本身無法回答，所以這個檢查需要第二個來源：載入
日誌（11-04 的 job 成功了嗎？）、來源系統當天的列數，或其他地區當天都有銷售這個事實。把 pipeline
寫成成功的那天**一定**會產出一列 —— 包括零 —— 這樣缺一列就只可能代表失敗。

**追問 —— 把它做成資料品質檢查。** 在載入結束時把它當成一個會**讓 job 失敗**的斷言來執行
（一個 Airflow task、一個 dbt test、一個 Great Expectations 或 Soda 檢查），而不是一個要有人
記得去跑的查詢：

<!--CODE-->

---

<!-- 9d420d83b86d -->
## 8) 遞迴 CTE：階層資料 ⭐⭐⭐

<!-- 481948a6b904 -->
### 8-1) 某個主管底下所有直接與間接的部屬

**「遞迴 CTE：anchor 是那位主管，遞迴成員 join 主管已經在結果裡的員工，當某一輪沒有新增任何列
時就停止。我會防範環路，因為來源裡一筆壞資料就能讓它永遠跑下去。」**

<!--CODE-->

Lin（2）管理 Ned（4）與 Oli（5）。

- **它怎麼執行** —— anchor 產生第一個工作集；每一輪把工作集 join 到 `employees`，新的列成為下一個
  工作集；某一輪什麼都沒回傳時結束。用 `UNION ALL` 而不是 `UNION`，除非你要每一輪都去重。
- **環路** —— 如果 A 管 B、B 又管 A，遞迴永遠不會結束。`path` 陣列在第一次重複時就擋下；
  PostgreSQL 14+ 也有內建的 `CYCLE employee_id SET is_cycle USING path` 子句。只靠深度上限也能
  停下來，但會默默回傳錯的列，所以只把它當最後防線。
- **另一個方向** —— 往*上*到 CEO 的那條鏈，是把 join 反過來的同一個查詢
  （`e.employee_id = r.manager_id`）。
- **在哪裡能跑** —— PostgreSQL、MySQL 8、SQL Server、Snowflake、BigQuery、DuckDB，以及 4.1 之後的
  Spark（更早的 Spark 沒有遞迴 CTE，所以迴圈寫在 driver 裡，每一層一次 join）。又深又寬、經常被
  查詢的階層，最好一次攤平成一張 closure table `(ancestor, descendant, depth)`，問題就變成普通的
  join。

---

<!-- d4dcaa88a3fc -->
## 9) 查詢優化 ⭐⭐⭐⭐⭐

<!-- 624b4da9eba6 -->
### 9-1) Q9 —— 一個查詢要跑 30 分鐘，你怎麼調查？

**「改任何東西之前先量測。我讀實際的執行計畫 —— 資料庫用 `EXPLAIN ANALYZE`，Spark 用 Spark UI
的 SQL 分頁與 stage 檢視 —— 找出時間花在哪一個節點或 stage。然後它一定是五種情況之一：讀太多、
shuffle 太多、spill、skew，或者錯誤的估計選了錯的計畫。」**

| 計畫或 UI 中的症狀 | 診斷 | 修法 |
|---|---|---|
| scan 讀的列 / 位元組遠多於結果需要的 | 沒有 pruning、`SELECT *`、filter 沒有下推 | 對 partition 欄位過濾、選欄位、讓條件 sargable |
| join 或聚合 stage 的 shuffle read / write 很大 | 在搬大表 | 先聚合、broadcast 小的一邊、兩邊都做 bucketing |
| `Sort Method: external merge`、`Batches: 8`、Spark 的「spill (disk)」 | 運算子記憶體不足 | 每個運算子更多記憶體、更多 shuffle partition、餵進去的資料少一點 |
| 少數 task 比中位數慢 10 倍 | 熱門 key 造成 skew | AQE skew join、把熱門 key 隔離出來、salting |
| 估計列數與實際列數差很多 | 統計資訊過期或缺少 | `ANALYZE`、延伸統計、改寫條件 |
| 對大量輸入做 nested loop | join 演算法選錯，通常是估計錯誤造成 | 修正估計；在內側建索引 |

接著**一次只改一件事、重跑、比較**同樣的指標 —— 並說明證明修正有效的是執行計畫，而不是碼表。
也要先檢查無聊的原因：鎖等待（查詢不是慢，而是被擋住）、冷快取、叢集被另一個 job 共用。
[`sql_explain.md`](./sql_explain.md) 逐節點走完一次完整的診斷。

<!-- 027eb8b9ecf2 -->
### 9-2) Q10 —— Partition pruning vs predicate pushdown？

**「兩者都讓引擎少讀一點，但在不同層級。Partition pruning 用 partition key 跳過整個 partition ——
目錄或檔案 —— 在打開任何檔案之前。Predicate pushdown 把 filter 交給儲存層，讓它在真正打開的檔案
內跳過 row group 或列。」**

| | Partition pruning | Predicate pushdown |
|---|---|---|
| 作用於 | partition 欄位（`dt=2026-11-01/`） | 檔案格式有保存統計資訊的任何欄位 |
| 跳過 | 整個 partition，光靠 metadata | 依 min / max 跳過 Parquet / ORC 的 row group、在來源端過濾列（JDBC 的 `WHERE`） |
| 需要 | 對 partition 欄位本身的過濾 | reader 看得懂的簡單比較 |
| 被破壞於 | 欄位外包了函式：`WHERE YEAR(dt) = 2026` | UDF、reader 無法下推的轉型、排序很差的資料（每個 row group 都涵蓋所有值） |

<!--CODE-->

第二種寫法也讓資料庫無法使用 `order_date` 上的索引 —— 它不是 **sargable**。兩者都能在計畫裡
確認：Spark 的 scan 節點會列出 `PartitionFilters` 與 `PushedFilters`；PostgreSQL 會顯示它保留了
哪些 partition，以及 filter 是 `Index Cond` 還是 `Filter`。

<!-- 148dc35e9b03 -->
### 9-3) Q11 —— 為什麼 `SELECT *` 會傷害效能？

**「在 columnar 格式上，你只需要三個欄位，它卻讀了每一個欄位 —— I/O 是以欄位為單位，所以成本和
選了多少欄位成正比。它也讓每次 shuffle 搬更多資料、用更多記憶體、讓 index-only scan 無法使用，
而且 schema 一改就壞。」**

- **I/O** —— Parquet、ORC、BigQuery 與 Snowflake 把每個欄位分開存；**column pruning** 只讀被引用
  的欄位。BigQuery 依掃描的位元組計費，所以 `SELECT *` 就是帳單本身。
- **網路與記憶體** —— 每個多出來的欄位都會被帶過每一次 shuffle、sort buffer 與 hash table，
  spill 就是從這裡來的。
- **索引** —— 只有當每個被選的欄位都在索引裡時，row store 才能只靠索引回答（index-only scan）。
- **正確性** —— 上游新增一個欄位就改變了結果的形狀；`INSERT … SELECT *` 會壞掉，更糟的是默默
  錯位欄位。

在資料庫上做探索性的 `LIMIT 10` 沒問題；在 pipeline 或 view 裡絕對不要。

<!-- 61474bf461aa -->
### 9-4) Q12 —— 什麼時候用 CTE 而不是子查詢？

**「為了可讀性幾乎都用 —— CTE 為每一步命名、可以被引用兩次，而且是寫遞迴的唯一方式。在效能上
通常一樣，因為現代 optimizer 會像子查詢一樣把 CTE 內聯；例外的情況才是我要提的。」**

| 引擎 | CTE 會怎麼處理 |
|---|---|
| PostgreSQL 12+ | 只引用一次就內聯；引用兩次就 materialize，除非寫 `NOT MATERIALIZED` |
| PostgreSQL ≤ 11 | 一律 materialize —— 一道*優化屏障*：外面的 filter 不會被推進去 |
| Spark SQL | 內聯；引用兩次的 CTE 會**算兩次**，除非計畫重用了 exchange |
| BigQuery、Snowflake | 一般會內聯；用好幾次的 CTE 可能每次都重算 |

所以：為了清楚而用 CTE；當一個昂貴的 CTE 被引用好幾次時，檢查計畫並刻意把它 materialize ——
temp table、`MATERIALIZED`，或 Spark 的 `df.cache()`。**相關**子查詢是另一回事（§9-10）。

<!-- 3a3c2c323283 -->
### 9-5) Q13 —— 有很多 join 的查詢怎麼優化？

**「每個輸入在 join 之前先縮小，最有篩選力的表先 join 讓中間結果保持小，確保沒有 join 會 fan-out，
其餘的順序交給 optimizer —— 而這需要好的統計資訊。」**

1. **儘早過濾** —— 把每個 filter 推進讀那張表的 CTE 或子查詢，而不是在最後一個 join 之後。
   optimizer 會下推大部分 filter，但不會越過 outer join 或 window function。
2. **儘早聚合** —— 在 join dimension 之前，先把 fact 聚合到結果需要的粒度（§3-1）。
3. **有篩選力的 join 先做** —— 能把 10 億列砍到 100 萬列的 join，應該在保留全部列的 join 之前執行。
   optimizer 依**基數估計**排 join 順序，所以過期的統計資訊會給出壞的順序；要更新它（`ANALYZE`，
   Spark 的 `ANALYZE TABLE … COMPUTE STATISTICS`，cost-based optimizer 需要它）。
4. **不要 fan-out** —— 每個 dimension 在它的 join key 上唯一（§3-3）。
5. **Broadcast 小的 dimension** —— 在 Spark 中，有好幾個小 dimension 的星型 join 會各自 broadcast，
   fact 只 shuffle 一次或完全不 shuffle。
6. **檢查計畫** —— join 演算法、順序，以及每次 join 之後的列數。

<!-- 538103c39cb8 -->
### 9-6) Q14 —— `GROUP BY` 或 `JOIN` 中的 data skew 是怎麼造成的？

**「Hash partitioning 會把同一個 key 的所有列送到同一個 task，所以一個熱門 key —— NULL、預設值、
一個超大客戶 —— 會讓大部分資料落在一個 task 上，其他 task 做完只能等它。」**

| 原因 | 例子 | 修法 |
|---|---|---|
| NULL 或哨兵 key | 每筆訪客訂單都是 `customer_id IS NULL` | 過濾掉，或另外 join |
| 真正的熱門 key | 一個賣家佔了 30 % 的訂單 | AQE skew join；把熱門 key 拆出來用 broadcast join；salting |
| 分區方式不好 | shuffle partition 太少，或對低基數欄位 `repartition(col)` | 增加 partition；AQE coalescing |
| 輸入檔案有 skew | 一個超大的 gzip 檔（無法切割） | 用可切割的格式；讀取後 repartition |

對 `GROUP BY` 來說，因為有**部分聚合** —— 每個 task 在 shuffle 前先在本地把熱門 key 預聚合 ——
skew 通常無害。它在聚合不可分解時（`COUNT(DISTINCT)`、`collect_list`、percentile）以及在 join 上
才會咬人。

**Salting**，手動修法：在大的一邊為熱門 key 附加一個 `0..N-1` 的隨機數，把小的一邊對應的列複製
`N` 份、各帶一個 salt，然後依 `(key, salt)` join。熱門 key 的列現在分散到 `N` 個 task 上，代價是
小的一邊那些列有 `N` 份副本。

<!--CODE-->

在 Spark 3 中先試 AQE（`spark.sql.adaptive.skewJoin.enabled`，預設開啟），它會在執行期切開過大的
shuffle partition，不用改查詢。

<!-- 062d51403f52 -->
### 9-7) Q15 —— 索引怎麼運作？什麼時候沒有幫助？

**「B-tree 索引把欄位值排序存好，並附上指向每一列的指標，所以查找一個值或一段窄範圍是
`O(log n)` 加上符合的列數，而不是全表掃描。當查詢符合表中很大一部分、條件用不上它時，它就沒有
幫助，而且它在寫入時永遠有成本。」**

索引在這些情況**沒有**幫助：

- **過濾條件沒有篩選力** —— 符合的列超過幾個百分比時，隨機讀索引比一次循序掃描還貴，planner
  正確地選擇掃描。
- **條件不是 sargable** —— 欄位外包了函式或轉型（`LOWER(email) = …`、`created_at::date = …`）、
  前綴萬用字元（`LIKE '%son'`）、或隱式型別轉換。表達式索引（`ON (LOWER(email))`）能修第一種。
- **欄位不是索引的第一個欄位** —— `(country, city)` 上的索引幫不了 `WHERE city = …`。
- **表很小**，或統計資訊過期讓 planner 以為它很小。
- **工作負載是分析型的** —— 在 columnar 資料倉儲上掃描幾十億列。那些引擎沒有 B-tree 索引；對應的
  東西是 partitioning、clustering / sort key 與 zone map（每個區塊的 min / max）。

成本：每個 `INSERT`、`UPDATE` 與 `DELETE` 都要維護每一個索引，索引佔磁碟與記憶體，而大量載入時
常常先刪掉索引、之後再重建比較快。**覆蓋**索引（`INCLUDE (amount)`）讓 index-only scan 成為可能。

<!-- 82598aba67b3 -->
### 9-8) Q16 —— 一個查詢掃描好幾 TB，怎麼讓它便宜一點？

**「先少讀：修剪 partition、修剪欄位、把 filter 推進檔案。再存得更好：大小合理的 columnar 檔案，
依過濾欄位排序或 clustering。然後少算：把每天都被查的東西預先聚合。」**

| 手段 | 作用 | 例子 |
|---|---|---|
| Partition pruning | 跳過整天 / 整區 | 對依 `dt` 分區的表下 `WHERE dt BETWEEN …` |
| Column pruning | 只讀被引用的欄位 | 不用 `SELECT *` |
| Clustering / 排序 / Z-order | 讓 min / max 統計有篩選力，從而跳過 row group | Delta `ZORDER BY (customer_id)`、BigQuery clustering、Snowflake clustering key |
| Columnar 格式 + 壓縮 | 每個值的位元組更少 | 用 Parquet / ORC 搭配 Zstd 或 Snappy，而不是 CSV / JSON |
| 健康的檔案大小 | 要開啟與規劃的檔案更少 | 把大量小檔案壓實成約 128 MB–1 GB |
| 預聚合 | dashboard 讀摘要而不是 fact | 每日聚合表或 materialized view |
| 近似 | 用 sketch 取代精確的 distinct | `approx_count_distinct` |
| 增量處理 | 每次執行只讀新的 partition | §10-1 |

然後**證明它**：比較前後掃描的位元組數（BigQuery 的 job 資訊、Snowflake 的 query profile、Spark 的
scan 指標）。[`data_performance.md`](./data_performance.md) 是完整版。

<!-- 4d018b3eb59f -->
### 9-9) 一個 Spark SQL job：大部分 task 花 2 分鐘，少數花 20 分鐘

> 一個 Spark SQL job 跑了 40 分鐘。某個 stage 裡大部分 task 2 分鐘內完成，少數花了 20 分鐘，而且
> 有一個 stage 的 shuffle read 特別大。是 skew、partition 數不對，還是 join 策略錯了 —— 你怎麼分辨？

**「看 task 時間的分布。少數慢 task 的 shuffle read 遠多於中位數，就是 skew。每個 task 都慢，或某個
stage 的 task 很少，是 partition 數的問題。一邊小到可以 broadcast 卻用了 sort-merge join，是策略
問題。我依這個順序檢查，然後才選修法。」**

1. **Spark UI 的 stage 與 task 檢視**：task 摘要的**最大值 vs 中位數**執行時間。長尾（20 分鐘對
   2 分鐘）指向 skew 或 straggler；整體一樣慢則不是。
2. **比較每個 task 的 shuffle read、記錄數與 spill。** 如果慢的 task 讀的記錄數也是 10 倍，那就是
   **data skew**。如果它們讀的和其他 task 一樣多，那就是 **straggler** —— 壞掉的節點、GC 暫停
   （看 GC time）或慢的磁碟 —— 開啟 speculation（`spark.speculation`）有幫助。
3. **找出熱門 key**，即該 stage 的 join 或 group key：
   `SELECT key, COUNT(*) FROM t GROUP BY key ORDER BY 2 DESC LIMIT 20`。先看 NULL 與哨兵值。
4. **讀實體計畫**（`EXPLAIN FORMATTED`，或 SQL 分頁）：是 `SortMergeJoin` 還是
   `BroadcastHashJoin`？AQE 有沒有介入（`AQEShuffleRead`、`skew=true`）？兩邊的輸入大小各是多少？
5. **決定**：

| 證據 | 診斷 | 修法 |
|---|---|---|
| 少數 task 拿到大部分記錄；有熱門 key | data skew | AQE skew join；過濾或另外處理 NULL key；broadcast；salting |
| task 都很大而且 spill；例如 2 TB 只有 200 個 partition | shuffle partition 太少 | 提高 `spark.sql.shuffle.partitions`，或從一個大的數字讓 AQE coalesce；目標每個 partition 約 100–200 MB |
| 上千個小 task，排程開銷為主 | partition 太多 | AQE coalescing；減少 partition |
| 一邊很小卻用了 sort-merge join | join 策略錯誤 | broadcast 它（hint，或提高門檻），更新統計資訊讓 optimizer 看到大小 |
| 慢的 task 讀的不比別人多 | straggler | 檢查 executor 的 GC 與節點；開啟 speculation |

這題順勢會帶出的面試建議：如果你在真實 job 上做過這件事，說出你實際做了其中哪幾項、前後量到了
什麼，並把它和你*也會*考慮的修法分開講。

<!-- d4c42c31901f -->
### 9-10) 要點名的兩個反模式：相關子查詢，以及在 partition 欄位上套函式

**相關子查詢** —— 引用了外層列的子查詢，邏輯上每一列都要執行一次：

<!--CODE-->

許多 optimizer 會把簡單的情況自動**去相關**成 join（Spark、Snowflake、DuckDB、SQL Server）；
PostgreSQL 對 `EXISTS` / `IN` 會這麼做，但 `SELECT` 清單裡的純量子查詢會以每列一次的 SubPlan 執行。
改寫永遠不會更慢，也不依賴 optimizer 夠聰明 —— 而「每列的前一個值」這種相關子查詢，用 `LAG` 更好。

**在 partition 或有索引的欄位上套函式** —— `WHERE YEAR(created_at) = 2026` 無法修剪 partition，
也用不上索引；`WHERE created_at >= '2026-01-01' AND created_at < '2027-01-01'` 可以（§9-2）。
半開區間對時間戳也才正確，`BETWEEN … AND '2026-12-31'` 會漏掉最後一天午夜之後的資料。

---

<!-- 58bd73ad82b5 -->
## 10) 增量 ETL 與資料品質 ⭐⭐⭐⭐⭐

<!-- bcdd7ad85d64 -->
### 10-1) Q7 —— 只處理上次執行之後變動的資料

> 每天讀取來源表自上次執行以來新增或更新的列。

**「用 high-water mark：存下上一次成功執行處理過的最大變動標記，讀高於它的列 —— 再減去一段給遲到
資料的回看 —— 去重成每個 key 一個版本，然後 `MERGE` 進目標表。`MERGE` 讓重跑無害，而 watermark
只在目標表 commit 之後才前進。」**

<!--CODE-->

訂單 1 最後是 `refunded`；訂單 2 最後是 55，因為版本順序是 `(updated_at, change_seq)` —— 只靠
時間戳會任選其中一筆 11:00 的列，而嚴格的 `updated_at >` 會在下一次執行時拒絕同一時間戳的更正。

**Watermark 取自 staging 表，而不是再讀一次 `src_orders`。** 在 PostgreSQL 預設的 `READ COMMITTED`
下，即使在同一個 transaction 裡，每個語句看到的也是新的 snapshot：在 `MERGE` 與 `UPDATE` 之間 commit
進來源的列，會把 watermark 推過一筆 `MERGE` 從沒讀到的列，下一次執行就永遠跳過它。把批次放進 staging
讓兩者使用同一個 snapshot。在 lakehouse 上目標表與 watermark 位於不同系統，保證改由 `MERGE` 的冪等性
來提供。

同一個 job 在 production 上的樣子：

<!--CODE-->

這筆稽核紀錄讓之後的對帳成為可能（§10-3）。而且光靠 `MERGE` 並不能讓整條 pipeline 做到
exactly-once：它還需要去重過的來源、transactional 的寫入、不能有兩個 job 同時 merge 進同一張表，
以及在 commit 之後才更新 watermark。

**追問 —— watermark 怎麼定義？** 定義為**來自來源的變動標記**，絕不是 job 自己的時鐘：來源的
`updated_at`，更好的是單調遞增的序號 —— CDC 日誌位置（LSN、binlog offset）、Kafka offset、只追加
表的自動遞增 id。把它存在依 job 為 key 的控制表裡，設成**實際讀到的**最大值，而且只有當標記唯一時
才用嚴格比較（`>`）；用時間戳時，很多列可能剛好落在邊界值上，所以用 `>=` 讀（或用回看），讓
`MERGE` 吸收重讀的列。兩個經典陷阱：在長時間來源 transaction **開始時**設定的 `updated_at`，會在
時間戳較晚的列之*後*才 commit，於是嚴格的 `>` 會漏掉它；以及應用程式在某條程式路徑上忘了更新的
欄位，那些變動永遠不會被看到 —— 這就是 log-based CDC 勝過時間戳欄位的原因。

**追問 —— 資料遲到了怎麼辦？** 依遲到多久來選，並說出取捨：

| 做法 | 怎麼做 | 成本 |
|---|---|---|
| 回看 window | 每次從 `watermark − 1 hour`（或 − 3 天）開始讀 | 重讀列；因為 `MERGE` 冪等所以無害 |
| 重新處理事件時間 partition | 每次重算最近 N 天的 partition | 簡單；每天花 N 天的運算 |
| 把遲到的列送到它的 partition | 遲到的列更新它事件日期的 partition | 之後的報表會變；下游需要知道 |
| 截止點 | N 天後該期間關帳；之後的列歸到調整項 | 報表穩定，明確地是近似值 |

回看只能涵蓋它長度以內的遲到。更晚的資料必須透過定期與來源對帳，或透過完全不依賴時間戳的 CDC 補回來。

要分清 **到達時間**（列抵達我們的時間，驅動 watermark）與 **事件時間**（事情發生的時間，決定
partition 與指標）。

**追問 —— job 失敗重跑，怎麼避免重複？** 讓每次寫入都**冪等**：依業務 key 做 `MERGE`，或對整個
partition 做 `INSERT OVERWRITE` / 刪除再插入 —— 絕不盲目 `INSERT`。在寫入 commit **之後**才推進
watermark，能的話放在同一個 transaction；如果 job 在兩者之間掛掉，重跑會讀同一個批次，`MERGE`
改寫同樣的列。然後驗證：每次執行後對 key 做唯一性檢查（§10-3）。

**追問 —— 同一個 key 在同一批次裡有好幾次更新？** 在 `MERGE` **之前**依來源的順序保留最新的
（`ROW_NUMBER() … ORDER BY updated_at DESC, seq DESC`），如上 —— 否則 `MERGE` 會因為一筆目標列
對到兩筆來源列而失敗，或在允許的引擎上以任意順序套用。如果目標表需要每個版本（歷史表、SCD 2），
就全部保留並為它們排序（§10-2）。

**追問 —— 寫出 `MERGE`。** 就是上面那個語句，再加上來源是帶操作欄位的 CDC 時的刪除分支：
`WHEN MATCHED AND s.op = 'D' THEN DELETE`。`ON` 條件只放 key，把「是否較新」的判斷放在 `WHEN`
子句；在分區的 lakehouse 表上，把 partition 範圍加進 `ON`，讓 `MERGE` 只讀被碰到的 partition。

<!-- c7f1256a1e24 -->
### 10-2) Q8 —— SCD Type 2：保留客戶住過的每一個地址

**「每次變動都關閉目前那一列 —— 設定 `valid_to` 與 `is_current = false` —— 並插入新的目前列。
兩個語句放在同一個 transaction 裡，兩者都只碰追蹤屬性真的有變的客戶，而目前列上的唯一約束讓
重複執行變得不可能，而不只是不太可能。」**

**追問 —— Type 1 vs Type 2？**

| | SCD Type 1 | SCD Type 2 |
|---|---|---|
| 變動時 | 覆寫屬性 | 關閉舊列、插入新版本 |
| 歷史 | 遺失 | 每個版本，帶 `valid_from` / `valid_to` |
| 每個 key 的列數 | 一列 | 每個版本一列，恰好一列是目前的 |
| Key | 自然鍵 | 每個版本一個代理鍵（fact 參照版本） |
| 用於 | 更正、沒人做歷史報表的屬性 | 會改變過去 fact 意義的屬性：地址、客群、價格級距 |

Type 3 保留一個「前一個值」欄位；真實的表通常依欄位混用不同類型。

<!--CODE-->

執行之後，客戶 101 有一列已關閉的 Taipei（`valid_to` 2025-07-01）與一列目前的 Tokyo，102 不變，
103 是新的，而 104 的 Incheon 變動留在 `stg_late` 等待歷史重建，Busan 仍是目前列。`BEGIN` / `COMMIT`
很重要：在 autocommit 模式下關閉會自己 commit，插入一旦失敗，客戶就沒有目前列了。（真實的表要用
sequence 或 identity 欄位當 `customer_sk`；`MAX + ROW_NUMBER` 只是讓範例可攜。）

「每個客戶至多一筆目前列」這個不變式由資料庫強制，而不是靠上面的小心：

<!--CODE-->

**追問 —— 新版本到達時怎麼處理？** 就像上面：步驟 2 關閉，步驟 3 插入。步驟 3 的
`NOT EXISTS … is_current` 用一個語句同時接住步驟 2 剛關閉的客戶與全新的客戶。順序很重要 —— 先關閉，
再插入。在 Delta 或 Snowflake 上，同樣的事常寫成**一個 `MERGE`**：來源是 staging 的列，再 union
上每一筆有變動的列、以 `NULL` 當 merge key 的副本，讓有變動的客戶在同一個原子語句裡既對得上
（關閉）又對不上（插入）。

**追問 —— 重跑不能產生兩筆目前列。** 三層防護：

1. **語句本身冪等。** 用同樣的 staging 資料重跑：步驟 2 發現 Tokyo 是目前列且與 staging 相同，
   所以什麼都不關；步驟 3 發現已有目前列，所以什麼都不插入。
2. **一個 transaction** 包住兩個語句，所以兩者之間的失敗不會留下一列已關閉卻沒有後繼的列。
3. **讓壞狀態不可能發生的約束** —— 上面的 partial unique index。沒有這種約束的資料倉儲在載入後
   執行下面的驗證檢查，並讓 job 失敗。

比較時用 `IS DISTINCT FROM`（null-safe）—— 任一邊為 NULL 時 `address <> s.address` 是 `UNKNOWN`，
所以變成 NULL 或從 NULL 變回來的變動會被默默漏掉。追蹤的欄位很多時，比較**追蹤欄位的 hash**，而不是逐欄比較。

**追問 —— 怎麼驗證歷史？** 三個檢查，只有 dimension 壞掉時才回傳列：

<!--CODE-->

**追問 —— 同一天有好幾次變動？** 先和業務方決定粒度：

- **日粒度** —— 只算當天最後一次變動。載入前把 staging 去重成每個客戶每天一列
  （`ROW_NUMBER() … ORDER BY change_ts DESC`）。
- **每次變動** —— 讓 `valid_from` / `valid_to` 變成時間戳，依時間戳（tie 時再依序號）排序批次裡
  的變動，用 `LEAD` 把每個版本的 `valid_to` 設為下一個版本的 `valid_from`；然後在批次第一次變動時
  關閉既有的目前列，並插入整條鏈。
- **遲到的變動**比目前版本還舊時，必須**插入到歷史中間**：切開它所落入的那個版本，這會改寫它前一個
  版本的 `valid_to`。從完整的變動日誌用 `LEAD` 重建一個 key 所有區間的批次 job，能乾淨地處理這種
  情況（[`sql_for_data.md`](./sql_for_data.md) §7-14）。

用**半開區間** —— `valid_from <= t AND (t < valid_to OR valid_to IS NULL)` —— 並讓 `valid_to`
等於下一個版本的 `valid_from`，而不是「前一天」。題目的範例表把 Taipei 關在 2025-06-30（含）；
對日期可行，但用時間戳時含尾端的區間會留下空隙，所以要說明你採用哪種慣例。用遙遠未來的
`valid_to`（`9999-12-31`）取代 `NULL`，能讓條件變成單純的 `BETWEEN` 式範圍。

<!-- f9bf8f3cf357 -->
### 10-3) 對帳：證明一次載入是正確的

**「每次載入後，我在列數、加總與 key 上比較目標與來源，並執行描述這張表的斷言 —— key 唯一、
key 沒有 NULL、值在範圍內。每個檢查只有出錯時才回傳列，所以 orchestrator 可以讓這次執行失敗。」**

<!--CODE-->

常用的一組：每個 partition 的列數對來源、金額欄位的加總、key 的唯一性與參照完整性（每筆 fact 的
key 都在它的 dimension 裡）、新鮮度（最新的時間戳夠近），以及相對於近期平均的資料量（一天只有平常
10 % 的量就是失敗，即使上面每個檢查都通過）。[`batch_pipeline.md`](./batch_pipeline.md) 與
[`sql_for_data.md`](./sql_for_data.md) §7-16 有更多。

---

<!-- 756e86948c84 -->
## 11) 效能速查表 ⭐⭐⭐⭐

| 技術 | 解決什麼 | 要注意 |
|---|---|---|
| Partition pruning | 掃描查詢用不到的 partition | filter 必須在 partition 欄位上，而且沒有包函式 |
| Predicate pushdown | 讀到不符合 filter 的列 / row group | 取決於格式、reader 以及資料如何排序 |
| Column pruning | 讀沒人選的欄位 | `SELECT *` 會讓它失效 |
| Broadcast join | 為了 join 一張小表而 shuffle 大表 | 小的一邊必須放得進每個 executor 的記憶體 |
| Sort-merge join | join 兩張大表 | 兩邊都要 shuffle 與排序；對 skew 敏感 |
| Bucketing | 每天重複 shuffle 同一個 join | 兩邊都依 key 做 bucketing 且 bucket 數相容；依引擎而定 |
| Materialized view | 每次查詢都重算同一個聚合 | refresh 成本與資料新鮮度 |
| 近似聚合 | 精確 distinct count 的成本 | 答案是近似值（~1–2 %）；不能用在金額 |

三組常以一句話問答出現的對比：

- **`WHERE` vs `HAVING`** —— `WHERE` 在分組前過濾列，不能用聚合；`HAVING` 在分組後過濾群組。
  對一般欄位的條件屬於 `WHERE`，它在那裡也能在聚合前先砍掉資料。
- **`UNION` vs `UNION ALL`** —— `UNION` 會去重，代價是對整個結果做排序或 hash；`UNION ALL` 保留
  每一列。預設用 `UNION ALL`，要去重時刻意去重。
- **`ROWS` vs `RANGE`** —— `ROWS` 依實體列數框定 frame；`RANGE` 依排序值，並把相等的值當成同一個
  peer 群組（§5-2）。

---

<!-- 2537e3b04cd3 -->
## 12) 練習計畫 ⭐⭐⭐

資深 DE 面試的**優先順序**：

1. **SQL coding** —— window function、去重、session、滾動聚合、多表 join。
2. **SQL 優化** —— 讀執行計畫、join 策略、partition pruning、skew 與 shuffle。
3. **資料工程 SQL** —— 增量載入、CDC 與 `MERGE`、SCD 1 / 2、資料品質與對帳。
4. **資料庫基礎** —— 索引、transaction 與 ACID、隔離等級、正規化 vs 反正規化
   （[`sql_vs_nosql.md`](./sql_vs_nosql.md)）。

**要能不看資料寫出來的十道題**，每一題上面都有解答：

| # | 題目 | 技巧 | 解答在 |
|---|---|---|---|
| 1 | 每個使用者最新的一筆事件 | `ROW_NUMBER` + tie-breaker | §4-1 |
| 2 | 連續登入三天的使用者 | gaps and islands | §7-1 |
| 3 | 每個部門薪資前三名 | `DENSE_RANK` | §5-1 |
| 4 | 每日營收與 7 日移動平均 | window frame + 日期序列 | §5-2 |
| 5 | 沒有對應紀錄的資料 | `NOT EXISTS` | §3-2 |
| 6 | DAU / WAU / MAU | 範圍上的 `COUNT(DISTINCT)` | §6-3 |
| 7 | 重複交易，保留最新版本 | 去重 | §4-1、§4-2 |
| 8 | 每個使用者的首次與最近購買 | `MIN` / `MAX` | §6-4 |
| 9 | 30 分鐘逾時的 session | `LAG` + 累計 `SUM` | §7-2 |
| 10 | 把每日資料轉成每月報表 | 條件聚合 | §6-5 |

每一題都要大聲演練 §1 的四個問題：*1 TB 呢？為什麼慢？你怎麼知道它是對的？重跑會怎樣？*

**Q1–Q8 一覽** —— 每題考什麼，以及資深答案會用到的關鍵字：

| 題目 | 考點 | 資深關鍵字 |
|---|---|---|
| Q1 大表 join（§3-1） | join 優化 | 先聚合、shuffle、skew、broadcast |
| Q2 去重（§4-1） | 保留最新版本 | `ROW_NUMBER`、確定性的 tie-breaker |
| Q3 Anti-join（§3-2） | 找出不存在的資料 | `NOT EXISTS`、`NOT IN` 的 NULL 陷阱 |
| Q4 排名（§5-1） | 排名與 tie | `RANK`、`DENSE_RANK`、薪資級距 vs 人 |
| Q5 移動平均（§5-2） | window 聚合 | `ROWS` vs `RANGE`、日曆 |
| Q6 轉換率（§6-1） | 條件聚合 | `COUNT(DISTINCT)`、分子分母同一群人 |
| Q7 增量 ETL（§10-1） | 只載入變動 | CDC、watermark、`MERGE`、冪等性 |
| Q8 SCD Type 2（§10-2） | 保留歷史 | 有效區間、唯一的目前列、重跑 |

時間不夠時，先練熟 **Q1、Q2、Q5、Q7 與 Q8**：合起來涵蓋大規模 Spark 處理、ETL 去重、window function、
production pipeline 與歷史建模。每一題都要能說出：為什麼選這個 pattern、正確性怎麼保證、十億列時瓶頸
在哪、重試或遲到資料時結果是否仍正確，以及你會怎麼驗證 —— 執行計畫、對帳、指標。

**去哪裡練習**：

- 用 [LeetCode SQL 50](https://leetcode.com/studyplan/top-sql-50/) 打基礎，再做 Hard 的資料庫題：
  [262 Trips and Users](https://leetcode.com/problems/trips-and-users/)、
  [601 Human Traffic of Stadium](https://leetcode.com/problems/human-traffic-of-stadium/)、
  [185 Department Top Three Salaries](https://leetcode.com/problems/department-top-three-salaries/)。
- [DataLemur](https://datalemur.com/) 與 [StrataScratch](https://www.stratascratch.com/) 提供各大
  公司問過、更接近真實產品資料的題目。
- 用 [Mode SQL tutorial](https://mode.com/sql-tutorial/) 複習分析型 SQL 與 window function。

---

<!-- 928e1847ed4d -->
## 13) 用英文說出來：Q1–Q8 ⭐⭐⭐⭐

每一題的答案，長度就是追問開始之前你會說出口的份量。面試用英文作答，所以這一節刻意保留英文原文，
照著練；每一句背後的細節都在上面的小節裡。

**Q1 —— 大表 join**

> I would first reduce the input by filtering and pre-aggregating the fact table to one row per
> customer. Then I would read the execution plan to see the join strategy and the shuffle. With
> two large tables I expect a sort-merge join; I would check the join key for skew and use
> adaptive query execution or salting if one key dominates.

**Q2 —— 去重**

> I would use `ROW_NUMBER` partitioned by `event_id`, ordered by `updated_at` descending, and
> keep row number one. To make the result deterministic I add a stable tie-breaker such as a
> source sequence. At a billion rows the cost is the shuffle and sort by `event_id`, so for
> continuous data I dedupe each new batch and `MERGE` it into an already-unique table.

**Q3 —— 沒有訂單的客戶**

> I would use `NOT EXISTS`, or a left anti join, because it states the requirement directly and
> is correct when the subquery has NULLs. I avoid `NOT IN`: one NULL in the subquery makes it
> return no rows. And I would compare the execution plans rather than assume one form is faster.

**Q4 —— 第二高薪水**

> I would use `DENSE_RANK` partitioned by department, ordered by salary descending, and keep rank
> two. That handles ties when the requirement is the second-highest distinct salary. I would
> first confirm whether the business means the second distinct salary level or the employee in
> second place, because ties give different answers.

**Q5 —— 移動平均**

> I would aggregate to one row per date, join it to a calendar table to fill missing dates —
> using zero only if a missing date really means zero revenue — and then apply a seven-row
> window over the complete series. That avoids confusing seven records with seven calendar days.

**Q6 —— 轉換率**

> I would use conditional aggregation with `COUNT(DISTINCT user_id)` per event type and `NULLIF`
> to avoid dividing by zero. More importantly, I would clarify the definition: if a conversion
> means a user who visited and purchased on the same day, I aggregate to the user-day level
> first so that the numerator and the denominator are the same cohort.

**Q7 —— 增量處理**

> I would read changes above a watermark taken from a reliable change timestamp or CDC offset,
> with a lookback for late data. I load them into staging, deduplicate and validate them, then
> `MERGE` into the target so a retry rewrites the same rows. The watermark advances only after
> the write commits, and every batch records its row counts for reconciliation.

**Q8 —— SCD Type 2**

> When a tracked attribute changes, I expire the current record by setting its end date and
> inserting a new current version, both in one transaction. I handle duplicate source events,
> late-arriving updates and retries, and I validate that every customer has exactly one current
> record and that the validity periods neither overlap nor leave gaps.
