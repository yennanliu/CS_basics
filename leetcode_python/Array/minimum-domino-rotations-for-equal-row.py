"""

1007. Minimum Domino Rotations For Equal Row
Medium

In a row of dominoes, tops[i] and bottoms[i] represent the top and bottom
halves of the ith domino. (A domino is a tile with two numbers from 1 to 6 -
one on each half of the tile.)

We may rotate the ith domino, so that tops[i] and bottoms[i] swap values.

Return the minimum number of rotations so that all the values in tops are the
same, or all the values in bottoms are the same.

If it cannot be done, return -1.

Example 1:

Input: tops = [2,1,2,4,2,2], bottoms = [5,2,6,2,3,2]

Output: 2

Explanation:

The first figure represents the dominoes as given by tops and bottoms: before
we do any rotations. If we rotate the second and fourth dominoes, we can make
every value in the top row equal to 2, as indicated by the second figure.

Example 2:

Input: tops = [3,5,1,2,3], bottoms = [3,6,3,3,4]

Output: -1

Explanation:

In this case, it is not possible to rotate the dominoes to make one row of
values equal.

Constraints:

2 <= tops.length <= 2 * 10^4
bottoms.length == tops.length
1 <= tops[i], bottoms[i] <= 6

"""

# V0
# IDEA : GREEDY - ONLY tops[0] OR bottoms[0] CAN BE THE TARGET
#
#   Whatever value ends up filling a row must appear on EVERY domino,
#   including the first one -> the target is tops[0] or bottoms[0], nothing else.
#
#   For a candidate target, walk the dominoes once:
#     - a domino with the target on neither half -> candidate impossible
#     - target missing from the top    -> one rotation to fix the TOP row
#     - target missing from the bottom -> one rotation to fix the BOTTOM row
#   A domino with the target on both halves costs nothing for either row.
#
#   e.g. tops = [2,1,2,4,2,2], bottoms = [5,2,6,2,3,2], target = 2
#        top misses 2 at i=1,3 -> 2 rotations; bottom misses at i=0,2,4 -> 3
#        -> min(2, 3) = 2
#
# time = O(n), space = O(1)
class Solution(object):
    def minDominoRotations(self, tops, bottoms):
        """
        :type tops: List[int]
        :type bottoms: List[int]
        :rtype: int
        """
        best = self._rotations_for(tops[0], tops, bottoms)
        # NOTE !!! only try bottoms[0] if it is a different value;
        #          if tops[0] already worked, its answer is just as good
        if bottoms[0] != tops[0]:
            other = self._rotations_for(bottoms[0], tops, bottoms)
            if best == -1 or (other != -1 and other < best):
                best = other
        return best

    def _rotations_for(self, target, tops, bottoms):
        """Min rotations to make one row all `target`, or -1 if impossible."""
        rotate_to_top = 0      # rotations needed so tops is all target
        rotate_to_bottom = 0   # rotations needed so bottoms is all target

        for i in range(len(tops)):
            # this domino cannot show target on either side
            if tops[i] != target and bottoms[i] != target:
                return -1
            if tops[i] != target:
                rotate_to_top += 1
            if bottoms[i] != target:
                rotate_to_bottom += 1

        return min(rotate_to_top, rotate_to_bottom)


# V1
# IDEA : COUNT EACH FACE VALUE (1..6)
#
#   For every value v, count:
#     in_top[v]    = dominoes with v on top
#     in_bottom[v] = dominoes with v on bottom
#     same[v]      = dominoes with v on BOTH halves
#   v can fill a row exactly when every domino carries it:
#     in_top[v] + in_bottom[v] - same[v] == n
#   Then filling the top row costs n - in_top[v] rotations,
#   and filling the bottom row costs n - in_bottom[v].
#
#   Tries all 6 values instead of 2 candidates - same O(n), one pass.
#
# time = O(n), space = O(1)
class Solution2(object):
    def minDominoRotations(self, tops, bottoms):
        """
        :type tops: List[int]
        :type bottoms: List[int]
        :rtype: int
        """
        FACES = 7  # values are 1..6; index 0 unused
        n = len(tops)
        in_top = [0] * FACES
        in_bottom = [0] * FACES
        same = [0] * FACES

        for i in range(n):
            in_top[tops[i]] += 1
            in_bottom[bottoms[i]] += 1
            if tops[i] == bottoms[i]:
                same[tops[i]] += 1

        best = -1
        for v in range(1, FACES):
            # v must appear on every domino (a both-sides domino counted once)
            if in_top[v] + in_bottom[v] - same[v] == n:
                cost = min(n - in_top[v], n - in_bottom[v])
                if best == -1 or cost < best:
                    best = cost

        return best
