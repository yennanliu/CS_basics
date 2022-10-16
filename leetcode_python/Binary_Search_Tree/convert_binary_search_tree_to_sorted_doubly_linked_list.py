"""

426. Convert Binary Search Tree to Sorted Doubly Linked List
Medium

Convert a Binary Search Tree to a sorted Circular Doubly-Linked List in place.

You can think of the left and right pointers as synonymous to the predecessor and successor pointers in a doubly-linked list. For a circular doubly linked list, the predecessor of the first element is the last element, and the successor of the last element is the first element.

We want to do the transformation in place. After the transformation, the left pointer of the tree node should point to its predecessor, and the right pointer should point to its successor. You should return the pointer to the smallest element of the linked list.

 

Example 1:



Input: root = [4,2,5,1,3]


Output: [1,2,3,4,5]

Explanation: The figure below shows the transformed BST. The solid line indicates the successor relationship, while the dashed line means the predecessor relationship.

Example 2:

Input: root = [2,1,3]
Output: [1,2,3]
 

Constraints:

The number of nodes in the tree is in the range [0, 2000].
-1000 <= Node.val <= 1000
All the values of the tree are unique.

"""
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