package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/greatest-sum-divisible-by-three/description/

/**
 * 1262. Greatest Sum Divisible by Three
 * Medium
 *
 * Given an integer array nums, return the maximum possible sum of elements of
 * the array such that it is divisible by three.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,6,5,1,8]
 * Output: 18
 * Explanation: Pick numbers 3, 6, 1 and 8 their sum is 18 (maximum sum divisible by 3).
 *
 * Example 2:
 *
 * Input: nums = [4]
 * Output: 0
 * Explanation: Since 4 is not divisible by 3, do not pick any number.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4,4]
 * Output: 12
 * Explanation: Pick numbers 1, 3, 4 and 4 their sum is 12 (maximum sum divisible by 3).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 4 * 10^4
 * 1 <= nums[i] <= 10^4
 *
 */
public class GreatestSumDivisibleByThree {

    /** Marks a remainder no subset has reached yet. */
    private static final int UNREACHABLE = -1;

    // V0
    // IDEA: DP ON REMAINDER MOD 3
    /**
     * dp[r] = biggest sum seen so far whose remainder mod 3 is r (UNREACHABLE if none)
     *
     * each x either joins a running sum (moving it to remainder (r + x) % 3) or is skipped:
     *   next[(dp[r] + x) % 3] = max(next[(dp[r] + x) % 3], dp[r] + x)
     *
     * base: dp = [0, UNREACHABLE, UNREACHABLE] (empty pick has sum 0)
     * answer: dp[0]
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxSumDivThree(int[] nums) {
        int[] dp = {0, UNREACHABLE, UNREACHABLE};

        for (int x : nums) {
            // start from a copy: skipping x keeps every old sum
            int[] next = dp.clone();
            for (int r = 0; r < 3; r++) {
                if (dp[r] == UNREACHABLE) {
                    continue;
                }
                int sum = dp[r] + x;
                int newRemainder = sum % 3;
                next[newRemainder] = Math.max(next[newRemainder], sum);
            }
            dp = next;
        }
        return dp[0];
    }

    // V1
    // IDEA: GREEDY (TAKE EVERYTHING, THEN DROP THE SMALLEST SURPLUS)
    /**
     * total % 3 == 1 -> drop the smallest x with x % 3 == 1, or the two smallest with x % 3 == 2
     * total % 3 == 2 -> mirrored
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxSumDivThree_1(int[] nums) {
        int total = 0;
        // two smallest numbers of each non-zero remainder class
        int smallestOne = Integer.MAX_VALUE;
        int secondOne = Integer.MAX_VALUE;
        int smallestTwo = Integer.MAX_VALUE;
        int secondTwo = Integer.MAX_VALUE;

        for (int x : nums) {
            total += x;
            if (x % 3 == 1) {
                if (x < smallestOne) {
                    secondOne = smallestOne;
                    smallestOne = x;
                } else if (x < secondOne) {
                    secondOne = x;
                }
            } else if (x % 3 == 2) {
                if (x < smallestTwo) {
                    secondTwo = smallestTwo;
                    smallestTwo = x;
                } else if (x < secondTwo) {
                    secondTwo = x;
                }
            }
        }

        if (total % 3 == 0) {
            return total;
        }

        int best = 0;
        if (total % 3 == 1) {
            if (smallestOne != Integer.MAX_VALUE) {
                best = Math.max(best, total - smallestOne);
            }
            if (secondTwo != Integer.MAX_VALUE) {
                best = Math.max(best, total - smallestTwo - secondTwo);
            }
        } else {
            if (smallestTwo != Integer.MAX_VALUE) {
                best = Math.max(best, total - smallestTwo);
            }
            if (secondOne != Integer.MAX_VALUE) {
                best = Math.max(best, total - smallestOne - secondOne);
            }
        }
        return best;
    }
}
