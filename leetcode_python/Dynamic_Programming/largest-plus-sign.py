# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82725695
class Solution:
    def orderOfLargestPlusSign(self, N, mines):
        """
        :type N: int
        :type mines: List[List[int]]
        :rtype: int
        """
        res = 0
        dp = [[0 for i in range(N)] for j in range(N)]
        s = set()
        for mine in mines:
            s.add(N * mine[0] + mine[1])
        for i in range(N):
            cnt = 0
            for j in range(N):#left
                cnt = 0 if N * i + j in s else cnt + 1
                dp[i][j] = cnt
            cnt = 0
            for j in range(N - 1, -1, -1):#right
                cnt = 0 if N * i + j in s else cnt + 1
                dp[i][j] = min(dp[i][j], cnt)
        for j in range(N):
            cnt = 0
            for i in range(N):#up
                cnt = 0 if N * i + j in s else cnt + 1
                dp[i][j] = min(dp[i][j], cnt)
            cnt = 0
            for i in range(N - 1, -1, -1):#down
                cnt = 0 if N * i + j in s else cnt + 1
                dp[i][j] = min(dp[i][j], cnt)
                res = max(dp[i][j], res)
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82725695
class Solution:
    def orderOfLargestPlusSign(self, N, mines):
        """
        :type N: int
        :type mines: List[List[int]]
        :rtype: int
        """
        res = 0
        dp = [[N for i in range(N)] for j in range(N)]
        s = set()
        for mine in mines:
            dp[mine[0]][mine[1]] = 0
        for i in range(N):
            l, r, u, d = 0, 0, 0, 0
            for j in range(N):
                l = l + 1 if dp[i][j] else 0
                r = r + 1 if dp[j][i] else 0
                u = u + 1 if dp[i][N - 1 -j] else 0
                d = d + 1 if dp[N - 1 - j][i] else 0
                dp[i][j] = min(dp[i][j], l)
                dp[j][i] = min(dp[j][i], r)
                dp[i][N - 1 - j] = min(dp[i][N -  1 - j], u)
                dp[N - 1 - j][i] = min(dp[N - 1 - j][i], d)
        for i in range(N):
            for j in range(N):
                res = max(res, dp[i][j])
        return res
        
# V1''
# https://www.jiuzhang.com/solution/largest-plus-sign/#tag-highlight-lang-python
class Solution(object):
    def orderOfLargestPlusSign(self, N, mines):
        """
        :type N: int
        :type mines: List[List[int]]
        :rtype: int
        """
        # Methods to reduce computational time:
        # 1. convert mines from list to dictionary. O(n^3) to O(n^2)
        # 2. use try/except instead of if else to deal with matrix boundary
        
        L  = [[0 for x in range(N)] for y in range(N)] #L[i][j]: number of continuous 1 from L[i][j] towards its left, L[i][j] included. 
        R  = [[0 for x in range(N)] for y in range(N)] #Right
        U  = [[0 for x in range(N)] for y in range(N)] #Up
        D  = [[0 for x in range(N)] for y in range(N)] #Down
 
        dicMines = {(mine[0], mine[1]) for mine in mines} # convert mines from list to dictionary
        
        # calcuate L and D in O(n^2) time
        for i in range(N):
            for j in range(N):
                if (i,j) not in dicMines:
                    # use try/except instead of if/else to reduce time, if/else method is presented below as well
                    try: L[i][j] = L[i][j-1] + 1
                    except Exception: L[i][j] = 1
                    try: D[i][j] = D[i-1][j] + 1
                    except Exception: D[i][j] = 1
                        
                    #if/else method presented for reference
                    #L[i][j] = L[i][j-1] + 1 if j > 0 else 1
                    #D[i][j] = D[i-1][j] + 1 if i > 0 else 1
                  
        # calcuate R and U in O(n^2) time
        for i in range (N-1, -1, -1):
            for j in range(N-1, -1, -1):
                if (i,j) not in dicMines:
                    try: R[i][j] = R[i][j+1] + 1
                    except Exception: R[i][j] = 1
                    try: U[i][j] = U[i+1][j] + 1
                    except Exception: U[i][j] = 1

                    #R[i][j] = R[i][j+1] + 1 if j < N-1 else 1
                    #U[i][j] = U[i+1][j] + 1 if i < N-1 else 1
        MaxK = 0
        MaxK = max(min(L[i][j], R[i][j], U[i][j], D[i][j]) for i in range(N) for j in range(N))
        return (MaxK)

# V2
# Time:  O(n^2)
# Space: O(n^2)
class Solution(object):
    def orderOfLargestPlusSign(self, N, mines):
        """
        :type N: int
        :type mines: List[List[int]]
        :rtype: int
        """
        lookup = {tuple(mine) for mine in mines}
        dp = [[0] * N for _ in range(N)]
        result = 0
        for i in range(N):
            l = 0
            for j in range(N):
                l = 0 if (i, j) in lookup else l+1
                dp[i][j] = l
            l = 0
            for j in reversed(range(N)):
                l = 0 if (i, j) in lookup else l+1
                dp[i][j] = min(dp[i][j], l)

        for j in range(N):
            l = 0
            for i in range(N):
                l = 0 if (i, j) in lookup else l+1
                dp[i][j] = min(dp[i][j], l)
            l = 0
            for i in reversed(range(N)):
                l = 0 if (i, j) in lookup else l+1
                dp[i][j] = min(dp[i][j], l)
                result = max(result, dp[i][j])
        return result