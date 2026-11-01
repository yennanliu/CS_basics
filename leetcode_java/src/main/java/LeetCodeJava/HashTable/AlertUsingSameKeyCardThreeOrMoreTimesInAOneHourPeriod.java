package LeetCodeJava.HashTable;

// https://leetcode.com/problems/alert-using-same-key-card-three-or-more-times-in-a-one-hour-period/description/

import java.util.*;

/**
 * 1604. Alert Using Same Key-Card Three or More Times in a One Hour Period
 * Medium
 *
 * LeetCode company workers use key-cards to unlock office doors. Each time a worker uses their key-card, the security system saves the worker's name and the time when it was used. The system emits an alert if any worker uses the key-card three or more times in a one-hour period.
 *
 * You are given a list of strings keyName and keyTime where [keyName[i], keyTime[i]] corresponds to a person's name and the time when their key-card was used in a single day.
 *
 * Access times are given in the 24-hour time format "HH:MM", such as "23:51" and "09:49".
 *
 * Return a list of unique worker names who received an alert for frequent keycard use. Sort the names in ascending order alphabetically.
 *
 * Notice that "10:00" - "11:00" is considered to be within a one-hour period, while "22:51" - "23:52" is not considered to be within a one-hour period.
 *
 *
 * Example 1:
 *
 * Input: keyName = ["daniel","daniel","daniel","luis","luis","luis","luis"], keyTime = ["10:00","10:40","11:00","09:00","11:00","13:00","15:00"]
 * Output: ["daniel"]
 * Explanation: "daniel" used the keycard 3 times in a one-hour period ("10:00","10:40", "11:00").
 *
 * Example 2:
 *
 * Input: keyName = ["alice","alice","alice","bob","bob","bob","bob"], keyTime = ["12:01","12:00","18:00","21:00","21:20","21:30","23:00"]
 * Output: ["bob"]
 * Explanation: "bob" used the keycard 3 times in a one-hour period ("21:00","21:20", "21:30").
 *
 *
 * Constraints:
 *
 * 1 <= keyName.length, keyTime.length <= 10^5
 * keyName.length == keyTime.length
 * keyTime[i] is in the format "HH:MM".
 * [keyName[i], keyTime[i]] is unique.
 * 1 <= keyName[i].length <= 10
 * keyName[i] contains only lowercase English letters.
 *
 */
public class AlertUsingSameKeyCardThreeOrMoreTimesInAOneHourPeriod {

    // V0
    // IDEA: GROUP BY NAME + SORT + FIXED-WIDTH WINDOW OF 3
    //       convert "HH:MM" to minutes, bucket the swipes per worker, sort each
    //       bucket. if any one-hour window holds >= 3 swipes, then some 3
    //       CONSECUTIVE sorted swipes fit in an hour -> one scan checking
    //       times[i + 2] - times[i] <= 60 is enough.
    //       the window is inclusive ("10:00" to "11:00" counts), hence <= 60.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<String> alertNames(String[] keyName, String[] keyTime) {
        // worker -> swipe times in minutes
        Map<String, List<Integer>> timesByName = new HashMap<>();
        for (int i = 0; i < keyName.length; i++) {
            String time = keyTime[i];
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3, 5));
            timesByName.computeIfAbsent(keyName[i], x -> new ArrayList<>()).add(hours * 60 + minutes);
        }

        List<String> alerted = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : timesByName.entrySet()) {
            List<Integer> times = entry.getValue();
            Collections.sort(times);
            for (int i = 0; i + 2 < times.size(); i++) {
                // three consecutive swipes within one hour
                if (times.get(i + 2) - times.get(i) <= 60) {
                    alerted.add(entry.getKey());
                    break;
                }
            }
        }

        // the answer is required in alphabetical order
        Collections.sort(alerted);
        return alerted;
    }
}
