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
