<!-- c2c3cb1f2af0 -->
# 讀懂 SQL `EXPLAIN` 執行計畫

> **範圍** —— 資料工程面試所考的查詢計畫判讀：`EXPLAIN`、`EXPLAIN ANALYZE` 與 `BUFFERS` 各自回報什麼、如何走讀計畫樹、scan 節點、估計列數與實際列數、三種 join 演算法在計畫中的樣子、sort 與 hash 的 spill、buffer 與 I/O 計數器、一個慢查詢的完整診斷，以及同一套判讀方式如何套用到 Spark SQL。以 PostgreSQL 16–18 為準。SQL 本身的寫法、索引設計與資料倉儲的調校手段歸 [sql_for_data.md](./sql_for_data.md)；Spark 的執行模型與調校歸 [spark.md](./spark.md)。
> **另見**：[`./sql_for_data.md`](./sql_for_data.md) —— §10 查詢效能，本文的精簡版；[`./spark.md`](./spark.md) —— Spark 計畫背後的 shuffle、AQE 與 skew；[`../db/faq_DB_DW.md`](../db/faq_DB_DW.md) —— MySQL `EXPLAIN` 的 `type` 欄位。

SQL 描述你**要什麼**資料；執行計畫則是資料庫決定**如何取得**它的方式。`EXPLAIN`
讓你讀到這個決定，`EXPLAIN ANALYZE` 讓你拿它和實際發生的情況對照。除非該節另有說明，
以下計畫都是 PostgreSQL，而且都只保留重要的幾行。

---

<!-- b912af3d437c -->
## 0) 一句話回答 ⭐⭐⭐⭐⭐

**「我會執行 `EXPLAIN (ANALYZE, BUFFERS)`，由下往上讀這棵樹，先找估計列數與實際列數
差距最大的節點 —— 大多數爛計畫的根本原因都是估計錯誤。接著檢查 scan 有沒有讀了又丟掉的資料、
join 有沒有選錯演算法、sort 或 hash 有沒有 spill 到磁碟。每次只改一件事，然後重跑。」**

一份計畫回答六個問題：

1. Optimizer 為什麼選這個策略？
2. 哪個節點花的時間最多？
3. 查詢是否讀了資料又把它丟掉？
4. 有沒有 join 用錯演算法？
5. Optimizer 的列數估計對不對？
6. 那麼，真正的瓶頸在哪裡？

---

<!-- fa277b6cf99d -->
## 1) `EXPLAIN` 顯示什麼 ⭐⭐⭐⭐⭐

<!-- 951f65e9b2f9 -->
### 1-1) 從 SQL 到執行計畫

**決定 scan 方式、join 順序、join 演算法以及要不要排序的是 optimizer，不是 SQL —— 而它是
根據統計資訊做決定，不是根據資料本身。**

<!--CODE-->

成本模型的輸入包括資料表**統計資訊**（列數、相異值個數、最常見的值、直方圖 —— 由 `ANALYZE` /
autovacuum 收集）、可用的**索引**，以及 `seq_page_cost`、`random_page_cost` 等成本參數。
統計資訊一旦錯了，計畫就是為一份不存在的資料挑的。

<!-- bff99c068378 -->
### 1-2) `EXPLAIN` vs `EXPLAIN ANALYZE` vs `BUFFERS` ⭐⭐⭐⭐⭐

**`EXPLAIN` 是預測；`EXPLAIN ANALYZE` 會真的執行查詢並回報實際情況；`BUFFERS`
再加上每個節點碰了多少個 8 KB 的 page。**

| 指令 | 會真的執行查詢嗎？ | 顯示 |
|---|---|---|
| `EXPLAIN` | 否 | 選中的計畫，含估計成本、列數與寬度 |
| `EXPLAIN ANALYZE` | **會** | 計畫，加上每個節點的實際時間、實際列數與 loops |
| `EXPLAIN (ANALYZE, BUFFERS)` | **會** | 再加上每個節點的 shared / local / temp page 數 |
| `EXPLAIN (FORMAT JSON)` | 否（除非加 `ANALYZE`） | 同一份計畫的 JSON 版，供計畫視覺化工具使用 |

- PostgreSQL 18 只要指定 `ANALYZE` 就會自動包含 `BUFFERS`。
- 查詢慢到跑不完或危險到不能跑時，用純 `EXPLAIN`；其他情況都用 `ANALYZE`，因為只有它能顯示
  估計在哪裡出錯。

<!-- 99f2610c1976 -->
### 1-3) 讀一個節點：cost、rows、width

<!--CODE-->

<!--CODE-->

| 欄位 | 意義 |
|---|---|
| `Seq Scan on orders` | 節點類型與它讀取的資料表 |
| `cost=0.00..18000.00` | **啟動成本 .. 總成本**：輸出第一列之前的成本，以及輸出全部列的成本 |
| `rows=500000` | 這個節點估計**輸出**的列數（套用過濾條件之後） |
| `width=64` | 估計每列輸出的平均 bytes |
| `Filter:` | 節點讀到的每一列都要檢查的條件 —— 不符合的列被丟棄 |

**`cost` 不是毫秒。** 它是任意單位，以循序讀取一個 page 的成本 `seq_page_cost` = 1.0 為基準，
只能用來比較同一台伺服器上同一個查詢的不同計畫。父節點的成本包含子節點的成本。

<!-- dec2cb39e72a -->
### 1-4) `ANALYZE` 多給了什麼：actual time、rows、loops

<!--CODE-->

<!--CODE-->

| 欄位 | 意義 |
|---|---|
| `actual time=0.050..850.000` | 到**第一**列的毫秒數 .. 到**最後**一列的毫秒數，以每次 loop 計 |
| `rows=1200000` | 實際輸出的列數，**以每次 loop 計** |
| `loops=1` | 這個節點被執行了幾次 |
| `Rows Removed by Filter` | 讀了又丟掉的列 —— 數字高代表 scan 讀的遠比留下的多 |
| `Planning Time` | 產生計畫的時間 |
| `Execution Time` | 執行的時間，不含把結果送到 client 的時間 |

這裡估計是 500,000 列，實際是 1,200,000 列。**先比較估計列數與實際列數，再看時間**：
時間告訴你哪裡痛，列數落差告訴你為什麼痛。

**`loops` 很重要。** 當 `loops > 1` 時，`actual time` 與 `rows` 是**每次 loop 的平均值**。
乘上 `loops` 才是該節點的總量：`actual time=0.010..0.020 rows=1 loops=1000000` 約等於
20 秒、一百萬列，而不是 0.02 毫秒、一列。

<!-- c415b920b61c -->
### 1-5) `EXPLAIN ANALYZE` 會真的執行語句

**`EXPLAIN ANALYZE DELETE …` 真的會刪掉資料。** 想量測寫入操作又不想保留結果，就把它包在
transaction 裡再 rollback：

<!--CODE-->

- Rollback 會還原資料列，但不是每個副作用都能還原：已取用的 sequence 值不會退回，
  trigger 對資料庫外部送出的東西也已經送出去了。
- `DELETE` 在 rollback 之前仍會持有它的 row lock，所以不要在忙碌的正式環境資料表上這樣做。

---

<!-- f104f9b49216 -->
## 2) 讀懂計畫樹 ⭐⭐⭐⭐⭐

<!-- 96608a0e3d3f -->
### 2-1) 由下往上：每個子節點餵給它的父節點

**計畫是一棵樹：縮排最深的節點讀取資料表，上面的節點負責過濾、join、排序與彙總，最上層的
節點回傳結果。從葉節點往上讀。**

<!--CODE-->

<!--CODE-->

- `->` 開頭的一行是子節點；縮排說明它餵給哪個父節點。
- 同一個節點的兩個子節點不一定同時執行：`Hash Join` 會先把 `Hash` 子節點完整建好，
  再讓另一個子節點的資料流過它。
- 沒有 `->` 的行（`Filter:`、`Hash Cond:`、`Group Key:`）是上方節點的細節，不是節點。
- 在大表上你常會看到 `Gather` 底下掛著 `Parallel Seq Scan`，並有一行 `Workers Launched:` ——
  這是 PostgreSQL 用多個 worker process 一起執行同一個節點。

<!-- e0d83f5380c9 -->
### 2-2) 你會遇到的節點類型

| 節點 | 做什麼 | 要檢查什麼 |
|---|---|---|
| `Seq Scan` | 讀取資料表的每一個 page | `Filter` 移除了多少列 |
| `Index Scan` | 走訪索引，再到資料表取回每一筆符合的列 | 列數多寡；每列一次隨機 I/O |
| `Index Only Scan` | 只靠索引回答 | `Heap Fetches` —— 仍需回資料表讀取的列數 |
| `Bitmap Index Scan` + `Bitmap Heap Scan` | 先收集符合條件的列位置，再依序讀取那些 page | `Heap Blocks: lossy` |
| `Nested Loop` | 外層每一列都執行一次內層 | 內層的 `loops` |
| `Hash Join` + `Hash` | 把一側建成 hash table，用另一側去查 | `Hash` 節點上的 `Batches` |
| `Merge Join` | 合併兩個已依 join key 排序的輸入 | 底下的 `Sort` 節點 |
| `Sort` | 排序輸入 | `Sort Method` —— `external merge` 代表有 spill |
| `HashAggregate` | 用 hash table 分組 | `Batches` / `Disk Usage` |
| `GroupAggregate` | 對已排序的輸入分組 | 底下的 `Sort` |
| `Limit` | 取到 n 列就停 | 子節點是否真的能提早停下 |
| `Gather` / `Gather Merge` | 收集 parallel worker 的輸出 | `Workers Launched` 與 `Workers Planned` |

---

<!-- 5f6a173f2793 -->
## 3) Scan ⭐⭐⭐⭐

假設 `orders` 有一千萬列，且 `order_date` 上有索引：

<!--CODE-->

<!-- 990827b4c218 -->
### 3-1) Seq Scan

**Seq Scan 讀取每一個 page、檢查每一列。只要查詢需要資料表的一大部分，它就是正確的選擇 ——
看到它本身並不代表有問題。**

<!--CODE-->

以下情況它是最便宜的計畫：

- 查詢需要大部分的列（透過索引讀一千萬列中的九百萬列，遠比依序把整張表讀一遍貴）；
- 資料表小到只占幾個 page；
- 沒有索引符合條件，或符合的索引選擇性不夠。

當一張大表被整張掃過、只為了回傳少數幾列時，它才是問題 —— 訊號是 `Rows Removed by Filter`
很高、輸出卻很少。

<!-- 1b3aa8de2c4a -->
### 3-2) Index Scan 與 Index Only Scan

**Index Scan 先在索引中找到符合的項目，再到資料表取回每一列。條件選擇性高時 —— 從很多列中
只取少數幾列 —— 它會勝出。**

<!--CODE-->

- `Index Cond` 在索引內就套用；Index Scan 底下的 `Filter` 行則是在取回資料列之後才檢查，
  所以被它移除的列都是白取的。
- 每次取回資料列都可能是一次隨機 page 讀取，這就是符合比例越高、索引越吃虧的原因。
- 當所需欄位全都在索引裡（例如透過 `INCLUDE`），**Index Only Scan** 就不必讀資料表。但對於
  visibility map 沒有標記為 all-visible 的 page，它仍得回資料表讀取；`Heap Fetches` 計算的就是這些，
  最近做過 `VACUUM` 就會降下來。

<!-- db36e0fd54b8 -->
### 3-3) Bitmap Heap Scan

**Bitmap scan 是折衷方案：先從索引收集所有符合的列位置，再依實體順序讀取資料表的 page，
每個 page 只讀一次。**

<!--CODE-->

- 它適合符合列數太多、不適合 Index Scan，但又太少、不值得 Seq Scan 的條件，而且它能結合多個
  索引（`BitmapAnd` / `BitmapOr`）。
- Bitmap 若超過 `work_mem` 就會變成 **lossy** —— 只記住整個 page 而不是個別列 —— 這些 page 上的
  每一列都要用 `Recheck Cond` 重新檢查。`ANALYZE` 輸出中的 `Heap Blocks: lossy=…` 表示發生了這件事。

<!-- 47b9fd1093ad -->
### 3-4) 「明明有索引，為什麼還是 Seq Scan？」 ⭐⭐⭐⭐⭐

**要嘛 planner 認為 Seq Scan 比較便宜（常常是對的），要嘛這個條件根本用不了索引。**

| 原因 | 例子 | 解法 |
|---|---|---|
| 查詢需要資料表的一大部分 | `orders` 有 20% 符合 | 不用解 —— Seq Scan 是對的；強制走索引前先量測 |
| 資料表很小 | 50 列的對照表 | 不用解 |
| 過舊的統計資訊讓符合列數看起來很大 | 大量匯入之後 | `ANALYZE orders;` |
| 對欄位套了函式或運算 | `WHERE DATE(order_ts) = …`、`WHERE amount * 2 > …` | 改寫成針對原始欄位的範圍條件，或建立 expression index |
| 型別或 collation 不符 | `text` 欄位拿去和數字比較；非 C collation 下的 `LIKE 'abc%'` | 讓型別一致；使用 `text_pattern_ops` 索引 |
| 條件不在複合索引的第一個欄位 | 索引是 `(user_id, order_date)`，只過濾 `order_date` | 建一個以被過濾欄位開頭的索引 |
| 開頭是萬用字元 | `WHERE email LIKE '%@gmail.com'` | trigram（`pg_trgm`）索引 |

面試官期待你講的是「對欄位套函式」這一種。下面的條件把 `order_ts` 藏在函式裡，所以
`order_ts` 上的一般索引無法使用：

<!--CODE-->

同樣的過濾改寫成針對原始欄位的半開區間，就能使用索引：

<!--CODE-->

想測試索引*是否可能*有幫助，可以在自己的 session 執行 `SET enable_seqscan = off;`，再比較
`EXPLAIN ANALYZE` 的輸出 —— 這只是診斷手段，絕不是正式環境的設定。

---

<!-- fb8fc1a34750 -->
## 4) 估計列數 vs 實際列數 ⭐⭐⭐⭐⭐

<!-- 80e8f694a4dc -->
### 4-1) 為什麼要先檢查估計

**Optimizer 依估計列數選擇 join 順序與 join 演算法。當估計差了好幾個數量級，這個節點以上的
每一個決定都是為錯誤的資料做的。**

<!--CODE-->

Planner 預期 10,000 列，結果得到 2,000,000 列 —— 多了 200 倍。常見的後果：

- 它以為外層很小而選了 **Nested Loop**，結果內層跑了兩百萬次；
- 它用 10,000 列的規模來配置 hash table，結果 spill 到磁碟；
- 它把「小」的輸入放到 join 錯誤的那一側。

快速排序節點的方法是看**誤差倍數**：估計列數與實際列數中較大者除以較小者（0 視為 1）。
大約 10 倍以內算正常；在上層節點成本很高的地方出現 100 倍以上，就值得調查。有三件事讓比較不失準：

- **以每次 loop 比較。** 估計值與實際值**都是以每次 loop 計**，所以一個估計 `rows=1`、實際顯示
  `rows=1 loops=1000` 的內層節點，估計其實是對的。實際列數 × `loops` 是節點的總**輸出**列數，
  不是它的工作量 —— 一個 scan 可能什麼都沒輸出，卻讀完了整張表；工作量要看它的時間與 buffer 計數。
- **顯示的 0 可能是四捨五入來的。** PostgreSQL 17 及更早版本會把每次 loop 的實際列數四捨五入為
  整數，所以一百萬次 loop 共 10,000 列會印成 `rows=0`，精確的倍數也就無從得知。PostgreSQL 18
  會印到小數點後兩位。
- **被提早停下的節點會偏低。** `Limit` 的子節點，或 Merge Join 在另一側輸入讀完後就不再讀的那一側，
  即使估計正確，實際列數也可能少於估計。這裡的偏低可能是提早結束，而不是統計資訊不準 ——
  兩者都先確認再下結論。

接著看有大落差的**最低**那個節點 —— 它上面的誤差通常是從它繼承來的。

<!-- 405c252940f9 -->
### 4-2) 原因與解法

| 原因 | 發生了什麼 | 解法 |
|---|---|---|
| **統計資訊過舊** | 上次 `ANALYZE` 之後有大量匯入或刪除 | `ANALYZE orders;` —— 並確認 autovacuum 有跟上 |
| **抽樣漏掉了偏斜的值** | 某個值占了 30% 的列 | 提高該欄位的抽樣量：`ALTER TABLE orders ALTER COLUMN status SET STATISTICS 1000;` 再 `ANALYZE` |
| **欄位彼此相關** | `country = 'TW' AND city = 'Taipei'`：planner 把兩個選擇率當成獨立的相乘，因而低估 | extended statistics（見下） |
| **planner 無法估計的運算式** | `WHERE lower(email) = …`、回傳集合的函式 | expression index（它會有自己的統計資訊） |
| **planner 看不到的參數** | prepared statement 重用了不含實際值的 generic plan | 用字面值比較看看；檢查 `plan_cache_mode` |

Extended statistics 讓 planner 知道兩個欄位會一起變動：

<!--CODE-->

---

<!-- 86fd7531ebed -->
## 5) 計畫中的 Join 演算法 ⭐⭐⭐⭐

<!-- a39cedbb668b -->
### 5-1) Nested Loop

**外層（第一個）子節點的每一列，都執行一次內層（第二個）子節點。外層小、內層是索引查找時
很快；外層大時就是災難。**

<!--CODE-->

- 內層的 Index Scan 執行了 1,000 次（`loops=1000`）。外層 1,000 列時很便宜；外層一千萬列時
  就是一千萬次索引查找。
- 它是唯一能處理任何 join 條件的演算法，包括不等式與 `LIKE`，所以 non-equi join 只能用它。
- 估計很小的外層底下，內層的 `loops` 卻很大 —— 這是估計錯誤的典型症狀（§4）。

<!-- 534b545226a7 -->
### 5-2) Hash Join

**把一個輸入（`Hash` 子節點）建成 hash table，再讓另一個輸入流過去查找匹配。這是大型
等值 join 的預設選擇。**

<!--CODE-->

- 需要等值條件。對兩個輸入都是線性時間。
- Planner 會把它估計比較小的那一側放在 `Hash` 底下。如果那一側比預期大，hash table 就會
  超出記憶體，拆成多個寫到磁碟的 **batch**（`Batches` > 1，§6）。
- Hash table 的記憶體上限是 `work_mem × hash_mem_multiplier`（自 PostgreSQL 15 起預設為 2.0）。

<!-- 12169b0ee3e3 -->
### 5-3) Merge Join

**把兩個輸入依 join key 排序，再像 merge sort 的合併步驟一樣一起往前走。輸入本來就有序時很便宜；
兩個大輸入都得先排序時就很貴。**

<!--CODE-->

- Join key 上的索引（如這裡的 `users`）或先前的排序，可以省掉那一側的 `Sort`。
- 它不需要 hash table 的記憶體，所以在兩側都放不進記憶體時可能勝出 —— 但底下的 `Sort` 節點
  反而可能 spill。

<!-- 36c4ebb9694a -->
### 5-4) 如何在三者之間選擇

| Join | 運作方式 | 適合 | 要注意 |
|---|---|---|---|
| **Nested Loop** | 外層每一列 → 查內層 | 外層小 + 內層 key 有索引；任何 join 條件 | 內層 `loops` 遠高於估計 |
| **Hash Join** | 把一側建成 hash，用另一側查 | 大型等值 join，沒有可利用的順序 | `Hash` 節點的 `Batches` > 1 |
| **Merge Join** | 合併兩個已排序的輸入 | 輸入已有序（索引、先前的排序）；非常大的輸入 | 昂貴的 `Sort` 子節點、`external merge` |

沒有哪個演算法永遠最好。Hash Join 並不「優於」Nested Loop：50 筆訂單加上有索引的 `users`，
Nested Loop 做 50 次查找，Hash Join 卻要把 `users` 整張讀完。要看實際列數，而不是節點名稱。

---

<!-- 05afa5202732 -->
## 6) Spill：Sort 與 Hash 記憶體不足 ⭐⭐⭐⭐

當 sort 或 hash 需要的記憶體超過上限，PostgreSQL 會把溢出的部分寫到暫存檔 —— 這就是
**spill to disk**。上限是每個 sort 或 hash 操作的 `work_mem`（預設 4 MB），hash 還要再乘上
`hash_mem_multiplier`。

<!-- 8b4b879b2eac -->
### 6-1) Sort spill

<!--CODE-->

<!--CODE-->

- `external merge  Disk: 204800kB` —— 排序放不進記憶體，於是把約 200 MB 已排序的 run 寫到磁碟，
  再合併起來。
- `quicksort  Memory: …` 代表放得下。`top-N heapsort` 代表 `LIMIT` 讓它只需保留前幾名 ——
  真正的解法常常是補上呼叫端本來就想要的 `LIMIT`。

<!-- 1d5d64b1d1ee -->
### 6-2) Hash spill

<!--CODE-->

<!--CODE-->

Hash Join 的 build side 也會出現同樣的訊號：

<!--CODE-->

`Batches: 1` 代表全部放得進記憶體。`Batches` > 1 代表輸入被拆開，部分經過暫存檔。
這是線索，不是定論 —— 要搭配節點的實際時間以及 `temp read` / `temp written` 的數量一起判斷。

<!-- 8a4d37427df7 -->
### 6-3) 處理 spill

| 解法 | 為什麼有效 |
|---|---|
| 更早過濾、選更少欄位 | 到達 sort 或 hash 的資料更少，較窄的列讓每 MB 放得下更多 |
| 拿掉沒人需要的 `ORDER BY`；補上真正需要的 `LIMIT` | 省掉排序，或把它變成 top-N heapsort |
| 修正估計（§4） | planner 可能改選別的 join，或正確配置 hash 大小 |
| 為查詢單獨開一個 transaction，在其中 `SET LOCAL work_mem = '256MB';` | 在該 transaction 結束前都有更多記憶體；commit 或 rollback 後自動還原 |
| 預先彙總，或分批處理 | 每一步處理的資料更少 |

`work_mem` 要**按 transaction** 調高，不要全域調。單純的 `SET` 會一直持續到 session 結束 ——
在連線池上，它還會漏給下一個借用那條連線的人 —— 所以要用 `SET LOCAL`（它會持續到 transaction 結束，所以讓查詢自己用一個 transaction），
或事後 `RESET work_mem`。
另外，這個上限套用在**每個**查詢的**每個** sort 與 hash
節點上，而且每個 parallel worker 各有一份。一個有五個 hash 節點、四個 worker 的查詢，
可能用掉設定值的二十倍。而如果真正的原因是讀了十億列你根本不需要的資料，加記憶體只會讓
錯誤的計畫稍微快一點跑完。

---

<!-- 9aa68d17c298 -->
## 7) `BUFFERS` 與 I/O ⭐⭐⭐

<!-- 64f17658a467 -->
### 7-1) 計數器

<!--CODE-->

<!--CODE-->

每個數字的單位都是 page，通常每個 8 KB，而且父節點的數字包含子節點的。

| 計數器 | 意義 |
|---|---|
| `shared hit` | page 已經在 PostgreSQL 的 shared buffer cache 裡 |
| `shared read` | page 必須讀進來 —— 來自 OS page cache 或磁碟 |
| `shared dirtied` | 這個節點修改過的 page |
| `shared written` | 這個節點為了騰出 buffer 空間而必須寫出的 page |
| `temp read` / `temp written` | 暫存檔的 page —— sort 與 hash 的 spill |
| `local …` | 同上，但針對 temporary table |

這裡 scan 碰了 92,000 個 page（約 720 MB）；12,000 個已在快取中，80,000 個是讀進來的。

<!-- 4d438dfcc3c4 -->
### 7-2) 如何解讀，以及它們的限制

- **`shared read` 很高** —— 查詢拉進了大量資料。問問看它是否掃描了超過需要的範圍：缺索引、
  沒有 partition pruning、過濾套用得太晚。
- **`shared hit` 很高** —— 資料已在快取中。現在很快，但冷快取時（重啟後、在 replica 上）
  就會表現得像 `read` 的情況。
- **`temp read` / `temp written` 很高** —— 有 sort 或 hash spill 了（§6）。
- **碰到的 page 數比時間更穩定**：它不受快取冷熱影響，所以是修正前後比較時更好的數字。

**`shared read` 不是磁碟讀取次數**：不在 shared buffers 裡的 page 仍可能在作業系統的
page cache 裡。想看真正花在等待 I/O 的時間，要開啟 `track_io_timing`（superuser 設定，或寫在
`postgresql.conf`）；之後 `EXPLAIN (ANALYZE, BUFFERS)` 會在每個節點印出一行 `I/O Timings:`：

<!--CODE-->

---

<!-- 0fc34cad8648 -->
## 8) 完整範例：一個很慢的營收查詢 ⭐⭐⭐⭐⭐

`orders` 有一億列（20 GB），`users` 有一千萬列（2 GB）。這份報表要跑 120 秒：

<!--CODE-->

<!-- 73e6282b74ff -->
### 8-1) 執行計畫

`EXPLAIN (ANALYZE, BUFFERS)` 的結果簡化如下（實際的計畫還會以 parallel 方式執行 scan）：

<!--CODE-->

<!-- 9f8673cc31ac -->
### 8-2) 找出瓶頸

由下往上讀：

1. **`Seq Scan on orders`** —— 一億列全部讀過，八千萬列被過濾掉。大部分 I/O 都花在查詢
   不要的列上。
2. **users 上的 `Hash`** —— 一千萬列、`Batches: 32`：build side 遠大於記憶體上限，所以大部分
   都經過了暫存檔。
3. **`Hash Join`** —— 輸出兩千萬列，從第 30 秒（建 hash 的時間）開始，一直跑到第 110 秒。
4. **`HashAggregate`** —— 輸入兩千萬列、輸出 20 列，花 8 秒。不是問題所在。

時間花在 join 以及它底下的 scan。

<!-- 2c2569526e4e -->
### 8-3) 候選的解法

**A —— 少讀一點 `orders`。** 如果依日期範圍查詢很常見，就把 `orders` 依 `order_date` 分區，
讓 planner 剪掉舊的分區，或加一個索引。由於有 20% 的列符合，`order_date` 上的索引很可能輸給
Seq Scan —— 要實測。Covering index `(order_date) INCLUDE (user_id, amount)` 可以讓
Index Only Scan 成為可能。

**B —— 縮小 hash 的 build side。** Planner 對 `users` 建 hash，是因為它是較小的輸入；它只需要
`user_id` 與 `country`，而針對這個 transaction（`SET LOCAL`）調高 `work_mem` 可能讓 `Batches` 降到 1。

**C —— 先彙總再 join。** 先把兩千萬筆訂單收斂成每位使用者一列，讓 join 最多只處理每位
活躍使用者一列：

<!--CODE-->

當使用者平均下很多筆訂單時這會有幫助；若不是，就只是多了一次彙總。由計畫決定，而不是直覺。

<!-- d4cf55f6cea3 -->
### 8-4) 證明解法有效

一次只改一件事，重跑 `EXPLAIN (ANALYZE, BUFFERS)`，然後比較兩份計畫：

| 指標 | 修改前 | 目標 |
|---|---|---|
| Execution Time | 120 秒 | 降低 |
| 從 `orders` 讀取的列數 | 一億 | 降低 |
| `Hash` batches | 32 | 1，或更少 |
| `temp written` | 高 | 接近零 |
| 結果 | 20 列 | **完全相同**，另外驗證 |

結果必須完全一致，而 `EXPLAIN ANALYZE` 無法證明這一點：它會丟棄查詢的輸出，所以它的列數只能
顯示兩個版本都回傳 20 列，不能證明這 20 個營收數字相同。要讓兩個查詢跑在同一份資料快照上
（同一個 `REPEATABLE READ` transaction，或一份凍結的副本），再比對結果列 —— 兩個方向的
`EXCEPT` 都應該回傳空集合。更快但答案不同的查詢是 bug。

---

<!-- 67f3cba3f39d -->
## 9) Spark SQL 與資料倉儲的計畫 ⭐⭐⭐⭐

<!-- caf7449cc564 -->
### 9-1) Spark 的 `EXPLAIN FORMATTED`

**讀法一樣 —— 由下往上，看 scan 與 join —— 但分散式計畫中昂貴的是 `Exchange`：資料在網路上的
shuffle。**

<!--CODE-->

<!--CODE-->

| Operator | 意義 |
|---|---|
| `Scan`（`FileScan parquet …`） | 讀取檔案；檢查 `PartitionFilters`（pruning）與 `PushedFilters` |
| `Filter` / `Project` | 過濾列 / 選擇欄位 |
| `Exchange` | **一次 shuffle** —— 把資料列重新分配到各 executor |
| `BroadcastExchange` | 把小表送到每個 executor |
| `HashAggregate` / `SortAggregate` | 彙總；通常 `Exchange` 之前做一次 partial、之後做一次 final |
| `BroadcastHashJoin` | 對小表的 broadcast 副本做 join，大表不需 shuffle |
| `SortMergeJoin` | 兩側都依 key shuffle 並排序，再合併 |
| `AdaptiveSparkPlan` | 開啟 Adaptive Query Execution（AQE）時的根節點 |

**開啟 AQE 時，`explain` 顯示的是初始計畫**（`AdaptiveSparkPlan isFinalPlan=false`）。AQE 可能在
執行期把 join 改成 broadcast、合併分區或切分 skewed 分區，所以實際跑的計畫要到 Spark UI 的
SQL 分頁看。

<!-- d55c21d3d579 -->
### 9-2) SortMergeJoin vs BroadcastHashJoin

<!--CODE-->

兩側都先 scan，再依 `customer_id` **shuffle** 到 200 個分區（`spark.sql.shuffle.partitions`），
排序後合併。兩個 `Exchange` 節點代表兩張表都要跨網路傳輸。

<!--CODE-->

`customers` 被複製到每個 executor，`orders` 則在原地做 join —— fact table 完全不用 shuffle。
小於 `spark.sql.autoBroadcastJoinThreshold`（預設 10 MB）時 Spark 會自動 broadcast，而 broadcast
副本會占用 driver 與 executor 的記憶體，所以一張「小」表其實不小時，就會造成記憶體不足的錯誤。

<!-- 04feb874cf98 -->
### 9-3) PostgreSQL vs Spark

| | PostgreSQL | Spark |
|---|---|---|
| 執行環境 | 單一伺服器（可用 parallel worker） | 由 executor 組成的叢集 |
| Scan | Seq / Index / Bitmap | 帶 partition pruning 與 pushed filter 的檔案 scan |
| Join | Nested Loop / Hash / Merge | Broadcast Hash / Sort-Merge / Shuffle Hash |
| 昂貴的資料搬移 | 磁碟 I/O、暫存檔 | 網路上的 **shuffle** |
| 記憶體壓力 | `work_mem`、spill | executor 記憶體、spill |
| 實際數字 | `EXPLAIN ANALYZE` | Spark UI：SQL metrics、stage、task |

在 Spark 裡，計畫說明會發生什麼；Spark UI 說明實際怎麼跑的。要在那裡檢查 shuffle read 與
write 的大小、spill、**task 執行時間分布**（某個 task 遠慢於中位數代表 skew），以及 broadcast 的大小。

<!-- 8a4f778c9aed -->
### 9-4) 其他引擎

| 引擎 | 計畫 | 實際執行 |
|---|---|---|
| MySQL | `EXPLAIN`（`type` 欄位：`ALL` 代表全表掃描） | `EXPLAIN ANALYZE`（8.0.18+） |
| BigQuery | — | job 的 *Execution details*：stage、bytes、slot time |
| Snowflake | `EXPLAIN` | *Query Profile*：operator、掃描的 bytes、被剪掉的分區、spill |
| Trino | `EXPLAIN` | `EXPLAIN ANALYZE` |

在資料倉儲中，要看掃描的分區占總數多少（pruning）、spill 的 bytes，以及 shuffle 資料最多的 stage。

---

<!-- 7fba03630e7a -->
## 10) 面試工具箱 ⭐⭐⭐⭐⭐

<!-- 6bf9c80ec299 -->
### 10-1) 除錯順序

1. **先確認查詢是正確的。** Join key、資料粒度（grain）、NULL、重複資料。絕不拿正確性換速度。
2. **取得真實的計畫。** PostgreSQL：`EXPLAIN (ANALYZE, BUFFERS)`。Spark：`EXPLAIN FORMATTED`
   加上 Spark UI。
3. **Scan。** 讀的是否遠多於留下的？Pruning 或索引有沒有發揮作用？
4. **估計列數 vs 實際列數。** 找出落差大的最低節點；檢查統計資訊、skew、欄位相關性。
5. **Join 與彙總。** 內層 `loops` 爆量的 Nested Loop？Batch 很多的 Hash？Spark 中有沒有
   不必要的 shuffle？
6. **記憶體與 I/O。** `Sort Method`、`Batches`、temp page、buffers；Spark 則看 spill 與 shuffle 大小。
7. **一次只改一件事，然後重新量測** —— 時間、讀取的 page 數，以及結果沒有改變。

<!-- 65e65cc9b8ec -->
### 10-2) 關鍵字速查表

| 你看到 | 代表 | 接著看 |
|---|---|---|
| `Seq Scan` + 很大的 `Rows Removed by Filter` | 讀很多、留很少 | 索引、條件能否用索引、分區 |
| 回傳很多列的 `Index Scan` | 大量隨機讀取 | Seq 或 Bitmap scan 是否更便宜 |
| Index Only Scan 的 `Heap Fetches` 很高 | visibility map 過舊 | `VACUUM` |
| 估計列數 ≪ 實際列數 | cardinality 估計錯誤 | `ANALYZE`、statistics target、extended statistics |
| `Nested Loop`，內層 `loops` 達百萬 | 對這個規模選錯 join | 外層的估計 |
| `Hash` 的 `Batches` > 1 | hash build spill 了 | build side 大小、`work_mem` |
| `Sort Method: external merge` | 排序 spill 了 | 排序輸入大小、`work_mem`、缺少的 `LIMIT` |
| `temp written` 很高 | spill 到暫存檔 | 上方的 sort 或 hash |
| `shared read` 很高 | 拉進大量資料 | scan 的資料量 |
| `Exchange`（Spark） | shuffle | join 策略、分區、skew |
| `SortMergeJoin`（Spark） | 兩側都 shuffle | 能否 broadcast 其中一側？ |
| `isFinalPlan=false`（Spark） | AQE 調整前的計畫 | Spark UI 中的最終計畫 |

<!-- c708750cea6f -->
### 10-3) 「你的查詢要跑 10 分鐘，你會怎麼做？」

一個範例回答，依照面試官想聽的順序（面試時通常以英文回答，這裡附上中文版）：

> 「我會從執行計畫著手，而不是先去改設定。
>
> 首先，我執行 `EXPLAIN ANALYZE`，比較每個節點的估計列數與實際列數，因為大的落差通常就能
> 解釋為什麼選錯 join 或發生 spill。
>
> 第二，看 scan：查詢讀的資料是否遠多於回傳的？索引或 partition pruning 有沒有用上？過濾條件
> 有沒有下推？
>
> 第三，檢查 join 策略 —— 內層跑了數百萬次的 nested loop、build side spill 的 hash join，
> 或是餵給 merge join 的昂貴排序。
>
> 我也會檢查記憶體與 I/O：buffer 讀取、暫存檔用量、sort 與 hash 的 spill。
>
> 在 Spark 上，我還會看 shuffle read 與 write 的大小、用來判斷 skew 的 task 執行時間分布，
> 以及 Spark UI 中 adaptive 執行後的最終計畫。
>
> 接著一次只改一件事，重新量測，並確認結果完全相同。」

**一句話版本：** `EXPLAIN` 給你一個「時間花在哪裡」的假設；`EXPLAIN ANALYZE` 與執行期的指標
負責證實或推翻它。目標是在答案不變的前提下降低端到端的時間與成本 —— 而不是一份只是看起來
比較漂亮的計畫。
