"""

400. Nth Digit
Medium

Given an integer n, return the n^th digit of the infinite integer sequence [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...].

Example 1:

Input: n = 3
Output: 3

Example 2:

Input: n = 11
Output: 0
Explanation: The 11^th digit of the sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... is a 0, which is part of the number 10.

Constraints:

1 <= n <= 2^31 - 1

"""

# V0

# V1 : dev (time out error : to fix )
# class Solution(object):
# 	def findNthDigit(self, n):
# 		collected_digit=""
# 		for i in range(2**n):
# 			collected_digit = collected_digit + str(i)
# 		return int(collected_digit[n])

# V2 

# idea :
# 1   1-9
# 2   10-99
# 3   100-999
# 4   1000-9999
# 5   10000-99999
# 6   100000-999999
# 7   1000000-9999999
# 8   10000000-99999999
# 9   100000000-99999999

# http://bookshadow.com/weblog/2016/09/18/leetcode-nth-digit/
# time = O(log n)
# space = O(1)
class Solution(object):
    def findNthDigit(self, n):
        """
        :type n: int
        :rtype: int
        """
        for i in range(9):
            d = 9 * 10 ** i
            if n <= d * (i + 1): break
            n -= d * (i + 1)
        n -= 1
        return int(str(10**i + n / (i + 1))[n % (i + 1)])

# V3
# time = O(logn)
# space = O(1)
class Solution(object):
    def findNthDigit(self, n):
        """
        :type n: int
        :rtype: int
        """
        digit_len = 1
        while n > digit_len * 9 * (10 ** (digit_len-1)):
            n -= digit_len  * 9 * (10 ** (digit_len-1))
            digit_len += 1

        num = 10 ** (digit_len-1) + (n-1)/digit_len

        nth_digit = num / (10 ** ((digit_len-1) - ((n-1)%digit_len)))
        nth_digit %= 10

        return nth_digit