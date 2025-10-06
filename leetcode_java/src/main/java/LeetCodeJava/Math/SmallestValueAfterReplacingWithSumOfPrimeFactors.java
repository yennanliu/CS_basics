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
//    public int smallestValue(int n) {
//
//    }

    // V0-1
    // IDEA: MATH (fixed by gemini)
    /**
     * Calculates the smallest value by repeatedly replacing a number n with the
     * sum of its prime factors until n becomes a prime number.
     * The process stops when n == sum_of_prime_factors(n).
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


    // TODO: fix below
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
