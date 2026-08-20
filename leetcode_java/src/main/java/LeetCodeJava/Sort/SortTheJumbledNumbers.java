package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-the-jumbled-numbers/

import java.util.Arrays;

/**
 *  2191. Sort the Jumbled Numbers
 *  Medium
 *
 *  You are given a 0-indexed integer array mapping which represents the mapping
 *  rule of a shuffled decimal system. mapping[i] = j means digit i should be
 *  mapped to digit j in this system.
 *
 *  The mapped value of an integer is the new integer obtained by replacing each
 *  occurrence of digit i in the integer with mapping[i] for all 0 <= i <= 9.
 *
 *  You are also given another integer array nums. Return the array nums sorted
 *  in non-decreasing order based on the mapped values of its elements.
 *
 *  Notes:
 *    - Elements with the same mapped values should appear in the same relative
 *      order as in the input.
 *    - The elements of nums should only be sorted in their mapped order, not
 *      modified.
 *
 *  Example 1:
 *    Input: mapping = [8,9,4,0,2,1,3,5,7,6], nums = [991,338,38]
 *    Output: [338,38,991]
 *    Explanation: 991 -> 669, 338 -> 007 = 7, 38 -> 07 = 7. 338 and 38 tie, so
 *                 they keep their input order.
 *
 *  Example 2:
 *    Input: mapping = [0,1,2,3,4,5,6,7,8,9], nums = [789,456,123]
 *    Output: [123,456,789]
 *
 *  Constraints:
 *    mapping.length == 10
 *    0 <= mapping[i] <= 9, all values of mapping[i] are unique.
 *    1 <= nums.length <= 3 * 10^4
 *    0 <= nums[i] < 10^9
 */
public class SortTheJumbledNumbers {

    // V0
    // IDEA: PRECOMPUTE THE MAPPED KEY ONCE PER ELEMENT + STABLE SORT
    //       translating digit by digit inside the comparator would redo the work
    //       O(log n) times per element, so map each number ONCE up front.
    //       leading zeros disappear for free because the key is rebuilt as a
    //       number (digit accumulation), not as a string.
    //       ties must preserve the input order -> Arrays.sort on an Integer[]
    //       is TimSort and therefore stable, so no index tiebreaker is needed.
    //       the ORIGINAL numbers are returned; the mapping is only a sort key.
    /**
     * time = O(n * d + n log n)   // d = number of digits
     * space = O(n)
     */
    public int[] sortJumbled(int[] mapping, int[] nums) {
        int n = nums.length;

        final int[] key = new int[n];
        for (int i = 0; i < n; i++) {
            key[i] = mapped(mapping, nums[i]);
        }

        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (a, b) -> Integer.compare(key[a], key[b]));

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = nums[idx[i]];
        }
        return res;
    }

    private int mapped(int[] mapping, int x) {
        if (x == 0) {
            return mapping[0];
        }
        // walk the decimal digits left -> right and rebuild the mapped number
        int res = 0;
        String s = String.valueOf(x);
        for (int i = 0; i < s.length(); i++) {
            res = res * 10 + mapping[s.charAt(i) - '0'];
        }
        return res;
    }
}
