package LeetCodeJava.Greedy;

// https://leetcode.com/problems/the-skyline-problem/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 *  218. The Skyline Problem
 *  Hard
 *
 *  A city's skyline is the outer contour of the silhouette formed by all the buildings
 *  in that city when viewed from a distance.
 *
 *  The geometric information of each building is given in the array buildings where
 *  buildings[i] = [lefti, righti, heighti].
 *
 *  Return the skyline formed by these buildings collectively, as a list of "key points"
 *  sorted by their x-coordinate. A key point [x, y] is the left endpoint of some
 *  horizontal segment in the skyline except the last point, which always has y = 0.
 *  There must be no consecutive horizontal lines of equal height in the output skyline.
 *
 *  Example 1:
 *    Input: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 *    Output: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 *
 *  Example 2:
 *    Input: buildings = [[0,2,3],[2,5,3]]
 *    Output: [[0,3],[5,0]]
 *
 *  Constraints:
 *    1 <= buildings.length <= 10^4
 *    0 <= lefti < righti <= 2^31 - 1
 *    1 <= heighti <= 2^31 - 1
 *    buildings is sorted by lefti in non-decreasing order.
 */
public class TheSkylineProblem {

    // V0
    // IDEA: sweep line. Turn each building into a start event (x, -h) and an end event (x, +h),
    //       sort them, and keep a multiset (TreeMap) of active heights; emit a key point
    //       whenever the current max height changes.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<int[]> events = new ArrayList<>();
        for (int[] b : buildings) {
            events.add(new int[]{b[0], -b[2]});  // start: negative height
            events.add(new int[]{b[1], b[2]});   // end:   positive height
        }

        // sort by x; at the same x: starts before ends, taller start first, shorter end first
        events.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return Integer.compare(a[0], b[0]);
                }
                return Integer.compare(a[1], b[1]);
            }
        });

        // active height -> count
        TreeMap<Integer, Integer> active = new TreeMap<>();
        active.put(0, 1); // ground level sentinel

        List<List<Integer>> res = new ArrayList<>();
        int prevMax = 0;

        for (int[] e : events) {
            int x = e[0];
            int h = e[1];
            if (h < 0) {
                int key = -h;
                Integer c = active.get(key);
                active.put(key, c == null ? 1 : c + 1);
            } else {
                Integer c = active.get(h);
                if (c != null) {
                    if (c == 1) {
                        active.remove(h);
                    } else {
                        active.put(h, c - 1);
                    }
                }
            }

            int curMax = active.lastKey();
            if (curMax != prevMax) {
                res.add(Arrays.asList(x, curMax));
                prevMax = curMax;
            }
        }
        return res;
    }
}
