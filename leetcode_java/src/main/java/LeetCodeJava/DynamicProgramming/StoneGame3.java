package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stone-game-iii/description/
// https://leetcode.cn/problems/stone-game-iii/
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

    // V0-1
    // IDEA: 1D DP (gemini)
    /** NOTE !!!
     *
     *   1. DP def:
     *
     *     dp[i] =
     *        the maximum relative score
     *        the current player can get
     *        `starting from index i to the end`
     *        of the array.
     *
     */
    /**
     * In **LC 1406 (Stone Game III)**,
     * the game becomes more complex because a
     * player can pick **1, 2, or 3** stones at a time.
     * This is a "take-away" game where we want to
     * find the   `maximum relative score ($Alice - Bob$).`
     *
     * ### 💡 The Strategy: Bottom-Up DP
     * We'll define **
     *
     *  `dp[i]`** as the maximum relative score the
     *  current player can get starting from index
     *  `i` to the end of the array.
     *
     * * At index `i`, the player has 3 choices:
     *     1.  Take `stoneValue[i]`, then the other player gets `dp[i+1]`.
     *     2.  Take `stoneValue[i] + stoneValue[i+1]`, then the other player gets `dp[i+2]`.
     *     3.  Take `stoneValue[i] + stoneValue[i+1] + stoneValue[i+2]`, then the other player gets `dp[i+3]`.
     * * The goal is to **maximize** $( \text{current\_pick} - \text{opponent's\_relative\_score} )$.
     *
     * ---
     *
     * ### 🔍 Senior Engineering Review
     * * **Space Optimization**: Since `dp[i]` only depends on `dp[i+1]`, `dp[i+2]`, and `dp[i+3]`, you could reduce the space from **$O(N)$** to **$O(1)$** by using 4 variables. However, in an interview, the $O(N)$ array is often preferred for clarity unless space is explicitly constrained.
     * * **The "Subtraction" Insight**: In minimax games, subtracting the opponent's score is a powerful pattern. It avoids having to track `alice_score` and `bob_score` separately, which simplifies the transition from 3 choices to just one `Math.max()` call.
     * * **Integer.MIN_VALUE**: Because stone values can be negative (e.g., $[-1, -2, -3]$), you must initialize `dp[i]` to `Integer.MIN_VALUE` rather than `0` to ensure you don't pick a "default" zero incorrectly.
     *
     * ### 📊 Complexity Analysis
     *
     * | Metric | Complexity | Explanation |
     * | :--- | :--- | :--- |
     * | **Time** | **$O(N)$** | We make one pass through the stones. The inner loop runs at most 3 times ($O(1)$). |
     * | **Space** | **$O(N)$** | An array of size $N+1$ (can be $O(1)$ with variables). |
     *
     *
     */
    public String stoneGameIII_0_1(int[] stoneValue) {
        int n = stoneValue.length;
        // dp[i] = max relative score the player starting at index i can get
        // We use n + 1 to handle the boundary case for the last few stones
        int[] dp = new int[n + 1];

        // Base case: no stones left, score is 0
        dp[n] = 0;

        // Fill the DP table backwards
        for (int i = n - 1; i >= 0; i--) {
            // Initialize with a very small number
            int take = 0;
            dp[i] = Integer.MIN_VALUE;

            // Try picking 1, 2, or 3 stones
            for (int k = 0; k < 3 && i + k < n; k++) {
                take += stoneValue[i + k];
                // Relative score = (current sum) - (max relative score of opponent)
                dp[i] = Math.max(dp[i], take - dp[i + k + 1]);
            }
        }

        // Alice starts at index 0. Check her relative score against Bob's (0)
        if (dp[0] > 0)
            return "Alice";
        if (dp[0] < 0)
            return "Bob";
        return "Tie";
    }


    // V0-2
    // IDEA: 1D DP (GPT)
    public String stoneGameIII_0_2(int[] stoneValue) {
        int n = stoneValue.length;
        int[] dp = new int[n + 1]; // dp[i] = max diff starting at i

        // dp[n] = 0 (no stones left)

        for (int i = n - 1; i >= 0; i--) {
            int sum = 0;
            dp[i] = Integer.MIN_VALUE;

            for (int k = 0; k < 3 && i + k < n; k++) {
                sum += stoneValue[i + k];
                dp[i] = Math.max(dp[i], sum - dp[i + k + 1]);
            }
        }

        if (dp[0] > 0)
            return "Alice";
        if (dp[0] < 0)
            return "Bob";
        return "Tie";
    }


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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
