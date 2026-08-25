# BFS (Breadth-First Search)

> **Scope** — The main BFS reference: the queue templates, level-by-level expansion, grid and multi-source BFS, and why first visit equals shortest path on an unweighted graph; the heavier variants and the long tail of worked problems live in their own sheets.
> **See also** — *deep dives split out of this file*: [bfs_advanced.md](./bfs_advanced.md) — bidirectional BFS, 0-1 BFS with a deque, state-space / implicit-graph BFS, all-shortest-path DAG enumeration, and the multi-source vs independent-runs distinction; [bfs_examples.md](./bfs_examples.md) — the worked-solution archive (LC 130 / 207 / 279 / 286 / 310 / 417 / 623 / 742 / 752 / 909 / 116-117 …) plus the LC 994 timing walkthrough.
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

#### 🔹 BFS (Breadth-First Search)
- Uses a **Queue**
- Order: **FIFO** (First In, First Out)
- How it works: Visit a node → Add all neighbors to queue → Process in order added
- 👉 Think: **level by level traversal**

#### 🔹 DFS (Depth-First Search)
- Uses a **Stack** (explicitly or via recursion)
- Order: **FILO / LIFO** (Last In, First Out)
- How it works: Go as deep as possible along one path → Backtrack when needed
- 👉 Think: **go deep first, then backtrack**

| Aspect | BFS | DFS |
|--------|-----|-----|
| Data Structure | Queue (FIFO) | Stack / Recursion (LIFO) |
| Traversal Order | Level by level | Deep path first, then backtrack |
| Memory | O(w) — width of tree | O(h) — height of tree |
| Shortest Path | ✅ Yes (unweighted) | ❌ No |
| Complete | ✅ Yes | ❌ No (infinite spaces) |
| When to Use | Shortest path, level traversal | Explore all paths, topological sort, cycle detection |

## Time & Space Complexity

### BFS Time Complexity Analysis

BFS time complexity depends on the graph representation:

#### 🔹 Graph Representations

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

> Pattern numbering is kept stable across the three BFS sheets. **Patterns 4.5, 4.6, 6, 8, 8.5, 9, 10, 12, 14 and 15** are deep dives that live in [bfs_advanced.md](./bfs_advanced.md); the `§2-N` worked examples live in [bfs_examples.md](./bfs_examples.md).

### Pattern 1: Basic Tree BFS
```python
from collections import deque

def bfs_tree(root):
    if not root:
        return []
    
    queue = deque([root])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node.val)
        
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    
    return result
```

### Pattern 2: Level-by-Level BFS — LC 102 ⭐⭐⭐⭐⭐
```python
def bfs_levels(root):
    if not root:
        return []
    
    queue = deque([root])
    levels = []
    
    while queue:
        level_size = len(queue)
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

#### Variation: **return at the first leaf popped** — LC 111 (Minimum Depth of Binary Tree)

> **Twist**: don't traverse the whole tree — BFS reaches the shallowest leaf first, so return the moment a node with no children is dequeued. On a long left-skewed spine DFS visits every node; BFS quits at the first leaf. (Contrast with LC 104 Maximum Depth, where you *must* see every level, so DFS recursion is the cleaner tool.)

```python
# python
# LC 111 - Minimum Depth of Binary Tree
# time = O(N) worst case but exits early, space = O(W)
# IDEA: first dequeued leaf = shallowest leaf -> answer
def minDepth(root):
    if not root:
        return 0
    q = deque([root])
    depth = 1
    while q:
        for _ in range(len(q)):
            node = q.popleft()
            if not node.left and not node.right:
                return depth            # early exit: BFS found the shallowest leaf
            if node.left:
                q.append(node.left)
            if node.right:
                q.append(node.right)
        depth += 1
    return depth
```

> Two further level-BFS variations — carrying a **heap index** (LC 662) and **enqueuing the `null` children** (LC 958) — are in [bfs_examples.md](./bfs_examples.md).

### Pattern 3: Graph BFS with Visited Set — LC 200
```python
def bfs_graph(start, graph):
    queue = deque([start])
    visited = set([start])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
    
    return result
```

### Pattern 3.1: The Visited-Set Placement Rule — Mark Before Enqueue ⭐⭐⭐⭐⭐

A critical BFS implementation detail: **always mark a cell as visited (update grid status and counters) BEFORE adding it to the queue**, not when you dequeue it.

#### The Rule

```text
Mark visited + update count → THEN add to queue
```

**General BFS template (canonical form):**
```python
visited = {start}
q.append(start)

while q:
    node = q.popleft()

    for nei in neighbors(node):
        if nei not in visited:
            visited.add(nei)    # <-- before enqueue
            q.append(nei)
```

**3-step pattern when state update is non-trivial (grid mutation, counters):**
```python
# 1. Validate the neighbor
if neighbor_is_valid and neighbor_not_visited:

    # 2. Update state IMMEDIATELY (mark visited / mutate grid / decrement counter)
    mark_as_visited(neighbor)

    # 3. Enqueue the neighbor
    queue.append(neighbor)
```

```java
// ✅ CORRECT: Mark BEFORE enqueue
if (grid[nr][nc] == 1) {
    grid[nr][nc] = 2;       // mark immediately
    freshOrange--;           // update count immediately
    q.add(new int[]{nr, nc});
}

// ❌ WRONG: Mark AFTER dequeue
int[] cur = q.poll();
grid[cur[0]][cur[1]] = 2;   // too late! duplicates already in queue
```

#### Why This Matters

If you defer marking until dequeue, **multiple neighbors can enqueue the same cell** before any of them processes it:

```text
BFS Layer 1: Cells A and B are both neighbors of cell X (fresh orange)

Thread of execution:
  1. Process A → sees X is fresh → enqueues X
  2. Process B → sees X is STILL fresh (not marked yet!) → enqueues X AGAIN
  3. Dequeue X → mark as rotten, freshOrange--
  4. Dequeue X again → already rotten, but freshOrange-- happens again! (WRONG)
```

**Result**: Double-counting, incorrect answers, or wasted processing.

#### Mark-Before-Enqueue guarantees:

| Guarantee | Explanation |
|-----------|-------------|
| **No duplicates in queue** | Cell is marked visited before any other neighbor can see it |
| **Correct counting** | Each cell counted exactly once |
| **O(m x n) time** | Each cell enqueued at most once |
| **Correct BFS layers** | Layer boundaries remain accurate for timing/distance |

#### Cases Where This Applies

| Scenario | Why mark-before-enqueue matters |
|----------|-------------------------------|
| **Counting** (fresh oranges, infections) | Prevents double-decrement of counters |
| **Timing / distance** (minutes elapsed) | Ensures cell is assigned to correct BFS layer |
| **Grid mutation** (spreading rot, flood fill) | Prevents same cell being processed multiple times |
| **Visited tracking via grid values** | Grid itself serves as visited set; must mark before enqueue |

#### When Using a Separate `visited` Set

The same principle applies — add to `visited` **when enqueuing**, not when dequeuing:

```java
// CORRECT
if (!visited[nr][nc]) {
    visited[nr][nc] = true;          // mark BEFORE enqueue
    queue.offer(new int[]{nr, nc});
}

// WRONG
int[] cur = queue.poll();
visited[cur[0]][cur[1]] = true;      // too late
```

#### Related LeetCode Problems

| Problem | Why mark-before-enqueue is critical |
|---------|-------------------------------------|
| **LC 994** - Rotting Oranges | Counter `freshOrange--` must happen exactly once per cell |
| **LC 542** - 01 Matrix | Distance assignment must happen on first (shortest) visit |
| **LC 286** - Walls and Gates | Room distance must not be overwritten by longer path |
| **LC 1162** - As Far from Land as Possible | Same multi-source BFS, distance must be set on first reach |
| **LC 200** - Number of Islands | Marking on enqueue prevents re-visiting same land cell |
| **LC 934** - Shortest Bridge | Expanding island boundary must not double-count water cells |
| **LC 127** - Word Ladder | Words must be marked visited on enqueue to avoid duplicate paths |

#### Summary — mark before vs after enqueue

> In BFS, **the moment you decide a neighbor should enter the queue is the moment you commit** — mark it visited, update your counters, mutate the grid. Never defer state changes to dequeue time. This is not an optimization; it is a **correctness requirement**.

### Pattern 3.2: Grid BFS with a Direction Array — LC 1091
> BFS from top-left to bottom-right through 0-cells (8-directional).
>
> **The idiom**: hoist the moves into a `dirs` array and loop it — `int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}}`
> for the usual 4-neighbour grid (see **Pattern 4**), the 8 entries below when diagonals count. Bounds-check,
> then mark-before-enqueue (**Pattern 3.1**) — here the grid itself is the visited set.

```java
// LC 1091 - Shortest Path in Binary Matrix
// IDEA: BFS — shortest path in unweighted graph
// time = O(N^2), space = O(N^2)
public int shortestPathBinaryMatrix(int[][] grid) {
    int n = grid.length;
    if (grid[0][0] == 1 || grid[n-1][n-1] == 1) return -1;
    int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{0, 0, 1});
    grid[0][0] = 1; // mark visited
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int r = curr[0], c = curr[1], dist = curr[2];
        if (r == n-1 && c == n-1) return dist;
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 0) {
                grid[nr][nc] = 1;
                queue.offer(new int[]{nr, nc, dist + 1});
            }
        }
    }
    return -1;
}
```

### Pattern 4: Multi-Source BFS (Distance Calculation) — LC 542 / LC 994 ⭐⭐⭐⭐⭐
```python
def multi_source_bfs(grid, sources):
    """Start BFS from multiple sources simultaneously"""
    queue = deque(sources)  # All sources at once
    visited = set(sources)

    while queue:
        x, y = queue.popleft()

        for dx, dy in [(0,1), (0,-1), (1,0), (-1,0)]:
            nx, ny = x + dx, y + dy

            if (0 <= nx < len(grid) and 0 <= ny < len(grid[0])
                and (nx, ny) not in visited):
                visited.add((nx, ny))
                queue.append((nx, ny))
```

#### Canonical: Rotting Oranges — multi-source, level = minute
> Spread rot from all initial rotten oranges simultaneously level by level.

```java
// LC 994 - Rotting Oranges
// IDEA: Multi-source BFS
// time = O(M*N), space = O(M*N)
public int orangesRotting(int[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    Queue<int[]> queue = new LinkedList<>();
    int fresh = 0;
    for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 2) queue.offer(new int[]{r, c});
            else if (grid[r][c] == 1) fresh++;
        }
    if (fresh == 0) return 0;
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
                    grid[nr][nc] = 2;
                    fresh--;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
    return fresh == 0 ? minutes : -1;
}
```

**Python Implementation — LC 994:**
```python
# IDEA: MULTI SRC BFS
# time = O(m × n), space = O(m × n)
from collections import deque

def orangesRotting(grid):
    l = len(grid)
    w = len(grid[0])
    fresh = 0
    q = deque()

    for y in range(l):
        for x in range(w):
            if grid[y][x] == 1:
                fresh += 1
            elif grid[y][x] == 2:
                q.append([x, y])

    if fresh == 0:
        return 0
    if not q:
        return -1

    dirs = [[0, 1], [0, -1], [1, 0], [-1, 0]]
    time = 0

    while q and fresh > 0:
        size = len(q)

        for _ in range(size):
            x, y = q.popleft()

            for dx, dy in dirs:
                x_ = x + dx
                y_ = y + dy

                if 0 <= x_ < w and 0 <= y_ < l and grid[y_][x_] == 1:
                    # NOTE: update RIGHT AWAY — before enqueue
                    # to avoid the same fresh orange being rotten several times
                    # (two rotten neighbors in the same layer would both see it as fresh
                    #  and enqueue it twice, causing fresh to go negative)
                    grid[y_][x_] = 2
                    fresh -= 1
                    q.append([x_, y_])

        time += 1  # increment AFTER processing the full level (Approach B)

    return time if fresh == 0 else -1
```

If we deferred `grid[nr][nc] = 2` (Java) / `grid[y_][x_] = 2` (Python) until dequeue, two rotten neighbors processing in the same layer could both enqueue the same fresh orange, leading to `fresh` going negative and returning a wrong answer.

#### Distance variant: 01 Matrix — distance to the nearest source
> Start BFS from all 0-cells simultaneously; distance propagates outward.

```java
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

**Why This Pattern Works:**
1. **Simultaneous Expansion**: All sources expand at same rate → layer by layer
2. **First Visit = Shortest**: In unweighted BFS, first arrival guarantees shortest path
3. **No Backtracking**: Once a cell is visited, we've found its shortest distance
4. **Linear Time**: Each cell visited exactly once → O(m×n) total

**Key Insight - Why Start from 0s, Not 1s?**
- ❌ Starting from each 1 → O(m×n) BFS calls → O(m²×n²) total time
- ✅ Starting from all 0s → Single BFS pass → O(m×n) total time
- **Principle**: Flip the problem - instead of "how far is this 1 from any 0?", ask "how far can all 0s reach?"

> **Where to increment time — rule of thumb:** if you use `time++` at the **beginning** of a level, you MUST have an early-exit condition in the while loop (`&& fresh > 0`). Otherwise use `time++` at the **end** with a flag. Full A-vs-B walkthrough: [bfs_examples.md](./bfs_examples.md) → *When to Increment Time/Distance*.

### Pattern 5: BFS with Path Tracking (carry the path, not the distance)
```python
def bfs_with_path(start, target):
    queue = deque([(start, [start])])
    visited = {start}

    while queue:
        node, path = queue.popleft()

        if node == target:
            return path

        for neighbor in get_neighbors(node):
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, path + [neighbor]))

    return None
```

### Pattern 7: Shortest Path on an Unweighted Graph — BFS + In-Place State Mutation — LC 127 ⭐⭐⭐⭐⭐
```java
/**
 * Pattern: BFS + Backtracking for exploring transformations
 * Use case: Word transformations, state space exploration where each state can transform to multiple neighbors
 * Key insight: Modify state in-place, explore all neighbors, restore state before moving to next position
 *
 * Time: Depends on state space (e.g., O(N * M * 26) for word ladder where N=words, M=length)
 * Space: O(N) for visited set, O(M) for char array
 */
public int bfsWithBacktracking(String beginWord, String endWord, List<String> wordList) {
    Set<String> dict = new HashSet<>(wordList);
    Set<String> visited = new HashSet<>();
    Queue<String> q = new LinkedList<>();

    q.add(beginWord);
    visited.add(beginWord);

    int steps = 1;  // beginWord counts as step 1
    String alpha = "abcdefghijklmnopqrstuvwxyz";

    while (!q.isEmpty()) {
        int size = q.size();

        for (int i = 0; i < size; i++) {
            String cur = q.poll();

            // Early exit when target found
            if (cur.equals(endWord))
                return steps;

            // Convert to char array for efficient modification
            char[] arr = cur.toCharArray();

            /**
             * Key Insight: Backtracking allows exploring ALL transformations
             *
             * For each position, we try ALL 26 letters:
             * - Position 0: try a-z → explore all words with same letters at positions 1,2,...
             * - Position 1: try a-z → explore all words with same letters at positions 0,2,...
             * - Position 2: try a-z → explore all words with same letters at positions 0,1,...
             *
             * This ensures no valid neighbor is missed.
             */
            // Loop 1: Try all positions in the word
            for (int j = 0; j < arr.length; j++) {
                char original = arr[j];

                // Loop 2: Try all 26 letters at this position
                for (char c : alpha.toCharArray()) {
                    if (c == original)
                        continue;

                    /**
                     * TRICK: Modify char array in-place to create new word
                     *
                     * This is more efficient than string concatenation:
                     * ✅ String s = beginWord.substring(0,j) + c + beginWord.substring(j+1)
                     *                    ← Creates new String each time (slow)
                     *
                     * ✅ char[] arr = word.toCharArray();
                     *    arr[j] = c;
                     *    String newStr = new String(arr);  ← Reuse array (fast)
                     */
                    arr[j] = c;
                    String newStr = new String(arr);

                    if (dict.contains(newStr) && !visited.contains(newStr)) {
                        /**
                         * CRITICAL: Mark as visited BEFORE adding to queue
                         *
                         * This prevents duplicate enqueuing:
                         * - If we defer marking until dequeue, multiple neighbors
                         *   could see the same unvisited word and enqueue it multiple times
                         * - Marking before enqueue ensures each word processed exactly once
                         */
                        visited.add(newStr);
                        q.add(newStr);
                    }
                }

                /**
                 * CRITICAL: Restore original character AFTER exploring all 26 letters at this position
                 *
                 * This is the "backtracking" step:
                 * - We modified arr[j] to try all 26 letters
                 * - Before moving to arr[j+1], we must restore arr[j]
                 * - Otherwise, arr[j+1] modification would operate on wrong base state
                 *
                 * Example:
                 * Position 0: Try 'a','b','c',... → restore to 'h'
                 * Position 1: Try 'a','b','c',... → restore to 'i'  ← must have 'h' at position 0!
                 * Position 2: Try 'a','b','c',... → restore to 't'  ← must have 'h','i' at positions 0,1!
                 */
                arr[j] = original;  // Restore before next iteration
            }
        }

        steps++;
    }

    return 0;  // No path found
}
```

> The line-by-line walkthrough of this template — the execution trace, why the restore step is mandatory, and how it compares with the other BFS shapes — is in [bfs_advanced.md](./bfs_advanced.md).

### Pattern 11: Parent Map + BFS Radiating Outward from a Target — LC 863 ⭐⭐⭐⭐⭐

**a. Core idea**

> **"Distance from a node" (not from the root) ⇒ make the tree undirected, then BFS out from that node.**

A binary tree only stores **downward** pointers (`left`, `right`), but nodes at distance `k` from a `target` can sit in **three** places: below it, **above** it, or in a **sibling subtree** (up-then-down). One DFS that only walks down can never reach them.

Fix it in two steps:

1. **DFS once to record `{node: parent}`** — this is the *only* missing edge direction. You don't need a full adjacency map (as in Pattern 10): `left`, `right` are already on the node, so **every node has ≤ 3 neighbors = `(left, right, parent)`**.
2. **BFS from `target` with `(node, dist)`**, expanding into all 3 directions. Because every edge costs 1, **`dist` is the exact tree distance** — when `dist == k`, collect `node.val` and **stop expanding that branch** (`continue`).

**Two things make it correct:**

| Element | Why it's mandatory |
|---|---|
| `visited` set | Adding parent edges makes the graph **undirected** → BFS would bounce `child → parent → child` forever. In a plain top-down tree traversal you never need `visited`; here you always do. |
| `continue` at `dist == k` | Nodes beyond `k` are irrelevant, and their only path back in is through an already-collected node. Skipping expansion caps work and prevents over-collecting. |

**b. Pattern**

```python
# python — LC 863 All Nodes Distance K in Binary Tree
# IDEA: DFS build {node: parent}, then BFS "radiate outward" from target
# time  = O(n)   each node is parented once + enqueued at most once
# space = O(n)   parent map + queue + visited
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
// java — LC 863 All Nodes Distance K in Binary Tree
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

**Visual trace** — `root = [3,5,1,6,2,0,8,null,null,7,4]`, `target = 5`, `k = 2`

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

**Recognition signals**
- Distance / neighbors are measured **from an arbitrary node**, not from the root.
- The answer set can include **ancestors** or nodes in a **sibling subtree**.
- Phrasing like "distance `k` from `target`", "spread / infect from node `start`", "closest X to node `k`".

**Variant: level-BFS `k` times, then dump the queue** — no `dist` in the tuple; after `k` expansions the queue *is* the answer set. Full code, an A-vs-B comparison and the pitfall table: [bfs_examples.md](./bfs_examples.md) §2-18.

> **DFS alternative ("percolate distance")**: a post-order DFS returns the depth of `target` in each subtree; at the node `d` edges above the target you collect nodes `k - d` levels down the *other* child. Also O(n) and O(1) extra beyond recursion — but far easier to get wrong. **BFS + parent map is the interview-safe answer.**

**Parent map (this pattern) vs full adjacency map (Pattern 10)**

| | Parent map `{node: parent}` | Adjacency map `{node: [neighbors]}` |
|---|---|---|
| Built by | one DFS, 1 entry per node | one DFS, 2 entries per edge |
| Neighbor access | `(node.left, node.right, parents[node])` | `graph[node]` |
| Space | ~n pointers | ~2(n-1) list entries |
| Use when | nodes are **real `TreeNode` objects** you can dereference | you work with **values**, or the structure isn't a binary tree |

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| All Nodes Distance K in Binary Tree | 863 | **canonical** — parent map + BFS `k` steps outward |
| Amount of Time for Binary Tree to Be Infected | 2385 | same parent-map BFS; answer = **max** distance (last BFS level) |
| Closest Leaf in a Binary Tree | 742 | BFS out from target, stop at **first leaf** popped (§2-15) |
| Find Distance in a Binary Tree | 1740 | distance between 2 nodes = BFS from one until the other pops (or LCA) |
| Number of Good Leaf Nodes Pairs | 1530 | Pattern 10 — bounded BFS out from *every* leaf |
| Step-By-Step Directions From a Binary Tree Node | 2096 | same up-then-down insight, path reconstruction instead of distance |
| Cousins in Binary Tree | 993 | needs `parent` + `depth` per node — parent map, no BFS radiation |
| All Possible Full Binary Trees / LCA 236 | 236 | LCA is the "turning point" of the up-then-down path |
| Minimum Height Trees | 310 | undirected-tree BFS, trimming inward instead of radiating out (§2-10) |

> **Pattern takeaway**: the instant a tree problem measures something **from a node other than
> the root**, stop thinking "tree recursion" and think **"undirected graph"** — add parent links
> (`{node: parent}` map, or `node.par` annotation), then it is an ordinary BFS where each node
> has 3 neighbors and `visited` is non-negotiable.

---

### Pattern 13: BFS 2-Coloring (Bipartite Check) — LC 785 ⭐⭐⭐⭐

**Key Idea**: BFS does not have to carry a **distance** — it can carry a **label**. Paint the start node `0`, paint every neighbor with the opposite color (`color ^ 1`). If BFS ever meets an already-painted neighbor with the **same** color, an odd-length cycle exists → not bipartite.

**Two traps**:
- **Disconnected graph** — you must loop `for (s = 0..n-1)` and start a fresh BFS on every uncolored node; one BFS only covers one component.
- **Don't use a plain `visited` boolean** — `color[]` with `-1 = unvisited` is both the visited marker *and* the answer.

```java
// java
// LC 785 - Is Graph Bipartite?
// time = O(V + E), space = O(V)
// IDEA: BFS paints alternating colors; same-color neighbor => odd cycle => false
public boolean isBipartite(int[][] graph) {
    int n = graph.length;
    int[] color = new int[n];
    Arrays.fill(color, -1);              // -1 = uncolored (doubles as "unvisited")
    for (int s = 0; s < n; s++) {
        if (color[s] != -1) continue;    // must restart per component
        color[s] = 0;
        Queue<Integer> q = new LinkedList<>();
        q.offer(s);
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nxt : graph[cur]) {
                if (color[nxt] == -1) {
                    color[nxt] = color[cur] ^ 1;   // flip 0 <-> 1
                    q.offer(nxt);
                } else if (color[nxt] == color[cur]) {
                    return false;                  // conflict
                }
            }
        }
    }
    return true;
}
```

```python
# python
# LC 785 - Is Graph Bipartite?
# time = O(V + E), space = O(V)
# IDEA: color[-1]=unvisited; BFS flips color; equal colors on an edge => not bipartite
def isBipartite(graph):
    n = len(graph)
    color = [-1] * n
    for s in range(n):
        if color[s] != -1:
            continue                  # component already done
        color[s] = 0
        q = deque([s])
        while q:
            cur = q.popleft()
            for nxt in graph[cur]:
                if color[nxt] == -1:
                    color[nxt] = color[cur] ^ 1
                    q.append(nxt)
                elif color[nxt] == color[cur]:
                    return False
    return True
```

**Note**: the same "BFS carries a label, not a distance" trick also solves "split people into 2 groups that dislike each other" style questions — build the adjacency list from the pairs first, then run this code unchanged.

---

## Summary & Quick Reference

> `Pattern 4.5 / 4.6 / 6 / 8 / 8.5 / 9 / 10 / 12 / 14 / 15` below live in [bfs_advanced.md](./bfs_advanced.md); `§2-N` references live in [bfs_examples.md](./bfs_examples.md).

### When to Use BFS
- Finding shortest path in unweighted graphs
- Level-order tree traversal
- Finding connected components
- Checking if graph is bipartite
- Web crawling (breadth-first exploration)
- **Simultaneous multi-source distance calculations** (Pattern 4) - distance to nearest source
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

#### ❌ Common Mistakes
1. Using `queue.pop()` instead of `queue.popleft()` with list
2. Not handling visited set in graphs (infinite loops)
3. Forgetting level-by-level processing when needed
4. Incorrect boundary checking in grid problems

#### ✅ Best Practices
1. Use `collections.deque` for better performance
2. Always use visited set for graph problems
3. Check boundaries before adding to queue in grid problems
4. Consider multi-source BFS for optimization
5. Track level/distance when needed for shortest path
6. **Mark state BEFORE enqueue, not after dequeue** — update grid/visited/counters the moment you decide to enqueue a neighbor; deferring until dequeue lets multiple neighbors re-enqueue the same cell (see **Pattern 3.1** above)

### Problems by Category

#### 1. Tree Traversal Problems
- **Level Order Traversal**: LC 102, 107, 103
- **Binary Tree Paths**: LC 257, 1022
- **Right Side View**: LC 199
- **Vertical Order**: LC 314
- **Level-wise Tree Mutation**: LC 623 (Add One Row), LC 116/117 (Next Right Pointers)
- **Distance from an arbitrary node (Pattern 11 — parent map + radiate out)**: LC 863 (Distance K), LC 2385 (Tree Infection), LC 742 (Closest Leaf), LC 1740 (Find Distance)

#### 2. Shortest Path Problems
- **Unweighted Graphs**: LC 127 (Word Ladder)
- **Grid Navigation**: LC 1730 (Shortest Path to Food), LC 1091 (Shortest Path in Binary Matrix)
- **Simultaneous Multi-source Distance (Pattern 4)**:
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
| Easy | LC 102 | Level-order traversal | Pattern 2 (Level-by-Level) |
| **Medium** | **LC 127** | **Shortest path transformation - Word Ladder** | **Pattern 7 (Unweighted Shortest Path)** |
| Medium | LC 200 | Connected components | Pattern 3 (Graph BFS) |
| Medium | LC 742 | Closest leaf (tree → undirected graph) | `bfs_examples.md` §2-15 (Tree → Graph + BFS) |
| Medium | LC 863 | Distance `k` from a **target node** (parent map, 3 neighbors) | Pattern 11 (Radiate Outward); shape B in `bfs_examples.md` §2-18 |
| Medium | LC 623 | Level BFS to `depth - 1`, rewire child pointers | `bfs_examples.md` §2-17 (Add One Row to Tree) |
| **Medium** | **LC 542** | **Simultaneous multi-source - 01 Matrix** | **Pattern 4 (Simultaneous Multi-Source)** |
| Medium | LC 934 | DFS + Multi-source BFS (island expansion) | Pattern 4.5 (DFS + Multi-Source) |
| Medium | LC 1162 | As Far from Land as Possible | Pattern 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 126** | **Find ALL shortest paths - Word Ladder II** | **Pattern 8.5 (BFS + DFS DAG Enumeration)** |
| Hard | LC 286 | Walls and Gates | Pattern 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 317** | **Independent BFS runs (sum of distances)** | **Pattern 4.6 (Independent BFS Runs)** |
| Hard | LC 675 | Sort + Repeated BFS (sequential targets) | Pattern 6 (Sort + Repeated BFS) |
| **Hard** | **LC 752** | **BFS on state space - Open the Lock** | **Pattern 7 (Unweighted Shortest Path); worked in `bfs_examples.md` §2-6** |
| **Hard** | **LC 815** | **Route-level BFS (minimum buses)** | **Pattern 8 (Route-Level BFS)** |
| Hard | LC 864 | BFS with state (key collection) | Pattern 3 + State |
| Hard | LC 1293 | BFS with state (obstacle elimination) | Pattern 3 + State |

### Also Frequently Asked (no new template — they reuse the ones above)

| LC | Problem | Which template it reuses |
|----|---------|--------------------------|
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | Pattern 2 level BFS writing `null` markers; deserialize = same queue read back (see Variation C in [bfs_examples.md](./bfs_examples.md)) |
| 104 | Maximum Depth of Binary Tree | Pattern 2 — count levels; DFS recursion is shorter here, BFS wins only for LC 111 |
| 101 | Symmetric Tree | Pattern 2 with a **pair queue** — enqueue `(left, right)` mirrored and compare on pop |
| 637 / 515 | Average of Levels / Largest Value in Each Tree Row | Pattern 2 — swap "collect the level" for "aggregate the level" (avg / max) |
| 433 | Minimum Genetic Mutation | Same template as LC 127 / 752 — 8-char gene string, 4 letters, bank = valid-state set |
| 529 | Minesweeper | Pattern 3 grid BFS — only expand a cell when its adjacent-mine count is `0`, otherwise write the digit and stop |
| 547 | Number of Provinces | Pattern 3 — count how many BFS runs it takes to cover all nodes (or Union-Find) |
| 1376 | Time Needed to Inform All Employees | Pattern 2 on the manager tree, queue holds `(employee, timeSoFar)` — answer is the max |
| 787 | Cheapest Flights Within K Stops | BFS **level-bounded relaxation** (Bellman-Ford flavored): run exactly `k+1` levels and **do not use a global visited** — a node may be re-entered with a cheaper cost. See `Dijkstra.md`. |
| 329 | Longest Increasing Path in a Matrix | Not a BFS problem — DFS + memo, or Kahn's BFS on the DAG (see `topology_sorting.md`) |
| 721 / 947 / 684 / 839 | Accounts Merge / Stones Removed / Redundant Connection / Similar String Groups | Connectivity, not shortest path — Union-Find is the expected answer (BFS flood-fill also works) |
