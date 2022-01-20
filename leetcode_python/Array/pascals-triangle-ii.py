"""

119. Pascal's Triangle II
Easy

Given an integer rowIndex, return the rowIndexth (0-indexed) row of the Pascal's triangle.

In Pascal's triangle, each number is the sum of the two numbers directly above it as shown:


Example 1:

Input: rowIndex = 3
Output: [1,3,3,1]
Example 2:

Input: rowIndex = 0
Output: [1]
Example 3:

Input: rowIndex = 1
Output: [1,1]
 

Constraints:

0 <= rowIndex <= 33
 

Follow up: Could you optimize your algorithm to use only O(rowIndex) extra space?

"""

# V0
class Solution(object):
    def getRow(self, rowIndex):
        # edge case
        if rowIndex == 0:
            return [1]
        if rowIndex == 1:
            return [1,1]
        res = [1,1]
        for i in range(rowIndex+1):
            if i <= 1:
                continue
            res = [1] + [ res[i] + res[i+1] for i in range(len(res)-1) ] + [1]
            print (res)
        #print ("--> res = " + str(res))
        return res

# V0
class Solution(object):
    def getRow(self, rowIndex):
        init = [[1], [1,1]]
        if rowIndex <= 1:
            return init[rowIndex]
        for i in range(rowIndex - 1):
            # get from last Pascal layer
            tmp = init[-1]
            # note this
            _tmp = [1] + [tmp[x] + tmp[x+1] for x in range(len(tmp)-1)] + [1]
            init.append(_tmp)
        return init[-1]

# V0 
class Solution(object):
    def getRow(self, rowIndex):
        res = [[1 for j in range(i + 1)] for i in range(rowIndex + 1)]
        for i in range(2, rowIndex + 1):
            for j in range(1, i):
                res[i][j] = res[i - 1][j - 1] + res[i - 1][j]
        return res[-1]

# V0'
# a bit different in index init
class Solution(object):
    def getRow(self, rowIndex):
        res = [[1 for j in range(i + 1)] for i in range(rowIndex + 1)]
        for i in range(2, rowIndex + 1):
            for j in range(i-1):
                res[i][j+1] = res[i - 1][j] + res[i - 1][j+1]
        return res[-1]

# V0''
class Solution(object):
    def getRow(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        res = [1] + [0] * rowIndex
        for i in range(rowIndex):
            res[0] = 1
            for j in range(i+1, 0, -1):
                res[j] = res[j] + res[j-1]
        return res

# V1 
# https://blog.csdn.net/coder_orz/article/details/51591374
# IDEA : FOR PASCALS TRIANGLE P(n) 
# -> p(n) = P(n-1) + shift(P(n-1))
# i.e.   
#     1 3 3 1 0 
#  +  0 1 3 3 1
# ------------------
#  =  1 4 6 4 1
class Solution(object):
    def getRow(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        res = [1] + [0] * rowIndex
        for i in range(rowIndex):
            res[0] = 1
            for j in range(i+1, 0, -1):
                res[j] = res[j] + res[j-1]
        return res

### Test case :
s=Solution()
assert s.getRow(0) == [1]
assert s.getRow(1) == [1,1]
assert s.getRow(2) == [1,2,1]
assert s.getRow(3) == [1,3,3,1]
assert s.getRow(4) == [1,4,6,4,1]

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/51348629
# IDEA : CALCULATE WHOLE PASCALS TRIANGLE AND GET THE LAST ONE
class Solution(object):
    def getRow(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        res = [[1 for j in range(i + 1)] for i in range(rowIndex + 1)]
        for i in range(2, rowIndex + 1):
            for j in range(1, i):
                res[i][j] = res[i - 1][j - 1] + res[i - 1][j]
        return res[-1]

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/51348629
# IDEA : ONLY THE Nth LAYER OF PASCALS TRIANGLE 
class Solution(object):
    def getRow(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        res = [1] * (rowIndex + 1)
        for i in range(2, rowIndex + 1):
            for j in range(i - 1, 0, -1):
                res[j] += res[j - 1]
        return res

# V1'''
# https://blog.csdn.net/coder_orz/article/details/51591374
# IDEA : PASCALS TRIANGLE PROPERTY C(n, m)
# IDEA  - > C(n, m) = n!/(m! * (n-m)!)
# so C(n, m-1) = n!/((m-1)! * (n-m+1)!)
# -> C(n, m) = C(n, m-1) * (n-m+1) / m
# and we can use below property reduce half of computation 
# C(n, m) == C(n, n-m)
class Solution(object):
    def getRow(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        res = [1] * (rowIndex+1)
        for i in range(1, rowIndex/2+1):
            res[i] = res[rowIndex-i] = res[i-1] * (rowIndex-i+1) / i
        return res

# V2 
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    # @return a list of integers
    def getRow(self, rowIndex):
        result = [0] * (rowIndex + 1)
        for i in range(rowIndex + 1):
            old = result[0] = 1
            for j in range(1, i + 1):
                old, result[j] = result[j], old + result[j]
        return result

    def getRow2(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        row = [1]
        for _ in range(rowIndex):
            row = [x + y for x, y in zip([0] + row, row + [0])]
        return row

    def getRow3(self, rowIndex):
        """
        :type rowIndex: int
        :rtype: List[int]
        """
        if rowIndex == 0: return [1]
        res = [1, 1]

        def add(nums):
            res = nums[:1]
            for i, j in enumerate(nums):
                if i < len(nums) - 1:
                    res += [nums[i] + nums[i + 1]]
            res += nums[:1]
            return res

        while res[1] < rowIndex:
            res = add(res)
        return res

# Time:  O(n^2)
# Space: O(n)
class Solution2(object):
    # @return a list of integers
    def getRow(self, rowIndex):
        result = [1]
        for i in range(1, rowIndex + 1):
            result = [1] + [result[j - 1] + result[j] for j in range(1, i)] + [1]
        return result