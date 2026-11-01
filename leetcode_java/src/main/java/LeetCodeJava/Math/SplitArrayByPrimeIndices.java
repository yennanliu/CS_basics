package LeetCodeJava.Math;

// https://leetcode.com/problems/split-array-by-prime-indices/description/

import java.util.*;

/**
 * 3618. Split Array by Prime Indices
 * Medium
 *
 * You are given an integer array nums.
 *
 * Split nums into two arrays A and B using the following rule:
 *
 * Elements at prime indices in nums must go into array A.
 * All other elements must go into array B.
 *
 * Return the absolute difference between the sums of the two arrays:
 * |sum(A) - sum(B)|.
 *
 * Note: An empty array has a sum of 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,4]
 * Output: 1
 * Explanation:
 * The only prime index in the array is 2, so nums[2] = 4 is placed in array A.
 * The remaining elements, nums[0] = 2 and nums[1] = 3 are placed in array B.
 * sum(A) = 4, sum(B) = 2 + 3 = 5.
 * The absolute difference is |4 - 5| = 1.
 *
 * Example 2:
 *
 * Input: nums = [-1,5,7,0]
 * Output: 3
 * Explanation:
 * The prime indices in the array are 2 and 3, so nums[2] = 7 and nums[3] = 0 are
 * placed in array A.
 * The remaining elements, nums[0] = -1 and nums[1] = 5 are placed in array B.
 * sum(A) = 7 + 0 = 7, sum(B) = -1 + 5 = 4.
 * The absolute difference is |7 - 4| = 3.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class SplitArrayByPrimeIndices {

    // V0
    // IDEA: SIEVE OF ERATOSTHENES + SIGNED SUM IN ONE PASS
    //       the split depends only on the index, so A and B never need to be built: add nums[i]
    //       at a prime index, subtract it otherwise; the total is sum(A) - sum(B). a sieve marks
    //       every prime index below n. values reach 10^9 * 10^5 -> use long.
    /**
     * time = O(N log log N)
     * space = O(N)
     */
    public long splitArray(int[] nums) {
        int n = nums.length;

        // isPrime[i] for every index i < n
        boolean[] isPrime = new boolean[Math.max(n, 2)];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
        for (int i = 2; (long) i * i < n; i++) {
            if (isPrime[i]) {
                // cross out multiples starting at i*i; smaller ones were crossed by smaller primes
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        long difference = 0;
        for (int i = 0; i < n; i++) {
            if (isPrime[i]) {
                difference += nums[i];
            } else {
                difference -= nums[i];
            }
        }
        return Math.abs(difference);
    }
}
