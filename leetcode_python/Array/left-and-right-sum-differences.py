"""

2574. Left and Right Sum Differences
Easy

You are given a 0-indexed integer array nums of size n.

Define two arrays leftSum and rightSum where:

leftSum[i] is the sum of elements to the left of the index i in the array nums. If there is no such element, leftSum[i] = 0.
rightSum[i] is the sum of elements to the right of the index i in the array nums. If there is no such element, rightSum[i] = 0.

Return an integer array answer of size n where answer[i] = |leftSum[i] - rightSum[i]|.


Example 1:

Input: nums = [10,4,8,3]
Output: [15,1,11,22]
Explanation: The array leftSum is [0,10,14,22] and the array rightSum is [15,11,3,0].
The array answer is [|0 - 15|,|10 - 11|,|14 - 3|,|22 - 0|] = [15,1,11,22].

Example 2:

Input: nums = [1]
Output: [0]
Explanation: The array leftSum is [0] and the array rightSum is [0].
The array answer is [|0 - 0|] = [0].


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : RUNNING PREFIX / SUFFIX SUM IN ONE PASS
#
#   Both leftSum[i] and rightSum[i] EXCLUDE nums[i] itself, and together with
#   nums[i] they partition the whole array:
#
#       leftSum[i] + nums[i] + rightSum[i] = total
#
#   So we never need two stored arrays - carry two scalars instead:
#     l = sum of everything strictly before i  (starts at 0)
#     r = sum of everything strictly after i   (starts at total)
#
#   NOTE : the ORDER of updates inside the loop is what makes it work.
#          Subtract nums[i] from r FIRST (r now excludes i), record the answer,
#          and only THEN add nums[i] into l (l must still exclude i when the
#          answer is recorded).
#
#   NOTE : the two boundary cases fall out for free - at i = 0, l is still 0,
#          and at i = n-1, r has been drained to 0.
#
# time = O(n), space = O(1) beyond the output
class Solution(object):
    def leftRightDifference(self, nums):
        l, r = 0, sum(nums)
        res = []
        for x in nums:
            r -= x           # r now excludes the current element
            res.append(abs(l - r))
            l += x           # only now does l absorb the current element
        return res
