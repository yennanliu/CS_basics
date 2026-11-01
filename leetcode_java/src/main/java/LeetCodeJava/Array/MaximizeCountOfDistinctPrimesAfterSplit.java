package LeetCodeJava.Array;

// https://leetcode.com/problems/maximize-count-of-distinct-primes-after-split/description/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 3569. Maximize Count of Distinct Primes After Split
 * Hard
 *
 * You are given an integer array nums having length n and a 2D integer array
 * queries where queries[i] = [idx, val].
 *
 * For each query:
 *
 * 1. Update nums[idx] = val.
 * 2. Choose an integer k with 1 <= k < n to split the array into the non-empty
 *    prefix nums[0..k-1] and suffix nums[k..n-1] such that the sum of the counts
 *    of distinct prime values in each part is maximum.
 *
 * Note: The changes made to the array in one query persist into the next query.
 *
 * Return an array containing the result for each query, in the order they are given.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,1,2], queries = [[1,2],[3,3]]
 * Output: [3,4]
 *
 * Example 2:
 *
 * Input: nums = [2,1,4], queries = [[0,1]]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 5 * 10^4
 * 1 <= queries.length <= 5 * 10^4
 * 1 <= nums[i] <= 10^5
 * 0 <= queries[i][0] < nums.length
 * 1 <= queries[i][1] <= 10^5
 *
 */
public class MaximizeCountOfDistinctPrimesAfterSplit {

    // V0
    // IDEA: "TOTAL PRIMES + INTERVAL COVERAGE" WITH A RANGE-ADD / MAX SEGMENT TREE
    //   a prime p is counted in the prefix iff first[p] < k and in the suffix iff
    //   last[p] >= k; at least one always holds, so every distinct prime scores 1,
    //   and scores a SECOND point exactly when first[p] < k <= last[p].
    //   answer = (#distinct primes) + max over k of (#primes whose [first+1, last] covers k).
    //   an update moves at most two primes' intervals: retract the old one (range -1),
    //   lay the new one (range +1); the segment tree root is the max coverage.
    //   each prime keeps its occurrence indices in a TreeSet for first / last.
    //   (recomputing prefix / suffix counts per query is O(q * n) and times out.)
    /**
     * time = O((N + Q) * log N + V log log V)
     * space = O(N + V)
     */
    public int[] maximumCount(int[] nums, int[][] queries) {
        boolean[] isPrime = sieve(MAX_VALUE);
        int n = nums.length;

        // split point k (1..n-1) lives at segment-tree position k - 1
        positions = n - 1;
        maxTree = new int[4 * positions];
        addTag = new int[4 * positions];

        Map<Integer, TreeSet<Integer>> occurrences = new HashMap<>();
        int[] current = nums.clone();
        for (int i = 0; i < n; i++) {
            if (isPrime[current[i]]) {
                attach(occurrences, current[i], i);
            }
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int index = queries[q][0];
            int value = queries[q][1];
            int old = current[index];
            if (old != value) {
                if (isPrime[old]) {
                    detach(occurrences, old, index);
                }
                current[index] = value;
                if (isPrime[value]) {
                    attach(occurrences, value, index);
                }
            }
            // every distinct prime present scores once, plus the best double-count
            answer[q] = occurrences.size() + maxTree[1];
        }
        return answer;
    }

    private static final int MAX_VALUE = 100000;

    private int positions;
    private int[] maxTree;  // max over the node's range, including this node's own tag
    private int[] addTag;   // value added to the whole node range (never pushed down)

    /** Adds index to prime p's occurrences, moving p's coverage interval. */
    private void attach(Map<Integer, TreeSet<Integer>> occurrences, int prime, int index) {
        TreeSet<Integer> indices = occurrences.computeIfAbsent(prime, x -> new TreeSet<>());
        changeInterval(indices, -1);
        indices.add(index);
        changeInterval(indices, +1);
    }

    /** Removes index from prime p's occurrences, moving p's coverage interval. */
    private void detach(Map<Integer, TreeSet<Integer>> occurrences, int prime, int index) {
        TreeSet<Integer> indices = occurrences.get(prime);
        changeInterval(indices, -1);
        indices.remove(index);
        if (indices.isEmpty()) {
            occurrences.remove(prime);
        } else {
            changeInterval(indices, +1);
        }
    }

    /** Adds delta over positions [first, last - 1], i.e. split points k in [first + 1, last]. */
    private void changeInterval(TreeSet<Integer> indices, int delta) {
        if (indices.size() < 2) {
            return; // a single occurrence never double-counts
        }
        int first = indices.first();
        int last = indices.last();
        update(1, 0, positions - 1, first, last - 1, delta);
    }

    /** Range add on [left, right]; maxTree[node] = max(children) + addTag[node]. */
    private void update(int node, int nodeLeft, int nodeRight, int left, int right, int delta) {
        if (right < nodeLeft || nodeRight < left) {
            return;
        }
        if (left <= nodeLeft && nodeRight <= right) {
            // the whole node range is covered: tag it instead of descending
            maxTree[node] += delta;
            addTag[node] += delta;
            return;
        }
        int mid = nodeLeft + (nodeRight - nodeLeft) / 2;
        update(2 * node, nodeLeft, mid, left, right, delta);
        update(2 * node + 1, mid + 1, nodeRight, left, right, delta);
        maxTree[node] = Math.max(maxTree[2 * node], maxTree[2 * node + 1]) + addTag[node];
    }

    /** Sieve of Eratosthenes up to limit inclusive. */
    private boolean[] sieve(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }
        for (int i = 2; (long) i * i <= limit; i++) {
            if (isPrime[i]) {
                for (int multiple = i * i; multiple <= limit; multiple += i) {
                    isPrime[multiple] = false;
                }
            }
        }
        return isPrime;
    }
}
