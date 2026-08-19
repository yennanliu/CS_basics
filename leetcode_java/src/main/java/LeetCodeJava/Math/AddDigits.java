package LeetCodeJava.Math;

// https://leetcode.com/problems/add-digits/

/**
 *  258. Add Digits
 *  Easy
 *
 *  Given an integer num, repeatedly add all its digits until the result has
 *  only one digit, and return it.
 *
 *  Example 1:
 *    Input: num = 38
 *    Output: 2
 *    Explanation: 3 + 8 = 11, 1 + 1 = 2. Since 2 has only one digit, return it.
 *
 *  Example 2:
 *    Input: num = 0
 *    Output: 0
 *
 *  Constraints:
 *    0 <= num <= 2^31 - 1
 *
 *  Follow up: Could you do it without any loop/recursion in O(1) runtime?
 */
public class AddDigits {

    // V0
    // IDEA: digital root - the answer is 1 + (num - 1) % 9 for num > 0
    /**
     * time = O(1)
     * space = O(1)
     */
    public int addDigits(int num) {
        if (num == 0) {
            return 0;
        }
        return 1 + (num - 1) % 9;
    }

    // V1
    // IDEA: straightforward simulation of the digit-sum loop
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int addDigits_1(int num) {
        while (num >= 10) {
            int sum = 0;
            while (num > 0) {
                sum += num % 10;
                num /= 10;
            }
            num = sum;
        }
        return num;
    }
}
