"""

2613. Beautiful Pairs
Hard

You are given two 0-indexed integer arrays nums1 and nums2 of the same length. A pair of indices (i,j) is called beautiful if |nums1[i] - nums1[j]| + |nums2[i] - nums2[j]| is the smallest amongst all possible indices pairs where i < j.

Return the beautiful pair. In the case that there are multiple beautiful pairs, return the lexicographically smallest pair.

Note that

|x| denotes the absolute value of x.
A pair of indices (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or i1 == i2 and j1 < j2.


Example 1:

Input: nums1 = [1,2,3,2,4], nums2 = [2,3,1,2,3]
Output: [0,3]
Explanation: Consider index 0 and index 3. The value of |nums1[i]-nums1[j]| + |nums2[i]-nums2[j]| is 1, which is the smallest value we can achieve.

Example 2:

Input: nums1 = [1,2,4,3,2,5], nums2 = [1,4,2,3,5,1]
Output: [1,4]
Explanation: Consider index 1 and index 4. The value of |nums1[i]-nums1[j]| + |nums2[i]-nums2[j]| is 1, which is the smallest value we can achieve.


Constraints:

2 <= nums1.length, nums2.length <= 10^5
nums1.length == nums2.length
0 <= nums1[i] <= nums1.length
0 <= nums2[i] <= nums2.length

"""

# V0
# IDEA : CLOSEST PAIR OF POINTS (DIVIDE & CONQUER) under the MANHATTAN metric
#
#   read (nums1[i], nums2[i]) as a 2D point; the score of a pair is its L1
#   distance, so we want the CLOSEST PAIR — the classic divide & conquer,
#   which works for L1 just as well as for L2.
#
#   1) DUPLICATES FIRST.  two identical points give distance 0, which no other
#      pair can beat. scanning left to right, the FIRST index whose point
#      repeats is the "i" of the lexicographically smallest such pair, and its
#      partner is the second index in that point's list -> return immediately.
#      NOTE : this also guarantees all remaining points are DISTINCT, hence
#             every recursive distance is >= 1 (never 0), which is what keeps
#             the strip step from degenerating.
#
#   2) sort points by x, split at the middle, solve both halves.
#      let d be the better of the two. any cross pair beating d must have both
#      endpoints inside the vertical STRIP |x - x_mid| <= d; sort the strip by
#      y and, for each point, only compare with the following ones while the
#      y-gap stays <= d — a constant number of them by a packing argument.
#
#   NOTE : the candidate is carried as the TUPLE (dist, i, j) with i < j, and
#          plain tuple comparison then gives exactly the required tie-break
#          (smallest distance, then lexicographically smallest pair).
#
#   NOTE : the strip bounds use "<=" and not "<" on purpose. with "<" we would
#          prune away pairs that TIE with the current best, and a tying pair
#          may still be lexicographically smaller.
#
# time = O(n * log(n)^2), space = O(n)
class Solution(object):
    def beautifulPair(self, nums1, nums2):
        n = len(nums1)

        # -- 1) identical points -> distance 0, answer is immediate
        seen = {}
        for i in range(n):
            seen.setdefault((nums1[i], nums2[i]), []).append(i)
        for i in range(n):
            same = seen[(nums1[i], nums2[i])]
            if len(same) > 1:
                return [same[0], same[1]]

        pts = sorted((nums1[i], nums2[i], i) for i in range(n))
        INF = float('inf')

        # -- 2) divide & conquer on pts[lo:hi] (already sorted by x)
        def solve(lo, hi):
            if hi - lo < 2:
                return (INF, -1, -1)
            mid = (lo + hi) // 2
            x_mid = pts[mid][0]
            best = min(solve(lo, mid), solve(mid, hi))
            d = best[0]

            strip = [p for p in pts[lo:hi] if abs(p[0] - x_mid) <= d]
            strip.sort(key=lambda p: p[1])
            for a in range(len(strip)):
                xa, ya, ia = strip[a]
                for b in range(a + 1, len(strip)):
                    xb, yb, ib = strip[b]
                    if yb - ya > d:
                        break
                    cand = (abs(xa - xb) + abs(ya - yb), min(ia, ib), max(ia, ib))
                    if cand < best:
                        best = cand
                        d = best[0]
            return best

        _, i, j = solve(0, n)
        return [i, j]
