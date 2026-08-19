package LeetCodeJava.String;

// https://leetcode.com/problems/next-greater-element-iii/

/**
 *  556. Next Greater Element III
 *  Medium
 *
 *  Given a positive integer n, find the smallest integer which has exactly
 *  the same digits existing in the integer n and is greater in value than n.
 *  If no such positive integer exists, return -1.
 *
 *  Note that the returned integer should fit in a 32-bit integer, if there is
 *  a valid answer but it does not fit in a 32-bit integer, return -1.
 *
 *  Example 1:
 *    Input: n = 12    Output: 21
 *  Example 2:
 *    Input: n = 21    Output: -1
 *
 *  Constraints:
 *    1 <= n <= 2^31 - 1
 */
public class NextGreaterElementIII {

    // V0
    // IDEA: next permutation on the digits - find the pivot from the right,
    //       swap it with the smallest bigger digit to its right, reverse the suffix
    /**
     * time = O(d), d = number of digits
     * space = O(d)
     */
    public int nextGreaterElement(int n) {
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;

        // 1) find the rightmost i with digits[i] < digits[i + 1]
        int i = len - 2;
        while (i >= 0 && digits[i] >= digits[i + 1]) {
            i--;
        }
        if (i < 0) {
            return -1; // already the largest permutation
        }

        // 2) find the rightmost j > i with digits[j] > digits[i]
        int j = len - 1;
        while (digits[j] <= digits[i]) {
            j--;
        }

        // 3) swap and reverse the suffix
        char tmp = digits[i];
        digits[i] = digits[j];
        digits[j] = tmp;

        reverse(digits, i + 1, len - 1);

        long val = Long.parseLong(new String(digits));
        return (val > Integer.MAX_VALUE) ? -1 : (int) val;
    }

    private void reverse(char[] arr, int left, int right) {
        while (left < right) {
            char tmp = arr[left];
            arr[left] = arr[right];
            arr[right] = tmp;
            left++;
            right--;
        }
    }
}
