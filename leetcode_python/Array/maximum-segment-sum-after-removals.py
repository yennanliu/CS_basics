"""

2382. Maximum Segment Sum After Removals
Hard

You are given two 0-indexed integer arrays nums and removeQueries, both of length n. For the ith query, the element in nums at the index removeQueries[i] is removed, splitting nums into different segments.

A segment is a contiguous sequence of positive integers in nums. A segment sum is the sum of every element in a segment.

Return an integer array answer, of length n, where answer[i] is the maximum segment sum after applying the ith removal.

Note: The same index will not be removed more than once.


Example 1:

Input: nums = [1,2,5,6,1], removeQueries = [0,3,2,4,1]
Output: [14,7,2,2,0]
Explanation: Using 0 to indicate a removed element, the answer is as follows:
Query 1: Remove the 0th element, nums becomes [0,2,5,6,1] and the maximum segment sum is 14 for segment [2,5,6,1].
Query 2: Remove the 3rd element, nums becomes [0,2,5,0,1] and the maximum segment sum is 7 for segment [2,5].
Query 3: Remove the 2nd element, nums becomes [0,2,0,0,1] and the maximum segment sum is 2 for segment [2].
Query 4: Remove the 4th element, nums becomes [0,2,0,0,0] and the maximum segment sum is 2 for segment [2].
Query 5: Remove the 1st element, nums becomes [0,0,0,0,0] and the maximum segment sum is 0 for segment [].
Finally, we return [14,7,2,2,0].

Example 2:

Input: nums = [3,2,11,1], removeQueries = [3,2,1,0]
Output: [16,5,3,0]
Explanation: Using 0 to indicate a removed element, the answer is as follows:
Query 1: Remove the 3rd element, nums becomes [3,2,11,0] and the maximum segment sum is 16 for segment [3,2,11].
Query 2: Remove the 2nd element, nums becomes [3,2,0,0] and the maximum segment sum is 5 for segment [3,2].
Query 3: Remove the 1st element, nums becomes [3,0,0,0] and the maximum segment sum is 3 for segment [3].
Query 4: Remove the 0th element, nums becomes [0,0,0,0] and the maximum segment sum is 0 for segment [].
Finally, we return [16,5,3,0].


Constraints:

n == nums.length == removeQueries.length
1 <= n <= 10^5
1 <= nums[i] <= 10^9
0 <= removeQueries[i] < n
All the values of removeQueries are unique.

"""

# V0
# IDEA : RUN THE QUERIES BACKWARDS SO REMOVALS BECOME *INSERTIONS* (union-find)
#
#   splitting segments is hard to maintain incrementally; MERGING them is
#   easy. so reverse time : start from the fully-erased array and add the
#   removed indices back in reverse query order.
#
#   adding index i creates a segment of sum nums[i], then unions with the
#   left and right neighbours if they are already present. a running `best`
#   only ever grows, which is exactly what a union-find supports.
#
#   the answer for query t is the best BEFORE index removeQueries[t] was
#   added back, so record `best` first and then insert — that off-by-one is
#   the whole trick.
#
# time = O(n * alpha(n)), space = O(n)
class Solution(object):
    def maximumSegmentSum(self, nums, removeQueries):
        n = len(nums)
        parent = list(range(n))
        total = [0] * n
        present = [False] * n

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        res = [0] * n
        best = 0
        for t in range(n - 1, -1, -1):
            res[t] = best                  # state BEFORE this index returns
            i = removeQueries[t]
            present[i] = True
            total[i] = nums[i]
            for j in (i - 1, i + 1):
                if 0 <= j < n and present[j]:
                    ri, rj = find(i), find(j)
                    if ri != rj:
                        parent[rj] = ri
                        total[ri] += total[rj]
            best = max(best, total[find(i)])
        return res


# V0-1
# IDEA : REVERSE TIME AGAIN, BUT MERGE VIA SEGMENT *BOUNDARIES* (no DSU)
#
#   when index i comes back, the segment on its left must END exactly at i - 1
#   and the one on its right must START exactly at i + 1 -- precisely because
#   i itself was missing. so keeping, at each endpoint, the opposite endpoint
#   plus the segment sum is enough to merge in true O(1): no find(), no path
#   compression, no alpha(n) factor, just list indexing.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maximumSegmentSum(self, nums, removeQueries):
        n = len(nums)
        present = [False] * n
        seg_l = [0] * n            # meaningful at a segment's RIGHT endpoint
        seg_r = [0] * n            # meaningful at a segment's LEFT endpoint
        seg_sum = [0] * n          # meaningful at a segment's LEFT endpoint
        res = [0] * n
        best = 0
        for t in range(n - 1, -1, -1):
            res[t] = best                  # state BEFORE this index returns
            i = removeQueries[t]
            l, r, s = i, i, nums[i]
            if i > 0 and present[i - 1]:
                l = seg_l[i - 1]
                s += seg_sum[l]
            if i + 1 < n and present[i + 1]:
                r = seg_r[i + 1]
                s += seg_sum[i + 1]
            present[i] = True
            seg_l[r] = l
            seg_r[l] = r
            seg_sum[l] = s
            if s > best:
                best = s
        return res


# V0-2
# IDEA : FORWARD IN TIME -- SPLIT SEGMENTS, MAX-HEAP WITH LAZY DELETION
#
#   the honest forward simulation. removing i splits the segment [L, R] that
#   contains it into [L, i-1] and [i+1, R]; a bisect over the sorted list of
#   already-removed indices supplies L and R, and prefix sums give the two new
#   sums in O(1).
#
#   a heap cannot erase the parent segment, so keep a set of live intervals and
#   discard dead tops lazily when reading the maximum. every interval is pushed
#   once and popped at most once.
#
# time = O(n log n) heap work, plus O(n) shifting per insert (O(n^2) worst case)
# space = O(n)
import bisect
import heapq


class Solution(object):
    def maximumSegmentSum(self, nums, removeQueries):
        n = len(nums)
        pref = [0] * (n + 1)
        for i, v in enumerate(nums):
            pref[i + 1] = pref[i] + v

        removed = [-1, n]                  # sentinels
        alive = set([(0, n - 1)])
        heap = [(-pref[n], 0, n - 1)]
        res = [0] * n
        for t, i in enumerate(removeQueries):
            p = bisect.bisect_left(removed, i)
            lo, hi = removed[p - 1] + 1, removed[p] - 1
            alive.discard((lo, hi))
            for a, b in ((lo, i - 1), (i + 1, hi)):
                if a <= b:
                    alive.add((a, b))
                    heapq.heappush(heap, (-(pref[b + 1] - pref[a]), a, b))
            removed.insert(p, i)
            while heap and (heap[0][1], heap[0][2]) not in alive:
                heapq.heappop(heap)
            res[t] = -heap[0][0] if heap else 0
        return res
