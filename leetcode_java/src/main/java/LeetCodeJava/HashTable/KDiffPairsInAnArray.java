package LeetCodeJava.HashTable;

// https://leetcode.com/problems/k-diff-pairs-in-an-array/

import java.util.HashMap;
import java.util.Map;

/**
 *  532. K-diff Pairs in an Array
 *  Medium
 *
 *  Given an array of integers nums and an integer k,
 *  return the number of unique k-diff pairs in the array.
 *
 *  A k-diff pair is an integer pair (nums[i], nums[j]), where the following are true:
 *   - 0 <= i < j < nums.length
 *   - |nums[i] - nums[j]| == k
 *
 *  Example 1:
 *  Input: nums = [3,1,4,1,5], k = 2
 *  Output: 2   // (1, 3) and (3, 5)
 *
 *  Example 2:
 *  Input: nums = [1,2,3,4,5], k = 1
 *  Output: 4
 *
 *  Example 3:
 *  Input: nums = [1,3,1,5,4], k = 0
 *  Output: 1
 *
 *  Constraints:
 *  1 <= nums.length <= 10^4
 *  -10^7 <= nums[i] <= 10^7
 *  0 <= k <= 10^7
 */
public class KDiffPairsInAnArray {

    // V0
    // IDEA: COUNTER. for k == 0 count values appearing >= 2 times;
    //       otherwise count distinct values x where (x + k) also exists
    /**
     * time = O(n)
     * space = O(n)
     */
    public int findPairs(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k < 0) {
            return 0;
        }
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int n : nums) {
            cnt.put(n, cnt.getOrDefault(n, 0) + 1);
        }

        int res = 0;
        for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
            if (k == 0) {
                if (e.getValue() >= 2) {
                    res++;
                }
            } else {
                // NOTE: only look "up" so each unordered pair is counted once
                if (cnt.containsKey(e.getKey() + k)) {
                    res++;
                }
            }
        }
        return res;
    }
}
