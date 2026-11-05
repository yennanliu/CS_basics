# BST (Binary Search Tree)

> **Scope** — Ordered trees only — what the `left < root < right` invariant buys you (O(log n) search, sorted inorder, range pruning, order statistics).

> **See also**: [bst_examples.md](./bst_examples.md) — the BST problems that add one step to these templates; [bst_advanced.md](./bst_advanced.md) — the lazy O(h) iterator, recovering a broken BST, order-statistic (rank) queries and the construction variants; [tree_lca_distance.md](./tree_lca_distance.md) — path-sum problems, which need no ordering; [binary_tree.md](./binary_tree.md) — unordered binary trees; [tree.md](./tree.md) — general tree concepts; [segment_tree.md](./segment_tree.md) — range queries over an array instead of a tree.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| BST (average)  | O(log n) | O(log n) | O(log n) | O(log n) |

> Average case shown (reasonably balanced tree). **Worst case (unbalanced / skewed): all operations O(n).** Min/Max = leftmost / rightmost node, i.e. O(h). Full traversal is always **O(n)**. Space is **O(n)** for storage plus **O(h)** for recursion.

## Overview
**Binary Search Tree (BST)** is a binary tree data structure where each node follows the ordering property: left child < parent < right child. This property enables efficient searching, insertion, and deletion operations.

### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Property**: `left < root < right` for all nodes
- **Inorder Traversal**: Produces sorted sequence (ascending order)
- **When to Use**: Sorted data operations, range queries, ordered statistics

### References
- [BST Visualizer](https://www.cs.usfca.edu/~galles/visualization/BST.html)
- [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/43/)
- [fucking-algorithm - BST pt.2](https://labuladong.github.io/algo/2/21/44/)
- [fucking-algorithm - BST pt.3](https://labuladong.github.io/algo/2/21/42/)

### Basic Operations on a Tree Node

The plain recursion every BST template is built on. Neither of these uses the ordering
property — this is the shape you fall back to when `left < root < right` does not help.

#### **Mutate every node — plain pre-order recursion**
```python
# python
# IDEA: change the node, then recurse into both children; no ordering is used
# time = O(n), space = O(h)
def add_one(root):
    if not root:
        return
    root.val += 1
    add_one(root.left)
    add_one(root.right)
```

```java
// java
// IDEA: change the node, then recurse into both children
// time = O(n), space = O(h)
void addOne(TreeNode root) {
    if (root == null) {
        return;
    }
    root.val += 1;
    addOne(root.left);
    addOne(root.right);
}
```

Comparing two trees node by node (LC 100) is the same recursion over two trees at once; it is
worked in [binary_tree.md § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100).

## Recognition

The constraint cues come from the ruler in [complexity_cheatsheet.md](./complexity_cheatsheet.md).
Every row assumes the statement says **binary search tree**; without that word, none of these
cards apply.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "search / find / closest value in a BST" | O(h) expected | [Template 1](#template-1-descend-by-comparison--lc-700) descend by comparison | the tree is unordered → full DFS ([binary_tree.md](./binary_tree.md)) |
| "insert a value and return the root" | | [Template 2](#template-2-insert-at-the-null-leaf--lc-701) insert at the null leaf | |
| "delete the node with key k" | | [Template 3](#template-3-delete-with-successor-swap--lc-450) successor swap | a whole range goes → Template 3b |
| "keep only values in `[low, high]`", "range sum of a BST" | | [Template 3b](#template-3b-trim-to-a-range--lc-669-) trim to a range | |
| "is this a valid BST" | values reach `Integer.MIN_VALUE` / `MAX_VALUE` | [Template 4](#template-4-validate-with-bounds--lc-98) bounds carried down | the two swapped nodes are wanted (LC 99) → in-order + `prev` ([bst_advanced.md](./bst_advanced.md)) |
| "kth smallest", "iterator", "minimum difference", "mode" | | [Template 5](#template-5-in-order-walk-is-sorted--lc-230) in-order is sorted | kth asked repeatedly under updates → size-augmented BST ([bst_advanced.md](./bst_advanced.md)) |
| "sorted array → height-balanced BST", "balance this BST" | | [Template 6](#template-6-sorted-array-to-balanced-bst--lc-108) middle as root | the input is a linked list (LC 109) → [bst_advanced.md](./bst_advanced.md) |
| "lowest common ancestor" in a BST | nodes ≤ 10⁵ | [Template 10](#template-10-lca-via-bst-ordering--lc-235-) walk down to the split | plain binary tree, or parent pointers only → [tree_lca_distance.md](./tree_lca_distance.md) |
| "path sum", "root to leaf" | | not a BST problem → [tree_lca_distance.md](./tree_lca_distance.md) | |

## Problem Categories

### **Pattern 1: BST Search & Validation**
- **Description**: Find elements or validate BST properties
- **Recognition**: "Search", "find", "validate", "is valid BST"
- **Examples**: LC 98, LC 700, LC 270, LC 285
- **Template**: Use Search Template with BST property

### **Pattern 2: BST Insertion & Deletion**
- **Description**: Modify BST structure while maintaining properties
- **Recognition**: "Insert", "delete", "remove", "add node"
- **Examples**: LC 450, LC 701, LC 669
- **Template**: Use Modification Template

### **Pattern 3: BST Traversal & Conversion**
- **Description**: Use inorder property for sorting/conversion
- **Recognition**: "Kth smallest", "convert", "flatten", "sorted order"
- **Examples**: LC 230, LC 173, LC 426, LC 538
- **Template**: Use Inorder Template

### **Pattern 4: BST Construction**
- **Description**: Build BST from various inputs or rebuild/balance existing BST
- **Recognition**: "Construct", "build", "generate", "serialize", "balance"
- **Examples**: LC 108, LC 109, LC 95, LC 96, LC 449, LC 1008, LC 1382
- **Template**: Use Construction Template

### **Pattern 5: BST Properties & Optimization**
- **Description**: Find optimal values or properties in BST
- **Recognition**: "Closest", "LCA", "range", "distance"
- **Examples**: LC 235, LC 530, LC 783, LC 776
- **Template**: Use Property Template

### **Pattern 6: Path Problems**
- **Description**: Problems involving root-to-leaf or node-to-node paths
- **Recognition**: "Path sum", "root to leaf", "maximum path", "consecutive sequence"
- **Examples**: LC 112, LC 113, LC 257, LC 124, LC 129, LC 298, LC 437
- **Template**: none here — no ordering is used; see [tree_lca_distance.md](./tree_lca_distance.md)

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Key Operation | Time | Space | When to Use |
|---------------|----------|---------------|------|-------|-------------|
| **Search Template** | Finding values | Binary search | O(log n) | O(1)/O(h) | Value lookup |
| **Insertion Template** | Adding nodes | Find position + insert | O(log n) | O(1)/O(h) | Adding new values |
| **Deletion Template** | Removing nodes | Find + restructure | O(log n) | O(h) | Removing values |
| **Inorder Template** | Sorted operations | Left-root-right | O(n) | O(h) | Kth element, range |
| **Construction Template** | Building BST | Divide & conquer | O(n) | O(n) | Creating from array |

> **Where the other templates live.** Templates 7–9 were moved out, so the numbering on this
> page skips from 6 to 10. The lazy iterator, recovering a broken BST, rank queries and the
> construction variants are [bst_advanced.md](./bst_advanced.md) Templates 1–5; the path
> problems are in [tree_lca_distance.md](./tree_lca_distance.md).

### Template 1: Descend by Comparison — LC 700

**Recognise** — "search / find / closest value in a **BST**", "does `target` exist", "floor /
ceiling of x". Nodes ≤ 5,000 and the tree is a BST → O(h), not O(n), is expected.

**Key point** — every node splits the remaining values in two: anything `< node.val` can only be
on the left, anything `>` only on the right. So one comparison discards a whole subtree, and the
walk is a single root-to-leaf path.

```python
# python
# LC 700 - Search in a Binary Search Tree
# IDEA: compare, then step to the ONE child that can still hold val
# time = O(h), space = O(1)
def searchBST(root, val):
    node = root
    while node and node.val != val:
        node = node.left if val < node.val else node.right
    return node            # the matching node, or None when the walk falls off
```

```java
// java
// LC 700 - Search in a Binary Search Tree
// IDEA: compare, then step to the ONE child that can still hold val
// time = O(h), space = O(1)
public TreeNode searchBST(TreeNode root, int val) {
    TreeNode node = root;
    while (node != null && node.val != val) {
        node = (val < node.val) ? node.left : node.right;
    }
    return node;
}
```

**Complexity**
- **Time O(h)**: the `while` takes one step down per iteration and never comes back up, so it
  runs at most h times: O(log n) balanced, O(n) on a skewed (sorted-insert) tree.
- **Space O(1)**: the loop keeps one pointer. The recursive spelling costs O(h) of call stack.

**Traps**
- Searching **both** children (`search(left) or search(right)`) still returns the right answer on
  `[4,2,7,1,3]`, but in O(n): it is the plain binary-tree scan and throws the BST away.
- Promising O(log n) without saying "if balanced": a tree built by inserting `1, 2, 3, …, n` is a
  linked list, and every template here is O(n) on it.

**Not this if** — the tree is not ordered (a plain binary tree) → a full DFS is the only option
([binary_tree.md](./binary_tree.md)).

**Drill** — LC 700 · LC 270 *(closest: track the best on the way down)* · LC 285 *(successor:
remember the last node you went left from)*

### Template 2: Insert at the Null Leaf — LC 701

**Recognise** — "insert into a BST", "add a value and return the root", "build a BST by inserting
these values in order".

**Key point** — the new value belongs exactly where Template 1's search for it falls off the tree.
Returning the (possibly new) child and assigning it back — `root.left = insert(root.left, val)` —
reconnects the path without ever holding a parent pointer.

```python
# python
# LC 701 - Insert into a Binary Search Tree
# IDEA: search for val; the null you fall off is where the new leaf goes
# time = O(h), space = O(h)
def insertIntoBST(root, val):
    if not root:
        return TreeNode(val)              # the empty spot: the new leaf
    if val < root.val:
        root.left = insertIntoBST(root.left, val)
    else:
        root.right = insertIntoBST(root.right, val)
    return root                           # unchanged nodes return themselves
```

```java
// java
// LC 701 - Insert into a Binary Search Tree
// IDEA: search for val; the null you fall off is where the new leaf goes
// time = O(h), space = O(h)
public TreeNode insertIntoBST(TreeNode root, int val) {
    if (root == null) {
        return new TreeNode(val);
    }
    if (val < root.val) {
        root.left = insertIntoBST(root.left, val);
    } else {
        root.right = insertIntoBST(root.right, val);
    }
    return root;
}
```

**Complexity**
- **Time O(h)**: one recursive call per level, on one side only.
- **Space O(h)**: the call stack holds the root-to-leaf path. A `while` loop with a `parent`
  pointer makes it O(1).

**Traps**
- Forgetting `return root` at the end: every ancestor returns `None` and the caller gets an empty
  tree back. `[4,2,7,1,3]`, val = 5 returns `[]`.
- Calling `insertIntoBST(root.left, val)` without assigning the result: the new leaf is created and
  then dropped, and the tree is unchanged.

**Drill** — LC 701 · LC 108 *(insert many, balanced)* · LC 1008 *(insert a preorder, or do it in
O(n) with bounds)*

### Template 3: Delete with Successor Swap — LC 450

**Recognise** — "delete the node with key k and return the root", "remove a value from a BST".

**Key point** — a node with **0 or 1** children is replaced by that child. A node with **2**
children keeps its place in the tree but takes the value of its in-order successor (the minimum of
its right subtree), and that successor, which has no left child, is then deleted from the right
subtree, falling into the easy case.

```python
# python
# LC 450 - Delete Node in a BST
# IDEA: find key; 0/1 child -> return the other child; 2 children -> copy the
#       right subtree's minimum in, then delete that minimum from the right subtree
# time = O(h), space = O(h)
def deleteNode(root, key):
    if not root:
        return None                       # key not in the tree
    if key < root.val:
        root.left = deleteNode(root.left, key)
    elif key > root.val:
        root.right = deleteNode(root.right, key)
    else:
        if not root.left:                 # 0 children, or only a right child
            return root.right
        if not root.right:                # only a left child
            return root.left
        succ = root.right                 # 2 children: in-order successor
        while succ.left:
            succ = succ.left
        root.val = succ.val
        root.right = deleteNode(root.right, succ.val)
    return root
```

```java
// java
// LC 450 - Delete Node in a BST
// IDEA: 0/1 child -> splice in the other child; 2 children -> successor swap
// time = O(h), space = O(h)
public TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) {
        return null;
    }
    if (key < root.val) {
        root.left = deleteNode(root.left, key);
    } else if (key > root.val) {
        root.right = deleteNode(root.right, key);
    } else {
        if (root.left == null) {
            return root.right;
        }
        if (root.right == null) {
            return root.left;
        }
        TreeNode succ = root.right;
        while (succ.left != null) {
            succ = succ.left;
        }
        root.val = succ.val;
        root.right = deleteNode(root.right, succ.val);
    }
    return root;
}
```

**Complexity**
- **Time O(h)**: the search for `key` walks one path down; the successor search and the second
  `deleteNode` continue down the same right subtree, so the total is still one path's length.
- **Space O(h)**: the recursion stack along that path.

**Dry run**

```text
[5,3,6,2,4,null,7], key = 3
at 5: 3 < 5  -> recurse left
at 3: found, two children (2, 4)
      successor = min of right subtree = 4
      copy 4 into this node, delete 4 from its right subtree (a leaf -> None)
result: [5,4,6,2,null,null,7]
```

**Traps**
- `root.val` is overwritten, not the node: if the caller holds a reference to the node with the
  successor's value, it now sees the moved value. When values must stay attached to nodes, splice
  the successor node in instead.
- A missing key must leave the tree unchanged: `key = 0` on `[5,3,6,2,4,null,7]` reaches the
  `if not root` base case and every level returns itself.
- Using the **predecessor** (max of the left subtree) is equally correct; mixing the two in one
  solution is not.
- Swapping `key` down into the successor's place and then recursing into **both** children (a
  common first draft) is correct but O(n): it gives up the single path that makes this O(h).

**Not this if** — the task removes a whole **range** of values → Template 3b trims in one pass,
instead of one delete per value.

**Drill** — LC 450 · LC 669 *(range delete)* · LC 776 *(split instead of delete)*

### Template 3b: Trim to a Range — LC 669 ⭐

**Recognise** — "keep only the values in `[low, high]`", "remove every node outside the range and
return the new root", "sum the values in a range" (LC 938).

**Key point** — if `root.val < low`, the root **and its entire left subtree** are below `low`, so
the answer is the trimmed right subtree; symmetrically for `> high`. An in-range root is kept and
both children are trimmed. Returning the trimmed subtree is what promotes the surviving
descendants into the removed node's place.

```python
# python
# LC 669 - Trim a Binary Search Tree
# IDEA: out-of-range root -> return the trimmed side that can still hold valid values;
#       in-range root -> keep it, trim and reattach both children
# time = O(n), space = O(h)
def trimBST(root, low, high):
    if not root:
        return None
    if root.val < low:                    # root and its left subtree are all too small
        return trimBST(root.right, low, high)
    if root.val > high:                   # root and its right subtree are all too large
        return trimBST(root.left, low, high)
    root.left = trimBST(root.left, low, high)
    root.right = trimBST(root.right, low, high)
    return root
```

```java
// java
// LC 669 - Trim a Binary Search Tree
// IDEA: out-of-range root -> promote the trimmed side; in-range -> trim both children
// time = O(n), space = O(h)
public TreeNode trimBST(TreeNode root, int low, int high) {
    if (root == null) {
        return null;
    }
    if (root.val < low) {
        return trimBST(root.right, low, high);
    }
    if (root.val > high) {
        return trimBST(root.left, low, high);
    }
    root.left = trimBST(root.left, low, high);
    root.right = trimBST(root.right, low, high);
    return root;
}
```

**Complexity**
- **Time O(n)**: each kept node is visited once and each discarded subtree is skipped in one step,
  so no node is visited twice. It is O(n) rather than O(h) because every in-range node must be
  walked to reattach its children.
- **Space O(h)**: the recursion stack.

**Dry run**

```text
low = 1, high = 3

        3                 3
       / \               /
      0   4    trim →   2
       \               /
        2             1
       /
      1

  - 3 in range        -> keep, trim both children
  - 0 < low           -> drop 0 and its (empty) left, return trim(right) = 2
  - 2 in range        -> keep; 1 in range -> keep (leaf)
  - 4 > high          -> drop 4 and its (empty) right, return trim(left) = None
```

**Traps**
- Returning `None` for an out-of-range node drops its in-range descendants too. On the tree above,
  `if root.val < low: return None` at node `0` loses `2` and `1`. Return the trimmed side instead.
- Trimming only the children of an in-range root and forgetting to **assign** the results
  (`trimBST(root.left, …)` on its own) leaves the out-of-range nodes attached.

**Not this if** — the tree is not a BST: the "whole subtree is out of range" step is no longer
true, and each node must be tested on its own (post-order delete, as in LC 1110).

**Drill** — LC 669 · LC 938 *(sum instead of rebuild)* · LC 450 · LC 776 *(split, keep every node)*

#### **Variation: Sum Instead of Restructure — LC 938**

> **Key point** — the same three-way pruning as `trimBST`: an out-of-range root rules out a
> whole side, so only the combine step changes, from reattaching children to adding values.

**Reduces to**: [Template 3b](#template-3b-trim-to-a-range--lc-669-) — sum instead of restructure.

```python
# python
# LC 938 - Range Sum of BST
# IDEA: Template 3b's pruning; add the in-range values instead of reattaching them
# time = O(n), space = O(h)
def rangeSumBST(root, low, high):
    if not root:
        return 0
    if root.val < low:                    # nothing on the left can be in range
        return rangeSumBST(root.right, low, high)
    if root.val > high:                   # nothing on the right can be in range
        return rangeSumBST(root.left, low, high)
    return (root.val
            + rangeSumBST(root.left, low, high)
            + rangeSumBST(root.right, low, high))
```

#### Comparison: Trim vs Split vs Delete

| Operation | LC # | Nodes Removed? | Return Value | Key Difference |
|-----------|------|----------------|--------------|----------------|
| **Trim** | 669 | Yes (out of range) | Single `TreeNode` | Keeps nodes in [L,R], discards rest |
| **Split** | 776 | No | `TreeNode[2]` | Partitions into 2 trees, keeps ALL nodes |
| **Delete** | 450 | Yes (1 node) | Single `TreeNode` | Removes exactly 1 specific node |

### Template 4: Validate with Bounds — LC 98

**Recognise** — "is this a valid BST", "largest subtree that is a BST" (LC 333), "is every value
inside the interval its ancestors allow". Values may be `Integer.MIN_VALUE` / `MAX_VALUE` → the
sentinels need care.

**Key point** — a node is not checked against its parent but against the **open interval** all its
ancestors allow: going left tightens the upper bound to the parent's value, going right tightens
the lower bound. The tree is valid exactly when every node lies inside its interval.

```python
# python
# LC 98 - Validate Binary Search Tree
# IDEA: carry the (low, high) interval down; None means "no bound on this side"
# time = O(n), space = O(h)
def isValidBST(root):
    def valid(node, low, high):
        if not node:
            return True
        if (low is not None and node.val <= low) or (high is not None and node.val >= high):
            return False
        return valid(node.left, low, node.val) and valid(node.right, node.val, high)
    return valid(root, None, None)
```

```java
// java
// LC 98 - Validate Binary Search Tree
// IDEA: carry the (low, high) interval down; null means "no bound", so MIN_VALUE stays legal
// time = O(n), space = O(h)
public boolean isValidBST(TreeNode root) {
    return valid(root, null, null);
}

private boolean valid(TreeNode node, Integer low, Integer high) {
    if (node == null) {
        return true;
    }
    if ((low != null && node.val <= low) || (high != null && node.val >= high)) {
        return false;
    }
    return valid(node.left, low, node.val) && valid(node.right, node.val, high);
}
```

A second solution, kept because it answers "can you do it **without recursion**?": the same
interval travels with each node in a queue, so a skewed tree of 10⁴ nodes cannot overflow the
call stack.

```python
# python
# LC 98 - Validate Binary Search Tree (iterative)
# IDEA: BFS of (node, low, high); each child inherits the interval, tightened by its parent
# time = O(n), space = O(w)   w = the widest level
from collections import deque

def isValidBST(root):
    queue = deque([(root, None, None)])
    while queue:
        node, low, high = queue.popleft()
        if not node:
            continue
        if (low is not None and node.val <= low) or (high is not None and node.val >= high):
            return False
        queue.append((node.left, low, node.val))      # left: values below node.val
        queue.append((node.right, node.val, high))    # right: values above node.val
    return True
```

**Complexity**
- **Time O(n)**: each node is checked once against its interval; the `and` stops early on the
  first violation.
- **Space O(h)**: the recursion stack.

**Traps**
- Comparing a node only with its **parent** accepts `[5,4,6,null,null,3,7]`: `3 < 6` is fine
  locally, but `3` sits in 5's right subtree. Carry the interval.
- Using `Integer.MIN_VALUE` / `MAX_VALUE` as the Java sentinels rejects a tree whose single node
  holds `Integer.MIN_VALUE`. Use `null` (or `long`) for "no bound".
- `<=` and `>=`, not `<` and `>`: duplicates are invalid in LC 98, so `[2,2,2]` must return false.

**Not this if** — the interviewer prefers in-order: walk the tree in order (Template 5) and check
that each value is strictly greater than the previous one. Same O(n), and it generalises to LC 99
(find the two swapped nodes).

**Drill** — LC 98 ⭐ · LC 530 *(in-order + prev)* · LC 333 *(return bounds up instead)* · LC 99

### Template 5: In-order Walk Is Sorted — LC 230

**Recognise** — "kth smallest", "BST iterator", "minimum difference between any two values",
"mode", "convert to a sorted list" — any question about the BST's values **in sorted order**.

**Key point** — in-order (left, node, right) visits a BST's values in ascending order. The
iterative form keeps the not-yet-visited left spine on a stack, so it can stop at the kth pop
without walking the rest of the tree.

```python
# python
# LC 230 - Kth Smallest Element in a BST
# IDEA: iterative in-order; the kth node popped is the kth smallest
# time = O(h + k), space = O(h)
def kthSmallest(root, k):
    stack = []
    node = root
    while node or stack:
        while node:                       # push the whole left spine
            stack.append(node)
            node = node.left
        node = stack.pop()                # the next value in sorted order
        k -= 1
        if k == 0:
            return node.val
        node = node.right                 # then the right subtree, in order
    return -1                             # k > number of nodes
```

```java
// java
// LC 230 - Kth Smallest Element in a BST
// IDEA: iterative in-order; stop at the kth pop
// time = O(h + k), space = O(h)
public int kthSmallest(TreeNode root, int k) {
    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode node = root;
    while (node != null || !stack.isEmpty()) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
        node = stack.pop();
        k--;
        if (k == 0) {
            return node.val;
        }
        node = node.right;
    }
    return -1;
}
```

**Complexity**
- **Time O(h + k)**: the inner `while` first descends h nodes to the minimum; after that each pop
  is one value in order, and the walk stops at the kth.
- **Space O(h)**: the stack holds at most one root-to-leaf spine.

**Traps**
- Building the whole sorted list first (`inorder(left) + [val] + inorder(right)`) is O(n) time and
  O(n) space for every query, and the list concatenation is O(n·h) on top of that.
- Off by one: decrement **after** the pop and test `k == 0`. Testing before the decrement returns
  the (k+1)th: `[3,1,4,null,2]`, k = 1 returns 2 instead of 1.

**Not this if** — kth smallest is asked repeatedly while the tree changes (LC 230 follow-up) →
store subtree sizes on each node, the order-statistic tree of
[bst_advanced.md § Template 3](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-).

**Drill** — LC 230 ⭐ · LC 530 · LC 501 · LC 173 *(the same stack, paused between calls)* · LC 538

#### **Pattern: Reverse Inorder for Descending**

Right → node → left visits values in descending order, so a running sum turns each node into the
sum of every value greater than or equal to it (LC 538 / LC 1038).

```python
# python
# LC 538 - Convert BST to Greater Tree
# IDEA: reverse in-order (right, node, left) with a running sum of the values seen so far
# time = O(n), space = O(h)
class Solution(object):
    def convertBST(self, root):
        self.running = 0

        def reverse_inorder(node):
            if not node:
                return
            reverse_inorder(node.right)   # larger values first
            self.running += node.val
            node.val = self.running
            reverse_inorder(node.left)

        reverse_inorder(root)
        return root
```

### Template 6: Sorted Array to Balanced BST — LC 108

**Recognise** — "convert a sorted array / list to a **height-balanced** BST", "rebuild this BST so
it is balanced" (LC 1382 = in-order + this).

**Key point** — the middle element of a sorted range is the root that leaves the two sides at
most one element apart in size. Recursing on `[left, mid − 1]` and `[mid + 1, right]` repeats that
at every level, so the heights of sibling subtrees differ by at most 1.

```python
# python
# LC 108 - Convert Sorted Array to Binary Search Tree
# IDEA: the middle of the index range is the root; recurse on the two halves (no slicing)
# time = O(n), space = O(log n)
def sortedArrayToBST(nums):
    def build_range(left, right):
        if left > right:
            return None
        mid = (left + right) // 2
        root = TreeNode(nums[mid])
        root.left = build_range(left, mid - 1)
        root.right = build_range(mid + 1, right)
        return root
    return build_range(0, len(nums) - 1)
```

```java
// java
// LC 108 - Convert Sorted Array to Binary Search Tree
// IDEA: the middle of the index range is the root; recurse on the two halves
// time = O(n), space = O(log n)
public TreeNode sortedArrayToBST(int[] nums) {
    return build(nums, 0, nums.length - 1);
}

private TreeNode build(int[] nums, int left, int right) {
    if (left > right) {
        return null;
    }
    int mid = left + (right - left) / 2;
    TreeNode root = new TreeNode(nums[mid]);
    root.left = build(nums, left, mid - 1);
    root.right = build(nums, mid + 1, right);
    return root;
}
```

**Complexity**
- **Time O(n)**: each index becomes exactly one node, and `build` does O(1) work per call.
- **Space O(log n)**: the recursion depth is the height of a balanced tree (the output's O(n) nodes
  are not counted).

**Traps**
- Slicing (`nums[:mid]`, `nums[mid+1:]`) copies the array at every level: O(n log n) time instead of O(n), and O(n)
  extra memory at peak (the live slices shrink by half each level). Pass indices.
- `mid = (left + right) / 2` in Python 3 is a float and fails as an index; use `//`.

**Not this if** — the input is a sorted **linked list** (LC 109): finding the middle costs O(n) per
level, so either use slow/fast pointers, or simulate the in-order build — see
[bst_advanced.md § LC 109](./bst_advanced.md#convert-sorted-list-to-binary-search-tree--lc-109).

**Drill** — LC 108 · LC 109 · LC 1382 *(in-order, then this)* · LC 1008

#### **The Other Construction Inputs**

A sorted linked list (LC 109), a preorder (LC 1008), an existing BST to rebalance (LC 1382),
and every unique BST (LC 95, counted in LC 96) are worked in the construction section of
[bst_advanced.md](./bst_advanced.md#construction-variants).

The one line worth carrying in your head: **LC 1382 = LC 94 + LC 108** — flatten the BST
with an in-order walk (Template 5), then rebuild it with the mid-as-root recursion above.

#### **Construction Pattern Summary Table**
| Input Type | Approach | Key Technique | Time | Space | LC # |
|------------|----------|---------------|------|-------|------|
| **Sorted Array** | Binary search | Pick mid as root | O(n) | O(n) | 108 |
| **Sorted List** | Two pointers | Find mid node | O(n log n) | O(log n) | 109 |
| **Preorder** | Bounds checking | Use min/max | O(n) | O(h) | 1008 |
| **Balance BST** | Inorder + Rebuild | Collect sorted nodes | O(n) | O(n) | 1382 |
| **Generate All** | Combinatorial | Try each as root | O(4^n/n^1.5) | O(4^n/n^1.5) | 95 |
| **Count Unique** | Dynamic programming | Catalan numbers | O(n²) | O(n) | 96 |
| **Serialize** | Preorder encoding | BST property | O(n) | O(n) | 449 |

### Template 10: LCA via BST Ordering — LC 235 ⭐⭐⭐⭐

**Recognise** — "lowest common ancestor" **and** the tree is a BST. Nodes ≤ 10⁵, both targets
guaranteed present.

**Key point** — while `p` and `q` are both smaller than `node.val` they are both in the left
subtree, and both larger means both in the right, so the LCA is on that side. The first node that
does not send them the same way (one on each side, or the node *is* one of them) is the split
point, and the split point is the LCA.

```python
# python
# LC 235 - Lowest Common Ancestor of a Binary Search Tree
# IDEA: walk down while p and q sit on the SAME side; the first split point is the LCA
# time = O(h), space = O(1)
def lowestCommonAncestor(root, p, q):
    node = root
    while node:
        if p.val < node.val and q.val < node.val:
            node = node.left
        elif p.val > node.val and q.val > node.val:
            node = node.right
        else:
            return node                   # split here, or node is p or q
    return None
```

```java
// java
// LC 235 - Lowest Common Ancestor of a Binary Search Tree
// IDEA: walk down while p and q sit on the SAME side; the first split point is the LCA
// time = O(h), space = O(1)
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    TreeNode node = root;
    while (node != null) {
        if (p.val < node.val && q.val < node.val) {
            node = node.left;
        } else if (p.val > node.val && q.val > node.val) {
            node = node.right;
        } else {
            return node;
        }
    }
    return null;
}
```

**Complexity**
- **Time O(h)**: one step down per iteration, along a single path.
- **Space O(1)**: one pointer. The recursive spelling costs O(h) of stack.

**Dry run**

```text
[6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
node 6: 2 < 6 and 4 < 6   -> go left
node 2: 2 is not < 2       -> split (node is p) -> LCA = 2
```

**Traps**
- Strict `<` / `>`: when `node` **is** `p` (p = 2, q = 4 above), neither "both smaller" nor "both
  larger" holds, which is exactly what returns it. `<=` walks past the answer.
- Comparing nodes instead of values (`p < node`) does not order anything; compare `.val`.

**Not this if** — the tree is not a BST → post-order search of both subtrees, O(n)
([tree_lca_distance.md](./tree_lca_distance.md)); no root, only `parent` pointers (LC 1650) →
walk up both parent chains.

**Drill** — LC 235 ⭐ · LC 236 *(no ordering)* · LC 1650 *(parent pointers)* · LC 1644

## Problems by Pattern

> Index only — no code. `Template N` is a card on this page; `advanced T N` is [bst_advanced.md](./bst_advanced.md)'s Template N; `advanced` alone is a problem card there.

### Pattern-Based Problem Classification

#### **Pattern 1: BST Search & Validation Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Validate Binary Search Tree | 98 | Medium | Min/Max bounds | Template 4 |
| Search in a BST | 700 | Easy | Binary search | Template 1 |
| Closest Binary Search Tree Value | 270 | Easy | Binary search | Template 1 |
| Inorder Successor in BST | 285 | Medium | Inorder property | Template 1 |
| Two Sum IV - Input is BST | 653 | Easy | Hash + Traversal | Template 5 |
| Find Mode in BST | 501 | Easy | Inorder traversal | Template 5 |

#### **Pattern 2: BST Insertion & Deletion Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Insert into a BST | 701 | Medium | Recursive insert | Template 2 |
| Delete Node in a BST | 450 | Medium | Three cases | Template 3 |
| Trim a Binary Search Tree | 669 | Medium | Recursive trim | Template 3b |

#### **Pattern 3: BST Traversal & Conversion Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Kth Smallest Element in BST | 230 | Medium | Inorder traversal | Template 5 |
| BST Iterator | 173 | Medium | Stack + Inorder | advanced T1 |
| Convert BST to Greater Tree | 538 | Medium | Reverse inorder | Template 5 |
| Binary Search Tree to Greater Sum Tree | 1038 | Medium | Reverse inorder | Template 5 |
| Convert Sorted List to BST | 109 | Medium | Two pointers | advanced |
| Flatten BST to Sorted List | 426 | Medium | Inorder + linking | Template 5 |
| Increasing Order Search Tree | 897 | Easy | Inorder rebuild | Template 5 |
| All Elements in Two BSTs | 1305 | Medium | Two lazy iterators + merge | advanced |

#### **Pattern 4: BST Construction Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Convert Sorted Array to BST | 108 | Easy | Binary search | Template 6 |
| Unique Binary Search Trees | 96 | Medium | DP/Catalan | advanced |
| Unique Binary Search Trees II | 95 | Medium | Generate all | advanced T5 |
| Serialize and Deserialize BST | 449 | Medium | Preorder encoding | Template 4 |
| Construct BST from Preorder | 1008 | Medium | Bounds on a preorder | advanced T4 |
| Balance a Binary Search Tree | 1382 | Medium | Inorder + rebuild | advanced |

#### **Pattern 5: BST Properties & Range Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor of BST | 235 | Easy | BST property | Template 10 |
| Minimum Distance Between BST Nodes | 783 | Easy | Inorder diff | Template 5 |
| Minimum Absolute Difference in BST | 530 | Easy | Inorder diff | Template 5 |
| Range Sum of BST | 938 | Easy | DFS with pruning | Template 3b |
| Split BST | 776 | Medium | Recursive split | Template 3b |
| Largest BST Subtree | 333 | Medium | Bottom-up validation | Template 4 |
| Kth Largest Element in a Stream | 703 | Easy | Size-augmented BST (rank query) | advanced T3 |
| Maximum Difference Between Node and Ancestor | 1026 | Medium | Bounds propagation (min/max down) | advanced |

#### **Pattern 6: Path Problems**
Not BST problems: LC 112, 113, 124, 129, 257, 437 are in [tree_lca_distance.md](./tree_lca_distance.md), LC 298 in [binary_tree.md](./binary_tree.md).

### Complete Problem List by Difficulty

#### Easy Problems (Foundation)
- LC 700: Search in a Binary Search Tree - Basic BST search
- LC 270: Closest Binary Search Tree Value - Modified search
- LC 108: Convert Sorted Array to BST - Basic construction
- LC 235: Lowest Common Ancestor of a BST - Use BST property
- LC 653: Two Sum IV - Input is a BST - Two pointers on tree
- LC 530: Minimum Absolute Difference in BST - Inorder property
- LC 783: Minimum Distance Between BST Nodes - Inorder traversal
- LC 897: Increasing Order Search Tree - Inorder rebuilding
- LC 938: Range Sum of BST - DFS with pruning
- LC 501: Find Mode in Binary Search Tree - Inorder + counting

#### Medium Problems (Core)
- LC 98: Validate Binary Search Tree - Classic validation
- LC 173: Binary Search Tree Iterator - Design pattern
- LC 230: Kth Smallest Element in a BST - Inorder application
- LC 450: Delete Node in a BST - Complex restructuring
- LC 701: Insert into a BST - Basic modification
- LC 285: Inorder Successor in BST - BST navigation
- LC 96: Unique Binary Search Trees - Catalan numbers
- LC 95: Unique Binary Search Trees II - Generate all trees
- LC 109: Convert Sorted List to BST - List to tree
- LC 449: Serialize and Deserialize BST - Encoding/decoding
- LC 538: Convert BST to Greater Tree - Reverse inorder
- LC 669: Trim a Binary Search Tree - Recursive trimming
- LC 776: Split BST - Advanced manipulation
- LC 333: Largest BST Subtree - Subtree validation
- LC 1008: Construct BST from Preorder - Stack approach
- LC 1038: Binary Search Tree to Greater Sum Tree - Accumulation
- LC 1382: Balance a Binary Search Tree - Inorder + rebuild balanced BST
- LC 426: Convert BST to Sorted Doubly Linked List - In-place conversion

#### Hard Problems (Advanced)
- LC 99: Recover Binary Search Tree - Fix swapped nodes (see [bst_advanced.md § Template 2](./bst_advanced.md#template-2-in-order-drop-detection--lc-99-))
- LC 1373: Maximum Sum BST in Binary Tree - Complex validation

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 235 ⭐ · LC 98 ⭐ · LC 230 ⭐ | the split point, the interval carried down, in-order as sorted order |
| **Practice** | LC 700 · LC 108 · LC 701 · LC 450 · LC 669 · LC 173 | search, build, insert, delete, range trim, a paused in-order |
| **Stretch** | LC 1382 · LC 776 · LC 99 | in-order then rebuild; split into two trees; find the two swapped nodes |

## Summary & Quick Reference

**Complexity** — one table only, at the top of this sheet: see
[Time Complexity](#time-complexity). Everything below is about *which* template to reach for.

### Decision Flowchart
```text
BST Problem Analysis Flowchart:

1. Does the problem require finding/searching a value?
   ├── YES → Use Search Template (1)
   │   ├── Exact match? → Basic binary search
   │   ├── Closest value? → Track min difference
   │   └── Range query? → Prune based on BST property
   └── NO → Continue to 2

2. Does the problem require modifying the BST structure?
   ├── YES → Check modification type
   │   ├── Insert new node? → Use Insertion Template (2)
   │   ├── Delete existing node? → Use Deletion Template (3)
   │   └── Trim/Split tree? → Use modified Deletion Template
   └── NO → Continue to 3

3. Does the problem use the sorted property of BST?
   ├── YES → Use Inorder Template (5)
   │   ├── Kth element? → Inorder with counter
   │   ├── Convert to list? → Inorder traversal
   │   └── Range sum? → Modified inorder
   └── NO → Continue to 4

4. Does the problem require validating BST properties?
   ├── YES → Use Validation Template (4)
   │   ├── Entire tree? → Min/max bounds approach
   │   └── Find largest valid subtree? → Bottom-up validation
   └── NO → Continue to 5

5. Does the problem involve constructing a BST?
   ├── YES → Use Construction Template (6)
   │   ├── From sorted array? → Binary search approach
   │   ├── From traversal? → Use BST properties
   │   └── Generate all possible? → Recursive generation
   └── NO → Continue to 6

6. Does the problem involve paths in the tree?
   ├── YES → Not a BST problem: tree_lca_distance.md (path templates)
   └── NO → Fall back to the plain BST descent (Template 1) or reconsider

```

### Which Template? — Quick Selection

| If Problem Asks... | Use Template | Key Technique | Typical LC Problems |
|-------------------|--------------|---------------|---------------------|
| "Find/search value" | Template 1 | Binary search property | 700, 270, 938 |
| "Insert into BST" | Template 2 | Recursive insertion | 701 |
| "Delete from BST" | Template 3 | Three-case handling | 450, 669 |
| "Is valid BST?" | Template 4 | Min/max bounds | 98, 333 |
| "Kth smallest/largest" | Template 5 | Inorder traversal | 230, 173 |
| "LCA of two nodes" | Template 10 | Descend the one side holding both | 235 |
| "Convert sorted array" | Template 6 | Binary search middle | 108 |
| "Balance BST" | [advanced § LC 1382](./bst_advanced.md#balance-a-binary-search-tree--lc-1382-) | Inorder + rebuild | 1382 |
| "Path sum", "max path sum" | [tree_lca_distance.md](./tree_lca_distance.md) | not a BST technique | 112, 113, 124, 437 |
| "Recover / fix a broken BST" | [advanced § Template 2](./bst_advanced.md#template-2-in-order-drop-detection--lc-99-) | In-order + `prev` drop detection | 99, 501, 530 |
| "Kth largest in a stream / rank query" | [advanced § Template 3](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-) | Size-augmented BST | 703 |

### Recognition Patterns

**Keywords → Template Mapping:**
- **"Search", "find", "closest"** → Template 1 (Search)
- **"Insert", "add node"** → Template 2 (Insertion)
- **"Delete", "remove", "trim"** → Template 3 (Deletion)
- **"Valid", "validate", "is BST"** → Template 4 (Validation)
- **"Kth", "sorted", "inorder", "iterator"** → Template 5 (Inorder)
- **"Lowest common ancestor", "split point"** → Template 10 (LCA via ordering)
- **"Construct", "build", "convert", "balance", "generate"** → Template 6 (Construction)
- **"Path", "sum", "maximum path", "consecutive"** → not BST: [tree_lca_distance.md](./tree_lca_distance.md)
- **"Recover", "swapped", "fix", "adjacent values", "successor", "mode"** → [bst_advanced.md](./bst_advanced.md) Template 2 (In-order + `prev`)
- **"Stream", "after each insert", "rank", "how many are less than"** → [bst_advanced.md](./bst_advanced.md) Template 3 (Order-Statistic BST)

### Problem-Solving Steps
1. **Identify BST property usage**: Can you use left < root < right?
2. **Choose appropriate template**: Based on operation type
3. **Consider edge cases**: Empty tree, single node, duplicates
4. **Optimize with pruning**: Skip unnecessary subtrees
5. **Test with skewed trees**: Worst case scenarios

### Common Mistakes & Tips

Each card's **Traps** names the input that springs it; these are the five that cost the most.

- **Parent-only validation** — `[5,4,6,null,null,3,7]` passes a parent/child check but `3` sits
  right of `5`. Carry the interval ([Template 4](#template-4-validate-with-bounds--lc-98)).
- **Dropping the returned child** — `insertIntoBST(root.left, val)` without `root.left = …`
  creates the leaf and loses it ([Template 2](#template-2-insert-at-the-null-leaf--lc-701)).
- **Returning `None` for an out-of-range node** — in LC 669 it discards the in-range nodes below
  it ([Template 3b](#template-3b-trim-to-a-range--lc-669-)).
- **`<=` while walking to the LCA** — with `p = 2, q = 4` on `[6,2,8,0,4,…]` it steps past `2`,
  which is the answer ([Template 10](#template-10-lca-via-bst-ordering--lc-235-)).
- **Claiming O(log n)** — inserting `1, 2, …, n` builds a chain; every template here is O(n) on
  it. Say "O(h), O(log n) if balanced".

### Interview Tips
1. **Clarify BST properties**: Can there be duplicates? Is it balanced?
2. **State complexity**: Mention average O(log n) and worst O(n)
3. **Consider self-balancing**: Mention AVL/Red-Black trees if relevant
4. **Use BST property**: Show you understand the optimization
5. **Handle all cases**: Especially for deletion (0, 1, 2 children)

### Related Topics
- **Self-Balancing BSTs**: AVL Tree, Red-Black Tree (guaranteed O(log n))
- **B-Trees**: For database indexes (multiple keys per node)
- **Binary Heap**: Different property (parent > children)
- **Trie**: Prefix tree for strings
- **Segment Tree**: Range queries and updates

### Java Implementation Notes
```java
// Java BST Node
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int x) { val = x; }
}

// Iterative inorder with Stack
Stack<TreeNode> stack = new Stack<>();
TreeNode curr = root;
while (curr != null || !stack.isEmpty()) {
    while (curr != null) {
        stack.push(curr);
        curr = curr.left;
    }
    curr = stack.pop();
    // Process curr
    curr = curr.right;
}
```

### Python Implementation Notes
```python
# TreeNode class
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# Generator for memory-efficient inorder
def inorder_generator(root):
    if root:
        yield from inorder_generator(root.left)
        yield root.val
        yield from inorder_generator(root.right)
```

### Pro Tips for Template Selection

1. **Leverage BST Property**: If problem can use `left < root < right`, use Templates 1-6
2. **Path Problems**: they work on any binary tree, so they are in [tree_lca_distance.md](./tree_lca_distance.md)
3. **Construction**: Check input type (array/list/traversal) → different template variant
4. **Modification**: Always return root after modification (Templates 2, 3, 6)
5. **When in Doubt**: Check if inorder traversal helps (Template 5)

---
**Must-Know Problems for Interviews**: LC 98, 108, 173, 230, 235, 450, 700, 701, 1382
**Advanced Problems**: LC 99, 333, 776, 1373
**Keywords**: BST, binary search tree, inorder, sorted, validation, search tree, path sum, DFS, backtracking, balance, construction