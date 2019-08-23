# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82824685
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        return max(max(A) - min(A) - 2 * K, 0)
        
# V1'
# https://www.cnblogs.com/seyjs/p/9692725.html
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        minv,maxv = A[0] + K, A[0] - K
        for i in A:
            minv = min(minv,i+K)
            maxv = max(maxv,i-K)
        return max(0,maxv-minv)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        return max(0, max(A) - min(A) - 2*K)