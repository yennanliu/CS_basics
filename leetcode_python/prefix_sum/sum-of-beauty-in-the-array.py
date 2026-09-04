"""

2012. Sum of Beauty in the Array
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given a 0-indexed integer array nums. For each index i (1 <= i <= nums.length - 2) the beauty of nums[i] equals:

2, if nums[j] < nums[i] < nums[k], for all 0 <= j < i and for all i < k <= nums.length - 1.
1, if nums[i - 1] < nums[i] < nums[i + 1], and the previous condition is not satisfied.
0, if none of the previous conditions holds.
Return the sum of beauty of all nums[i] where 1 <= i <= nums.length - 2.

 

Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation: For each index i in the range 1 <= i <= 1:
- The beauty of nums[1] equals 2.
Example 2:

Input: nums = [2,4,6,4]
Output: 1
Explanation: For each index i in the range 1 <= i <= 2:
- The beauty of nums[1] equals 1.
- The beauty of nums[2] equals 0.
Example 3:

Input: nums = [3,2,1]
Output: 0
Explanation: For each index i in the range 1 <= i <= 1:
- The beauty of nums[1] equals 0.
 

Constraints:

3 <= nums.length <= 105
1 <= nums[i] <= 105

"""

# V0
class Solution(object):
    def sumOfBeauties(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V1
# IDEA: PREFIX + SUFFIX SUM (GEMINI)
"""
NOTE !!!


1.  LC 2012 判斷 nums[i] 的 beauty 時，不只是看左右相鄰元素，而是看：

nums[i] 是否 大於左邊所有元素
nums[i] 是否 小於右邊所有元素

	-> so we need to keep tracking the `min val` from right to left (<-)


2. core idea:

	for every i:
		->

		 ```
		 left_max[i]  = i 左邊最大的數
		 right_min[i] = i 右邊最小的數
		 ```

"""
class Solution:
    def sumOfBeauties(self, nums: list[int]) -> int:
        n = len(nums)
        
        # 1. 預先計算右側最小值陣列 (Suffix Min)
        suffix_min = [0] * n
        suffix_min[-1] = nums[-1]
        for i in range(n - 2, -1, -1):
            suffix_min[i] = min(suffix_min[i + 1], nums[i])
            
        res = 0
        prefix_max = nums[0]  # 動態維護左側最大值
        
        # 2. 遍歷 1 到 n - 2
        for i in range(1, n - 1):
            # 條件 1：全域判斷 (Beauty = 2)
            if prefix_max < nums[i] < suffix_min[i + 1]:
                res += 2
            # 條件 2：區域判斷 (Beauty = 1)
            elif nums[i - 1] < nums[i] < nums[i + 1]:
                res += 1
                
            # 更新左側最大值
            prefix_max = max(prefix_max, nums[i])
            
        return res

# V2
# IDEA: PREFIX + SUFFIX SUM (GPT)
class Solution(object):
    def sumOfBeauties(self, nums):
        n = len(nums)

        # right_min[i] = nums[i] 右邊所有元素的最小值
        right_min = [float("inf")] * n

        for i in range(n - 2, -1, -1):
            right_min[i] = min(right_min[i + 1], nums[i + 1])

        res = 0
        left_max = nums[0]

        for i in range(1, n - 1):

            # Beauty = 2
            # nums[i] > 左邊所有數字
            # nums[i] < 右邊所有數字
            if left_max < nums[i] < right_min[i]:
                res += 2

            # Beauty = 1
            # 只需要看左右相鄰元素
            elif nums[i - 1] < nums[i] < nums[i + 1]:
                res += 1

            # Beauty = 0
            # do nothing

            left_max = max(left_max, nums[i])

        return res
