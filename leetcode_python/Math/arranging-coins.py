# V1 
class Solution(object):
	def arrangeCoins(self, n):
		i = 1
		if n == 1:
			return 1 
		while (i*(i+1))/2 < n:
			i = i + 1
		if (i*(i+1))/2  == n:
				return i 
		return i - 1

# V1'
# The core of this problem is solving this equation : k^2 + k > 2n
# which has 2 roots : k = (- 1 +- (1+8*n)^(1/2))/2
# so we take the "positve root" -> k = (- 1 + (1+8*n)^(1/2))/2
# k =  (math.sqrt((1+8*n)/2) - 1)/2
import math
class Solution(object):
	def arrangeCoins(self, n):
		return  int((math.sqrt(1+8*n) - 1)/2)
		

# V2 
# Time:  O(logn)
# Space: O(1)

import math
class Solution(object):
    def arrangeCoins(self, n):
        """
        :type n: int
        :rtype: int
        """
        return int((math.sqrt(8*n+1)-1) / 2)  # sqrt is O(logn) time.


# Time:  O(logn)
# Space: O(1)
class Solution2(object):
    def arrangeCoins(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left <= right:
            mid = left + (right - left) / 2
            if 2 * n < mid * (mid+1):
                right = mid - 1
            else:
                left = mid + 1
        return left - 1
