"""

354. Russian Doll Envelopes
Hard

You are given a 2D array of integers envelopes where envelopes[i] = [wi, hi] represents
the width and the height of an envelope.

One envelope can fit into another if and only if both the width and height of one envelope
are greater than the other envelope's width and height.

Return the maximum number of envelopes you can Russian doll (i.e., put one inside the other).

Note: You cannot rotate an envelope.


Example 1:

Input: envelopes = [[5,4],[6,4],[6,7],[2,3]]
Output: 3
Explanation: The maximum number of envelopes you can Russian doll is 3
([2,3] => [5,4] => [6,7]).

Example 2:

Input: envelopes = [[1,1],[1,1],[1,1]]
Output: 1


Constraints:

1 <= envelopes.length <= 10^5
envelopes[i].length == 2
1 <= wi, hi <= 10^5

"""

# V0
# IDEA : SORT (w ASC, h DESC) then LIS on heights via BINARY SEARCH
#
#  Sorting by width ascending reduces the 2D problem to a 1D LIS on heights.
#
#  KEY TRICK: for EQUAL widths, sort the heights DESCENDING. That way two envelopes
#  with the same width can never both be picked by the (strictly) increasing
#  subsequence - a descending run contains no increasing pair.
#
#  Then run the classic patience-sorting LIS with bisect_left (strictly increasing).
#
# time  = O(n log n)
# space = O(n)
from bisect import bisect_left
class Solution(object):
    def maxEnvelopes(self, envelopes):
        if not envelopes:
            return 0

        envelopes.sort(key=lambda e: (e[0], -e[1]))

        tails = []  # tails[i] = smallest possible tail of an increasing subseq of length i+1
        for _, h in envelopes:
            # bisect_left -> STRICTLY increasing (equal heights replace, not append)
            i = bisect_left(tails, h)
            if i == len(tails):
                tails.append(h)
            else:
                tails[i] = h

        return len(tails)
