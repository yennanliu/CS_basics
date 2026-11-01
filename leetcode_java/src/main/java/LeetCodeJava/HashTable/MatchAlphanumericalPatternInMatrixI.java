package LeetCodeJava.HashTable;

// https://leetcode.com/problems/match-alphanumerical-pattern-in-matrix-i/description/

import java.util.*;

/**
 * 3078. Match Alphanumerical Pattern in Matrix I
 * Medium
 *
 *
 * You are given a 2D integer matrix board and a 2D character matrix pattern. Where 0 <= board[r][c] <= 9 and each element of pattern is either a digit or a lowercase English letter.
 *
 * Your task is to find a submatrix of board that matches pattern.
 *
 * An integer matrix part matches pattern if we can replace cells containing letters in pattern with some digits (each distinct letter with a unique digit) in such a way that the resulting matrix equals part. In other words,
 *
 * The matrices have identical dimensions.
 * If pattern[r][c] is a digit, then part[r][c] must be the same digit.
 * If pattern[r][c] is a letter x:
 *     If there is another cell (r', c') in pattern with pattern[r'][c'] == x, then part[r][c] must equal part[r'][c'].
 *     Distinct letters must map to distinct digits, i.e. if pattern[r][c] != pattern[r'][c'] and both are letters, then part[r][c] != part[r'][c'].
 *
 * Return an array of length 2 containing the row number and column number of the upper-left corner of the submatrix of board that matches pattern, or [-1, -1] if there is no such submatrix.
 *
 * If there are multiple such submatrices, return the coordinates of the upper-left corner of the topmost one, and if there are multiple such rows, return the leftmost one.
 *
 *
 * Example 1:
 *
 * Input: board = [[1,2,2],[2,2,3],[2,3,3]], pattern = ["ab","bb"]
 * Output: [0,0]
 * Explanation: If we consider this mapping: "a" -> 1 and "b" -> 2; the submatrix with the upper-left corner (0,0) is a match as outlined in the matrix above.
 * Note that the submatrix with the upper-left corner (1,1) is also a match but since it comes after the other one, we return [0,0].
 *
 * Example 2:
 *
 * Input: board = [[1,1,2],[3,3,4],[6,6,6]], pattern = ["ab","66"]
 * Output: [1,1]
 * Explanation: If we consider this mapping: "a" -> 3 and "b" -> 4; the submatrix with the upper-left corner (1,1) is a match as outlined in the matrix above.
 * Note that the submatrix with the upper-left corner (0,1) is not a match because the mapping is not one-to-one.
 *
 * Example 3:
 *
 * Input: board = [[1,2],[2,1]], pattern = ["22"]
 * Output: [-1,-1]
 * Explanation: The pattern requires two adjacent equal cells with the value 2, which board does not have.
 *
 *
 * Constraints:
 *
 * 1 <= board.length <= 50
 * 1 <= board[i].length <= 50
 * 0 <= board[i][j] <= 9
 * 1 <= pattern.length <= 50
 * 1 <= pattern[i].length <= 50
 * pattern[i][j] is either a digit represented as a character or a lowercase English letter.
 *
 */
public class MatchAlphanumericalPatternInMatrixI {

    // V0
    // IDEA: TRY EVERY ANCHOR, VERIFY WITH A BIJECTIVE LETTER <-> DIGIT MAP
    // scanning anchors row by row, then column by column, yields the topmost-then-
    // leftmost match first. letters need a mapping that is one-to-one both ways:
    //   letter -> digit (the same letter always means the same digit)
    //   digit -> letter (two different letters may not claim one digit)
    /**
     * time = O(M * N * PM * PN)
     * space = O(1)
     */
    public int[] findPattern(int[][] board, String[] pattern) {
        int rows = board.length;
        int cols = board[0].length;
        int patternRows = pattern.length;
        int patternCols = pattern[0].length();

        for (int top = 0; top + patternRows <= rows; top++) {
            for (int left = 0; left + patternCols <= cols; left++) {
                if (matchesAt(board, pattern, top, left)) {
                    return new int[] {top, left};
                }
            }
        }
        return new int[] {-1, -1};
    }

    /** True if pattern matches the submatrix of board whose upper-left corner is (top, left). */
    private boolean matchesAt(int[][] board, String[] pattern, int top, int left) {
        // both maps are rebuilt per anchor; -1 / '\0' mean "not assigned yet"
        int[] letterToDigit = new int[26];
        Arrays.fill(letterToDigit, -1);
        char[] digitToLetter = new char[10];

        for (int r = 0; r < pattern.length; r++) {
            for (int c = 0; c < pattern[r].length(); c++) {
                char symbol = pattern[r].charAt(c);
                int value = board[top + r][left + c];

                if (Character.isDigit(symbol)) {
                    // a digit must match literally
                    if (value != symbol - '0') {
                        return false;
                    }
                    continue;
                }

                int letter = symbol - 'a';
                if (letterToDigit[letter] != -1) {
                    // letter already bound: it must mean the same digit again
                    if (letterToDigit[letter] != value) {
                        return false;
                    }
                } else if (digitToLetter[value] != '\0') {
                    // digit already taken by a different letter
                    return false;
                } else {
                    letterToDigit[letter] = value;
                    digitToLetter[value] = symbol;
                }
            }
        }
        return true;
    }
}
