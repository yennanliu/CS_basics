<!-- c4244549ab07 -->
# 陣列 — 題目實作

> **範圍** — [array.md](./array.md) 背後的題解倉庫：十三題真正在考「原地改寫陣列」或「拿索引當儲存空間」的題目，依各自用到的技巧分組。
> **另見**：[array.md](./array.md) — 母表：基本操作、特殊演算法，以及告訴你一題到底該歸哪張表的選擇表；[2_pointers.md](./2_pointers.md)、[sliding_window.md](./sliding_window.md)、[prefix_sum.md](./prefix_sum.md)、[difference_array.md](./difference_array.md) — 吃掉大部分 array 標籤題目的四個模式家族；[sort.md](./sort.md)、[matrix.md](./matrix.md)、[kadane_algorithm.md](./kadane_algorithm.md)、[stock_trading.md](./stock_trading.md) — 選擇表會把題目導向的其他主表。

<!-- 252a821e92ee -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)

<!-- ca795b8651a5 -->
## 總覽

這裡是 [array.md](./array.md) 的長尾。母表放的是操作、特殊演算法和選擇表；這個檔案放的是*應用*它們的題目。

<!-- a8ca5e1b5ecf -->
### 關鍵性質
- **複雜度**：逐題標註；原地那一組是 O(n) 時間、O(1) 額外空間，而這通常就是題目的重點
- **核心想法**：陣列不只是輸入，它同時也是草稿紙 — 用正負號、用位置，或用一個由後往前寫的指標
- **什麼時候用**：當[選擇表](./array.md#2-pattern-selection)已經判定這題其實不是視窗、指標或前綴和的問題之後

<!-- 3b5601eec3eb -->
### 關於重複收錄

其中五題也出現在擁有該*技巧*的表裡 — LC 121 在
[stock_trading.md](./stock_trading.md) 和 [kadane_algorithm.md](./kadane_algorithm.md)、
LC 1109 在 [difference_array.md](./difference_array.md)、LC 1567 在
[kadane_algorithm.md](./kadane_algorithm.md)、LC 251 在 [design.md](./design.md)、LC 406 在
[sort.md](./sort.md)。現階段這些重複是刻意留著的：把它們合併是跨檔整併那一輪的工作，不是單張表自己該處理的事。

<!-- d9f7568c72e5 -->
## 原地改寫與索引技巧

<!-- 47105027a3a6 -->
### 1) First Missing Positive — LC 41 ⭐⭐⭐⭐⭐

> 兩種**把陣列本身當雜湊表**的做法，都是 O(n) 時間、O(1) 額外空間。
> 之所以成對保留，是因為技巧不同：第一種靠翻正負號來*標記*一個位置，
> 第二種則是把每個值*搬*到它該待的位置。

**做法 A — 用正負號標記**（先把超出範圍的值夾掉，再把 `nums[v-1]` 變號）：

<!--CODE-->

<!--CODE-->


**做法 B — 循環排序**（把每個值送回家，再掃出第一個缺口）：

<!--CODE-->

**變形 — LC 287 Find the Duplicate Number（用 SIGN 標記）：** 和 LC 41 一樣是「索引當雜湊 key」的想法，只是這次不*交換*值，而是把 `nums[v]` **變號**來記錄「值 `v` 出現過」。第一次踩到已經是負數的位置，那個索引就是重複的數字。值落在 `1..n`、陣列長度是 `n+1`，所以 `abs(v)` 永遠是合法索引。

<!--CODE-->

<!--CODE-->

> **注意：** LC 287 嚴格版的 follow-up 禁止修改陣列 — 那個版本要用 **Floyd 環偵測**來解（把 `i -> nums[i]` 看成一條鏈結串列），見 [2_pointers.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md)。上面這個變號版是「允許改動陣列」時該拿出來用的。
>
> **變號標記檢查清單：** ① 值必須能對應到合法索引，② 取值時一律用 `abs(...)`，③ 如果陣列還要再用，記得把正負號還原（LC 442／448 用的是一模一樣的技巧）。

<!-- 3304547287a8 -->
### 4) Maximum Swap — LC 670

<!--CODE-->

> **對照組** — O(n²) 的暴力解。值得看一次，因為這裡正好是 `A[:]` 這個淺拷貝
> 慣用法發揮作用的地方：`A` 每一輪都會被改動再還原，所以目前最佳解必須存成一份
> *拷貝*，不能是參考。

<!--CODE-->

<!-- 0a4a154402dd -->
## 掃描與滾動狀態

<!-- f9680ce5031a -->
### 5) Best Time to Buy and Sell Stock — LC 121 ⭐⭐⭐⭐

<!--CODE-->

**變形 — LC 122 Best Time to Buy and Sell Stock II（交易次數不限）：** 轉折在於買賣次數不限之後，你根本不用再追蹤 `minPrice` — 只要**把每天之間的正價差全部加起來**（每一段上漲都能獨立賺到）。

<!--CODE-->

<!--CODE-->

> **貪婪為什麼是對的：** 任何有利可圖的區間 `[i, j]` 都能拆成每日價差的總和（`p[j] - p[i] = Σ (p[k+1] - p[k])`），而把負的價差丟掉只會讓總和變大。所以「正價差總和」既是上界，也真的做得到。
>
> **對照：** LC 121 = **1** 次交易 → 追蹤滾動最小值。LC 122 = **∞** 次交易 → 加總正價差。

<!-- 1ec71b3a8c85 -->
## 計數、訂位與模擬
