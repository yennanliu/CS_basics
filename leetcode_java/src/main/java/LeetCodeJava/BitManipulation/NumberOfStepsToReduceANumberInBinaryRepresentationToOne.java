package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-steps-to-reduce-a-number-in-binary-representation-to-one/description/

/**
 * 1404. Number of Steps to Reduce a Number in Binary Representation to One
 * Medium
 *
 * Given the binary representation of an integer as a string s, return the number
 * of steps to reduce it to 1 under the following rules:
 *
 * If the current number is even, you have to divide it by 2.
 *
 * If the current number is odd, you have to add 1 to it.
 *
 * It is guaranteed that you can always reach one for all test cases.
 *
 * Example 1:
 *
 * Input: s = "1101"
 * Output: 6
 * Explanation: "1101" corressponds to number 13 in their decimal representation.
 * Step 1) 13 is odd, add 1 and obtain 14.
 * Step 2) 14 is even, divide by 2 and obtain 7.
 * Step 3) 7 is odd, add 1 and obtain 8.
 * Step 4) 8 is even, divide by 2 and obtain 4.
 * Step 5) 4 is even, divide by 2 and obtain 2.
 * Step 6) 2 is even, divide by 2 and obtain 1.
 *
 * Example 2:
 *
 * Input: s = "10"
 * Output: 1
 * Explanation: "10" corresponds to number 2 in their decimal representation.
 * Step 1) 2 is even, divide by 2 and obtain 1.
 *
 * Example 3:
 *
 * Input: s = "1"
 * Output: 0
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of characters '0' or '1'
 * s[0] == '1'
 *
 */
public class NumberOfStepsToReduceANumberInBinaryRepresentationToOne {

    // V0
    // IDEA: BIT SIMULATION WITH A CARRY, SCANNING FROM THE LEAST SIGNIFICANT BIT
    //   walk s from the last bit down to index 1 (index 0 is the leading 1).
    //   carry = a +1 from an earlier "odd" step is still propagating.
    //     - apply the carry to the current bit
    //     - bit now 1 -> number is odd  -> +1 step (add 1), which leaves a carry
    //     - bit now 0 -> number is even -> +1 step (divide by 2, drop this bit)
    //   a carry still pending at the end turns the leading "1" into "10": one more divide
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numSteps(String s) {
        int steps = 0;
        int carry = 0;

        for (int i = s.length() - 1; i >= 1; i--) {
            int bit = (s.charAt(i) - '0') + carry;
            if (bit == 1) {
                // odd: add 1 -> this bit becomes 0 and a carry moves left
                steps++;
                carry = 1;
            }
            // bit == 0 (even, carry stays as it was) or bit == 2 (0 with carry kept):
            // either way the bit is now 0, so divide by 2
            steps++;
        }

        // leading 1 plus a carry is "10", which needs one more divide
        return steps + carry;
    }
}
