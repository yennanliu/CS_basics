# Binary Search — Worked Examples

> **Scope** — The worked-problem archive for [binary_search.md](./binary_search.md) — one canonical solution per problem for the index-space templates, with the traces and pitfalls that do not fit on the main sheet.
> **See also** — *parent sheet*: [binary_search.md](./binary_search.md) — the loop invariants, the boundary templates and the "which template" decision table; [binary_search_on_answer.md](./binary_search_on_answer.md) — every problem that binary searches the *answer* rather than an index.
> *Neighbouring sheets*: [2_pointers.md](./2_pointers.md) — the converging-pointer alternative for LC 167 / LC 658; [dp.md](./dp.md) — the `O(n²)` DP that LC 300 replaces; [matrix.md](./matrix.md) — the 2D-grid family.

## LeetCode Problem Lists

- [Binary Search](https://leetcode.com/problem-list/binary-search/)

## Overview

Every problem below is solved **once**, in the language that teaches it best (both, where
both are instructive). The template each one uses lives in
[binary_search.md](./binary_search.md) — this file is the practice set, not a second
template catalogue. Problems that binary search the *answer space* are in
[binary_search_on_answer.md](./binary_search_on_answer.md) instead.

## LC Examples

### 1) Two Sum II — Input Array Is Sorted (LC 167)
**Approach**: Binary search for each element's complement
```python
# 167 Two Sum II - Input array is sorted
class Solution(object):
    def twoSum(self, numbers, target):
        for i in range(len(numbers)):
            l, r = i+1, len(numbers)-1
            tmp = target - numbers[i]
            while l <= r:
                mid = l + (r-l)//2
                if numbers[mid] == tmp:
                    return [i+1, mid+1]
                elif numbers[mid] < tmp:
                    l = mid+1
                else:
                    r = mid-1
```

### 2) Find Peak Element (LC 162, LC 852)

#### Core Idea: Hill Climbing (Guaranteed Peak Exists)

**Key Rule:**
```text
If nums[mid] < nums[mid + 1]  →  peak is on the RIGHT  (move l = mid + 1)
If nums[mid] > nums[mid + 1]  →  peak is on the LEFT   (move r = mid)
```

**Why it's guaranteed to work (the "-∞ boundary" trick):**

The problem states `nums[-1] = nums[n] = -∞`. This means the array is
always "sandwiched" between two bottomless pits on each end.

```text
         peak
        /    \
       /      \
-∞ ___/        \___ -∞
```

No matter where you are in the array, if you walk toward the *higher*
neighbor, you are guaranteed to hit a peak — either the ground rises
and then falls (a peak in the middle), or it keeps rising all the way
to the end (the last element is a peak because -∞ is on its right).

---

#### Case 1: `nums[mid] < nums[mid+1]` → Going UPHILL → move RIGHT

```text
         ?
        /
       /
 .... mid  mid+1 ....
      low  HIGH

You are on an upward slope. Two sub-cases:
  a) The slope eventually drops → peak is somewhere to the right
  b) The slope never drops → last element is a peak (because -∞ is on its right)

Either way, a peak MUST exist to the right → l = mid + 1
```

```text
Example: nums = [1, 2, 3, 1]
                    ^mid ^mid+1
nums[mid]=2 < nums[mid+1]=3  → uphill → move RIGHT
                        ^--- peak is here (index 2, value 3)
```

---

#### Case 2: `nums[mid] > nums[mid+1]` → Going DOWNHILL → stay LEFT (include mid)

```text
 ....  mid  mid+1 ....
       HIGH  low
          \
           \
            ?

You are on a downward slope. Two sub-cases:
  a) nums[mid] > nums[mid-1]: mid itself IS a peak
  b) nums[mid] < nums[mid-1]: the slope was already rising from the left,
     so a peak exists somewhere to the left of mid

Either way, the peak is at mid or to the left → r = mid
```

```text
Example: nums = [1, 2, 1, 3, 5, 6, 4]
                               ^mid ^mid+1
nums[mid]=5 > nums[mid+1]=6?  No — pick a better example:
                                  ^mid  ^mid+1
nums[mid]=6 > nums[mid+1]=4  → downhill → move LEFT (r = mid, keep mid)
              ^--- peak is here (index 5, value 6)
```

---

#### Visual: Search Space Convergence

```text
nums = [1, 2, 3, 1]
        0  1  2  3

l=0, r=3:  mid=1, nums[1]=2 < nums[2]=3  → uphill → l=2
           [_, _, 2, 3]
                  l  r

l=2, r=3:  mid=2, nums[2]=3 > nums[3]=1  → downhill → r=2
           [_, _, 3, _]
                  l
                  r

l==r → return 2  ✓  (nums[2]=3 is the peak)
```

```text
nums = [1, 2, 1, 3, 5, 6, 4]
        0  1  2  3  4  5  6

l=0, r=6:  mid=3, nums[3]=3 < nums[4]=5  → uphill → l=4
l=4, r=6:  mid=5, nums[5]=6 > nums[6]=4  → downhill → r=5
l=4, r=5:  mid=4, nums[4]=5 < nums[5]=6  → uphill → l=5
l=5, r=5:  l==r → return 5  ✓  (nums[5]=6 is the peak)
```

---

#### Why `while (l < r)` and NOT `while (l <= r)`?

With `r = mid` (not `r = mid - 1`), when `l == r` the loop must stop —
otherwise `mid == l == r` would cause infinite loop (`r = mid` never shrinks).

```java
// ✅ Correct: while (l < r)
while (l < r) {
    int mid = (l + r) / 2;
    if (nums[mid] > nums[mid + 1])
        r = mid;       // Keep mid, since it may be the peak
    else
        l = mid + 1;   // mid is not the peak, skip it
}
// When l == r, that IS the peak index
return l;
```

---

**Approach**: Compare mid with adjacent elements to determine search direction
```python
# LC 162 Find Peak Element, LC 852 Peak Index in a Mountain Array
# V0'
# IDEA : RECURSIVE BINARY SEARCH
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid) # r = mid
            return help(nums, mid+1, r) # l = mid + 1
            
        return help(nums, 0, len(nums)-1)
```

```java
// java
// LC 162
// V2
// IDEA: RECURSIVE BINARY SEARCH
// https://leetcode.com/problems/find-peak-element/editorial/
    // NOTE : ONLY have to compare index i with index i + 1 (its right element)
    //        ; otherwise, i-1 already returned as answer
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }
    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }
```

### 3) Valid Perfect Square (LC 367)
**Approach**: Binary search on the range [1, num] to find square root
```python
# 367 Valid Perfect Square, LC 69 Sqrt(x)
# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def isPerfectSquare(self, num):
        left, right = 0, num
        while left <= right:
            ### NOTE : there is NO mid * mid == num condition
            mid = (left + right) / 2
            if mid * mid >= num:
                right = mid - 1
            else:
                left = mid + 1
        ### NOTE this
        return left * left == num
```

```java
// java
// LC 367
public boolean isPerfectSquare(int num) {

    if (num < 2) {
        return true;
    }

    long left = 2;
    long right = num / 2; // NOTE !!!, "long right = num;" is OK as well
    long x;
    long guessSquared;

    while (left <= right) {
        x = (left + right) / 2;
        guessSquared = x * x;
        if (guessSquared == num) {
            return true;
        }
        if (guessSquared > num) {
            right = x - 1;
        } else {
            left = x + 1;
        }
    }
    return false;
}
```

### 4) Sqrt(x) (LC 69)
**Approach**: Binary search with careful boundary handling
```python
# LC 069 Sqrt(x)
# V0
# IDEA : binary search
class Solution(object):
    def mySqrt(self, num):
        if num <= 1:
            return num
        l = 0
        r = num - 1
        while r >= l:
            mid = l + (r - l) // 2
            if mid * mid == num:
                return mid
            elif mid * mid > num:
                r = mid - 1
            else:
                l = mid + 1
        return l if l * l < num else l - 1
```

### 5) Minimum Size Subarray Sum (LC 209)
**Approach**: Binary search on possible subarray lengths + sliding window validation
```python
# LC 209 Minimum Size Subarray Sum
### NOTE : there is also sliding window approach
# V1' 
# http://bookshadow.com/weblog/2015/05/12/leetcode-minimum-size-subarray-sum/
# IDEA : BINARY SEARCH 
class Solution:
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        left, right = 0, size
        bestAns = 0
        while left <= right:
            mid = (left + right) / 2
            if self.solve(mid, s, nums):
                bestAns = mid
                right = mid - 1
            else:
                left = mid + 1
        return bestAns

    def solve(self, l, s, nums):
        sums = 0
        for x in range(len(nums)):
            sums += nums[x]
            if x >= l:
                sums -= nums[x - l]
            if sums >= s:
                return True
        return False
```

### 6) First Bad Version (LC 278)
> Find the leftmost bad version without calling the API more than necessary.

```java
// LC 278 - First Bad Version
// IDEA: Binary search for left boundary — first bad version
// time = O(log N), space = O(1)
public int firstBadVersion(int n) {
    int l = 1, r = n;
    while (l < r) {
        int mid = l + (r - l) / 2;
        if (isBadVersion(mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
```

### 7) Find K Closest Elements (LC 658)
**Approach**: Two pointers approach to shrink array to k elements
```python
# LC 658. Find K Closest Elements
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : TWO POINTERS 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        # since the array already sorted, arr[-1] must be the biggest one,
        # while arr[0] is the smallest one
        # so if the distance within arr[-1],  x >  arr[0],  x
        # then remove the arr[-1] since we want to keep k elements with smaller distance,
        # and vice versa (remove arr[0]) 
        while len(arr) > k:
            if x - arr[0] <= arr[-1] - x:
                arr.pop()
            else:
                arr.pop(0)
        return arr
```

### 8) Find Smallest Letter Greater Than Target (LC 744)
**Pattern**: `while (l < r)` - Finding insertion position
```python
# LC 744 Find Smallest Letter Greater Than Target
class Solution(object):
    def nextGreatestLetter(self, letters, target):
        l, r = 0, len(letters)
        
        # Use half-open boundary [l, r)
        while l < r:
            mid = l + (r - l) // 2
            if letters[mid] <= target:  # Need strictly greater
                l = mid + 1
            else:
                r = mid
        
        # Handle circular array - if no letter greater than target, return first
        return letters[l % len(letters)]
```

### 9) Arranging Coins (LC 441)
**Pattern**: `while (l <= r)` - Finding exact value with mathematical property
```java
// LC 441 Arranging Coins
public int arrangeCoins(int n) {
    long l = 0, r = n;
    
    while (l <= r) {
        long mid = l + (r - l) / 2;
        long coins = mid * (mid + 1) / 2;  // Sum of 1+2+...+mid
        
        if (coins == n) {
            return (int) mid;
        } else if (coins < n) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    
    return (int) r;  // Return the complete rows we can form
}
```

### 10) Find Minimum in Rotated Sorted Array II (LC 154)
**Pattern**: `while (l < r)` - Handling duplicates in rotated array
```java
// LC 154 Find Minimum in Rotated Sorted Array II (with duplicates)
public int findMin(int[] nums) {
    int l = 0, r = nums.length - 1;
    
    while (l < r) {
        int mid = l + (r - l) / 2;
        
        if (nums[mid] < nums[r]) {
            // Right half is sorted, minimum is in left half (including mid)
            r = mid;
        } else if (nums[mid] > nums[r]) {
            // Left half is sorted, minimum is in right half
            l = mid + 1;
        } else {
            // nums[mid] == nums[r], can't determine which half to search
            // Reduce search space by 1
            r--;
        }
    }
    
    return nums[l];
}
```

### 11) Missing Element in Sorted Array (LC 1060)
**Pattern**: `while (l < r - 1)` - Finding missing elements with gap calculation
```python
# LC 1060 Missing Element in Sorted Array
class Solution(object):
    def missingElement(self, nums, k):
        def missing_count(idx):
            # How many numbers are missing up to nums[idx]
            return nums[idx] - nums[0] - idx
        
        n = len(nums)
        
        # If k-th missing number is beyond the array
        if k > missing_count(n - 1):
            return nums[-1] + k - missing_count(n - 1)
        
        l, r = 0, n - 1
        
        # Find the largest index where missing_count < k
        while l < r - 1:
            mid = l + (r - l) // 2
            if missing_count(mid) < k:
                l = mid
            else:
                r = mid
        
        # The k-th missing number is between nums[l] and nums[r]
        return nums[l] + k - missing_count(l)
```

### 12) Median of Two Sorted Arrays (LC 4)
> Binary search on partition of smaller array to find median in O(log(min(M,N))).

```java
// LC 4 - Median of Two Sorted Arrays
// IDEA: Binary search partition on smaller array
// time = O(log(min(M,N))), space = O(1)
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
    int m = nums1.length, n = nums2.length;
    int l = 0, r = m;
    while (l <= r) {
        int partX = (l + r) / 2;
        int partY = (m + n + 1) / 2 - partX;
        int maxLeftX  = partX == 0 ? Integer.MIN_VALUE : nums1[partX-1];
        int minRightX = partX == m ? Integer.MAX_VALUE : nums1[partX];
        int maxLeftY  = partY == 0 ? Integer.MIN_VALUE : nums2[partY-1];
        int minRightY = partY == n ? Integer.MAX_VALUE : nums2[partY];
        if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
            if ((m + n) % 2 == 0)
                return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
            else
                return Math.max(maxLeftX, maxLeftY);
        } else if (maxLeftX > minRightY) r = partX - 1;
        else l = partX + 1;
    }
    return 0;
}
```

### 13) Time Based Key-Value Store (LC 981)
> For each key, binary search on sorted timestamps to find the largest <= given time.

```java
// LC 981 - Time Based Key-Value Store
// IDEA: HashMap of key -> sorted list of (timestamp, value); binary search on query
// time = O(log N) per get, O(1) per set, space = O(N)
class TimeMap {
    Map<String, List<int[]>> map = new HashMap<>(); // val stored as [timestamp, valueIndex]
    Map<String, List<String>> vals = new HashMap<>();
    public void set(String key, String value, int timestamp) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(new int[]{timestamp});
        vals.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
    public String get(String key, int timestamp) {
        if (!map.containsKey(key)) return "";
        List<int[]> times = map.get(key);
        int l = 0, r = times.size() - 1, idx = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (times.get(mid)[0] <= timestamp) { idx = mid; l = mid + 1; }
            else r = mid - 1;
        }
        return idx == -1 ? "" : vals.get(key).get(idx);
    }
}
```

### 14) Single Element in a Sorted Array (LC 540)
> Pair pattern breaks after the single element; binary search on even indices.

```java
// LC 540 - Single Element in a Sorted Array
// IDEA: Binary search — check if pair pattern holds at mid (even index)
// time = O(log N), space = O(1)
public int singleNonDuplicate(int[] nums) {
    int l = 0, r = nums.length - 1;
    while (l < r) {
        int mid = (l + r) / 2;
        if (mid % 2 == 1) mid--; // ensure mid is even
        if (nums[mid] == nums[mid + 1]) l = mid + 2; // pair intact, single is to the right
        else r = mid;                                  // pair broken, single is here or left
    }
    return nums[l];
}
```

### 15) Check If a Number Is Majority Element in a Sorted Array (LC 1150)

#### Core Idea

Given a sorted array, a **majority element** appears more than `N/2` times.

**Key Insight**: In a sorted array, if target appears more than `N/2` times, then the element at index `firstIndex + N/2` must also equal target.

**Why it works:**
- Find the first occurrence of target at index `firstIndex`
- If target appears `> N/2` times, it must occupy at least `N/2 + 1` consecutive positions
- So `nums[firstIndex + N/2]` **must** still be target

This avoids counting all occurrences — O(log N) instead of O(N).

---

#### Pattern: Find First Index via Binary Search

```java
// Find first occurrence of target in sorted array
private int findFirstIndex(int[] nums, int target) {
    int low = 0, high = nums.length - 1;
    int firstIdx = -1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            firstIdx = mid;       // record potential answer
            high = mid - 1;       // keep searching LEFT for earlier occurrence
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return firstIdx;
}
```

**Template rules:**
- When `nums[mid] == target`: save `mid` as candidate, then **shrink right** (`high = mid - 1`) to keep searching left
- When loop ends, `firstIdx` holds the leftmost index of target (or -1 if not found)

---

#### Solution

```java
// LC 1150 - Check If a Number Is Majority Element in a Sorted Array
// time: O(log N), space: O(1)
public boolean isMajorityElement(int[] nums, int target) {
    int n = nums.length;

    // Step 1: Find first occurrence of target
    int firstIndex = findFirstIndex(nums, target);

    // Step 2: If not found, can't be majority
    if (firstIndex == -1) return false;

    // Step 3: Check if element at (firstIndex + n/2) is still target
    // If yes → target appears at least (n/2 + 1) times → majority
    int majorityIndex = firstIndex + n / 2;
    return majorityIndex < n && nums[majorityIndex] == target;
}

private int findFirstIndex(int[] nums, int target) {
    int low = 0, high = nums.length - 1, firstIdx = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            firstIdx = mid;
            high = mid - 1;    // search left for earlier occurrence
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return firstIdx;
}
```

**Alternative using lower_bound style (V1):**
```java
// Uses two binary searches: first index of target, first index of (target+1)
public boolean isMajorityElement_v1(int[] nums, int target) {
    int left  = lowerBound(nums, target);      // first index >= target
    int right = lowerBound(nums, target + 1);  // first index >= target+1
    return right - left > nums.length / 2;
}

private int lowerBound(int[] nums, int x) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = (left + right) >>> 1;
        if (nums[mid] >= x) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

---

#### Visual Example

```text
nums = [2,4,5,5,5,5,5,6,6], target = 5, N = 9

findFirstIndex(5) → index 2

majorityIndex = 2 + 9/2 = 2 + 4 = 6
nums[6] = 5 == target ✓  → return true

Intuition:
index:  0  1  2  3  4  5  6  7  8
value:  2  4 [5] 5  5  5 [5] 6  6
           first↑           ↑ must still be 5 if majority
```

---

#### Find First Index — Comparison with Similar Patterns

| Pattern | When `nums[mid] == target` | After Loop | Returns |
|---------|---------------------------|-----------|---------|
| **Standard BS** | `return mid` | N/A | exact index or -1 |
| **Find First (Left Boundary)** | `firstIdx = mid; high = mid-1` | `firstIdx` | leftmost index or -1 |
| **Find Last (Right Boundary)** | `lastIdx = mid; low = mid+1` | `lastIdx` | rightmost index or -1 |
| **Lower Bound** | `right = mid` (half-open) | `left` | first index >= target |

---

#### Similar LC Problems

| Problem | Core Idea | Difficulty |
|---------|-----------|------------|
| **LC 1150** | Find first index + jump by N/2 to verify majority | Easy (Prime) |
| LC 34 | Find first AND last occurrence via two boundary searches | Medium |
| LC 35 | Find insertion position (return `left` after loop) | Easy |
| LC 278 | First Bad Version — find first index where condition is true | Easy |
| LC 153 | Find minimum in rotated sorted array | Medium |
| LC 374 | Guess Number Higher or Lower — classic find-first pattern | Easy |
| LC 540 | Single Element in a Sorted Array — parity-based boundary search | Medium |
| LC 852 | Peak Index in a Mountain Array — find first descending point | Medium |

---

#### Interview Tips for the Find-First Pattern

1. **Recognize sorted array + count query** → think "find first index + O(1) check"
2. **Key line**: `return majorityIndex < n && nums[majorityIndex] == target` — bounds check matters
3. **Why not count?** Counting is O(N); binary search is O(log N) — interviewer expects the optimal
4. **Edge cases**: target not in array, single element array, all elements equal target

---

### 16) Find Right Interval (LC 436) ⭐⭐⭐⭐

**Approach**: Sort starts + **lower-bound** binary search, mapping sorted position back to the **original index**.

#### 1) Core Idea

> For each interval `[start_i, end_i]`, find the interval `j` whose `start_j` is the
> **smallest start that is `>= end_i`** — this is a **lower-bound** (first `>=` target) search.

The catch: the answer must be the **original index** of that interval, but binary
search needs the starts **sorted**. So we sort `(start, original_index)` pairs together —
sorting keeps each start glued to its original index, so after we locate the position in
the sorted array we can read the original index straight off the pair.

```text
intervals = [[3,4],[2,3],[1,2]]   (original indices 0,1,2)

sort starts with their index →  starts = [(1,2), (2,1), (3,0)]
                                            ^val,idx

For interval [2,3]:  end = 3  → first start >= 3  → (3,0) → original index 0  ✅
For interval [1,2]:  end = 2  → first start >= 2  → (2,1) → original index 1  ✅
For interval [3,4]:  end = 4  → no start >= 4                → -1
```

**Why binary search?** The starts are unique and, once sorted, **monotonic** — exactly the
lower-bound predicate `start >= end_i` (False…False, True…True). Total: `O(n log n)`.

#### 2) Pattern — Sort-with-index + Lower Bound

```python
# LC 436 Find Right Interval
# V1: manual lower-bound binary search
# time = O(n log n), space = O(n)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)

        # NOTE !!! collect BOTH `start val` AND `original idx`, then sort
        # -> sorting keeps start glued to its original index
        starts = [(intervals[i][0], i) for i in range(n)]
        starts.sort()                          # sort by start (unique)

        res = [-1] * n
        for i in range(n):
            target = intervals[i][1]           # we need first start >= end_i

            # ---- lower bound: first start >= target ----
            left, right, ans = 0, n - 1, -1
            while left <= right:
                mid = (left + right) // 2
                if starts[mid][0] >= target:
                    ans = starts[mid][1]       # record original index (candidate)
                    right = mid - 1            # keep searching LEFT for a smaller start
                else:
                    left = mid + 1
            res[i] = ans
        return res
```

**Cleaner via `bisect`** (extract just the sorted starts, `bisect_left` = first `>=`):

```python
import bisect
# time = O(n log n), space = O(n)
class Solution(object):
    def findRightInterval(self, intervals):
        n = len(intervals)
        # (start, original_idx) sorted by start
        starts = sorted([[iv[0], i] for i, iv in enumerate(intervals)])
        just_starts = [s[0] for s in starts]   # bisect needs a plain sorted list

        res = [-1] * n
        for i, iv in enumerate(intervals):
            idx = bisect.bisect_left(just_starts, iv[1])   # first start >= end_i
            if idx < n:
                res[i] = starts[idx][1]        # map sorted pos -> original index
        return res
```

> **Common pitfall (TLE)**: the brute-force "sort then double loop to rebuild the
> index map" is `O(n^2)`. The whole point is to replace that inner scan with a
> `O(log n)` lower-bound search — recognizing the **first `>=` target** shape is the key.

**Sort-with-index recipe (reusable):** when a problem sorts data but the answer must be
the *original* position, pair each value with its index **before** sorting
(`(val, idx)`), sort the pairs, run binary search on the values, then read `pair[1]` for
the original index. Same trick appears in LC 315 / LC 493.

#### 3) Similar LC Problems

| Problem | LC# | Binary-search role | Twist |
|---------|-----|--------------------|-------|
| **Find Right Interval** | **436** | lower bound: first `start >= end_i` | map sorted pos → original index |
| Search Insert Position | 35 | lower bound: first `>= target` | return the insertion index itself |
| Time Based Key-Value Store | 981 | **upper bound − 1**: floor on timestamp | per-key sorted timestamp list |
| Two Sum II (sorted) | 167 | search complement in sorted half | two-pointer alt |
| Find First and Last Position | 34 | left + right boundary search | two lower/upper-bound calls |
| Count of Smaller After Self | 315 | `SortedList` + `bisect` scanning right→left | index-preserving count |
| My Calendar I | 729 | floor/ceiling via `SortedDict` | overlap check on ordered map |
| Data Stream as Disjoint Intervals | 352 | floor/ceiling to merge intervals | ordered interval map |

> **Recognition signal**: "for each element, find the *smallest value `>=` X* (or *largest
> `<=` X*)" over a set that can be **sorted once** → sort + lower/upper-bound binary search.
> If the set **mutates over time**, reach for `SortedList`/`SortedDict` instead (see
> [python_trick.md §1-27-3](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md)).

---

### 17) Find in Mountain Array (LC 1095) ⭐⭐⭐⭐⭐

> A **mountain array** strictly increases to a peak, then strictly decreases. Find the **smallest index** whose value equals `target` (`-1` if absent), using only `arr.get(i)` / `arr.length()` — and as few `get()` calls as possible.

#### Core Idea — Three Binary Searches, Not One

The array is not sorted, but it is the **concatenation of two sorted runs**. So:

```text
1) find the PEAK             -> hill-climbing binary search (same as LC 162 / LC 852, §2 above)
2) binary search [0, peak]   -> ASCENDING  order
3) binary search [peak+1, n) -> DESCENDING order   <-- the twist most people miss
```

Left half is searched **first** because the problem wants the smallest index.

The **descending-order flip** that makes step 3 work is stated once in
[binary_search.md](./binary_search.md) §2.4.
```java
// java
// LC 1095 - Find in Mountain Array
// IDEA: 1) hill-climb binary search for the peak, 2) ascending BS on left half,
//       3) descending BS on right half (flip the comparison)
// time = O(log n)  (~3 * log n get() calls), space = O(1)
class Solution {
    public int findInMountainArray(int target, MountainArray arr) {
        int n = arr.length();
        int l = 0, r = n - 1;
        while (l < r) {                                     // 1) peak = first i with a[i] > a[i+1]
            int mid = l + (r - l) / 2;
            if (arr.get(mid) < arr.get(mid + 1)) l = mid + 1;   // uphill  -> peak on the right
            else r = mid;                                       // downhill-> peak at mid or left
        }
        int peak = l;

        int idx = bs(arr, target, 0, peak, true);           // 2) ascending half (smallest index first)
        if (idx != -1) return idx;
        return bs(arr, target, peak + 1, n - 1, false);     // 3) descending half
    }

    // one binary search that handles BOTH orders
    private int bs(MountainArray arr, int target, int l, int r, boolean asc) {
        while (l <= r) {
            int mid = l + (r - l) / 2;
            int val = arr.get(mid);
            if (val == target) return mid;
            if ((val < target) == asc) l = mid + 1;   // asc: too small -> right; desc: too small -> left
            else r = mid - 1;
        }
        return -1;
    }
}
```

```python
# python
# LC 1095 - Find in Mountain Array
# IDEA: peak via hill-climbing BS, then ascending BS on the left half and descending BS on the right
# time = O(log n), space = O(1)
class Solution:
    def findInMountainArray(self, target, mountain_arr):
        n = mountain_arr.length()

        # 1) find peak
        l, r = 0, n - 1
        while l < r:
            mid = (l + r) // 2
            if mountain_arr.get(mid) < mountain_arr.get(mid + 1):
                l = mid + 1          # uphill  -> peak on the right
            else:
                r = mid              # downhill-> peak at mid or to the left
        peak = l

        # 2) ascending half first (we want the SMALLEST index)
        idx = self.bs(mountain_arr, target, 0, peak, True)
        if idx != -1:
            return idx
        # 3) descending half
        return self.bs(mountain_arr, target, peak + 1, n - 1, False)

    def bs(self, arr, target, l, r, asc):
        while l <= r:
            mid = (l + r) // 2
            val = arr.get(mid)
            if val == target:
                return mid
            if (val < target) == asc:   # asc -> move right; desc -> move left
                l = mid + 1
            else:
                r = mid - 1
        return -1
```

**Interview notes**
- `arr.get()` is a **rate-limited API** (LC 1095 caps it at 100 calls) — cache nothing, just keep the call count at `3 log n`. Never scan linearly to find the peak.
- The peak search uses `while (l < r)` with `r = mid` — see §2 (Find Peak Element) for why `l <= r` would overflow past `mid + 1`.
- Peak search compares `a[mid]` with `a[mid+1]`, so `mid + 1` must be in range — guaranteed because `r = n - 1` and `mid < r`.

**Similar problems**

| LC | Problem | Relation |
|----|---------|----------|
| **1095** | Find in Mountain Array | This problem — peak + 2 ordered searches |
| 852 | Peak Index in a Mountain Array | Only step 1 (the peak search) |
| 162 | Find Peak Element | Peak search when no mountain shape is guaranteed |
| 33 / 153 | Search in Rotated Sorted Array | Same "two sorted runs" idea, different split rule ([binary_search.md](./binary_search.md) §1.2) |

---

### 18) Longest Increasing Subsequence — the `tails` Array (LC 300) ⭐⭐⭐⭐⭐

> Longest Increasing Subsequence in `O(n log n)`. The `O(n²)` DP is the expected first answer; the binary-search version is the follow-up FAANG interviewers ask for.

#### Core Idea

Keep `tails[k]` = **the smallest possible tail value** of an increasing subsequence of length `k + 1`.

- `tails` is **always sorted ascending** → it can be binary searched.
- For each `x`, find `lower_bound(tails, x)` (first tail `>= x`):
  - index `== len(tails)` → `x` extends the longest run → **append**
  - otherwise → **overwrite** that tail with the smaller `x` (keeps future options open)
- Answer = `len(tails)`.

> `tails` is **not** an actual subsequence — only its **length** is meaningful.

```text
nums = [10, 9, 2, 5, 3, 7, 101, 18]
10  -> [10]
 9  -> [9]              (replace: length-1 run can end smaller)
 2  -> [2]
 5  -> [2,5]
 3  -> [2,3]            (replace 5)
 7  -> [2,3,7]
101 -> [2,3,7,101]
 18 -> [2,3,7,18]       -> answer = 4
```

```java
// java
// LC 300 - Longest Increasing Subsequence
// IDEA: patience sorting - tails[k] = smallest tail of an increasing run of length k+1,
//       binary search (lower_bound) for the slot to extend or overwrite
// time = O(n log n), space = O(n)
public int lengthOfLIS(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;
    for (int x : nums) {
        int l = 0, r = size;
        while (l < r) {                 // lower_bound: first tails[i] >= x
            int mid = l + (r - l) / 2;
            if (tails[mid] < x) l = mid + 1;
            else r = mid;
        }
        tails[l] = x;                   // overwrite ...
        if (l == size) size++;          // ... or append (l == size)
    }
    return size;
}
```

```python
# python
# LC 300 - Longest Increasing Subsequence
# IDEA: keep sorted `tails` array, bisect_left = lower_bound -> replace, else append
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def lengthOfLIS(self, nums):
        tails = []
        for x in nums:
            i = bisect.bisect_left(tails, x)   # first tail >= x
            if i == len(tails):
                tails.append(x)                # x extends the longest run
            else:
                tails[i] = x                   # smaller tail for the same length
        return len(tails)
```

**Variation — strictly increasing vs non-decreasing** (the classic off-by-one twist):

```text
strictly increasing (LC 300)      -> bisect_left  / lower_bound  (first tail >= x)
non-decreasing (duplicates OK)    -> bisect_right / upper_bound  (first tail >  x)
```

**Variation — LC 354 Russian Doll Envelopes** = LIS in 2-D. Sort widths **ascending** and, on ties, heights **descending** (so two envelopes with the same width can never both be picked), then run the exact same LIS on heights:

```python
# python
# LC 354 - Russian Doll Envelopes
# IDEA: sort by (w asc, h desc) -> reduces to LIS on heights
# time = O(n log n), space = O(n)
import bisect

def maxEnvelopes(envelopes):
    envelopes.sort(key=lambda e: (e[0], -e[1]))   # ties: h DESC blocks same-width chains
    tails = []
    for _, h in envelopes:
        i = bisect.bisect_left(tails, h)
        if i == len(tails):
            tails.append(h)
        else:
            tails[i] = h
    return len(tails)
```

```java
// java
// LC 354 - Russian Doll Envelopes
// IDEA: sort (w asc, h desc), then LIS on the heights array (reuse lengthOfLIS above)
// time = O(n log n), space = O(n)
public int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
    int[] heights = new int[envelopes.length];
    for (int i = 0; i < envelopes.length; i++) heights[i] = envelopes[i][1];
    return lengthOfLIS(heights);
}
```

**Related — binary search as the DP transition lookup**

| LC | Problem | How binary search is used |
|----|---------|---------------------------|
| **300** | Longest Increasing Subsequence | `tails` + lower bound |
| **354** | Russian Doll Envelopes | sort by (w asc, h desc) → LIS on heights |
| 1235 | Maximum Profit in Job Scheduling | sort jobs by end time; binary search the **last job ending `<= start_i`**, then `dp[i] = max(dp[i-1], profit + dp[j])` |
| 1751 | Maximum Number of Events That Can Be Attended II | same "sort by end + binary search previous compatible" DP, with a `k`-events dimension |
| 1027 | Longest Arithmetic Subsequence | DP over `(index, diff)` — hash map, **not** binary search (know the difference) |

---

### 19) Random Pick with Weight (LC 528) ⭐⭐⭐⭐

> `pickIndex()` must return index `i` with probability `w[i] / sum(w)`.

#### Core Idea — turn weights into contiguous ranges, then binary search

Build the prefix sums, draw a uniform integer `target` in `[1, total]`, and return the **first prefix `>= target`** (a `lower_bound`). Each index `i` owns exactly `w[i]` of the `total` slots → probability is exactly `w[i] / total`.

```text
w      = [1,  3,  2]
prefix = [1,  4,  6]
target:   1 | 2 3 4 | 5 6
index :   0 |   1   |  2
```

```java
// java
// LC 528 - Random Pick with Weight
// IDEA: prefix sums split [1, total] into per-index ranges; lower_bound maps a uniform draw to an index
// time = ctor O(n), pickIndex O(log n); space = O(n)
class Solution {
    private int[] prefix;
    private Random rand = new Random();

    public Solution(int[] w) {
        prefix = new int[w.length];
        int s = 0;
        for (int i = 0; i < w.length; i++) { s += w[i]; prefix[i] = s; }
    }

    public int pickIndex() {
        int target = rand.nextInt(prefix[prefix.length - 1]) + 1;   // uniform in [1, total]
        int l = 0, r = prefix.length - 1;
        while (l < r) {                       // lower_bound: first prefix >= target
            int mid = l + (r - l) / 2;
            if (prefix[mid] < target) l = mid + 1;
            else r = mid;
        }
        return l;
    }
}
```

```python
# python
# LC 528 - Random Pick with Weight
# IDEA: prefix sums + bisect_left (lower bound) on a uniform draw in [1, total]
# time = __init__ O(n), pickIndex O(log n); space = O(n)
import bisect, random

class Solution:
    def __init__(self, w):
        self.prefix = []
        s = 0
        for x in w:
            s += x
            self.prefix.append(s)
        self.total = s

    def pickIndex(self):
        target = random.randint(1, self.total)          # inclusive on both ends
        return bisect.bisect_left(self.prefix, target)  # first prefix >= target
```

**Off-by-one guard** — pick ONE convention and stay in it:
- draw in `[1, total]` → `bisect_left` (first prefix `>= target`) ✅ (used above)
- draw in `[0, total)` → `bisect_right` (first prefix `> target`)
- Mixing them silently gives index `0` probability `0` or an out-of-range index.

**Similar problems — "binary search a sorted history / cumulative array"**

| LC | Problem | What the sorted array holds | Query |
|----|---------|------------------------------|-------|
| **528** | Random Pick with Weight | prefix sums of weights | lower bound of a random draw |
| 497 | Random Point in Non-overlapping Rectangles | prefix counts of points per rectangle | same lower bound, then pick inside the rectangle |
| 911 | Online Election | times array + leader-at-time array | upper bound − 1 (floor on time) |
| 1146 | Snapshot Array | per-index list of `(snap_id, val)` | upper bound − 1 (latest value `<= snap_id`) |
| 1348 | Tweet Counts Per Frequency | sorted tweet times per name | two bounds → count in `[start, end]` |
| 981 | Time Based Key-Value Store | per-key sorted timestamps | upper bound − 1 (§13 above) |

> All six are the same template: **keep a sorted array, answer each query with `lower_bound` / `upper_bound − 1`.** Only the payload changes.

---

## Problems by Pattern

| Template (in [binary_search.md](./binary_search.md)) | Problems here |
|---|---|
| Standard exact search, `while l <= r` — §2.1 | LC 167, LC 367, LC 69, LC 441 |
| Lower bound / first `>=` — §1.3 | LC 278, LC 744, LC 436, LC 1150, LC 528, LC 300 |
| Upper bound − 1 / floor query — §1.3 | LC 981 |
| Half-open `while l < r`, `r = mid` — §2.0 | LC 162, LC 852, LC 540, LC 154 |
| Gap-based `while l < r - 1` — §2.0 | LC 1060 |
| Rotated / two sorted runs — §1.2, §2.4 | LC 154, LC 1095 |
| Binary search a partition, not an index — §2.0 | LC 4 |
| Binary search a length + window check — §1.4 | LC 209 |

## Summary

- **The archive is not a template catalogue.** If a problem here looks unfamiliar, read
  its template on the parent sheet first — every solution below is an instance of one.
- **`lower_bound` is the workhorse.** LC 278, 744, 436, 1150, 528 and 300 are all "first
  index where a monotone predicate turns true", written six different ways.
- **`while l < r` with `r = mid` is for convergence, not for matching** — LC 162, 540, 154
  and 1095's peak search never test equality; they squeeze `l` and `r` onto one index.
- **Watch the payload, not the search.** LC 436, 981 and 528 are all "keep a sorted array,
  answer each query with a bound" — only what the array stores changes.
