"""

3661. Maximum Walls Destroyed by Robots
Hard

There is an endless straight line populated with some robots and walls. You
are given integer arrays robots, distance, and walls:

robots[i] is the position of the ith robot.
distance[i] is the maximum distance the ith robot's bullet can travel.
walls[j] is the position of the jth wall.

Every robot has one bullet that can either fire to the left or the right at
most distance[i] meters.

A bullet destroys every wall in its path that lies within its range. Robots
are fixed obstacles: if a bullet hits another robot before reaching a wall,
it immediately stops at that robot and cannot continue.

Return the maximum number of unique walls that can be destroyed by the
robots.

Notes:

A wall and a robot may share the same position; the wall can be destroyed
by the robot at that position.
Robots are not destroyed by bullets.


Example 1:

Input: robots = [4], distance = [3], walls = [1,10]
Output: 1
Explanation:
robots[0] = 4 fires left with distance[0] = 3, covering [1, 4] and destroys
walls[0] = 1.
Thus, the answer is 1.

Example 2:

Input: robots = [10,2], distance = [5,1], walls = [5,2,7]
Output: 3
Explanation:
robots[0] = 10 fires left with distance[0] = 5, covering [5, 10] and
destroys walls[0] = 5 and walls[2] = 7.
robots[1] = 2 fires left with distance[1] = 1, covering [1, 2] and destroys
walls[1] = 2.
Thus, the answer is 3.

Example 3:

Input: robots = [1,2], distance = [100,1], walls = [10]
Output: 0
Explanation:
In this example, only robots[0] can reach the wall, but its shot to the
right is blocked by robots[1]; thus the answer is 0.


Constraints:

1 <= robots.length == distance.length <= 10^5
1 <= walls.length <= 10^5
1 <= robots[i], walls[j] <= 10^9
All values in robots are unique
All values in walls are unique

"""

# V0
# IDEA : SORT BY POSITION, DP OVER "WHICH WAY DID THE PREVIOUS ROBOT FIRE"
#
#   once the robots are sorted, a bullet can never pass a neighbour, so the
#   interval a robot covers lives strictly between its two neighbours:
#     firing right -> [p_i, min(p_i + d_i, p_{i+1} - 1)]
#     firing left  -> [max(p_i - d_i, p_{i-1} + 1), p_i]
#   which means only ADJACENT robots can ever cover a common wall, and they
#   can do so only in the one combination "i-1 fires right, i fires left".
#   that single interaction is what the DP state has to remember, so one bit
#   -- the previous robot's direction -- suffices.
#
#   walls are UNIQUE, so double counting is the only hazard; we kill it by
#   trimming rather than by inclusion-exclusion. when the previous robot
#   fired right, robot i's left interval is additionally clipped to start
#   after wherever that bullet stopped, so the two intervals become disjoint
#   and their wall counts simply add.
#
#   with walls sorted, each interval's wall count is a difference of two
#   binary searches, and the DP is two rolling numbers.
#
# time = O(n log n + m log m), space = O(n + m)
class Solution(object):
    def maxWalls(self, robots, distance, walls):
        from bisect import bisect_left, bisect_right

        arr = sorted(zip(robots, distance))
        n = len(arr)
        walls = sorted(walls)

        def count(lo, hi):
            if lo > hi:
                return 0
            return bisect_right(walls, hi) - bisect_left(walls, lo)

        NEG = float('-inf')
        # dp[0] = best so far with this robot firing left, dp[1] = firing right
        dp0 = dp1 = 0
        for i in range(n):
            p, d = arr[i]
            pp, pd = arr[i - 1] if i else (NEG, 0)
            # firing right: never overlaps anything to the left of p
            hi = p + d
            if i + 1 < n and arr[i + 1][0] - 1 < hi:
                hi = arr[i + 1][0] - 1
            right_gain = max(dp0, dp1) + count(p, hi)

            # firing left: floor at the previous robot, plus a trim when that
            # robot's own bullet already swept part of the gap
            lo_free = p - d
            if i and pp + 1 > lo_free:
                lo_free = pp + 1
            if i:
                prev_right_end = pp + pd
                if p - 1 < prev_right_end:
                    prev_right_end = p - 1
                lo_blocked = max(lo_free, prev_right_end + 1)
            else:
                lo_blocked = lo_free
            left_gain = max(dp0 + count(lo_free, p), dp1 + count(lo_blocked, p))

            dp0, dp1 = left_gain, right_gain
        return max(dp0, dp1)
