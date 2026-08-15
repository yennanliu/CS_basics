"""

2710. Remove Trailing Zeros From a String
Easy

Given a positive integer num represented as a string, return the integer num without trailing zeros as a string.


Example 1:

Input: num = "51230100"
Output: "512301"
Explanation: Integer "51230100" has 2 trailing zeros, we remove them and return integer "512301".

Example 2:

Input: num = "123"
Output: "123"
Explanation: Integer "123" has no trailing zeros, we return integer "123".


Constraints:

1 <= num.length <= 1000
num consists of only digits.
num doesn't have any leading zeros.

"""

# V0
# IDEA : SCAN FROM THE RIGHT
#
#   walk backwards while the current char is '0', then cut the string there.
#
#   NOTE : num is a POSITIVE integer with no leading zeros, so it can never be
#          all zeros -> the scan always stops at a non-'0' char and the result
#          is never the empty string. (str.rstrip("0") does exactly this.)
#
# time = O(n), space = O(1) beyond the output
class Solution(object):
    def removeTrailingZeros(self, num):
        i = len(num) - 1
        while i >= 0 and num[i] == '0':
            i -= 1
        return num[:i + 1]
