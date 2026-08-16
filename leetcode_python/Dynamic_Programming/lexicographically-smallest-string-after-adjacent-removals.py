"""

3563. Lexicographically Smallest String After Adjacent Removals
Hard

You are given a string s consisting of lowercase English letters.

You can perform the following operation any number of times (including zero):

Remove any pair of adjacent characters in the string that are consecutive in the alphabet, where the alphabet is treated as cyclic ('a' and 'z' are considered consecutive).
Shift the remaining characters to the left to fill the gap.

Return the lexicographically smallest string that can be obtained after performing the operations optimally.

Note: Consider the alphabet as circular, thus 'a' and 'z' are consecutive.


Example 1:

Input: s = "abc"
Output: "a"
Explanation:
Remove "bc" from the string, leaving "a" as the remaining string. No further operations are possible. Thus, the lexicographically smallest achievable string is "a".

Example 2:

Input: s = "bcda"
Output: ""
Explanation:
Remove "cd" from the string, leaving "ba" as the remaining string. Remove "ba" from the string, leaving "" as the remaining string. Thus, the lexicographically smallest achievable string is "".

Example 3:

Input: s = "zdce"
Output: "zdce"
Explanation:
Remove "dc" from the string, leaving "ze" as the remaining string. No further operations are possible on "ze". However, since "ze" is lexicographically larger than "zdce", the smallest achievable string is "zdce".


Constraints:

1 <= s.length <= 250
s consists only of lowercase English letters.

"""

# V0
# IDEA : INTERVAL DP FOR "CAN VANISH" + SUFFIX DP FOR THE SMALLEST TAIL
#
#   a whole block s[i..j] can disappear only if s[i] pairs off with some
#   s[k] (k of opposite parity to i) and both the block strictly inside
#   (i+1..k-1) and the block after it (k+1..j) vanish on their own — the
#   removals never interleave across those two blocks, so the recursion is
#   exact.
#
#   with `gone[i][j]` known, scanning right to left gives the answer:
#   from position i we either keep s[i] and append the best tail of i+1,
#   or erase a vanishing block s[i..j] and jump to j+1. python's string
#   comparison is exactly the required lexicographic order (a prefix is
#   smaller than any extension of it, so "" beats everything).
#
# time = O(n^3), space = O(n^2)
class Solution(object):
    def lexicographicallySmallestString(self, s):
        n = len(s)

        def adjacent(a, b):
            d = abs(ord(a) - ord(b))
            return d == 1 or d == 25

        # gone[i][j] : s[i..j] (inclusive) can be fully removed
        gone = [[False] * (n + 1) for _ in range(n + 1)]

        # an empty range (i > j) vanishes trivially
        def vanish(i, j):
            if i > j:
                return True
            return gone[i][j]

        for length in range(2, n + 1, 2):
            for i in range(0, n - length + 1):
                j = i + length - 1
                ok = False
                for k in range(i + 1, j + 1, 2):
                    if adjacent(s[i], s[k]) and vanish(i + 1, k - 1) and vanish(k + 1, j):
                        ok = True
                        break
                gone[i][j] = ok

        best = [""] * (n + 1)
        for i in range(n - 1, -1, -1):
            cand = s[i] + best[i + 1]
            for j in range(i + 1, n, 2):
                if gone[i][j] and best[j + 1] < cand:
                    cand = best[j + 1]
            best[i] = cand
        return best[0]
