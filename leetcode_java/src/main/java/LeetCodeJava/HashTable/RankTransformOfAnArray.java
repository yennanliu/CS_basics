package LeetCodeJava.HashTable;

// https://leetcode.com/problems/rank-transform-of-an-array/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  1331. Rank Transform of an Array
 *  Easy
 *
 *  Given an array of integers arr, replace each element with its rank.
 *
 *  The rank represents how large the element is. The rank has the following rules:
 *    - Rank is an integer starting from 1.
 *    - The larger the element, the larger the rank. If two elements are equal,
 *      their rank must be the same.
 *    - Rank should be as small as possible.
 *
 *  Example 1:
 *  Input: arr = [40,10,20,30]
 *  Output: [4,1,2,3]
 *
 *  Example 2:
 *  Input: arr = [100,100,100]
 *  Output: [1,1,1]
 *
 *  Example 3:
 *  Input: arr = [37,12,28,9,100,56,80,5,12]
 *  Output: [5,3,4,2,8,6,7,1,3]
 *
 *  Constraints:
 *  0 <= arr.length <= 10^5
 *  -10^9 <= arr[i] <= 10^9
 */
public class RankTransformOfAnArray {

    // V0
    // IDEA: SORT A COPY + HASH MAP (value -> rank)
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] arrayRankTransform(int[] arr) {

        // edge
        if (arr == null || arr.length == 0) {
            return new int[0];
        }

        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> rank = new HashMap<>();
        for (int v : sorted) {
            if (!rank.containsKey(v)) {
                rank.put(v, rank.size() + 1);
            }
        }

        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = rank.get(arr[i]);
        }

        return res;
    }
}
