"""

1676. Lowest Common Ancestor of a Binary Tree IV
Medium

Given the root of a binary tree and an array of TreeNode objects nodes, return the lowest common
ancestor (LCA) of all the nodes in nodes. All the nodes will exist in the tree, and all values of
the tree's nodes are unique.

Extending the definition of LCA on Wikipedia: "The lowest common ancestor of n nodes p1, p2, ..., pn
in a binary tree T is the lowest node that has every pi as a descendant (where we allow a node to be
a descendant of itself) for every valid i". A descendant of a node x is a node y that is on the path
from node x to some leaf node.


Example 1:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [4,7]
Output: 2
Explanation: The lowest common ancestor of nodes 4 and 7 is node 2.

Example 2:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [1]
Output: 1
Explanation: The lowest common ancestor of a single node is the node itself.

Example 3:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [7,6,2,4]
Output: 5
Explanation: The lowest common ancestor of the nodes 7, 6, 2, and 4 is node 5.


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
-10^9 <= Node.val <= 10^9
All Node.val are unique.
All nodes[i] will exist in the tree.
All nodes[i] are distinct.

"""

# V0-1
# IDEA: LCA + set + (post order) DFS (gpt)
"""

## 🧪 Dry Run 追蹤範例

### 1. 測資設定

* **二元樹結構**：
```text
    3 (root)
   / \
  5   1
 / \
6   4

```


* **目標節點**：`nodes = [6, 4]`, `node_set = {6, 4}`

---

### 2. 遞迴呼叫與回傳過程（後序走訪 Bottom-Up）

| 執行步驟 | 當前節點 `curr` | 檢查 `curr in node_set` | 遞迴分流 `left` / `right` | 條件判斷與回傳值 |
| --- | --- | --- | --- | --- |
| **1** | `dfs(3)` | `3` 不在集合 | 呼叫 `dfs(5)` 與 `dfs(1)` | 等待子樹回傳 |
| **2** | `dfs(5)` | `5` 不在集合 | 呼叫 `dfs(6)` 與 `dfs(4)` | 等待子樹回傳 |
| **3** | `dfs(6)` | **`6` 在集合中** | 略過子樹 | **`return Node(6)`** |
| **4** | `dfs(4)` | **`4` 在集合中** | 略過子樹 | **`return Node(4)`** |
| **5** | 回到 `dfs(5)` | - | `left = Node(6)`, `right = Node(4)` | `left and right` 成立 $\to$ **`return Node(5)`** |
| **6** | 進入 `dfs(1)` | `1` 不在集合 | 左右子樹均為 `None` | `left or right` $\to$ **`return None`** |
| **7** | 回到 `dfs(3)` | - | `left = Node(5)`, `right = None` | `left and right` 不成立 $\to$ `return left or right` $\to$ **`return Node(5)`** |

> **最終輸出**：`Node(5)` ✅

---

### 💡 核心收斂機制解析

1. **短路命中（Early Hit）**：當碰到屬於 `node_set` 的節點（如 `6` 或 `4`），直接往上回傳該節點，**不再往下深究**。
2. **交會點收斂（Convergence）**：在節點 `5` 處，`left` 拿到 `6`，`right` 拿到 `4` $\to$ `left and right` 觸發，代表多個目標散落不同子樹，當前節點 `5` 即為解答。
3. **單邊氣泡上升（Bubble Up）**：若所有目標都在同一側（例如找 `[6]` 而無 `4`），會一路被 `left or right` 往上傳遞，最終根節點拿到唯一的單側代表。

"""
class Solution(object):
    def lowestCommonAncestor(self, root, nodes):
        """
        :type root: TreeNode
        :type nodes: List[TreeNode]
        :rtype: TreeNode
        """

        if not root or not nodes:
            return None


        """
        NOTE !!!


        use `set` for O(1) lookup time complexity
        """
        # Convert nodes to a set for O(1) lookup
        target = set(nodes)

        return self.helper(root, target)

    def helper(self, root, target):
        if not root:
            return None

        """
        NOTE !!!


        if note is in set,
        return root directly.
        """
        # Current node is one of the target nodes
        if root in target:
            return root

        left = self.helper(root.left, target)
        right = self.helper(root.right, target)

        # Target nodes exist on both sides
        if left and right:
            return root

        # Target nodes only exist on one side
        if left:
            return left

        if right:
            return right

        return None


# V0-2
# IDEA: LCA + DFS (post order) (gemini)
class Solution(object):

  def lowestCommonAncestor(self, root, nodes):
    """
        :type root: TreeNode
        :type nodes: List[TreeNode]
        :rtype: TreeNode
        """
    # 將目標節點放入 set 中，方便以 O(1) 時間判斷當前節點是否為目標之一
    target_set = set(nodes)

    def helper(node):
      # 基地條件：若節點為空，或當前節點就是我們要找的目標之一
      if not node or node in target_set:
        return node

      # 遞迴搜尋左右子樹
      left = helper(node.left)
      right = helper(node.right)

      # 如果左右子樹分別都找到了目標節點，代表當前節點就是它們的最低共同祖先
      if left and right:
        return node

      # 否則回傳不為空的那個分支（代表目標集中在單一側，或兩者皆無）
      return left if left else right

    return helper(root)



# V0-3
# IDEA : DFS "BUBBLE UP A HIT" (the classic LCA recursion, targets held in a set)
#
#   dfs(node) returns the LCA of all targets found inside node's subtree,
#   or None if the subtree contains none.
#     - node is itself a target -> return node (a node may be its own ancestor,
#       and any deeper target is already covered by node)
#     - both children return non-None -> the split happens here -> return node
#     - otherwise -> forward whichever side found something
#
#   NOTE : values are unique, so a set of target VALUES is a safe membership test.
#   NOTE : this works for any number of targets, not just two -- the moment two
#          branches both report a hit, that node dominates all of them.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def lowestCommonAncestor(self, root, nodes):
        targets = set()
        for node in nodes:
            targets.add(node.val)

        def dfs(node):
            if node is None or node.val in targets:
                return node
            left = dfs(node.left)
            right = dfs(node.right)
            if left and right:
                return node
            return left or right

        return dfs(root)


# V0-4
# runs 二元 LCA 折疊法（Folding）+ post order DFS (gpt)
# NOTE !!! below works, but poor performance, and code is neither clean and elegant
"""
time: O(K * N), k = len(nodes), N= tree height/ nodes cnt
space: O(H)
"""
class Solution(object):
    def lowestCommonAncestor(self, root, nodes):
        # Edge cases
        if not root:
            return None

        if not nodes:
            return None

        if len(nodes) == 1:
            return nodes[0]

        # First LCA
        pca = self.helper(root, nodes[0], nodes[1])

        # Merge the remaining nodes one by one
        for i in range(2, len(nodes)):
            pca = self.helper(root, pca, nodes[i])

        return pca

    def helper(self, root, p, q):
        if not root:
            return None

        # Found p or q
        if root == p or root == q:
            return root

        left = self.helper(root.left, p, q)
        right = self.helper(root.right, p, q)

        # p and q are on different sides
        if left and right:
            return root

        # Only one side has p or q
        if left:
            return left

        if right:
            return right

        # Neither p nor q exists in this subtree
        return None