package LeetCodeJava.Math;

// https://leetcode.com/problems/adding-two-negabinary-numbers/description/

import java.util.*;

/**
 * 1073. Adding Two Negabinary Numbers
 * Medium
 *
 * Given two numbers arr1 and arr2 in base -2, return the result of adding them together.
 *
 * Each number is given in array format: as an array of 0s and 1s, from most significant bit to
 * least significant bit. For example, arr = [1,1,0,1] represents the number
 * (-2)^3 + (-2)^2 + (-2)^0 = -3. A number arr in array format is also guaranteed to have no
 * leading zeros: either arr == [0] or arr[0] == 1.
 *
 * Return the result of adding arr1 and arr2 in the same format: as an array of 0s and 1s with
 * no leading zeros.
 *
 *
 * Example 1:
 *
 * Input: arr1 = [1,1,1,1,1], arr2 = [1,0,1]
 * Output: [1,0,0,0,0]
 * Explanation: arr1 represents 11, arr2 represents 5, the output represents 16.
 *
 * Example 2:
 *
 * Input: arr1 = [0], arr2 = [0]
 * Output: [0]
 *
 * Example 3:
 *
 * Input: arr1 = [0], arr2 = [1]
 * Output: [1]
 *
 *
 * Constraints:
 *
 * 1 <= arr1.length, arr2.length <= 1000
 * arr1[i] and arr2[i] are 0 or 1
 * arr1 and arr2 have no leading zeros
 *
 */
public class AddingTwoNegabinaryNumbers {

    // V0
    // IDEA: SCHOOLBOOK ADDITION WITH A BASE -2 CARRY
    //   add column by column from the least significant bit, sum = a + b + carry.
    //   the next place is worth -2 times this one, so:
    //     sum == 2  -> write 0, carry -1    (2 * v == -1 * (-2 * v))
    //     sum == 3  -> write 1, carry -1
    //     sum == -1 -> write 1, carry  1    (-1 * v == 1 * v + 1 * (-2 * v))
    //   the carry stays in {-1, 0, 1}; strip leading zeros at the end.
    /**
     * time = O(N), N = max(arr1.length, arr2.length)
     * space = O(N)
     */
    public int[] addNegabinary(int[] arr1, int[] arr2) {
        int i = arr1.length - 1;
        int j = arr2.length - 1;
        int carry = 0;
        // least significant bit first
        List<Integer> bits = new ArrayList<>();

        while (i >= 0 || j >= 0 || carry != 0) {
            int a = (i >= 0) ? arr1[i] : 0;
            int b = (j >= 0) ? arr2[j] : 0;
            int sum = a + b + carry;
            carry = 0;
            if (sum >= 2) {
                sum -= 2;
                carry = -1;
            } else if (sum == -1) {
                sum = 1;
                carry = 1;
            }
            bits.add(sum);
            i--;
            j--;
        }

        // drop leading zeros (they sit at the end of the list), keeping at least one digit
        while (bits.size() > 1 && bits.get(bits.size() - 1) == 0) {
            bits.remove(bits.size() - 1);
        }

        // reverse into most-significant-first order
        int[] result = new int[bits.size()];
        for (int k = 0; k < bits.size(); k++) {
            result[k] = bits.get(bits.size() - 1 - k);
        }
        return result;
    }
}
