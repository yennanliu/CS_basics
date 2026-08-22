"""

3078. Match Alphanumerical Pattern in Matrix I
Medium
🔒 (premium)

You are given a 2D integer matrix board and a 2D character matrix pattern. Where 0 <= board[r][c] <= 9 and each element of pattern is either a digit or a lowercase English letter.

Your task is to find a submatrix of board that matches pattern.

An integer matrix part matches pattern if we can replace cells containing letters in pattern with some digits (each distinct letter with a unique digit) in such a way that the resulting matrix equals part. In other words,

The matrices have identical dimensions.
If pattern[r][c] is a digit, then part[r][c] must be the same digit.
If pattern[r][c] is a letter x:
    If there is another cell (r', c') in pattern with pattern[r'][c'] == x, then part[r][c] must equal part[r'][c'].
    Distinct letters must map to distinct digits, i.e. if pattern[r][c] != pattern[r'][c'] and both are letters, then part[r][c] != part[r'][c'].

Return an array of length 2 containing the row number and column number of the upper-left corner of the submatrix of board that matches pattern, or [-1, -1] if there is no such submatrix.

If there are multiple such submatrices, return the coordinates of the upper-left corner of the topmost one, and if there are multiple such rows, return the leftmost one.


Example 1:

Input: board = [[1,2,2],[2,2,3],[2,3,3]], pattern = ["ab","bb"]
Output: [0,0]
Explanation: If we consider this mapping: "a" -> 1 and "b" -> 2; the submatrix with the upper-left corner (0,0) is a match as outlined in the matrix above.
Note that the submatrix with the upper-left corner (1,1) is also a match but since it comes after the other one, we return [0,0].

Example 2:

Input: board = [[1,1,2],[3,3,4],[6,6,6]], pattern = ["ab","66"]
Output: [1,1]
Explanation: If we consider this mapping: "a" -> 3 and "b" -> 4; the submatrix with the upper-left corner (1,1) is a match as outlined in the matrix above.
Note that the submatrix with the upper-left corner (0,1) is not a match because the mapping is not one-to-one.

Example 3:

Input: board = [[1,2],[2,1]], pattern = ["22"]
Output: [-1,-1]
Explanation: The pattern requires two adjacent equal cells with the value 2, which board does not have.


Constraints:

1 <= board.length <= 50
1 <= board[i].length <= 50
0 <= board[i][j] <= 9
1 <= pattern.length <= 50
1 <= pattern[i].length <= 50
pattern[i][j] is either a digit represented as a character or a lowercase English letter.

"""

# V0
# IDEA : TRY EVERY ANCHOR, VERIFY WITH A *BIJECTIVE* LETTER -> DIGIT MAP
#
#   50x50 against 50x50 is at most 6.25 * 10^6 cell comparisons, so the
#   direct anchor sweep is affordable — and scanning rows then columns
#   naturally yields the topmost-then-leftmost answer first.
#
#   the verification is where the care goes. digits in the pattern must match
#   literally; letters need a consistent assignment that is ONE-TO-ONE in
#   both directions, so keep two maps :
#       letter -> digit   (the same letter always means the same digit)
#       digit -> letter   (two different letters may not claim one digit)
#   both are rebuilt per anchor, which is what makes the check local.
#
# time = O(m * n * pm * pn), space = O(1)  (at most 10 letters can map)
class Solution(object):
    def findPattern(self, board, pattern):
        m, n = len(board), len(board[0])
        pm, pn = len(pattern), len(pattern[0])

        def matches(r0, c0):
            letter_to_digit = {}
            digit_to_letter = {}
            for r in range(pm):
                for c in range(pn):
                    p = pattern[r][c]
                    v = board[r0 + r][c0 + c]
                    if p.isdigit():
                        if v != int(p):
                            return False
                        continue
                    if p in letter_to_digit:
                        if letter_to_digit[p] != v:
                            return False
                    elif v in digit_to_letter:
                        return False            # digit already taken by another letter
                    else:
                        letter_to_digit[p] = v
                        digit_to_letter[v] = p
            return True

        for r0 in range(m - pm + 1):
            for c0 in range(n - pn + 1):
                if matches(r0, c0):
                    return [r0, c0]
        return [-1, -1]


# V0-1
# IDEA : PRECOMPUTE THE PATTERN'S CONSTRAINT GROUPS ONCE
#
#   the pattern says three separate things, and all three can be prepared
#   before any anchor is examined :
#       digit cells    -> (r, c, required digit)
#       letter groups  -> every cell of one letter must hold the same value
#       group leaders  -> the leaders' values must be pairwise distinct
#   an anchor is then validated by checking those three lists, cheapest
#   first, instead of rebuilding two dictionaries cell by cell. the digit
#   list usually rejects an anchor immediately.
#
# time = O(m * n * pm * pn), space = O(pm * pn)
class Solution(object):
    def findPattern(self, board, pattern):
        m, n = len(board), len(board[0])
        pm, pn = len(pattern), len(pattern[0])

        digit_cells = []
        groups = {}
        for r in range(pm):
            for c in range(pn):
                ch = pattern[r][c]
                if ch.isdigit():
                    digit_cells.append((r, c, int(ch)))
                else:
                    groups.setdefault(ch, []).append((r, c))
        group_cells = list(groups.values())
        leaders = [cells[0] for cells in group_cells]

        for r0 in range(m - pm + 1):
            for c0 in range(n - pn + 1):
                if any(board[r0 + r][c0 + c] != d for r, c, d in digit_cells):
                    continue
                ok = True
                for cells in group_cells:
                    lr, lc = cells[0]
                    v = board[r0 + lr][c0 + lc]
                    if any(board[r0 + r][c0 + c] != v for r, c in cells[1:]):
                        ok = False
                        break
                if not ok:
                    continue
                vals = [board[r0 + r][c0 + c] for r, c in leaders]
                if len(set(vals)) == len(vals):
                    return [r0, c0]
        return [-1, -1]


# V0-2
# IDEA : RECURSIVE ROW-BY-ROW VERIFICATION WITH BACKTRACKING
#
#   same anchor sweep, but the per-anchor check is a recursion over pattern
#   ROWS (depth <= 50, so no recursion-limit trouble even for a 50x50
#   pattern). it carries ONE shared letter -> digit dict plus a 10-bit mask
#   of digits already claimed : a letter is bound the first time it is seen
#   and every binding made inside a row is UNDONE when that row's subtree
#   fails, so nothing is allocated per anchor.
#   the taken-digit mask is what enforces injectivity, replacing the reverse
#   digit -> letter dictionary of V0.
#
# time = O(m * n * pm * pn), space = O(pm)   (recursion depth)
class Solution(object):
    def findPattern(self, board, pattern):
        m, n = len(board), len(board[0])
        pm, pn = len(pattern), len(pattern[0])
        bound = {}

        def dfs(r, r0, c0, used):
            if r == pm:
                return True
            fresh = []
            for c in range(pn):
                ch = pattern[r][c]
                v = board[r0 + r][c0 + c]
                if ch.isdigit():
                    if v != int(ch):
                        break
                elif ch in bound:
                    if bound[ch] != v:
                        break
                elif used >> v & 1:
                    break
                else:
                    bound[ch] = v
                    used |= 1 << v
                    fresh.append(ch)
            else:
                if dfs(r + 1, r0, c0, used):
                    return True
            for ch in fresh:
                del bound[ch]
            return False

        for r0 in range(m - pm + 1):
            for c0 in range(n - pn + 1):
                bound.clear()
                if dfs(0, r0, c0, 0):
                    return [r0, c0]
        return [-1, -1]
