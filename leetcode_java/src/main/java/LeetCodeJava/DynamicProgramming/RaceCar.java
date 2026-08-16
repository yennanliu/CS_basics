package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/race-car/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 818. Race Car
 * Hard
 *
 * Your car starts at position 0 and speed +1 on an infinite number line. Your car can go
 * into negative positions. Your car drives automatically according to a sequence of
 * instructions 'A' (accelerate) and 'R' (reverse):
 *
 * When you get an instruction 'A', your car does the following:
 *     position += speed
 *     speed *= 2
 * When you get an instruction 'R', your car does the following:
 *     If your speed is positive then speed = -1
 *     otherwise speed = 1
 *     Your position stays the same.
 *
 * For example, after commands "AAR", your car goes to positions 0 --> 1 --> 3 --> 3, and
 * your speed goes to 1 --> 2 --> 4 --> -1.
 *
 * Given a target position target, return the length of the shortest sequence of
 * instructions to get there.
 *
 * Example 1:
 *
 * Input: target = 3
 * Output: 2
 * Explanation:
 * The shortest instruction sequence is "AA".
 * Your position goes from 0 --> 1 --> 3.
 *
 * Example 2:
 *
 * Input: target = 6
 * Output: 5
 * Explanation:
 * The shortest instruction sequence is "AAARA".
 * Your position goes from 0 --> 1 --> 3 --> 7 --> 7 --> 6.
 *
 * Constraints:
 *
 * 1 <= target <= 10^4
 *
 */
public class RaceCar {

    // V0
    // IDEA: 1D DP
    /**
     *  DP def:
     *     - dp[i] = shortest instruction length to reach position i
     *               (starting at 0 with speed +1, ending anywhere)
     *
     *  For i, let k = bitLength(i), so 2^(k-1) <= i < 2^k.
     *  After j consecutive 'A' the car sits at 2^j - 1 with speed 2^j.
     *
     *  THREE shapes of optimal answer:
     *
     *   1) i == 2^k - 1                  -> exactly k 'A's        : dp[i] = k
     *
     *   2) OVERSHOOT: run k 'A's to 2^k - 1 (> i), then 'R', then solve the
     *      remaining distance (2^k - 1 - i) MIRRORED:
     *          dp[i] = dp[2^k - 1 - i] + k + 1
     *
     *   3) UNDERSHOOT: run k-1 'A's to 2^(k-1) - 1 (<= i), 'R', back up j 'A's
     *      (moving 2^j - 1 backwards), 'R' again, then solve the rest forward:
     *          dp[i] = dp[i - (2^(k-1) - 2^j)] + (k - 1) + j + 2
     *      for j = 0 .. k-2
     *
     *  time  = O(target * log(target))
     *  space = O(target)
     */
    public int racecar(int target) {
        int[] dp = new int[target + 1];

        for (int i = 1; i <= target; i++) {
            int k = 32 - Integer.numberOfLeadingZeros(i); // 2^(k-1) <= i < 2^k

            // case 1: i is EXACTLY 2^k - 1
            if (i == (1 << k) - 1) {
                dp[i] = k;
                continue;
            }

            // case 2: OVERSHOOT past i, reverse, cover the leftover
            dp[i] = dp[(1 << k) - 1 - i] + k + 1;

            // case 3: stop SHORT, reverse, back up, reverse again
            for (int j = 0; j < k - 1; j++) {
                int rest = i - ((1 << (k - 1)) - (1 << j));
                dp[i] = Math.min(dp[i], dp[rest] + (k - 1) + j + 2);
            }
        }

        return dp[target];
    }

    // V0-1
    // IDEA: BFS over (position, speed) states
    /**
     *   Every instruction is ONE edge, so plain BFS gives the shortest sequence.
     *
     *   NOTE !!! PRUNING is essential: never wander further than `target` away from
     *            the target, otherwise the state space is INFINITE.
     *
     *   time  = O(target * log(target))
     *   space = O(target * log(target))
     */
    public int racecar_0_1(int target) {
        Deque<int[]> queue = new ArrayDeque<>(); // {position, speed}
        queue.offer(new int[] { 0, 1 });

        Set<String> visited = new HashSet<>();
        visited.add("0,1");

        int steps = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                int[] cur = queue.poll();
                int pos = cur[0];
                int speed = cur[1];

                if (pos == target) {
                    return steps;
                }

                int[][] nxts = {
                        { pos + speed, speed * 2 },            // 'A'
                        { pos, speed > 0 ? -1 : 1 }            // 'R'
                };

                for (int[] state : nxts) {
                    // PRUNE states that ran too far past the target
                    if (Math.abs(state[0] - target) <= target) {
                        String key = state[0] + "," + state[1];
                        if (visited.add(key)) {
                            queue.offer(state);
                        }
                    }
                }
            }
            steps += 1;
        }

        return -1;
    }


    // V1
    // IDEA: BIDIRECTIONAL-STYLE DP with an explicit `overshoot then come back` bound
    /**
     *  Same three cases as V0, but the undershoot branch is bounded by
     *  `j < k - 1` derived from the position rather than looped blindly, and the
     *  table is filled with an explicit `reachable so far` guard.
     *
     *  Kept because it makes the two reversal points visible as concrete positions
     *  instead of as index arithmetic.
     *
     *  time  = O(target * log(target))
     *  space = O(target)
     */
    public int racecar_1(int target) {
        int[] dp = new int[target + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;

        for (int i = 1; i <= target; i++) {
            int k = 1;
            while ((1 << k) - 1 < i) {
                k += 1;
            }
            if ((1 << k) - 1 == i) {
                dp[i] = k;               // exactly 2^k - 1 -> k accelerations
                continue;
            }
            // OVERSHOOT: k accelerations past i, reverse, solve the surplus
            dp[i] = Math.min(dp[i], dp[(1 << k) - 1 - i] + k + 1);
            // UNDERSHOOT: k-1 accelerations, reverse, back up j, reverse again
            for (int j = 0; j < k - 1; j++) {
                int back = (1 << j) - 1;
                int rest = i - ((1 << (k - 1)) - 1) + back;
                if (rest >= 0 && rest <= target) {
                    dp[i] = Math.min(dp[i], dp[rest] + (k - 1) + 1 + j + 1);
                }
            }
        }
        return dp[target];
    }

    // V2
    // IDEA: DIJKSTRA over (position, speed)
    /**
     *  Same state graph as the BFS in V0-1, but explored with a priority queue.
     *
     *  Redundant while every instruction costs 1 -- yet it is the version that
     *  keeps working if 'A' and 'R' were ever priced differently, which the BFS
     *  layer argument could not survive.
     *
     *  time  = O(target log target)
     *  space = O(target)
     */
    public int racecar_2(int target) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        pq.add(new int[] { 0, 0, 1 });
        Map<Long, Integer> best = new HashMap<>();
        best.put(key(0, 1), 0);

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0];
            int pos = cur[1];
            int speed = cur[2];

            if (pos == target) {
                return cost;
            }
            if (cost > best.getOrDefault(key(pos, speed), Integer.MAX_VALUE)) {
                continue;
            }

            int[][] nxts = {
                    { pos + speed, speed * 2 },
                    { pos, speed > 0 ? -1 : 1 }
            };
            for (int[] st : nxts) {
                if (Math.abs(st[0] - target) > target) {
                    continue;             // pruned: too far past the target
                }
                long k = key(st[0], st[1]);
                if (cost + 1 < best.getOrDefault(k, Integer.MAX_VALUE)) {
                    best.put(k, cost + 1);
                    pq.add(new int[] { cost + 1, st[0], st[1] });
                }
            }
        }
        return -1;
    }

    private long key(int pos, int speed) {
        return (long) (pos + 20000) * 100000L + (speed + 50000);
    }

    // V3
    // IDEA: MEMOISED RECURSION on the remaining distance
    /**
     *  solve(t) with the same overshoot / undershoot split, expressed top-down.
     *
     *  Only the distances actually reachable are computed, so on a target whose
     *  optimal play uses few reversals it touches far fewer states than the full
     *  sweep.
     *
     *  time  = O(target log target)
     *  space = O(target)
     */
    private Map<Integer, Integer> memoRace;

    public int racecar_3(int target) {
        memoRace = new HashMap<>();
        return solveRace(target);
    }

    private int solveRace(int t) {
        if (t == 0) {
            return 0;
        }
        Integer cached = memoRace.get(t);
        if (cached != null) {
            return cached;
        }

        int k = 32 - Integer.numberOfLeadingZeros(t);
        int res;
        if (t == (1 << k) - 1) {
            res = k;
        } else {
            res = solveRace((1 << k) - 1 - t) + k + 1;         // overshoot
            for (int j = 0; j < k - 1; j++) {
                int rest = t - (1 << (k - 1)) + (1 << j);
                res = Math.min(res, solveRace(rest) + (k - 1) + j + 2);
            }
        }
        memoRace.put(t, res);
        return res;
    }

}
