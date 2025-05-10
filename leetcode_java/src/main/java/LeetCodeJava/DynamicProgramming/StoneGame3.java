package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-iii/description/
/**
 * 1406. Stone Game III
 * Hard
 * Topics
 * Companies
 * Hint
 * Alice and Bob continue their games with piles of stones. There are several stones arranged in a row, and each stone has an associated value which is an integer given in the array stoneValue.
 *
 * Alice and Bob take turns, with Alice starting first. On each player's turn, that player can take 1, 2, or 3 stones from the first remaining stones in the row.
 *
 * The score of each player is the sum of the values of the stones taken. The score of each player is 0 initially.
 *
 * The objective of the game is to end with the highest score, and the winner is the player with the highest score and there could be a tie. The game continues until all the stones have been taken.
 *
 * Assume Alice and Bob play optimally.
 *
 * Return "Alice" if Alice will win, "Bob" if Bob will win, or "Tie" if they will end the game with the same score.
 *
 *
 *
 * Example 1:
 *
 * Input: stoneValue = [1,2,3,7]
 * Output: "Bob"
 * Explanation: Alice will always lose. Her best move will be to take three piles and the score become 6. Now the score of Bob is 7 and Bob wins.
 * Example 2:
 *
 * Input: stoneValue = [1,2,3,-9]
 * Output: "Alice"
 * Explanation: Alice must choose all the three piles at the first move to win and leave Bob with negative score.
 * If Alice chooses one pile her score will be 1 and the next move Bob's score becomes 5. In the next move, Alice will take the pile with value = -9 and lose.
 * If Alice chooses two piles her score will be 3 and the next move Bob's score becomes 3. In the next move, Alice will take the pile with value = -9 and also lose.
 * Remember that both play optimally so here Alice will choose the scenario that makes her win.
 * Example 3:
 *
 * Input: stoneValue = [1,2,3,6]
 * Output: "Tie"
 * Explanation: Alice cannot win this game. She can end the game in a draw if she decided to choose all the first three piles, otherwise she will lose.
 *
 *
 * Constraints:
 *
 * 1 <= stoneValue.length <= 5 * 104
 * -1000 <= stoneValue[i] <= 1000
 *
 */
public class StoneGame3 {

    // V0
//    public String stoneGameIII(int[] stoneValue) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=HsLG5QW9CFQ
    // https://github.com/neetcode-gh/leetcode/blob/main/kotlin%2F1406-stone-game-iii.kt
    // kotlin
//    fun stoneGameIII(s: IntArray): String {
//        val dp = IntArray(s.size) { Integer.MIN_VALUE }
//
//        fun dfs(i: Int): Int {
//            if (i == s.size)
//                return 0
//            if (dp[i] != Integer.MIN_VALUE)
//                return dp[i]
//
//            var total = 0
//            for (j in i until minOf(i + 3, s.size)) {
//                total += s[j]
//                dp[i] = maxOf(dp[i], total - dfs(j + 1))
//            }
//
//            return dp[i]
//        }
//
//        val sum = dfs(0)
//        return if (sum > 0) "Alice" else if (sum < 0) "Bob" else "Tie"
//    }


    // V2
    // https://leetcode.com/problems/stone-game-iii/solutions/3566457/image-explanation-recursion-memo-bottom-t421z/
    public String stoneGameIII_2(int[] stoneValue) {
        int n = stoneValue.length;
        int[] dp = new int[3];

        for (int i = n - 1; i >= 0; i--) {
            int takeOne = stoneValue[i] - dp[(i + 1) % 3];

            int takeTwo = Integer.MIN_VALUE;
            if (i + 1 < n)
                takeTwo = stoneValue[i] + stoneValue[i + 1] - dp[(i + 2) % 3];

            int takeThree = Integer.MIN_VALUE;
            if (i + 2 < n)
                takeThree = stoneValue[i] + stoneValue[i + 1] + stoneValue[i + 2] - dp[(i + 3) % 3];

            dp[i % 3] = Math.max(Math.max(takeOne, takeTwo), takeThree);
        }

        int value = dp[0];
        if (value > 0)
            return "Alice";
        else if (value < 0)
            return "Bob";
        else
            return "Tie";
    }

    // V3
    // https://leetcode.com/problems/stone-game-iii/solutions/3566303/pythonjavacsimple-solutioneasy-to-unders-p749/
    public String stoneGameIII_3(int[] stoneValue) {
        int n = stoneValue.length;
        int[] dp = new int[3];

        for (int i = n - 1; i >= 0; i--) {
            int takeOne = stoneValue[i] - dp[(i + 1) % 3];
            int takeTwo = Integer.MIN_VALUE;
            if (i + 1 < n) {
                takeTwo = stoneValue[i] + stoneValue[i + 1] - dp[(i + 2) % 3];
            }
            int takeThree = Integer.MIN_VALUE;
            if (i + 2 < n) {
                takeThree = stoneValue[i] + stoneValue[i + 1] + stoneValue[i + 2] - dp[(i + 3) % 3];
            }

            dp[i % 3] = Math.max(takeOne, Math.max(takeTwo, takeThree));
        }

        int scoreDiff = dp[0];
        if (scoreDiff > 0) {
            return "Alice";
        } else if (scoreDiff < 0) {
            return "Bob";
        } else {
            return "Tie";
        }
    }


}
