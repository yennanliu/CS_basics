package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/smallest-greater-multiple-made-of-two-digits/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1999. Smallest Greater Multiple Made of Two Digits
 *  Medium
 *
 *  Given three integers, k, digit1, and digit2, you want to find the smallest
 *  integer that is:
 *    Larger than k,
 *    A multiple of k, and
 *    Comprised of only the digits digit1 and/or digit2.
 *
 *  Return the smallest such integer. If no such integer exists or the integer
 *  exceeds the limit of a signed 32-bit integer (2^31 - 1), return -1.
 *
 *  Example 1:
 *    Input: k = 2, digit1 = 0, digit2 = 2
 *    Output: 20
 *
 *  Example 2:
 *    Input: k = 3, digit1 = 4, digit2 = 2
 *    Output: 24
 *
 *  Example 3:
 *    Input: k = 2, digit1 = 0, digit2 = 0
 *    Output: -1
 *
 *  Constraints:
 *    1 <= k <= 1000
 *    0 <= digit1 <= 9
 *    0 <= digit2 <= 9
 */
public class SmallestGreaterMultipleMadeOfTwoDigits {

    // V0
    // IDEA: BFS OVER THE DIGIT TREE (generates candidates in increasing order)
    //       every candidate is built by repeatedly appending digit1 or digit2 to a
    //       shorter candidate : x -> x*10 + d.
    //
    //       push the smaller digit first, so a FIFO queue emits candidates in
    //       non-decreasing numeric order - the first one that is > k and divisible by
    //       k is the answer, no sorting needed.
    //
    //       NOTE : with digit1 == digit2 push only one child, otherwise the queue
    //              duplicates every value and blows up.
    //       NOTE : the search is bounded - stop and return -1 as soon as the front of
    //              the queue passes 2^31 - 1, since everything behind it is larger.
    //       NOTE : use long for the arithmetic so appending a digit cannot overflow.
    /**
     * time = O(2^L), L <= 10 digits before the 2^31 cutoff
     * space = O(2^L)
     */
    public int findInteger(int k, int digit1, int digit2) {
        if (digit1 == 0 && digit2 == 0) {
            return -1;
        }
        int lo = Math.min(digit1, digit2);
        int hi = Math.max(digit1, digit2);
        final long LIMIT = Integer.MAX_VALUE;

        Deque<Long> q = new ArrayDeque<>();
        q.add(0L);
        while (!q.isEmpty()) {
            long x = q.poll();
            if (x > LIMIT) {
                return -1;
            }
            if (x > k && x % k == 0) {
                return (int) x;
            }
            // never append a leading zero (x == 0 means nothing built yet),
            // otherwise 0 -> 0 loops forever
            if (x > 0 || lo != 0) {
                q.add(x * 10 + lo);
            }
            if (lo != hi && (x > 0 || hi != 0)) {
                q.add(x * 10 + hi);
            }
        }
        return -1;
    }
}
