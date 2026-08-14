"""

1563. Stone Game V
Hard

There are several stones arranged in a row, and each stone has an associated value which is an integer given in the array stoneValue.

In each round of the game, Alice divides the row into two non-empty rows (i.e. left row and right row), then Bob calculates the value of each row which is the sum of the values of all the stones in this row. Bob throws away the row which has the maximum value, and Alice's score increases by the value of the remaining row. If the value of the two rows are equal, Bob lets Alice decide which row will be thrown away. The next round starts with the remaining row.

The game ends when there is only one stone remaining. Alice's score is initially zero.

Return the maximum score that Alice can obtain.

Example 1:

Input: stoneValue = [6,2,3,4,5,5]
Output: 18
Explanation: In the first round, Alice divides the row to [6,2,3], [4,5,5]. The left row has the value 11 and the right row has value 14. Bob throws away the right row and Alice's score is now 11.
In the second round Alice divides the row to [6], [2,3]. This time Bob throws away the left row and Alice's score becomes 16 (11 + 5).
The last round Alice has only one choice to divide the row which is [2], [3]. Bob throws away the right row and Alice's score is now 18 (16 + 2). The game ends because only one stone is remaining in the row.

Example 2:

Input: stoneValue = [7,7,7,7,7,7,7]
Output: 28

Example 3:

Input: stoneValue = [4]
Output: 0

Constraints:

1 <= stoneValue.length <= 500
1 <= stoneValue[i] <= 10^6

"""

# V0
# IDEA : INTERVAL DP + PREFIX MAX (O(n^2), the plain O(n^3) memo is too slow)
#
#   f[i][j] = best score Alice gets from the row a[i..j].
#   splitting at k gives L = s(i,k), R = s(k+1,j) :
#     L <  R -> L + f[i][k]      (right row thrown away)
#     L >  R -> R + f[k+1][j]
#     L == R -> L + max(both)
#   as k grows L grows and R shrinks, so one threshold k* splits the
#   choices in two ; the two sides are answered in O(1) by
#     mx_l[i][j] = max over t <= j of s(i,t) + f[i][t]
#     mx_r[i][j] = max over t >= i of s(t,j) + f[t][j]
#   NOTE : k* is non-decreasing in j -> a single moving pointer per i.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def stoneGameV(self, stoneValue):
        a = stoneValue
        n = len(a)
        if n < 2:
            return 0
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + a[i]

        f = [[0] * n for _ in range(n)]
        mx_l = [[0] * n for _ in range(n)]
        mx_r = [[0] * n for _ in range(n)]

        for i in range(n - 1, -1, -1):
            mx_l[i][i] = a[i]
            mx_r[i][i] = a[i]
            k = i - 1  # largest split with 2 * s(i, k) <= s(i, j)
            for j in range(i + 1, n):
                total = pre[j + 1] - pre[i]
                while k + 1 <= j - 1 and 2 * (pre[k + 2] - pre[i]) <= total:
                    k += 1
                best = 0
                if k >= i:
                    left = pre[k + 1] - pre[i]
                    if k > i:
                        best = mx_l[i][k - 1]
                    if 2 * left == total:
                        cand = left + max(f[i][k], f[k + 1][j])
                    else:
                        cand = left + f[i][k]
                    if cand > best:
                        best = cand
                if k + 2 <= j and mx_r[k + 2][j] > best:
                    best = mx_r[k + 2][j]
                f[i][j] = best
                v = total + best
                mx_l[i][j] = mx_l[i][j - 1] if mx_l[i][j - 1] > v else v
                mx_r[i][j] = mx_r[i + 1][j] if mx_r[i + 1][j] > v else v
        return f[0][n - 1]
