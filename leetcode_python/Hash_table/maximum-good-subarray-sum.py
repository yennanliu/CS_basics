"""

3026. Maximum Good Subarray Sum
Medium

You are given an array nums of length n and a positive integer k.

A subarray of nums is called good if the absolute difference between its first and last element is exactly k, in other words, the subarray nums[i..j] is good if |nums[i] - nums[j]| == k.

Return the maximum sum of a good subarray of nums. If there are no good subarrays, return 0.


Example 1:

Input: nums = [1,2,3,4,5,6], k = 1
Output: 11
Explanation: The absolute difference between the first and last element must be 1 for a good subarray. All the good subarrays are: [1,2], [2,3], [3,4], [4,5], and [5,6]. The maximum subarray sum is 11 for the subarray [5,6].

Example 2:

Input: nums = [-1,3,2,4,5], k = 3
Output: 11
Explanation: The absolute difference between the first and last element must be 3 for a good subarray. All the good subarrays are: [-1,3,2], and [2,4,5]. The maximum subarray sum is 11 for the subarray [2,4,5].

Example 3:

Input: nums = [-1,-2,-3,-4], k = 2
Output: -6
Explanation: The absolute difference between the first and last element must be 2 for a good subarray. All the good subarrays are: [-1,-2,-3], and [-2,-3,-4]. The maximum subarray sum is -6 for the subarray [-1,-2,-3].


Constraints:

2 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9
1 <= k <= 10^9

"""

# V0
class Solution(object):
    def maximumSubarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        pass
        


# V0-1
# IDEA: 前綴和 + 雜湊表（Prefix Sum + Hash Map） (gemini)
class Solution(object):

  def maximumSubarraySum(self, nums, k):
    """:type nums: List[int] :type k: int :rtype: int"""
    # 初始最大和設為負無限大，以防子陣列和全都是負數的情況被 0 擋掉
    ans = -float("inf")
    prefix = 0

    # 記錄每個數值出現「之前」所對應的最小前綴和：{ value: min_prefix_sum }
    num_to_min_prefix = {}

    for num in nums:
      # 1. 在把當前 num 併入前綴和之前，先檢查並更新該數值對應的最小前綴和
      # 為了讓後續相減 (prefix_end - prefix_start) 最大化，prefix_start 越小越好
      if num not in num_to_min_prefix or num_to_min_prefix[num] > prefix:
        num_to_min_prefix[num] = prefix

      # 2. 將當前 num 累加進前綴和中（此時 prefix 代表包含當前結尾的總前綴和）
      prefix += num

      # 3. 檢查是否存在能與當前 num 形成好子陣列的歷史數值 (差值絕對值為 k)
      # 也就是尋找歷史數值為 (num + k) 或 (num - k) 的起點
      if (num + k) in num_to_min_prefix:
        ans = max(ans, prefix - num_to_min_prefix[num + k])

      if (num - k) in num_to_min_prefix:
        ans = max(ans, prefix - num_to_min_prefix[num - k])

    # 如果 ans 仍為負無限大，代表完全沒有找到符合條件的好子陣列，依題意回傳 0
    return ans if ans != -float("inf") else 0


# V0-2
# IDEA: 前綴和 + 雜湊表（Prefix Sum + Hash Map） (GPT)
class Solution(object):

    def maximumSubarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """

        if not nums:
            return 0

        # Prefix sum
        prefix_sum = 0

        # {value: minimum prefix sum BEFORE this value}
        val_min_prefix = {}

        # IMPORTANT:
        # Answer can be negative.
        max_sub_sum = float("-inf")

        for val in nums:

            # Current prefix sum includes nums[i]
            prefix_sum += val

            # We need:
            #
            # abs(val - target) == k
            #
            # => target = val - k
            #             or
            #             val + k
            for target in (val - k, val + k):

                if target in val_min_prefix:

                    # Sum of subarray:
                    #
                    # current prefix
                    # -
                    # prefix before target
                    #
                    tmp_sum = prefix_sum - val_min_prefix[target]

                    max_sub_sum = max(
                        max_sub_sum,
                        tmp_sum
                    )

            # Prefix sum BEFORE current val
            prev_prefix_sum = prefix_sum - val

            # Keep the minimum prefix sum for this value.
            #
            # Smaller prefix sum
            # -> larger subarray sum
            if val not in val_min_prefix:
                val_min_prefix[val] = prev_prefix_sum
            else:
                val_min_prefix[val] = min(
                    val_min_prefix[val],
                    prev_prefix_sum
                )

        # No valid subarray found
        if max_sub_sum == float("-inf"):
            return 0

        return max_sub_sum


# V0-3
# IDEA : PREFIX SUMS + "CHEAPEST START PER VALUE" (claude)
#
#   the sum of nums[i..j] is pre[j+1] - pre[i], so for a fixed right end j
#   the best subarray is the one whose start has the SMALLEST pre[i] — among
#   the starts that make the subarray good.
#
#   "good" pins the start's VALUE : nums[i] must be nums[j] - k or
#   nums[j] + k. so keep a dict
#
#       best[v] = min prefix sum at any index i seen so far with nums[i] == v
#
#   and at each j look up the (at most two) qualifying values.
#
#   NOTE : the answer can be negative, so start from -inf and only fall back
#          to 0 when no good subarray exists at all.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumSubarraySum(self, nums, k):
        best = {}                     # value -> min prefix sum before it
        res = float('-inf')
        pre = 0
        for x in nums:
            # x closes a subarray that started at a value of x-k or x+k
            for v in (x - k, x + k):
                if v in best:
                    res = max(res, pre + x - best[v])
            if x not in best or pre < best[x]:
                best[x] = pre
            pre += x
        return 0 if res == float('-inf') else res
