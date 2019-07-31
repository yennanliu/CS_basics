# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79089937
# IDEA : GREEDY 
# TO CHECK IF 0 ALWAYS NEXT/BEFORE 1 (1 ALWAYS NEXT/BEFORE 0)
# IDEA : ALL FUN IN PYTHON 
# https://www.programiz.com/python-programming/methods/built-in/all
# Return Value from all()
# The all() method returns:
# True - If all elements in an iterable are true
# False - If any element in an iterable is false
class Solution(object):
    def hasAlternatingBits(self, n):
        """
        :type n: int
        :rtype: bool
        """
        bin_n = bin(n)[2:]
        return all(bin_n[i] != bin_n[i+1] for i in range(len(bin_n) - 1))

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79089937
# IDEA : PATTERN
class Solution(object):
    def hasAlternatingBits(self, n):
        """
        :type n: int
        :rtype: bool
        """
        b = 0b1010101010101010101010101010101010101010101010101010101010101010
        while b > 0:
            if b == n:
                return True
            b = b >> 1
        return False

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79089937
# IDEA : BIT MANIPULATION 
class Solution(object):
    def hasAlternatingBits(self, n):
        """
        :type n: int
        :rtype: bool
        """
        n ^= (n >> 1)
        return not (n & n + 1)

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def hasAlternatingBits(self, n):
        """
        :type n: int
        :rtype: bool
        """
        n, curr = divmod(n, 2)
        while n > 0:
            if curr == n % 2:
                return False
            n, curr = divmod(n, 2)
        return True
