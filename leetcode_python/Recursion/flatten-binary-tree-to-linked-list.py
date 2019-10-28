# V0  

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/70241424
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        res = []
        self.preOrder(root, res)
        for i in range(len(res) - 1):
            res[i].left = None
            res[i].right = res[i + 1]
    
    def preOrder(self, root, res):
        if not root: return
        res.append(root)
        self.preOrder(root.left, res)
        self.preOrder(root.right, res)

# V1'
# http://bookshadow.com/weblog/2016/09/02/leetcode-flatten-binary-tree-to-linked-list/
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        pointer = dummy = TreeNode(None)
        stack = [root]
        while stack:
            top = stack.pop()
            if not top: continue
            stack.append(top.right)
            stack.append(top.left)
            pointer.right = top
            pointer.left = None
            pointer = top

# V1''
# http://bookshadow.com/weblog/2016/09/02/leetcode-flatten-binary-tree-to-linked-list/
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        self.pointer = None
        def traverse(root):
            if not root: return
            traverse(root.right)
            traverse(root.left)
            root.right = self.pointer
            root.left = None
            self.pointer = root
        traverse(root)

# V1'''
# https://www.jiuzhang.com/solution/flatten-binary-tree-to-linked-list/#tag-highlight-lang-python
class Solution:
    last_node = None
    
    """
    @param root: a TreeNode, the root of the binary tree
    @return: nothing
    """
    def flatten(self, root):
        if root is None:
            return
        
        if self.last_node is not None:
            self.last_node.left = None
            self.last_node.right = root
            
        self.last_node = root
        right = root.right
        self.flatten(root.left)
        self.flatten(right)

# V1''''
# https://www.jiuzhang.com/solution/flatten-binary-tree-to-linked-list/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode, the root of the binary tree
    @return: nothing
    """
    def flatten(self, root):
        self.helper(root)
        
    # restructure and return last node in preorder
    def helper(self, root):
        if root is None:
            return None
            
        left_last = self.helper(root.left)
        right_last = self.helper(root.right)
        
        # connect 
        if left_last is not None:
            left_last.right = root.right
            root.right = root.left
            root.left = None
            
        if right_last is not None:
            return right_last
            
        if left_last is not None:
            return left_last            
        return root

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
    # @return nothing, do it in place
    def flatten(self, root):
        self.flattenRecu(root, None)

    def flattenRecu(self, root, list_head):
        if root:
            list_head = self.flattenRecu(root.right, list_head)
            list_head = self.flattenRecu(root.left, list_head)
            root.right = list_head
            root.left = None
            return root
        else:
            return list_head

class Solution2(object):
    list_head = None
    # @param root, a tree node
    # @return nothing, do it in place
    def flatten(self, root):
        if root:
            self.flatten(root.right)
            self.flatten(root.left)
            root.right = self.list_head
            root.left = None
            self.list_head = root
