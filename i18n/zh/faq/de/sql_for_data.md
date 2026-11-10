<!-- 1e78c607a9cf -->
# 資料工程用 SQL

> **範圍** —— 資料工程面試所考的分析型 SQL：查詢如何被求值、join 與其演算法、NULL 語意、彙總、window function、CTE 與遞迴、經典面試查詢題型、MERGE 與 upsert、日期與時區、查詢效能，以及 PostgreSQL、BigQuery、Snowflake、Spark SQL 之間的方言差異。資料表設計歸 [schema_design.md](./schema_design.md)；選哪種資料庫歸 [sql_vs_nosql.md](./sql_vs_nosql.md)。
> **另見**：[`../faq_DE_interview.md`](../faq_DE_interview.md) —— 總覽與考前檢查清單；[`./data_modeling.md`](./data_modeling.md) —— 這些查詢所跑的星型模型；[`./spark.md`](./spark.md) —— Spark SQL 如何執行它們；[`../sql/faq_sql.md`](../sql/faq_sql.md) —— 較舊、較長的 SQL 筆記。

除非 code block 的第一行寫了別的方言，否則每個查詢都是 **PostgreSQL**。每一節都先給你會說出口的那句話，
再講機制，最後是面試官接著會問的取捨。

---

<!-- 5f6f084168e6 -->
## 0) 範例資料表

下面每個查詢都跑在這四張表上。面試官通常會給你非常接近的表，
所以值得先認得這些形狀。

<!--CODE-->

---

<!-- 25a7b1e22c50 -->
## 1) 查詢如何被求值 ⭐⭐⭐⭐⭐

<!-- 62f0beb3e2b4 -->
### 1-1) 邏輯執行順序

**SQL 寫的時候 `SELECT` 在最前面，求值時卻是 `FROM` 先 —— 幾乎所有「為什麼這樣寫不行」的問題，
都能用下面的順序回答。**

<!--CODE-->

你應該能說出的推論：

- `SELECT` 的別名不能用在 `WHERE` —— `WHERE` 在 `SELECT` 之前執行。（Snowflake 是例外：
  它會在 `WHERE` 裡解析別名。）
- 別名**可以**用在 `ORDER BY`，而 PostgreSQL、BigQuery、Snowflake、Spark 也以擴充語法
  允許在 `GROUP BY` 使用別名。
- 彙總函式不能出現在 `WHERE`；要在 `HAVING` 裡過濾它。
- Window function 不能出現在 `WHERE`、`GROUP BY` 或 `HAVING`，因為它在這些之後才計算。
  把查詢包進 CTE，或使用 `QUALIFY`。
- 這是**邏輯**順序。最佳化器可以在實體上重排 —— 把 filter 推到 join 之下、在 join 之前先彙總 ——
  只要結果相同。

<!-- 7dad652100fe -->
### 1-2) `WHERE` vs `HAVING` vs `QUALIFY`

| 子句 | 過濾對象 | 可以引用 | 執行時機 |
|---|---|---|---|
| `WHERE` | 輸入的列 | `FROM` 列集合中的欄位 | 分組之前 |
| `HAVING` | 群組 | 彙總結果與分組欄位 | 分組之後 |
| `QUALIFY` | 輸出的列 | window function 的結果 | window 之後 |

<!--CODE-->

把列條件放在 `HAVING` 而不是 `WHERE`，對分組欄位來說是合法的，但會先把每一列都分組 ——
永遠盡早過濾。

---

<!-- 2b4c98da2709 -->
## 2) Join ⭐⭐⭐⭐⭐

<!-- a960c49f9be4 -->
### 2-1) Join 的類型

**Join 依條件把兩張表的列配對；類型決定找不到配對的列會怎樣。**

| Join | 保留 | 沒配到的一側填入 |
|---|---|---|
| `INNER JOIN` | 只有配對成功的 | — |
| `LEFT [OUTER] JOIN` | 左表每一列 | 右側為 `NULL` |
| `RIGHT [OUTER] JOIN` | 右表每一列 | 左側為 `NULL` |
| `FULL [OUTER] JOIN` | 兩邊的每一列 | 缺的那側為 `NULL` |
| `CROSS JOIN` | 每一種組合（笛卡兒積） | — |
| semi-join（`EXISTS`、`IN`） | 有配對的左表列，每列一次 | — |
| anti-join（`NOT EXISTS`） | **沒有**配對的左表列 | — |

- `RIGHT JOIN` 就是把兩張表對調的 `LEFT JOIN`；多數風格指南只用 `LEFT`。
- 對 `LEFT JOIN` 來說，**右表**的條件要放在 `ON`。放在 `WHERE` 會把補 `NULL` 的列刪掉，
  默默把它變成 inner join：

<!--CODE-->

<!-- 95a04bb1f44d -->
### 2-2) Semi-join 與 anti-join

**`EXISTS` 問的是「至少有一筆配對嗎？」，每個左表列只回傳一次；一般的 join 每配到一筆就回傳一列，
可能讓左表重複。**

<!--CODE-->

- `NOT EXISTS` 是安全的預設：子查詢含 `NULL` 時它仍然正確，`NOT IN` 則不然
  （見 [3-2](#3-2-the-not-in-trap-is-distinct-from-coalesce)）。
- 現代最佳化器會把 `EXISTS`、`IN` 與 `LEFT JOIN … IS NULL` 規劃成同一個 semi/anti-join，
  所以挑最好讀、而且對 `NULL` 安全的那個寫法。
- Spark SQL 也可以直接寫：`LEFT SEMI JOIN` 與 `LEFT ANTI JOIN`。

<!-- d512fdecf2fb -->
### 2-3) Self join、cross join 與 non-equi join

**Self join 拿同一張表的列互相比較；non-equi join 用範圍或不等式配對，而不是 `=`。**

<!--CODE-->

<!--CODE-->

- `CROSS JOIN` 在兩個地方是刻意的：建立網格（每個日期 × 每個國家），以及複製一張小表
  （替傾斜的 key 加鹽）。其他地方少了 `ON` 就是 bug。
- Non-equi join 不能用 hash join；在大表上會退回 nested loop（在 Spark 是 broadcast nested loop），
  所以要讓其中一邊夠小，或加上一個等值條件。

<!-- fe3c8b19f9a8 -->
### 2-4) Fan-out：讓列數倍增的 join

**Join 到一張每個 key 有多列的表，列數會倍增，結果上的 `SUM` 就會重複計算。
每次 join 前都先確認兩邊的粒度。**

<!--CODE-->

面試時快速自我檢查：數一數 join 前後的列數。如果一個多對一的 join 改變了「多」那一側的列數，
就代表某個 key 不唯一。

<!-- 43899acf2df4 -->
### 2-5) Join 演算法

**引擎決定 join 的執行方式：輸入小或有索引時用 nested loop，大型等值 join 用 hash join，
輸入已排序或大到放不進記憶體時用 sort-merge。**

| 演算法 | 做法 | 需要 | 成本 |
|---|---|---|---|
| **Nested loop** | 對外層每一列，掃描（或用索引查找）內層 | 不需要 —— 任何條件都能用 | O(n·m)；內層有索引時為 O(n·log m) |
| **Hash join** | 在較小的一側建 hash table，用較大的一側去探測 | 等值條件；build 端放得進記憶體（或 spill） | O(n + m) |
| **Sort-merge join** | 兩邊依 key 排序，再一起往前走 | 等值條件；可排序的 key | O(n log n + m log m)，已排序則為線性 |
| **Broadcast hash join**（分散式） | 把小的一側複製到每個 worker，在本地做 hash join | 有一側很小 | 大的一側不用 shuffle |
| **Shuffle hash / sort-merge**（分散式） | 兩邊依 key 在叢集中重新分區 | 等值條件 | 兩邊都要做網路 shuffle |

在 PostgreSQL 裡，`EXPLAIN` 會顯示 `Nested Loop`、`Hash Join` 或 `Merge Join`；在 Spark 裡，
實體計畫會顯示 `BroadcastHashJoin` 或 `SortMergeJoin`（見 [`./spark.md`](./spark.md)）。

---

<!-- d4363daec3c1 -->
## 3) NULL 語意 ⭐⭐⭐⭐

<!-- 7a7002ddd38b -->
### 3-1) 三值邏輯

**`NULL` 代表「未知」，所以跟它做任何比較都是 `UNKNOWN`，不是 true 也不是 false ——
而 `WHERE` 只保留條件為 `TRUE` 的列。**

| 運算式 | 結果 |
|---|---|
| `NULL = NULL` | `UNKNOWN` |
| `NULL <> 1` | `UNKNOWN` |
| `NULL AND FALSE` | `FALSE` |
| `NULL AND TRUE` | `UNKNOWN` |
| `NULL OR TRUE` | `TRUE` |
| `NOT NULL`（布林值） | `UNKNOWN` |
| `NULL + 1`、`'a' \|\| NULL` | `NULL`（PostgreSQL；`CONCAT` 會略過 NULL） |

- 用 `IS NULL` / `IS NOT NULL` 檢查，永遠不要寫 `= NULL`。
- `WHERE status <> 'paid'` 也會丟掉 `status` 為 `NULL` 的列。如果這些列應該保留，就明說：
  `WHERE status IS DISTINCT FROM 'paid'`。

<!-- 262bb4608de2 -->
### 3-2) `NOT IN` 陷阱、`IS DISTINCT FROM`、`COALESCE`

**只要子查詢產生一個 `NULL`，`x NOT IN (subquery)` 就完全不回傳任何列，
因為對每個 `x` 來說 `x <> NULL` 都是 `UNKNOWN`。**

<!--CODE-->

| 工具 | 作用 |
|---|---|
| `a IS DISTINCT FROM b` | 對 `NULL` 安全的 `<>`：兩個 `NULL` 視為相等，`NULL` 對上一個值視為不同（Snowflake / Spark 另有 `<=>` 或 `EQUAL_NULL`） |
| `COALESCE(a, b, c)` | 第一個非 `NULL` 的參數 |
| `NULLIF(a, b)` | `a = b` 時為 `NULL`，否則為 `a` —— `x / NULLIF(y, 0)` 避免除以零 |

變更偵測時要用的比較就是 `IS DISTINCT FROM` —— 例如判斷某列是否改變的 SCD2 更新或 `MERGE` ——
因為 `old <> new` 會漏掉變成 `NULL` 或從 `NULL` 變來的改動。

<!-- 33f6565102ad -->
### 3-3) 彙總、分組與排序中的 NULL

- `COUNT(*)` 數列數；`COUNT(col)` 數非 `NULL` 的值；`COUNT(DISTINCT col)` 忽略 `NULL`。
- `SUM`、`AVG`、`MIN`、`MAX` 都會略過 `NULL`，所以 `AVG(col)` 是已知值的平均，不是
  `SUM / COUNT(*)`。全部是 `NULL`（或沒有列）時 `SUM` 為 `NULL` —— 用 `COALESCE(…, 0)` 包起來。
- `GROUP BY` 會把所有 `NULL` 放進**同一個**群組，即使 `NULL = NULL` 是未知。
- 排序位置因引擎而異 —— PostgreSQL 與 Snowflake 在升冪時把 `NULL` 放最後，
  BigQuery 與 Spark 放最前。在意時就寫 `NULLS FIRST` / `NULLS LAST`。
- PostgreSQL 的 `UNIQUE` 約束允許多個 `NULL`（除非用 `NULLS NOT DISTINCT`，PostgreSQL 15+）。

---

<!-- edf4f0a7f5cf -->
## 4) 彙總 ⭐⭐⭐⭐

<!-- 3ef20c909e74 -->
### 4-1) 條件彙總與 `FILTER`

**條件彙總一次掃描就算出多個指標：把條件放進彙總函式裡，而不是放在 `WHERE`。**

<!--CODE-->

- `FILTER (WHERE …)` 是標準 SQL，PostgreSQL、SQLite、DuckDB 都支援；BigQuery 與
  Snowflake 用 `CASE WHEN` 的寫法（Spark SQL 兩種都接受）。
- 對 `CASE WHEN … THEN 1 ELSE 0 END` 取平均得到比率；省略 `ELSE` 會得到 `NULL`，
  而 `SUM` 與 `COUNT` 會略過它 —— 兩種都有用，搞混就是經典 bug。
- `SELECT` 中每個非彙總欄位都必須在 `GROUP BY` 裡（PostgreSQL 對函數相依於已分組主鍵的欄位放寬此規則）。

<!-- 308e7a48bd68 -->
### 4-2) `GROUPING SETS`、`ROLLUP`、`CUBE`

**它們在一個查詢裡產生多個層級的彙總 —— 報表需要的小計與總計列 ——
而不是用 `UNION ALL` 串好幾個 `GROUP BY`。**

| 子句 | 對 `(a, b)` 產生的群組 |
|---|---|
| `GROUPING SETS ((a, b), (a), ())` | 正好是列出來的那些組合 |
| `ROLLUP (a, b)` | `(a, b)`、`(a)`、`()` —— 階層：明細、小計、總計 |
| `CUBE (a, b)` | `(a, b)`、`(a)`、`(b)`、`()` —— 每一種組合 |

<!--CODE-->

`GROUPING()` 回傳一個 bitmask，用來區分小計產生的 `NULL`（該欄被 roll up 了）和資料裡真正的 `NULL`
—— 這裡明細列是 0、國家小計是 1、總計是 3。

<!-- 33ae319f8e18 -->
### 4-3) `COUNT(DISTINCT)` 與近似彙總

**精確的 `COUNT(DISTINCT)` 必須記住每一個不同的值，在大規模下很吃記憶體與 shuffle；
近似函式使用固定大小的 sketch（HyperLogLog），以小而有界的誤差換取這些成本。**

| 需求 | PostgreSQL | BigQuery | Snowflake | Spark SQL |
|---|---|---|---|---|
| 相異值計數 | `COUNT(DISTINCT x)`（近似需 `hll` extension） | `APPROX_COUNT_DISTINCT(x)` | `APPROX_COUNT_DISTINCT(x)` | `approx_count_distinct(x)` |
| 百分位 | `PERCENTILE_CONT(p) WITHIN GROUP (ORDER BY x)`（精確） | `APPROX_QUANTILES(x, 100)[OFFSET(p)]` | `APPROX_PERCENTILE(x, p)` | `percentile_approx(x, p)` |
| 最常見的值 | — | `APPROX_TOP_COUNT(x, k)` | `APPROX_TOP_K(x, k)` | — |

- Sketch 可以**合併**：每個區隔每天存一個 HLL sketch，再聯集出每週的相異數，
  精確計數做不到這件事（`DAU` 加起來不等於 `WAU`）。
- 會計費或要對帳的東西用精確計數；dashboard 與探索用近似值。

---

<!-- 38c24f5568fb -->
## 5) Window Function ⭐⭐⭐⭐⭐

<!-- 07b269b91049 -->
### 5-1) Window 的結構

**Window function 為每一列，從一組相關的列（它的 partition）算出一個值，但不會把它們合併。
`GROUP BY` 每個群組回傳一列；window 保留每一列。**

<!--CODE-->

| 家族 | 函式 | 會用到 frame 嗎？ |
|---|---|---|
| 排名 | `ROW_NUMBER`、`RANK`、`DENSE_RANK`、`PERCENT_RANK`、`CUME_DIST`、`NTILE(n)` | 不會 |
| 位移 | `LAG(x, n, default)`、`LEAD(x, n, default)` | 不會 |
| 取值 | `FIRST_VALUE`、`LAST_VALUE`、`NTH_VALUE` | **會** |
| 彙總 | `SUM`、`AVG`、`COUNT`、`MIN`、`MAX` … `OVER (…)` | **會** |

具名 window 可以避免重複定義：`… OVER w … WINDOW w AS (PARTITION BY user_id
ORDER BY order_date)`（PostgreSQL、BigQuery、Spark、DuckDB）。

<!-- 5ef56a100172 -->
### 5-2) 排名：`ROW_NUMBER` vs `RANK` vs `DENSE_RANK`

**它們只在同分時不同：`ROW_NUMBER` 任意打破平手，`RANK` 讓同分同名次並跳過下一個，
`DENSE_RANK` 讓同分同名次且不跳號。**

<!--CODE-->

- 「每個部門薪水前 3 名，同分都算」→ `DENSE_RANK() <= 3`。
- 「每個 key 恰好一列」（去重）→ `ROW_NUMBER() = 1`，**並在 `ORDER BY` 加上 tie-breaker**
  （`ORDER BY updated_at DESC, ingest_id DESC`），否則每次執行選到的列可能不同。
- `NTILE(4)` 把列分成四個大小幾乎相等的桶（依筆數分的四分位，不是依數值）。

<!-- 06fd5154baba -->
### 5-3) `LAG` 與 `LEAD`

**`LAG` 讀取 partition 中較前面那一列的欄位，`LEAD` 讀較後面的 ——
處理「和上一筆比較」類問題的工具。**

<!--CODE-->

`LAG(x, n)` 數的是**列**，不是時間：如果少了一個月，`LAG(revenue, 12)` 就不是「去年同月」。
要先補齊日曆（見 [7-4](#7-4-month-over-month-and-year-over-year-growth)）。

<!-- fec854e41102 -->
### 5-4) Frame：`ROWS` vs `RANGE` vs `GROUPS` ⭐⭐⭐⭐⭐

**Frame 決定 partition 中哪些列會餵給彙總型 window：`ROWS` 數實體列數，`RANGE` 用
`ORDER BY` 欄位的值，`GROUPS` 數同值的 peer group。**

| Frame | `2 PRECEDING` 的意思 | 同分（peer） |
|---|---|---|
| `ROWS` | 前兩列 | 拆開 —— 每一列各自獨立 |
| `RANGE` | 值與目前值相差 2 以內（需要單一數值 / 日期的 `ORDER BY`） | 一起納入 |
| `GROUPS` | 前兩組同值的群組（PostgreSQL 11+、SQLite、DuckDB） | 一起納入 |

預設值，也是大多數 window bug 的來源：

- **沒有 `ORDER BY`** → frame 是整個 partition：`SUM(x) OVER (PARTITION BY k)` 在每一列上都是群組總和。
- **有 `ORDER BY` 但沒寫 frame** → `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`。累計和會
  **一次把排序鍵同分的所有列加進來**，而 `LAST_VALUE(x)` 回傳的是目前這一列的 peer group，
  不是 partition 的最後一列。

<!--CODE-->

只要某天沒有訂單，`avg_last_7_rows` 和 `avg_last_7_days` 就會不同：row frame 會越過空缺往回取七列，
range frame 涵蓋的是七個日曆天。「7 日移動平均」指的是後者 —— 或是先建 date spine 再用前者。

<!-- 250f6a326826 -->
### 5-5) 依 window 結果過濾：CTE 或 `QUALIFY`

**Window function 不能放在 `WHERE`；在 CTE 裡算好再在外面過濾，或在支援的引擎上使用 `QUALIFY`。**

<!--CODE-->

<!--CODE-->

PostgreSQL 也有 `SELECT DISTINCT ON (dept) … ORDER BY dept, salary DESC` 來取每個 key 一列，
但它只有 PostgreSQL 有，也無法推廣到 top-N。

---

<!-- 640036da0e68 -->
## 6) CTE、子查詢與集合運算 ⭐⭐⭐⭐

<!-- a78eb73bfab9 -->
### 6-1) CTE vs 子查詢 vs 暫存表

**CTE（`WITH`）是有名字的子查詢：它讓長查詢能一步一步讀懂。它是只算一次還是被內聯（inline），
由最佳化器決定，不是保證。**

| | CTE | 子查詢 | 暫存表 |
|---|---|---|---|
| 可讀性 | 最好 —— 具名、由上而下的步驟 | 巢狀，較難讀 | 分成多個陳述式 |
| 在同一個查詢內重用 | 可以，用名字 | 必須重寫 | 可以 |
| 會被實體化嗎？ | 依引擎而定 | 內聯 | 會，實際寫出 |
| 統計資訊 / 索引 | 沒有 | 沒有 | 有（可以 `ANALYZE`、加索引） |

- PostgreSQL 12+ 會內聯只被引用一次的非遞迴 CTE；`WITH x AS MATERIALIZED
  (…)` 強制只算一次，`NOT MATERIALIZED` 強制內聯。
- BigQuery 與 Snowflake 對被引用兩次的 CTE 可能會**算兩次**。如果 CTE 很貴又被重用，
  暫存表可能更便宜。
- 面試時就寫 CTE：每一步一個（過濾 → 彙總 → 排名 → 過濾），讓邏輯能邊說邊檢查。

<!-- ce0eae3a85fd -->
### 6-2) 相關子查詢 vs join

**相關子查詢（correlated subquery）引用外層的列，所以邏輯上每個外層列都要求值一次；
多數引擎會把簡單的改寫成 join，但複雜的可能仍然逐列執行。**

<!--CODE-->

Window 的寫法只掃描一次表，被問到「能不能不用子查詢？」時就給這個答案。

<!-- 483f95acedb9 -->
### 6-3) 遞迴 CTE：階層與序列

**遞迴 CTE 有一個錨點查詢，和一個引用 CTE 自己的遞迴查詢；它會一直重複，直到遞迴部分不再回傳新列 ——
這是走訪以父指標儲存的樹的標準方法。**

<!--CODE-->

- 防範髒資料中的循環：限制最大深度（`WHERE o.depth < 20`），或使用
  PostgreSQL 14 的 `CYCLE emp_id SET is_cycle USING path_ids`。
- 用 `UNION` 取代 `UNION ALL` 會在每一步去重，也能擋下某些循環，但要付出排序成本。
- BigQuery 與 Snowflake 支援 `WITH RECURSIVE`；Apache Spark 直到
  4.x 版才加入遞迴 CTE —— 在較舊的 Spark 上，改在應用程式碼裡迭代。

<!-- 9c9001b6d3b9 -->
### 6-4) 集合運算

**`UNION ALL` 是串接；`UNION` 串接後再去重，這需要對整個結果做排序或 hash ——
除非需要去重，否則用 `UNION ALL`。**

| 運算子 | 回傳 |
|---|---|
| `UNION ALL` | 兩個輸入的每一列 |
| `UNION`（`UNION DISTINCT`） | 兩者合起來的相異列 |
| `INTERSECT` | 同時出現在兩者的相異列 |
| `EXCEPT`（Oracle / Snowflake 為 `MINUS`） | 第一個有、第二個沒有的相異列 |

- 欄位是**依位置**對應，不是依名稱，而且型別必須相容。
- BigQuery 必須寫出關鍵字：`UNION ALL` 或 `UNION DISTINCT`；單寫 `UNION` 會報錯。
- 集合運算把兩個 `NULL` 視為相等，這點和 `=` 不同 —— 所以 `EXCEPT` 是以整列為單位、對 `NULL` 安全的 anti-join。

---

<!-- 20a9ee8213fc -->
## 7) 面試題型 ⭐⭐⭐⭐⭐

<!-- 172f4ad9588f -->
### 7-1) 每組 Top-N

**用 window 在每個群組內排名，再保留你要的名次；依同分該怎麼處理來選排名函式。**

<!--CODE-->

經典的「第二高薪水」是同一個題型、只是沒有 partition —— 而且沒有第二個值時，
必須回傳 `NULL`，不是空結果：

<!--CODE-->

<!-- 377626ab0010 -->
### 7-2) 去重：每個 key 保留最新的一列

**把每個 key 的列由新到舊編號，保留第 1 號 —— 這是 CDC 與事件表收到同一筆紀錄不只一次時的標準清理方式。**

<!--CODE-->

- `SELECT DISTINCT` 只會移除**每一個**欄位都相同的列；它不是依 key 去重。
- **讓排序是確定的（deterministic）。** 兩列的 `updated_at` 相同時，`ROW_NUMBER` 可以任挑一列，
  重跑時可能挑到另一列。加上一個真正能排定變更先後的 tie-breaker —— 有的話就用來源序號或
  log 位置（LSN）：`ORDER BY updated_at DESC, source_seq DESC`。
- **`MAX(updated_at)` 再 join 回原表是錯的答案。** `JOIN (SELECT order_id,
  MAX(updated_at) …)` 在兩列共用最新時間戳時會回傳*兩列*，於是「每個 key 一列」的結果
  裡就有重複。
- **移除重複之前，先說清楚什麼叫重複。** 訂單 1 先 `paid` 再 `refunded` 是兩次真實的變更，
  不是重複；同一次變更被送了兩次才是。key 就是這張表的粒度 —— 目前狀態表用 `order_id`，
  變更日誌用 `(order_id, version)` 或 `event_id`。

<!-- 44802e0b32bb -->
### 7-3) 累計總和與移動平均

<!--CODE-->

`ROWS UNBOUNDED PRECEDING` 是 `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` 的簡寫；
明確寫出 `ROWS`（並在 `ORDER BY` 加上唯一的 tie-breaker），可避免 `RANGE` 預設把同分列一起加總。

<!-- a896f3d38a29 -->
### 7-4) 月增率與年增率

**先彙總到月，再用 `LAG` 把每個月和上個月比較 —— 而且要確保每個月都存在，
否則 `LAG` 會拿錯的月份來比。**

<!--CODE-->

`NULLIF(…, 0)` 讓基期為零時的成長率變成 `NULL`，而不是除以零的錯誤 ——
要說出來：從零開始的成長率是未定義，不是無限大。

<!-- e6ee04ca239a -->
### 7-5) Gaps and islands：連續區間 ⭐⭐⭐⭐⭐

**在一段連續的日期內，`date − ROW_NUMBER()` 是常數，所以它能替每一段（island）貼上標籤；
依這個標籤分組，就得到每段連續區間的起點、終點與長度。**

<!--CODE-->

<!--CODE-->

- `DISTINCT` 很重要：同一天兩個事件會讓同一個日期被編號兩次，打破這個恆等式。
- 同樣的技巧可以找出缺口（缺少的 id、缺少的日子）與合併重疊的區間，
  改用 `LAG` 標記新 island 開始的位置，而不是用減法。

<!-- 843f4a806ff0 -->
### 7-6) 和相鄰的列比較

**當條件跨越連續的列 ——「7 天內再次購買」、「連續三筆訂單金額遞增」——
用 `LAG` / `LEAD` 把相鄰的列放到同一列上再過濾。**

<!--CODE-->

<!-- 0b7594a8aede -->
### 7-7) Sessionisation（切分 session）

**當距離使用者上一個事件的間隔超過 timeout，就開始一個新 session；用 `LAG` 標記這些起點，
再對旗標做累計 `SUM` 就能替 session 編號。**

<!--CODE-->

把條件寫成 `<= 30 minutes THEN 0 ELSE 1`，第一個事件（`LAG` 為 `NULL`）就會自動開啟一個 session，
不需要另外檢查 `IS NULL`。

<!-- 4f41933fb573 -->
### 7-8) 留存 cohort

**替每個使用者指定一個 cohort（註冊月份），算出每次活動落在註冊後第幾個月，
再把每個位移上的活躍使用者除以 cohort 大小。**

<!--CODE-->

Cohort 大小來自該 cohort 的**所有**使用者，而不是有活動的那些 ——
否則第 0 個月永遠是 100%，之後每個月都會被高估。

<!-- 7eefb51bbe8a -->
### 7-9) 漏斗

**找出每個使用者第一次到達每個步驟的時間，再數依序走完每一步的使用者。**

<!--CODE-->

要說出的取捨：用每個步驟的**第一次**時間很簡單，但會少算一種使用者 —— 他的第一次加購物車早於第一次瀏覽，
之後才依序走完。嚴格的版本要對每個使用者依序走過事件（以 `LAG` 或以 session 為基礎的漏斗）——
要說清楚業務要的是哪一種。

<!-- 0fa0e0874c4a -->
### 7-10) 中位數與百分位

**`PERCENTILE_CONT` 在中間兩個值之間做內插；`PERCENTILE_DISC` 回傳資料中實際存在的值。**

<!--CODE-->

- 中位數不受極端值影響，這就是面試官要你算它而不是 `AVG` 的原因。
- 在 BigQuery 裡，`PERCENTILE_CONT(x, 0.5) OVER (PARTITION BY …)` 是 window function，不是
  彙總函式；大規模時用 `APPROX_QUANTILES`。

<!-- d2c8892ce566 -->
### 7-11) Pivot 與 unpivot

**Pivot 用條件彙總把列的值轉成欄位；unpivot 把每一列 join 到它自己欄位值的清單，
把欄位轉回列。**

<!--CODE-->

Snowflake、BigQuery、Spark 與 DuckDB 也有 `PIVOT` / `UNPIVOT` 運算子，但必須事先列出要轉置的值 ——
條件彙總可以跨方言，也是白板上該寫的版本。

<!-- 94c8c40a3ff1 -->
### 7-12) 直方圖與分佈

**分佈問題是兩層彙總疊起來：先依實體彙總，再數每個值或每個桶有多少實體。**

<!--CODE-->

訂單數為**零**的使用者不會出現在第一個查詢裡；如果題目要包含他們，
就從 `users` 開始做 `LEFT JOIN`。PostgreSQL 也有 `width_bucket(amount, 0, 1000, 20)`。

<!-- 3296f90da98b -->
### 7-13) 「做了 X 但沒做 Y」

**要嘛在兩個集合之間做 anti-join，要嘛掃一次、對每個實體同時計算兩個條件，
保留 `X > 0 AND Y = 0`。**

<!--CODE-->

單次掃描的 `HAVING` 寫法只讀一次表，而且能自然延伸到「做了 X、Y、Z 但沒做 W」。

<!-- 4c23b86286c8 -->
### 7-14) 從變更日誌重建有效區間 ⭐⭐⭐⭐

**把每個 key 的變更依生效時間排序，讓 `LEAD` 把下一個版本的開始交給每個版本當作結束 ——
這樣晚到的變更在下一次重建歷史時，就會落在正確的位置。**

這就是 SCD Type 2 維度表背後的查詢，也是「late-arriving CDC」背後的查詢：下面的
Tokyo 變更發生在 2 月 1 日，卻在 3 月 5 日才抵達 pipeline，比 Kaohsiung 的變更還晚。

<!--CODE-->

<!--CODE-->

- **依事件時間排序，絕不依到達時間。** 如果依 `arrived_at` 排序，JP 會變成目前的國家。
- **半開區間** —— `valid_from <= t < valid_to` —— 讓落在邊界上的時間點只屬於一個版本。
  時間點 join 因此寫成
  `ON f.user_id = d.user_id AND f.order_date >= d.valid_from AND f.order_date < d.valid_to`。
- 任何 SCD2 表上都應該回傳零列的兩個檢查：

<!--CODE-->

重疊檢查拿每個版本，和排在它前面的版本中最晚的結束時間比較。這能抓到 `valid_from`
**相同**的兩個版本 —— 用 `a.valid_from < b.valid_from` 的 self-join 會漏掉它們 ——
而且只需排序一次，不必兩兩 join。

重建一個 key 的整段歷史，是處理晚到資料最簡單又正確的答案；只對收到晚到變更的 key
重建，則是最佳化。維度表如何用 `MERGE` 載入，是 [data_modeling.md](./data_modeling.md) 的主題。

<!-- f76da1ca0b39 -->
### 7-15) 尖峰同時數：重疊區間

**把每個區間轉成開始時的 `+1` 與結束時的 `−1`，把這些點排序，累計和就是每個時刻開著的數量；
它的最大值就是尖峰。**

經典的出法是「同時在線最多的使用者數」、「這些會議需要幾間會議室」或「最大同時 session 數」。

<!--CODE-->

- **同時刻的排序決定答案。** session 1 在 10:30 結束時 session 4 正好開始。`delta` 由小到大
  排序會把 `−1` 排在前面，所以兩者不算重疊 —— 對半開區間來說是對的。如果結束時間是包含的，
  就把 `+1` 排在前面。
- 用 `ROWS`，不要用預設的 `RANGE`，讓每個點在自己那一列上加進累計和。
- 對 2n 個點排序一次：O(n log n)，在任何資料倉儲裡做法都一樣。兩兩比較的 self-join 是 O(n²)。

<!-- 63589f442546 -->
### 7-16) 把資料品質檢查寫成 SQL 斷言 ⭐⭐⭐⭐

**把每個檢查寫成回傳壞資料列的查詢，或一個必須為零的計數，不為零時就讓 pipeline 失敗。**

<!--CODE-->

對帳（reconciliation）要拿載入結果和來源比較**同一個切片**，這樣不一致時指向的是某一天，
而不是整張表：

<!--CODE-->

- `FULL OUTER JOIN` 加上 `IS DISTINCT FROM` 能抓到只存在於一側的日子，而 inner join 和 `<>`
  都會把它藏起來。
- 孤兒檢查用 `NOT EXISTS` 而不是 `NOT IN`，原因是 [3-2](#3-2-the-not-in-trap-is-distinct-from-coalesce)
  講的 `NULL`。
- 不是每個失敗都該停下 pipeline：`NULL` key 讓這次執行失敗；來自尚未抵達的維度的孤兒列則
  放進隔離區，或給它一個 unknown member。哪些檢查要擋下、由哪些工具執行（dbt tests、
  Great Expectations），是 [batch_pipeline.md](./batch_pipeline.md) 的主題。

---

<!-- b98650aca258 -->
## 8) 寫入資料：MERGE 與 upsert ⭐⭐⭐⭐

<!-- b9308b8113e2 -->
### 8-1) `MERGE`

**`MERGE` 在一個原子陳述式裡把來源套用到目標：更新有配對的列、插入沒配對的、
也可以刪除 —— 這是載入維度表或套用 CDC 的標準方式。**

<!--CODE-->

- **先對來源去重。** 如果兩筆來源列配到同一筆目標列，PostgreSQL、
  Snowflake（預設）與 BigQuery 會直接報錯，而不是挑一筆。
- `AND … IS DISTINCT FROM …` 條件會略過沒有實際變化的更新，省下寫入，也讓
  `updated_at` 保有意義。
- 當來源是冪等的，`MERGE` 就是冪等的：用同樣的來源重跑不會改變任何東西。
- **別讓舊的變更覆蓋新的變更。** 如果變更可能亂序抵達，或重跑時重播了較舊的批次，就把版本當成
  更新條件：`WHEN MATCHED AND s.updated_at > t.updated_at THEN UPDATE SET country = s.country,
  updated_at = s.updated_at`（來源序號比時間戳更安全）。它**取代** `IS DISTINCT FROM` 條件，
  而不是和它疊加。帶著相同國家的較新事件，仍然必須把 `updated_at` 往前推；否則目標在時間 10
  是 `A` 時，時間 12 的 `A` 會被略過，之後重播的時間 11 的 `B` 就會通過保護條件、把它覆蓋掉。

<!-- 441f00cf05d4 -->
### 8-2) Upsert 與分區覆寫

| 技術 | 方言 | 用途 |
|---|---|---|
| `INSERT … ON CONFLICT (key) DO UPDATE` | PostgreSQL、SQLite | 列層級 upsert；`key` 上需要唯一索引 |
| `INSERT … ON DUPLICATE KEY UPDATE` | MySQL | MySQL 的同等寫法 |
| `MERGE` | 標準；PostgreSQL 15+、Snowflake、BigQuery、Delta / Iceberg | 在一個陳述式裡 upsert + 刪除 |
| `INSERT OVERWRITE … PARTITION (dt = …)` | Hive、Spark SQL | 取代整個分區 —— 冪等的批次載入 |
| 在一個交易裡 `DELETE` + `INSERT` | 任何 | 可跨方言的分區取代 |

<!--CODE-->

`EXCLUDED` 是原本要被插入的那一列。為什麼批次載入時覆寫勝過附加，
是 [batch_pipeline.md](./batch_pipeline.md) 的主題。

---

<!-- 3f9416fcf507 -->
## 9) 日期、時間與時區 ⭐⭐⭐

<!-- ccd3f80ed729 -->
### 9-1) 時間戳型別

**用帶時區意識的型別以 UTC 儲存時間點，只有在推導報表日期時才轉換成當地時間。**

| 意義 | PostgreSQL | BigQuery | Snowflake | Spark SQL |
|---|---|---|---|---|
| 一個時間點（絕對時刻） | `timestamptz` | `TIMESTAMP` | `TIMESTAMP_TZ` / `TIMESTAMP_LTZ` | `TIMESTAMP`（session 時區） |
| 牆上時鐘的時間，不帶時區 | `timestamp` | `DATETIME` | `TIMESTAMP_NTZ` | `TIMESTAMP_NTZ`（3.4+） |
| 日曆日期 | `date` | `DATE` | `DATE` | `DATE` |

<!--CODE-->

- 「依日期」的每日報表必須說明是**誰的**日期。把 UTC 資料依當地日期分區，或反過來，
  都會讓午夜附近的事件落在錯的那一天。
- 使用 IANA 時區名稱（`Asia/Taipei`、`America/New_York`），而不是固定位移，這樣夏令時間才會被處理。

<!-- 9d42847796cd -->
### 9-2) 截斷、範圍與 date spine

**在原始欄位上用半開區間過濾時間，用截斷分組，並產生一份日曆來顯示沒有資料的日子。**

<!--CODE-->

- 對 timestamp 用 `BETWEEN '2026-11-01' AND '2026-11-30'`，會默默丟掉最後一天午夜之後的所有資料。
- `WHERE DATE(event_ts) = …` 讀起來正確，但可能讓 `event_ts` 上的索引或 partition pruning 失效；
  範圍寫法讓過濾條件留在原始欄位上。
- BigQuery：`GENERATE_DATE_ARRAY` + `UNNEST`；Snowflake：`GENERATOR` + `DATEADD`；Spark：
  `explode(sequence(start, stop, interval 1 day))`。

---

<!-- b8df30a4b0b5 -->
## 10) 查詢效能 ⭐⭐⭐⭐

<!-- 86af10425924 -->
### 10-1) OLTP 資料庫中的索引

**當查詢依索引的前導欄位過濾或排序時，B-tree 索引能把掃描變成查找；
代價是空間，以及每一次寫入都變慢。**

- **複合索引 `(a, b)`** 可以服務 `WHERE a = ?`、`WHERE a = ? AND b = ?` 與
  `ORDER BY a, b`，但一般不能單獨服務 `WHERE b = ?` —— 這是最左前綴規則
  （PostgreSQL 18 有時能對它做 skip scan）。
- 等值欄位放前面，範圍欄位放後面：`(user_id, order_date)` 能服務
  `user_id = ? AND order_date >= ?`。
- **覆蓋索引** —— `CREATE INDEX … (user_id) INCLUDE (amount)` 只靠索引就能回答查詢
  （index-only scan）。
- **可利用索引的條件（sargable）**：`WHERE created_at >= …` 能用索引；`WHERE DATE(created_at) = …`、
  `WHERE amount * 2 > …` 或 `WHERE col LIKE '%x'` 都不能（除非有運算式索引）。
- 低選擇性的欄位（布林值、只有三種值的 status）很少因索引受益；**部分索引**
  （`WHERE status = 'pending'`）通常有用。

<!-- 429735b55522 -->
### 10-2) 欄式資料倉儲上的效能

**資料倉儲沒有 B-tree 索引可以依靠：成本是掃描的 bytes 與 shuffle 的資料量，
所以要讀更少的欄位、剪掉分區，並在 join 前先縮小資料。**

| 手段 | 作用 |
|---|---|
| 只選需要的欄位 | 欄式儲存只讀那些欄位 —— `SELECT *` 會全部讀 |
| 在分區欄位上過濾 | partition pruning 跳過整個分區 |
| clustering / sort key | 每個區塊的 min/max 統計讓引擎跳過區塊 |
| 在 join 前先過濾與彙總 | 要 shuffle 的資料更少 |
| broadcast 小維度表 | 避免 shuffle 事實表 |
| materialised view / 摘要表 | 預先計算熱門彙總 |
| 近似彙總 | 用 sketch 取代精確的相異集合 |

- Pruning 需要條件**直接作用在分區欄位本身**：`WHERE dt = '2026-11-01'` 能剪；
  在欄位外面包函式可能不行，依引擎而定。
- BigQuery 依掃描的 bytes 計費，所以對一般的表掃描，`LIMIT` 不會降低成本 ——
  `SELECT * … LIMIT 10` 仍然會讀取每一個被選的欄位。

<!-- bd7492f13251 -->
### 10-3) 讀懂 `EXPLAIN`

**`EXPLAIN` 顯示最佳化器選擇的計畫；`EXPLAIN ANALYZE` 會真的執行查詢，並加上實際列數與時間 ——
比較估計列數與實際列數，就能找到出問題的那一步。**

<!--CODE-->

要找的東西：

- 大表上出現 **Seq Scan**，而你預期的是索引 —— 缺索引或條件無法利用索引。
- **估計列數與實際列數相差很大** —— 統計資訊過舊（對表做 `ANALYZE`）或欄位彼此相關；
  它會導致選錯 join 演算法。
- 外層很大的 **Nested Loop** —— 通常是估計錯誤。
- **Sort / Hash spill 到磁碟**（`external merge`、`Batches: > 1`）—— `work_mem` 太小，
  或到達該節點的資料太多。
- 在資料倉儲或 Spark 的計畫裡：shuffle 最多 bytes 的 stage，以及 join 是 broadcast 還是 shuffle。

<!-- a482049bf96a -->
### 10-4) 傾斜與超大型 join

**當一個 key 握有很大比例的列時，負責它的 worker 會做掉大部分的工作；
查詢的速度就是那一個 worker 的速度。**

- 找出它：`SELECT key, COUNT(*) FROM t GROUP BY key ORDER BY 2 DESC LIMIT 20`。最大的 key
  常常是 `NULL`、`0`、`'unknown'` 或測試帳號。
- 解法：把熱點 key 過濾掉或另外處理，再 `UNION ALL` 結果；broadcast 小的那一側；
  讓自適應執行切開傾斜的分區；替 key 加鹽（見 [`./spark.md`](./spark.md)）。
- 對傾斜 key 做 `COUNT(DISTINCT)` 也有同樣問題；先用 `GROUP BY key,
  value` 預先彙總，或改用近似值。

---

<!-- e7c3bd67c4f7 -->
## 11) 方言差異 ⭐⭐⭐

**概念到處都一樣；拼法不同。面試時說出你寫的是哪個方言，面試官就會接受。**

| 任務 | PostgreSQL | BigQuery | Snowflake | Spark SQL / Databricks |
|---|---|---|---|---|
| 截斷到月 | `DATE_TRUNC('month', d)` | `DATE_TRUNC(d, MONTH)` | `DATE_TRUNC('month', d)` | `date_trunc('MONTH', d)` / `trunc(d, 'MM')` |
| 相差天數 | `d2 - d1` | `DATE_DIFF(d2, d1, DAY)` | `DATEDIFF(day, d1, d2)` | `datediff(d2, d1)` |
| 加上一段時間 | `d + INTERVAL '7 days'` | `DATE_ADD(d, INTERVAL 7 DAY)` | `DATEADD(day, 7, d)` | `date_add(d, 7)` |
| 依 window 過濾 | 子查詢 / CTE | `QUALIFY` | `QUALIFY` | `QUALIFY`（Databricks） |
| 字串彙總 | `string_agg(x, ',')` | `STRING_AGG(x, ',')` | `LISTAGG(x, ',')` | `array_join(collect_list(x), ',')` |
| 展開陣列 | `unnest(arr)` | `CROSS JOIN UNNEST(arr)` | `LATERAL FLATTEN(input => arr)` | `explode(arr)` / `LATERAL VIEW explode(arr)` |
| 安全除法 | `x / NULLIF(y, 0)` | `SAFE_DIVIDE(x, y)` | `x / NULLIF(y, 0)`（`DIV0` 回傳 0） | `try_divide(x, y)` |
| 整數 `7 / 2` | `3`（整數除法） | `3.5`（`DIV(7, 2)` = 3） | `3.5` | `3.5`（`7 div 2` = 3） |
| 加引號的識別字 | `"col"` | `` `col` `` | `"col"` | `` `col` `` |
| 不分大小寫比對 | `ILIKE` | `LOWER(x) LIKE …` | `ILIKE` | `ILIKE` |
| upsert | `ON CONFLICT` / `MERGE`（15+） | `MERGE` | `MERGE` | `MERGE`（Delta / Iceberg） |

整數除法那一列是會默默產生錯誤答案的那個：在 PostgreSQL 裡，兩個整數做
`COUNT(*) / total`，任何小於 1 的比率都會是 `0`。乘上 `1.0` 或轉型。

---

<!-- 48fa484c2aca -->
## 12) 快問快答 ⭐⭐⭐⭐

<!-- c8b86d49d3d1 -->
### 12-1) 常見問題的簡短答案

| 問題 | 答案 |
|---|---|
| `WHERE` vs `HAVING`？ | `WHERE` 在分組前過濾列；`HAVING` 在分組後過濾群組，可以用彙總函式。 |
| `COUNT(*)` vs `COUNT(1)` vs `COUNT(col)`？ | 前兩個完全相同（數列數）；`COUNT(col)` 會略過 `NULL`。 |
| `UNION` vs `UNION ALL`？ | `UNION` 以排序 / hash 的代價去除重複；`UNION ALL` 保留每一列。 |
| `RANK` vs `DENSE_RANK` vs `ROW_NUMBER`？ | 同分時：同名次且跳號 / 同名次不跳號 / 任意的唯一編號。 |
| `DELETE` vs `TRUNCATE` vs `DROP`？ | 刪除列（有日誌、可加條件）/ 快速移除所有列 / 移除整張表。 |
| 為什麼我的 `LEFT JOIN` 少了列？ | 右表上的 `WHERE` 條件丟掉了補 `NULL` 的列 —— 把它移到 `ON`。 |
| 為什麼 join 回傳的列比左表還多？ | 右側每個 key 有多列 —— fan-out；檢查唯一性。 |
| 為什麼 `NOT IN` 什麼都沒回傳？ | 子查詢含有 `NULL`；改用 `NOT EXISTS`。 |
| 怎麼找重複？ | `GROUP BY key HAVING COUNT(*) > 1`；要移除就用 `ROW_NUMBER()` 保留 1。 |
| 用 `MAX(updated_at)` 再 join 回原表取每個 key 的最新列？ | 兩列共用最新時間戳時會回傳兩列；改用 `ROW_NUMBER()` 加上 tie-breaker。 |
| 一筆變更晚到了 —— 哪個版本才是目前的？ | 依事件時間（或來源序號）最晚的那個，絕不是依到達時間；用 `LEAD` 重建該 key 的區間。 |
| CTE 只會算一次嗎？ | 依引擎而定：PostgreSQL 12+ 會內聯只用一次的 CTE，資料倉儲可能會重算被重用的 CTE。 |
| 怎麼讓查詢更快？ | 讀 `EXPLAIN`；盡早在有索引 / 分區的欄位上過濾；選更少的欄位；join 前先彙總；修掉傾斜。 |
| view vs materialised view？ | view 是每次都會執行的已存查詢；materialised view 存下結果，必須刷新。 |

<!-- 8e3c5db96baf -->
### 12-2) 面試時如何處理一道 SQL 題

1. **先重述答案的粒度** ——「每個使用者每個月一列」—— 再開始寫。
2. **問清楚邊界情況**：同分、`NULL`、沒有活動的使用者、「一天」用的是哪個時區。
3. **用 CTE 來寫**，每一步一個，並依它所裝的內容命名。
4. **用一個很小的例子檢查**，邊做邊說 —— 兩個使用者、一組同分、一個缺少的日子。
5. **說出成本**：哪一步 shuffle 或掃描最多，以及哪個索引或分區有幫助。
