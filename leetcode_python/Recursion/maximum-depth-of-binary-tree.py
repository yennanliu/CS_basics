# V0
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):

        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

# V1
# https://blog.csdn.net/coder_orz/article/details/51337420
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

### Test case  (TODO : write test for tree data structure)
s=Solution()
assert s.maxDepth([3,9,20,None,None,15,7]) == 3 
assert s.maxDepth([]) == 0
assert s.maxDepth([1]) == 1 
assert s.maxDepth([1,2,3]) == 2
assert s.maxDepth([1,2,3,4,5]) == 3

# V1'
# https://blog.csdn.net/coder_orz/article/details/51337420
# IDEA : BFS
class Solution(object):
    def maxDepth(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if root == None:
            return 0

        depth = 0
        q = [root]
        while len(q) != 0:
            depth += 1
            for i in range(0, len(q)):
                if q[0].left:
                    q.append(q[0].left)
                if q[0].right:
                    q.append(q[0].right)
                del q[0]
        return depth

# V1''
# https://blog.csdn.net/qqxx6661/article/details/75676272
class Solution(object):
    level_true = 0
    def preorder(self, root, level, level_true):
        if root:
            if level_true < level+1: 
                #print(level_true)
                self.level_true += 1
            self.preorder(root.left, level+1, self.level_true)
            self.preorder(root.right, level+1, self.level_true)
    def maxDepth(self, root):
        self.preorder(root, 0, self.level_true)
        return self.level_true

# V2
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @return an integer
    def maxDepth(self, root):
        if root is None:
            return 0
        else:
            return max(self.maxDepth(root.left), self.maxDepth(root.right)) + 1
