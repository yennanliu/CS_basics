"""

1134. Armstrong Number
Easy

Given an integer n, return true if and only if it is an Armstrong number.

The k-digit number n is an Armstrong number if and only if the kth power of each digit
sums to n.


Example 1:

Input: n = 153
Output: true
Explanation: 153 is a 3-digit number, and 153 = 1^3 + 5^3 + 3^3.

Example 2:

Input: n = 123
Output: false
Explanation: 123 is a 3-digit number, and 123 != 1^3 + 2^3 + 3^3 = 36.


Constraints:

1 <= n <= 10^8

"""

# V0
# IDEA : MATH (simulation)
#        1) get digit count k
#        2) sum up (digit ** k) over all digits
#        3) compare with n
# time = O(log n)
# space = O(1)
class Solution(object):
    def isArmstrong(self, n):
        k = 0
        x = n
        while x:
            k += 1
            x //= 10

        s = 0
        x = n
        while x:
            s += (x % 10) ** k
            x //= 10

        return s == n
