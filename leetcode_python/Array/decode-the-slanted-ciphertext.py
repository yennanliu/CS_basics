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


# V0-1
# IDEA: MATRIX + ARRAY OP (gpt)
class Solution(object):
    def decodeCiphertext(self, encodedText, rows):
        """
        :type encodedText: str
        :type rows: int
        :rtype: str
        """
        # Edge case
        if not encodedText:
            return ""

        if rows == 1:
            return encodedText

        # Get matrix width.
        width = len(encodedText) // rows

        # Build matrix.
        matrix = [[""] * width for _ in range(rows)]

        for i in range(len(encodedText)):
            x = i % width
            y = i // width
            matrix[y][x] = encodedText[i]

        # Prepare result.
        res = []

        # Read the matrix diagonally:
        #
        # (0, 0) -> (1, 1) -> (2, 2)
        #
        # (0, 1) -> (1, 2) -> (2, 3)
        #
        # (0, 2) -> (1, 3) -> (2, 4)
        #
        # Start from each column in the first row.
        for start_x in range(width):
            y = 0
            x = start_x

            # Move down + right.
            while y < rows and x < width:
                res.append(matrix[y][x])

                y += 1
                x += 1

        # Remove trailing spaces.
        return "".join(res).rstrip()


# V0-2
# IDEA: ARRAY OP (gemini)
class Solution(object):

  def decodeCiphertext(self, encodedText, rows):
    """:type encodedText: str

    :type rows: int
    :rtype: str
    """
    if not encodedText or rows == 1:
      return encodedText

    cols = len(encodedText) // rows
    res = []

    # 對角線起點由第 0 列的每一欄 c (0 ~ cols-1) 開始
    for c in range(cols):
      # 沿著斜對角線向下移動：(r, c + r)
      for r in range(rows):
        if c + r < cols:
          # 計算 2D 座標 (r, c + r) 在一維字串中的索引
          idx = r * cols + (c + r)
          res.append(encodedText[idx])
        else:
          break  # 超出右邊界，結束當前對角線

    # 組合字串並去除尾隨空格
    return "".join(res).rstrip()


# V0
# IDEA : REBUILD THE MATRIX SHAPE, THEN READ IT BACK ALONG ITS DIAGONALS
#
#   encodedText is the rows x cols matrix flattened row by row, so
#       cols = len(encodedText) // rows   and   cell (r, c) = encodedText[r * cols + c]
#   originalText was written diagonal by diagonal (top-left -> bottom-right),
#   starting from column 0, then column 1, ... -- so walking each diagonal in
#   start-column order gives the characters back in their original order.
#
#   e.g. "ch   ie   pr", rows = 3 -> cols = 4
#        c h _ _        diag from col 0 : c i p
#        _ i e _        diag from col 1 : h e r
#        _ _ p r        diag from col 2 : _ _ / col 3 : _
#        -> "cipher  " -> strip trailing padding -> "cipher"
#
#   the padding cells are spaces, and originalText has NO trailing spaces, so
#   one rstrip removes exactly the padding (interior spaces are real text).
#
# time = O(n), space = O(n)   (n = len(encodedText))
class Solution(object):
    def decodeCiphertext(self, encodedText, rows):
        """
        :type encodedText: str
        :type rows: int
        :rtype: str
        """
        # edge case: nothing was encoded
        if not encodedText:
            return ""

        cols = len(encodedText) // rows

        decoded_chars = []
        # each start column begins one diagonal of the original text
        for start_col in range(cols):
            row = 0
            col = start_col
            # walk down-right until we fall off the bottom or the right edge
            while row < rows and col < cols:
                decoded_chars.append(encodedText[row * cols + col])
                row += 1
                col += 1

        decoded = "".join(decoded_chars)

        # NOTE !!! strip only the TRAILING padding -- interior spaces are part
        #          of the original text (e.g. "i love leetcode")
        return decoded.rstrip()


# V1
# IDEA : SCATTER -- SEND EACH ENCODED CHARACTER STRAIGHT TO ITS FINAL SLOT
#
#   V0 GATHERS the answer diagonal by diagonal. this goes the other way: one
#   left-to-right pass over encodedText, writing every character directly
#   into its position in the answer.
#
#   cell (r, c) lies on the diagonal that started at column d = c - r, at
#   offset r along it. cells with c < r belong to no diagonal (padding).
#   diagonal d holds min(rows, cols - d) cells, so a prefix sum over the
#   diagonal lengths gives where diagonal d starts in the answer:
#       answer[diag_start[d] + r] = encodedText[r * cols + c]
#
#   NOTE : same bound as V0 and harder to say out loud -- learn V0 first.
#
# time = O(n), space = O(n)   (n = len(encodedText))
class Solution2(object):
    def decodeCiphertext(self, encodedText, rows):
        """
        :type encodedText: str
        :type rows: int
        :rtype: str
        """
        # edge case: nothing was encoded
        if not encodedText:
            return ""

        cols = len(encodedText) // rows

        # diag_start[d] = index in the answer where diagonal d begins
        diag_start = [0] * cols
        total_length = 0
        for d in range(cols):
            diag_start[d] = total_length
            diag_length = min(rows, cols - d)
            total_length += diag_length

        decoded_chars = [" "] * total_length
        for pos in range(len(encodedText)):
            row = pos // cols
            col = pos % cols
            # cells left of the main diagonal start no diagonal -> padding
            if col < row:
                continue
            diag = col - row
            decoded_chars[diag_start[diag] + row] = encodedText[pos]

        decoded = "".join(decoded_chars)

        # strip only the trailing padding, as in V0
        return decoded.rstrip()
