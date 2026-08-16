"""

2856. Minimum Array Length After Pair Removals
Medium

Given an integer array num sorted in non-decreasing order.

You can perform the following operation any number of times:

Choose two indices, i and j, where nums[i] < nums[j].
Then, remove the elements at indices i and j from nums. The remaining elements retain their original order, and the array is re-indexed.

Return the minimum length of nums after applying the operation zero or more times.


Example 1:

Input: nums = [1,2,3,4]
Output: 0

Example 2:

Input: nums = [1,1,2,2,3,3]
Output: 0

Example 3:

Input: nums = [1000000000,1000000000]
Output: 2
Explanation: Since both numbers are equal, they cannot be removed.

Example 4:

Input: nums = [2,3,4,4,4]
Output: 1


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
nums is sorted in non-decreasing order.

"""

# V0
# IDEA : MAJORITY-ELEMENT COUNTING (greedy pairing argument)
#
#   only equal values block a removal, so think of the array as groups of
#   equal values with sizes c_1, c_2, ... and let mx = max(c_i), n = len(nums).
#
#   - if mx * 2 <= n, no single value dominates: we can always pair items
#     from two different groups (repeatedly pop from the two currently
#     largest groups), so we remove everything except a leftover element
#     when n is odd -> answer n % 2.
#   - if mx * 2 > n, the dominant group has more members than all the other
#     groups combined. Every removal consumes at most one of them, so at
#     best all (n - mx) other elements are paired off against dominant ones,
#     leaving mx - (n - mx) = 2 * mx - n elements -> answer 2 * mx - n.
#
#   NOTE : nums is already SORTED, so equal values are contiguous and the
#          largest group size can be found with one linear scan — no hash
#          map and no heap needed, which matters at n = 10^5.
#
# time = O(n), space = O(1)
class Solution(object):
    def minLengthAfterRemovals(self, nums):
        n = len(nums)
        mx = 0
        run = 0
        prev = None
        for x in nums:
            if x == prev:
                run += 1
            else:
                run = 1
                prev = x
            if run > mx:
                mx = run

        if mx * 2 > n:
            return 2 * mx - n
        return n % 2
