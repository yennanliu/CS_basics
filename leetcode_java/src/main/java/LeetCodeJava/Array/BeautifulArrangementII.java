package LeetCodeJava.Array;

// https://leetcode.com/problems/beautiful-arrangement-ii/

/**
 *  667. Beautiful Arrangement II
 *  Medium
 *
 *  Given two integers n and k, construct a list answer that contains n
 *  different positive integers ranging from 1 to n and obeys the following
 *  requirement:
 *
 *   - Suppose this list is answer = [a1, a2, a3, ... , an], then the list
 *     [|a1 - a2|, |a2 - a3|, |a3 - a4|, ... , |a(n-1) - an|] has exactly
 *     k distinct integers.
 *
 *  Return the list answer. If there are multiple valid answers, return any of them.
 *
 *  Example 1:
 *    Input: n = 3, k = 1
 *    Output: [1,2,3]
 *    Explanation: The [1,2,3] has three different positive integers ranging from
 *    1 to 3, and the [1,1] has exactly 1 distinct integer: 1
 *
 *  Example 2:
 *    Input: n = 3, k = 2
 *    Output: [1,3,2]
 *    Explanation: The [1,3,2] has three different positive integers ranging from
 *    1 to 3, and the [2,1] has exactly 2 distinct integers: 1 and 2.
 *
 *  Constraints:
 *    1 <= k < n <= 10^4
 */
public class BeautifulArrangementII {

    // V0
    // IDEA: build the "zig-zag" prefix 1, k+1, 2, k, ... so the first k gaps are
    //       k, k-1, ..., 1 (all distinct), then append k+2 ... n in order (gap 1).
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public int[] constructArray(int n, int k) {
        int[] res = new int[n];
        int idx = 0;
        res[idx++] = 1;
        for (int x = 0; x < k; x++) {
            int sign = (x % 2 == 0) ? 1 : -1;
            res[idx] = res[idx - 1] + sign * (k - x);
            idx++;
        }
        for (int v = k + 2; v <= n; v++) {
            res[idx++] = v;
        }
        return res;
    }

    // V1
    // IDEA: two pointers from both ends; while k > 1 keep alternating (each step
    //       produces a brand new gap), then run out the rest monotonically.
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public int[] constructArray_1(int n, int k) {
        int[] res = new int[n];
        int left = 1, right = n, idx = 0;
        int kk = k;
        while (left <= right) {
            if (kk % 2 == 1) {
                res[idx++] = left++;
            } else {
                res[idx++] = right--;
            }
            if (kk > 1) {
                kk--;
            }
        }
        return res;
    }
}
