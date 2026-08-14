"""

2578. Split With Minimum Sum
Easy

Given a positive integer num, split it into two non-negative integers num1 and num2 such that:

The concatenation of num1 and num2 is a permutation of num.
In other words, the sum of the number of occurrences of each digit in num1 and num2 is equal to
the number of occurrences of that digit in num.
num1 and num2 can contain leading zeros.

Return the minimum possible sum of num1 and num2.

Notes:

It is guaranteed that num does not contain any leading zeros.
The order of occurrence of the digits in num1 and num2 may differ from the order of occurrence of num.


Example 1:

Input: num = 4325
Output: 59
Explanation: We can split 4325 so that num1 is 24 and num2 is 35, giving a sum of 59.
We can prove that 59 is indeed the minimal possible sum.

Example 2:

Input: num = 687
Output: 75
Explanation: We can split 687 so that num1 is 68 and num2 is 7, which would give an optimal sum of 75.


Constraints:

10 <= num <= 10^9

"""

# V0
# IDEA : SORT DIGITS + GREEDY ALTERNATING DEAL
#
#   the two numbers must have lengths as balanced as possible (a longer number
#   pushes some digit into a higher place value, which can only cost more), and
#   inside each number the SMALL digits belong to the HIGH place values.
#
#   so: sort all digits ascending, then deal them alternately into num1 / num2.
#   the first two (smallest) digits land on the two highest place values, the
#   next two on the next place, and so on — every place value is weighted the
#   same for both numbers, so the alternating deal is optimal.
#
#   NOTE : leading zeros are allowed, so "0" dealt first is fine — int() just
#          drops it, which is exactly the intended value.
#
#   NOTE : s[::2] / s[1::2] is the alternating deal; each keeps the digits in
#          the sorted (ascending) order, which is what we want per number.
#
# time = O(d * log d), space = O(d)   (d = number of digits, <= 10)
class Solution(object):
    def splitNum(self, num):
        s = sorted(str(num))
        return int("".join(s[0::2])) + int("".join(s[1::2]))
