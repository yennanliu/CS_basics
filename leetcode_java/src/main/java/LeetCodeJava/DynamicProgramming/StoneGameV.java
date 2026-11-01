package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-v/description/

/**
 * 1563. Stone Game V
 * Hard
 *
 * There are several stones arranged in a row, and each stone has an associated value which is an integer given in the array stoneValue.
 *
 * In each round of the game, Alice divides the row into two non-empty rows (i.e. left row and right row), then Bob calculates the value of each row which is the sum of the values of all the stones in this row. Bob throws away the row which has the maximum value, and Alice's score increases by the value of the remaining row. If the value of the two rows are equal, Bob lets Alice decide which row will be thrown away. The next round starts with the remaining row.
 *
 * The game ends when there is only one stone remaining. Alice's score is initially zero.
 *
 * Return the maximum score that Alice can obtain.
 *
 * Example 1:
 *
 * Input: stoneValue = [6,2,3,4,5,5]
 * Output: 18
 * Explanation: In the first round, Alice divides the row to [6,2,3], [4,5,5]. The left row has the value 11 and the right row has value 14. Bob throws away the right row and Alice's score is now 11.
 * In the second round Alice divides the row to [6], [2,3]. This time Bob throws away the left row and Alice's score becomes 16 (11 + 5).
 * The last round Alice has only one choice to divide the row which is [2], [3]. Bob throws away the right row and Alice's score is now 18 (16 + 2). The game ends because only one stone is remaining in the row.
 *
 * Example 2:
 *
 * Input: stoneValue = [7,7,7,7,7,7,7]
 * Output: 28
 *
 * Example 3:
 *
 * Input: stoneValue = [4]
 * Output: 0
 *
 * Constraints:
 *
 * 1 <= stoneValue.length <= 500
 * 1 <= stoneValue[i] <= 10^6
 *
 */
public class StoneGameV {

    // V0
    // IDEA: INTERVAL DP + PREFIX MAXIMA (O(N^2))
    //       dp[i][j] = best score Alice gets from row i..j.
    //       a split at k (left = i..k, right = k+1..j) keeps the SMALLER half:
    //         left <  right -> left  + dp[i][k]
    //         left >  right -> right + dp[k+1][j]
    //         equal         -> left  + max(dp[i][k], dp[k+1][j])
    //       values are positive, so "left is the smaller half" holds for every split up to some
    //       index `split`, and never after it. Two tables answer each side in O(1):
    //         bestLeft[i][j]  = max over r in i..j of sum(i..r) + dp[i][r]
    //         bestRight[i][j] = max over l in i..j of sum(l..j) + dp[l][j]
    //       and `split` only moves right as j grows, so it is a pointer, not a search.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int stoneGameV(int[] stoneValue) {
        int n = stoneValue.length;
        if (n < 2) {
            return 0;
        }

        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stoneValue[i];
        }

        int[][] dp = new int[n][n];
        int[][] bestLeft = new int[n][n];
        int[][] bestRight = new int[n][n];

        for (int i = n - 1; i >= 0; i--) {
            // a single stone: no move left, dp = 0, so the "sum + dp" tables hold just the value
            bestLeft[i][i] = stoneValue[i];
            bestRight[i][i] = stoneValue[i];

            // split = largest k with 2 * sum(i..k) <= sum(i..j); i - 1 means "none yet"
            int split = i - 1;
            for (int j = i + 1; j < n; j++) {
                int total = prefix[j + 1] - prefix[i];
                while (split + 1 <= j - 1 && 2 * (prefix[split + 2] - prefix[i]) <= total) {
                    split++;
                }

                int best = 0;
                if (split >= i) {
                    // every k < split has a strictly smaller left half
                    if (split > i) {
                        best = bestLeft[i][split - 1];
                    }
                    // the split itself: left is smaller, or the halves are equal
                    int left = prefix[split + 1] - prefix[i];
                    int atSplit;
                    if (2 * left == total) {
                        atSplit = left + Math.max(dp[i][split], dp[split + 1][j]);
                    } else {
                        atSplit = left + dp[i][split];
                    }
                    best = Math.max(best, atSplit);
                }
                // every k > split keeps the right half, which starts at k + 1 >= split + 2
                if (split + 2 <= j) {
                    best = Math.max(best, bestRight[split + 2][j]);
                }
                dp[i][j] = best;

                int withScore = total + best;
                bestLeft[i][j] = Math.max(bestLeft[i][j - 1], withScore);
                bestRight[i][j] = Math.max(bestRight[i + 1][j], withScore);
            }
        }
        return dp[0][n - 1];
    }

    // V1
    // IDEA: PLAIN INTERVAL DP (O(N^3)) — the version to explain first in an interview
    //       dp[i][j] = max over every split k of the smaller half's sum plus dp of that half
    //       (both halves when the sums are equal). Iterate by interval length.
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public int stoneGameV_1(int[] stoneValue) {
        int n = stoneValue.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stoneValue[i];
        }

        int[][] dp = new int[n][n];
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;
                int best = 0;
                for (int k = i; k < j; k++) {
                    int left = prefix[k + 1] - prefix[i];
                    int right = prefix[j + 1] - prefix[k + 1];
                    int score;
                    if (left < right) {
                        score = left + dp[i][k];
                    } else if (left > right) {
                        score = right + dp[k + 1][j];
                    } else {
                        score = left + Math.max(dp[i][k], dp[k + 1][j]);
                    }
                    best = Math.max(best, score);
                }
                dp[i][j] = best;
            }
        }
        return dp[0][n - 1];
    }
}
