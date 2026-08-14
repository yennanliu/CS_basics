"""

2168. Unique Substrings With Equal Digit Frequency
Medium
(premium / locked problem)

You are given a digit string s.

Return the number of unique substrings of s where every digit appears the same number of times.


Example 1:

Input: s = "1212"
Output: 5
Explanation: The substrings that meet the requirements are "1", "2", "12", "21", "1212".
Note that although the substring "12" appears twice, it is counted only once.

Example 2:

Input: s = "12321"
Output: 9
Explanation: The substrings that meet the requirements are "1", "2", "3", "12", "23", "32", "21", "123", "321".


Constraints:

1 <= s.length <= 1000
s consists of digits.

"""

# V0
# IDEA : EXPAND EVERY START, TRACK THE COUNTS INCREMENTALLY, DEDUPE BY HASH
#
#   for a fixed start i, extend the end j one character at a time keeping :
#       cnt[d]    = occurrences of digit d in s[i..j]
#       distinct  = how many digits are present
#       hi        = the largest count
#   the window is "balanced" iff every present digit hits `hi`, which is true
#   exactly when
#       distinct * hi == (j - i + 1)
#
#   uniqueness is the other half of the problem. materialising the substrings
#   would cost O(n^3) characters, so identify each one by a POLYNOMIAL
#   ROLLING HASH built in O(1) from prefix hashes, and drop (hash, length)
#   pairs into a set.
#
# time = O(n^2), space = O(n^2) worst case for the set of hashes
class Solution(object):
    def equalDigitFrequency(self, s):
        n = len(s)
        MOD = (1 << 61) - 1
        BASE = 131

        # prefix hashes so any substring hash is O(1)
        h = [0] * (n + 1)
        pw = [1] * (n + 1)
        for i, c in enumerate(s):
            h[i + 1] = (h[i] * BASE + ord(c)) % MOD
            pw[i + 1] = pw[i] * BASE % MOD

        def sub_hash(i, j):              # s[i..j] inclusive
            return (h[j + 1] - h[i] * pw[j - i + 1]) % MOD

        seen = set()
        for i in range(n):
            cnt = [0] * 10
            distinct = 0
            hi = 0
            for j in range(i, n):
                d = ord(s[j]) - ord('0')
                if cnt[d] == 0:
                    distinct += 1
                cnt[d] += 1
                if cnt[d] > hi:
                    hi = cnt[d]
                if distinct * hi == j - i + 1:
                    seen.add(sub_hash(i, j))
        return len(seen)
