"""

2484. Count Palindromic Subsequences
Hard

Given a string of digits s, return the number of palindromic subsequences of s having length 5. Since the answer may be very large, return it modulo 10^9 + 7.

Note:

A string is palindromic if it reads the same forward and backward.
A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.


Example 1:

Input: s = "103301"
Output: 2
Explanation:
There are 6 possible subsequences of length 5: "10330","10331","10301","10301","13301","03301".
Two of them (both equal to "10301") are palindromic.

Example 2:

Input: s = "0000000"
Output: 21
Explanation: All 21 subsequences are "00000", which is palindromic.

Example 3:

Input: s = "9999900000"
Output: 2
Explanation: The only two palindromic subsequences are "99999" and "00000".


Constraints:

1 <= s.length <= 10^4
s consists of digits.

"""

# V0
# IDEA : FIX THE MIDDLE CHARACTER AND COUNT THE "ab" PAIRS ON EACH SIDE
#
#   a length-5 palindrome looks like  a b c b a. once the centre index m is
#   fixed, the count is
#       sum over digits a, b of  (pairs "ab" in s[:m]) * (pairs "ba" in s[m+1:])
#   because the right half must mirror the left.
#
#   both pair tables are 10 x 10 and are maintained INCREMENTALLY as m sweeps
#   right, so nothing is stored per index :
#     * growing the prefix by one character c adds  pair_pre[x][c] += cnt_pre[x]
#     * shrinking the suffix from its LEFT end by c removes the pairs where c
#       was the first element, i.e. pair_suf[c][y] -= (occurrences of y strictly
#       after that position)
#
#   that keeps the whole thing O(100 n) time and O(100) space rather than
#   materialising 10^4 snapshots of a 10 x 10 table.
#
# time = O(100 * n), space = O(100)
class Solution(object):
    def countPalindromes(self, s):
        MOD = 10 ** 9 + 7
        n = len(s)
        digits = [int(c) for c in s]

        # suffix side : start with the WHOLE string, then peel from the left
        cnt_suf = [0] * 10
        pair_suf = [[0] * 10 for _ in range(10)]
        for d in digits:
            for x in range(10):
                pair_suf[x][d] += cnt_suf[x]
            cnt_suf[d] += 1

        cnt_pre = [0] * 10
        pair_pre = [[0] * 10 for _ in range(10)]

        res = 0
        for m in range(n):
            c = digits[m]
            # drop s[m] from the suffix region (it becomes the centre)
            cnt_suf[c] -= 1
            for y in range(10):
                pair_suf[c][y] -= cnt_suf[y]

            for a in range(10):
                row = pair_pre[a]
                for b in range(10):
                    if row[b]:
                        res += row[b] * pair_suf[b][a]
            res %= MOD

            # now fold s[m] into the prefix region for the next centre
            for x in range(10):
                pair_pre[x][c] += cnt_pre[x]
            cnt_pre[c] += 1

        return res % MOD
