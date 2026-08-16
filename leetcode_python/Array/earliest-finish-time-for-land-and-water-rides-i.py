"""

3633. Earliest Finish Time for Land and Water Rides I
Easy

You are given two categories of theme park attractions: land rides and water rides.

Land rides are represented by two arrays landStartTime and landDuration, where landStartTime[i] is the earliest time the ith land ride can be boarded, and landDuration[i] is how long it lasts.
Water rides are represented by two arrays waterStartTime and waterDuration, where waterStartTime[j] is the earliest time the jth water ride can be boarded, and waterDuration[j] is how long it lasts.

A tourist must experience exactly one ride of each type, in either order.

A ride can be boarded at any time t >= the ride's start time. If a ride is boarded at time t, it ends at time t + duration.
Immediately after finishing one ride the tourist may board the other (if it is already available) or wait for it to become available.

Return the earliest possible time at which the tourist can finish both rides.


Example 1:

Input: landStartTime = [2,8], landDuration = [4,1], waterStartTime = [6], waterDuration = [3]
Output: 9
Explanation:
Board the land ride at index 0 at time 2, it finishes at 2 + 4 = 6. Then board the water ride at index 0 at time 6, it finishes at 6 + 3 = 9.
Hence, the earliest time to finish both rides is 9.

Example 2:

Input: landStartTime = [5], landDuration = [3], waterStartTime = [1], waterDuration = [10]
Output: 14
Explanation:
Board the water ride at index 0 at time 1, it finishes at 1 + 10 = 11. Then board the land ride at index 0 at time 11, it finishes at 11 + 3 = 14.
Hence, the earliest time to finish both rides is 14.


Constraints:

1 <= n, m <= 100
landStartTime.length == landDuration.length == n
waterStartTime.length == waterDuration.length == m
1 <= landStartTime[i], landDuration[i] <= 1000
1 <= waterStartTime[j], waterDuration[j] <= 1000

"""

# V0
# IDEA : BRUTE FORCE OVER BOTH ORDERS
#
#   waiting is never useful before the FIRST ride: boarding it exactly at its
#   own start time is optimal, because every later boarding only pushes the
#   second ride's start later or leaves it unchanged.
#
#   so a plan is fully described by (which land ride, which water ride, which
#   goes first), and its finish time is
#       t = start_first + duration_first
#       finish = max(t, start_second) + duration_second
#
#   n, m <= 100 so all 2*n*m plans can simply be enumerated.
#
# time = O(n * m), space = O(1)
class Solution(object):
    def earliestFinishTime(self, landStartTime, landDuration, waterStartTime, waterDuration):
        best = float('inf')
        for i in range(len(landStartTime)):
            t = landStartTime[i] + landDuration[i]
            for j in range(len(waterStartTime)):
                fin = max(t, waterStartTime[j]) + waterDuration[j]
                if fin < best:
                    best = fin
        for j in range(len(waterStartTime)):
            t = waterStartTime[j] + waterDuration[j]
            for i in range(len(landStartTime)):
                fin = max(t, landStartTime[i]) + landDuration[i]
                if fin < best:
                    best = fin
        return best
