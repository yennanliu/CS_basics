"""

4058. Maximum Pulse Value After One Subarray Rotation
Medium

You are given an integer array nums of length n.

Define the pulse value of an integer array arr as the alternating sum starting
at index 0: pulse(arr) = arr[0] - arr[1] + arr[2] - arr[3] + ....

You may perform at most one operation on nums:

Choose two indices l and r such that 0 <= l < r < n.
Left-rotate the subarray nums[l..r] by exactly one position. For example,
[a, b, c, d] becomes [b, c, d, a].

Return the maximum pulse value that can be obtained after performing at most
one such operation.

Example 1:

Input: nums = [1,5,2]

Output: 6

Explanation:

The original pulse value is 1 - 5 + 2 = -2.
Rotate the subarray nums[0..1] from [1, 5] to [5, 1].
The resulting array is [5, 1, 2] and its pulse value is 5 - 1 + 2 = 6, which
is the maximum possible.

Example 2:

Input: nums = [6,4,3]

Output: 7

Explanation:

The original pulse value is 6 - 4 + 3 = 5.
Rotate the subarray nums[1..2] from [4, 3] to [3, 4].
The resulting array is [6, 3, 4] and its pulse value is 6 - 3 + 4 = 7, which
is the maximum possible.

Example 3:

Input: nums = [9,7]

Output: 2

Explanation:

The original pulse value is 9 - 7 = 2, which is already maximum. Thus, no
rotation is required.

Constraints:

1 <= n == nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : PREFIX SUM OF SIGN-FLIP GAINS + BEST LEFT END SO FAR
#
#   let sign[i] = +1 on even i, -1 on odd i. left-rotating nums[l..r] :
#
#     - nums[l+1..r] each shift one slot left -> their sign FLIPS,
#       gain  -2 * sign[k] * nums[k]  for every k in (l, r]
#     - nums[l] jumps to slot r -> gain (sign[r] - sign[l]) * nums[l]
#
#   with P[x] = prefix sum of b[k] = -2 * sign[k] * nums[k], the gain is
#
#     P[r] - P[l]  +  sign[r] * nums[l]  -  sign[l] * nums[l]
#
#   so for a fixed r it only needs the best l < r, split by sign[r] :
#
#     best_pos = max over l < r of ( +nums[l] - P[l] - sign[l] * nums[l] )
#     best_neg = max over l < r of ( -nums[l] - P[l] - sign[l] * nums[l] )
#
#   answer = pulse(nums) + max(0, best gain)   (0 = "no operation")
#
# NOTE !!! the rotation is NOT just a swap of two elements : every element in
#          (l, r] flips sign, so a long rotation can gain far more than any
#          adjacent swap.
#          e.g. [0,1,0,1,0,1] : pulse = -3, best swap -> -1, but rotating the
#          whole array gives [1,0,1,0,1,0] -> 3
#
#   e.g. [1,5,2] : pulse = -2, (l, r) = (0, 1) gains (-2*-1*5) + (-1 - 1)*1 = 8 -> 6
#
# time = O(n), space = O(1)
class Solution(object):
    def maxValue(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        n = len(nums)

        base = 0
        for i in range(n):
            base += nums[i] if i % 2 == 0 else -nums[i]

        if n == 1:
            return base

        NEG_INF = float('-inf')

        best_gain = 0     # doing nothing is allowed
        best_pos = NEG_INF
        best_neg = NEG_INF

        pre = 0           # P[k] = sum of b[0..k]
        for k in range(n):
            sign = 1 if k % 2 == 0 else -1
            pre += -2 * sign * nums[k]

            # k as the right end r, using an l < k seen before
            if k > 0:
                best_l = best_pos if sign == 1 else best_neg
                best_gain = max(best_gain, pre + best_l)

            # k as a left end l for later r
            best_pos = max(best_pos, nums[k] - pre - sign * nums[k])
            best_neg = max(best_neg, -nums[k] - pre - sign * nums[k])

        return base + best_gain
