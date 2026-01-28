package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/interval-list-intersections/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 986. Interval List Intersections
 * Medium
 * Topics
 * Companies
 * You are given two lists of closed intervals, firstList and secondList, where firstList[i] = [starti, endi] and secondList[j] = [startj, endj]. Each list of intervals is pairwise disjoint and in sorted order.
 *
 * Return the intersection of these two interval lists.
 *
 * A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.
 *
 * The intersection of two closed intervals is a set of real numbers that are either empty or represented as a closed interval. For example, the intersection of [1, 3] and [2, 4] is [2, 3].
 *
 *
 *
 * Example 1:
 *
 *
 * Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
 * Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
 * Example 2:
 *
 * Input: firstList = [[1,3],[5,9]], secondList = []
 * Output: []
 *
 *
 * Constraints:
 *
 * 0 <= firstList.length, secondList.length <= 1000
 * firstList.length + secondList.length >= 1
 * 0 <= starti < endi <= 109
 * endi < starti+1
 * 0 <= startj < endj <= 109
 * endj < startj+1
 */
public class IntervalListIntersections {

    // V0
    // IDEA: 2 POINTER (on 2 arrays) + boundary check (fixed by gpt)
    class Solution {
        public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {

            // edge
            if (firstList.length == 0 || secondList.length == 0) {
                return new int[][] {};
            }

            // 2 pointers
            // define 2 pointers, one for firstList, the other for secondList
            int i = 0;
            int j = 0;
            List<List<Integer>> collected = new ArrayList<>();
            // ONLY loop while i < firstList len and j < secondList
            while (i < firstList.length && j < secondList.length) {
                int[] firstVal = firstList[i];
                int[] secondVal = secondList[j];
                int maxStart = Math.max(firstVal[0], secondVal[0]);
                int minEnd = Math.min(firstVal[1], secondVal[1]);
                // check if there is an overlap
                if (maxStart <= minEnd) {
                    List<Integer> cur = new ArrayList<>();
                    // start idx
                    cur.add(maxStart);
                    // end idx
                    cur.add(minEnd);
                    collected.add(cur);
                }

                // NOTE !!! below is WRONG
                // NOTE !!! move idx in list if it NOT possible to make any new overlap
                // per current idx
                // if (maxStart > firstVal[1]){
                // i += 1;
                // }
                // if (maxStart > secondVal[1]){
                // j += 1;
                // }

                // Move the pointer for the interval that ends first
                // NOTE !!! use below logic instead
                /**
                 *  NOTE !!! we only move `1 idx` at once
                 *
                 *
                 * The logic for moving the pointers is based on the
                 * idea of always advancing to the next potential overlap in
                 * the list that ends first. Here's the reasoning behind it:
                 *
                 *
                 * Given two intervals:
                 *
                 * firstVal = [start1, end1] from firstList
                 * secondVal = [start2, end2] from secondList
                 *
                 *
                 * -> We compare end1 and end2 (the end times of both intervals):
                 *
                 * -> If end1 < end2:
                 *     This means that the interval in firstList ends earlier
                 *     than the interval in secondList. So, we increment
                 *     the pointer i to move to the next interval in firstList
                 *     because the current interval from firstList is finished,
                 *     and no further intersections with the current interval from secondList can happen.
                 *
                 * -> If end1 >= end2:
                 *     This means that the interval in secondList ends earlier
                 *     (or at the same time as the firstList interval).
                 *     Therefore, we increment the pointer j to move to the next interval in secondList,
                 *     because the current interval from secondList is finished,
                 *     and no further intersections with the current interval from firstList can happen.
                 *
                 */
                if (firstVal[1] < secondVal[1]) {
                    i++;
                } else {
                    j++;
                }
            }

           // System.out.println(">>> collected = " + collected);

            // NOTE !!! init res with (list.size(), 2) dimension
            int[][] res = new int[collected.size()][2];
            for (int k = 0; k < collected.size(); k++) {
                res[k] = new int[] { collected.get(k).get(0), collected.get(k).get(1) }; // ??? TODO: check !!!!
            }

            //System.out.println(">>> res = " + res);

            return res;
        }

    }

    // V0-1
    // IDEA: SCANNING LINE (fixed by gpt)
    /**
     *
     * The scanning line approach involves:
     *  - 1. Creating Events:
     *        For each interval in firstList and secondList,
     *        create events for start and end.
     *
     *  - 2. Sorting Events:
     *       Sort the events by position.
     *       If positions are the same, process start before end.
     *
     *  - 3. Tracking Overlap:
     *       Use a counter to track overlapping
     *       intervals as you process each event.
     *       If two intervals overlap, calculate and add their intersection.
     *
     */
    public int[][] intervalIntersection_0_1(int[][] firstList, int[][] secondList) {
        List<int[]> events = new ArrayList<>();
        List<int[]> result = new ArrayList<>();

        // Create events for firstList
        for (int[] interval : firstList) {
            events.add(new int[]{interval[0], 1}); // Start of interval
            /** NOTE !!! `interval[1] + 1` for exclusive */
            events.add(new int[]{interval[1] + 1, -1}); // End of interval (exclusive)
        }

        // Create events for secondList
        for (int[] interval : secondList) {
            events.add(new int[]{interval[0], 1}); // Start of interval
            /** NOTE !!! `interval[1] + 1` for exclusive */
            events.add(new int[]{interval[1] + 1, -1}); // End of interval (exclusive)
        }

        // Sort events: First by position, then by type (-1 before 1 if same position)
        events.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

        int active = 0; // Number of active overlapping intervals
        Integer prev = null; // Previous event position

        // Process events
        for (int[] event : events) {
            int pos = event[0];
            int type = event[1];

      /**
       * NOTE !!!!  below logic
       *
       *
       *
       * 1) Key Variables
       * 	1.	prev:
       * 	  - Tracks the start of the current active interval range being processed.
       * 	  - This helps in determining where an intersection starts or ends.
       * 	2.	active:
       * 	  - A counter that tracks the number of overlapping intervals at the current position.
       * 	  - When active == 2, it indicates two intervals are overlapping, and an intersection exists.
       * 	3.	pos:
       * 	  - The current position (from the sorted events list) where an event is being processed.
       * 	  - Events can represent the start or end of an interval.
       *
       *
       *  2) Further explanation:
       *
       *  1. Condition Check: prev != null && active == 2
       * 	•	Purpose: Ensure there is an overlap (active == 2) and a valid prev position exists.
       * 	•	Reason:
       * 	   - When two intervals overlap, active will equal 2 during the overlap.
       * 	   - If the previous position (prev) is not null, it means we can form an intersection range starting from prev up to pos - 1.
       *
       */
      if (prev != null && active == 2) {
           // Add an intersection for overlapping intervals
           result.add(new int[]{prev, pos - 1});
      }

      // Update active intervals and previous position
      /**
       * 3. Update active and prev
       * 	- active += type;
       * 	  - Updates the overlap count.
       * 	  - type is either +1 (for interval start) or -1 (for interval end).
       * 	      - Example:
       * 	          - If an interval starts, active increases.
       * 	          - If an interval ends, active decreases.
       * 	- prev = pos;
       * 	    - Moves the prev pointer to the current position (pos).
       * 	    - Ensures prev always reflects the start of the next potential intersection.
       *
       */
      active += type; // NOTE !!! type could be 1 or -1 (start of interval or end of interval)
      prev = pos;
     }

        return result.toArray(new int[result.size()][2]);
    }

    // V1
    // https://leetcode.com/problems/interval-list-intersections/solutions/1593579/java-two-pointers-most-intutive-by-chait-8cfr/
    // IDEA: 2 POINTERS
    public int[][] intervalIntersection_1(int[][] firstList, int[][] secondList) {
        if (firstList.length == 0 || secondList.length == 0)
            return new int[0][0];
    /**
     *  NOTE !!!!
     *   - i and j are pointers used to iterate through
     *      `firstList` and `secondList` respectively.
     *
     * 	 - `startMax` and `endMin` are used to compute
     * 	   the `intersection` of the current intervals
     * 	   from firstList and secondList.
     *
     * 	 - ans is a list to store the resulting intersection intervals.
     */
        int i = 0;
        int j = 0;
        int startMax = 0, endMin = 0;
        List<int[]> ans = new ArrayList<>();

    /**
     *
     *   - The loop continues as long as
     *      there are intervals remaining in `BOTH lists`.
     *
     *   - `startMax` is the maximum of the `START points` of the two
     *     intervals (firstList[i] and secondList[j]).
     * 	     -> This ensures the intersection starts no earlier than both intervals.
     *
     * 	 - `endMin` is the minimum of the `END points` of the two intervals.
     *
     * 	 - This ensures the intersection ends no later than the earlier of
     * 	   the two intervals.
     *
     */
    while (i < firstList.length && j < secondList.length) {
      startMax = Math.max(firstList[i][0], secondList[j][0]);
      endMin = Math.min(firstList[i][1], secondList[j][1]);

      // you have end greater than start and you already know that this interval is
      // surrounded with startMin and endMax so this must be the intersection
      /**
       *
       *  - If endMin >= startMax, it means there is an intersection between the two intervals.
       *    ->  Add the intersection [startMax, endMin] to the result list.
       */
      if (endMin >= startMax) {
        ans.add(new int[] {startMax, endMin});
      }

      // the interval with min end has been covered completely and have no chance to
      // intersect with any other interval so move that list's pointer
      /**
       * - Since the intervals are sorted and disjoint:
       * 	- If the interval from firstList ends first (or at the same time), increment i.
       * 	- If the interval from secondList ends first (or at the same time), increment j.
       *    -> This ensures that the interval which has been fully processed is skipped, moving to the next potential candidate for intersection.
       *
       */
      if (endMin == firstList[i][1]) i++;
      if (endMin == secondList[j][1]) j++;
        }

        return ans.toArray(new int[ans.size()][2]);
    }

    // V2
    // https://leetcode.com/problems/interval-list-intersections/solutions/592550/2-ms-faster-than-9963-of-java-and-memory-0pqj/
    // IDEA: 2 POINTERS
    public int[][] intervalIntersection_2(int[][] A, int[][] B) {
        int i = 0;
        int j = 0;
        List<int[]> list = new ArrayList<>();
        while (i < A.length && j < B.length) {
            int lower = Math.max(A[i][0], B[j][0]);
            int upper = Math.min(A[i][1], B[j][1]);
            if (lower <= upper) {
                list.add(new int[] { lower, upper });
            }
            if (A[i][1] < B[j][1]) {
                i++;
            } else {
                j++;
            }

        }
        return list.toArray(new int[list.size()][]);
    }

    // V3
    // https://leetcode.com/problems/interval-list-intersections/solutions/6048050/simple-solution-with-diagrams-in-short-v-u77n/
    // IDEA: 2 POINTERS
    public int[][] intervalIntersection_3(int[][] firstList, int[][] secondList) {
        List<int[]> intersections = new ArrayList<>(); // to store all
        // intersecting intervals
        // index "i" to iterate over the length of list a and index "j"
        // to iterate over the length of list b
        int i = 0, j = 0;
        // while loop will break whenever either of the lists ends
        while (i < firstList.length && j < secondList.length) {
            // Let's check if firstList[i] intersects secondList[j]
            // 1. start - the potential startpoint of the intersection
            // 2. end - the potential endpoint of the intersection
            int start = Math.max(firstList[i][0], secondList[j][0]);
            int end = Math.min(firstList[i][1], secondList[j][1]);

            if (start <= end) // if this is an actual intersection
                intersections.add(new int[] { start, end }); // add it to the list
            // Move forward in the list whose interval ends earlier
            if (firstList[i][1] < secondList[j][1])
                i += 1;
            else
                j += 1;
        }

        return intersections.toArray(new int[0][]);
    }
    


}
