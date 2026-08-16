package LeetCodeJava.Math;

// https://leetcode.com/problems/chalkboard-xor-game/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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


    // V1
    // IDEA: GAME-TREE SEARCH WITH MEMO (no theorem)
    /**
     *  Play the actual game: from a multiset state, the mover wins if the XOR is
     *  already 0, or if SOME erasure leaves the opponent losing.
     *
     *  Exponential in the number of distinct values, so it is only usable on tiny
     *  inputs -- but it assumes nothing, so it is the proof that the one-line rule
     *  in V0 is right.
     *
     *  time  = O(2^n * n)
     *  space = O(2^n)
     */
    public boolean xorGame_1(int[] nums) {
        Map<String, Boolean> memo = new HashMap<>();
        boolean[] alive = new boolean[nums.length];
        Arrays.fill(alive, true);
        return wins(nums, alive, memo);
    }

    private boolean wins(int[] nums, boolean[] alive, Map<String, Boolean> memo) {
        int total = 0;
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (alive[i]) {
                total ^= nums[i];
                count += 1;
            }
        }
        if (total == 0) {
            return true;      // the mover wins immediately
        }
        if (count == 0) {
            return false;
        }

        String key = Arrays.toString(alive);
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }
        memo.put(key, false);

        boolean res = false;
        for (int i = 0; i < nums.length && !res; i++) {
            if (!alive[i]) {
                continue;
            }
            alive[i] = false;
            // erasing must not zero the board, and must leave the opponent losing
            if ((total ^ nums[i]) != 0 && !wins(nums, alive, memo)) {
                res = true;
            }
            alive[i] = true;
        }

        memo.put(key, res);
        return res;
    }

    // V2
    // IDEA: BIT-BY-BIT ARGUMENT (why an even count always leaves a safe move)
    /**
     *  Restates the proof as code: if the total XOR is non-zero, look at any bit
     *  set in it. The number of elements carrying that bit must be ODD, so with an
     *  EVEN element count some element does NOT carry it -- erasing that one keeps
     *  the XOR non-zero, which is exactly the safe move.
     *
     *  Same O(n) answer, but the search for the witness makes the theorem
     *  constructive rather than asserted.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public boolean xorGame_2(int[] nums) {
        int total = 0;
        for (int v : nums) {
            total ^= v;
        }
        if (total == 0) {
            return true;
        }
        if (nums.length % 2 == 1) {
            return false;
        }

        // with an even count a safe erasure must exist -- find it
        int bit = total & (-total);
        for (int v : nums) {
            if ((v & bit) == 0) {
                return true;   // erasing v leaves the XOR non-zero
            }
        }
        return true;           // unreachable for an even count
    }

    // V3
    // IDEA: PARITY ONE-LINER via XOR reduction over a stream
    /**
     *  The whole rule collapses to `xor == 0 || n is even`, written as a fold.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public boolean xorGame_3(int[] nums) {
        return Arrays.stream(nums).reduce(0, (x, y) -> x ^ y) == 0 || (nums.length & 1) == 0;
    }

}
