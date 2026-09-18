"""

1461. Check If a String Contains All Binary Codes of Size K
Medium

Given a binary string s and an integer k, return true if every binary code of
length k is a substring of s. Otherwise, return false.


Example 1:

Input: s = "00110110", k = 2
Output: true
Explanation: The binary codes of length 2 are "00", "01", "10" and "11". They can
be all found as substrings at indices 0, 1, 3 and 2 respectively.

Example 2:

Input: s = "0110", k = 1
Output: true
Explanation: The binary codes of length 1 are "0" and "1", it is clear that both
exist as a substring.

Example 3:

Input: s = "0110", k = 2
Output: false
Explanation: The binary code "00" is of length 2 and does not exist in the array.


Constraints:

1 <= s.length <= 5 * 10^5
s[i] is either '0' or '1'.
1 <= k <= 20

"""

# V0
# IDEA : ROLLING WINDOW AS AN INTEGER + SET (count the codes, do not build them)
#
#   there are exactly 2^k codes of length k, so the answer is just
#   "did the n - k + 1 windows cover all 2^k of them".
#
#   the window is carried as an integer instead of a string slice : shift left,
#   drop in the new bit, mask off the bit that fell out the top.
#
#     cur = ((cur << 1) | bit) & (2^k - 1)
#
#   e.g. s = "00110110", k = 2 -> windows 00,01,11,10,01,11,10 -> {0,1,3,2} = 4
#        = 2^2 -> True
#
#   NOTE !!! slicing s[i:i+k] instead would be O(n * k) time AND O(n * k) of
#            string garbage -- with n = 5*10^5 and k = 20 that is the difference
#            between passing and timing out.
#
# time = O(n), space = O(2^k)
class Solution(object):
    def hasAllCodes(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: bool
        """
        n = len(s)
        need = 1 << k

        # pigeonhole : n - k + 1 windows cannot cover 2^k distinct codes
        if n < k + need - 1:
            return False

        mask = need - 1
        seen = set()
        cur = 0
        for i, ch in enumerate(s):
            cur = ((cur << 1) | (1 if ch == '1' else 0)) & mask
            if i >= k - 1:
                seen.add(cur)
                if len(seen) == need:
                    return True

        return len(seen) == need
