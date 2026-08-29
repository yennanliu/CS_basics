<!-- 2406d7d5fe75 -->
# Python 插入、切片與索引運算

> **範圍** — Python 解法「邏輯明明對了卻答錯」的兩大元凶：插入到底落在哪一格，以及一段索引區間指的是「個數」還是「距離」。
> **另見**：[python_trick.md](./python_trick.md) — 這裡用到的語言慣用寫法；[python_trick_stdlib.md](./python_trick_stdlib.md) — 用 `bisect.insort` 邊插入邊維持排序；[prefix_sum.md](./prefix_sum.md) — 本檔前綴和一節背後的技巧；[array.md](./array.md) — 同樣的操作，改用陣列演算法而非 Python 呼叫的角度來看。

<!-- 28e614d75a67 -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

<!-- fc6183b5c74a -->
## 總覽

本檔從 [python_trick.md](./python_trick.md) 拆出來。原檔最長的兩節 —— 插入串列，以及索引距離 vs 元素個數 —— 其實是同一件事的兩端：`i` 和 `j` 到底代表什麼，以及它們後面的東西會怎麼被搬動。

<!-- 03f8c7cde4bf -->
### 關鍵性質
- **核心想法**：`list.insert(i, x)` 會把 `x` 放**在**索引 `i`，其餘往右移 —— 這是 O(n)，不是 O(1)；`x[i:j]` 不含 `j`，所以要取 `j - i + 1` 個元素得寫 `x[i:j+1]`
- **什麼時候用**：答案剛好差 1，或某個元素落在你預期位置的隔壁一格時

<!-- 0d8196600214 -->
## 插入與搬移元素

<!-- 4f5a2b624f3d -->
### 原地插入串列 ⭐⭐⭐⭐⭐

<!--CODE-->

<!-- 69e6bb6abd34 -->
#### **核心想法 —— 原地插入串列**

<!--CODE-->

<!--CODE-->

**關鍵性質（LC 406 為什麼成立）：** 執行 `insert(k, v)` 之後，值 `v` 就**剛好落在索引 `k`** —— 所以當你必須把元素*擺到指定位置*、而不只是接在尾巴時，`insert` 就是那個工具。

<!-- d786b67e671d -->
#### **邊界情況／行為**

<!--CODE-->

**❌ 常見錯誤**

<!--CODE-->

<!-- d5c3d4c792a8 -->
#### **`insert` vs `append` vs `extend` vs `+`**

| 操作 | 效果 | 時間 | 回傳 |
|----|--------|------|---------|
| `arr.append(v)` | 在尾端加入一個元素 | `O(1)` 攤銷 | `None`（原地） |
| `arr.insert(i, v)` | 在索引 `i` 加入一個元素，其餘右移 | `O(n)` | `None`（原地） |
| `arr.insert(0, v)` | 加在最前面（最差的搬移量） | `O(n)` | `None`（原地） |
| `arr.extend([a,b])` | 在尾端加入多個元素 | `O(k)` | `None`（原地） |
| `arr = arr + [v]` | 建立一個新串列 | `O(n)` | 新串列 |
| `arr[i:i] = [a,b]` | 用切片在索引 `i` 插入多個元素 | `O(n+k)` | `None`（原地） |
| `deque.appendleft(v)` | 加在最前面 | **`O(1)`** | `None`（原地） |
| `bisect.insort(arr, v)` | 插入並維持陣列有序 | `O(n)`（搜尋 `O(log n)`） | `None`（原地） |

<!--CODE-->

> **效能提醒**：`insert` 會把 `idx` 之後的每個元素都往後搬，所以是 `O(n)`。
> 放在迴圈裡呼叫就變成 `O(n²)`。以 LC 那種 `n <= 2000` 的限制（LC 406）還可以接受，
> 但如果你只會插在最前面，請改用
> `collections.deque.appendleft()`（`O(1)`）—— 見 [1-32) deque](./python_trick_stdlib.md#deque-double-ended-queue)。

<!-- 30dc3fc6f474 -->
#### **使用情境 1 —— LC 406 Queue Reconstruction by Height ⭐⭐⭐⭐⭐**

`people[i] = [h, k]` 表示身高 `h`，前面剛好有 `k` 個人**身高大於等於**他。

**關鍵洞見**：先照身高**遞減**排序，同高再照 `k` **遞增**排；接著把每個人插到索引 `k`。
因為已經放好的人都**比他高或一樣高**，「索引 `k`」字面上就等於「前面有 `k` 個不比他矮的人」——
而之後再插進來的**更矮**的人，永遠不會破壞先前那些人的計數（矮的人不算進 `k`）。

<!--CODE-->

**視覺追蹤** —— `people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]`

<!--CODE-->

**兩個排序鍵為什麼都不能少**

<!--CODE-->

> 相關：排序鍵本身請看
> [1-11'') 多鍵 tuple 排序](./python_trick.md#multi-key-tuple-sort-keylambda-x-x0-x1-)。

<!-- a80c3a040a68 -->
#### **使用情境 2 —— 插入並維持陣列有序（`bisect.insort`）**

別自己手刻「先找位置再插入」—— `bisect` 已經幫你把搜尋做掉了。

<!--CODE-->

<!--CODE-->

> 見 [1-27) bisect](./python_trick_stdlib.md#bisect_left-and-bisect_right)。

<!-- 67b8e71a2eff -->
#### **使用情境 3 —— 插在最前面（反向建結果）**

當你反著走一條路徑或鏈結串列、卻要正向輸出時，很常見。

<!--CODE-->

<!-- f8719d9b147f -->
#### **使用情境 4 —— LC 57 Insert Interval（插進依起點排序的清單）**

<!--CODE-->

<!-- a3390514e0f0 -->
#### **類似的 LC 題目 —— 原地插入串列**

| LC # | 題目 | `insert` 怎麼用 |
|------|---------|----------------------|
| 406 | Queue Reconstruction by Height | 身高遞減排序後 `res.insert(k, person)` ⭐ |
| 57 | Insert Interval | 插到排序位置，再合併 |
| 315 | Count of Smaller Numbers After Self | `bisect` 找位置 + `insert` 維持有序 |
| 220 | Contains Duplicate III | 有序視窗（`SortedList.add` 就是 insert） |
| 148 | Sort List | 串列上的插入排序變形 |
| 147 | Insertion Sort List | 同樣想法的鏈結串列版 |
| 146 | LRU Cache | 用 `remove` + `append` 把元素移到尾端（見 [1-21](#moving-an-element-to-the-rightmost--leftmost-position)） |
| 155 | Min Stack | 只在尾端 `append` / `pop` —— `O(1)`，不需要 insert |
| 622 | Design Circular Queue | 為什麼要避開 `insert(0, ..)` → 改用 `deque` |

<!-- c2fdfaa6120e -->
### 原地加到串列最前面

<!--CODE-->

<!-- f4cba64d60ed -->
### 把元素搬到最右／最左

<!--CODE-->

<!-- 750db759ecb0 -->
### 串列 `extend`

<!--CODE-->

<!-- 3ecb19dfb503 -->
## 切片

<!-- 175c5c0f5843 -->
### 陣列切片（子陣列／子字串）


**語法**：`arr[start:end]` —— **end 不含在內**，所以切片涵蓋的索引是 `[start, end-1]`。

<!--CODE-->

| 寫法 | 意思 |
|-----------|---------|
| `arr[i:j+1]` | 索引 `i` 到 `j`，含兩端 |
| `arr[:j+1]` | 索引 `0` 到 `j`，含兩端 |
| `arr[i:]` | 索引 `i` 到結尾 |
| `arr[:]` | 完整淺複製 |
| `arr[::-1]` | 反轉 |

<!-- 6f4bc51bfdd0 -->
#### `x[i:j+1]` vs `x[i:j]` —— 要不要含索引 `j`？

<!--CODE-->

**規則**：
<!--CODE-->

<!-- 4996a69af1df -->
#### 具體例子 —— LC 105（用前序 + 中序建二元樹）

<!--CODE-->

<!-- cfbaf91fa33a -->
### 列舉「所有」子字串 —— 內層 `j` 迴圈為什麼要 `+1` ⭐⭐⭐⭐⭐


<!--CODE-->

<!-- 594cebf3346b -->
#### **核心想法 —— 列舉所有子字串**

**這裡的 `j` 不是索引，而是切片的邊界（一個「切點」）。**

- **索引**指向某個字元 → 合法範圍 `0 … n-1`（`n` 個值）
- **邊界**指向字元之間的縫 → 合法範圍 `0 … n`（`n+1` 個值）

`s[i:j]` 是由兩個*邊界*定義的，所以 `j` 必須能取到 `n`
（最後一個字元「之後」的那個切點）。這正是為什麼迴圈寫成
`range(i+1, len(s)+1)` 而不是 `range(i+1, len(s))`。

<!--CODE-->

<!-- 39538ab1f288 -->
#### **說明 —— 兩種等價寫法**

<!--CODE-->

| 寫法 | `j` 代表 | 迴圈 | 切片 | 子字串長度 |
|------|-----------|------|-------|------------------|
| **A** | 邊界／切點 | `range(i+1, n+1)` | `s[i:j]` | `j - i` |
| **B** | 最後一個字元的索引 | `range(i, n)` | `s[i:j+1]` | `j - i + 1` |

> **規則**：`+1` 只會出現**剛好一次** —— 要嘛在 `range()`（寫法 A），
> 要嘛在切片裡（寫法 B）。**兩邊都放**或**兩邊都不放**就是 bug。

**三個經典錯誤**

<!--CODE-->

**總數為什麼是 `n*(n+1)/2`** —— 快速檢查迴圈有沒有寫對：

<!--CODE-->

**子陣列也是同一條規則**（邏輯完全一樣，只是把字串換成串列）：

<!--CODE-->

> **相關**：這跟 [1-51) 陣列切片](#array-slicing-subarray--substring)
> 的「不含右端」規則（`x[i:j]` 不含 `j`）以及 [1-52) 索引距離 vs 元素個數](#index-distance-vs-element-count-off-by-one) 是同一回事。
> 但要小心：子字串的 **DP** 通常用 `dp[i][j]`，這時 `j` 是**索引**
> （寫法 B，`s[i:j+1]`）—— 同一份解法裡不要混用兩種慣例。

<!-- 07b6f40bf9bd -->
#### **類似的 LC 題目 —— 列舉所有子字串**

| LC # | 題目 | `j` 怎麼用 |
|------|---------|-----------------|
| 647 | Palindromic Substrings | 邊界 `s[i:j]`（暴力）／索引 `dp[i][j]`（DP） |
| 5 | Longest Palindromic Substring | 邊界 —— 依長度追蹤最佳的 `s[i:j]` |
| 3 | Longest Substring Without Repeating Chars | 滑動視窗：`right` 的行為就像邊界 |
| 76 | Minimum Window Substring | 視窗 `s[left:right+1]` → 索引寫法 |
| 131 | Palindrome Partitioning | `for j in range(i+1, n+1): s[i:j]`，然後從 `j` 繼續回溯 |
| 139 | Word Break | `for j in range(i+1, n+1): s[i:j] in wordDict` |
| 560 | Subarray Sum Equals K | 子陣列 `nums[i:j]`，邊界寫法（前綴和用的是同一批切點） |
| 53 | Maximum Subarray | 列舉子陣列（暴力）／Kadane |
| 209 | Minimum Size Subarray Sum | 視窗長度 = `right - left + 1` → 索引寫法 |
| 516 | Longest Palindromic Subsequence | DP `dp[i][j]`，`j` 是索引（寫法 B） |
| 1143 | Longest Common Subsequence | DP `dp[i][j]`，`i`／`j` 是**長度**（0 … n）—— 接近邊界的概念 |

<!-- 6a11ba6b86d5 -->
## 索引運算

<!-- b2f4d8726f6c -->
### 索引距離 vs 元素個數（差一錯誤）


**核心規則：** 兩個索引之間的距離 ≠ 它們之間的元素個數。

<!--CODE-->

| 寫法 | 值 | 意思 |
|-----------|-------|---------|
| `last - first` | `2` | 距離／跨度（柵欄的縫） |
| `last - first + 1` | `3` | 元素個數（柵欄的柱子） |

**視覺化 —— 「柵欄柱子」比喻：**
<!--CODE-->

**常見的 LC 應用：**

<!--CODE-->

**快速判斷法則：**
<!--CODE-->
<!--CODE-->

<!-- 2132c30a27bf -->
### 建立前綴和陣列


累積和的慣用寫法：先算好累計總和，之後任何區間和都變成 O(1)。
完整內容見 [`prefix_sum.md`](./prefix_sum.md)。

<!--CODE-->

**要背起來的那一行：**
<!--CODE-->

**追蹤（注意結果會比 `cnt` 多一個元素）：**
<!--CODE-->

**為什麼寫在索引 `i + 1`（而不是 `i`）？** `prefix` 的大小是 `n+1`，`prefix[k]` 代表
「前 `k` 個元素的和」。寫進 `prefix[i+1]` 才能保住開頭的 `prefix[0]=0` ——
這樣 `sum(l, r) = prefix[r+1] - prefix[l]` 就不必處理任何邊界情況。

**一行版替代方案** —— 用 `itertools.accumulate` 搭配 `initial=0`：
<!--CODE-->

**區間和查詢（建表 O(n) 之後每次 O(1)）：**
<!--CODE-->
