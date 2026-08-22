"""

2237. Count Positions on Street With Required Brightness
Medium
(premium / locked problem)

You are given an integer n. A perfectly straight street is represented by a number line ranging from 0 to n - 1. You are given a 2D integer array lights representing the street lamp(s) on the street. Each lights[i] = [position_i, range_i] indicates that there is a street lamp at position position_i that lights up the area from [max(0, position_i - range_i), min(n - 1, position_i + range_i)] (inclusive).

The brightness of a position p is defined as the number of street lamps that light up the position p. You are given a 0-indexed integer array requirement of size n where requirement[i] is the minimum brightness of the ith position on the street.

Return the number of positions i on the street between 0 and n - 1 that have a brightness of at least requirement[i].


Example 1:

Input: n = 5, lights = [[0,1],[2,1],[3,2]], requirement = [0,2,1,4,1]
Output: 4
Explanation:
- The first street lamp lights up the area from [max(0, 0 - 1), min(n - 1, 0 + 1)] = [0, 1] (inclusive).
- The second street lamp lights up the area from [max(0, 2 - 1), min(n - 1, 2 + 1)] = [1, 3] (inclusive).
- The third street lamp lights up the area from [max(0, 3 - 2), min(n - 1, 3 + 2)] = [1, 4] (inclusive).

- Position 0 is covered by the first street lamp. It is covered by 1 street lamp which is greater than requirement[0].
- Position 1 is covered by all 3 street lamps. It is covered by 3 street lamps which is greater than requirement[1].
- Position 2 is covered by the second and third street lamp. It is covered by 2 street lamps which is greater than requirement[2].
- Position 3 is covered by the second and third street lamp. It is covered by 2 street lamps which is less than requirement[3].
- Position 4 is covered by the third street lamp. It is covered by 1 street lamp which is equal to requirement[4].

Positions 0, 1, 2, and 4 meet the requirement so we return 4.

Example 2:

Input: n = 1, lights = [[0,1]], requirement = [2]
Output: 0
Explanation:
- The first street lamp lights up the area from [max(0, 0 - 1), min(n - 1, 0 + 1)] = [0, 0] (inclusive).
- Position 0 is covered by the first street lamp. It is covered by 1 street lamp which is less than requirement[0].
- We return 0 because no position meets the requirement.


Constraints:

1 <= n <= 10^5
1 <= lights.length <= 10^5
0 <= position_i < n
0 <= range_i <= 10^5
requirement.length == n
0 <= requirement[i] <= 10^5

"""

# V0
# IDEA : DIFFERENCE ARRAY — EACH LAMP IS ONE RANGE INCREMENT
#
#   a lamp adds 1 to the brightness of a whole interval. doing that literally
#   would be O(n) per lamp; instead record +1 at the interval start and -1
#   just past its end, then a single prefix sum turns the diff array into the
#   actual brightness of every position.
#
#   finally compare position by position against `requirement`.
#
#   NOTE : clamp both endpoints to [0, n-1] before recording, exactly as the
#          statement specifies.
#
# time = O(n + len(lights)), space = O(n)
class Solution(object):
    def meetRequirement(self, n, lights, requirement):
        diff = [0] * (n + 1)
        for pos, rng in lights:
            lo = max(0, pos - rng)
            hi = min(n - 1, pos + rng)
            diff[lo] += 1
            diff[hi + 1] -= 1

        res = 0
        brightness = 0
        for i in range(n):
            brightness += diff[i]
            if brightness >= requirement[i]:
                res += 1
        return res


# V0-1
# IDEA : SWEEP THE STREET WITH A MIN-HEAP OF LAMP END POSITIONS
#
#   sort the clamped lamp spans by their start, then walk positions 0..n-1
#   keeping a heap of the END positions of the lamps that currently cover us:
#     - push every span whose start has been reached
#     - pop every span whose end has already been passed
#   the heap size is then literally the brightness of the current position.
#
#   slower than the difference array, but this is the shape to reach for when
#   the question also asks WHICH lamps cover a position, or when the street is
#   sparse/coordinate-compressed rather than an array of size n.
#
# time = O(n + k log k), space = O(k)   with k = len(lights)
import heapq
class Solution(object):
    def meetRequirement(self, n, lights, requirement):
        spans = sorted((max(0, p - r), min(n - 1, p + r)) for p, r in lights)

        heap = []
        idx = 0
        res = 0
        for i in range(n):
            while idx < len(spans) and spans[idx][0] <= i:
                heapq.heappush(heap, spans[idx][1])
                idx += 1
            while heap and heap[0] < i:
                heapq.heappop(heap)
            if len(heap) >= requirement[i]:
                res += 1
        return res


# V0-2
# IDEA : BRUTE FORCE — ASK EVERY LAMP ABOUT EVERY POSITION
#
#   for each position re-derive each lamp's lit interval and test membership,
#   which is the direct reading of the statement. too slow for the real limits
#   (10^5 * 10^5) but it is the ground truth the two sweeps are validated
#   against on small inputs.
#
# time = O(n * k), space = O(1)   with k = len(lights)
class Solution(object):
    def meetRequirement(self, n, lights, requirement):
        res = 0
        for i in range(n):
            brightness = 0
            for p, r in lights:
                if max(0, p - r) <= i <= min(n - 1, p + r):
                    brightness += 1
            if brightness >= requirement[i]:
                res += 1
        return res
