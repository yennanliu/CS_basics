package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/delivering-boxes-from-storage-to-ports/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1687. Delivering Boxes from Storage to Ports
 * Hard
 *
 * You have the task of delivering some boxes from storage to their ports using only one ship. However,
 * this ship has a limit on the number of boxes and the total weight that it can carry.
 *
 * You are given an array boxes, where boxes[i] = [portsi, weighti], and three integers portsCount,
 * maxBoxes, and maxWeight.
 *
 * - portsi is the port where you need to deliver the ith box and weightsi is the weight of the ith box.
 * - portsCount is the number of ports.
 * - maxBoxes and maxWeight are the respective box and weight limits of the ship.
 *
 * The boxes need to be delivered in the order they are given. The ship will follow these steps:
 *
 * - The ship will take some number of boxes from the boxes queue, not violating the maxBoxes and
 *   maxWeight constraints.
 * - For each loaded box in order, the ship will make a trip to the port the box needs to be delivered
 *   to and deliver it. If the ship is already at the correct port, no trip is needed, and the box can
 *   immediately be delivered.
 * - The ship then makes a return trip to storage to take more boxes from the queue.
 *
 * The ship must end at storage after all the boxes have been delivered.
 *
 * Return the minimum number of trips the ship needs to make to deliver all boxes to their respective
 * ports.
 *
 *
 * Example 1:
 *
 * Input: boxes = [[1,1],[2,1],[1,1]], portsCount = 2, maxBoxes = 3, maxWeight = 3
 * Output: 4
 * Explanation: The optimal strategy is as follows:
 * - The ship takes all the boxes in the queue, goes to port 1, then port 2, then port 1 again, then
 *   returns to storage. 4 trips.
 * So the total number of trips is 4.
 * Note that the first and third boxes cannot be delivered together because the boxes need to be
 * delivered in order (i.e. the second box needs to be delivered at port 2 before the third box).
 *
 * Example 2:
 *
 * Input: boxes = [[1,2],[3,3],[3,1],[3,1],[2,4]], portsCount = 3, maxBoxes = 3, maxWeight = 6
 * Output: 6
 * Explanation: The optimal strategy is as follows:
 * - The ship takes the first box, goes to port 1, then returns to storage. 2 trips.
 * - The ship takes the second, third and fourth boxes, goes to port 3, then returns to storage. 2 trips.
 * - The ship takes the fifth box, goes to port 2, then returns to storage. 2 trips.
 * So the total number of trips is 2 + 2 + 2 = 6.
 *
 * Example 3:
 *
 * Input: boxes = [[1,4],[1,2],[2,1],[2,1],[3,2],[3,4]], portsCount = 3, maxBoxes = 6, maxWeight = 7
 * Output: 6
 * Explanation: The optimal strategy is as follows:
 * - The ship takes the first and second boxes, goes to port 1, then returns to storage. 2 trips.
 * - The ship takes the third and fourth boxes, goes to port 2, then returns to storage. 2 trips.
 * - The ship takes the fifth and sixth boxes, goes to port 3, then returns to storage. 2 trips.
 * So the total number of trips is 2 + 2 + 2 = 6.
 *
 *
 * Constraints:
 *
 * 1 <= boxes.length <= 10^5
 * 1 <= portsCount, maxBoxes, maxWeight <= 10^5
 * 1 <= portsi <= portsCount
 * 1 <= weightsi <= maxWeight
 *
 */
public class DeliveringBoxesFromStorageToPorts {

    // V0
    // IDEA: DP + SLIDING-WINDOW MINIMUM (monotonic deque)
    //       boxes go in order, so a plan is a split of the array into trips.
    //       a trip carrying boxes [j, i) costs
    //         1 (leave storage) + (#port changes inside it) + 1 (return)
    //         = changes[i-1] - changes[j] + 2
    //       with changes[t] = number of t' <= t where ports[t'] != ports[t'-1].
    //       dp[i] = min trips to deliver the first i boxes
    //             = changes[i-1] + 2 + min over feasible j of (dp[j] - changes[j])
    //       j is feasible when i - j <= maxBoxes and weight(j..i) <= maxWeight;
    //       both lower bounds only move right as i grows, so a monotonic deque of
    //       j's with increasing key (dp[j] - changes[j]) gives the min in O(1).
    //       the deque is never empty: j = i-1 always fits (one box is allowed).
    //       (the plain O(N * maxBoxes) DP over all j times out.)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int boxDelivering(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;

        // prefixWeight[i] = total weight of the first i boxes
        long[] prefixWeight = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixWeight[i + 1] = prefixWeight[i] + boxes[i][1];
        }

        // changes[t] = port changes among boxes[0..t]
        int[] changes = new int[n];
        for (int t = 1; t < n; t++) {
            changes[t] = changes[t - 1];
            if (boxes[t][0] != boxes[t - 1][0]) {
                changes[t]++;
            }
        }

        int[] dp = new int[n + 1];
        // key[j] = dp[j] - changes[j], the part of the cost that depends on j
        int[] key = new int[n + 1];
        dp[0] = 0;
        key[0] = 0;

        Deque<Integer> candidates = new ArrayDeque<>();
        candidates.offerLast(0);

        for (int i = 1; i <= n; i++) {
            // drop starts that would carry too many boxes or too much weight
            while (!candidates.isEmpty()
                    && (candidates.peekFirst() < i - maxBoxes
                        || prefixWeight[i] - prefixWeight[candidates.peekFirst()] > maxWeight)) {
                candidates.pollFirst();
            }

            int bestStart = candidates.peekFirst();
            dp[i] = key[bestStart] + changes[i - 1] + 2;

            if (i < n) {
                key[i] = dp[i] - changes[i];
                // keep keys increasing from front to back
                while (!candidates.isEmpty() && key[candidates.peekLast()] >= key[i]) {
                    candidates.pollLast();
                }
                candidates.offerLast(i);
            }
        }
        return dp[n];
    }
}
