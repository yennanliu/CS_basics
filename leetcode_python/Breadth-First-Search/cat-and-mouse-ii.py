"""

1728. Cat and Mouse II
Hard

A game is played by a cat and a mouse named Cat and Mouse.

The environment is represented by a grid of size rows x cols, where each element is a wall, floor, player (Cat, Mouse), or food.

Players are represented by the characters 'C'(Cat),'M'(Mouse).
Floors are represented by the character '.' and can be walked on.
Walls are represented by the character '#' and cannot be walked on.
Food is represented by the character 'F' and can be walked on.
There is only one of each character 'C', 'M', and 'F' in grid.

Mouse and Cat play according to the following rules:

Mouse moves first, then they take turns to move.
During each turn, Cat and Mouse can jump in one of the four directions (left, right, up, down). They cannot jump over the wall nor outside of the grid.
catJump, mouseJump are the maximum lengths Cat and Mouse can jump at a time, respectively. Cat and Mouse can jump less than the maximum length.
Staying in the same position is allowed.
Mouse can jump over Cat.

The game can end in 4 ways:

If Cat occupies the same position as Mouse, Cat wins.
If Cat reaches the food first, Cat wins.
If Mouse reaches the food first, Mouse wins.
If Mouse cannot get to the food within 1000 turns, Cat wins.

Given a rows x cols matrix grid and two integers catJump and mouseJump, return true if Mouse can win the game if both Cat and Mouse play optimally, otherwise return false.


Example 1:

Input: grid = ["####F","#C...","M...."], catJump = 1, mouseJump = 2
Output: true
Explanation: Cat cannot catch Mouse on its turn nor can it get the food before Mouse.

Example 2:

Input: grid = ["M.C...F"], catJump = 1, mouseJump = 4
Output: true

Example 3:

Input: grid = ["M.C...F"], catJump = 1, mouseJump = 3
Output: false


Constraints:

rows == grid.length
cols = grid[i].length
1 <= rows, cols <= 8
grid[i][j] consist only of characters 'C', 'M', 'F', '.', and '#'.
There is only one of each character 'C', 'M', and 'F' in grid.
1 <= catJump, mouseJump <= 8

"""

# V0
# IDEA : GAME THEORY, RETROGRADE ANALYSIS (BFS backwards from terminal states)
#
#   state = (mouse cell, cat cell, whose turn). at most 64 * 64 * 2 states,
#   so we can solve the whole game - but forward minimax loops forever on
#   cycles, hence we propagate BACKWARDS from the known endings instead.
#
#   terminal states (1 = mouse wins, 2 = cat wins):
#     mouse on food (cat to move)  -> 1
#     cat on food   (mouse to move)-> 2
#     both on the same cell        -> 2
#
#   BFS on the reversed move graph. for a predecessor state whose mover is
#   `pt` and a resolved successor with winner `t`:
#     - pt is the winner  -> that player just picks this move : resolved.
#       (encoded as `pt == t - 1`, since turn 0 = mouse pairs with result 1)
#     - pt is the loser   -> only resolved once EVERY option is a loss, so
#       decrement degree[pt-state] and resolve when it hits 0.
#
#   NOTE : the move graph is symmetric (an unobstructed jump works both
#          ways), so g_cat[c] doubles as the list of cells the cat could
#          have jumped FROM. same for g_mouse.
#   NOTE : states still unresolved at the end are draws-by-timeout, which
#          the rules score as a cat win - returning `== 1` handles that.
#
# time = O((mn)^2 * (m + n)), space = O((mn)^2)
from collections import deque
class Solution(object):
    def canMouseWin(self, grid, catJump, mouseJump):
        m, n = len(grid), len(grid[0])
        N = m * n
        dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]

        g_mouse = [[] for _ in range(N)]
        g_cat = [[] for _ in range(N)]
        mouse_start = cat_start = food = 0

        for i in range(m):
            for j in range(n):
                c = grid[i][j]
                if c == '#':
                    continue
                v = i * n + j
                if c == 'C':
                    cat_start = v
                elif c == 'M':
                    mouse_start = v
                elif c == 'F':
                    food = v
                for a, b in dirs:
                    for jump, g in ((mouseJump, g_mouse), (catJump, g_cat)):
                        for k in range(jump + 1):
                            x, y = i + k * a, j + k * b
                            if not (0 <= x < m and 0 <= y < n) or grid[x][y] == '#':
                                break
                            g[v].append(x * n + y)

        # res[mouse][cat][turn] : 0 unknown, 1 mouse wins, 2 cat wins
        res = [[[0, 0] for _ in range(N)] for _ in range(N)]
        degree = [[[len(g_mouse[i]), len(g_cat[j])] for j in range(N)]
                  for i in range(N)]

        q = deque()
        for i in range(N):
            res[food][i][1] = 1
            res[i][food][0] = 2
            res[i][i][0] = res[i][i][1] = 2
            q.append((food, i, 1))
            q.append((i, food, 0))
            q.append((i, i, 0))
            q.append((i, i, 1))

        while q:
            mo, ca, t = q.popleft()
            winner = res[mo][ca][t]
            pt = t ^ 1

            if pt == 1:
                prevs = [(mo, pc, 1) for pc in g_cat[ca] if res[mo][pc][1] == 0]
            else:
                prevs = [(pm, ca, 0) for pm in g_mouse[mo] if res[pm][ca][0] == 0]

            for pm, pc, ppt in prevs:
                if ppt == winner - 1:
                    res[pm][pc][ppt] = winner
                    q.append((pm, pc, ppt))
                else:
                    degree[pm][pc][ppt] -= 1
                    if degree[pm][pc][ppt] == 0:
                        res[pm][pc][ppt] = winner
                        q.append((pm, pc, ppt))

        return res[mouse_start][cat_start][0] == 1
