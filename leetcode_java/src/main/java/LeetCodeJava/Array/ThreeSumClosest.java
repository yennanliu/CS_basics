package LeetCodeJava.Array;

// https://leetcode.com/problems/3sum-closest/description/

import java.util.Arrays;

/**
 *  16. 3Sum Closest
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums of length n and an integer target, find three integers at distinct indices in nums such that the sum is closest to target.
 *
 * Return the sum of the three integers.
 *
 * You may assume that each input would have exactly one solution.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [-1,2,1,-4], target = 1
 * Output: 2
 * Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 * Example 2:
 *
 * Input: nums = [0,0,0], target = 1
 * Output: 0
 * Explanation: The sum that is closest to the target is 0. (0 + 0 + 0 = 0).
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 500
 * -1000 <= nums[i] <= 1000
 * -104 <= target <= 104
 *
 *
 *
 */
public class ThreeSumClosest {

    // V0
//    public int threeSumClosest(int[] nums, int target) {
//
//    }

    // V1
    // https://leetcode.com/problems/3sum-closest/solutions/6830683/using-two-pointer-easiest-explanation-an-r7q0/
    public int threeSumClosest_1(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int result = nums[0] + nums[1] + nums[2]; // Initial best guess

        for (int i = 0; i < n - 2; i++) {
            int left = i + 1, right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (Math.abs(target - sum) < Math.abs(target - result)) {
                    result = sum;
                }

                if (sum == target)
                    return target;
                else if (sum < target)
                    left++;
                else
                    right--;
            }
        }

        return result;
    }


    // V2-1
    // https://leetcode.com/problems/3sum-closest/solutions/5446457/2-ways-to-the-point-explanation-java-c-p-26mo/
    public int threeSumClosest_2_1(int[] nums, int target) {
        Arrays.sort(nums);
        int closest_sum = Integer.MAX_VALUE / 2; // A large value but not overflow

        for (int i = 0; i < nums.length - 2; ++i) {
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int current_sum = nums[i] + nums[left] + nums[right];
                if (Math.abs(current_sum - target) < Math.abs(closest_sum - target)) {
                    closest_sum = current_sum;
                }
                if (current_sum < target) {
                    ++left;
                } else if (current_sum > target) {
                    --right;
                } else {
                    return current_sum;
                }
            }
        }

        return closest_sum;
    }


    // V2-2
    // https://leetcode.com/problems/3sum-closest/solutions/5446457/2-ways-to-the-point-explanation-java-c-p-26mo/
    public int threeSumClosest_2_2(int[] nums, int target) {
        int closest_sum = Integer.MAX_VALUE / 2; // A large value but not overflow

        for (int i = 0; i < nums.length - 2; ++i) {
            for (int j = i + 1; j < nums.length - 1; ++j) {
                for (int k = j + 1; k < nums.length; ++k) {
                    int current_sum = nums[i] + nums[j] + nums[k];
                    if (Math.abs(current_sum - target) < Math.abs(closest_sum - target)) {
                        closest_sum = current_sum;
                    }
                }
            }
        }

        return closest_sum;
    }




    // V3
    // https://leetcode.com/problems/3sum-closest/solutions/8288266/3sum-closest-solution-by-yagnikaadarsh5-9gqg/
    public int threeSumClosest_3(int[] nums, int target) {
        Arrays.sort(nums);
        int ans = 0;
        int minDiff = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length - 2; i++) {
            int left = i + 1;
            int right = nums.length - 1;

            if (i > 0 && nums[i] == nums[i - 1])
                continue;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                int diff = Math.abs(target - sum);
                if (sum == target) {
                    return sum;
                } else if (sum < target) {
                    if (diff < minDiff) {
                        minDiff = diff;
                        ans = sum;
                    }
                    left++;
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                } else {
                    if (diff < minDiff) {
                        minDiff = diff;
                        ans = sum;
                    }

                    right--;
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                }
            }
        }

        return ans;
    }







}
