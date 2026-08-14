"""

2311. Longest Binary Subsequence Less Than or Equal to K
Medium

You are given a binary string s and a positive integer k.

Return the length of the longest subsequence of s that makes up a binary number less than or equal to k.

Note:

The subsequence can contain leading zeroes.
The empty string is considered to be equal to 0.
A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.


Example 1:

Input: s = "1001010", k = 5
Output: 5
Explanation: The longest subsequence of s that makes up a binary number less than or equal to 5 is "00010", as this number is equal to 2 in decimal.
Note that "00100" and "00101" are also possible, which are equal to 4 and 5 in decimal, respectively.
The length of this subsequence is 5, so 5 is returned.

Example 2:

Input: s = "00101001", k = 1
Output: 6
Explanation: "000001" is the longest subsequence of s that makes up a binary number less than or equal to 1, as this number is equal to 1 in decimal.
The length of this subsequence is 6, so 6 is returned.


Constraints:

1 <= s.length <= 1000
s[i] is either '0' or '1'.
1 <= k <= 10^9

"""

# V0
# IDEA : GREEDY FROM THE RIGHT (every '0' is free, take cheap '1's first)
#
#   leading zeroes do not change the value, so ALL zeroes of s can be kept.
#   for the ones, scan right to left: a '1' picked when the answer already
#   has `length` chars would sit at bit position `length`, i.e. it adds
#   2^length to the value. taking the rightmost ones first is optimal
#   because they are the cheapest available bits.
#
#   NOTE : guard length < 30 before shifting - k <= 10^9 < 2^30, so any
#          bit at position >= 30 cannot fit anyway.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestSubsequence(self, s, k):
        res = 0
        val = 0
        for c in reversed(s):
            if c == '0':
                res += 1
            elif res < 30 and val + (1 << res) <= k:
                val += 1 << res
                res += 1
        return res
