package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/odd-even-jump/description/

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/**
 * 975. Odd Even Jump
 * Hard
 *
 * You are given an integer array arr. From some starting index, you can make a series of
 * jumps. The (1st, 3rd, 5th, ...) jumps in the series are called odd-numbered jumps, and
 * the (2nd, 4th, 6th, ...) jumps in the series are called even-numbered jumps. Note that
 * the jumps are numbered, not the indices.
 *
 * You may jump forward from index i to index j (with i < j) in the following way:
 *
 * During odd-numbered jumps (i.e., jumps 1, 3, 5, ...), you jump to the index j such that
 * arr[i] <= arr[j] and arr[j] is the smallest possible value. If there are multiple such
 * indices j, you can only jump to the smallest such index j.
 * During even-numbered jumps (i.e., jumps 2, 4, 6, ...), you jump to the index j such
 * that arr[i] >= arr[j] and arr[j] is the largest possible value. If there are multiple
 * such indices j, you can only jump to the smallest such index j.
 * It may be the case that for some index i, there are no legal jumps.
 *
 * A starting index is good if, starting from that index, you can reach the end of the
 * array (index arr.length - 1) by jumping some number of times (possibly 0 or more than
 * once).
 *
 * Return the number of good starting indices.
 *
 * Example 1:
 *
 * Input: arr = [10,13,12,14,15]
 * Output: 2
 *
 * Example 2:
 *
 * Input: arr = [2,3,1,1,4]
 * Output: 3
 *
 * Example 3:
 *
 * Input: arr = [5,1,3,4,2]
 * Output: 3
 *
 * Constraints:
 *
 * 1 <= arr.length <= 2 * 10^4
 * 0 <= arr[i] < 10^5
 *
 */
public class OddEvenJump {

    // V0
    // IDEA: SORTING + MONOTONIC STACK (precompute jump targets) + BACKWARD DP
    /**
     *  Step 1 - precompute, for every index i:
     *     oddNext[i]  = the index we land on with an ODD  jump (or -1)
     *     evenNext[i] = the index we land on with an EVEN jump (or -1)
     *
     *     TRICK: sort indices by (value, index) ASCENDING. Walking that order and
     *     keeping a MONOTONIC STACK of indices, whenever the incoming index is
     *     LARGER than the stack top, the incoming index is exactly the
     *     `next greater-or-equal value at a later position` for the popped index.
     *     Sorting by (-value, index) gives the EVEN jumps the same way.
     *
     *  Step 2 - BACKWARD DP:
     *     odd[i]  = true if starting at i and making an ODD  jump we can reach the end
     *     even[i] = true if starting at i and making an EVEN jump we can reach the end
     *     odd[i]  = even[oddNext[i]]
     *     even[i] = odd[evenNext[i]]
     *     odd[n-1] = even[n-1] = true
     *
     *  answer = number of i with odd[i] true (the 1st jump is ALWAYS odd)
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int oddEvenJumps(int[] arr) {
        int n = arr.length;

        // odd jump  : smallest value >= arr[i], ties -> smallest index
        Integer[] byAsc = new Integer[n];
        for (int i = 0; i < n; i++) {
            byAsc[i] = i;
        }
        java.util.Arrays.sort(byAsc,
                Comparator.<Integer>comparingInt(i -> arr[i]).thenComparingInt(i -> i));
        int[] oddNext = nextTarget(byAsc, n);

        // even jump : largest value <= arr[i], ties -> smallest index
        Integer[] byDesc = new Integer[n];
        for (int i = 0; i < n; i++) {
            byDesc[i] = i;
        }
        java.util.Arrays.sort(byDesc,
                Comparator.<Integer>comparingInt(i -> -arr[i]).thenComparingInt(i -> i));
        int[] evenNext = nextTarget(byDesc, n);

        boolean[] odd = new boolean[n];
        boolean[] even = new boolean[n];
        odd[n - 1] = true;
        even[n - 1] = true;

        for (int i = n - 2; i >= 0; i--) {
            if (oddNext[i] != -1) {
                odd[i] = even[oddNext[i]];
            }
            if (evenNext[i] != -1) {
                even[i] = odd[evenNext[i]];
            }
        }

        int res = 0;
        for (boolean b : odd) {
            if (b) {
                res += 1;
            }
        }
        return res;
    }

    /**
     * for each index, the next index to its RIGHT that comes
     * right after it in this sorted order
     */
    private int[] nextTarget(Integer[] sortedIndices, int n) {
        int[] res = new int[n];
        java.util.Arrays.fill(res, -1);

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i : sortedIndices) {
            while (!stack.isEmpty() && i > stack.peek()) {
                res[stack.pop()] = i;
            }
            stack.push(i);
        }
        return res;
    }

}
