package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/count-number-of-rectangles-containing-each-point/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2250. Count Number of Rectangles Containing Each Point
 * Medium
 *
 * You are given a 2D integer array rectangles where rectangles[i] = [li, hi] indicates that ith
 * rectangle has a length of li and a height of hi. You are also given a 2D integer array points where
 * points[j] = [xj, yj] is a point with coordinates (xj, yj).
 *
 * The ith rectangle has its bottom-left corner point at the coordinates (0, 0) and its top-right corner
 * point at (li, hi).
 *
 * Return an integer array count of length points.length where count[j] is the number of rectangles that
 * contain the jth point.
 *
 * The ith rectangle contains the jth point if 0 <= xj <= li and 0 <= yj <= hi. Note that points that lie
 * on the edges of a rectangle are also considered to be contained by that rectangle.
 *
 *
 * Example 1:
 *
 * Input: rectangles = [[1,2],[2,3],[2,5]], points = [[2,1],[1,4]]
 * Output: [2,1]
 * Explanation:
 * The first rectangle contains no points.
 * The second rectangle contains only the point (2, 1).
 * The third rectangle contains the points (2, 1) and (1, 4).
 * The number of rectangles that contain the point (2, 1) is 2.
 * The number of rectangles that contain the point (1, 4) is 1.
 * Therefore, we return [2, 1].
 *
 * Example 2:
 *
 * Input: rectangles = [[1,1],[2,2],[3,3]], points = [[1,3],[1,1]]
 * Output: [1,3]
 * Explanation:
 * The first rectangle contains only the point (1, 1).
 * The second rectangle contains only the point (1, 1).
 * The third rectangle contains the points (1, 3) and (1, 1).
 * The number of rectangles that contain the point (1, 3) is 1.
 * The number of rectangles that contain the point (1, 1) is 3.
 * Therefore, we return [1, 3], in that order.
 *
 *
 * Constraints:
 *
 * 1 <= rectangles.length, points.length <= 5 * 10^4
 * rectangles[i].length == points[j].length == 2
 * 1 <= li, xj <= 10^9
 * 1 <= hi, yj <= 100
 *
 */
public class CountNumberOfRectanglesContainingEachPoint {

    private static final int MAX_HEIGHT = 100;

    // V0
    // IDEA: BUCKET BY HEIGHT (<= 100) + BINARY SEARCH ON SORTED LENGTHS
    /**
     *  Rectangle [l, h] contains (x, y) iff l >= x and h >= y. Heights are at most 100, so
     *  bucket the rectangles by height and sort the lengths in each bucket.
     *  A query (x, y) then sums, over heights y .. 100, how many lengths are >= x: one lower
     *  bound per bucket, at most 100 binary searches. The length dimension reaches 10^9 and
     *  has to stay a binary search; the small height domain is what makes this cheap.
     */
    /**
     * time = O(R log R + P * 100 * log R), R = rectangles, P = points
     * space = O(R)
     */
    public int[] countRectangles(int[][] rectangles, int[][] points) {
        // lengthsByHeight.get(h) = lengths of every rectangle with height h
        List<List<Integer>> lengthsByHeight = new ArrayList<>();
        for (int h = 0; h <= MAX_HEIGHT; h++) {
            lengthsByHeight.add(new ArrayList<>());
        }
        for (int[] rectangle : rectangles) {
            lengthsByHeight.get(rectangle[1]).add(rectangle[0]);
        }
        for (List<Integer> lengths : lengthsByHeight) {
            Collections.sort(lengths);
        }

        int[] answer = new int[points.length];
        for (int j = 0; j < points.length; j++) {
            int x = points[j][0];
            int y = points[j][1];
            int count = 0;
            // only rectangles at least as tall as the point can contain it
            for (int h = y; h <= MAX_HEIGHT; h++) {
                List<Integer> lengths = lengthsByHeight.get(h);
                count += lengths.size() - lowerBound(lengths, x);
            }
            answer[j] = count;
        }
        return answer;
    }

    /** First index whose value is >= target, or list size if none. */
    private int lowerBound(List<Integer> sorted, int target) {
        int left = 0;
        int right = sorted.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
