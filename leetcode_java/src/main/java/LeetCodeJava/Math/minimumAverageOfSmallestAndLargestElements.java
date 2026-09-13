package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-average-of-smallest-and-largest-elements/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 3194. Minimum Average of Smallest and Largest Elements
 * Easy
 *
 * You have an array of floating point numbers averages which is initially empty. You are given an array nums of n integers where n is even.
 *
 * You repeat the following procedure n / 2 times:
 *
 * Remove the smallest element, minElement, and the largest element maxElement, from nums.
 * Add (minElement + maxElement) / 2 to averages.
 *
 * Return the minimum element in averages.
 *
 * Example 1:
 *
 * Input: nums = [7,8,3,4,15,13,4,1]
 * Output: 5.5
 * Explanation:
 *
 * step
 * nums
 * averages
 *
 * 0 | [7,8,3,4,15,13,4,1] | []
 *
 * 1 | [7,8,3,4,13,4] | [8]
 *
 * 2 | [7,8,4,4] | [8,8]
 *
 * 3 | [7,4] | [8,8,6]
 *
 * 4 | [] | [8,8,6,5.5]
 *
 * The smallest element of averages, 5.5, is returned.
 *
 * Example 2:
 *
 * Input: nums = [1,9,8,3,10,5]
 * Output: 5.5
 * Explanation:
 *
 * step
 * nums
 * averages
 *
 * 0 | [1,9,8,3,10,5] | []
 *
 * 1 | [9,8,3,5] | [5.5]
 *
 * 2 | [8,5] | [5.5,6]
 *
 * 3 | [] | [5.5,6,6.5]
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,7,8,9]
 * Output: 5.0
 * Explanation:
 *
 * step
 * nums
 * averages
 *
 * 0 | [1,2,3,7,8,9] | []
 *
 * 1 | [2,3,7,8] | [5]
 *
 * 2 | [3,7] | [5,5]
 *
 * 3 | [] | [5,5,5]
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 50
 * n is even.
 * 1 <= nums[i] <= 50
 *
 */
public class minimumAverageOfSmallestAndLargestElements {

    // V0

    // IDEA : MATH
    /**
     * time = O(N)
     * space = O(1)
     */
    public double minimumAverage(int[] nums) {

        if (nums.length == 1){
            return nums[0];
        }

        List<Float> collected = new ArrayList<>();

        // sort, in increasing order
        System.out.println("before sort : " + Arrays.toString(nums));
        Arrays.sort(nums);
        System.out.println("after sort : " + Arrays.toString(nums));

        while(nums.length > 0){
            int max_ = nums[nums.length-1];
            int min_ = nums[0];
            nums = Arrays.copyOfRange(nums, 1, nums.length-1);
            float val = (float) (max_ + min_) / 2; // ?
            collected.add(val);
        }

        System.out.println("(before) collected  = " + collected);
        collected.sort((a,b) -> {
            if (a > b){
                return 1;
            }else if (a < b){
                return -1;
            }
            return 0;
        });

        System.out.println("(after) collected  = " + collected);
        // return collected.stream().min();
        return collected.get(0);
    }

}
