"""

488. Zuma Game
Hard

You are playing a variation of the game Zuma.

In this variation of Zuma, there is a single row of colored balls on a board, where each ball
can be colored red 'R', yellow 'Y', blue 'B', green 'G', or white 'W'.
You also have several colored balls in your hand.

Your goal is to clear all of the balls from the board. On each turn:

- Pick any ball from your hand and insert it in between two balls in the row or on either end of the row.
- If there is a group of three or more consecutive balls of the same color, remove the group of balls
  from the board.
  - If this removal causes more groups of three or more of the same color to form, then continue
    removing each group until there are none left.
- If there are no more balls on the board, then you win the game.
- Repeat this process until you either win or do not have any more balls in your hand.

Given a string board, representing the row of balls on the board, and a string hand, representing
the balls in your hand, return the minimum number of balls you have to insert to clear all the balls
from the board. If you cannot clear all the balls from the board using the balls in your hand, return -1.

Example 1:

Input: board = "WRRBBW", hand = "RB"
Output: -1
Explanation: It is impossible to clear all the balls. The best you can do is:
- Insert 'R' so the board becomes WRRRBBW. WRRRBBW -> WBBW.
- Insert 'B' so the board becomes WBBBW. WBBBW -> WW.
There are still balls remaining on the board, and you are out of balls to insert.

Example 2:

Input: board = "WWRRBBWW", hand = "WRBRW"
Output: 2
Explanation: To make the board empty:
- Insert 'R' so the board becomes WWRRRBBWW. WWRRRBBWW -> WWBBWW.
- Insert 'B' so the board becomes WWBBBWW. WWBBBWW -> WWWW -> empty.
2 balls from your hand were needed to clear the board.

Example 3:

Input: board = "G", hand = "GGGGG"
Output: 2
Explanation: To make the board empty:
- Insert 'G' so the board becomes GG.
- Insert 'G' so the board becomes GGG. GGG -> empty.
2 balls from your hand were needed to clear the board.


Constraints:

1 <= board.length <= 16
1 <= hand.length <= 5
board and hand consist of the characters 'R', 'Y', 'B', 'G', and 'W'.
The initial row of balls on the board will not have any groups of three or more consecutive balls
of the same color.

"""

# V0
# IDEA : DFS (backtracking) + MEMOIZATION on (board, hand) state
#
#        pruning (this is what makes the search feasible):
#         1) hand is sorted, so identical balls in hand are tried only once
#         2) inserting anywhere inside a run of identical balls gives the SAME board,
#            so only the first position of such a run is tried
#         3) an inserted ball is only useful when it either
#              (a) touches a same colored ball (it can grow into a group of 3), or
#              (b) splits a pair of identical balls (board[j-1] == board[j] != ball)
#            inserting 3 same colored balls into a "no same color neighbor" gap
#            removes them immediately -> board unchanged -> never helps.
#
# time = O(states * len(hand) * len(board) * len(board)), bounded by the tiny input limits
# space = O(states * (len(board) + len(hand)))
class Solution(object):
    def findMinStep(self, board, hand):

        def shrink(s):
            """remove groups of >= 3 same consecutive balls, repeatedly (cascade)"""
            i = 0
            while i < len(s):
                j = i
                while j < len(s) and s[j] == s[i]:
                    j += 1
                if j - i >= 3:
                    # a removal may create a new group -> re-run on the shorter string
                    return shrink(s[:i] + s[j:])
                i = j
            return s

        memo = {}

        def dfs(board, hand):
            if not board:
                return 0
            if not hand:
                return -1
            if (board, hand) in memo:
                return memo[(board, hand)]

            res = -1
            for i in range(len(hand)):
                # pruning 1 : skip duplicated ball in hand (hand is sorted)
                if i > 0 and hand[i] == hand[i - 1]:
                    continue
                ball = hand[i]

                for j in range(len(board) + 1):
                    # pruning 2 : same insertion inside a run of `ball` colored balls
                    if j > 0 and board[j - 1] == ball:
                        continue

                    # pruning 3 : (a) extend a same colored group / (b) split a pair
                    grow = j < len(board) and board[j] == ball
                    split = 0 < j < len(board) and board[j - 1] == board[j]
                    if not (grow or split):
                        continue

                    nxt_board = shrink(board[:j] + ball + board[j:])
                    nxt_hand = hand[:i] + hand[i + 1:]

                    sub = dfs(nxt_board, nxt_hand)
                    if sub != -1 and (res == -1 or sub + 1 < res):
                        res = sub + 1

            memo[(board, hand)] = res
            return res

        return dfs(board, "".join(sorted(hand)))
