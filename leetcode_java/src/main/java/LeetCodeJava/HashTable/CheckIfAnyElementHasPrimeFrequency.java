package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-any-element-has-prime-frequency/description/

import java.util.*;

/**
 * 3591. Check if Any Element Has Prime Frequency
 * Easy
 *
 * You are given an integer array nums.
 *
 * Return true if the frequency of any element of the array is prime, otherwise, return false.
 *
 * The frequency of an element x is the number of times it occurs in the array.
 *
 * A prime number is a natural number greater than 1 with only two factors, 1 and itself.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,4]
 * Output: True
 * Explanation:
 * 4 appears 2 times. 2 is a prime number.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: False
 * Explanation:
 * All elements appear only once. 1 is not a prime number.
 *
 * Example 3:
 *
 * Input: nums = [2,2,2,4,4]
 * Output: True
 * Explanation:
 * Both 2 and 4 appear 3 and 2 times respectively. Both 3 and 2 are prime numbers.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 100
 *
 */
public class CheckIfAnyElementHasPrimeFrequency {

    // V0
    // IDEA: COUNT, THEN TRIAL-DIVIDE EACH FREQUENCY
    //   a frequency is at most nums.length (100), so trial division up to its
    //   square root is plenty - no sieve needed.
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public boolean checkPrimeFrequency(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        for (int frequency : count.values()) {
            if (isPrime(frequency)) {
                return true;
            }
        }
        return false;
    }

    /** Trial division primality test. */
    private boolean isPrime(int x) {
        if (x < 2) {
            return false;
        }
        for (int d = 2; d * d <= x; d++) {
            if (x % d == 0) {
                return false;
            }
        }
        return true;
    }
}
