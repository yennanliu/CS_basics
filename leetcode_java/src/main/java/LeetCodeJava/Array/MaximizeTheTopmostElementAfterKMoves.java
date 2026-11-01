package LeetCodeJava.Array;

// https://leetcode.com/problems/maximize-the-topmost-element-after-k-moves/description/

/**
 * 2202. Maximize the Topmost Element After K Moves
 * Medium
 *
 * You are given a 0-indexed integer array nums representing the contents of a pile, where nums[0] is the topmost element of the pile.
 *
 * In one move, you can perform either of the following:
 *
 * If the pile is not empty, remove the topmost element of the pile.
 * If there are one or more removed elements, add any one of them back onto the pile. This element becomes the new topmost element.
 *
 * You are also given an integer k, which denotes the total number of moves to be made.
 *
 * Return the maximum value of the topmost element of the pile possible after exactly k moves. In case it is not possible to obtain a non-empty pile after k moves, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,2,2,4,0,6], k = 4
 * Output: 5
 * Explanation:
 * One of the ways we can end with 5 at the top of the pile after 4 moves is as follows:
 * - Step 1: Remove the topmost element = 5. The pile becomes [2,2,4,0,6].
 * - Step 2: Remove the topmost element = 2. The pile becomes [2,4,0,6].
 * - Step 3: Remove the topmost element = 2. The pile becomes [4,0,6].
 * - Step 4: Add 5 back onto the pile. The pile becomes [5,4,0,6].
 * Note that this is not the only way to end with 5 at the top of the pile. It can be shown that 5 is the largest answer possible after 4 moves.
 *
 * Example 2:
 *
 * Input: nums = [2], k = 1
 * Output: -1
 * Explanation:
 * In the first move, our only option is to pop the topmost element of the pile.
 * Since it is not possible to obtain a non-empty pile after one move, we return -1.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 0 <= nums[i], k <= 10^9
 *
 */
public class MaximizeTheTopmostElementAfterKMoves {

    // V0
    // IDEA: CASE ANALYSIS ON WHICH INDICES k MOVES CAN BRING TO THE TOP
    /**
     *  After exactly k moves the top can only be:
     *   - some nums[i] with i <= k - 2: pop i + 1 elements, burn spare moves,
     *     and push nums[i] back as the last move
     *   - nums[k]: pop exactly k elements (needs k < n)
     *  nums[k - 1] is never reachable: after popping k - 1 elements one move is
     *  left, and it either pops nums[k - 1] or pushes something on top of it.
     *  Special cases: k == 0 (nothing changes), a single-element pile (it only
     *  alternates between [nums[0]] and empty), k == 1 (only nums[1]).
     *
     * time = O(N)
     * space = O(1)
     */
    public int maximumTop(int[] nums, int k) {
        int n = nums.length;
        if (k == 0) {
            return nums[0];
        }
        if (n == 1) {
            // even k: pop then push back, pairwise; odd k: the pile ends empty
            return k % 2 == 0 ? nums[0] : -1;
        }
        if (k == 1) {
            return nums[1];
        }

        int best = -1;
        // indices 0 .. k-2, clipped to the array
        int lastReachable = Math.min(k - 2, n - 1);
        for (int i = 0; i <= lastReachable; i++) {
            best = Math.max(best, nums[i]);
        }
        if (k < n) {
            best = Math.max(best, nums[k]);
        }
        return best;
    }
}
