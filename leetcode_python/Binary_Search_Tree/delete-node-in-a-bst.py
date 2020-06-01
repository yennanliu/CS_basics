# V0 
class Solution(object):
    def deleteNode(self, root, key):
        if not root: return None
        if root.val == key:
            if not root.right:
                left = root.left
                return left
            else:
                right = root.right
                # find min in "right" sub-tree
                while right.left:
                    right = right.left
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root

# V0'
class Solution(object):
    def deleteNode(self, root, key):

        if not root:
            return root

        if root.val > key:
            root.left = self.deleteNode(root.left, key)
        elif root.val < key:
            root.right = self.deleteNode(root.right, key)
        else:
            if not root.left:
                right = root.right
                del root
                return right
            elif not root.right:
                left = root.left
                del root
                return left
            else:
                successor = root.right
                while successor.left:
                    successor = successor.left

                root.val = successor.val
                root.right = self.deleteNode(root.right, successor.val)
        return root

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79670068
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def deleteNode(self, root, key):
        """
        :type root: TreeNode
        :type key: int
        :rtype: TreeNode
        """
        if not root: return None
        if root.val == key:
            if not root.right:
                left = root.left
                return left
            else:
                right = root.right
                while right.left:
                    right = right.left
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
   
### Test case : dev 
    
# V1'
# https://leetcode.com/problems/delete-node-in-a-bst/discuss/93374/Simple-Python-Solution-With-Explanation
class Solution(object):
       def deleteNode(self,root, key):
            if not root: # if root doesn't exist, just return it
                return root
            if root.val > key: # if key value is less than root value, find the node in the left subtree
                root.left = self.deleteNode(root.left, key)
            elif root.val < key: # if key value is greater than root value, find the node in right subtree
                root.right= self.deleteNode(root.right, key)
            else: #if we found the node (root.value == key), start to delete it
                if not root.right: # if it doesn't have right children, we delete the node then new root would be root.left
                    return root.left
                if not root.left: # if it has no left children, we delete the node then new root would be root.right
                    return root.right
                       # if the node have both left and right children,  we replace its value with the minmimum value in the right subtree and then delete that minimum node in the right subtree
                temp = root.right
                mini = temp.val
                while temp.left:
                    temp = temp.left
                    mini = temp.val
                root.val = mini # replace value
                root.right = self.deleteNode(root.right,root.val) # delete the minimum node in right subtree
            return root

# V1''
# https://leetcode.com/problems/delete-node-in-a-bst/discuss/213685/Clean-Python-3-with-comments-in-details
class Solution:
    def deleteNode(self, root, key):
        """
        :type root: TreeNode
        :type key: int
        :rtype: TreeNode
        """
        if not root:
            return
        
        # we always want to delete the node when it is the root of a subtree,
        # so we handle left or right according to the val.
        # if the node does not exist, we will hit the very first if statement and return None.
        if key > root.val:
            root.right = self.deleteNode(root.right, key)
            
        elif key < root.val:
            root.left = self.deleteNode(root.left, key)
        
        # now the key is the root of a subtree
        else:
            # if the subtree does not have a left child, we just return its right child
            # to its father, and they will be connected on the higher level recursion.
            if not root.left:
                return root.right
            
            # if it has a left child, we want to find the max val on the left subtree to 
            # replace the node we want to delete.
            else:
                # try to find the max value on the left subtree
                tmp = root.left
                while tmp.right:
                    tmp = tmp.right
                    
                # replace
                root.val = tmp.val
                
                # since we have replaced the node we want to delete with the tmp, now we don't
                # want to keep the tmp on this tree, so we just use our function to delete it.
                # pass the val of tmp to the left subtree and repeat the whole approach.
                root.left = self.deleteNode(root.left, tmp.val)
        
        return root

# V1'''
class Solution(object):
    def deleteNode(self, root, key):
        """
        :type root: TreeNode
        :type key: int
        :rtype: TreeNode
        """
        if not root: return None
        
        if root.val == key:
            if root.left:
                # Find the right most leaf of the left sub-tree
                left_right_most = root.left
                while left_right_most.right:
                    left_right_most = left_right_most.right
                # Attach right child to the right of that leaf
                left_right_most.right = root.right
                # Return left child instead of root, a.k.a delete root
                return root.left
            else:
                return root.right
        # If left or right child got deleted, the returned root is the child of the deleted node.
        elif root.val > key:
            root.left = self.deleteNode(root.left, key)
        else:
            root.right = self.deleteNode(root.right, key)
            
        return root

# V2 
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/delete-node-in-a-bst.py
# Time:  O(h)
# Space: O(h)
class Solution(object):
    def deleteNode(self, root, key):
        """
        :type root: TreeNode
        :type key: int
        :rtype: TreeNode
        """
        if not root:
            return root

        if root.val > key:
            root.left = self.deleteNode(root.left, key)
        elif root.val < key:
            root.right = self.deleteNode(root.right, key)
        else:
            if not root.left:
                right = root.right
                del root
                return right
            elif not root.right:
                left = root.left
                del root
                return left
            else:
                successor = root.right
                while successor.left:
                    successor = successor.left

                root.val = successor.val
                root.right = self.deleteNode(root.right, successor.val)

        return root