"""

498. Diagonal Traverse
Medium


Add to List

Share
Given an m x n matrix mat, return an array of all the elements of the array in a diagonal order.


Example 1:


Input: mat = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,4,7,5,3,6,8,9]
Example 2:

Input: mat = [[1,2],[3,4]]
Output: [1,2,3,4]
 

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 104
1 <= m * n <= 104
-105 <= mat[i][j] <= 105


"""

# V0 
# IDEA : while loop + boundary conditions
### NOTE : the "directions" trick
class Solution(object):
    def findDiagonalOrder(self, matrix):
        if not matrix or not matrix[0]: return []
        ### NOTE this trick
        directions = [(-1, 1), (1, -1)]
        count = 0
        res = []
        i, j = 0, 0
        M, N = len(matrix), len(matrix[0])
        while len(res) < M * N:
            if 0 <= i < M and 0 <= j < N:
                res.append(matrix[i][j])
                direct = directions[count % 2]
                i, j = i + direct[0], j + direct[1]
                continue
            elif i < 0 and 0 <= j < N:
                i += 1
            elif 0 <= i < M and j < 0:
                j += 1
            elif i < M and j >= N:
                i += 2
                j -= 1
            elif i >= M and j < N:
                j += 2
                i -= 1
            count += 1
        return res

# V0'
# IDEA : Diagonal Iteration and Reversal
# NOTE !!! : for "odd" diagoal traversal, we just need to go over it and REVERSE it before append tp res
class Solution:
    def findDiagonalOrder(self, matrix):
        
        # Check for empty matrices
        if not matrix or not matrix[0]:
            return []
        
        # Variables to track the size of the matrix
        N, M = len(matrix), len(matrix[0])
        
        # The two arrays as explained in the algorithm
        result, intermediate = [], []
        
        # We have to go over all the elements in the first
        # row and the last column to cover all possible diagonals
        for d in range(N + M - 1):
            
            # Clear the intermediate array everytime we start
            # to process another diagonal
            intermediate = []
            
            # We need to figure out the "head" of this diagonal
            # The elements in the first row and the last column
            # are the respective heads.
            # r : row idx
            # c : col idx
            #r, c = 0 if d < M else d - M + 1, d if d < M else M - 1
            if d < M:
                r = 0
                c = d
            else:
                r = d - M + 1
                c = M - 1
            
            # Iterate until one of the indices goes out of scope
            # Take note of the index math to go down the diagonal
            while r < N and c > -1:
                intermediate.append(matrix[r][c])
                r += 1
                c -= 1
            
            # Reverse even numbered diagonals. The
            # article says we have to reverse odd 
            # numbered articles but here, the numbering
            # is starting from 0 :P
            if d % 2 == 0:
                result.extend(intermediate[::-1])
            else:
                result.extend(intermediate)
        return result        

# V1
# IDEA : Diagonal Iteration and Reversal
# https://leetcode.com/problems/diagonal-traverse/solution/
class Solution:
    
    def findDiagonalOrder(self, matrix: List[List[int]]) -> List[int]:
        
        # Check for empty matrices
        if not matrix or not matrix[0]:
            return []
        
        # Variables to track the size of the matrix
        N, M = len(matrix), len(matrix[0])
        
        # The two arrays as explained in the algorithm
        result, intermediate = [], []
        
        # We have to go over all the elements in the first
        # row and the last column to cover all possible diagonals
        for d in range(N + M - 1):
            
            # Clear the intermediate array everytime we start
            # to process another diagonal
            intermediate.clear()
            
            # We need to figure out the "head" of this diagonal
            # The elements in the first row and the last column
            # are the respective heads.
            r, c = 0 if d < M else d - M + 1, d if d < M else M - 1
            
            # Iterate until one of the indices goes out of scope
            # Take note of the index math to go down the diagonal
            while r < N and c > -1:
                intermediate.append(matrix[r][c])
                r += 1
                c -= 1
            
            # Reverse even numbered diagonals. The
            # article says we have to reverse odd 
            # numbered articles but here, the numbering
            # is starting from 0 :P
            if d % 2 == 0:
                result.extend(intermediate[::-1])
            else:
                result.extend(intermediate)
        return result        

# V1
# IDEA : Simulation
# https://leetcode.com/problems/diagonal-traverse/solution/
class Solution:
    
    def findDiagonalOrder(self, matrix: List[List[int]]) -> List[int]:
        
        # Check for an empty matrix
        if not matrix or not matrix[0]:
            return []
        
        # The dimensions of the matrix
        N, M = len(matrix), len(matrix[0])
        
        # Incides that will help us progress through 
        # the matrix, one element at a time.
        row, column = 0, 0
        
        # As explained in the article, this is the variable
        # that helps us keep track of what direction we are
        # processing the current diaonal
        direction = 1
        
        # Final result array that will contain all the elements
        # of the matrix
        result = []
        
        # The uber while loop which will help us iterate over all
        # the elements in the array.
        while row < N and column < M:
            
            # First and foremost, add the current element to 
            # the result matrix. 
            result.append(matrix[row][column])
            
            # Move along in the current diagonal depending upon
            # the current direction.[i, j] -> [i - 1, j + 1] if 
            # going up and [i, j] -> [i + 1][j - 1] if going down.
            new_row = row + (-1 if direction == 1 else 1)
            new_column = column + (1 if direction == 1 else -1)
            
            # Checking if the next element in the diagonal is within the
            # bounds of the matrix or not. If it's not within the bounds,
            # we have to find the next head. 
            if new_row < 0 or new_row == N or new_column < 0 or new_column == M:
                
                # If the current diagonal was going in the upwards
                # direction.
                if direction:
                    
                    # For an upwards going diagonal having [i, j] as its tail
                    # If [i, j + 1] is within bounds, then it becomes
                    # the next head. Otherwise, the element directly below
                    # i.e. the element [i + 1, j] becomes the next head
                    row += (column == M - 1)
                    column += (column < M - 1)
                else:
                    
                    # For a downwards going diagonal having [i, j] as its tail
                    # if [i + 1, j] is within bounds, then it becomes
                    # the next head. Otherwise, the element directly below
                    # i.e. the element [i, j + 1] becomes the next head
                    column += (row == N - 1)
                    row += (row < N - 1)
                    
                # Flip the direction
                direction = 1 - direction        
            else:
                row = new_row
                column = new_column
                        
        return result

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82528226
class Solution(object):
    def findDiagonalOrder(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[int]
        """
        if not matrix or not matrix[0]: return []
        directions = [(-1, 1), (1, -1)]
        count = 0
        res = []
        i, j = 0, 0
        M, N = len(matrix), len(matrix[0])
        while len(res) < M * N:
            if 0 <= i < M and 0 <= j < N:
                res.append(matrix[i][j])
                direct = directions[count % 2]
                i, j = i + direct[0], j + direct[1]
                continue
            elif i < 0 and 0 <= j < N:
                i += 1
            elif 0 <= i < M and j < 0:
                j += 1
            elif i < M and j >= N:
                i += 2
                j -= 1
            elif i >= M and j < N:
                j += 2
                i -= 1
            count += 1
        return res

# V1''
# http://bookshadow.com/weblog/2017/02/05/leetcode-diagonal-traverse/
class Solution(object):
    def findDiagonalOrder(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[int]
        """
        if not matrix: return []
        i, j, k = 0, 0, 1
        w, h = len(matrix), len(matrix[0])
        ans = []
        for x in range(w * h):
            ans.append(matrix[i][j])
            if k > 0:
                di, dj = i - 1, j + 1
            else:
                di, dj = i + 1, j - 1
            if 0 <= di < w and 0 <= dj < h:
                i, j = di, dj
            else:
                if k > 0:
                    if j + 1 < h:
                        j += 1
                    else:
                        i += 1
                else:
                    if i + 1 < w:
                        i += 1
                    else:
                        j += 1
                k *= -1
        return ans

# V2