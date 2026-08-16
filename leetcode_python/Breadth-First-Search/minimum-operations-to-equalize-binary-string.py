"""

3666. Minimum Operations to Equalize Binary String
Hard

You are given a binary string s and an integer k.

In one operation, you must choose exactly k different indices and flip each of the corresponding bits ('0' becomes '1' and '1' becomes '0').

Return the minimum number of operations required to make all characters in s equal to '1'. If it is not possible, return -1.


Example 1:

Input: s = "110", k = 1
Output: 1
Explanation:
Flip the single index 2, turning s into "111".

Example 2:

Input: s = "0101", k = 3
Output: 2
Explanation:
Flip indices 0, 1 and 2 to get "1011", then flip indices 1, 2 and 3 to get "1111".
Two operations are needed and one is not enough.


Constraints:

1 <= s.length <= 10^5
1 <= k <= s.length
s consists only of '0' and '1'.

"""

# V0
# IDEA : COLLAPSE THE STATE TO "HOW MANY ZEROS" AND BFS OVER INTERVALS
#
#   an operation may pick ANY k indices, so which particular bits are zero
#   never matters — only how many. the state is the zero count z, and one
#   operation that flips a zeros and k-a ones sends
#       z -> z + k - 2a,     max(0, k - (n - z)) <= a <= min(k, z)
#   so from a single z the successors form an arithmetic interval with step 2.
#
#   bfs over z is therefore bfs over intervals, and the only performance risk
#   is re-scanning already-visited values. a union-find "next unvisited value
#   of this parity" pointer makes every value leave the search exactly once,
#   which keeps the whole bfs near-linear even though the intervals overlap
#   heavily.
#
# time = O(n * alpha(n)), space = O(n)
class Solution(object):
    def minOperations(self, s, k):
        n = len(s)
        z = s.count('0')
        if z == 0:
            return 0
        if k > n:
            return -1

        size = n + 3
        nxt = list(range(size + 2))

        def find(x):
            root = x
            while nxt[root] != root:
                root = nxt[root]
            while nxt[x] != root:
                nxt[x], x = root, nxt[x]
            return root

        def take(v):
            nxt[v] = v + 2 if v + 2 < len(nxt) else v

        take(z)
        frontier = [z]
        steps = 0
        while frontier:
            steps += 1
            nf = []
            for cz in frontier:
                lo_a = k - (n - cz)
                if lo_a < 0:
                    lo_a = 0
                hi_a = k if k < cz else cz
                if lo_a > hi_a:
                    continue
                lo_z = cz + k - 2 * hi_a
                hi_z = cz + k - 2 * lo_a
                v = find(lo_z)
                while v <= hi_z:
                    if v == 0:
                        return steps
                    take(v)
                    nf.append(v)
                    v = find(v + 2)
            frontier = nf
        return -1
