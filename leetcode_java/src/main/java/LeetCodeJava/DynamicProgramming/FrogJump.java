package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/frog-jump/description/

import java.util.*;

/**
 * 403. Frog Jump
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * A frog is crossing a river. The river is divided into some number of units, and at each unit, there may or may not exist a stone. The frog can jump on a stone, but it must not jump into the water.
 *
 * Given a list of stones positions (in units) in sorted ascending order, determine if the frog can cross the river by landing on the last stone. Initially, the frog is on the first stone and assumes the first jump must be 1 unit.
 *
 * If the frog's last jump was k units, its next jump must be either k - 1, k, or k + 1 units. The frog can only jump in the forward direction.
 *
 *
 *
 * Example 1:
 *
 * Input: stones = [0,1,3,5,6,8,12,17]
 * Output: true
 * Explanation: The frog can jump to the last stone by jumping 1 unit to the 2nd stone, then 2 units to the 3rd stone, then 2 units to the 4th stone, then 3 units to the 6th stone, 4 units to the 7th stone, and 5 units to the 8th stone.
 * Example 2:
 *
 * Input: stones = [0,1,2,3,4,8,9,11]
 * Output: false
 * Explanation: There is no way to jump to the last stone as the gap between the 5th and 6th stone is too large.
 *
 *
 * Constraints:
 *
 * 2 <= stones.length <= 2000
 * 0 <= stones[i] <= 231 - 1
 * stones[0] == 0
 * stones is sorted in a strictly increasing order.
 *
 */
public class FrogJump {

    // V0
//    public boolean canCross(int[] stones) {
//
//    }

    // V0-1
    // IDEA: DP (gemini)
    public boolean canCross_0_1(int[] stones) {
        // Map: Stone Position -> Set of jump sizes that can be made FROM this stone
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int stone : stones) {
            map.put(stone, new HashSet<>());
        }

        // Base case: At the first stone (position 0), the only allowed first jump is 1
        map.get(0).add(1);

        for (int i = 0; i < stones.length; i++) {
            int currentStone = stones[i];
            Set<Integer> jumps = map.get(currentStone);

            for (int k : jumps) {
                int nextPosition = currentStone + k;

                // If we reached the last stone
                if (nextPosition == stones[stones.length - 1]) {
                    return true;
                }

                // If there is a stone at the next position
                if (map.containsKey(nextPosition)) {
                    // From the next stone, we can jump k-1, k, or k+1
                    if (k - 1 > 0)
                        map.get(nextPosition).add(k - 1);
                    map.get(nextPosition).add(k);
                    map.get(nextPosition).add(k + 1);
                }
            }
        }

        return false;
    }

    // V0-2
    // IDEA: DP (gpt)
    public boolean canCross_0_2(int[] stones) {
        int n = stones.length;

        // Quick edge-check: first jump must be 1
        if (n == 0 || stones[1] - stones[0] != 1) {
            return false;
        }

        // Map each position to the set of jump sizes that reach it
        Map<Integer, Set<Integer>> dp = new HashMap<>();

        for (int stone : stones) {
            dp.put(stone, new HashSet<>());
        }

        // Start at stones[0] with jump 0
        dp.get(stones[0]).add(0);

        for (int stone : stones) {
            Set<Integer> jumps = dp.get(stone);
            for (int jump : jumps) {

                // Next possible jumps: k - 1, k, k + 1
                for (int step = jump - 1; step <= jump + 1; step++) {
                    if (step > 0) {
                        int nextPos = stone + step;
                        if (dp.containsKey(nextPos)) {
                            dp.get(nextPos).add(step);
                        }
                    }
                }
            }
        }

        // If last stone has any reachable jumps, return true
        return !dp.get(stones[n - 1]).isEmpty();
    }



    // V1
    // IDEA: DP
    // https://leetcode.com/problems/frog-jump/solutions/6280324/video-solution-by-niits-br29/
    public boolean canCross_1(int[] stones) {
        Map<Integer, Set<Integer>> dp = new HashMap<>();
        for (int stone : stones) {
            dp.put(stone, new HashSet<>());
        }
        dp.get(0).add(0);

        for (int stone : stones) {
            for (int jump : dp.get(stone)) {
                for (int jumpDistance : new int[] { jump - 1, jump, jump + 1 }) {
                    if (jumpDistance > 0 && dp.containsKey(stone + jumpDistance)) {
                        dp.get(stone + jumpDistance).add(jumpDistance);
                    }
                }
            }
        }

        return !dp.get(stones[stones.length - 1]).isEmpty();
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/frog-jump/solutions/3965107/dp-beginners-friendly-easy-to-understand-7egp/
    HashMap<Integer, Integer> m = new HashMap<>();
    int[][] dp;

    boolean solve(int i, int k, int[] stones) {
        if (i == stones.length - 1) {
            return true;
        }

        if (dp[i][k] != -1) {
            return dp[i][k] == 1;
        }

        boolean k0 = false, kp = false, k1 = false;

        if (m.containsKey(stones[i] + k)) {
            k0 = solve(m.get(stones[i] + k), k, stones);
        }
        if (k > 1 && m.containsKey(stones[i] + k - 1)) {
            kp = solve(m.get(stones[i] + k - 1), k - 1, stones);
        }
        if (m.containsKey(stones[i] + k + 1)) {
            k1 = solve(m.get(stones[i] + k + 1), k + 1, stones);
        }

        dp[i][k] = (k0 || kp || k1) ? 1 : 0;
        return dp[i][k] == 1;
    }

    public boolean canCross_2(int[] stones) {
        if (stones[1] - stones[0] != 1) {
            return false;
        }

        for (int i = 0; i < stones.length; i++) {
            m.put(stones[i], i);
        }

        dp = new int[stones.length][stones.length];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        return solve(1, 1, stones);
    }

    // V3
    // IDEA:DP
    // https://leetcode.com/problems/frog-jump/solutions/3964889/simple-java-solution-beginner-friendly-e-k03j/
    public boolean canCross_3(int[] stones) {
        int n = stones.length;
        boolean[][] dp = new boolean[n][n + 1];
        dp[0][1] = true;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int jump = stones[i] - stones[j];

                if (jump <= j + 1) {
                    dp[i][jump] = dp[j][jump - 1] || dp[j][jump] || dp[j][jump + 1];
                    if (i == n - 1 && dp[i][jump])
                        return true;
                }
            }
        }
        return false;
    }



}
