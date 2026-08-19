package LeetCodeJava.HashTable;

// https://leetcode.com/problems/4sum-ii/

import java.util.HashMap;
import java.util.Map;

/**
 *  454. 4Sum II
 *  Medium
 *
 *  Given four integer arrays nums1, nums2, nums3, and nums4 all of length n,
 *  return the number of tuples (i, j, k, l) such that:
 *
 *  0 <= i, j, k, l < n
 *  nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
 *
 *  Example 1:
 *  Input: nums1 = [1,2], nums2 = [-2,-1], nums3 = [-1,2], nums4 = [0,2]
 *  Output: 2
 *
 *  Example 2:
 *  Input: nums1 = [0], nums2 = [0], nums3 = [0], nums4 = [0]
 *  Output: 1
 *
 *  Constraints:
 *  n == nums1.length == nums2.length == nums3.length == nums4.length
 *  1 <= n <= 200
 *  -2^28 <= nums1[i], nums2[i], nums3[i], nums4[i] <= 2^28
 */
public class FourSumII {

    // V0
    // IDEA: MEET IN THE MIDDLE + HASHMAP
    //       count all (a + b) sums, then for each (c + d) look up -(c + d)
    /**
     * time = O(n^2)
     * space = O(n^2)
     */
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int a : nums1) {
            for (int b : nums2) {
                cnt.put(a + b, cnt.getOrDefault(a + b, 0) + 1);
            }
        }

        int res = 0;
        for (int c : nums3) {
            for (int d : nums4) {
                res += cnt.getOrDefault(-(c + d), 0);
            }
        }
        return res;
    }
}
