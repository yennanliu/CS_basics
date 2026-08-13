"""

1386. Cinema Seat Allocation
Medium

A cinema has n rows of seats, numbered from 1 to n. Each row has 10 seats,
numbered from 1 to 10.

You are given a 2D integer array reservedSeats, where
reservedSeats[i] = [rowi, seati] means that seat seati in row rowi is already
reserved.

A four-person group must be assigned to four seats in the same row. The group
can be seated in one of the following seat blocks:

seats 2, 3, 4, 5
seats 4, 5, 6, 7
seats 6, 7, 8, 9

A block can be used only if none of its seats are reserved. Each seat can be
assigned to at most one group.

Return an integer denoting the maximum number of four-person groups that can be
assigned.


Example 1:

Input: n = 3, reservedSeats = [[1,2],[1,3],[1,8],[2,6],[3,1],[3,10]]
Output: 4

Example 2:

Input: n = 2, reservedSeats = [[2,1],[1,8],[2,6]]
Output: 2

Example 3:

Input: n = 4, reservedSeats = [[4,3],[1,4],[4,6],[1,7]]
Output: 4


Constraints:

1 <= n <= 10^9
1 <= reservedSeats.length <= min(10 * n, 10^4)
reservedSeats[i] == [rowi, seati]
1 <= rowi <= n
1 <= seati <= 10
All reservedSeats[i] are distinct.

"""

# V0
# IDEA: hash map (row -> bitmask of reserved seats) + greedy
#
#  - seats 1 and 10 never belong to any block, so we can ignore them.
#  - a row with NO reservation always fits exactly 2 groups (2345 + 6789),
#    so untouched rows contribute (n - len(rows_seen)) * 2.
#  - for a touched row try the blocks in the order  2345 -> 6789 -> 4567.
#    trying the two disjoint outer blocks FIRST is what makes the greedy
#    correct: the middle block 4567 overlaps both outer blocks, so it is only
#    worth taking when neither outer block was available.
#
# time = O(m), m = len(reservedSeats)
# space = O(m)
from collections import defaultdict
class Solution(object):
    def maxNumberOfFamilies(self, n, reservedSeats):
        # bit (10 - seat) is set when `seat` is reserved -> seat 1 = bit 9 ... seat 10 = bit 0
        rows = defaultdict(int)
        for r, s in reservedSeats:
            rows[r] |= 1 << (10 - s)

        BLOCK_2345 = 0b0111100000
        BLOCK_6789 = 0b0000011110
        BLOCK_4567 = 0b0001111000
        masks = (BLOCK_2345, BLOCK_6789, BLOCK_4567)

        res = (n - len(rows)) * 2
        for taken in rows.values():
            for mask in masks:
                if taken & mask == 0:
                    taken |= mask   # occupy the block so it can't be reused
                    res += 1
        return res
