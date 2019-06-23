# V1 
import math
class Solution(object):
	def judgeSquareSum(self, c):
		mid_point = int(math.sqrt(c))
		left = 0
		right = mid_point
		"""
		the condition here should be "while left <= mid_point" instead of "for i in range(0, mid_point)"
		since it woule only run  mid_point times if in for loop case 
		However, setting left <= mid_point can make sure that left and right 
		loop all possible combination that make left**2 + right**2 = c 
		"""
		#for i in range(0, mid_point):
		while left <= mid_point: 
			if left**2 + right**2 < c:
				left = left + 1 
			elif left**2 + right**2 > c:
				right = right - 1 
			else:
				return True 
		return False 

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/77531867
class Solution(object):
    def judgeSquareSum(self, c):
        """
        :type c: int
        :rtype: bool
        """
        left = 0
        right = int(c ** 0.5)
        while left <= right:
            cur = left ** 2 + right ** 2
            if cur < c:
                left += 1
            elif cur > c:
                right -= 1
            else:
                return True
        return False

# V2 
# https://blog.csdn.net/marlonlyh/article/details/74529655
# idea :
# since a**2 + b**2 = c 
# so c - b**2 = a**2 which is a square integer
# so the key of this solution is :  check if c - b**2 is a square integer
class Solution:
    def judgeSquareSum(self, c):
        """
        :type c: int
        :rtype: bool
        """
        for i in range(0,int(c**0.5)+1):
            j=c-i**2
            if((int(j**0.5)**2)==j):
                return True
        return False

# V3 
# Time:  O(sqrt(c) * logc)
# Space: O(1)
import math
class Solution(object):
    def judgeSquareSum(self, c):
        """
        :type c: int
        :rtype: bool
        """
        for a in range(int(math.sqrt(c))+1):
            b = int(math.sqrt(c-a**2))
            if a**2 + b**2 == c:
                return True
        return False
