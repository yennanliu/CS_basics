package LeetCodeJava.Array;

// https://leetcode.com/problems/plus-one/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    // IDEA: ARRAY OP
    public int[] plusOne(int[] digits) {
        // edge
        if (digits == null || digits.length == 0) {
            return new int[] { 1 };
        }

        int plus = 1;
        List<Integer> list = new ArrayList<>();

        // `inverse` loop over arr
        for (int i = digits.length - 1; i >= 0; i--) {
            int val = digits[i] + plus;
            if (val > 9) {
                plus = 1;
                /** NOTE !!! we minus 10, instead of 9 */
                val -= 10;
            } else {
                plus = 0;
            }
            list.add(val);
        }

        /** NOTE !!! below */
        if (plus != 0) {
            list.add(plus);
        }

        // reverse
        int[] res = new int[list.size()];
        //System.out.println(">>> before reverse: list = " + list);
        Collections.reverse(list);
        //System.out.println(">>> after reverse: list = " + list);
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }

        return res;
    }


    // V0-1
    // IDEA: reverse loop, `plus one` at iex=len-1, reverse, assign val to array
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] plusOne_0_1(int[] digits) {
        // edge
        if (digits == null || digits.length == 0) {
            return new int[] { 1 };
        }

        List<Integer> cache = new ArrayList<>();
        int plus = 0;
        for (int i = digits.length - 1; i >= 0; i--) {
            int val = digits[i];
            if (i == digits.length - 1) {
                val += 1;
            }
            val += plus;
            if (val > 9) {
                val -= 10;
                plus = 1;
            } else {
                plus = 0;
            }
            cache.add(val);
        }

        // handle `last plus one`
        if (plus > 0) {
            cache.add(plus);
        }

        // reverse
        Collections.reverse(cache);

        int[] res = new int[cache.size()];
        for (int i = 0; i < cache.size(); i++) {
            res[i] = cache.get(i);
        }

        return res;
    }

    // V0-2
    // IDEA: MATH ( add per digit, then transform result) (fixed by gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int[] plusOne_0_2(int[] digits) {
        if (digits == null || digits.length == 0) {
            return new int[] { 1 };
        }

        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i] += 1;
                return digits;
            }
            digits[i] = 0;
        }

        // If all digits were 9, we need a new array (e.g., 999 + 1 = 1000)
        int[] result = new int[digits.length + 1];
        result[0] = 1; // all others default to 0
        return result;
    }


    // V0-3
    // TODO: fix below (java.lang.NumberFormatException: For input string: "728509129536673284379577474947011174006")
//    public int[] plusOne(int[] digits) {
//        // edge
//        if (digits == null || digits.length == 0) {
//            return new int[] { 1 };
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int x : digits) {
//            sb.append(x);
//        }
//
//        // to int
//        Long val = Long.parseLong(sb.toString());
//        // int val = sb.length();
//
//        val += 1L;
//        String val_str = String.valueOf(val);
//        String[] val_arr = val_str.split("");
//
//        int[] res = new int[val_arr.length];
//        for (int i = 0; i < val_str.length(); i++) {
//            res[i] = Integer.parseInt(val_arr[i]);
//        }
//
//        return res;
//    }


    // V1-1
    // https://neetcode.io/problems/plus-one
    // IDEA:  RECURSION
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
