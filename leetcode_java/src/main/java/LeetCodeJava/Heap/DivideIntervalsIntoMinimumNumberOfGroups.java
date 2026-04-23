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
     *  3. If the current interval
     *     `starts after the` `earliest` `ending` interval,
     *     -> REUSE that group.
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
    /**  Dry run:
     *
     *  ->
     *    Input: intervals = [[5,10],[6,8],[1,5],[2,3],[1,10]]
     *    Output: 3
     *
     * Time →
     * 1    2    3    4    5    6    7    8    9    10
     * ------------------------------------------------
     * [1,5]   █████
     * [1,10]  ███████████████
     * [2,3]        ██
     * [5,10]            █████████
     * [6,8]                 ███
     *
     *
     *
     *  ->
     *
     *   Let’s dry-run the **min-heap solution** step by step for your input:
     *
     * ```
     * NOTE !!!
     *
     *
     * intervals = [[5,10],[6,8],[1,5],[2,3],[1,10]]
     * ```
     *
     * ---
     *
     * ## 🔹 Step 1: Sort by start
     *
     * ```text
     * [1,5], [1,10], [2,3], [5,10], [6,8]
     * ```
     *
     * ---
     *
     * ## 🔹 Step 2: Process with min-heap (stores end times)
     *
     * We maintain:
     *
     * * `pq` = min-heap of end times
     * * rule: reuse group if `pq.peek() < start`
     *
     * ---
     *
     * ### ▶️ Iteration 1: [1,5]
     *
     * * pq = []
     * * no group → create one
     *
     * ```
     * pq = [5]
     * ```
     *
     * ---
     *
     * ### ▶️ Iteration 2: [1,10]
     *
     * * pq.peek() = 5
     * * 5 < 1 ❌ (overlap, since inclusive)
     *
     * → need new group
     *
     * ```
     * pq = [5, 10]
     * ```
     *
     * ---
     *
     * ### ▶️ Iteration 3: [2,3]
     *
     * * pq.peek() = 5
     * * 5 < 2 ❌
     *
     * → still overlapping → new group
     *
     * ```
     * pq = [3, 10, 5]   // (heap internally sorted → [3,5,10])
     * ```
     *
     * ---
     *
     * ### ▶️ Iteration 4: [5,10]
     *
     * * pq.peek() = 3
     * * 3 < 5 ✅ → reuse group
     *
     * → remove 3, add 10
     *
     * ```
     * pq = [5, 10, 10]
     * ```
     *
     * ---
     *
     * ### ▶️ Iteration 5: [6,8]
     *
     * * pq.peek() = 5
     * * 5 < 6 ✅ → reuse group
     *
     * → remove 5, add 8
     *
     * ```
     * pq = [8, 10, 10]
     * ```
     *
     * ---
     *
     * ## 🔹 Final result
     *
     * ```
     * pq.size() = 3
     * ```
     *
     * ✅ Answer = **3**
     *
     * ---
     *
     * ## 🧠 Key moment to notice
     *
     * At time around `2`:
     *
     * ```
     * [1,5], [1,10], [2,3]
     * ```
     *
     * All overlap → **3 groups needed**
     *
     * That’s exactly what the heap captures.
     *
     * ---
     *
     * ## ⚠️ Subtle but critical detail
     *
     * Why this works:
     *
     * ```java
     * if (pq.peek() < start)
     * ```
     *
     * NOT:
     *
     * ```java
     * <=
     * ```
     *
     * Because intervals are **inclusive**:
     *
     * * `[1,5]` and `[5,10]` **overlap at 5**
     * * so they **cannot** share a group
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
             *
             * -> NOTE !!!
             *
             *  ` pq.peek()` is the min `end time` from PQ,
             *    NOT the prev one (check above dry run)
             *
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
    // IDEA: (Sweep Line) (gemini)
    /**
     *  Instead of intervals, we look at Events.
     *  An interval is just a +1 event at
     *  the start and a -1 event at the end.
     *
     *  - Logic: Break every interval into (time, type).
     *           Type +1 for start, -1
     *          for end. Sort all events.
     *
     *  - Pro Tip: Because [1, 5] and [5, 10] overlap,
     *             must process "starts" at time $X$
     *             before "ends" at time $X$ to
     *             correctly count the peak.
     *
     *  - Best for: Finding the "High-Water Mark" of resource usage.
     */
    public int minGroups_0_2(int[][] intervals) {
        List<int[]> events = new ArrayList<>();
        for (int[] in : intervals) {
            events.add(new int[] { in[0], 1 }); // Start event
            events.add(new int[] { in[1], -1 }); // End event
        }

        // Sort by time. If time is same, process START (1) before END (-1)
        // Actually, in this problem, [5,5] overlaps.
        // To handle this, we treat END as occurring slightly AFTER the start
        // or sort 1 before -1? No, 1 (start) must come before -1 (end) if we sort descending.
        // Standard trick: Sort by time, then by type (descending)
        Collections.sort(events, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);

        int maxGroups = 0, currentGroups = 0;
        for (int[] event : events) {
            currentGroups += event[1];
            maxGroups = Math.max(maxGroups, currentGroups);
        }
        return maxGroups;
    }


    // V0-3
    // IDEA: DIFFERENCE ARRAY (GEMINI)
    /**  Core idea:
     *
     * If the range of coordinates is fixed (like $1 \dots 10^6$),
     * we can use a difference array.
     * This is basically a "static" version of the Sweep Line.
     *
     *
     * - Logic: diff[start]++, diff[end + 1]-
     *
     * - Best for: Extremely high $N$ (number of intervals)
     *      but a limited coordinate range.
     *
     */
    public int minGroups_0_3(int[][] intervals) {
        int[] diff = new int[1000002]; // Max range based on constraints
        for (int[] in : intervals) {
            diff[in[0]]++;
            diff[in[1] + 1]--;
        }

        int maxGroups = 0, cur = 0;
        for (int count : diff) {
            cur += count;
            maxGroups = Math.max(maxGroups, cur);
        }
        return maxGroups;
    }


    // V0-5
    // IDEA: SORT + PQ (GEMINI)
    public int minGroups_0_5(int[][] intervals) {
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


    // V0-6
    // IDEA: PQ + SORT (gpt)
    public int minGroups_0_6(int[][] intervals) {
        // sort by start
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // min heap storing end times
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // if earliest group is free, reuse it
            if (!pq.isEmpty() && pq.peek() < start) {
                pq.poll();
            }

            // assign current interval to a group
            pq.offer(end);
        }

        return pq.size();
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
