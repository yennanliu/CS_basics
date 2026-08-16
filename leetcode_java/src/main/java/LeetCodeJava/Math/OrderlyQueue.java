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


    // V1
    // IDEA: BOOTH-STYLE ROTATION SCAN (least rotation without building them)
    /**
     *  For k == 1 the answer is the smallest rotation. Instead of materialising all
     *  n rotations (O(n^2) memory traffic), compare them in place with the classic
     *  two-candidate scan used by Booth's algorithm.
     *
     *  -> O(n) comparisons and O(1) extra memory instead of O(n^2) / O(n).
     *
     *  time  = O(n) for k == 1, O(n log n) for k > 1
     *  space = O(n) (the returned string only)
     */
    public String orderlyQueue_1(String s, int k) {
        if (k > 1) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            return new String(arr);
        }

        int n = s.length();
        int i = 0;
        int j = 1;
        int offset = 0;

        while (i < n && j < n && offset < n) {
            char a = s.charAt((i + offset) % n);
            char b = s.charAt((j + offset) % n);
            if (a == b) {
                offset += 1;
                continue;
            }
            if (a > b) {
                i = i + offset + 1;   // candidate i loses
            } else {
                j = j + offset + 1;   // candidate j loses
            }
            if (i == j) {
                j += 1;
            }
            offset = 0;
        }

        int start = Math.min(i, j);
        return s.substring(start) + s.substring(0, start);
    }

    // V2
    // IDEA: COUNTING SORT for the k > 1 branch
    /**
     *  The alphabet is 26 letters, so the sorted string can be produced by a
     *  histogram rather than a comparison sort.
     *
     *  O(n) instead of O(n log n) on the branch that dominates whenever k >= 2.
     *
     *  time  = O(n) for k > 1
     *  space = O(n)
     */
    public String orderlyQueue_2(String s, int k) {
        if (k > 1) {
            int[] cnt = new int[26];
            for (int i = 0; i < s.length(); i++) {
                cnt[s.charAt(i) - 'a'] += 1;
            }
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < 26; c++) {
                for (int t = 0; t < cnt[c]; t++) {
                    sb.append((char) ('a' + c));
                }
            }
            return sb.toString();
        }

        String best = s;
        for (int i = 1; i < s.length(); i++) {
            String rot = s.substring(i) + s.substring(0, i);
            if (rot.compareTo(best) < 0) {
                best = rot;
            }
        }
        return best;
    }

    // V3
    // IDEA: DOUBLE THE STRING, then slide a length-n window
    /**
     *  Every rotation of s is a length-n substring of s + s, so the rotations can
     *  be compared by index into ONE doubled buffer -- no per-rotation
     *  concatenation.
     *
     *  The standard `cyclic string` trick; it also makes the k > 1 branch and the
     *  k == 1 branch share a single buffer.
     *
     *  time  = O(n^2) comparisons but O(n) allocation
     *  space = O(n)
     */
    public String orderlyQueue_3(String s, int k) {
        if (k > 1) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            return new String(arr);
        }

        int n = s.length();
        String doubled = s + s;
        int best = 0;
        for (int i = 1; i < n; i++) {
            // compare the windows starting at `best` and at `i`, character by character
            for (int t = 0; t < n; t++) {
                char a = doubled.charAt(best + t);
                char b = doubled.charAt(i + t);
                if (a != b) {
                    if (b < a) {
                        best = i;
                    }
                    break;
                }
            }
        }
        return doubled.substring(best, best + n);
    }

}
