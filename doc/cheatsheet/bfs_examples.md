# BFS — Worked LeetCode Examples

> **Scope** — The worked-solution archive for BFS: one canonical solution per grid, state-space, tree-mutation and leaf-trimming problem, plus the LC 994 walkthrough on where to increment time — it teaches no new templates.
> **See also**: [bfs.md](./bfs.md) — the canonical templates every example here instantiates, and where all `Pattern N` references point; [bfs_advanced.md](./bfs_advanced.md) — the rarer BFS variants (bidirectional, 0-1 deque, route-level, all-shortest-paths enumeration).

## LeetCode Problem Lists

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Overview

The example archive split out of [bfs.md](./bfs.md). Section numbers (`§2-6` … `§2-18`) are the ones the main sheet and [bfs_advanced.md](./bfs_advanced.md) cite, so they are kept stable rather than renumbered — gaps mean that example now lives in the main sheet as a template.

| Group | Problems |
|---|---|
| Level-order variations | LC 662, LC 958 |
| Level counting / timing | LC 994 |
| State-space BFS | LC 752, LC 773, LC 909 |
| Grid BFS | LC 130, LC 286, LC 417 |
| Graph BFS | LC 207, LC 279, LC 310 |
| Tree BFS | LC 742, LC 116 / 117, LC 623, LC 863 |

## Level-Order Variations

> Both extend **Pattern 2** (level-by-level BFS) in [bfs.md](./bfs.md).

### Variation A: carry a **heap index** with each node — LC 662 (Maximum Width of Binary Tree)

> **Twist**: the queue holds `(node, index)` where a node at index `i` has children `2i` / `2i+1`. Width of a level = `lastIndex - firstIndex + 1`, counting the `null` gaps without ever storing them. **Normalize** each level by subtracting the level's first index, otherwise the index overflows `int` on a skewed tree of depth ~60.

```java
// java
// LC 662 - Maximum Width of Binary Tree
// time = O(N), space = O(W)   W = max level width
// IDEA: level BFS carrying a heap index; width = last - first + 1 (nulls counted implicitly)
public int widthOfBinaryTree(TreeNode root) {
    if (root == null) return 0;
    int ans = 0;
    Queue<TreeNode> nodes = new LinkedList<>();
    Queue<Integer> idxs = new LinkedList<>();
    nodes.offer(root); idxs.offer(0);
    while (!nodes.isEmpty()) {
        int size = nodes.size(), first = 0, last = 0;
        for (int i = 0; i < size; i++) {
            TreeNode node = nodes.poll();
            int id = idxs.poll();
            if (i == 0) first = id;
            id -= first;                    // re-base the level at 0 -> no overflow
            last = id;
            if (node.left  != null) { nodes.offer(node.left);  idxs.offer(2 * id); }
            if (node.right != null) { nodes.offer(node.right); idxs.offer(2 * id + 1); }
        }
        ans = Math.max(ans, last + 1);      // last is already relative to first
    }
    return ans;
}
```

```python
# python
# LC 662 - Maximum Width of Binary Tree
# time = O(N), space = O(W)
# IDEA: queue holds (node, index); re-base index per level to keep numbers small
def widthOfBinaryTree(root):
    if not root:
        return 0
    ans = 0
    q = deque([(root, 0)])
    while q:
        first = q[0][1]
        last = first
        for _ in range(len(q)):
            node, idx = q.popleft()
            idx -= first                    # normalize against this level's start
            last = idx
            if node.left:
                q.append((node.left, 2 * idx))
            if node.right:
                q.append((node.right, 2 * idx + 1))
        ans = max(ans, last + 1)
    return ans
```

### Variation C: enqueue the `null` children too — LC 958 (Check Completeness of a Binary Tree)

> **Twist**: pushing `null`s makes the queue a literal array-representation of the tree. A complete tree has all its `null`s at the tail, so: once you pop a `null`, no non-`null` may follow. This is also the shape of level-order **serialization** (LC 297 / LC 449 write `null` markers for exactly this reason).

```python
# python
# LC 958 - Check Completeness of a Binary Tree
# time = O(N), space = O(W)
# IDEA: push nulls; after the first null pops, any real node means "not complete"
def isCompleteTree(root):
    q = deque([root])
    seen_null = False
    while q:
        node = q.popleft()
        if node is None:
            seen_null = True
        else:
            if seen_null:
                return False        # a real node after a gap -> not complete
            q.append(node.left)     # push children unconditionally, nulls included
            q.append(node.right)
    return True
```

## Level Counting & Timing

### When to Increment Time/Distance: Beginning vs End of BFS Level

A common source of bugs in level-by-level BFS is **where to place the time/distance increment**. There are two valid approaches, each with different trade-offs.

#### The Two Approaches

**Approach A: Increment at BEGINNING of level (before processing)**
```java
// From LC 994 - RottingOranges.java V0
while (!queue.isEmpty() && freshOrange > 0) {  // NOTE: extra condition!
    int size = queue.size();
    time++;  // Increment FIRST - we're about to process a "minute" level

    for (int i = 0; i < size; i++) {
        int[] cur = queue.poll();
        // process neighbors, infect fresh oranges...
    }
}
return freshOrange == 0 ? time : -1;
```

**Approach B: Increment at END of level (only if work was done)**
```java
// From LC 994 - RottingOranges.java V0-0-2, V0-1, V0-4
while (!queue.isEmpty()) {
    int size = queue.size();
    boolean rottedThisMinute = false;

    for (int i = 0; i < size; i++) {
        int[] cur = queue.poll();
        // process neighbors...
        if (/* infected a fresh neighbor */) {
            rottedThisMinute = true;
        }
    }

    if (rottedThisMinute) time++;  // Only count if actual infection happened
}
return freshOrange == 0 ? time : -1;
```

#### Detailed Comparison

| Aspect | Approach A (Beginning) | Approach B (End with Flag) |
|--------|------------------------|---------------------------|
| **When to increment** | Before processing level | After processing, only if work done |
| **Extra while condition?** | Yes: `freshOrange > 0` | No, flag handles edge cases |
| **Risk** | Over-counting if condition missing | None if flag used correctly |
| **Code complexity** | Simpler loop body | Requires tracking boolean flag |
| **When returns 0?** | Natural if no fresh oranges | Natural: no work = no increment |

#### Why Approach A Needs `freshOrange > 0` in While Condition

**The Problem:** If we only check `!queue.isEmpty()`, we'll increment time for processing already-rotten cells that have nothing left to infect.

```text
Scenario: After all oranges are infected

Layer N: Queue = [(2,1)], freshOrange = 1
  - time++ → time = 4
  - Process (2,1): infect (2,2)
  - freshOrange = 0, Queue = [(2,2)]

Layer N+1: Queue = [(2,2)], freshOrange = 0
  - WITHOUT `freshOrange > 0`: time++ → time = 5 (WRONG! over-count)
  - WITH `freshOrange > 0`: Exit loop, return time = 4 (CORRECT!)
```

**The Key Insight:** When `freshOrange == 0`, all oranges are ALREADY infected (marked as 2). The queue may still contain rotten cells, but they have no fresh neighbors to infect. Processing them would waste time and over-count.

```java
// CORRECT: Exit early when nothing left to infect
while (!queue.isEmpty() && freshOrange > 0) {
    time++;
    // ...
}
```

#### Why Approach B Naturally Handles Edge Cases

```java
while (!queue.isEmpty()) {
    int size = queue.size();
    boolean rottedThisMinute = false;

    for (int i = 0; i < size; i++) {
        // process...
        if (/* infected a neighbor */) {
            rottedThisMinute = true;
        }
    }

    if (rottedThisMinute) time++;  // Only count if actual infection happened
}
```

**Why it works:**
- Even if queue has items (previously infected cells)
- If they don't infect any NEW cells → `rottedThisMinute = false`
- No increment → no over-counting

#### Concrete Example: LC 994 Rotting Oranges — where to increment time

```text
Grid: [[2,1,1],    Initial: 6 fresh oranges, 1 rotten at (0,0)
       [1,1,0],
       [0,1,1]]    Expected answer: 4 minutes
```

**Approach A Trace (time++ at beginning with `freshOrange > 0`):**

```text
Initial: Queue=[(0,0)], fresh=6, time=0

Check: queue not empty && fresh>0 → TRUE
  time++ → time=1
  Process (0,0): infect (0,1), (1,0)
  fresh=4, Queue=[(0,1),(1,0)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=2
  Process (0,1): infect (0,2), (1,1)
  Process (1,0): nothing new
  fresh=2, Queue=[(0,2),(1,1)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=3
  Process (0,2): nothing (neighbor (1,2)=0)
  Process (1,1): infect (2,1)
  fresh=1, Queue=[(2,1)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=4
  Process (2,1): infect (2,2)
  fresh=0, Queue=[(2,2)]

Check: queue not empty && fresh>0 → FALSE (fresh=0)
  EXIT LOOP
  Return fresh==0 ? time : -1 → time=4 ✓ CORRECT!
```

**What if we removed `freshOrange > 0` from while condition?**

```text
...continuing from above...

Check: queue not empty → TRUE (Queue=[(2,2)])
  time++ → time=5  ← WRONG! Over-counting
  Process (2,2): no fresh neighbors
  Queue=[]

Return time=5 ✗ WRONG!
```

#### Decision Guide: Which Approach to Use?

**Use Approach A (time++ at beginning) when:**
- ✅ You have a clear "completion" condition (e.g., `freshOrange == 0`)
- ✅ You want simpler loop body without tracking flags
- ✅ Problem semantics: "time passes, THEN infection spreads"
- ⚠️ MUST add completion condition to while loop!

**Use Approach B (time++ at end with flag) when:**
- ✅ No clear completion condition available
- ✅ Want to be safe from over-counting
- ✅ Problem semantics: "infection spreads, THEN time passes"
- ✅ Multiple different "work" types need tracking

#### Common Patterns in Rotting-Oranges Solutions

| Version | Strategy | Key Code |
|---------|----------|----------|
| V0, V0-0-1 | time++ at beginning | `while (!q.isEmpty() && freshOrange > 0) { time++; ... }` |
| V0-0-2, V0-1, V0-4 | time++ at end with flag | `if (rottedThisMinute) time++;` |
| V1-1 | time++ at end (no flag) | `while (fresh > 0 && !q.isEmpty()) { ... } time++;` |

#### Summary — incrementing time at the start vs end of a level

| Scenario | Recommended Approach |
|----------|---------------------|
| Have completion counter (fresh oranges, keys collected) | Approach A with counter in while condition |
| No completion counter | Approach B with boolean flag |
| Want simplest correct code | Approach B (harder to get wrong) |
| Want most efficient code | Approach A (no flag overhead) |

> **Rule of Thumb:** If you use `time++` at the BEGINNING, you MUST have an early-exit condition in the while loop. Otherwise, use `time++` at the END with a flag.

---

## LC Examples

### 2-6) Open the Lock (LC 752) — BFS on State Space
> Model each lock combination as a node; BFS finds minimum turns to reach target.

```java
// LC 752 - Open the Lock
// IDEA: BFS on 4-digit combinations; each turn = 1 step
// time = O(10^4 * 4 * 2), space = O(10^4)
public int openLock(String[] deadends, String target) {
    Set<String> dead = new HashSet<>(Arrays.asList(deadends));
    Set<String> visited = new HashSet<>();
    Queue<String> queue = new LinkedList<>();
    String start = "0000";
    if (dead.contains(start)) return -1;
    queue.offer(start);
    visited.add(start);
    int steps = 0;
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String curr = queue.poll();
            if (curr.equals(target)) return steps;
            char[] chars = curr.toCharArray();
            for (int j = 0; j < 4; j++) {
                char orig = chars[j];
                for (int delta : new int[]{1, -1}) {
                    chars[j] = (char)((orig - '0' + delta + 10) % 10 + '0');
                    String next = new String(chars);
                    if (!visited.contains(next) && !dead.contains(next)) {
                        visited.add(next); queue.offer(next);
                    }
                    chars[j] = orig;
                }
            }
        }
        steps++;
    }
    return -1;
}
```

#### Variation: Sliding Puzzle (LC 773) — same state-space BFS, board flattened to a string

> **Twist**: identical skeleton to LC 752 — only the *state encoding* and the *neighbor rule* change. Serialize the 2×3 board to `"123450"`, and precompute which indices the blank (`'0'`) can swap with, so "generate neighbors" is a table lookup instead of 2D bounds math. Target `"123450"`; unreachable → `-1` (only half of the 6! = 720 permutations are reachable).

```text
index layout      swap table (neighbors of each index)
 0 1 2            0:[1,3]  1:[0,2,4]  2:[1,5]
 3 4 5            3:[0,4]  4:[1,3,5]  5:[2,4]
```

```java
// java
// LC 773 - Sliding Puzzle
// time = O(6! * 6), space = O(6!)   at most 720 board states
// IDEA: state = flattened board string; BFS levels = number of moves
public int slidingPuzzle(int[][] board) {
    StringBuilder sb = new StringBuilder();
    for (int[] row : board) for (int v : row) sb.append(v);
    String start = sb.toString(), target = "123450";
    int[][] nbr = {{1,3},{0,2,4},{1,5},{0,4},{1,3,5},{2,4}};   // precomputed adjacency
    Queue<String> q = new LinkedList<>();
    Set<String> seen = new HashSet<>();
    q.offer(start); seen.add(start);
    int steps = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String cur = q.poll();
            if (cur.equals(target)) return steps;
            int zero = cur.indexOf('0');
            for (int j : nbr[zero]) {              // slide a tile into the blank
                char[] arr = cur.toCharArray();
                char tmp = arr[zero]; arr[zero] = arr[j]; arr[j] = tmp;
                String next = new String(arr);
                if (seen.add(next)) q.offer(next);
            }
        }
        steps++;
    }
    return -1;                                     // target permutation unreachable
}
```

```python
# python
# LC 773 - Sliding Puzzle
# time = O(6! * 6), space = O(6!)
# IDEA: BFS on the flattened board string, blank '0' swaps with its table neighbors
def slidingPuzzle(board):
    start = "".join(str(x) for row in board for x in row)
    target = "123450"
    nbr = [[1,3], [0,2,4], [1,5], [0,4], [1,3,5], [2,4]]
    q = deque([start])
    seen = {start}
    steps = 0
    while q:
        for _ in range(len(q)):
            cur = q.popleft()
            if cur == target:
                return steps
            i = cur.index('0')
            for j in nbr[i]:
                lst = list(cur)
                lst[i], lst[j] = lst[j], lst[i]
                nxt = "".join(lst)
                if nxt not in seen:
                    seen.add(nxt)
                    q.append(nxt)
        steps += 1
    return -1
```

**Takeaway**: LC 752, LC 773 and LC 433 are the same template — *hash the state, define a `neighbors(state)` function, count BFS levels*. Interview value is in spotting that a puzzle/word/lock is really an implicit graph.

### 2-7) Surrounded Regions (LC 130) — BFS from Border
> BFS from all border 'O' cells; mark reachable ones safe; flip the rest.

```java
// LC 130 - Surrounded Regions
// IDEA: BFS from border O-cells to find non-surrounded regions
// time = O(M*N), space = O(M*N)
public void solve(char[][] board) {
    int m = board.length, n = board[0].length;
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if ((i==0||i==m-1||j==0||j==n-1) && board[i][j]=='O') {
                board[i][j] = 'S'; queue.offer(new int[]{i,j});
            }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] c = queue.poll();
        for (int[] d : dirs) {
            int nr=c[0]+d[0], nc=c[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&board[nr][nc]=='O') {
                board[nr][nc]='S'; queue.offer(new int[]{nr,nc});
            }
        }
    }
    for (int i=0;i<m;i++) for (int j=0;j<n;j++)
        board[i][j] = board[i][j]=='S' ? 'O' : (board[i][j]=='O' ? 'X' : board[i][j]);
}
```

### 2-8) Course Schedule (LC 207) — BFS Topological Sort (Kahn's)
> Build in-degree array; BFS processes nodes with zero in-degree iteratively.

```java
// LC 207 - Course Schedule
// IDEA: Kahn's BFS topological sort — detect cycle in directed graph
// time = O(V+E), space = O(V+E)
public boolean canFinish(int numCourses, int[][] prerequisites) {
    int[] inDegree = new int[numCourses];
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
    for (int[] pre : prerequisites) {
        adj.get(pre[1]).add(pre[0]);
        inDegree[pre[0]]++;
    }
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < numCourses; i++) if (inDegree[i] == 0) queue.offer(i);
    int processed = 0;
    while (!queue.isEmpty()) {
        int course = queue.poll();
        processed++;
        for (int next : adj.get(course))
            if (--inDegree[next] == 0) queue.offer(next);
    }
    return processed == numCourses;
}
```

### 2-9) Walls and Gates (LC 286) — Multi-source BFS
> Start BFS from all gates (0s) simultaneously; fill rooms with shortest distance.

```java
// LC 286 - Walls and Gates
// IDEA: Multi-source BFS from all gates — propagate distances
// time = O(M*N), space = O(M*N)
public void wallsAndGates(int[][] rooms) {
    int m = rooms.length, n = rooms[0].length;
    int INF = Integer.MAX_VALUE;
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if (rooms[i][j] == 0) queue.offer(new int[]{i, j});
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] cell = queue.poll();
        for (int[] d : dirs) {
            int nr = cell[0]+d[0], nc = cell[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&rooms[nr][nc]==INF) {
                rooms[nr][nc] = rooms[cell[0]][cell[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
}
```

### 2-10) Minimum Height Trees (LC 310) — BFS Leaf Trimming
> Repeatedly remove leaf nodes; the remaining 1-2 nodes are the roots of MHTs.

**Core Idea — BFS / Layer Trimming (Onion Peeling):**
- Think of the tree like an **onion**. The MHT roots are in the innermost layer
- This is **multi-source BFS from leaves inward** — NOT BFS from a single root
- Leaves = nodes with degree 1. Remove all leaves simultaneously → their neighbors may become new leaves
- Repeat until ≤ 2 nodes remain. These are the **centroids** (MHT roots)
- Why ≤ 2? A tree has at most 2 centroids (diameter even → 2, diameter odd → 1)

```text
Example: 0 - 1 - 2 - 3 - 4

Layer 1: remove 0, 4  (leaves)
Layer 2: remove 1, 3  (new leaves)
Result:  [2] ✅        (centroid)
```

**Why NOT brute force?**
- BFS from every node to compute height → O(N²) → TLE
- Leaf trimming → O(N) — each node and edge processed once

**Pattern — When to Recognize This:**

| Signal | Meaning |
|--------|---------|
| Undirected tree + find optimal root | Leaf trimming |
| Minimize max distance to any leaf | Find centroid |
| "Peel layers from outside inward" | Multi-source BFS |
| Degree-based processing on tree | Similar to Kahn's on DAG |

**Two Implementation Styles:**

Style 1 — `int[] degree` array (simpler, preferred):
```java
// LC 310 - Minimum Height Trees
// IDEA: BFS leaf trimming with degree array
// time = O(N), space = O(N)
public List<Integer> findMinHeightTrees(int n, int[][] edges) {
    if (n == 1) return Collections.singletonList(0);

    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    int[] degree = new int[n];

    for (int[] e : edges) {
        graph.get(e[0]).add(e[1]);
        graph.get(e[1]).add(e[0]);
        degree[e[0]]++;
        degree[e[1]]++;
    }

    Queue<Integer> leaves = new LinkedList<>();
    for (int i = 0; i < n; i++)
        if (degree[i] == 1) leaves.offer(i);

    int remaining = n;
    while (remaining > 2) {
        int size = leaves.size();
        remaining -= size;
        for (int i = 0; i < size; i++) {
            int leaf = leaves.poll();
            for (int nei : graph.get(leaf)) {
                degree[nei]--;
                if (degree[nei] == 1) leaves.offer(nei);
            }
        }
    }
    return new ArrayList<>(leaves);
}
```

Style 2 — `Set<Integer>` adjacency (O(1) removal, tracks actual edges):
```java
// LC 310 - Using Set for adjacency
// time = O(N), space = O(N)
public List<Integer> findMinHeightTrees_set(int n, int[][] edges) {
    if (n == 1) return Collections.singletonList(0);
    List<Set<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new HashSet<>());
    for (int[] e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
    Queue<Integer> leaves = new LinkedList<>();
    for (int i = 0; i < n; i++) if (adj.get(i).size() == 1) leaves.offer(i);
    int remaining = n;
    while (remaining > 2) {
        int size = leaves.size();
        remaining -= size;
        for (int i = 0; i < size; i++) {
            int leaf = leaves.poll();
            int neighbor = adj.get(leaf).iterator().next();
            adj.get(neighbor).remove(leaf);
            if (adj.get(neighbor).size() == 1) leaves.offer(neighbor);
        }
    }
    return new ArrayList<>(leaves);
}
```

**Classic Similar LCs:**

| LC # | Problem | Connection |
|------|---------|------------|
| 310 | Minimum Height Trees | Core leaf trimming problem |
| 207 | Course Schedule | Kahn's algo — same BFS + degree pattern on DAG |
| 210 | Course Schedule II | Kahn's with ordering output |
| 834 | Sum of Distances in Tree | Tree centroid / rerooting DP |
| 1245 | Tree Diameter | Diameter → centroid is at midpoint |
| 2603 | Collect Coins in a Tree | Leaf trimming to prune unnecessary nodes |
| 863 | All Nodes Distance K in Binary Tree | BFS on tree structure |
| 994 | Rotting Oranges | Multi-source BFS (same layer-by-layer pattern) |
| 542 | 01 Matrix | Multi-source BFS from all zeros |

### 2-11) Snakes and Ladders (LC 909) — BFS on Board
> Model board as graph; BFS finds minimum dice rolls to reach final square.

```java
// LC 909 - Snakes and Ladders
// IDEA: BFS — each square is a node, dice roll = edges
// time = O(N^2), space = O(N^2)
public int snakesAndLadders(int[][] board) {
    int n = board.length;
    int[] flat = new int[n * n + 1];
    int idx = 1; boolean leftToRight = true;
    for (int r = n-1; r >= 0; r--) {
        if (leftToRight) for (int c = 0; c < n; c++) flat[idx++] = board[r][c];
        else for (int c = n-1; c >= 0; c--) flat[idx++] = board[r][c];
        leftToRight = !leftToRight;
    }
    boolean[] visited = new boolean[n*n+1];
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{1, 0});
    visited[1] = true;
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int pos = curr[0], steps = curr[1];
        for (int dice = 1; dice <= 6 && pos+dice <= n*n; dice++) {
            int next = pos + dice;
            if (flat[next] != -1) next = flat[next];
            if (next == n*n) return steps + 1;
            if (!visited[next]) { visited[next] = true; queue.offer(new int[]{next, steps+1}); }
        }
    }
    return -1;
}
```

### 2-13) Pacific Atlantic Water Flow (LC 417) — BFS from Both Oceans
> BFS backward from Pacific and Atlantic borders; cells in both sets can flow to both.

```java
// LC 417 - Pacific Atlantic Water Flow
// IDEA: BFS from Pacific border + Atlantic border; intersection = answer
// time = O(M*N), space = O(M*N)
public List<List<Integer>> pacificAtlantic(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
    Queue<int[]> pq = new LinkedList<>(), aq = new LinkedList<>();
    for (int i = 0; i < m; i++) {
        pq.offer(new int[]{i,0}); pac[i][0]=true;
        aq.offer(new int[]{i,n-1}); atl[i][n-1]=true;
    }
    for (int j = 0; j < n; j++) {
        pq.offer(new int[]{0,j}); pac[0][j]=true;
        aq.offer(new int[]{m-1,j}); atl[m-1][j]=true;
    }
    bfs(heights, pq, pac, m, n);
    bfs(heights, aq, atl, m, n);
    List<List<Integer>> res = new ArrayList<>();
    for (int i=0;i<m;i++) for (int j=0;j<n;j++)
        if (pac[i][j]&&atl[i][j]) res.add(Arrays.asList(i,j));
    return res;
}
private void bfs(int[][] h, Queue<int[]> q, boolean[][] visited, int m, int n) {
    int[][] dirs={{1,0},{-1,0},{0,1},{0,-1}};
    while (!q.isEmpty()) {
        int[] c=q.poll();
        for (int[] d:dirs) {
            int nr=c[0]+d[0],nc=c[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&!visited[nr][nc]&&h[nr][nc]>=h[c[0]][c[1]]) {
                visited[nr][nc]=true; q.offer(new int[]{nr,nc});
            }
        }
    }
}
```

### 2-14) Perfect Squares (LC 279) — BFS on Abstract Graph (Number Decomposition)
> BFS from `n` toward `0`; each level subtracts a perfect square. First time we reach 0 = minimum count.

```java
// LC 279 - Perfect Squares
// IDEA: BFS — treat each number as a node, edges = subtracting a perfect square
// time = O(N * sqrt(N)), space = O(N)
public int numSquares(int n) {
    // Pre-calculate perfect squares up to n
    List<Integer> squares = new ArrayList<>();
    for (int i = 1; i * i <= n; i++) {
        squares.add(i * i);
    }

    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(n);
    visited.add(n);

    int level = 0;

    while (!queue.isEmpty()) {
        level++;
        int size = queue.size();

        for (int i = 0; i < size; i++) {
            int remaining = queue.poll();

            for (int square : squares) {
                int nextVal = remaining - square;

                if (nextVal == 0)
                    return level; // Found shortest path
                if (nextVal < 0)
                    break; // Squares are sorted, so we can stop

                if (!visited.contains(nextVal)) {
                    visited.add(nextVal);
                    queue.offer(nextVal);
                }
            }
        }
    }
    return -1;
}
```

### 2-15) Closest Leaf in a Binary Tree (LC 742) — Tree → Graph + BFS ⭐⭐⭐⭐
> "Closest" = fewest **edges** on a binary tree. The catch: from the target you may need
> to walk **upward** (to a parent) as well as downward (to children). A plain tree only
> has child pointers, so first **convert the tree into an undirected graph** (each node ↔
> its parent and children), then run a normal BFS from the target — the **first leaf
> popped is the answer** (BFS on an unweighted graph gives shortest #edges).

**1) Core Idea**

- **DFS to build an undirected graph** + record the `target` node + collect `leaves`.
  - For each node, add edges *both ways*: `graph[node]→parent` and `graph[parent]→node`.
  - This is the crucial step — it makes the parent reachable, so "going up" becomes a normal edge.
- **BFS from the target node**; the first node popped that is a leaf (no children) is closest.
  - Equal-weight edges ⇒ BFS guarantees minimal edge count; no need to track distances.

```python
# python — LC 742 (DFS build graph + BFS from target)
from collections import defaultdict, deque

class Solution(object):
    def findClosestLeaf(self, root, k):
        graph = defaultdict(list)   # node -> [neighbors]  (undirected)
        leaves = set()
        target = [None]

        def build(node, parent):
            if not node:
                return
            if node.val == k:
                target[0] = node
            if parent:                          # connect BOTH directions
                graph[node].append(parent)
                graph[parent].append(node)
            if not node.left and not node.right: # leaf = no children
                leaves.add(node)
            build(node.left, node)
            build(node.right, node)

        build(root, None)

        # BFS from target; first leaf reached is the closest
        q = deque([target[0]])
        visited = {target[0]}
        while q:
            node = q.popleft()
            if node in leaves:
                return node.val                  # earliest pop = fewest edges
            for nei in graph[node]:
                if nei not in visited:
                    visited.add(nei)
                    q.append(nei)
```

**2) Why BFS (not DFS)?**

| | |
|---|---|
| Goal | **minimum #edges** target → any leaf |
| Edge weights | all equal (1) ⇒ BFS layer = exact distance |
| Why graph, not tree | answer leaf may be *above* the target → need parent edges |
| Why "first leaf wins" | BFS pops nodes in nondecreasing distance order |

```text
Tree (k=2):                 As undirected graph, BFS from 2:
       1                    dist 0: 2
      / \                   dist 1: 4, 1
     2   3   (leaf)         dist 2: 5, 3(leaf) <- returned (3 closer than the 5→6 chain)
    /
   4
  /
 5
/
6 (leaf)
```

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 742 | Closest Leaf in a Binary Tree | this — tree→graph, BFS to nearest leaf |
| 863 | All Nodes Distance K in Binary Tree | same tree→graph trick, BFS K levels out — Pattern 11 / §2-18 |
| 1192 | Critical Connections | tree/graph as undirected, edge traversal |
| 994 | Rotting Oranges | multi-source BFS, "first reach = min dist" idea |
| 542 | 01 Matrix | BFS shortest distance on unweighted grid |

> **Pattern takeaway**: whenever a *tree* problem needs movement **upward (toward parent)**,
> convert it to an **undirected graph** (add parent links via DFS) and switch to graph BFS/DFS.
> This "tree → graph" reframing is the key to LC 742 and LC 863.

### 2-16) Populating Next Right Pointers (LC 116 / 117) — Level BFS wires the `next` links ⭐⭐⭐⭐

> Each node has a `next` pointer that should point to the node **immediately to its right on
> the same level** (or `NULL` if it's the rightmost). This is just a **level-order BFS**: while
> processing one level, chain each node to the one dequeued after it. The follow-up ("O(1)
> extra space") reuses the `next` pointers you just built as a **linked list of the level above**
> to wire the level below — no queue needed.

**1) Core Idea**

- **`next` = "the node to my right on the same level."** So process the tree **level by level**
  and, inside each level, link `prev.next = cur` as you pop nodes off the queue.
- The **last node of every level** gets `next = None` (queue is emptied per level, so it never
  points into the next level).
- Works for both LC 116 (perfect tree) and LC 117 (any binary tree) — BFS doesn't care about
  the tree shape; children are simply enqueued when they exist.
- **O(1)-space follow-up**: once level *L* is fully linked, walk it via `next` pointers as if it
  were a linked list and set the `next` pointers of level *L+1* — recycling the structure you
  already built instead of a queue.

**2) Pattern**

```python
# python — LC 116/117: BFS by layer, chain nodes via prev pointer
from collections import deque

class Solution(object):
    def connect(self, root):
        # time = O(N), space = O(W)  (W = max width / one level)
        if not root:
            return None

        q = deque([root])
        while q:
            size = len(q)
            prev = None
            for _ in range(size):          # one full level per outer iteration
                cur = q.popleft()
                if prev:                    # link previous node -> current
                    prev.next = cur
                prev = cur
                if cur.left:
                    q.append(cur.left)
                if cur.right:
                    q.append(cur.right)
            prev.next = None                # last node of the level -> NULL
        return root
```

**Alternative (peek at queue front instead of tracking `prev`):**

```python
# python — same BFS, use i < size - 1 to point at the next node still in queue
for i in range(size):
    cur = q.popleft()
    if i < size - 1:
        cur.next = q[0]                     # front of queue = node to the right
    if cur.left:  q.append(cur.left)
    if cur.right: q.append(cur.right)
```

**Follow-up — O(1) space (perfect tree, LC 116):** reuse `next` links, no queue.

```python
# python — walk each level as a linked list to wire the next level
class Solution(object):
    def connect(self, root):
        # time = O(N), space = O(1)
        if not root:
            return None
        leftmost = root
        while leftmost.left:               # stop once we reach the leaf level
            head = leftmost
            while head:
                head.left.next = head.right             # (1) same parent
                if head.next:
                    head.right.next = head.next.left    # (2) across parents
                head = head.next                        # move right via existing links
            leftmost = leftmost.left        # drop to next level's leftmost
        return root
```

```text
Visual (LC 116):
        1 -> NULL
      /   \
     2  -> 3 -> NULL
    / \   / \
   4-> 5->6->7 -> NULL

BFS level 2: prev walks 4→5→6→7, chaining next; last (7) -> NULL.
O(1) trick: from level [2,3], (1) 2.left→2.right = 4→5, (2) 2.right→2.next.left = 5→6, ...
```

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 116 | Populating Next Right Pointers in Each Node | this — **perfect** tree, BFS or O(1) `next`-reuse |
| 117 | Populating Next Right Pointers II | same BFS; tree **not** perfect, so O(1) version needs a dummy head per level |
| 102 | Binary Tree Level Order Traversal | the base level-BFS this is built on |
| 199 | Binary Tree Right Side View | rightmost node per level = last node before `next = None` |
| 314 | Binary Tree Vertical Order Traversal | level BFS grouping, but keyed by column not by `next` |

> **Pattern takeaway**: "point to the node on my right" ⇒ **level-order BFS**, linking nodes in
> dequeue order and terminating each level with `next = None`. For O(1) space, treat the
> already-linked level as a linked list to build the one below.

### 2-17) Add One Row to Tree (LC 623) — Level BFS stops at `depth - 1` and rewires ⭐⭐⭐⭐

> Insert a row of `val` nodes at `depth`. The trick: you don't act at `depth`, you act at
> **`depth - 1`** — that's the *parent* row whose pointers must be rewired. So run a plain
> level-order BFS, counting levels, and **stop as soon as `cur_depth == depth - 1`**; for every
> node in that level, splice in two new nodes and **reattach the original subtrees**
> (`old_left` under `new_left.left`, `old_right` under `new_right.right`).

**1) Core Idea**

- **BFS is a natural fit** because the operation is defined *per level* — exactly what
  level-by-level BFS (`size = len(q)`) gives you. No parent pointers, no recursion depth needed.
- **Edge case first: `depth == 1`.** There is no level 0 to rewire, so create a new root and
  hang the whole original tree as its **left** child, then return the new root.
- **Cache before overwrite.** `node.left = TreeNode(val)` destroys the original pointer, so
  save `old_left`/`old_right` *first*. This is the one line that breaks the solution if skipped.
- **Asymmetric reattach**: original left subtree goes to `new_left.left`, original right subtree
  goes to `new_right.right` — the outer sides — so the tree keeps its left/right shape.
- **Return / break immediately** after rewiring the level. Continuing the BFS would walk into the
  brand-new nodes and (worse) the queue no longer reflects the pre-insert tree.
- Nodes with `None` children still get **two** new children (whose own children are `None`) —
  the rule applies to every non-null node at `depth - 1`, not just to nodes that had children.

**2) Pattern**

```python
# python — LC 623 Add One Row to Tree (level BFS, stop at depth-1)
# time = O(N), space = O(W)   N = #nodes, W = max level width
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        # (1) no `depth - 1` row exists -> new node becomes the new root
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        q = deque([root])
        cur_depth = 1                       # root is at depth 1 (NOT 0)

        while q:
            size = len(q)

            # NOTE !!! treat `cur_depth == depth - 1` as a SEPARATE path:
            #          inside it we rewire instead of descending, then stop
            if cur_depth == depth - 1:
                for _ in range(size):
                    node = q.popleft()

                    old_left = node.left     # (2) cache BEFORE overwriting
                    old_right = node.right

                    node.left = TreeNode(val)   # (3) splice the new row in
                    node.right = TreeNode(val)

                    node.left.left = old_left   # (4) reattach on OUTER sides
                    node.right.right = old_right
                break                        # (5) done — never descend further

            # otherwise: ordinary level-order descent
            for _ in range(size):
                node = q.popleft()
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            cur_depth += 1

        return root
```

**Variant — single loop, `if/else` inside** (same logic, one pass over the level):

```python
# python — branch per node instead of per level; break after the level finishes
while q:
    size = len(q)
    for _ in range(size):
        node = q.popleft()
        if cur_depth == depth - 1:
            old_left, old_right = node.left, node.right
            node.left, node.right = TreeNode(val), TreeNode(val)
            node.left.left = old_left
            node.right.right = old_right
        else:
            if node.left:  q.append(node.left)
            if node.right: q.append(node.right)

    if cur_depth == depth - 1:   # break AFTER the whole level is rewired
        break
    cur_depth += 1
```

**Alternative — DFS recursion** (shorter, but O(h) stack — full treatment in [dfs.md §2-31](./dfs.md)):

```python
# python — recurse down to d == 2, then rewire that node's children
# time = O(N), space = O(h)
class Solution(object):
    def addOneRow(self, root, v, d):
        if not root:
            return None
        if d == 1:                                   # new root
            new_root = TreeNode(v)
            new_root.left = root
            return new_root
        if d == 2:                                   # root IS the depth-1 parent
            root.left,  root.left.left   = TreeNode(v), root.left
            root.right, root.right.right = TreeNode(v), root.right
            return root
        root.left  = self.addOneRow(root.left,  v, d - 1)
        root.right = self.addOneRow(root.right, v, d - 1)
        return root
```

```text
Visual — root = [4,2,6,3,1,5], val = 1, depth = 2   (rewire level depth-1 = 1, i.e. node 4)

before                    cache 4's children       after (new row of 1s)
      4                   old_left  = 2                  4
     / \                  old_right = 6                 / \
    2   6                                              1   1
   / \   \                4.left  = new 1              /     \
  3   1   5               4.right = new 1             2       6
                          1.left  = 2  (outer)       / \       \
                          1.right = 6  (outer)      3   1       5

depth == 1 case: brand-new node becomes root, whole old tree hangs on its LEFT.
```

**Common pitfalls**

| Pitfall | Why it breaks |
|---|---|
| Stopping at `cur_depth == depth` | too late — the pointers to rewire live on the **parent** row |
| Overwriting `node.left` before caching | original subtree is lost (unreachable) forever |
| `new_left.right = old_left` (inner sides) | mirrors the tree; must be `new_left.left` / `new_right.right` |
| Forgetting `depth == 1` | `depth - 1 == 0` is never reached, so nothing is inserted |
| Not breaking after the rewire | BFS descends into the freshly created `val` nodes |
| Starting `cur_depth = 0` | off-by-one — problem defines the **root as depth 1** |

**3) Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 623 | Add One Row to Tree | this — level BFS to `depth - 1`, then rewire pointers |
| 102 | Binary Tree Level Order Traversal | the base level-BFS loop (`size = len(q)`) |
| 199 | Binary Tree Right Side View | same level loop, pick last node per level |
| 116 / 117 | Populating Next Right Pointers | §2-16 — level BFS that also **mutates pointers** |
| 971 | Flip Binary Tree To Match Preorder | swap left/right children while traversing |
| 226 | Invert Binary Tree | cache-then-swap child pointers (same aliasing hazard) |

> **Pattern takeaway**: when a tree problem says "do X at depth `d`", the row you actually
> **mutate is `d - 1`** — level BFS there, cache the old children before assigning, reattach
> them on the **outer** sides, and stop immediately so you never traverse your own new nodes.

### 2-18) All Nodes Distance K in Binary Tree (LC 863) — Parent map + BFS radiating outward ⭐⭐⭐⭐⭐

> Distance is measured from **`target`**, not from the root, so the answer can lie **below**,
> **above**, or in a **sibling subtree** of the target. Record `{node: parent}` with one DFS to
> supply the missing "up" edge, then BFS from `target` where each node has **3 neighbors:
> `left`, `right`, `parent`**. Every edge costs 1 ⇒ BFS level == tree distance.
> Full write-up: **Pattern 11** in [bfs.md](./bfs.md).

**Shape B — expand exactly `k` levels, then the queue IS the answer**

> Shape A (carry `(node, dist)` in the queue) is the canonical template in [bfs.md](./bfs.md) → **Pattern 11**.

```python
# python — shape B: expand exactly k levels, then the queue IS the answer
# (no distance stored; naturally returns [] when the tree is smaller than k)
# time = O(n), space = O(n)
class Solution(object):
    def distanceK(self, root, target, k):
        parents = {}
        def add_parents(node, parent):
            if not node:
                return
            parents[node] = parent
            add_parents(node.left, node)
            add_parents(node.right, node)
        add_parents(root, None)

        q = collections.deque([target])
        visited = {target}
        for _ in range(k):                     # k full level expansions
            for _ in range(len(q)):            # snapshot the level size FIRST
                node = q.popleft()
                for nxt in (node.left, node.right, parents[node]):
                    if nxt and nxt not in visited:
                        visited.add(nxt)
                        q.append(nxt)
        return [node.val for node in q]        # everything left is exactly k away
```

```java
// java — shape B: k level expansions, remaining queue = answer
// LC 863 - All Nodes Distance K in Binary Tree
// time = O(n), space = O(n)
public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    Map<TreeNode, TreeNode> parents = new HashMap<>();
    build(root, null, parents);

    Queue<TreeNode> q = new LinkedList<>();
    q.offer(target);
    Set<TreeNode> visited = new HashSet<>();
    visited.add(target);

    for (int step = 0; step < k; step++) {
        int size = q.size();                    // snapshot: level boundary
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            for (TreeNode nei : new TreeNode[]{node.left, node.right, parents.get(node)}) {
                if (nei != null && visited.add(nei)) q.offer(nei);
            }
        }
    }
    List<Integer> ans = new ArrayList<>();
    for (TreeNode node : q) ans.add(node.val);  // may be empty → correct
    return ans;
}

private void build(TreeNode node, TreeNode parent, Map<TreeNode, TreeNode> parents) {
    if (node == null) return;
    parents.put(node, parent);
    build(node.left, node, parents);
    build(node.right, node, parents);
}
```

| | Shape A `(node, dist)` | Shape B `k` level expansions |
|---|---|---|
| Distance tracking | stored per queue entry | implicit in the loop counter |
| Collect answer | when `dist == k` | whatever remains in the queue |
| `k = 0` | returns `[target.val]` ✅ | loop skipped, queue = `[target]` ✅ |
| `k >` tree height | naturally `[]` ✅ | queue drains to empty → `[]` ✅ |
| Best for | also need distances / early exit | terse, matches "level = distance" intuition |

**Common pitfalls**

| Pitfall | Why it breaks |
|---|---|
| No `visited` set | parent edge makes it undirected → `5 → 3 → 5 → 3 …` infinite bounce |
| Marking visited **at pop** instead of at enqueue | same node enqueued via 2 paths ⇒ duplicates in the answer |
| Expanding after `dist == k` | wasted work; with a sloppy `visited` it also collects nodes at `k+1` |
| Keying the map/`visited` by `node.val` | fine here (values are unique per constraints) but **breaks on duplicate values** — prefer node identity |
| `parents[node]` on a node never DFS'd | `KeyError` — build parents from `root`, not from `target` |
| Forgetting `k = 0` | answer is `[target.val]`, not `[]` |
| Recomputing `len(q)` inside the level loop (shape B) | queue grows mid-level → mixes distances `k` and `k+1` |

**Similar LC**

| LC | Problem | Relation |
|----|---------|----------|
| 863 | All Nodes Distance K in Binary Tree | this — parent map + BFS `k` steps out |
| 2385 | Amount of Time for Binary Tree to Be Infected | identical setup; answer = number of BFS levels (max distance) |
| 742 | Closest Leaf in a Binary Tree | §2-15 — BFS out from target, first leaf popped wins |
| 1740 | Find Distance in a Binary Tree | BFS from node `p` until `q` pops out |
| 1530 | Number of Good Leaf Nodes Pairs | Pattern 10 — bounded BFS from every leaf |
| 993 | Cousins in Binary Tree | parent + depth per node (no radiation needed) |
| 236 | LCA of a Binary Tree | the LCA is where the up-then-down path turns around |
| 542 / 994 | 01 Matrix / Rotting Oranges | same "BFS layer == distance" engine on a grid |

## Summary

| If the problem says… | Reach for | Worked here |
|---|---|---|
| "minimum turns / moves on a puzzle or lock" | hash the state, `neighbors(state)`, count BFS levels | §2-6 (LC 752 / LC 773), §2-11 (LC 909) |
| "regions **not** touching the border" | BFS **inward from the border**, flip what you never reached | §2-7 (LC 130) |
| "distance from every gate / ocean / zero" | multi-source BFS seeded with all sources | §2-9 (LC 286), §2-13 (LC 417) |
| "can all courses be finished" | Kahn's in-degree BFS | §2-8 (LC 207) |
| "fewest perfect squares / coins summing to n" | BFS on an abstract number graph | §2-14 (LC 279) |
| "the root that minimises tree height" | leaf trimming inward until ≤ 2 nodes remain | §2-10 (LC 310) |
| "closest leaf / distance k **from a node**" | tree → undirected graph (parent edges), then BFS out | §2-15 (LC 742), §2-18 (LC 863) |
| "wire / insert / mutate a whole tree level" | level BFS, act inside the `for _ in range(len(q))` body | §2-16 (LC 116/117), §2-17 (LC 623) |
| "how many minutes until everything is X" | level = one unit of time — mind where `time++` goes | *Level Counting & Timing* (LC 994) |

Every template these instantiate is in [bfs.md](./bfs.md); the rarer variants are in [bfs_advanced.md](./bfs_advanced.md).
