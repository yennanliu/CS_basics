"""

3669. Balanced K-Factor Decomposition
Medium

Given two integers n and k, split the number n into exactly k positive
integers such that the product of these integers is equal to n.

Return any one split in which the maximum difference between any two numbers
is minimized. You may return the result in any order.

Example 1:

Input: n = 100, k = 2
Output: [10,10]
Explanation:
The split [10, 10] yields 10 * 10 = 100 and a max-min difference of 0, which
is minimal.

Example 2:

Input: n = 44, k = 3
Output: [2,2,11]
Explanation:
Split [1, 1, 44] yields a difference of 43
Split [1, 2, 22] yields a difference of 21
Split [1, 4, 11] yields a difference of 10
Split [2, 2, 11] yields a difference of 9
Therefore, [2, 2, 11] is the optimal split with the smallest difference 9.

Constraints:

4 <= n <= 10^5
2 <= k <= 5
k is strictly less than the total number of positive divisors of n.

"""

# V0
# IDEA : DFS OVER DIVISORS IN NON-DECREASING ORDER
#
#   a factorisation is a multiset, so fixing the factors to come out
#   non-decreasing removes all k! permutations of the same answer and lets
#   the spread be read off as last - first at the leaf.
#
#   at each level only divisors of what is left are viable, and they must be
#   at least the previous factor. that gives the pruning d^(levels left) <=
#   remaining, which collapses the search to a handful of nodes for k <= 5.
#
#   n <= 10^5 so listing divisors by trial division up to sqrt is cheap
#   enough to redo at every node.
#
# time = O(d(n)^(k-1) * sqrt(n)) in the worst case, tiny in practice, space = O(k)
class Solution(object):
    def minDifference(self, n, k):
        best = [None, float('inf')]

        def divisors_at_least(m, lo):
            out = []
            i = 1
            while i * i <= m:
                if m % i == 0:
                    if i >= lo:
                        out.append(i)
                    j = m // i
                    if j != i and j >= lo:
                        out.append(j)
                i += 1
            out.sort()
            return out

        def dfs(rem, left, cur):
            if left == 1:
                if not cur or rem >= cur[-1]:
                    spread = rem - (cur[0] if cur else rem)
                    if spread < best[1]:
                        best[1] = spread
                        best[0] = cur + [rem]
                return
            lo = cur[-1] if cur else 1
            for d in divisors_at_least(rem, lo):
                if d ** left > rem:
                    break
                dfs(rem // d, left - 1, cur + [d])

        dfs(n, k, [])
        return best[0]
