package LeetCodeJava.Array;

// https://leetcode.com/problems/degree-of-an-array/

import java.util.HashMap;
import java.util.Map;

/**
 *  697. Degree of an Array
 *  Easy
 *
 *  Given a non-empty array of non-negative integers nums, the degree of this
 *  array is defined as the maximum frequency of any one of its elements.
 *
 *  Your task is to find the smallest possible length of a (contiguous) subarray
 *  of nums, that has the same degree as nums.
 *
 *  Example 1:
 *    Input: nums = [1,2,2,3,1]
 *    Output: 2
 *    Explanation: The input array has a degree of 2 because both elements 1 and 2
 *    appear twice. The shortest subarray with the same degree is [2,2].
 *
 *  Example 2:
 *    Input: nums = [1,2,2,3,1,4,2]
 *    Output: 6
 *
 *  Constraints:
 *    nums.length will be between 1 and 50,000.
 *    nums[i] will be an integer between 0 and 49,999.
 */
public class DegreeOfAnArray {

    // V0
    // IDEA: one pass recording first index, last index and count per value;
    //       answer = min span among the values reaching the max count.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int findShortestSubArray(int[] nums) {
        Map<Integer, Integer> first = new HashMap<>();
        Map<Integer, Integer> last = new HashMap<>();
        Map<Integer, Integer> cnt = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int v = nums[i];
            if (!first.containsKey(v)) {
                first.put(v, i);
            }
            last.put(v, i);
            cnt.put(v, cnt.getOrDefault(v, 0) + 1);
        }

        int degree = 0;
        for (int c : cnt.values()) {
            degree = Math.max(degree, c);
        }

        int res = nums.length;
        for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
            if (e.getValue() == degree) {
                int v = e.getKey();
                res = Math.min(res, last.get(v) - first.get(v) + 1);
            }
        }
        return res;
    }
}
