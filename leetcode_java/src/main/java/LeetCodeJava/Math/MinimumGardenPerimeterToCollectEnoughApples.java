package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-garden-perimeter-to-collect-enough-apples/description/

/**
 * 1954. Minimum Garden Perimeter to Collect Enough Apples
 * Medium
 *
 * In a garden represented as an infinite 2D grid, there is an apple tree planted at every integer coordinate. The apple tree planted at an integer coordinate (i, j) has |i| + |j| apples growing on it.
 *
 * You will buy an axis-aligned square plot of land that is centered at (0, 0).
 *
 * Given an integer neededApples, return the minimum perimeter of a plot such that at least neededApples apples are inside or on the perimeter of that plot.
 *
 * The value of |x| is defined as:
 *
 * x if x >= 0
 * -x if x < 0
 *
 *
 * Example 1:
 *
 * Input: neededApples = 1
 * Output: 8
 * Explanation: A square plot of side length 1 does not contain any apples.
 * However, a square plot of side length 2 has 12 apples inside (as depicted in the image above).
 * The perimeter is 2 * 4 = 8.
 *
 * Example 2:
 *
 * Input: neededApples = 13
 * Output: 16
 *
 * Example 3:
 *
 * Input: neededApples = 1000000000
 * Output: 5040
 *
 *
 * Constraints:
 *
 * 1 <= neededApples <= 10^15
 *
 */
public class MinimumGardenPerimeterToCollectEnoughApples {

    private static final long MAX_HALF_SIDE = 100_000L;

    // V0
    // IDEA: CLOSED-FORM APPLE COUNT + BINARY SEARCH ON THE HALF-SIDE
    //  a plot spanning [-x, x] holds apples(x) = 2 * x * (x + 1) * (2x + 1); perimeter = 8x.
    //  apples(10^5) ~ 4 * 10^15 >= the max neededApples, so it bounds the search.
    /**
     * time = O(log X)
     * space = O(1)
     */
    public long minimumPerimeter(long neededApples) {
        long left = 1;
        long right = MAX_HALF_SIDE;
        // find the smallest x with apples(x) >= neededApples
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (apples(mid) >= neededApples) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return 8 * left;
    }

    /** Apples inside the square plot [-x, x] x [-x, x]. */
    private long apples(long x) {
        return 2 * x * (x + 1) * (2 * x + 1);
    }
}
