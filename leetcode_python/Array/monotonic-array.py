"""

896. Monotonic Array
Easy

An array is monotonic if it is either monotone increasing or monotone decreasing.

An array nums is monotone increasing if for all i <= j, nums[i] <= nums[j]. An array nums is monotone decreasing if for all i <= j, nums[i] >= nums[j].

Given an integer array nums, return true if the given array is monotonic, or false otherwise.

 

Example 1:

Input: nums = [1,2,2,3]
Output: true
Example 2:

Input: nums = [6,5,4,4]
Output: true
Example 3:

Input: nums = [1,3,2]
Output: false
Example 4:

Input: nums = [1,2,4,5]
Output: true
Example 5:

Input: nums = [1,1,1]
Output: true
 

Constraints:

1 <= nums.length <= 105
-105 <= nums[i] <= 105


"""

# V0 
class Solution(object):
    def isMonotonic(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        return self.isIncrease(A) or self.isDecrease(A)
        
    def isIncrease(self, A):
        for i in range(len(A) - 1):
            if A[i] > A[i+1]:
                return False
        return True

    def isDecrease(self, A):
        for i in range(len(A) - 1):
            if A[i] < A[i+1]:
                return False
        return True
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82348997
# IDEA : CHECK IF THE ARRAY IS MONOTONIC ASCENDING OR MONOTONIC DESCENDING OR NOT 
class Solution(object):
    def isMonotonic(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        return self.isIncrease(A) or self.isDecrease(A)
        
    def isIncrease(self, A):
        return all(A[i] - A[i+1] >= 0 for i in range(len(A) - 1))
        
    def isDecrease(self, A):
        return all(A[i] - A[i+1] <= 0 for i in range(len(A) - 1))

# V1'
# https://zxi.mytechroad.com/blog/algorithms/array/leetcode-896-monotonic-array/
class Solution:
  def isMonotonic(self, A):    
    inc = True;
    dec = True;
    
    for i in range(1, len(A)):
      inc = inc and A[i] >= A[i - 1]
      dec = dec and A[i] <= A[i - 1]    
    
    return inc or dec;

# V1''
# https://www.jiuzhang.com/solution/monotonic-array/#tag-highlight-lang-python
class Solution:
    """
    @param A: a array
    @return: is it monotonous
    """
    def isMonotonic(self, A):
        # Write your code here.
        sign = None
        for i in range(1, len(A)):
            if(sign == None):
                if(A[i] - A[i-1] > 0):
                    sign = 1
                elif(A[i] - A[i-1] < 0):
                    sign = -1
            else:
                if((A[i] - A[i-1]) * sign < 0):
                    return False
        return True

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isMonotonic(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        inc, dec = False, False
        for i in range(len(A)-1):
            if A[i] < A[i+1]:
                inc = True
            elif A[i] > A[i+1]:
                dec = True
        return not inc or not dec