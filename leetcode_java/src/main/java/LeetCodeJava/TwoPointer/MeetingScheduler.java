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
//    public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {
//    }


    // V0-1
    // IDEA: SORT + 2 POINTERS (GPT)
    // TODO: validate
    // IDEA: Sort + Two Pointers
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
