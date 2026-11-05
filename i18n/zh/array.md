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

<!-- f44c42d75b35 -->
### 0-2) 辨識

多數「陣列」題其實會被導到某個模式文件（見 [2) 模式選擇](#2-pattern-selection)）。
下面這些訊號才是留在這份文件的。限制條件的提示依照
[complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的尺規。

| 題目敘述裡的訊號 | 限制條件提示 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「把 `nums2` **原地**合併進 `nums1`」、「最後 n 格是 0，應該忽略」 | 追問 O(1) 額外空間 | [2_pointers § Template 5](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) 反向寫入指標 | 空位在前面（移除重複）→ 正向快慢指標，[2_pointers.md](./2_pointers.md) |
| 「`insert` / `remove` / `getRandom` 都要**平均 O(1)**」 | 約 10⁵ 次呼叫 | [Template 7](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-) 和最後一個交換再刪除 | 允許重複（LC 381）→ 值對應到一組索引的集合 |
| 「出現**超過 ⌊n/2⌋** 次」、「O(1) 額外空間」 | n ≤ 5·10⁴，一次掃描 | [Template 2](#1-2-1-boyer-moore-majority-vote-algorithm) 多數投票 | 「最常出現」／「前 k 個」→ 需要計數，[hash_map.md](./hash_map.md) |
| 「所有出現**超過 ⌊n/3⌋** 次的元素」 | O(1) 空間 | [Template 3](#1-2-1-boyer-moore-majority-vote-algorithm) k − 1 個候選 | |
| 「`1..n` 的排列」、「對每個前綴，有幾個 …」 | 值 ≤ n → 陣列本身就是雜湊表 | [Template 4](#1-2-2-frequency-array--running-count) 頻率陣列＋即時累計 | 值可到 10⁹ → 用雜湊表，做同樣的「跨過門檻」判斷 |
| 「**所有子字串／子陣列**上 X 的總和」 | n ≤ 10⁵ → 無法列出 O(n²) 個子陣列 | [Template 5](#1-2-3-index-contribution-counting-count-each-elements-appearances-across-all-subarrays) `(i + 1) * (n - i)` | 元素只有在是最小／最大值時才算（LC 907）→ [monotonic_stack.md](./monotonic_stack.md) |
| 「讓一整列的元素都相等，求最少操作數，否則 −1」 | 每個元素只有約 2 種選擇 | [Template 8](#1-2-6-bounded-candidate-enumeration-only-a-few-answers-are-possible) 從索引 0 取候選 | 目標可以是任何值 → 排序／中位數，[sort.md](./sort.md) |
| 在二維陣列上「設計一個迭代器」，`next()` / `hasNext()` | 每次呼叫攤銷 O(1) | [Template 1](#1-1-6-flatten-array) 兩個游標 | 巢狀深度不固定（LC 341）→ 用迭代器的堆疊，[iterator.md](./iterator.md) |
| 「值在 `[1, n]` 之間」、「缺少的／重複的／第一個缺少的正數」、「O(1) 額外空間」 | n ≤ 10⁵，一到兩次掃描 | [Template 9](#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) 循環排序或正負號標記 | 不准修改輸入（LC 287）→ 在值域上二分搜尋，[binary_search_on_answer.md](./binary_search_on_answer.md) |
| 「**原地**往右旋轉 `k` 步」、兩個區塊互換位置 | k 可能大於 n | [Template 10](#1-2-9-rotate-in-place-by-three-reversals-) 三次反轉 | 是鏈結串列（LC 61）→ 重新接上新的尾巴 |
| 「到最近的 … 的最大距離」、「有幾個前綴 …」、答案只取決於前綴的一個摘要 | 一次掃描，O(1) 空間 | [Template 11](#1-2-10-one-pass-running-state-) 即時狀態 | 摘要被打敗時要丟掉 → [monotonic_stack.md](./monotonic_stack.md) |

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

<!-- a57df133d62f -->
#### 1-1-5) 排序陣列*****

**`list.sort()` 是原地排序，回傳 `None`。`sorted()` 回傳一個新的 list。**

| | `nums.sort(...)` | `sorted(nums, ...)` |
|---|---|---|
| **原地？** | ✅ 是 — `nums` 會被改動 | ❌ 否 — 原本的不動 |
| **回傳值** | `None` | 新的已排序 `list` |
| **可用對象** | 只有 `list` | **任何 iterable**（str、tuple、set、dict、generator） |
| **空間** | 額外 O(1)* | O(n) |
| **穩定？** | ✅ 是（Timsort） | ✅ 是（Timsort） |

> \* CPython 的 Timsort 最壞情況可能需要一塊 O(n) 的暫存區，但**不會配置新的 list 物件**。

<!--CODE-->

**常見錯誤**

<!--CODE-->

**什麼時候用哪一個**

- **`.sort()`** → list 是你的、而且想省空間到 O(1)（例如 LC 406、LC 56 Merge Intervals、LC 253）
- **`sorted()`** → 輸入是字串／dict／set／tuple，或原本的順序必須保留

**Java 的對應寫法：**

<!--CODE-->

<!-- 21f36f83d1c9 -->
#### 1-1-6) 攤平陣列

攤平巢狀 list 就是一次深度優先走訪：整數是葉子，直接輸出；list 是節點，依序走訪它的子元素。
Python 沒有能處理任意深度的內建函式。

<!--CODE-->

第二個版本留著是有理由的：同樣的走訪寫成 **generator** 就是惰性的。事前什麼都不建，呼叫端提早停下時，
剩下的部分也不會被走到。這正是下面 Template 1 替迭代器類別做的「急切或惰性」選擇。

<!--CODE-->

LeetCode 有兩種設計把這個走訪變成迭代器類別：

- **深度固定為二**（LC 251 Flatten 2D Vector）：用兩個游標取代遞迴，就是下面的 Template 1；
- **深度不固定**（LC 341 Flatten Nested List Iterator）：用一個待處理項目的堆疊取代遞迴，在
  `hasNext()` 裡惰性地拆開。詳解在
  [iterator.md § 2-2](./iterator.md#2-2-flatten-nested-list-iterator--lc-341)。

<!-- b7870fae76d3 -->
##### Template 1: Lazy Two-Cursor Iterator — LC 251 ⭐⭐⭐⭐

**辨識** — 在二維或巢狀陣列上「設計一個迭代器」／「實作 `next()` 和 `hasNext()`」，常會追問
「輸入非常大」或「只用 O(1) 額外空間」。呼叫次數 ≤ 10⁵ → 每次呼叫必須攤銷 O(1)。

**關鍵** — `(row, col)` 永遠指向**原本的**陣列，而且 `hasNext()` 回傳之後它一定停在一個真正的
元素上；只有 `next()` 會越過元素。所以什麼都不用複製，而每一列在整趟走訪中只會被跳過一次。

| | 急切式（在建構子裡攤平） | **惰性（兩個游標）** |
|---|---|---|
| 建構子 | O(N)：複製每個元素 | O(1) |
| 額外空間 | O(N) | **O(1)** |
| `next()` / `hasNext()` | O(1) | 攤銷 O(1)：整趟走訪中每一列只被跳過一次，總共 O(R + N) |
| 來源很大或會變動時 | 不行：它是快照 | 可以：沒有預先讀取任何東西 |

`N` = 元素總數，`R` = 列數。急切式版本就是上面的 `flatten`：在建構子裡執行，`next()` 讀存好的 list。
面試官說「設計一個迭代器」指的是惰性版本，它也是 iterator.md 裡
[`hasNext()` / `next()` 約定](./iterator.md#1-2-the-hasnext--next-contract-) 的二維特例：
深度固定為二，所以兩個索引就能取代堆疊。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間：每次呼叫攤銷 O(1)**：`hasNext()` 裡的 `while` 可能一次跳過好幾列，但 `row` 只會往前，
  所以整趟走訪中它總共只前進 R 次：O(R + N)。
- **空間：O(1) 額外**：`self.vec = vec` 存的是參考而不是複本；狀態只有兩個整數。

**試跑**

<!--CODE-->

**陷阱**
- **`next()` 盲信游標。** `self.col += 1; return vec[row][col - 1]` 只有在先呼叫過 `hasNext()`
  時才正確。在 `[[], [3]]` 上直接呼叫 `next()` 會讀到空的第 0 列而崩潰。在 `next()` 裡先正規化
  不花任何成本，就能消除這個依賴。
- **`hasNext()` 會消耗元素。** 它可以跳過什麼都沒有的列，但絕不能越過真正的元素；只有 `next()`
  可以。
- **該用 `while` 卻用了 `if`。** 連續兩個空列（`[[1], [], [], [2]]`）需要跳不只一次。
- **換列時忘了 `col = 0`**：在 `[[1, 2], [3]]` 上，第二列會從索引 2 開始讀而被整列跳過。

**不適用時** — 巢狀深度不固定（LC 341 Flatten Nested List Iterator）：兩個游標追蹤不了
無上限的深度，改用迭代器的堆疊
（[iterator.md](./iterator.md#1-2-the-hasnext--next-contract-)）。

**練習** — LC 251 ⭐ · LC 284 *（peek：先快取下一個元素）* · LC 281 *（zigzag：游標的佇列）* ·
LC 341 *（深度不固定：游標的堆疊）*

<!-- 368ae77e3b35 -->
#### 1-1-7) 同時走訪兩個陣列（長度可能不同）

走到**較長**的那個長度，把超出範圍的位置當成預設值讀，這樣兩個陣列都不必先補齊、複製或交換。
LC 165 Compare Version Numbers 就是在兩串版本號上做這種走訪，缺少的版本號算 0。

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

**關鍵** — 每一次減一，都是把一個 `candidate` 和一個*不同的*值互相抵銷。出現超過 n/2 次的值
不可能被完全抵銷，所以最後留下的候選就是它。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：唯一的 `for num in nums` 迴圈對每個元素做 O(1) 的事（若要驗證，再多一次 O(n)
  掃描）。
- **空間 O(1)**：全部的狀態就是 `candidate` 和 `count`。

**試跑**

<!--CODE-->

**陷阱**
- **沒有保證多數存在時卻省略驗證。** `[1,2,3]` 最後的候選是 3，但它只出現一次。投票只告訴你：
  *如果*有多數元素，*就是*它。
- **候選不是最常出現的元素。** `[1,1,2,2,3]`（沒有多數）最後停在 3，不是 1 或 2。Boyer-Moore
  回答的是「> n/2」，從來不是「眾數」。

**不適用時** — 門檻是「最常出現」或「前 k 個」，而不是「超過 n/k」：需要真正的計數，
改用雜湊表加桶或堆積（[hash_map.md](./hash_map.md)）。

**練習** — LC 169 ⭐ · LC 1150 *（已排序：改用二分搜尋）* · LC 229 *（> n/3：Template 3）* ·
LC 1157 *（區間內的多數：隨機抽樣或投票線段樹）*

<!-- a88bd9d8e987 -->
##### Template 3: Majority Vote, k − 1 Candidates — LC 229

**辨識** — 「所有出現**超過 ⌊n/3⌋ 次**（或 ⌊n/k⌋）的元素」，追問「O(1) 空間」。最多只有 k − 1
個值能超過 n/k 的門檻，所以答案是最多 k − 1 個元素的清單。

**關鍵** — 和 k − 1 個候選都不同的值，會**各抵銷一份**，一次移除 k 個值。出現超過 n/k 次的值，
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

**關鍵** — `count[v]` 是目前為止看過 `v` 幾次，而 `common` 只在某個 `count[v]` **剛好達到**門檻
（這裡是 2：A 裡一次、B 裡一次）的那一步改變。每個值最多跨過門檻一次，所以 `common` 正好等於
已經跨過的值的個數。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一個 `for i in range(n)`，每步兩次 O(1) 的陣列更新，不重掃前綴。
- **空間 O(n)**：`count = [0] * (n + 1)`，大小由值域決定（一般是 O(最大值)）。

**試跑**

<!--CODE-->

**陷阱**
- **值是 `1..n` 卻開 `[0] * n`。** `A = [1]` 會在長度 1 的 list 上存取 `count[1]`：IndexError
  （或 `ArrayIndexOutOfBoundsException`）。要開 `n + 1`。
- **先判斷再加一。** `if count[x] == 2: common += 1` 之後才 `count[x] += 1`，算到的是跨過門檻的
  *下一步*，所以 `A = [1], B = [1]` 會回傳 `[0]` 而不是 `[1]`。
- **陣列不是排列時用 `>= 2` 取代 `== 2`**：出現三次的值會被算兩次。要判斷的是「跨過」，不是「狀態」。

**不適用時** — 值無界或很稀疏（`-10⁹ ≤ v ≤ 10⁹`）：開不了那麼大的陣列，改用雜湊表
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

**關鍵** — 包含索引 `i` 的子陣列，就是在 `0..i` 選左端、在 `i..n-1` 選右端，所以 `i` 恰好落在
`(i + 1) * (n - i)` 個子陣列裡。當元素的貢獻和它所在的子陣列無關時，總和就是
`value(i) * (i + 1) * (n - i)` 的加總。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：一個 `for i in range(n)`；乘積取代了 O(n²) 的 `(left, right)` 列舉。
- **空間 O(1)**：一個累計總和。

**試跑**

<!--CODE-->

**陷阱**
- **邊界差一。** `i * (n - i)` 漏掉了從 `i` 開始的子陣列；對 `"a"` 會回傳 0 而不是 1。左端有
  `i + 1` 種選擇，右端有 `n - i` 種。
- **Java 的 `int` 溢位。** n = 10⁵ 時光是中間那個索引就貢獻約 2.5·10⁹，超過 `Integer.MAX_VALUE`。
  用 `long` 累加，並在相乘*之前*就轉型。
- **「包含 i」與「從 i 開始／在 i 結束」。** 從 `i` 開始的子陣列：`n - i`；在 `i` 結束：`i + 1`；
  包含 `i`：兩者相乘。把它們搞混是最常見的錯誤答案。

**不適用時** — 貢獻取決於子陣列，例如元素只有在是最小值時才算（LC 907）：它的範圍被最近的
較小元素限制住，所以跨度要用 [monotonic stack](./monotonic_stack.md) 求，而不是 `i + 1` 和 `n - i`。

**練習** — LC 2063 ⭐ · LC 1588 *（奇數長度子陣列：數奇數長度的 (left, right) 配對）* ·
LC 828 *（唯一字元：到前一個與下一個相同字元的跨度）* · LC 907 *（最小值：單調堆疊跨度）* ·
LC 2104 *（最大值貢獻減最小值貢獻）*

<!-- 6b6169a05717 -->
#### 1-2-4) 反向寫入指標（原地合併／原地覆寫） ⭐⭐⭐⭐⭐

當輸出要寫進同一個還在讀取的陣列，而空位在尾端時，就從後面往前寫。

卡片在 [2_pointers.md § Template 5](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-)：
LC 88 Merge Sorted Array、讓它安全的不變量（`write >= p1`，所以正要寫入的格子不是已經讀過就是填充區）、
Python 和 Java、手動追蹤和陷阱。兩個讀取指標加一個寫入指標，讓它成為指標模式，所以由那份文件擁有；
這份文件只保留上面那條規則，因為那是關於覆寫陣列的事實。

合併以外的同一條規則：

- **LC 1089 Duplicate Zeros**：先數有幾個零，就知道最後一個留下的值落在哪裡，再從後往前複製，
  每個零寫兩次。
- **LC 977 Squares of a Sorted Array**：最大的平方在兩端之一，所以從後面往前填結果。
- **LC 443 String Compression** 則是相反的情況：輸出永遠不比已讀的部分長，所以*正向*寫入指標是安全的。

<!-- 439e056ccfcc -->
#### 1-2-5) 陣列＋雜湊索引表（用「和最後一個交換」做 O(1) 刪除） ⭐⭐⭐⭐⭐

從陣列中間刪除之所以是 O(n)，只是因為要搬移。如果順序不重要，就用最後一個元素補洞，再彈出尾端。

<!-- 2fd1f44d1dc4 -->
##### Template 7: Swap-With-Last Delete — LC 380

**辨識** — 「實作 `insert`、`remove` 和 `getRandom`，每個都要**平均 O(1)** 時間」、「每個元素被回傳
的機率必須相同」。最多 2·10⁵ 次呼叫 → 每個操作 O(1)。

**關鍵** — `vals` 永遠是緊密的（沒有洞），所以 `vals[隨機索引]` 是均勻抽樣，而 `idx[v]` 永遠是 `v`
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

**不適用時** — 允許重複（LC 381）：一個值有好幾個位置，所以 `idx` 要把值對應到索引的**集合**，
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

**關鍵** — 如果某個值 `x` 能填滿整列，第 0 張骨牌的某一面一定已經是 `x`，所以
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

<!-- d6516c699bf0 -->
#### 1-2-7) 其他高頻陣列題（速查）

| 題目 | LC# | Diff | 一句話技巧 |
|---------|-----|------|--------------------|
| Verifying an Alien Dictionary | 953 | Easy | 建 `char -> rank` 表，然後只比**相鄰兩兩**；短的若是前綴，就必須排在前面 |
| Prison Cells After N Days | 957 | Medium | 模擬到狀態**重複**為止，然後 `N %= cycle_len` — 把 `N = 10^9` 變成 O(cycle) |
| Longest Common Prefix | 14 | Easy | 垂直掃描：固定第 `j` 欄，比所有字串的那個字元；第一次不匹配就停 |
| Minimum Moves to Equal Array Elements | 453 | Medium | 「對 n-1 個元素 +1」等價於「對一個元素 -1」⇒ 答案 = `sum(nums) - n * min(nums)` |

<!-- 06fa3f821895 -->
#### 1-2-8) 索引當雜湊（循環排序與正負號標記） ⭐⭐⭐⭐⭐

當值是 `1..n`，或只有這些值重要時，陣列自己的索引就是一張有 `n` 個桶的雜湊表：值 `v` 屬於索引 `v - 1`。
用法有兩種，都只要 O(1) 額外空間。把每個值**搬**到它的位置（循環排序，就是下面的模板），或是把
`v - 1` 那格變成負數來**標記**（正負號標記，在
[array_examples.md](./array_examples.md#index-as-hash-examples) 用 LC 448 和 LC 442 示範）。

<!-- 16f26198b518 -->
##### Template 9: Cyclic Sort, Index as Hash — LC 41 ⭐⭐⭐⭐⭐

**辨識** — 「值在 `[1, n]` 範圍內」、「找出缺少的／重複的／第一個缺少的正數」，並要求「O(n) 時間、
**O(1) 額外空間**」，這就排除了 set。n ≤ 10⁵。

**關鍵** — 交換迴圈結束後，每個出現過的 `1..n` 值 `v` 都在索引 `v - 1`。每次交換都把一個值永久送回家，
已經在家的值不會再被搬動，所以第一個 `nums[i] != i + 1` 的索引 `i`，就指出第一個缺少的正數。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while` 看起來是巢狀，但每次交換都把一個值永久送回家，所以所有 `while` 加起來最多
  交換 n 次。最後的掃描是一次走訪。
- **空間 O(1)**：交換都在 `nums` 裡進行；`home` 是唯一的額外變數。

**試跑**

<!--CODE-->

**陷阱**
- **沒有判斷重複。** 少了 `nums[nums[i] - 1] != nums[i]`，`[1, 1]` 會把 1 和已經在家的 1 永遠交換下去。
  要比的是*值*，不是 `home != i`。
- **Python 一行交換。** `nums[i], nums[nums[i] - 1] = nums[nums[i] - 1], nums[i]` 會先賦值
  `nums[i]`，於是第二個目標是用*新的*值算出來的。在 `[3, 4, -1, 1]` 上它會弄亂陣列而且永遠不停。
  先把 `home` 算好。
- **超出範圍的值**（`0`、負數、`> n`）沒有位置。放著不管；最後的掃描會把它們的索引當成缺口回報。
- **回傳 `n`**：`1..n` 全部都在時，`[1, 2]` 必須回傳 3。

**不適用時** — 不准修改輸入：LC 287 Find the Duplicate Number 禁止修改，所以它是在值域上二分搜尋，
或用 Floyd 的環偵測
（[binary_search_on_answer.md](./binary_search_on_answer.md)）。
如果允許 O(n) 額外空間，用 set 更簡單，也不會動到輸入。

**練習** — LC 41 ⭐ · LC 268 *（缺少的數字：總和或 XOR 更簡單，循環排序也行）* ·
LC 448 *（每個缺少的值：用正負號標記）* · LC 442 *（每個重複的值：某格已經是負的）*
· LC 645 *（集合不符：一個重複、一個缺少，同一次掃描找出）*

<!-- 32cd2fc36bf9 -->
#### 1-2-9) 用三次反轉原地旋轉 ⭐⭐⭐⭐

旋轉 `k` 格就是讓陣列的兩個區塊互換位置，同時保持各自的內部順序。反轉可以原地做到這件事。

<!-- 348c49a578d9 -->
##### Template 10: Rotate by Three Reversals — LC 189 ⭐⭐⭐⭐

**辨識** — 「把陣列往右旋轉 `k` 步」、「**原地**、O(1) 額外空間」；更一般地說，兩個相鄰的區塊要互換位置
並保持各自的內部順序（原地反轉字串裡的單字）。n ≤ 10⁵、k 最多 10⁵，所以 `k` 可能大於 n。

**關鍵** — 令 `A = nums[0 .. n-k-1]`、`B = nums[n-k .. n-1]`，往右旋轉就是把 `A B` 變成 `B A`。
把整個陣列反轉得到 `rev(B) rev(A)`：每個區塊都已在最終位置，只是方向相反；再各自反轉一次就恢復內部順序。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：三次 `reverse` 呼叫總共走過陣列兩遍，每一對做一次交換。
- **空間 O(1)**：`reverse` 在 `nums` 裡面交換。

**試跑**

<!--CODE-->

**陷阱**
- **沒有 `k %= n`。** `[1, 2]` 配 `k = 5`：`reverse(0, 4)` 會讀出界。
- **先做兩段區塊反轉。** 在整個陣列反轉*之前*先反轉 `[0, k-1]` 和 `[k, n-1]`，會變成往**左**旋轉：
  `[1..7]`、`k = 3` 得到 `[4, 5, 6, 7, 1, 2, 3]`。
- **`nums = nums[-k:] + nums[:-k]`** 只是重新綁定區域變數名稱，呼叫端的 list 沒變。
  `nums[:] = ...` 可以修好，但會建一份 O(n) 的複本。
- **`pop()` 再 `insert(0, …)`，做 k 次**是 O(n·k)：每次在開頭插入都要搬動整個 list。

**不適用時** — 序列是鏈結串列（LC 61 Rotate List）：找到新的尾巴重新接上，不需要反轉。
如果允許開新陣列，`nums[i]` 一次就能放到 `(i + k) % n`。

**練習** — LC 189 ⭐ · LC 151 *（反轉單字：整個字串反轉，再反轉每個單字）* ·
LC 186 *（LC 151 的原地版本）* · LC 61 *（旋轉鏈結串列：改成重新接線）*

<!-- 0a2050303027 -->
#### 1-2-10) 一次掃描的即時狀態 ⭐⭐⭐⭐

很多陣列題根本不需要資料結構：掃一次，帶著決定答案的一兩個值走，例如某樣東西最後出現的索引、
目前為止的最大值，或是位置加方向。把這些值說出來，就是整道題。

<!-- f575401c4a94 -->
##### Template 11: Running State Scan, Last-Seen Index — LC 849 ⭐⭐⭐⭐

**辨識** — 「到最近的 … 的最大距離」、「前綴有幾次 …」、「會不會回到 …」：索引 `i` 的答案只取決於
它之前所有東西的一個摘要（最後一個有人坐的位子、目前最大值）。預期一次掃描、O(1) 額外空間；n ≤ 2·10⁴。

**關鍵** — `last` 是最近一個有人坐的位子的索引。坐在 `last` 和 `i` 兩個有人的位子之間的空段，
可以得到距離 `(i - last) // 2`。第一個有人位子之前的那段、最後一個有人位子之後的那段，只有一邊有人，
所以整段長度都算。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：對座位做一次 `for`，每一步 O(1)。
- **空間 O(1)**：整個狀態就是 `last` 和 `best`。

**試跑**

<!--CODE-->

**陷阱**
- **把兩端的空段減半。** `[0, 0, 0, 1]` 會回傳 1 而不是 3：坐在第 0 號位子只有一邊有人。
- **漏了尾端空段。** `[1, 0, 0, 0]` 會回傳 0 而不是 3，因為之後沒有任何 `1` 來結束最後那段。
- **Python 的 `/`。** `(i - last) / 2` 在 `[1, 0, 0, 1]` 上得到 `1.5`；要用 `//`。

**不適用時** — 每個索引都要知道它到*兩側*最近標記的距離（LC 821 Shortest Distance to a Character）：
兩次掃描，各從一端開始。如果帶著的值一旦被打敗就必須丟掉（下一個更大元素），狀態就是一個堆疊：
[monotonic_stack.md](./monotonic_stack.md)。

**練習** — LC 849 ⭐ · LC 121 *（目前為止的最低價：[stock_trading.md](./stock_trading.md)）* ·
LC 1375 *（目前最大值等於步數）* · LC 1041 *（一次走完後的位置和方向）* ·
LC 670 *（從右邊掃，帶著最大值的索引）*

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

<!-- cd352455abe6 -->
### 這份文件真正擁有的東西

| 如果你需要… | 技巧 | 寫在哪 |
|---|---|---|
| 原地覆寫而不弄丟還沒讀的資料 | **反向寫入指標** — 從尾端（空位所在）開始填 | [1-2-4)](#1-2-4-backward-write-pointer-in-place-merge--in-place-overwrite-) |
| 不用額外空間就記下「我看過值 `v`」 | **索引當雜湊，正負號標記** — 把 `nums[v - 1]` 變負號，讀取一律透過 `abs()` | [1-2-8)](#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-)、[examples](./array_examples.md#index-as-hash-examples) |
| 把每個值放回它該在的索引 | **循環排序** — 一直交換直到 `nums[i] == i + 1`，再掃出缺口 | [1-2-8)](#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) |
| 以 O(1) 刪除任意元素 | **跟最後一個交換再彈掉** — 外加一張值 → 索引的表 | [1-2-5)](#1-2-5-array--hash-index-map-o1-delete-via-swap-with-last-) |
| 用 O(1) 空間找多數元素 | **Boyer-Moore 投票** — 一個候選人加一個計數器，沒別的 | [1-2-1)](#1-2-1-boyer-moore-majority-vote-algorithm) |
| 值域小又有界時統計出現次數 | **頻率陣列**，不要用雜湊表 | [1-2-2)](#1-2-2-frequency-array--running-count) |
| 原地旋轉 `k` 格 | **整個反轉、前 `k` 個反轉、剩下的反轉** | [1-2-9)](#1-2-9-rotate-in-place-by-three-reversals-) |
| 可能的結果就那幾種時該怎麼答 | **有界候選列舉** — 全部試一遍 | [1-2-6)](#1-2-6-bounded-candidate-enumeration-only-a-few-answers-are-possible) |
| 答案只取決於到目前為止的一個摘要 | **即時狀態** — 帶著最後的索引、目前最大值、位置 | [1-2-10)](#1-2-10-one-pass-running-state-) |

<!-- aec314495953 -->
### 三個陷阱

1. **正向覆寫會毀掉還沒讀的輸入。** 從前面開始把東西併進 `nums1`，會蓋掉還沒讀到的值。
   要從後面往前填；尾巴才是保證空著的那一段。
2. **用正負號做標記卻沒配 `abs()`。** 一格被變成負的之後，直接拿它去讀就會得到負索引。
   每次讀取都要經過 `abs(...)`，而且如果呼叫端之後還要用這個陣列，記得把正負號還原。
3. **索引當雜湊的前提是那些值本身要是合法索引。** 在拿值當偏移量*之前*，先把 `1..n` 之外的東西夾住或跳過，
   不然這招會變成陣列越界。

<!-- fc8847492297 -->
## 3) 題目詳解

題目詳解放在 **[array_examples.md](./array_examples.md)**，依各題歸約到的模板分類。它的
[索引](./array_examples.md#index) 也列出那些標成陣列、但歸屬在別份文件的題目。

| 模板 | 題目 |
|---|---|
| [Template 9: Cyclic Sort, Index as Hash](./array_examples.md#index-as-hash-examples) | LC 448, 442 |
| [Template 11: Running State Scan](./array_examples.md#one-pass-running-state-examples) | LC 1375, 1041, 670 |

<!-- 6c85ffd56ee9 -->
## 4) 練習路徑

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** | LC 88 · LC 169 · LC 238 ⭐ · LC 380 | 反向寫入（[2_pointers](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-)）；投票；前綴 × 後綴乘積（[prefix_sum](./prefix_sum.md#product-variant--no-inverse-so-build-both-sides-lc-238-)）；和最後一個交換再刪除 |
| **練習** | LC 849 · LC 448 · LC 189 · LC 229 · LC 251 · LC 1007 · LC 2063 · LC 2657 | 即時狀態；正負號標記；用三次反轉做旋轉；k − 1 個候選；惰性迭代器；從索引 0 取候選；貢獻計數；頻率陣列 |
| **延伸** | LC 41 · LC 381 · LC 828 | 循環排序／索引當雜湊；有重複時的交換刪除；以每個字元跨度計算的貢獻 |
