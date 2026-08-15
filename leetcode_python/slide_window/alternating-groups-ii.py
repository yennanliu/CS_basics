"""

3208. Alternating Groups II
Medium

There is a circle of red and blue tiles. You are given an array of integers colors and an integer k. The color of tile i is represented by colors[i]:

colors[i] == 0 means that tile i is red.
colors[i] == 1 means that tile i is blue.

An alternating group is every k contiguous tiles in the circle with alternating colors (each tile in the group except the first and last one has a different color from its left and right tiles).

Return the number of alternating groups.

Note that since colors represents a circle, the first and the last tiles are considered to be next to each other.


Example 1:

Input: colors = [0,1,0,1,0], k = 3
Output: 3

Example 2:

Input: colors = [0,1,0,0,1,0,1], k = 6
Output: 2

Example 3:

Input: colors = [1,1,0,1], k = 4
Output: 0


Constraints:

3 <= colors.length <= 10^5
0 <= colors[i] <= 1
3 <= k <= colors.length

"""

# V0
# IDEA : TRACK THE CURRENT ALTERNATING RUN LENGTH AROUND THE CIRCLE
#
#   a window of k tiles is a group exactly when every adjacent pair inside it
#   differs. so walk the circle keeping `run` = how many tiles the current
#   alternating stretch covers, resetting to 1 whenever two neighbours match.
#
#   every position where run reaches k closes one group, so the count is the
#   number of such positions.
#
#   the wrap-around is handled by walking n + k - 1 steps (indices taken
#   modulo n), which lets a run that starts near the end finish at the front
#   without double counting.
#
# time = O(n + k), space = O(1)
class Solution(object):
    def numberOfAlternatingGroups(self, colors, k):
        n = len(colors)
        res = 0
        run = 1
        for t in range(1, n + k - 1):
            i = t % n
            prev = (t - 1) % n
            run = run + 1 if colors[i] != colors[prev] else 1
            if run >= k:
                res += 1
        return res
