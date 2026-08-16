package LeetCodeJava.Math;

// https://leetcode.com/problems/construct-the-rectangle/description/
/**
 * 492. Construct the Rectangle
 * Easy
 *
 * A web developer needs to know how to design a web page's size. So, given a specific
 * rectangular web page's area, your job by now is to design a rectangular web page,
 * whose length L and width W satisfy the following requirements:
 *
 * 1. The area of the rectangular web page you designed must equal to the given target
 *    area.
 * 2. The width W should not be larger than the length L, which means L >= W.
 * 3. The difference between length L and width W should be as small as possible.
 *
 * Return an array [L, W] where L and W are the length and width of the web page you
 * designed in sequence.
 *
 * Example 1:
 *
 * Input: area = 4
 * Output: [2,2]
 * Explanation: The target area is 4, and all the possible ways to construct it are
 * [1,4], [2,2], [4,1]. But according to requirement 2, [1,4] is illegal; according to
 * requirement 3, [4,1] is not optimal compared to [2,2]. So the length L is 2, and the
 * width W is 2.
 *
 * Example 2:
 *
 * Input: area = 37
 * Output: [37,1]
 *
 * Example 3:
 *
 * Input: area = 122122
 * Output: [427,286]
 *
 *
 * Constraints:
 *
 * 1 <= area <= 10^7
 *
 */
public class ConstructTheRectangle {

    // V0
    // IDEA: MATH
    /**
     *  -> start from W = floor(sqrt(area)) and walk DOWN until W divides area
     *  -> the FIRST divisor found is the biggest W with W <= L, so |L - W| is MINIMAL
     *
     *  NOTE !!! Math.sqrt returns a double, which can be off by one near a perfect
     *           square -> the two guard loops below correct it in both directions.
     *
     *  time  = O(sqrt(area))
     *  space = O(1)
     */
    public int[] constructRectangle(int area) {
        int w = (int) Math.sqrt(area);

        // guard against float rounding error of Math.sqrt
        while ((long) (w + 1) * (w + 1) <= area) {
            w += 1;
        }
        while ((long) w * w > area) {
            w -= 1;
        }

        // walk down to the first divisor of area
        while (area % w != 0) {
            w -= 1;
        }

        return new int[] { area / w, w };
    }

}
