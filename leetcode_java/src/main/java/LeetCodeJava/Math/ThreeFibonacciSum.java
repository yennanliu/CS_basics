package LeetCodeJava.Math;

// https://leetcode.com/problems/three-fibonacci-sum/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 4078. Three Fibonacci Sum
 * Easy
 *
 * You are given an integer n.
 *
 * The Fibonacci sequence starts with 0 and 1, and each subsequent number is the
 * sum of the previous two numbers. Its first few terms are
 * 0, 1, 1, 2, 3, 5, 8, 13, ....
 *
 * Return true if n can be expressed as the sum of three consecutive terms in the
 * Fibonacci sequence, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: n = 16
 * Output: true
 * Explanation:
 * The three consecutive terms 3, 5, and 8 have a sum of 3 + 5 + 8 = 16.
 *
 * Example 2:
 *
 * Input: n = 8
 * Output: false
 * Explanation:
 * Although 1 + 2 + 5 = 8, these terms are not consecutive in the Fibonacci
 * sequence. No three consecutive terms have a sum of 8.
 *
 * Example 3:
 *
 * Input: n = 2
 * Output: true
 * Explanation:
 * The first three terms have a sum of 0 + 1 + 1 = 2.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class ThreeFibonacciSum {

    // V0
    // IDEA: GENERATE FIBONACCI TERMS + SLIDE A WINDOW OF 3 CONSECUTIVE TERMS
    //       terms grow exponentially, so only ~45 stay <= 10^9: build them all, then check
    //       every window of 3. window sums only grow (2, 4, 6, 10, ...), so stop once past n.
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public boolean threeFibonacciSum(int n) {
        // build every term up to n (a window containing a term > n already sums past n);
        // long because the first term past 10^9 is still computed
        List<Long> fib = new ArrayList<>();
        fib.add(0L);
        fib.add(1L);
        while (fib.get(fib.size() - 1) <= n) {
            int size = fib.size();
            fib.add(fib.get(size - 1) + fib.get(size - 2));
        }

        // try every window of 3 consecutive terms
        for (int i = 0; i + 2 < fib.size(); i++) {
            long windowSum = fib.get(i) + fib.get(i + 1) + fib.get(i + 2);
            if (windowSum == n) {
                return true;
            }
            // window sums only grow, so once past n it is hopeless
            if (windowSum > n) {
                return false;
            }
        }
        return false;
    }

    // V1
    // IDEA: MATH -- F(i) + F(i+1) + F(i+2) == 2 * F(i+2)
    //       since F(i) + F(i+1) = F(i+2), n is such a sum exactly when n is even and n / 2 is a
    //       Fibonacci term F(k) with k >= 2 (the first window is 0 + 1 + 1 = 2 * F(2)).
    //       same bound as V0; the identity is the thing worth remembering.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean threeFibonacciSum_1(int n) {
        // 2 * F(k) is always even
        if (n % 2 == 1) {
            return false;
        }

        int half = n / 2;

        // walk F(2), F(3), ... = 1, 2, 3, 5, ... until it reaches half
        long prev = 1; // F(1)
        long cur = 1;  // F(2)
        while (cur < half) {
            long next = prev + cur;
            prev = cur;
            cur = next;
        }
        return cur == half;
    }
}
