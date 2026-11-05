<!-- c73384744915 -->
# 陣列

> **範圍** — 陣列的基本功——原地改寫、旋轉、分割，以及「用索引當雜湊」的招式。它擁有這些操作本身；至於各個模式家族（視窗、指標、前綴和）各有自己的檔案。
> **另見**：[array_examples.md](./array_examples.md) — 撐起這些操作的題目詳解；[python_trick.md](./python_trick.md) 與 [java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — 排序鍵與比較器，多鍵排序規則歸它們管；[2_pointers.md](./2_pointers.md)、[sliding_window.md](./sliding_window.md)、[prefix_sum.md](./prefix_sum.md) 和 [difference_array.md](./difference_array.md) — 四大陣列模式家族；[matrix.md](./matrix.md) — 二維；[sort.md](./sort.md) — 排序。

> 最基本的線性資料結構

<!-- 252a821e92ee -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)

<!-- 447dc0da7a94 -->
## 時間複雜度

| 資料結構 | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| 陣列          | O(n)     | O(n)     | O(n)     | O(n)     |

> 這裡指未排序的動態陣列。用索引存取是 **O(1)**；append 是攤還 **O(1)**；任意位置的 Insert/Delete 是 **O(n)**（要搬移元素）。陣列若已排序，Search 可以降到 **O(log n)**（二分搜尋）。

<!-- 0135d91451cd -->
## 0) 概念

- [Java Array](https://cloud.tencent.com/developer/article/1672332)
    - 底層：記憶體中一塊連續的空間

<!-- 90fdb0038636 -->
### 0-1) 分類

- 相關主題
    - [greedy.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/greedy.md)
    - [matrix.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/matrix.md)

- 演算法
    - 索引操作
    - 陣列操作
    - 排序
    - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
    - [2 pointers](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md)
        - 快慢指標
        - 左右指標
    - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)
    - [prefix sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
    - [difference array](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/difference_array.md)
    - [Kadane algo](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/kadane_algo.md)

- 資料結構
    - dict
    - set
    - array

<!-- 2e4a12810aa0 -->
### 0-2) 辨識

多數「陣列」題其實會被導到某個模式文件（見 [2) 模式選擇](#2-pattern-selection)）。
下面這些訊號才是留在這份文件的。限制條件的提示依照
[complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的尺規。

| 題目敘述裡的訊號 | 限制條件提示 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「把 `nums2` **原地**合併進 `nums1`」、「最後 n 格是 0，應該忽略」 | 追問 O(1) 額外空間 | [Template 6](#template-6-backward-write-pointer--lc-88) 反向寫入指標 | 空位在前面（移除重複）→ 正向快慢指標，[2_pointers.md](./2_pointers.md) |
| 「`insert` / `remove` / `getRandom` 都要**平均 O(1)**」 | 約 10⁵ 次呼叫 | [Template 7](#template-7-swap-with-last-delete--lc-380) 和最後一個交換再刪除 | 允許重複（LC 381）→ 值對應到一組索引的集合 |
| 「出現**超過 ⌊n/2⌋** 次」、「O(1) 額外空間」 | n ≤ 5·10⁴，一次掃描 | [Template 2](#template-2-majority-vote-one-candidate--lc-169) 多數投票 | 「最常出現」／「前 k 個」→ 需要計數，[hash_map.md](./hash_map.md) |
| 「所有出現**超過 ⌊n/3⌋** 次的元素」 | O(1) 空間 | [Template 3](#template-3-majority-vote-k--1-candidates--lc-229) k − 1 個候選 | |
| 「`1..n` 的排列」、「對每個前綴，有幾個 …」 | 值 ≤ n → 陣列本身就是雜湊表 | [Template 4](#template-4-frequency-array--running-count--lc-2657) 頻率陣列＋即時累計 | 值可到 10⁹ → 用雜湊表，做同樣的「跨過門檻」判斷 |
| 「**所有子字串／子陣列**上 X 的總和」 | n ≤ 10⁵ → 無法列出 O(n²) 個子陣列 | [Template 5](#template-5-index-contribution-counting--lc-2063) `(i + 1) * (n - i)` | 元素只有在是最小／最大值時才算（LC 907）→ [monotonic_stack.md](./monotonic_stack.md) |
| 「讓一整列的元素都相等，求最少操作數，否則 −1」 | 每個元素只有約 2 種選擇 | [Template 8](#template-8-bounded-candidate-enumeration--lc-1007) 從索引 0 取候選 | 目標可以是任何值 → 排序／中位數，[sort.md](./sort.md) |
| 在二維陣列上「設計一個迭代器」，`next()` / `hasNext()` | 每次呼叫攤銷 O(1) | [Template 1](#template-1-lazy-two-cursor-iterator--lc-251-) 兩個游標 | 巢狀深度不固定（LC 341）→ 用迭代器的堆疊，[iterator.md](./iterator.md) |

<!-- 0de0d3d03b4b -->
## 1) 一般形式

<!-- f7f9ea61f2fe -->
### 1-1) 基本操作

<!-- aa2a59071fbd -->
#### 1-1-0) 切割陣列
<!--CODE-->

<!-- 0d0ea2366463 -->
#### 1-1-1) 插入元素
<!--CODE-->

<!-- df82c13fc1ea -->
#### 1-1-2) 刪除元素
<!--CODE-->

<!-- 00a9653d9fdc -->
#### 1-1-3) 檢查元素是否在陣列中
<!--CODE-->

<!-- e5bc949c47ee -->
#### 1-1-4) 加到陣列（頭、尾）
<!--CODE-->

<!-- ff30db13d82d -->
#### 1-1-5) 排序陣列*****

**`list.sort()`（V1）是原地排序。`sorted()`（V2）回傳一個新的 list。**

| | `_array.sort(...)` (V1) | `sorted(_array, ...)` (V2) |
|---|---|---|
| **原地？** | ✅ 是 — `_array` 會被改動 | ❌ 否 — 原本的不動 |
| **回傳值** | `None` | 新的已排序 `list` |
| **可用對象** | 只有 `list` | **任何 iterable**（str、tuple、set、dict、generator） |
| **空間** | 額外 O(1)* | O(n) |
| **穩定？** | ✅ 是（Timsort） | ✅ 是（Timsort） |

> \* CPython 的 Timsort 最壞情況可能需要一塊 O(n) 的暫存區，但**不會配置新的 list 物件**。

<!--CODE-->

**🚫 常見錯誤：**

<!--CODE-->

**💡 什麼時候用哪一個：**

- **`.sort()`** → list 是你的、而且想省空間到 O(1)（例如 LC 406、LC 56 Merge Intervals、LC 253）
- **`sorted()`** → 輸入是字串／dict／set／tuple，或原本的順序必須保留

**Java 的對應寫法：**

<!--CODE-->

<!-- 9e574ddf6b9d -->
#### 1-1-6) 攤平陣列
<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- da9e565bc886 -->
##### Template 1: Lazy Two-Cursor Iterator — LC 251 ⭐⭐⭐⭐

**辨識** — 在二維或巢狀陣列上「設計一個迭代器」／「實作 `next()` 和 `hasNext()`」，常會追問
「輸入非常大」或「只用 O(1) 額外空間」。呼叫次數 ≤ 10⁵ → 每次呼叫必須攤銷 O(1)。

**關鍵點** — `(row, col)` 永遠指向**原本的**陣列，而且 `hasNext()` 回傳之後它一定停在一個真正的
元素上；只有 `next()` 會越過元素。所以什麼都不用複製，而每一列在整趟走訪中只會被跳過一次。

| | 急切式（在建構子裡攤平） | **惰性（兩個游標）** |
|---|---|---|
| 建構子 | O(N)：複製每個元素 | O(1) |
| 額外空間 | O(N) | **O(1)** |
| `next()` / `hasNext()` | O(1) | 攤銷 O(1)：整趟走訪中每一列只被跳過一次，總共 O(R + N) |
| 來源很大或會變動時 | 不行：它是快照 | 可以：沒有預先讀取任何東西 |

`N` = 元素總數，`R` = 列數。急切式版本在
[array_examples.md §13](./array_examples.md#13-flatten-2d-vector--lc-251)。面試官說「設計一個
迭代器」指的是惰性版本，它也是 iterator.md 裡
[`hasNext()` / `next()` 約定](./iterator.md#1-2-the-hasnext--next-contract-) 的二維特例：
深度固定為二，所以兩個索引就能取代堆疊。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間：每次呼叫攤銷 O(1)**：`hasNext()` 裡的 `while` 可能一次跳過好幾列，但 `row` 只會往前，
  所以整趟走訪中它總共只前進 R 次：O(R + N)。
- **空間：O(1) 額外**：`self.vec = vec` 存的是參考而不是複本；狀態只有兩個整數。

**手動追蹤**

<!--CODE-->

**陷阱**
- **`next()` 盲信游標。** `self.col += 1; return vec[row][col - 1]` 只有在先呼叫過 `hasNext()`
  時才正確。在 `[[], [3]]` 上直接呼叫 `next()` 會讀到空的第 0 列而崩潰。在 `next()` 裡先正規化
  不花任何成本，就能消除這個依賴。
- **`hasNext()` 會消耗元素。** 它可以跳過什麼都沒有的列，但絕不能越過真正的元素；只有 `next()`
  可以。
- **該用 `while` 卻用了 `if`。** 連續兩個空列（`[[1], [], [], [2]]`）需要跳不只一次。
- **換列時忘了 `col = 0`**：在 `[[1, 2], [3]]` 上，第二列會從索引 2 開始讀而被整列跳過。

**不是這個，如果** — 巢狀深度不固定（LC 341 Flatten Nested List Iterator）：兩個游標追蹤不了
無上限的深度，改用迭代器的堆疊
（[iterator.md](./iterator.md#1-2-the-hasnext--next-contract-)）。

**練習** — LC 251 ⭐ · LC 284 *（peek：先快取下一個元素）* · LC 281 *（zigzag：游標的佇列）* ·
LC 341 *（深度不固定：游標的堆疊）*

<!-- feee22d705f3 -->
#### 1-1-7) 同時走訪兩個陣列（長度可能不同）
<!--CODE-->


---

<!-- 944969701590 -->
### 1-2) 特殊的陣列演算法 ⭐⭐⭐⭐

<!-- eb4d0f8b7d1a -->
#### 1-2-1) Boyer-Moore 多數投票演算法

把不同的元素兩兩配對抵銷，就能用 O(1) 額外空間找出出現超過 ⌊n/k⌋ 次的元素。
[streaming_algorithms.md](./streaming_algorithms.md) 說明了為什麼同樣的想法也適用於資料流。

<!-- 5be61b084b52 -->
##### Template 2: Majority Vote, One Candidate — LC 169

**辨識** — 「出現**超過 ⌊n/2⌋ 次**的元素」、「可以假設多數元素一定存在」，追問「O(1) 額外空間」。
n ≤ 5·10⁴ → 一次 O(n) 掃描。

**關鍵點** — 每一次減一，都是把一個 `candidate` 和一個*不同的*值互相抵銷。出現超過 n/2 次的值
不可能被完全抵銷，所以最後留下的候選就是它。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：唯一的 `for num in nums` 迴圈對每個元素做 O(1) 的事（若要驗證，再多一次 O(n)
  掃描）。
- **空間 O(1)**：全部的狀態就是 `candidate` 和 `count`。

**手動追蹤**

<!--CODE-->

**陷阱**
- **沒有保證多數存在時卻省略驗證。** `[1,2,3]` 最後的候選是 3，但它只出現一次。投票只告訴你：
  *如果*有多數元素，*就是*它。
- **候選不是最常出現的元素。** `[1,1,2,2,3]`（沒有多數）最後停在 3，不是 1 或 2。Boyer-Moore
  回答的是「> n/2」，從來不是「眾數」。

**不是這個，如果** — 門檻是「最常出現」或「前 k 個」，而不是「超過 n/k」：需要真正的計數，
改用雜湊表加桶或堆積（[hash_map.md](./hash_map.md)）。

**練習** — LC 169 ⭐ · LC 1150 *（已排序：改用二分搜尋）* · LC 229 *（> n/3：Template 3）* ·
LC 1157 *（區間內的多數：隨機抽樣或投票線段樹）*

<!-- a88bd9d8e987 -->
##### Template 3: Majority Vote, k − 1 Candidates — LC 229

**辨識** — 「所有出現**超過 ⌊n/3⌋ 次**（或 ⌊n/k⌋）的元素」，追問「O(1) 空間」。最多只有 k − 1
個值能超過 n/k 的門檻，所以答案是最多 k − 1 個元素的清單。

**關鍵點** — 和 k − 1 個候選都不同的值，會**各抵銷一份**，一次移除 k 個值。出現超過 n/k 次的值，
參與抵銷的次數不會超過它自己的個數，所以永遠不會被消滅；但留下來的只是*候選*，必須再掃一次計數。

<!--CODE-->

<!--CODE-->

同一個迴圈可以推廣到任意 k，用一個最多存 k − 1 個候選的 map：

<!--CODE-->

**複雜度**
- **時間 O(n)**：兩次線性掃描，投票的 `for` 和驗證的 `nums.count`（一般化版本是 O(n · k)，
  因為 map 滿了時要逐個鍵減一）。
- **空間 O(1)**：兩個候選和兩個計數器（一般情況 O(k)）。

**陷阱**
- **省略驗證。** `[1,2,3]` 留下 1 和 2 兩個候選，但都只出現一次。和 LC 169 不同，LC 229 什麼都
  不保證，所以第二次掃描是答案的一部分。
- **還沒檢查兩個候選是否相符就先收養。** 把 `count1 == 0` 放在 `num == cand2` 之前，
  `[1,2,3,2]` 會變成 `cand1 == cand2 == 2`：同一個值被追蹤兩次，回報成 `[2, 2]`。

**練習** — LC 229 ⭐ · LC 169 *（k = 2）* · LC 2404 *（最常出現的偶數：要計數，不是投票）*

<!-- afca09f2c1ff -->
#### 1-2-2) 頻率陣列＋即時累計

當值有界（`1..n` 的排列、小寫字母）時，以值為索引的陣列就是雜湊表，而在頻率跨過門檻的那一刻
更新的計數器，可以取代任何重新掃描。

<!-- 64f252a7eda8 -->
##### Template 4: Frequency Array + Running Count — LC 2657

**辨識** — 「`1..n` 的排列」、「值在 `[1, n]` 內」，再加上「對每個前綴／每個索引，有幾個 …」。
LC 2657 的 n ≤ 50，但這個形狀是給 n ≤ 10⁵ → O(n) 用的，所以要避免的是每個前綴都重算一次（O(n²)）。

**關鍵點** — `count[v]` 是目前為止看過 `v` 幾次，而 `common` 只在某個 `count[v]` **剛好達到**門檻
（這裡是 2：A 裡一次、B 裡一次）的那一步改變。每個值最多跨過門檻一次，所以 `common` 正好等於
已經跨過的值的個數。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一個 `for i in range(n)`，每步兩次 O(1) 的陣列更新，不重掃前綴。
- **空間 O(n)**：`count = [0] * (n + 1)`，大小由值域決定（一般是 O(最大值)）。

**手動追蹤**

<!--CODE-->

**陷阱**
- **值是 `1..n` 卻開 `[0] * n`。** `A = [1]` 會在長度 1 的 list 上存取 `count[1]`：IndexError
  （或 `ArrayIndexOutOfBoundsException`）。要開 `n + 1`。
- **先判斷再加一。** `if count[x] == 2: common += 1` 之後才 `count[x] += 1`，算到的是跨過門檻的
  *下一步*，所以 `A = [1], B = [1]` 會回傳 `[0]` 而不是 `[1]`。
- **陣列不是排列時用 `>= 2` 取代 `== 2`**：出現三次的值會被算兩次。要判斷的是「跨過」，不是「狀態」。

**不是這個，如果** — 值無界或很稀疏（`-10⁹ ≤ v ≤ 10⁹`）：開不了那麼大的陣列，改用雜湊表
（[hash_map.md](./hash_map.md)），做同樣的跨門檻判斷。

**練習** — LC 2657 ⭐ · LC 217 *（門檻 2，提早回傳）* · LC 448 *（計數 0：缺少的值）* ·
LC 645 *（一個值是 2、一個是 0）* · LC 442 *（同樣的事原地做，用正負號技巧）*

<!-- 640cd58733f6 -->
#### 1-2-3) 索引貢獻計數（統計每個元素在所有子陣列中出現幾次）

不要列舉每個子陣列，而是問「有幾個子陣列包含這個索引」，然後一次掃描把這些貢獻加總。

<!-- a33811356f47 -->
##### Template 5: Index Contribution Counting — LC 2063

**辨識** — 「**所有子字串／子陣列**上 X 的總和」，而且每個元素各自對 X 有貢獻（母音算 1、值就加它
自己）。n ≤ 10⁵ → 子陣列約有 5·10⁹ 個，無法列出；預期 O(n)。

**關鍵點** — 包含索引 `i` 的子陣列，就是在 `0..i` 選左端、在 `i..n-1` 選右端，所以 `i` 恰好落在
`(i + 1) * (n - i)` 個子陣列裡。當元素的貢獻和它所在的子陣列無關時，總和就是
`value(i) * (i + 1) * (n - i)` 的加總。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一個 `for i in range(n)`；乘積取代了 O(n²) 的 `(left, right)` 列舉。
- **空間 O(1)**：一個累計總和。

**手動追蹤**

<!--CODE-->

**陷阱**
- **邊界差一。** `i * (n - i)` 漏掉了從 `i` 開始的子陣列；對 `"a"` 會回傳 0 而不是 1。左端有
  `i + 1` 種選擇，右端有 `n - i` 種。
- **Java 的 `int` 溢位。** n = 10⁵ 時光是中間那個索引就貢獻約 2.5·10⁹，超過 `Integer.MAX_VALUE`。
  用 `long` 累加，並在相乘*之前*就轉型。
- **「包含 i」與「從 i 開始／在 i 結束」。** 從 `i` 開始的子陣列：`n - i`；在 `i` 結束：`i + 1`；
  包含 `i`：兩者相乘。把它們搞混是最常見的錯誤答案。

**不是這個，如果** — 貢獻取決於子陣列，例如元素只有在是最小值時才算（LC 907）：它的範圍被最近的
較小元素限制住，所以跨度要用 [monotonic stack](./monotonic_stack.md) 求，而不是 `i + 1` 和 `n - i`。

**練習** — LC 2063 ⭐ · LC 1588 *（奇數長度子陣列：數奇數長度的 (left, right) 配對）* ·
LC 828 *（唯一字元：到前一個與下一個相同字元的跨度）* · LC 907 *（最小值：單調堆疊跨度）* ·
LC 2104 *（最大值貢獻減最小值貢獻）*

<!-- e2ec52786af9 -->
#### 1-2-4) 反向寫入指標（原地合併／原地覆寫） ⭐⭐⭐⭐⭐

當輸出要寫進同一個還在讀取的陣列，而空位在尾端時，就從後面往前寫。

<!-- 245de492f6e2 -->
##### Template 6: Backward Write Pointer — LC 88

**辨識** — 「**原地**合併」、「`nums1` 長度是 `m + n`，最後 `n` 格是 0，應該忽略」、「不要回傳
任何東西」。LC 88 的 m + n ≤ 200，但追問要求 O(m + n) 時間和 O(1) 額外空間，這就排除了先複製 `nums1`。

**關鍵點** — `write - p1` 等於 `nums2` 裡還沒合併的個數，永遠不會是負的，所以 `write >= p1` 恆成立：
正要寫入的格子不是已經讀過，就是填充區。每一步寫入**較大**的尾端，就會從最大值往下把 `nums1` 填滿。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(m + n)**：`while p2 >= 0` 的每一輪都讓 `write` 往回一格，而 `write` 從 `m + n - 1` 開始。
- **空間 O(1)**：三個索引；合併直接在 `nums1` 裡進行。

**手動追蹤**

<!--CODE-->

**陷阱**
- **正向合併。** 從 `write = 0` 開始寫，會在讀到 `nums1[0]` 之前就把它蓋掉：`[4,5,6,0,0,0]` 和
  `[1,2,3]` 會弄丟 4 和 5。
- **沒有 `p1 >= 0` 的防護。** `nums1` 的真實值用完之後，`nums1[p1]` 就是 `nums1[-1]`：Java 會丟例外，
  Python 則把填充區當成資料讀。`nums1 = [0]`、`m = 0`、`nums2 = [-1]` 會把填充的 0 往前複製，接著
  丟出 IndexError。
- **也對 `p1` 迴圈，最後再複製 `nums1` 的尾巴。** 多做的工：`nums1` 剩下的部分已經在最終位置，
  所以只對 `p2` 迴圈。

**不是這個，如果** — 輸出永遠不比已讀的部分長，而且空位在前面（移除重複、移除元素）：那裡用**正向**
的慢／快指標就安全（[2_pointers.md](./2_pointers.md)）。

**練習** — LC 88 ⭐ · LC 977 *（平方：從後面填結果，最大的先放）* · LC 1089 *（複寫零：先計數，再從後往前寫）* ·
LC 443 *（字串壓縮：正向寫入，往前讀）*

<!-- 439e056ccfcc -->
#### 1-2-5) 陣列＋雜湊索引表（用「和最後一個交換」做 O(1) 刪除） ⭐⭐⭐⭐⭐

從陣列中間刪除之所以是 O(n)，只是因為要搬移。如果順序不重要，就用最後一個元素補洞，再彈出尾端。

<!-- 2fd1f44d1dc4 -->
##### Template 7: Swap-With-Last Delete — LC 380

**辨識** — 「實作 `insert`、`remove` 和 `getRandom`，每個都要**平均 O(1)** 時間」、「每個元素被回傳
的機率必須相同」。最多 2·10⁵ 次呼叫 → 每個操作 O(1)。

**關鍵點** — `vals` 永遠是緊密的（沒有洞），所以 `vals[隨機索引]` 是均勻抽樣，而 `idx[v]` 永遠是 `v`
在 `vals` 裡的位置。刪除時把最後一個值移進洞裡、更新它的索引、再彈出尾端，就能同時維持這兩件事；
這也是 list 唯一 O(1) 的刪除方式。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間：每次呼叫平均 O(1)**：每個方法只做常數次雜湊表操作，而且只碰 `vals` 的尾端（`append`、
  `pop`）；沒有任何搬移。
- **空間 O(n)**：`vals` 和 `idx` 對每個存放的值各存一筆。

**陷阱**
- **還沒重新指向就先刪掉 map 的項目。** 當要刪的就是最後一個元素（`insert(5); remove(5)`）時，先
  `del idx[val]` 再 `idx[last] = i` 會把 5 放回 map，之後的 `insert(5)` 就回傳 False。
- **從中間刪除。** `vals.remove(val)`／`vals.remove(i)` 會搬移後面的元素：每次刪除 O(n)，正是交換
  要避免的成本。
- **Java 的 `remove` 多載。** 在 `List<Integer>` 上，用 `int` 呼叫 `vals.remove(lastVal)` 是依**索引**
  刪除；要嘛刻意傳索引，要嘛用 `Integer.valueOf(v)` 依值刪除。
- **用墓碑標記取代交換。** 把格子標成已刪除會留下洞，`getRandom` 不是不均勻，就是得無上限地重試。

**不是這個，如果** — 允許重複（LC 381）：一個值有好幾個位置，所以 `idx` 要把值對應到索引的**集合**，
交換時只更新被移動那個值的集合中的一個成員。

**練習** — LC 380 ⭐ · LC 381 *（有重複：值 -> 索引集合）* · LC 710 *（帶黑名單的隨機抽樣：把被列入
黑名單的低索引重新對應到尾端）*

<!-- c6d3b00ad9bb -->
#### 1-2-6) 有界候選列舉（可能的答案就那幾個）

有些「讓所有元素都相等」的題目看起來要搜尋每一個值，但限制條件把答案釘死在一兩個候選上。列舉
這些候選，每個用 O(n) 驗證。

<!-- e7ec8db03a1c -->
##### Template 8: Bounded Candidate Enumeration — LC 1007

**辨識** — 「讓一整列的**每個**元素／所有元素都等於同一個值，求最少操作數，否則 −1」，而每個元素
只有一兩種選擇。n ≤ 2·10⁴ → 每個候選 O(n) 沒問題，只要候選很少。

**關鍵點** — 如果某個值 `x` 能填滿整列，第 0 張骨牌的某一面一定已經是 `x`，所以
`x ∈ {tops[0], bottoms[0]}`。兩個候選、各用一次掃描驗證，就是全部的搜尋。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：最多呼叫兩次 `rotations_to_show`，每次一個 `for i in range(n)` 掃描。
- **空間 O(1)**：每個候選兩個計數器。

**陷阱**
- **該用 `elif` 卻用了 `else`。** 兩面都是 `x` 的骨牌不用翻。寫成 `else: rot_bottom += 1` 會把它算成
  一次下排翻轉，所以 `tops = [2,1]`、`bottoms = [2,2]` 會回傳 1 而不是 0。
- **第一個候選失敗就回傳**，沒有試 `bottoms[0]`：`tops = [1,2,3]`、`bottoms = [2,2,2]` 對 1 失敗，
  對 2 只需要翻 0 次。
- **把 `1..6` 每個值都跑一遍。** LC 1007 會過，但真正重要的論證（「答案一定在第 0 張骨牌上」）才是
  能遷移的那個，而且值域到 10⁹ 時也還成立。

**練習** — LC 1007 ⭐ · LC 169 *（投票留下一個候選；驗證它）* · LC 2128 *（第 0 列釘住其他每一列：
每列必須等於它或它的反相）*

<!-- 3020c1782346 -->
#### 1-2-7) 其他高頻陣列題（速查）

| 題目 | LC# | Diff | 一句話技巧 |
|---------|-----|------|--------------------|
| Verifying an Alien Dictionary | 953 | Easy | 建 `char -> rank` 表，然後只比**相鄰兩兩**；短的若是前綴，就必須排在前面 |
| Prison Cells After N Days | 957 | Medium | 模擬到狀態**重複**為止，然後 `N %= cycle_len` — 把 `N = 10^9` 變成 O(cycle) |
| Longest Common Prefix | 14 | Easy | 垂直掃描：固定第 `j` 欄，比所有字串的那個字元；第一次不匹配就停 |
| Minimum Moves to Equal Array Elements | 453 | Medium | 「對 n-1 個元素 +1」等價於「對一個元素 -1」⇒ 答案 = `sum(nums) - n * min(nums)` |

---

<!-- 9ef66cff4e7b -->
## 2) 模式選擇

大多數被標成 **array** 的題目，其實不是陣列題。它們是視窗、指標、前綴和或排序的題目，只是剛好裝在陣列裡，
而那些各自都有自己的專頁。這張表的用途是讓你**趕快離開這份文件**——這裡只留真正屬於陣列本身的東西：
**原地改寫它，以及把它的索引當成儲存空間。**

| 題目真正在考的是… | 去哪裡 | 為什麼不在這裡 |
|---|---|---|
| 一段長度或內容會變的連續區間 | [sliding_window.md](./sliding_window.md) | 視窗的擴張／收縮規則才是整題的核心 |
| 兩個索引互相靠近，或一組快慢指標 | [2_pointers.md](./2_pointers.md) | 不變量活在兩個指標之間，不在陣列裡 |
| 反覆查詢區間和或區間計數 | [prefix_sum.md](./prefix_sum.md) | 前處理本身*就是*技巧 |
| 大量區間**更新**、少量讀取 | [difference_array.md](./difference_array.md) | 你更新的是端點，不是整段區間 |
| 在已排序資料中找某個值或某個邊界 | [binary_search.md](./binary_search.md) | 陣列已排序是前提，不是招式 |
| 找和為目標值的兩數或三數 | [n_sum.md](./n_sum.md) | 排序加雙指標，整個家族一次講完 |
| 排序本身——自訂鍵、穩定性、部分排序 | [sort.md](./sort.md) | 比較器就是答案 |
| 帶平手規則的排序鍵，或混合方向的排序 | [python_trick.md](./python_trick.md#multi-key-tuple-sort-keylambda-x-x0-x1-) 與 [java_trick_strings_sorting.md](./java_trick_strings_sorting.md#custom-sort--comparator-return-value-rules-) | 這是比較器的問題，不是陣列的問題 |
| 二維格子 | [matrix.md](./matrix.md) | 走訪順序與邊界處理才是主戲 |
| 以每個索引結尾的最佳和／最佳乘積 | [kadane_algorithm.md](./kadane_algorithm.md) | 那是一行就寫完的 DP 遞迴式 |
| 帶限制的買進賣出 | [stock_trading.md](./stock_trading.md) | 重點在那個狀態機 |

<!-- 01db8faa4122 -->
### 這份文件真正擁有的東西

| 如果你需要… | 技巧 | 寫在哪 |
|---|---|---|
| 原地覆寫而不弄丟還沒讀的資料 | **反向寫入指標** — 從尾端（空位所在）開始填 | [1-2-4)](#1-2-4-backward-write-pointer-in-place-merge--in-place-overwrite-) |
| 不用額外空間就記下「我看過值 `v`」 | **索引當雜湊** — 把 `nums[v]` 變負號，讀取一律透過 `abs()` | [1-2-5)](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-)、[examples 1)](./array_examples.md#1-first-missing-positive--lc-41-) |
| 把每個值放回它該在的索引 | **循環排序** — 一直交換直到 `nums[i] == i + 1`，再掃出缺口 | [examples 1)](./array_examples.md#1-first-missing-positive--lc-41-) |
| 以 O(1) 刪除任意元素 | **跟最後一個交換再彈掉** — 外加一張值 → 索引的表 | [1-2-5)](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-) |
| 用 O(1) 空間找多數元素 | **Boyer-Moore 投票** — 一個候選人加一個計數器，沒別的 | [1-2-1)](#1-2-1-boyer-moore-majority-vote-algorithm) |
| 值域小又有界時統計出現次數 | **頻率陣列**，不要用雜湊表 | [1-2-2)](#1-2-2-frequency-array--running-count) |
| 原地旋轉 `k` 格 | **整個反轉、前 `k` 個反轉、剩下的反轉** | [examples 2)](./array_examples.md#2-rotate-array--lc-189-) |
| 可能的結果就那幾種時該怎麼答 | **有界候選列舉** — 全部試一遍 | [1-2-6)](#1-2-6-bounded-candidate-enumeration-only-a-few-answers-are-possible) |

<!-- aec314495953 -->
### 三個陷阱

1. **正向覆寫會毀掉還沒讀的輸入。** 從前面開始把東西併進 `nums1`，會蓋掉還沒讀到的值。
   要從後面往前填；尾巴才是保證空著的那一段。
2. **用正負號做標記卻沒配 `abs()`。** 一格被變成負的之後，直接拿它去讀就會得到負索引。
   每次讀取都要經過 `abs(...)`，而且如果呼叫端之後還要用這個陣列，記得把正負號還原。
3. **索引當雜湊的前提是那些值本身要是合法索引。** 在拿值當偏移量*之前*，先把 `1..n` 之外的東西夾住或跳過，
   不然這招會變成陣列越界。

<!-- 3b8a06fc2cc7 -->
## 3) 題目詳解

題目詳解放在 **[array_examples.md](./array_examples.md)**：

| 分組 | 題目 |
|---|---|
| [In-place rewriting & index tricks](./array_examples.md#in-place-rewriting--index-tricks) | LC 41, 287, 189, 238, 670 |
| [Scanning & running state](./array_examples.md#scanning--running-state) | LC 121, 1567, 334, 849 |
| [Counting, bookings & simulation](./array_examples.md#counting-bookings--simulation) | LC 1109, 1375, 1041, 406, 251 |

<!-- 700a43122e6c -->
## 4) 練習路徑

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** | LC 88 · LC 169 · LC 238 ⭐ · LC 380 | 反向寫入；投票；前綴 × 後綴乘積（[array_examples](./array_examples.md#3-product-of-array-except-self--lc-238-)）；和最後一個交換再刪除 |
| **練習** | LC 189 · LC 229 · LC 251 · LC 1007 · LC 2063 · LC 2657 | 用三次反轉做旋轉；k − 1 個候選；惰性迭代器；從索引 0 取候選；貢獻計數；頻率陣列 |
| **延伸** | LC 41 · LC 381 · LC 828 | 循環排序／索引當雜湊；有重複時的交換刪除；以每個字元跨度計算的貢獻 |
