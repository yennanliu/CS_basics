"""

314. Binary Tree Vertical Order Traversal
Medium

Given the root of a binary tree, return the vertical order traversal of its nodes' values. (i.e., from top to bottom, column by column).

If two nodes are in the same row and column, the order should be from left to right.

 

Example 1:


Input: root = [3,9,20,null,null,15,7]
Output: [[9],[3,15],[20],[7]]
Example 2:


Input: root = [3,9,8,4,0,1,7]
Output: [[4],[9],[3,0,1],[8],[7]]
Example 3:


Input: root = [3,9,8,4,0,1,7,null,null,null,2,5]
Output: [[4],[9,5],[3,0,1],[8,2],[7]]
 

Constraints:

The number of nodes in the tree is in the range [0, 100].
-100 <= Node.val <= 100

"""

# V0
# IDEA : BFS + defaultdict
from collections import defaultdict
class Solution(object):
    def verticalOrder(self, root):
        # edge
        if not root:
            return []
        idx = 0
        q = [[idx, root]]
        res = defaultdict(list)
        while q:
            for i in range(len(q)):
                _idx, tmp = q.pop(0)
                res[_idx].append(tmp.val)
                if tmp.left:
                    q.append([_idx-1, tmp.left])
                if tmp.right:
                    q.append([_idx+1, tmp.right])
        d_res = dict(res)
        d_res_ = sorted(d_res.items(), key = lambda x : x[0])
        print ("d_res_ = " + str(d_res_))
        return [x[1] for x in d_res_]

# V0'
# IDEA : DFS
from collections import defaultdict
class Solution:
    def verticalOrder(self, root):
        
        if root is None:
            return []

        columnTable = defaultdict(list)
        min_column = max_column = 0

        def DFS(node, row, column):
            if node is not None:
                nonlocal min_column, max_column
                columnTable[column].append((row, node.val))
                min_column = min(min_column, column)
                max_column = max(max_column, column)

                # preorder DFS
                DFS(node.left, row + 1, column - 1)
                DFS(node.right, row + 1, column + 1)

        DFS(root, 0, 0)

        # order by column and sort by row
        ret = []
        for col in range(min_column, max_column + 1):
            columnTable[col].sort(key=lambda x:x[0])
            colVals = [val for row, val in columnTable[col]]
            ret.append(colVals)

        return ret

# V0''
# IDEA : BFS + collections.defaultdict(list)
import collections
class Solution(object):
    def verticalOrder(self, root):
        if not root: return []
        cols = collections.defaultdict(list)
        q = [(root, 0)]
        while q:
            for node, col in q:
                cols[col].append(node.val)
            new_q = []
            for node, col in q:
                if node.left:
                    new_q.append((node.left, col-1))
                if node.right:
                    new_q.append((node.right, col+1))
            q = new_q          
        return [cols[c] for c in sorted(cols.keys())]

# V1
# IDEA : BFS
# https://leetcode.com/problems/binary-tree-vertical-order-traversal/solution/
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
from collections import defaultdict
class Solution:
    def verticalOrder(self, root: TreeNode) -> List[List[int]]:
        columnTable = defaultdict(list)
        queue = deque([(root, 0)])

        while queue:
            node, column = queue.popleft()

            if node is not None:
                columnTable[column].append(node.val)
                
                queue.append((node.left, column - 1))
                queue.append((node.right, column + 1))
                        
        return [columnTable[x] for x in sorted(columnTable.keys())]

# V1
# IDEA : BFS WITHOUT SORTING
# https://leetcode.com/problems/binary-tree-vertical-order-traversal/solution/
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
from collections import defaultdict
class Solution:
    def verticalOrder(self, root: TreeNode) -> List[List[int]]:
        if root is None:
            return []

        columnTable = defaultdict(list)
        min_column = max_column = 0
        queue = deque([(root, 0)])

        while queue:
            node, column = queue.popleft()

            if node is not None:
                columnTable[column].append(node.val)
                min_column = min(min_column, column)
                max_column = max(max_column, column)

                queue.append((node.left, column - 1))
                queue.append((node.right, column + 1))

        return [columnTable[x] for x in range(min_column, max_column + 1)]

# V1
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-vertical-order-traversal/solution/
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
from collections import defaultdict
class Solution:
    def verticalOrder(self, root: TreeNode) -> List[List[int]]:
        if root is None:
            return []

        columnTable = defaultdict(list)
        min_column = max_column = 0

        def DFS(node, row, column):
            if node is not None:
                nonlocal min_column, max_column
                columnTable[column].append((row, node.val))
                min_column = min(min_column, column)
                max_column = max(max_column, column)

                # preorder DFS
                DFS(node.left, row + 1, column - 1)
                DFS(node.right, row + 1, column + 1)

        DFS(root, 0, 0)

        # order by column and sort by row
        ret = []
        for col in range(min_column, max_column + 1):
            columnTable[col].sort(key=lambda x:x[0])
            colVals = [val for row, val in columnTable[col]]
            ret.append(colVals)

        return ret

# V1 
# https://blog.csdn.net/qq508618087/article/details/50760661
# https://blog.csdn.net/danspace1/article/details/86654851
# IDEA : BFS + collections.defaultdict(list) 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
import collections
class Solution(object):
    def verticalOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        # base case
        if not root: return []
        cols = collections.defaultdict(list)
        q = [(root, 0)]
        while q:
            for node, col in q:
                cols[col].append(node.val)
            new_q = []
            for node, col in q:
                if node.left:
                    new_q.append((node.left, col-1))
                if node.right:
                    new_q.append((node.right, col+1))
            q = new_q
            
        return [cols[c] for c in sorted(cols.keys())]

# V1'
# https://www.jiuzhang.com/solution/binary-tree-vertical-order-traversal/#tag-highlight-lang-python
# IDEA : BFS + collections.defaultdict(list)
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    # @param {TreeNode} root the root of binary tree
    # @return {int[][]} the vertical order traversal
    def verticalOrder(self, root):
        # Write your code here
        results = collections.defaultdict(list)
        import Queue
        queue = Queue.Queue()
        queue.put((root, 0))
        while not queue.empty():
            node, x = queue.get()
            if node:
                results[x].append(node.val)
                queue.put((node.left, x - 1))
                queue.put((node.right, x + 1))
                
        return [results[i] for i in sorted(results)]

# V2 
# Time:  O(n)
# Space: O(n)
import collections
# BFS + hash solution.
class Solution(object):
    def verticalOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        cols = collections.defaultdict(list)
        queue = [(root, 0)]
        for node, i in queue:
            if node:
                cols[i].append(node.val)
                queue += (node.left, i - 1), (node.right, i + 1)
        return [cols[i] for i in range(min(cols.keys()),
                                        max(cols.keys()) + 1)] if cols else []