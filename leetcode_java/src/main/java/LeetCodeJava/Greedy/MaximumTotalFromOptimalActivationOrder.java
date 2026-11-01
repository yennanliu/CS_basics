package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-total-from-optimal-activation-order/description/

import java.util.*;

/**
 * 3645. Maximum Total from Optimal Activation Order
 * Medium
 *
 * You are given two integer arrays value and limit, both of length n.
 *
 * Initially, all elements are inactive. You may activate them in any order.
 *
 * To activate an inactive element at index i, the number of currently active
 * elements must be strictly less than limit[i].
 * When you activate the element at index i, it adds value[i] to the total
 * activation value (i.e., the sum of value[i] for all elements that have
 * undergone activation operations).
 * After each activation, if the number of currently active elements becomes
 * x, then all elements j with limit[j] <= x become permanently inactive, even
 * if they are already active.
 *
 * Return the maximum total you can obtain by choosing the activation order
 * optimally.
 *
 *
 * Example 1:
 *
 * Input: value = [3,5,8], limit = [2,1,3]
 * Output: 16
 * Explanation:
 * One optimal activation order is:
 * Step 1: activate i = 1 (value 5). Active count goes 0 -> 1, so j = 1 dies
 *         because limit[1] = 1. Total = 5.
 * Step 2: activate i = 0 (value 3). Active count goes 0 -> 1, nothing new
 *         dies. Total = 8.
 * Step 3: activate i = 2 (value 8). Active count goes 1 -> 2, so j = 0 dies
 *         because limit[0] = 2. Total = 16.
 * Thus, the maximum possible total is 16.
 *
 * Example 2:
 *
 * Input: value = [4,2,6], limit = [1,1,1]
 * Output: 6
 * Explanation:
 * One optimal activation order is:
 * Step 1: activate i = 2 (value 6). Active count goes 0 -> 1, so j = 0, 1, 2
 *         all die because limit[j] = 1. Total = 6.
 * Thus, the maximum possible total is 6.
 *
 * Example 3:
 *
 * Input: value = [4,1,5,2], limit = [3,3,2,3]
 * Output: 12
 * Explanation:
 * One optimal activation order is:
 * Step 1: activate i = 2 (value 5). Active 0 -> 1, nothing dies. Total = 5.
 * Step 2: activate i = 0 (value 4). Active 1 -> 2, so j = 2 dies because
 *         limit[2] = 2. Total = 9.
 * Step 3: activate i = 1 (value 1). Active 1 -> 2, nothing dies. Total = 10.
 * Step 4: activate i = 3 (value 2). Active 2 -> 3, so j = 0, 1, 3 die
 *         because limit[j] = 3. Total = 12.
 * Thus, the maximum possible total is 12.
 *
 *
 * Constraints:
 *
 * 1 <= n == value.length == limit.length <= 10^5
 * 1 <= value[i] <= 10^5
 * 1 <= limit[i] <= n
 *
 */
public class MaximumTotalFromOptimalActivationOrder {

    // V0
    // IDEA: BUCKET BY LIMIT, KEEP THE `limit` LARGEST VALUES OF EACH BUCKET
    //       at most L elements with limit == L can ever be activated (the active count
    //       reaching L kills them all), and the caps for different L do not interact.
    //       values are positive, so each bucket simply keeps its L largest.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maxTotal(int[] value, int[] limit) {
        Map<Integer, List<Integer>> buckets = new HashMap<>();
        for (int i = 0; i < value.length; i++) {
            buckets.computeIfAbsent(limit[i], key -> new ArrayList<>()).add(value[i]);
        }

        long total = 0;
        for (Map.Entry<Integer, List<Integer>> entry : buckets.entrySet()) {
            int cap = entry.getKey();
            List<Integer> values = entry.getValue();
            values.sort(Collections.reverseOrder());
            int take = Math.min(cap, values.size());
            for (int i = 0; i < take; i++) {
                total += values.get(i);
            }
        }
        return total;
    }
}
