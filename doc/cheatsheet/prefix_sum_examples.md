# Prefix Sum — Worked Examples

> **Scope** — The worked-solution archive behind [prefix_sum.md](./prefix_sum.md): the seven problems the templates do not already solve end to end, grouped by which prefix-sum shape they need.
> **See also**: [prefix_sum.md](./prefix_sum.md) — the parent sheet: templates 1–8, the concept and the decision framework; [prefix_sum_advanced.md](./prefix_sum_advanced.md) — templates 9–13; [difference_array.md](./difference_array.md) — range updates in their own right, including LC 370; [sliding_window.md](./sliding_window.md) — the alternative when all values are non-negative; [hash_map.md](./hash_map.md) — the structure four of these turn on.

## LeetCode Problem Lists

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## Overview

This is the long tail of [prefix_sum.md](./prefix_sum.md), and it is deliberately short. The
parent sheet's thirteen templates each name the LC problem they solve, so an example section
that re-solved those problems was the file's largest source of duplication — fourteen LC numbers
appeared in more than one section heading, the worst count measured anywhere in the corpus.

What is left are the problems no template already works end to end.

### Key Properties
- **Complexity**: every solution below is O(n) time after the prefix array is built, except LC 1292, which is O(m·n·log(min(m,n)))
- **Core Idea**: four of the seven are the same move — a hash map from prefix value to how many times it has been seen — applied to a different transform of the input
- **When to Use**: after the parent's decision framework has named the template


## Subarray Sums with a HashMap

### 1) Maximum Size Subarray Sum Equals k — LC 325

```python
# LC 325. Maximum Size Subarray Sum Equals k
# V0 
# time complexity : O(N) | space complexity : O(N)
# IDEA : HASH TBALE
# -> have a var acc keep sum of all item in nums,
# -> and use dic collect acc and its index
# -> since we want to find nums[i:j] = k  -> so it's a 2 sum problem now
# -> i.e. if acc - k in dic => there must be a solution (i,j) of  nums[i:j] = k  
# -> return the max result 
# -> ### acc DEMO : given array a = [1,2,3,4,5] ###
# -> acc_list = [1,3,6,10,15]
# -> so sum(a[1:3]) = 9 = acc_list[3] - acc_list[1-1] = 10 - 1 = 9 
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        # NOTE !!! we init dic as {0:-1} ({sum:idx})
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                ### NOTE : we save idx as dict value
                dic[acc] = i
            ### acc - x = k -> so x = acc - k, that's why we check if acc - x in the dic or not
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result
```

### 2) Continuous Subarray Sum — LC 523


```java
// java
// LC 523
// V1
// IDEA : HASHMAP
// https://leetcode.com/problems/continuous-subarray-sum/editorial/
// https://github.com/yennanliu/CS_basics/blob/master/doc/pic/presum_mod.png
public boolean checkSubarraySum_1(int[] nums, int k) {
    int prefixMod = 0;
    HashMap<Integer, Integer> modSeen = new HashMap<>();
    modSeen.put(0, -1);

    for (int i = 0; i < nums.length; i++) {
        /**
         * NOTE !!! we get `mod of prefixSum`, instead of get prefixSum
         */
        prefixMod = (prefixMod + nums[i]) % k;

        if (modSeen.containsKey(prefixMod)) {
            // ensures that the size of subarray is at least 2
            if (i - modSeen.get(prefixMod) > 1) {
                return true;
            }
        } else {
            // mark the value of prefixMod with the current index.
            modSeen.put(prefixMod, i);
        }
    }

    return false;
}
```

### 3) Longest Well-Performing Interval — LC 1124


**Pattern:** HashMap + Prefix Sum — Longest Subarray with Positive Sum

**Core Idea:**
Transform each day: tiring (`hours[i] > 8`) → `+1`, non-tiring → `-1`. The problem becomes: find the longest subarray whose sum > 0.

```text
At each index i with running prefix sum p:

  Case 1: p > 0
    → entire interval [0..i] is valid
    → length = i + 1

  Case 2: p ≤ 0
    → look for the earliest index j where prefix[j] = p - 1
    → subarray [j+1..i] has sum = p - (p-1) = 1 > 0
    → length = i - j

Why (p - 1)?
  We want the LONGEST span ending at i with a net positive sum.
  That means we need the SMALLEST prefix sum just one below the current value,
  recorded at the EARLIEST index possible — hence putIfAbsent (first occurrence only).
```

**Key Difference from Template 2:**
- Template 2 stores `{prefix_sum: count}` for counting subarrays.
- This variant stores `{prefix_sum: first_index}` for maximum length — only the first occurrence matters because an earlier start gives a longer interval.

**Java Code:**
```java
// LC 1124 — Time: O(n), Space: O(n)
public int longestWPI(int[] hours) {
    Map<Integer, Integer> map = new HashMap<>();
    int prefix = 0, maxLen = 0;

    for (int i = 0; i < hours.length; i++) {
        prefix += hours[i] > 8 ? 1 : -1;

        if (prefix > 0) {
            maxLen = i + 1;                           // whole prefix is valid
        } else {
            if (map.containsKey(prefix - 1)) {
                maxLen = Math.max(maxLen, i - map.get(prefix - 1));
            }
        }
        map.putIfAbsent(prefix, i);                   // first occurrence only
    }
    return maxLen;
}
```

**Similar LCs:**
| Problem | LC # | Similarity |
|---------|------|------------|
| Contiguous Array | 525 | Longest subarray with equal 0s and 1s — same pattern, target sum = 0 |
| Maximum Size Subarray Sum Equals k | 325 | Longest subarray with sum = k, first-occurrence map |
| Subarray Sum Equals K | 560 | Count variant (store count, not index) |
| Binary Subarrays With Sum | 930 | Count subarrays with binary-transformed sum = k |

### 4) Flip String to Monotone Increasing — LC 926


```python
# LC 926. Flip String to Monotone Increasing
# NOTE : there is also dp approaches
# V0 
# IDEA : PREFIX SUM
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # find min
        res = float('inf')
        for j in range(len(P)):
            res = min(res, P[j] + len(S)-j-(P[-1]-P[j]))
        return res

# V1
# IDEA : PREFIX SUM
# https://leetcode.com/problems/flip-string-to-monotone-increasing/solution/
class Solution(object):
    def minFlipsMonoIncr(self, S):
        # get pre-fix sum
        P = [0]
        for x in S:
            P.append(P[-1] + int(x))
        # return min
        return min(P[j] + len(S)-j-(P[-1]-P[j])
                   for j in range(len(P)))
```

## Fixed and Paired Windows

### 5) Maximum Sum of Two Non-Overlapping Subarrays — LC 1031


**Core Idea (LC 1031):**
```text
Given two non-overlapping windows of fixed lengths L and M, maximize their combined sum.

Key Insight: one window must come before the other. Handle both orderings separately:
  - Case 1: L-window appears before M-window
  - Case 2: M-window appears before L-window

For each position i (right edge of the second window), track the maximum
sum of the first window seen so far (t), then combine with the current second window.

Prefix sum formula for a window of length W ending at index i (1-based):
  window_sum = prefix[i] - prefix[i - W]

At each step:
  t   = max(t, prefix[i - M] - prefix[i - M - L])   ← best L-window before M starts
  ans = max(ans, t + prefix[i] - prefix[i - M])      ← best L + current M

Why two passes? The two window orders (L before M, M before L) are
independent. The overall answer is max of both passes.
```

**Pattern:** Prefix Sum + Running Maximum (two-pass)
- Build prefix sum array once: O(n)
- For each pass, slide the second window right while maintaining `maxFirst` (best first window so far)
- Two passes cover all non-overlapping configurations

```java
// java
// LC 1031 — Prefix Sum + Running Max
// time: O(N), space: O(N)
public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
    int n = nums.length;
    int[] s = new int[n + 1];
    for (int i = 0; i < n; ++i) {
        s[i + 1] = s[i] + nums[i];
    }
    int ans = 0;

    // Case 1: firstLen window comes before secondLen window
    // i is the right edge (exclusive) of the secondLen window
    for (int i = firstLen, t = 0; i + secondLen - 1 < n; ++i) {
        // best firstLen window that ends at or before position i (before M starts)
        t = Math.max(t, s[i] - s[i - firstLen]);
        // current secondLen window starting at i
        ans = Math.max(ans, t + s[i + secondLen] - s[i]);
    }

    // Case 2: secondLen window comes before firstLen window
    for (int i = secondLen, t = 0; i + firstLen - 1 < n; ++i) {
        t = Math.max(t, s[i] - s[i - secondLen]);
        ans = Math.max(ans, t + s[i + firstLen] - s[i]);
    }
    return ans;
}
```

**Alternative helper-function style (cleaner):**
```java
// Calls helper(L before M) and helper(M before L), returns max
public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
    int n = nums.length;
    int[] prefix = new int[n + 1];
    for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];
    return Math.max(helper(prefix, firstLen, secondLen),
                    helper(prefix, secondLen, firstLen));
}

// L comes before M
private int helper(int[] prefix, int L, int M) {
    int maxL = 0, res = 0;
    for (int i = L + M; i < prefix.length; i++) {
        // best L-window ending just before the M-window
        maxL = Math.max(maxL, prefix[i - M] - prefix[i - M - L]);
        // current M-window
        res  = Math.max(res, maxL + prefix[i] - prefix[i - M]);
    }
    return res;
}
```

**Python (prefix sum + running max):**
```python
# python
# LC 1031 — Prefix Sum + Running Max
# time: O(N), space: O(N)
# ref: leetcode_python/Array/maximum-sum-of-two-non-overlapping-subarrays.py
class Solution:
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        n = len(nums)

        # prefix[i] = sum(nums[:i])   (size n+1, prefix[0] = 0 sentinel)
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]

        # maxSum(L, M): best combined sum when the L-window is BEFORE the M-window
        def maxSum(L, M):
            # bestL = best L-window seen so far, ending before the current M-window
            bestL = prefix[L] - prefix[0]
            ans = 0

            # i = starting index of the M-window
            for i in range(L, n - M + 1):
                # update best L-window ending at index i (i.e. nums[i-L:i])
                bestL = max(bestL, prefix[i] - prefix[i - L])
                # current M-window = nums[i:i+M]
                currM = prefix[i + M] - prefix[i]
                ans = max(ans, bestL + currM)

            return ans

        # try BOTH orders: L-before-M and M-before-L
        return max(maxSum(firstLen, secondLen),
                   maxSum(secondLen, firstLen))
```

**Why this is correct (core idea recap):**
```text
1. Core idea
   - Two fixed-length windows (L and M) that must NOT overlap.
   - One window is always fully to the left of the other, so enumerate
     both orderings and take the max.
   - Within one ordering, freeze the M-window's start (i), then the best
     L-window is any L-window ending at/before i — track it as a running
     max `bestL` so each i costs O(1).

2. Pattern
   - Prefix Sum (O(1) window sum) + Running Maximum (best left window so far).
   - Single left-to-right sweep per ordering → 2 sweeps total, O(n) each.
   - window_sum for length W ending at index i:  prefix[i] - prefix[i - W]

3. Similar LC  → see table below
```

**Similar LCs:**
| Problem | LC # | Similarity |
|---------|------|------------|
| Maximum Subarray | 53 | Running max subarray (Kadane's) |
| Best Time to Buy and Sell Stock III | 123 | Two non-overlapping operations, prefix+suffix |
| Maximum Sum of 3 Non-Overlapping Subarrays | 689 | Same pattern extended to 3 windows |
| Subarray Sum Equals K | 560 | Prefix sum + HashMap |
| Maximum Average Subarray II | 644 | Fixed/variable window with prefix sum |

## 2D Prefix Sums

### 6) Maximum Side Length of a Square with Sum ≤ Threshold — LC 1292


**Pattern:** 2D Prefix Sum + Binary Search **or** 2D Prefix Sum + Greedy

**Core Idea:**
1. Build a 2D prefix sum table (size `(m+1) x (n+1)`) so any square's sum is computed in O(1).
2. **Binary Search approach**: Binary search on side length `[1, min(m,n)]`. For each candidate length `mid`, scan all valid top-left corners and check if any square sum ≤ threshold. → O(m·n·log(min(m,n)))
3. **Greedy approach**: Single pass over all cells; at each cell `(i,j)`, only test if a square of side `maxSide+1` fits. If yes, increment `maxSide`. → O(m·n)

**2D Prefix Sum formula (square ending at (i,j) with side `k`):**
```text
sum = P[i][j] - P[i-k][j] - P[i][j-k] + P[i-k][j-k]
```

**Binary Search approach (Java):**
```java
// LC 1292 - V1 Binary Search
public int maxSideLength(int[][] mat, int threshold) {
    int m = mat.length, n = mat[0].length;
    int[][] P = new int[m + 1][n + 1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            P[i][j] = mat[i-1][j-1] + P[i-1][j] + P[i][j-1] - P[i-1][j-1];

    int l = 1, r = Math.min(m, n), ans = 0;
    while (l <= r) {
        int mid = (l + r) / 2;
        boolean found = false;
        outer:
        for (int i = mid; i <= m; i++) {
            for (int j = mid; j <= n; j++) {
                int sum = P[i][j] - P[i-mid][j] - P[i][j-mid] + P[i-mid][j-mid];
                if (sum <= threshold) { found = true; break outer; }
            }
        }
        if (found) { ans = mid; l = mid + 1; }
        else r = mid - 1;
    }
    return ans;
}
```

**Greedy approach (Java):**
```java
// LC 1292 - V0 Greedy (O(m*n), optimal)
public int maxSideLength(int[][] mat, int threshold) {
    int m = mat.length, n = mat[0].length;
    int[][] P = new int[m + 1][n + 1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            P[i][j] = mat[i-1][j-1] + P[i-1][j] + P[i][j-1] - P[i-1][j-1];

    int maxSide = 0;
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            int k = maxSide + 1;           // only try to improve by 1
            if (i >= k && j >= k) {
                int sum = P[i][j] - P[i-k][j] - P[i][j-k] + P[i-k][j-k];
                if (sum <= threshold) maxSide++;
            }
        }
    }
    return maxSide;
}
```

**Why Greedy works:** We only need to know the *maximum* achievable side length. Scanning left-to-right, top-to-bottom ensures we never miss a valid square — if a larger square exists somewhere, it will be discovered when we reach its bottom-right corner.

**Similar LCs:**
| Problem | LC # | Similarity |
|---------|------|------------|
| Range Sum Query 2D | 304 | Core 2D prefix sum template |
| Matrix Block Sum | 1314 | Fixed-radius 2D range query |
| Number of Submatrices That Sum to Target | 1074 | 2D prefix sum + count (harder) |
| Maximal Square | 221 | Max square in matrix (DP approach) |
| Largest 1-Bordered Square | 1139 | Max square with border condition |

## Range Updates

### 7) Range Addition II / prefix on a difference array — LC 1094


```java
// java

// LC 1094

// ...

int[] prefixSum = new int[1001]; // the biggest array size given by problem

// `init pre prefix sum`
for (int[] t : trips) {
    
    int amount = t[0];
    int start = t[1];
    int end = t[2];

    /**
     *  NOTE !!!!
     *
     *   via trick below, we can `efficiently` setup prefix sum
     *   per start, end index
     *
     *   -> we ADD amount at start point (customer pickup up)
     *   -> we MINUS amount at `end point` (customer drop off)
     *
     *   -> via above, we get the `adjusted` `init prefix sum`
     *   -> so all we need to do next is :
     *      -> loop over the `init prefix sum`
     *      -> and keep adding `previous to current val`
     *      -> e.g. prefixSum[i] = prefixSum[i-1] + prefixSum[i]
     *
     */
    prefixSum[start] += amount;
    prefixSum[end] -= amount;
}

// update `prefix sum` array
for (int i = 1; i < prefixSum.length; i++) {
    prefixSum[i] += prefixSum[i - 1];
}


// ...
```
