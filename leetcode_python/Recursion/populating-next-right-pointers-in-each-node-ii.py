"""

117. Populating Next Right Pointers in Each Node II
Medium

Given a binary tree

struct Node {
  int val;
  Node *left;
  Node *right;
  Node *next;
}
Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

Initially, all next pointers are set to NULL.

 

Example 1:


Input: root = [1,2,3,4,5,null,7]
Output: [1,#,2,3,#,4,5,7,#]
Explanation: Given the above binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B. The serialized output is in level order as connected by the next pointers, with '#' signifying the end of each level.
Example 2:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 6000].
-100 <= Node.val <= 100
 

Follow-up:

You may only use constant extra space.
The recursive approach is fine. You may assume implicit stack space does not count as extra space for this problem.

"""

# V0
# IDEA : BFS
class Solution:
    def connect(self, root):
        if not root: return
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

# V0'
# IDEA : BFS 
class Solution:
    def connect(self, root: Optional['Node']) -> Optional['Node']:
        
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
# IDEA : Level Order Traversal
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solution/
class Solution:
    def connect(self, root: Optional['Node']) -> Optional['Node']:
        
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

# V1'
# IDEA : Using previously established next pointers
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/solution/
class Solution:
    
    def processChild(self, childNode, prev, leftmost):
        if childNode:
            
            # If the "prev" pointer is alread set i.e. if we
            # already found atleast one node on the next level,
            # setup its next pointer
            if prev:
                prev.next = childNode
            else:    
                # Else it means this child node is the first node
                # we have encountered on the next level, so, we
                # set the leftmost pointer
                leftmost = childNode
            prev = childNode 
        return prev, leftmost
    
    def connect(self, root: Optional['Node']) -> Optional['Node']:
        
        if not root:
            return root
        
        # The root node is the only node on the first level
        # and hence its the leftmost node for that level
        leftmost = root
        
        # We have no idea about the structure of the tree,
        # so, we keep going until we do find the last level.
        # The nodes on the last level won't have any children
        while leftmost:
            
            # "prev" tracks the latest node on the "next" level
            # while "curr" tracks the latest node on the current
            # level.
            prev, curr = None, leftmost
            
            # We reset this so that we can re-assign it to the leftmost
            # node of the next level. Also, if there isn't one, this
            # would help break us out of the outermost loop.
            leftmost = None
            
            # Iterate on the nodes in the current level using
            # the next pointers already established.
            while curr:
                
                # Process both the children and update the prev
                # and leftmost pointers as necessary.
                prev, leftmost = self.processChild(curr.left, prev, leftmost)
                prev, leftmost = self.processChild(curr.right, prev, leftmost)
                
                # Move onto the next node.
                curr = curr.next
                
        return root 

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79560379
# IDEA : BFS
class Solution:
    def connect(self, root):
        if not root: return
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

# V1'''
# IDEA BFS
# https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/discuss/389389/Simply-Simple-Python-Solutions-Level-order-traversal-and-O(1)-space-both-approach
class Solution(object):
    def connect(self, root):
        if not root: return root
        q = []
        q.append(root)
        tail = root
        while len(q) > 0:
            node = q.pop(0)
            if node.left:
                q.append(node.left)
            if node.right:
                q.append(node.right)   
            if node == tail:
                node.next = None
                tail = q[-1] if len(q) > 0 else None
            else:
                node.next = q[0]      
        return root

# V1'''''
# https://www.bbsmax.com/A/pRdBoNL2zn/
class Solution(object):
     def connect(self, root):
         """
         :type root: TreeLinkNode
         :rtype: nothing
         """
         if root:
             tmp,tmp1,tmp2 = root,None,None
             while tmp:
                 if tmp.left:
                     if tmp1:
                         tmp1.next = tmp.left
                     tmp1 = tmp.left
                     if not tmp2:
                         tmp2 = tmp1
                 if tmp.right:
                     if tmp1:
                         tmp1.next = tmp.right
                     tmp1 = tmp.right
                     if not tmp2:
                         tmp2 = tmp1
                 tmp = tmp.next
             self.connect(tmp2)

# V2 