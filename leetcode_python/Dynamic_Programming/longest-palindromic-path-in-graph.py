"""

3615. Longest Palindromic Path in Graph
Hard

You are given an integer n and an undirected graph with n nodes labeled from 0
to n - 1 and a 2D array edges, where edges[i] = [ui, vi] indicates an edge
between nodes ui and vi.

You are also given a string label of length n, where label[i] is the character
associated with node i.

You may start at any node and move to any adjacent node, visiting each node at
most once.

Return the maximum possible length of a palindrome that can be formed by
visiting a set of unique nodes along a valid path.


Example 1:

Input: n = 3, edges = [[0,1],[1,2]], label = "aba"
Output: 3
Explanation:
The longest palindromic path is from node 0 to node 2 via node 1, following the
path 0 -> 1 -> 2 forming string "aba".
This is a valid palindrome of length 3.

Example 2:

Input: n = 3, edges = [[0,1],[0,2]], label = "abc"
Output: 1
Explanation:
No path with more than one node forms a palindrome.
The best option is any single node, giving a palindrome of length 1.

Example 3:

Input: n = 4, edges = [[0,2],[0,3],[3,1]], label = "bbac"
Output: 3
Explanation:
The longest palindromic path is from node 0 to node 1, following the path
0 -> 3 -> 1, forming string "bcb".
This is a valid palindrome of length 3.


Constraints:

1 <= n <= 14
n - 1 <= edges.length <= n * (n - 1) / 2
edges[i] == [ui, vi]
0 <= ui, vi <= n - 1
ui != vi
label.length == n
label consists of lowercase English letters.
There are no duplicate edges.

"""

# V0
# IDEA : BITMASK DP GROWING THE PALINDROME FROM BOTH ENDS INWARD
#
#   a palindromic path p0..p(L-1) pairs up p0 with p(L-1), p1 with p(L-2), and
#   so on. building it left-to-right is hopeless because the constraint links
#   the two far ends, but building it *outside-in* makes every constraint
#   local: pick the matching pair of endpoints first, then repeatedly step
#   both frontiers one edge inward, always choosing two nodes with the same
#   letter.
#
#   so a state is (u, v, mask): u and v are the two current frontier nodes and
#   mask is the set of nodes already spent on the two arms. popcount(mask) is
#   always even and is exactly the number of characters fixed so far — the two
#   arms are grown in lockstep, so no length bookkeeping is needed.
#
#   a state finishes the path in one of two ways, and both are checked in
#   place: if u and v are adjacent the two arms meet and the palindrome has
#   even length popcount(mask); if instead some unused node is adjacent to
#   both, it becomes the single middle character and the length is
#   popcount(mask) + 1. a lone node is always a palindrome, so 1 is the floor.
#
#   the trick that keeps this fast is that the choice of the new v only
#   depends on the *union* of the neighbourhoods of the current partners of u.
#   any v in that union came from some concrete partner, so or-ing them all
#   together loses nothing and collapses the inner double loop over
#   (partner, new partner) into one precomputed lookup, orAdj[set].
#
"""

DP def
    a palindromic path pairs p0 with p(L-1), p1 with p(L-2), ... so building
    it left-to-right is hopeless (the constraint links the FAR ends). build it
    OUTSIDE-IN instead and every constraint becomes local.

    state (u, v, mask): u and v are the two current FRONTIER nodes, and mask

           is the set of nodes already spent on the two arms

           -> popcount(mask) is always EVEN and is exactly the number of
              characters fixed so far (the arms grow in lockstep, so no
              length bookkeeping is needed)

DP eq

     step both frontiers one edge inward, to any pair (u', v') with

        u' adjacent to u, v' adjacent to v, u'/v' unused,
        and label[u'] == label[v']

     a state FINISHES the path in one of two ways:

        u and v adjacent            -> even length  popcount(mask)

        some unused w adjacent to BOTH -> w is the single middle character
                                       -> length popcount(mask) + 1


    -> e.g. the speed-up: the choice of the new v depends only on the UNION
              of the neighbourhoods of u's current partners, so or-ing them
              collapses the inner (partner, new partner) double loop into
              one precomputed lookup orAdj[set]

     a lone node is always a palindrome, so 1 is the floor
     ans = max length found

"""
# time = O(2^n * n^2), space = O(2^n * n)
class Solution(object):
    def maxLen(self, n, edges, label):
        adj = [0] * n
        for u, v in edges:
            adj[u] |= 1 << v
            adj[v] |= 1 << u

        # nodes sharing a letter with node i (i itself included)
        by_letter = [0] * 26
        for i in range(n):
            by_letter[ord(label[i]) - 97] |= 1 << i
        same = [by_letter[ord(label[i]) - 97] for i in range(n)]

        size = 1 << n
        full = size - 1

        # or_adj[s] = union of the neighbourhoods of every node in s
        or_adj = [0] * size
        for s in range(1, size):
            low = s & -s
            or_adj[s] = or_adj[s ^ low] | adj[low.bit_length() - 1]

        pc = [0] * size
        for s in range(1, size):
            pc[s] = pc[s >> 1] + (s & 1)

        # reach[mask][u] = bitmask of v with state (u, v, mask) reachable
        reach = [None] * size
        for u in range(n):
            for v in range(u + 1, n):
                if label[u] == label[v]:
                    m = (1 << u) | (1 << v)
                    if reach[m] is None:
                        reach[m] = [0] * n
                    reach[m][u] |= 1 << v
                    reach[m][v] |= 1 << u

        res = 1
        for mask in range(size):
            row = reach[mask]
            if row is None:
                continue
            p = pc[mask]
            avail = full & ~mask
            for u in range(n):
                vs = row[u]
                if not vs:
                    continue
                au = adj[u]

                # the two arms meet directly -> even length palindrome
                if au & vs and p > res:
                    res = p

                # an unused node adjacent to both arms -> odd length palindrome
                if p + 1 > res:
                    b = vs
                    while b:
                        low = b & -b
                        b ^= low
                        if au & adj[low.bit_length() - 1] & avail:
                            res = p + 1
                            break

                # step both frontiers one edge inward
                orv = or_adj[vs] & avail
                if not orv:
                    continue
                bu = au & avail
                while bu:
                    lu = bu & -bu
                    bu ^= lu
                    up = lu.bit_length() - 1
                    cands = orv & same[up] & ~lu
                    while cands:
                        lv = cands & -cands
                        cands ^= lv
                        nm = mask | lu | lv
                        nrow = reach[nm]
                        if nrow is None:
                            nrow = reach[nm] = [0] * n
                        nrow[up] |= lv
                        nrow[lv.bit_length() - 1] |= lu
            if res == n:
                return n
        return res
