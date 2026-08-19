package LeetCodeJava.Math;

// https://leetcode.com/problems/rectangle-area/

/**
 *  223. Rectangle Area
 *  Medium
 *
 *  Given the coordinates of two rectilinear rectangles in a 2D plane, return the
 *  total area covered by the two rectangles.
 *
 *  The first rectangle is defined by its bottom-left corner (ax1, ay1) and its
 *  top-right corner (ax2, ay2).
 *  The second rectangle is defined by its bottom-left corner (bx1, by1) and its
 *  top-right corner (bx2, by2).
 *
 *  Example 1:
 *    Input: ax1 = -3, ay1 = 0, ax2 = 3, ay2 = 4,
 *           bx1 = 0,  by1 = -1, bx2 = 9, by2 = 2
 *    Output: 45
 *
 *  Example 2:
 *    Input: ax1 = -2, ay1 = -2, ax2 = 2, ay2 = 2,
 *           bx1 = -2, by1 = -2, bx2 = 2, by2 = 2
 *    Output: 16
 *
 *  Constraints:
 *    -10^4 <= ax1 <= ax2 <= 10^4
 *    -10^4 <= ay1 <= ay2 <= 10^4
 *    -10^4 <= bx1 <= bx2 <= 10^4
 *    -10^4 <= by1 <= by2 <= 10^4
 */
public class RectangleArea {

    // V0
    // IDEA: inclusion-exclusion: areaA + areaB - overlap; overlap sides clamped at 0
    /**
     * time = O(1)
     * space = O(1)
     */
    public int computeArea(int ax1, int ay1, int ax2, int ay2,
                           int bx1, int by1, int bx2, int by2) {

        long areaA = (long) (ax2 - ax1) * (ay2 - ay1);
        long areaB = (long) (bx2 - bx1) * (by2 - by1);

        // overlap window on each axis (0 when they don't intersect)
        long overlapW = Math.max(0, Math.min(ax2, bx2) - Math.max(ax1, bx1));
        long overlapH = Math.max(0, Math.min(ay2, by2) - Math.max(ay1, by1));

        return (int) (areaA + areaB - overlapW * overlapH);
    }
}
