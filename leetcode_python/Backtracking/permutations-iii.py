"""

3437. Permutations III
Medium

Given an integer n, an alternating permutation is a permutation of the first n
positive integers such that no two adjacent elements are both odd or both even.

Return all such alternating permutations sorted in lexicographical order.

Example 1:

Input: n = 4

Output: [[1,2,3,4],[1,4,3,2],[2,1,4,3],[2,3,4,1],[3,2,1,4],[3,4,1,2],[4,1,2,3],[4,3,2,1]]

Example 2:

Input: n = 2

Output: [[1,2],[2,1]]

Example 3:

Input: n = 3

Output: [[1,2,3],[3,2,1]]

Constraints:

1 <= n <= 10

"""

# V0
# IDEA : BACKTRACKING THAT ONLY EVER OFFERS THE OPPOSITE PARITY
#
#   "no two adjacent elements share a parity" means the parities must alternate,
#   so once the first element is placed the parity of every later slot is fixed.
#   the search therefore never has to test the constraint after the fact — at
#   each step it simply iterates over the unused numbers of the required parity.
#
#   trying the candidates in increasing order makes the recursion emit complete
#   permutations in lexicographic order, so no final sort is needed.
#
#   for odd n one parity class is strictly larger and must occupy both ends;
#   that is handled implicitly — the smaller class runs out and the branch dies.
#
# time = O(n * number of alternating permutations), space = O(n)
class Solution(object):
    def permute(self, n):
        res = []
        cur = []
        used = [False] * (n + 1)

        def dfs():
            if len(cur) == n:
                res.append(cur[:])
                return
            if cur:
                want = 1 - (cur[-1] & 1)
                lo = 2 if want == 0 else 1
                for v in range(lo, n + 1, 2):
                    if not used[v]:
                        used[v] = True
                        cur.append(v)
                        dfs()
                        cur.pop()
                        used[v] = False
            else:
                for v in range(1, n + 1):
                    used[v] = True
                    cur.append(v)
                    dfs()
                    cur.pop()
                    used[v] = False

        dfs()
        return res
