package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-to-separate-sentence-into-rows/description/
/**
 * 2052. Minimum Cost to Separate Sentence Into Rows
 * Medium
 *
 * You are given a string sentence containing words separated by spaces, and an integer k.
 * Your task is to separate sentence into rows where the number of characters in each row
 * is at most k. You may assume that sentence does not begin or end with a space, and the
 * words in sentence are separated by a single space.
 *
 * You can split sentence into rows by inserting line breaks between words in sentence. A
 * word cannot be split between two rows. Each word must be used exactly once, and the
 * word order cannot be rearranged. Adjacent words in a row should be separated by a
 * single space, and rows should not begin or end with spaces.
 *
 * The cost of a row with length n is (k - n)^2, and the total cost is the sum of the
 * costs for all rows except the last one.
 *
 * For example if sentence = "i love leetcode" and k = 12:
 * Separating sentence into "i", "love", and "leetcode" has a cost of
 * (12 - 1)^2 + (12 - 4)^2 = 185.
 * Separating sentence into "i love", and "leetcode" has a cost of (12 - 6)^2 = 36.
 * Separating sentence into "i", and "love leetcode" is not possible because the length of
 * "love leetcode" is greater than k.
 *
 * Return the minimum possible total cost of separating sentence into rows.
 *
 *
 * Example 1:
 *
 * Input: sentence = "i love leetcode", k = 12
 * Output: 36
 *
 * Example 2:
 *
 * Input: sentence = "apples and bananas taste great", k = 7
 * Output: 21
 * Explanation: Separating sentence into "apples", "and", "bananas", "taste", and "great"
 * has a cost of (7 - 6)^2 + (7 - 3)^2 + (7 - 7)^2 + (7 - 5)^2 = 21.
 *
 * Example 3:
 *
 * Input: sentence = "a", k = 5
 * Output: 0
 * Explanation: The cost of the last row is not included in the total cost, and since
 * there is only one row, return 0.
 *
 *
 * Constraints:
 *
 * 1 <= sentence.length <= 5000
 * 1 <= k <= 5000
 * The length of each word in sentence is at most k.
 * sentence consists of only lowercase English letters and spaces.
 * sentence does not begin or end with a space.
 * Words in sentence are separated by a single space.
 *
 */
public class MinimumCostToSeparateSentenceIntoRows {

    // V0
    // IDEA: PREFIX SUM OF WORD LENGTHS + BOTTOM-UP DP (WORD WRAP WITH SQUARED SLACK)
    /**
     *  prefixLen[i] = total length of the first i words, so a row holding words [i, j)
     *  is  rowLen = prefixLen[j] - prefixLen[i] + (j - i - 1)  characters
     *  (the (j - i - 1) term counts the single spaces between them).
     *
     *  DP def:
     *     - cost[i] = min cost of laying out words i..n-1
     *
     *  DP eq:
     *     cost[i] = 0                                     if words i..n-1 fit in one row
     *                                                     (the last row is free)
     *     cost[i] = min over feasible j of cost[j] + (k - rowLen(i, j))^2
     *
     *  Bottom-up (i from n-1 down to 0) avoids deep recursion on ~2500 words.
     *
     *  time = O(W^2), W = number of words
     *  space = O(W)
     */
    public int minimumCost(String sentence, int k) {
        String[] words = sentence.split(" ");
        int n = words.length;

        int[] prefixLen = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixLen[i + 1] = prefixLen[i] + words[i].length();
        }

        long[] cost = new long[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            // can everything from word i onward sit on the (free) last row?
            int restLen = prefixLen[n] - prefixLen[i] + (n - i - 1);
            if (restLen <= k) {
                cost[i] = 0;
                continue;
            }

            long best = Long.MAX_VALUE;
            // the current row holds words [i, j); j < n because the rest does not fit
            for (int j = i + 1; j < n; j++) {
                int rowLen = prefixLen[j] - prefixLen[i] + (j - i - 1);
                if (rowLen > k) {
                    break; // adding more words only makes the row longer
                }
                long slack = k - rowLen;
                best = Math.min(best, cost[j] + slack * slack);
            }
            cost[i] = best;
        }
        return (int) cost[0];
    }
}
