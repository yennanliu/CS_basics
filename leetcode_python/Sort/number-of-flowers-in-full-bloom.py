"""

2251. Number of Flowers in Full Bloom
Hard

You are given a 0-indexed 2D integer array flowers, where flowers[i] = [start_i, end_i] means the ith flower will be in full bloom from start_i to end_i (inclusive). You are also given a 0-indexed integer array people of size n, where people[i] is the time that the ith person will arrive to see the flowers.

Return an integer array answer of size n, where answer[i] is the number of flowers that are in full bloom when the ith person arrives.


Example 1:

Input: flowers = [[1,6],[3,7],[9,12],[4,13]], people = [2,3,7,11]
Output: [1,2,2,2]
Explanation: The figure above shows the times when the flowers are in full bloom and when the people arrive.
For each person, we return the number of flowers in full bloom during their arrival.

Example 2:

Input: flowers = [[1,10],[3,3]], people = [3,3,2]
Output: [2,2,1]
Explanation: The figure above shows the times when the flowers are in full bloom and when the people arrive.
For each person, we return the number of flowers in full bloom during their arrival.


Constraints:

1 <= flowers.length <= 5 * 10^4
flowers[i].length == 2
1 <= start_i <= end_i <= 10^9
1 <= people.length <= 5 * 10^4
1 <= people[i] <= 10^9

"""

# V0
# IDEA : INCLUSION-EXCLUSION ON TWO SORTED ARRAYS OF ENDPOINTS
#
#   at time t, the number of blooming flowers is
#       (how many have already STARTED)  -  (how many have already ENDED)
#
#   sort the starts and the ends independently — the pairing between them is
#   irrelevant for a counting question — then per person :
#       started = bisect_right(starts, t)     # start <= t
#       ended   = bisect_left(ends, t)        # end   <  t   (inclusive bloom)
#
#   the bisect SIDES carry the inclusivity : `right` on starts counts a
#   flower opening exactly at t, `left` on ends does NOT retire a flower
#   closing exactly at t.
#
# time = O((F + P) log F), space = O(F)
import bisect


class Solution(object):
    def fullBloomFlowers(self, flowers, people):
        starts = sorted(f[0] for f in flowers)
        ends = sorted(f[1] for f in flowers)
        return [bisect.bisect_right(starts, t) - bisect.bisect_left(ends, t)
                for t in people]
