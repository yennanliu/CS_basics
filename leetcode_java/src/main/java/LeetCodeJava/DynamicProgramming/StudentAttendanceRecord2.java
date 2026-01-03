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
//    public int checkRecord(int n) {
//
//    }

    // V1
    // IDEA: DP (gemini)
    /**  IDEA:
     *
     * `LC 552: Student Attendance Record II`
     * is a much more difficult version of the
     * previous problem. Since  can be up to , we must use **Dynamic Programming**.
     *
     * ### 🧠 The Logic: State Definition
     *
     * A record is valid if:
     *
     * 1. It contains **fewer than 2** 'A's (Absences).
     * 2. It does **not** contain **3 or more consecutive** 'L's (Lates).
     *
     * To track this, our DP state must keep track of two things:
     *
     * 1. **Total count of 'A'** seen so far ( or ).
     * 2. **Current streak of 'L'** ending at the current day (, , or ).
     *
     * We define `dp[n][countA][streakL]` as the number of valid sequences of length `n`.
     *
     * ### 🔑 Logic Explanation
     *
     * 1. **State Transitions**:
     * * **If we add 'P'**: We can always do this. It "breaks" any existing streak of 'L', resetting it to .
     * * **If we add 'A'**: We can only do this if we haven't used an 'A' before (). This also resets the 'L' streak to .
     * * **If we add 'L'**: We can only do this if the current streak is less than  (). This increments the 'L' streak.
     *
     *
     * 2. **Modular Arithmetic**: Because the answer can be very large, we use `long` for calculations and take `% 10^9 + 7` at every addition to prevent overflow.
     * 3. **Space Optimization**: By using `prevDp` and `nextDp`, we reduce the space complexity from  to , which is essentially ** constant space**.
     *
     * ---
     *
     * ### 📊 Complexity Analysis
     *
     * * **Time Complexity**: . We iterate from  to . Inside the loop, we perform a constant number of operations ( states).
     * * **Space Complexity**: . We only maintain two small 2D arrays regardless of the size of .
     *
     * **Note**: If  was extremely large (e.g., ), this problem would require **Matrix Exponentiation** to solve in  time. For , this DP approach is optimal.
     */
    public int checkRecord_1(int n) {
        long MOD = 1_000_000_007;

        /**
         * dp[n][countA][streakL]: number of valid sequences of length n.
         *
         */
        // dp[countA][streakL]
        // countA: 0, 1
        // streakL: 0, 1, 2
        long[][] prevDp = new long[2][3];

        // Base case: day 0 (an empty string is 1 valid way)
        prevDp[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            long[][] nextDp = new long[2][3];

            for (int a = 0; a < 2; a++) {
                for (int l = 0; l < 3; l++) {
                    if (prevDp[a][l] == 0)
                        continue;

                    // 1. Add 'P' (Present)
                    // Resets Late streak to 0, A count stays same
                    nextDp[a][0] = (nextDp[a][0] + prevDp[a][l]) % MOD;

                    // 2. Add 'A' (Absent)
                    // Resets Late streak to 0, A count increases
                    if (a == 0) {
                        nextDp[1][0] = (nextDp[1][0] + prevDp[a][l]) % MOD;
                    }

                    // 3. Add 'L' (Late)
                    // Increases Late streak by 1, A count stays same
                    if (l < 2) {
                        nextDp[a][l + 1] = (nextDp[a][l + 1] + prevDp[a][l]) % MOD;
                    }
                }
            }
            prevDp = nextDp;
        }

        // The answer is the sum of all valid states after n days
        long total = 0;
        for (int a = 0; a < 2; a++) {
            for (int l = 0; l < 3; l++) {
                total = (total + prevDp[a][l]) % MOD;
            }
        }

        return (int) total;
    }

    // V1-1
    // IDEA: DP (GPT)
    /**  IDEA:
     *
     * Here’s a **clean, efficient Java DP solution** for **LeetCode 552 — Student Attendance Record II**.
     *
     * ### ✅ Problem Summary
     *
     * You want to count **all rewardable attendance records** of length `n`, where a record:
     *
     * * Has **fewer than 2 ‘A’** (absent) total
     * * Has **no 3 consecutive ‘L’** (late)
     *
     * Return the count modulo **1_000_000_007**. ([Leetcode][1])
     *
     * ---
     *
     * ## 🧠 DP Approach (Bottom-Up, State Tracking)
     *
     * We define:
     *
     * ```
     * dp[a][l] = number of sequences built so far
     *            with 'a' total A’s and ending with 'l' consecutive L’s
     * ```
     *
     * Where:
     *
     * * `a ∈ {0,1}` (0 or 1 absences so far)
     * * `l ∈ {0,1,2}` (last consecutive L’s)
     *
     * At each step, we transition by appending:
     *
     * * `'P'`: resets late count
     * * `'L'`: increases late count (if < 2)
     * * `'A'`: increases absence count (if 0)
     *
     * ---
     *
     * ## 📌 Explanation (High-Level)
     *
     * Let `dp[a][l]` represent:
     * ✔ number of sequences of current length
     * ✔ with **a** Absences used so far
     * ✔ and **l** consecutive Lates at the end.
     *
     * For each added day:
     *
     * * If we add **P** → `l` becomes 0 (late streak ends)
     * * If we add **A** → only valid if we have *not* used an A yet
     * * If we add **L** → only if `l < 2` (to avoid 3 in a row)
     *
     * The answer is the sum of all `dp[a][l]` after `n` days. ([WalkCCC][2])
     *
     * ---
     *
     * ## ⏱ Time and Space
     *
     * | Metric | Value                    |
     * | ------ | ------------------------ |
     * | Time   | **O(n)**                 |
     * | Space  | **O(1)** (only 6 states) |
     *
     * This passes constraints up to **n = 100000**. ([Leetcode][1])
     *
     * ---
     *
     * ## 🧠 Alternative (Optimized with rolling states)
     *
     * This solution already uses two rolling 2×3 buffers, so it’s space-optimized compared to a full 3D DP array. If you want further mathematical or matrix exponentiation optimizations for extremely large `n`, let me know!
     *
     * ---
     *
     * ### Example
     *
     * ```
     * Input: n = 2
     * Valid rewardable records:
     * "PP","AP","PA","LP","PL","AL","LA","LL"
     * Output: 8
     * ```
     *
     * Explanation: Only `"AA"` is invalid because it has 2 Absences. ([Leetcode][1])
     *
     * ---
     *
     * If you want, I can also show a **top-down memoized DP solution** or explain how this DP works with a **state diagram/table**.
     *
     * [1]: https://leetcode.ca/all/552.html?utm_source=chatgpt.com "Leetcode 552. Student Attendance Record II"
     * [2]: https://walkccc.me/LeetCode/problems/552/?utm_source=chatgpt.com "552. Student Attendance Record II - LeetCode Solutions"
     *
     */
    public int checkRecord_1_2(int n) {
        final int MOD = 1_000_000_007;

        // dp[a][l]: number of ways with a A's and l consecutive L's
        long[][] dp = new long[2][3];
        dp[0][0] = 1; // start with empty prefix: 0 A's, 0 L's

        for (int day = 0; day < n; day++) {
            long[][] next = new long[2][3];

            for (int a = 0; a <= 1; a++) {
                for (int l = 0; l <= 2; l++) {
                    long count = dp[a][l];
                    if (count == 0)
                        continue;

                    // Append 'P': resets L count
                    next[a][0] = (next[a][0] + count) % MOD;

                    // Append 'A': only if no A used yet
                    if (a == 0) {
                        next[1][0] = (next[1][0] + count) % MOD;
                    }

                    // Append 'L': only if < 2 consecutive L's so far
                    if (l < 2) {
                        next[a][l + 1] = (next[a][l + 1] + count) % MOD;
                    }
                }
            }

            dp = next;
        }

        long ans = 0;
        // sum all states
        for (int a = 0; a <= 1; a++) {
            for (int l = 0; l <= 2; l++) {
                ans = (ans + dp[a][l]) % MOD;
            }
        }
        return (int) ans;
    }



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
            int consecutiveLates) {
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
