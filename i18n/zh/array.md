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

<!-- ce142482bd5a -->
#### 1-1-7) 同時走訪兩個陣列（長度可能不同）
<!--CODE-->


---

<!-- 944969701590 -->
### 1-2) 特殊的陣列演算法 ⭐⭐⭐⭐

<!-- a27f18562320 -->
#### 1-2-1) Boyer-Moore 多數投票演算法

**概念：**
- 找出陣列中出現超過 ⌊n/k⌋ 次的元素
- **核心想法**：把不同的元素兩兩配對抵銷掉
- 多數元素一定能撐過抵銷
- **兩階段**：(1) 找候選人，(2) 驗證次數
- **空間**：O(k)，用來裝 k-1 個候選人

**什麼時候用：**
- 「找出多數元素」→ 出現超過 n/2 次的元素
- 「找出所有出現超過 n/3 次的元素」
- 「heavy hitters」或「高頻元素」類題目
- 需要 O(1) 空間（比 HashMap 的 O(n) 好）

**相關：** 詳細說明見 [streaming_algorithms.md](./streaming_algorithms.md)。

---

<!-- f925f10ca7dc -->
##### **模式 1：標準多數元素（> n/2）- LC 169**

**演算法：**
- 維護一個候選人和一個計數
- 計數歸零時，換一個新的候選人
- 遇到相同元素就加一，不同就減一
- 多數元素一定活到最後

<!--CODE-->

<!--CODE-->

**執行過程：** `nums = [2,2,1,1,1,2,2]`

<!--CODE-->

---

<!-- 0dadd6f894d4 -->
##### **模式 2：出現超過 n/3 次的元素 - LC 229**

**關鍵洞見：** 最多只會有 2 個元素出現超過 n/3 次。

**演算法：**
- 維護兩個候選人和兩個計數
- 抵銷時要把兩個計數同時減一
- 第二階段必須把兩個候選人都驗過

<!--CODE-->

<!--CODE-->

**執行過程：** `nums = [3,2,3]`

<!--CODE-->

---

<!-- f17cc5d6e662 -->
##### **模式 3：一般化的 k 多數（> n/k 次）**

**概念：** 出現超過 n/k 次的元素，最多只會有 k-1 個。

<!--CODE-->

---

<!-- db594ada2b76 -->
#### **Boyer-Moore — 常見錯誤與訣竅**

**🚫 常見錯誤：**

1. **漏掉驗證階段**
<!--CODE-->

2. **LC 229 的抵銷邏輯寫錯**
<!--CODE-->

3. **沒處理候選人重複的情況（LC 229）**
<!--CODE-->

**💡 面試技巧：**

1. **什麼時候用：**
   - 題目出現「找出多數元素」這種字眼
   - 需要 O(1) 空間（相對於 HashMap 的 O(n)）
   - 題目保證多數元素存在

2. **可以講的重點：**
   - 「配對抵銷會把非多數的元素消掉」
   - 「出現超過 n/k 次的元素最多只有 k-1 個」
   - 「兩階段：先找候選人，再驗證」

3. **複雜度：**
   - 時間：O(n) 單趟掃描（＋驗證的 O(n) = 總共 O(n)）
   - 空間：標準版 O(1)，一般化版 O(k)

---

<!-- 958cf20c02d5 -->
#### **Boyer-Moore — 相關 LeetCode 題目**

| 題目 | Difficulty | 門檻 | 候選人數 | 要驗證？ |
|---------|------------|-----------|------------|---------|
| LC 169 | Easy | > n/2 | 1 | 可選* |
| LC 229 | Medium | > n/3 | 2 | 必須 |
| 一般化 | - | > n/k | k-1 | 必須 |

*題目若保證多數元素存在，就可以不驗。

---

**重點整理：**
- ✅ Boyer-Moore：O(n) 時間、O(1) 空間找出多數元素
- ✅ 關鍵洞見：抵銷會把非多數的元素消掉
- ✅ 兩階段：(1) 找候選人，(2) 驗證次數
- ✅ > n/2 用 1 個候選人，> n/3 用 2 個，> n/k 用 k-1 個
- ✅ 替代方案：用 HashMap，花 O(n) 空間換精確次數

---

<!-- 1e788ec1c8ef -->
#### 1-2-2) 頻率陣列＋即時累計

**概念：**
- 當數值範圍有界（例如 1 到 n）時，用頻率陣列追蹤每個元素出現幾次
- 把陣列索引當成雜湊鍵，換取 O(1) 查詢
- 維護一個隨條件達成而更新的累計值
- **關鍵洞見**：某個元素的頻率一達到門檻，就觸發一個動作

**什麼時候用：**
- 數值有界／受限（例如值為 1 到 n 的排列）
- 需要追蹤「兩邊都出現過」或「出現了 k 次」這種條件
- 前綴式的計數問題
- 即時／串流的計數更新

**相關：** 排列類問題、前綴陣列、交集計數都用得上。

---

<!-- 11a3d26c7fb4 -->
##### **模式：Prefix Common Array（LC 2657）**

**題目：** 給兩個長度為 n 的排列 A 和 B，求 C，其中 C[i] = 在索引 i（含）之前同時出現在 A 和 B 裡的數字個數。

**演算法：**
- 用大小為 n+1 的頻率陣列（因為值是 1 到 n）
- 每個索引都處理 A[i] 和 B[i]
- 任何元素的頻率一到 2，就代表它是共同元素（兩個陣列都看過）
- 累計值加一，寫進結果

<!--CODE-->

<!--CODE-->

**執行過程：** `A = [1,3,2,4], B = [3,1,2,4]`

<!--CODE-->

---

<!-- b5a4590d3509 -->
##### **一般化模式：頻率門檻偵測**

需要偵測元素何時達到某個特定次數門檻時，就用這個模式：

<!--CODE-->

---

<!-- 44794a8d24f9 -->
#### **頻率陣列＋即時累計 — 常見錯誤與訣竅**

**🚫 常見錯誤：**

1. **陣列開錯大小**
<!--CODE-->

2. **在遞增之前就檢查門檻**
<!--CODE-->

3. **沒處理同一個索引上兩個陣列出現相同元素的情況**
<!--CODE-->

**💡 面試技巧：**

1. **什麼時候用：**
   - 題目出現「排列」或「值為 1 到 n」這種字眼
   - 要求「前綴」或「即時」的計數
   - 「共同元素」或「交集」類的題目
   - 值有界，而且需要 O(1) 查詢

2. **可以講的重點：**
   - 「用陣列索引當雜湊鍵，查詢就是 O(1)」
   - 「用頻率門檻來觸發條件」
   - 「即時累計可以省掉重算」

3. **複雜度：**
   - 時間：O(n) 單趟掃描
   - 空間：頻率陣列要 O(n) 或 O(max_value)

---

<!-- 7aa9e1be9f8b -->
#### **頻率陣列＋即時累計 — 相關 LeetCode 題目**

| 題目 | Difficulty | 模式變形 |
|---------|------------|-----------------|
| LC 2657 | Medium | 前綴共同陣列（frequency == 2） |
| LC 349 | Easy | 兩陣列交集（兩邊 frequency >= 1） |
| LC 350 | Easy | 允許重複的交集 |
| LC 442 | Medium | 找重複元素（frequency == 2，原地） |
| LC 448 | Easy | 找缺失的數字（frequency == 0） |
| LC 645 | Easy | Set mismatch（frequency == 2 且 == 0） |
| LC 1 | Easy | Two Sum（查補數的頻率） |
| LC 217 | Easy | 是否含重複（frequency >= 2） |

---

**重點整理：**
- ✅ 頻率陣列：值有界時，用陣列索引當雜湊（O(1) 查詢）
- ✅ 即時累計：維護累積計數，達到門檻時更新
- ✅ 關鍵洞見：frequency[x] == k 代表 x 在各來源中總共出現了 k 次
- ✅ 最適合：排列、前綴問題、交集計數
- ✅ 替代方案：值無界或稀疏時改用 HashMap

---

<!-- 08dfc44e3ab8 -->
#### 1-2-3) 索引貢獻計數（統計每個元素在所有子陣列中出現幾次）

**概念：**
- 與其把每個子陣列都列舉一遍（O(n²) 甚至更糟），不如反過來問：
  > 「對索引 `i` 的那個元素來說，**有幾個子陣列包含它？**」
- 然後把所有相關元素的貢獻加總，**一趟掃完 — O(n)**。
- **關鍵公式**（長度為 `n` 的陣列／字串，索引 `i` 的元素）：

<!--CODE-->

**為什麼成立：**
- 一個子陣列由它的 `(left, right)` 決定，條件是 `left <= i <= right`。
- 左端可以落在 `0` 到 `i` 的任一索引 → `i + 1` 種選法。
- 右端可以落在 `i` 到 `n - 1` 的任一索引 → `n - i` 種選法。
- 每一種組合都是一個包含索引 `i` 的相異子陣列，所以乘積就把它們全數算進來了。

**什麼時候用：**
- 「**在所有子字串／子陣列上**計算 X 的總和／個數」，而且每個元素各自獨立貢獻
- 每個元素的貢獻**不**取決於它落在哪個子陣列（例如數母音、加總值、算匹配次數）
- 你想避免真的把 O(n²) 個子陣列全部生出來

---

<!-- ee3f8bc5642a -->
##### **模式：LC 2063 - Vowels of All Substrings**

**題目：** 把 `word` **每一個**子字串裡的母音數量全部加總。

**洞見：** 索引 `i` 上的母音，每有一個包含它的子字串就被算一次，而那個數量剛好是 `(i + 1) * (n - i)`。所以只要對所有母音位置把這個乘積加起來就好。

<!--CODE-->

<!--CODE-->

**執行過程：** `word = "aba"`（n = 3）

<!--CODE-->

---

<!-- e3f8f3b72912 -->
##### **一般化模式：逐元素貢獻**

<!--CODE-->

> **注意：** 只要元素的貢獻**與子陣列邊界無關**（純計數、純加總），這招就成立。如果貢獻取決於該元素是不是子陣列裡的最小／最大值，就要改用**單調堆疊版的「Sum of Subarray Minimums」（LC 907）**，那個是用 `(左跨度) * (右跨度)` 來算每個元素的範圍。

---

<!-- 0ef5ad847e93 -->
#### **索引貢獻計數 — 常見錯誤與訣竅**

**🚫 常見錯誤：**

1. **邊界計數差一**
<!--CODE-->

2. **整數溢位**
<!--CODE-->

3. **把「包含索引 i」和「從 i 開始／在 i 結束」搞混**
   - *包含* i 的子陣列：`(i+1) * (n-i)`
   - *從* i *開始*的子陣列：`n - i`
   - *在* i *結束*的子陣列：`i + 1`

**💡 面試技巧：**
- 看到 **「在所有子字串／子陣列上」** 加上一個各自獨立的逐元素值 → 就是貢獻計數。
- 這樣講出來：「與其跑 O(n²) 個子陣列，我改成算每個元素被幾個子陣列包含——`(i+1)*(n-i)`——再加總。」
- 一開始就主動提用 `long` 防溢位。

---

<!-- 0acb131ce16b -->
#### **索引貢獻計數 — 相關 LeetCode 題目**

| 題目 | LC# | Difficulty | 貢獻的算法 |
|---------|-----|------------|-------------------|
| **Vowels of All Substrings** | **2063** | **Medium** | 每個母音貢獻 `(i+1)*(n-i)` |
| Sum of All Subarray Minimums | 907 | Medium | 單調堆疊跨度：`left * right` |
| Sum of Subarray Ranges | 2104 | Medium | 每個元素的（最大貢獻 − 最小貢獻）加總 |
| Sum of Total Strength of Wizards | 2281 | Hard | 貢獻＋前綴和的前綴和 |
| Number of Substrings Containing All Three Characters | 1358 | Medium | 對每個右端算合法的左邊界數量 |

---

**重點整理：**
- ✅ 每個索引 `i` 被 `(i + 1) * (n - i)` 個子陣列包含
- ✅ 把「所有子陣列上」的 O(n²) 加總變成一趟 O(n)
- ✅ 前提是逐元素的貢獻與子陣列邊界無關
- ✅ 乘積要小心用 `long` 防溢位
- ✅ 貢獻取決於最小／最大值時 → 改用單調堆疊的跨度計數（LC 907）

---

<!-- 1f5908a53836 -->
#### 1-2-4) 反向寫入指標（原地合併／原地覆寫） ⭐⭐⭐⭐⭐

**核心想法：** 當你必須把結果寫進**同一個還在讀的陣列**時，往前寫的指標會蓋掉還沒讀到的資料。如果目的地**尾端有空位**，就**倒著走**——寫入指標永遠在兩個讀取指標之上或之前，所以不會有東西在被用掉之前就被覆蓋。

**模式：**

<!--CODE-->

**為什麼倒著寫是安全的：** `write >= p1` 恆成立，因為 `write - p1 = （src 裡還沒合併的元素數）>= 0`。所以被寫的那一格，要嘛是已經用掉的空間，要嘛是尾端的填充區。

**什麼時候用：**
- 把一個已排序陣列合併進另一個尾端有空位的已排序陣列（LC 88）
- 任何「原地產生輸出，且輸出不會比輸入長」的改寫（同一個想法的正向版本，就是原地過濾的 `slow`／`fast`，見 [2_pointers.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md)）

---

<!-- e24fb77c8746 -->
##### **模式：LC 88 - Merge Sorted Array**

**題目：** `nums1` 的長度是 `m + n`（前 `m` 個是真資料，後 `n` 個是填充的 0）。把 `nums2` **原地**合併進去並保持排序。

**洞見：** 正向合併會蓋掉 `nums1` 還沒讀到的值。改從**最大的**元素往下合併。

<!--CODE-->

<!--CODE-->

**視覺化過程** — `nums1 = [1,2,3,0,0,0], m = 3`、`nums2 = [2,5,6], n = 3`

<!--CODE-->

**常見錯誤：**
- ❌ 正向合併（`write = 0`）——會毀掉 `nums1` 裡還沒讀的值
- ❌ 用 `while (p1 >= 0 || p2 >= 0)` 迴圈，卻忘了在裡面加 `p1 >= 0` 的守衛
- ❌ 最後又去複製 `nums1` 剩下的尾巴——多此一舉，它本來就已經對了
- ❌ `>=` 和 `>` 混用——這題兩者皆可（整數不用管穩定性）

---

<!-- 9eb6e6a471e9 -->
#### 1-2-5) 陣列＋雜湊索引表（用「和最後一個交換」做 O(1) 刪除） ⭐⭐⭐⭐⭐

**核心想法：** 從陣列中間刪東西之所以是 O(n)，*純粹是因為要搬移*。如果**順序不重要**，刪除就能做到 O(1)：

<!--CODE-->

陣列提供 **O(1) 隨機存取**（`getRandom` 需要），雜湊表提供 **O(1) 查詢**。兩者合起來，既贏過單純的 `HashSet`（沒辦法用索引隨機挑），也贏過單純的陣列（查詢／刪除是 O(n)）。

**什麼時候用：**
- 要求 `insert`／`remove`／`getRandom` 全部 O(1) 的設計題
- 任何順序無關、又要按值刪除任意元素的集合
- free-list／插槽重用這類記帳工作

---

<!-- 1e536aad4b9b -->
##### **模式：LC 380 - Insert Delete GetRandom O(1)**

<!--CODE-->

<!--CODE-->

**為什麼陣列必須保持緊密：** `getRandom` 要的是均勻抽樣，而 `arr[rand(0, size)]` 只有在**沒有空洞**時才均勻。用墓碑標記（把格子標成失效）會破壞均勻性——所以才要跟最後一個交換。

**常見錯誤：**
- ❌ Java 在 `List<Integer>` 上呼叫 `list.remove(value)`——多載會混淆；改用 `remove(int index)` 或 `remove(Integer.valueOf(v))`
- ❌ **先**刪掉 map 的項目，才去把搬過來的元素重新指向（當被刪的*就是*最後一個元素時會出錯）
- ❌ 用 `vals.remove(i)`（從中間移除）而不是彈掉尾巴 → 又變回 O(n)
- ❌ 想用 `HashSet` ＋ `iterator.next()` 做 `getRandom` → O(n)，而且不均勻

---

<!-- e54dd5426df4 -->
#### 1-2-6) 有界候選列舉（可能的答案就那幾個）

**核心想法：** 有些「把全部弄成一樣／把全部弄成合法」的題目，看起來要在所有數值上搜尋，但限制條件其實把答案鎖死在一個**極小的候選集合**裡——通常是從索引 `0` 推出來的。把那 1-2 個候選列出來，每個用 O(n) 驗一次就好。

**辨識方式：** *「若存在一個合法答案 `x`，那位置 0 上一定已經有 `x`」* → 候選 = `{tops[0], bottoms[0]}`，總工作量就是 `2 * O(n)`。

---

<!-- 4d642b294aac -->
##### **模式：LC 1007 - Minimum Domino Rotations For Equal Row**

**題目：** 給骨牌的 `tops[]`／`bottoms[]`，求最少旋轉幾次能讓某一整排的值都一樣（否則回傳 `-1`）。

**洞見：** 目標值一定會出現在第 `0` 張骨牌上——否則第 0 張永遠沒辦法顯示它。所以只要檢查 `tops[0]` 和 `bottoms[0]`。

<!--CODE-->

<!--CODE-->

**常見錯誤：**
- ❌ 把 `1..6` 所有值都跑一遍——這題可行，但錯過了能遷移的洞見（而且值域一大就爛掉）
- ❌ 在同一個 `if` 裡同時累加 `rotTop` 和 `rotBottom`（必須寫成 `if / elif`：兩面都是 `x` 的骨牌根本不用轉）
- ❌ 忘了 `tops[0] == bottoms[0]` 的短路 → 同一個候選驗了兩次

---

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

<!-- 32f1c1cfcf14 -->
## 3) 題目詳解

十三道題目放在 **[array_examples.md](./array_examples.md)**：

| 分組 | 題目 |
|---|---|
| [In-place rewriting & index tricks](./array_examples.md#in-place-rewriting--index-tricks) | LC 41, 287, 189, 238, 670 |
| [Scanning & running state](./array_examples.md#scanning--running-state) | LC 121, 1567, 334, 849 |
| [Counting, bookings & simulation](./array_examples.md#counting-bookings--simulation) | LC 1109, 1375, 1041, 406, 251 |
