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


    // V1
    // IDEA: brute force O(n^2) - OR every subarray explicitly.
    //       Kept as a readable correctness reference (TLE on the real limits).
    /**
     * time = O(n^2)
     * space = O(n^2) worst case for the result set (bounded by distinct ORs)
     */
    public int subarrayBitwiseORs_1(int[] arr) {
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            int cur = 0;
            for (int j = i; j < arr.length; j++) {
                cur |= arr[j];
                res.add(cur);
            }
        }
        return res.size();
    }

    // V2
    // IDEA: "next set bit" jump table. Fixing a start i, the OR over [i, j] only
    //       changes when some arr[j] contributes a bit the running OR lacks.
    //       nxt[i][b] = first index >= i whose bit b is set, so from the current
    //       OR we jump straight to the next index that changes it - at most 32
    //       jumps per start, and no per-index set merging like V0.
    /**
     * time = O(32 * 32 * n)
     * space = O(32 * n)
     */
    public int subarrayBitwiseORs_2(int[] arr) {
        int n = arr.length;
        final int B = 32;
        int[][] nxt = new int[n + 1][B];
        for (int b = 0; b < B; b++) {
            nxt[n][b] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int b = 0; b < B; b++) {
                nxt[i][b] = (((arr[i] >>> b) & 1) == 1) ? i : nxt[i + 1][b];
            }
        }

        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int cur = 0;
            int j = i;
            while (j < n) {
                cur |= arr[j];
                res.add(cur);
                // jump to the next index that actually adds a new bit
                int j2 = n;
                for (int b = 0; b < B; b++) {
                    if (((cur >>> b) & 1) == 0) {
                        j2 = Math.min(j2, nxt[j + 1][b]);
                    }
                }
                j = j2;
            }
        }
        return res.size();
    }
}
