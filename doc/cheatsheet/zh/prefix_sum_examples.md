# 前綴和 — 範例詳解

> **範圍** — [prefix_sum.md](./prefix_sum.md) 背後的解題存檔：七道模板無法從頭到尾解掉的題目，依照它們需要哪種前綴和形狀分組。
> **另見**：[prefix_sum.md](./prefix_sum.md) — 母文件：模板 1–8、觀念與決策框架；[prefix_sum_advanced.md](./prefix_sum_advanced.md) — 模板 9–13；[difference_array.md](./difference_array.md) — 區間更新本身的完整討論，含 LC 370；[sliding_window.md](./sliding_window.md) — 當所有數值都非負時的替代解法；[hash_map.md](./hash_map.md) — 這裡有四題的關鍵結構。

## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## 總覽

這是 [prefix_sum.md](./prefix_sum.md) 的長尾，而且刻意寫得很短。母文件的十三個模板各自標明了自己解掉的
LC 題號，所以一個把那些題目重解一遍的範例區塊，就是整份檔案最大的重複來源 — 有十四個 LC 題號
出現在不只一個章節標題裡，是整個文件庫測出來最糟的數字。

留下來的，是沒有任何模板能直接完整解掉的題目。

### 關鍵性質
- **複雜度**：下面每一個解法在前綴和陣列建好之後都是 O(n) 時間，只有 LC 1292 例外，它是 O(m·n·log(min(m,n)))
- **核心想法**：七題裡有四題其實是同一招 — 用雜湊表記錄「前綴值 → 出現過幾次」 — 只是套在輸入的不同轉換上
- **什麼時候用**：等母文件的決策框架先幫你選好模板之後


## 用雜湊表處理子陣列和

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


**模式：** 雜湊表 + 前綴和 — 找出總和為正的最長子陣列

**核心想法：**
把每一天做轉換：疲勞（`hours[i] > 8`）→ `+1`，不疲勞 → `-1`。問題就變成：找出總和 > 0 的最長子陣列。

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

**和模板 2 的關鍵差異：**
- 模板 2 存的是 `{prefix_sum: count}`，用來計算子陣列個數。
- 這個變形存的是 `{prefix_sum: first_index}`，用來求最大長度 — 只有第一次出現才有意義，因為起點越早，區間越長。

**Java 程式碼：**
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

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Contiguous Array | 525 | 0 和 1 一樣多的最長子陣列 — 同一個模式，目標和 = 0 |
| Maximum Size Subarray Sum Equals k | 325 | 總和 = k 的最長子陣列，首次出現位置的雜湊表 |
| Subarray Sum Equals K | 560 | 計數版（存次數，不是索引） |
| Binary Subarrays With Sum | 930 | 計算二元轉換後總和 = k 的子陣列個數 |

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

## 固定視窗與成對視窗

### 5) Maximum Sum of Two Non-Overlapping Subarrays — LC 1031


**核心想法（LC 1031）：**
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

**模式：** 前綴和 + 滾動最大值（掃兩趟）
- 前綴和陣列只建一次：O(n)
- 每一趟都把第二個視窗往右滑，同時維護 `maxFirst`（到目前為止最好的第一個視窗）
- 兩趟就涵蓋了所有不重疊的擺法

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

**另一種輔助函式寫法（比較乾淨）：**
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

**Python（前綴和 + 滾動最大值）：**
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

**為什麼這樣是對的（核心想法回顧）：**
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

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Maximum Subarray | 53 | 滾動最大子陣列（Kadane） |
| Best Time to Buy and Sell Stock III | 123 | 兩段不重疊的操作，前綴 + 後綴 |
| Maximum Sum of 3 Non-Overlapping Subarrays | 689 | 同一個模式擴充到 3 個視窗 |
| Subarray Sum Equals K | 560 | 前綴和 + 雜湊表 |
| Maximum Average Subarray II | 644 | 固定／可變視窗搭配前綴和 |

## 二維前綴和

### 6) Maximum Side Length of a Square with Sum ≤ Threshold — LC 1292


**模式：** 二維前綴和 + 二分搜尋 **或** 二維前綴和 + 貪婪

**核心想法：**
1. 建一張二維前綴和表（大小 `(m+1) x (n+1)`），這樣任何一個正方形的總和都能 O(1) 算出來。
2. **二分搜尋解法**：對邊長 `[1, min(m,n)]` 做二分搜尋。對每個候選邊長 `mid`，掃過所有合法的左上角，檢查是否存在總和 ≤ threshold 的正方形。→ O(m·n·log(min(m,n)))
3. **貪婪解法**：掃過所有格子一次；在每個格子 `(i,j)` 只測試邊長 `maxSide+1` 的正方形放不放得下。放得下就把 `maxSide` 加一。→ O(m·n)

**二維前綴和公式（以 (i,j) 為右下角、邊長 `k` 的正方形）：**
```text
sum = P[i][j] - P[i-k][j] - P[i][j-k] + P[i-k][j-k]
```

**二分搜尋解法（Java）：**
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

**貪婪解法（Java）：**
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

**貪婪為什麼可行：** 我們只在乎能達到的*最大*邊長。由左到右、由上到下掃描保證不會漏掉任何合法正方形 — 如果某處存在更大的正方形，走到它的右下角時就一定會被發現。

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Range Sum Query 2D | 304 | 二維前綴和的核心模板 |
| Matrix Block Sum | 1314 | 固定半徑的二維區間查詢 |
| Number of Submatrices That Sum to Target | 1074 | 二維前綴和 + 計數（更難） |
| Maximal Square | 221 | 矩陣中的最大正方形（DP 解法） |
| Largest 1-Bordered Square | 1139 | 有邊框條件的最大正方形 |

## 區間更新

### 7) Range Addition II / 差分陣列上的前綴和 — LC 1094


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
