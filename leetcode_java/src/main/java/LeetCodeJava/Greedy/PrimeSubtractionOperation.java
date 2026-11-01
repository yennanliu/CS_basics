package LeetCodeJava.Greedy;

// https://leetcode.com/problems/prime-subtraction-operation/description/

import java.util.*;

/**
 * 2601. Prime Subtraction Operation
 * Medium
 *
 * You are given a 0-indexed integer array nums of length n.
 *
 * You can perform the following operation as many times as you want:
 *
 * - Pick an index i that you haven't picked before, and pick a prime p strictly less than nums[i], then subtract p from nums[i].
 *
 * Return true if you can make nums a strictly increasing array using the above operation and false otherwise.
 *
 * A strictly increasing array is an array whose each element is strictly greater than its preceding element.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,9,6,10]
 * Output: true
 * Explanation: In the first operation: Pick i = 0 and p = 3, and then subtract 3 from nums[0], so that nums becomes [1,9,6,10].
 * In the second operation: i = 1, p = 7, subtract 7 from nums[1], so nums becomes equal to [1,2,6,10].
 * After the second operation, nums is sorted in strictly increasing order, so the answer is true.
 *
 * Example 2:
 *
 * Input: nums = [6,8,11,12]
 * Output: true
 * Explanation: Initially nums is sorted in strictly increasing order, so we don't need to make any operations.
 *
 * Example 3:
 *
 * Input: nums = [5,8,3]
 * Output: false
 * Explanation: It can be proven that there is no way to perform operations to make nums sorted in strictly increasing order, so the answer is false.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 * nums.length == n
 *
 */
public class PrimeSubtractionOperation {

    private static final int LIMIT = 1000;

    // V0
    // IDEA: SIEVE + GREEDY (make every element as small as legally possible)
    //       left to right, keep `prev` = the value settled for the previous index.
    //       for nums[i] subtract the LARGEST prime p < nums[i] with nums[i] - p > prev
    //       (or subtract nothing); a smaller value only helps the elements after it.
    /**
     * time = O(V log log V + N * pi(V))   (V = 1000, pi(V) = number of primes below V)
     * space = O(V)
     */
    public boolean primeSubOperation(int[] nums) {
        List<Integer> primes = primesUpTo(LIMIT);

        int prev = 0;
        for (int x : nums) {
            // "do nothing" is a candidate only if it already beats prev; -1 = no candidate
            int best = -1;
            if (x > prev) {
                best = x;
            }
            // primes are ascending, so the last one that works is the largest -> smallest result
            for (int p : primes) {
                if (p >= x) {
                    break;
                }
                if (x - p > prev) {
                    best = x - p;
                }
            }
            if (best == -1) {
                // values can only shrink, so nothing later can fix this
                return false;
            }
            prev = best;
        }
        return true;
    }

    /** Sieve of Eratosthenes: every prime in [2, limit], ascending. */
    private List<Integer> primesUpTo(int limit) {
        boolean[] composite = new boolean[limit + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (composite[i]) {
                continue;
            }
            primes.add(i);
            // multiples below i * i were already crossed out by smaller primes
            for (long j = (long) i * i; j <= limit; j += i) {
                composite[(int) j] = true;
            }
        }
        return primes;
    }
}
