"""

480. Sliding Window Median
Hard

The median is the middle value in an ordered integer list. If the size of the
list is even, there is no middle value. So the median is the mean of the two
middle values.

- For examples, if arr = [2,3,4], the median is 3.
- For examples, if arr = [1,2,3,4], the median is (2 + 3) / 2 = 2.5.

You are given an integer array nums and an integer k. There is a sliding window
of size k which is moving from the very left of the array to the very right. You
can only see the k numbers in the window. Each time the sliding window moves
right by one position.

Return the median array for each window in the original array. Answers within
10^-5 of the actual value will be accepted.

Example 1:

Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [1.00000,-1.00000,-1.00000,3.00000,5.00000,6.00000]
Explanation:
Window position                Median
---------------                -----
[1  3  -1] -3  5  3  6  7        1
 1 [3  -1  -3] 5  3  6  7       -1
 1  3 [-1  -3  5] 3  6  7       -1
 1  3  -1 [-3  5  3] 6  7        3
 1  3  -1  -3 [5  3  6] 7        5
 1  3  -1  -3  5 [3  6  7]       6

Example 2:

Input: nums = [1,2,3,4,2,3,1,4,2], k = 3
Output: [2.00000,3.00000,3.00000,3.00000,2.00000,3.00000,2.00000]

Constraints:

1 <= k <= nums.length <= 10^5
-2^31 <= nums[i] <= 2^31 - 1

"""

# V0
# IDEA : SORTED WINDOW + BINARY SEARCH (bisect)
#
#  Keep the window itself in sorted order. Sliding one step is:
#     - delete the outgoing value (bisect_left to find it, then pop)
#     - insert the incoming value (insort)
#  Then the median is just the middle of the sorted list.
#
#  The list ops are O(k) memmove, but that memmove is C-level, which makes this
#  the simplest correct solution and fast enough in practice.
#
# time = O(n * k)
# space = O(k)
import bisect
class Solution(object):
    def medianSlidingWindow(self, nums, k):
        window = sorted(nums[:k])

        def median():
            if k % 2 == 1:
                return float(window[k // 2])
            return (window[k // 2 - 1] + window[k // 2]) / 2.0

        res = [median()]
        for i in range(k, len(nums)):
            # remove the element leaving the window
            window.pop(bisect.bisect_left(window, nums[i - k]))
            # add the element entering the window
            bisect.insort(window, nums[i])
            res.append(median())
        return res


# V1
# IDEA : TWO HEAPS + LAZY DELETION
#
#  `small` : max-heap (values negated) holding the lower half
#  `large` : min-heap holding the upper half
#  invariant: len(small) == len(large)  or  len(small) == len(large) + 1
#  -> median is small's top (odd k) or the average of the two tops (even k)
#
#  A heap cannot delete an arbitrary element, so we DEFER it: mark the outgoing
#  value in `delayed` and only physically pop it once it surfaces to a top.
#  `size` tracks the number of VALID (non-deleted) elements per heap so the
#  balancing stays correct even while garbage sits inside the heaps.
#
# time = O(n * log n)
# space = O(n)
import heapq
from collections import defaultdict
class Solution2(object):
    def medianSlidingWindow(self, nums, k):
        small = []                  # max-heap (negated) - lower half
        large = []                  # min-heap            - upper half
        delayed = defaultdict(int)  # value -> pending deletions
        size = [0, 0]               # valid sizes of [small, large]

        def prune(heap, sign):
            # sign = -1 for `small` (stored negated), +1 for `large`
            while heap:
                val = sign * heap[0]
                if delayed[val] > 0:
                    delayed[val] -= 1
                    heapq.heappop(heap)
                else:
                    break

        def balance():
            if size[0] > size[1] + 1:
                heapq.heappush(large, -heapq.heappop(small))
                size[0] -= 1
                size[1] += 1
                prune(small, -1)        # the new top may be garbage
            elif size[0] < size[1]:
                heapq.heappush(small, -heapq.heappop(large))
                size[0] += 1
                size[1] -= 1
                prune(large, 1)

        def insert(num):
            if not small or num <= -small[0]:
                heapq.heappush(small, -num)
                size[0] += 1
            else:
                heapq.heappush(large, num)
                size[1] += 1
            balance()

        def erase(num):
            delayed[num] += 1
            if num <= -small[0]:
                size[0] -= 1
                if num == -small[0]:
                    prune(small, -1)
            else:
                size[1] -= 1
                if num == large[0]:
                    prune(large, 1)
            balance()

        def median():
            if k % 2 == 1:
                return float(-small[0])
            return (-small[0] + large[0]) / 2.0

        for i in range(k):
            insert(nums[i])

        res = [median()]
        for i in range(k, len(nums)):
            insert(nums[i])
            erase(nums[i - k])
            res.append(median())
        return res
