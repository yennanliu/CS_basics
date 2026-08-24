"""

1872. Stone Game VIII
Hard

Alice and Bob take turns playing a game, with Alice starting first.

There are n stones arranged in a row. On each player's turn, while the number of stones is more than one, they will do the following:

Choose an integer x > 1, and remove the leftmost x stones from the row.
Add the sum of the removed stones' values to the player's score.
Place a new stone, whose value is equal to that sum, on the left side of the row.

The game stops when only one stone is left in the row.

The score difference between Alice and Bob is (Alice's score - Bob's score). Alice's goal is to maximize the score difference, and Bob's goal is the minimize the score difference.

Given an integer array stones of length n where stones[i] represents the value of the ith stone from the left, return the score difference between Alice and Bob if they both play optimally.


Example 1:

Input: stones = [-1,2,-3,4,-5]
Output: 5
Explanation:
- Alice removes the first 4 stones, adds (-1) + 2 + (-3) + 4 = 2 to her score, and places a stone of value 2 on the left. stones = [2,-5].
- Bob removes the first 2 stones, adds 2 + (-5) = -3 to his score, and places a stone of value -3 on the left. stones = [-3].
The difference between their scores is 2 - (-3) = 5.

Example 2:

Input: stones = [7,-6,5,10,5,-2,-6]
Output: 13
Explanation:
- Alice removes all stones, adds 7 + (-6) + 5 + 10 + 5 + (-2) + (-6) = 13 to her score, and places a stone of value 13 on the left. stones = [13].
The difference between their scores is 13 - 0 = 13.

Example 3:

Input: stones = [-10,-12]
Output: -22
Explanation:
- Alice can only make one move, which is to remove both stones. She adds (-10) + (-12) = -22 to her score and places a stone of value -22 on the left. stones = [-22].
The difference between their scores is (-22) - 0 = -22.


Constraints:

n == stones.length
2 <= n <= 10^5
-10^4 <= stones[i] <= 10^4

"""

# V0
# IDEA : PREFIX SUM + SUFFIX DP (a move is just "pick a prefix boundary")
#
#   KEY OBSERVATION : merging the leftmost x stones into their sum means the
#   board is ALWAYS described by one index i - the current head stone equals
#   pre[i] (the prefix sum of the original array), and the untouched tail is
#   stones[i+1 ...]. so a move from state i is simply "choose j > i", scoring
#   pre[j].
#
#   dp[i] = best (current player - opponent) difference when the player to
#           move must pick some j >= i :
#     dp[i] = max( dp[i + 1],          # skip i, let a later boundary be used
#                  pre[i] - dp[i + 1] ) # take j = i, then roles swap
#     dp[n - 1] = pre[n - 1]           # forced to take everything
#
#   Alice moves first and must take at least 2 stones -> answer = dp[1].
#
#   NOTE : dp only ever reads i+1, so a single rolling variable suffices.
#
"""

DP def
    KEY OBSERVATION: merging the leftmost x stones into their sum means the
    board is ALWAYS described by one index i - the current head stone equals
    pre[i] (the prefix sum of the original array) and the untouched tail is
    stones[i+1..]. so a move from state i is simply "choose j > i", scoring
    pre[j].

    dp[i]: best (current player - opponent) difference when the player to

           move must pick some j >= i

DP eq

     dp[i] = max(
                dp[i+1],              # SKIP i, let a later boundary be used

                pre[i] - dp[i+1]      # TAKE j = i, then the roles swap
             )

     dp[n-1] = pre[n-1]               # forced to take everything


    -> e.g. dp only ever reads i+1, so ONE rolling variable suffices

     ans = dp[1]      # Alice moves first and must take at least 2 stones

"""
# time = O(n), space = O(n) for the prefix sums
class Solution(object):
    def stoneGameVIII(self, stones):
        n = len(stones)

        pre = [0] * n
        pre[0] = stones[0]
        for i in range(1, n):
            pre[i] = pre[i - 1] + stones[i]

        dp = pre[n - 1]
        for i in range(n - 2, 0, -1):
            dp = max(dp, pre[i] - dp)

        return dp
