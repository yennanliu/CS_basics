package LeetCodeJava.DFS;

// https://leetcode.com/problems/minimize-hamming-distance-after-swap-operations/

import java.util.HashMap;
import java.util.Map;

/**
 *  1722. Minimize Hamming Distance After Swap Operations
 *  Medium
 *
 *  You are given two integer arrays, source and target, both of length n. You
 *  are also given an array allowedSwaps where each allowedSwaps[i] = [ai, bi]
 *  indicates that you are allowed to swap the elements at index ai and index bi
 *  (0-indexed) of array source. Note that you can swap elements at a specific
 *  pair of indices multiple times and in any order.
 *
 *  The Hamming distance of two arrays of the same length, source and target, is
 *  the number of positions where the elements are different.
 *
 *  Return the minimum Hamming distance of source and target after performing
 *  any amount of swap operations on array source.
 *
 *  Example 1:
 *    Input: source = [1,2,3,4], target = [2,1,4,5],
 *           allowedSwaps = [[0,1],[2,3]]
 *    Output: 1
 *    Explanation: source can become [2,1,4,3]; it differs from target only at
 *                 index 3.
 *
 *  Example 2:
 *    Input: source = [1,2,3,4], target = [1,3,2,4], allowedSwaps = []
 *    Output: 2
 *    Explanation: No swaps allowed; indices 1 and 2 differ.
 *
 *  Constraints:
 *    n == source.length == target.length
 *    1 <= n <= 10^5
 *    1 <= source[i], target[i] <= 10^5
 *    0 <= allowedSwaps.length <= 10^5
 *    allowedSwaps[i].length == 2
 *    0 <= ai, bi <= n - 1
 *    ai != bi
 */
public class MinimizeHammingDistanceAfterSwapOperations {

    private int[] parent;

    // V0
    // IDEA: UNION FIND + PER-COMPONENT MULTISET
    //       swaps are transitive: if we may swap (a,b) and (b,c) we can realise
    //       ANY permutation of {a,b,c}. so a connected component of the "allowed
    //       swap" graph is a bag whose values can be arranged freely.
    //       -> union the indices, then per component keep a count of the source
    //          values it holds.
    //       scan target left to right: if the component of index i still holds
    //       target[i], consume one (this position is matched for free); else the
    //       position must stay wrong -> distance += 1.
    //       the greedy is safe: every position that wants value v sits in the
    //       same bag, and the supply of v is fixed.
    /**
     * time = O(N * alpha(N))
     * space = O(N)
     */
    public int minimumHammingDistance(int[] source, int[] target, int[][] allowedSwaps) {
        int n = source.length;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        for (int[] sw : allowedSwaps) {
            union(sw[0], sw[1]);
        }

        // root -> (value -> how many copies the bag holds)
        Map<Integer, Map<Integer, Integer>> bags = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = find(i);
            Map<Integer, Integer> bag = bags.get(r);
            if (bag == null) {
                bag = new HashMap<>();
                bags.put(r, bag);
            }
            Integer cur = bag.get(source[i]);
            bag.put(source[i], cur == null ? 1 : cur + 1);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> bag = bags.get(find(i));
            Integer cnt = bag.get(target[i]);
            if (cnt != null && cnt > 0) {
                bag.put(target[i], cnt - 1);
            } else {
                res++;
            }
        }
        return res;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra != rb) {
            parent[ra] = rb;
        }
    }
}
