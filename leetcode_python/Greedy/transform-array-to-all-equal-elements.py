"""

3576. Transform Array to All Equal Elements
Medium

You are given an integer array nums of size n containing only 1 and -1, and an integer k.

You can perform the following operation at most k times:

Choose an index i (0 <= i < n - 1), and multiply both nums[i] and nums[i + 1] by -1.

Note that you can choose the same index i more than once in different operations.

Return true if it is possible to make all elements of the array equal after at most k operations, and false otherwise.


Example 1:

Input: nums = [1,-1,1,-1,1], k = 3
Output: True
Explanation:
We can make all elements in the array equal in 2 operations as follows:
Choose index i = 1, and multiply both nums[1] and nums[2] by -1. Now nums = [1,1,-1,-1,1].
Choose index i = 2, and multiply both nums[2] and nums[3] by -1. Now nums = [1,1,1,1,1].

Example 2:

Input: nums = [-1,-1,-1,1,1,1], k = 5
Output: False
Explanation:
It is not possible to make all elements equal in at most 5 operations.


Constraints:

1 <= n == nums.length <= 10^5
nums[i] is either -1 or 1.
1 <= k <= n

"""

# V0
# IDEA : LEFT-TO-RIGHT FORCED SWEEP, ONE TARGET SIGN AT A TIME
#
#   index 0 can only ever be touched by the operation at i = 0, so once we
#   fix the target sign the decision at every position is forced: if
#   nums[i] already differs from the target, the operation at i must be
#   applied (and applying it twice is pointless). that makes the number of
#   operations for a given target unique — no search needed.
#
#   after the sweep the last element is whatever it is; if it does not match
#   the target the sign is unreachable.
#
# time = O(n), space = O(1)
class Solution(object):
    def canMakeEqual(self, nums, k):
        n = len(nums)

        def cost(target):
            flip = 1
            used = 0
            for i in range(n - 1):
                v = nums[i] * flip
                if v != target:
                    used += 1
                    if used > k:
                        return None
                    flip = -1
                else:
                    flip = 1
            return used if nums[n - 1] * flip == target else None

        return cost(1) is not None or cost(-1) is not None
