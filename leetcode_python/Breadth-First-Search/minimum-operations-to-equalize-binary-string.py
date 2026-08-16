"""

3666. Minimum Operations to Equalize Binary String
Hard

You are given a binary string s, and an integer k.

In one operation, you must choose exactly k different indices and flip each
'0' to '1' and each '1' to '0'.

Return the minimum number of operations required to make all characters in
the string equal to '1'. If it is not possible, return -1.

Example 1:

Input: s = "110", k = 1
Output: 1
Explanation:
There is one '0' in s.
Since k = 1, we can flip it directly in one operation.

Example 2:

Input: s = "0101", k = 3
Output: 2
Explanation:
One optimal set of operations choosing k = 3 indices in each operation is:
Operation 1: Flip indices [0, 1, 3]. s changes from "0101" to "1000".
Operation 2: Flip indices [1, 2, 3]. s changes from "1000" to "1111".
Thus, the minimum number of operations is 2.

Example 3:

Input: s = "101", k = 2
Output: -1
Explanation:
Since k = 2 and s has only one '0', it is impossible to flip exactly k
indices to make all '1'. Hence, the answer is -1.

Constraints:

1 <= s.length <= 10^5
s[i] is either '0' or '1'.
1 <= k <= s.length

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
