package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/missing-number-in-arithmetic-progression/description/

/**
 * 1228. Missing Number In Arithmetic Progression
 * Easy
 *
 * In some array arr, the values were in arithmetic progression: the values
 * arr[i + 1] - arr[i] are all equal for every 0 <= i < arr.length - 1.
 *
 * A value from arr was removed that was not the first or last value in the array.
 *
 * Given arr, return the removed value.
 *
 *
 * Example 1:
 *
 * Input: arr = [5,7,11,13]
 * Output: 9
 * Explanation: The previous array was [5,7,9,11,13].
 *
 * Example 2:
 *
 * Input: arr = [15,13,12]
 * Output: 14
 * Explanation: The previous array was [15,14,13,12].
 *
 *
 * Constraints:
 *
 * 3 <= arr.length <= 1000
 * 0 <= arr[i] <= 10^5
 * The given array is guaranteed to be a valid array.
 *
 */
public class MissingNumberInArithmeticProgression {

    // V0
    // IDEA: BINARY SEARCH ON THE "STILL IN PLACE" PREDICATE
    /**
     *  The original progression had n + 1 terms, so its common difference is
     *  diff = (arr[n-1] - arr[0]) / n.
     *
     *  Every element before the gap satisfies arr[i] == arr[0] + i * diff; every element
     *  after it does not (it shifted left one slot). That predicate is monotonic, so binary
     *  search the first index where it breaks: the missing value is arr[0] + index * diff.
     */
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int missingNumber(int[] arr) {
        int n = arr.length;
        int diff = (arr[n - 1] - arr[0]) / n;

        int left = 0;
        int right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == arr[0] + mid * diff) {
                // still in place -> the gap is to the right of mid
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return arr[0] + left * diff;
    }

    // V1
    // IDEA: ARITHMETIC SERIES SUM
    // the full progression (n + 1 terms) sums to (first + last) * (n + 1) / 2;
    // the missing value is that total minus the sum of arr
    /**
     * time = O(N)
     * space = O(1)
     */
    public int missingNumber_1(int[] arr) {
        int n = arr.length;
        int expectedSum = (arr[0] + arr[n - 1]) * (n + 1) / 2;
        int actualSum = 0;
        for (int value : arr) {
            actualSum += value;
        }
        return expectedSum - actualSum;
    }
}
