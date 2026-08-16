package LeetCodeJava.BFS;

// https://leetcode.com/problems/k-similar-strings/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 854. K-Similar Strings
 * Hard
 *
 * Strings s1 and s2 are k-similar (for some non-negative integer k) if we can swap
 * the positions of two letters in s1 exactly k times so that the resulting string
 * equals s2.
 *
 * Given two anagrams s1 and s2, return the smallest k for which s1 and s2 are k-similar.
 *
 *
 * Example 1:
 *
 * Input: s1 = "ab", s2 = "ba"
 * Output: 1
 * Explanation: The two string are 1-similar because we can use one swap to change
 * s1 to s2: "ab" --> "ba".
 *
 * Example 2:
 *
 * Input: s1 = "abc", s2 = "bca"
 * Output: 2
 * Explanation: The two strings are 2-similar because we can use two swaps to change
 * s1 to s2: "abc" --> "bac" --> "bca".
 *
 *
 * Constraints:
 *
 * 1 <= s1.length <= 20
 * s2.length == s1.length
 * s1 and s2 contain only lowercase letters from the set {'a','b','c','d','e','f'}.
 * s2 is an anagram of s1.
 *
 */
public class KSimilarStrings {

    // V0
    // IDEA: BFS with GREEDY PRUNING
    /**
     *   BFS over strings, ONE swap = ONE edge, so the first time we reach s2 the
     *   level IS the minimum number of swaps.
     *
     *   The naive branching factor (all C(n,2) swaps) EXPLODES. Two prunings make
     *   it tractable AND stay optimal:
     *
     *     1) Always fix the LEFTMOST mismatching index i.
     *        Some swap must eventually place s2[i] at position i, and doing it now
     *        never costs more, so we only branch on swaps touching i.
     *
     *     2) Only swap in a j with cur[j] == s2[i] AND cur[j] != s2[j].
     *        Moving a character that is ALREADY in its correct place would just
     *        have to be undone later.
     *
     *   time  = O(n * n! / ...) in theory, but pruning keeps it small for n <= 20
     *   space = O(number of visited states)
     */
    public int kSimilarity(String s1, String s2) {
        if (s1.equals(s2)) {
            return 0;
        }

        int n = s1.length();

        Deque<String> queue = new ArrayDeque<>();
        queue.offer(s1);

        Set<String> visited = new HashSet<>();
        visited.add(s1);

        int steps = 0;

        while (!queue.isEmpty()) {
            steps += 1;

            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                String cur = queue.poll();

                /** NOTE !!! pruning 1)
                 *
                 *  jump straight to the LEFTMOST position that is still wrong
                 *  -> everything before it is already settled
                 */
                int i = 0;
                while (cur.charAt(i) == s2.charAt(i)) {
                    i += 1;
                }

                for (int j = i + 1; j < n; j++) {
                    /** NOTE !!! pruning 2)
                     *
                     *  bring the RIGHT character to i,
                     *  and DON'T disturb a character already in place
                     */
                    if (cur.charAt(j) != s2.charAt(i) || cur.charAt(j) == s2.charAt(j)) {
                        continue;
                    }

                    char[] arr = cur.toCharArray();
                    char tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                    String nxt = new String(arr);

                    if (nxt.equals(s2)) {
                        return steps;
                    }
                    if (visited.add(nxt)) {
                        queue.offer(nxt);
                    }
                }
            }
        }

        return -1;
    }

}
