package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-array-changes-to-make-differences-equal/description/

/**
 * 3224. Minimum Array Changes to Make Differences Equal
 * Medium
 *
 * You are given an integer array nums of size n where n is even, and an integer k.
 *
 * You can perform some changes on the array, where in one change you can replace any element in the array with any integer in the range from 0 to k.
 *
 * You need to perform some changes (possibly none) such that the final array satisfies the following condition:
 *
 * There exists an integer X such that abs(a[i] - a[n - i - 1]) = X for all (0 <= i < n).
 *
 * Return the minimum number of changes required to satisfy the above condition.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,0,1,2,4,3], k = 4
 * Output: 2
 * Explanation:
 * We can perform the following changes:
 * Replace nums[1] by 2. The resulting array is nums = [1,2,1,2,4,3].
 * Replace nums[3] by 3. The resulting array is nums = [1,2,1,3,4,3].
 * The integer X will be 2.
 *
 * Example 2:
 *
 * Input: nums = [0,1,2,3,3,6,5,4], k = 6
 * Output: 2
 * Explanation:
 * We can perform the following operations:
 * Replace nums[3] by 0. The resulting array is nums = [0,1,2,0,3,6,5,4].
 * Replace nums[4] by 4. The resulting array is nums = [0,1,2,0,4,6,5,4].
 * The integer X will be 4.
 *
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 10^5
 * n is even.
 * 0 <= nums[i] <= k <= 10^5
 *
 */
public class MinimumArrayChangesToMakeDifferencesEqual {

    // V0
    // IDEA: DIFFERENCE ARRAY OVER CANDIDATE X (each mirrored pair costs 0, 1 or 2 changes for a given X)
    /**
     * time = O(N + K)
     * space = O(K)
     */
    public int minChanges(int[] nums, int k) {
        int n = nums.length;
        int pairs = n / 2;

        // exactGap[x] = number of pairs whose current gap is exactly x (cost 0 for X = x)
        int[] exactGap = new int[k + 2];
        // reachDiff is a difference array: after a prefix sum, reachable[x] = number of
        // pairs that can reach gap x by changing only ONE element (cost <= 1)
        int[] reachDiff = new int[k + 2];

        for (int i = 0; i < pairs; i++) {
            int a = nums[i];
            int b = nums[n - 1 - i];
            int gap = Math.abs(a - b);

            // changing one element can make any gap up to:
            // push the larger one to k, or pull the smaller one down to 0
            int reach = Math.max(Math.max(a, b), k - Math.min(a, b));

            exactGap[gap]++;
            reachDiff[0]++;
            reachDiff[reach + 1]--;
        }

        int best = Integer.MAX_VALUE;
        int reachable = 0;
        for (int x = 0; x <= k; x++) {
            reachable += reachDiff[x];
            // pairs already at x cost 0, other reachable pairs cost 1, the rest cost 2
            int cost = (reachable - exactGap[x]) + 2 * (pairs - reachable);
            best = Math.min(best, cost);
        }
        return best;
    }
}
