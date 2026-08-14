"""

2350. Shortest Impossible Sequence of Rolls
Hard

You are given an integer array rolls of length n and an integer k. You roll a k sided dice numbered from 1 to k, n times, where the result of the ith roll is rolls[i].

Return the length of the shortest sequence of rolls so that there's no such subsequence in rolls.

A sequence of rolls of length len is the result of rolling a k sided dice len times.


Example 1:

Input: rolls = [4,2,1,2,3,3,2,4,1], k = 4
Output: 3
Explanation: Every sequence of rolls of length 1, [1], [2], [3], [4], can be taken from rolls.
Every sequence of rolls of length 2, [1, 1], [1, 2], ..., [4, 4], can be taken from rolls.
The sequence [1, 4, 2] cannot be taken from rolls, so we return 3.
Note that there are other sequences that cannot be taken from rolls.

Example 2:

Input: rolls = [1,1,2,2], k = 2
Output: 2
Explanation: Every sequence of rolls of length 1, [1], [2], can be taken from rolls.
The sequence [2, 1] cannot be taken from rolls, so we return 2.
Note that there are other sequences that cannot be taken from rolls but [2, 1] is the shortest.

Example 3:

Input: rolls = [1,1,3,2,2,2,3,3], k = 4
Output: 1
Explanation: The sequence [4] cannot be taken from rolls, so we return 1.
Note that there are other sequences that cannot be taken from rolls but [4] is the shortest.


Constraints:

n == rolls.length
1 <= n <= 10^5
1 <= rolls[i] <= k <= 10^5

"""

# V0
# IDEA : GREEDY SEGMENT SPLIT (count complete "all k faces" blocks)
#
#   Scan left to right and cut a new segment every time the current segment has
#   seen all k distinct faces. Say we get m complete segments.
#
#   - Every sequence of length m IS a subsequence: greedily match the i-th
#     wanted face inside the i-th complete segment (it contains all k faces).
#   - No sequence of length m+1 can always be matched: after consuming the m
#     complete segments the leftover tail is missing some face f, so pick f as
#     the (m+1)-th element and matching fails.
#
#   So the answer is m + 1.
#
# time = O(n), space = O(k)
class Solution(object):
    def shortestSequence(self, rolls, k):
        res = 1
        seen = set()
        for v in rolls:
            seen.add(v)
            if len(seen) == k:
                res += 1
                seen = set()
        return res
