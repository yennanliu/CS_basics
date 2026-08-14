"""

1496. Path Crossing
Easy

Given a string path, where path[i] = 'N', 'S', 'E' or 'W', each representing moving one unit north, south, east, or west, respectively. You start at the origin (0, 0) on a 2D plane and walk on the path specified by path.

Return true if the path crosses itself at any point, that is, if at any time you are on a location you have previously visited. Return false otherwise.


Example 1:

Input: path = "NES"
Output: false
Explanation: Notice that the path doesn't cross any point more than once.

Example 2:

Input: path = "NESWW"
Output: true
Explanation: Notice that the path visits the origin twice.


Constraints:

1 <= path.length <= 10^4
path[i] is either 'N', 'S', 'E', or 'W'.

"""

# V0
# IDEA : HASH SET (record every visited coordinate)
#
#   walk the path step by step, keeping the current (x, y).
#   NOTE : seed the set with the starting point (0, 0) — coming back
#          to the origin also counts as a crossing.
#   the moment a new coordinate is already in the set -> crossing.
#
# time = O(n), space = O(n)
class Solution(object):
    def isPathCrossing(self, path):
        moves = {
            'N': (0, 1),
            'S': (0, -1),
            'E': (1, 0),
            'W': (-1, 0),
        }
        x, y = 0, 0
        seen = set([(0, 0)])
        for c in path:
            dx, dy = moves[c]
            x += dx
            y += dy
            if (x, y) in seen:
                return True
            seen.add((x, y))
        return False
