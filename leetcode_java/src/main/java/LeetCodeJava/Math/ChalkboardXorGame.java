package LeetCodeJava.Math;

// https://leetcode.com/problems/chalkboard-xor-game/description/
/**
 * 810. Chalkboard XOR Game
 * Hard
 *
 * You are given an array of integers nums represents the numbers written on a chalkboard.
 *
 * Alice and Bob take turns erasing exactly one number from the chalkboard,
 * with Alice starting first. If erasing a number causes the bitwise XOR of all the
 * elements of the chalkboard to become 0, then that player loses. The bitwise XOR of
 * one element is that element itself, and the bitwise XOR of no elements is 0.
 *
 * Also, if any player starts their turn with the bitwise XOR of all the elements of
 * the chalkboard equal to 0, then that player wins.
 *
 * Return true if and only if Alice wins the game, assuming both players play optimally.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2]
 * Output: false
 * Explanation:
 * Alice has two choices: erase 1 or erase 2.
 * If she erases 1, the nums array becomes [1, 2]. The bitwise XOR of all the elements
 * of the chalkboard is 1 XOR 2 = 3. Now Bob can remove any element he wants, because
 * Alice will be the one to erase the last element and she will lose.
 * If Alice erases 2 first, now nums become [1, 1]. The bitwise XOR of all the elements
 * of the chalkboard is 1 XOR 1 = 0. Alice will lose.
 *
 * Example 2:
 *
 * Input: nums = [0,1]
 * Output: true
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] < 2^16
 *
 */
public class ChalkboardXorGame {

    // V0
    // IDEA: GAME THEORY / BRAINTEASER (bit counting proof)
    /**
     *   Alice wins iff  XOR(nums) == 0  OR  nums.length is EVEN.
     *
     *   WHY:
     *     - If XOR(nums) == 0 at the start, Alice wins IMMEDIATELY by the rules.
     *
     *     - Otherwise let S = XOR(nums) != 0 and n = nums.length.
     *       Alice loses only if EVERY move leaves XOR 0, i.e. S ^ nums[i] == 0
     *       for all i -> every nums[i] == S.
     *       Then S = XOR of n copies of S. For EVEN n that XOR is 0,
     *       CONTRADICTING S != 0. So when n is even at least one safe move
     *       always exists, and the same argument holds on Alice's every turn
     *       (the count stays even at the start of each of her turns) -> Alice wins.
     *
     *     - For ODD n with S != 0, the symmetric argument makes Bob the winner.
     *
     *   time  = O(n)
     *   space = O(1)
     */
    public boolean xorGame(int[] nums) {
        int total = 0;
        for (int v : nums) {
            total ^= v;
        }
        return total == 0 || nums.length % 2 == 0;
    }

}
