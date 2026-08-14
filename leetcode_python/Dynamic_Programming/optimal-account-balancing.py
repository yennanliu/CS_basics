"""

465. Optimal Account Balancing
Hard

You are given an array of transactions transactions where
transactions[i] = [fromi, toi, amounti] indicates that the person with
ID = fromi gave amounti $ to the person with ID = toi.

Return the minimum number of transactions required to settle the debt.

Example 1:

Input: transactions = [[0,1,10],[2,0,5]]
Output: 2
Explanation:
Person #0 gave person #1 $10.
Person #2 gave person #0 $5.
Two transactions are needed. One way to settle the debt is person #1 pays
person #0 and #2 $5 each.

Example 2:

Input: transactions = [[0,1,10],[1,0,1],[1,2,5],[2,0,5]]
Output: 1
Explanation:
Person #0 gave person #1 $10.
Person #1 gave person #0 $1.
Person #1 gave person #2 $5.
Person #2 gave person #0 $5.
Therefore, person #1 only need to give person #0 $4, and all debt is settled.

Constraints:

1 <= transactions.length <= 8
transactions[i].length == 3
0 <= fromi, toi < 12
fromi != toi
1 <= amounti <= 100

"""

# V0
# IDEA : NET BALANCES + DFS BACKTRACKING
#
#  Who paid whom does not matter, only each person's NET balance does.
#  Drop everyone whose net balance is 0 -> we get a list `bal` of m non-zero
#  balances that sums to 0.
#
#  Settling m non-zero people needs at most m - 1 transactions. To do better we
#  want to split them into as many zero-sum groups as possible (a group of size
#  g costs g - 1). DFS does exactly that implicitly:
#
#    at index i, try to pay off bal[i] using any later person of OPPOSITE sign,
#    push bal[i] into them (1 transaction), recurse on i + 1, then undo.
#
# time = O(m!)  # m = number of non-zero balances (<= 12), heavily pruned
# space = O(m)
from collections import defaultdict
class Solution(object):
    def minTransfers(self, transactions):
        net = defaultdict(int)
        for f, t, amount in transactions:
            net[f] -= amount
            net[t] += amount

        bal = [v for v in net.values() if v != 0]
        m = len(bal)

        def dfs(i):
            # skip already-settled people
            while i < m and bal[i] == 0:
                i += 1
            if i == m:
                return 0

            best = float('inf')
            for j in range(i + 1, m):
                # only a person of the opposite sign can absorb bal[i]
                if bal[i] * bal[j] < 0:
                    bal[j] += bal[i]
                    best = min(best, 1 + dfs(i + 1))
                    bal[j] -= bal[i]
            return best

        return dfs(0)


# V1
# IDEA : BITMASK DP OVER SUBSETS (provably optimal, no backtracking)
#
#  For a subset S of the non-zero balances:
#    - if sum(S) == 0, S can be settled on its own with at most |S| - 1 moves
#    - and we can do better by splitting S into two zero-sum halves:
#        f[S] = min(|S| - 1, min over sub-subsets T of f[T] + f[S ^ T])
#    - if sum(S) != 0, S is unsettleable on its own -> INF
#
# time = O(3^m)   # subset-of-subset enumeration, m <= 12
# space = O(2^m)
class Solution2(object):
    def minTransfers(self, transactions):
        net = defaultdict(int)
        for f, t, amount in transactions:
            net[f] -= amount
            net[t] += amount

        bal = [v for v in net.values() if v != 0]
        m = len(bal)
        if m == 0:
            return 0

        INF = float('inf')
        f = [INF] * (1 << m)
        f[0] = 0

        for mask in range(1, 1 << m):
            s = 0
            bits = 0
            for j in range(m):
                if mask >> j & 1:
                    s += bal[j]
                    bits += 1
            if s != 0:
                continue                    # cannot settle on its own

            f[mask] = bits - 1              # worst case: chain them
            sub = (mask - 1) & mask         # iterate proper non-empty subsets
            while sub > 0:
                if f[sub] != INF and f[mask ^ sub] != INF:
                    f[mask] = min(f[mask], f[sub] + f[mask ^ sub])
                sub = (sub - 1) & mask

        return f[(1 << m) - 1]
