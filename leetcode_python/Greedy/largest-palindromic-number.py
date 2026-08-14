"""

2384. Largest Palindromic Number
Medium

You are given a string num consisting of digits only.

Return the largest palindromic integer (in the form of a string) that can be formed using digits taken from num. It should not contain leading zeroes.

Notes:

You do not need to use all the digits of num, but you must use at least one digit.
The digits can be reordered.


Example 1:

Input: num = "444947137"
Output: "7449447"
Explanation: Use the digits "4449477" from "444947137" to form the palindromic integer "7449447".
It can be shown that "7449447" is the largest palindromic integer that can be formed.

Example 2:

Input: num = "00009"
Output: "9"
Explanation: It can be shown that "9" is the largest palindromic integer that can be formed.
Note that the integer returned should not contain leading zeroes.


Constraints:

1 <= num.length <= 10^5
num consists of digits.

"""

# V0
# IDEA : BUILD THE LEFT HALF GREEDILY FROM '9' DOWN, THEN PICK A CENTRE
#
#   a palindrome is a half plus an optional middle digit plus the mirrored
#   half. to maximise it, fill the half with the LARGEST digits available in
#   pairs, biggest first.
#
#   the only trap is leading zeros : a pair of '0's may only be used once the
#   half is already non-empty, otherwise it would sit at the front.
#
#   the centre is then the largest digit with any count LEFT OVER after the
#   pairing (including a lone zero, which is fine in the middle).
#
#   if nothing survives — the input is all zeros — the answer is "0".
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def largestPalindromic(self, num):
        cnt = Counter(num)
        DIGITS = '9876543210'

        half = []
        for d in DIGITS:
            pairs = cnt[d] // 2
            if pairs == 0:
                continue
            if d == '0' and not half:
                continue                  # would create a leading zero
            half.append(d * pairs)
            cnt[d] -= pairs * 2
        half = ''.join(half)

        middle = ''
        for d in DIGITS:
            if cnt[d] > 0:
                middle = d
                break

        res = half + middle + half[::-1]
        return res if res else '0'
