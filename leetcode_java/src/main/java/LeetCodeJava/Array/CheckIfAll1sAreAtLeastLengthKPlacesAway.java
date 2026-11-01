package LeetCodeJava.Array;

// https://leetcode.com/problems/check-if-all-1s-are-at-least-length-k-places-away/description/

/**
 * 1437. Check If All 1's Are at Least Length K Places Away
 * Easy
 *
 * Given an binary array nums and an integer k, return true if all 1's are at
 * least k places away from each other, otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,0,0,0,1,0,0,1], k = 2
 * Output: true
 * Explanation: Each of the 1s are at least 2 places away from each other.
 *
 * Example 2:
 *
 * Input: nums = [1,0,0,1,0,1], k = 2
 * Output: false
 * Explanation: The second 1 and third 1 are only one apart from each other.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= k <= nums.length
 * nums[i] is 0 or 1
 *
 */
public class CheckIfAll1sAreAtLeastLengthKPlacesAway {

    // V0
    // IDEA: ONE PASS, TRACK THE PREVIOUS 1's INDEX
    //       the number of 0s between two consecutive 1s at prev and i is (i - prev - 1),
    //       and every such gap must be >= k
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean kLengthApart(int[] nums, int k) {
        // -1 means "no 1 seen yet", so the first 1 never fails the check
        int previousOne = -1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 1) {
                continue;
            }
            if (previousOne != -1) {
                int zerosBetween = i - previousOne - 1;
                if (zerosBetween < k) {
                    return false;
                }
            }
            previousOne = i;
        }
        return true;
    }
}
