package LeetCodeJava.Tree;

// https://leetcode.com/problems/rectangle-area-ii/description/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 850. Rectangle Area II
 * Hard
 *
 * You are given a 2D array of axis-aligned rectangles. Each rectangle[i] =
 * [xi1, yi1, xi2, yi2] denotes the ith rectangle where (xi1, yi1) are the coordinates
 * of the bottom-left corner, and (xi2, yi2) are the coordinates of the top-right corner.
 *
 * Calculate the total area covered by all rectangles in the plane.
 * Any area covered by two or more rectangles should only be counted once.
 *
 * Return the total area. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
 * Output: 6
 * Explanation: A total area of 6 is covered by all three rectangles.
 * From (1,1) to (2,2), the green and red rectangles overlap.
 * From (1,0) to (2,3), all three rectangles overlap.
 *
 * Example 2:
 *
 * Input: rectangles = [[0,0,1000000000,1000000000]]
 * Output: 49
 * Explanation: The answer is 10^18 modulo (10^9 + 7), which is 49.
 *
 *
 * Constraints:
 *
 * 1 <= rectangles.length <= 200
 * rectanges[i].length == 4
 * 0 <= xi1, yi1, xi2, yi2 <= 10^9
 * xi1 <= xi2
 * yi1 <= yi2
 * All rectangles have non zero area.
 *
 */
public class RectangleArea2 {

    // V0
    // IDEA: SWEEP LINE + COORDINATE COMPRESSION
    /**
     *   Sweep a HORIZONTAL line upward through all distinct y values.
     *   Between two consecutive y values the set of active rectangles NEVER
     *   changes, so the covered area of that horizontal SLAB is
     *
     *       (covered x length) * (slab height)
     *
     *   To get the covered x length, COMPRESS all distinct x coordinates into
     *   at most 2n `columns` and keep a counter per column of how many active
     *   rectangles cover it. A column contributes its width when its counter > 0.
     *
     *   Each rectangle becomes TWO events:
     *       (y1, +1, x1, x2)  bottom edge -> the rectangle becomes ACTIVE
     *       (y2, -1, x1, x2)  top edge    -> the rectangle becomes INACTIVE
     *
     *   NOTE !!! take the modulo ONLY at the very end - taking it earlier would
     *            corrupt the running width/height arithmetic.
     *
     *   NOTE !!! coordinates reach 10^9 so a single slab is up to 10^18
     *            -> `area` and the width arithmetic MUST be `long`.
     *            (the total stays under 10^18 because the slabs are disjoint in y)
     *
     *   time  = O(n^2)     // 2n events, each rescanning 2n columns
     *   space = O(n)
     */
    public int rectangleArea(int[][] rectangles) {
        final long MOD = 1_000_000_007L;

        // compress the x axis
        TreeSet<Integer> xsSet = new TreeSet<>();
        for (int[] r : rectangles) {
            xsSet.add(r[0]);
            xsSet.add(r[2]);
        }
        Integer[] xs = xsSet.toArray(new Integer[0]);

        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < xs.length; i++) {
            pos.put(xs[i], i);
        }

        // sweep events {y, delta, x1, x2}, sorted by y
        int[][] events = new int[rectangles.length * 2][4];
        int e = 0;
        for (int[] r : rectangles) {
            events[e++] = new int[] { r[1], 1, r[0], r[2] };
            events[e++] = new int[] { r[3], -1, r[0], r[2] };
        }
        Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));

        // count[i] = how many active rectangles cover column [xs[i], xs[i+1])
        int[] count = new int[xs.length];
        long area = 0;
        int prevY = events[0][0];

        for (int[] ev : events) {
            int y = ev[0];
            int delta = ev[1];

            // close off the slab between prevY and y with the CURRENT coverage
            long covered = 0;
            for (int i = 0; i + 1 < xs.length; i++) {
                if (count[i] > 0) {
                    covered += (long) xs[i + 1] - xs[i];
                }
            }
            area += covered * (y - prevY);
            prevY = y;

            // THEN apply this event
            for (int i = pos.get(ev[2]); i < pos.get(ev[3]); i++) {
                count[i] += delta;
            }
        }

        return (int) (area % MOD);
    }


    // V1
    // IDEA: SWEEP LINE + SEGMENT TREE (count-of-covered-length per node)
    /**
     *  V0 rescans all 2n columns to measure the covered width of every slab, giving
     *  O(n^2). A segment tree over the compressed x axis maintains that covered
     *  length incrementally: each node stores how many rectangles cover its WHOLE
     *  range plus the covered length below it.
     *
     *  -> O(n log n) instead of O(n^2).
     *
     *  NOTE !!! the counter is never `pushed down`; a node's covered length is
     *           either its full width (count > 0) or the sum of its children.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    private int[] segCount;
    private long[] segCovered;
    private long[] segXs;

    public int rectangleArea_1(int[][] rectangles) {
        final long MOD = 1_000_000_007L;

        TreeSet<Integer> xsSet = new TreeSet<>();
        for (int[] r : rectangles) {
            xsSet.add(r[0]);
            xsSet.add(r[2]);
        }
        Integer[] xs = xsSet.toArray(new Integer[0]);
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < xs.length; i++) {
            idx.put(xs[i], i);
        }

        int m = xs.length;
        segXs = new long[m];
        for (int i = 0; i < m; i++) {
            segXs[i] = xs[i];
        }
        segCount = new int[4 * m];
        segCovered = new long[4 * m];

        // {y, delta, x1index, x2index}
        int[][] events = new int[rectangles.length * 2][4];
        int e = 0;
        for (int[] r : rectangles) {
            events[e++] = new int[] { r[1], 1, idx.get(r[0]), idx.get(r[2]) };
            events[e++] = new int[] { r[3], -1, idx.get(r[0]), idx.get(r[2]) };
        }
        Arrays.sort(events, Comparator.comparingInt(x -> x[0]));

        long area = 0;
        long prevY = events[0][0];

        for (int[] ev : events) {
            area += segCovered[1] * (ev[0] - prevY);
            prevY = ev[0];
            if (ev[2] < ev[3]) {
                segUpdate(1, 0, m - 2, ev[2], ev[3] - 1, ev[1]);
            }
        }

        return (int) (area % MOD);
    }

    private void segUpdate(int node, int lo, int hi, int ql, int qr, int delta) {
        if (hi < lo || qr < lo || hi < ql) {
            return;
        }
        if (ql <= lo && hi <= qr) {
            segCount[node] += delta;
        } else {
            int mid = lo + (hi - lo) / 2;
            segUpdate(node * 2, lo, mid, ql, qr, delta);
            segUpdate(node * 2 + 1, mid + 1, hi, ql, qr, delta);
        }

        if (segCount[node] > 0) {
            segCovered[node] = segXs[hi + 1] - segXs[lo];
        } else if (lo == hi) {
            segCovered[node] = 0;
        } else {
            segCovered[node] = segCovered[node * 2] + segCovered[node * 2 + 1];
        }
    }

    // V2
    // IDEA: RECURSIVE RECTANGLE SUBTRACTION (inclusion by splitting)
    /**
     *  Add rectangles one at a time; before adding, SUBTRACT the parts already
     *  covered by splitting the new rectangle around each existing one.
     *
     *  Every surviving piece is disjoint from everything before it, so the total is
     *  a plain sum -- no sweep and no coordinate compression.
     *
     *  Exponential in the worst case, but it makes the `count overlaps once` rule
     *  geometric rather than algebraic.
     *
     *  time  = O(n^2) typical, worse on heavy overlap
     *  space = O(n)
     */
    public int rectangleArea_2(int[][] rectangles) {
        final long MOD = 1_000_000_007L;
        List<long[]> disjoint = new ArrayList<>();

        for (int[] r : rectangles) {
            List<long[]> pieces = new ArrayList<>();
            pieces.add(new long[] { r[0], r[1], r[2], r[3] });

            for (long[] old : disjoint) {
                List<long[]> next = new ArrayList<>();
                for (long[] p : pieces) {
                    next.addAll(subtract(p, old));
                }
                pieces = next;
            }
            disjoint.addAll(pieces);
        }

        long area = 0;
        for (long[] p : disjoint) {
            area += (p[2] - p[0]) * (p[3] - p[1]);
        }
        return (int) (area % MOD);
    }

    /** the parts of `a` not covered by `b`, as up to 4 rectangles */
    private List<long[]> subtract(long[] a, long[] b) {
        List<long[]> out = new ArrayList<>();
        if (a[2] <= b[0] || b[2] <= a[0] || a[3] <= b[1] || b[3] <= a[1]) {
            out.add(a);            // no overlap
            return out;
        }
        if (a[0] < b[0]) {
            out.add(new long[] { a[0], a[1], b[0], a[3] });
        }
        if (b[2] < a[2]) {
            out.add(new long[] { b[2], a[1], a[2], a[3] });
        }
        long midLeft = Math.max(a[0], b[0]);
        long midRight = Math.min(a[2], b[2]);
        if (a[1] < b[1]) {
            out.add(new long[] { midLeft, a[1], midRight, b[1] });
        }
        if (b[3] < a[3]) {
            out.add(new long[] { midLeft, b[3], midRight, a[3] });
        }
        return out;
    }

    // V3
    // IDEA: FULL GRID DECOMPOSITION (compress BOTH axes, then count cells)
    /**
     *  Compress the x AND y coordinates, giving an O(n) x O(n) grid of cells whose
     *  coverage is constant. Mark every cell any rectangle covers, then sum the
     *  areas of the marked cells.
     *
     *  O(n^3) so it is the slowest, but it involves no sweep, no events and no tree
     *  -- the most obviously correct of the four, and the natural oracle.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public int rectangleArea_3(int[][] rectangles) {
        final long MOD = 1_000_000_007L;

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

        boolean[][] covered = new boolean[xs.length][ys.length];
        for (int[] r : rectangles) {
            for (int i = 0; i + 1 < xs.length; i++) {
                if (xs[i] < r[0] || xs[i + 1] > r[2]) {
                    continue;
                }
                for (int j = 0; j + 1 < ys.length; j++) {
                    if (ys[j] >= r[1] && ys[j + 1] <= r[3]) {
                        covered[i][j] = true;
                    }
                }
            }
        }

        long area = 0;
        for (int i = 0; i + 1 < xs.length; i++) {
            for (int j = 0; j + 1 < ys.length; j++) {
                if (covered[i][j]) {
                    area += (long) (xs[i + 1] - xs[i]) * (ys[j + 1] - ys[j]);
                }
            }
        }
        return (int) (area % MOD);
    }

}
