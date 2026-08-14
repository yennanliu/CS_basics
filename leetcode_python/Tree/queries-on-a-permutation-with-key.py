"""

1409. Queries on a Permutation With Key
Medium

Given the array queries of positive integers between 1 and m, you have to
process all queries[i] (from i=0 to i=queries.length-1) according to the
following rules:

In the beginning, you have the permutation P=[1,2,3,...,m].

For the current i, find the position of queries[i] in the permutation P
(indexing from 0) and then move this at the beginning of the permutation P.
Notice that the position of queries[i] in P is the result for queries[i].

Return an array containing the result for the given queries.


Example 1:

Input: queries = [3,1,2,1], m = 5
Output: [2,1,2,1]
Explanation: The queries are processed as follow:
For i=0: queries[i]=3, P=[1,2,3,4,5], position of 3 in P is 2,
         then we move 3 to the beginning of P resulting in P=[3,1,2,4,5].
For i=1: queries[i]=1, P=[3,1,2,4,5], position of 1 in P is 1,
         then we move 1 to the beginning of P resulting in P=[1,3,2,4,5].
For i=2: queries[i]=2, P=[1,3,2,4,5], position of 2 in P is 2,
         then we move 2 to the beginning of P resulting in P=[2,1,3,4,5].
For i=3: queries[i]=1, P=[2,1,3,4,5], position of 1 in P is 1,
         then we move 1 to the beginning of P resulting in P=[1,2,3,4,5].
Therefore, the array containing the result is [2,1,2,1].

Example 2:

Input: queries = [4,1,2,2], m = 4
Output: [3,1,2,0]

Example 3:

Input: queries = [7,5,5,8,3], m = 8
Output: [6,5,0,7,5]


Constraints:

1 <= m <= 10^3
1 <= queries.length <= m
1 <= queries[i] <= m

"""

# V0
# IDEA : SIMULATION (m <= 1000, so a plain list is fast enough)
# time = O(m * q), q = len(queries)
# space = O(m)
class Solution(object):
    def processQueries(self, queries, m):
        p = list(range(1, m + 1))
        res = []
        for v in queries:
            j = p.index(v)
            res.append(j)
            p.pop(j)
            p.insert(0, v)
        return res

# V1
# IDEA : BINARY INDEXED TREE (Fenwick)
#
#   Lay the values out on a virtual line of size m + q.
#   Slots [q+1 .. q+m] hold the initial permutation; slots q, q-1, ... are the
#   free space in FRONT of the line that we move queried values into.
#   The 0-based position of a value = number of live values strictly to its left
#   = prefix sum up to its slot (after removing itself).
#
# time = O((m + q) log (m + q))
# space = O(m + q)
class BinaryIndexedTree(object):
    def __init__(self, n):
        self.n = n
        self.c = [0] * (n + 1)

    def update(self, x, delta):
        while x <= self.n:
            self.c[x] += delta
            x += x & -x

    def query(self, x):
        s = 0
        while x > 0:
            s += self.c[x]
            x -= x & -x
        return s


class Solution(object):
    def processQueries(self, queries, m):
        q = len(queries)
        pos = [0] * (m + 1)
        tree = BinaryIndexedTree(m + q)
        for i in range(1, m + 1):
            pos[i] = q + i
            tree.update(q + i, 1)

        res = []
        for i, v in enumerate(queries):
            j = pos[v]
            tree.update(j, -1)
            res.append(tree.query(j))
            pos[v] = q - i
            tree.update(q - i, 1)
        return res
