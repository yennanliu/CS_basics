"""

2463. Minimum Total Distance Traveled
Hard

There are some robots and factories on the X-axis. You are given an integer array robot where robot[i] is the position of the ith robot. You are also given a 2D integer array factory where factory[j] = [position_j, limit_j] indicates that position_j is the position of the jth factory and that it can repair at most limit_j robots.

The positions of each robot are unique. The positions of each factory are also unique. Note that a robot can be in the same position as a factory initially.

All the robots are initially broken; they keep moving in one direction. The direction could be the negative or the positive direction of the X-axis. When a robot reaches a factory that did not reach its limit, the factory repairs the robot, and it stops moving.

At any moment, you can set the initial direction of moving for some robot. Your target is to minimize the total distance traveled by all the robots.

Return the minimum total distance traveled by all the robots. The test cases are generated such that all the robots can be repaired.

Note that

All robots move at the same speed.
If two robots move in the same direction, they will never collide.
If two robots move in opposite directions and they meet at some point, they do not collide. They are considered to move past each other.
If a robot passes by a factory that reached its limits, it crosses it as if it does not exist.
If a robot reached a factory that did not reach its limit, it will be repaired inside it, and it will not leave it.
Initially, all the robots are broken; they keep moving in one direction.


Example 1:

Input: robot = [0,4,6], factory = [[2,2],[6,2]]
Output: 4
Explanation: As shown in the figure:
- The first robot at position 0 moves in the positive direction. It will be repaired at the first factory.
- The second robot at position 4 moves in the negative direction. It will be repaired at the first factory.
- The third robot at position 6 will be repaired at the second factory. It does not need to move.
The limit of the first factory is 2, and it fixed 2 robots.
The limit of the second factory is 2, and it fixed 1 robot.
The total distance is |2 - 0| + |2 - 4| + |6 - 6| = 4. It can be shown that we cannot achieve a better total distance than 4.

Example 2:

Input: robot = [1,-1], factory = [[-2,1],[2,1]]
Output: 2
Explanation: As shown in the figure:
- The first robot at position 1 moves in the positive direction. It will be repaired at the second factory.
- The second robot at position -1 moves in the negative direction. It will be repaired at the first factory.
The total distance is |2 - 1| + |(-2) - (-1)| = 2. It can be shown that we cannot achieve a better total distance than 2.


Constraints:

1 <= robot.length, factory.length <= 100
factory[j].length == 2
-10^9 <= robot[i], position_j <= 10^9
0 <= limit_j <= robot.length
The test cases are generated such that all the robots can be repaired.

"""

# V0
# IDEA : SORT BOTH, THEN DP — AN OPTIMAL ASSIGNMENT NEVER CROSSES
#
#   with everything sorted by position, some optimal solution assigns robots
#   to factories WITHOUT crossings (swapping any crossing pair never
#   increases the total). so each factory takes a CONTIGUOUS block of robots
#   in order.
#
#   dp[i][j] = least distance to repair the first i robots using the first j
#              factories :
#       dp[i][j] = min( dp[i][j-1],                      # factory j unused
#                       dp[i-t][j-1] + cost of the last t robots at factory j )
#   for t = 1 .. limit[j-1], with the block cost accumulated incrementally.
#
#   sizes are <= 100 each, so the triple loop is about 10^6 steps.
#
"""

DP def
    with both lists SORTED by position, some optimal assignment has NO
    crossings (swapping a crossing pair never increases the total), so each
    factory takes a CONTIGUOUS block of robots in order

    dp[i][j]: LEAST total distance to repair the first i robots using

              the first j factories

DP eq

     dp[i][j] = min(
                   dp[i][j-1],                              # factory j unused

                   dp[i-t][j-1] + cost of the last t robots at factory j
                )     for t = 1 .. min(limit[j-1], i)


    -> e.g. the block cost is accumulated INCREMENTALLY as t grows:
              block += abs(robots[i-t] - pos)

     sizes are <= 100 each, so the triple loop is about 10^6 steps

     init: dp[0][j] = 0 (no robots left to repair)
     ans = dp[n][m]

"""
# time = O(n * m * limit), space = O(n * m)
class Solution(object):
    def minimumTotalDistance(self, robot, factory):
        robots = sorted(robot)
        factories = sorted(factory)
        n, m = len(robots), len(factories)
        INF = float('inf')

        dp = [[INF] * (m + 1) for _ in range(n + 1)]
        for j in range(m + 1):
            dp[0][j] = 0                     # no robots left to repair

        for j in range(1, m + 1):
            pos, limit = factories[j - 1]
            for i in range(1, n + 1):
                dp[i][j] = dp[i][j - 1]      # this factory repairs nobody
                block = 0
                for t in range(1, min(limit, i) + 1):
                    block += abs(robots[i - t] - pos)
                    if dp[i - t][j - 1] < INF:
                        dp[i][j] = min(dp[i][j], dp[i - t][j - 1] + block)
        return dp[n][m]
