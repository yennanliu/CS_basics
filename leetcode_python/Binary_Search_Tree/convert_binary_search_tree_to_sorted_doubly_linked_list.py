# V0
# IDEA : DFS + INORDER
class Solution:
    """
    @param root: root of a tree
    @return: head node of a doubly linked list
    """
    ### build an iterator so that we can flattern the tree to get a increasing order
    def inorder(self, node):
        if not node: return
        left = node.left
        for n in self.inorder(left):
            yield n
        yield node
        right = node.right
        for n in self.inorder(right):
            yield n
    
    def treeToDoublyList(self, root):
        # Write your code here.
        if not root: return root
        first = None
        last = None
        prev = None
        # iterate the tree like a list
        for v in self.inorder(root):
            if first is None: first = v
            last = v
            if prev is not None:
                prev.right = v
                v.left = prev
            prev = v
        first.left = last
        last.right = first
        return first

# V1
# https://blog.csdn.net/zhangpeterx/article/details/89567460
# IDEA : BFS + INORDER 
class Solution:
    def treeToDoublyList(self, root):
        if not root: return
        dummy = Node(0, None, None)
        prev = dummy
        stack, node = [], root
        while stack or node:
            while node:
                stack.append(node)
                node = node.left
            node = stack.pop()
            node.left, prev.right, prev = prev, node, node
            node = node.right
        dummy.right.left, prev.right = prev, dummy.right
        return dummy.right

# V1'
# https://www.jiuzhang.com/solution/convert-binary-search-tree-to-sorted-doubly-linked-list/#tag-highlight-lang-python
# IDEA : DFS + INORDER
class Solution:
    """
    @param root: root of a tree
    @return: head node of a doubly linked list
    """
    # build an iterator so that we can flattern the tree to get a increasing order
    def inorder(self, node):
        if not node: return
        left = node.left
        for n in self.inorder(left):
            yield n
        yield node
        right = node.right
        for n in self.inorder(right):
            yield n
    
    def treeToDoublyList(self, root):
        # Write your code here.
        if not root: return root
        first = None
        last = None
        prev = None
        # iterate the tree like a list
        for v in self.inorder(root):
            if first is None: first = v
            last = v
            if prev is not None:
                prev.right = v
                v.left = prev
            prev = v
        first.left = last
        last.right = first
        return first
        
# V1''
# https://blog.csdn.net/zhangpeterx/article/details/89567460
class Solution:
    def treeToDoublyList(self, root: 'Node') -> 'Node':
        def helper(node):
            """
            Performs standard inorder traversal:
            left -> node -> right
            and links all nodes into DLL
            """
            nonlocal last, first
            if node:
                # left
                helper(node.left)
                # node 
                if last:
                    # link the previous node (last)
                    # with the current one (node)
                    last.right = node
                    node.left = last
                else:
                    # keep the smallest node
                    # to close DLL later on
                    first = node        
                last = node
                # right
                helper(node.right)
        
        if not root:
            return None
        
        # the smallest (first) and the largest (last) nodes
        first, last = None, None
        helper(root)
        # close DLL
        last.right = first
        first.left = last
        return first

# V2