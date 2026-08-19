package LeetCodeJava.Math;

// https://leetcode.com/problems/random-point-in-non-overlapping-rectangles/

import java.util.Random;

/**
 *  497. Random Point in Non-overlapping Rectangles
 *  Medium
 *
 *  You are given an array of non-overlapping axis-aligned rectangles rects where
 *  rects[i] = [ai, bi, xi, yi] indicates that (ai, bi) is the bottom-left corner point
 *  of the ith rectangle and (xi, yi) is the top-right corner point of the ith rectangle.
 *  Design an algorithm to pick a random integer point inside the space covered by one of
 *  the given rectangles. A point on the perimeter of a rectangle is included in the space
 *  covered by the rectangle.
 *
 *  Any integer point inside the space covered by one of the given rectangles should be
 *  equally likely to be returned.
 *
 *  Implement the Solution class:
 *    - Solution(int[][] rects) initializes the object with the given rectangles rects.
 *    - int[] pick() returns a random integer point [u, v] inside the space covered by
 *      one of the given rectangles.
 *
 *  Example 1:
 *    Input: ["Solution", "pick", "pick", "pick", "pick", "pick"]
 *           [[[[-2, -2, 1, 1], [2, 2, 4, 6]]], [], [], [], [], []]
 *    Output: [null, [1, -2], [1, -1], [-1, -2], [-2, -2], [0, 0]]
 *
 *  Constraints:
 *    1 <= rects.length <= 100
 *    rects[i].length == 4
 *    -10^9 <= ai < xi <= 10^9
 *    -10^9 <= bi < yi <= 10^9
 *    xi - ai <= 2000
 *    yi - bi <= 2000
 *    All the rectangles do not overlap.
 *    At most 10^4 calls will be made to pick.
 */
public class RandomPointInNonOverlappingRectangles {

    private final int[][] rects;
    private final long[] prefixSum; // prefixSum[i] = total number of points in rects[0..i]
    private final Random rand;

    public RandomPointInNonOverlappingRectangles(int[][] rects) {
        this.rects = rects;
        this.rand = new Random();
        this.prefixSum = new long[rects.length];
        long running = 0;
        for (int i = 0; i < rects.length; i++) {
            long w = (long) rects[i][2] - rects[i][0] + 1;
            long h = (long) rects[i][3] - rects[i][1] + 1;
            running += w * h;
            this.prefixSum[i] = running;
        }
    }

    // V0
    // IDEA: weight each rect by its integer-point count, binary search a random
    //       "global index" into the prefix sums, then map the offset to (x, y)
    /**
     * time = O(log n) per pick, O(n) for the constructor
     * space = O(n)
     */
    public int[] pick() {
        long total = this.prefixSum[this.prefixSum.length - 1];
        // uniform target in [0, total - 1]
        long target = (long) (this.rand.nextDouble() * total);
        if (target >= total) {
            target = total - 1;
        }

        // binary search: smallest idx with prefixSum[idx] > target
        int left = 0;
        int right = this.prefixSum.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (this.prefixSum[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        int[] rect = this.rects[left];
        long base = this.prefixSum[left] - ((long) rect[2] - rect[0] + 1) * ((long) rect[3] - rect[1] + 1);
        long offset = target - base;
        long width = (long) rect[2] - rect[0] + 1;
        int x = (int) (rect[0] + offset % width);
        int y = (int) (rect[1] + offset / width);
        return new int[] { x, y };
    }
}
