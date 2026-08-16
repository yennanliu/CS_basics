package LeetCodeJava.Math;

// https://leetcode.com/problems/orderly-queue/description/

import java.util.Arrays;

/**
 * 899. Orderly Queue
 * Hard
 *
 * You are given a string s and an integer k. You can choose one of the first k letters
 * of s and append it at the end of the string.
 *
 * Return the lexicographically smallest string you could have after applying the
 * mentioned step any number of moves.
 *
 *
 * Example 1:
 *
 * Input: s = "cba", k = 1
 * Output: "acb"
 * Explanation:
 * In the first move, we move the 1st character 'c' to the end, obtaining the string
 * "bac". In the second move, we move the 1st character 'b' to the end, obtaining the
 * final result "acb".
 *
 * Example 2:
 *
 * Input: s = "baaca", k = 3
 * Output: "aaabc"
 * Explanation:
 * In the first move, we move the 1st character 'b' to the end, obtaining the string
 * "aacab". In the second move, we move the 3rd character 'c' to the end, obtaining the
 * final result "aaabc".
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 1000
 * s consist of lowercase English letters.
 *
 */
public class OrderlyQueue {

    // V0
    // IDEA: MATH / CASE SPLIT ON k
    /**
     *  k == 1:
     *     the ONLY thing we can do is ROTATE the string, so there are exactly
     *     s.length reachable strings -> just take the SMALLEST rotation.
     *
     *  k >= 2:
     *     we can swap ANY TWO ADJACENT characters, therefore ANY permutation is
     *     reachable -> the answer is simply the SORTED string.
     *
     *     WHY? for "abc[xy]def" move a, b, c to the end   -> "[xy]defabc"
     *                           move y, then x to the end -> "defabc[yx]"
     *                           move d, e, f to the end   -> "abc[yx]def"
     *     i.e. x and y got SWAPPED and nothing else changed.
     *
     *  time  = O(n^2)  // building n rotations of length n when k == 1
     *  space = O(n)
     */
    public String orderlyQueue(String s, int k) {
        if (k > 1) {
            // ANY permutation reachable -> the sorted string is the smallest
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            return new String(arr);
        }

        // k == 1 -> only ROTATIONS are reachable
        int n = s.length();
        String best = s;
        for (int i = 1; i < n; i++) {
            String rot = s.substring(i) + s.substring(0, i);
            if (rot.compareTo(best) < 0) {
                best = rot;
            }
        }
        return best;
    }

}
