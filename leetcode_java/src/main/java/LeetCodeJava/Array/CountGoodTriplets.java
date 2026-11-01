package LeetCodeJava.Array;

// https://leetcode.com/problems/count-good-triplets/description/

/**
 * 1534. Count Good Triplets
 * Easy
 *
 * Given an array of integers arr, and three integers a, b and c. You need to find the
 * number of good triplets.
 *
 * A triplet (arr[i], arr[j], arr[k]) is good if the following conditions are true:
 *
 * 0 <= i < j < k < arr.length
 * |arr[i] - arr[j]| <= a
 * |arr[j] - arr[k]| <= b
 * |arr[i] - arr[k]| <= c
 *
 * Where |x| denotes the absolute value of x.
 *
 * Return the number of good triplets.
 *
 *
 * Example 1:
 *
 * Input: arr = [3,0,1,1,9,7], a = 7, b = 2, c = 3
 * Output: 4
 * Explanation: There are 4 good triplets: [(3,0,1), (3,0,1), (3,1,1), (0,1,1)].
 *
 * Example 2:
 *
 * Input: arr = [1,1,2,2,3], a = 0, b = 0, c = 1
 * Output: 0
 * Explanation: No triplet satisfies all conditions.
 *
 *
 * Constraints:
 *
 * 3 <= arr.length <= 100
 * 0 <= arr[i] <= 1000
 * 0 <= a, b, c <= 1000
 *
 */
public class CountGoodTriplets {

    // V0
    // IDEA: BRUTE FORCE ENUMERATION WITH EARLY PRUNING — n <= 100, so C(100, 3) ~ 1.6e5
    //       triplets is the intended answer; test the (i, j) condition before the k loop
    /**
     * time = O(N^3)
     * space = O(1)
     */
    public int countGoodTriplets(int[] arr, int a, int b, int c) {
        int n = arr.length;
        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // prune: if (i, j) already fails, no k can rescue it
                if (Math.abs(arr[i] - arr[j]) > a) {
                    continue;
                }
                for (int k = j + 1; k < n; k++) {
                    if (Math.abs(arr[j] - arr[k]) <= b && Math.abs(arr[i] - arr[k]) <= c) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
