# Time:  O(n)
# Space: O(1)
# Suppose you have a long flowerbed in which some of the plots are planted and some are not.
# However, flowers cannot be planted in adjacent plots - they would compete for water
# and both would die.
#
# Given a flowerbed (represented as an array containing 0 and 1,
# where 0 means empty and 1 means not empty), and a number n,
# return if n new flowers can be planted in it without violating the no-adjacent-flowers rule.
#
# Example 1:
# Input: flowerbed = [1,0,0,0,1], n = 1
# Output: True
# Example 2:
# Input: flowerbed = [1,0,0,0,1], n = 2
# Output: False
# Note:
# The input array won't violate no-adjacent-flowers rule.
# The input array size is in the range of [1, 20000].
# n is a non-negative integer which won't exceed the input array size.


# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79253848
# http://bookshadow.com/weblog/2017/06/04/leetcode-can-place-flowers/
# IDEA : GREEDY 
# NOTE : CONTINUE VS PASS 
# In [1]: for i in range(10):
#    ...:     if i < 5:
#    ...:         continue
#    ...:     print (i)
#    ...:     
# 5
# 6
# 7
# 8
# 9

# In [2]: for i in range(10):
#    ...:     if i < 5:
#    ...:         pass 
#    ...:     print (i)
#    ...:     
# 0
# 1
# 2
# 3
# 4
# 5
# 6
# 7
# 8
# 9
class Solution(object):
    def canPlaceFlowers(self, flowerbed, n):
        """
        :type flowerbed: List[int]
        :type n: int
        :rtype: bool
        """
        for i, num in enumerate(flowerbed):
            if num == 1: continue
            if i > 0 and flowerbed[i - 1] == 1: continue
            if i < len(flowerbed) - 1 and flowerbed[i + 1] == 1: continue
            flowerbed[i] = 1
            n -= 1
        return n <= 0

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79253848
# http://bookshadow.com/weblog/2017/06/04/leetcode-can-place-flowers/
# IDEA : GREEDY 
class Solution(object):
    def canPlaceFlowers(self, flowerbed, n):
        """
        :type flowerbed: List[int]
        :type n: int
        :rtype: bool
        """
        flowerbed = [0] + flowerbed + [0]
        N = len(flowerbed)
        res = 0
        for i in range(1, N - 1):
            if flowerbed[i - 1] == flowerbed[i] == flowerbed[i + 1] == 0:
                res += 1
                flowerbed[i] = 1
        return res >= n

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def canPlaceFlowers(self, flowerbed, n):
        """
        :type flowerbed: List[int]
        :type n: int
        :rtype: bool
        """
        for i in range(len(flowerbed)):
            if flowerbed[i] == 0 and (i == 0 or flowerbed[i-1] == 0) and \
                (i == len(flowerbed)-1 or flowerbed[i+1] == 0):
                flowerbed[i] = 1
                n -= 1
            if n <= 0:
                return True
        return False
