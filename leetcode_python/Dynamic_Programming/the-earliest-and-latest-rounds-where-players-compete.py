"""

1900. The Earliest and Latest Rounds Where Players Compete
Hard

There is a tournament where n players are participating. The players are standing in a single row and are numbered from 1 to n based on their initial standing position (player 1 is the first player in the row, player 2 is the second player in the row, etc.).

The tournament consists of multiple rounds (starting from round number 1). In each round, the ith player from the front of the row competes against the ith player from the end of the row, and the winner advances to the next round. When the number of players is odd for the current round, the player in the middle automatically advances to the next round.

For example, if the row consists of players 1, 2, 4, 6, 7
Player 1 competes against player 7.
Player 2 competes against player 6.
Player 4 automatically advances to the next round.

After each round is over, the winners are lined back up in the row based on the original ordering assigned to them initially (ascending order).

The players numbered firstPlayer and secondPlayer are the best in the tournament. They can win against any other player before they compete against each other. If any two other players compete against each other, either of them might win, and thus you may choose the outcome of this round.

Given the integers n, firstPlayer, and secondPlayer, return an integer array containing two values, the earliest possible round number and the latest possible round number in which these two players will compete against each other, respectively.


Example 1:

Input: n = 11, firstPlayer = 2, secondPlayer = 4
Output: [3,4]
Explanation:
One possible scenario which leads to the earliest round number:
First round: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
Second round: 2, 3, 4, 5, 6, 11
Third round: 2, 3, 4
One possible scenario which leads to the latest round number:
First round: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
Second round: 1, 2, 3, 4, 5, 6
Third round: 1, 2, 4
Fourth round: 2, 4

Example 2:

Input: n = 5, firstPlayer = 1, secondPlayer = 5
Output: [1,1]
Explanation: The players numbered 1 and 5 compete in the first round.
There is no way to make them compete in any other round.


Constraints:

2 <= n <= 28
1 <= firstPlayer < secondPlayer <= n

"""

# V0
# IDEA : MEMOISED SEARCH ON THE STATE (n, position of f, position of s)
#
#   the identities of the other players never matter - only how many of
#   them survive to the LEFT of each of our two heroes. so the state is
#   just (row size n, 1-indexed position f, 1-indexed position s).
#
#   f + s == n + 1  ->  they are paired right now -> (1, 1).
#
#   SYMMETRY : reversing the row is a bijection of the tournament, so
#     dp(n, f, s) == dp(n, n+1-s, n+1-f)
#   which lets us always assume f + s < n + 1 (both in the "left" side).
#
#   TRANSITION : next round has half = (n + 1) // 2 players. we may freely
#   decide how many survivors land before f (i) and how many between f and
#   s (j); every reachable (i+1, i+j+2) pair in the smaller row is a legal
#   next state. when s sits in the RIGHT half, mirror s to s' = n + 1 - s
#   and shift the gap by mid, the count of survivors forced between them.
#
#   NOTE : both answers are computed in ONE recursion - min for earliest,
#          max for latest - then +1 for the round we just played.
#
# time = O(n^4 * log n) states x transitions (n <= 28, trivial in practice)
# space = O(n^3) memo entries
class Solution(object):
    def earliestAndLatest(self, n, firstPlayer, secondPlayer):
        memo = {}

        def dp(n, f, s):
            if f + s == n + 1:
                return (1, 1)
            if f + s > n + 1:                 # mirror the row
                f, s = n + 1 - s, n + 1 - f

            key = (n, f, s)
            if key in memo:
                return memo[key]

            half = (n + 1) // 2
            earliest = float('inf')
            latest = float('-inf')

            if s <= half:
                # both heroes are in the left half
                for i in range(f):
                    for j in range(s - f):
                        x, y = dp(half, i + 1, i + j + 2)
                        earliest = min(earliest, x)
                        latest = max(latest, y)
            else:
                # s is in the right half -> mirror it to s2
                s2 = n + 1 - s
                mid = (n - 2 * s2 + 1) // 2   # survivors forced between them
                for i in range(f):
                    for j in range(s2 - f):
                        x, y = dp(half, i + 1, i + j + mid + 2)
                        earliest = min(earliest, x)
                        latest = max(latest, y)

            memo[key] = (earliest + 1, latest + 1)
            return memo[key]

        a, b = dp(n, firstPlayer, secondPlayer)
        return [a, b]
