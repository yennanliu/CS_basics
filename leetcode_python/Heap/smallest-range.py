"""

632. Smallest Range Covering Elements from K Lists
Hard

You have k lists of sorted integers in non-decreasing order.
Find the smallest range that includes at least one number from each of the k lists.

We define the range [a, b] is smaller than range [c, d] if b - a < d - c
or a < c if b - a == d - c.

Example 1:

Input: nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
Output: [20,24]
Explanation:
List 1: [4, 10, 15, 24, 26], 24 is in range [20,24].
List 2: [0, 9, 12, 20], 20 is in range [20,24].
List 3: [5, 18, 22, 30], 22 is in range [20,24].

Example 2:

Input: nums = [[1,2,3],[1,2,3],[1,2,3]]
Output: [1,1]

Constraints:

nums.length == k
1 <= k <= 3500
1 <= nums[i].length <= 50
-10^5 <= nums[i][j] <= 10^5
nums[i] is sorted in non-decreasing order.

"""

# V0
# IDEA : MIN HEAP (k-way merge, one pointer per list)
#
#   Keep exactly one candidate per list in a min heap, plus the running max of
#   those k candidates. The heap top is the min, so [min, max] is always a valid
#   covering range. To shrink it, the only useful move is to advance the list
#   holding the minimum (advancing anything else can only raise the max).
#   Stop when a list is exhausted -- past that point no range can cover it.
#
#   Tie-break note: candidates are visited in increasing `min` order, so using a
#   strict `<` comparison naturally keeps the smallest `a` among equal widths.
#
# time = O(n * log(k)), n = total number of elements
# space = O(k)
import heapq
class Solution(object):
    def smallestRange(self, nums):
        # (value, which list, index inside that list)
        pq = [(row[0], i, 0) for i, row in enumerate(nums)]
        heapq.heapify(pq)

        cur_max = max(row[0] for row in nums)
        best = [pq[0][0], cur_max]

        while True:
            val, i, j = heapq.heappop(pq)
            if cur_max - val < best[1] - best[0]:
                best = [val, cur_max]

            # this list has no more elements -> no further range can cover it
            if j + 1 == len(nums[i]):
                break

            nxt = nums[i][j + 1]
            cur_max = max(cur_max, nxt)
            heapq.heappush(pq, (nxt, i, j + 1))

        return best


# V1
# IDEA : SORT ALL (value, list_id) PAIRS + SLIDING WINDOW
#        distinct trick: turns "cover all k lists" into the classic
#        "smallest window containing all k kinds" problem
# time = O(n * log(n))
# space = O(n)
from collections import defaultdict
class Solution(object):
    def smallestRange(self, nums):
        pairs = [(x, i) for i, row in enumerate(nums) for x in row]
        pairs.sort()

        k = len(nums)
        cnt = defaultdict(int)
        covered = 0
        left = 0
        best = [-10 ** 6, 10 ** 6]

        for right, (val, i) in enumerate(pairs):
            if cnt[i] == 0:
                covered += 1
            cnt[i] += 1

            # shrink while the window still covers every list
            while covered == k:
                lo = pairs[left][0]
                if val - lo < best[1] - best[0]:
                    best = [lo, val]
                cnt[pairs[left][1]] -= 1
                if cnt[pairs[left][1]] == 0:
                    covered -= 1
                left += 1

        return best
