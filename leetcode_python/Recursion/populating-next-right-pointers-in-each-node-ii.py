# Follow up for problem “Populating Next Right Pointers in Each Node”.
#
# What if the given tree could be any binary tree? Would your previous solution still work?
#
# Note:
#
# You may only use constant extra space.
#
# For example,
#
# Given the following binary tree,
#
#          1
#        /  \
#       2    3
#      / \    \
#     4   5    7
#
# After calling your function, the tree should look like:
#
#          1 -> NULL
#        /  \
#       2 -> 3 -> NULL
#      / \    \
#     4-> 5 -> 7 -> NULL

# V0
# BFS 
# DFS (?)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79560379
# IDEA : BFS
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root: return
        queue = collections.deque()
        queue.append(root)
        while queue:
            _len = len(queue)
            for i in range(_len):
                node = queue.popleft()
                if i < _len - 1:
                    node.next = queue[0]
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        return root

### Test case
# dev

# V1'
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

# V1''
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
