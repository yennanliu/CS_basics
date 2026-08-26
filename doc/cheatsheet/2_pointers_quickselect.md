# QuickSelect (Kth Element by Partition)

> **Scope** — Partition-based selection: finding the Kth largest, Kth smallest or K closest element in O(n) average time by recursing into only one side of a QuickSort partition, including pivot strategies and an outline of the O(n) worst-case Median of Medians.
> **See also**: [2_pointers.md](./2_pointers.md) — the two-pointer sheet this was split out of, since a partition scan looks like two pointers but is a selection algorithm; [sort.md](./sort.md) — QuickSort itself, and where sorting beats selecting; [heap.md](./heap.md) — the size-K heap alternative, O(n log k) but streaming-friendly; [advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — quickselect as a divide-and-conquer recurrence.

## LeetCode Problem Lists

- [Quickselect](https://leetcode.com/problem-list/quickselect/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## Overview

**Pattern Overview:**
QuickSelect is a selection algorithm to find the Kth smallest/largest element in an unordered list. It's related to QuickSort but only recurses into one side of the partition. This makes it **O(n) average time** instead of O(n log n).

**Core Concept:**
```text
Given array: [3, 2, 1, 5, 6, 4], find 2nd largest (k=2)

QuickSort: Sorts entire array → O(n log n)
QuickSelect: Only finds the Kth element position → O(n) average
```

**Key Insight:**
- After partitioning around a pivot, the pivot is in its final sorted position
- If pivot index = k, we found the answer
- If pivot index < k, search right partition
- If pivot index > k, search left partition

**Algorithm Steps:**
1. Choose a pivot (usually last element, or random for better performance)
2. Partition array: elements < pivot on left, elements > pivot on right
3. If pivot position == k, return pivot value
4. If pivot position < k, recursively search right partition
5. If pivot position > k, recursively search left partition

### Key Properties
- **Complexity**: O(n) average, O(n^2) worst case with a bad pivot (O(n) worst case with Median of Medians); O(1) extra space iteratively, O(log n) recursion stack
- **Core Idea**: after a partition the pivot sits at its final sorted index, so comparing that index with `k` tells you which single side still contains the answer
- **When to Use**: one-shot "kth largest / smallest / closest" where the full order is not required
- **When NOT to Use**: streaming input, read-only input, or when you need all k elements in sorted order — use a size-K heap instead

## Templates & Algorithms

### Template 1: Kth Largest Element — LC 215

```python
# Python - QuickSelect for Kth Largest
def findKthLargest(nums, k):
    """
    Find Kth largest element using QuickSelect.

    Time: O(n) average, O(n^2) worst (if bad pivots)
    Space: O(1) iterative, O(log n) recursive

    Key: Kth largest means (n - k)th smallest in 0-indexed array
    """
    def partition(left, right):
        """
        Partition using last element as pivot.
        Returns pivot's final position.
        """
        pivot = nums[right]
        i = left  # Position where elements < pivot should go

        # Move all elements < pivot to the left
        for j in range(left, right):
            if nums[j] < pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1

        # Place pivot in correct position
        nums[i], nums[right] = nums[right], nums[i]
        return i

    def quickselect(left, right, k_smallest):
        """
        QuickSelect to find k_smallest element (0-indexed).
        """
        if left == right:  # Only one element
            return nums[left]

        # Partition and get pivot position
        pivot_idx = partition(left, right)

        # Check if we found the answer
        if pivot_idx == k_smallest:
            return nums[pivot_idx]
        elif pivot_idx < k_smallest:
            # Search right partition
            return quickselect(pivot_idx + 1, right, k_smallest)
        else:
            # Search left partition
            return quickselect(left, pivot_idx - 1, k_smallest)

    # Kth largest = (n - k)th smallest (0-indexed)
    n = len(nums)
    return quickselect(0, n - 1, n - k)

# Example usage:
# nums = [3, 2, 1, 5, 6, 4], k = 2
# Result: 5 (2nd largest)
```

```java
// Java - QuickSelect for Kth Largest
/**
 * LC 215 - Kth Largest Element in an Array
 *
 * time = O(N) average, O(N^2) worst
 * space = O(1) iterative, O(log N) recursive
 */
class Solution {
    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        // Kth largest = (n - k)th smallest (0-indexed)
        return quickSelect(nums, 0, n - 1, n - k);
    }

    private int quickSelect(int[] nums, int left, int right, int kSmallest) {
        if (left == right) {
            return nums[left];
        }

        // Partition and get pivot position
        int pivotIdx = partition(nums, left, right);

        // Check if we found the answer
        if (pivotIdx == kSmallest) {
            return nums[pivotIdx];
        } else if (pivotIdx < kSmallest) {
            // Search right partition
            return quickSelect(nums, pivotIdx + 1, right, kSmallest);
        } else {
            // Search left partition
            return quickSelect(nums, left, pivotIdx - 1, kSmallest);
        }
    }

    private int partition(int[] nums, int left, int right) {
        // Use last element as pivot
        int pivot = nums[right];
        int i = left;  // Position for elements < pivot

        // Move all elements < pivot to the left
        for (int j = left; j < right; j++) {
            if (nums[j] < pivot) {
                swap(nums, i, j);
                i++;
            }
        }

        // Place pivot in correct position
        swap(nums, i, right);
        return i;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

---

### Visual Example: Finding 2nd Largest in [3, 2, 1, 5, 6, 4]

```text
Target: k = 2 (2nd largest)
Array: [3, 2, 1, 5, 6, 4]
n = 6, so we need (n - k) = 4th smallest element (0-indexed)

Step 1: Partition with pivot = 4 (last element)
[3, 2, 1, 4, 6, 5]
         ↑
    pivot_idx = 3

  Elements < 4: [3, 2, 1]
  Pivot: 4 (at index 3)
  Elements > 4: [6, 5]

Check: pivot_idx (3) < k_smallest (4)
Action: Search right partition [6, 5]

Step 2: Partition right side [6, 5] with pivot = 5
[3, 2, 1, 4, 5, 6]
            ↑
    pivot_idx = 4

Check: pivot_idx (4) == k_smallest (4) ✓
Answer: nums[4] = 5 (2nd largest element)
```

---

### Template 2: K Closest Points to Origin — LC 973

```python
# Python - K Closest Points using QuickSelect
def kClosest(points, k):
    """
    Find K closest points to origin using QuickSelect.

    Time: O(n) average
    Space: O(1)
    """
    def distance(point):
        return point[0] ** 2 + point[1] ** 2

    def partition(left, right):
        pivot_dist = distance(points[right])
        i = left

        for j in range(left, right):
            if distance(points[j]) < pivot_dist:
                points[i], points[j] = points[j], points[i]
                i += 1

        points[i], points[right] = points[right], points[i]
        return i

    def quickselect(left, right, k):
        if left == right:
            return

        pivot_idx = partition(left, right)

        if pivot_idx == k:
            return
        elif pivot_idx < k:
            quickselect(pivot_idx + 1, right, k)
        else:
            quickselect(left, pivot_idx - 1, k)

    # Find K smallest distances
    quickselect(0, len(points) - 1, k - 1)
    return points[:k]
```

```java
// Java - K Closest Points
/**
 * LC 973 - K Closest Points to Origin
 *
 * time = O(N) average
 * space = O(1)
 */
class Solution {
    public int[][] kClosest(int[][] points, int k) {
        quickSelect(points, 0, points.length - 1, k - 1);
        return Arrays.copyOfRange(points, 0, k);
    }

    private void quickSelect(int[][] points, int left, int right, int k) {
        if (left >= right) return;

        int pivotIdx = partition(points, left, right);

        if (pivotIdx == k) {
            return;
        } else if (pivotIdx < k) {
            quickSelect(points, pivotIdx + 1, right, k);
        } else {
            quickSelect(points, left, pivotIdx - 1, k);
        }
    }

    private int partition(int[][] points, int left, int right) {
        int[] pivot = points[right];
        int pivotDist = distance(pivot);
        int i = left;

        for (int j = left; j < right; j++) {
            if (distance(points[j]) < pivotDist) {
                swap(points, i, j);
                i++;
            }
        }

        swap(points, i, right);
        return i;
    }

    private int distance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
}
```

#### **In Java**

```java
// java
// LC 973 - K Closest Points to Origin
// IDEA: quickselect on squared distance; once the pivot sits at index k-1, points[0..k-1] IS the answer
// time = O(N) expected, space = O(1)
public int[][] kClosest(int[][] points, int k) {
    int lo = 0, hi = points.length - 1;
    while (lo < hi) {
        int p = partitionPts(points, lo, hi, lo + rnd.nextInt(hi - lo + 1));
        if (p == k - 1) break;
        else if (p < k - 1) lo = p + 1;
        else hi = p - 1;
    }
    return Arrays.copyOfRange(points, 0, k);
}
private long d(int[] p) { return (long) p[0] * p[0] + (long) p[1] * p[1]; }   // no sqrt needed
private int partitionPts(int[][] a, int lo, int hi, int pivotIdx) {
    long pivot = d(a[pivotIdx]);
    int[] t = a[pivotIdx]; a[pivotIdx] = a[hi]; a[hi] = t;
    int store = lo;
    for (int i = lo; i < hi; i++)
        if (d(a[i]) < pivot) { int[] x = a[store]; a[store] = a[i]; a[i] = x; store++; }
    int[] x = a[store]; a[store] = a[hi]; a[hi] = x;
    return store;
}
```

---

### Optimization: Randomized Pivot

```python
# Randomized QuickSelect for better average performance
import random

def findKthLargest_randomized(nums, k):
    """
    Randomized pivot selection reduces worst-case probability.

    Time: O(n) average with high probability
    """
    def partition(left, right):
        # RANDOM pivot selection
        random_idx = random.randint(left, right)
        nums[random_idx], nums[right] = nums[right], nums[random_idx]

        pivot = nums[right]
        i = left

        for j in range(left, right):
            if nums[j] < pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1

        nums[i], nums[right] = nums[right], nums[i]
        return i

    def quickselect(left, right, k_smallest):
        if left == right:
            return nums[left]

        pivot_idx = partition(left, right)

        if pivot_idx == k_smallest:
            return nums[pivot_idx]
        elif pivot_idx < k_smallest:
            return quickselect(pivot_idx + 1, right, k_smallest)
        else:
            return quickselect(left, pivot_idx - 1, k_smallest)

    n = len(nums)
    return quickselect(0, n - 1, n - k)
```

---

### Partition Algorithm Variants

**1. Hoare Partition (Two-Pointer from Ends):**
```python
def partition_hoare(nums, left, right):
    """
    Hoare's partition: pointers move from both ends.
    More efficient with fewer swaps.
    """
    pivot = nums[(left + right) // 2]  # Middle element as pivot
    i, j = left - 1, right + 1

    while True:
        # Move i right until element >= pivot
        i += 1
        while nums[i] < pivot:
            i += 1

        # Move j left until element <= pivot
        j -= 1
        while nums[j] > pivot:
            j -= 1

        if i >= j:
            return j

        nums[i], nums[j] = nums[j], nums[i]
```

**2. Lomuto Partition (Single Pass):**
```python
def partition_lomuto(nums, left, right):
    """
    Lomuto's partition: single pointer from left.
    Simpler but may do more swaps.
    """
    pivot = nums[right]
    i = left

    for j in range(left, right):
        if nums[j] <= pivot:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1

    nums[i], nums[right] = nums[right], nums[i]
    return i
```

**3. Hoare in Java — and the trap it sets:**
> The twist: `hoare()` guarantees `a[lo..j] <= pivot <= a[j+1..hi]`, so you can **not** test `p == target`; you narrow the window with `target <= j` and loop until `lo == hi`.

```java
// java
// LC 215 - Kth Largest Element in an Array (Hoare partition variant)
// time = O(N) expected, space = O(1)
public int findKthLargestHoare(int[] nums, int k) {
    int target = nums.length - k;
    int lo = 0, hi = nums.length - 1;
    while (lo < hi) {
        int j = hoare(nums, lo, hi);
        if (target <= j) hi = j; else lo = j + 1;
    }
    return nums[lo];
}
private int hoare(int[] a, int lo, int hi) {
    int pivot = a[lo + rnd.nextInt(hi - lo + 1)];   // pivot by VALUE, chosen at random
    int i = lo - 1, j = hi + 1;
    while (true) {
        do { i++; } while (a[i] < pivot);
        do { j--; } while (a[j] > pivot);
        if (i >= j) return j;
        swap(a, i, j);
    }
}
```

**4. Three-way (Dutch-flag) partition — the fix for heavy duplicates:**
> The twist: a random pivot does **not** save Lomuto on `[2,2,2,…,2]` — every partition still peels off one element, giving O(n²). Partitioning into `< / == / >` collapses all equal keys in one pass.

```python
# python
# LC 215 - Kth Largest Element in an Array (3-way partition; safe on many duplicates)
# time = O(N) expected even when almost all values are equal, space = O(1)
import random

def findKthLargest(nums, k):
    target = len(nums) - k
    lo, hi = 0, len(nums) - 1
    while True:
        pivot = nums[random.randint(lo, hi)]
        lt, i, gt = lo, lo, hi
        while i <= gt:                          # invariant: [lo,lt) < pivot, [lt,i) == pivot, (gt,hi] > pivot
            if nums[i] < pivot:
                nums[lt], nums[i] = nums[i], nums[lt]
                lt += 1
                i += 1
            elif nums[i] > pivot:
                nums[i], nums[gt] = nums[gt], nums[i]
                gt -= 1
            else:
                i += 1
        if target < lt:
            hi = lt - 1
        elif target > gt:
            lo = gt + 1
        else:
            return pivot                        # target fell inside the equal band
```

### Other quickselect variations (same skeleton, different key)

| Problem | LC # | The twist |
|---------|------|-----------|
| Top K Frequent Elements | 347 | Quickselect over the *entries* of a frequency map keyed on count. Bucket sort by frequency is the true O(N) answer — mention both. |
| Find the Kth Largest Integer in the Array | 1985 | Values are numeric **strings**; only the comparator changes: shorter string is smaller, else lexicographic. |
| Wiggle Sort II | 324 | Quickselect the **median**, then 3-way partition, then write into *virtual indices* `(1 + 2*i) % (n | 1)` so equal medians end up far apart. |
| Kth Largest XOR Coordinate Value | 1738 | Build the 2-D prefix-XOR grid first (`O(mn)`), then quickselect the k-th largest over the `m*n` values. |


---

### Advanced: Median of Medians (O(n) Worst-Case) — Outline Only

> ⚠️ **This is a sketch, not a runnable solution.** The pivot-selection half is spelled out;
> `partition`, the driver loop and the `median_of_medians_list` recursion are left as `pass`.
> Do not copy it expecting it to run — it always returns `None`. It is here to show *where*
> the O(n) guarantee comes from, which is the only part an interviewer asks about.

```python
def findKthLargest_median_of_medians(nums, k):
    """
    OUTLINE of the guaranteed O(n) worst-case pivot strategy — incomplete.

    The idea: pick the pivot as the median of the group-of-5 medians. That pivot
    is guaranteed to discard at least 30% of the array each round, which turns
    the O(n^2) worst case into O(n).

    Time: O(n) worst-case
    Space: O(log n) recursion
    """
    def median_of_medians(left, right):
        """Find approximate median for good pivot."""
        if right - left < 5:
            return sorted(nums[left:right + 1])[len(nums[left:right + 1]) // 2]

        # Divide into groups of 5, find median of each
        medians = []
        for i in range(left, right + 1, 5):
            sub_right = min(i + 4, right)
            median = sorted(nums[i:sub_right + 1])[(sub_right - i) // 2]
            medians.append(median)

        # Recursively find median of medians
        # NOTE: median_of_medians_list is NOT defined here — it would recurse
        # on `medians` the same way this function recurses on `nums`.
        return median_of_medians_list(medians)

    def partition(left, right, pivot_value):
        # Partition around pivot_value — same three-way scan as Template 1 above
        pass                      # left as an exercise

    # Main quickselect loop, using median_of_medians() for the pivot instead of
    # a random index — otherwise identical to Template 1.
    pass                          # left as an exercise
```

**Note:** Median of Medians is rarely implemented in interviews due to complexity. Randomized QuickSelect is preferred in practice.

---


## Summary & Quick Reference

### Classic LeetCode Problems

| Problem | LC# | Difficulty | Variant | Key Insight |
|---------|-----|------------|---------|-------------|
| Kth Largest Element in Array | 215 | Medium | Basic QuickSelect | Find (n-k)th smallest |
| K Closest Points to Origin | 973 | Medium | Custom comparator | Partition by distance |
| Top K Frequent Elements | 347 | Medium | With frequency map | QuickSelect on frequencies |
| Top K Frequent Words | 692 | Medium | With frequency + trie | QuickSelect + lexicographic order |
| Kth Largest Element in Stream | 703 | Easy | Min heap alternative | QuickSelect for initialization |
| Find Kth Smallest Pair Distance | 719 | Hard | Binary search on answer | Not direct QuickSelect |
| Wiggle Sort II | 324 | Medium | 3-way partition | Dutch national flag variant |
| Sort Colors | 75 | Medium | 3-way partition | Dutch national flag |
| Kth Smallest Element in BST | 230 | Medium | In-order traversal | Not QuickSelect (tree structure) |
| Find Median from Data Stream | 295 | Hard | Two heaps | QuickSelect alternative |

---

### Performance Comparison

| Algorithm | Average Time | Worst Time | Space | Use Case |
|-----------|--------------|------------|-------|----------|
| **QuickSelect** | **O(n)** | O(n²) | O(1) | Find Kth element (unsorted) |
| QuickSelect (Randomized) | O(n) | O(n²) low prob | O(1) | Better average performance |
| Heap (Min/Max) | O(n log k) | O(n log k) | O(k) | Online/streaming data |
| Full Sort | O(n log n) | O(n log n) | O(1) or O(n) | Need sorted array anyway |
| Counting Sort | O(n + k) | O(n + k) | O(k) | Small integer range |

**When to Use QuickSelect:**
- ✅ Need exactly Kth element, don't need full sort
- ✅ Can modify input array (in-place)
- ✅ Offline algorithm (all data available)
- ✅ Large dataset where O(n) vs O(n log n) matters

**When NOT to Use QuickSelect:**
- ❌ Need all K elements in sorted order → Use heap or full sort
- ❌ Online/streaming data → Use heap
- ❌ Cannot modify input array → Use heap
- ❌ Worst-case guarantee needed → Use Median of Medians (O(n) worst-case)

---

### Interview Tips

**1. Common Mistakes:**
- Forgetting to convert "Kth largest" → "(n - k)th smallest"
- Off-by-one errors with 0-indexed vs 1-indexed k
- Not handling left == right base case
- Infinite recursion when partition doesn't move pivot

**2. Optimization Techniques:**
- **Randomized pivot**: Reduces worst-case probability
- **Median-of-three**: Choose median of first, middle, last elements as pivot
- **Iterative version**: Avoid stack overflow for very large arrays
- **Tail recursion**: Only recurse into smaller partition

**3. Complexity Analysis:**
```text
Best/Average Case: O(n + n/2 + n/4 + ... + 1) = O(2n) = O(n)

Worst Case (bad pivots every time):
  O(n + (n-1) + (n-2) + ... + 1) = O(n²)

With randomized pivot:
  Worst case O(n²) probability → near zero for large n
```

**4. Interview Talking Points:**
- "QuickSelect is like QuickSort but only recurses into one partition"
- "Average O(n) is better than O(n log k) heap for finding single Kth element"
- "Trade-off: Modifies array vs. heap keeps original"
- "Randomized pivot gives O(n) with high probability"

**5. Follow-up Questions:**
- Q: "What if we need all K elements sorted?"
  - A: Use heap (O(n log k)) or partial QuickSort
- Q: "What if array is read-only?"
  - A: Copy to new array or use heap
- Q: "Can we guarantee O(n) worst-case?"
  - A: Yes, using Median of Medians algorithm (complex, rarely asked)

---

