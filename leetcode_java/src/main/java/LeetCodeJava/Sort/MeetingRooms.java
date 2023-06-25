package LeetCodeJava.Sort;

// https://leetcode.com/problems/meeting-rooms/

import java.util.Arrays;

public class MeetingRooms {

    public boolean canAttendMeetings(int[][] intervals) {

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

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/meeting-rooms/editorial/

    // V1
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
