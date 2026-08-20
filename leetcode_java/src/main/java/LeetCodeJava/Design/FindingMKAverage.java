package LeetCodeJava.Design;

// https://leetcode.com/problems/finding-mk-average/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1825. Finding MK Average
 *  Hard
 *
 *  You are given two integers, m and k, and a stream of integers. You are tasked to
 *  implement a data structure that calculates the MKAverage for the stream.
 *
 *  The MKAverage can be calculated using these steps:
 *    - If the number of the elements in the stream is less than m you should
 *      consider the MKAverage to be -1. Otherwise, copy the last m elements of the
 *      stream to a separate container.
 *    - Remove the smallest k elements and the largest k elements from the container.
 *    - Calculate the average value for the rest of the elements rounded down to the
 *      nearest integer.
 *
 *  Implement the MKAverage class:
 *    MKAverage(int m, int k) Initializes the MKAverage object with an empty stream
 *      and the two integers m and k.
 *    void addElement(int num) Inserts a new element num into the stream.
 *    int calculateMKAverage() Calculates and returns the MKAverage for the current
 *      stream rounded down to the nearest integer.
 *
 *  Example 1:
 *    Input
 *      ["MKAverage","addElement","addElement","calculateMKAverage","addElement",
 *       "calculateMKAverage","addElement","addElement","addElement",
 *       "calculateMKAverage"]
 *      [[3,1],[3],[1],[],[10],[],[5],[5],[5],[]]
 *    Output
 *      [null,null,null,-1,null,3,null,null,null,5]
 *    Explanation
 *      only 2 elements exist -> -1
 *      last 3 = [3,1,10] -> drop min & max -> [3] -> 3
 *      last 3 = [5,5,5]  -> drop min & max -> [5] -> 5
 *
 *  Constraints:
 *    3 <= m <= 10^5
 *    1 < k*2 < m
 *    1 <= num <= 10^5
 *    At most 10^5 calls will be made to addElement and calculateMKAverage.
 */
public class FindingMKAverage {

    // V0
    // IDEA: SLIDING WINDOW (deque) + FENWICK TREE INDEXED BY *VALUE*
    //
    //       two independent jobs:
    //         - keep only the last m elements  -> a deque, evict the oldest
    //         - "sum of the t smallest inside the window" fast
    //
    //       since 1 <= num <= 10^5, index the Fenwick tree BY VALUE and keep two
    //       parallel trees: how many copies of that value are live, and their sum.
    //       sumOfSmallest(t) is then a binary-lifting descent: walk the bits high
    //       -> low, greedily taking a block while the running count stays BELOW t;
    //       the shortfall is supplied by the next value (this is also what makes
    //       duplicates work -- we may take only PART of one value's copies).
    //
    //       answer = ( sumOfSmallest(m-k) - sumOfSmallest(k) ) / (m - 2k)
    //       i.e. drop the k smallest and the k largest, never sorting anything.
    /**
     * time = O(log M) per addElement / calculateMKAverage, M = 10^5
     * space = O(M + m)
     */
    private static final int MAX_V = 100000;

    private final int m;
    private final int k;
    private final Deque<Integer> window;
    private final int[] cnt;   // fenwick: how many live values
    private final long[] tot;  // fenwick: sum of those values
    private final int highBit;

    public FindingMKAverage(int m, int k) {
        this.m = m;
        this.k = k;
        this.window = new ArrayDeque<>();
        this.cnt = new int[MAX_V + 1];
        this.tot = new long[MAX_V + 1];
        int p = 1;
        while (p * 2 <= MAX_V) {
            p *= 2;
        }
        this.highBit = p;
    }

    public void addElement(int num) {
        window.addLast(num);
        update(num, 1);
        if (window.size() > m) {
            int old = window.pollFirst();
            update(old, -1);
        }
    }

    public int calculateMKAverage() {
        if (window.size() < m) {
            return -1;
        }
        long middle = sumOfSmallest(m - k) - sumOfSmallest(k);
        return (int) (middle / (m - 2L * k));
    }

    private void update(int v, int d) {
        for (int i = v; i <= MAX_V; i += i & (-i)) {
            cnt[i] += d;
            tot[i] += (long) d * v;
        }
    }

    /** sum of the t smallest live values */
    private long sumOfSmallest(int t) {
        if (t <= 0) {
            return 0L;
        }
        int idx = 0;
        int c = 0;
        long s = 0L;
        for (int pw = highBit; pw > 0; pw >>= 1) {
            int nxt = idx + pw;
            if (nxt <= MAX_V && c + cnt[nxt] < t) {
                idx = nxt;
                c += cnt[nxt];
                s += tot[nxt];
            }
        }
        // value (idx + 1) supplies the remaining (t - c) copies
        return s + (long) (t - c) * (idx + 1);
    }
}
