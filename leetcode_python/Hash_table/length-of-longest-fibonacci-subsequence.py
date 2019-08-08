# V0

# V1 
# http://bookshadow.com/weblog/2018/07/22/leetcode-length-of-longest-fibonacci-subsequence/
# IDEA : DP
# STATUS EQ : dp[y][x + y] = max(dp[y][x + y], dp[x][y] + 1)
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        vset = set(A)
        dp = collections.defaultdict(lambda: collections.defaultdict(int))
        size = len(A)
        ans = 0
        for i in range(size):
            x = A[i]
            for j in range(i + 1, size):
                y = A[j]
                if x + y not in vset: continue
                dp[y][x + y] = max(dp[y][x + y], dp[x][y] + 1)
                ans = max(dp[y][x + y], ans)
        return ans and ans + 2 or 0

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82715323
# IDEA : GREEDY 
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        s = set(A)
        n = len(A)
        res = 0
        for i in range(n - 1):
            for j in range(i + 1, n):
                a, b = A[i], A[j]
                count = 2
                while a + b in s:
                    a, b = b, a + b
                    count += 1
                    res = max(res, count)
        return res if res > 2 else 0
        
# V2 
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def lenLongestFibSubseq(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        lookup = set(A)
        result = 2
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                x, y, l = A[i], A[j], 2
                while x+y in lookup:
                    x, y, l = y, x+y, l+1
                result = max(result, l)
        return result if result > 2 else 0