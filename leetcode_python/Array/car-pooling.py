# https://leetcode.com/problems/car-pooling/description/
"""

1094. Car Pooling
Solved
Medium
Topics
premium lock icon
Companies
Hint
There is a car with capacity empty seats. The vehicle only drives east (i.e., it cannot turn around and drive west).

You are given the integer capacity and an array trips where trips[i] = [numPassengersi, fromi, toi] indicates that the ith trip has numPassengersi passengers and the locations to pick them up and drop them off are fromi and toi respectively. The locations are given as the number of kilometers due east from the car's initial location.

Return true if it is possible to pick up and drop off all passengers for all the given trips, or false otherwise.

 

Example 1:

Input: trips = [[2,1,5],[3,3,7]], capacity = 4
Output: false
Example 2:

Input: trips = [[2,1,5],[3,3,7]], capacity = 5
Output: true
 

Constraints:

1 <= trips.length <= 1000
trips[i].length == 3
1 <= numPassengersi <= 100
0 <= fromi < toi <= 1000
1 <= capacity <= 105
 

"""


# V0
class Solution(object):
    def carPooling(self, trips, capacity):
        """
        :type trips: List[List[int]]
        :type capacity: int
        :rtype: bool
        """
        pass


# V0-1
# IDEA : DIFFERENCE ARRAY (BUCKET BY LOCATION) + PREFIX SUM
#
#   locations are bounded by 1000, so we can keep one bucket per kilometre :
#   +p at `from`, -p at `to`. sweeping the prefix sum west -> east then gives
#   the number of passengers on board at every kilometre.
#
# time = O(n + L)   (L = 1001 possible locations)
# space = O(L)
class Solution(object):
    def carPooling(self, trips, capacity):
        diff = [0] * 1001
        for p, start, end in trips:
            diff[start] += p
            diff[end] -= p
        on_board = 0
        for delta in diff:
            on_board += delta
            if on_board > capacity:
                return False
        return True


# V0-2
# IDEA : SORT THE PICK-UP / DROP-OFF EVENTS AND SWEEP
#
#   the same sweep, but WITHOUT assuming a bounded coordinate range : build
#   2 * n events and sort them.
#   NOTE !!! the delta is the tie breaker, so (x, -p) sorts before (x, +p) and
#            a drop-off at x frees its seats before a pick-up at x needs them.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def carPooling(self, trips, capacity):
        events = []
        for p, start, end in trips:
            events.append((start, p))
            events.append((end, -p))
        events.sort()
        on_board = 0
        for _, delta in events:
            on_board += delta
            if on_board > capacity:
                return False
        return True


# V0-3
# IDEA : MIN HEAP OF ACTIVE TRIPS, KEYED BY DROP-OFF LOCATION
#
#   the "meeting rooms II" pattern : walk the trips in pick-up order and,
#   before boarding a new one, pop every active trip whose drop-off is already
#   behind us (<= current pick-up) and hand those seats back.
#
# time = O(n log n)
# space = O(n)
import heapq
class Solution(object):
    def carPooling(self, trips, capacity):
        active = []          # min heap of (drop_off_location, passengers)
        on_board = 0
        for p, start, end in sorted(trips, key=lambda t: t[1]):
            while active and active[0][0] <= start:
                on_board -= heapq.heappop(active)[1]
            on_board += p
            if on_board > capacity:
                return False
            heapq.heappush(active, (end, p))
        return True


# V1


# V2
