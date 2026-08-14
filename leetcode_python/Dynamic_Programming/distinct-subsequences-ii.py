"""

940. Distinct Subsequences II
Hard

Given a string s, return the number of distinct non-empty subsequences of s.
Since the answer may be very large, return it modulo 10^9 + 7.

A subsequence of a string is a new string that is formed from the original string by
deleting some (can be none) of the characters without disturbing the relative positions
of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not.)


Example 1:

Input: s = "abc"
Output: 7
Explanation: The 7 distinct subsequences are "a", "b", "c", "ab", "ac", "bc", and "abc".

Example 2:

Input: s = "aba"
Output: 6
Explanation: The 6 distinct subsequences are "a", "b", "ab", "aa", "ba", and "aba".

Example 3:

Input: s = "aaa"
Output: 3
Explanation: The 3 distinct subsequences are "a", "aa" and "aaa".


Constraints:

1 <= s.length <= 2000
s consists of lowercase English letters.

"""

# V0
# IDEA : DP KEYED BY LAST CHARACTER (this is what kills the duplicates)
"""
 DP def:
    - dp[c] = number of distinct non-empty subsequences that END with character c

 Two distinct subsequences ending with the same character are counted once,
 because we OVERWRITE dp[c] instead of adding to it.

 DP eq (for each character c of s):
    - dp[c] = sum(dp) + 1
        sum(dp) : append c to every distinct subsequence built so far
        + 1     : the single-character subsequence "c"

    Overwriting is the whole trick: every subsequence ending with c is
    re-derived from the LATEST occurrence of c, so earlier duplicates
    are dropped automatically.

 Answer: sum(dp)
"""
# time = O(26 * n)
# space = O(26)
class Solution(object):
    def distinctSubseqII(self, s):
        MOD = 10 ** 9 + 7

        dp = [0] * 26  # dp[i] -> subsequences ending with chr(ord('a') + i)

        for ch in s:
            i = ord(ch) - ord('a')
            # NOTE !!! assignment (not +=) -> old subsequences ending with ch are replaced
            dp[i] = (sum(dp) + 1) % MOD

        return sum(dp) % MOD
