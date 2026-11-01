package LeetCodeJava.HashTable;

// https://leetcode.com/problems/form-smallest-number-from-two-digit-arrays/description/

/**
 * 2605. Form Smallest Number From Two Digit Arrays
 * Easy
 *
 * Given two arrays of unique digits nums1 and nums2, return the smallest number that contains at least one digit from each array.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [4,1,3], nums2 = [5,7]
 * Output: 15
 * Explanation: The number 15 contains the digit 1 from nums1 and the digit 5 from nums2. It can be proven that 15 is the smallest number we can have.
 *
 * Example 2:
 *
 * Input: nums1 = [3,5,2,6], nums2 = [3,1,7]
 * Output: 3
 * Explanation: The number 3 contains the digit 3 which exists in both arrays.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 9
 * 1 <= nums1[i], nums2[i] <= 9
 * All digits in each array are unique.
 *
 */
public class FormSmallestNumberFromTwoDigitArrays {

    // V0
    // IDEA: SHARED DIGIT -> 1-DIGIT ANSWER, ELSE COMBINE THE TWO SMALLEST DIGITS
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int minNumber(int[] nums1, int[] nums2) {
        boolean[] inFirst = new boolean[10];
        int min1 = 10;
        for (int d : nums1) {
            inFirst[d] = true;
            min1 = Math.min(min1, d);
        }

        int min2 = 10;
        int smallestShared = 10;
        for (int d : nums2) {
            min2 = Math.min(min2, d);
            if (inFirst[d]) {
                smallestShared = Math.min(smallestShared, d);
            }
        }

        // a single digit present in both arrays beats any 2-digit number
        if (smallestShared < 10) {
            return smallestShared;
        }
        // otherwise put the smaller of the two minimums in the tens place
        return Math.min(min1 * 10 + min2, min2 * 10 + min1);
    }
}
