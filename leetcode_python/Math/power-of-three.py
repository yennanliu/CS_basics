# V1 
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
# Time:  O(1)
# Space: O(1)
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