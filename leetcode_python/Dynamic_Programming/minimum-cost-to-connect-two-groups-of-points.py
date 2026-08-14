"""

1595. Minimum Cost to Connect Two Groups of Points
Hard

You are given two groups of points where the first group has size1 points, the second group has size2 points, and size1 >= size2.

The cost of the connection between any two points are given in an size1 x size2 matrix where cost[i][j] is the cost of connecting point i of the first group and point j of the second group. The groups are connected if each point in both groups is connected to one or more points in the opposite group. In other words, each point in the first group must be connected to at least one point in the second group, and each point in the second group must be connected to at least one point in the first group.

Return the minimum cost it takes to connect the two groups.

Example 1:

Input: cost = [[15, 96], [36, 2]]
Output: 17
Explanation: The optimal way of connecting the groups is:
1--A
2--B
This results in a total cost of 17.

Example 2:

Input: cost = [[1, 3, 5], [4, 1, 1], [1, 5, 3]]
Output: 4
Explanation: The optimal way of connecting the groups is:
1--A
2--B
2--C
3--A
This results in a total cost of 4.
Note that there are multiple points connected to point 2 in the first group and point A in the second group. This does not matter as there is no limit to the number of points that can be connected. We only care about the minimum total cost.

Example 3:

Input: cost = [[2, 5, 1], [3, 4, 7], [8, 1, 2], [6, 2, 4], [3, 8, 8]]
Output: 10

Constraints:

size1 == cost.length
size2 == cost[i].length
1 <= size1, size2 <= 12
size1 >= size2
0 <= cost[i][j] <= 100

"""

# V0
# IDEA : BITMASK DP over group 2 (size2 <= 12)
#
#   f[i][mask] = min cost after deciding the edges of the first i points
#                of group 1, "mask" = points of group 2 already covered.
#   point i (1-indexed) may take several partners, so for every j in mask
#   we allow three predecessors :
#     f[i-1][mask]              j was already covered, i connects to it
#     f[i-1][mask ^ bit(j)]     i connects to j, j is new
#     f[i  ][mask ^ bit(j)]     i connects to one MORE partner j
#   every f[i][mask] is written only through some j -> each group-1 point
#   automatically gets at least one edge, and f[m][full] forces every
#   group-2 point to be covered.
#
# time = O(m * 2^n * n), space = O(m * 2^n)
class Solution(object):
    def connectTwoGroups(self, cost):
        m, n = len(cost), len(cost[0])
        full = 1 << n
        INF = float('inf')
        f = [[INF] * full for _ in range(m + 1)]
        f[0][0] = 0
        for i in range(1, m + 1):
            row, prev = f[i], f[i - 1]
            ci = cost[i - 1]
            for mask in range(full):
                best = INF
                for j in range(n):
                    bit = 1 << j
                    if not (mask & bit):
                        continue
                    rest = mask ^ bit
                    cand = prev[mask]
                    if prev[rest] < cand:
                        cand = prev[rest]
                    if row[rest] < cand:
                        cand = row[rest]
                    cand += ci[j]
                    if cand < best:
                        best = cand
                row[mask] = best
        return f[m][full - 1]
