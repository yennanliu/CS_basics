"""

1542. Find Longest Awesome Substring
Hard

You are given a string s. An awesome substring is a non-empty substring of s such that we can make any number of swaps in order to make it a palindrome.

Return the length of the maximum length awesome substring of s.


Example 1:

Input: s = "3242415"
Output: 5
Explanation: "24241" is the longest awesome substring, we can form the palindrome "24142" with some swaps.

Example 2:

Input: s = "12345678"
Output: 1

Example 3:

Input: s = "213123"
Output: 6
Explanation: "213123" is the longest awesome substring, we can form the palindrome "231132" with some swaps.


Constraints:

1 <= s.length <= 10^5
s consists only of digits.

"""

# V0
# IDEA : PREFIX PARITY BITMASK + FIRST-OCCURRENCE TABLE
#
#   a multiset can be rearranged into a palindrome iff AT MOST ONE digit
#   occurs an odd number of times. only PARITIES matter, so compress the
#   prefix into a 10-bit mask (bit d = parity of digit d so far).
#
#   substring s[j+1 .. i] has parity mask  = mask[i] ^ mask[j].
#   it is awesome iff that xor has 0 or 1 bits set, i.e.
#       mask[j] == mask[i]            (all even)
#    or mask[j] == mask[i] ^ (1 << d) (exactly digit d odd), for d in 0..9
#
#   so store the FIRST index at which each mask appeared and, at every i,
#   test those 11 candidate masks.
#   NOTE : seed the table with {0: -1} — the empty prefix — otherwise
#          substrings starting at index 0 are missed.
#
# time = O(10 * n), space = O(2^10)
class Solution(object):
    def longestAwesome(self, s):
        first = {0: -1}
        mask = 0
        res = 0
        for i in range(len(s)):
            mask ^= 1 << (ord(s[i]) - ord('0'))

            if mask in first:
                if i - first[mask] > res:
                    res = i - first[mask]
            else:
                first[mask] = i

            for d in range(10):
                other = mask ^ (1 << d)
                if other in first and i - first[other] > res:
                    res = i - first[other]
        return res
