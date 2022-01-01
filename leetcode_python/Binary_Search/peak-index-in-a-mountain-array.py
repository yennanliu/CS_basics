"""

852. Peak Index in a Mountain Array
Easy

Let's call an array arr a mountain if the following properties hold:

arr.length >= 3
There exists some i with 0 < i < arr.length - 1 such that:
arr[0] < arr[1] < ... arr[i-1] < arr[i]
arr[i] > arr[i+1] > ... > arr[arr.length - 1]
Given an integer array arr that is guaranteed to be a mountain, return any i such that arr[0] < arr[1] < ... arr[i - 1] < arr[i] > arr[i + 1] > ... > arr[arr.length - 1].

 

Example 1:

Input: arr = [0,1,0]
Output: 1
Example 2:

Input: arr = [0,2,1,0]
Output: 1
Example 3:

Input: arr = [0,10,5,2]
Output: 1
 

Constraints:

3 <= arr.length <= 104
0 <= arr[i] <= 106
arr is guaranteed to be a mountain array.
 

Follow up: Finding the O(n) is straightforward, could you find an O(log(n)) solution?

"""

# V0
# IDEA : PROBLEM UNDERSTANDING
# SAME AS LC 162 Find Peak Element
class Solution(object):
    def peakIndexInMountainArray(self, arr):
        if len(arr) < 3:
            return False
        for i in range(len(arr)):
            if arr[i] > arr[i+1]:
                return i
        return -1

# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def peakIndexInMountainArray(self, arr):
        if len(arr) < 3:
            return False
        # binary search
        l = 0
        r = len(arr) - 1
        while r >= l:
            mid = l + (r-l)//2
            #print ("l = " + str(l) + " r = " + str(r) + " mid = " + str(mid))
            if arr[mid] > arr[mid-1] and arr[mid] > arr[mid+1]:
                return mid
            elif arr[mid] < arr[mid+1]:
                l = mid + 1
            else:
                r = mid - 1
        return -1

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