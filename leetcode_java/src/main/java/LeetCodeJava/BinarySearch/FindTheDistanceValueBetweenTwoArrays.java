package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-the-distance-value-between-two-arrays/description/

import java.util.Arrays;

/**
 * 1385. Find the Distance Value Between Two Arrays
 * Easy
 *
 * Given two integer arrays arr1 and arr2, and the integer d, return the distance
 * value between the two arrays.
 *
 * The distance value is defined as the number of elements arr1[i] such that there
 * is not any element arr2[j] where |arr1[i]-arr2[j]| <= d.
 *
 *
 * Example 1:
 *
 * Input: arr1 = [4,5,8], arr2 = [10,9,1,8], d = 2
 * Output: 2
 * Explanation:
 * For arr1[0]=4 every |4 - arr2[j]| > 2, for arr1[1]=5 every |5 - arr2[j]| > 2,
 * but arr1[2]=8 has |8-10|=2, |8-9|=1 and |8-8|=0, all <= d=2.
 *
 * Example 2:
 *
 * Input: arr1 = [1,4,2,3], arr2 = [-4,-3,6,10,20,30], d = 3
 * Output: 2
 *
 * Example 3:
 *
 * Input: arr1 = [2,1,100,3], arr2 = [-5,-2,10,-3,7], d = 6
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= arr1.length, arr2.length <= 500
 * -1000 <= arr1[i], arr2[j] <= 1000
 * 0 <= d <= 100
 *
 */
public class FindTheDistanceValueBetweenTwoArrays {

    // V0
    // IDEA: SORT arr2 + BINARY SEARCH (LOWER BOUND)
    /**
     *  For x in arr1 we only need to know whether arr2 has any value inside [x - d, x + d].
     *  The lower bound of x - d is the first arr2 value >= x - d; if it exists and is
     *  <= x + d, x is disqualified.
     */
    /**
     * time = O((M + N) log N)
     * space = O(N), the sorted copy
     */
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        int[] sorted = arr2.clone();
        Arrays.sort(sorted);

        int count = 0;
        for (int x : arr1) {
            int index = lowerBound(sorted, x - d);
            boolean noneInWindow = index == sorted.length || sorted[index] > x + d;
            if (noneInWindow) {
                count++;
            }
        }
        return count;
    }

    /** First index whose value is >= target, or arr.length if none. */
    private int lowerBound(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    // V1
    // IDEA: BRUTE FORCE DOUBLE LOOP (lengths <= 500, so at most 250k checks)
    /**
     * time = O(M * N)
     * space = O(1)
     */
    public int findTheDistanceValue_1(int[] arr1, int[] arr2, int d) {
        int count = 0;
        for (int x : arr1) {
            boolean farFromAll = true;
            for (int y : arr2) {
                if (Math.abs(x - y) <= d) {
                    farFromAll = false;
                    break;
                }
            }
            if (farFromAll) {
                count++;
            }
        }
        return count;
    }
}
