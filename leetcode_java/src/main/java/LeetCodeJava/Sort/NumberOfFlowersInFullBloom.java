package LeetCodeJava.Sort;

// https://leetcode.com/problems/number-of-flowers-in-full-bloom/

import java.util.Arrays;

/**
 *  2251. Number of Flowers in Full Bloom
 *  Hard
 *
 *  You are given a 0-indexed 2D integer array flowers, where
 *  flowers[i] = [start_i, end_i] means the ith flower will be in full bloom from
 *  start_i to end_i (inclusive). You are also given a 0-indexed integer array
 *  people of size n, where people[i] is the time that the ith person will arrive
 *  to see the flowers.
 *
 *  Return an integer array answer of size n, where answer[i] is the number of
 *  flowers that are in full bloom when the ith person arrives.
 *
 *  Example 1:
 *    Input: flowers = [[1,6],[3,7],[9,12],[4,13]], people = [2,3,7,11]
 *    Output: [1,2,2,2]
 *
 *  Example 2:
 *    Input: flowers = [[1,10],[3,3]], people = [3,3,2]
 *    Output: [2,2,1]
 *
 *  Constraints:
 *    1 <= flowers.length <= 5 * 10^4
 *    flowers[i].length == 2
 *    1 <= start_i <= end_i <= 10^9
 *    1 <= people.length <= 5 * 10^4
 *    1 <= people[i] <= 10^9
 */
public class NumberOfFlowersInFullBloom {

    // V0
    // IDEA: INCLUSION-EXCLUSION ON TWO SORTED ARRAYS OF ENDPOINTS
    //       at time t the number of blooming flowers is
    //           (how many have already STARTED) - (how many have already ENDED)
    //
    //       sort the starts and the ends independently - the pairing between them
    //       is irrelevant for a counting question - then per person:
    //           started = # starts <= t
    //           ended   = # ends   <  t      (the bloom is inclusive)
    //
    //       the strict/non-strict sides carry the inclusivity: a flower opening
    //       exactly at t counts, a flower closing exactly at t is NOT retired yet.
    /**
     * time = O((F + P) log F)
     * space = O(F)
     */
    public int[] fullBloomFlowers(int[][] flowers, int[] people) {
        int f = flowers.length;
        int[] starts = new int[f];
        int[] ends = new int[f];
        for (int i = 0; i < f; i++) {
            starts[i] = flowers[i][0];
            ends[i] = flowers[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        int[] res = new int[people.length];
        for (int i = 0; i < people.length; i++) {
            int t = people[i];
            res[i] = countLessOrEqual(starts, t) - countLess(ends, t);
        }
        return res;
    }

    // number of elements <= target
    private int countLessOrEqual(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // number of elements < target
    private int countLess(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
