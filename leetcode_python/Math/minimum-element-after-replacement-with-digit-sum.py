"""

3300. Minimum Element After Replacement With Digit Sum
Easy

You are given an integer array nums.

You replace each element in nums with the sum of its digits.

Return the minimum element in nums after all replacements.


Example 1:

Input: nums = [10,12,13,14]
Output: 1
Explanation:
nums becomes [1, 3, 4, 5] after all replacements, with minimum element 1.

Example 2:

Input: nums = [1,2,3,4]
Output: 1
Explanation:
nums becomes [1, 2, 3, 4] after all replacements, with minimum element 1.

Example 3:

Input: nums = [999,19,199]
Output: 10
Explanation:
nums becomes [27, 10, 19] after all replacements, with minimum element 10.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : DIGIT SUM VIA THE DECIMAL STRING, THEN TAKE THE MINIMUM
#
#   the replacement is applied once (not repeated to a single digit), so the
#   digit sum of each value is computed directly and min() finishes it.
#
# time = O(n * digits), space = O(1)
class Solution(object):
    def minElement(self, nums):
        return min(sum(int(ch) for ch in str(v)) for v in nums)
