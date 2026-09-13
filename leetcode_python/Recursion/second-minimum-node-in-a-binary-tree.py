"""

671. Second Minimum Node In a Binary Tree
Easy

Given a non-empty special binary tree consisting of nodes with the non-negative value, where each node in this tree has exactly two or zero sub-node. If the node has two sub-nodes, then this node's value is the smaller value among its two sub-nodes. More formally, the property root.val = min(root.left.val, root.right.val) always holds.

Given such a binary tree, you need to output the second minimum value in the set made of all the nodes' value in the whole tree.

If no such second minimum value exists, output -1 instead.

Example 1:

https://assets.leetcode.com/uploads/2020/10/15/smbt1.jpg

Input: root = [2,2,5,null,null,5,7]
Output: 5
Explanation: The smallest value is 2, the second smallest value is 5.

Example 2:

https://assets.leetcode.com/uploads/2020/10/15/smbt2.jpg

Input: root = [2,2,2]
Output: -1
Explanation: The smallest value is 2, but there isn't any second smallest value.

Constraints:

The number of nodes in the tree is in the range [1, 25].
1 <= Node.val <= 2^31 - 1
root.val == min(root.left.val, root.right.val) for each internal node of the tree.

"""

# V0
# IDEA : DFS
# time = O(N log N) (sorting all N values)
# space = O(N)
class Solution:
    def findSecondMinimumValue(self, root):
        result=[]
        self.dfs(root, result)
        result_=sorted(set(result))
        return result_[1] if len(result_) > 1 else -1 

    def dfs(self,root, result):
        if not root:
            return 
        self.dfs(root.left, result)
        result.append(root.val)
        self.dfs(root.right, result)

# V0-1
# time = O(N log N) (sorting all N values)
# space = O(N)
class Solution:
    def findSecondMinimumValue(self, root: TreeNode) -> int:
        values = []
        def getValues(root):
            if not root:
                return
            values.append(root.val)
            if root.left:
                getValues(root.left)
            if root.right:
                getValues(root.right)
        getValues(root)
        values = sorted(set(values))
        if len(values) > 1:
            return values[1]
        return -1

# V1
# https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/discuss/455080/Python3-simple-solution-using-set()-function
# IDEA : DFS
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# time = O(N log N) (sorting all N values)
# space = O(N)
class Solution:
    def findSecondMinimumValue(self, root: TreeNode) -> int:
        values = []
        def getValues(root):
            if not root:
                return
            values.append(root.val)
            if root.left:
                getValues(root.left)
            if root.right:
                getValues(root.right)
        getValues(root)
        values = sorted(set(values))
        if len(values) > 1:
            return values[1]
        return -1

### Test case : dev
#s=Solution()
#s.findSecondMinimumValue([2,2,5,None,None,5,7])

# V1'
# http://bookshadow.com/weblog/2017/09/03/leetcode-second-minimum-node-in-a-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(N)
# space = O(H)
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

# V1''
# https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/solution/
# IDEA : BRUTE FORCE
# time = O(N)
# space = O(N)
class Solution(object):
    def findSecondMinimumValue(self, root):
        def dfs(node):
            if node:
                uniques.add(node.val)
                dfs(node.left)
                dfs(node.right)

        uniques = set()
        dfs(root)

        min1, ans = root.val, float('inf')
        for v in uniques:
            if min1 < v < ans:
                ans = v

        return ans if ans < float('inf') else -1

# V1'''
# IDEA : AD-HOC
# https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/solution/
# time = O(N)
# space = O(H)
def findSecondMinimumValue(self, root):
    self.ans = float('inf')
    min1 = root.val

    def dfs(node):
        if node:
            if min1 < node.val < self.ans:
                self.ans = node.val
            elif node.val == min1:
                dfs(node.left)
                dfs(node.right)

    dfs(root)
    return self.ans if self.ans < float('inf') else -1

# V1''''
# https://www.jianshu.com/p/5b1de2697e1b
import sys
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# time = O(N)
# space = O(H)
class Solution(object):
    def findSecondMinimumValue(self, root):
        self.min = sys.maxsize
        self.second_min = sys.maxsize
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
        if self.second_min != sys.maxsize:
            return self.second_min
        return -1

# root = TreeNode(2)
# root.left = TreeNode(2)
# root.right = TreeNode(5)
# root.right.left = TreeNode(5)
# root.right.right = TreeNode(7)
# assert Solution().findSecondMinimumValue(root) == 5

# V2
# time = O(N log 2) = O(N) (heap bounded to size 2)
# space = O(H)
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
