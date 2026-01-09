package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/delete-and-earn/description/

import java.util.Arrays;
import java.util.HashMap;

/**
 *  740. Delete and Earn
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array nums. You want to maximize the number of points you get by performing the following operation any number of times:
 *
 * Pick any nums[i] and delete it to earn nums[i] points. Afterwards, you must delete every element equal to nums[i] - 1 and every element equal to nums[i] + 1.
 * Return the maximum number of points you can earn by applying the above operation some number of times.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,2]
 * Output: 6
 * Explanation: You can perform the following operations:
 * - Delete 4 to earn 4 points. Consequently, 3 is also deleted. nums = [2].
 * - Delete 2 to earn 2 points. nums = [].
 * You earn a total of 6 points.
 * Example 2:
 *
 * Input: nums = [2,2,3,3,3,4]
 * Output: 9
 * Explanation: You can perform the following operations:
 * - Delete a 3 to earn 3 points. All 2's and 4's are also deleted. nums = [3,3].
 * - Delete a 3 again to earn 3 points. nums = [3].
 * - Delete a 3 once more to earn 3 points. nums = [].
 * You earn a total of 9 points.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 104
 * 1 <= nums[i] <= 104
 *
 */
public class DeleteAndEarn {

    // V0
//    public int deleteAndEarn(int[] nums) {
//
//    }

    // V1
    // https://leetcode.ca/2017-12-09-740-Delete-and-Earn/


    // V2
    // IDEA: TOP DOWN DP
    // top down DP solution
    // https://leetcode.com/problems/delete-and-earn/solutions/284609/for-top-down-dp-fans-java-2ms-solution-e-sthk/
    public int deleteAndEarn_2(int[] nums) {
        // if we sort the array, we do not need to delete elements smaller than nums[idx] (ie nums[idx] - 1) because they are already computed
        // and saved in memo, we only need to delete nums[idx] + 1 and we can do this simply by skipping them since the array is sorted
        Arrays.sort(nums);
        return deleteAndEarn(nums, 0, new int[nums.length]);
    }

    private int deleteAndEarn(int[] nums, int idx, int[] memo) {
        // if we reached the end of the array, we can not earn anymore, return 0
        if (idx == nums.length)
            return 0;

        if (memo[idx] == 0) {
            // delete and earn this element
            int earned = nums[idx];
            int skip = idx + 1;

            // simply add all similar values of nums[idx] to earned at once
            while (skip < nums.length && nums[skip] == nums[idx]) {
                earned += nums[idx];
                skip++;
            }

            // skip all elements = nums[idx] + 1
            // this is instead of deleting the elements = nums[idx] + 1
            // which does not alter the array and make the solution work
            while (skip < nums.length && nums[skip] == nums[idx] + 1)
                skip++;

            // recurse
            earned += deleteAndEarn(nums, skip, memo);

            // skip deleting and earning this element
            int skipped = deleteAndEarn(nums, idx + 1, memo);

            // return the max of the 2 values
            memo[idx] = Math.max(earned, skipped);
        }

        return memo[idx];
    }


    // V3
    // IDEA: DP
    // https://leetcode.com/problems/delete-and-earn/solutions/1820924/java-simple-explained-by-prashant404-nvj8/
    /** Variable Description:
     *
     *  numToCount = map of nums[i] to their count
     *  min = min number in nums
     *  max = max number in nums
     *  prevIncEarn = Earning if previous num is excluded (deleted)
     *  prevExcEarn = Earning if previous num is included (not-deleted)
     *  incEarn = Earning if this num is excluded (deleted)
     *  excEarn = Earning if this num is included (not-deleted)
     */
    public int deleteAndEarn_3(int[] nums) {
        HashMap numToCount = new HashMap<Integer, Integer>();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
           // TODO: fix below syntax for java 8
           // numToCount.compute(num, (k, v) -> v == null ? 1 : ++v);
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        int prevIncEarn = 0;
        int prevExcEarn = 0;
        for (int i = min; i <= max; i++) {
            // TODO: fix below syntax for java 8
            //int incEarn = prevExcEarn + i * numToCount.getOrDefault(i, 0);
            int incEarn = 0;
            int excEarn = Math.max(prevIncEarn, prevExcEarn);
            prevIncEarn = incEarn;
            prevExcEarn = excEarn;
        }
        return Math.max(prevIncEarn, prevExcEarn);
    }


    // V4


}
