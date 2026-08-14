"""

2216. Minimum Deletions to Make Array Beautiful
Medium

You are given a 0-indexed integer array nums. The array nums is beautiful if:

nums.length is even.
nums[i] != nums[i + 1] for all i % 2 == 0.

Note that an empty array is considered beautiful.

You can delete any number of elements from nums. When you delete an element, all the elements to the right of the deleted element will be shifted one unit to the left to fill the gap created and all the elements to the left of the deleted element will remain unchanged.

Return the minimum number of elements to delete from nums to make it beautiful.


Example 1:

Input: nums = [1,1,2,3,5]
Output: 1
Explanation: You can delete either nums[0] or nums[1] to make nums = [1,2,3,5] which is beautiful. It can be proven you need at least 1 deletion to make nums beautiful.

Example 2:

Input: nums = [1,1,2,2,3,3]
Output: 2
Explanation: You can delete nums[0] and nums[5] to make nums = [1,2,2,3] which is beautiful. It can be proven you need at least 2 deletions to make nums beautiful.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : GREEDY LEFT-TO-RIGHT PAIRING, THEN FIX THE PARITY AT THE END
#
#   walk the array building pairs. at the current even slot i :
#     nums[i] != nums[i+1] -> the pair is fine, jump ahead by 2
#     nums[i] == nums[i+1] -> one of them must go. deleting nums[i] shifts
#                             everything left, so the next candidate partner
#                             for the surviving element is nums[i+2]; that is
#                             the same as advancing by 1 and paying 1 deletion
#
#   deleting the FIRST of an equal pair is never worse than deleting the
#   second (the survivor gets the widest choice of partners), which makes the
#   single pass optimal.
#
#   finally, the array must have EVEN length, so if what remains is odd, drop
#   one more element from the end.
#
# time = O(n), space = O(1)
class Solution(object):
    def minDeletion(self, nums):
        n = len(nums)
        res = 0
        i = 0
        while i < n - 1:
            if nums[i] == nums[i + 1]:
                res += 1
                i += 1
            else:
                i += 2
        if (n - res) % 2:
            res += 1
        return res
