# Time & Space Complexity — Classic LC Code Walkthroughs

> **Scope** — **Walkthroughs** — how to argue the complexity of an actual LC solution: one worked classic per technique, then a by-topic review table of the frequently asked problems, each with the line that sets its bound and the follow-up an interviewer asks.
> **See also**: [complexity_cheatsheet.md](./complexity_cheatsheet.md) — the lookup tables; [complexity_drills.md](./complexity_drills.md) — self-test drills.

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

### How to Read Complexity (the 3 questions) ⭐⭐⭐⭐

```text
1. TIME  : how many primitive ops as input n grows?
           -> count loops, recursion calls, work-per-call
2. SPACE : how much EXTRA memory (excludes the input itself)?
           -> data structures you allocate + recursion stack depth
3. CAN I DO BETTER?  (interviewers always ask)
           -> sort first? hashmap? two pointers? in-place?
```

### The "n vs reasonable complexity" ruler ⭐⭐⭐⭐⭐

The ruler lives in one place: [complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide) — `n ≤ 20` invites O(2^n), `n ≤ 10^5` invites O(n log n), and ~10^8 simple ops per second is the budget.

### Two complexity traps interviewers love ⭐⭐⭐⭐⭐

| Trap | Wrong | Right | Why |
|------|-------|-------|-----|
| Build heap | O(n log n) | **O(n)** | `heapify` = geometric series ∑ (see [cheatsheet 3-1](./complexity_cheatsheet.md)) |
| Recursion stack space | "O(n) nodes" | **O(h)** = tree height | DFS holds ONE path, not all nodes |
| `j` starts at `i` | O(n) | **O(n²)** | n+(n-1)+...+1 = n(n+1)/2 |
| Slicing `s[1:]` in a loop | O(n) | **O(n²)** | each slice copies O(n) chars |

---

## 0) Cheat Table — Classic LCs at a Glance ⭐⭐⭐⭐⭐

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

### LC 875 — Koko Eating Bananas (binary search on the ANSWER) ⭐⭐⭐⭐

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

> **Similar:** LC 347 Top K Frequent, LC 23 Merge K Sorted Lists (O(N log k)), LC 295 Find Median from Data Stream (two heaps), LC 973 K Closest Points. See [`heap.md`](./heap.md).

---

## 7) Trees — time = nodes, space = HEIGHT (the classic gotcha) ⭐⭐⭐⭐

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

### O(V + E) vs O(V × E) — add, or multiply? ⭐⭐⭐⭐

**The rule:** **add** when every vertex and every edge is handled once *in total*; **multiply** when the
whole edge set (or the whole graph) is handled again *for each* vertex or round. It is the same rule for
time and for space. For time, count the steps. For space, count what is held in memory at the same time.

| You see | Time | Extra space (input not counted) | Example |
|---|---|---|---|
| One BFS / DFS over an adjacency list | **O(V + E)** | O(V + E) | LC 207, LC 399 |
| `V − 1` rounds, each relaxing all E edges | **O(V × E)** | O(V) | Bellman-Ford, LC 743 |
| A full traversal started from every vertex | **O(V × (V + E))** | depends what you keep | LC 1462, LC 2192 |
| Every vertex scans the raw edge list for its neighbours | **O(V × E)** | O(V) | a bug, not an algorithm |

#### Time — why one traversal is V + E, not V × E

```python
# python
# IDEA: build an adjacency list once, then BFS — every vertex is queued once, every list scanned once
# time = O(V + E) : each vertex is dequeued once; each adjacency list is scanned once
# space = O(V + E): the adjacency list this code builds, plus seen + queue (O(V))
from collections import defaultdict, deque

def bfs(edges, src):
    graph = defaultdict(list)
    for u, v in edges:                 # O(E) to build
        graph[u].append(v)
        graph[v].append(u)

    seen = {src}
    queue = deque([src])
    while queue:                       # runs once per vertex   → V
        node = queue.popleft()
        for nb in graph[node]:         # runs deg(node) times   → Σ deg = 2E in total
            if nb not in seen:
                seen.add(nb)
                queue.append(nb)
    return seen
```

**The trap:** "the outer loop runs V times and the inner loop can run up to E times, so V × E." The
inner loop's bound is right for *one* vertex and wrong for all of them together. Each vertex's list is
scanned exactly once, and the lengths of all the lists add up to the number of edges (the handshake sum):

```text
deg(v1) + deg(v2) + … + deg(vV) = 2E      (undirected; = E for a directed graph)

star graph: centre 0 joined to 1…9         V = 10, E = 9
  graph[0] has 9 neighbours, graph[1…9] have 1 each
  inner-loop steps, counted:   9 + 9·1 = 18 = 2E     ✓ O(V + E)
  the V × E estimate:          10 · 9  = 90          ✗ 5× too many
```

When one vertex has many neighbours, the others have few. The total is fixed: 2E for an undirected
graph, or E for the outgoing lists of a directed one.

#### Time — where V × E really comes from

**1. The edge set is scanned once per round — Bellman-Ford.**

```python
# python
# LC 743 - Network Delay Time
# IDEA: Bellman-Ford — relax every edge, up to V − 1 rounds, stop early once a round changes nothing
# time = O(V × E) : up to V − 1 rounds, each relaxing every edge
# space = O(V)    : one dist array, overwritten every round (`times` is the input)
def networkDelayTime(times, n, k):
    dist = [float('inf')] * (n + 1)
    dist[k] = 0
    for _ in range(n - 1):                 # V − 1 rounds
        updated = False
        for u, v, w in times:              # all E edges, every round
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:                    # nothing changed: later rounds would not either
            break
    best = max(dist[1:])
    return -1 if best == float('inf') else best
```

The loops multiply. The memory does not: every round writes into the **same** `dist` array, so space
stays O(V). Dijkstra solves LC 743 in O((V + E) log V); Bellman-Ford is the tool when weights can be
negative or the number of edges is bounded (LC 787). See [Bellman-Ford.md](./Bellman-Ford.md).

**2. A whole traversal is repeated from every vertex.**

```python
# python
# LC 1462 - Course Schedule IV
# IDEA: BFS from every course and keep each reachable set; a query is then one set lookup
# time = O(V × (V + E)) : V separate BFS runs, each O(V + E)
# space = O(V²)         : one reachable set per course, kept for the queries
from collections import defaultdict, deque

def checkIfPrerequisite(numCourses, prerequisites, queries):
    graph = defaultdict(list)
    for pre, nxt in prerequisites:
        graph[pre].append(nxt)

    reach = []                             # reach[s] = every course reachable from s
    for start in range(numCourses):        # V starts
        seen = {start}
        queue = deque([start])
        while queue:                       # one full BFS = O(V + E)
            node = queue.popleft()
            for nb in graph[node]:
                if nb not in seen:
                    seen.add(nb)
                    queue.append(nb)
        reach.append(seen)
    return [v in reach[u] for u, v in queries]
```

This is `V × (V + E) = O(V² + V × E)`, which is not the same as V × E. Say both terms unless the graph is
known to be connected (then E ≥ V − 1 and it simplifies to O(V × E)).

**3. The neighbours are found by scanning the edge list.** If the code never builds an adjacency list
and instead runs `for u, v in edges` for every vertex it pops, each of the V vertices scans all E edges:
**O(V × E)**. On the star graph above that is 90 steps instead of 18. The fix is to build the
adjacency list once, in O(E), which brings the traversal back to O(V + E).

#### Space — count the entries, not the levels of nesting

A hashmap's space is **the total number of entries stored under every key**, not "one per key" and not
"the map is nested, so multiply".

```text
A --- B          graph = {
|     |            "A": ["B", "C"],
C --- D            "B": ["A", "D"],
                   "C": ["A", "D"],
                   "D": ["B", "C"],
                 }
```

| Stored | Count | Why |
|---|---|---|
| keys | V = 4 | one per vertex |
| entries in all the lists | 2E = 8 | each undirected edge is stored twice (A→B and B→A) |
| **total** | **O(V + E)** | the constant 2 drops |

A nested map does not change the count. LC 399 Evaluate Division stores `graph[a][b] = value` and
`graph[b][a] = 1 / value`: the inner dicts hold each vertex's *own* neighbours, so all of them together
hold 2E entries. That is still **O(V + E)**.

Space becomes **O(V × E)** only when each of the V keys holds its **own copy** of all E edges:

```python
# python
# IDEA: every vertex keeps its own full copy of the edge list (shown to count it, not to copy it)
# space = O(V × E): V lists, each of length E
copies = {vertex: list(edges) for vertex in graph}
```

On the square above that is 4 × 4 = 16 entries against the adjacency list's 8. This is rarely what a
problem needs. If you find yourself writing it, store the edges once and share them.

Two products that are easy to mislabel as V × E:

- **An adjacency matrix** is `V × V` cells whatever E is: **O(V²)**.
- **A per-source result kept for later**, as in LC 1462 above, is V sets of up to V vertices: **O(V²)**.

#### How to say it in an interview

```text
1. How many keys?                    one per vertex        → O(V)
2. How many entries under ALL keys?  each edge once/twice  → + O(E)    ⇒ O(V + E)
                                     a copy of E per key   → V × E     ⇒ O(V × E)
3. Does a loop repeat over all edges?  once in total → + E;  once per vertex/round → × E
```

> "The space is the total number of entries in the map, not the number of keys. An adjacency list stores
> V vertices and E edges, so it is O(V + E). It would be O(V × E) only if each of the V vertices kept its
> own copy of all E edges. For time it is the same idea: one BFS touches each edge once, so O(V + E);
> Bellman-Ford relaxes every edge in each of V − 1 rounds, so O(V × E)."

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

## 12) Classic LC Review by Topic — the Interviewer's Table ⭐⭐⭐⭐⭐

Sections 1–11 derive one classic per technique, line by line. This section covers breadth: the **frequently asked problems** that are not worked above, grouped by topic. They are the Blind 75 and NeetCode 150 entries in the [Complexity Quiz](https://yennanliu.github.io/CS_basics/lc-complexity-quiz.html), plus its *Interview essentials* set. Each row gives the bound, **the line or structure that sets it**, and the follow-up or trap an interviewer is most likely to raise. A **bold** LC number is in the [L3 core set](https://yennanliu.github.io/CS_basics/l3-core.html).

Use the tables as a drill: cover the last three columns, say the bound and why out loud, then check. The quiz asks the same problems with the code in front of you.

### How a Google interviewer hears a complexity answer

Complexity is scored under *problem solving* and *verification*, and a wrong or unexplained bound costs more than a slow first draft. The same O(n) can be scored very differently:

| What the candidate says | How it reads | Why |
|---|---|---|
| "It's O(n)." | Leaning no hire | Gives the bound but not the reason; the interviewer has to ask for the reason |
| "O(n) — one loop." | Leaning hire, if correct | States the reason, but often misses an amortised inner loop or a hidden slice |
| "O(n) time: `right` and `left` each move at most n times, so the inner `while` totals n. O(k) space for the deque." | Hire | Names the line that sets the bound, the variable each bound is in, and space separately |
| …plus "a heap that drops expired entries is O(n log k), and a lazy one can grow to O(n log n); the deque is what gets us linear." | Strong hire | Also places the solution among the alternatives, which is what the follow-up was going to ask |

**Say your answer in four sentences:** *(1) define the variables* ("n = len(nums), k = window"); *(2) time, and the line that sets it*; *(3) space, and the structure that holds it, with the recursion stack and the output stated separately*; *(4) the trade-off*, meaning the slower brute force and what a faster or leaner answer would cost.

### Arrays & Hashing

What the interviewer listens for: whether you can say what the hash map **buys** (a log factor or a second loop) and what it **costs** (O(n) space). Then the follow-up asks for O(1) space.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **121** | Best Time to Buy and Sell Stock | O(n) | O(1) | One pass; a running minimum and a best profit | Checking every buy/sell pair is O(n²) |
| **238** | Product of Array Except Self | O(n) | O(1) | A prefix pass, then a suffix pass; two loops in sequence add | The output array is not counted; the suffix is one variable |
| **217** | Contains Duplicate | O(n) | O(n) | One pass with an O(1) average `in seen` | Sort and compare neighbours: O(1) space, O(n log n) time |
| 169 | Majority Element | O(n) | O(1) | Boyer–Moore keeps `candidate` and `count` | `Counter` is O(n) space; sorting is O(n log n) |
| 41 | First Missing Positive | O(n) | O(1) | Each swap puts one value in its final slot, so there are at most n swaps in total | A `while` inside a `for` that is still linear: count swaps, not loops |
| 36 | Valid Sudoku | O(1) | O(1) | A fixed 9 × 9 board, 81 cells × 3 keys | There is no n; on a general n × n board it is O(n²) |
| 525 | Contiguous Array | O(n) | O(n) | Map 0 → −1, then "seen this prefix sum before" | Counting both digits for every subarray is O(n²) |
| 974 | Subarray Sums Divisible by K | O(n) | O(min(n, k)) | One pass; the keys are remainders 0..k−1 | O(n) space is safe but loose: the map holds at most k keys |
| 289 | Game of Life | O(m·n) | O(1) | 9 constant reads per cell; the next state goes in bit 1 of the same cell | Copying the board is O(m·n) space; the follow-up asks for in place |
| 442 | Find All Duplicates in an Array | O(n) | O(1) | The sign of `nums[v−1]` marks v as seen | Only works because values lie in 1..n |

### Two Pointers & Sliding Window

What the interviewer listens for: an **amortised** argument. "`left` only moves forward" is the sentence that turns a nested `while` into O(n).

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| 88 | Merge Sorted Array | O(m + n) | O(1) | Write from the back; each slot is written once | Merging forward needs a copy or O(m·n) shifting |
| **567** | Permutation in String | O(m + n) | O(1) | One slide; each window compare is 26 slots, a constant | The comparison is O(26), not O(m) |
| 283 | Move Zeroes | O(n) | O(1) | One `fast` pass with at most one swap per element | `remove(0)` + `append(0)` in a loop is O(n²) |
| **239** | Sliding Window Maximum | O(n) | O(k) | Each index enters and leaves the deque at most once | Calling `max` on each window is O(n·k). A heap is O(n log k) only if it removes expired entries and stays at k; a lazy heap keeps them, so on an increasing array it grows to O(n) and costs O(n log n). See the card below |
| 340 | Longest Substring with At Most K Distinct | O(n) | O(k) | `left` never moves back | Space is O(k), not O(n): the shrink caps the map |
| 1438 | Longest Subarray with Abs Diff ≤ Limit | O(n) | O(n) | Two monotonic deques, max and min, each push/pop once | A heap or SortedList of the window gives O(n log n) |
| 977 | Squares of a Sorted Array | O(n) | O(1) | The largest square is at one of the two ends; fill from the back | Squaring then sorting is O(n log n) |

### Stack

What the interviewer listens for: "every element is pushed once and popped at most once". That one sentence settles almost every monotonic-stack bound.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **150** | Evaluate Reverse Polish Notation | O(n) | O(n) | One push or two pops per token | Pop order: the second operand comes off first |
| **853** | Car Fleet | O(n log n) | O(n) | Sorting by position; the sweep after it is linear | Simulating the cars catching up is O(n²) |
| 402 | Remove K Digits | O(n) | O(n) | Monotonic stack; there are only n pushes in total | Rescanning from the start after each removal is O(n·k) |
| 735 | Asteroid Collision | O(n) | O(n) | A destroyed asteroid never comes back, so total pops ≤ n | One big asteroid clearing many looks like O(n²) |
| 907 | Sum of Subarray Minimums | O(n) | O(n) | Two monotonic passes find the previous and next smaller element | `>` on one side and `>=` on the other avoids double-counting equal values |
| 503 | Next Greater Element II | O(n) | O(n) | 2n iterations, but indices are pushed only in the first n | Going around twice doubles the scan, not the stack work |
| 1249 | Minimum Remove to Make Valid Parentheses | O(n) | O(n) | Match pass + leftover pass + one `join` | `result += c` in a loop can be O(n²) |
| 227 | Basic Calculator II | O(n) | O(n) | `*` and `/` fold into the top; `sum` at the end | O(1) space: keep a running total and the last term |
| 85 | Maximal Rectangle | O(m·n) | O(n) | LC 84 run on each row's histogram, O(n) per row | It reads as O(m·n²); it is m runs of an amortised O(n) |

### Binary Search

What the interviewer listens for: **what is being halved**. It may be an index range, a 2-D range flattened to one, or a range of possible answers. Name it.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **74** | Search a 2D Matrix | O(log(m·n)) | O(1) | One search over m·n flat positions, `mid // n, mid % n` | = log m + log n, so two searches give the same bound |
| 34 | Find First and Last Position | O(log n) | O(1) | Two lower-bound searches | Finding one match and expanding is O(n) on `[7,7,7,…]` |
| **4** | Median of Two Sorted Arrays | O(log min(m, n)) | O(1) | Binary search for the cut in the **shorter** array | Merging is O(m + n), which the problem rules out |
| 162 | Find Peak Element | O(log n) | O(1) | Moving toward the larger neighbour keeps a peak in range | The array is unsorted, so people assume O(n) |
| 528 | Random Pick with Weight | O(log n) per pick | O(n) | `bisect` over the prefix sums | Expanding each weight into copies is O(W) space |
| 540 | Single Element in a Sorted Array | O(log n) | O(1) | Pairs start at even indices until the single element | XOR is O(n), the LC 136 answer, and misses the sorted property |
| 1539 | Kth Missing Positive Number | O(log n) | O(1) | `arr[i] − (i + 1)` is monotone, so binary search on it | Walking 1, 2, 3… is O(n + k), and k can be much larger than n |

### Linked List

What the interviewer listens for: the **O(1)-space** answer. Most list problems have an easy O(n)-space version (a set, or copying the values to an array), and the question is there to see whether you can avoid it.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **206** | Reverse Linked List | O(n) | O(1) | One walk, three pointers | The recursive version is O(n) stack |
| **141** | Linked List Cycle | O(n) | O(1) | Inside the cycle, `fast` gains one node per step | A `seen` set is O(n) space |
| **21** | Merge Two Sorted Lists | O(n + m) | O(1) | Splice existing nodes; attach the leftover in O(1) | The recursive version is O(n + m) stack |
| **19** | Remove Nth Node From End | O(L) | O(1) | Move `fast` n ahead, then walk both together | Copying the nodes into a list to index from the back is O(L) space |
| **143** | Reorder List | O(n) | O(1) | Find the middle → reverse → weave: three passes in sequence | An array of nodes is the same time but O(n) space |
| **138** | Copy List with Random Pointer | O(n) | O(n) | Two passes with an old → new map | O(1) space: interleave the clones, then split |
| 148 | Sort List | O(n log n) | O(log n) | Top-down merge sort, log n levels | O(1) space needs bottom-up merge sort |
| 234 | Palindrome Linked List | O(n) | O(1) | Reverse the second half in place and compare | `vals == vals[::-1]` is O(n) space |
| **287** | Find the Duplicate Number | O(n) | O(1) | Floyd on `i → nums[i]` | A set uses O(n) space; sorting mutates the input |
| **25** | Reverse Nodes in k-Group | O(n) | O(1) | Each node is walked twice: a check pass and a reverse pass | n/k groups × O(k) = O(n), not O(n·k) |
| 160 | Intersection of Two Linked Lists | O(m + n) | O(1) | After switching lists, both pointers are equally far from the end | It looks like it could loop forever; it ends within m + n steps |
| **2** | Add Two Numbers | O(m + n) | O(1) aux | One pass plus the carry; the new nodes are the output | Converting to integers overflows in fixed-width languages |
| 142 | Linked List Cycle II | O(n) | O(1) | Meeting point → restart from the head → meet at the entry | The second `while` runs after the first, not inside it |
| 92 | Reverse Linked List II | O(n) | O(1) | Walk to `left`, then head-insert `right − left` times | Copying the values to a list is O(n) space |

### Trees

What the interviewer listens for: **space = height**, said as "O(h): log n balanced, n skewed". And a check that no subtree is walked once per ancestor, the hidden O(n²).

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **98** | Validate Binary Search Tree | O(n) | O(h) | Pass a `(lo, hi)` range down, O(1) per node | Calling `max(left)` / `min(right)` at every node is O(n²) |
| **543** | Diameter of Binary Tree | O(n) | O(h) | One post-order pass returns the depth | A separate `depth()` called from every node is O(n²) |
| **105** | Construct Tree from Preorder & Inorder | O(n) | O(n) | Value → index map; one `next()` per node | `inorder.index()` + slicing is O(n²) |
| **230** | Kth Smallest Element in a BST | O(h + k) | O(h) | Iterative in-order traversal that stops at k | Collecting the full in-order list is O(n) time and space |
| **199** | Binary Tree Right Side View | O(n) | O(w) | Level BFS; `len(q)` fixes each level's size | The `for` inside the `while` still visits each node once |
| **110** | Balanced Binary Tree | O(n) | O(h) | Return the height, or −1 for unbalanced, in one pass | Top-down `height()` at each node: O(n log n) balanced, O(n²) skewed |
| **235** | LCA of a BST | O(h) | O(1) | The BST order picks one direction per level | LC 236 on a general tree is O(n) |
| **572** | Subtree of Another Tree | O(m·n) | O(m + n) | `same()` started from each of the m nodes | O(m + n): serialise both with null markers and use KMP |
| 108 | Sorted Array to BST | O(n) | O(log n) | Pick the middle; the result is balanced | Slicing `nums[:mid]` adds O(n log n) copying |
| 437 | Path Sum III | O(n) | O(n) | Prefix sum + hash map; undo on the way back up | Starting a new count at every node is O(n²) |
| **100** | Same Tree | O(n) | O(h) | Lock-step recursion | It is the inner loop of LC 572, where it stops being linear |
| 113 | Path Sum II | O(n·h) | O(h) aux | Each matching leaf copies a path of up to h values | Saying O(n) counts the visits but forgets the copies |
| 662 | Maximum Width of Binary Tree | O(n) | O(n) | Level BFS with positional indices | The width can be 2^h, but the work is per node |
| 450 | Delete Node in a BST | O(h) | O(h) | One path down, plus the successor's left spine | O(log n) only if the tree is balanced |
| 863 | All Nodes Distance K | O(n) | O(n) | Parent map, then BFS with `seen` | The `range(k)` loop does not multiply the cost: no node is visited twice |
| 116 | Populating Next Right Pointers | O(n) | O(1) | Walk each level along the `next` links set on the level above | Queue BFS is O(n) space; the follow-up asks for O(1) |

### Heap

What the interviewer listens for: the size of the heap. **O(n log k)** with a heap capped at k, not O(n log n). And `heapify` is O(n).

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **621** | Task Scheduler | O(t) | O(1) | Counting plus a formula over ≤ 26 counts | A slot-by-slot simulation gets described as O(t·n) |
| **1046** | Last Stone Weight | O(n log n) | O(n) | < n rounds × O(log n) heap operations | Re-sorting every round is O(n² log n) |
| **703** | Kth Largest Element in a Stream | O(log k) per add | O(k) | Min-heap capped at k | A sorted list is O(k) per insert because of the shift |
| **355** | Design Twitter | O(f) per feed | O(f) | Up to 10 tweets per followee, heap capped at 10 | Merging whole timelines is O(N log N) over all tweets |
| 692 | Top K Frequent Words | O(n + k log u) | O(u) | Count, `heapify` (O(u)), then k pops | `heapify` is linear; pushing one at a time is u log u |
| 373 | Find K Pairs with Smallest Sums | O(k log k) | O(k) | Seed only the first min(m, k) rows; advance a row only when its pair is popped | Seeding all m rows makes the heap O(m), which breaks O(k) when k is small. Generating all m·n pairs is far worse |

### Graphs

What the interviewer listens for: **what V and E are for this input**. A grid is V = m·n with E ≈ 4V; an adjacency matrix costs V² whatever the number of edges.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **994** | Rotting Oranges | O(m·n) | O(m·n) | Multi-source BFS; each cell is enqueued once | The `for _ in range(len(q))` loop marks levels; it adds no work |
| 547 | Number of Provinces | O(n²) | O(n) | An adjacency **matrix**: every row is scanned | It is O(V + E) in disguise, with E read as n² |
| **417** | Pacific Atlantic Water Flow | O(m·n) | O(m·n) | Two flood fills, inward from each ocean's border | Searching outward from every cell is O((m·n)²) |
| **695** | Max Area of Island | O(m·n) | O(m·n) | Sink cells as you enter them | Stack space can hold the whole grid on a snake-shaped island |
| **127** | Word Ladder | O(n·L²) | O(n·L) | Per word: L positions × 26 letters × an O(L) string build and hash | Most people forget the second L. See the card below |
| **323** | Number of Connected Components | O(n + e·α(n)) | O(n) | Union-find with path compression | Without compression, a find can be O(n) |
| **261** | Graph Valid Tree | O(n + e·α(n)) | O(n) | `len(edges) == n − 1`, plus union-find finds no cycle | The edge-count check does half the work |
| 721 | Accounts Merge | O(s log s) | O(s) | Sorting each group's emails dominates | Union-find is the cheap part here |
| 542 | 01 Matrix | O(m·n) | O(m·n) | Seed the BFS with **all** the 0s at once | A BFS from every 1 is O((m·n)²) |
| 785 | Is Graph Bipartite? | O(V + E) | O(V) | Colour as you go; the outer loop handles disconnected parts | It looks like V × BFS; `color` stops any re-visit |
| 909 | Snakes and Ladders | O(n²) | O(n²) | BFS over the n² squares, 6 moves each | The graph has n² vertices; answering O(n) is wrong |

### Advanced Graphs

What the interviewer listens for: **which algorithm, and why its bound fits this graph**. Choosing between Prim's with an array or with a heap depends on whether the graph is dense or sparse.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **269** | Alien Dictionary | O(c) | O(1) graph | Compare adjacent words; the graph is capped at 26 letters | O(V + E) is the wrong question: give the bound in c, the total characters |
| **332** | Reconstruct Itinerary | O(e log e) | O(e) | Sort the tickets, then Hierholzer uses each edge once | Backtracking over ticket orders is factorial |
| **778** | Swim in Rising Water | O(n² log n) | O(n²) | Dijkstra over n² cells; log(n²) = 2 log n | It is not (log n)² |
| **1584** | Min Cost to Connect All Points | O(n²) | O(n) | Prim's with an array on a complete graph | The heap version is O(n² log n), which is worse on a dense graph |
| 1631 | Path With Minimum Effort | O(m·n·log(m·n)) | O(m·n) | Dijkstra with minimax relaxation | A plain BFS with `visited` gives wrong answers |
| 1293 | Shortest Path with Obstacles Elimination | O(m·n·k) | O(m·n·k) | State = (row, col, removals left) | Saying O(m·n) forgets the extra state dimension |

### Dynamic Programming

What the interviewer listens for: **states × transition cost**, then "can the table roll?". If dp[i] reads only the previous row, space drops by one dimension.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **198** | House Robber | O(n) | O(1) | Each state reads only i−1 and i−2, so two variables | Plain recursion is O(2^n) |
| **62** | Unique Paths | O(m·n) | O(n) | One rolling row | O(min(m, n)) via the binomial C(m+n−2, m−1) |
| **139** | Word Break | O(n²) slices | O(n + m) | `for i` × `for j`, with a set lookup | Each `s[j:i]` slice and hash is O(n), so honestly O(n³); bound j by the longest word |
| **152** | Maximum Product Subarray | O(n) | O(1) | Track both the max and the min, because a negative swaps them | `nums[1:]` silently copies the array |
| **91** | Decode Ways | O(n) | O(1) | Two rolling variables | Plain recursion branches two ways per index: O(2^n) |
| **213** | House Robber II | O(n) | O(1) with indices | Run LC 198 twice, on [0, n−2] and [1, n−1] | Slicing the array makes it O(n) space |
| **416** | Partition Equal Subset Sum | O(n·s) | O(s) | One boolean row over sums, iterated backward | Pseudo-polynomial: s is a value, not an input size |
| **647** | Palindromic Substrings | O(n²) | O(1) | 2n−1 centres × expansion | Checking every substring is O(n³); the DP table is O(n²) space |
| 64 | Minimum Path Sum | O(m·n) | O(1) | Write over the grid in place | Enumerating paths is exponential |
| 10 | Regular Expression Matching | O(m·n) | O(m·n) | One cell per (i, j), O(1) each | Plain recursion is exponential |
| **97** | Interleaving String | O(m·n) | O(n) | Rolling row | A greedy two-pointer fails on ties |
| **309** | Stock with Cooldown | O(n) | O(1) | A three-state machine: hold, sold, rest | A table by day collapses to three scalars |
| 312 | Burst Balloons | O(n³) | O(n²) | O(n²) intervals × n choices of the **last** balloon | Choosing the first balloon leaves non-contiguous gaps |
| **329** | Longest Increasing Path in a Matrix | O(m·n) | O(m·n) | Memoised DFS, each cell solved once | Without the memo it is exponential |
| 494 | Target Sum | O(n·s) | O(s) | One layer of reachable sums in −s..s | Pseudo-polynomial; brute force is O(2^n) |
| 518 | Coin Change II | O(n·a) | O(a) | Coins outer, amounts inner | Swapping the loops counts orderings instead (LC 377) |
| 516 | Longest Palindromic Subsequence | O(n²) | O(n²) → O(n) | Interval DP; only rows i and i+1 are read | Enumerating subsequences is O(2^n) |
| 221 | Maximal Square | O(m·n) | O(n) | min(up, left, diagonal) + 1, rolling row | Growing a square from every corner is much worse |
| 1235 | Maximum Profit in Job Scheduling | O(n log n) | O(n) | Sort by end time + binary search for the previous compatible job | A linear backward scan is O(n²) |
| 96 | Unique Binary Search Trees | O(n²) | O(n) | Catalan sum over each root | Closed form C(2n, n)/(n+1) in O(n) |
| 44 | Wildcard Matching | O(m·n) | O(m·n) | `*` handled as dp[i−1][j] or dp[i][j−1] | Looping over every length `*` could absorb makes it O(m²·n) |

### Backtracking

What the interviewer listens for: **leaves × cost per leaf**, with the copy into the result counted. A bound with no factor for building each answer is the most common miss.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **79** | Word Search | O(m·n·3^L) | O(L) | m·n starts × branching 3 (4 at the first step) for L levels | 4^L is accepted; 3^L is tighter because you never step back |
| **17** | Letter Combinations of a Phone Number | O(n·4^n) | O(n) aux | Up to 4^n leaves × an O(n) `join` | O(4^n) counts the strings but not their length |
| **131** | Palindrome Partitioning | O(n·2^n) | O(n) aux | 2^(n−1) ways to cut × an O(n) palindrome check | Precompute an O(n²) palindrome table |
| **90** | Subsets II | O(n·2^n) | O(n) aux | 2^n subsets × copy | Deduplication does not change the worst case (all values distinct) |
| **40** | Combination Sum II | O(n·2^n) | O(n) | In or out for each value, plus a copy per hit | `break` pruning depends on the values, not on n |
| 47 | Permutations II | O(n·n!) | O(n) | All-distinct input is the worst case | A set of results is the same time and more space |
| 93 | Restore IP Addresses | O(1) | O(1) | 4 parts × ≤ 3 digits caps it at 3^4 leaves | It looks exponential, but the depth and fan-out are fixed |
| 797 | All Paths From Source to Target | O(n·2^n) | O(n) aux | A DAG can have 2^(n−2) paths, each copied | It is not O(V + E): there is no `visited` set, by design |

### Tries

What the interviewer listens for: bounds in **word length L**, not in the number of words. That independence is why you use a trie.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **208** | Implement Trie | O(L) per operation | O(L) per insert | One dict hop per character | No n in either bound |
| **211** | Add and Search Words | O(26^L) worst for search | O(L) stack | Each `.` forks into every child | Plain words are O(L); only the wildcard is exponential |
| **212** | Word Search II | O(K + m·n·3^L) | O(K + L) | Building the trie is O(K), K = total characters in the words; then one DFS per cell, shared through the trie, L = longest word | Space is the trie (O(K)) plus the DFS stack (O(L)). Running LC 79 once per word multiplies the search by w |

### Intervals

What the interviewer listens for: **sort once, sweep once**. Sorting dominates, so the bound is O(n log n) unless the input is already sorted.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **253** | Meeting Rooms II | O(n log n) | O(n) | Sort + a min-heap of end times | `rooms[0]` is an O(1) peek, not a scan |
| **1851** | Minimum Interval to Include Each Query | O(n log n + q log q) | O(n + q) | Sort both; each interval is pushed and popped once | Only works offline, with all queries known up front |
| 986 | Interval List Intersections | O(m + n) | O(1) aux | Advance whichever interval ends first | Both inputs are already sorted, so there is no log factor |

### Greedy

What the interviewer listens for: **one pass + an argument for why greedy is safe**. The bound is easy; the proof is what is being tested.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **55** | Jump Game | O(n) | O(1) | Track the farthest reachable index | The DP is O(n²) |
| **45** | Jump Game II | O(n) | O(1) | Implicit BFS by levels: `end` and `farthest` | Stop at n−1 or you count one jump too many |
| **134** | Gas Station | O(n) | O(1) | If the total gas covers the total cost, the one surviving start works | Simulating from every station is O(n²) |
| **763** | Partition Labels | O(n) | O(1) | `last` index per letter (≤ 26 keys) | The map is bounded by the alphabet, not by n |
| **846** | Hand of Straights | O(n log n) | O(n) | Sort the distinct cards; the inner loop totals O(n) | It reads as O(n·g) |
| 31 | Next Permutation | O(n) | O(1) | Pivot → swap → reverse the tail | Sorting the tail is O(n log n); it is already descending |
| **678** | Valid Parenthesis String | O(n) | O(1) | Track the [low, high] range of open counts | Trying every meaning of `*` is O(3^n) |
| 406 | Queue Reconstruction by Height | O(n²) | O(n) | Sort, then `list.insert` at k shifts the tail | O(n log n) needs a Fenwick tree for the k-th free slot |
| 334 | Increasing Triplet Subsequence | O(n) | O(1) | Two running minimums | It is patience sorting with length 3 |

### Sorting

What the interviewer listens for: **the cost of one comparison**. When a comparison builds strings, the sort costs O(n log n) × the cost of that comparison.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **242** | Valid Anagram | O(n log n) | O(n) | Two `sorted` calls | A `Counter` or a 26-slot count is O(n) |
| 75 | Sort Colors | O(n) | O(1) | Dutch flag: each step moves `i` forward or `hi` back | `i` does not advance after a swap with `hi` |
| 315 | Count of Smaller Numbers After Self | O(n log n) | O(n) | Rank compression + a Fenwick tree, two O(log n) walks per value | The double loop is O(n²) |
| 179 | Largest Number | O(k·n log n) | O(n·k) | n log n comparisons, each concatenating strings of length ≤ 2k | Treating a comparison as O(1) is wrong here |

### Bit Manipulation

What the interviewer listens for: **fixed width means O(1)**. A loop over the bits of a 32-bit integer is constant, and calling it O(n) treats the value as the input size.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| 136 | Single Number | O(n) | O(1) | XOR cancels the pairs | A set is O(n) space |
| **191** | Number of 1 Bits | O(1) | O(1) | `n &= n − 1` runs popcount ≤ 32 times | O(b) for a general width b |
| **338** | Counting Bits | O(n) | O(n) output | `dp[i] = dp[i >> 1] + (i & 1)` | `bin(i).count("1")` per number is O(n log n) |
| **268** | Missing Number | O(n) | O(1) | XOR of the indices and the values | Gauss's sum overflows in fixed-width languages |
| **190** | Reverse Bits | O(1) | O(1) | Exactly 32 iterations | It is not log n, because the loop never exits early |
| **7** | Reverse Integer | O(log x) | O(1) | One step per digit, ≤ 10 digits | The overflow check is the real point of the question |

### Math & Geometry

What the interviewer listens for: **in-place** matrix tricks and correct reasoning about magnitude: digits are log x, and exponentiation is O(log n).

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **48** | Rotate Image | O(n²) | O(1) | Transpose, then reverse each row | `zip(*matrix[::-1])` is O(n²) space |
| **54** | Spiral Matrix | O(m·n) | O(1) aux | Four shrinking bounds; each cell is emitted once | The inner loops split the work; they do not repeat it |
| **73** | Set Matrix Zeroes | O(m·n) | O(1) | Use row 0 and column 0 as markers, plus one flag | Two index sets are O(m + n) space |
| **50** | Pow(x, n) | O(log n) | O(log n) | Halve the exponent each call | The iterative version is O(1) space |
| **202** | Happy Number | O(log n) | O(log n) | The first digit-square sum shrinks n to a small value | Floyd's cycle detection drops the set |
| 240 | Search a 2D Matrix II | O(m + n) | O(1) | Start in the top-right corner; drop a row or a column each step | One flat binary search is wrong: the rows do not chain |
| **43** | Multiply Strings | O(m·n) | O(m + n) | Every digit meets every digit | Karatsuba is about O(n^1.585) |

### Design

What the interviewer listens for: **cost per operation, and whether it is amortised**. "O(1) amortised" is a complete answer only if you can say what pays for the expensive call.

| LC | Problem | Time | Space | What sets the bound | Follow-up / trap |
|---|---|---|---|---|---|
| **155** | Min Stack | O(1) per operation | O(n) | Store `(val, min_so_far)` pairs | `min(a, b)` compares two values; it does not scan the stack |
| **146** | LRU Cache | O(1) per operation | O(c) | Hash map + doubly linked list. See the card below | `move_to_end` relinks one node; nothing shifts |
| **981** | Time Based Key-Value Store | O(log n) get, O(1) set | O(t) | Binary search on each key's timestamp list | O(1) set relies on timestamps arriving in increasing order |
| **271** | Encode and Decode Strings | O(c) | O(c) | Length-prefix framing | A plain delimiter is wrong, not just slow |
| **2013** | Detect Squares | O(n) per count | O(n) | Fix the diagonal point; the other two corners are O(1) lookups | Trying every pair of points is O(n²) |
| 173 | BST Iterator | O(1) amortised | O(h) | Each node is pushed and popped once over all calls | A single `next` can cost O(h) |
| 380 | Insert Delete GetRandom O(1) | O(1) | O(n) | Array + value → index map; swap with the last element, then pop | `list.remove` is O(n) |

### The derivations interviewers push hardest on

Most bounds in the tables follow from one sentence. These three are the exceptions: the obvious first answer is wrong, and the follow-up is built to test exactly that.

#### Sliding Window Maximum — LC 239

> **Key point** — the `while` inside the `for` is not O(k) per step. Each index is appended once and popped at most once, so all the pops in the whole run total at most n.

**Reduces to**: [§ 3 Sliding Window](#3-sliding-window--on-over-substringssubarrays), with a monotonic deque where § 3 uses a counter. The amortised argument is § 2's LC 84 argument.

```python
# python — LC 239
# IDEA: keep a deque of indices whose values strictly decrease; the front is the window max
# time = O(n)  : each index enters the deque once and leaves at most once (≤ 2n deque operations)
# space = O(k) : the deque only ever holds indices inside the current window
from collections import deque

class Solution:
    def maxSlidingWindow(self, nums, k):
        window = deque()          # indices; nums[window[0]] is the current max
        result = []
        for right, value in enumerate(nums):
            # smaller values behind `value` can never be a max again, so drop them
            while window and nums[window[-1]] <= value:
                window.pop()
            window.append(right)
            # the front left the window
            if window[0] <= right - k:
                window.popleft()
            if right >= k - 1:
                result.append(nums[window[0]])
        return result
```

**How to say it:** "O(n) time. The inner `while` is amortised: every index is pushed once and popped at most once, so it does at most n pops over the whole run. O(k) extra space for the deque. The alternatives are O(n·k) for a `max` per window, and a heap: O(n log k) time and O(k) space if expired entries are removed so it stays at k, but O(n log n) time and O(n) space for the usual lazy heap, which keeps expired entries on an increasing array."

#### LRU Cache — LC 146

> **Key point** — O(1) for both `get` and `put` needs two structures. A hash map finds a node in O(1), and a doubly linked list moves or evicts that node in O(1). Neither can do both alone.

**Reduces to**: [§ 1 HashMap](#1-hashmap--o1-lookup-trades-space-for-time), with the map's values kept in recency order.

```python
# python — LC 146
# IDEA: OrderedDict = hash map + doubly linked list; the front is least recently used
# time = O(1) per get / put : a hash lookup plus relinking one node (move_to_end, popitem)
# space = O(c)              : at most `capacity` entries
from collections import OrderedDict

class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = OrderedDict()

    def get(self, key):
        if key not in self.cache:
            return -1
        self.cache.move_to_end(key)          # mark as most recently used
        return self.cache[key]

    def put(self, key, value):
        if key in self.cache:
            self.cache.move_to_end(key)
        self.cache[key] = value
        if len(self.cache) > self.capacity:
            self.cache.popitem(last=False)   # evict the least recently used
```

**How to say it:** "O(1) per operation. The map gives O(1) lookup and the linked list gives O(1) move and evict. O(capacity) space. A plain list would make the 'move to front' step O(n)." Interviewers often ask you to write the doubly linked list yourself, and the bound is the same.

#### Word Ladder — LC 127

> **Key point** — the work per dequeued word is L positions × 26 letters × **an O(L) string build and hash**. The usual answer of O(n·L) forgets that each candidate is a new string.

**Reduces to**: [§ 8 Graphs](#8-graphs--ov--e). It is BFS on an implicit graph with V = n words, where the edges are generated rather than stored.

```python
# python — LC 127
# IDEA: BFS from beginWord; neighbours are every one-letter change that is still in the word set
# time = O(n · L²)  : n words dequeued × (L · 26 candidates) × O(L) to build and hash each one
# space = O(n · L)  : the word set and the queue hold up to n strings of length L
from collections import deque

class Solution:
    def ladderLength(self, beginWord, endWord, wordList):
        words = set(wordList)
        if endWord not in words:
            return 0
        queue = deque([(beginWord, 1)])
        while queue:
            word, steps = queue.popleft()
            if word == endWord:
                return steps
            for i in range(len(word)):
                for letter in "abcdefghijklmnopqrstuvwxyz":
                    candidate = word[:i] + letter + word[i + 1:]   # O(L) build
                    if candidate in words:                          # O(L) hash
                        words.remove(candidate)                     # mark visited
                        queue.append((candidate, steps + 1))
        return 0
```

**How to say it:** "Treat it as BFS on an implicit graph with n vertices. Each word tries 26·L neighbours, and building and hashing each one is O(L), so O(n·L²) time and O(n·L) space. Bidirectional BFS keeps the same worst case but explores far fewer nodes in practice."

---

## 13) Quick Reference — "How to argue complexity in an interview"

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
GRAPH, all edges per round   → O(V × E)   (Bellman-Ford; V traversals = O(V × (V + E)))
GRID traversal               → O(rows · cols)
DP                           → (#states) × (transition cost)
AMORTIZED (each item ≤1 push/pop) → O(n) even with an inner while-loop
```

### Space-complexity checklist

```text
□ Did I count the recursion stack?      (DFS = O(height), not O(1))
□ Is the output counted or excluded?    (usually excluded as "aux space")
□ Can a 2D dp collapse to 1D rolling?   (dp[i] depends only on dp[i-1]?)
□ Can two pointers replace an array?    (prefix-array → running variable)
□ In-place possible?                    (mark grid, reverse array, swap)
```

### Final sanity checks

```text
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
