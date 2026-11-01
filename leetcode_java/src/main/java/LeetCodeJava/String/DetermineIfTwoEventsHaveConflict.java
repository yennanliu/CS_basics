package LeetCodeJava.String;

// https://leetcode.com/problems/determine-if-two-events-have-conflict/description/

/**
 * 2446. Determine if Two Events Have Conflict
 * Easy
 *
 * You are given two arrays of strings that represent two inclusive events that happened on the same day, event1 and event2, where:
 *
 * event1 = [startTime1, endTime1] and
 * event2 = [startTime2, endTime2].
 *
 * Event times are valid 24 hours format in the form of HH:MM.
 *
 * A conflict happens when two events have some non-empty intersection (i.e., some moment is common to both events).
 *
 * Return true if there is a conflict between two events. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: event1 = ["01:15","02:00"], event2 = ["02:00","03:00"]
 * Output: true
 * Explanation: The two events intersect at time 2:00.
 *
 * Example 2:
 *
 * Input: event1 = ["01:00","02:00"], event2 = ["01:20","03:00"]
 * Output: true
 * Explanation: The two events intersect starting from 01:20 to 02:00.
 *
 * Example 3:
 *
 * Input: event1 = ["10:00","11:00"], event2 = ["14:00","15:00"]
 * Output: false
 * Explanation: The two events do not intersect.
 *
 *
 * Constraints:
 *
 * event1.length == event2.length == 2
 * event1[i].length == event2[i].length == 2
 * startTime1 <= endTime1
 * startTime2 <= endTime2
 * All the event times follow the HH:MM format.
 *
 */
public class DetermineIfTwoEventsHaveConflict {

    // V0
    // IDEA: INTERVAL OVERLAP ON THE RAW STRINGS
    //       two closed intervals [s1, e1] and [s2, e2] intersect iff s1 <= e2 and
    //       s2 <= e1. HH:MM is zero-padded, so lexicographic order equals time order
    //       and no parsing is needed.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean haveConflict(String[] event1, String[] event2) {
        boolean firstStartsBeforeSecondEnds = event1[0].compareTo(event2[1]) <= 0;
        boolean secondStartsBeforeFirstEnds = event2[0].compareTo(event1[1]) <= 0;
        return firstStartsBeforeSecondEnds && secondStartsBeforeFirstEnds;
    }

    // V1
    // IDEA: PARSE TO MINUTES, THEN COMPARE INTEGERS
    //       the format-independent version: convert each "HH:MM" to minutes since
    //       midnight and apply the same overlap test.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean haveConflict_1(String[] event1, String[] event2) {
        int start1 = toMinutes(event1[0]);
        int end1 = toMinutes(event1[1]);
        int start2 = toMinutes(event2[0]);
        int end2 = toMinutes(event2[1]);
        return start1 <= end2 && start2 <= end1;
    }

    /** "HH:MM" -> minutes since midnight. */
    private int toMinutes(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        return hours * 60 + minutes;
    }
}
