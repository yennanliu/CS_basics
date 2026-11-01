package LeetCodeJava.Math;

// https://leetcode.com/problems/find-closest-person/description/

/**
 * 3516. Find Closest Person
 * Easy
 *
 * You are given three integers x, y, and z, representing the positions of three
 * people on a number line:
 *
 * x is the position of Person 1.
 *
 * y is the position of Person 2.
 *
 * z is the position of Person 3, who does not move.
 *
 * Both Person 1 and Person 2 move toward Person 3 at the same speed.
 *
 * Determine which person reaches Person 3 first:
 *
 * Return 1 if Person 1 arrives first.
 *
 * Return 2 if Person 2 arrives first.
 *
 * Return 0 if both arrive at the same time.
 *
 * Return the result accordingly.
 *
 * Example 1:
 *
 * Input: x = 2, y = 7, z = 4
 *
 * Output: 1
 *
 * Explanation:
 *
 * Person 1 is at position 2 and can reach Person 3 (at position 4) in 2 steps.
 *
 * Person 2 is at position 7 and can reach Person 3 in 3 steps.
 *
 * Since Person 1 reaches Person 3 first, the output is 1.
 *
 * Example 2:
 *
 * Input: x = 2, y = 5, z = 6
 *
 * Output: 2
 *
 * Explanation:
 *
 * Person 1 is at position 2 and can reach Person 3 (at position 6) in 4 steps.
 *
 * Person 2 is at position 5 and can reach Person 3 in 1 step.
 *
 * Since Person 2 reaches Person 3 first, the output is 2.
 *
 * Example 3:
 *
 * Input: x = 1, y = 5, z = 3
 *
 * Output: 0
 *
 * Explanation:
 *
 * Person 1 is at position 1 and can reach Person 3 (at position 3) in 2 steps.
 *
 * Person 2 is at position 5 and can reach Person 3 in 2 steps.
 *
 * Since both Person 1 and Person 2 reach Person 3 at the same time, the output is
 * 0.
 *
 * Constraints:
 *
 * 1 <= x, y, z <= 100
 *
 */
public class FindClosestPerson {

    // V0
    // IDEA: EQUAL SPEED MEANS TIME IS JUST DISTANCE
    //       both people walk at the same speed, so whoever is closer to z arrives first;
    //       compare |x - z| with |y - z| and report a tie as 0.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int findClosest(int x, int y, int z) {
        int distance1 = Math.abs(x - z);
        int distance2 = Math.abs(y - z);
        if (distance1 < distance2) {
            return 1;
        }
        if (distance2 < distance1) {
            return 2;
        }
        return 0;
    }
}
