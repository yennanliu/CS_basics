package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/get-the-maximum-score/description/

/**
 * 1537. Get the Maximum Score
 * Hard
 *
 * You are given two sorted arrays of distinct integers nums1 and nums2.
 *
 * A valid path is defined as follows:
 *
 * Choose array nums1 or nums2 to traverse (from index-0).
 * Traverse the current array from left to right.
 * If you are reading any value that is present in nums1 and nums2 you are allowed to change your path to the other array. (Only one repeated value is considered in the valid path).
 *
 * The score is defined as the sum of unique values in a valid path.
 *
 * Return the maximum score you can obtain of all possible valid paths. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,4,5,8,10], nums2 = [4,6,8,9]
 * Output: 30
 * Explanation: Valid paths:
 * [2,4,5,8,10], [2,4,5,8,9], [2,4,6,8,9], [2,4,6,8,10],  (starting from nums1)
 * [4,6,8,9], [4,5,8,10], [4,5,8,9], [4,6,8,10]    (starting from nums2)
 * The maximum is obtained with the path [2,4,6,8,10].
 *
 * Example 2:
 *
 * Input: nums1 = [1,3,5,7,9], nums2 = [3,5,100]
 * Output: 109
 * Explanation: Maximum sum is obtained with the path [1,3,5,100].
 *
 * Example 3:
 *
 * Input: nums1 = [1,2,3,4,5], nums2 = [6,7,8,9,10]
 * Output: 40
 * Explanation: There are no common elements between nums1 and nums2.
 * Maximum sum is obtained with the path [6,7,8,9,10].
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 10^5
 * 1 <= nums1[i], nums2[i] <= 10^7
 * nums1 and nums2 are strictly increasing.
 *
 */
public class GetTheMaximumScore {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: TWO POINTERS OVER BOTH SORTED ARRAYS, SETTLE THE CHOICE AT EACH COMMON VALUE
    //   common values cut both arrays into aligned segments; between two of them we are locked
    //   into one array, so take whichever segment sum is larger - segments are independent.
    //   sum1 / sum2 = best score ending on the nums1 / nums2 side so far.
    //   at a common value both sides may switch: sum1 = sum2 = max(sum1, sum2) + value.
    //   apply the modulo only at the end, or it would corrupt the max comparisons.
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int maxSum(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int i = 0;
        int j = 0;
        // long: up to 10^5 values of up to 10^7 each
        long sum1 = 0;
        long sum2 = 0;

        while (i < m || j < n) {
            if (j == n || (i < m && nums1[i] < nums2[j])) {
                sum1 += nums1[i];
                i++;
            } else if (i == m || nums2[j] < nums1[i]) {
                sum2 += nums2[j];
                j++;
            } else {
                // common value: the better path so far can continue on either array
                long best = Math.max(sum1, sum2) + nums1[i];
                sum1 = best;
                sum2 = best;
                i++;
                j++;
            }
        }
        return (int) (Math.max(sum1, sum2) % MOD);
    }
}
