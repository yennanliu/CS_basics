package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sum-of-distances/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    // V0-1
    // IDEA: PREFIX SUM (gemini)
    public long[] distance_0_1(int[] nums) {
        int n = nums.length;
        long[] res = new long[n];
        Map<Integer, List<Integer>> map = new HashMap<>();

        // 1. Group indices by value
        for (int i = 0; i < n; i++) {
            map.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }

        // 2. For each unique value, calculate distance sums in O(K)
        for (List<Integer> indices : map.values()) {
            int k = indices.size();
            if (k == 1)
                continue; // Distance sum for a single element is 0

            // Calculate total sum of indices for the "after" part
            long totalSum = 0;
            for (int idx : indices)
                totalSum += idx;

            long prefixSum = 0;
            for (int i = 0; i < k; i++) {
                long currentIdx = indices.get(i);

                // Numbers before currentIdx
                long leftCount = i;
                long leftSum = prefixSum;

                // Numbers after currentIdx (including itself)
                long rightCount = k - i;
                long rightSum = totalSum - prefixSum;

                // Distance formula:
                // (CountBefore * val - SumBefore) + (SumAfter - CountAfter * val)
                res[(int) currentIdx] = (leftCount * currentIdx - leftSum) +
                        (rightSum - rightCount * currentIdx);

                prefixSum += currentIdx;
            }
        }

        return res;
    }


    // V0-2
    // IDEA: HASHMAP (TLE) (gpt)
    public long[] distance_0_2(int[] nums) {

        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            map.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }

        long[] res = new long[nums.length];

        for (int i = 0; i < nums.length; i++) {
            res[i] = getDistSum(map.get(nums[i]), i);
        }

        return res;
    }

    private long getDistSum(List<Integer> list, int idx) {
        long res = 0;

        for (int j : list) {
            res += Math.abs(idx - j);
        }

        return res;
    }


    // V0-3
    // IDEA: PREFIX SUM + HASHMAP (gpt)
    public long[] distance_0_3(int[] nums) {

        int n = nums.length;
        long[] res = new long[n];

        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            map.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }

        for (List<Integer> list : map.values()) {

            int m = list.size();
            long[] prefix = new long[m];

            prefix[0] = list.get(0);
            for (int i = 1; i < m; i++) {
                prefix[i] = prefix[i - 1] + list.get(i);
            }

            for (int i = 0; i < m; i++) {

                int idx = list.get(i);

                long left = (long) list.get(i) * i - (i == 0 ? 0 : prefix[i - 1]);
                long right = (prefix[m - 1] - prefix[i]) - (long) list.get(i) * (m - i - 1);

                res[idx] = left + right;
            }
        }

        return res;
    }


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
