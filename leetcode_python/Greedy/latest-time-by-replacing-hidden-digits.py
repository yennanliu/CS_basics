"""

1736. Latest Time by Replacing Hidden Digits
Easy

You are given a string time in the form of hh:mm, where some of the digits in the string are hidden (represented by ?).

The valid times are those inclusively between 00:00 and 23:59.

Return the latest valid time you can get from time by replacing the hidden digits.


Example 1:

Input: time = "2?:?0"
Output: "23:50"
Explanation: The latest hour beginning with the digit '2' is 23 and the latest minute ending with the digit '0' is 50.

Example 2:

Input: time = "0?:3?"
Output: "09:39"

Example 3:

Input: time = "1?:22"
Output: "19:22"


Constraints:

time is in the format hh:mm.
It is guaranteed that you can produce a valid time from the given string.

"""

# V0
# IDEA : GREEDY, DIGIT BY DIGIT (earlier digits dominate the value)
#
#   the time is a 4-digit number read left to right, so maximise each hidden
#   digit as large as it can be WITHOUT making the time invalid:
#
#     t[0] (hour tens) : hours cap at 23, so '2' is allowed only when the
#                        ones digit is <= 3. if t[1] is a known digit in
#                        4..9 we are forced down to '1', else take '2'.
#     t[1] (hour ones) : if the tens digit ended up '2' the cap is '3',
#                        otherwise '9'.
#     t[3] (min tens)  : minutes cap at 59 -> '5'.
#     t[4] (min ones)  : always '9'.
#
#   NOTE : order matters - decide t[0] BEFORE t[1], because t[1]'s cap
#          depends on the choice just made for t[0].
#
# time = O(1), space = O(1)
class Solution(object):
    def maximumTime(self, time):
        t = list(time)

        if t[0] == '?':
            t[0] = '1' if '4' <= t[1] <= '9' else '2'
        if t[1] == '?':
            t[1] = '3' if t[0] == '2' else '9'
        if t[3] == '?':
            t[3] = '5'
        if t[4] == '?':
            t[4] = '9'

        return ''.join(t)
