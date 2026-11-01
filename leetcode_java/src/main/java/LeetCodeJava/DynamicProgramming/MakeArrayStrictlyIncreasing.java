package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/make-array-strictly-increasing/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 1187. Make Array Strictly Increasing
 * Hard
 *
 * Given two integer arrays arr1 and arr2, return the minimum number of operations
 * (possibly zero) needed to make arr1 strictly increasing.
 *
 * In one operation, you can choose two indices 0 <= i < arr1.length and
 * 0 <= j < arr2.length and do the assignment arr1[i] = arr2[j].
 *
 * If there is no way to make arr1 strictly increasing, return -1.
 *
 *
 * Example 1:
 *
 * Input: arr1 = [1,5,3,6,7], arr2 = [1,3,2,4]
 * Output: 1
 * Explanation: Replace 5 with 2, then arr1 = [1, 2, 3, 6, 7].
 *
 * Example 2:
 *
 * Input: arr1 = [1,5,3,6,7], arr2 = [4,3,1]
 * Output: 2
 * Explanation: Replace 5 with 3 and then replace 3 with 4. arr1 = [1, 3, 4, 6, 7].
 *
 * Example 3:
 *
 * Input: arr1 = [1,5,3,6,7], arr2 = [1,6,3,3]
 * Output: -1
 * Explanation: You can't make arr1 strictly increasing.
 *
 *
 * Constraints:
 *
 * 1 <= arr1.length, arr2.length <= 2000
 * 0 <= arr1[i], arr2[i] <= 10^9
 *
 */
public class MakeArrayStrictlyIncreasing {

    // V0
    // IDEA: DP OVER "LAST VALUE PLACED" + BINARY SEARCH (TreeSet.higher)
    /**
     * dp = { lastValue : min operations to make arr1[0..i] strictly increasing
     *                    while ending with lastValue }
     *
     * for each next element a of arr1, for each (prev, cost) in dp:
     *   - keep a     : if a > prev                         -> next[a] = min(next[a], cost)
     *   - replace a  : v = smallest arr2 value > prev      -> next[v] = min(next[v], cost + 1)
     * only the SMALLEST valid replacement matters; any bigger one is dominated.
     *
     * the map holds at most len(arr2) + 1 keys per step.
     */
    /**
     * time = O(N * M log M)
     * space = O(M)
     */
    public int makeArrayIncreasing(int[] arr1, int[] arr2) {
        // sorted, deduplicated candidates, so "smallest value > prev" is one lookup
        TreeSet<Integer> candidates = new TreeSet<>();
        for (int value : arr2) {
            candidates.add(value);
        }

        // -1 is a sentinel for "nothing placed yet": every value is >= 0
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(-1, 0);

        for (int current : arr1) {
            Map<Integer, Integer> next = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : dp.entrySet()) {
                int prev = entry.getKey();
                int cost = entry.getValue();

                // case 1: keep arr1[i] as it is
                if (current > prev) {
                    int known = next.getOrDefault(current, Integer.MAX_VALUE);
                    next.put(current, Math.min(known, cost));
                }

                // case 2: replace arr1[i] with the smallest arr2 value above prev
                Integer replacement = candidates.higher(prev);
                if (replacement != null) {
                    int known = next.getOrDefault(replacement, Integer.MAX_VALUE);
                    next.put(replacement, Math.min(known, cost + 1));
                }
            }
            // no way to place this element
            if (next.isEmpty()) {
                return -1;
            }
            dp = next;
        }

        int best = Integer.MAX_VALUE;
        for (int cost : dp.values()) {
            best = Math.min(best, cost);
        }
        return best;
    }

    // V1
    // IDEA: 2D DP OVER (INDEX, NUMBER OF REPLACEMENTS) -> SMALLEST POSSIBLE LAST VALUE
    /**
     * Same problem, indexed the other way round (the editorial's bottom-up form):
     *
     * minLast[j] = smallest possible value of the last element after processing
     *              arr1[0..i] with exactly j replacements (INF if impossible)
     *
     * for element a = arr1[i]:
     *   - keep a     : if a > minLast[j]             -> nextLast[j]   = min(.., a)
     *   - replace a  : v = smallest arr2 value > minLast[j]
     *                                                -> nextLast[j+1] = min(.., v)
     *
     * answer: smallest j whose minLast[j] is not INF.
     */
    /**
     * time = O(N * min(N, M) * log M)
     * space = O(N)
     */
    public int makeArrayIncreasing_1(int[] arr1, int[] arr2) {
        int n = arr1.length;
        // sorted, deduplicated replacement values
        TreeSet<Integer> unique = new TreeSet<>();
        for (int value : arr2) {
            unique.add(value);
        }
        int[] sorted = new int[unique.size()];
        int position = 0;
        for (int value : unique) {
            sorted[position++] = value;
        }
        long INF = Long.MAX_VALUE;

        long[] minLast = new long[n + 1];
        Arrays.fill(minLast, INF);
        // nothing placed yet: last value is "below everything"
        minLast[0] = -1;

        for (int i = 0; i < n; i++) {
            long[] nextLast = new long[n + 1];
            Arrays.fill(nextLast, INF);
            for (int j = 0; j <= i; j++) {
                if (minLast[j] == INF) {
                    continue;
                }
                // keep arr1[i]
                if (arr1[i] > minLast[j]) {
                    nextLast[j] = Math.min(nextLast[j], arr1[i]);
                }
                // replace arr1[i] with the smallest arr2 value above minLast[j]
                int index = upperBound(sorted, minLast[j]);
                if (index < sorted.length) {
                    nextLast[j + 1] = Math.min(nextLast[j + 1], sorted[index]);
                }
            }
            minLast = nextLast;
        }

        for (int j = 0; j <= n; j++) {
            if (minLast[j] != INF) {
                return j;
            }
        }
        return -1;
    }

    /** Index of the first element strictly greater than target (sorted.length if none). */
    private int upperBound(int[] sorted, long target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
