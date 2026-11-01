package LeetCodeJava.Array;

// https://leetcode.com/problems/button-with-longest-push-time/description/

/**
 * 3386. Button with Longest Push Time
 * Easy
 *
 * You are given a 2D array events which represents a sequence of events where a child performs a series of button presses.
 *
 * Each events[i] = [index_i, time_i] indicates that the button at index index_i was pressed at time time_i.
 *
 * The array is sorted in increasing order of time.
 * The time taken to press a button is the difference in time between consecutive button presses. The time for the first button is simply the time at which it was pressed.
 *
 * Return the index of the button that took the longest time to push. If multiple buttons have the same longest time, return the button with the smallest index.
 *
 *
 * Example 1:
 *
 * Input: events = [[1,2],[2,5],[3,9],[1,15]]
 * Output: 1
 * Explanation:
 *
 * Button with index 1 is pressed at time 2.
 * Button with index 2 is pressed at time 5, so it took 5 - 2 = 3 units of time.
 * Button with index 3 is pressed at time 9, so it took 9 - 5 = 4 units of time.
 * Button with index 1 is pressed again at time 15, so it took 15 - 9 = 6 units of time.
 *
 * Example 2:
 *
 * Input: events = [[10,5],[1,7]]
 * Output: 10
 * Explanation:
 *
 * Button with index 10 is pressed at time 5, so it took 5 units of time.
 * Button with index 1 is pressed at time 7, so it took 7 - 5 = 2 units of time.
 *
 *
 * Constraints:
 *
 * 1 <= events.length <= 1000
 * events[i] == [index_i, time_i]
 * 1 <= index_i, time_i <= 10^5
 * The input is generated such that events is sorted in increasing order of time_i.
 *
 */
public class ButtonWithLongestPushTime {

    // V0
    // IDEA: ONE PASS OVER GAPS BETWEEN CONSECUTIVE PRESSES (first press costs its own timestamp)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int buttonWithLongestTime(int[][] events) {
        int bestIndex = events[0][0];
        int bestDuration = events[0][1];
        int previousTime = events[0][1];

        for (int i = 1; i < events.length; i++) {
            int index = events[i][0];
            int time = events[i][1];
            int duration = time - previousTime;
            previousTime = time;

            // longer wins; on a tie the smaller button index wins
            if (duration > bestDuration || (duration == bestDuration && index < bestIndex)) {
                bestDuration = duration;
                bestIndex = index;
            }
        }
        return bestIndex;
    }
}
