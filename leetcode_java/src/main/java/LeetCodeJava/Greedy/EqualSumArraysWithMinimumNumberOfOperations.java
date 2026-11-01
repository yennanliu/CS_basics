package LeetCodeJava.Greedy;

// https://leetcode.com/problems/equal-sum-arrays-with-minimum-number-of-operations/description/

/**
 * 1775. Equal Sum Arrays With Minimum Number of Operations
 * Medium
 *
 * You are given two arrays of integers nums1 and nums2, possibly of different
 * lengths. The values in the arrays are between 1 and 6, inclusive.
 *
 * In one operation, you can change any integer's value in any of the arrays to
 * any value between 1 and 6, inclusive.
 *
 * Return the minimum number of operations required to make the sum of values in
 * nums1 equal to the sum of values in nums2. Return -1 if it is not possible to
 * make the sum of the two arrays equal.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,3,4,5,6], nums2 = [1,1,2,2,2,2]
 * Output: 3
 * Explanation: You can make the sums of nums1 and nums2 equal with 3 operations.
 * - Change nums2[0] to 6. nums1 = [1,2,3,4,5,6], nums2 = [6,1,2,2,2,2].
 * - Change nums1[5] to 1. nums1 = [1,2,3,4,5,1], nums2 = [6,1,2,2,2,2].
 * - Change nums1[2] to 2. nums1 = [1,2,2,4,5,1], nums2 = [6,1,2,2,2,2].
 *
 * Example 2:
 *
 * Input: nums1 = [1,1,1,1,1,1,1], nums2 = [6]
 * Output: -1
 * Explanation: There is no way to decrease the sum of nums1 or to increase the
 * sum of nums2 to make them equal.
 *
 * Example 3:
 *
 * Input: nums1 = [6,6], nums2 = [1]
 * Output: 3
 * Explanation: You can make the sums of nums1 and nums2 equal with 3 operations.
 * - Change nums1[0] to 2. nums1 = [2,6], nums2 = [1].
 * - Change nums1[1] to 2. nums1 = [2,2], nums2 = [1].
 * - Change nums2[0] to 4. nums1 = [2,2], nums2 = [4].
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 10^5
 * 1 <= nums1[i], nums2[i] <= 6
 *
 */
public class EqualSumArraysWithMinimumNumberOfOperations {

    // V0
    // IDEA: GREEDY ON THE "GAIN" OF EACH SINGLE CHANGE (counting sort over gains 1..5)
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public int minOperations(int[] nums1, int[] nums2) {
        int sum1 = 0;
        for (int v : nums1) {
            sum1 += v;
        }
        int sum2 = 0;
        for (int v : nums2) {
            sum2 += v;
        }

        // make `smaller` the array with the smaller sum
        int[] smaller = nums1;
        int[] larger = nums2;
        if (sum1 > sum2) {
            smaller = nums2;
            larger = nums1;
        }
        int gap = Math.abs(sum1 - sum2);
        if (gap == 0) {
            return 0;
        }

        // gainCount[g] = how many single changes can close the gap by up to g
        // raising v in the smaller array gains 6 - v; lowering v in the larger gains v - 1
        int[] gainCount = new int[6];
        for (int v : smaller) {
            gainCount[6 - v]++;
        }
        for (int v : larger) {
            gainCount[v - 1]++;
        }

        // always spend the biggest available gain first
        int operations = 0;
        for (int gain = 5; gain >= 1; gain--) {
            if (gainCount[gain] == 0) {
                continue;
            }
            // ceil(gap / gain) changes of this size would finish the job
            int needed = (gap + gain - 1) / gain;
            int take = Math.min(needed, gainCount[gain]);
            operations += take;
            gap -= take * gain;
            if (gap <= 0) {
                return operations;
            }
        }

        // all possible gains together still cannot close the gap
        return -1;
    }
}
