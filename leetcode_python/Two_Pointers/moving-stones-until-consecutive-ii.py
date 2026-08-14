"""

1040. Moving Stones Until Consecutive II
Medium

There are some stones in different positions on the X-axis. You are given an integer
array stones, the positions of the stones.

Call a stone an endpoint stone if it has the smallest or largest position.
In one move, you pick up an endpoint stone and move it to an unoccupied position so
that it is no longer an endpoint stone.

In particular, if the stones are at say, stones = [1,2,5], you cannot move the
endpoint stone at position 5, since moving it to any position (such as 0, or 3)
will still keep that stone as an endpoint stone.

The game ends when you cannot make any more moves (i.e., the stones are in
consecutive positions).

Return an integer array answer of length 2 where:

answer[0] is the minimum number of moves you can play, and
answer[1] is the maximum number of moves you can play.


Example 1:

Input: stones = [7,4,9]
Output: [1,2]
Explanation: We can move 4 -> 8 for one move to finish the game.
Or, we can move 9 -> 5, 4 -> 6 for two moves to finish the game.

Example 2:

Input: stones = [6,5,4,3,10]
Output: [2,3]
Explanation: We can move 3 -> 8 then 10 -> 7 to finish the game.
Or, we can move 3 -> 7, 4 -> 8, 5 -> 9 to finish the game.
Notice we cannot move 10 -> 2 to finish the game, because that would be an
illegal move.


Constraints:

3 <= stones.length <= 10^4
1 <= stones[i] <= 10^9
All the values of stones are unique.

"""

# V0
# IDEA : SORT + SLIDING WINDOW (min) + MATH (max)
#
#  MAX :
#    the very first move must be one of the two endpoints jumping just inside
#    the other end, which "throws away" one of the two outer gaps for good.
#    After that every move can fill exactly one empty slot.
#    -> max = max(stones[-1] - stones[1], stones[-2] - stones[0]) - (n - 2)
#
#  MIN :
#    the final n stones occupy a window of width n. Slide a window of width n
#    over the sorted stones; if it already holds k stones we need (n - k) moves
#    to bring the rest in.
#    SPECIAL CASE : the window holds n-1 stones AND they are already
#    consecutive (e.g. [1,2,3,4,10]) -> the single outside stone cannot be
#    dropped straight in with 1 move, it takes 2.
#
# time = O(n log n)
# space = O(1)
#   space is O(1) extra (the input list is sorted in place)
class Solution(object):
    def numMovesStonesII(self, stones):
        stones.sort()
        n = len(stones)

        # ---- max ----
        max_move = max(stones[-1] - stones[1], stones[-2] - stones[0]) - (n - 2)

        # ---- min (sliding window of width n) ----
        min_move = n
        i = 0
        for j in range(n):
            # keep window span <= n - 1 (i.e. width n)
            while stones[j] - stones[i] + 1 > n:
                i += 1
            cnt = j - i + 1
            if cnt == n - 1 and stones[j] - stones[i] == n - 2:
                # n-1 already-consecutive stones + 1 far away stone -> 2 moves
                min_move = min(min_move, 2)
            else:
                min_move = min(min_move, n - cnt)

        return [min_move, max_move]
