"""

1837. Sum of Digits in Base K
Easy

Given an integer n (in base 10) and a base k, return the sum of the digits of n after converting n from base 10 to base k.

After converting, each digit should be interpreted as a base 10 number, and the sum should be returned in base 10.


Example 1:

Input: n = 34, k = 6
Output: 9
Explanation: 34 (base 10) expressed in base 6 is 54. 5 + 4 = 9.

Example 2:

Input: n = 10, k = 10
Output: 1
Explanation: n is already in base 10. 1 + 0 = 1.


Constraints:

1 <= n <= 100
2 <= k <= 10

"""

# V0
# IDEA : REPEATED DIVMOD (peel off base-k digits from the least significant end)
#
#   n % k is the lowest base-k digit, n //= k drops it. add them up until n is 0.
#
#   NOTE : we never have to build the base-k string -- only the digit SUM is
#          asked for, and the digits are already base-10 integers here since
#          k <= 10.
#
# time = O(log_k n), space = O(1)
class Solution(object):
    def sumBase(self, n, k):
        res = 0
        while n:
            res += n % k
            n //= k
        return res
