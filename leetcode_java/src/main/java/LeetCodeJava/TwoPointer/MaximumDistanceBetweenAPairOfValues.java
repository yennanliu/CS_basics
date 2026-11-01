package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/maximum-distance-between-a-pair-of-values/description/

/**
 * 1855. Maximum Distance Between a Pair of Values
 * Medium
 *
 * You are given two non-increasing 0-indexed integer arrays nums1 and nums2.
 *
 * A pair of indices (i, j), where 0 <= i < nums1.length and 0 <= j < nums2.length, is valid if both i <= j and nums1[i] <= nums2[j]. The distance of the pair is j - i.
 *
 * Return the maximum distance of any valid pair (i, j). If there are no valid pairs, return 0.
 *
 * An array arr is non-increasing if arr[i-1] >= arr[i] for every 1 <= i < arr.length.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [55,30,5,4,2], nums2 = [100,20,10,10,5]
 * Output: 2
 * Explanation: The valid pairs are (0,0), (2,2), (2,3), (2,4), (3,3), (3,4), and (4,4).
 * The maximum distance is 2 with pair (2,4).
 *
 * Example 2:
 *
 * Input: nums1 = [2,2,2], nums2 = [10,10,1]
 * Output: 1
 * Explanation: The valid pairs are (0,0), (0,1), and (1,1).
 * The maximum distance is 1 with pair (0,1).
 *
 * Example 3:
 *
 * Input: nums1 = [30,29,19,5], nums2 = [25,25,25,25,25]
 * Output: 2
 * Explanation: The valid pairs are (2,2), (2,3), (2,4), and (3,3), and (3,4).
 * The maximum distance is 2 with pair (2,4).
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 10^5
 * 1 <= nums1[i], nums2[j] <= 10^5
 * Both nums1 and nums2 are non-increasing.
 *
 */
public class MaximumDistanceBetweenAPairOfValues {

    // V0
    // IDEA: TWO POINTERS (both arrays are non-increasing -> j never moves back)
    //       for a fixed i we want the LARGEST j >= i with nums1[i] <= nums2[j].
    //       as i grows nums1[i] only gets smaller, so that frontier j only moves
    //       forward. push j while the pair is valid; then j - 1 is the last
    //       valid index and j - 1 - i the candidate distance.
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int maxDistance(int[] nums1, int[] nums2) {
        int best = 0;
        int j = 0;
        for (int i = 0; i < nums1.length; i++) {
            // the pair needs i <= j
            if (j < i) {
                j = i;
            }
            while (j < nums2.length && nums1[i] <= nums2[j]) {
                j++;
            }
            // j is the first invalid index; a negative candidate is ignored by max
            best = Math.max(best, j - 1 - i);
        }
        return best;
    }
}
