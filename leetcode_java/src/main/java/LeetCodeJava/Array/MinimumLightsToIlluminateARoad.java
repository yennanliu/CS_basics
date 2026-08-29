package LeetCodeJava.Array;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/minimum-lights-to-illuminate-a-road/

/**
 *  3964. Minimum Lights to Illuminate a Road
 *  Medium
 *
 *  You are given an integer array lights of length n, representing positions
 *  0 through n - 1 on a road.
 *
 *  For each position i:
 *   - If lights[i] = v, where v > 0, there is a working bulb at position i that
 *     illuminates every position from max(0, i - v) to min(n - 1, i + v), inclusive.
 *   - If lights[i] = 0, there is no working bulb at position i.
 *
 *  A position is visible if it is illuminated by at least one working bulb.
 *
 *  You may install additional bulbs at any positions. Each additional bulb
 *  installed at position j illuminates positions from max(0, j - 1) to
 *  min(n - 1, j + 1), inclusive.
 *
 *  Return the minimum number of additional bulbs required to make every position
 *  on the road visible.
 *
 *
 *  Example 1:
 *
 *  Input: lights = [0,0,0,0]
 *  Output: 2
 *  Explanation: install a bulb at position 1 (covers [0,2]) and one at position 3.
 *
 *  Example 2:
 *
 *  Input: lights = [0,0,0,2,0]
 *  Output: 1
 *  Explanation: lights[3] = 2 covers [1,4]; one extra bulb at position 1 covers [0,2].
 *
 *
 *  Constraints:
 *
 *  1 <= n == lights.length <= 10^5
 *  0 <= lights[i] <= n
 */
public class MinimumLightsToIlluminateARoad {

    // V0
    // IDEA: DIFF ARRAY for existing coverage + GREEDY — at the leftmost dark spot i,
    //       the best new bulb sits at i + 1 (covers i, i+1, i+2), so jump 3 ahead
    /**
     * time = O(n)
     * space = O(n)
     */
    public int minLights(int[] lights) {
        int n = lights.length;

        // diff needs n + 1 slots so that "right + 1" is always in range
        int[] diff = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int v = lights[i];
            if (v > 0) {
                int left = Math.max(0, i - v);
                int right = Math.min(n - 1, i + v);
                diff[left] += 1;
                diff[right + 1] -= 1;
            }
        }

        // prefix sum -> how many working bulbs cover each position
        int[] covered = new int[n];
        int run = 0;
        for (int i = 0; i < n; i++) {
            run += diff[i];
            covered[i] = run;
        }

        int res = 0;
        int i = 0;
        while (i < n) {
            if (covered[i] == 0) {
                res++;
                // new bulb at i + 1 lights up i, i + 1, i + 2
                i += 3;
            } else {
                i++;
            }
        }
        return res;
    }

    // V1
    // IDEA: INTERVAL MERGE — build the lit intervals, sort by left edge, sweep them
    //       and pay ceil(gap / 3) bulbs for every dark gap in between
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minLights_1(int[] lights) {
        int n = lights.length;
        List<int[]> intervals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int v = lights[i];
            if (v > 0) {
                intervals.add(new int[] { Math.max(0, i - v), Math.min(n - 1, i + v) });
            }
        }
        intervals.sort((a, b) -> Integer.compare(a[0], b[0]));

        int res = 0;
        int cur = 0; // first position not yet known to be lit
        for (int[] p : intervals) {
            if (p[0] > cur) {
                int gap = p[0] - cur;
                res += (gap + 2) / 3; // each new bulb lights 3 consecutive spots
            }
            cur = Math.max(cur, p[1] + 1);
        }
        if (cur < n) {
            res += (n - cur + 2) / 3;
        }
        return res;
    }

    // V2
    // IDEA: brute force O(n^2) — literally paint every position each working bulb
    //       reaches, then greedily cover the dark spots; readable reference
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int minLights_2(int[] lights) {
        int n = lights.length;
        boolean[] lit = new boolean[n];
        for (int i = 0; i < n; i++) {
            int v = lights[i];
            if (v > 0) {
                for (int j = Math.max(0, i - v); j <= Math.min(n - 1, i + v); j++) {
                    lit[j] = true;
                }
            }
        }

        int res = 0;
        int i = 0;
        while (i < n) {
            if (!lit[i]) {
                res++;
                i += 3; // a new bulb placed at i + 1 covers i, i + 1, i + 2
            } else {
                i++;
            }
        }
        return res;
    }
}
