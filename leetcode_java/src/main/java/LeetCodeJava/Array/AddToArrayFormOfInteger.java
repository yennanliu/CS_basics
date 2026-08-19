package LeetCodeJava.Array;

// https://leetcode.com/problems/add-to-array-form-of-integer/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  989. Add to Array-Form of Integer
 *  Easy
 *
 *  The array-form of an integer num is an array representing its digits in left
 *  to right order.
 *
 *  For example, for num = 1321, the array form is [1,3,2,1].
 *
 *  Given num, the array-form of an integer, and an integer k, return the array-form
 *  of the integer num + k.
 *
 *
 *  Example 1:
 *
 *  Input: num = [1,2,0,0], k = 34
 *  Output: [1,2,3,4]
 *  Explanation: 1200 + 34 = 1234
 *
 *  Example 2:
 *
 *  Input: num = [2,7,4], k = 181
 *  Output: [4,5,5]
 *
 *  Example 3:
 *
 *  Input: num = [2,1,5], k = 806
 *  Output: [1,0,2,1]
 *
 *
 *  Constraints:
 *
 *  1 <= num.length <= 10^4
 *  0 <= num[i] <= 9
 *  num does not contain any leading zeros except for the zero itself.
 *  1 <= k <= 10^4
 */
public class AddToArrayFormOfInteger {

    // V0
    // IDEA: SCHOOLBOOK ADDITION from the last digit, carrying the remainder of k
    /**
     * time = O(max(n, log k))
     * space = O(max(n, log k))
     */
    public List<Integer> addToArrayForm(int[] num, int k) {
        List<Integer> res = new ArrayList<>();
        int i = num.length - 1;
        int carry = k;
        while (i >= 0 || carry > 0) {
            if (i >= 0) {
                carry += num[i];
                i--;
            }
            res.add(carry % 10);
            carry /= 10;
        }
        Collections.reverse(res);
        return res;
    }
}
