# Segment Tree & Binary Indexed Tree (Fenwick Tree)

> **範圍** — 支援區間查詢＋區間更新的結構 —— 線段樹（segment tree）、懶惰標記（lazy propagation），以及 BIT vs 線段樹 vs 合併排序該怎麼選。
> **另見**：[binary_indexed_tree.md](./binary_indexed_tree.md) — 樹狀陣列（Fenwick tree）的深入版；[prefix_sum.md](./prefix_sum.md) — 完全不需要更新時用它；[difference_array.md](./difference_array.md) — 區間更新、最後只讀一次。

## LeetCode 題目清單

- [Segment Tree](https://leetcode.com/problem-list/segment-tree/)
- [Binary Indexed Tree](https://leetcode.com/problem-list/binary-indexed-tree/)

## 總覽
**線段樹（Segment Tree）**和**樹狀陣列（BIT / Fenwick Tree）**是兩種進階資料結構，專門用來高效處理陣列上的區間查詢與更新。

### 關鍵性質
- **時間複雜度**：查詢跟更新都是 O(log n)
- **空間複雜度**：BIT 是 O(n)，線段樹是 O(4n)
- **核心想法**：把區間資訊預先算好存在樹狀結構裡，換取快速查詢
- **什麼時候用**：帶更新的區間 sum/min/max 查詢、順序統計量
- **主要操作**：區間查詢、單點／區間更新、建樹

### 核心特徵
- **區間查詢**：區間上的總和、最小值、最大值、GCD、XOR
- **單點更新**：高效修改單一元素
- **區間更新**：一次改整段（搭配懶惰標記）
- **空間換時間**：多花點空間換更快的查詢

## 題型分類

### **分類 1：區間和查詢**
- **說明**：在有更新的情況下算區間總和
- **例子**：LC 307（Range Sum Query - Mutable）、LC 308（Range Sum Query 2D - Mutable）
- **模式**：用 BIT 或線段樹做單點更新、區間查詢

### **分類 2：區間最小／最大值查詢**
- **說明**：在有更新的情況下找區間 min/max
- **例子**：LC 315（Count of Smaller Numbers After Self）、各種自訂 RMQ 題
- **模式**：把線段樹的合併操作換成 min/max

### **分類 3：帶懶惰標記的區間更新**
- **說明**：高效地一次更新整段區間
- **例子**：把某段全部加上一個值、把某段設成同一個值
- **模式**：線段樹 + 懶惰標記

### **分類 4：順序統計量與逆序對**
- **說明**：數比自己小／大的元素個數、數逆序對
- **例子**：LC 315（Count Smaller）、LC 493（Reverse Pairs）、LC 327（Count Range Sum）
- **模式**：BIT + 座標壓縮，或用合併排序

## 資料結構比較

### **BIT vs 線段樹**
| 面向 | 樹狀陣列（BIT） | 線段樹 |
|--------|-------------------|--------------|
| **空間** | O(n) | O(4n) |
| **實作** | 簡單、程式碼短 | 比較複雜 |
| **可做的運算** | 總和、XOR、OR | 任何滿足結合律的運算 |
| **區間更新** | 麻煩 | 加上懶惰標記就很簡單 |
| **1-indexed** | 天生適合 | 也可以配合 |
| **查詢型態** | 前綴查詢很容易 | 任意區間查詢 |

## 模板與演算法

### 模板 1：樹狀陣列（Fenwick Tree）
```python
class BIT:
    """Binary Indexed Tree for range sum queries and point updates"""

    def __init__(self, n):
        self.n = n
        self.tree = [0] * (n + 1)  # 1-indexed

    def update(self, i, delta):
        """Add delta to element at index i"""
        while i <= self.n:
            self.tree[i] += delta
            i += i & (-i)  # Add lowest set bit

    def query(self, i):
        """Get prefix sum from 1 to i"""
        total = 0
        while i > 0:
            total += self.tree[i]
            i -= i & (-i)  # Remove lowest set bit
        return total

    def range_query(self, left, right):
        """Get sum from left to right (inclusive)"""
        if left > 1:
            return self.query(right) - self.query(left - 1)
        else:
            return self.query(right)

    def build(self, arr):
        """Build BIT from array (1-indexed)"""
        for i in range(1, len(arr)):
            self.update(i, arr[i])
```

### 模板 2：線段樹（區間和）
```python
class SegmentTree:
    """Segment Tree for range sum queries and point updates"""

    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [0] * (4 * self.n)  # 4x space for safety
        self.build(arr, 1, 0, self.n - 1)

    def build(self, arr, node, start, end):
        """Build segment tree recursively"""
        if start == end:
            self.tree[node] = arr[start]
        else:
            mid = (start + end) // 2
            self.build(arr, 2 * node, start, mid)
            self.build(arr, 2 * node + 1, mid + 1, end)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]

    def update(self, node, start, end, idx, val):
        """Update single element at index idx to val"""
        if start == end:
            self.tree[node] = val
        else:
            mid = (start + end) // 2
            if idx <= mid:
                self.update(2 * node, start, mid, idx, val)
            else:
                self.update(2 * node + 1, mid + 1, end, idx, val)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]

    def query(self, node, start, end, left, right):
        """Query sum in range [left, right]"""
        if right < start or end < left:
            return 0  # No overlap

        if left <= start and end <= right:
            return self.tree[node]  # Complete overlap

        # Partial overlap
        mid = (start + end) // 2
        left_sum = self.query(2 * node, start, mid, left, right)
        right_sum = self.query(2 * node + 1, mid + 1, end, left, right)
        return left_sum + right_sum

    # Public interface methods
    def point_update(self, idx, val):
        """Update element at index idx to val"""
        self.update(1, 0, self.n - 1, idx, val)

    def range_sum(self, left, right):
        """Get sum in range [left, right]"""
        return self.query(1, 0, self.n - 1, left, right)
```

### 模板 3：帶懶惰標記的線段樹
```python
class LazySegmentTree:
    """Segment Tree with lazy propagation for range updates"""

    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [0] * (4 * self.n)
        self.lazy = [0] * (4 * self.n)
        self.build(arr, 1, 0, self.n - 1)

    def build(self, arr, node, start, end):
        if start == end:
            self.tree[node] = arr[start]
        else:
            mid = (start + end) // 2
            self.build(arr, 2 * node, start, mid)
            self.build(arr, 2 * node + 1, mid + 1, end)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]

    def push(self, node, start, end):
        """Push lazy value down to children"""
        if self.lazy[node] != 0:
            self.tree[node] += self.lazy[node] * (end - start + 1)

            if start != end:  # Not a leaf node
                self.lazy[2 * node] += self.lazy[node]
                self.lazy[2 * node + 1] += self.lazy[node]

            self.lazy[node] = 0

    def update_range(self, node, start, end, left, right, val):
        """Add val to range [left, right]"""
        self.push(node, start, end)

        if start > right or end < left:
            return

        if start >= left and end <= right:
            self.lazy[node] += val
            self.push(node, start, end)
            return

        mid = (start + end) // 2
        self.update_range(2 * node, start, mid, left, right, val)
        self.update_range(2 * node + 1, mid + 1, end, left, right, val)

        self.push(2 * node, start, mid)
        self.push(2 * node + 1, mid + 1, end)
        self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]

    def query_range(self, node, start, end, left, right):
        """Query sum in range [left, right]"""
        if start > right or end < left:
            return 0

        self.push(node, start, end)

        if start >= left and end <= right:
            return self.tree[node]

        mid = (start + end) // 2
        left_sum = self.query_range(2 * node, start, mid, left, right)
        right_sum = self.query_range(2 * node + 1, mid + 1, end, left, right)
        return left_sum + right_sum

    # Public interface
    def range_add(self, left, right, val):
        """Add val to range [left, right]"""
        self.update_range(1, 0, self.n - 1, left, right, val)

    def range_sum(self, left, right):
        """Get sum in range [left, right]"""
        return self.query_range(1, 0, self.n - 1, left, right)
```

### 模板 4：二維樹狀陣列 — LC 308
```python
class BIT2D:
    """2D Binary Indexed Tree for 2D range sum queries"""

    def __init__(self, rows, cols):
        self.rows = rows
        self.cols = cols
        self.tree = [[0] * (cols + 1) for _ in range(rows + 1)]

    def update(self, row, col, delta):
        """Add delta to element at (row, col)"""
        orig_col = col
        while row <= self.rows:
            col = orig_col
            while col <= self.cols:
                self.tree[row][col] += delta
                col += col & (-col)
            row += row & (-row)

    def query(self, row, col):
        """Get sum from (1,1) to (row, col)"""
        total = 0
        orig_col = col
        while row > 0:
            col = orig_col
            while col > 0:
                total += self.tree[row][col]
                col -= col & (-col)
            row -= row & (-row)
        return total

    def range_query(self, row1, col1, row2, col2):
        """Get sum in rectangle from (row1, col1) to (row2, col2)"""
        return (self.query(row2, col2) -
                self.query(row1 - 1, col2) -
                self.query(row2, col1 - 1) +
                self.query(row1 - 1, col1 - 1))
```

## LeetCode 題目與解法

### **區間和查詢題**
| 題目 | LC # | 資料結構 | 難度 | 關鍵技巧 |
|---------|------|----------------|------------|---------------|
| Range Sum Query - Immutable | 303 | 前綴和 | Easy | 單純的前綴陣列 |
| Range Sum Query - Mutable | 307 | BIT／線段樹 | Medium | 單點更新、區間查詢 |
| Range Sum Query 2D - Immutable | 304 | 二維前綴和 | Medium | 二維前綴陣列 |
| Range Sum Query 2D - Mutable | 308 | 二維 BIT | Hard | 二維單點更新、區間查詢 |

### **順序統計量題**
| 題目 | LC # | 資料結構 | 難度 | 關鍵技巧 |
|---------|------|----------------|------------|---------------|
| Count of Smaller Numbers After Self | 315 | BIT + 壓縮 | Hard | 座標壓縮 |
| Reverse Pairs | 493 | BIT／合併排序 | Hard | 數逆序對 |
| Count of Range Sum | 327 | BIT + 前綴和 | Hard | 座標壓縮 |

### Range Sum Query - Mutable — LC 307
```python
class NumArray:
    """Range Sum Query with updates using BIT"""

    def __init__(self, nums):
        self.nums = [0] + nums  # Make 1-indexed
        self.bit = BIT(len(nums))

        # Build BIT
        for i in range(1, len(self.nums)):
            self.bit.update(i, self.nums[i])

    def update(self, index, val):
        """Update element at index to val"""
        index += 1  # Convert to 1-indexed
        delta = val - self.nums[index]
        self.nums[index] = val
        self.bit.update(index, delta)

    def sumRange(self, left, right):
        """Sum elements from left to right"""
        return self.bit.range_query(left + 1, right + 1)

# Alternative using Segment Tree
class NumArraySegTree:
    def __init__(self, nums):
        self.seg_tree = SegmentTree(nums)
        self.nums = nums

    def update(self, index, val):
        self.nums[index] = val
        self.seg_tree.point_update(index, val)

    def sumRange(self, left, right):
        return self.seg_tree.range_sum(left, right)
```

### Count of Smaller Numbers After Self — LC 315
```python
def countSmaller(nums):
    """Count smaller numbers after self using BIT"""
    if not nums:
        return []

    # Coordinate compression
    sorted_nums = sorted(set(nums))
    rank = {num: i + 1 for i, num in enumerate(sorted_nums)}

    bit = BIT(len(sorted_nums))
    result = []

    # Process from right to left
    for i in range(len(nums) - 1, -1, -1):
        # Count numbers smaller than nums[i]
        count = bit.query(rank[nums[i]] - 1) if rank[nums[i]] > 1 else 0
        result.append(count)

        # Add current number to BIT
        bit.update(rank[nums[i]], 1)

    return result[::-1]  # Reverse to get correct order

# Alternative using merge sort
def countSmallerMergeSort(nums):
    """Using merge sort to count inversions"""
    def mergeSort(arr):
        if len(arr) <= 1:
            return arr, [0] * len(arr)

        mid = len(arr) // 2
        left, left_counts = mergeSort(arr[:mid])
        right, right_counts = mergeSort(arr[mid:])

        merged = []
        counts = [0] * len(arr)
        i = j = 0

        while i < len(left) and j < len(right):
            if left[i][0] <= right[j][0]:
                merged.append(left[i])
                counts[left[i][1]] += j  # j elements from right are smaller
                i += 1
            else:
                merged.append(right[j])
                j += 1

        while i < len(left):
            merged.append(left[i])
            counts[left[i][1]] += j
            i += 1

        while j < len(right):
            merged.append(right[j])
            j += 1

        return merged, counts

    # Create (value, original_index) pairs
    indexed_nums = [(nums[i], i) for i in range(len(nums))]
    _, counts = mergeSort(indexed_nums)
    return counts
```

### Reverse Pairs — LC 493
```python
def reversePairs(nums):
    """Count reverse pairs using BIT and coordinate compression"""
    if not nums:
        return 0

    # Get all possible values (including doubled values)
    values = set(nums)
    for num in nums:
        values.add(2 * num)

    # Coordinate compression
    sorted_values = sorted(values)
    rank = {val: i + 1 for i, val in enumerate(sorted_values)}

    bit = BIT(len(sorted_values))
    count = 0

    for num in reversed(nums):
        # Count how many numbers > 2 * num are already seen
        target_rank = rank[2 * num]
        # Query from target_rank+1 to end
        if target_rank < len(sorted_values):
            count += bit.query(len(sorted_values)) - bit.query(target_rank)

        # Add current number to BIT
        bit.update(rank[num], 1)

    return count

# Alternative merge sort approach
def reversePairsMergeSort(nums):
    def mergeSort(arr, start, end):
        if start >= end:
            return 0

        mid = (start + end) // 2
        count = mergeSort(arr, start, mid) + mergeSort(arr, mid + 1, end)

        # Count reverse pairs
        j = mid + 1
        for i in range(start, mid + 1):
            while j <= end and arr[i] > 2 * arr[j]:
                j += 1
            count += j - (mid + 1)

        # Merge sorted arrays
        arr[start:end + 1] = sorted(arr[start:end + 1])
        return count

    return mergeSort(nums, 0, len(nums) - 1)
```

### Count of Range Sum — LC 327
```python
def countRangeSum(nums, lower, upper):
    """Count range sums in [lower, upper] using BIT"""
    if not nums:
        return 0

    # Compute prefix sums
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    # Get all relevant values for coordinate compression
    values = set(prefix_sums)
    for ps in prefix_sums:
        values.add(ps - lower)
        values.add(ps - upper)

    sorted_values = sorted(values)
    rank = {val: i + 1 for i, val in enumerate(sorted_values)}

    bit = BIT(len(sorted_values))
    count = 0

    for ps in prefix_sums:
        # Count prefix sums in range [ps - upper, ps - lower]
        left_rank = rank[ps - upper]
        right_rank = rank[ps - lower]
        count += bit.range_query(left_rank, right_rank)

        # Add current prefix sum to BIT
        bit.update(rank[ps], 1)

    return count
```

## 進階技巧

### 座標壓縮
```python
def coordinate_compress(arr):
    """Compress coordinates for BIT usage"""
    unique_vals = sorted(set(arr))
    rank_map = {val: i + 1 for i, val in enumerate(unique_vals)}
    return rank_map, len(unique_vals)

def use_compression_example():
    nums = [100, 1, 50, 200, 75]
    rank_map, max_rank = coordinate_compress(nums)
    # rank_map = {1: 1, 50: 2, 75: 3, 100: 4, 200: 5}

    bit = BIT(max_rank)
    for num in nums:
        bit.update(rank_map[num], 1)  # Add frequency
```

### 區間最大值查詢（RMQ）線段樹
```python
class RMQSegmentTree:
    """Segment Tree for Range Maximum Queries"""

    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [0] * (4 * self.n)
        self.build(arr, 1, 0, self.n - 1)

    def build(self, arr, node, start, end):
        if start == end:
            self.tree[node] = arr[start]
        else:
            mid = (start + end) // 2
            self.build(arr, 2 * node, start, mid)
            self.build(arr, 2 * node + 1, mid + 1, end)
            self.tree[node] = max(self.tree[2 * node], self.tree[2 * node + 1])

    def query_max(self, node, start, end, left, right):
        if right < start or end < left:
            return float('-inf')

        if left <= start and end <= right:
            return self.tree[node]

        mid = (start + end) // 2
        left_max = self.query_max(2 * node, start, mid, left, right)
        right_max = self.query_max(2 * node + 1, mid + 1, end, left, right)
        return max(left_max, right_max)

    def range_max(self, left, right):
        return self.query_max(1, 0, self.n - 1, left, right)
```

## 效能分析

### 時間複雜度對照
| 操作 | 樸素陣列 | BIT | 線段樹 | 稀疏表 |
|-----------|-------------|-----|--------------|--------------|
| **建構** | O(1) | O(n log n) | O(n) | O(n log n) |
| **單點更新** | O(1) | O(log n) | O(log n) | O(n) |
| **區間查詢** | O(n) | O(log n) | O(log n) | O(1) |
| **區間更新** | O(n) | O(log n) | O(log n) | O(n) |

### 空間複雜度
- **BIT**：O(n) —— 非常省空間
- **線段樹**：O(4n) —— 比較吃空間，但彈性大得多
- **二維 BIT**：O(n×m) —— 隨維度平方成長
- **懶惰線段樹**：O(4n) —— 跟一般線段樹一樣

## 實作提醒

### 常見陷阱與解法
```python
def bit_pitfalls():
    """Common BIT implementation mistakes"""

    # ❌ Wrong: 0-indexed BIT
    # BIT naturally works with 1-indexed arrays

    # ✅ Correct: Convert to 1-indexed
    def update_correct(bit, index, delta):
        index += 1  # Convert 0-indexed to 1-indexed
        while index <= bit.n:
            bit.tree[index] += delta
            index += index & (-index)

    # ❌ Wrong: Forgetting coordination compression
    def wrong_approach(nums):
        bit = BIT(max(nums))  # Might use too much memory

    # ✅ Correct: Use coordinate compression
    def correct_approach(nums):
        rank_map, size = coordinate_compress(nums)
        bit = BIT(size)
        for num in nums:
            bit.update(rank_map[num], 1)

def segment_tree_tips():
    """Segment Tree best practices"""

    # Use 4n space allocation for safety
    tree = [0] * (4 * n)

    # Handle edge cases properly
    def query(node, start, end, left, right):
        if right < start or end < left:
            return 0  # Return identity element
        # ... rest of query logic
```

## 總結與速查

### 各結構的使用時機

| 使用情境 | 最佳選擇 | 原因 |
|----------|-------------|-----|
| **區間和 + 單點更新** | BIT | 簡單、省空間 |
| **區間 min/max + 更新** | 線段樹 | 任何滿足結合律的運算都撐得住 |
| **區間更新** | 懶惰線段樹 | 批次更新很有效率 |
| **二維區間查詢** | 二維 BIT | 自然的延伸 |
| **數逆序對** | BIT + 壓縮 | 順序統計量的絕配 |

### 實作檢查清單
- [ ] **BIT**：記得是 1-indexed，數值很大時要先做座標壓縮
- [ ] **線段樹**：配置 4n 空間，處理好查詢的邊界情況
- [ ] **懶惰標記**：push 要寫對，子節點要延後更新
- [ ] **二維結構**：注意記憶體用量，先拿小例子測過

### LeetCode 題型分類
- **區間和**：LC 303, 307, 308（BIT／線段樹）
- **順序統計量**：LC 315, 327, 493（BIT + 壓縮）
- **動態規劃**：搭配 RMQ 最佳化的區間 DP
- **幾何**：二維區間查詢、矩形類題目

## LC 範例

### 2-1) Range Sum Query - Mutable (LC 307) — 線段樹
> 線段樹支援 O(log N) 的區間和查詢與單點更新。

```java
// LC 307 - Range Sum Query - Mutable
// IDEA: Segment Tree — build, update, query in O(log N)
// time = O(log N) per op, space = O(N)
class NumArray {
    int[] tree;
    int n;
    public NumArray(int[] nums) {
        n = nums.length;
        tree = new int[2 * n];
        // build leaves
        for (int i = 0; i < n; i++) tree[n + i] = nums[i];
        // build internal nodes
        for (int i = n - 1; i >= 1; i--) tree[i] = tree[2*i] + tree[2*i+1];
    }
    public void update(int i, int val) {
        tree[n + i] = val;
        for (int pos = (n + i) >> 1; pos >= 1; pos >>= 1)
            tree[pos] = tree[2*pos] + tree[2*pos+1];
    }
    public int sumRange(int l, int r) {
        int sum = 0;
        for (l += n, r += n + 1; l < r; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) sum += tree[l++];
            if ((r & 1) == 1) sum += tree[--r];
        }
        return sum;
    }
}
```

### 2-2) My Calendar I (LC 729) — 用線段樹／TreeMap 做行程預約
> 用 TreeMap 檢查新預約是否跟既有預約重疊。

```java
// LC 729 - My Calendar I
// IDEA: TreeMap — check overlap with floorKey and ceilingKey
// time = O(log N) per booking, space = O(N)
class MyCalendar {
    TreeMap<Integer, Integer> calendar = new TreeMap<>();
    public boolean book(int start, int end) {
        Integer prev = calendar.floorKey(start);
        Integer next = calendar.ceilingKey(start);
        // No overlap if: prev booking ends before start, AND next booking starts after end
        if ((prev == null || calendar.get(prev) <= start) &&
            (next == null || next >= end)) {
            calendar.put(start, end);
            return true;
        }
        return false;
    }
}
```

這份完整指南涵蓋了線段樹與樹狀陣列的核心概念與實作，並搭配 LeetCode 題目的實際範例。

### 2-3) 線段樹模板 — 帶懶惰標記的區間和
> 完整的線段樹，區間更新與區間查詢都是 O(log N)。

```java
// Segment Tree — Lazy Propagation Template
// time = O(log N) per update/query, space = O(N)
class SegTree {
    int[] tree, lazy;
    int n;
    SegTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        lazy = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }
    private void build(int[] nums, int node, int l, int r) {
        if (l == r) { tree[node] = nums[l]; return; }
        int mid = (l + r) / 2;
        build(nums, 2*node+1, l, mid);
        build(nums, 2*node+2, mid+1, r);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    private void pushDown(int node, int l, int r) {
        if (lazy[node] != 0) {
            int mid = (l + r) / 2;
            tree[2*node+1] += (mid-l+1) * lazy[node]; lazy[2*node+1] += lazy[node];
            tree[2*node+2] += (r-mid)   * lazy[node]; lazy[2*node+2] += lazy[node];
            lazy[node] = 0;
        }
    }
    void update(int node, int l, int r, int ql, int qr, int val) {
        if (ql > r || qr < l) return;
        if (ql <= l && r <= qr) { tree[node] += (r-l+1)*val; lazy[node] += val; return; }
        pushDown(node, l, r);
        int mid = (l + r) / 2;
        update(2*node+1, l, mid, ql, qr, val);
        update(2*node+2, mid+1, r, ql, qr, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    int query(int node, int l, int r, int ql, int qr) {
        if (ql > r || qr < l) return 0;
        if (ql <= l && r <= qr) return tree[node];
        pushDown(node, l, r);
        int mid = (l + r) / 2;
        return query(2*node+1, l, mid, ql, qr) + query(2*node+2, mid+1, r, ql, qr);
    }
}
```

### 2-4) Corporate Flight Bookings (LC 1109) — 差分陣列
> 對 [first, last] 這段加上乘客數；最後做前綴和得到每個航班的總數。

```java
// LC 1109 - Corporate Flight Bookings
// IDEA: Difference array — range add O(1), prefix sum O(N) for result
// time = O(N + Q), space = O(N)
public int[] corpFlightBookings(int[][] bookings, int n) {
    int[] diff = new int[n + 1];
    for (int[] b : bookings) {
        diff[b[0] - 1] += b[2];
        if (b[1] < n) diff[b[1]] -= b[2];
    }
    for (int i = 1; i < n; i++) diff[i] += diff[i-1];
    return Arrays.copyOf(diff, n);
}
```

### 2-5) Count of Smaller Numbers After Self (LC 315) — 迭代式線段樹
> 在值域上建線段樹；由右往左插入；查詢比目前值小的前綴計數。

```java
// LC 315 - Count of Smaller Numbers After Self (Segment Tree on values)
// IDEA: Iterative seg tree on [0, 20001] value range; query prefix, then update
// time = O(N log M), space = O(M)
public List<Integer> countSmaller(int[] nums) {
    int offset = 10001, size = 2 * offset + 1;
    int[] tree = new int[2 * size];
    Integer[] res = new Integer[nums.length];
    for (int i = nums.length - 1; i >= 0; i--) {
        int val = nums[i] + offset;
        res[i] = queryTree(tree, size, 0, val - 1);
        updateTree(tree, size, val);
    }
    return Arrays.asList(res);
}
private void updateTree(int[] t, int n, int i) { for (i+=n; i>0; i>>=1) t[i]++; }
private int  queryTree(int[] t, int n, int l, int r) {
    int s=0; for(l+=n,r+=n+1; l<r; l>>=1,r>>=1) {if((l&1)==1)s+=t[l++];if((r&1)==1)s+=t[--r];} return s;
}
```

### 2-6) Falling Squares (LC 699) — 線段樹取最大值
> 每個方塊會落在它涵蓋範圍內最高的既有高度上；一路追蹤目前的最大高度。

```java
// LC 699 - Falling Squares (naive O(N^2); segment tree gives O(N log N))
// IDEA: For each square compute max height in its column range, then update
// time = O(N^2), space = O(N)
public List<Integer> fallingSquares(int[][] positions) {
    List<Integer> ans = new ArrayList<>();
    int[] heights = new int[positions.length];
    int maxH = 0;
    for (int i = 0; i < positions.length; i++) {
        int l = positions[i][0], sz = positions[i][1], r = l + sz;
        heights[i] = sz;
        for (int j = 0; j < i; j++) {
            int lj = positions[j][0], rj = lj + positions[j][1];
            if (lj < r && l < rj)                    // overlap
                heights[i] = Math.max(heights[i], heights[j] + sz);
        }
        maxH = Math.max(maxH, heights[i]);
        ans.add(maxH);
    }
    return ans;
}
```

### 2-7) Maximum Sum Rectangle No Larger Than K (LC 363) — 前綴和 + TreeSet
> 固定左右行界；壓縮成一維的列和；用 TreeSet 找出 ≤ k 的最大和。

```java
// LC 363 - Max Sum of Rectangle No Larger Than K
// IDEA: Fix left/right cols; 1D Kadane + TreeSet for sum <= k constraint
// time = O(M^2 * N log N), space = O(N)
public int maxSumSubmatrix(int[][] matrix, int k) {
    int m = matrix.length, n = matrix[0].length, ans = Integer.MIN_VALUE;
    for (int l = 0; l < n; l++) {
        int[] rowSum = new int[m];
        for (int r = l; r < n; r++) {
            for (int i = 0; i < m; i++) rowSum[i] += matrix[i][r];
            TreeSet<Integer> set = new TreeSet<>();
            set.add(0);
            int curr = 0;
            for (int s : rowSum) {
                curr += s;
                Integer ceiling = set.ceiling(curr - k);
                if (ceiling != null) ans = Math.max(ans, curr - ceiling);
                set.add(curr);
            }
        }
    }
    return ans;
}
```

### 2-8) Range Module (LC 715) — 線段樹／TreeMap
> 追蹤哪些區間被納入；高效地新增、移除與查詢區間。

```java
// LC 715 - Range Module (TreeMap approach)
// IDEA: TreeMap<start, end> — merge on addRange, split on removeRange
// time = O(N log N) per op, space = O(N)
class RangeModule {
    TreeMap<Integer, Integer> map = new TreeMap<>();
    public void addRange(int left, int right) {
        Integer lo = map.floorKey(left), hi = map.floorKey(right);
        if (lo != null && map.get(lo) >= left) left = lo;
        if (hi != null && map.get(hi) > right) right = map.get(hi);
        map.subMap(left, right).clear();
        map.put(left, right);
    }
    public boolean queryRange(int left, int right) {
        Integer lo = map.floorKey(left);
        return lo != null && map.get(lo) >= right;
    }
    public void removeRange(int left, int right) {
        Integer lo = map.floorKey(left), hi = map.floorKey(right);
        if (hi != null && map.get(hi) > right) map.put(right, map.get(hi));
        if (lo != null && map.get(lo) > left)  map.put(lo, left);
        map.subMap(left, right).clear();
    }
}
```

### 2-9) Longest Increasing Subsequence (LC 300) — 在值域上建線段樹
> 對壓縮後的值建線段樹；查詢所有小於目前值的最大 LIS 長度，再回頭更新。

```java
// LC 300 - LIS via Segment Tree (max query on value range)
// IDEA: Compress values; seg tree max query for all smaller values; update at current
// time = O(N log N), space = O(N)
public int lengthOfLIS(int[] nums) {
    int[] sorted = nums.clone();
    Arrays.sort(sorted);
    Map<Integer, Integer> rank = new HashMap<>();
    int r = 1;
    for (int v : sorted) if (!rank.containsKey(v)) rank.put(v, r++);
    int n = r - 1;
    int[] tree = new int[2 * (n + 1)];
    int ans = 0;
    for (int num : nums) {
        int pos = rank.get(num);
        int best = qmax(tree, n, 1, pos - 1) + 1;
        ans = Math.max(ans, best);
        umax(tree, n, pos, best);
    }
    return ans;
}
private int  qmax(int[] t, int n, int l, int r) { int res=0; for(l+=n,r+=n+1;l<r;l>>=1,r>>=1){if((l&1)==1)res=Math.max(res,t[l++]);if((r&1)==1)res=Math.max(res,t[--r]);}return res; }
private void umax(int[] t, int n, int i, int v) { for(i+=n;i>0;i>>=1) t[i]=Math.max(t[i],v); }
```

### 2-10) My Calendar II (LC 731) — 線段樹／TreeMap
> 最多允許兩個預約重疊；一旦會出現第三次重疊就拒絕。

```java
// LC 731 - My Calendar II
// IDEA: Two TreeMaps — bookings and double-bookings; reject if new interval hits double-booked region
// time = O(N^2) worst, space = O(N)
class MyCalendarTwo {
    List<int[]> single = new ArrayList<>(), overlap = new ArrayList<>();
    public boolean book(int start, int end) {
        for (int[] o : overlap)
            if (o[0] < end && start < o[1]) return false;
        for (int[] s : single) {
            int lo = Math.max(s[0], start), hi = Math.min(s[1], end);
            if (lo < hi) overlap.add(new int[]{lo, hi});
        }
        single.add(new int[]{start, end});
        return true;
    }
}
```

### 2-11) Rectangle Area II (LC 850) — 掃描線 + 「覆蓋長度」線段樹 ⭐⭐⭐⭐⭐

> **模式**：讓一條垂直線沿著 x 掃過去；在**壓縮後的 y** 上建線段樹，回答*「目前 y 軸有多長被至少一個矩形蓋住？」*
>
> **關鍵想法**：這是唯一一種**沒有懶惰標記、也沒有 push-down** 的線段樹。每個節點存 `cnt` = 有幾個作用中的矩形完整蓋住這個節點*整段*區間，以及 `cover` = 這段裡被覆蓋的長度。`cnt` 永遠不會往子節點推 —— 它只會在同一組節點上加減（每個 `+1` 之後一定有一個完全對應的 `-1`），所以一個節點的真實狀態是*「我自己的 `cnt` 再加上祖先們貢獻的」*。這也是為什麼 `cover` 只有**從根節點讀**才有意義。

```text
pull(node):
    cnt[node] > 0  ->  cover = ys[r+1] - ys[l]      # fully covered by an active rect
    leaf           ->  cover = 0
    else           ->  cover = cover[left] + cover[right]
```

```java
// java
// LC 850 - Rectangle Area II
// IDEA: sweep x; seg tree over compressed y tracks covered length via a cnt field (no push-down)
// time = O(N log N), space = O(N)
class Solution {
    long[] cover;   // covered y-length inside this node
    int[] cnt;      // # active rectangles covering this node's WHOLE interval
    int[] ys;

    public int rectangleArea(int[][] rectangles) {
        final int MOD = 1_000_000_007;
        TreeSet<Integer> set = new TreeSet<>();                 // coordinate compression on y
        for (int[] r : rectangles) { set.add(r[1]); set.add(r[3]); }
        ys = new int[set.size()];
        int k = 0;
        for (int y : set) ys[k++] = y;
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < ys.length; i++) idx.put(ys[i], i);
        int m = ys.length - 1;                                  // # elementary y-segments
        cover = new long[4 * m];
        cnt   = new int[4 * m];

        int[][] ev = new int[rectangles.length * 2][];          // {x, yLo, yHi, +1/-1}
        int p = 0;
        for (int[] r : rectangles) {
            ev[p++] = new int[]{r[0], idx.get(r[1]), idx.get(r[3]),  1};   // left edge  -> open
            ev[p++] = new int[]{r[2], idx.get(r[1]), idx.get(r[3]), -1};   // right edge -> close
        }
        Arrays.sort(ev, (a, b) -> a[0] - b[0]);

        long area = 0;
        int prevX = ev[0][0];
        for (int i = 0; i < ev.length; ) {
            int x = ev[i][0];
            area = (area + cover[0] % MOD * ((x - prevX) % MOD)) % MOD;    // strip [prevX, x]
            while (i < ev.length && ev[i][0] == x) {            // apply ALL events at this x
                update(0, 0, m - 1, ev[i][1], ev[i][2] - 1, ev[i][3]);
                i++;
            }
            prevX = x;
        }
        return (int) area;
    }

    private void update(int node, int l, int r, int ql, int qr, int val) {
        if (qr < l || r < ql) return;
        if (ql <= l && r <= qr) { cnt[node] += val; pull(node, l, r); return; }
        int mid = (l + r) >>> 1;
        update(2*node+1, l, mid,   ql, qr, val);
        update(2*node+2, mid+1, r, ql, qr, val);
        pull(node, l, r);
    }

    private void pull(int node, int l, int r) {                 // no lazy push-down needed
        if (cnt[node] > 0) cover[node] = ys[r+1] - ys[l];
        else if (l == r)   cover[node] = 0;
        else               cover[node] = cover[2*node+1] + cover[2*node+2];
    }
}
```

```python
# python
# LC 850 - Rectangle Area II
# IDEA: sweep x; seg tree over compressed y tracks covered length via a cnt field (no push-down)
# time = O(N log N), space = O(N)
def rectangleArea(rectangles):
    MOD = 10 ** 9 + 7
    ys = sorted({y for r in rectangles for y in (r[1], r[3])})   # coordinate compression
    idx = {y: i for i, y in enumerate(ys)}
    m = len(ys) - 1                                              # # elementary y-segments
    cover = [0] * (4 * m)
    cnt   = [0] * (4 * m)

    def pull(node, l, r):                       # no lazy push-down needed
        if cnt[node] > 0: cover[node] = ys[r + 1] - ys[l]
        elif l == r:      cover[node] = 0
        else:             cover[node] = cover[2*node+1] + cover[2*node+2]

    def update(node, l, r, ql, qr, val):
        if qr < l or r < ql: return
        if ql <= l and r <= qr:
            cnt[node] += val
            pull(node, l, r)
            return
        mid = (l + r) // 2
        update(2*node+1, l, mid,   ql, qr, val)
        update(2*node+2, mid+1, r, ql, qr, val)
        pull(node, l, r)

    events = []
    for x1, y1, x2, y2 in rectangles:
        events.append((x1, idx[y1], idx[y2],  1))                # open
        events.append((x2, idx[y1], idx[y2], -1))                # close
    events.sort()

    area, prev_x, i = 0, events[0][0], 0
    while i < len(events):
        x = events[i][0]
        area += cover[0] * (x - prev_x)                          # strip [prev_x, x]
        while i < len(events) and events[i][0] == x:             # apply ALL events at this x
            _, lo, hi, val = events[i]
            update(0, 0, m - 1, lo, hi - 1, val)
            i += 1
        prev_x = x
    return area % MOD
```

**容易踩到的坑**
- 一個橫跨 `y in [ys[a], ys[b]]` 的矩形，對應到的基本線段索引是 `[a, b-1]`（是 `b-1`，不是 `b`）—— 線段活在壓縮座標*之間*。
- 量下一條長條之前，要先把同一個 x 上的**所有**事件處理完。
- `cover[0] * dx` 可能達到 `1e9 * 1e9` → Java 要用 `long`；取模留到最後（或每條長條）再做，絕對不要對線段樹裡的值取模。

**變形 —— 區間賦值 + 區間最大值（LC 699 Falling Squares）**：掃描骨架一模一樣，只是樹上存的是*高度*，並帶一個「assign」的懶惰標記。請直接看 [`difference_array.md`](difference_array.md) 裡的懶惰區間賦值模板，不要在這裡重抄一份；`O(N^2)` 的基準解已經在上面的 [2-6](#2-6-falling-squares-lc-699--segment-tree-max)。

### 2-12) The Skyline Problem (LC 218) — 掃描線 + 最大堆積（用不到線段樹） ⭐⭐⭐⭐⭐

> 這一頁裡出現頻率最高的題目（Google／Meta／Amazon／Microsoft／Apple／Uber）。它*看起來*像線段樹題，標籤也是這樣掛的，但期待的答案是**掃描線 + 帶懶惰刪除的最大堆積** —— 先把這個寫出來；線段樹版本複雜度一樣，程式碼卻多得多。
>
> **關鍵想法**：把事件依 x 排序，**同一個 x 上起始事件排在結束事件前面**，並維護一個裝著「目前還活著的高度」的堆積。只有最大高度改變時才吐出一個關鍵點。

```java
// java
// LC 218 - The Skyline Problem
// IDEA: sweep x; max-heap of {height, end} with lazy deletion; emit on max-height change
// time = O(N log N), space = O(N)
public List<List<Integer>> getSkyline(int[][] buildings) {
    List<int[]> ev = new ArrayList<>();
    for (int[] b : buildings) {
        ev.add(new int[]{b[0], -b[2], b[1]});      // start: negative height => sorts before ends
        ev.add(new int[]{b[1], 0, 0});             // end marker (only wakes the sweep up)
    }
    ev.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);   // {height, end}
    pq.offer(new int[]{0, Integer.MAX_VALUE});     // ground sentinel, never popped
    List<List<Integer>> res = new ArrayList<>();
    for (int[] e : ev) {
        int x = e[0];
        while (pq.peek()[1] <= x) pq.poll();       // lazy delete: drop buildings already ended
        if (e[1] < 0) pq.offer(new int[]{-e[1], e[2]});
        int h = pq.peek()[0];
        if (res.isEmpty() || res.get(res.size() - 1).get(1) != h)
            res.add(Arrays.asList(x, h));          // skyline changed -> key point
    }
    return res;
}
```

```python
# python
# LC 218 - The Skyline Problem
# IDEA: sweep x; max-heap of (-height, end) with lazy deletion; emit on max-height change
# time = O(N log N), space = O(N)
import heapq

def getSkyline(buildings):
    events = [(l, -h, r) for l, r, h in buildings] + [(r, 0, 0) for _, r, _ in buildings]
    events.sort()                                  # start (-h < 0) sorts before end (0) at same x
    res, heap = [], [(0, float('inf'))]            # max-heap via negated heights + ground sentinel
    for x, neg_h, r in events:
        while heap[0][1] <= x:                     # lazy delete: drop buildings already ended
            heapq.heappop(heap)
        if neg_h < 0:
            heapq.heappush(heap, (neg_h, r))
        h = -heap[0][0]
        if not res or res[-1][1] != h:
            res.append([x, h])                     # skyline changed -> key point
    return res
```

**為什麼那些排序決勝規則很重要**
- 同一個 x、start 排在 end 前面 → 兩棟等高又相鄰的建築不會吐出多餘的 `0`。
- 懶惰刪除（只在*堆頂*過期時才彈出）讓你不必實作帶索引的堆積；堆頂底下那些過期項目無害，因為我們只會讀最大值。
- 地面的哨兵 `(0, +inf)` 讓堆積不可能變空，所以最後那個 `[x, 0]` 自然就掉出來了。

### 2-13) Number of Longest Increasing Subsequence (LC 673) — 自訂合併的線段樹 ⭐⭐⭐⭐

> 這題是 [2-9](#2-9-longest-increasing-subsequence-lc-300--segment-tree-on-values)（LC 300 用值域線段樹解 LIS）的延伸 —— 差別在於節點現在存的是一個**配對** `(bestLen, count)`，而 `merge` 不是 `+` 也不是 `max`，而是*「留比較長的；長度相同就把個數相加」*。
>
> **關鍵想法**：只要滿足結合律，任何合併方式線段樹都吃得下。由左往右掃；對值 `v`，查詢所有嚴格更小的排名上的最大長度與其個數，長度加 1，再對 rank(`v`) 做單點更新。

```java
// java
// LC 673 - Number of Longest Increasing Subsequence
// IDEA: seg tree on compressed values; node = (maxLen, count), merge keeps longer / sums ties
// time = O(N log N), space = O(N)
class Solution {
    int[] len, cnt;
    int n;

    public int findNumberOfLIS(int[] nums) {
        int[] s = nums.clone();
        Arrays.sort(s);
        Map<Integer, Integer> rank = new HashMap<>();
        int r = 0;
        for (int v : s) if (!rank.containsKey(v)) rank.put(v, r++);   // coordinate compression
        n = r;
        len = new int[4*n];
        cnt = new int[4*n];

        int bestLen = 0, bestCnt = 0;
        for (int num : nums) {
            int pos = rank.get(num);
            int[] q = pos == 0 ? new int[]{0, 0} : query(0, 0, n-1, 0, pos-1);  // strictly smaller
            int nl = q[0] + 1, nc = q[0] > 0 ? q[1] : 1;   // no smaller value -> chain of length 1
            update(0, 0, n-1, pos, nl, nc);
            if (nl > bestLen)       { bestLen = nl; bestCnt = nc; }
            else if (nl == bestLen) { bestCnt += nc; }
        }
        return bestCnt;
    }

    private int[] merge(int[] a, int[] b) {            // <-- the only non-standard part
        if (a[0] > b[0]) return a;
        if (b[0] > a[0]) return b;
        return new int[]{a[0], a[1] + b[1]};           // tie: accumulate counts
    }

    private void update(int node, int l, int r, int i, int nl, int nc) {
        if (l == r) {                                  // leaf ACCUMULATES (not overwrite)
            if (nl > len[node])       { len[node] = nl; cnt[node] = nc; }
            else if (nl == len[node]) { cnt[node] += nc; }
            return;
        }
        int mid = (l + r) >>> 1;
        if (i <= mid) update(2*node+1, l, mid,   i, nl, nc);
        else          update(2*node+2, mid+1, r, i, nl, nc);
        int[] m = merge(new int[]{len[2*node+1], cnt[2*node+1]},
                        new int[]{len[2*node+2], cnt[2*node+2]});
        len[node] = m[0]; cnt[node] = m[1];
    }

    private int[] query(int node, int l, int r, int ql, int qr) {
        if (qr < l || r < ql) return new int[]{0, 0};  // identity element
        if (ql <= l && r <= qr) return new int[]{len[node], cnt[node]};
        int mid = (l + r) >>> 1;
        return merge(query(2*node+1, l, mid,   ql, qr),
                     query(2*node+2, mid+1, r, ql, qr));
    }
}
```

```python
# python
# LC 673 - Number of Longest Increasing Subsequence
# IDEA: seg tree on compressed values; node = (maxLen, count), merge keeps longer / sums ties
# time = O(N log N), space = O(N)
def findNumberOfLIS(nums):
    ranks = {v: i for i, v in enumerate(sorted(set(nums)))}     # coordinate compression
    n = len(ranks)
    length = [0] * (4 * n)
    count  = [0] * (4 * n)

    def merge(a, b):                            # <-- the only non-standard part
        if a[0] > b[0]: return a
        if b[0] > a[0]: return b
        return (a[0], a[1] + b[1])              # tie: accumulate counts

    def update(node, l, r, i, nl, nc):
        if l == r:                              # leaf ACCUMULATES (not overwrite)
            if nl > length[node]:    length[node], count[node] = nl, nc
            elif nl == length[node]: count[node] += nc
            return
        mid = (l + r) // 2
        if i <= mid: update(2*node+1, l, mid,   i, nl, nc)
        else:        update(2*node+2, mid+1, r, i, nl, nc)
        length[node], count[node] = merge((length[2*node+1], count[2*node+1]),
                                          (length[2*node+2], count[2*node+2]))

    def query(node, l, r, ql, qr):
        if qr < l or r < ql: return (0, 0)      # identity element
        if ql <= l and r <= qr: return (length[node], count[node])
        mid = (l + r) // 2
        return merge(query(2*node+1, l, mid,   ql, qr),
                     query(2*node+2, mid+1, r, ql, qr))

    best_len = best_cnt = 0
    for num in nums:
        pos = ranks[num]
        ql, qc = query(0, 0, n - 1, 0, pos - 1) if pos > 0 else (0, 0)
        nl, nc = ql + 1, (qc if ql > 0 else 1)  # no smaller value -> new chain of length 1
        update(0, 0, n - 1, pos, nl, nc)
        if nl > best_len:    best_len, best_cnt = nl, nc
        elif nl == best_len: best_cnt += nc
    return best_cnt
```

**兩個陷阱**
- 查詢結果為空時是 `(0, 0)`，但一個沒有更小前驅的值，它開的鏈個數是 **1** 而不是 0 —— 更新前要先把 `len == 0` 對應成 `count = 1`。
- 葉節點的更新必須跟原本的值**合併**（同一個值可能出現很多次），不能直接覆寫。用 `[2,2,2,2,2] -> 5` 就能抓到這個 bug。

> `N <= 2000` 時，用 `(len[i], cnt[i])` 寫 `O(N^2)` DP 才是面試預期的答案；只有被要求 `O(N log N)` 時才動用這棵樹。

**變形 —— LC 1157 Online Majority Element In Subarray**：骨架一模一樣，只是節點換成存 **Boyer–Moore 投票配對** `(candidate, votes)`，讓 `merge` 把對立的票抵銷掉。某段區間合併出來的根節點會給出*唯一可能*的多數候選；再用一個 `value -> 排序過的索引清單` 的表，在 `[left, right]` 上二分搜尋來驗證它。

### 2-14) 線段樹下降 — 找第 k 個空位（LC 406） ⭐⭐⭐⭐

> **模式**：樹上存的是*可用空位的數量*；與其用 `O(log N)` 的區間查詢去二分搜尋（會變成 `O(log^2 N)`），不如**在樹上一路往下走一次**，每層拿 `k` 跟左子樹的計數比。這個「在樹上下降」的技巧，是*找第 k 個 1／第 k 個空位／和 ≥ k 的最短前綴*這類問題的標準答案。
>
> **LC 406 的關鍵想法**：依身高**遞增**排序，同高則以 k **遞減**決勝。排在後面的人都至少一樣高，所以對於 `(h, k)` 這個人，剩下的空位中必須剛好有 `k` 個排在他前面 → 把他放進第 `(k+1)` 個空位。

```java
// java
// LC 406 - Queue Reconstruction by Height
// IDEA: sort by (height asc, k desc); place each person in the (k+1)-th still-empty slot via descent
// time = O(N log N), space = O(N)
class Solution {
    int[] empty;   // # empty slots in this node's index range

    public int[][] reconstructQueue(int[][] people) {
        int n = people.length;
        empty = new int[4 * n];
        build(0, 0, n - 1);
        // ties on height MUST be k-descending: a shorter-k person placed first would occupy
        // a non-empty slot that still counts toward the larger-k person's k.
        Arrays.sort(people, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
        int[][] res = new int[n][];
        for (int[] p : people) res[kth(0, 0, n - 1, p[1] + 1)] = p;
        return res;
    }

    private void build(int node, int l, int r) {
        empty[node] = r - l + 1;
        if (l == r) return;
        int mid = (l + r) >>> 1;
        build(2*node+1, l, mid);
        build(2*node+2, mid+1, r);
    }

    private int kth(int node, int l, int r, int k) {   // descend once: O(log N), not O(log^2 N)
        empty[node]--;                                 // consume the slot on the way down
        if (l == r) return l;
        int mid = (l + r) >>> 1;
        if (empty[2*node+1] >= k) return kth(2*node+1, l, mid, k);
        return kth(2*node+2, mid+1, r, k - empty[2*node+1]);   // skip the whole left subtree
    }
}
```

```python
# python
# LC 406 - Queue Reconstruction by Height
# IDEA: sort by (height asc, k desc); place each person in the (k+1)-th still-empty slot via descent
# time = O(N log N), space = O(N)
def reconstructQueue(people):
    n = len(people)
    empty = [0] * (4 * n)                       # # empty slots in this node's index range

    def build(node, l, r):
        empty[node] = r - l + 1
        if l == r: return
        mid = (l + r) // 2
        build(2*node+1, l, mid)
        build(2*node+2, mid+1, r)

    def kth(node, l, r, k):                     # descend once: O(log N), not O(log^2 N)
        empty[node] -= 1                        # consume the slot on the way down
        if l == r: return l
        mid = (l + r) // 2
        if empty[2*node+1] >= k:
            return kth(2*node+1, l, mid, k)
        return kth(2*node+2, mid+1, r, k - empty[2*node+1])     # skip the whole left subtree

    build(0, 0, n - 1)
    res = [None] * n
    # ties on height MUST be k-descending (see Java note)
    for h, k in sorted(people, key=lambda p: (p[0], -p[1])):
        res[kth(0, 0, n - 1, k + 1)] = [h, k]
    return res
```

> 在面試裡，貪婪的「依 (height desc, k asc) 排序」+ `list.insert(k, person)` 這種一行解才是 LC 406 的預期答案（`O(N^2)`，但大約三行）。只有在被要求打敗 `O(N^2)` 時才端出這個版本 —— 它真正的價值是那個**下降**流程，到處都用得上。

**變形 —— LC 1505 Minimum Possible Integer After at Most K Adjacent Swaps On Digits**：貪婪地取出剩餘預算內能搆到的最小數字；用 BIT／線段樹在位置上數出它前面已經被移走幾個數字，把*原始*索引換算成*目前*索引。同樣是「還在場的空位數量」這個結構，只是用前綴**查詢**而不是下降。

### 2-15) 決策筆記 — 線段樹 vs BIT vs 前綴和 vs 有序表 ⭐⭐⭐⭐⭐

> 在面試裡動用線段樹**通常是錯誤的選擇**。從上往下看這張表，停在第一個符合的列。

| 情境 | 該用什麼 | 為什麼不用線段樹 |
|-----------|-----------|------------------------|
| 靜態陣列、只要區間和 | **前綴和** | `O(N)` 建表、`O(1)` 查詢，兩行搞定 |
| 離線的區間加值，最後才全部讀出來 | **差分陣列** | 見 [`difference_array.md`](difference_array.md) —— 完全不用樹 |
| 單點更新 + 前綴／區間**和** | **BIT** | 一樣是 `O(log N)`，約 8 行，`O(N)` 空間 |
| 數比自己小的元素／逆序對 | **BIT + 座標壓縮**（或合併排序） | 你只需要求和 |
| 區間預約／新增／移除／重疊判斷 | **TreeMap／`SortedList`**（LC 715, 729, 731） | 區間很稀疏；在 `1e9` 座標上建樹得用動態節點 |
| 掃描過程中的「目前最大值」 | **帶懶惰刪除的堆積**（LC 218） | 你只會讀最大值 |
| `N <= 2000` 而且 `O(N^2)` 過得了 | **單純 DP／暴力解**（LC 673, 406, 1395） | 先寫簡單的，順口提一下樹的作法 |
| 區間 **max/min/gcd** + 單點更新 | **線段樹** | — |
| 區間**更新** + 區間查詢 | **懶惰線段樹** | — |
| 自訂的結合律合併，例如 `(len, count)`、Boyer–Moore 配對 | **線段樹** | BIT 做不了不可逆的合併 |
| 掃描線下的覆蓋長度／聯集面積 | **帶 `cnt` 的線段樹**（LC 850） | — |
| 座標大到 `1e9`、但操作次數不多 | **座標壓縮**，否則用動態／稀疏線段樹 | `4 * 1e9` 的陣列塞不下 |

**面試時請把這句話講出來**：*「BIT 八行就能給我前綴和；只有在合併不可逆（max/gcd/自訂）或需要懶惰區間更新時，我才需要線段樹。」* 這一句的價值，勝過背下八十行的模板。

**幾條參考題**（標籤掛著線段樹／BIT，但樹並不是預期解法）：
- **LC 1622 Fancy Sequence** —— 帶*仿射標記合成* `(a, b) -> (a*x + b)` 的懶惰線段樹可以解，但預期解法是每次操作 `O(1)`：維護一組全域的 `(mul, add)` 變換，並在附加值時先用**模反元素**（`pow(mul, MOD-2, MOD)`）把它除掉再存。
- **LC 1395 Count Number of Teams** —— BIT 可以對每個索引數出「前面較小／後面較大」的個數，但 `n <= 1000`，所以「固定中間那個士兵，再把兩邊的計數相乘」的 `O(N^2)` 解才是預期答案。
- **LC 1409 Queries on a Permutation With Key** —— 在 `2m` 大小的陣列上用 BIT 模擬 move-to-front；但 `m <= 10^3` 時，單純用 list 的 `index` + `pop` + `insert(0, ...)` 就會過，而且清楚太多。
