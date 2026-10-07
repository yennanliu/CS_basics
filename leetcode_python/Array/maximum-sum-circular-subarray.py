"""

918. Maximum Sum Circular Subarray
Medium

Given a circular integer array nums of length n, return the maximum possible sum of a non-empty subarray of nums.

A circular array means the end of the array connects to the beginning of the array. Formally, the next element of nums[i] is nums[(i + 1) % n] and the previous element of nums[i] is nums[(i - 1 + n) % n].

A subarray may only include each element of the fixed buffer nums at most once. Formally, for a subarray nums[i], nums[i + 1], ..., nums[j], there does not exist i <= k1, k2 <= j with k1 % n == k2 % n.

Example 1:

Input: nums = [1,-2,3,-2]
Output: 3
Explanation: Subarray [3] has maximum sum 3.

Example 2:

Input: nums = [5,-3,5]
Output: 10
Explanation: Subarray [5,5] has maximum sum 5 + 5 = 10.

Example 3:

Input: nums = [-3,-2,-3]
Output: -2
Explanation: Subarray [-2] has maximum sum -2.

Constraints:

n == nums.length
1 <= n <= 3 * 10^4
-3 * 10^4 <= nums[i] <= 3 * 10^4

"""

# V0
class Solution(object):
    def maxSubarraySumCircular(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: Kadane (DP) + max_sum, cur_max, min_sum, cur_min (gpt)
"""

1. CORE IDEA:


->
    ```
    Circular Maximum
    =
    max(
        normal maximum subarray,
        total sum - minimum subarray
    )
    ```


2.  `2 cases`



    ->

    Case 1: 不跨頭尾
    [ - - - MAX - - - ]

    => 普通 Kadane
    => max_sum



    Case 2: 跨頭尾

    [ MAX ... MAX ]
           ↑
        中間不要

    => total_sum - 中間最小的 subarray
    => total_sum - min_sum   (NOTE this !!!)



    -> so,

        ```
        answer = max(max_sum, total_sum - min_sum)
        ```


3. 
    need to maintain 4 var (for Kadane):

         max_sum
         cur_max
         min_sum
         cur_min

"""
class Solution(object):
    def maxSubarraySumCircular(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """

        # Edge case
        if not nums:
            return 0

        # Total sum of the array
        total_sum = sum(nums)

        # Kadane for maximum subarray
        max_sum = nums[0]
        cur_max = nums[0]

        # Kadane for minimum subarray
        min_sum = nums[0]
        cur_min = nums[0]


        # NOTE !!!
        # i starts from 1
        for i in range(1, len(nums)):
            val = nums[i]

            # Maximum subarray ending at current position
            cur_max = max(val, cur_max + val)
            max_sum = max(max_sum, cur_max)

            # Minimum subarray ending at current position
            cur_min = min(val, cur_min + val)
            min_sum = min(min_sum, cur_min)


        """
        NOTE !!! edge case below (All negative)


        ->

        nums = [-3, -2, -5]


        total_sum - min_sum = -10  - (-10) = 0

        -> but 0 means choose NOTHING
            -> which is NOT correct

        -> so,

            ```
            if max_sum < 0:
                return max_sum
            ```

        """
        # If all numbers are negative,
        # total_sum - min_sum would become 0,
        # which means choosing an empty subarray.
        # That is not allowed.
        if max_sum < 0:
            return max_sum

        # Two cases:
        #
        # 1. Normal subarray
        #    -> max_sum
        #
        # 2. Circular subarray
        #    -> total_sum - minimum subarray
        #
        # Example:
        # [5, -3, 5]
        # total = 7  ( sum([5, -3, 5]) = 7 )
        # min subarray = [-3]
        # circular max = 7 - (-3) = 10
        return max(max_sum, total_sum - min_sum)


# V0-2
# IDEA:  Kadane 演算法 (最大與最小子陣列和) (gemini)
"""
CORE IDEA:

    
    情況 1：未跨越邊界 (Standard Kadane)
    [  [ max subarray ]  ]
    Ans = max_sum

    情況 2：跨越環形邊界 (Total Sum - Min Subarray)
    [ max_1 ] [ min subarray ] [ max_2 ]
    Ans = total_sum - min_sum


"""
class Solution(object):

  def maxSubarraySumCircular(self, nums):
    """:type nums: List[int]

    :rtype: int
    """
    total_sum = 0

    # 維護未環繞的最大子陣列和 (Max Kadane)
    cur_max = 0
    max_sum = float('-inf')

    # 維護中間被剔除的最小子陣列和 (Min Kadane)
    cur_min = 0
    min_sum = float('inf')

    for x in nums:
      total_sum += x

      # 1. 標準 Kadane：求最大連續子陣列和
      cur_max = max(x, cur_max + x)
      max_sum = max(max_sum, cur_max)

      # 2. 反向 Kadane：求最小連續子陣列和
      cur_min = min(x, cur_min + x)
      min_sum = min(min_sum, cur_min)

    # 特例：若全為負數，max_sum < 0，此時 total_sum - min_sum 會得到 0 (非法空陣列)
    if max_sum < 0:
      return max_sum

    # 答案取「未環繞最大和」與「環繞最大和 (全域和 - 最小和)」的較大值
    return max(max_sum, total_sum - min_sum)


# V0-3
# IDEA: 2 * ARRAY + BRUTE FORCE (TLE)
class Solution(object):
    def maxSubarraySumCircular(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # Edge case
        if not nums:
            return 0

        if len(nums) == 1:
            return nums[0]

        # Duplicate the array so we can handle circular subarrays.
        #
        # Example:
        # nums   = [5, -3, 5]
        # nums_2 = [5, -3, 5, 5, -3, 5]
        nums_2 = nums + nums

        n = len(nums)
        max_sub_arr = nums[0]

        # Try every starting position in the original array.
        for i in range(n):
            tmp = 0

            # The subarray length cannot exceed n.
            for j in range(n):
                tmp += nums_2[i + j]

                max_sub_arr = max(max_sub_arr, tmp)

        return max_sub_arr


# V1-1
# IDEA: Enumerate prefix and suffix sums
# https://leetcode.com/problems/maximum-sum-circular-subarray/editorial/
# class Solution {
#     public int maxSubarraySumCircular(int[] nums) {
#         final int n = nums.length;
#         final int[] rightMax = new int[n];
#         rightMax[n - 1] = nums[n - 1];
#         int suffixSum = nums[n - 1];
#
#         for (int i = n - 2; i >= 0; --i) {
#             suffixSum += nums[i];
#             rightMax[i] = Math.max(rightMax[i + 1], suffixSum);
#         }
#
#         int maxSum = nums[0];
#         int specialSum = nums[0];
#         int curMax = 0;
#         for (int i = 0, prefixSum = 0; i < n; ++i) {
#             // This is Kadane's algorithm.
#             curMax = Math.max(curMax, 0) + nums[i];
#             maxSum = Math.max(maxSum, curMax);
#
#             prefixSum += nums[i];
#             if (i + 1 < n) {
#                 specialSum = Math.max(specialSum, prefixSum + rightMax[i + 1]);
#             }
#         }
#
#         return Math.max(maxSum, specialSum);  
#     }
# }



# V1-2
# IDEA: Calculate the "Minimum Subarray"
# https://leetcode.com/problems/maximum-sum-circular-subarray/editorial/
# class Solution {
#     public int maxSubarraySumCircular(int[] nums) {
#         int curMax = 0;
#         int curMin = 0;
#         int maxSum = nums[0];
#         int minSum = nums[0];
#         int totalSum = 0;
#        
#         for (int num: nums) {
#             // Normal Kadane's
#             curMax = Math.max(curMax, 0) + num;
#             maxSum = Math.max(maxSum, curMax);
#            
#             // Kadane's but with min to find minimum subarray
#             curMin = Math.min(curMin, 0) + num;
#             minSum = Math.min(minSum, curMin);
#            
#             totalSum += num;  
#         }
#
#         if (totalSum == minSum) {
#             return maxSum;
#         }
#        
#         return Math.max(maxSum, totalSum - minSum);
#     }
# }



# V2 
# https://buptwc.com/2018/10/08/Leetcode-918-Maximum-Sum-Circular-Subarray/
# time = O(n)
# space = O(n)
class Solution(object):
    def maxSubarraySumCircular(self, A):
        left = [A[0]] * len(A)
        s = [A[0]] * len(A)
        r_max = [s[0]] * len(A)
        for i in range(1,len(A)):
            left[i] = max(A[i], left[i-1]+A[i])
            s[i] = s[i-1] + A[i]
            r_max[i] = max(s[i], r_max[i-1])

        res = max(left)
        for i in range(len(A)):
            res = max(res, s[-1] - s[i] + r_max[i])
        return res

# V3
# https://www.jiuzhang.com/solution/maximum-sum-circular-subarray/#tag-highlight-lang-python
# time = O(n)
# space = O(n)
class Solution(object):
    def maxSubarraySumCircular(self, A):
        N = len(A)

        ans = cur = None
        for x in A:
            cur = x + max(cur, 0)
            ans = max(ans, cur)

        # ans is the answer for 1-interval subarrays.
        # Now, let's consider all 2-interval subarrays.
        # For each i, we want to know
        # the maximum of sum(A[j:]) with j >= i+2

        # rightsums[i] = sum(A[i:])
        rightsums = [None] * N
        rightsums[-1] = A[-1]
        for i in range(N-2, -1, -1):
            rightsums[i] = rightsums[i+1] + A[i]

        # maxright[i] = max_{j >= i} rightsums[j]
        maxright = [None] * N
        maxright[-1] = rightsums[-1]
        for i in range(N-2, -1, -1):
            maxright[i] = max(maxright[i+1], rightsums[i])

        leftsum = 0
        for i in range(N-2):
            leftsum += A[i]
            ans = max(ans, leftsum + maxright[i+2])
        return ans
        
# V4
# time = O(n)
# space = O(1)
class Solution(object):
    def maxSubarraySumCircular(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        total, max_sum, cur_max, min_sum, cur_min = 0, -float("inf"), 0, float("inf"), 0
        for a in A:
            cur_max = max(cur_max+a, a)
            max_sum = max(max_sum, cur_max)
            cur_min = min(cur_min+a, a)
            min_sum = min(min_sum, cur_min)
            total += a
        return max(max_sum, total-min_sum) if max_sum > 0 else max_sum
