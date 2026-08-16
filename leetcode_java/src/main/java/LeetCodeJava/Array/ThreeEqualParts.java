package LeetCodeJava.Array;

// https://leetcode.com/problems/three-equal-parts/description/
/**
 * 927. Three Equal Parts
 * Hard
 *
 * You are given an array arr which consists of only zeros and ones, divide the array
 * into three non-empty parts such that all of these parts represent the same binary value.
 *
 * If it is possible, return any [i, j] with i + 1 < j, such that:
 *
 * arr[0], arr[1], ..., arr[i] is the first part,
 * arr[i + 1], arr[i + 2], ..., arr[j - 1] is the second part, and
 * arr[j], arr[j + 1], ..., arr[arr.length - 1] is the third part.
 * All three parts have equal binary values.
 *
 * If it is not possible, return [-1, -1].
 *
 * Note that the entire part is used when considering what binary value it represents.
 * For example, [1,1,0] represents 6 in decimal, not 3. Also, leading zeros are allowed,
 * so [0,1,1] and [1,1] represent the same value.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,0,1,0,1]
 * Output: [0,3]
 *
 * Example 2:
 *
 * Input: arr = [1,1,0,1,1]
 * Output: [-1,-1]
 *
 * Example 3:
 *
 * Input: arr = [1,1,0,0,1]
 * Output: [0,2]
 *
 *
 * Constraints:
 *
 * 3 <= arr.length <= 3 * 10^4
 * arr[i] is 0 or 1
 *
 */
public class ThreeEqualParts {

    // V0
    // IDEA: COUNT THE 1s + THREE POINTERS
    /**
     *  1) total number of 1s must be DIVISIBLE BY 3, otherwise impossible.
     *     if there are no 1s at all, any split works -> [0, n - 1].
     *
     *  2) each part must contain exactly cnt = total / 3 ones.
     *     Locate the FIRST '1' of each part (ignoring leading zeros):
     *
     *         0 1 1 0 0 0 1 1 0 0 0 0 0 1 1 0 0
     *           ^         ^             ^
     *           i         j             k
     *
     *  3) walk i, j, k forward in LOCKSTEP and require arr[i] == arr[j] == arr[k].
     *     The third part's TRAILING ZEROS are what fixes the amount of padding the
     *     first two parts get, so we stop when k reaches the end of the array.
     *
     *  4) if k landed exactly on n, the split is valid -> [i - 1, j].
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int[] threeEqualParts(int[] arr) {
        int n = arr.length;

        int total = 0;
        for (int x : arr) {
            total += x;
        }

        if (total % 3 != 0) {
            return new int[] { -1, -1 };
        }
        if (total == 0) {
            // all zeros -> any split is fine
            return new int[] { 0, n - 1 };
        }

        int cnt = total / 3;

        /** NOTE !!!
         *
         *  i, j, k point at the FIRST `1` of part 1, 2, 3 respectively
         */
        int i = find(arr, 1);
        int j = find(arr, cnt + 1);
        int k = find(arr, 2 * cnt + 1);

        // advance all three in LOCKSTEP until the last part runs out
        while (k < n && arr[i] == arr[j] && arr[j] == arr[k]) {
            i += 1;
            j += 1;
            k += 1;
        }

        return k == n ? new int[] { i - 1, j } : new int[] { -1, -1 };
    }

    /** index of the x-th `1` (1-indexed) */
    private int find(int[] arr, int x) {
        int seen = 0;
        for (int idx = 0; idx < arr.length; idx++) {
            seen += arr[idx];
            if (seen == x) {
                return idx;
            }
        }
        return -1;
    }

}
