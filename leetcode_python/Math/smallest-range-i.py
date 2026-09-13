"""

908. Smallest Range I
Easy

You are given an integer array nums and an integer k.

In one operation, you can choose any index i where 0 <= i < nums.length and change nums[i] to nums[i] + x where x is an integer from the range [-k, k]. You can apply this operation at most once for each index i.

The score of nums is the difference between the maximum and minimum elements in nums.

Return the minimum score of nums after applying the mentioned operation at most once for each index in it.

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
Output: 0
Explanation: Change nums to be [4, 4, 4]. The score is max(nums) - min(nums) = 4 - 4 = 0.

Constraints:

1 <= nums.length <= 10^4
0 <= nums[i] <= 10^4
0 <= k <= 10^4

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82824685
# time = O(n)
# space = O(1)
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        return max(max(A) - min(A) - 2 * K, 0)
        
# V1'
# https://www.cnblogs.com/seyjs/p/9692725.html
# time = O(n)
# space = O(1)
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        minv,maxv = A[0] + K, A[0] - K
        for i in A:
            minv = min(minv,i+K)
            maxv = max(maxv,i-K)
        return max(0,maxv-minv)

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def smallestRangeI(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        return max(0, max(A) - min(A) - 2*K)