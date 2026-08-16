package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/race-car/description/

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

}
