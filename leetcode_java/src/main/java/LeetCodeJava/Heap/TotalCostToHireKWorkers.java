package LeetCodeJava.Heap;

// https://leetcode.com/problems/total-cost-to-hire-k-workers/

import java.util.PriorityQueue;

/**
 *  2462. Total Cost to Hire K Workers
 *  Medium
 *
 *  You are given a 0-indexed integer array costs where costs[i] is the cost of
 *  hiring the ith worker.
 *
 *  You are also given two integers k and candidates. We want to hire exactly k
 *  workers according to the following rules:
 *
 *   - You will run k sessions and hire exactly one worker in each session.
 *   - In each hiring session, choose the worker with the lowest cost from either the
 *     first candidates workers or the last candidates workers. Break the tie by the
 *     smallest index.
 *   - If there are fewer than candidates workers remaining, choose the worker with
 *     the lowest cost among them (tie broken by smallest index).
 *   - A worker can only be chosen once.
 *
 *  Return the total cost to hire exactly k workers.
 *
 *  Example 1:
 *    Input: costs = [17,12,10,2,7,2,11,20,8], k = 3, candidates = 4
 *    Output: 11
 *    Explanation: hire 2 (index 3), then 2 (index 5), then 7 -> 2 + 2 + 7 = 11.
 *
 *  Example 2:
 *    Input: costs = [1,2,4,1], k = 3, candidates = 3
 *    Output: 4
 *
 *  Constraints:
 *    1 <= costs.length <= 10^5
 *    1 <= costs[i] <= 10^5
 *    1 <= k, candidates <= costs.length
 */
public class TotalCostToHireKWorkers {

    // V0
    // IDEA: TWO MIN-HEAPS (ONE PER END) REFILLED FROM THE SHRINKING MIDDLE
    //       the candidate pool is always "the first `candidates` remaining" plus
    //       "the last `candidates` remaining", so keep a heap per side and two
    //       pointers marking the untouched middle.
    //       each round takes the cheaper of the two tops; TIES GO TO THE HEAD heap,
    //       which reproduces the "smallest index" rule.
    //       after a pop that side is topped up from the middle — since the pointers
    //       never cross, a worker sitting in BOTH windows is counted only once.
    /**
     * time = O((k + candidates) log candidates)
     * space = O(candidates)
     */
    public long totalCost(int[] costs, int k, int candidates) {
        int n = costs.length;
        int left = 0;
        int right = n - 1;

        PriorityQueue<Integer> head = new PriorityQueue<>();
        while (left <= right && head.size() < candidates) {
            head.add(costs[left++]);
        }

        PriorityQueue<Integer> tail = new PriorityQueue<>();
        while (left <= right && tail.size() < candidates) {
            tail.add(costs[right--]);
        }

        long total = 0L;
        for (int i = 0; i < k; i++) {
            if (!tail.isEmpty() && (head.isEmpty() || tail.peek() < head.peek())) {
                total += tail.poll();
                if (left <= right) {
                    tail.add(costs[right--]);
                }
            } else {
                total += head.poll();
                if (left <= right) {
                    head.add(costs[left++]);
                }
            }
        }
        return total;
    }
}
