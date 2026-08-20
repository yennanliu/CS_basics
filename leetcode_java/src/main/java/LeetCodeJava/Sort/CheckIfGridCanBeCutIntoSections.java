package LeetCodeJava.Sort;

// https://leetcode.com/problems/check-if-grid-can-be-cut-into-sections/

import java.util.Arrays;

/**
 *  3394. Check if Grid can be Cut into Sections
 *  Medium
 *
 *  You are given an integer n representing the dimensions of an n x n grid, with
 *  the origin at the bottom-left corner of the grid. You are also given a 2D array
 *  of coordinates rectangles, where rectangles[i] is in the form
 *  [start_x, start_y, end_x, end_y], representing a rectangle on the grid:
 *    (start_x, start_y) is the bottom-left corner,
 *    (end_x, end_y) is the top-right corner.
 *
 *  Note that the rectangles do not overlap. Your task is to determine if it is
 *  possible to make either two horizontal or two vertical cuts on the grid such
 *  that each of the three resulting sections contains at least one rectangle and
 *  every rectangle belongs to exactly one section.
 *
 *  Return true if such cuts can be made; otherwise, return false.
 *
 *  Example 1:
 *    Input: n = 5, rectangles = [[1,0,5,2],[0,2,2,4],[3,2,5,3],[0,4,4,5]]
 *    Output: true
 *    Explanation: horizontal cuts at y = 2 and y = 4.
 *
 *  Example 2:
 *    Input: n = 4, rectangles = [[0,2,2,4],[1,0,3,2],[2,2,3,4],[3,0,4,2],[3,2,4,4]]
 *    Output: false
 *
 *  Constraints:
 *    3 <= n <= 10^9
 *    3 <= rectangles.length <= 10^5
 *    0 <= rectangles[i][0] < rectangles[i][2] <= n
 *    0 <= rectangles[i][1] < rectangles[i][3] <= n
 *    No two rectangles overlap.
 */
public class CheckIfGridCanBeCutIntoSections {

    // V0
    // IDEA: INTERVAL MERGING ON EACH AXIS INDEPENDENTLY
    //       a horizontal cut is a line y = c that no rectangle straddles.
    //       projecting every rectangle onto the y axis turns it into the interval
    //       [start_y, end_y), and a legal cut is exactly a point where the union
    //       of those intervals falls apart. the rectangles never overlap, but
    //       their PROJECTIONS may -> merging is required.
    //       after merging, the number of blobs on an axis is the number of pieces
    //       the grid splits into along that axis; >= 3 blobs means the two cuts
    //       can go in two of the gaps and each piece keeps a rectangle.
    //       the two axes are independent, so just test both.
    /**
     * time = O(m log m), m = rectangles.length
     * space = O(m)
     */
    public boolean checkValidCuts(int n, int[][] rectangles) {
        int m = rectangles.length;
        int[][] xs = new int[m][2];
        int[][] ys = new int[m][2];
        for (int i = 0; i < m; i++) {
            xs[i][0] = rectangles[i][0];
            xs[i][1] = rectangles[i][2];
            ys[i][0] = rectangles[i][1];
            ys[i][1] = rectangles[i][3];
        }
        return sections(xs) >= 3 || sections(ys) >= 3;
    }

    // number of disjoint blobs after merging the spans
    private int sections(int[][] spans) {
        Arrays.sort(spans, (a, b) -> a[0] != b[0]
                ? Integer.compare(a[0], b[0])
                : Integer.compare(a[1], b[1]));
        int cnt = 0;
        long end = Long.MIN_VALUE;
        for (int[] s : spans) {
            if (s[0] >= end) {
                cnt++;
                end = s[1];
            } else {
                end = Math.max(end, s[1]);
            }
        }
        return cnt;
    }
}
