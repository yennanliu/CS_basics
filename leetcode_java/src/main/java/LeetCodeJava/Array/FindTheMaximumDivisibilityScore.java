package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-maximum-divisibility-score/description/

import java.util.Arrays;

/**
 * 2644. Find the Maximum Divisibility Score
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two integer arrays nums and divisors.
 *
 * The divisibility score of divisors[i] is the number of indices j such that nums[j] is divisible by divisors[i].
 *
 * Return the integer divisors[i] with the maximum divisibility score. If multiple integers have the maximum score, return the smallest one.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,9,15,50], divisors = [5,3,7,2]
 *
 * Output: 2
 *
 * Explanation:
 *
 * The divisibility score of divisors[0] is 2 since nums[2] and nums[3] are divisible by 5.
 *
 * The divisibility score of divisors[1] is 2 since nums[1] and nums[2] are divisible by 3.
 *
 * The divisibility score of divisors[2] is 0 since none of the numbers in nums is divisible by 7.
 *
 * The divisibility score of divisors[3] is 2 since nums[0] and nums[3] are divisible by 2.
 *
 * As divisors[0], divisors[1], and divisors[3] have the same divisibility score, we return the smaller one which is divisors[3].
 *
 * Example 2:
 *
 * Input: nums = [4,7,9,3,9], divisors = [5,2,3]
 *
 * Output: 3
 *
 * Explanation:
 *
 * The divisibility score of divisors[0] is 0 since none of numbers in nums is divisible by 5.
 *
 * The divisibility score of divisors[1] is 1 since only nums[0] is divisible by 2.
 *
 * The divisibility score of divisors[2] is 3 since nums[2], nums[3] and nums[4] are divisible by 3.
 *
 * Example 3:
 *
 * Input: nums = [20,14,21,10], divisors = [10,16,20]
 *
 * Output: 10
 *
 * Explanation:
 *
 * The divisibility score of divisors[0] is 2 since nums[0] and nums[3] are divisible by 10.
 *
 * The divisibility score of divisors[1] is 0 since none of the numbers in nums is divisible by 16.
 *
 * The divisibility score of divisors[2] is 1 since nums[0] is divisible by 20.
 *
 *
 *
 * Constraints:
 *
 * 1 <= nums.length, divisors.length <= 1000
 * 1 <= nums[i], divisors[i] <= 109
 *
 */
public class FindTheMaximumDivisibilityScore {

    // V0
//    public int maxDivScore(int[] nums, int[] divisors) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gemini)
    /**
     * Finds the divisor with the maximum divisibility score.
     * Time Complexity: O(D * N), where D is divisors.length and N is nums.length.
     * Space Complexity: O(1).
     */
    public int maxDivScore_0_1(int[] nums, int[] divisors) {
        int maxScore = -1;
        int bestDivisor = Integer.MAX_VALUE;

        // 1. Iterate through each divisor in the divisors array
        /**  NOTE !!!
         *
         *  can simply loop over every divisor
         */
        for (int d : divisors) {
            int currentScore = getDivisibleCnt(nums, d);

            // 2. Tie-breaking logic:
            // - If the current score is higher, update both maxScore and bestDivisor.
            // - If the score is equal, update bestDivisor only if the current divisor is smaller.
            if (currentScore > maxScore) {
                maxScore = currentScore;
                bestDivisor = d;
            }
            /**  NOTE !!!
             *
             *  if there is a tie, we should keep the one
             *  with SMALLER value (but not small index)
             *
             *  e.g. If multiple integers have the maximum score, return the smallest one.
             *
             */
            else if (currentScore == maxScore) {
                if (d < bestDivisor) {
                    bestDivisor = d;
                }
            }
        }

        return bestDivisor;
    }

    private int getDivisibleCnt(int[] nums, int x) {
        int res = 0;
        for (int n : nums) {
            if (n % x == 0) {
                res++;
            }
        }
        return res;
    }

    // V0-2
    // IDEA: BRUTE FORCE (fixed by GPT)
    public int maxDivScore_0_2(int[] nums, int[] divisors) {
        int bestScore = -1;
        int bestDiv = Integer.MAX_VALUE;

        for (int d : divisors) {
            int cnt = 0;

            for (int n : nums) {
                if (n % d == 0)
                    cnt++;
            }

            if (cnt > bestScore || (cnt == bestScore && d < bestDiv)) {
                bestScore = cnt;
                bestDiv = d;
            }
        }

        return bestDiv;
    }


    // V1
    // https://leetcode.ca/2023-02-25-2644-Find-the-Maximum-Divisibility-Score/
    public int maxDivScore_1(int[] nums, int[] divisors) {
        int ans = divisors[0];
        int mx = 0;
        for (int div : divisors) {
            int cnt = 0;
            for (int x : nums) {
                if (x % div == 0) {
                    ++cnt;
                }
            }
            if (mx < cnt) {
                mx = cnt;
                ans = div;
            } else if (mx == cnt) {
                ans = Math.min(ans, div);
            }
        }
        return ans;
    }


    // V2
    // https://leetcode.com/problems/find-the-maximum-divisibility-score/solutions/3421736/clean-simple-java-solution-by-himanshubh-2ndh/
    public int maxDivScore_2(int[] nums, int[] divisors) {
        Arrays.sort(divisors);
        int max = 0, res = divisors[0];
        for (int i = 0; i < divisors.length; i++) {
            int cnt = 0;
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] % divisors[i] == 0)
                    cnt++;
            }
            if (cnt > max) {
                max = cnt;
                res = divisors[i];
            }
        }
        return res;
    }

    // V3


}
