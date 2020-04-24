# Matrix relative operation 

### 1) Transpose matrix
```python
# 048 Rotate Image
def rotate(matrix):
    n = len(matrix)
    for i in range(n):
        for j in range(i+1, n):
            matrix[i][j], matrix[j][i] = \
            matrix[j][i], matrix[i][j]

    return matrix

# matrix=[[1,2,3],[4,5,6],[7,8,9]]
# m_ = rotate(matrix)
# print (m_)
# [[1, 4, 7], [2, 5, 8], [3, 6, 9]]

```

### 2) Spiral matrix
```python
# 054 Spiral Matrix
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
```