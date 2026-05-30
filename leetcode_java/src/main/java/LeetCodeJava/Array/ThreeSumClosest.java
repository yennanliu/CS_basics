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
    // IDEA: SORT + 3 POINTERS (i, l, r)
    public int threeSumClosest(int[] nums, int target) {
        if (nums.length == 3) {
            return nums[0] + nums[1] + nums[2]; // ???
        }

        // sort ??? (small -> big ??)
        Arrays.sort(nums);

        int res = nums[0] + nums[1] + nums[2]; // ???

        /**  NOTE !!!
         *
         *  we define 3 POINTERS
         *
         *  -> i, l, r
         *
         *  [x,x,x,x... x,x, a,b]
         *   i
         *     l               r
         *
         *
         *  (i is in [0, nums.len -2] range)
         *
         */
        for (int i = 0; i < nums.length - 2; i++) {
            int l = i + 1; // ???
            int r = nums.length - 1; // ???
            // ???
            while (r > l) {
                int cur = nums[i] + nums[l] + nums[r]; // ??
                // NOTE !!!
                // ONLY update res, when `cur` is
                // more closer (the distance) to target
                if (Math.abs(res - target) > Math.abs(cur - target)) {
                    res = cur;
                }

                if (cur == target) {
                    return cur;
                }
                if (cur > target) {
                    r -= 1;
                } else {
                    l += 1;
                }
            }
        }

        return res;
    }



    // V0-1
    // IDEA: SORT + 3 POINTERS (gpt)
    /**
     *
     * but LC 16 requires checking `3 numbers together`.
     *
     *  The standard solution is:
     *
     * 1. Sort the array
     * 2. Fix one number i
     * 3. Use two pointers (l, r) for the remaining part
     * 4. Track the closest sum
     *
     */
    /**  Dry run:
     *
     * ==============================================================================================================================
     * |  i  |  l  |  r  |  Current Array Snapshot  |  currentSum Calculation  |  Distance  |  closestSum Update  |     Action Taken      |
     * ==============================================================================================================================
     * |  0  |  1  |  3  |  [-4, -1,  1,  2]        |  -4 + (-1) + 2 = -3      |  |-3 - 1|  |  Updates to -3      |  currentSum < 1 -> l++|
     * |     |     |     |   ^   ^        ^         |                          |    = 4     |                     |                       |
     * |     |     |     |   i   l        r         |                          |            |                     |                       |
     * ------------------------------------------------------------------------------------------------------------------------------
     * |  0  |  2  |  3  |  [-4, -1,  1,  2]        |  -4 + 1 + 2 = -1         |  |-1 - 1|  |  Updates to -1      |  currentSum < 1 -> l++|
     * |     |     |     |   ^        ^   ^         |                          |    = 2     |                     |                       |
     * |     |     |     |   i        l   r         |                          |            |                     |                       |
     * ------------------------------------------------------------------------------------------------------------------------------
     * |  0  |  3  |  3  |  [-4, -1,  1,  2]        |  *Pointers Collide* |     --     |  No Change (-1)     |  Loop breaks          |
     * |     |     |     |   ^            ^         |                          |            |                     |  (l < r fails)        |
     * |     |     |     |   i           l,r        |                          |            |                     |                       |
     * ------------------------------------------------------------------------------------------------------------------------------
     * |  1  |  2  |  3  |  [-4, -1,  1,  2]        |  -1 + 1 + 2 = 2          |  |2 - 1|   |  Updates to 2       |  currentSum > 1 -> r--|
     * |     |     |     |       ^    ^   ^         |                          |    = 1     |                     |                       |
     * |     |     |     |       i    l   r         |                          |            |                     |                       |
     * ------------------------------------------------------------------------------------------------------------------------------
     * |  1  |  2  |  2  |  [-4, -1,  1,  2]        |  *Pointers Collide* |     --     |  Final Ans = 2      |  Loop breaks          |
     * |     |     |     |       ^    ^             |                          |            |                     |  (l < r fails)        |
     * |     |     |     |       i   l,r            |                          |            |                     |                       |
     * ==============================================================================================================================
     *
     *
     */
    public int threeSumClosest_0_1(int[] nums, int target) {
        Arrays.sort(nums);

        int closest = nums[0] + nums[1] + nums[2];

        /** NOTE !!!
         *
         *  i starts from 0 and end at `nums.length - 2`
         *
         */
        for (int i = 0; i < nums.length - 2; i++) {

            /** NOTE !!!
             *
             *   l = i + 1
             *   r = nums.len - 1
             *
             */
            int l = i + 1;
            int r = nums.length - 1;

            /** NOTE !!!
             *
             *  while(l < r) loop
             *
             */
            while (l < r) {

                int sum = nums[i] + nums[l] + nums[r];

                // update closest
                if (Math.abs(sum - target) < Math.abs(closest - target)) {
                    closest = sum;
                }

                // exact match
                if (sum == target) {
                    return sum;
                }
                // need smaller sum
                else if (sum > target) {
                    r--;
                }
                // need bigger sum
                else {
                    l++;
                }
            }
        }

        return closest;
    }


    // V0-2
    // IDEA:  SORT + 3 POINTERS  (GEMINI)
    public int threeSumClosest_0_2(int[] nums, int target) {
        // Step 1: Sort the array to enable directional two-pointer adjustments
        Arrays.sort(nums);

        // Initialize our closest sum with the first possible combination triplet
        int closestSum = nums[0] + nums[1] + nums[2];
        int n = nums.length;

        // Step 2: Set an anchor pivot pointer 'i'
        for (int i = 0; i < n - 2; i++) {
            // Two pointers initialization on the remaining sub-array segment
            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                // Calculate the true 3-sum combination using our anchor and pointers
                int currentSum = nums[i] + nums[left] + nums[right];

                // If we hit the exact target, return immediately
                if (currentSum == target) {
                    return currentSum;
                }

                // If the current combination is closer to the target than our previous best, update it
                if (Math.abs(currentSum - target) < Math.abs(closestSum - target)) {
                    closestSum = currentSum;
                }

                // Adjust pointers based on how our sum compares to the target
                if (currentSum > target) {
                    right--; // Sum is too big, move right pointer left to find smaller numbers
                } else {
                    left++; // Sum is too small, move left pointer right to find larger numbers
                }
            }
        }

        return closestSum;
    }



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
