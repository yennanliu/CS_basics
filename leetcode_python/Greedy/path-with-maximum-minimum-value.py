"""

# https://www.cnblogs.com/Dylan-Java-NYC/p/11297106.html
# https://www.cnblogs.com/rookielet/p/11146393.html

Given a matrix of integers A with R rows and C columns, find the maximum score of a path starting at [0,0] and ending at [R-1,C-1].

The score of a path is the minimum value in that path.  For example, the value of the path 8 →  4 →  5 →  9 is 4.

A path moves some number of times from one visited cell to any neighbouring unvisited cell in one of the 4 cardinal directions (north, east, west, south).

 

Example 1:



Input: [[5,4,5],[1,2,6],[7,4,6]]
Output: 4
Explanation: 
The path with the maximum score is highlighted in yellow. 
Example 2:



Input: [[2,2,1,2,2,2],[1,2,2,2,1,2]]
Output: 2
Example 3:



Input: [[3,4,6,3,4],[0,2,1,1,7],[8,8,3,2,7],[3,2,4,9,8],[4,1,2,0,0],[4,6,5,4,3]]
Output: 3
Note:

1 <= R, C <= 100
0 <= A[i][j] <= 10^9


"""

# V0

# V1
# IDEA : DFS

# V1'
# IDEA : greedy

# V1''
# IDEA : DP
# https://leetcode.com/discuss/interview-question/383669/Amazon-or-OAA-2019-or-Max-Min-Path-Value
class Solution(object):
    def minPathSum(self, grid):

        # handle invalid cases
        m = len(grid)
        n = len(grid[0]) if grid else 0
        if not m or m == 1 and n <= 1:
            return 0

        # create a dp variable of size m x n - initialized with 'infinity'
        dp = [[float('inf')] * n for _ in xrange(m)]
        
        for i in xrange(m):
            for j in xrange(n):
                if (i == 0 and j == 0):
                    # ignore the very first element of the matrix
                    continue
                elif i == 0:
                    # just check with previous column
                    dp[i][j] = min(dp[i][j-1], grid[i][j])
                elif j == 0:
                    # just check with previous row
                    dp[i][j] = min(dp[i-1][j], grid[i][j])
                elif i == m - 1 and j == n - 1:
                    # we are at the last element, so just check the max from DP
                    dp[i][j] = max(dp[i][j-1], dp[i-1][j])                    
                else:
                    # pick the max from the dp and compare that against current grid[i][j] for min
                    dp[i][j] = min(max(dp[i][j-1], dp[i-1][j]), grid[i][j])
                    
        if m == 1:
            return dp[0][n - 2]
        elif n == 1:
            return dp[m - 2][0]
        else:
            return dp[i][j]

# V1'
# IDEA : DP
# https://leetcode.com/discuss/interview-question/383669/Amazon-or-OAA-2019-or-Max-Min-Path-Value
class Solution:
    def sol(self, nums):
        
        N = len(nums)
        M = len(nums[0])

        nums[0][0] = 1e9
        nums[N - 1][M - 1] = 1e9

        dp = [[1e9] * M for i in range(N)]

        for j in range(1, M):
            dp[0][j] = min(dp[0][j - 1], nums[0][j])
        for i in range(1, N):
            dp[i][0] = min(dp[i - 1][0], nums[i][0])

        for i in range(1, N):
            for j in range(1, M):
                cur = max(dp[i - 1][j], dp[i][j - 1])
                dp[i][j] = min(cur, nums[i][j])
        #print(dp)

        print("ans: " + str(dp[N - 1][M - 1]))

# V1''
# https://www.cnblogs.com/rookielet/p/11146393.html
# https://www.codeprj.com/zh/blog/aa14991.html
# C++
# int[][] dirs = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
#     public int maximumMinimumPath(int[][] A) {
#         int m = A.length, n = A[0].length;
#         boolean[][] visited = new boolean[m][n];
#         PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (b[0] - a[0]));
#         pq.offer(new int[]{A[0][0], 0, 0});
#         int res = A[0][0];
#         visited[0][0] = true;
#       
#         while(!pq.isEmpty()) {
#             int[] top = pq.poll();
#             // System.out.println(top[0] + " " + top[1] + " " + top[2]);
#             res = Math.min(res, top[0]);
#             if(top[1] == m - 1 && top[2] == n - 1) {
#                 break;
#             }
#             for(int[] dir : dirs) {
#                 int x = dir[0] + top[1];
#                 int y = dir[1] + top[2];
#                 // System.out.println("can" + " " + x + " " + y);
#                 if(x < 0 || x >= m || y < 0 || y >= n || visited[x][y]) continue;
#                 // System.out.println("add" + A[x][y] + " " + x + " " + y);
#                 pq.offer(new int[]{A[x][y], x, y});
#                 visited[x][y] = true;
#             }
#         }
#         return res;
#     }


# V1'''
# IDEA : SORT + UNION FIND
# https://blog.csdn.net/fuxuemingzhu/article/details/101231938

# V1''''
# IDEA : PRIORITY QUEUE
# https://blog.csdn.net/fuxuemingzhu/article/details/101231938


# V2