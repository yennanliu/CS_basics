"""

2187. Minimum Time to Complete Trips
Medium

You are given an array time where time[i] denotes the time taken by the ith bus to complete one trip.

Each bus can make multiple trips successively; that is, the next trip can start immediately after completing the current trip. Also, each bus operates independently; that is, the trips of one bus do not influence the trips of any other bus.

You are also given an integer totalTrips, which denotes the number of trips all buses should make in total. Return the minimum time required for all buses to complete at least totalTrips trips.


Example 1:

Input: time = [1,2,3], totalTrips = 5
Output: 3
Explanation:
- At time t = 1, the number of trips completed by each bus are [1,0,0].
  The total number of trips completed is 1 + 0 + 0 = 1.
- At time t = 2, the number of trips completed by each bus are [2,1,0].
  The total number of trips completed is 2 + 1 + 0 = 3.
- At time t = 3, the number of trips completed by each bus are [3,1,1].
  The total number of trips completed is 3 + 1 + 1 = 5.
So the minimum time needed for all buses to complete at least 5 trips is 3.

Example 2:

Input: time = [2], totalTrips = 1
Output: 2
Explanation:
There is only one bus, and it will complete its first trip at t = 2.
So the minimum time needed to complete 1 trip is 2.


Constraints:

1 <= time.length <= 10^5
1 <= time[i], totalTrips <= 10^7

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER (a time, not an index)
#
#   in t units of time bus i finishes  t // time[i]  trips, so the fleet
#   completes  sum(t // time[i])  trips — a NON-DECREASING function of t.
#   that monotonicity is what makes binary search valid.
#
#   search t over [1, min(time) * totalTrips] : the upper bound is what the
#   single fastest bus would need on its own, and is certainly enough.
#
# time = O(n log(min(time) * totalTrips)), space = O(1)
class Solution(object):
    def minimumTime(self, time, totalTrips):
        lo, hi = 1, min(time) * totalTrips
        while lo < hi:
            mid = (lo + hi) // 2
            if sum(mid // t for t in time) >= totalTrips:
                hi = mid
            else:
                lo = mid + 1
        return lo
