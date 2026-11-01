package LeetCodeJava.Array;

// https://leetcode.com/problems/decode-the-slanted-ciphertext/description/

/**
 * 2075. Decode the Slanted Ciphertext
 * Medium
 *
 * A string originalText is encoded using a slanted transposition cipher to a string encodedText
 * with the help of a matrix having a fixed number of rows rows.
 *
 * originalText is placed first in a top-left to bottom-right manner.
 *
 * The blue cells are filled first, followed by the red cells, then the yellow cells, and so on,
 * until we reach the end of originalText. The arrow indicates the order in which the cells are
 * filled. All empty cells are filled with ' '. The number of columns is chosen such that the
 * rightmost column will not be empty after filling in originalText.
 *
 * encodedText is then formed by appending all characters of the matrix in a row-wise fashion.
 *
 * The characters in the blue cells are appended first to encodedText, then the red cells, and so
 * on, and finally the yellow cells. The arrow indicates the order in which the cells are accessed.
 *
 * For example, if originalText = "cipher" and rows = 3, then we encode it in the following manner:
 *
 * The blue arrows depict how originalText is placed in the matrix, and the red arrows denote the
 * order in which encodedText is formed. In the above example, encodedText = "ch   ie   pr".
 *
 * Given the encoded string encodedText and number of rows rows, return the original string
 * originalText.
 *
 * Note: originalText cannot have any trailing spaces ' '. The test cases are generated such that
 * there is only one possible originalText.
 *
 *
 * Example 1:
 *
 * Input: encodedText = "ch   ie   pr", rows = 3
 * Output: "cipher"
 * Explanation: This is the same example described in the problem description.
 *
 * Example 2:
 *
 * Input: encodedText = "iveo    eed   l te   olc", rows = 4
 * Output: "i love leetcode"
 * Explanation: The following image shows the matrix that was used to encode originalText.
 * The blue arrows show how we can find originalText from encodedText.
 *
 * Example 3:
 *
 * Input: encodedText = "coding", rows = 1
 * Output: "coding"
 * Explanation: Since there is only 1 row, both originalText and encodedText are the same.
 *
 *
 * Constraints:
 *
 * 0 <= encodedText.length <= 10^6
 * encodedText consists of lowercase English letters and ' ' only.
 * encodedText is a valid encoding of some originalText that does not have trailing spaces.
 * 1 <= rows <= 1000
 * The test cases are generated such that there is only one possible originalText.
 *
 */
public class DecodeTheSlantedCiphertext {

    // V0
    // IDEA: REBUILD THE MATRIX SHAPE, THEN READ IT BACK ALONG ITS DIAGONALS
    //       encodedText is the rows x cols matrix flattened row by row, so
    //       cols = len / rows and cell (r, c) = encodedText[r * cols + c];
    //       walking each diagonal in start-column order gives the original order,
    //       then strip the trailing padding spaces (interior spaces are real text)
    /**
     * time = O(N)
     * space = O(N)
     */
    public String decodeCiphertext(String encodedText, int rows) {
        // edge case: nothing was encoded
        if (encodedText == null || encodedText.isEmpty()) {
            return "";
        }

        int cols = encodedText.length() / rows;

        StringBuilder decoded = new StringBuilder();
        // each start column begins one diagonal of the original text
        for (int startCol = 0; startCol < cols; startCol++) {
            int row = 0;
            int col = startCol;
            // walk down-right until we fall off the bottom or the right edge
            while (row < rows && col < cols) {
                decoded.append(encodedText.charAt(row * cols + col));
                row++;
                col++;
            }
        }

        return stripTrailingSpaces(decoded);
    }

    // V1
    // IDEA: SCATTER -- SEND EACH ENCODED CHARACTER STRAIGHT TO ITS FINAL SLOT
    //       cell (r, c) lies on the diagonal that started at column d = c - r, at offset r;
    //       cells with c < r are padding. diagonal d holds min(rows, cols - d) cells, so a
    //       prefix sum over the diagonal lengths gives where diagonal d starts in the answer.
    //       same bound as V0 and harder to say out loud -- learn V0 first.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String decodeCiphertext_1(String encodedText, int rows) {
        // edge case: nothing was encoded
        if (encodedText == null || encodedText.isEmpty()) {
            return "";
        }

        int cols = encodedText.length() / rows;

        // diagStart[d] = index in the answer where diagonal d begins
        int[] diagStart = new int[cols];
        int totalLength = 0;
        for (int d = 0; d < cols; d++) {
            diagStart[d] = totalLength;
            totalLength += Math.min(rows, cols - d);
        }

        char[] decoded = new char[totalLength];
        for (int pos = 0; pos < encodedText.length(); pos++) {
            int row = pos / cols;
            int col = pos % cols;
            // cells left of the main diagonal start no diagonal -> padding
            if (col < row) {
                continue;
            }
            int diag = col - row;
            decoded[diagStart[diag] + row] = encodedText.charAt(pos);
        }

        return stripTrailingSpaces(new StringBuilder(new String(decoded)));
    }

    /** Removes the trailing ' ' padding; originalText never ends in a space. */
    private String stripTrailingSpaces(StringBuilder sb) {
        int end = sb.length();
        while (end > 0 && sb.charAt(end - 1) == ' ') {
            end--;
        }
        return sb.substring(0, end);
    }
}
