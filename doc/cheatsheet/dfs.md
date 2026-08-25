# DFS (Depth-First Search)

> **Scope** — The main DFS reference: the ten core depth-first templates — tree traversal, grid flood fill, path finding, backtracking, tree modification, post-order aggregation, boundary elimination, shape signatures and weighted-edge traversal — with the recognition table that picks between them.
> **See also** — *deep dives split out of this file*: [dfs_advanced.md](./dfs_advanced.md) — Euler paths (Hierholzer), Tarjan bridges, trie + wildcard DFS, depth-indexed stack DFS, distance-bucket leaf pairing, N-ary and `parent[]` rollups; [dfs_examples.md](./dfs_examples.md) — the worked-solution archive and the full problem index by pattern and difficulty.
> *Neighbouring sheets*: [bfs.md](./bfs.md) — the breadth-first counterpart and how to choose; [backtrack.md](./backtrack.md) — DFS that undoes state on the way back up; [graph.md](./graph.md) — representation; [tree.md](./tree.md) — DFS on trees.

## LeetCode Problem Lists

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Overview
**Depth-First Search (DFS)** is a graph/tree traversal algorithm that explores as far as possible along each branch before backtracking. It uses recursion or a stack to maintain the traversal path.

### Key Properties
- **Time Complexity**: O(V + E) for graphs, O(n) for trees
- **Space Complexity**: O(h) for recursion stack, where h = height
- **Core Idea**: Go deep before going wide
- **Data Structure**: Stack (implicit via recursion or explicit)
- **When to Use**: Path finding, cycle detection, topological sort, tree traversal, backtracking problems

### References
- [DFS Visualization](https://www.cs.usfca.edu/~galles/visualization/DFS.html)
- [DFS vs BFS Comparison](https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_vs_bfs.png)
- [Tree Traversal Animations](https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_2.png)

## Problem Categories

Each pattern below is presented **exactly once** — as a template in the next section. This table is
the index into them: match the recognition keywords, then jump to the template.

| # | Pattern | Recognition keywords | Template | Canonical LC | Also |
|---|---------|----------------------|----------|--------------|------|
| 1 | Tree traversal | "traverse", "visit all", "print tree", "serialize" | [T1](#template-1-tree-traversal-lc-94) | LC 94 | 144, 145, 297, 449, 100 |
| 2 | Graph / grid traversal, components | "connected components", "islands", "cycle detection" | [T2](#template-2-graph-grid-dfs-flood-fill-lc-200) | LC 200 | 695, 133, 207, 210, 419 |
| 3 | Path problems | "path sum", "root to leaf", "all paths", "does a path exist" | [T3](#template-3-path-finding-lc-112) | LC 112 | 113, 257, 129, 1971 |
| 4 | Backtracking | "all combinations", "permutations", "subsets" | [T4](#template-4-backtracking-lc-46) | LC 46 | 78, 39, 17, 22, 51, 79 |
| 5 | Tree modification | "delete", "insert", "trim", "convert" | [T5](#template-5-tree-modification-lc-450) | LC 450 | 701, 669, 538, 226, 114 |
| 6 | Subtree aggregation & LCA | "subtree sum", "duplicate subtrees", "LCA", "deepest leaves" | [T6](#template-6-bottom-up-post-order-dfs-lc-543) | LC 543 | 124, 236, 508, 652, 663, 2049 |
| 7 | Boundary elimination (2 passes) | "closed islands", "surrounded regions", "captured" | [T7](#template-7-2-pass-dfs-boundary-elimination-lc-1254) | LC 1254 | 130, 417, 1020 |
| 8 | Path signatures (shape encoding) | "distinct islands", "unique shapes", "same shape after translation" | [T8](#template-8-path-signature-shape-encoding-lc-694) | LC 694 | 711, 652 |
| 9 | Grid DFS + backtracking | "one path", "collect the most", "cannot revisit a cell" | [T9](#template-9-grid-dfs-backtracking-3-styles-compared-lc-1219-path-with-maximum-gold) | LC 1219 | 79, 329, 980 |
| 10 | Weighted-edge DFS (ratio queries) | "evaluate division", "exchange rates", "transitive ratios" | [T10](#template-10-weighted-graph-dfs-division-ratio-queries-lc-399) | LC 399 | 721, 1101, 737 |

**Not on this sheet** — these live in [dfs_advanced.md](./dfs_advanced.md): two-grid validation (LC 1905),
edge-direction tracking (LC 1466), component pair counting (LC 2316), Euler paths (LC 332, 753), Tarjan
bridges (LC 1192), trie + wildcard DFS (LC 211, 676), depth-indexed stack DFS (LC 388, 1233),
distance-bucket leaf pairing (LC 1530), N-ary post-order rollup (LC 3965), tree ⟷ string codecs
(LC 606, 536) and `parent[]`-array depth climbs (LC 4015). The full problem list by pattern and by
difficulty is in [dfs_examples.md → Problems by Pattern](./dfs_examples.md#problems-by-pattern).

## Templates & Algorithms

### Template Comparison Table
| Template | Use Case | Key Operation | Time | Space | When to Use |
|----------|----------|---------------|------|-------|-------------|
| **1. Tree Traversal** | Visit all nodes | Recursive/Stack | O(n) | O(h) | Tree problems |
| **2. Graph / Grid DFS** | Explore graph, flood fill | Visited set / in-place mark | O(V+E) | O(V) | Graph & grid exploration |
| **3. Path Finding** | Find specific paths | Track path | O(n) | O(h) | Path problems |
| **4. Backtracking** | Try all paths | Undo choices | O(b^d) | O(d) | Combinatorial |
| **5. Modification** | Change structure | Update nodes | O(n) | O(h) | Tree editing |
| **6. Bottom-up** | Aggregate info | Post-order return | O(n) | O(h) | Subtree problems |
| **7. 2-Pass DFS** | Boundary elimination | Two-phase flood | O(m×n) | O(m×n) | Closed/surrounded regions |
| **8. Path Signature** | Encode shapes | Directional tracking | O(m×n) | O(m×n) | Distinct shape counting |
| **9. Grid DFS + Backtrack** | One best path in a grid | Mark, recurse, **restore** | O(4^k) | O(k) | Overlapping paths from many starts |
| **10. Weighted Graph DFS** | Ratio/division queries | Product accumulation | O(Q·(V+E)) | O(V+E) | Transitive ratio computation |

### Universal DFS Template
```python
def dfs(node, visited=None):
    """
    Universal DFS template for trees and graphs
    Can be adapted for various problems
    """
    # Base case
    if not node or (visited and node in visited):
        return
    
    # Mark as visited (for graphs)
    if visited is not None:
        visited.add(node)
    
    # Process current node (pre-order position)
    process(node)
    
    # Recursive calls
    for neighbor in get_neighbors(node):
        dfs(neighbor, visited)
    
    # Post-order processing if needed
    # process_after(node)
```

### Template 1: Tree Traversal — LC 94 ⭐⭐⭐⭐⭐
- **Description**: Visit all nodes in specific order (preorder, inorder, postorder)
- **Recognition**: "Traverse", "visit all", "print tree", "serialize"
- **Examples**: LC 94, LC 144, LC 145, LC 297, LC 449

```python
# Preorder: Root -> Left -> Right
def preorder(root):
    if not root:
        return []
    return [root.val] + preorder(root.left) + preorder(root.right)

# Inorder: Left -> Root -> Right  
def inorder(root):
    if not root:
        return []
    return inorder(root.left) + [root.val] + inorder(root.right)

# Postorder: Left -> Right -> Root
def postorder(root):
    if not root:
        return []
    return postorder(root.left) + postorder(root.right) + [root.val]

# Iterative with Stack
def dfs_iterative(root):
    if not root:
        return []
    
    stack = [root]
    result = []
    
    while stack:
        node = stack.pop()
        result.append(node.val)
        # Add right first so left is processed first (LIFO)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    
    return result
```

### Template 2: Graph / Grid DFS (Flood Fill) — LC 200 ⭐⭐⭐⭐⭐
- **Description**: Explore graphs, find components, detect cycles
- **Recognition**: "Connected components", "islands", "cycle detection"
- **Examples**: LC 200, LC 695, LC 133, LC 207, LC 210

```python
def dfs_graph(graph, start):
    """
    DFS for graph with cycle handling
    """
    visited = set()
    result = []
    
    def dfs(node):
        if node in visited:
            return
        
        visited.add(node)
        result.append(node)
        
        for neighbor in graph[node]:
            dfs(neighbor)
    
    dfs(start)
    return result

# For detecting cycles
def has_cycle(graph):
    visited = set()
    rec_stack = set()
    
    def dfs(node):
        visited.add(node)
        rec_stack.add(node)
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                if dfs(neighbor):
                    return True
            elif neighbor in rec_stack:
                return True
        
        rec_stack.remove(node)
        return False
    
    for node in graph:
        if node not in visited:
            if dfs(node):
                return True
    return False
```

#### Variation: count components **without** flood fill — LC 419 Battleships in a Board

**Twist**: when every component is guaranteed to be a straight 1×k / k×1 line, you don't need DFS at
all — count only the cells that are the **top-left end** of a ship, which makes it `O(1)` extra space
(no `visited` set, no in-place mutation). Good answer to the classic follow-up *"can you do it in one
pass, O(1) space, without modifying the board?"* on top of the LC 200 flood-fill baseline.

```java
// java
// LC 419 - Battleships in a Board
// IDEA: a cell starts a NEW ship iff it is 'X' and has no 'X' above and no 'X' to its left
// time = O(M*N), space = O(1)
public int countBattleships(char[][] board) {
    int count = 0;
    for (int r = 0; r < board.length; r++) {
        for (int c = 0; c < board[0].length; c++) {
            if (board[r][c] != 'X') continue;
            if (r > 0 && board[r - 1][c] == 'X') continue;   // continuation of a vertical ship
            if (c > 0 && board[r][c - 1] == 'X') continue;   // continuation of a horizontal ship
            count++;
        }
    }
    return count;
}
```

```python
# python
# LC 419 - Battleships in a Board
# IDEA: count only the top-left cell of each ship -> no visited set needed
# time = O(M*N), space = O(1)
def countBattleships(board):
    count = 0
    for r in range(len(board)):
        for c in range(len(board[0])):
            if board[r][c] != 'X':
                continue
            if r > 0 and board[r - 1][c] == 'X':
                continue
            if c > 0 and board[r][c - 1] == 'X':
                continue
            count += 1
    return count
```

> If the "ships are straight lines" guarantee is dropped, fall back to the plain LC 200 grid DFS above.

### Template 3: Path Finding — LC 112 ⭐⭐⭐⭐⭐
- **Description**: Find paths with specific properties in trees/graphs
- **Recognition**: "Path sum", "root to leaf", "all paths", "longest path"
- **Examples**: LC 112, LC 113, LC 257, LC 124, LC 543

**📚 Related Patterns**: For comprehensive path problem patterns with multiple variations (path sum, max path, consecutive sequences, prefix sum technique), see **bst.md Template 7 (Path Problems)** which provides 7 detailed path patterns with full implementations.

```python
def find_paths(root, target):
    """
    Find all root-to-leaf paths with sum = target
    """
    def dfs(node, curr_sum, path, result):
        if not node:
            return
        
        # Update current state
        curr_sum += node.val
        path.append(node.val)
        
        # Check if leaf and target met
        if not node.left and not node.right:
            if curr_sum == target:
                result.append(path[:])
        
        # Explore children
        dfs(node.left, curr_sum, path, result)
        dfs(node.right, curr_sum, path, result)
        
        # Backtrack
        path.pop()
    
    result = []
    dfs(root, 0, [], result)
    return result
```

### DFS Early Return Pattern — return TRUE eagerly, FALSE lazily
**Problem**: When searching for a path in DFS, what's the difference between these two approaches?

#### ❌ WRONG Approach: Not Checking Return Value
```java
private boolean dfsPathVisitor(int node, int destination, Map<Integer, List<Integer>> map, boolean[] visited) {
    if (node == destination) return true;

    visited[node] = true;

    for (int next : map.get(node)) {
        if (!visited[next]) {
            // ❌ WRONG: Ignoring return value - continues searching even after path found!
            dfsPathVisitor(next, destination, map, visited);
        }
    }

    return false;  // Will ALWAYS return false (except for direct hits)
}
```

#### ✅ CORRECT Approach: Early Return on Success
```java
private boolean dfsPathVisitor(int node, int destination, Map<Integer, List<Integer>> map, boolean[] visited) {
    if (node == destination) return true;

    visited[node] = true;

    for (int next : map.get(node)) {
        if (!visited[next]) {
            // ✅ CORRECT: Return immediately when path found!
            if (dfsPathVisitor(next, destination, map, visited)) {
                return true;
            }
        }
    }

    return false;  // Only return false if ALL paths explored
}
```

---

#### 📊 Concrete Example: Why Early Return Matters

**Test Case:**
```text
Graph: 0 -- 1 -- 2 -- 3
       |         |
       4 -------- 5

Adjacency List:
0: [1, 4]
1: [0, 2]
2: [1, 3, 5]
3: [2]
4: [0, 5]
5: [2, 4]

Task: Find path from 0 to 3
```

---

##### Scenario 1: ❌ WRONG (Without Early Return)

**Call Stack Trace:**
```text
1. dfsPathVisitor(0, 3, ..., visited=[])
   → visited = [0]
   → Loop neighbors: [1, 4]

   2. dfsPathVisitor(1, 3, ..., visited=[0])  // First neighbor
      → visited = [0, 1]
      → Loop neighbors: [0, 2]  (skip 0, already visited)

      3. dfsPathVisitor(2, 3, ..., visited=[0,1])
         → visited = [0, 1, 2]
         → Loop neighbors: [1, 3, 5]  (skip 1)

         4. dfsPathVisitor(3, 3, ..., visited=[0,1,2])
            → ✅ Found! Returns TRUE

         ← Returns TRUE to level 3

      ← But level 2 IGNORES the return value!
      ← Continues checking neighbor 5

      5. dfsPathVisitor(5, 3, ..., visited=[0,1,2])
         → visited = [0, 1, 2, 5]
         → Loop neighbors: [2, 4]  (both visited)
         ← Returns FALSE

      ← Level 2 finishes loop, returns FALSE

   ← Level 1 receives FALSE from neighbor 1

   6. dfsPathVisitor(4, 3, ..., visited=[0,1,2,5])  // Second neighbor
      → visited = [0, 1, 2, 5, 4]
      → Loop neighbors: [0, 5]  (both visited)
      ← Returns FALSE

   ← Level 0 finishes loop, returns FALSE

❌ FINAL RESULT: FALSE (Path exists but not detected!)
```

**Why it fails:**
- Found destination at step 4 (returned TRUE)
- But parent call at step 3 **ignored** the TRUE result
- Continued exploring other neighbors unnecessarily
- Eventually returned FALSE because other paths didn't reach destination

---

##### Scenario 2: ✅ CORRECT (With Early Return)

**Call Stack Trace:**
```text
1. dfsPathVisitor(0, 3, ..., visited=[])
   → visited = [0]
   → Loop neighbors: [1, 4]

   2. dfsPathVisitor(1, 3, ..., visited=[0])  // First neighbor
      → visited = [0, 1]
      → Loop neighbors: [0, 2]  (skip 0)

      3. dfsPathVisitor(2, 3, ..., visited=[0,1])
         → visited = [0, 1, 2]
         → Loop neighbors: [1, 3, 5]  (skip 1)

         4. dfsPathVisitor(3, 3, ..., visited=[0,1,2])
            → ✅ Found! Returns TRUE

         ← Returns TRUE to level 3

      ← Level 2 checks: if (TRUE) return true;  ✅
      ← Returns TRUE immediately (skips remaining neighbors!)

   ← Level 1 checks: if (TRUE) return true;  ✅
   ← Returns TRUE immediately (skips neighbor 4!)

✅ FINAL RESULT: TRUE (Correct!)
```

**Why it works:**
- Found destination at step 4 (returned TRUE)
- Parent call at step 3 **checked** the return value
- Immediately returned TRUE without exploring other paths
- Propagated TRUE all the way back to the root

---

#### 🎯 Key Insights

| Aspect | ❌ Without Early Return | ✅ With Early Return |
|--------|------------------------|---------------------|
| **Correctness** | ❌ Returns FALSE even when path exists | ✅ Returns TRUE when path found |
| **Efficiency** | Explores ALL paths unnecessarily | Stops immediately upon finding path |
| **Time Complexity** | O(V + E) always (full traversal) | O(V + E) worst case, but often much better |
| **Use Case** | Collecting ALL paths/results | Finding ANY path (exists/not exists) |

---

#### 📝 When to Use Each Pattern

##### Pattern 1: Early Return (Path Existence Check)
```java
// Use when: "Does path exist?" "Can we reach?" "Is there a route?"
if (dfs(next)) {
    return true;  // Found one path - that's enough!
}
```
**Examples:** LC 1971 (Path Exists), LC 797 (All Paths), LC 79 (Word Search)

##### Pattern 2: Continue Without Return (Collecting All Results)
```java
// Use when: "Find ALL paths" "Count all solutions" "Collect all combinations"
dfs(next);  // Don't return early - need to explore all branches
```
**Examples:** LC 257 (All Root-to-Leaf Paths), LC 113 (Path Sum II), LC 22 (Generate Parentheses)

---

### Template 4: Backtracking — LC 46
- **Description**: Try all possibilities, undo choices
- **Recognition**: "All combinations", "permutations", "subsets"
- **Examples**: LC 46, LC 78, LC 39, LC 17

```python
def backtrack_template(candidates, target):
    """
    General backtracking template
    """
    def backtrack(start, path, remaining):
        # Base case - found solution
        if remaining == 0:
            result.append(path[:])
            return
        
        # Try all possibilities
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                continue
            
            # Make choice
            path.append(candidates[i])
            
            # Recurse
            backtrack(i, path, remaining - candidates[i])
            
            # Undo choice (backtrack)
            path.pop()
    
    result = []
    backtrack(0, [], target)
    return result
```

### Template 5: Tree Modification — LC 450
- **Description**: Modify tree structure or values during traversal
- **Recognition**: "Delete", "insert", "trim", "convert"
- **Examples**: LC 450, LC 701, LC 669, LC 538

```python
def modify_tree(root, condition):
    """
    Modify tree structure based on condition
    """
    if not root:
        return None
    
    # Recursively modify subtrees first
    root.left = modify_tree(root.left, condition)
    root.right = modify_tree(root.right, condition)
    
    # Modify current node based on condition
    if not condition(root):
        # Example: delete node, return child
        if not root.left:
            return root.right
        if not root.right:
            return root.left
        # Handle two children case
        # ... (find successor/predecessor)
    
    return root
```

#### Idiom: reassign the subtree, then return the node
- Assign sub tree to node, then return updated node at final stage (Important !!!!)

```java
// java
// LC 199
private TreeNode _dfs(TreeNode node){

    if (node == null){
        return null;
    }

    /** NOTE !!! no need to create global node, but can define inside the method */
    TreeNode root2 = node;
    root2.left = this._dfs(node.left);
    root2.right = this._dfs(node.right);

    /** NOTE !!! we need to return root as final step */
    return root2;
}
```

### Template 6: Bottom-up (Post-Order) DFS — LC 543 ⭐⭐⭐⭐⭐
- **Description**: Process subtrees and aggregate results bottom-up; find the lowest common ancestor of target nodes
- **Recognition**: "Subtree sum", "duplicate subtrees", "LCA", "smallest subtree containing", "lowest common ancestor", "deepest leaves"
- **Examples**: LC 508, LC 652, LC 236, LC 663, LC 865, LC 1123
- **When to Use LCA Approach**:
  - Two (or more) target nodes exist in different subtrees and you need the first node that "sees" both sides
  - "Smallest subtree that contains [condition X]" — this is LCA in disguise
  - Targets may be **given** (LC 236: find LCA of p, q) or **implicit** (LC 865/1123: all nodes at max depth)
- **Core Idea (Post-Order / Bottom-Up)**:
  1. Recurse left and right subtrees first (post-order)
  2. Each subtree returns a `(node, depth/info)` pair upward
  3. At each node, compare left vs right results:
     - **Left deeper** → answer is in the left subtree, propagate left result up
     - **Right deeper** → answer is in the right subtree, propagate right result up
     - **Equal depth** → current node is the LCA (deepest paths meet here), return current node
  4. The root of the recursion holds the final answer
- **Key Variants**:
  - **Standard LCA (LC 236)**: Targets p, q are given; return first node that sees both in different subtrees
  - **Depth-Based LCA (LC 865/1123)**: Targets are discovered (deepest nodes); use depth comparison to find where deepest paths converge
  - **Paint + Answer (LC 865 Editorial V1)**: Two-pass — first DFS computes all depths, second DFS finds the subtree containing all max-depth nodes
  - **BFS + Parent Map (LC 865 V0-4)**: BFS to find deepest level, then walk parents upward until all converge to one node
- **Similar Classic LC Problems**:
  - LC 236 - Lowest Common Ancestor of a Binary Tree (standard LCA)
  - LC 235 - Lowest Common Ancestor of a Binary Search Tree (BST property optimization)
  - LC 865 - Smallest Subtree with all the Deepest Nodes (depth-based LCA)
  - LC 1123 - Lowest Common Ancestor of Deepest Leaves (same as LC 865)
  - LC 1644 - Lowest Common Ancestor of a Binary Tree II (nodes may not exist)
  - LC 1650 - Lowest Common Ancestor of a Binary Tree III (with parent pointers)
  - LC 1676 - Lowest Common Ancestor of a Binary Tree IV (multiple target nodes)

```python
def bottom_up_dfs(root):
    """
    Process subtrees first, then current node
    Useful for subtree problems
    """
    def dfs(node):
        if not node:
            return 0  # or base value

        # Process subtrees first
        left_result = dfs(node.left)
        right_result = dfs(node.right)

        # Process current node using subtree results
        current_result = process(node, left_result, right_result)

        # Update global result if needed
        self.global_result = max(self.global_result, current_result)

        return current_result

    self.global_result = 0
    dfs(root)
    return self.global_result
```

#### Global-accumulator form — LC 124 Binary Tree Maximum Path Sum
```python
class Solution:
    def maxPathSum(self, root):
        self.max_sum = float('-inf')
        
        def dfs(node):
            if not node:
                return 0
            left = max(0, dfs(node.left))
            right = max(0, dfs(node.right))
            self.max_sum = max(self.max_sum, left + right + node.val)
            return max(left, right) + node.val
        
        dfs(root)
        return self.max_sum
```

#### Variation: subtree size aggregation (remove-node scoring) — LC 2049
- **Description**: Post-order DFS that returns each node's **subtree size**, while simultaneously computing a per-node value (score) derived from the sizes of the components formed when that node is removed
- **Recognition**: "remove node and edges → tree splits into subtrees", "product/sum of component sizes", "score of a node", "tree given as `parents[]` array"
- **Key Technique**: One DFS returns `subtree_size = 1 + Σ child_subtree_size`. When node is removed, the components are (a) each child's subtree, and (b) the **parent side** = `n - subtree_size`. Aggregate these on the fly.
- **Examples**: LC 2049 (Count Nodes With the Highest Score)
- **Core Idea**:
  - Removing node `x` cuts it into `len(children[x])` child components **plus** the "above" component (everything outside x's subtree).
  - `child component size` = subtree size of each child (returned by DFS).
  - `parent / above component size` = `n - subtree_size(x)` (only counts if `> 0`, i.e. x is not the root).
  - `score(x) = Π(child subtree sizes) × max(1, n - subtree_size(x))` — every subtree size is computed exactly **once**, giving O(n) time / O(n) space (needed since n ≤ 10^5).
- **Build the tree from `parents[]`**: `children[parents[i]].append(i)` for `i != root`; root is the index where `parents[i] == -1` (usually node 0).
- **Pattern variants**:
  - **One-pass DFS** (return size + multiply/track max inline) — most concise
  - **Two-pass** (pass 1: precompute `subtree_size[]` array; pass 2: iterate nodes computing scores) — decouples size calc from scoring, easier to reason about
- **Important Notes**:
  - Guard the parent component with `max(1, ...)` or `if remaining > 0` — root has no "above" component.
  - Use a `Counter`/dict keyed by score to count how many nodes hit the max, or track `(max_score, count)` running maxima.
  - Generalizes beyond binary trees — the same DFS works for any tree given via `parents[]`/adjacency list.
- **Similar Classic LC Problems**:
  - LC 2049 - Count Nodes With the Highest Score (canonical remove-node scoring)
  - LC 1519 - Number of Nodes in the Sub-Tree With the Same Label (subtree aggregation via DFS)
  - LC 508 - Most Frequent Subtree Sum (per-subtree value + frequency count)
  - LC 543 - Diameter of Binary Tree (bottom-up subtree metric)
  - LC 124 - Binary Tree Maximum Path Sum (return subtree value, aggregate global max)
  - LC 834 - Sum of Distances in Tree (subtree size + reroot DP, advanced follow-up)

### Template 7: 2-Pass DFS (Boundary Elimination) — LC 1254
- **Description**: Eliminate boundary-connected cells first, then process interior
- **Recognition**: "Closed islands", "surrounded regions", "captured pieces"
- **Examples**: LC 1254, LC 130, LC 417

```java
// java
// LC 1254
// V0
// IDEA: 2-Pass DFS (Boundary Elimination)
/**
 * Algorithm:
 * Pass 1: Start from all boundary cells and flood-fill to eliminate
 *         all islands connected to the boundary (these cannot be closed)
 * Pass 2: Count remaining land cells as closed islands
 *
 * Time: O(m×n), Space: O(m×n) for recursion stack
 */
public int closedIsland(int[][] grid) {
    if (grid == null || grid.length == 0) {
        return 0;
    }

    int rows = grid.length;
    int cols = grid[0].length;

    // Pass 1: Eliminate boundary-connected islands
    // Flood top and bottom borders
    for (int c = 0; c < cols; c++) {
        flood(grid, 0, c);           // Top border
        flood(grid, rows - 1, c);    // Bottom border
    }

    // Flood left and right borders
    for (int r = 0; r < rows; r++) {
        flood(grid, r, 0);           // Left border
        flood(grid, r, cols - 1);    // Right border
    }

    // Pass 2: Count closed islands
    int count = 0;
    for (int r = 1; r < rows - 1; r++) {
        for (int c = 1; c < cols - 1; c++) {
            if (grid[r][c] == 0) {
                count++;
                flood(grid, r, c);  // Mark entire island
            }
        }
    }

    return count;
}

private void flood(int[][] grid, int r, int c) {
    int rows = grid.length;
    int cols = grid[0].length;

    // Base case: out of bounds or water
    if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 1) {
        return;
    }

    grid[r][c] = 1;  // Mark land as water (visited)

    // Flood 4-directionally
    flood(grid, r + 1, c);
    flood(grid, r - 1, c);
    flood(grid, r, c + 1);
    flood(grid, r, c - 1);
}
```

```python
# python
# LC 1254
def closedIsland(grid):
    """
    2-Pass DFS approach
    """
    if not grid or not grid[0]:
        return 0

    rows, cols = len(grid), len(grid[0])

    def flood(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 1:
            return
        grid[r][c] = 1
        flood(r + 1, c)
        flood(r - 1, c)
        flood(r, c + 1)
        flood(r, c - 1)

    # Pass 1: Eliminate boundary islands
    for c in range(cols):
        flood(0, c)
        flood(rows - 1, c)

    for r in range(rows):
        flood(r, 0)
        flood(r, cols - 1)

    # Pass 2: Count closed islands
    count = 0
    for r in range(1, rows - 1):
        for c in range(1, cols - 1):
            if grid[r][c] == 0:
                count += 1
                flood(r, c)

    return count
```

### Template 8: Path Signature (Shape Encoding) — LC 694
- **Description**: Encode the shape/structure of islands or subtrees using unique path signatures
- **Recognition**: "Distinct islands", "unique shapes", "count different structures", "same shape after translation"
- **Key Technique**: Record directional movements during DFS traversal to create a canonical signature
- **Examples**: LC 694, LC 711, LC 652

```java
// Java implementation with directional encoding
public int numDistinctIslands(int[][] grid) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) {
        return 0;
    }

    Set<String> uniqueIslandShapes = new HashSet<>();
    int rows = grid.length;
    int cols = grid[0].length;

    // Iterate through every cell in the grid
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            // Start DFS only on unvisited land cells
            if (grid[r][c] == 1) {
                StringBuilder pathSignature = new StringBuilder();
                // Start DFS from (r, c). 'S' marks the start
                dfs(grid, r, c, pathSignature, 'S');

                if (pathSignature.length() > 0) {
                    uniqueIslandShapes.add(pathSignature.toString());
                }
            }
        }
    }

    return uniqueIslandShapes.size();
}

/**
 * DFS with directional encoding
 * Records the direction taken to reach each cell
 * Uses 'O' delimiter when backtracking
 */
private void dfs(int[][] grid, int r, int c, StringBuilder path, char direction) {
    int rows = grid.length;
    int cols = grid[0].length;

    // Base cases: Out of bounds or water/visited
    if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
        return;
    }

    // 1. Mark as visited by setting to 0
    grid[r][c] = 0;

    // 2. Record the direction taken to reach this cell
    path.append(direction);

    // 3. Recurse in FIXED order (Down, Up, Right, Left)
    dfs(grid, r + 1, c, path, 'D');  // Down
    dfs(grid, r - 1, c, path, 'U');  // Up
    dfs(grid, r, c + 1, path, 'R');  // Right
    dfs(grid, r, c - 1, path, 'L');  // Left

    // 4. Add delimiter when backtracking
    // This distinguishes different branch structures
    path.append('O');
}
```

> **Two interchangeable encodings.** The Java block above records the **direction taken** into each
> cell (`D/U/R/L` + an `O` delimiter on the way back up); the Python block below records the
> **relative coordinate** `(r-r0, c-c0)` of each cell instead. Both are translation-invariant and
> rotation-sensitive — pick either, but never mix them inside one signature.

```python
def count_distinct_shapes(grid):
    """
    Count distinct island shapes using path signatures
    Key: Encode each island's shape as a unique string
    """
    if not grid or not grid[0]:
        return 0

    rows, cols = len(grid), len(grid[0])
    unique_shapes = set()

    def dfs(r, c, r0, c0, path):
        """
        DFS with path signature encoding
        r0, c0: Starting position for relative encoding
        path: StringBuilder to record the shape signature
        """
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != 1:
            return

        # Mark as visited
        grid[r][c] = 0

        # Encode relative position
        path.append(f"({r - r0},{c - c0})")

        # Visit neighbors in FIXED order (critical for consistency)
        dfs(r + 1, c, r0, c0, path)  # Down
        dfs(r - 1, c, r0, c0, path)  # Up
        dfs(r, c + 1, r0, c0, path)  # Right
        dfs(r, c - 1, r0, c0, path)  # Left

    # Iterate through grid in fixed order (top-left to bottom-right)
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                path = []
                dfs(r, c, r, c, path)  # Start with (r, c) as origin
                unique_shapes.add(tuple(path))

    return len(unique_shapes)
```

**Key Concepts for Path Signatures:**

1. **Canonical Traversal Order**
   - Always check neighbors in the same fixed sequence (e.g., D, U, R, L)
   - This ensures identical shapes produce identical signatures

2. **Starting Point Normalization**
   - Grid traversal in fixed order (top-to-bottom, left-to-right)
   - The first land cell encountered becomes the origin
   - All coordinates are relative to this origin

3. **Why Delimiters Matter**
   ```text
   Shape 1:  11      Shape 2:   1
              1                11

   Without delimiter: "SDRO"  vs "SDRO"  (Same - Wrong!)
   With delimiter:    "SDOO"  vs "SDRO"  (Different - Correct!)
   ```

4. **Consistency Guarantees**
   - Same shape → Same signature (always)
   - Different shapes → Different signatures
   - Translation invariant (position doesn't matter)
   - Rotation/reflection sensitive (as required)

### Template 9: Grid DFS + Backtracking — 3 Styles Compared (LC 1219 Path with Maximum Gold)

> **Problem**: In an `m x n` grid, collect the most gold on a single path. You may start/stop
> at any gold cell, move up/down/left/right, never revisit a cell, and never step on a `0` cell.
> Since a path can start anywhere, we launch a DFS from **every** gold cell.
> Because paths overlap across different start cells, we **backtrack** (restore the cell) after
> each DFS so the grid is clean for the next launch.
>
> Source: [`path-with-maximum-gold.py`](../../leetcode_python/Backtracking/path-with-maximum-gold.py)

All three versions are correct. They differ in **where** three decisions are made:
1. **Guard** — is the neighbor valid (in-bounds + gold + not visited)?
2. **Accumulate** — where does `cur_gold` get the current cell added?
3. **Update max** — where do we record `self.max_gold`?

#### Quick Comparison

| | **V0-1** — validate in child | **V0-2** — validate before call | **V0-3** — update max in loop |
|---|---|---|---|
| **Neighbor loop** | 4 explicit recursive calls | `for m in moves:` | `for m in moves:` |
| **Guard location** | **top of child** (base case) | **before** the recursive call | **before** the recursive call |
| **Accumulate `cur_gold`** | inside child (`+= grid[r][c]`) | at call site (`cur_gold + grid[..]`) | at call site (`cur_gold + grid[..]`) |
| **Start value passed** | `0` | `grid[start]` | `grid[start]` |
| **Update `max_gold`** | top of child (once per cell) | top of child (once per cell) | inside loop (per neighbor) — **needs seed** |
| **Extra recursive calls?** | Yes — invalid neighbors still call+return | No — only valid neighbors recurse | No — only valid neighbors recurse |
| **Handles isolated start cell?** | ✅ automatic | ✅ automatic | ⚠️ only via caller seed |
| **Verdict** | ✅ cleanest default | ✅ efficient, idiomatic | ⚠️ works, but fragile — avoid |

**Mental model of the difference:** V0-1 pushes the validity check *down* into the callee ("the
child decides if it should exist") — so the base case doubles as the guard. V0-2 / V0-3 pull it
*up* into the caller ("the parent only calls valid children") — so there is no wasted stack frame,
but the start cell must be validated separately (done by `if grid[y][x] > 0` in the launch loop).

#### V0-1 — Validate inside the child (recommended default)

```python
# python — LC 1219
# GUARD lives at the top of the child → doubles as the recursion base case.
# Cleanest to reason about: you may call dfs() on ANY coordinate (even off-grid);
# the child rejects itself. Cost: every invalid neighbor still spends one call frame.
class Solution:
    def getMaximumGold(self, grid):
        self.max_gold = 0
        rows, cols = len(grid), len(grid[0])
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] > 0:
                    self.dfs(grid, r, c, 0)   # start value = 0
        return self.max_gold

    def dfs(self, grid, r, c, cur_gold):
        rows, cols = len(grid), len(grid[0])
        # (1) GUARD: out of bounds OR empty(0) OR visited(-1) → stop
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] <= 0:
            return
        cache = grid[r][c]
        cur_gold += cache                                  # (2) ACCUMULATE here
        self.max_gold = max(self.max_gold, cur_gold)       # (3) UPDATE MAX per cell entry
        grid[r][c] = -1                                    # mark visited
        # recurse into ALL 4 dirs unconditionally — guard filters at the top
        self.dfs(grid, r + 1, c, cur_gold)
        self.dfs(grid, r - 1, c, cur_gold)
        self.dfs(grid, r, c + 1, cur_gold)
        self.dfs(grid, r, c - 1, cur_gold)
        grid[r][c] = cache                                 # BACKTRACK: restore
```

**When to use:** your **default** for grid DFS. Fewest ways to get it wrong — the start cell and
neighbors go through the *same* guard, so there is no special-casing. Prefer it when clarity matters
or when the start cell might itself be invalid.

#### V0-2 — Validate before the call, accumulate at the call site

```python
# python — LC 1219
# GUARD is inline BEFORE each recursive call → no wasted frames on invalid neighbors.
# The launch loop's `if grid[y][x] > 0` validates the START cell (child no longer does).
class Solution:
    def getMaximumGold(self, grid):
        self.max_gold = 0
        L, W = len(grid), len(grid[0])
        for y in range(L):
            for x in range(W):
                if grid[y][x] > 0:
                    self.dfs(grid, x, y, grid[y][x])   # start value = the cell itself
        return self.max_gold

    def dfs(self, grid, x, y, cur_gold):
        L, W = len(grid), len(grid[0])
        self.max_gold = max(self.max_gold, cur_gold)   # (3) UPDATE MAX per cell entry — safe
        cache = grid[y][x]
        grid[y][x] = -1                                # mark visited
        moves = [[-1, 0], [1, 0], [0, 1], [0, -1]]
        for dx, dy in moves:
            x_, y_ = x + dx, y + dy
            # (1) GUARD before recursing  +  (2) ACCUMULATE at the call site
            if 0 <= x_ < W and 0 <= y_ < L and grid[y_][x_] > 0:
                self.dfs(grid, x_, y_, cur_gold + grid[y_][x_])
        grid[y][x] = cache                             # BACKTRACK: restore
```

**When to use:** when you want the **efficient / idiomatic competitive** form — a `moves` array
scales cleanly to 8-direction or diagonal problems, and you skip the useless calls into walls.
Because `max_gold` is still updated at **entry** (before the loop), an isolated start cell is scored
correctly with no extra code. This is the version to reach for once you're comfortable.

#### V0-3 — Update max inside the loop (works, but fragile — avoid)

```python
# python — LC 1219
# Same structure as V0-2, BUT max_gold is updated INSIDE the loop (on `next_gold`),
# not at cell entry. Consequence: the entry cell is never scored by the DFS itself,
# so a lone gold cell with no gold neighbors would be missed → the launch loop must
# SEED max_gold with grid[y][x]. That extra dependency is exactly what makes it fragile.
class Solution:
    def getMaximumGold(self, grid):
        self.max_gold = 0
        L, W = len(grid), len(grid[0])
        for y in range(L):
            for x in range(W):
                if grid[y][x] > 0:
                    self.max_gold = max(self.max_gold, grid[y][x])  # ⚠️ REQUIRED seed
                    self.dfs(grid, x, y, grid[y][x])
        return self.max_gold

    def dfs(self, grid, x, y, cur_gold):
        L, W = len(grid), len(grid[0])
        cache = grid[y][x]
        grid[y][x] = -1                                # mark visited (once per frame)
        moves = [[-1, 0], [1, 0], [0, 1], [0, -1]]
        for dx, dy in moves:
            x_, y_ = x + dx, y + dy
            if 0 <= x_ < W and 0 <= y_ < L and grid[y_][x_] > 0:
                # NOTE: do NOT mark/unmark grid[y][x] here inside the loop.
                # Marking is per-cell-entry, not per-neighbor: the same cell is
                # explored by all 4 branches of THIS frame; re-marking each
                # iteration would corrupt the shared state.
                next_gold = cur_gold + grid[y_][x_]
                self.max_gold = max(self.max_gold, next_gold)   # (3) UPDATE MAX in loop
                self.dfs(grid, x_, y_, next_gold)
        grid[y][x] = cache                             # BACKTRACK: restore
```

**When to use:** effectively **never** as a first choice. It's included to show the trap: moving the
max-update into the loop makes the entry cell invisible to the DFS, forcing the caller-side seed.
Miss that one line and single-cell (or fully-isolated) inputs silently return `0`. Prefer V0-1 / V0-2.

#### Things to note (all versions)

- **Backtracking is mandatory here**, not optional. A path may start from many cells and paths
  overlap; restoring `grid[r][c] = cache` after the recursion lets later launches reuse the cell.
  Contrast with plain "count islands" (LC 200) where you mark-and-never-restore.
- **In-place visited marking** (`-1` / `0`) avoids an extra `visited` set — fine because we undo it.
  The guard treats *empty* and *visited* uniformly (`<= 0`), so no separate visited check is needed.
- **Mark/unmark exactly once per frame**, wrapping the neighbor exploration — never per neighbor.
- **Where you update max determines whether you need a seed**: update at *cell entry* (V0-1/V0-2) and
  every cell (including isolated ones) is counted for free; update *per neighbor* (V0-3) and you owe
  the caller a seed for the start cell.
- **Complexity** (all three): `time = O(4^k)` worst case where `k ≤ 25` is the number of gold cells
  (each cell branches into ≤3 unvisited neighbors after the first); `space = O(k)` recursion depth.

### Template 10: Weighted Graph DFS (Division/Ratio Queries) — LC 399
- **Description**: Build a weighted directed graph where edge weights represent ratios/division results, then DFS to compute transitive ratios between any two connected nodes
- **Recognition**: "Evaluate division", "exchange rates", "currency conversion", "ratio queries", "transitive relationships with weights"
- **Key Technique**: Model equations as a bidirectional weighted graph (`Map<String, Map<String, Double>>`), DFS with accumulated product along the path
- **Examples**: LC 399 (Evaluate Division), LC 1101 (The Earliest Moment When Everyone Become Friends - variant), LC 721 (Accounts Merge - graph grouping variant)
- **Core Algorithm Idea**:
  1. **Graph Construction**: For each equation `a / b = val`, add edge `a → b` with weight `val` and edge `b → a` with weight `1/val`
  2. **Query Processing**: For query `c / d`, DFS from `c` to `d`, multiplying edge weights along the path
  3. **Product Accumulation**: Pass a running product through DFS; when target is reached, the product is the answer
  4. **Alternative**: Union-Find with ratio tracking (store `node → root` ratio for O(α(n)) queries)
- **Important Notes**:
  - **Bidirectional Edges**: Always store both `a→b` and `b→a` with reciprocal weights
  - **Visited Set**: Reset per query to allow independent path exploration
  - **Early Termination**: If either node not in graph, return -1.0 immediately
  - **Self-Division**: If `start == end` and node exists in graph, return 1.0
  - **Product vs Additive**: Unlike shortest-path problems, this uses multiplicative accumulation
- **Similar Classic LC Problems**:
  - LC 399 - Evaluate Division (canonical weighted graph DFS)
  - LC 1976 - Number of Ways to Arrive at Destination (weighted graph traversal)
  - LC 787 - Cheapest Flights Within K Stops (weighted graph with constraints)
  - LC 743 - Network Delay Time (weighted graph exploration)
  - LC 1334 - Find the City With the Smallest Number of Neighbors at a Threshold Distance

```python
# 399 Evaluate Division
# there is also an "union find" solution
class Solution:
    def calcEquation(self, equations, values, queries):
        from collections import defaultdict
        # build graph
        graph = defaultdict(dict)
        for (x, y), v in zip(equations, values):
            graph[x][y] = v
            graph[y][x] = 1.0/v
        ans = [self.dfs(x, y, graph, set()) for (x, y) in queries]
        return ans

    def dfs(self, x, y, graph, visited):
        if not graph:
            return
        if x not in graph or y not in graph:
            return -1
        if x == y:
            return 1
        visited.add(x)
        for n in graph[x]:
            if n in visited:
                continue
            visited.add(n)
            d = self.dfs(n, y, graph, visited)
            if d > 0:
                return d * graph[x][n]
        return -1.0
```

```java
// java
// V1
// IDEA: DFS
// https://leetcode.com/problems/evaluate-division/solutions/3543256/image-explanation-easiest-concise-comple-okpu/
public double[] calcEquation_1(List<List<String>> equations, double[] values, List<List<String>> queries) {
    HashMap<String, HashMap<String, Double>> gr = buildGraph(equations, values);
    double[] finalAns = new double[queries.size()];

    for (int i = 0; i < queries.size(); i++) {
        String dividend = queries.get(i).get(0);
        String divisor = queries.get(i).get(1);

        /** NOTE !!!
         *
         *  either dividend nor divisor NOT in graph, return -1.0 directly
         */
        if (!gr.containsKey(dividend) || !gr.containsKey(divisor)) {
            finalAns[i] = -1.0;
        } else {

            /** NOTE !!!
             *
             *  we use `vis` to check if element already visited
             *  (to avoid repeat accessing)
             *  `vis` init again in every loop
             */

            HashSet<String> vis = new HashSet<>();
            /**
             *  NOTE !!!
             *
             *   we init `ans` and pass it to dfs method
             *   (but dfs method return NOTHING)
             *   -> `ans` is init, and pass into dfs,
             *   -> so `ans` value is updated during dfs recursion run
             *   -> and after dfs run completed, we get the result `ans` value
             */
            double[] ans = { -1.0 };
            double temp = 1.0;
            dfs(dividend, divisor, gr, vis, ans, temp);
            finalAns[i] = ans[0];
        }
    }

    return finalAns;
}

/** NOTE !!! below dfs method */
public void dfs(String node, String dest, HashMap<String, HashMap<String, Double>> gr, HashSet<String> vis,
                double[] ans, double temp) {

    /** NOTE !!! we use `vis` to check if element already visited */
    if (vis.contains(node))
        return;

    vis.add(node);
    if (node.equals(dest)) {
        ans[0] = temp;
        return;
    }

    for (Map.Entry<String, Double> entry : gr.get(node).entrySet()) {
        String ne = entry.getKey();
        double val = entry.getValue();
        /** NOTE !!! update temp as `temp * val` */
        dfs(ne, dest, gr, vis, ans, temp * val);
    }
}

public HashMap<String, HashMap<String, Double>> buildGraph(List<List<String>> equations, double[] values) {
    HashMap<String, HashMap<String, Double>> gr = new HashMap<>();

    for (int i = 0; i < equations.size(); i++) {
        String dividend = equations.get(i).get(0);
        String divisor = equations.get(i).get(1);
        double value = values[i];

        gr.putIfAbsent(dividend, new HashMap<>());
        gr.putIfAbsent(divisor, new HashMap<>());

        gr.get(dividend).put(divisor, value);
        gr.get(divisor).put(dividend, 1.0 / value);
    }

    return gr;
}
```

## Summary & Quick Reference

### Decision Flowchart
```text
DFS Problem Analysis Flowchart:

1. Is it a tree/graph traversal problem?
   ├── YES → Check structure type
   │   ├── Tree? → Use Tree Templates (1, 3, 5, 6)
   │   │   ├── Need specific order? → Template 1 (Traversal)
   │   │   ├── Need paths? → Template 3 (Path Finding)
   │   │   ├── Need to modify? → Template 5 (Modification)
   │   │   └── Need subtree info? → Template 6 (Bottom-up)
   │   └── Graph? → Use Graph Template (2)
   │       ├── Has cycles? → Add visited set
   │       ├── Need all paths? → Track path
   │       └── Multi-source? → Start from all sources
   └── NO → Continue to 2

2. Is it a combinatorial problem?
   ├── YES → Use Backtracking Template (4)
   │   ├── Permutations? → Swap elements
   │   ├── Combinations? → Start index
   │   ├── Subsets? → Include/exclude
   │   └── Constraint satisfaction? → Check validity
   └── NO → Continue to 3

3. Does it require exploring all possibilities?
   ├── YES → Use DFS with appropriate state tracking
   │   ├── Grid problem? → 4-directional DFS
   │   ├── String problem? → Index-based DFS
   │   └── Decision tree? → Choice-based DFS
   └── NO → Consider different algorithm

4. Special considerations:
   ├── Need shortest path? → Consider BFS instead
   ├── Has optimal substructure? → Consider DP
   └── Need all solutions? → DFS with backtracking
```

### Problem-Solving Steps
1. **Identify pattern**: Tree, graph, backtracking, or path
2. **Choose template**: Select appropriate DFS template
3. **Track state**: Visited set, path list, or global variable
4. **Handle base cases**: Null nodes, boundaries, target found
5. **Test edge cases**: Empty input, single node, cycles

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Forgetting visited set**: Infinite loops in graphs
- **Not backtracking**: Incorrect paths in combinatorial problems
- **Wrong traversal order**: Using preorder when postorder needed
- **Modifying while traversing**: Can break iteration
- **Not handling null**: NullPointerException
- **⚠️ CRITICAL: Not returning immediately when path found**: When searching for a path in DFS, must return true immediately when found (see detailed explanation below)

**✅ Best Practices:**
- **Use visited set for graphs**: Prevent cycles
- **Clone paths**: `path[:]` when storing results
- **Check boundaries first**: In grid problems
- **Use meaningful names**: `visited` not `v`
- **Consider iterative**: For deep recursion

### Interview Tips
1. **Clarify problem type**: Tree or graph? Cycles possible?
2. **State approach**: "I'll use DFS because..."
3. **Discuss complexity**: Time and space analysis
4. **Handle edge cases**: Empty, single element, cycles
5. **Optimize if needed**: Memoization, pruning

### Pro Tips for Pattern Selection

- **Two-pass problems**: If you need to eliminate something first (boundary, edges), use Template 7
- **Shape comparison**: If comparing structures/shapes, use Template 8 (Path Signatures)
- **Bottom-up aggregation**: If answer depends on processing children first, use Template 6
- **Try all possibilities**: If problem asks for "all" solutions/combinations, use Template 4 (Backtracking)
- **Overlapping paths from many starts**: mark, recurse, then **restore** — Template 9
- **Anything that does not fit**: check [dfs_advanced.md](./dfs_advanced.md) before inventing a pattern

### Related Topics
- **[bfs.md](./bfs.md)**: when the shortest path is needed
- **[dp.md](./dp.md)**: overlapping subproblems — memoize the DFS
- **[backtrack.md](./backtrack.md)**: DFS for combinations, with undo
- **[union_find.md](./union_find.md)**: alternative for connectivity
- **[topology_sorting.md](./topology_sorting.md)**: DFS application for dependencies
- **[dfs_advanced.md](./dfs_advanced.md)**: the rare templates split out of this sheet
- **[dfs_examples.md](./dfs_examples.md)**: worked solutions and the full problem index

---
**Must-Know Problems for Interviews**: LC 94, 104, 112, 113, 124, 200, 236, 297, 399, 694
**Advanced Problems**: LC 124, 297, 329, 472, 652, 694, 711
**Path Signature Pattern**: LC 694 (Distinct Islands), LC 711 (Distinct Islands II), LC 652 (Find Duplicate Subtrees)
