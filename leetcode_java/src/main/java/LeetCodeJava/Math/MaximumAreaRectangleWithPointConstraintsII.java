package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-area-rectangle-with-point-constraints-ii/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  3382. Maximum Area Rectangle With Point Constraints II
 *  Hard
 *
 *  There are n points on an infinite plane. You are given two integer arrays xCoord and
 *  yCoord where (xCoord[i], yCoord[i]) represents the coordinates of the ith point.
 *
 *  Your task is to find the maximum area of a rectangle that:
 *    - Can be formed using four of these points as its corners.
 *    - Does not contain any other point inside or on its border.
 *    - Has its edges parallel to the axes.
 *
 *  Return the maximum area that you can obtain or -1 if no such rectangle is possible.
 *
 *  Example 1:
 *    Input: xCoord = [1,1,3,3], yCoord = [1,3,1,3]
 *    Output: 4
 *
 *  Example 3:
 *    Input: xCoord = [1,1,3,3,1,3], yCoord = [1,3,1,3,2,2]
 *    Output: 2
 *
 *  Constraints:
 *    1 <= xCoord.length == yCoord.length <= 2 * 10^5
 *    0 <= xCoord[i], yCoord[i] <= 8 * 10^7
 *    All the given points are unique.
 */
public class MaximumAreaRectangleWithPointConstraintsII {

    private int[] tree;
    private int size;

    // V0
    // IDEA: SWEEP BY x, PAIRING *VERTICALLY ADJACENT* POINTS, COUNTING WITH A BIT
    //
    //   a valid rectangle's left edge holds two points with NOTHING between them on that
    //   edge - otherwise the extra point sits on the border. so on each vertical line
    //   only consecutive y's can be a rectangle's left (or right) edge, which brings the
    //   candidate pairs down from quadratic to O(n).
    //
    //   sweeping x from small to large, a pair (y1, y2) that was last seen at column x0
    //   closes a rectangle now. it is empty exactly when no other point entered the strip
    //   meanwhile, which a Fenwick tree over y answers by comparing the counts in
    //   [y1, y2] then and now: the difference must be exactly the 2 points just added on
    //   this column.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maxRectangleArea(int[] xCoord, int[] yCoord) {
        int n = xCoord.length;

        // sort point indices by (x, y)
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        final int[] xs0 = xCoord;
        final int[] ys0 = yCoord;
        Arrays.sort(idx, (p, q) -> {
            if (xs0[p] != xs0[q]) {
                return Integer.compare(xs0[p], xs0[q]);
            }
            return Integer.compare(ys0[p], ys0[q]);
        });

        // rank the y values (1-based)
        int[] sortedY = yCoord.clone();
        Arrays.sort(sortedY);
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sortedY[i] != sortedY[i - 1]) {
                sortedY[m++] = sortedY[i];
            }
        }
        size = m;
        tree = new int[size + 1];
        Map<Integer, Integer> rank = new HashMap<>();
        for (int i = 0; i < m; i++) {
            rank.put(sortedY[i], i + 1);
        }

        // (y1Rank, y2Rank) -> {lastX, countThen}
        Map<Long, long[]> last = new HashMap<>();
        long best = -1;

        int i = 0;
        while (i < n) {
            int j = i;
            int x = xCoord[idx[i]];
            while (j < n && xCoord[idx[j]] == x) {
                j++;
            }
            // insert this whole column first
            for (int t = i; t < j; t++) {
                add(rank.get(yCoord[idx[t]]));
            }
            // adjacent pairs on this column (already ascending in y)
            for (int t = i; t + 1 < j; t++) {
                int y1 = yCoord[idx[t]];
                int y2 = yCoord[idx[t + 1]];
                int r1 = rank.get(y1);
                int r2 = rank.get(y2);
                long cur = prefix(r2) - prefix(r1 - 1);
                long key = (long) r1 * (size + 1) + r2;
                long[] prev = last.get(key);
                if (prev != null && cur - prev[1] == 2) {
                    long area = (x - prev[0]) * (long) (y2 - y1);
                    if (area > best) {
                        best = area;
                    }
                }
                last.put(key, new long[]{x, cur});
            }
            i = j;
        }
        return best;
    }

    private void add(int i) {
        while (i <= size) {
            tree[i]++;
            i += i & (-i);
        }
    }

    private int prefix(int i) {
        int s = 0;
        while (i > 0) {
            s += tree[i];
            i -= i & (-i);
        }
        return s;
    }
}
