package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-harmonious-subsequence/description/

import java.util.*;

/**
 * 594. Longest Harmonious Subsequence
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * We define a harmonious array as an array where the difference between its maximum value and its minimum value is exactly 1.
 *
 * Given an integer array nums, return the length of its longest harmonious subsequence among all its possible subsequences.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,2,5,2,3,7]
 *
 * Output: 5
 *
 * Explanation:
 *
 * The longest harmonious subsequence is [3,2,2,2,3].
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 *
 * Output: 2
 *
 * Explanation:
 *
 * The longest harmonious subsequences are [1,2], [2,3], and [3,4], all of which have a length of 2.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1]
 *
 * Output: 0
 *
 * Explanation:
 *
 * No harmonic subsequence exists.
 *
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 104
 * -109 <= nums[i] <= 109
 *
 */
public class LongestHarmoniousSubsequence {

    // V0
    // IDEA: HASHMAP
    public int findLHS(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        HashSet<Integer> set = new HashSet<>();
        // map: { val: [idx1, idx2,...]}
        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int k = nums[i];
            set.add(k);
            List<Integer> list = new ArrayList<>();
            if (map.containsKey(k)) {
                list = map.get(k);
            }
            list.add(i);
            map.put(k, list);
        }

        int maxSubLen = 0;

        //System.out.println(">>> map = " + map);

        if (map.keySet().size() <= 1) {
            return 0;
        }

        for (Integer x : set) {
            int bigger = x + 1;
            int smaller = x - 1;
            if (map.containsKey(bigger)) {
                maxSubLen = Math.max(maxSubLen, map.get(bigger).size() + map.get(x).size());
            }
            if (map.containsKey(smaller)) {
                maxSubLen = Math.max(maxSubLen, map.get(smaller).size() + map.get(x).size());
            }
        }

        return maxSubLen;
    }


    // V1
    // IDEA: HASH MAP
    // https://leetcode.ca/2017-07-16-594-Longest-Harmonious-Subsequence/
    public int findLHS_1(int[] nums) {
        Map<Integer, Integer> counter = new HashMap<>();
        for (int num : nums) {
            counter.put(num, counter.getOrDefault(num, 0) + 1);
        }
        int ans = 0;
        for (int num : nums) {
            if (counter.containsKey(num + 1)) {
                ans = Math.max(ans, counter.get(num) + counter.get(num + 1));
            }
        }
        return ans;
    }

    // V2-1
    // IDEA: SORT + ARRAY
    // https://leetcode.com/problems/longest-harmonious-subsequence/solutions/6899953/beginner-freindlyjavacpythonjs-by-ashokv-7can/
    public int findLHS_2_1(int[] nums) {
        Arrays.sort(nums);
        int j = 0;
        int maxLength = 0;

        for (int i = 0; i < nums.length; i++) {
            while (nums[i] - nums[j] > 1) {
                j++;
            }
            if (nums[i] - nums[j] == 1) {
                maxLength = Math.max(maxLength, i - j + 1);
            }
        }
        return maxLength;
    }

    // V2-2
    // IDEA: HASHMAP
    // https://leetcode.com/problems/longest-harmonious-subsequence/solutions/6899953/beginner-freindlyjavacpythonjs-by-ashokv-7can/
    public int findLHS_2_2(int[] nums) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Step 1: Count frequencies of each number
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int maxLength = 0;

        // Step 2: Check all possible (num, num+1) pairs
        for (int num : frequencyMap.keySet()) {
            if (frequencyMap.containsKey(num + 1)) {
                int currentLength = frequencyMap.get(num) + frequencyMap.get(num + 1);
                maxLength = Math.max(maxLength, currentLength);
            }
        }
        return maxLength;
    }


}
