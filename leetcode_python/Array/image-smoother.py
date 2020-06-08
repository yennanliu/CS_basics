# Time:  O(m * n)
# Space: O(1)
#
# Given a 2D integer matrix M representing the gray scale of an image,
# you need to design a smoother to make the gray scale of each cell becomes
# the average gray scale (rounding down) of all the 8 surrounding cells and itself.
# If a cell has less than 8 surrounding cells, then use as many as you can.
#
# Example 1:
# Input:
# [[1,1,1],
#  [1,0,1],
#  [1,1,1]]
# Output:
# [[0, 0, 0],
#  [0, 0, 0],
#  [0, 0, 0]]
# Explanation:
# For the point (0,0), (0,2), (2,0), (2,2): floor(3/4) = floor(0.75) = 0
# For the point (0,1), (1,0), (1,2), (2,1): floor(5/6) = floor(0.83333333) = 0
# For the point (1,1): floor(8/9) = floor(0.88888889) = 0
# Note:
# The value in the given matrix is in the range of [0, 255].
# The length and width of the given matrix are in the range of [1, 150].

# V0
class Solution:
    def imageSmoother(self, M):
        row, col = len(M), len(M[0])
        res = [[0]*col for i in range(row)]
        dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
        for i in range(row):
            for j in range(col):
                temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
                res[i][j] = sum(temp)//len(temp)
        return res

# V0'
class Solution(object):
    def imageSmoother(self, M):
        dx, dy = [-1, 0, 1], [-1, 0, 1]
        w, h = len(M), len(M[0])
        N = []
        for x in range(w):
            row = []
            for y in range(h):
                nbs = [M[x + i][y + j] for i in dx for j in dy \
                       if 0 <= x + i < w and 0 <= y + j < h]
                row.append(floor(sum(nbs) / len(nbs)))
            N.append(row)
        return N

# V1 
# http://bookshadow.com/weblog/2017/08/21/leetcode-image-smoother/
from math import floor
class Solution(object):
    def imageSmoother(self, M):
        """
        :type M: List[List[int]]
        :rtype: List[List[int]]
        """
        dx, dy = [-1, 0, 1], [-1, 0, 1]
        w, h = len(M), len(M[0])
        N = []
        for x in range(w):
            row = []
            for y in range(h):
                nbs = [M[x + i][y + j] for i in dx for j in dy \
                       if 0 <= x + i < w and 0 <= y + j < h]
                row.append(floor(sum(nbs) / len(nbs)))
            N.append(row)
        return N

### Test case :
s=Solution()
assert s.imageSmoother([[1,1,1],[1,0,1],[1,1,1]]) == [[0, 0, 0], [0, 0, 0], [0, 0, 0]] 
assert s.imageSmoother([[0,0,0],[0,0,0],[0,0,0]]) == [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
assert s.imageSmoother([[]]) == [[]]
assert s.imageSmoother([[3,3,3],[3,3,3],[3,3,3]]) == [[3, 3, 3], [3, 3, 3], [3, 3, 3]]
assert s.imageSmoother([[1,99,1],[1,100,1],[777,1,1]]) ==  [[50, 33, 50], [163, 109, 33], [219, 146, 25]]
assert s.imageSmoother([[1,1,1],[1,0,1]]) == [[0, 0, 0], [0, 0, 0]]
assert s.imageSmoother([[],[]]) == [[], []]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79156499
from copy import deepcopy as copy
class Solution(object):
    def imageSmoother(self, M):
        """
        :type M: List[List[int]]
        :rtype: List[List[int]]
        """
        if not M or not M[0]:
            return M
        rows = len(M)
        cols = len(M[0])
        isValid = lambda i,j: i >=0 and i < rows and j >= 0 and j < cols
        row, col = 0, 0
        answer = copy(M)
        for row in range(rows):
            for col in range(cols):
                _sum, count = 0, 0
                for i in range(-1, 2):
                    for j in range(-1, 2):
                        if isValid(row + i, col + j):
                            _sum += M[row + i][col + j]
                            count += 1
                answer[row][col] = _sum / count
        return answer

# V1''
# https://leetcode.com/problems/image-smoother/solution/
# IDEA : BRUTE FORCE 
# Time Complexity: O(N)
# Space Complexity: O(N)
class Solution(object):
    def imageSmoother(self, M):
        R, C = len(M), len(M[0])
        ans = [[0] * C for _ in M]

        for r in range(R):
            for c in range(C):
                count = 0
                for nr in (r-1, r, r+1):
                    for nc in (c-1, c, c+1):
                        if 0 <= nr < R and 0 <= nc < C:
                            ans[r][c] += M[nr][nc]
                            count += 1
                ans[r][c] /= count
        return ans

# V1'''
# https://leetcode.com/problems/image-smoother/discuss/454951/Python3-simple-solution
class Solution:
    def imageSmoother(self, M: List[List[int]]) -> List[List[int]]:
        row, col = len(M), len(M[0])
        res = [[0]*col for i in range(row)]
        dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
        for i in range(row):
            for j in range(col):
                temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
                res[i][j] = sum(temp)//len(temp)
        return res

# V1''''
# https://leetcode.com/problems/image-smoother/discuss/106635/python-O(m*n)
class Solution(object):
    def imageSmoother(self, M):
        if not M: return M
        new = [[0 for _ in range(len(M[0]))] for _ in range(len(M))]
        directions = ((0, 0), (0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (-1, -1), (-1, 1), (1, -1))
        for i in range(len(new)):
            for j in range(len(new[0])):
                total = 0
                count = 0
                for r, c in directions:
                    if i + r < 0 or j + c < 0 or i + r >= len(M) or j + c >= len(M[0]):
                        continue
                    total += M[i + r][j + c]
                    count += 1
                new[i][j] = floor(total/count)
        return new

# V2 
# Time:  O(m * n)
# Space: O(1)
class Solution(object):
    def imageSmoother(self, M):
        """
        :type M: List[List[int]]
        :rtype: List[List[int]]
        """
        def getGray(M, i, j):
            total, count = 0, 0.0
            for r in range(-1, 2):
                for c in range(-1, 2):
                    ii, jj = i + r, j + c
                    if 0 <= ii < len(M) and 0 <= jj < len(M[0]):
                        total += M[ii][jj]
                        count += 1.0
            return int(total / count)

        result = [[0 for _ in range(len(M[0]))] for _ in range(len(M))]
        for i in range(len(M)):
            for j in range(len(M[0])):
                result[i][j] = getGray(M, i, j)
        return result
