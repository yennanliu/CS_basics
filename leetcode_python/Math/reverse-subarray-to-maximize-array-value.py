"""

1330. Reverse Subarray To Maximize Array Value
Hard

You are given an integer array nums. The value of this array is defined as the sum of
|nums[i] - nums[i + 1]| for all 0 <= i < nums.length - 1.

You are allowed to select any subarray of the given array and reverse it.
You can perform this operation only once.

Find maximum possible value of the final array.


Example 1:

Input: nums = [2,3,1,5,4]
Output: 10
Explanation: By reversing the subarray [3,1,5] the array becomes [2,5,1,3,4] whose value is 10.

Example 2:

Input: nums = [2,4,9,24,2,1,10]
Output: 68


Constraints:

2 <= nums.length <= 3 * 10^4
-10^5 <= nums[i] <= 10^5
The answer is guaranteed to fit in a 32-bit integer.

"""

# V0
# IDEA : MATH (a reversal only rewires 2 boundaries -> maximise the delta)
#
#   reversing nums[i..j] leaves every internal |a - b| untouched. Only the two
#   seams change, so the gain is
#     |nums[i-1] - nums[j]| + |nums[i] - nums[j+1]|
#         - |nums[i-1] - nums[i]| - |nums[j] - nums[j+1]|
#
#   three families to maximise :
#
#   1) prefix reversal (i = 0)  : gain = |nums[0]  - nums[k+1]| - |nums[k] - nums[k+1]|
#   2) suffix reversal (j = n-1): gain = |nums[-1] - nums[k]|   - |nums[k] - nums[k+1]|
#      both are single O(n) sweeps over the seam index k.
#
#   3) interior reversal : expand the absolute values. The gain is positive
#      only when one seam pair is "entirely above" the other, and the best
#      such swap equals
#         2 * ( max over adjacent pairs of min(x, y)
#             - min over adjacent pairs of max(x, y) )
#      NOTE : that expression can be negative -- clamp it away by simply
#             comparing against the untouched base value.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxValueAfterReverse(self, nums):
        n = len(nums)
        base = 0
        for i in range(n - 1):
            base += abs(nums[i] - nums[i + 1])

        res = base

        # reversing a prefix / a suffix : only one seam moves
        for i in range(n - 1):
            seam = abs(nums[i] - nums[i + 1])
            res = max(res, base - seam + abs(nums[0] - nums[i + 1]))
            res = max(res, base - seam + abs(nums[-1] - nums[i]))

        # interior reversal
        max_low = float('-inf')
        min_high = float('inf')
        for i in range(n - 1):
            a, b = nums[i], nums[i + 1]
            max_low = max(max_low, min(a, b))
            min_high = min(min_high, max(a, b))
        res = max(res, base + 2 * (max_low - min_high))

        return res
