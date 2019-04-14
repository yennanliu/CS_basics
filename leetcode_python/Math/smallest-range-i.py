# V1 : DEV 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/82824685
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        return max(max(A) - min(A) - 2 * K, 0)

# V3 
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