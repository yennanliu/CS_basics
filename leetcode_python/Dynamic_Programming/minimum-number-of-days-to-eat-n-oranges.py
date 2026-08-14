"""

1553. Minimum Number of Days to Eat N Oranges
Hard

There are n oranges in the kitchen and you decided to eat some of these oranges every day as follows:

Eat one orange.
If the number of remaining oranges n is divisible by 2 then you can eat n / 2 oranges.
If the number of remaining oranges n is divisible by 3 then you can eat 2 * (n / 3) oranges.

You can only choose one of the actions per day.

Given the integer n, return the minimum number of days to eat n oranges.

Example 1:

Input: n = 10
Output: 4
Explanation: You have 10 oranges.
Day 1: Eat 1 orange,  10 - 1 = 9.
Day 2: Eat 6 oranges, 9 - 2*(9/3) = 9 - 6 = 3. (Since 9 is divisible by 3)
Day 3: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1.
Day 4: Eat the last orange  1 - 1  = 0.
You need at least 4 days to eat the 10 oranges.

Example 2:

Input: n = 6
Output: 3
Explanation: You have 6 oranges.
Day 1: Eat 3 oranges, 6 - 6/2 = 6 - 3 = 3. (Since 6 is divisible by 2).
Day 2: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1. (Since 3 is divisible by 3)
Day 3: Eat the last orange  1 - 1  = 0.
You need at least 3 days to eat the 6 oranges.

Constraints:

1 <= n <= 2 * 10^9

"""

# V0
# IDEA : MEMOIZED DFS (only n//2 and n//3 states are ever reachable)
#
#   eating 1 orange at a time is never worth more than the few steps
#   needed to reach a multiple of 2 or 3, so :
#     dfs(n) = 1 + min( n % 2 + dfs(n // 2),
#                       n % 3 + dfs(n // 3) )
#   NOTE : the reachable states are exactly the values n // (2^a * 3^b),
#          so the memo holds only O(log^2 n) entries.
#
# time = O(log(n)^2), space = O(log(n)^2)
class Solution(object):
    def minDays(self, n):
        memo = {0: 0, 1: 1}

        def dfs(k):
            if k in memo:
                return memo[k]
            by2 = k % 2 + dfs(k // 2)
            by3 = k % 3 + dfs(k // 3)
            memo[k] = 1 + min(by2, by3)
            return memo[k]

        return dfs(n)
