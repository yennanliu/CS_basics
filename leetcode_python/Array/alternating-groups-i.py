"""

3206. Alternating Groups I
Easy

There is a circle of red and blue tiles. You are given an array of integers colors. The color of tile i is represented by colors[i]:

colors[i] == 0 means that tile i is red.
colors[i] == 1 means that tile i is blue.

Every 3 contiguous tiles in the circle with alternating colors (the middle tile has a different color from its left and right tiles) is called an alternating group.

Return the number of alternating groups.

Note that since colors represents a circle, the first and the last tiles are considered to be next to each other.


Example 1:

Input: colors = [1,1,1]
Output: 0

Example 2:

Input: colors = [0,1,0,0,1]
Output: 3


Constraints:

3 <= colors.length <= 100
0 <= colors[i] <= 1


"""

# V0
# IDEA : CHECK EVERY WINDOW OF THREE, WRAPPING WITH MODULO
#
#   a window centred at i alternates exactly when its middle differs from
#   BOTH neighbours, i.e. colors[i-1] != colors[i] != colors[i+1]. the circle
#   is handled by reading the indices modulo n, so every tile gets to be a
#   centre.
#
# time = O(n), space = O(1)
class Solution(object):
    def numberOfAlternatingGroups(self, colors):
        n = len(colors)
        return sum(1
                   for i in range(n)
                   if colors[(i - 1) % n] != colors[i] != colors[(i + 1) % n])


# V0-1
# IDEA : EDGE ARRAY — A GROUP IS TWO ADJACENT "DIFFERENT" EDGES
#
#   label every circular edge i -> i+1 with whether its two tiles differ. the
#   window centred on tile i alternates exactly when the edge entering i and
#   the edge leaving i are BOTH labelled "different", so the answer is the
#   number of adjacent True pairs in that (circular) edge array.
#
# time = O(n)
# space = O(n) for the edge array
class Solution(object):
    def numberOfAlternatingGroups(self, colors):
        n = len(colors)
        diff = [colors[i] != colors[(i + 1) % n] for i in range(n)]
        # edge i-1 enters tile i, edge i leaves it
        return sum(1 for i in range(n) if diff[(i - 1) % n] and diff[i])


# V0-2
# IDEA : RUNNING ALTERNATING STREAK OVER THE UNROLLED CIRCLE
#
#   walk k-1 steps past the end (indices taken mod n) while tracking the
#   length of the current run of alternating tiles: a same-colour step resets
#   the streak to 1, a different-colour step extends it. every step that
#   pushes the streak to k = 3 closes off one more group.
#
#   NOTE : this is the shape that generalises to "alternating groups II",
#          where k is given instead of fixed at 3.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def numberOfAlternatingGroups(self, colors):
        n = len(colors)
        k = 3
        streak = 1
        res = 0
        for i in range(1, n + k - 1):
            if colors[i % n] == colors[(i - 1) % n]:
                streak = 1
            else:
                streak += 1
                if streak >= k:
                    res += 1
        return res
