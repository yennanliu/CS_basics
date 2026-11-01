package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-cost-to-set-cooking-time/description/

/**
 * 2162. Minimum Cost to Set Cooking Time
 * Medium
 *
 * A generic microwave supports cooking times for:
 *
 * at least 1 second.
 * at most 99 minutes and 99 seconds.
 *
 * To set the cooking time, you push at most four digits. The microwave normalizes what you push as four digits by prepending zeroes. It interprets the first two digits as the minutes and the last two digits as the seconds. It then adds them up as the cooking time. For example,
 *
 * You push 9 5 4 (three digits). It is normalized as 0954 and interpreted as 9 minutes and 54 seconds.
 * You push 0 0 0 8 (four digits). It is interpreted as 0 minutes and 8 seconds.
 * You push 8 0 9 0. It is interpreted as 80 minutes and 90 seconds.
 * You push 8 1 3 0. It is interpreted as 81 minutes and 30 seconds.
 *
 * You are given integers startAt, moveCost, pushCost, and targetSeconds. Initially, your finger is on the digit startAt. Moving the finger above any specific digit costs moveCost units of fatigue. Touching the digit you are already on costs nothing (0 units of fatigue) to move, and pushCost units of fatigue to push.
 *
 * There can be multiple ways to set the microwave to cook for targetSeconds seconds but you are interested in the way with the minimum cost.
 *
 * Return the minimum cost to set targetSeconds seconds of cooking time.
 *
 * Remember that one minute consists of 60 seconds.
 *
 *
 * Example 1:
 *
 * Input: startAt = 1, moveCost = 2, pushCost = 1, targetSeconds = 600
 * Output: 6
 * Explanation: The following are the possible ways to set the cooking time.
 * - 1 0 0 0, interpreted as 10 minutes and 0 seconds.
 *   The finger is already on digit 1, pushes 1 (with cost 1), moves to 0 (with cost 2), pushes 0 (with cost 1), pushes 0 (with cost 1), and pushes 0 (with cost 1).
 *   The cost is: 1 + 2 + 1 + 1 + 1 = 6. This is the minimum cost.
 * - 0 9 6 0, interpreted as 9 minutes and 60 seconds. That is also 600 seconds.
 *   The finger moves to 0 (with cost 2), pushes 0 (with cost 1), moves to 9 (with cost 2), pushes 9 (with cost 1), moves to 6 (with cost 2), pushes 6 (with cost 1), moves to 0 (with cost 2), and pushes 0 (with cost 1).
 *   The cost is: 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1 = 12.
 * - 9 6 0, normalized as 0960 and interpreted as 9 minutes and 60 seconds.
 *   The finger moves to 9 (with cost 2), pushes 9 (with cost 1), moves to 6 (with cost 2), pushes 6 (with cost 1), moves to 0 (with cost 2), pushes 0 (with cost 1), and pushes 0 (with cost 1).
 *   The cost is: 2 + 1 + 2 + 1 + 2 + 1 + 1 = 10.
 *
 * Example 2:
 *
 * Input: startAt = 0, moveCost = 1, pushCost = 2, targetSeconds = 76
 * Output: 6
 * Explanation: The optimal way is to push two digits: 7 6, interpreted as 76 seconds.
 * The finger moves to 7 (with cost 1), pushes 7 (with cost 2), moves to 6 (with cost 1), and pushes 6 (with cost 2). The total cost is: 1 + 2 + 1 + 2 = 6
 * Note other possible ways are 0076, 076, 0116, and 116, but none of them produces the minimum cost.
 *
 *
 * Constraints:
 *
 * 0 <= startAt <= 9
 * 1 <= moveCost, pushCost <= 10^5
 * 1 <= targetSeconds <= 6039
 *
 */
public class MinimumCostToSetCookingTime {

    // V0
    // IDEA: ONLY TWO (minutes, seconds) ENCODINGS EXIST -- COST BOTH
    /**
     *  targetSeconds = 60 * minutes + seconds with 0 <= minutes, seconds <= 99
     *  has at most two solutions:
     *    the natural one : minutes = t / 60,     seconds = t % 60
     *    the borrowed one: minutes = t / 60 - 1, seconds = t % 60 + 60
     *  For each, type the 4 digits without leading zeros (the microwave adds them
     *  for free) and charge pushCost per digit plus moveCost per finger move.
     *
     * time = O(1)
     * space = O(1)
     */
    public int minCostSetTime(int startAt, int moveCost, int pushCost, int targetSeconds) {
        int minutes = targetSeconds / 60;
        int seconds = targetSeconds % 60;
        int natural = typingCost(minutes, seconds, startAt, moveCost, pushCost);
        int borrowed = typingCost(minutes - 1, seconds + 60, startAt, moveCost, pushCost);
        return Math.min(natural, borrowed);
    }

    /** Cost to type minutes:seconds, or Integer.MAX_VALUE if it is not a valid display. */
    private int typingCost(int minutes, int seconds, int startAt, int moveCost, int pushCost) {
        if (minutes < 0 || minutes > 99 || seconds < 0 || seconds > 99) {
            return Integer.MAX_VALUE;
        }
        int[] digits = {minutes / 10, minutes % 10, seconds / 10, seconds % 10};

        // skip leading zeros: they are prepended for free
        int first = 0;
        while (first < 3 && digits[first] == 0) {
            first++;
        }

        int cost = 0;
        int finger = startAt;
        for (int i = first; i < 4; i++) {
            if (digits[i] != finger) {
                cost += moveCost;
                finger = digits[i];
            }
            cost += pushCost;
        }
        return cost;
    }
}
