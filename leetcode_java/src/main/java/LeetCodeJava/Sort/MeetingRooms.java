package LeetCodeJava.Sort;

// https://leetcode.com/problems/meeting-rooms/
/**
 * 252. Meeting Rooms
 * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
 *
 * Example 1:
 *
 * Input: [[0,30],[5,10],[15,20]]
 * Output: false
 * Example 2:
 *
 * Input: [[7,10],[2,4]]
 * Output: true
 * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
 *
 * Difficulty:
 * Easy
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg Facebook Google Microsoft Twitter
 *
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MeetingRooms {

    // V0
    // IDEA : SORT
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/meeting-rooms.py
    public boolean canAttendMeetings(int[][] intervals) {
        // SORT ON 1st element (idx = 0)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        // NOTE !!! start from idx=1
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i-1][1] > intervals[i][0]) {
                return false;
            }
        }
        return true;
    }

    // V0-1
    // IDEA: LC 235 (SCANNING LINE) (fixed by gpt)
    public boolean canAttendMeetings_0_1(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return true;
        }

        List<int[]> events = new ArrayList<>();

        for (int[] interval : intervals) {
            events.add(new int[]{interval[0], 1});   // start
            events.add(new int[]{interval[1], -1});  // end
        }

        // Sort: by time ASC, then type DESC (end before start if same time)
        /**
         *  NOTE !!!
         *
         *   step 1) sort on 1st element (time) (small -> big)
         *   step 2) sort on 2nd element (status) (close -> open)
         *
         */
        Collections.sort(events, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);  // end (-1) before start (1)
        });

        int activeMeetings = 0;

        for (int[] event : events) {
            activeMeetings += event[1];
            if (activeMeetings > 1) {
                return false;
            }
        }

        return true;
    }


    // V0-2
    // IDEA: SCANNING LINE (GPT) (same idea as Meeting room II, LC 253)
    // TODO: validate
    public boolean canAttendMeetings_0_2(int[][] intervals) {
        // Edge cases
        if (intervals == null || intervals.length == 0) {
            return true;
        }

        // Create an event list with start and end times marked
        List<int[]> events = new ArrayList<>();
        for (int[] interval : intervals) {
            // Add the start time with a flag of 1 (open)
            events.add(new int[]{interval[0], 1});
            // Add the end time with a flag of 0 (close)
            events.add(new int[]{interval[1], 0});
        }

        // Sort the events by time. If times are equal, prioritize closing (0) before opening (1)
        /**
         * NOTE !!!
         *
         *  we sort 1) on val,
         *    if val are the same,
         *    2) then sort on open / close status
         *
         */
        Collections.sort(events, (a, b) -> {
            if (a[0] == b[0]) {
                return a[1] - b[1]; // Close (0) should come before open (1)
            }
            return a[0] - b[0]; // Otherwise, sort by time
        });

        int cnt = 0;
        for (int[] event : events) {
            if (event[1] == 1) {
                // A new meeting starts
                cnt++;
                /** NOTE !!!
                 *
                 * if any cnt > 1, means meeting conflicts, then we CAN'T finish all meeting at once
                 * -> return false directly
                 */
                if (cnt > 1) {
                    return false; // More than one meeting at the same time
                }
            } else {
                // A meeting ends
                cnt--;
            }
        }

        return true;
    }

    // V1
    // IDEA : SORT
    public boolean canAttendMeetings_(int[][] intervals) {

        // sort
        //Arrays.sort(intervals, Comparator.comparingInt((x, y) -> x[0] - y[0]).reversed());
        // NOTE !! we sort int array via below
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // check
        for (int i = 0; i < intervals.length - 1; i++){
            int firstStart = intervals[i][0];
            int firstEnd = intervals[i][1];
            int secondStart = intervals[i+1][0];
            int secondEnd = intervals[i+1][1];

            // below is wrong
//            if ( (secondStart < firstStart && firstStart < secondEnd) || (secondStart < firstEnd && firstEnd < secondEnd) ){
//                return false;
//            }

            // NOTE !!!
            // since we already sort Array with 1st element (increasing order)
            // -> so second element's start MUST BIGGER than first element
            // -> so all we need to do is : comparing if first element's end time is bigger thant second element's start time
            // -> e.g. if (intervals[i][1] > intervals[i+1][0])
            if ( firstEnd > secondStart ){
                return false;
            }
        }

        return true;
    }

    // V2
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/meeting-rooms/editorial/

    // V3
    // IDEA : SORTING
    // https://leetcode.com/problems/meeting-rooms/editorial/
    public boolean canAttendMeetings_2(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < intervals.length - 1; i++) {
            if (intervals[i][1] > intervals[i + 1][0]) {
                return false;
            }
        }
        return true;
    }

}
