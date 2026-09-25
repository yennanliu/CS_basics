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


# V0-1
# IDEA: alternating prefix sum (GPT)
"""
CORE IDEA:

LeetCode 的官方 hint 其實就是利用 alternating prefix sum，
把每個 (l, r) 的 rotation gain 在 O(1) 算出來，再用 parity 分組維護最大 prefix。

"""
class Solution(object):
    def maxValue(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """

        n = len(nums)

        if n == 1:
            return nums[0]

        # P[i] = alternating sum of nums[0:i]
        #
        # P[0] = 0
        # P[1] = nums[0]
        # P[2] = nums[0] - nums[1]
        # P[3] = nums[0] - nums[1] + nums[2]
        prefix = [0] * (n + 1)

        for i in range(n):
            if i % 2 == 0:
                prefix[i + 1] = prefix[i] + nums[i]
            else:
                prefix[i + 1] = prefix[i] - nums[i]

        # No rotation
        max_sum = prefix[n]

        # best_prefix[l_parity]
        #
        # Used for:
        # l and r have different parity
        #
        # gain = 2 * (P[l] - P[r + 1])
        best_prefix = [None, None]

        # best_prefix_plus[l_parity]
        #
        # Used for:
        # l and r have same parity
        #
        # gain = 2 * (P[l + 1] - P[r + 1])
        best_prefix_plus = [None, None]

        for r in range(1, n):

            # Add l = r - 1
            # so that l < r
            l = r - 1
            parity = l % 2

            if best_prefix[parity] is None:
                best_prefix[parity] = prefix[l]
            else:
                best_prefix[parity] = max(
                    best_prefix[parity],
                    prefix[l]
                )

            if best_prefix_plus[parity] is None:
                best_prefix_plus[parity] = prefix[l + 1]
            else:
                best_prefix_plus[parity] = max(
                    best_prefix_plus[parity],
                    prefix[l + 1]
                )

            # Case 1:
            # l and r have the same parity
            #
            # gain = 2 * (P[l + 1] - P[r + 1])
            parity = r % 2

            if best_prefix_plus[parity] is not None:
                gain = 2 * (
                    best_prefix_plus[parity]
                    - prefix[r + 1]
                )

                max_sum = max(
                    max_sum,
                    prefix[n] + gain
                )

            # Case 2:
            # l and r have different parity
            #
            # gain = 2 * (P[l] - P[r + 1])
            opposite = 1 - (r % 2)

            if best_prefix[opposite] is not None:
                gain = 2 * (
                    best_prefix[opposite]
                    - prefix[r + 1]
                )

                max_sum = max(
                    max_sum,
                    prefix[n] + gain
                )

        return max_sum


# V0-2
# IDEA: 前綴和 + 奇偶狀態追蹤 (gemini)
class Solution(object):

  def maxValue(self, nums):
    """
        :type nums: List[int]
        :rtype: int
        """
    if not nums:
      return 0

    n = len(nums)

    # 1. 計算原始 Pulse Value 與交錯前綴和陣列 P
    initial_pulse = 0
    p = [0] * (n + 1)

    for i in range(n):
      a_i = nums[i] if i % 2 == 0 else -nums[i]
      initial_pulse += a_i
      p[i + 1] = p[i] + a_i

    # 2. 尋找偶數長度子陣列的最小區間和 (Min Even-Length Subarray Sum)
    # 區間和 = P[r] - P[l]，當 len 為偶數時，r 與 l 同奇或同偶
    max_p_even = p[0]  # 同為偶數索引時的最大 P[l]
    max_p_odd = float('-inf')  # 同為奇數索引時的最大 P[l]
    min_even_subarray_sum = float('inf')

    for r in range(1, n + 1):
      if r % 2 == 0:
        # r 為偶數，與 max_p_even 匹配
        min_even_subarray_sum = min(min_even_subarray_sum, p[r] - max_p_even)
        max_p_even = max(max_p_even, p[r])
      else:
        # r 為奇數，與 max_p_odd 匹配
        if max_p_odd != float('-inf'):
          min_even_subarray_sum = min(min_even_subarray_sum, p[r] - max_p_odd)
        max_p_odd = max(max_p_odd, p[r])

    # 3. 計算最大 Delta（如果不進行旋轉，Delta 為 0）
    max_delta = 0
    if min_even_subarray_sum != float('inf'):
      max_delta = max(0, -2 * min_even_subarray_sum)

    return initial_pulse + max_delta
