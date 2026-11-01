package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-minimum-possible-sum-of-a-beautiful-array/description/

/**
 * 2834. Find the Minimum Possible Sum of a Beautiful Array
 * Medium
 *
 * You are given positive integers n and target.
 *
 * An array nums is beautiful if it meets the following conditions:
 *
 * nums.length == n.
 * nums consists of pairwise distinct positive integers.
 * There doesn't exist two distinct indices, i and j, in the range [0, n - 1], such that nums[i] + nums[j] == target.
 *
 * Return the minimum possible sum that a beautiful array could have modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 2, target = 3
 * Output: 4
 * Explanation: We can see that nums = [1,3] is beautiful.
 * - The array nums has length n = 2.
 * - The array nums consists of pairwise distinct positive integers.
 * - There doesn't exist two distinct indices, i and j, with nums[i] + nums[j] == 3.
 * It can be proven that 4 is the minimum possible sum that a beautiful array could have.
 *
 * Example 2:
 *
 * Input: n = 3, target = 3
 * Output: 8
 * Explanation: We can see that nums = [1,3,4] is beautiful.
 * - The array nums has length n = 3.
 * - The array nums consists of pairwise distinct positive integers.
 * - There doesn't exist two distinct indices, i and j, with nums[i] + nums[j] == 3.
 * It can be proven that 8 is the minimum possible sum that a beautiful array could have.
 *
 * Example 3:
 *
 * Input: n = 1, target = 1
 * Output: 1
 * Explanation: We can see, that nums = [1] is beautiful.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 1 <= target <= 10^9
 *
 */
public class FindTheMinimumPossibleSumOfABeautifulArray {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: GREEDY + ARITHMETIC SERIES (closed form, no loop)
    //       same pairing as LC 2829, but n reaches 10^9. 1..half (half = target / 2) are all
    //       safe: each bans a partner > half. everything in (half, target) is then banned, so
    //       the remaining n - half values are target, target + 1, ...
    //         sum = half(half + 1)/2 + rest * target + rest(rest - 1)/2,  rest = n - half
    //       every term is below ~1.3 * 10^18, so a long holds it exactly before the mod.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minimumPossibleSum(int n, int target) {
        long count = n;
        // target / 2 pairs only with itself, so it is still legal
        long half = target / 2;

        // n small enough: the answer is just 1..n
        if (count <= half) {
            return (int) ((count * (count + 1) / 2) % MOD);
        }

        long rest = count - half;
        long lowBlock = half * (half + 1) / 2;
        long highBlock = rest * target + rest * (rest - 1) / 2;
        return (int) ((lowBlock + highBlock) % MOD);
    }
}
