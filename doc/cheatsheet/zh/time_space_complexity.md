# 時間與空間複雜度 — 經典 LC 程式碼逐題解析

> **範圍** — **逐題解析** — 怎麼替一份真實的 LC 解答論證複雜度，每個技巧配一道經典題。
> **另見**：[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 查表用；[complexity_drills.md](./complexity_drills.md) — 自我測驗。

> **這份文件是什麼：** 一份以程式碼為主的搭配讀物。每道經典 LeetCode 題目我們都給出
> *實際的解答程式碼*，標上 `time`／`space` 複雜度，並解釋這個複雜度**為什麼**成立 —
> 再加上它所屬的**模式**與類似題目。
>
> **相關文件（不要重複內容）：**
> - [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — 參考表格 + 數學直覺（等比／等差級數、主定理）
> - [`complexity_drills.md`](./complexity_drills.md) — 自我測驗：從一段程式碼推導複雜度
> - [`lc_pattern.md`](./lc_pattern.md) — 模式 → 題目對照

---

## 總覽

### 怎麼讀複雜度（三個問題） ⭐⭐⭐⭐

```text
1. TIME  : how many primitive ops as input n grows?
           -> count loops, recursion calls, work-per-call
2. SPACE : how much EXTRA memory (excludes the input itself)?
           -> data structures you allocate + recursion stack depth
3. CAN I DO BETTER?  (interviewers always ask)
           -> sort first? hashmap? two pointers? in-place?
```

### 「n 對應到多少複雜度才合理」的量尺 ⭐⭐⭐⭐⭐

```text
n ≤ 10        → O(n!) / O(2^n)     backtracking, permutations
n ≤ 20        → O(2^n)             bitmask DP, subsets
n ≤ 100       → O(n³)              Floyd-Warshall, 3-nested DP
n ≤ 1,000     → O(n²)              nested loops, naive DP
n ≤ 100,000   → O(n log n)         sort, heap, balanced BST
n ≤ 1,000,000 → O(n)               single/two pass, sliding window, hashmap
n ≥ 10^9      → O(log n) / O(√n)   binary search, math
```
> 規則：一台機器每秒約做 10^8 次簡單運算。如果 `n × (每次運算的工作量) > 10^8`，就準備 TLE。

### 面試官最愛的兩個複雜度陷阱 ⭐⭐⭐⭐⭐

| 陷阱 | 錯誤答案 | 正確答案 | 原因 |
|------|-------|-------|-----|
| 建堆積 | O(n log n) | **O(n)** | `heapify` = 等比級數 ∑（見 [cheatsheet 3-1](./complexity_cheatsheet.md)） |
| 遞迴的堆疊空間 | 「O(n) 個節點」 | **O(h)** = 樹高 | DFS 同時只握有一條路徑，不是所有節點 |
| `j` 從 `i` 開始 | O(n) | **O(n²)** | n+(n-1)+...+1 = n(n+1)/2 |
| 在迴圈裡切片 `s[1:]` | O(n) | **O(n²)** | 每次切片都複製 O(n) 個字元 |

---

## 0) 速查表 — 經典 LC 一覽 ⭐⭐⭐⭐⭐

| # | 題目 | 模式 | 時間 | 空間 |
|---|---------|---------|------|-------|
| 1 | Two Sum | 雜湊表 | O(n) | O(n) |
| 20 | Valid Parentheses | 堆疊 | O(n) | O(n) |
| 21 | Merge Two Sorted Lists | 雙指標（鏈結串列） | O(n+m) | O(1) |
| 3 | Longest Substring w/o Repeat | 滑動視窗 | O(n) | O(min(n,Σ)) |
| 76 | Minimum Window Substring | 滑動視窗 | O(n+m) | O(Σ) |
| 704 | Binary Search | 二分搜尋 | O(log n) | O(1) |
| 33 | Search Rotated Sorted Array | 二分搜尋 | O(log n) | O(1) |
| 875 | Koko Eating Bananas | 對答案二分搜尋 | O(n log m) | O(1) |
| 56 | Merge Intervals | 排序 + 掃描 | O(n log n) | O(n) |
| 215 | Kth Largest Element | 堆積 / QuickSelect | O(n log k) / 平均 O(n) | O(k) / O(1) |
| 347 | Top K Frequent | 堆積 / 桶 | O(n log k) / O(n) | O(n) |
| 23 | Merge K Sorted Lists | 堆積 | O(N log k) | O(k) |
| 104 | Max Depth Binary Tree | DFS | O(n) | O(h) |
| 102 | Level Order Traversal | BFS | O(n) | O(w) |
| 200 | Number of Islands | 網格上的 DFS/BFS | O(m·n) | O(m·n) |
| 207 | Course Schedule | 拓撲排序 | O(V+E) | O(V+E) |
| 70 | Climbing Stairs | 一維 DP | O(n) | O(1) |
| 322 | Coin Change | 無限背包 DP | O(n·amount) | O(amount) |
| 300 | LIS | DP / patience sort | O(n²) / O(n log n) | O(n) |
| 78 | Subsets | 回溯 | O(n·2^n) | O(n) |
| 46 | Permutations | 回溯 | O(n·n!) | O(n) |
| 42 | Trapping Rain Water | 雙指標 | O(n) | O(1) |

---

## 1) 雜湊表 — 用空間換 O(1) 查詢

### LC 1 — Two Sum

**模式：** 記下「我看過什麼 → 它在哪」。把 O(n²) 的兩兩搜尋壓成 O(n)。

```python
# python — LC 1
# time = O(n)   : single pass over nums
# space = O(n)  : hashmap can hold up to n entries
class Solution:
    def twoSum(self, nums, target):
        seen = {}                       # value -> index
        for i, x in enumerate(nums):
            if target - x in seen:      # O(1) average lookup
                return [seen[target - x], i]
            seen[x] = i
```

**為什麼是 O(n) 時間／O(n) 空間：**
- 一個迴圈跑 n 次；每次做的都是 O(1) 的雜湊表操作 → **O(n)**。
- `seen` 最多長到 n 個 key → **O(n)** 額外記憶體。

**為什麼不是 O(n²)？** 暴力法會檢查每一對（`n(n+1)/2` 次比較）。雜湊表「記住」了互補值，
所以每個元素只被檢視一次。

> **類似題：** LC 49 Group Anagrams（O(n·k)）、LC 128 Longest Consecutive Sequence（O(n)）、LC 560 Subarray Sum = K。

---

## 2) 堆疊 — 用 LIFO 順序做配對／回復

### LC 20 — Valid Parentheses

```python
# python — LC 20
# time = O(n)   : each char pushed/popped at most once
# space = O(n)  : worst case all opening brackets "((((("
class Solution:
    def isValid(self, s):
        pair = {')': '(', ']': '[', '}': '{'}
        stack = []
        for c in s:
            if c in pair:
                if not stack or stack.pop() != pair[c]:
                    return False
            else:
                stack.append(c)
        return not stack
```

**為什麼是 O(n)/O(n)：** 單趟掃描，push/pop 都是 O(1)。堆疊最多可以裝 n 個沒配到對的左括號。

### LC 84 — Largest Rectangle in Histogram（單調堆疊）

```python
# python — LC 84
# time = O(n)   : each bar pushed once + popped once  (amortized O(1) per bar)
# space = O(n)  : the monotonic stack
class Solution:
    def largestRectangleArea(self, heights):
        stack = []          # indices, increasing heights
        heights.append(0)   # sentinel flushes the stack at the end
        best = 0
        for i, h in enumerate(heights):
            while stack and heights[stack[-1]] > h:
                top = stack.pop()
                left = stack[-1] if stack else -1
                width = i - left - 1
                best = max(best, heights[top] * width)
            stack.append(i)
        return best
```

**明明有內層 `while`，為什麼還是 O(n)？** 經典的攤還分析：每個索引**剛好被推入一次**、
**最多被彈出一次**，所以整趟執行下來內層迴圈的總工作量 ≤ 2n → **O(n)**，不是 O(n²)。

> **類似題：** LC 42 Trapping Rain Water、LC 496/503 Next Greater Element、LC 739 Daily Temperatures。見 [`monotonic_stack.md`](./monotonic_stack.md)。

---

## 3) 滑動視窗 — 在子字串／子陣列上做到 O(n)

### LC 3 — Longest Substring Without Repeating Characters

```python
# python — LC 3
# time = O(n)        : left & right pointers each move forward ≤ n times
# space = O(min(n,Σ)): the window set, bounded by alphabet size Σ
class Solution:
    def lengthOfLongestSubstring(self, s):
        seen = set()
        left = best = 0
        for right, c in enumerate(s):
            while c in seen:        # shrink until window valid
                seen.remove(s[left])
                left += 1
            seen.add(c)
            best = max(best, right - left + 1)
        return best
```

**為什麼是 O(n) 而不是 O(n²)？** 那個 `while` 看起來像巢狀迴圈，但 `left` 只會**遞增**，
而且永遠不會超過 `right`。整趟跑下來，`left` 總共只前進 ≤ n 步。所以兩個指標合起來的工作量是
O(n) → **O(n)**。

**為什麼空間是 O(min(n, Σ))？** 集合裡永遠不會超過相異字元的個數，而這個數同時被字串長度 n
和字母表大小 Σ（例如 26 或 128）夾住。

> **類似題：** LC 76 Min Window Substring（O(n+m)）、LC 424 Longest Repeating Char Replacement、LC 209 Min Size Subarray Sum、LC 438 Find All Anagrams。見 [`sliding_window.md`](./sliding_window.md)。

---

## 4) 二分搜尋 — 每次砍掉一半搜尋空間 → O(log n)

### LC 704 — Binary Search

```python
# python — LC 704
# time = O(log n) : search space halves each iteration
# space = O(1)    : two pointers, iterative
class Solution:
    def search(self, nums, target):
        lo, hi = 0, len(nums) - 1
        while lo <= hi:
            mid = (lo + hi) // 2
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                lo = mid + 1
            else:
                hi = mid - 1
        return -1
```

**為什麼是 O(log n)？** n 要對半砍幾次才會到 1？`log₂(n)` 次。

### LC 875 — Koko Eating Bananas（對**答案**二分搜尋） ⭐⭐⭐⭐

```python
# python — LC 875
# time = O(n log m) : binary search over speeds [1..max_pile] (log m), each check scans n piles
# space = O(1)
import math
class Solution:
    def minEatingSpeed(self, piles, h):
        def hours(speed):
            return sum(math.ceil(p / speed) for p in piles)   # O(n)
        lo, hi = 1, max(piles)
        while lo < hi:
            mid = (lo + hi) // 2
            if hours(mid) <= h:     # feasible -> try slower
                hi = mid
            else:
                lo = mid + 1
        return lo
```

**為什麼是 O(n log m)？** 我們是在*數值範圍* `m = max(piles)` 上做二分搜尋（→ `log m` 次迭代），
而每次可行性檢查是一趟 O(n) 掃描。答案空間具單調性（「速度 s 可行，s+1 就一定可行」），
這正是二分搜尋能套用的前提。

> **類似題：** LC 33 Search in Rotated Array（O(log n)）、LC 153 Find Min in Rotated Array、LC 410 Split Array Largest Sum、LC 1011 Capacity to Ship Packages。見 [`binary_search.md`](./binary_search.md)。

---

## 5) 排序 + 掃描 — 先付一次 O(n log n)，之後都是 O(n)

### LC 56 — Merge Intervals

```python
# python — LC 56
# time = O(n log n) : sorting dominates; the merge sweep is O(n)
# space = O(n)      : output list (O(log n)..O(n) for the sort itself)
class Solution:
    def merge(self, intervals):
        intervals.sort(key=lambda x: x[0])      # O(n log n)
        res = []
        for s, e in intervals:                  # O(n) sweep
            if res and s <= res[-1][1]:
                res[-1][1] = max(res[-1][1], e)  # overlap -> extend
            else:
                res.append([s, e])
        return res
```

**為什麼是 O(n log n)？** 比較排序是瓶頸；排完之後那趟線性掃描相對來說「不用錢」。
**心得：** 看到「區間」、「重疊」或「排程」，先排序通常就能換到一趟線性掃描。

> **類似題：** LC 57 Insert Interval、LC 252/253 Meeting Rooms（II 會用到堆積）、LC 435 Non-overlapping Intervals、LC 1288 Remove Covered Intervals。見 [`intervals.md`](./intervals.md)。

---

## 6) 堆積（優先佇列） — 用低成本維持前 k 名

### LC 215 — Kth Largest Element

```python
# python — LC 215   (min-heap of size k approach)
# time = O(n log k) : n pushes/pops on a heap capped at size k
# space = O(k)      : heap holds at most k elements
import heapq
class Solution:
    def findKthLargest(self, nums, k):
        heap = []
        for x in nums:
            heapq.heappush(heap, x)
            if len(heap) > k:
                heapq.heappop(heap)     # evict smallest -> heap keeps top-k
        return heap[0]                  # smallest of the top-k = kth largest
```

**為什麼是 O(n log k) 而不是 O(n log n)？** 堆積的大小被限制在 **k**，所以每次 push/pop 都是
`log k`（不是 `log n`）。掃過 n 個元素 → **O(n log k)**。空間是 **O(k)**，因為我們從不存超過
k 個元素。

**替代方案 — QuickSelect：平均 O(n)、額外空間 O(1)**（但最差 O(n²)）。如果你還需要*串流式*的
前 k 名，就用堆積。

> **類似題：** LC 347 Top K Frequent、LC 23 Merge K Sorted Lists（O(N log k)）、LC 295 Find Median from Data Stream（雙堆積）、LC 973 K Closest Points。見 [`heap.md`](./heap.md) / [`priority_queue.md`](./priority_queue.md)。

---

## 7) 樹 — 時間看節點數，空間看**樹高**（最經典的坑） ⭐⭐⭐⭐

### LC 104 — Maximum Depth of Binary Tree

```python
# python — LC 104
# time = O(n)  : visit every node once
# space = O(h) : recursion stack = tree height  (h = log n balanced, h = n skewed)
class Solution:
    def maxDepth(self, root):
        if not root:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))
```

**為什麼空間是 O(h) 而不是 O(n)？** ⭐ DFS 遞迴**一次只往下走一條路徑**。任何一個瞬間，
堆疊裡只有目前這條 root→leaf 路徑上的框架 = 樹高 `h`。它**不會**同時裝下全部 n 個節點。
- 平衡樹 → `h = log n` → 空間 **O(log n)**
- 歪斜樹（長得像鏈結串列） → `h = n` → 空間 **O(n)**

### LC 102 — Binary Tree Level Order Traversal（BFS）

```python
# python — LC 102
# time = O(n)  : each node enqueued/dequeued once
# space = O(w) : queue holds the widest level; w ≈ n/2 for a full tree -> O(n)
from collections import deque
class Solution:
    def levelOrder(self, root):
        if not root:
            return []
        res, q = [], deque([root])
        while q:
            level = []
            for _ in range(len(q)):          # snapshot current level size
                node = q.popleft()
                level.append(node.val)
                if node.left:  q.append(node.left)
                if node.right: q.append(node.right)
            res.append(level)
        return res
```

**DFS 與 BFS 的空間差別：** DFS = O(樹高)，BFS = O(樹寬)。平衡樹的最後一層就有大約 n/2 個節點，
所以 BFS 是 O(n) 而 DFS 只有 O(log n)。**依樹的形狀來選走訪方式。**

> **類似題：** LC 226 Invert Tree、LC 236 LCA（O(n)/O(h)）、LC 124 Max Path Sum（O(n)/O(h)）、LC 297 Serialize/Deserialize。見 [`tree.md`](./tree.md) / [`binary_tree.md`](./binary_tree.md)。

---

## 8) 圖 — O(V + E)

### LC 200 — Number of Islands（在網格上做 DFS）

```python
# python — LC 200
# time = O(m·n)  : every cell visited at most once
# space = O(m·n) : recursion stack worst case (grid is one big island / snake)
class Solution:
    def numIslands(self, grid):
        if not grid:
            return 0
        m, n = len(grid), len(grid[0])
        def sink(r, c):
            if r < 0 or c < 0 or r >= m or c >= n or grid[r][c] != '1':
                return
            grid[r][c] = '0'                 # mark visited in-place
            sink(r+1, c); sink(r-1, c); sink(r, c+1); sink(r, c-1)
        count = 0
        for r in range(m):
            for c in range(n):
                if grid[r][c] == '1':
                    count += 1
                    sink(r, c)
        return count
```

**為什麼時間是 O(m·n)？** 一個有 `m·n` 個格子的網格，就是一張 `V = m·n` 個頂點、
`E ≈ 4·m·n` 條邊的圖。DFS/BFS 是 O(V+E) = **O(m·n)**。每個格子只會被沉掉（走訪）一次。

**為什麼空間是 O(m·n)？** 最差情況（一整座蛇行的島）遞迴深度可以到格子總數。
改成用佇列的 BFS 也是類似的上界，或者改用併查集。

### LC 207 — Course Schedule（拓撲排序／偵測環）

```python
# python — LC 207  (Kahn's BFS)
# time = O(V + E)  : build graph O(E), process each node + edge once
# space = O(V + E) : adjacency list + indegree array + queue
from collections import deque, defaultdict
class Solution:
    def canFinish(self, numCourses, prerequisites):
        graph = defaultdict(list)
        indeg = [0] * numCourses
        for nxt, pre in prerequisites:
            graph[pre].append(nxt)
            indeg[nxt] += 1
        q = deque(i for i in range(numCourses) if indeg[i] == 0)
        done = 0
        while q:
            node = q.popleft()
            done += 1
            for nb in graph[node]:
                indeg[nb] -= 1
                if indeg[nb] == 0:
                    q.append(nb)
        return done == numCourses      # all taken => no cycle
```

**為什麼是 O(V+E)？** 建圖時每條邊碰一次（O(E)）。BFS 每個節點只出隊一次（O(V)），
每條邊只鬆弛一次（O(E)）。總共 **O(V+E)**。

> **類似題：** LC 133 Clone Graph、LC 210 Course Schedule II、LC 743 Network Delay（Dijkstra O((V+E)log V)）、LC 684 Redundant Connection（併查集約 O(α)）。見 [`graph.md`](./graph.md) / [`topology_sorting.md`](./topology_sorting.md)。

---

## 9) 動態規劃 — 定義狀態，然後算「狀態數 × 每個狀態的工作量」

### LC 70 — Climbing Stairs（一維 DP，空間已最佳化）

```python
# python — LC 70
# time = O(n)  : one pass building dp
# space = O(1) : only keep the last two values (rolling variables)
class Solution:
    def climbStairs(self, n):
        a, b = 1, 1            # ways(0), ways(1)
        for _ in range(n - 1):
            a, b = b, a + b    # Fibonacci recurrence
        return b
```

**為什麼是 O(n)/O(1)？** 總共 n 個子問題，每個都 O(1) 就能算完 → O(n) 時間。遞迴式只依賴
**前兩個**狀態，所以我們把整條 O(n) 陣列縮成 **O(1)** 的滾動變數。
> **空間最佳化的規則：** 如果 `dp[i]` 只讀 `dp[i-1]`、`dp[i-2]`，就用變數，別用陣列。

### LC 322 — Coin Change（無限背包）

```python
# python — LC 322
# time = O(n · amount) : for each of `amount` sub-targets, try all n coins
# space = O(amount)    : 1D dp over target values
class Solution:
    def coinChange(self, coins, amount):
        INF = amount + 1
        dp = [0] + [INF] * amount         # dp[t] = min coins to make t
        for t in range(1, amount + 1):
            for c in coins:
                if c <= t:
                    dp[t] = min(dp[t], dp[t - c] + 1)
        return dp[amount] if dp[amount] != INF else -1
```

**為什麼是 O(n·amount)？** 狀態是「還要湊出多少金額」（`amount+1` 種取值），每個狀態都要試
全部 `n` 種硬幣 → **n × amount** 次轉移。這是*偽多項式*時間 — 它取決於 `amount` 這個
數值大小，而不只是輸入規模。

### LC 300 — Longest Increasing Subsequence（兩種複雜度等級）

```python
# python — LC 300  (V1: classic DP)
# time = O(n²)  : for each i, scan all j < i
# space = O(n)
class Solution:
    def lengthOfLIS(self, nums):
        dp = [1] * len(nums)
        for i in range(len(nums)):
            for j in range(i):
                if nums[j] < nums[i]:
                    dp[i] = max(dp[i], dp[j] + 1)
        return max(dp)
```

```python
# python — LC 300  (V2: patience sorting + binary search)
# time = O(n log n) : n elements, each binary-searched into `tails`
# space = O(n)
import bisect
class Solution:
    def lengthOfLIS(self, nums):
        tails = []                       # tails[k] = smallest tail of an LIS of length k+1
        for x in nums:
            i = bisect.bisect_left(tails, x)
            if i == len(tails):
                tails.append(x)
            else:
                tails[i] = x             # replace -> keeps tails minimal
        return len(tails)
```

**加速的關鍵是什麼？** V1 的內層掃描是 O(n) → 整體 O(n²)。V2 把那趟掃描換成對已排序的 `tails`
陣列做 O(log n) 二分搜尋 → **O(n log n)**。答案一樣，漸進更好 — 這是教科書等級的
「你能做得更好嗎？」升級。

> **類似題：** LC 1143 LCS（O(m·n)）、LC 72 Edit Distance（O(m·n)）、LC 53 Maximum Subarray / Kadane（O(n)）、LC 5 Longest Palindromic Substring。見 [`dp.md`](./dp.md) / [`dp_pattern.md`](./dp_pattern.md)。

---

## 10) 回溯 — 由輸出量決定：O(答案數 × 每個答案的成本)

### LC 78 — Subsets

```python
# python — LC 78
# time = O(n · 2^n)  : 2^n subsets, each costs O(n) to copy into the result
# space = O(n)       : recursion depth + current path (output not counted as aux)
class Solution:
    def subsets(self, nums):
        res = []
        def dfs(start, path):
            res.append(path[:])               # O(n) copy
            for i in range(start, len(nums)):
                path.append(nums[i])
                dfs(i + 1, path)
                path.pop()                    # undo (backtrack)
        dfs(0, [])
        return res
```

**為什麼是 O(n·2^n)？** 每個元素不是選就是不選 → **2^n** 個子集。把每個子集複製進結果是 O(n)
→ **n·2^n**。遞迴堆疊深度只有 **O(n)**（路徑長度）。

### LC 46 — Permutations

```python
# python — LC 46
# time = O(n · n!) : n! permutations, each O(n) to build/copy
# space = O(n)     : recursion depth + used set
class Solution:
    def permute(self, nums):
        res = []
        def dfs(path, remaining):
            if not remaining:
                res.append(path[:])
                return
            for i in range(len(remaining)):
                dfs(path + [remaining[i]], remaining[:i] + remaining[i+1:])
        dfs([], nums)
        return res
```

**為什麼是 O(n·n!)？** 一共有 `n!` 種排列；產生每一種要 O(n)。回溯的複雜度幾乎總是
**（解的個數）×（每個解的工作量）** — 去數遞迴樹的葉子就對了。

> **類似題：** LC 77 Combinations、LC 39/40 Combination Sum、LC 51 N-Queens（O(n!)）、LC 22 Generate Parentheses（結果數是卡塔蘭數）。見 [`backtrack.md`](./backtrack.md)。

---

## 11) 雙指標 — O(n) 時間、O(1) 空間的甜蜜點

### LC 42 — Trapping Rain Water

```python
# python — LC 42
# time = O(n)  : single pass, two converging pointers
# space = O(1) : just a few scalars (vs O(n) for the prefix/suffix-array version)
class Solution:
    def trap(self, height):
        l, r = 0, len(height) - 1
        left_max = right_max = water = 0
        while l < r:
            if height[l] < height[r]:
                left_max = max(left_max, height[l])
                water += left_max - height[l]
                l += 1
            else:
                right_max = max(right_max, height[r])
                water += right_max - height[r]
                r -= 1
        return water
```

**為什麼空間是 O(1)？** 樸素的 DP 要先算好 `leftMax[]` 和 `rightMax[]` 兩個陣列 = O(n) 空間。
雙指標的洞見 —「比較矮的那一側決定了水位」— 讓我們只需要追蹤兩個滾動最大值，
空間塌縮成 **O(1)**，同時保持 O(n) 時間。

> **類似題：** LC 11 Container With Most Water、LC 15 3Sum（O(n²)）、LC 167 Two Sum II、LC 125 Valid Palindrome、LC 26/27 原地移除重複元素。見 [`2_pointers.md`](./2_pointers.md)。

---

## 12) 速查 — 「面試時怎麼論證複雜度」

```text
SINGLE LOOP over n           → O(n)
TWO NESTED LOOPS (full)      → O(n²)
LOOP that halves/doubles     → O(log n)
LOOP + inner binary search   → O(n log n)
SORT anything                → O(n log n)  (then often an O(n) sweep)
RECURSION                    → (#calls) × (work per call); space = max depth
BACKTRACKING                 → (#solutions) × (cost each); space = recursion depth
HEAP of size k, n ops        → O(n log k)
GRAPH traversal              → O(V + E)
GRID traversal               → O(rows · cols)
DP                           → (#states) × (transition cost)
AMORTIZED (each item ≤1 push/pop) → O(n) even with an inner while-loop
```

### 空間複雜度檢查清單

```text
□ Did I count the recursion stack?      (DFS = O(height), not O(1))
□ Is the output counted or excluded?    (usually excluded as "aux space")
□ Can a 2D dp collapse to 1D rolling?   (dp[i] depends only on dp[i-1]?)
□ Can two pointers replace an array?    (prefix-array → running variable)
□ In-place possible?                    (mark grid, reverse array, swap)
```

### 最後的合理性檢查

```text
1. Two O(n) passes = still O(n)        (constants drop)
2. heapify = O(n);  n× heappush = O(n log n)   ← NOT the same!
3. Inner `while` ≠ automatically O(n²) — check amortized (sliding window, monotonic stack)
4. Slicing/concatenating inside a loop hides an O(n) cost → can make it O(n²)
5. HashMap is O(1) AVERAGE, O(n) worst (collisions) — say this out loud
6. Recursion space = stack depth = tree/recursion height
```

## 參考資料
- [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — 參考表格 + Big-O 數學（級數、主定理）
- [`complexity_drills.md`](./complexity_drills.md) — 從程式碼片段推導複雜度（測驗形式）
- [`lc_pattern.md`](./lc_pattern.md) — 模式 → LC 對照
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
