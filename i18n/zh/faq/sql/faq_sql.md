<!-- 74e546e75692 -->
# SQL FAQ

<!-- da1dffe1ecee -->
### 1. 有兩張表 a（3k 筆）、b（4k 筆），
   `select a.Color, b.Size from a cross join b` 的結果有幾筆？

- 3k * 4k = 12k
- https://www.essentialsql.com/cross-join-introduction/
- http://www.sqlservertutorial.net/sql-server-basics/sql-server-cross-join/
- （cross join 示意圖）

<p align="center"><img src="../../pic/cross_join.png" width="500" height="300"></p>

<!-- 8c6169bd097a -->
### 2. SQL 裡 `full outer Join` 和 `Union` 差在哪？
- Full outer Join：把兩張表接起來，給你 1) 對得上的紀錄 + 2) 右表對不上的紀錄 + 3) 左表對不上的紀錄
	- 例如 table A (a,b,c)、table B (c,d)。

- Full outer join：回傳 table A、table B 的所有欄位（不重複）
	- -> `select A*, B.* from A full outer join B on A.c = B.c`
<!--CODE-->
<p align="center"><img src="../../pic/full_outer_join.gif" width="500" height="300"></p>

- Union：把兩個查詢的結果合併成單一個結果集。
	- 例如 table A (a,b,c)、table B (c,d)。
	- -> `select A.a, A.b from A union select B.c, B.d from B `
<!--CODE-->

<p align="center"><img src="../../pic/union_sql.png" width="500" height="300"></p>

- 簡單說：
	- Full outer join：接所有`欄位`（不管 A、B 有沒有相同欄位）
	- Union           ：接所有`列`（前提是 A、B 有相同欄位）
	- Cross join      ：A 與 B 所有欄位的`組合`
- https://www.quora.com/What-is-the-difference-between-full-outer-join-and-union-in-SQL
- https://www.solutionfactory.in/posts/Difference-between-Join-And-Union-in-SQL 

<!-- 013fe5439d73 -->
### 3. 找出某一季的訂單？（不要寫死）

<!--CODE-->


<!--CODE-->

<!-- 13ecd8f9c9f6 -->
### 4. 一個查詢就同時取出值與帶條件的最大值？

<!--CODE-->

<!-- cc704c0a3b65 -->
### 5. 取出大於平均值的資料？

<!--CODE-->

<!-- 4c4534115d86 -->
### 6. 產生 1 -> 100 的整數清單（遞迴 CTE）？
<!--CODE-->

<!-- 7b64dc0c2ac5 -->
### 7. 給定 movie、actor 兩張表（多對多關係），請設計資料模型與查詢，回報某個 movie-id／movie-name 有幾位演員？

- 多對多關係需要一張`關聯（橋接）表`。絕對不要存一串用逗號隔開的 actor id —— 那沒辦法建索引、沒辦法 join、也沒辦法加約束。

<!--CODE-->

<!--CODE-->

- 面試官在聽的幾個點
	- 複合主鍵 `(movie_id, actor_id)` 同時做到去重與正向查詢的索引；反向查詢需要它自己的索引
	- 用 `LEFT JOIN` + `COUNT(ma.actor_id)`（不是 `COUNT(*)`），這樣沒有演員的電影算 0 而不是 1
	- 電影名稱不唯一 —— id 才是真正的 key，名稱只是查詢上的方便
	- 如果這個數字一直被讀，就把它`反正規化`成 `movie.actor_cnt`，用觸發器或應用程式維護，並接受寫入的代價

<!-- 9b024f837398 -->
### 8. 怎麼解「多對多」的資料庫設計問題？
- https://dzone.com/articles/how-to-handle-a-many-to-many-relationship-in-datab

<!-- 9a3e9f51e4bc -->
### 9. `union` 和 `union all` 哪個快？
- `union all` 比較快，因為它**不會**去處理可能的重複。

<!-- 018ed6e9ab77 -->
### 9'. `union` 和 `union all` 差在哪？
- `Union` 會把重複的紀錄去掉（同樣的紀錄只出現一次）
- `Union all` 全部都回傳，包含重複的資料（直接合併）
- https://dataschool.com/learn-sql/what-is-the-difference-between-union-and-union-all/
- http://sqlqna.blogspot.com/2013/08/union-union-all.html

<!-- 792d26ebfc9c -->
### 10. SQL 裡使用變數的例子？

<!--CODE-->

<!-- bd2cbe76bc13 -->
### 11. 刪掉表裡重複的紀錄？
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_SQL/delete-duplicate-emails.sql

<!--CODE-->
- 追問：如果是「整列都重複」的情況呢？

<!--CODE-->

<!-- c90360eb1430 -->
### 12. SQL 怎麼處理 NULL？

- `IS NULL` 與 ` = NULL`

<!--CODE-->

- COALESCE

<!--CODE-->

- 和 Null 相加

<!--CODE-->

- 任何值 + NULL = 任何值？

<!--CODE-->

- NULL != NULL、NULL = NULL 的結果是什麼？

<!--CODE-->
- https://www.codeproject.com/Articles/1017058/Handle-NULL-in-SQL-Server
- https://www.tutorialspoint.com/sql/sql-null-values.htm

<!-- a60fb1d92144 -->
### 13. 什麼時候用子查詢？怎麼用？

- 什麼時候
  - 子查詢用來取出資料，讓主查詢拿它當條件，進一步限縮要撈的資料。
  - 子查詢可以出現在：
    - SELECT 子句
    - FROM 子句
    - WHERE 子句
  - 內層查詢會先於外層執行，這樣內層的結果才能傳給外層。

- 怎麼用

<!--CODE-->

- https://www.tutorialspoint.com/sql/sql-sub-queries.htm
- https://www.w3resource.com/sql/subqueries/understanding-sql-subqueries.php
- https://www.sqlservertutorial.net/sql-server-basics/sql-server-subquery/

<!-- de3f99f92589 -->
### 14. SQL join 與子查詢？

-> 看情況。（效能 vs 可讀性）

- https://www.quora.com/Which-is-faster-joins-or-subqueries
- https://stackoverflow.com/questions/2577174/join-vs-sub-query
- https://stackoverflow.com/questions/3856164/sql-joins-vs-sql-subqueries-performance

<!-- b1a135333bdf -->
### 15. 說明／示範 SQL 的`視窗函數（window function）`？
->
- 視窗函數是對「與目前這一列有某種關聯的一組列」做計算
- 語法樣板
<!--CODE-->
- 範例
<!--CODE-->
- ROW_NUMBER()
  - ROW_NUMBER() 就跟它的名字一樣 —— 顯示某一列的編號。它從 1 開始，依照視窗語句裡 ORDER BY 的順序編號。
- 範例
<!--CODE-->

- RANK() 與 DENSE_RANK()
  - RANK()：和 ROW_NUMBER() 稍有不同。例如你依 start_time 排序時，某些站點可能有兩趟車的出發時間完全一樣。這種情況下它們會拿到相同的名次，而 ROW_NUMBER() 會給它們不同的號碼。在下面的查詢裡可以看到 start_terminal 31000 的第 4、5 筆 —— 它們都拿到名次 4，而下一筆會拿到 6：
  - RANK() 會讓相同的列都拿到名次 2，然後跳過 3 和 4，下一筆就是 5
  - DENSE_RANK() 一樣讓相同的列都拿到 2，但下一列會是 3 —— 名次不會被跳過。
- 範例
<!--CODE-->

- LAG 與 LEAD()
  - 用 LAG 或 LEAD 可以做出「從其他列取值」的欄位 —— 你只要指定要從哪個欄位取、以及要往前／往後幾列。LAG 往前取，LEAD 往後取
- 範例
<!--CODE-->

- NTILE()
  - 視窗函數也能算出某一列落在哪一個百分位（或四分位，或任何其他切分）。語法是 NTILE(*桶數*)。這時 ORDER BY 決定要依哪個欄位來切
- 範例
<!--CODE-->

- 也可以把視窗定義成別名（定義 window alias）
- 範例
<!--CODE-->
- https://mode.com/sql-tutorial/sql-window-functions/
- https://www.postgresql.org/docs/8.4/functions-window.html

<!-- 4fc625bf6b39 -->
### 16. SQL 的 Count(`*`) 與 Count(1)？

- 差別
  - 對`非 null` 計數
    - `Count(col)` 只會數該欄位裡`非 Null` 的值，忽略 NULL。（欄位裡有值的筆數）
  - 對`全部`計數
    - `Count(1)` 不管怎樣都會數所有的值（含 NULL），不忽略 NULL。（全部紀錄數，含 Null）
    - Count(`*`) 和 Count(1) `相同`（考慮表裡所有欄位），也不忽略 NULL

- 效能
  - 若該欄位是主鍵，count(col) 比 count(1) 快
  - 若該欄位不是主鍵，count(1) 比 count(col) 快
  - 若表上沒有主鍵，count(1) 比 count(col) 快
  - 若表上有主鍵，count(col) 最快

- 參考
  - https://stackoverflow.com/questions/3003457/count-vs-countcolumn-name-which-is-more-correct

- 範例
<!--CODE-->

- https://kknews.cc/tech/mro8689.html

<!-- 425d450034a6 -->
### 17. SQL 的 `Where` 與 `Having`？
->
- `having` 作用在彙總後的資料上
- 除了 SELECT，WHERE 也可以搭配 UPDATE 與 DELETE；HAVING 則只能用在 SELECT 查詢裡。
- `WHERE` 用來過濾列，套用在每一`列`上；而 `HAVING` 用來過濾 SQL 裡的`群組`。
- 語法層面的一個差別是：WHERE 寫在 GROUP BY 之前，HAVING 寫在 GROUP BY 之後。
- 當帶彙總函數的 SELECT 同時用到 WHERE 與 HAVING 時，WHERE 會先套用在個別列上，只有通過條件的列才會拿去分組。分完組之後，再由 HAVING 依條件過濾群組。

- 範例
<!--CODE-->
- https://www.geeksforgeeks.org/having-vs-where-clause-in-sql/
- https://javarevisited.blogspot.com/2013/08/difference-between-where-vs-having-clause-SQL-databse-group-by-comparision.html

<!-- c1dfff85623d -->
### 18. SQL 的 `case when.. then.. else end` CASE 運算式？
->
- 語法樣板
<!--CODE-->
- 範例
<!--CODE-->
- https://www.db2tutorial.com/db2-basics/db2-case-expression/
- https://chartio.com/resources/tutorials/how-to-use-if-then-logic-in-sql-server/

<!-- 0846cab77116 -->
### 19. 什麼時候用 `right`、`left`、`inner`、`full outer` join？

<p align="center"><img src="../../pic/sql_join.jpg" width="700" height="500"></p>

- OUTER join：在 inner join 的基礎上，把對不上的列也放進最終結果
- LEFT outer：把寫在 join 條件左邊那張表裡對不上的列也帶進來
  - LEFT outer join = INNER JOIN + 左表對不上的列
- RIGHT outer：把寫在 join 條件右邊那張表裡對不上的列也帶進來
  - RIGHT OUTER join = INNER JOIN + 右表對不上的列

- https://sqlhints.com/2016/10/15/difference-between-left-outer-join-and-right-outer-join-in-sql-server/
- https://javarevisited.blogspot.com/2013/05/difference-between-left-and-right-outer-join-sql-mysql.html

<!-- 91e192af9269 -->
### 20. SQL 的彙總函數
->
- https://www.geeksforgeeks.org/aggregate-functions-in-sql/

- 範例
<!--CODE-->
<!--CODE-->

<!-- 7bfda011ddb0 -->
### 21. 說明／示範 SQL 的`階層查詢`？

- 「階層查詢」＝走訪一張`自我參照`的表（員工 → 主管、分類 → 上層分類、留言 → 上層留言）。可攜的工具是`遞迴 CTE`。

<!--CODE-->

<!--CODE-->

<!--CODE-->

- 注意事項
	- 用 `UNION ALL`（不是 `UNION`）—— 每一輪都去重很貴，而且在這裡通常是錯的
	- 把 join 反過來寫（`o.manager_id = e.emp_id`）就能改成`往上`走到根節點
	- 一定要限制遞迴 —— 上面那個深度守衛就是用來擋掉環的，同時也把結果限制在 10 層。
	  如果想偵測環而不是限制深度，就用 **id** 組出路徑（`,1,4,9,`），
	  然後測 `NOT (id_path LIKE '%,' || e.emp_id || ',%')`；
	  用名字比對會在兩個人同名、或一個名字包含另一個名字時出錯。
	  PostgreSQL 14+ 與 SQL Server 也有內建的 `CYCLE` 子句
	- 各家也有自己的捷徑 —— Oracle 的 `CONNECT BY PRIOR`、SQL Server 的 `hierarchyid` —— 但遞迴 CTE 是 ANSI 標準，MySQL 8+、Postgres、SQL Server 與 SQLite 都能跑
	- 讀取為主時的替代方案：`物化路徑`（存 `/1/4/9/`）、`巢狀集合`，或`閉包表`（每一對祖先-後代一列）—— 全都是拿寫入成本換 `O(1)` 的子樹讀取
	- 也見下面第 23 題

<!-- 07c6addf1912 -->
### 22. 資料庫建模的題目？
- https://www.toptal.com/data-modeling/interview-questions
- https://mindmajix.com/data-modeling-interview-questions
- https://www.indeed.com/career-advice/interviewing/data-modeling-interview-questions

<!-- 441ed81bc694 -->
### 23. SQL 遞迴 CTE
- https://www.sqlservertutorial.net/sql-server-basics/sql-server-recursive-cte/
<!--CODE-->

<!-- e4748fe47f4a -->
### 24. 說明 `<>` 的意思？
<!--CODE-->

<!-- 6f553ad692f5 -->
### 25. `char` 與 `varchar`？
- https://stackoverflow.com/questions/1885630/whats-the-difference-between-varchar-and-char#:~:text=CHAR%20is%20fixed%20length%20and,to%20store%20the%20actual%20text.&text=The%20char%20is%20a%20fixed,variable%2Dlength%20character%20data%20type.
- CHAR 是固定長度，VARCHAR 是可變長度。CHAR 每一筆都用掉同樣的儲存空間，VARCHAR 則只用實際存放文字所需的空間。
- VARCHAR：
  - 存放可變長度的字串，是最常見的字串型別。它通常比固定長度型別省空間，因為它只用需要的空間（值比較短就用比較少空間）。例外是用 ROW_FORMAT=FIXED 建的 MyISAM 表，它每一列在磁碟上都佔固定空間，因此可能浪費。VARCHAR 因為省空間而有助於效能。
- CHAR：
  - 是固定長度：MySQL 一律為指定的字元數配足空間。存放 CHAR 值時，MySQL 會把尾端空白去掉。（MySQL 4.1 與更舊的版本裡 VARCHAR 也是如此 —— CHAR 與 VARCHAR 邏輯上完全一樣，只差在儲存格式。）比較時會視需要用空白補齊。

<!-- 0f2549a55734 -->
### 26. `lead` 與 `lag`？
- https://riptutorial.com/sql/example/27455/lag-and-lead
- LEAD 函數提供結果集中目前這一列「之後」那些列的資料。例如在 SELECT 裡，你可以拿目前這列的值和下一列的值做比較。
- 範例：
<!--CODE-->

<!-- 1d1396f1fc0b -->
### 27. 說明`外鍵（foreign key, fk）`？
- https://www.w3schools.com/sql/sql_foreignkey.asp
- https://www.cockroachlabs.com/docs/stable/foreign-key.html
- https://b-l-u-e-b-e-r-r-y.github.io/post/ForeignKey/
- 也叫做`FOREIGN KEY 約束`
- 外鍵約束用來阻止那些會破壞表與表之間連結的操作。
- 帶外鍵的那張表叫子表，帶主鍵的那張表叫被參照表或父表。
- 任何 CRUD 操作都會受外鍵約束限制
  - -> 擋掉那些會破壞（有外鍵的）表之間資料一致性的非法操作
  - -> 也就是說，我們得對`所有帶同一個外鍵約束的表`一起做操作

<!-- 2b0c05942b3d -->
### 28. 說明 data mart 與 data warehouse 的差別？
<p align="center"><img src="../../pic/dm_dw2.png" width="700" height="300"></p>
<p align="center"><img src="../../pic/dm_dw.png" width="700" height="500"></p>

- https://aws.amazon.com/data-warehouse/?nc1=h_ls

<!-- dde8b425de36 -->
### 29. Rank() 的範例？
<!--CODE-->

<!-- 7336f1a7cf9a -->
### 30. `EXISTS` 的範例？
- https://www.fooish.com/sql/exists.html
<!--CODE-->

<!-- ec9294370339 -->
### 31. 說明 `cross join`？
- https://www.fooish.com/sql/cross-join.html
- Cross join 會回傳`給定欄位與表之間所有可能的列組合`
- 注意：下面幾個 SQL 彼此等價
<!--CODE-->

<!-- c32243a6c377 -->
### 32. Left join 的範例？
- 注意：要用 `IS NULL`（而不是「= null」）
<!--CODE-->

<!-- bf0729d83bc5 -->
### 33. 多欄位的 where in
<!--CODE-->

<!-- 824a7cb6bcf0 -->
### 34. 中等偏難的資料分析師 SQL 面試題
- https://quip.com/2gwZArKuWk7W
- 自我 join 練習題
  - MoM（逐月）百分比變化：算出月活躍使用者（MAU）的逐月百分比變化。
<!--CODE-->
<!--CODE-->
- 視窗函數練習題
- 其他中等／困難的 SQL 練習題

<!-- ab9802f7e9c8 -->
### 35. SQL 依不同欄位分別做遞減與遞增排序
<!--CODE-->
