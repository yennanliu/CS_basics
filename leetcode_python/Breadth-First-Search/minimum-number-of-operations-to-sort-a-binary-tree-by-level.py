"""

2471. Minimum Number of Operations to Sort a Binary Tree by Level
Medium

You are given the root of a binary tree with unique values.

In one operation, you can choose any two nodes at the same level and swap their values.

Return the minimum number of operations needed to make the values at each level sorted in a strictly increasing order.

The level of a node is the number of edges along the path between it and the root node.


Example 1:

Input: root = [1,4,3,7,6,8,5,null,null,null,null,9,null,10]
Output: 3
Explanation:
- Swap 4 and 3. The 2nd level becomes [3,4].
- Swap 7 and 5. The 3rd level becomes [5,6,8,7].
- Swap 8 and 7. The 3rd level becomes [5,6,7,8].
We used 3 operations so return 3.
It can be proven that 3 is the minimum number of operations needed.

Example 2:

Input: root = [1,3,2,7,6,5,4]
Output: 3
Explanation:
- Swap 3 and 2. The 2nd level becomes [2,3].
- Swap 7 and 4. The 3rd level becomes [4,6,5,7].
- Swap 6 and 5. The 3rd level becomes [4,5,6,7].
We used 3 operations so return 3.
It can be proven that 3 is the minimum number of operations needed.

Example 3:

Input: root = [1,2,3,4,5,6]
Output: 0
Explanation: Each level is already sorted in increasing order so return 0.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
1 <= Node.val <= 10^5
All the values of the tree are unique.


"""

# V0
# IDEA : PER LEVEL, THE MINIMUM SWAPS TO SORT = size - (number of cycles)
#
#   swaps are confined to a level, so each level is an independent "sort a
#   permutation with arbitrary swaps" problem. its answer is the classic
#   cycle count : a cycle of length L needs L - 1 swaps, so the total is
#       size - number of cycles
#
#   the permutation is built by mapping each value to the index it should
#   occupy (its rank within the level), which a sort gives directly.
#
#   levels are collected with an iterative BFS — the tree can hold 10^5 nodes.
#
# time = O(n log n), space = O(n)
from collections import deque


# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def minimumOperations(self, root):
        res = 0
        q = deque([root])
        while q:
            level = []
            for _ in range(len(q)):
                node = q.popleft()
                level.append(node.val)
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            size = len(level)
            # target[i] = index that level[i] must move to
            order = sorted(range(size), key=lambda i: level[i])
            target = [0] * size
            for rank, i in enumerate(order):
                target[i] = rank

            seen = [False] * size
            cycles = 0
            for i in range(size):
                if seen[i]:
                    continue
                cycles += 1
                j = i
                while not seen[j]:
                    seen[j] = True
                    j = target[j]
            res += size - cycles
        return res
