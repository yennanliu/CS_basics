package LeetCodeJava.Heap;

// https://leetcode.com/problems/digit-operations-to-make-two-integers-equal/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 *  3377. Digit Operations to Make Two Integers Equal
 *  Medium
 *
 *  You are given two integers n and m that consist of the same number of digits.
 *
 *  You can perform the following operations any number of times:
 *    - Choose any digit from n that is not 9 and increase it by 1.
 *    - Choose any digit from n that is not 0 and decrease it by 1.
 *
 *  The integer n must not be a prime number at any point, including its original
 *  value and after each operation.
 *
 *  The cost of a transformation is the sum of all values that n takes throughout
 *  the operations performed.
 *
 *  Return the minimum cost to transform n into m. If it is impossible, return -1.
 *
 *  Example 1:
 *    Input: n = 10, m = 12
 *    Output: 85
 *    Explanation: 10 -> 20 -> 21 -> 22 -> 12, and 10+20+21+22+12 = 85.
 *
 *  Example 2:
 *    Input: n = 4, m = 8
 *    Output: -1
 *
 *  Example 3:
 *    Input: n = 6, m = 2
 *    Output: -1
 *    Explanation: 2 is already a prime, so n can never equal m.
 *
 *  Constraints:
 *    1 <= n, m < 10^4
 *    n and m consist of the same number of digits.
 */
public class DigitOperationsToMakeTwoIntegersEqual {

    // V0
    // IDEA: DIJKSTRA OVER THE NUMBERS, WHERE ENTERING A STATE COSTS ITS VALUE
    //
    //   every operation keeps the digit count, so the states are just the
    //   integers of that width (at most 9000 of them), with an edge between two
    //   numbers differing by +/-1 in a single digit.
    //
    //   the cost is the SUM of the values visited, i.e. ARRIVING at a node costs
    //   that node's value - a shortest-path problem with node weights, which
    //   Dijkstra handles directly once seeded with n's own value.
    //
    //   primes are forbidden everywhere, endpoints included, so a sieve marks
    //   them up front and they are simply never entered.
    /**
     * time = O(S * D * 10 * log S)   // S = #states <= 9000, D = #digits <= 4
     * space = O(S)
     */
    public int minOperations(int n, int m) {
        int digits = String.valueOf(n).length();
        int hi = (int) Math.pow(10, digits) - 1;

        boolean[] isPrime = new boolean[hi + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        if (hi >= 1) {
            isPrime[1] = false;
        }
        for (int p = 2; (long) p * p <= hi; p++) {
            if (isPrime[p]) {
                for (int v = p * p; v <= hi; v += p) {
                    isPrime[v] = false;
                }
            }
        }

        if (isPrime[n] || isPrime[m]) {
            return -1;
        }

        long[] dist = new long[hi + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[n] = n;

        // {cost, value}
        PriorityQueue<long[]> pq = new PriorityQueue<>(new java.util.Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        pq.add(new long[]{n, n});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int node = (int) cur[1];
            if (d > dist[node]) {
                continue;
            }
            if (node == m) {
                return (int) d;
            }
            char[] s = String.valueOf(node).toCharArray();
            for (int i = 0; i < s.length; i++) {
                char old = s[i];
                int c = old - '0';
                for (int delta = -1; delta <= 1; delta += 2) {
                    int nd = c + delta;
                    if (nd < 0 || nd > 9) {
                        continue;
                    }
                    if (i == 0 && nd == 0 && digits > 1) {
                        continue;                      // no leading zero
                    }
                    s[i] = (char) ('0' + nd);
                    int nxt = Integer.parseInt(new String(s));
                    s[i] = old;
                    if (isPrime[nxt]) {
                        continue;                      // primes are off limits
                    }
                    long cand = d + nxt;
                    if (cand < dist[nxt]) {
                        dist[nxt] = cand;
                        pq.add(new long[]{cand, nxt});
                    }
                }
            }
        }
        return -1;
    }
}
