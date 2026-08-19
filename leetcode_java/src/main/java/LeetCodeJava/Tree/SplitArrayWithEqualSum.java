package LeetCodeJava.Tree;

// https://leetcode.com/problems/split-array-with-equal-sum/

import java.util.HashSet;
import java.util.Set;

/**
 *  548. Split Array with Equal Sum
 *  Hard
 *
 *  Given an integer array nums of length n, return true if there is a triplet
 *  (i, j, k) which satisfies the following conditions:
 *
 *   - 0 < i, i + 1 < j, j + 1 < k < n - 1
 *   - The sum of the subarrays nums[0..i-1], nums[i+1..j-1],
 *     nums[j+1..k-1] and nums[k+1..n-1] is equal.
 *
 *  Example 1:
 *
 *  Input: nums = [1,2,1,2,1,2,1]
 *  Output: true
 *  Explanation: i = 1, j = 3, k = 5, every part sums to 1.
 *
 *  Example 2:
 *
 *  Input: nums = [1,2,1,2,1,2,1,2]
 *  Output: false
 *
 *  Constraints:
 *
 *  n == nums.length
 *  1 <= n <= 2000
 *  -10^6 <= nums[i] <= 10^6
 */
public class SplitArrayWithEqualSum {

    // V0
    // IDEA: prefix sum + fix the middle cut j, collect all valid left parts
    //       (i) into a set, then scan the right cuts (k) against that set
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public boolean splitArray(int[] nums) {
        if (nums == null || nums.length < 7) {
            return false;
        }
        int n = nums.length;

        // preSum[x] = sum of nums[0 .. x]
        int[] preSum = new int[n];
        preSum[0] = nums[0];
        for (int x = 1; x < n; x++) {
            preSum[x] = preSum[x - 1] + nums[x];
        }

        for (int j = 3; j < n - 3; j++) {
            Set<Integer> seen = new HashSet<>();
            for (int i = 1; i < j - 1; i++) {
                // sum(0..i-1) == sum(i+1..j-1)
                if (preSum[i - 1] == preSum[j - 1] - preSum[i]) {
                    seen.add(preSum[i - 1]);
                }
            }
            for (int k = j + 2; k < n - 1; k++) {
                int third = preSum[k - 1] - preSum[j];   // sum(j+1..k-1)
                int fourth = preSum[n - 1] - preSum[k];  // sum(k+1..n-1)
                if (third == fourth && seen.contains(third)) {
                    return true;
                }
            }
        }
        return false;
    }
}
