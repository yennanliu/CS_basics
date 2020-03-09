"""
Given a binary tree

struct TreeLinkNode {
  TreeLinkNode *left;
  TreeLinkNode *right;
  TreeLinkNode *next;
}

Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

Initially, all next pointers are set to NULL.

Note:

You may only use constant extra space.
You may assume that it is a perfect binary tree (ie, all leaves are at the same level, and every parent has two children).
For example,

Given the following perfect binary tree,
         1
       /  \
      2    3
     / \  / \
    4  5  6  7


After calling your function, the tree should look like:

         1 -> NULL
       /  \
      2 -> 3 -> NULL
     / \  / \
    4->5->6->7 -> NULL

"""
# V0
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
