package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-unique-xor-triplets-ii/

/**
 *  3514. Number of Unique XOR Triplets II
 *  Medium
 *
 *  You are given an integer array nums.
 *
 *  A XOR triplet is defined as the XOR of three elements
 *  nums[i] XOR nums[j] XOR nums[k] where i <= j <= k.
 *
 *  Return the number of unique XOR triplet values from all possible triplets
 *  (i, j, k).
 *
 *  Example 1:
 *    Input: nums = [1,3]
 *    Output: 2
 *    Explanation: the reachable values are {1, 3}.
 *
 *  Example 2:
 *    Input: nums = [6,7,8,9]
 *    Output: 4
 *    Explanation: the reachable values are {6, 7, 8, 9}.
 *
 *  Constraints:
 *    1 <= nums.length <= 1500
 *    1 <= nums[i] <= 1500
 */
public class NumberOfUniqueXorTripletsII {

    // V0
    // IDEA: PAIR XORS FIRST, THEN ONE MORE XOR PASS
    //       i <= j <= k with repeats allowed means the reachable set is
    //       { a ^ b ^ c : a, b, c in nums }, which already holds every nums[k]
    //       (take a == b).
    //       values are <= 1500 < 2048, so any xor stays below 2048 -> the set of
    //       pairwise xors has at most 2048 members however big nums is. compute it
    //       in O(n^2) over the DISTINCT values, then xor that small set against the
    //       (also <= 2048) distinct values -> O(V^2) instead of O(n^3).
    /**
     * time = O(V^2)   // V = 2048, distinct values are bucketed first
     * space = O(V)
     */
    public int uniqueXorTriplets(int[] nums) {
        final int V = 2048;

        boolean[] seen = new boolean[V];
        for (int x : nums) {
            seen[x] = true;
        }

        boolean[] pair = new boolean[V];
        for (int a = 0; a < V; a++) {
            if (!seen[a]) {
                continue;
            }
            for (int b = 0; b < V; b++) {
                if (seen[b]) {
                    pair[a ^ b] = true;
                }
            }
        }

        boolean[] triple = new boolean[V];
        for (int p = 0; p < V; p++) {
            if (!pair[p]) {
                continue;
            }
            for (int c = 0; c < V; c++) {
                if (seen[c]) {
                    triple[p ^ c] = true;
                }
            }
        }

        int res = 0;
        for (int i = 0; i < V; i++) {
            if (triple[i]) {
                res++;
            }
        }
        return res;
    }
}
