package LeetCodeJava.Array;

// https://leetcode.com/problems/find-lucky-integer-in-an-array/description/

/**
 * 1394. Find Lucky Integer in an Array
 * Easy
 *
 * Given an array of integers arr, a lucky integer is an integer that has a frequency in the array equal to its value.
 *
 * Return the largest lucky integer in the array. If there is no lucky integer return -1.
 *
 * Example 1:
 *
 * Input: arr = [2,2,3,4]
 * Output: 2
 * Explanation: The only lucky number in the array is 2 because frequency[2] == 2.
 *
 * Example 2:
 *
 * Input: arr = [1,2,2,3,3,3]
 * Output: 3
 * Explanation: 1, 2 and 3 are all lucky numbers, return the largest of them.
 *
 * Example 3:
 *
 * Input: arr = [2,2,2,3,3]
 * Output: -1
 * Explanation: There are no lucky numbers in the array.
 *
 * Constraints:
 *
 * 1 <= arr.length <= 500
 * 1 <= arr[i] <= 500
 *
 */
public class FindLuckyIntegerInAnArray {

    private static final int MAX_VALUE = 500;

    // V0
    // IDEA: COUNTING (a value is lucky when its frequency equals the value)
    //   values are in [1, 500], so a count array replaces a hash map; scanning it
    //   from the top returns the largest lucky value first
    /**
     * time = O(N + K), K = 500 (the value range)
     * space = O(K)
     */
    public int findLucky(int[] arr) {
        int[] frequency = new int[MAX_VALUE + 1];
        for (int value : arr) {
            frequency[value]++;
        }

        for (int value = MAX_VALUE; value >= 1; value--) {
            if (frequency[value] == value) {
                return value;
            }
        }
        // no lucky integer
        return -1;
    }
}
