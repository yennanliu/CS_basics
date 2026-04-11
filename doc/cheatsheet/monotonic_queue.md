# Monotonic Queue (Deque)

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

### **Pattern 1: Sliding Window Maximum/Minimum**
- **Description**: Find max or min in every window of size K
- **Examples**: LC 239 (Sliding Window Maximum), LC 1438 (Longest Subarray with Abs Diff ≤ Limit)
- **Pattern**: Decreasing deque for max, increasing deque for min; pop front when out of window

### **Pattern 2: DP Optimization with Bounded Range**
- **Description**: Optimize DP transitions where you pick the best value from a sliding range
- **Examples**: LC 1425 (Constrained Subsequence Sum), LC 1696 (Jump Game VI)
- **Pattern**: dp[i] = max(dp[j]) + val[i] for j in [i-K, i-1] → use monotonic deque for the max

### **Pattern 3: Shortest Subarray with Sum Constraint**
- **Description**: Find shortest/longest subarray meeting a sum condition
- **Examples**: LC 862 (Shortest Subarray with Sum ≥ K), LC 1499 (Max Value of Equation)
- **Pattern**: Monotonic deque on prefix sums

### **Pattern 4: Multi-Queue (Min + Max simultaneously)**
- **Description**: Maintain both min and max in a sliding window
- **Examples**: LC 1438 (Longest Subarray with Abs Diff ≤ Limit)
- **Pattern**: Two deques — one increasing (min), one decreasing (max)

## Templates & Algorithms

### Template 1: Sliding Window Maximum (Decreasing Deque)

```java
// Java — LC 239 Sliding Window Maximum
// Time: O(N), Space: O(K)
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
# Time: O(N), Space: O(K)
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
// Time: O(N), Space: O(N)
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
# Time: O(N), Space: O(N)
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

## Key Decision Guide

```
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
