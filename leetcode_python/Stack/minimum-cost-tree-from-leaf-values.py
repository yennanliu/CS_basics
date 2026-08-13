"""

1130. Minimum Cost Tree From Leaf Values
Medium

Given an array arr of positive integers, consider all binary trees such that:

Each node has either 0 or 2 children;
The values of arr correspond to the values of each leaf in an in-order traversal of the tree.
The value of each non-leaf node is equal to the product of the largest leaf value
in its left and right subtree, respectively.

Among all possible binary trees considered, return the smallest possible sum of the values
of each non-leaf node. It is guaranteed this sum fits into a 32-bit integer.

A node is a leaf if and only if it has zero children.


Example 1:

Input: arr = [6,2,4]
Output: 32
Explanation: There are two possible trees shown.
The first has a non-leaf node sum 36, and the second has non-leaf node sum 32.

Example 2:

Input: arr = [4,11]
Output: 44


Constraints:

2 <= arr.length <= 40
1 <= arr[i] <= 15
It is guaranteed that the answer fits into a 32-bit signed integer (i.e., it is less than 2^31).

"""

# V0
# IDEA : MONOTONIC (DECREASING) STACK + GREEDY
#        every time we merge 2 neighbouring leaves we pay
#        (left max) * (right max), and the smaller of the 2 disappears.
#        -> so each element should be merged (removed) as early as possible,
#           paired with the SMALLER of its 2 nearest greater neighbours.
#        -> keep a decreasing stack, when arr[i] >= stack top,
#           pop the top and pay top * min(new top, arr[i]).
# time = O(n)
# space = O(n)
class Solution(object):
    def mctFromLeafValues(self, arr):
        res = 0
        # NOTE !!! sentinel `inf` so stack[-1] is always valid
        stack = [float('inf')]
        for a in arr:
            while stack[-1] <= a:
                mid = stack.pop()
                res += mid * min(stack[-1], a)
            stack.append(a)
        # remaining stack is strictly decreasing -> merge from the top
        while len(stack) > 2:
            res += stack.pop() * stack[-1]
        return res

# V1
# IDEA : INTERVAL DP (memoization)
#        dp(i, j) = min non-leaf sum for leaves arr[i..j]
#        split at k : dp(i, k) + dp(k+1, j) + max(arr[i..k]) * max(arr[k+1..j])
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def mctFromLeafValues(self, arr):
        n = len(arr)

        # g[i][j] = max of arr[i..j]
        g = [[0] * n for _ in range(n)]
        for i in range(n - 1, -1, -1):
            g[i][i] = arr[i]
            for j in range(i + 1, n):
                g[i][j] = max(g[i][j - 1], arr[j])

        memo = {}

        def dfs(i, j):
            if i == j:
                return 0
            if (i, j) in memo:
                return memo[(i, j)]
            best = float('inf')
            for k in range(i, j):
                best = min(best, dfs(i, k) + dfs(k + 1, j) + g[i][k] * g[k + 1][j])
            memo[(i, j)] = best
            return best

        return dfs(0, n - 1)
