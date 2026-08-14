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
