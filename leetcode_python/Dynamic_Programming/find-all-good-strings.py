"""

1397. Find All Good Strings
Hard

Given the strings s1 and s2 of size n and the string evil, return the number of good strings.

A good string has size n, it is alphabetically greater than or equal to s1, it is alphabetically smaller than or equal to s2, and it does not contain the string evil as a substring. Since the answer can be a huge number, return this modulo 10^9 + 7.


Example 1:

Input: n = 2, s1 = "aa", s2 = "da", evil = "b"
Output: 51
Explanation: There are 25 good strings starting with 'a': "aa","ac","ad",...,"az". Then there are 25 good strings starting with 'c': "ca","cc","cd",...,"cz" and finally there is one good string starting with 'd': "da".

Example 2:

Input: n = 8, s1 = "leetcode", s2 = "leetgoes", evil = "leet"
Output: 0
Explanation: All strings greater than or equal to s1 and smaller than or equal to s2 start with the prefix "leet", therefore, there is not any good string.

Example 3:

Input: n = 2, s1 = "gx", s2 = "gz", evil = "x"
Output: 2


Constraints:

s1.length == n
s2.length == n
s1 <= s2
1 <= n <= 500
1 <= evil.length <= 50
All strings consist of lowercase English letters.

"""

# V0
# IDEA : DIGIT DP + KMP AUTOMATON (build the string left to right)
#
#   state = (position i, KMP-matched-length j, still hugging s1, still hugging s2)
#     - j is how many chars of evil the current suffix already matches;
#       j == len(evil) means evil appeared -> that branch contributes 0.
#     - lo=True means the prefix equals s1's prefix, so the next char has a
#       lower bound s1[i]; hi=True likewise gives the upper bound s2[i].
#   precompute the KMP transition table trans[j][c] once so each step is O(1).
#   NOTE : lo and hi must both be tracked -> a string can hug both bounds
#          at once when s1 and s2 share a prefix.
#
"""

DP def
    (DIGIT DP over the 26-letter alphabet + a KMP AUTOMATON for `evil`)

    dp(i, j, lo, hi): number of ways to fill positions i..n-1, given

        i  - position being chosen
        j  - how many chars of `evil` the current SUFFIX already matches
             (j == len(evil) means evil appeared -> that branch is 0)
        lo - the prefix still equals s1's prefix, so the next char is
             LOWER-bounded by s1[i]
        hi - the prefix still equals s2's prefix, so the next char is
             UPPER-bounded by s2[i]

DP eq

     dp(i, j, lo, hi) = sum over c in [lo_bound .. hi_bound] of

        dp( i+1, trans[j][c], lo and c == lo_bound, hi and c == hi_bound )

        skipping any c with trans[j][c] == len(evil)


    -> e.g. trans[j][c] is precomputed from evil's KMP failure function,
              so each step is O(1) instead of re-matching

     NOTE !!! lo AND hi must both be tracked - a string can hug BOTH bounds
              at once when s1 and s2 share a prefix

     base: i == n -> 1
     ans = dp(0, 0, True, True) % (10^9 + 7)

"""
# time = O(n * m * 26), space = O(n * m * 4), m = len(evil)
from functools import lru_cache
class Solution(object):
    def findGoodStrings(self, n, s1, s2, evil):
        MOD = 10 ** 9 + 7
        m = len(evil)

        # KMP failure function of evil
        lps = [0] * m
        j = 0
        for i in range(1, m):
            while j > 0 and evil[i] != evil[j]:
                j = lps[j - 1]
            if evil[i] == evil[j]:
                j += 1
            lps[i] = j

        # trans[j][c] = new matched length after appending char c
        trans = [[0] * 26 for _ in range(m)]
        for st in range(m):
            for c in range(26):
                ch = chr(ord('a') + c)
                k = st
                while k > 0 and evil[k] != ch:
                    k = lps[k - 1]
                if evil[k] == ch:
                    k += 1
                trans[st][c] = k

        @lru_cache(None)
        def dp(i, matched, lo, hi):
            if matched == m:
                return 0
            if i == n:
                return 1
            start = ord(s1[i]) - ord('a') if lo else 0
            end = ord(s2[i]) - ord('a') if hi else 25
            res = 0
            for c in range(start, end + 1):
                res += dp(
                    i + 1,
                    trans[matched][c],
                    lo and c == start,
                    hi and c == end,
                )
            return res % MOD

        res = dp(0, 0, True, True)
        dp.cache_clear()
        return res
