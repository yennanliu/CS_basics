# Quickselect (Kth Element by Partition)

> **Scope** — Partition-based selection: finding the kth largest, kth smallest or k closest element in O(n) average time by keeping only one side of a quicksort partition, with the partition schemes it can use and the median-of-medians pivot that makes the worst case linear. It owns LC 215's quickselect answer; the heap answer is in heap.md.
> **See also**: [heap.md](./heap.md) — the size-k heap alternative, O(n log k) but streaming-friendly; [2_pointers.md](./2_pointers.md) — the Dutch-flag partition (LC 75) this sheet's template is built on; [sort.md](./sort.md) — quicksort itself, and when sorting beats selecting; [advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) — quickselect as the one D&C recurrence that keeps a single side.

## LeetCode Problem Lists

- [Quickselect](https://leetcode.com/problem-list/quickselect/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

## 0) Concept

Quicksort partitions around a pivot and then recurses into **both** sides. Quickselect asks for
one index only, so after each partition it keeps the **one** side that contains that index and
throws the other away. The work is `n + n/2 + n/4 + … ≈ 2n`, so it is **O(n) on average**,
against O(n log n) for sorting and O(n log k) for a size-k heap.

```text
Find the 2nd largest of [3, 2, 1, 5, 6, 4]   →   index n - k = 4 in ascending order

sort everything          [1, 2, 3, 4, 5, 6]          O(n log n)   →  5
quickselect              partition, keep one side    O(n) average →  5
```

### Key Properties

- **Complexity**: O(n) average; O(n²) worst case with an unlucky pivot every round, which a
  random pivot makes vanishingly unlikely, and the median-of-medians pivot removes. O(1) extra
  space when written as a loop.
- **Core Idea**: after a partition, every value left of the pivot's band is smaller and every
  value right of it is larger, so comparing the band with the target index tells you which single
  side still holds the answer.
- **When to Use**: a one-shot "kth largest / kth smallest / k closest" over data you may reorder,
  when the k results do not have to come out sorted.
- **When NOT to Use**: streaming input, read-only input, or k results needed in order: use a
  size-k heap ([heap.md § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-)).

### Quickselect vs Heap vs Sort

| Approach | Average time | Worst time | Extra space | Pick it when |
|---|---|---|---|---|
| Quickselect, random pivot | O(n) | O(n²), vanishingly unlikely | O(1) | one query, input may be reordered |
| Quickselect, median of medians | O(n) | O(n) | O(n) as written here | a worst-case bound is demanded |
| Size-k heap | O(n log k) | O(n log k) | O(k) | streaming, read-only input, k ≪ n |
| Full sort | O(n log n) | O(n log n) | O(1) to O(n) | you need the sorted order anyway |
| Counting / bucket | O(n + range) | O(n + range) | O(range) | small integer range, or frequencies (LC 347) |

### Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "kth largest / kth smallest element", "the median" | n ≤ 10⁵, a single query, follow-up "can you beat O(n log n)?" | [Template 1](#template-1-three-way-partition-quickselect--lc-215-) | values arrive one at a time (LC 703) → a size-k [heap](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) |
| "the k closest points", "the k strongest", "any order is fine" | the k results may be returned unsorted | [Template 2](#template-2-quickselect-on-a-derived-key--lc-973-) | the k results must be sorted → heap, or select then sort the k |
| "kth largest" where the values are strings or tuples | the comparison is not plain `<` | [Template 2](#template-2-quickselect-on-a-derived-key--lc-973-), with that key | |
| "kth smallest in a sorted matrix", "kth smallest pair distance" | the values are not materialised, or there are n² of them | | binary search on the value → [binary_search_on_answer.md](./binary_search_on_answer.md) (LC 378, LC 719) |

## 1) General form

### Template 1: Three-Way Partition Quickselect — LC 215 ⭐⭐⭐⭐⭐

**Recognise** — "find the kth largest element", "the kth smallest", "the median", with one query
and an input you are allowed to reorder. n ≤ 10⁵ and a follow-up asking to beat sorting →
O(n) average.

**Key point** — the 3-way partition leaves `[left, lt) < pivot`, `[lt, gt] == pivot`,
`(gt, right] > pivot`, and every value in each band is already on the correct side of every value
in the others. So if the target index falls inside `[lt, gt]` the answer **is** the pivot, and
otherwise only one band can contain it. The `==` band is what keeps all-equal input O(n): a
2-way partition peels off one element per round on `[2, 2, …, 2]`.

```python
# python
# IDEA: random pivot + 3-way partition (< | == | >); keep only the band that holds index n-k
# time = O(n) average, O(n^2) worst (vanishingly unlikely with a random pivot), space = O(1)
# Stop when that index lands in the == band.
# LC 215 - Kth Largest Element in an Array
import random

def findKthLargest(nums, k):
    target = len(nums) - k            # kth largest = index n-k in ascending order
    left, right = 0, len(nums) - 1
    while True:
        pivot = nums[random.randint(left, right)]
        # Dutch national flag: [left, lt) < pivot, [lt, i) == pivot, (gt, right] > pivot
        lt, i, gt = left, left, right
        while i <= gt:
            if nums[i] < pivot:
                nums[lt], nums[i] = nums[i], nums[lt]
                lt += 1
                i += 1
            elif nums[i] > pivot:
                nums[i], nums[gt] = nums[gt], nums[i]
                gt -= 1                   # do not advance i: the swapped-in value is unchecked
            else:
                i += 1
        if target < lt:
            right = lt - 1                # answer is in the < band
        elif target > gt:
            left = gt + 1                 # answer is in the > band
        else:
            return pivot                  # target index is inside the == band
```

```java
// java
// IDEA: random pivot + 3-way partition; keep only the band that holds index n-k
// time = O(N) average, O(N^2) worst (vanishingly unlikely with a random pivot), space = O(1)
// LC 215 - Kth Largest Element in an Array
import java.util.*;

class Solution {
    private final Random rand = new Random();

    public int findKthLargest(int[] nums, int k) {
        int target = nums.length - k;   // kth largest = index n-k in ascending order
        int left = 0, right = nums.length - 1;
        while (true) {
            int pivot = nums[left + rand.nextInt(right - left + 1)];
            // [left, lt) < pivot, [lt, i) == pivot, (gt, right] > pivot
            int lt = left, i = left, gt = right;
            while (i <= gt) {
                if (nums[i] < pivot) {
                    swap(nums, lt++, i++);
                } else if (nums[i] > pivot) {
                    swap(nums, i, gt--);    // do not advance i
                } else {
                    i++;
                }
            }
            if (target < lt) {
                right = lt - 1;
            } else if (target > gt) {
                left = gt + 1;
            } else {
                return pivot;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
```

**Complexity**
- **Time O(n) average**: the inner `while i <= gt` scan touches every value of the current range
  once, and a random pivot shrinks the range to about half each round, so the scans total
  `n + n/2 + n/4 + … < 2n`. Worst case O(n²) needs a near-extreme pivot every round.
- **Space O(1)**: the partition swaps in place and the outer `while True` replaces recursion.

**Dry run** — `nums = [3, 2, 1, 5, 6, 4]`, k = 2, so target = 4.

```text
round  range   pivot  after the 3-way partition    lt  gt   target 4 is …
1      [0,5]   4      [3, 2, 1, 4, 6, 5]            3   3    > gt  → left = 4
2      [4,5]   5      [3, 2, 1, 4, 5, 6]            4   4    in [lt, gt] → return 5
```

**Traps**
- Kth **largest** is index `n - k` in ascending order. Converting once, up front, is safer than
  flipping every comparison: flipping one of the two branches and not the other returns the kth
  smallest.
- Advancing `i` after the `> pivot` swap skips the value that just arrived from `gt`:
  `[1, 3, 2]` with pivot 1 ends with 2 inside the `==` band, so k = 2 returns 1 instead of 2.
- A first-or-last-element pivot is O(n²) on sorted input, which is exactly LC 215's adversarial
  test. Pick the pivot at random.
- A 2-way (Lomuto) partition is O(n²) on `[2, 2, …, 2]` even with a random pivot; that is what the
  `==` band fixes.
- It reorders `nums`. If the caller still needs the original order, copy first, or use a heap.

**Not this if** — the values arrive as a stream or the kth element is asked again after each
insert (LC 703): a size-k heap ([heap.md § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-))
answers each query in O(log k). If the candidate values are implied rather than stored (the kth
smallest of a sorted matrix, LC 378), binary search on the value instead.

**Drill** — LC 215 ⭐ · LC 75 *(the same partition with a known pivot, as a sort)* ·
LC 1985 *(compare by `(len, s)`)* · LC 324 *(select the median, then interleave)*

### Template 2: Quickselect on a Derived Key — LC 973 ⭐⭐⭐⭐

**Recognise** — "return the k closest points", "the k weakest rows", "the k strongest values",
"in any order": you need the **set** of the k best, not their order, and "best" is a key computed
from each item.

**Key point** — once the partition settles index `k - 1`, everything in `items[0 .. k-1]` has a
key ≤ every key after it, and that prefix **is** the answer even though it is unsorted. So the
loop stops when `k - 1` falls in the `==` band, and it never has to sort the k results.

```python
# python
# LC 973 - K Closest Points to Origin
# IDEA: Template 1 on key = squared distance; stop once index k-1 is settled, return the prefix
# time = O(n) average, space = O(1) extra (the returned slice is the output)
import random

def kClosest(points, k):
    def dist(p):
        return p[0] * p[0] + p[1] * p[1]     # squared: same order as the distance, no sqrt
    target = k - 1
    left, right = 0, len(points) - 1
    while left < right:
        pivot = dist(points[random.randint(left, right)])
        lt, i, gt = left, left, right
        while i <= gt:
            d = dist(points[i])
            if d < pivot:
                points[lt], points[i] = points[i], points[lt]
                lt += 1
                i += 1
            elif d > pivot:
                points[i], points[gt] = points[gt], points[i]
                gt -= 1
            else:
                i += 1
        if target < lt:
            right = lt - 1
        elif target > gt:
            left = gt + 1
        else:
            break                             # index k-1 is in the == band: the prefix is settled
    return points[:k]
```

```java
// java
// LC 973 - K Closest Points to Origin
// IDEA: 3-way quickselect on the squared distance; once index k-1 is settled, points[0..k-1] is the answer
// time = O(N) average, space = O(1) extra
import java.util.*;

class Solution {
    private final Random rand = new Random();

    public int[][] kClosest(int[][] points, int k) {
        int target = k - 1;
        int left = 0, right = points.length - 1;
        while (left < right) {
            long pivot = dist(points[left + rand.nextInt(right - left + 1)]);
            int lt = left, i = left, gt = right;
            while (i <= gt) {
                long d = dist(points[i]);
                if (d < pivot) {
                    swap(points, lt++, i++);
                } else if (d > pivot) {
                    swap(points, i, gt--);
                } else {
                    i++;
                }
            }
            if (target < lt) {
                right = lt - 1;
            } else if (target > gt) {
                left = gt + 1;
            } else {
                break;
            }
        }
        return Arrays.copyOfRange(points, 0, k);
    }

    private long dist(int[] p) {
        return (long) p[0] * p[0] + (long) p[1] * p[1];   // long: 2 * (10^4)^2 fits, but stay safe
    }

    private void swap(int[][] a, int i, int j) {
        int[] tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
```

**Complexity**
- **Time O(n) average**: the same halving argument as Template 1. `dist` is O(1), so a derived
  key costs nothing extra as long as it is cheap; an expensive key should be computed once into a
  parallel array first.
- **Space O(1) extra**: the partition swaps rows in place; only the output slice is new.

**Traps**
- Using `sqrt` makes the key a float for no gain: squared distance has the same order.
- `points[:k]` before the loop has finished returns an arbitrary prefix. Return only after
  `k - 1` is settled, or after `left >= right`.
- If the problem then asks for the k results **sorted**, sort the prefix: O(n + k log k), still
  better than sorting everything.

**Not this if** — the points arrive one at a time, or k is tiny and the input huge and read-only:
a size-k max-heap keyed by distance (heap.md) does it in O(n log k) without reordering anything.

**Drill** — LC 973 ⭐ · LC 1337 *(key = `(soldiers, row)`)* · LC 1471 *(key = distance from the
median, which is itself a quickselect)* · LC 347 *(key = frequency; bucket sort is the true O(n)
answer)*

### Partition Schemes — Lomuto, Hoare and Three-Way ⭐⭐⭐

The templates above use the three-way partition. The other two are what interviewers mean by
"the quicksort partition", and each changes what the loop is allowed to test.

| Scheme | Returns | Loop test | Weak spot |
|---|---|---|---|
| Lomuto | the pivot's final index `p` | `p == target` → done | O(n²) on many duplicates |
| Hoare | a split `j` with `a[lo..j] <= pivot <= a[j+1..hi]` | `target <= j` → `hi = j`, else `lo = j + 1`, until `lo == hi` | `nums[j]` need not be the pivot |
| Three-way | the band `[lt, gt]` equal to the pivot | target in the band → done | none for selection; a few more swaps |

```python
# python
# Lomuto and Hoare partitions, each with the selection loop it needs
# IDEA: Lomuto settles the pivot at p, so p == target ends the search; Hoare only splits the
#       range at j, so the loop narrows to one side until a single index is left
# time = O(n) average for either selection loop, space = O(1)
import random

def partition_lomuto(nums, left, right):
    pivot = nums[right]                   # the caller has already moved a random pivot to `right`
    store = left
    for j in range(left, right):
        if nums[j] < pivot:
            nums[store], nums[j] = nums[j], nums[store]
            store += 1
    nums[store], nums[right] = nums[right], nums[store]
    return store                          # the pivot's final index

def kth_smallest_lomuto(nums, target):
    left, right = 0, len(nums) - 1
    while True:
        r = random.randint(left, right)
        nums[r], nums[right] = nums[right], nums[r]
        p = partition_lomuto(nums, left, right)
        if p == target:
            return nums[p]
        if p < target:
            left = p + 1
        else:
            right = p - 1

def partition_hoare(nums, left, right):
    pivot = nums[(left + right) // 2]
    i, j = left - 1, right + 1
    while True:
        i += 1
        while nums[i] < pivot:
            i += 1
        j -= 1
        while nums[j] > pivot:
            j -= 1
        if i >= j:
            return j                      # a split point, not the pivot's index
        nums[i], nums[j] = nums[j], nums[i]

def kth_smallest_hoare(nums, target):
    lo, hi = 0, len(nums) - 1
    while lo < hi:
        j = partition_hoare(nums, lo, hi)
        if target <= j:
            hi = j
        else:
            lo = j + 1
    return nums[lo]
```

```java
// java
// IDEA: hoare() only guarantees a[lo..j] <= pivot <= a[j+1..hi]: narrow with target <= j until lo == hi
// time = O(N) expected, space = O(1)
// Never test p == target.
// LC 215 - Kth Largest Element in an Array, Hoare partition
import java.util.*;

class Solution {
    private final Random rnd = new Random();

    public int findKthLargest(int[] nums, int k) {
        int target = nums.length - k;
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int j = hoare(nums, lo, hi);
            if (target <= j) {
                hi = j;
            } else {
                lo = j + 1;
            }
        }
        return nums[lo];
    }

    private int hoare(int[] a, int lo, int hi) {
        int pivot = a[lo + rnd.nextInt(hi - lo + 1)];   // pivot by VALUE, chosen at random
        int i = lo - 1, j = hi + 1;
        while (true) {
            do { i++; } while (a[i] < pivot);
            do { j--; } while (a[j] > pivot);
            if (i >= j) {
                return j;
            }
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }
}
```

**Traps**
- Hoare with Lomuto's loop (`p == target`, `right = p - 1`) is the classic wrong answer: the
  returned `j` is a boundary, not the pivot's index. `[2, 3, 1]`, target 0: the split around 3
  gives `[2, 1, 3]` with `j = 1`, the loop sets `right = 0` as if index 1 held a settled pivot,
  and returns 2, but the smallest value, 1, was at index 1.
- Lomuto with `<=` instead of `<` in the scan still works, but it is the same O(n²) on duplicates;
  only the three-way band fixes that.

### Median of Medians — Where a Linear Worst Case Comes From ⭐⭐

Interviewers ask *why* a guaranteed O(n) exists, rarely for the code. Split the values into groups
of five, take each group's median, and use the median of those medians as the pivot. At least
3/10 of the values are then on each side of it, so the recurrence is
`T(n) ≤ T(n/5) + T(7n/10) + O(n)`, and since `1/5 + 7/10 < 1` it solves to O(n).

```python
# python
# Median-of-medians select: the value at index `target` of sorted(nums)
# IDEA: pivot = median of the group-of-5 medians, found recursively; it guarantees at least
#       3/10 of the values fall on each side, so each round discards a constant fraction
# time = O(n) worst case, space = O(n) (this version builds new lists instead of partitioning in place)
def select(nums, target):
    while True:
        if len(nums) <= 5:
            return sorted(nums)[target]
        medians = []
        for start in range(0, len(nums), 5):
            group = sorted(nums[start:start + 5])
            medians.append(group[len(group) // 2])
        pivot = select(medians, len(medians) // 2)
        less = [x for x in nums if x < pivot]
        equal_count = sum(1 for x in nums if x == pivot)
        if target < len(less):
            nums = less
        elif target < len(less) + equal_count:
            return pivot
        else:
            target -= len(less) + equal_count
            nums = [x for x in nums if x > pivot]
```

The constant factor is large, which is why the random pivot is what anyone actually writes.

## 2) LC Example

### Find the Kth Largest Integer in the Array — LC 1985

> **Key point** — the values are numeric **strings** with no leading zeros, so a longer string is
> always the larger number, and two strings of the same length compare correctly as text. The key
> `(len(s), s)` turns string order into numeric order without ever converting to `int`.

**Reduces to**: [Template 2](#template-2-quickselect-on-a-derived-key--lc-973-) — the key is
`(len(s), s)`, and the answer is a single index, as in Template 1.

```python
# python
# LC 1985 - Find the Kth Largest Integer in the Array
# IDEA: 3-way quickselect on key (len, s); index n-k in ascending order is the kth largest
# time = O(n * L) average, L = the longest string (each comparison is O(L)), space = O(1)
import random

def kthLargestNumber(nums, k):
    def key(s):
        return (len(s), s)
    target = len(nums) - k
    left, right = 0, len(nums) - 1
    while True:
        pivot = key(nums[random.randint(left, right)])
        lt, i, gt = left, left, right
        while i <= gt:
            current = key(nums[i])
            if current < pivot:
                nums[lt], nums[i] = nums[i], nums[lt]
                lt += 1
                i += 1
            elif current > pivot:
                nums[i], nums[gt] = nums[gt], nums[i]
                gt -= 1
            else:
                i += 1
        if target < lt:
            right = lt - 1
        elif target > gt:
            left = gt + 1
        else:
            return nums[lt]
```

**Traps**
- Comparing the strings as text alone puts `"10"` before `"6"`.
- `int(s)` works in Python but not in Java: the strings can be 100 digits long.

### Other Problems on the Same Skeleton

| Problem | The twist | Home |
|---|---|---|
| LC 215 Kth Largest Element in an Array | the anchor | [Template 1](#template-1-three-way-partition-quickselect--lc-215-); heap answer in [heap.md](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) |
| LC 347 Top K Frequent Elements | quickselect the (value, count) entries by count; bucket sort by count is the true O(n) | [heap.md](./heap.md) |
| LC 324 Wiggle Sort II | select the median, 3-way partition around it, then write through the virtual index `(1 + 2*i) % (n \| 1)` so equal medians end up apart | this skeleton |
| LC 1738 Kth Largest XOR Coordinate Value | build the 2-D prefix-XOR grid first, then select over its `m*n` values | this skeleton |
| LC 75 Sort Colors | the three-way partition itself, with the pivot fixed at 1 | [2_pointers.md](./2_pointers.md) |
| LC 378 / LC 719 kth smallest in a matrix / pair distance | the values are implied, not stored: binary search on the value instead | [binary_search_on_answer.md](./binary_search_on_answer.md) |

## 3) Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 215 · LC 973 | the three-way loop; a derived key and an unsorted prefix as the answer |
| **Practice** | LC 75 · LC 1985 · LC 1337 · LC 347 | the partition alone; a string key; a tuple key; selection over a frequency map |
| **Stretch** | LC 1471 · LC 324 · LC 1738 | select the median, then select again; median plus virtual indexing; select over a derived grid |
