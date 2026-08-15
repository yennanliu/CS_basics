"""

3138. Minimum Length of Anagram Concatenation
Medium

You are given a string s, which is known to be a concatenation of anagrams of some string t.

Return the minimum possible length of the string t.

An anagram is formed by rearranging the letters of a string. For example, "aab", "aba", and, "baa" are anagrams of "aab".


Example 1:

Input: s = "abba"
Output: 2
Explanation:
One possible string t could be "ba".

Example 2:

Input: s = "cdef"
Output: 4
Explanation:
One possible string t could be "cdef", notice that t can be equal to s.


Constraints:

1 <= s.length <= 10^5
s consist only of lowercase English letters.

"""

# V0
# IDEA : THE BLOCK COUNT MUST DIVIDE EVERY LETTER'S TOTAL — THEN VERIFY
#
#   if s splits into d anagram blocks, each block holds total[c] / d copies of
#   letter c, so d must divide EVERY letter total (and hence their gcd, and
#   n itself). that prunes the candidates from "all divisors of n" down to
#   "divisors of g", where g = gcd of the letter totals.
#
#   the block length is k = n / d, so the smallest k comes from the LARGEST
#   valid d — walk the divisors of g downwards and return the first that
#   verifies.
#
#   verification compares sorted(block) against the first block's; sorting a
#   slice runs in C, so a candidate costs one cheap pass over the string
#   rather than a python-level recount.
#
# time = O(d(g) * n log n) worst case, space = O(n)
class Solution(object):
    def minAnagramLength(self, s):
        n = len(s)

        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        totals = {}
        for ch in s:
            totals[ch] = totals.get(ch, 0) + 1
        g = 0
        for v in totals.values():
            g = gcd(g, v)

        divisors = sorted((d for d in range(1, g + 1) if g % d == 0), reverse=True)
        for d in divisors:
            k = n // d
            base = sorted(s[:k])
            if all(sorted(s[i:i + k]) == base for i in range(k, n, k)):
                return k
        return n
