package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-length-of-a-good-subsequence-ii/description/

import java.util.*;

/**
 * 3177. Find the Maximum Length of a Good Subsequence II
 * Hard
 *
 * You are given an integer array nums and a non-negative integer k. A sequence of integers seq is called good if there are at most k indices i in the range [0, seq.length - 2] such that seq[i] != seq[i + 1].
 *
 * Return the maximum possible length of a good subsequence of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,1,3], k = 2
 * Output: 4
 * Explanation:
 * The maximum length subsequence is [1,2,1,1,3].
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5,1], k = 0
 * Output: 2
 * Explanation:
 * The maximum length subsequence is [1,2,3,4,5,1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 10^3
 * 1 <= nums[i] <= 10^9
 * 0 <= k <= min(nums.length, 50)
 *
 */
public class FindTheMaximumLengthOfAGoodSubsequenceII {

    // V0
    // IDEA: DP WITH A BEST-PER-VALUE TABLE (DROPS LC 3176'S "WHICH EARLIER INDEX" LOOP)
    //   bestEndingWith[j].get(v) = longest good subsequence using j changes that ends with v
    //   bestAny[j]               = the same, maximised over every ending value
    //   Appending x: keep the run (same value, no change) or switch values (one change):
    //     candidate = max(bestEndingWith[j][x], bestAny[j - 1]) + 1
    //   Iterate j downward so bestAny[j - 1] still holds the state from before x.
    //   (If bestAny[j - 1] already ends with x, the change was not really spent; that sequence
    //   is still good under budget j, so the maximum stays correct.)
    /**
     * time = O(N * K)
     * space = O(N * K)
     */
    public int maximumLength(int[] nums, int k) {
        List<Map<Integer, Integer>> bestEndingWith = new ArrayList<>();
        for (int j = 0; j <= k; j++) {
            bestEndingWith.add(new HashMap<>());
        }
        int[] bestAny = new int[k + 1];
        int best = 0;

        for (int x : nums) {
            for (int j = k; j >= 0; j--) {
                Map<Integer, Integer> endingWith = bestEndingWith.get(j);
                // continue a subsequence that already ends with x (no change spent)
                int candidate = endingWith.getOrDefault(x, 0);
                // or append x after any value, spending one change
                if (j > 0) {
                    candidate = Math.max(candidate, bestAny[j - 1]);
                }
                candidate += 1;

                if (candidate > endingWith.getOrDefault(x, 0)) {
                    endingWith.put(x, candidate);
                }
                bestAny[j] = Math.max(bestAny[j], candidate);
                best = Math.max(best, candidate);
            }
        }
        return best;
    }
}
