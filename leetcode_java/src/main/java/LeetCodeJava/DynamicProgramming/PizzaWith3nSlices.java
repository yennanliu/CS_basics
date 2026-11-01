package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/pizza-with-3n-slices/description/

/**
 * 1388. Pizza With 3n Slices
 * Hard
 *
 * There is a pizza with 3n slices of varying size, you and your friends will take slices
 * of pizza as follows:
 *
 * You will pick any pizza slice.
 * Your friend Alice will pick the next slice in the anti-clockwise direction of your pick.
 * Your friend Bob will pick the next slice in the clockwise direction of your pick.
 * Repeat until there are no more slices of pizzas.
 *
 * Given an integer array slices that represent the sizes of the pizza slices in a
 * clockwise direction, return the maximum possible sum of slice sizes that you can pick.
 *
 *
 * Example 1:
 *
 * Input: slices = [1,2,3,4,5,6]
 * Output: 10
 * Explanation: Pick pizza slice of size 4, Alice and Bob will pick slices with size 3 and 5
 * respectively. Then Pick slices with size 6, finally Alice and Bob will pick slice of size
 * 2 and 1 respectively. Total = 4 + 6.
 *
 * Example 2:
 *
 * Input: slices = [8,9,8,6,1,1]
 * Output: 16
 * Explanation: Pick pizza slice of size 8 in each turn. If you pick slice with size 9 your
 * partners will pick slices of size 8.
 *
 *
 * Constraints:
 *
 * 3 * n == slices.length
 * 1 <= slices.length <= 500
 * 1 <= slices[i] <= 1000
 *
 */
public class PizzaWith3nSlices {

    // V0
    // IDEA: CIRCULAR HOUSE ROBBER (LC 213), BUT TAKING EXACTLY n SLICES
    /**
     * Every slice you take burns its two neighbours, so your picks are pairwise
     * NON-ADJACENT on the circle, and you end up with exactly n = len / 3 of them.
     * -> maximise the sum of exactly n non-adjacent elements on a circle.
     *
     * The circle: slices[0] and slices[len-1] are adjacent, so at most one is taken.
     * Solve the linear problem on slices[0..len-2] and on slices[1..len-1]; take the better.
     *
     * linear DP:
     *   best[i][j] = best sum using the first i elements and taking j of them
     *   best[i][j] = max( best[i-1][j],                  // skip element i
     *                     best[i-2][j-1] + nums[i-1] )   // take it; element i-1 is blocked
     */
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int maxSizeSlices(int[] slices) {
        int length = slices.length;
        int picks = length / 3;
        // the first slice may be taken, the last may not ...
        int withoutLast = bestNonAdjacent(slices, 0, length - 2, picks);
        // ... or the last slice may be taken, the first may not
        int withoutFirst = bestNonAdjacent(slices, 1, length - 1, picks);
        return Math.max(withoutLast, withoutFirst);
    }

    /** Max sum of `picks` pairwise non-adjacent elements of slices[from..to] (a line, not a circle). */
    private int bestNonAdjacent(int[] slices, int from, int to, int picks) {
        int m = to - from + 1;
        int[][] best = new int[m + 1][picks + 1];

        for (int i = 1; i <= m; i++) {
            int value = slices[from + i - 1];
            for (int j = 1; j <= picks; j++) {
                int skip = best[i - 1][j];
                // taking element i means element i-1 is not taken, so look two back
                int take = (i >= 2 ? best[i - 2][j - 1] : 0) + value;
                best[i][j] = Math.max(skip, take);
            }
        }
        return best[m][picks];
    }
}
