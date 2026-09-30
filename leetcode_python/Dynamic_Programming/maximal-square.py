"""

221. Maximal Square
Medium

Given an m x n binary matrix filled with 0's and 1's, find the largest square containing only 1's and return its area.

 

Example 1:


Input: matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
Output: 4
Example 2:


Input: matrix = [["0","1"],["1","0"]]
Output: 1
Example 3:

Input: matrix = [["0"]]
Output: 0
 

Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 300
matrix[i][j] is '0' or '1'.

"""

# V0
class Solution(object):
    def maximalSquare(self, matrix):
        """
        :type matrix: List[List[str]]
        :rtype: int
        """
        pass



# V0-1
# IDEA: 2D DP (GEMINI)
"""

NOTE !!!


1.


DP def:

    dp[i][j] 代表以 (i-1, j-1) 為右下角所能組成的最大正方形邊長



DP eq:
    
    if matrix[i - 1][j - 1] == "1":

    
          # 狀態轉移方程：由上方、左方、左上方的最小值決定當前能擴展的最大正方形邊長
          dp[i][j] = (
              min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1
          )
          # 持續更新全域見過的最大邊長
          max_side = max(max_side, dp[i][j])


----

2. 

why  dp[i][j] = (min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1)  ?


-> 

在我們設計的表格中，dp[i][j] 代表：「以矩陣中位置 (i-1, j-1) 作為正方形的『右下角』時，所能組成的最大正方形邊長」。

當我們當前的位置 matrix[i-1][j-1] 是 '1' 時，它想要組成更大的正方形，必須依賴它周遭的三個鄰居：



    - 上方 (dp[i-1][j])：代表往上延伸的潛在正方形邊長。

    - 左方 (dp[i][j-1])：代表往左延伸的潛在正方形邊長。

    - 左上方 (dp[i-1][j-1])：代表往斜上方延伸的潛在正方形邊長。


->

🧩 為什麼一定要取這三個方向的「最小值（min）」？

    -> 如果這三個方向提供的邊長不一樣大，
       當前格子要決定自己的最大邊長時，會受到限制最嚴格的那一個方向
       (最短的那塊木板）所侷限。

"""
class Solution(object):

  def maximalSquare(self, matrix):
    """:type matrix: List[List[str]] :rtype: int"""
    if not matrix or not matrix[0]:
      return 0

    rows = len(matrix)
    cols = len(matrix[0])

    # dp[i][j] 代表以 (i-1, j-1) 為右下角所能組成的最大正方形邊長
    # 多開一行一列 (Padding) 讓邊界處理變得很單純，預設值皆為 0
    dp = [[0] * (cols + 1) for _ in range(rows + 1)]
    
    max_side = 0

    for i in range(1, rows + 1):
      for j in range(1, cols + 1):
        
        # 記得 matrix 裡裝的是字串 '1'，不是整數 1
        if matrix[i - 1][j - 1] == "1":
          
          # 狀態轉移方程：由上方、左方、左上方的最小值決定當前能擴展的最大正方形邊長
          dp[i][j] = (
              min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1
          )
          
          # 持續更新全域見過的最大邊長
          max_side = max(max_side, dp[i][j])

    # 題目要求回傳的是「面積」（邊長的平方）
    return max_side * max_side


# V0-2
# IDEA: 2D DP (gpt)
class Solution(object):
    def maximalSquare(self, matrix):
        """
        :type matrix: List[List[str]]
        :rtype: int
        """

        # Edge case
        if not matrix or len(matrix) == 0:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        # dp[i][j] = largest square side length
        # whose bottom-right corner is at (i - 1, j - 1)
        #
        # Extra row and column make boundary handling easier.
        dp = [
            [0] * (cols + 1)
            for _ in range(rows + 1)
        ]

        max_side = 0

        for i in range(1, rows + 1):
            for j in range(1, cols + 1):

                # Matrix contains strings: "0" / "1"
                if matrix[i - 1][j - 1] == "1":

                    # Current cell can form a square.
                    #
                    # The square size is limited by the
                    # smallest of:
                    #   top
                    #   left
                    #   top-left
                    dp[i][j] = min(
                        dp[i - 1][j],
                        dp[i][j - 1],
                        dp[i - 1][j - 1]
                    ) + 1

                    max_side = max(
                        max_side,
                        dp[i][j]
                    )

        # Return area
        return max_side * max_side



# V1 
# http://bookshadow.com/weblog/2015/06/03/leetcode-maximal-square/
# dynamic programming state equation :
# dp[x][y] = min(dp[x - 1][y - 1], dp[x][y - 1], dp[x - 1][y]) + 1
"""

DP def
    dp[x][y]: the SIDE LENGTH of the largest all-1 square whose

              BOTTOM-RIGHT corner is (x, y)

              -> 0 when matrix[x][y] == '0'

DP eq

     if matrix[x][y] == '1':

        dp[x][y] = min( dp[x-1][y-1], dp[x][y-1], dp[x-1][y] ) + 1


    -> e.g. the three neighbours are the squares ending just above, just
              left, and diagonally up-left; a square of side s+1 needs ALL
              THREE to already reach s - hence the MIN

     init: first row / column = int(matrix[x][y])
     ans = max(dp)^2      (the question asks for the AREA)

"""
# time = O(m*n)
# space = O(m*n)
class Solution:
    # @param {character[][]} matrix
    # @return {integer}
    def maximalSquare(self, matrix):
        if matrix == []:
            return 0
        m, n = len(matrix), len(matrix[0])
        dp = [[0] * n for x in range(m)]
        ans = 0
        for x in range(m):
            for y in range(n):
                dp[x][y] = int(matrix[x][y])
                if x and y and dp[x][y]:
                    dp[x][y] = min(dp[x - 1][y - 1], dp[x][y - 1], dp[x - 1][y]) + 1
                ans = max(ans, dp[x][y])
        return ans * ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82992233
"""

DP def
    dp[x][y]: the SIDE LENGTH of the largest all-1 square whose

              BOTTOM-RIGHT corner is (x, y)

              -> 0 when matrix[x][y] == '0'

DP eq

     if matrix[x][y] == '1':

        dp[x][y] = min( dp[x-1][y-1], dp[x][y-1], dp[x-1][y] ) + 1


    -> e.g. the three neighbours are the squares ending just above, just
              left, and diagonally up-left; a square of side s+1 needs ALL
              THREE to already reach s - hence the MIN

     init: first row / column = int(matrix[x][y])
     ans = max(dp)^2      (the question asks for the AREA)

"""
# time = O(m*n)
# space = O(m*n)
class Solution(object):
    def maximalSquare(self, matrix):
        """
        :type matrix: List[List[str]]
        :rtype: int
        """
        if not matrix: return 0
        M = len(matrix)
        N = len(matrix[0])
        dp = [[0] * N for _ in range(M)]
        for i in range(M):
            dp[i][0] = int(matrix[i][0])
        for j in range(N):
            dp[0][j] = int(matrix[0][j])
        for i in range(1, M):
            for j in range(1, N):
                if int(matrix[i][j]) == 1:
                    dp[i][j] = min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]) + 1
        return max(map(max, dp)) ** 2

# V2 
"""

DP def
    dp[x][y]: the SIDE LENGTH of the largest all-1 square whose

              BOTTOM-RIGHT corner is (x, y)

              -> 0 when matrix[x][y] == '0'

DP eq

     if matrix[x][y] == '1':

        dp[x][y] = min( dp[x-1][y-1], dp[x][y-1], dp[x-1][y] ) + 1


    -> e.g. the three neighbours are the squares ending just above, just
              left, and diagonally up-left; a square of side s+1 needs ALL
              THREE to already reach s - hence the MIN

     init: first row / column = int(matrix[x][y])
     ans = max(dp)^2      (the question asks for the AREA)

"""
# time = O(n^2)
# space = O(n)
class Solution(object):
    # @param {character[][]} matrix
    # @return {integer}
    def maximalSquare(self, matrix):
        if not matrix:
            return 0

        m, n = len(matrix), len(matrix[0])
        size = [[0 for j in range(n)] for i in range(2)]
        max_size = 0

        for j in range(n):
            if matrix[0][j] == '1':
                size[0][j] = 1
            max_size = max(max_size, size[0][j])

        for i in range(1, m):
            if matrix[i][0] == '1':
                size[i % 2][0] = 1
            else:
                size[i % 2][0] = 0
            for j in range(1, n):
                if matrix[i][j] == '1':
                    size[i % 2][j] = min(size[i % 2][j - 1], \
                                         size[(i - 1) % 2][j], \
                                         size[(i - 1) % 2][j - 1]) + 1
                    max_size = max(max_size, size[i % 2][j])
                else:
                    size[i % 2][j] = 0

        return max_size * max_size

# time = O(n^2)
# space = O(n^2)
# DP.
class Solution2(object):
    # @param {character[][]} matrix
    # @return {integer}
    def maximalSquare(self, matrix):
        if not matrix:
            return 0

        m, n = len(matrix), len(matrix[0])
        size = [[0 for j in range(n)] for i in range(m)]
        max_size = 0

        for j in range(n):
            if matrix[0][j] == '1':
                size[0][j] = 1
            max_size = max(max_size, size[0][j])

        for i in range(1, m):
            if matrix[i][0] == '1':
                size[i][0] = 1
            else:
                size[i][0] = 0
            for j in range(1, n):
                if matrix[i][j] == '1':
                    size[i][j] = min(size[i][j - 1],  \
                                     size[i - 1][j],  \
                                     size[i - 1][j - 1]) + 1
                    max_size = max(max_size, size[i][j])
                else:
                    size[i][j] = 0

        return max_size * max_size

# V2'
"""

DP def
    dp[x][y]: the SIDE LENGTH of the largest all-1 square whose

              BOTTOM-RIGHT corner is (x, y)

              -> 0 when matrix[x][y] == '0'

DP eq

     if matrix[x][y] == '1':

        dp[x][y] = min( dp[x-1][y-1], dp[x][y-1], dp[x-1][y] ) + 1


    -> e.g. the three neighbours are the squares ending just above, just
              left, and diagonally up-left; a square of side s+1 needs ALL
              THREE to already reach s - hence the MIN

     init: first row / column = int(matrix[x][y])
     ans = max(dp)^2      (the question asks for the AREA)

"""
# time = O(m*n)
# space = O(m*n)
class Solution3(object):
    # @param {character[][]} matrix
    # @return {integer}
    def maximalSquare(self, matrix):
        if not matrix:
            return 0

        H, W = 0, 1
        # DP table stores (h, w) for each (i, j).
        table = [[[0, 0] for j in range(len(matrix[0]))] \
                         for i in range(len(matrix))]
        for i in reversed(range(len(matrix))):
            for j in reversed(range(len(matrix[i]))):
                # Find the largest h such that (i, j) to (i + h - 1, j) are feasible.
                # Find the largest w such that (i, j) to (i, j + w - 1) are feasible.
                if matrix[i][j] == '1':
                    h, w = 1, 1
                    if i + 1 < len(matrix):
                        h = table[i + 1][j][H] + 1
                    if j + 1 < len(matrix[i]):
                        w = table[i][j + 1][W] + 1
                    table[i][j] = [h, w]

        # A table stores the length of largest square for each (i, j).
        s = [[0 for j in range(len(matrix[0]))] \
                for i in range(len(matrix))]
        max_square_area = 0
        for i in reversed(range(len(matrix))):
            for j in reversed(range(len(matrix[i]))):
                side = min(table[i][j][H], table[i][j][W])
                if matrix[i][j] == '1':
                    # Get the length of largest square with bottom-left corner (i, j).
                    if i + 1 < len(matrix) and j + 1 < len(matrix[i + 1]):
                        side = min(s[i + 1][j + 1] + 1, side)
                    s[i][j] = side
                    max_square_area = max(max_square_area, side * side)

        return max_square_area