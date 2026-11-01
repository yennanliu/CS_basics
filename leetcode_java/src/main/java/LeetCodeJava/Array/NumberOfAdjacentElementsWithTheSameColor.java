package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-adjacent-elements-with-the-same-color/description/

/**
 * 2672. Number of Adjacent Elements With the Same Color
 * Medium
 *
 * You are given an integer n representing an array colors of length n where all elements are set to 0's meaning uncolored. You are also given a 2D integer array queries where queries[i] = [indexi, colori]. For the ith query:
 *
 * Set colors[indexi] to colori.
 * Count the number of adjacent pairs in colors which have the same color (regardless of colori).
 *
 * Return an array answer of the same length as queries where answer[i] is the answer to the ith query.
 *
 *
 * Example 1:
 *
 * Input: n = 4, queries = [[0,2],[1,2],[3,1],[1,1],[2,1]]
 * Output: [0,1,1,0,2]
 * Explanation:
 * Initially array colors = [0,0,0,0], where 0 denotes uncolored elements of the array.
 * After the 1st query colors = [2,0,0,0]. The count of adjacent pairs with the same color is 0.
 * After the 2nd query colors = [2,2,0,0]. The count of adjacent pairs with the same color is 1.
 * After the 3rd query colors = [2,2,0,1]. The count of adjacent pairs with the same color is 1.
 * After the 4th query colors = [2,1,0,1]. The count of adjacent pairs with the same color is 0.
 * After the 5th query colors = [2,1,1,1]. The count of adjacent pairs with the same color is 2.
 *
 * Example 2:
 *
 * Input: n = 1, queries = [[0,100000]]
 * Output: [0]
 * Explanation:
 * After the 1st query colors = [100000]. The count of adjacent pairs with the same color is 0.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 0 <= indexi <= n - 1
 * 1 <= colori <= 10^5
 *
 */
public class NumberOfAdjacentElementsWithTheSameColor {

    // V0
    // IDEA: INCREMENTAL COUNTER (only the 2 pairs touching the painted index change)
    //       recounting n - 1 pairs per query is O(N * Q). painting index i only affects the
    //       pair on its left and the pair on its right, so:
    //         1) remove the old colour's matches with its neighbours
    //         2) paint
    //         3) add the new colour's matches with its neighbours
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public int[] colorTheArray(int n, int[][] queries) {
        // 0 means uncoloured; real colours are >= 1
        int[] colors = new int[n];
        int[] answer = new int[queries.length];
        int samePairs = 0;

        for (int q = 0; q < queries.length; q++) {
            int index = queries[q][0];
            int newColor = queries[q][1];
            int oldColor = colors[index];

            // step 1: an uncoloured cell never formed a pair, so skip it
            if (oldColor != 0) {
                if (index > 0 && colors[index - 1] == oldColor) {
                    samePairs--;
                }
                if (index < n - 1 && colors[index + 1] == oldColor) {
                    samePairs--;
                }
            }

            // step 2
            colors[index] = newColor;

            // step 3: newColor >= 1, so it can never match an uncoloured neighbour
            if (index > 0 && colors[index - 1] == newColor) {
                samePairs++;
            }
            if (index < n - 1 && colors[index + 1] == newColor) {
                samePairs++;
            }

            answer[q] = samePairs;
        }
        return answer;
    }
}
