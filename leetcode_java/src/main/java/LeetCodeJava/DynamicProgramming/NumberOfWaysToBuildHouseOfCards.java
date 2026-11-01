package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-build-house-of-cards/description/

/**
 * 2189. Number of Ways to Build House of Cards
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given an integer n representing the number of playing cards you have. A house of cards meets the following conditions:
 *
 * A house of cards consists of one or more rows of triangles and horizontal cards.
 * Triangles are created by leaning two cards against each other.
 * One card must be placed horizontally between all adjacent triangles in a row.
 * Any triangle on a row higher than the first must be placed on a horizontal card from the previous row.
 * Each triangle is placed in the leftmost available spot in the row.
 *
 * Return the number of distinct house of cards you can build using all n cards.
 *
 * Two houses of cards are considered distinct if there exists a row where the two houses contain a different number of cards.
 *
 *
 * Example 1:
 *
 * Input: n = 16
 * Output: 2
 * Explanation: The two valid houses of cards are shown.
 * The third house of cards in the diagram is not valid because the rightmost triangle on the top row is not placed on top of a horizontal card.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: The one valid house of cards is shown.
 *
 * Example 3:
 *
 * Input: n = 4
 * Output: 0
 * Explanation: The three houses of cards in the diagram are not valid.
 * The first house of cards needs a horizontal card placed between the two triangles.
 * The second house of cards uses 5 cards.
 * The third house of cards uses 2 cards.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 *
 */
public class NumberOfWaysToBuildHouseOfCards {

    // V0
    // IDEA: 0/1 KNAPSACK COUNT — A ROW OF k TRIANGLES COSTS 3k - 1 CARDS AND ROW SIZES ARE DISTINCT
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int houseOfCards(int n) {
        // k triangles = 2k leaning cards + (k - 1) horizontal cards = 3k - 1
        // the row above k' triangles holds at most k' - 1, so a house is a SET of distinct k
        // dp[total] = number of ways to spend exactly `total` cards
        int[] dp = new int[n + 1];
        dp[0] = 1;

        for (int k = 1; 3 * k - 1 <= n; k++) {
            int cost = 3 * k - 1;
            // iterate capacity downward so each row size is used at most once
            for (int total = n; total >= cost; total--) {
                dp[total] += dp[total - cost];
            }
        }
        return dp[n];
    }
}
