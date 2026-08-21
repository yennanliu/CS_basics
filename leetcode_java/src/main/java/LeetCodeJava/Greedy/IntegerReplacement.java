package LeetCodeJava.Greedy;

// https://leetcode.com/problems/integer-replacement/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 *  397. Integer Replacement
 *  Medium
 *
 *  Given a positive integer n, you can apply one of the following operations:
 *    1. If n is even, replace n with n / 2.
 *    2. If n is odd, replace n with either n + 1 or n - 1.
 *
 *  Return the minimum number of operations needed for n to become 1.
 *
 *  Example 1:
 *    Input: n = 8
 *    Output: 3    (8 -> 4 -> 2 -> 1)
 *
 *  Example 2:
 *    Input: n = 7
 *    Output: 4    (7 -> 8 -> 4 -> 2 -> 1)
 *
 *  Example 3:
 *    Input: n = 4
 *    Output: 2
 *
 *  Constraints:
 *    1 <= n <= 2^31 - 1
 */
public class IntegerReplacement {

    // V0
    // IDEA: greedy on bits. For odd n, going to n+1 is better when it clears more trailing
    //       1-bits, i.e. when n % 4 == 3; the only exception is n == 3 where n-1 wins.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int integerReplacement(int n) {
        long x = n;   // n can be 2^31 - 1, so n + 1 overflows int
        int steps = 0;

        while (x > 1) {
            if ((x & 1L) == 0L) {
                x >>= 1;
            } else if (x == 3L || ((x >> 1) & 1L) == 0L) {
                // n % 4 == 1  (or n == 3)  ->  n - 1
                x--;
            } else {
                // n % 4 == 3  ->  n + 1
                x++;
            }
            steps++;
        }
        return steps;
    }

    // V1
    // IDEA: memoized recursion over the reachable values.
    /**
     * time = O(log n) states amortized
     * space = O(log n)
     */
    public int integerReplacement_1(int n) {
        return dfs((long) n, new HashMap<Long, Integer>());
    }

    private int dfs(long n, Map<Long, Integer> memo) {
        if (n <= 1) {
            return 0;
        }
        Integer cached = memo.get(n);
        if (cached != null) {
            return cached;
        }

        int res;
        if ((n & 1L) == 0L) {
            res = 1 + dfs(n / 2, memo);
        } else {
            res = 1 + Math.min(dfs(n + 1, memo), dfs(n - 1, memo));
        }
        memo.put(n, res);
        return res;
    }

    // V2
    // IDEA: BFS - the answer is the shortest path from n down to 1 in the
    //       "halve / +-1" state graph, so expand level by level and stop at the first 1.
    //       Assumes nothing about bit patterns, so it is the reference for V0's greedy.
    /**
     * time = O(log^2 n) reachable states
     * space = O(log^2 n)
     */
    public int integerReplacement_2(int n) {
        if (n <= 1) {
            return 0;
        }
        Set<Long> visited = new HashSet<>();
        Deque<Long> q = new ArrayDeque<>();
        q.offer((long) n);
        visited.add((long) n);

        int steps = 0;
        while (!q.isEmpty()) {
            steps++;
            int size = q.size();
            for (int i = 0; i < size; i++) {
                long cur = q.poll();
                long[] nexts = ((cur & 1L) == 0L)
                        ? new long[]{cur / 2}
                        : new long[]{cur + 1, cur - 1};
                for (long nx : nexts) {
                    if (nx == 1L) {
                        return steps;
                    }
                    if (visited.add(nx)) {
                        q.offer(nx);
                    }
                }
            }
        }
        return steps;   // unreachable
    }
}
