"""

2307. Check for Contradictions in Equations
Hard

You are given a 2D array of strings equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] means that Ai / Bi = values[i].

Determine if there exists a contradiction in the equations. Return true if there is a contradiction, or false otherwise.

Note:

When checking if two numbers are equal, check that their absolute difference is less than 10^-5.
The testcases are generated such that there are no cases targeting precision, i.e. using double is enough to solve the problem.


Example 1:

Input: equations = [["a","b"],["b","c"],["a","c"]], values = [3,0.5,1.5]
Output: false
Explanation:
The given equations are: a / b = 3, b / c = 0.5, a / c = 1.5
There are no contradictions in the equations. One possible assignment to satisfy all equations is:
a = 3, b = 1 and c = 2.

Example 2:

Input: equations = [["le","et"],["le","code"],["code","et"]], values = [2,5,0.5]
Output: true
Explanation:
The given equations are: le / et = 2, le / code = 5, code / et = 0.5
Based on the first two equations, we get code / et = 0.4.
Since the third equation is code / et = 0.5, we get a contradiction.


Constraints:

1 <= equations.length <= 100
equations[i].length == 2
1 <= Ai.length, Bi.length <= 5
Ai, Bi consist of lowercase English letters.
equations.length == values.length
0.0 < values[i] <= 10.0
values[i] has a maximum of 2 decimal places.

"""

# V0
# IDEA : WEIGHTED UNION FIND (w[x] = value(x) / value(root(x)))
#
#   each variable keeps a ratio to its component root. find() compresses
#   the path and multiplies the ratios along the way, so after find(x)
#   w[x] is exactly value(x) / value(root).
#
#   for a / b = v :
#     - different roots -> merge; attach root(b) under root(a) with
#       weight  w[rb] = v * w[a] / w[b]   (so the ratios stay consistent)
#     - same root -> the ratio is already fixed at w[a] / w[b];
#       a contradiction iff | v * w[a] - w[b] | >= 1e-5
#
#   NOTE : find() is written iteratively (collect the path, then rewrite it)
#          to avoid any recursion depth concern.
#
# time = O(m * alpha(n)), space = O(n)
class Solution(object):
    def checkContradictions(self, equations, values):
        idx = {}
        for a, b in equations:
            if a not in idx:
                idx[a] = len(idx)
            if b not in idx:
                idx[b] = len(idx)

        n = len(idx)
        par = list(range(n))
        w = [1.0] * n

        def find(x):
            # collect the path up to the root
            path = []
            while par[x] != x:
                path.append(x)
                x = par[x]
            # rewrite it, accumulating ratios from the top down
            for node in reversed(path):
                w[node] *= w[par[node]]
                par[node] = x
            return x

        eps = 1e-5
        for i in range(len(equations)):
            a, b = idx[equations[i][0]], idx[equations[i][1]]
            v = values[i]
            ra, rb = find(a), find(b)
            if ra != rb:
                par[rb] = ra
                w[rb] = v * w[a] / w[b]
            elif abs(v * w[a] - w[b]) >= eps:
                return True
        return False
