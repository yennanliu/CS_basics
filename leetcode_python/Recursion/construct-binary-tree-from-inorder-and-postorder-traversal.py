"""

106. Construct Binary Tree from Inorder and Postorder Traversal
Medium

Given two integer arrays inorder and postorder where inorder is the inorder traversal of a binary tree and postorder is the postorder traversal of the same tree, construct and return the binary tree.



Example 1:


Input: inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
Output: [3,9,20,null,null,15,7]
Example 2:

Input: inorder = [-1], postorder = [-1]
Output: [-1]
 

Constraints:

1 <= inorder.length <= 3000
postorder.length == inorder.length
-3000 <= inorder[i], postorder[i] <= 3000
inorder and postorder consist of unique values.
Each value of postorder also appears in inorder.
inorder is guaranteed to be the inorder traversal of the tree.
postorder is guaranteed to be the postorder traversal of the tree.

"""

# V0
# IDEA : Binary Tree property, same as LC 105 
class Solution(object):
    def buildTree(self, inorder, postorder):
        if not inorder:
            return None
        if len(inorder) == 1:
            return TreeNode(inorder[0])
        ### NOTE : we get root from postorder
        root = TreeNode(postorder[-1])
        """
        ### NOTE : the index is from inorder
        ### NOTE : we get index of root in inorder
        #    -> and this idx CAN BE USED IN BOTH inorder, postorder (Binary Tree property)
        """
        idx = inorder.index(root.val)
        ### NOTE : inorder[:idx], postorder[:idx]
        root.left = self.buildTree(inorder[:idx], postorder[:idx])
        ### NOTE : postorder[idx:-1]
        root.right =  self.buildTree(inorder[idx+1:], postorder[idx:-1])
        return root

# V0'
class Solution(object):
    def buildTree(self, inorder, postorder):
        if len(postorder) == 0:
            return None
        if len(inorder) == 1:
            return TreeNode(inorder[0])
        root = TreeNode(postorder[-1]) # get the value of "root" via postorder
        index = inorder.index(postorder[-1]) # get the index of "root" in inorder 
        root.left = self.buildTree(inorder[  : index ], postorder[ : index ])
        root.right = self.buildTree(inorder[ index + 1 : ], postorder[ index : - 1 ])
        return root
        
# V0' 
class Solution(object):
    def buildTree(self, inorder, postorder):
        if len(inorder) == 0:
            return None
        if len(inorder) == 1:
            return TreeNode(inorder[0])
        root = TreeNode(postorder[len(postorder) - 1]) # get the value of "root" via postorder
        index = inorder.index(postorder[len(postorder) - 1]) # get the index of "root" in inorder 
        root.left = self.buildTree(inorder[ 0 : index ], postorder[ 0 : index ])
        root.right = self.buildTree(inorder[ index + 1 : len(inorder) ], postorder[ index : len(postorder) - 1 ])
        return root
        
# V1 
# https://blog.csdn.net/qqxx6661/article/details/75905524
# https://www.cnblogs.com/zuoyuan/p/3720138.html
# IDEA : IN-ORDER AND POST-ORDER
# DEMO : 
# CONSIDER A BST : {1,2,3,4,5,6,7}
# SO THE INORDER OF BST IS : {4,2,5,1,6,3,7}
# AND THE POSTORDER OF BST IS : {4,5,2,6,7,3,1}
# -> SO WE CAN KNOW THE ROOT OF BST IS "1" (postorder[len(postorder)[:-1]])
# -> SO WE CAN KNOW HOW TO SPLIT LEFT-SUB-TREE AND RIGHT-SUB-TREE BOTH IN INORDER AND POSTORDER
# i.e. INORDER :  [left-sub-tree]root[right-sub-tree]
# i.e. POSTORDER :  [left-sub-tree][right-sub-tree]root
# -> SO WE CAN RE-BUILD THE BST FROM INORDER AND POSTORDER AND INFROMATION ABOVE
class Solution(object):
    def buildTree(self, inorder, postorder):
        if len(inorder) == 0:
            return None
        if len(inorder) == 1:
            return TreeNode(inorder[0])
        root = TreeNode(postorder[len(postorder) - 1]) # get the value of "root" via postorder
        index = inorder.index(postorder[len(postorder) - 1]) # get the index of "root" in inorder 
        root.left = self.buildTree(inorder[ 0 : index ], postorder[ 0 : index ])
        root.right = self.buildTree(inorder[ index + 1 : len(inorder) ], postorder[ index : len(postorder) - 1 ])
        return root

# V1'
# https://blog.csdn.net/qqxx6661/article/details/75905524
# IDEA : PRE-ORDER AND IN-ORDER 
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        root = TreeNode(preorder[0])
        index = inorder.index(root.val) # the root place for mid-order. it's left is left-sub-tree ; right is right-sub-tree
        root.left = self.buildTree(preorder[1 : index + 1], inorder[0 : index])
        root.right = self.buildTree(preorder[index + 1 : len(preorder)], inorder[index + 1 : len(inorder)])
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
    # @param inorder, a list of integers
    # @param postorder, a list of integers
    # @return a tree node
    def buildTree(self, inorder, postorder):
        lookup = {}
        for i, num in enumerate(inorder):
            lookup[num] = i
        return self.buildTreeRecu(lookup, postorder, inorder, len(postorder), 0, len(inorder))

    def buildTreeRecu(self, lookup, postorder, inorder, post_end, in_start, in_end):
        if in_start == in_end:
            return None
        node = TreeNode(postorder[post_end - 1])
        i = lookup[postorder[post_end - 1]]
        node.left = self.buildTreeRecu(lookup, postorder, inorder, post_end - 1 - (in_end - i - 1), in_start, i)
        node.right = self.buildTreeRecu(lookup, postorder, inorder, post_end - 1, i + 1, in_end)
        return node