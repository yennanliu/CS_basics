package LeetCodeJava.Array;

// https://leetcode.com/problems/4sum/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  18. 4Sum
 *  Medium
 *
 *  Given an array nums of n integers, return an array of all the unique
 *  quadruplets [nums[a], nums[b], nums[c], nums[d]] such that:
 *
 *   - 0 <= a, b, c, d < n
 *   - a, b, c, and d are distinct
 *   - nums[a] + nums[b] + nums[c] + nums[d] == target
 *
 *  You may return the answer in any order.
 *
 *  Example 1:
 *   Input: nums = [1,0,-1,0,-2,2], target = 0
 *   Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 *
 *  Example 2:
 *   Input: nums = [2,2,2,2,2], target = 8
 *   Output: [[2,2,2,2]]
 *
 *  Constraints:
 *   1 <= nums.length <= 200
 *   -10^9 <= nums[i] <= 10^9
 *   -10^9 <= target <= 10^9
 */
public class FourSum {

    // V0
    // IDEA: SORT + FIX 2 INDEXES + 2 POINTERS ON THE REST, SKIP DUPLICATES
    /**
     * time = O(n^3)
     * space = O(1) (excluding output, sorting stack ignored)
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 4) {
            return res;
        }

        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n - 3; i++) {
            // skip duplicated 1st element
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < n - 2; j++) {
                // skip duplicated 2nd element
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                int left = j + 1;
                int right = n - 1;
                while (left < right) {
                    // NOTE !!! use long, since sum may overflow int
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        List<Integer> cur = new ArrayList<>();
                        cur.add(nums[i]);
                        cur.add(nums[j]);
                        cur.add(nums[left]);
                        cur.add(nums[right]);
                        res.add(cur);

                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return res;
    }
}
