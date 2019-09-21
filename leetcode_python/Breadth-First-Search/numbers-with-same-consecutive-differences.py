# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85387183
# https://zxi.mytechroad.com/blog/searching/967-numbers-with-same-consecutive-differences/
class Solution(object):
    def numsSameConsecDiff(self, N, K):
        """
        :type N: int
        :type K: int
        :rtype: List[int]
        """
        if N == 1:
            return [0, 1,2,3,4,5,6,7,8,9]
        res = []
        for i in range(1, 10):
            self.dfs(res, i, N - 1, K)
        return list(set(res))
        
    def dfs(self, res, curint, N, K):
        if N == 0:
            res.append(curint)
            return
        last = curint % 10
        if last + K <= 9:
            self.dfs(res, curint * 10 + last + K, N - 1, K)
        if last - K >= 0:
            self.dfs(res, curint * 10 + last - K, N - 1, K)

# V2 
# Time:  O(2^n)
# Space: O(2^n)
class Solution(object):
    def numsSameConsecDiff(self, N, K):
        """
        :type N: int
        :type K: int
        :rtype: List[int]
        """
        curr = range(10)
        for i in range(N-1):
            curr = [x*10 + y for x in curr for y in set([x%10 + K, x%10 - K]) 
                    if x and 0 <= y < 10]
        return curr