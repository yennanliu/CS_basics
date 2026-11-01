package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/maximum-pulse-value-after-one-subarray-rotation/description/

/**
 * 4058. Maximum Pulse Value After One Subarray Rotation
 * Medium
 *
 * You are given an integer array nums of length n.
 *
 * Define the pulse value of an integer array arr as the alternating sum starting
 * at index 0: pulse(arr) = arr[0] - arr[1] + arr[2] - arr[3] + ....
 *
 * You may perform at most one operation on nums:
 *
 * Choose two indices l and r such that 0 <= l < r < n.
 * Left-rotate the subarray nums[l..r] by exactly one position. For example,
 * [a, b, c, d] becomes [b, c, d, a].
 *
 * Return the maximum pulse value that can be obtained after performing at most
 * one such operation.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,2]
 * Output: 6
 * Explanation:
 * The original pulse value is 1 - 5 + 2 = -2.
 * Rotate the subarray nums[0..1] from [1, 5] to [5, 1].
 * The resulting array is [5, 1, 2] and its pulse value is 5 - 1 + 2 = 6, which
 * is the maximum possible.
 *
 * Example 2:
 *
 * Input: nums = [6,4,3]
 * Output: 7
 * Explanation:
 * The original pulse value is 6 - 4 + 3 = 5.
 * Rotate the subarray nums[1..2] from [4, 3] to [3, 4].
 * The resulting array is [6, 3, 4] and its pulse value is 6 - 3 + 4 = 7, which
 * is the maximum possible.
 *
 * Example 3:
 *
 * Input: nums = [9,7]
 * Output: 2
 * Explanation:
 * The original pulse value is 9 - 7 = 2, which is already maximum. Thus, no
 * rotation is required.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class MaximumPulseValueAfterOneSubarrayRotation {

    // V0
    // IDEA: PREFIX SUM OF SIGN-FLIP GAINS + BEST LEFT END SO FAR
    // let sign[i] = +1 on even i, -1 on odd i. left-rotating nums[l..r]:
    //   - nums[l+1..r] each move one slot left, so their sign flips: gain -2 * sign[k] * nums[k] for k in (l, r]
    //   - nums[l] jumps to slot r:                                   gain (sign[r] - sign[l]) * nums[l]
    // with P[x] = prefix sum of b[k] = -2 * sign[k] * nums[k], the gain of (l, r) is
    //   P[r] - P[l] + sign[r] * nums[l] - sign[l] * nums[l]
    // so for a fixed r we only need the best l < r, kept separately for sign[r] = +1 and sign[r] = -1.
    // NOTE: a rotation is NOT a swap -- every element in (l, r] flips sign, so a long rotation can gain more.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxValue(int[] nums) {
        // edge case: empty input
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;

        // pulse value with no operation; long because n * 10^9 overflows int
        long base = 0;
        for (int i = 0; i < n; i++) {
            base += sign(i) * (long) nums[i];
        }
        // a single element has no (l, r) pair to rotate
        if (n == 1) {
            return base;
        }

        long bestGain = 0;                 // doing nothing is allowed
        long bestLeftIfPos = Long.MIN_VALUE; // best (+nums[l] - P[l] - sign[l]*nums[l]) over l seen so far
        long bestLeftIfNeg = Long.MIN_VALUE; // best (-nums[l] - P[l] - sign[l]*nums[l]) over l seen so far
        long prefix = 0;                   // P[k] = b[0] + ... + b[k]

        for (int k = 0; k < n; k++) {
            long value = nums[k];
            int signK = sign(k);
            prefix += -2L * signK * value;

            // k as the right end r, paired with the best left end l < k
            if (k > 0) {
                long bestLeft = (signK == 1) ? bestLeftIfPos : bestLeftIfNeg;
                bestGain = Math.max(bestGain, prefix + bestLeft);
            }

            // k as a left end l for a later r
            long common = -prefix - signK * value;
            bestLeftIfPos = Math.max(bestLeftIfPos, value + common);
            bestLeftIfNeg = Math.max(bestLeftIfNeg, -value + common);
        }
        return base + bestGain;
    }

    /** +1 on an even index, -1 on an odd one: the index's sign in the pulse sum. */
    private int sign(int index) {
        return (index % 2 == 0) ? 1 : -1;
    }
}
