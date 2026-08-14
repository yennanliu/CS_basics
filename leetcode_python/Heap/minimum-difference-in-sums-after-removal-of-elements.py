"""

2163. Minimum Difference in Sums After Removal of Elements
Hard

You are given a 0-indexed integer array nums consisting of 3 * n elements.

You are allowed to remove any subsequence of elements of size exactly n from nums. The remaining 2 * n elements will be divided into two equal parts:

The first n elements belonging to the first part and their sum is sumfirst.
The next n elements belonging to the second part and their sum is sumsecond.

The difference in sums of the two parts is denoted as sumfirst - sumsecond.

For example, if sumfirst = 3 and sumsecond = 2, their difference is 1.
Similarly, if sumfirst = 2 and sumsecond = 3, their difference is -1.

Return the minimum difference possible between the sums of the two parts after the removal of n elements.


Example 1:

Input: nums = [3,1,2]
Output: -1
Explanation: Here, nums has 3 elements, so n = 1.
Thus we have to remove 1 element from nums and divide the array into two equal parts.
- If we remove nums[0] = 3, the array will be [1,2]. The difference in sums of the two parts will be 1 - 2 = -1.
- If we remove nums[1] = 1, the array will be [3,2]. The difference in sums of the two parts will be 3 - 2 = 1.
- If we remove nums[2] = 2, the array will be [3,1]. The difference in sums of the two parts will be 3 - 1 = 2.
The minimum difference between sums of the two parts is min(-1,1,2) = -1.

Example 2:

Input: nums = [7,9,5,8,1,3]
Output: 1
Explanation: Here n = 2. So we must remove 2 elements and divide the remaining array into two parts containing two elements each.
If we remove nums[2] = 5 and nums[3] = 8, the resultant array will be [7,9,1,3]. The difference in sums will be (7+9) - (1+3) = 12.
To obtain the minimum difference, we should remove nums[1] = 9 and nums[4] = 1. The resultant array becomes [7,5,8,3]. The difference in sums of the two parts is (7+5) - (8+3) = 1.
It can be shown that it is not possible to obtain a difference smaller than 1.


Constraints:

nums.length == 3 * n
1 <= n <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : SPLIT POINT + TWO HEAPS — MINIMISE THE LEFT SUM, MAXIMISE THE RIGHT
#
#   the kept elements keep their original order, so there is a cut index i :
#   the first part is chosen from nums[0..i-1] and the second from nums[i..].
#   i ranges over [n, 2n].
#
#   left[i]  = the SMALLEST sum of n elements taken from the prefix nums[:i]
#              -> sweep left to right holding the n smallest in a MAX-heap,
#                 evicting the largest whenever a smaller value arrives
#   right[i] = the LARGEST sum of n elements taken from the suffix nums[i:]
#              -> mirror sweep with a MIN-heap
#
#   the answer is min over i of  left[i] - right[i].
#
#   NOTE : python's heapq is a min-heap, so the max-heap stores negatives.
#
# time = O(n log n), space = O(n)
import heapq


class Solution(object):
    def minimumDifference(self, nums):
        total = len(nums)
        n = total // 3

        # left[i] : min sum of n elements among nums[:i], for i in [n, 2n]
        left = [0] * (total + 1)
        heap = []                      # max-heap (negated) of the n smallest
        cur = 0
        for i in range(2 * n):
            x = nums[i]
            heapq.heappush(heap, -x)
            cur += x
            if len(heap) > n:
                cur += heapq.heappop(heap)   # popping -max removes the largest
            if i + 1 >= n:
                left[i + 1] = cur

        # right[i] : max sum of n elements among nums[i:], for i in [n, 2n]
        right = [0] * (total + 1)
        heap = []                      # min-heap of the n largest
        cur = 0
        for i in range(total - 1, n - 1, -1):
            x = nums[i]
            heapq.heappush(heap, x)
            cur += x
            if len(heap) > n:
                cur -= heapq.heappop(heap)
            if total - i >= n:
                right[i] = cur

        return min(left[i] - right[i] for i in range(n, 2 * n + 1))
