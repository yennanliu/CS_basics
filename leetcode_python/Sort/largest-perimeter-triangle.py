"""

976. Largest Perimeter Triangle
Easy


Given an integer array nums, return the largest perimeter of a triangle with a non-zero area, formed from three of these lengths. If it is impossible to form any triangle of a non-zero area, return 0.

 
Example 1:

Input: nums = [2,1,2]
Output: 5
Explanation: You can form a triangle with three side lengths: 1, 2, and 2.
Example 2:

Input: nums = [1,2,1,10]
Output: 0
Explanation: 
You cannot use the side lengths 1, 1, and 2 to form a triangle.
You cannot use the side lengths 1, 1, and 10 to form a triangle.
You cannot use the side lengths 1, 2, and 10 to form a triangle.
As we cannot use any three side lengths to form a triangle of non-zero area, we return 0.
 

Constraints:

3 <= nums.length <= 104
1 <= nums[i] <= 106

"""

# V0

# V1 
class Solution(object):
	def largestPerimeter(self, A):
		A.sort()
		for i in list(reversed(list(range(len(A)-2)))):
			if ( A[i] + A[i+1] > A[i+2] and 
			 A[i+2] + A[i+1] > A[i] and
			 A[i] + A[i+2] > A[i+1] ):
				return A[i] + A[i+1] + A[i+2]
		return 0 

# V2 
# Time:  O(nlogn)
# Space: O(1)

class Solution(object):
    def largestPerimeter(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        A.sort()
        for i in reversed(range(len(A) - 2)):
            if A[i] + A[i+1] > A[i+2]:
                return A[i] + A[i+1] + A[i+2]
        return 0