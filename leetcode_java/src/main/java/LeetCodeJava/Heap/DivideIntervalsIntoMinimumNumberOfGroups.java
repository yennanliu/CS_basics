package LeetCodeJava.Heap;

// https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/description/

import java.util.*;

/**
 *  2406. Divide Intervals Into Minimum Number of Groups
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 2D integer array intervals where intervals[i] = [lefti, righti] represents the inclusive interval [lefti, righti].
 *
 * You have to divide the intervals into one or more groups such that each interval is in exactly one group, and no two intervals that are in the same group intersect each other.
 *
 * Return the minimum number of groups you need to make.
 *
 * Two intervals intersect if there is at least one common number between them. For example, the intervals [1, 5] and [5, 8] intersect.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[5,10],[6,8],[1,5],[2,3],[1,10]]
 * Output: 3
 * Explanation: We can divide the intervals into the following groups:
 * - Group 1: [1, 5], [6, 8].
 * - Group 2: [2, 3], [5, 10].
 * - Group 3: [1, 10].
 * It can be proven that it is not possible to divide the intervals into fewer than 3 groups.
 * Example 2:
 *
 * Input: intervals = [[1,3],[5,6],[8,10],[11,13]]
 * Output: 1
 * Explanation: None of the intervals overlap, so we can put all of them in one group.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 105
 * intervals[i].length == 2
 * 1 <= lefti <= righti <= 106
 *
 *
 */
public class DivideIntervalsIntoMinimumNumberOfGroups {

    // V0
//    public int minGroups(int[][] intervals) {
//
//    }


    // V0-1
    // IDEA: SORT + PQ (gpt)
    /**  Core idea:
     *
     *  1. `Sort` intervals by `start` time.
     *
     *  2. Use a min-heap (priority queue)
     *     to track the `earliest` `ending` group.
     *
     *  3. If the current interval starts
     *     after the `earliest` `ending` interval, REUSE that group.
     *
     *  4. Otherwise, create a new group.
     *
     *
     *  -----------
     *
     *  NOTE !!!
     *
     *   PQ stores `end` times of `current groups`
     *
     *
     */
    public int minGroups_0_1(int[][] intervals) {
        // sort by start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        /** NOTE !!!!
         *
         *
         *  PQ stores `end` times of `current groups`
         *
         *  (min PQ, sort on end time (small -> big))
         */
        // min heap storing end times
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int[] interval : intervals) {
            // if earliest ending group is available
            /** NOTE !!!
             *
             *  - Check if any group is free
             *
             *  - pq.peek() = earliest ending group
             *
             *  - If:
             *
             *      earliest end < current start
             *
             *     → no overlap → we can reuse that group
             *
             *
             */
            /** NOTE !!!
             *
             * Smallest value (pq.peek()) = earliest available group
             */
            if (!pq.isEmpty() && pq.peek() < interval[0]) {
                /** NOTE !!!
                 *
                 * Remove that group (we're reusing it)
                 */
                pq.poll(); // reuse this group
            }
            /** NOTE !!!
             *
             *  - Add current interval’s end time
             *  - This means:
             *    -> “this group is now occupied until interval[1]”
             */
            pq.offer(interval[1]); // add current interval
        }

        /** NOTE !!!
         *
         * Number of groups needed = number of overlapping intervals at peak
         * PQ size = how many groups are currently active
         */
        return pq.size();
    }


    // V0-2
    // IDEA: SORT + PQ (GEMINI)
    public int minGroups_0_2(int[][] intervals) {
        int n = intervals.length;
        int[] starts = new int[n];
        int[] ends = new int[n];

        // 1. Separate starts and ends
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }

        // 2. Sort both independently
        Arrays.sort(starts);
        Arrays.sort(ends);

        int groupCount = 0;
        int endPointer = 0;

        // 3. Sweep through the starts
        for (int start : starts) {
            // If the current start time is GREATER than the earliest end time,
            // it means an existing group has become free.
            // Note: Use 'start > ends[endPointer]' if they DON'T overlap at the same number.
            // But LC 2406 says [1, 5] and [5, 10] OVERLAP, so we need 'start > ends[endPointer]'.
            if (start > ends[endPointer]) {
                endPointer++;
            } else {
                // Otherwise, we need a new group
                groupCount++;
            }
        }

        return groupCount;
    }


    // V1-1
    // IDEA: Sorting or Priority Queue
    // https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/editorial/
    public int minGroups_1_1(int[][] intervals) {
        // Convert the intervals to two events
        // start as {start, 1} and end as {end + 1, -1}
        List<int[]> events = new ArrayList<>();

        for (int[] interval : intervals) {
            events.add(new int[] { interval[0], 1 }); // Start event
            events.add(new int[] { interval[1] + 1, -1 }); // End event (interval[1] + 1)
        }

        // Sort the events first by time, and then by type (1 for start, -1 for end).
        Collections.sort(events, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]); // Sort by type (1 before -1)
            } else {
                return Integer.compare(a[0], b[0]); // Sort by time
            }
        });

        int concurrentIntervals = 0;
        int maxConcurrentIntervals = 0;

        // Sweep through the events
        for (int[] event : events) {
            concurrentIntervals += event[1]; // Track currently active intervals
            maxConcurrentIntervals = Math.max(
                    maxConcurrentIntervals,
                    concurrentIntervals); // Update max
        }

        return maxConcurrentIntervals;
    }



    // V1-2
    // IDEA: Line Sweep Algorithm With Ordered Container
    // https://leetcode.com/problems/divide-intervals-into-minimum-number-of-groups/editorial/
    public int minGroups_1_2(int[][] intervals) {
        // Use a TreeMap to store the points and their counts
        TreeMap<Integer, Integer> pointToCount = new TreeMap<>();

        // Mark the starting and ending points in the TreeMap
        for (int[] interval : intervals) {
            pointToCount.put(
                    interval[0],
                    pointToCount.getOrDefault(interval[0], 0) + 1
            );
            pointToCount.put(
                    interval[1] + 1,
                    pointToCount.getOrDefault(interval[1] + 1, 0) - 1
            );
        }

        int concurrentIntervals = 0;
        int maxConcurrentIntervals = 0;

        // Iterate over the entries of the TreeMap in ascending order of keys
        for (Map.Entry<Integer, Integer> entry : pointToCount.entrySet()) {
            concurrentIntervals += entry.getValue(); // Update currently active intervals
            maxConcurrentIntervals = Math.max(
                    maxConcurrentIntervals,
                    concurrentIntervals
            ); // Update max intervals
        }

        return maxConcurrentIntervals;
    }



    // V2




}
