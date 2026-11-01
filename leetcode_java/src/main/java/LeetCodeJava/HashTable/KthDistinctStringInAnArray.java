package LeetCodeJava.HashTable;

// https://leetcode.com/problems/kth-distinct-string-in-an-array/description/

import java.util.*;

/**
 * 2053. Kth Distinct String in an Array
 * Easy
 *
 * A distinct string is a string that is present only once in an array.
 *
 * Given an array of strings arr, and an integer k, return the kth distinct string present in arr.
 * If there are fewer than k distinct strings, return an empty string "".
 *
 * Note that the strings are considered in the order in which they appear in the array.
 *
 *
 * Example 1:
 *
 * Input: arr = ["d","b","c","b","c","a"], k = 2
 * Output: "a"
 * Explanation:
 * The only distinct strings in arr are "d" and "a".
 * "d" appears 1st, so it is the 1st distinct string.
 * "a" appears 2nd, so it is the 2nd distinct string.
 * Since k == 2, "a" is returned.
 *
 * Example 2:
 *
 * Input: arr = ["aaa","aa","a"], k = 1
 * Output: "aaa"
 *
 * Example 3:
 *
 * Input: arr = ["a","b","a"], k = 3
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= k <= arr.length <= 1000
 * 1 <= arr[i].length <= 5
 * arr[i] consists of lowercase English letters.
 *
 */
public class KthDistinctStringInAnArray {

    // V0
    // IDEA: HASH TABLE COUNTING (first pass counts, second pass walks arr in its original order)
    /**
     * time = O(L), L = total length of all strings
     * space = O(L)
     */
    public String kthDistinct(String[] arr, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String s : arr) {
            count.put(s, count.getOrDefault(s, 0) + 1);
        }

        // re-scan arr, not the map: the order of appearance is what defines "kth"
        int remaining = k;
        for (String s : arr) {
            if (count.get(s) == 1) {
                remaining--;
                if (remaining == 0) {
                    return s;
                }
            }
        }
        return "";
    }
}
