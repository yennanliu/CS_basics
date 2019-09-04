# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83539773
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        res = [1]
        while len(res) < N:
            res = [2 * i - 1 for i in res] + [2 * i  for i in res]
        return [i for i in res if i <= N]
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83539773
# IDEA : ITERATION 
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        if N == 1: return [1]
        odd = [i * 2 - 1 for i in self.beautifulArray(N / 2 + N % 2)]
        even = [i * 2 for i in self.beautifulArray(N / 2)]
        return odd + even

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        result = [1]
        while len(result) < N:
            result = [i*2 - 1 for i in result] + [i*2 for i in result]
        return [i for i in result if i <= N]