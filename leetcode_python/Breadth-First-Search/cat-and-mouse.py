"""

913. Cat and Mouse
Hard

A game on an undirected graph is played by two players, Mouse and Cat,
who alternate turns.

The graph is given as follows: graph[a] is a list of all nodes b such that ab is an
edge of the graph.

The mouse starts at node 1 and goes first, the cat starts at node 2 and goes second,
and there is a hole at node 0.

During each player's turn, they must travel along one edge of the graph that meets
where they are. For example, if the Mouse is at node 1, it must travel to any node
in graph[1].

Additionally, it is not allowed for the Cat to travel to the Hole (node 0).

Then, the game can end in three ways:

If ever the Cat occupies the same node as the Mouse, the Cat wins.
If ever the Mouse reaches the Hole, the Mouse wins.
If ever a position is repeated (i.e., the players are in the same position as a
previous turn, and it is the same player's turn to move), the game is a draw.

Given a graph, and assuming both players play optimally, return

1 if the mouse wins the game,
2 if the cat wins the game, or
0 if the game is a draw.


Example 1:

Input: graph = [[2,5],[3],[0,4,5],[1,4,5],[2,3],[0,2,3]]
Output: 0

Example 2:

Input: graph = [[1,3],[0],[3],[0,2]]
Output: 1


Constraints:

3 <= graph.length <= 50
1 <= graph[i].length < graph.length
0 <= graph[i][j] < graph.length
graph[i][j] != i
graph[i] is unique.
The mouse and the cat can always move.

"""

# V0
# IDEA : RETROGRADE ANALYSIS (BFS / topological sort from the terminal states)
"""
 A game state is (mouse_pos, cat_pos, whose_turn). There are only n * n * 2 of them.

 Terminal (known) states:
    - mouse at the hole (0)        -> MOUSE_WIN
    - cat and mouse on same node   -> CAT_WIN

 We propagate BACKWARDS from those known states. For a predecessor state where
 player P is to move:

    - if the known child is a WIN for P    -> P just picks that move -> state is a win for P
    - otherwise                            -> that move is bad for P; decrement its
                                              "degree" (number of untried moves).
                                              When degree hits 0, EVERY move loses,
                                              so the state takes the opponent's result.

 Anything still unlabelled when the queue empties is a DRAW.

 NOTE: the cat can never step into the hole, so the cat's move count for a node
       adjacent to node 0 is one less.
"""
# time = O(n^3)
# space = O(n^2)
from collections import deque
class Solution(object):
    def catMouseGame(self, graph):
        n = len(graph)
        HOLE = 0
        MOUSE_TURN, CAT_TURN = 0, 1
        DRAW, MOUSE_WIN, CAT_WIN = 0, 1, 2

        # result[m][c][t], degree[m][c][t]
        result = [[[DRAW, DRAW] for _ in range(n)] for _ in range(n)]
        degree = [[[0, 0] for _ in range(n)] for _ in range(n)]

        for m in range(n):
            for c in range(1, n):
                degree[m][c][MOUSE_TURN] = len(graph[m])
                degree[m][c][CAT_TURN] = len(graph[c])
            # the cat is not allowed to enter the hole -> drop that option
            for c in graph[HOLE]:
                degree[m][c][CAT_TURN] -= 1

        def prev_states(m, c, t):
            """states that could have led to (m, c, t) in one move"""
            pt = t ^ 1  # the player that moved last
            if pt == CAT_TURN:
                # the cat moved into c, so it came from a neighbour of c (never the hole)
                for pc in graph[c]:
                    if pc != HOLE:
                        yield (m, pc, pt)
            else:
                # the mouse moved into m, so it came from a neighbour of m
                for pm in graph[m]:
                    yield (pm, c, pt)

        q = deque()

        # terminal: mouse reached the hole
        for c in range(1, n):
            result[0][c][MOUSE_TURN] = MOUSE_WIN
            result[0][c][CAT_TURN] = MOUSE_WIN
            q.append((0, c, MOUSE_TURN))
            q.append((0, c, CAT_TURN))

        # terminal: cat caught the mouse
        for i in range(1, n):
            result[i][i][MOUSE_TURN] = CAT_WIN
            result[i][i][CAT_TURN] = CAT_WIN
            q.append((i, i, MOUSE_TURN))
            q.append((i, i, CAT_TURN))

        while q:
            m, c, t = q.popleft()
            outcome = result[m][c][t]
            for pm, pc, pt in prev_states(m, c, t):
                if result[pm][pc][pt] != DRAW:
                    continue  # already decided
                mover_wins = (outcome == MOUSE_WIN and pt == MOUSE_TURN) or \
                             (outcome == CAT_WIN and pt == CAT_TURN)
                if mover_wins:
                    # the moving player can force this outcome
                    result[pm][pc][pt] = outcome
                    q.append((pm, pc, pt))
                else:
                    degree[pm][pc][pt] -= 1
                    if degree[pm][pc][pt] == 0:
                        # every single move loses -> forced
                        result[pm][pc][pt] = outcome
                        q.append((pm, pc, pt))

        # mouse starts at 1, cat starts at 2, mouse moves first
        return result[1][2][MOUSE_TURN]
