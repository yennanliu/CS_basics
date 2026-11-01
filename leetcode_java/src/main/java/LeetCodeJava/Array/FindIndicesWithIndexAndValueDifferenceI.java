package LeetCodeJava.Array;

// https://leetcode.com/problems/find-indices-with-index-and-value-difference-i/description/

/**
 * 2903. Find Indices With Index and Value Difference I
 * Easy
 *
 * You are given a 0-indexed integer array nums having length n, an integer indexDifference, and an integer valueDifference.
 *
 * Your task is to find two indices i and j, both in the range [0, n - 1], that satisfy the following conditions:
 *
 * - abs(i - j) >= indexDifference, and
 * - abs(nums[i] - nums[j]) >= valueDifference
 *
 * Return an integer array answer, where answer = [i, j] if there are two such indices, and answer = [-1, -1] otherwise. If there are multiple choices for the two indices, return any of them.
 *
 * Note: i and j may be equal.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,1,4,1], indexDifference = 2, valueDifference = 4
 * Output: [0,3]
 * Explanation: In this example, i = 0 and j = 3 can be selected.
 * abs(0 - 3) >= 2 and abs(nums[0] - nums[3]) >= 4.
 * Hence, a valid answer is [0,3].
 * [3,0] is also a valid answer.
 *
 * Example 2:
 *
 * Input: nums = [2,1], indexDifference = 0, valueDifference = 0
 * Output: [0,0]
 * Explanation: In this example, i = 0 and j = 0 can be selected.
 * abs(0 - 0) >= 0 and abs(nums[0] - nums[0]) >= 0.
 * Hence, a valid answer is [0,0].
 * Other valid answers are [0,1], [1,0], and [1,1].
 *
 * Example 3:
 *
 * Input: nums = [1,2,3], indexDifference = 2, valueDifference = 4
 * Output: [-1,-1]
 * Explanation: In this example, it can be shown that it is impossible to find two indices that satisfy both conditions.
 * Hence, [-1,-1] is returned.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 100
 * 0 <= nums[i] <= 50
 * 0 <= indexDifference <= 100
 * 0 <= valueDifference <= 50
 *
 */
public class FindIndicesWithIndexAndValueDifferenceI {

    // V0
    // IDEA: SLIDING GAP + RUNNING MIN / MAX INDEX
    /**
     * time = O(N)
     * space = O(1)
     */
    public int[] findIndices(int[] nums, int indexDifference, int valueDifference) {
        // indices of the smallest / largest value among every j that is already
        // at least indexDifference behind the current right end i
        int minIdx = 0;
        int maxIdx = 0;

        // if indexDifference > n - 1 the loop never runs and we fall through to [-1, -1]
        for (int i = indexDifference; i < nums.length; i++) {
            // j is the index that has just become far enough away from i
            // (j == i when indexDifference == 0, which the problem allows)
            int j = i - indexDifference;
            if (nums[j] < nums[minIdx]) {
                minIdx = j;
            }
            if (nums[j] > nums[maxIdx]) {
                maxIdx = j;
            }

            // against a fixed nums[i], the largest |difference| is reached
            // either at the running min or at the running max
            if (nums[i] - nums[minIdx] >= valueDifference) {
                return new int[] {minIdx, i};
            }
            if (nums[maxIdx] - nums[i] >= valueDifference) {
                return new int[] {maxIdx, i};
            }
        }
        return new int[] {-1, -1};
    }

    // V1
    // IDEA: BRUTE FORCE - TEST EVERY PAIR (n <= 100, so O(n^2) is fine here)
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int[] findIndices_1(int[] nums, int indexDifference, int valueDifference) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // j starts at i (not i + 1): i == j is allowed by the problem
            for (int j = i; j < n; j++) {
                boolean farEnoughApart = j - i >= indexDifference;
                boolean differentEnough = Math.abs(nums[i] - nums[j]) >= valueDifference;
                if (farEnoughApart && differentEnough) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {-1, -1};
    }
}
