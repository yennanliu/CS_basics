package LeetCodeJava.Tree;

// https://leetcode.com/problems/falling-squares/description/

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 699. Falling Squares
 * Hard
 *
 * There are several squares being dropped onto the X-axis of a 2D plane.
 *
 * You are given a 2D integer array positions where positions[i] = [left_i, sideLength_i]
 * represents the ith square with a side length of sideLength_i that is dropped with its
 * left edge aligned with X-coordinate left_i.
 *
 * Each square is dropped one at a time from a height above any landed squares.
 * It then falls downward (negative Y direction) until it either lands on the top side
 * of another square or on the X-axis. A square brushing the left/right side of another
 * square does not count as landing on it. Once it lands, it freezes in place and cannot
 * be moved.
 *
 * After each square is dropped, you must record the height of the current tallest stack
 * of squares.
 *
 * Return an integer array ans where ans[i] represents the height described above
 * after dropping the ith square.
 *
 *
 * Example 1:
 *
 * Input: positions = [[1,2],[2,3],[6,1]]
 * Output: [2,5,5]
 * Explanation:
 * After the first drop, the tallest stack is square 1 with a height of 2.
 * After the second drop, the tallest stack is squares 1 and 2 with a height of 5.
 * After the third drop, the tallest stack is still squares 1 and 2 with a height of 5.
 * Thus, we return an answer of [2, 5, 5].
 *
 * Example 2:
 *
 * Input: positions = [[100,100],[200,100]]
 * Output: [100,100]
 * Explanation:
 * After the first drop, the tallest stack is square 1 with a height of 100.
 * After the second drop, the tallest stack is either square 1 or square 2, both with
 * heights of 100.
 * Thus, we return an answer of [100, 100].
 * Note that square 2 only brushes the right side of square 1, which does not count as
 * landing on it.
 *
 *
 * Constraints:
 *
 * 1 <= positions.length <= 1000
 * 1 <= left_i <= 10^8
 * 1 <= sideLength_i <= 10^6
 *
 */
public class FallingSquares {

    // V0
    // IDEA: BRUTE FORCE + INTERVAL OVERLAP
    /**
     *   Keep every landed square as (left, right, topHeight).
     *   A new square [l, r) lands on the TALLEST square it *STRICTLY* overlaps with.
     *
     *   NOTE !!! touching edges do NOT count as landing
     *            -> the test must be `left < r && l < right` (STRICT on both sides),
     *               not `<=`, otherwise example 2 would stack to 200.
     *
     *   Then track the RUNNING max height (the answer is a prefix maximum, not the
     *   height of the square just dropped).
     *
     *   time  = O(n^2)
     *   space = O(n)
     */
    public List<Integer> fallingSquares(int[][] positions) {
        List<int[]> landed = new ArrayList<>(); // {left, right, top height}
        List<Integer> res = new ArrayList<>();
        int curMax = 0;

        for (int[] p : positions) {
            int left = p[0];
            int side = p[1];
            int right = left + side; // half open interval [left, right)

            // the square rests on top of the HIGHEST square it overlaps with
            int base = 0;
            for (int[] sq : landed) {
                int l = sq[0];
                int r = sq[1];
                int h = sq[2];
                // STRICT overlap only: brushing a side is not landing on it
                if (left < r && l < right) {
                    base = Math.max(base, h);
                }
            }

            int top = base + side;
            landed.add(new int[] { left, right, top });

            curMax = Math.max(curMax, top);
            res.add(curMax);
        }

        return res;
    }


    // V1
    // IDEA: COORDINATE COMPRESSION + a flat height array
    /**
     *  Compress the O(n) distinct x coordinates into at most 2n columns, then a
     *  square is a RANGE of columns: read the maximum height over that range, add
     *  its side, and write the new height back over the whole range.
     *
     *  Still O(n^2) in the worst case but each step is a contiguous array scan
     *  rather than a walk over an interval list -- far better constants, and it is
     *  the natural stepping stone toward the segment tree in V2.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public List<Integer> fallingSquares_1(int[][] positions) {
        TreeSet<Integer> xsSet = new TreeSet<>();
        for (int[] p : positions) {
            xsSet.add(p[0]);
            xsSet.add(p[0] + p[1]);
        }
        List<Integer> xs = new ArrayList<>(xsSet);
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < xs.size(); i++) {
            idx.put(xs.get(i), i);
        }

        int[] height = new int[xs.size()];
        List<Integer> res = new ArrayList<>();
        int best = 0;

        for (int[] p : positions) {
            int lo = idx.get(p[0]);
            int hi = idx.get(p[0] + p[1]);   // exclusive column boundary

            int base = 0;
            for (int i = lo; i < hi; i++) {
                base = Math.max(base, height[i]);
            }
            int top = base + p[1];
            for (int i = lo; i < hi; i++) {
                height[i] = top;
            }

            best = Math.max(best, top);
            res.add(best);
        }
        return res;
    }

    // V2
    // IDEA: SEGMENT TREE WITH LAZY ASSIGNMENT over the compressed columns
    /**
     *  `max over a range` plus `assign a value to a whole range` is exactly a lazy
     *  segment tree, so each drop costs O(log n) instead of O(n).
     *
     *  -> O(n log n) overall, the only version that stays fast as n grows.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    private int[] segMax;
    private int[] segLazy;

    public List<Integer> fallingSquares_2(int[][] positions) {
        TreeSet<Integer> xsSet = new TreeSet<>();
        for (int[] p : positions) {
            xsSet.add(p[0]);
            xsSet.add(p[0] + p[1]);
        }
        List<Integer> xs = new ArrayList<>(xsSet);
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < xs.size(); i++) {
            idx.put(xs.get(i), i);
        }

        int m = Math.max(1, xs.size());
        segMax = new int[4 * m];
        segLazy = new int[4 * m];

        List<Integer> res = new ArrayList<>();
        int best = 0;

        for (int[] p : positions) {
            int lo = idx.get(p[0]);
            int hi = idx.get(p[0] + p[1]) - 1;   // inclusive
            if (hi < lo) {
                hi = lo;
            }
            int base = segQuery(1, 0, m - 1, lo, hi);
            int top = base + p[1];
            segAssign(1, 0, m - 1, lo, hi, top);
            best = Math.max(best, top);
            res.add(best);
        }
        return res;
    }

    private void segPush(int node) {
        if (segLazy[node] != 0) {
            for (int child = node * 2; child <= node * 2 + 1; child++) {
                segLazy[child] = segLazy[node];
                segMax[child] = segLazy[node];
            }
            segLazy[node] = 0;
        }
    }

    private int segQuery(int node, int lo, int hi, int ql, int qr) {
        if (qr < lo || hi < ql) {
            return 0;
        }
        if (ql <= lo && hi <= qr) {
            return segMax[node];
        }
        segPush(node);
        int mid = lo + (hi - lo) / 2;
        return Math.max(segQuery(node * 2, lo, mid, ql, qr),
                        segQuery(node * 2 + 1, mid + 1, hi, ql, qr));
    }

    private void segAssign(int node, int lo, int hi, int ql, int qr, int val) {
        if (qr < lo || hi < ql) {
            return;
        }
        if (ql <= lo && hi <= qr) {
            segMax[node] = val;
            segLazy[node] = val;
            return;
        }
        segPush(node);
        int mid = lo + (hi - lo) / 2;
        segAssign(node * 2, lo, mid, ql, qr, val);
        segAssign(node * 2 + 1, mid + 1, hi, ql, qr, val);
        segMax[node] = Math.max(segMax[node * 2], segMax[node * 2 + 1]);
    }

    // V3
    // IDEA: MAINTAIN A LIST OF DISJOINT (start, end, height) SEGMENTS
    /**
     *  Keep the skyline as a set of disjoint segments and, on each drop, SPLIT the
     *  overlapped ones and replace their middles with the new height.
     *
     *  The segment list IS the skyline, so this version can also answer `what does
     *  the profile look like now?` -- which neither the array nor the interval-list
     *  version of V0 exposes directly.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public List<Integer> fallingSquares_3(int[][] positions) {
        // disjoint segments [start, end) with a height, kept sorted by start
        List<int[]> sky = new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        int best = 0;

        for (int[] p : positions) {
            int left = p[0];
            int right = p[0] + p[1];

            int base = 0;
            for (int[] seg : sky) {
                if (left < seg[1] && seg[0] < right) {
                    base = Math.max(base, seg[2]);
                }
            }
            int top = base + p[1];

            List<int[]> next = new ArrayList<>();
            for (int[] seg : sky) {
                if (seg[1] <= left || seg[0] >= right) {
                    next.add(seg);            // untouched
                    continue;
                }
                if (seg[0] < left) {
                    next.add(new int[] { seg[0], left, seg[2] });
                }
                if (seg[1] > right) {
                    next.add(new int[] { right, seg[1], seg[2] });
                }
            }
            next.add(new int[] { left, right, top });
            next.sort(Comparator.comparingInt(s -> s[0]));
            sky = next;

            best = Math.max(best, top);
            res.add(best);
        }
        return res;
    }

}
