"""

2386. Find the K-Sum of an Array
Hard

You are given an integer array nums and a positive integer k. You can choose any subsequence of the array and sum all of its elements together.

We define the K-Sum of the array as the kth largest subsequence sum that can be obtained (not necessarily distinct).

Return the K-Sum of the array.

Note that the empty subsequence is considered to have a sum of 0.


Example 1:

Input: nums = [2,4,-2], k = 5
Output: 2
Explanation: All the possible subsequence sums that we can obtain are the following sorted in decreasing order:
- 6, 4, 4, 2, 2, 0, 0, -2.
The 5th largest subsequence sum is 2.

Example 2:

Input: nums = [1,-2,3,4,-10,12], k = 16
Output: 10
Explanation: The 16th largest subsequence sum is 10.


Constraints:

n == nums.length
1 <= n <= 10^5
-10^9 <= nums[i] <= 10^9
1 <= k <= min(2000, 2^n)

"""

# V0
# IDEA : REDUCE TO "k SMALLEST SUBSET SUMS OF THE ABSOLUTE VALUES"
#
#   the largest possible sum takes every positive element :
#       total = sum(x for x in nums if x > 0)
#   every other subsequence sum is total minus some non-negative "loss" :
#   dropping a positive x costs x, and adding a negative x costs abs(x). so
#   the losses are exactly the subset sums of  a = sorted(abs(nums)).
#
#   therefore the k-th LARGEST subsequence sum is total minus the k-th
#   SMALLEST subset sum of a.
#
#   those are enumerated in order with a min-heap over (sum, next index),
#   expanding each popped state two ways :
#       include a[i]                  -> (s + a[i], i + 1)
#       swap a[i-1] out for a[i]      -> (s + a[i] - a[i-1], i + 1)
#   which reaches every subset exactly once, in non-decreasing sum order.
#
#   only k pops are needed, and k <= 2000.
#
# time = O(n log n + k log k), space = O(n + k)
import heapq


class Solution(object):
    def kSum(self, nums, k):
        total = sum(x for x in nums if x > 0)
        a = sorted(abs(x) for x in nums)

        heap = [(0, 0)]                 # (loss so far, next index to consider)
        res = total
        for _ in range(k):
            loss, i = heapq.heappop(heap)
            res = total - loss
            if i < len(a):
                heapq.heappush(heap, (loss + a[i], i + 1))
                if i > 0:
                    heapq.heappush(heap, (loss + a[i] - a[i - 1], i + 1))
        return res
