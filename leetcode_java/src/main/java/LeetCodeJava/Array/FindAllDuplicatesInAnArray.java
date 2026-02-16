package LeetCodeJava.Array;

// https://leetcode.com/problems/find-all-duplicates-in-an-array/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 442. Find All Duplicates in an Array
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums of length n where all the integers of nums are in the range [1, n] and each integer appears at most twice, return an array of all the integers that appears twice.
 *
 * You must write an algorithm that runs in O(n) time and uses only constant auxiliary space, excluding the space needed to store the output
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,2,7,8,2,3,1]
 * Output: [2,3]
 * Example 2:
 *
 * Input: nums = [1,1,2]
 * Output: [1]
 * Example 3:
 *
 * Input: nums = [1]
 * Output: []
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 105
 * 1 <= nums[i] <= n
 * Each element in nums appears once or twice.
 *
 */
public class FindAllDuplicatesInAnArray {

    // V0
    // IDEA: HASHMAP
    public List<Integer> findDuplicates(int[] nums) {
        // edge
        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        List<Integer> res = new ArrayList<>();
        for(int k: map.keySet()){
            if(map.get(k) > 1){
                res.add(k);
            }
        }

        return res;
    }

    // V1
    // https://leetcode.com/problems/find-all-duplicates-in-an-array/solutions/4921063/utilizing-integer-range-for-duplicate-id-cah0/
    public List<Integer> findDuplicates_1(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int x = Math.abs(nums[i]);
            if (nums[x - 1] < 0) {
                ans.add(x);
            }
            nums[x - 1] *= -1;
        }
        return ans;
    }

    // V2
    public List<Integer> findDuplicates_2(int[] nums) {
        final List<Integer> duplicates = new ArrayList<Integer>();
        for (int i = 0; i < nums.length; i++) {
            final int n = Math.abs(nums[i]);
            if (nums[n - 1] < 0) {
                duplicates.add(n);
            } else {
                nums[n - 1] *= -1;
            }
        }
        return duplicates;
    }


    // V3



}
