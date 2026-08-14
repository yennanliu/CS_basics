"""

1911. Maximum Alternating Subsequence Sum
Medium

The alternating sum of a 0-indexed array is defined as the sum of the elements at even indices minus the sum of the elements at odd indices.

For example, the alternating sum of [4,2,5,3] is (4 + 5) - (2 + 3) = 4.

Given an array nums, return the maximum alternating sum of any subsequence of nums (after reindexing the elements of the subsequence).

A subsequence of an array is a new array generated from the original array by deleting some elements (possibly none) without changing the remaining elements' relative order. For example, [2,7,4] is a subsequence of [4,2,3,7,2,1,4] (the underlined elements), while [2,4,2] is not.


Example 1:

Input: nums = [4,2,5,3]
Output: 7
Explanation: It is optimal to choose the subsequence [4,2,5] with alternating sum (4 + 5) - 2 = 7.

Example 2:

Input: nums = [5,6,7,8]
Output: 8
Explanation: It is optimal to choose the subsequence [8] with alternating sum 8.

Example 3:

Input: nums = [6,2,1,2,4,5]
Output: 10
Explanation: It is optimal to choose the subsequence [6,1,5] with alternating sum (6 + 5) - 1 = 10.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : DP ON PARITY OF THE PICKED LENGTH (2 rolling states, "stock trading" shape)
#
#   scan left -> right, keep two best values :
#     g = best alternating sum of a subsequence with an EVEN length so far
#         (i.e. the next element picked would be added, index parity 0)
#     f = best alternating sum of a subsequence with an ODD length so far
#         (i.e. the next element picked would be subtracted)
#
#   for each x :
#     g' = max(g, f - x)     # x lands on an odd index -> subtracted
#     f' = max(f, g + x)     # x lands on an even index -> added
#
#   NOTE : both must be updated from the OLD pair simultaneously.
#          nums[i] >= 1 so the answer is f (odd length always wins), but
#          max(f, g) is the safe form.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAlternatingSum(self, nums):
        # f : last picked index is even (added), g : last picked index is odd
        f, g = 0, 0
        for x in nums:
            f, g = max(f, g + x), max(g, f - x)
        return max(f, g)
