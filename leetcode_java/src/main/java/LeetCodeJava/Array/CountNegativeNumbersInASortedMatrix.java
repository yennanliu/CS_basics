package LeetCodeJava.Array;

// https://leetcode.com/problems/count-negative-numbers-in-a-sorted-matrix/description/

/**
 * 1351. Count Negative Numbers in a Sorted Matrix
 * Easy
 *
 * Given a m x n matrix grid which is sorted in non-increasing order both row-wise and column-wise,
 * return the number of negative numbers in grid.
 *
 * Example 1:
 *
 * Input: grid = [[4,3,2,-1],[3,2,1,-1],[1,1,-1,-2],[-1,-1,-2,-3]]
 * Output: 8
 * Explanation: There are 8 negatives number in the matrix.
 *
 * Example 2:
 *
 * Input: grid = [[3,2],[1,0]]
 * Output: 0
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 100
 * -100 <= grid[i][j] <= 100
 *
 * Follow up: Could you find an O(n + m) solution?
 *
 */
public class CountNegativeNumbersInASortedMatrix {

    // V0
    // IDEA: STAIRCASE SEARCH FROM THE BOTTOM-LEFT CORNER
    //   the bottom-left cell is the smallest of its row and the largest of its column:
    //     grid[r][c] >= 0 -> everything left of c in this row is >= 0 too, move right
    //     grid[r][c] <  0 -> cells c..n-1 of this row are all negative, count them, move up
    //   each step drops one row or one column -> O(M + N), the follow-up's answer
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int countNegatives(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int r = m - 1;
        int c = 0;
        int negatives = 0;

        while (r >= 0 && c < n) {
            if (grid[r][c] >= 0) {
                c++;
            } else {
                negatives += n - c;
                r--;
            }
        }
        return negatives;
    }

    // V1
    // IDEA: BINARY SEARCH EACH ROW FOR ITS FIRST NEGATIVE (rows are non-increasing)
    /**
     * time = O(M log N)
     * space = O(1)
     */
    public int countNegatives_1(int[][] grid) {
        int negatives = 0;
        for (int[] row : grid) {
            int left = 0;
            int right = row.length;
            // invariant: the first negative index lies in [left, right] (right == length -> none)
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (row[mid] < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            negatives += row.length - left;
        }
        return negatives;
    }
}
