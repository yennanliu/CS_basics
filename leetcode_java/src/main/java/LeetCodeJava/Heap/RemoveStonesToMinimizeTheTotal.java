package LeetCodeJava.Heap;

// https://leetcode.com/problems/remove-stones-to-minimize-the-total/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  1962. Remove Stones to Minimize the Total
 *  Medium
 *
 *  You are given a 0-indexed integer array piles, where piles[i] represents the
 *  number of stones in the ith pile, and an integer k. You should apply the
 *  following operation exactly k times:
 *
 *   - Choose any piles[i] and remove floor(piles[i] / 2) stones from it.
 *
 *  Notice that you can apply the operation on the same pile more than once.
 *
 *  Return the minimum possible total number of stones remaining after applying the
 *  k operations.
 *
 *  Example 1:
 *    Input: piles = [5,4,9], k = 2
 *    Output: 12
 *    Explanation: [5,4,9] -> [5,4,5] -> [3,4,5], total = 12.
 *
 *  Example 2:
 *    Input: piles = [4,3,6,7], k = 3
 *    Output: 12
 *
 *  Constraints:
 *    1 <= piles.length <= 10^5
 *    1 <= piles[i] <= 10^4
 *    1 <= k <= 10^5
 */
public class RemoveStonesToMinimizeTheTotal {

    // V0
    // IDEA: GREEDY + MAX HEAP
    //       one operation on a pile of size x removes floor(x/2), which is monotone
    //       in x -> always hit the CURRENT largest pile. (exchange argument: if an
    //       optimal plan halves a smaller pile while a larger one is available,
    //       swapping the targets never removes fewer stones.)
    //       after halving, the pile keeps x - x/2 = ceil(x/2) stones.
    /**
     * time = O(n + k log n)
     * space = O(n)
     */
    public int minStoneSum(int[] piles, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int total = 0;
        for (int x : piles) {
            pq.add(x);
            total += x;
        }

        for (int i = 0; i < k; i++) {
            int x = pq.poll();
            total -= x / 2;
            pq.add(x - x / 2);
        }
        return total;
    }
}
