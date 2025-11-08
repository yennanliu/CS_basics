package LeetCodeJava.Heap;

// https://leetcode.com/problems/ugly-number-ii/description/

import java.util.*;

/**
 * 264. Ugly Number II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.
 *
 * Given an integer n, return the nth ugly number.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 10
 * Output: 12
 * Explanation: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12] is the sequence of the first 10 ugly numbers.
 * Example 2:
 *
 * Input: n = 1
 * Output: 1
 * Explanation: 1 has no prime factors, therefore all of its prime factors are limited to 2, 3, and 5.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1690
 *
 *
 *
 */
public class UglyNumber2 {

    // V0
//    public int nthUglyNumber(int n) {
//
//    }

    // V1-1
    // IDEA: SET
    // https://leetcode.com/problems/ugly-number-ii/editorial/
    public int nthUglyNumber_1_1(int n) {
        TreeSet<Long> uglyNumbersSet = new TreeSet<>(); // TreeSet to store potential ugly numbers
        uglyNumbersSet.add(1L); // Start with 1, the first ugly number
        // TreeSet automatically sorts elements in ascending order and does not
        // allow duplicate entries, just like a HashSet in python

        Long currentUgly = 1L;
        for (int i = 0; i < n; i++) {
            currentUgly = uglyNumbersSet.pollFirst(); // Get the smallest number from the set and remove it

            // Insert the next potential ugly numbers into the set
            uglyNumbersSet.add(currentUgly * 2);
            uglyNumbersSet.add(currentUgly * 3);
            uglyNumbersSet.add(currentUgly * 5);
        }

        return currentUgly.intValue(); // Return the nth ugly number
    }


    // V1-2
    // IDEA: Min-Heap/Priority Queue
    // https://leetcode.com/problems/ugly-number-ii/editorial/
    public int nthUglyNumber_1_2(int n) {
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        Set<Long> seenNumbers = new HashSet<>(); // Set to avoid duplicates
        int[] primeFactors = { 2, 3, 5 }; // Factors for generating new ugly numbers

        minHeap.offer(1L);
        seenNumbers.add(1L);

        long currentUgly = 1L;
        for (int i = 0; i < n; i++) {
            currentUgly = minHeap.poll(); // Get the smallest number

            // Generate and push the next ugly numbers
            for (int prime : primeFactors) {
                long nextUgly = currentUgly * prime;
                if (seenNumbers.add(nextUgly)) { // Avoid duplicates
                    minHeap.offer(nextUgly);
                }
            }
        }

        return (int) currentUgly; // Return the nth ugly number
    }


    // V1-3
    // IDEA: DP
    // https://leetcode.com/problems/ugly-number-ii/editorial/
    public int nthUglyNumber_1_3(int n) {
        int[] uglyNumbers = new int[n]; // DP array to store ugly numbers
        uglyNumbers[0] = 1; // The first ugly number is 1

        // Three pointers for the multiples of 2, 3, and 5
        int indexMultipleOf2 = 0, indexMultipleOf3 = 0, indexMultipleOf5 = 0;
        int nextMultipleOf2 = 2, nextMultipleOf3 = 3, nextMultipleOf5 = 5;

        // Generate ugly numbers until we reach the nth one
        for (int i = 1; i < n; i++) {
            // Find the next ugly number as the minimum of the next multiples
            int nextUglyNumber = Math.min(
                    nextMultipleOf2,
                    Math.min(nextMultipleOf3, nextMultipleOf5)
            );
            uglyNumbers[i] = nextUglyNumber;

            // Update the corresponding pointer and next multiple
            if (nextUglyNumber == nextMultipleOf2) {
                indexMultipleOf2++;
                nextMultipleOf2 = uglyNumbers[indexMultipleOf2] * 2;
            }
            if (nextUglyNumber == nextMultipleOf3) {
                indexMultipleOf3++;
                nextMultipleOf3 = uglyNumbers[indexMultipleOf3] * 3;
            }
            if (nextUglyNumber == nextMultipleOf5) {
                indexMultipleOf5++;
                nextMultipleOf5 = uglyNumbers[indexMultipleOf5] * 5;
            }
        }

        return uglyNumbers[n - 1]; // Return the nth ugly number
    }


    // V2-1
    // IDEA:
    // https://leetcode.ca/2016-08-20-264-Ugly-Number-II/
    public int nthUglyNumber_2_1(int n) {
        if (n <= 0) {
            return 0;
        }

        List<Integer> nums = new ArrayList<>();
        nums.add(1);

        int i2 = 0;
        int i3 = 0;
        int i5 = 0;

        while (nums.size() < n) {

            int m2 = nums.get(i2) * 2;
            int m3 = nums.get(i3) * 3;
            int m5 = nums.get(i5) * 5;

            int mn = Math.min(Math.min(m2, m3), m5);
            nums.add(mn);

            if (mn == m2) {
                i2++;
            }

            if (mn == m3) { // @note: 3*2 and 2*3 are both 6, so cannot else-if
                i3++;
            }

            if (mn == m5) {
                i5++;
            }
        }

        return nums.get(nums.size() - 1);
    }


    // V2-2
    // IDEA:
    // https://leetcode.ca/2016-08-20-264-Ugly-Number-II/
    public int nthUglyNumber_2_2(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        int p2 = 0, p3 = 0, p5 = 0;
        for (int i = 1; i < n; ++i) {
            int next2 = dp[p2] * 2, next3 = dp[p3] * 3, next5 = dp[p5] * 5;
            dp[i] = Math.min(next2, Math.min(next3, next5));
            if (dp[i] == next2) ++p2;
            if (dp[i] == next3) ++p3;
            if (dp[i] == next5) ++p5;
        }
        return dp[n - 1];

    }




}
