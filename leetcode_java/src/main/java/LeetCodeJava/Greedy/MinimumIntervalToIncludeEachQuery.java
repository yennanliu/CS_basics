package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-interval-to-include-each-query/description/

import java.util.*;

/**
 * 1851. Minimum Interval to Include Each Query
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given a 2D integer array intervals, where intervals[i] = [lefti, righti] describes the ith interval starting at lefti and ending at righti (inclusive). The size of an interval is defined as the number of integers it contains, or more formally righti - lefti + 1.
 *
 * You are also given an integer array queries. The answer to the jth query is the size of the smallest interval i such that lefti <= queries[j] <= righti. If no such interval exists, the answer is -1.
 *
 * Return an array containing the answers to the queries.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]
 * Output: [3,3,1,4]
 * Explanation: The queries are processed as follows:
 * - Query = 2: The interval [2,4] is the smallest interval containing 2. The answer is 4 - 2 + 1 = 3.
 * - Query = 3: The interval [2,4] is the smallest interval containing 3. The answer is 4 - 2 + 1 = 3.
 * - Query = 4: The interval [4,4] is the smallest interval containing 4. The answer is 4 - 4 + 1 = 1.
 * - Query = 5: The interval [3,6] is the smallest interval containing 5. The answer is 6 - 3 + 1 = 4.
 * Example 2:
 *
 * Input: intervals = [[2,3],[2,5],[1,8],[20,25]], queries = [2,19,5,22]
 * Output: [2,-1,4,6]
 * Explanation: The queries are processed as follows:
 * - Query = 2: The interval [2,3] is the smallest interval containing 2. The answer is 3 - 2 + 1 = 2.
 * - Query = 19: None of the intervals contain 19. The answer is -1.
 * - Query = 5: The interval [2,5] is the smallest interval containing 5. The answer is 5 - 2 + 1 = 4.
 * - Query = 22: The interval [20,25] is the smallest interval containing 22. The answer is 25 - 20 + 1 = 6.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 105
 * 1 <= queries.length <= 105
 * intervals[i].length == 2
 * 1 <= lefti <= righti <= 107
 * 1 <= queries[j] <= 107
 *
 *
 */
public class MinimumIntervalToIncludeEachQuery {

    // V0
    // IDEA: PQ + SORT (fixed by gemini)
    public int[] minInterval(int[][] intervals, int[] queries) {
        int n = intervals.length;
        int m = queries.length;

        // 1. Sort intervals by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // 2. Store queries with their original indices so we can fill the result array correctly
        int[][] sortedQueries = new int[m][2];
        for (int i = 0; i < m; i++) {
            sortedQueries[i][0] = queries[i];
            sortedQueries[i][1] = i;
        }
        // Sort queries by their value
        Arrays.sort(sortedQueries, (a, b) -> Integer.compare(a[0], b[0]));

        // 3. Priority Queue: stores {size, end_time}, sorted by smallest size
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        int[] res = new int[m];
        int i = 0; // Pointer for intervals

        for (int[] query : sortedQueries) {
            int qVal = query[0];
            int qIdx = query[1];

            // 4. Add all intervals that START before or at the current query value
            while (i < n && intervals[i][0] <= qVal) {
                int size = intervals[i][1] - intervals[i][0] + 1;
                pq.add(new int[] { size, intervals[i][1] });
                i++;
            }

            // 5. Remove intervals from the top that END before the current query value
            // These are "expired" and can never satisfy this or future sorted queries
            while (!pq.isEmpty() && pq.peek()[1] < qVal) {
                pq.poll();
            }

            // 6. The smallest valid interval is at the top of the PQ
            res[qIdx] = pq.isEmpty() ? -1 : pq.peek()[0];
        }

        return res;
    }

    // V0-0-1
    // IDEA: PQ + SORT (gpt)
    public int[] minInterval_0_0_1(int[][] intervals, int[] queries) {

        // sort intervals by start
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // queries with original indices
        int[][] qs = new int[queries.length][2];
        for (int i = 0; i < queries.length; i++) {
            qs[i][0] = queries[i];
            qs[i][1] = i;
        }

        Arrays.sort(qs, (a, b) -> a[0] - b[0]);

        // min heap: [interval size, interval end]
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> a[0] - b[0]);

        int[] res = new int[queries.length];
        Arrays.fill(res, -1);

        int i = 0;

        for (int[] q : qs) {
            int query = q[0];
            int idx = q[1];

            // add intervals whose start <= query
            while (i < intervals.length && intervals[i][0] <= query) {
                int start = intervals[i][0];
                int end = intervals[i][1];
                pq.offer(new int[] { end - start + 1, end });
                i++;
            }

            // remove intervals that no longer cover query
            while (!pq.isEmpty() && pq.peek()[1] < query) {
                pq.poll();
            }

            if (!pq.isEmpty()) {
                res[idx] = pq.peek()[0];
            }
        }

        return res;
    }


    // V0-0-2
    // https://www.youtube.com/watch?v=5hQ5WWW5awQ
    

    // V1-1
    // https://neetcode.io/problems/minimum-interval-including-query
    // IDEA: BRUTE FORCE
    public int[] minInterval_1_1(int[][] intervals, int[] queries) {
        int[] res = new int[queries.length];
        int idx = 0;
        for (int q : queries) {
            int cur = -1;
            for (int[] interval : intervals) {
                int l = interval[0], r = interval[1];
                if (l <= q && q <= r) {
                    if (cur == -1 || (r - l + 1) < cur) {
                        cur = r - l + 1;
                    }
                }
            }
            res[idx++] = cur;
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/minimum-interval-including-query
    // IDEA: SWEEP LINE
    public int[] minInterval_1_2(int[][] intervals, int[] queries) {
        List<int[]> events = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            events.add(new int[]{intervals[i][0], 0, intervals[i][1] - intervals[i][0] + 1, i});
            events.add(new int[]{intervals[i][1], 2, intervals[i][1] - intervals[i][0] + 1, i});
        }

        for (int i = 0; i < queries.length; i++) {
            events.add(new int[]{queries[i], 1, i});
        }

        // Sort by time and type (end before query)
        events.sort((a, b) -> a[0] != b[0] ?
                Integer.compare(a[0], b[0]) :
                Integer.compare(a[1], b[1]));

        int[] ans = new int[queries.length];
        Arrays.fill(ans, -1);

        // Min heap storing [size, index]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        boolean[] inactive = new boolean[intervals.length];

        for (int[] event : events) {
            if (event[1] == 0) { // Interval start
                pq.offer(new int[]{event[2], event[3]});
            }
            else if (event[1] == 2) { // Interval end
                inactive[event[3]] = true;
            }
            else { // Query
                while (!pq.isEmpty() && inactive[pq.peek()[1]]) {
                    pq.poll();
                }
                if (!pq.isEmpty()) {
                    ans[event[2]] = pq.peek()[0];
                }
            }
        }

        return ans;
    }


    // V1-3
    // https://neetcode.io/problems/minimum-interval-including-query
    // IDEA: MIN HEAP
    public int[] minInterval_1_3(int[][] intervals, int[] queries) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        Map<Integer, Integer> res = new HashMap<>();
        int i = 0;
        for (int q : Arrays.stream(queries).sorted().toArray()) {
            while (i < intervals.length && intervals[i][0] <= q) {
                int l = intervals[i][0];
                int r = intervals[i][1];
                minHeap.offer(new int[]{r - l + 1, r});
                i++;
            }

            while (!minHeap.isEmpty() && minHeap.peek()[1] < q) {
                minHeap.poll();
            }
            res.put(q, minHeap.isEmpty() ? -1 : minHeap.peek()[0]);
        }
        int[] result = new int[queries.length];
        for (int j = 0; j < queries.length; j++) {
            result[j] = res.get(queries[j]);
        }
        return result;
    }

    // V1-4
    // https://neetcode.io/problems/minimum-interval-including-query
    // IDEA: Min Segment Tree (Lazy Propagation)
    public class SegmentTree {
        int n;
        int[] tree;
        int[] lazy;

        SegmentTree(int N) {
            this.n = N;
            tree = new int[4 * N];
            lazy = new int[4 * N];
            Arrays.fill(tree, Integer.MAX_VALUE);
            Arrays.fill(lazy, Integer.MAX_VALUE);
        }

        void propagate(int treeidx, int lo, int hi) {
            if (lazy[treeidx] != Integer.MAX_VALUE) {
                tree[treeidx] = Math.min(tree[treeidx], lazy[treeidx]);
                if (lo != hi) {
                    lazy[2 * treeidx + 1] = Math.min(lazy[2 * treeidx + 1], lazy[treeidx]);
                    lazy[2 * treeidx + 2] = Math.min(lazy[2 * treeidx + 2], lazy[treeidx]);
                }
                lazy[treeidx] = Integer.MAX_VALUE;
            }
        }

        void update(int treeidx, int lo, int hi, int left, int right, int val) {
            propagate(treeidx, lo, hi);
            if (lo > right || hi < left) return;
            if (lo >= left && hi <= right) {
                lazy[treeidx] = Math.min(lazy[treeidx], val);
                propagate(treeidx, lo, hi);
                return;
            }
            int mid = (lo + hi) / 2;
            update(2 * treeidx + 1, lo, mid, left, right, val);
            update(2 * treeidx + 2, mid + 1, hi, left, right, val);
            tree[treeidx] = Math.min(tree[2 * treeidx + 1], tree[2 * treeidx + 2]);
        }

        int query(int treeidx, int lo, int hi, int idx) {
            propagate(treeidx, lo, hi);
            if (lo == hi) return tree[treeidx];
            int mid = (lo + hi) / 2;
            if (idx <= mid) return query(2 * treeidx + 1, lo, mid, idx);
            else return query(2 * treeidx + 2, mid + 1, hi, idx);
        }

        void update(int left, int right, int val) {
            update(0, 0, n - 1, left, right, val);
        }

        int query(int idx) {
            return query(0, 0, n - 1, idx);
        }
    }

    public int[] minInterval_1_4(int[][] intervals, int[] queries) {
        List<Integer> points = new ArrayList<>();
        for (int[] interval : intervals) {
            points.add(interval[0]);
            points.add(interval[1]);
        }
        for (int q : queries) {
            points.add(q);
        }
        points = new ArrayList<>(new HashSet<>(points));
        Collections.sort(points);

        // Compress the points to indices
        Map<Integer, Integer> compress = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            compress.put(points.get(i), i);
        }

        // Create the segment tree
        SegmentTree segTree = new SegmentTree(points.size());

        // Update the segment tree with intervals
        for (int[] interval : intervals) {
            int start = compress.get(interval[0]);
            int end = compress.get(interval[1]);
            int length = interval[1] - interval[0] + 1;
            segTree.update(start, end, length);
        }

        // Query the segment tree for each query
        int[] ans = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int idx = compress.get(queries[i]);
            int res = segTree.query(idx);
            ans[i] = (res == Integer.MAX_VALUE) ? -1 : res;
        }

        return ans;
    }


    // V2




}
