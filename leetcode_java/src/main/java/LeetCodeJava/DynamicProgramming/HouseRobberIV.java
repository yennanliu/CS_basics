package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/house-robber-iv/description/

import java.util.Arrays;

/**
 *  2560. House Robber IV
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are several consecutive houses along a street, each of which has some money inside. There is also a robber, who wants to steal money from the homes, but he refuses to steal from adjacent homes.
 *
 * The capability of the robber is the maximum amount of money he steals from one house of all the houses he robbed.
 *
 * You are given an integer array nums representing how much money is stashed in each house. More formally, the ith house from the left has nums[i] dollars.
 *
 * You are also given an integer k, representing the minimum number of houses the robber will steal from. It is always possible to steal at least k houses.
 *
 * Return the minimum capability of the robber out of all the possible ways to steal at least k houses.
 *
 *  Example 1:
 *
 * Input: nums = [2,3,5,9], k = 2
 * Output: 5
 * Explanation:
 * There are three ways to rob at least 2 houses:
 * - Rob the houses at indices 0 and 2. Capability is max(nums[0], nums[2]) = 5.
 * - Rob the houses at indices 0 and 3. Capability is max(nums[0], nums[3]) = 9.
 * - Rob the houses at indices 1 and 3. Capability is max(nums[1], nums[3]) = 9.
 * Therefore, we return min(5, 9, 9) = 5.
 * Example 2:
 *
 * Input: nums = [2,7,9,3,1], k = 2
 * Output: 2
 * Explanation: There are 7 ways to rob the houses. The way which leads to minimum capability is to rob the house at index 0 and 4. Return max(nums[0], nums[4]) = 2.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 * 1 <= k <= (nums.length + 1)/2
 *
 */
public class HouseRobberIV {

    // V0
//    public int minCapability(int[] nums, int k) {
//
//    }

    // V1
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/house-robber-iv/editorial/
    public int minCapability_1(int[] nums, int k) {
        // Store the maximum nums value in maxReward.
        int minReward = 1;
        int maxReward = Arrays.stream(nums).max().getAsInt();
        int totalHouses = nums.length;

        // Use binary search to find the minimum reward possible.
        while (minReward < maxReward) {
            int midReward = (minReward + maxReward) / 2;
            int possibleThefts = 0;

            for (int index = 0; index < totalHouses; ++index) {
                if (nums[index] <= midReward) {
                    possibleThefts += 1;
                    index++; // Skip the next house to maintain the
                    // non-adjacent condition
                }
            }

            if (possibleThefts >= k)
                maxReward = midReward;
            else
                minReward = midReward + 1;
        }

        return minReward;
    }


    // V2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/house-robber-iv/solutions/3143697/javacpython-binary-search-o1-space-by-le-nylu/
    public int minCapability_2(int[] A, int k) {
        int left = 1, right = (int) 1e9, n = A.length;
        while (left < right) {
            int mid = (left + right) / 2, take = 0;
            for (int i = 0; i < n; ++i)
                if (A[i] <= mid) {
                    take += 1;
                    i++;
                }
            if (take >= k)
                right = mid;
            else
                left = mid + 1;
        }
        return left; //left == right
    }


    // V3
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/house-robber-iv/solutions/6537473/binary-search-python-c-java-js-c-go-by-o-tfv7/
    private boolean canStealKHouses(int[] nums, int k, int capability) {
        int count = 0;
        int i = 0;
        while (i < nums.length) {
            if (nums[i] <= capability) {
                count++;
                i += 2;
            } else {
                i++;
            }
        }
        return count >= k;
    }

    public int minCapability_3(int[] nums, int k) {
        int left = Arrays.stream(nums).min().getAsInt();
        int right = Arrays.stream(nums).max().getAsInt();

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canStealKHouses(nums, k, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // V4
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/house-robber-iv/solutions/6537753/binary-search-visualization-math-python-26vxz/
    public boolean canRob(int[] nums, int mid, int k) {
        int count = 0, n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] <= mid) {
                count++;
                i++;
            }
        }
        return count >= k;
    }

    public int minCapability_4(int[] nums, int k) {
        int left = 1, right = Arrays.stream(nums).max().getAsInt(), ans = right;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (canRob(nums, mid, k)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }



}
