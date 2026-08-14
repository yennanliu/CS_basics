"""

1776. Car Fleet II
Hard

There are n cars traveling at different speeds in the same direction along a one-lane road. You are given an array cars of length n, where cars[i] = [positioni, speedi] represents:

positioni is the distance between the i^th car and the beginning of the road in meters. It is guaranteed that positioni < positioni+1.
speedi is the initial speed of the i^th car in meters per second.

For simplicity, cars can be considered as points moving along the number line. Two cars collide when they occupy the same position. Once a car collides with another car, they unite and form a single car fleet. The cars in the formed fleet will have the same position and the same speed, which is the initial speed of the slowest car in the fleet.

Return an array answer, where answer[i] is the time, in seconds, at which the i^th car collides with the next car, or -1 if the car does not collide with the next car. Answers within 10^-5 of the actual answers are accepted.

Example 1:

Input: cars = [[1,2],[2,1],[4,3],[7,2]]
Output: [1.00000,-1.00000,3.00000,-1.00000]
Explanation: After exactly one second, the first car will collide with the second car, and form a car fleet with speed 1 m/s. After exactly 3 seconds, the third car will collide with the fourth car, and form a car fleet with speed 2 m/s.

Example 2:

Input: cars = [[3,4],[5,4],[6,3],[9,1]]
Output: [2.00000,1.00000,1.50000,-1.00000]

Constraints:

1 <= cars.length <= 10^5
1 <= positioni, speedi <= 10^6
positioni < positioni+1

"""

# V0
# IDEA : MONOTONIC STACK SCANNED RIGHT TO LEFT
#
#   car i can only ever hit car i + 1, i + 2, ... . process cars from the right
#   keeping a stack of the cars that are still "catchable" candidates.
#   for the top candidate j :
#     - if speed[i] <= speed[j], car i never reaches j -> pop j
#     - else the naive meeting time is t = (pos[j] - pos[i]) / (v[i] - v[j]);
#       if j itself already merged at ans[j] < t, then by time t car j is gone
#       (it slowed down earlier) -> pop j and try the next candidate
#     - otherwise t is the real answer for i
#   NOTE : ans[j] == -1 means j never collides, so its speed never changes and
#          t is valid immediately.
#
# time = O(n) amortised, space = O(n)
class Solution(object):
    def getCollisionTimes(self, cars):
        n = len(cars)
        res = [-1.0] * n
        stack = []
        for i in range(n - 1, -1, -1):
            pi, vi = cars[i]
            while stack:
                j = stack[-1]
                pj, vj = cars[j]
                if vi > vj:
                    t = float(pj - pi) / (vi - vj)
                    if res[j] == -1 or t <= res[j]:
                        res[i] = t
                        break
                stack.pop()
            stack.append(i)
        return res
