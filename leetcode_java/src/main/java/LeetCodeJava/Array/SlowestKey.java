package LeetCodeJava.Array;

// https://leetcode.com/problems/slowest-key/description/

/**
 * 1629. Slowest Key
 * Easy
 *
 * A newly designed keypad was tested, where a tester pressed a sequence of n keys, one at a
 * time.
 *
 * You are given a string keysPressed of length n, where keysPressed[i] was the ith key
 * pressed in the testing sequence, and a sorted list releaseTimes, where releaseTimes[i]
 * was the time the ith key was released. Both arrays are 0-indexed. The 0th key was pressed
 * at the time 0, and every subsequent key was pressed at the exact time the previous key
 * was released.
 *
 * The tester wants to know the key of the keypress that had the longest duration. The ith
 * keypress had a duration of releaseTimes[i] - releaseTimes[i - 1], and the 0th keypress
 * had a duration of releaseTimes[0].
 *
 * Note that the same key could have been pressed multiple times during the test, and these
 * multiple presses of the same key may not have had the same duration.
 *
 * Return the key of the keypress that had the longest duration. If there are multiple such
 * keypresses, return the lexicographically largest key of the keypresses.
 *
 *
 * Example 1:
 *
 * Input: releaseTimes = [9,29,49,50], keysPressed = "cbcd"
 * Output: "c"
 * Explanation: 'b' and the second 'c' both lasted 20; 'c' is lexicographically larger.
 *
 * Example 2:
 *
 * Input: releaseTimes = [12,23,36,46,62], keysPressed = "spuda"
 * Output: "a"
 *
 *
 * Constraints:
 *
 * releaseTimes.length == n
 * keysPressed.length == n
 * 2 <= n <= 1000
 * 1 <= releaseTimes[i] <= 10^9
 * releaseTimes[i] < releaseTimes[i+1]
 * keysPressed contains only lowercase English letters.
 *
 */
public class SlowestKey {

    // V0
    // IDEA: ONE PASS with a (duration, key) tie-break — duration[0] = releaseTimes[0],
    //       duration[i] = releaseTimes[i] - releaseTimes[i - 1]; on a tie keep the larger key
    /**
     * time = O(N)
     * space = O(1)
     */
    public char slowestKey(int[] releaseTimes, String keysPressed) {
        // key 0 was pressed at time 0
        char slowest = keysPressed.charAt(0);
        int longestDuration = releaseTimes[0];

        for (int i = 1; i < releaseTimes.length; i++) {
            int duration = releaseTimes[i] - releaseTimes[i - 1];
            char key = keysPressed.charAt(i);
            boolean longer = duration > longestDuration;
            boolean tieWithLargerKey = duration == longestDuration && key > slowest;
            if (longer || tieWithLargerKey) {
                longestDuration = duration;
                slowest = key;
            }
        }
        return slowest;
    }
}
