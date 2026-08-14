"""

1326. Minimum Number of Taps to Open to Water a Garden
Hard

There is a one-dimensional garden on the x-axis. The garden starts at the point 0
and ends at the point n. (i.e., the length of the garden is n).

There are n + 1 taps located at points [0, 1, ..., n] in the garden.

Given an integer n and an integer array ranges of length n + 1 where ranges[i] (0-indexed)
means the i-th tap can water the area [i - ranges[i], i + ranges[i]] if it was open.

Return the minimum number of taps that should be open to water the whole garden,
If the garden cannot be watered return -1.


Example 1:

Input: n = 5, ranges = [3,4,1,1,0,0]
Output: 1
Explanation: The tap at point 0 can cover the interval [-3,3]
The tap at point 1 can cover the interval [-3,5]
The tap at point 2 can cover the interval [1,3]
The tap at point 3 can cover the interval [2,4]
The tap at point 4 can cover the interval [4,4]
The tap at point 5 can cover the interval [5,5]
Opening Only the second tap will water the whole garden [0,5]

Example 2:

Input: n = 3, ranges = [0,0,0,0]
Output: -1
Explanation: Even if you activate all the four taps you cannot water the whole garden.


Constraints:

1 <= n <= 10^4
ranges.length == n + 1
0 <= ranges[i] <= 100

"""

# V0
# IDEA : GREEDY / JUMP GAME II (interval covering reduced to minimum jumps)
#
#   collapse the taps into one array :
#     reach[l] = the farthest right end among all taps whose interval starts at l
#   (clamp l at 0, everything left of the garden is irrelevant)
#
#   now it is exactly Jump Game II on positions 0..n-1 :
#     sweep i from 0, keep `far` = best right end seen among reach[0..i].
#     - far <= i          -> a gap at i that no tap covers -> -1
#     - i == frontier     -> the current tap's coverage ends here, so we must
#                            open one more tap and push the frontier to `far`
#
#   NOTE : the loop stops at n - 1, not n : reaching point n means the segment
#          [n-1, n] is watered, and point n itself has no width to cover.
#
# time = O(n), space = O(n)
class Solution(object):
    def minTaps(self, n, ranges):
        reach = [0] * (n + 1)
        for i, r in enumerate(ranges):
            left = max(0, i - r)
            reach[left] = max(reach[left], i + r)

        res, far, frontier = 0, 0, 0
        for i in range(n):
            far = max(far, reach[i])
            if far <= i:
                return -1
            if frontier == i:
                res += 1
                frontier = far
        return res
