package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-cost-to-hire-k-workers/description/

import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 857. Minimum Cost to Hire K Workers
 * Hard
 *
 * There are n workers. You are given two integer arrays quality and wage where
 * quality[i] is the quality of the ith worker and wage[i] is the minimum wage
 * expectation for the ith worker.
 *
 * We want to hire exactly k workers to form a paid group. To hire a group of k
 * workers, we must pay them according to the following rules:
 *
 * 1. Every worker in the paid group must be paid at least their minimum wage expectation.
 * 2. In the group, each worker's pay must be directly proportional to their quality.
 *    This means if a worker's quality is double that of another worker in the group,
 *    then they must be paid twice as much as the other worker.
 *
 * Given the integer k, return the least amount of money needed to form a paid group
 * satisfying the above conditions. Answers within 10^-5 of the actual answer will be
 * accepted.
 *
 *
 * Example 1:
 *
 * Input: quality = [10,20,5], wage = [70,50,30], k = 2
 * Output: 105.00000
 * Explanation: We pay 70 to 0th worker and 35 to 2nd worker.
 *
 * Example 2:
 *
 * Input: quality = [3,1,10,10,1], wage = [4,8,2,2,7], k = 3
 * Output: 30.66667
 * Explanation: We pay 4 to 0th worker, 13.33333 to 2nd and 3rd workers separately.
 *
 *
 * Constraints:
 *
 * n == quality.length == wage.length
 * 1 <= k <= n <= 10^4
 * 1 <= quality[i], wage[i] <= 10^4
 *
 */
public class MinimumCostToHireKWorkers {

    // V0
    // IDEA: GREEDY (sort by wage/quality ratio) + MAX HEAP on quality
    /**
     *   Pay must be PROPORTIONAL to quality, so the group is paid
     *       rate * quality[i]   for every hired worker i
     *   and `rate` must satisfy rate >= wage[i] / quality[i] for ALL of them,
     *   i.e. rate = MAX ratio inside the group. Total cost = rate * sum(quality).
     *
     *   So: SORT workers by ratio ASCENDING and walk through them. When worker r
     *   is the LAST one considered, its ratio IS the group rate, and we want the
     *   k-1 CHEAPEST qualities among the workers before it.
     *
     *   -> keep a MAX-heap of size k over quality, always evicting the largest.
     *
     *   time  = O(n * log(n))
     *   space = O(n)
     */
    public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;

        // {wage/quality ratio, quality}
        double[][] workers = new double[n][2];
        for (int i = 0; i < n; i++) {
            workers[i][0] = (double) wage[i] / (double) quality[i];
            workers[i][1] = quality[i];
        }
        java.util.Arrays.sort(workers, Comparator.comparingDouble(a -> a[0]));

        /** NOTE !!!
         *
         *  MAX-heap of qualities -> so we can evict the WORST (largest) quality
         *  once the group grows past k
         */
        PriorityQueue<Integer> heap = new PriorityQueue<>(Collections.reverseOrder());

        long totalQuality = 0;
        double ans = Double.MAX_VALUE;

        for (double[] w : workers) {
            double ratio = w[0];
            int q = (int) w[1];

            heap.add(q);
            totalQuality += q;

            if (heap.size() > k) {
                totalQuality -= heap.poll();
            }

            if (heap.size() == k) {
                /** NOTE !!!
                 *
                 *  because the list is sorted by ratio, `ratio` here is the
                 *  LARGEST ratio in the current group -> it IS the rate
                 */
                ans = Math.min(ans, ratio * totalQuality);
            }
        }

        return ans;
    }

}
