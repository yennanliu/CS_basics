package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-servers-that-handled-most-number-of-requests/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  1606. Find Servers That Handled Most Number of Requests
 *  Hard
 *
 *  You have k servers numbered from 0 to k-1 that handle multiple requests
 *  simultaneously. Each server has infinite computational capacity but cannot handle
 *  more than one request at a time. Requests are assigned like this:
 *    - The ith (0-indexed) request arrives.
 *    - If all servers are busy, the request is dropped.
 *    - If the (i % k)th server is available, assign the request to that server.
 *    - Otherwise assign it to the next available server, wrapping around to 0.
 *
 *  You are given a strictly increasing array arrival, where arrival[i] is the arrival
 *  time of the ith request, and load, where load[i] is how long it takes to complete.
 *  A server is busiest if it handled the most requests. Return the IDs of the
 *  busiest server(s), in any order.
 *
 *  Example 1:
 *    Input: k = 3, arrival = [1,2,3,4,5], load = [5,2,3,3,3]
 *    Output: [1]
 *    Explanation: server 1 handled two requests, servers 0 and 2 one each.
 *
 *  Example 2:
 *    Input: k = 3, arrival = [1,2,3], load = [10,12,11]
 *    Output: [0,1,2]
 *
 *  Constraints:
 *    1 <= k <= 10^5
 *    1 <= arrival.length == load.length <= 10^5
 *    1 <= arrival[i], load[i] <= 10^9
 *    arrival is strictly increasing.
 */
public class FindServersThatHandledMostNumberOfRequests {

    private int k;
    private int[] tree;      // BIT over free servers, 1-indexed
    private int power;       // largest power of two <= k

    // V0
    // IDEA: MIN-HEAP OF BUSY SERVERS (by finish time) + BIT OVER THE FREE ONES
    //
    //   two structures:
    //     busy : min-heap of (endTime, server) so every server that finished before
    //            the current arrival can be released; each server is popped at most
    //            once per request -> amortised O(log k).
    //     BIT  : marks free servers with 1, answering "first free server at index
    //            >= p, wrapping to 0" in O(log k):
    //              c = #free in [0, p-1]
    //              if freeTotal > c -> take the (c+1)-th free server
    //              else             -> wrap and take the 1st free server
    //            the t-th free server is located by binary lifting on the tree.
    //
    //   NOTE: a linear scan from i % k is O(k) per request and TLEs at
    //         k = arrival.length = 10^5.
    /**
     * time = O((n + k) log k)
     * space = O(k)
     */
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        this.k = k;
        this.tree = new int[k + 1];
        this.power = 1;
        while (this.power * 2 <= k) {
            this.power *= 2;
        }

        for (int s = 1; s <= k; s++) {         // every server starts free
            add(s, 1);
        }
        int free = k;

        // {endTime, server}
        PriorityQueue<long[]> busy = new PriorityQueue<>(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        int[] cnt = new int[k];

        for (int i = 0; i < arrival.length; i++) {
            long start = arrival[i];
            while (!busy.isEmpty() && busy.peek()[0] <= start) {
                int s = (int) busy.poll()[1];
                add(s + 1, 1);
                free++;
            }
            if (free == 0) {
                continue;                      // dropped
            }

            int p = i % k;
            int c = pref(p);                   // free servers among ids 0..p-1
            int rank = (free > c) ? c + 1 : 1;
            int idx = kth(rank);
            int server = idx - 1;

            add(idx, -1);
            free--;
            cnt[server]++;
            busy.add(new long[]{start + load[i], server});
        }

        int best = 0;
        for (int c : cnt) {
            best = Math.max(best, c);
        }
        List<Integer> res = new ArrayList<>();
        for (int s = 0; s < k; s++) {
            if (cnt[s] == best) {
                res.add(s);
            }
        }
        return res;
    }

    private void add(int i, int v) {            // i is 1-indexed
        while (i <= k) {
            tree[i] += v;
            i += i & (-i);
        }
    }

    private int pref(int i) {                   // sum of 1..i
        int s = 0;
        while (i > 0) {
            s += tree[i];
            i -= i & (-i);
        }
        return s;
    }

    private int kth(int t) {                    // smallest idx with pref(idx) >= t
        int pos = 0;
        int bit = power;
        while (bit > 0) {
            if (pos + bit <= k && tree[pos + bit] < t) {
                pos += bit;
                t -= tree[pos];
            }
            bit >>= 1;
        }
        return pos + 1;
    }
}
