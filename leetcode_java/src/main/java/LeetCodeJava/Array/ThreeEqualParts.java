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


    // V1
    // IDEA: COUNT THE TRAILING ZEROS OF THE LAST BLOCK, THEN COMPARE SLICES
    /**
     *  The third part fixes the shape of the whole answer: it ends at n-1, so the
     *  number of trailing zeros AFTER its last 1 is the padding every part gets.
     *
     *  -> locate the three blocks of `cnt` ones, then verify the three slices
     *     [i1, i1 + len), [i2, i2 + len), [i3, i3 + len) are IDENTICAL,
     *     where len = (n - i3).
     *
     *  Explicit slice comparison instead of V0's lockstep walk -- easier to debug.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int[] threeEqualParts_1(int[] arr) {
        int n = arr.length;

        int total = 0;
        for (int x : arr) {
            total += x;
        }
        if (total % 3 != 0) {
            return new int[] { -1, -1 };
        }
        if (total == 0) {
            return new int[] { 0, n - 1 };
        }

        int cnt = total / 3;
        int i1 = find(arr, 1);
        int i2 = find(arr, cnt + 1);
        int i3 = find(arr, 2 * cnt + 1);

        int len = n - i3; // the third block, trailing zeros included
        if (i1 + len > i2 || i2 + len > i3) {
            return new int[] { -1, -1 };
        }
        for (int k = 0; k < len; k++) {
            if (arr[i1 + k] != arr[i3 + k] || arr[i2 + k] != arr[i3 + k]) {
                return new int[] { -1, -1 };
            }
        }

        return new int[] { i1 + len - 1, i2 + len };
    }

    // V2
    // IDEA: DERIVE THE SPLIT FROM THE TRAILING-ZERO COUNT
    /**
     *  Count the zeros after the LAST 1 (call it z). Each part must end with
     *  exactly z zeros, so walking backwards from each block's last 1 by z steps
     *  lands the cut points directly -- no search over split positions at all.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int[] threeEqualParts_2(int[] arr) {
        int n = arr.length;

        int total = 0;
        for (int x : arr) {
            total += x;
        }
        if (total % 3 != 0) {
            return new int[] { -1, -1 };
        }
        if (total == 0) {
            return new int[] { 0, n - 1 };
        }

        // trailing zeros after the very last 1
        int z = 0;
        while (arr[n - 1 - z] == 0) {
            z += 1;
        }

        int cnt = total / 3;
        int s1 = find(arr, 1);
        int s2 = find(arr, cnt + 1);
        int s3 = find(arr, 2 * cnt + 1);

        // each part is `its ones block` + z trailing zeros
        int len = n - s3;
        if (s1 + len > s2 || s2 + len > s3) {
            return new int[] { -1, -1 };
        }
        for (int k = 0; k < len; k++) {
            if (arr[s1 + k] != arr[s2 + k] || arr[s2 + k] != arr[s3 + k]) {
                return new int[] { -1, -1 };
            }
        }
        return new int[] { s1 + len - 1, s2 + len };
    }

    // V3
    // IDEA: BRUTE FORCE OVER BOTH CUTS + BINARY VALUE COMPARISON
    /**
     *  Try every (i, j) pair and compare the three parts as binary numbers with
     *  leading zeros stripped.
     *
     *  O(n^3) so it TLEs at n = 3 * 10^4, but it is the DIRECT transcription of
     *  the statement -- kept as the oracle the linear versions are checked against.
     *
     *  time  = O(n^3)
     *  space = O(n)
     */
    public int[] threeEqualParts_3(int[] arr) {
        int n = arr.length;
        for (int i = 0; i + 2 < n; i++) {
            for (int j = i + 2; j < n; j++) {
                String a = strip(arr, 0, i + 1);
                String b = strip(arr, i + 1, j);
                String c = strip(arr, j, n);
                if (a.equals(b) && b.equals(c)) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    /** arr[from, to) as a binary string with leading zeros removed */
    private String strip(int[] arr, int from, int to) {
        int k = from;
        while (k < to && arr[k] == 0) {
            k += 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int t = k; t < to; t++) {
            sb.append(arr[t]);
        }
        return sb.toString();
    }

}
