package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sum-of-distances/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *   2615. Sum of Distances
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums. There exists an array arr of length nums.length, where arr[i] is the sum of |i - j| over all j such that nums[j] == nums[i] and j != i. If there is no such j, set arr[i] to be 0.
 *
 * Return the array arr.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,1,1,2]
 * Output: [5,0,3,4,0]
 * Explanation:
 * When i = 0, nums[0] == nums[2] and nums[0] == nums[3]. Therefore, arr[0] = |0 - 2| + |0 - 3| = 5.
 * When i = 1, arr[1] = 0 because there is no other index with value 3.
 * When i = 2, nums[2] == nums[0] and nums[2] == nums[3]. Therefore, arr[2] = |2 - 0| + |2 - 3| = 3.
 * When i = 3, nums[3] == nums[0] and nums[3] == nums[2]. Therefore, arr[3] = |3 - 0| + |3 - 2| = 4.
 * When i = 4, arr[4] = 0 because there is no other index with value 2.
 *
 * Example 2:
 *
 * Input: nums = [0,5,3]
 * Output: [0,0,0]
 * Explanation: Since each element in nums is distinct, arr[i] = 0 for all i.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 *
 */
public class SumOfDistances {

    // V0
//    public long[] distance(int[] nums) {
//
//    }

    // V1

    // V2

    // V3
    // IDEA: HASHMAP
    // https://leetcode.com/problems/sum-of-distances/solutions/3395683/why-leetcode-why-easiest-solution-by-har-oq8f/
    public long[] distance_3(int[] arr) {
        long[] output = new long[arr.length];
        Map<Integer, Long> sumMap = new HashMap<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i = 0; i < arr.length; ++i) {
            if (!sumMap.containsKey(arr[i])) {
                sumMap.put(arr[i], 0l);
                countMap.put(arr[i], 0);
            }

            output[i] += i * (long) countMap.get(arr[i]) - sumMap.get(arr[i]);
            sumMap.put(arr[i], sumMap.get(arr[i]) + i);
            countMap.put(arr[i], countMap.get(arr[i]) + 1);
        }

        sumMap = new HashMap<>();
        countMap = new HashMap<>();
        int len = arr.length;
        for (int i = len - 1; i >= 0; --i) {
            if (!sumMap.containsKey(arr[i])) {
                sumMap.put(arr[i], 0l);
                countMap.put(arr[i], 0);
            }

            output[i] += (len - i - 1) * (long) countMap.get(arr[i]) - sumMap.get(arr[i]);
            sumMap.put(arr[i], sumMap.get(arr[i]) + (len - i - 1));
            countMap.put(arr[i], countMap.get(arr[i]) + 1);
        }

        return output;
    }


    // V4
    // https://leetcode.com/problems/sum-of-distances/solutions/5810176/good-to-go-by-dixon_n-vn44/
    public long[] distance_4(int[] nums) {
        HashMap<Integer, ArrayList<Integer>> hm = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (!hm.containsKey(nums[i])) {
                hm.put(nums[i], new ArrayList<>());
            }
            hm.get(nums[i]).add(i);
        }

        long[] ans = new long[nums.length];
        for (ArrayList<Integer> indices : hm.values()) {
            int n = indices.size();
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += indices.get(i);
            }
            long leftsum = 0;
            long rightsum = sum;
            long currsum = 0;
            for (int i = 0; i < n; i++) {
                currsum = 0;
                currsum += (long) i * (long) indices.get(i) - leftsum;
                currsum += rightsum - (long) (n - i) * (long) indices.get(i);
                leftsum += (long) indices.get(i);
                rightsum -= (long) indices.get(i);
                ans[indices.get(i)] = currsum;
            }
        }
        return ans;
    }


    // V5





}
