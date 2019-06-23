# V0  : DEV 

# V1 
# http://bookshadow.com/weblog/2015/11/12/leetcode-range-sum-query-2d-immutable/
# idea : 
# sumRange(row1, col1, row2, col2) = sums[row2][col2] + sums[row1 - 1][col1 - 1] - sums[row1 - 1][col2] - sums[row2][col1 - 1]
class NumMatrix(object):
    def __init__(self, matrix):
        """
        initialize your data structure here.
        :type matrix: List[List[int]]
        """
        m = len(matrix)
        n = len(matrix[0]) if m else 0
        self.sums = [[0] * (n + 1) for x in range(m + 1)]
        for x in range(1, m + 1):
            rowSum = 0
            for y in range(1, n + 1):
                self.sums[x][y] += rowSum + matrix[x - 1][y - 1]
                if x > 1:
                    self.sums[x][y] += self.sums[x - 1][y]
                rowSum += matrix[x - 1][y - 1]

    def sumRegion(self, row1, col1, row2, col2):
        """
        sum of elements matrix[(row1,col1)..(row2,col2)], inclusive.
        :type row1: int
        :type col1: int
        :type row2: int
        :type col2: int
        :rtype: int
        """
        return self.sums[row2 + 1][col2 + 1] + self.sums[row1][col1] \
                 - self.sums[row1][col2 + 1] - self.sums[row2 + 1][col1]


# Your NumMatrix object will be instantiated and called as such:
# numMatrix = NumMatrix(matrix)
# numMatrix.sumRegion(0, 1, 2, 3)
# numMatrix.sumRegion(1, 2, 3, 4)


# V2 
# Time:  ctor:   O(m * n),
#        lookup: O(1)
# Space: O(m * n)

class NumMatrix(object):
    def __init__(self, matrix):
        """
        initialize your data structure here.
        :type matrix: List[List[int]]
        """
        if not matrix:
            return

        m, n = len(matrix), len(matrix[0])
        self.__sums = [[0 for _ in range(n+1)] for _ in range(m+1)]
        for i in range(1, m+1):
            for j in range(1, n+1):
                self.__sums[i][j] = self.__sums[i][j-1] + matrix[i-1][j-1]
        for j in range(1, n+1):
            for i in range(1, m+1):
                self.__sums[i][j] += self.__sums[i-1][j]

    def sumRegion(self, row1, col1, row2, col2):
        """
        sum of elements matrix[(row1,col1)..(row2,col2)], inclusive.
        :type row1: int
        :type col1: int
        :type row2: int
        :type col2: int
        :rtype: int
        """
        return self.__sums[row2+1][col2+1] - self.__sums[row2+1][col1] - \
               self.__sums[row1][col2+1] + self.__sums[row1][col1]
