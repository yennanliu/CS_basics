"""

116. Populating Next Right Pointers in Each Node
Medium

You are given a perfect binary tree where all leaves are on the same level, and every parent has two children. The binary tree has the following definition:

struct Node {
  int val;
  Node *left;
  Node *right;
  Node *next;
}
Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

Initially, all next pointers are set to NULL.

 

Example 1:


Input: root = [1,2,3,4,5,6,7]
Output: [1,#,2,3,#,4,5,6,7,#]
Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B. The serialized output is in level order as connected by the next pointers, with '#' signifying the end of each level.
Example 2:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 212 - 1].
-1000 <= Node.val <= 1000
 

Follow-up:

You may only use constant extra space.
The recursive approach is fine. You may assume implicit stack space does not count as extra space for this problem.

"""

# V0
# IDEA : BFS
# REF : LC # 117 : populating-next-right-pointers-in-each-node-ii/
class Solution:
    def connect(self, root):
        if not root: 
            return None
        queue = collections.deque()
        queue.append(root)
        while queue:
            _len = len(queue)
            for i in range(_len):
                node = queue.popleft()
                ### IF NOT LAST NODE, POINT NEXT TO FIRST NODE IN THE QUEUE
                if i < _len - 1:
                    node.next = queue[0]
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        return root

# V0
# IDEA : BFS
### NOTE : in this problem, we need to populate the result as LINKED LIST form
from collections import deque
class Solution(object):
    def connect(self, root):
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp, size=[], len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            ### NOTE : this op is OUTSIDE OF FOR LOOP
            ###    -> DO ONCE WHEN EVERY BFS LAYER
            # >>> a
            # [1, 2, 3]
            # >>> a[:-1]
            # [1, 2]
            # -> enumerate(temp[:-1]) will looping from last n-1 elements
            for index, node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            # point last element to None (linked list)
            temp[-1].next = None ###  NOTE : temp[-1].next, is pointing tmp's next to None
        return root

# V0''
# IDEA : BFS
### NOTE : in this problem, we need to populate the result as LINKED LIST form
from collections import deque
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp,size=[],len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left and node.right:
                    queue.append(node.left)
                    queue.append(node.right)
            for index,node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            temp[-1].next = None
        return root

# V0'
### NOTE : in this problem, we need to populate the result as LINKED LIST form
# IDEA : DFS 
# DEMO
#      1 -> NULL
#    /  \
#   2 -> 3 -> NULL
#  / \  / \
# 4->5->6->7 -> NULL
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root: return
        if root.right:
            # when at 1 node, connect 2->3 
            root.left.next = root.right
            # when at 2 node, the root.next = 3 
            if root.next:
                # so will connect 5 -> 6
                root.right.next = root.next.left
        self.connect(root.left)
        self.connect(root.right) 

# V1
# IDEA :  Level Order Traversal
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/solution/
import collections 

class Solution:
    def connect(self, root: 'Node') -> 'Node':
        
        if not root:
            return root
        
        # Initialize a queue data structure which contains
        # just the root of the tree
        Q = collections.deque([root])
        
        # Outer while loop which iterates over 
        # each level
        while Q:
            
            # Note the size of the queue
            size = len(Q)
            
            # Iterate over all the nodes on the current level
            for i in range(size):
                
                # Pop a node from the front of the queue
                node = Q.popleft()
                
                # This check is important. We don't want to
                # establish any wrong connections. The queue will
                # contain nodes from 2 levels at most at any
                # point in time. This check ensures we only 
                # don't establish next pointers beyond the end
                # of a level
                if i < size - 1:
                    node.next = Q[0]
                
                # Add the children, if any, to the back of
                # the queue
                if node.left:
                    Q.append(node.left)
                if node.right:
                    Q.append(node.right)
        
        # Since the tree has now been modified, return the root node
        return root

# V1
# IDEA : Using previously established next pointers
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/solution/
class Solution:
    def connect(self, root: 'Node') -> 'Node':
        
        if not root:
            return root
        
        # Start with the root node. There are no next pointers
        # that need to be set up on the first level
        leftmost = root
        
        # Once we reach the final level, we are done
        while leftmost.left:
            
            # Iterate the "linked list" starting from the head
            # node and using the next pointers, establish the 
            # corresponding links for the next level
            head = leftmost
            while head:
                
                # CONNECTION 1
                head.left.next = head.right
                
                # CONNECTION 2
                if head.next:
                    head.right.next = head.next.left
                
                # Progress along the list (nodes on the current level)
                head = head.next
            
            # Move onto the next level
            leftmost = leftmost.left
        
        return root 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79559645
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root: return
        if root.right:
            root.left.next = root.right
            if root.next:
                root.right.next = root.next.left
        self.connect(root.left)
        self.connect(root.right)

# V1'
# IDEA : BFS
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/discuss/291622/Python-recursive-solution
from collections import deque
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        root.next = None
        queue = deque([root])
        while queue:
            temp,size=[],len(queue)
            for i in range(size):
                node = queue.popleft()
                if node:
                    temp.append(node)
                if node.left and node.right:
                    queue.append(node.left)
                    queue.append(node.right)
            for index,node in enumerate(temp[:-1]):
                node.next = temp[index+1]
            temp[-1].next = None
        return root

# V1''
# IDEA : LINKED LIST
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node/discuss/291622/Python-recursive-solution
class Solution(object):
    def connect(self, root):
        """
        :type root: Node
        :rtype: Node
        """
        if not root:
            return None
        cur = root
        nex = cur.left
        while cur.left:
            cur.left.next = cur.right
            if cur.next:
                cur.right.next = cur.next.left
                cur = cur.next
            else:
                cur = nex
                nex = cur.left
        return root

# V1'''
# https://www.cnblogs.com/loadofleaf/p/5523911.html
class Solution(object):
    def connect(self, root):
        if root and root.left:
            root.left.next = root.right
            if root.next:
                root.right.next = root.next.left
            else:
                root.right.next = None
            self.connect(root.left)
            self.connect(root.right)

# V2
# Time:  O(n)
# Space: O(1)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    # @param root, a tree node
    # @return nothing
    def connect(self, root):
        head = root
        while head:
            cur = head
            while cur and cur.left:
                cur.left.next = cur.right
                if cur.__next__:
                    cur.right.next = cur.next.left
                cur = cur.__next__
            head = head.left