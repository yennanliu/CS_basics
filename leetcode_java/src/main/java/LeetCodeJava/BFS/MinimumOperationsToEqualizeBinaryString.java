package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-operations-to-equalize-binary-string/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

/**
 * 3666. Minimum Operations to Equalize Binary String
 * Hard
 *
 * You are given a binary string s, and an integer k.
 *
 * In one operation, you must choose exactly k different indices and flip each
 * '0' to '1' and each '1' to '0'.
 *
 * Return the minimum number of operations required to make all characters in
 * the string equal to '1'. If it is not possible, return -1.
 *
 * Example 1:
 *
 * Input: s = "110", k = 1
 * Output: 1
 * Explanation:
 * There is one '0' in s.
 * Since k = 1, we can flip it directly in one operation.
 *
 * Example 2:
 *
 * Input: s = "0101", k = 3
 * Output: 2
 * Explanation:
 * One optimal set of operations choosing k = 3 indices in each operation is:
 * Operation 1: Flip indices [0, 1, 3]. s changes from "0101" to "1000".
 * Operation 2: Flip indices [1, 2, 3]. s changes from "1000" to "1111".
 * Thus, the minimum number of operations is 2.
 *
 * Example 3:
 *
 * Input: s = "101", k = 2
 * Output: -1
 * Explanation:
 * Since k = 2 and s has only one '0', it is impossible to flip exactly k
 * indices to make all '1'. Hence, the answer is -1.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either '0' or '1'.
 * 1 <= k <= s.length
 *
 */
public class MinimumOperationsToEqualizeBinaryString {

    // V0
    // IDEA: BFS OVER "NUMBER OF ZEROS", WITH TWO ORDERED SETS (EVEN / ODD) OF UNVISITED COUNTS
    //   an operation flips ANY k indices, so only the zero count z matters.
    //   Flipping a zeros and k - a ones sends z -> z + k - 2a, with
    //       max(0, k - (n - z)) <= a <= min(k, z)
    //   so the successors of z are every other value in an interval (step 2).
    //   Keep unvisited counts in a TreeSet per parity and remove each one the
    //   first time BFS reaches it, so overlapping intervals are never rescanned.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minOperations(String s, int k) {
        int n = s.length();
        int zeros = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                zeros++;
            }
        }
        if (zeros == 0) {
            return 0;
        }
        if (k > n) {
            return -1;
        }

        // unvisited.get(p) = zero counts in [0, n] with parity p not yet reached
        List<TreeSet<Integer>> unvisited = new ArrayList<>();
        unvisited.add(new TreeSet<>());
        unvisited.add(new TreeSet<>());
        for (int z = 0; z <= n; z++) {
            if (z != zeros) {
                unvisited.get(z % 2).add(z);
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(zeros);
        int operations = 0;
        while (!queue.isEmpty()) {
            operations++;
            int layerSize = queue.size();
            for (int i = 0; i < layerSize; i++) {
                int z = queue.poll();
                // a = number of zeros flipped in this operation
                int minFlipZeros = Math.max(0, k - (n - z));
                int maxFlipZeros = Math.min(k, z);
                if (minFlipZeros > maxFlipZeros) {
                    continue;
                }
                int lowZ = z + k - 2 * maxFlipZeros;
                int highZ = z + k - 2 * minFlipZeros;
                TreeSet<Integer> candidates = unvisited.get(lowZ % 2);

                Integer next = candidates.ceiling(lowZ);
                while (next != null && next <= highZ) {
                    if (next == 0) {
                        return operations;
                    }
                    queue.offer(next);
                    candidates.remove(next);
                    next = candidates.ceiling(next);
                }
            }
        }
        return -1;
    }

    // V1
    // IDEA: SAME BFS, WITH A UNION-FIND "NEXT UNVISITED SAME-PARITY COUNT" ARRAY
    //   nextFree[x] points to the first unvisited count >= x with x's parity;
    //   visiting x sets nextFree[x] = x + 2. Near-linear instead of N log N.
    /**
     * time = O(N * alpha(N))
     * space = O(N)
     */
    public int minOperations_1(String s, int k) {
        int n = s.length();
        int zeros = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                zeros++;
            }
        }
        if (zeros == 0) {
            return 0;
        }
        if (k > n) {
            return -1;
        }

        // values reach at most n, and x + 2 must stay in range
        int[] nextFree = new int[n + 5];
        for (int i = 0; i < nextFree.length; i++) {
            nextFree[i] = i;
        }
        nextFree[zeros] = zeros + 2;

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(zeros);
        int operations = 0;
        while (!queue.isEmpty()) {
            operations++;
            int layerSize = queue.size();
            for (int i = 0; i < layerSize; i++) {
                int z = queue.poll();
                int minFlipZeros = Math.max(0, k - (n - z));
                int maxFlipZeros = Math.min(k, z);
                if (minFlipZeros > maxFlipZeros) {
                    continue;
                }
                int lowZ = z + k - 2 * maxFlipZeros;
                int highZ = z + k - 2 * minFlipZeros;
                int next = find(nextFree, lowZ);
                while (next <= highZ) {
                    if (next == 0) {
                        return operations;
                    }
                    queue.offer(next);
                    nextFree[next] = next + 2;
                    next = find(nextFree, next + 2);
                }
            }
        }
        return -1;
    }

    /** First unvisited value >= x with x's parity, with iterative path compression. */
    private int find(int[] nextFree, int x) {
        int root = x;
        while (nextFree[root] != root) {
            root = nextFree[root];
        }
        while (nextFree[x] != root) {
            int next = nextFree[x];
            nextFree[x] = root;
            x = next;
        }
        return root;
    }
}
