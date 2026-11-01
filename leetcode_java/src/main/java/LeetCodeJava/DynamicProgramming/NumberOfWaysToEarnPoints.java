package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-earn-points/description/

/**
 * 2585. Number of Ways to Earn Points
 * Hard
 *
 * There is a test that has n types of questions. You are given an integer target and a 0-indexed
 * 2D integer array types where types[i] = [counti, marksi] indicates that there are counti
 * questions of the ith type, and each one of them is worth marksi points.
 *
 * Return the number of ways you can earn exactly target points in the exam. Since the answer may
 * be too large, return it modulo 10^9 + 7.
 *
 * Note that questions of the same type are indistinguishable.
 *
 * For example, if there are 3 questions of the same type, then solving the 1st and 2nd questions
 * is the same as solving the 1st and 3rd questions, or the 2nd and 3rd questions.
 *
 *
 * Example 1:
 *
 * Input: target = 6, types = [[6,1],[3,2],[2,3]]
 * Output: 7
 * Explanation: You can earn 6 points in one of the seven ways:
 * - Solve 6 questions of the 0th type: 1 + 1 + 1 + 1 + 1 + 1 = 6
 * - Solve 4 questions of the 0th type and 1 question of the 1st type: 1 + 1 + 1 + 1 + 2 = 6
 * - Solve 2 questions of the 0th type and 2 questions of the 1st type: 1 + 1 + 2 + 2 = 6
 * - Solve 3 questions of the 0th type and 1 question of the 2nd type: 1 + 1 + 1 + 3 = 6
 * - Solve 1 question of the 0th type, 1 question of the 1st type and 1 question of the 2nd type: 1 + 2 + 3 = 6
 * - Solve 3 questions of the 1st type: 2 + 2 + 2 = 6
 * - Solve 2 questions of the 2nd type: 3 + 3 = 6
 *
 * Example 2:
 *
 * Input: target = 5, types = [[50,1],[50,2],[50,5]]
 * Output: 4
 * Explanation: You can earn 5 points in one of the four ways:
 * - Solve 5 questions of the 0th type: 1 + 1 + 1 + 1 + 1 = 5
 * - Solve 3 questions of the 0th type and 1 question of the 1st type: 1 + 1 + 1 + 2 = 5
 * - Solve 1 questions of the 0th type and 2 questions of the 1st type: 1 + 2 + 2 = 5
 * - Solve 1 question of the 2nd type: 5
 *
 * Example 3:
 *
 * Input: target = 18, types = [[6,1],[3,2],[2,3]]
 * Output: 1
 * Explanation: You can only earn 18 points by answering all questions.
 *
 *
 * Constraints:
 *
 * 1 <= target <= 1000
 * n == types.length
 * 1 <= n <= 50
 * types[i].length == 2
 * 1 <= counti, marksi <= 50
 *
 */
public class NumberOfWaysToEarnPoints {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: BOUNDED KNAPSACK (GROUP KNAPSACK) - FOR EACH TYPE, TRY TAKING 0..count QUESTIONS OF IT
    /**
     * time = O(N * target * count)
     * space = O(target)
     */
    public int waysToReachTarget(int target, int[][] types) {
        // ways[p] = number of ways to score exactly p points with the types processed so far
        long[] ways = new long[target + 1];
        ways[0] = 1;

        for (int[] type : types) {
            int count = type[0];
            int marks = type[1];

            long[] nextWays = new long[target + 1];
            for (int points = 0; points <= target; points++) {
                long total = 0;
                // take `taken` questions of this type; the rest of the points come from earlier types
                for (int taken = 0; taken <= count; taken++) {
                    int remaining = points - taken * marks;
                    if (remaining < 0) {
                        break;
                    }
                    total += ways[remaining];
                }
                nextWays[points] = total % MOD;
            }
            ways = nextWays;
        }
        return (int) ways[target];
    }
}
