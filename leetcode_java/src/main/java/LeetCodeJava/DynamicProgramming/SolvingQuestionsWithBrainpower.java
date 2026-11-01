package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/solving-questions-with-brainpower/description/

/**
 * 2140. Solving Questions With Brainpower
 * Medium
 *
 * You are given a 0-indexed 2D integer array questions where questions[i] = [points_i, brainpower_i].
 *
 * The array describes the questions of an exam, where you have to process the questions in order (i.e., starting from question 0) and make a decision whether to solve or skip each question. Solving question i will earn you points_i points but you will be unable to solve each of the next brainpower_i questions. If you skip question i, you get to make the decision on the next question.
 *
 * For example, given questions = [[3, 2], [4, 3], [4, 4], [2, 5]]:
 * If question 0 is solved, you will earn 3 points but you will be unable to solve questions 1 and 2.
 * If instead, question 0 is skipped and question 1 is solved, you will earn 4 points but you will be unable to solve questions 2 and 3.
 *
 * Return the maximum points you can earn for the exam.
 *
 *
 * Example 1:
 *
 * Input: questions = [[3,2],[4,3],[4,4],[2,5]]
 * Output: 5
 * Explanation: The maximum points can be earned by solving questions 0 and 3.
 * - Solve question 0: Earn 3 points, will be unable to solve the next 2 questions
 * - Unable to solve questions 1 and 2
 * - Solve question 3: Earn 2 points
 * Total points earned: 3 + 2 = 5. There is no other way to earn 5 or more points.
 *
 * Example 2:
 *
 * Input: questions = [[1,1],[2,2],[3,3],[4,4],[5,5]]
 * Output: 7
 * Explanation: The maximum points can be earned by solving questions 1 and 4.
 * - Skip question 0
 * - Solve question 1: Earn 2 points, will be unable to solve the next 2 questions
 * - Unable to solve questions 2 and 3
 * - Solve question 4: Earn 5 points
 * Total points earned: 2 + 5 = 7. There is no other way to earn 7 or more points.
 *
 *
 * Constraints:
 *
 * 1 <= questions.length <= 10^5
 * questions[i].length == 2
 * 1 <= points_i, brainpower_i <= 10^5
 *
 */
public class SolvingQuestionsWithBrainpower {

    // V0
    // IDEA: SUFFIX DP — dp[i] = BEST SCORE FROM QUESTION i ONWARDS (SKIP OR SOLVE)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long mostPoints(int[][] questions) {
        int n = questions.length;

        // dp[i] = max points achievable from question i to the end
        // dp has n + 1 slots so "jumped past the end" reads a clean 0
        long[] dp = new long[n + 1];

        // fill from the back so both lookups are already computed
        for (int i = n - 1; i >= 0; i--) {
            int points = questions[i][0];
            int brainpower = questions[i][1];

            // solving i blocks the next `brainpower` questions
            int nextAllowed = Math.min(n, i + brainpower + 1);

            long skip = dp[i + 1];
            long solve = points + dp[nextAllowed];
            dp[i] = Math.max(skip, solve);
        }
        return dp[0];
    }
}
