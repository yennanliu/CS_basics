package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-reverse-operations/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

/**
 * 2612. Minimum Reverse Operations
 * Hard
 *
 * You are given an integer n and an integer p representing an array arr of length n where all elements are set to 0's, except position p which is set to 1. You are also given an integer array banned containing restricted positions. Perform the following operation on arr:
 *
 * Reverse a subarray with size k if the single 1 is not set to a position in banned.
 *
 * Return an integer array answer with n results where the ith result is the minimum number of operations needed to bring the single 1 to position i in arr, or -1 if it is impossible.
 *
 *
 * Example 1:
 *
 * Input: n = 4, p = 0, banned = [1,2], k = 4
 *
 * Output: [0,-1,-1,1]
 *
 * Explanation:
 *
 * Initially 1 is placed at position 0 so the number of operations we need for position 0 is 0.
 * We can never place 1 on the banned positions, so the answer for positions 1 and 2 is -1.
 * Perform the operation of size 4 to reverse the whole array.
 * After a single operation 1 is at position 3 so the answer for position 3 is 1.
 *
 * Example 2:
 *
 * Input: n = 5, p = 0, banned = [2,4], k = 3
 *
 * Output: [0,-1,-1,-1,-1]
 *
 * Explanation:
 *
 * Initially 1 is placed at position 0 so the number of operations we need for position 0 is 0.
 * We cannot perform the operation on the subarray positions [0, 2] because position 2 is in banned.
 * Because 1 cannot be set at position 2, it is impossible to set 1 at other positions in more operations.
 *
 * Example 3:
 *
 * Input: n = 4, p = 2, banned = [0,1,3], k = 1
 *
 * Output: [-1,-1,0,-1]
 *
 * Explanation:
 *
 * Perform operations of size 1 and 1 never changes its position.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 0 <= p <= n - 1
 * 0 <= banned.length <= n - 1
 * 0 <= banned[i] <= n - 1
 * 1 <= k <= n
 * banned[i] != p
 * all values in banned are unique
 *
 */
public class MinimumReverseOperations {

    // V0
    // IDEA: BFS + TWO ORDERED SETS (EVEN / ODD) OF UNVISITED POSITIONS
    //   with the 1 at i and the window [L, L + k - 1] reversed, the 1 lands at
    //   j = 2L + k - 1 - i. L ranges over [max(0, i - k + 1), min(i, n - k)], so
    //   j sweeps every other index in [lo, hi] with
    //       lo = max(i - k + 1, k - 1 - i),  hi = min(i + k - 1, 2n - k - 1 - i)
    //   all with lo's parity. Rescanning [lo, hi] each time is O(n * k); instead
    //   keep unvisited positions in a TreeSet per parity and remove each one
    //   the first (and only) time BFS reaches it.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] minReverseOperations(int n, int p, int[] banned, int k) {
        int[] answer = new int[n];
        Arrays.fill(answer, -1);

        boolean[] isBanned = new boolean[n];
        for (int b : banned) {
            isBanned[b] = true;
        }

        // unvisited.get(0) = even positions, unvisited.get(1) = odd positions
        List<TreeSet<Integer>> unvisited = new ArrayList<>();
        unvisited.add(new TreeSet<>());
        unvisited.add(new TreeSet<>());
        for (int i = 0; i < n; i++) {
            if (i != p && !isBanned[i]) {
                unvisited.get(i % 2).add(i);
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(p);
        answer[p] = 0;
        while (!queue.isEmpty()) {
            int i = queue.poll();
            int lo = Math.max(i - k + 1, k - 1 - i);
            int hi = Math.min(i + k - 1, 2 * n - k - 1 - i);
            TreeSet<Integer> candidates = unvisited.get(lo % 2);

            // take every still-unvisited target in [lo, hi] of the right parity
            Integer j = candidates.ceiling(lo);
            while (j != null && j <= hi) {
                answer[j] = answer[i] + 1;
                queue.offer(j);
                candidates.remove(j);
                j = candidates.ceiling(j);
            }
        }
        return answer;
    }

    // V1
    // IDEA: BFS + UNION-FIND "NEXT UNVISITED SAME-PARITY POSITION"
    //   same reachability formula as V0, but the ordered set is an array:
    //   nextFree[x] points to the first unvisited position >= x with x's parity.
    //   Visiting x sets nextFree[x] = x + 2; path compression keeps finds cheap.
    /**
     * time = O(N * alpha(N))
     * space = O(N)
     */
    public int[] minReverseOperations_1(int n, int p, int[] banned, int k) {
        // +4 so x + 2 never goes out of bounds for either parity
        int[] nextFree = new int[n + 4];
        for (int i = 0; i < nextFree.length; i++) {
            nextFree[i] = i;
        }

        int[] answer = new int[n];
        Arrays.fill(answer, -1);
        answer[p] = 0;
        // the start and the banned positions are consumed up front
        nextFree[p] = p + 2;
        for (int b : banned) {
            nextFree[b] = b + 2;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(p);
        while (!queue.isEmpty()) {
            int i = queue.poll();
            int lo = Math.max(i - k + 1, k - 1 - i);
            int hi = Math.min(i + k - 1, 2 * n - k - 1 - i);
            int j = find(nextFree, lo);
            while (j <= hi) {
                answer[j] = answer[i] + 1;
                queue.offer(j);
                nextFree[j] = j + 2;
                j = find(nextFree, j + 2);
            }
        }
        return answer;
    }

    /** First unvisited position >= x with x's parity, with iterative path compression. */
    private int find(int[] nextFree, int x) {
        int root = x;
        while (nextFree[root] != root) {
            root = nextFree[root];
        }
        // point every node on the path straight at the root
        while (nextFree[x] != root) {
            int next = nextFree[x];
            nextFree[x] = root;
            x = next;
        }
        return root;
    }
}
