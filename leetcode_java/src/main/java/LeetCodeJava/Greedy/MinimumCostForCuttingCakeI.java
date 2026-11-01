package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-cost-for-cutting-cake-i/description/

import java.util.*;

/**
 * 3218. Minimum Cost for Cutting Cake I
 * Medium
 *
 * There is an m x n cake that needs to be cut into 1 x 1 pieces.
 *
 * You are given integers m, n, and two arrays:
 *
 * horizontalCut of size m - 1, where horizontalCut[i] represents the cost to cut along the horizontal line i.
 * verticalCut of size n - 1, where verticalCut[j] represents the cost to cut along the vertical line j.
 *
 * In one operation, you can choose any piece of cake that is not yet a 1 x 1 square and perform one of the following cuts:
 *
 * Cut along a horizontal line i at a cost of horizontalCut[i].
 * Cut along a vertical line j at a cost of verticalCut[j].
 *
 * After the cut, the piece of cake is divided into two distinct pieces.
 *
 * The cost of a cut depends only on the initial cost of the line and does not change.
 *
 * Return the minimum total cost to cut the entire cake into 1 x 1 pieces.
 *
 *
 * Example 1:
 *
 * Input: m = 3, n = 2, horizontalCut = [1,3], verticalCut = [5]
 * Output: 13
 * Explanation:
 * Perform a cut on the vertical line 0 with cost 5, current total cost is 5.
 * Perform a cut on the horizontal line 0 on 3 x 1 subgrid with cost 1.
 * Perform a cut on the horizontal line 0 on 3 x 1 subgrid with cost 1.
 * Perform a cut on the horizontal line 1 on 2 x 1 subgrid with cost 3.
 * Perform a cut on the horizontal line 1 on 2 x 1 subgrid with cost 3.
 * The total cost is 5 + 1 + 1 + 3 + 3 = 13.
 *
 * Example 2:
 *
 * Input: m = 2, n = 2, horizontalCut = [7], verticalCut = [4]
 * Output: 15
 * Explanation:
 * Perform a cut on the horizontal line 0 with cost 7.
 * Perform a cut on the vertical line 0 on 1 x 2 subgrid with cost 4.
 * Perform a cut on the vertical line 0 on 1 x 2 subgrid with cost 4.
 * The total cost is 7 + 4 + 4 = 15.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 20
 * horizontalCut.length == m - 1
 * verticalCut.length == n - 1
 * 1 <= horizontalCut[i], verticalCut[i] <= 10^3
 *
 */
public class MinimumCostForCuttingCakeI {

    // V0
    // IDEA: GREEDY - EXPENSIVE LINES FIRST; A LINE IS PAID ONCE PER PIECE IT CROSSES
    /**
     *  A horizontal line costs its price times the number of vertical strips
     *  that exist when it is cut (and vice versa). Every line must be cut once,
     *  so cut the most expensive lines first, while they are multiplied by the
     *  fewest pieces: merge the two descending-sorted lists.
     *
     *  time = O(M log M + N log N)
     *  space = O(M + N)
     */
    public int minimumCost(int m, int n, int[] horizontalCut, int[] verticalCut) {
        int[] horizontal = horizontalCut.clone();
        int[] vertical = verticalCut.clone();
        Arrays.sort(horizontal);
        Arrays.sort(vertical);

        // walk both arrays from the largest value down
        int h = horizontal.length - 1;
        int v = vertical.length - 1;
        int rows = 1; // horizontal pieces so far
        int cols = 1; // vertical pieces so far
        int total = 0;

        while (h >= 0 || v >= 0) {
            boolean takeHorizontal = v < 0 || (h >= 0 && horizontal[h] >= vertical[v]);
            if (takeHorizontal) {
                // this line crosses every vertical strip
                total += horizontal[h] * cols;
                rows++;
                h--;
            } else {
                total += vertical[v] * rows;
                cols++;
                v--;
            }
        }
        return total;
    }
}
