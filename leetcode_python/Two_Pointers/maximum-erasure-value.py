"""

1695. Maximum Erasure Value
Medium

You are given an array of positive integers nums and want to erase a subarray containing unique
elements. The score you get by erasing the subarray is equal to the sum of its elements.

Return the maximum score you can get by erasing exactly one subarray.

An array b is called to be a subarray of a if it forms a contiguous subsequence of a, that is, if it
is equal to a[l],a[l+1],...,a[r] for some (l,r).


Example 1:

Input: nums = [4,2,4,5,6]
Output: 17
Explanation: The optimal subarray here is [2,4,5,6].

Example 2:

Input: nums = [5,2,1,2,5,2,1,2,5]
Output: 8
Explanation: The optimal subarray here is [5,2,1] or [1,2,5].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : SLIDING WINDOW / TWO POINTERS (longest-unique-window, but scored by sum)
#
#   this is "longest substring without repeating characters" with sum instead of
#   length. keep a window [l, r] whose values are all distinct:
#     - extend r, adding nums[r] to the running sum
#     - while nums[r] is already inside, shrink from the left, subtracting values
#     - record the running sum as a candidate answer
#
#   NOTE : values are POSITIVE, so a bigger window is never worse -- the maximum
#          is always attained on a maximal unique window, which the sweep visits.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumUniqueSubarray(self, nums):
        seen = set()
        left = 0
        cur = 0
        best = 0
        for v in nums:
            while v in seen:
                seen.remove(nums[left])
                cur -= nums[left]
                left += 1
            seen.add(v)
            cur += v
            if cur > best:
                best = cur
        return best
