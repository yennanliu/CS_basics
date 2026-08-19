package LeetCodeJava.Heap;

// https://leetcode.com/problems/take-gifts-from-the-richest-pile/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  2558. Take Gifts From the Richest Pile
 *  Easy
 *
 *  You are given an integer array gifts denoting the number of gifts in various
 *  piles. Every second, you do the following:
 *
 *   - Choose the pile with the maximum number of gifts.
 *   - If there is more than one such pile, choose any.
 *   - Reduce the number of gifts in the pile to the floor of the square root of the
 *     original number of gifts in the pile.
 *
 *  Return the number of gifts remaining after k seconds.
 *
 *  Example 1:
 *    Input: gifts = [25,64,9,4,100], k = 4
 *    Output: 29
 *    Explanation: 100 -> 10, 64 -> 8, 25 -> 5, 10 -> 3, leaving [5,8,9,4,3] = 29.
 *
 *  Example 2:
 *    Input: gifts = [1,1,1,1], k = 4
 *    Output: 4
 *
 *  Constraints:
 *    1 <= gifts.length <= 10^3
 *    1 <= gifts[i] <= 10^9
 *    1 <= k <= 10^3
 */
public class TakeGiftsFromTheRichestPile {

    // V0
    // IDEA: MAX HEAP (SIMULATION)
    //       k times : pop the biggest pile, push back floor(sqrt(x)).
    //       NOTE: (int) Math.sqrt(x) can be off by one around perfect squares for
    //             x up to 10^9, so the result is corrected explicitly.
    //       NOTE: a pile of 1 stays 1 forever, so running all k rounds is safe.
    /**
     * time = O(n + k log n)
     * space = O(n)
     */
    public long pickGifts(int[] gifts, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int x : gifts) {
            pq.add(x);
        }

        for (int i = 0; i < k; i++) {
            int top = pq.poll();
            pq.add(isqrt(top));
        }

        long res = 0L;
        while (!pq.isEmpty()) {
            res += pq.poll();
        }
        return res;
    }

    // exact integer floor(sqrt(x)) for x >= 0
    private int isqrt(int x) {
        int r = (int) Math.sqrt((double) x);
        while ((long) r * r > x) {
            r--;
        }
        while ((long) (r + 1) * (r + 1) <= x) {
            r++;
        }
        return r;
    }
}
