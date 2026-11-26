<!-- 4ad4d947b0e8 -->
# LeetCode SQL FAQ

> **範圍** —— LeetCode SQL（Database）題目所靠的技巧，以 MySQL 8 撰寫：排名與 Top-N、重複資料、NULL 與缺少的列、條件彙總與比率、樞紐（pivot）、比較相鄰的列、gaps and islands、累計與移動視窗、`UPDATE` / `DELETE` 題型，以及會讓提交失敗的 MySQL 細節。每個技巧都示範在考它的 LeetCode 題目上。資料倉儲規模的 SQL 與查詢計畫不在這裡。
> **另見**：[`../de/sql_for_data.md`](../de/sql_for_data.md) —— 同樣的題型用 PostgreSQL 寫，並從第一原理講解 join、NULL 語意與 window frame；[`../de/sql_interview_questions.md`](../de/sql_interview_questions.md) —— 資深資料工程的 SQL 面試關卡，每個答案都會被追問「那 1 TB 呢？」；[`./faq_sql.md`](./faq_sql.md) —— 較舊、較長的 SQL 筆記。

每個查詢都是 **MySQL 8**，也就是 LeetCode 評分 SQL 用的方言；列出的每個結果，都是 MySQL 8.0
在所示資料上實際回傳的內容。表名與欄位名沿用 LeetCode 原題，所以查詢可以直接貼進它所標的題目。
每一節先給你會說出口的那句話，接著是查詢，最後是陷阱。

<!-- 0544c4cabb7a -->
## 0) 動手寫查詢之前 ⭐⭐⭐⭐⭐

多數錯誤的提交是讀錯題目，而不是語法錯。開始打字前先回答這五個問題：

| 先問 | 為什麼重要 | 在哪裡會踩到 |
|---|---|---|
| **一列輸出代表什麼？**（粒度） | 決定 `GROUP BY`，以及 join 會不會讓列數倍增 | [1-3](#1-3-top-n-per-group)、[4-2](#4-2-rates-and-percentages) |
| **同分時怎麼辦？** | 決定用 `ROW_NUMBER`、`RANK` 還是 `DENSE_RANK` | [1-1](#1-1-row_number-vs-rank-vs-dense_rank)、[1-4](#1-4-first-or-latest-row-per-group) |
| **值可能是 `NULL` 嗎？** | `=`、`<>` 與 `NOT IN` 會悄悄丟掉 `NULL` 的列 | [3-1](#3-1--and--drop-null-rows)、[3-2](#3-2-never-did-x-the-anti-join) |
| **缺少的列也必須出現嗎？** | 「回報 0」或「回傳 `null`」需要 `LEFT JOIN` 或純量子查詢 | [1-2](#1-2-the-n-th-highest-value-null-when-there-is-none)、[3-3](#3-3-keep-the-rows-with-zero-matches) |
| **四捨五入與排序？** | `ROUND(x, 2)`，以及「任意順序回傳」還是要明確的 `ORDER BY` | [4-2](#4-2-rates-and-percentages) |

<!-- 00a716f4374e -->
## 1) 排名與 Top-N ⭐⭐⭐⭐⭐

<!-- b7894be38705 -->
### 1-1) `ROW_NUMBER` vs `RANK` vs `DENSE_RANK`

**三者只在同分時不同。`ROW_NUMBER` 給每一列各自的號碼；`RANK` 讓同分的列同名次，並跳過其後的
名次；`DENSE_RANK` 讓同分的列同名次，但不跳號。**

<!--CODE-->

<!--CODE-->

| 函式 | 遇到同分 | 記憶方式 | 用在 |
|---|---|---|---|
| `ROW_NUMBER()` | 每一列仍拿到唯一的號碼 | 每筆資料一個號碼 | 每組恰好一列、去重 |
| `RANK()` | 同名次，接著跳號（1, 2, 2, 4） | 競賽排名，有並列就跳號 | 「前 N 名」，同分都保留 |
| `DENSE_RANK()` | 同名次，不跳號（1, 2, 2, 3） | 競賽排名，有並列但不跳號 | 「第 N 高的**不同**值」 |

LC 178（*Rank Scores*）顧名思義就是 `DENSE_RANK` —— 而且它會卡在一個 MySQL 細節上：

<!--CODE-->

- **同分的列誰拿到較小的 `ROW_NUMBER` 是不確定的**，除非 `ORDER BY` 能決定；下次執行 Bob
  可能變成 3。只要選擇必須穩定，就加上一個唯一欄位當 tie-breaker（`ORDER BY salary DESC, id`）。
  `RANK` 與 `DENSE_RANK` 不需要：同分的列本來就拿到相同的值。
- **window function 不能出現在 `WHERE`** —— 它在 `WHERE` 執行之後才計算 —— 而 MySQL 沒有
  `QUALIFY`。先在 CTE 或衍生表（derived table）裡算出名次，再到外層過濾。

LC：[178](https://leetcode.com/problems/rank-scores/)

<!-- 75f91058a3a3 -->
### 1-2) 第 N 高的值，沒有時回傳 `NULL`

**先把值去重，跳過 N − 1 個，取一個 —— 再把整個查詢包進純量子查詢，讓「不存在」以 `NULL`
回傳，而不是空結果。**

<!--CODE-->

<!--CODE-->

- **在 9000, 8000, 8000 上求「第二高」，答案靠的是 `DISTINCT`** —— 少了它，`OFFSET 1` 回傳第一個
  8000，碰巧是對的；但 `OFFSET 2`（第三高）會再回傳一次 8000，而不是 7000。
- **空結果不是 `NULL`。** 在只有一列的表上，單純的 `SELECT … LIMIT 1 OFFSET 1` 回傳零列，而題目要的是
  一列、內容為 `null`。外層的 `SELECT ( … )` 或彙總函式（`MAX`）才會把「什麼都沒有」變成 `NULL`。
- **在 stored function 裡寫 `LIMIT N - 1` 是語法錯誤**：`LIMIT` 只接受字面值、參數或區域變數。
  所以要先 `SET N = N - 1`。

LC：[176](https://leetcode.com/problems/second-highest-salary/)、
[177](https://leetcode.com/problems/nth-highest-salary/)

<!-- d9338cbedce2 -->
### 1-3) 每組 Top-N

**用 `PARTITION BY` 在每組內排名，再保留你要的名次。用哪個排名函式，取決於題目對同分怎麼說。**

<!--CODE-->

<!--CODE-->

| 「Top 3」指的是 | 過濾條件 | 在 9000, 8000, 8000, 7000 上會留下 |
|---|---|---|
| 最多 3 列 | `ROW_NUMBER() <= 3` + tie-breaker | 3 列 |
| 前 3 個名次，同分都算 | `RANK() <= 3` | 3 列（7000 是第 4 名） |
| 前 3 個不同的值（LC 185） | `DENSE_RANK() <= 3` | 全部 4 列 |

每一種過濾都只是上限：只有兩個人的部門，三種寫法都回傳兩列。若只想留下人數足夠的組，在 CTE 裡再算
`COUNT(*) OVER (PARTITION BY departmentId) AS group_size`，並保留 `group_size >= 3`。

LC 184（*Department Highest Salary*）就是同一個查詢改成 `salary_rank = 1`，或是不用 window：

<!--CODE-->

LC：[184](https://leetcode.com/problems/department-highest-salary/)、
[185](https://leetcode.com/problems/department-top-three-salaries/)

<!-- 345b2174d03e -->
### 1-4) 每組的第一列或最新一列

**把每組的列由新到舊（或由舊到新）編號，保留第 1 號。答案是每組一列時用 `ROW_NUMBER`；並列第一的
每一列都要回傳時用 `RANK`。**

<!--CODE-->

排序欄位不唯一時 —— 兩筆訂單的 `order_date` 相同 —— 就再加一個唯一的欄位：
`ORDER BY order_date DESC, order_id DESC`。少了它，選到的那一列每次執行都可能不同。
這讓選擇變得**確定**，但不一定是最新的：只有 id 依建立順序配發時，較大的 `order_id` 才是較晚的訂單。
必須是最新的那筆時，先依更細的時間戳（`created_at`）排序。

LC 1070 正是 `ROW_NUMBER` **錯**的情況：它要每個產品第一年的所有銷售，而一個產品在那一年
可能有好幾筆。

<!--CODE-->

<!--CODE-->

- 不用 window 的寫法是 `WHERE (product_id, year) IN (SELECT product_id, MIN(year) … GROUP BY
  product_id)` —— 它同樣保留同分。
- **用 `MAX(date)` 再依日期 join 回去**：同分全部都要回傳時沒問題；題目只要恰好一列時就錯了 ——
  共用最新日期的兩列都會配對到。
- **只需要日期時，不必用 window。** LC 511 要每位玩家的首次登入：
  `SELECT player_id, MIN(event_date) AS first_login FROM Activity GROUP BY player_id`。只有連同那一列的
  其他欄位（例如它的 `device_id`）也需要時，才替列編號。

LC：[511](https://leetcode.com/problems/game-play-analysis-i/)、
[1070](https://leetcode.com/problems/product-sales-analysis-iii/)

<!-- 6ad5f27c57cb -->
## 2) 重複資料 ⭐⭐⭐⭐

<!-- 22ce6ffb9dd9 -->
### 2-1) 找出重複的值

**依值分組，保留超過一列的組。**

<!--CODE-->

- `WHERE` 在分組之前過濾列；對 `COUNT(*)` 的條件要放在 `HAVING`。

LC：[182](https://leetcode.com/problems/duplicate-emails/)

<!-- 6c58533bf0f1 -->
### 2-2) 刪除重複，保留一筆

**把表依重複的值和自己 join，刪掉 id 較大的那列，於是每個值留下最小的 id。**

<!--CODE-->

window function 的版本把同一套編號反過來讀：編號大於 1 的每一列都是多出來的副本。

<!--CODE-->

- **編號之前先濾掉 `NULL` 的 email。** `PARTITION BY email` 會把所有 `NULL` 放進同一個 partition，
  少了 `email IS NOT NULL`，兩個沒有 email 的人會被當成重複，其中一人會被刪掉。上面的 self-join
  本來就會跳過他們，因為 `NULL = NULL` 不為真；`GROUP BY email` 的寫法和 window 一樣需要這個過濾。
- **MySQL error 1093** —— *"You can't specify target table 'Person' for update in FROM
  clause"* —— 就是 `DELETE FROM Person WHERE id NOT IN (SELECT MIN(id) FROM Person GROUP BY
  email)` 會丟出的錯。把子查詢再包一層衍生表
  （`SELECT id FROM (SELECT MIN(id) AS id … ) AS keep`），MySQL 會先把它實體化（materialise），
  敘述就能執行。
- `SELECT DISTINCT` 只移除**每一個**欄位都相同的列；它不是依 key 去重，而且完全不會改動表裡的資料。

LC：[196](https://leetcode.com/problems/delete-duplicate-emails/)

<!-- 04af8a377d79 -->
### 2-3) 只出現一次的值

**`HAVING COUNT(*) = 1` 找出它們；把結果包進 `MAX`，沒有任何一個時就回傳 `NULL`。**

<!--CODE-->

LC：[619](https://leetcode.com/problems/biggest-single-number/)

<!-- db3405d57c0b -->
## 3) NULL 與缺少的列 ⭐⭐⭐⭐⭐

<!-- c779968ae089 -->
### 3-1) `<>` 與 `=` 會丟掉 `NULL` 的列

**任何和 `NULL` 的比較都是 `UNKNOWN`，而 `WHERE` 只保留 `TRUE` —— 所以「不是由 2 號顧客推薦」
必須明確寫出 `NULL` 的列。**

<!--CODE-->

- 只寫 `referee_id <> 2` 只會回傳 Zack：三位沒有推薦人的顧客結果是 `UNKNOWN`，不是 `TRUE`，
  於是消失了。
- `<=>` 把 `NULL <=> NULL` 當成 true、`NULL <=> 2` 當成 false，所以永遠不會回傳 `UNKNOWN`。
  標準 SQL 的寫法是 `IS NOT DISTINCT FROM`。

LC：[584](https://leetcode.com/problems/find-customer-referee/)

<!-- efacfa6bcdb4 -->
### 3-2) 「從來沒做過 X」：anti-join

**`LEFT JOIN` 活動表，保留什麼都沒配對到的列 —— 或用 `NOT EXISTS`。避免對可能是 `NULL` 的欄位
用 `NOT IN`。**

<!--CODE-->

<!--CODE-->

<!--CODE-->

- 所以只要有一筆訂單的 `customerId` 是 `NULL`，`WHERE id NOT IN (SELECT customerId FROM Orders)`
  就會**一列都不回傳**。`NOT EXISTS` 與 `LEFT JOIN … IS NULL` 的寫法不受影響。
- 在 `LEFT JOIN` 寫法裡，要檢查一個真正配對到時絕不會是 `NULL` 的欄位 —— 被 join 那張表的主鍵 ——
  而不是可為 `NULL` 的屬性欄位。

LC：[183](https://leetcode.com/problems/customers-who-never-order/)

<!-- ba6bbbf03e23 -->
### 3-3) 保留配對數為零的列

**先建出答案必須包含的每一列 —— 常用 `CROSS JOIN` —— 再 `LEFT JOIN` 事實資料，並計數右表的某個
欄位，讓沒配對到的算成 0。**

<!--CODE-->

<!--CODE-->

- **`COUNT(*)` 會把 Bob 的 Physics 算成 1**：`LEFT JOIN` 仍會為他產生一列，右側全是 `NULL`。
  `COUNT(column)` 會跳過 `NULL`，所以算出 0。
- **對右表的條件要放在 `ON`，不是 `WHERE`。** `WHERE e.subject_name = …` 會把 `LEFT JOIN`
  剛保留下來的 `NULL` 列丟掉，讓它又變回 inner join。

答案是比率而不是計數時，空的組會得到 `NULL`，用 `IFNULL(…, 0)` 把它變成題目要的 0：

<!--CODE-->

- `BETWEEN` 兩端都包含，這正是價格期間的 `start_date` 與 `end_date` 在這裡的意思。

LC：[1251](https://leetcode.com/problems/average-selling-price/)、
[1280](https://leetcode.com/problems/students-and-examinations/)

<!-- 9524e2e3f2cb -->
### 3-4) `LEFT JOIN` 之後的 `COUNT(*)` vs `COUNT(column)`

**`COUNT(*)` 計算傳到它手上的每一列，包含有 `NULL` 的列；`COUNT(column)` 只計算該欄位不為
`NULL` 的列。兩者都只計算通過 `WHERE` 的列，因為 `WHERE` 在任何彙總之前執行 —— 所以在
`LEFT JOIN` 的右表上加會排除 `NULL` 的 `WHERE`（`a.col = …`、`a.col IS NOT NULL`），會把
`COUNT(*)` 本來要計算的那些沒配對到的列刪掉。**

<!--CODE-->

| 寫法 | 計算什麼 | `LEFT JOIN` 沒配對到的一列 |
|---|---|---|
| `COUNT(*)`（與 `COUNT(1)` 相同） | 每一列 | 算 1 |
| `COUNT(a.col)` | `a.col` 不為 `NULL` 的列 | 算 0 |
| `COUNT(DISTINCT a.col)` | `a.col` 不同的非 `NULL` 值 | 算 0 |

LC 550 就是拿其中一個除以另一個。在題目自己的範例上，`LEFT JOIN` 交給 `COUNT` 三列，每位玩家
一列，只有玩家 1 找到隔天那一列：

<!--CODE-->

<!--CODE-->

`COUNT(a.player_id)` 是 1（隔天回來的玩家），`COUNT(*)` 是 3（所有玩家），`ROUND(1 / 3, 2)`
就是預期的 `0.33`。

把隔天的條件從 `ON` 移到 `WHERE`，分母就垮了：

<!--CODE-->

- **`COUNT(*)` 會計算含有 `NULL` 的列，但只在 `WHERE` 之後。** 它不會因為某列有 `NULL` 就跳過，
  但它只看得到 `WHERE` 放行的列。在正確的 join 之後加 `WHERE a.player_id IS NOT NULL`，會得到
  同樣錯誤的 `1.00`：那只是繞了一圈的 inner join。
- **只有會排除 `NULL` 的條件才會這樣。** `NULL` 能通過的右表條件，例如
  `WHERE a.player_id IS NULL`，留下的正好是沒配對到的列 —— 那就是
  [3-2](#3-2-never-did-x-the-anti-join) 的 anti-join。
- **沒有任何人回來時，錯誤的查詢回傳 `NULL`，不是 `1.00`。** `WHERE` 之後沒有任何列，兩個計數
  都是 0，而 MySQL 的 `0 / 0` 是 `NULL`。只要至少有一位玩家回來，就會出現 `1.00`。
- **對彙總結果的篩選要放在 `HAVING`**，它在 `COUNT` 之後執行；`WHERE COUNT(*) > 1` 是語法錯誤。
- **只要一位玩家可能出現在不只一列 join 結果裡，就需要 `DISTINCT`** —— 左表一列配對到右表
  多列，或左表本身就有同一位玩家的多列。LC 550 兩者都不會發生：`first_login` 每位玩家一列，
  而 `(player_id, event_date)` 是 primary key，所以每位玩家隔天最多一列，`COUNT(a.player_id)`
  本來就是玩家數。若是一天有多列的原始登入 log，或左表有重複的玩家，兩個計數都會膨脹，要寫成
  `COUNT(DISTINCT a.player_id) / COUNT(DISTINCT f.player_id)`。
- LC 1280 的 0 也由同一條規則決定 —— 見 [3-3](#3-3-keep-the-rows-with-zero-matches)。

LC：[550](https://leetcode.com/problems/game-play-analysis-iv/)、
[1280](https://leetcode.com/problems/students-and-examinations/)

<!-- d510d6cfb043 -->
## 4) 彙總 ⭐⭐⭐⭐⭐

<!-- d4a44ca1c1f8 -->
### 4-1) 條件彙總

**把條件放進彙總函式裡 —— `SUM(CASE WHEN … THEN … END)` —— 掃過表一次就算出好幾個過濾後的總和。**

<!--CODE-->

- 在 MySQL 裡比較運算的結果就是數字 1 或 0，所以 `SUM(state = 'approved')` 會計算符合的列數。
  可攜的寫法是 `SUM(CASE WHEN state = 'approved' THEN 1 ELSE 0 END)`。
- **總和必須是 0 時，`ELSE 0` 很重要**；題目要空的桶回傳 `null` 時，則必須**拿掉**它 —— 見
  [4-4](#4-4-pivot-and-unpivot) 的 pivot。
- MySQL 允許 `GROUP BY` 使用 `SELECT` 的別名（`month`）；大多數其他資料庫不行。

LC：[1193](https://leetcode.com/problems/monthly-transactions-i/)

<!-- 4a4ea581d0a3 -->
### 4-2) 比率與百分比

**比率就是 0/1 條件的平均：`AVG(condition)` 就是條件成立的列所佔的比例。最後只四捨五入一次。**

<!--CODE-->

<!--CODE-->

- **MySQL 的 `/` 是小數除法**（`7 / 2` 是 `3.5000`）；`DIV` 才是整數除法。同一個查詢在
  SQL Server 或 PostgreSQL 會遇到的整數除法 bug，在這裡不會發生。
- **四捨五入最終的值，絕不是各個部分。** `ROUND(a, 2) / ROUND(b, 2)` 會在最後一位和預期答案有出入。
- **`AVG` 會跳過 `NULL`。** 只有條件永遠不是 `NULL` 時，`AVG(condition)` 才是列的比例：一列 `TRUE`、
  一列 `NULL` 的平均是 1，不是 0.5。每一列都該算進分母時，先把 `NULL` 對應成 0 ——
  `AVG(IFNULL(condition, 0))`。在 LC 1934 裡，`NULL` 只來自完全沒有訊息的使用者，他整個平均就是
  `NULL`，由外層的 `IFNULL(…, 0)` 處理。
- **檢查分母的粒度。** 「玩家的比例」要除以 `COUNT(DISTINCT player_id)`，而不是列數
  （見 [5-4](#5-4-retention-the-day-after-the-first-login)）。

LC：[1211](https://leetcode.com/problems/queries-quality-and-percentage/)、
[1934](https://leetcode.com/problems/confirmation-rate/)

<!-- 3c9fbfce385e -->
### 4-3) 「全部都有」：relational division

**計算每個實體擁有的不同項目數，和現有的項目總數比較。**

<!--CODE-->

- `DISTINCT` 不可少：一位顧客買了兩次產品 5、從沒買過產品 6，會有兩列，`COUNT(*) = 2` 就會讓他過關。

LC：[1045](https://leetcode.com/problems/customers-who-bought-all-products/)

<!-- fee532a1a71c -->
### 4-4) Pivot 與 unpivot

**pivot（列轉欄）：每個輸出欄位一個條件彙總；unpivot（欄轉列）：每個欄位一個 `SELECT`，用
`UNION ALL` 接起來。**

<!--CODE-->

<!--CODE-->

<!--CODE-->

- 這個 pivot **沒有 `ELSE 0`**：LC 1179 要沒有營收的月份回傳 `null`，而沒有 `ELSE` 的 `CASE` 會得到
  `NULL`，`SUM` 會忽略它。
- unpivot 用 `UNION ALL`。這裡改成單純的 `UNION` 結果相同，因為 `(product_id, store)` 已經讓每一列
  都唯一，但它會白白多做一次去重排序 —— 而在列不唯一時，它會悄悄丟掉真正的資料列。

LC：[1179](https://leetcode.com/problems/reformat-department-table/)、
[1795](https://leetcode.com/problems/rearrange-products-table/)

<!-- 75993c0d6270 -->
### 4-5) 字串：`GROUP_CONCAT`、`CONCAT`、`LIKE`

**`GROUP_CONCAT` 把一組的值串成一個字串；`CONCAT`、`UPPER`、`LOWER` 與 `SUBSTRING` 用來重組
一個值；pattern 裡帶空格的 `LIKE` 能比對一個完整的字詞。**

<!--CODE-->

<!--CODE-->

<!--CODE-->

- `'%DIAB1%'` 也會比對到 `SADIAB100`，那裡的 `DIAB1` 藏在另一個字詞裡。兩個 pattern 合起來只在
  字串開頭或空格之後比對它。
- `GROUP_CONCAT` 的輸出上限是 `group_concat_max_len`（預設 1024 bytes），超過就悄悄截斷 ——
  在 LeetCode 上無關緊要，在正式環境值得知道。

LC：[1484](https://leetcode.com/problems/group-sold-products-by-the-date/)、
[1527](https://leetcode.com/problems/patients-with-a-condition/)、
[1667](https://leetcode.com/problems/fix-names-in-a-table/)

<!-- f7cb58d0cddc -->
## 5) 比較列與列 ⭐⭐⭐⭐⭐

<!-- 9ec7a0f9da91 -->
### 5-1) 「比前一天高」：依日期 join，而不是依前一列

**「昨天」是一個日期，不是前一列。把表依 `date - INTERVAL 1 DAY` 和自己 join，缺少的日子就不會配對到。**

<!--CODE-->

<!--CODE-->

- **`LAG(temperature) OVER (ORDER BY recordDate)` 在這裡會錯**：它拿 1 月 5 日和前一*列* 1 月 3 日
  比較，回傳 id 4。只有題目指的是前一列時才用 `LAG`，否則也要檢查間隔
  （`DATEDIFF(recordDate, LAG(recordDate) OVER (…)) = 1`）。
- `DATEDIFF(a, b)` 是以天計的 `a − b` —— 參數順序很重要。
- 永遠不要依 `id - 1` join；id 不是日期，也不保證連續。

LC：[197](https://leetcode.com/problems/rising-temperature/)

<!-- f558c66a3158 -->
### 5-2) 同一個值連續出現 N 次：`LAG` / `LEAD`

**用 `LAG(x, 1)` 與 `LAG(x, 2)` 往回看，保留三者相同的列。**

<!--CODE-->

- `DISTINCT` 是必要的：連續四個 `1` 會配對到兩次。
- self-join 寫法 `l2.id = l1.id + 1 AND l3.id = l1.id + 2` 假設 id 沒有間隔；依 `id` 排序的 `LAG` 沒有這個假設。
- 若是 N 很大或不固定的「連續 N 次」，改用 gaps and islands（[5-3](#5-3-gaps-and-islands)）。

LC：[180](https://leetcode.com/problems/consecutive-numbers/)

<!-- 65dc7285fb7f -->
### 5-3) Gaps and islands

**依序替符合條件的列編號，再用 id（或日期）減掉這個編號：同一段不中斷的連續列會得到相同的差值，
所以這個差值就是這一段的分組 key。**

<!--CODE-->

<!--CODE-->

- 若是日期，key 是 `DATE_SUB(visit_date, INTERVAL rn DAY)` —— 連續的日子減掉連續的數字，得到同一個日期。
- **為什麼要 `CAST`：在 MySQL 裡 `ROW_NUMBER()` 是 `BIGINT UNSIGNED`**，所以單純寫
  `id - ROW_NUMBER()`，只要結果會是負數就會丟出 *"BIGINT UNSIGNED value is out of range"* ——
  id 從 0 開始時，第一列就是 `0 - 1`。兩邊都轉成 `SIGNED`，分組不變，而且任何 id 都能用。
- 依 `grp` 分組取 `MIN(id)` 與 `MAX(id)` 就是這一段的起點與終點 —— LC 1285 問的正是這個。

LC：[601](https://leetcode.com/problems/human-traffic-of-stadium/)、
[1285](https://leetcode.com/problems/find-the-start-and-end-number-of-continuous-ranges/)

<!-- 67d93711b9be -->
### 5-4) 留存率：首次登入的隔天

**找出每位玩家的第一天，再找第一天隔天的那一列，最後除以玩家人數。**

<!--CODE-->

- `first_login` 每位玩家一列，所以 `COUNT(*)` 就是玩家人數，而 `COUNT(a.player_id)` 只計算找到
  隔天那一列的玩家。
- 隔天的條件要留在 `ON`。放到 `WHERE` 會把沒有回來的玩家從分母移除，答案變成 `1.00`（沒有人回來時則是 `NULL`）——
  見
  [3-4](#3-4-count-vs-countcolumn-after-a-left-join)。
- `DATEDIFF(a.event_date, f.first_day) = 1` 是同一個條件；答案是比例（`0.33`），不是百分比。
- 「在**任何一次**登入的隔天又登入」是另一個、更簡單的問題；這一題只以第一天為基準。

LC：[550](https://leetcode.com/problems/game-play-analysis-iv/)

<!-- 0341f019d65d -->
## 6) 累計總和與移動視窗 ⭐⭐⭐⭐

<!-- 50f77820735f -->
### 6-1) 累計總和

**`SUM(x) OVER (ORDER BY …)` 是到這一列為止（含這一列）所有列的總和。**

<!--CODE-->

- 有 `ORDER BY` 而沒寫 frame 時，預設 frame 是 `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`，
  它會包含和目前這列**同分**的每一列。這裡 `turn` 是唯一的，所以沒差；排序 key 不唯一時，要寫
  `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` 或加上 tie-breaker。

LC：[1204](https://leetcode.com/problems/last-person-to-fit-in-the-bus/)

<!-- d2627942e086 -->
### 6-2) N 天的移動總和與平均

**先收斂成每天一列，再用 frame。對日期用 `RANGE` frame 是以日曆天計算，缺少的日子不會把視窗拉長。**

<!--CODE-->

<!--CODE-->

- **先依天分組。** 表裡 1 月 10 日有兩次造訪。對原始列開 frame 時，`ROWS` frame 會把它們算成七「天」
  中的兩天；`RANGE` frame 仍會加總正確的七個日曆天，但每次造訪各輸出一次，所以 1 月 10 日會在結果裡
  出現兩次。先分組才會每天一列。
- `ROWS BETWEEN 6 PRECEDING AND CURRENT ROW` 指的是最後七**列**，只有在沒有缺少任何一天時才等於七天。
  `RANGE … INTERVAL 6 DAY` 兩種情況都正確。
- 對 `first_day` 的 `WHERE` 會丟掉前六天，那幾天的視窗還沒滿。

LC：[1321](https://leetcode.com/problems/restaurant-growth/)

<!-- 7a76b98fe1e3 -->
## 7) 更新與重塑 ⭐⭐⭐

<!-- 06da94c820dc -->
### 7-1) 搭配 `CASE` 的 `UPDATE`

**一個帶 `CASE` 的 `UPDATE` 就能在單一敘述裡交換值，不需要暫存值。**

<!--CODE-->

- 分成兩個敘述（先 `'m'` → `'f'`，再 `'f'` → `'m'`）會把所有人都變成 `'m'`；單一 `UPDATE` 是依每一列
  原本的值計算 `CASE`。

LC：[627](https://leetcode.com/problems/swap-salary/)

<!-- 395fb8d3909c -->
### 7-2) 交換相鄰的列

**奇數 id 取下一列的值，偶數 id 取上一列的值；最後一個奇數 id 沒有搭檔，保留自己的值。**

<!--CODE-->

<!--CODE-->

- 兩種寫法都依賴 id 恰好是 1..n，這是 LC 626 保證的：第二種改寫 id，第一種用 `id % 2` 決定每一列的
  搭檔。id 是 1, 3, 4 時，第一種會配錯列。若 id 任意，先在 CTE 裡算 `ROW_NUMBER() OVER (ORDER BY id)`，
  並在奇偶判斷裡用它取代 `id`。

LC：[626](https://leetcode.com/problems/exchange-seats/)

<!-- 135609f82067 -->
### 7-3) 一對裡的兩邊都要算：`UNION ALL`

**用 `UNION ALL` 把兩個欄位疊成一個，再分組 —— 每一筆被接受的請求，對雙方來說都是一位朋友。**

<!--CODE-->

- **在這裡用 `UNION` 是 bug**：它會移除重複，於是出現在三筆請求裡的 3 號只會出現一次、算成 1。
  `UNION ALL` 保留每一列，而且因為省掉去重也比較快。

LC：[602](https://leetcode.com/problems/friend-requests-ii-who-has-the-most-friends/)

<!-- 0d761bcee914 -->
## 8) 會讓提交失敗的 MySQL 細節 ⭐⭐⭐⭐

<!--CODE-->

<!--CODE-->

| 細節 | 會發生什麼 | 這樣做 |
|---|---|---|
| `/` vs `DIV` | `/` 是小數除法，`DIV` 會截斷 | 比率用 `/`，再 `ROUND(x, 2)` |
| `DATEDIFF(a, b)` | 以整天計的 `a − b` | 較晚的日期放前面 |
| 日期運算 | `d + INTERVAL 1 DAY` 會處理月底與閏年 | 絕不要把日期當數字加 1 |
| 月份分桶 | `DATE_FORMAT(d, '%Y-%m')` → `2024-02` | 依格式化後的值分組 |
| `WHERE` 裡的 window function | 不允許，而且 MySQL 沒有 `QUALIFY` | 用 CTE 或衍生表，在外層過濾 |
| 用保留字當別名 | `rank`、`row_number` 與 `year_month`（一個 `INTERVAL` 單位）在 MySQL 8 是保留字 | 加反引號 `` AS `rank` ``，或換個名字 |
| `LIMIT` | 只接受字面值或變數，不接受 `N - 1` | 先 `SET N = N - 1;`（[1-2](#1-2-the-n-th-highest-value-null-when-there-is-none)） |
| Error 1093 | 不能在子查詢裡讀正在被更新或刪除的表 | 包一層衍生表（[2-2](#2-2-delete-duplicates-keep-one)） |
| `ONLY_FULL_GROUP_BY` | MySQL 8 預設 `sql_mode` 會開啟：被選取的欄位必須在分組裡或被彙總 | 依每個非彙總欄位分組 |
| `GROUP BY` 別名 | MySQL 接受用 `GROUP BY month` 指 `SELECT` 的別名 | 這裡可以；其他資料庫可能拒絕 |
| `UNION` vs `UNION ALL` | `UNION` 會去重 | 除非重複必須去掉，否則用 `UNION ALL`（[7-3](#7-3-count-both-sides-of-a-pair-union-all)） |
| `ROW_NUMBER()` 的運算 | 回傳 `BIGINT UNSIGNED`，差值為負就報錯 | `CAST(… AS SIGNED)`（[5-3](#5-3-gaps-and-islands)） |
| `NULL` 比較 | `= NULL`、`<> 2` 與 `NOT IN (…, NULL)` 會丟掉列 | `IS NULL`、`<=>`、`NOT EXISTS`（[3](#3-nulls-and-missing-rows-)） |

<!-- b28addbbc000 -->
## 9) 技巧 → 題目索引 ⭐⭐⭐

| 技巧 | 題目 | 章節 |
|---|---|---|
| `DENSE_RANK`、第 N 個不同的值 | 176, 177, 178, 185 | [1-1](#1-1-row_number-vs-rank-vs-dense_rank)、[1-2](#1-2-the-n-th-highest-value-null-when-there-is-none) |
| 每組 Top-N / 最高 | 184, 185 | [1-3](#1-3-top-n-per-group) |
| 每組第一列或最新一列 | 511, 1070 | [1-4](#1-4-first-or-latest-row-per-group) |
| 找出 / 刪除重複 | 182, 196 | [2-1](#2-1-find-the-duplicated-values)、[2-2](#2-2-delete-duplicates-keep-one) |
| `HAVING COUNT(*) = 1`，用 `MAX` 得到 `NULL` | 619 | [2-3](#2-3-values-that-appear-exactly-once) |
| `WHERE` 裡的 `NULL`、`<=>` | 584 | [3-1](#3-1--and--drop-null-rows) |
| Anti-join | 183 | [3-2](#3-2-never-did-x-the-anti-join) |
| `CROSS JOIN` + `LEFT JOIN` + `COUNT(col)`、`IFNULL` | 1251, 1280 | [3-3](#3-3-keep-the-rows-with-zero-matches) |
| `COUNT(*)` vs `COUNT(col)`、`WHERE` 在 `COUNT` 之前執行 | 550, 1280 | [3-4](#3-4-count-vs-countcolumn-after-a-left-join) |
| 條件彙總 | 1193 | [4-1](#4-1-conditional-aggregation) |
| `AVG(condition)` 比率 | 1211, 1934 | [4-2](#4-2-rates-and-percentages) |
| Relational division | 1045 | [4-3](#4-3-has-all-of-them-relational-division) |
| Pivot / unpivot | 1179, 1795 | [4-4](#4-4-pivot-and-unpivot) |
| `GROUP_CONCAT`、字串函式、`LIKE` | 1484, 1527, 1667 | [4-5](#4-5-strings-group_concat-concat-like) |
| 依日期 self-join | 197 | [5-1](#5-1-higher-than-the-day-before-join-on-the-date-not-on-the-row-before) |
| `LAG` / `LEAD` | 180, 626 | [5-2](#5-2-the-same-value-n-times-in-a-row-lag--lead)、[7-2](#7-2-swap-adjacent-rows) |
| Gaps and islands | 601, 1285 | [5-3](#5-3-gaps-and-islands) |
| 留存率 | 550 | [5-4](#5-4-retention-the-day-after-the-first-login) |
| 累計總和 | 1204 | [6-1](#6-1-running-total) |
| 移動視窗 | 1321 | [6-2](#6-2-moving-sum-and-average-over-n-days) |
| `UPDATE … CASE` | 627 | [7-1](#7-1-update-with-case) |
| 兩個欄位 `UNION ALL` | 602 | [7-3](#7-3-count-both-sides-of-a-pair-union-all) |
