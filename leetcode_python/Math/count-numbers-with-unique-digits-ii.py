"""

3032. Count Numbers With Unique Digits II
Easy
🔒 (premium)

Given two positive integers a and b, return the count of numbers having unique digits in the range [a, b] (inclusive).


Example 1:

Input: a = 1, b = 20
Output: 19
Explanation: All the numbers in the range [1, 20] have unique digits except 11. Hence, the answer is 19.

Example 2:

Input: a = 9, b = 19
Output: 10
Explanation: All the numbers in the range [9, 19] have unique digits except 11. Hence, the answer is 10.

Example 3:

Input: a = 80, b = 120
Output: 27
Explanation: There are 41 numbers in the range [80, 120], 27 of which have unique digits.


Constraints:

1 <= a <= b <= 1000

"""

# V0
# IDEA : b <= 1000, SO JUST TEST EVERY NUMBER
#
#   at most 1000 candidates and at most 4 digits each — a set of the digits
#   has the same size as the digit string exactly when no digit repeats.
#
#   NOTE : the digit-DP that this problem's title hints at only pays off for
#          much larger bounds; here the direct scan is both shorter and
#          faster than building one.
#
# time = O((b - a) * digits), space = O(1)
class Solution(object):
    def numberCount(self, a, b):
        res = 0
        for x in range(a, b + 1):
            s = str(x)
            if len(set(s)) == len(s):
                res += 1
        return res
