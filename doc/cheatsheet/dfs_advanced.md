# DFS — Advanced Patterns

> **Scope** — The rare and hard DFS techniques a first pass should skip: two-grid validation, edge-direction tracking, cross-component pair counting, Euler paths, Tarjan low-link bridges, trie-backed wildcard search, depth-indexed stack DFS, and the post-order rollups on N-ary and parent-array trees.
> **See also** — *parent sheet*: [dfs.md](./dfs.md) — the ten core DFS templates, the recognition table and the pattern-selection flowchart; [dfs_examples.md](./dfs_examples.md) — the worked-solution archive for the core templates.
> *Neighbouring sheets*: [trie.md](./trie.md) — the trie structure itself, without the branching query; [union_find.md](./union_find.md) — the DSU alternative to a connectivity DFS; [graph.md](./graph.md) — graph representation and traversal order; [tree_codec.md](./tree_codec.md) — the full tree ⟷ string codec write-up; [bfs.md](./bfs.md) — the breadth-first counterpart.

## LeetCode Problem Lists

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Eulerian Circuit](https://leetcode.com/problem-list/eulerian-circuit/)
- [Trie](https://leetcode.com/problem-list/trie/)

## Overview
These are the DFS patterns that appear **once** in an interview loop, not every round. Each one is a
single recognisable trick layered on top of a core template from [dfs.md](./dfs.md) — read that sheet
first, then come here when a problem does not fit any of its ten templates.

### Key Properties
- **Complexity**: per template — see the [Template Comparison Table](#template-comparison-table) below
- **Core Idea**: every pattern here is "plain DFS plus one extra piece of bookkeeping" — an extra grid,
  a direction flag, a discovery timestamp, a consumed-edge set, a depth-indexed stack, a distance bucket
- **When to Use**: only when the problem statement names the extra structure (two grids, edge directions,
  "critical" edges, "use every edge once", indentation, a `parent[]` array)
- **Prerequisite**: the core templates in [dfs.md](./dfs.md)

## Problem Categories

| # | Pattern | Recognition keywords | Canonical LC | Also |
|---|---------|----------------------|--------------|------|
| 1 | DFS with Validation (sub-component detection) | "sub-islands", "subset validation", "inclusion checking" | LC 1905 | 827, 463 |
| 2 | Bidirectional graph with direction tracking | "reorder edges", "reverse routes", "make all paths lead to" | LC 1466 | 1568, 1579 |
| 3 | Component pair counting | "unreachable pairs", "pairs in different components" | LC 2316 | 323, 547 |
| 4 | Euler path / Hierholzer | "use **every edge** exactly once", "reconstruct itinerary" | LC 332 | 753 |
| 5 | Tarjan bridge finding (low-link) | "critical connection", "which edge disconnects the graph" | LC 1192 | 1568 |
| 6 | Trie + DFS wildcard search | "`.` matches any letter", "one edit away", "magic dictionary" | LC 211 | 676 |
| 7 | Depth-indexed stack DFS | tab-indented input, `/`-separated paths, "longest absolute path" | LC 388 | 1233 |
| 8 | Post-order distance-bucket aggregation | "good leaf pairs", "distance between leaves ≤ k" | LC 1530 | 124, 543, 687 |
| 9 | N-ary post-order child min/max rollup | tree as `edges` rooted at 0, answer only for the root | LC 3965 | 559, 590, 1376 |
| 10 | Tree ⟷ string codec | DFS **returns a string** / parses a nesting string | LC 606 / LC 536 | 297, 449, 331, 652 |
| 11 | Parent-array tree, memoized upward depth | input is `parent[]` / `manager[]` with `-1` at the root | LC 4015 | 1376, 1483, 1650 |

## Templates & Algorithms

### Template Comparison Table
| Template | Extra bookkeeping | Time | Space | When to Use |
|----------|-------------------|------|-------|-------------|
| **1. DFS Validation** | a second reference grid + a boolean flag | O(m×n) | O(m×n) | one component must be contained in another |
| **2. Direction Tracking** | a `0/1` flag per bidirectional edge | O(V+E) | O(V+E) | count edges pointing the wrong way |
| **3. Component Pair Counting** | running total of already-processed nodes | O(V+E) | O(V) | count cross-component pairs without O(n²) |
| **4. Hierholzer** | consumed-**edge** marks, post-order append + reverse | O(E log E) | O(E) | must walk every edge exactly once |
| **5. Tarjan low-link** | `disc[]` / `low[]` timestamps | O(V+E) | O(V+E) | bridges / articulation points in one pass |
| **6. Trie + DFS** | branch into all children on a wildcard | O(26^d · L) | O(total chars) | prefix search where the query branches |
| **7. Depth-indexed stack** | `stack[d]` = prefix value of the depth-`d` ancestor | O(N) | O(D) | the input string *is* the tree |
| **8. Distance buckets** | `cnt[d]` array returned per node | O(N·d²) | O(N) | count node pairs by tree distance |
| **9. N-ary rollup** | adjacency list instead of `.left`/`.right` | O(N) | O(N) | value is a pure function of children's values |
| **10. Codec** | shared parse cursor / format template | O(N) | O(N) | encode a tree to a string and back |
| **11. Memoized climb** | `depth[]` memo, `0` doubles as "not computed" | O(N) | O(N) | depth/height from a `parent[]` array

### Template 1: DFS with Validation (Sub-Component Detection) — LC 1905
- **Description**: Traverse one grid/graph structure while validating against another reference structure
- **Recognition**: "Sub-islands", "subset validation", "component matching", "inclusion checking"
- **Key Technique**: DFS traversal with boolean flag that tracks whether ALL cells satisfy a condition
- **Examples**: LC 1905 (Count Sub Islands)
- **Important Notes**:
  - **Boolean Flag Propagation**: Use `res = dfs(...) && res` pattern to accumulate validation results
  - **Mark Visited**: Mark visited cells in the traversal grid to avoid revisiting
  - **Short-circuit Optimization**: Can optimize by returning early if validation fails
  - **Two-Grid Comparison**: One grid for traversal structure, another for validation condition

```java
/**
 * Pattern: DFS traversal on one grid while validating against another grid
 * Use case: Count sub-islands, validate subset components, inclusion checking
 * Key insight: Use boolean flag propagation to track whether ALL cells satisfy condition
 *
 * Time: O(m × n) - visit each cell once
 * Space: O(m × n) - recursion stack + visited set
 */
public int countSubComponents(int[][] grid1, int[][] grid2) {
    if (grid2 == null || grid2.length == 0) {
        return 0;
    }

    int rows = grid2.length;
    int cols = grid2[0].length;
    Set<Integer> visited = new HashSet<>();
    int count = 0;

    // Iterate through grid2 to find all components
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            int flatCoord = r * cols + c;

            // Start DFS on unvisited land cells in grid2
            if (grid2[r][c] == 1 && !visited.contains(flatCoord)) {
                // DFS returns true if ALL cells in this component exist in grid1
                if (dfsValidate(grid1, grid2, r, c, visited)) {
                    count++;
                }
            }
        }
    }

    return count;
}

/**
 * DFS with validation: Check if entire component in grid2 is subset of grid1
 * Returns true only if ALL cells in the component satisfy the condition
 */
private boolean dfsValidate(int[][] grid1, int[][] grid2, int r, int c, Set<Integer> visited) {
    int rows = grid2.length;
    int cols = grid2[0].length;
    int flatCoord = r * cols + c;

    // Base cases
    if (r < 0 || r >= rows || c < 0 || c >= cols
        || grid2[r][c] == 0 || visited.contains(flatCoord)) {
        return true; // Empty/visited cells don't violate the condition
    }

    // Mark as visited
    visited.add(flatCoord);

    // Initialize result as true
    boolean isValid = true;

    // Check condition: Does this cell exist in grid1?
    if (grid1[r][c] == 0) {
        isValid = false; // Found a cell in grid2 that's NOT in grid1
    }

    // CRITICAL: Use && with res to propagate validation through entire component
    // Must visit ALL neighbors even if isValid is false (to mark them as visited)
    isValid = dfsValidate(grid1, grid2, r - 1, c, visited) && isValid;
    isValid = dfsValidate(grid1, grid2, r + 1, c, visited) && isValid;
    isValid = dfsValidate(grid1, grid2, r, c - 1, visited) && isValid;
    isValid = dfsValidate(grid1, grid2, r, c + 1, visited) && isValid;

    return isValid;
}
```

**Python Implementation:**
```python
def count_sub_components(grid1, grid2):
    """
    Count components in grid2 that are completely contained in grid1
    """
    if not grid2 or not grid2[0]:
        return 0

    rows, cols = len(grid2), len(grid2[0])
    visited = set()
    count = 0

    def dfs(r, c):
        """
        DFS with validation
        Returns True if entire component is valid
        """
        # Base cases
        if (r < 0 or r >= rows or c < 0 or c >= cols
            or grid2[r][c] == 0 or (r, c) in visited):
            return True

        visited.add((r, c))

        # Check condition
        is_valid = True
        if grid1[r][c] == 0:
            is_valid = False

        # Visit all neighbors (must visit ALL even if invalid)
        is_valid = dfs(r - 1, c) and is_valid
        is_valid = dfs(r + 1, c) and is_valid
        is_valid = dfs(r, c - 1) and is_valid
        is_valid = dfs(r, c + 1) and is_valid

        return is_valid

    # Main loop
    for r in range(rows):
        for c in range(cols):
            if grid2[r][c] == 1 and (r, c) not in visited:
                if dfs(r, c):
                    count += 1

    return count
```

**Concrete Example: LC 1905 - Count Sub Islands**
```text
Problem: Count islands in grid2 that are completely contained in grid1

grid1: [[1,1,1,0,0],    grid2: [[1,1,1,0,0],
        [0,1,1,1,1],            [0,0,1,0,0],
        [0,0,0,0,0],            [0,1,0,0,0],
        [1,0,0,0,0],            [1,0,1,1,0],
        [1,1,0,1,1]]            [0,1,0,1,0]]

Analysis:
- Island 1 in grid2 (top-left): Cells (0,0), (0,1), (0,2), (1,2)
  → Check grid1: All exist? YES → Count it ✓

- Island 2 in grid2 (middle): Cells (2,1)
  → Check grid1: (2,1) = 0 → NOT a sub-island ✗

- Island 3 in grid2 (bottom): Cells (3,0), (3,2), (3,3), (4,1), (4,3)
  → Check grid1: (3,0) = 1, but (4,1) = 1... complex shape
  → Some cells don't match → NOT a sub-island ✗

Result: 1 sub-island (only the first one)

Key Insight:
- Must traverse ENTIRE island in grid2
- Check EVERY cell against grid1
- Return true only if ALL cells pass validation
```

**Why Boolean Propagation Works:**

```java
// CORRECT: Visit all neighbors, accumulate results
res = dfs(r - 1, c) && res;
res = dfs(r + 1, c) && res;
res = dfs(r, c - 1) && res;
res = dfs(r, c + 1) && res;

// WRONG: Short-circuits, doesn't visit all cells
if (!dfs(r - 1, c)) return false;  // Stops early, leaves cells unvisited!
```

**Pattern Characteristics:**
- **Two Data Sources**: One for structure (grid2), one for validation (grid1)
- **Complete Traversal**: Must visit entire component, cannot short-circuit
- **Boolean Accumulation**: Use `res = dfs(...) && res` pattern
- **Visited Tracking**: Essential to avoid infinite loops and double-counting
- **Total Time**: O(m × n) - each cell visited once
- **Total Space**: O(m × n) - recursion stack + visited set

**When to Use This Pattern:**
- Validate that one component is subset of another
- Check if structure A is completely contained in structure B
- Count valid sub-components with specific properties
- Two-grid comparison problems

**Key Variations:**
1. **Early Termination**: Mark entire component as invalid if one cell fails
2. **Flip Validation**: Check grid2 cells DON'T exist in grid1 (inverse problem)
3. **Multiple Grids**: Validate against multiple reference grids
4. **Weighted Validation**: Sum values during traversal, check threshold

**Similar Problems:**
- LC 1905: Count Sub Islands (two grids, subset validation)
- LC 200: Number of Islands (single grid, basic DFS)
- LC 695: Max Area of Island (single grid, count cells)
- LC 463: Island Perimeter (single grid, count edges)
- LC 827: Making A Large Island (grid modification, max area)

---

### Template 2: Bidirectional Graph with Direction Tracking — LC 1466
- **Description**: Build undirected graph representation of a directed graph, track original edge directions during DFS traversal
- **Recognition**: "Reorder edges", "reverse routes", "make paths lead to", "minimum edge reversals", "orient edges"
- **Key Technique**: Store direction metadata (flag) for each edge in bidirectional adjacency list, count edges needing reversal during DFS
- **Examples**: LC 1466 (Reorder Routes to Make All Paths Lead to the City Zero)
- **Important Notes**:
  - **Bidirectional Graph Construction**: Add both directions for each edge, but mark original direction with flag
  - **Direction Flag**: Use 1 for edges in original direction, 0 for reverse direction
  - **Count During Traversal**: Increment counter when traversing an edge with flag=1 (wrong direction)
  - **Tree Property**: Works well with tree structures (n-1 edges for n nodes)
  - **From Root**: Always start DFS from the target node (the node all paths should lead to)

```java
/**
 * Pattern: Build bidirectional graph with direction flags, count edge reversals via DFS
 * Use case: Reorder edges, reverse routes, make all paths lead to a target node
 * Key insight: Treat directed graph as undirected for traversal, but track original directions
 *
 * Time: O(V + E) - visit each node and edge once
 * Space: O(V + E) - adjacency list + visited array
 */
public int minReorder(int n, int[][] connections) {
    // Build bidirectional adjacency list with direction flags
    // Map: city -> List of [neighbor, direction_flag]
    // direction_flag: 1 if original direction (needs reversal)
    // direction_flag: 0 if reverse direction (correct direction)
    Map<Integer, List<int[]>> adj = new HashMap<>();
    for (int i = 0; i < n; i++) {
        adj.put(i, new ArrayList<>());
    }

    for (int[] c : connections) {
        int from = c[0];
        int to = c[1];

        // Original direction: from -> to (flag = 1, needs reversal)
        adj.get(from).add(new int[]{to, 1});

        // Reverse direction: to -> from (flag = 0, correct direction)
        adj.get(to).add(new int[]{from, 0});
    }

    boolean[] visited = new boolean[n];
    int[] count = {0}; // Use array to pass by reference

    // Start DFS from target node (city 0)
    dfsCountReversals(0, adj, visited, count);

    return count[0];
}

/**
 * DFS to count edges that need reversal
 * Increment count when traversing edge with flag=1 (wrong direction)
 */
private void dfsCountReversals(int node, Map<Integer, List<int[]>> adj,
                                boolean[] visited, int[] count) {
    visited[node] = true;

    for (int[] edge : adj.get(node)) {
        int neighbor = edge[0];
        int directionFlag = edge[1];

        if (!visited[neighbor]) {
            // If flag = 1, edge points away from target (needs reversal)
            if (directionFlag == 1) {
                count[0]++;
            }
            dfsCountReversals(neighbor, adj, visited, count);
        }
    }
}
```

**Python Implementation:**
```python
def min_reorder(n, connections):
    """
    Count minimum edge reversals to make all paths lead to node 0
    """
    # Build bidirectional graph with direction flags
    adj = {i: [] for i in range(n)}

    for src, dst in connections:
        # Original direction: src -> dst (flag=1, needs reversal)
        adj[src].append((dst, 1))
        # Reverse direction: dst -> src (flag=0, correct)
        adj[dst].append((src, 0))

    visited = set()
    count = [0]

    def dfs(node):
        visited.add(node)

        for neighbor, flag in adj[node]:
            if neighbor not in visited:
                # If flag=1, edge points away from 0 (needs reversal)
                if flag == 1:
                    count[0] += 1
                dfs(neighbor)

    dfs(0)  # Start from target node
    return count[0]
```

**Key Concepts:**

1. **Bidirectional Graph Construction**
   - Add both directions for each edge
   - Original direction gets flag=1 (needs reversal)
   - Reverse direction gets flag=0 (already correct)

2. **Why This Works**
   ```text
   Example: connections = [[0,1],[1,3],[2,3],[4,0],[4,5]]

   Original directed graph (edges point away from 0):
   0 -> 1 -> 3
   2 -> 3
   4 -> 0, 4 -> 5

   Need to reverse: 0->1, 1->3, 4->5 (3 reversals)

   During DFS from 0:
   - Visit 1: used edge 0->1 (flag=1) → count++
   - Visit 3: used edge 1->3 (flag=1) → count++
   - Visit 2: used edge 2->3 (flag=0) → no count
   - Visit 4: used edge 4->0 (flag=0) → no count
   - Visit 5: used edge 4->5 (flag=1) → count++
   Total = 3
   ```

3. **Direction Flag Logic**
   - Flag=1: Edge in original direction (current->neighbor)
     - Means we're using an edge pointing away from root
     - Must be reversed
   - Flag=0: Edge in reverse direction (neighbor->current)
     - Means original edge pointed toward root
     - Already correct

4. **Tree Property**
   - Works perfectly for tree structures (n-1 edges)
   - Every node reachable from root
   - No cycles to worry about

**Pattern Characteristics:**
- **Graph Type**: Tree or directed graph
- **Key Technique**: Bidirectional representation with metadata
- **DFS Start**: Always from target node
- **Count Condition**: Edges with flag=1 need reversal
- **Visited Tracking**: Essential for tree traversal
- **Time Complexity**: O(V + E) - linear
- **Space Complexity**: O(V + E) - adjacency list

**When to Use This Pattern:**
- "Reorder routes/edges to make all paths lead to X"
- "Minimum edge reversals to connect all nodes to root"
- "Orient edges so all nodes can reach target"
- Tree/graph problems requiring edge direction changes
- Counting necessary modifications to edge directions

**Similar Problems:**
- LC 1466: Reorder Routes to Make All Paths Lead to the City Zero
- LC 1568: Minimum Number of Days to Disconnect Island (related graph modification)
- LC 1579: Remove Max Number of Edges to Keep Graph Fully Traversable (edge orientation)

---

### Template 3: Component Pair Counting (Unreachable Pairs) — LC 2316
- **Description**: Count pairs of nodes that cannot reach each other in a graph with multiple disconnected components
- **Recognition**: "Unreachable pairs", "count disconnected pairs", "pairs in different components", "isolated node pairs"
- **Key Technique**: Find all components using DFS/Union-Find, then count pairs between different components using cumulative multiplication
- **Examples**: LC 2316 (Count Unreachable Pairs of Nodes in an Undirected Graph)
- **Important Notes**:
  - **Two Counting Approaches**:
    - Forward: `componentSize × nodesProcessed` (nodes already seen)
    - Backward: `componentSize × (n - componentSize - processed)` (remaining nodes)
  - **Avoid Double Counting**: Only count pairs between different components once
  - **Mathematical Optimization**: O(components) instead of O(n²) brute force
  - **Component Discovery**: Use DFS or Union-Find to identify all components
  - **Cumulative Tracking**: Keep running sum of processed nodes to calculate pairs efficiently


```java
/**
 * Pattern: Count pairs of nodes that cannot reach each other across different components
 * Use case: Count unreachable/disconnected pairs, isolated node pairs
 * Key insight: For each component, multiply its size by nodes in OTHER components
 *
 * Time: O(V + E) - DFS to find all components
 * Space: O(V) - visited array + adjacency list
 */

// Approach 1: DFS with Forward Counting (count against already processed)
public long countUnreachablePairs_DFS_Forward(int n, int[][] edges) {
    // Build adjacency list
    List<Integer>[] adj = new ArrayList[n];
    for (int i = 0; i < n; i++) {
        adj[i] = new ArrayList<>();
    }
    for (int[] edge : edges) {
        adj[edge[0]].add(edge[1]);
        adj[edge[1]].add(edge[0]);
    }

    boolean[] visited = new boolean[n];
    long totalUnreachablePairs = 0;
    long nodesProcessed = 0; // Track nodes in components already processed

    // Find each component and count pairs
    for (int i = 0; i < n; i++) {
        if (!visited[i]) {
            // DFS to find component size
            long componentSize = dfs(i, adj, visited);

            /**
             * KEY TRICK: Forward counting
             * Each node in current component is unreachable from
             * ALL nodes in previous components
             *
             * Formula: componentSize × nodesProcessed
             * - componentSize: nodes in current component
             * - nodesProcessed: nodes in all previous components
             */
            totalUnreachablePairs += componentSize * nodesProcessed;

            // Update processed count
            nodesProcessed += componentSize;
        }
    }

    return totalUnreachablePairs;
}

private long dfs(int node, List<Integer>[] adj, boolean[] visited) {
    visited[node] = true;
    long count = 1;

    for (int neighbor : adj[node]) {
        if (!visited[neighbor]) {
            count += dfs(neighbor, adj, visited);
        }
    }

    return count;
}

// Approach 2: Union-Find with Backward Counting (count against remaining unprocessed)
public long countUnreachablePairs_UnionFind_Backward(int n, int[][] edges) {
    // Initialize Union-Find
    int[] parent = new int[n];
    int[] rank = new int[n];
    for (int i = 0; i < n; i++) {
        parent[i] = i;
    }

    // Union all edges
    for (int[] edge : edges) {
        union(edge[0], edge[1], parent, rank);
    }

    // Count component sizes
    Map<Integer, Integer> sizeMap = new HashMap<>();
    for (int i = 0; i < n; i++) {
        int root = find(i, parent);
        sizeMap.put(root, sizeMap.getOrDefault(root, 0) + 1);
    }

    long result = 0;
    long processed = 0;

    /**
     * KEY TRICK: Backward counting
     * For each component, count pairs with ALL remaining unprocessed nodes
     *
     * Formula: size × (n - size - processed)
     * - size: nodes in current component
     * - n: total nodes
     * - processed: nodes in components already counted
     * - (n - size - processed): nodes in OTHER components not yet counted
     *
     * This avoids double counting by only counting forward to remaining components
     */
    for (int size : sizeMap.values()) {
        result += size * (n - size - processed);
        processed += size;
    }

    return result;
}

private int find(int x, int[] parent) {
    if (parent[x] != x) {
        parent[x] = find(parent[x], parent); // Path compression
    }
    return parent[x];
}

private void union(int x, int y, int[] parent, int[] rank) {
    int rootX = find(x, parent);
    int rootY = find(y, parent);

    if (rootX != rootY) {
        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
}

// Approach 3: Alternative - Count total pairs minus reachable pairs
public long countUnreachablePairs_Alternative(int n, int[][] edges) {
    // Build adjacency list
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        adj.add(new ArrayList<>());
    }
    for (int[] edge : edges) {
        adj.get(edge[0]).add(edge[1]);
        adj.get(edge[1]).add(edge[0]);
    }

    /**
     * Total possible pairs = n × (n-1) / 2
     * Reachable pairs = sum of (componentSize × (componentSize-1) / 2) for each component
     * Unreachable pairs = Total - Reachable
     */
    long totalPairs = (long) n * (n - 1) / 2;
    boolean[] visited = new boolean[n];

    for (int i = 0; i < n; i++) {
        if (!visited[i]) {
            long size = dfsCount(i, adj, visited);
            // Subtract reachable pairs within this component
            totalPairs -= (size * (size - 1)) / 2;
        }
    }

    return totalPairs;
}

private long dfsCount(int node, List<List<Integer>> adj, boolean[] visited) {
    visited[node] = true;
    long count = 1;

    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            count += dfsCount(neighbor, adj, visited);
        }
    }

    return count;
}
```

**Python Implementation:**
```python
def count_unreachable_pairs_dfs(n, edges):
    """
    Count unreachable pairs using DFS with forward counting
    """
    # Build adjacency list
    adj = [[] for _ in range(n)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    visited = [False] * n
    total_pairs = 0
    processed = 0

    def dfs(node):
        """DFS to count component size"""
        visited[node] = True
        count = 1
        for neighbor in adj[node]:
            if not visited[neighbor]:
                count += dfs(neighbor)
        return count

    # Find each component
    for i in range(n):
        if not visited[i]:
            component_size = dfs(i)

            # Key trick: multiply by already processed nodes
            total_pairs += component_size * processed
            processed += component_size

    return total_pairs


def count_unreachable_pairs_uf(n, edges):
    """
    Count unreachable pairs using Union-Find with backward counting
    """
    # Initialize Union-Find
    parent = list(range(n))

    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(x, y):
        root_x, root_y = find(x), find(y)
        if root_x != root_y:
            parent[root_x] = root_y

    # Union all edges
    for u, v in edges:
        union(u, v)

    # Count component sizes
    from collections import Counter
    size_map = Counter(find(i) for i in range(n))

    result = 0
    processed = 0

    # Key trick: count against remaining unprocessed nodes
    for size in size_map.values():
        result += size * (n - size - processed)
        processed += size

    return result
```

**Key Concepts:**

1. **Two Counting Approaches**
   ```text
   Forward Counting (Approach 1):
   - Component 1 (size=3): 3 × 0 = 0
   - Component 2 (size=2): 2 × 3 = 6
   - Component 3 (size=4): 4 × 5 = 20
   - Total: 26

   Backward Counting (Approach 2):
   - Component 1 (size=3): 3 × (9-3-0) = 18
   - Component 2 (size=2): 2 × (9-2-3) = 8
   - Component 3 (size=4): 4 × (9-4-5) = 0
   - Total: 26

   Both give same result, different order of calculation
   ```

2. **Why This Works**
   - Nodes in different components CANNOT reach each other
   - Each pair of nodes from different components = 1 unreachable pair
   - Multiplication counts all such cross-component pairs efficiently
   - Avoid O(n²) brute force by tracking cumulative counts

3. **Visualization**
   ```text
   Example: n=7, components=[3,2,2]

   Component A: {0,1,2}  (size=3)
   Component B: {3,4}     (size=2)
   Component C: {5,6}     (size=2)

   Unreachable pairs:
   - A-B: 3×2 = 6 pairs
   - A-C: 3×2 = 6 pairs
   - B-C: 2×2 = 4 pairs
   Total: 16 pairs

   Forward: 3×0 + 2×3 + 2×5 = 0+6+10 = 16 ✓
   Backward: 3×4 + 2×2 + 2×0 = 12+4+0 = 16 ✓
   ```

4. **Common Pitfalls**
   - **Double Counting**: Must only count each pair once
   - **Component Discovery**: Must visit ALL nodes to find all components
   - **Overflow**: Use `long` for large n (up to 10^5 nodes → ~10^10 pairs)
   - **Edge Cases**: Single component (return 0), no edges (return n×(n-1)/2)

**Pattern Characteristics:**
- **Graph Type**: Undirected graph with multiple components
- **Key Insight**: Unreachable = different components
- **Optimization**: Cumulative multiplication instead of nested loops
- **Component Finding**: DFS, BFS, or Union-Find all work
- **Time Complexity**: O(V + E) - linear in graph size
- **Space Complexity**: O(V) - visited tracking or parent array

**When to Use This Pattern:**
- "Count pairs of nodes that cannot reach each other"
- "Number of unreachable/disconnected node pairs"
- "Pairs from different components"
- "Isolated groups" with pair counting
- Graph connectivity with counting requirement

**Similar Problems:**
- LC 2316: Count Unreachable Pairs of Nodes in an Undirected Graph
- LC 323: Number of Connected Components in an Undirected Graph (component counting)
- LC 547: Number of Provinces (similar component detection)
- LC 684: Redundant Connection (Union-Find with components)
- LC 1135: Connecting Cities With Minimum Cost (MST with component awareness)

**Variations:**
1. **Weighted Pairs**: Count with node weights instead of simple counting
2. **Conditional Pairs**: Only count pairs satisfying additional constraints
3. **Dynamic Components**: Add/remove edges and update count incrementally
4. **K-Component Pairs**: Count pairs from components of specific size k

---

### Template 4: Euler Path — Hierholzer's Algorithm (LC 332 Reconstruct Itinerary) ⭐⭐⭐⭐

**When to use**: *"use EVERY edge exactly once"* (not every node). Plain DFS + backtracking is
exponential here; Hierholzer is linear.

**Key Idea**: greedily walk forward consuming edges until you get stuck, then **append the stuck node
to the answer and back off**. Because you append in **post-order** and reverse at the end, the dead-end
you hit first is guaranteed to be the *last* stop of the itinerary. Never mark nodes visited — mark
**edges** consumed (an airport may be revisited many times).

| | Plain DFS/backtracking | Hierholzer |
|---|---|---|
| Marks | nodes visited | edges consumed |
| On dead end | undo & retry another branch | keep it — append node, pop |
| Time | exponential | `O(E log E)` (sorting only) |

```java
// java
// LC 332 - Reconstruct Itinerary
// IDEA: Hierholzer — greedy walk consuming edges, append node on dead end, reverse at the end
// time  = O(E log E)   PriorityQueue ordering; each edge is consumed exactly once
// space = O(E)         adjacency map + explicit stack + route
public List<String> findItinerary(List<List<String>> tickets) {
    // min-heap per airport -> always take the smallest lexical destination first
    Map<String, PriorityQueue<String>> graph = new HashMap<>();
    for (List<String> t : tickets) {
        graph.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
    }

    LinkedList<String> route = new LinkedList<>();
    Deque<String> stack = new ArrayDeque<>();
    stack.push("JFK");

    while (!stack.isEmpty()) {
        PriorityQueue<String> pq = graph.get(stack.peek());
        if (pq != null && !pq.isEmpty()) {
            stack.push(pq.poll());        // consume an edge, walk forward
        } else {
            route.addFirst(stack.pop());  // dead end -> finalize (post-order + reverse in one step)
        }
    }
    return route;
}
```

```python
# python
# LC 332 - Reconstruct Itinerary
# IDEA: Hierholzer — greedy walk consuming edges, append node on dead end, reverse at the end
# time  = O(E log E)   sorting the tickets; each edge is consumed exactly once
# space = O(E)         adjacency lists + explicit stack + route
from collections import defaultdict

def findItinerary(tickets):
    graph = defaultdict(list)
    # sort DESC so list.pop() (from the tail) always yields the smallest airport
    for src, dst in sorted(tickets, reverse=True):
        graph[src].append(dst)

    route, stack = [], ["JFK"]
    while stack:
        # walk forward until the current airport has no unused ticket
        while graph[stack[-1]]:
            stack.append(graph[stack[-1]].pop())
        # dead end -> this airport is finalized, append in POST-ORDER
        route.append(stack.pop())

    return route[::-1]
```

**Gotchas**
- Do **not** keep a `visited` set of airports — the same airport is legitimately visited many times.
- The answer is built **backwards**; forgetting the final reverse (or `addFirst`) is the classic bug.
- The recursive form is the same idea: `for nxt in sorted(graph[u]): consume; dfs(nxt)` then
  `route.append(u)` **after** the loop.

#### Variation: Euler circuit on a de Bruijn graph — LC 753 Cracking the Safe

**Twist**: the graph is implicit. Nodes are the `(n-1)`-length prefixes, edges are the `k^n` possible
passwords; walking an Euler circuit visits every password once, giving the shortest containing string.

```python
# python
# LC 753 - Cracking the Safe
# IDEA: Hierholzer on the de Bruijn graph — node = last (n-1) digits, edge = a full n-digit code
# time = O(k^n), space = O(k^n)
def crackSafe(n, k):
    start = "0" * (n - 1)
    seen, out = set(), []

    def dfs(node):
        for d in map(str, range(k)):
            edge = node + d
            if edge not in seen:
                seen.add(edge)      # consume the EDGE (the code), not the node
                dfs(edge[1:])
                out.append(d)       # post-order append, same as LC 332

    dfs(start)
    return "".join(out) + start
```

---

### Template 5: Tarjan Bridge Finding (Low-Link DFS) — LC 1192 Critical Connections ⭐⭐⭐⭐⭐

**When to use**: *"which edge, if removed, disconnects the graph?"* / find all **bridges** (critical
connections) or articulation points. Brute force (remove each edge, re-run connectivity) is `O(E*(V+E))`;
Tarjan does it in a **single DFS pass**.

**Key Idea**: run a DFS tree and keep two timestamps per node.

- `disc[u]` — when `u` was first discovered (fixed forever).
- `low[u]`  — the smallest `disc` reachable from `u`'s subtree using tree edges plus **at most one back edge**.

**Bridge condition**: for a tree edge `u -> v`, the edge is a bridge iff `low[v] > disc[u]` — i.e. `v`'s
whole subtree has **no** back edge climbing to `u` or above, so cutting `u-v` isolates it.

```java
// java
// LC 1192 - Critical Connections in a Network
// IDEA: Tarjan low-link — bridge iff low[child] > disc[parent]
// time  = O(V + E)     single DFS pass
// space = O(V + E)     adjacency list + disc/low arrays + recursion depth
private List<List<Integer>> graph;
private int[] disc, low;
private int timer = 0;
private List<List<Integer>> bridges = new ArrayList<>();

public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
    graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    for (List<Integer> e : connections) {
        graph.get(e.get(0)).add(e.get(1));
        graph.get(e.get(1)).add(e.get(0));
    }
    disc = new int[n];
    low = new int[n];
    Arrays.fill(disc, -1);            // -1 = unvisited
    timer = 0;
    bridges = new ArrayList<>();

    for (int i = 0; i < n; i++) {
        if (disc[i] == -1) dfs(i, -1);   // loop handles a disconnected graph too
    }
    return bridges;
}

private void dfs(int u, int parent) {
    disc[u] = low[u] = timer++;
    for (int v : graph.get(u)) {
        if (v == parent) continue;                 // don't walk straight back up the tree edge
        if (disc[v] == -1) {
            dfs(v, u);
            low[u] = Math.min(low[u], low[v]);     // pull the child's reach up
            if (low[v] > disc[u]) {
                bridges.add(Arrays.asList(u, v));  // no back edge bypasses u-v => bridge
            }
        } else {
            low[u] = Math.min(low[u], disc[v]);    // back edge: use disc[v], NOT low[v]
        }
    }
}
```

```python
# python
# LC 1192 - Critical Connections in a Network
# IDEA: Tarjan low-link — bridge iff low[child] > disc[parent]
# time  = O(V + E)     single DFS pass
# space = O(V + E)     adjacency list + disc/low arrays + recursion depth
def criticalConnections(n, connections):
    graph = [[] for _ in range(n)]
    for a, b in connections:
        graph[a].append(b)
        graph[b].append(a)

    disc = [-1] * n          # discovery time, -1 = unvisited
    low = [0] * n            # lowest disc reachable from u's subtree via <= 1 back edge
    timer = [0]
    res = []

    def dfs(u, parent):
        disc[u] = low[u] = timer[0]
        timer[0] += 1
        for v in graph[u]:
            if v == parent:
                continue                        # never go straight back up the tree edge
            if disc[v] == -1:
                dfs(v, u)
                low[u] = min(low[u], low[v])
                if low[v] > disc[u]:            # v's subtree cannot reach u or above
                    res.append([u, v])
            else:
                low[u] = min(low[u], disc[v])   # back edge

    for i in range(n):
        if disc[i] == -1:
            dfs(i, -1)
    return res
```

**Gotchas**
- On a back edge use `disc[v]`, **not** `low[v]` — using `low[v]` can wrongly merge cross-subtree reach.
- The `v == parent` skip assumes **no parallel edges** (true on LC 1192). With multi-edges, skip by
  *edge id* instead, otherwise a duplicated edge is wrongly reported as a bridge.
- `n` can be `10^5` — in Python bump `sys.setrecursionlimit(10 ** 6)` or convert to an explicit stack.
- **Sanity check**: any edge inside a cycle is never a bridge; a tree's every edge is a bridge.

---

### Template 6: Trie + DFS Wildcard Search — LC 211 Design Add and Search Words ⭐⭐⭐⭐

**When to use**: prefix data structure where a **query can branch** — `.` matches any letter, or "one
edit away". Insert stays a plain loop; only **search** becomes DFS, branching into all 26 children when
the current query char is a wildcard.

**Key Idea**: `dfs(node, i)` = "can `word[i:]` be matched starting at trie `node`?".
Base case `i == len(word)` returns the node's end-of-word flag (**not** `True` — `"b."` must not match
the *prefix* of `"bad"` unless a word actually ends there).

```java
// java
// LC 211 - Design Add and Search Words Data Structure
// IDEA: trie; '.' in a query branches the DFS into every non-null child
// time  = O(L) per addWord; search O(L) with no '.', O(26^d * L) worst case with d dots
// space = O(total chars) for the trie, O(L) recursion depth
class WordDictionary {
    private final WordDictionary[] children = new WordDictionary[26];
    private boolean isWord = false;

    public void addWord(String word) {
        WordDictionary node = this;
        for (char c : word.toCharArray()) {
            int i = c - 'a';
            if (node.children[i] == null) node.children[i] = new WordDictionary();
            node = node.children[i];
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        return dfs(word, 0, this);
    }

    private boolean dfs(String word, int idx, WordDictionary node) {
        if (node == null) return false;                 // guard inside the child
        if (idx == word.length()) return node.isWord;   // NOT `true` — must end a word
        char c = word.charAt(idx);
        if (c == '.') {
            for (WordDictionary child : node.children) {
                if (dfs(word, idx + 1, child)) return true;   // early return on first hit
            }
            return false;
        }
        return dfs(word, idx + 1, node.children[c - 'a']);
    }
}
```

```python
# python
# LC 211 - Design Add and Search Words Data Structure
# IDEA: dict-based trie; '.' in a query branches the DFS into every child
# time  = O(L) per addWord; search O(L) with no '.', O(26^d * L) worst case with d dots
# space = O(total chars) for the trie, O(L) recursion depth
class WordDictionary:
    def __init__(self):
        self.root = {}

    def addWord(self, word):
        node = self.root
        for ch in word:
            node = node.setdefault(ch, {})
        node['$'] = True                       # end-of-word marker

    def search(self, word):
        def dfs(node, i):
            if i == len(word):
                return '$' in node             # NOT True — must end a word
            ch = word[i]
            if ch == '.':
                # branch into EVERY child -> this is the DFS part
                for k, child in node.items():
                    if k != '$' and dfs(child, i + 1):
                        return True
                return False
            return ch in node and dfs(node[ch], i + 1)

        return dfs(self.root, 0)
```

**Gotchas**
- With a dict-trie, always skip the `'$'` sentinel when iterating children — otherwise `.` "matches"
  the end marker and you recurse into `True`.
- **Early return** the moment a branch succeeds (see [dfs.md → DFS Early Return Pattern](./dfs.md#dfs-early-return-pattern--return-true-eagerly-false-lazily));
  looping all 26 children without returning turns an `O(26^d)` worst case into a guaranteed one.

#### Variation: exact-one-mismatch DFS — LC 676 Implement Magic Dictionary

**Twist**: instead of a wildcard at a known position, carry a **mismatch budget** down the recursion and
require it to be exactly spent (`budget == 0`) at the end.

```python
# python
# LC 676 - Implement Magic Dictionary
# IDEA: trie DFS carrying a mismatch budget; must be fully spent at the word end
# time = O(26^1 * L) practically (one mismatch), space = O(total chars)
class MagicDictionary:
    def __init__(self):
        self.root = {}

    def buildDict(self, dictionary):
        self.root = {}
        for w in dictionary:
            node = self.root
            for ch in w:
                node = node.setdefault(ch, {})
            node['$'] = True

    def search(self, searchWord):
        def dfs(node, i, budget):
            if i == len(searchWord):
                return budget == 0 and '$' in node   # EXACTLY one change required
            for ch, child in node.items():
                if ch == '$':
                    continue
                cost = 0 if ch == searchWord[i] else 1
                if cost <= budget and dfs(child, i + 1, budget - cost):
                    return True
            return False

        return dfs(self.root, 0, 1)
```

---

### Template 7: Depth-Indexed Stack DFS (Implicit Tree from Indentation / Paths) — LC 388 ⭐⭐⭐⭐

**When to use**: the input **encodes a tree** (tab-indented text, `/`-separated paths, nested tokens)
and you need a root-to-leaf aggregate. Don't build the tree — a single stack where **index == depth**
gives you the running "path prefix to the parent" in `O(1)`.

**Key Idea**: `stack[d]` holds the accumulated value of the directory at depth `d`.
Before processing a line at depth `d`, pop until `len(stack) == d + 1` — that pop **is** the
"return from the recursion" step of a DFS; `stack[d]` is then exactly the current node's parent prefix.

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: stack indexed by depth holds the path length up to each ancestor; popping == returning up
// time  = O(N)   N = input length; every char is scanned a constant number of times
// space = O(D)   D = max nesting depth
public int lengthLongestPath(String input) {
    int maxLen = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);                            // depth 0 has an empty prefix

    for (String line : input.split("\n")) {
        int depth = 0;
        while (depth < line.length() && line.charAt(depth) == '\t') depth++;
        String name = line.substring(depth);

        while (stack.size() > depth + 1) stack.pop();   // unwind to this node's parent

        if (name.contains(".")) {
            maxLen = Math.max(maxLen, stack.peek() + name.length());   // file -> a leaf, score it
        } else {
            stack.push(stack.peek() + name.length() + 1);              // dir  -> +1 for the '/'
        }
    }
    return maxLen;
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: stack indexed by depth holds the path length up to each ancestor; popping == returning up
# time  = O(N)   N = len(input); every char is scanned a constant number of times
# space = O(D)   D = max nesting depth
def lengthLongestPath(input):
    max_len = 0
    stack = [0]                                # stack[d] = prefix length of the dir at depth d

    for line in input.split('\n'):
        name = line.lstrip('\t')
        depth = len(line) - len(name)          # number of leading tabs == depth
        while len(stack) > depth + 1:          # pop back up to this node's parent
            stack.pop()
        if '.' in name:
            max_len = max(max_len, stack[depth] + len(name))     # file: leaf, no '/' suffix
        else:
            stack.append(stack[depth] + len(name) + 1)           # dir: +1 for the '/'

    return max_len
```

**Gotchas**
- A **file is a leaf**: score it, never push it. Pushing files corrupts every deeper prefix.
- Return `0` when there is no file at all (`"a"` -> `0`), not the longest directory path.
- The `+1` is for the `'/'` separator that the *directory* contributes, so a top-level file
  (`"file1.txt"`) is scored against `stack[0] == 0` with no leading slash.

#### Variation: prefix-tree DFS with early cut — LC 1233 Remove Sub-Folders from the Filesystem

**Twist**: same "split the path into depth levels" idea, but build an actual trie and **stop descending**
the moment you hit a stored folder — everything below it is by definition a sub-folder.

```python
# python
# LC 1233 - Remove Sub-Folders from the Filesystem
# IDEA: build a path trie, then DFS and cut the branch at the first stored folder
# time = O(total path chars), space = O(total path chars)
def removeSubfolders(folder):
    root = {}
    for f in folder:
        node = root
        for part in f.split('/')[1:]:          # [0] is the empty string before the leading '/'
            node = node.setdefault(part, {})
        node['$'] = f                          # store the full path at its terminal node

    res = []

    def dfs(node):
        if '$' in node:
            res.append(node['$'])
            return                             # CUT: anything deeper is a sub-folder
        for k, child in node.items():
            dfs(child)

    dfs(root)
    return res
```
---

### Template 8: Post-Order Distance-Bucket Aggregation (Leaf-Pair Counting) — LC 1530

**a. Core idea**

Instead of converting the tree to a graph and running BFS from every leaf (O(N²)), a single **post-order DFS** counts leaf pairs in **O(N)**. Each node returns a small **bucket array** `cnt[d]` = *"how many leaves in my subtree are exactly distance `d` below me."*

At every node you do two things:
1. **Combine children into a pair count.** A leaf `d1` deep in the left subtree and a leaf `d2` deep in the right subtree are joined *through this node*, so their path length is `d1 + d2 + 2`. Add `left[d1] * right[d2]` to the global answer whenever `d1 + d2 + 2 ≤ distance`.
2. **Shift up and merge for the parent.** Return `cur[d+1] = left[d] + right[d]` — every leaf is now one edge farther from the parent than it was from this node.

The key insight: **a pair is counted exactly once, at their lowest common ancestor** — the single node where one leaf sits below the left child and the other below the right child. No divide-by-2 needed (unlike the BFS approach).

**b. Pattern**

```python
# python — Post-order distance-bucket aggregation (LC 1530)
# time  = O(N * distance^2)   distance^2 from the d1/d2 double loop per node
# space = O(N)                recursion depth + O(distance) bucket per frame
class Solution:
    def countPairs(self, root, distance):
        self.ans = 0

        def post_order(node):
            # cnt[d] = number of leaves exactly d edges below `node`
            if not node:
                return [0] * (distance + 1)
            if not node.left and not node.right:      # leaf: distance 0 to itself
                base = [0] * (distance + 1)
                base[0] = 1
                return base

            left  = post_order(node.left)
            right = post_order(node.right)

            # (1) join a left-leaf and a right-leaf THROUGH this node (their LCA)
            for d1 in range(distance + 1):
                for d2 in range(distance + 1):
                    if d1 + d2 + 2 <= distance:       # +2 for the two edges via node
                        self.ans += left[d1] * right[d2]

            # (2) shift up by 1 edge for the parent's view
            cur = [0] * (distance + 1)
            for d in range(distance):                 # d+1 must stay in bounds
                cur[d + 1] = left[d] + right[d]
            return cur

        post_order(root)
        return self.ans
```

> **Optimization (prefix-sum counting, LC 1530 editorial V2-3):** replace the O(distance²) double loop with a running prefix sum so pairs are counted in O(distance) per node → overall O(N * distance). Same idea, cheaper join step.

**Recognition signals**
- Count / aggregate over **pairs of leaves (or nodes) constrained by their tree distance**.
- Distance is **small and bounded** (`distance ≤ 10`) → a fixed-size bucket array per node is cheap.
- You want **O(N)-ish** without building a graph — the pairing happens naturally at each LCA.

> **Contrast with the BFS approach:** BFS converts the tree to an undirected graph and runs a bounded BFS from each leaf (O(L·N), each pair counted twice). Post-order DFS keeps the tree structure, counts each pair once at its LCA, and is usually the interview-preferred answer.

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| Number of Good Leaf Nodes Pairs | 1530 | canonical post-order distance-bucket aggregation |
| Binary Tree Maximum Path Sum | 124 | return best downward value, combine left+right at node (LCA join) |
| Diameter of Binary Tree | 543 | return subtree depth, `left_depth + right_depth` joined at node |
| Longest Univalue Path | 687 | return one-side length, combine both sides at each node |
| Count Nodes With the Highest Score | 2049 | post-order subtree size, aggregate at each node ([dfs.md Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-)) |
| Sum of Distances in Tree | 834 | post-order subtree counts + reroot DP (advanced follow-up) |
---

### Template 9: N-ary Tree Post-Order Value Aggregation (Child Min/Max Rollup) — LC 3965

**a. Core idea**

Compute a value for the **root of an N-ary tree** where each node's value depends **only on aggregates of its children's computed values** (typically `min` / `max`), never on the node's own left/right. A single **post-order DFS** returns each node's value up to its parent:

- **Leaf** → return its base value directly (base case: no children).
- **Non-leaf** → recurse into *all* children, track `earliest = min(child values)` and `latest = max(child values)`, then combine with the node's own base value via the problem's formula and return that up.

For LC 3965 the formula is:
```text
ownDuration = (latest - earliest) + baseTime[node]
finishTime  = latest + ownDuration
```

**Two things that make this an N-ary (not binary) tree pattern:**
1. Build an **adjacency list** `graph[parent] = [child, ...]` from the `edges` array — you loop `for child in graph[node]`, *not* `node.left / node.right`.
2. `edges[i] = [u, v]` means **u is the parent of v** → append `v` to `graph[u]` (direction matters; don't build it undirected).

**b. Pattern**

```python
# python — N-ary tree post-order child min/max rollup (LC 3965)
# time  = O(N)   visit each node once
# space = O(N)   adjacency list + recursion depth
from collections import defaultdict

class Solution:
    def finishTime(self, n, edges, baseTime):
        graph = defaultdict(list)
        for u, v in edges:          # u is PARENT of v
            graph[u].append(v)

        def dfs(node):
            # base case: leaf = no children in the graph
            if not graph[node]:
                return baseTime[node]

            earliest, latest = float('inf'), float('-inf')
            for child in graph[node]:        # loop ALL children (N-ary)
                t = dfs(child)               # value bubbles up from child
                earliest = min(earliest, t)
                latest   = max(latest, t)

            own_duration = (latest - earliest) + baseTime[node]
            return latest + own_duration     # return THIS node's value to parent

        return dfs(0)                        # tree rooted at task 0
```

**Recognition signals**
- Tree given as **`edges` + rooted at 0** (N-ary / general tree), not a `TreeNode` with `.left/.right`.
- A node's answer is a pure function of its **children's returned values** (min/max/sum) plus its own weight → classic **bottom-up post-order**.
- You only need the **root's** result → let DFS return the value; no global variable needed.

> **Contrast with binary bottom-up (Pattern 6 / 15):** same "return a value up, combine at parent" shape, but children are an arbitrary-length list from an adjacency list rather than fixed `left`/`right`. Watch the edge direction when building the graph.

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| Finish Time of Tasks I | 3965 | canonical N-ary post-order min/max child rollup |
| Sum of Nodes with Even-Valued Grandparent | 1315 | post-order over tree, aggregate from descendants |
| Maximum Depth of N-ary Tree | 559 | `1 + max(child depths)` — N-ary post-order max rollup |
| N-ary Tree Postorder Traversal | 590 | canonical post-order visit of an N-ary tree |
| Time Needed to Inform All Employees | 1376 | rooted tree via manager array, `max(child times) + own` |
| Count Nodes With the Highest Score | 2049 | post-order subtree aggregation ([dfs.md Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-)) |
---

### Template 10: DFS that Returns / Consumes a String (Tree ⟷ String Codec) — LC 606 / LC 536

**a. Core idea**

Two mirror-image DFS shapes. Both are pre-order; they differ only in **what the recursion returns**:

- **Encode (tree → string)**: DFS returns the **string of its own subtree**; the parent glues the
  children's strings into a format template.
  ```python
  def encode(node):
      if not node:
          return NULL                     # "" for parens, "#" for comma format
      return FMT.format(node.val, encode(node.left), encode(node.right))
  ```
- **Decode (string → tree)**: DFS returns the **node plus how much of the string it consumed** —
  a recursive-descent parser with a shared cursor (`int[]` / instance field / `iter()`).
  ```python
  def decode(i):
      val, i = read_value(s, i)           # digit loop; handle '-'
      node = TreeNode(val)
      if s[i] == '(':                     # left first
          node.left, i = decode(i + 1); i += 1
      if s[i] == '(':
          node.right, i = decode(i + 1); i += 1
      return node, i
  ```

**Recognition signals**
- Return type is `String` (not `int` / `void`) → you're in the encode half.
- Input is a string that *nests* (`4(2(3)(1))(6(5))`) or *marks nulls* (`1,2,#,#,3,#,#`) → decode half.
- The three questions that pin down any such format: **delimiter**, **null representation**
  (explicit marker vs structural nesting), **traversal order** — encoder and decoder must agree.

**Common pitfalls**
- ❌ `int(s[i])` instead of a `while isdigit()` loop → multi-digit values break; also handle `'-'`.
- ❌ Re-slicing the string per call → O(N²); keep one cursor instead.
- ❌ Naive `+=` string building → O(N²); use `StringBuilder` / list-join.
- ❌ LC 606: dropping the empty **left** `()` when only a right child exists → `1(3)` decodes as a
  left child and the mapping is no longer one-to-one.

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| Construct String from Binary Tree | 606 | canonical encode: `"{}({})({})"` + omission rule |
| Construct Binary Tree from String | 536 | canonical decode: recursive descent + cursor |
| Serialize and Deserialize Binary Tree | 297 | both halves; comma delimiter + `#` null marker |
| Serialize and Deserialize BST | 449 | BST order lets you drop null markers |
| Verify Preorder Serialization | 331 | validate the encoding without building the tree |
| Find Duplicate Subtrees | 652 | **post-order** encode used as a HashMap key |
| Recover a Tree From Preorder Traversal | 1028 | depth prefix as the delimiter + stack decode |

> **Full write-up** (encode/decode symmetry table, LC 606 case analysis + visual trace, LC 536 both
> parser styles, Java versions): [`tree_codec.md` → Tree ⟷ String Codec Pattern](./tree_codec.md#3-tree--string-codec-pattern-)
---

### Template 11: Parent-Array Tree — Memoized Upward Depth — LC 4015

**a. Core idea**

The tree arrives as a **`parent[]` array** (`parent[root] = -1`), not as a `TreeNode` and not as an
`edges` list. You are asked for something that depends on each node's **depth** (and often the tree
**height** = `max(depth)`).

You have two directions to choose from, and the array picks one for you:

| Direction | What it needs | Cost |
|-----------|---------------|------|
| **Top-down** (root → leaves) | first invert `parent[]` into a children adjacency list, then DFS/BFS from the root | O(N) + an extra O(N) structure |
| **Bottom-up climb** (node → root) | nothing — `parent[]` *already is* the up-edge | O(N) with a memo, **O(N²) without** |

The climb is the pattern worth memorising, because `parent[]` is literally a pointer to the parent:

```text
depth[x] = 1                        if parent[x] == -1     (root)
depth[x] = depth[parent[x]] + 1     otherwise
```

Run that from every node and memoize. **The memo is the whole trick** — each edge is then walked
exactly once amortized, so the total is O(N). Without it, a path-shaped tree (`0←1←2←…←n-1`) costs
`1 + 2 + … + N` = O(N²).

**Two details that make the code shorter than it looks:**
1. Depth is **1-based**, so `depth[x] == 0` doubles as "not computed yet" → **no separate `visited`
   array** and no `None` sentinel.
2. Recursion terminates on the root's `-1`, not on a node count — a valid `parent[]` is acyclic by
   the problem's guarantee, so no cycle guard is needed.

**⚠️ Why you cannot just sweep `i = 0 … n-1` in one pass:** that only works when the input guarantees
`parent[i] < i` (parent always appears before its child). LC 4015 does **not** — it only guarantees
`0 <= parent[i] <= n-1` — so `depth[i] = depth[parent[i]] + 1` in index order reads a not-yet-filled
entry. Memoized recursion (or BFS from the root) handles arbitrary labelling.

**b. Pattern**

```python
# python — parent-array tree: memoized depth climb (LC 4015)
# time  = O(N)   each node's depth is computed once; each up-edge walked once amortized
# space = O(N)   depth memo + recursion depth (worst case a path-shaped tree)
class Solution:
    def weightedSum(self, parent, nums):
        n = len(parent)
        depth = [0] * n              # 0 == "not computed" (depths are 1-based)

        def get_depth(x):
            if depth[x]:             # memo hit -> stop climbing
                return depth[x]
            if parent[x] == -1:      # root
                depth[x] = 1
            else:
                depth[x] = get_depth(parent[x]) + 1
            return depth[x]

        for i in range(n):           # fill the whole memo
            get_depth(i)

        h = max(depth)               # height = deepest depth
        return sum(nums[i] * (h - depth[i] + 1) for i in range(n))
```

> **Algebraic shortcut**: `Σ nums[i]·(h − d_i + 1)` = `(h+1)·Σ nums[i] − Σ nums[i]·d_i`, so a single
> pass accumulating `Σ nums[i]` and `Σ nums[i]·d_i` finishes it — useful when the weights are
> queried repeatedly and only `h` changes.

**Iterative climb** — the recursion is `O(N)` deep on a path-shaped tree, which blows Python's
default 1000-frame limit at `n = 10^5`. Push the chain onto an explicit stack and unwind it:

```python
# python — same memo, no recursion
# time = O(N), space = O(N)
def get_depth(x, parent, depth):
    stack = []
    while depth[x] == 0:             # climb until a computed node (or the root)
        if parent[x] == -1:
            depth[x] = 1
            break
        stack.append(x)
        x = parent[x]
    d = depth[x]
    while stack:                     # unwind: fill every node on the climbed chain
        d += 1
        depth[stack.pop()] = d
    return d
```

**Recognition signals**
- Input is `parent` / `manager` / `parents` — an **array of ancestors**, with `-1` marking the root.
- The answer needs **depth, height, or an ancestor** — not subtree aggregates. (Needing subtree sums
  or child min/max flips you back to top-down: invert to a children list, then
  [Template 9](#template-9-n-ary-tree-post-order-value-aggregation-child-minmax-rollup--lc-3965).)
- `n` up to `10^5` with a possible path-shaped tree → the memo is required, and in Python so is the
  iterative form.

> **Contrast with Union-Find:** the climb-and-memo is structurally the same walk as DSU `find()` with
> path compression, and `parent[]` even looks like a DSU array — but there is **no `union()`**, no
> merging, and the tree is fixed. Reaching for a DSU here adds `α(N)` bookkeeping for nothing. See
> [union_find.md → When NOT to use Union Find](./union_find.md#3-tips--pitfalls).

**c. Similar LC**

| Problem | LC # | Link to this pattern |
|---------|------|----------------------|
| Weighted Sum of a Tree | 4015 | canonical — memoized depth climb + `height = max(depth)` |
| Time Needed to Inform All Employees | 1376 | `manager[]` parent array; memoize the accumulated time up the chain |
| Kth Ancestor of a Tree Node | 1483 | parent array + **binary lifting** — the climb pre-computed at `2^k` strides |
| LCA of a Binary Tree III | 1650 | climb both parent chains → reduces to "intersection of two linked lists" |
| All Nodes Distance K in Binary Tree | 863 | build a parent map first, then the tree is walkable upward too |
| Number of Nodes in the Sub-Tree With the Same Label | 1519 | the top-down alternative: invert edges to children, post-order aggregate |
| Smallest Missing Genetic Value After Subtree Queries | 2003 | `parents[]` rooted tree; climb the ancestor chain from the value-1 node |
---

### Language Notes: Java & Python DFS Idioms

#### Java: iterative stack and adjacency-list DFS
```java
// Java DFS with Stack
Stack<TreeNode> stack = new Stack<>();
stack.push(root);
while (!stack.isEmpty()) {
    TreeNode node = stack.pop();
    // Process node
    if (node.right != null) stack.push(node.right);
    if (node.left != null) stack.push(node.left);
}

// Graph DFS with adjacency list
void dfs(int node, boolean[] visited, List<List<Integer>> adj) {
    visited[node] = true;
    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            dfs(neighbor, visited, adj);
        }
    }
}
```

#### Python: deque as a stack, adjacency map, recursion limit
```python
# Using collections.deque as stack
from collections import deque
stack = deque([root])
while stack:
    node = stack.pop()  # pop() for stack behavior
    # Process node

# Graph representation
graph = defaultdict(list)  # Adjacency list
visited = set()  # Track visited nodes

# Recursion limit for deep trees
import sys
sys.setrecursionlimit(10000)
```

---

#### Java: pass-by-reference path recording with `StringBuilder`

Used by [dfs.md Template 8 (Path Signature)](./dfs.md#template-8-path-signature-shape-encoding--lc-694):
the signature is accumulated into one shared `StringBuilder` rather than returned up the stack.


   **Key Insight**: StringBuilder is a **reference type** (not a primitive). When passed to a function, changes made inside persist after the function returns.

   ```java
   // Pattern: Create placeholder → Pass to DFS → Use modified result

   Set<String> uniqueIslands = new HashSet<>();

   for (int r = 0; r < rows; r++) {
       for (int c = 0; c < cols; c++) {
           if (grid[r][c] == 1) {
               // 1. Create empty StringBuilder placeholder
               StringBuilder pathSignature = new StringBuilder();

               // 2. Pass to DFS — DFS will modify it in place
               dfs(grid, r, c, pathSignature, 'S');

               // 3. After DFS returns, pathSignature is populated
               //    Add the modified result to set
               if (pathSignature.length() > 0) {
                   uniqueIslands.add(pathSignature.toString());
               }
           }
       }
   }

   private void dfs(int[][] grid, int r, int c, StringBuilder path, char direction) {
       // Base case
       if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
           return;
       }

       // Mark as visited
       grid[r][c] = 0;

       // ✅ MODIFY the reference: append to StringBuilder
       //    This change persists in the caller's pathSignature object
       path.append(direction);

       // Explore neighbors in fixed order
       dfs(grid, r + 1, c, path, 'D');  // Down
       dfs(grid, r - 1, c, path, 'U');  // Up
       dfs(grid, r, c + 1, path, 'R');  // Right
       dfs(grid, r, c - 1, path, 'L');  // Left

       // Backtrack: remove the character added in this call
       path.append('O');  // Backtrack marker
   }
   ```

   **Why This Works:**
   ```text
   Memory Model:

   Main stack frame:
   ├── pathSignature = StringBuilder{} (heap object at address 0x1000)
   └── call dfs(..., pathSignature, 'S')

       DFS stack frame 1:
       ├── path = reference to 0x1000 (SAME object!)
       ├── path.append('S')  → 0x1000 now contains "S"
       └── call dfs(..., path, 'D')

           DFS stack frame 2:
           ├── path = reference to 0x1000 (still SAME object!)
           ├── path.append('D')  → 0x1000 now contains "SD"
           └── return

       Back in frame 1:
       ├── path.append('O')  → 0x1000 now contains "SDO"
       └── return

   Back in main:
   └── pathSignature = StringBuilder{"SDO"}  ✅ (modified!)
   ```

   **Contrast with Primitives:**
   ```java
   // ❌ WRONG: Primitive won't persist changes
   private void dfs(int curSum) {
       curSum++;  // Only affects local copy
   }

   int mySum = 5;
   dfs(mySum);
   System.out.println(mySum);  // Still 5, NOT 6!

   // ✅ CORRECT: Use reference type or return value
   private void dfs(StringBuilder path) {
       path.append('D');  // Affects original StringBuilder
   }

   StringBuilder myPath = new StringBuilder();
   dfs(myPath);
   System.out.println(myPath);  // Modified! ✅
   ```

   **Common Reference Types for This Pattern:**
   | Type | Modifiable? | Use Case |
   |------|-----------|----------|
   | `StringBuilder` | ✅ Yes (`append`, `setCharAt`, `deleteCharAt`) | Build strings incrementally |
   | `List<T>` | ✅ Yes (`add`, `remove`, `set`) | Collect results or paths |
   | `int[]` / `char[]` | ✅ Yes (`arr[i] = value`) | Modify array elements |
   | `Map<K, V>` | ✅ Yes (`put`, `remove`) | Track frequency/state |
   | `int` / `long` (primitives) | ❌ No | Only pass-by-value |
   | `String` | ❌ No (immutable) | Use StringBuilder instead |

## Summary & Quick Reference

| If the statement says… | Reach for | Template |
|---|---|---|
| "islands of `grid2` that are also islands of `grid1`" | two-grid boolean propagation, `res = dfs(...) && res` | 1 |
| "minimum edge reversals so every node reaches X" | undirected graph + direction flag, DFS from X | 2 |
| "how many pairs cannot reach each other" | component sizes + cumulative multiplication | 3 |
| "use every ticket / every edge exactly once" | Hierholzer: consume edges, append on dead end, reverse | 4 |
| "which connections are critical" | Tarjan `low[child] > disc[parent]` | 5 |
| "`.` can match any letter" | trie whose search branches on the wildcard | 6 |
| tab-indented text or `a/b/c` paths | stack indexed by depth, pop == return up | 7 |
| "good leaf pairs within distance `d`" | post-order bucket array, join at the LCA | 8 |
| tree given as `edges`, rooted at 0 | adjacency list + post-order min/max rollup | 9 |
| tree ⟷ string, both directions | format template down, parse cursor back up | 10 |
| input is `parent[]` with `-1` for the root | memoized climb; `depth[x] == 0` means "not computed" | 11 |

**Pitfalls that are unique to this sheet**
- Hierholzer marks **edges**, never nodes — an airport is legitimately revisited.
- Tarjan uses `disc[v]` on a back edge, never `low[v]`.
- A dict-trie DFS must skip the `'$'` end-of-word sentinel when iterating children.
- A depth-indexed stack must never push a **leaf** (a file), only an interior node (a directory).
- The memoized depth climb is O(N²) without the memo on a path-shaped tree.
- `n` up to `10^5` plus recursion means Python needs `sys.setrecursionlimit(...)` or an explicit stack
  in Templates 5, 8 and 11.

### Related Topics
- **[union_find.md](./union_find.md)**: Templates 1 and 3 both have a DSU form; Template 11 looks like
  DSU but has no `union()`.
- **[trie.md](./trie.md)**: the structure Template 6 searches.
- **[topology_sorting.md](./topology_sorting.md)**: the other classic "DFS with timestamps" algorithm.
- **[bfs.md](./bfs.md)**: Template 8's BFS-from-every-leaf alternative, and the safer form of any
  deep-recursion template here.
