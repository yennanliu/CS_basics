"""

2519. Count the Number of K-Big Indices
Hard

You are given a 0-indexed integer array nums and a positive integer k.

We call an index i k-big if the following conditions are satisfied:

There exist at least k different indices idx1 such that idx1 < i and nums[idx1] < nums[i].
There exist at least k different indices idx2 such that idx2 > i and nums[idx2] < nums[i].

Return the number of k-big indices.


Example 1:

Input: nums = [2,3,6,5,2,3], k = 2
Output: 2
Explanation: There are only two 2-big indices in nums:
- i = 2 --> There are two valid idx1: 0 and 1. There are three valid idx2: 2, 3, and 4.
- i = 3 --> There are two valid idx1: 0 and 1. There are two valid idx2: 3 and 4.

Example 2:

Input: nums = [1,1,1], k = 3
Output: 0
Explanation: There are no 3-big indices in nums.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], k <= nums.length

"""

import heapq


# V0
# IDEA : TWO SIZE-K MAX HEAPS (keep only the k smallest values on each side)
#
#   "at least k earlier elements are smaller than nums[i]" is equivalent to
#   "the k-th smallest value among nums[0..i-1] is < nums[i]". so we do not
#   need the whole prefix — just its k smallest values.
#
#   maintain a MAX-heap capped at size k holding the k smallest values seen so
#   far. python's heapq is a min-heap, so push negated values; then -heap[0] is
#   exactly the k-th smallest, and the test is simply  -heap[0] < nums[i]
#   (with size == k required, otherwise fewer than k candidates exist at all).
#
#   do this once left-to-right and once right-to-left, then an index is k-big
#   iff both passes flagged it.
#
#   NOTE : the strict `<` matters — equal values do NOT count, and capping the
#          heap AFTER the query (push current value only once it has been
#          tested) keeps nums[i] itself out of its own side count.
#
# time = O(n * log k), space = O(n)
class Solution(object):
    def kBigIndices(self, nums, k):
        n = len(nums)

        def scan(seq):
            # flag[t] = True if at least k of the already-seen values are < seq[t]
            flag = [False] * len(seq)
            heap = []  # max-heap (negated) of the k smallest values seen so far
            for t, v in enumerate(seq):
                if len(heap) == k and -heap[0] < v:
                    flag[t] = True
                heapq.heappush(heap, -v)
                if len(heap) > k:
                    heapq.heappop(heap)
            return flag

        left = scan(nums)
        right = scan(nums[::-1])
        right.reverse()

        return sum(1 for i in range(n) if left[i] and right[i])
