"""

2018. Check if Word Can Be Placed In Crossword
Medium

You are given an m x n matrix board, representing the current state of a crossword puzzle. The crossword contains lowercase English letters (from solved words), ' ' to represent any empty cells, and '#' to represent any blocked cells.

A word can be placed horizontally (left to right or right to left) or vertically (top to bottom or bottom to top) in the board if:

It does not occupy a cell containing the character '#'.
The cell each letter is placed in must either be ' ' (empty) or match the letter already on the board.
There must not be any empty cells ' ' or other lowercase letters directly left or right of the word if the word was placed horizontally.
There must not be any empty cells ' ' or other lowercase letters directly above or below the word if the word was placed vertically.

Given a string word, return true if word can be placed in board, or false otherwise.


Example 1:

Input: board = [["#", " ", "#"], [" ", " ", "#"], ["#", "c", " "]], word = "abc"
Output: true
Explanation: The word "abc" can be placed as shown above (top to bottom).

Example 2:

Input: board = [[" ", "#", "a"], [" ", "#", "c"], [" ", "#", "a"]], word = "ac"
Output: false
Explanation: It is impossible to place the word because there will always be a space/letter above or below it.

Example 3:

Input: board = [["#", " ", "#"], [" ", " ", "#"], ["#", " ", "c"]], word = "ca"
Output: true
Explanation: The word "ca" can be placed as shown above (right to left).


Constraints:

m == board.length
n == board[i].length
1 <= m * n <= 2 * 10^5
board[i][j] will be ' ', '#', or a lowercase English letter.
1 <= word.length <= max(m, n)
word will contain only lowercase English letters.

"""

# V0
# IDEA : SPLIT EVERY ROW / COLUMN INTO '#'-FREE SLOTS, THEN MATCH
#
#   a placement is legal iff the word exactly fills one maximal run of
#   non-'#' cells (the "no empty cell / letter directly before or after"
#   rules are precisely "the run length equals len(word)").
#
#   so : cut each row and each column on '#', keep the segments whose length
#   == len(word), and test the word forwards and backwards against it
#   ( ' ' is a wildcard, a letter must match exactly ).
#
#   NOTE : columns are handled by transposing, which also covers the
#          bottom-to-top direction via the reversed match.
#
# time = O(m * n), space = O(max(m, n))
class Solution(object):
    def placeWordInCrossword(self, board, word):
        def fits(seg, w):
            # seg and w have equal length here
            return all(c == ' ' or c == t for c, t in zip(seg, w))

        n = len(word)

        def scan(rows):
            for row in rows:
                for seg in ''.join(row).split('#'):
                    if len(seg) == n and (fits(seg, word) or fits(seg, word[::-1])):
                        return True
            return False

        return scan(board) or scan(zip(*board))


# V0-1
# IDEA : BRUTE FORCE - TRY THE WORD AT EVERY CELL IN ALL 4 DIRECTIONS
#
#   lay the word down starting at (r, c) walking in one of the 4 directions and
#   verify three things explicitly :
#     - every covered cell is ' ' or already the matching letter (never '#')
#     - the cell just BEFORE the start is off board or '#'
#     - the cell just AFTER  the end   is off board or '#'
#   the last two encode "no space / stray letter may touch the word", i.e. the
#   same condition V0 gets for free from exact segment length.
#
# time = O(m * n * L) with L = len(word)
# space = O(1)
class Solution(object):
    def placeWordInCrossword(self, board, word):
        m, n = len(board), len(board[0])
        L = len(word)

        def wall(r, c):
            # off board counts as a wall
            return not (0 <= r < m and 0 <= c < n) or board[r][c] == '#'

        for r in range(m):
            for c in range(n):
                for dr, dc in ((0, 1), (0, -1), (1, 0), (-1, 0)):
                    if not wall(r - dr, c - dc):
                        continue
                    er, ec = r + dr * (L - 1), c + dc * (L - 1)
                    if not wall(er + dr, ec + dc):
                        continue
                    ok = True
                    for k in range(L):
                        rr, cc = r + dr * k, c + dc * k
                        if not (0 <= rr < m and 0 <= cc < n):
                            ok = False
                            break
                        ch = board[rr][cc]
                        if ch != ' ' and ch != word[k]:
                            ok = False
                            break
                    if ok:
                        return True
        return False


# V0-2
# IDEA : COMPILE THE WORD INTO A REGEX, RUN IT ON '#'-PADDED LINES
#
#   a letter of the word may land on ' ' or on that same letter, which is the
#   character class [ x]. So the whole placement is one pattern :
#       '#' + [ w0] + [ w1] + ... + '#'
#   the two literal '#' are the walls, and padding each line with '#' at both
#   ends turns "off board" into a wall too, so there is zero index arithmetic.
#   Run it on every row and every column (zip(*board)), for the word and for
#   its reverse - the reverse covers right-to-left / bottom-to-top.
#
# time = O(m * n * L)
# space = O(max(m, n))
class Solution(object):
    def placeWordInCrossword(self, board, word):
        import re
        fwd = re.compile('#' + ''.join('[ %s]' % ch for ch in word) + '#')
        bwd = re.compile('#' + ''.join('[ %s]' % ch for ch in reversed(word))
                         + '#')

        def scan(rows):
            for row in rows:
                s = '#' + ''.join(row) + '#'
                if fwd.search(s) or bwd.search(s):
                    return True
            return False

        return scan(board) or scan(zip(*board))
