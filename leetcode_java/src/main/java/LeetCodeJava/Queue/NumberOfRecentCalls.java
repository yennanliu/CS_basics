package LeetCodeJava.Queue;

// https://leetcode.com/problems/number-of-recent-calls/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  933. Number of Recent Calls
 *  Easy
 *
 *  You have a RecentCounter class which counts the number of recent requests
 *  within a certain time frame.
 *
 *  Implement the RecentCounter class:
 *
 *   - RecentCounter() initializes the counter with zero recent requests.
 *   - int ping(int t) adds a new request at time t, where t represents some
 *     time in milliseconds, and returns the number of requests that has
 *     happened in the past 3000 milliseconds (including the new request).
 *     Specifically, return the number of requests that have happened in the
 *     inclusive range [t - 3000, t].
 *
 *  It is guaranteed that every call to ping uses a strictly larger value of t
 *  than the previous call.
 *
 *
 *  Example 1:
 *
 *  Input: ["RecentCounter", "ping", "ping", "ping", "ping"]
 *         [[], [1], [100], [3001], [3002]]
 *  Output: [null, 1, 2, 3, 3]
 *
 *
 *  Constraints:
 *
 *  1 <= t <= 10^9
 *  Each test case will call ping with strictly increasing values of t.
 *  At most 10^4 calls will be made to ping.
 */
public class NumberOfRecentCalls {

    /**
     * Your RecentCounter object will be instantiated and called as such:
     * RecentCounter obj = new RecentCounter();
     * int param_1 = obj.ping(t);
     */

    // V0
    // IDEA: QUEUE (sliding window over time). Since t is strictly increasing,
    //       pop everything older than t - 3000 from the front.
    public static class RecentCounter {

        private final Deque<Integer> q;

        /**
         * time = O(1)
         * space = O(1)
         */
        public RecentCounter() {
            this.q = new ArrayDeque<>();
        }

        /**
         * time = O(1) amortized  (each t enters and leaves the deque once)
         * space = O(w)           (w = calls inside the 3000 ms window)
         */
        public int ping(int t) {
            this.q.addLast(t);
            while (!this.q.isEmpty() && this.q.peekFirst() < t - 3000) {
                this.q.pollFirst();
            }
            return this.q.size();
        }
    }

    // V1
    // IDEA: BINARY SEARCH over the (already sorted) list of all pings
    public static class RecentCounter2 {

        private final List<Integer> nums;

        /**
         * time = O(1)
         * space = O(1)
         */
        public RecentCounter2() {
            this.nums = new ArrayList<>();
        }

        /**
         * time = O(log m)   (m = total pings so far)
         * space = O(m)
         */
        public int ping(int t) {
            this.nums.add(t);
            int lo = lowerBound(t - 3000);
            return this.nums.size() - lo;
        }

        // first index with nums[idx] >= target
        private int lowerBound(int target) {
            int l = 0;
            int r = this.nums.size();
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (this.nums.get(mid) < target) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            return l;
        }
    }
}
