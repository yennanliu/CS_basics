"""

Given the root of a binary tree, invert the tree, and return its root.

Example 1:


Input: root = [4,2,7,1,3,6,9]
Output: [4,7,2,9,6,3,1]

Example 2:


Input: root = [2,1,3]
Output: [2,3,1]

Example 3:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 100].
-100 <= Node.val <= 100


Invert a binary tree.

     4
   /   \
  2     7
 / \   / \
1   3 6   9

to
     4
   /   \
  7     2
 / \   / \
9   6 3   1


"""

# V0
# IDEA : DFS
# -> below code shows a good example that tree is a type of "linked list"
# -> we don't really modify tree's "value", but we modify the pointer
# -> e.g. make root.left point to root.right, make root.right point to root.left
class Solution(object):
    def invertTree(self, root):
        def dfs(root):
            if not root:
                return root
            ### NOTE THIS
            if root:
                # NOTE : have to do root.left, root.right ON THE SAME TIME
                root.left, root.right = dfs(root.right), dfs(root.left)
        dfs(root)
        return root

# V0'
# IDEA BFS
class Solution(object):
    def invertTree(self, root):
        if root == None:
            return root
        queue = [root]
        while queue:
            # queue = queue[::-1] <-- this one is NOT working
            for i in range(len(queue)):         
                tmp = queue.pop()
                ### NOTE here !!!!!!
                # -> we do invert op via below
                tmp.left, tmp.right = tmp.right, tmp.left
                if tmp.left:
                    queue.append(tmp.left)
                if tmp.right:
                    queue.append(tmp.right)
        return root

# V0
# IDEA : DFS
class Solution(object):
    def invertTree(self, root):
        if root is None:
            return root
        if root is not None:
            # NOTE : have to do root.left, root.right ON THE SAME TIME
            root.left, root.right = self.invertTree(root.right), \
                                    self.invertTree(root.left)

        return root

# V1 
# https://blog.csdn.net/coder_orz/article/details/51383933
# DFS (Recursion)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def invertTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if root == None:
            return root
        tmp = root.left
        root.left = self.invertTree(root.right)
        root.right = self.invertTree(tmp)
        return root

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51383933
# Stack 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def invertTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if root == None:
            return root

        stack = [root]
        while len(stack) != 0:
            node = stack.pop()
            node.left, node.right = node.right, node.left
            if node.left:
                stack.append(node.left)
            if node.right:
                stack.append(node.right)

        return root

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51383933
# BFS 
class Solution(object):
    def invertTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if root == None:
            return root
        q = [root]
        while len(q) != 0:
            q[0].left, q[0].right = q[0].right, q[0].left
            if q[0].left:
                q.append(q[0].left)
            if q[0].right:
                q.append(q[0].right)
            del q[0]

        return root

# V2 
# Time:  O(n)
# Space: O(h)
# BFS solution.
import collections
class Queue(object):
    def __init__(self):
        self.data = collections.deque()

    def push(self, x):
        self.data.append(x)

    def peek(self):
        return self.data[0]

    def pop(self):
        return self.data.popleft()

    def size(self):
        return len(self.data)

    def empty(self):
        return len(self.data) == 0

# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    # @param {TreeNode} root
    # @return {TreeNode}
    def invertTree(self, root):
        if root is not None:
            nodes = Queue()
            nodes.push(root)
            while not nodes.empty():
                node = nodes.pop()
                node.left, node.right = node.right, node.left
                if node.left is not None:
                    nodes.push(node.left)
                if node.right is not None:
                    nodes.push(node.right)

        return root

# Time:  O(n)
# Space: O(h)
# Stack solution.
class Solution2(object):
    # @param {TreeNode} root
    # @return {TreeNode}
    def invertTree(self, root):
        if root is not None:
            nodes = []
            nodes.append(root)
            while nodes:
                node = nodes.pop()
                node.left, node.right = node.right, node.left
                if node.left is not None:
                    nodes.append(node.left)
                if node.right is not None:
                    nodes.append(node.right)

        return root

# Time:  O(n)
# Space: O(h)
# DFS, Recursive solution.
class Solution3(object):
    # @param {TreeNode} root
    # @return {TreeNode}
    def invertTree(self, root):
        if root is not None:
            root.left, root.right = self.invertTree(root.right), \
                                    self.invertTree(root.left)

        return root

