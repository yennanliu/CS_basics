# Binary Search — Worked Examples

> **Scope** — The problem set for [binary_search.md](./binary_search.md): each problem filed under the index-space template it reduces to, as one problem card with one canonical solution, plus the few answer-space problems that are not worked on [binary_search_on_answer.md](./binary_search_on_answer.md).
> **See also** — *parent sheet*: [binary_search.md](./binary_search.md) — the loop invariants, the boundary templates and the "which template" decision table; [binary_search_on_answer.md](./binary_search_on_answer.md) — every problem that binary searches the *answer* rather than an index.
> *Neighbouring sheets*: [n_sum.md](./n_sum.md) — the converging-pointer answer to LC 167; [sort.md](./sort.md) — the sort-then-LIS answer to LC 354; [patience_sorting.md](./patience_sorting.md) — the problems that reduce to LIS; [matrix.md](./matrix.md) — the 2D-grid family.

## LeetCode Problem Lists

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

## Overview

[binary_search.md](./binary_search.md) holds the eight index-space templates; this sheet is the
problems. Each section below mirrors one template, in the core sheet's order, and every card
names the one step it adds to that template. Templates whose anchor problem is the whole lesson
(Template 4, 6 and 8) have no section here. The last section holds the answer-space problems
that reduce to [binary_search_on_answer.md](./binary_search_on_answer.md)'s cards, and the
pointer table lists the problems worked on another sheet.

## Index

| Pattern card | Problems (easy → hard) |
|---|---|
| [Template 1: Exact Match on a Closed Interval](./binary_search.md#template-1-exact-match-on-a-closed-interval--lc-704-) | [LC 4](#median-of-two-sorted-arrays--lc-4-) |
| [Template 2: Lower and Upper Bound](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) | [LC 278](#first-bad-version--lc-278) · [LC 744](#find-smallest-letter-greater-than-target--lc-744) · [LC 1150](#check-if-a-number-is-majority-element-in-a-sorted-array--lc-1150) · [LC 540](#single-element-in-a-sorted-array--lc-540) · [LC 658](#find-k-closest-elements--lc-658) · [LC 436](#find-right-interval--lc-436-) |
| [Template 3: Rotated Array Minimum](./binary_search.md#template-3-rotated-array-minimum--lc-153-) | [LC 154](#find-minimum-in-rotated-sorted-array-ii--lc-154) |
| [Template 7: Peak Finding by Slope](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) | [LC 852](#peak-index-in-a-mountain-array--lc-852) · [LC 1095](#find-in-mountain-array--lc-1095-) |
| [binary_search_on_answer § Template 1: Smallest Feasible Value](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) | [LC 367](#valid-perfect-square--lc-367) · [LC 1060](#missing-element-in-sorted-array--lc-1060) |
| [binary_search_on_answer § Template 2: Largest Feasible Value](./binary_search_on_answer.md#template-2-largest-feasible-value--lc-1552-) | [LC 441](#arranging-coins--lc-441) |

**Lives elsewhere** — worked once, on the sheet that owns it:

| LC | Problem | Home | Why there |
|---|---|---|---|
| 162 | Find Peak Element | [binary_search.md § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) | the template's anchor problem |
| 300 | Longest Increasing Subsequence | [binary_search.md § Template 5](./binary_search.md#template-5-lower-bound-on-a-maintained-tails-array--lc-300-) | the template's anchor problem |
| 354 | Russian Doll Envelopes | [sort.md § Template 9](./sort.md#template-9-sort-by-a-derived-key-to-unlock-greedy--dp--lc-354-) | the sort-with-a-tie-rule is the lesson; the LIS after it is Template 5 |
| 167 | Two Sum II — Input Array Is Sorted | [n_sum.md](./n_sum.md#2-1-2-sum-ii--lc-167) | two pointers is O(n); a binary search per element is O(n log n) |
| 209 | Minimum Size Subarray Sum | [sliding_window.md § Template 4](./sliding_window.md#template-4-shortest-window-satisfying-p--lc-209-) | the window is O(n); prefix sums + a lower bound per start is the O(n log n) follow-up |
| 69 | Sqrt(x) | [math.md § Integer Square Root](./math.md#1-1-10-integer-square-root--binary-search--newton-) | binary search on the answer and Newton's method, side by side |
| 528 | Random Pick with Weight | [math.md § Randomized Sampling](./math.md#1-1-11-randomized-sampling-templates-) | prefix sums + a lower bound on a uniform draw |
| 981 | Time Based Key-Value Store | [design_examples.md](./design_examples.md#time-based-key-value-store--lc-981) | a per-key sorted list, queried with upper bound − 1 |
| 287 | Find the Duplicate Number | [binary_search_on_answer.md § Find the Duplicate Number](./binary_search_on_answer.md#find-the-duplicate-number--lc-287-) | binary search over the value domain, with a pigeonhole count |

## Exact Match Examples

### Median of Two Sorted Arrays — LC 4 ⭐⭐⭐⭐⭐

> **Key point** — search the **cut** in the shorter array, not a value. A cut `i` in `nums1`
> forces the cut `j = half − i` in `nums2`; the cut is right when both left maxima are `<=`
> both right minima, and when it is wrong, which inequality failed says which way to move.

**Reduces to**: [binary_search § Template 1](./binary_search.md#template-1-exact-match-on-a-closed-interval--lc-704-) — the same closed-interval loop with three outcomes (found, go left, go right), over cut positions `[0, m]` instead of indices, and a cross-array test instead of `nums[mid] == target`.

```python
# python
# LC 4 - Median of Two Sorted Arrays
# IDEA: binary search the cut i in the shorter array; j = half - i; stop when left maxima <= right minima
# time = O(log(min(m, n))), space = O(1)
def findMedianSortedArrays(nums1, nums2):
    if len(nums1) > len(nums2):
        nums1, nums2 = nums2, nums1          # search the shorter array
    m, n = len(nums1), len(nums2)
    half = (m + n + 1) // 2                  # size of the left half (the larger one when odd)
    left, right = 0, m                       # i = how many of nums1 go to the left half
    while left <= right:
        i = left + (right - left) // 2
        j = half - i
        left1 = nums1[i - 1] if i > 0 else float('-inf')
        right1 = nums1[i] if i < m else float('inf')
        left2 = nums2[j - 1] if j > 0 else float('-inf')
        right2 = nums2[j] if j < n else float('inf')
        if left1 <= right2 and left2 <= right1:          # a valid cut
            if (m + n) % 2 == 1:
                return max(left1, left2)
            return (max(left1, left2) + min(right1, right2)) / 2
        if left1 > right2:
            right = i - 1                    # took too many from nums1
        else:
            left = i + 1                     # took too few from nums1
    return 0.0                               # unreachable for sorted input
```

```java
// java
// LC 4 - Median of Two Sorted Arrays
// IDEA: binary search the cut in the shorter array; the other cut follows from the half size
// time = O(log(min(m, n))), space = O(1)
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
    int m = nums1.length, n = nums2.length;
    int half = (m + n + 1) / 2;
    int left = 0, right = m;
    while (left <= right) {
        int i = left + (right - left) / 2;
        int j = half - i;
        int left1  = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
        int right1 = i == m ? Integer.MAX_VALUE : nums1[i];
        int left2  = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
        int right2 = j == n ? Integer.MAX_VALUE : nums2[j];
        if (left1 <= right2 && left2 <= right1) {
            if ((m + n) % 2 == 1) return Math.max(left1, left2);
            return (Math.max(left1, left2) + (double) Math.min(right1, right2)) / 2.0;
        } else if (left1 > right2) {
            right = i - 1;
        } else {
            left = i + 1;
        }
    }
    return 0.0;
}
```

**Traps**
- Searching the longer array lets `j = half − i` go negative: `nums1 = [1, 2, 3, 4]`,
  `nums2 = [5]` can probe `i = 4`, so `j = −1`. Swap so `nums1` is the shorter one.
- An empty side needs the `±inf` sentinels: `nums1 = []`, `nums2 = [1]` has `i = 0`, so there is
  no `nums1[i − 1]`.
- In Java, `max + min` of two large ints overflows before the division: add as `double`.

**Similar** — LC 2040 *(k-th smallest product: [binary_search_on_answer § Template 4](./binary_search_on_answer.md#template-4-count-at-most-over-a-value-range--lc-378-))*

## Lower and Upper Bound Examples

### First Bad Version — LC 278

> **Key point** — `isBadVersion` reads `False … False True … True`, so the first bad version is
> Template 2's `findLeft` with the API call as the comparison. There is no array and no target.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — `findLeft` with `isBadVersion(mid)` in place of `nums[mid] >= target`; the answer always exists, so no validation.

```python
# python
# LC 278 - First Bad Version
# IDEA: lower bound on the boolean API: the first version where isBadVersion turns True
# time = O(log n), space = O(1)
def firstBadVersion(n, isBadVersion):
    left, right = 1, n
    while left < right:
        mid = left + (right - left) // 2
        if isBadVersion(mid):
            right = mid              # mid is bad: the first bad one is mid or earlier
        else:
            left = mid + 1           # mid is good: the first bad one is after it
    return left
```

```java
// java
// LC 278 - First Bad Version
// IDEA: lower bound on the boolean API
// time = O(log n), space = O(1)
public int firstBadVersion(int n) {
    int left = 1, right = n;
    while (left < right) {
        int mid = left + (right - left) / 2;   // (left + right) overflows when n is near 2^31
        if (isBadVersion(mid)) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

**Traps**
- `(left + right) / 2` overflows in Java: `n = 2126753390` is one of LeetCode's own tests.

**Similar** — LC 374 *(guess API, exact match)* · LC 35 *(insert position)*

### Find Smallest Letter Greater Than Target — LC 744

> **Key point** — "smallest letter **strictly greater** than target" is an upper bound (the first
> letter `> target`). When none is greater, the answer wraps to `letters[0]`, which `% n` does for free.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — `findRight + 1` (upper bound) on characters, then wrap the index.

```python
# python
# LC 744 - Find Smallest Letter Greater Than Target
# IDEA: upper bound (first letter > target) on a half-open range, then wrap with % n
# time = O(log n), space = O(1)
def nextGreatestLetter(letters, target):
    left, right = 0, len(letters)            # half-open [left, right): right == n means "none"
    while left < right:
        mid = left + (right - left) // 2
        if letters[mid] <= target:
            left = mid + 1                   # not strictly greater: the answer is to the right
        else:
            right = mid
    return letters[left % len(letters)]      # left == n wraps around to letters[0]
```

**Traps**
- `letters[mid] < target` finds the first letter `>= target`: `letters = ["c","f","j"]`,
  `target = "c"` returns `"c"` instead of `"f"`.
- Duplicates are allowed: `["e","e","e","n"]`, `target = "e"` must skip every `"e"`, which `<=` does.

### Check If a Number Is Majority Element in a Sorted Array — LC 1150

> **Key point** — in a sorted array a value occupies one contiguous run. Find where `target`'s
> run starts; it is a majority exactly when the element `n // 2` places later is still `target`.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — one `findLeft`, then a single O(1) check instead of a second search.

```python
# python
# LC 1150 - Check If a Number Is Majority Element in a Sorted Array
# IDEA: lower bound = start of target's run; majority iff nums[start + n // 2] == target
# time = O(log n), space = O(1)
def isMajorityElement(nums, target):
    n = len(nums)
    left, right = 0, n
    while left < right:                      # first index with nums[i] >= target
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    end = left + n // 2                      # a run longer than n / 2 must still cover this index
    return end < n and nums[end] == target
```

```java
// java
// LC 1150 - Check If a Number Is Majority Element in a Sorted Array
// IDEA: lower bound = start of target's run; majority iff nums[start + n / 2] == target
// time = O(log n), space = O(1)
public boolean isMajorityElement(int[] nums, int target) {
    int n = nums.length;
    int left = 0, right = n;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) left = mid + 1;
        else right = mid;
    }
    int end = left + n / 2;
    return end < n && nums[end] == target;
}
```

```text
nums = [2,4,5,5,5,5,5,6,6], target = 5, n = 9
lower bound of 5 -> index 2
index 2 + 9 // 2 = 6, nums[6] = 5 == target -> true

index:  0  1  2  3  4  5  6  7  8
value:  2  4 [5] 5  5  5 [5] 6  6
             start        ^ still 5 iff the run is longer than n / 2
```

**Traps**
- Skipping the `end < n` check: `nums = [1, 2]`, `target = 2` starts at 1, and `1 + 1 = 2` is out of range.
- Target absent: the lower bound lands on a different value, and the check still returns `False`
  without a separate "found" test.

**Similar** — LC 34 *(both ends of the run)* · LC 169 *(unsorted majority: Boyer–Moore)*

### Single Element in a Sorted Array — LC 540

> **Key point** — before the single element, every pair starts at an **even** index; after it,
> at an odd one. So "the pair at even `mid` is intact" is `True … True False … False`, and the
> single element is the first even index where it fails.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — a lower bound on a parity predicate, probing even indices only.

```python
# python
# LC 540 - Single Element in a Sorted Array
# IDEA: probe even mids; an intact pair (nums[mid] == nums[mid+1]) means the single is to the right
# time = O(log n), space = O(1)
def singleNonDuplicate(nums):
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if mid % 2 == 1:
            mid -= 1                         # align mid to the start of a pair
        if nums[mid] == nums[mid + 1]:
            left = mid + 2                   # pair intact: the single is after this pair
        else:
            right = mid                      # pair broken: the single is at mid or before
    return nums[left]
```

```java
// java
// LC 540 - Single Element in a Sorted Array
// IDEA: probe even mids; an intact pair means the single is to the right
// time = O(log n), space = O(1)
public int singleNonDuplicate(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (mid % 2 == 1) mid--;
        if (nums[mid] == nums[mid + 1]) left = mid + 2;
        else right = mid;
    }
    return nums[left];
}
```

**Traps**
- Without aligning `mid` to even, `nums[mid] == nums[mid + 1]` can match the second half of a
  pair: `[1,1,2,3,3,4,4]` probes `mid = 3`, sees `3 == 3`, jumps to `left = 5` and returns 4 instead of 2.
- `mid + 1` is in range because `left < right` and `right` is always even (the length is odd).

**Similar** — LC 136 *(unsorted: XOR in O(n))*

### Find K Closest Elements — LC 658

> **Key point** — the answer is a window `arr[i .. i + k − 1]`, so search the window's **start**.
> Compare the two elements that a shift would swap: if `x − arr[i] > arr[i + k] − x`, the window
> starting at `i + 1` is at least as close, and that test turns from `True` to `False` once.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — a lower bound over window starts `[0, n − k]`, with the predicate "shifting right is no better".

```python
# python
# LC 658 - Find K Closest Elements
# IDEA: binary search the window start i in [0, n - k]; move right while arr[i] is farther than arr[i + k]
# time = O(log(n - k) + k), space = O(1) besides the output
def findClosestElements(arr, k, x):
    left, right = 0, len(arr) - k
    while left < right:
        mid = left + (right - left) // 2
        if x - arr[mid] > arr[mid + k] - x:
            left = mid + 1                   # arr[mid] is farther than arr[mid + k]: start later
        else:
            right = mid                      # arr[mid] is at least as close (ties keep the smaller)
    return arr[left:left + k]
```

**Traps**
- `abs(x - arr[mid]) > abs(arr[mid + k] - x)` breaks when both sit on the same side of `x`:
  `arr = [1,1,2,2,2,2,2,3,3]`, `k = 3`, `x = 3` returns `[2,2,2]` instead of `[2,3,3]`. Keep the
  signed differences.
- `>=` instead of `>` breaks ties toward the larger value; the problem wants the smaller one:
  `arr = [1,2,3,4,5]`, `k = 4`, `x = 3` must return `[1,2,3,4]`.

**Second approach** — shrink from both ends (drop whichever end is farther) until `k` remain:
O(n − k), the version to give when the binary-search predicate does not come to mind. Sorting by
distance ([sort.md](./sort.md)) is O(n log n).

### Find Right Interval — LC 436 ⭐⭐⭐⭐

> **Key point** — for each interval, the answer is the **smallest start `>= end_i`** over all
> intervals: a lower bound over the sorted starts. The answer must be the *original* index, so
> sort `(start, index)` pairs and read the index off the pair the search lands on.

**Reduces to**: [binary_search § Template 2](./binary_search.md#template-2-lower-and-upper-bound--lc-34-) — one `findLeft` per interval, on starts sorted together with their original indices.

```python
# python
# LC 436 - Find Right Interval
# IDEA: sort (start, original index) once; per interval, lower-bound end_i among the starts
# time = O(n log n), space = O(n)
import bisect

def findRightInterval(intervals):
    pairs = sorted((start, i) for i, (start, _) in enumerate(intervals))
    starts = [start for start, _ in pairs]           # bisect needs a plain sorted list
    result = []
    for _, end in intervals:
        j = bisect.bisect_left(starts, end)          # findLeft: first start >= end
        result.append(pairs[j][1] if j < len(pairs) else -1)
    return result
```

```text
intervals = [[3,4],[2,3],[1,2]]          original indices 0, 1, 2
pairs     = [(1,2), (2,1), (3,0)]        sorted by start
[3,4]: end 4 -> no start >= 4            -> -1
[2,3]: end 3 -> first start >= 3 = (3,0) -> 0
[1,2]: end 2 -> first start >= 2 = (2,1) -> 1
```

**Traps**
- Sorting the intervals themselves loses the index the answer needs: pair each start with its
  index **before** sorting.
- An interval may be its own right interval: `[[1,1]]` returns `[0]` (start `1 >= end 1`).
- A nested scan is O(n²) and times out at n = 2·10⁴; the whole point is one O(log n) search per interval.

**Similar** — LC 35 *(the bare lower bound)* · LC 2300 *(lower bound per spell, count the suffix)* · LC 1268 *(lower bound the growing prefix)*

## Rotated Array Minimum Examples

### Find Minimum in Rotated Sorted Array II — LC 154

> **Key point** — duplicates add a third case: `nums[mid] == nums[right]` says nothing about which
> run `mid` is in, but `nums[right]` is not the unique minimum then (`nums[mid]` equals it), so
> `right -= 1` drops it safely. The worst case becomes O(n).

**Reduces to**: [binary_search § Template 3](./binary_search.md#template-3-rotated-array-minimum--lc-153-) — the same half-open squeeze against `nums[right]`, plus a one-step shrink on equality.

```python
# python
# LC 154 - Find Minimum in Rotated Sorted Array II
# IDEA: compare mid with right; on equality the right end is a duplicate, so drop it
# time = O(log n) average, O(n) worst case (all equal); space = O(1)
def findMin(nums):
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] < nums[right]:
            right = mid                      # mid is in the right run: min is at mid or left of it
        elif nums[mid] > nums[right]:
            left = mid + 1                   # mid is in the left run: min is after mid
        else:
            right -= 1                       # equal: nums[right] has a copy at mid, drop it
    return nums[left]
```

```java
// java
// LC 154 - Find Minimum in Rotated Sorted Array II
// IDEA: compare mid with right; on equality drop the right end
// time = O(log n) average, O(n) worst case; space = O(1)
public int findMin(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < nums[right]) right = mid;
        else if (nums[mid] > nums[right]) left = mid + 1;
        else right--;
    }
    return nums[left];
}
```

**Traps**
- Comparing with `nums[left]` instead: `[1, 3, 3]` is unrotated, `nums[mid] >= nums[left]`
  sends the search right and misses the `1` at index 0.
- The O(n) worst case is real: `[1,1,1,1,0,1,1]` cannot be decided by any probe. Say so when asked.

**Similar** — LC 81 *(search with duplicates: the same `right -= 1` fallback on Template 4)*

## Peak Finding Examples

### Peak Index in a Mountain Array — LC 852

> **Key point** — a mountain has exactly one peak, so Template 7 needs no change: walk toward the
> higher neighbour of `mid` until the window is one index.

**Reduces to**: [binary_search § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) — unchanged; the mountain guarantee makes the peak unique.

```python
# python
# LC 852 - Peak Index in a Mountain Array
# IDEA: half-open squeeze toward the higher neighbour; the window always holds the peak
# time = O(log n), space = O(1)
def peakIndexInMountainArray(arr):
    left, right = 0, len(arr) - 1
    while left < right:
        mid = left + (right - left) // 2
        if arr[mid] < arr[mid + 1]:
            left = mid + 1                   # rising: the peak is to the right
        else:
            right = mid                      # falling: the peak is mid or to the left
    return left
```

**Similar** — LC 162 *(any peak, no mountain guarantee)* · LC 1095 *(below)* · LC 1901 *(2D peak)*

### Find in Mountain Array — LC 1095 ⭐⭐⭐⭐⭐

> **Key point** — a mountain is two sorted runs glued at the peak. Find the peak with
> Template 7, then search the ascending left run first (the smallest index wins), and only then
> the descending right run, with the comparison flipped. Three O(log n) searches stay under the
> 100-call cap on `get()`.

**Reduces to**: [binary_search § Template 7](./binary_search.md#template-7-peak-finding-by-slope--lc-162-) — the peak search, then Template 1 twice: as written on the left run, and with the direction flipped on the right run.

```python
# python
# LC 1095 - Find in Mountain Array
# IDEA: peak via the slope search, then an ascending search on [0, peak] and a descending one on [peak + 1, n - 1]
# time = O(log n), space = O(1)
class Solution:
    def findInMountainArray(self, target, mountain_arr):
        n = mountain_arr.length()

        left, right = 0, n - 1                       # 1) the peak
        while left < right:
            mid = left + (right - left) // 2
            if mountain_arr.get(mid) < mountain_arr.get(mid + 1):
                left = mid + 1
            else:
                right = mid
        peak = left

        index = self.search(mountain_arr, target, 0, peak, True)       # 2) ascending half first
        if index != -1:
            return index
        return self.search(mountain_arr, target, peak + 1, n - 1, False)  # 3) descending half

    def search(self, arr, target, left, right, ascending):
        while left <= right:
            mid = left + (right - left) // 2
            value = arr.get(mid)
            if value == target:
                return mid
            if (value < target) == ascending:        # ascending: too small -> right; descending: too big -> right
                left = mid + 1
            else:
                right = mid - 1
        return -1
```

```java
// java
// LC 1095 - Find in Mountain Array
// IDEA: peak via the slope search, then an ascending and a descending Template 1 search
// time = O(log n), space = O(1)
class Solution {
    public int findInMountainArray(int target, MountainArray arr) {
        int n = arr.length();
        int left = 0, right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid) < arr.get(mid + 1)) left = mid + 1;
            else right = mid;
        }
        int peak = left;

        int index = search(arr, target, 0, peak, true);
        if (index != -1) return index;
        return search(arr, target, peak + 1, n - 1, false);
    }

    private int search(MountainArray arr, int target, int left, int right, boolean ascending) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int value = arr.get(mid);
            if (value == target) return mid;
            if ((value < target) == ascending) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
}
```

```text
arr = [1,2,3,4,5,3,1], target = 3
peak search  -> peak = 4 (value 5)
ascending  [0, 4]: mid 2 -> 3 == target -> return 2     (index 5 also holds 3, but 2 is smaller)
```

**Traps**
- Searching the right run first returns the larger index: `[1,2,3,4,5,3,1]`, `target = 3` must be 2, not 5.
- Using the ascending comparison on the right run walks the wrong way: `[0,5,3,1]`, `target = 1`
  is never found.
- A linear scan for the peak exceeds the call budget at n = 10⁴.

**Similar** — LC 852 *(the peak alone)* · LC 33 *(two sorted runs, split by rotation: [binary_search § Template 4](./binary_search.md#template-4-search-a-rotated-array--lc-33-))*

## Search on the Answer Examples

### Valid Perfect Square — LC 367

> **Key point** — search the root, not the array: the smallest `r` with `r * r >= num` is the
> first `True` of a monotone predicate over `[1, num]`. `num` is a perfect square exactly when
> that `r` squares to it.

**Reduces to**: [binary_search_on_answer § Template 1](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) — smallest feasible value with `feasible(r) = r * r >= num`, then one equality check.

```python
# python
# LC 367 - Valid Perfect Square
# IDEA: first r in [1, num] with r * r >= num; a perfect square iff r * r == num
# time = O(log num), space = O(1)
def isPerfectSquare(num):
    left, right = 1, num
    while left < right:
        mid = left + (right - left) // 2
        if mid * mid >= num:
            right = mid                      # mid is big enough: the root is mid or smaller
        else:
            left = mid + 1
    return left * left == num
```

```java
// java
// LC 367 - Valid Perfect Square
// IDEA: first r with r * r >= num, in long to avoid overflow
// time = O(log num), space = O(1)
public boolean isPerfectSquare(int num) {
    long left = 1, right = num;
    while (left < right) {
        long mid = left + (right - left) / 2;
        if (mid * mid >= num) right = mid;
        else left = mid + 1;
    }
    return left * left == num;
}
```

**Traps**
- `int mid * mid` overflows in Java once `mid > 46340`: `num = 2147483647` probes `mid ≈ 10⁹`. Use `long`.
- `sqrt` from the library is banned by the statement; Newton's iteration is the other accepted
  answer ([math.md § Integer Square Root](./math.md#1-1-10-integer-square-root--binary-search--newton-)).

**Similar** — LC 69 *(the floor root: the last `r` with `r * r <= x`)* · LC 633 *(two squares summing to c)*

### Missing Element in Sorted Array — LC 1060

> **Key point** — `missing(i) = nums[i] − nums[0] − i` counts the numbers absent before index `i`,
> and it never decreases. Find the last index with fewer than `k` missing; the answer is that many
> steps past `nums[i]`.

**Reduces to**: [binary_search_on_answer § Template 1](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) — the first index with `missing(i) >= k`, over indices; the same derived-count trick as LC 1539 on that sheet.

```python
# python
# LC 1060 - Missing Element in Sorted Array
# IDEA: missing(i) = nums[i] - nums[0] - i is monotone; find the first index with missing(i) >= k
# time = O(log n), space = O(1)
def missingElement(nums, k):
    def missing(i):
        return nums[i] - nums[0] - i         # numbers absent between nums[0] and nums[i]

    left, right = 0, len(nums)               # right = n: the answer may lie past the last element
    while left < right:
        mid = left + (right - left) // 2
        if missing(mid) >= k:
            right = mid
        else:
            left = mid + 1
    # left = first index with >= k missing, so index left - 1 has < k; the answer is past nums[left - 1]
    return nums[left - 1] + k - missing(left - 1)
```

```text
nums = [4,7,9,10], k = 3
missing:  [0, 2, 3, 3]
first index with missing >= 3 -> 2
answer = nums[1] + 3 - missing(1) = 7 + 3 - 2 = 8      (missing numbers: 5, 6, 8)
```

**Traps**
- The k-th missing number can be past the array: `[4,7,9,10]`, `k = 5` → 12. `right = len(nums)`
  covers it; `right = len(nums) − 1` does not.
- `left` is never 0, because `missing(0) = 0 < k` for every `k >= 1`, so `left − 1` is safe.

**Similar** — LC 1539 *(missing positives counted from 1)* · LC 275 *(H-Index II: a derived count against the index)*

### Arranging Coins — LC 441

> **Key point** — `k` full rows need `k (k + 1) / 2` coins, which grows with `k`. The answer is the
> **largest** `k` that still fits, the last `True` of `True … True False … False`.

**Reduces to**: [binary_search_on_answer § Template 2](./binary_search_on_answer.md#template-2-largest-feasible-value--lc-1552-) — largest feasible value with `feasible(k) = k (k + 1) / 2 <= n`, upper mid.

```python
# python
# LC 441 - Arranging Coins
# IDEA: last k in [0, n] with k * (k + 1) // 2 <= n; upper mid so left = mid always moves
# time = O(log n), space = O(1)
def arrangeCoins(n):
    left, right = 0, n
    while left < right:
        mid = left + (right - left + 1) // 2  # upper mid
        if mid * (mid + 1) // 2 <= n:
            left = mid                        # mid rows fit: the answer is mid or more
        else:
            right = mid - 1
    return left
```

```java
// java
// LC 441 - Arranging Coins
// IDEA: last k with k * (k + 1) / 2 <= n, in long; upper mid
// time = O(log n), space = O(1)
public int arrangeCoins(int n) {
    long left = 0, right = n;
    while (left < right) {
        long mid = left + (right - left + 1) / 2;
        if (mid * (mid + 1) / 2 <= n) left = mid;
        else right = mid - 1;
    }
    return (int) left;
}
```

**Traps**
- The lower mid with `left = mid` loops forever once `right = left + 1`: `n = 1` sticks at `left = 0, right = 1`.
- `mid * (mid + 1)` overflows `int` for `n` near 2³¹: use `long`.

**Similar** — LC 69 *(the same "largest k whose cost fits")* · LC 2226 *(largest feasible pile size)*
