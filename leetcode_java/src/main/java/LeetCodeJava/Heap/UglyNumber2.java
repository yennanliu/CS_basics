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

    // V0-1
    // IDEA: PQ (fixed by gpt)
    public int nthUglyNumber_0_1(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        // Min-heap for ascending order
        PriorityQueue<Long> pq = new PriorityQueue<>();
        Set<Long> seen = new HashSet<>();

        pq.offer(1L);
        seen.add(1L);

        long current = 1;
        int[] factors = {2, 3, 5};

        for (int i = 0; i < n; i++) {
            current = pq.poll(); // smallest so far

            for (int f : factors) {
                long next = current * f;
                if (!seen.contains(next)) {
                    pq.offer(next);
                    seen.add(next);
                }
            }
        }

        return (int) current;
    }

    // V0-3
    // IDEA: DP (gemini)
    /**
     * Finds the n-th ugly number using Dynamic Programming (Multi-pointer merge).
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int nthUglyNumber_0_3(int n) {
        // Base case: 1 is the 1st ugly number.
        if (n <= 0) {
            return 0; // Or throw an exception, depending on expected input for n <= 0.
        }
        if (n == 1) {
            return 1;
        }

        // dp[i] will store the i-th ugly number (1-indexed based on logic, but 0-indexed in array)
        int[] dp = new int[n];
        dp[0] = 1; // The first ugly number is 1

        // Three pointers for the next multiple of 2, 3, and 5.
        // They track the index in the 'dp' array.
        int p2 = 0;
        int p3 = 0;
        int p5 = 0;

        // The actual next candidate ugly numbers
        long next2 = dp[p2] * 2L;
        long next3 = dp[p3] * 3L;
        long next5 = dp[p5] * 5L;

        // Generate ugly numbers from the 2nd (index 1) up to the n-th (index n-1)
        for (int i = 1; i < n; i++) {
            // Find the minimum of the three candidates. This is the next smallest ugly number.
            int nextUgly = (int) Math.min(next2, Math.min(next3, next5));
            dp[i] = nextUgly;

            // Advance the pointer(s) corresponding to the chosen minimum.
            // Note: We use 'if' instead of 'else if' to handle duplicates (e.g., 6 is 2*3 and 3*2).
            if (nextUgly == next2) {
                p2++;
                next2 = dp[p2] * 2L; // Calculate the new next multiple of 2
            }
            if (nextUgly == next3) {
                p3++;
                next3 = dp[p3] * 3L; // Calculate the new next multiple of 3
            }
            if (nextUgly == next5) {
                p5++;
                next5 = dp[p5] * 5L; // Calculate the new next multiple of 5
            }
        }

        // The n-th ugly number is at the last position.
        return dp[n - 1];
    }


    // V0-4
    // IDEA: DP (fixed by gpt)
    public int nthUglyNumber_0_4(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        if (n == 1) {
            return 1;
        }

        int[] dp = new int[n];
        dp[0] = 1;
        int i2 = 0, i3 = 0, i5 = 0;

        for (int i = 1; i < n; i++) {
            int next2 = dp[i2] * 2;
            int next3 = dp[i3] * 3;
            int next5 = dp[i5] * 5;

            int nextUgly = Math.min(next2, Math.min(next3, next5));
            dp[i] = nextUgly;

            if (nextUgly == next2) i2++;
            if (nextUgly == next3) i3++;
            if (nextUgly == next5) i5++;
        }

        return dp[n - 1];
    }

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
