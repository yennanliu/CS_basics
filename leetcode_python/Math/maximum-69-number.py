"""

1323. Maximum 69 Number
Easy

You are given a positive integer num consisting only of digits 6 and 9.

Return the maximum number you can get by changing at most one digit (6 becomes 9, and 9 becomes 6).


Example 1:

Input: num = 9669
Output: 9969
Explanation:
Changing the first digit results in 6669.
Changing the second digit results in 9969.
Changing the third digit results in 9699.
Changing the fourth digit results in 9666.
The maximum number is 9969.

Example 2:

Input: num = 9996
Output: 9999
Explanation: Changing the last digit 6 to 9 results in the maximum number.

Example 3:

Input: num = 9999
Output: 9999
Explanation: It is better not to apply any change.


Constraints:

1 <= num <= 10^4
num consists of only 6 and 9 digits.

"""

# V0
# IDEA : GREEDY (flip the LEFTMOST 6 into a 9)
#
#   changing a 9 into a 6 can only shrink the number, so the single allowed
#   edit must be 6 -> 9.
#   among all the 6s, upgrading the most significant one buys the largest
#   place value, so the leftmost 6 is always the right pick.
#   NOTE : replace(..., 1) already stops after the first match; when there is
#          no 6 at all the string is returned untouched (num is the answer).
#
# time = O(d), space = O(d)   d = number of digits
class Solution(object):
    def maximum69Number(self, num):
        return int(str(num).replace('6', '9', 1))
