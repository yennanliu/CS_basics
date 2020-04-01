# Time:  O(n^2)
# Space: O(1)
#
# Given numRows, generate the first numRows of Pascal's triangle.
#
# For example, given numRows = 5,
# Return
#
# [
#      [1],
#     [1,1],
#    [1,2,1],
#   [1,3,3,1],
#  [1,4,6,4,1]
# ]
#

# V0 


# V1
# https://blog.csdn.net/coder_orz/article/details/51589254
# IDEA : CONSIDER EACH ROW OF PASCALS TRIANGLE (n > 1)
# ARE ALWAYS STARTING AND AND END WITH 1, i.e. [1,a,b,c,...,1]
# [
#      [1],
#     [1,1],
#    [1,2,1],
#   [1,3,3,1],
#  [1,4,6,4,1]
# ]
# GIVEN res[i][j] = res[i-1][j-1] + res[i-1][j]
# -> res[2] = [1,2,1]
# -> res[3] =[1,3,3,1]  = [1,res[2][0]+res[2][1],res[2][1]+res[2][2],1]
# DEMO
# In [21]: numRows = 5 
#     ...: res = []
#     ...: for i in range(0, numRows):
#     ...:     res.append([1]*(i+1))
#     ...: 
#     ...: print (res)
#     ...: 
# [[1], [1, 1], [1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1, 1]]
class Solution(object):
    def generate(self, numRows):
        """
        :type numRows: int
        :rtype: List[List[int]]
        """
        res = []
        for i in range(0, numRows):
            res.append([1]*(i+1))
            # for j in range(1, i) -> make sure j start only when i >= 2 
            for j in range(1, i):
                res[i][j] = res[i-1][j-1] + res[i-1][j]
        return res

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51589254
# IDEA : PASCALS RULE 
class Solution(object):
    def generate(self, numRows):
        """
        :type numRows: int
        :rtype: List[List[int]]
        """
        if numRows == 0:
            return []
        res = [[1]]
        for i in range(1, numRows):
            res.append([])
            for j in range(i+1):
                res[i].append((res[i-1][j-1] if j>0 else 0) + (res[i-1][j] if j<i else 0))
        return res

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51589254
# IDEA : MAP 
# IDEA : FOR PASCALS TRIANGLE P(n) 
# -> p(n) = P(n-1) + shift(P(n-1))
# i.e.   
#     1 3 3 1 0 
#  +  0 1 3 3 1
# ------------------
#  =  1 4 6 4 1
class Solution(object):
    def generate(self, numRows):
        """
        :type numRows: int
        :rtype: List[List[int]]
        """
        res = [[1]]
        for i in range(1, numRows):
            res += [map(lambda x, y: x+y, res[-1] + [0], [0] + res[-1])]
        return res[:numRows]

# V1'''
# IDEA : RECURSION
# https://stackoverflow.com/questions/30036082/creating-pascals-triangle-using-python-recursion
class Solution(object):
    def pascals_triangle(self, rows):

        def combination(n, k):
            if k == 0 or k == n:
                return 1
            return combination(n - 1, k - 1) + combination(n - 1, k)
        
        for row in range(rows):
            answer = ""
            for column in range( row + 1):
                answer = answer + str(combination(row, column)) + "\t"
            print(answer)

# V2 
# Time:  O(n^2)
# Space: O(1) 
class Solution(object):
    # @return a list of lists of integers
    def generate(self, numRows):
        result = []
        for i in range(numRows):
            result.append([])
            for j in range(i + 1):
                if j in (0, i):
                    result[i].append(1)
                else:
                    result[i].append(result[i - 1][j - 1] + result[i - 1][j])
        return result

    def generate2(self, numRows):
        if not numRows: return []
        res = [[1]]
        for i in range(1, numRows):
            res += [map(lambda x, y: x + y, res[-1] + [0], [0] + res[-1])]
        return res[:numRows]

    def generate3(self, numRows):
        """
        :type numRows: int
        :rtype: List[List[int]]
        """
        if numRows == 0: return []
        if numRows == 1: return [[1]]
        res = [[1], [1, 1]]

        def add(nums):
            res = nums[:1]
            for i, j in enumerate(nums):
                if i < len(nums) - 1:
                    res += [nums[i] + nums[i + 1]]
            res += nums[:1]
            return res

        while len(res) < numRows:
            res.extend([add(res[-1])])
        return res
