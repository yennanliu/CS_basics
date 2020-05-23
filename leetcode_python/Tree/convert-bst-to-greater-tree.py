# V0 
# IDEA : DFS 
class Solution(object):

    def convertBST(self, root):
        self.sum = 0
        self.dfs(root)
        return root

    def dfs(self, cur):
        if not cur: 
            return
        self.dfs(cur.right)
        self.sum += cur.val
        cur.val = self.sum
        self.dfs(cur.left)

# V0'
# IDEA : BFS
class Solution(object):
    def convertBST(self, root):
        total = 0
        
        node = root
        stack = []
        while stack or node is not None:
            while node is not None:
                stack.append(node)
                node = node.right

            node = stack.pop()
            total += node.val
            node.val = total

            node = node.left
        return root

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79132336
# IDEA : RIGHT -> ROOT -> LEFT 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def convertBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        self.sum = 0
        def afterOrder(cur):
            if not cur: return
            afterOrder(cur.right)
            self.sum += cur.val
            cur.val = self.sum
            afterOrder(cur.left)
        afterOrder(root)
        return root

### Test case : dev 

# V1'
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# IDEA : RECURSION 
# Time complexity : O(N)
# Space complexity : O(N)
class Solution(object):
    def __init__(self):
        self.total = 0

    def convertBST(self, root):
        if root is not None:
            self.convertBST(root.right)
            self.total += root.val
            root.val = self.total
            self.convertBST(root.left)
        return root

# V1''
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# Iteration with a Stack
# Time complexity : O(N)
# Space complexity : O(N)
class Solution(object):
    def convertBST(self, root):
        total = 0
        
        node = root
        stack = []
        while stack or node is not None:
            # push all nodes up to (and including) this subtree's maximum on
            # the stack.
            while node is not None:
                stack.append(node)
                node = node.right

            node = stack.pop()
            total += node.val
            node.val = total

            # all nodes with values between the current and its parent lie in
            # the left subtree.
            node = node.left
        return root

# V1'''
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# IDEA :  Reverse Morris In-order Traversal 
# Time complexity : O(N)
# Space complexity : O(1)
class Solution(object):
    def convertBST(self, root):
        # Get the node with the smallest value greater than this one.
        def get_successor(node):
            succ = node.right
            while succ.left is not None and succ.left is not node:
                succ = succ.left
            return succ
                
        total = 0
        node = root
        while node is not None:
            # If there is no right subtree, then we can visit this node and
            # continue traversing left.
            if node.right is None:
                total += node.val
                node.val = total
                node = node.left
            # If there is a right subtree, then there is a node that has a
            # greater value than the current one. therefore, we must traverse
            # that node first.
            else:
                succ = get_successor(node)
                # If there is no left subtree (or right subtree, because we are
                # in this branch of control flow), make a temporary connection
                # back to the current node.
                if succ.left is None:
                    succ.left = node
                    node = node.right
                # If there is a left subtree, it is a link that we created on
                # a previous pass, so we should unlink it and visit this node.
                else:
                    succ.left = None
                    total += node.val
                    node.val = total
                    node = node.left
        
        return root 

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def convertBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        def convertBSTHelper(root, cur_sum):
            if not root:
                return cur_sum

            if root.right:
                cur_sum = convertBSTHelper(root.right, cur_sum)
            cur_sum += root.val
            root.val = cur_sum
            if root.left:
                cur_sum = convertBSTHelper(root.left, cur_sum)
            return cur_sum

        convertBSTHelper(root, 0)
        return root
