"""

Given an m x n matrix, 
return all elements of the matrix in spiral order.


Example 1:


Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,3,6,9,8,7,4,5]

Example 2:


Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 

Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 10
-100 <= matrix[i][j] <= 100

"""

# V0
class Solution:
    def spiralOrder(self, matrix):
        res = []
        if len(matrix) == 0:
            return res
        num_row = max(0, len(matrix) - 1)
        num_col = len(matrix[0])
        row = 0
        col = -1
        while True:
            # right
            if num_col == 0:
                break
            for i in range(num_col):
                col += 1
                res.append(matrix[row][col])                
            num_col -= 1
            # down
            if num_row == 0:
                break
            for i in range(num_row):
                row += 1
                res.append(matrix[row][col])
            num_row -= 1
            # left
            if num_col == 0:
                break
            for i in range(num_col):
                col -= 1
                res.append(matrix[row][col])
            num_col -= 1
            # up
            if num_row == 0:
                break
            for i in range(num_row):
                row -= 1
                res.append(matrix[row][col])
            num_row -= 1
        return res

# V1
# https://leetcode.com/problems/spiral-matrix/discuss/591493/Easy-to-understand-Python-solution
# IDEA : 4 Cases
# (1) move right if possible, otherwise break out; 
# (2) move down if possible, otherwise break out; 
# (3) move left if possible, otherwise break out; 
# (4) move up if possible and go back to (1), otherwise break out
# NOTICE :
# 1) 
# The move pattern : -> ↓ <- ↑ 
# 2)
# use num_col == 0, num_row == 0 to decide whether keep while looping or not
# use num_col == 0 for both "left" and "right" move
# -> since for "spiral move", the left + right move should == len(matrix) which is the num_col == 0 condition
class Solution:
    def spiralOrder(self, matrix):
        res = []
        if len(matrix) == 0:
            return res
        # NOTICE HERE
        num_row = max(0, len(matrix) - 1)
        num_col = len(matrix[0])
        row = 0
        col = -1
        while True:
            # right
            if num_col == 0:
                break
            for i in range(num_col):
                col += 1
                res.append(matrix[row][col])                
            num_col -= 1  # num_col - 1 when every "right" move finished, since we need decide whether break the wile loop based on num_col == 0 or not
            # down
            if num_row == 0:
                break
            for i in range(num_row):
                row += 1
                res.append(matrix[row][col])
            num_row -= 1
            # left
            if num_col == 0:
                break
            for i in range(num_col):
                col -= 1
                res.append(matrix[row][col]) 
            num_col -= 1 # num_col - 1 when every "left" move finished, since we need decide whether break the wile loop based on num_col == 0 or not
            # up
            if num_row == 0:
                break
            for i in range(num_row):
                row -= 1
                res.append(matrix[row][col])
            num_row -= 1
        return res

### Test case
s = Solution()
assert s.spiralOrder([[2,5,8],[4,0,-1]]) == [2,5,8,-1,0,4]
assert s.spiralOrder([[ 1, 2, 3 ],[ 4, 5, 6 ],[ 7, 8, 9 ]]) == [1,2,3,6,9,8,7,4,5]
assert s.spiralOrder([[]]) == []
assert s.spiralOrder([[0]]) == [0]
assert s.spiralOrder([[1,2,3]]) == [1,2,3]
assert s.spiralOrder([[1,2,3],[4,5,6]]) == [1,2,3,6,5,4]

# V1'
# IDEA : SIMULATION
# Time Complexity: O(N)
# Space Complexity: O(N)
# https://leetcode.com/problems/spiral-matrix/solution/
class Solution(object):
    def spiralOrder(self, matrix):
        if not matrix: return []
        R, C = len(matrix), len(matrix[0])
        seen = [[False] * C for _ in matrix]
        ans = []
        dr = [0, 1, 0, -1]
        dc = [1, 0, -1, 0]
        r = c = di = 0
        for _ in range(R * C):
            ans.append(matrix[r][c])
            seen[r][c] = True
            cr, cc = r + dr[di], c + dc[di]
            if 0 <= cr < R and 0 <= cc < C and not seen[cr][cc]:
                r, c = cr, cc
            else:
                di = (di + 1) % 4
                r, c = r + dr[di], c + dc[di]
        return ans

# V1''
# IDEA : Layer-by-Layer
# Time Complexity: O(N)
# Space Complexity:
#   -> O(1)O(1) without considering the output array, since we don't use any additional data structures for our computations.
#   -> O(N)O(N) if the output array is taken into account.
# https://leetcode.com/problems/spiral-matrix/solution/
class Solution(object):
    def spiralOrder(self, matrix):
        def spiral_coords(r1, c1, r2, c2):
            for c in range(c1, c2 + 1):
                yield r1, c
            for r in range(r1 + 1, r2 + 1):
                yield r, c2
            if r1 < r2 and c1 < c2:
                for c in range(c2 - 1, c1, -1):
                    yield r2, c
                for r in range(r2, r1, -1):
                    yield r, c1

        if not matrix: return []
        ans = []
        r1, r2 = 0, len(matrix) - 1
        c1, c2 = 0, len(matrix[0]) - 1
        while r1 <= r2 and c1 <= c2:
            for r, c in spiral_coords(r1, c1, r2, c2):
                ans.append(matrix[r][c])
            r1 += 1; r2 -= 1
            c1 += 1; c2 -= 1
        return ans

# V1'''
# https://www.cnblogs.com/zuoyuan/p/3769829.html
# https://blog.csdn.net/qian2729/article/details/50539281
# IDEA : STATUS + POINTER 
# IDEA : STATUS = [0, 1, 2, 3]. POINTER = [up, down, left, right]
class Solution:
    # @param matrix, a list of lists of integers
    # @return a list of integers
    def spiralOrder(self, matrix):
        if matrix == []: return []
        up = 0; left = 0
        down = len(matrix)-1
        right = len(matrix[0])-1
        direct = 0  # 0: go right   1: go down  2: go left  3: go up
        res = []
        while True:
            if direct == 0:
                for i in range(left, right+1):
                    res.append(matrix[up][i])
                up += 1
            if direct == 1:
                for i in range(up, down+1):
                    res.append(matrix[i][right])
                right -= 1
            if direct == 2:
                for i in range(right, left-1, -1):
                    res.append(matrix[down][i])
                down -= 1
            if direct == 3:
                for i in range(down, up-1, -1):
                    res.append(matrix[i][left])
                left += 1
            if up > down or left > right: return res
            direct = (direct+1) % 4

# V2 
# Time:  O(m * n)
# Space: O(1)
class Solution(object):
    # @param matrix, a list of lists of integers
    # @return a list of integers
    def spiralOrder(self, matrix):
        result = []
        if matrix == []:
            return result

        left, right, top, bottom = 0, len(matrix[0]) - 1, 0, len(matrix) - 1

        while left <= right and top <= bottom:
            for j in range(left, right + 1):
                result.append(matrix[top][j])
            for i in range(top + 1, bottom):
                result.append(matrix[i][right])
            for j in reversed(range(left, right + 1)):
                if top < bottom:
                    result.append(matrix[bottom][j])
            for i in reversed(range(top + 1, bottom)):
                if left < right:
                    result.append(matrix[i][left])
            left, right, top, bottom = left + 1, right - 1, top + 1, bottom - 1

        return result
