package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-good-caption/description/

/**
 * 3441. Minimum Cost Good Caption
 * Hard
 *
 * You are given a string caption of length n. A good caption is a string where every
 * character appears in groups of at least 3 consecutive occurrences.
 *
 * For example:
 *
 * "aaabbb" and "aaaaccc" are good captions.
 * "aabbb" and "ccccd" are not good captions.
 *
 * You can perform the following operation any number of times:
 *
 * Choose an index i (where 0 <= i < n) and change the character at that index to either:
 *
 * The character immediately before it in the alphabet (if caption[i] != 'a').
 * The character immediately after it in the alphabet (if caption[i] != 'z').
 *
 * Your task is to convert the given caption into a good caption using the minimum number
 * of operations, and return it. If there are multiple possible good captions, return the
 * lexicographically smallest one among them. If it is impossible to create a good caption,
 * return an empty string "".
 *
 *
 * Example 1:
 *
 * Input: caption = "cdcd"
 * Output: "cccc"
 * Explanation: "dddd" and "cccc" both take 2 operations; "cccc" is lexicographically smaller.
 *
 * Example 2:
 *
 * Input: caption = "aca"
 * Output: "aaa"
 *
 * Example 3:
 *
 * Input: caption = "bc"
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= caption.length <= 5 * 10^4
 * caption consists only of lowercase English letters.
 *
 */
public class MinimumCostGoodCaption {

    private static final int ALPHABET = 26;
    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: SUFFIX DP WITH TWO STATES (OPEN A RUN / CONTINUE A RUN), THEN GREEDY LEX-SMALLEST REPLAY
    /**
     *  cost(i, c) = |caption[i] - c|; the only coupling is "every run has length >= 3".
     *
     *  DP def:
     *     - bestStart[i] = cheapest suffix from i when a FRESH run opens at i
     *                      (any letter; the run covers at least i, i+1, i+2)
     *     - cont[j][c]   = cheapest suffix from j when a run of c already has its
     *                      three characters, so j may extend it or open a new run
     *
     *  DP eq:
     *     - bestStart[i] = min over c of cost(i,c) + cost(i+1,c) + cost(i+2,c) + cont[i+3][c]
     *     - cont[j][c]   = min( bestStart[j], cost(j,c) + cont[j+1][c] )
     *     - base: cont[n][c] = 0, bestStart[n] = 0
     *     (a new run may reuse the same letter: two adjacent runs just merge)
     *
     *  Replay: walk forward carrying the exact remaining budget; at each position try
     *  letters a..z and keep the first whose cost plus the resulting state's value
     *  EQUALS the budget -- that is exactly "still on an optimal path".
     *
     *  time  = O(26 * N)
     *  space = O(26 * N)
     */
    public String minCostGoodCaption(String caption) {
        int n = caption.length();
        if (n < 3) {
            return "";
        }
        int[] letters = new int[n];
        for (int i = 0; i < n; i++) {
            letters[i] = caption.charAt(i) - 'a';
        }

        int[][] cont = new int[n + 1][ALPHABET];
        int[] bestStart = new int[n + 1];
        java.util.Arrays.fill(bestStart, INF);
        bestStart[n] = 0;

        for (int j = n - 1; j >= 0; j--) {
            // open a fresh run of 3 at j (needs 3 characters left)
            if (j + 3 <= n) {
                int best = INF;
                for (int c = 0; c < ALPHABET; c++) {
                    int value = cost(letters, j, c) + cost(letters, j + 1, c) + cost(letters, j + 2, c)
                            + cont[j + 3][c];
                    best = Math.min(best, value);
                }
                bestStart[j] = best;
            }
            // a completed run of c at j: extend it, or open a new run here
            for (int c = 0; c < ALPHABET; c++) {
                int extend = cost(letters, j, c) + cont[j + 1][c];
                cont[j][c] = Math.min(bestStart[j], extend);
            }
        }

        if (bestStart[0] >= INF) {
            return "";
        }

        // replay forward, choosing the smallest letter that keeps us on an optimal path
        StringBuilder result = new StringBuilder();
        int budget = bestStart[0];
        int prevLetter = -1;
        int runLength = 0;
        int i = 0;
        while (i < n) {
            // a run shorter than 3 is forced to continue with the same letter
            boolean forced = runLength > 0 && runLength < 3;
            int firstLetter = forced ? prevLetter : 0;
            int lastLetter = forced ? prevLetter : ALPHABET - 1;

            for (int x = firstLetter; x <= lastLetter; x++) {
                int newRun;
                if (x != prevLetter) {
                    newRun = 1;
                } else {
                    newRun = Math.min(runLength + 1, 3);
                }
                int stepCost = cost(letters, i, x);
                int need = stepCost + suffixValue(cont, letters, n, i + 1, x, newRun);
                if (need == budget) {
                    result.append((char) ('a' + x));
                    budget -= stepCost;
                    prevLetter = x;
                    runLength = newRun;
                    break;
                }
            }
            i++;
        }
        return result.toString();
    }

    /** Cheapest suffix from j given a current run of letter c of length runLength (capped at 3). */
    private int suffixValue(int[][] cont, int[] letters, int n, int j, int c, int runLength) {
        if (runLength == 3) {
            return cont[j][c];
        }
        if (runLength == 2) {
            // one more c is required at j
            if (j >= n) {
                return INF;
            }
            return cost(letters, j, c) + cont[j + 1][c];
        }
        // runLength == 1: two more c are required at j and j+1
        if (j + 1 >= n) {
            return INF;
        }
        return cost(letters, j, c) + cost(letters, j + 1, c) + cont[j + 2][c];
    }

    /** Number of operations to turn the letter at position j into letter c. */
    private int cost(int[] letters, int j, int c) {
        return Math.abs(letters[j] - c);
    }
}
