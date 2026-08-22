"""

1503. Last Moment Before All Ants Fall Out of a Plank
Medium

We have a wooden plank of the length n units. Some ants are walking on the plank,
each ant moves with a speed of 1 unit per second. Some of the ants move to the left,
the other move to the right.

When two ants moving in two different directions meet at some point,
they change their directions and continue moving again.
Assume changing directions does not take any additional time.

When an ant reaches one end of the plank at a time t, it falls out of the plank immediately.

Given an integer n and two integer arrays left and right,
the positions of the ants moving to the left and the right,
return the moment when the last ant(s) fall out of the plank.


Example 1:

Input: n = 4, left = [4,3], right = [0,1]
Output: 4
Explanation:
-The ant at index 0 is named A and going to the right.
-The ant at index 1 is named B and going to the right.
-The ant at index 3 is named C and going to the left.
-The ant at index 4 is named D and going to the left.
The last moment when an ant was on the plank is t = 4 seconds.
After that, it falls immediately out of the plank.

Example 2:

Input: n = 7, left = [], right = [0,1,2,3,4,5,6,7]
Output: 7
Explanation: All ants are going to the right, the ant at index 0 needs 7 seconds to fall.

Example 3:

Input: n = 7, left = [0,1,2,3,4,5,6,7], right = []
Output: 7
Explanation: All ants are going to the left, the ant at index 7 needs 7 seconds to fall.


Constraints:

1 <= n <= 10^4
0 <= left.length <= n + 1
0 <= left[i] <= n
0 <= right.length <= n + 1
0 <= right[i] <= n
1 <= left.length + right.length <= n + 1
All values of left and right are unique, and each value can appear only in one of the two arrays.

"""

# V0
# IDEA : BRAIN TEASER
#
#   two ants bouncing off each other is INDISTINGUISHABLE from
#   the two ants simply passing through each other (they are identical,
#   only the multiset of positions matters).
#
#   -> so every ant just walks straight to its own end,
#      and the answer is the largest single walking distance:
#        * an ant going left  at x needs x       seconds
#        * an ant going right at x needs (n - x) seconds
#
#   NOTE : left / right can be empty, so start the max at 0
# time = O(n)
# space = O(1)
class Solution(object):
    def getLastMoment(self, n, left, right):
        res = 0
        for x in left:
            res = max(res, x)
        for x in right:
            res = max(res, n - x)
        return res


# V0-1
# IDEA : HONEST SIMULATION, HALF-SECOND TICKS ON DOUBLED COORDINATES
#         (no "ants pass through each other" insight used at all)
#
#   collisions can happen at half-integer points, so double every coordinate:
#   a half second then becomes an integer step of 1, every meeting lands
#   exactly on a lattice point, and no pair can slip past one another.
#   all ants share the parity of the tick counter, so gaps stay even and the
#   plank ends (0 and 2n) are only ever reached on even ticks.
#
#   per tick : move everyone, flip the two ants sitting on a shared point,
#   drop whoever reached an end and remember when that happened.
#
# time = O(n * k), k = len(left) + len(right)
# space = O(k)
from collections import defaultdict
class Solution(object):
    def getLastMoment(self, n, left, right):
        end = 2 * n
        alive = []
        res = 0
        for x in left:
            if x == 0:
                continue          # already at the left end -> falls at t = 0
            alive.append([2 * x, -1])
        for x in right:
            if x == n:
                continue          # already at the right end -> falls at t = 0
            alive.append([2 * x, 1])

        tick = 0
        while alive:
            tick += 1
            spot = defaultdict(list)
            for a in alive:
                a[0] += a[1]
                spot[a[0]].append(a)
            for same in spot.values():
                if len(same) > 1:
                    for a in same:
                        a[1] = -a[1]          # head-on bounce
            keep = []
            for a in alive:
                if (a[0] == 0 and a[1] == -1) or (a[0] == end and a[1] == 1):
                    res = tick // 2           # ticks are half seconds
                else:
                    keep.append(a)
            alive = keep
        return res


# V0-2
# IDEA : SORT - ONLY ONE ANT PER DIRECTION CAN POSSIBLY BE THE LAST
#
#   since (by V0's argument) each ant just walks straight to its own end,
#   the walking distances are monotone in the position :
#     * among the left-goers the RIGHT-most one walks furthest  -> max(left)
#     * among the right-goers the LEFT-most one walks furthest  -> n - min(right)
#   so sorting each list and reading a single endpoint is enough - no scan
#   over all the distances.
#
# time = O(k log k), k = len(left) + len(right)
# space = O(k) for the sorted copies
class Solution(object):
    def getLastMoment(self, n, left, right):
        l = sorted(left)
        r = sorted(right)
        res = 0
        if l:
            res = max(res, l[-1])
        if r:
            res = max(res, n - r[0])
        return res
