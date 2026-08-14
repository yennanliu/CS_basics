"""

1499. Max Value of Equation
Hard

You are given an array points containing the coordinates of points on a 2D plane,
sorted by the x-values, where points[i] = [xi, yi] such that xi < xj
for all 1 <= i < j <= points.length. You are also given an integer k.

Return the maximum value of the equation yi + yj + |xi - xj|
where |xi - xj| <= k and 1 <= i < j <= points.length.

It is guaranteed that there exists at least one pair of points
that satisfy the constraint |xi - xj| <= k.


Example 1:

Input: points = [[1,3],[2,0],[5,10],[6,-10]], k = 1
Output: 4
Explanation: The first two points satisfy the condition |xi - xj| <= 1
and if we calculate the equation we get 3 + 0 + |1 - 2| = 4.
Third and fourth points also satisfy the condition and give a value of 10 + -10 + |5 - 6| = 1.
No other pairs satisfy the condition, so we return the max of 4 and 1.

Example 2:

Input: points = [[0,0],[3,0],[9,2]], k = 3
Output: 3
Explanation: Only the first two points have an absolute difference of 3 or less
in the x-values, and give the value of 0 + 0 + |0 - 3| = 3.


Constraints:

2 <= points.length <= 10^5
points[i].length == 2
-10^8 <= xi, yi <= 10^8
0 <= k <= 2 * 10^8
xi < xj for all 1 <= i < j <= points.length
xi form a strictly increasing sequence.

"""

# V0
# IDEA : MONOTONIC DEQUE (sliding window maximum)
#
#   since x is strictly increasing, for i < j we have xi < xj, so
#
#       yi + yj + |xi - xj| = (yi - xi) + (yj + xj)
#
#   -> fix j, we want to maximize (yi - xi) over all i < j with xj - xi <= k
#   -> classic sliding window maximum : keep a deque of candidate i,
#      decreasing in (y - x), and pop from the front once it falls out of the window
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def findMaxValueOfEquation(self, points, k):
        # deque holds (x, y - x), decreasing by (y - x)
        dq = deque()
        res = float('-inf')

        for x, y in points:
            # NOTE !!! drop the candidates that are too far away (x - xi > k)
            while dq and x - dq[0][0] > k:
                dq.popleft()

            if dq:
                res = max(res, y + x + dq[0][1])

            # NOTE !!! keep the deque decreasing on (y - x)
            while dq and dq[-1][1] <= y - x:
                dq.pop()

            dq.append((x, y - x))

        return res


# V1
# IDEA : MAX HEAP (lazy deletion)
#   same transform as above, but instead of a monotonic deque we keep a max heap
#   of (-(y - x), x) and lazily pop the entries whose x already left the window.
# time = O(n log n)
# space = O(n)
import heapq
class Solution(object):
    def findMaxValueOfEquation(self, points, k):
        pq = []
        res = float('-inf')

        for x, y in points:
            while pq and x - pq[0][1] > k:
                heapq.heappop(pq)

            if pq:
                res = max(res, y + x - pq[0][0])

            heapq.heappush(pq, (-(y - x), x))

        return res
