"""

1552. Magnetic Force Between Two Balls
Medium

In the universe Earth C-137, Rick discovered a special form of magnetic force
between two balls if they are put in his new invented basket. Rick has n empty
baskets, the ith basket is at position[i], Morty has m balls and needs to
distribute the balls into the baskets such that the minimum magnetic force
between any two balls is maximum.

Rick stated that magnetic force between two different balls at positions x and
y is |x - y|.

Given the integer array position and the integer m. Return the required force.

Example 1:

Input: position = [1,2,3,4,7], m = 3

Output: 3

Explanation:

Distributing the 3 balls into baskets 1, 4 and 7 will make the magnetic force
between ball pairs [3, 3, 6]. The minimum magnetic force is 3. We cannot
achieve a larger minimum magnetic force than 3.

Example 2:

Input: position = [5,4,3,2,1,1000000000], m = 2

Output: 999999999

Explanation:

We can use baskets 1 and 1000000000.

Constraints:

n == position.length
2 <= n <= 10^5
1 <= position[i] <= 10^9
All integers in position are distinct.
2 <= m <= position.length

"""

# V0
# IDEA : SORT + BINARY SEARCH ON ANSWER + GREEDY CHECK (LC 410, LC 1231)
#
#   "max of the min gap" -> binary search the GAP, not an index.
#   The check is monotonic: if m balls fit with every gap >= d, they also fit
#   with every gap >= d - 1. So the answers form  [yes, yes, ..., yes, no, no],
#   and we want the LAST yes.
#
#   Greedy check for a given gap d: put the first ball in the leftmost basket,
#   then each next ball in the first basket >= last + d. Placing as early as
#   possible never hurts, because it leaves the most room for the balls after.
#
#   e.g. position = [1,2,3,4,7], m = 3
#        d = 3 -> balls at 1, 4, 7 -> 3 balls -> ok
#        d = 4 -> balls at 1, 7    -> 2 balls -> not ok   => answer 3
#
#   (brute force: try every d from 1 to max - min -> O(n * range), TLE)
#
# time = O(n log n + n log(max - min)), space = O(n)
class Solution(object):
    def maxDistance(self, position, m):
        """
        :type position: List[int]
        :type m: int
        :rtype: int
        """
        # NOTE !!! sort first: the greedy check walks baskets left to right.
        #          sorted() makes a copy, so the input is not mutated
        baskets = sorted(position)

        # smallest possible min gap is 1 (positions are distinct);
        # the largest is reached with only 2 balls at both ends
        left = 1
        right = baskets[-1] - baskets[0]
        best = 0

        while left <= right:
            mid = left + (right - left) // 2

            if self.can_place(baskets, m, mid):
                # gap mid works -> record it, and try a larger one
                best = mid
                left = mid + 1
            else:
                # gap mid is too large -> shrink it
                right = mid - 1

        return best

    def can_place(self, baskets, m, min_gap):
        """Return True if m balls fit in sorted baskets with every gap >= min_gap."""
        # first ball always goes in the leftmost basket
        placed = 1
        last_pos = baskets[0]

        for pos in baskets[1:]:
            # place the next ball at the earliest basket far enough away
            if pos - last_pos >= min_gap:
                placed += 1
                last_pos = pos
                # stop early: enough balls already placed
                if placed >= m:
                    return True

        return placed >= m
