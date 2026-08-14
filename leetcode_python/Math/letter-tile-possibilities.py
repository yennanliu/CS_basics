"""

1079. Letter Tile Possibilities
Medium

You have n tiles, where each tile has one letter tiles[i] printed on it.

Return the number of possible non-empty sequences of letters you can make using
the letters printed on those tiles.


Example 1:

Input: tiles = "AAB"
Output: 8
Explanation: The possible sequences are "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA".

Example 2:

Input: tiles = "AAABBC"
Output: 188

Example 3:

Input: tiles = "V"
Output: 1


Constraints:

1 <= tiles.length <= 7
tiles consists of uppercase English letters.

"""

# V0
# IDEA : BACKTRACK on a letter COUNTER (dedupe by letter, not by position)
#
#  at every step we pick a letter that still has stock -> that itself is one
#  new sequence, then recurse to extend it. because we loop over DISTINCT
#  letters (counter keys) instead of tile positions, identical tiles are
#  never counted twice.
# time = O(n!) worst case (n <= 7)
# space = O(n)
from collections import Counter
class Solution(object):
    def numTilePossibilities(self, tiles):
        cnt = Counter(tiles)

        def dfs():
            res = 0
            for ch in cnt:
                if cnt[ch] == 0:
                    continue
                res += 1            # the sequence ending right here
                cnt[ch] -= 1
                res += dfs()        # and everything that extends it
                cnt[ch] += 1
            return res

        return dfs()
