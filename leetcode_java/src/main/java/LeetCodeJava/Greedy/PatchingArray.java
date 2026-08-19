package LeetCodeJava.Greedy;

// https://leetcode.com/problems/patching-array/

/**
 *  330. Patching Array
 *  Hard
 *
 *  Given a sorted integer array nums and an integer n, add/patch elements to the array
 *  such that any number in the range [1, n] inclusive can be formed by the sum of some
 *  elements in the array.
 *
 *  Return the minimum number of patches required.
 *
 *  Example 1:
 *    Input: nums = [1,3], n = 6
 *    Output: 1     (patch 2 -> [1,2,3] covers 1..6)
 *
 *  Example 2:
 *    Input: nums = [1,5,10], n = 20
 *    Output: 2     (patch 2 and 4)
 *
 *  Example 3:
 *    Input: nums = [1,2,2], n = 5
 *    Output: 0
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    1 <= nums[i] <= 10^4
 *    nums is sorted in ascending order.
 *    1 <= n <= 2^31 - 1
 */
public class PatchingArray {

    // V0
    // IDEA: greedy. Track `miss` = smallest value in [1, n] not yet reachable.
    //       If nums[i] <= miss we can extend coverage to [1, miss + nums[i]);
    //       otherwise patch `miss` itself, which doubles the covered range.
    /**
     * time = O(m + log n)   where m = nums.length
     * space = O(1)
     */
    public int minPatches(int[] nums, int n) {
        long miss = 1;
        int i = 0;
        int patches = 0;

        while (miss <= n) {
            if (i < nums.length && nums[i] <= miss) {
                miss += nums[i];
                i++;
            } else {
                miss += miss; // patch with `miss`
                patches++;
            }
        }
        return patches;
    }
}
