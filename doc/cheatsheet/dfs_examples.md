# DFS — Worked Examples

> **Scope** — The problem set for [dfs.md](./dfs.md): the DFS problems its templates solve, each as one problem card filed under the template it reduces to, plus pointers to the sheet that works a problem when it lives elsewhere.
> **See also** — *parent sheet*: [dfs.md](./dfs.md) — the ten core templates and the pattern-selection flowchart, which is where the *technique* is explained; [dfs_advanced.md](./dfs_advanced.md) — the rare/hard DFS templates and their examples.
> *Neighbouring sheets*: [bst.md](./bst.md), [tree_codec.md](./tree_codec.md) and [tree_lca_distance.md](./tree_lca_distance.md) own the tree problems pointed to here; [bfs_examples.md](./bfs_examples.md) — the breadth-first problem set; [union_find.md](./union_find.md) — the alternative engine for the connectivity problems.

## LeetCode Problem Lists

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## Overview

The DFS problems that are not a template's own anchor problem, grouped by the
[dfs.md](./dfs.md) template they reduce to, in that sheet's order. [dfs.md](./dfs.md) holds the
templates; this sheet is the problems. Each card states the one step the problem adds to its
template. A problem worked in full on another sheet appears here as a pointer row, so every
problem has exactly one home. A template with no problem of its own here (Template 9, Template 10)
has no section.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 655 | [Print Binary Tree](#print-binary-tree--lc-655) | [Template 1: Tree Traversal](./dfs.md#template-1-tree-traversal--lc-94-) | Medium |
| 695 | [Max Area of Island](#max-area-of-island--lc-695) | [Template 2: Graph / Grid DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 529 | [Minesweeper](#minesweeper--lc-529) | [Template 2: Graph / Grid DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 737 | [Sentence Similarity II](#sentence-similarity-ii--lc-737) | [Template 2: Graph / Grid DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 990 | [Satisfiability of Equality Equations](#satisfiability-of-equality-equations--lc-990) | [Template 2: Graph / Grid DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 472 | [Concatenated Words](#concatenated-words--lc-472) | [Template 4: Backtracking](./dfs.md#template-4-backtracking--lc-46) | Hard |
| 663 | [Equal Tree Partition](#equal-tree-partition--lc-663) | [Template 6: Bottom-up DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 1339 | [Maximum Product of Splitted Binary Tree](#maximum-product-of-splitted-binary-tree--lc-1339) | [Template 6: Bottom-up DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 3319 | [K-th Largest Perfect Subtree Size in Binary Tree](#k-th-largest-perfect-subtree-size-in-binary-tree--lc-3319) | [Template 6: Bottom-up DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 417 | [Pacific Atlantic Water Flow](#pacific-atlantic-water-flow--lc-417) | [Template 7: 2-Pass DFS (Boundary)](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254) | Medium |

## Tree Traversal Examples

### Print Binary Tree — LC 655

> **Key point** — the grid's size depends only on the height, so compute the height first and the
> DFS only has to carry `(row, col)` down. Each level down, the horizontal offset to a child halves:
> `2^(height - row - 1)`, which is what keeps subtrees from overlapping.

**Reduces to**: [dfs § Template 1](./dfs.md#template-1-tree-traversal--lc-94-) — a pre-order walk that passes the node's position down instead of returning anything up.

```python
# python
# LC 655 - Print Binary Tree
# IDEA: height first; then pre-order DFS places each node and halves the column offset per level
# time = O(h * 2^h), space = O(h * 2^h)   the output grid is (h + 1) x (2^(h+1) - 1)
def printTree(root):
    def height(node):
        if not node:
            return -1                      # a lone leaf has height 0
        return 1 + max(height(node.left), height(node.right))

    h = height(root)
    rows, cols = h + 1, 2 ** (h + 1) - 1
    grid = [[""] * cols for _ in range(rows)]

    def place(node, row, col):
        if not node:
            return
        grid[row][col] = str(node.val)
        if row == h:
            return
        offset = 2 ** (h - row - 1)        # halves on every level down
        place(node.left, row + 1, col - offset)
        place(node.right, row + 1, col + offset)

    place(root, 0, (cols - 1) // 2)        # root in the middle of the top row
    return grid
```

**Traps**
- Height must be 0 for a single node (null returns `-1`). Returning `0` for null makes the grid one
  row too tall: `[1]` would give two rows instead of one.

**Worked elsewhere**

| LC | Problem | Home |
|---|---|---|
| 94 | Binary Tree Inorder Traversal | [dfs § Template 1](./dfs.md#template-1-tree-traversal--lc-94-) (anchor) |
| 100 | Same Tree | [tree_examples.md](./tree_examples.md) |
| 98 | Validate Binary Search Tree | [bst.md](./bst.md) — validate with bounds |
| 700 | Search in a Binary Search Tree | [bst.md](./bst.md) |
| 297 | Serialize and Deserialize Binary Tree | [tree_codec.md](./tree_codec.md) |
| 449 | Serialize and Deserialize BST | [bst_examples.md](./bst_examples.md) |
| 662 | Maximum Width of Binary Tree | [bfs_examples.md](./bfs_examples.md) — heap index per level |

## Graph / Grid DFS Examples

### Max Area of Island — LC 695

> **Key point** — the flood fill that counts islands in LC 200 also *sizes* them: let the sink
> return `1 + the sizes of its four neighbours`, and the launch loop keeps the maximum instead of a
> count.

**Reduces to**: [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — the same flood fill, returning a size instead of nothing.

```python
# python
# LC 695 - Max Area of Island
# IDEA: each 1 still standing starts a sink that returns the island's size; keep the largest
# time = O(m * n), space = O(m * n)   the recursion is as deep as the largest island
def maxAreaOfIsland(grid):
    rows, cols = len(grid), len(grid[0])

    def sink(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != 1:
            return 0
        grid[r][c] = 0                     # sink on entry: never counted twice
        return 1 + sink(r + 1, c) + sink(r - 1, c) + sink(r, c + 1) + sink(r, c - 1)

    best = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                best = max(best, sink(r, c))
    return best
```

```java
// java
// LC 695 - Max Area of Island
// IDEA: flood fill that returns the size of the island it sinks; keep the largest
// time = O(m * n), space = O(m * n)
class Solution {
    public int maxAreaOfIsland(int[][] grid) {
        int best = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                best = Math.max(best, area(grid, r, c));
            }
        }
        return best;
    }

    private int area(int[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] == 0) {
            return 0;
        }
        grid[r][c] = 0;                           // sink it so it is counted once
        return 1 + area(grid, r + 1, c) + area(grid, r - 1, c)
                 + area(grid, r, c + 1) + area(grid, r, c - 1);
    }
}
```

**Traps**
- Mark the cell *before* recursing. Marking after the four calls lets two neighbours count each
  other, and the recursion never ends on a 2×2 block of land.

### Minesweeper — LC 529

> **Key point** — the flood fill stops at a number. A revealed cell with an adjacent mine becomes
> its count and does not expand; only a cell with **zero** adjacent mines becomes `'B'` and
> recurses into all eight neighbours. Recursing only into `'E'` cells is the visited check.

**Reduces to**: [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — eight directions, and a cell decides from its neighbours whether the flood continues.

```python
# python
# LC 529 - Minesweeper
# IDEA: a mine click is game over; otherwise flood from the click, stopping at cells next to a mine
# time = O(m * n), space = O(m * n)   each cell is revealed once; recursion as deep as the blank region
def updateBoard(board, click):
    rows, cols = len(board), len(board[0])
    r, c = click
    if board[r][c] == "M":
        board[r][c] = "X"
        return board
    dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    def reveal(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or board[r][c] != "E":
            return                         # only an unrevealed empty cell expands
        mines = 0
        for dr, dc in dirs:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] == "M":
                mines += 1
        if mines > 0:
            board[r][c] = str(mines)       # a number stops the flood
            return
        board[r][c] = "B"
        for dr, dc in dirs:
            reveal(r + dr, c + dc)

    reveal(r, c)
    return board
```

**Traps**
- The guard is `board[r][c] != "E"`, not "is it a mine". `'B'` and digit cells are already revealed;
  without the guard two blank cells recurse into each other forever.

### Sentence Similarity II — LC 737

> **Key point** — similarity is **transitive**, so two words are similar exactly when they are in
> the same connected component of the graph whose edges are the similar pairs. Each aligned word
> pair is one reachability query. (LC 734, which is not transitive, is a set lookup.)

**Reduces to**: [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — on an explicit word graph, run once per query.

```python
# python
# LC 737 - Sentence Similarity II
# IDEA: undirected graph of similar pairs; each differing word pair must be reachable
# time = O(n * (V + E)), space = O(V + E)   one DFS per aligned pair, n = sentence length
from collections import defaultdict

def areSentencesSimilarTwo(sentence1, sentence2, similarPairs):
    if len(sentence1) != len(sentence2):
        return False
    graph = defaultdict(set)
    for a, b in similarPairs:
        graph[a].add(b)
        graph[b].add(a)                    # similarity is symmetric: both directions

    def connected(src, dst):
        stack, seen = [src], {src}
        while stack:
            word = stack.pop()
            if word == dst:
                return True
            for nxt in graph[word]:
                if nxt not in seen:
                    seen.add(nxt)
                    stack.append(nxt)
        return False

    for w1, w2 in zip(sentence1, sentence2):
        if w1 != w2 and not connected(w1, w2):
            return False
    return True
```

**Traps**
- A word is similar to itself even when it appears in no pair: check `w1 != w2` before searching.
- With many queries, label components once (as in LC 990 below) or use union-find; a DFS per query
  repeats the same walk.

### Satisfiability of Equality Equations — LC 990

> **Key point** — `==` is an equivalence: the `==` edges split the variables into components, and
> the equations are satisfiable unless some `!=` joins two variables of the **same** component. So
> label every component once, then each `!=` is an O(1) check. All `==` must be read before any `!=`.

**Reduces to**: [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — flood fill that writes a component id instead of sinking cells.

```python
# python
# LC 990 - Satisfiability of Equality Equations
# IDEA: DFS labels each == component; a != inside one component is a contradiction
# time = O(n), space = O(1)   n = equations; at most 26 variables
from collections import defaultdict

def equationsPossible(equations):
    graph = defaultdict(list)
    for eq in equations:
        if eq[1] == "=":
            graph[eq[0]].append(eq[3])
            graph[eq[3]].append(eq[0])     # == is symmetric: store both directions

    group = {}
    def label(var, gid):
        group[var] = gid
        for nxt in graph[var]:
            if nxt not in group:
                label(nxt, gid)

    for var in list(graph):
        if var not in group:
            label(var, var)                # one DFS per component

    for eq in equations:
        if eq[1] == "!":
            a, b = eq[0], eq[3]
            if a == b or (a in group and group.get(b) == group[a]):
                return False
    return True
```

**Traps**
- `a!=a` is unsatisfiable on its own, and `a` may appear in no `==`. Test `a == b` first.
- A one-directional graph breaks transitivity checks: from `a==b, b==c`, a search from `c` finds no
  edge. Add both directions.
- The union-find version is the other common answer; it is worked in
  [union_find_examples.md](./union_find_examples.md).

**Worked elsewhere**

| LC | Problem | Home |
|---|---|---|
| 200 | Number of Islands | [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) (anchor) |
| 133 | Clone Graph | [graph_examples.md](./graph_examples.md) |

## Path Finding Examples

Every root-to-leaf path problem in this set is worked on another sheet.

| LC | Problem | Home |
|---|---|---|
| 112 | Path Sum | [dfs § Template 3](./dfs.md#template-3-path-finding--lc-112-) (anchor) |
| 113 | Path Sum II | [dfs § Template 3](./dfs.md#template-3-path-finding--lc-112-) — collect every path |
| 129 | Sum Root to Leaf Numbers | [tree_lca_distance.md](./tree_lca_distance.md) — root-to-leaf paths |
| 257 | Binary Tree Paths | [tree_lca_distance.md](./tree_lca_distance.md) — root-to-leaf paths |
| 1448 | Count Good Nodes in Binary Tree | [tree_examples.md](./tree_examples.md) |

## Backtracking Examples

### Concatenated Words — LC 472

> **Key point** — a word qualifies when it splits into **two or more** dictionary words. Try every
> dictionary prefix and recurse on the rest; memoising "can this suffix be split" turns the
> exponential search into word-break DP, because the same suffix is reached by many cuts.

**Reduces to**: [dfs § Template 4](./dfs.md#template-4-backtracking--lc-46) — choose a prefix, recurse on the remainder; plus a memo, because the choices overlap.

```python
# python
# LC 472 - Concatenated Words
# IDEA: a word counts if a proper dictionary prefix leaves a suffix that splits into dictionary words
# time = O(n * L^3), space = O(n * L)   n words of length L: L suffixes, L cuts each, O(L) slicing
def findAllConcatenatedWordsInADict(words):
    word_set = set(words)
    memo = {}

    def splits(word):
        # can `word` be cut into one or more dictionary words?
        if word in memo:
            return memo[word]
        for i in range(1, len(word) + 1):
            prefix = word[:i]
            if prefix in word_set and (i == len(word) or splits(word[i:])):
                memo[word] = True
                return True
        memo[word] = False
        return False

    result = []
    for word in words:
        if not word:
            continue
        # two or more pieces: the first piece is a PROPER prefix
        for i in range(1, len(word)):
            if word[:i] in word_set and splits(word[i:]):
                result.append(word)
                break
    return result
```

**Traps**
- A word is in its own dictionary. Requiring a *proper* first prefix (`i < len(word)`) is what stops
  every word from matching itself.
- Without the memo, `"aaaa…ab"` against `["a", "aa", "aaa"]` explores every composition: exponential.

## Tree Modification Examples

Every tree-modification problem in this set is worked on another sheet.

| LC | Problem | Home |
|---|---|---|
| 450 | Delete Node in a BST | [dfs § Template 5](./dfs.md#template-5-tree-modification--lc-450) (anchor) |
| 701 | Insert into a Binary Search Tree | [bst.md](./bst.md) |
| 669 | Trim a Binary Search Tree | [bst.md](./bst.md) |
| 538 | Convert BST to Greater Tree | [bst_examples.md](./bst_examples.md) — reverse in-order |
| 776 | Split BST | [bst_examples.md](./bst_examples.md) |
| 623 | Add One Row to Tree | [bfs_examples.md](./bfs_examples.md) — level BFS, with the DFS countdown beside it |

## Bottom-up (Post-Order) DFS Examples

### Equal Tree Partition — LC 663

> **Key point** — removing one edge leaves the subtree below it and the rest. Collect every subtree
> sum in one post-order pass; the tree splits evenly exactly when some subtree **other than the
> whole tree** sums to `total / 2`.

**Reduces to**: [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — each node returns its subtree sum upward and records it.

```python
# python
# LC 663 - Equal Tree Partition
# IDEA: post-order collects every subtree sum; look for total / 2 among the proper subtrees
# time = O(n), space = O(n)
def checkEqualTree(root):
    sums = []

    def subtree_sum(node):
        if not node:
            return 0
        s = node.val + subtree_sum(node.left) + subtree_sum(node.right)
        sums.append(s)
        return s

    total = subtree_sum(root)
    sums.pop()                             # the whole tree is the last sum: not a cut
    return total % 2 == 0 and total // 2 in sums
```

**Traps**
- Drop the root's own sum before searching. For `[0, -1, 1]` the total is 0 and the whole tree
  sums to 0, but no edge removal gives two halves of 0.
- An odd total can never split; check it before `//`.

### Maximum Product of Splitted Binary Tree — LC 1339

> **Key point** — the same subtree-sum pass as LC 663: cutting above a subtree with sum `s` gives
> `s * (total - s)`. One pass for every `s`, then the best product.

**Reduces to**: [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — subtree sums returned upward, then scored against the total.

```python
# python
# LC 1339 - Maximum Product of Splitted Binary Tree
# IDEA: post-order records every subtree sum s; each is one cut worth s * (total - s)
# time = O(n), space = O(n)
def maxProduct(root):
    sums = []

    def subtree_sum(node):
        if not node:
            return 0
        s = node.val + subtree_sum(node.left) + subtree_sum(node.right)
        sums.append(s)
        return s

    total = subtree_sum(root)
    best = 0
    for s in sums:
        best = max(best, s * (total - s))
    return best % (10 ** 9 + 7)            # take the max BEFORE the modulo
```

**Traps**
- Apply the modulo once, to the maximum. Comparing products already reduced mod 10⁹+7 picks the
  wrong cut.

### K-th Largest Perfect Subtree Size in Binary Tree — LC 3319

> **Key point** — a subtree is perfect exactly when both children are perfect **and** have the
> same height. Return `(height, is_perfect)` upward; a perfect subtree of height `h` has
> `2^h − 1` nodes, so its size needs no separate count.

**Reduces to**: [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — the node decides from its children's returned pair.

```python
# python
# LC 3319 - K-th Largest Perfect Subtree Size in Binary Tree
# IDEA: post-order returns (height, is_perfect); record 2^height - 1 for every perfect subtree
# time = O(n log n), space = O(n)   the sort of the recorded sizes dominates
def kthLargestPerfectSubtree(root, k):
    sizes = []

    def dfs(node):
        if not node:
            return 0, True
        left_h, left_ok = dfs(node.left)
        right_h, right_ok = dfs(node.right)
        if left_ok and right_ok and left_h == right_h:
            sizes.append(2 ** (left_h + 1) - 1)
            return left_h + 1, True
        return max(left_h, right_h) + 1, False

    dfs(root)
    if len(sizes) < k:
        return -1
    sizes.sort(reverse=True)
    return sizes[k - 1]
```

**Traps**
- "Both children perfect" is not enough: a node whose left child is a leaf and whose right child is
  a perfect subtree of height 2 is not perfect. The heights must match.

**Worked elsewhere**

| LC | Problem | Home |
|---|---|---|
| 543 | Diameter of Binary Tree | [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) (anchor) |
| 236 | Lowest Common Ancestor of a Binary Tree | [tree_lca_distance.md](./tree_lca_distance.md) |
| 508 | Most Frequent Subtree Sum | [hashing.md](./hashing.md) — subtree sums counted in a hash map |

## 2-Pass DFS (Boundary Elimination) Examples

### Pacific Atlantic Water Flow — LC 417

> **Key point** — asking "can water from this cell reach the ocean?" for every cell is O((mn)²).
> Reverse it: start at each ocean's border and climb **uphill** (`next >= current`). The cells one
> flood reaches can drain to that ocean; the answer is the cells both floods reach.

**Reduces to**: [dfs § Template 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254) — the flood starts from the border, but it is run twice (one per ocean) and the results are intersected rather than subtracted.

```python
# python
# LC 417 - Pacific Atlantic Water Flow
# IDEA: flood uphill from each ocean's border; answer = cells reached by both floods
# time = O(m * n), space = O(m * n)   each flood visits a cell at most once
def pacificAtlantic(heights):
    rows, cols = len(heights), len(heights[0])
    pacific, atlantic = set(), set()

    def climb(r, c, reached):
        reached.add((r, c))
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if (0 <= nr < rows and 0 <= nc < cols and (nr, nc) not in reached
                    and heights[nr][nc] >= heights[r][c]):   # uphill: its water flows down to us
                climb(nr, nc, reached)

    for r in range(rows):
        climb(r, 0, pacific)               # left edge
        climb(r, cols - 1, atlantic)       # right edge
    for c in range(cols):
        climb(0, c, pacific)               # top edge
        climb(rows - 1, c, atlantic)       # bottom edge

    result = []
    for r in range(rows):
        for c in range(cols):
            if (r, c) in pacific and (r, c) in atlantic:
                result.append([r, c])
    return result
```

**Traps**
- The comparison is reversed: the flood moves to a neighbour that is **higher or equal**, because
  water would flow from it down to the current cell. Using `<` finds where water flows *to*.
- The reached set doubles as the visited set; without it, two equal-height cells recurse forever.

**Worked elsewhere**

| LC | Problem | Home |
|---|---|---|
| 1254 | Number of Closed Islands | [dfs § Template 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254) (anchor) |

## Path Signature Examples

| LC | Problem | Home |
|---|---|---|
| 694 | Number of Distinct Islands | [dfs § Template 8](./dfs.md#template-8-path-signature-shape-encoding--lc-694) (anchor) |
| 652 | Find Duplicate Subtrees | [tree_codec.md](./tree_codec.md) — post-order subtree signature + hash map count |
