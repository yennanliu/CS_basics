# Monotonic Queue (Deque)

> **Scope** — Sliding-window max/min in O(1) amortised, using a deque that stays monotonic.
> **See also**: [queue.md](./queue.md) — plain FIFO; [monotonic_stack.md](./monotonic_stack.md) — the non-windowed counterpart; [sliding_window.md](./sliding_window.md) — the window mechanics themselves; [heap.md](./heap.md) — the O(log n) alternative.

## LeetCode Problem Lists

- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Queue](https://leetcode.com/problem-list/queue/)

## Overview
**Monotonic Queue** is a double-ended queue (deque) that maintains elements in monotonic order (increasing or decreasing). Unlike monotonic stack which only removes from one end, monotonic queue can remove from **both ends** — enabling efficient sliding window min/max queries.

### Key Properties
- **Time Complexity**: O(N) amortized for sliding window problems (each element added/removed at most once)
- **Space Complexity**: O(K) where K is the window size
- **Core Idea**: Maintain candidates in monotonic order; remove stale elements from front, dominated elements from back
- **When to Use**: Sliding window min/max, bounded subarray optimization, DP optimization

### Monotonic Queue vs Monotonic Stack

| Feature | Monotonic Stack | Monotonic Queue |
|---------|----------------|-----------------|
| Structure | Stack (one end) | Deque (both ends) |
| Remove from front? | No | Yes — expiry/window |
| Primary use | Next greater/smaller | Sliding window min/max |
| Window constraint? | No | Yes — bounded range |
| Classic problem | LC 84 (Histogram) | LC 239 (Sliding Window Max) |

## Problem Categories

### **Pattern 1: Sliding Window Maximum/Minimum** — LC 239
- **Description**: Find max or min in every window of size K
- **Examples**: LC 239 (Sliding Window Maximum), LC 1438 (Longest Subarray with Abs Diff ≤ Limit)
- **Pattern**: Decreasing deque for max, increasing deque for min; pop front when out of window

### **Pattern 2: DP Optimization with Bounded Range** — LC 1696
- **Description**: Optimize DP transitions where you pick the best value from a sliding range
- **Examples**: LC 1425 (Constrained Subsequence Sum), LC 1696 (Jump Game VI)
- **Pattern**: dp[i] = max(dp[j]) + val[i] for j in [i-K, i-1] → use monotonic deque for the max

### **Pattern 3: Shortest Subarray with Sum Constraint** — LC 862
- **Description**: Find shortest/longest subarray meeting a sum condition
- **Examples**: LC 862 (Shortest Subarray with Sum ≥ K), LC 1499 (Max Value of Equation)
- **Pattern**: Monotonic deque on prefix sums

### **Pattern 4: Multi-Queue (Min + Max simultaneously)** — LC 1438
- **Description**: Maintain both min and max in a sliding window
- **Examples**: LC 1438 (Longest Subarray with Abs Diff ≤ Limit)
- **Pattern**: Two deques — one increasing (min), one decreasing (max)

### **Pattern 5: Order-Statistic Window (deque does NOT work)** — LC 480
- **Description**: Window query needs the **median / k-th smallest**, not just min or max
- **Examples**: LC 480 (Sliding Window Median)
- **Pattern**: A monotonic deque can only answer the *extreme* of a window. Anything "in the middle" needs an ordered multiset (`TreeSet` / `SortedList`) or **two heaps + lazy deletion** → O(N log K)

### **Pattern 6: Expiry Queue (range-effect that times out)** — LC 995
- **Description**: An operation applied at index `i` affects `[i, i+k-1]` and then expires
- **Examples**: LC 995 (Minimum Number of K Consecutive Bit Flips)
- **Pattern**: Queue holds indices of still-active operations; pop front when `front + k <= i`. `queue.size()` parity = current flip state (a difference-array in queue form). **Not** monotonic — the queue is used purely as an expiry window

### **Pattern 7: Circular Array → Doubled Prefix + Bounded Deque** — LC 918
- **Description**: Best subarray of a circular array (wrap-around allowed)
- **Examples**: LC 918 (Maximum Sum Circular Subarray)
- **Pattern**: Duplicate the array, take prefix sums, then maximize `prefix[i] - prefix[j]` with the extra constraint `i - j <= n` → increasing deque of prefix indices with front-expiry

## Templates & Algorithms

### Template 1: Sliding Window Maximum (Decreasing Deque) — LC 239

```java
// Java — LC 239 Sliding Window Maximum
// time = O(N), space = O(K)
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length;
    int[] result = new int[n - k + 1];
    Deque<Integer> deque = new ArrayDeque<>(); // stores indices

    for (int i = 0; i < n; i++) {
        // Remove elements outside the window
        while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
            deque.pollFirst();
        }
        // Remove smaller elements from back (they'll never be the max)
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
            deque.pollLast();
        }
        deque.offerLast(i);
        // Window is fully formed
        if (i >= k - 1) {
            result[i - k + 1] = nums[deque.peekFirst()];
        }
    }
    return result;
}
```

```python
# Python — LC 239 Sliding Window Maximum
# time = O(N), space = O(K)
from collections import deque

def maxSlidingWindow(nums, k):
    dq = deque()  # stores indices, front = max
    result = []
    for i, num in enumerate(nums):
        # Remove out-of-window
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        # Remove smaller from back
        while dq and nums[dq[-1]] < num:
            dq.pop()
        dq.append(i)
        if i >= k - 1:
            result.append(nums[dq[0]])
    return result
```

### Template 2: DP Optimization — Jump Game VI (LC 1696)

```java
// Java — LC 1696 Jump Game VI
// dp[i] = max(dp[j] for j in [i-k, i-1]) + nums[i]
// time = O(N), space = O(N)
public int maxResult(int[] nums, int k) {
    int n = nums.length;
    int[] dp = new int[n];
    dp[0] = nums[0];
    Deque<Integer> deque = new ArrayDeque<>();
    deque.offerLast(0);

    for (int i = 1; i < n; i++) {
        // Remove out-of-range
        while (!deque.isEmpty() && deque.peekFirst() < i - k) {
            deque.pollFirst();
        }
        dp[i] = dp[deque.peekFirst()] + nums[i];
        // Maintain decreasing order of dp values
        while (!deque.isEmpty() && dp[deque.peekLast()] <= dp[i]) {
            deque.pollLast();
        }
        deque.offerLast(i);
    }
    return dp[n - 1];
}
```

```python
# Python — LC 1696 Jump Game VI
from collections import deque

def maxResult(nums, k):
    n = len(nums)
    dp = [0] * n
    dp[0] = nums[0]
    dq = deque([0])

    for i in range(1, n):
        while dq and dq[0] < i - k:
            dq.popleft()
        dp[i] = dp[dq[0]] + nums[i]
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        dq.append(i)
    return dp[-1]
```

### Template 3: Shortest Subarray with Sum ≥ K (LC 862)

```python
# Python — LC 862 (handles negative numbers via prefix sum + monotonic deque)
# time = O(N), space = O(N)
from collections import deque

def shortestSubarray(nums, k):
    n = len(nums)
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + nums[i]

    dq = deque()  # increasing deque of indices into prefix
    ans = n + 1

    for i in range(n + 1):
        # If prefix[i] - prefix[dq.front] >= k, we found a valid subarray
        while dq and prefix[i] - prefix[dq[0]] >= k:
            ans = min(ans, i - dq.popleft())
        # Maintain increasing order of prefix values
        while dq and prefix[dq[-1]] >= prefix[i]:
            dq.pop()
        dq.append(i)

    return ans if ans <= n else -1
```

### Template 4: Sliding Window Median — Ordered Multiset / Two Heaps (LC 480) ⭐⭐⭐⭐

> **Why not a deque?** LC 239 works because the answer is always an *extreme* of the window, so dominated elements can be discarded forever. A **median** may be any element, so nothing can be discarded → deque is useless here. Use an ordered multiset, or two heaps with **lazy deletion**.

```java
// java
// LC 480 - Sliding Window Median
// IDEA: keep window split into two ordered halves: small (lower half) / large (upper half).
//       Invariant: small.size() == large.size()  OR  small.size() == large.size() + 1
//       => median is small.last() (odd k) or avg(small.last(), large.first()) (even k).
//       TreeSet stores INDICES with a (value, index) comparator, so duplicates are kept distinct.
// time = O(N log K), space = O(K)
public double[] medianSlidingWindow(int[] nums, int k) {
    Comparator<Integer> byVal = (a, b) ->
            nums[a] != nums[b] ? Integer.compare(nums[a], nums[b]) : Integer.compare(a, b);
    TreeSet<Integer> small = new TreeSet<>(byVal);  // lower half, max at last()
    TreeSet<Integer> large = new TreeSet<>(byVal);  // upper half, min at first()
    double[] res = new double[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
        // --- add index i, then rebalance ---
        small.add(i);
        large.add(small.pollLast());
        if (large.size() > small.size()) small.add(large.pollFirst());

        if (i >= k - 1) {
            res[i - k + 1] = (k % 2 == 1)
                    ? (double) nums[small.last()]
                    // cast BOTH: nums[i] can be Integer.MAX_VALUE -> int sum overflows
                    : ((double) nums[small.last()] + (double) nums[large.first()]) / 2.0;

            // --- remove the index leaving the window, then rebalance ---
            int out = i - k + 1;
            if (!small.remove(out)) large.remove(out);
            if (small.size() < large.size()) small.add(large.pollFirst());
            else if (small.size() > large.size() + 1) large.add(small.pollLast());
        }
    }
    return res;
}
```

```python
# python
# LC 480 - Sliding Window Median
# IDEA: two heaps + LAZY DELETION. heapq has no remove(), so mark a value as
#       "delayed" and only physically pop it once it surfaces at a heap top.
#       Keep LOGICAL sizes separately (small_sz / large_sz) — len(heap) is unreliable.
# time = O(N log N), space = O(N)
import heapq
from collections import defaultdict

def medianSlidingWindow(nums, k):
    small, large = [], []        # small: max-heap (negated) = lower half; large: min-heap = upper half
    delayed = defaultdict(int)   # value -> pending deletions
    small_sz = large_sz = 0      # logical sizes

    def prune(heap):
        # drop already-deleted values sitting on top
        while heap:
            v = -heap[0] if heap is small else heap[0]
            if delayed[v] > 0:
                delayed[v] -= 1
                heapq.heappop(heap)
            else:
                break

    def rebalance():
        nonlocal small_sz, large_sz
        if small_sz > large_sz + 1:
            heapq.heappush(large, -heapq.heappop(small))
            small_sz -= 1; large_sz += 1
            prune(small)
        elif small_sz < large_sz:
            heapq.heappush(small, -heapq.heappop(large))
            large_sz -= 1; small_sz += 1
            prune(large)

    def add(v):
        nonlocal small_sz, large_sz
        if not small or v <= -small[0]:
            heapq.heappush(small, -v); small_sz += 1
        else:
            heapq.heappush(large, v); large_sz += 1
        rebalance()

    def remove(v):
        nonlocal small_sz, large_sz
        delayed[v] += 1
        if small and v <= -small[0]:
            small_sz -= 1
            if v == -small[0]:
                prune(small)
        else:
            large_sz -= 1
            if large and v == large[0]:
                prune(large)
        rebalance()

    res = []
    for i, v in enumerate(nums):
        add(v)
        if i >= k - 1:
            res.append(float(-small[0]) if k % 2 else (-small[0] + large[0]) / 2.0)
            remove(nums[i - k + 1])
    return res

# One-liner alternative if `sortedcontainers` is allowed (O(N log K)):
#   sl = SortedList(nums[:k]); median = (sl[k // 2] + sl[(k - 1) // 2]) / 2.0
#   then per step: sl.remove(nums[i - k]); sl.add(nums[i])
```

### Template 5: Expiry Queue — K-Consecutive Bit Flips (LC 995) ⭐⭐⭐⭐

> **Twist vs Template 1**: the deque here is **not monotonic**. It stores the start index of each still-active flip; `queue.size() % 2` is the accumulated flip state at `i` (a difference array expressed as a queue).

```java
// java
// LC 995 - Minimum Number of K Consecutive Bit Flips
// IDEA: greedy left-to-right — if the EFFECTIVE value at i is 0, we must flip [i, i+k-1].
//       Queue keeps start indices of flips still covering i; pop front once front + k <= i.
//       effective(i) = nums[i] XOR (q.size() % 2)  ->  need flip when (q.size() + nums[i]) is even.
// time = O(N), space = O(K)
public int minKBitFlips(int[] nums, int k) {
    Deque<Integer> q = new ArrayDeque<>();  // start indices of active flips
    int res = 0;
    for (int i = 0; i < nums.length; i++) {
        if (!q.isEmpty() && q.peekFirst() + k <= i) q.pollFirst();  // flip expired
        if ((q.size() + nums[i]) % 2 == 0) {                        // effective bit is 0
            if (i + k > nums.length) return -1;                     // no room to flip
            q.offerLast(i);
            res++;
        }
    }
    return res;
}
```

```python
# python
# LC 995 - Minimum Number of K Consecutive Bit Flips
# time = O(N), space = O(K)
from collections import deque

def minKBitFlips(nums, k):
    q = deque()   # start indices of flips still in effect
    res = 0
    for i, x in enumerate(nums):
        if q and q[0] + k <= i:
            q.popleft()
        if (len(q) + x) % 2 == 0:      # effective bit is 0 -> must flip here
            if i + k > len(nums):
                return -1
            q.append(i)
            res += 1
    return res
```

### Template 6: Circular Subarray — Doubled Prefix + Bounded Deque (LC 918) ⭐⭐⭐

> **Twist vs Template 3**: same "prefix sum + increasing deque", but we **maximize** `prefix[i] - prefix[j]` under a *length* cap instead of finding the shortest subarray meeting a sum bound. The cap `i - j <= n` is exactly what the front-expiry pop enforces.

```java
// java
// LC 918 - Maximum Sum Circular Subarray
// IDEA: duplicate the array (length 2n) so every wrap-around subarray becomes a normal one.
//       Answer = max over i of prefix[i] - min(prefix[j]) for j in [i-n, i-1].
//       Increasing deque of prefix indices: front = smallest prefix still in range.
//       Works for all-negative input (single elements are always candidates).
// time = O(N), space = O(N)
public int maxSubarraySumCircular(int[] nums) {
    int n = nums.length;
    int[] prefix = new int[2 * n + 1];
    for (int i = 0; i < 2 * n; i++) prefix[i + 1] = prefix[i] + nums[i % n];

    Deque<Integer> dq = new ArrayDeque<>();   // indices into prefix, increasing prefix values
    dq.offerLast(0);
    int ans = Integer.MIN_VALUE;

    for (int i = 1; i <= 2 * n; i++) {
        while (!dq.isEmpty() && dq.peekFirst() < i - n) dq.pollFirst();   // length cap
        ans = Math.max(ans, prefix[i] - prefix[dq.peekFirst()]);
        while (!dq.isEmpty() && prefix[dq.peekLast()] >= prefix[i]) dq.pollLast();
        dq.offerLast(i);
    }
    return ans;
}
```

```python
# python
# LC 918 - Maximum Sum Circular Subarray
# time = O(N), space = O(N)
from collections import deque

def maxSubarraySumCircular(nums):
    n = len(nums)
    prefix = [0] * (2 * n + 1)
    for i in range(2 * n):
        prefix[i + 1] = prefix[i] + nums[i % n]

    dq = deque([0])          # increasing prefix values
    ans = float('-inf')
    for i in range(1, 2 * n + 1):
        while dq and dq[0] < i - n:      # subarray length must stay <= n
            dq.popleft()
        ans = max(ans, prefix[i] - prefix[dq[0]])
        while dq and prefix[dq[-1]] >= prefix[i]:
            dq.pop()
        dq.append(i)
    return ans

# Note: the O(1)-space "Kadane" solution is max(maxKadane, total - minKadane),
# with the guard: if maxKadane < 0 (all negatives) return maxKadane.
```

## Key Decision Guide

```text
Need sliding window max/min?
  → Monotonic Deque: O(N) time, O(K) space
  → Alternative: Heap O(N log N) — slower but simpler

DP transition = max/min over a range of size K?
  → Monotonic Deque optimization: O(N) instead of O(N·K)

Subarray sum with negative numbers?
  → Prefix sum + monotonic deque (LC 862 pattern)

Need BOTH min and max in window?
  → Two deques: one increasing, one decreasing
```

## LC Example

| # | Problem | Difficulty | Pattern | Time | Space |
|---|---------|------------|---------|------|-------|
| 239 | Sliding Window Maximum | Hard | Decreasing deque | O(N) | O(K) |
| 862 | Shortest Subarray with Sum ≥ K | Hard | Prefix sum + increasing deque | O(N) | O(N) |
| 1425 | Constrained Subsequence Sum | Hard | DP + decreasing deque | O(N) | O(N) |
| 1438 | Longest Subarray Abs Diff ≤ Limit | Medium | Two deques (min + max) | O(N) | O(N) |
| 1696 | Jump Game VI | Medium | DP + decreasing deque | O(N) | O(K) |
| 1499 | Max Value of Equation | Hard | Decreasing deque | O(N) | O(N) |
| 2398 | Max Number of Robots Within Budget | Hard | Sliding window + deque | O(N) | O(N) |
| 480 | Sliding Window Median | Hard | Ordered multiset / two heaps + lazy delete (deque fails) | O(N log K) | O(K) |
| 995 | Min Number of K Consecutive Bit Flips | Hard | Expiry queue (non-monotonic) | O(N) | O(K) |
| 918 | Maximum Sum Circular Subarray | Medium | Doubled prefix + increasing deque | O(N) | O(N) |
| 2071 | Maximum Number of Tasks You Can Assign | Hard | Binary search on answer + deque as candidate pool | O(N log N) | O(N) |

### Scope Note

This doc covers only windows answered with a **deque**. Variable-size two-pointer windows with a
counter/hash-map (LC 3, 76, 209, 424, 438, 567, 992, 1004) live in [`sliding_window.md`](sliding_window.md);
queue/deque **design** problems (LC 622, 641, 232, 225) live in [`queue.md`](queue.md).
