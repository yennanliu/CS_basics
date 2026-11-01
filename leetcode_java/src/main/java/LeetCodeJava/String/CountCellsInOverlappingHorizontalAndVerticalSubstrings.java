package LeetCodeJava.String;

// https://leetcode.com/problems/count-cells-in-overlapping-horizontal-and-vertical-substrings/description/

/**
 * 3529. Count Cells in Overlapping Horizontal and Vertical Substrings
 * Medium
 *
 * You are given an m x n matrix grid consisting of characters and a string
 * pattern.
 *
 * A horizontal substring is a contiguous sequence of characters read from left to
 * right. If the end of a row is reached before the substring is complete, it wraps
 * to the first column of the next row and continues as needed. You do not wrap
 * from the bottom row back to the top.
 *
 * A vertical substring is a contiguous sequence of characters read from top to
 * bottom. If the bottom of a column is reached before the substring is complete,
 * it wraps to the first row of the next column and continues as needed. You do not
 * wrap from the last column back to the first.
 *
 * Count the number of cells in the matrix that satisfy the following condition:
 *
 * The cell must be part of at least one horizontal substring and at least one
 * vertical substring, where both substrings are equal to the given pattern.
 *
 * Return the count of these cells.
 *
 * Example 1:
 *
 * Input: grid =
 * [["a","a","c","c"],["b","b","b","c"],["a","a","b","a"],["c","a","a","c"],["a","a","b","a"]],
 * pattern = "abaca"
 *
 * Output: 1
 *
 * Explanation:
 *
 * The pattern "abaca" appears once as a horizontal substring (colored blue) and
 * once as a vertical substring (colored red), intersecting at one cell (colored
 * purple).
 *
 * Example 2:
 *
 * Input: grid =
 * [["c","a","a","a"],["a","a","b","a"],["b","b","a","a"],["a","a","b","a"]],
 * pattern = "aba"
 *
 * Output: 4
 *
 * Explanation:
 *
 * The cells colored above are all part of at least one horizontal and one vertical
 * substring matching the pattern "aba".
 *
 * Example 3:
 *
 * Input: grid = [["a"]], pattern = "a"
 *
 * Output: 1
 *
 * Constraints:
 *
 * m == grid.length
 *
 * n == grid[i].length
 *
 * 1 <= m, n <= 1000
 *
 * 1 <= m * n <= 10^5
 *
 * 1 <= pattern.length <= m * n
 *
 * grid and pattern consist of only lowercase English letters.
 *
 */
public class CountCellsInOverlappingHorizontalAndVerticalSubstrings {

    // V0
    // IDEA: FLATTEN THE GRID ROW-MAJOR AND COLUMN-MAJOR, KMP EACH, MARK COVERED CELLS WITH A DIFF ARRAY
    //   a horizontal substring (wrapping row to row) is exactly a window of the row-major string;
    //   a vertical one is a window of the column-major string. KMP finds every occurrence; a
    //   difference array marks the covered flat indices without quadratic re-marking.
    //   column-major position t is cell (t % m, t / m) = row-major index (t % m) * n + t / m.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countCells(char[][] grid, String pattern) {
        int rows = grid.length;
        int cols = grid[0].length;
        int total = rows * cols;
        int patternLength = pattern.length();
        if (patternLength > total) {
            return 0;
        }

        int[] failure = buildFailure(pattern);

        StringBuilder rowMajor = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rowMajor.append(grid[r][c]);
            }
        }
        StringBuilder colMajor = new StringBuilder();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                colMajor.append(grid[r][c]);
            }
        }

        boolean[] horizontal = coveredPositions(rowMajor.toString(), pattern, failure);
        boolean[] vertical = coveredPositions(colMajor.toString(), pattern, failure);

        int count = 0;
        for (int t = 0; t < total; t++) {
            // translate column-major position t back to its row-major index
            int r = t % rows;
            int c = t / rows;
            if (vertical[t] && horizontal[r * cols + c]) {
                count++;
            }
        }
        return count;
    }

    /** KMP failure function: failure[i] = length of the longest proper border of pattern[0..i]. */
    private int[] buildFailure(String pattern) {
        int[] failure = new int[pattern.length()];
        int matched = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (matched > 0 && pattern.charAt(i) != pattern.charAt(matched)) {
                matched = failure[matched - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(matched)) {
                matched++;
            }
            failure[i] = matched;
        }
        return failure;
    }

    /** Marks every index of text covered by at least one occurrence of pattern. */
    private boolean[] coveredPositions(String text, String pattern, int[] failure) {
        int len = text.length();
        int patternLength = pattern.length();
        // diff[s]++ and diff[s + L]-- for each occurrence starting at s
        int[] diff = new int[len + 1];
        int matched = 0;
        for (int i = 0; i < len; i++) {
            while (matched > 0 && text.charAt(i) != pattern.charAt(matched)) {
                matched = failure[matched - 1];
            }
            if (text.charAt(i) == pattern.charAt(matched)) {
                matched++;
            }
            if (matched == patternLength) {
                int occurrenceStart = i - patternLength + 1;
                diff[occurrenceStart]++;
                diff[occurrenceStart + patternLength]--;
                // keep going to find overlapping occurrences
                matched = failure[matched - 1];
            }
        }
        boolean[] covered = new boolean[len];
        int running = 0;
        for (int i = 0; i < len; i++) {
            running += diff[i];
            covered[i] = running > 0;
        }
        return covered;
    }
}
