# V0 : DEV 

# V1
# http://bookshadow.com/weblog/2017/09/03/leetcode-second-minimum-node-in-a-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def findSecondMinimumValue(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.ans = 0x80000000
        minVal = root.val
        def traverse(root):
            if not root: return
            if self.ans > root.val > minVal:
                self.ans = root.val
            traverse(root.left)
            traverse(root.right)
        traverse(root)
        return self.ans if self.ans != 0x80000000 else -1
# V2 
# https://www.jianshu.com/p/5b1de2697e1b
import sys
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def findSecondMinimumValue(self, root):
        self.min = sys.maxint
        self.second_min = sys.maxint
        def traverse(node):
            if node:
                if node.val < self.min:
                    self.second_min = self.min
                    self.min = node.val
                elif node.val < self.second_min and node.val != self.min:
                    self.second_min = node.val

                traverse(node.left)
                traverse(node.right)

        traverse(root)
        if self.second_min != sys.maxint:
            return self.second_min
        return -1

# root = TreeNode(2)
# root.left = TreeNode(2)
# root.right = TreeNode(5)
# root.right.left = TreeNode(5)
# root.right.right = TreeNode(7)
# assert Solution().findSecondMinimumValue(root) == 5

# V3 
# Time:  O(n)
# Space: O(h)
import heapq
class Solution(object):
    def findSecondMinimumValue(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def findSecondMinimumValueHelper(root, max_heap, lookup):
            if not root:
                return
            if root.val not in lookup:
                heapq.heappush(max_heap, -root.val)
                lookup.add(root.val)
                if len(max_heap) > 2:
                    lookup.remove(-heapq.heappop(max_heap))
            findSecondMinimumValueHelper(root.left, max_heap, lookup)
            findSecondMinimumValueHelper(root.right, max_heap, lookup)

        max_heap, lookup = [], set()
        findSecondMinimumValueHelper(root, max_heap, lookup)
        if len(max_heap) < 2:
            return -1
        return -max_heap[0]
