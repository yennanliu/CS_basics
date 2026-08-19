package LeetCodeJava.Sort;

// https://leetcode.com/problems/brightest-position-on-street/

import java.util.Map;
import java.util.TreeMap;

/**
 *  2021. Brightest Position on Street
 *  Medium
 *
 *  A perfectly straight street is represented by a number line. Each lights[i] =
 *  [position_i, range_i] means there is a street lamp at position_i lighting up
 *  [position_i - range_i, position_i + range_i] (inclusive).
 *
 *  The brightness of a position p is the number of street lamps that light up p.
 *
 *  Given lights, return the brightest position on the street. If there are multiple
 *  brightest positions, return the smallest one.
 *
 *  Example 1:
 *  Input: lights = [[-3,2],[1,2],[3,3]]
 *  Output: -1
 *
 *  Example 2:
 *  Input: lights = [[1,0],[0,1]]
 *  Output: 1
 *
 *  Example 3:
 *  Input: lights = [[1,2]]
 *  Output: -1
 *
 *  Constraints:
 *  1 <= lights.length <= 10^5
 *  lights[i].length == 2
 *  -10^8 <= position_i <= 10^8
 *  0 <= range_i <= 10^8
 */
public class BrightestPositionOnStreet {

    // V0
    // IDEA: SWEEP LINE — +1 at (pos - range), -1 at (pos + range + 1); a TreeMap keeps the
    //       event coordinates sorted and merges the deltas that share a coordinate
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int brightestPosition(int[][] lights) {

        TreeMap<Integer, Integer> events = new TreeMap<>();
        for (int[] l : lights) {
            int start = l[0] - l[1];
            int end = l[0] + l[1] + 1; // exclusive
            Integer s = events.get(start);
            events.put(start, s == null ? 1 : s + 1);
            Integer e = events.get(end);
            events.put(end, e == null ? -1 : e - 1);
        }

        int cur = 0;
        int best = 0;
        int bestPos = 0;
        for (Map.Entry<Integer, Integer> entry : events.entrySet()) {
            cur += entry.getValue();
            /**
             *  NOTE: strict `>` keeps the SMALLEST coordinate on ties,
             *        since we walk the coordinates in ascending order
             */
            if (cur > best) {
                best = cur;
                bestPos = entry.getKey();
            }
        }
        return bestPos;
    }
}
