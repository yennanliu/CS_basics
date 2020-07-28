"""
A matrix is Toeplitz if every diagonal from top-left to bottom-right has the same element.

Now given an M x N matrix, return True if and only if the matrix is Toeplitz.
 

Example 1:

Input:
matrix = [
  [1,2,3,4],
  [5,1,2,3],
  [9,5,1,2]
]
Output: True
Explanation:
In the above grid, the diagonals are:
"[9]", "[5, 5]", "[1, 1, 1]", "[2, 2, 2]", "[3, 3]", "[4]".
In each diagonal all elements are the same, so the answer is True.
Example 2:

Input:
matrix = [
  [1,2],
  [2,2]
]
Output: False
Explanation:
The diagonal "[1, 2]" has different elements.
"""

# V0 
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        m, n = len(matrix), len(matrix[0])
        for i in range(m-1):
            for j in range(n-1):
                if matrix[i][j] != matrix[i+1][j+1]:
                    return False
        return True

# V1 
# https://blog.csdn.net/mario_mmh/article/details/79940837
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: bool
        """
        for i in range(len(matrix)-1):
            for j in range(len(matrix[0])-1):
                if matrix[i][j] != matrix[i+1][j+1]:
                    return False
        return True

### Test case : dev

# V1' 
# http://bookshadow.com/weblog/2018/01/21/leetcode-toeplitz-matrix/
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: bool
        """
        vmap = collections.defaultdict(set)
        M, N = len(matrix), len(matrix[0])
        for x in range(M):
            for y in range(N):
                vmap[y - x].add(matrix[x][y])
                if len(vmap[y - x]) > 1: return False
        return True

# V1''
# https://leetcode.com/problems/toeplitz-matrix/solution/
# IDEA : GROUP BY CATEGORY
# Time Complexity: O(M*N)
# Space Complexity: O(M+N)
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        groups = {}
        for r, row in enumerate(matrix):
            for c, val in enumerate(row):
                if r-c not in groups:
                    groups[r-c] = val
                elif groups[r-c] != val:
                    return False
        return True

# V1'''
# https://leetcode.com/problems/toeplitz-matrix/solution/
# IDEA :  Compare With Top-Left Neighbor
# Time Complexity: O(M*N)
# Space Complexity: O(1)
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        return all(r == 0 or c == 0 or matrix[r-1][c-1] == val
                   for r, row in enumerate(matrix)
                   for c, val in enumerate(row))

# V2 
# Time:  O(m * n)
# Space: O(1)
class Solution(object):
    def isToeplitzMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: bool
        """
        return all(i == 0 or j == 0 or matrix[i-1][j-1] == val
                   for i, row in enumerate(matrix)
                   for j, val in enumerate(row))

class Solution2(object):
    def isToeplitzMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: bool
        """
        for row_index, row in enumerate(matrix):
            for digit_index, digit in enumerate(row):
                if not row_index or not digit_index:
                    continue
                if matrix[row_index - 1][digit_index - 1] != digit:
                    return False
        return True