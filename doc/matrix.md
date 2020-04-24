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