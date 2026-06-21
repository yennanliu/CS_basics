# https://leetcode.com/problems/minimize-max-distance-to-gas-station/description/
# https://leetcode.ca/2018-01-12-774-Minimize-Max-Distance-to-Gas-Station/

"""

774. Minimize Max Distance to Gas Station
Description
You are given an integer array stations that represents the positions of the gas stations on the x-axis. You are also given an integer k.

You should add k new gas stations. You can add the stations anywhere on the x-axis, and not necessarily on an integer position.

Let penalty() be the maximum distance between adjacent gas stations after adding the k new stations.

Return the smallest possible value of penalty(). Answers within 10-6 of the actual answer will be accepted.

 

Example 1:

Input: stations = [1,2,3,4,5,6,7,8,9,10], k = 9
Output: 0.50000
Example 2:

Input: stations = [23,24,36,39,46,56,57,65,84,98], k = 1
Output: 14.00000
 

Constraints:

10 <= stations.length <= 2000
0 <= stations[i] <= 108
stations is sorted in a strictly increasing order.
1 <= k <= 106

"""


# V0
class Solution:
    def minmaxGasDist(self, stations: List[int], k: int) -> float:
        pass


# V0-1
# gemini
# TODO: validate
class Solution(object):
    def minmaxGasDist(self, stations, k):
        """
        :type stations: List[int]
        :type k: int
        :rtype: float
        """
        # Step 1: Initialize floating-point boundaries
        l = 0.0
        r = float(stations[-1] - stations[0])
        
        # FIX: Loop until the search window is smaller than the required precision threshold
        while r - l > 1e-6:
            mid = l + (r - l) / 2.0
            
            # If this maximum distance can be achieved with <= k stations
            if self.can_allocate(stations, k, mid):
                # FIX: We want to MINIMIZE the distance, so look left.
                # In floating binary search, do NOT do 'mid - 1', just use 'mid'
                r = mid
            else:
                # This distance is too tight; look right to find a larger allowed max distance
                # Do NOT do 'mid + 1', just use 'mid'
                l = mid
                
        # Both l and r have converged to the answer within 10^-6 precision
        return l

    def can_allocate(self, stations, k, max_dist):
        stations_needed = 0
        for i in range(1, len(stations)):
            actual_gap = stations[i] - stations[i - 1]
            stations_needed += int(actual_gap / max_dist)
        return stations_needed <= k


# V0-2
# gpt
# TODO: validate
import math

class Solution:
    def minmaxGasDist(self, stations, k):
        l = 0.0
        r = max(
            stations[i + 1] - stations[i]
            for i in range(len(stations) - 1)
        )

        eps = 1e-6

        while r - l > eps:
            mid = (l + r) / 2

            if self.can_allocate(stations, k, mid):
                r = mid      # try smaller maximum distance
            else:
                l = mid      # need a larger distance

        return r

    def can_allocate(self, stations, k, max_dist):
        needed = 0

        for i in range(len(stations) - 1):
            gap = stations[i + 1] - stations[i]
            needed += math.ceil(gap / max_dist) - 1

        return needed <= k


# V1
class Solution:
    def minmaxGasDist(self, stations: List[int], k: int) -> float:
        def check(x):
            return sum(int((b - a) / x) for a, b in pairwise(stations)) <= k

        left, right = 0, 1e8
        while right - left > 1e-6:
            mid = (left + right) / 2
            if check(mid):
                right = mid
            else:
                left = mid
        return left

# V2

# V3