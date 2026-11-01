package LeetCodeJava.HashTable;

// https://leetcode.com/problems/partition-array-into-k-distinct-groups/description/

import java.util.*;

/**
 * 3659. Partition Array Into K-Distinct Groups
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * Your task is to determine whether it is possible to partition all elements
 * of nums into one or more groups such that:
 *
 * Each group contains exactly k elements.
 *
 * All elements in each group are distinct.
 *
 * Each element in nums must be assigned to exactly one group.
 *
 * Return true if such a partition is possible, otherwise return false.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], k = 2
 * Output: true
 * Explanation:
 * One possible partition is to have 2 groups:
 * Group 1: [1, 2]
 * Group 2: [3, 4]
 * Each group contains k = 2 distinct elements, and all elements are used
 * exactly once.
 *
 * Example 2:
 *
 * Input: nums = [3,5,2,2], k = 2
 * Output: true
 * Explanation:
 * One possible partition is to have 2 groups:
 * Group 1: [2, 3]
 * Group 2: [2, 5]
 * Each group contains k = 2 distinct elements, and all elements are used
 * exactly once.
 *
 * Example 3:
 *
 * Input: nums = [1,5,2,3], k = 3
 * Output: false
 * Explanation:
 * We cannot form groups of k = 3 distinct elements using all values exactly
 * once.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 * ^1 <= k <= nums.length
 *
 */
public class PartitionArrayIntoKDistinctGroups {

    // V0
    // IDEA: COUNTING ARGUMENT - n % k == 0 AND EVERY FREQUENCY <= n / k
    //   there are exactly n / k groups, and a value appearing c times needs c
    //   different groups, so c <= n / k is necessary. it is also sufficient:
    //   sort the values and deal them round-robin into the groups - equal values
    //   land in different groups as long as c <= n / k.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean partitionArray(int[] nums, int k) {
        int n = nums.length;
        if (n % k != 0) {
            return false;
        }
        int groups = n / k;

        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            int c = count.getOrDefault(num, 0) + 1;
            if (c > groups) {
                return false;
            }
            count.put(num, c);
        }
        return true;
    }
}
