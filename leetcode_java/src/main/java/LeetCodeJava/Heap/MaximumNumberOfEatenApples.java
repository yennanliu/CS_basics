package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-number-of-eaten-apples/description/

import java.util.PriorityQueue;

/**
 * 1705. Maximum Number of Eaten Apples
 * Medium
 *
 * There is a special kind of apple tree that grows apples every day for n days. On
 * the ith day, the tree grows apples[i] apples that will rot after days[i] days,
 * that is on day i + days[i] the apples will be rotten and cannot be eaten. On some
 * days, the apple tree does not grow any apples, which are denoted by
 * apples[i] == 0 and days[i] == 0.
 *
 * You decided to eat at most one apple a day (to keep the doctors away). Note that
 * you can keep eating after the first n days.
 *
 * Given two integer arrays days and apples of length n, return the maximum number
 * of apples you can eat.
 *
 *
 * Example 1:
 *
 * Input: apples = [1,2,3,5,2], days = [3,2,1,4,2]
 * Output: 7
 *
 * Example 2:
 *
 * Input: apples = [3,0,0,0,0,2], days = [3,0,0,0,0,2]
 * Output: 5
 *
 *
 * Constraints:
 *
 * n == apples.length == days.length
 * 1 <= n <= 2 * 10^4
 * 0 <= apples[i], days[i] <= 2 * 10^4
 * days[i] = 0 if and only if apples[i] = 0.
 *
 */
public class MaximumNumberOfEatenApples {

    // V0
    // IDEA: GREEDY + MIN HEAP (ALWAYS EAT FROM THE BATCH THAT ROTS SOONEST)
    //       heap entry = {lastEdibleDay, remaining}. Each day: add today's batch,
    //       drop rotten batches, eat one apple from the earliest-expiring batch.
    //       Keep going after day n while batches remain.
    /**
     * time = O((N + D) log N)   # D = max(days)
     * space = O(N)
     */
    public int eatenApples(int[] apples, int[] days) {
        int n = apples.length;
        // ordered by last edible day, earliest first
        PriorityQueue<int[]> heap = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));
        int eaten = 0;
        int day = 0;

        while (day < n || !heap.isEmpty()) {
            // apples grown today rot ON day + days[day], so the last edible day is one before
            if (day < n && apples[day] > 0) {
                heap.offer(new int[] {day + days[day] - 1, apples[day]});
            }

            // throw away batches that are already rotten
            while (!heap.isEmpty() && heap.peek()[0] < day) {
                heap.poll();
            }

            if (!heap.isEmpty()) {
                int[] batch = heap.poll();
                eaten++;
                batch[1]--;
                // put the rest back only if it can still be eaten on a later day
                if (batch[1] > 0 && batch[0] > day) {
                    heap.offer(batch);
                }
            }
            day++;
        }
        return eaten;
    }
}
