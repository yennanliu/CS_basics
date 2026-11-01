package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-almost-equal-pairs-i/description/

/**
 * 3265. Count Almost Equal Pairs I
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * We call two integers x and y in this problem almost equal if both integers can become equal after performing the following operation at most once:
 *
 * Choose either x or y and swap any two digits within the chosen number.
 *
 * Return the number of indices i and j in nums where i < j such that nums[i] and nums[j] are almost equal.
 *
 * Note that it is allowed for an integer to have leading zeros after performing an operation.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,12,30,17,21]
 * Output: 2
 * Explanation:
 * The almost equal pairs of elements are:
 * 3 and 30. By swapping 3 and 0 in 30, you get 03 which is 3.
 * 12 and 21. By swapping 1 and 2 in 12, you get 21.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,1]
 * Output: 10
 * Explanation:
 * Every two elements in the array are almost equal.
 *
 * Example 3:
 *
 * Input: nums = [123,231]
 * Output: 0
 * Explanation:
 * We cannot swap any two digits of 123 or 231 to reach the other.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 10^6
 *
 */
public class CountAlmostEqualPairsI {

    // V0
    // IDEA: PAD TO A COMMON WIDTH, THEN CHECK EACH PAIR'S MISMATCHED DIGITS
    //   leading zeros are allowed after a swap, so write every number with the
    //   same number of digits. a swap inside x reaching y is symmetric (the same
    //   two positions swapped in y reach x), so a pair is "almost equal" when the
    //   padded strings are equal, or differ in exactly two positions holding
    //   each other's digits. n <= 100 keeps the O(n^2) pair scan cheap.
    /**
     * time = O(N^2 * D)
     * space = O(N * D)
     */
    public int countPairs(int[] nums) {
        int n = nums.length;
        String[] padded = padToCommonWidth(nums);

        int pairs = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isAlmostEqual(padded[i], padded[j])) {
                    pairs++;
                }
            }
        }
        return pairs;
    }

    /** Writes every number with the same digit count, left-padding with zeros. */
    private String[] padToCommonWidth(int[] nums) {
        int width = 0;
        for (int num : nums) {
            width = Math.max(width, String.valueOf(num).length());
        }
        String[] padded = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            StringBuilder sb = new StringBuilder(String.valueOf(nums[i]));
            while (sb.length() < width) {
                sb.insert(0, '0');
            }
            padded[i] = sb.toString();
        }
        return padded;
    }

    /** True when a and b are equal or one swap of two positions turns a into b. */
    private boolean isAlmostEqual(String a, String b) {
        int first = -1;
        int second = -1;
        int mismatches = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                mismatches++;
                if (mismatches > 2) {
                    return false;
                }
                if (first == -1) {
                    first = i;
                } else {
                    second = i;
                }
            }
        }
        if (mismatches == 0) {
            return true;
        }
        // one swap repairs exactly two mismatches holding each other's digits
        return mismatches == 2
                && a.charAt(first) == b.charAt(second)
                && a.charAt(second) == b.charAt(first);
    }
}
