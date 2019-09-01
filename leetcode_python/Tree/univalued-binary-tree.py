# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85385974
# IDEA : BFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isUnivalTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        q = collections.deque()
        q.append(root)
        val = root.val
        while q:
            node = q.popleft()
            if not node:
                continue
            if val != node.val:
                return False
            q.append(node.left)
            q.append(node.right)
        return True

# V1' 
# https://www.jianshu.com/p/fcedb4635798
class Solution:
    def isUnivalTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        values = []
        
        def getValues(node):
            if node:
                values.append(node.val)
                getValues(node.left)
                getValues(node.right)
        
        getValues(root)
        return len(set(values)) == 1

# V1'' 
# https://www.jianshu.com/p/fcedb4635798
class Solution:
    def isUnivalTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        root_val = root.val
        
        def hasUniValue(node):
            if node:
                return node.val == root_val and hasUniValue(node.left) and hasUniValue(node.right)
            else:
                return True
        
        return hasUniValue(root) 

# V1''' 
# https://www.jianshu.com/p/fcedb4635798
def hasUniValue(node):
    return not node or (node.val == root_val and hasUniValue(node.left) and hasUniValue(node.right))

# V2 
# Time:  O(n)
# Space: O(h)
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution(object):
    def isUnivalTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        s = [root]
        while s:
            node = s.pop()
            if not node:
                continue
            if node.val != root.val:
                return False
            s.append(node.left)
            s.append(node.right)
        return True
    
# Time:  O(n)
# Space: O(h)
class Solution2(object):
    def isUnivalTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return (not root.left or (root.left.val == root.val and self.isUnivalTree(root.left))) and \
               (not root.right or (root.right.val == root.val and self.isUnivalTree(root.right)))
