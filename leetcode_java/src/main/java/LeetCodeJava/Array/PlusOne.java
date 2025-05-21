package LeetCodeJava.Array;

// https://leetcode.com/problems/plus-one/
/**
 *
 66. Plus One
 Solved
 Easy
 Topics
 Companies
 You are given a large integer represented as an integer array digits, where each digits[i] is the ith digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading 0's.

 Increment the large integer by one and return the resulting array of digits.



 Example 1:

 Input: digits = [1,2,3]
 Output: [1,2,4]
 Explanation: The array represents the integer 123.
 Incrementing by one gives 123 + 1 = 124.
 Thus, the result should be [1,2,4].
 Example 2:

 Input: digits = [4,3,2,1]
 Output: [4,3,2,2]
 Explanation: The array represents the integer 4321.
 Incrementing by one gives 4321 + 1 = 4322.
 Thus, the result should be [4,3,2,2].
 Example 3:

 Input: digits = [9]
 Output: [1,0]
 Explanation: The array represents the integer 9.
 Incrementing by one gives 9 + 1 = 10.
 Thus, the result should be [1,0].


 Constraints:

 1 <= digits.length <= 100
 0 <= digits[i] <= 9
 digits does not contain any leading 0's.
 *
 *
 *
 */

public class PlusOne {

    // V0
//    public int[] plusOne(int[] digits) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/plus-one
    // IDEA:  RECURSION
    public int[] plusOne_1_1(int[] digits) {
        if (digits.length == 0)
            return new int[]{1};

        if (digits[digits.length - 1] < 9) {
            digits[digits.length - 1] += 1;
            return digits;
        } else {
            int[] newDigits = new int[digits.length - 1];
            System.arraycopy(digits, 0, newDigits, 0, digits.length - 1);
            int[] result = plusOne_1_1(newDigits);
            result = java.util.Arrays.copyOf(result, result.length + 1);
            result[result.length - 1] = 0;
            return result;
        }
    }


    // V1-2
    // https://neetcode.io/problems/plus-one
    // IDEA: ITERATION - I
    public int[] plusOne_1_2(int[] digits) {
        int one = 1;
        int i = 0;
        boolean carry = true;

        for (int j = digits.length - 1; j >= 0; j--) {
            if (carry) {
                if (digits[j] == 9) {
                    digits[j] = 0;
                } else {
                    digits[j]++;
                    carry = false;
                }
            }
        }
        if (carry) {
            int[] result = new int[digits.length + 1];
            result[0] = 1;
            System.arraycopy(digits, 0, result, 1, digits.length);
            return result;
        }
        return digits;
    }

    // V1-3
    // https://neetcode.io/problems/plus-one
    // IDEA: ITERATION - II
    public int[] plusOne_1_3(int[] digits) {
        int n = digits.length;
        for (int i = n - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }
        int[] result = new int[n + 1];
        result[0] = 1;
        return result;
    }


    // V2
    // https://leetcode.com/problems/plus-one/editorial/
    public int[] plusOne_2(int[] digits) {
        int n = digits.length;

        // move along the input array starting from the end
        for (int idx = n - 1; idx >= 0; --idx) {
            // set all the nines at the end of array to zeros
            if (digits[idx] == 9) {
                digits[idx] = 0;
            }
            // here we have the rightmost not-nine
            else {
                // increase this rightmost not-nine by 1
                digits[idx]++;
                // and the job is done
                return digits;
            }
        }
        // we're here because all the digits are nines
        digits = new int[n + 1];
        digits[0] = 1;
        return digits;
    }

    // V3
    // https://leetcode.com/problems/plus-one/solutions/2706861/java-fastest-0ms-runtime-easy-and-elegant-solution/
    public int[] plusOne_3(int[] digits){
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }

        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }


}
