"""

2616. Minimize the Maximum Difference of Pairs
Medium

You are given a 0-indexed integer array nums and an integer p. Find p pairs of
indices of nums such that the maximum difference amongst all the pairs is
minimized. Also, ensure no index appears more than once amongst the p pairs.

Note that for a pair of elements at the index i and j, the difference of this
pair is |nums[i] - nums[j]|, where |x| represents the absolute value of x.

Return the minimum maximum difference among all p pairs. We define the maximum
of an empty set to be zero.

Example 1:

Input: nums = [10,1,2,7,1,3], p = 2

Output: 1

Explanation:

The first pair is formed from the indices 1 and 4, and the second pair is
formed from the indices 2 and 5.
The maximum difference is max(|nums[1] - nums[4]|, |nums[2] - nums[5]|) =
max(0, 1) = 1. Therefore, we return 1.

Example 2:

Input: nums = [4,2,1,2], p = 1

Output: 0

Explanation:

Let the indices 1 and 3 form a pair. The difference of that pair is
|2 - 2| = 0, which is the minimum we can attain.

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= p <= (nums.length)/2

"""


"""
NOTE !!!


-> CAN'T just use `counter` (hash map), and find the pairs
    -> since we need to get `p pairs` on the same time


        -> 你原本使用 Counter、排序和雙層迴圈的方向可以理解
          ，但這題不能只找出最小的一組差值，而是要同時選出 p 組 pair。
"""

# V0
class Solution(object):
    def minimizeMax(self, nums, p):
        """
        :type nums: List[int]
        :type p: int
        :rtype: int
        """
        pass

# V0-1
# IDEA: SORT + Binary Search + Greedy (GEMINI)
"""
STEPS:

    1. 先排序 nums。

    2. 二分搜尋答案 max_diff。

    3. 每次用 Greedy 判斷：能不能選出`至少` p 組差值不超過 max_diff 的 pair？

"""
class Solution(object):

  def minimizeMax(self, nums, p):
    """:type nums: List[int]

    :type p: int
    :rtype: int
    """
    if p == 0:
      return 0

    nums.sort()
    n = len(nums)

    # 1. 二分搜的數值範圍：最小值 0，最大值 (最大值 - 最小值)
    l, r = 0, nums[-1] - nums[0]
    ans = r

    while l <= r:
      mid = l + (r - l) // 2

      # 2. 檢查在當前容許差值 mid 下，能否湊出至少 p 個不重疊數對
      if self.can_form_pairs(nums, mid, p):
        ans = mid  # 成功，嘗試尋找更小的最大差值
        r = mid - 1
      else:
        l = mid + 1  # 差值限制太嚴格，無法湊出 p 對，放寬限制

    return ans

  def can_form_pairs(self, nums, max_diff, p):
    count = 0
    i = 0
    while i < len(nums) - 1:
      # 如果相鄰元素差值小於等於限制，則成對
      if nums[i + 1] - nums[i] <= max_diff:
        count += 1
        i += 2  # 跳過這兩個元素，確保 index 不重複使用
      else:
        i += 1  # 否則往後檢查下一個

      if count >= p:
        return True
    return False



# V0-1
# IDEA: Binary Search + Greedy (GPT)
class Solution(object):
    def minimizeMax(self, nums, p):
        """
        :type nums: List[int]
        :type p: int
        :rtype: int
        """
        # Edge case: need zero pairs
        if p == 0:
            return 0

        nums.sort()
        n = len(nums)

        # Check if we can form at least p pairs
        # where each pair's difference <= max_diff
        def can_form_pairs(max_diff):
            cnt = 0
            i = 0

            while i < n - 1:
                if nums[i + 1] - nums[i] <= max_diff:
                    cnt += 1
                    i += 2  # Both indices are used
                else:
                    i += 1

                if cnt >= p:
                    return True

            return False

        # Binary search for the minimum feasible max difference
        left = 0
        right = nums[-1] - nums[0]

        while left < right:
            mid = left + (right - left) // 2

            if can_form_pairs(mid):
                right = mid
            else:
                left = mid + 1

        return left


# V0-3
# IDEA : SORT + BINARY SEARCH ON THE ANSWER (MINIMIZE THE MAXIMUM) + GREEDY CHECK (CLAUDE)
#
#   Binary search the answer `max_diff` over [0, max(nums) - min(nums)].
#   The check "can we form p disjoint pairs, each with diff <= max_diff?" is
#   MONOTONIC: if it works for d, it works for every d' > d. So find the
#   smallest d that works.
#
#   Why sort: in sorted order, the best partner for any element is a NEIGHBOUR
#   (pairing i with j > i + 1 is never better than pairing i with i + 1).
#
#   Why the greedy check is optimal: scan left to right; if nums[i + 1] - nums[i]
#   <= max_diff, take the pair and skip both. Taking it never hurts — any
#   solution that instead uses i + 1 with i + 2 can swap that pair for (i, i + 1)
#   without losing a pair (exchange argument).
#
#   e.g. nums = [10,1,2,7,1,3], p = 2 -> sorted [1,1,2,3,7,10]
#        max_diff = 0 -> (1,1) only            -> 1 pair  < 2 -> too small
#        max_diff = 1 -> (1,1), (2,3)          -> 2 pairs >= 2 -> answer 1
#
#   (DP over (index, pairs used) is also correct, but O(n * p) times out here.)
#
# time = O(n log n + n log(max - min)), space = O(1) extra (O(n) for the sorted copy)
class Solution(object):
    def minimizeMax(self, nums, p):
        """
        :type nums: List[int]
        :type p: int
        :rtype: int
        """
        # edge case: the maximum of an empty set of pairs is 0
        if p == 0:
            return 0

        # sort a copy so the closest values sit next to each other
        sorted_nums = sorted(nums)

        # the answer is a difference: at least 0, at most the full spread
        left = 0
        right = sorted_nums[-1] - sorted_nums[0]
        answer = right

        while left <= right:
            mid = (left + right) // 2
            if self.can_form_pairs(sorted_nums, p, mid):
                # mid works -> record it, then try a smaller maximum
                answer = mid
                right = mid - 1
            else:
                # mid is too tight -> allow a larger difference
                left = mid + 1

        return answer

    def can_form_pairs(self, sorted_nums, p, max_diff):
        """Return True if p disjoint adjacent pairs with diff <= max_diff exist."""
        pair_count = 0
        i = 0
        while i < len(sorted_nums) - 1:
            if sorted_nums[i + 1] - sorted_nums[i] <= max_diff:
                pair_count += 1
                # NOTE !!! skip BOTH indices: each index may be used only once
                i += 2
            else:
                # sorted_nums[i] cannot pair with its neighbour -> leave it unpaired
                i += 1

            # early exit: enough pairs already
            if pair_count >= p:
                return True

        return False
