package LeetCodeJava.Heap;

// https://leetcode.com/problems/super-ugly-number/

/**
 *  313. Super Ugly Number
 *  Medium
 *
 *  A super ugly number is a positive integer whose prime factors are in the array
 *  primes.
 *
 *  Given an integer n and an array of integers primes, return the nth super ugly
 *  number. The nth super ugly number is guaranteed to fit in a 32-bit signed integer.
 *
 *  Example 1:
 *  Input: n = 12, primes = [2,7,13,19]
 *  Output: 32
 *  Explanation: [1,2,4,7,8,13,14,16,19,26,28,32] is the sequence of the first 12
 *  super ugly numbers given primes = [2,7,13,19].
 *
 *  Example 2:
 *  Input: n = 1, primes = [2,3,5]
 *  Output: 1
 *  Explanation: 1 has no prime factors, therefore all of its prime factors are in
 *  the array primes = [2,3,5].
 *
 *  Constraints:
 *  1 <= n <= 10^5
 *  1 <= primes.length <= 100
 *  2 <= primes[i] <= 1000
 *  primes[i] is guaranteed to be a prime number.
 *  All the values of primes are unique and sorted in ascending order.
 */
public class SuperUglyNumber {

    // V0
    // IDEA: K POINTERS (merge k sorted streams). ugly[i] is the min over all
    //       primes[j] * ugly[idx[j]]; every pointer that produced the min advances,
    //       which naturally de-duplicates.
    /**
     * time = O(n * k), k = primes.length
     * space = O(n + k)
     */
    public int nthSuperUglyNumber(int n, int[] primes) {

        // edge
        if (n <= 0) {
            return 0;
        }
        if (primes == null || primes.length == 0) {
            return 1;
        }

        int k = primes.length;
        int[] ugly = new int[n];
        ugly[0] = 1;

        int[] idx = new int[k];        // idx[j] = index into ugly used by primes[j]
        long[] next = new long[k];     // next[j] = primes[j] * ugly[idx[j]]
        for (int j = 0; j < k; j++) {
            next[j] = primes[j];
        }

        for (int i = 1; i < n; i++) {
            long min = Long.MAX_VALUE;
            for (int j = 0; j < k; j++) {
                if (next[j] < min) {
                    min = next[j];
                }
            }
            ugly[i] = (int) min;
            // advance every pointer that produced the min (skips duplicates)
            for (int j = 0; j < k; j++) {
                if (next[j] == min) {
                    idx[j]++;
                    next[j] = (long) primes[j] * ugly[idx[j]];
                }
            }
        }

        return ugly[n - 1];
    }

    // V1
    // IDEA: MIN HEAP of (value, primeIndex, uglyIndex)
    /**
     * time = O(n * k * log k)
     * space = O(n + k)
     */
    public int nthSuperUglyNumber_1(int n, int[] primes) {

        if (n <= 0) {
            return 0;
        }
        if (primes == null || primes.length == 0) {
            return 1;
        }

        int k = primes.length;
        int[] ugly = new int[n];
        ugly[0] = 1;

        // long[] {value, primeIdx, uglyIdx}
        java.util.PriorityQueue<long[]> pq = new java.util.PriorityQueue<>(
                new java.util.Comparator<long[]>() {
                    @Override
                    public int compare(long[] a, long[] b) {
                        return Long.compare(a[0], b[0]);
                    }
                });

        for (int j = 0; j < k; j++) {
            pq.offer(new long[]{primes[j], j, 0});
        }

        for (int i = 1; i < n; i++) {
            long min = pq.peek()[0];
            ugly[i] = (int) min;
            while (!pq.isEmpty() && pq.peek()[0] == min) {
                long[] cur = pq.poll();
                int j = (int) cur[1];
                int u = (int) cur[2] + 1;
                pq.offer(new long[]{(long) primes[j] * ugly[u], j, u});
            }
        }

        return ugly[n - 1];
    }
}
