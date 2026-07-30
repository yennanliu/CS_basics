"""

814. Binary Tree Pruning
Solved
Medium
Topics
premium lock icon
Companies
Given the root of a binary tree, return the same tree where every subtree (of the given tree) not containing a 1 has been removed.

A subtree of a node node is node plus every node that is a descendant of node.

 

Example 1:


Input: root = [1,null,0,0,1]
Output: [1,null,0,null,1]
Explanation: 
Only the red nodes satisfy the property "every subtree not containing a 1".
The diagram on the right represents the answer.
Example 2:


Input: root = [1,0,1,0,0,0,1]
Output: [1,null,1,null,1]
Example 3:


Input: root = [1,1,0,1,1,0,1,0]
Output: [1,1,0,1,1,null,1]
 

Constraints:

The number of nodes in the tree is in the range [1, 200].
Node.val is either 0 or 1.

"""

# V0
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        pass


# V0-1
# IDEA: DFS (post order) (gpt)
class Solution(object):
    def pruneTree(self, root):
        if not root:
            return None

        # NOTE !! below
        if not self.dfs_helper(root):
            return None

        return root


    """
    NOTE !!!

    in this helper func,
    we return `boolean` 
        -> true: the sub child still existed (NOT prune)
        -> false: the sub child NOT existed (pruned)


    and we use `post order DFS`
    """
    def dfs_helper(self, root):
        if not root:
            return False

        left = self.dfs_helper(root.left)
        right = self.dfs_helper(root.right)

        if not left:
            root.left = None
        if not right:
            root.right = None

        # After pruning, if this node becomes a leaf
        if not root.left and not root.right:
            return root.val == 1

        return True


# V0-2
# IDEA: DFS (post order) (gemini)
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        # Edge case for empty tree
        if not root:
            return None
            
        # If the entire tree contains no 1s, the helper returns False. 
        # In that case, we must return None to prune the whole tree.
        tree_contains_one = self.dfs_helper(root)
        if not tree_contains_one:
            return None
            
        # Otherwise, the tree is properly pruned, return the root!
        return root

    def dfs_helper(self, node):
        # 1. Base case: An empty node contains no 1s
        if not node:
            return False
            
        # 2. Check the children (Post-order)
        left_has_one = self.dfs_helper(node.left)
        right_has_one = self.dfs_helper(node.right)
        
        # 3. Prune the children that evaluated to False
        if not left_has_one:
            node.left = None
            
        if not right_has_one:
            node.right = None
            
        # 4. Does the CURRENT subtree contain a 1?
        # It contains a 1 if: 
        # its own value is 1, OR its left side has a 1, OR its right side has a 1.
        return node.val == 1 or left_has_one or right_has_one



# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79858752
# IDEA : POSTORDER  + DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root: return
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if not root.left and not root.right and root.val == 0:
            return None
        return root

# V1'
# https://www.jiuzhang.com/solution/binary-tree-pruning/#tag-highlight-lang-python
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution:
    """
    @param root: the root
    @return: the same tree where every subtree (of the given tree) not containing a 1 has been removed
    """
    def pruneTree(self, root):
        if root is None:
            return None
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if root.val == 0 and root.left == None and root.right == None:
            return None 
        else:
            return root

# V1''
# http://bookshadow.com/weblog/2018/04/09/leetcode-binary-tree-pruning/
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root: return root
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        return root if root.left or root.right or root.val else None
        
# V2
# time = O(n)
# space = O(h)
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root:
            return None
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if not root.left and not root.right and root.val == 0:
            return None
        return root
