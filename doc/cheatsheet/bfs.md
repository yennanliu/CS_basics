# BFS (Breadth-First Search)

> **Scope** — The main BFS reference: the queue templates, level-by-level expansion, grid and multi-source BFS, and why first visit equals shortest path on an unweighted graph; the heavier variants and the long tail of worked problems live in their own sheets.
> **See also** — *deep dives split out of this file*: [bfs_advanced.md](./bfs_advanced.md) — bidirectional BFS, 0-1 BFS with a deque, state-space / implicit-graph BFS, all-shortest-path DAG enumeration, and the multi-source vs independent-runs distinction; [bfs_examples.md](./bfs_examples.md) — the classic problems (LC 116-117 / 130 / 279 / 286 / 623 / 662 / 742 / 752 / 773 / 909 / 958), grouped by this sheet's templates.
> *Neighbouring sheets*: [dfs.md](./dfs.md) — the depth-first counterpart and how to choose; [graph.md](./graph.md) — representation and the graph catalogue; [Dijkstra.md](./Dijkstra.md) — once edges have weights; [topology_sorting.md](./topology_sorting.md) — Kahn's algorithm is BFS.

## LeetCode Problem Lists

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Overview
Breadth-First Search is a graph traversal algorithm that explores nodes level by level, visiting all nodes at the current depth before moving to nodes at the next depth.

### Key Properties
- **Complete**: Always finds a solution if one exists
- **Optimal**: Finds shortest path in `unweighted` graphs
- **Complexity**: see the [Time & Space Complexity](#time--space-complexity) table below

### Core Characteristics
- Uses **Queue** data structure (FIFO - First In, First Out)
- Guarantees **shortest path** in unweighted graphs
- Explores nodes **level by level** (breadth first, then depth)
- Memory intensive compared to DFS

### Node States (for cycle detection)
- **State 0**: Not visited (white)
- **State 1**: Currently being processed (gray) 
- **State 2**: Completely processed (black)

### BFS vs DFS

#### BFS (Breadth-First Search)
- Uses a **Queue**
- Order: **FIFO** (First In, First Out)
- How it works: Visit a node → Add all neighbors to queue → Process in order added
- Think: **level by level traversal**

#### DFS (Depth-First Search)
- Uses a **Stack** (explicitly or via recursion)
- Order: **FILO / LIFO** (Last In, First Out)
- How it works: Go as deep as possible along one path → Backtrack when needed
- Think: **go deep first, then backtrack**

| Aspect | BFS | DFS |
|--------|-----|-----|
| Data Structure | Queue (FIFO) | Stack / Recursion (LIFO) |
| Traversal Order | Level by level | Deep path first, then backtrack |
| Memory | O(w) — width of tree | O(h) — height of tree |
| Shortest Path | ✅ Yes (unweighted) | ❌ No |
| Complete | ✅ Yes | ❌ No (infinite spaces) |
| When to Use | Shortest path, level traversal | Explore all paths, topological sort, cycle detection |

## Recognition

The tell for BFS is a **minimum count of equal-cost moves**, or anything said "level by level".
Read the constraints against the [constraint ruler](./complexity_cheatsheet.md#6-interview-decision-guide):
BFS is O(V + E), so it fits any graph or grid up to about 10⁶ cells.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "level order", "each level", "right side view", "minimum depth" | nodes ≤ 10⁵ | [Template 1](#template-1-level-order-bfs--lc-102-) — level-order BFS | a value rolled up from the leaves → [dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) |
| "minimum number of steps / edges", "is there a path", every move costs 1 | V + E ≤ 10⁵ | [Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — graph BFS, mark on enqueue | moves have different costs → [Dijkstra.md](./Dijkstra.md); costs 0 or 1 → 0-1 BFS ([bfs_advanced.md](./bfs_advanced.md)) |
| "shortest path in a binary matrix / maze" | grid ≤ 10⁶ cells | [Template 3](#template-3-grid-bfs-with-a-direction-array--lc-1091-) — grid BFS with `dirs` | the path may cost per cell (effort, height) → Dijkstra (LC 1631) |
| "rotting", "spreads from every source at once", "distance to the nearest 0 / gate" | grid ≤ 10⁵ cells | [Template 4](#template-4-multi-source-bfs--lc-994-) — multi-source BFS | each source's distances are **summed** → independent runs (LC 317, [bfs_advanced.md](./bfs_advanced.md)) |
| "transform one word / lock state into another, one change per step" | L ≤ 10, N ≤ 5,000 | [Template 5](#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — BFS over generated neighbours | every shortest sequence is wanted → BFS + DFS (LC 126) |
| "nodes at distance k from `target`", "time to infect the tree from `start`" | nodes ≤ 10⁵ | [Template 6](#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) — parent map, then BFS | distance from the **root** → Template 1 |
| "split into two groups", "is it bipartite" | n ≤ 100 | [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — BFS 2-colouring | three or more groups → backtracking colouring |
| "shortest path with at most k obstacles removed", "collect all keys" | state ≤ 10⁶ | Template 2 over `(cell, resource)` states ([bfs_advanced.md](./bfs_advanced.md) Pattern 16) | the resource is unlimited → plain grid BFS |

## Time & Space Complexity

### BFS Time Complexity Analysis

BFS time complexity depends on the graph representation:

#### Graph Representations

**Adjacency List (most common in practice):**
- Each vertex is enqueued/dequeued once → O(V)
- Each edge is explored at most once → O(E)
- ✅ **Total = O(V + E)**

**Adjacency Matrix:**
- Checking all neighbors of a vertex costs O(V)
- Doing this for all vertices costs O(V²)
- ✅ **Total = O(V²)**

#### Detailed Breakdown by Data Structure

**Tree BFS**
- **Time**: O(n) - visit each node once
- **Space**: O(w) - maximum width of tree
- **Explanation**: Each node visited exactly once, queue stores at most one level

**Graph BFS (Adjacency List)**
- **Time**: O(V + E) - visit each vertex and edge once
- **Space**: O(V) - queue and visited set
- **Explanation**:
  - Vertex processing: Each vertex enqueued/dequeued once = O(V)
  - Edge processing: Each edge examined once = O(E)
  - Queue space: At most O(V) vertices
  - Visited set: O(V) vertices

**Graph BFS (Adjacency Matrix)**
- **Time**: O(V²) - check all possible edges
- **Space**: O(V) - queue and visited set
- **Explanation**:
  - For each vertex, check all V possible neighbors
  - Total vertices × neighbors per vertex = V × V = O(V²)

**Grid BFS**
- **Time**: O(m × n) - visit each cell once
- **Space**: O(m × n) - worst case queue size
- **Explanation**:
  - Each cell visited at most once
  - Queue can contain at most all cells in worst case
  - Grid is essentially a graph with m×n vertices and 4-directional edges

#### Performance Comparison Table

| Graph Type | Representation | Time Complexity | Space Complexity | Best For |
|------------|----------------|-----------------|------------------|----------|
| **Sparse Graph** | Adjacency List | O(V + E) | O(V) | E << V² |
| **Dense Graph** | Adjacency Matrix | O(V²) | O(V²) | E ≈ V² |
| **Tree** | Parent-Child Links | O(n) | O(w) | Hierarchical data |
| **Grid** | 2D Array | O(m × n) | O(m × n) | Spatial problems |

#### Why O(V + E) for Adjacency List?

```python
# Detailed analysis of BFS with adjacency list
def bfs_analysis(graph, start):
    queue = deque([start])        # O(1)
    visited = {start}             # O(1)

    while queue:                  # Executes at most V times
        vertex = queue.popleft()  # O(1) - each vertex dequeued once

        # This inner loop runs exactly deg(vertex) times
        for neighbor in graph[vertex]:  # Total across all vertices = E
            if neighbor not in visited:  # O(1) with set
                visited.add(neighbor)    # O(1) - each vertex added once
                queue.append(neighbor)   # O(1) - each vertex enqueued once

    # Analysis:
    # - Outer while loop: O(V) iterations
    # - Inner for loop: Sum of deg(v) for all v = 2E (undirected) or E (directed)
    # - Each operation inside: O(1)
    # Total: O(V + E)
```

## Implementation Patterns

> The two satellite sheets keep their own `Pattern N` numbers: **Patterns 4.5, 4.6, 6, 8, 8.5, 9, 12, 15 and 16** are deep dives in [bfs_advanced.md](./bfs_advanced.md), and the worked examples are in [bfs_examples.md](./bfs_examples.md), grouped by the templates below. When either sheet cites a pattern that lives here, it uses this sheet's old numbering:
>
> | Old name | Card here |
> |---|---|
> | Pattern 1 (basic tree BFS), Pattern 2 (level by level) | [Template 1](#template-1-level-order-bfs--lc-102-) |
> | Pattern 3 (graph BFS), Pattern 3.1 (mark before enqueue), Pattern 5 (path tracking) | [Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-) |
> | Pattern 3.2 (direction array) | [Template 3](#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
> | Pattern 4 (multi-source) | [Template 4](#template-4-multi-source-bfs--lc-994-) |
> | Pattern 7 (word ladder) | [Template 5](#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) |
> | Pattern 11 (parent map, radiate from a target) | [Template 6](#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) |
> | Pattern 13 (2-colouring) | [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) |

### Template 1: Level-Order BFS — LC 102 ⭐⭐⭐⭐⭐

**Recognise** — "level by level", "level order", "right side view", "zigzag", "average of each
level", "minimum depth". Nodes ≤ 2,000 (LC 102) to 10⁵ → O(n) expected.

**Key point** — at the top of each `while` pass the queue holds exactly one whole level, so
`len(queue)` read **before** the inner loop is that level's size; the children appended during
the inner loop are the next level, waiting behind it.

```python
# python
# LC 102 - Binary Tree Level Order Traversal
# IDEA: freeze len(queue) = size of the current level, pop exactly that many, enqueue their children
# time = O(n), space = O(w)   w = the widest level
from collections import deque

def levelOrder(root):
    if not root:
        return []
    levels = []
    queue = deque([root])
    while queue:
        level_size = len(queue)              # the whole current level, and nothing else
        current_level = []
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        levels.append(current_level)
    return levels
```

```java
// java
// LC 102 - Binary Tree Level Order Traversal
// IDEA: freeze queue.size() = size of the current level, poll exactly that many
// time = O(n), space = O(w)
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> levels = new ArrayList<>();
    if (root == null) {
        return levels;
    }
    Deque<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        int levelSize = queue.size();        // read once: the queue grows inside the loop
        List<Integer> currentLevel = new ArrayList<>();
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        levels.add(currentLevel);
    }
    return levels;
}
```

**Complexity**
- **Time O(n)**: each node is appended once and popped once; the inner `for` only splits the
  same n pops into levels.
- **Space O(w)**: the queue never holds more than one level plus the part of the next one built so
  far. A complete tree's last level is about n/2, so the worst case is O(n).

**Dry run**

```text
root = [3,9,20,null,null,15,7]
queue [3]        level_size=1  pop 3, push 9 20      levels [[3]]
queue [9,20]     level_size=2  pop 9; pop 20, push 15 7   levels [[3],[9,20]]
queue [15,7]     level_size=2  pop 15; pop 7          levels [[3],[9,20],[15,7]]
```

**Traps**
- Java `for (int i = 0; i < queue.size(); i++)` re-reads a size that grows as children are
  offered: `[1,2,3]` returns `[[1,2],[3]]`. Freeze the size in a variable first.
- `list.pop(0)` as the queue costs O(n) per pop, so O(n²) on a 10⁵-node tree. Use
  `deque.popleft()`.
- Empty tree: `[]` must return `[]`, not `[[]]` — the `if not root` guard.
- When levels are not needed, drop the inner `for`: plain BFS order is this template without
  `level_size`.

**Not this if** — the answer is aggregated up from the leaves (height, diameter, balanced) →
post-order DFS ([dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-)).
LC 104 Maximum Depth works as a level count, but recursion is shorter.

**Drill** — LC 102 ⭐ · LC 111 *(return at the first leaf: variation below)* · LC 199 *(keep the
last node of each level)* · LC 103 *(reverse every other level)* · LC 662 *(carry a heap index)*

#### Minimum Depth of Binary Tree — LC 111

> **Key point** — levels come out of the queue in depth order, so the **first leaf dequeued is
> the shallowest leaf**: return its depth on the spot. A DFS has to visit every node before it
> knows the minimum; BFS stops at the first leaf.

**Reduces to**: [Template 1](#template-1-level-order-bfs--lc-102-) — with an early return at the
first node that has no children.

Worked in one place, with its traps: [tree_examples.md § LC 111](./tree_examples.md#minimum-depth-of-binary-tree--lc-111).

**Traps**
- A node with one child is **not** a leaf: `[1, 2]` has minimum depth 2, not 1. The test is
  "no left **and** no right".
- LC 104 Maximum Depth is the opposite case: every level must be seen, so recursion is shorter.

> Two further level-BFS variations — carrying a **heap index** (LC 662) and **enqueuing the `null` children** (LC 958) — are in [bfs_examples.md](./bfs_examples.md).

### Template 2: Graph BFS, Mark on Enqueue — LC 1971 ⭐⭐⭐⭐⭐

**Recognise** — "is there a path", "minimum number of steps / edges / moves", every move costs the
same, and the graph arrives as an edge list or an adjacency list. V + E ≤ 4·10⁵ (LC 1971) → one
O(V + E) pass.

**Key point** — a node is marked **the moment it is enqueued**, so it enters the queue at most once,
and its first `dist` is final: BFS pops nodes in non-decreasing distance, so the first arrival is
along a shortest path. The moment you decide a neighbour enters the queue is the moment you
commit — mark it, update counters, mutate the grid. That is a correctness rule, not an
optimisation.

```python
# python
# LC 1971 - Find if Path Exists in Graph
# IDEA: BFS from source; dist doubles as the visited set and is written when a node is enqueued
# time = O(V + E), space = O(V + E)
from collections import deque

def validPath(n, edges, source, destination):
    graph = [[] for _ in range(n)]
    for u, v in edges:                       # undirected: both directions
        graph[u].append(v)
        graph[v].append(u)
    dist = {source: 0}                       # first arrival = shortest distance
    queue = deque([source])
    while queue:
        node = queue.popleft()
        if node == destination:
            return True                      # dist[node] is the shortest path length
        for nxt in graph[node]:
            if nxt not in dist:
                dist[nxt] = dist[node] + 1   # mark BEFORE enqueue
                queue.append(nxt)
    return False
```

```java
// java
// LC 1971 - Find if Path Exists in Graph
// IDEA: BFS from source; dist[] doubles as the visited set and is written when a node is enqueued
// time = O(V + E), space = O(V + E)
public boolean validPath(int n, int[][] edges, int source, int destination) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        graph.add(new ArrayList<>());
    }
    for (int[] e : edges) {                  // undirected: both directions
        graph.get(e[0]).add(e[1]);
        graph.get(e[1]).add(e[0]);
    }
    int[] dist = new int[n];
    Arrays.fill(dist, -1);                   // -1 = not reached yet
    dist[source] = 0;
    Deque<Integer> queue = new ArrayDeque<>();
    queue.offer(source);
    while (!queue.isEmpty()) {
        int node = queue.poll();
        if (node == destination) {
            return true;
        }
        for (int nxt : graph.get(node)) {
            if (dist[nxt] == -1) {
                dist[nxt] = dist[node] + 1;  // mark BEFORE enqueue
                queue.offer(nxt);
            }
        }
    }
    return false;
}
```

**Complexity**
- **Time O(V + E)**: building the list reads each edge twice; the `nxt not in dist` guard lets each
  node into the queue once, so `for nxt in graph[node]` reads each list once over the run.
- **Space O(V + E)**: the adjacency list; `dist` and the queue are O(V).

**Dry run** — why the mark goes on enqueue, not on dequeue

```text
Cells A and B are both in the current layer; X is a fresh neighbour of both.

mark on DEQUEUE (wrong)                    mark on ENQUEUE (right)
1. pop A -> X unmarked -> enqueue X        1. pop A -> X unmarked -> mark X, enqueue X
2. pop B -> X STILL unmarked -> enqueue X  2. pop B -> X marked -> skip
3. pop X -> mark, fresh -= 1               3. pop X -> fresh -= 1 happened once, at step 1
4. pop X again -> fresh -= 1 AGAIN
```

**Traps**
- Marking at dequeue lets two neighbours in one layer both enqueue the same node: a counter
  updated on visit moves twice (LC 994's `fresh` goes negative), a distance can be overwritten by a
  longer one (LC 286), and on a dense graph the queue grows to O(E).
- An undirected edge list added in one direction only: `n = 2, edges = [[1,0]], source = 0,
  destination = 1` returns False.
- `source == destination` must be True even with no edges: the check runs on pop, and the source
  is popped first.

Where mark-on-enqueue is the difference between right and wrong:

| Problem | What breaks if you mark on dequeue |
|---|---|
| LC 994 Rotting Oranges | `fresh -= 1` runs once per duplicate, so `fresh` ends below zero |
| LC 542 01 Matrix, LC 1162 As Far from Land | a distance must be set on the first, shortest, arrival |
| LC 286 Walls and Gates | a room's distance is overwritten by a longer path |
| LC 200 Number of Islands (BFS form) | the same land cell is expanded again |
| LC 934 Shortest Bridge | water cells on the frontier are counted twice |
| LC 127 Word Ladder | a word is enqueued once per neighbour that reaches it |

**Not this if** — edges have different weights → Dijkstra ([Dijkstra.md](./Dijkstra.md)); weights
are only 0 and 1 → 0-1 BFS with a deque ([bfs_advanced.md](./bfs_advanced.md) Pattern 15). Only
reachability is asked and recursion depth is small → DFS works as well
([graph.md § Template 2](./graph.md#template-2-dfs-reachability--lc-841-)).

**Drill** — LC 1971 · LC 841 *(reachability from room 0)* · LC 1091 *(grid, 8 directions:
Template 3)* · LC 433 *(neighbours generated: Template 5)* · LC 1293 *(state = cell + eliminations
left)*

#### Variation: return the path, not only its length (parent map)

Record who enqueued each node; once the target is popped, walk those parents back to the start.
Carrying `path + [neighbor]` in the queue instead also works, but copies a path at every enqueue,
which is O(V · L) memory for paths of length L.

```python
# python
# IDEA: parent[nxt] = node when nxt is first enqueued; walk parents back from the target
# time = O(V + E), space = O(V)
from collections import deque

def shortest_path(graph, start, target):
    parent = {start: None}                   # doubles as the visited set
    queue = deque([start])
    while queue:
        node = queue.popleft()
        if node == target:
            path = []
            while node is not None:          # walk back to the start
                path.append(node)
                node = parent[node]
            return path[::-1]
        for nxt in graph[node]:
            if nxt not in parent:
                parent[nxt] = node
                queue.append(nxt)
    return []
```

### Template 3: Grid BFS with a Direction Array — LC 1091 ⭐⭐⭐⭐

**Recognise** — "shortest path in a binary matrix / maze / grid", "minimum steps from the top-left
to the bottom-right", moves to 4 or 8 neighbours that each cost 1. Grid ≤ 100 × 100 (LC 1091) →
O(m·n).

**Key point** — the grid is the graph: a cell is a node and the `dirs` array lists its edges.
Writing `1` into a cell when it is enqueued makes the grid its own visited set, so every cell is
enqueued once, and the first time the target is popped its `dist` is the shortest.

```python
# python
# LC 1091 - Shortest Path in Binary Matrix
# IDEA: BFS over cells; dirs = the 8 moves; the grid doubles as the visited set
# time = O(n^2), space = O(n^2)
from collections import deque

def shortestPathBinaryMatrix(grid):
    n = len(grid)
    if grid[0][0] == 1 or grid[n - 1][n - 1] == 1:
        return -1
    dirs = [(0, 1), (1, 0), (0, -1), (-1, 0), (1, 1), (1, -1), (-1, 1), (-1, -1)]
    queue = deque([(0, 0, 1)])               # (row, col, cells on the path so far)
    grid[0][0] = 1                           # mark the start before the loop
    while queue:
        r, c, dist = queue.popleft()
        if r == n - 1 and c == n - 1:
            return dist
        for dr, dc in dirs:
            nr, nc = r + dr, c + dc
            if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 0:
                grid[nr][nc] = 1             # mark when enqueued
                queue.append((nr, nc, dist + 1))
    return -1
```

```java
// java
// LC 1091 - Shortest Path in Binary Matrix
// IDEA: BFS over cells; dirs = the 8 moves; the grid doubles as the visited set
// time = O(n^2), space = O(n^2)
public int shortestPathBinaryMatrix(int[][] grid) {
    int n = grid.length;
    if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
        return -1;
    }
    int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
    Deque<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{0, 0, 1});
    grid[0][0] = 1;                          // mark visited
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int r = curr[0], c = curr[1], dist = curr[2];
        if (r == n - 1 && c == n - 1) {
            return dist;
        }
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 0) {
                grid[nr][nc] = 1;            // mark when enqueued
                queue.offer(new int[]{nr, nc, dist + 1});
            }
        }
    }
    return -1;
}
```

**Complexity**
- **Time O(n²)**: each of the n² cells is enqueued at most once, and the `for dr, dc in dirs`
  loop is a constant 8 per cell.
- **Space O(n²)**: the queue can hold a whole frontier of cells; marking in place costs nothing.

**Traps**
- The answer counts **cells**, not moves: `[[0]]` returns 1, so the start is enqueued with
  `dist = 1`.
- A blocked start or end: `[[1,0],[0,0]]` returns -1. Check both before seeding the queue.
- Bounds before reading: in Python `grid[-1][c]` silently reads the last row, so test
  `0 <= nr < n` first, as the `and` chain does.

**Not this if** — moving costs differ per cell (LC 1631 effort, LC 1368 arrow cost 0 or 1) →
Dijkstra or 0-1 BFS. The question is "nearest X from **every** cell" → multi-source BFS
([Template 4](#template-4-multi-source-bfs--lc-994-)).

**Drill** — LC 1091 · LC 1926 *(nearest exit, 4 directions)* · LC 1730 *(shortest path to food)* ·
LC 490 *(the ball rolls to a wall: the neighbours are the stop cells)* · LC 1293 *(the state adds
eliminations left)*

### Template 4: Multi-Source BFS — LC 994 ⭐⭐⭐⭐⭐

**Recognise** — "rotting oranges", "distance to the nearest 0 / gate / land", "fire or infection
spreads from every source at once", "minimum minutes until". Grid ≤ 10⁵ cells → one O(m·n) BFS,
never one BFS per source.

**Key point** — seeding every source at level 0 is BFS from one virtual super-source joined to all
of them, so each cell is reached first by its nearest source, and one level is one minute.

```python
# python
# LC 994 - Rotting Oranges
# IDEA: seed every rotten orange at minute 0; one BFS level = one minute; count fresh to detect -1
# time = O(m * n), space = O(m * n)
from collections import deque

def orangesRotting(grid):
    rows, cols = len(grid), len(grid[0])
    queue = deque()
    fresh = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 2:
                queue.append((r, c))         # every source at level 0
            elif grid[r][c] == 1:
                fresh += 1
    dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]
    minutes = 0
    while queue and fresh > 0:               # stop as soon as nothing is left to rot
        minutes += 1
        for _ in range(len(queue)):
            r, c = queue.popleft()
            for dr, dc in dirs:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1:
                    grid[nr][nc] = 2         # rot it when enqueued, not when popped
                    fresh -= 1
                    queue.append((nr, nc))
    return minutes if fresh == 0 else -1
```

```java
// java
// LC 994 - Rotting Oranges
// IDEA: seed every rotten orange at minute 0; one BFS level = one minute
// time = O(m * n), space = O(m * n)
public int orangesRotting(int[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    Deque<int[]> queue = new ArrayDeque<>();
    int fresh = 0;
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 2) {
                queue.offer(new int[]{r, c});
            } else if (grid[r][c] == 1) {
                fresh++;
            }
        }
    }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int minutes = 0;
    while (!queue.isEmpty() && fresh > 0) {
        minutes++;
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0] + d[0], nc = cell[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                    grid[nr][nc] = 2;        // rot it when enqueued
                    fresh--;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
    return fresh == 0 ? minutes : -1;
}
```

**Complexity**
- **Time O(m·n)**: the seeding loop reads every cell once, and the mark-on-enqueue guard lets each
  fresh orange into the queue once.
- **Space O(m·n)**: the queue can hold every cell.

**Dry run**

```text
grid = [[2,1,1],
        [1,1,0],
        [0,1,1]]          fresh = 6, queue = [(0,0)]
minute 1: rot (0,1) (1,0)          fresh 4
minute 2: rot (0,2) (1,1)          fresh 2
minute 3: rot (2,1)                fresh 1
minute 4: rot (2,2)                fresh 0  -> loop stops, answer 4
```

**Traps**
- `minutes += 1` at the start of a level needs `fresh > 0` in the `while`: without it the last
  level, whose cells rot nothing, adds a minute and the example above returns 5. The other
  correct form increments at the **end** of a level, and only when something rotted in it (a
  `rotted_this_minute` flag). Mixing the two, or using neither, counts the final empty level.
- No fresh orange at all: `[[0,2]]` returns 0, not -1 — the `while` never runs.
- One BFS per rotten orange, keeping the minimum, gives the right numbers k times slower and times
  out once there are many sources.
- Rotting a cell when it is popped: two rotten neighbours in one level both enqueue it, and
  `fresh` goes negative ([Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-)).

**Not this if** — each source needs its own distances added up (LC 317 Shortest Distance from All
Buildings) → independent BFS runs with a fresh `visited` per source
([bfs_advanced.md](./bfs_advanced.md) Pattern 4.6). Edges carry weights → Dijkstra seeded with
every source.

**Drill** — LC 994 ⭐ · LC 542 *(distance variant below)* · LC 286 · LC 1162 · LC 417 ⭐ *(one
multi-source BFS per ocean, then intersect)* · LC 934 *(DFS one island, then BFS from all of it)*

#### 01 Matrix — LC 542

> **Key point** — "distance from every 1 to its nearest 0" is the same as "how far every 0 can
> reach": seed **all** 0-cells at distance 0 and run one BFS. The first time a cell is reached is
> its distance, so each cell is written once — O(m·n) instead of one BFS per 1-cell.

**Reduces to**: [Template 4](#template-4-multi-source-bfs--lc-994-) — with a `dist` grid instead
of a minute counter: the distance is written per cell, not per level.

```python
# python
# LC 542 - 01 Matrix
# IDEA: multi-source BFS from every 0; the first time a cell is reached sets its distance
# time = O(m * n), space = O(m * n)
from collections import deque

def updateMatrix(mat):
    rows, cols = len(mat), len(mat[0])
    dist = [[-1] * cols for _ in range(rows)]   # -1 = not reached yet
    queue = deque()
    for r in range(rows):
        for c in range(cols):
            if mat[r][c] == 0:
                dist[r][c] = 0
                queue.append((r, c))
    dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]
    while queue:
        r, c = queue.popleft()
        for dr, dc in dirs:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and dist[nr][nc] == -1:
                dist[nr][nc] = dist[r][c] + 1
                queue.append((nr, nc))
    return dist
```

```java
// java
// LC 542 - 01 Matrix
// IDEA: Multi-source BFS — enqueue all 0s first, then expand
// time = O(M*N), space = O(M*N)
public int[][] updateMatrix(int[][] mat) {
    int m = mat.length, n = mat[0].length;
    int[][] dist = new int[m][n];
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++) {
            if (mat[i][j] == 0) queue.offer(new int[]{i, j});
            else dist[i][j] = Integer.MAX_VALUE;
        }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] cell = queue.poll();
        for (int[] d : dirs) {
            int nr = cell[0]+d[0], nc = cell[1]+d[1];
            if (nr>=0 && nr<m && nc>=0 && nc<n && dist[nr][nc] > dist[cell[0]][cell[1]]+1) {
                dist[nr][nc] = dist[cell[0]][cell[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    return dist;
}
```

**Traps**
- One BFS per 1-cell is O((m·n)²): it times out at m·n = 10⁴. Flip the direction and start from
  the 0s.
- Mark a cell when it is **enqueued** (`dist[nr][nc] == -1` → write it), not when it is popped,
  or the same cell is queued by several neighbours.

### Template 5: Word Ladder BFS over Generated Neighbours — LC 127 ⭐⭐⭐⭐⭐

**Recognise** — "transform one word into another, one letter at a time", "minimum number of moves
to open the lock", "each step changes one position", a dictionary or `deadends` list of the valid
states. Word length L ≤ 10, words N ≤ 5,000 → generate a word's 26·L neighbours instead of
comparing all N² pairs.

**Key point** — the graph is never built: a word's neighbours are generated by changing one position
at a time, and kept only if they are in the dictionary. Each word is marked when enqueued, so it is
expanded once, and BFS levels count transformations.

```python
# python
# LC 127 - Word Ladder
# IDEA: BFS over words; neighbours = every one-letter change that is in the dictionary
# time = O(N * 26 * L^2), space = O(N * L)   N = words, L = word length
from collections import deque

def ladderLength(beginWord, endWord, wordList):
    words = set(wordList)
    if endWord not in words:
        return 0
    visited = {beginWord}
    queue = deque([beginWord])
    steps = 1                                # beginWord itself is the first word
    while queue:
        for _ in range(len(queue)):
            word = queue.popleft()
            if word == endWord:
                return steps
            for i in range(len(word)):
                for ch in 'abcdefghijklmnopqrstuvwxyz':
                    if ch == word[i]:
                        continue
                    candidate = word[:i] + ch + word[i + 1:]
                    if candidate in words and candidate not in visited:
                        visited.add(candidate)          # mark when enqueued
                        queue.append(candidate)
        steps += 1
    return 0
```

```java
// java
// LC 127 - Word Ladder
// IDEA: BFS over words; change one char in place, test the dictionary, then restore the char
// time = O(N * 26 * L^2), space = O(N * L)
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> dict = new HashSet<>(wordList);
    if (!dict.contains(endWord)) {
        return 0;
    }
    Set<String> visited = new HashSet<>();
    Deque<String> queue = new ArrayDeque<>();
    queue.offer(beginWord);
    visited.add(beginWord);
    int steps = 1;                           // beginWord counts as the first word
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String cur = queue.poll();
            if (cur.equals(endWord)) {
                return steps;
            }
            char[] arr = cur.toCharArray();
            for (int j = 0; j < arr.length; j++) {
                char original = arr[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == original) {
                        continue;
                    }
                    arr[j] = c;              // try every letter at position j
                    String next = new String(arr);
                    if (dict.contains(next) && !visited.contains(next)) {
                        visited.add(next);   // mark BEFORE enqueue
                        queue.offer(next);
                    }
                }
                arr[j] = original;           // restore before moving to position j + 1
            }
        }
        steps++;
    }
    return 0;
}
```

**Complexity**
- **Time O(N · 26 · L²)**: `visited` lets each word be expanded once; expanding it tries 26
  letters at each of L positions, and building and hashing each candidate string costs O(L).
- **Space O(N · L)**: the word set, `visited` and the queue each hold up to N words of length L.

**Traps**
- `endWord` not in `wordList` → 0 at once. LC 127's second example (`"cog"` missing) otherwise
  searches the whole dictionary for nothing.
- The answer counts **words**, not edges: `hit → hot → dot → dog → cog` is 5, so `steps` starts at 1.
- Java's char-array form must restore `arr[j] = original` after the 26 letters, or position `j + 1`
  is tried on a word that still has the wrong letter at `j`.
- Building the graph by comparing every pair of words is O(N² · L), about 2.5·10⁸ at N = 5,000;
  generating neighbours keeps it linear in N.

**Not this if** — every shortest sequence is wanted (LC 126) → BFS to build the level DAG, then DFS
over it ([bfs_advanced.md](./bfs_advanced.md) Pattern 8.5). Both ends are known and branching is
large → bidirectional BFS ([bfs_advanced.md](./bfs_advanced.md)).

**Drill** — LC 127 ⭐ · LC 433 *(4 letters, 8 positions)* · LC 752 *(a lock: each wheel ±1,
deadends are walls)* · LC 126 *(all shortest sequences)* · LC 854 *(neighbours by swapping two
letters)*

### Template 6: Parent Map + BFS Radiating from a Target — LC 863 ⭐⭐⭐⭐⭐

**Recognise** — "all nodes at distance k from `target`", "time for an infection to spread from
node `start`", "closest leaf to node k": distance is measured from a node that is **not the root**,
and the answer can include ancestors or nodes in a sibling subtree. Nodes ≤ 500 (LC 863) to
10⁵ (LC 2385).

**Key point** — a binary tree stores only downward pointers. One DFS that records
`{node: parent}` adds the missing edge direction, so every node has at most three neighbours —
`(left, right, parent)` — and BFS out from `target` then gives the exact tree distance of every
node, because every edge costs 1.

A tree only stores **downward** pointers (`left`, `right`), but nodes at distance `k` from a
`target` can sit in **three** places: below it, **above** it, or in a **sibling subtree**
(up-then-down). One DFS that only walks down can never reach them. Two things make the BFS correct:

| Element | Why it's mandatory |
|---|---|
| `visited` set | Adding parent edges makes the graph **undirected** → BFS would bounce `child → parent → child` forever. In a plain top-down tree traversal you never need `visited`; here you always do. |
| `continue` at `dist == k` | Nodes beyond `k` are irrelevant, and their only path back in is through an already-collected node. Skipping expansion caps work and prevents over-collecting. |

```python
# python
# LC 863 - All Nodes Distance K in Binary Tree
# IDEA: DFS build {node: parent}, then BFS "radiate outward" from target
# time = O(n), space = O(n)   each node is parented once and enqueued at most once
import collections

class Solution(object):
    def distanceK(self, root, target, k):
        # Step 1: map every node to its parent (the missing "up" edge)
        parents = {}
        def add_parents(node, parent):
            if not node:
                return
            parents[node] = parent
            add_parents(node.left, node)
            add_parents(node.right, node)
        add_parents(root, None)

        # Step 2: BFS outward from target
        queue = collections.deque([(target, 0)])   # (current_node, distance)
        visited = set([target])                    # MUST have: graph is undirected now
        ans = []

        while queue:
            node, dist = queue.popleft()

            if dist == k:
                ans.append(node.val)
                continue                            # don't expand past k

            # 3 directions: down-left, down-right, UP (parent)
            for neighbor in (node.left, node.right, parents[node]):
                if neighbor and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append((neighbor, dist + 1))

        return ans
```

```java
// java
// LC 863 - All Nodes Distance K in Binary Tree
// IDEA: DFS build parent map, then BFS k steps out from target
// time = O(n), space = O(n)
public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    Map<TreeNode, TreeNode> parents = new HashMap<>();
    buildParents(root, null, parents);

    Deque<Object[]> queue = new ArrayDeque<>();
    queue.offer(new Object[]{target, 0});
    Set<TreeNode> visited = new HashSet<>();
    visited.add(target);
    List<Integer> ans = new ArrayList<>();

    while (!queue.isEmpty()) {
        Object[] cur = queue.poll();
        TreeNode node = (TreeNode) cur[0];
        int dist = (int) cur[1];

        if (dist == k) {          // exactly k edges away
            ans.add(node.val);
            continue;             // stop expanding this branch
        }
        for (TreeNode nei : new TreeNode[]{node.left, node.right, parents.get(node)}) {
            if (nei != null && visited.add(nei)) {   // add() returns false if seen
                queue.offer(new Object[]{nei, dist + 1});
            }
        }
    }
    return ans;
}

private void buildParents(TreeNode node, TreeNode parent, Map<TreeNode, TreeNode> parents) {
    if (node == null) return;
    parents.put(node, parent);
    buildParents(node.left, node, parents);
    buildParents(node.right, node, parents);
}
```

**Complexity**
- **Time O(n)**: `add_parents` visits each node once, and the `visited` guard lets each node into
  the queue once; each node has at most 3 neighbours to try.
- **Space O(n)**: the parent map, `visited` and the queue; the DFS adds O(h) of recursion.

**Dry run** — `root = [3,5,1,6,2,0,8,null,null,7,4]`, `target = 5`, `k = 2`

```text
        3                  BFS from 5 (each layer = distance):
      /   \                dist 0 : 5
     5     1               dist 1 : 6, 2  (children) , 3  (PARENT ← the key edge)
    / \   / \              dist 2 : 7, 4  (via 2)    , 1  (via 3, sibling subtree)
   6   2 0   8                      ^^^^^^^^^^^^^^^^^^^^^ answer = [7, 4, 1]
      / \
     7   4              Without the parent edge you would only find 7 and 4 — the
                        node `1` requires going UP to 3, then DOWN to 1.
```

**Traps**
- No `visited` set: the walk still stops (the `continue` at `dist == k` caps every path at `k`
  edges), but it returns wrong nodes. With `k = 2` it goes 5 → 6 → 5, so `target` itself comes back
  at distance 2, and a node can be collected more than once. `visited` is what makes `dist` the
  shortest distance.
- No `continue` at `dist == k`: the answer is still right, because only `dist == k` is collected,
  but the BFS walks the whole tree for every `k`.
- LC 863 hands you `target` as a **node**; LC 2385 hands you `start` as a **value**. Find that
  node during the parent DFS before the BFS starts.
- `k = 0` returns `[target.val]`: the target is popped with `dist == 0` before anything expands.

**Not this if** — the distance is from the **root** → plain level BFS
([Template 1](#template-1-level-order-bfs--lc-102-)). The DFS alternative ("percolate distance")
is a post-order DFS returning the depth of `target` in each subtree; at the node `d` edges above
the target you collect nodes `k - d` levels down the *other* child. Also O(n), but far easier to
get wrong — BFS + parent map is the interview-safe answer. When the nodes are values rather than
`TreeNode` objects, build a full adjacency map instead (LC 1530 needs one; its answer is the
post-order bucket DFS in [dfs_advanced.md](./dfs_advanced.md)):

| | Parent map `{node: parent}` | Adjacency map `{node: [neighbors]}` |
|---|---|---|
| Built by | one DFS, 1 entry per node | one DFS, 2 entries per edge |
| Neighbor access | `(node.left, node.right, parents[node])` | `graph[node]` |
| Space | ~n pointers | ~2(n-1) list entries |
| Use when | nodes are **real `TreeNode` objects** you can dereference | you work with **values**, or the structure isn't a binary tree |

**Drill** — LC 863 ⭐ · LC 2385 *(the answer is the last BFS level)* · LC 742 *(stop at the first
leaf popped)* · LC 1740 *(BFS from one node until the other pops)* · LC 2096 *(the same up-then-down
path, reconstructed)*

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| All Nodes Distance K in Binary Tree | 863 | **canonical** — parent map + BFS `k` steps outward |
| Amount of Time for Binary Tree to Be Infected | 2385 | same parent-map BFS; answer = **max** distance (last BFS level) |
| Closest Leaf in a Binary Tree | 742 | BFS out from target, stop at **first leaf** popped ([bfs_examples.md](./bfs_examples.md)) |
| Find Distance in a Binary Tree | 1740 | distance between 2 nodes = BFS from one until the other pops (or LCA) |
| Number of Good Leaf Nodes Pairs | 1530 | per-leaf bounded BFS works, but the answer is the post-order distance buckets in [dfs_advanced.md](./dfs_advanced.md) |
| Step-By-Step Directions From a Binary Tree Node | 2096 | same up-then-down insight, path reconstruction instead of distance |
| Cousins in Binary Tree | 993 | needs `parent` + `depth` per node — parent map, no BFS radiation |
| Lowest Common Ancestor of a Binary Tree | 236 | LCA is the "turning point" of the up-then-down path |
| Minimum Height Trees | 310 | undirected-tree BFS, trimming inward instead of radiating out ([topology_sorting_examples.md](./topology_sorting_examples.md)) |

#### Second solution: expand exactly `k` levels, then the queue is the answer

No `dist` in the queue entry: run `k` full level expansions (snapshot `len(q)` first), and whatever
is left in the queue is exactly the set at distance `k`. Same O(n); `k = 0` returns `[target]` and
`k` larger than the tree drains the queue to `[]` without a special case. Prefer the `(node, dist)`
form above when the distances themselves are also needed.

```python
# python
# LC 863 - All Nodes Distance K in Binary Tree (k level expansions)
# IDEA: no distance stored; after exactly k level expansions the queue IS the answer set
# time = O(n), space = O(n)
import collections

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

> **Pattern takeaway**: the instant a tree problem measures something **from a node other than
> the root**, stop thinking "tree recursion" and think **"undirected graph"** — add parent links
> (`{node: parent}` map, or `node.par` annotation), then it is an ordinary BFS where each node
> has 3 neighbors and `visited` is non-negotiable.

---

### Bipartite Check (2-Colouring) — lives in graph.md

BFS does not have to carry a distance; it can carry a **label**. The one classic case is
2-colouring: paint each neighbour the opposite colour, and an edge with the same colour at both
ends is an odd cycle. That card — BFS and DFS forms, Python and Java, the disconnected-graph and
self-loop traps — is
[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-), the one home of
LC 785; LC 886 is worked in [graph_examples.md](./graph_examples.md).


## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 102 · LC 994 · LC 127 | level BFS, multi-source BFS, neighbours generated rather than stored |
| **Practice** | LC 111 · LC 1971 · LC 1091 · LC 542 · LC 863 · LC 785 · LC 752 | early exit at the first leaf, an edge list, a grid with 8 moves, distances from every source, a tree as a graph, 2-colouring, a lock as a state graph |
| **Stretch** | LC 126 · LC 815 · LC 1293 | all shortest paths, BFS over routes instead of stops, a state that carries a budget |

---

## Summary & Quick Reference

> `Pattern 4.5 / 4.6 / 6 / 8 / 8.5 / 9 / 12 / 15 / 16` below live in [bfs_advanced.md](./bfs_advanced.md); the problems marked *bfs_examples* are worked in [bfs_examples.md](./bfs_examples.md).

### When to Use BFS
- Finding shortest path in unweighted graphs
- Level-order tree traversal
- Finding connected components
- Checking if graph is bipartite
- Web crawling (breadth-first exploration)
- **Simultaneous multi-source distance calculations** (Template 4) - distance to nearest source
- **Independent BFS runs from multiple sources** (Pattern 4.6) - sum of distances to all sources

### When NOT to Use BFS
- Deep trees/graphs with limited memory
- Only need to find ANY path (not shortest)
- Weighted graphs with varying costs (use Dijkstra instead)
- Need to explore all paths (use DFS)

### BFS vs Dijkstra — When to Use Which

| Criteria | BFS | Dijkstra |
|----------|-----|----------|
| **Edge weights** | All equal (unweighted) or 0/1 | Non-negative, varying weights |
| **Data structure** | Queue (`LinkedList`) | Priority Queue (min-heap) |
| **Time complexity** | O(V + E) | O((V + E) log V) |
| **First visit = shortest?** | ✅ Yes (level = distance) | ❌ No (must relax via PQ) |
| **"Minimum steps/moves"** | ✅ Use BFS | ❌ Overkill |
| **"Minimum cost/weight"** | ❌ Wrong answer | ✅ Use Dijkstra |
| **Grid with uniform cost** | ✅ BFS | ❌ Unnecessary overhead |
| **Grid with varying costs** | ❌ | ✅ Dijkstra on implicit graph |

**Decision rule**: If every edge has the same cost (or cost is 1), use BFS. The moment edges have different weights, switch to Dijkstra.

**Common trap**: Using Dijkstra (PQ) for problems like LC 279 Perfect Squares or LC 752 Open the Lock where all edges cost 1 — plain BFS is simpler and faster.

**0-1 BFS special case**: If edges are weighted 0 or 1 only, use a **deque** — push weight-0 edges to front, weight-1 edges to back. O(V+E) like BFS, handles two weights correctly.

### Common Mistakes & Best Practices

#### Common Mistakes
1. Using `queue.pop()` instead of `queue.popleft()` with list
2. Not handling visited set in graphs (infinite loops)
3. Forgetting level-by-level processing when needed
4. Incorrect boundary checking in grid problems

#### Best Practices
1. Use `collections.deque` for better performance
2. Always use visited set for graph problems
3. Check boundaries before adding to queue in grid problems
4. Consider multi-source BFS for optimization
5. Track level/distance when needed for shortest path
6. **Mark state BEFORE enqueue, not after dequeue** — update grid/visited/counters the moment you decide to enqueue a neighbor; deferring until dequeue lets multiple neighbors re-enqueue the same cell (see **Template 2** above)

### Problems by Category

#### 1. Tree Traversal Problems
- **Level Order Traversal**: LC 102, 107, 103
- **Binary Tree Paths**: LC 257, 1022
- **Right Side View**: LC 199
- **Vertical Order**: LC 314
- **Level-wise Tree Mutation**: LC 623 (Add One Row), LC 116/117 (Next Right Pointers)
- **Distance from an arbitrary node (Template 6 — parent map + radiate out)**: LC 863 (Distance K), LC 2385 (Tree Infection), LC 742 (Closest Leaf), LC 1740 (Find Distance)

#### 2. Shortest Path Problems
- **Unweighted Graphs**: LC 127 (Word Ladder)
- **Grid Navigation**: LC 1730 (Shortest Path to Food), LC 1091 (Shortest Path in Binary Matrix)
- **Simultaneous Multi-source Distance (Template 4)**:
  - **LC 542 (01 Matrix)** - Distance to nearest 0 from each cell
  - LC 1162 (As Far from Land) - Distance to nearest land from each water cell
  - LC 286 (Walls and Gates) - Distance from gates to rooms
  - LC 994 (Rotting Oranges) - Time for infection to spread
- **Independent BFS Runs (Pattern 4.6)**:
  - **LC 317 (Shortest Distance from All Buildings)** - Sum of distances to all buildings (use fresh visited for each)
- **DFS + Multi-source BFS (Pattern 4.5)**: LC 934 (Shortest Bridge - mark one component, expand to find other)
- **Sequential Targets (Pattern 6)**: LC 675 (Cut Off Trees for Golf Event - Sort + Repeated BFS)
- **Route-Level BFS (Pattern 8)**: LC 815 (Bus Routes - minimum buses/transfers to reach target)
- **State-Based BFS**: LC 864 (Shortest Path to Get All Keys), LC 1293 (Shortest Path with Obstacles Elimination)

#### 3. Graph Structure Problems
- **Cycle Detection**: LC 207 (Course Schedule)
- **Connected Components**: LC 200 (Number of Islands)
- **Graph Validation**: LC 261 (Graph Valid Tree)
- **Clone Graph**: LC 133

#### 4. Matrix/Grid Problems
- **Surrounded Regions**: LC 130
- **Walls and Gates**: LC 286
- **Maze Problems**: LC 490

#### 5. Combination Enumeration Problems (Pattern 9 — BFS-Style Cartesian Product)
- **Brace Expansion (LC 1087)** — parse into groups, BFS layer-by-layer
- **Letter Combinations of a Phone Number (LC 17)** — digit → letter group, Cartesian BFS
- **Letter Case Permutation (LC 784)** — per-char 1-or-2 option groups
- **Generalized Abbreviation (LC 320)** — keep-or-skip groups per character

### Key LeetCode Problems
| Difficulty | Problem | Key Concept | Core Pattern |
|------------|---------|-------------|--------------|
| Easy | LC 102 | Level-order traversal | Template 1 (Level-by-Level) |
| **Medium** | **LC 127** | **Shortest path transformation - Word Ladder** | **Template 5 (Unweighted Shortest Path)** |
| Medium | LC 200 | Connected components | Template 2 (Graph BFS) |
| Medium | LC 742 | Closest leaf (tree → undirected graph) | [bfs_examples.md](./bfs_examples.md#closest-leaf-in-a-binary-tree--lc-742) (Tree → Graph + BFS) |
| Medium | LC 863 | Distance `k` from a **target node** (parent map, 3 neighbors) | Template 6 (Radiate Outward), both shapes |
| Medium | LC 623 | Level BFS to `depth - 1`, rewire child pointers | [bfs_examples.md](./bfs_examples.md#add-one-row-to-tree--lc-623) |
| **Medium** | **LC 542** | **Simultaneous multi-source - 01 Matrix** | **Template 4 (Simultaneous Multi-Source)** |
| Medium | LC 934 | DFS + Multi-source BFS (island expansion) | Pattern 4.5 (DFS + Multi-Source) |
| Medium | LC 1162 | As Far from Land as Possible | Template 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 126** | **Find ALL shortest paths - Word Ladder II** | **Pattern 8.5 (BFS + DFS DAG Enumeration)** |
| Hard | LC 286 | Walls and Gates | Template 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 317** | **Independent BFS runs (sum of distances)** | **Pattern 4.6 (Independent BFS Runs)** |
| Hard | LC 675 | Sort + Repeated BFS (sequential targets) | Pattern 6 (Sort + Repeated BFS) |
| **Hard** | **LC 752** | **BFS on state space - Open the Lock** | **Template 5 (Unweighted Shortest Path); worked in [bfs_examples.md](./bfs_examples.md#open-the-lock--lc-752)** |
| **Hard** | **LC 815** | **Route-level BFS (minimum buses)** | **Pattern 8 (Route-Level BFS)** |
| Hard | LC 864 | BFS with state (key collection) | Template 2 + State ([bfs_advanced.md](./bfs_advanced.md) Pattern 16) |
| Hard | LC 1293 | BFS with state (obstacle elimination) | Template 2 + State ([bfs_advanced.md](./bfs_advanced.md) Pattern 16) |

### Also Frequently Asked (no new template — they reuse the ones above)

| LC | Problem | Which template it reuses |
|----|---------|--------------------------|
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | Template 1 level BFS writing `null` markers; deserialize = same queue read back (see [LC 958 in bfs_examples.md](./bfs_examples.md#check-completeness-of-a-binary-tree--lc-958)) |
| 104 | Maximum Depth of Binary Tree | Template 1 — count levels; DFS recursion is shorter here, BFS wins only for LC 111 |
| 101 | Symmetric Tree | Template 1 with a **pair queue** — enqueue `(left, right)` mirrored and compare on pop |
| 637 / 515 | Average of Levels / Largest Value in Each Tree Row | Template 1 — swap "collect the level" for "aggregate the level" (avg / max) |
| 433 | Minimum Genetic Mutation | Same template as LC 127 / 752 — 8-char gene string, 4 letters, bank = valid-state set |
| 529 | Minesweeper | Template 3 grid BFS — only expand a cell when its adjacent-mine count is `0`, otherwise write the digit and stop |
| 547 | Number of Provinces | Template 2 — count how many BFS runs it takes to cover all nodes (or Union-Find) |
| 1376 | Time Needed to Inform All Employees | Template 1 on the manager tree, queue holds `(employee, timeSoFar)` — answer is the max |
| 787 | Cheapest Flights Within K Stops | BFS **level-bounded relaxation** (Bellman-Ford flavored): run exactly `k+1` levels and **do not use a global visited** — a node may be re-entered with a cheaper cost. See `Dijkstra.md`. |
| 329 | Longest Increasing Path in a Matrix | Not a BFS problem — DFS + memo, or Kahn's BFS on the DAG (see `topology_sorting.md`) |
| 721 / 947 / 684 / 839 | Accounts Merge / Stones Removed / Redundant Connection / Similar String Groups | Connectivity, not shortest path — Union-Find is the expected answer (BFS flood-fill also works) |
