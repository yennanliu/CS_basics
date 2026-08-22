"""

1732. Find the Highest Altitude
Medium

There is a biker going on a road trip. The road trip consists of n + 1 points at various altitudes. The biker starts his trip on point 0 with altitude equal 0.

You are given an integer array gain of length n where gain[i] is the net gain in altitude between points i and i + 1 for all (0 <= i < n). Return the highest altitude of a point.


Example 1:

Input: gain = [-5,1,5,0,-7]
Output: 1
Explanation: The altitudes are [0,-5,-4,1,1,-6]. The highest is 1.

Example 2:

Input: gain = [-4,-3,-2,-1,4,3,2]
Output: 0
Explanation: The altitudes are [0,-4,-7,-9,-10,-6,-3,-1]. The highest is 0.


Constraints:

n == gain.length
1 <= n <= 100
-100 <= gain[i] <= 100

"""

# V0
# IDEA : RUNNING PREFIX SUM (gain is a difference array)
#
#   gain[i] = h[i+1] - h[i], so the altitude of point i+1 is the prefix sum
#       h[i+1] = gain[0] + gain[1] + ... + gain[i]
#
#   the answer is the max prefix sum - no array needed, just carry `h`.
#
#   NOTE : seed the max with 0, since point 0 (altitude 0) is a valid point
#          and may well be the highest one (Example 2).
#
# time = O(n), space = O(1)
class Solution(object):
    def largestAltitude(self, gain):
        res = 0
        h = 0
        for g in gain:
            h += g
            res = max(res, h)
        return res


# V0-1
# IDEA : MATERIALISE THE ALTITUDE ARRAY WITH itertools.accumulate
#
#   accumulate(gain, initial=0) yields the whole altitude list
#       [0, gain[0], gain[0]+gain[1], ...]
#   i.e. exactly the points described in the statement, so max() over it is
#   the answer. useful when the altitudes themselves are wanted (plotting,
#   "which point is highest"), at the cost of O(n) memory.
#
# time = O(n), space = O(n)
from itertools import accumulate


class Solution(object):
    def largestAltitude(self, gain):
        altitudes = list(accumulate(gain, initial=0))
        return max(altitudes)


# V0-2
# IDEA : BRUTE FORCE — RE-SUM THE PREFIX FOR EVERY POINT
#
#   translate the definition literally : the altitude of point i is
#   sum(gain[:i]), so evaluate that sum from scratch for every i in 0..n and
#   keep the largest. no carried state at all, which makes it the easiest
#   version to trust, and O(n^2) is still nothing at n <= 100.
#
#   included as the baseline that V0's single carried `h` optimises.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def largestAltitude(self, gain):
        n = len(gain)
        res = float('-inf')
        for i in range(n + 1):
            h = 0
            for j in range(i):
                h += gain[j]
            res = max(res, h)
        return res
