package LeetCodeJava.Array;

// https://leetcode.com/problems/largest-component-size-by-common-factor/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 952. Largest Component Size by Common Factor
 * Hard
 *
 * You are given an integer array of unique positive integers nums. Consider the following graph:
 *
 * There are nums.length nodes, labeled nums[0] to nums[nums.length - 1],
 * There is an undirected edge between nums[i] and nums[j] if nums[i] and nums[j] share a
 * common factor greater than 1.
 *
 * Return the size of the largest connected component in the graph.
 *
 * Example 1:
 *
 * Input: nums = [4,6,15,35]
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [20,50,9,63]
 * Output: 2
 *
 * Example 3:
 *
 * Input: nums = [2,3,6,7,4,12,21,39]
 * Output: 8
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= nums[i] <= 10^5
 * All the values of nums are unique.
 *
 */
public class LargestComponentSizeByCommonFactor {

    // V0
    // IDEA: UNION FIND over PRIME FACTORS
    /**
     *  - Building edges PAIRWISE is O(n^2) -> too slow.
     *
     *  - Instead, union every number with each of its PRIME FACTORS.
     *    Two numbers sharing a prime factor then land in the same set
     *    (transitively, through that prime's node).
     *
     *  - Numbers and primes share ONE label space (both <= max(nums)); the
     *    collision is harmless because `number v` and `prime v` belong together
     *    anyway.
     *
     *  - Finally, count how many of the ORIGINAL numbers fall under each root
     *    (the prime nodes themselves must NOT be counted).
     *
     *  time  = O(n * sqrt(M) * a(M)), n = nums.length, M = max(nums)
     *  space = O(M)
     */

    private int[] parent;

    public int largestComponentSize(int[] nums) {
        int m = 0;
        for (int v : nums) {
            m = Math.max(m, v);
        }

        this.parent = new int[m + 1];
        for (int i = 0; i <= m; i++) {
            parent[i] = i;
        }

        for (int v : nums) {
            int x = v;
            int f = 2;
            /** NOTE !!!
             *
             *  trial division only up to sqrt(x) -> O(sqrt(M)) per number
             */
            while (f * f <= x) {
                if (x % f == 0) {
                    union(v, f);
                    while (x % f == 0) {
                        x /= f;
                    }
                }
                f += 1;
            }
            if (x > 1) {
                // leftover prime factor (bigger than sqrt(v))
                union(v, x);
            }
        }

        /** NOTE !!!
         *
         *  only the ACTUAL numbers count towards component size,
         *  NOT the prime nodes we introduced above
         */
        Map<Integer, Integer> cnt = new HashMap<>();
        int res = 0;
        for (int v : nums) {
            int root = find(v);
            int c = cnt.getOrDefault(root, 0) + 1;
            cnt.put(root, c);
            res = Math.max(res, c);
        }

        return res;
    }

    private int find(int x) {
        // iterative find with path halving
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra != rb) {
            parent[ra] = rb;
        }
    }

}
