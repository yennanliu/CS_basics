package LeetCodeJava.Heap;

// https://leetcode.com/problems/final-array-state-after-k-multiplication-operations-ii/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  3266. Final Array State After K Multiplication Operations II
 *  Hard
 *
 *  You are given an integer array nums, an integer k, and an integer multiplier.
 *
 *  You need to perform k operations on nums. In each operation:
 *    - Find the minimum value x in nums. If there are multiple occurrences of the
 *      minimum value, select the one that appears first.
 *    - Replace the selected minimum value x with x * multiplier.
 *
 *  After the k operations, apply modulo 10^9 + 7 to every value in nums, and return
 *  the resulting array.
 *
 *  Example 1:
 *    Input: nums = [2,1,3,5,6], k = 5, multiplier = 2
 *    Output: [8,4,6,5,6]
 *
 *  Example 2:
 *    Input: nums = [100000,2000], k = 2, multiplier = 1000000
 *    Output: [999999307,999999993]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^4
 *    1 <= nums[i] <= 10^9
 *    1 <= k <= 10^9
 *    1 <= multiplier <= 10^6
 */
public class FinalArrayStateAfterKMultiplicationOperationsII {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: SIMULATE UNTIL THE VALUES LEVEL OFF, THEN FINISH IN BULK
    //
    //   while some element is still below the ORIGINAL maximum the heap must be
    //   walked one step at a time - but that phase is short: multiplier >= 2 at
    //   least doubles an element each time, so after about n * log2(max) steps
    //   every value has caught up with the maximum.
    //
    //   from there the heap is "flat" - one full sweep multiplies everybody once -
    //   so the remaining r operations split evenly: each element gets r / n more
    //   multiplications, and the first r % n in the current heap order get one
    //   extra.
    //
    //   multiplier == 1 changes nothing, so it is answered immediately. The values
    //   explode past 64 bits, hence modular exponentiation in the bulk phase.
    /**
     * time = O(n log(max) log n + n log n)
     * space = O(n)
     */
    public int[] getFinalState(int[] nums, int k, int multiplier) {
        int n = nums.length;
        int[] res = new int[n];

        if (multiplier == 1) {
            for (int i = 0; i < n; i++) {
                res[i] = (int) (nums[i] % MOD);
            }
            return res;
        }

        Comparator<long[]> byValueThenIndex = new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                if (a[0] != b[0]) {
                    return Long.compare(a[0], b[0]);
                }
                return Long.compare(a[1], b[1]);
            }
        };
        PriorityQueue<long[]> heap = new PriorityQueue<>(byValueThenIndex);
        long limit = 0L;
        for (int i = 0; i < n; i++) {
            heap.add(new long[]{nums[i], i});
            limit = Math.max(limit, nums[i]);
        }

        // phase 1 : real simulation until every value reaches the initial max
        long remain = k;
        while (remain > 0 && heap.peek()[0] < limit) {
            long[] cur = heap.poll();
            heap.add(new long[]{cur[0] * multiplier, cur[1]});
            remain--;
        }

        // phase 2 : the heap order is now stable, so spread out what is left
        long[][] rest = heap.toArray(new long[0][]);
        Arrays.sort(rest, byValueThenIndex);
        long base = remain / n;
        long extra = remain % n;
        for (int t = 0; t < n; t++) {
            long times = base + (t < extra ? 1 : 0);
            long v = rest[t][0] % MOD;
            res[(int) rest[t][1]] = (int) (v * modPow(multiplier, times) % MOD);
        }
        return res;
    }

    private long modPow(long b, long exp) {
        long r = 1L;
        b %= MOD;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                r = r * b % MOD;
            }
            b = b * b % MOD;
            exp >>= 1;
        }
        return r;
    }
}
