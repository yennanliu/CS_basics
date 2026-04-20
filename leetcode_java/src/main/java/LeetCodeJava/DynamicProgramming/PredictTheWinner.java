package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/predict-the-winner/description/
/**
 *  486. Predict the Winner
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer array nums. Two players are playing a game with this array: player 1 and player 2.
 *
 * Player 1 and player 2 take turns, with player 1 starting first. Both players start the game with a score of 0. At each turn, the player takes one of the numbers from either end of the array (i.e., nums[0] or nums[nums.length - 1]) which reduces the size of the array by 1. The player adds the chosen number to their score. The game ends when there are no more elements in the array.
 *
 * Return true if Player 1 can win the game. If the scores of both players are equal, then player 1 is still the winner, and you should also return true. You may assume that both players are playing optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,2]
 * Output: false
 * Explanation: Initially, player 1 can choose between 1 and 2.
 * If he chooses 2 (or 1), then player 2 can choose from 1 (or 2) and 5. If player 2 chooses 5, then player 1 will be left with 1 (or 2).
 * So, final score of player 1 is 1 + 2 = 3, and player 2 is 5.
 * Hence, player 1 will never be the winner and you need to return false.
 * Example 2:
 *
 * Input: nums = [1,5,233,7]
 * Output: true
 * Explanation: Player 1 first chooses 1. Then player 2 has to choose between 5 and 7. No matter which number player 2 choose, player 1 can choose 233.
 * Finally, player 1 has more score (234) than player 2 (12), so you need to return True representing player1 can win.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 107
 *
 */
public class PredictTheWinner {

    // V0
    // IDEA: 2D DP (fixed by GPT)
    public boolean predictTheWinner(int[] nums) {

        int n = nums.length;
        /** NOTE !!!
         *
         *  DP def:
         *
         *
         * dp[i][j] =
         *    max score difference current player can get from nums[i..j]
         *
         */
        int[][] dp = new int[n][n];

        // base case
        /** NOTE !!!
         *
         *  init: dp[i][i] = nums[i];
         */
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        /**  NOTE !!!
         *
         *  1. 2 LOOP
         *
         *  2.  1st loop: len starts from 2
         *  3.  2nd loop: i starts from 0
         *
         *  4.  j = i + len - 1;
         *
         *
         *  ----------
         *
         *  DP eq:
         *
         *
         *  dp[i][j] = max(
         *     nums[i] - dp[i+1][j],   // take left
         *     nums[j] - dp[i][j-1]    // take right
         * )
         *
         *
         */
        // fill DP (length from 2 to n)
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                /**  NOTE !!!
                 *
                 *   get j via `adding extra idx to i idx`
                 *   -> i + (len - 1)
                 *
                 *   (len - 1) is the `extra idx`
                 *
                 */
                int j = i + len - 1;

                dp[i][j] = Math.max(
                        nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        /**  NOTE !!!
         *
         *  the [0, n-1] max diff score is the max diff score play 1 can get,
         *  and if diff_score >= 0, play 1 can win.
         */
        return dp[0][n - 1] >= 0;
    }


    // V0-0-0-0-1
    // IDEA: 2D DP (fixed by gemini)
    public boolean predictTheWinner_0_0_0_0_1(int[] nums) {
        int n = nums.length;
        // 1. Edge case: If there's only one number, P1 takes it and wins.
        if (n <= 1)
            return true;

        /** NOTE !!!
         *
         * dp[i][j] =
         *    max score difference current player can get from nums[i..j]
         *
         */
        // 2. dp[i][j] = max relative score from subarray nums[i...j]
        int[][] dp = new int[n][n];

        // 3. Base Case: Subarrays of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // 4. Fill the table for lengths 2 up to n
        // We move i from bottom up to ensure dp[i+1] is always ready
        /** NOTE !!!
         *
         *   2 loops
         *
         *   i starts from `n-2`
         *   j starts from i+1
         */
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {

                /** NOTE !!!
                 *
                 * dp[i][j] = max(
                 *     nums[i] - dp[i+1][j],   // take left
                 *     nums[j] - dp[i][j-1]    // take right
                 * )
                 */
                // Choice A: Take nums[i],
                // subtract the opponent's best relative score from the rest
                int pickLeft = nums[i] - dp[i + 1][j];

                // Choice B: Take nums[j],
                // subtract the opponent's best relative score from the rest
                int pickRight = nums[j] - dp[i][j - 1];

                
                dp[i][j] = Math.max(pickLeft, pickRight);
            }
        }

        // 5. If the relative score for the full array is >= 0, P1 wins or ties
        return dp[0][n - 1] >= 0;
    }


    // V0-0-1
    // IDEA: 2D DP (fixed by gemini)
    /**
     *  NOTE !!!
     *
     *   1. dp[i][j] =
     *
     *      maximum score difference (current player − opponent)
     *      when playing optimally on subarray nums[i..j]
     *      -> So it’s a relative score, not an absolute score.
     *
     *   2.
     *
     *    dp[i][j] = max(
     *     nums[i] - dp[i+1][j],
     *     nums[j] - dp[i][j-1]
     *   )
     *
     */
    public boolean predictTheWinner_0_0_1(int[] nums) {
        int n = nums.length;
        if (n <= 1)
            return true;

        // dp[i][j] is the max relative score a player can get from nums[i...j]
        int[][] dp = new int[n][n];

        // Base case: only one number left, the player takes it
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // Fill the table for lengths 2 up to n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // Option 1: Take nums[i], then the other player gets dp[i+1][j]
                // Option 2: Take nums[j], then the other player gets dp[i][j-1]
                // We subtract the other player's max relative score from our pick
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }


    // V0-1
    // IDEA: Explicit score track + 2D DP + custom class (fixed by gpt)
    class Pair {
        int first; // current player's score
        int second; // opponent's score

        Pair(int f, int s) {
            first = f;
            second = s;
        }
    }


    public boolean predictTheWinner_0_1(int[] nums) {
        int n = nums.length;
        Pair[][] dp = new Pair[n][n];

        // base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = new Pair(nums[i], 0);
        }

        // fill DP
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // pick left
                int leftFirst = nums[i] + dp[i + 1][j].second;
                int leftSecond = dp[i + 1][j].first;

                // pick right
                int rightFirst = nums[j] + dp[i][j - 1].second;
                int rightSecond = dp[i][j - 1].first;

                if (leftFirst > rightFirst) {
                    dp[i][j] = new Pair(leftFirst, leftSecond);
                } else {
                    dp[i][j] = new Pair(rightFirst, rightSecond);
                }
            }
        }

        Pair res = dp[0][n - 1];
        return res.first >= res.second;
    }


    // V1-1
    // IDEA: 1D DP (fixed by GPT)
    /** NOTE !!!
     *
     * -  dp[i][j] = max score difference for nums[i..j]
     */
    public boolean predictTheWinner_1_1(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];

        // base case: dp[i][i] = nums[i]
        for (int i = 0; i < n; i++) {
            dp[i] = nums[i];
        }

        // fill from bottom up
        /** NOTE !!!
         *
         * iterate i backward, j forward
         *
         *  e.g.:
         *    i: small -> big
         *    j: big -> small
         */
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[j] = Math.max(
                        nums[i] - dp[j], // dp[i+1][j]
                        nums[j] - dp[j - 1] // dp[i][j-1]
                );
            }
        }

        return dp[n - 1] >= 0;
    }


    // V1-2
    // IDEA: 1D DP (fixed by gemini)
    public boolean predictTheWinner_1_2(int[] nums) {
        int n = nums.length;
        if (n <= 1)
            return true;

        // dp[j] will eventually store the max relative score for nums[i...j]
        int[] dp = new int[n];

        // Base case: For a subarray of length 1 (where i == j),
        // the max score is just the number itself.
        for (int i = 0; i < n; i++) {
            dp[i] = nums[i];
        }

        // Fill the array from bottom to top (i decreases)
        // and from left to right (j increases)
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                // Here, dp[j] (on the right side) is the value from the
                // previous i-iteration (representing dp[i+1][j]).
                // dp[j-1] is the value we just updated in this i-iteration
                // (representing dp[i][j-1]).
                dp[j] = Math.max(nums[i] - dp[j],
                        nums[j] - dp[j - 1]);
            }
        }

        return dp[n - 1] >= 0;
    }


    // V2
    // IDEA: 2D DP (fixed by gemini)
    /**
     *  NOTE !!!
     *
     *   1. dp[i][j] = max score difference from nums[i..j]
     *
     *   2.
     *
     *    dp[i][j] = max(
     *     nums[i] - dp[i+1][j],
     *     nums[j] - dp[i][j-1]
     *   )
     *
     */
    public boolean predictTheWinner_2(int[] nums) {
        int n = nums.length;
        if (n <= 1)
            return true;

        // dp[i][j] is the max relative score a player can get from nums[i...j]
        int[][] dp = new int[n][n];

        // Base case: only one number left, the player takes it
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // Fill the table for lengths 2 up to n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // Option 1: Take nums[i], then the other player gets dp[i+1][j]
                // Option 2: Take nums[j], then the other player gets dp[i][j-1]
                // We subtract the other player's max relative score from our pick
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }


    // V3
    // IDEA: 2D DP (fixed by GPT)
    public boolean predictTheWinner_3(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];

        // base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // fill dp
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Math.max(
                        nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }


    // V3




}
