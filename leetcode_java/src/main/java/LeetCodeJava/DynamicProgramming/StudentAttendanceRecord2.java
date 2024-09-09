package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/student-attendance-record-ii/description/

import java.util.Arrays;

/**
 * 552. Student Attendance Record II
 * Hard
 * Topics
 * Companies
 * An attendance record for a student can be represented as a string where each character signifies whether the student was absent, late, or present on that day. The record only contains the following three characters:
 *
 * 'A': Absent.
 * 'L': Late.
 * 'P': Present.
 * Any student is eligible for an attendance award if they meet both of the following criteria:
 *
 * The student was absent ('A') for strictly fewer than 2 days total.
 * The student was never late ('L') for 3 or more consecutive days.
 * Given an integer n, return the number of possible attendance records of length n that make a student eligible for an attendance award. The answer may be very large, so return it modulo 109 + 7.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 8
 * Explanation: There are 8 records with length 2 that are eligible for an award:
 * "PP", "AP", "PA", "LP", "PL", "AL", "LA", "LL"
 * Only "AA" is not eligible because there are 2 absences (there need to be fewer than 2).
 * Example 2:
 *
 * Input: n = 1
 * Output: 3
 * Example 3:
 *
 * Input: n = 10101
 * Output: 183236316
 *
 *
 * Constraints:
 *
 * 1 <= n <= 105
 *
 */

public class StudentAttendanceRecord2 {

    // V0
    // TODO : implement

    // V1

    // V2_1
    // IDEA : Top-Down Dynamic Programming with Memoization
    // https://leetcode.com/problems/student-attendance-record-ii/editorial/

    private final int MOD = 1000000007;
    // Cache to store sub-problem results.
    private int[][][] memo;

    // Recursive function to return the count of combinations of length 'n' eligible for the award.
    private int eligibleCombinations(
            int n,
            int totalAbsences,
            int consecutiveLates
    ) {
        // If the combination has become not eligible for the award,
        // then we will not count any combinations that can be made using it.
        if (totalAbsences >= 2 || consecutiveLates >= 3) {
            return 0;
        }
        // If we have generated a combination of length 'n' we will count it.
        if (n == 0) {
            return 1;
        }
        // If we have already seen this sub-problem earlier, we return the stored result.
        if (memo[n][totalAbsences][consecutiveLates] != -1) {
            return memo[n][totalAbsences][consecutiveLates];
        }

        int count = 0;
        // We choose 'P' for the current position.
        count = eligibleCombinations(n - 1, totalAbsences, 0) % MOD;
        // We choose 'A' for the current position.
        count = (count + eligibleCombinations(n - 1, totalAbsences + 1, 0)) %
                MOD;
        // We choose 'L' for the current position.
        count = (count +
                eligibleCombinations(n - 1, totalAbsences, consecutiveLates + 1)) %
                MOD;

        // Return and store the current sub-problem result in the cache.
        return memo[n][totalAbsences][consecutiveLates] = count;
    }

    public int checkRecord_2_1(int n) {
        // Initialize the cache.
        memo = new int[n + 1][2][3];
        for (int[][] array2D : memo) {
            for (int[] array1D : array2D) {
                Arrays.fill(array1D, -1);
            }
        }
        // Return count of combinations of length 'n' eligible for the award.
        return eligibleCombinations(n, 0, 0);
    }


    // V2_2
    // IDEA : Bottom-Up Dynamic Programming
    // https://leetcode.com/problems/student-attendance-record-ii/editorial/
    public int checkRecord_2_2(int n) {
        int MOD = 1000000007;
        // Cache to store sub-problem results.
        int[][][] dp = new int[n + 1][2][3];

        // Base case: there is 1 string of length 0 with zero 'A' and zero 'L'.
        dp[0][0][0] = 1;

        // Iterate on smaller sub-problems and use the current smaller sub-problem
        // to generate results for bigger sub-problems.
        for (int len = 0; len < n; ++len) {
            for (int totalAbsences = 0; totalAbsences <= 1; ++totalAbsences) {
                for (
                        int consecutiveLates = 0;
                        consecutiveLates <= 2;
                        ++consecutiveLates
                ) {
                    // Store the count when 'P' is chosen.
                    dp[len + 1][totalAbsences][0] = (dp[len +
                            1][totalAbsences][0] +
                            dp[len][totalAbsences][consecutiveLates]) %
                            MOD;
                    // Store the count when 'A' is chosen.
                    if (totalAbsences < 1) {
                        dp[len + 1][totalAbsences + 1][0] = (dp[len +
                                1][totalAbsences + 1][0] +
                                dp[len][totalAbsences][consecutiveLates]) %
                                MOD;
                    }
                    // Store the count when 'L' is chosen.
                    if (consecutiveLates < 2) {
                        dp[len + 1][totalAbsences][consecutiveLates + 1] =
                                (dp[len + 1][totalAbsences][consecutiveLates + 1] +
                                        dp[len][totalAbsences][consecutiveLates]) %
                                        MOD;
                    }
                }
            }
        }

        // Sum up the counts for all combinations of length 'n' with different absent and late counts.
        int count = 0;
        for (int totalAbsences = 0; totalAbsences <= 1; ++totalAbsences) {
            for (
                    int consecutiveLates = 0;
                    consecutiveLates <= 2;
                    ++consecutiveLates
            ) {
                count = (count + dp[n][totalAbsences][consecutiveLates]) % MOD;
            }
        }
        return count;
    }

    // V2_3
    // IDEA : Bottom-Up Dynamic Programming, Space Optimized
    // https://leetcode.com/problems/student-attendance-record-ii/editorial/
    public int checkRecord_2_3(int n) {
        int MOD = 1000000007;
        // Cache to store current sub-problem results.
        int[][] dpCurrState = new int[2][3];
        // Cache to store next sub-problem results.
        int[][] dpNextState = new int[2][3];

        // Base case: there is 1 string of length 0 with zero 'A' and zero 'L'.
        dpCurrState[0][0] = 1;

        // Iterate on smaller sub-problems and use the current smaller sub-problem
        // to generate results for bigger sub-problems.
        for (int len = 0; len < n; ++len) {
            for (int totalAbsences = 0; totalAbsences <= 1; ++totalAbsences) {
                for (
                        int consecutiveLates = 0;
                        consecutiveLates <= 2;
                        ++consecutiveLates
                ) {
                    // Store the count when 'P' is chosen.
                    dpNextState[totalAbsences][0] =
                            (dpNextState[totalAbsences][0] +
                                    dpCurrState[totalAbsences][consecutiveLates]) %
                                    MOD;
                    // Store the count when 'A' is chosen.
                    if (totalAbsences < 1) {
                        dpNextState[totalAbsences + 1][0] =
                                (dpNextState[totalAbsences + 1][0] +
                                        dpCurrState[totalAbsences][consecutiveLates]) %
                                        MOD;
                    }
                    // Store the count when 'L' is chosen.
                    if (consecutiveLates < 2) {
                        dpNextState[totalAbsences][consecutiveLates + 1] =
                                (dpNextState[totalAbsences][consecutiveLates + 1] +
                                        dpCurrState[totalAbsences][consecutiveLates]) %
                                        MOD;
                    }
                }
            }

            // Next state sub-problems will become current state sub-problems in the next iteration.
            System.arraycopy(
                    dpNextState,
                    0,
                    dpCurrState,
                    0,
                    dpCurrState.length
            );
            // Next state sub-problem results will reset to zero.
            dpNextState = new int[2][3];
        }

        // Sum up the counts for all combinations of length 'n' with different absent and late counts.
        int count = 0;
        for (int totalAbsences = 0; totalAbsences <= 1; ++totalAbsences) {
            for (
                    int consecutiveLates = 0;
                    consecutiveLates <= 2;
                    ++consecutiveLates
            ) {
                count = (count + dpCurrState[totalAbsences][consecutiveLates]) %
                        MOD;
            }
        }
        return count;
    }

}
