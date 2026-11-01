package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-time-to-transport-all-individuals/description/

import java.util.*;

/**
 * 3594. Minimum Time to Transport All Individuals
 * Hard
 *
 * You are given n individuals at a base camp who need to cross a river to reach a
 * destination using a single boat. The boat can carry at most k people at a time.
 * The trip is affected by environmental conditions that vary cyclically over m
 * stages.
 *
 * Each stage j has a speed multiplier mul[j]:
 *
 * If mul[j] > 1, the trip slows down.
 * If mul[j] < 1, the trip speeds up.
 *
 * Each individual i has a rowing strength represented by time[i], the time (in
 * minutes) it takes them to cross alone in neutral conditions.
 *
 * Rules:
 *
 * A group g departing at stage j takes time equal to the maximum time[i] among
 * its members, multiplied by mul[j] minutes to reach the destination.
 * After the group crosses the river in time d, the stage advances by
 * floor(d) % m steps.
 * If individuals are left behind, one person must return with the boat. Let r be
 * the index of the returning person, the return takes time[r] x mul[current_stage],
 * defined as return_time, and the stage advances by floor(return_time) % m steps.
 *
 * Return the minimum total time required to transport all individuals. If it is
 * not possible to transport all individuals to the destination, return -1.
 *
 *
 * Example 1:
 *
 * Input: n = 1, k = 1, m = 2, time = [5], mul = [1.0,1.3]
 * Output: 5.00000
 * Explanation:
 * Individual 0 departs from stage 0, so crossing time = 5 x 1.00 = 5.00 minutes.
 * All team members are now at the destination. Thus, the total time taken is
 * 5.00 minutes.
 *
 * Example 2:
 *
 * Input: n = 3, k = 2, m = 3, time = [2,5,8], mul = [1.0,1.5,0.75]
 * Output: 14.50000
 * Explanation:
 * The optimal strategy is:
 * Send individuals 0 and 2 from the base camp to the destination from stage 0.
 * The crossing time is max(2, 8) x mul[0] = 8 x 1.00 = 8.00 minutes. The stage
 * advances by floor(8.00) % 3 = 2, so the next stage is (0 + 2) % 3 = 2.
 * Individual 0 returns alone from the destination to the base camp from stage 2.
 * The return time is 2 x mul[2] = 2 x 0.75 = 1.50 minutes. The stage advances by
 * floor(1.50) % 3 = 1, so the next stage is (2 + 1) % 3 = 0.
 * Send individuals 0 and 1 from the base camp to the destination from stage 0.
 * The crossing time is max(2, 5) x mul[0] = 5 x 1.00 = 5.00 minutes. The stage
 * advances by floor(5.00) % 3 = 2, so the final stage is (0 + 2) % 3 = 2.
 * All team members are now at the destination. The total time taken is
 * 8.00 + 1.50 + 5.00 = 14.50 minutes.
 *
 * Example 3:
 *
 * Input: n = 2, k = 1, m = 2, time = [10,10], mul = [2.0,2.0]
 * Output: -1.00000
 * Explanation:
 * Since the boat can only carry one person at a time, it is impossible to
 * transport both individuals as one must always return. Thus, the answer is
 * -1.00.
 *
 *
 * Constraints:
 *
 * 1 <= n == time.length <= 12
 * 1 <= k <= 5
 * 1 <= m <= 5
 * 1 <= time[i] <= 100
 * m == mul.length
 * 0.5 <= mul[i] <= 2.0
 *
 */
public class MinimumTimeToTransportAllIndividuals {

    // V0
    // IDEA: DIJKSTRA OVER (CAMP BITMASK, STAGE, BOAT SIDE)
    //  - the future depends only on who is still at camp, the current stage, and the boat side
    //    -> 2^n * m * 2 states
    //  - crossing (boat at camp): pick a group of <= k people from the camp; cost = max time
    //    in the group * mul[stage]; stage advances by floor(cost) mod m
    //  - return (boat across): one person already across rows back alone
    //  - all costs > 0, so the first time "camp empty, boat across" is popped it is optimal;
    //    never popped -> -1
    /**
     * time = O(3^n * m * log(2^n * m))
     * space = O(3^n)
     */
    public double minTime(int n, int k, int m, int[] time, double[] mul) {
        int size = 1 << n;
        int full = size - 1;

        // groupMax[mask] = slowest person in mask; built by removing the lowest set bit
        int[] groupMax = new int[size];
        for (int mask = 1; mask < size; mask++) {
            int lowest = Integer.numberOfTrailingZeros(mask);
            int rest = mask & (mask - 1);   // mask without its lowest set bit
            groupMax[mask] = Math.max(time[lowest], groupMax[rest]);
        }

        // state id = (mask * m + stage) * 2 + side, side 0 = boat at camp, 1 = boat across
        double[] best = new double[size * m * 2];
        Arrays.fill(best, Double.MAX_VALUE);
        int startState = encode(full, 0, 0, m);
        best[startState] = 0.0;

        // heap entry: {time so far, state id}
        PriorityQueue<double[]> heap = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));
        heap.offer(new double[] {0.0, startState});
        while (!heap.isEmpty()) {
            double[] top = heap.poll();
            double elapsed = top[0];
            int state = (int) top[1];
            if (elapsed > best[state]) {
                continue;   // stale entry
            }
            int side = state % 2;
            int stage = (state / 2) % m;
            int campMask = (state / 2) / m;

            // goal: nobody left at camp and the boat on the far side
            if (side == 1 && campMask == 0) {
                return elapsed;
            }

            if (side == 0) {
                // send any non-empty group of at most k people still at camp
                for (int group = campMask; group > 0; group = (group - 1) & campMask) {
                    if (Integer.bitCount(group) > k) {
                        continue;
                    }
                    double cost = groupMax[group] * mul[stage];
                    int nextStage = (stage + (int) Math.floor(cost)) % m;
                    int next = encode(campMask ^ group, nextStage, 1, m);
                    relax(heap, best, next, elapsed + cost);
                }
            } else {
                // one person who is already across rows the boat back
                int across = full ^ campMask;
                for (int person = 0; person < n; person++) {
                    if ((across & (1 << person)) == 0) {
                        continue;
                    }
                    double cost = time[person] * mul[stage];
                    int nextStage = (stage + (int) Math.floor(cost)) % m;
                    int next = encode(campMask | (1 << person), nextStage, 0, m);
                    relax(heap, best, next, elapsed + cost);
                }
            }
        }
        return -1.0;
    }

    /** Packs (camp mask, stage, boat side) into one state id. */
    private int encode(int campMask, int stage, int side, int m) {
        return (campMask * m + stage) * 2 + side;
    }

    /** Pushes the state if the new time improves on the best known one. */
    private void relax(PriorityQueue<double[]> heap, double[] best, int state, double candidate) {
        if (candidate < best[state]) {
            best[state] = candidate;
            heap.offer(new double[] {candidate, state});
        }
    }
}
