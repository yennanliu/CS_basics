package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/k-th-smallest-prime-fraction/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 786. K-th Smallest Prime Fraction
 * Medium
 *
 * You are given a sorted integer array arr containing 1 and prime numbers,
 * where all the integers of arr are unique. You are also given an integer k.
 *
 * For every i and j where 0 <= i < j < arr.length, we consider the fraction arr[i] / arr[j].
 *
 * Return the kth smallest fraction considered. Return your answer as an array of
 * integers of size 2, where answer[0] == arr[i] and answer[1] == arr[j].
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,5], k = 3
 * Output: [2,5]
 * Explanation: The fractions to be considered in sorted order are:
 * 1/5, 1/3, 2/5, 1/2, 3/5, and 2/3.
 * The third fraction is 2/5.
 *
 * Example 2:
 *
 * Input: arr = [1,7], k = 1
 * Output: [1,7]
 *
 *
 * Constraints:
 *
 * 2 <= arr.length <= 1000
 * 1 <= arr[i] <= 3 * 10^4
 * arr[0] == 1
 * arr[i] is a prime number for i > 0.
 * All the numbers of arr are unique and sorted in strictly increasing order.
 * 1 <= k <= arr.length * (arr.length - 1) / 2
 *
 * Follow up: Can you solve the problem with better than O(n^2) complexity?
 *
 */
public class KthSmallestPrimeFraction {

    // V0
    // IDEA: HEAP (k-way merge)
    /**
     *   For a FIXED denominator arr[j], the fractions arr[0]/arr[j] < arr[1]/arr[j] < ...
     *   form a SORTED list. So we have (n-1) sorted lists and want the k-th
     *   smallest overall -> classic K-WAY MERGE with a min heap.
     *
     *   Seed the heap with the HEAD of each list (arr[0]/arr[j]),
     *   then pop k-1 times, pushing the SUCCESSOR of every popped element.
     *
     *   time  = O(n + k * log(n))
     *   space = O(n)
     */
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;

        // {numerator index, denominator index}, ordered by the fraction value
        PriorityQueue<int[]> heap = new PriorityQueue<>(
                Comparator.comparingDouble(p -> (double) arr[p[0]] / (double) arr[p[1]]));

        for (int j = 1; j < n; j++) {
            heap.add(new int[] { 0, j });
        }

        for (int t = 0; t < k - 1; t++) {
            int[] cur = heap.poll();
            int i = cur[0];
            int j = cur[1];
            /** NOTE !!!
             *
             *  the NEXT fraction sharing the same denominator,
             *  valid only while the numerator index stays below j
             */
            if (i + 1 < j) {
                heap.add(new int[] { i + 1, j });
            }
        }

        int[] top = heap.peek();
        return new int[] { arr[top[0]], arr[top[1]] };
    }

    // V0-1
    // IDEA: BINARY SEARCH ON THE ANSWER VALUE + TWO POINTERS
    /**
     *   Binary search a threshold `mid` in (0, 1) and COUNT how many fractions
     *   are < mid. Because arr is sorted, for a growing denominator index j the
     *   largest numerator index satisfying arr[i] < mid * arr[j] is NON-DECREASING,
     *   so the count can be done with two pointers in O(n).
     *
     *   While counting we also keep the BIGGEST fraction that is still < mid;
     *   when the count hits EXACTLY k, that biggest one IS the k-th smallest.
     *
     *   -> runtime is independent of k (unlike V0)
     *
     *   time  = O(n * log(1/eps))
     *   space = O(1)
     */
    public int[] kthSmallestPrimeFraction_0_1(int[] arr, int k) {
        int n = arr.length;
        double lo = 0.0;
        double hi = 1.0;

        while (true) {
            double mid = (lo + hi) / 2.0;

            int cnt = 0;
            int p = 0; // best (largest) fraction seen that is < mid : p / q
            int q = 1;
            int i = -1; // largest index with arr[i] < mid * arr[j]

            for (int j = 1; j < n; j++) {
                while (i + 1 < j && arr[i + 1] < mid * arr[j]) {
                    i += 1;
                }
                cnt += i + 1;

                /** NOTE !!!
                 *
                 *  compare arr[i]/arr[j] > p/q WITHOUT floating division
                 *  -> cross multiply instead
                 */
                if (i >= 0 && arr[i] * q > p * arr[j]) {
                    p = arr[i];
                    q = arr[j];
                }
            }

            if (cnt == k) {
                return new int[] { p, q };
            } else if (cnt < k) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
    }


    // V1
    // IDEA: BRUTE FORCE -- materialise every fraction and sort
    /**
     *  All n(n-1)/2 pairs, sorted by value, take the (k-1)-th.
     *
     *  O(n^2 log n) at n = 1000 is ~5 * 10^5 pairs -- actually fine here, and it is
     *  the definition of the answer, so it doubles as the oracle.
     *
     *  time  = O(n^2 log n)
     *  space = O(n^2)
     */
    public int[] kthSmallestPrimeFraction_1(int[] arr, int k) {
        int n = arr.length;
        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairs.add(new int[] { arr[i], arr[j] });
            }
        }
        // compare a/b vs c/d as a*d vs c*b -> no floating point at all
        pairs.sort((p, q) -> Long.compare((long) p[0] * q[1], (long) q[0] * p[1]));
        return pairs.get(k - 1);
    }

    // V2
    // IDEA: QUICKSELECT over the materialised pairs (no full sort)
    /**
     *  We only need the k-th element, not the whole order, so partition instead of
     *  sorting -- O(n^2) expected rather than O(n^2 log n).
     *
     *  The pivot comparison is again CROSS MULTIPLICATION, so the selection is
     *  exact with no float tolerance to tune.
     *
     *  time  = O(n^2) expected
     *  space = O(n^2)
     */
    public int[] kthSmallestPrimeFraction_2(int[] arr, int k) {
        int n = arr.length;
        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairs.add(new int[] { arr[i], arr[j] });
            }
        }

        int[][] a = pairs.toArray(new int[0][]);
        select(a, 0, a.length - 1, k - 1);
        return a[k - 1];
    }

    private void select(int[][] a, int lo, int hi, int target) {
        while (lo < hi) {
            int p = partition(a, lo, hi);
            if (p == target) {
                return;
            }
            if (p < target) {
                lo = p + 1;
            } else {
                hi = p - 1;
            }
        }
    }

    private int partition(int[][] a, int lo, int hi) {
        int[] pivot = a[hi];
        int store = lo;
        for (int i = lo; i < hi; i++) {
            // a[i] < pivot   <=>   a[i][0] * pivot[1] < pivot[0] * a[i][1]
            if ((long) a[i][0] * pivot[1] < (long) pivot[0] * a[i][1]) {
                int[] t = a[i];
                a[i] = a[store];
                a[store] = t;
                store += 1;
            }
        }
        int[] t = a[hi];
        a[hi] = a[store];
        a[store] = t;
        return store;
    }

    // V3
    // IDEA: K-WAY MERGE WITH A LINEAR POINTER SCAN (no heap)
    /**
     *  Same (n-1) sorted lists as V0, but the smallest head is found by scanning
     *  the pointer array instead of by a PriorityQueue.
     *
     *  O(k * n) rather than O(k log n) -- worse for large k, yet it allocates
     *  nothing per step and is the version that makes the k-way merge structure
     *  most visible.
     *
     *  time  = O(k * n)
     *  space = O(n)
     */
    public int[] kthSmallestPrimeFraction_3(int[] arr, int k) {
        int n = arr.length;
        int[] num = new int[n]; // num[j] = current numerator index for denominator j
        for (int j = 1; j < n; j++) {
            num[j] = 0;
        }

        int bestI = 0;
        int bestJ = 1;
        for (int step = 0; step < k; step++) {
            bestI = -1;
            bestJ = -1;
            for (int j = 1; j < n; j++) {
                int i = num[j];
                if (i >= j) {
                    continue; // this list is exhausted
                }
                if (bestJ == -1
                        || (long) arr[i] * arr[bestJ] < (long) arr[bestI] * arr[j]) {
                    bestI = i;
                    bestJ = j;
                }
            }
            num[bestJ] += 1;
        }

        return new int[] { arr[bestI], arr[bestJ] };
    }

}
