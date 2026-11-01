package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/taking-maximum-energy-from-the-mystic-dungeon/description/

/**
 * 3147. Taking Maximum Energy From the Mystic Dungeon
 * Medium
 *
 * In a mystic dungeon, n magicians are standing in a line. Each magician has an attribute that gives you energy. Some magicians can give you negative energy, which means taking energy from you.
 *
 * You have been cursed in such a way that after absorbing energy from magician i, you will be instantly transported to magician (i + k). This process will be repeated until you reach the magician where (i + k) does not exist.
 *
 * In other words, you will choose a starting point and then teleport with k jumps until you reach the end of the magicians' sequence, absorbing all the energy during the journey.
 *
 * You are given an array energy and an integer k. Return the maximum possible energy you can gain.
 *
 *
 * Example 1:
 *
 * Input: energy = [5,2,-10,-5,1], k = 3
 * Output: 3
 * Explanation: We can gain a total energy of 3 by starting from magician 1 absorbing 2 + 1 = 3.
 *
 * Example 2:
 *
 * Input: energy = [-2,-3,-1], k = 2
 * Output: -1
 * Explanation: We can gain a total energy of -1 by starting from magician 2.
 *
 *
 * Constraints:
 *
 * 1 <= energy.length <= 10^5
 * -1000 <= energy[i] <= 1000
 * 1 <= k <= energy.length - 1
 *
 */
public class TakingMaximumEnergyFromTheMysticDungeon {

    // V0
    // IDEA: SUFFIX SUMS ALONG EACH JUMP CHAIN, BUILT FROM THE BACK
    //   From start i the path is forced: i, i+k, i+2k, ... so
    //   total[i] = energy[i] + total[i + k]   (or just energy[i] when i + k is past the end).
    //   Every index is a legal start, so the answer is the max total.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumEnergy(int[] energy, int k) {
        int n = energy.length;
        int[] total = new int[n];
        int best = Integer.MIN_VALUE;
        for (int i = n - 1; i >= 0; i--) {
            total[i] = energy[i];
            if (i + k < n) {
                total[i] += total[i + k];
            }
            best = Math.max(best, total[i]);
        }
        return best;
    }
}
