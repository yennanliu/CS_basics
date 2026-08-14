"""

1743. Restore the Array From Adjacent Pairs
Medium

There is an integer array nums that consists of n unique elements, but you have forgotten it. However, you do remember every pair of adjacent elements in nums.

You are given a 2D integer array adjacentPairs of size n - 1 where each adjacentPairs[i] = [ui, vi] indicates that the elements ui and vi are adjacent in nums.

It is guaranteed that every adjacent pair of elements nums[i] and nums[i+1] will exist in adjacentPairs, either as [nums[i], nums[i+1]] or [nums[i+1], nums[i]]. The pairs can appear in any order.

Return the original array nums. If there are multiple solutions, return any of them.


Example 1:

Input: adjacentPairs = [[2,1],[3,4],[3,2]]
Output: [1,2,3,4]
Explanation: This array has all its adjacent pairs in adjacentPairs.
Notice that adjacentPairs[i] may not be in left-to-right order.

Example 2:

Input: adjacentPairs = [[4,-2],[1,4],[-3,1]]
Output: [-2,4,1,-3]
Explanation: There can be negative numbers.
Another solution is [-3,1,4,-2], which would also be accepted.

Example 3:

Input: adjacentPairs = [[100000,-100000]]
Output: [100000,-100000]


Constraints:

nums.length == n
adjacentPairs.length == n - 1
adjacentPairs[i].length == 2
2 <= n <= 10^5
-10^5 <= nums[i], ui, vi <= 10^5
There exists some nums that has adjacentPairs as its pairs.

"""

# V0
# IDEA : GRAPH IS A PATH - START AT A DEGREE-1 ENDPOINT AND WALK IT
#
#   treat the values as nodes and each pair as an undirected edge. since
#   nums is an array of UNIQUE values, this graph is exactly a simple path:
#     - the two ends of nums have degree 1
#     - every interior value has degree 2
#
#   so : find any node with a single neighbour (an end of the array), then
#   walk forward. at each step the current node has (at most) two
#   neighbours - one is where we came from, the other is the next value.
#
#   NOTE : walk ITERATIVELY, not recursively - n can be 10^5 and the path
#          is one long chain, which would blow Python's recursion limit.
#
# time = O(n), space = O(n)
from collections import defaultdict
class Solution(object):
    def restoreArray(self, adjacentPairs):
        g = defaultdict(list)
        for a, b in adjacentPairs:
            g[a].append(b)
            g[b].append(a)

        n = len(adjacentPairs) + 1

        start = None
        for k in g:
            if len(g[k]) == 1:
                start = k
                break

        res = [start]
        prev = None
        cur = start
        while len(res) < n:
            nxt = g[cur][0]
            if nxt == prev:
                nxt = g[cur][1]
            res.append(nxt)
            prev, cur = cur, nxt

        return res
