# https://leetcode.ca/all/776.html


r"""

LC 776 - Split BST

Problem Description
Given a Binary Search Tree (BST) with root node root, and a target value V, split the tree into two subtrees where one subtree has nodes that are all smaller or equal to the target value, while the other subtree has all nodes that are greater than the target value. It’s not necessarily the case that the tree contains a node with value V.

Additionally, most of the structure of the original tree should remain. Formally, for any child C with parent P in the original tree, if they are both in the same subtree after the split, then node C should still have the parent P.

You should output the root TreeNode of both subtrees after splitting, in any order.

Example 1:

Input: root = [4,2,6,1,3,5,7], V = 2
Output: [[2,1],[4,3,6,null,null,5,7]]
Explanation:
Note that root, output[0], and output[1] are TreeNode objects, not arrays.

The given tree [4,2,6,1,3,5,7] is represented by the following diagram:

          4
        /   \
      2      6
     / \    / \
    1   3  5   7

while the diagrams for the outputs are:

          4
        /   \
      3      6      and    2
            / \           /
           5   7         1
Note:

The size of the BST will not exceed 50.
The BST is always valid and each node’s value is different.

"""


r"""

#-------------------------------
# CORE IDEA:
#-------------------------------

```

-> 為什麼 `root.right = split[0]` ??


> **root 自己已經 <= target，所以 root 留在左邊。**
>
> 但是 root 的右子樹可能需要切開。
>
> 切完後：
>
> * `split[0]`：還可以留在 root 右邊 → 接回去
> * `split[1]`：太大了，不能留在 root 下面 → 當作右 partition 回傳
```



這裡的關鍵是理解 **LC 776: Split BST** 的 `split(root, target)` 到底保證什麼。

### 先記住 split 的目標

我們要把 BST 分成兩棵樹：

```text
split(root, target)
    ↓
[左樹, 右樹]

左樹：所有 node.val <= target
右樹：所有 node.val > target
```

---

### 情況：`root.val <= target`

假設現在：

```text
        root = 5
       /    \
      3      8
            / \
           6   10
```

target = 7。

因為：

```text
root.val = 5 <= 7
```

所以 **root 本身一定屬於左 partition**。

但是 `root.right` 裡面可能同時有：

```text
<= 7 的 node
> 7 的 node
```

所以我們對 `root.right` 做 split：

```text
split(root.right, 7)
```

得到：

```text
split[0] = 所有 <= 7 的 node
split[1] = 所有 > 7 的 node
```

在這個例子：

```text
root.right:

      8
     / \
    6   10

split(8, 7)

split[0]:
    6

split[1]:
    8
     \
      10
```

---

#-------------------------------------
### 為什麼 `root.right = split[0]`？
#-------------------------------------


原本：

```text
        5
       / \
      3   8
         / \
        6   10
```

我們已經知道：

```text
5 <= 7
```

所以 `5` 要留在左 partition。

而 `5` 的右邊，只能接 **仍然 <= 7 的 nodes**。

所以：

```text
root.right = split[0]
```

變成：

```text
        5
       / \
      3   6
```

這棵樹裡全部都是：

```text
<= 7
```

---

### 那 `split[1]` 呢？

`split[1]` 是：

```text
所有 > target 的 nodes
```

也就是：

```text
    8
     \
      10
```

所以最後：

```text
return [root, split[1]]
```

其實就是：

```text
[
    左 partition,
    右 partition
]
```

也就是：

```text
        5                 8
       / \                 \
      3   6                 10
```

左邊全部：

```text
<= 7
```

右邊全部：

```text
> 7
```

---

### 最重要的直覺

你可以把這段：

```text
root.right = split[0]
return [root, split[1]]
```

理解成：

> **root 自己已經 <= target，所以 root 留在左邊。**
>
> 但是 root 的右子樹可能需要切開。
>
> 切完後：
>
> * `split[0]`：還可以留在 root 右邊 → 接回去
> * `split[1]`：太大了，不能留在 root 下面 → 當作右 partition 回傳

所以：

```text
root.right = split[0]
```

就是：

> 「把還符合 `<= target` 的部分接回 root。」

而：

```text
return [root, split[1]]
```

就是：

> 「root 現在代表完整的左 partition；`split[1]` 是完整的右 partition。」

---

### 為什麼不能 `return [root, root.right]`？

因為 `root.right` 已經被改成：

```text
split[0]
```

它是 **<= target 的部分**，不是右 partition。

真正的右 partition 是：

```text
split[1]
```

所以一定是：

```java
root.right = split[0];
return new TreeNode[]{root, split[1]};
```

一句話記：

> **`root <= target` → root 留左邊；把右子樹 split，`split[0]` 接回 root，`split[1]` 拿出去當右樹。**


"""



# V0
class Solution(object):
    def splitBST(self, root, V):
        pass


# V0-0-1
# IDEA : BST properties (left < root < right) + recursion (GEMINI)
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: Optional[TreeNode]
        :type V: int
        :rtype: List[Optional[TreeNode]]
        """

        if not root:
            return [None, None]

        return self.helper(root, V)

    def helper(self, root, V):
        if not root:
            return [None, None]

        # Case 1:
        # root.val <= V
        #
        # root belongs to the SMALL tree.
        # Because this is a BST, everything in root.left
        # is also <= root.val <= V.
        #
        # We only need to split root.right.
        if root.val <= V:
            small, big = self.helper(root.right, V)

            # Connect the <= V part back to root
            root.right = small

            return [root, big]

        # Case 2:
        # root.val > V
        #
        # root belongs to the BIG tree.
        # Everything in root.right is also > root.val > V.
        #
        # We only need to split root.left.
        small, big = self.helper(root.left, V)

        # Connect the > V part back to root
        root.left = big

        return [small, root]

# V0
# IDEA : BST properties (left < root < right) + recursion (GEMINI)
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: TreeNode
        :type V: int
        :rtype: List[TreeNode]
        """
        # FIX 1: Base case must return a matching pair of elements [small, big]
        if not root:
            return [None, None]
            
        # Case 1: The current root belongs to the SMALL tree
        if root.val <= V:
            # Recursively split the right subtree
            small_part, big_part = self.splitBST(root.right, V)
            
            # The smaller pieces from the right side attach to root's right
            root.right = small_part
            
            # root is now the complete head of the small tree group
            return [root, big_part]
            
        # Case 2: The current root belongs to the BIG tree
        else:
            # Recursively split the left subtree
            small_part, big_part = self.splitBST(root.left, V)
            
            # The bigger pieces from the left side attach to root's left
            root.left = big_part
            
            # root is now the complete head of the big tree group
            return [small_part, root]


# V0-0-1
# IDEA : BST properties (left < root < right) + recursion (GPT)
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return [None, None]

        if root.val <= V:
            # Split the right subtree.
            left, right = self.splitBST(root.right, V)

            # root belongs to the <= V tree.
            root.right = left

            # root is the root of the left result.
            return [root, right]

        else:
            # Split the left subtree.
            left, right = self.splitBST(root.left, V)

            # root belongs to the > V tree.
            root.left = right

            # left is the <= V tree, root is the > V tree.
            return [left, root]


# V0-0-1
# IDEA: (gemini)
class Solution(object):
    def generateTrees(self, n):
        """
        :type n: int
        :rtype: List[Optional[TreeNode]]
        """
        if n == 0:
            return []
        return self.helper(1, n)

    def helper(self, start, end):
        # Base case: empty subtree, represented by a list containing None
        if start > end:
            return [None]

        all_trees = []

        # Pick every number 'i' in range [start, end] as the root value
        for i in range(start, end + 1):
            # 1. Generate all possible left subtrees with values < i
            left_trees = self.helper(start, i - 1)

            # 2. Generate all possible right subtrees with values > i
            right_trees = self.helper(i + 1, end)

            # 3. Combine each left subtree and right subtree with root 'i'
            for left in left_trees:
                for right in right_trees:
                    root = TreeNode(i)
                    root.left = left
                    root.right = right
                    all_trees.append(root)

        return all_trees


# V0-1
# IDEA : BST properties (left < root < right) + recursion (GPT)
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        return self.helper(root, V)

    def helper(self, root, V):
        if not root:
            return None, None

        if root.val <= V:
            small, big = self.helper(root.right, V)
            root.right = small
            return root, big

        else:
            small, big = self.helper(root.left, V)
            root.left = big
            return small, root


# V0
# IDEA : BST properties (left < root < right) + recursion
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
### STEPS
#  -> 1) check whether root.val > or < V
#     -> if root.val > V : 
#           - NO NEED TO MODIFY ALL RIGHT SUB TREE
#           - BUT NEED TO re-connect nodes in LEFT SUB TREE WHICH IS BIGGER THAN V (root.left = right)
#     -> if root.val < V : 
#           - NO NEED TO MODIFY ALL LEFT SUB TREE
#           - BUT NEED TO re-connect nodes in RIGHT SUB TREE WHICH IS SMALLER THAN V (root.right = left)
# -> 2) return result
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        ### NOTE : if root.val <= V
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        ### NOTE : if root.val > V
        else:
            left, right = self.splitBST(root.right, V)
            root.right = left
            return [root, right]

# V0'
# IDEA : BST properties (left < root < right) + recursion
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        ### NOTE : if root.val <= V
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        ### NOTE : if root.val > V
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root

# V0'
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        elif root.val > V:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root

# V0''
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        left, right = self.splitBST(root.right, V)
        root.right = left
        return [root, right]

# V1
# http://bookshadow.com/weblog/2018/02/04/leetcode-split-bst/
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(h)  # h = tree height, BST-guided single-branch recursion
# space = O(h)  # recursion stack
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: TreeNode
        :type V: int
        :rtype: List[TreeNode]
        """
        if not root: return [None, None]
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        left, right = self.splitBST(root.right, V)
        root.right = left
        return [root, right]

### Test case : dev 

# V1'
# https://blog.csdn.net/magicbean2/article/details/79679927
# JAVA
# /**
#  * Definition for a binary tree node.
#  * struct TreeNode {
#  *     int val;
#  *     TreeNode *left;
#  *     TreeNode *right;
#  *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
#  * };
#  */
# class Solution {
# public:
#     vector<TreeNode*> splitBST(TreeNode* root, int V) {
#         vector<TreeNode *> res(2, NULL);
#         if(root == NULL) {
#             return res;
#         }
#         if(root->val > V) {         // the right child is retained
#             res[1] = root;
#             auto res1 = splitBST(root->left, V);
#             root->left = res1[1];
#             res[0]=res1[0];
#         }
#         else {                      // the left child is retained
#             res[0] = root;
#             auto res1 = splitBST(root->right, V);
#             root->right = res1[0];
#             res[1] = res1[1];
#         }
#         return res;
#     }
# };

# V1''
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
# C++
# class Solution {
# public:
#     vector<TreeNode*> splitBST(TreeNode* root, int V) {
#         vector<TreeNode*> res{NULL, NULL};
#         if (!root) return res;
#         if (root->val <= V) {
#             res = splitBST(root->right, V);
#             root->right = res[0];
#             res[0] = root;
#         } else {
#             res = splitBST(root->left, V);
#             root->left = res[1];
#             res[1] = root;
#         }
#         return res;
#     }
# };

# V1'''
# https://www.acwing.com/solution/LeetCode/content/208/
# IDEA : JAVA
# class Solution {
#     public TreeNode[] splitBST(TreeNode root, int V) {
#         TreeNode[] ans = new TreeNode[2];
#         return dfs(root, V);
#     }

#     public TreeNode[] dfs(TreeNode node, int v) {
#         TreeNode[] ans = new TreeNode[2];
#         if(node==null) return ans;

#         if(node.val>v) {
#             ans[1] = node;
#             TreeNode left = node.left;
#             node.left = null;
#             TreeNode[] nodes = dfs(left, v);
#             node.left = nodes[1];
#             ans[0] = nodes[0];
#         }else {
#             ans[0] = node;
#             TreeNode right = node.right;
#             node.right = null;
#             TreeNode[] nodes = dfs(right, v);
#             node.right = nodes[0];
#             ans[1]=nodes[1];
#         }
#         return ans;
#     }
#
# }


# V2
# time = O(n)
# space = O(h)
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: TreeNode
        :type V: int
        :rtype: List[TreeNode]
        """
        if not root:
            return None, None
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root
