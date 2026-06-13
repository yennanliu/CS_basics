# Time & Space Complexity — Classic LC Code Walkthroughs

> **What this doc is:** a code-first companion. For each classic LeetCode problem we show the
> *actual solution code*, annotate it with `time`/`space` complexity, and explain **WHY** that
> complexity holds — plus the **pattern** it belongs to and similar LCs.
>
> **Related docs (do not duplicate):**
> - [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — reference tables + math intuitions (geometric/arithmetic series, Master Theorem)
> - [`complexity_drills.md`](./complexity_drills.md) — quiz yourself: derive complexity from a snippet
> - [`lc_pattern.md`](./lc_pattern.md) — pattern → problem mapping

---

## Overview

### How to Read Complexity (the 3 questions)

```
1. TIME  : how many primitive ops as input n grows?
           -> count loops, recursion calls, work-per-call
2. SPACE : how much EXTRA memory (excludes the input itself)?
           -> data structures you allocate + recursion stack depth
3. CAN I DO BETTER?  (interviewers always ask)
           -> sort first? hashmap? two pointers? in-place?
```

### The "n vs reasonable complexity" ruler ⭐⭐⭐⭐⭐

```
n ≤ 10        → O(n!) / O(2^n)     backtracking, permutations
n ≤ 20        → O(2^n)             bitmask DP, subsets
n ≤ 100       → O(n³)              Floyd-Warshall, 3-nested DP
n ≤ 1,000     → O(n²)              nested loops, naive DP
n ≤ 100,000   → O(n log n)         sort, heap, balanced BST
n ≤ 1,000,000 → O(n)               single/two pass, sliding window, hashmap
n ≥ 10^9      → O(log n) / O(√n)   binary search, math
```
> Rule: a machine does ~10^8 simple ops/sec. If `n × (per-op work) > 10^8`, expect TLE.

### Two complexity traps interviewers love

| Trap | Wrong | Right | Why |
|------|-------|-------|-----|
| Build heap | O(n log n) | **O(n)** | `heapify` = geometric series ∑ (see [cheatsheet 3-1](./complexity_cheatsheet.md)) |
| Recursion stack space | "O(n) nodes" | **O(h)** = tree height | DFS holds ONE path, not all nodes |
| `j` starts at `i` | O(n) | **O(n²)** | n+(n-1)+...+1 = n(n+1)/2 |
| Slicing `s[1:]` in a loop | O(n) | **O(n²)** | each slice copies O(n) chars |

---

## 0) Cheat Table — Classic LCs at a Glance

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 1 | Two Sum | HashMap | O(n) | O(n) |
| 20 | Valid Parentheses | Stack | O(n) | O(n) |
| 21 | Merge Two Sorted Lists | Two pointers (list) | O(n+m) | O(1) |
| 3 | Longest Substring w/o Repeat | Sliding window | O(n) | O(min(n,Σ)) |
| 76 | Minimum Window Substring | Sliding window | O(n+m) | O(Σ) |
| 704 | Binary Search | Binary search | O(log n) | O(1) |
| 33 | Search Rotated Sorted Array | Binary search | O(log n) | O(1) |
| 875 | Koko Eating Bananas | Binary search on answer | O(n log m) | O(1) |
| 56 | Merge Intervals | Sort + sweep | O(n log n) | O(n) |
| 215 | Kth Largest Element | Heap / QuickSelect | O(n log k) / O(n) avg | O(k) / O(1) |
| 347 | Top K Frequent | Heap / bucket | O(n log k) / O(n) | O(n) |
| 23 | Merge K Sorted Lists | Heap | O(N log k) | O(k) |
| 104 | Max Depth Binary Tree | DFS | O(n) | O(h) |
| 102 | Level Order Traversal | BFS | O(n) | O(w) |
| 200 | Number of Islands | DFS/BFS grid | O(m·n) | O(m·n) |
| 207 | Course Schedule | Topological sort | O(V+E) | O(V+E) |
| 70 | Climbing Stairs | 1D DP | O(n) | O(1) |
| 322 | Coin Change | Unbounded knapsack DP | O(n·amount) | O(amount) |
| 300 | LIS | DP / patience sort | O(n²) / O(n log n) | O(n) |
| 78 | Subsets | Backtracking | O(n·2^n) | O(n) |
| 46 | Permutations | Backtracking | O(n·n!) | O(n) |
| 42 | Trapping Rain Water | Two pointers | O(n) | O(1) |

---

## 1) HashMap — O(1) lookup trades space for time

### LC 1 — Two Sum

**Pattern:** store "what I've seen → where". Turn an O(n²) pair search into O(n).

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

**Why O(n) time / O(n) space:**
- One loop = n iterations; each does O(1) hashmap ops → **O(n)**.
- `seen` grows to at most n keys → **O(n)** extra memory.

**Why not O(n²)?** The brute force checks every pair (`n(n+1)/2` comparisons). The hashmap
"remembers" complements so each element is examined once.

> **Similar:** LC 49 Group Anagrams (O(n·k)), LC 128 Longest Consecutive Sequence (O(n)), LC 560 Subarray Sum = K.

---

## 2) Stack — match / undo in LIFO order

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

**Why O(n)/O(n):** single pass, push/pop are O(1). Stack can hold up to n unmatched openers.

### LC 84 — Largest Rectangle in Histogram (monotonic stack)

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

**Why O(n) despite the inner `while`?** Classic amortized argument: every index is pushed
**exactly once** and popped **at most once**, so total inner-loop work across the whole run is
≤ 2n → **O(n)**, not O(n²).

> **Similar:** LC 42 Trapping Rain Water, LC 496/503 Next Greater Element, LC 739 Daily Temperatures. See [`monotonic_stack.md`](./monotonic_stack.md).

---

## 3) Sliding Window — O(n) over substrings/subarrays

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

**Why O(n), not O(n²)?** The `while` looks nested, but `left` only ever **increases** and never
exceeds `right`. Across the whole run, `left` advances a total of ≤ n steps. So both pointers do
O(n) combined work → **O(n)**.

**Why O(min(n, Σ)) space?** The set never holds more than the number of distinct chars, which is
capped by both the string length n and the alphabet size Σ (e.g. 26 or 128).

> **Similar:** LC 76 Min Window Substring (O(n+m)), LC 424 Longest Repeating Char Replacement, LC 209 Min Size Subarray Sum, LC 438 Find All Anagrams. See [`sliding_window.md`](./sliding_window.md).

---

## 4) Binary Search — halve the search space → O(log n)

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

**Why O(log n)?** How many times can you halve n until reaching 1? `log₂(n)` times.

### LC 875 — Koko Eating Bananas (binary search on the ANSWER)

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

**Why O(n log m)?** We binary search over the *value range* `m = max(piles)` (→ `log m` iterations),
and each feasibility check is an O(n) scan. The answer space is monotonic ("if speed s works, s+1 works"),
which is what makes binary search applicable.

> **Similar:** LC 33 Search in Rotated Array (O(log n)), LC 153 Find Min in Rotated Array, LC 410 Split Array Largest Sum, LC 1011 Capacity to Ship Packages. See [`binary_search.md`](./binary_search.md).

---

## 5) Sort + Sweep — pay O(n log n) once, then O(n)

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

**Why O(n log n)?** Comparison sort is the bottleneck; the linear sweep after it is "free" by
comparison. **Lesson:** when you see "intervals", "overlap", or "schedule", sorting first usually
unlocks a linear pass.

> **Similar:** LC 57 Insert Interval, LC 252/253 Meeting Rooms (II uses a heap), LC 435 Non-overlapping Intervals, LC 1288 Remove Covered Intervals. See [`intervals.md`](./intervals.md).

---

## 6) Heap (Priority Queue) — keep the top-k cheaply

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

**Why O(n log k), not O(n log n)?** The heap is bounded at size **k**, so each push/pop is
`log k` (not `log n`). Over n elements → **O(n log k)**. Space is **O(k)** because we never store
more than k items.

**Alternative — QuickSelect: O(n) average, O(1) extra space** (but O(n²) worst case). Use the heap
when you also want a *streaming* top-k.

> **Similar:** LC 347 Top K Frequent, LC 23 Merge K Sorted Lists (O(N log k)), LC 295 Find Median from Data Stream (two heaps), LC 973 K Closest Points. See [`heap.md`](./heap.md) / [`priority_queue.md`](./priority_queue.md).

---

## 7) Trees — time = nodes, space = HEIGHT (the classic gotcha)

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

**Why space = O(h), NOT O(n)?** ⭐ DFS recursion descends **one path at a time**. At any instant the
stack holds only the frames along the current root→leaf path = the height `h`. It does **not** hold
all n nodes simultaneously.
- Balanced tree → `h = log n` → **O(log n)** space
- Skewed tree (linked-list shape) → `h = n` → **O(n)** space

### LC 102 — Binary Tree Level Order Traversal (BFS)

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

**DFS vs BFS space:** DFS = O(height), BFS = O(width). For a balanced tree the last level holds
~n/2 nodes, so BFS is O(n) while DFS is O(log n). **Choose traversal by tree shape.**

> **Similar:** LC 226 Invert Tree, LC 236 LCA (O(n)/O(h)), LC 124 Max Path Sum (O(n)/O(h)), LC 297 Serialize/Deserialize. See [`tree.md`](./tree.md) / [`binary_tree.md`](./binary_tree.md).

---

## 8) Graphs — O(V + E)

### LC 200 — Number of Islands (DFS on a grid)

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

**Why O(m·n) time?** A grid with `m·n` cells is a graph with `V = m·n` vertices and `E ≈ 4·m·n`
edges. DFS/BFS is O(V+E) = **O(m·n)**. Each cell is sunk (visited) once.

**Why O(m·n) space?** The recursion can go as deep as the number of cells in the worst case (a single
snaking island). Convert to BFS with a queue to bound auxiliary structure similarly, or use Union-Find.

### LC 207 — Course Schedule (topological sort / cycle detection)

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

**Why O(V+E)?** Building the graph touches every edge once (O(E)). The BFS dequeues each node once
(O(V)) and relaxes each edge once (O(E)). Total **O(V+E)**.

> **Similar:** LC 133 Clone Graph, LC 210 Course Schedule II, LC 743 Network Delay (Dijkstra O((V+E)log V)), LC 684 Redundant Connection (Union-Find ~O(α)). See [`graph.md`](./graph.md) / [`topology_sorting.md`](./topology_sorting.md).

---

## 9) Dynamic Programming — define state, count states × work

### LC 70 — Climbing Stairs (1D DP, space-optimized)

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

**Why O(n)/O(1)?** There are n subproblems, each O(1) to compute → O(n) time. The recurrence only
depends on the **previous two** states, so we drop the full O(n) array down to **O(1)** rolling vars.
> **Space-optimization rule:** if `dp[i]` only reads `dp[i-1]`, `dp[i-2]`, keep variables, not arrays.

### LC 322 — Coin Change (unbounded knapsack)

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

**Why O(n·amount)?** The state is "amount left to make" (`amount+1` values), and each state tries
all `n` coins → **n × amount** transitions. This is *pseudo-polynomial* — it depends on the numeric
value `amount`, not just the input size.

### LC 300 — Longest Increasing Subsequence (two complexity tiers)

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

**Why the speedup?** V1's inner scan is O(n) → O(n²). V2 replaces that scan with an O(log n) binary
search into a sorted `tails` array → **O(n log n)**. Same answer, better asymptotics — a textbook
"can you do better?" upgrade.

> **Similar:** LC 1143 LCS (O(m·n)), LC 72 Edit Distance (O(m·n)), LC 53 Maximum Subarray / Kadane (O(n)), LC 5 Longest Palindromic Substring. See [`dp.md`](./dp.md) / [`dp_pattern.md`](./dp_pattern.md).

---

## 10) Backtracking — output-bounded: O(answers × cost per answer)

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

**Why O(n·2^n)?** Each element is either in or out → **2^n** subsets. Copying each subset into the
result is O(n) → **n·2^n**. The recursion stack depth is only **O(n)** (the path length).

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

**Why O(n·n!)?** There are `n!` orderings; producing each costs O(n). Backtracking complexity is
almost always **(number of solutions) × (work per solution)** — count the leaves of the recursion tree.

> **Similar:** LC 77 Combinations, LC 39/40 Combination Sum, LC 51 N-Queens (O(n!)), LC 22 Generate Parentheses (Catalan number of results). See [`backtrack.md`](./backtrack.md).

---

## 11) Two Pointers — O(n) time, O(1) space sweet spot

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

**Why O(1) space?** The naive DP precomputes `leftMax[]` and `rightMax[]` arrays = O(n) space. The
two-pointer insight — "the smaller side bounds the water" — lets us track just two running maxima,
collapsing space to **O(1)** while staying O(n) time.

> **Similar:** LC 11 Container With Most Water, LC 15 3Sum (O(n²)), LC 167 Two Sum II, LC 125 Valid Palindrome, LC 26/27 Remove Duplicates in place. See [`2_pointers.md`](./2_pointers.md).

---

## 12) Quick Reference — "How to argue complexity in an interview"

```
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

### Space-complexity checklist

```
□ Did I count the recursion stack?      (DFS = O(height), not O(1))
□ Is the output counted or excluded?    (usually excluded as "aux space")
□ Can a 2D dp collapse to 1D rolling?   (dp[i] depends only on dp[i-1]?)
□ Can two pointers replace an array?    (prefix-array → running variable)
□ In-place possible?                    (mark grid, reverse array, swap)
```

### Final sanity checks

```
1. Two O(n) passes = still O(n)        (constants drop)
2. heapify = O(n);  n× heappush = O(n log n)   ← NOT the same!
3. Inner `while` ≠ automatically O(n²) — check amortized (sliding window, monotonic stack)
4. Slicing/concatenating inside a loop hides an O(n) cost → can make it O(n²)
5. HashMap is O(1) AVERAGE, O(n) worst (collisions) — say this out loud
6. Recursion space = stack depth = tree/recursion height
```

## References
- [`complexity_cheatsheet.md`](./complexity_cheatsheet.md) — reference tables + Big-O math (series, Master Theorem)
- [`complexity_drills.md`](./complexity_drills.md) — derive complexity from snippets (quiz format)
- [`lc_pattern.md`](./lc_pattern.md) — pattern → LC mapping
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
