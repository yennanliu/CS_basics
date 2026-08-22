"""

1184. Distance Between Bus Stops
Easy

A bus has n stops numbered from 0 to n - 1 that form a circle.
We know the distance between all pairs of neighboring stops where distance[i] is the distance
between the stops number i and (i + 1) % n.

The bus goes along both directions i.e. clockwise and counterclockwise.

Return the shortest distance between the given start and destination stops.


Example 1:

Input: distance = [1,2,3,4], start = 0, destination = 1

Output: 1

Explanation: Distance between 0 and 1 is 1 or 9, minimum is 1.

Example 2:

Input: distance = [1,2,3,4], start = 0, destination = 2

Output: 3

Explanation: Distance between 0 and 2 is 3 or 7, minimum is 3.

Example 3:

Input: distance = [1,2,3,4], start = 0, destination = 3

Output: 4

Explanation: Distance between 0 and 3 is 6 or 4, minimum is 4.


Constraints:

1 <= n <= 10^4
distance.length == n
0 <= start, destination < n
0 <= distance[i] <= 10^4

"""

# V0
# IDEA : PREFIX SUM on a circle (one arc + its complement)
#
#   the ring is split by {start, destination} into exactly 2 arcs, and their
#   lengths add up to total = sum(distance).
#   so we only need to measure one of them :
#     clockwise = distance[lo] + ... + distance[hi - 1]
#   and the other arc is total - clockwise.
#   NOTE : normalise with lo = min(start, destination), hi = max(...) so the
#          slice is a plain forward range and no wrap-around is needed.
#
# time = O(n), space = O(1)
class Solution(object):
    def distanceBetweenBusStops(self, distance, start, destination):
        lo, hi = min(start, destination), max(start, destination)
        total = sum(distance)
        clockwise = sum(distance[lo:hi])
        return min(clockwise, total - clockwise)


# V0-1
# IDEA : EXPLICIT PREFIX-SUM TABLE (O(1) per query afterwards)
#
#   precompute P where P[k] = distance[0] + ... + distance[k-1] (P[0] = 0).
#   then ANY forward arc [lo, hi) is P[hi] - P[lo] in constant time, and the
#   whole ring is P[n], so :
#     clockwise = P[hi] - P[lo],  counter = P[n] - clockwise
#
#   V0 re-sums a slice each time; the table makes the summation a one-off, so
#   answering q different (start, destination) pairs costs O(n + q) instead of
#   O(n * q). itertools.accumulate builds it in one C-level pass.
#
# time = O(n) to build then O(1) per query, space = O(n)
class Solution(object):
    def distanceBetweenBusStops(self, distance, start, destination):
        from itertools import accumulate

        pre = [0] + list(accumulate(distance))
        lo, hi = min(start, destination), max(start, destination)
        clockwise = pre[hi] - pre[lo]
        return min(clockwise, pre[-1] - clockwise)


# V0-2
# IDEA : SIMULATE THE BUS IN BOTH DIRECTIONS WITH MODULAR STEPS
#
#   actually ride the ring. going clockwise from i you pay distance[i] and
#   land on (i + 1) % n; going counterclockwise from i you step back to
#   (i - 1) % n and pay distance[(i - 1) % n] (the edge you just crossed).
#
#   walk each direction from start until destination is reached, accumulating,
#   and take the smaller total. No min/max normalisation and no total sum are
#   needed - the wrap-around is handled by the modulus itself, which is the
#   version that still works if the ring were directed or edge costs differed
#   per direction.
#
# time = O(n), space = O(1)
class Solution(object):
    def distanceBetweenBusStops(self, distance, start, destination):
        n = len(distance)

        cw = 0
        i = start
        while i != destination:
            cw += distance[i]
            i = (i + 1) % n

        ccw = 0
        i = start
        while i != destination:
            i = (i - 1) % n
            ccw += distance[i]

        return min(cw, ccw)
