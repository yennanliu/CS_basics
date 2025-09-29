# Segment Tree & Binary Indexed Tree (Fenwick Tree)

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

### Template 4: 2D Binary Indexed Tree
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

### LC 307: Range Sum Query - Mutable
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

### LC 315: Count of Smaller Numbers After Self
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

### LC 493: Reverse Pairs
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

### LC 327: Count of Range Sum
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

This comprehensive guide covers the essential concepts and implementations for Segment Trees and Binary Indexed Trees, with practical examples from LeetCode problems.