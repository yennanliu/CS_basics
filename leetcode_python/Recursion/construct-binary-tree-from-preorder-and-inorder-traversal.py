"""

105. Construct Binary Tree from Preorder and Inorder Traversal
Medium


Share
Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.

 
Example 1:


Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
Output: [3,9,20,null,null,15,7]
Example 2:

Input: preorder = [-1], inorder = [-1]
Output: [-1]
 

Constraints:

1 <= preorder.length <= 3000
inorder.length == preorder.length
-3000 <= preorder[i], inorder[i] <= 3000
preorder and inorder consist of unique values.
Each value of inorder also appears in preorder.
preorder is guaranteed to be the preorder traversal of the tree.
inorder is guaranteed to be the inorder traversal of the tree.

"""

# V0
# IDEA : BST property
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        ### NOTE : init root like below (via TreeNode and root value (preorder[0]))
        root = TreeNode(preorder[0])
        # get the index of root.val in order to SPLIT TREE
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        # recursion for root.left
        #### NOTE : WE idx from inorder in preorder as well 
        #### NOTE : preorder[1 : index + 1] (for left sub tree)
        root.left = self.buildTree(preorder[1 : index + 1], inorder[ : index]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the left-sub-tree of Preorder as well
        # recursion for root.right 
        root.right = self.buildTree(preorder[index + 1 : ], inorder[index + 1 :]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the right-sub-tree of Preorder as well
        return root

# V1
# https://blog.csdn.net/qqxx6661/article/details/75905524
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        root = TreeNode(preorder[0])
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        root.left = self.buildTree(preorder[1 : index + 1], inorder[0 : index]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the left-sub-tree of Preorder as well
        root.right = self.buildTree(preorder[index + 1 : len(preorder)], inorder[index + 1 : len(inorder)]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the right-sub-tree of Preorder as well
        return root

# V1'
# https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/solution/
class Solution:
    def buildTree(self, preorder: List[int], inorder: List[int]) -> TreeNode:

        def array_to_tree(left, right):
            nonlocal preorder_index
            # if there are no elements to construct the tree
            if left > right: return None

            # select the preorder_index element as the root and increment it
            root_value = preorder[preorder_index]
            root = TreeNode(root_value)


            preorder_index += 1

            # build left and right subtree
            # excluding inorder_index_map[root_value] element because it's the root
            root.left = array_to_tree(left, inorder_index_map[root_value] - 1)
            root.right = array_to_tree(inorder_index_map[root_value] + 1, right)

            return root

        preorder_index = 0

        # build a hashmap to store value -> its index relations
        inorder_index_map = {}
        for index, value in enumerate(inorder):
            inorder_index_map[value] = index

        return array_to_tree(0, len(preorder) - 1)

# V1''
# https://www.jiuzhang.com/solution/construct-binary-tree-from-preorder-and-inorder-traversal/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        this.val = val
        this.left, this.right = None, None
"""
from lintcode import TreeNode
class Solution:
    """
    @param preorder : A list of integers that preorder traversal of a tree
    @param inorder : A list of integers that inorder traversal of a tree
    @return : Root of a tree
    """
    def buildTree(self, preorder, inorder):
        # write your code here
        if not inorder: return None # inorder is empty
        root = TreeNode(preorder[0])
        rootPos = inorder.index(preorder[0])
        root.left = self.buildTree(preorder[1 : 1 + rootPos], inorder[ : rootPos])
        root.right = self.buildTree(preorder[rootPos + 1 : ], inorder[rootPos + 1 : ])
        return root

# V2
# Time:  O(n)
# Space: O(n)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param preorder, a list of integers
    # @param inorder, a list of integers
    # @return a tree node
    def buildTree(self, preorder, inorder):
        lookup = {}
        for i, num in enumerate(inorder):
            lookup[num] = i
        return self.buildTreeRecu(lookup, preorder, inorder, 0, 0, len(inorder))

    def buildTreeRecu(self, lookup, preorder, inorder, pre_start, in_start, in_end):
        if in_start == in_end:
            return None
        node = TreeNode(preorder[pre_start])
        i = lookup[preorder[pre_start]]
        node.left = self.buildTreeRecu(lookup, preorder, inorder, pre_start + 1, in_start, i)
        node.right = self.buildTreeRecu(lookup, preorder, inorder, pre_start + 1 + i - in_start, i + 1, in_end)
        return node

# time: O(n)
# space: O(n)
class Solution2(object):
    def buildTree(self, preorder, inorder):
        """
        :type preorder: List[int]
        :type inorder: List[int]
        :rtype: TreeNode
        """
        preorder_iterator = iter(preorder)
        inorder_lookup = {n: i for i, n in enumerate(inorder)}
        
        def helper(start, end):
            if start > end:
                return None         
            root_val = next(preorder_iterator)
            root = TreeNode(root_val)
            idx = inorder_lookup[root_val]
            root.left = helper(start, idx-1)
            root.right = helper(idx+1, end)
            return root
        return helper(0, len(inorder)-1)
