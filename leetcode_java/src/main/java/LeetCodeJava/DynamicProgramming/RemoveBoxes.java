package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/remove-boxes/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    // V1
    // IDEA: BOTTOM-UP 3D TABLE
    /**
     *  The same (i, j, k) state filled iteratively by increasing interval length,
     *  so there is no recursion and no memo-hit branch.
     *
     *  NOTE !!! V0 relies on `memo[i][j][k] != 0` meaning `already computed`, which
     *           happens to be safe only because every real answer is positive. The
     *           bottom-up fill has no such implicit sentinel.
     *
     *  time  = O(n^4)
     *  space = O(n^3)
     */
    public int removeBoxes_1(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n];

        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                for (int k = 0; k <= i; k++) {
                    // option 1 : remove boxes[i] with its k attached partners
                    int res = (k + 1) * (k + 1) + (i + 1 <= j ? dp[i + 1][j][0] : 0);
                    // option 2 : clear the middle so a later same-coloured box joins
                    for (int m = i + 1; m <= j; m++) {
                        if (boxes[m] != boxes[i]) {
                            continue;
                        }
                        int mid = (i + 1 <= m - 1) ? dp[i + 1][m - 1][0] : 0;
                        res = Math.max(res, mid + dp[m][j][k + 1]);
                    }
                    dp[i][j][k] = res;
                }
            }
        }
        return n == 0 ? 0 : dp[0][n - 1][0];
    }

    // V2
    // IDEA: RUN-LENGTH COMPRESSION FIRST
    /**
     *  Collapse the input into (colour, count) runs before running the DP, so the
     *  `merge equal neighbours` normalisation V0 does inside the recursion happens
     *  ONCE up front.
     *
     *  On inputs with long runs this shrinks n dramatically, and n^4 is very
     *  sensitive to n.
     *
     *  time  = O(r^4), r = number of runs
     *  space = O(r^3)
     */
    private int[] colour;
    private int[] count;
    private Integer[][][] memoRun;

    public int removeBoxes_2(int[] boxes) {
        int n = boxes.length;
        if (n == 0) {
            return 0;
        }
        List<int[]> runs = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && boxes[j] == boxes[i]) {
                j += 1;
            }
            runs.add(new int[] { boxes[i], j - i });
            i = j;
        }

        int r = runs.size();
        colour = new int[r];
        count = new int[r];
        for (int t = 0; t < r; t++) {
            colour[t] = runs.get(t)[0];
            count[t] = runs.get(t)[1];
        }
        memoRun = new Integer[r][r][n + 1];
        return solveRun(0, r - 1, 0);
    }

    private int solveRun(int i, int j, int extra) {
        if (i > j) {
            return 0;
        }
        if (memoRun[i][j][extra] != null) {
            return memoRun[i][j][extra];
        }

        int total = count[i] + extra;
        int res = total * total + solveRun(i + 1, j, 0);

        for (int m = i + 1; m <= j; m++) {
            if (colour[m] != colour[i]) {
                continue;
            }
            res = Math.max(res, solveRun(i + 1, m - 1, 0) + solveRun(m, j, total));
        }

        memoRun[i][j][extra] = res;
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE over every removable group (tiny inputs only)
    /**
     *  Enumerate every maximal same-colour group, remove it, and recurse on the
     *  shortened array.
     *
     *  Exponential, so it only finishes for very short inputs -- but it performs
     *  the operation the STATEMENT describes, which is what validates the
     *  `k attached boxes` state that makes the DP work.
     *
     *  time  = exponential
     *  space = O(n) recursion depth
     */
    public int removeBoxes_3(int[] boxes) {
        return bruteRemove(boxes, new HashMap<>());
    }

    private int bruteRemove(int[] boxes, Map<String, Integer> memo) {
        if (boxes.length == 0) {
            return 0;
        }
        String key = Arrays.toString(boxes);
        Integer cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int best = 0;
        int i = 0;
        while (i < boxes.length) {
            int j = i;
            while (j < boxes.length && boxes[j] == boxes[i]) {
                j += 1;
            }
            int len = j - i;
            int[] rest = new int[boxes.length - len];
            System.arraycopy(boxes, 0, rest, 0, i);
            System.arraycopy(boxes, j, rest, i, boxes.length - j);
            best = Math.max(best, len * len + bruteRemove(rest, memo));
            i = j;
        }

        memo.put(key, best);
        return best;
    }

}
