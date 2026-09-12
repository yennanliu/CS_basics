"""

4043. Count Rotations With Exactly K Equal Adjacent Pairs
Easy

You are given a string s of length n and an integer k.

A cyclic rotation of s is formed by choosing a prefix of s within [0, n - 1],
moving that prefix to the end, and preserving the order of all characters.

The score of a rotation is the number of indices i such that 0 <= i < n - 1
and the characters at positions i and i + 1 are equal.

Return the number of cyclic rotations of s whose score equals k.


Example 1:

Input: s = "aab", k = 1

Output: 2

Explanation:

The three rotations are "aab" (score 1), "aba" (score 0) and "baa" (score 1),
so two of them score 1.


Example 2:

Input: s = "ab", k = 0

Output: 2

Explanation:

Both "ab" and "ba" score 0.


Constraints:

1 <= s.length <= 10^5

0 <= k < s.length

s consists of lowercase English letters

"""

# V0
# IDEA : BRUTE FORCE + {SCORE : CNT} COUNTER
#
#   build every rotation by moving one leading char to the end, score it, and
#   tally the scores; the answer is the tally for k.
#
#   NOTE !!! there are n ROTATIONS, not n distinct strings -- "aa" rotates to
#            "aa" twice and BOTH count, so score each of the n rotations and
#            never de-duplicate the strings.
#
#   e.g. s = "aab" -> "aab"(1), "aba"(0), "baa"(1) -> {1: 2, 0: 1} -> k=1 -> 2
#
# time = O(n^2), space = O(n)
from collections import defaultdict


class Solution(object):
    def countRotations(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        # edge
        if not s:
            return 0

        # {score: cnt}
        c_map = defaultdict(int)

        for i in range(len(s)):
            if i > 0:
                # rotate: move the leading char to the end
                tmp = s[0]
                s = s[1:] + tmp
            c_map[self.get_score(s)] += 1

        return c_map[k]

    def get_score(self, x):
        return sum(1 for i in range(len(x) - 1) if x[i] == x[i + 1])


# V1
# IDEA : COUNT THE CYCLIC PAIRS ONCE -- A ROTATION ONLY DROPS ONE OF THEM
#
#   lay s out on a circle: there are exactly n cyclic pairs,
#   (s[j], s[(j+1) % n]) for j in [0, n), and let C be how many of them match.
#
#   rotating by t gives s[t:] + s[:t], whose n-1 LINEAR pairs are the cyclic
#   pairs j = t, t+1, ..., t+n-2 -- i.e. every cyclic pair EXCEPT j = t-1,
#   which is the one that got cut open to make the two ends. so
#
#      score(t) = C - (1 if cyclic pair t-1 matches else 0)
#
#   -> a rotation scores only C or C-1, never anything else, and which one it
#      is depends solely on the single pair it cut. so just count the cuts:
#
#         k == C      -> cut a NON-matching pair -> n - C rotations
#         k == C - 1  -> cut a MATCHING pair     -> C rotations
#         otherwise   -> 0
#
#   e.g. s = "aab", cyclic pairs (a,a)Y (a,b)N (b,a)N -> C = 1
#        k=1 -> cut a non-match -> 3 - 1 = 2 rotations
#
# time = O(n), space = O(1)
class Solution2(object):
    def countRotations(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        # edge
        if not s:
            return 0

        n = len(s)

        # NOTE !!! the pair (s[n-1], s[0]) is included -- the circle has n
        #          pairs, while any one rotation only ever sees n-1 of them
        c = sum(1 for i in range(n) if s[i] == s[(i + 1) % n])

        if k == c:
            return n - c
        if k == c - 1:
            return c
        return 0
