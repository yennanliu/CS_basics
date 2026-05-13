package LeetCodeJava.Array;

// https://leetcode.com/problems/watering-plants/description/
/**
 *  2079. Watering Plants
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You want to water n plants in your garden with a watering can. The plants are arranged in a row and are labeled from 0 to n - 1 from left to right where the ith plant is located at x = i. There is a river at x = -1 that you can refill your watering can at.
 *
 * Each plant needs a specific amount of water. You will water the plants in the following way:
 *
 * Water the plants in order from left to right.
 * After watering the current plant, if you do not have enough water to completely water the next plant, return to the river to fully refill the watering can.
 * You cannot refill the watering can early.
 * You are initially at the river (i.e., x = -1). It takes one step to move one unit on the x-axis.
 *
 * Given a 0-indexed integer array plants of n integers, where plants[i] is the amount of water the ith plant needs, and an integer capacity representing the watering can capacity, return the number of steps needed to water all the plants.
 *
 *
 *
 * Example 1:
 *
 * Input: plants = [2,2,3,3], capacity = 5
 * Output: 14
 * Explanation: Start at the river with a full watering can:
 * - Walk to plant 0 (1 step) and water it. Watering can has 3 units of water.
 * - Walk to plant 1 (1 step) and water it. Watering can has 1 unit of water.
 * - Since you cannot completely water plant 2, walk back to the river to refill (2 steps).
 * - Walk to plant 2 (3 steps) and water it. Watering can has 2 units of water.
 * - Since you cannot completely water plant 3, walk back to the river to refill (3 steps).
 * - Walk to plant 3 (4 steps) and water it.
 * Steps needed = 1 + 1 + 2 + 3 + 3 + 4 = 14.
 * Example 2:
 *
 * Input: plants = [1,1,1,4,2,3], capacity = 4
 * Output: 30
 * Explanation: Start at the river with a full watering can:
 * - Water plants 0, 1, and 2 (3 steps). Return to river (3 steps).
 * - Water plant 3 (4 steps). Return to river (4 steps).
 * - Water plant 4 (5 steps). Return to river (5 steps).
 * - Water plant 5 (6 steps).
 * Steps needed = 3 + 3 + 4 + 4 + 5 + 5 + 6 = 30.
 * Example 3:
 *
 * Input: plants = [7,7,7,7,7,7,7], capacity = 8
 * Output: 49
 * Explanation: You have to refill before watering each plant.
 * Steps needed = 1 + 1 + 2 + 2 + 3 + 3 + 4 + 4 + 5 + 5 + 6 + 6 + 7 = 49.
 *
 *
 * Constraints:
 *
 * n == plants.length
 * 1 <= n <= 1000
 * 1 <= plants[i] <= 106
 * max(plants[i]) <= capacity <= 109
 *
 */
public class WateringPlants {

    // V0
//    public int wateringPlants(int[] plants, int capacity) {
//
//    }


    // V1


    // V2
    // https://leetcode.com/problems/watering-plants/solutions/1589049/simulation-by-votrubac-7tv6/
    public int wateringPlants_2(int[] p, int capacity) {
        int res = 0, can = capacity;
        for (int i = 0; i < p.length; ++i) {
            if (can < p[i]) {
                res += i * 2;
                can = capacity;
            }
            can -= p[i];
        }
        return res + p.length;
    }



    // V3
    // https://leetcode.com/problems/watering-plants/solutions/7623068/easy-java-solution-beats-100-by-tanmay96-d3fh/
    public int wateringPlants_3(int[] plants, int capacity) {
        int iValue = capacity;
        int iTotal = 0;

        for (int i = 0; i < plants.length; i++) {
            if (iValue < plants[i]) {
                iTotal = iTotal + (i); //Backward to fill
                iValue = capacity - plants[i];
                iTotal = iTotal + (i + 1);
            } else {
                iTotal++;
                iValue = iValue - plants[i];
            }
        }
        return iTotal;
    }



    // V4
    // https://leetcode.com/problems/watering-plants/solutions/6361749/java-best-solution-by-aimlc_22b1531167-6iif/
    public int wateringPlants_4(int[] plants, int capacity) {
        int l = plants.length;
        int c = 0;
        int d = capacity;
        for (int i = 0; i < l; i++) {
            if (plants[i] <= capacity) {
                capacity -= plants[i];
                c++;
            } else {
                capacity = d - plants[i];
                c += 2 * i + 1;
            }
        }
        return c;
    }






}
