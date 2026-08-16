package LeetCodeJava.Math;

// https://leetcode.com/problems/perfect-rectangle/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 391. Perfect Rectangle
 * Hard
 *
 * Given an array rectangles where rectangles[i] = [xi, yi, ai, bi] represents an
 * axis-aligned rectangle. The bottom-left point of the rectangle is (xi, yi) and
 * the top-right point of it is (ai, bi).
 *
 * Return true if all the rectangles together form an exact cover of a rectangular region.
 *
 * Example 1:
 *
 * Input: rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
 * Output: true
 * Explanation: All 5 rectangles together form an exact cover of a rectangular region.
 *
 * Example 2:
 *
 * Input: rectangles = [[1,1,2,3],[1,3,2,4],[3,1,4,2],[3,2,4,4]]
 * Output: false
 * Explanation: Because there is a gap between the two rectangular regions.
 *
 * Example 3:
 *
 * Input: rectangles = [[1,1,3,3],[3,1,4,2],[1,3,2,4],[2,2,4,4]]
 * Output: false
 * Explanation: Because two of the rectangles overlap with each other.
 *
 * Constraints:
 *
 * 1 <= rectangles.length <= 2 * 10^4
 * rectangles[i].length == 4
 * -10^5 <= xi < ai <= 10^5
 * -10^5 <= yi < bi <= 10^5
 *
 */
public class PerfectRectangle {

    // V0
    // IDEA: AREA + CORNER COUNTING
    /**
     *  A set of rectangles is a perfect cover iff BOTH hold:
     *
     *   1) SUM of the small areas == area of the BOUNDING BOX
     *      (no gap, and no overlap can hide since overlap would make the sum bigger)
     *
     *   2) CORNER PARITY:
     *      - the 4 corners of the bounding box appear EXACTLY ONCE
     *      - every other corner point appears an EVEN number of times (2 or 4)
     *
     *  NOTE !!! (1) alone is NOT enough: an overlap plus an equal-sized gap keeps the
     *           area right. (2) alone is not enough either -> we need BOTH.
     *
     *  NOTE !!! the area sum reaches 2*10^4 * (2*10^5)^2 = 8*10^14,
     *           which OVERFLOWS int -> `area` must be `long`.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public boolean isRectangleCover(int[][] rectangles) {
        long area = 0;
        int minX = rectangles[0][0];
        int minY = rectangles[0][1];
        int maxX = rectangles[0][2];
        int maxY = rectangles[0][3];

        Map<Long, Integer> cnt = new HashMap<>();

        for (int[] r : rectangles) {
            int x1 = r[0];
            int y1 = r[1];
            int x2 = r[2];
            int y2 = r[3];

            area += (long) (x2 - x1) * (y2 - y1);

            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
            maxX = Math.max(maxX, x2);
            maxY = Math.max(maxY, y2);

            // count the 4 corners of this small rectangle
            bump(cnt, x1, y1);
            bump(cnt, x1, y2);
            bump(cnt, x2, y1);
            bump(cnt, x2, y2);
        }

        // check 1 : total area must match the bounding box
        if (area != (long) (maxX - minX) * (maxY - minY)) {
            return false;
        }

        // check 2 : the 4 OUTER corners must appear exactly once
        long[] corners = {
                encode(minX, minY), encode(minX, maxY),
                encode(maxX, minY), encode(maxX, maxY)
        };
        for (long c : corners) {
            if (cnt.getOrDefault(c, 0) != 1) {
                return false;
            }
            cnt.remove(c);
        }

        // every REMAINING point must be shared by an EVEN number of rectangles
        for (int v : cnt.values()) {
            if (v != 2 && v != 4) {
                return false;
            }
        }

        return true;
    }

    private void bump(Map<Long, Integer> cnt, int x, int y) {
        long k = encode(x, y);
        cnt.put(k, cnt.getOrDefault(k, 0) + 1);
    }

    private long encode(int x, int y) {
        return (long) x * 1000000L + y;
    }

}
