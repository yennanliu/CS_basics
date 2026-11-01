package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-operations-to-make-array-equal-ii/description/

/**
 * 2541. Minimum Operations to Make Array Equal II
 * Medium
 *
 * You are given two integer arrays nums1 and nums2 of equal length n and an integer k. You can perform the following operation on nums1:
 *
 * Choose two indexes i and j and increment nums1[i] by k and decrement nums1[j] by k. In other words, nums1[i] = nums1[i] + k and nums1[j] = nums1[j] - k.
 *
 * nums1 is said to be equal to nums2 if for all indices i such that 0 <= i < n, nums1[i] == nums2[i].
 *
 * Return the minimum number of operations required to make nums1 equal to nums2. If it is impossible to make them equal, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [4,3,1,4], nums2 = [1,3,7,1], k = 3
 * Output: 2
 * Explanation: In 2 operations, we can transform nums1 to nums2.
 * 1st operation: i = 2, j = 0. After applying the operation, nums1 = [1,3,4,4].
 * 2nd operation: i = 2, j = 3. After applying the operation, nums1 = [1,3,7,1].
 * One can prove that it is impossible to make arrays equal in fewer operations.
 *
 * Example 2:
 *
 * Input: nums1 = [3,8,5,2], nums2 = [2,4,1,6], k = 1
 * Output: -1
 * Explanation: It can be proved that it is impossible to make the two arrays equal.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 2 <= n <= 10^5
 * 0 <= nums1[i], nums2[j] <= 10^9
 * 0 <= k <= 10^5
 *
 */
public class MinimumOperationsToMakeArrayEqualII {

    // V0
    // IDEA: GREEDY / COUNTING (every op moves exactly k from one slot to another)
    //       each position's gap (nums1[i] - nums2[i]) must be a multiple of k;
    //       a positive gap must DONATE gap / k times, a negative one RECEIVE |gap| / k times.
    //       one op pairs one donation with one receipt -> answer = donations, iff donations == receipts.
    //       k == 0 means nothing can move, so the arrays must already be equal.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long minOperations(int[] nums1, int[] nums2, int k) {
        long give = 0; // total units of k that must leave nums1
        long take = 0; // total units of k that must arrive in nums1

        for (int i = 0; i < nums1.length; i++) {
            // long: the difference of two values up to 1e9 is safe, but keep the sums wide
            long diff = (long) nums1[i] - nums2[i];
            if (diff == 0) {
                continue;
            }
            // k == 0: no value can change, so any mismatch is fatal (also avoids % 0)
            if (k == 0 || diff % k != 0) {
                return -1;
            }
            long steps = diff / k;
            if (steps > 0) {
                give += steps;
            } else {
                take += -steps;
            }
        }

        // every op moves one unit out and one unit in, so both totals must match
        if (give != take) {
            return -1;
        }
        return give;
    }
}
