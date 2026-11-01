package LeetCodeJava.Array;

// https://leetcode.com/problems/make-two-arrays-equal-by-reversing-sub-arrays/description/

import java.util.*;

/**
 * 1460. Make Two Arrays Equal by Reversing Sub-arrays
 * Easy
 *
 * You are given two integer arrays of equal length target and arr. In one step, you can
 * select any non-empty subarray of arr and reverse it. You are allowed to make any number
 * of steps.
 *
 * Return true if you can make arr equal to target or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: target = [1,2,3,4], arr = [2,4,1,3]
 * Output: true
 * Explanation: You can follow the next steps to convert arr to target:
 * 1- Reverse subarray [2,4,1], arr becomes [1,4,2,3]
 * 2- Reverse subarray [4,2], arr becomes [1,2,4,3]
 * 3- Reverse subarray [4,3], arr becomes [1,2,3,4]
 *
 * Example 2:
 *
 * Input: target = [7], arr = [7]
 * Output: true
 *
 * Example 3:
 *
 * Input: target = [3,7,9], arr = [3,7,11]
 * Output: false
 * Explanation: arr does not have value 9 and it can never be converted to target.
 *
 *
 * Constraints:
 *
 * target.length == arr.length
 * 1 <= target.length <= 1000
 * 1 <= target[i] <= 1000
 * 1 <= arr[i] <= 1000
 *
 */
public class MakeTwoArraysEqualByReversingSubArrays {

    // V0
    // IDEA: MULTISET EQUALITY — reversing a length-2 subarray is an adjacent swap, and
    //       adjacent swaps compose into any permutation, so only the multiset of values
    //       matters. Counts matter: a plain set comparison would be wrong.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean canBeEqual(int[] target, int[] arr) {
        if (target.length != arr.length) {
            return false;
        }

        // +1 for every value in target, -1 for every value in arr
        Map<Integer, Integer> balance = new HashMap<>();
        for (int i = 0; i < target.length; i++) {
            balance.put(target[i], balance.getOrDefault(target[i], 0) + 1);
            balance.put(arr[i], balance.getOrDefault(arr[i], 0) - 1);
        }

        // equal multisets <=> every value cancels out
        for (int diff : balance.values()) {
            if (diff != 0) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: SORT BOTH AND COMPARE — two multisets are equal exactly when their sorted
    //       forms are identical
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public boolean canBeEqual_1(int[] target, int[] arr) {
        // sort copies so the caller's arrays are left untouched
        int[] sortedTarget = target.clone();
        int[] sortedArr = arr.clone();
        Arrays.sort(sortedTarget);
        Arrays.sort(sortedArr);
        return Arrays.equals(sortedTarget, sortedArr);
    }
}
