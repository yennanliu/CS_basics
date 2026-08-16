"""

3510. Minimum Pair Removal to Sort Array II
Hard

Given an array nums, you can perform the following operation any number of
times:

Select the adjacent pair with the minimum sum in nums. If multiple such pairs
exist, choose the leftmost one.

Replace the pair with their sum.

Return the minimum number of operations needed to make the array non-decreasing.

An array is said to be non-decreasing if each element is greater than or equal
to its previous element (if it exists).

Example 1:

Input: nums = [5,2,3,1]

Output: 2

Explanation:

The pair (3,1) has the minimum sum of 4. After replacement, nums = [5,2,4].

The pair (2,4) has the minimum sum of 6. After replacement, nums = [5,6].

The array nums became non-decreasing in two operations.

Example 2:

Input: nums = [1,2,2]

Output: 0

Explanation:

The array nums is already sorted.

Constraints:

1 <= nums.length <= 10^5

-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : DOUBLY LINKED LIST + LAZY MIN-HEAP + A COUNTER OF DESCENTS
#
#   the process is fully forced, so there is nothing to optimise -- the only
#   problem is running the forced process fast.  each operation deletes one
#   element, so there are at most n - 1 of them; what must be cheap is
#   (a) finding the minimum-sum adjacent pair and (b) knowing when to stop.
#
#   (a) keep the surviving elements in a doubly linked list over the original
#   indices, and push every live pair into a min-heap as (sum, left index).
#   ordering by the left index breaks ties, and since the survivors stay in
#   increasing index order that is exactly the "leftmost" rule.  a pair is
#   invalidated by bumping a version counter on its left endpoint, so stale
#   heap entries are simply skipped when popped -- no deletion needed.
#
#   (b) maintain `bad`, the number of adjacent pairs with a[i] > a[next].  the
#   array is sorted precisely when bad == 0, and a merge only touches the three
#   pairs around the merged cell, so the counter is O(1) to keep current.  this
#   avoids rescanning the array after every operation.
#
# time = O(n * log n), space = O(n)
import heapq


class Solution(object):
    def minimumPairRemoval(self, nums):
        n = len(nums)
        if n < 2:
            return 0
        val = list(nums)
        left = list(range(-1, n - 1))
        right = list(range(1, n + 1))
        right[n - 1] = -1
        alive = [True] * n
        ver = [0] * n

        bad = sum(1 for i in range(n - 1) if val[i] > val[i + 1])
        heap = [(val[i] + val[i + 1], i, 0) for i in range(n - 1)]
        heapq.heapify(heap)

        ops = 0
        while bad:
            while True:
                s, i, v = heapq.heappop(heap)
                if alive[i] and ver[i] == v and right[i] != -1:
                    break
            j = right[i]
            p, q = left[i], right[j]
            if val[i] > val[j]:
                bad -= 1
            if p != -1 and val[p] > val[i]:
                bad -= 1
            if q != -1 and val[j] > val[q]:
                bad -= 1

            val[i] += val[j]
            alive[j] = False
            right[i] = q
            if q != -1:
                left[q] = i
            ver[i] += 1
            ops += 1

            if p != -1:
                if val[p] > val[i]:
                    bad += 1
                ver[p] += 1
                heapq.heappush(heap, (val[p] + val[i], p, ver[p]))
            if q != -1:
                if val[i] > val[q]:
                    bad += 1
                heapq.heappush(heap, (val[i] + val[q], i, ver[i]))
        return ops
