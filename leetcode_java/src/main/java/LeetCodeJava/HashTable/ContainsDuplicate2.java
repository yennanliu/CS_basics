package LeetCodeJava.HashTable;

// https://leetcode.com/problems/contains-duplicate-ii/description/

import java.util.*;

/**
 * 219. Contains Duplicate II
 * Solved
 * Easy
 * Topics
 * Companies
 * Given an integer array nums and an integer k, return true if there are two distinct indices i and j in the array such that nums[i] == nums[j] and abs(i - j) <= k.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1], k = 3
 * Output: true
 * Example 2:
 *
 * Input: nums = [1,0,1,1], k = 1
 * Output: true
 * Example 3:
 *
 * Input: nums = [1,2,3,1,2,3], k = 2
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 * 0 <= k <= 105
 *
 */
public class ContainsDuplicate2 {

    // V0
    // IDEA: HASHMAP + BRUTR FORCE
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return false; // ??
        }
        if (nums.length == 1) {
            return false;
        }
        // init map: {val: [idx1, idx2, ...]
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            List<Integer> cur = map.getOrDefault(nums[i], new ArrayList<>());
            cur.add(i);
            map.put(nums[i], cur);
        }

        //System.out.println(">>> map = " + map);
        for (int i = 0; i < nums.length; i++) {
            int val = nums[i];
            if (map.containsKey(val)) {
                // TODO: optimize below
                for (int x : map.get(val)) {
                    if (Math.abs(x - i) <= k && x != i) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // V1
    // IDEA: SET
    // https://leetcode.com/problems/contains-duplicate-ii/solutions/61372/simple-java-solution-by-southpenguin-ruxe/
    public boolean containsNearbyDuplicate_1(int[] nums, int k) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (i > k)
                set.remove(nums[i - k - 1]);
            if (!set.add(nums[i]))
                return true;
        }
        return false;
    }

    // V2
    // IDEA: SET
    // https://leetcode.com/problems/contains-duplicate-ii/solutions/2463150/very-easy-100-fully-explained-java-c-pyt-mhkz/
    public boolean containsNearbyDuplicate_2(int[] nums, int k) {
        // Base case...
        if (nums == null || nums.length < 2 || k == 0)
            return false;
        int i = 0;
        // Create a Hash Set for storing previous of k elements...
        HashSet<Integer> hset = new HashSet<Integer>();
        // Traverse for all elements of the given array in a for loop...
        for (int j = 0; j < nums.length; j++) {
            // If duplicate element is present at distance less than equal to k, return
            // true...
            if (!hset.add(nums[j])) {
                return true;
            }
            // If size of the Hash Set becomes greater than k...
            if (hset.size() >= k + 1) {
                // Remove the last visited element from the set...
                hset.remove(nums[i++]);
            }
        }
        // If no duplicate element is found then return false...
        return false;
    }

}
