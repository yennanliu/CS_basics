# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80471495
# IDEA : OPERATION : INVERSE -> Exclusive or (1->0, 0->1)
# NOTE : Exclusive or
# In [29]: x=1

# In [30]: x
# Out[30]: 1

# In [31]: x^=1

# In [32]: x
# Out[32]: 0

# In [33]: x=0

# In [34]: x
# Out[34]: 0

# In [35]: x^=1

# In [36]: x
# Out[36]: 1
class Solution:
    def flipAndInvertImage(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        rows = len(A)
        cols = len(A[0])
        for row in range(rows):
            A[row] = A[row][::-1]
            for col in range(cols):
                A[row][col] ^= 1
        return A

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80471495
# IDEA : GREEDY 
class Solution:
    def flipAndInvertImage(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        M, N = len(A), len(A[0])
        res = [[0] * N for _ in range(M)]
        for i in range(M):
            for j in range(N):
                res[i][j] = 1 - A[i][N - 1 - j]
        return res

# V2 
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    def flipAndInvertImage(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        for row in A:
            for i in range((len(row)+1) // 2):
                row[i], row[~i] = row[~i] ^ 1, row[i] ^ 1
        return A