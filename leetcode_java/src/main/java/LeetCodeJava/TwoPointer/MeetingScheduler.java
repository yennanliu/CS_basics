package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/meeting-scheduler/description/
// https://leetcode.ca/all/1229.html

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 1229. Meeting Scheduler
 * Given the availability time slots arrays slots1 and slots2 of two people and a meeting duration duration, return the earliest time slot that works for both of them and is of duration duration.
 *
 * If there is no common time slot that satisfies the requirements, return an empty array.
 *
 * The format of a time slot is an array of two elements [start, end] representing an inclusive time range from start to end.
 *
 * It is guaranteed that no two availability slots of the same person intersect with each other. That is, for any two time slots [start1, end1] and [start2, end2] of the same person, either start1 > end2 or start2 > end1.
 *
 *
 *
 * Example 1:
 *
 * Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 8
 * Output: [60,68]
 * Example 2:
 *
 * Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 12
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= slots1.length, slots2.length <= 10^4
 * slots1[i].length, slots2[i].length == 2
 * slots1[i][0] < slots1[i][1]
 * slots2[i][0] < slots2[i][1]
 * 0 <= slots1[i][j], slots2[i][j] <= 10^9
 * 1 <= duration <= 10^6
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon DoorDash Paypal pramp Uber
 * Problem Solution
 * 1229-Meeting-Scheduler
 *
 *
 */
public class MeetingScheduler {

    // V0
    // IDEA: SORT + 2 POINTERS
    // TODO: validate
    public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {

        List<Integer> res = new ArrayList<>();

        if (slots1 == null || slots2 == null || duration <= 0) {
            return res;
        }

        Arrays.sort(slots1, (a, b) -> a[0] - b[0]);
        Arrays.sort(slots2, (a, b) -> a[0] - b[0]);

        int i = 0;
        int j = 0;

        /** NOTE !!!
         *
         *  while loop on i, j index
         */
        while (i < slots1.length && j < slots2.length) {

            // overlap interval
            /** NOTE !!!
             *
             *  how we get the overlap start, and end idx
             */
            int overlapStart = Math.max(slots1[i][0], slots2[j][0]);
            int overlapEnd = Math.min(slots1[i][1], slots2[j][1]);

            // enough duration
            if (overlapEnd - overlapStart >= duration) {
                res.add(overlapStart);
                /** NOTE !!!
                 *
                 *  the `end` iex is `overlapStart + duration`
                 */
                res.add(overlapStart + duration);
                return res;
            }

            /** NOTE !!!
             *
             *  we move the slot has `smaller` end to next idx,
             *  since it is more UNLIKELY to form a `longer` overlap
             */
            // move the interval with smaller end
            if (slots1[i][1] < slots2[j][1]) {
                i++;
            } else {
                j++;
            }
        }

        return res;
    }


    // V0-1
    // IDEA: SORT + 2 POINTERS (GPT)
    // TODO: validate
    public List<Integer> minAvailableDuration_0_1(int[][] slots1, int[][] slots2, int duration) {

        List<Integer> res = new ArrayList<>();

        if (slots1 == null || slots2 == null || duration <= 0) {
            return res;
        }

        // sort by start time
        Arrays.sort(slots1, (a, b) -> a[0] - b[0]);
        Arrays.sort(slots2, (a, b) -> a[0] - b[0]);

        int i = 0;
        int j = 0;

        while (i < slots1.length && j < slots2.length) {

            // overlap interval
            int start = Math.max(slots1[i][0], slots2[j][0]);
            int end = Math.min(slots1[i][1], slots2[j][1]);

            // enough duration
            if (end - start >= duration) {
                res.add(start);
                res.add(start + duration);
                return res;
            }

            /** NOTE !!!
             *
             *   move the one that ends `earlier`
             *
             *   ---
             *
             *
             *   Reason:
             *
             * ->  Because the interval that ends `earlier `
             *     can NEVEN help produce a` better overlap later.`
             *
             *
             * Suppose we have:
             *
             * ```
             * slots1[i] = [10, 20]
             * slots2[j] = [15, 30]
             * ```
             *
             * Current overlap:
             *
             * ```
             * [15, 20]
             * ```
             *
             * Now compare end times:
             *
             * * `slots1[i]` ends at `20`
             * * `slots2[j]` ends at `30`
             *
             * Since `[10,20]` ends earlier, after time `20` it is useless.
             * It cannot overlap with any future interval beyond `20`.
             *
             * So we move `i++`.
             *
             * ---
             *
             * Another example:
             *
             * ```
             * slots1[i] = [10, 50]
             * slots2[j] = [20, 25]
             * ```
             *
             * Overlap:
             *
             * ```
             * [20, 25]
             * ```
             *
             * `slots2[j]` ends earlier (`25`).
             *
             * Even if we keep it and move `slots1`, this interval can never overlap past `25`.
             *
             * So we discard it:
             *
             * ```
             * j++;
             * ```
             *
             * ---
             *
             * The greedy intuition:
             *
             * At each step:
             *
             * ```
             * overlap = [max(start1, start2), min(end1, end2)]
             * ```
             *
             * The limiting factor is the **smaller end**.
             *
             * To possibly get a larger future overlap, we must keep the interval with the
             * larger end and discard the smaller-end interval.
             *
             * That’s why:
             *
             * ```
             * if (slots1[i][1] < slots2[j][1]) {
             *     i++;
             * } else {
             *     j++;
             * }
             * ```
             *
             * This guarantees:
             *
             * * no valid answer is skipped
             * * time complexity stays `O(n log n + m log m)` due to sorting + linear scan afterward.
             *
             *
             */
            // move the one that ends earlier
            if (slots1[i][1] < slots2[j][1]) {
                i++;
            } else {
                j++;
            }
        }

        return res;
    }


    // V0-2
    // IDEA: SORT + 2 POINTERS (gpt)
    // TODO: validate
    // IDEA: Sort + Two Pointers
    public List<Integer> minAvailableDuration_0_2(int[][] slots1, int[][] slots2, int duration) {
        List<Integer> res = new ArrayList<>();
        if (slots1 == null || slots2 == null || duration == 0) {
            return res;
        }

        // 1. Sort both slots by their start times in ascending order
        Arrays.sort(slots1, (o1, o2) -> Integer.compare(o1[0], o2[0]));
        Arrays.sort(slots2, (o1, o2) -> Integer.compare(o1[0], o2[0]));

        int i = 0;
        int j = 0;
        int l1 = slots1.length;
        int l2 = slots2.length;

        // 2. Parallel linear scan
        while (i < l1 && j < l2) {
            // Calculate intersection boundaries correctly between slots1 and slots2
            int overlapStart = Math.max(slots1[i][0], slots2[j][0]);
            int overlapEnd = Math.min(slots1[i][1], slots2[j][1]);

            // If the overlap can accommodate the duration requirement
            if (overlapEnd - overlapStart >= duration) {
                res.add(overlapStart);
                res.add(overlapStart + duration); // First available slot of required length
                return res;
            }

            /** NOTE !!!
             *
             *  Move the pointer that points to the slot ending earlier
             *
             *
             *  ----
             *
             *  slots1 [i]:  |========= 10 ------------ 50 =========| (Ends at 50) -> Keep
             *  slots2 [j]:  |=== 0 ---- 15 ===|                       (Ends at 15) -> Drop! j++
             *
             * Intersection: [10, 15] (Length 5 < 8) -> Move j forward because 15 < 50.
             *
             *  ----
             *
             *  Step,i Pointer,j Pointer,overlapStart / overlapEnd,overlapEnd - overlapStart >= duration,Pointer Decision (slots1[i][1] vs slots2[j][1])
             * Start,0,0,"max(10,0)=10 / min(50,15)=15",15−10=5≥8 (False),slots1[0][1] (50) > slots2[0][1] (15) → j++
             * Step 2,0,1,"max(10,60)=60 / min(50,70)=50",50−60=−10≥8 (False),slots1[0][1] (50) < slots2[1][1] (70) → i++
             * Step 3,1,1,"max(60,60)=60 / min(120,70)=70",70−60=10≥8 (True),"Match Found! Returns [60, 68]"
             * 
             *
             */
            // 3. Move the pointer that points to the slot ending earlier
            if (slots1[i][1] < slots2[j][1]) {
                i++;
            } else {
                j++;
            }
        }

        return res;
    }



    // V1
    // IDEA: SORT + 2 POINTERS
    // https://leetcode.ca/2019-04-12-1229-Meeting-Scheduler/
    public List<Integer> minAvailableDuration_1(int[][] slots1, int[][] slots2, int duration) {
        Arrays.sort(slots1, (a, b) -> a[0] - b[0]);
        Arrays.sort(slots2, (a, b) -> a[0] - b[0]);
        int m = slots1.length, n = slots2.length;
        int i = 0, j = 0;
        while (i < m && j < n) {
            int start = Math.max(slots1[i][0], slots2[j][0]);
            int end = Math.min(slots1[i][1], slots2[j][1]);
            if (end - start >= duration) {
                return Arrays.asList(start, start + duration);
            }
            if (slots1[i][1] < slots2[j][1]) {
                ++i;
            } else {
                ++j;
            }
        }
        return Collections.emptyList();
    }




    // V2





}
