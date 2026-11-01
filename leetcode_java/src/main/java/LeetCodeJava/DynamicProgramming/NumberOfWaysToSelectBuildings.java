package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-select-buildings/description/

/**
 * 2222. Number of Ways to Select Buildings
 * Medium
 *
 * You are given a 0-indexed binary string s which represents the types of buildings along a street where:
 *
 * s[i] = '0' denotes that the ith building is an office and
 * s[i] = '1' denotes that the ith building is a restaurant.
 *
 * As a city official, you would like to select 3 buildings for random inspection. However, to ensure variety, no two consecutive buildings out of the selected buildings can be of the same type.
 *
 * For example, given s = "001101", we cannot select the 1st, 3rd, and 5th buildings as that would form "011" which is not allowed due to having two consecutive buildings of the same type.
 *
 * Return the number of valid ways to select 3 buildings.
 *
 *
 * Example 1:
 *
 * Input: s = "001101"
 * Output: 6
 * Explanation:
 * The following sets of indices selected are valid:
 * - [0,2,4] from "001101" forms "010"
 * - [0,3,4] from "001101" forms "010"
 * - [1,2,4] from "001101" forms "010"
 * - [1,3,4] from "001101" forms "010"
 * - [2,4,5] from "001101" forms "101"
 * - [3,4,5] from "001101" forms "101"
 * No other selection is valid. Thus, there are 6 total ways.
 *
 * Example 2:
 *
 * Input: s = "11100"
 * Output: 0
 * Explanation: It can be shown that there are no valid selections.
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 10^5
 * s[i] is either '0' or '1'.
 *
 */
public class NumberOfWaysToSelectBuildings {

    // V0
    // IDEA: FIX THE MIDDLE BUILDING — COUNT OPPOSITE-TYPE BUILDINGS TO ITS LEFT AND RIGHT
    /**
     * time = O(N)
     * space = O(1)
     */
    public long numberOfWays(String s) {
        // valid selections are "010" and "101": the middle differs from both neighbours
        long totalZeros = 0;
        long totalOnes = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                totalZeros++;
            } else {
                totalOnes++;
            }
        }

        long leftZeros = 0;
        long leftOnes = 0;
        long ways = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                // middle '0': pick a '1' on the left and a '1' on the right
                long rightOnes = totalOnes - leftOnes;
                ways += leftOnes * rightOnes;
                leftZeros++;
            } else {
                // middle '1': pick a '0' on the left and a '0' on the right
                long rightZeros = totalZeros - leftZeros;
                ways += leftZeros * rightZeros;
                leftOnes++;
            }
        }
        return ways;
    }

    // V1
    // IDEA: SUBSEQUENCE-COUNTING DP — count0, count1, count01, count10 BUILD UP "010" / "101"
    /**
     * time = O(N)
     * space = O(1)
     */
    public long numberOfWays_1(String s) {
        // countX = number of subsequences equal to pattern X seen so far
        long count0 = 0;
        long count1 = 0;
        long count01 = 0;
        long count10 = 0;
        long ways = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                // this '0' closes every "01" into "010", and extends every "1" into "10"
                ways += count01;
                count10 += count1;
                count0++;
            } else {
                // this '1' closes every "10" into "101", and extends every "0" into "01"
                ways += count10;
                count01 += count0;
                count1++;
            }
        }
        return ways;
    }
}
