"""

441. Arranging Coins
Easy

You have n coins and you want to build a staircase with these coins. The staircase consists of k rows where the i^th row has exactly i coins. The last row of the staircase may be incomplete.

Given the integer n, return the number of complete rows of the staircase you will build.

Example 1:

https://assets.leetcode.com/uploads/2021/04/09/arrangecoins1-grid.jpg

Input: n = 5
Output: 2
Explanation: Because the 3^rd row is incomplete, we return 2.

Example 2:

https://assets.leetcode.com/uploads/2021/04/09/arrangecoins2-grid.jpg

Input: n = 8
Output: 3
Explanation: Because the 4^th row is incomplete, we return 3.

Constraints:

1 <= n <= 2^31 - 1

"""

# V0

# V1
# time = O(sqrt(n))
# space = O(1)
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
# time = O(logn)  # sqrt is O(logn) time
# space = O(1)
import math
class Solution(object):
	def arrangeCoins(self, n):
		return  int((math.sqrt(1+8*n) - 1)/2)
		

# V2
# time = O(logn)
# space = O(1)

import math
class Solution(object):
    def arrangeCoins(self, n):
        """
        :type n: int
        :rtype: int
        """
        return int((math.sqrt(8*n+1)-1) / 2)  # sqrt is O(logn) time.


# time = O(logn)
# space = O(1)
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
