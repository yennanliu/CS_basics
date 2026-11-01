package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-students-taking-exam/description/

import java.util.*;

/**
 * 1349. Maximum Students Taking Exam
 * Hard
 *
 * Given a m * n matrix seats that represent seats distributions in a classroom.
 * If a seat is broken, it is denoted by '#' character otherwise it is denoted by a '.' character.
 *
 * Students can see the answers of those sitting next to the left, right, upper left and upper right,
 * but he cannot see the answers of the student sitting directly in front or behind him.
 * Return the maximum number of students that can take the exam together without any cheating
 * being possible.
 *
 * Students must be placed in seats in good condition.
 *
 *
 * Example 1:
 *
 * Input: seats = [["#",".","#","#",".","#"],
 *                 [".","#","#","#","#","."],
 *                 ["#",".","#","#",".","#"]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: seats = [[".","#"],
 *                 ["#","#"],
 *                 ["#","."],
 *                 ["#","#"],
 *                 [".","#"]]
 * Output: 3
 *
 * Example 3:
 *
 * Input: seats = [["#",".",".",".","#"],
 *                 [".","#",".","#","."],
 *                 [".",".","#",".","."],
 *                 [".","#",".","#","."],
 *                 ["#",".",".",".","#"]]
 * Output: 10
 *
 *
 * Constraints:
 *
 * seats contains only characters '.' and '#'.
 * m == seats.length
 * n == seats[i].length
 * 1 <= m <= 8
 * 1 <= n <= 8
 *
 */
public class MaximumStudentsTakingExam {

    // V0
    // IDEA: BITMASK DP ROW BY ROW (n <= 8 -> a row's seating is one n-bit mask)
    // bit j set = a student sits at column j. A row mask is valid when:
    //   - it only uses good seats             -> (mask & ~available[row]) == 0
    //   - no two students are side by side    -> (mask & (mask << 1)) == 0
    // and it is compatible with the row above when nobody sits diagonally in front:
    //   - (mask & (prev << 1)) == 0 and (mask & (prev >> 1)) == 0
    // dp[prev] = best total so far when the last processed row is seated as `prev`.
    /**
     * time = O(M * 4^N)
     * space = O(2^N)
     */
    public int maxStudents(char[][] seats) {
        int rows = seats.length;
        int cols = seats[0].length;
        int maskCount = 1 << cols;

        // available[r] = bitmask of the good seats in row r
        int[] available = new int[rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seats[r][c] == '.') {
                    available[r] |= 1 << c;
                }
            }
        }

        // -1 marks an unreachable state; dp[0] = 0 is "an empty row above row 0"
        int[] dp = new int[maskCount];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        for (int r = 0; r < rows; r++) {
            int[] nextDp = new int[maskCount];
            Arrays.fill(nextDp, -1);
            for (int mask = 0; mask < maskCount; mask++) {
                // uses a broken seat
                if ((mask & ~available[r]) != 0) {
                    continue;
                }
                // two students next to each other in the same row
                if ((mask & (mask << 1)) != 0) {
                    continue;
                }
                int students = Integer.bitCount(mask);
                for (int prev = 0; prev < maskCount; prev++) {
                    if (dp[prev] == -1) {
                        continue;
                    }
                    // a student in the row above sits diagonally in front
                    if ((mask & (prev << 1)) != 0 || (mask & (prev >> 1)) != 0) {
                        continue;
                    }
                    nextDp[mask] = Math.max(nextDp[mask], dp[prev] + students);
                }
            }
            dp = nextDp;
        }

        int best = 0;
        for (int value : dp) {
            best = Math.max(best, value);
        }
        return best;
    }
}
