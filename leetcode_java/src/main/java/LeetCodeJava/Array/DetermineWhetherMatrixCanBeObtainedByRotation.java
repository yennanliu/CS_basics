package LeetCodeJava.Array;

// https://leetcode.com/problems/determine-whether-matrix-can-be-obtained-by-rotation/description/

import java.util.Arrays;

/**
 * 1886. Determine Whether Matrix Can Be Obtained By Rotation
 * Easy
 *
 * Given two n x n binary matrices mat and target, return true if it is possible to make mat equal to
 * target by rotating mat in 90-degree increments, or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: mat = [[0,1],[1,0]], target = [[1,0],[0,1]]
 * Output: true
 * Explanation: We can rotate mat 90 degrees clockwise to make mat equal target.
 *
 * Example 2:
 *
 * Input: mat = [[0,1],[1,1]], target = [[1,0],[0,1]]
 * Output: false
 * Explanation: It is impossible to make mat equal to target by rotating mat.
 *
 * Example 3:
 *
 * Input: mat = [[0,0,0],[0,1,0],[1,1,1]], target = [[1,1,1],[0,1,0],[0,0,0]]
 * Output: true
 * Explanation: We can rotate mat 90 degrees clockwise two times to make mat equal target.
 *
 *
 * Constraints:
 *
 * n == mat.length == target.length
 * n == mat[i].length == target[i].length
 * 1 <= n <= 10
 * mat[i][j] and target[i][j] are either 0 or 1.
 *
 */
public class DetermineWhetherMatrixCanBeObtainedByRotation {

    private static final int ROTATIONS = 4;

    // V0
    // IDEA: TRY THE 4 ROTATIONS (a 4th rotation is the identity)
    // rotating 90 degrees clockwise gives rotated[i][j] = old[n - 1 - j][i].
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public boolean findRotation(int[][] mat, int[][] target) {
        int[][] current = mat;
        for (int turn = 0; turn < ROTATIONS; turn++) {
            if (Arrays.deepEquals(current, target)) {
                return true;
            }
            current = rotateClockwise(current);
        }
        return false;
    }

    /** Returns a new matrix rotated 90 degrees clockwise. */
    private int[][] rotateClockwise(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[i][j] = matrix[n - 1 - j][i];
            }
        }
        return rotated;
    }

    // V1
    // IDEA: CHECK ALL 4 ROTATIONS IN ONE PASS WITHOUT BUILDING THEM
    // cell (i, j) of mat lands at (j, n-1-i) after 90, (n-1-i, n-1-j) after 180 and (n-1-j, i)
    // after 270 degrees; a rotation is ruled out by the first cell that disagrees with target.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public boolean findRotation_1(int[][] mat, int[][] target) {
        int n = mat.length;
        boolean[] possible = {true, true, true, true}; // 0, 90, 180, 270 degrees
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = mat[i][j];
                if (target[i][j] != value) {
                    possible[0] = false;
                }
                if (target[j][n - 1 - i] != value) {
                    possible[1] = false;
                }
                if (target[n - 1 - i][n - 1 - j] != value) {
                    possible[2] = false;
                }
                if (target[n - 1 - j][i] != value) {
                    possible[3] = false;
                }
            }
        }
        return possible[0] || possible[1] || possible[2] || possible[3];
    }
}
