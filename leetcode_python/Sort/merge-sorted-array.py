# Time:  O(n)
# Space: O(1)
#
# Given two sorted integer arrays A and B, merge B into A as one sorted array.
#
# Note:
# You may assume that A has enough space (size that is greater or equal to m + n) to hold additional elements from B.
# The number of elements initialized in A and B are m and n respectively.
#

# V0 
# IDEA 
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        """
        :type nums1: List[int]
        :type m: int
        :type nums2: List[int]
        :type n: int
        :rtype: void Do not return anything, modify nums1 in-place instead.
        """
        p, q = m-1, n-1
        while p >= 0 and q >= 0:
            if nums1[p] > nums2[q]:
                nums1[p+q+1] = nums1[p]
                p = p-1
            else:
                nums1[p+q+1] = nums2[q]
                q = q-1
        nums1[:q+1] = nums2[:q+1]

# V0'
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        return sorted(nums1 + nums2)

# V1
# https://blog.csdn.net/coder_orz/article/details/51681144
# IDEA : 3 POINTERS
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        """
        :type nums1: List[int]
        :type m: int
        :type nums2: List[int]
        :type n: int
        :rtype: void Do not return anything, modify nums1 in-place instead.
        """
        p, q, k = m-1, n-1, m+n-1
        while p >= 0 and q >= 0:
            if nums1[p] > nums2[q]:
                nums1[k] = nums1[p]
                p, k = p-1, k-1
            else:
                nums1[k] = nums2[q]
                q, k = q-1, k-1
        nums1[:q+1] = nums2[:q+1]

# V1'
# https://blog.csdn.net/coder_orz/article/details/51681144
# IDEA : 2 POINTERS
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        """
        :type nums1: List[int]
        :type m: int
        :type nums2: List[int]
        :type n: int
        :rtype: void Do not return anything, modify nums1 in-place instead.
        """
        p, q = m-1, n-1
        while p >= 0 and q >= 0:
            if nums1[p] > nums2[q]:
                nums1[p+q+1] = nums1[p]
                p = p-1
            else:
                nums1[p+q+1] = nums2[q]
                q = q-1
        nums1[:q+1] = nums2[:q+1]

# V1'
# https://www.jiuzhang.com/solution/merge-sorted-array/#tag-highlight-lang-python
class Solution:
    """
    @param: A: sorted integer array A which has m elements, but size of A is m+n
    @param: m: An integer
    @param: B: sorted integer array B which has n elements
    @param: n: An integer
    @return: nothing
    """
    def mergeSortedArray(self, A, m, B, n):
        # write your code here
        pos = m + n - 1 
        i = m - 1  
        j = n - 1
        while  i >= 0 and j >= 0 :
            if A[i]>B[j] :
                A[pos]=A[i]
                pos-=1
                i-=1
            else :
                A[pos]=B[j]
                pos-=1
                j-=1
                
        while i >= 0 :
            A[pos] = A[i]
            pos-=1
            i-=1
        while j >= 0:
            A[pos] = B[j]
            pos-=1
            j-=1

# V1''
# https://blog.csdn.net/liuxiao214/article/details/77856326
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        """
        :type nums1: List[int]
        :type m: int
        :type nums2: List[int]
        :type n: int
        :rtype: void Do not return anything, modify nums1 in-place instead.
        """
        i=m-1
        j=n-1
        k=m+n-1
        while k>=0:
            if (j<0 or nums1[i]>nums2[j]) and i>=0:
                nums1[k]=nums1[i]
                i-=1
            else:
                nums1[k]=nums2[j]
                j-=1
            k-=1

# V2 
class Solution:
    # @param A  a list of integers
    # @param m  an integer, length of A
    # @param B  a list of integers
    # @param n  an integer, length of B
    # @return nothing
    def merge(self, A, m, B, n):
        last, i, j = m + n - 1, m - 1, n - 1

        while i >= 0 and j >= 0:
            if A[i] > B[j]:
                A[last] = A[i]
                last, i = last - 1, i - 1
            else:
                A[last] = B[j]
                last, j = last - 1, j - 1

        while j >= 0:
                A[last] = B[j]
                last, j = last - 1, j - 1

# if __name__ == "__main__":
#     A = [1, 3, 5, 0, 0, 0, 0]
#     B = [2, 4, 6, 7]
#     Solution().merge(A, 3, B, 4)
#     print(A)

# V3 
# Time:  O(n)
# Space: O(n)
# you may get a input like this,
# nums1 : [0]
# m : 0
# nums2 : [1]
# n : 1
# so you need to judge if n is still large than 0
class Solution2:
    def merge(self, nums1, m, nums2, n):
        # Space: O(n),
        # Reference:
        # - https://stackoverflow.com/questions/4948293/python-slice-assignment-memory-usage
        # - https://stackoverflow.com/questions/10623302/how-assignment-works-with-python-list-slice

        """
        :type nums1: List[int]
        :type m: int
        :type nums2: List[int]
        :type n: int
        :rtype: void Do not return anything, modify nums1 in-place instead.
        """
        while m > 0 and n > 0:
            if nums1[m-1] > nums2[n-1]:
                nums1[m+n-1] = nums1[m-1]
                m -= 1
            else:
                nums1[m+n-1] = nums2[n-1]
                n -= 1
        if n > 0:
            nums1[:n] = nums2[:n]  