# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81118328
class Solution(object):
    def matrixScore(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        rows, cols = len(A), len(A[0])
        for row in range(rows):
            if A[row][0] == 0:
                A[row] = self.toggle_row(A[row])
        for col in range(1, cols):
            col_array = [A[row][col] for row in range(rows)]
            sum_col_array = sum(col_array)
            if sum_col_array <= rows / 2:
                col_array = self.toggle_col(col_array)
            for row in range(rows):
                A[row][col] = col_array[row]
        bin_row = []
        for row in range(rows):
            bin_row.append(int("".join(map(str, A[row])), 2))
        return sum(bin_row)
                
    def toggle_row(self, row_array):
        return [0 if x == 1 else 1 for x in row_array]
    
    def toggle_col(self, col_array):
        return [0 if x == 1 else 1 for x in col_array]

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/81118328
class Solution(object):
    def matrixScore(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        M, N = len(A), len(A[0])
        for i in range(M):
            if A[i][0] == 0:
                for j in range(N):
                    A[i][j] = 1 - A[i][j]
        res = 0
        for j in range(N):
            count1 = 0
            for i in range(M):
                if A[i][j]:
                    count1 += 1
            res += (1 << N - 1- j) * max(count1, M - count1)
        return res
        
# V2 
# Time:  O(r * c)
# Space: O(1)
class Solution(object):
    def matrixScore(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        R, C = len(A), len(A[0])
        result = 0
        for c in range(C):
            col = 0
            for r in range(R):
                col += A[r][c] ^ A[r][0]
            result += max(col, R-col) * 2**(C-1-c)
        return result