package LeetCodeJava.Array;

// https://leetcode.com/problems/largest-component-size-by-common-factor/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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


    // V1
    // IDEA: SIEVE OF SMALLEST PRIME FACTOR + UNION FIND
    /**
     *  V0 trial-divides every number in O(sqrt(M)). Precomputing a SMALLEST PRIME
     *  FACTOR sieve once lets each number be factorised in O(log v) instead.
     *
     *  -> O(M log log M) up front, then O(n log M) total factorisation.
     *
     *  time  = O(M log log M + n log M)
     *  space = O(M)
     */
    public int largestComponentSize_1(int[] nums) {
        int m = 0;
        for (int v : nums) {
            m = Math.max(m, v);
        }

        // smallest prime factor sieve
        int[] spf = new int[m + 1];
        for (int i = 2; i <= m; i++) {
            if (spf[i] == 0) {
                for (int j = i; j <= m; j += i) {
                    if (spf[j] == 0) {
                        spf[j] = i;
                    }
                }
            }
        }

        int[] par = new int[m + 1];
        for (int i = 0; i <= m; i++) {
            par[i] = i;
        }

        for (int v : nums) {
            int x = v;
            while (x > 1) {
                int p = spf[x];
                uf2Union(par, v, p);
                while (x % p == 0) {
                    x /= p;
                }
            }
        }

        Map<Integer, Integer> cnt = new HashMap<>();
        int res = 0;
        for (int v : nums) {
            int c = cnt.merge(uf2Find(par, v), 1, Integer::sum);
            res = Math.max(res, c);
        }
        return res;
    }

    private int uf2Find(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    private void uf2Union(int[] par, int a, int b) {
        int ra = uf2Find(par, a);
        int rb = uf2Find(par, b);
        if (ra != rb) {
            par[ra] = rb;
        }
    }

    // V2
    // IDEA: UNION THE NUMBERS DIRECTLY VIA A `prime -> first number seen` MAP
    /**
     *  V0 puts primes and numbers in ONE label space, which works but means the
     *  parent array must be sized by max(nums) even when n is tiny.
     *
     *  Here the union-find is over the n INDICES only, and a map
     *  `prime -> index of the first number carrying it` links numbers sharing a
     *  prime. Memory becomes O(n + #primes) instead of O(M).
     *
     *  time  = O(n * sqrt(M) * alpha)
     *  space = O(n + number of distinct primes)
     */
    public int largestComponentSize_2(int[] nums) {
        int n = nums.length;
        int[] par = new int[n];
        int[] sz = new int[n];
        for (int i = 0; i < n; i++) {
            par[i] = i;
            sz[i] = 1;
        }

        Map<Integer, Integer> owner = new HashMap<>(); // prime -> index

        for (int i = 0; i < n; i++) {
            int x = nums[i];
            for (int f = 2; (long) f * f <= x; f++) {
                if (x % f == 0) {
                    linkByPrime(par, sz, owner, f, i);
                    while (x % f == 0) {
                        x /= f;
                    }
                }
            }
            if (x > 1) {
                linkByPrime(par, sz, owner, x, i);
            }
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            res = Math.max(res, sz[uf2Find(par, i)]);
        }
        return res;
    }

    private void linkByPrime(int[] par, int[] sz, Map<Integer, Integer> owner, int prime, int i) {
        Integer prev = owner.get(prime);
        if (prev == null) {
            owner.put(prime, i);
            return;
        }
        int ra = uf2Find(par, prev);
        int rb = uf2Find(par, i);
        if (ra != rb) {
            par[ra] = rb;
            sz[rb] += sz[ra];
        }
    }

    // V3
    // IDEA: BUILD THE GRAPH EXPLICITLY (prime -> list of numbers) + BFS COMPONENTS
    /**
     *  No union-find at all: bucket the indices by prime factor, add an edge
     *  between consecutive members of each bucket, then walk the components with
     *  a plain BFS.
     *
     *  Slower and heavier, but it makes the underlying GRAPH visible, which is
     *  what the problem statement actually describes.
     *
     *  time  = O(n * sqrt(M) + n)
     *  space = O(n + number of distinct primes)
     */
    public int largestComponentSize_3(int[] nums) {
        int n = nums.length;

        Map<Integer, List<Integer>> byPrime = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int x = nums[i];
            for (int f = 2; (long) f * f <= x; f++) {
                if (x % f == 0) {
                    byPrime.computeIfAbsent(f, k -> new ArrayList<>()).add(i);
                    while (x % f == 0) {
                        x /= f;
                    }
                }
            }
            if (x > 1) {
                byPrime.computeIfAbsent(x, k -> new ArrayList<>()).add(i);
            }
        }

        // chain each bucket: i0 - i1 - i2 ... keeps the edge count linear
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (List<Integer> bucket : byPrime.values()) {
            for (int t = 1; t < bucket.size(); t++) {
                adj.get(bucket.get(t - 1)).add(bucket.get(t));
                adj.get(bucket.get(t)).add(bucket.get(t - 1));
            }
        }

        boolean[] seen = new boolean[n];
        int res = 0;
        Deque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (seen[i]) {
                continue;
            }
            seen[i] = true;
            q.offer(i);
            int size = 0;
            while (!q.isEmpty()) {
                int cur = q.poll();
                size += 1;
                for (int nxt : adj.get(cur)) {
                    if (!seen[nxt]) {
                        seen[nxt] = true;
                        q.offer(nxt);
                    }
                }
            }
            res = Math.max(res, size);
        }

        return res;
    }

}
