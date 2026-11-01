package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-vii/description/

/**
 * 1690. Stone Game VII
 * Medium
 *
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * There are n stones arranged in a row. On each player's turn, they can remove either the leftmost
 * stone or the rightmost stone from the row and receive points equal to the sum of the remaining
 * stones' values in the row. The winner is the one with the higher score when there are no stones left
 * to remove.
 *
 * Bob found that he will always lose this game (poor Bob, he always loses), so he decided to minimize
 * the score's difference. Alice's goal is to maximize the difference in the score.
 *
 * Given an array of integers stones where stones[i] represents the value of the ith stone from the
 * left, return the difference in Alice and Bob's score if they both play optimally.
 *
 *
 * Example 1:
 *
 * Input: stones = [5,3,1,4,2]
 * Output: 6
 * Explanation:
 * - Alice removes 2 and gets 5 + 3 + 1 + 4 = 13 points. Alice = 13, Bob = 0, stones = [5,3,1,4].
 * - Bob removes 5 and gets 3 + 1 + 4 = 8 points. Alice = 13, Bob = 8, stones = [3,1,4].
 * - Alice removes 3 and gets 1 + 4 = 5 points. Alice = 18, Bob = 8, stones = [1,4].
 * - Bob removes 1 and gets 4 points. Alice = 18, Bob = 12, stones = [4].
 * - Alice removes 4 and gets 0 points. Alice = 18, Bob = 12, stones = [].
 * The score difference is 18 - 12 = 6.
 *
 * Example 2:
 *
 * Input: stones = [7,90,5,1,100,10,10,2]
 * Output: 122
 *
 *
 * Constraints:
 *
 * n == stones.length
 * 2 <= n <= 1000
 * 1 <= stones[i] <= 1000
 *
 */
public class StoneGameVII {

    // V0
    // IDEA: INTERVAL MINIMAX DP ON THE SCORE DIFFERENCE
    //       dp[i][j] = (current player's score) - (opponent's score) when stones i..j remain.
    //       removing stones[i] scores sum(i+1..j); removing stones[j] scores sum(i..j-1);
    //       then the opponent plays optimally on what is left:
    //         dp[i][j] = max( sum(i+1..j) - dp[i+1][j],  sum(i..j-1) - dp[i][j-1] )
    //       dp[i][i] = 0 (removing the last stone scores nothing).
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int stoneGameVII(int[] stones) {
        int n = stones.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stones[i];
        }

        int[][] dp = new int[n][n];
        // i goes downward so dp[i + 1][j] is ready; j goes upward so dp[i][j - 1] is ready
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                int removeLeft = (prefix[j + 1] - prefix[i + 1]) - dp[i + 1][j];
                int removeRight = (prefix[j] - prefix[i]) - dp[i][j - 1];
                dp[i][j] = Math.max(removeLeft, removeRight);
            }
        }
        return dp[0][n - 1];
    }
}
