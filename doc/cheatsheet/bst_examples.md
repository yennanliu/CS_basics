# BST — Worked LeetCode Examples

> **Scope** — The worked-solution archive for the BST templates taught in `bst.md` — one canonical solution per problem per language — plus the root-to-leaf and node-to-node path family that is filed with BST but needs no ordering at all.
> **See also**: [bst.md](./bst.md) — the canonical BST templates these examples apply; [bst_advanced.md](./bst_advanced.md) — order-statistic queries, the lazy iterator, recovering a broken BST and the construction variants; [tree_backtrack.md](./tree_backtrack.md) — root→leaf paths that undo state on the way back up.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## Overview

Six worked BST problems whose solutions are not already spelled out by a template in
[bst.md](./bst.md), followed by the seven-pattern path family (Template 7). Anything a
template already solves *in the same language* is not restated here — go to the template.

### Key Properties
- **Complexity**: every solution below is O(n) time except LC 776 (O(h)); space is O(h) for
  the recursion stack unless the code comment says otherwise
- **Core Idea**: one canonical solution per problem per language; a second variant appears
  only where the note above it says what it teaches that the first one does not
- **When to Use**: after you can write the matching template from memory

## LC Examples

### 1) Serialize and Deserialize BST — LC 449

Two genuinely different codecs: `V0` is level-order with `#` placeholders (works for any
binary tree, LC 297 style), `V1` is preorder plus `(min, max)` bounds and needs **no**
placeholders at all — that saving is exactly what the BST property buys you.

```python
# LC 449 Serialize and Deserialize BST
# V0
# IDEA : BFS + queue op
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'

        res = [root.val]
        q = [root]

        while q:
            new_q = []
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                    res.extend( [tmp.left.val] )
                else:
                    res.append('#')
                if tmp.right:
                    q.append(tmp.right)
                    res.extend( [tmp.right.val] )
                else:
                    res.append('#')

        while res and res[-1] == '#':
                    res.pop()

        return '{' + ','.join(map(str, res)) + '}' 


    def deserialize(self, data):
        if data == '{}':
            return

        nodes = [ TreeNode(x) for x in data[1:-1].split(",") ]
        root = nodes.pop(0)
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.pop(0)
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.pop(0)
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2) Split BST — LC 776

#### Pattern: Recursive BST Partition
Split a BST into two valid BSTs based on a target value. This is a **partition** problem, NOT a delete problem — we preserve all nodes but redistribute them into two trees.

#### Theory: Why Split ≠ Delete

| Operation | Goal | Nodes Lost? | Return Value |
|-----------|------|-------------|--------------|
| **Delete** (LC 450) | Remove one node, keep one tree | Yes (1 node) | Single `TreeNode` |
| **Split** (LC 776) | Separate into two trees | No | `TreeNode[2]` array |

#### Core Idea

```text
Return value: TreeNode[2]
  res[0] → BST with all values ≤ target
  res[1] → BST with all values > target
```

**Two cases based on root.val vs target:**

```text
Case 1: root.val <= target
  → root belongs to LEFT partition (res[0])
  → But root.right may contain nodes > target
  → So SPLIT root.right, reconnect the pieces

Case 2: root.val > target
  → root belongs to RIGHT partition (res[1])
  → But root.left may contain nodes <= target
  → So SPLIT root.left, reconnect the pieces
```

#### Visual Walkthrough

```text
Input:        target = 2
         4
        / \
       2   6
      / \ / \
     1  3 5  7

Step 1: root=4, val=4 > target=2 → root goes to RIGHT partition
        Split root.left (the subtree rooted at 2)

Step 2: root=2, val=2 <= target=2 → root goes to LEFT partition
        Split root.right (the subtree rooted at 3)

Step 3: root=3, val=3 > target=2 → root goes to RIGHT partition
        Split root.left (null) → returns [null, null]
        root.left = null (from split[1])
        Return [null, 3]

Back to Step 2: split of node 2's right returned [null, 3]
        node 2's right = split[0] = null  (was 3, now detached)
        Return [2→(left:1, right:null), 3]

Back to Step 1: split of node 4's left returned [2, 3]
        node 4's left = split[1] = 3  (reconnect!)
        Return [2, 4→(left:3, right:6)]

Result:
  res[0] (≤ 2):     res[1] (> 2):
       2                  4
      /                  / \
     1                  3   6
                           / \
                          5   7
```

#### Key Insight: The Reconnection

```text
When root.val <= target and we split root.right:
  split[0] = nodes from right subtree that are still <= target
  split[1] = nodes from right subtree that are > target

  root.right = split[0]   ← keep the small ones attached to root
  return [root, split[1]] ← root is left partition, split[1] is right partition

When root.val > target and we split root.left:
  split[0] = nodes from left subtree that are <= target
  split[1] = nodes from left subtree that are > target

  root.left = split[1]    ← keep the big ones attached to root
  return [split[0], root] ← split[0] is left partition, root is right partition
```

#### Python Implementation
```python
# LC 776 Split BST
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        # root belongs to LEFT partition
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]  # keep <= V part
            return root, result[1]  # [smallTree, bigTree]
        # root belongs to RIGHT partition
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]   # keep > V part
            return result[0], root  # [smallTree, bigTree]
```

#### Java Implementation
```java
// LC 776 Split BST
// Time: O(H), Space: O(H) where H = height of BST
public TreeNode[] splitBST(TreeNode root, int target) {
    if (root == null) {
        return new TreeNode[]{null, null};
    }

    if (root.val <= target) {
        // root goes to LEFT partition (<=target)
        TreeNode[] split = splitBST(root.right, target);
        root.right = split[0];          // reconnect small part
        return new TreeNode[]{root, split[1]};
    } else {
        // root goes to RIGHT partition (>target)
        TreeNode[] split = splitBST(root.left, target);
        root.left = split[1];           // reconnect big part
        return new TreeNode[]{split[0], root};
    }
}
```

#### Similar Problems

| Problem | LC # | Similarity | Key Difference |
|---------|------|-----------|----------------|
| **Split BST** | 776 | Core pattern | Partition into 2 trees by value |
| **Delete Node in BST** | 450 | Both modify BST structure | Delete removes 1 node; split keeps all |
| **Trim a BST** | 669 | Both remove nodes outside range | Trim discards nodes; split preserves all in 2 trees |
| **Search in BST** | 700 | Same left/right branching logic | Search returns 1 node; split returns 2 trees |
| **Insert into BST** | 701 | Same recursive BST traversal | Insert adds; split partitions |
| **Merge Two BSTs** | - | Inverse operation | Split → 2 trees; Merge → 1 tree |

#### Complexity
- **Time**: O(H) — only visits nodes along one root-to-leaf path
- **Space**: O(H) — recursion stack depth = tree height

### 3) Binary Search Tree Iterator — LC 173

The **eager** variant: flatten the whole tree in the constructor, then serve from a list.
Kept because it is the baseline the lazy O(h) stack version is measured against — see
`Template 5b` in [bst_advanced.md](./bst_advanced.md) for the lazy form and the
eager-vs-lazy table.

```python
# LC 173. Binary Search Tree Iterator
# IDEA : STACK + tree
class BSTIterator(object):
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.stack = []
        self.inOrder(root)
    
    def inOrder(self, root):
        if not root:
            return
        """
        NOTE !!! how we do inorder traversal here

        irOrder : left -> root -> right
        """
        self.inOrder(root.left)
        self.stack.append(root.val)
        self.inOrder(root.right)
    
    def hasNext(self):
        """
        :rtype: bool
        """
        return len(self.stack) > 0

    def next(self):
        """
        :rtype: int
        """
        return self.stack.pop(0)  # NOTE here
```

### 4) Validate Binary Search Tree — LC 98

The **BFS** formulation: carry `(node, min, max)` in the queue instead of on the recursion
stack. Kept because `Template 4` in [bst.md](./bst.md) is the DFS form — this one is the
answer to "can you do it iteratively / without recursion?".

```python
# LC 98 Validate Binary Search Tree
# IDEA : BFS
#  -> trick : we make sure current tree and all of sub tree are valid BST
#   -> not only compare tmp.val with tmp.left.val, tmp.right.val,
#   -> but we need compare if tmp.left.val is SMALLER then `previous node val`
#   -> but we need compare if tmp.right.val is BIGGER then `previous node val`
class Solution(object):
    def isValidBST(self, root):
        if not root:
            return True
        _min = -float('inf')
        _max = float('inf')
        ### NOTE : we set q like below
        q = [[root, _min, _max]]
        while q:
            for i in range(len(q)):
                tmp, _min, _max = q.pop(0)
                if tmp.left:
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.left.val" with others (BEFORE we visit tmp.left)
                    """
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.right.val" with others (BEFORE we visit tmp.right)
                    """
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True
```

### 5) Convert BST to Greater Tree — LC 538
Use **reverse inorder** (right → root → left) with a running sum. See the
[Reverse Inorder for Descending](./bst.md#pattern-reverse-inorder-for-descending) pattern in `bst.md` for the implementation.

### 6) Binary Search Tree to Greater Sum Tree — LC 1038
Identical to LC 538 — reverse inorder traversal accumulating a running sum.
See the [Reverse Inorder for Descending](./bst.md#pattern-reverse-inorder-for-descending) pattern in `bst.md`.

## Root-to-Leaf & Node-to-Node Paths

Filed here rather than in [bst.md](./bst.md) because **none of these use the BST ordering** —
they work on any binary tree. They are collected as one template because they all share the
same DFS skeleton, differing only in what state travels down and what comes back up.

### Template 7: Path Problems

#### **Pattern Overview**
- **Description**: Find or validate paths in binary trees (root-to-leaf, node-to-node)
- **Recognition**: "Path sum", "root to leaf", "maximum path", "consecutive"
- **Key Concept**: Use DFS with path tracking, accumulation, or global state
- **Time Complexity**: O(n) for visiting all nodes
- **Space Complexity**: O(h) for recursion stack + path storage

**📚 Related Patterns**: These path problems use DFS traversal. For general tree path-finding patterns and techniques, see **dfs.md Template 3 (Path Finding)**. The examples here focus on common path problems that work for both BST and general binary trees.

#### **Core Path Patterns**

##### **Pattern 7.1: Simple Path Sum** (LC 112)
```python
def has_path_sum(root, target_sum):
    """
    Check if root-to-leaf path exists with given sum
    Time: O(n), Space: O(h)
    """
    if not root:
        return False

    # Leaf node check
    if not root.left and not root.right:
        return root.val == target_sum

    # Recurse with reduced sum
    remaining = target_sum - root.val
    return (has_path_sum(root.left, remaining) or
            has_path_sum(root.right, remaining))
```

##### **Pattern 7.2: Path Sum II - All Paths** (LC 113)
```python
def path_sum(root, target_sum):
    """
    Find all root-to-leaf paths with given sum
    Uses backtracking to track current path
    Time: O(n), Space: O(h)
    """
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        # Add current node to path
        path.append(node.val)

        # Check if leaf with target sum
        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])  # Deep copy

        # Recurse on children
        new_remaining = remaining - node.val
        dfs(node.left, new_remaining, path)
        dfs(node.right, new_remaining, path)

        # Backtrack
        path.pop()

    dfs(root, target_sum, [])
    return result
```

##### **Pattern 7.3: Binary Tree Paths** (LC 257)
```python
def binary_tree_paths(root):
    """
    Find all root-to-leaf paths as strings
    Time: O(n), Space: O(h)
    """
    if not root:
        return []

    result = []

    def dfs(node, path):
        if not node:
            return

        # Add current node to path string
        path += str(node.val)

        # Leaf node - add complete path
        if not node.left and not node.right:
            result.append(path)
            return

        # Continue path with arrow
        path += "->"
        dfs(node.left, path)
        dfs(node.right, path)

    dfs(root, "")
    return result
```

##### **Pattern 7.4: Sum Root to Leaf Numbers** (LC 129)
```python
def sum_numbers(root):
    """
    Sum all numbers formed by root-to-leaf paths
    Example: 1->2->3 represents 123
    Time: O(n), Space: O(h)
    """
    def dfs(node, current_sum):
        if not node:
            return 0

        # Build number: current_sum * 10 + node.val
        current_sum = current_sum * 10 + node.val

        # Leaf node - return the number
        if not node.left and not node.right:
            return current_sum

        # Sum from both subtrees
        return dfs(node.left, current_sum) + dfs(node.right, current_sum)

    return dfs(root, 0)
```

##### **Pattern 7.5: Binary Tree Maximum Path Sum** (LC 124)
```python
def max_path_sum(root):
    """
    Find maximum path sum (any node to any node)
    Uses global variable to track maximum
    Time: O(n), Space: O(h)
    """
    max_sum = float('-inf')

    def dfs(node):
        nonlocal max_sum

        if not node:
            return 0

        # Get max contribution from left and right
        # Use max(0, ...) to ignore negative paths
        left_max = max(0, dfs(node.left))
        right_max = max(0, dfs(node.right))

        # Update global max with path through current node
        path_sum = node.val + left_max + right_max
        max_sum = max(max_sum, path_sum)

        # Return max path going through this node (one side only)
        return node.val + max(left_max, right_max)

    dfs(root)
    return max_sum
```

##### **Pattern 7.6: Binary Tree Longest Consecutive Sequence** (LC 298) ⭐⭐⭐⭐

###### **Core Idea**

- **Problem**: find the longest path where values increase by exactly `+1` at every step.
- **Direction is fixed**: the path must go **parent → child**. `3-2-1` does NOT count, only `1-2-3`.
- **The path is a "chain", not a "V"**: unlike LC 124 (Max Path Sum), you may **never** join a left branch and a right branch through a node. Each answer is a single top-down chain.
- **You may switch sides while descending**: `root.left` then `.right` then `.left` is fine — "consecutive" constrains the *values*, not which child pointer you follow.

```text
    1
     \
      2      <-- valid input; path 1 -> 2 -> 3 -> 4 has length 4
     /
    3
     \
      4
```

- **Key Idea**: the streak length at a node depends **only on its parent**, so carry `(parent_val, current_len)` **down** the recursion (top-down DFS). Every node either **extends** its parent's streak (`node.val == parent.val + 1`) or **starts a new one** (`len = 1`).
- **Where the answer lives**: no single node's return value is the answer — track it in a **global max**, updated at every node.

###### **Pattern**

**Template A — top-down (carry the streak down) ⭐ preferred**

```python
# python
# LC 298 - Binary Tree Longest Consecutive Sequence
# IDEA: TOP-DOWN DFS, pass (parent_val, current_len) downward
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        self.max_len = 0

        def dfs(node, parent_val, cur_len):
            if not node:
                return

            # extend the streak, or restart it at this node
            if node.val == parent_val + 1:
                cur_len += 1
            else:
                cur_len = 1

            self.max_len = max(self.max_len, cur_len)

            # NOTE: pass node.val down as the NEW parent value
            dfs(node.left, node.val, cur_len)
            dfs(node.right, node.val, cur_len)

        # trick: seed with (root.val - 1, 0) so root always counts as length 1
        dfs(root, root.val - 1, 0)
        return self.max_len
```

```java
// java
// LC 298 - Binary Tree Longest Consecutive Sequence
// IDEA: TOP-DOWN DFS
// time = O(n), space = O(h)
class Solution {
    private int maxLen = 0;

    public int longestConsecutive(TreeNode root) {
        if (root == null) return 0;
        dfs(root, root.val - 1, 0);
        return maxLen;
    }

    private void dfs(TreeNode node, int parentVal, int curLen) {
        if (node == null) return;
        curLen = (node.val == parentVal + 1) ? curLen + 1 : 1;
        maxLen = Math.max(maxLen, curLen);
        dfs(node.left, node.val, curLen);
        dfs(node.right, node.val, curLen);
    }
}
```

**Template B — bottom-up (return the streak starting at this node)**

```python
# python
# IDEA: POST-ORDER DFS, return "longest consecutive path STARTING at this node"
# time = O(n), space = O(h)
class Solution(object):
    def longestConsecutive(self, root):
        self.max_len = 0

        def helper(node):
            if not node:
                return 0

            left_len = helper(node.left)
            right_len = helper(node.right)

            cur_len = 1
            # NOTE: only take the child's length if the child continues the streak
            if node.left and node.left.val == node.val + 1:
                cur_len = max(cur_len, left_len + 1)
            if node.right and node.right.val == node.val + 1:
                cur_len = max(cur_len, right_len + 1)

            self.max_len = max(self.max_len, cur_len)
            return cur_len          # <-- ONE side only, never left + right

        helper(root)
        return self.max_len
```

**Template C — iterative DFS (stack of `(node, len)`)**

```python
# python
# IDEA: DFS with explicit stack; the streak length travels WITH the node
# time = O(n), space = O(n)
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        stack = [(root, 1)]
        max_len = 1
        while stack:
            node, path_len = stack.pop()
            for child in (node.left, node.right):
                if child:
                    new_len = path_len + 1 if child.val == node.val + 1 else 1
                    max_len = max(max_len, new_len)
                    stack.append((child, new_len))

        return max_len
```

###### **Visual Trace** — Template A on the example tree

```text
   1
    \
     3
    / \
   2   4
        \
         5

dfs(1, parent=0, len=0)   -> 1 == 0+1  -> len=1   max=1
  dfs(3, parent=1, len=1) -> 3 != 1+1  -> len=1   max=1   (streak breaks)
    dfs(2, parent=3, len=1) -> 2 != 4  -> len=1   max=1
    dfs(4, parent=3, len=1) -> 4 == 3+1-> len=2   max=2
      dfs(5, parent=4, len=2) -> 5 == 5-> len=3   max=3   <-- answer
```

###### **🚫 Why the `path = "{}-{}-{}".format(root.val, _left, _right)` Approach Does NOT Work**

A tempting idea is to **serialize every subtree into a string** (like LC 297 / LC 652), collect all the strings in a map, then parse each string back and measure its consecutive run:

```python
# 🚫 WRONG
def helper(self, root):
    if not root:
        return "#"
    _left  = self.helper(root.left)
    _right = self.helper(root.right)
    path = "{}-{}-{}".format(root.val, _left, _right)   # <-- the bug
    self.p_map[path] = 1
    return path
```

**1. A serialized subtree is a *tree*, not a *path* (the fatal flaw)**

`"{val}-{left}-{right}"` splices **both** subtrees into one flat string. But a consecutive sequence is a **single root→descendant chain** — it can only ever contain one of the two children. Flattening merges two sibling branches that are *not* connected by any parent-child edge, so `split("-")` produces neighbors that were never adjacent in the tree.

```text
   3
  / \
 9   4
      \
       5

serialize(3) = "3-9-#-#-4-#-5-#-#"
split         = [3, 9, #, #, 4, #, 5, #, #]
                       ^^^^^^^^
   `9` and `4` are SIBLINGS — no edge between them.
   `4` and `5` are a real edge but they are separated by `#`s.
   The linear scan `_list[i] == _list[i-1] + 1` is scanning a
   pre-order dump, NOT a path. It cannot recover the answer.
```

**2. It also loses the direction / start point**

Even if you filtered out the `#`s, a pre-order dump gives no way to tell "is `x` the *parent* of `y`, or its uncle?". The `+1` check needs the **parent-child edge**, which is exactly the information the string throws away. The recursion already has that edge for free (`node` and `node.val` while recursing into `node.left/right`) — serializing discards it and then tries to reconstruct it.

**3. Wrong data structure for the goal**

Subtree serialization exists to answer **"are two subtrees identical?"** (LC 652 Find Duplicate Subtrees, LC 297 Serialize/Deserialize). LC 298 asks about **one downward chain**, so the natural state is a scalar (`cur_len`), not a string.

**4. Bugs that hide the real problem**

| Line | Bug | Effect |
|------|-----|--------|
| `self.p_map().keys()` | dict is not callable | `TypeError` |
| `len = 0` | shadows the builtin `len()` | `TypeError: 'int' object is not callable` on next `len(_list)` |
| `p_map[path] = 1` | missing `self.` | `NameError` |
| `range(1, len(_list) - 1)` | off-by-one, skips last element | wrong count even if it ran |
| `return len(_list) - 1` | subtracting for no reason | wrong count |

**5. Cost**

Each node's string is O(size of its subtree), so building them all is **O(n²) time and O(n²) space** (worst case), vs **O(n) / O(h)** for the plain DFS.

> **Takeaway**: when the quantity you need is defined **along a single root→node chain**, carry it as a **parameter down the recursion**. Only reach for serialization when you need to compare *whole subtrees* to each other.

###### **Similar LeetCode Problems**

| LC # | Problem | Relation to LC 298 | Key Difference |
|------|---------|--------------------|----------------|
| **549** | Binary Tree Longest Consecutive Sequence II | Direct sequel ⭐ | Path may go **child → parent → child** (a "V"), and may be increasing **or** decreasing → return `inc + dec - 1` at each node |
| **124** | Binary Tree Maximum Path Sum | Same "global max + return one side" skeleton | Path CAN bend through a node; uses sums not `+1` steps |
| **687** | Longest Univalue Path | Same skeleton | Requires **equal** values instead of `+1`; counts **edges** not nodes |
| **543** | Diameter of Binary Tree | Same skeleton | No value constraint at all; pure edge counting |
| **1372** | Longest ZigZag Path in a Binary Tree | Top-down carried state | State is `(direction, length)` instead of `(parent_val, length)` |
| **129** | Sum Root to Leaf Numbers | Top-down carried state | Carries an accumulated number down; must reach a leaf |
| **112 / 113** | Path Sum I / II | Top-down carried state | Carries remaining sum; root→**leaf** only |
| **128** | Longest Consecutive Sequence (array) | Same "consecutive" idea | Unsorted array + hash set, no tree, order-free |
| **652** | Find Duplicate Subtrees | The problem serialization IS for | Compares whole subtrees → `"{val}-{left}-{right}"` is *correct* here |

###### **Key Takeaways**

1. **Carry state down, not up** — `cur_len` depends only on the parent, so it belongs in the parameter list.
2. **`else: cur_len = 1`, never `return`** — a broken streak restarts at the current node; it does not end the traversal.
3. **Return one side only** (Template B) — `left + right` would create a bent path, which LC 298 forbids (that's LC 549).
4. **Seed with `root.val - 1`** so the root is counted as a streak of length 1 without a special case.
5. **Don't serialize** — a flattened subtree string cannot represent a single downward path (see above).

##### **Pattern 7.7: Path Sum III** (LC 437)
```python
def path_sum_3(root, target_sum):
    """
    Count paths with given sum (not necessarily root-to-leaf)
    Uses prefix sum technique with hash map
    Time: O(n), Space: O(n)
    """
    from collections import defaultdict

    def dfs(node, current_sum, prefix_sums):
        if not node:
            return 0

        # Update current sum
        current_sum += node.val

        # Count paths ending at current node
        # If (current_sum - target_sum) exists, we found path(s)
        count = prefix_sums[current_sum - target_sum]

        # Add current sum to prefix map
        prefix_sums[current_sum] += 1

        # Recurse on children
        count += dfs(node.left, current_sum, prefix_sums)
        count += dfs(node.right, current_sum, prefix_sums)

        # Backtrack: remove current sum from map
        prefix_sums[current_sum] -= 1

        return count

    # Initialize with 0 sum (for paths starting from root)
    prefix_sums = defaultdict(int)
    prefix_sums[0] = 1

    return dfs(root, 0, prefix_sums)
```

#### **Java Implementations**
```java
// Pattern 7.1: Simple Path Sum (LC 112)
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;

    // Leaf node check
    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) ||
           hasPathSum(root.right, remaining);
}

// Pattern 7.2: Path Sum II (LC 113)
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    dfs(root, targetSum, new ArrayList<>(), result);
    return result;
}

private void dfs(TreeNode node, int remaining,
                 List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remaining == node.val) {
        result.add(new ArrayList<>(path));
    }

    int newRemaining = remaining - node.val;
    dfs(node.left, newRemaining, path, result);
    dfs(node.right, newRemaining, path, result);

    path.remove(path.size() - 1);  // Backtrack
}

// Pattern 7.5: Maximum Path Sum (LC 124)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    dfs(root);
    return maxSum;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int leftMax = Math.max(0, dfs(node.left));
    int rightMax = Math.max(0, dfs(node.right));

    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    return node.val + Math.max(leftMax, rightMax);
}

// Pattern 7.7: Path Sum III (LC 437)
public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefixSums = new HashMap<>();
    prefixSums.put(0L, 1);
    return dfs(root, 0L, targetSum, prefixSums);
}

private int dfs(TreeNode node, long currentSum, int target,
                Map<Long, Integer> prefixSums) {
    if (node == null) return 0;

    currentSum += node.val;
    int count = prefixSums.getOrDefault(currentSum - target, 0);

    prefixSums.put(currentSum, prefixSums.getOrDefault(currentSum, 0) + 1);

    count += dfs(node.left, currentSum, target, prefixSums);
    count += dfs(node.right, currentSum, target, prefixSums);

    prefixSums.put(currentSum, prefixSums.get(currentSum) - 1);

    return count;
}
```

#### **Path Pattern Summary Table**
| Problem Type | Approach | Key Technique | Time | Space | LC # |
|--------------|----------|---------------|------|-------|------|
| **Simple Path Sum** | DFS recursion | Reduce sum | O(n) | O(h) | 112 |
| **All Paths** | DFS + backtrack | Track path | O(n) | O(h) | 113 |
| **Path Strings** | DFS + string | Concatenate | O(n) | O(h) | 257 |
| **Sum Numbers** | DFS + accumulate | Build number | O(n) | O(h) | 129 |
| **Max Path Sum** | DFS + global | Track max | O(n) | O(h) | 124 |
| **Consecutive** | DFS + counter | Track length | O(n) | O(h) | 298 |
| **Prefix Sum** | DFS + hashmap | Prefix technique | O(n) | O(n) | 437 |

#### **Key Concepts & Principles**

1. **Root-to-Leaf Paths**
   - Always check for leaf nodes: `not node.left and not node.right`
   - Reduce target sum at each level
   - Return result at leaf nodes

2. **Backtracking Pattern**
   - Add current node to path
   - Recurse on children
   - Remove current node from path (restore state)
   - Essential for finding all paths

3. **Global State**
   - Use nonlocal/class variable for maximum values
   - Update during traversal
   - Return contribution, not final answer

4. **Path Through Node**
   - For max path: left_max + node.val + right_max
   - For return: node.val + max(left_max, right_max)
   - Use max(0, ...) to ignore negative contributions

5. **Prefix Sum Technique**
   - Track cumulative sum from root
   - Use hashmap: prefixSum[currentSum - target] = count
   - Backtrack by decrementing counts

#### **Common Mistakes & Pitfalls**

**🚫 Mistake 1: Not Checking Leaf Nodes**
```python
# BAD: Doesn't verify it's a leaf
if root.val == target:
    return True

# GOOD: Check both children are None
if not root.left and not root.right and root.val == target:
    return True
```

**🚫 Mistake 2: Forgetting to Backtrack**
```python
# BAD: Path grows indefinitely
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)

# GOOD: Remove after recursion
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)
    path.pop()
```

**🚫 Mistake 3: Shallow Copy in Results**
```python
# BAD: All results reference same list
result.append(path)

# GOOD: Create deep copy
result.append(path[:])  # or list(path)
```

**🚫 Mistake 4: Wrong Max Path Logic**
```python
# BAD: Includes both subtrees in return
def dfs(node):
    left = dfs(node.left)
    right = dfs(node.right)
    return node.val + left + right  # Wrong!

# GOOD: Return one path only
return node.val + max(left, right)
```

**🚫 Mistake 5: Not Handling Negative Values**
```python
# BAD: Negative paths reduce maximum
left_max = dfs(node.left)

# GOOD: Ignore negative contributions
left_max = max(0, dfs(node.left))
```
