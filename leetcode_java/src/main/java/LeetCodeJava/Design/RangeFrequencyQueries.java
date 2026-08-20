package LeetCodeJava.Design;

// https://leetcode.com/problems/range-frequency-queries/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  2080. Range Frequency Queries
 *  Medium
 *
 *  Design a data structure to find the frequency of a given value in a given
 *  subarray. The frequency of a value in a subarray is the number of occurrences of
 *  that value in the subarray.
 *
 *  Implement the RangeFreqQuery class:
 *    RangeFreqQuery(int[] arr) Constructs an instance of the class with the given
 *      0-indexed integer array arr.
 *    int query(int left, int right, int value) Returns the frequency of value in the
 *      subarray arr[left...right].
 *
 *  Example 1:
 *    Input
 *      ["RangeFreqQuery","query","query"]
 *      [[[12,33,4,56,22,2,34,33,22,12,34,56]],[1,2,4],[0,11,33]]
 *    Output
 *      [null, 1, 2]
 *    Explanation
 *      query(1,2,4)  -> 1 (the value 4 occurs once in [33,4])
 *      query(0,11,33)-> 2 (the value 33 occurs twice in the whole array)
 *
 *  Constraints:
 *    1 <= arr.length <= 10^5
 *    1 <= arr[i], value <= 10^4
 *    0 <= left <= right < arr.length
 *    1 <= queries <= 10^5
 */
public class RangeFrequencyQueries {

    // V0
    // IDEA: VALUE -> SORTED LIST OF ITS INDICES, THEN BINARY SEARCH THE RANGE
    //
    //       building the map is one pass, and because indices are appended in
    //       increasing order each list is already sorted. a query is then
    //         upperBound(idx, right) - lowerBound(idx, left)
    //       i.e. how many of that value's positions fall inside [left, right].
    //       a value that never occurs has no entry -> frequency 0.
    /**
     * time = O(n) build, O(log n) per query
     * space = O(n)
     */
    private final Map<Integer, List<Integer>> pos;

    public RangeFrequencyQueries(int[] arr) {
        this.pos = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            List<Integer> l = pos.get(arr[i]);
            if (l == null) {
                l = new ArrayList<>();
                pos.put(arr[i], l);
            }
            l.add(i);
        }
    }

    public int query(int left, int right, int value) {
        List<Integer> idx = pos.get(value);
        if (idx == null) {
            return 0;
        }
        return lowerBound(idx, right + 1) - lowerBound(idx, left);
    }

    /** first position in idx whose value >= target */
    private int lowerBound(List<Integer> idx, int target) {
        int l = 0;
        int r = idx.size();
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (idx.get(mid) < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }
}
