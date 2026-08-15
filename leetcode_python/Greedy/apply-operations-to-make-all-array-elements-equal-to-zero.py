"""

2772. Apply Operations to Make All Array Elements Equal to Zero
Medium

You are given a 0-indexed integer array nums and a positive integer k.

You can apply the following operation on the array any number of times:

Choose any subarray of size k from the array and decrease all its elements by 1.

Return true if you can make all the array elements equal to 0, or false otherwise.

A subarray is a contiguous non-empty part of an array.


Example 1:

Input: nums = [2,2,3,1,1,0], k = 3
Output: true
Explanation: We can do the following operations:
- Choose the subarray [2,2,3]. The resulting array will be nums = [1,1,2,1,1,0].
- Choose the subarray [2,1,1]. The resulting array will be nums = [1,1,1,0,0,0].
- Choose the subarray [1,1,1]. The resulting array will be nums = [0,0,0,0,0,0].

Example 2:

Input: nums = [1,3,1,1], k = 2
Output: false
Explanation: It is not possible to make all the array elements equal to 0.


Constraints:

1 <= k <= nums.length <= 10^5
0 <= nums[i] <= 10^6

"""

# V0
# IDEA : LEFT-TO-RIGHT GREEDY + DIFFERENCE ARRAY
#
#   scan left to right. Index i can only ever be touched by windows starting at
#   i - k + 1 .. i, and once we pass i no window starting further right can
#   reach it. So the number of operations starting AT i is forced: whatever
#   value is still left at i after earlier windows have been applied.
#
#       cur = nums[i] + (sum of window deltas currently covering i)
#       cur == 0 -> nothing to do
#       cur  < 0 -> earlier windows overshot     -> False
#       i + k > n -> no room for a window here   -> False
#       else start `cur` operations at i, ending after i + k - 1
#
#   NOTE : applying `cur` operations one cell at a time is O(n * k); instead a
#          difference array records "-cur from i, +cur again at i + k", so the
#          running sum is maintained in O(1) per index.
#   NOTE : nums[i] can be 1e6 and n 1e5, so the counts are large but Python
#          ints are unbounded — nothing to mod or clamp here.
#
# time = O(n), space = O(n)
class Solution(object):
    def checkArray(self, nums, k):
        n = len(nums)
        diff = [0] * (n + 1)           # diff[i] applied when we reach index i
        running = 0                    # total decrease currently covering i

        for i in range(n):
            running += diff[i]
            cur = nums[i] + running
            if cur == 0:
                continue
            if cur < 0 or i + k > n:
                return False
            running -= cur             # start `cur` windows at i
            diff[i + k] += cur         # ... which expire right after i + k - 1

        return True
