"""

871. Minimum Number of Refueling Stops
Hard

A car travels from a starting position to a destination which is target miles
east of the starting position.

There are gas stations along the way. The gas stations are represented as an
array stations where stations[i] = [positioni, fueli] indicates that the ith
gas station is positioni miles east of the starting position and has fueli
liters of gas.

The car starts with an infinite tank of gas, which initially has startFuel
liters of fuel in it. It uses one liter of gas per one mile that it drives.
When the car reaches a gas station, it may stop and refuel, transferring all
the gas from the station into the car.

Return the minimum number of refueling stops the car must make in order to
reach its destination. If it cannot reach the destination, return -1.

Note that if the car reaches a gas station with 0 fuel left, the car can still
refuel there. If the car reaches the destination with 0 fuel left, it is still
considered to have arrived.

Example 1:

Input: target = 1, startFuel = 1, stations = []

Output: 0

Explanation: We can reach the target without refueling.

Example 2:

Input: target = 100, startFuel = 1, stations = [[10,100]]

Output: -1

Explanation: We can not reach the target (or even the first gas station).

Example 3:

Input: target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]]

Output: 2

Explanation: We start with 10 liters of fuel. We drive to position 10,
expending 10 liters of fuel. We refuel from 0 liters to 60 liters of gas.
Then, we drive from position 10 to position 60 (expending 50 liters of fuel),
and refuel from 10 liters to 50 liters of gas. We then drive to and reach the
target. We made 2 refueling stops along the way, so we return 2.

Constraints:

1 <= target, startFuel <= 10^9
0 <= stations.length <= 500
1 <= positioni < positioni+1 < target
1 <= fueli < 10^9

"""

# V0
# IDEA : GREEDY + MAX HEAP ("REFUEL IN HINDSIGHT")
#
#   Drive as far as the current fuel allows, remembering the fuel of every
#   station passed in a max heap. Only when the next station (or the target)
#   is out of reach do we "go back in time" and take the BIGGEST fuel we
#   skipped — one stop buys the most distance, so it is never worse to pick
#   it. If the heap is empty while still short, the target is unreachable.
#
#   Where the stop physically happened does not matter: we had already driven
#   past that station, so we could have refueled there.
#
#   e.g. target=100, fuel=10, [[10,60],[20,30],[30,30],[60,40]]
#        reach 10 (heap [60]) -> 20 out of reach -> take 60, fuel 70, stops=1
#        pass 20, 30, 60 (heap [30,30,40]) -> 100 out of reach (reach=70)
#        -> take 40, reach 110, stops=2 -> answer 2
#
# time = O(n log n), space = O(n)
import heapq


class Solution(object):
    def minRefuelStops(self, target, startFuel, stations):
        """
        :type target: int
        :type startFuel: int
        :type stations: List[List[int]]
        :rtype: int
        """
        # heapq is a min heap -> store negated fuel for a max heap
        skipped_fuel = []
        # farthest position reachable with the stops made so far
        reach = startFuel
        stops = 0

        # treat the target as a final station with no fuel, so the same loop
        # checks whether we can arrive there
        checkpoints = list(stations) + [[target, 0]]

        for position, fuel in checkpoints:
            # NOTE !!! use a `while`: one stop may not be enough to get here
            while reach < position:
                if not skipped_fuel:
                    # nothing left to refuel from -> stuck before `position`
                    return -1
                reach += -heapq.heappop(skipped_fuel)
                stops += 1

            # we can reach this station -> remember its fuel for later
            heapq.heappush(skipped_fuel, -fuel)

        return stops


# V1
# IDEA : 1D DP — FARTHEST REACH WITH i STOPS
#
#   dp[i] = the farthest position reachable using exactly i refueling stops,
#           among the stations seen so far.
#   base : dp[0] = startFuel
#   For each station (in order), for i from high to low:
#       if dp[i] >= position (station reachable with i stops):
#           dp[i + 1] = max(dp[i + 1], dp[i] + fuel)
#   Answer: the smallest i with dp[i] >= target.
#
#   i runs DOWNWARD so each station is used at most once per pass, just like
#   the 0/1 knapsack rolling array.
#
#   NOTE : O(n^2) — fine for n <= 500, but V0 is the one to use in an interview
#
# time = O(n^2), space = O(n)


class Solution2(object):
    def minRefuelStops(self, target, startFuel, stations):
        """
        :type target: int
        :type startFuel: int
        :type stations: List[List[int]]
        :rtype: int
        """
        station_count = len(stations)

        # dp[i] = farthest reach using i stops (0 = not reachable yet)
        dp = [0] * (station_count + 1)
        dp[0] = startFuel

        for station_idx in range(station_count):
            position = stations[station_idx][0]
            fuel = stations[station_idx][1]

            # at most station_idx stops could have been made before this one;
            # iterate downward so this station counts at most once
            for stops in range(station_idx, -1, -1):
                if dp[stops] >= position:
                    dp[stops + 1] = max(dp[stops + 1], dp[stops] + fuel)

        # the first stop count that reaches the target is the minimum
        for stops in range(station_count + 1):
            if dp[stops] >= target:
                return stops

        return -1
