package LeetCodeJava.Sort;

// https://leetcode.com/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/description/

import java.util.*;

/**
 * 1465. Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
 * Medium
 *
 * You are given a rectangular cake of size h x w and two arrays of integers horizontalCuts
 * and verticalCuts where:
 *
 * horizontalCuts[i] is the distance from the top of the rectangular cake to the ith
 * horizontal cut and similarly, and verticalCuts[j] is the distance from the left of the
 * rectangular cake to the jth vertical cut.
 *
 * Return the maximum area of a piece of cake after you cut at each horizontal and vertical
 * position provided in the arrays horizontalCuts and verticalCuts. Since the answer can be
 * a large number, return this modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: h = 5, w = 4, horizontalCuts = [1,2,4], verticalCuts = [1,3]
 * Output: 4
 *
 * Example 2:
 *
 * Input: h = 5, w = 4, horizontalCuts = [3,1], verticalCuts = [1]
 * Output: 6
 *
 * Example 3:
 *
 * Input: h = 5, w = 4, horizontalCuts = [3], verticalCuts = [3]
 * Output: 9
 *
 *
 * Constraints:
 *
 * 2 <= h, w <= 10^9
 * 1 <= horizontalCuts.length <= min(h - 1, 10^5)
 * 1 <= verticalCuts.length <= min(w - 1, 10^5)
 * 1 <= horizontalCuts[i] < h
 * 1 <= verticalCuts[i] < w
 * All the elements in horizontalCuts are distinct.
 * All the elements in verticalCuts are distinct.
 *
 */
public class MaximumAreaOfAPieceOfCakeAfterHorizontalAndVerticalCuts {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SORT + LARGEST GAP — every piece is (a gap in y) x (a gap in x), and the two
    //       dimensions are independent, so answer = largest horizontal gap * largest
    //       vertical gap. Take the modulo only at the very end.
    /**
     * time = O(M log M + N log N)
     * space = O(M + N)   (sorted copies)
     */
    public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        long maxHeight = largestGap(horizontalCuts, h);
        long maxWidth = largestGap(verticalCuts, w);
        // both gaps can be up to 10^9, so multiply in long before reducing
        return (int) ((maxHeight * maxWidth) % MOD);
    }

    /** Largest distance between consecutive cuts, counting the borders 0 and size. */
    private long largestGap(int[] cuts, int size) {
        int[] sorted = cuts.clone();
        Arrays.sort(sorted);

        // the first slice runs from the border 0 to the first cut
        long best = sorted[0];
        for (int i = 1; i < sorted.length; i++) {
            best = Math.max(best, sorted[i] - sorted[i - 1]);
        }
        // the last slice runs from the last cut to the far border
        best = Math.max(best, size - sorted[sorted.length - 1]);
        return best;
    }
}
