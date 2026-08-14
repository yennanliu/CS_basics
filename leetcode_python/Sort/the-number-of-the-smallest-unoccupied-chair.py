"""

1942. The Number of the Smallest Unoccupied Chair
Medium

There is a party where n friends numbered from 0 to n - 1 are attending. There is an infinite number of chairs in this party that are numbered from 0 to infinity. When a friend arrives at the party, they sit on the unoccupied chair with the smallest number.

For example, if chairs 0, 1, and 5 are occupied when a friend comes, they will sit on chair number 2.

When a friend leaves the party, their chair becomes unoccupied at the moment they leave. If another friend arrives at that same moment, they can sit in that chair.

You are given a 0-indexed 2D integer array times where times[i] = [arrivali, leavingi], indicating the arrival and leaving times of the ith friend respectively, and an integer targetFriend. All arrival times are distinct.

Return the chair number that the friend numbered targetFriend will sit on.


Example 1:

Input: times = [[1,4],[2,3],[4,6]], targetFriend = 1
Output: 1
Explanation:
- Friend 0 arrives at time 1 and sits on chair 0.
- Friend 1 arrives at time 2 and sits on chair 1.
- Friend 1 leaves at time 3 and chair 1 becomes empty.
- Friend 0 leaves at time 4 and chair 0 becomes empty.
- Friend 2 arrives at time 4 and sits on chair 0.
Since friend 1 sat on chair 1, we return 1.

Example 2:

Input: times = [[3,10],[1,5],[2,6]], targetFriend = 0
Output: 2
Explanation:
- Friend 1 arrives at time 1 and sits on chair 0.
- Friend 2 arrives at time 2 and sits on chair 1.
- Friend 0 arrives at time 3 and sits on chair 2.
- Friend 1 leaves at time 5 and chair 0 becomes empty.
- Friend 2 leaves at time 6 and chair 1 becomes empty.
- Friend 0 leaves at time 10 and chair 2 becomes empty.
Since friend 0 sat on chair 2, we return 2.


Constraints:

n == times.length
2 <= n <= 10^4
times[i].length == 2
1 <= arrivali < leavingi <= 10^5
0 <= targetFriend <= n - 1
Each arrivali time is distinct.

"""

# V0
# IDEA : SORT BY ARRIVAL + TWO HEAPS (free chairs / occupied chairs by leave time)
#
#   process friends in arrival order (arrivals are distinct so no tie-break).
#   two min-heaps :
#     free : chair numbers currently empty -> pop gives the SMALLEST number
#     busy : (leaving_time, chair) of seated friends
#
#   before seating someone arriving at t, release every busy chair whose
#   leaving time is <= t (a chair freed at exactly t may be reused at t).
#
#   NOTE : at most n friends overlap, so chairs 0..n-1 always suffice -> we can
#          pre-seed `free` with range(n) instead of tracking a "next new chair".
#
# time = O(n log n), space = O(n)
import heapq
class Solution(object):
    def smallestChair(self, times, targetFriend):
        n = len(times)
        order = sorted(range(n), key=lambda i: times[i][0])

        free = list(range(n))
        heapq.heapify(free)
        busy = []                       # (leaving, chair)

        for i in order:
            arrival, leaving = times[i]
            while busy and busy[0][0] <= arrival:
                heapq.heappush(free, heapq.heappop(busy)[1])
            chair = heapq.heappop(free)
            if i == targetFriend:
                return chair
            heapq.heappush(busy, (leaving, chair))
        return -1
