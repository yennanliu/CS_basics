# Segment Tree & Binary Indexed Tree (Fenwick Tree)

> **Scope** — Range query + range update structures — the segment tree, lazy propagation, and the BIT-vs-segment-tree-vs-merge-sort decision.
> **See also**: [binary_indexed_tree.md](./binary_indexed_tree.md) — the Fenwick tree in depth; [prefix_sum.md](./prefix_sum.md) — no updates needed; [difference_array.md](./difference_array.md) — range update, single final read.

## LeetCode Problem Lists

- [Segment Tree](https://leetcode.com/problem-list/segment-tree/)
- [Binary Indexed Tree](https://leetcode.com/problem-list/binary-indexed-tree/)

## Overview
**Segment Trees** and **Binary Indexed Trees (BIT/Fenwick Tree)** are advanced data structures for efficiently handling range queries and updates on arrays.

### Key Properties
- **Time Complexity**: O(log n) for both query and update operations
- **Space Complexity**: O(n) for BIT, O(4n) for Segment Tree
- **Core Idea**: Precompute range information in tree structure for fast queries
- **When to Use**: Range sum/min/max queries with updates, order statistics
- **Key Operations**: Range query, point/range update, build tree

### Core Characteristics
- **Range Queries**: Sum, minimum, maximum, GCD, XOR over ranges
- **Point Updates**: Modify single element efficiently
- **Range Updates**: Modify entire ranges (with lazy propagation)
- **Space-Time Tradeoff**: Extra space for faster query processing

## Problem Categories

### **Category 1: Range Sum Queries**
- **Description**: Calculate sum over ranges with updates
- **Examples**: LC 307 (Range Sum Query - Mutable), LC 308 (Range Sum Query 2D - Mutable)
- **Pattern**: Use BIT or Segment Tree for point updates, range queries

### **Category 2: Range Minimum/Maximum Queries**
- **Description**: Find min/max in ranges with updates
- **Examples**: LC 315 (Count of Smaller Numbers After Self), Custom RMQ problems
- **Pattern**: Segment Tree with min/max operations

### **Category 3: Range Updates with Lazy Propagation**
- **Description**: Update entire ranges efficiently
- **Examples**: Add value to range, set range to value
- **Pattern**: Segment Tree with lazy propagation

### **Category 4: Order Statistics & Inversions**
- **Description**: Count smaller/larger elements, inversions
- **Examples**: LC 315 (Count Smaller), LC 493 (Reverse Pairs), LC 327 (Count Range Sum)
- **Pattern**: BIT with coordinate compression or merge sort

## Data Structure Comparison

### **BIT vs Segment Tree Comparison**
| Aspect | Binary Indexed Tree | Segment Tree |
|--------|-------------------|--------------|
| **Space** | O(n) | O(4n) |
| **Implementation** | Simple, short code | More complex |
| **Operations** | Sum, XOR, OR | Any associative operation |
| **Range Updates** | Difficult | Easy with lazy propagation |
| **1-indexed** | Natural fit | Can be adapted |
| **Query Types** | Prefix queries easy | Arbitrary range queries |

## Templates & Algorithms

### Template 1: Binary Indexed Tree (Fenwick Tree)
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

### Template 2: Segment Tree (Range Sum)
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

### Template 3: Segment Tree with Lazy Propagation
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

### Template 4: 2D Binary Indexed Tree — LC 308
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

## LeetCode Problems & Solutions

### **Range Sum Query Problems**
| Problem | LC # | Data Structure | Difficulty | Key Technique |
|---------|------|----------------|------------|---------------|
| Range Sum Query - Immutable | 303 | Prefix Sum | Easy | Simple prefix array |
| Range Sum Query - Mutable | 307 | BIT/Segment Tree | Medium | Point update, range query |
| Range Sum Query 2D - Immutable | 304 | 2D Prefix Sum | Medium | 2D prefix array |
| Range Sum Query 2D - Mutable | 308 | 2D BIT | Hard | 2D point update, range query |

### **Order Statistics Problems**
| Problem | LC # | Data Structure | Difficulty | Key Technique |
|---------|------|----------------|------------|---------------|
| Count of Smaller Numbers After Self | 315 | BIT + Compression | Hard | Coordinate compression |
| Reverse Pairs | 493 | BIT/Merge Sort | Hard | Count inversions |
| Count of Range Sum | 327 | BIT + Prefix Sum | Hard | Coordinate compression |

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

## Advanced Techniques

### Coordinate Compression
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

### Range Maximum Query (RMQ) Segment Tree
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

## Performance Analysis

### Time Complexity Comparison
| Operation | Naive Array | BIT | Segment Tree | Sparse Table |
|-----------|-------------|-----|--------------|--------------|
| **Build** | O(1) | O(n log n) | O(n) | O(n log n) |
| **Point Update** | O(1) | O(log n) | O(log n) | O(n) |
| **Range Query** | O(n) | O(log n) | O(log n) | O(1) |
| **Range Update** | O(n) | O(log n) | O(log n) | O(n) |

### Space Complexity
- **BIT**: O(n) - very space efficient
- **Segment Tree**: O(4n) - needs more space but more flexible
- **2D BIT**: O(n×m) - scales quadratically
- **Lazy Segment Tree**: O(4n) - same as regular segment tree

## Implementation Tips

### Common Pitfalls & Solutions
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

## Summary & Quick Reference

### When to Use Each Structure

| Use Case | Best Choice | Why |
|----------|-------------|-----|
| **Range Sum + Point Updates** | BIT | Simple, space-efficient |
| **Range Min/Max + Updates** | Segment Tree | Supports any associative operation |
| **Range Updates** | Lazy Segment Tree | Efficient batch updates |
| **2D Range Queries** | 2D BIT | Natural extension |
| **Count Inversions** | BIT + Compression | Perfect for order statistics |

### Implementation Checklist
- [ ] **BIT**: Remember 1-indexing, use coordinate compression for large values
- [ ] **Segment Tree**: Allocate 4n space, handle query edge cases
- [ ] **Lazy Propagation**: Implement push correctly, update children lazily
- [ ] **2D Structures**: Consider memory usage, test with small examples first

### LeetCode Problem Categories
- **Range Sum**: LC 303, 307, 308 (BIT/Segment Tree)
- **Order Statistics**: LC 315, 327, 493 (BIT + Compression)
- **Dynamic Programming**: Range DP with RMQ optimization
- **Geometry**: 2D range queries, rectangle problems

## LC Examples

### 2-1) Range Sum Query - Mutable (LC 307) — Segment Tree
> Segment tree supports O(log N) range sum query and point update.

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

### 2-2) My Calendar I (LC 729) — Segment Tree / TreeMap for Interval Booking
> Use TreeMap to check if new booking overlaps with any existing booking.

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

This comprehensive guide covers the essential concepts and implementations for Segment Trees and Binary Indexed Trees, with practical examples from LeetCode problems.

### 2-3) Segment Tree Template — Range Sum with Lazy Propagation
> Full segment tree with range-update and range-query in O(log N).

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

### 2-4) Corporate Flight Bookings (LC 1109) — Difference Array
> Range-add passengers [first, last]; prefix-sum to get totals per flight.

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

### 2-5) Count of Smaller Numbers After Self (LC 315) — Iterative Segment Tree
> Build segment tree on value range; insert from right to left; query smaller prefix count.

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

### 2-6) Falling Squares (LC 699) — Segment Tree Max
> Each square lands on the highest existing height in its range; track running max height.

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

### 2-7) Maximum Sum Rectangle No Larger Than K (LC 363) — Prefix Sum + TreeSet
> Fix column bounds; compress to 1D row sums; use TreeSet to find max sum ≤ k.

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

### 2-8) Range Module (LC 715) — Segment Tree / TreeMap
> Track which ranges are tracked; add, remove, and query ranges efficiently.

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

### 2-9) Longest Increasing Subsequence (LC 300) — Segment Tree on Values
> Segment tree on compressed values; query max LIS length for values < current, then update.

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

### 2-10) My Calendar II (LC 731) — Segment Tree / TreeMap
> Allow at most 2 overlapping bookings; reject if a third overlap would occur.

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

### 2-11) Rectangle Area II (LC 850) — Sweep Line + "Covered Length" Segment Tree ⭐⭐⭐⭐⭐

> **Pattern**: sweep a vertical line over x; a segment tree over **compressed y** answers *"how much of the y-axis is currently covered by at least one rectangle?"*
>
> **Key Idea**: this is the one segment tree that has **no lazy propagation and no push-down**. Each node keeps `cnt` = how many active rectangles cover this node's *whole* interval, and `cover` = covered length inside it. `cnt` is never pushed to children — it is only ever incremented/decremented on the same set of nodes (every `+1` is later matched by an exact `-1`), so a node's true state is *"my own `cnt` plus whatever my ancestors add"*. That is why `cover` is only meaningful when read **from the root**.

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

**Gotchas**
- A rectangle spanning `y in [ys[a], ys[b]]` maps to elementary-segment indices `[a, b-1]` (`b-1`, not `b`) — segments live *between* compressed coordinates.
- Process **all** events at the same x before measuring the next strip.
- `cover[0] * dx` can reach `1e9 * 1e9` → use `long` in Java; take the mod only at the very end (or per strip), never on the segment tree values themselves.

**Variation — range assign + range max (LC 699 Falling Squares)**: same sweep skeleton but the tree stores a *height* with a lazy "assign" tag. See the lazy range-assign template in [`difference_array.md`](difference_array.md) rather than duplicating it here; the `O(N^2)` baseline is already at [2-6](#2-6-falling-squares-lc-699--segment-tree-max) above.

### 2-12) The Skyline Problem (LC 218) — Sweep Line + Max-Heap (segment tree NOT needed) ⭐⭐⭐⭐⭐

> Highest-frequency problem on this page (Google / Meta / Amazon / Microsoft / Apple / Uber). It *looks* like a segment tree problem and is tagged as one, but the expected answer is a **sweep line + max-heap with lazy deletion** — write that first; the segment tree version is strictly more code for the same complexity.
>
> **Key Idea**: sort events by x, **start events before end events at the same x**, and keep the heap of "currently alive" heights. Emit a key point only when the max height changes.

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

**Why the tie-breaks matter**
- Same x, start before end → two touching buildings of equal height do not emit a spurious `0`.
- Lazy deletion (pop only while the *top* is expired) avoids needing an indexed heap; stale entries below the top are harmless because only the max is ever read.
- The ground sentinel `(0, +inf)` makes "heap empty" impossible, so the final `[x, 0]` falls out for free.

### 2-13) Number of Longest Increasing Subsequence (LC 673) — Segment Tree with a Custom Merge ⭐⭐⭐⭐

> Extends [2-9](#2-9-longest-increasing-subsequence-lc-300--segment-tree-on-values) (LC 300 LIS via segment tree on values) — the twist is that a node now stores a **pair** `(bestLen, count)` and `merge` is not `+` or `max`, it is *"keep the longer; on a tie, add the counts"*.
>
> **Key Idea**: any associative merge works in a segment tree. Sweep left→right; for value `v`, query the max-length-and-count over all strictly smaller ranks, extend by 1, then point-update rank(`v`).

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

**Two traps**
- Empty query result is `(0, 0)`, but a value with no smaller predecessor starts a chain of **count 1**, not 0 — map `len == 0` to `count = 1` before updating.
- The leaf update must **merge** with what is already there (same value may appear many times), not overwrite it. `[2,2,2,2,2] -> 5` catches this.

> `O(N^2)` DP with `(len[i], cnt[i])` is the expected interview answer for `N <= 2000`; reach for this tree only when asked for `O(N log N)`.

**Variation — LC 1157 Online Majority Element In Subarray**: identical skeleton, different node — store a **Boyer–Moore vote pair** `(candidate, votes)` and let `merge` cancel opposing votes. The merged root of a range yields the *only possible* majority candidate; confirm it with a `value -> sorted list of indices` map plus binary search on `[left, right]`.

### 2-14) Segment Tree Descent — Find the k-th Empty Slot (LC 406) ⭐⭐⭐⭐

> **Pattern**: the tree stores *counts of available slots*; instead of binary-searching with `O(log N)` range queries (→ `O(log^2 N)`), you **walk down the tree once** comparing `k` against the left child's count. This "descend on the tree" trick is the standard answer to *find the k-th 1 / k-th empty position / smallest prefix with sum ≥ k*.
>
> **LC 406 Key Idea**: sort by height **ascending**, tie-break by k **descending**. Everyone placed later is at least as tall, so for person `(h, k)` exactly `k` of the still-empty slots must sit before them → put them in the `(k+1)`-th empty slot.

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

> In an interview, the greedy `sort by (height desc, k asc)` + `list.insert(k, person)` one-liner is the expected LC 406 answer (`O(N^2)` but ~3 lines). Bring this version only when asked to beat `O(N^2)` — its real value is the **descent** routine, which is reusable everywhere.

**Variation — LC 1505 Minimum Possible Integer After at Most K Adjacent Swaps On Digits**: greedily take the smallest digit reachable within the remaining budget; a BIT/segment tree over positions counts how many already-removed digits lie before it, converting an *original* index into the *current* index. Same "count of still-present slots" structure, using a prefix **query** instead of a descent.

### 2-15) Decision Note — Segment Tree vs BIT vs Prefix Sum vs Ordered Map ⭐⭐⭐⭐⭐

> Reaching for a segment tree in an interview is **usually the wrong call**. Walk this table top-down and stop at the first row that fits.

| Situation | Reach for | Why not a segment tree |
|-----------|-----------|------------------------|
| Static array, range sum only | **Prefix sum** | `O(N)` build, `O(1)` query, 2 lines |
| Range add offline, read all at the end | **Difference array** | see [`difference_array.md`](difference_array.md) — no tree at all |
| Point update + prefix/range **sum** | **BIT** | same `O(log N)`, ~8 lines, `O(N)` space |
| Counting smaller / inversions | **BIT + coordinate compression** (or merge sort) | sums are all you need |
| Interval booking / add / remove / overlap | **TreeMap / `SortedList`** (LC 715, 729, 731) | intervals are sparse; a tree over `1e9` coords needs dynamic nodes |
| "Max so far" while sweeping | **Heap with lazy deletion** (LC 218) | you only ever read the max |
| `N <= 2000` and `O(N^2)` passes | **Plain DP / brute force** (LC 673, 406, 1395) | write the simple one, mention the tree |
| Range **max/min/gcd** + point update | **Segment tree** | — |
| Range **update** + range query | **Lazy segment tree** | — |
| Custom associative merge, e.g. `(len, count)`, Boyer–Moore pair | **Segment tree** | BIT cannot do non-invertible merges |
| Covered length / area of union under a sweep | **Segment tree with `cnt`** (LC 850) | — |
| Coordinates up to `1e9`, few operations | **Coordinate compression**, else a dynamic/sparse tree | a `4 * 1e9` array does not fit |

**Say this out loud in the interview**: *"A BIT gives me prefix sums in 8 lines; I only need a segment tree if the merge isn't invertible (max/gcd/custom) or if I need lazy range updates."* That single sentence is worth more than a memorized 80-line template.

**Reference lines** (segment-tree/BIT-tagged, but the tree is not the intended solution):
- **LC 1622 Fancy Sequence** — a lazy segment tree with *affine tag composition* `(a, b) -> (a*x + b)` works, but the intended solution is `O(1)` per op: keep one global `(mul, add)` transform and store each appended value pre-divided by `mul` using the **modular inverse** (`pow(mul, MOD-2, MOD)`).
- **LC 1395 Count Number of Teams** — BIT counts "smaller before / greater after" per index, but `n <= 1000`, so the `O(N^2)` "fix the middle soldier and multiply the two counts" solution is the expected answer.
- **LC 1409 Queries on a Permutation With Key** — BIT over a `2m`-sized array simulates the move-to-front; with `m <= 10^3` a plain list `index` + `pop` + `insert(0, ...)` is accepted and far clearer.