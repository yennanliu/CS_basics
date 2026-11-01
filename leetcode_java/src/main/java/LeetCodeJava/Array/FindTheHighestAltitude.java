package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-highest-altitude/description/
/**
 * 1732. Find the Highest Altitude
 * Easy
 *
 * There is a biker going on a road trip. The road trip consists of n + 1 points at various
 * altitudes. The biker starts his trip on point 0 with altitude equal 0.
 *
 * You are given an integer array gain of length n where gain[i] is the net gain in altitude between
 * points i and i + 1 for all (0 <= i < n). Return the highest altitude of a point.
 *
 *
 * Example 1:
 *
 * Input: gain = [-5,1,5,0,-7]
 * Output: 1
 * Explanation: The altitudes are [0,-5,-4,1,1,-6]. The highest is 1.
 *
 * Example 2:
 *
 * Input: gain = [-4,-3,-2,-1,4,3,2]
 * Output: 0
 * Explanation: The altitudes are [0,-4,-7,-9,-10,-6,-3,-1]. The highest is 0.
 *
 *
 * Constraints:
 *
 * n == gain.length
 * 1 <= n <= 100
 * -100 <= gain[i] <= 100
 *
 */
public class FindTheHighestAltitude {

    // V0
    // IDEA: RUNNING PREFIX SUM (gain is a difference array of the altitudes)
    // altitude of point i + 1 = gain[0] + ... + gain[i]; the answer is the max prefix sum.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int largestAltitude(int[] gain) {
        // point 0 has altitude 0 and may itself be the highest point (Example 2)
        int highest = 0;
        int altitude = 0;
        for (int delta : gain) {
            altitude += delta;
            highest = Math.max(highest, altitude);
        }
        return highest;
    }
}
