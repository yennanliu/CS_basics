"""

1465. Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
Medium

You are given a rectangular cake of size h x w and two arrays of integers horizontalCuts and verticalCuts where:

horizontalCuts[i] is the distance from the top of the rectangular cake to the ith horizontal cut and similarly, and
verticalCuts[j] is the distance from the left of the rectangular cake to the jth vertical cut.

Return the maximum area of a piece of cake after you cut at each horizontal and vertical position provided in the arrays horizontalCuts and verticalCuts. Since the answer can be a large number, return this modulo 10^9 + 7.


Example 1:

Input: h = 5, w = 4, horizontalCuts = [1,2,4], verticalCuts = [1,3]
Output: 4
Explanation: The figure above represents the given rectangular cake. Red lines are the horizontal and vertical cuts. After you cut the cake, the green piece of cake has the maximum area.

Example 2:

Input: h = 5, w = 4, horizontalCuts = [3,1], verticalCuts = [1]
Output: 6
Explanation: The figure above represents the given rectangular cake. Red lines are the horizontal and vertical cuts. After you cut the cake, the green and yellow pieces of cake have the maximum area.

Example 3:

Input: h = 5, w = 4, horizontalCuts = [3], verticalCuts = [3]
Output: 9


Constraints:

2 <= h, w <= 10^9
1 <= horizontalCuts.length <= min(h - 1, 10^5)
1 <= verticalCuts.length <= min(w - 1, 10^5)
1 <= horizontalCuts[i] < h
1 <= verticalCuts[i] < w
All the elements in horizontalCuts are distinct.
All the elements in verticalCuts are distinct.

"""

# V0
# IDEA : SORT + LARGEST GAP (height and width are independent)
#
#   the grid of cuts makes every piece a (gap in y) x (gap in x) rectangle,
#   and the two dimensions can be maximised separately, so the answer is
#   (largest horizontal gap) * (largest vertical gap).
#   NOTE : add the borders 0 and h (resp. 0 and w) before scanning, else the
#          first and last slices are missed.
#   NOTE : take the modulo only at the very end - maximising a value mod p
#          on the way would compare the wrong numbers.
#
# time = O(m log m + n log n), space = O(m + n)
class Solution(object):
    def maxArea(self, h, w, horizontalCuts, verticalCuts):
        MOD = 10 ** 9 + 7

        def widest(cuts, size):
            pts = sorted(cuts + [0, size])
            best = 0
            for i in range(1, len(pts)):
                best = max(best, pts[i] - pts[i - 1])
            return best

        return (widest(horizontalCuts, h) * widest(verticalCuts, w)) % MOD


# V0-1
# IDEA : LINEAR MAX GAP BY BUCKETING (THE LC 164 "MAXIMUM GAP" TRICK)
#
#   the widest gap between the m + 2 boundary points of one axis is at least
#   the average gap, so if the range [0, size] is split into buckets of width
#   floor(size / (#points - 1)) the two ends of the widest gap can never fall
#   in the same bucket. keeping only the min and max of each bucket is then
#   enough : scan the non-empty buckets in order and measure
#   bucket_min - previous_bucket_max.
#   nothing is sorted, so each axis costs O(m) instead of O(m log m).
#
# time = O(m + n), space = O(m + n)
class Solution(object):
    def maxArea(self, h, w, horizontalCuts, verticalCuts):
        MOD = 10 ** 9 + 7

        def widest(cuts, size):
            pts = cuts + [0, size]
            width = max(1, size // (len(pts) - 1))
            nb = size // width + 1
            bmin = [-1] * nb
            bmax = [-1] * nb
            for p in pts:
                b = p // width
                if bmin[b] < 0 or p < bmin[b]:
                    bmin[b] = p
                if p > bmax[b]:
                    bmax[b] = p
            best = 0
            prev = -1
            for b in range(nb):
                if bmin[b] < 0:
                    continue
                if prev >= 0 and bmin[b] - prev > best:
                    best = bmin[b] - prev
                prev = bmax[b]
            return best

        return (widest(horizontalCuts, h) * widest(verticalCuts, w)) % MOD


# V0-2
# IDEA : HEAP - STREAM THE BOUNDARY POINTS OUT IN INCREASING ORDER
#
#   the largest gap only ever compares neighbours in sorted order, and a
#   binary heap can hand those out one at a time : heapify the boundaries in
#   O(m), then pop and diff against the previously popped point.
#   the sorted array of V0 is never materialised, which is the shape you want
#   when the cuts arrive as a stream or when only the first few gaps matter.
#
# time = O(m log m + n log n), space = O(m + n)
class Solution(object):
    def maxArea(self, h, w, horizontalCuts, verticalCuts):
        import heapq

        MOD = 10 ** 9 + 7

        def widest(cuts, size):
            pq = cuts + [0, size]
            heapq.heapify(pq)
            best = 0
            prev = heapq.heappop(pq)
            while pq:
                p = heapq.heappop(pq)
                if p - prev > best:
                    best = p - prev
                prev = p
            return best

        return (widest(horizontalCuts, h) * widest(verticalCuts, w)) % MOD
