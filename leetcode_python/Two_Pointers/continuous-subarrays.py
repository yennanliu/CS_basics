"""

2762. Continuous Subarrays
Medium

You are given a 0-indexed integer array nums. A subarray of nums is called continuous if:

Let i, i + 1, ..., j be the indices in the subarray. Then, for each pair of indices i <= i1, i2 <= j, 0 <= |nums[i1] - nums[i2]| <= 2.

Return the total number of continuous subarrays.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [5,4,2,4]
Output: 8
Explanation:
Continuous subarray of size 1: [5], [4], [2], [4].
Continuous subarray of size 2: [5,4], [4,2], [2,4].
Continuous subarray of size 3: [4,2,4].
There are no subarrys of size 4.
Total continuous subarrays = 4 + 3 + 1 = 8.
It can be shown that there are no more continuous subarrays.

Example 2:

Input: nums = [1,2,3]
Output: 6
Explanation:
Continuous subarray of size 1: [1], [2], [3].
Continuous subarray of size 2: [1,2], [2,3].
Continuous subarray of size 3: [1,2,3].
Total continuous subarrays = 3 + 2 + 1 = 6.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SLIDING WINDOW + TWO MONOTONIC DEQUES (window max & window min)
#
#  "|nums[i1] - nums[i2]| <= 2 for every pair" is just
#       max(window) - min(window) <= 2
#
#  and that property is MONOTONE: shrinking a valid window keeps it valid.
#  So for every right end r there is a smallest left end l(r), and l(r) never
#  decreases as r grows -> classic sliding window, and every window ending at
#  r contributes r - l + 1 subarrays.
#
#  to read max/min of the window in O(1) we keep two deques of INDICES:
#     maxq : values decreasing -> maxq[0] is the window maximum
#     minq : values increasing -> minq[0] is the window minimum
#
#  while the spread exceeds 2 we must drop whichever extreme is FURTHEST LEFT
#  (it is the one blocking us), and move l just past it.
#
#   NOTE : store indices, not values -- we need to know WHERE the extreme is
#          to decide how far l jumps.
#   NOTE : the answer can reach ~n^2/2 = 5*10^9, i.e. beyond 32 bits; Python
#          ints are unbounded so nothing to do here, but the Java/C++ versions
#          of this problem require a long long.
#   NOTE : an ordered multiset (SortedList / TreeMap) also works but costs an
#          extra log n; the deques give a clean O(n).
#
# time = O(n), space = O(n)
from collections import deque
class Solution(object):
    def continuousSubarrays(self, nums):
        maxq = deque()      # indices, nums values decreasing
        minq = deque()      # indices, nums values increasing
        res = 0
        l = 0
        for r, x in enumerate(nums):
            while maxq and nums[maxq[-1]] <= x:
                maxq.pop()
            maxq.append(r)
            while minq and nums[minq[-1]] >= x:
                minq.pop()
            minq.append(r)

            while nums[maxq[0]] - nums[minq[0]] > 2:
                if maxq[0] < minq[0]:
                    l = maxq[0] + 1
                    maxq.popleft()
                else:
                    l = minq[0] + 1
                    minq.popleft()

            res += r - l + 1
        return res
