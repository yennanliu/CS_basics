"""

3579. Minimum Steps to Convert String with Operations
Hard

You are given two strings, word1 and word2, of equal length. You need to
transform word1 into word2.

For this, divide word1 into one or more contiguous substrings. For each
substring substr you can perform the following operations:

1. Replace: Replace the character at any one index of substr with another
   lowercase English letter.
2. Swap: Swap any two characters in substr.
3. Reverse Substring: Reverse substr.

Each of these counts as one operation and each character of each substring
can be used in each type of operation at most once (i.e. no single index may
be involved in more than one replace, one swap, or one reverse).

Return the minimum number of operations required to transform word1 into
word2.


Example 1:

Input: word1 = "abcdf", word2 = "dacbe"
Output: 4
Explanation:
Divide word1 into "ab", "c", and "df". The operations are:
- For the substring "ab",
  - Perform operation of type 3 on "ab" -> "ba".
  - Perform operation of type 1 on "ba" -> "da".
- For the substring "c" do no operations.
- For the substring "df",
  - Perform operation of type 1 on "df" -> "bf".
  - Perform operation of type 1 on "bf" -> "be".

Example 2:

Input: word1 = "abceded", word2 = "baecfef"
Output: 4
Explanation:
Divide word1 into "ab", "ce", and "ded". The operations are:
- For the substring "ab",
  - Perform operation of type 2 on "ab" -> "ba".
- For the substring "ce",
  - Perform operation of type 2 on "ce" -> "ec".
- For the substring "ded",
  - Perform operation of type 1 on "ded" -> "fed".
  - Perform operation of type 1 on "fed" -> "fef".

Example 3:

Input: word1 = "abcdef", word2 = "fedabc"
Output: 2
Explanation:
Divide word1 into "abcdef". The operations are:
- For the substring "abcdef",
  - Perform operation of type 3 on "abcdef" -> "fedcba".
  - Perform operation of type 2 on "fedcba" -> "fedabc".


Constraints:

1 <= word1.length == word2.length <= 100
word1 and word2 consist only of lowercase English letters.

"""

# V0
# IDEA : PARTITION DP OVER SUBSTRINGS + GREEDY PAIRING OF MISMATCHES
#
#   cutting word1 into pieces is free and pieces never interact, so the
#   whole thing is a linear partition dp: f[i] = best cost for the first i
#   characters, f[i] = min over j of f[j] + cost(j, i-1).
#
#   inside one piece the cost is exact and needs no search. every index that
#   already matches is left alone. an index that mismatches must be fixed
#   either by a replace (1 op for 1 index) or by a swap, and a swap only
#   helps when it fixes *both* of its endpoints — that happens exactly when
#   one index needs a -> b while another needs b -> a. so a swap is 1 op for
#   2 indices and everything else is 1 op for 1 index; the cost is
#   (#mismatches) - (#such opposite pairs), and pairing greedily by keeping
#   a running tally of unmatched (a, b) demands is optimal because every
#   (a, b) is interchangeable with every other (a, b).
#
#   reverse is worth at most one use per piece: two reverses cancel, and a
#   reverse commutes with the swaps/replaces that follow it (relabelling
#   positions), so "reverse first or not at all" covers every schedule. that
#   leaves just two candidates per piece, cost(no reverse) and
#   1 + cost(after reversing).
#
"""

DP def
    cutting word1 into pieces is FREE and pieces never interact, so it is a
    linear PARTITION DP

    f[i]: MIN total cost for the first i characters

    cost(l, r, rev): exact cost of turning word1[l..r] (optionally reversed)

                     into word2[l..r], not counting the reversal itself

DP eq

     f[i] = min over j < i of  f[j] + min( cost(j, i-1, False),

                                           1 + cost(j, i-1, True) )


    -> e.g. inside one piece the cost needs NO search:
         every matching index is left alone; a mismatch is fixed by a
         REPLACE (1 op / 1 index) or by a SWAP, and a swap only helps when
         it fixes BOTH endpoints - i.e. one index needs a -> b while another
         needs b -> a

         cost = (#mismatches) - (#such opposite pairs), and pairing greedily
         with a running tally of unmatched (a, b) demands is optimal because
         all (a, b) demands are interchangeable

     REVERSE is worth at most ONE use per piece: two reverses cancel, and a
     reverse commutes with the later swaps/replaces (just relabelling
     positions), so "reverse first or not at all" covers every schedule

     init: f[0] = 0
     ans = f[n]

"""
# time = O(n^3 + n^2 * |S|), space = O(n + |S|^2)
class Solution(object):
    def minOperations(self, word1, word2):
        n = len(word1)

        def cost(l, r, rev):
            # min ops to turn word1[l..r] (optionally reversed) into
            # word2[l..r], not counting the reversal itself
            cnt = {}
            res = 0
            for i in range(l, r + 1):
                j = r - (i - l) if rev else i
                a, b = word1[j], word2[i]
                if a == b:
                    continue
                # an outstanding (b, a) demand turns two replaces into one swap
                if cnt.get((b, a), 0) > 0:
                    cnt[(b, a)] -= 1
                else:
                    cnt[(a, b)] = cnt.get((a, b), 0) + 1
                    res += 1
            return res

        f = [float('inf')] * (n + 1)
        f[0] = 0
        for i in range(1, n + 1):
            for j in range(i):
                t = min(cost(j, i - 1, False), 1 + cost(j, i - 1, True))
                if f[j] + t < f[i]:
                    f[i] = f[j] + t
        return f[n]
