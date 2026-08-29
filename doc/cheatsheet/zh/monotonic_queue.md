# 單調佇列（雙端佇列）

> **範圍** — 用一個維持單調性的雙端佇列，以攤還 O(1) 的成本求滑動視窗的最大／最小值。
> **另見**：[queue.md](./queue.md) — 單純的 FIFO；[monotonic_stack.md](./monotonic_stack.md) — 沒有視窗限制的對應版本；[sliding_window.md](./sliding_window.md) — 視窗本身的運作機制；[heap.md](./heap.md) — O(log n) 的替代方案。

## LeetCode 題目清單

- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Queue](https://leetcode.com/problem-list/queue/)

## 概觀
**單調佇列**是一種讓元素保持單調順序（遞增或遞減）的雙端佇列。單調堆疊只能從一端移除元素，單調佇列則是**兩端都能移除**——這正是它能高效回答滑動視窗最小／最大值的原因。

### 關鍵性質
- **時間複雜度**：滑動視窗類問題攤還 O(N)（每個元素最多進出各一次）
- **空間複雜度**：O(K)，K 是視窗大小
- **核心想法**：把候選者按單調順序放好；從前端丟掉過期的，從後端丟掉被壓過的
- **什麼時候用**：滑動視窗最小／最大值、有範圍限制的子陣列最佳化、DP 最佳化

### 單調佇列 vs 單調堆疊

| 特性 | 單調堆疊 | 單調佇列 |
|---------|----------------|-----------------|
| 結構 | 堆疊（單端） | 雙端佇列（兩端） |
| 能從前端移除？ | 不行 | 可以——過期／視窗 |
| 主要用途 | 下一個更大／更小的元素 | 滑動視窗最小／最大值 |
| 有視窗限制？ | 沒有 | 有——範圍受限 |
| 代表題 | LC 84（直方圖） | LC 239（滑動視窗最大值） |

## 題型分類

### **模式 1：滑動視窗最大／最小值** — LC 239
- **說明**：求每個大小為 K 的視窗裡的最大或最小值
- **例題**：LC 239（Sliding Window Maximum）、LC 1438（Longest Subarray with Abs Diff ≤ Limit）
- **模式**：求最大值用遞減佇列，求最小值用遞增佇列；元素滑出視窗就從前端彈掉

### **模式 2：範圍受限的 DP 最佳化** — LC 1696
- **說明**：DP 轉移時要從一個滑動範圍裡挑最好的值
- **例題**：LC 1425（Constrained Subsequence Sum）、LC 1696（Jump Game VI）
- **模式**：dp[i] = max(dp[j]) + val[i]，j 落在 [i-K, i-1] → 用單調雙端佇列維護這個最大值

### **模式 3：帶總和限制的最短子陣列** — LC 862
- **說明**：找出滿足某個總和條件的最短／最長子陣列
- **例題**：LC 862（Shortest Subarray with Sum ≥ K）、LC 1499（Max Value of Equation）
- **模式**：在前綴和上跑單調雙端佇列

### **模式 4：多佇列（同時維護最小與最大）** — LC 1438
- **說明**：在滑動視窗中同時掌握最小值與最大值
- **例題**：LC 1438（Longest Subarray with Abs Diff ≤ Limit）
- **模式**：兩條雙端佇列——一條遞增（最小值）、一條遞減（最大值）

### **模式 5：順序統計量視窗（雙端佇列在這裡行不通）** — LC 480
- **說明**：視窗查詢要的是**中位數／第 k 小**，而不只是最小或最大值
- **例題**：LC 480（Sliding Window Median）
- **模式**：單調雙端佇列只能回答視窗的*極值*。任何「落在中間」的東西，都需要有序多重集合（`TreeSet` / `SortedList`）或**兩個堆積＋延遲刪除** → O(N log K)

### **模式 6：過期佇列（會自動失效的區間影響）** — LC 995
- **說明**：在索引 `i` 施加的操作會影響 `[i, i+k-1]`，之後就失效
- **例題**：LC 995（Minimum Number of K Consecutive Bit Flips）
- **模式**：佇列裝的是仍在生效的操作索引；當 `front + k <= i` 就從前端彈掉。`queue.size()` 的奇偶性就是目前的翻轉狀態（等於用佇列表達的差分陣列）。它**不是**單調的——這裡的佇列純粹當成一個過期視窗在用

### **模式 7：環狀陣列 → 加倍前綴和＋受限雙端佇列** — LC 918
- **說明**：求環狀陣列的最佳子陣列（允許繞回頭）
- **例題**：LC 918（Maximum Sum Circular Subarray）
- **模式**：把陣列接一份在後面、取前綴和，然後在 `i - j <= n` 的額外限制下最大化 `prefix[i] - prefix[j]` → 用前綴索引的遞增雙端佇列，搭配前端過期彈出

## 模板與演算法

### 模板 1：滑動視窗最大值（遞減雙端佇列） — LC 239

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

### 模板 2：DP 最佳化 — Jump Game VI（LC 1696）

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

### 模板 3：Shortest Subarray with Sum ≥ K（LC 862）

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

### 模板 4：滑動視窗中位數 — 有序多重集合／兩個堆積（LC 480） ⭐⭐⭐⭐

> **為什麼不能用雙端佇列？** LC 239 能用，是因為答案永遠是視窗的*極值*，所以被壓過的元素可以永遠丟掉。但**中位數**可能是任何一個元素，什麼都不能丟 → 雙端佇列在這裡毫無用處。改用有序多重集合，或兩個堆積搭配**延遲刪除**。

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

### 模板 5：過期佇列 — K-Consecutive Bit Flips（LC 995） ⭐⭐⭐⭐

> **和模板 1 的差別**：這裡的雙端佇列**不是單調的**。它存的是每個仍在生效的翻轉的起始索引；`queue.size() % 2` 就是位置 `i` 上累積的翻轉狀態（用佇列表達的差分陣列）。

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

### 模板 6：環狀子陣列 — 加倍前綴和＋受限雙端佇列（LC 918） ⭐⭐⭐

> **和模板 3 的差別**：一樣是「前綴和＋遞增雙端佇列」，但這裡是在*長度*上限之下**最大化** `prefix[i] - prefix[j]`，而不是找滿足總和下限的最短子陣列。前端過期彈出所執行的，正好就是 `i - j <= n` 這個上限。

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

## 關鍵決策指南

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

## LC 範例

| # | 題目 | Difficulty | 模式 | Time | Space |
|---|---------|------------|---------|------|-------|
| 239 | Sliding Window Maximum | Hard | 遞減雙端佇列 | O(N) | O(K) |
| 862 | Shortest Subarray with Sum ≥ K | Hard | 前綴和＋遞增雙端佇列 | O(N) | O(N) |
| 1425 | Constrained Subsequence Sum | Hard | DP＋遞減雙端佇列 | O(N) | O(N) |
| 1438 | Longest Subarray Abs Diff ≤ Limit | Medium | 兩條雙端佇列（最小＋最大） | O(N) | O(N) |
| 1696 | Jump Game VI | Medium | DP＋遞減雙端佇列 | O(N) | O(K) |
| 1499 | Max Value of Equation | Hard | 遞減雙端佇列 | O(N) | O(N) |
| 2398 | Max Number of Robots Within Budget | Hard | 滑動視窗＋雙端佇列 | O(N) | O(N) |
| 480 | Sliding Window Median | Hard | 有序多重集合／兩個堆積＋延遲刪除（雙端佇列失效） | O(N log K) | O(K) |
| 995 | Min Number of K Consecutive Bit Flips | Hard | 過期佇列（非單調） | O(N) | O(K) |
| 918 | Maximum Sum Circular Subarray | Medium | 加倍前綴和＋遞增雙端佇列 | O(N) | O(N) |
| 2071 | Maximum Number of Tasks You Can Assign | Hard | 對答案二分搜尋＋雙端佇列當候選池 | O(N log N) | O(N) |

### 範圍說明

這份文件只談用**雙端佇列**解決的視窗題。用計數器／雜湊表撐起來的可變長度雙指標視窗（LC 3、76、209、424、438、567、992、1004）在 [`sliding_window.md`](sliding_window.md)；佇列／雙端佇列的**設計**題（LC 622、641、232、225）在 [`queue.md`](queue.md)。
