"""

2745. Construct the Longest New String
Medium

You are given three integers x, y, and z.

You have x strings equal to "AA", y strings equal to "BB", and z strings equal to "AB". You want to choose some (possibly all or none) of these strings and concatenate them in some order to form a new string. This new string must not contain "AAA" or "BBB" as a substring.

Return the maximum possible length of the new string.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: x = 2, y = 5, z = 1
Output: 12
Explanation: We can concatenate the strings "BB", "AA", "BB", "AA", "BB", and "AB" in that order. Then, our new string is "BBAABBAABBAB".
That string has length 12, and we can show that it is impossible to construct a string of longer length.

Example 2:

Input: x = 3, y = 2, z = 2
Output: 14
Explanation: We can concatenate the strings "AB", "AB", "AA", "BB", "AA", "BB", and "AA" in that order. Then, our new string is "ABABAABBAABBAA".
That string has length 14, and we can show that it is impossible to construct a string of longer length.


Constraints:

1 <= x, y, z <= 50

"""

# V0
# IDEA : GREEDY / CASE ANALYSIS ON THE "AA" vs "BB" ALTERNATION
#
#   Think of each block by the letter it starts and ends with:
#       "AA" -> A..A, "BB" -> B..B, "AB" -> A..B
#   Concatenating two blocks that touch with the same letter on both sides
#   makes 3+ of that letter in a row ("AAA" / "BBB"). So in the final string
#   the "AA" and "BB" blocks MUST strictly alternate: AA BB AA BB ...
#
#   Consequences :
#     - if x == y  : use all of them, "AABBAABB..." works.
#     - if x != y  : the alternation caps the bigger pile at min(x, y) + 1
#                    (start and end with the majority letter), so we place
#                    min(x, y) of each plus one extra of the majority.
#
#   Every "AB" block is A..B, so a run of "AB"s ("ABABAB...") is always safe,
#   and it can be prefixed to a chain that STARTS with A (because the run
#   ends in B) — that is, all z of them are always usable, for free.
#
#   NOTE : the whole layout ends up as  (AB)*z + [AA BB AA ...]  when the
#          chain starts with "AA", or  [BB AA ...] + (AB)*z  when it starts
#          with "BB". Either way no "AB" is ever wasted.
#
#   NOTE : each block contributes 2 characters, hence the final "* 2".
#
# time = O(1), space = O(1)
class Solution(object):
    def longestString(self, x, y, z):
        if x == y:
            blocks = x + y + z
        else:
            blocks = 2 * min(x, y) + 1 + z
        return blocks * 2


# V0-1
# IDEA : TOP-DOWN DFS + MEMOISATION ON (x, y, z, LAST LETTER WRITTEN)
#
#   every block is 2 chars with a fixed (first, last) letter :
#       "AA" -> (A, A),  "BB" -> (B, B),  "AB" -> (A, B)
#   appending a block whose FIRST letter equals the previous block's LAST
#   letter always makes 3 of that letter in a row ("AA" + "AB" -> "AAAB",
#   "AB" + "BB" -> "ABBB"), so the ONLY rule is next.first != prev.last.
#   that makes (remaining x, remaining y, remaining z, last letter) a complete
#   state — explore all legal next blocks and memoise.
#
#   NOTE : last = '' is the empty-string state, where every block is legal.
#   NOTE : no formula/case analysis is needed here; the search re-derives the
#          greedy answer of V0 by itself.
#
# time = O(x * y * z)   (constant work per state, 3 branches)
# space = O(x * y * z)
from functools import lru_cache
class Solution(object):
    def longestString(self, x, y, z):
        @lru_cache(maxsize=None)
        def dfs(a, b, c, last):
            best = 0
            if a and last != 'A':
                best = max(best, 2 + dfs(a - 1, b, c, 'A'))
            if b and last != 'B':
                best = max(best, 2 + dfs(a, b - 1, c, 'B'))
            if c and last != 'A':
                best = max(best, 2 + dfs(a, b, c - 1, 'B'))
            return best

        return dfs(x, y, z, '')


# V0-2
# IDEA : BOTTOM-UP TABULATION OVER THE SAME (a, b, c, last) STATE SPACE
#
#   dp[a][b][c][t] = the best length still obtainable when a "AA", b "BB" and
#   c "AB" blocks are left and the string currently ends with letter t
#   (t = 0 nothing yet, 1 -> 'A', 2 -> 'B'). the loops grow a, b and c, so
#   every state read (a-1 / b-1 / c-1) is already final : no recursion, no
#   memo table lookups, no stack depth.
#
# time = O(x * y * z)
# space = O(x * y * z)
class Solution(object):
    def longestString(self, x, y, z):
        dp = [[[[0] * 3 for _ in range(z + 1)] for _ in range(y + 1)]
              for _ in range(x + 1)]
        for a in range(x + 1):
            for b in range(y + 1):
                for c in range(z + 1):
                    for t in range(3):
                        best = 0
                        if a and t != 1:
                            best = max(best, 2 + dp[a - 1][b][c][1])
                        if b and t != 2:
                            best = max(best, 2 + dp[a][b - 1][c][2])
                        if c and t != 1:
                            best = max(best, 2 + dp[a][b][c - 1][2])
                        dp[a][b][c][t] = best
        return dp[x][y][z][0]
