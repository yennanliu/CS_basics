# BFS (Breadth-First Search)

## Overview
Breadth-First Search is a graph traversal algorithm that explores nodes level by level, visiting all nodes at the current depth before moving to nodes at the next depth.

### Key Properties
- **Complete**: Always finds a solution if one exists
- **Optimal**: Finds shortest path in `unweighted` graphs
- **Space Complex**: O(b^d) where b=branching factor, d=depth
- **Time Complex**: O(V + E) for graphs, O(n) for trees

### Core Characteristics
- Uses **Queue** data structure (FIFO - First In, First Out)
- Guarantees **shortest path** in unweighted graphs
- Explores nodes **level by level** (breadth first, then depth)
- Memory intensive compared to DFS

## Fundamental Concepts

### 1. Node States (for cycle detection)
- **State 0**: Not visited (white)
- **State 1**: Currently being processed (gray) 
- **State 2**: Completely processed (black)

### 2. BFS vs DFS Comparison
| Aspect | BFS | DFS |
|--------|-----|-----|
| Data Structure | Queue | Stack |
| Memory | O(w) width | O(h) height |
| Shortest Path | ✅ Yes | ❌ No |
| Complete | ✅ Yes | ❌ No (infinite spaces) |

## Implementation Patterns

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

### Pattern 2: Level-by-Level BFS
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

### Pattern 3: Graph BFS with Visited Set
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

### Pattern 4: Multi-Source BFS (Distance Calculation)
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

**Alternative: Using MAX_VALUE for initialization (allows for optimization)**
```java
public int[][] multiSourceBFS_optimized(int[][] mat) {
    int rows = mat.length;
    int cols = mat[0].length;
    Queue<int[]> queue = new LinkedList<>();

    // Initialize with MAX_VALUE for non-sources
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (mat[r][c] == 0) {
                queue.offer(new int[]{r, c});
            } else {
                mat[r][c] = Integer.MAX_VALUE;  // Treat as infinity
            }
        }
    }

    int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

    while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        int r = cur[0], c = cur[1];

        for (int[] d : dirs) {
            /** NOTE !!!
             * 
             * - mat[r][c] + 1: 
             * This is the "new potential distance" you just calculated
             * by walking from your current cell (r, c) to its neighbor (nr, nc).
             *  
             * 
             * - mat[nr][nc]: 
             * This is the "best distance found so far" for that neighb
             * 
             */
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;

            /**
             * KEY OPTIMIZATION: Only update when new distance is shorter
             * Why this works:
             * - In unweighted BFS, first visit = shortest distance
             * - If mat[nr][nc] <= mat[r][c] + 1, cell already has better/equal path
             * - No need to re-process cells that won't improve
             *
             * This prevents redundant enqueuing and guarantees O(m×n) time
             */
            /** e.g.
             * 
             * "If the existing distance at the neighbor is worse than the one I just found, update the neighbor and put it in the queue."
             */
            if (mat[nr][nc] > mat[r][c] + 1) {
                mat[nr][nc] = mat[r][c] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }

    return mat;
}
```

**Concrete Example: LC 542 - 01 Matrix**
```
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

**Why This Pattern Works:**
1. **Simultaneous Expansion**: All sources expand at same rate → layer by layer
2. **First Visit = Shortest**: In unweighted BFS, first arrival guarantees shortest path
3. **No Backtracking**: Once a cell is visited, we've found its shortest distance
4. **Linear Time**: Each cell visited exactly once → O(m×n) total

**Key Insight - Why Start from 0s, Not 1s?**
- ❌ Starting from each 1 → O(m×n) BFS calls → O(m²×n²) total time
- ✅ Starting from all 0s → Single BFS pass → O(m×n) total time
- **Principle**: Flip the problem - instead of "how far is this 1 from any 0?", ask "how far can all 0s reach?"

### Pattern 4.5: DFS + Multi-Source BFS (Island Expansion)
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
```
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
```
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
```
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

### Pattern 5: BFS with Path Tracking
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

### Pattern 6: Sort + Repeated BFS (Sequential Shortest Paths)
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
```
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

### Pattern 7: BFS + Backtracking (State Space Exploration)
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

**Concrete Example: LC 127 - Word Ladder**

```
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

```
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

---

## Problem Categories

### 1. Tree Traversal Problems
- **Level Order Traversal**: LC 102, 107, 103
- **Binary Tree Paths**: LC 257, 1022
- **Right Side View**: LC 199
- **Vertical Order**: LC 314

### 2. Shortest Path Problems
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
- **State-Based BFS**: LC 864 (Shortest Path to Get All Keys), LC 1293 (Shortest Path with Obstacles Elimination)

### 3. Graph Structure Problems
- **Cycle Detection**: LC 207 (Course Schedule)
- **Connected Components**: LC 200 (Number of Islands)
- **Graph Validation**: LC 261 (Graph Valid Tree)
- **Clone Graph**: LC 133

### 4. Matrix/Grid Problems
- **Surrounded Regions**: LC 130
- **Walls and Gates**: LC 286
- **Maze Problems**: LC 490

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

## Common Mistakes & Tips

### ❌ Common Mistakes
1. Using `queue.pop()` instead of `queue.popleft()` with list
2. Not handling visited set in graphs (infinite loops)
3. Forgetting level-by-level processing when needed
4. Incorrect boundary checking in grid problems

### ✅ Best Practices
1. Use `collections.deque` for better performance
2. Always use visited set for graph problems
3. Check boundaries before adding to queue in grid problems
4. Consider multi-source BFS for optimization
5. Track level/distance when needed for shortest path

### When to Update Grid Status & Count (Mark Before vs After Enqueue)

A critical BFS implementation detail: **always mark a cell as visited (update grid status and counters) BEFORE adding it to the queue**, not when you dequeue it.

#### The Rule

```
Mark visited + update count → THEN add to queue
```

```java
// CORRECT: Mark BEFORE enqueue
if (grid[nr][nc] == 1) {
    grid[nr][nc] = 2;       // mark immediately
    freshOrange--;           // update count immediately
    q.add(new int[]{nr, nc});
}

// WRONG: Mark AFTER dequeue
int[] cur = q.poll();
grid[cur[0]][cur[1]] = 2;   // too late! duplicates already in queue
```

#### Why This Matters

If you defer marking until dequeue, **multiple neighbors can enqueue the same cell** before any of them processes it:

```
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

#### Concrete Example: LC 994 - Rotting Oranges

```java
// From RottingOranges.java - V0 solution
while (!q.isEmpty() && freshOrange > 0) {
    int size = q.size();
    time++;

    for (int i = 0; i < size; i++) {
        int[] cur = q.poll();
        int r = cur[0], c = cur[1];

        for (int[] m : moves) {
            int nr = r + m[0];
            int nc = c + m[1];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                // CRITICAL: Mark rotten and decrement count BEFORE enqueue
                grid[nr][nc] = 2;
                freshOrange--;
                q.add(new int[] { nr, nc });
            }
        }
    }
}
```

If we deferred `grid[nr][nc] = 2` until dequeue, two rotten neighbors processing in the same layer could both enqueue the same fresh orange, leading to `freshOrange` going negative and returning a wrong answer.

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

#### Summary

> In BFS, **the moment you decide a neighbor should enter the queue is the moment you commit** — mark it visited, update your counters, mutate the grid. Never defer state changes to dequeue time. This is not an optimization; it is a **correctness requirement**.

---

## Advanced Techniques

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

## Core Concepts Summary

### Multi-Source BFS Distance Calculation (LC 542 Pattern)

**The Problem Type:**
Calculate shortest distance from each cell to ANY source cell in a grid.

**Why Multi-Source BFS?**
```
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

## Quick Reference

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
- Weighted graphs (use Dijkstra instead)
- Need to explore all paths (use DFS)

### Key LeetCode Problems
| Difficulty | Problem | Key Concept | Core Pattern |
|------------|---------|-------------|--------------|
| Easy | LC 102 | Level-order traversal | Pattern 2 (Level-by-Level) |
| **Medium** | **LC 127** | **Shortest path transformation - Word Ladder** | **Pattern 7 (BFS + Backtracking)** |
| Medium | LC 200 | Connected components | Pattern 3 (Graph BFS) |
| **Medium** | **LC 542** | **Simultaneous multi-source - 01 Matrix** | **Pattern 4 (Simultaneous Multi-Source)** |
| Medium | LC 934 | DFS + Multi-source BFS (island expansion) | Pattern 4.5 (DFS + Multi-Source) |
| Medium | LC 1162 | As Far from Land as Possible | Pattern 4 (Simultaneous Multi-Source) |
| Hard | LC 286 | Walls and Gates | Pattern 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 317** | **Independent BFS runs (sum of distances)** | **Pattern 4.6 (Independent BFS Runs)** |
| Hard | LC 675 | Sort + Repeated BFS (sequential targets) | Pattern 6 (Sort + Repeated BFS) |
| **Hard** | **LC 752** | **BFS + Backtracking on state space - Open the Lock** | **Pattern 7 (BFS + Backtracking)** |
| Hard | LC 864 | BFS with state (key collection) | Pattern 3 + State |
| Hard | LC 1293 | BFS with state (obstacle elimination) | Pattern 3 + State |

## LC Examples

### 2-1) Rotting Oranges (LC 994) — Multi-source BFS
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

### 2-2) Word Ladder (LC 127) — BFS Shortest Transformation
> BFS on word graph; each edge connects words differing by one letter.

```java
// LC 127 - Word Ladder
// IDEA: BFS level by level on word transformations
// time = O(M^2 * N), space = O(M^2 * N)  M=word length, N=wordList size
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> wordSet = new HashSet<>(wordList);
    if (!wordSet.contains(endWord)) return 0;
    Queue<String> queue = new LinkedList<>();
    queue.offer(beginWord);
    int steps = 1;
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String word = queue.poll();
            char[] chars = word.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char orig = chars[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    chars[j] = c;
                    String next = new String(chars);
                    if (next.equals(endWord)) return steps + 1;
                    if (wordSet.remove(next)) queue.offer(next);
                }
                chars[j] = orig;
            }
        }
        steps++;
    }
    return 0;
}
```

### 2-3) Shortest Path in Binary Matrix (LC 1091) — BFS Shortest Path
> BFS from top-left to bottom-right through 0-cells (8-directional).

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

### 2-4) 01 Matrix (LC 542) — Multi-source BFS from All Zeros
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

### 2-5) Open the Lock (LC 752) — BFS on State Space
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

### 2-6) Surrounded Regions (LC 130) — BFS from Border
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

### 2-7) Course Schedule (LC 207) — BFS Topological Sort (Kahn's)
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

### 2-8) Walls and Gates (LC 286) — Multi-source BFS
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

### 2-9) Minimum Height Trees (LC 310) — BFS Leaf Trimming
> Repeatedly remove leaf nodes; the remaining 1-2 nodes are the roots of MHTs.

```java
// LC 310 - Minimum Height Trees
// IDEA: BFS — trim leaves layer by layer until 1 or 2 nodes remain
// time = O(N), space = O(N)
public List<Integer> findMinHeightTrees(int n, int[][] edges) {
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

### 2-10) Snakes and Ladders (LC 909) — BFS on Board
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

### 2-11) Pacific Atlantic Water Flow (LC 417) — BFS from Both Oceans
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