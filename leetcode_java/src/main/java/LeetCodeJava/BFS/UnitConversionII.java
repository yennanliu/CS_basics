package LeetCodeJava.BFS;

// https://leetcode.com/problems/unit-conversion-ii/description/
// https://leetcode.ca/2025-05-28-3535-Unit-Conversion-II/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 3535. Unit Conversion II
 * Medium
 *
 * There are n types of units indexed from 0 to n - 1.
 *
 * You are given a 2D integer array conversions of length n - 1, where
 * conversions[i] = [sourceUnit_i, targetUnit_i, conversionFactor_i]. This
 * indicates that a single unit of type sourceUnit_i is equivalent to
 * conversionFactor_i units of type targetUnit_i.
 *
 * You are also given a 2D integer array queries of length q, where queries[i] =
 * [unitA_i, unitB_i].
 *
 * Return an array answer of length q where answer[i] is the number of units of
 * type unitB_i equivalent to 1 unit of type unitA_i, and can be represented as p/q
 * where p and q are coprime. Return each answer[i] as pq^-1 modulo 10^9 + 7, where
 * q^-1 represents the multiplicative inverse of q modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: conversions = [[0,1,2],[0,2,6]], queries = [[1,2],[1,0]]
 *
 * Output: [3,500000004]
 *
 * Explanation:
 *
 * In the first query, we can convert unit 1 into 3 units of type 2 using the
 * inverse of conversions[0], then conversions[1].
 *
 * In the second query, we can convert unit 1 into 1/2 units of type 0 using the
 * inverse of conversions[0]. We return 500000004 since it is the multiplicative
 * inverse of 2.
 *
 * Example 2:
 *
 * Input: conversions = [[0,1,2],[0,2,6],[0,3,8],[2,4,2],[2,5,4],[3,6,3]], queries
 * = [[1,2],[0,4],[6,5],[4,6],[6,1]]
 *
 * Output: [3,12,1,2,83333334]
 *
 * Explanation:
 *
 * In the first query, we can convert unit 1 into 3 units of type 2 using the
 * inverse of conversions[0], then conversions[1].
 *
 * In the second query, we can convert unit 0 into 12 units of type 4 using
 * conversions[1], then conversions[3].
 *
 * In the third query, we can convert unit 6 into 1 unit of type 5 using the
 * inverse of conversions[5], the inverse of conversions[2], conversions[1], then
 * conversions[4].
 *
 * In the fourth query, we can convert unit 4 into 2 units of type 6 using the
 * inverse of conversions[3], the inverse of conversions[1], conversions[2], then
 * conversions[5].
 *
 * In the fifth query, we can convert unit 6 into 1/12 units of type 1 using the
 * inverse of conversions[5], the inverse of conversions[2], then conversions[0].
 * We return 83333334 since it is the multiplicative inverse of 12.
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 *
 * conversions.length == n - 1
 *
 * 0 <= sourceUnit_i, targetUnit_i < n
 *
 * 1 <= conversionFactor_i <= 10^9
 *
 * 1 <= q <= 10^5
 *
 * queries.length == q
 *
 * 0 <= unitA_i, unitB_i < n
 *
 * It is guaranteed that unit 0 can be uniquely converted into any other unit
 * through a combination of forward or backward conversions.
 *
 */
public class UnitConversionII {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: BFS FROM UNIT 0 OVER THE CONVERSION TREE, ONE RATIO PER NODE + MODULAR INVERSE
    //   n - 1 conversions with unit 0 reaching everything form a tree, so every
    //   chain factors through the root. Let ratio[u] = units of u worth one unit
    //   of 0 (product of factors on the path 0 -> u; a backwards edge divides,
    //   i.e. multiplies by the modular inverse). Then 1 unit of A = ratio[B] /
    //   ratio[A] units of B -> answer = ratio[B] * inverse(ratio[A]) mod p.
    //   Inverses use Fermat: x^(p-2) mod p, since p is prime.
    /**
     * time = O(N + Q log MOD)
     * space = O(N)
     */
    public int[] queryConversions(int[][] conversions, int[][] queries) {
        int n = conversions.length + 1;
        // graph.get(u) holds {neighbour, factor to multiply by when going u -> neighbour}
        List<List<long[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] conversion : conversions) {
            int source = conversion[0];
            int target = conversion[1];
            long factor = conversion[2] % MOD;
            graph.get(source).add(new long[] { target, factor });
            graph.get(target).add(new long[] { source, modPow(factor, MOD - 2) });
        }

        long[] ratio = new long[n];
        boolean[] visited = new boolean[n];
        ratio[0] = 1;
        visited[0] = true;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int unit = queue.poll();
            for (long[] edge : graph.get(unit)) {
                int next = (int) edge[0];
                if (!visited[next]) {
                    visited[next] = true;
                    ratio[next] = ratio[unit] * edge[1] % MOD;
                    queue.offer(next);
                }
            }
        }

        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            long fromRatio = ratio[queries[i][0]];
            long toRatio = ratio[queries[i][1]];
            answer[i] = (int) (toRatio * modPow(fromRatio, MOD - 2) % MOD);
        }
        return answer;
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            // odd exponent: this power of base is part of the product
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
