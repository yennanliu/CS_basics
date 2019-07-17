# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80721162
# IDEA : BINARY SEARCH 
class Solution(object):
    def peakIndexInMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        left, right = 0, len(A) - 1
        while left < right:
            mid = (left + right) / 2
            if A[mid - 1] < A[mid] and A[mid] < A[mid + 1]:
                left = mid
            elif A[mid - 1] > A[mid] and A[mid] > A[mid + 1]:
                right = mid
            else:
                break
        return mid

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80721162
# IDEA : BINARY SEARCH 
class Solution:
    def peakIndexInMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        left, right = 0, N
        while left < right:
            mid = left + (right - left) // 2
            if A[mid - 1] < A[mid] and A[mid] > A[mid + 1]:
                return mid
            if A[mid] < A[mid + 1]:
                left = mid + 1
            else:
                right = mid
        return -1

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80721162
# IDEA : MAX 
class Solution:
    def peakIndexInMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        return A.index(max(A))

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/80721162
# IDEA : FIRST DECREASE 
class Solution:
    def peakIndexInMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        for i in range(len(A) - 1):
            if A[i + 1] < A[i]:
                return i
        return -1
        
# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def peakIndexInMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        left, right = 0, len(A)
        while left < right:
            mid = left + (right-left)//2
            if A[mid] > A[mid+1]:
                right = mid
            else:
                left = mid+1
        return left