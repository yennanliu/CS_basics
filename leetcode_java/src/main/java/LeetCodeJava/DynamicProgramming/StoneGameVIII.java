package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-viii/description/

/**
 * 1872. Stone Game VIII
 * Hard
 *
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * There are n stones arranged in a row. On each player's turn, while the number of stones is more
 * than one, they will do the following:
 *
 * Choose an integer x > 1, and remove the leftmost x stones from the row.
 * Add the sum of the removed stones' values to the player's score.
 * Place a new stone, whose value is equal to that sum, on the left side of the row.
 *
 * The game stops when only one stone is left in the row.
 *
 * The score difference between Alice and Bob is (Alice's score - Bob's score). Alice's goal is to
 * maximize the score difference, and Bob's goal is the minimize the score difference.
 *
 * Given an integer array stones of length n where stones[i] represents the value of the ith stone
 * from the left, return the score difference between Alice and Bob if they both play optimally.
 *
 *
 * Example 1:
 *
 * Input: stones = [-1,2,-3,4,-5]
 * Output: 5
 *
 * Example 2:
 *
 * Input: stones = [7,-6,5,10,5,-2,-6]
 * Output: 13
 *
 * Example 3:
 *
 * Input: stones = [-10,-12]
 * Output: -22
 *
 *
 * Constraints:
 *
 * n == stones.length
 * 2 <= n <= 10^5
 * -10^4 <= stones[i] <= 10^4
 *
 */
public class StoneGameVIII {

    // V0
    // IDEA: GAME DP ON PREFIX SUMS (SUFFIX MAX)
    //
    //   whatever is taken, the merged stone keeps the whole prefix sum, so a move
    //   "take up to index i" scores prefix[i] no matter what happened before.
    //   dp[i] = best score difference for the player to move when the next move
    //           must end at index >= i.
    //     dp[n-1] = prefix[n-1]                       (take everything)
    //     dp[i]   = max(dp[i+1], prefix[i] - dp[i+1]) (skip index i, or end at i)
    //   the first move must take >= 2 stones -> answer dp[1].
    /**
     * time = O(N)
     * space = O(N)
     */
    public int stoneGameVIII(int[] stones) {
        int n = stones.length;

        // prefix[i] = stones[0] + ... + stones[i]
        int[] prefix = new int[n];
        prefix[0] = stones[0];
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + stones[i];
        }

        // best holds dp[i+1] while computing dp[i]
        int best = prefix[n - 1];
        for (int i = n - 2; i >= 1; i--) {
            best = Math.max(best, prefix[i] - best);
        }
        return best;
    }
}
