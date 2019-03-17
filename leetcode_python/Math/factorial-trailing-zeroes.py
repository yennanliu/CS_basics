
# V1  : dev 
# TODO : fix time out error 
# class Solution(object):
# 	# @return an integer
# 	def trailingZeroes(self, n):
# 		result = 1 
# 		for i in range(1,n+1):
# 			result = result*i 
# 		count = 0
# 		for i in str(result)[::-1]:
# 			if i == '0':
# 				count += 1 
# 			else:
# 				break
# 		return  count 

# V2  : dev 
# class Solution(object):
# 	# @return an integer
# 	def trailingZeroes(self, n):
# 		count = 0 
# 		for i in range(1,n+1):
# 			if (i%5==0): 
# 				while i >0:
# 					i = int(i/5) 
# 					count = count+1  
# 		return 0 if count - 1  < 0 else count - 1 



# V3 
"""

* only have to consider 5, but not 2, since the amount of 2 is always enough 
for example :

135!
 
-> 135/5 = 27 : there are 27 number in [1,  135] is 5 multiplier
-> 27/5 =  5 : there are 5 number in [1, 135] is 25 multiplier
-> 5/5 =  1 : there are 1 number in [1,  135] is 125 multiplier

so the number Trailing Zeroes in 135! =  27+5 + 1 = 33

"""
# https://blog.csdn.net/coder_orz/article/details/51590478
class Solution(object):
    def trailingZeroes(self, n):
        """
        :type n: int
        :rtype: int
        """
        res = 0
        while n > 0:
            n = n/5
            res += n
        return res

# V4 
# Time:  O(logn) = O(1)
# Space: O(1)
class Solution(object):
    # @return an integer
    def trailingZeroes(self, n):
        result = 0
        while n > 0:
            result += n / 5
            n /= 5
        return result
