"""

3161. Block Placement Queries
Hard

There exists an infinite number line, with its origin at 0 and extending towards the positive x-axis.

You are given a 2D array queries, which contains two types of queries:

For a query of type 1, queries[i] = [1, x]. Build an obstacle at distance x from the origin. It is guaranteed that there is no obstacle at distance x when the query is asked.
For a query of type 2, queries[i] = [2, x, sz]. Check if it is possible to place a block of size sz anywhere in the range [0, x] on the line, such that the block entirely lies in the range [0, x]. A block cannot be placed if it intersects with any obstacle, but it may touch it. Note that you do not actually place the block. Queries are separated by a gap of at least one unit.

Return a boolean array results, where results[i] is true if you can place the block specified in the ith query of type 2, and false otherwise.


Example 1:

Input: queries = [[1,2],[2,3,3],[2,3,1],[2,2,2]]
Output: [false,true,true]
Explanation:
For query 0, place an obstacle at x = 2. A block of size at most 2 can be placed before x = 3.

Example 2:

Input: queries = [[1,7],[2,7,6],[1,2],[2,7,5],[2,7,6]]
Output: [true,true,false]
Explanation:
Place an obstacle at x = 7 for query 0. A block of size at most 7 can be placed before x = 7.
Place an obstacle at x = 2 for query 2. Now, a block of size at most 5 can be placed before x = 7, and a block of size at most 2 before x = 2.


Constraints:

1 <= queries.length <= 15 * 10^4
2 <= queries[i].length <= 3
1 <= queries[i][0] <= 2
1 <= x, sz <= min(5 * 10^4, 3 * queries.length)
The input is generated such that for queries of type 1, no obstacle exists at distance x when the query is asked.
The input is generated such that there is at least one query of type 2.

"""

# V0
# IDEA : RUN THE QUERIES BACKWARDS SO INSERTIONS BECOME DELETIONS
#
#   a block of size sz fits in [0, x] iff some obstacle-free stretch inside
#   [0, x] is at least sz long. treating the origin as an obstacle at 0, the
#   candidate stretches are
#       every gap that ENDS at an obstacle p <= x   (length p - prev(p))
#       the tail from the last obstacle <= x out to x
#
#   inserting obstacles splits gaps, which is awkward to maintain. processing
#   the queries in REVERSE turns every insertion into a REMOVAL, and removing
#   an obstacle simply merges its gap into the next one — a single update.
#
#   two max-segment-trees over the coordinate axis answer the rest :
#       tree_gap : gap length stored at the obstacle that ends it
#       tree_pos : the position itself, to find the last obstacle <= x
#   both are prefix-max queries, and a doubly linked list over the sorted
#   obstacle positions supplies each removal's neighbours in O(1).
#
# time = O((n + maxX) log maxX), space = O(n + maxX)
class Solution(object):
    def getResults(self, queries):
        NEG = -1
        max_x = 0
        for q in queries:
            max_x = max(max_x, q[1])
        size = max_x + 1

        class MaxTree(object):
            def __init__(self, n):
                self.n = n
                self.t = [NEG] * (2 * n)

            def update(self, i, v):
                i += self.n
                self.t[i] = v
                i >>= 1
                while i:
                    self.t[i] = max(self.t[2 * i], self.t[2 * i + 1])
                    i >>= 1

            def query(self, lo, hi):            # max over [lo, hi]
                res = NEG
                lo += self.n
                hi += self.n + 1
                while lo < hi:
                    if lo & 1:
                        res = max(res, self.t[lo]); lo += 1
                    if hi & 1:
                        hi -= 1; res = max(res, self.t[hi])
                    lo >>= 1
                    hi >>= 1
                return res

        # every obstacle that ever exists, plus the origin
        positions = sorted(set([0] + [q[1] for q in queries if q[0] == 1]))
        idx = {p: i for i, p in enumerate(positions)}
        m = len(positions)
        prev_i = [i - 1 for i in range(m)]
        next_i = [i + 1 if i + 1 < m else -1 for i in range(m)]

        tree_gap = MaxTree(size)
        tree_pos = MaxTree(size)
        tree_pos.update(0, 0)
        for i in range(1, m):
            tree_gap.update(positions[i], positions[i] - positions[i - 1])
            tree_pos.update(positions[i], positions[i])

        res = []
        for q in reversed(queries):
            if q[0] == 2:
                _, x, sz = q
                best = tree_gap.query(0, x)          # gaps closing at an obstacle <= x
                last = tree_pos.query(0, x)          # last obstacle at or before x
                if last != NEG:
                    best = max(best, x - last)       # the open tail up to x
                res.append(best >= sz)
            else:
                x = q[1]
                i = idx[x]
                a, b = prev_i[i], next_i[i]
                tree_gap.update(x, NEG)
                tree_pos.update(x, NEG)
                next_i[a] = b
                if b != -1:
                    prev_i[b] = a
                    tree_gap.update(positions[b], positions[b] - positions[a])

        res.reverse()
        return res
