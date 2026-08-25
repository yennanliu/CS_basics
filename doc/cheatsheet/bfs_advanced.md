# BFS — Advanced Variants

> **Scope** — The BFS techniques a first pass should skip: bidirectional BFS, 0-1 BFS with a deque, multi-source beyond the canonical template, BFS over implicit state spaces, and all-shortest-path DAG enumeration — the must-know queue templates stay in the main sheet.
> **See also**: [bfs.md](./bfs.md) — the canonical templates these variants build on, and where `Pattern 1-5 / 7 / 11 / 13` references point; [bfs_examples.md](./bfs_examples.md) — the worked-solution archive; [Dijkstra.md](./Dijkstra.md) — once edges carry arbitrary weights.

## LeetCode Problem Lists

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)

## Overview

Everything here is a variant of a template in [bfs.md](./bfs.md): the queue payload changes, the queue becomes a deque, the graph becomes implicit, or one BFS becomes many. Pattern numbering is shared across the three BFS sheets, so `Pattern 4` / `Pattern 11` below refer to the main sheet.

| Variant | Push / payload rule | Cost |
|---|---|---|
| Multi-source (canonical, see `bfs.md` Pattern 4) | all sources seeded at level 0 | O(V + E) |
| Independent BFS runs (Pattern 4.6) | fresh `visited` per source | O(k(V + E)) |
| 0-1 BFS (Pattern 15) | deque: cost-0 front, cost-1 back | O(V + E) |
| Bidirectional | expand the smaller frontier | ~O(b^(d/2)) |
| Value-carrying (Pattern 14) | `(node, valueSoFar)` | O(V + E) per query |
| Priority (Dijkstra-like) | heap ordered by distance | O(E log V) |

## Multi-Source BFS — Deep Dive

### Initialization Strategies and Execution Trace

**Java Implementation (LC 542 - 01 Matrix Pattern):**
```java
/**
 * Pattern: Multi-Source BFS for Distance Calculation
 * Use case: Calculate shortest distance from each cell to any source cell
 * Key insight: Start BFS from ALL sources simultaneously - first visit guarantees shortest path
 *
 * Time: O(m × n) - each cell visited at most once
 * Space: O(m × n) - queue can hold entire grid in worst case
 */
public int[][] multiSourceBFS(int[][] mat) {
    int rows = mat.length;
    int cols = mat[0].length;
    Queue<int[]> queue = new LinkedList<>();

    // Step 1: Initialize - Add all sources (0s) to queue, mark others as unvisited
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (mat[r][c] == 0) {
                queue.offer(new int[]{r, c});  // Multi-source starting points
            } else {
                // Mark as unvisited - two common approaches:
                // Option A: mat[r][c] = -1 (easier to check)
                // Option B: mat[r][c] = Integer.MAX_VALUE (easier for min comparison)
                mat[r][c] = -1;
            }
        }
    }

    int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

    // Step 2: BFS expansion from all sources
    while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        int r = cur[0], c = cur[1];

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            // Only process unvisited cells
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mat[nr][nc] == -1) {
                // KEY: Distance = parent's distance + 1
                mat[nr][nc] = mat[r][c] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }

    return mat;
}
```

**Concrete Example: LC 542 - 01 Matrix**
```text
Problem: Find distance to nearest 0 for each cell
Input:  [[0,0,0],     Output: [[0,0,0],
         [0,1,0],              [0,1,0],
         [1,1,1]]              [1,2,1]]

Execution trace:
Step 1 - Initialize:
  Queue: [(0,0), (0,1), (0,2), (1,0), (1,2)]  ← All 0s
  Grid:  [[0, 0, 0],
          [0, -1, 0],
          [-1, -1, -1]]

Step 2 - BFS Layer 1 (distance = 1):
  Process (0,0): Check (1,0) - already 0, skip
  Process (0,1): Check (1,1) - is -1, update to 1, enqueue
  Process (1,0): Check (2,0) - is -1, update to 1, enqueue

  Grid:  [[0, 0, 0],
          [0, 1, 0],
          [1, -1, -1]]
  Queue: [(1,1), (2,0), ...]

Step 3 - BFS Layer 2 (distance = 2):
  Process (1,1): Check (2,1) - is -1, update to 2, enqueue
  Process (2,0): Check (2,1) - is -1, update to 2, enqueue (redundant)

  Final: [[0, 0, 0],
          [0, 1, 0],
          [1, 2, 1]]
```

### Multi-Source BFS Distance Calculation (LC 542 Pattern)

**The Problem Type:**
Calculate shortest distance from each cell to ANY source cell in a grid.

**Why Multi-Source BFS?**
```text
❌ Naive Approach: Start BFS from each target cell
   - For each 1, run BFS to find nearest 0
   - Time: O(m×n) targets × O(m×n) BFS = O(m²×n²) ❌

✅ Multi-Source Approach: Start BFS from ALL sources simultaneously
   - Add all 0s to queue initially
   - Run single BFS that expands from all sources
   - Time: O(m×n) - each cell visited once ✅
```

**Key Implementation Details:**

1. **Initialization Strategy:**
   ```java
   // Option A: Use sentinel value -1
   mat[r][c] = -1;  // Easier to check: if (mat[nr][nc] == -1)

   // Option B: Use MAX_VALUE
   mat[r][c] = Integer.MAX_VALUE;  // Easier for comparison: if (mat[nr][nc] > mat[r][c] + 1)
   ```

2. **The Update Condition:**
   ```java
   // Why only update when new distance is shorter?
   if (mat[nr][nc] > mat[r][c] + 1) {
       mat[nr][nc] = mat[r][c] + 1;
       queue.offer(new int[]{nr, nc});
   }

   // Explanation:
   // - In unweighted BFS, first visit = shortest path
   // - If cell already has distance ≤ current + 1, it has a better path
   // - Prevents redundant re-processing and ensures O(m×n) time
   ```

3. **Why First Visit = Shortest Distance:**
   ```
   BFS expands in layers (level-by-level):
   Layer 0: All sources (distance = 0)
   Layer 1: All cells 1 step away (distance = 1)
   Layer 2: All cells 2 steps away (distance = 2)
   ...

   When BFS first reaches a cell, it MUST be via the shortest path
   because all shorter paths were explored in earlier layers.
   ```

**Pattern Recognition - Use Multi-Source BFS When:**
- Need distance from each cell to ANY source (not a specific source)
- Multiple sources exist naturally in the problem
- Problem asks for "nearest/closest" among multiple options
- Can "flip" the problem (start from targets instead of sources)

**Similar Problems Using This Pattern:**
- LC 542: 01 Matrix (distance to nearest 0)
- LC 1162: As Far from Land as Possible (distance to nearest land)
- LC 286: Walls and Gates (distance from gates to rooms)
- LC 994: Rotting Oranges (time for all oranges to rot)
- LC 1765: Map of Highest Peak (assign heights with constraints)

### Pattern 4.5: DFS + Multi-Source BFS (Island Expansion) — LC 934
```java
/**
 * Pattern: DFS to identify first component, then Multi-Source BFS to find shortest distance to second component
 * Use case: Find shortest bridge between two islands, connect two separate regions
 * Key insight: DFS marks entire first island, BFS expands from ALL cells of first island simultaneously
 *
 * Time: O(m × n) - each cell visited at most once by DFS + once by BFS
 * Space: O(m × n) - queue can hold entire island boundary
 */
public int dfsMarkThenMultiSourceBFS(int[][] grid) {
    int n = grid.length;
    Queue<int[]> queue = new LinkedList<>();
    boolean found = false;

    // Step 1: DFS to find and mark first island (change 1 → 2)
    // Add ALL cells of first island to queue for multi-source BFS
    for (int y = 0; y < n && !found; y++) {
        for (int x = 0; x < n && !found; x++) {
            if (grid[y][x] == 1) {
                dfsMarkIsland(grid, x, y, queue);
                found = true;
            }
        }
    }

    // Step 2: Multi-Source BFS from entire first island
    // Expand outward layer by layer until reaching second island
    int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    int steps = 0;
    boolean[][] visited = new boolean[n][n];

    while (!queue.isEmpty()) {
        int size = queue.size();

        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx >= 0 && nx < n && ny >= 0 && ny < n && !visited[ny][nx]) {
                    visited[ny][nx] = true;

                    if (grid[ny][nx] == 1) {
                        return steps; // Reached second island
                    }

                    if (grid[ny][nx] == 0) {
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }
        steps++;
    }

    return -1;
}

// DFS helper: Mark all cells of first island and add to queue
void dfsMarkIsland(int[][] grid, int x, int y, Queue<int[]> queue) {
    int n = grid.length;
    if (x < 0 || x >= n || y < 0 || y >= n || grid[y][x] != 1) {
        return;
    }

    grid[y][x] = 2; // Mark as visited (part of first island)
    queue.add(new int[]{x, y}); // Add to BFS queue

    // Recursively mark all connected cells
    dfsMarkIsland(grid, x + 1, y, queue);
    dfsMarkIsland(grid, x - 1, y, queue);
    dfsMarkIsland(grid, x, y + 1, queue);
    dfsMarkIsland(grid, x, y - 1, queue);
}
```

**Concrete Example: LC 934 - Shortest Bridge**
```text
Problem: Connect two islands with minimum number of flips (0→1)
Grid: [[0,1],     Two islands: Island A at (0,1), Island B at (1,0)
       [1,0]]     Need to flip 1 cell to connect them

Step 1 - DFS marks Island A:
Original: [0,1]  →  After DFS: [0,2]  (2 = marked as first island)
          [1,0]                [1,0]
Queue: [(1,0)] - all cells of first island

Step 2 - BFS Layer 0 (from first island):
Check neighbors of (1,0):
- (0,0): water, add to queue → Queue: [(0,0)]
- (1,1): water, add to queue → Queue: [(0,0), (1,1)]
After Layer 0: steps = 0

Step 3 - BFS Layer 1:
Process (0,0):
  - (1,0): already visited (marked as 2)
  - (0,1): FOUND Island B (value = 1)! Return steps = 0

Result: 1 flip needed (but we count layers, answer may vary based on problem definition)

Key insight:
- DFS ensures we mark ENTIRE first island (not just one cell)
- Multi-source BFS expands from ALL boundary cells simultaneously
- This guarantees we find the absolute shortest bridge
```

**Why This Pattern Works:**
1. **Complete Coverage**: DFS ensures we find the entire first island, not just part of it
2. **Optimal Distance**: Multi-source BFS from all island cells guarantees shortest path
3. **No Redundant Work**: Each cell visited at most once in DFS + once in BFS
4. **Natural Layering**: BFS level corresponds to bridge length

**Pattern Characteristics:**
- **DFS Phase**: O(m × n) worst case - mark entire first island
- **BFS Phase**: O(m × n) worst case - expand to entire grid
- **Total Time**: O(m × n) - each cell visited constant times
- **Space**: O(m × n) - recursion stack + queue + visited array

**When to Use This Pattern:**
- Find shortest connection between two separate components
- One component needs complete identification before distance calculation
- Problem requires expanding from entire boundary of a region
- Grid has exactly two distinct regions/islands

**Key Variations:**
1. **Boundary-Only BFS**: Only add island boundary cells to queue (optimization)
2. **Bidirectional BFS**: Expand from both islands simultaneously (faster)
3. **Modified Grid**: Mark visited cells in original grid (space optimization)
4. **Different Marking**: Use different values (2, -1) based on problem requirements

**Similar Problems:**
- LC 934: Shortest Bridge (connect two islands)
- LC 1162: As Far from Land as Possible (distance from any land cell)
- LC 542: 01 Matrix (distance to nearest 0 from each 1)
- LC 286: Walls and Gates (distance from gates to rooms)
- LC 1020: Number of Enclaves (count land cells not connected to boundary)

### Pattern 4.6: Multi-Source BFS vs Independent BFS Runs (Critical Distinction)

**🚨 IMPORTANT: This is the #1 source of confusion in multi-source BFS problems!**

Many students confuse these two fundamentally different patterns:

#### **Type 1: Simultaneous Multi-Source BFS** (Patterns 4, 4.5)
- **Goal**: Find distance to the **NEAREST** source from each cell
- **Setup**: Add ALL sources to queue at `time = 0`
- **Visited**: ONE shared `visited` array/set for entire BFS
- **Logic**: All sources expand simultaneously, layer by layer
- **Result**: Each cell knows its distance to the **closest** source

**Example Problems:**
- LC 542 (01 Matrix): Distance to nearest 0
- LC 994 (Rotting Oranges): Time for infection to spread
- LC 1162 (As Far from Land): Distance to nearest land

```java
// Simultaneous Multi-Source BFS Template
public int[][] simultaneousMultiSourceBFS(int[][] grid) {
    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visited = new boolean[rows][cols];

    // Add ALL sources to queue at once
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == SOURCE) {
                queue.offer(new int[]{r, c});
                visited[r][c] = true;  // ONE shared visited array
            }
        }
    }

    // Single BFS run - all sources expand together
    while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        // Process neighbors...
        // First visit to any cell = shortest distance from ANY source
    }
}
```

#### **Type 2: Independent BFS Runs** (One BFS per source)
- **Goal**: Find **SUM of distances** or **aggregate metric** across ALL sources
- **Setup**: Run separate BFS for EACH source, one at a time
- **Visited**: FRESH `visited` array for EACH BFS run
- **Logic**: Each source independently explores the entire reachable space
- **Result**: Each cell accumulates distances/metrics from ALL sources

**Example Problem:**
- LC 317 (Shortest Distance from All Buildings): Sum of distances to all buildings

```java
// Independent BFS Runs Template - LC 317 Pattern
public int independentBFSRuns(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;

    // Global accumulator - each BFS adds to this
    int[][] totalDist = new int[rows][cols];
    int[][] reachCount = new int[rows][cols];

    int buildingCount = 0;

    // Run SEPARATE BFS for each source
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 1) {  // Found a building (source)
                buildingCount++;

                // FRESH visited array for this building's BFS
                boolean[][] visited = new boolean[rows][cols];

                bfsSingleSource(grid, r, c, visited, totalDist, reachCount);
            }
        }
    }

    // Find best cell that was reached by ALL buildings
    int minDist = Integer.MAX_VALUE;
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 0 && reachCount[r][c] == buildingCount) {
                minDist = Math.min(minDist, totalDist[r][c]);
            }
        }
    }

    return minDist == Integer.MAX_VALUE ? -1 : minDist;
}

// BFS from single source - accumulates distances
private void bfsSingleSource(int[][] grid, int sr, int sc,
                             boolean[][] visited,
                             int[][] totalDist,
                             int[][] reachCount) {
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{sr, sc});
    visited[sr][sc] = true;

    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int dist = 0;

    while (!queue.isEmpty()) {
        int size = queue.size();
        dist++;

        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];

                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length
                    && !visited[nr][nc] && grid[nr][nc] == 0) {

                    visited[nr][nc] = true;

                    // Accumulate distance from this building
                    totalDist[nr][nc] += dist;
                    reachCount[nr][nc]++;

                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
```

#### **Comparison Table**

| Aspect | Simultaneous Multi-Source | Independent BFS Runs |
|--------|---------------------------|----------------------|
| **Queue Init** | Add ALL sources at once | Each source starts its own BFS |
| **Visited Array** | ONE shared across entire BFS | FRESH for each BFS run |
| **Time Complexity** | O(m×n) - single pass | O(k × m×n) where k = # sources |
| **First Visit Means** | Distance to NEAREST source | Distance from CURRENT source |
| **Use Case** | Find nearest/closest | Find sum/aggregate across all |
| **Example** | LC 542, 994, 1162 | LC 317 |

#### **Why Fresh Visited Arrays in Independent BFS?**

**The Key Question:** *"Why can't we reuse the visited array across different buildings in LC 317?"*

**The Answer:**
```text
Building A runs BFS:
  - Visits land cell (2,3) and marks it visited ✓
  - Calculates: distance from A to (2,3) = 5 steps

Building B runs BFS:
  - If we reuse visited array, cell (2,3) is still marked as visited!
  - We would SKIP (2,3) and never calculate distance from B to (2,3) ❌

But we NEED both distances because:
  - totalDist[2][3] = distFromA + distFromB + distFromC + ...
```

**Each building needs to "see" every empty cell independently** to contribute its distance.

#### **Common Mistake Example**

```java
// ❌ WRONG - Reusing visited array
boolean[][] visited = new boolean[rows][cols];  // Created ONCE

for (Building b : allBuildings) {
    bfs(b, visited);  // All buildings share same visited array
    // Later buildings can't visit cells that earlier buildings marked!
}

// ✅ CORRECT - Fresh visited array
for (Building b : allBuildings) {
    boolean[][] visited = new boolean[rows][cols];  // Fresh each time
    bfs(b, visited);  // Each building can visit all reachable cells
}
```

#### **Optimization: Grid Value Trick** (Space-efficient alternative)

Instead of creating fresh `boolean[][] visited` arrays, modify the grid itself:

```java
// LC 317 Optimization: Decrement empty cells for each building
public int shortestDistance(int[][] grid) {
    int[][] totalDist = new int[rows][cols];
    int emptyValue = 0;  // Changes with each BFS: 0 → -1 → -2 → -3...
    int buildingCount = 0;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 1) {
                buildingCount++;

                // BFS from this building, only visit cells with value = emptyValue
                bfsWithGridMarking(grid, r, c, emptyValue, totalDist);

                emptyValue--;  // Next building looks for different value
            }
        }
    }

    // Find best cell with value = (emptyValue + 1)
    // That cell was reached by ALL buildings
}

private void bfsWithGridMarking(int[][] grid, int sr, int sc,
                               int targetValue, int[][] totalDist) {
    // Only process cells with grid[r][c] == targetValue
    // After processing, change to (targetValue - 1)
    // This ensures cell must be reached by ALL previous buildings
}
```

**How Grid Trick Works:**
```text
Initial grid: All empty cells = 0

Building 1 BFS:
  - Visit cells with value 0
  - Change them to -1 after visiting
  - Now empty cells = -1

Building 2 BFS:
  - Only visit cells with value -1
  - Change them to -2 after visiting
  - Now only cells reachable by BOTH buildings = -2

Building 3 BFS:
  - Only visit cells with value -2
  - Change them to -3
  - Only cells reachable by ALL 3 buildings = -3
```

**Benefits:**
- ✅ No need for `boolean[][] visited` arrays (saves space)
- ✅ Automatically filters cells unreachable by earlier buildings
- ✅ Final value indicates how many buildings reached that cell

#### **When to Use Which Pattern?**

**Use Simultaneous Multi-Source (Pattern 4) when:**
- ✅ Need distance to **nearest** source
- ✅ Only care about the **closest** one
- ✅ Problem asks: "minimum distance to ANY..."
- ✅ Want O(m×n) time complexity

**Use Independent BFS Runs (Pattern 4.6) when:**
- ✅ Need **sum** of distances to **all** sources
- ✅ Need to know if cell is reachable from **every** source
- ✅ Problem asks: "find position that minimizes total distance..."
- ✅ Willing to accept O(k × m×n) time complexity

#### **Quick Recognition Guide**

| Problem Statement Contains... | Pattern to Use |
|-------------------------------|----------------|
| "distance to **nearest** building" | Simultaneous Multi-Source |
| "**sum** of distances to all buildings" | Independent BFS Runs |
| "infection spreads from all sources" | Simultaneous Multi-Source |
| "all friends can reach in **minimum total** time" | Independent BFS Runs |
| "find the cell **closest** to any land" | Simultaneous Multi-Source |

## State-Space & Implicit-Graph BFS

### Pattern 7 Walkthrough: Why the Restore Step Matters — LC 127

> The template itself is **Pattern 7** in [bfs.md](./bfs.md); everything below dissects it.

**Concrete Example: LC 127 - Word Ladder**

```text
Problem: Transform "hit" → "cog" using dictionary ["hot","dot","dog","lot","log","cog"]
Expected: 5 (hit → hot → dot → dog → cog)

BFS + Backtracking Execution:

Layer 0: Queue = [hit], steps = 1
  Process "hit":
    Position 0: h→a,b,c,...,z  (none in dict)
    Position 1: i→a,b,c,...,o,... → "hot" ✓ add to queue
    Position 2: t→a,b,c,...,g,... (none in dict besides "hit" itself)
  After Layer 0: Queue = [hot]

Layer 1: Queue = [hot], steps = 2
  Process "hot":
    Position 0: h→a,b,c,...,d → "dot" ✓, "lot" ✓
    Position 1: o→... (backtrack, restore 'o')
    Position 2: t→... (none found)
  After Layer 1: Queue = [dot, lot]

Layer 2: Queue = [dot, lot], steps = 3
  Process "dot":
    Position 0: d→... (none found)
    Position 1: o→... (none found)
    Position 2: t→g → "dog" ✓
  Process "lot":
    Position 0: l→... (none found)
    Position 1: o→... (none found)
    Position 2: t→g → "log" ✓
  After Layer 2: Queue = [dog, log]

Layer 3: Queue = [dog, log], steps = 4
  Process "dog":
    Position 0: d→... (none found)
    Position 1: o→... (none found)
    Position 2: g→... (none found)
  Process "log":
    Position 0: l→... (none found)
    Position 1: o→... (none found)
    Position 2: g→c → "cog" ✓
  After Layer 3: Queue = [cog]

Layer 4: Queue = [cog], steps = 5
  Process "cog":
    cur.equals(endWord) == true
  RETURN steps = 5 ✓
```

**Why Backtracking is Essential Here:**

```text
❌ Naive Approach (without backtracking):
   For each position, generate ONE new word per letter
   Problem: Must process all positions with CORRECT base state

✅ Backtracking Approach:
   1. Modify position 0 → try all 26 letters
   2. Restore position 0 to original
   3. Modify position 1 → try all 26 letters (with position 0 restored!)
   4. Restore position 1 to original
   5. Continue to position 2, etc.

   Result: Each position explored independently with correct base state
```

**Pattern Characteristics:**

- **State Modification**: In-place modification of mutable state (char array)
- **Exploration**: Try all possibilities at each "decision point" (position)
- **Restoration**: Undo changes before moving to next decision point
- **BFS Integration**: Process states level-by-level to find shortest path
- **Visited Tracking**: Prevent re-exploring same state (before enqueue)

**When to Use This Pattern:**

- ✅ Word transformation problems (Word Ladder, Word Ladder II)
- ✅ State space exploration where state can be modified in-place
- ✅ Need to try ALL neighbors systematically
- ✅ Neighbors differ by exactly ONE element (one char, one digit, one bit, etc.)
- ✅ Want to find shortest path through state space

**Key Implementation Details:**

1. **Mark Before Enqueue**: Add to visited set BEFORE adding to queue
   - Prevents duplicate processing
   - Ensures O(state_space) time complexity

2. **Restore After Inner Loop**: Restore state after trying all variations at one position
   - Ensures correct base state for next position
   - This is the "backtracking" aspect

3. **Efficient State Creation**: Use char array modification instead of string concatenation
   - Reuse same array object
   - Only recreate string when needed
   - Much faster than substring operations

4. **Early Exit**: Check for target when dequeuing (not after modification)
   - Allows immediate return when target found
   - Saves unnecessary exploration

**Comparison with Other Patterns:**

| Pattern | State Modification | Restoration | Use Case |
|---------|-------------------|-------------|----------|
| **BFS + Backtracking** | ✓ In-place | ✓ Required | Word transformations, state exploration |
| **BFS + Queue Pairs** | ✗ Create new | N/A | Simple shortest path without transformation |
| **DFS + Backtracking** | ✓ In-place | ✓ Required | All paths, permutations, combinations |
| **Standard BFS** | ✗ Create new | N/A | Graph traversal with pre-built adjacency |

**Similar Problems:**

- LC 127: Word Ladder (find shortest transformation sequence)
- LC 126: Word Ladder II (find ALL shortest transformation sequences - use DFS + backtracking instead)
- LC 752: Open the Lock (similar BFS pattern on digit combinations)
- LC 1008: Construct Binary Search Tree from Preorder Traversal (different pattern)

### Pattern 8: BFS on Abstract Graph (Route-Level BFS) — LC 815
```java
/**
 * Pattern: BFS where nodes are ROUTES (buses/lines), not physical locations
 * Use case: Find minimum number of transfers/buses to reach a destination
 * Key insight: Build stop→routes mapping, BFS on routes with two visited sets (buses + stops)
 *
 * Time: O(N * M) where N = number of routes, M = avg stops per route
 * Space: O(N * M) for the stop-to-routes map and visited sets
 */
public int routeLevelBFS(int[][] routes, int source, int target) {
    if (source == target) return 0;

    // Step 1: Build mapping from stop → list of route IDs
    Map<Integer, List<Integer>> stopToRoutes = new HashMap<>();
    for (int i = 0; i < routes.length; i++) {
        for (int stop : routes[i]) {
            stopToRoutes.computeIfAbsent(stop, k -> new ArrayList<>()).add(i);
        }
    }

    // Step 2: BFS on route IDs (not stops!)
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visitedRoutes = new HashSet<>();
    Set<Integer> visitedStops = new HashSet<>();

    // Seed: all routes that pass through the source stop
    for (int routeId : stopToRoutes.getOrDefault(source, new ArrayList<>())) {
        queue.offer(routeId);
        visitedRoutes.add(routeId);
    }

    int busCount = 1; // Already on the first bus

    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int currRoute = queue.poll();

            // Check all stops on this route
            for (int stop : routes[currRoute]) {
                if (stop == target) return busCount;

                if (visitedStops.contains(stop)) continue;
                visitedStops.add(stop);

                // Transfer: enqueue all OTHER routes at this stop
                for (int nextRoute : stopToRoutes.getOrDefault(stop, new ArrayList<>())) {
                    if (!visitedRoutes.contains(nextRoute)) {
                        visitedRoutes.add(nextRoute);
                        queue.offer(nextRoute);
                    }
                }
            }
        }
        busCount++;
    }

    return -1;
}
```

**Concrete Example: LC 815 - Bus Routes**
```text
Problem: Find minimum buses to travel from source=1 to target=6
Routes: [[1,2,7], [3,6,7]]
  Route 0: stops 1→2→7→1→...
  Route 1: stops 3→6→7→3→...

Step 1 - Build stop→routes map:
  1 → [Route 0]
  2 → [Route 0]
  7 → [Route 0, Route 1]   ← transfer point!
  3 → [Route 1]
  6 → [Route 1]

Step 2 - BFS:
  Source stop = 1 → seed Route 0 into queue
  Queue: [Route 0], busCount = 1

  Layer 1 (busCount = 1):
    Process Route 0 → check stops [1, 2, 7]:
      Stop 1: not target. Routes at stop 1 = [Route 0] (already visited)
      Stop 2: not target. Routes at stop 2 = [Route 0] (already visited)
      Stop 7: not target. Routes at stop 7 = [Route 0, Route 1]
        → Route 1 not visited → enqueue Route 1
    Queue: [Route 1]

  busCount++ → busCount = 2

  Layer 2 (busCount = 2):
    Process Route 1 → check stops [3, 6, 7]:
      Stop 3: not target
      Stop 6: == target! → return busCount = 2 ✓
```

**Why Two Visited Sets?**
```text
visitedRoutes: Prevents boarding the same bus twice (infinite loop)
visitedStops:  Prevents re-processing transfer points
               (stop 7 connects Routes 0 and 1, but once explored, no need to revisit)

Without visitedStops: Every stop would re-check all its routes
  → Redundant work, potentially O(N²*M) instead of O(N*M)
```

**Why BFS on Routes, Not Stops?**
```text
❌ BFS on stops: Queue = [stop1, stop2, ...]
   Problem: How do you define "neighbors" of a stop?
   All other stops on the SAME route → huge adjacency list
   Loses the concept of "how many buses taken"

✅ BFS on routes: Queue = [route0, route1, ...]
   Each BFS layer = one bus ride
   Transfer = finding a new route at a shared stop
   busCount directly maps to BFS depth
```

**When to Use This Pattern:**
- Minimum number of transfers/vehicles/connections
- Nodes in BFS are abstract entities (routes, lines, groups), not physical locations
- Problem involves shared stops/stations between routes
- Need to count transitions between groups, not individual steps

**Similar Problems:**
- LC 815: Bus Routes (minimum buses to reach target)
- LC 127: Word Ladder (can be seen as BFS on word groups — Pattern 7 is more natural)
- LC 841: Keys and Rooms (BFS/DFS on rooms accessed via keys)
- LC 1197: Minimum Knight Moves (BFS on chess positions)

---

### Pattern 8.5: BFS + DFS (Find All Shortest Paths - DAG Enumeration) — LC 126
```java
/**
 * Pattern: BFS to build shortest-path DAG, then DFS to enumerate all paths
 * Use case: Find ALL shortest transformation sequences (not just one)
 * Key insight: BFS builds a reverse graph of predecessors, DFS reconstructs all valid paths
 *
 * Time: O(N * M * 26 + paths) where N=words, M=length, paths=output size
 * Space: O(N * M) for graph + O(M) for DFS recursion stack
 */
public List<List<String>> findAllShortestPaths(String beginWord, String endWord, List<String> wordList) {
    List<List<String>> result = new ArrayList<>();
    Set<String> wordSet = new HashSet<>(wordList);

    if (!wordSet.contains(endWord))
        return result;

    // Map to store: word → list of predecessors (parents) at shortest distance
    Map<String, List<String>> parents = new HashMap<>();

    // Map to store: word → shortest distance from beginWord
    Map<String, Integer> distances = new HashMap<>();

    // ========== PHASE 1: BFS to build shortest-path DAG ==========
    Queue<String> queue = new LinkedList<>();
    queue.add(beginWord);
    distances.put(beginWord, 0);

    boolean found = false;
    String alpha = "abcdefghijklmnopqrstuvwxyz";

    while (!queue.isEmpty() && !found) {
        int size = queue.size();

        /**
         * CRITICAL: Use levelVisited to allow multiple parents at same distance
         *
         * Why separate from main visited set?
         * - Allows a word to be reached from multiple neighbors in same level
         * - We record ALL parents that reach it in shortest distance
         * - Main visited updated AFTER processing entire level
         *
         * Without this, we'd lose valid shortest paths!
         */
        Set<String> levelVisited = new HashSet<>();

        for (int i = 0; i < size; i++) {
            String word = queue.poll();
            char[] chars = word.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char original = chars[j];

                for (char c : alpha.toCharArray()) {
                    if (c == original)
                        continue;

                    chars[j] = c;
                    String nextWord = new String(chars);

                    // Skip words not in dictionary
                    if (!wordSet.contains(nextWord))
                        continue;

                    int newDistance = distances.get(word) + 1;

                    /**
                     * KEY LOGIC: Record ALL predecessors at shortest distance
                     *
                     * Case 1: First time reaching nextWord
                     * - Set distance
                     * - Add current word as first predecessor
                     * - Enqueue for next level
                     *
                     * Case 2: Reaching nextWord again at SAME distance (same level)
                     * - Add current word as ANOTHER predecessor
                     * - Don't enqueue again (already enqueued in this level)
                     *
                     * Case 3: Reaching nextWord at LONGER distance
                     * - Ignore (we only want shortest paths)
                     */
                    if (!distances.containsKey(nextWord)) {
                        // Case 1: First time reaching this word
                        distances.put(nextWord, newDistance);
                        parents.computeIfAbsent(nextWord, k -> new ArrayList<>()).add(word);

                        if (!levelVisited.contains(nextWord)) {
                            levelVisited.add(nextWord);
                            queue.add(nextWord);
                        }

                        if (nextWord.equals(endWord)) {
                            found = true;
                        }
                    } else if (distances.get(nextWord) == newDistance) {
                        // Case 2: Same distance from another parent
                        parents.computeIfAbsent(nextWord, k -> new ArrayList<>()).add(word);
                    }
                    // Case 3: Longer distance - ignore
                }

                chars[j] = original;  // Restore after exploring all letters
            }
        }
    }

    // ========== PHASE 2: DFS to enumerate all paths ==========
    if (distances.containsKey(endWord)) {
        List<String> path = new LinkedList<>();
        dfsEnumeratePaths(endWord, beginWord, parents, path, result);
    }

    return result;
}

/**
 * DFS backtracking to reconstruct all paths from endWord to beginWord
 *
 * Why backward (from endWord to beginWord)?
 * - parents map stores: word → predecessors
 * - Easier to traverse backward from target to source
 * - Build path in reverse, then it's already correct order when we reach beginWord
 */
private void dfsEnumeratePaths(String current, String beginWord,
                               Map<String, List<String>> parents,
                               List<String> path, List<List<String>> result) {
    // Add current word to path (building backward)
    path.add(0, current);

    // Base case: reached the beginning
    if (current.equals(beginWord)) {
        result.add(new ArrayList<>(path));
    } else {
        // Recursive case: explore all predecessors
        List<String> predecessors = parents.get(current);
        if (predecessors != null) {
            for (String prev : predecessors) {
                dfsEnumeratePaths(prev, beginWord, parents, path, result);
            }
        }
    }

    // Backtrack: remove current word before returning
    path.remove(0);
}
```

**Concrete Example: LC 126 - Word Ladder II**

```text
Problem: Find ALL shortest paths from "hit" to "cog"
Dictionary: ["hot","dot","dog","lot","log","cog"]
Expected: [["hit","hot","dot","dog","cog"], ["hit","hot","lot","log","cog"]]

========== BFS PHASE ==========

Level 0: Queue = [hit], distances = {hit:0}
  Process "hit":
    Neighbors: "hot" (only one in dict differing by 1 letter)
    distances[hot] = 1, parents[hot] = [hit]
    levelVisited = {hot}
  After level: visited = {hit, hot}

Level 1: Queue = [hot], distances = {hit:0, hot:1}
  Process "hot":
    Neighbors: "dot", "lot", "hit" (hit already visited at distance 0, skip)
    distances[dot] = 2, parents[dot] = [hot]
    distances[lot] = 2, parents[lot] = [hot]
    levelVisited = {dot, lot}
  After level: visited = {hit, hot, dot, lot}

Level 2: Queue = [dot, lot], distances = {hit:0, hot:1, dot:2, lot:2}
  Process "dot":
    Neighbors: "dog", "hot" (hot at distance 1, skip)
    distances[dog] = 3, parents[dog] = [dot]
  Process "lot":
    Neighbors: "log", "hot" (hot at distance 1, skip)
    distances[log] = 3, parents[log] = [lot]
    levelVisited = {dog, log}
  After level: visited = {hit, hot, dot, lot, dog, log}

Level 3: Queue = [dog, log], distances = {hit:0, hot:1, dot:2, lot:2, dog:3, log:3}
  Process "dog":
    Neighbors: "cog", "dot" (dot at distance 2, skip)
    distances[cog] = 4, parents[cog] = [dog]
    found = true
  Process "log":
    Neighbors: "cog", "lot" (lot at distance 2, skip)
    cog already has distance 4, same as current+1!
    parents[cog] = [dog, log]  ← KEY: multiple parents!
  After level: visited = {hit, hot, dot, lot, dog, log, cog}

STOP BFS (found = true after finishing level)

Final parents map:
  cog → [dog, log]
  dog → [dot]
  log → [lot]
  dot → [hot]
  lot → [hot]
  hot → [hit]

========== DFS PHASE ==========

DFS from "cog" to "hit":

dfs(cog):
  path = [cog]
  predecessors = [dog, log]

  dfs(dog):
    path = [dog, cog]
    predecessors = [dot]

    dfs(dot):
      path = [dot, dog, cog]
      predecessors = [hot]

      dfs(hot):
        path = [hot, dot, dog, cog]
        predecessors = [hit]

        dfs(hit):
          path = [hit, hot, dot, dog, cog]
          hit == beginWord → FOUND PATH!
          result = [[hit, hot, dot, dog, cog]]

  dfs(log):
    path = [log, cog]
    predecessors = [lot]

    dfs(lot):
      path = [lot, log, cog]
      predecessors = [hot]

      dfs(hot):
        path = [hot, lot, log, cog]
        predecessors = [hit]

        dfs(hit):
          path = [hit, hot, lot, log, cog]
          hit == beginWord → FOUND PATH!
          result = [[hit, hot, dot, dog, cog], [hit, hot, lot, log, cog]]

Final result: 2 paths found ✓
```

**Why This Pattern Works:**

1. **BFS Phase - Build the Graph**:
   - Level-order traversal ensures first reach = shortest distance
   - `Map<String, List<String>> parents` records ALL predecessors at shortest distance
   - `Set<String> levelVisited` allows multiple parents from same level
   - Stop after finding endWord (ensures only shortest paths in graph)

2. **DFS Phase - Enumerate Paths**:
   - Walk backward from endWord to beginWord
   - At each node, recursively explore all predecessors
   - This generates ALL valid combinations of shortest paths
   - Backtrack to explore alternative paths

3. **Avoiding Duplicates & TLE**:
   - BFS only records shortest distances
   - DFS only traverses the shortest-path DAG
   - No redundant paths or longer paths explored
   - Graph structure is minimal

**Critical Implementation Details:**

| Detail | Why Important | What Happens Without |
|--------|---|---|
| **`levelVisited` separate from `visited`** | Allows multiple parents in same level | Lose valid shortest paths |
| **Update `visited` after level** | Records all same-level predecessors | Incorrectly skip valid parents |
| **Stop BFS after finding endWord** | Prevents longer paths from being recorded | Include suboptimal paths |
| **Use Map for predecessors** | Records all predecessors (not just one) | Find only some paths, not all |
| **DFS backward traversal** | Can follow multiple predecessor chains | Can't enumerate all combinations |

**Pattern Characteristics:**

- **Two-Phase Algorithm**: BFS phase, then DFS phase (sequential, not simultaneous)
- **Graph Construction**: Build a reverse DAG of predecessors during BFS
- **Path Enumeration**: Use DFS with backtracking to traverse all paths in the DAG
- **Distance Tracking**: Essential for determining shortest distance and stopping BFS
- **Multiple Parents**: A node can have multiple predecessors at the same distance

**When to Use This Pattern:**

- ✅ Find ALL shortest paths (not just one)
- ✅ Multiple valid paths of same minimum length exist
- ✅ Need to enumerate all combinations
- ✅ Must avoid exploring longer paths (TLE prevention)
- ✅ Word transformation, graph traversal problems

**When NOT to Use:**

- ❌ Only need one shortest path (use Pattern 7 or simpler BFS)
- ❌ Unique shortest path guaranteed (unnecessary complexity)
- ❌ Need to find longest paths or all paths (use DFS alone)

**Key Variations:**

1. **Distance Map Variant**: Store distances explicitly (see V0-3 in code)
2. **Early Termination**: Stop BFS immediately upon reaching endWord (current approach)
3. **Bidirectional BFS**: Expand from both ends to reduce search space
4. **Neighbor Precomputation**: Pre-compute all valid neighbors to avoid regenerating (optimization)

**Similar Problems:**

- **LC 126: Word Ladder II** (find all shortest word transformation sequences)
- **LC 913: Cat and Mouse** (find all game strategies in shortest time)
- **LC 1585: Check If String Is Transformable With Substring Sort Operations** (enumerate transformations)
- **LC 1948: Delete the Middle Node of a Linked List** (not similar, but similar pattern in graph problems)
- **LC 2115: Find All Recipes from Given Supplies** (topological sort variant, similar enumeration pattern)

**Comparison with Pattern 7 (BFS + Backtracking):**

| Aspect | Pattern 7 (BFS + Backtracking) | Pattern 8.5 (BFS + DFS) |
|--------|---|---|
| **Goal** | Find ONE shortest path | Find ALL shortest paths |
| **Graph Building** | On-the-fly neighbor generation | Explicit parent map construction |
| **Visited Tracking** | Standard visited set | levelVisited + visited (2-tier) |
| **Enumeration** | Early exit on found | DFS backtracks through all paths |
| **Memory** | O(M) for char array | O(N*M) for full parent graph |
| **Example** | LC 127 | LC 126 |

---

### Pattern 9: BFS-Style Cartesian Product Generation (Level-by-Level Combination Building) — LC 1087

**Core idea:** Use a queue of partial strings (prefixes). Each independent "group" of options maps to one BFS depth level. For every level, drain the current queue and expand every prefix with every option in that group — producing the full Cartesian product one layer at a time.

This is **not** BFS over a graph with visited-node tracking. It is the BFS traversal structure applied to combination enumeration: process all nodes at depth `k`, generate all nodes at depth `k+1`, repeat.

#### When to Use

| Signal | Reason |
|--------|--------|
| Output must enumerate **all combinations** from independent choice groups | Cartesian product = one choice per group |
| Groups are **independent** (no constraint between them) | No pruning needed; every combination is valid |
| Want **lexicographic order** | Sort each group before BFS; row-major queue output is already sorted |
| Prefer **iterative** over recursive | BFS loop replaces DFS/backtracking recursion |

**Why NOT DFS/backtracking?** Both work, but BFS avoids recursion depth limits and naturally produces combinations in group-order. Backtracking is better when choices within groups have cross-constraints (e.g., no duplicate characters in path).

#### How the Queue Evolves (Cartesian Product Visualization)

```text
Input: s = "{a,b}c{d,e}f"
Parsed groups: [["a","b"], ["c"], ["d","e"], ["f"]]

Start:
  queue = [""]

After group ["a","b"]  (level 1):
  Drain "" → append "a", "b"
  queue = ["a", "b"]

After group ["c"]      (level 2):
  Drain "a" → "ac"
  Drain "b" → "bc"
  queue = ["ac", "bc"]

After group ["d","e"]  (level 3):
  Drain "ac" → "acd", "ace"
  Drain "bc" → "bcd", "bce"
  queue = ["acd", "ace", "bcd", "bce"]

After group ["f"]      (level 4):
  queue = ["acdf", "acef", "bcdf", "bcef"]   ← final result
```

Each level multiplies the queue size by the group's option count.  
Total combinations = `|group_0| × |group_1| × ... × |group_k|` (the Cartesian product size).

#### Template (Java)

```java
// Pattern 9: BFS-Style Cartesian Product Generation
// Time: O(G * |result|) where G = number of groups, |result| = total combinations
// Space: O(|result|) for the queue at the final level
public String[] cartesianBFS(List<List<String>> groups) {
    Queue<String> queue = new LinkedList<>();
    queue.add("");  // seed: one empty prefix at depth 0

    for (List<String> group : groups) {
        int size = queue.size();  // snapshot current layer size
        for (int k = 0; k < size; k++) {
            String prefix = queue.poll();
            for (String option : group) {
                queue.add(prefix + option);  // expand: prefix × option
            }
        }
        // After the loop: queue holds exactly one layer deeper
    }

    String[] res = new String[queue.size()];
    int idx = 0;
    while (!queue.isEmpty()) res[idx++] = queue.poll();
    return res;
}
```

**Key invariant:** after processing group `i`, every string in the queue has length `i + 1` (one char per group so far). The queue holds exactly the complete Cartesian product of groups `[0..i]`.

#### Variant: Explicit State Object (more canonical BFS)

```java
// Use State(prefix, groupIndex) so the BFS loop drives termination
Queue<State> queue = new LinkedList<>();
queue.add(new State("", 0));

while (!queue.isEmpty()) {
    State cur = queue.poll();
    if (cur.groupIndex == groups.size()) {
        result.add(cur.prefix);  // leaf: complete combination
        continue;
    }
    for (String opt : groups.get(cur.groupIndex))
        queue.add(new State(cur.prefix + opt, cur.groupIndex + 1));
}
```

Both variants are correct; the snapshot-size version is more concise; the State version makes the "BFS tree" structure explicit.

#### Comparison: BFS vs Backtracking for Cartesian Products

| Aspect | BFS (Pattern 9) | Backtracking / DFS |
|--------|-----------------|---------------------|
| **Control flow** | Iterative loop, one group per iteration | Recursive, one group per call frame |
| **Ordering** | Natural row-major order if groups pre-sorted | Same if groups pre-sorted |
| **Memory peak** | Full final layer (all combinations) | O(depth) recursion stack |
| **Pruning** | Not straightforward | Easy to add |
| **Constraint between groups?** | Hard to express | Easy (check at each step) |
| **Best for** | Enumerate all, no cross-group constraints | Constrained search (e.g., sum ≤ target) |

#### Similar Problems

| Problem | LC # | How Cartesian BFS Applies |
|---------|------|---------------------------|
| Brace Expansion | 1087 | Each `{a,b}` or single char = one group |
| Letter Combinations of a Phone Number | 17 | Each digit maps to a letter group |
| Letter Case Permutation | 784 | Each char has 1 (digit) or 2 (letter) options |
| Word Squares | 425 | Each position in the word is a group |
| Generalized Abbreviation | 320 | Each char = keep or abbreviate (2-option group) |

> **Rule of thumb**: if you can parse the input into `k` independent groups and need **all** length-`k` strings formed by picking one element from each group, use BFS-style Cartesian product generation. If groups have cross-constraints, switch to backtracking.

---

### Pattern 12: BFS over String States — Stop at the First Fruitful Level — LC 301 ⭐⭐⭐⭐⭐

**Key Idea**: when the question is *"remove the **minimum** number of X"*, make **one removal = one BFS level**. Level `k` holds every string reachable by exactly `k` deletions. The **first level that contains any valid string** is the answer level — collect all valid strings on it and return immediately. No counting, no backtracking, no "how many to remove" pre-pass.

**Why BFS beats DFS here**: DFS finds *some* valid string but you'd still have to prove minimality; BFS gets minimality for free from the level number, and returns **all** answers of that length in one shot.

**Two rules that keep it from exploding**:
1. **Dedupe with a `visited` set** — `"(())"` is reachable by many different deletion orders.
2. **Stop expanding as soon as one valid string is found in the level** — still finish scanning the rest of that level (there may be several answers), but never build level `k+1`.

```java
// java
// LC 301 - Remove Invalid Parentheses
// time = O(2^n * n)  worst case every subset of chars; n per validity check
// space = O(2^n)     visited set + queue
// IDEA: 1 BFS level = 1 deletion. First level containing a valid string is the answer level.
public List<String> removeInvalidParentheses(String s) {
    List<String> res = new ArrayList<>();
    Set<String> visited = new HashSet<>();
    Queue<String> q = new LinkedList<>();
    q.offer(s);
    visited.add(s);
    boolean found = false;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String cur = q.poll();
            if (isValid(cur)) { res.add(cur); found = true; }
            if (found) continue;               // drain this level, but stop expanding
            for (int j = 0; j < cur.length(); j++) {
                char c = cur.charAt(j);
                if (c != '(' && c != ')') continue;   // only parens are removable
                String next = cur.substring(0, j) + cur.substring(j + 1);
                if (visited.add(next)) q.offer(next); // add() returns false if dup
            }
        }
        if (found) return res;                 // this level is minimal -> done
    }
    return res;
}

private boolean isValid(String t) {
    int cnt = 0;
    for (char c : t.toCharArray()) {
        if (c == '(') cnt++;
        else if (c == ')' && --cnt < 0) return false;  // ')' before its '('
    }
    return cnt == 0;
}
```

```python
# python
# LC 301 - Remove Invalid Parentheses
# time = O(2^n * n), space = O(2^n)
# IDEA: level = number of deletions; return the first level that has valid strings
def removeInvalidParentheses(s):
    def valid(t):
        cnt = 0
        for ch in t:
            if ch == '(':
                cnt += 1
            elif ch == ')':
                cnt -= 1
                if cnt < 0:
                    return False
        return cnt == 0

    level = {s}                       # a set IS the visited-dedup for this level
    while level:
        found = [t for t in level if valid(t)]
        if found:
            return found              # minimal deletions -> all answers of this size
        nxt = set()
        for t in level:
            for i, ch in enumerate(t):
                if ch in '()':        # letters are never removed
                    nxt.add(t[:i] + t[i + 1:])
        level = nxt
    return [""]
```

**Recognize this pattern when**: "minimum number of removals/edits/changes to make X valid", answer must list **all** optimal results, and the state is small enough to hash (a string).

---

## Tree → Undirected Graph BFS

### Pattern 10: Tree → Undirected Graph + Per-Leaf Bounded BFS — LC 1530

**a. Core idea**

A tree only lets you walk *down* (parent → child). But the shortest path between two **leaf** nodes goes **up** to their lowest common ancestor and then **down** again — you need to traverse edges in *both* directions. So convert the tree into an **undirected graph** (add both `parent→child` and `child→parent` edges), then the leaf-to-leaf shortest path becomes a plain graph distance you can measure with BFS.

For LC 1530 (count pairs of leaves whose shortest path ≤ `distance`):
1. **One DFS/traversal** to (a) collect all leaf nodes and (b) build the undirected adjacency map.
2. **Run a bounded BFS from each leaf**, expanding only while `dist < distance`. Every *other* leaf reached is a good pair.
3. Each pair `A–B` is discovered twice (once from `A`, once from `B`) → **divide the final count by 2**.

**b. Pattern**

```python
# python — Tree → Graph conversion + per-leaf bounded BFS (LC 1530)
# time  = O(L * (V + E)) = O(L * N)   L = #leaves, N = #nodes
# space = O(N)                        adjacency map + queue/visited
from collections import deque, defaultdict

class Solution:
    def countPairs(self, root, distance):
        leaves = []
        graph = defaultdict(list)

        # Step 1: collect leaves + build UNDIRECTED graph
        def build(node, parent=None):
            if not node:
                return
            if not node.left and not node.right:   # leaf
                leaves.append(node)
            if parent:                              # bidirectional edge
                graph[node].append(parent)
                graph[parent].append(node)
            build(node.left, node)
            build(node.right, node)
        build(root)

        cnt = 0
        # Step 2: bounded BFS from every leaf
        for leaf in leaves:
            queue = deque([(leaf, 0)])              # (node, dist)
            visited = {leaf}
            while queue:
                cur, d = queue.popleft()
                if cur != leaf and not cur.left and not cur.right:
                    cnt += 1                         # reached another leaf
                if d < distance:                     # only expand within limit
                    for nxt in graph[cur]:
                        if nxt not in visited:
                            visited.add(nxt)
                            queue.append((nxt, d + 1))
        return cnt // 2                              # each pair counted twice
```

**Recognition signals**
- Problem talks about the **distance / shortest path between leaf (or arbitrary) nodes** of a tree.
- Path must go **up and then down** → downward-only tree recursion is insufficient.
- Small constraints (`distance ≤ 10`, `N ≤ 2^10`) make the bounded-BFS-per-leaf cost acceptable.

> **Alternative (often preferred):** a single **post-order DFS** that returns a bucket array of leaf-distances and combines left/right subtrees at each node — O(N) with no graph. See **DFS Pattern 15**. Use BFS when the "convert-to-graph, then measure distance" mental model is clearer or when non-tree edges exist.

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| Number of Good Leaf Nodes Pairs | 1530 | canonical tree→graph + per-leaf bounded BFS |
| All Nodes Distance K in Binary Tree | 863 | tree→graph, then BFS `k` steps from a target node — see **Pattern 11** (cheaper: parent map only) |
| Amount of Time for Binary Tree to Be Infected | 2385 | tree→graph, BFS "infection spread" = max distance |
| Step-By-Step Directions From a Binary Tree Node | 2096 | shortest node-to-node path via LCA (up-then-down) |
| Closest Leaf in a Binary Tree | 742 | tree→graph, multi-source/target BFS to nearest leaf |

---

## Weighted-Edge and Bidirectional BFS

### Pattern 14: BFS Carrying an Accumulated Value Along the Path — LC 399 ⭐⭐⭐⭐

**Key Idea**: the queue entry is `(node, valueSoFar)` instead of `(node, distance)`. Every edge carries a weight and you **combine** it (multiply here, could be add/min/max) as you expand. BFS is still valid because the question is *"is there **a** path, and what does it evaluate to"* — not *"the cheapest path"*. In `a/b = 2` the graph is `a --2--> b` and `b --1/2--> a`, so any path from `x` to `y` gives the same product and the first one BFS finds is fine.

**Guard rails**: return `-1.0` if either endpoint was never seen in the equations (an unknown variable, *not* a disconnected one), and `1.0` for `x/x` **only when `x` is known**.

```java
// java
// LC 399 - Evaluate Division
// time = O(Q * (V + E)), space = O(V + E)     Q = #queries
// IDEA: weighted graph a->b = v, b->a = 1/v; BFS carries the running product
public double[] calcEquation(List<List<String>> equations, double[] values,
                             List<List<String>> queries) {
    Map<String, Map<String, Double>> g = new HashMap<>();
    for (int i = 0; i < values.length; i++) {
        String a = equations.get(i).get(0), b = equations.get(i).get(1);
        g.computeIfAbsent(a, k -> new HashMap<>()).put(b, values[i]);
        g.computeIfAbsent(b, k -> new HashMap<>()).put(a, 1.0 / values[i]);
    }
    double[] res = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++)
        res[i] = bfs(g, queries.get(i).get(0), queries.get(i).get(1));
    return res;
}

private double bfs(Map<String, Map<String, Double>> g, String src, String dst) {
    if (!g.containsKey(src) || !g.containsKey(dst)) return -1.0;  // unknown variable
    if (src.equals(dst)) return 1.0;
    Queue<Object[]> q = new LinkedList<>();
    Set<String> seen = new HashSet<>();
    q.offer(new Object[]{src, 1.0});
    seen.add(src);
    while (!q.isEmpty()) {
        Object[] cur = q.poll();
        String node = (String) cur[0];
        double prod = (double) cur[1];
        for (Map.Entry<String, Double> e : g.get(node).entrySet()) {
            if (e.getKey().equals(dst)) return prod * e.getValue();
            if (seen.add(e.getKey()))
                q.offer(new Object[]{e.getKey(), prod * e.getValue()});
        }
    }
    return -1.0;   // known variables, but no path connects them
}
```

```python
# python
# LC 399 - Evaluate Division
# time = O(Q * (V + E)), space = O(V + E)
# IDEA: queue holds (node, product_so_far) instead of (node, distance)
from collections import deque, defaultdict

def calcEquation(equations, values, queries):
    g = defaultdict(dict)
    for (a, b), v in zip(equations, values):
        g[a][b] = v
        g[b][a] = 1.0 / v

    def bfs(src, dst):
        if src not in g or dst not in g:
            return -1.0                     # variable never appeared
        if src == dst:
            return 1.0
        q = deque([(src, 1.0)])
        seen = {src}
        while q:
            node, prod = q.popleft()
            for nxt, w in g[node].items():
                if nxt == dst:
                    return prod * w
                if nxt not in seen:
                    seen.add(nxt)
                    q.append((nxt, prod * w))
        return -1.0

    return [bfs(a, b) for a, b in queries]
```

**Generalizes to**: any "propagate a value along edges" question — swap the `*` for `+` (accumulate cost), `min`/`max` (bottleneck path), or a boolean (reachability). The queue payload is the only thing that changes.

---

### Pattern 15: 0-1 BFS with a Deque — LC 1368 ⭐⭐⭐⭐

**Key Idea**: when every edge costs **0 or 1**, you don't need Dijkstra's heap. Use a **deque**:
- cost-0 edge → `addFirst` (same "layer", process before anything costlier)
- cost-1 edge → `addLast` (next layer)

The deque stays sorted by distance with at most two distinct values in it, so the first pop of a node is its final distance — Dijkstra's guarantee at **O(V + E)** instead of `O(E log V)`.

**LC 1368**: the grid tells you the "free" direction of each cell. Following the arrow costs `0`; any other of the 4 moves costs `1` (one sign change).

```java
// java
// LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
// time = O(m*n), space = O(m*n)
// IDEA: 0-1 BFS. Following grid[r][c]'s arrow costs 0 -> push FRONT; turning costs 1 -> push BACK.
public int minCost(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};    // index k <-> grid value k+1
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    dist[0][0] = 0;
    Deque<int[]> dq = new ArrayDeque<>();
    dq.offerFirst(new int[]{0, 0});
    while (!dq.isEmpty()) {
        int[] cur = dq.pollFirst();
        int r = cur[0], c = cur[1];
        for (int k = 0; k < 4; k++) {
            int nr = r + dirs[k][0], nc = c + dirs[k][1];
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
            int cost = (grid[r][c] == k + 1) ? 0 : 1;
            if (dist[r][c] + cost < dist[nr][nc]) {
                dist[nr][nc] = dist[r][c] + cost;
                if (cost == 0) dq.offerFirst(new int[]{nr, nc});   // 0-weight: front
                else           dq.offerLast(new int[]{nr, nc});    // 1-weight: back
            }
        }
    }
    return dist[m - 1][n - 1];
}
```

```python
# python
# LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
# time = O(m*n), space = O(m*n)
# IDEA: deque BFS - appendleft for 0-cost moves, append for 1-cost moves
def minCost(grid):
    m, n = len(grid), len(grid[0])
    dirs = [(0, 1), (0, -1), (1, 0), (-1, 0)]      # grid value 1,2,3,4
    INF = float('inf')
    dist = [[INF] * n for _ in range(m)]
    dist[0][0] = 0
    dq = deque([(0, 0)])
    while dq:
        r, c = dq.popleft()
        for k, (dr, dc) in enumerate(dirs):
            nr, nc = r + dr, c + dc
            if not (0 <= nr < m and 0 <= nc < n):
                continue
            cost = 0 if grid[r][c] == k + 1 else 1
            if dist[r][c] + cost < dist[nr][nc]:
                dist[nr][nc] = dist[r][c] + cost
                if cost == 0:
                    dq.appendleft((nr, nc))        # free move -> front
                else:
                    dq.append((nr, nc))            # paid move -> back
    return dist[m - 1][n - 1]
```

**BFS vs 0-1 BFS vs Dijkstra**

| Edge weights | Structure | Push rule | Time |
|---|---|---|---|
| all 1 | Queue | always back | O(V + E) |
| 0 or 1 | **Deque** | 0 → front, 1 → back | O(V + E) |
| arbitrary ≥ 0 | PriorityQueue | by distance | O(E log V) |

**Similar 0-1 BFS problems**: LC 1263 Minimum Moves to Move a Box to Their Target Location (pushing the box costs 1, walking the player around costs 0 — state is `(box, player)`), and any "minimum obstacles to remove / minimum sign flips" grid question. Compare with LC 1730 (already in this doc) where every move costs 1 → plain queue is enough.

---

### Bidirectional BFS
```python
def bidirectional_bfs(start, end):
    """Meet in the middle - faster for long paths"""
    if start == end:
        return 0
    
    forward = {start: 0}
    backward = {end: 0}
    queue_forward = deque([start])
    queue_backward = deque([end])
    
    while queue_forward or queue_backward:
        # Expand smaller frontier
        if len(forward) <= len(backward):
            if expand_level(queue_forward, forward, backward):
                return True
        else:
            if expand_level(queue_backward, backward, forward):
                return True
    
    return False
```

### BFS with Priority (Dijkstra-like)
```python
import heapq

def weighted_bfs(start, end, graph):
    """BFS variant for weighted graphs"""
    heap = [(0, start)]
    distances = {start: 0}
    
    while heap:
        dist, node = heapq.heappop(heap)
        
        if node == end:
            return dist
            
        if dist > distances.get(node, float('inf')):
            continue
            
        for neighbor, weight in graph[node]:
            new_dist = dist + weight
            if new_dist < distances.get(neighbor, float('inf')):
                distances[neighbor] = new_dist
                heapq.heappush(heap, (new_dist, neighbor))
    
    return -1
```

## Repeated & Sequential BFS

### Pattern 6: Sort + Repeated BFS (Sequential Shortest Paths) — LC 675
```java
/**
 * Pattern: Sort targets by priority, then repeatedly call BFS to find shortest paths
 * Use case: Visit multiple targets in specific order, minimize total travel distance
 * Key insight: BFS guarantees shortest path between each pair of consecutive targets
 *
 * Time: O(k × m × n) where k = number of targets, m×n = grid size
 * Space: O(m × n) for visited array in each BFS call
 */
public int sortAndBFS(List<List<Integer>> grid) {
    int rows = grid.size();
    int cols = grid.get(0).size();

    // Step 1: Collect all targets and sort by priority (e.g., value)
    List<int[]> targets = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid.get(r).get(c) > 1) {
                // Store [value, row, col]
                targets.add(new int[]{grid.get(r).get(c), r, c});
            }
        }
    }

    // Sort by value (ascending) - defines visit order
    targets.sort(Comparator.comparingInt(a -> a[0]));

    // Step 2: Sequentially visit each target using BFS
    int totalSteps = 0;
    int startR = 0, startC = 0; // Starting position

    for (int[] target : targets) {
        int targetR = target[1];
        int targetC = target[2];

        // Find shortest path from current position to next target
        int steps = bfs(grid, startR, startC, targetR, targetC);

        if (steps == -1) {
            return -1; // Target unreachable
        }

        totalSteps += steps;

        // Update starting position for next iteration
        startR = targetR;
        startC = targetC;
    }

    return totalSteps;
}

/**
 * Standard BFS to find shortest path in grid
 * Returns minimum steps from (sr, sc) to (tr, tc), or -1 if unreachable
 */
private int bfs(List<List<Integer>> grid, int sr, int sc, int tr, int tc) {
    if (sr == tr && sc == tc) return 0;

    int rows = grid.size();
    int cols = grid.get(0).size();

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{sr, sc});

    boolean[][] visited = new boolean[rows][cols];
    visited[sr][sc] = true;

    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int steps = 0;

    while (!queue.isEmpty()) {
        int size = queue.size();
        steps++;

        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check bounds and obstacles
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols
                    || visited[nr][nc] || grid.get(nr).get(nc) == 0) {
                    continue;
                }

                // Found target
                if (nr == tr && nc == tc) {
                    return steps;
                }

                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }

    return -1; // Unreachable
}
```

**Concrete Example: LC 675 - Cut Off Trees for Golf Event**
```text
Problem: Cut trees in forest from shortest to tallest, return minimum steps
Grid: [[1,2,3],    Trees: (0,1)=2, (0,2)=3, (1,2)=4, (2,0)=7, (2,1)=6, (2,2)=5
       [0,0,4],    Sorted: 2→3→4→5→6→7
       [7,6,5]]

Path: (0,0) →[1 step]→ (0,1) cut 2
      (0,1) →[2 steps]→ (0,2) cut 3
      (0,2) →[1 step]→ (1,2) cut 4
      (1,2) →[1 step]→ (2,2) cut 5
      (2,2) →[1 step]→ (2,1) cut 6
      (2,1) →[1 step]→ (2,0) cut 7
Total: 1+2+1+1+1+1 = 7 steps (Note: Problem statement has different expected output)

Key insight: Must cut in sorted order, BFS finds shortest path between each pair
```

**Pattern Characteristics:**
- **Sort Phase**: O(k log k) where k = number of targets
- **BFS Phase**: O(k) iterations, each BFS is O(m×n) for grid
- **Total Time**: O(k log k + k×m×n) ≈ O(k×m×n) when k << m×n
- **Space**: O(m×n) for visited array (created fresh each BFS)

**When to Use This Pattern:**
- Must visit targets in specific order (sorted by value, priority, etc.)
- Need shortest path between consecutive targets
- Targets are sparse in the space
- Cannot use dynamic programming due to order constraints

**Key Variations:**
1. **Different Sort Criteria**: Sort by distance, value, custom priority
2. **Modified Grid**: Update grid after visiting target (set to 1, remove obstacle)
3. **Early Termination**: Return immediately if any target unreachable
4. **Optimization**: Use A* instead of BFS for large grids

**Similar Problems:**
- LC 675: Cut Off Trees for Golf Event (sort trees by height)
- LC 1293: Shortest Path with Obstacles Elimination (BFS with state)
- LC 864: Shortest Path to Get All Keys (BFS with key collection state)
- LC 1091: Shortest Path in Binary Matrix (basic BFS shortest path)
- LC 317: Shortest Distance from All Buildings (multi-source BFS)

## Summary

| Signal in the problem | Variant | Section |
|---|---|---|
| "distance to the **nearest** source" vs "**sum** of distances to all sources" | one shared `visited` vs a fresh `visited` per source | Pattern 4.6 |
| Two components, connect them with the fewest flips | DFS to mark one component, multi-source BFS out from all of it | Pattern 4.5 |
| "minimum **transfers** / buses / lines" | BFS whose nodes are **routes**, not stops | Pattern 8 |
| "return **all** shortest sequences" | BFS builds a predecessor DAG, DFS enumerates it | Pattern 8.5 |
| "minimum removals to make it valid" | one BFS level = one removal; stop at the first fruitful level | Pattern 12 |
| Enumerate every combination from independent groups | one group per BFS level (Cartesian product) | Pattern 9 |
| Path must go **up** a tree as well as down | convert the tree to an undirected graph, then BFS | Pattern 10 |
| Edges carry a value to accumulate (product / min / max) | queue payload is `(node, valueSoFar)` | Pattern 14 |
| Every edge costs **0 or 1** | deque: cost-0 to the front, cost-1 to the back | Pattern 15 |
| Both endpoints are known and the path is long | expand the smaller frontier from each end | Bidirectional BFS |
| Edges carry **arbitrary** non-negative weights | stop using BFS — see [Dijkstra.md](./Dijkstra.md) | BFS with Priority |
| Visit targets in a **forced order** | sort, then one BFS per consecutive pair | Pattern 6 |

The must-know queue templates these build on are in [bfs.md](./bfs.md); the worked-solution archive is [bfs_examples.md](./bfs_examples.md).
