"""

719. Find K-th Smallest Pair Distance
Hard

The distance of a pair of integers a and b is defined as the absolute difference
between a and b.

Given an integer array nums and an integer k, return the kth smallest distance
among all the pairs nums[i] and nums[j] where 0 <= i < j < nums.length.


Example 1:

Input: nums = [1,3,1], k = 1
Output: 0
Explanation: Here are all the pairs:
(1,3) -> 2
(1,1) -> 0
(3,1) -> 2
Then the 1st smallest distance pair is (1,1), and its distance is 0.

Example 2:

Input: nums = [1,1,1], k = 2
Output: 0

Example 3:

Input: nums = [1,6,1], k = 3
Output: 5


Constraints:

n == nums.length
2 <= n <= 10^4
0 <= nums[i] <= 10^6
1 <= k <= n * (n - 1) / 2

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER + SLIDING WINDOW COUNT
#
#   We never enumerate the O(n^2) pairs. Instead we binary search the distance d
#   over [0, max - min] and ask "how many pairs have distance <= d ?".
#   That count is monotonically increasing in d, so the smallest d whose count
#   reaches k IS the k-th smallest distance.
#
#   Counting is a two pointer sweep on the sorted array: for each `right`,
#   move `left` forward until nums[right] - nums[left] <= d; every index in
#   [left, right) then forms a valid pair with `right`.
#
# time = O(n log n + n log W), W = max(nums) - min(nums)
# space = O(1) (ignoring the sort)
class Solution(object):
    def smallestDistancePair(self, nums, k):
        nums.sort()
        n = len(nums)

        def count_pairs(dist):
            """number of pairs (i < j) with nums[j] - nums[i] <= dist"""
            cnt = 0
            left = 0
            for right in range(n):
                while nums[right] - nums[left] > dist:
                    left += 1
                cnt += right - left
            return cnt

        lo, hi = 0, nums[-1] - nums[0]
        while lo < hi:
            mid = (lo + hi) // 2
            if count_pairs(mid) >= k:
                hi = mid
            else:
                lo = mid + 1

        return lo
