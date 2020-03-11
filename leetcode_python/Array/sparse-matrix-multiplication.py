"""
Given two sparse matrices A and B, return the result of AB.

You may assume that A's column number is equal to B's row number.

Example:

A = [
  [ 1, 0, 0],
  [-1, 0, 3]
]

B = [
  [ 7, 0, 0 ],
  [ 0, 0, 0 ],
  [ 0, 0, 1 ]
]


     |  1 0 0 |   | 7 0 0 |   |  7 0 0 |
AB = | -1 0 3 | x | 0 0 0 | = | -7 0 3 |
                  | 0 0 1 |
"""

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

# V1 
# http://www.voidcn.com/article/p-muhtjhos-qp.html
# https://www.cnblogs.com/grandyang/p/5282959.html
# IDEA : MAXTRIX OP
class Solution(object):
    def multiply(self, A, B):
        """
        :type A: List[List[int]]
        :type B: List[List[int]]
        :rtype: List[List[int]]
        """
        result = []
        BT = zip(*B) # transfer B,get the column of B
        length = len(B)
        for row in A:
            if not any(row):
                result.append([0] * length)
                continue
            result.append([sum(map(lambda (x, y): x*y, zip(row, col))) 
                                        if any(col) else 0 for col in BT])
        return result

# V2 
# Time:  O(m * n * l), A is m x n matrix, B is n x l matrix
# Space: O(m * l)
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
