# V1 : DEV 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/84998077
class Solution(object):
    def smallestRangeII(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        A.sort()
        N = len(A)
        mn, mx = A[0], A[-1]
        res = mx - mn
        for i in range(N - 1):
            mx = max(A[i] + 2 * K, mx)
            mn = min(A[i + 1], A[0] + 2 * K)
            res = min(mx - mn, res)
        return res

# V3 
# Time:  O(nlogn)
# Space: O(1)

class Solution(object):
    def smallestRangeII(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        A.sort()
        result = A[-1]-A[0]
        for i in range(len(A)-1):
            result = min(result,
                         max(A[-1]-K, A[i]+K) -
                         min(A[0]+K, A[i+1]-K))
        return result
