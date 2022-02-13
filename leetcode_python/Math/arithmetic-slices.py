"""

413. Arithmetic Slices
Medium

An integer array is called arithmetic if it consists of at least three elements and if the difference between any two consecutive elements is the same.

For example, [1,3,5,7,9], [7,7,7,7], and [3,-1,-5,-9] are arithmetic sequences.
Given an integer array nums, return the number of arithmetic subarrays of nums.

A subarray is a contiguous subsequence of the array.

 

Example 1:

Input: nums = [1,2,3,4]
Output: 3
Explanation: We have 3 arithmetic slices in nums: [1, 2, 3], [2, 3, 4] and [1,2,3,4] itself.
Example 2:

Input: nums = [1]
Output: 0
 

Constraints:

1 <= nums.length <= 5000
-1000 <= nums[i] <= 1000

"""

# V0

# V0'
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        # edge case
        if len(A) < 3:
            return 0
        count = 0
        addend = 0
        for i in range(len(A) - 2):
            if A[i + 1] - A[i] == A[i + 2] - A[i + 1]:
                addend += 1
                count += addend
            else:
                addend = 0
        return count

# V1

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79404220
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        N = len(A)
        self.res = 0
        self.slices(A, N - 1)
        return self.res
    
    def slices(self, A, end):
        if end < 2: return 0
        op = 0
        if A[end] - A[end - 1] == A[end - 1] - A[end - 2]:
            op = 1 + self.slices(A, end - 1)
            self.res += op 
        else:
            self.slices(A, end - 1)
        return op

# V2'
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        count = 0
        addend = 0
        for i in range(len(A) - 2):
            if A[i + 1] - A[i] == A[i + 2] - A[i + 1]:
                addend += 1
                count += addend
            else:
                addend = 0
        return count

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        res, i = 0, 0
        while i+2 < len(A):
            start = i
            while i+2 < len(A) and A[i+2] + A[i] == 2*A[i+1]:
                res += i - start + 1
                i += 1
            i += 1
        return res