# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83444553
class Solution(object):
    def champagneTower(self, poured, query_row, query_glass):
        """
        :type poured: int
        :type query_row: int
        :type query_glass: int
        :rtype: float
        """
        N = 100
        dp = [[0] * N for _ in range(N)]
        dp[0][0] = poured
        for i in range(query_row):
            for j in range(i + 1):
                if dp[i][j] > 1:
                    dp[i + 1][j    ] += (dp[i][j] - 1) / 2.0
                    dp[i + 1][j + 1] += (dp[i][j] - 1) / 2.0
        return min(1, dp[query_row][query_glass])
        
# V1'
# https://www.jiuzhang.com/solution/champagne-tower/#tag-highlight-lang-python
class Solution:
    """
    @param poured: an integer
    @param query_row: an integer
    @param query_glass: an integer
    @return: return a double
    """
    def champagneTower(self, poured, query_row, query_glass):
        # write your code here
        res = [[0.0 for _ in range(i)] for i in range(1, query_row + 2)]
        res[0][0] = poured
        
        for i in range(query_row):
            for j in range(len(res[i])):
                if res[i][j] > 1 :
                    res[i+1][j] += (res[i][j] - 1) / 2.0
                    res[i+1][j+1] += (res[i][j] - 1) / 2.0
        
        return res[query_row][query_glass] if res[query_row][query_glass] <= 1 else 1

# V2
# Time:  O(n^2) = O(1), since n is at most 99
# Space: O(n) = O(1)
class Solution(object):
    def champagneTower(self, poured, query_row, query_glass):
        """
        :type poured: int
        :type query_row: int
        :type query_glass: int
        :rtype: float
        """
        result = [poured] + [0] * query_row
        for i in range(1, query_row+1):
            for j in reversed(range(i+1)):
                result[j] = max(result[j]-1, 0)/2.0 + \
                            max(result[j-1]-1, 0)/2.0
        return min(result[query_glass], 1)