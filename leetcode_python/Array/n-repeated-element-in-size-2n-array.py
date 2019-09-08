# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85223404
import collections
class Solution(object):
    def repeatedNTimes(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A) / 2
        count = collections.Counter(A)
        for k, v in count.items():
            if v == N:
                return k
        return 0

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def repeatedNTimes(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        for i in range(2, len(A)):
            if A[i-1] == A[i] or A[i-2] == A[i]:
                return A[i]
        return A[0]