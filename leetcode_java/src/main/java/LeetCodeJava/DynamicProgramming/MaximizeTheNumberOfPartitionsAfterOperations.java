package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-the-number-of-partitions-after-operations/description/

import java.util.*;

/**
 * 3003. Maximize the Number of Partitions After Operations
 * Hard
 *
 * You are given a 0-indexed string s and an integer k.
 *
 * You are to perform the following partitioning operations until s is empty:
 *
 * Choose the longest prefix of s containing at most k distinct characters.
 * Delete the prefix from s and increase the number of partitions by one. The remaining characters (if any) in s maintain their initial order.
 *
 * Before the operations, you are allowed to change at most one index in s to another lowercase English letter.
 *
 * Return an integer denoting the maximum number of resulting partitions after the operations by optimally choosing at most one index to change.
 *
 *
 * Example 1:
 *
 * Input: s = "accca", k = 2
 * Output: 3
 * Explanation: In this example, to maximize the number of resulting partitions, s[2] can be changed to 'b'.
 * s becomes "acbca".
 * The operations can now be performed as follows until s becomes empty:
 * - Choose the longest prefix containing at most 2 distinct characters, "acbca".
 * - Delete the prefix, and s becomes "bca". The number of partitions is now 1.
 * - Choose the longest prefix containing at most 2 distinct characters, "bca".
 * - Delete the prefix, and s becomes "a". The number of partitions is now 2.
 * - Choose the longest prefix containing at most 2 distinct characters, "a".
 * - Delete the prefix, and s becomes empty. The number of partitions is now 3.
 * Hence, the answer is 3.
 * It can be shown that it is not possible to obtain more than 3 partitions.
 *
 * Example 2:
 *
 * Input: s = "aabaab", k = 3
 * Output: 1
 * Explanation: In this example, to maximize the number of resulting partitions we can leave s as it is.
 * The operations can now be performed as follows until s becomes empty:
 * - Choose the longest prefix containing at most 3 distinct characters, "aabaab".
 * - Delete the prefix, and s becomes empty. The number of partitions becomes 1.
 * Hence, the answer is 1.
 * It can be shown that it is not possible to obtain more than 1 partition.
 *
 * Example 3:
 *
 * Input: s = "xxyz", k = 1
 * Output: 4
 * Explanation: In this example, to maximize the number of resulting partitions, s[1] can be changed to 'a'.
 * s becomes "xayz".
 * The operations can now be performed as follows until s becomes empty:
 * - Choose the longest prefix containing at most 1 distinct character, "xayz".
 * - Delete the prefix, and s becomes "ayz". The number of partitions is now 1.
 * - Choose the longest prefix containing at most 1 distinct character, "ayz".
 * - Delete the prefix, and s becomes "yz". The number of partitions is now 2.
 * - Choose the longest prefix containing at most 1 distinct character, "yz".
 * - Delete the prefix, and s becomes "z". The number of partitions is now 3.
 * - Choose the longest prefix containing at most 1 distinct character, "z".
 * - Delete the prefix, and s becomes empty. The number of partitions is now 4.
 * Hence, the answer is 4.
 * It can be shown that it is not possible to obtain more than 4 partitions.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s consists only of lowercase English letters.
 * 1 <= k <= 26
 *
 */
public class MaximizeTheNumberOfPartitionsAfterOperations {

    // V0
    // IDEA: MEMOIZED DFS OVER (POSITION, LETTERS IN CURRENT SEGMENT, EDIT USED)
    //   (recursion goes n deep: fine on LC, but V1 is the iterative version for a small stack)
    //   The greedy partitioning is deterministic; the only choice is where to spend the
    //   single edit. So the edit becomes part of the state:
    //     i = position being consumed, mask = 26-bit set of letters in the open segment,
    //     changed = whether the edit has been used.
    //   At i try the real letter (and, if the edit is unused, every other letter):
    //     fits (popcount(mask | bit) <= k) -> stay in the segment
    //     overflows                          -> close the segment (+1), the letter opens a new one
    //   Reachable (i, mask) pairs stay small because masks only grow and reset at each cut.
    /**
     * time = O(R * 26), R = reachable states
     * space = O(R)
     */
    public int maxPartitionsAfterOperations(String s, int k) {
        Map<Long, Integer> memo = new HashMap<>();
        return dfs(s, k, 0, 0, false, memo);
    }

    private int dfs(String s, int k, int i, int mask, boolean changed, Map<Long, Integer> memo) {
        // the final, still-open segment counts as one partition
        if (i == s.length()) {
            return 1;
        }
        // pack (i, mask, changed) into one long key: i | 26 mask bits | 1 flag bit
        long key = ((long) i << 27) | ((long) mask << 1) | (changed ? 1L : 0L);
        Integer cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int current = s.charAt(i) - 'a';
        int best = 0;
        for (int letter = 0; letter < 26; letter++) {
            // once the edit is spent only the real letter is allowed
            if (changed && letter != current) {
                continue;
            }
            boolean usedEdit = changed || letter != current;
            int bit = 1 << letter;
            int nextMask = mask | bit;
            int result;
            if (Integer.bitCount(nextMask) > k) {
                // too many distinct letters: cut before i, the letter starts a new segment
                result = 1 + dfs(s, k, i + 1, bit, usedEdit, memo);
            } else {
                result = dfs(s, k, i + 1, nextMask, usedEdit, memo);
            }
            best = Math.max(best, result);
        }

        memo.put(key, best);
        return best;
    }

    // V1
    // IDEA: PREFIX + SUFFIX GREEDY PARTITIONS, TRY EACH POSITION AS THE EDIT  (editorial)
    //   Iterative, so no recursion depth issue (V0 recurses n = 10^4 deep).
    //   prefix state at i = greedy partition of s[0 .. i-1]: completed segments, letters of the
    //   open segment, and how many distinct letters it holds. suffix state at i = the same for
    //   s[i+1 .. n-1], scanned right to left.
    //   With s[i] free to change, the base count is prefixDone + suffixDone + 2, then:
    //     both open segments are full (k distinct) and some letter is in neither ->
    //       s[i] becomes that letter and stands alone: +1
    //     the two open segments plus s[i] fit in k letters -> they merge into one: -1
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxPartitionsAfterOperations_1(String s, int k) {
        int n = s.length();
        // {completed segments, mask of the open segment, distinct letters in the open segment}
        int[][] prefix = new int[n][3];
        int[][] suffix = new int[n][3];

        int done = 0;
        int mask = 0;
        int distinct = 0;
        for (int i = 0; i < n - 1; i++) {
            int bit = 1 << (s.charAt(i) - 'a');
            if ((mask & bit) == 0) {
                distinct++;
                if (distinct <= k) {
                    mask |= bit;
                } else {
                    // the open segment overflows: close it, s[i] starts a new one
                    done++;
                    mask = bit;
                    distinct = 1;
                }
            }
            prefix[i + 1][0] = done;
            prefix[i + 1][1] = mask;
            prefix[i + 1][2] = distinct;
        }

        done = 0;
        mask = 0;
        distinct = 0;
        for (int i = n - 1; i > 0; i--) {
            int bit = 1 << (s.charAt(i) - 'a');
            if ((mask & bit) == 0) {
                distinct++;
                if (distinct <= k) {
                    mask |= bit;
                } else {
                    done++;
                    mask = bit;
                    distinct = 1;
                }
            }
            suffix[i - 1][0] = done;
            suffix[i - 1][1] = mask;
            suffix[i - 1][2] = distinct;
        }

        int best = 0;
        for (int i = 0; i < n; i++) {
            int segments = prefix[i][0] + suffix[i][0] + 2;
            int unionCount = Integer.bitCount(prefix[i][1] | suffix[i][1]);
            boolean bothFull = prefix[i][2] == k && suffix[i][2] == k;
            if (bothFull && unionCount < 26) {
                // a letter outside both open segments isolates position i
                segments += 1;
            } else if (Math.min(unionCount + 1, 26) <= k) {
                // left part, s[i] and right part all fit in one segment
                segments -= 1;
            }
            best = Math.max(best, segments);
        }
        return best;
    }
}
