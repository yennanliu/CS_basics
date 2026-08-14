"""

2488. Count Subarrays With Median K
Hard

You are given an array nums of size n consisting of distinct integers from 1 to n and a positive integer k.

Return the number of non-empty subarrays in nums that have a median equal to k.

Note:

The median of an array is the middle element after sorting the array in ascending order. If the array is of even length, the median is the left middle element.
For example, the median of [2,3,1,4] is 2, and the median of [8,4,3,5,1] is 4.
A subarray is a contiguous part of an array.


Example 1:

Input: nums = [3,2,1,4,5], k = 4
Output: 3
Explanation: The subarrays that have a median equal to 4 are: [4], [4,5] and [1,4,5].

Example 2:

Input: nums = [2,3,1], k = 3
Output: 1
Explanation: [3] is the only subarray that has a median equal to 3.


Constraints:

n == nums.length
1 <= n <= 10^5
1 <= nums[i], k <= n
The integers in nums are distinct.

"""

# V0
# IDEA : ONLY THE BALANCE OF "BIGGER THAN k" vs "SMALLER THAN k" MATTERS
#
#   every valid subarray must contain k (it is the median), and the values
#   are distinct, so score each element +1 if it exceeds k and -1 if it is
#   below. let `balance` be that sum over the subarray.
#
#   with `less` values below and `more` above, k sits at sorted position
#   `less`. matching that against the required median position gives
#       odd length  -> less == more        -> balance == 0
#       even length -> less == more - 1    -> balance == 1
#   (the left middle is the median, hence the extra element lands ABOVE k).
#
#   so : tally the balances of every prefix walking LEFT from k's position,
#   then sweep RIGHT accumulating the other half and look up how many left
#   parts complete it to 0 or 1.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def countSubarrays(self, nums, k):
        pos = nums.index(k)

        left = Counter()
        left[0] = 1                    # take nothing to the left of k
        balance = 0
        for i in range(pos - 1, -1, -1):
            balance += 1 if nums[i] > k else -1
            left[balance] += 1

        res = 0
        balance = 0
        res += left[0] + left[1]       # take nothing to the right of k
        for j in range(pos + 1, len(nums)):
            balance += 1 if nums[j] > k else -1
            res += left[-balance] + left[1 - balance]
        return res
