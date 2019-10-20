# V0

# V1 
# http://bookshadow.com/weblog/2016/12/11/leetcode-ones-and-zeroes/
# IDEA : DP
# DP STATUS EQUATION:
# for s in strs:
#     zero, one = s.count('0'), s.count('1')
#     for x in range(m, zero - 1, -1):
#         for y in range(n, one - 1, -1):
#             dp[x][y] = max(dp[x - zero][y - one] + 1, dp[x][y])        
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[0] * (n + 1) for x in range(m + 1)]
        for s in strs:
            zero, one = s.count('0'), s.count('1')
            for x in range(m, zero - 1, -1):
                for y in range(n, one - 1, -1):
                    dp[x][y] = max(dp[x - zero][y - one] + 1, dp[x][y])
        return dp[m][n]
        
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82825032
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        # m of 0, n of 1 that can be collected as longest string 
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        for str in strs:
            zeros, ones = 0, 0
            for c in str:
                if c == "0":
                    zeros += 1
                elif c == "1":
                    ones += 1
            for i in range(m, zeros - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
        return dp[m][n]

# V1''
# https://www.jiuzhang.com/solution/ones-and-zeroes/#tag-highlight-lang-python
# IDEA : DP
class Solution:
    """
    @param strs: an array with strings include only 0 and 1
    @param m: An integer
    @param n: An integer
    @return: find the maximum number of strings
    """
    def findMaxForm(self, strs, m, n):
        # write your code here
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        for s in strs:
            zero = 0
            one = 0
            for ch in s:
                if ch == "1":
                    one += 1
                else:
                    zero += 1
            for i in range(n,one - 1,-1):
                for j in range(m,zero - 1,-1):
                    if dp[i - one][j - zero] + 1 > dp[i][j]:
                        dp[i][j] = dp[i - one][j - zero] + 1
        return dp[-1][-1]

# V2 
# Time:  O(s * m * n), s is the size of the array.
# Space: O(m * n)
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[0 for _ in range(n+1)] for _ in range(m+1)]
        for s in strs:
            zero_count, one_count = 0, 0
            for c in s:
                if c == '0':
                    zero_count += 1
                elif c == '1':
                    one_count += 1

            for i in reversed(range(zero_count, m+1)):
            	for j in reversed(range(one_count, n+1)):
                    dp[i][j] = max(dp[i][j], dp[i-zero_count][j-one_count]+1)
        return dp[m][n]
