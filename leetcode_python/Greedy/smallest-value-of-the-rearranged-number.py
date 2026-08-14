"""

2165. Smallest Value of the Rearranged Number
Medium

You are given an integer num. Rearrange the digits of num such that its value is minimized and it does not contain any leading zeros.

Return the rearranged number with minimal value.

Note that the sign of the number does not change after rearranging the digits.


Example 1:

Input: num = 310
Output: 103
Explanation: The possible arrangements for the digits of 310 are 013, 031, 103, 130, 301, 310.
The arrangement with the smallest value that does not contain any leading zeros is 103.

Example 2:

Input: num = -7605
Output: -7650
Explanation: Some possible arrangements for the digits of -7605 are -7650, -6705, -5076, -0567.
The arrangement with the smallest value that does not contain any leading zeros is -7650.


Constraints:

-10^15 <= num <= 10^15

"""

# V0
# IDEA : SIGN DECIDES THE SORT DIRECTION; ZEROS ONLY MATTER WHEN POSITIVE
#
#   NEGATIVE num : the magnitude should be as LARGE as possible, so sort the
#                  digits descending. leading zeros cannot occur (the biggest
#                  digit leads unless the number is all zeros).
#
#   POSITIVE num : sort ascending for the smallest magnitude, but a leading
#                  zero is illegal — so swap the first NON-ZERO digit into
#                  position 0 and leave the zeros right behind it.
#
# time = O(d log d), space = O(d)
class Solution(object):
    def smallestNumber(self, num):
        if num == 0:
            return 0
        if num < 0:
            return -int(''.join(sorted(str(-num), reverse=True)))

        d = sorted(str(num))
        if d[0] == '0':
            i = next(k for k, c in enumerate(d) if c != '0')
            d[0], d[i] = d[i], d[0]
        return int(''.join(d))
