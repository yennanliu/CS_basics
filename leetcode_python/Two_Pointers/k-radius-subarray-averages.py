"""

2090. K Radius Subarray Averages
Medium

You are given a 0-indexed array nums of n integers, and an integer k.

The k-radius average for a subarray of nums centered at some index i with the radius k is the average of all elements in nums between the indices i - k and i + k (inclusive). If there are less than k elements before or after the index i, then the k-radius average is -1.

Build and return an array avgs of length n where avgs[i] is the k-radius average for the subarray centered at index i.

The average of x elements is the sum of the x elements divided by x, using integer division. The integer division truncates toward zero, which means losing its fractional part.

For example, the average of four elements 2, 3, 1, and 5 is (2 + 3 + 1 + 5) / 4 = 11 / 4 = 2.75, which truncates to 2.


Example 1:

Input: nums = [7,4,3,9,1,8,5,2,6], k = 3
Output: [-1,-1,-1,5,4,4,-1,-1,-1]
Explanation:
- avg[0], avg[1], and avg[2] are -1 because there are less than k elements before each index.
- The sum of the subarray centered at index 3 with radius 3 is: 7 + 4 + 3 + 9 + 1 + 8 + 5 = 37.
  Using integer division, avg[3] = 37 / 7 = 5.
- For the subarray centered at index 4, avg[4] = (4 + 3 + 9 + 1 + 8 + 5 + 2) / 7 = 4.
- For the subarray centered at index 5, avg[5] = (3 + 9 + 1 + 8 + 5 + 2 + 6) / 7 = 4.
- avg[6], avg[7], and avg[8] are -1 because there are less than k elements after each index.

Example 2:

Input: nums = [100000], k = 0
Output: [100000]
Explanation:
- The sum of the subarray centered at index 0 with radius 0 is: 100000.
  avg[0] = 100000 / 1 = 100000.

Example 3:

Input: nums = [8], k = 100000
Output: [-1]
Explanation:
- avg[0] is -1 because there are less than k elements before and after index 0.


Constraints:

n == nums.length
1 <= n <= 10^5
0 <= nums[i], k <= 10^5

"""


# V0

# V0-1
# IDEA : PREFIX SUM (fixed-width window of size 2k+1) (gpt)
class Solution(object):
    def getAverages(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if not nums:
            return []

        n = len(nums)
        res = [-1] * n

        # prefix[i] = sum(nums[0:i])
        # prefix[i] does NOT include nums[i].
        prefix = [0] * (n + 1)

        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]

        window_size = 2 * k + 1

        for i in range(n):
            left = i - k
            right = i + k

            # Not enough elements on either side.
            if left < 0 or right >= n:
                continue

            # Sum nums[left:right + 1] using prefix sum.
            window_sum = prefix[right + 1] - prefix[left]

            # Integer floor average.
            res[i] = window_sum // window_size

        return res


# V0-2
# IDEA : PREFIX SUM (fixed-width window of size 2k+1) (GEMINI)
class Solution(object):

  def getAverages(self, nums, k):
    """:type nums: List[int]

    :type k: int
    :rtype: List[int]
    """
    n = len(nums)
    res = [-1] * n

    # 1. 邊界特例處理
    if k == 0:
      return nums

    window_size = 2 * k + 1
    # 如果視窗大小超過整個陣列長度，直接回傳全為 -1 的陣列
    if window_size > n:
      return res

    # 2. 建立標準前綴和陣列 (prefix[i] 代表前 i 個元素的總和)
    prefix = [0] * (n + 1)
    for i in range(n):
      prefix[i + 1] = prefix[i] + nums[i]

    # 3. 遍歷所有合法的中心點 i (範圍從 k 到 n - k - 1)
    for i in range(k, n - k):
      left = i - k
      right = i + k

      # 利用前綴和 O(1) 取得 nums[left ... right] 的區間總和
      sub_sum = prefix[right + 1] - prefix[left]

      # 計算平均值 (向下取整)
      res[i] = sub_sum // window_size

    return res


# V0-3
# IDEA : PREFIX SUM (fixed-width window of size 2k+1)
#
#   index i has a full window iff  k <= i <= n-1-k. for those, the window sum
#   is  prefix[i+k+1] - prefix[i-k]  and the average is that sum // (2k+1).
#   every other index is -1.
#
#   NOTE : the window width can exceed n (k up to 10^5 with n == 1), in which
#          case the valid range is simply empty and the answer stays all -1.
#
# time = O(n), space = O(n)
class Solution(object):
    def getAverages(self, nums, k):
        n = len(nums)
        res = [-1] * n
        width = 2 * k + 1
        if width > n:
            return res

        prefix = [0] * (n + 1)
        for i, x in enumerate(nums):
            prefix[i + 1] = prefix[i] + x

        for i in range(k, n - k):
            res[i] = (prefix[i + k + 1] - prefix[i - k]) // width
        return res
