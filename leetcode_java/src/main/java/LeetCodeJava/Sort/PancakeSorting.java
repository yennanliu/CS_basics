package LeetCodeJava.Sort;

// https://leetcode.com/problems/pancake-sorting/

import java.util.ArrayList;
import java.util.List;

/**
 *  969. Pancake Sorting
 *  Medium
 *
 *  Given an array of integers arr, sort the array by performing a series of pancake flips.
 *
 *  In one pancake flip we do the following steps:
 *   - Choose an integer k where 1 <= k <= arr.length.
 *   - Reverse the sub-array arr[0...k-1] (0-indexed).
 *
 *  For example, if arr = [3,2,1,4] and we performed a pancake flip choosing k = 3,
 *  we reverse the sub-array [3,2,1], so arr = [1,2,3,4] after the pancake flip at k = 3.
 *
 *  Return an array of the k-values corresponding to a sequence of pancake flips that
 *  sort arr. Any valid answer that sorts the array within 10 * arr.length flips will
 *  be judged as correct.
 *
 *  Example 1:
 *  Input: arr = [3,2,4,1]
 *  Output: [4,2,4,3]
 *
 *  Example 2:
 *  Input: arr = [1,2,3]
 *  Output: []
 *
 *  Constraints:
 *  1 <= arr.length <= 100
 *  1 <= arr[i] <= arr.length
 *  All integers in arr are unique (arr is a permutation of 1..arr.length).
 */
public class PancakeSorting {

    // V0
    // IDEA: SELECTION-SORT style — repeatedly bring the current max to the front, then flip it to its final slot
    /**
     * time = O(n^2)
     * space = O(n)   // output list (ignoring it, O(1) extra)
     */
    public List<Integer> pancakeSort(int[] arr) {
        List<Integer> res = new ArrayList<>();
        if (arr == null || arr.length <= 1) {
            return res;
        }
        for (int size = arr.length; size > 1; size--) {
            // step 1) locate the max within arr[0 .. size-1]
            int maxIdx = 0;
            for (int i = 1; i < size; i++) {
                if (arr[i] > arr[maxIdx]) {
                    maxIdx = i;
                }
            }
            // already in place -> nothing to do
            if (maxIdx == size - 1) {
                continue;
            }
            // step 2) flip it to the front (skip if it is already there)
            if (maxIdx != 0) {
                flip(arr, maxIdx + 1);
                res.add(maxIdx + 1);
            }
            // step 3) flip the whole prefix so the max lands at index size-1
            flip(arr, size);
            res.add(size);
        }
        return res;
    }

    /** reverse arr[0 .. k-1] */
    private void flip(int[] arr, int k) {
        int l = 0;
        int r = k - 1;
        while (l < r) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
            l++;
            r--;
        }
    }
}
