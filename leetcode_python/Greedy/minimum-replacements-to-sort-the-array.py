"""

2366. Minimum Replacements to Sort the Array
Hard

You are given a 0-indexed integer array nums. In one operation you can replace any element of the array with any two elements that sum to it.

For example, consider nums = [5,6,7]. In one operation, we can replace nums[1] with 2 and 4 and convert nums to [5,2,4,7].

Return the minimum number of operations to make an array that is sorted in non-decreasing order.


Example 1:

Input: nums = [3,9,3]
Output: 2
Explanation: Here are the steps to sort the array in non-decreasing order:
- From [3,9,3], replace the 9 with 3 and 6 so the array becomes [3,3,6,3]
- From [3,3,6,3], replace the 6 with 3 and 3 so the array becomes [3,3,3,3,3]
There are 2 steps to sort the array in non-decreasing order. Therefore, we return 2.

Example 2:

Input: nums = [1,2,3,4,5]
Output: 0
Explanation: The array is already in non-decreasing order. Therefore, we return 0.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : GREEDY RIGHT-TO-LEFT (split each value into as few parts as possible,
#        and make the LEFTMOST part as large as possible)
#
#   The last element is never worth splitting, so sweep from the right keeping
#   mx = the value the current element must not exceed.
#
#   For nums[i] > mx we must cut it into k pieces, each <= mx:
#       k = ceil(nums[i] / mx)          -> k - 1 operations
#   Among all balanced splits into k parts, the best we can do for the piece
#   that ends up FIRST (leftmost) is floor(nums[i] / k) -- spreading the
#   remainder to the right keeps the sequence non-decreasing and leaves the
#   largest possible ceiling for whatever comes before i.
#   So mx becomes nums[i] // k.
#
#   NOTE : minimising k is safe because a bigger k only lowers nums[i]//k,
#          which makes the prefix strictly harder.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumReplacement(self, nums):
        res = 0
        mx = nums[-1]
        for i in range(len(nums) - 2, -1, -1):
            v = nums[i]
            if v <= mx:
                mx = v
                continue
            k = (v + mx - 1) // mx
            res += k - 1
            mx = v // k
        return res
