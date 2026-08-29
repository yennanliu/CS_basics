# 樹狀陣列（Binary Indexed Tree / Fenwick Tree）

> **範圍** — 專講 Fenwick 樹：`i & -i` 的索引運算、單點更新 + 前綴查詢，以及用它最俐落的那些題目。
> **另見**：[segment_tree.md](./segment_tree.md) — 更通用的區間結構，以及 BIT vs 線段樹該怎麼選；[prefix_sum.md](./prefix_sum.md) — 靜態、不支援更新的版本。

## LeetCode 題目清單

- [Binary Indexed Tree](https://leetcode.com/problem-list/binary-indexed-tree/)
- [Segment Tree](https://leetcode.com/problem-list/segment-tree/)

## 總覽

樹狀陣列（BIT），也叫 Fenwick 樹，是一個能同時高效支援下面兩件事的資料結構：

單點更新（也就是修改陣列裡的某個元素）。
前綴和查詢（也就是查陣列開頭到某個索引為止的總和）。
樹狀陣列的重點特性：
更新很快：修改陣列中的一個元素只要 O(log n)。
查詢很快：算出從開頭到某索引的區間和只要 O(log n)。
運作方式：
BIT 建在一個陣列之上，用類似樹的結構存放陣列的部分和。索引用二進位表示，BIT 陣列的每個位置依照該索引的二進位形式，存下某個元素子集合的和。

BIT 的結構：
底層陣列是 1-indexed（也可以改寫成 0-indexed）。
對每個索引 i，BIT 存的是一段元素的和。這段的長度取決於索引 i 的最低有效設定位元。
舉例來說：

BIT[i] 存的是從索引 i - (i & -i) + 1 到 i 的元素和，其中 i & -i 取出 i 的二進位表示中最低的那個設定位元。
操作：
1. 更新操作：
要修改原陣列的某個元素時，你得把 BIT 裡的值一起調整，讓它反映這次更新。更新會往上傳播到所有「其總和涵蓋這個索引」的位置。

要更新 BIT 的索引 i，就先調整 i 這格，接著跳到 i + (i & -i)（也就是同一組裡的下一個索引）繼續調整，直到超出 BIT 尾端。
2. 前綴和查詢：
要算出索引 1 到 i（含）的總和，就從 i 開始，不斷減掉最低設定位元（i & -i）往前跳，把沿路這些索引的值加起來。

樹狀陣列的例子：
考慮陣列：[3, 2, -1, 6, 5, 4]。

BIT 初始化：先開一個等長的 BIT 陣列（一開始全是 0），再依輸入陣列逐一更新把它建起來。

更新操作：想改原陣列的某個元素時（例如把 array[2] 從 -1 改成 4），就更新對應的 BIT 值。

前綴和查詢：要求索引 1 到 i 的和，就把相關的 BIT 值加總。



- LC 307

## 模板與演算法

### BIT 類別 — 單點更新 + 前綴／區間查詢


```java
// BIT (Fenwick Tree) — Generic Template
// time = O(log N) per update/query, space = O(N)
class BIT {
    int[] tree;
    int n;
    BIT(int n) { this.n = n; tree = new int[n + 1]; }
    void update(int i, int delta) {          // 1-indexed; add delta at position i
        for (; i <= n; i += i & -i) tree[i] += delta;
    }
    int query(int i) {                        // prefix sum [1..i]
        int sum = 0;
        for (; i > 0; i -= i & -i) sum += tree[i];
        return sum;
    }
    int query(int l, int r) { return query(r) - query(l - 1); } // range [l..r]
}
```


### 為什麼是 `i & -i` — lowbit 的機制 ⭐⭐⭐⭐⭐

> **核心想法**：`i & -i` 會取出 `i` 的**最低設定位元**。這個值剛好就是 `tree[i]` 涵蓋多少個原始元素，
> 所以它同時是*跳躍步長*和*區間寬度*。

```text
n = 8, tree[i] covers the range (i - lowbit(i), i]   (1-indexed)

 i  | binary | lowbit | tree[i] covers
----+--------+--------+----------------
 1  |  0001  |   1    | [1..1]
 2  |  0010  |   2    | [1..2]
 3  |  0011  |   1    | [3..3]
 4  |  0100  |   4    | [1..4]
 5  |  0101  |   1    | [5..5]
 6  |  0110  |   2    | [5..6]
 7  |  0111  |   1    | [7..7]
 8  |  1000  |   8    | [1..8]

QUERY prefix(7): walk DOWN, i -= lowbit(i)   -> ADD disjoint blocks
    7 -> 6 -> 4 -> 0
    tree[7]  + tree[6]  + tree[4]
    [7..7]   + [5..6]   + [1..4]      = [1..7]   (exactly covers, no overlap)

UPDATE at 3: walk UP, i += lowbit(i)         -> every block CONTAINING 3
    3 -> 4 -> 8
    tree[3], tree[4], tree[8]
    [3..3]  [1..4]   [1..8]

Both walks strip / carry one bit per step  =>  O(log N).
```

**要背起來的兩條規則**

| 操作 | 方向 | 步長 | 為什麼 |
|-----------|-----------|------|-----|
| `query(i)` — 前綴 `[1..i]` | 往下到 0 | `i -= i & -i` | 剝掉剛剛涵蓋的那一塊，跳到剩下的部分 |
| `update(i, d)` — 單點加值 | 往上到 `n` | `i += i & -i` | 跳到下一個*包含* `i` 的區塊 |

> **容易踩到的坑**：BIT 是 **1-indexed**，而索引 `0` 在兩邊會用*不同方式*出錯。
> `i = 0` 時 `i & -i` 是 `0`，所以 `update(0, d)` 永遠不會前進，會**無限迴圈**。
> `query(0)` 不會卡住 — 它的迴圈條件是 `i > 0`，所以立刻回傳 `0`，然後默默漏掉你本來想算的那個元素。
> 兩邊都一樣：在邊界用 `i + 1` 轉換（上面 LC 307 就是這樣做）。


### 建在「位置」上的 BIT — 刪除後仍然「活著」的索引 — LC 1505 / LC 1409 ⭐⭐⭐⭐

> **模式**：BIT 在每個**還在**的位置存 `1`，被移除／搬走的位置存 `0`。那麼 `query(i)` = 前 `i` 格裡有幾格還活著
> = 那個元素的**當前索引**，就算整體一直在位移也一樣成立。

**為什麼要 BIT**：直覺的「我前面還剩幾個？」每一步都要掃 `O(N)` → 總共 `O(N^2)`。
BIT 只要 `O(log N)` 就能回答，而移除本身就是一次單點更新。

**核心想法**：永遠不要真的去搬動陣列。原始位置固定不動，讓前綴和負責把*原始位置*翻譯成*當前位置*。

#### 範例 A — LC 1505（貪婪 + 用 BIT 算出真正的交換成本）

貪婪：一位一位建出答案；每一步試 `0..9`，取最小且成本還付得起的那個數字。把原始索引 `i` 上的數字拖到最前面的成本是
`i -（i 之前已被移除的數字個數）` — 那個修正量就是 BIT 提供的。

```java
// LC 1505 - Minimum Possible Integer After at Most K Adjacent Swaps On Digits
// IDEA: greedy smallest digit + BIT over POSITIONS to get the real (post-removal) swap cost
// time = O(N * 10 * log N), space = O(N)
public String minInteger(String num, int k) {
    int n = num.length();
    // queue of original indices for each digit, ascending
    List<Deque<Integer>> pos = new ArrayList<>();
    for (int d = 0; d <= 9; d++) pos.add(new ArrayDeque<>());
    for (int i = 0; i < n; i++) pos.get(num.charAt(i) - '0').addLast(i);

    BIT removed = new BIT(n);          // 1 at index -> that digit is already consumed
    StringBuilder sb = new StringBuilder();

    for (int step = 0; step < n; step++) {
        for (int d = 0; d <= 9; d++) {
            if (pos.get(d).isEmpty()) continue;
            int i = pos.get(d).peekFirst();               // earliest remaining occurrence
            // removed.query(i) = consumed digits among original idx 0..i-1
            int cost = i - removed.query(i);              // swaps needed to bring it to the front
            if (cost <= k) {
                k -= cost;
                pos.get(d).pollFirst();
                removed.update(i + 1, 1);                 // +1 -> 1-indexed BIT
                sb.append((char) ('0' + d));
                break;
            }
        }
    }
    return sb.toString();
}
```

```python
# python
# LC 1505 - Minimum Possible Integer After at Most K Adjacent Swaps On Digits
# IDEA: greedy smallest digit + BIT over POSITIONS to get the real (post-removal) swap cost
# time = O(N * 10 * log N), space = O(N)
# (reuses the BIT class from 2-13)
from collections import deque


def minInteger(num, k):
    n = len(num)
    pos = [deque() for _ in range(10)]          # original indices per digit
    for i, ch in enumerate(num):
        pos[int(ch)].append(i)

    removed = BIT(n)                            # 1 at index -> digit already consumed
    out = []

    for _ in range(n):
        for d in range(10):
            if not pos[d]:
                continue
            i = pos[d][0]                       # earliest remaining occurrence
            cost = i - removed.query(i)         # consumed among original idx 0..i-1
            if cost <= k:
                k -= cost
                pos[d].popleft()
                removed.update(i + 1, 1)        # +1 -> 1-indexed BIT
                out.append(str(d))
                break
    return "".join(out)
```

> **貪婪為什麼不會卡住**：目前還在的最小原始索引，`cost` 一定是 `0`（前面沒有任何還活著的元素），
> 所以永遠有數字付得起，迴圈每一步都一定能吐出一位數字。

#### 範例 B — LC 1409（搬到最前面，加一段備用緩衝）

一樣是「BIT 建在位置上」的想法，只是元素是搬到**最前面**而不是消失。在原本的 `m` 格前面預留 `n` 個空格，
這樣每次搬到最前面都有新的空位可用。

```java
// LC 1409 - Queries on a Permutation With Key
// IDEA: BIT over POSITIONS — n spare front slots; query(p-1) = # alive before p = current index
// time = O(N log(N+M)), space = O(N + M)
public int[] processQueries(int[] queries, int m) {
    int n = queries.length, size = n + m;
    BIT bit = new BIT(size);
    int[] posOf = new int[m + 1];
    // value v initially lives at BIT slot n+v  (slots 1..n are the reserved front buffer)
    for (int v = 1; v <= m; v++) { posOf[v] = n + v; bit.update(n + v, 1); }

    int front = n;                       // next free slot, filled right-to-left
    int[] res = new int[n];
    for (int q = 0; q < n; q++) {
        int p = posOf[queries[q]];
        res[q] = bit.query(p - 1);       // # of alive slots before p == current 0-based index
        bit.update(p, -1);               // vacate old slot
        posOf[queries[q]] = front;
        bit.update(front, 1);            // occupy new front slot
        front--;
    }
    return res;
}
```

```python
# python
# LC 1409 - Queries on a Permutation With Key
# IDEA: BIT over POSITIONS — n spare front slots; query(p-1) = # alive before p = current index
# time = O(N log(N+M)), space = O(N + M)
def processQueries(queries, m):
    n = len(queries)
    bit = BIT(n + m)
    pos_of = [0] * (m + 1)
    # value v initially lives at BIT slot n+v (slots 1..n are the reserved front buffer)
    for v in range(1, m + 1):
        pos_of[v] = n + v
        bit.update(n + v, 1)

    front = n                            # next free slot, filled right-to-left
    res = []
    for q in queries:
        p = pos_of[q]
        res.append(bit.query(p - 1))     # alive slots before p == current 0-based index
        bit.update(p, -1)                # vacate old slot
        pos_of[q] = front
        bit.update(front, 1)             # occupy new front slot
        front -= 1
    return res
```

> **看到這些字眼就想到這個模式**：題目說*「移除一個元素，後面全部往左移」*、*「搬到最前面」*或*「成本 = 當前距離」*。
> 固定格子 + 0/1 的 BIT，能把每次位移都變成 `O(log N)` 的前綴計數。

## 抉擇：BIT vs 線段樹 vs 合併排序


很多掛著 BIT 標籤的題目其實是**偽裝過的逆序數計算**，而合併排序不用額外資料結構就能回答同一個問題。要刻意去選：

| | 樹狀陣列 | 線段樹 | 合併排序（分治法） |
|---|---|---|---|
| **能回答** | 前綴聚合 + 單點更新 | 任意區間查詢 + 區間更新 | 只能做離線的配對計數 |
| **支援的運算** | 只支援可逆運算（sum、xor、計數） | 任何結合律運算（min/max/gcd/sum） | 每次 merge 固定算一種計數 |
| **能線上處理（更新與查詢交錯）？** | 可以 | 可以 | **不行** — 需要一開始就拿到全部輸入 |
| **程式碼長度** | 約 6 行 | 約 40 行 | 約 25 行 |
| **常數項** | 最小 | 約 BIT 的 2-3 倍 | 小，但要配置暫存陣列 |
| **典型 LC** | 307, 315, 493, 1395, 1505, 1409, 1649 | 699, 715, 732, 850, 1622 | 315, 327, 493 |

**判斷原則**

- 需要區間的 **min / max / gcd**，或**懶惰區間更新**？→ 用線段樹，不要用 BIT。
  BIT 只在你能*還原*的運算上成立（`query(r) - query(l-1)`），這就把 min/max 排除掉了。
- 需要**區間插入／合併／「這段被覆蓋了嗎」**（行事曆與矩形類題目）？→
  用線段樹配座標壓縮，或用有序 map。見 `doc/cheatsheet/segment_tree.md`。
- **離線**計算滿足 `i < j` 且某種大小關係的配對 `(i, j)`？→ 合併排序通常是最短的正確解，
  而且完全不需要座標壓縮。
  LC 315 的作法見 `doc/cheatsheet/sort.md` 的 **Template 7: Merge Sort as a COUNTER**。
- **同一題兩種工具**：LC 315 / 327 / 493 兩種都能解。
  - *合併排序*：不用壓縮、不用假設值域 — 但嚴格只能離線。
  - *BIT*：得先做座標壓縮，但可以免費延伸到*線上*串流（LC 1649）
    以及「兩側各數一次」的掃描（LC 1395），這是合併排序做不到的。
- **完全沒有更新**？→ 什麼都別建。單純的前綴和陣列每次查詢就是 `O(1)`。
  見 `doc/cheatsheet/prefix_sum.md`。


### 同一題的兩種寫法 — LC 307


```java
// LC 307 - Range Sum Query Mutable (BIT version)
// IDEA: BIT — O(log N) point update and prefix sum; range via subtraction
// time = O(log N) per update/query, space = O(N)
class NumArray {
    int[] bit, nums;
    int n;
    public NumArray(int[] nums) {
        n = nums.length;
        this.nums = new int[n];
        bit = new int[n + 1];
        for (int i = 0; i < n; i++) update(i, nums[i]);
    }
    public void update(int i, int val) {
        int delta = val - nums[i];
        nums[i] = val;
        for (int x = i + 1; x <= n; x += x & -x) bit[x] += delta;
    }
    public int sumRange(int l, int r) { return prefix(r+1) - prefix(l); }
    private int prefix(int i) { int s=0; for(;i>0;i-=i&-i) s+=bit[i]; return s; }
}
```


## LC 範例

### 單點更新、前綴查詢

#### 1) Range Sum Query — Mutable — LC 307

> BIT 支援 O(log N) 的單點更新與前綴和查詢。

```java
// LC 307 - Range Sum Query - Mutable
// IDEA: Binary Indexed Tree (Fenwick Tree)
// time = O(log N) per update/query, space = O(N)
class NumArray {
    int[] bit, nums;
    int n;
    public NumArray(int[] nums) {
        this.n = nums.length;
        this.nums = new int[n];
        this.bit = new int[n + 1];
        for (int i = 0; i < n; i++) update(i, nums[i]);
    }
    public void update(int i, int val) {
        int delta = val - nums[i];
        nums[i] = val;
        for (int x = i + 1; x <= n; x += x & (-x)) bit[x] += delta;
    }
    public int sumRange(int left, int right) {
        return prefixSum(right + 1) - prefixSum(left);
    }
    private int prefixSum(int i) {
        int sum = 0;
        for (int x = i; x > 0; x -= x & (-x)) sum += bit[x];
        return sum;
    }
}
```

#### 2) Range Sum Query 2D — Mutable — LC 308 — 二維 BIT

> 把 BIT 推廣到二維：矩陣上每次更新／查詢是 O(log M * log N)。

#### 一維 BIT vs 二維 BIT

| | 一維 BIT（LC 307） | 二維 BIT（LC 308） |
|---|---|---|
| **結構** | 大小 `n+1` 的 `int[] bit` | 大小 `(m+1) x (n+1)` 的 `int[][] bit` |
| **更新** | 單層迴圈：`i += i & -i` | 巢狀迴圈：列 `i += i & -i`、行 `j += j & -j` |
| **查詢** | 單層迴圈：`i -= i & -i` | 巢狀迴圈：列 `i -= i & -i`、行 `j -= j & -j` |
| **區間和** | `prefix(r+1) - prefix(l)` | 二維排容原理（4 項） |
| **時間** | O(log N) | O(log M * log N) |

#### 為什麼要用二維排容？

```text
To get sum of rectangle (r1,c1) to (r2,c2):

  query(r2, c2)           = entire top-left block
- query(r1-1, c2)         = remove rows above
- query(r2, c1-1)         = remove cols to the left
+ query(r1-1, c1-1)       = add back double-subtracted corner

Visual:

  ┌──────────┬──────────┐
  │  +added  │  -removed│     query(r1-1, c2) removes this top strip
  │  back    │  (top)   │
  ├──────────┼──────────┤  r1
  │  -removed│ ★ TARGET │
  │  (left)  │  REGION  │
  └──────────┴──────────┘  r2
            c1          c2

  sum = query(r2,c2) - query(r1-1,c2) - query(r2,c1-1) + query(r1-1,c1-1)
```

這跟二維前綴和（LC 304）的排容原理一模一樣，差別在 BIT 支援**可變更新**。

#### Java 實作

```java
// LC 308 - Range Sum Query 2D - Mutable
// IDEA: 2D Binary Indexed Tree (Fenwick Tree)
// time = O(log M * log N) per update/query, space = O(M * N)
class NumMatrix {
    private int[][] tree;  // 2D BIT (1-indexed)
    private int[][] nums;  // original values (for computing delta)
    private int m, n;

    public NumMatrix(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) return;
        m = matrix.length;
        n = matrix[0].length;
        tree = new int[m + 1][n + 1];
        nums = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                update(i, j, matrix[i][j]);
    }

    // Point update: set matrix[row][col] = val
    public void update(int row, int col, int val) {
        int delta = val - nums[row][col];
        nums[row][col] = val;
        // Propagate delta through 2D BIT (nested lowbit traversal)
        for (int i = row + 1; i <= m; i += i & -i)
            for (int j = col + 1; j <= n; j += j & -j)
                tree[i][j] += delta;
    }

    // Prefix sum query: sum of (0,0) to (row,col)
    private int query(int row, int col) {
        int sum = 0;
        for (int i = row + 1; i > 0; i -= i & -i)
            for (int j = col + 1; j > 0; j -= j & -j)
                sum += tree[i][j];
        return sum;
    }

    // Range sum: rectangle (row1,col1) to (row2,col2)
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // 2D inclusion-exclusion
        return query(row2, col2)
             - query(row1 - 1, col2)
             - query(row2, col1 - 1)
             + query(row1 - 1, col1 - 1);
    }
}
```

#### 另一種作法：逐列的一維 BIT（比較簡單但比較慢）

```java
// Each row has its own 1D BIT
// update = O(log N), sumRegion = O(M * log N)  — slower for large M
class NumMatrix_RowBIT {
    private BIT[] trees;

    public NumMatrix_RowBIT(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        trees = new BIT[m];
        for (int i = 0; i < m; i++) {
            trees[i] = new BIT(n);
            for (int j = 0; j < n; j++)
                trees[i].update(j + 1, matrix[i][j]);
        }
    }

    public void update(int row, int col, int val) {
        int prev = trees[row].query(col + 1) - trees[row].query(col);
        trees[row].update(col + 1, val - prev);
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int sum = 0;
        for (int i = row1; i <= row2; i++)
            sum += trees[i].query(col2 + 1) - trees[i].query(col1);
        return sum;
    }
}
```

#### LC 308 各解法比較

| 解法 | 更新 | sumRegion | 空間 | 什麼時候用 |
|----------|--------|-----------|-------|-------------|
| **暴力法** | O(1) | O(M*N) | O(M*N) | 永遠別用（TLE） |
| **逐列一維 BIT** | O(log N) | O(M * log N) | O(M*N) | 列數少、查詢多 |
| **二維 BIT** | O(log M * log N) | O(log M * log N) | O(M*N) | 整體最平衡 |
| **二維線段樹** | O(log M * log N) | O(log M * log N) | O(M*N) | 需要懶惰傳遞時 |

#### 相關題目

| 題目 | LC # | 與 LC 308 的關係 |
|---------|------|--------------------|
| Range Sum Query - Mutable | 307 | 同一模式的一維版 |
| Range Sum Query 2D - Immutable | 304 | 二維前綴和（不支援更新） |
| Count of Smaller Numbers After Self | 315 | BIT 配座標壓縮 |
| Count of Range Sum | 327 | 用 BIT／合併排序做區間計數 |

### 配合座標壓縮計算逆序數

#### 3) Count of Smaller Numbers After Self — LC 315 ⭐⭐⭐⭐

> 把值映射成排名；對每個元素查詢「已插入的元素裡有幾個比它小」。

```java
// LC 315 - Count of Smaller Numbers After Self
// IDEA: BIT + coordinate compression — process right to left
// time = O(N log N), space = O(N)
public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] sorted = nums.clone();
    Arrays.sort(sorted);
    Map<Integer, Integer> rank = new HashMap<>();
    int r = 1;
    for (int v : sorted) if (!rank.containsKey(v)) rank.put(v, r++);
    int[] bit = new int[r];
    Integer[] result = new Integer[n];
    for (int i = n - 1; i >= 0; i--) {
        int pos = rank.get(nums[i]);
        result[i] = query(bit, pos - 1);
        update(bit, pos, r - 1);
    }
    return Arrays.asList(result);
}
private void update(int[] bit, int i, int n) {
    for (; i <= n; i += i & (-i)) bit[i]++;
}
private int query(int[] bit, int i) {
    int sum = 0;
    for (; i > 0; i -= i & (-i)) sum += bit[i];
    return sum;
}
```

#### 4) Reverse Pairs — LC 493

> 對每個 nums[i]（從右往左），數出先前插入的值裡有幾個 < nums[i]；接著插入 2*nums[i]。

```java
// LC 493 - Reverse Pairs
// IDEA: BIT + coordinate compression — process right to left; query then update
// time = O(N log N), space = O(N)
public int reversePairs(int[] nums) {
    int n = nums.length;
    long[] sorted = new long[2 * n];
    for (int i = 0; i < n; i++) { sorted[i] = nums[i]; sorted[n+i] = 2L * nums[i]; }
    Arrays.sort(sorted);
    Map<Long, Integer> rank = new HashMap<>();
    int r = 1;
    for (long v : sorted) if (!rank.containsKey(v)) rank.put(v, r++);
    int[] bit = new int[r];
    int count = 0;
    for (int i = n - 1; i >= 0; i--) {
        count += queryBIT(bit, rank.get((long)nums[i]) - 1);
        updateBIT(bit, rank.get(2L * nums[i]), r - 1);
    }
    return count;
}
private void updateBIT(int[] b, int i, int n) { for (; i <= n; i += i&-i) b[i]++; }
private int  queryBIT(int[] b, int i)         { int s=0; for(;i>0;i-=i&-i) s+=b[i]; return s; }
```

#### 5) Count of Range Sum — LC 327 — BIT 或合併排序

> 對每個前綴和，數出先前的前綴和裡有幾個落在 [prefixSum-upper, prefixSum-lower]。

```java
// LC 327 - Count of Range Sum (merge sort approach)
// IDEA: Merge sort — count valid pairs during the merge step
// time = O(N log N), space = O(N)
public int countRangeSum(int[] nums, int lower, int upper) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) prefix[i+1] = prefix[i] + nums[i];
    return mergeCount(prefix, 0, n + 1, lower, upper);
}
private int mergeCount(long[] arr, int l, int r, int lo, int hi) {
    if (r - l <= 1) return 0;
    int mid = (l + r) / 2;
    int count = mergeCount(arr, l, mid, lo, hi) + mergeCount(arr, mid, r, lo, hi);
    int j = mid, k = mid;
    for (int i = l; i < mid; i++) {
        while (j < r && arr[j] - arr[i] < lo) j++;
        while (k < r && arr[k] - arr[i] <= hi) k++;
        count += k - j;
    }
    long[] tmp = Arrays.copyOfRange(arr, l, r);
    Arrays.sort(tmp);
    System.arraycopy(tmp, 0, arr, l, tmp.length);
    return count;
}
```

#### 6) Global and Local Inversions — LC 775

> 全域逆序數永遠 >= 區域逆序數；相等的充要條件是不存在不相鄰的逆序對。

```java
// LC 775 - Global and Local Inversions
// IDEA: Global inversions == local iff no nums[i] > nums[j] for j >= i+2
// time = O(N), space = O(1)
public boolean isIdealPermutation(int[] nums) {
    // global == local iff every element is within 1 of its sorted position
    for (int i = 0; i < nums.length; i++)
        if (Math.abs(nums[i] - i) > 1) return false;
    return true;
}
```

### 建在「值」而非「位置」上的計數

#### 7) Count Number of Teams — LC 1395 — 在每個索引的兩側各數一次


> **模式**：BIT 以**值**為索引（不是陣列位置）。從左往右掃一次得到
> `leftSmaller[j] / leftLarger[j]`，從右往左掃一次得到 `rightSmaller[j] / rightLarger[j]`，
> 然後把每個元素固定當成三元組的**中間那個**。

**核心想法**：對中間元素 `j`，
`teams(j) = leftSmaller[j] * rightLarger[j] + leftLarger[j] * rightSmaller[j]`
（遞增三元組 + 遞減三元組）。把所有 `j` 加總，每個三元組剛好被算到一次，因為每個三元組只有一個中間元素。

**計數小技巧**：插入 `t` 個元素之後，`larger = t - query(v)`，因為 `query(v)` 數的是所有 `<= v` 的元素。
「比較大」那一側不需要第二棵 BIT。

```java
// LC 1395 - Count Number of Teams
// IDEA: BIT over VALUES — for each middle soldier, count smaller/larger on the left and right
// time = O(N log M), space = O(M)   M = value range (ratings <= 1e5)
public int numTeams(int[] rating) {
    int MAX = 100000, n = rating.length;
    int[] leftSmaller = new int[n], leftLarger = new int[n];

    // pass 1: left -> right. after j insertions, j elements sit to the left of index j
    BIT left = new BIT(MAX);
    for (int j = 0; j < n; j++) {
        leftSmaller[j] = left.query(rating[j] - 1);          // count(< rating[j])
        leftLarger[j]  = j - left.query(rating[j]);          // j - count(<= rating[j])
        left.update(rating[j], 1);
    }

    // pass 2: right -> left, and combine
    BIT right = new BIT(MAX);
    long res = 0;
    for (int j = n - 1; j >= 0; j--) {
        int rightSmaller = right.query(rating[j] - 1);
        int rightLarger  = (n - 1 - j) - right.query(rating[j]);
        res += (long) leftSmaller[j] * rightLarger           // ascending  i < j < k
             + (long) leftLarger[j]  * rightSmaller;         // descending i > j > k
        right.update(rating[j], 1);
    }
    return (int) res;
}

// BIT helper (1-indexed)
class BIT {
    int[] tree; int n;
    BIT(int n) { this.n = n; tree = new int[n + 1]; }
    void update(int i, int delta) { for (; i <= n; i += i & -i) tree[i] += delta; }
    int query(int i) { int s = 0; for (; i > 0; i -= i & -i) s += tree[i]; return s; }
}
```

```python
# python
# LC 1395 - Count Number of Teams
# IDEA: BIT over VALUES — for each middle soldier, count smaller/larger on the left and right
# time = O(N log M), space = O(M)   M = value range (ratings <= 1e5)
class BIT:
    def __init__(self, n):
        self.n = n
        self.tree = [0] * (n + 1)

    def update(self, i, delta):
        while i <= self.n:
            self.tree[i] += delta
            i += i & (-i)

    def query(self, i):          # prefix sum [1..i]
        s = 0
        while i > 0:
            s += self.tree[i]
            i -= i & (-i)
        return s


def numTeams(rating):
    MAX = 100000
    n = len(rating)
    left_smaller = [0] * n
    left_larger = [0] * n

    # pass 1: left -> right
    left = BIT(MAX)
    for j in range(n):
        left_smaller[j] = left.query(rating[j] - 1)      # count(< rating[j])
        left_larger[j] = j - left.query(rating[j])       # j - count(<= rating[j])
        left.update(rating[j], 1)

    # pass 2: right -> left, and combine
    right = BIT(MAX)
    res = 0
    for j in range(n - 1, -1, -1):
        right_smaller = right.query(rating[j] - 1)
        right_larger = (n - 1 - j) - right.query(rating[j])
        res += left_smaller[j] * right_larger + left_larger[j] * right_smaller
        right.update(rating[j], 1)
    return res
```

> **變形 — 稀疏／超大值域**：如果 rating 沒有上界，就先做座標壓縮（跟上面 LC 315 / LC 493 一樣），
> BIT 的大小改用相異值的個數，而不是 `MAX`。
>
> **變形 — 數四元組／更長的鏈**：把兩個計數陣列換成
> `dp[len][j]` 表格，其中 `dp[L][j] = Σ dp[L-1][i]`（對所有 `i < j, rating[i] < rating[j]`），
> 並讓 BIT 存 **dp 值的總和**（而不是一堆 1 的計數）。骨架相同，只是裝的東西不同。

#### 8) Create Sorted Array through Instructions — LC 1649

> 每道指令的成本 = min(已插入元素中比它小的個數, 比它大的個數)；用 BIT。

```java
// LC 1649 - Create Sorted Array through Instructions
// IDEA: BIT on value range — count smaller and greater elements already inserted
// time = O(N log M), space = O(M)  M = max value
public int createSortedArray(int[] instructions) {
    int MOD = 1_000_000_007, n = 100001;
    int[] bit = new int[n + 1];
    long cost = 0;
    for (int i = 0; i < instructions.length; i++) {
        int x = instructions[i];
        int smaller = query(bit, x - 1);
        int larger  = i - query(bit, x);
        cost = (cost + Math.min(smaller, larger)) % MOD;
        update(bit, x, n);
    }
    return (int) cost;
}
private void update(int[] b, int i, int n) { for (; i <= n; i += i&-i) b[i]++; }
private int  query(int[] b, int i)         { int s=0; for(;i>0;i-=i&-i) s+=b[i]; return s; }
```

#### 9) Number of Longest Increasing Subsequences — LC 673 — DP

> 每個位置同時追蹤長度與數量；遇到等長的路徑就把數量加上去。

```java
// LC 673 - Number of Longest Increasing Subsequences
// IDEA: DP — (len[i], cnt[i]) = (LIS length at i, number of such LIS)
// time = O(N^2), space = O(N)
public int findNumberOfLIS(int[] nums) {
    int n = nums.length, maxLen = 0, result = 0;
    int[] len = new int[n], cnt = new int[n];
    for (int i = 0; i < n; i++) {
        len[i] = cnt[i] = 1;
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                if (len[j] + 1 > len[i]) { len[i] = len[j]+1; cnt[i] = cnt[j]; }
                else if (len[j] + 1 == len[i]) cnt[i] += cnt[j];
            }
        }
        if (len[i] > maxLen) { maxLen = len[i]; result = cnt[i]; }
        else if (len[i] == maxLen) result += cnt[i];
    }
    return result;
}
```

#### 10) Queue Reconstruction by Height — LC 406 — 貪婪插入

> 依身高 DESC 排序（同高則 k ASC）；把每個人插到索引 k — 比他高的都已經排好了。

```java
// LC 406 - Queue Reconstruction by Height
// IDEA: Sort height DESC (k ASC); insert at index k — taller people already positioned
// time = O(N^2), space = O(N)
public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);
    List<int[]> res = new ArrayList<>();
    for (int[] p : people) res.add(p[1], p);
    return res.toArray(new int[res.size()][]);
}
```

## 其他掛著 BIT／線段樹標籤的題目（參考用）

這些題目在 LeetCode 上帶著 BIT／線段樹標籤，但**不是** BIT 的模板題 — 預期解法是掃描線、堆積或有序 map。
列在這裡是為了別被標籤誤導。

| LC | 題目 | 難度 | 實際上用什麼解 |
|----|-------|------|----------------------|
| 218 | The Skyline Problem | Hard | 掃描線 + 對現存建築高度的最大堆積（或 multiset） |
| 699 | Falling Squares | Hard | 座標壓縮的線段樹配懶惰 max，或 `O(N^2)` 區間掃描 |
| 715 | Range Module | Hard | 用有序 map 維護互不相交的區間（`TreeMap`）— 見 `segment_tree.md` |
| 729 | My Calendar I | Medium | `TreeMap.floorKey/ceilingKey` 做重疊檢查 |
| 731 | My Calendar II | Medium | 兩個區間清單，或邊界差分計數 |
| 732 | My Calendar III | Hard | 邊界差分計數（`TreeMap` 掃描）求最大重疊 |
| 850 | Rectangle Area II | Hard | 沿 x 掃描線，用線段樹算被覆蓋的 y 長度 |
| 1622 | Fancy Sequence | Hard | 懶惰仿射變換 + 模反元素（不用 BIT） |
| 1157 | Online Majority Element In Subarray | Hard | 隨機抽樣 + 在每個值的索引清單上二分搜尋 |

> **面試重點**：只有在查詢是**可逆運算的前綴聚合**、而且搭配**單點更新**時，才該掏出 BIT。
> 這張表裡的其他東西都該用別的工具。
