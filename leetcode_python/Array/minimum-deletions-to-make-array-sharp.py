"""

4083. Minimum Deletions to Make Array Sharp
Easy

You are given an integer array nums.

You may delete any number of elements from nums. The remaining elements
keep their original order.

The resulting array a is sharp if the bitwise XOR of every pair of
adjacent elements is non-zero. That is, a[i] XOR a[i + 1] != 0 for every
index i such that 0 <= i < a.length - 1. An array with at most one element
is sharp.

Return the minimum number of deletions needed to make nums sharp.

Bitwise XOR compares the corresponding bits of two integers. A bit in the
result is 1 if the corresponding bits differ, and 0 otherwise.


Example 1:

Input: nums = [1,1,2,2,2,3]

Output: 3

Explanation:

The pairs at indices (0, 1), (2, 3), and (3, 4) each have an XOR of 0.
Deleting one 1 and two 2s leaves [1, 2, 3], where 1 XOR 2 == 3 and
2 XOR 3 == 1 are both non-zero. Therefore, the answer is 3.

Example 2:

Input: nums = [4,4,9,4]

Output: 1

Explanation:

The pair at indices (0, 1) has an XOR of 0.
Deleting either of these two 4s leaves [4, 9, 4], where 4 XOR 9 == 13 and
9 XOR 4 == 13 are both non-zero.


Constraints:

2 <= nums.length <= 100

1 <= nums[i] <= 100

"""

# V0
# IDEA : XOR == 0 <=> EQUAL VALUES -> COUNT ADJACENT EQUAL PAIRS
#
#   a XOR b == 0 exactly when a == b, so "sharp" just means
#   "no two adjacent elements are equal".
#
#   a run of k equal values must shrink to 1 -> k - 1 deletions, and every
#   extra member of a run is exactly one "nums[i] == nums[i-1]" pair :
#
#     [1,1,2,2,2,3] -> runs [1,1] [2,2,2] [3] -> 1 + 2 + 0 = 3
#
#   deleting inside a run never makes two DIFFERENT runs touch with equal
#   values (neighbouring runs differ by definition), so runs are independent.
#
# time = O(n), space = O(1)
class Solution(object):
    def minDeletions(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge case: 0 or 1 element is already sharp
        if len(nums) <= 1:
            return 0

        deletions = 0
        for i in range(1, len(nums)):
            # NOTE !!! count the pair right away - the draft only added a run's
            #          count when a DIFFERENT value arrived, so a run at the very
            #          end of nums (e.g. [4,4]) was never added
            if nums[i] == nums[i - 1]:
                deletions += 1

        return deletions
