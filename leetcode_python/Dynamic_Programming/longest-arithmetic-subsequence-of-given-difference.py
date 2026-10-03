"""

1218. Longest Arithmetic Subsequence of Given Difference
Medium

Given an integer array arr and an integer difference, return the length of the longest
subsequence in arr which is an arithmetic sequence such that the difference between
adjacent elements in the subsequence equals difference.

A subsequence is a sequence that can be derived from arr by deleting some or no elements
without changing the order of the remaining elements.

Example 1:

Input: arr = [1,2,3,4], difference = 1
Output: 4
Explanation: The longest arithmetic subsequence is [1,2,3,4].

Example 2:

Input: arr = [1,3,5,7], difference = 1
Output: 1
Explanation: The longest arithmetic subsequence is any single element.

Example 3:

Input: arr = [1,5,7,8,5,3,4,2,1], difference = -2
Output: 4
Explanation: The longest arithmetic subsequence is [7,5,3,1].


Constraints:

1 <= arr.length <= 10^5
-10^4 <= arr[i], difference <= 10^4

"""


# V0
class Solution(object):
    def longestSubsequence(self, arr, difference):
        """
        :type arr: List[int]
        :type difference: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASH MAP DP (gpt)
# time: O(N)
# space: O(N)
class Solution(object):
    def longestSubsequence(self, arr, difference):
        """
        :type arr: List[int]
        :type difference: int
        :rtype: int
        """

        # dp[value] =
        # longest arithmetic subsequence ending with `value`
        #
        # Example:
        # difference = 2
        #
        # 1 -> 3 -> 5
        #
        # dp[1] = 1
        # dp[3] = 2
        # dp[5] = 3
        dp = {}

        max_len = 0

        for val in arr:

            # To make an arithmetic subsequence ending at val,
            # the previous value must be:
            #
            # val - difference
            #
            prev = val - difference

            # If prev exists:
            # extend the previous subsequence.
            #
            # Otherwise:
            # start a new subsequence with val.
            dp[val] = dp.get(prev, 0) + 1

            max_len = max(max_len, dp[val])

        return max_len


# V0-2
# IDEA: HASH MAP DP (gemini)
# time: O(N)
# space: O(N)
"""
CORE IDEA:


-> 正確的突破口：以「數值」為鍵值的雜湊表 DP


    -> 我們不需要開一個陣列去記每個索引，而是用一個雜湊表（Dictionary）來記錄：

        - dp[x]：以數字 x 結尾的最長等差子序列長度。

        - 於每一個數字 x，它想要接續的「前一個數字」固定是 x - difference。

        - 因此，狀態轉移方程可以直接寫成：

            ```
            dp[x] = dp[x - diff] + 1
            ```

"""
class Solution(object):

  def longestSubsequence(self, arr, difference):
    """:type arr: List[int] :type difference: int :rtype: int"""
    # 雜湊表記錄：{ 結尾數值 : 以該數值結尾的最長等差子序列長度 }
    dp = {}
    max_len = 0

    for x in arr:
      # 1. 尋找這個等差數列在當前數字之前的「前一個數字」是什麼
      prev_val = x - difference

      # 2. 如果前一個數字存在於 dp 中，長度就 +1；否則代表這是新數列的開端，長度從 1 開始
      dp[x] = dp.get(prev_val, 0) + 1

      # 3. 即時更新全域最大長度
      max_len = max(max_len, dp[x])

    return max_len


# V0-3
# IDEA: HASH MAP DP (claude)
"""
 DP def:
    - dp[x] = length of the longest valid subsequence ENDING with value x

 DP eq:
    - dp[x] = dp[x - difference] + 1

 (we scan left -> right, so dp[x - difference] already refers to an
  earlier index, which keeps the subsequence order valid)
"""
# time = O(n)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def longestSubsequence(self, arr, difference):
        dp = defaultdict(int)
        res = 0
        for x in arr:
            dp[x] = dp[x - difference] + 1
            res = max(res, dp[x])
        return res
