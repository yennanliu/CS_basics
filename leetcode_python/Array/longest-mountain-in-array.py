# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83422260
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        inc = [1] * N
        dec = [1] * N
        for i in range(1, N):
            if A[i] - A[i - 1] > 0:
                inc[i] = inc[i - 1] + 1
        for i in range(N - 2, -1, -1):
            if A[i] - A[i + 1] > 0:
                dec[i] = dec[i + 1] + 1
        res = 0
        for i in range(1, N - 1):
            if inc[i] != 1 and dec[i] != 1:
                res = max(res, inc[i] + dec[i] - 1)
        return res

# V1'
# http://bookshadow.com/weblog/2018/06/03/leetcode-longest-mountain-in-array/
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        SA = len(A)
        left, right = [0] * SA, [0] * SA
        for x in range(1, SA):
            if A[x] > A[x - 1]:
                left[x] = left[x - 1] + 1
        ans = 0
        for x in range(SA - 2, -1, -1):
            if A[x] > A[x + 1]:
                right[x] = right[x + 1] + 1
            if left[x] and right[x]:
                ans = max(ans, left[x] + right[x] + 1)
        return ans
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        result, up_len, down_len = 0, 0, 0
        for i in range(1, len(A)):
            if (down_len and A[i-1] < A[i]) or A[i-1] == A[i]:
                up_len, down_len = 0, 0
            up_len += A[i-1] < A[i]
            down_len += A[i-1] > A[i]
            if up_len and down_len:
                result = max(result, up_len+down_len+1)
        return result