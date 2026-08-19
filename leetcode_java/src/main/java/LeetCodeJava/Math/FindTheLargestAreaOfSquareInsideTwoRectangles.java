package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-largest-area-of-square-inside-two-rectangles/

/**
 *  3047. Find the Largest Area of Square Inside Two Rectangles
 *  Medium
 *
 *  There exist n rectangles in a 2D plane with edges parallel to the x and y axis. You
 *  are given two 2D integer arrays bottomLeft and topRight where
 *  bottomLeft[i] = [a_i, b_i] and topRight[i] = [c_i, d_i] represent the bottom-left and
 *  top-right coordinates of the ith rectangle, respectively.
 *
 *  You need to find the maximum area of a square that can fit inside the intersecting
 *  region of at least two rectangles.
 *
 *  Return the maximum area of such a square, or 0 if such a square does not exist.
 *
 *  Example 1:
 *    Input: bottomLeft = [[1,1],[2,2],[3,1]], topRight = [[3,3],[4,4],[6,6]]
 *    Output: 1
 *
 *  Example 3:
 *    Input: bottomLeft = [[1,1],[3,3],[3,1]], topRight = [[2,2],[4,4],[4,2]]
 *    Output: 0
 *    Explanation: no pair of rectangles intersect.
 *
 *  Constraints:
 *    n == bottomLeft.length == topRight.length
 *    2 <= n <= 10^3
 *    bottomLeft[i].length == topRight[i].length == 2
 *    1 <= bottomLeft[i][0], bottomLeft[i][1] <= 10^7
 *    1 <= topRight[i][0], topRight[i][1] <= 10^7
 *    bottomLeft[i][0] < topRight[i][0]
 *    bottomLeft[i][1] < topRight[i][1]
 */
public class FindTheLargestAreaOfSquareInsideTwoRectangles {

    // V0
    // IDEA: THE BEST SQUARE ALWAYS LIVES IN SOME *PAIRWISE* INTERSECTION
    //
    //   an intersection of three or more rectangles is contained in the intersection of
    //   any two of them, so it can never beat the best pair. that reduces the search to
    //   the n^2/2 pairs - 5 * 10^5 of them at n = 1000.
    //
    //   two axis-aligned rectangles intersect in another axis-aligned rectangle:
    //       x-range [max(left), min(right)]   y-range [max(bottom), min(top)]
    //   and the largest square inside it has side min(width, height), which is 0 or
    //   negative when they miss each other.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public long largestSquareArea(int[][] bottomLeft, int[][] topRight) {
        int n = bottomLeft.length;
        long best = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long w = Math.min(topRight[i][0], topRight[j][0])
                        - Math.max(bottomLeft[i][0], bottomLeft[j][0]);
                long h = Math.min(topRight[i][1], topRight[j][1])
                        - Math.max(bottomLeft[i][1], bottomLeft[j][1]);
                long side = Math.min(w, h);
                if (side > best) {
                    best = side;
                }
            }
        }
        return best * best;
    }
}
