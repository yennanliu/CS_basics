"""

2075. Decode the Slanted Ciphertext
Medium

A string originalText is encoded using a slanted transposition cipher to a string encodedText with the help of a matrix having a fixed number of rows rows.

originalText is placed first in a top-left to bottom-right manner.

The blue cells are filled first, followed by the red cells, then the yellow cells, and so on, until we reach the end of originalText. The arrow indicates the order in which the cells are filled. All empty cells are filled with ' '. The number of columns is chosen such that the rightmost column will not be empty after filling in originalText.

encodedText is then formed by appending all characters of the matrix in a row-wise fashion.

The characters in the blue cells are appended first to encodedText, then the red cells, and so on, and finally the yellow cells. The arrow indicates the order in which the cells are accessed.

For example, if originalText = "cipher" and rows = 3, then we encode it in the following manner:

The blue arrows depict how originalText is placed in the matrix, and the red arrows denote the order in which encodedText is formed. In the above example, encodedText = "ch ie pr".

Given the encoded string encodedText and number of rows rows, return the original string originalText.

Note: originalText cannot have any trailing spaces ' '. The test cases are generated such that there is only one possible originalText.


Example 1:

Input: encodedText = "ch   ie   pr", rows = 3
Output: "cipher"
Explanation: This is the same example described in the problem description.

Example 2:

Input: encodedText = "iveo    eed   l te   olc", rows = 4
Output: "i love leetcode"
Explanation: The following image shows the matrix that was used to encode originalText.
The blue arrows show how we can find originalText from encodedText.

Example 3:

Input: encodedText = "coding", rows = 1
Output: "coding"
Explanation: Since there is only 1 row, both originalText and encodedText are the same.


Constraints:

0 <= encodedText.length <= 10^6
encodedText consists of lowercase English letters and ' ' only.
encodedText is a valid encoding of some originalText that does not have trailing spaces.
1 <= rows <= 1000
The test cases are generated such that there is only one possible originalText.

"""

# V0
# IDEA : READ THE MATRIX BACK ALONG ITS DIAGONALS
#
#   the matrix is rows x cols with  cols = len(encodedText) // rows, laid out
#   row-wise in encodedText. the original text was written along the
#   diagonals, so reading it back means, for each start column c :
#       encodedText[0*cols + c], encodedText[1*cols + c+1], ... until the
#       column index runs past cols
#
#   finally strip the padding: originalText has no TRAILING spaces (interior
#   spaces are real, e.g. "i love leetcode"), so a single rstrip is correct.
#
#   NOTE : rows == 1 (or an empty input) falls out of the same loop.
#
# time = O(len(encodedText)), space = O(len(encodedText))
class Solution(object):
    def decodeCiphertext(self, encodedText, rows):
        if rows == 0 or not encodedText:
            return ""
        cols = len(encodedText) // rows
        out = []
        for c in range(cols):
            r, cc = 0, c
            while r < rows and cc < cols:
                out.append(encodedText[r * cols + cc])
                r += 1
                cc += 1
        return ''.join(out).rstrip()


# V0-1
# IDEA : INVERSE INDEX MAP (SCATTER) + PREFIX SUMS
#
#   instead of GATHERING the answer diagonal by diagonal, push every
#   encoded character straight to the slot it occupies in originalText.
#
#   the diagonal starting at column d holds min(rows, cols - d) characters,
#   so start[d] = sum of the earlier diagonal lengths tells where diagonal d
#   begins inside originalText.
#   encoded position p sits at (r, c) = divmod(p, cols) and belongs to
#   diagonal d = c - r at offset r  ->  originalText[start[c - r] + r] = ch.
#   cells with c < r are the padding the encoder wrote, so they are skipped.
#
# time = O(len(encodedText)), space = O(len(encodedText))
class Solution(object):
    def decodeCiphertext(self, encodedText, rows):
        if rows == 0 or not encodedText:
            return ""
        cols = len(encodedText) // rows
        start = [0] * cols
        total = 0
        for d in range(cols):
            start[d] = total
            total += min(rows, cols - d)

        out = [' '] * total
        for p, ch in enumerate(encodedText):
            r, c = divmod(p, cols)
            if c >= r:
                out[start[c - r] + r] = ch
        return ''.join(out).rstrip()


# V0-2
# IDEA : A DIAGONAL IS AN ARITHMETIC PROGRESSION -> STRIDED SLICING
#
#   moving one step down-right in the matrix moves cols + 1 characters
#   forward in the flat encodedText, so the whole diagonal starting at
#   column c is just the slice encodedText[c :: cols + 1].
#   that slice runs off the bottom of the matrix (it wraps into later rows'
#   earlier columns), so cut it at its true length min(rows, cols - c).
#
#   no per-character indexing at all: cols slices, then one join.
#
# time = O(len(encodedText)), space = O(len(encodedText))
class Solution(object):
    def decodeCiphertext(self, encodedText, rows):
        if rows == 0 or not encodedText:
            return ""
        cols = len(encodedText) // rows
        step = cols + 1
        out = []
        for c in range(cols):
            out.append(encodedText[c::step][:min(rows, cols - c)])
        return ''.join(out).rstrip()
