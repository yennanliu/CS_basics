package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/remove-boxes/description/
/**
 * 546. Remove Boxes
 * Hard
 *
 * You are given several boxes with different colors represented by different positive
 * numbers.
 *
 * You may experience several rounds to remove boxes until there is no box left.
 * Each time you can choose some continuous boxes with the same color (i.e., composed of
 * k boxes, k >= 1), remove them and get k * k points.
 *
 * Return the maximum points you can get.
 *
 * Example 1:
 *
 * Input: boxes = [1,3,2,2,2,3,4,3,1]
 * Output: 23
 * Explanation:
 * [1, 3, 2, 2, 2, 3, 4, 3, 1]
 * ----> [1, 3, 3, 4, 3, 1] (3*3=9 points)
 * ----> [1, 3, 3, 3, 1] (1*1=1 points)
 * ----> [1, 1] (3*3=9 points)
 * ----> [] (2*2=4 points)
 *
 * Example 2:
 *
 * Input: boxes = [1,1,1]
 * Output: 9
 *
 * Example 3:
 *
 * Input: boxes = [1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= boxes.length <= 100
 * 1 <= boxes[i] <= 100
 *
 */
public class RemoveBoxes {

    // V0
    // IDEA: 3D DP (top down + memo)
    /**
     *  DP def:
     *    - dp(i, j, k) = max points for boxes[i..j], PLUS k EXTRA boxes with the same
     *      color as boxes[i] already GLUED IN FRONT of boxes[i]
     *      (the extra k boxes come from the left part that was already cleared away)
     *
     *    NOTE !!! the 3rd state k is REQUIRED because `how many same colored boxes are
     *             attached` changes the score -- k * k is NOT additive.
     *
     *  DP eq:
     *    - option 1 : remove boxes[i] TOGETHER with the k attached ones now
     *         (k + 1)^2 + dp(i + 1, j, 0)
     *    - option 2 : KEEP them, and first clear boxes[i+1..m-1] so that boxes[m]
     *                 (same color as boxes[i]) becomes ADJACENT to the group
     *         dp(i + 1, m - 1, 0) + dp(m, j, k + 1)
     *         for each m in [i+1, j] with boxes[m] == boxes[i]
     *
     *  time  = O(n^4)
     *  space = O(n^3)
     */

    private int[] boxes;
    private int[][][] memo;

    public int removeBoxes(int[] boxes) {
        int n = boxes.length;
        this.boxes = boxes;
        this.memo = new int[n][n][n];
        return dp(0, n - 1, 0);
    }

    private int dp(int i, int j, int k) {
        if (i > j) {
            return 0;
        }

        /** NOTE !!!
         *
         *  merge the same colored boxes right after i into the attached group
         *  -> this NORMALIZES the state, which massively improves the memo hit rate
         */
        while (i < j && boxes[i + 1] == boxes[i]) {
            i += 1;
            k += 1;
        }

        if (memo[i][j][k] != 0) {
            return memo[i][j][k];
        }

        // option 1 : remove the whole leading group NOW
        int res = (k + 1) * (k + 1) + dp(i + 1, j, 0);

        // option 2 : clear the middle part first, so a later same colored box joins
        for (int m = i + 1; m <= j; m++) {
            if (boxes[m] == boxes[i]) {
                res = Math.max(res, dp(i + 1, m - 1, 0) + dp(m, j, k + 1));
            }
        }

        memo[i][j][k] = res;
        return res;
    }

}
