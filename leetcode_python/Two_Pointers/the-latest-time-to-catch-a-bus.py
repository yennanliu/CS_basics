"""

2332. The Latest Time to Catch a Bus
Medium

You are given a 0-indexed integer array buses of length n, where buses[i] represents the departure time of the ith bus. You are also given a 0-indexed integer array passengers of length m, where passengers[j] represents the arrival time of the jth passenger. All bus departure times are unique. All passenger arrival times are unique.

You are given an integer capacity, which represents the maximum number of passengers that can get on each bus.

When a passenger arrives, they will wait in line for the next available bus. You can get on a bus that departs at x minutes if you arrive at y minutes where y <= x, and the bus is not full. Passengers with the earliest arrival times get on the bus first.

More formally when a bus arrives, either:

If capacity or fewer passengers are waiting for a bus, they will all get on the bus, or
The capacity passengers with the earliest arrival times will get on the bus.

Return the latest time you may arrive at the bus station to catch a bus. You cannot arrive at the same time as another passenger.

Note: The arrays buses and passengers are not necessarily sorted.


Example 1:

Input: buses = [10,20], passengers = [2,17,18,19], capacity = 2
Output: 16
Explanation: Suppose you arrive at time 16.
At time 10, the first bus departs with the 0th passenger.
At time 20, the second bus departs with you and the 1st passenger.
Note that you may not arrive at the same time as another passenger, which is why you must arrive before the 1st passenger to catch the bus.

Example 2:

Input: buses = [20,30,10], passengers = [19,13,26,4,25,11,21], capacity = 2
Output: 20
Explanation: Suppose you arrive at time 20.
At time 10, the first bus departs with the 3rd passenger.
At time 20, the second bus departs with the 5th and 1st passengers.
At time 30, the third bus departs with the 0th passenger and you.
Notice if you had arrived any later, then the 6th passenger would have taken your seat on the third bus.


Constraints:

n == buses.length
m == passengers.length
1 <= n, m <= 10^5
2 <= buses[i], passengers[i] <= 10^9
Each element in buses is unique.
Each element in passengers is unique.

"""

# V0
# IDEA : SIMULATE THE BOARDING, THEN WALK BACK FROM THE BEST SEAT
#
#   sort both arrays and run the queue : each bus takes up to `capacity`
#   waiting passengers whose arrival is <= its departure.
#
#   after the LAST bus leaves there are two cases :
#     * it still had a free seat -> the best arrival is the bus's departure
#       time itself
#     * it left full            -> the best arrival is one tick before the
#       last passenger who boarded it, so squeeze in ahead of them
#
#   finally, the arrival must be unique, so decrement while it collides with
#   an existing passenger time.
#
# time = O(n log n + m log m), space = O(m)
class Solution(object):
    def latestTimeCatchTheBus(self, buses, passengers, capacity):
        buses = sorted(buses)
        passengers = sorted(passengers)
        taken = set(passengers)

        i = 0                      # index of the next waiting passenger
        seats = 0                  # free seats on the LAST bus processed
        for depart in buses:
            seats = capacity
            while i < len(passengers) and seats > 0 and passengers[i] <= depart:
                i += 1
                seats -= 1

        if seats > 0:
            best = buses[-1]       # room left : arrive exactly at departure
        else:
            best = passengers[i - 1] - 1   # cut in front of the last boarder

        while best in taken:
            best -= 1
        return best
