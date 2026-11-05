# BST — Worked LeetCode Examples

> **Scope** — The BST problems that add one step to a template in `bst.md`, each worked once as a problem card and filed under the card it reduces to; problems a template or an advanced card already works are listed here as pointers, not re-solved.
> **See also**: [bst.md](./bst.md) — the BST templates these problems reduce to; [bst_advanced.md](./bst_advanced.md) — the lazy iterator, recovering a broken BST, rank queries and the construction variants; [tree_lca_distance.md](./tree_lca_distance.md) — the root-to-leaf and any-to-any path problems (LC 112, 113, 124, 129, 257, 437), which need no ordering; [binary_tree.md](./binary_tree.md) — LC 298 and the other unordered-tree DFS shapes.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## Overview

The BST problem set, grouped by the [bst.md](./bst.md) template each one reduces to, in that
sheet's template order. A problem the template itself already solves is a row in the Index
pointing at its home, not a second copy. The path-sum family that used to be filed here works
on any binary tree, so it lives in [tree_lca_distance.md](./tree_lca_distance.md).

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 776 | [Split BST](#split-bst--lc-776) | [bst § Template 3b](./bst.md#template-3b-trim-to-a-range--lc-669-) — keep both sides instead of discarding one | Medium |
| 98 | Validate Binary Search Tree — worked in [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98), with the iterative form | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) | Medium |
| 449 | [Serialize and Deserialize BST](#serialize-and-deserialize-bst--lc-449) | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) — the bounds decide where each preorder value goes | Medium |
| 538 | Convert BST to Greater Tree — worked in [bst § Template 5](./bst.md#pattern-reverse-inorder-for-descending) | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230), reversed | Medium |
| 1038 | Binary Search Tree to Greater Sum Tree — the same problem as LC 538, the same code | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230), reversed | Medium |
| 173 | Binary Search Tree Iterator — worked in [bst_advanced § Template 1](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) | [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230), paused between calls | Medium |

## Trim to a Range Examples

### Split BST — LC 776

> **Key point** — trimming discards the side of the root that is out of range; splitting keeps
> it as the second tree. At each node, one of the two returned trees is the node itself, and the
> other comes from splitting only the child that can straddle `target`, so the walk is one path.

**Reduces to**: [bst § Template 3b](./bst.md#template-3b-trim-to-a-range--lc-669-) — the same
"the whole other side is on one side of the cut" step, but nothing is thrown away.

```python
# python
# LC 776 - Split BST
# IDEA: root <= target -> root heads the small tree; split its right child and keep the small part;
#       root > target  -> root heads the big tree; split its left child and keep the big part
# time = O(h), space = O(h)
def splitBST(root, target):
    if not root:
        return [None, None]                 # [<= target, > target]
    if root.val <= target:
        small, big = splitBST(root.right, target)
        root.right = small                  # the part of the right subtree that stays with root
        return [root, big]
    small, big = splitBST(root.left, target)
    root.left = big                         # the part of the left subtree that stays with root
    return [small, root]
```

```java
// java
// LC 776 - Split BST
// IDEA: split only the child that straddles target, reattach the half that belongs with root
// time = O(h), space = O(h)
public TreeNode[] splitBST(TreeNode root, int target) {
    if (root == null) {
        return new TreeNode[]{null, null};
    }
    if (root.val <= target) {
        TreeNode[] split = splitBST(root.right, target);
        root.right = split[0];
        return new TreeNode[]{root, split[1]};
    }
    TreeNode[] split = splitBST(root.left, target);
    root.left = split[1];
    return new TreeNode[]{split[0], root};
}
```

```text
[4,2,6,1,3,5,7], target = 2
at 4: 4 > 2   -> split 4.left (2); 4 keeps the big half of it
at 2: 2 <= 2  -> split 2.right (3); 2 keeps the small half of it
at 3: 3 > 2   -> split 3.left (null) -> [null, null]; return [null, 3]
back at 2: 2.right = null           -> return [2, 3]
back at 4: 4.left = 3               -> return [2, 4]
small tree [2,1]          big tree [4,3,6,null,null,5,7]
```

**Traps**
- Reattaching the wrong half: when `root.val <= target`, `root.right` takes `split[0]` (the
  small half), not `split[1]`. Swapping them puts 3 under 2 in the example above.
- Splitting both children is O(n) and still correct; only one child can straddle the cut.

| Operation | LC | Nodes removed | Returns |
|---|---|---|---|
| Delete | 450 | one | one root |
| Trim | 669 | every node outside `[low, high]` | one root |
| Split | 776 | none | two roots |

## Validate with Bounds Examples

### Serialize and Deserialize BST — LC 449

> **Key point** — a BST's preorder alone determines the tree: the next value belongs in the
> current subtree exactly when it lies inside that subtree's `(low, high)` interval. So the
> encoding needs no null markers, which is what makes it more compact than LC 297's.

**Reduces to**: [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) — the same
interval carried down, used to place a value instead of to reject one.

```python
# python
# LC 449 - Serialize and Deserialize BST
# IDEA: serialize = preorder values only; deserialize = rebuild with (low, high) bounds:
#       the next value starts this subtree iff it fits the interval, else this subtree is empty
# time = O(n), space = O(n)
from collections import deque

class Codec:
    def serialize(self, root):
        vals = []

        def preorder(node):
            if not node:
                return
            vals.append(str(node.val))
            preorder(node.left)
            preorder(node.right)

        preorder(root)
        return ",".join(vals)

    def deserialize(self, data):
        vals = deque(int(x) for x in data.split(",")) if data else deque()

        def build(low, high):
            if not vals or not (low < vals[0] < high):
                return None                    # the next value belongs to an ancestor
            node = TreeNode(vals.popleft())
            node.left = build(low, node.val)
            node.right = build(node.val, high)
            return node

        return build(float("-inf"), float("inf"))
```

**Traps**
- `"".split(",")` is `[""]`, and `int("")` raises: the empty tree needs its own guard.
- Strict `<` assumes distinct values, which a BST guarantees. With duplicates, preorder alone no
  longer fixes the shape.
- LC 297's level-order codec with `#` markers also passes here, but it ignores the BST and
  writes roughly twice as many tokens. Say why the bounds make the markers unnecessary.

**Similar** — LC 1008 (the deserialize half on its own, [bst_advanced § Template 4](./bst_advanced.md#template-4-preorder-build-with-bounds--lc-1008)) · LC 297 (any binary tree: markers are needed)
