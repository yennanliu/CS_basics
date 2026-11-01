package LeetCodeJava.BFS;

// https://leetcode.com/problems/lexicographically-smallest-string-after-applying-operations/description/

import java.util.*;

/**
 * 1625. Lexicographically Smallest String After Applying Operations
 * Medium
 *
 * You are given a string s of even length consisting of digits from 0 to 9, and two integers a and b.
 *
 * You can apply either of the following two operations any number of times and in any order on s:
 *
 * - Add a to all odd indices of s (0-indexed). Digits post 9 are cycled back to 0.
 *   For example, if s = "3456" and a = 5, s becomes "3951".
 * - Rotate s to the right by b positions. For example, if s = "3456" and b = 1, s becomes "6345".
 *
 * Return the lexicographically smallest string you can obtain by applying the above operations
 * any number of times on s.
 *
 * A string a is lexicographically smaller than a string b (of the same length) if in the first
 * position where a and b differ, string a has a letter that appears earlier in the alphabet than
 * the corresponding letter in b. For example, "0158" is lexicographically smaller than "0190"
 * because the first position they differ is at the third letter, and '5' comes before '9'.
 *
 *
 * Example 1:
 *
 * Input: s = "5525", a = 9, b = 2
 * Output: "2050"
 * Explanation: We can apply the following operations:
 * Start:  "5525"
 * Rotate: "2555"
 * Add:    "2454"
 * Add:    "2353"
 * Rotate: "5323"
 * Add:    "5222"
 * Add:    "5121"
 * Rotate: "2151"
 * Add:    "2050"
 * There is no way to obtain a string that is lexicographically smaller than "2050".
 *
 * Example 2:
 *
 * Input: s = "74", a = 5, b = 1
 * Output: "24"
 * Explanation: We can apply the following operations:
 * Start:  "74"
 * Rotate: "47"
 * Add:    "42"
 * Rotate: "24"
 * There is no way to obtain a string that is lexicographically smaller than "24".
 *
 * Example 3:
 *
 * Input: s = "0011", a = 4, b = 2
 * Output: "0011"
 * Explanation: There are no sequence of operations that will give us a lexicographically
 * smaller string than "0011".
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 100
 * s.length is even.
 * s consists of digits from 0 to 9 only.
 * 1 <= a <= 9
 * 1 <= b <= s.length - 1
 *
 */
public class LexicographicallySmallestStringAfterApplyingOperations {

    // V0
    // IDEA: ENUMERATE THE REACHABLE SET (ROTATIONS x INDEPENDENT DIGIT SHIFTS)
    //  any reachable string = rotate right by some multiple of b, then add i * a to the
    //  odd positions (i in 0..9, since 10 * a == 0 mod 10).
    //  If b is ODD a rotation swaps index parity, so the EVEN positions can also be
    //  shifted by j * a independently; if b is even they never change.
    /**
     * time = O(N^2 * 100)
     * space = O(N)
     */
    public String findLexSmallestString(String s, int a, int b) {
        int n = s.length();
        int[] digits = new int[n];
        for (int i = 0; i < n; i++) {
            digits[i] = s.charAt(i) - '0';
        }
        // even positions are only reachable for shifting when b is odd
        int evenShiftCount = (b % 2 == 1) ? 10 : 1;

        String best = null;
        boolean[] seenRotation = new boolean[n];
        int rotation = 0;
        // reachable rotations are the multiples of b mod n; stop once they cycle
        while (!seenRotation[rotation]) {
            seenRotation[rotation] = true;

            // rotate right by `rotation`: new index i takes old index (i - rotation) mod n
            int[] rotated = new int[n];
            for (int i = 0; i < n; i++) {
                rotated[i] = digits[(i - rotation + n) % n];
            }

            for (int oddShift = 0; oddShift < 10; oddShift++) {
                for (int evenShift = 0; evenShift < evenShiftCount; evenShift++) {
                    StringBuilder candidate = new StringBuilder();
                    for (int i = 0; i < n; i++) {
                        int times = (i % 2 == 1) ? oddShift : evenShift;
                        candidate.append((rotated[i] + times * a) % 10);
                    }
                    String candidateString = candidate.toString();
                    if (best == null || candidateString.compareTo(best) < 0) {
                        best = candidateString;
                    }
                }
            }
            rotation = (rotation + b) % n;
        }
        return best;
    }

    // V1
    // IDEA: BFS OVER EVERY REACHABLE STRING
    //  a node is a string, the two operations are its two edges. The state space is
    //  at most n * 10 * 10 strings, so exploring all of them and keeping the minimum passes.
    /**
     * time = O(N^2 * 100)
     * space = O(N^2 * 100)
     */
    public String findLexSmallestString_1(String s, int a, int b) {
        Set<String> visited = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.offer(s);
        visited.add(s);
        String best = s;

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.compareTo(best) < 0) {
                best = current;
            }
            String added = addToOddIndices(current, a);
            String rotated = rotateRight(current, b);
            if (visited.add(added)) {
                queue.offer(added);
            }
            if (visited.add(rotated)) {
                queue.offer(rotated);
            }
        }
        return best;
    }

    /** Adds a (mod 10) to every digit at an odd index. */
    private String addToOddIndices(String s, int a) {
        char[] chars = s.toCharArray();
        for (int i = 1; i < chars.length; i += 2) {
            int digit = chars[i] - '0';
            chars[i] = (char) ('0' + (digit + a) % 10);
        }
        return new String(chars);
    }

    /** Rotates s to the right by b positions. */
    private String rotateRight(String s, int b) {
        int n = s.length();
        return s.substring(n - b) + s.substring(0, n - b);
    }
}
