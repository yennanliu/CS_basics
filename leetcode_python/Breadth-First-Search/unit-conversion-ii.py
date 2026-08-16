"""

3535. Unit Conversion II
Medium

There are n types of units indexed from 0 to n - 1.

You are given a 2D integer array conversions of length n - 1, where
conversions[i] = [sourceUnit_i, targetUnit_i, conversionFactor_i]. This
indicates that a single unit of type sourceUnit_i is equivalent to
conversionFactor_i units of type targetUnit_i.

You are also given a 2D integer array queries of length q, where queries[i] =
[unitA_i, unitB_i].

Return an array answer of length q where answer[i] is the number of units of
type unitB_i equivalent to 1 unit of type unitA_i, and can be represented as p/q
where p and q are coprime. Return each answer[i] as pq^-1 modulo 10^9 + 7, where
q^-1 represents the multiplicative inverse of q modulo 10^9 + 7.

Example 1:

Input: conversions = [[0,1,2],[0,2,6]], queries = [[1,2],[1,0]]

Output: [3,500000004]

Explanation:

In the first query, we can convert unit 1 into 3 units of type 2 using the
inverse of conversions[0], then conversions[1].

In the second query, we can convert unit 1 into 1/2 units of type 0 using the
inverse of conversions[0]. We return 500000004 since it is the multiplicative
inverse of 2.

Example 2:

Input: conversions = [[0,1,2],[0,2,6],[0,3,8],[2,4,2],[2,5,4],[3,6,3]], queries
= [[1,2],[0,4],[6,5],[4,6],[6,1]]

Output: [3,12,1,2,83333334]

Explanation:

In the first query, we can convert unit 1 into 3 units of type 2 using the
inverse of conversions[0], then conversions[1].

In the second query, we can convert unit 0 into 12 units of type 4 using
conversions[1], then conversions[3].

In the third query, we can convert unit 6 into 1 unit of type 5 using the
inverse of conversions[5], the inverse of conversions[2], conversions[1], then
conversions[4].

In the fourth query, we can convert unit 4 into 2 units of type 6 using the
inverse of conversions[3], the inverse of conversions[1], conversions[2], then
conversions[5].

In the fifth query, we can convert unit 6 into 1/12 units of type 1 using the
inverse of conversions[5], the inverse of conversions[2], then conversions[0].
We return 83333334 since it is the multiplicative inverse of 12.

Constraints:

2 <= n <= 10^5

conversions.length == n - 1

0 <= sourceUnit_i, targetUnit_i < n

1 <= conversionFactor_i <= 10^9

1 <= q <= 10^5

queries.length == q

0 <= unitA_i, unitB_i < n

It is guaranteed that unit 0 can be uniquely converted into any other unit
through a combination of forward or backward conversions.

"""

# V0
# IDEA : ROOT THE CONVERSION TREE AT UNIT 0 AND STORE ONE RATIO PER NODE
#
#   n - 1 conversions over n units, with unit 0 able to reach everything, means
#   the conversions form a tree.  so between any two units there is exactly one
#   chain of conversions and the answer is well defined -- and, more usefully,
#   every chain factors through the root.
#
#   let f[u] be how many units of u one unit of 0 is worth, i.e. the product of
#   the factors along the path 0 -> u (edges traversed backwards divide).
#   then 1 unit of A is 1/f[A] units of 0, hence f[B]/f[A] units of B, and the
#   required answer is f[B] * f[A]^-1 modulo 10^9 + 7.
#
#   so a single BFS/DFS from unit 0 answers every query in O(log MOD) -- the
#   modular inverse by Fermat -- and no per-query traversal is ever needed.
#   the walk is iterative because n reaches 10^5 and the tree may be a path.
#
# time = O(n + q * log MOD), space = O(n)
class Solution(object):
    def queryConversions(self, conversions, queries):
        MOD = 10 ** 9 + 7
        n = len(conversions) + 1
        g = [[] for _ in range(n)]
        for s, t, f in conversions:
            g[s].append((t, f % MOD))
            g[t].append((s, pow(f, MOD - 2, MOD)))

        val = [0] * n                       # units of u that 1 unit of 0 buys
        val[0] = 1
        seen = [False] * n
        seen[0] = True
        stack = [0]
        while stack:
            u = stack.pop()
            vu = val[u]
            for v, f in g[u]:
                if not seen[v]:
                    seen[v] = True
                    val[v] = vu * f % MOD
                    stack.append(v)

        return [val[b] * pow(val[a], MOD - 2, MOD) % MOD for a, b in queries]
