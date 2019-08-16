"""
In MATLAB, there is a very useful function called 'reshape', which can reshape a matrix into a new one with different size but keep its original data.

You're given a matrix represented by a two-dimensional array, and two positive integers r and c representing the row number and column number of the wanted reshaped matrix, respectively.

The reshaped matrix need to be filled with all the elements of the original matrix in the same row-traversing order as they were.

If the 'reshape' operation with given parameters is possible and legal, output the new reshaped matrix; Otherwise, output the original matrix.

Example 1:
Input: 
nums = 
[[1,2],
 [3,4]]
r = 1, c = 4
Output: 
[[1,2,3,4]]
Explanation:
The row-traversing of nums is [1,2,3,4]. The new reshaped matrix is a 1 * 4 matrix, fill it row by row by using the previous list.
Example 2:
Input: 
nums = 
[[1,2],
 [3,4]]
r = 2, c = 4
Output: 
[[1,2],
 [3,4]]
Explanation:
There is no way to reshape a 2 * 2 matrix to a 2 * 4 matrix. So output the original matrix.
Note:
The height and width of the given matrix is in range [1, 100].
The given r and c are all positive.
"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83626519
# IDEA : LIST & GO THROUGH MATRIX 
class Solution(object):
    def matrixReshape(self, nums, r, c):
        """
        :type nums: List[List[int]]
        :type r: int
        :type c: int
        :rtype: List[List[int]]
        """
        M, N = len(nums), len(nums[0])
        if M * N != r * c:
            return nums
        res = []
        row = []
        for i in range(M):
            for j in range(N):
                row.append(nums[i][j])
                if len(row) == c:
                    res.append(row)
                    row = []
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83626519
# IDEA : REMAINDER  
class Solution(object):
    def matrixReshape(self, nums, r, c):
        """
        :type nums: List[List[int]]
        :type r: int
        :type c: int
        :rtype: List[List[int]]
        """
        M, N = len(nums), len(nums[0])
        if M * N != r * c:
            return nums
        res = [[0] * c for _ in range(r)]
        count = 0
        for i in range(M):
            for j in range(N):
                res[count / c][count % c] = nums[i][j]
                count +=1
        return res

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83626519
class Solution(object):
    def matrixReshape(self, nums, r, c):
        """
        :type nums: List[List[int]]
        :type r: int
        :type c: int
        :rtype: List[List[int]]
        """
        M, N = len(nums), len(nums[0])
        if M * N != r * c:
            return nums
        res = [[0] * c for _ in range(r)]
        row, col = 0, 0
        for i in range(M):
            for j in range(N):
                if col == c:
                    row += 1
                    col = 0
                res[row][col] = nums[i][j]
                col += 1
        return res

# V2 
class Solution(object):
    def matrixReshape(self, nums, r, c):
        """
        :type nums: List[List[int]]
        :type r: int
        :type c: int
        :rtype: List[List[int]]
        """
        if not nums or \
           r*c != len(nums) * len(nums[0]):
            return nums

        result = [[0 for _ in range(c)] for _ in range(r)]
        count = 0
        for i in range(len(nums)):
            for j in range(len(nums[0])):
                result[count/c][count%c] = nums[i][j]
                count += 1
        return result
