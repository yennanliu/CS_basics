package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-energy-boost-from-two-drinks/description/

/**
 * 3259. Maximum Energy Boost From Two Drinks
 * Medium
 *
 * You are given two integer arrays energyDrinkA and energyDrinkB of the same length n by a futuristic sports scientist. These arrays represent the energy boosts per hour provided by two different energy drinks, A and B, respectively.
 *
 * You want to maximize your total energy boost by drinking one energy drink per hour. However, if you want to switch from consuming one energy drink to the other, you need to wait for one hour to cleanse your system (meaning you won't get any energy boost in that hour).
 *
 * Return the maximum total energy boost you can gain in the next n hours.
 *
 * Note that you can start consuming either of the two energy drinks.
 *
 *
 * Example 1:
 *
 * Input: energyDrinkA = [1,3,1], energyDrinkB = [3,1,1]
 * Output: 5
 * Explanation:
 * To gain an energy boost of 5, drink only the energy drink A (or only B).
 *
 * Example 2:
 *
 * Input: energyDrinkA = [4,1,1], energyDrinkB = [1,1,3]
 * Output: 7
 * Explanation:
 * To gain an energy boost of 7:
 * Drink the energy drink A for the first hour.
 * Switch to the energy drink B and we lose the energy boost of the second hour.
 * Gain the energy boost of the drink B in the third hour.
 *
 *
 * Constraints:
 *
 * n == energyDrinkA.length == energyDrinkB.length
 * 3 <= n <= 10^5
 * 1 <= energyDrinkA[i], energyDrinkB[i] <= 10^5
 *
 */
public class MaximumEnergyBoostFromTwoDrinks {

    // V0
    // IDEA: TWO ROLLING DP CHAINS, ONE PER DRINK
    /**
     *  DP def:
     *     endA[i] = best total that ENDS with drink A at hour i
     *     endB[i] = best total that ENDS with drink B at hour i
     *
     *  DP eq:
     *     endA[i] = A[i] + max(endA[i-1],    // keep drinking A
     *                          endB[i-2])    // switch from B: the cleanse costs hour i-1
     *     endB[i] = B[i] + max(endB[i-1], endA[i-2])
     *
     *  Only the previous two values of each chain are read -> 4 variables.
     *
     *  Init:   endA[0] = A[0], endB[0] = B[0], endA[-1] = endB[-1] = 0
     *  Answer: max(endA[n-1], endB[n-1])
     *
     *  time = O(N)
     *  space = O(1)
     */
    public long maxEnergyBoost(int[] energyDrinkA, int[] energyDrinkB) {
        int n = energyDrinkA.length;

        // values two hours back (hour -1 contributes nothing)
        long endATwoBack = 0;
        long endBTwoBack = 0;
        // values one hour back
        long endAPrev = energyDrinkA[0];
        long endBPrev = energyDrinkB[0];

        for (int i = 1; i < n; i++) {
            long endACur = energyDrinkA[i] + Math.max(endAPrev, endBTwoBack);
            long endBCur = energyDrinkB[i] + Math.max(endBPrev, endATwoBack);

            // slide the two-hour window forward
            endATwoBack = endAPrev;
            endBTwoBack = endBPrev;
            endAPrev = endACur;
            endBPrev = endBCur;
        }

        return Math.max(endAPrev, endBPrev);
    }
}
