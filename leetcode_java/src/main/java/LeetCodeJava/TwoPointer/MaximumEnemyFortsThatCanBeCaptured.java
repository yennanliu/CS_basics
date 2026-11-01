package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/maximum-enemy-forts-that-can-be-captured/description/

/**
 * 2511. Maximum Enemy Forts That Can Be Captured
 * Easy
 *
 * You are given a 0-indexed integer array forts of length n representing the positions of several forts. forts[i] can be -1, 0, or 1 where:
 *
 * -1 represents there is no fort at the ith position.
 * 0 indicates there is an enemy fort at the ith position.
 * 1 indicates the fort at the ith the position is under your command.
 *
 * Now you have decided to move your army from one of your forts at position i to an empty position j such that:
 *
 * 0 <= i, j <= n - 1
 * The army travels over enemy forts only. Formally, for all k where min(i,j) < k < max(i,j), forts[k] == 0.
 *
 * While moving the army, all the enemy forts that come in the way are captured.
 *
 * Return the maximum number of enemy forts that can be captured. In case it is impossible to move your army, or you do not have any fort under your command, return 0.
 *
 *
 * Example 1:
 *
 * Input: forts = [1,0,0,-1,0,0,0,0,1]
 * Output: 4
 * Explanation:
 * - Moving the army from position 0 to position 3 captures 2 enemy forts, at 1 and 2.
 * - Moving the army from position 8 to position 3 captures 4 enemy forts.
 * Since 4 is the maximum number of enemy forts that can be captured, we return 4.
 *
 * Example 2:
 *
 * Input: forts = [0,0,1,-1]
 * Output: 0
 * Explanation: Since no enemy fort can be captured, 0 is returned.
 *
 *
 * Constraints:
 *
 * 1 <= forts.length <= 1000
 * -1 <= forts[i] <= 1
 *
 */
public class MaximumEnemyFortsThatCanBeCaptured {

    // V0
    // IDEA: TWO POINTERS BETWEEN CONSECUTIVE NON-ZERO FORTS
    //  the army only crosses a run of 0s, and that run must be bounded by a 1 on one side and
    //  a -1 on the other (either order). keep `prev` = index of the last non-zero fort; at each
    //  new non-zero fort i, if the two have opposite signs the i - prev - 1 forts between them
    //  are all 0 and can be captured.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int captureForts(int[] forts) {
        int best = 0;
        // index of the last fort that is not 0, or -1 before the first one
        int prev = -1;
        for (int i = 0; i < forts.length; i++) {
            if (forts[i] == 0) {
                continue;
            }
            // one side is our fort (1), the other is empty (-1)
            if (prev != -1 && forts[prev] == -forts[i]) {
                best = Math.max(best, i - prev - 1);
            }
            prev = i;
        }
        return best;
    }
}
