# Advanced Divide and Conquer

## LeetCode Problem Lists

- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Merge Sort](https://leetcode.com/problem-list/merge-sort/)
- [Quickselect](https://leetcode.com/problem-list/quickselect/)

## Overview
**Divide and Conquer** is a powerful algorithmic paradigm that breaks complex problems into smaller subproblems, solves them recursively, and combines the results. This approach is particularly effective for inversion counting, range queries, and merge-based operations.

### Key Properties
- **Time Complexity**: Typically O(n log n) for most problems
- **Space Complexity**: O(n) for auxiliary arrays, O(log n) for recursion stack
- **Core Idea**: Divide problem into halves, solve recursively, merge results
- **When to Use**: Counting inversions, range problems, merge operations
- **Key Technique**: Modified merge sort with custom merge logic

### Core Characteristics
- **Divide**: Split problem into smaller subproblems
- **Conquer**: Solve subproblems recursively
- **Combine**: Merge solutions to get final answer
- **Optimal Substructure**: Problem can be broken down optimally
- **Merge Logic**: Custom combine step for specific requirements

## Problem Categories

### **Category 1: Inversion Counting**
- **Description**: Count pairs where left element > right element
- **Examples**: LC 315 (Count Smaller After Self), LC 493 (Reverse Pairs), LC 327 (Count Range Sum)
- **Pattern**: Modified merge sort with counting during merge

### **Category 2: Range Sum Problems**
- **Description**: Count elements/subarrays in specific ranges
- **Examples**: LC 327 (Count of Range Sum), LC 493 (Reverse Pairs with condition)
- **Pattern**: Prefix sums + divide and conquer

### **Category 3: Array Reconstruction**
- **Description**: Build arrays with specific ordering properties
- **Examples**: LC 1649 (Create Sorted Array), LC 2426 (Pairs Satisfying Inequality)
- **Pattern**: Merge sort with reconstruction logic

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Basic Inversion Count** | Count inversions | O(n log n) | Simple inversion problems |
| **Conditional Inversion** | Count with conditions | O(n log n) | Reverse pairs, range conditions |
| **Range Query D&C** | Range sum counting | O(n log n) | Subarray sum problems |
| **Reconstruction D&C** | Build sorted arrays | O(n log n) | Array construction problems |

### Template 1: Basic Inversion Counting — LC 315
```python
def count_inversions(arr):
    """Count total number of inversions using merge sort"""
    def merge_and_count(arr, temp, left, mid, right):
        i, j, k = left, mid + 1, left
        inv_count = 0

        # Merge with inversion counting
        while i <= mid and j <= right:
            if arr[i] <= arr[j]:
                temp[k] = arr[i]
                i += 1
            else:
                temp[k] = arr[j]
                # Count inversions: all elements from i to mid are > arr[j]
                inv_count += (mid - i + 1)
                j += 1
            k += 1

        # Copy remaining elements
        while i <= mid:
            temp[k] = arr[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = arr[j]
            j += 1
            k += 1

        # Copy back to original array
        for i in range(left, right + 1):
            arr[i] = temp[i]

        return inv_count

    def merge_sort_and_count(arr, temp, left, right):
        inv_count = 0
        if left < right:
            mid = (left + right) // 2
            inv_count += merge_sort_and_count(arr, temp, left, mid)
            inv_count += merge_sort_and_count(arr, temp, mid + 1, right)
            inv_count += merge_and_count(arr, temp, left, mid, right)
        return inv_count

    temp = [0] * len(arr)
    return merge_sort_and_count(arr[:], temp, 0, len(arr) - 1)
```

### Template 2: Conditional Inversion Counting — LC 493
```python
def count_reverse_pairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first (before sorting)
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Now perform regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### Template 3: Range Sum Divide and Conquer — LC 327
```python
def count_range_sum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        # For each prefix sum in left half
        for i in range(left, mid + 1):
            # Find range [j, k) where prefix_sums[j] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Regular merge
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sums
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

### Template 4: Array Reconstruction — LC 1649
```python
def create_sorted_array(instructions):
    """Create sorted array with minimum cost"""
    def merge_and_count(arr, temp, left, mid, right):
        smaller_count = [0] * len(arr)
        larger_count = [0] * len(arr)

        # Count smaller and larger elements during merge
        i, j = left, mid + 1
        for k in range(left, right + 1):
            if i > mid:
                temp[k] = arr[j]
                j += 1
            elif j > right:
                temp[k] = arr[i]
                # Count how many elements from right half are smaller
                smaller_count[arr[i][1]] += (right - mid)
                i += 1
            elif arr[i][0] <= arr[j][0]:
                temp[k] = arr[i]
                smaller_count[arr[i][1]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = arr[j]
                j += 1

        # Copy back
        for k in range(left, right + 1):
            arr[k] = temp[k]

        return smaller_count, larger_count

    def merge_sort_with_count(arr, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort_with_count(arr, temp, left, mid)
        merge_sort_with_count(arr, temp, mid + 1, right)
        merge_and_count(arr, temp, left, mid, right)

    # Implementation depends on specific problem
    # This is a general framework for array reconstruction
    pass
```

---

## Beyond Merge Sort: Other D&C Shapes

Templates 1-4 are all the **same shape**: split at the midpoint, recurse on both halves, count during the merge. But D&C in interviews shows up in four other shapes that the merge-sort template does *not* cover. Learn to recognise which one you are in — the recurrence tells you the answer's complexity before you write a line.

### Quick Decision Table — Which D&C Shape?
| What you are asked | Shape | Recurrence | Complexity | Examples |
|--------------------|-------|------------|------------|----------|
| k-th smallest / largest, "top k" (order NOT required) | **Recurse into ONE side** (Template 5) | `T(n) = T(n/2) + O(n)` | **O(n)** expected | LC 215, 973, 347, 1985 |
| Best subarray / segment, answer may straddle the middle | **Combine across the midpoint** (Template 6) | `T(n) = 2T(n/2) + O(n)` | O(n log n) | LC 53, 918, 218 |
| Combine k already-sorted things | **Pairwise merge** (Template 7) | `T(k) = 2T(k/2) + O(N)` | O(N log k) | LC 23, 148, 912 |
| Search / build over a 2-D grid or a data-chosen split point | **D&C over an implicit space** (Template 8) | `T(n) = 3T(n/2) + O(1)` | O(n^log₂3) ≈ O(n^1.58) | LC 240, 427, 395, 1763 |

> **Master theorem quick read** for `T(n) = a·T(n/b) + f(n)`, compare `f(n)` against `n^(log_b a)`:
> - `f(n)` **smaller** → cost dominated by the leaves → `O(n^(log_b a))` — *LC 240: a=3, b=2, f=O(1) → O(n^1.58)*
> - `f(n)` **equal** → every level costs the same → `O(n^(log_b a) · log n)` — *LC 53: a=2, b=2, f=O(n) → O(n log n)*
> - `f(n)` **bigger** → cost dominated by the root → `O(f(n))` — *LC 215: a=1, b=2, f=O(n) → O(n)*
>
> The whole reason quickselect beats sorting is that **a drops from 2 to 1**, turning the geometric series `n + n/2 + n/4 + …` into `2n`.

---

### Template 5: Quickselect — Partition, Recurse into ONE Side — LC 215 ⭐⭐⭐⭐⭐

**Key Idea**: after one partition the pivot sits at its **final sorted index** `p`. Compare `p` with the target index — the answer lives on exactly one side, so **throw the other half away** instead of recursing into it.

**Recurrence**: `T(n) = T(n/2) + O(n)` → `n + n/2 + n/4 + … = O(n)` **expected**. Worst case is `O(n²)` when the pivot is always extremal — that is why the pivot must be **random**.

**Selecting k-th largest**: convert to an ascending index once, up front: `target = n - k`. Do not flip comparators inside the partition.

```java
// java
// LC 215 - Kth Largest Element in an Array
// IDEA: Lomuto partition with a RANDOM pivot; the pivot lands at its final index,
//       so recurse into only the side that contains the target index
// time = O(N) expected / O(N^2) worst, space = O(1) (iterative — no recursion stack)
Random rnd = new Random();

public int findKthLargest(int[] nums, int k) {
    int target = nums.length - k;              // k-th largest == index (n-k) in ascending order
    int lo = 0, hi = nums.length - 1;
    while (true) {
        if (lo == hi) return nums[lo];
        int p = partition(nums, lo, hi, lo + rnd.nextInt(hi - lo + 1));   // random pivot!
        if (p == target) return nums[p];
        else if (p < target) lo = p + 1;       // keep the RIGHT side only
        else hi = p - 1;                       // keep the LEFT side only
    }
}

// Lomuto: park the pivot at the end, sweep everything smaller to the front, put the pivot back
private int partition(int[] a, int lo, int hi, int pivotIdx) {
    int pivot = a[pivotIdx];
    swap(a, pivotIdx, hi);
    int store = lo;
    for (int i = lo; i < hi; i++) if (a[i] < pivot) swap(a, store++, i);
    swap(a, store, hi);
    return store;                              // final resting index of the pivot
}
private void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
```

```python
# python
# LC 215 - Kth Largest Element in an Array
# time = O(N) expected / O(N^2) worst, space = O(1)
import random

def findKthLargest(nums, k):
    target = len(nums) - k                      # k-th largest == index (n-k) ascending

    def partition(lo, hi, pivot_idx):
        pivot = nums[pivot_idx]
        nums[pivot_idx], nums[hi] = nums[hi], nums[pivot_idx]
        store = lo
        for i in range(lo, hi):
            if nums[i] < pivot:
                nums[store], nums[i] = nums[i], nums[store]
                store += 1
        nums[store], nums[hi] = nums[hi], nums[store]
        return store

    lo, hi = 0, len(nums) - 1
    while True:
        if lo == hi:
            return nums[lo]
        p = partition(lo, hi, random.randint(lo, hi))
        if p == target:
            return nums[p]
        elif p < target:
            lo = p + 1                          # keep the RIGHT side only
        else:
            hi = p - 1                          # keep the LEFT side only
```

#### **Hoare partition** — fewer swaps, but returns a *split point*, not the pivot's index
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

#### **3-way (Dutch-flag) partition** — the fix for heavy duplicates
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

#### **Variation: LC 973 — K Closest Points to Origin**
> The twist: the *ordering key* becomes squared distance and you stop as soon as the pivot lands at index `k-1` — the first `k` slots are then the answer **in any order**, which is exactly what the problem allows. Beats the O(N log k) heap.

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

#### **Other quickselect variations (same skeleton, different key)**
| Problem | LC # | The twist |
|---------|------|-----------|
| Top K Frequent Elements | 347 | Quickselect over the *entries* of a frequency map keyed on count. Bucket sort by frequency is the true O(N) answer — mention both. |
| Find the Kth Largest Integer in the Array | 1985 | Values are numeric **strings**; only the comparator changes: shorter string is smaller, else lexicographic. |
| Wiggle Sort II | 324 | Quickselect the **median**, then 3-way partition, then write into *virtual indices* `(1 + 2*i) % (n | 1)` so equal medians end up far apart. |
| Kth Largest XOR Coordinate Value | 1738 | Build the 2-D prefix-XOR grid first (`O(mn)`), then quickselect the k-th largest over the `m*n` values. |

---

### Template 6: Combine Across the Midpoint — LC 53

**Key Idea**: when the answer is a *contiguous segment*, it either lives entirely in the left half, entirely in the right half, or **straddles the midpoint**. The first two come free from recursion; the third is the only real work — and it is computed by expanding outward from the middle in O(n).

**Recurrence**: `T(n) = 2T(n/2) + O(n)` → **O(n log n)** (Master theorem, equal case). Kadane's O(n) DP is strictly better, so say *"D&C gives O(n log n), Kadane gives O(n)"* — the D&C version is what interviewers ask for when they say "now solve it with divide and conquer".

```java
// java
// LC 53 - Maximum Subarray (divide & conquer view)
// IDEA: best = max(best in left, best in right, best crossing the midpoint);
//       the crossing one = max suffix of left + max prefix of right
// time = O(N log N), space = O(log N) recursion stack
public int maxSubArray(int[] nums) { return solve(nums, 0, nums.length - 1); }

private int solve(int[] a, int l, int r) {
    if (l == r) return a[l];                       // single element is its own best
    int mid = l + (r - l) / 2;
    int left  = solve(a, l, mid);
    int right = solve(a, mid + 1, r);

    int sum = 0, leftBest = Integer.MIN_VALUE;     // best SUFFIX of the left half (must touch mid)
    for (int i = mid; i >= l; i--) { sum += a[i]; leftBest = Math.max(leftBest, sum); }

    sum = 0;
    int rightBest = Integer.MIN_VALUE;             // best PREFIX of the right half (must touch mid+1)
    for (int i = mid + 1; i <= r; i++) { sum += a[i]; rightBest = Math.max(rightBest, sum); }

    return Math.max(Math.max(left, right), leftBest + rightBest);
}
```

```python
# python
# LC 53 - Maximum Subarray (divide & conquer view)
# time = O(N log N), space = O(log N)
def maxSubArray(nums):
    def solve(l, r):
        if l == r:
            return nums[l]
        mid = (l + r) // 2
        left = solve(l, mid)
        right = solve(mid + 1, r)

        s, left_best = 0, float('-inf')            # best suffix of left half
        for i in range(mid, l - 1, -1):
            s += nums[i]
            left_best = max(left_best, s)

        s, right_best = 0, float('-inf')           # best prefix of right half
        for i in range(mid + 1, r + 1):
            s += nums[i]
            right_best = max(right_best, s)

        return max(left, right, left_best + right_best)

    return solve(0, len(nums) - 1)
```

**🚫 The classic bug**: initialising `leftBest = 0`. That silently allows an *empty* suffix, so an all-negative array returns `0` instead of the largest single element. Start from `-infinity` and force both halves to be non-empty.

#### **Variation: LC 918 — Maximum Sum Circular Subarray**
> The twist: a wrap-around segment is exactly the complement of a **non**-wrapping one, so `answer = max(maxKadane, total - minKadane)`. Guard the all-negative case, where `total - minKadane == 0` describes the forbidden empty array.

```java
// java
// LC 918 - Maximum Sum Circular Subarray
// IDEA: best is either a normal subarray, or everything EXCEPT the minimum subarray
// time = O(N), space = O(1)
public int maxSubarraySumCircular(int[] nums) {
    int total = 0, curMax = 0, best = Integer.MIN_VALUE, curMin = 0, worst = Integer.MAX_VALUE;
    for (int x : nums) {
        total  += x;
        curMax = Math.max(curMax + x, x); best  = Math.max(best, curMax);
        curMin = Math.min(curMin + x, x); worst = Math.min(worst, curMin);
    }
    return best > 0 ? Math.max(best, total - worst) : best;   // all-negative -> plain Kadane
}
```

```python
# python
# LC 918 - Maximum Sum Circular Subarray
# time = O(N), space = O(1)
def maxSubarraySumCircular(nums):
    total, cur_max, best, cur_min, worst = 0, 0, float('-inf'), 0, float('inf')
    for x in nums:
        total += x
        cur_max = max(cur_max + x, x)
        best = max(best, cur_max)
        cur_min = min(cur_min + x, x)
        worst = min(worst, cur_min)
    return max(best, total - worst) if best > 0 else best
```

---

### Template 7: Pairwise D&C Merge (k-way) — LC 23 ⭐⭐⭐⭐

**Key Idea**: to combine `k` sorted structures, do **not** fold them one at a time (`O(N·k)` — every early element gets recopied `k` times). Pair them up and merge halves, so each of the `N` total elements is touched once per level and there are only `log k` levels.

**Recurrence**: `T(k) = 2T(k/2) + O(N)` → **O(N log k)** time, matching the heap solution but with `O(1)` extra space instead of `O(k)`.

```java
// java
// LC 23 - Merge k Sorted Lists
// IDEA: divide the ARRAY OF LISTS in half, merge each half, then merge the two results
// time = O(N log k) where N = total nodes, space = O(log k) recursion stack
public ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0) return null;
    return mergeRange(lists, 0, lists.length - 1);
}
private ListNode mergeRange(ListNode[] ls, int l, int r) {
    if (l == r) return ls[l];
    int mid = l + (r - l) / 2;
    return merge2(mergeRange(ls, l, mid), mergeRange(ls, mid + 1, r));
}
private ListNode merge2(ListNode a, ListNode b) {
    ListNode dummy = new ListNode(0), cur = dummy;
    while (a != null && b != null) {
        if (a.val <= b.val) { cur.next = a; a = a.next; }
        else                { cur.next = b; b = b.next; }
        cur = cur.next;
    }
    cur.next = (a != null) ? a : b;          // attach the non-empty remainder wholesale
    return dummy.next;
}
```

```python
# python
# LC 23 - Merge k Sorted Lists
# time = O(N log k), space = O(log k)
def mergeKLists(lists):
    if not lists:
        return None

    def merge2(a, b):
        dummy = cur = ListNode()
        while a and b:
            if a.val <= b.val:
                cur.next, a = a, a.next
            else:
                cur.next, b = b, b.next
            cur = cur.next
        cur.next = a or b
        return dummy.next

    def merge_range(l, r):
        if l == r:
            return lists[l]
        mid = (l + r) // 2
        return merge2(merge_range(l, mid), merge_range(mid + 1, r))

    return merge_range(0, len(lists) - 1)
```

#### **Variation: LC 148 — Sort List**
> The twist: a linked list has no midpoint index, so **slow/fast pointers** find the split. Start `fast = head.next` (not `head`) so that on an even-length list `slow` stops on the *left* tail — otherwise a 2-node list splits into `2 + 0` and recurses forever. Cut with `slow.next = null` before recursing.

```java
// java
// LC 148 - Sort List
// IDEA: merge sort on a linked list — split via slow/fast, sort halves, reuse merge2()
// time = O(N log N), space = O(log N) recursion stack (O(1) if written bottom-up)
public ListNode sortList(ListNode head) {
    if (head == null || head.next == null) return head;
    ListNode slow = head, fast = head.next;      // fast starts AHEAD -> slow lands on left tail
    while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
    ListNode right = slow.next;
    slow.next = null;                            // cut the list in two
    return merge2(sortList(head), sortList(right));
}
```

```python
# python
# LC 148 - Sort List
# time = O(N log N), space = O(log N)
def sortList(head):
    if not head or not head.next:
        return head
    slow, fast = head, head.next                 # fast starts ahead -> slow lands on left tail
    while fast and fast.next:
        slow, fast = slow.next, fast.next.next
    right, slow.next = slow.next, None           # cut into two halves
    return merge2(sortList(head), sortList(right))
```

---

### Template 8: D&C over a 2-D / Implicit Space — LC 240

**Key Idea**: the "array to halve" need not be an array. Split a **matrix into quadrants** and use one comparison at the centre to prove an entire quadrant cannot contain the target — in a row- and column-sorted matrix, the block's minimum sits at its top-left corner and its maximum at its bottom-right.

**Recurrence**: `T(n) = 3T(n/2) + O(1)` → **O(n^log₂3) ≈ O(n^1.58)** for an `n × n` matrix (Master theorem, leaf-dominated). Note this is *worse* than the O(m + n) staircase walk — know both and say why you'd ship the staircase.

```java
// java
// LC 240 - Search a 2D Matrix II
// IDEA: split the rectangle into 4 quadrants around its centre; comparing the centre to the
//       target always eliminates one whole quadrant, leaving 3 subproblems of half the side
// time = O(N^log2(3)) ~ O(N^1.58) on an N x N matrix, space = O(log N)
public boolean searchMatrix(int[][] m, int target) {
    if (m.length == 0 || m[0].length == 0) return false;
    return dq(m, target, 0, 0, m.length - 1, m[0].length - 1);
}
private boolean dq(int[][] m, int t, int r1, int c1, int r2, int c2) {
    if (r1 > r2 || c1 > c2) return false;
    if (t < m[r1][c1] || t > m[r2][c2]) return false;   // block min / max prune
    if (r1 == r2 && c1 == c2) return m[r1][c1] == t;    // 1x1 base case (stops infinite recursion)
    int rm = (r1 + r2) / 2, cm = (c1 + c2) / 2;
    if (m[rm][cm] == t) return true;
    if (m[rm][cm] < t)                                  // centre too small -> drop TOP-LEFT block
        return dq(m, t, r1, cm + 1, rm, c2)
            || dq(m, t, rm + 1, c1, r2, cm)
            || dq(m, t, rm + 1, cm + 1, r2, c2);
    return dq(m, t, r1, c1, rm, cm)                     // centre too big -> drop BOTTOM-RIGHT block
        || dq(m, t, r1, cm + 1, rm, c2)
        || dq(m, t, rm + 1, c1, r2, cm);
}

// The answer you should actually give: walk in from the top-right corner.
// time = O(M + N), space = O(1)
public boolean searchMatrixStaircase(int[][] m, int target) {
    int r = 0, c = m[0].length - 1;
    while (r < m.length && c >= 0) {
        if (m[r][c] == target) return true;
        if (m[r][c] > target) c--; else r++;   // this column is too big / this row is exhausted
    }
    return false;
}
```

```python
# python
# LC 240 - Search a 2D Matrix II
# time = O(N^log2(3)) ~ O(N^1.58), space = O(log N)
def searchMatrix(matrix, target):
    if not matrix or not matrix[0]:
        return False

    def dq(r1, c1, r2, c2):
        if r1 > r2 or c1 > c2:
            return False
        if target < matrix[r1][c1] or target > matrix[r2][c2]:   # block min / max prune
            return False
        if r1 == r2 and c1 == c2:
            return matrix[r1][c1] == target
        rm, cm = (r1 + r2) // 2, (c1 + c2) // 2
        if matrix[rm][cm] == target:
            return True
        if matrix[rm][cm] < target:                              # drop TOP-LEFT block
            return dq(r1, cm + 1, rm, c2) or dq(rm + 1, c1, r2, cm) or dq(rm + 1, cm + 1, r2, c2)
        return dq(r1, c1, rm, cm) or dq(r1, cm + 1, rm, c2) or dq(rm + 1, c1, r2, cm)  # drop BOTTOM-RIGHT

    return dq(0, 0, len(matrix) - 1, len(matrix[0]) - 1)
```

**Visual trace** — quadrants around the centre `(rm, cm)` of a row/column-sorted block:

```
        c1 ....... cm ....... c2
   r1  +-----------+-----------+
       |    TL     |    TR     |     TL max is m[rm][cm]  -> if m[rm][cm] < target, TL is all too small
   rm  |        (rm,cm)        |     BR min is m[rm+1][cm+1] -> if m[rm][cm] > target, BR is all too big
       +-----------+-----------+
       |    BL     |    BR     |     TR and BL are never eliminable: they hold both
   r2  +-----------+-----------+     smaller and larger values than the centre
```

#### **Variation: LC 427 — Construct Quad Tree**
> The twist: same quadrant split, but a *build* rather than a search — recurse into all four, then **collapse** the node if all four children came back as leaves with the same value.

```python
# python
# LC 427 - Construct Quad Tree
# IDEA: split the grid into 4 equal quadrants; merge back into one leaf when all 4 agree
# time = O(N^2 log N) worst (each of log N levels scans up to N^2 cells), space = O(log N)
def construct(grid):
    def build(r, c, size):
        if size == 1:
            return Node(grid[r][c] == 1, True)
        h = size // 2
        tl = build(r, c, h)
        tr = build(r, c + h, h)
        bl = build(r + h, c, h)
        br = build(r + h, c + h, h)
        if (tl.isLeaf and tr.isLeaf and bl.isLeaf and br.isLeaf
                and tl.val == tr.val == bl.val == br.val):
            return Node(tl.val, True)            # 4 uniform leaves -> collapse into one leaf
        return Node(True, False, tl, tr, bl, br)  # val is ignored for internal nodes

    return build(0, 0, len(grid))
```

> **Related**: LC 558 (Logical OR of Two Quad-Trees) applies the same recursion to *two* trees at once — if either node is a leaf with `val == 1` return that leaf, if either is a leaf with `val == 0` return the other subtree, else OR the four child pairs and collapse.

#### **Variation: LC 395 — split on the *data-chosen* point, not the midpoint**
> The twist: the divide point is dictated by the input. Any character whose **total** frequency in the window is `< k` can never appear inside a valid answer, so it is a wall: split the string on every occurrence and recurse on the pieces.

**Recurrence**: each level removes at least one distinct character, so depth ≤ alphabet size → **O(26·N)**.

```java
// java
// LC 395 - Longest Substring with At Least K Repeating Characters
// IDEA: a char with total count < k can never be inside the answer -> split on it and recurse
// time = O(26 * N), space = O(26 * N) for the substrings / recursion
public int longestSubstring(String s, int k) {
    if (s.length() < k) return 0;
    int[] freq = new int[26];
    for (char ch : s.toCharArray()) freq[ch - 'a']++;
    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0 && freq[i] < k) {            // found a wall character
            int best = 0;
            for (String part : s.split(String.valueOf((char) ('a' + i))))
                best = Math.max(best, longestSubstring(part, k));
            return best;
        }
    }
    return s.length();                               // no wall -> the whole string qualifies
}
```

```python
# python
# LC 395 - Longest Substring with At Least K Repeating Characters
# time = O(26 * N), space = O(26 * N)
def longestSubstring(s, k):
    if len(s) < k:
        return 0
    for c in set(s):
        if s.count(c) < k:                           # c can never sit inside a valid substring
            return max(longestSubstring(part, k) for part in s.split(c))
    return len(s)                                    # every char already appears >= k times
```

#### **Variation: LC 1763 — Longest Nice Substring**
> The twist: identical shape, different wall test — a character whose opposite case is missing from the window can never be inside a nice substring, so split there and take the longer side. Prefer the **leftmost** wall to keep the earliest-answer tie-break.

```python
# python
# LC 1763 - Longest Nice Substring
# IDEA: a char missing its case-partner is a wall -> split there, best of the two sides
# time = O(26 * N), space = O(26 * N)
def longestNiceSubstring(s):
    if len(s) < 2:
        return ""
    chars = set(s)
    for i, c in enumerate(s):
        if c.upper() in chars and c.lower() in chars:
            continue                                  # c is fine, keep scanning
        left = longestNiceSubstring(s[:i])            # wall at i -> answer avoids index i
        right = longestNiceSubstring(s[i + 1:])
        return left if len(left) >= len(right) else right
    return s                                          # no wall -> whole string is nice
```

---

## Problems by Pattern

### **Inversion Counting Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Count of Smaller Numbers After Self | 315 | Modified merge sort | Hard |
| Reverse Pairs | 493 | Conditional inversion count | Hard |
| Count of Range Sum | 327 | Prefix sum + D&C | Hard |
| Create Sorted Array through Instructions | 1649 | Dynamic inversion counting | Hard |

### **Range Query Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Count of Range Sum | 327 | Prefix sum merging | Hard |
| Number of Pairs Satisfying Inequality | 2426 | Range condition D&C | Hard |

### **Other D&C-Tagged Problems (Quick Reference)**
> These are tagged divide-and-conquer but are usually solved with a technique that lives in a sibling cheatsheet, or are a light reuse of a template above.

| Problem | LC # | Why it is D&C / what to actually use |
|---------|------|--------------------------------------|
| Median of Two Sorted Arrays | 4 | Binary search for the **partition point** in the shorter array — see `binary_search.md`. O(log(min(m,n))). |
| The Skyline Problem | 218 | Split buildings in half, recurse, then **merge two skylines** exactly like merge sort (sweep both key-point lists, output on height change). O(n log n). |
| Sort an Array | 912 | Plain merge sort / 3-way quicksort — the baseline both Template 1 and Template 5 are built on. |
| Convert Sorted Array to BST | 108 | Mid element = root, recurse on both halves. `T(n) = 2T(n/2) + O(1)` → O(n). |
| Convert Sorted List to BST | 109 | Same, but find mid via slow/fast (LC 148 trick), or **inorder-build** in O(n). |
| Construct BT from Preorder + Inorder | 105 | Root from preorder, split inorder at the root index — hash the inorder indices to get O(n) instead of O(n²). |
| Construct BT from Inorder + Postorder | 106 | Same split, but read postorder **right to left** and build the right child first. |
| Construct BT from Preorder + Postorder | 889 | Same idea; the left child's size comes from locating `preorder[i+1]` in postorder (answer is not unique). |
| Maximum Binary Tree | 654 | Root = max of range, recurse on both sides. O(n²) worst; a monotonic stack gives O(n). |
| Balance a BST | 1382 | Inorder-flatten to a sorted array, then apply LC 108. |
| Majority Element | 169 | D&C: majority of a range is the majority of one half; combine by counting. Boyer–Moore voting is O(n)/O(1) and is the expected answer. |
| Range Sum Query - Mutable | 307 | Recursive halving of the index range **is** a segment tree — see `segment_tree.md` / `binary_indexed_tree.md`. |
| Number of Ways to Reorder Array to Get Same BST | 1569 | Split values into left/right subtree sequences, recurse, and interleave with `C(n-1, leftSize)`. |
| Beautiful Array | 932 | Build from halves: `2*A[i]-1` (odds) followed by `2*A[i]` (evens) preserves the property. |
| Super Pow | 372 | Fast exponentiation: `a^b = (a^(b/2))^2 · a^(b%2)`, applied digit-by-digit over the exponent array. |
| Number of 1 Bits / Reverse Bits | 191 / 190 | Bit-level D&C: fold pairs → nibbles → bytes with masks (`0x55555555`, `0x33333333`, …) for O(log w) instead of O(w). |

## LC Examples

### 2-1) Count of Smaller Numbers After Self (LC 315) — Merge Sort with Index Tracking
> During merge, count how many right-half elements moved past each left-half element.

```java
// LC 315 - Count of Smaller Numbers After Self
// IDEA: Merge sort on indices; each right-side element that passes left side increments its count
// time = O(N log N), space = O(N)
public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] counts = new int[n], indices = new int[n];
    for (int i = 0; i < n; i++) indices[i] = i;
    mergeSort(nums, indices, counts, 0, n - 1);
    List<Integer> res = new ArrayList<>();
    for (int c : counts) res.add(c);
    return res;
}
private void mergeSort(int[] nums, int[] idx, int[] counts, int l, int r) {
    if (l >= r) return;
    int mid = (l + r) / 2;
    mergeSort(nums, idx, counts, l, mid);
    mergeSort(nums, idx, counts, mid + 1, r);
    int[] tmp = new int[r - l + 1];
    int i = l, j = mid + 1, k = 0, rightMoved = 0;
    while (i <= mid && j <= r) {
        if (nums[idx[j]] < nums[idx[i]]) { tmp[k++] = idx[j++]; rightMoved++; }
        else { counts[idx[i]] += rightMoved; tmp[k++] = idx[i++]; }
    }
    while (i <= mid) { counts[idx[i]] += rightMoved; tmp[k++] = idx[i++]; }
    while (j <= r) tmp[k++] = idx[j++];
    System.arraycopy(tmp, 0, idx, l, tmp.length);
}
```

```python
def countSmaller(nums):
    """Count how many numbers after each element are smaller"""
    def merge_and_count(indices, temp, left, mid, right):
        # Count smaller elements to the right
        i, j, k = left, mid + 1, left

        while i <= mid and j <= right:
            if nums[indices[i]] <= nums[indices[j]]:
                temp[k] = indices[i]
                # All elements from mid+1 to j-1 are smaller than nums[indices[i]]
                counts[indices[i]] += (j - mid - 1)
                i += 1
            else:
                temp[k] = indices[j]
                j += 1
            k += 1

        # Process remaining elements
        while i <= mid:
            temp[k] = indices[i]
            counts[indices[i]] += (j - mid - 1)
            i += 1
            k += 1
        while j <= right:
            temp[k] = indices[j]
            j += 1
            k += 1

        # Copy back
        for i in range(left, right + 1):
            indices[i] = temp[i]

    def merge_sort(indices, temp, left, right):
        if left >= right:
            return

        mid = (left + right) // 2
        merge_sort(indices, temp, left, mid)
        merge_sort(indices, temp, mid + 1, right)
        merge_and_count(indices, temp, left, mid, right)

    n = len(nums)
    counts = [0] * n
    indices = list(range(n))
    temp = [0] * n

    merge_sort(indices, temp, 0, n - 1)
    return counts
```

### 2-2) Reverse Pairs (LC 493) — Merge Sort Count Before Merge
> Count cross-half pairs (nums[i] > 2*nums[j]) with two pointers before doing the merge.

```java
// LC 493 - Reverse Pairs
// IDEA: Merge sort — count i>2j pairs across halves first, then merge normally
// time = O(N log N), space = O(N)
public int reversePairs(int[] nums) {
    return mergeSort(nums, 0, nums.length - 1);
}
private int mergeSort(int[] nums, int l, int r) {
    if (l >= r) return 0;
    int mid = (l + r) / 2;
    int count = mergeSort(nums, l, mid) + mergeSort(nums, mid + 1, r);
    int j = mid + 1;
    for (int i = l; i <= mid; i++) {
        while (j <= r && nums[i] > 2L * nums[j]) j++;
        count += j - (mid + 1);
    }
    int[] tmp = new int[r - l + 1];
    int i = l, k = 0; j = mid + 1;
    while (i <= mid && j <= r) tmp[k++] = nums[i] <= nums[j] ? nums[i++] : nums[j++];
    while (i <= mid) tmp[k++] = nums[i++];
    while (j <= r)   tmp[k++] = nums[j++];
    System.arraycopy(tmp, 0, nums, l, tmp.length);
    return count;
}
```

```python
def reversePairs(nums):
    """Count pairs where nums[i] > 2 * nums[j] for i < j"""
    def merge_and_count(nums, temp, left, mid, right):
        # Count reverse pairs first
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)

        # Regular merge
        i, j, k = left, mid + 1, left
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp[k] = nums[i]
                i += 1
            else:
                temp[k] = nums[j]
                j += 1
            k += 1

        while i <= mid:
            temp[k] = nums[i]
            i += 1
            k += 1
        while j <= right:
            temp[k] = nums[j]
            j += 1
            k += 1

        for i in range(left, right + 1):
            nums[i] = temp[i]

        return count

    def merge_sort_and_count(nums, temp, left, right):
        count = 0
        if left < right:
            mid = (left + right) // 2
            count += merge_sort_and_count(nums, temp, left, mid)
            count += merge_sort_and_count(nums, temp, mid + 1, right)
            count += merge_and_count(nums, temp, left, mid, right)
        return count

    temp = [0] * len(nums)
    return merge_sort_and_count(nums[:], temp, 0, len(nums) - 1)
```

### 2-3) Count of Range Sum (LC 327) — Merge Sort on Prefix Sums
> Build prefix sum array; count cross-half pairs (prefixSum[j] - prefixSum[i]) in [lower, upper].

```java
// LC 327 - Count of Range Sum
// IDEA: Merge sort on prefix sums; count cross pairs in [lower, upper] before merging
// time = O(N log N), space = O(N)
public int countRangeSum(int[] nums, int lower, int upper) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];
    return mergeSort(prefix, 0, n, lower, upper);
}
private int mergeSort(long[] p, int l, int r, int lo, int hi) {
    if (l >= r) return 0;
    int mid = (l + r) / 2;
    int count = mergeSort(p, l, mid, lo, hi) + mergeSort(p, mid + 1, r, lo, hi);
    int j = mid + 1, k = mid + 1;
    for (int i = l; i <= mid; i++) {
        while (j <= r && p[j] - p[i] < lo) j++;
        while (k <= r && p[k] - p[i] <= hi) k++;
        count += k - j;
    }
    long[] tmp = new long[r - l + 1];
    int i = l, a = mid + 1, idx = 0;
    while (i <= mid && a <= r) tmp[idx++] = p[i] <= p[a] ? p[i++] : p[a++];
    while (i <= mid) tmp[idx++] = p[i++];
    while (a <= r)   tmp[idx++] = p[a++];
    System.arraycopy(tmp, 0, p, l, tmp.length);
    return count;
}
```

```python
def countRangeSum(nums, lower, upper):
    """Count subarrays with sum in [lower, upper]"""
    def merge_and_count(prefix_sums, temp, left, mid, right):
        count = 0
        j = k = mid + 1

        for i in range(left, mid + 1):
            # Find range where prefix_sums[x] - prefix_sums[i] is in [lower, upper]
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            count += k - j

        # Merge sorted arrays
        i, j, p = left, mid + 1, left
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp[p] = prefix_sums[i]
                i += 1
            else:
                temp[p] = prefix_sums[j]
                j += 1
            p += 1

        while i <= mid:
            temp[p] = prefix_sums[i]
            i += 1
            p += 1
        while j <= right:
            temp[p] = prefix_sums[j]
            j += 1
            p += 1

        for i in range(left, right + 1):
            prefix_sums[i] = temp[i]

        return count

    def divide_and_conquer(prefix_sums, temp, left, right):
        if left >= right:
            return 0

        mid = (left + right) // 2
        count = divide_and_conquer(prefix_sums, temp, left, mid)
        count += divide_and_conquer(prefix_sums, temp, mid + 1, right)
        count += merge_and_count(prefix_sums, temp, left, mid, right)
        return count

    # Build prefix sum array
    prefix_sums = [0]
    for num in nums:
        prefix_sums.append(prefix_sums[-1] + num)

    temp = [0] * len(prefix_sums)
    return divide_and_conquer(prefix_sums, temp, 0, len(prefix_sums) - 1)
```

## Advanced Techniques

### Optimization Strategies
```python
def divide_and_conquer_optimizations():
    """Various optimization techniques for D&C"""

    # 1. In-place operations to reduce space
    def in_place_merge(arr, left, mid, right):
        # Reduce auxiliary space usage
        pass

    # 2. Iterative bottom-up approach
    def iterative_merge_sort(arr):
        n = len(arr)
        size = 1
        while size < n:
            left = 0
            while left < n - 1:
                mid = min(left + size - 1, n - 1)
                right = min(left + 2 * size - 1, n - 1)
                # merge(arr, left, mid, right)
                left += 2 * size
            size *= 2

    # 3. Parallel divide and conquer
    def parallel_merge_sort(arr):
        # Use threading for large datasets
        pass

    # 4. Hybrid approach with insertion sort for small subarrays
    def hybrid_merge_sort(arr, threshold=10):
        if len(arr) <= threshold:
            return insertion_sort(arr)
        # Regular merge sort for larger arrays
```

### Custom Merge Logic Patterns
```python
class AdvancedMergePatterns:
    """Advanced merge logic for specific problems"""

    def merge_with_multiple_conditions(self, arr1, arr2):
        """Merge with multiple counting conditions"""
        result = []
        i = j = 0
        counts = {"condition1": 0, "condition2": 0}

        while i < len(arr1) and j < len(arr2):
            if self.condition1(arr1[i], arr2[j]):
                counts["condition1"] += len(arr2) - j
            if self.condition2(arr1[i], arr2[j]):
                counts["condition2"] += len(arr2) - j

            if arr1[i] <= arr2[j]:
                result.append(arr1[i])
                i += 1
            else:
                result.append(arr2[j])
                j += 1

        return result + arr1[i:] + arr2[j:], counts

    def merge_with_reconstruction(self, left_part, right_part):
        """Merge while reconstructing array with specific properties"""
        # Custom merge logic for array reconstruction problems
        pass
```

## Performance Optimization Tips

### Time Complexity Analysis
```python
def complexity_analysis():
    """Analyze time complexity of different D&C approaches"""

    # Standard divide and conquer: T(n) = 2T(n/2) + O(n) = O(n log n)
    # With k-way division: T(n) = kT(n/k) + O(n) = O(n log n) if k is constant
    # With additional work per level: T(n) = 2T(n/2) + O(n^c)
    #   - If c < 1: O(n log n)
    #   - If c = 1: O(n log n)
    #   - If c > 1: O(n^c)

    pass
```

### Space Optimization
```python
def space_optimizations():
    """Techniques to reduce space complexity"""

    # 1. Reuse auxiliary arrays
    def reuse_temp_array(arr):
        temp = [0] * len(arr)  # Create once, reuse everywhere
        # Pass temp to all recursive calls

    # 2. In-place merge (complex but saves space)
    def in_place_merge_technique(arr, left, mid, right):
        # Advanced in-place merging algorithms
        pass

    # 3. Iterative approach to eliminate recursion stack
    def iterative_divide_conquer(arr):
        # Bottom-up approach to save stack space
        pass
```

## Summary & Quick Reference

### Common D&C Patterns

| Pattern | Template | Use Case | Example |
|---------|----------|----------|---------|
| **Basic Inversion** | Modified merge sort | Count inversions | Simple inversion count |
| **Conditional Count** | Merge with conditions | Specific pair conditions | Reverse pairs |
| **Range Queries** | Prefix sum + D&C | Range sum problems | Count range sum |
| **Reconstruction** | Merge with building | Array construction | Sorted array creation |

### Time Complexity Guide
| Problem Type | Time Complexity | Space Complexity | Notes |
|--------------|-----------------|------------------|-------|
| Basic Inversion | O(n log n) | O(n) | Standard merge sort |
| Conditional Inversion | O(n log n) | O(n) | Additional condition checks |
| Range Sum | O(n log n) | O(n) | Prefix sum preprocessing |
| Reconstruction | O(n log n) | O(n) | May need additional structures |

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Forgetting to handle edge cases in merge step
- Incorrect index management during merge
- Not preserving original array when needed
- Inefficient condition checking in merge

**✅ Best Practices:**
- Always use auxiliary array for merge operations
- Handle left and right boundaries carefully
- Optimize condition checking in merge step
- Consider iterative approach for very large inputs
- Use stable sorting when relative order matters

### Interview Tips
1. **Identify D&C opportunities**: Look for inversion counting, range queries
2. **Master merge logic**: The key is in the custom merge step
3. **Handle indices carefully**: Off-by-one errors are common
4. **Consider space-time tradeoffs**: Auxiliary space vs. in-place operations
5. **Practice merge variations**: Different counting/reconstruction logic
6. **Test edge cases**: Empty arrays, single elements, duplicate values

This comprehensive divide and conquer cheatsheet covers the most important patterns and techniques for solving complex counting and range query problems efficiently.