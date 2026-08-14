"""

2443. Sum of Number and Its Reverse
Medium

Given a non-negative integer num, return true if num can be expressed as the sum of any non-negative integer and its reverse, or false otherwise.


Example 1:

Input: num = 443
Output: true
Explanation: 172 + 271 = 443 so we return true.

Example 2:

Input: num = 63
Output: false
Explanation: 63 cannot be expressed as the sum of a non-negative integer and its reverse so we return false.

Example 3:

Input: num = 181
Output: true
Explanation: 140 + 041 = 181 so we return true. Note that when a number is reversed, there may be leading zeros.


Constraints:

0 <= num <= 10^5

"""

# V0
# IDEA : ENUMERATION (the candidate k is bounded, so just scan it)
#
#   if num = k + reverse(k) then k <= num, and since reverse(k) >= 0 we can
#   also start the scan at k = num // 2 (k is the larger half whenever
#   reverse(k) <= k, and by symmetry one of k / reverse(k) is >= num // 2).
#   so scanning k in [num // 2, num] is enough.
#
#   NOTE : num <= 10^5, so this is ~5 * 10^4 cheap string reversals.
#          reverse() drops leading zeros: 140 + reverse(140) = 140 + 41 = 181.
#
# time = O(num * d), d = number of digits
# space = O(d)
class Solution(object):
    def sumOfNumberAndReverse(self, num):
        for k in range(num // 2, num + 1):
            if k + int(str(k)[::-1]) == num:
                return True
        return False
