"""

# https://kennyzhuang.gitbooks.io/leetcode-lock/content/308_range_sum_query_2d_-_mutable.html

308: Range Sum Query 2D - Mutable

Given a 2D matrix matrix, find the sum of the elements inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (row2, col2).

Range Sum Query 2D

The above rectangle (with the red border) is defined by (row1, col1) = (2, 1) and (row2, col2) = (4, 3), which contains sum = 8.

Example:

Given matrix = [
  [3, 0, 1, 4, 2],
  [5, 6, 3, 2, 1],
  [1, 2, 0, 1, 5],
  [4, 1, 0, 1, 7],
  [1, 0, 3, 0, 5]
]
sumRegion(2, 1, 4, 3) -> 8

update(3, 2, 2)

sumRegion(2, 1, 4, 3) -> 10

Note:

The matrix is only modifiable by the update function.

You may assume the number of calls to update and sumRegion function is distributed evenly.

You may assume that row1 ≤ row2 and col1 ≤ col2.

"""

# V0

# V1
# IDEA : matrix op + pre-sum
# https://liyao13.wordpress.com/2018/02/09/leetcode-308-range-sum-query-2d-mutable/
class NumMatrix(object):
    """
    Your NumMatrix object will be instantiated and called as such:
    obj = NumMatrix(matrix)
    obj.update(row,col,val)
    param_2 = obj.sumRegion(row1,col1,row2,col2)

    https://leetcode.com/problems/range-sum-query-2d-mutable/discuss/75872/Python-94.5-Simple-sum-array-on-one-dimension-O(n)-for-both-update-and-sum

    beats 83.86%
    """

    def __init__(self, matrix):
        """
        :type matrix: List[List[int]]

        element m[i][j] in self.matrix means sum of previous elements in this row,
        namely sum(m[i][0] + m[i][1] + ... + m[i][j])
        """
        for row in matrix:
            for col in range(1, len(row)):
                row[col] += row[col - 1]
        self.matrix = matrix

    def update(self, row, col, val):
        """
        original means the single element value in original matrix
        """
        original = self.matrix[row][col]
        if col != 0:
            original -= self.matrix[row][col - 1]

        diff = original - val

        for y in range(col, len(self.matrix[0])):  # update elements in the row
            self.matrix[row][y] -= diff

    def sumRegion(self, row1, col1, row2, col2):
        """
        region sum is the sum of range sum in every row from row1 to row2
        as the above definition of self.matrix
        each range sum could be calculated as (m[row][col2] - m[row][col1 - 1])
        """
        region_sum = 0
        for x in range(row1, row2 + 1):
            region_sum += self.matrix[x][col2]
            if col1 != 0:
                region_sum -= self.matrix[x][col1 - 1]
        return region_sum

# V1'
# https://github.com/GJzh/Leetcode/blob/master/python/308%20Range%20Sum%20Query%202D%20-%20Mutable.py
class NumMatrix(object):

    def __init__(self, matrix):
        """
        :type matrix: List[List[int]]
        """
        m = len(matrix)
        if m == 0: return
        n = len(matrix[0])
        if n == 0: return
        self.matrix = [[0 for j in range(n)] for i in range(m)]
        self.BIT2D = [[0 for j in range(n+1)] for i in range(m+1)]
        for i in range(m):
            for j in range(n):
                self.update(i, j, matrix[i][j])

    def update(self, row, col, val):
        """
        :type row: int
        :type col: int
        :type val: int
        :rtype: void
        """
        m = len(self.matrix)
        n = len(self.matrix[0])
        diff = val - self.matrix[row][col]
        self.matrix[row][col] = val
        i = row + 1
        while i <= m:
            j = col + 1
            while j <= n:
                self.BIT2D[i][j] += diff
                j += (j & (-j))
            i += (i & (-i))

    def getSum(self, row, col):
        ans = 0
        i = row + 1
        while i > 0:
            j = col + 1
            while j > 0:
                ans += self.BIT2D[i][j]
                j -= (j & (-j))
            i -= (i & (-i))
        return ans
            
    def sumRegion(self, row1, col1, row2, col2):
        """
        :type row1: int
        :type col1: int
        :type row2: int
        :type col2: int
        :rtype: int
        """
        return self.getSum(row2, col2) - self.getSum(row2, col1-1) - self.getSum(row1-1, col2) + self.getSum(row1-1, col1-1)
 
# V1'''
# https://github.com/clairett/Leetcode-Lintcode-Python/blob/master/308.py
class NumMatrix:

    def __init__(self, matrix: List[List[int]]):
        if not matrix or not matrix[0]: return
        self.m, self.n = len(matrix), len(matrix[0])
        self.matrix = [[0] * self.n for _ in range(self.m)]
        self.tree = [[0] * (self.n + 1) for _ in range(self.m + 1)]
        i = 1
        while i <= self.m:
            j = 1
            while j <= self.n:
                self.update(i - 1, j - 1, matrix[i - 1][j - 1])
                j += 1
            i += 1

    def update(self, row: int, col: int, val: int) -> None:
        diff, self.matrix[row][col], i = val - self.matrix[row][col], val, row + 1
        while i <= self.m:
            j = col + 1
            while j <= self.n:
                self.tree[i][j] += diff
                j += (j & -j)
            i += (i & -i)

    def sumRegion(self, row1: int, col1: int, row2: int, col2: int) -> int:
        return self.getSum(row2, col2) - self.getSum(row2, col1 - 1) - self.getSum(row1 - 1, col2) + self.getSum(
            row1 - 1, col1 - 1)

    def getSum(self, row, col):
        result, i = 0, row + 1
        while i > 0:
            j = col + 1
            while j > 0:
                result += self.tree[i][j]
                j -= (j & -j)
            i -= (i & -i)
        return result

# V1''
# https://leetcode.jp/leetcode-308-range-sum-query-2d-mutable-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
# JAVA
# int[][] presum; // 前缀和数组
# int[][] m; // 原数组
# public NumMatrix(int[][] matrix) {
#     m=matrix;
#     if(m.length==0) return;
#     // 初始化前缀和数组
#     presum=new int[matrix.length][matrix[0].length];
#     // 计算所有前缀和
#     for(int r=0;r<matrix.length;r++){
#         int sum=0;
#         for(int c=0;c<matrix[0].length;c++){
#             sum+=matrix[r][c];
#             presum[r][c]=sum+(r>0?presum[r-1][c]:0);
#         }
#     }
# }
#
# public void update(int row, int col, int val) {
#     // 查看更新后值的变化
#     int diff=val-m[row][col];
#     // 更新元素组中对应的数值
#     m[row][col]=val;
#     // 更新前缀和数组中的值
#     for(int r=row;r<m.length;r++){
#         for(int c=col;c<m[0].length;c++){
#             presum[r][c]+=diff;
#         }
#     }
# }
#
# public int sumRegion(int row1, int col1, int row2, int col2) {
#     // 利用前缀和计算当前区域和
#     return presum[row2][col2]
#         - (row1>0?presum[row1-1][col2]:0)
#         - (col1>0?presum[row2][col1-1]:0)
#         + (row1>0&&col1>0?presum[row1-1][col1-1]:0);
# }

# V1'''
# https://kennyzhuang.gitbooks.io/leetcode-lock/content/308_range_sum_query_2d_-_mutable.html
# JAVA
# public class NumMatrix {
#
#     int[][] tree;
#     int[][] nums;
#     int m;
#     int n;
#
#     public NumMatrix(int[][] matrix) {
#         if (matrix.length == 0 || matrix[0].length == 0) return;
#         m = matrix.length;
#         n = matrix[0].length;
#         tree = new int[m+1][n+1];
#         nums = new int[m][n];
#         for (int i = 0; i < m; i++) {
#             for (int j = 0; j < n; j++) {
#                 update(i, j, matrix[i][j]);
#             }
#         }
#     }
#
#     public void update(int row, int col, int val) {
#         if (m == 0 || n == 0) return;
#         int delta = val - nums[row][col];
#         nums[row][col] = val;
#         for (int i = row + 1; i <= m; i += i & (-i)) {
#             for (int j = col + 1; j <= n; j += j & (-j)) {
#                 tree[i][j] += delta;
#             }
#         }
#     }
#
#     public int sumRegion(int row1, int col1, int row2, int col2) {
#         if (m == 0 || n == 0) return 0;
#         return sum(row2+1, col2+1) + sum(row1, col1) - sum(row1, col2+1) - sum(row2+1, col1);
#     }
#
#     public int sum(int row, int col) {
#         int sum = 0;
#         for (int i = row; i > 0; i -= i & (-i)) {
#             for (int j = col; j > 0; j -= j & (-j)) {
#                 sum += tree[i][j];
#             }
#         }
#         return sum;
#     }
# }

# V2