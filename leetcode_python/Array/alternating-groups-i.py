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
