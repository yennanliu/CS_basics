"""

2842. Count K-Subsequences of a String With Maximum Beauty
Hard

You are given a string s and an integer k.

A k-subsequence is a subsequence of s, having length k, and all its characters are unique, i.e., every character occurs once.

Let f(c) denote the number of times the character c occurs in s.

The beauty of a k-subsequence is the sum of f(c) for every character c in the k-subsequence.

For example, consider s = "abbbdd" and k = 2:

f('a') = 1, f('b') = 3, f('d') = 2
Some k-subsequences of s are:
"abbbdd" -> "ab" having a beauty of f('a') + f('b') = 4
"abbbdd" -> "ad" having a beauty of f('a') + f('d') = 3
"abbbdd" -> "bd" having a beauty of f('b') + f('d') = 5

Return an integer denoting the number of k-subsequences whose beauty is the maximum among all k-subsequences. Since the answer may be too large, return it modulo 10^9 + 7.

A subsequence of a string is a new string formed from the original string by deleting some (possibly none) of the characters without disturbing the relative positions of the remaining characters.

Notes

- f(c) is the number of times a character c occurs in s, not a k-subsequence.
- Two k-subsequences are considered different if one is formed by an index that is not present in the other. So, two k-subsequences may form the same string.


Example 1:

Input: s = "bcca", k = 2
Output: 4
Explanation: From s we have f('a') = 1, f('b') = 1, and f('c') = 2.
The k-subsequences of s are:
bcca having a beauty of f('b') + f('c') = 3
bcca having a beauty of f('b') + f('c') = 3
bcca having a beauty of f('b') + f('a') = 2
bcca having a beauty of f('c') + f('a') = 3
bcca having a beauty of f('c') + f('a') = 3
There are 4 k-subsequences that have the maximum beauty, 3.
Hence, the answer is 4.

Example 2:

Input: s = "abbcd", k = 4
Output: 2
Explanation: From s we have f('a') = 1, f('b') = 2, f('c') = 1, and f('d') = 1.
The k-subsequences of s are:
abbcd having a beauty of f('a') + f('b') + f('c') + f('d') = 5
abbcd having a beauty of f('a') + f('b') + f('c') + f('d') = 5
There are 2 k-subsequences that have the maximum beauty, 5.
Hence, the answer is 2.


Constraints:

1 <= s.length <= 2 * 10^5
1 <= k <= s.length
s consists only of lowercase English letters.

"""

# V0
# IDEA : GREEDY (pick the k biggest frequencies) + COMBINATORICS
#
#   a k-subsequence uses k DISTINCT letters, so if s has fewer than k distinct
#   letters the answer is 0. otherwise the beauty is the sum of the chosen
#   letters' frequencies, and it is maximised by simply taking the k largest
#   frequencies -> sort the frequencies descending as vs.
#
#   counting the ways: a chosen letter c can be realised by ANY one of its f(c)
#   positions, so each chosen letter contributes a factor f(c).
#     - letters with frequency STRICTLY greater than val = vs[k-1] are forced
#       into every optimal pick -> multiply their frequencies together.
#     - letters with frequency exactly val are interchangeable: if x of them
#       exist and r = k - (#forced) slots remain, choose which ones with
#       C(x, r) and pick a position inside each with val^r.
#
#   ans = (prod of forced freqs) * C(x, r) * val^r  (mod 1e9+7)
#
#   NOTE : distinct letters <= 26, so C(x, r) is tiny - compute it exactly with
#          an integer loop (no modular inverse / no math.comb needed).
#   NOTE : take the product modulo as you go; freqs multiply up fast.
#
# time = O(n + 26 log 26), space = O(26)
class Solution(object):
    def countKSubsequencesWithMaxBeauty(self, s, k):
        MOD = 10 ** 9 + 7

        freq = {}
        for ch in s:
            freq[ch] = freq.get(ch, 0) + 1
        if len(freq) < k:
            return 0

        vs = sorted(freq.values(), reverse=True)
        val = vs[k - 1]

        ans = 1
        forced = 0
        while forced < k and vs[forced] > val:
            ans = ans * vs[forced] % MOD
            forced += 1

        x = 0
        for v in vs:
            if v == val:
                x += 1
        r = k - forced

        # C(x, r), exact (x <= 26)
        c = 1
        for i in range(r):
            c = c * (x - i) // (i + 1)

        return ans * (c % MOD) % MOD * pow(val, r, MOD) % MOD
