"""

2271. Maximum White Tiles Covered by a Carpet
Medium

You are given a 2D integer array tiles where tiles[i] = [li, ri] represents that every tile j in the range li <= j <= ri is colored white.

You are also given an integer carpetLen, the length of a single carpet that can be placed anywhere.

Return the maximum number of white tiles that can be covered by the carpet.


Example 1:

Input: tiles = [[1,5],[10,11],[12,18],[20,25],[30,32]], carpetLen = 10
Output: 9
Explanation: Place the carpet starting on tile 10.
It covers 9 white tiles, so we return 9.
Note that there may be other places where the carpet covers 9 white tiles.
It can be shown that the carpet cannot cover more than 9 white tiles.

Example 2:

Input: tiles = [[10,11],[1,1]], carpetLen = 2
Output: 2
Explanation: Place the carpet starting on tile 10.
It covers 2 white tiles, so we return 2.


Constraints:

1 <= tiles.length <= 5 * 10^4
tiles[i].length == 2
1 <= li <= ri <= 10^9
1 <= carpetLen <= 10^9
The tiles are non-overlapping.

"""

# V0
# IDEA : SORT + SLIDING WINDOW (an optimal carpet can start on some tile's left end)
#
#   exchange argument : sliding a carpet right never gains until its left edge
#   hits the start of an interval, so it is enough to try starts = tiles[i][0].
#
#   sort by left end, keep a window [i, j) of intervals FULLY inside
#   [li, li + carpetLen - 1] with running sum `covered`. the first interval
#   that sticks out (tiles[j]) may still be partially covered, contributing
#   li + carpetLen - tiles[j][0] cells when that is positive.
#
#   NOTE : both i and j only move forward, so the whole scan is O(n) after the
#          sort.
#
# time = O(n log n), space = O(1) beyond the sort
class Solution(object):
    def maximumWhiteTiles(self, tiles, carpetLen):
        tiles.sort()
        n = len(tiles)
        res = 0
        covered = 0
        j = 0
        for i in range(n):
            li, ri = tiles[i]
            # extend while the whole interval j fits inside the carpet
            while j < n and tiles[j][1] - li + 1 <= carpetLen:
                covered += tiles[j][1] - tiles[j][0] + 1
                j += 1
            cur = covered
            if j < n and li + carpetLen > tiles[j][0]:
                cur += li + carpetLen - tiles[j][0]
            if cur > res:
                res = cur
            covered -= ri - li + 1
        return res


# V0-1
# IDEA : SORT + PREFIX SUM + BINARY SEARCH FOR THE LAST TOUCHED INTERVAL
#
#   same exchange argument (only starts at tiles[i][0] matter), but instead of
#   maintaining the window incrementally, precompute pre[] = prefix sums of
#   interval lengths and BINARY SEARCH the last interval whose LEFT end still
#   falls inside the carpet [li, li + carpetLen - 1].
#
#   every interval from i up to that index j is (at least partly) touched, so
#   the covered amount is pre[j+1] - pre[i] minus the part of interval j that
#   hangs over the carpet's right edge.
#
#   this trades the two-pointer bookkeeping for a log factor per start, and it
#   is easier to reason about because each start is computed independently.
#
# time = O(n log n), space = O(n) for the prefix sums
from bisect import bisect_right
class Solution(object):
    def maximumWhiteTiles(self, tiles, carpetLen):
        tiles.sort()
        n = len(tiles)
        lefts = [t[0] for t in tiles]
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + tiles[i][1] - tiles[i][0] + 1
        res = 0
        for i in range(n):
            end = tiles[i][0] + carpetLen - 1
            # last interval that starts at or before the carpet's right edge
            j = bisect_right(lefts, end) - 1
            covered = pre[j + 1] - pre[i]
            if tiles[j][1] > end:
                covered -= tiles[j][1] - end
            if covered > res:
                res = covered
        return res


# V0-2
# IDEA : BRUTE FORCE — TRY EVERY CANDIDATE START, RESCAN ALL INTERVALS
#
#   the plain version of the same exchange argument: for each candidate start
#   li = tiles[i][0], walk the whole list and add the overlap of every interval
#   with [li, li + carpetLen - 1], i.e. max(0, min(ri, end) - max(li_k, start) + 1).
#
#   needs no sorting and no prefix sums at all, which makes it a useful
#   correctness reference for the O(n log n) versions above.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def maximumWhiteTiles(self, tiles, carpetLen):
        res = 0
        for start, _ in tiles:
            end = start + carpetLen - 1
            cur = 0
            for a, b in tiles:
                lo = a if a > start else start
                hi = b if b < end else end
                if hi >= lo:
                    cur += hi - lo + 1
            if cur > res:
                res = cur
        return res
