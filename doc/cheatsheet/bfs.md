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

### Pattern 8: BFS on Abstract Graph (Route-Level BFS)
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
```
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
```
visitedRoutes: Prevents boarding the same bus twice (infinite loop)
visitedStops:  Prevents re-processing transfer points
               (stop 7 connects Routes 0 and 1, but once explored, no need to revisit)

Without visitedStops: Every stop would re-check all its routes
  → Redundant work, potentially O(N²*M) instead of O(N*M)
```

**Why BFS on Routes, Not Stops?**
```
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

### Pattern 8: BFS + DFS (Find All Shortest Paths - DAG Enumeration)
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

```
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

| Aspect | Pattern 7 (BFS + Backtracking) | Pattern 8 (BFS + DFS) |
|--------|---|---|
| **Goal** | Find ONE shortest path | Find ALL shortest paths |
| **Graph Building** | On-the-fly neighbor generation | Explicit parent map construction |
| **Visited Tracking** | Standard visited set | levelVisited + visited (2-tier) |
| **Enumeration** | Early exit on found | DFS backtracks through all paths |
| **Memory** | O(M) for char array | O(N*M) for full parent graph |
| **Example** | LC 127 | LC 126 |

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
- **Route-Level BFS (Pattern 8)**: LC 815 (Bus Routes - minimum buses/transfers to reach target)
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

```
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

#### Concrete Example: LC 994 - Rotting Oranges

```
Grid: [[2,1,1],    Initial: 6 fresh oranges, 1 rotten at (0,0)
       [1,1,0],
       [0,1,1]]    Expected answer: 4 minutes
```

**Approach A Trace (time++ at beginning with `freshOrange > 0`):**

```
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

```
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

#### Common Patterns in LC 994 Solutions

| Version | Strategy | Key Code |
|---------|----------|----------|
| V0, V0-0-1 | time++ at beginning | `while (!q.isEmpty() && freshOrange > 0) { time++; ... }` |
| V0-0-2, V0-1, V0-4 | time++ at end with flag | `if (rottedThisMinute) time++;` |
| V1-1 | time++ at end (no flag) | `while (fresh > 0 && !q.isEmpty()) { ... } time++;` |

#### Summary

| Scenario | Recommended Approach |
|----------|---------------------|
| Have completion counter (fresh oranges, keys collected) | Approach A with counter in while condition |
| No completion counter | Approach B with boolean flag |
| Want simplest correct code | Approach B (harder to get wrong) |
| Want most efficient code | Approach A (no flag overhead) |

> **Rule of Thumb:** If you use `time++` at the BEGINNING, you MUST have an early-exit condition in the while loop. Otherwise, use `time++` at the END with a flag.

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

### When NOT to Use BFS
- Deep trees/graphs with limited memory
- Only need to find ANY path (not shortest)
- Weighted graphs with varying costs (use Dijkstra instead)
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
| **Hard** | **LC 126** | **Find ALL shortest paths - Word Ladder II** | **Pattern 8 (BFS + DFS DAG Enumeration)** |
| Hard | LC 286 | Walls and Gates | Pattern 4 (Simultaneous Multi-Source) |
| **Hard** | **LC 317** | **Independent BFS runs (sum of distances)** | **Pattern 4.6 (Independent BFS Runs)** |
| Hard | LC 675 | Sort + Repeated BFS (sequential targets) | Pattern 6 (Sort + Repeated BFS) |
| **Hard** | **LC 752** | **BFS + Backtracking on state space - Open the Lock** | **Pattern 7 (BFS + Backtracking)** |
| **Hard** | **LC 815** | **Route-level BFS (minimum buses)** | **Pattern 8 (Route-Level BFS)** |
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

### 2-3) Word Ladder II (LC 126) — BFS + DFS All Shortest Paths
> BFS builds a DAG of shortest-path predecessors; DFS enumerates all valid paths.

```java
// LC 126 - Word Ladder II
// IDEA: BFS to build predecessors map, then DFS to reconstruct all shortest paths
// time = O(N * M * 26 + output), space = O(N * M)
public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
    List<List<String>> result = new ArrayList<>();
    Set<String> wordSet = new HashSet<>(wordList);
    if (!wordSet.contains(endWord)) return result;

    // Map: word → list of predecessors (parents) at shortest distance
    Map<String, List<String>> parents = new HashMap<>();
    // BFS to build the parent graph
    Queue<String> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    queue.offer(beginWord);
    visited.add(beginWord);

    boolean found = false;
    while (!queue.isEmpty() && !found) {
        int size = queue.size();
        Set<String> levelVisited = new HashSet<>();  // Critical: track nodes at this level

        for (int i = 0; i < size; i++) {
            String word = queue.poll();
            char[] chars = word.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char orig = chars[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == orig) continue;
                    chars[j] = c;
                    String next = new String(chars);

                    if (!wordSet.contains(next)) continue;

                    if (!visited.contains(next)) {
                        parents.computeIfAbsent(next, k -> new ArrayList<>()).add(word);
                        levelVisited.add(next);
                        if (next.equals(endWord)) found = true;
                    }
                }
                chars[j] = orig;
            }
        }

        // Update visited after entire level (allows multiple parents from same level)
        visited.addAll(levelVisited);
        for (String node : levelVisited) {
            queue.offer(node);
        }
    }

    // DFS to enumerate all paths
    if (found) {
        List<String> path = new LinkedList<>();
        dfsEnumerate(endWord, beginWord, parents, path, result);
    }

    return result;
}

private void dfsEnumerate(String word, String beginWord, Map<String, List<String>> parents,
                          List<String> path, List<List<String>> result) {
    path.add(0, word);
    if (word.equals(beginWord)) {
        result.add(new ArrayList<>(path));
    } else if (parents.containsKey(word)) {
        for (String prev : parents.get(word)) {
            dfsEnumerate(prev, beginWord, parents, path, result);
        }
    }
    path.remove(0);
}
```

**Key Differences from LC 127:**
- **LC 127 (Pattern 7)**: BFS + Backtracking → Find ONE shortest path, early exit
- **LC 126 (Pattern 8)**: BFS + DFS → Find ALL shortest paths, use parent map, DFS enumeration
- **Critical Detail**: `levelVisited` allows multiple parents from same BFS level (essential for finding all paths)

---

### 2-4) Shortest Path in Binary Matrix (LC 1091) — BFS Shortest Path
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

**Core Idea — BFS / Layer Trimming (Onion Peeling):**
- Think of the tree like an **onion**. The MHT roots are in the innermost layer
- This is **multi-source BFS from leaves inward** — NOT BFS from a single root
- Leaves = nodes with degree 1. Remove all leaves simultaneously → their neighbors may become new leaves
- Repeat until ≤ 2 nodes remain. These are the **centroids** (MHT roots)
- Why ≤ 2? A tree has at most 2 centroids (diameter even → 2, diameter odd → 1)

```
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

### 2-11) Bus Routes (LC 815) — Route-Level BFS
> Map stops to routes; BFS on route IDs counts minimum buses to reach target.

```java
// LC 815 - Bus Routes
// IDEA: BFS on bus route IDs — each layer = one bus ride
// time = O(N*M), space = O(N*M)  N=routes, M=avg stops per route
public int numBusesToDestination(int[][] routes, int source, int target) {
    if (source == target) return 0;
    // stop -> list of route IDs
    Map<Integer, List<Integer>> stopToRoutes = new HashMap<>();
    for (int i = 0; i < routes.length; i++)
        for (int stop : routes[i])
            stopToRoutes.computeIfAbsent(stop, k -> new ArrayList<>()).add(i);

    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visitedRoutes = new HashSet<>();
    Set<Integer> visitedStops = new HashSet<>();

    // seed all routes passing through source
    for (int r : stopToRoutes.getOrDefault(source, new ArrayList<>())) {
        queue.offer(r);
        visitedRoutes.add(r);
    }
    int buses = 1;
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int route = queue.poll();
            for (int stop : routes[route]) {
                if (stop == target) return buses;
                if (visitedStops.contains(stop)) continue;
                visitedStops.add(stop);
                for (int nextRoute : stopToRoutes.getOrDefault(stop, new ArrayList<>())) {
                    if (!visitedRoutes.contains(nextRoute)) {
                        visitedRoutes.add(nextRoute);
                        queue.offer(nextRoute);
                    }
                }
            }
        }
        buses++;
    }
    return -1;
}
```

### 2-12) Pacific Atlantic Water Flow (LC 417) — BFS from Both Oceans
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

### 2-13) Perfect Squares (LC 279) — BFS on Abstract Graph (Number Decomposition)
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