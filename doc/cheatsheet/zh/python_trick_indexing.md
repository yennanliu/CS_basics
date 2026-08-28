# Python 插入、切片與索引運算

> **範圍** — Python 解法「邏輯明明對了卻答錯」的兩大元凶：插入到底落在哪一格，以及一段索引區間指的是「個數」還是「距離」。
> **另見**：[python_trick.md](./python_trick.md) — 這裡用到的語言慣用寫法；[python_trick_stdlib.md](./python_trick_stdlib.md) — 用 `bisect.insort` 邊插入邊維持排序；[prefix_sum.md](./prefix_sum.md) — 本檔前綴和一節背後的技巧；[array.md](./array.md) — 同樣的操作，改用陣列演算法而非 Python 呼叫的角度來看。

## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## 總覽

本檔從 [python_trick.md](./python_trick.md) 拆出來。原檔最長的兩節 —— 插入串列，以及索引距離 vs 元素個數 —— 其實是同一件事的兩端：`i` 和 `j` 到底代表什麼，以及它們後面的東西會怎麼被搬動。

### 關鍵性質
- **核心想法**：`list.insert(i, x)` 會把 `x` 放**在**索引 `i`，其餘往右移 —— 這是 O(n)，不是 O(1)；`x[i:j]` 不含 `j`，所以要取 `j - i + 1` 個元素得寫 `x[i:j+1]`
- **什麼時候用**：答案剛好差 1，或某個元素落在你預期位置的隔壁一格時


## 插入與搬移元素

### 原地插入串列 ⭐⭐⭐⭐⭐

```text

# syntax : 
# arr.insert(<index>,<value>)
In [12]: x = [1,2,3]
    ...: x.insert(2,77)

In [13]: x
Out[13]: [1, 2, 77, 3]
```

#### **核心想法 —— 原地插入串列**

```text
arr.insert(idx, val)
   -> val is placed AT index `idx`  (i.e. inserted BEFORE the old arr[idx])
   -> everything from old arr[idx] onward SHIFTS RIGHT by 1
   -> mutates IN PLACE and returns None   (NOT a new list!)
   -> time = O(n)  (because of the shifting), space = O(1)
```

```text
x = [1, 2, 3]        x.insert(2, 77)

 idx:  0    1    2                idx:  0    1    2     3
     [ 1 ][ 2 ][ 3 ]     ───►         [ 1 ][ 2 ][ 77 ][ 3 ]
                ^                                ^     ^
           insert HERE                       new val   old x[2] pushed right
```

**關鍵性質（LC 406 為什麼成立）：** 執行 `insert(k, v)` 之後，值 `v` 就**剛好落在索引 `k`** —— 所以當你必須把元素*擺到指定位置*、而不只是接在尾巴時，`insert` 就是那個工具。

#### **邊界情況／行為**

```python
#----------------------------
# 1) index == len(arr)  -> same as append
#----------------------------
In [1]: x = [1,2,3]; x.insert(3, 99); x
Out[1]: [1, 2, 3, 99]

#----------------------------
# 2) index > len(arr)   -> NO IndexError, clamped to the end (append)
#----------------------------
In [2]: x = [1,2,3]; x.insert(100, 99); x
Out[2]: [1, 2, 3, 99]

#----------------------------
# 3) index == 0         -> insert at FRONT (see 1-6')
#----------------------------
In [3]: x = [1,2,3]; x.insert(0, 0); x
Out[3]: [0, 1, 2, 3]

#----------------------------
# 4) NEGATIVE index     -> counts from the END, inserts BEFORE that element
#----------------------------
In [4]: x = [1,2,3]; x.insert(-1, 99); x
Out[4]: [1, 2, 99, 3]        # before the LAST element, NOT at the end

In [5]: x = [1,2,3]; x.insert(-100, 99); x
Out[5]: [99, 1, 2, 3]        # clamped to the front

#----------------------------
# 5) returns None (IN-PLACE!) -> classic bug
#----------------------------
In [6]: x = [1,2,3]
In [7]: y = x.insert(1, 9)   # ❌ y is None
In [8]: print(y, x)
None [1, 9, 2, 3]
```

**❌ 常見錯誤**

```python
arr = arr.insert(0, x)        # ❌ arr becomes None  (insert returns None)
arr.insert(0, x)              # ✅ just call it

# ❌ mutating the list you are iterating -> infinite loop / skipped items
for v in arr:
    arr.insert(0, v)          # ❌ never do this
res = []                      # ✅ build a NEW list instead
for v in arr:
    res.insert(pos, v)
```

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

```python
# bulk insert via slice assignment (insert MANY at once)
In [9]: x = [1, 2, 5]
In [10]: x[2:2] = [3, 4]      # insert [3,4] AT index 2, nothing removed
In [11]: x
Out[11]: [1, 2, 3, 4, 5]
```

> **效能提醒**：`insert` 會把 `idx` 之後的每個元素都往後搬，所以是 `O(n)`。
> 放在迴圈裡呼叫就變成 `O(n²)`。以 LC 那種 `n <= 2000` 的限制（LC 406）還可以接受，
> 但如果你只會插在最前面，請改用
> `collections.deque.appendleft()`（`O(1)`）—— 見 [1-32) deque](./python_trick_stdlib.md#deque-double-ended-queue)。

#### **使用情境 1 —— LC 406 Queue Reconstruction by Height ⭐⭐⭐⭐⭐**

`people[i] = [h, k]` 表示身高 `h`，前面剛好有 `k` 個人**身高大於等於**他。

**關鍵洞見**：先照身高**遞減**排序，同高再照 `k` **遞增**排；接著把每個人插到索引 `k`。
因為已經放好的人都**比他高或一樣高**，「索引 `k`」字面上就等於「前面有 `k` 個不比他矮的人」——
而之後再插進來的**更矮**的人，永遠不會破壞先前那些人的計數（矮的人不算進 `k`）。

```python
# LC 406 Queue Reconstruction by Height
# time = O(n^2)   (n inserts × O(n) shift)
# space = O(n)
class Solution(object):
    def reconstructQueue(self, people):
        # sort: height DESC (-x[0]), then k ASC (x[1])
        people.sort(key=lambda x: (-x[0], x[1]))

        # py insert syntax:
        # python_trick.html#1-6-insert-into-array-in-place
        # arr.insert(<index>, <value>)
        res = []
        for p in people:
            res.insert(p[1], p)   # place person AT index k
        return res
```

**視覺追蹤** —— `people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]`

```text
after sort (h DESC, k ASC):
  [[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]]

step | person | insert(k, p)  | res
-----+--------+---------------+------------------------------------------
  1  | [7,0]  | insert(0, ..) | [[7,0]]
  2  | [7,1]  | insert(1, ..) | [[7,0], [7,1]]
  3  | [6,1]  | insert(1, ..) | [[7,0], [6,1], [7,1]]
  4  | [5,0]  | insert(0, ..) | [[5,0], [7,0], [6,1], [7,1]]
  5  | [5,2]  | insert(2, ..) | [[5,0], [7,0], [5,2], [6,1], [7,1]]
  6  | [4,4]  | insert(4, ..) | [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
                                                              ^ landed at idx 4
```

**兩個排序鍵為什麼都不能少**

```python
people.sort(key=lambda x: (-x[0], x[1]))
#                          ^^^^^  ^^^^
#  -x[0] : TALLEST first  -> everyone already in `res` is >= current height,
#                            so "index k" == "k taller-or-equal in front"
#   x[1] : k ASC on ties  -> among SAME height, smaller k inserted first,
#                            otherwise [7,1] before [7,0] would misplace [7,0]
```

> 相關：排序鍵本身請看
> [1-11'') 多鍵 tuple 排序](./python_trick.md#multi-key-tuple-sort-keylambda-x-x0-x1-)。

#### **使用情境 2 —— 插入並維持陣列有序（`bisect.insort`）**

別自己手刻「先找位置再插入」—— `bisect` 已經幫你把搜尋做掉了。

```python
import bisect

# manual (2 steps)
idx = bisect.bisect_left(a, val)
a.insert(idx, val)

# one-liner (identical result)
bisect.insort_left(a, val)
```

```python
# LC 315 Count of Smaller Numbers After Self — scan right → left,
# keep a sorted list of seen values; the insert position IS the answer
def countSmaller(nums):
    seen, res = [], []
    for n in reversed(nums):
        idx = bisect.bisect_left(seen, n)   # how many seen values are < n
        res.append(idx)
        seen.insert(idx, n)                 # keep `seen` sorted
    return res[::-1]
```

> 見 [1-27) bisect](./python_trick_stdlib.md#bisect_left-and-bisect_right)。

#### **使用情境 3 —— 插在最前面（反向建結果）**

當你反著走一條路徑或鏈結串列、卻要正向輸出時，很常見。

```python
# BFS/DFS: walk parent pointers backwards, insert(0, ..) to get the path in order
path = []
while node:
    path.insert(0, node.val)   # O(n) each -> O(n^2) total
    node = parent[node]

# ✅ FASTER equivalents
path.append(node.val); ...; path = path[::-1]      # append then reverse — O(n)
from collections import deque
path = deque(); path.appendleft(node.val)          # O(1) per push
```

#### **使用情境 4 —— LC 57 Insert Interval（插進依起點排序的清單）**

```python
# find where the new interval starts, then insert & merge
import bisect

def insert(intervals, newInterval):
    idx = bisect.bisect_left(intervals, newInterval)
    intervals.insert(idx, newInterval)      # now list is still sorted by start
    # ... then do the standard merge pass
    res = []
    for it in intervals:
        if res and res[-1][1] >= it[0]:
            res[-1][1] = max(res[-1][1], it[1])
        else:
            res.append(it)
    return res
```

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

### 原地加到串列最前面

```python
In [1]: x = [1,2,3]

In [2]: x
Out[2]: [1, 2, 3]

In [3]: x.insert(0,0)

In [4]: x
Out[4]: [0, 1, 2, 3]

In [5]: x.insert(0,-1)

In [6]: x
Out[6]: [-1, 0, 1, 2, 3]
```

### 把元素搬到最右／最左

```text
# LC 146 LRU Cache
In [18]: x
Out[18]: [1, 3, 2]

In [19]: x = [1,2,3]

# NOTE this !!!!
# LC 146
In [20]: x.remove(2)
#x
#[1,2]

In [21]: x.append(2)

In [22]: x
Out[22]: [1, 3, 2]

In [23]:

In [23]: x.remove(1)

In [24]: x.append(1)

In [25]: x
Out[25]: [3, 2, 1]
```

### 串列 `extend`

```python
# LC 969. Pancake Sorting

In [10]: x = [1,2,3]

In [11]: x.extend([4])

In [12]: x
Out[12]: [1, 2, 3, 4]

In [13]: x = [1,2,3]

In [14]: x = x + [4]

In [15]: x
Out[15]: [1, 2, 3, 4]
```


## 切片

### 陣列切片（子陣列／子字串）


**語法**：`arr[start:end]` —— **end 不含在內**，所以切片涵蓋的索引是 `[start, end-1]`。

```python
arr = [0, 1, 2, 3, 4]

arr[1:4]     # [1, 2, 3]  → indices 1, 2, 3  (end=4 is excluded)
arr[i:j+1]   # indices i .. j  (inclusive on both ends)
arr[:3]      # [0, 1, 2]  → from start up to index 2
arr[2:]      # [2, 3, 4]  → index 2 to end
arr[:]       # full copy
arr[::-1]    # reversed copy

# Common pattern: get subarray from index i to j (inclusive)
sub = arr[i : j + 1]

# String slicing works the same way
s = "abcde"
s[1:4]       # "bcd"  → indices 1, 2, 3
s[i:j+1]     # chars from i to j (inclusive)
```

| 寫法 | 意思 |
|-----------|---------|
| `arr[i:j+1]` | 索引 `i` 到 `j`，含兩端 |
| `arr[:j+1]` | 索引 `0` 到 `j`，含兩端 |
| `arr[i:]` | 索引 `i` 到結尾 |
| `arr[:]` | 完整淺複製 |
| `arr[::-1]` | 反轉 |

#### `x[i:j+1]` vs `x[i:j]` —— 要不要含索引 `j`？

```python
x = [1, 3, 2]
#    0  1  2   ← indices

x[0:2]   # [1, 3]  → j=2 is NOT included  (indices 0, 1)
x[0:3]   # [1, 3, 2] → j=3 is NOT included, but covers all (indices 0, 1, 2)

# To include index j, use j+1 as the stop:
x[0:1+1]  # [1, 3]  → includes index j=1
x[0:2+1]  # [1, 3, 2] → includes index j=2
```

**規則**：
```text
x[i:j]   → j index is NOT included  (standard Python — end is exclusive)
x[i:j+1] → j index IS included      (add +1 to make end inclusive)
```

#### 具體例子 —— LC 105（用前序 + 中序建二元樹）

```python
# preorder = [3, 9, 20, 15, 7]
# inorder  = [9, 3, 15, 20,  7]
#
# root = preorder[0] = 3
# idx  = inorder.index(3) = 1   ← root sits at index 1 in inorder
#
# inorder layout:
#   index:   0   1   2   3   4
#   value:  [9,  3, 15, 20,  7]
#             ^   ^
#           left root  right subtree starts at idx+1=2
#
# Left subtree of inorder  = elements BEFORE root  = inorder[:idx]
# Right subtree of inorder = elements AFTER  root  = inorder[idx+1:]

# ✅ CORRECT: inorder[:idx]   → [9]           (excludes root at idx=1)
# ❌ WRONG:   inorder[:idx+1] → [9, 3]        (includes root — builds wrong tree)

root.left = self.buildTree(
    preorder[1 : 1 + idx],   # left subtree has `idx` nodes
    inorder[:idx]             # everything LEFT of root (exclusive stop = idx)
)
root.right = self.buildTree(
    preorder[1 + idx:],       # remaining nodes after left subtree
    inorder[idx + 1:]         # everything RIGHT of root (skip root at idx)
)

# Why inorder[:idx] and NOT inorder[:idx+1]?
#   Python slice stop is EXCLUSIVE, so inorder[:idx] gives indices 0..idx-1,
#   which is exactly the elements to the LEFT of root (root at idx is excluded).
#   Using inorder[:idx+1] would mistakenly include the root itself in the left subtree.
```

### 列舉「所有」子字串 —— 內層 `j` 迴圈為什麼要 `+1` ⭐⭐⭐⭐⭐


```python
# LC 647 Palindromic Substrings (brute force)
count = 0
# NOTE: since i from 0 to len(s) - 1,
#  -> so for j we need to "+1" then can go through all elements in str
for i in range(len(s)):
    # Note : for j we need to "+1"
    for j in range(i+1, len(s)+1):
        if s[i:j] == s[i:j][::-1]:
            count += 1
```

#### **核心想法 —— 列舉所有子字串**

**這裡的 `j` 不是索引，而是切片的邊界（一個「切點」）。**

- **索引**指向某個字元 → 合法範圍 `0 … n-1`（`n` 個值）
- **邊界**指向字元之間的縫 → 合法範圍 `0 … n`（`n+1` 個值）

`s[i:j]` 是由兩個*邊界*定義的，所以 `j` 必須能取到 `n`
（最後一個字元「之後」的那個切點）。這正是為什麼迴圈寫成
`range(i+1, len(s)+1)` 而不是 `range(i+1, len(s))`。

```text
s = "abc"

index:        0     1     2
           +--a--+--b--+--c--+
boundary:  0     1     2     3        ← j lives HERE (0 .. n, so n+1 = 4 slots)

s[0:1] = "a"      s[0:3] = "abc"   ← needs j = 3 = len(s)  → stop must be len(s)+1
s[1:3] = "bc"     s[3:3] = ""
```

#### **說明 —— 兩種等價寫法**

```python
n = len(s)

# ── Form A: j as BOUNDARY (slice end, exclusive) ──
for i in range(n):
    for j in range(i+1, n+1):     # +1 on BOTH start and stop
        sub = s[i:j]              # substring s[i .. j-1], length = j - i

# ── Form B: j as INDEX (last char of the substring) ──
for i in range(n):
    for j in range(i, n):         # no +1 anywhere in range()
        sub = s[i:j+1]            # +1 moves INTO the slice, length = j - i + 1
```

| 寫法 | `j` 代表 | 迴圈 | 切片 | 子字串長度 |
|------|-----------|------|-------|------------------|
| **A** | 邊界／切點 | `range(i+1, n+1)` | `s[i:j]` | `j - i` |
| **B** | 最後一個字元的索引 | `range(i, n)` | `s[i:j+1]` | `j - i + 1` |

> **規則**：`+1` 只會出現**剛好一次** —— 要嘛在 `range()`（寫法 A），
> 要嘛在切片裡（寫法 B）。**兩邊都放**或**兩邊都不放**就是 bug。

**三個經典錯誤**

```python
n = len(s)

# ❌ 1) forgot +1 on stop → MISSES every substring ending at the LAST char
for j in range(i+1, n):
    s[i:j]          # for s="abc", i=0 -> only "a","ab"   ("abc" never checked!)

# ❌ 2) forgot +1 on start → produces the EMPTY string s[i:i] = ""
for j in range(i, n+1):
    s[i:j]          # j == i gives "", and "" == ""[::-1] is True → OVER-counts

# ❌ 3) mixed the two forms → out of range / duplicated work
for j in range(i+1, n+1):
    s[i:j+1]        # j+1 can reach n+1 → silently returns the same string again
```

**總數為什麼是 `n*(n+1)/2`** —— 快速檢查迴圈有沒有寫對：

```python
s = "abc"                      # n = 3  ->  3*4/2 = 6 substrings
# i=0: "a", "ab", "abc"        (j = 1,2,3)
# i=1: "b", "bc"               (j = 2,3)
# i=2: "c"                     (j = 3)

n = len(s)
print(sum(1 for i in range(n) for j in range(i+1, n+1)))   # 6  ✅
```

**子陣列也是同一條規則**（邏輯完全一樣，只是把字串換成串列）：

```python
# all contiguous subarrays of nums
for i in range(len(nums)):
    for j in range(i+1, len(nums)+1):
        sub = nums[i:j]        # e.g. sum(sub), max(sub), ...
```

> **相關**：這跟 [1-51) 陣列切片](#array-slicing-subarray--substring)
> 的「不含右端」規則（`x[i:j]` 不含 `j`）以及 [1-52) 索引距離 vs 元素個數](#index-distance-vs-element-count-off-by-one) 是同一回事。
> 但要小心：子字串的 **DP** 通常用 `dp[i][j]`，這時 `j` 是**索引**
> （寫法 B，`s[i:j+1]`）—— 同一份解法裡不要混用兩種慣例。

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

## 索引運算

### 索引距離 vs 元素個數（差一錯誤）


**核心規則：** 兩個索引之間的距離 ≠ 它們之間的元素個數。

```python
a = [1, 2, 3]
#    0  1  2     ← indices

# distance (span between indices, e.g. window width in pixels)
# last_idx - first_idx  =  2 - 0  =  2

# element count (how many items are IN the range [first_idx, last_idx] inclusive)
# last_idx - first_idx + 1  =  2 - 0 + 1  =  3
```

| 寫法 | 值 | 意思 |
|-----------|-------|---------|
| `last - first` | `2` | 距離／跨度（柵欄的縫） |
| `last - first + 1` | `3` | 元素個數（柵欄的柱子） |

**視覺化 —— 「柵欄柱子」比喻：**
```text
index:   0    1    2
         |    |    |       ← 3 posts  (= last - first + 1 = 3)
         +----+----+       ← 2 gaps   (= last - first     = 2)
```

**常見的 LC 應用：**

```python
# 1. Sliding window length
#    window covers indices [l, r] inclusive
window_len = r - l + 1      # NOT r - l

# 2. Substring / subarray length
s = "abcde"
# substring s[i:j] in Python has j - i characters (Python end is exclusive)
# substring from index i to j INCLUSIVE has j - i + 1 characters
length = j - i + 1

# 3. Array midpoint (binary search)
mid = (lo + hi) // 2        # mid is an index, not a count

# 4. Difference array / prefix sum length
#    to cover indices 0..n-1, need n+1 slots in prefix sum array
prefix = [0] * (n + 1)

# 5. Range check: does [l, r] contain at least k elements?
if r - l + 1 >= k:          # NOT r - l >= k
    ...
```

**快速判斷法則：**
```text
result = right - left       → use when you need a GAP / DISTANCE
result = right - left + 1  → use when you need an ELEMENT COUNT
```
```python
#-------------------------------
# Sliding window template
#-------------------------------
def sliding_window(s, k):
    left = 0
    window = {}
    result = 0
    for right, ch in enumerate(s):
        window[ch] = window.get(ch, 0) + 1
        while len(window) > k:       # shrink condition
            lch = s[left]
            window[lch] -= 1
            if window[lch] == 0:
                del window[lch]
            left += 1
        result = max(result, right - left + 1)
    return result

#-------------------------------
# Binary search template
#-------------------------------
def binary_search(nums, target):
    lo, hi = 0, len(nums) - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1
    return -1

# Binary search on answer (find leftmost valid value)
def binary_search_left(lo, hi, feasible):
    while lo < hi:
        mid = (lo + hi) // 2
        if feasible(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo

#-------------------------------
# DFS template (iterative)
#-------------------------------
def dfs_iterative(graph, start):
    visited = set()
    stack = [start]
    while stack:
        node = stack.pop()
        if node in visited:
            continue
        visited.add(node)
        for neighbor in graph[node]:
            stack.append(neighbor)

#-------------------------------
# Backtracking template
#-------------------------------
def backtrack(result, current, choices):
    if is_complete(current):
        result.append(current[:])
        return
    for choice in choices:
        current.append(choice)
        backtrack(result, current, next_choices(choice))
        current.pop()

#-------------------------------
# Union-Find (Disjoint Set Union)
#-------------------------------
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True

#-------------------------------
# Trie (Prefix Tree)
#-------------------------------
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word):
        node = self.root
        for ch in word:
            node = node.children.setdefault(ch, TrieNode())
        node.is_end = True

    def search(self, word):
        node = self.root
        for ch in word:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return node.is_end

    def startsWith(self, prefix):
        node = self.root
        for ch in prefix:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return True
```

### 建立前綴和陣列


累積和的慣用寫法：先算好累計總和，之後任何區間和都變成 O(1)。
完整內容見 [`prefix_sum.md`](./prefix_sum.md)。

```python
cnt = [1, 0, 1, 1, 1]

# Step 1: allocate size n+1, fill with 0
#   prefix[0] = 0 is the "empty sum" sentinel
#   -> makes sum starting at index 0 work without a special case
prefix = [0] * (len(cnt) + 1)
# prefix = [0, 0, 0, 0, 0, 0]

# Step 2 (CORE) : prefix[i+1] = running total up to (and including) cnt[i]
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]

# prefix = [0, 1, 1, 2, 3, 4]
```

**要背起來的那一行：**
```python
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]
```

**追蹤（注意結果會比 `cnt` 多一個元素）：**
```text
cnt:        [ 1,  0,  1,  1,  1 ]
index i:      0   1   2   3   4

prefix[0] = 0                            ← sentinel (empty prefix)
prefix[1] = prefix[0] + cnt[0] = 0 + 1 = 1
prefix[2] = prefix[1] + cnt[1] = 1 + 0 = 1
prefix[3] = prefix[2] + cnt[2] = 1 + 1 = 2
prefix[4] = prefix[3] + cnt[3] = 2 + 1 = 3
prefix[5] = prefix[4] + cnt[4] = 3 + 1 = 4

prefix = [0, 1, 1, 2, 3, 4]
          ↑                 ↑
       empty sum        sum of ALL cnt
```

**為什麼寫在索引 `i + 1`（而不是 `i`）？** `prefix` 的大小是 `n+1`，`prefix[k]` 代表
「前 `k` 個元素的和」。寫進 `prefix[i+1]` 才能保住開頭的 `prefix[0]=0` ——
這樣 `sum(l, r) = prefix[r+1] - prefix[l]` 就不必處理任何邊界情況。

**一行版替代方案** —— 用 `itertools.accumulate` 搭配 `initial=0`：
```python
from itertools import accumulate
prefix = list(accumulate(cnt, initial=0))   # [0, 1, 1, 2, 3, 4]

# without initial=0 -> same length as cnt, no leading sentinel
list(accumulate(cnt))                        # [1, 1, 2, 3, 4]
```

**區間和查詢（建表 O(n) 之後每次 O(1)）：**
```python
# sum of cnt[l .. r] inclusive
def range_sum(l, r):
    return prefix[r + 1] - prefix[l]

range_sum(1, 3)   # cnt[1]+cnt[2]+cnt[3] = 0+1+1 = 2  -> prefix[4]-prefix[1] = 3-1 = 2
```
