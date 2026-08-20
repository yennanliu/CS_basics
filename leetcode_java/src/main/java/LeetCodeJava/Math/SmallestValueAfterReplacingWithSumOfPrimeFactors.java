package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-value-after-replacing-with-sum-of-prime-factors/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2507. Smallest Value After Replacing With Sum of Prime Factors
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a positive integer n.
 *
 * Continuously replace n with the sum of its prime factors.
 *
 * Note that if a prime factor divides n multiple times, it should be included in the sum as many times as it divides n.
 * Return the smallest value n will take on.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 15
 * Output: 5
 * Explanation: Initially, n = 15.
 * 15 = 3 * 5, so replace n with 3 + 5 = 8.
 * 8 = 2 * 2 * 2, so replace n with 2 + 2 + 2 = 6.
 * 6 = 2 * 3, so replace n with 2 + 3 = 5.
 * 5 is the smallest value n will take on.
 * Example 2:
 *
 * Input: n = 3
 * Output: 3
 * Explanation: Initially, n = 3.
 * 3 is the smallest value n will take on.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 105
 *
 *
 *
 */
public class SmallestValueAfterReplacingWithSumOfPrimeFactors {

    // V0
    // IDEA: MATH (prime factorization) + loop until n stops changing
    /**
     *  NOTE !!!
     *
     *   1) keep replacing n with `sum of its prime factors`
     *      (a prime factor is counted as many times as it divides n)
     *
     *   2) the loop MUST stop when sum == n, otherwise it loops forever
     *      -> sum == n happens exactly when n is prime
     *         (prime p factorizes as p, so sum = p = n)
     *
     *   3) each replacement strictly decreases n (for composite n),
     *      so the loop terminates
     */
    /**
     * time = O(log(N) * sqrt(N))
     * space = O(1)
     */
    public int smallestValue(int n) {
        // edge
        if (n <= 3) {
            return n;
        }

        while (true) {
            int sum = getPrimeFactorSum(n);
            /** NOTE !!! if sum == n, n is already prime -> can NOT be reduced */
            if (sum == n) {
                break;
            }
            n = sum;
        }

        return n;
    }

    // sum of prime factors (with multiplicity)
    private int getPrimeFactorSum(int x) {
        int sum = 0;
        for (int i = 2; (long) i * i <= x; i++) {
            while (x % i == 0) {
                sum += i;
                x /= i;
            }
        }
        // the `remaining` x (if > 1) is a prime factor as well
        if (x > 1) {
            sum += x;
        }
        return sum;
    }

    // V0-1
    // IDEA: MATH (fixed by gemini)
    /**
     * Calculates the smallest value by repeatedly replacing a number n with the
     * sum of its prime factors until n becomes a prime number.
     * The process stops when n == sum_of_prime_factors(n).
     */

    /**
     * time = O(N)
     * space = O(1)
     */
    public int smallestValue_0_1(int n) {
        // Base case: If n is 1, return 1. (Though constraints start at n=2)
        if (n <= 1) {
            return n;
        }

        // Loop as long as the number can be reduced.
        // We break when sumOfFactors == n, which means n is prime.
        while (true) {
            // Get the list of prime factors (including duplicates)
            List<Integer> factors = getPrimeFactors(n);

            // Calculate the sum of these factors
            int sumOfFactors = getListSum(factors);

            /**
             *  NOTE !!!
             *
             *   have below logic to AVOID `ENDLESS LOOP`
             *   e.g. we should JUMP OUT from while loop
             *       if the `sumOfFactors` is unchanged (compared with prev iteration)
             *
             */
            // If the number doesn't change, we've reached a prime number (the smallest value).
            if (sumOfFactors == n) {
                break;
            }

            // Otherwise, replace n with the new sum and repeat.
            n = sumOfFactors;
        }

        return n;
    }

    /**
     * Correctly finds the prime factors of x (including duplicates).
     * This replaces the flawed divideToFactors function.
     */
    private List<Integer> getPrimeFactors(int x) {
        List<Integer> res = new ArrayList<>();
        int i = 2;

        // Loop up to the square root of the current value of x.
        while (i * i <= x) {
            while (x % i == 0) {
                res.add(i);
                x = x / i;
            }
            // Only increment i for the next potential prime factor
            i += 1;
        }

        // If x is greater than 1 after the loop, the remaining x is the largest prime factor.
        if (x > 1) {
            res.add(x);
        }

        return res;
    }

    /**
     * Calculates the sum of all elements in the list.
     * This function was already correct.
     */
    private int getListSum(List<Integer> list) {
        int res = 0;
        for (int x : list) {
            res += x;
        }
        return res;
    }

    // The unnecessary and flawed hasFactor function is removed.

    // V0-2

    // IDEA: MATH (gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int smallestValue_0_2(int n) {
        while (true) {
            int sum = sumOfPrimeFactors(n);
            if (sum == n)
                break; // No further reduction possible
            n = sum;
        }
        return n;
    }

    private int sumOfPrimeFactors(int n) {
        int sum = 0;
        int i = 2;
        while (i * i <= n) {
            while (n % i == 0) {
                sum += i;
                n /= i;
            }
            i++;
        }
        if (n > 1) {
            sum += n; // Add the last prime factor if any
        }
        return sum;
    }


    // NOTE !!! below is WRONG (`hasFactor` + `divideToFactors` cause an endless loop
    //           for prime n, and divideToFactors is O(N)), fixed version is V0 above
//    public int smallestValue(int n) {
//        // edge
//        if(n <= 3){
//            return n;
//        }
//
//        while(hasFactor(n)){
//            // ....
//            List<Integer> list = divideToFactors(n);
//            System.out.println(">>> n = " + n + ", list = " + list);
//            n = getListSum(list);
//        }
//
//        return n;
//    }
//
//    private int getListSum(List<Integer> list){
//        int res = 0;
//        for(int x: list){
//            res += x;
//        }
//        return res;
//    }
//
//    private List<Integer> divideToFactors(int x){
//        List<Integer> res = new ArrayList<>();
//        // ????
//        int i = 2;
//        //int sqrtX = (int) Math.sqrt(x); // ??
//        while(i < x){
//            // ???
//            while(x % i == 0){
//                //return true;
//                res.add(i);
//                x = x / i; // ???
//            }
//            i += 1;
//        }
//
//        // ??
//        if(x != 1){
//            res.add(x); // ?? append the `remaining val` to list
//        }
//
//        return res;
//    }
//
//    private boolean hasFactor(int x){
//        // ???
//        int sqrtX = (int) Math.sqrt(x); // ??
//        int i = 2;
//        System.out.println(">>> (hasFactor)  x = " + x + ", sqrtX = " + sqrtX);
//        while(i <= sqrtX + 1){
//            System.out.println(">>> (hasFactor)  x = " + x + ", i = " + i);
//            if(x % i == 0){
//                return true;
//            }
//            i += 1;
//        }
//        return false;
//    }


}
