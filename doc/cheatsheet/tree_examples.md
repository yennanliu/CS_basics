# Tree — Worked LeetCode Examples

> **Scope** — The binary-tree problem set for the templates in binary_tree.md: each problem is filed under the template it reduces to, with the one step it adds, and the templates themselves are not restated here.
> **See also**: [binary_tree.md](./binary_tree.md) — the templates these problems reduce to; [tree.md](./tree.md) — tree concepts, traversal strategy and Morris threading; [tree_lca_distance.md](./tree_lca_distance.md) — LCA, distance and path problems; [bst_examples.md](./bst_examples.md) — problems that use the BST ordering; [tree_codec.md](./tree_codec.md) and [tree_construction.md](./tree_construction.md) — serialising and building trees.

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

## Overview

The classic binary-tree problems, grouped by the [binary_tree.md](./binary_tree.md) template each
one reduces to and in that sheet's template order. binary_tree.md holds the templates; this sheet is
the problems, and each card says only what the problem adds to its template. A problem that is the
anchor of a template, or that is worked in another sheet, has a row in the Index pointing there
instead of a second copy here.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 545 | [Boundary of Binary Tree](#boundary-of-binary-tree--lc-545) | [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) | Medium |
| 111 | [Minimum Depth of Binary Tree](#minimum-depth-of-binary-tree--lc-111) | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) | Easy |
| 199 | [Binary Tree Right Side View](#binary-tree-right-side-view--lc-199) | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) | Medium |
| 106 | [Construct Binary Tree from Inorder and Postorder Traversal](#construct-binary-tree-from-inorder-and-postorder-traversal--lc-106) | [binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) | Medium |
| 1448 | [Count Good Nodes in Binary Tree](#count-good-nodes-in-binary-tree--lc-1448) | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | Medium |
| 1026 | [Maximum Difference Between Node and Ancestor](#maximum-difference-between-node-and-ancestor--lc-1026) | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | Medium |
| 104 | [Maximum Depth of Binary Tree](#maximum-depth-of-binary-tree--lc-104) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Easy |
| 110 | [Balanced Binary Tree](#balanced-binary-tree--lc-110) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Easy |
| 124 | Binary Tree Maximum Path Sum — worked in [tree_lca_distance.md](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Hard |
| 222 | Count Complete Tree Nodes — the template's anchor | [binary_tree § Template 7](./binary_tree.md#template-7-count-nodes-of-a-complete-tree--lc-222) | Medium |
| 968 | [Binary Tree Cameras](#binary-tree-cameras--lc-968) | [binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) | Hard |
| 226 | [Invert Binary Tree](#invert-binary-tree--lc-226) | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Easy |
| 114 | [Flatten Binary Tree to Linked List](#flatten-binary-tree-to-linked-list--lc-114) | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Medium |
| 1110 | Delete Nodes And Return Forest — the template's anchor | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Medium |
| 662 | Maximum Width of Binary Tree — the template's anchor | [binary_tree § Template 11](./binary_tree.md#template-11-bfs-with-positional-index--lc-662-) | Medium |
| 101 | [Symmetric Tree](#symmetric-tree--lc-101) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Easy |
| 617 | [Merge Two Binary Trees](#merge-two-binary-trees--lc-617) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Easy |
| 951 | [Flip Equivalent Binary Trees](#flip-equivalent-binary-trees--lc-951) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Medium |
| 2415 | [Reverse Odd Levels of Binary Tree](#reverse-odd-levels-of-binary-tree--lc-2415) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Medium |
| 98 | Validate Binary Search Tree — the template's anchor | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) | Medium |

Height vs depth, and the `-1` "not found" sentinel for a depth search, are taught once in
[binary_tree.md § 0-5](./binary_tree.md#0-5-depth-vs-height--the-two-numbers-a-tree-dfs-computes-).

## Recursive Traversal Examples

### Boundary of Binary Tree — LC 545

> **Key point** — the boundary is three traversals glued together: the left boundary in
> **pre-order** (recorded on the way down), every leaf left to right, then the right boundary in
> **post-order** (recorded on the way back up, so it comes out bottom to top). Excluding leaves from
> both boundary walks is what keeps a corner from being listed twice.

**Reduces to**: [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) — three walks instead of one, each recording in the order its part of the boundary is read.

```python
# python
# LC 545 - Boundary of Binary Tree
# IDEA: root, then the left boundary top-down, the leaves left to right, the right boundary bottom-up;
#       neither boundary walk records a leaf, so no node appears twice
# time = O(n), space = O(h)
def boundaryOfBinaryTree(root):
    if not root:
        return []
    result = [root.val]

    def is_leaf(node):
        return not node.left and not node.right

    def left_boundary(node):               # pre-order: record on the way down
        while node and not is_leaf(node):
            result.append(node.val)
            node = node.left if node.left else node.right

    def leaves(node):                      # every leaf, left to right
        if not node:
            return
        if is_leaf(node):
            result.append(node.val)
            return
        leaves(node.left)
        leaves(node.right)

    def right_boundary(node):              # post-order: record on the way back up
        stack = []
        while node and not is_leaf(node):
            stack.append(node.val)
            node = node.right if node.right else node.left
        while stack:
            result.append(stack.pop())

    if not is_leaf(root):                  # a lone root is not also a leaf
        left_boundary(root.left)
        leaves(root)
        right_boundary(root.right)
    return result
```

**Traps**
- A boundary node with only a right child: `[1,2,null,null,3,…]` — the left boundary continues to
  the **right** child when there is no left one. Following `.left` only stops the walk early.
- `root = [1]`: the root is a leaf, so collecting leaves again prints `[1, 1]`. Guard the root.
- `root = [1,null,2,3,4]`: the root has no left subtree, so the left boundary is just `[1]`; the
  leaves are `3, 4` and the right boundary is `2`, giving `[1,3,4,2]`.

## Level-Order Examples

### Minimum Depth of Binary Tree — LC 111

> **Key point** — BFS reaches nodes in order of depth, so the **first leaf** it dequeues is the
> shallowest one and the search can stop there; a recursive `min` has to treat a node with one child
> specially, because its empty side is not a leaf.

**Reduces to**: [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — the level-order queue with an early exit at the first leaf.

```python
# python
# LC 111 - Minimum Depth of Binary Tree
# IDEA: BFS carrying the depth; the first leaf dequeued is the shallowest
# time = O(n), space = O(w)   w = maximum width of a level; stops at the first leaf
from collections import deque

def minDepth(root):
    if not root:
        return 0
    queue = deque([(root, 1)])
    while queue:
        node, depth = queue.popleft()
        if not node.left and not node.right:   # the first leaf BFS reaches is the shallowest
            return depth
        if node.left:
            queue.append((node.left, depth + 1))
        if node.right:
            queue.append((node.right, depth + 1))
```

**Traps**
- The recursive form `1 + min(minDepth(left), minDepth(right))` returns 1 on `[2,null,3,null,4,null,5,null,6]`:
  the empty left child scores 0, but it is not a leaf. The correct answer is 5. When one child is
  missing, recurse only into the other.
- LC 104 (max depth) does not have this trap: `max` never picks the empty side over a real one.

### Binary Tree Right Side View — LC 199

> **Key point** — the node you see from the right on each level is the **last** node of that level
> in left-to-right order, and the level-size snapshot is what tells you which dequeue is the last.

**Reduces to**: [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — the per-level reducer keeps only the last node.

```python
# python
# LC 199 - Binary Tree Right Side View
# IDEA: level-order BFS; record the last node of each level
# time = O(n), space = O(w)
from collections import deque

def rightSideView(root):
    result = []
    if not root:
        return result
    queue = deque([root])
    while queue:
        level_size = len(queue)
        for i in range(level_size):
            node = queue.popleft()
            if i == level_size - 1:            # the last node of the level is the one you see
                result.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    return result
```

**Traps**
- Taking only right children: `[1,2,3,4]` has `4` visible on level 2 although it hangs off the
  **left** subtree. The answer is `[1,3,4]`, not `[1,3]`.
- A DFS that visits right before left and records the first node at each new depth works too, in
  O(h) space; `len(result) == depth` is the "first visit to this depth" test.

## Build-from-Traversals Examples

### Construct Binary Tree from Inorder and Postorder Traversal — LC 106

> **Key point** — the **last** postorder value is the root, so read postorder from the end; read
> backwards it is root, right, left, which means the **right** subtree must be built before the left.

**Reduces to**: [binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) — the same inorder split, with the root taken from the other end and the two recursive calls swapped.

```python
# python
# LC 106 - Construct Binary Tree from Inorder and Postorder Traversal
# IDEA: postorder[-1] is the root; its inorder index splits [lo, hi]; build RIGHT first
# time = O(n), space = O(n)   the index map, plus O(h) recursion
def buildTree(inorder, postorder):
    index_of = {val: i for i, val in enumerate(inorder)}
    post_idx = [len(postorder) - 1]           # next root, read from the END of postorder

    def build_range(lo, hi):                  # the subtree whose inorder slice is [lo, hi]
        if lo > hi:
            return None
        root = TreeNode(postorder[post_idx[0]])
        post_idx[0] -= 1
        mid = index_of[root.val]
        root.right = build_range(mid + 1, hi)  # RIGHT first: postorder read backwards is root, right, left
        root.left = build_range(lo, mid - 1)
        return root

    return build_range(0, len(inorder) - 1)
```

**Traps**
- Building the left subtree first: the next value from the end of postorder belongs to the right
  subtree, so `inorder = [9,3,15,20,7]`, `postorder = [9,15,7,20,3]` comes out with 20 on the left.
- Slicing (`inorder[:idx]`, `postorder[:idx]`) and `inorder.index(...)` at every call are O(n) each,
  O(n²) on a skewed tree; the index map and the moving pointer keep it O(n).

## Root-to-Leaf State Examples

### Count Good Nodes in Binary Tree — LC 1448

> **Key point** — whether a node is "good" depends only on the maximum **above** it, so that
> maximum travels down as an argument; it is an `int`, so each call has its own copy and there is
> nothing to undo on the way back.

**Reduces to**: [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) — the root-to-node state is one number instead of a list, so the backtracking step disappears.

```python
# python
# LC 1448 - Count Good Nodes in Binary Tree
# IDEA: pre-order DFS carrying the maximum on the path from the root
# time = O(n), space = O(h)
def goodNodes(root):
    def dfs(node, max_so_far):
        if not node:
            return 0
        good = 1 if node.val >= max_so_far else 0
        max_so_far = max(max_so_far, node.val)   # an int: each call has its own copy
        return good + dfs(node.left, max_so_far) + dfs(node.right, max_so_far)

    return dfs(root, root.val)
```

**Traps**
- `>` instead of `>=` drops the nodes equal to the max so far: in `[3,3,null,4,2]` the second 3
  is good, so the answer is 3, and `>` returns 2.
- Seeding with 0 instead of `root.val` breaks on negative values; the root is always good, so seed
  with its own value (or `-inf`).

### Maximum Difference Between Node and Ancestor — LC 1026

> **Key point** — the largest `|ancestor − descendant|` on a path is the path's max minus its min,
> so carry those two numbers down and read `high − low` when the path ends. No pair is ever
> compared, and no BST order is used.

**Reduces to**: [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) — state carried down a root-to-leaf path; the
same two values as [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98)'s bounds, widened instead of checked.

```python
# python
# LC 1026 - Maximum Difference Between Node and Ancestor
# IDEA: carry the path's min and max down; at the end of a path the answer is max - min
# time = O(n), space = O(h)
def maxAncestorDiff(root):
    def dfs(node, low, high):
        if not node:
            return high - low                    # the path ended: its widest spread
        low = min(low, node.val)
        high = max(high, node.val)
        return max(dfs(node.left, low, high), dfs(node.right, low, high))

    return dfs(root, root.val, root.val)
```

**Traps**
- Comparing each node only with its parent: LC 1026's example 1,
  `[8,3,10,1,6,null,14,null,null,4,7,13]`, returns 5 (8 − 3) instead of 7 (8 − 1, two edges
  apart).

| | Validate (LC 98) | Max ancestor diff (LC 1026) |
|---|---|---|
| carried down | the legal interval | the path's min and max |
| at a node outside it | return false | widen it |
| children combine with | `and` | `max` |

## Height-Up Examples

### Maximum Depth of Binary Tree — LC 104

> **Key point** — the depth of a tree is one more than the deeper of its two subtrees; it is the
> height the template returns, with no global needed because the root's own return value is the answer.

**Reduces to**: [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) — the return value alone, without the global.

```python
# python
# LC 104 - Maximum Depth of Binary Tree
# IDEA: post-order; my depth = 1 + the deeper child's depth
# time = O(n), space = O(h)
def maxDepth(root):
    if not root:
        return 0
    return 1 + max(maxDepth(root.left), maxDepth(root.right))
```

**Traps**
- Counting edges instead of nodes: LC 104 counts **nodes**, so `maxDepth(None) = 0` and a single
  node is 1. Returning `-1` for `None` (the edge convention) gives 2 for `[3,9,20,null,null,15,7]`.
- On a chain of 10⁴ nodes the recursion is 10⁴ deep; in Python that needs `sys.setrecursionlimit`
  or a level-order BFS that counts levels.

### Balanced Binary Tree — LC 110

> **Key point** — return the height and the verdict from the **same** post-order pass: `-1` means
> "already unbalanced somewhere below", and every ancestor passes it straight up, so each node is
> visited once.

**Reduces to**: [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) — the height doubles as a failure signal instead of feeding a global.

```python
# python
# LC 110 - Balanced Binary Tree
# IDEA: post-order height; -1 is the "unbalanced" sentinel that short-circuits upward
# time = O(n), space = O(h)
def isBalanced(root):
    def height(node):                      # -1 means "already unbalanced below here"
        if not node:
            return 0
        left = height(node.left)
        if left == -1:
            return -1
        right = height(node.right)
        if right == -1:
            return -1
        if abs(left - right) > 1:
            return -1
        return 1 + max(left, right)

    return height(root) != -1
```

**Second solution — top-down, and why it costs more.** The version most people write first checks
the heights at the root and then recurses into both children, so `height()` re-walks every subtree
once per ancestor. It is correct; it is shown only for the complexity question the interviewer asks next.

```python
# python
# LC 110 - Balanced Binary Tree (top-down)
# IDEA: at every node compare the two subtree heights, then recurse into both children
# time = O(n log n) on a balanced tree, O(n^2) upper bound; space = O(h)
def isBalanced(root):
    def height(node):
        if not node:
            return 0
        return 1 + max(height(node.left), height(node.right))

    if not root:
        return True
    if abs(height(root.left) - height(root.right)) > 1:
        return False
    return isBalanced(root.left) and isBalanced(root.right)
```

Count the work per level of the recursion, times the number of levels. Each shape gives one of
each; never take one number from each shape:

| | Balanced tree | Skewed tree (a chain) |
|---|---|---|
| Levels | log n | n |
| Work on level `d` | 2^d calls × subtree size n/2^d ≈ n | 1 call × subtree size n − d |
| Total | n · log n = **O(n log n)** — T(n) = 2T(n/2) + O(n) | n + (n−1) + … + 1 = **O(n²)** — T(n) = T(n−1) + O(n) |

Per node, the same count reads: `height()` visits a node once for each of its ancestors, so the
total is the sum of the depths, n × (average depth). Only the balanced shape keeps that depth at log n.

**Traps**
- O(n² log n) is the answer you get by multiplying the skewed series (already a total over every
  level) by the balanced height. It counts the levels twice.
- On a chain this top-down code actually exits at the root (heights n−1 and 0 differ by more than
  1), so the O(n²) is the cost of the recursion pattern, paid in full by code with no early exit,
  such as a naive diameter that calls `height` at every node.
- Checking the bottom-up `-1` only once, after both calls: still O(n), but it walks the right
  subtree of an already-failed node for nothing. Test `left == -1` before recursing right.

## Tree DP Examples

### Binary Tree Cameras — LC 968

> **Key point** — each call returns a **state** — uncovered, has a camera, covered — and the parent
> decides. A null child counts as covered, so a leaf reports "uncovered" and forces the camera onto
> its parent, which covers three levels instead of one. An uncovered node places nothing: it defers
> to its parent.

**Reduces to**: [binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) — three states instead of (take, skip), and a greedy rule at each node instead of a max.

```python
# python
# LC 968 - Binary Tree Cameras
# IDEA: post-order DFS returning one of 3 states; the parent makes the decision
# time = O(n), space = O(h)
def minCameraCover(root):
    UNCOVERED, CAMERA, COVERED = 0, 1, 2
    cameras = 0

    def dfs(node):
        nonlocal cameras
        if not node:
            return COVERED                 # so a leaf reports UNCOVERED and its parent acts
        left = dfs(node.left)
        right = dfs(node.right)
        if left == UNCOVERED or right == UNCOVERED:   # test this FIRST
            cameras += 1
            return CAMERA
        if left == CAMERA or right == CAMERA:
            return COVERED
        return UNCOVERED                   # decide nothing: the parent places the camera

    if dfs(root) == UNCOVERED:             # the root has no parent to defer to
        cameras += 1
    return cameras
```

```java
// java
// LC 968 - Binary Tree Cameras
// IDEA: post-order, 3 states; an uncovered child forces a camera here
// time = O(n), space = O(h)
private static final int UNCOVERED = 0, CAMERA = 1, COVERED = 2;
private int cameras = 0;

public int minCameraCover(TreeNode root) {
    cameras = 0;
    if (dfs(root) == UNCOVERED) {
        cameras++;                              // the root has no parent to defer to
    }
    return cameras;
}

private int dfs(TreeNode node) {
    if (node == null) {
        return COVERED;
    }
    int left = dfs(node.left);
    int right = dfs(node.right);
    if (left == UNCOVERED || right == UNCOVERED) {
        cameras++;
        return CAMERA;
    }
    if (left == CAMERA || right == CAMERA) {
        return COVERED;
    }
    return UNCOVERED;
}
```

**Dry run** — `root = [1,2,3,4,5,null,null,6,7]`

```text
        1
       / \
      2   3
     / \
    4   5
   / \
  6   7

post-order: 6, 7, 4, 5, 2, 3, 1
  6, 7 : both children null (COVERED)  -> UNCOVERED
  4    : left UNCOVERED                -> camera #1, CAMERA
  5    : leaf                          -> UNCOVERED
  2    : left CAMERA, right UNCOVERED  -> the uncovered test wins: camera #2, CAMERA
  3    : leaf                          -> UNCOVERED
  1    : right UNCOVERED               -> camera #3, CAMERA
answer 3
```

**Traps**
- Testing "a child has a camera" before "a child is uncovered": node 2 above returns COVERED and
  leaves 5 with no camera anywhere near it.
- Forgetting the root: `[0]` is a single uncovered leaf with no parent, so the answer is 1, not 0.
- Returning UNCOVERED for null puts a camera on every leaf; null must be the covered-without-a-camera state.

## Structural Modification Examples

### Invert Binary Tree — LC 226

> **Key point** — inverting a tree is swapping the two child **pointers** at every node; the
> recursion returns the inverted subtree and the parent reattaches it, so both results must be
> computed before either pointer is overwritten.

**Reduces to**: [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — return the rebuilt subtree and reassign; nothing is deleted.

```python
# python
# LC 226 - Invert Binary Tree
# IDEA: post-order; invert both subtrees, then swap them
# time = O(n), space = O(h)
def invertTree(root):
    if not root:
        return None
    left = invertTree(root.left)           # compute BOTH before assigning either
    right = invertTree(root.right)
    root.left = right
    root.right = left
    return root
```

```java
// java
// LC 226 - Invert Binary Tree
// IDEA: post-order; cache both inverted subtrees, then swap
// time = O(n), space = O(h)
public TreeNode invertTree(TreeNode root) {
    if (root == null) {
        return null;
    }
    TreeNode left = invertTree(root.left);     // cache both results first
    TreeNode right = invertTree(root.right);
    root.left = right;
    root.right = left;
    return root;
}
```

**Traps**
- `root.left = invertTree(root.right); root.right = invertTree(root.left);` — the second line reads
  the left pointer the first line just overwrote, so `[2,1,3]` comes back as `[2,3,3]`-shaped
  garbage. Python's `root.left, root.right = f(root.right), f(root.left)` is safe only because the
  right-hand side is evaluated in full first.

### Flatten Binary Tree to Linked List — LC 114

> **Key point** — each call flattens its subtree and returns the subtree's **tail** (its last node in
> pre-order); the parent then splices `node → left chain → right chain` in O(1) by hanging the
> original right subtree off the left chain's tail, instead of walking down to find it.

**Reduces to**: [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — the post-order rewire, returning the tail rather than the new root.

```python
# python
# LC 114 - Flatten Binary Tree to Linked List
# IDEA: post-order; flatten both sides, splice node -> left chain -> right chain, return the tail
# time = O(n), space = O(h)
def flatten(root):
    def flatten_tail(node):                # flatten node's subtree, return its LAST node
        if not node:
            return None
        left_tail = flatten_tail(node.left)
        right_tail = flatten_tail(node.right)
        if left_tail:                      # splice: node -> left chain -> right chain
            left_tail.right = node.right
            node.right = node.left
            node.left = None
        if right_tail:                     # the tail is the last non-empty box
            return right_tail
        if left_tail:
            return left_tail
        return node

    flatten_tail(root)
```

**Dry run** — `root = [1,2,5,3,4,null,6]`

```text
flatten_tail(3) -> 3              flatten_tail(4) -> 4
flatten_tail(2): left_tail 3, right_tail 4
                 3.right = 4, 2.right = 3, 2.left = None   => 2 -> 3 -> 4,  return 4
flatten_tail(6) -> 6
flatten_tail(5): no left; right_tail 6                     => 5 -> 6,       return 6
flatten_tail(1): left_tail 4, right_tail 6
                 4.right = 5, 1.right = 2, 1.left = None   => 1 -> 2 -> 3 -> 4 -> 5 -> 6
```

**Traps**
- Writing `node.right = node.left` **before** `left_tail.right = node.right` loses the original
  right subtree: read it first, or cache it.
- The tail is looked up right → left → node, the reverse of the traversal: the chain ends with the
  right part if there is one. A node with a right child but no left still returns `right_tail`.
- Finding the tail by walking `while cur.right` at every node instead of returning it is O(n·h),
  O(n²) on a skewed tree.

**Not this if** — the follow-up asks for O(1) extra space: that is the Morris rewire in
[tree.md § Template 1](./tree.md#template-1-morris-in-order-traversal--lc-94), which threads each
left subtree's rightmost node to the right child without recursion.

## Two Trees in Lockstep Examples

### Symmetric Tree — LC 101

> **Key point** — a tree is symmetric when its left and right subtrees are **mirrors**, which is LC
> 100 on two subtrees with the pairing crossed: outer with outer (`a.left`, `b.right`), inner with
> inner (`a.right`, `b.left`).

**Reduces to**: [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — one tree compared with itself, children paired crosswise. It is worked in one place, recursive and iterative, with a trace and the straight-pairing bug: [dfs.md § LC 101](./dfs.md#symmetric-tree--lc-101).

**Traps**
- Comparing values level by level and checking each level is a palindrome misses the shape:
  `[1,2,2,null,3,null,3]` has palindromic values per level once nulls are dropped, and is not symmetric.
- Pairing `a.left` with `b.left` checks equality, not symmetry.

### Merge Two Binary Trees — LC 617

> **Key point** — where both trees have a node, the merged node is their sum; where only one does,
> that whole subtree is reused as it is, so the recursion stops there instead of copying it.

**Reduces to**: [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — the lockstep walk returns a new node instead of a boolean.

```python
# python
# LC 617 - Merge Two Binary Trees
# IDEA: walk both trees in lockstep; sum where both exist, reuse the lone subtree otherwise
# time = O(min(n1, n2)), space = O(min(h1, h2))
def mergeTrees(root1, root2):
    if not root1:
        return root2                       # reuse the other subtree as it is
    if not root2:
        return root1
    merged = TreeNode(root1.val + root2.val)
    merged.left = mergeTrees(root1.left, root2.left)
    merged.right = mergeTrees(root1.right, root2.right)
    return merged
```

**Traps**
- Recursing into the lone subtree to copy it costs O(n1 + n2) instead of the overlap only;
  returning it as is is the point.
- Writing the sum into `root1` and returning it (as many solutions do) is also accepted, but it
  mutates the input; say which one you are doing.

### Flip Equivalent Binary Trees — LC 951

> **Key point** — at every node the children may have been swapped, so accept either pairing: the
> lockstep check with `left-left / right-right` **or** `left-right / right-left`.

**Reduces to**: [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — two pairings tried at each node, joined with `or`.

```python
# python
# LC 951 - Flip Equivalent Binary Trees
# IDEA: Same Tree, but the children pair may also be matched crosswise
# time = O(n), space = O(h)   values are unique, so at most one pairing can match at each node
def flipEquiv(root1, root2):
    if not root1 or not root2:
        return root1 is root2
    if root1.val != root2.val:
        return False
    same = flipEquiv(root1.left, root2.left) and flipEquiv(root1.right, root2.right)
    flipped = flipEquiv(root1.left, root2.right) and flipEquiv(root1.right, root2.left)
    return same or flipped
```

**Traps**
- The O(n) bound depends on the values being unique: with duplicates, both pairings can recurse
  deep and the worst case is worse than linear.
- Normalising both trees first (sort each node's children by value) also works here, but only
  because the values are unique; the two-way check is the general form.

### Reverse Odd Levels of Binary Tree — LC 2415

> **Key point** — in a **perfect** tree, the node at position `i` of a level and the node at its
> mirror position are reached by walking the two halves in lockstep, outer with outer and inner with
> inner, exactly as LC 101 pairs them; reversing a level is swapping the values of each such pair.

**Reduces to**: [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — the LC 101 mirror pairing, swapping values on odd levels instead of comparing them.

```python
# python
# LC 2415 - Reverse Odd Levels of Binary Tree
# IDEA: walk mirror pairs in lockstep; swap their values on odd levels
# time = O(n), space = O(log n)   the tree is perfect, so its height is log n
def reverseOddLevels(root):
    def pair(a, b, level):                 # a and b are mirror positions on the same level
        if not a:                          # perfect tree: a and b are empty together
            return
        if level % 2 == 1:
            a.val, b.val = b.val, a.val
        pair(a.left, b.right, level + 1)   # outer pair
        pair(a.right, b.left, level + 1)   # inner pair

    pair(root.left, root.right, 1)
    return root
```

**Traps**
- Swapping nodes instead of values means relinking parents on both sides; swapping `val` is all the
  problem needs.
- Pairing `a.left` with `b.left` maps position `i` to `i + half`, not to its mirror: a level holding
  `1 … 8` comes out `5 6 7 8 1 2 3 4` instead of `8 7 6 5 4 3 2 1`.

**Not this if** — the tree is not perfect, or you prefer the obvious route: a level-order BFS that
collects each odd level and reverses its values ([binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102)), at O(w) space.
