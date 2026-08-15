"""

2708. Maximum Strength of a Group
Medium

You are given a 0-indexed integer array nums representing the score of students in an exam. The teacher would like to form one non-empty group of students with maximal strength, where the strength of a group of students of indices i0, i1, i2, ... , ik is defined as nums[i0] * nums[i1] * nums[i2] * ... * nums[ik].

Return the maximum strength of a group the teacher can create.


Example 1:

Input: nums = [3,-1,-5,2,5,-9]
Output: 1350
Explanation: One way to form a group of maximal strength is to group the students at indices [0,2,3,4,5]. Their strength is 3 * (-5) * 2 * 5 * (-9) = 1350, which we can show is optimal.

Example 2:

Input: nums = [-4,-5,-4]
Output: 20
Explanation: Group the students at indices [0, 1] . Then, we'll have a resulting strength of 20. We cannot achieve greater strength.


Constraints:

1 <= nums.length <= 13
-9 <= nums[i] <= 9

"""

# V0
# IDEA : GREEDY (take every positive, pair up the negatives)
#
#   the group is any non-empty SUBSET, so:
#     - every positive number can only help -> always take it
#     - zeros never help (they zero the product) -> never take one, unless
#       nothing else is takeable
#     - negatives are useful in PAIRS (a product of two negatives is positive),
#       so take them all; if their count is ODD, drop exactly one - and the one
#       to drop is the LARGEST negative, i.e. the one closest to 0, since it
#       contributes the smallest magnitude
#
#   NOTE : n == 1 must be answered directly - the group is non-empty, so a
#          lone -5 (or a lone 0) IS the answer.
#   NOTE : if the greedy selection ends up empty (e.g. [0, -3] -> the single
#          negative gets dropped), the best legal group is a single element,
#          and in that case a 0 must be present, so the answer is 0.
#   NOTE : an exhaustive 2^n subset scan is also fine here (n <= 13) but this
#          greedy is O(n log n) and generalises.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxStrength(self, nums):
        if len(nums) == 1:
            return nums[0]

        pos = [x for x in nums if x > 0]
        neg = sorted([x for x in nums if x < 0])
        if len(neg) % 2 == 1:
            neg.pop()          # drop the negative closest to 0

        picked = pos + neg
        if not picked:
            return 0           # only zeros (and at most one dropped negative) left

        ans = 1
        for x in picked:
            ans *= x
        return ans
