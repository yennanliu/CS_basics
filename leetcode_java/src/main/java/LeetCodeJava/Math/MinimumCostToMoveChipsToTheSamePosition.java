package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-cost-to-move-chips-to-the-same-position/description/

/**
 * 1217. Minimum Cost to Move Chips to The Same Position
 * Easy
 *
 * (this problem was originally titled "Play with Chips")
 *
 * We have n chips, where the position of the ith chip is position[i].
 *
 * We need to move all the chips to the same position. In one step, we can change the position of
 * the ith chip from position[i] to:
 *
 * position[i] + 2 or position[i] - 2 with cost = 0.
 * position[i] + 1 or position[i] - 1 with cost = 1.
 *
 * Return the minimum cost needed to move all the chips to the same position.
 *
 *
 * Example 1:
 *
 * Input: position = [1,2,3]
 * Output: 1
 * Explanation: First step: Move the chip at position 3 to position 1 with cost = 0.
 * Second step: Move the chip at position 2 to position 1 with cost = 1.
 * Total cost is 1.
 *
 * Example 2:
 *
 * Input: position = [2,2,2,3,3]
 * Output: 2
 * Explanation: We can move the two chips at position 3 to position 2. Each move has cost = 1.
 * The total cost = 2.
 *
 * Example 3:
 *
 * Input: position = [1,1000000000]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= position.length <= 100
 * 1 <= position[i] <= 10^9
 *
 */
public class MinimumCostToMoveChipsToTheSamePosition {

    // V0
    // IDEA: PARITY (GREEDY)
    //   moving by 2 is free, so every chip reaches any position of the same parity for free.
    //   collapse all even chips onto one spot and all odd chips onto a neighbouring spot;
    //   the only real cost is moving the smaller group by 1, one coin per chip.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minCostToMoveChips(int[] position) {
        int oddCount = 0;
        for (int p : position) {
            if (p % 2 == 1) {
                oddCount++;
            }
        }
        int evenCount = position.length - oddCount;
        return Math.min(oddCount, evenCount);
    }
}
