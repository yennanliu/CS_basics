"""

326. Power of Three
Easy

Given an integer n, return true if it is a power of three. Otherwise, return false.

An integer n is a power of three, if there exists an integer x such that n == 3^x.

Example 1:

Input: n = 27
Output: true
Explanation: 27 = 3^3

Example 2:

Input: n = 0
Output: false
Explanation: There is no x where 3^x = 0.

Example 3:

Input: n = -1
Output: false
Explanation: There is no x where 3^x = (-1).

Constraints:

-2^31 <= n <= 2^31 - 1

Follow up: Could you solve it without loops/recursion?

"""

# V0

# V1
# time = O(log n)  # n = n, log base 3
# space = O(1)
class Solution(object):
    def isPowerOfThree(self, n):
        if (n ==0 or n < 0):
            return False 
        while n > 1:
            if n%3 != 0:
                return False
        n = int(n/3)
        #print (n)
        return True

# V2
# time = O(1)
# space = O(1)
# import math
# class Solution(object):
#     def __init__(s=elf):
#         self.__max_log3 = int(math.log(0x7fffffff) / math.log(3))
#         self.__max_pow3 = 3 ** self.__max_log3

#     def isPowerOfThree(self, n):
#         """
#         :type n: int
#         :rtype: bool
#         """
#         return n > 0 and self.__max_pow3 % n == 0

# class Solution2(object):
#     def isPowerOfThree(self, n):
#         return n > 0 and (math.log10(n)/math.log10(3)).is_integer()