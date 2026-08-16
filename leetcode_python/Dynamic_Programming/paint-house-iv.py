"""

3429. Paint House IV
Medium

You are given an even integer n representing the number of houses arranged in a
straight line, and a 2D array cost of size n x 3, where cost[i][j] represents
the cost of painting house i with color j + 1.

The houses will look beautiful if they satisfy the following conditions:

No two adjacent houses are painted the same color.
Houses equidistant from the ends of the row are not painted the same color. For
example, if n = 6, houses at positions (0, 5), (1, 4), and (2, 3) are considered
equidistant.

Return the minimum cost to paint the houses such that they look beautiful.

Example 1:

Input: n = 4, cost = [[3,5,7],[6,2,9],[4,8,1],[7,3,5]]

Output: 9

Explanation:

The optimal painting sequence is [1, 2, 3, 2] with corresponding costs [3, 2, 1,
3]. This satisfies the following conditions:

No adjacent houses have the same color.
Houses at positions 0 and 3 (equidistant from the ends) are not painted the same
color (1 != 2).
Houses at positions 1 and 2 (equidistant from the ends) are not painted the same
color (2 != 3).

The minimum cost to paint the houses so that they look beautiful is 3 + 2 + 1 +
3 = 9.

Example 2:

Input: n = 6, cost = [[2,4,6],[5,3,8],[7,1,9],[4,6,2],[3,5,7],[8,2,4]]

Output: 18

Explanation:

The optimal painting sequence is [1, 3, 2, 3, 1, 2] with corresponding costs [2,
8, 1, 2, 3, 2]. This satisfies the following conditions:

No adjacent houses have the same color.
Houses at positions 0 and 5 (equidistant from the ends) are not painted the same
color (1 != 2).
Houses at positions 1 and 4 (equidistant from the ends) are not painted the same
color (3 != 1).
Houses at positions 2 and 3 (equidistant from the ends) are not painted the same
color (2 != 3).

The minimum cost to paint the houses so that they look beautiful is 2 + 8 + 1 +
2 + 3 + 2 = 18.

Constraints:

2 <= n <= 10^5
n is even.
cost.length == n
cost[i].length == 3
0 <= cost[i][j] <= 10^5

"""

# V0
# IDEA : FOLD THE ROW IN HALF AND WALK BOTH ENDS INWARD TOGETHER
#
#   the "equidistant from the ends" rule ties house i to house n-1-i, so the two
#   halves cannot be decided independently.  process them in lockstep instead:
#   step t handles the pair (t, n-1-t), and the state is the pair of colours
#   (a, b) painted there — only 6 combinations survive, since the mirror rule
#   forbids a == b.
#
#   moving from pair t to pair t+1 must respect adjacency on *both* sides: the
#   new left colour differs from the old left colour, the new right colour
#   differs from the old right colour, and the new pair itself must differ.
#   with 6 states and at most 4 legal predecessors each, one step is 24 compares
#   no matter how long the row is.
#
#   the two halves meet at houses n/2 - 1 and n/2, which are the two members of
#   the final pair — and they are adjacent.  the mirror constraint a != b on
#   that pair *is* the adjacency constraint, so nothing extra is needed at the
#   seam.
#
# time = O(n), space = O(1)
class Solution(object):
    def minCost(self, n, cost):
        states = [(a, b) for a in range(3) for b in range(3) if a != b]
        prevOf = []
        for c, d in states:
            prevOf.append([i for i, (a, b) in enumerate(states)
                           if a != c and b != d])

        INF = float('inf')
        dp = [cost[0][a] + cost[n - 1][b] for a, b in states]
        for t in range(1, n // 2):
            lo = cost[t]
            hi = cost[n - 1 - t]
            ndp = []
            for s, (c, d) in enumerate(states):
                best = INF
                for p in prevOf[s]:
                    if dp[p] < best:
                        best = dp[p]
                ndp.append(best + lo[c] + hi[d])
            dp = ndp
        return min(dp)
