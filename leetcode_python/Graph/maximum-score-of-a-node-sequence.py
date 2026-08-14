"""

2242. Maximum Score of a Node Sequence
Hard

There is an undirected graph with n nodes, numbered from 0 to n - 1.

You are given a 0-indexed integer array scores of length n where scores[i] denotes the score of node i. You are also given a 2D integer array edges where edges[i] = [ai, bi] denotes that there exists an undirected edge connecting nodes ai and bi.

A node sequence is valid if it meets the following conditions:

There is an edge connecting every pair of adjacent nodes in the sequence.
No node appears more than once in the sequence.

The score of a node sequence is defined as the sum of the scores of the nodes in the sequence.

Return the maximum score of a valid node sequence with a length of 4. If no such sequence exists, return -1.


Example 1:

Input: scores = [5,2,9,8,4], edges = [[0,1],[1,2],[2,3],[0,2],[1,3],[2,4]]
Output: 24
Explanation: The figure above shows the graph and the chosen node sequence [0,1,2,3].
The score of the node sequence is 5 + 2 + 9 + 8 = 24.
It can be shown that no other node sequence has a score of more than 24.
Note that the sequences [3,1,2,0] and [1,0,2,3] are also valid and have a score of 24.
The sequence [0,3,2,4] is not valid since no edge connects nodes 0 and 3.

Example 2:

Input: scores = [9,20,6,4,11,12], edges = [[0,3],[5,3],[2,4],[1,3]]
Output: -1
Explanation: The figure above shows the graph.
There are no valid node sequences of length 4, so we return -1.


Constraints:

n == scores.length
4 <= n <= 5 * 10^4
1 <= scores[i] <= 10^8
0 <= edges.length <= 5 * 10^4
edges[i].length == 2
0 <= ai, bi <= n - 1
ai != bi
There are no duplicate edges.

"""

# V0
# IDEA : ANCHOR ON THE MIDDLE EDGE, AND ONLY THE TOP-3 NEIGHBOURS CAN MATTER
#
#   a length-4 path is  a - b - c - d, so iterate over every edge as the
#   MIDDLE pair (b, c) and pick the best `a` adjacent to b and best `d`
#   adjacent to c.
#
#   the greedy pick can fail because of the distinctness rule (a may equal c
#   or d), but at most TWO neighbours of b are blocked (c and d), so the
#   answer is always among b's THREE highest-scoring neighbours. same for c.
#   keeping only the top 3 per node turns an O(deg^2) scan into O(9).
#
# time = O(V + E log 3 + E * 9), space = O(V + E)
from collections import defaultdict
import heapq


class Solution(object):
    def maximumScore(self, scores, edges):
        top = defaultdict(list)          # node -> up to 3 best neighbours
        for a, b in edges:
            for u, v in ((a, b), (b, a)):
                heapq.heappush(top[u], (scores[v], v))
                if len(top[u]) > 3:
                    heapq.heappop(top[u])

        res = -1
        for b, c in edges:
            for _, a in top[b]:
                if a == c:
                    continue
                for _, d in top[c]:
                    if d == b or d == a:
                        continue
                    res = max(res, scores[a] + scores[b] + scores[c] + scores[d])
        return res
