package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-the-number-of-substrings-with-dominant-ones/

/**
 *  3234. Count the Number of Substrings With Dominant Ones
 *  Medium
 *
 *  You are given a binary string s.
 *
 *  Return the number of substrings with dominant ones.
 *
 *  A string has dominant ones if the number of ones in the string is greater
 *  than or equal to the square of the number of zeros in the string.
 *
 *  Example 1:
 *    Input: s = "00011"
 *    Output: 5
 *
 *  Example 2:
 *    Input: s = "101101"
 *    Output: 16
 *
 *  Constraints:
 *    1 <= s.length <= 4 * 10^4
 *    s consists only of characters '0' and '1'.
 */
public class CountTheNumberOfSubstringsWithDominantOnes {

    // V0
    // IDEA: THE ZERO COUNT IS BOUNDED BY sqrt(N) — ENUMERATE IT
    //       a substring with z zeros needs at least z^2 ones, so its length is
    //       at least z^2 + z. with n <= 4*10^4 that caps z at about 200 — far
    //       fewer possibilities than the O(N^2) substrings.
    //       so for each start index walk the possible zero counts z. the
    //       substrings holding exactly z zeros end in a contiguous window:
    //       from the z-th zero at or after the start, up to just before the
    //       (z+1)-th. intersecting that window with the length requirement
    //           end >= start + z^2 + z - 1
    //       gives the count for this (start, z) in O(1).
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numberOfSubstrings(String s) {
        int n = s.length();
        int[] zeros = new int[n];
        int zc = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                zeros[zc++] = i;
            }
        }

        long res = 0L;
        int k = 0; // index into zeros of the first zero at or after `start`
        for (int start = 0; start < n; start++) {
            while (k < zc && zeros[k] < start) {
                k++;
            }
            for (int z = 0; ; z++) {
                if ((long) z * z + z > n) {
                    break; // even the whole string is too short
                }
                int lo;
                int hi;
                if (z == 0) {
                    lo = start;
                    hi = (k < zc) ? zeros[k] - 1 : n - 1;
                } else {
                    if (k + z - 1 >= zc) {
                        break; // not that many zeros left
                    }
                    lo = zeros[k + z - 1];
                    hi = (k + z < zc) ? zeros[k + z] - 1 : n - 1;
                }
                int need = start + z * z + z - 1; // minimum end index
                lo = Math.max(lo, need);
                if (hi >= lo) {
                    res += hi - lo + 1;
                }
            }
        }
        return (int) res;
    }
}
