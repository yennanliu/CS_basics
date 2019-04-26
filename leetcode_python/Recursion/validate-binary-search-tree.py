# V0 : DEV 



# V1
# https://blog.csdn.net/yangjingjing9/article/details/77096057
#Definition for a binary tree node.
classTreeNode(object):
     def __init__(self, x):
         self.val = x
         self.left = None
         self.right = None
 
classSolution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, None, None)
               
    def valid(self, root, min, max):
        if root == None or root.val == None:
            return True
       
        if (min is not None and root.val <=min) or (max is not None and root.val >= max):
            print(1)
            return False
       
        return self.valid(root.left, min,root.val) and self.valid(root.right, root.val, max)
         
# if__name__ == '__main__':
#     S = Solution()
#     l1 = TreeNode(9)
#     l2 = TreeNode(5)
#     l3 = TreeNode(11)
#     l4 = TreeNode(2)
#     l5 = TreeNode(7)
#     l6 = TreeNode(10)
#     l7 = TreeNode(14)
#     l8 = TreeNode(1)
#     l9 = TreeNode(3)
#     l10 = TreeNode(6)
#     l11 = TreeNode(8)
#     l12 = TreeNode(12)
#     l13 = TreeNode(15)
   
#     root = l1
   
#     l1.left = l2
#     l1.right = l3
   
#     l2.left = l4
#     l2.right = l5
#     l3.left = l6
#     l3.right = l7
   
#     l4.left = l8
#     l4.right = l9
#     l5.left = l10
#     l5.right = l11
#     l7.left = l12
#     l7.right = l13
   
#     S.isValidBST(root) 

# V2 
# Time:  O(n)
# Space: O(1)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# Morris Traversal Solution
class Solution(object):
    # @param root, a tree node
    # @return a list of integers
    def isValidBST(self, root):
        prev, cur = None, root
        while cur:
            if cur.left is None:
                if prev and prev.val >= cur.val:
                    return False
                prev = cur
                cur = cur.right
            else:
                node = cur.left
                while node.right and node.right != cur:
                    node = node.right

                if node.right is None:
                    node.right = cur
                    cur = cur.left
                else:
                    if prev and prev.val >= cur.val:
                        return False
                    node.right = None
                    prev = cur
                    cur = cur.right

        return True

# Time:  O(n)
# Space: O(h)
class Solution2(object):
    # @param root, a tree node
    # @return a boolean
    def isValidBST(self, root):
        return self.isValidBSTRecu(root, float("-inf"), float("inf"))

    def isValidBSTRecu(self, root, low, high):
        if root is None:
            return True

        return low < root.val and root.val < high \
            and self.isValidBSTRecu(root.left, low, root.val) \
            and self.isValidBSTRecu(root.right, root.val, high)

