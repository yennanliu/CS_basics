package LeetCodeJava.Heap;

// https://leetcode.com/problems/zero-array-transformation-iii/

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  3362. Zero Array Transformation III
 *  Medium
 *
 *  You are given an integer array nums of length n and a 2D array queries where
 *  queries[i] = [l_i, r_i].
 *
 *  Each queries[i] represents the following action on nums:
 *   - Decrement the value at each index in the range [l_i, r_i] in nums by at most 1.
 *     The amount by which the value is decremented can be chosen independently for
 *     each index.
 *
 *  A Zero Array is an array with all its elements equal to 0.
 *
 *  Return the maximum number of elements that can be removed from queries, such
 *  that nums can still be converted to a zero array using the remaining queries.
 *  If it is not possible to convert nums to a zero array, return -1.
 *
 *  Example 1:
 *    Input: nums = [2,0,2], queries = [[0,2],[0,2],[1,1]]
 *    Output: 1
 *
 *  Example 2:
 *    Input: nums = [1,1,1,1], queries = [[1,3],[0,2],[1,3],[1,2]]
 *    Output: 2
 *
 *  Example 3:
 *    Input: nums = [1,2,3,4], queries = [[0,3]]
 *    Output: -1
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^5
 *    1 <= queries.length <= 10^5
 *    queries[i].length == 2
 *    0 <= l_i <= r_i < nums.length
 */
public class ZeroArrayTransformationIII {

    // V0
    // IDEA: LEFT-TO-RIGHT SWEEP, ALWAYS SPENDING THE QUERY THAT REACHES FURTHEST
    //       maximising removals == minimising how many queries are KEPT.
    //       sweeping index by index, whatever demand is still unmet at i must be
    //       paid by a query starting at or before i — and among those the one with
    //       the LARGEST right end is never worse, since it covers everything a
    //       shorter one could.
    //       so hold the available queries in a max-heap keyed by right end, adding
    //       those that start at i, and pull until the running coverage meets nums[i].
    //       a difference array remembers where each chosen query's coverage expires.
    //       if the heap runs dry (or its best already ended before i) -> -1.
    /**
     * time = O((n + q) log q)
     * space = O(n + q)
     */
    public int maxRemoval(int[] nums, int[][] queries) {
        int n = nums.length;

        // sort queries by start so they can be fed to the heap with one pointer
        int[][] qs = queries.clone();
        Arrays.sort(qs, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> available = new PriorityQueue<>(Collections.reverseOrder());
        int[] expire = new int[n + 1]; // difference array of chosen coverage
        int cover = 0;
        int used = 0;
        int p = 0;

        for (int i = 0; i < n; i++) {
            cover += expire[i];

            while (p < qs.length && qs[p][0] == i) {
                available.add(qs[p][1]);
                p++;
            }

            while (cover < nums[i]) {
                if (available.isEmpty() || available.peek() < i) {
                    return -1;
                }
                int r = available.poll();
                used++;
                cover++;
                expire[r + 1] -= 1;
            }
        }

        return queries.length - used;
    }
}
