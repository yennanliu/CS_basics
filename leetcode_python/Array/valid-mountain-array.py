"""

941. Valid Mountain Array
Easy

Given an array of integers arr, return true if and only if it is a valid mountain array.

Recall that arr is a mountain array if and only if:

arr.length >= 3
There exists some i with 0 < i < arr.length - 1 such that:

arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
arr[i] > arr[i + 1] > ... > arr[arr.length - 1]

https://assets.leetcode.com/uploads/2019/10/20/hint_valid_mountain_array.png

Example 1:

Input: arr = [2,1]
Output: false

Example 2:

Input: arr = [3,5,5]
Output: false

Example 3:

Input: arr = [0,3,2,1]
Output: true

Constraints:

1 <= arr.length <= 10^4
0 <= arr[i] <= 10^4

"""

# V0 

# V1 
# https://blog.csdn.net/GQxxxxxl/article/details/84247251
# IDEA : TWO POINTER 
# -> SET UP TWO POINTERS : LEFT, RIGHT (AT LEFT END AND RIGHT END) 
# -> IF A[LEFT] < A[LEFT +1] :  MOVE LEFT TO RIGHT (LEFT = LEFT + 1 )
# -> IF A[RIGHT] < A[RIGHT - 1] :  MOVE RIGHT TO LEFT (RIGHT  = RIGHT - 1 )
# -> CHECK WHETHER TWO POINTER LEFT, RIGHT OVERLAP  
# time = O(n)
# space = O(1)
class Solution:
    def validMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        if len(A)<3:
            return False
        left,right=0,len(A)-1
        while left<len(A)-1 and A[left]<A[left+1]:
            left+=1
        while right>0 and A[right]<A[right-1]:
            right-=1
        return  left!=0 and right!=len(A)-1 and left==right

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/84206380
# time = O(n)
# space = O(1)
class Solution:
    def validMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        N = len(A)
        if N < 3:
            return False
        i = 0
        while i < N - 1:
            if A[i] < A[i + 1]:
                i += 1
            else:
                break
        if i == 0 or i == N - 1: return False
        while i < N - 1:
            if A[i] > A[i + 1]:
                i += 1
            else:
                break
        return i == N - 1

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/84206380
# time = O(n)
# space = O(1)
class Solution(object):
    def validMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        print(A)
        N = len(A)
        if N < 3: return False
        i = 0
        while i < N - 1 and A[i + 1] > A[i]:
            i += 1
        if i == 0 or i == N - 1: return False
        while i < N - 1 and A[i] > A[i + 1]:
            i += 1
        return i == N - 1

# V2 
# time = O(n)
# space = O(1)
class Solution(object):
    def validMountainArray(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        i = 0
        while i+1 < len(A) and A[i] < A[i+1]:
            i += 1
        j = len(A)-1
        while j-1 >= 0 and A[j-1] > A[j]:
            j -= 1
        return 0 < i == j < len(A)-1