package LeetCodeJava.Sort;

// https://leetcode.com/problems/average-height-of-buildings-in-each-segment/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *  2015. Average Height of Buildings in Each Segment
 *  Medium
 *
 *  A perfectly straight street is represented by a number line. The street has
 *  building(s) on it and is represented by a 2D integer array buildings, where
 *  buildings[i] = [start_i, end_i, height_i]. This means that there is a building
 *  with height_i in the half-closed segment [start_i, end_i).
 *
 *  You want to describe the heights of the buildings on the street with the
 *  minimum number of non-overlapping segments. The street can be represented by
 *  the 2D integer array street where street[j] = [left_j, right_j, average_j]
 *  describes a half-closed segment [left_j, right_j) of the road where the average
 *  height of the buildings in the segment is average_j.
 *
 *  Given buildings, return the 2D integer array street as described above
 *  (excluding any areas of the street where there are no buildings). You may
 *  return the array in any order. The average of n elements is the sum of the n
 *  elements divided (integer division) by n.
 *
 *  Example 1:
 *    Input: buildings = [[1,4,2],[3,9,4]]
 *    Output: [[1,3,2],[3,4,3],[4,9,4]]
 *    Explanation: 1..3 only building 1 -> 2 ; 3..4 both -> (2+4)/2 = 3 ;
 *                 4..9 only building 2 -> 4.
 *
 *  Example 2:
 *    Input: buildings = [[1,3,2],[2,5,3],[2,8,3]]
 *    Output: [[1,3,2],[3,8,3]]
 *    Explanation: equal consecutive averages are merged into one segment.
 *
 *  Constraints:
 *    1 <= buildings.length <= 10^5
 *    buildings[i].length == 3
 *    0 <= start_i < end_i <= 10^8
 *    1 <= height_i <= 10^5
 */
public class AverageHeightOfBuildingsInEachSegment {

    // V0
    // IDEA: SWEEP LINE / DIFFERENCE MAP ON EVENT POINTS + MERGE EQUAL SEGMENTS
    //       at x = start : +1 building, +height ; at x = end : -1, -height.
    //       walking the event points in sorted order gives, between two
    //       consecutive events, a constant (count, sum) -> average = sum / count.
    //       a gap with count == 0 must NOT be emitted, and it also breaks the
    //       merge chain -> only merge into the previous segment when that one
    //       ENDS exactly where the current one starts AND the average matches.
    /**
     * time = O(m log m), m = buildings.length
     * space = O(m)
     */
    public int[][] averageHeightOfBuildings(int[][] buildings) {
        // x -> [countDelta, sumDelta]
        TreeMap<Integer, long[]> delta = new TreeMap<>();
        for (int[] b : buildings) {
            long[] s = delta.get(b[0]);
            if (s == null) {
                s = new long[2];
                delta.put(b[0], s);
            }
            s[0] += 1;
            s[1] += b[2];

            long[] e = delta.get(b[1]);
            if (e == null) {
                e = new long[2];
                delta.put(b[1], e);
            }
            e[0] -= 1;
            e[1] -= b[2];
        }

        List<int[]> res = new ArrayList<>();
        long total = 0;
        long count = 0;
        int prev = -1;
        for (Map.Entry<Integer, long[]> entry : delta.entrySet()) {
            int x = entry.getKey();
            if (count > 0) {
                int avg = (int) (total / count);
                if (!res.isEmpty()
                        && res.get(res.size() - 1)[1] == prev
                        && res.get(res.size() - 1)[2] == avg) {
                    res.get(res.size() - 1)[1] = x;
                } else {
                    res.add(new int[]{prev, x, avg});
                }
            }
            count += entry.getValue()[0];
            total += entry.getValue()[1];
            prev = x;
        }

        return res.toArray(new int[0][]);
    }
}
