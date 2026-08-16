package LeetCodeJava.Math;

// https://leetcode.com/problems/perfect-rectangle/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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


    // V1
    // IDEA: CORNER PARITY VIA A XOR SET (no counters)
    /**
     *  Instead of counting how often each corner appears, TOGGLE it in a set. Every
     *  interior corner is touched an even number of times and cancels out, so the
     *  set must end holding exactly the four outer corners.
     *
     *  One set operation per corner and no `is this 2 or 4?` case analysis.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public boolean isRectangleCover_1(int[][] rectangles) {
        long area = 0;
        int minX = rectangles[0][0];
        int minY = rectangles[0][1];
        int maxX = rectangles[0][2];
        int maxY = rectangles[0][3];

        Set<Long> corners = new HashSet<>();
        for (int[] r : rectangles) {
            area += (long) (r[2] - r[0]) * (r[3] - r[1]);
            minX = Math.min(minX, r[0]);
            minY = Math.min(minY, r[1]);
            maxX = Math.max(maxX, r[2]);
            maxY = Math.max(maxY, r[3]);

            for (long key : new long[] { enc(r[0], r[1]), enc(r[0], r[3]),
                                         enc(r[2], r[1]), enc(r[2], r[3]) }) {
                if (!corners.add(key)) {
                    corners.remove(key);   // TOGGLE: seen an even number of times
                }
            }
        }

        if (corners.size() != 4) {
            return false;
        }
        if (!corners.contains(enc(minX, minY)) || !corners.contains(enc(minX, maxY))
                || !corners.contains(enc(maxX, minY)) || !corners.contains(enc(maxX, maxY))) {
            return false;
        }
        return area == (long) (maxX - minX) * (maxY - minY);
    }

    private long enc(int x, int y) {
        return (long) x * 1000000L + y;
    }

    // V2
    // IDEA: SWEEP LINE over x, maintaining the active y intervals
    /**
     *  Sort the vertical edges by x and keep the set of y intervals currently
     *  covered. At each x the intervals opening must exactly fill the gaps left by
     *  those closing, and between events the covered y range must be contiguous.
     *
     *  Detects gaps AND overlaps directly rather than inferring them from area plus
     *  corner parity -- and it reports WHERE the defect is.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public boolean isRectangleCover_2(int[][] rectangles) {
        // {x, type(0=open,1=close), y1, y2}
        List<int[]> events = new ArrayList<>();
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int[] r : rectangles) {
            events.add(new int[] { r[0], 0, r[1], r[3] });
            events.add(new int[] { r[2], 1, r[1], r[3] });
            minY = Math.min(minY, r[1]);
            maxY = Math.max(maxY, r[3]);
        }
        events.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        TreeMap<Integer, Integer> active = new TreeMap<>(); // y1 -> y2
        int i = 0;
        while (i < events.size()) {
            int x = events.get(i)[0];
            // process every event at this x: closes first, then opens
            while (i < events.size() && events.get(i)[0] == x && events.get(i)[1] == 1) {
                int[] e = events.get(i++);
                Integer end = active.get(e[2]);
                if (end == null || end != e[3]) {
                    return false;
                }
                active.remove(e[2]);
            }
            while (i < events.size() && events.get(i)[0] == x && events.get(i)[1] == 0) {
                int[] e = events.get(i++);
                Map.Entry<Integer, Integer> floor = active.floorEntry(e[2]);
                if (floor != null && floor.getValue() > e[2]) {
                    return false;            // overlap
                }
                Map.Entry<Integer, Integer> ceil = active.ceilingEntry(e[2]);
                if (ceil != null && ceil.getKey() < e[3]) {
                    return false;            // overlap
                }
                active.put(e[2], e[3]);
            }

            // between events the covered y range must be a single contiguous block
            if (!active.isEmpty() && i < events.size()) {
                int expect = active.firstKey();
                if (expect != minY) {
                    return false;
                }
                for (Map.Entry<Integer, Integer> e : active.entrySet()) {
                    if (e.getKey() != expect) {
                        return false;        // a gap
                    }
                    expect = e.getValue();
                }
                if (expect != maxY) {
                    return false;
                }
            }
        }
        return active.isEmpty();
    }

    // V3
    // IDEA: GRID DECOMPOSITION -- compress both axes and count each cell
    /**
     *  Compress x and y, then count how many rectangles cover each elementary cell.
     *  A perfect cover means EVERY cell inside the bounding box is covered exactly
     *  once.
     *
     *  O(n^3), the slowest, but it distinguishes a gap (count 0) from an overlap
     *  (count >= 2) explicitly -- the oracle for the two clever versions.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public boolean isRectangleCover_3(int[][] rectangles) {
        TreeSet<Integer> xsSet = new TreeSet<>();
        TreeSet<Integer> ysSet = new TreeSet<>();
        for (int[] r : rectangles) {
            xsSet.add(r[0]);
            xsSet.add(r[2]);
            ysSet.add(r[1]);
            ysSet.add(r[3]);
        }
        Integer[] xs = xsSet.toArray(new Integer[0]);
        Integer[] ys = ysSet.toArray(new Integer[0]);

        int[][] count = new int[xs.length][ys.length];
        for (int[] r : rectangles) {
            for (int i = 0; i + 1 < xs.length; i++) {
                if (xs[i] < r[0] || xs[i + 1] > r[2]) {
                    continue;
                }
                for (int j = 0; j + 1 < ys.length; j++) {
                    if (ys[j] >= r[1] && ys[j + 1] <= r[3]) {
                        count[i][j] += 1;
                        if (count[i][j] > 1) {
                            return false;   // OVERLAP
                        }
                    }
                }
            }
        }

        for (int i = 0; i + 1 < xs.length; i++) {
            for (int j = 0; j + 1 < ys.length; j++) {
                if (count[i][j] == 0) {
                    return false;           // GAP
                }
            }
        }
        return true;
    }

}
