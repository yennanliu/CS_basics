# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/description/

"""

1031. Maximum Sum of Two Non-Overlapping Subarrays
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given an integer array nums and two integers firstLen and secondLen, return the maximum sum of elements in two non-overlapping subarrays with lengths firstLen and secondLen.

The array with length firstLen could occur before or after the array with length secondLen, but they have to be non-overlapping.

A subarray is a contiguous part of an array.

 

Example 1:

Input: nums = [0,6,5,2,2,5,1,9,4], firstLen = 1, secondLen = 2
Output: 20
Explanation: One choice of subarrays is [9] with length 1, and [6,5] with length 2.
Example 2:

Input: nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
Output: 29
Explanation: One choice of subarrays is [3,8,1] with length 3, and [8,9] with length 2.
Example 3:

Input: nums = [2,1,5,6,0,9,5,0,3,8], firstLen = 4, secondLen = 3
Output: 31
Explanation: One choice of subarrays is [5,6,0,9] with length 4, and [0,3,8] with length 3.
 

Constraints:

1 <= firstLen, secondLen <= 1000
2 <= firstLen + secondLen <= 1000
firstLen + secondLen <= nums.length <= 1000
0 <= nums[i] <= 1000


"""


# V0
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        """
        :type nums: List[int]
        :type firstLen: int
        :type secondLen: int
        :rtype: int
        """
        pass


# V1


# V2
# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/6575097/maximum-sum-of-two-non-overlapping-subar-abkc/
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        """
        :type nums: List[int]
        :type firstLen: int
        :type secondLen: int
        :rtype: int
        """
        def getMaxSum(first, second):
            maxFirst, res = 0, 0
            prefix = [0] * (len(nums) + 1)  

            for i in range(len(nums)):
                prefix[i + 1] = prefix[i] + nums[i]

            for i in range(first + second, len(nums) + 1):
                maxFirst = max(maxFirst, prefix[i - second] - prefix[i - first - second])
                res = max(res, maxFirst + prefix[i] - prefix[i - second])
            return res

        return max(getMaxSum(firstLen, secondLen), getMaxSum(secondLen, firstLen))


# V3
# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/760194/python-clean-on-98-by-warmr0bot-9k9z/
class Solution:
    def maxSumTwoNoOverlap(self, A: List[int], L: int, M: int) -> int:
        prefix = [0]
        maxl = maxm = summ = 0
        for x in A:
            prefix.append(prefix[-1] + x)
        
        for x in range(M, len(prefix) - L):
            maxm = max(maxm, prefix[x] - prefix[x - M])
            summ = max(summ, maxm + prefix[x + L] - prefix[x])
        
        for x in range(L, len(prefix) - M):
            maxl = max(maxl, prefix[x] - prefix[x - L])
            summ = max(summ, maxl + prefix[x + M] - prefix[x])
        
        return summ
