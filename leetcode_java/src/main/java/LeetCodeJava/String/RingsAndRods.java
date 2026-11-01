package LeetCodeJava.String;

// https://leetcode.com/problems/rings-and-rods/description/

/**
 * 2103. Rings and Rods
 * Easy
 *
 * There are n rings and each ring is either red, green, or blue. The rings are distributed across ten rods labeled from 0 to 9.
 *
 * You are given a string rings of length 2n that describes the n rings that are placed onto the rods. Every two characters in rings forms a color-position pair that is used to describe each ring where:
 *
 * The first character of the ith pair denotes the ith ring's color ('R', 'G', 'B').
 * The second character of the ith pair denotes the rod that the ith ring is placed on ('0' to '9').
 *
 * For example, "R3G2B1" describes n == 3 rings: a red ring placed onto the rod labeled 3, a green ring placed onto the rod labeled 2, and a blue ring placed onto the rod labeled 1.
 *
 * Return the number of rods that have all three colors of rings on them.
 *
 *
 * Example 1:
 *
 * Input: rings = "B0B6G0R6R0R6"
 * Output: 1
 * Explanation:
 * - The rod labeled 0 holds 3 rings with all colors: red, green, and blue.
 * - The rod labeled 6 holds 3 rings, but it only has red and blue.
 * - The rod labeled 5 holds 0 rings.
 * Thus, the number of rods with all three colors is 1.
 *
 * Example 2:
 *
 * Input: rings = "B0R0G0R9R0B0G0"
 * Output: 1
 * Explanation:
 * - The rod labeled 0 holds 6 rings with all colors: red, green, and blue.
 * - The rod labeled 9 holds 1 ring, but it only has red.
 * Thus, the number of rods with all three colors is 1.
 *
 * Example 3:
 *
 * Input: rings = "G4"
 * Output: 0
 * Explanation:
 * Only one ring is given. Thus, no rods have all three colors.
 *
 *
 * Constraints:
 *
 * rings.length == 2 * n
 * 1 <= n <= 100
 * rings[i] where i is even is either 'R', 'G', or 'B' (0-indexed).
 * rings[i] where i is odd is a digit from '0' to '9' (0-indexed).
 *
 */
public class RingsAndRods {

    private static final int RODS = 10;

    // V0
    // IDEA: A SET OF COLOURS PER ROD (as a 3-bit mask: R = 1, G = 2, B = 4)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countPoints(String rings) {
        int[] colorMask = new int[RODS];
        // read (colour, rod) pairs
        for (int i = 0; i + 1 < rings.length(); i += 2) {
            char color = rings.charAt(i);
            int rod = rings.charAt(i + 1) - '0';
            if (color == 'R') {
                colorMask[rod] |= 1;
            } else if (color == 'G') {
                colorMask[rod] |= 2;
            } else {
                colorMask[rod] |= 4;
            }
        }
        int count = 0;
        for (int rod = 0; rod < RODS; rod++) {
            // all three bits set -> all three colours present
            if (colorMask[rod] == 7) {
                count++;
            }
        }
        return count;
    }
}
