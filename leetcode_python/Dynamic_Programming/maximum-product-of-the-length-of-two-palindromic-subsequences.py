"""

2002. Maximum Product of the Length of Two Palindromic Subsequences
Medium

Given a string s, find two disjoint palindromic subsequences of s such that the product of their lengths is maximized. The two subsequences are disjoint if they do not both pick a character at the same index.

Return the maximum possible product of the lengths of the two palindromic subsequences.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters. A string is palindromic if it reads the same forward and backward.


Example 1:

Input: s = "leetcodecom"
Output: 9
Explanation: An optimal solution is to choose "ete" for the 1st subsequence and "cdc" for the 2nd subsequence.
The product of their lengths is: 3 * 3 = 9.

Example 2:

Input: s = "bb"
Output: 1
Explanation: An optimal solution is to choose "b" (the first character) for the 1st subsequence and "b" (the second character) for the 2nd subsequence.
The product of their lengths is: 1 * 1 = 1.

Example 3:

Input: s = "accbcaxxcxx"
Output: 25
Explanation: An optimal solution is to choose "accca" for the 1st subsequence and "xxcxx" for the 2nd subsequence.
The product of their lengths is: 5 * 5 = 25.


Constraints:

2 <= s.length <= 12
s consists of lowercase English letters only.

"""

# V0
# IDEA : BITMASK ENUMERATION + SUBMASK ENUMERATION (n <= 12, so 2^n masks is tiny)
#
#   step 1 : for every mask of indices, build the picked substring and test
#            whether it is a palindrome  ->  ok[mask]
#   step 2 : for every palindromic mask a, walk the SUBMASKS of its complement
#            (the classic  j = (j - 1) & comp  trick) and keep the best
#            popcount(a) * popcount(b) over palindromic b.
#
#   NOTE : disjoint == the two masks share no bit, which is exactly why b must
#          be a submask of ~a.
#   NOTE : submask enumeration over all masks costs sum over masks of
#          2^(n - popcount) = 3^n, not 4^n.
#
# time = O(3^n + 2^n * n), space = O(2^n)
class Solution(object):
    def maxProduct(self, s):
        n = len(s)
        full = (1 << n) - 1

        ok = [False] * (1 << n)
        bits = [0] * (1 << n)
        for mask in range(1, 1 << n):
            picked = []
            cnt = 0
            for i in range(n):
                if mask >> i & 1:
                    picked.append(s[i])
                    cnt += 1
            bits[mask] = cnt
            ok[mask] = picked == picked[::-1]

        res = 0
        for a in range(1, 1 << n):
            if not ok[a]:
                continue
            comp = full ^ a
            b = comp
            while b:
                if ok[b]:
                    cur = bits[a] * bits[b]
                    if cur > res:
                        res = cur
                b = (b - 1) & comp

        return res
