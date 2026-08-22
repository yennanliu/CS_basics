"""

1138. Alphabet Board Path
Medium

On an alphabet board, we start at position (0, 0), corresponding to character board[0][0].

Here, board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"].

We may make the following moves:

'U' moves our position up one row, if the position exists on the board;
'D' moves our position down one row, if the position exists on the board;
'L' moves our position left one column, if the position exists on the board;
'R' moves our position right one column, if the position exists on the board;
'!' adds the character board[r][c] at our current position (r, c) to the answer.

(Here, the only positions that exist on the board are positions with letters on them.)

Return a sequence of moves that makes our answer equal to target in the minimum number
of moves. You may return any path that does so.


Example 1:

Input: target = "leet"
Output: "DDR!UURRR!!DDD!"

Example 2:

Input: target = "code"
Output: "RR!DDRR!UUL!R!"


Constraints:

1 <= target.length <= 100
target consists only of English lowercase letters.

"""

# V0
# IDEA : SIMULATION + move ORDER trick (handle the lonely 'z')
#        letter c sits at row = (c - 'a') // 5, col = (c - 'a') % 5
#        the only "hole" is (5,1)..(5,4), i.e. row 5 only has column 0 ('z')
#        -> emit moves in order  U -> L -> R -> D
#           * go UP  before going RIGHT  => leaving 'z' is always legal
#           * go LEFT before going DOWN  => entering 'z' is always legal
# time = O(n * (rows + cols)), n = len(target)
# space = O(n * (rows + cols)), for the output string
class Solution(object):
    def alphabetBoardPath(self, target):
        res = []
        r, c = 0, 0
        for ch in target:
            nr, nc = divmod(ord(ch) - ord('a'), 5)
            # NOTE !!! U / L must come before D / R (because of 'z' at (5,0))
            if nr < r:
                res.append('U' * (r - nr))
            if nc < c:
                res.append('L' * (c - nc))
            if nc > c:
                res.append('R' * (nc - c))
            if nr > r:
                res.append('D' * (nr - r))
            res.append('!')
            r, c = nr, nc
        return "".join(res)


# V0-1
# IDEA : BFS SHORTEST PATH ON THE BOARD GRAPH
#
#   treat the 26 cells as nodes and the legal single steps as edges, then BFS
#   from the current cell to the next letter's cell, remembering which move
#   reached each node so the path can be rebuilt. no reasoning about 'z' is
#   needed at all — the graph simply has no edge into the missing cells.
#
# time = O(n * R * C), n = len(target), one BFS over 26 cells per letter
# space = O(R * C) for the BFS bookkeeping (plus the O(n * (R + C)) output)
class Solution(object):
    def alphabetBoardPath(self, target):
        import collections
        board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"]
        cells = {(r, c) for r, row in enumerate(board) for c in range(len(row))}
        moves = {'U': (-1, 0), 'D': (1, 0), 'L': (0, -1), 'R': (0, 1)}

        def bfs(src, dst):
            if src == dst:
                return ""
            prev = {src: None}
            q = collections.deque([src])
            while q:
                cur = q.popleft()
                for mv, (dr, dc) in moves.items():
                    step = (cur[0] + dr, cur[1] + dc)
                    if step not in cells or step in prev:
                        continue
                    prev[step] = (cur, mv)
                    if step == dst:
                        out = []
                        node = step
                        while prev[node] is not None:
                            node, took = prev[node]
                            out.append(took)
                        return "".join(reversed(out))
                    q.append(step)
            return ""

        res = []
        cur = (0, 0)
        for ch in target:
            dst = divmod(ord(ch) - ord('a'), 5)
            res.append(bfs(cur, dst))
            res.append('!')
            cur = dst
        return "".join(res)


# V0-2
# IDEA : ROUTE THROUGH 'u' WHENEVER 'z' IS AN ENDPOINT
#
#   with 'z' taken out, the board is a full 5x5 rectangle where any two cells
#   can be joined by a plain vertical-then-horizontal walk in either order. so
#   deal with 'z' by splitting the trip at its only neighbour 'u' = (4, 0):
#       leaving  'z' : emit 'U' first, then walk from (4, 0)
#       entering 'z' : walk to (4, 0) first, then emit 'D'
#   both legs stay the same total length as the Manhattan distance, so the
#   path is still minimal.
#
# time = O(n * (rows + cols))
# space = O(n * (rows + cols)) for the output string
class Solution(object):
    def alphabetBoardPath(self, target):
        def walk(src, dst):
            # both cells live inside the 5x5 block -> move order is free
            r, c = src
            nr, nc = dst
            out = 'D' * (nr - r) if nr > r else 'U' * (r - nr)
            out += 'R' * (nc - c) if nc > c else 'L' * (c - nc)
            return out

        Z = (5, 0)
        U = (4, 0)
        res = []
        cur = (0, 0)
        for ch in target:
            dst = divmod(ord(ch) - ord('a'), 5)
            if cur == Z and dst == Z:
                pass
            elif cur == Z:
                res.append('U' + walk(U, dst))
            elif dst == Z:
                res.append(walk(cur, U) + 'D')
            else:
                res.append(walk(cur, dst))
            res.append('!')
            cur = dst
        return "".join(res)
