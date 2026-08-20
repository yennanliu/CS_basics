package LeetCodeJava.Sort;

// https://leetcode.com/problems/relative-sort-array/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  1122. Relative Sort Array
 *  Easy
 *
 *  Given two arrays arr1 and arr2, the elements of arr2 are distinct, and all
 *  elements in arr2 are also in arr1.
 *
 *  Sort the elements of arr1 such that the relative ordering of items in arr1
 *  are the same as in arr2. Elements that do not appear in arr2 should be placed
 *  at the end of arr1 in ascending order.
 *
 *  Example 1:
 *    Input: arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
 *    Output: [2,2,2,1,4,3,3,9,6,7,19]
 *
 *  Example 2:
 *    Input: arr1 = [28,6,22,8,44,17], arr2 = [22,28,8,6]
 *    Output: [22,28,8,6,17,44]
 *
 *  Constraints:
 *    1 <= arr1.length, arr2.length <= 1000
 *    0 <= arr1[i], arr2[i] <= 1000
 *    All the elements of arr2 are distinct.
 *    Each arr2[i] is in arr1.
 */
public class RelativeSortArray {

    // V0
    // IDEA: COUNTING SORT OVER THE BOUNDED VALUE DOMAIN (0..1000)
    //       bucket arr1 by value. First emit the arr2 values in arr2's order
    //       (each as many times as it occurs), then sweep 0..1000 and emit
    //       whatever count is left -> that sweep is ascending by construction,
    //       which is exactly the required tail ordering.
    /**
     * time = O(n + m + V)   // V = 1001, the value domain
     * space = O(V)
     */
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        final int V = 1001;
        int[] count = new int[V];
        for (int x : arr1) {
            count[x]++;
        }

        int[] res = new int[arr1.length];
        int idx = 0;

        // 1) values present in arr2, following arr2's order
        for (int x : arr2) {
            while (count[x] > 0) {
                res[idx++] = x;
                count[x]--;
            }
        }

        // 2) the remaining values, ascending
        for (int v = 0; v < V; v++) {
            while (count[v] > 0) {
                res[idx++] = v;
                count[v]--;
            }
        }

        return res;
    }

    // V1
    // IDEA: RANK MAP + COMPARATOR SORT (works for an unbounded value domain)
    //       rank[x] = index of x inside arr2; a value absent from arr2 gets the
    //       key (1000 + x), which is larger than any rank and still ascending
    //       among the absent values.
    /**
     * time = O(n log n)
     * space = O(n + m)
     */
    public int[] relativeSortArraySort(int[] arr1, int[] arr2) {
        Map<Integer, Integer> rank = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) {
            rank.put(arr2[i], i);
        }

        Integer[] boxed = new Integer[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            boxed[i] = arr1[i];
        }

        Arrays.sort(boxed, (a, b) -> {
            int ka = rank.containsKey(a) ? rank.get(a) : 1000 + a;
            int kb = rank.containsKey(b) ? rank.get(b) : 1000 + b;
            return Integer.compare(ka, kb);
        });

        int[] res = new int[arr1.length];
        for (int i = 0; i < boxed.length; i++) {
            res[i] = boxed[i];
        }
        return res;
    }
}
