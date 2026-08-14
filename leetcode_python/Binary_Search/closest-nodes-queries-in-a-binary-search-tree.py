"""

2476. Closest Nodes Queries in a Binary Search Tree
Medium

You are given the root of a binary search tree and an array queries of size n consisting of positive integers.

Find a 2D array answer of size n where answer[i] = [mini, maxi]:

mini is the largest value in the tree that is smaller than or equal to queries[i]. If a such value does not exist, add -1 instead.
maxi is the smallest value in the tree that is greater than or equal to queries[i]. If a such value does not exist, add -1 instead.

Return the array answer.


Example 1:

Input: root = [6,2,13,1,4,9,15,null,null,null,null,null,null,14], queries = [2,5,16]
Output: [[2,2],[4,6],[15,-1]]
Explanation: We answer the queries in the following way:
- The largest number that is smaller or equal than 2 in the tree is 2, and the smallest number that is greater or equal than 2 is still 2. So the answer for the first query is [2,2].
- The largest number that is smaller or equal than 5 in the tree is 4, and the smallest number that is greater or equal than 5 is 6. So the answer for the second query is [4,6].
- The largest number that is smaller or equal than 16 in the tree is 15, and the smallest number that is greater or equal than 16 does not exist. So the answer for the third query is [15,-1].

Example 2:

Input: root = [4,null,9], queries = [3]
Output: [[-1,4]]
Explanation: The largest number that is smaller or equal to 3 in the tree does not exist, and the smallest number that is greater or equal to 3 is 4. So the answer for the query is [-1,4].


Constraints:

The number of nodes in the tree is in the range [2, 10^5].
1 <= Node.val <= 10^6
n == queries.length
1 <= n <= 10^5
1 <= queries[i] <= 10^6

"""

# V0
# IDEA : IN-ORDER TRAVERSAL -> SORTED ARRAY + BINARY SEARCH
#
#   in-order of a BST is sorted, so flatten once and answer every query
#   with two bisects :
#     hi = bisect_right(nums, x) - 1  -> largest value <= x
#     lo = bisect_left(nums, x)       -> smallest value >= x
#   NOTE : the tree can hold 10^5 nodes and may be a degenerate chain,
#          so the traversal is written ITERATIVELY (recursion would blow
#          CPython's stack limit).
#
# time = O(n + q * log n), space = O(n)
import bisect
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def closestNodes(self, root, queries):
        nums = []
        stack = []
        cur = root
        while stack or cur:
            while cur:
                stack.append(cur)
                cur = cur.left
            cur = stack.pop()
            nums.append(cur.val)
            cur = cur.right

        n = len(nums)
        res = []
        for x in queries:
            i = bisect.bisect_right(nums, x) - 1
            j = bisect.bisect_left(nums, x)
            mi = nums[i] if i >= 0 else -1
            mx = nums[j] if j < n else -1
            res.append([mi, mx])
        return res
