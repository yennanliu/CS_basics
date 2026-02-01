package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximize-greatness-of-an-array/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2592. Maximize Greatness of an Array
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums. You are allowed to permute nums into a new array perm of your choosing.
 *
 * We define the greatness of nums be the number of indices 0 <= i < nums.length for which perm[i] > nums[i].
 *
 * Return the maximum possible greatness you can achieve after permuting nums.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,2,1,3,1]
 * Output: 4
 * Explanation: One of the optimal rearrangements is perm = [2,5,1,3,3,1,1].
 * At indices = 0, 1, 3, and 4, perm[i] > nums[i]. Hence, we return 4.
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: 3
 * Explanation: We can prove the optimal perm is [2,3,4,1].
 * At indices = 0, 1, and 2, perm[i] > nums[i]. Hence, we return 3.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 *
 */
public class MaximizeGreatnessOfAnArray {

    // V0
//    public int maximizeGreatness(int[] nums) {
//
//    }

    // V1
    // IDEA: TREE MAP + 2 POINTERS (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximizeGreatness_1(int[] nums) {
        if (nums == null || nums.length <= 1)
            return 0;

        // count occurrences
        /** NOTE !!!
         *
         *  use `TreeMap` for using the `higherKey` method
         */
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int count = 0;

        // iterate over nums (doesn’t matter if sorted or not)
        for (int x : nums) {
            // find the smallest available number > x
            Integer higher = map.higherKey(x);
            if (higher != null) {
                count++;
                // decrease frequency
                map.put(higher, map.get(higher) - 1);
                if (map.get(higher) == 0) {
                    map.remove(higher);
                }
            }
        }

        return count;
    }

    // V2-1
    // IDEA: SORT
    // https://leetcode.com/problems/maximize-greatness-of-an-array/solutions/3312061/javacpython-easy-and-concise-on-by-lee21-y92l/
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximizeGreatness_2_1(int[] A) {
        Arrays.sort(A);
        int res = 0;
        for (int a : A)
            if (a > A[res])
                res++;
        return res;
    }

    // V2-2
    // IDEA: hashmap + greedy
    // https://leetcode.com/problems/maximize-greatness-of-an-array/solutions/3312061/javacpython-easy-and-concise-on-by-lee21-y92l/
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximizeGreatness_2_2(int[] A) {
        Map<Integer, Integer> count = new HashMap<Integer, Integer>();
        int k = 0;
        for (int a : A)
            k = Math.max(k, count.merge(a, 1, Integer::sum));
        return A.length - k;
    }

    // V3
    // https://leetcode.com/problems/maximize-greatness-of-an-array/solutions/3311993/easy-java-solution-by-1asthakhushi1-1hmy/
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximizeGreatness_3(int[] nums) {
        Arrays.sort(nums);
        if (nums.length == 1)
            return 0;
        int count = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[count])
                count++;
        }
        return count;
    }


}
