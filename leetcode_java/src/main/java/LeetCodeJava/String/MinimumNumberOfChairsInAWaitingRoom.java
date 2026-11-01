package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-number-of-chairs-in-a-waiting-room/description/

/**
 * 3168. Minimum Number of Chairs in a Waiting Room
 * Easy
 *
 * You are given a string s. Simulate events at each second i:
 *
 * If s[i] == 'E', a person enters the waiting room and takes one of the chairs in it.
 * If s[i] == 'L', a person leaves the waiting room, freeing up a chair.
 *
 * Return the minimum number of chairs needed so that a chair is available for every person who enters the waiting room given that it is initially empty.
 *
 *
 * Example 1:
 *
 * Input: s = "EEEEEEE"
 * Output: 7
 * Explanation:
 * After each second, a person enters the waiting room and no person leaves it. Therefore, a minimum of 7 chairs is needed.
 *
 * Example 2:
 *
 * Input: s = "ELELEEL"
 * Output: 2
 * Explanation:
 * Let's consider that there are 2 chairs in the waiting room. The table below shows the state of the waiting room at each second.
 *
 * Example 3:
 *
 * Input: s = "ELEELEELLL"
 * Output: 3
 * Explanation:
 * Let's consider that there are 3 chairs in the waiting room. The table below shows the state of the waiting room at each second.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * s consists only of the letters 'E' and 'L'.
 *
 */
public class MinimumNumberOfChairsInAWaitingRoom {

    // V0
    // IDEA: THE ANSWER IS THE PEAK OCCUPANCY OF THE RUNNING ENTER / LEAVE COUNT
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumChairs(String s) {
        int occupied = 0;
        int peak = 0;
        for (int i = 0; i < s.length(); i++) {
            // 'E' takes a chair, 'L' frees one
            if (s.charAt(i) == 'E') {
                occupied++;
            } else {
                occupied--;
            }
            // enough chairs must exist for the busiest second
            peak = Math.max(peak, occupied);
        }
        return peak;
    }
}
