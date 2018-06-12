

"""

# https://leetcode.com/problems/power-of-two/description/
# https://github.com/yennanliu/LeetCode/blob/master/Python/power-of-two.py
Example 1:

Input: 1
Output: true 
Explanation: 20 = 1
Example 2:

Input: 16
Output: true
Explanation: 24 = 16
Example 3:

Input: 218
Output: false



"""


# V1 

class Solution(object):
	import numpy as np 
	def isPowerOfTwo(self, n):
		if np.log2(n).is_integer():
			return True
		else:
			return False 



# V2 
# https://www.programiz.com/python-programming/operators
# &	 : Bitwise AND	
# e.g. : x& y = 0 (0000 0000)

class Solution:
    # @param {integer} n
    # @return {boolean}
    def isPowerOfTwo(self, n):
        return n > 0 and (n & (n - 1)) == 0




# V3 
# with Time Limit Exceeded error 
class Solution(object):
	def isPowerOfTwo(self, n):
		if n%2 != 0:
			return False
		else:
			n=n/2
			isPowerOfTwo(self,n)
			
		return True













