"""

104. Maximum Depth of Binary Tree
Easy

Given the root of a binary tree, return its maximum depth.

A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

 

Example 1:


Input: root = [3,9,20,null,null,15,7]
Output: 3
Example 2:

Input: root = [1,null,2]
Output: 2
 

Constraints:

The number of nodes in the tree is in the range [0, 104].
-100 <= Node.val <= 100


"""


# V0
# IDEA: DFS (post order)
# NOTE !!! we DON'T need global var
class Solution(object):
    def maxDepth(self, root):
        # Base case: empty tree has depth 0
        if not root:
            return 0

        # Compute depth of left subtree
        left_depth = self.maxDepth(root.left)

        # Compute depth of right subtree
        right_depth = self.maxDepth(root.right)

        # Current node contributes +1 level
        return max(left_depth, right_depth) + 1


# V0-0-1
# IDEA: DFS (post order)
# NOTE !!! we DON'T need global var
class Solution(object):
    def maxDepth(self, root):
        # Base case: empty tree has depth 0
        if not root:
            return 0

        # Compute depth of left subtree
        left_depth = self.maxDepth(root.left) + 1

        # Compute depth of right subtree
        right_depth = self.maxDepth(root.right) + 1

        # Current node contributes +1 level
        return max(left_depth, right_depth)


# V0-1
# IDEA: DFS (post order) with attr var
# NOTE !!! we init `max_depth_tracker` everytime when the function is called
#          so the `max_depth_tracker` in test cases NOT affect each other
class Solution(object):
    def maxDepth(self, root):
        # CRITICAL FIX: Initialize your tracker inside the method.
        # This resets the counter to 0 for every fresh LeetCode test case!
        self.max_depth_tracker = 0
        
        # Start our recursive helper function at the root with an initial depth of 1
        self.traverse(root, 1)
        
        return self.max_depth_tracker

    def traverse(self, node, current_depth):
        if not node:
            return
            
        # Update our tracking variable if we've found a deeper path
        self.max_depth_tracker = max(self.max_depth_tracker, current_depth)
        
        # Recurse down to children, incrementing the depth level by 1
        self.traverse(node.left, current_depth + 1)
        self.traverse(node.right, current_depth + 1)


# V0
# IDEA : BFS
# compare with LC 111 : mininum Depth of Binary Tree
class Solution(object):
    def maxDepth(self, root):
        # edge case
        if not root:
            return 0
        if root and not root.left and not root.right:
            return 1
        layer = 1
        q = [[layer, root]]
        res = []
        while q:
            for i in range(len(q)):
                layer, tmp = q.pop(0)
                """
                NOTE !!! : via below condition, we get "layer" of " A leaf is a node with no children."
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(layer)
                if tmp.left:
                    q.append([layer+1, tmp.left])
                if tmp.right:
                    q.append([layer+1, tmp.right])
        # get min
        #print ("res = " + str(res))
        return max(res)

# V0
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):

        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

# V0'
# bfs
class Solution(object):
    def maxDepth(self, root):
        # edge case
        if not root:
            return 0
        res = 0
        layer = 0
        q = [[root, layer]]
        while q:
            for i in range(len(q)):
                tmp, layer = q.pop(0)
                res = max(res, layer)
                if tmp.left:
                    q.append([tmp.left, layer+1])
                if tmp.right:
                    q.append([tmp.right, layer+1])                   
        return res + 1

# V0''
# IDEA : BFS
class Solution(object):
    def maxDepth(self, root):
        if not root:
            return 0
        _layer = 0
        q = [root]
        while q:
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                if tmp.right:
                    q.append(tmp.right)
            _layer += 1
        return _layer

# V0''''
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):
        def dfs(root, _layer):
            if not root:
                return
            cache.append(_layer)
            if root.left:
                dfs(root.left, _layer+1)
            if root.right:
                dfs(root.right, _layer+1)
        if not root:
            return 0
        _layer = 0
        cache = []
        dfs(root, 0)
        print (str(cache))
        return max(cache) + 1

# V0'''
# IDEA : DFS
class Solution:
    def maxDepth(self, root):
        self.layer_final = 0
        self.dfs(root, 0, self.layer_final)
        return self.layer_final
        
    def dfs(self, root, layer, layer_final):
        if root:
            if layer + 1 > layer_final:
                self.layer_final += 1
            self.dfs(root.left,  layer + 1, self.layer_final)
            self.dfs(root.right, layer + 1, self.layer_final)
            
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