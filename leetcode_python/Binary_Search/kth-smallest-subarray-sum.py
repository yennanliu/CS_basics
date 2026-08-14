"""

1918. Kth Smallest Subarray Sum
Medium

Given an integer array nums of length n and an integer k, return the kth smallest subarray sum.

A subarray is defined as a non-empty contiguous sequence of elements in an array. A subarray sum is the sum of all elements in the subarray.


Example 1:

Input: nums = [2,1,3], k = 4
Output: 3
Explanation: The subarrays of [2,1,3] are:
- [2] with sum 2
- [1] with sum 1
- [3] with sum 3
- [2,1] with sum 3
- [1,3] with sum 4
- [2,1,3] with sum 6
Ordering the sums from smallest to largest gives 1, 2, 3, 3, 4, 6. The 4th smallest is 3.

Example 2:

Input: nums = [3,3,5,5], k = 7
Output: 10
Explanation: The subarrays of [3,3,5,5] are:
- [3] with sum 3
- [3] with sum 3
- [5] with sum 5
- [5] with sum 5
- [3,3] with sum 6
- [3,5] with sum 8
- [5,5] with sum 10
- [3,3,5], with sum 11
- [3,5,5] with sum 13
- [3,3,5,5] with sum 16
Ordering the sums from smallest to largest gives 3, 3, 5, 5, 6, 8, 10, 11, 13, 16. The 7th smallest is 10.


Constraints:

n == nums.length
1 <= n <= 2 * 10^4
1 <= nums[i] <= 5 * 10^4
1 <= k <= n * (n + 1) / 2

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER + SLIDING WINDOW COUNT
#
#   nums[i] >= 1, so subarray sums grow when the window grows -> the count of
#   subarrays with sum <= s is MONOTONE non-decreasing in s.
#
#   binary search s over [min(nums), sum(nums)] and keep the smallest s with
#   count(s) >= k. that s is the k-th smallest sum (it must itself be an
#   achievable sum, otherwise count(s) == count(s-1) and s-1 would have won).
#
#   count(s) : two pointers. for each right end i, shrink the left end j while
#              the window sum > s; then every window ending at i and starting
#              at j..i is valid -> add (i - j + 1).
#
# time = O(n * log(S)), S = sum(nums)
# space = O(1)
class Solution(object):
    def kthSmallestSubarraySum(self, nums, k):
        def count(s):
            # number of subarrays whose sum is <= s
            total = 0
            cur = 0
            j = 0
            for i in range(len(nums)):
                cur += nums[i]
                while cur > s:
                    cur -= nums[j]
                    j += 1
                total += i - j + 1
            return total

        lo, hi = min(nums), sum(nums)
        while lo < hi:
            mid = (lo + hi) // 2
            if count(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
