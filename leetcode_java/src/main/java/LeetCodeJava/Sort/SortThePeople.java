package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-the-people/

import java.util.Arrays;

/**
 *  2418. Sort the People
 *  Easy
 *
 *  You are given an array of strings names, and an array heights that consists
 *  of distinct positive integers. Both arrays are of length n.
 *
 *  For each index i, names[i] and heights[i] denote the name and height of the
 *  ith person.
 *
 *  Return names sorted in descending order by the people's heights.
 *
 *  Example 1:
 *    Input: names = ["Mary","John","Emma"], heights = [180,165,170]
 *    Output: ["Mary","Emma","John"]
 *
 *  Example 2:
 *    Input: names = ["Alice","Bob","Bob"], heights = [155,185,150]
 *    Output: ["Bob","Alice","Bob"]
 *
 *  Constraints:
 *    n == names.length == heights.length
 *    1 <= n <= 10^3
 *    1 <= names[i].length <= 20
 *    1 <= heights[i] <= 10^5
 *    All the values of heights are distinct.
 */
public class SortThePeople {

    // V0
    // IDEA: SORT AN INDEX ARRAY BY HEIGHT, DESCENDING
    //       heights are distinct, so there is no tie-break and the name never
    //       enters the comparison. Sorting indices (rather than pairs) keeps the
    //       name/height association without building any wrapper object.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public String[] sortPeople(String[] names, int[] heights) {
        int n = names.length;

        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (a, b) -> Integer.compare(heights[b], heights[a]));

        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = names[idx[i]];
        }
        return res;
    }
}
