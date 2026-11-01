package LeetCodeJava.Math;

// https://leetcode.com/problems/pass-the-pillow/description/

/**
 * 2582. Pass the Pillow
 * Easy
 *
 * There are n people standing in a line labeled from 1 to n. The first person in the line is holding
 * a pillow initially. Every second, the person holding the pillow passes it to the next person
 * standing in the line. Once the pillow reaches the end of the line, the direction changes, and
 * people continue passing the pillow in the opposite direction.
 *
 * For example, once the pillow reaches the nth person they pass it to the n - 1th person,
 * then to the n - 2th person and so on.
 *
 * Given the two positive integers n and time, return the index of the person holding the pillow
 * after time seconds.
 *
 *
 * Example 1:
 *
 * Input: n = 4, time = 5
 * Output: 2
 * Explanation: People pass the pillow in the following way: 1 -> 2 -> 3 -> 4 -> 3 -> 2.
 *
 * Example 2:
 *
 * Input: n = 3, time = 2
 * Output: 3
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= time <= 1000
 *
 */
public class PassThePillow {

    // V0
    // IDEA: MATH -- one leg (1 -> n or n -> 1) takes n - 1 seconds; completed legs' parity gives the direction
    /**
     * time = O(1)
     * space = O(1)
     */
    public int passThePillow(int n, int time) {
        int legLength = n - 1;
        int completedLegs = time / legLength;
        int stepsIntoLeg = time % legLength;

        // odd number of finished legs -> walking backward from person n
        if (completedLegs % 2 == 1) {
            return n - stepsIntoLeg;
        }
        // even -> walking forward from person 1
        return stepsIntoLeg + 1;
    }
}
