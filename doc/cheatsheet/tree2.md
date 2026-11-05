# Tree Pattern Templates — Index and Traversal Variants

> **Scope** — An index from every tree pattern to the one card that teaches it, plus the traversal forms no core sheet spells out: iterative pre- and post-order, zigzag levels, the bottom-left value and vertical order.
> **See also**: [binary_tree.md](./binary_tree.md) — the binary-tree templates, one card each; [tree.md](./tree.md) — concepts, tree types, when to use which traversal; [tree_lca_distance.md](./tree_lca_distance.md) — LCA, node distance and root-to-leaf paths; [tree_examples.md](./tree_examples.md) — the worked problems; [tree_construction.md](./tree_construction.md) and [tree_codec.md](./tree_codec.md) — building a tree from an encoding, and serialising it back; [bst.md](./bst.md) — ordered trees.

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## Overview

This sheet used to hold its own copy of every tree template, next to the copies in
[binary_tree.md](./binary_tree.md), [tree_lca_distance.md](./tree_lca_distance.md) and
[tree_examples.md](./tree_examples.md), and the copies drifted apart. Each pattern now has one
home, and every section below says where it is. What stays here is the handful of forms that
have no other home: the explicit-stack pre- and post-order walks, the zigzag BFS, the
bottom-left value, and the sort-by-coordinate vertical order.

---

## Recognition

Only the forms this sheet still owns; every other tree signal is in [binary_tree.md](./binary_tree.md#recognition)'s table.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "preorder traversal", and the follow-up "iteratively" | n ≤ 100 | [1.1)](#11-preorder-template--lc-144) one stack, right child pushed first | in-order without recursion → [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) |
| "postorder traversal", "iteratively" | n ≤ 100 | [1.3)](#13-postorder-template--lc-145) two stacks, or a reversed root-right-left walk | the order does not matter, only the visit → any traversal |
| "zigzag level order", "alternate left-to-right and right-to-left" | n ≤ 2000 | [1.5)](#15-bfs--direction-template--lc-103) level BFS, flip every other level | every level in one direction → [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) |
| "leftmost value in the last row" | n ≤ 10⁴ | [2.7)](#27-leftmost-value-at-maximum-depth--lc-513) index 0 of every level | the rightmost value of **every** level (LC 199) → [tree_examples.md](./tree_examples.md#binary-tree-right-side-view--lc-199) |
| "vertical order", ties broken by value | n ≤ 1000 | [8.3)](#83-coordinate-map-traversal-template--lc-987) collect `(col, row, val)`, sort | ties kept in BFS order (LC 314) → BFS grouping by column, no sort |

## 1) Tree Traversal Templates

| What this section used to hold | Where it lives now |
|---|---|
| 1.2) Inorder Template — LC 94 | [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) (recursive) and [§ Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) (the stack form); the O(1)-space Morris walk is [tree.md § Template 1](./tree.md#template-1-morris-in-order-traversal--lc-94) |
| 1.4) BFS Template (Level-order) — LC 102 | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — the queue-length snapshot |

### 1.1) Preorder Template — LC 144

**Pattern**: Root → Left → Right
**Use Case**: When you need parent data before processing children
**Time Complexity**: O(n)
**Space Complexity**: O(h) for recursion stack

#### Template Code

```python
# Python - Recursive
def preorder_traversal(root):
    result = []

    def preorder(node):
        if not node:
            return

        # Process root first
        result.append(node.val)

        # Then left subtree
        preorder(node.left)

        # Then right subtree
        preorder(node.right)

    preorder(root)
    return result

# Python - Iterative
def preorder_iterative(root):
    if not root:
        return []

    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)

        # Add right first (LIFO - will process left first)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)

    return result
```

```java
// Java - Recursive
public void preorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    result.add(root.val);              // Process root
    preorderTraversal(root.left, result);   // Left subtree
    preorderTraversal(root.right, result);  // Right subtree
}

// Java - Iterative
public List<Integer> preorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;

    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);

        if (node.right != null) stack.push(node.right);
        if (node.left != null) stack.push(node.left);
    }

    return result;
}
```

#### LeetCode Problems
- LC 144: Binary Tree Preorder Traversal (Easy)
- LC 589: N-ary Tree Preorder Traversal (Easy)

---

### 1.3) Postorder Template — LC 145

**Pattern**: Left → Right → Root
**Use Case**: Need children data before parent processing
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Recursive
def postorder_traversal(root):
    result = []

    def postorder(node):
        if not node:
            return

        # Left subtree first
        postorder(node.left)

        # Right subtree
        postorder(node.right)

        # Process root last
        result.append(node.val)

    postorder(root)
    return result

# Python - Iterative (Two Stacks)
def postorder_iterative(root):
    if not root:
        return []

    stack1 = [root]
    stack2 = []

    # Collect nodes in reverse postorder
    while stack1:
        node = stack1.pop()
        stack2.append(node)

        if node.left:
            stack1.append(node.left)
        if node.right:
            stack1.append(node.right)

    # Pop from stack2 to get postorder
    result = []
    while stack2:
        result.append(stack2.pop().val)

    return result
```

```java
// Java - Recursive
public void postorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    postorderTraversal(root.left, result);   // Left subtree
    postorderTraversal(root.right, result);  // Right subtree
    result.add(root.val);                    // Current node
}
```

#### LeetCode Problems
- LC 145: Binary Tree Postorder Traversal (Easy)
- LC 590: N-ary Tree Postorder Traversal (Easy)

---

### 1.5) BFS + Direction Template — LC 103

**Pattern**: Alternating direction per level
**Use Case**: Zigzag traversal
**Time Complexity**: O(n)
**Space Complexity**: O(w)

#### Template Code

```python
# Python - Zigzag Level Order
from collections import deque

def zigzag_level_order(root):
    if not root:
        return []

    result = []
    queue = deque([root])
    left_to_right = True

    while queue:
        level_size = len(queue)
        current_level = deque()

        for _ in range(level_size):
            node = queue.popleft()

            # Add to level based on direction
            if left_to_right:
                current_level.append(node.val)
            else:
                current_level.appendleft(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(list(current_level))
        left_to_right = not left_to_right

    return result
```

```java
// Java - Zigzag Level Order
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    boolean leftToRight = true;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        LinkedList<Integer> currentLevel = new LinkedList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (leftToRight) {
                currentLevel.addLast(node.val);
            } else {
                currentLevel.addFirst(node.val);
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(currentLevel);
        leftToRight = !leftToRight;
    }

    return result;
}
```

#### LeetCode Problems
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)

---

## 2) Tree Property Templates

| What this section used to hold | Where it lives now |
|---|---|
| 2.1) Postorder Height Template — LC 104 | [Maximum Depth of Binary Tree — LC 104](./tree_examples.md#maximum-depth-of-binary-tree--lc-104) |
| 2.2) BFS Early Stop Template — LC 111 | [Minimum Depth of Binary Tree — LC 111](./tree_examples.md#minimum-depth-of-binary-tree--lc-111) — the first leaf a BFS pops |
| 2.3) Height Validation Template — LC 110 | [Balanced Binary Tree — LC 110](./tree_examples.md#balanced-binary-tree--lc-110) — return `-1` as the unbalanced signal |
| 2.4) Mirror Validation Template — LC 101 | [Symmetric Tree — LC 101](./tree_examples.md#symmetric-tree--lc-101) |
| 2.5) Tree Comparison Template — LC 100 | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — two trees in lockstep |
| 2.6) Minimum Depth — the recursive form and its single-child trap | the same LC 111 card, whose traps cover the one-child node |

### 2.7) Leftmost Value at Maximum Depth — LC 513

> **Key point** — a level-order BFS meets the levels top to bottom and each level left to right, so
> the first node popped on each level is that level's leftmost; the last value recorded that way is
> the bottom-left one.

**Reduces to**: [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — the queue-length snapshot, recording index 0 of
every level instead of the whole level.

```python
# python
# LC 513 - Find Bottom Left Tree Value
# IDEA: level-order BFS; the first node of each level overwrites the answer, so the last level wins
# time = O(n), space = O(w)   w = maximum width of a level
from collections import deque

def find_bottom_left_value(root):
    queue = deque([root])
    leftmost = root.val
    while queue:
        level_size = len(queue)
        for i in range(level_size):
            node = queue.popleft()
            if i == 0:                     # first node of this level
                leftmost = node.val
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    return leftmost
```

```java
// java
// LC 513 - Find Bottom Left Tree Value
// IDEA: level-order BFS; record index 0 of every level
// time = O(n), space = O(w)
public int findBottomLeftValue(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int leftmost = root.val;
    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            if (i == 0) {
                leftmost = node.val;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
    return leftmost;
}
```

**Traps**
- "Leftmost" is per level, not "keep going left": in `[1,2,3,4,null,5,6,null,null,7]` the answer 7
  sits under the **right** child.
- Enqueue right before left and the **last** node popped is the bottom-left one: a one-variable
  version, but the order is the whole trick, so say it out loud.

---

## 3) Path-Based Templates

Every path template moved to the sheet that owns paths, or to the height card the bent-path
problems reduce to:

| What this section used to hold | Where it lives now |
|---|---|
| 3.1) Global Max Update Template — LC 124 | [Binary Tree Maximum Path Sum — LC 124](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124) |
| 3.2) Path Accumulation Template — LC 112 | [Path Sum — LC 112](./tree_lca_distance.md#path-sum--lc-112) |
| 3.3) Path + Backtrack Template — LC 113 | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) |
| 3.4) Path Count Tracking Template — LC 437 | [tree_lca_distance § Template 5](./tree_lca_distance.md#template-5-downward-path-count-with-a-prefix-sum-map--lc-437) |
| 3.5) Path Value Building Template — LC 129 | [Sum Root to Leaf Numbers — LC 129](./tree_lca_distance.md#sum-root-to-leaf-numbers--lc-129) |
| 3.6) Path State Tracking Template — LC 1448 | [Count Good Nodes in Binary Tree — LC 1448](./tree_examples.md#count-good-nodes-in-binary-tree--lc-1448) |
| 3.7) Longest Path Template — LC 543 | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) |
| 3.8) Same Value Path Template — LC 687 | [Longest Univalue Path — LC 687](./tree_lca_distance.md#longest-univalue-path--lc-687) |

---

## 4) Distance and LCA Templates

These four templates moved out of this sheet. [tree_lca_distance.md](./tree_lca_distance.md)
owns them and teaches each one at several times the length this sheet had room for:

| What this section used to hold | Where it lives now |
|---|---|
| 4.1) LCA Standard Template — LC 236 | [LCA — LC 236](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236), plus the LC 865 / 1123 deepest-nodes variant |
| 4.2) Value Comparison Template — LC 235 | the same section — LC 235 is the BST shortcut on the same template |
| 4.3) Path Distance Template — LC 1740 | [Distance Between Nodes — LC 1740](./tree_lca_distance.md#template-3-distance-between-nodes-via-lca--lc-1740) |
| 4.4) Tree to Graph Template — LC 863 | [Move Parent Pattern](./tree_lca_distance.md#template-2-parent-map--bfs--lc-863), which is the general form — LC 863 and LC 742 are both instances |

**The idea worth carrying away from here**: every "distance" question on a tree is an LCA
question in disguise, because the only path between two nodes runs through their lowest common
ancestor — `dist(p, q) = depth(p) + depth(q) - 2·depth(lca)`. When you also need to walk
*upward*, add parent pointers and treat the tree as an undirected graph.


## 6) Tree Construction Templates

Construction moved out of this sheet as well; two Tier 1 sheets own it between them:

| What this section used to hold | Where it lives now |
|---|---|
| 6.1) Tree Building Template — LC 105 / 106 | [tree_construction.md](./tree_construction.md#2-construct-binary-tree-from-preorder-and-inorder-traversal--lc-105) — this sheet's Java template and its LC 106 post-order variant were merged in |
| 6.2) String Conversion Template — LC 297 | [tree_codec.md](./tree_codec.md) — the whole codec family, LC 297 / 449 / 331 |
| 6.3) String Construction Template — LC 606 | [tree_codec.md](./tree_codec.md) — the parenthesis format and the pair-omission rule |

**The idea worth carrying away from here**: every construction problem is the same recursion —
identify the root from the encoding, work out how much of the input belongs to each subtree, and
recurse. Only the first step differs: the pre-order head, the post-order tail, the maximum of a
range, or the token before the first `(`.


## 7) Tree Modification Templates

| What this section used to hold | Where it lives now |
|---|---|
| 7.1) Tree Inversion Template — LC 226 | [Invert Binary Tree — LC 226](./tree_examples.md#invert-binary-tree--lc-226) |
| 7.2) Tree Flattening Template — LC 114 | [Flatten Binary Tree to Linked List — LC 114](./tree_examples.md#flatten-binary-tree-to-linked-list--lc-114) |
| 7.3) Tree Merging Template — LC 617 | [Merge Two Binary Trees — LC 617](./tree_examples.md#merge-two-binary-trees--lc-617) |
| Deleting nodes and returning the forest — LC 1110 | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) |

---

## 8) Advanced Tree Templates

| What this section used to hold | Where it lives now |
|---|---|
| 8.1) O(1)-Space Level Linking Template — LC 117 | [binary_tree § Template 8](./binary_tree.md#template-8-level-linking-with-o1-space--lc-117-) |
| 8.2) Postorder Tree DP (Pair Return) Template — LC 337 | [binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) |
| 8.4) Complete Tree Node Count Template — LC 222 | [binary_tree § Template 7](./binary_tree.md#template-7-count-nodes-of-a-complete-tree--lc-222) |

### 8.3) Coordinate Map Traversal Template — LC 987

> **Key point** — give every node a coordinate (`left → col - 1`, `right → col + 1`, depth → `row`)
> and the output order is a plain sort by `(col, row, val)`. A BFS alone is not enough: two nodes
> can share `(col, row)`, and the tie is broken by **value**, so collect first, then sort.

**Reduces to**: [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) —
any traversal that carries `(row, col)` down, followed by one sort.

```python
# python
# LC 987 - Vertical Order Traversal of a Binary Tree
# IDEA: DFS collecting (col, row, val); plain tuple sort gives col → row → val
# time = O(N log N), space = O(N)
def vertical_traversal(root):
    nodes = []

    def dfs(node, row, col):
        if not node:
            return
        nodes.append((col, row, node.val))
        dfs(node.left,  row + 1, col - 1)
        dfs(node.right, row + 1, col + 1)

    dfs(root, 0, 0)
    nodes.sort()

    res, prev_col = [], None
    for col, row, val in nodes:
        if col != prev_col:
            res.append([])
            prev_col = col
        res[-1].append(val)
    return res
```
```java
// java
// LC 987 - Vertical Order Traversal of a Binary Tree
// IDEA: DFS collecting (col, row, val) triples, then sort col → row → val
// time = O(N log N), space = O(N)
public List<List<Integer>> verticalTraversal(TreeNode root) {
    List<int[]> nodes = new ArrayList<>();   // {col, row, val}
    dfs(root, 0, 0, nodes);

    nodes.sort((a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0])
                       : a[1] != b[1] ? Integer.compare(a[1], b[1])
                       : Integer.compare(a[2], b[2]));

    List<List<Integer>> res = new ArrayList<>();
    Integer prevCol = null;
    for (int[] n : nodes) {
        if (prevCol == null || n[0] != prevCol) {
            res.add(new ArrayList<>());
            prevCol = n[0];
        }
        res.get(res.size() - 1).add(n[2]);
    }
    return res;
}

private void dfs(TreeNode node, int row, int col, List<int[]> nodes) {
    if (node == null) return;
    nodes.add(new int[]{col, row, node.val});
    dfs(node.left,  row + 1, col - 1, nodes);
    dfs(node.right, row + 1, col + 1, nodes);
}
```
**Traps**
- Grouping by column with a BFS and no sort: LC 987 example 3 has 5 and 6 at the same `(col, row)`,
  and they must come out as `[1,5,6]`, sorted by value, not in visiting order.
- LC 314 (vertical order, no value tie-break) wants BFS order within a column, so there the sort
  key is `(col, BFS index)`, not `(col, row, val)`.

---

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** | LC 144 · LC 145 · LC 103 | the two explicit-stack walks, then a level BFS whose direction flips |
| **Practice** | LC 513 · LC 589 · LC 590 | the last level's first node; the same stack walks on an N-ary tree |
| **Stretch** | LC 987 · LC 314 | coordinates, with ties broken by value, then by BFS order |
