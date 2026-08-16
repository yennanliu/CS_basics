package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/coin-path/description/

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 656. Coin Path
 * Hard
 * Lock: Prime
 *
 * You are given an integer array coins (1-indexed) of length n and an integer maxJump.
 * You can jump to any index i of the array coins if coins[i] != -1 and you have to pay
 * coins[i] when you visit index i. In addition to that, if you are currently at index i,
 * you can only jump to any index i + k where i + k <= n and k is a value in the range
 * [1, maxJump].
 *
 * You are initially positioned at index 1 (coins[1]). You want to find the path that
 * reaches index n with the minimum cost.
 *
 * Return an integer array of the indices that you will visit in order so that you can
 * reach index n with the minimum cost. If there are multiple paths with the same cost,
 * return the lexicographically smallest such path. If it is not possible to reach index
 * n, return an empty array.
 *
 * A path p1 = [Pa1, Pa2, ..., Pax] of length x is lexicographically smaller than
 * p2 = [Pb1, Pb2, ..., Pbx] of length y, if and only if at the first j where Paj and Pbj
 * differ, Paj < Pbj; when no such j exists, then x < y.
 *
 * Example 1:
 *
 * Input: coins = [1,2,4,-1,2], maxJump = 2
 * Output: [1,3,5]
 *
 * Example 2:
 *
 * Input: coins = [1,2,4,-1,2], maxJump = 1
 * Output: []
 *
 * Constraints:
 *
 * 1 <= coins.length <= 1000
 * -1 <= coins[i] <= 100
 * coins[1] != -1
 * 1 <= maxJump <= 100
 *
 */
public class CoinPath {

    // V0
    // IDEA: BACKWARD DP + FORWARD GREEDY RECONSTRUCTION
    /**
     *  DP def (0-indexed internally):
     *    - f[i] = minimum cost to travel from index i to the LAST index (coins[i] included)
     *    - f[i] = INF if index i is blocked (coins[i] == -1) or the end is unreachable
     *
     *  DP eq:
     *    - f[n-1] = coins[n-1]
     *    - f[i]   = coins[i] + min(f[i+1] .. f[i+maxJump])
     *
     *  Filling f from RIGHT TO LEFT is required because f[i] depends on LARGER indices.
     *
     *  Reconstruction (LEXICOGRAPHICALLY SMALLEST):
     *    Walk i = 0, 1, 2, ... keeping `remain` = the cost still to be paid from the
     *    current position onward. Whenever f[i] == remain, index i lies on an OPTIMAL
     *    path -- and because we sweep LEFT TO RIGHT, it is the SMALLEST such index,
     *    which is exactly what `lexicographically smallest` asks for.
     *
     *    NOTE !!! such an index ALWAYS exists within maxJump of the previous one
     *             (that is what the min in the DP equation guarantees), so the
     *             reconstructed jumps are legal.
     *
     *  time  = O(n * maxJump)
     *  space = O(n)
     */
    public List<Integer> cheapestJump(int[] coins, int maxJump) {
        int n = coins.length;
        final long INF = Long.MAX_VALUE / 4;

        List<Integer> res = new ArrayList<>();

        // the DESTINATION itself must be steppable
        if (coins[n - 1] == -1) {
            return res;
        }

        long[] f = new long[n];
        Arrays.fill(f, INF);
        f[n - 1] = coins[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            if (coins[i] == -1) {
                continue;
            }
            for (int j = i + 1; j < Math.min(n, i + maxJump + 1); j++) {
                if (f[j] + coins[i] < f[i]) {
                    f[i] = f[j] + coins[i];
                }
            }
        }

        if (f[0] >= INF) {
            return res;
        }

        // greedily pick the EARLIEST index that is still on an optimal path
        long remain = f[0];
        for (int i = 0; i < n; i++) {
            if (f[i] == remain) {
                res.add(i + 1); // the answer is 1-INDEXED
                remain -= coins[i];
            }
        }
        return res;
    }


    // V1
    // IDEA: FORWARD DP + PARENT POINTERS
    /**
     *  Fill cost[] left to right, relaxing forward into the next maxJump slots and
     *  remembering the PREDECESSOR that achieved each best cost.
     *
     *  The lexicographic tie-break becomes `prefer the smaller predecessor index`,
     *  which is a local comparison instead of V0's backwards reconstruction sweep.
     *
     *  time  = O(n * maxJump)
     *  space = O(n)
     */
    public List<Integer> cheapestJump_1(int[] coins, int maxJump) {
        int n = coins.length;
        final long INF = Long.MAX_VALUE / 4;

        long[] cost = new long[n];
        int[] prev = new int[n];
        Arrays.fill(cost, INF);
        Arrays.fill(prev, -1);

        List<Integer> res = new ArrayList<>();
        if (coins[0] == -1 || coins[n - 1] == -1) {
            return res;
        }
        cost[0] = coins[0];

        for (int i = 0; i < n; i++) {
            if (cost[i] >= INF) {
                continue;
            }
            for (int j = i + 1; j <= Math.min(n - 1, i + maxJump); j++) {
                if (coins[j] == -1) {
                    continue;
                }
                long cand = cost[i] + coins[j];
                if (cand < cost[j]) {
                    cost[j] = cand;
                    prev[j] = i;
                } else if (cand == cost[j] && prev[j] != -1) {
                    /** NOTE !!!
                     *
                     *  equal cost -> keep whichever FULL path is lexicographically
                     *  smaller. The endpoint j MUST be appended before comparing:
                     *  a shorter predecessor path is not necessarily better once
                     *  j is on the end (e.g. [1,2,3] beats [1,3] for j = 3, even
                     *  though [1] beats [1,2] as bare prefixes).
                     */
                    List<Integer> candPath = pathOf(prev, i);
                    candPath.add(j + 1);
                    List<Integer> curPath = pathOf(prev, prev[j]);
                    curPath.add(j + 1);
                    if (comparePathsLex(candPath, curPath) < 0) {
                        prev[j] = i;
                    }
                }
            }
        }

        if (cost[n - 1] >= INF) {
            return res;
        }
        int cur = n - 1;
        while (cur != -1) {
            res.add(cur + 1);
            cur = prev[cur];
        }
        Collections.reverse(res);
        return res;
    }

    /** lexicographic order on two 1-indexed paths */
    private int comparePathsLex(List<Integer> a, List<Integer> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return a.get(i) - b.get(i);
            }
        }
        return a.size() - b.size();
    }

    private List<Integer> pathOf(int[] prev, int end) {
        List<Integer> p = new ArrayList<>();
        int cur = end;
        while (cur != -1) {
            p.add(cur + 1);
            cur = prev[cur];
        }
        Collections.reverse(p);
        return p;
    }

    // V2
    // IDEA: DIJKSTRA over the index graph
    /**
     *  Each index is a node and a jump is an edge of weight coins[target], so this
     *  is a shortest path -- with the tie-break folded into the priority queue's
     *  comparator (cost first, then the path).
     *
     *  Overkill for a DAG, but it is the version that survives if jumps were ever
     *  allowed BACKWARDS, where the left-to-right DP would break.
     *
     *  time  = O(n * maxJump * log n)
     *  space = O(n)
     */
    public List<Integer> cheapestJump_2(int[] coins, int maxJump) {
        int n = coins.length;
        List<Integer> empty = new ArrayList<>();
        if (coins[0] == -1 || coins[n - 1] == -1) {
            return empty;
        }

        // {cost, path}
        PriorityQueue<Object[]> pq = new PriorityQueue<>((x, y) -> {
            long cx = (Long) x[0];
            long cy = (Long) y[0];
            if (cx != cy) {
                return Long.compare(cx, cy);
            }
            return comparePaths((List<Integer>) x[1], (List<Integer>) y[1]);
        });

        List<Integer> start = new ArrayList<>();
        start.add(1);
        pq.add(new Object[] { (long) coins[0], start });

        boolean[] done = new boolean[n];
        while (!pq.isEmpty()) {
            Object[] cur = pq.poll();
            long cost = (Long) cur[0];
            List<Integer> path = (List<Integer>) cur[1];
            int at = path.get(path.size() - 1) - 1;

            if (done[at]) {
                continue;
            }
            done[at] = true;
            if (at == n - 1) {
                return path;
            }

            for (int j = at + 1; j <= Math.min(n - 1, at + maxJump); j++) {
                if (coins[j] == -1 || done[j]) {
                    continue;
                }
                List<Integer> np = new ArrayList<>(path);
                np.add(j + 1);
                pq.add(new Object[] { cost + coins[j], np });
            }
        }
        return empty;
    }

    private int comparePaths(List<Integer> a, List<Integer> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return a.get(i) - b.get(i);
            }
        }
        return a.size() - b.size();
    }

    // V3
    // IDEA: BACKWARD DP CARRYING THE WHOLE PATH
    /**
     *  Same backwards fill as V0, but each cell stores the best PATH object rather
     *  than just its cost, so the tie-break is a direct list comparison and no
     *  reconstruction pass is needed.
     *
     *  O(n^2) memory in the worst case, but nothing has to be re-derived.
     *
     *  time  = O(n * maxJump * n)
     *  space = O(n^2)
     */
    public List<Integer> cheapestJump_3(int[] coins, int maxJump) {
        int n = coins.length;
        final long INF = Long.MAX_VALUE / 4;
        List<Integer> empty = new ArrayList<>();
        if (coins[n - 1] == -1) {
            return empty;
        }

        long[] cost = new long[n];
        List<List<Integer>> path = new ArrayList<>();
        Arrays.fill(cost, INF);
        for (int i = 0; i < n; i++) {
            path.add(null);
        }

        cost[n - 1] = coins[n - 1];
        List<Integer> tail = new ArrayList<>();
        tail.add(n);
        path.set(n - 1, tail);

        for (int i = n - 2; i >= 0; i--) {
            if (coins[i] == -1) {
                continue;
            }
            for (int j = i + 1; j <= Math.min(n - 1, i + maxJump); j++) {
                if (cost[j] >= INF) {
                    continue;
                }
                long cand = cost[j] + coins[i];
                List<Integer> candPath = new ArrayList<>();
                candPath.add(i + 1);
                candPath.addAll(path.get(j));

                if (cand < cost[i]
                        || (cand == cost[i] && comparePaths(candPath, path.get(i)) < 0)) {
                    cost[i] = cand;
                    path.set(i, candPath);
                }
            }
        }

        return cost[0] >= INF ? empty : path.get(0);
    }

}
