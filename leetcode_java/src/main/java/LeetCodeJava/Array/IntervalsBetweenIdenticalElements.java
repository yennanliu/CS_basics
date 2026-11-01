package LeetCodeJava.Array;

// https://leetcode.com/problems/intervals-between-identical-elements/description/

import java.util.*;

/**
 * 2121. Intervals Between Identical Elements
 * Medium
 *
 * You are given a 0-indexed array of n integers arr.
 *
 * The interval between two elements in arr is defined as the absolute difference between their indices. More formally, the interval between arr[i] and arr[j] is |i - j|.
 *
 * Return an array intervals of length n where intervals[i] is the sum of intervals between arr[i] and each element in arr with the same value as arr[i].
 *
 * Note: |x| is the absolute value of x.
 *
 *
 * Example 1:
 *
 * Input: arr = [2,1,3,1,2,3,3]
 * Output: [4,2,7,2,4,4,5]
 * Explanation:
 * - Index 0: Another 2 is found at index 4. |0 - 4| = 4
 * - Index 1: Another 1 is found at index 3. |1 - 3| = 2
 * - Index 2: Two more 3s are found at indices 5 and 6. |2 - 5| + |2 - 6| = 7
 * - Index 3: Another 1 is found at index 1. |3 - 1| = 2
 * - Index 4: Another 2 is found at index 0. |4 - 0| = 4
 * - Index 5: Two more 3s are found at indices 2 and 6. |5 - 2| + |5 - 6| = 4
 * - Index 6: Two more 3s are found at indices 2 and 5. |6 - 2| + |6 - 5| = 5
 *
 * Example 2:
 *
 * Input: arr = [10,5,10,10]
 * Output: [5,0,3,4]
 * Explanation:
 * - Index 0: Two more 10s are found at indices 2 and 3. |0 - 2| + |0 - 3| = 5
 * - Index 1: There is only one 5 in the array, so its sum of intervals to identical elements is 0.
 * - Index 2: Two more 10s are found at indices 0 and 3. |2 - 0| + |2 - 3| = 3
 * - Index 3: Two more 10s are found at indices 0 and 2. |3 - 0| + |3 - 2| = 4
 *
 *
 * Constraints:
 *
 * n == arr.length
 * 1 <= n <= 10^5
 * 1 <= arr[i] <= 10^5
 *
 */
public class IntervalsBetweenIdenticalElements {

    // V0
    // IDEA: GROUP INDICES BY VALUE, THEN PREFIX SUMS INSIDE EACH GROUP
    /**
     *  For one group of sorted indices idx[0 .. m-1] and a position t:
     *      left  part = sum over j < t of (idx[t] - idx[j]) = t * idx[t] - prefix[t]
     *      right part = sum over j > t of (idx[j] - idx[t])
     *                 = (groupTotal - prefix[t + 1]) - (m - 1 - t) * idx[t]
     *  where prefix[t] = idx[0] + ... + idx[t-1].
     *  Summing |i - j| pairwise per group would be O(m^2).
     *
     * time = O(N)
     * space = O(N)
     */
    public long[] getDistances(int[] arr) {
        // value -> the indices where it appears, in increasing order
        Map<Integer, List<Integer>> positions = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            positions.computeIfAbsent(arr[i], key -> new ArrayList<>()).add(i);
        }

        long[] result = new long[arr.length];
        for (List<Integer> idx : positions.values()) {
            int m = idx.size();
            long[] prefix = new long[m + 1];
            for (int t = 0; t < m; t++) {
                prefix[t + 1] = prefix[t] + idx.get(t);
            }
            long groupTotal = prefix[m];

            for (int t = 0; t < m; t++) {
                long current = idx.get(t);
                long leftPart = t * current - prefix[t];
                long rightPart = (groupTotal - prefix[t + 1]) - (long) (m - 1 - t) * current;
                result[idx.get(t)] = leftPart + rightPart;
            }
        }
        return result;
    }

    // V1
    // IDEA: TWO STREAMING SWEEPS, ONLY (COUNT, SUM OF INDICES) PER VALUE
    /**
     *  left -> right: each earlier equal index j adds (i - j) = count * i - indexSum
     *  right -> left: each later equal index j adds (j - i)   = indexSum - count * i
     *  No per-value index list is kept, only two numbers per distinct value.
     *
     * time = O(N)
     * space = O(U), U = number of distinct values
     */
    public long[] getDistances_1(int[] arr) {
        int n = arr.length;
        long[] result = new long[n];

        Map<Integer, Long> count = new HashMap<>();
        Map<Integer, Long> indexSum = new HashMap<>();
        for (int i = 0; i < n; i++) {
            long seen = count.getOrDefault(arr[i], 0L);
            long sum = indexSum.getOrDefault(arr[i], 0L);
            result[i] += seen * i - sum;
            count.put(arr[i], seen + 1);
            indexSum.put(arr[i], sum + i);
        }

        count.clear();
        indexSum.clear();
        for (int i = n - 1; i >= 0; i--) {
            long seen = count.getOrDefault(arr[i], 0L);
            long sum = indexSum.getOrDefault(arr[i], 0L);
            result[i] += sum - seen * i;
            count.put(arr[i], seen + 1);
            indexSum.put(arr[i], sum + i);
        }
        return result;
    }
}
