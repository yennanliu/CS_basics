package LeetCodeJava.DFS;

// https://leetcode.com/problems/can-i-win/description/

import java.util.HashMap;
import java.util.Map;

/**
 *  464. Can I Win
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * In the "100 game" two players take turns adding, to a running total, any integer from 1 to 10. The player who first causes the running total to reach or exceed 100 wins.
 *
 * What if we change the game so that players cannot re-use integers?
 *
 * For example, two players might take turns drawing from a common pool of numbers from 1 to 15 without replacement until they reach a total >= 100.
 *
 * Given two integers maxChoosableInteger and desiredTotal, return true if the first player to move can force a win, otherwise, return false. Assume both players play optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: maxChoosableInteger = 10, desiredTotal = 11
 * Output: false
 * Explanation:
 * No matter which integer the first player choose, the first player will lose.
 * The first player can choose an integer from 1 up to 10.
 * If the first player choose 1, the second player can only choose integers from 2 up to 10.
 * The second player will win by choosing 10 and get a total = 11, which is >= desiredTotal.
 * Same with other integers chosen by the first player, the second player will always win.
 * Example 2:
 *
 * Input: maxChoosableInteger = 10, desiredTotal = 0
 * Output: true
 * Example 3:
 *
 * Input: maxChoosableInteger = 10, desiredTotal = 1
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= maxChoosableInteger <= 20
 * 0 <= desiredTotal <= 300
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 */
public class CanIWin {

    // V0
//    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
//
//    }

    // V1

    // V2

    // V3
    // IDEA: DP
    // https://leetcode.com/problems/can-i-win/solutions/3780152/thoroughly-commented-with-clear-variable-1oiw/
    public boolean canIWin_3(int maxChoosableInteger, int desiredTotal) {
        // First of all, we check if it's even possible for our players to reach the disiredTotal
        int totalPossibleSum = maxChoosableInteger * (maxChoosableInteger + 1) / 2;
        if (totalPossibleSum < desiredTotal)
            return false;

        // Declare a map for memoization, where the key is the bitmask of our current state (0 - number is available for use, 1 - number was taken)
        Map<Integer, Boolean> memo = new HashMap<>();
        return dp(desiredTotal, 0, maxChoosableInteger, memo);
    }

    private boolean dp(int goal, int state, int maxChoosable, Map<Integer, Boolean> memo) {
        // If we have already calculated a result for this state, then return it
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        // Declare the result variable
        boolean result = false;

        // Iterate over all possible integers from 1 to maxChoosable
        for (int i = 1; i <= maxChoosable; i++) {

            // Then we have to check our state (bitmask) to see if our current integer (i) was used or not
            boolean isAvailable = (state >> i) % 2 == 0;

            // If it was used, then we keep looking for an unused integer
            if (!isAvailable) {
                continue;
            }

            // We check our win conditions. If we reach the goal, our result is true, and we can jump to our last lines.
            if (goal - i <= 0) {
                result = true;
                break;
            }

            // We need to create a new state (bitmask) to mark our current integer as used
            int currMask = 1 << i;
            int newState = state | currMask;

            // And we pass the turn to our rival
            boolean rivalResult = dp(goal - i, newState, maxChoosable, memo);

            // In case our rival doesn't win, it means that it's possible for us to beat the rival
            if (!rivalResult) {
                result = true;
                break;
            }
        }

        // We save our result for the current state and return it
        memo.put(state, result);
        return result;
    }


    // V4
    // https://leetcode.com/problems/can-i-win/solutions/6153051/100-beat-easy-explanation-c-dpbitmasking-6mqz/
    public boolean canIWin_4(int maxChoosableInteger, int desiredTotal) {
        if (desiredTotal == 0) {
            return true;
        }
        if ((maxChoosableInteger * (maxChoosableInteger + 1)) / 2 < desiredTotal) {
            return false;
        }

        Map<Integer, Boolean> memo = new HashMap<>();
        return canIWinHelper(maxChoosableInteger, desiredTotal, 0, memo);
    }

    private boolean canIWinHelper(int maxChoosableInteger, int desiredTotal, int usedNumbers,
                                  Map<Integer, Boolean> memo) {
        if (memo.containsKey(usedNumbers)) {
            return memo.get(usedNumbers);
        }

        for (int i = 1; i <= maxChoosableInteger; i++) {
            int currentBit = 1 << i;
            if ((usedNumbers & currentBit) == 0) { // Check if the number i is available
                if (desiredTotal - i <= 0 ||
                        !canIWinHelper(maxChoosableInteger, desiredTotal - i, usedNumbers | currentBit, memo)) {
                    memo.put(usedNumbers, true);
                    return true;
                }
            }
        }

        memo.put(usedNumbers, false);
        return false;
    }




}
