# Matrix
> N dimension (2-D in most cases) linear data structure

## 0) Concept  

### 0-1) Types

- Types
    - [greedy.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/greedy.md)
    - [array.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/array.md)
    - Search in 2D Matrix
        - LC 74 : flatten matrix + binary search
            - [binary_search.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
    - Spiral Matrix I
        - LC 54
        - LC 59
    - Sparse matrix product
        - LC 311
    - Diagonal Traverse
        - LC 498
    - Rotate Image
        - LC 48
        - (compare LC 867 VS LC 48)
    - Transpose Matrix
        - LC 867

- Algorithm
    - [fucking algorithm : 二维数组的花式遍历技巧](https://labuladong.github.io/algo/2/20/26/)
        - LC 151
        - LC 48
        - LC 54
        - LC 59

- Data structure
    - array

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Matrix

- Properties
    - diagonal, anti-diagonal
        - https://leetcode.com/problems/n-queens-ii/solutions/1146740/n-queens-ii/
        - diagonal
            - For each square on a given diagonal, the difference between the row and column indexes (row - col) will be constant. Think about the diagonal that starts from (0, 0) - the i th square has coordinates (i, i), so the difference is always 0.
            - <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/diagonal.png" ></p>
        - anti-diagonal
            - For each square on a given anti-diagonal, the sum of the row and column indexes (row + col) will be constant. If you were to start at the highest square in an anti-diagonal and move downwards, the row index increments by 1 (row + 1), and the column index decrements by 1 (col - 1).
            - <img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/anti-diagonal.png" ></p>


##### 1-1-1-1) Init Matrix

```python
# 1) init matrix 
# LC 73
### NOTE : 
# -> for matrix[i][j]:
#    -> y is FIRST element  (i)
#    -> x is SECOND element (j)
```


##### 1-1-1-2) Rotate Matrix

```java
//-----------------------------
// ROTATE (LC 48)
//-----------------------------

// Step 1) : mirror ([i, j] -> [j, i])
    /**
     *  Example :
     *
     *  matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
     *
     *  so, below double loop will visit :
     *
     *  (0,1), (0,2), (0,3)
     *  (1,2), (1,3)
     *  (2,3
     *
     */
    /** NOTE !!!
     *
     * for (int i = 0; i < len; i++)
     *   for (int j = i+1; j < width; j++)
     *
     * (j start from i+1)
     */


 // Step 2) : reverse ([1,2,3] -> [3,2,1])
```

```python
#--------------------------------
# transpose (i,j) -> (j, i)
#--------------------------------
# LC  048 Rotate Image
matrix = [
    [1,2,4],
    [4,5,6],
    [7,8,9]
]

l = len(matrix)
w = len(matrix[0])
for i in range(l):
    """
    NOTE !!!
        -> j start from i+1 to len(matrix[0])
    """
    for j in range(i+1, w): # NOTE THIS !!!!
        matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]

print (matrix)

# output
# i = 0 j = 1
# i = 0 j = 2
# i = 1 j = 2
# In [36]: matrix
# Out[36]: [[1, 4, 7], [2, 5, 8], [4, 6, 9]]


matrix = [
    [1,2,3,4],
    [5,6,7,8],
    [9,10,11,12],
    [13,14,15,16]
]

# output
# i = 0 j = 1
# i = 0 j = 2
# i = 0 j = 3
# i = 1 j = 2
# i = 1 j = 3
# i = 2 j = 3
# [[1, 5, 9, 13], [2, 6, 10, 14], [3, 7, 11, 15], [4, 8, 12, 16]]
```

```python
#----------------------
# Rotate matrix
#----------------------
# LC  048 Rotate Image
class Solution:
    def rotate(self, matrix):
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        # transpose
        for i in range(n):
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # reverse
        for i in range(n):
            matrix[i].reverse()
        return matrix
```


##### 1-1-1-3) Get avg value of Matrix

```python
#----------------------
# 2) get avg value of matrix
#----------------------
# LC 661
# some code
# M : matrix
row, col = len(M), len(M[0])
res = [[0]*col for i in range(row)]
# get tmp sum
dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
# get avg value
res[i][j] = sum(temp)//len(temp)
# some code
```

#### 1-1-11) Matrix relative problems
```python
# Transpose matrix
# LC  048 Rotate Image
# V0
# IDEA : TRANSPOSE -> REVERSE 
class Solution:
    def rotate(self, matrix):
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        # transpose
        for i in range(n):
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # reverse
        for i in range(n):
            matrix[i].reverse()
        return matrix

# Spiral matrix
# LC 054 Spiral Matrix
# V0
# IDEA : 4 cases : right, down, left, up + boundary condition
# PATTERN:
# while condition:
#     # right
#     for ..
#     # down
#     for ...
#     # left
#     for ...
#     # up
#     for ...
class Solution(object):
    def spiralOrder(self, matrix):
        # edge case
        if not matrix:
            return
        res=[]
        """
        NOTE this : we define 4 boundaries
        """
        left = 0
        right = len(matrix[0])-1
        top = 0
        bottom = len(matrix)-1
        """
        NOTE : this condition
        """
        while left <= right and top <= bottom:
            # NOTE !!! we use for loop INSTEAD of while
            # right
            for j in range(left, right+1):  # note : range(left, right+1)
                res.append(matrix[top][j])
            # down
            for i in range(top+1, bottom):  # note : range(top+1, bottom)
                res.append(matrix[i][right])
            # left
            for j in range(left, right+1)[::-1]: # note : range(left, right+1)[::-1]
                """
                NOTE : this condition
                """
                if top < bottom:
                    res.append(matrix[bottom][j])
            # up
            for i in range(top+1, bottom)[::-1]: # note : range(top+1, bottom)[::-1]
                """
                NOTE : this condition
                """
                if left < right:
                    res.append(matrix[i][left])

            # NOTE !!! we do boundary update AFTER each "right-down-left-up" iteration
            left += 1
            right -= 1
            top += 1
            bottom -= 1
            
        return res

# V0'
class Solution(object):
    # @param matrix, a list of lists of integers
    # @return a list of integers
    def spiralOrder(self, matrix):
        result = []
        if matrix == []:
            return result

        left, right, top, bottom = 0, len(matrix[0]) - 1, 0, len(matrix) - 1

        while left <= right and top <= bottom:
            # right
            for j in range(left, right + 1):
                result.append(matrix[top][j])
            # down
            for i in range(top + 1, bottom):
                result.append(matrix[i][right])
            # left
            for j in (range(left, right + 1))[::-1]:
                if top < bottom: # notice
                    result.append(matrix[bottom][j])
            # up
            for i in range(top + 1, bottom)[::-1]:
                if left < right: # notice
                    result.append(matrix[i][left])
            left, right, top, bottom = left + 1, right - 1, top + 1, bottom - 1

        return result
```

```python
# -------------------------
# get diagonal sum of matrix
# -------------------------
# LC 348. Design Tic-Tac-Toe
# ...
n = len(self.grid)
sum_of_row = sum([self.grid[row][c] == mark for c in range(n)])
sum_of_col = sum([self.grid[r][col]== mark for r in range(n)])
sum_of_left_d = sum([self.grid[i][i] == mark for i in range(n)])
sum_of_right_d = sum([self.grid[i][n-1-i] == mark for i in range(n)])
# ....
```

```python
# LC 311. Sparse Matrix Multiplication
# V0 
# TODO : OPTIMIZE THE PROCESS DUE TO THE SPARSE-MATRIX CONDITION 
class Solution(object):
    def multiply(self, A, B):
        """
        :type A: List[List[int]]
        :type B: List[List[int]]
        :rtype: List[List[int]]
        """
        m, n, l = len(A), len(A[0]), len(B[0])
        res = [[0 for _ in range(l)] for _ in range(m)]
        for i in range(m):
            for k in range(n):
                if A[i][k]:
                    for j in range(l):
                        res[i][j] += A[i][k] * B[k][j]
        return res
```

```python
# LC 498. Diagonal Traverse
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
```
 
## 2) LC Example


### 2-1) Set Matrix Zeroes
```python
# LC 73. Set Matrix Zeroes
# V0
class Solution(object):
    def setZeroes(self, matrix):   

        if not matrix:
            return matrix

        def help(matrix, xy):
            ### NOTE : 
            #          -> for cases matrix[i][j]:
            #            -> y is FIRST element  (i)
            #            -> x is SECOND element (j)
            x = xy[1]
            y = xy[0]
            matrix[y] = [0] * len(matrix[0])
            for j in range(len(matrix)):
                matrix[j][x] = 0
            return matrix

        _list = []
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    _list.append([i,j])

        for xy in _list:
            matrix = help(matrix, xy)
        return matrix
```

### 2-5) Image Smoother
```python
# LC 661 Image Smoother
class Solution:
    def imageSmoother(self, M):
        row, col = len(M), len(M[0])
        res = [[0]*col for i in range(row)]
        dirs = [[0,0],[0,1],[0,-1],[1,0],[-1,0],[1,1],[-1,-1],[-1,1],[1,-1]]
        # note we need to for looping row, col
        for i in range(row):
            for j in range(col):
                # and to below op for each i, j (row, col)
                temp = [M[i+m][j+n] for m,n in dirs if 0<=i+m<row and 0<=j+n<col]
                ### NOTE : this trick for getting avg
                res[i][j] = sum(temp)//len(temp)
        return res
```

### 2-6) Search a 2D Matrix
```python
# LC 74 Search a 2D Matrix
# LC 240. Search a 2D Matrix II
"""
NOTE !!!  boundary condition
"""
# V0'
# IDEA : DFS
class Solution(object):
    def searchMatrix(self, matrix, target):
        def dfs(matrix, target, x, y):
            if matrix[y][x] == target:
                res.append(True)
            matrix[y][x] = "#"
            moves = [[0,1],[0,-1],[1,0],[-1,0]]
            for move in moves:
                _x = x + move[1]
                _y = y + move[0]
                #print ("_x = " + str(_x) + " _y = " + str(_y))
                if 0 <= _x < w and 0 <= _y < l:
                    if matrix[_y][_x] != "#":
                        dfs(matrix, target, _x, _y)

        if not matrix:
            return False
        l = len(matrix)
        w = len(matrix[0])
        res = []
        dfs(matrix, target, 0, 0)
        return True in res
```

### 2-7) Sparse Matrix Multiplication

```java
// java
// LC 311
// V0
// IDEA : ARRAY OP (fix by gpt)
/**
*  Why there is 3 loop ?
*
*   -> Matrix Multiplication: Ci,j = Sigma(Aik * Bkj)
*
*   -> so we have 3 layer loop as below:
*    - i : Iterates over the rows of  A  (outer loop).
*    - j : Iterates over the columns of  B  (second loop).
*    - k : Iterates over the "shared dimension" (columns of  A  or rows of  B ) to compute the dot product (inner loop).
*
*
*  ->
*
*  The Role of the Loops
*    1.  Outer loop ( i ): Iterates over the rows of mat1 to calculate each row of the result matrix.
*    2.  Middle loop ( j ): Iterates over the columns of mat2 to compute each element in a row of the result matrix.
*    3.  Inner loop ( k ): Iterates over the "shared dimension" to compute the dot product of the  i^{th}  row of mat1 and the  j^{th}  column of mat2.
*
*
* ->  Why the Inner Loop ( k ) Exists ?
*
*    -> The inner loop is necessary
*       because each element of the result matrix
*       is computed as the dot product of a
*       row from mat1 and a column from mat2.
*       Without this loop, the computation of the dot product would be incomplete.
*/
public static int[][] multiply(int[][] mat1, int[][] mat2) {
    // Edge case: Single element matrices
    if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1) {
        return new int[][]{ {mat1[0][0] * mat2[0][0]} };
    }

    int l_1 = mat1.length;    // Number of rows in mat1
    int w_1 = mat1[0].length; // Number of columns in mat1 (and rows in mat2)

    int w_2 = mat2[0].length; // Number of columns in mat2

    // Initialize the result matrix
    int[][] res = new int[l_1][w_2];

    // Perform matrix multiplication
    for (int i = 0; i < l_1; i++) {
        for (int j = 0; j < w_2; j++) {
            int sum = 0; // Sum for res[i][j]
            for (int k = 0; k < w_1; k++) {
                sum += mat1[i][k] * mat2[k][j];
            }
            res[i][j] = sum;
        }
    }

    return res;
}
```


### 2-8) Transpose Matrix

```java
// java
// LC 867

// V0-1
// IDEA: MATH + ARRAY OP (fixed by gpt)
public int[][] transpose_0_1(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return new int[0][0];
    }

    int rows = matrix.length;
    int cols = matrix[0].length;

    int[][] result = new int[cols][rows]; // Transposed dimensions

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[j][i] = matrix[i][j];
        }
    }

    return result;
}
```