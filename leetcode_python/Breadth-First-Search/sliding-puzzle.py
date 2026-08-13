"""

773. Sliding Puzzle
Hard

On an 2 x 3 board, there are five tiles labeled from 1 to 5, and an empty square
represented by 0. A move consists of choosing 0 and a 4-directionally adjacent number
and swapping it.

The state of the board is solved if and only if the board is [[1,2,3],[4,5,0]].

Given the puzzle board board, return the least number of moves required so that the
state of the board is solved. If it is impossible for the state of the board to be
solved, return -1.


Example 1:

Input: board = [[1,2,3],[4,0,5]]
Output: 1
Explanation: Swap the 0 and the 5 in one move.

Example 2:

Input: board = [[1,2,3],[5,4,0]]
Output: -1
Explanation: No number of moves will make the board solved.

Example 3:

Input: board = [[4,1,2],[5,0,3]]
Output: 5
Explanation: 5 is the smallest number of moves that solves the board.
An example path:
After move 0: [[4,1,2],[5,0,3]]
After move 1: [[4,1,2],[0,5,3]]
After move 2: [[0,1,2],[4,5,3]]
After move 3: [[1,0,2],[4,5,3]]
After move 4: [[1,2,0],[4,5,3]]
After move 5: [[1,2,3],[4,5,0]]


Constraints:

board.length == 2
board[i].length == 3
0 <= board[i][j] <= 5
Each value board[i][j] is unique.

"""

from collections import deque


# V0
# IDEA : BFS ON FLATTENED BOARD STATES
#
#   Flatten the 2x3 board into a 6-char string so a whole state is one hashable key:
#       [[1,2,3],[4,5,0]] -> "123450" (the goal)
#   BFS from the start state gives the minimum number of moves; the search space is
#   only 6! = 720 states, half of which are unreachable (parity) -> return -1 then.
#
#   NEIGHBORS[i] lists the flat indices adjacent to index i on the 2x3 grid:
#       0 1 2
#       3 4 5
#
# time = O(6! * 6)  -> effectively O(1), the state space is fixed
# space = O(6!)
class Solution(object):
    def slidingPuzzle(self, board):
        target = "123450"
        start = "".join(str(v) for row in board for v in row)
        if start == target:
            return 0

        NEIGHBORS = {
            0: (1, 3),
            1: (0, 2, 4),
            2: (1, 5),
            3: (0, 4),
            4: (1, 3, 5),
            5: (2, 4),
        }

        seen = {start}
        queue = deque([(start, 0)])

        while queue:
            state, steps = queue.popleft()
            zero = state.index("0")

            for nxt in NEIGHBORS[zero]:
                chars = list(state)
                chars[zero], chars[nxt] = chars[nxt], chars[zero]
                new_state = "".join(chars)

                if new_state == target:
                    return steps + 1
                if new_state not in seen:
                    seen.add(new_state)
                    queue.append((new_state, steps + 1))

        return -1
