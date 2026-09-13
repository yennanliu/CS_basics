"""

910. Smallest Range II
Medium

You are given an integer array nums and an integer k.

For each index i where 0 <= i < nums.length, change nums[i] to be either nums[i] + k or nums[i] - k.

The score of nums is the difference between the maximum and minimum elements in nums.

Return the minimum score of nums after changing the values at each index.

Example 1:

Input: nums = [1], k = 0
Output: 0
Explanation: The score is max(nums) - min(nums) = 1 - 1 = 0.

Example 2:

Input: nums = [0,10], k = 2
Output: 6
Explanation: Change nums to be [2, 8]. The score is max(nums) - min(nums) = 8 - 2 = 6.

Example 3:

Input: nums = [1,3,6], k = 3
Output: 3
Explanation: Change nums to be [4, 6, 3]. The score is max(nums) - min(nums) = 6 - 3 = 3.

Constraints:

1 <= nums.length <= 10^4
0 <= nums[i] <= 10^4
0 <= k <= 10^4

"""

# V0

# V1 : DEV 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/84998077
# time = O(n log n)  # n = len(A), sorting
# space = O(1)  # in-place sort
class Solution(object):
    def smallestRangeII(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        A.sort()
        N = len(A)
        mn, mx = A[0], A[-1]
        res = mx - mn
        for i in range(N - 1):
            mx = max(A[i] + 2 * K, mx)
            mn = min(A[i + 1], A[0] + 2 * K)
            res = min(mx - mn, res)
        return res

# V3
# time = O(nlogn)
# space = O(1)

class Solution(object):
    def smallestRangeII(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        A.sort()
        result = A[-1]-A[0]
        for i in range(len(A)-1):
            result = min(result,
                         max(A[-1]-K, A[i]+K) -
                         min(A[0]+K, A[i+1]-K))
        return result
