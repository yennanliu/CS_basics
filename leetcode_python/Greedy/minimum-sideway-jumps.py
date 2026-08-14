"""

1824. Minimum Sideway Jumps
Medium

There is a 3 lane road of length n that consists of n + 1 points labeled from 0 to n. A frog starts at point 0 in the second lane and wants to jump to point n. However, there could be obstacles along the way.

You are given an array obstacles of length n + 1 where each obstacles[i] (ranging from 0 to 3) describes an obstacle on the lane obstacles[i] at point i. If obstacles[i] == 0, there are no obstacles at point i. There will be at most one obstacle in the 3 lanes at each point.

For example, if obstacles[2] == 1, then there is an obstacle on lane 1 at point 2.

The frog can only travel from point i to point i + 1 on the same lane if there is not an obstacle on the lane at point i + 1. To avoid obstacles, the frog can also perform a side jump to jump to another lane (even if they are not adjacent) at the same point if there is no obstacle on the new lane.

For example, the frog can jump from lane 3 at point 3 to lane 1 at point 3.

Return the minimum number of side jumps the frog needs to reach any lane at point n starting from lane 2 at point 0.

Note: There will be no obstacles on points 0 and n.


Example 1:

Input: obstacles = [0,1,2,3,0]
Output: 2
Explanation: The optimal solution is shown by the arrows above. There are 2 side jumps (red arrows).
Note that the frog can jump over obstacles only when making side jumps (as shown at point 2).

Example 2:

Input: obstacles = [0,1,1,3,3,0]
Output: 0
Explanation: There are no obstacles on lane 2. No side jumps are required.

Example 3:

Input: obstacles = [0,2,1,0,3,0]
Output: 2
Explanation: The optimal solution is shown by the arrows above. There are 2 side jumps.


Constraints:

obstacles.length == n + 1
1 <= n <= 5 * 10^5
0 <= obstacles[i] <= 3
obstacles[0] == obstacles[n] == 0

"""

# V0
# IDEA : ROLLING DP OVER 3 LANES (f[j] = min side jumps to stand on lane j+1
#        at the current point)
#
#   start : f = [1, 0, 1]  -- the frog is on lane 2 for free, the other two
#   lanes cost one side jump.
#
#   moving to the next point, in this order:
#     1) the lane holding the obstacle becomes unreachable -> f[j] = INF
#     2) side jumps WITHIN the new point : from the cheapest reachable lane we
#        can hop to any other obstacle-free lane for +1, so
#        f[j] = min(f[j], min(f) + 1) for every obstacle-free lane j
#
#   NOTE : step 1 must happen before step 2, otherwise the blocked lane would
#          be used as a stepping stone at that very point.
#   NOTE : lanes are not adjacency-constrained -- one jump reaches any lane --
#          which is why a single min(f) + 1 covers all sideways moves.
#
# time = O(n), space = O(1)
class Solution(object):
    def minSideJumps(self, obstacles):
        INF = float('inf')
        f = [1, 0, 1]
        for v in obstacles[1:]:
            if v:
                f[v - 1] = INF
            best = min(f) + 1
            for j in range(3):
                if v != j + 1 and best < f[j]:
                    f[j] = best
        return min(f)
