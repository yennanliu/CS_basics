package LeetCodeJava.Math;

// https://leetcode.com/problems/distinct-prime-factors-of-product-of-array/description/

import java.util.*;

/**
 * 2521. Distinct Prime Factors of Product of Array
 * Medium
 *
 * Given an array of positive integers nums, return the number of distinct prime
 * factors in the product of the elements of nums.
 *
 * Note that:
 *
 * A number greater than 1 is called prime if it is divisible by only 1 and itself.
 * An integer val1 is a factor of another integer val2 if val2 / val1 is an integer.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,3,7,10,6]
 * Output: 4
 * Explanation:
 * The product of all the elements in nums is: 2 * 4 * 3 * 7 * 10 * 6 = 10080 = 2^5 * 3^2 * 5 * 7.
 * There are 4 distinct prime factors so we return 4.
 *
 * Example 2:
 *
 * Input: nums = [2,4,8,16]
 * Output: 1
 * Explanation:
 * The product of all the elements in nums is: 2 * 4 * 8 * 16 = 1024 = 2^10.
 * There is 1 distinct prime factor so we return 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * 2 <= nums[i] <= 1000
 *
 */
public class DistinctPrimeFactorsOfProductOfArray {

    // V0
    // IDEA: TRIAL DIVISION + SET - THE PRIME FACTORS OF A PRODUCT ARE THE UNION OF EACH TERM'S
    //   so never build the (huge) product; factorize each number up to sqrt and collect primes.
    /**
     * time = O(n * sqrt(m))
     * space = O(number of distinct primes)
     */
    public int distinctPrimeFactors(int[] nums) {
        Set<Integer> primes = new HashSet<>();
        for (int num : nums) {
            int rest = num;
            for (int factor = 2; factor * factor <= rest; factor++) {
                if (rest % factor == 0) {
                    primes.add(factor);
                    // divide it out completely so only primes are ever met
                    while (rest % factor == 0) {
                        rest /= factor;
                    }
                }
            }
            // whatever is left above 1 is a prime larger than sqrt(num)
            if (rest > 1) {
                primes.add(rest);
            }
        }
        return primes.size();
    }
}
