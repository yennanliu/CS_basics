"""

3528. Unit Conversion I
Medium

There are n types of units indexed from 0 to n - 1.

You are given a 2D integer array conversions of length n - 1, where
conversions[i] = [sourceUnit_i, targetUnit_i, conversionFactor_i]; this indicates
that a single unit of type sourceUnit_i is equivalent to conversionFactor_i units
of type targetUnit_i.

Return an array baseUnitConversion of length n, where baseUnitConversion[i] is
the number of units of type i equivalent to a single unit of type 0. Since the
answer may be large, return each baseUnitConversion[i] modulo 10^9 + 7.


Example 1:

Input: n = 3, conversions = [[0,1,2],[1,2,3]]
Output: [1,2,6]
Explanation:
Convert a single unit of type 0 into 2 units of type 1 using conversions[0].
Convert a single unit of type 0 into 6 units of type 2 using conversions[0], then
conversions[1].

Example 2:

Input: n = 6, conversions = [[0,1,2],[0,2,3],[1,3,4],[1,4,5],[2,5,2]]
Output: [1,2,3,8,10,6]
Explanation:
Convert a single unit of type 0 into 2 units of type 1 using conversions[0].
Convert a single unit of type 0 into 3 units of type 2 using conversions[1].
Convert a single unit of type 0 into 8 units of type 3 using conversions[0], then
conversions[2].
Convert a single unit of type 0 into 10 units of type 4 using conversions[0], then
conversions[3].
Convert a single unit of type 0 into 6 units of type 5 using conversions[1], then
conversions[4].


Constraints:

1 <= n <= 10^5
conversions.length == n - 1
0 <= sourceUnit_i, targetUnit_i < n
1 <= conversionFactor_i <= 10^9
It is guaranteed that unit 0 can be converted into any other unit through a
unique combination of conversions without any cycles.

"""

# V0
# IDEA : THE CONVERSIONS FORM A TREE ROOTED AT 0 — ONE DFS MULTIPLIES ALONG IT
#
#   n - 1 edges plus "unit 0 reaches every unit by a unique chain" means the
#   graph is exactly a rooted tree.  so each node's factor is the product of the
#   factors on the single path from the root, which one traversal accumulates.
#
#   the traversal is written with an explicit stack: n is up to 1e5 and a deep
#   chain would blow python's recursion limit.
#
# time = O(n), space = O(n)
class Solution(object):
    def baseUnitConversions(self, n, conversions):
        MOD = 10 ** 9 + 7
        adj = [[] for _ in range(n)]
        for u, v, f in conversions:
            adj[u].append((v, f))
        res = [0] * n
        res[0] = 1
        stack = [0]
        while stack:
            u = stack.pop()
            base = res[u]
            for v, f in adj[u]:
                res[v] = base * f % MOD
                stack.append(v)
        return res
