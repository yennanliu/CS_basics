# DFS (Depth-First Search)

> **Scope** — The main DFS reference: the ten core depth-first templates — tree traversal, grid flood fill, path finding, backtracking, tree modification, post-order aggregation, boundary elimination, shape signatures and weighted-edge traversal — with the recognition table that picks between them.
> **See also** — *deep dives split out of this file*: [dfs_advanced.md](./dfs_advanced.md) — two-grid validation, edge-direction flags, cross-component pair counting, trie + wildcard DFS, depth-indexed stack DFS, distance-bucket leaf pairing, N-ary and `parent[]` rollups; [graph_advanced.md](./graph_advanced.md) — Euler paths (Hierholzer) and Tarjan low-link; [dfs_examples.md](./dfs_examples.md) — the classic problems, grouped by this sheet's templates, with an index.
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

## Recognition

Each pattern below is presented **exactly once**, as a card under Templates & Algorithms. Match the
wording in the statement, check the constraint against the
[constraint ruler](./complexity_cheatsheet.md#6-interview-decision-guide), then read the last column
before you code.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "inorder / preorder / postorder", "visit all", "serialize", "is it a mirror", "are two trees the same" | nodes ≤ 10⁴ | [Template 1](#template-1-tree-traversal--lc-94-) — tree traversal; two trees → its [paired-DFS variation](#symmetric-tree--lc-101) | level by level → [bfs.md § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) |
| "number of islands", "connected regions", "flood fill", "clone the graph" | grid ≤ 300 × 300 | [Template 2](#template-2-graph--grid-dfs-flood-fill--lc-200-) — flood fill, count launches | the shortest route between two cells → [bfs.md § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
| "root-to-leaf path with sum", "does a path exist", "all paths" | nodes ≤ 5,000 | [Template 3](#template-3-path-finding--lc-112-) — state passed down, early return | the path may start below the root → prefix sum on the path (LC 437) |
| "all permutations / combinations / subsets" | n ≤ 10 (n!) or n ≤ 20 (2ⁿ) | [Template 4](#template-4-backtracking--lc-46) — choose, recurse, un-choose | only the count, with repeated states → DP |
| "delete / insert / trim a node and return the root" | BST, O(h) | [Template 5](#template-5-tree-modification--lc-450) — reassign the returned subtree | a property of a whole subtree decides the cut → Template 6 |
| "diameter", "balanced?", "subtree sum", "lowest common ancestor", "minimum moves between adjacent nodes" | nodes ≤ 10⁴ | [Template 6](#template-6-bottom-up-post-order-dfs--lc-543-) — post-order, return one value, record another | the answer depends on the path from the root → Template 3 |
| "closed islands", "surrounded regions", "enclaves" | grid ≤ 200 × 200 | [Template 7](#template-7-2-pass-dfs-boundary-elimination--lc-1254) — flood the border first | the distance to the border → multi-source BFS |
| "distinct islands", "same shape after translation" | grid ≤ 50 × 50 | [Template 8](#template-8-path-signature-shape-encoding--lc-694) — path signature | rotations and reflections count as equal → normalise over 8 transforms (LC 711) |
| "one path, collect the most, never revisit a cell", "word search" | grid ≤ 15 × 15, ≤ 25 useful cells | [Template 9](#template-9-grid-dfs-with-restore-backtracking--lc-1219) — mark, recurse, **restore** | cells may be revisited, or the grid is large → DP or BFS |
| "evaluate division", "exchange rates", "transitive ratios" | variables ≤ 40 | [Template 10](#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) — weighted DFS, multiply along the path | many queries on a fixed graph → weighted union-find |

**Not on this sheet** — these live in [dfs_advanced.md](./dfs_advanced.md): two-grid validation (LC 1905),
edge-direction tracking (LC 1466), component pair counting (LC 2316), trie + wildcard DFS (LC 211,
676), depth-indexed stack DFS (LC 388, 1233), distance-bucket leaf pairing (LC 1530), N-ary
post-order rollup (LC 3965) and `parent[]`-array depth climbs (LC 4015). Euler paths (LC 332, 753)
and Tarjan bridges (LC 1192) are in [graph_advanced.md](./graph_advanced.md); tree ⟷ string codecs
(LC 606, 536) in [tree_codec.md](./tree_codec.md). The problems, grouped by template, are
in [dfs_examples.md → Index](./dfs_examples.md#index).

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

### Template 1: Tree Traversal — LC 94 ⭐⭐⭐⭐⭐

**Recognise** — "inorder / preorder / postorder traversal", "visit every node", "serialize", "print
the tree", and in a BST "in sorted order" (a BST's inorder is sorted). Nodes ≤ 10⁴; the recursion is
as deep as the tree is tall.

**Key point** — the three orders are one recursion with the *visit* line in a different place:
before the children (pre), between them (in), after them (post). Each node is entered once,
whichever order you pick.

```python
# python
# LC 94 - Binary Tree Inorder Traversal
# IDEA: recurse left, visit, recurse right; move `result.append` to get pre- or post-order
# time = O(n), space = O(h)   h = tree height, the recursion depth
def inorderTraversal(root):
    result = []

    def dfs(node):
        if not node:
            return
        dfs(node.left)
        result.append(node.val)          # inorder: between the two children
        dfs(node.right)

    dfs(root)
    return result


def inorderTraversal_iterative(root):
    # the problem's follow-up: an explicit stack replaces the call stack
    result, stack = [], []
    node = root
    while node or stack:
        while node:                      # walk as far left as possible
            stack.append(node)
            node = node.left
        node = stack.pop()               # the leftmost node not yet visited
        result.append(node.val)
        node = node.right                # then its right subtree
    return result
```

```java
// java
// LC 94 - Binary Tree Inorder Traversal
// IDEA: recurse left, visit, recurse right; move `result.add` to get pre- or post-order
// time = O(n), space = O(h)
public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    dfs(root, result);
    return result;
}

private void dfs(TreeNode node, List<Integer> result) {
    if (node == null) {
        return;
    }
    dfs(node.left, result);
    result.add(node.val);                // inorder: between the two children
    dfs(node.right, result);
}
```

**Complexity**
- **Time O(n)**: `dfs` runs once per node and once per empty child (n + 1 of them), each in O(1).
- **Space O(h)**: the recursion (or `stack`) holds one root-to-node path — O(log n) when balanced,
  O(n) for a chain.

**Traps**
- `return inorder(node.left) + [node.val] + inorder(node.right)` copies lists at every level: O(n·h),
  which is O(n²) on a skewed tree. Append to one shared list.
- Iterative **preorder** pushes the right child before the left, so the left pops first; pushing
  left first returns `[1,3,2]` for `[1,2,3]`.
- A 10⁵-deep chain overflows Python's recursion limit; the iterative form has no limit.

**Not this if** — the order is level by level → BFS
([bfs.md § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-)). A node's answer is computed
from its children's answers → [Template 6](#template-6-bottom-up-post-order-dfs--lc-543-). The
predicate needs two nodes at once → the paired-DFS variation below.

**Drill** — LC 94 · LC 144 · LC 145 *(iterative postorder = a reversed root-right-left preorder)* ·
LC 100 ⭐ *(paired DFS)* · LC 101 *(paired DFS, mirrored: below)* · LC 297 ⭐ *(preorder with null
markers)*

#### Symmetric Tree — LC 101

> **Key point** — a tree is symmetric iff its two subtrees are **mirrors** of each other, so the
> DFS carries **two cursors** `(a, b)` and checks a relation between them: equal values, then the
> **outer** pair `(a.left, b.right)` and the **inner** pair `(a.right, b.left)`. The pairing you
> recurse on *is* the problem: the straight pairing `(a.left, b.left)` + `(a.right, b.right)` is
> LC 100 Same Tree.

**Reduces to**: [Template 1](#template-1-tree-traversal--lc-94-) — a pre-order walk over a
*pair* of nodes instead of one. Source:
[`symmetric-tree.py`](../../leetcode_python/Stack/symmetric-tree.py).

The launch is `helper(root.left, root.right)`; the root itself is never compared to anything. Three
base cases, in this order: both `None` → `True`; exactly one `None` → `False`; values differ →
`False`. Only then descend, and let `and` short-circuit on the first mismatch.

```text
LC 101 trace — root = [1,2,2,null,3,null,3]     helper(a, b) pairs  a.left <-> b.right
                                                                   a.right <-> b.left
        1               helper(a=2, b=2)                  vals equal -> descend
      /   \             |- helper(a.left=null, b.right=3)  exactly one null -> FALSE
    2       2           |- (never evaluated: `and` short-circuits)
     \       \
      3       3         answer = False
   a.right  b.right     <- both 3's are RIGHT children, so they are not mirror partners
```

**Traps** — recursing on the *straight* pairing does not "almost work": it answers a different
question ("is the left subtree equal to the right subtree?") and is wrong in **both** directions:

```text
straight pairing (a.left<->b.left, a.right<->b.right) on the same two inputs:

  [1,2,2,null,3,null,3]   null==null, then 3==3  -> True    (correct answer: False)
  [1,2,2,3,4,4,3]         3 vs 4                 -> False   (correct answer: True)
```

```python
# python
# LC 101 - Symmetric Tree
# IDEA: paired (dual-pointer) DFS; recurse on the MIRROR pairing (a.left, b.right) / (a.right, b.left)
# time = O(n), space = O(h)   # h = tree height, worst O(n)
class Solution:
    def isSymmetric(self, root):
        if not root:
            return True
        # the helper takes TWO nodes -> the root is never compared to anything
        return self.helper(root.left, root.right)

    def helper(self, left, right):
        if not left and not right:      # both empty -> mirrored
            return True
        if not left or not right:       # exactly one empty -> not mirrored
            return False
        if left.val != right.val:
            return False
        return (
            self.helper(left.left, right.right)     # outer pair
            and
            self.helper(left.right, right.left)     # inner pair
        )
```

```java
// java
// LC 101 - Symmetric Tree
// IDEA: paired (dual-pointer) DFS; recurse on the MIRROR pairing
// time = O(n), space = O(h)
public boolean isSymmetric(TreeNode root) {
    if (root == null) {
        return true;
    }
    return helper(root.left, root.right);
}

private boolean helper(TreeNode left, TreeNode right) {
    if (left == null && right == null) {
        return true;
    }
    if (left == null || right == null) {
        return false;
    }
    if (left.val != right.val) {
        return false;
    }
    return helper(left.left, right.right)      // outer pair
            && helper(left.right, right.left); // inner pair
}
```

##### Iterative form — the pair lives on the stack

The problem's own follow-up asks for it, and it is the general way to de-recurse a paired DFS: the
stack holds **pairs**, pushed and popped two entries at a time.

```python
# python
# LC 101 - Symmetric Tree (iterative — the stated follow-up)
# IDEA: same mirror pairing, kept on an explicit stack; pop two entries = pop one pair
# time = O(n), space = O(n)
def isSymmetric(root):
    if not root:
        return True
    stack = [root.left, root.right]
    while stack:
        p, q = stack.pop(), stack.pop()          # pops ONE pair
        if not p and not q:
            continue
        if not p or not q or p.val != q.val:
            return False
        # push the two mirror pairs
        stack.append(p.left)
        stack.append(q.right)                    # outer pair
        stack.append(p.right)
        stack.append(q.left)                     # inner pair
    return True
```

- **Push `None`s on purpose.** Unlike the iterative traversal in Template 1, you may **not** skip a
  null child: a `(None, node)` pair has to be *popped and compared* to return `False`. Guarding the
  pushes with `if p.left:` silently accepts asymmetric trees.
- **The pop reverses the pair** — `stack.pop(), stack.pop()` hands back `(q.left, p.right)`, i.e. the
  second-pushed entry first. Harmless, because the mirror predicate is symmetric in its two
  arguments; it is *not* harmless in a paired DFS whose predicate is directional (e.g. "is `b` a
  subtree of `a`"), where you must pop into the right slots.
- **A pair queue works identically** — swap the stack for a `deque` and `popleft()` twice; the order of
  comparison changes, the answer does not. See the LC 101 row in [bfs.md](./bfs.md).

**Similar**:
  - LC 100 - Same Tree (the same skeleton on the *straight* pairing)
  - LC 951 - Flip Equivalent Binary Trees (try **both** pairings and `or` them)
  - LC 572 - Subtree of Another Tree (LC 100's paired DFS relaunched at every node)
  - LC 617 - Merge Two Binary Trees (paired DFS that **builds** a node instead of returning a bool)
  - LC 1612 - Check If Two Expression Trees are Equivalent (paired walk + leaf multiset compare)
  - LC 226 - Invert Binary Tree (the mirror as a *transformation* — symmetric is
    `isSameTree(root, invert(root))`, at the cost of mutating the tree)
  - LC 872 - Leaf-Similar Trees (deliberate contrast: two **independent** DFS runs whose leaf
    sequences are compared afterwards, because the trees' shapes are allowed to differ)

### Template 2: Graph / Grid DFS (Flood Fill) — LC 200 ⭐⭐⭐⭐⭐

**Recognise** — "number of islands", "connected regions", "flood fill", "max area of an island",
"clone the graph". A grid ≤ 300 × 300, or V + E ≤ 10⁵ → one O(V + E) pass.

**Key point** — the outer scan meets each island first at one cell, and that cell's DFS sinks every
land cell connected to it. So each island is counted exactly once, at its first cell, and every
cell is entered a constant number of times.

```python
# python
# LC 200 - Number of Islands
# IDEA: scan the grid; each '1' still standing starts a DFS that sinks its island -> count the launches
# time = O(m * n), space = O(m * n)   the recursion is as deep as the largest island
def numIslands(grid):
    rows, cols = len(grid), len(grid[0])

    def sink(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != '1':
            return                        # off the grid, water, or already sunk
        grid[r][c] = '0'                  # mark BEFORE recursing, so no cell is entered twice
        sink(r + 1, c)
        sink(r - 1, c)
        sink(r, c + 1)
        sink(r, c - 1)

    islands = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == '1':
                islands += 1              # the first cell of a new island
                sink(r, c)
    return islands
```

```java
// java
// LC 200 - Number of Islands
// IDEA: scan the grid; each '1' still standing starts a DFS that sinks its island -> count the launches
// time = O(m * n), space = O(m * n)
public int numIslands(char[][] grid) {
    int islands = 0;
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] == '1') {
                islands++;                // the first cell of a new island
                sink(grid, r, c);
            }
        }
    }
    return islands;
}

private void sink(char[][] grid, int r, int c) {
    if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != '1') {
        return;
    }
    grid[r][c] = '0';                     // mark BEFORE recursing
    sink(grid, r + 1, c);
    sink(grid, r - 1, c);
    sink(grid, r, c + 1);
    sink(grid, r, c - 1);
}
```

**Complexity**
- **Time O(m·n)**: the outer scan reads every cell once, and `sink` turns a `'1'` into `'0'` before
  recursing, so it enters each land cell once; the four neighbour checks are O(1).
- **Space O(m·n)**: an all-land grid is one island, and the recursion can be that deep.

**Traps**
- Marking after the four recursive calls: two land cells recurse into each other for ever —
  `[["1","1"]]` overflows the stack.
- LC 200's grid holds the **characters** `'1'` and `'0'`; comparing with the integer `1` finds no
  islands at all.
- 300 × 300 of land is a 9·10⁴-deep recursion: Python needs `sys.setrecursionlimit`, or the BFS form
  ([bfs.md § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-)).
- Sinking the grid destroys the input. Say so in the interview, or keep a `visited` set.

**Not this if** — a directed graph and "is there a cycle" → three-state DFS
([graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-)); a plain
`visited` set reports false cycles. The shortest route between two cells → BFS. Edges arrive one at
a time → union-find.

**Drill** — LC 200 ⭐ · LC 733 *(flood one region)* · LC 695 *(return the size from `sink`)* ·
LC 133 ⭐ *(visited is the `{original: copy}` map)* · LC 417 ⭐ *(flood inward from each ocean,
then intersect)* · LC 419 *(no DFS at all: the variation below)*

#### Battleships in a Board — LC 419

> **Key point** — every ship is a straight 1×k or k×1 line, so each one has exactly one **top-left
> end**: an `'X'` with no `'X'` above it and none to its left. Count those cells and you have
> counted the ships — one pass, O(1) extra space, no `visited` set, the board untouched.

**Reduces to**: [Template 2](#template-2-graph--grid-dfs-flood-fill--lc-200-) — counting
components, where the straight-line guarantee lets you count each component's first cell instead
of flooding it. That is the stated follow-up (*one pass, O(1) space, without modifying the board*)
on top of the LC 200 flood fill.

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

**Traps**
- The trick depends on the guarantee. Drop "ships are straight lines" and an L-shaped ship has two
  top-left-looking cells; fall back to the LC 200 flood fill above.

### Template 3: Path Finding — LC 112 ⭐⭐⭐⭐⭐

**Recognise** — "root-to-leaf path with sum", "does a path exist", "all paths", "a path from a to
b". Nodes ≤ 5,000.

**Key point** — the path state (here the sum still needed) is passed **down** as an argument, so each
call sees exactly the path from the root to itself. The answer is checked only at a leaf, and a
True from either child is returned at once.

```python
# python
# LC 112 - Path Sum
# IDEA: pass the remaining sum down; at a leaf it must be exactly 0; return True as soon as a child does
# time = O(n), space = O(h)
def hasPathSum(root, targetSum):
    if not root:
        return False
    remaining = targetSum - root.val
    if not root.left and not root.right:      # a leaf: the path ends here
        return remaining == 0
    return hasPathSum(root.left, remaining) or hasPathSum(root.right, remaining)
```

```java
// java
// LC 112 - Path Sum
// IDEA: pass the remaining sum down; at a leaf it must be exactly 0; return true as soon as a child does
// time = O(n), space = O(h)
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }
    int remaining = targetSum - root.val;
    if (root.left == null && root.right == null) {   // a leaf: the path ends here
        return remaining == 0;
    }
    return hasPathSum(root.left, remaining) || hasPathSum(root.right, remaining);
}
```

**Complexity**
- **Time O(n)**: each node is visited at most once, and `or` stops at the first True.
- **Space O(h)**: one call per node on the current root-to-node path.

**Traps**
- Checking `remaining == 0` at an empty child instead of at a leaf: `[1,2]` with target 1 returns
  True through the missing right child of 1, though the only root-to-leaf path sums to 3.
- An empty tree has no path, even for target 0: `hasPathSum(None, 0)` is False.
- In a graph search, calling `dfs(next)` without returning its result: the True is found and then
  thrown away (the early-return section below).

**Not this if** — the path may start and end anywhere going down → prefix sums along the path
(LC 437). The **shortest** path is wanted → BFS. The path may turn at a node (max path sum) →
[Template 6](#template-6-bottom-up-post-order-dfs--lc-543-).

**Drill** — LC 112 · LC 257 *(collect the paths as strings)* · LC 113 *(collect the paths with the
sum: below)* · LC 1971 *(in a graph: return early)* · LC 437 *(any downward path: prefix sums)*

For the full family of tree path problems (max path, consecutive sequences, the prefix-sum
technique), see [tree_lca_distance.md § 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113).

#### Variation: collect every path (push, recurse, pop)

```python
# python
# LC 113 - Path Sum II
# IDEA: carry the path down; copy it at a matching leaf; pop on the way back up
# time = O(n * h), space = O(h)   copying a path of length h at each matching leaf
def pathSum(root, targetSum):
    result, path = [], []

    def dfs(node, remaining):
        if not node:
            return
        path.append(node.val)
        remaining -= node.val
        if not node.left and not node.right and remaining == 0:
            result.append(path[:])               # a copy: path keeps changing
        dfs(node.left, remaining)
        dfs(node.right, remaining)
        path.pop()                               # undo before returning to the parent

    dfs(root, targetSum)
    return result
```

#### DFS Early Return Pattern — return TRUE eagerly, FALSE lazily

When a DFS looks for **one** path, a child's `true` must be returned the moment it arrives. Ignoring
it keeps searching, and the function still falls through to the `return false` at its end.

```java
// java
// IDEA: WRONG — the child's result is ignored, so a path that was found is thrown away
// time = O(V + E), space = O(V)
private boolean dfsPathVisitor(int node, int destination, Map<Integer, List<Integer>> map, boolean[] visited) {
    if (node == destination) return true;
    visited[node] = true;
    for (int next : map.get(node)) {
        if (!visited[next]) {
            dfsPathVisitor(next, destination, map, visited);   // result dropped
        }
    }
    return false;   // false for every node except the destination itself
}
```

```java
// java
// IDEA: RIGHT — return true as soon as any child reaches the destination
// time = O(V + E), space = O(V)
private boolean dfsPathVisitor(int node, int destination, Map<Integer, List<Integer>> map, boolean[] visited) {
    if (node == destination) return true;
    visited[node] = true;
    for (int next : map.get(node)) {
        if (!visited[next] && dfsPathVisitor(next, destination, map, visited)) {
            return true;    // found one path - that is enough
        }
    }
    return false;           // only after every neighbour has failed
}
```

```text
Graph: 0 -- 1 -- 2 -- 3        find a path 0 -> 3
       |         |
       4 ------- 5

dfs(0) -> dfs(1) -> dfs(2) -> dfs(3) returns TRUE
WRONG: dfs(2) ignores it, tries 5 (a dead end), returns FALSE; so do dfs(1) and dfs(0)
RIGHT: dfs(2), dfs(1) and dfs(0) each return TRUE at once; 5 and 4 are never tried
```

| | Ignore the child's result | Return on `true` |
|---|---|---|
| Correctness | false even when a path exists | true when one is found |
| Work | explores every branch | stops at the first path |
| Use for | collecting or counting **all** results (LC 257, 113, 22) | existence (LC 1971, 79, 112) |

### Template 4: Backtracking — LC 46

**Recognise** — "all permutations / combinations / subsets", "generate every valid …", "return all
ways". n ≤ 10 (n! ≈ 3.6·10⁶) or n ≤ 20 (2ⁿ ≈ 10⁶) → an exponential search is the intended answer.

**Key point** — `path` always holds the choices on the way from the root of the decision tree to the
current call, and every choice is undone (`pop`, `used[i] = False`) before the next one is tried, so
sibling branches all start from the same state.

```python
# python
# LC 46 - Permutations
# IDEA: choose an unused number, recurse, un-choose; a full-length path is one permutation
# time = O(n * n!), space = O(n)   n! leaves, each copied in O(n); output not counted
def permute(nums):
    result, path = [], []
    used = [False] * len(nums)

    def backtrack():
        if len(path) == len(nums):
            result.append(path[:])            # copy: path is undone afterwards
            return
        for i in range(len(nums)):
            if used[i]:
                continue
            used[i] = True                    # choose
            path.append(nums[i])
            backtrack()
            path.pop()                        # un-choose, so the next i starts clean
            used[i] = False

    backtrack()
    return result
```

```java
// java
// LC 46 - Permutations
// IDEA: choose an unused number, recurse, un-choose; a full-length path is one permutation
// time = O(n * n!), space = O(n)
public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, new boolean[nums.length], new ArrayList<>(), result);
    return result;
}

private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
    if (path.size() == nums.length) {
        result.add(new ArrayList<>(path));    // copy: path is undone afterwards
        return;
    }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) {
            continue;
        }
        used[i] = true;                       // choose
        path.add(nums[i]);
        backtrack(nums, used, path, result);
        path.remove(path.size() - 1);         // un-choose
        used[i] = false;
    }
}
```

**Complexity**
- **Time O(n · n!)**: the decision tree has n! leaves, each copied in O(n); the internal nodes
  above them add no more than that.
- **Space O(n)**: `path`, `used` and the recursion depth, not counting the output.

**Traps**
- `result.append(path)` stores the same list n! times, and it is empty once the search has unwound:
  `[1,2]` returns `[[], []]`.
- Combinations (LC 39, LC 77) pass a `start` index so that `[1,2]` and `[2,1]` are not both produced;
  permutations loop from 0 and skip `used`. Swapping the two gives duplicates or missing orders.
- Duplicates in the input (LC 47): sort, then skip `nums[i] == nums[i-1]` while `nums[i-1]` is unused.

**Not this if** — only the **number** of arrangements is asked and sub-states repeat → DP. The full
family (subsets, combination sum, N-Queens, pruning) is in [backtrack.md](./backtrack.md).

**Drill** — LC 46 ⭐ · LC 78 ⭐ *(subsets: record every node, not only leaves)* · LC 39 ⭐ *(start
index, reuse allowed)* · LC 17 ⭐ *(one digit per level)* · LC 51 *(prune with column and diagonal
sets)*

### Template 5: Tree Modification — LC 450

**Recognise** — "delete / insert / trim / prune a node", "return the root of the modified tree",
"convert the tree". Nodes ≤ 10⁴; in a BST the work is O(h).

**Key point** — the function returns the (possibly new) root of the subtree it was given, and every
caller writes that back: `node.left = f(node.left)`. A deleted or replaced node is then unlinked by
its own parent's assignment, with no parent pointers and no special case for the root.

```python
# python
# LC 450 - Delete Node in a BST
# IDEA: return the new subtree root and let the parent reassign it; two children -> copy the successor in
# time = O(h), space = O(h)
def deleteNode(root, key):
    if not root:
        return None
    if key < root.val:
        root.left = deleteNode(root.left, key)     # reassign: that subtree's root may change
    elif key > root.val:
        root.right = deleteNode(root.right, key)
    else:
        if not root.left:
            return root.right                      # 0 or 1 child: lift the other side
        if not root.right:
            return root.left
        successor = root.right                     # 2 children: smallest node on the right
        while successor.left:
            successor = successor.left
        root.val = successor.val
        root.right = deleteNode(root.right, successor.val)
    return root
```

```java
// java
// LC 450 - Delete Node in a BST
// IDEA: return the new subtree root and let the parent reassign it; two children -> copy the successor in
// time = O(h), space = O(h)
public TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) {
        return null;
    }
    if (key < root.val) {
        root.left = deleteNode(root.left, key);    // reassign: that subtree's root may change
    } else if (key > root.val) {
        root.right = deleteNode(root.right, key);
    } else {
        if (root.left == null) {
            return root.right;
        }
        if (root.right == null) {
            return root.left;
        }
        TreeNode successor = root.right;
        while (successor.left != null) {
            successor = successor.left;
        }
        root.val = successor.val;
        root.right = deleteNode(root.right, successor.val);
    }
    return root;
}
```

**Complexity**
- **Time O(h)**: one root-to-node walk, plus one more down the right subtree to the successor —
  O(log n) balanced, O(n) for a chain.
- **Space O(h)**: the recursion follows that same path.

**Traps**
- Calling `deleteNode(root.left, key)` without assigning the result: the parent keeps pointing at the
  old child, and `[5,3,6]` with key 3 still contains 3.
- Deleting the root: the caller must use the returned value. `[5]` with key 5 returns `None`.
- After copying the successor's value up, delete **the successor's value** from the right subtree,
  not `key` — `key` is no longer in the tree.

**Not this if** — the tree is not a BST → search both sides, O(n). The cut depends on a property of a
whole subtree ("remove subtrees with no 1", LC 814) → post-order
([Template 6](#template-6-bottom-up-post-order-dfs--lc-543-)), still returning the pruned subtree.

**Drill** — LC 450 · LC 226 ⭐ *(invert: reassign swapped children)* · LC 701 *(insert: the same
reassignment)* · LC 669 *(trim to a range)* · LC 814 *(prune post-order)*

### Template 6: Bottom-up (Post-Order) DFS — LC 543 ⭐⭐⭐⭐⭐

**Recognise** — "diameter", "height", "is it balanced", "subtree sum", "lowest common ancestor",
"smallest subtree containing …", "minimum moves between adjacent nodes": a node's answer needs its
children's answers first. Nodes ≤ 10⁴.

**Key point** — each call returns **one** value to its parent (here the height), while the answer the
problem asks for (here `left + right`, the longest path that turns at this node) is a **different**
value, recorded in an outer variable and never returned.

```python
# python
# LC 543 - Diameter of Binary Tree
# IDEA: post-order; return the height upward, record left + right (the path through this node) globally
# time = O(n), space = O(h)
def diameterOfBinaryTree(root):
    best = 0

    def height(node):
        nonlocal best
        if not node:
            return 0
        left = height(node.left)
        right = height(node.right)
        best = max(best, left + right)        # edges on the longest path that turns here
        return 1 + max(left, right)           # what the parent needs: one side only

    height(root)
    return best
```

```java
// java
// LC 543 - Diameter of Binary Tree
// IDEA: post-order; return the height upward, record left + right (the path through this node) globally
// time = O(n), space = O(h)
private int best = 0;

public int diameterOfBinaryTree(TreeNode root) {
    best = 0;
    height(root);
    return best;
}

private int height(TreeNode node) {
    if (node == null) {
        return 0;
    }
    int left = height(node.left);
    int right = height(node.right);
    best = Math.max(best, left + right);      // edges on the longest path that turns here
    return 1 + Math.max(left, right);         // what the parent needs: one side only
}
```

**Complexity**
- **Time O(n)**: `height` runs once per node and does O(1) work after its two children return.
- **Space O(h)**: the recursion depth.

**Dry run**

```text
root = [1,2,3,4,5]          1
                           / \
                          2   3
                         / \
                        4   5
height(4) = 1, height(5) = 1     at 2: best = 1 + 1 = 2, return 2
height(3) = 1                    at 1: best = max(2, 2 + 1) = 3, return 3
answer 3   (the path 4-2-1-3 has 3 edges)
```

**Traps**
- Returning `left + right` upward instead of `1 + max(left, right)`: the parent then joins two forks
  into a shape that is not a path. LC 124 below is the same trap with values.
- Counting nodes instead of edges: LC 543 wants edges, so `[1,2]` is 1, not 2.
- Calling a separate `height(node.left)` from every ancestor (top-down) is O(n²) on a chain; one
  post-order pass returns each height once.

**Not this if** — the value depends on the path from the root (depth, sum so far) → pass it **down**
([Template 3](#template-3-path-finding--lc-112-)). The lowest common ancestor of two given nodes has
its own sheet: [tree_lca_distance.md](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236).

**Drill** — LC 543 ⭐ · LC 104 ⭐ · LC 110 ⭐ *(return -1 for "unbalanced")* · LC 236 *(return which
targets a subtree holds)* · LC 979 *(a signed balance: below)* · LC 124 ⭐ *(LC 543 with values
and a clamp: below)*

#### Variation: lowest common ancestor, found bottom-up

LCA is post-order in disguise: the answer is the first node, on the way up, that sees targets on
**both** sides.

- **Standard LCA (LC 236)**: each call returns the target it found in its subtree, or `None`; the
  first node whose left and right calls are both non-empty is the LCA.
- **Depth-based LCA (LC 865, LC 1123)**: the targets are implicit — every deepest leaf. Return
  `(node, depth)`; the deeper side wins, and **equal depth** means the deepest paths meet here.
- **"Smallest subtree that contains every X"** is the same question as LCA of all the X nodes.
- Related: LC 235 (a BST: walk down by value, O(h)), LC 1644 (targets may be missing), LC 1650
  (parent pointers: two-pointer walk up), LC 1676 (many targets). The worked solutions are in
  [tree_lca_distance.md](./tree_lca_distance.md).

#### Binary Tree Maximum Path Sum — LC 124

> **Key point** — Template 6 with values: return the best one-sided path, and update the answer with the two-sided bend at each node.

**Reduces to**: [tree_lca_distance.md § LC 124](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124), where it is worked; this is a pointer.

#### Distribute Coins in Binary Tree — LC 979

> **Key point** — every tree edge is a bridge: cutting the edge above a subtree leaves exactly two
> sides, so the coins that must cross it are `|balance(subtree)|`, where
> `balance = node.val - 1 + balance(left) + balance(right)` (keep one coin, push the rest up). The
> traffic is forced; the answer is the sum of `|balance|` over every edge, with nothing to search.

**Reduces to**: [Template 6](#template-6-bottom-up-post-order-dfs--lc-543-) — return a signed
value upward (the balance), accumulate a different one globally (its absolute value).

```text
LC 979 trace — root = [0, 3, 0]        balance = val - 1 + left + right

        0                 dfs(left  3) -> 3 - 1 + 0 + 0 = +2   moves += 2   (2 coins go UP)
       / \                dfs(right 0) -> 0 - 1 + 0 + 0 = -1   moves += 1   (1 coin  goes DOWN)
      3   0               dfs(root  0) -> 0 - 1 + 2 + (-1) = 0            <- always 0 at root

  total moves = 2 + 1 = 3
```

```python
# python
# LC 979 - Distribute Coins in Binary Tree
# IDEA: post-order DFS; each subtree returns its net balance, each edge costs |balance| moves
# time = O(n), space = O(h)   # h = tree height, worst O(n)
class Solution:
    def distributeCoins(self, root):
        self.moves = 0

        def dfs(node):
            if not node:
                return 0
            left = dfs(node.left)                 # net surplus/deficit of left subtree
            right = dfs(node.right)               # net surplus/deficit of right subtree
            balance = node.val - 1 + left + right # keep 1 coin, push the rest up
            self.moves += abs(balance)            # this subtree's edge to its parent
            return balance                        # signed, never abs()

        dfs(root)
        return self.moves
```

```java
// java
// LC 979 - Distribute Coins in Binary Tree
// IDEA: post-order DFS; each subtree returns its net balance, each edge costs |balance| moves
// time = O(n), space = O(h)
private int moves = 0;

public int distributeCoins(TreeNode root) {
    moves = 0;
    dfs(root);
    return moves;
}

private int dfs(TreeNode node) {
    if (node == null) {
        return 0;
    }
    int left = dfs(node.left);
    int right = dfs(node.right);
    int balance = node.val - 1 + left + right;  // keep 1 coin, push the rest up
    moves += Math.abs(balance);                 // this subtree's edge to its parent
    return balance;                             // signed, never Math.abs()
}
```

**Traps**
- Return the **signed** balance and accumulate the absolute one. Returning `abs(...)` upward is the
  classic bug: a `-2` deficit must stay negative to cancel a sibling's `+2` at their parent.
- Do not skip a node because `node.val == 1`: a locally balanced node still carries its subtrees'
  traffic.
- Do not diffuse coins with a BFS over an adjacency list. A frontier cannot see that the left
  subtree is 3 short and the right has 3 spare; only the bottom-up balance gives that global view.

**Similar**:
  - LC 979 - Distribute Coins in Binary Tree (canonical post-order balance/flow)
  - LC 2477 - Minimum Fuel Cost to Report to the Capital (same edge-flow count, but `ceil(people / seats)` per edge)
  - LC 1443 - Minimum Time to Collect All Apples in a Tree (post-order, charge 2 per useful edge)
  - LC 1339 - Maximum Product of Splitted Binary Tree (post-order subtree sum, then cut one edge)
  - LC 508 - Most Frequent Subtree Sum (per-subtree value rolled up post-order)
  - LC 124 - Binary Tree Maximum Path Sum (return one value up, aggregate a different one globally)
  - LC 2049 - Count Nodes With the Highest Score (subtree size rollup — see the variation below)

#### Count Nodes With the Highest Score — LC 2049

> **Key point** — removing node `x` leaves each child's subtree as one component, plus everything
> **above** `x`, of size `n - size(x)`. One post-order pass that returns subtree sizes therefore
> scores every node: `score(x) = Π size(child) × (n - size(x))`, the last factor dropped at the
> root.

**Reduces to**: [Template 6](#template-6-bottom-up-post-order-dfs--lc-543-) — return the subtree
size upward, aggregate the score (and how many nodes reach the best one) on the way.

```python
# python
# LC 2049 - Count Nodes With the Highest Score
# IDEA: post-order returns each subtree's size; removing x leaves its child subtrees + (n - size(x)) above
# time = O(n), space = O(n)
def countHighestScoreNodes(parents):
    n = len(parents)
    children = [[] for _ in range(n)]
    for node in range(1, n):                 # parents[0] == -1: node 0 is the root
        children[parents[node]].append(node)

    best_score = 0
    best_count = 0

    def subtree_size(node):
        nonlocal best_score, best_count
        size = 1
        score = 1
        for child in children[node]:
            child_size = subtree_size(child)
            score *= child_size               # each child's subtree is one component
            size += child_size
        above = n - size                      # everything outside this subtree
        if above > 0:                         # the root has nothing above it
            score *= above
        if score > best_score:
            best_score, best_count = score, 1
        elif score == best_score:
            best_count += 1
        return size

    subtree_size(0)
    return best_count
```

**Traps**
- The root has no component above it: `n - size(root) == 0` would zero every root score. Skip the
  factor when it is 0.
- n ≤ 10⁵ and the tree can be a chain, so Python recursion overflows its default limit. Raise it
  with `sys.setrecursionlimit`, or compute sizes iteratively in reverse BFS order.
- The product of three components of ~3·10⁴ nodes is ~2.7·10¹³: in Java it needs `long`.

**Similar** — LC 1519 *(subtree aggregation by label)* · LC 508 *(subtree sum + frequency)* ·
LC 834 *(subtree size, then re-rooting)*

### Template 7: 2-Pass DFS (Boundary Elimination) — LC 1254

**Recognise** — "closed islands", "surrounded regions", "enclaves", "captured": land connected to the
border does not count. Grid ≤ 100 × 100 (LC 1254) or 200 × 200 (LC 130).

**Key point** — land that touches the border can never be closed, so pass 1 floods every
border-connected cell away; whatever land survives pass 1 is exactly the closed land, and pass 2
counts it the way LC 200 counts islands.

```python
# python
# LC 1254 - Number of Closed Islands
# IDEA: pass 1 floods every island touching the border; pass 2 counts the islands left (0 = land here)
# time = O(m * n), space = O(m * n)
def closedIsland(grid):
    rows, cols = len(grid), len(grid[0])

    def flood(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 1:
            return
        grid[r][c] = 1                        # land -> water, which also marks it visited
        flood(r + 1, c)
        flood(r - 1, c)
        flood(r, c + 1)
        flood(r, c - 1)

    # pass 1: remove every island that touches the border
    for c in range(cols):
        flood(0, c)
        flood(rows - 1, c)
    for r in range(rows):
        flood(r, 0)
        flood(r, cols - 1)

    # pass 2: every island still standing is closed
    count = 0
    for r in range(1, rows - 1):
        for c in range(1, cols - 1):
            if grid[r][c] == 0:
                count += 1
                flood(r, c)
    return count
```

```java
// java
// LC 1254 - Number of Closed Islands
// IDEA: pass 1 floods every island touching the border; pass 2 counts the islands left (0 = land here)
// time = O(m * n), space = O(m * n)
public int closedIsland(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    for (int c = 0; c < cols; c++) {          // pass 1: top and bottom borders
        flood(grid, 0, c);
        flood(grid, rows - 1, c);
    }
    for (int r = 0; r < rows; r++) {          // pass 1: left and right borders
        flood(grid, r, 0);
        flood(grid, r, cols - 1);
    }
    int count = 0;                            // pass 2: every island left is closed
    for (int r = 1; r < rows - 1; r++) {
        for (int c = 1; c < cols - 1; c++) {
            if (grid[r][c] == 0) {
                count++;
                flood(grid, r, c);
            }
        }
    }
    return count;
}

private void flood(int[][] grid, int r, int c) {
    if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] == 1) {
        return;
    }
    grid[r][c] = 1;                           // land -> water, which also marks it visited
    flood(grid, r + 1, c);
    flood(grid, r - 1, c);
    flood(grid, r, c + 1);
    flood(grid, r, c - 1);
}
```

**Complexity**
- **Time O(m·n)**: both passes together flood each land cell once, because `flood` turns a 0 into
  a 1 before recursing; the scans read every cell a constant number of times.
- **Space O(m·n)**: the recursion depth of one flood, in the worst case the whole grid.

**Traps**
- Deciding "closed" inside a single DFS and returning as soon as it touches the border: the rest of
  that island is left unflooded and is counted again from another cell. If you do it in one pass,
  the DFS must finish the whole island before it reports.
- LC 1254 uses `0` for land and `1` for water — the reverse of LC 200.
- LC 130 marks border regions with a third value (`'E'`), then flips `'O' → 'X'` and `'E' → 'O'`
  in a separate sweep; flipping during the flood rewrites cells it still has to read.

**Not this if** — the question is the **distance** to the border, not whether a cell reaches it →
multi-source BFS from the border cells
([bfs.md § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-)).

**Drill** — LC 1254 · LC 1020 *(count cells instead of islands)* · LC 130 *(flip what is left)* ·
LC 417 ⭐ *(two floods, one inward from each ocean, then intersect)*

### Template 8: Path Signature (Shape Encoding) — LC 694

**Recognise** — "number of distinct islands", "same shape after translation", "unique shapes",
"duplicate subtrees". Grid ≤ 50 × 50.

**Key point** — a DFS that always tries the four directions in the same order walks two islands of
the same shape in the same order. So the string of moves, with a back marker each time it returns,
is identical for equal shapes and different for different ones, and a set of those strings counts
the shapes.

```python
# python
# LC 694 - Number of Distinct Islands
# IDEA: record the move into each cell and a 'B' on the way back; equal shapes give equal strings
# time = O(m * n), space = O(m * n)
def numDistinctIslands(grid):
    rows, cols = len(grid), len(grid[0])
    shapes = set()

    def dfs(r, c, move, path):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != 1:
            return
        grid[r][c] = 0                        # mark visited
        path.append(move)
        dfs(r + 1, c, 'D', path)              # a FIXED order: down, up, right, left
        dfs(r - 1, c, 'U', path)
        dfs(r, c + 1, 'R', path)
        dfs(r, c - 1, 'L', path)
        path.append('B')                      # back marker: this cell's branch is finished

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                path = []
                dfs(r, c, 'S', path)          # 'S' = the start cell
                shapes.add(''.join(path))
    return len(shapes)
```

```java
// java
// LC 694 - Number of Distinct Islands
// IDEA: record the move into each cell and a 'B' on the way back; equal shapes give equal strings
// time = O(m * n), space = O(m * n)
public int numDistinctIslands(int[][] grid) {
    Set<String> shapes = new HashSet<>();
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] == 1) {
                StringBuilder path = new StringBuilder();
                dfs(grid, r, c, 'S', path);
                shapes.add(path.toString());
            }
        }
    }
    return shapes.size();
}

private void dfs(int[][] grid, int r, int c, char move, StringBuilder path) {
    if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != 1) {
        return;
    }
    grid[r][c] = 0;                           // mark visited
    path.append(move);
    dfs(grid, r + 1, c, 'D', path);           // a FIXED order: down, up, right, left
    dfs(grid, r - 1, c, 'U', path);
    dfs(grid, r, c + 1, 'R', path);
    dfs(grid, r, c - 1, 'L', path);
    path.append('B');                         // back marker
}
```

**Complexity**
- **Time O(m·n)**: each land cell is entered once and appends two characters, so building and
  hashing all the strings is linear in the total land.
- **Space O(m·n)**: the strings in `shapes` plus the recursion depth.

**Dry run** — why the back marker is needed

```text
Shape A   1 1        Shape B   1 .
          1 .                  1 1

order D, U, R, L from the top-left cell
A: S, D to (1,0), back, R to (0,1), back, back  ->  "SDBRBB"
B: S, D to (1,0), R to (1,1), back, back, back  ->  "SDRBBB"
without the 'B's both are "SDR"
```

**Traps**
- No back marker: the two shapes above both encode as `"SDR"` and are counted as one.
- The direction order must be the same for every island; looping over a `set` of directions does
  not guarantee that.
- Relative coordinates `(r - r0, c - c0)` in visiting order are an equally valid signature and need
  no marker. Never mix the two encodings in one set.

**Not this if** — rotations and reflections count as the same shape (LC 711) → collect each island's
cells and normalise them over the 8 transforms. Subtrees instead of islands (LC 652) → serialise
each subtree post-order and count the strings in a map.

**Drill** — LC 694 · LC 652 *(serialise every subtree)* · LC 711 *(8 transforms)*

### Template 9: Grid DFS with Restore (Backtracking) — LC 1219

> Source: [`path-with-maximum-gold.py`](../../leetcode_python/Backtracking/path-with-maximum-gold.py)

**Recognise** — "collect the most gold on one path", "the longest path that never revisits a cell",
"start and stop anywhere", "word search". A small grid (m, n ≤ 15, at most 25 useful cells) → an
exponential search is the intended answer.

**Key point** — a cell is marked when the path enters it and **restored** when the path leaves, so
the grid always shows exactly the cells on the current path. That is what lets a later path — from
another start, or down a sibling branch — use the cell again.

```python
# python
# LC 1219 - Path with Maximum Gold
# IDEA: DFS from every gold cell; mark on entry, restore on exit; the guard at the top of the child
# time = O(k * 3^k), space = O(k)   k = number of gold cells (k <= 25)
class Solution:
    def getMaximumGold(self, grid):
        self.max_gold = 0
        rows, cols = len(grid), len(grid[0])
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] > 0:
                    self.dfs(grid, r, c, 0)            # start value = 0
        return self.max_gold

    def dfs(self, grid, r, c, cur_gold):
        rows, cols = len(grid), len(grid[0])
        # GUARD: off the grid, empty (0) or on the current path (-1) -> stop
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] <= 0:
            return
        cache = grid[r][c]
        cur_gold += cache                              # ACCUMULATE this cell
        self.max_gold = max(self.max_gold, cur_gold)   # UPDATE MAX on every cell entry
        grid[r][c] = -1                                # mark: on the current path
        self.dfs(grid, r + 1, c, cur_gold)             # the guard filters every direction
        self.dfs(grid, r - 1, c, cur_gold)
        self.dfs(grid, r, c + 1, cur_gold)
        self.dfs(grid, r, c - 1, cur_gold)
        grid[r][c] = cache                             # RESTORE: off the path again
```

```java
// java
// LC 1219 - Path with Maximum Gold
// IDEA: DFS from every gold cell; mark on entry, restore on exit; the guard at the top of the child
// time = O(k * 3^k), space = O(k)
private int maxGold = 0;

public int getMaximumGold(int[][] grid) {
    maxGold = 0;
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] > 0) {
                dfs(grid, r, c, 0);
            }
        }
    }
    return maxGold;
}

private void dfs(int[][] grid, int r, int c, int curGold) {
    if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] <= 0) {
        return;                                        // off the grid, empty, or on the path
    }
    int cache = grid[r][c];
    curGold += cache;
    maxGold = Math.max(maxGold, curGold);
    grid[r][c] = -1;                                   // mark: on the current path
    dfs(grid, r + 1, c, curGold);
    dfs(grid, r - 1, c, curGold);
    dfs(grid, r, c + 1, curGold);
    dfs(grid, r, c - 1, curGold);
    grid[r][c] = cache;                                // restore: off the path again
}
```

**Complexity**
- **Time O(k · 3^k)** worst case: one launch from each of the k gold cells, and after the first step
  each cell on a path has at most 3 neighbours not already on it. With k ≤ 25 and real grids far
  sparser, this runs fast.
- **Space O(k)**: the recursion is as deep as the longest path; the marks live in the grid itself.

**Traps**
- No restore: cells stay `-1` after the first launch, so later launches and sibling branches cannot
  use them. LC 1219's first example, `[[0,6,0],[5,8,7],[0,9,0]]`, returns 23 instead of 24.
- Mark and unmark **once per frame**, around the four calls — never inside a neighbour loop, where the
  same cell is re-marked for every branch of the frame.
- Where the max is updated decides whether you need a seed: on cell entry (above, and V0-2 below)
  every cell counts for free; per neighbour (V0-3) the start cell is never scored unless the caller
  seeds it.

**Not this if** — the path may revisit cells, or the grid is large (10⁵ cells) → not a search; DP or
BFS. Counting regions (LC 200) → mark and **never** restore
([Template 2](#template-2-graph--grid-dfs-flood-fill--lc-200-)).

**Drill** — LC 1219 · LC 79 ⭐ *(word search: restore the letter)* · LC 980 *(the path must cover
every empty cell)* · LC 212 ⭐ *(many words: a trie prunes the DFS)*

All three versions below are correct. They differ in **where** three decisions are made:
1. **Guard** — is the neighbor valid (in-bounds + gold + not visited)?
2. **Accumulate** — where does `cur_gold` get the current cell added?
3. **Update max** — where do we record `self.max_gold`?

#### Quick Comparison

| | **V0-1** — validate in child (the template above) | **V0-2** — validate before call | **V0-3** — update max in loop |
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

#### V0-2 — Validate before the call, accumulate at the call site

```python
# python
# LC 1219 - Path with Maximum Gold (V0-2)
# IDEA: guard BEFORE each recursive call, so no frame is spent on an invalid neighbour;
#       the launch loop's `if grid[y][x] > 0` validates the START cell
# time = O(k * 3^k), space = O(k)
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
# python
# LC 1219 - Path with Maximum Gold (V0-3)
# IDEA: same as V0-2, but max_gold is updated INSIDE the loop (on next_gold), so the entry cell is
#       never scored by the DFS itself and the launch loop must SEED max_gold with grid[y][x]
# time = O(k * 3^k), space = O(k)
class Solution:
    def getMaximumGold(self, grid):
        self.max_gold = 0
        L, W = len(grid), len(grid[0])
        for y in range(L):
            for x in range(W):
                if grid[y][x] > 0:
                    self.max_gold = max(self.max_gold, grid[y][x])  # REQUIRED seed
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
                # mark/unmark is per cell entry, not per neighbour: do not touch grid[y][x] here
                next_gold = cur_gold + grid[y_][x_]
                self.max_gold = max(self.max_gold, next_gold)   # (3) UPDATE MAX in loop
                self.dfs(grid, x_, y_, next_gold)
        grid[y][x] = cache                             # BACKTRACK: restore
```

**When to use:** effectively **never** as a first choice. It's included to show the trap: moving the
max-update into the loop makes the entry cell invisible to the DFS, forcing the caller-side seed.
Miss that one line and single-cell (or fully-isolated) inputs silently return `0`. Prefer V0-1 / V0-2.

### Template 10: Weighted Graph DFS (Division/Ratio Queries) — LC 399

**Recognise** — "evaluate division", "given a / b = 2.0 and b / c = 3.0, what is a / c", "exchange
rates", "unit conversion", "transitive ratios". Variables ≤ 40, queries ≤ 20 → one DFS per query is
plenty.

**Key point** — `a / b = v` is an edge a → b of weight v **and** an edge b → a of weight 1/v. Along any
path the weights multiply and the middle terms cancel (a/b · b/c = a/c), so the product of the
weights from x to y is x / y; any path will do, because the equations are consistent.

```python
# python
# LC 399 - Evaluate Division
# IDEA: weighted graph a -> b (v) and b -> a (1/v); a DFS multiplies the weights along one path
# time = O(Q * (V + E)), space = O(V + E)
from collections import defaultdict

def calcEquation(equations, values, queries):
    graph = defaultdict(dict)
    for (a, b), value in zip(equations, values):
        graph[a][b] = value
        graph[b][a] = 1.0 / value               # the reverse edge, reciprocal weight

    def dfs(node, target, visited):
        if node == target:
            return 1.0
        visited.add(node)
        for nxt, weight in graph[node].items():
            if nxt not in visited:
                rest = dfs(nxt, target, visited)
                if rest != -1.0:
                    return weight * rest        # node/nxt * nxt/target = node/target
        return -1.0

    answers = []
    for x, y in queries:
        if x not in graph or y not in graph:
            answers.append(-1.0)                # an unknown variable, even for x / x
        else:
            answers.append(dfs(x, y, set()))    # a fresh visited set per query
    return answers
```

```java
// java
// LC 399 - Evaluate Division
// IDEA: weighted graph a -> b (v) and b -> a (1/v); a DFS multiplies the weights along one path
// time = O(Q * (V + E)), space = O(V + E)
public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
    Map<String, Map<String, Double>> graph = new HashMap<>();
    for (int i = 0; i < equations.size(); i++) {
        String a = equations.get(i).get(0);
        String b = equations.get(i).get(1);
        graph.computeIfAbsent(a, k -> new HashMap<>()).put(b, values[i]);
        graph.computeIfAbsent(b, k -> new HashMap<>()).put(a, 1.0 / values[i]);
    }
    double[] answers = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
        String x = queries.get(i).get(0);
        String y = queries.get(i).get(1);
        if (!graph.containsKey(x) || !graph.containsKey(y)) {
            answers[i] = -1.0;                  // an unknown variable, even for x / x
        } else {
            answers[i] = dfs(graph, x, y, new HashSet<>());   // a fresh visited set per query
        }
    }
    return answers;
}

private double dfs(Map<String, Map<String, Double>> graph, String node, String target, Set<String> visited) {
    if (node.equals(target)) {
        return 1.0;
    }
    visited.add(node);
    for (Map.Entry<String, Double> edge : graph.get(node).entrySet()) {
        if (!visited.contains(edge.getKey())) {
            double rest = dfs(graph, edge.getKey(), target, visited);
            if (rest != -1.0) {
                return edge.getValue() * rest;  // node/next * next/target
            }
        }
    }
    return -1.0;
}
```

**Complexity**
- **Time O(Q · (V + E))**: each query's DFS visits each variable at most once (`visited`) and reads
  each of its edges once.
- **Space O(V + E)**: the graph, plus O(V) for `visited` and the recursion.

**Traps**
- `x / x` for a variable that never appears is -1.0, not 1.0: LC 399's query `["x","x"]`. Check
  membership before the `node == target` shortcut.
- One `visited` set shared by every query: the second query finds the nodes already visited and
  returns -1.0.
- Storing only `a → b`: a query in the other direction (`b / a`) finds no path.
- Testing `rest > 0` instead of `rest != -1.0` leans on LC 399's guarantee that values are positive;
  the sentinel test does not.

**Not this if** — many queries on a fixed graph, or equations arriving one at a time → weighted
union-find (store each node's ratio to its root). Costs **add** along a path and you want the
cheapest → shortest path ([Dijkstra.md](./Dijkstra.md)).

**Drill** — LC 399 · LC 990 *(equal / not-equal constraints: union-find, no weights)* · LC 2307
*(check the equations for a contradiction)*

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 100 · LC 543 · LC 200 · LC 417 · LC 124 | paired DFS, a post-order return value, flood fill, reachability from a border, a global accumulator |
| **Practice** | LC 94 · LC 112 · LC 101 · LC 695 · LC 113 · LC 46 · LC 450 · LC 1254 · LC 694 · LC 1219 · LC 399 | each remaining template once, in the order the cards teach them |
| **Stretch** | LC 979 · LC 2049 · LC 711 · LC 980 | a signed flow per edge, subtree sizes on removal, shapes up to rotation, covering every cell |

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
- **⚠️ CRITICAL: Not returning immediately when path found**: When searching for a path in DFS, must return true immediately when found (see the early-return section under Template 3)

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
- **Two nodes in the predicate**: if the question cannot be *stated* about one node ("mirror", "same tree"), carry a **pair** through the recursion — Template 1's [paired-DFS variation](#symmetric-tree--lc-101)
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
**Must-Know Problems for Interviews**: LC 94, 100, 101, 104, 112, 113, 124, 200, 236, 297, 399, 694
**Advanced Problems**: LC 124, 297, 329, 472, 652, 694, 711
**Path Signature Pattern**: LC 694 (Distinct Islands), LC 711 (Distinct Islands II), LC 652 (Find Duplicate Subtrees)
