package LeetCodeJava.Math;

// https://leetcode.com/problems/find-if-digit-game-can-be-won/description/

/**
 * 3232. Find if Digit Game Can Be Won
 * Easy
 *
 * You are given an array of positive integers nums.
 *
 * Alice and Bob are playing a game. In the game, Alice can choose either all single-digit numbers or
 * all double-digit numbers from nums, and the rest of the numbers are given to Bob. Alice wins if the
 * sum of her numbers is strictly greater than the sum of Bob's numbers.
 *
 * Return true if Alice can win this game, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,10]
 * Output: false
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5,14]
 * Output: true
 *
 * Example 3:
 *
 * Input: nums = [5,5,5,25]
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 99
 *
 */
public class FindIfDigitGameCanBeWon {

    // V0
    // IDEA: THE TWO GROUPS ARE COMPLEMENTARY — ALICE TAKES THE LARGER SUM, SO SHE LOSES ONLY ON A TIE
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canAliceWin(int[] nums) {
        int singleDigitSum = 0;
        int doubleDigitSum = 0;
        for (int value : nums) {
            if (value < 10) {
                singleDigitSum += value;
            } else {
                doubleDigitSum += value;
            }
        }
        return singleDigitSum != doubleDigitSum;
    }
}
