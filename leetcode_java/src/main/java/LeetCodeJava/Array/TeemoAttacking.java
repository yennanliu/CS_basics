package LeetCodeJava.Array;

// https://leetcode.com/problems/teemo-attacking/description/

import java.util.ArrayList;
import java.util.List;
/**
 * 495. Teemo Attacking
 * Easy
 *
 * Our hero Teemo is attacking an enemy Ashe with poison attacks! When Teemo attacks Ashe,
 * Ashe gets poisoned for a exactly duration seconds. More formally, an attack at second t
 * will mean Ashe is poisoned during the inclusive time interval [t, t + duration - 1].
 * If Teemo attacks again before the poison effect ends, the timer for it is reset,
 * and the poison effect will end duration seconds after the new attack.
 *
 * You are given a non-decreasing integer array timeSeries, where timeSeries[i] denotes that
 * Teemo attacks Ashe at second timeSeries[i], and an integer duration.
 *
 * Return the total number of seconds that Ashe is poisoned.
 *
 * Example 1:
 *
 * Input: timeSeries = [1,4], duration = 2
 * Output: 4
 * Explanation: Teemo's attacks on Ashe go as follows:
 * - At second 1, Teemo attacks, and Ashe is poisoned for seconds 1 and 2.
 * - At second 4, Teemo attacks, and Ashe is poisoned for seconds 4 and 5.
 * Ashe is poisoned for seconds 1, 2, 4, and 5, which is 4 seconds in total.
 *
 * Example 2:
 *
 * Input: timeSeries = [1,2], duration = 2
 * Output: 3
 * Explanation: Teemo's attacks on Ashe go as follows:
 * - At second 1, Teemo attacks, and Ashe is poisoned for seconds 1 and 2.
 * - At second 2 however, Teemo attacks again and resets the poison timer.
 *   Ashe is poisoned for seconds 2 and 3.
 * Ashe is poisoned for seconds 1, 2, and 3, which is 3 seconds in total.
 *
 *
 * Constraints:
 *
 * 1 <= timeSeries.length <= 10^4
 * 0 <= timeSeries[i], duration <= 10^7
 * timeSeries is sorted in non-decreasing order.
 *
 */
public class TeemoAttacking {

    // V0
    // IDEA: SIMULATION (interval merge)
    /**
     *  -> attack i contributes `min(duration, gap to next attack)` seconds,
     *     since a new attack RESETS (not stacks) the timer.
     *  -> the LAST attack always contributes the full duration.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        // edge
        if (timeSeries == null || timeSeries.length == 0 || duration == 0) {
            return 0;
        }

        int res = 0;
        for (int i = 0; i < timeSeries.length - 1; i++) {
            int gap = timeSeries[i + 1] - timeSeries[i];
            /** NOTE !!!
             *
             *  we take `min(gap, duration)`:
             *   - gap < duration      -> the poison got cut short (timer reset)
             *   - gap >= duration     -> the full duration applied
             */
            res += Math.min(gap, duration);
        }

        // the last attack is never interrupted
        return res + duration;
    }


    // V1
    // IDEA: TRACK THE CURRENT POISON END TIME (interval union)
    /**
     *  Keep `end` = the second at which the poison currently expires (exclusive).
     *  For each attack t, the newly poisoned seconds are max(0, (t + duration) - max(t, end)).
     *
     *  This is the generic `union of intervals` formulation -- it still works when
     *  timeSeries is NOT sorted-adjacent or when durations differ per attack.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findPoisonedDuration_1(int[] timeSeries, int duration) {
        if (timeSeries == null || timeSeries.length == 0 || duration == 0) {
            return 0;
        }

        int res = 0;
        int end = Integer.MIN_VALUE; // poison expires at `end` (exclusive)

        for (int t : timeSeries) {
            int start = Math.max(t, end);
            int stop = t + duration;
            if (stop > start) {
                res += stop - start;
            }
            end = stop;
        }

        return res;
    }

    // V2
    // IDEA: TOTAL MINUS THE OVERLAPS
    /**
     *  Start from the naive `n * duration` (as if no attack ever overlapped),
     *  then SUBTRACT the overlap of each adjacent pair:
     *      overlap = max(0, duration - gap)
     *
     *  Reads as an inclusion-exclusion counterpart to V0's min().
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findPoisonedDuration_2(int[] timeSeries, int duration) {
        if (timeSeries == null || timeSeries.length == 0 || duration == 0) {
            return 0;
        }

        int res = timeSeries.length * duration;
        for (int i = 0; i + 1 < timeSeries.length; i++) {
            int gap = timeSeries[i + 1] - timeSeries[i];
            res -= Math.max(0, duration - gap);
        }
        return res;
    }

    // V3
    // IDEA: MERGE INTO AN EXPLICIT INTERVAL LIST, THEN SUM THE LENGTHS
    /**
     *  Build the merged poisoned intervals explicitly and add up their widths.
     *
     *  The slowest of the four, but it is the only one that can also REPORT the
     *  poisoned intervals themselves, which is what a follow-up usually asks for.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int findPoisonedDuration_3(int[] timeSeries, int duration) {
        if (timeSeries == null || timeSeries.length == 0 || duration == 0) {
            return 0;
        }

        List<int[]> merged = new ArrayList<>();
        for (int t : timeSeries) {
            int start = t;
            int stop = t + duration;
            if (!merged.isEmpty() && merged.get(merged.size() - 1)[1] >= start) {
                // overlaps the previous interval -> extend it
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], stop);
            } else {
                merged.add(new int[] { start, stop });
            }
        }

        int res = 0;
        for (int[] itv : merged) {
            res += itv[1] - itv[0];
        }
        return res;
    }

}
