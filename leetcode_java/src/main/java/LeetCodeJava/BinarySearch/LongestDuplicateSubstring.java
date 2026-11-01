package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-duplicate-substring/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 1044. Longest Duplicate Substring
 * Hard
 *
 * Given a string s, consider all duplicated substrings: (contiguous) substrings of s
 * that occur 2 or more times. The occurrences may overlap.
 *
 * Return any duplicated substring that has the longest possible length.
 * If s does not have a duplicated substring, the answer is "".
 *
 *
 * Example 1:
 *
 * Input: s = "banana"
 * Output: "ana"
 *
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: ""
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 3 * 10^4
 * s consists of lowercase English letters.
 *
 */
public class LongestDuplicateSubstring {

    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 26L;

    // V0
    // IDEA: BINARY SEARCH ON LENGTH + RABIN-KARP (ROLLING HASH)
    /**
     *  monotonic property:
     *    if a duplicated substring of length L exists, one of length L - 1 exists too
     *    (drop its last char) -> binary search the largest feasible L.
     *
     *  for a fixed L, sweep all n - L + 1 windows with a rolling hash. On a hash hit we
     *  still compare the real substrings, so a collision can never produce a wrong answer.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String longestDupSubstring(String s) {
        int n = s.length();
        String best = "";

        // search the largest length in [1, n - 1] that has a duplicate
        int left = 1;
        int right = n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int start = findDuplicateStart(s, mid);
            if (start != -1) {
                // a duplicate of length mid exists -> record it and try longer
                best = s.substring(start, start + mid);
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return best;
    }

    /** Returns the start index of some duplicated substring of the given length, or -1. */
    private int findDuplicateStart(String s, int length) {
        int n = s.length();

        // hash of the first window
        long hash = 0;
        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + (s.charAt(i) - 'a')) % MOD;
        }

        // BASE^length, used to remove the char leaving the window
        long power = 1;
        for (int i = 0; i < length; i++) {
            power = (power * BASE) % MOD;
        }

        // hash -> start indices of windows with that hash
        Map<Long, List<Integer>> seen = new HashMap<>();
        seen.computeIfAbsent(hash, x -> new ArrayList<>()).add(0);

        for (int start = 1; start + length <= n; start++) {
            // roll: shift left, drop the old first char, add the new last char
            long outgoing = (s.charAt(start - 1) - 'a') * power % MOD;
            long incoming = s.charAt(start + length - 1) - 'a';
            hash = (hash * BASE - outgoing + MOD) % MOD;
            hash = (hash + incoming) % MOD;

            List<Integer> candidates = seen.get(hash);
            if (candidates != null) {
                // guard against hash collisions with a real comparison
                for (int prevStart : candidates) {
                    if (s.regionMatches(prevStart, s, start, length)) {
                        return start;
                    }
                }
            }
            seen.computeIfAbsent(hash, x -> new ArrayList<>()).add(start);
        }
        return -1;
    }

    // V1
    // IDEA: BINARY SEARCH ON LENGTH + HASH SET OF SUBSTRINGS
    // simpler to say in an interview, but each substring costs O(L) to build and hash,
    // so it is TLE-prone on the largest inputs
    /**
     * time = O(N^2 log N)
     * space = O(N^2)
     */
    public String longestDupSubstring_1(String s) {
        int n = s.length();
        String best = "";
        int left = 1;
        int right = n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String duplicate = findDuplicate(s, mid);
            if (!duplicate.isEmpty()) {
                best = duplicate;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return best;
    }

    /** Returns some substring of the given length that occurs twice, or "". */
    private String findDuplicate(String s, int length) {
        Set<String> seen = new HashSet<>();
        for (int i = 0; i + length <= s.length(); i++) {
            String window = s.substring(i, i + length);
            if (seen.contains(window)) {
                return window;
            }
            seen.add(window);
        }
        return "";
    }
}
