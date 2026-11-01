package LeetCodeJava.Array;

// https://leetcode.com/problems/detect-pattern-of-length-m-repeated-k-or-more-times/description/

/**
 * 1566. Detect Pattern of Length M Repeated K or More Times
 * Easy
 *
 * Given an array of positive integers arr, find a pattern of length m that is repeated k
 * or more times.
 *
 * A pattern is a subarray (consecutive sub-sequence) that consists of one or more values,
 * repeated multiple times consecutively without overlapping. A pattern is defined by its
 * length and the number of repetitions.
 *
 * Return true if there exists a pattern of length m that is repeated k or more times,
 * otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,4,4,4,4], m = 1, k = 3
 * Output: true
 *
 * Example 2:
 *
 * Input: arr = [1,2,1,2,1,1,1,3], m = 2, k = 2
 * Output: true
 *
 * Example 3:
 *
 * Input: arr = [1,2,1,2,1,3], m = 2, k = 3
 * Output: false
 *
 *
 * Constraints:
 *
 * 2 <= arr.length <= 100
 * 1 <= arr[i] <= 100
 * 1 <= m <= 100
 * 2 <= k <= 100
 *
 */
public class DetectPatternOfLengthMRepeatedKOrMoreTimes {

    // V0
    // IDEA: RUN COUNTING — a length-m pattern repeated k times spans m * k cells, which is
    //       the same as m * (k - 1) CONSECUTIVE matches of arr[i] == arr[i + m]
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean containsPattern(int[] arr, int m, int k) {
        int matchesNeeded = m * (k - 1);
        int matchRun = 0;

        for (int i = 0; i + m < arr.length; i++) {
            if (arr[i] == arr[i + m]) {
                matchRun++;
                if (matchRun == matchesNeeded) {
                    return true;
                }
            } else {
                // the period-m repetition is broken here
                matchRun = 0;
            }
        }
        return false;
    }

    // V1
    // IDEA: BRUTE FORCE — try every start i and check that each of the k - 1 following
    //       blocks of length m equals the first block. Easy to verify, slower.
    /**
     * time = O(N * M * K)
     * space = O(1)
     */
    public boolean containsPattern_1(int[] arr, int m, int k) {
        int n = arr.length;
        for (int start = 0; start + m * k <= n; start++) {
            boolean repeated = true;
            // compare every cell in the k*m window with the cell m positions earlier
            for (int offset = m; offset < m * k; offset++) {
                if (arr[start + offset] != arr[start + offset - m]) {
                    repeated = false;
                    break;
                }
            }
            if (repeated) {
                return true;
            }
        }
        return false;
    }
}
