package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/bitwise-ors-of-subarrays/

import java.util.HashSet;
import java.util.Set;

/**
 *  898. Bitwise ORs of Subarrays
 *  Medium
 *
 *  Given an integer array arr, return the number of distinct bitwise ORs of all
 *  the non-empty subarrays of arr.
 *
 *  The bitwise OR of a subarray is the bitwise OR of each integer in the
 *  subarray. The bitwise OR of a subarray of one integer is that integer.
 *
 *  A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *  Example 1:
 *  Input: arr = [0]
 *  Output: 1
 *  Explanation: There is only one possible result: 0.
 *
 *  Example 2:
 *  Input: arr = [1,1,2]
 *  Output: 3
 *  Explanation: The possible subarrays are [1], [1], [2], [1, 1], [1, 2],
 *  [1, 1, 2]. These yield the results 1, 1, 2, 1, 3, 3. There are 3 unique
 *  values, so the answer is 3.
 *
 *  Example 3:
 *  Input: arr = [1,2,4]
 *  Output: 6
 *
 *  Constraints:
 *  1 <= arr.length <= 5 * 10^4
 *  0 <= arr[i] <= 10^9
 */
public class BitwiseORsOfSubarrays {

    // V0
    // IDEA: keep the set of OR values of all subarrays ending at index i.
    //       cur = {arr[i]} U {x | arr[i] : x in prev}. That set has at most ~32
    //       distinct values since OR only turns bits on.
    /**
     * time = O(32 * n)
     * space = O(32 * n)
     */
    public int subarrayBitwiseORs(int[] arr) {
        Set<Integer> res = new HashSet<>();
        Set<Integer> cur = new HashSet<>();
        for (int a : arr) {
            Set<Integer> next = new HashSet<>();
            next.add(a);
            for (int x : cur) {
                next.add(x | a);
            }
            cur = next;
            res.addAll(cur);
        }
        return res.size();
    }
}
