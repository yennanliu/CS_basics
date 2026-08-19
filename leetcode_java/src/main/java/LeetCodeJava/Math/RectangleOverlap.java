package LeetCodeJava.Math;

// https://leetcode.com/problems/rectangle-overlap/

/**
 *  836. Rectangle Overlap
 *  Easy
 *
 *  An axis-aligned rectangle is represented as a list [x1, y1, x2, y2], where
 *  (x1, y1) is the coordinate of its bottom-left corner, and (x2, y2) is the
 *  coordinate of its top-right corner.
 *
 *  Two rectangles overlap if the area of their intersection is positive. Two
 *  rectangles that only touch at the corner or edges do not overlap.
 *
 *  Given two axis-aligned rectangles rec1 and rec2, return true if they overlap,
 *  otherwise return false.
 *
 *  Example 1:
 *   Input: rec1 = [0,0,2,2], rec2 = [1,1,3,3]
 *   Output: true
 *
 *  Example 2:
 *   Input: rec1 = [0,0,1,1], rec2 = [1,0,2,1]
 *   Output: false
 *
 *  Constraints:
 *   - rec1.length == 4, rec2.length == 4
 *   - -10^9 <= rec1[i], rec2[i] <= 10^9
 *   - rec1 and rec2 represent a valid rectangle with a non-zero area.
 */
public class RectangleOverlap {

    // V0
    // IDEA: the overlap width and height must both be strictly positive.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
        long width = Math.min(rec1[2], rec2[2]) - (long) Math.max(rec1[0], rec2[0]);
        long height = Math.min(rec1[3], rec2[3]) - (long) Math.max(rec1[1], rec2[1]);
        return width > 0 && height > 0;
    }
}
