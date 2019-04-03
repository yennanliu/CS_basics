# V1 : DEV 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79451356
class Solution:
    def flipLights(self, n, m):
        """
        :type n: int
        :type m: int
        :rtype: int
        """
        if m == 0:
            return 1
        if n >= 3:
            return 4 if m == 1 else 7 if m == 2 else 8
        if n == 2:
            return 3 if m == 1 else 4
        if n == 1:
            return 2

# V3 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def flipLights(self, n, m):
        """
        :type n: int
        :type m: int
        :rtype: int
        """
        if m == 0:
            return 1
        if n == 1:
            return 2
        if m == 1 and n == 2:
            return 3
        if m == 1 or n == 2:
            return 4
        if m == 2:
            return 7
        return 8
