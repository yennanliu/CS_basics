# Time:  O(n^2)
# Space: O(1)
#
# You are given an n x n 2D matrix representing an image.
#
# Rotate the image by 90 degrees (clockwise).
#
# Follow up:
# Could you do this in-place?
#


# V1 
# if mymatrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
# -> list(zip(*mymatrix)) = [(1, 4, 7), (2, 5, 8), (3, 6, 9)]
# -> list(reversed(x)) for x in zip(*matrix) = [(7,4,1), (8,5,2), (9,6,3)]

# if array = [1, 2, 3]
# -> list(reversed(array)) = [3,2,1]

class Solution:
	def rotate(self, matrix):
		return [ list(reversed(x)) for x in  zip(*matrix)]




# V2 

class Solution:
    # @param matrix, a list of lists of integers
    # @return a list of lists of integers
    def rotate(self, matrix):
        n = len(matrix)

        # anti-diagonal mirror
        for i in range(n):
            for j in range(n - i):
                matrix[i][j], matrix[n-1-j][n-1-i] = matrix[n-1-j][n-1-i], matrix[i][j]

        # horizontal mirror
        for i in range(n / 2):
            for j in range(n):
                matrix[i][j], matrix[n-1-i][j] = matrix[n-1-i][j], matrix[i][j]

        return matrix


        






