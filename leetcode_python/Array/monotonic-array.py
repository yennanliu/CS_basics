# V0 

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