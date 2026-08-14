"""

887. Super Egg Drop
Hard

You are given k identical eggs and you have access to a building with n floors
labeled from 1 to n.

You know that there exists a floor f where 0 <= f <= n such that any egg dropped at
a floor higher than f will break, and any egg dropped at or below floor f will not break.

Each move, you may take an unbroken egg and drop it from any floor x (where 1 <= x <= n).
If the egg breaks, you can no longer use it. However, if the egg does not break,
you may reuse it in future moves.

Return the minimum number of moves that you need to determine with certainty
what the value of f is.


Example 1:

Input: k = 1, n = 2
Output: 2
Explanation:
Drop the egg from floor 1. If it breaks, we know that f = 0.
Otherwise, drop the egg from floor 2. If it breaks, we know that f = 1.
If it does not break, then we know f = 2.
Hence, we need at minimum 2 moves to determine with certainty what the value of f is.

Example 2:

Input: k = 2, n = 6
Output: 3

Example 3:

Input: k = 3, n = 14
Output: 4


Constraints:

1 <= k <= 100
1 <= n <= 10^4

"""

# V0
# IDEA : DP ON "MOVES" (flip the state around)
"""
 Instead of asking "how many moves for n floors", ask the inverse:

 DP def:
    - f[t][j] = the MAXIMUM number of floors we can fully decide
                using t moves and j eggs

 DP eq:
    - f[t][j] = f[t-1][j-1]   (egg breaks   -> search below, one fewer egg)
              + f[t-1][j]     (egg survives -> search above, same eggs)
              + 1             (the floor we just dropped from)

 Answer: the smallest t with f[t][k] >= n
"""
# time = O(k * moves), moves <= n
# space = O(k)
class Solution(object):
    def superEggDrop(self, k, n):
        # f[j] = max floors solvable with the current number of moves and j eggs
        f = [0] * (k + 1)
        moves = 0

        while f[k] < n:
            moves += 1
            # NOTE !!! iterate eggs DOWNWARDS so f[j - 1] is still
            #          the value from the previous move count
            for j in range(k, 0, -1):
                f[j] = f[j] + f[j - 1] + 1

        return moves


# V1
# IDEA : BOTTOM UP DP + BINARY SEARCH ON THE DROP FLOOR
#
#   f[i][j] = min moves for i floors with j eggs.
#   For a fixed i, dfs(mid-1, j-1) grows with mid while dfs(i-mid, j) shrinks,
#   so the optimal drop floor can be found by binary search instead of a linear scan.
#
# time = O(n * k * log(n))
# space = O(n * k)
class Solution(object):
    def superEggDrop(self, k, n):
        f = [[0] * (k + 1) for _ in range(n + 1)]

        # 1 egg -> we must scan floor by floor
        for i in range(1, n + 1):
            f[i][1] = i

        for i in range(1, n + 1):
            for j in range(2, k + 1):
                lo, hi = 1, i
                while lo < hi:
                    mid = (lo + hi + 1) // 2
                    broke = f[mid - 1][j - 1]   # increasing in mid
                    survived = f[i - mid][j]    # decreasing in mid
                    if broke <= survived:
                        lo = mid
                    else:
                        hi = mid - 1
                f[i][j] = max(f[lo - 1][j - 1], f[i - lo][j]) + 1

        return f[n][k]
