# V0 
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
