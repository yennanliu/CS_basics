"""

1642. Furthest Building You Can Reach
Medium

You are given an integer array heights representing the heights of buildings, some
bricks, and some ladders.

You start your journey from building 0 and move to the next building by possibly
using bricks or ladders.

While moving from building i to building i+1 (0-indexed),

If the current building's height is greater than or equal to the next building's
height, you do not need a ladder or bricks.
If the current building's height is less than the next building's height, you can
either use one ladder or (h[i+1] - h[i]) bricks.

Return the furthest building index (0-indexed) you can reach if you use the given
ladders and bricks optimally.


Example 1:

Input: heights = [4,2,7,6,9,14,12], bricks = 5, ladders = 1
Output: 4
Explanation: Starting at building 0, you can follow these steps:
- Go to building 1 without using ladders nor bricks since 4 >= 2.
- Go to building 2 using 5 bricks. You must use either bricks or ladders because
  2 < 7.
- Go to building 3 without using ladders nor bricks since 7 >= 6.
- Go to building 4 using your only ladder. You must use either bricks or ladders
  because 6 < 9.
It is impossible to go beyond building 4 because you do not have any more bricks or
ladders.

Example 2:

Input: heights = [4,12,2,7,3,18,20,3,19], bricks = 10, ladders = 2
Output: 7

Example 3:

Input: heights = [14,3,19,3], bricks = 17, ladders = 0
Output: 3


Constraints:

1 <= heights.length <= 10^5
1 <= heights[i] <= 10^6
0 <= bricks <= 10^9
0 <= ladders <= heights.length

"""

# V0
# IDEA : MIN-HEAP OF THE CLIMBS (a ladder is worth its climb, so save them for
#        the BIGGEST ones -- but you only know which those are later)
#
#   greedy forward is impossible : spending the ladder on the first big climb
#   may be wrong if a bigger one comes later. so instead : hand a ladder to
#   EVERY climb as it appears, and when we have handed out more ladders than we
#   own, take the ladder back off the smallest climb in the heap and pay bricks
#   for that one instead.
#
#   the heap therefore always holds the `ladders` largest climbs seen so far,
#   which is exactly the optimal assignment for the prefix walked.
#
#     [4,2,7,6,9,...], bricks=5, ladders=1
#       climb 2->7 (5) : heap [5]        (1 ladder used, ok)
#       climb 6->9 (3) : heap [3,5] -> too many -> pop 3, bricks 5-3 = 2
#       next climb 9->14 (5) : heap [5,5] -> pop 5, bricks 2-5 < 0 -> stop at 4
#
#   NOTE !!! return i (not i + 1) when the bricks run out : we failed to cross
#            from i to i + 1, so i is the furthest building actually reached.
#
# time = O(nlogn), space = O(n)
import heapq


class Solution(object):
    def furthestBuilding(self, heights, bricks, ladders):
        """
        :type heights: List[int]
        :type bricks: int
        :type ladders: int
        :rtype: int
        """
        # edge
        if not heights:
            return 0

        used = []
        for i in range(len(heights) - 1):
            d = heights[i + 1] - heights[i]
            # going down or flat is free
            if d <= 0:
                continue

            heapq.heappush(used, d)
            if len(used) > ladders:
                # the smallest climb is the cheapest one to pay bricks for
                bricks -= heapq.heappop(used)
                if bricks < 0:
                    return i

        return len(heights) - 1
