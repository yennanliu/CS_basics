package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-cost-to-hire-k-workers/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
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


    // V1
    // IDEA: BRUTE FORCE -- try every worker as the RATE SETTER
    /**
     *  Fix worker r as the one whose ratio sets the group rate. Everyone with a
     *  ratio <= r's is eligible; take the k SMALLEST qualities among them.
     *
     *  O(n^2 log n), so it TLEs at n = 10^4, but it needs no sorting insight and no
     *  heap -- it is the definition of the problem written out, and the oracle the
     *  greedy versions are checked against.
     *
     *  time  = O(n^2 log n)
     *  space = O(n)
     */
    public double mincostToHireWorkers_1(int[] quality, int[] wage, int k) {
        int n = quality.length;
        double ans = Double.MAX_VALUE;

        for (int r = 0; r < n; r++) {
            double rate = (double) wage[r] / quality[r];

            List<Integer> eligible = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((double) wage[i] / quality[i] <= rate) {
                    eligible.add(quality[i]);
                }
            }
            if (eligible.size() < k) {
                continue;
            }

            Collections.sort(eligible);
            long sum = 0;
            for (int i = 0; i < k; i++) {
                sum += eligible.get(i);
            }
            ans = Math.min(ans, rate * sum);
        }

        return ans;
    }

    // V2
    // IDEA: SORT BY RATIO + TreeMap MULTISET OF QUALITIES
    /**
     *  Same greedy as V0, but the `k cheapest qualities so far` set is a
     *  TreeMap<quality, count> instead of a max-heap.
     *
     *  The multiset can also report the MEDIAN or any order statistic of the
     *  current group, which the heap cannot -- useful when the follow-up asks for
     *  more than the sum.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public double mincostToHireWorkers_2(int[] quality, int[] wage, int k) {
        int n = quality.length;

        double[][] workers = new double[n][2];
        for (int i = 0; i < n; i++) {
            workers[i][0] = (double) wage[i] / quality[i];
            workers[i][1] = quality[i];
        }
        Arrays.sort(workers, Comparator.comparingDouble(w -> w[0]));

        TreeMap<Integer, Integer> group = new TreeMap<>(); // quality -> count
        int groupSize = 0;
        long totalQuality = 0;
        double ans = Double.MAX_VALUE;

        for (double[] w : workers) {
            int q = (int) w[1];
            group.merge(q, 1, Integer::sum);
            groupSize += 1;
            totalQuality += q;

            if (groupSize > k) {
                // evict the LARGEST quality
                int worst = group.lastKey();
                if (group.merge(worst, -1, Integer::sum) == 0) {
                    group.remove(worst);
                }
                groupSize -= 1;
                totalQuality -= worst;
            }

            if (groupSize == k) {
                ans = Math.min(ans, w[0] * totalQuality);
            }
        }

        return ans;
    }

    // V3
    // IDEA: SORT BY RATIO + FIXED-SIZE ARRAY MAINTAINED BY INSERTION
    /**
     *  For small k an explicit sorted array of size k beats both a heap and a
     *  balanced tree: insertion is a memmove over at most k slots with no object
     *  allocation and perfect cache locality.
     *
     *  Asymptotically worse (O(n k)), practically faster whenever k is small --
     *  the classic `small k` specialisation.
     *
     *  time  = O(n log n + n * k)
     *  space = O(k)
     */
    public double mincostToHireWorkers_3(int[] quality, int[] wage, int k) {
        int n = quality.length;

        double[][] workers = new double[n][2];
        for (int i = 0; i < n; i++) {
            workers[i][0] = (double) wage[i] / quality[i];
            workers[i][1] = quality[i];
        }
        Arrays.sort(workers, Comparator.comparingDouble(w -> w[0]));

        int[] top = new int[k]; // the k smallest qualities, ascending
        int size = 0;
        long totalQuality = 0;
        double ans = Double.MAX_VALUE;

        for (double[] w : workers) {
            int q = (int) w[1];

            if (size < k) {
                int pos = size++;
                while (pos > 0 && top[pos - 1] > q) {
                    top[pos] = top[pos - 1];
                    pos -= 1;
                }
                top[pos] = q;
                totalQuality += q;
            } else if (q < top[k - 1]) {
                totalQuality -= top[k - 1];
                int pos = k - 1;
                while (pos > 0 && top[pos - 1] > q) {
                    top[pos] = top[pos - 1];
                    pos -= 1;
                }
                top[pos] = q;
                totalQuality += q;
            }

            if (size == k) {
                ans = Math.min(ans, w[0] * totalQuality);
            }
        }

        return ans;
    }

}
