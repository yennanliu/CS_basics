"""

1000. Minimum Cost to Merge Stones
Hard

There are n piles of stones arranged in a row. The ith pile has stones[i] stones.

A move consists of merging exactly k consecutive piles into one pile, and the cost of this move is equal to the total number of stones in these k piles.

Return the minimum cost to merge all piles of stones into one pile. If it is impossible, return -1.

 

Example 1:

Input: stones = [3,2,4,1], k = 2
Output: 20
Explanation: We start with [3, 2, 4, 1].
We merge [3, 2] for a cost of 5, and we are left with [5, 4, 1].
We merge [4, 1] for a cost of 5, and we are left with [5, 5].
We merge [5, 5] for a cost of 10, and we are left with [10].
The total cost was 20, and this is the minimum possible.
Example 2:

Input: stones = [3,2,4,1], k = 3
Output: -1
Explanation: After any merge operation, there are 2 piles left, and we can't merge anymore.  So the task is impossible.
Example 3:

Input: stones = [3,5,1,2,6], k = 3
Output: 25
Explanation: We start with [3, 5, 1, 2, 6].
We merge [5, 1, 2] for a cost of 8, and we are left with [3, 8, 6].
We merge [3, 8, 6] for a cost of 17, and we are left with [17].
The total cost was 25, and this is the minimum possible.
 

Constraints:

n == stones.length
1 <= n <= 30
1 <= stones[i] <= 100
2 <= k <= 30

"""

# V0

# V1
# https://leetcode.com/problems/minimum-cost-to-merge-stones/discuss/250504/Concise-Python-3
# IDEA :
# Create prefix table. I used python "reduce" method. Same if using "for" loop. With prefix table, cost from i to j could be calculated in O(1) time.
# Simply use dfs to loop through all possibilites merging between index i and j into m piles of stones.
# "@functools.lrc_cache(None)" caches the execution result for (i,j,m) input, so that the complexity level could be largely reduced
class Solution:
    def mergeStones(self, stones: List[int], K: int) -> int:
        p = functools.reduce(lambda s,x: s+[s[-1]+x],stones,[0])
        @functools.lru_cache(None)
        def dfs(i,j,m):
            if (j-i+1-m)%(K-1): return math.inf
            if i==j: return 0 if m==1 else math.inf
            if m==1: return dfs(i,j,K)+p[j+1]-p[i]
            return min(dfs(i,k,1)+dfs(k+1,j,m-1) for k in range(i,j))
        res = dfs(0,len(stones)-1,1)
        return res if res != math.inf else -1

# V1'
# IDEA : 1D DP
# https://leetcode.com/problems/minimum-cost-to-merge-stones/discuss/247567/JavaC%2B%2BPython-DP
class Solution:
    def mergeStones(self, stones, K):
        n = len(stones)
        inf = float('inf')
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + stones[i]

        import functools

        @functools.lru_cache(None)
        def dp(i, j, m):
            if (j - i + 1 - m) % (K - 1):
                return inf
            if i == j:
                return 0 if m == 1 else inf
            if m == 1:
                return dp(i, j, K) + prefix[j + 1] - prefix[i]
            return min(dp(i, mid, 1) + dp(mid + 1, j, m - 1) for mid in range(i, j, K - 1))
        res = dp(0, n - 1, 1)
        return res if res < inf else -1

# V1''
# IDEA : 2D DP
# https://leetcode.com/problems/minimum-cost-to-merge-stones/discuss/247567/JavaC%2B%2BPython-DP
# JAVA
# public int mergeStones(int[] stones, int K) {
#     int n = stones.length;
#     if ((n - 1) % (K - 1) > 0) return -1;
#
#     int[] prefix = new int[n+1];
#     for (int i = 0; i <  n; i++)
#         prefix[i + 1] = prefix[i] + stones[i];
#
#     int[][] dp = new int[n][n];
#     for (int m = K; m <= n; ++m)
#         for (int i = 0; i + m <= n; ++i) {
#             int j = i + m - 1;
#             dp[i][j] = Integer.MAX_VALUE;
#             for (int mid = i; mid < j; mid += K - 1)
#                 dp[i][j] = Math.min(dp[i][j], dp[i][mid] + dp[mid + 1][j]);
#             if ((j - i) % (K - 1) == 0)
#                 dp[i][j] += prefix[j + 1] - prefix[i];
#         }
#     return dp[0][n - 1];
# }

# V1'''
# IDEA : DP
class Solution:
    def mergeStones(self, stones: List[int], K: int) -> int:
            def recursive(i, j, piles):
                if i == j and piles == 1:
                    return 0
                ''' within recursive we only make new calls for answerable subproblems
                if (j - i + 1 - piles) % (K - 1) != 0: 
                    return float('inf')  # means impossible
                '''
                if (i, j, piles) in dp:
                    return dp[(i, j, piles)]
                if piles == 1:
                    dp[(i,j,piles)] = recursive(i, j, K) + pre_sum[j+1] - pre_sum[i]
                    return dp[(i,j,piles)]
                else:
                    min_cost = float('inf')
                    for k in range(i, j, K - 1):
                        min_cost = min(min_cost, recursive(i, k, 1) + recursive(k + 1, j, piles - 1))
                    dp[(i, j, piles)] = min_cost
                    return dp[(i, j, piles)]

            n = len(stones)
            if (n - 1) % (K - 1) != 0:
                return -1
            pre_sum = [0] * (n + 1)
            for i in range(n):
                pre_sum[i + 1] = pre_sum[i] + stones[i]
            dp = {}
            return recursive(0, n - 1, 1)

# V1''''
# IDEA : DP TOP DOWN
# https://leetcode.com/problems/minimum-cost-to-merge-stones/discuss/424194/Python-top-down-dp-solution
class Solution:
    def mergeStones(self, s, K):
        N = len(s)
        if (N-1) % (K-1): return -1
        preS= [0] * (N+1)
        for i in range(1,N+1):
            preS[i]=s[i-1]+preS[i-1]
        M = [[float('inf')]*N for _ in range(N)]
        def dp(i, j):
            if M[i][j] == float('inf'):
                if j - i + 1 < K:
                    M[i][j] = 0
                else:
                    M[i][j]=min(dp(i, k) + dp(k+1,j) for k in range(i, j, K - 1))
                    if (j-i)%(K-1)==0:
                        M[i][j]+=preS[j+1]-preS[i]
            return M[i][j]
        return dp(0,N-1)

# V2