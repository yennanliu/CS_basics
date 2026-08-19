package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/

/**
 *  1342. Number of Steps to Reduce a Number to Zero
 *  Easy
 *
 *  Given an integer num, return the number of steps to reduce it to zero.
 *
 *  In one step, if the current number is even, you have to divide it by 2,
 *  otherwise, you have to subtract 1 from it.
 *
 *
 *  Example 1:
 *
 *  Input: num = 14
 *  Output: 6
 *  Explanation:
 *  Step 1) 14 is even; divide by 2 and obtain 7.
 *  Step 2) 7 is odd; subtract 1 and obtain 6.
 *  Step 3) 6 is even; divide by 2 and obtain 3.
 *  Step 4) 3 is odd; subtract 1 and obtain 2.
 *  Step 5) 2 is even; divide by 2 and obtain 1.
 *  Step 6) 1 is odd; subtract 1 and obtain 0.
 *
 *  Example 2:
 *
 *  Input: num = 8
 *  Output: 4
 *
 *  Example 3:
 *
 *  Input: num = 123
 *  Output: 12
 *
 *
 *  Constraints:
 *
 *  0 <= num <= 10^6
 */
public class NumberOfStepsToReduceANumberToZero {

    // V0
    // IDEA: SIMULATION (halve when even, decrement when odd)
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int numberOfSteps(int num) {
        int cnt = 0;
        while (num != 0) {
            if (num % 2 == 0) {
                num = num / 2;
            } else {
                num = num - 1;
            }
            cnt += 1;
        }
        return cnt;
    }

    // V1
    // IDEA: BIT OP
    // steps = (bit length - 1) shifts + (number of set bits) subtractions
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int numberOfSteps_1(int num) {
        if (num == 0) {
            return 0;
        }
        int bitLen = 32 - Integer.numberOfLeadingZeros(num);
        return (bitLen - 1) + Integer.bitCount(num);
    }
}
