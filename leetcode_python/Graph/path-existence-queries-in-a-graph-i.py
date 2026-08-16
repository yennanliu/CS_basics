"""

3532. Path Existence Queries in a Graph I
Medium

You are given an integer n representing the number of nodes in a graph, labeled
from 0 to n - 1.

You are also given an integer array nums of length n sorted in non-decreasing
order, and an integer maxDiff.

An undirected edge exists between nodes i and j if the absolute difference
between nums[i] and nums[j] is at most maxDiff (i.e., |nums[i] - nums[j]| <=
maxDiff).

You are also given a 2D integer array queries. For each queries[i] = [u_i, v_i],
determine whether there exists a path between nodes u_i and v_i.

Return a boolean array answer, where answer[i] is true if there exists a path
between u_i and v_i in the ith query and false otherwise.


Example 1:

Input: n = 2, nums = [1,3], maxDiff = 1, queries = [[0,0],[0,1]]
Output: [true,false]
Explanation:
Query [0,0]: Node 0 has a trivial path to itself.
Query [0,1]: There is no edge between Node 0 and Node 1 because
|nums[0] - nums[1]| = |1 - 3| = 2, which is greater than maxDiff.
Thus, after processing all queries, the final answer is [true, false].

Example 2:

Input: n = 4, nums = [2,5,6,8], maxDiff = 2, queries = [[0,1],[0,2],[1,3],[2,3]]
Output: [false,false,true,true]
Explanation:
The resulting graph is:
Edge between node 1 and node 2 because |nums[1] - nums[2]| = |5 - 6| = 1 <= 2.
Edge between node 2 and node 3 because |nums[2] - nums[3]| = |6 - 8| = 2 <= 2.
Query [0,1]: There is no path between Node 0 and Node 1.
Query [0,2]: There is no path between Node 0 and Node 2.
Query [1,3]: There is a path between Node 1 and Node 3.
Query [2,3]: There is a path between Node 2 and Node 3.
Thus, after processing all queries, the final answer is [false, false, true, true].


Constraints:

1 <= n == nums.length <= 10^5
0 <= nums[i] <= 10^5
nums is sorted in non-decreasing order.
0 <= maxDiff <= 10^5
1 <= queries.length <= 10^5
queries[i] == [u_i, v_i]
0 <= u_i, v_i < n

"""

# V0
# IDEA : SORTED VALUES MAKE COMPONENTS CONTIGUOUS RUNS
#
#   nums is already non-decreasing, so if nums[i+1] - nums[i] <= maxDiff then i
#   and i+1 are joined, and if that gap is too large then *no* pair (a, b) with
#   a <= i < b can be joined either — every such pair differs by at least that
#   gap.  the gap therefore acts as a hard wall.
#
#   so the components are exactly the maximal runs between walls, and a single
#   left-to-right pass labels every node.  each query is then one comparison.
#
# time = O(n + q), space = O(n)
class Solution(object):
    def pathExistenceQueries(self, n, nums, maxDiff, queries):
        comp = [0] * n
        cur = 0
        for i in range(1, n):
            if nums[i] - nums[i - 1] > maxDiff:
                cur += 1
            comp[i] = cur
        return [comp[u] == comp[v] for u, v in queries]
